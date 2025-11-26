/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.42	フェーズプルダウン変更時の設定修正 
003		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
004		2016/01/05		SSC				案件No.BP201601002 障害対応（督促メール送信機能） 
005		2016/02/02		SSC				案件No.BP201602008 障害対応（もぎ取り依頼メール・転送・差戻）
006		2016/03/24		SSC				案件No.BJ201602002 部門廃止対応（一次）
007		2016/04/18		SSC				案件No.BJ201602002 部門廃止対応（一次）
******************************************************************************/

package app.common.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.common.dbAcc.SashimodoshiDbAcc;
import app.common.form.RirekiBean;
import app.common.form.RirekiListBean;
import app.common.form.SashimodoshiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * OZ2101_差戻先選択 ビジネスロジッククラス
 */
public class SashimodoshiBss {

	//画面モード
	private static final String TAIRYU_MODE   = "1";
	private static final String SATEI_MODE    = "2";
	private static final String HIKIATE_MODE  = "3";
	private static final String JIMU_MODE     = "4";
	private static final String SINKI_MODE    = "5";
	private static final String KARITUIKA_MODE= "6";
	//フェーズ判定用
	private static final String PHASE_TAIRYU  = "1";
	private static final String PHASE_SATEI   = "2";
	//差戻種別判定用
	private static final String TYPE1         = "1";
	private static final String TYPE2         = "2";
	private static final String TYPE3         = "3";
	private static final String TYPE4         = "4";
	private static final String TYPE5         = "5";
	private static final String TYPE6         = "6";
	private static final String TYPE7         = "7";
	private static final String TYPE8         = "8";
	private static final String TYPE9         = "9";
	private static final String TYPE10        = "10";
	//入力テーブル削除時の処理ID
	private static final String DELETE_ID0    = "0";
	private static final String DELETE_ID1    = "1";
	private static final String DELETE_ID2    = "2";
	//判定査定区分
	private static final String NYURYOKU_T    = "1";
	private static final String NYURYOKU_S    = "2";
	//メールステータス
	private static final String MOGITORI      = "10";
	private static final String SYONIN        = "20";
	private static final String SASHIMODOSHI  = "30";
	//査定対象フラグ
	private static final String SATEI_FLG1    = "1";
	
	//フォーカス制御用
	private static final String SASHI_COMMENT = "sashi_comment";

