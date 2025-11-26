/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/07		SSC				課題No.191 代行者ID設定値修正 
003		2016/02/26		SSC				BJ201602002 部門廃止対応（一次）
004		2016/03/02		SSC				BJ201602002 部門廃止対応（一次）_本部絞込対応
004		2016/12/26		SSC				BJ201612070 SSO対応
******************************************************************************/
package app.login.bss;

import app.SessionData;
import app.UserBean;
import app.login.dbAcc.MenuDbAcc;
import app.login.form.MenuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * OS2101_メインメニュー ビジネスロジッククラス
 */
public class MenuBss {

	private AppContext appContext	= null;								// APPコンテキスト
	private SqlExecuter sqlExec	= null;								// DBアクセス
	private Log log				= null;								// ログ
	private SessionData cmnData	= null;								// 機能共通セッション
	private MenuForm form			= null;								// アクションフォーム
	private UserBean user_bean		= null;								// ユーザ情報Bean
	private MenuDbAcc dbacc 		= null;
	
	private static final String SATEIKAISYA_CD			= "sateikaisya_cd";
	private static final String SYSTEM_KBN				= "system_kbn";
	private static final String PATTERN_ID				= "pattern_id";
	private static final String DEFAULT_FLG				= "default_flg";
    private static final String SPACE 					= "       ";            // 半角スペース7個
	private static final String BUNRUI2 					= "bunrui2";			// 分類２
    private static final String SATEI_KAISHA_CD 			= "satei_kaisha_cd";    // 査定会社コード
    private static final String KARI_KIJUN_FIRST 			= "1";            		// 基準日区分:1
    private static final String KARI_KIJUN_MIDDLE 		= "3";            		// 基準日区分:3
    private static final String KARI_KIJUN_MIDDLE2 		= "5";            		// 基準日区分:5
    private static final String KIJUN 					= "9";            		// 基準日区分:9
    private static final String KIJUNBI_KBN 				= "kijunbi_kbn";        // 基準日区分
    private static final String TAISHO_YM 				= "taisho_ym";          // 抽出処理日
    private static final String TAISHO_YM_HYOJI	 		= "taisho_ym_hyoji";    // 対象年月
	private static final String SANKO_SATEIKAISYA_CD_KEY	= "SATEIKAISYA_CD";		// 査定会社コード
	private static final String SANKO_SYSTEM_KBN_KEY		= "SYSTEM_KBN";			// システム区分
	private static final String SANKO_BUNRUI2_KEY 		= "BUNRUI2";			// 分類２
	private static final String SANSYO_HONBU_CD 		= "sansyo_honbu_cd";	// 参照本部コード
    private static final String SPACE_HONBU 			= "  ";          		// 半角スペース2個
	private static final String SANKO_HONBU_CD_KEY 		= "SANSYO_HONBU_CD";	// 参照本部コード
	
	/**
	 * コンストラクタ
	 */
	public MenuBss(AppContext appContext) {
		this.appContext = appContext;
		this.sqlExec = appContext.getSqlExecuter();
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (MenuForm)appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得<br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// ログインユーザの既定の業務フローパターンの設定 
		this.setLoginPattern();	
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);

		// 各処理権限の設定
		dbacc.setKengen();
		
		// 参照分類１コードの設定
		this.setLoginSansyoBunrui1();		

		// 参照分類２コードの設定
		this.setLoginSansyoBunrui2();
		
		// 全参照組織【マップ】の設定
		this.setLoginSansyoSoshiki();
		
		// 実施業務【リスト】の設定
		dbacc.getJisshigyoumuList();
		
		// 各画面で使用する汎用項目のタイトル名を取得する
		dbacc.getHanyouLabel();
		// 汎用項目タイトル名の設定
		this.setHanyouLabel();				
		
		// INパラメータを設定する
		this.setInParameter();
		// 業務フロー切替セレクトボックス 
		dbacc.getPatternName();
		// 業務フロー切替セレクトボックスの表示制御
		isPattern_hyoji();
		
		// 査定期セレクトボックスの設定値を取得する
		dbacc.getSateiKi();
		
		// 対象年月リスト
		dbacc.getYmListAll();
		// 対象年月セレクトボックス
		dbacc.getYmList();
		// 初回年月、中間年月、最終年月を設定する
		this.setInitYm();
		
