/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/17		SSC				課題No.209 承認プルダウン修正
003		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SenteisyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
* OB2102_対象先選定_選定先詳細 DBアクセスクラス
*/
public class SenteisyosaiDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
    private TorihikisakiBean tori_bean = null;      	// 取引先情報
	private SenteisyosaiForm form = null;				// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String CNT                  = "cnt";
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KBN_VAL              = "kbn_val";
	private static final String TOGO_ID              = "togo_id";
	private static final String TANTO_NAME           = "tanto_name";
	private static final String COMMENT_VAL          = "comment_val";
    private static final String OUT_SATEI_ANKEN_NO	= "OUT_SATEI_ANKEN_NO";     // 査定案件NO
    private static final String OUT_BUNRUI2			= "OUT_BUNRUI2";            // 分類２
    private static final String OUT_BU_CD			= "OUT_BU_CD";              // 部コード

	private static final String TAISYOGAI_KBN   = "taisyogai_kbn";	//区分キー（対象外区分）
	private static final String JIYUU_CD        = "jiyuu_cd";			//区分キー（抽出事由）
    private static final String HS_KBN_HANTEI   = "2";				//判定査定区分 '2'： 一次・二次査定
	private static final String HAISINZUMI_FLG  = "N";				//配信済フラグ'N'：未配信
	private static final String GOLF_KAIINKEN   = "2";				//ゴルフ会員権(更新用)
	private static final String JIYU_CD35       = "35";				//四半期フラグ先
	private static final String DELETE_ID0      = "0";				//査定関連テーブル削除時の処理ID

	private static final String SP_SS_OB2102_SELECT_SHONINSHA = "SP_SS_OB2102_SELECT_SHONINSHA";	//承認担当者取得用プロシージャ
	private static final String SP_SS_OL_SELECT_T1200         = "SP_SS_OL_SELECT_T1200";			//コメント取得プロシージャ
	private static final String SP_SS_O_UPDATE_T1400          = "SP_SS_O_UPDATE_T1400";			//T14更新処理プロシージャ
	private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";			//T13登録処理プロシージャ
	private static final String SP_SS_OB2102_UPDATE_T1400     = "SP_SS_OB2102_UPDATE_T1400";		//T14更新処理プロシージャ
	private static final String SP_SS_O_INSERT_T0400          = "SP_SS_O_INSERT_T0400";			//T04登録プロシージャ
	private static final String SP_SS_O_DELETE_T1200          = "SP_SS_O_DELETE_T1200";			//T12削除プロシージャ
	private static final String SP_SS_O_INSERT_T1200          = "SP_SS_O_INSERT_T1200";			//T12登録プロシージャ
	private static final String SP_SS_OB2102_UPDATE_T0100     = "SP_SS_OB2102_UPDATE_T0100";		//T01更新プロシージャ
	private static final String SP_SS_OB_SELECT_T0700         = "SP_SS_OB_SELECT_T0700";			//チャンピオン部重複チェックプロシージャ
	private static final String SP_SS_OB2102_SELECT_T0800     = "SP_SS_OB2102_SELECT_T0800";		//滞留判定実施中チェックプロシージャ
	private static final String SP_SS_OB2102_SELECT_T1400     = "SP_SS_OB2102_SELECT_T1400";		//引当金確認実施中チェックプロシージャ
	private static final String SP_SS_OB2102_INSERT_T1600     = "SP_SS_OB2102_INSERT_T1600";		//T16登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_T1601     = "SP_SS_OB2102_INSERT_T1601";		//T16登録処理プロシージャ(引当金確認事務局差戻時)
	private static final String SP_SS_OB_INSERT_SATEISTAT00   = "SP_SS_OB_INSERT_SATEISTAT00";	//T17登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_T1400     = "SP_SS_OB2102_INSERT_T1400";		//T14登録処理プロシージャ
	private static final String SP_SS_OB2102_DELETE_T0200     = "SP_SS_OB2102_DELETE_T0200";		//T02削除処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_T0200     = "SP_SS_OB2102_INSERT_T0200";		//T02登録処理プロシージャ
	private static final String SP_SS_OB2102_SELECT_T0100     = "SP_SS_OB2102_SELECT_T0100";		//T01存在チェックプロシージャ
	private static final String SP_SS_OB2102_INSERT_T0100     = "SP_SS_OB2102_INSERT_T0100";		//T01登録処理プロシージャ
	private static final String SP_SS_OB2102_UPDATE_T0101     = "SP_SS_OB2102_UPDATE_T0101";		//T01更新処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_T1500     = "SP_SS_OB2102_INSERT_T1500";		//T15登録処理プロシージャ
	private static final String SP_SS_OB2102_DELETE_TAIHI     = "SP_SS_OB2102_DELETE_TAIHI";		//退避情報削除処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_E0100     = "SP_SS_OB2102_INSERT_E0100";		//E01_統合対比退避登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_E0200     = "SP_SS_OB2102_INSERT_E0200";		//E02_統合マスタ退避登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_E0300     = "SP_SS_OB2102_INSERT_E0300";		//E03_格付退避登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_E0400     = "SP_SS_OB2102_INSERT_E0400";		//E04_D&B企業情報登録処理プロシージャ
	private static final String SP_SS_OB2102_INSERT_E0500     = "SP_SS_OB2102_INSERT_E0500";		//E05_財務退避登録処理プロシージャ
	private static final String SP_SS_O_DELETE_T1500          = "SP_SS_O_DELETE_T1500";			//T15_一次二次査定削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1600          = "SP_SS_O_DELETE_T1600";			//T16_引当金検討対象BS明細削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1700          = "SP_SS_O_DELETE_T1700";			//T17_引当金判定表示用削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1900          = "SP_SS_O_DELETE_T1900";			//T19_留保債務削除用プロシージャ
	private static final String SP_SS_O_DELETE_T2000          = "SP_SS_O_DELETE_T2000";			//T20_第三者留保債務削除取得用プロシージャ
	private static final String SP_SS_OB2102_UPDATE_T1401     = "SP_SS_OB2102_UPDATE_T1401";		//T14更新処理プロシージャ
	
	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String satei_kaisha_cd;	// 査定会社コード
	private String mise_cd;			// 店コード
	private String systemkbn;			// システム区分
    private String comLangMode;        // 共)言語モード
	private String anken_no;			// 案件No
	private String phase;				// フェーズ
	private String taisyo_ym;			// 対象年月
	private String sateiki;			// 査定期
	private String kijunbi_kbn;		// 基準日区分
	private String syori_kaisu;		// 処理回数
	private String tori_cd;			// 取引先コード
	private String tori_nm;			// 取引先名称
	private String togo_tori_cd;		// 統合取引先コード
	private String hanki_sihanki_kbn;	// 半期四半期区分
	private String comDaiko_userId;	// 代行ユーザID

    // OUTパラメータ
    private String outAnkenNoT14 = null;    // 査定案件NO
    private String outBunrui2T14 = null;    // 分類２
    private String outBu_cdT14 = null;      // 部コード

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SenteisyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
		form = (SenteisyosaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
        comLangMode = cmnData.getComLangMode();
		systemkbn = tori_bean.getSystem_kbn();
		satei_kaisha_cd = tori_bean.getSateikaisya_cd();
		mise_cd = tori_bean.getMise_cd();
        anken_no = tori_bean.getAnken_no();
        phase = tori_bean.getPhase();
		taisyo_ym = tori_bean.getTaisyo_ym();
		sateiki = tori_bean.getSatei_ki();
		kijunbi_kbn = tori_bean.getKijunbi_kbn();
		syori_kaisu = tori_bean.getSyori_kaisu();
		tori_cd = tori_bean.getKanjo_cd();
		tori_nm = tori_bean.getKanjo_nm();
		togo_tori_cd = tori_bean.getTogo_tori_cd();
		hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();
        comDaiko_userId = user_bean.getComDaiko_userId();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    userId = GS.EMPTY_CHARCTER;
	    satei_kaisha_cd = GS.EMPTY_CHARCTER;
	    systemkbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
	}

	/**
	 * 対象外区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTaishogai_kbn() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(TAISYOGAI_KBN,systemkbn,comLangMode);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_taisyogai = new LinkedHashMap<String,String>();

			//ブランク設定
			ar_taisyogai.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				ar_taisyogai.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			form.setAr_taisyogai(ar_taisyogai);	    	
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
		
	/**
	 * 承認担当者セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSyonin_tanto() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_SELECT_SHONINSHA, sqlExec);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(phase);
		exCstmt.setResultSet(RESULTSET);
		
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_syonin_tanto = new LinkedHashMap<String,String>();

			//ブランク設定
			ar_syonin_tanto.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				//課題No.209
				//修正開始
				//ar_syonin_tanto.put(rs.getString(TANTO_NAME),rs.getString(TOGO_ID));
				ar_syonin_tanto.put(rs.getString(TOGO_ID),rs.getString(TANTO_NAME));
				//修正完了
			}
			form.setAr_syonin_tanto(ar_syonin_tanto);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
		
	/**
	 * 抽出事由セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTyusyutu_jiyu() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(JIYUU_CD,systemkbn,comLangMode);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_tyusyutu_jiyu = new LinkedHashMap<String,String>();
			ar_tyusyutu_jiyu.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);	    			
			while ( rs.next() ) {
				//第2/4四半期の場合、リ企指定のみ格納
				if(GS.HANKI.equals(hanki_sihanki_kbn) && JIYU_CD35.equals(rs.getString(KBN_VAL))){
					continue;
				}
				ar_tyusyutu_jiyu.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
			}
			form.setAr_tyusyutu_jiyu(ar_tyusyutu_jiyu);
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
		
	/**
	 * 対象外・追加コメント取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getComment() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);			
		exCstmt.setStringIn(GS.COMMENT_VAL_97);			
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
			// ActionForm に取得値を格納
			if ( rs.next() ) {
				form.setComment(rs.getString(COMMENT_VAL));
			}
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}	
		
    /**
     * T14_査定進捗管理の更新（もぎ取り解除,承認時） <br>
     * 
     * @exception SQLException
     */
    public void updT14_1(String strPhase,String strStatus,String sasi_ten_flg,String hoji_user) throws SQLException {

		InputCheck check = new InputCheck();

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(strPhase);
		exCstmt.setStringIn(strStatus);
		exCstmt.setStringIn(hoji_user);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(sasi_ten_flg);
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
				rs.close();
			}	
		}
    }
    
    /**
     * T13_入力履歴の登録 <br>
     * 
     * @param insertKbn 入力区分
     * @param add_flg   追加時：true, 追加時以外：false
     * @exception SQLException
     */
    public void insT13(String nyuryokuKbn,boolean add_flg) throws SQLException {

	    // INパラメータ取得
        String comUser_Nm = user_bean.getComUser_Nm();							// 担当者名日本語
        String comUser_Nm_En = user_bean.getComUser_Nm_En();					// 担当者名英語
        String comSyozokuSoshiki_Nm = user_bean.getComSyozokuSoshiki_Nm();		// 所属部署名日本語
        String comSyozokuSoshiki_Nm_En = user_bean.getComSyozokuSoshiki_Nm_En();// 所属部署名英語
        String comDaiko_user_nm = user_bean.getComDaiko_user_nm();				// 代行者名日本語
        String comDaiko_user_nm_en = user_bean.getComDaiko_user_nm_en();		// 代行者名英語

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		if(add_flg){
			exCstmt.setStringIn(outAnkenNoT14);
		}else{
			exCstmt.setStringIn(anken_no);
		}
        exCstmt.setStringIn(HS_KBN_HANTEI);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(comUser_Nm);
        exCstmt.setStringIn(comUser_Nm_En);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm_En);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(nyuryokuKbn);
        exCstmt.setStringIn(userId);
       	exCstmt.setStringIn(comDaiko_userId);
       	exCstmt.setStringIn(comDaiko_user_nm);
       	exCstmt.setStringIn(comDaiko_user_nm_en);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(form.getSyonin_tanto());
		
		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}	
		}
    }   
 
    /**
     * T14_査定進捗管理の更新（SP_SS_OB2102_UPDATE_T1400） <br>
     * 
	 * @param taisyogai_flg 対象外フラグ
     * @exception SQLException
     */
    public void updT14_2(String taisyogai_flg) throws SQLException {

		InputCheck check = new InputCheck();

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(GS.STATUS_SYONIN_MACHI);
		exCstmt.setStringIn(form.getSyonin_tanto());
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(taisyogai_flg);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(null);
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
			exCstmt.setStringIn(comDaiko_userId);
		}		
		exCstmt.setStringIn(GS.OFF);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}	
		}
    }   

    /**
	 * T04_メール配信の登録 <br>
	 * 
     * @param haishinsaki_tanto  配信先担当者 
     * @param mail_phase         フェーズ
     * @param mail_status        メールステータス
     * @param add_flg            追加時：true, 追加時以外：false
	 * @exception SQLException
	 */
	public void insT04(String haishinsaki_tanto,String mail_phase,String mail_status,boolean add_flg) throws SQLException {

		InputCheck check = new InputCheck();

	    // INパラメータ取得
        String taisyo_ym = tori_bean.getTaisyo_ym();					// 対象年月
        String bunrui2 = tori_bean.getBunrui2();						// 分類２
        String bu_cd = tori_bean.getBu_cd();						// 分類２
        
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
        exCstmt.setStringIn(userId);        
		if(add_flg){
			exCstmt.setStringIn(outAnkenNoT14);
		}else{
			exCstmt.setStringIn(anken_no);			
		}
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(satei_kaisha_cd);
		if(add_flg){
			exCstmt.setStringIn(outBunrui2T14);
			// 部コード(システム区分01の場合のみ設定)
			if(GS.GSS.equals(tori_bean.getSystem_kbn())){
				exCstmt.setStringIn(outBu_cdT14);
			} else {
				exCstmt.setStringIn(GS.EMPTY_CHARCTER);
			}
		}else{
			exCstmt.setStringIn(bunrui2);			
			// 部コード(システム区分01の場合のみ設定)
			if(GS.GSS.equals(tori_bean.getSystem_kbn())){
				exCstmt.setStringIn(bu_cd);
			} else {
				exCstmt.setStringIn(GS.EMPTY_CHARCTER);
			}
		}
        exCstmt.setStringIn(mail_phase);
        exCstmt.setStringIn(mail_status);
        exCstmt.setStringIn(haishinsaki_tanto);
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
				rs.close();
			}
		}
	}

    /**
     * T15_一次二次査定の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT15() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1500, sqlExec);
		exCstmt.setStringIn(anken_no);
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
    public void delT16() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1600, sqlExec);
		exCstmt.setStringIn(anken_no);
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
    public void delT17() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1700, sqlExec);
		exCstmt.setStringIn(anken_no);
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
    public void delT19() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1900, sqlExec);
		exCstmt.setStringIn(anken_no);
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
    public void delT20() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T2000, sqlExec);
		exCstmt.setStringIn(anken_no);
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
	 * T12_コメントの削除 <br>
	 * 
     * @param add_flg   追加時：true, 追加時以外：false
	 * @exception SQLException
	 */
	public void delT12(boolean add_flg) throws SQLException {

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1200, sqlExec);
		if(add_flg){
			exCstmt.setStringIn(outAnkenNoT14);
		}else{
			exCstmt.setStringIn(anken_no);			
		}
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(GS.COMMENT_VAL_97);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T12_コメントの登録 <br>
     * 
	 * @param toroku_kbn 登録区分
     * @param add_flg   追加時：true, 追加時以外：false
     * @exception SQLException
     */
    public void insT12(String toroku_kbn,boolean add_flg) throws SQLException {

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1200, sqlExec);
		if(add_flg){
			exCstmt.setStringIn(outAnkenNoT14);
		}else{
			exCstmt.setStringIn(anken_no);			
		}
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(GS.COMMENT_VAL_97);
        exCstmt.setStringIn(toroku_kbn);
        exCstmt.setStringIn(form.getComment());
        exCstmt.setStringIn(userId);
       	exCstmt.setStringIn(comDaiko_userId);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
    }   
 
    /**
     * T01_対象先の更新 <br>
     * 
     * @exception SQLException
     */
    public void updT01() throws SQLException {

		InputCheck check = new InputCheck();

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_UPDATE_T0100, sqlExec);
		
		if(check.isNullBlank(form.getTaisyogai_kbn())){
			exCstmt.setStringIn(null);						
		}else{
			exCstmt.setStringIn(GOLF_KAIINKEN);			
		}
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);        
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(tori_cd);
       	exCstmt.setStringIn(hanki_sihanki_kbn);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
    }   
 
    /**
     * チャンピオン部重複チェック <br>
     * 
     * @exception SQLException
     */
    public int getCountChampion () throws SQLException {

        // 件数
        int count = 0;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0700, sqlExec);
		
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
		exCstmt.setIntOut(CNT);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
            count = exCstmt.getInt(CNT);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
        return count;
    }   
 
    /**
     * 滞留判定実施中チェック <br>
     * 
     * @exception SQLException
     */
    public int getCountTairyu () throws SQLException {

        // 件数
        int count = 0;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_SELECT_T0800, sqlExec);
		
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(taisyo_ym);
		exCstmt.setIntOut(CNT);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
            count = exCstmt.getInt(CNT);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
        return count;
    }   

    /**
     * 引当金確認実施中チェック <br>
     * 
     * @exception SQLException
     */
    public int getCountHikiate() throws SQLException {

        // 件数
        int count = 0;

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_SELECT_T1400, sqlExec);
		
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(sateiki);
        exCstmt.setStringIn(sateiki);
        exCstmt.setStringIn(GS.PHASE_HIKIATEKIN_KAKUNIN);
		exCstmt.setIntOut(CNT);
		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
            count = exCstmt.getInt(CNT);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
        return count;
    }   

    /**
     * T16_引当金検討対象BS明細の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT16() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T1600, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(sateiki);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(satei_kaisha_cd);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
       	exCstmt.setStringIn(hanki_sihanki_kbn);

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
     * T16_引当金検討対象BS明細の登録(引当金確認差戻時) <br>
     * 
     * @exception SQLException
     */
    public void insT16h() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T1601, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(sateiki);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(satei_kaisha_cd);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}
       	exCstmt.setStringIn(hanki_sihanki_kbn);
       	exCstmt.setStringIn(tori_bean.getAnken_no());

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
     * T17_引当金判定表示用の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT17() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEISTAT00, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(syori_kaisu);
       	exCstmt.setStringIn(hanki_sihanki_kbn);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        
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
     * T14_査定進捗管理の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT14() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T1400, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(syori_kaisu);
       	exCstmt.setStringIn(hanki_sihanki_kbn);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(kijunbi_kbn);
        exCstmt.setStringIn(sateiki);        
        exCstmt.setStringIn(form.getSyonin_tanto());        
        exCstmt.setStringOut(OUT_SATEI_ANKEN_NO);
        exCstmt.setStringOut(OUT_BUNRUI2);
        exCstmt.setStringOut(OUT_BU_CD);
        
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
            //OUTパラメータ取得
            outAnkenNoT14 = exCstmt.getString(OUT_SATEI_ANKEN_NO);
            outBunrui2T14 = exCstmt.getString(OUT_BUNRUI2);
            outBu_cdT14 = exCstmt.getString(OUT_BU_CD);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }


    /**
     * T14_査定進捗管理の更新(仮基準査定追加時) <br>
     * 
     * @exception SQLException
     */
    public void updT14() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_UPDATE_T1401, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(kijunbi_kbn);
        exCstmt.setStringIn(sateiki);        
        exCstmt.setStringIn(form.getSyonin_tanto());        
        exCstmt.setStringIn(tori_bean.getAnken_no());        
        exCstmt.setStringOut(OUT_BUNRUI2);
        exCstmt.setStringOut(OUT_BU_CD);
        
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
            //OUTパラメータ取得
            outAnkenNoT14 = tori_bean.getAnken_no();
            outBunrui2T14 = exCstmt.getString(OUT_BUNRUI2);
            outBu_cdT14 = exCstmt.getString(OUT_BU_CD);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }

    /**
     * T02_検討対象先の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT02() throws SQLException {
        
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_DELETE_T0200, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(form.getTyusyutu_jiyu());
       	exCstmt.setStringIn(hanki_sihanki_kbn);
        exCstmt.setStringIn(tori_cd);
        
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
     * T02_検討対象先の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT02() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T0200, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(form.getTyusyutu_jiyu());
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
       	exCstmt.setStringIn(hanki_sihanki_kbn);
       	exCstmt.setStringIn(sateiki);
       	exCstmt.setStringIn(kijunbi_kbn);
       	exCstmt.setStringIn(outBunrui2T14);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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
     * T01_対象先の存在チェック <br>
     * 
     * @exception SQLException
     */
    public int getCountT01() throws SQLException {
        // 件数
        int count = 0;
        //ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_SELECT_T0100, sqlExec);
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(hanki_sihanki_kbn);
		exCstmt.setIntOut(CNT);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
            count = exCstmt.getInt(CNT);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
        return count;
    }

    /**
     * T01_対象先の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT01() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T0100, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(outAnkenNoT14);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
       	exCstmt.setStringIn(hanki_sihanki_kbn);
       	exCstmt.setStringIn(sateiki);
       	exCstmt.setStringIn(kijunbi_kbn);
       	exCstmt.setStringIn(outBunrui2T14);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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
     * T01_対象先の更新(追加処理時) <br>
     * 
     * @exception SQLException
     */
    public void updT01_add() throws SQLException {

		InputCheck check = new InputCheck();

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_UPDATE_T0101, sqlExec);		
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);        
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(tori_cd);
       	exCstmt.setStringIn(hanki_sihanki_kbn);
       	exCstmt.setStringIn(outAnkenNoT14);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
    }   
 
    /**
     * T15_一次二次査定の登録 <br>
     * 
     * @exception SQLException
     */
    public void insT15() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_T1500, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(syori_kaisu);
       	exCstmt.setStringIn(hanki_sihanki_kbn);
        exCstmt.setStringIn(kijunbi_kbn);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        
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
     * 退避情報の削除 <br>
     * 
     * @exception SQLException
     */
    public void delTaihi() throws SQLException {
        
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_DELETE_TAIHI, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(syori_kaisu);
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(tori_cd);
        
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
     * E01_統合対比退避の登録 <br>
     * 
     * @exception SQLException
     */
    public void insE01() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_E0100, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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
     * E02_統合マスタ退避の登録 <br>
     * 
     * @exception SQLException
     */
    public void insE02() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_E0200, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(tori_nm);
        
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
     * E03_格付退避の登録 <br>
     * 
     * @exception SQLException
     */
    public void insE03() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_E0300, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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
     * E04_D&B企業情報の登録 <br>
     * 
     * @exception SQLException
     */
    public void insE04() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_E0400, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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
     * E05_財務退避の登録 <br>
     * 
     * @exception SQLException
     */
    public void insE05() throws SQLException {
        
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_INSERT_E0500, sqlExec);
        exCstmt.setStringIn(syori_kaisu);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
			exCstmt.setStringIn(userId);
		}else{
            // 代行時
			exCstmt.setStringIn(comDaiko_userId);
		}		
        exCstmt.setStringIn(togo_tori_cd);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_cd);
        
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