/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/10/28		SSC				課題No.06 国内一括取込対応
003		2011/06/30		SSC				案件No.D9059 もぎ取り解除時に記入内容をクリアしない
004		2015/02/23		SSC				案件No.BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.bss;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.dbAcc.TorokuDbAcc;
import app.tairyu.form.TorokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;
import org.apache.struts.upload.FormFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OB1102_実質滞留債権判定_明細一覧 ビジネスロジッククラス <br>
 */
public class TorokuBss {

    private static final String FLG_ICHIJI_SATEI_1      = "1";                  // 一次査定対象FLG
    private static final String TAIRYU_HANTEI_OVER6     = "1";                  // 滞留判定 「6ヶ月超滞留」
    private static final String PROCESS_METHOD          = "process";            // 処理メソッド名
    private static final String INSERT_KBN_MT_KAIJYO    = "40";                 // 入力区分 '40': もぎ取り解除
    private static final String INSERT_KBN_REGIST       = "10";                 // 入力区分 '10': 登録
    private static final String INSERT_KBN_TMP_SAVE     = "20";                 // 入力区分 '20': 一時保存
    private static final String ERROR_0059              = "err.0059";           // エラーメッセージキー
    private static final String DOTCSV                  = ".csv";
    private static final String STR_N                   = "\\\\n";
    private static final String KAIGYO                  = "\r\n";
    private static final int MAX_LEN_HANTEI_JIYU       =1000;	// 判定事由の最大バイト数
    private static final String SJIS						= "SJIS";
    private static final String WINDOWS_31J				= "Windows-31J";

    private SessionData cmnData;            // 共通セッション
    private UserBean user_bean;             // ユーザー情報
    private TorihikisakiBean tori_bean;     // 取引先情報
    private TorokuForm form;                // アクションフォーム

    private AppContext appContext       = null; // ＡＰＰコンテキスト
    private SqlExecuter sqlExec         = null; // ＤＢアクセス
    private TorokuDbAcc dbacc			 = null; // SQL実行
    private Log log                     = null; // LOG

