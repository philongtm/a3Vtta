/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正 
003		2011/07/06		SSC				案件No.D9059 〆後修正後でも、対象先選定ができるように変更
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.action.KihonJohoSyokaiAction;
import app.common.action.SaikenMeisaiSyokaiAction;
import app.common.form.KihonJohoSyokaiForm;
import app.tairyu.dbAcc.SenteisyosaiDbAcc;
import app.tairyu.form.SenteisyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * OB2102_対象先選定_選定先詳細 ビジネスロジッククラス
 */
public class SenteisyosaiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
    private SenteisyosaiDbAcc dbacc = null;					// DBアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	private TorihikisakiBean tori_bean;						// 共通セッション
	private SenteisyosaiForm form;								// アクションフォーム
	private String rtn_gamen_id = null;						// 遷移元画面ID

	private static final String TOROKU               = "10";		//入力区分 '10'： 登録
    private static final String MOGITORI_KAIJO       = "40";		//入力区分 '40'： もぎ取り解除
    private static final String HAKI                 = "70";		//入力区分 '70'： 破棄
    private static final String SYONIN               = "80";		//入力区分 '80'： 承認
    private static final String DIV_TUJOU            = "1";		//登録区分 '1' ： 通常
    private static final String DIV_TAISYOGAI_TUIKA  = "4";		//入力区分 '4' ： 対象外・追加
	private static final String GOLF_KAIINKEN        = "2";		//ゴルフ会員権
	private static final String TAISYOGAI            = "1";		//対象外
	private static final String KOKUNAI_KINGAKU_0    = "0";		//金額チェック用
	private static final String KAIGAI_KINGAKU_0     = "0.00";	//金額チェック用
	private static final String KIHONJOHOSYOKAIFORM  = "KihonJohoSyokaiForm";
	//案件No.D9059 コメント化
	//private static final String SYORIKAISU_4         = "4";		//処理回数'4':〆後修正
	
	//フォーカス設定用
	private static final String COMMENT              = "comment";			
	private static final String SYONIN_TANTO         = "syonin_tanto";
	private static final String TAISYOGAI_KBN        = "taisyogai_kbn";
	private static final String TYUSYUTU_JIYU        = "tyusyutu_jiyu";
	
	/**
	 * コンストラクタ
	 */
	public SenteisyosaiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
        this.sqlExec = appContext.getSqlExecuter();
        this.dbacc = new SenteisyosaiDbAcc(sqlExec, log, appContext);
		this.cmnData = appContext.getCMN();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (SenteisyosaiForm)appContext.getActionForm();
		this.rtn_gamen_id = cmnData.getReturn_gamenId();
	}

	/**
	 * 画面初期表示値取得
	 * 
	 * @return 画面ID
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// 差戻ボタン表示判定
		setSasimodoshi_flg();

		// 抽出事由セレクトボックス表示判定
		setTyusyutujiyu_flg();

		// 対象外区分セレクトボックス値取得
		if(GS.OB2101.equals(rtn_gamen_id)){
			dbacc.getTaishogai_kbn();			
		}

		// 承認担当者セレクトボックス値取得
		if(!GS.OB2104.equals(rtn_gamen_id)){
			dbacc.getSyonin_tanto();			
		}

		// 抽出事由セレクトボックス値取得
		if(GS.OB2103.equals(rtn_gamen_id) || GS.OB2105.equals(rtn_gamen_id) || GS.OS4101.equals(rtn_gamen_id)){
			dbacc.getTyusyutu_jiyu();			
		}

		// 対象外・追加コメント取得
		if(GS.OB2104.equals(rtn_gamen_id) || GS.OB2105.equals(rtn_gamen_id)){
			dbacc.getComment();			
		}

		// OZ6108_基本情報照会タブ生成
		KihonJohoSyokaiAction acc_OZ6108 = new KihonJohoSyokaiAction();
		acc_OZ6108.appExecute(appContext);

		// OZ6105_債権明細照会タブ生成
		SaikenMeisaiSyokaiAction acc_OZ6105 = new SaikenMeisaiSyokaiAction();
		acc_OZ6105.appExecute(appContext);

		return GS.OB2102;
	}
	
	/**
	 * 差戻ボタン表示判定
	 * 
	 * @throws Exception
	 */
	public void setSasimodoshi_flg() throws Exception {
		if(GS.OB2101.equals(rtn_gamen_id) || GS.OB2104.equals(rtn_gamen_id)){
			form.setSasimodoshi_flg(true);
		}
	}

	/**
	 * 抽出事由セレクトボックス表示判定
	 * 
	 * @throws Exception
	 */
	public void setTyusyutujiyu_flg() throws Exception {
		if(GS.OB2103.equals(rtn_gamen_id) || GS.OB2105.equals(rtn_gamen_id) || GS.OS4101.equals(rtn_gamen_id)){
			form.setTyusyutujiyu_flg(true);
		}
	}

	/**
	 * もぎ取り解除処理
	 * 
	 * @throws Exception
	 */
	public void mogitori_kaijo() throws Exception {
		String phase = tori_bean.getPhase(); 
		String status = GS.STATUS_MISYORI; 
		String sasi_ten_flg = tori_bean.getSasi_ten_flg();
		String hoji_user = null;

		// T14_査定進捗管理の更新
		dbacc.updT14_1(phase,status,sasi_ten_flg,hoji_user);			
		// T13_入力履歴の登録
		dbacc.insT13(MOGITORI_KAIJO,false);			
		// COMMIT
		dbacc.commit();
	}

	/**
	 * 対象外処理
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doTaisyogai() throws Exception {
		String syonin_tanto = form.getSyonin_tanto(); 
		String phase = tori_bean.getPhase(); 
		String status = GS.STATUS_SYONIN_MACHI; 

		// 承認担当者セレクトボックスチェック
		if(!chkSyoninTanto()){
            return false;
		}
		// 対象外・追加コメント入力チェック
		if(!chkComment()){
            return false;
		}
		// T14_査定進捗管理の更新
		dbacc.updT14_2(GS.ON);
		// T13_入力履歴の登録
		dbacc.insT13(HAKI,false);
		// T04_メール配信の登録
		dbacc.insT04(syonin_tanto,phase,status,false);
		// T12_コメントの削除
		dbacc.delT12(false);
		// T12_コメントの登録
		dbacc.insT12(DIV_TAISYOGAI_TUIKA,false);
		// T01_対象先の更新
		dbacc.updT01();
		// COMMIT
		dbacc.commit();
		return true;
	}

	/**
	 * 登録処理
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doToroku() throws Exception {
		String syonin_tanto = form.getSyonin_tanto(); 
		String phase = tori_bean.getPhase(); 
		String status = GS.STATUS_SYONIN_MACHI; 

		// 承認担当者セレクトボックスチェック
		if(!chkSyoninTanto()){
            return false;
		}
		// 対象外・追加コメント入力チェック
		if(!chkComment()){
            return false;
		}
		// 対象外区分セレクトボックスチェック
		if(!chkTaisyogai_kbn()){
            return false;
		}
		// T01_対象先の更新
		dbacc.updT01();
		// T04_メール配信の登録
		dbacc.insT04(syonin_tanto,phase,status,false);
		// T14_査定進捗管理の更新
		dbacc.updT14_2(GS.OFF);
		// T13_入力履歴の登録
		dbacc.insT13(TOROKU,false);
		// T12_コメントの削除
		dbacc.delT12(false);
		// T12_コメントの登録
		dbacc.insT12(DIV_TUJOU,false);
		// COMMIT
		dbacc.commit();
		return true;
	}

    /**
     * 【承認実行処理】 <br>
	 * 
	 * @throws Exception
     */
    public void doSyonin() throws Exception {
		String syonin_tanto = form.getSyonin_tanto(); 
		String phase = GS.PHASE_ICHIJI_SATEI;
		String status = GS.STATUS_MISYORI;
		String sasi_ten_flg = null;
		String hoji_user = null;
		boolean mail_haishin_flg = true;
		
		//T14_査定進捗管理の更新
   		if(TAISYOGAI.equals(tori_bean.getTaisyogai_flg()) || GOLF_KAIINKEN.equals(tori_bean.getGolf_flg())){
   			//対象外、ゴルフ会員権の場合、対象先選定完了とする。
   			phase = tori_bean.getPhase();
   			status = GS.STATUS_KANRYO;
   			mail_haishin_flg = false;
   			//課題No.129
   			//削除開始
   			//hoji_user = tori_bean.getHoji_user_id();
   			//削除完了
   		}
		dbacc.updT14_1(phase,status,sasi_ten_flg,hoji_user);
   		//T13_入力履歴の登録
   		dbacc.insT13(SYONIN,false);

   		//対象外、ゴルフ会員権の場合、T04_メール配信に登録しない
   		if(mail_haishin_flg){
   			//T04_メール配信の登録
   	   		dbacc.insT04(syonin_tanto,phase,status,false);
   		}else{
   			//査定データ削除
   			//T15_一次二次査定の削除
   			dbacc.delT15();
   			//T16_引当金検討対象BS明細の削除
   			dbacc.delT16();
   			//T17_引当金判定表示用の削除
   			dbacc.delT17();
   			//T19_留保債務の削除
   			dbacc.delT19();
   			//T20_第三者留保債務の削除
   			dbacc.delT20();
   		}
        // コミット
       	dbacc.commit();
    }

	/**
	 * 追加処理
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doAdd() throws Exception {
		String syonin_tanto = form.getSyonin_tanto(); 
		String phase = tori_bean.getPhase(); 
		String status = GS.STATUS_SYONIN_MACHI; 
		
		// 承認担当者セレクトボックスチェック
		if(!chkSyoninTanto()){
            return false;
		}
		// 対象外・追加コメント入力チェック
		if(!chkComment()){
            return false;
		}
		// 抽出事由セレクトボックスチェック
		if(!chkJiyu()){
            return false;
		}
		// 対象外・追加コメント入力チェック
		if(!chkComment()){
            return false;
		}
		// 金額チェック
		if(!chkKingaku(rtn_gamen_id)){
            return false;
		}
		// チャンピオン部重複チェック
		if(!chkChampion()){
            return false;
		}
		//案件No.D9059 コメント化
		// 処理回数チェック(〆後修正時は追加不可)
		//if(SYORIKAISU_4.equals(tori_bean.getSyori_kaisu())){
        //    appContext.setMsgCode(GL.ERR_MAKE);
        //    return false;
		//}
		// 引当金確認実施中チェック
		if(!chkHikiatekakunin()){
            return false;
		}
		// 滞留判定実施中チェック
		if(!chkTairyuhantei()){
            return false;
		}
		// 査定データ作成
		makeSateiData();
		// T13_入力履歴の登録
		dbacc.insT13(TOROKU,true);
		// T12_コメントの削除
		dbacc.delT12(true);
		// T12_コメントの登録
		dbacc.insT12(DIV_TAISYOGAI_TUIKA,true);
		// T04_メール配信の登録
		dbacc.insT04(syonin_tanto,phase,status,true);
		// 退避情報の作成
		makeTaihi();
		dbacc.commit();
		return true;
	}

	/**
	 * 承認担当者セレクトボックスチェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkSyoninTanto() throws Exception {
		InputCheck check = new InputCheck();
		boolean result = true;

		if (check.isNullBlank(form.getSyonin_tanto())){
            appContext.setMsgCode(GL.ERR_SELECT, GL.OB2102_SYONIN_TANTO);
			appContext.setFocusField(SYONIN_TANTO);
			result = false;
		}		
		return result;
	}

	/**
	 * 対象外・追加コメント入力チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkComment() throws Exception {	
		InputCheck check = new InputCheck();
		int comment_len = check.lenB(form.getComment());

    	// 対象外・追加コメントが2000byteを超える場合エラー
	    if(2000 < comment_len){
            appContext.setMsgCode(GL.ERR_LENGTH,GL.OB2102_COMMENT);
			appContext.setFocusField(COMMENT);
			return false;
	    }
	    // 入力禁止文字が含まれている場合エラー
		for (int i = 0; i < form.getComment().length(); i++) {
			String kinshiChar = form.getComment().substring(i, i + 1);
			if (check.haveKinshiMoji(kinshiChar)) {
				// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
				appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
				appContext.setFocusField(COMMENT);
				return false;
			}
		}
		return true;
	}

	/**
	 * 対象外区分セレクトボックスチェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkTaisyogai_kbn() throws Exception {
		InputCheck check = new InputCheck();
		boolean result = true;

		if (!check.isNullBlank(form.getTaisyogai_kbn())){
            appContext.setMsgCode(GL.ERR_TAISYOUGAI);
			appContext.setFocusField(TAISYOGAI_KBN);
			result = false;
		}		
		return result;
	}

	/**
	 * 抽出事由セレクトボックスチェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkJiyu() throws Exception {
		InputCheck check = new InputCheck();
		boolean result = true;

		if (check.isNullBlank(form.getTyusyutu_jiyu())){
            appContext.setMsgCode(GL.ERR_SELECT, GL.OB2102_REASON);
			appContext.setFocusField(TYUSYUTU_JIYU);
			result = false;
		}		
		return result;
	}

	/**
	 * 金額チェック
	 * 
	 * @param  画面ID
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkKingaku(String gamen_id) throws Exception {
		InputCheck check = new InputCheck();
		boolean result = true;
		KihonJohoSyokaiForm form_OZ6108 = (KihonJohoSyokaiForm)appContext.getSessionActionForm(KIHONJOHOSYOKAIFORM);; 
		String saiken_kei = form_OZ6108.getSaiken_kei(); //債権残高合計
		String hosyo_saimu_kei = form_OZ6108.getKbn14(); //保証債務合計
		String ki_hikiatekin = form_OZ6108.getKbn15();   //既引当金
		String format0 = GS.EMPTY_CHARCTER;

		//金額のフォーマット
		if(GS.GSS.equals(tori_bean.getSystem_kbn())){
			format0 = KOKUNAI_KINGAKU_0;
		}else{
			format0 = KAIGAI_KINGAKU_0;	
		}
		// OB2102_対象先選定_追加対象先選択から遷移時のみチェック実行
		if(GS.OB2103.equals(gamen_id)){
			//債権残高合計,保証債務合計,既引当金が0の場合エラー
			if (format0.equals(saiken_kei)
					&& (check.isNullBlank(hosyo_saimu_kei) || format0.equals(hosyo_saimu_kei))
					&& (check.isNullBlank(ki_hikiatekin) || format0.equals(ki_hikiatekin))){
				appContext.setMsgCode(GL.ERR_ADD);
				result = false;
			}		
		}
		return result;
	}

	/**
	 * チャンピオン部重複チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkChampion() throws Exception {
		// 海外はチェックしない
        if (!GS.GSS.equals(tori_bean.getSystem_kbn())) {
            return true;
        }
        int count = dbacc.getCountChampion();
        if (count>1) {
            // 結果が2件以上の場合エラー
            appContext.setMsgCode(GL.ERR_DUPLICATION);
            return false;
		}		
        return true;
	}

	/**
	 * 滞留判定実施中チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkTairyuhantei() throws Exception {
		boolean result = true;

        int count = dbacc.getCountTairyu();
        if (count>0) {
            // 結果が1件以上の場合エラー
            appContext.setMsgCode(GL.ERR_COMPLETED);
			result = false;
		}		
		return result;
	}

	/**
	 * 引当金確認実施中チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkHikiatekakunin() throws Exception {
		boolean result = true;

		if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(tori_bean.getPhase())){
			return result;
		}

		int count = dbacc.getCountHikiate();
        if (count>0) {
            // 結果が1件以上の場合エラー
            appContext.setMsgCode(GL.ERR_HIKIATESTART);
			result = false;
		}		
		return result;
	}

    /**
     * 査定データ作成 <br>
	 * 
	 * @throws Exception
     */
    private void makeSateiData() throws Exception {
    	// T16_引当金検討対象BS明細の登録
    	if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(tori_bean.getPhase())){
        	// 引当金確認差戻時
            dbacc.insT16h();
    	}else{
            dbacc.insT16();
    	}
        // T17_引当金判定表示用の登録
        dbacc.insT17();
        // T14_査定進捗管理の登録
        if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(tori_bean.getPhase())){
            dbacc.updT14();
        }else{
            dbacc.insT14();
        }
        // T02_検討対象先の削除
        dbacc.delT02();
        // T02_検討対象先の登録
        dbacc.insT02();
        // T01_対象先の存在チェック
        if(dbacc.getCountT01() == 0){
            // T01_対象先の登録
        	dbacc.insT01();        	
        }else{
            // T01_対象先の更新
        	dbacc.updT01_add();        	
        }
        // T15_一次二次査定の登録
        dbacc.insT15();
    }

    /**
     * 退避データ作成 <br>
	 * 
	 * @throws Exception
     */
    private void makeTaihi() throws Exception {
    	// 退避情報の削除
        dbacc.delTaihi();
        // E01_統合対比退避の登録
        dbacc.insE01();
        // E02_統合マスタ退避の登録
        dbacc.insE02();
        // E03_格付退避の登録
        dbacc.insE03();
        // E04_D&B企業情報の登録
        dbacc.insE04();
        // E05_財務退避の登録
        dbacc.insE05();        	
    }
	
	/**
	 * 実施業務取得<br>
     * @param String
     * @param String
     * @return HashMap
	 */
	protected HashMap getJishiGyoumu(String phase,String stat){
		List<HashMap> list = cmnData.getUser_bean().getComOparation();
		HashMap map = null;
		for(int i=0;i<list.size();i++){
			map = list.get(i);
			if(phase.equals((String)map.get(GS.JISHI_PHASE)) && stat.equals((String)map.get(GS.KAISHI_STATUS))){
				break;
			}
		}
		return map;
	}
}