    private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	private SessionData cmnData;				// 共通セッション
	private UserBean user_bean;				// ユーザ情報
	private TorihikisakiBean tori_bean;		// 取引先情報
	private SashimodoshiForm form;				// アクションフォーム
	
	
	/**
	 * コンストラクタ
	 */
	public SashimodoshiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (SashimodoshiForm)appContext.getActionForm();
	}


	/**
	 * 画面初期表示処理
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		// 入力項目初期化
		initNyuryoku();
		// 実施フェーズ判定
		setJishiPhase();
		// 差戻種別ラジオボタン表示制御
		initRadioBtn();
		// 一覧/詳細部情報取得
		getSyosai();
	}

	/**
	 * 差戻種別ラジオボタン処理
	 * 
	 * @throws Exception
	 */
	public void gamen_mode() throws Exception {
		// 一覧/詳細部情報取得
		getSyosai();
	}

	/**
	 * 実施フェーズ判定
	 * 
	 * @throws Exception
	 */
	public void setJishiPhase() throws Exception {

		String phase = tori_bean.getPhase();
		
		if(GS.PHASE_TAIRYU_HANTEI.equals(phase) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(phase)){
			form.setJishi_phase(PHASE_TAIRYU);
		}
		if(GS.PHASE_ICHIJI_SATEI.equals(phase) || GS.PHASE_ICHIJI_SATEI_KENSYO.equals(phase)
				|| GS.PHASE_NIJI_SATEI.equals(phase)){
			form.setJishi_phase(PHASE_SATEI);
		}
	}

	/**
	 * 差戻種別ラジオボタン表示制御
	 * 
	 * @throws Exception
	 */
	public void initRadioBtn() throws Exception {
	    
		String rtn_gamen_id = cmnData.getReturn_gamenId();
		String phase = tori_bean.getPhase();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();	
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		if(PHASE_SATEI.equals(form.getJishi_phase()) && !GS.SAISYU.equals(tori_bean.getKijunbi_kbn())){

			// 基準日取得(一次二次査定フェーズかつ仮基準日の場合)
			String kijunbi = dbacc.getKijunbi();
			
			// 引当金検証/確認開始済みチェック
			boolean hikiateChk = dbacc.isHikiate_kaishi(kijunbi);
			form.setHikiateFlg(hikiateChk);
		}
		if(GS.OB2101.equals(rtn_gamen_id) || PHASE_SATEI.equals(form.getJishi_phase())){
			// 滞留判定データ存在チェック
			boolean tairyuChk = dbacc.isExsit_Tairyu();
			form.setTairyuFlg(tairyuChk);
		}
		// 滞留判定
		if(PHASE_TAIRYU.equals(form.getJishi_phase())){
			form.setGamen_mode(TAIRYU_MODE);
			form.setTairyu_sashi_flg(true);
		}
		// 対象先選定(登録)
		if(GS.OB2101.equals(rtn_gamen_id)){
			form.setGamen_mode(SINKI_MODE);
			if(form.isTairyuFlg()){
				form.setGamen_mode(TAIRYU_MODE);
				form.setTairyu_sashi_flg(true);
				form.setTo_tairyu_sashi_flg(true);
			}
		}
		// 対象先選定(承認)
		if(GS.OB2104.equals(rtn_gamen_id)){
			form.setGamen_mode(SATEI_MODE);
			form.setSatei_sashi_flg(true);
		}
		// 一次二次査定
		if(PHASE_SATEI.equals(form.getJishi_phase())){
			form.setGamen_mode(SATEI_MODE);
			form.setSatei_sashi_flg(true);
			if(!form.isHikiateFlg()){
				form.setJimu_sashi_flg(true);
			}
			if(!form.isHikiateFlg() && form.isTairyuFlg() && GS.OFF.equals(user_bean.getComJimukyoku_sashi_flg())){
				form.setTairyu_sashi_flg(true);
				form.setTo_tairyu_sashi_flg(true);
			}
		}
		// 引当金検証
		if(GS.PHASE_HIKIATEKIN_KENSYO.equals(phase)){
			form.setGamen_mode(SATEI_MODE);
			form.setSatei_sashi_flg(true);
		}
		// 引当金確認
		if(GS.PHASE_HIKIATEKIN_KAKUNIN.equals(phase)){
			if(GS.OD1102.equals(rtn_gamen_id)){
				form.setGamen_mode(KARITUIKA_MODE);
				form.setJimuSashiKariFlg(true);
			}else{
				form.setGamen_mode(HIKIATE_MODE);
				form.setHikiate_sashi_flg(true);
				
			}
		}
		// クレーム債権再設定
		if(GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(phase)){
			form.setGamen_mode(SATEI_MODE);
			form.setSatei_sashi_flg(true);
			form.setJimu_sashi_flg(true);
		}
		// 新規差戻先選択表示設定
		if(!GS.OD1102.equals(rtn_gamen_id) && !GS.OB2104.equals(rtn_gamen_id) && !GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(phase)){
			form.setSinki_sashi_flg(true);
		}
	}

	/**
	 * 一覧/詳細部情報取得処理
	 * 
	 * @throws Exception
	 */
	public void getSyosai() throws Exception {

		String gamen_mode = form.getGamen_mode();
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();	
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		// 履歴情報取得
		getRireki(gamen_mode,form.isTo_tairyu_sashi_flg());

		// 事務局差戻時設定値取得
		if(JIMU_MODE.equals(form.getGamen_mode()) || KARITUIKA_MODE.equals(form.getGamen_mode())){
			// 差戻区分セレクトボックス設定値取得
			dbacc.getSashi_kbn();
		}

		// 新規差戻選択時設定値取得
		if(SINKI_MODE.equals(form.getGamen_mode())){
			// 差戻フェーズセレクトボックス設定値取得
			dbacc.getSashiphase();
			// 汎用２(右)セレクトボックス設定値取得
			dbacc.getHanyo2();
			// 汎用３セレクトボックス設定値取得
			dbacc.getHanyo3();
	        // 査定会社別担当者一覧取得
			if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
			   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
				// 担当者一覧取得（本部）
				dbacc.getTantoIchiran2();
			} else {
				// 担当者一覧取得
				dbacc.getTantoIchiran();
			}
		}
	}

	/**
	 * 履歴情報取得
     * 
     * @param String
     * @param boolean
	 * @throws Exception
	 */
	public void getRireki(String gamen_mode,boolean to_tairyu_sashi_flg) throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
			
		// 滞留判定フェーズ差戻時(滞留判定フェーズ内)
		if(TAIRYU_MODE.equals(gamen_mode) && !to_tairyu_sashi_flg){
			dbacc.getTairyu_sashimodoshisaki();
		}
		// 滞留判定フェーズ差戻時(査定から滞留判定)
		if(TAIRYU_MODE.equals(gamen_mode) && form.isTo_tairyu_sashi_flg()){
			dbacc.getTairyu_sashimodoshisaki2();
		}
		// 査定フェーズ、引当金確認フェーズ差戻時
		if(SATEI_MODE.equals(gamen_mode) || HIKIATE_MODE.equals(gamen_mode)){
			dbacc.getSatei_sashimodoshisaki();
		}
	}

	/**
	 * 差戻フェーズセレクトボックス処理
     * 
	 * @throws Exception
	 */
	public void sashi_phase() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();	
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
		
		//担当者初期化
		initTanto();
		
		// 課題No.42
		// 修正開始
		// 汎用２(右)セレクトボックス再設定
		// if(GS.PHASE_NIJI_SATEI.equals(form.getSashiphase())){
		dbacc.getHanyo2();
		// 汎用３(右)セレクトボックス再設定
		dbacc.getHanyo3();
		// }
		// 修正完了
		
        // 査定会社別担当者一覧取得
		if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
		   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			// 担当者一覧再取得（本部）
			dbacc.getTantoIchiran2();
		} else {
			// 担当者一覧再取得
			dbacc.getTantoIchiran();
		}
	}

	/**
	 * 汎用２セレクトボックス処理
     * 
	 * @throws Exception
	 */
	public void hanyo2() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
		 // 汎用3セレクトボックスの設定値を取得する
        dbacc.getHanyo3();
		//担当者初期化
		initTanto();
        // 査定会社別担当者一覧取得
		if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
		   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			// 担当者一覧再取得
			dbacc.getTantoIchiran2();
		} else {
			// 担当者一覧再取得
			dbacc.getTantoIchiran();
		}
	}

	/**
	 * 汎用３セレクトボックス処理
     * 
	 * @throws Exception
	 */
	public void hanyo3() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
		//担当者初期化
		initTanto();
		// 担当者一覧再取得
		dbacc.getTantoIchiran2();
	}

	/**
	 * 入力項目初期化
     * 
	 * @throws Exception
	 */
	public void initNyuryoku() throws Exception {
		form.setSashi_comment(GS.EMPTY_CHARCTER);
		initTanto();
	}

	/**
	 * 担当者初期化
     * 
	 * @throws Exception
	 */
	public void initTanto() throws Exception {
		form.setInTanto(GS.EMPTY_CHARCTER);
		form.setTxtTanto(GS.EMPTY_CHARCTER);
		form.setSelectedTantoId(GS.EMPTY_CHARCTER);
	}

	/**
	 * 差戻処理
     * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean doSashimodoshi() throws Exception {
		boolean result = false;
		String satei_flg = null;
		String gamen_mode = form.getGamen_mode();
		String kariAnkenNo = GS.EMPTY_CHARCTER;
		String kariYm = GS.EMPTY_CHARCTER;
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//差戻コメント入力チェック
		if(!chkComment()){
			return result;
		}
		if(!KARITUIKA_MODE.equals(gamen_mode) && !JIMU_MODE.equals(gamen_mode) && !SINKI_MODE.equals(gamen_mode)){
			//差戻先チェック
			if(!chkSashimodoshiSaki()){
				return result;
			}
			//ユーザ権限チェック
			if(!chkUserKengen()){
				return result;
			}
		}
		if (SINKI_MODE.equals(gamen_mode)){
			//新規差戻画面 担当者選択チェック
			if (!GS.GSS.equals(tori_bean.getSystem_kbn())) {
				if(!chkTanto()){
					return result;
				}
			}
			
			//新規差戻画面 部選択チェック
			if (GS.GSS.equals(tori_bean.getSystem_kbn())
					&& GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())) {
				if(!chkBuSentaku()){
					return result;
				}
			}
		}
		//差戻種別判定
		String sashimodoshi_type = chkSashimodoshiType();
		//進捗テーブル
		if(TYPE1.equals(sashimodoshi_type) || TYPE2.equals(sashimodoshi_type) || TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//T08_滞留判定進捗管理の更新
			doUpdT08(sashimodoshi_type);
			//T10_滞留判定の削除
			doDelT10(sashimodoshi_type);
		}else if(TYPE10.equals(sashimodoshi_type)){
			//仮基準査定選択案件のT14_査定進捗管理の作成
			kariYm = dbacc.getKariYm();
			kariAnkenNo = Function.trim(dbacc.insT14(kariYm));
		}else{
			//T14_査定進捗管理の更新
			doUpdT14(sashimodoshi_type);
		}
		if(TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//査定関連テーブルの削除
			doDelSateiTbl_All();
			//抽出事由判定
			if(chkTyusyutuJiyu()){
				satei_flg = SATEI_FLG1;
			}
			//T01_対象先の更新
			dbacc.updT01(satei_flg);
		}
		//査定入力系テーブルの削除①
		if(TYPE5.equals(sashimodoshi_type) || TYPE6.equals(sashimodoshi_type)){
			doDelSateiTbl1(sashimodoshi_type);
		}
		//査定入力系テーブルの削除②
		if(TYPE7.equals(sashimodoshi_type)){
			doDelSateiTbl2();
		}

		//T13_入力履歴の登録
		String nyuryoku_kbn;
		if(PHASE_TAIRYU.equals(form.getJishi_phase())){
			nyuryoku_kbn = NYURYOKU_T;
		}else{
			nyuryoku_kbn = NYURYOKU_S;
		}
		dbacc.insT13(nyuryoku_kbn);

		if(!TYPE10.equals(sashimodoshi_type)){
			//T11_文書添付の削除
			doDelT11(sashimodoshi_type);
			//T12_コメントの削除
			doDelT12(sashimodoshi_type);
			//T12_コメントの登録
			doInsT12(sashimodoshi_type);
			//T04_メール配信の登録
			doInsT04(sashimodoshi_type);
		}else{
			//コメント登録
			dbacc.insT12(kariAnkenNo,form.getSashikbn());
			//メール配信登録
			dbacc.insT04(kariAnkenNo,kariYm,MOGITORI);
			//E01_統合対比退避・E02_統合マスタ退避・E03_格付退避・E04_D&B企業情報・E05_財務退避の削除
			dbacc.delTaihi(kariYm);
			//E01_統合対比退避・E02_統合マスタ退避・E03_格付退避・E04_D&B企業情報・E05_財務退避の登録
			dbacc.insTaihi(kariYm);
		}
		//コミット
		dbacc.commit();
		return true;
	}

	/**
	 * 差戻コメント入力チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkComment() throws Exception {
		InputCheck check = new InputCheck();
		String comment = form.getSashi_comment();
		String kinshiChar = GS.EMPTY_CHARCTER;

    	// コメントが2000byteを超える場合エラー
		if(!(check.islength(comment,2000))){
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OZ2101_COMMENT);
			appContext.setFocusField(SASHI_COMMENT);
			return false;
		}
		
	    // 入力禁止文字が含まれている場合エラー
		for (int i = 0; i < comment.length(); i++) {
			kinshiChar = comment.substring(i,i + 1);
			if(check.haveKinshiMoji(kinshiChar)) {
				//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
				appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
				return false;
			}
		}
		return true;
	}

	/**
	 * 差戻先選択チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkSashimodoshiSaki() throws Exception {
		List ar_rireki = form.getAr_rireki();
		//フェーズ選択ラジオボタンが全て未チェックの場合エラー
		for (int i = 0; i < ar_rireki.size(); i++) {
			RirekiListBean rirekiBean = (RirekiListBean)ar_rireki.get(i);
			if(!GS.EMPTY_CHARCTER.equals(rirekiBean.getRadio_id())){
				return true;
			}
		}
        appContext.setMsgCode(GL.ERR_SELECT,GL.REPLACE_SASHIMODOSHISAKI);
		return false;
	}

	/**
	 * ユーザ権限チェック
     * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkUserKengen() throws Exception {
    	String errMsg = GL.ERR_NOTSET;
    	boolean result = true;
		List ar_rireki = form.getAr_rireki();
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		for (int i = 0; i < ar_rireki.size(); i++) {
			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				int id = Integer.parseInt(listBean.getRadio_id());
				if(!dbacc.isUserKengen(rireki_list.get(id))){
					result = false;
					break;
				}
				// ユーザの参照権限チェック追加
				if(!dbacc.isUserSansyoKengen(rireki_list.get(id))){
					result = false;
					errMsg = GL.ERR_SANSYO_NOTSET;
					break;
				}
			}
		}
		if(!result){
	    	List<String> msgList = new ArrayList<String>();
	    	msgList.add(errMsg);
	    	msgList.add(GL.REPLACE_USER);
	    	msgList.add(GL.REPLACE_SASHIMODOSHISAKI);
	    	appContext.setMsgCode(msgList);

		}
		return result;
	}

	/**
	 * 担当者選択チェック
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkTanto() throws Exception {
		InputCheck check = new InputCheck();
		if(check.isNullBlank(form.getTxtTanto())){
			appContext.setMsgCode(GL.ERR_SELECT,GL.OZ2101_TANTO);
			return false;
		}
		return true;
	}

	/**
	 * 部選択チェック（査定会社SJの場合）
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	public boolean chkBuSentaku() throws Exception {
		InputCheck check = new InputCheck();
		if(check.isNullBlank(form.getHanyou3())){
			//エラー出力
			appContext.setMsgCode(GL.ERR_SELECT,GL.OZ2101_BU);
			return false;
		}
		return true;
	}

	/**
	 * 差戻種別判定
	 * 
	 * @return String
	 * @throws Exception
	 */
	public String chkSashimodoshiType() throws Exception {
		String gamen_mode = form.getGamen_mode();
		boolean to_tairyu_flg = form.isTo_tairyu_sashi_flg();
		String phase = tori_bean.getPhase();
		String sashi_phase = form.getSashiphase();

		/* 差戻種別  1:滞留フェーズ差戻(滞留フェーズ内)					*/
		/*			 2:新規差戻先選択差戻(滞留フェーズ内)				*/
		/*			 3:滞留フェーズ差戻(査定から滞留フェーズに差戻)		*/
		/*			 4:新規差戻先選択差戻(査定から滞留フェーズに差戻)	*/
		/*			 5:査定フェーズ差戻									*/
		/*			 6:新規差戻先選択差戻(査定フェーズ内)				*/
		/*			 7:事務局差戻										*/
		/*			 8:引当金確認フェーズ差戻							*/
		/*			 9:新規差戻先選択差戻(引当金検証/確認フェーズ内)　　*/
		/*			10:事務局差戻(引当金確認/仮基準査定追加)		　　*/
		
		if(TAIRYU_MODE.equals(gamen_mode) && !to_tairyu_flg){
			form.setSahimodoshiType(TYPE1);
		}else if(SINKI_MODE.equals(gamen_mode) && PHASE_TAIRYU.equals(form.getJishi_phase())){
			form.setSahimodoshiType(TYPE2);
		}else if(TAIRYU_MODE.equals(gamen_mode) && to_tairyu_flg){
			form.setSahimodoshiType(TYPE3);
		}else if(SINKI_MODE.equals(gamen_mode) && to_tairyu_flg && (GS.PHASE_TAIRYU_HANTEI.equals(sashi_phase) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(sashi_phase))){
			form.setSahimodoshiType(TYPE4);
		}else if(SATEI_MODE.equals(gamen_mode)){
			form.setSahimodoshiType(TYPE5);
		}else if(SINKI_MODE.equals(gamen_mode) && 
				(Integer.parseInt(GS.PHASE_TAISHOSAKI_SENTEI) <= Integer.parseInt(sashi_phase) && Integer.parseInt(sashi_phase) <=Integer.parseInt(GS.PHASE_NIJI_SATEI))){
			form.setSahimodoshiType(TYPE6);
		}else if(JIMU_MODE.equals(gamen_mode)){
			form.setSahimodoshiType(TYPE7);
		}else if(HIKIATE_MODE.equals(gamen_mode)){
			form.setSahimodoshiType(TYPE8);
		}else if(SINKI_MODE.equals(gamen_mode) && (GS.PHASE_HIKIATEKIN_KENSYO.equals(phase) || GS.PHASE_HIKIATEKIN_KAKUNIN.equals(phase))){
			form.setSahimodoshiType(TYPE9);
		}else if(KARITUIKA_MODE.equals(gamen_mode)){
			form.setSahimodoshiType(TYPE10);
		}
		return form.getSahimodoshiType();
	}

	/**
	 * T08_滞留判定進捗管理の更新
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doUpdT08(String sashimodoshi_type) throws Exception {
		InputCheck check = new InputCheck();
		List ar_rireki = form.getAr_rireki();
		String phase = form.getSashiphase();
		String status = GS.STATUS_MISYORI;
		String hoji_user = null;
		String bunrui2 = tori_bean.getBunrui2();
		String bu_cd = null;
		String anken_no = tori_bean.getAnken_no();
		form.setBu_cd_upd_flg(false);

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

    	//新規差戻先選択
    	if(SINKI_MODE.equals(form.getGamen_mode())){
    		//画面に部コードが入力されている場合
    		if(!check.isNullBlank(form.getHanyou3())){
    			form.setBu_cd_upd_flg(true);
    			bu_cd = form.getHanyou3();
    			//案件の部門コードと画面の部門コードが異なる場合
    			if(!Function.trim(tori_bean.getBunrui2()).equals(form.getHanyou2())){
    				bunrui2 = form.getHanyou2();
    			}
    			//担当者選択時
    			if(!check.isNullBlank(form.getTxtTanto())){
    				status = GS.STATUS_SYORICHU;
    				hoji_user = form.getSelectedTantoId();
    			}
    		//画面に部コードが入力されていない場合（査定会社SJ以外）
			}else{
				// システム区分01、査定会社がPNの時
				if (GS.GSS.equals(tori_bean.getSystem_kbn())
						&& GS.SATEIKAISYA_PN.equals(tori_bean.getSateikaisya_cd())){
					// 案件の部門コードと画面の部門コードが異なる場合
					if(!Function.trim(tori_bean.getBunrui2()).equals(form.getHanyou2())){
						bunrui2 = form.getHanyou2();
						form.setBu_cd_upd_flg(true); // 部コードをnullで更新
					}
					//担当者選択時
					if(!check.isNullBlank(form.getTxtTanto())){
						status = GS.STATUS_SYORICHU;
						hoji_user = form.getSelectedTantoId();
					}
				}else{
					// 海外の時（担当者必須、部の更新なし）
					status = GS.STATUS_SYORICHU;
					hoji_user = form.getSelectedTantoId();
				}
			}
			//査定から滞留へ差戻時
			if(TYPE4.equals(sashimodoshi_type)){
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
					List<String> ankenList = form.getAr_tairyu_anken_no();
					anken_no = ankenList.get(i);
					dbacc.updT08(phase,status,hoji_user,anken_no,bunrui2,bu_cd);
				}	
			}else{
				dbacc.updT08(phase,status,hoji_user,anken_no,bunrui2,bu_cd);
			}
		//各フェーズ差戻
    	}else{
			for (int i = 0; i < ar_rireki.size(); i++) {
    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
    				int id = Integer.parseInt(listBean.getRadio_id());
    				List<RirekiBean> rireki_list = listBean.getRireki_list();
    				RirekiBean rirekiBean = rireki_list.get(id);
    				phase = rirekiBean.getPhase();
    				status = rirekiBean.getStatus();
    				hoji_user = rirekiBean.getTanto_user_id();
    				anken_no = rirekiBean.getAnken_no();
    				dbacc.updT08(phase,status,hoji_user,anken_no,bunrui2,bu_cd);
    			}
    		}
    	}
	}

	/**
	 * T10_滞留判定の削除
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doDelT10(String sashimodoshi_type) throws Exception {
		List ar_rireki = form.getAr_rireki();
		String sashiphase = form.getSashiphase();
		String anken_no = tori_bean.getAnken_no();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

    	//新規差戻先選択
    	if(SINKI_MODE.equals(form.getGamen_mode())){
			//査定から滞留へ差戻時
			if(TYPE4.equals(sashimodoshi_type)){
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
		        	List<String> ankenList = form.getAr_tairyu_anken_no();
		        	anken_no = ankenList.get(i);
    				dbacc.delT10(anken_no,sashiphase);
		        }	
			}else{
				dbacc.delT10(anken_no,sashiphase);
			}
		
		//各フェーズ差戻
    	}else{
			for (int i = 0; i < ar_rireki.size(); i++) {
    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
    				int id = Integer.parseInt(listBean.getRadio_id());
    				List<RirekiBean> rireki_list = listBean.getRireki_list();
    				RirekiBean rirekiBean = rireki_list.get(id);
    				sashiphase = rirekiBean.getPhase();
    				anken_no = rirekiBean.getAnken_no();
    				dbacc.delT10(anken_no,sashiphase);
    			}
    		}
    	}
	}

	/**
	 * T14_査定進捗管理の更新
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doUpdT14(String sashimodoshi_type) throws Exception {
		InputCheck check = new InputCheck();
		List ar_rireki = form.getAr_rireki();
		String phase = form.getSashiphase();
		String status = GS.STATUS_MISYORI;
		String hoji_user = null;
		String bunrui2 = tori_bean.getBunrui2();
		String bu_cd = null;
		form.setBu_cd_upd_flg(false);

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

    	//新規差戻先選択
    	if(SINKI_MODE.equals(form.getGamen_mode())){
    		//画面に部コードが入力されている場合（査定会社SJ）
    		if(!check.isNullBlank(form.getHanyou3())){
    			form.setBu_cd_upd_flg(true);
    			bu_cd = form.getHanyou3();
    			//案件の部門コードと画面の部門コードが異なる場合
    			if(!Function.trim(tori_bean.getBunrui2()).equals(form.getHanyou2())){
    				bunrui2 = form.getHanyou2();
    			}
    			//担当者選択時
    			if(!check.isNullBlank(form.getTxtTanto())){
    				status = GS.STATUS_SYORICHU;
    				hoji_user = form.getSelectedTantoId();
    			}
    		//画面に部コードが入力されていない場合（査定会社SJ以外）
    		}else{
				// システム区分01、査定会社がPNの時
				if (GS.GSS.equals(tori_bean.getSystem_kbn())
						&& GS.SATEIKAISYA_PN.equals(tori_bean.getSateikaisya_cd())){
					// 案件の部門コードと画面の部門コードが異なる場合
					if(!Function.trim(tori_bean.getBunrui2()).equals(form.getHanyou2())){
						bunrui2 = form.getHanyou2();
						form.setBu_cd_upd_flg(true); // 部コードをnullで更新
					}
					//担当者選択時
					if(!check.isNullBlank(form.getTxtTanto())){
						status = GS.STATUS_SYORICHU;
						hoji_user = form.getSelectedTantoId();
					}
				// 海外の時（担当者必須、部の更新なし）
				}else{
					status = GS.STATUS_SYORICHU;
					hoji_user = form.getSelectedTantoId();
				}
    		}
		
		//事務局差戻
    	}else if(JIMU_MODE.equals(form.getGamen_mode())){
			phase = form.getSashikbn();
 
		//各フェーズ差戻
    	}else{
    		RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
   			int id = Integer.parseInt(listBean.getRadio_id());
   			List<RirekiBean> rireki_list = listBean.getRireki_list();
   			RirekiBean rirekiBean = rireki_list.get(id);
   			phase = rirekiBean.getPhase();
   			status = rirekiBean.getStatus();
   			hoji_user = rirekiBean.getTanto_user_id();
    	}
		dbacc.updT14(phase,status,hoji_user,bunrui2,bu_cd);
	}

	/**
	 * 査定関連テーブルの削除
	 * 
	 * @throws Exception
	 */
	public void doDelSateiTbl_All() throws Exception {
		String phase = tori_bean.getPhase();
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//T14_査定進捗管理の削除
		dbacc.delT14();
		//T15_一次二次査定の削除
		dbacc.delT15(DELETE_ID0,phase);
		//T16_引当金検討対象BS明細の削除
		dbacc.delT16();
		//T17_引当金判定表示用の削除
		dbacc.delT17();
		//T19_留保債務の削除
		dbacc.delT19(DELETE_ID0,phase);
		//T20_第三者留保債務の削除
		dbacc.delT20(DELETE_ID0,phase);
	}

	/**
	 * 抽出事由判定
	 * 
	 * @throws Exception
	 */
	public boolean chkTyusyutuJiyu() throws Exception {
		boolean result = false;
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
		int cnt = dbacc.selJiyu();
		if(cnt > 0){
			result = true;
		}
		return result;
	}

	/**
	 * 査定入力系テーブルの削除①(査定フェーズ内差戻)
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doDelSateiTbl1(String sashimodoshi_type) throws Exception {
		String sashiphase = form.getSashiphase();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		if(TYPE5.equals(sashimodoshi_type)){
			List ar_rireki = form.getAr_rireki();
			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
			int id = Integer.parseInt(listBean.getRadio_id());
			List<RirekiBean> rireki_list = listBean.getRireki_list();
			RirekiBean rirekiBean = rireki_list.get(id);
			sashiphase = rirekiBean.getPhase();
		}

		//T15_一次二次査定の削除
		dbacc.delT15(DELETE_ID1,sashiphase);
		//T19_留保債務の削除
		dbacc.delT19(DELETE_ID1,sashiphase);
		//T20_第三者留保債務の削除
		dbacc.delT20(DELETE_ID1,sashiphase);
	}

	/**
	 * 査定入力系テーブルの削除②(事務局差戻)
	 * 
	 * @throws Exception
	 */
	public void doDelSateiTbl2() throws Exception {
		String phase = tori_bean.getPhase();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);
		
		//T15_一次二次査定の削除
		dbacc.delT15(DELETE_ID2,phase);
		//T19_留保債務の削除
		dbacc.delT19(DELETE_ID2,phase);
		//T20_第三者留保債務の削除
		dbacc.delT20(DELETE_ID2,phase);
	}

	/**
	 * T11_文書添付の削除
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doDelT11(String sashimodoshi_type) throws Exception {
		String phase = tori_bean.getPhase();
		String sashiphase = form.getSashiphase();
		String anken_no = tori_bean.getAnken_no();
		List ar_rireki = form.getAr_rireki();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//査定から滞留へ差戻時
		if(TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//査定案件の添付情報削除
			dbacc.delT11(DELETE_ID0,sashiphase,phase,anken_no);
			phase = GS.PHASE_TAIRYU_HANTEI_KENSHO;
			//新規差戻先選択
			if(TYPE4.equals(sashimodoshi_type)){
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
		        	List<String> ankenList = form.getAr_tairyu_anken_no();
		        	anken_no = ankenList.get(i);
					//滞留案件の添付情報削除
					dbacc.delT11(DELETE_ID1,sashiphase,phase,anken_no);
		        }	

			//滞留判定フェーズ差戻
			}else{
				for (int i = 0; i < ar_rireki.size(); i++) {
	    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
	    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
	    				int id = Integer.parseInt(listBean.getRadio_id());
	    				List<RirekiBean> rireki_list = listBean.getRireki_list();
	    				RirekiBean rirekiBean = rireki_list.get(id);
	    				sashiphase = rirekiBean.getPhase();
	    				anken_no = rirekiBean.getAnken_no();
						//滞留案件の添付情報削除
	    				dbacc.delT11(DELETE_ID1,sashiphase,phase,anken_no);
	    			}
	    		}
			}

		}else{
			//事務局差戻
			if(TYPE7.equals(sashimodoshi_type)){
				sashiphase = GS.PHASE_TAISHOSAKI_SENTEI;

			//新規差戻先選択
			}else if(SINKI_MODE.equals(form.getGamen_mode())){

			//フェーズ内差戻
			}else{
				RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
				int id = Integer.parseInt(listBean.getRadio_id());
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				RirekiBean rirekiBean = rireki_list.get(id);
				sashiphase = rirekiBean.getPhase();
				anken_no = rirekiBean.getAnken_no();
			}
			//差戻対象案件の添付情報削除
			dbacc.delT11(DELETE_ID1,sashiphase,phase,anken_no);
		}
	}

	/**
	 * T12_コメントの削除
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doDelT12(String sashimodoshi_type) throws Exception {
		String phase = tori_bean.getPhase();
		String sashiphase = form.getSashiphase();
		String anken_no = tori_bean.getAnken_no();
		List ar_rireki = form.getAr_rireki();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//査定から滞留へ差戻時
		if(TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//査定案件のコメント情報削除
			dbacc.delT12(DELETE_ID0,sashiphase,phase,anken_no);
			phase = GS.PHASE_TAIRYU_HANTEI_KENSHO;
			//新規差戻先選択
			if(TYPE4.equals(sashimodoshi_type)){
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
		        	List<String> ankenList = form.getAr_tairyu_anken_no();
		        	anken_no = ankenList.get(i);
					//滞留案件の添付情報削除
					dbacc.delT12(DELETE_ID1,sashiphase,phase,anken_no);
		        }	

			//滞留判定フェーズ差戻
			}else{
				for (int i = 0; i < ar_rireki.size(); i++) {
	    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
	    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
	    				int id = Integer.parseInt(listBean.getRadio_id());
	    				List<RirekiBean> rireki_list = listBean.getRireki_list();
	    				RirekiBean rirekiBean = rireki_list.get(id);
	    				sashiphase = rirekiBean.getPhase();
	    				anken_no = rirekiBean.getAnken_no();
						//滞留案件のコメント情報削除
	    				dbacc.delT12(DELETE_ID1,sashiphase,phase,anken_no);
	    			}
	    		}
			}

		}else{
			//事務局差戻
			if(TYPE7.equals(sashimodoshi_type)){
				sashiphase = GS.PHASE_TAISHOSAKI_SENTEI;

			//新規差戻先選択
			}else if(SINKI_MODE.equals(form.getGamen_mode())){

			//フェーズ内差戻
			}else{
				RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
				int id = Integer.parseInt(listBean.getRadio_id());
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				RirekiBean rirekiBean = rireki_list.get(id);
				sashiphase = rirekiBean.getPhase();
				anken_no = rirekiBean.getAnken_no();
			}
			//差戻対象案件のコメント情報削除
			dbacc.delT12(DELETE_ID1,sashiphase,phase,anken_no);
		}
	}

	/**
	 * T12_コメントの登録
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doInsT12(String sashimodoshi_type) throws Exception {
		String phase = form.getSashiphase();
		String anken_no = tori_bean.getAnken_no();
		List ar_rireki = form.getAr_rireki();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//査定から滞留へ差戻時
		if(TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//新規差戻先選択
			if(TYPE4.equals(sashimodoshi_type)){
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
		        	List<String> ankenList = form.getAr_tairyu_anken_no();
		        	anken_no = ankenList.get(i);
					//コメント登録
					dbacc.insT12(anken_no,phase);
		        }	

			//滞留判定フェーズ差戻
			}else{
				for (int i = 0; i < ar_rireki.size(); i++) {
	    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
	    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
	    				int id = Integer.parseInt(listBean.getRadio_id());
	    				List<RirekiBean> rireki_list = listBean.getRireki_list();
	    				RirekiBean rirekiBean = rireki_list.get(id);
	    				phase = rirekiBean.getPhase();
	    				anken_no = rirekiBean.getAnken_no();
						//コメント登録
						dbacc.insT12(anken_no,phase);
	    			}
	    		}
			}

		}else{
			//事務局差戻
			if(TYPE7.equals(sashimodoshi_type)){
				phase = form.getSashikbn();

			//新規差戻先選択
			}else if(SINKI_MODE.equals(form.getGamen_mode())){

			//フェーズ内差戻
			}else{
				RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
				int id = Integer.parseInt(listBean.getRadio_id());
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				RirekiBean rirekiBean = rireki_list.get(id);
				phase = rirekiBean.getPhase();
				anken_no = rirekiBean.getAnken_no();
			}
			//コメント登録
			dbacc.insT12(anken_no,phase);
		}
	}

	/**
	 * T04_メール配信の登録
	 * 
     * @param String
	 * @throws Exception
	 */
	public void doInsT04(String sashimodoshi_type) throws Exception {
		InputCheck check = new InputCheck();
		String phase = form.getSashiphase();
		String anken_no = tori_bean.getAnken_no();
		String status = SASHIMODOSHI;
		String bunrui2 = form.getHanyou2();
		String bunrui3 = form.getHanyou3();
		String tanto = null;
		List ar_rireki = form.getAr_rireki();

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SashimodoshiDbAcc dbacc = new SashimodoshiDbAcc(sqlExec, log, appContext);

		//査定から滞留へ差戻時
		if(TYPE3.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type)){
			//新規差戻先選択
			if(TYPE4.equals(sashimodoshi_type)){
				if(check.isNullBlank(form.getTxtTanto())){
					status = MOGITORI;
				}else{
    				tanto = form.getSelectedTantoId();
				}
				for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
		        	List<String> ankenList = form.getAr_tairyu_anken_no();
		        	anken_no = ankenList.get(i);
					//メール配信登録
		        	if(TYPE4.equals(sashimodoshi_type)){
		        	dbacc.insT04_2(anken_no,phase,status,bunrui2,bunrui3,tanto);
		        	}else{
		        		dbacc.insT04(anken_no,phase,status,bunrui2,bunrui3,tanto);
		        	}
		        }	

			//滞留判定フェーズ差戻
			}else{
				for (int i = 0; i < ar_rireki.size(); i++) {
	    			RirekiListBean listBean = (RirekiListBean)ar_rireki.get(i);
	    			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
	    				int id = Integer.parseInt(listBean.getRadio_id());
	    				List<RirekiBean> rireki_list = listBean.getRireki_list();
	    				RirekiBean rirekiBean = rireki_list.get(id);
	    				anken_no = rirekiBean.getAnken_no();
	    				phase = rirekiBean.getPhase();
	    				bunrui2 = rirekiBean.getBunrui2();
	    				tanto = rirekiBean.getTanto_user_id();
	    				if(GS.STATUS_SYONIN_MACHI.equals(rirekiBean.getStatus())){
	    					status = SYONIN;
	    				}
						//メール配信登録
	    				bunrui3 = tori_bean.getBu_cd();
						dbacc.insT04(anken_no,phase,status,bunrui2,bunrui3,tanto);
	    			}
	    		}
			}
		}else{
			//事務局差戻
			if(TYPE7.equals(sashimodoshi_type)){
				phase = form.getSashikbn();
				status = MOGITORI;
				bunrui2 = tori_bean.getBunrui2();
			
			//新規差戻先選択
			}else if(SINKI_MODE.equals(form.getGamen_mode())){
				if(check.isNullBlank(form.getTxtTanto())){
					status = MOGITORI;
				}else{
    				tanto = form.getSelectedTantoId();
				}

			//フェーズ内差戻
			}else{
				RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
				int id = Integer.parseInt(listBean.getRadio_id());
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				RirekiBean rirekiBean = rireki_list.get(id);
				phase = rirekiBean.getPhase();
				bunrui2 = rirekiBean.getBunrui2();
				tanto = rirekiBean.getTanto_user_id();
				if(GS.STATUS_SYONIN_MACHI.equals(rirekiBean.getStatus())){
					status = SYONIN;
				}
			}
			//メール配信登録
			if(GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
				if(TYPE2.equals(sashimodoshi_type) || TYPE4.equals(sashimodoshi_type) || TYPE6.equals(sashimodoshi_type) || TYPE9.equals(sashimodoshi_type)){
					dbacc.insT04_2(anken_no,phase,status,bunrui2,bunrui3,tanto);
				}else{
					bunrui3 = tori_bean.getBu_cd();
					dbacc.insT04(anken_no,phase,status,bunrui2,bunrui3,tanto);
				}
			}else{
				bunrui3 = tori_bean.getBu_cd();
				dbacc.insT04(anken_no,phase,status,bunrui2,bunrui3,tanto);
			}
		}
	}
}