		// 代行画面切替セレクトボックス
		dbacc.getDaikoList();
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();		
		
		return GS.OS2101;
	}
    
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();		
		
		return GS.OS2101;
		
	}	
	
	/**
	 * 業務フロー切替処理 <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String pattern() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);

		// ログインユーザの業務フローパターンの再設定
		this.setChangePattern();
				
		// 各処理権限の再設定
		dbacc.setKengen();
		
		// 参照分類１コードの再設定
		this.setLoginSansyoBunrui1();		

		// 参照分類２コードの再設定
		this.setLoginSansyoBunrui2();
		
		// 実施業務【リスト】の再設定
		dbacc.getJisshigyoumuList();
		
		// 各画面で使用する汎用項目のタイトル名を取得する
		dbacc.getHanyouLabel();
		// 汎用項目タイトル名の設定
		this.setHanyouLabel();
		
		// 査定期セレクトボックスの設定値を取得する
		dbacc.getSateiKi();
		// 対象年月リスト
		dbacc.getYmListAll();
		// 対象年月セレクトボックス
		dbacc.getYmList();
		// 初回年月、中間年月、最終年月を設定する
		this.setInitYm();
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();		
		
		return GS.OS2101;
	}    	
	
	/**
	 * 査定期切替処理 <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String satei() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);
		
		// 対象年月リスト
		dbacc.getYmListAll();
		// 対象年月セレクトボックス
		dbacc.getYmList();
		// 初回年月、中間年月、最終年月を設定する
		this.setInitYm();
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();		
		
		return GS.OS2101;
	} 
	
	/**
	 * 対象年月切替処理 <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String taishou_ym() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();		
		
		return GS.OS2101;
	} 
	
	/**
	 * 代行者切替処理 <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String daiko() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);
		
		// 代行排他チェック
		boolean resualt = dbacc.setCheckDaiko();
		if (!resualt) {
			appContext.setMsgCode(GL.ERR_PERSONLOGIN);
			form.setDaiko(form.getTemp_daiko());
			return GS.OS2101;
		}
		
		// T22_代行排他の削除
		dbacc.setDeleteT2200();
		
		// T22_代行排他の登録
		dbacc.setInsertT2200();
		
		// コミット処理
		dbacc.commit();
		
		// 代行者情報の退避
		InputCheck inpCheck = new InputCheck();
		if (inpCheck.isNullBlank(user_bean.getComDaiko_userId())) {
			// 共)ユーザ情報.代行者ID
			user_bean.setComDaiko_userId(user_bean.getComUserId());		
			// 共)ユーザ情報.代行者名日本語
			user_bean.setComDaiko_user_nm(user_bean.getComUser_Nm());
			// 共)ユーザ情報.代行者名英語
			user_bean.setComDaiko_user_nm_en(user_bean.getComUser_Nm_En());
		}
				
		// 被代行者情報の取得 
		// 被代行者のユーザ情報を取得する
		dbacc.getDaikoUser();
		
		// 被代行者の業務フローパターン【リスト】を取得する
		dbacc.getPattern();
		
		// 既定の業務フローパターンを設定する
		// ログインユーザの既定の業務フローパターンの設定 
		this.setLoginPattern();	
				
		// 各処理権限の設定
		dbacc.setKengen();
				
		// 参照組織を設定する
		// 参照分類１コードの設定
		this.setLoginSansyoBunrui1();		

		// 参照分類２コードの設定
		this.setLoginSansyoBunrui2();
		
		// 全参照組織【マップ】の設定
		this.setLoginSansyoSoshiki();
				
		// 実施業務【リスト】の設定
		dbacc.getJisshigyoumuList();
		
		// 各画面で使用する汎用項目のタイトル名を取得する
		dbacc.getHanyouLabel();
		// 汎用項目タイトル名の設定
		this.setHanyouLabel();	
		
		// 共)ユーザ情報.ユーザIDと共)ユーザ情報.代行ユーザIDが同じ場合
		if (user_bean.getComUserId().equals(user_bean.getComDaiko_userId())) {
			
			//課題No.191
			//修正開始
			// 共)ユーザ情報.代行ユーザIDを初期化する
			//user_bean.setComDaiko_userId(GS.EMPTY_CHARCTER);
			user_bean.setComDaiko_userId(null);
			//修正完了
			// 共)ユーザ情報.代行者名日本語
			user_bean.setComDaiko_user_nm(GS.EMPTY_CHARCTER);
			// 共)ユーザ情報.代行者名英語
			user_bean.setComDaiko_user_nm_en(GS.EMPTY_CHARCTER);
		}		
		// INパラメータを設定する
		this.setInParameter();
		// 業務フロー切替セレクトボックス 
		dbacc.getPatternName();
		// 業務フロー切替セレクトボックスの表示制御
		isPattern_hyoji();
		
		// 査定期セレクトボックスの設定値を取得する
		dbacc.getSateiKi();
		
		// 対象年月リスト
		dbacc.getYmListAll();
		// 対象年月セレクトボックス
		dbacc.getYmList();
		// 初回年月、中間年月、最終年月を設定する
		this.setInitYm();
		
		// ステータスの表示
		dbacc.getStatus();
		
		// 査定結果の表示
		dbacc.getSateiKeka();	
		
		return GS.OS2101;
	} 
	
	/**
	 * ログインユーザの既定の業務フローパターンの設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setLoginPattern() throws Exception {
		
		// 共)業務フローパターン【リスト】が繰り返し、既定の業務フローパターンをユーザ情報Beanに格納する
		for (int i = 0; i < user_bean.getComWorkflowList().size(); i++) {
			HashMap hm = (HashMap)user_bean.getComWorkflowList().get(i);
			
			// 既定フラグ：１のものが判定
			if ("1".equals(String.valueOf(hm.get(DEFAULT_FLG)))) {
				// 業務フローパターンシステム区分
				user_bean.setComWorkflowSystemkbn(String.valueOf(hm.get(SYSTEM_KBN)));
				
				// 業務フローパターン査定会社コード
				user_bean.setComWorkflowSateikaisya_cd(String.valueOf(hm.get(SATEIKAISYA_CD)));
				
				// 業務フローパターンID
				user_bean.setComWorkflowId(String.valueOf(hm.get(PATTERN_ID)));
				
				break;
			}
		}
	}
	
	/**
	 *  参照分類１コードの設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setLoginSansyoBunrui1() throws Exception {				
        
        String bunrui1 = GS.EMPTY_CHARCTER;
        
        // カンマ区切りで設定
        for (int i = 0; i < user_bean.getComWorkflowList().size(); i++) {
        	HashMap hm = (HashMap)user_bean.getComWorkflowList().get(i);
        	if (i == 0) {
            	bunrui1 = GS.SINGLE_QUOTATION + String.valueOf(hm.get(SATEIKAISYA_CD)) + GS.SINGLE_QUOTATION;
        	} else {
            	bunrui1 += GS.COMMA + GS.SINGLE_QUOTATION + String.valueOf(hm.get(SATEIKAISYA_CD)) + GS.SINGLE_QUOTATION;
        	}

        }
        
        user_bean.setComSansyoBunrui1(this.getCutReValue(bunrui1));
        
	}
	
	/**
	 *  参照分類２コードの設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setLoginSansyoBunrui2() throws Exception {				
		
		// ログインユーザの業務フローパターンから参照分類２を取得する
		dbacc.getBunrui2();
		
        String bunrui2 = GS.EMPTY_CHARCTER;
        String sansyo_honbu_cd = GS.EMPTY_CHARCTER;
        
        if (0 < form.getAr_oparation().size()) {
        	
            for (int i = 0; i < form.getAr_oparation().size(); i++) {
            	HashMap hm = (HashMap)form.getAr_oparation().get(i);            	
                
                if (SPACE.equals(String.valueOf(hm.get(BUNRUI2)))) {
                	bunrui2 = null;
                	sansyo_honbu_cd = null;
                	break;
                }
                
            	if (GS.EMPTY_CHARCTER.equals(bunrui2)) {
                	bunrui2 = GS.SINGLE_QUOTATION + String.valueOf(hm.get(BUNRUI2)) + GS.SINGLE_QUOTATION;
            	} else {
                	bunrui2 += GS.COMMA + GS.SINGLE_QUOTATION + String.valueOf(hm.get(BUNRUI2)) + GS.SINGLE_QUOTATION;
            	}
            	
            	// 参照本部コード
            	if (SPACE_HONBU.equals(String.valueOf(hm.get(SANSYO_HONBU_CD)))) {
            		sansyo_honbu_cd = null;
            	} else {
                	if (GS.EMPTY_CHARCTER.equals(sansyo_honbu_cd)) {
                		// nop
                	} else {
                		sansyo_honbu_cd += GS.COMMA;
                	}
            		sansyo_honbu_cd += GS.SINGLE_QUOTATION + String.valueOf(hm.get(SANSYO_HONBU_CD)) + GS.SINGLE_QUOTATION;
            	}
            	
            }
        }
        
        user_bean.setComSansyoBunrui2(this.getCutReValue2(bunrui2));
        user_bean.setComSansyoHonbuCd(sansyo_honbu_cd);
        
	}
	
	/**
	 *   全参照組織【マップ】の設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setLoginSansyoSoshiki() throws Exception {				
        
		// ログインユーザの全参照システム区分を取得する
		dbacc.getSystemKbn();
		        
		String system_kbn = GS.EMPTY_CHARCTER;
		// 取得したシステム区分をシングルコートで括り、カンマ区切りで設定
        for (int i = 0; i < form.getAr_systemkbn().size(); i++) {
        	HashMap hm = (HashMap) form.getAr_systemkbn().get(i);
        	if (i == 0) {
        		system_kbn = GS.SINGLE_QUOTATION + hm.get(SYSTEM_KBN) + GS.SINGLE_QUOTATION;
        	} else {
        		system_kbn += GS.COMMA + GS.SINGLE_QUOTATION + String.valueOf(hm.get(SYSTEM_KBN)) + GS.SINGLE_QUOTATION;
        	}
        }
        
		// ログインユーザの全参照査定会社を取得する
		dbacc.getSateiKaisha();
                
		String satei_kaisha = GS.EMPTY_CHARCTER;
		// 取得した査定会社コードをシングルコートで括り、カンマ区切りで設定
        for (int i = 0; i < form.getAr_satei_kaisha().size(); i++) {
        	HashMap hm = (HashMap) form.getAr_satei_kaisha().get(i);
        	if (i == 0) {
        		satei_kaisha = GS.SINGLE_QUOTATION + hm.get(SATEI_KAISHA_CD) + GS.SINGLE_QUOTATION;
        	} else {
        		satei_kaisha += GS.COMMA + GS.SINGLE_QUOTATION + hm.get(SATEI_KAISHA_CD) + GS.SINGLE_QUOTATION;
        	}
        }
        
        // 空のとき、「''」に空を編集する
        InputCheck inpChk = new InputCheck();
        if (inpChk.isNullBlank(satei_kaisha)) {
        	satei_kaisha = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
        }
        form.setSankou_satei_kaisha(satei_kaisha);                
        
		String sankyo_bunrui2 = GS.EMPTY_CHARCTER;
		String sankyo_honbu_cd = GS.EMPTY_CHARCTER;
        for (int i = 0; i < form.getAr_satei_kaisha().size(); i++) {
        	HashMap hm = (HashMap) form.getAr_satei_kaisha().get(i);
        	form.setSankou_satei_kaisha(GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION);
        	if (!inpChk.isNullBlank(String.valueOf(hm.get(SATEI_KAISHA_CD)))) {        		
            	form.setSankou_satei_kaisha(GS.SINGLE_QUOTATION + String.valueOf(hm.get(SATEI_KAISHA_CD)) + GS.SINGLE_QUOTATION);
        	}
    		// ログインユーザの参照分類２・参照本部コードを取得する
    		dbacc.getSansyoBunrui2();
    		 
            // 取得した分類２・参照本部コードをシングルコートで括り、カンマ区切りで設定
            for (int j = 0; j < form.getAr_bunrui2().size(); j++) {
            	HashMap hm_bunrui2 = (HashMap)form.getAr_bunrui2().get(j);
            	if (GS.EMPTY_CHARCTER.equals(sankyo_bunrui2)) {
            		sankyo_bunrui2 = GS.SINGLE_QUOTATION + hm_bunrui2.get(BUNRUI2) + GS.SINGLE_QUOTATION;
            	} else {
            		sankyo_bunrui2 += GS.COMMA + GS.SINGLE_QUOTATION + hm_bunrui2.get(BUNRUI2) + GS.SINGLE_QUOTATION;
            	}
            	// 参照本部コード
            	if (SPACE_HONBU.equals(String.valueOf(hm_bunrui2.get(SANSYO_HONBU_CD)))) {
            		// nop
            	} else {
                	if (GS.EMPTY_CHARCTER.equals(sankyo_honbu_cd)) {
                		// nop
                	} else {
                		sankyo_honbu_cd += GS.COMMA;
                	}
                	sankyo_honbu_cd += GS.SINGLE_QUOTATION + String.valueOf(hm_bunrui2.get(SANSYO_HONBU_CD)) + GS.SINGLE_QUOTATION;
            	}
            }
        }
        
        HashMap<String, String> comSansyososhiki_all = new HashMap<String, String>();
        comSansyososhiki_all.put(SANKO_SYSTEM_KBN_KEY, this.getCutReValue(system_kbn));									// システム区分
        comSansyososhiki_all.put(SANKO_SATEIKAISYA_CD_KEY, this.getCutReValue(satei_kaisha));							// 査定会社コード
        comSansyososhiki_all.put(SANKO_BUNRUI2_KEY, this.getCutReValue2(sankyo_bunrui2));								// 分類２
        comSansyososhiki_all.put(SANKO_HONBU_CD_KEY, this.getCutReValue2(sankyo_honbu_cd));								// 参照本部コード
        
        // 共)全参照組織【マップ】を機能共通セッションのユーザ情報Beanに設定する
        user_bean.setComSansyososhiki_all(comSansyososhiki_all);               
	}
	
	/**
	 *  汎用項目タイトル名の設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setHanyouLabel() throws Exception {				
		
		// ラベル名１
		cmnData.setLbl_nm1(form.getLabel1());
        
		// ラベル名２
		cmnData.setLbl_nm2(form.getLabel2());
		
		// ラベル名３
		cmnData.setLbl_nm3(form.getLabel3());
		
		// ラベル名４
		cmnData.setLbl_nm4(form.getLabel4());
		
		// ラベル名５
		cmnData.setLbl_nm5(form.getLabel5());
		
		// ラベル名６
		cmnData.setLbl_nm6(form.getLabel6());
		
		// ラベル名７
		cmnData.setLbl_nm7(form.getLabel7());
		
		// ラベル名８
		cmnData.setLbl_nm8(form.getLabel8());
		
		// ラベル名９
		cmnData.setLbl_nm9(form.getLabel9());		
		
		// ラベル名１０
		cmnData.setLbl_nm10(form.getLabel10());
	}	
	
	/**
	 *  業務フロー切替の初期設定 <br>
	 * 
	 * @throws Exception
	 */
	private void setInitYm() throws Exception {				
		
		if (form.getAr_ym() == null) {
			return;
		}
		
    	String first_ym = GS.EMPTY_CHARCTER;					// 初回年月
    	String middle_ym = GS.EMPTY_CHARCTER;					// 中間年月
    	String last_ym = GS.EMPTY_CHARCTER;						// 最終年月
    	String first_ym_hy = GS.EMPTY_CHARCTER;					// 初回年月表示用
    	String middle_ym_hy = GS.EMPTY_CHARCTER;				// 中間年月表示用
    	String last_ym_hy = GS.EMPTY_CHARCTER;					// 最終年月表示用
    	
		for (int i = 0; i < form.getAr_taisho_ym().size(); i++) {
			HashMap hm = (HashMap) form.getAr_taisho_ym().get(i);
			
			// 初回年月
        	if (KARI_KIJUN_FIRST.equals(hm.get(KIJUNBI_KBN))) {
        		first_ym += hm.get(TAISHO_YM);
        		first_ym_hy += hm.get(TAISHO_YM_HYOJI);
        	}
        	
        	// 中間年月
        	if (KARI_KIJUN_MIDDLE.equals(hm.get(KIJUNBI_KBN)) || KARI_KIJUN_MIDDLE2.equals(hm.get(KIJUNBI_KBN))) {
        		
        		if (GS.EMPTY_CHARCTER.equals(middle_ym)) {
        			middle_ym +=  GS.SINGLE_QUOTATION + hm.get(TAISHO_YM) + GS.SINGLE_QUOTATION;
        		} else {
        			middle_ym += GS.COMMA + GS.SINGLE_QUOTATION + hm.get(TAISHO_YM) + GS.SINGLE_QUOTATION;
        		}
        		
        		if (GS.EMPTY_CHARCTER.equals(middle_ym_hy)) {
        			middle_ym_hy += hm.get(TAISHO_YM_HYOJI);
        		} else {
        			middle_ym_hy += "<br/>" + hm.get(TAISHO_YM_HYOJI);
        		}
        		
        	}
        	
        	// 最終年月
        	if (KIJUN.equals(hm.get(KIJUNBI_KBN))) {
        		last_ym += hm.get(TAISHO_YM);
        		last_ym_hy += hm.get(TAISHO_YM_HYOJI);
        	}
        }
		
		if (GS.EMPTY_CHARCTER.equals(first_ym)) {
			first_ym = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
		}
		if (GS.EMPTY_CHARCTER.equals(middle_ym)) {
			middle_ym = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
		}
		if (GS.EMPTY_CHARCTER.equals(last_ym)) {
			last_ym = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
		}
        form.setFirst_ym(first_ym);
        form.setFirst_ym_hy(first_ym_hy);
        form.setMiddle_ym(middle_ym);
        form.setMiddle_ym_list(middle_ym_hy);
        form.setLast_ym(last_ym);
        form.setLast_ym_hy(last_ym_hy);
	}
	
	/**
	 *  業務フローパターン名称の取得のSQL文INパラメータを設定する <br>
	 * 
	 * @throws Exception
	 */
	private void setInParameter() throws Exception {
		
    	String arg_system = null;					// 編集した業務フローパターンシステム区分
    	String arg_satei_kaisha = null;				// 編集した業務フローパターン査定会社コード
    	String arg_pattern_id = null;				// 編集した業務フローパターンID
    	
    	String comma = GS.COMMA + GS.SINGLE_QUOTATION;
		for (int i = 0; i < user_bean.getComWorkflowList().size(); i++) {
			HashMap hm = (HashMap) user_bean.getComWorkflowList().get(i);
			
        	if (0 == i) {
        		arg_system =  GS.SINGLE_QUOTATION + hm.get(SYSTEM_KBN) + GS.SINGLE_QUOTATION;
        		arg_satei_kaisha = GS.SINGLE_QUOTATION + hm.get(SATEIKAISYA_CD) + GS.SINGLE_QUOTATION;
        		arg_pattern_id = GS.SINGLE_QUOTATION + hm.get(PATTERN_ID) + GS.SINGLE_QUOTATION;
        	} else {
        		arg_system += comma + hm.get(SYSTEM_KBN) + GS.SINGLE_QUOTATION;
        		arg_satei_kaisha += comma + hm.get(SATEIKAISYA_CD) + GS.SINGLE_QUOTATION;
        		arg_pattern_id += comma + hm.get(PATTERN_ID) + GS.SINGLE_QUOTATION;
        	}
        	
        }
		
        form.setArg_system(this.getCutReValue(arg_system));
        form.setArg_satei_kaisha(this.getCutReValue(arg_satei_kaisha));
        form.setArg_pattern_id(this.getCutReValue(arg_pattern_id));
        
        if (GS.EMPTY_CHARCTER.equals(form.getArg_system())) {
        	form.setArg_system(GS.SINGLE_QUOTATION+GS.SINGLE_QUOTATION);
        }
        if (GS.EMPTY_CHARCTER.equals(form.getArg_satei_kaisha())) {
        	form.setArg_satei_kaisha(GS.SINGLE_QUOTATION+GS.SINGLE_QUOTATION);
        }
        if (GS.EMPTY_CHARCTER.equals(form.getArg_pattern_id())) {
        	form.setArg_pattern_id(GS.SINGLE_QUOTATION+GS.SINGLE_QUOTATION);
        }
	}
		
	/**
	 * 選択された業務フローパターンのものを設定する <br>
	 * 
	 * @throws Exception
	 */
	private void setChangePattern() throws Exception {
		
		String selId = form.getPattern_id();
		String selSystemkbn = form.getPattern_system_kbn();
		String selSateikaisyacd = form.getPattern_sateikaisya_cd();
		// 画面の 業務フロー切替【リスト】が繰り返し、ユーザ情報Beanに格納する
		for (int i = 0; i < user_bean.getComWorkflowList().size(); i++) {
			HashMap hm = (HashMap)user_bean.getComWorkflowList().get(i);
			
			if (selId.equals(hm.get(PATTERN_ID)) && selSystemkbn.equals(hm.get(SYSTEM_KBN)) && selSateikaisyacd.equals(hm.get(SATEIKAISYA_CD))) {
				// 業務フローパターンシステム区分
				user_bean.setComWorkflowSystemkbn(String.valueOf(hm.get(SYSTEM_KBN)));
				
				// 業務フローパターン査定会社コード
				user_bean.setComWorkflowSateikaisya_cd(String.valueOf(hm.get(SATEIKAISYA_CD)));
				
				// 業務フローパターンID
				user_bean.setComWorkflowId(String.valueOf(hm.get(PATTERN_ID)));
				
				break;
			}
		}
	}
	
	/**
	 * 重複の値を切る <br>
	 * 
	 * @throws Exception
	 */
	private String getCutReValue(String value) throws Exception {
		
		InputCheck inpChk = new InputCheck();
		if (inpChk.isNullBlank(value)) {
			return GS.EMPTY_CHARCTER;
		}
		
		String[] val = value.split(GS.COMMA);
		
		String old_val = GS.EMPTY_CHARCTER;	
		String new_val = GS.EMPTY_CHARCTER;
		for (int i = 0; i < val.length; i++) {
			if (!old_val.equals(val[i])) {
				new_val += val[i] + GS.COMMA;
			}			
			old_val = val[i];
		}
				
		// 最後カンマを切る
		new_val = new_val.substring(0, new_val.length()-1);
		
		return new_val;
	}

	/**
	 * 重複の値を切る <br>
	 * ・文字列の並びに関係なく重複を排除する。<br>
	 *  （getCutReValue は隣同士の比較しか行わない。）
	 * 
	 * @throws Exception
	 */
	private String getCutReValue2(String value) throws Exception {
		
		InputCheck inpChk = new InputCheck();
		if (inpChk.isNullBlank(value)) {
			return value;
		}
		
		String[] val = value.split(GS.COMMA);
		
		String new_val = GS.EMPTY_CHARCTER;
		List<String> val_list = new ArrayList<String>();
		for (int i = 0; i < val.length; i++) {
			if (!val_list.contains(val[i])) {
				val_list.add(val[i]);
				new_val += val[i] + GS.COMMA;
			}
		}
		
		// 最後カンマを切る
		new_val = new_val.substring(0, new_val.length()-1);
		
		return new_val;
	}

	/**
	 * 業務フロー切替セレクトボックスの表示制御 <br>
	 * 
	 * @throws Exception
	 */
	private void isPattern_hyoji() throws Exception {
		int pattern_size = form.getAr_pattern().size();
		if(1 < pattern_size ){
			form.setPattern_flg(true);
		}else{
			form.setPattern_flg(false);
		}
	}
	
	/**
	 * 言語切替処理 <br>
	 * 日本語⇔英語の切り替えを行う。<br>
	 * 切替を行うと、ログイン後の初期表示（各セレクトボックスデフォルト値）になる。<br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String gengo() throws Exception {

		Object[] daikoList = form.getAr_daiko().keySet().toArray();
		form.setDaiko((String)daikoList[0]);

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new MenuDbAcc(sqlExec, log, appContext);
		
		// T22_代行排他の削除
		dbacc.setDeleteT2200();
		
		// T22_代行排他の登録
		dbacc.setInsertT2200();
		
		// コミット処理
		dbacc.commit();
		
		// 代行者情報の退避
		InputCheck inpCheck = new InputCheck();
		if (!inpCheck.isNullBlank(user_bean.getComDaiko_userId())) {
			// 共)ユーザ情報.代行者ID
			user_bean.setComDaiko_userId("");		
			// 共)ユーザ情報.代行者名日本語
			user_bean.setComDaiko_user_nm("");
			// 共)ユーザ情報.代行者名英語
			user_bean.setComDaiko_user_nm_en("");
		}
				
		// 被代行者情報の取得 
		// 被代行者のユーザ情報を取得する
		dbacc.getDaikoUser();
		
		// 被代行者の業務フローパターン【リスト】を取得する
		dbacc.getPattern();
		
		// 既定の業務フローパターンを設定する
		// ログインユーザの既定の業務フローパターンの設定 
		this.setLoginPattern();	

		// ログインユーザで初期表示
		this.executeInit();

		return GS.OS2101;
	}
}