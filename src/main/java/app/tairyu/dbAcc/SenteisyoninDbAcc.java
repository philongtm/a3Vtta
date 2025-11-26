/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.50 査定期表示対応 
003		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正 
004		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.dbAcc;


import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SenteisyoninForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
* OB2104_対象先選定_承認一覧 DBアクセスクラス
*/
public class SenteisyoninDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private SenteisyoninForm form = null;				// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KBN_VAL              = "kbn_val";
	private static final String PHASE                = "phase";
	private static final String STATUS               = "status";
	private static final String ANKEN_NO             = "anken_no";
	private static final String KIKAN_TORI_CD        = "kikan_tori_cd";
	private static final String BUSINESS_NM          = "business_nm";
	private static final String SAIKEN_KINGAKU       = "saiken_kingaku";
	private static final String SAIKEN_TUUKA_CD      = "saiken_tuuka_cd";	
	private static final String YM                   = "ym";
	private static final String YM_HYOJI             = "ym_hyoji";
	private static final String SATEI_KAISHA_CD      = "satei_kaisha_cd";
	private static final String BUNRUI2              = "bunrui2";
	private static final String INIT_BUNRUI2         = "init_bunrui2";
	private static final String INIT_BU_CD           = "init_bu_cd";
	private static final String BU_CD                = "bu_cd";
	private static final String INIT_BUNRUI3         = "init_bunrui3";
	private static final String INIT_BUNRUI2_NM      = "init_bunrui2_nm";
	private static final String INIT_BU_NM           = "init_bu_nm";
	private static final String INIT_BUNRUI3_NM      = "init_bunrui3_nm";
	private static final String SOSHIKI              = "soshiki";
	private static final String HOJI_USER_ID         = "hoji_user_id";
	private static final String DAIKO_USER_ID        = "daiko_user_id";
	private static final String TANTO_USER_NM        = "tanto_user_nm";
	private static final String DAIKO_USER_NM        = "daiko_user_nm";
	private static final String TAISHO_KBN           = "taisho_kbn";
	private static final String TAISHOGAI_FLG        = "taishogai_flg";
	private static final String GOLF_FLG             = "golf_flg";
	private static final String KTK                  = "ktk";
	private static final String GAIBU_KTK            = "gaibu_ktk";
	private static final String KTK_KIKAN            = "ktk_kikan";
	private static final String FSS                  = "fss";
	private static final String DUNS_RATING          = "duns_rating";
	private static final String OYA_DUNS_NO          = "oya_duns_no";
	private static final String OYA_KTK              = "oya_ktk";
	private static final String OYA_BUSINESS_NM      = "oya_business_nm";
	private static final String OYA_ITTAI_DOKURITU   = "oya_ittai_dokuritu";
	private static final String TOGO_TORI_CD         = "togo_tori_cd";
	private static final String SIKIBETU_CD          = "sikibetu_cd";
	private static final String SATEI_KI             = "satei_ki";
	private static final String HANKI_SIHANKI_KBN    = "hanki_sihanki_kbn";
	private static final String JIYU_CD              = "jiyu_cd";
	private static final String JIYU_NM              = "jiyu_nm";
	private static final String SYSTEM_KBN           = "system_kbn";
	private static final String MISE_CD              = "mise_cd";
	private static final String KIJUNBI_KBN          = "kijunbi_kbn";
	private static final String SYORI_KAISU          = "syori_kaisu";
	private static final String SASI_TEN_FLG         = "sasi_ten_flg";
	// 課題No.50
	// 追加開始
	private static final String SATEIKI_HYOJI			= "satei_ki_hyoji";
	// 追加完了

	private static final String SHOW                 = "show";		//区分キー（表示件数）
    private static final String HS_KBN_HANTEI        = "2";			//判定査定区分 '2'： 一次・二次査定
    private static final String NYURYOKU_KBN         = "80";			//入力区分 '80'： 承認
	private static final String DAIKO                = "( 代行 ";
	private static final String DAIKO_EN             = "( proxy ";
	private static final String CHECKBOX_STATUS_OFF  = "off";			//承認チェック
	private static final String GOLF_ON              = "2";			//2:ゴルフ会員権(画面で設定されたゴルフ会員権)
	private static final String HAISINZUMI_FLG       = "N";			//配信済フラグ：未配信
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.##";   // 金額のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";   // 金額のフォーマット：海外
	private static final String DELETE_ID0      = "0";				//査定関連テーブル削除時の処理ID

	private static final String SP_SS_OB2104_SELECT_ICHIRAN   = "SP_SS_OB2104_SELECT_ICHIRAN";	//対象先選定承認一覧情報取得プロシージャ
	private static final String SP_SS_OB2104_UPDATE_T1400     = "SP_SS_OB2104_UPDATE_T1400";		//T14_査定進捗管理更新プロシージャ
	private static final String SP_SS_O_UPDATE_T1400          = "SP_SS_O_UPDATE_T1400";			//T14_査定進捗管理更新プロシージャ
	private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";			//T13_入力履歴登録プロシージャ
	private static final String SP_SS_O_INSERT_T0400          = "SP_SS_O_INSERT_T0400";			//T04_メール配信登録プロシージャ
	private static final String SP_SS_O_DELETE_T1500          = "SP_SS_O_DELETE_T1500";			//T15_一次二次査定削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1600          = "SP_SS_O_DELETE_T1600";			//T16_引当金検討対象BS明細削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1700          = "SP_SS_O_DELETE_T1700";			//T17_引当金判定表示用削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1900          = "SP_SS_O_DELETE_T1900";			//T19_留保債務削除用プロシージャ
	private static final String SP_SS_O_DELETE_T2000          = "SP_SS_O_DELETE_T2000";			//T20_第三者留保債務削除取得用プロシージャ
	
	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String satei_kaisha_cd;	// 業務フローパターン査定会社コード
	private String workflowSystemkbn;	// 業務フローパターンシステム区分
    private String comLangMode;        // 共)言語モード

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SenteisyoninDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (SenteisyoninForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
		satei_kaisha_cd = user_bean.getComWorkflowSateikaisya_cd();
		workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    userId = GS.EMPTY_CHARCTER;
	    satei_kaisha_cd = GS.EMPTY_CHARCTER;
	    workflowSystemkbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
	}

	/**
	 * 表示件数セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getShow() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(SHOW,workflowSystemkbn,comLangMode);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			form.setAr_show(ar_show);	    	
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
		
	/**
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getMeisai() throws SQLException {

		ResultSet rs = null;
        String formatType = GS.EMPTY_CHARCTER;

        //金額のフォーマット
        if(GS.GSS.equals(workflowSystemkbn)){
        	formatType = NUM_FMT_KOKUNAI;
        }else{
        	formatType = NUM_FMT_KAIGAI;
        }

        //ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2104_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(comLangMode);			
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);	 
	    	
	    	// ActionForm に取得値を格納
	    	List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();	// 明細配列
			InputCheck check = new InputCheck();
	    	int i = 0;
	    	while ( rs.next() ) {
				TorihikisakiBean listBean = new TorihikisakiBean();
				
				//id
				listBean.setId(Function.getStringOfInt(i));
				
				// 案件Ｎｏ．
				listBean.setAnken_no(rs.getString(ANKEN_NO));
				
				// 勘定先コード
				listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
				
				// 勘定先名称
				listBean.setKanjo_nm(rs.getString(BUSINESS_NM));

				// 債権残計
				listBean.setSaiken_kingaku(Function.format(formatType,rs.getDouble(SAIKEN_KINGAKU))+ rs.getString(SAIKEN_TUUKA_CD));
				
				// 対象年月
				listBean.setTaisyo_ym(rs.getString(YM));

				// 対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(YM_HYOJI));

				// 査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
				
				// 分類２
				listBean.setBunrui2(rs.getString(BUNRUI2));
				
				// 初期分類２
				listBean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
				
				// 初期部コード
				listBean.setInit_bu_cd(rs.getString(INIT_BU_CD));
				
				// 部コード
				listBean.setBu_cd(rs.getString(BU_CD));
				
				// 初期分類３
				listBean.setInit_bunrui3(rs.getString(INIT_BUNRUI3));
				
				// 初期分類２名称
				listBean.setInit_bunrui2_nm(rs.getString(INIT_BUNRUI2_NM));
				
				// 初期部名称
				listBean.setInit_bu_nm(rs.getString(INIT_BU_NM));

				// 初期分類３名称
				listBean.setInit_bunrui3_nm(rs.getString(INIT_BUNRUI3_NM));
									
				//組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				
				// 案件保持ユーザID
				listBean.setHoji_user_id(rs.getString(HOJI_USER_ID));
				
				// 代行ユーザID
				listBean.setDaiko_user_id(rs.getString(DAIKO_USER_ID));
				
				// 担当者
				StringBuffer daiko_nm = new StringBuffer(GS.EMPTY_CHARCTER);
				if(!check.isNullBlank(rs.getString(DAIKO_USER_NM))){
					//代行者IDが存在する場合	
					if(comLangMode.equals(GS.LANG_JA)){					
						daiko_nm.append(DAIKO);
					}else{
						daiko_nm.append(DAIKO_EN);
					}
					daiko_nm.append(rs.getString(DAIKO_USER_NM))
							.append(GS.SPACE_CHARCTER)
							.append(GS.KAKKO_MIGI);					
				}
				if(!check.isNullBlank(rs.getString(TANTO_USER_NM))){
					listBean.setTanto_nm(rs.getString(TANTO_USER_NM) + daiko_nm);
				}
				
				// 選定区分
				listBean.setSentei_kbn(rs.getString(TAISHO_KBN));
				
				// 対象外フラグ
				listBean.setTaisyogai_flg(rs.getString(TAISHOGAI_FLG));
				
				// ゴルフ会員権フラグ
				listBean.setGolf_flg(rs.getString(GOLF_FLG));
				
				// 信用格付情報
				listBean.setSinyoktk(rs.getString(KTK));
				
				// 外部格付
				listBean.setGaibu_ktk(rs.getString(GAIBU_KTK));
				
				// 格付機関
				listBean.setKtk_kikan(rs.getString(KTK_KIKAN));
				
				// FSS
				listBean.setFss(rs.getString(FSS));
				
				// DUNS Rating
				listBean.setDuns_rating(rs.getString(DUNS_RATING));
				
				// 親会社DUNS_NO
				listBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
				
				// 親会社格付
				listBean.setOya_ktk(rs.getString(OYA_KTK));
				
				// 親会社名称
				listBean.setOya_business_nm(rs.getString(OYA_BUSINESS_NM));
				
				// 親会社一体独立
				listBean.setOya_ittai_dokuritu(rs.getString(OYA_ITTAI_DOKURITU));
				
				// 統合取引先コード
				listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				
				// 識別コード
				listBean.setShikibetu_cd(rs.getString(SIKIBETU_CD));
				
				// フェーズ
				listBean.setPhase(rs.getString(PHASE));
				
				// ステータス
				listBean.setStatus(rs.getString(STATUS));
				
				// 査定期
				listBean.setSatei_ki(rs.getString(SATEI_KI));

				// 課題No.50
				// 追加開始
				// 査定期表示用
				listBean.setSatei_ki_hyouji(rs.getString(SATEIKI_HYOJI));
				// 追加完了

				// 半期・四半期区分
				listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));

				// 抽出事由コード
				listBean.setJiyu_cd(rs.getString(JIYU_CD));

				// 抽出事由名称
				listBean.setJiyu_nm(rs.getString(JIYU_NM));

				// システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				
				// 店コード
				listBean.setMise_cd(rs.getString(MISE_CD));
				
				// 基準日区分
				listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
				
				// 処理回数
				listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));

				// 差戻・転送フラグ
				listBean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));
				
				// 承認チェック
				listBean.setSyonin_chk(CHECKBOX_STATUS_OFF);
				
	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, listBean);
	    		i++;   
	    	}
		    	
		    // ActionForm に明細を格納
		    form.setAr_meisai(ar_meisai);    
		    // ページ設定
		    form.setPager(ar_meisai);
		
	    } finally {
	    	if (rs != null) {
	    		try {
	    			//Resultset close
	    			rs.close();
	    		} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
	    		}
	    	}
	    }
	}	

    /**
     * T14_査定進捗管理の更新(取戻不可とする) <br>
     * 
     * @param meisaiBean
     * @throws SQLException
     */
    public void upd_torimodoshi_flg() throws SQLException {

    	InputCheck check = new InputCheck();

		ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2104_UPDATE_T1400, sqlExec);
            exCstmt.setStringIn(GS.ON);
    		exCstmt.setStringIn(workflowSystemkbn);
    		exCstmt.setStringIn(satei_kaisha_cd);
    		exCstmt.setStringIn(userId);
    		exCstmt.setStringIn(GS.PHASE_TAISHOSAKI_SENTEI);
    		exCstmt.setStringIn(GS.PHASE_KARIKIJUN_SATEI_TUIKA);
    		exCstmt.setStringIn(GS.STATUS_SYONIN_MACHI);
    		if(check.isNullBlank(user_bean.getComDaiko_userId())){
                exCstmt.setStringIn(userId);                
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());                
            }

			// SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
	
	
	/**
	 * T14_査定進捗管理の更新 <br>
	 * 
	 * @exception SQLException
	 */
	public void updT14(TorihikisakiBean meisaiBean) throws SQLException {

		InputCheck check = new InputCheck();

	    String taishogai_flg = meisaiBean.getTaisyogai_flg();	// 対象外フラグ
	    String golf_flg = meisaiBean.getGolf_flg();				// ゴルフ会員権フラグ
        String comDaiko_userId = user_bean.getComDaiko_userId();// 代行ユーザID
   		
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);

		exCstmt.setStringIn(meisaiBean.getAnken_no());
  		if(GS.ON.equals(taishogai_flg) || GOLF_ON.equals(golf_flg)){
			//対象外、ゴルフ会員権の場合、対象先選定完了で更新
			exCstmt.setStringIn(meisaiBean.getPhase());
			exCstmt.setStringIn(GS.STATUS_KANRYO);
			//課題No.129
			//追加開始
			//exCstmt.setStringIn(meisaiBean.getHoji_user_id());
			exCstmt.setStringIn(null);
			//追加完了
			exCstmt.setStringIn(null);
		}else{
			//対象外、ゴルフ会員権でない場合、一次査定未処理で更新
			exCstmt.setStringIn(GS.PHASE_ICHIJI_SATEI);
			exCstmt.setStringIn(GS.STATUS_MISYORI);			
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(null);
		}
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
		exCstmt.setStringIn(GS.ON);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);

		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}	
		}
	}

	/**
	 * T13_入力履歴の登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void insT13(TorihikisakiBean meisaiBean) throws SQLException {

		InputCheck check = new InputCheck();

	    // INパラメータ取得
	    String anken_no = meisaiBean.getAnken_no();								// 案件No.
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();					// 査定会社コード
        String comUser_Nm = user_bean.getComUser_Nm();							// 担当者名日本語
        String comUser_Nm_En = user_bean.getComUser_Nm_En();					// 担当者名英語
        String comSyozokuSoshiki_Nm = user_bean.getComSyozokuSoshiki_Nm();		// 所属部署名日本語
        String comSyozokuSoshiki_Nm_En = user_bean.getComSyozokuSoshiki_Nm_En();// 所属部署名英語
        String phase = meisaiBean.getPhase();									// フェーズ
        String comDaiko_userId = user_bean.getComDaiko_userId();				// 代行ユーザID
        String comDaiko_user_nm = user_bean.getComDaiko_user_nm();				// 代行者名日本語
        String comDaiko_user_nm_en = user_bean.getComDaiko_user_nm_en();		// 代行者名英語

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
        exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(HS_KBN_HANTEI);
        exCstmt.setStringIn(sateikaisya_cd);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(comUser_Nm);
        exCstmt.setStringIn(comUser_Nm_En);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm_En);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(NYURYOKU_KBN);
        exCstmt.setStringIn(userId);        
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(null);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        	exCstmt.setStringIn(comDaiko_user_nm);
        	exCstmt.setStringIn(comDaiko_user_nm_en);
        }
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
		

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);

		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}	
		}
	}
	
	/**
	 * T04_メール配信の登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void insT04(TorihikisakiBean meisaiBean) throws SQLException {

		InputCheck check = new InputCheck();

	    // INパラメータ取得
	    String anken_no = meisaiBean.getAnken_no();								// 案件No.
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();					// 査定会社コード
        String taisyo_ym = meisaiBean.getTaisyo_ym();							// 対象年月
        String bunrui2 = meisaiBean.getBunrui2();								// 分類２
        String bu_cd = meisaiBean.getBu_cd();									// 部コード
        String comDaiko_userId = user_bean.getComDaiko_userId();				// 代行ユーザID

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
        exCstmt.setStringIn(userId);        
        exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(sateikaisya_cd);
        exCstmt.setStringIn(bunrui2);
        // 部コード(システム区分01の場合のみ設定)
        if(GS.GSS.equals(meisaiBean.getSystem_kbn())){
            exCstmt.setStringIn(bu_cd);
        } else {
        	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        }
        exCstmt.setStringIn(GS.PHASE_ICHIJI_SATEI);
        exCstmt.setStringIn(GS.STATUS_MISYORI);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(HAISINZUMI_FLG);
        exCstmt.setStringIn(form.toString());
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);        
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);

		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}	
		}
	}
	
    /**
     * T15_一次二次査定の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT15(TorihikisakiBean meisaiBean) throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1500, sqlExec);
		exCstmt.setStringIn(meisaiBean.getAnken_no());
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(DELETE_ID0);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T16_引当金検討対象BS明細の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT16(TorihikisakiBean meisaiBean) throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1600, sqlExec);
		exCstmt.setStringIn(meisaiBean.getAnken_no());
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T17_引当金判定表示用の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT17(TorihikisakiBean meisaiBean) throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1700, sqlExec);
		exCstmt.setStringIn(meisaiBean.getAnken_no());
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T19_留保債務の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT19(TorihikisakiBean meisaiBean) throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1900, sqlExec);
		exCstmt.setStringIn(meisaiBean.getAnken_no());
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(DELETE_ID0);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T20_第三者留保債務の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT20(TorihikisakiBean meisaiBean) throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T2000, sqlExec);
		exCstmt.setStringIn(meisaiBean.getAnken_no());
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(DELETE_ID0);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

}