    /**
     * コンストラクタ <br>
     *
     * @param appContext AppContext
     * @throws Exception
     */
    public TorokuBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.tori_bean = cmnData.getTori_bean();
        this.form = (TorokuForm) appContext.getActionForm();
        this.sqlExec = appContext.getSqlExecuter();
        this.dbacc = new TorokuDbAcc(sqlExec, log, appContext);
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時) <br>
     *
     * @return forward
     * @throws Exception
     */
    public String executeInit() throws Exception {

        // 実施業務
        this.getOparation();

        // 次フェーズが滞留判定承認/検証承認の時のみ表示する。
        if (GS.PHASE_TAIRYU_HANTEI.equals(form.getJi_jishi_phase())
				|| GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(form.getJi_jishi_phase())) {
	        // 次開始ステータスがの’30’:承認待ちの場合、承認担当者のリストを取得する。
	        if (GS.STATUS_SYONIN_MACHI.equals(form.getJi_kaishi_status())) {
	            // 承認担当者セレクトボックスを表示にする
	            form.setFlg_disp_tanto(GS.ON);
	            // 承認担当者セレクトボックス値取得
	            dbacc.getTanto();
	        }
        }

        // 表示件数セレクトボックス値取得
        dbacc.getShow();
        // 滞留判定セレクトボックス値取得
        dbacc.getTairyuJdg();
        // ボタンの初期化
        this.initButton();

        // 前回実施案件No.取得
        String preAnkenNo = dbacc.getPreAnkenNo();
        // 一覧情報取得
        if(dbacc.getMeisaiList(preAnkenNo,tori_bean.getPhase())){
        	// 滞留判定検証の初期表示時は滞留判定の登録内容を表示
        	dbacc.getMeisaiList(preAnkenNo,GS.PHASE_TAIRYU_HANTEI);
        }

        return GS.OB1102;
    }

    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     *
     * @return forward
     * @throws Exception
     */
    public String execute() throws Exception {
        // 前回実施案件No.取得
        String preAnkenNo = dbacc.getPreAnkenNo();
        // 一覧情報取得
        if(dbacc.getMeisaiList(preAnkenNo,tori_bean.getPhase())){
        	// 滞留判定検証の初期表示時は滞留判定の登録内容を表示
        	dbacc.getMeisaiList(preAnkenNo,GS.PHASE_TAIRYU_HANTEI);
        }

        return GS.OB1102;
    }

    /**
     * 共)実施業務【リスト】より、実施フェーズが共)取引先情報.フェーズ、開始ステータスが'10'の実施業務を取得する。 <br>
     */
    private void getOparation() {
        // 共)実施業務【リスト】
        List comOparation = user_bean.getComOparation();
        if (comOparation == null) {
            return;
        }
        for (Object oparation : comOparation) {
            // 実施業務
            Map mapOparation = (Map) oparation;
            // 実施フェーズ
            Object phase = mapOparation.get(GS.JISHI_PHASE);
            // 開始ステータス
            Object status = mapOparation.get(GS.KAISHI_STATUS);

            // 次開始ステータスを取得する。
            if (phase != null && phase.equals(tori_bean.getPhase()) && GS.STATUS_MISYORI.equals(status)) {
                // 次実施フェーズ
                form.setJi_jishi_phase((String)mapOparation.get(GS.JI_JISHI_PHASE));
                // 次開始ステータス
                form.setJi_kaishi_status((String) mapOparation.get(GS.JI_KAISHI_STATUS));
            }
        }
    }

    /**
     * ボタンの初期化 <br>
     */
    private void initButton() {
        // 共)取引先情報.フェーズが’20’：滞留判定検証の時のみ表示する。
        if (GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(tori_bean.getPhase())) {
            form.setFlg_disp_sashimodoshi(GS.ON);
        }
        //課題No.06
        //削除開始
        // 共)取引先情報.システム区分が’02’：FOCUS、または’03’：MTSの時のみ表示する。
        //String system_kbn = tori_bean.getSystem_kbn();
        //if (GS.FOCUS.equals(system_kbn) || GS.MTS.equals(system_kbn)) {
        	//form.setFlg_disp_upload_all(GS.ON);
        //}
        //削除完了

        // 共)取引先情報.差戻転送FLGがNULL or 空文字ではない場合。
        if (!(tori_bean.getSasi_ten_flg() == null) && !(GS.EMPTY_CHARCTER.equals(tori_bean.getSasi_ten_flg()))) {
            form.setFlg_disp_comment(GS.ON);
        }
    }

    /**
     * もぎ取り解除処理 <br>
     *
     * @throws Exception
     */
    public void doKaijyo() throws Exception {
    	// 案件No.D9059 もぎ取り解除時に記入内容をクリアしない
        // T10_滞留判定の更新(滞留判定と判定事由をNULLで更新)
    	//dbacc.updT10Kaijo();
        // T08_滞留判定進捗管理の更新
        dbacc.updT08MogitoriKaijyo();
        // T13_入力履歴の登録
        dbacc.insT13(INSERT_KBN_MT_KAIJYO);
        dbacc.commit();
    }

    /**
     * 一時保存処理(入力履歴なし) <br>
     *
     * @throws Exception
     */
    public void doSaveNyuryoku(boolean flg) throws Exception {
    	// T10_滞留判定の更新（件数分ループ）
    	if(flg){
            dbacc.updT10();
    	}else{
    		//滞留判定セレクトボックス変更時
            dbacc.updT10_2();
    	}
        // T08_滞留判定進捗管理の更新
        dbacc.updT08Save();
        dbacc.commit();
    }

    /**
     * 一時保存処理 <br>
     *
     * @throws Exception
     */
    public void doTempSave() throws Exception {
        // T10_滞留判定の更新（件数分ループ）
        dbacc.updT10();
        // T13_入力履歴の登録
        dbacc.insT13(INSERT_KBN_TMP_SAVE);
        // T08_滞留判定進捗管理の更新
        dbacc.updT08Save();
        dbacc.commit();
    }

    /**
     * 登録処理 <br>
     *
     * @return 登録成功フラグ
     * @throws Exception
     */
    public boolean doRegist() throws Exception {
        // 実施業務
        this.getOparation();
        // 処理リストを取得する。
        boolean[] ar_process_flg = this.getProcessFlg();
        // 該当登録処理を行う。
        for (int i = 0; i < ar_process_flg.length; i++) {
            // 処理フラグによって、判断を行う。
            if (ar_process_flg[i]) {
                // メソッドを取得
                Method method = this.getClass().getDeclaredMethod(PROCESS_METHOD + (i + 1));
                method.setAccessible(true);
                // 実行
                Boolean hasError = (Boolean) method.invoke(this);
                if (hasError) {
                	if (i == 9 || i == 10) {
                		// この二つチェックにはエラーがあったら、後の処理は行わない。
						break;
					}
                    // 処理終了の場合
                    return false;
                }
            }
        }
        dbacc.commit();
        // 登録成功の場合
        return true;
    }

    /**
     * 処理リストを取得する。 <br>
     */
    private boolean[] getProcessFlg() {

        // 滞留判定承認と滞留判定検証承認
        boolean[] ar_process_shonin = { true, true, false, true, false, true, false, false, true,
                false, false, false, false, true };
        // 滞留判定検証登録
        boolean[] ar_process_toroku = { true, false, false, true, true, false, true, false, true,
                false, false, false, false, true };
        // 一次査定登録
        boolean[] ar_process_satei = { true, false, true, true, false, false, false, true, true,
                true, true, true, true, true };

        if (GS.PHASE_TAIRYU_HANTEI.equals(form.getJi_jishi_phase())
                && GS.STATUS_SYONIN_MACHI.equals(form.getJi_kaishi_status())) {
            // 滞留判定承認
            return ar_process_shonin;
        } else if (GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(form.getJi_jishi_phase())
                && GS.STATUS_SYONIN_MACHI.equals(form.getJi_kaishi_status())) {
            // 滞留判定検証承認
            return ar_process_shonin;
        } else if (GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(form.getJi_jishi_phase())
                && GS.STATUS_MISYORI.equals(form.getJi_kaishi_status())) {
            // 滞留判定検証登録
            return ar_process_toroku;
        } else if (GS.PHASE_ICHIJI_SATEI.equals(form.getJi_jishi_phase())
                && GS.STATUS_MISYORI.equals(form.getJi_kaishi_status())) {
            // 一次査定登録
            return ar_process_satei;
        }
        return null;
    }

    /**
     * 取り込み処理 <br>
     *
     * @return 登録成功フラグ
     * @throws Exception
     */
    public boolean doTorikomi() throws Exception {
		FormFile file = form.getFileUp();
		String fileNm = file.getFileName();
    	//ファイル選択チェック
    	if(!chkFileNm(fileNm)){
    		return false;
    	}
		//拡張子チェック
		if (!chkExtension(fileNm)) {
        	return false;
		}
    	//禁止文字チェック
    	if(!chkKinshiMoji(fileNm)){
    		return false;
    	}
    	//CSV読み込み
    	List<String> line_list = readData(file);

    	//CSVデータチェック
    	if(!chkAnkenNo(line_list)){
    		return false;
    	}

    	//T10_滞留判定の更新
    	this.updIkkatu(line_list);
        //T13_入力履歴の登録
        dbacc.insT13(INSERT_KBN_TMP_SAVE);
        //T08_滞留判定進捗管理の更新
        dbacc.updT08Save();

    	//コミット
    	dbacc.commit();

    	//ファイル破棄
		file.destroy();

		//一覧情報再取得
		execute();
    	return true;
    }

	/**
	 * ファイル選択チェック
	 */
	public boolean chkFileNm(String file_nm) throws Exception {
		InputCheck check = new InputCheck();
    	if(check.isNullBlank(file_nm)){
	    	appContext.setMsgCode(ERROR_0059);
    		return false;
    	}
		return true;
	}

	/**
	 * 拡張子チェック
	 */
	public boolean chkExtension(String file_nm) throws Exception {
		if (!(file_nm.endsWith(DOTCSV) || file_nm.endsWith(DOTCSV.toUpperCase()))) {
        	appContext.setMsgCode(GL.WARNING_EXTENSIONJAVASCRIPT);
        	return false;
		}
		return true;
	}

	/**
	 * 入力禁止文字チェック
	 */
	public boolean chkKinshiMoji(String file_nm) throws Exception {
		InputCheck check = new InputCheck();
	    // 入力禁止文字が含まれている場合エラー
		if (check.haveKinshiMoji(file_nm)) {
			for (int i = 0; i < file_nm.length(); i++) {
				String kinshiChar = file_nm.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * CSV読み込み
	 */
	public List<String> readData(FormFile file) throws Exception {
    	List<String> line_list = new ArrayList<String>();
    	String line = null;
    	InputStream is = form.getFileUp().getInputStream();
		BufferedReader reader = null;

		try {
    		reader = new BufferedReader(new InputStreamReader(is,WINDOWS_31J));
		} catch (UnsupportedEncodingException e){
    		reader = new BufferedReader(new InputStreamReader(is,SJIS));
		}
    	try{
    		while((line = reader.readLine()) != null){
    			line_list.add(line);
    		}
  			if(is !=null){
  				is.close();
  				is = null;
  			}
  			if(reader !=null){
  				reader.close();
  				reader = null;
  			}
    	}finally{
    		try{
      			if(is !=null){
      				is.close();
      				is = null;
      			}
    		}finally{
      			if(reader !=null){
      				reader.close();
      				reader = null;
      			}
    		}
    	}
		return line_list;
	}

	/**
	 * CSVデータチェック
	 */
	public boolean chkAnkenNo(List<String> line_list) throws Exception {
    	InputCheck check = new InputCheck();
		String anken_no = null;
		String tairyu_hantei = null;
		String hantei_jiyu = null;
    	//課題No.06
    	//追加開始
    	StringBuffer strSplit = new StringBuffer(GS.DOUBLE_QUOTATION).append(GS.COMMA).append(GS.DOUBLE_QUOTATION);
    	//追加完了
		for(int i=0;i<line_list.size();i++){

	    	//課題No.06
	    	//修正開始
			//String[] strClm = (line_list.get(i).split(GS.COMMA));
	    	String[] strClm = (line_list.get(i).split(strSplit.toString()));
	    	//1レコードが4カラムでない場合エラー
	    	if(strClm.length != 4){
		    	appContext.setMsgCode(GL.ERR_INVALID,GL.REPLACE_DATA);
	    		return false;
	    	}
	    	//anken_no = strClm[0].substring(1,strClm[0].length()-1);
	    	//tairyu_hantei = strClm[2].substring(1,strClm[2].length()-1);
	    	//hantei_jiyu = strClm[3].substring(1,strClm[3].length()-1);
	    	anken_no = strClm[0].substring(1,strClm[0].length());
	    	tairyu_hantei = strClm[2].substring(0,strClm[2].length());
	    	hantei_jiyu = strClm[3].substring(0,strClm[3].length()-1);
	    	//修正完了

	    	//案件Noが一致しない場合エラー
	    	if(!tori_bean.getAnken_no().equals(anken_no)){
		    	appContext.setMsgCode(GL.ERR_INVALID,GL.REPLACE_DATA);
	    		return false;
	    	}

	    	//滞留判定の区分値が取得できない場合エラー
	    	tairyu_hantei = dbacc.selP02(tairyu_hantei);
	    	if(tairyu_hantei == null){
		    	appContext.setMsgCode(GL.ERR_INVALID,GL.OB1102_TAIRYUHANTEI);
	    		return false;
	    	}

	    	// 滞留判定が「6ヶ月超滞留」以外の場合
	        if (!TAIRYU_HANTEI_OVER6.equals(tairyu_hantei)) {
	        	// 判定事由が入力されていない場合
	        	if (check.isNullBlank(hantei_jiyu)) {
	                appContext.setMsgCode(GL.ERR_INPUT,GL.REPLACE_HANTEIJIYU);
	                return false;
	        	}
	        }
	    	// 判定事由が1000バイトを超える場合
	        if (check.lenB(hantei_jiyu) > MAX_LEN_HANTEI_JIYU) {
	            appContext.setMsgCode(GL.ERR_LENGTH, GL.REPLACE_HANTEIJIYU);
	            return false;
			}

	        // 判定事由に入力禁止文字が含まれている場合
			if (check.haveKinshiMoji(hantei_jiyu)) {
				// エラーダイアログ（err.prohibitted）を表示する。
				for (int j = 0; j < hantei_jiyu.length(); j++) {
					String kinshiChar = hantei_jiyu.substring(j, j + 1);
					if (check.haveKinshiMoji(kinshiChar)) {
						// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
						appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * T10_滞留判定の更新(一括取り込み)
	 */
	public void updIkkatu(List<String> line_list) throws Exception {
		String anken_no_eda = null;
		String tairyu_hantei = null;
		String hantei_jiyu = null;
		String upd_tairyu_hantei = null;
    	//課題No.06
    	//追加開始
    	StringBuffer strSplit = new StringBuffer(GS.DOUBLE_QUOTATION).append(GS.COMMA).append(GS.DOUBLE_QUOTATION);
    	//追加完了
		for(int i=0;i<line_list.size();i++){
	    	//課題No.06
	    	//修正開始
			//String[] strClm = (line_list.get(i).split(GS.COMMA));
	    	String[] strClm = (line_list.get(i).split(strSplit.toString()));
	    	//anken_no_eda = strClm[1].substring(1,strClm[1].length()-1);
	    	//tairyu_hantei = strClm[2].substring(1,strClm[2].length()-1);
	    	//hantei_jiyu = strClm[3].substring(1,strClm[3].length()-1).replaceAll(STR_N, KAIGYO);
	    	anken_no_eda = strClm[1].substring(0,strClm[1].length());
	    	tairyu_hantei = strClm[2].substring(0,strClm[2].length());
	    	hantei_jiyu = strClm[3].substring(0,strClm[3].length()-1).replaceAll(STR_N, KAIGYO);
	    	//修正完了
	    	//滞留判定の区分値取得
	    	upd_tairyu_hantei = dbacc.selP02(tairyu_hantei);
	    	//T10_滞留判定の更新
	    	dbacc.updTorikomi(anken_no_eda,upd_tairyu_hantei,hantei_jiyu);
		}
	}

    /**
     * 滞留判定入力チェック <br>
     *
     * @return エラーフラグ　ある：true 無し：false
     */
    private boolean process1() throws Exception {
    	// チェッククラス
    	InputCheck check = new InputCheck();
        // 明細リスト
        List ar_meisai = form.getAr_meisai();
        // 全ての明細データに対し入力チェックを行う。
        for (Object obj : ar_meisai) {
			MeisaisyosaiBean meisaiBean = (MeisaisyosaiBean) obj;
			// 滞留判定
			String tairyuHantei = meisaiBean.getTairyu_hantei();
			// 判定事由
			String hanteiJiyu = meisaiBean.getHantei_jiyu();
			if (check.isNullBlank(tairyuHantei)) {
				// 滞留判定が選択されていない場合
				appContext.setMsgCode(GL.ERR_SELECT, GL.OB1102_TAIRYUHANTEI);
				return true;
			} else if (!TAIRYU_HANTEI_OVER6.equals(tairyuHantei) && (check.isNullBlank(hanteiJiyu))) {
				// 滞留判定が「6ヶ月超滞留」以外かつ、判定事由(明細詳細画面で入力)が入力されていない場合
				appContext.setMsgCode(GL.ERR_INPUT,GL.REPLACE_HANTEIJIYU);
				return true;
			}
		}

        return false;
    }

    /**
     * 承認担当者セレクトボックス入力チェック <br>
     */
    private boolean process2() throws Exception {
    	// チェッククラス
    	InputCheck check = new InputCheck();
        // 承認担当者セレクトボックス入力チェック
        String tanto = form.getSyonin_tanto();
        if (check.isNullBlank(tanto)) {
            // 承認担当者が選択されていない場合
            appContext.setMsgCode(GL.ERR_SELECT, GL.OB1102_SHONINTANTOSHA);
            return true;
        }
        return false;
    }

    /**
     * チャンピオン部重複チェック <br>
     */
    private boolean process3() throws Exception {
        // 共)ユーザ情報.業務フローパターンシステム区分が'01'：GSS以外の場合
        if (!GS.GSS.equals(user_bean.getComWorkflowSystemkbn())) {
            return false;
        }
        // 件数を取得する。
        int count = dbacc.selT07();
        if (count>1) {
            // 結果が2件以上の場合
            appContext.setMsgCode(GL.ERR_DUPLICATION);
            return true;
        }

        return false;
    }

    /**
     * T10_滞留判定の更新（SQL08）件数分ループ <br>
     */
    private boolean process4() throws Exception {
        // T10_滞留判定の更新
        dbacc.updT10();
        return false;
    }

    /**
     * T10_滞留判定の登録 <br>
     */
    private boolean process5() throws Exception {
        // T10_滞留判定の登録
        dbacc.insT10Select();
        return false;
    }

    /**
     * T08_滞留判定進捗管理の更新① <br>
     */
    private boolean process6() throws Exception {
        // T08_滞留判定進捗管理の更新①
        dbacc.updT08Regist6();
        return false;
    }

    /**
     * T08_滞留判定進捗管理の更新② <br>
     */
    private boolean process7() throws Exception {
        // T08_滞留判定進捗管理の更新②
        dbacc.updT08Regist7();
        return false;
    }

    /**
     * T08_滞留判定進捗管理の更新③ <br>
     */
    private boolean process8() throws Exception {
        // T08_滞留判定進捗管理の更新③
        dbacc.updT08Regist8();
        return false;
    }

    /**
     * T13_入力履歴の登録 <br>
     */
    private boolean process9() throws Exception {
        // T13_入力履歴の登録を行う。
        dbacc.insT13(INSERT_KBN_REGIST);
        return false;
    }

    /**
     * 同一査定会社、基幹取引先毎の滞留判定済みチェック <br>
     */
    private boolean process10() throws Exception {
        // 件数を取得する。
        int count = dbacc.selT08KanryoCheck();
        if (count>0) {
            // 結果が1件以上の場合
            return true;
        }

        return false;
    }

    /**
     * 査定データ作成条件チェック <br>
     */
    private boolean process11() throws Exception {
    	// T01_対象先の一次査定対象FLGをチェックする。
        String ichiji_flg = dbacc.selT01IchijiSateiFlgCheck();
        if (!FLG_ICHIJI_SATEI_1.equals(ichiji_flg)) {
        	if (dbacc.selM09() == 0) {
                return false;
            }
        	if (!dbacc.selSateiDataCheck()) {
                return true;
            }
        }
        return false;
    }

    /**
     * チャンピオン部再選定 <br>
     */
    private boolean process12() throws Exception {
    	// 共)ユーザ情報.業務フローパターンシステム区分が'01'：GSSの場合
        if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())) {
            //現チャンピオン部、滞留債権額が最も大きい部を取得
            String championbu_cd = dbacc.selCh();
            String max_tairyu_bu_cd = dbacc.selT09();
            // 現チャンピオン部と滞留債権額が最も大きい部が一致しない場合、T07_チャンピオン部を更新する。
            if(max_tairyu_bu_cd != null){
            	if(!championbu_cd.equals(max_tairyu_bu_cd)){
                    dbacc.updT07(championbu_cd);
                    dbacc.updT07_2(max_tairyu_bu_cd);
                }
            }
        }
        return false;
    }

    /**
     * 査定データ作成 <br>
     */
    private boolean process13() throws Exception {
    	// T01_対象先の査定案件Noの取得
    	String satei_anken_no = dbacc.getSateiAnkenNo();
    	// T16_引当金検討対象BS明細の登録
        dbacc.insT16TairyuMeisai(satei_anken_no);
        // T16_引当金検討対象BS明細の登録
        dbacc.insT16Meisai();
        // T17_引当金判定表示用の登録
        dbacc.insT17HikiateHyoji();
        // T14_査定進捗管理の登録
        dbacc.insT14SateiShinchoku();
        // T01_対象先の更新
        dbacc.updT01Taishosaki();
        // T08_滞留判定進捗管理の更新
        dbacc.updT08Select();
        // T15_一次二次査定の登録
        dbacc.insT15Satei();

        return false;
    }

    /**
     * T04_メール配信の登録 <br>
     */
    private boolean process14() throws Exception {
        // T04_メール配信の登録
        dbacc.insT04Mail();

        return false;
    }
}