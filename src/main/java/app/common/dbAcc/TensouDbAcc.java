/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/22		SSC				課題No.222 照会関係者検索対応
003		2015/03/18		SSC				課題No.BJ201408049 IA化対応時の機能改善 
004		2016/03/25		SSC				課題No.BJ201602002 部門廃止対応（一次）
005		2016/04/13		SSC				課題No.BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.common.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.common.form.TensouForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* OZ3101_転送先選択 DBアクセスクラス
*/
public class TensouDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SessionData cmnData = null;		// 共通セッションデータ
	private UserBean user_bean = null;			// ユーザ情報
	private TorihikisakiBean tori_bean = null;	// 取引先情報
	private TensouForm form = null;			// アクションフォーム

	//Resultset用文字列	
	private static final String BUMON_CD   = "bumon_cd";
	private static final String BUMON_NM   = "bumon_nm";
	private static final String EMAIL_ADDR = "email_addr";
	private static final String TOGO_ID    = "togo_id";
	private static final String BU_CD      = "bu_cd";
	private static final String BU_NM      = "bu_nm";

	private static final String NYURYOKU_KBN    = "50";	//入力区分:転送
	private static final String TENSOU          = "2";	//差戻・転送フラグ
	private static final String TOROKU_POINT    = "3";	//登録箇所：差戻
	private static final String HAISINZUMI_FLG  = "N";	//配信済フラグ'N'：未配信
	
	private static final String SP_SS_O_SELECT_M1200   = "SP_SS_O_SELECT_M1200";		//汎用２（右）セレクトボックスの設定値（国内）取得用プロシージャ
	private static final String SP_SS_O_SELECT_TANTO   = "SP_SS_O_SELECT_TANTO";		//担当者の一覧取得用プロシージャ
	private static final String SP_SS_O_SELECT_SYOZOKU = "SP_SS_O_SELECT_SYOZOKU";	//担当者の所属組織取得用プロシージャ
	private static final String SP_SS_OZ_UPDATE_T0800  = "SP_SS_OZ_UPDATE_T0800";		//T08_滞留判定進捗管理更新用プロシージャ
	private static final String SP_SS_OZ_UPDATE_T1400  = "SP_SS_OZ_UPDATE_T1400";		//T14_査定進捗管理更新用プロシージャ
	private static final String SP_SS_O_INSERT_T1300   = "SP_SS_O_INSERT_T1300";		//T13_入力履歴登録用プロシージャ
	private static final String SP_SS_O_DELETE_T1200   = "SP_SS_O_DELETE_T1200";		//T12_コメント削除用プロシージャ
	private static final String SP_SS_O_INSERT_T1200   = "SP_SS_O_INSERT_T1200";		//T12_コメント登録用プロシージャ
	private static final String SP_SS_O_INSERT_T0400   = "SP_SS_O_INSERT_T0400";		//T04_メール配信登録用プロシージャ
	private static final String SP_SS_OS_SELECT_M1200_2  = "SP_SS_OS_SELECT_M1200_2";	//汎用３セレクトボックスの設定値（国内）取得用プロシージャ
	private static final String SP_SS_O_SELECT_TANTO_HONBU = "SP_SS_O_SELECT_TANTO_HONBU";	//担当者の一覧取得用プロシージャ（部選択時）
	
	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String satei_kaisha_cd;	// 査定会社コード
	private String systemkbn;			// システム区分
	private String anken_no;			// 案件No
	private String phase;				// フェーズ
	private String taisyo_ym;			// 対象年月
	private String comDaiko_userId;	// 代行ユーザID
	private String bumon_cd;			// 	部門コード
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 * @param appLog
	 */
	public TensouDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		tori_bean = cmnData.getTori_bean();
		form = (TensouForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
		systemkbn = tori_bean.getSystem_kbn();
		satei_kaisha_cd = tori_bean.getSateikaisya_cd();
        anken_no = tori_bean.getAnken_no();
        phase = tori_bean.getPhase();
		taisyo_ym = tori_bean.getTaisyo_ym();
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
	}
		
	/**
	 * 汎用１セレクトボックス設定値取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo1() throws SQLException {
		ResultSet rs = null;
		String hanyo1 = Function.trim(tori_bean.getBunrui2());
		LinkedHashMap<String,String> ar_hanyo1 = new LinkedHashMap<String,String>();

		//初期設定
		form.setHanyou1(hanyo1);

		//海外の場合、差戻対象案件の分類2のみ格納
		if(!GS.GSS.equals(systemkbn)){
			ar_hanyo1.put(hanyo1,hanyo1);
			form.setAr_Hanyou1(ar_hanyo1);
			return;
		}

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_M1200, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				//差戻フェーズが二次査定の場合、差戻対象案件の分類2のみ格納
				if(GS.PHASE_NIJI_SATEI.equals(phase)){
					if(!hanyo1.equals(rs.getString(BUMON_CD))){
						continue;
					}
				}
				ar_hanyo1.put(rs.getString(BUMON_NM),rs.getString(BUMON_CD));
			}
			form.setAr_Hanyou1(ar_hanyo1);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 汎用２セレクトボックス設定値取得 <br>
	 *
	 * @exception SQLException
	 */
	public void getHanyo2() throws SQLException {
		ResultSet rs = null;
		String hanyo2 = Function.trim(tori_bean.getBunrui3());
		String buCd = Function.trim(tori_bean.getBu_cd());
		String firstBuCd = "";
		LinkedHashMap<String,String> ar_hanyo2 = new LinkedHashMap<String,String>();

		//初期設定
		form.setHanyou2(hanyo2);

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M1200_2, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				//汎用２（右）セレクトボックスで選択された分類２に紐付く分類３のみ格納
				ar_hanyo2.put(rs.getString(BU_NM),rs.getString(BU_CD));
				if (firstBuCd.length() == 0) {
					//セレクトボックス一番目の部コード
					firstBuCd = Function.trim(rs.getString(BU_CD));
				}
			}
			// 初期値設定（SJ)
			if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
			   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
				if (ar_hanyo2.containsValue(buCd)) {
					form.setHanyou2(buCd);
				} else {
					form.setHanyou2(firstBuCd);
				}
			}
			form.setAr_Hanyou2(ar_hanyo2);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
    /**
     * 担当者一覧を表示 <br>
     * 
     * @exception SQLException
     */
    public void getTantoIchiran() throws SQLException {
		List<Map> ar_tanto = new ArrayList<Map>();  // 担当者一覧【配列】
		// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_TANTO, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(phase);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAILＡｄｄｒｅｓｓ
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
			}
			form.setAr_Tanto(ar_tanto);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 担当者一覧（本部単位）を表示 <br>
     * 
     * @exception SQLException
     */
    public void getTantoIchiran2() throws SQLException {
		List<Map> ar_tanto = new ArrayList<Map>();  // 担当者一覧【配列】
		// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_TANTO_HONBU, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAILＡｄｄｒｅｓｓ
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
			}
			form.setAr_Tanto(ar_tanto);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 担当者の所属組織取得 <br>
     * 
     * @exception SQLException
     */
    public void getTanto_Syozoku() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_SYOZOKU, sqlExec);
		exCstmt.setStringIn(form.getSelectedTantoId());
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setTanto_bumon_cd(rs.getString(BUMON_CD));
				form.setTanto_bu_cd(rs.getString(BU_CD));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T08_滞留判定進捗管理の更新 <br>
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @exception SQLException
     */
    public void updT08(String upd_status,String upd_hoji_user,String upd_bu_cd,String upd_user) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ_UPDATE_T0800, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(upd_status);
		exCstmt.setStringIn(upd_hoji_user);
		exCstmt.setStringIn(TENSOU);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(upd_bu_cd);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(GS.OFF);
		exCstmt.setStringIn(comDaiko_userId);
        exCstmt.setStringIn(upd_user);        
		if(form.isBu_cd_upd_flg()){
			exCstmt.setStringIn(GS.TRUE_CHARCTER);
		}else{
			exCstmt.setStringIn(GS.FALSE_CHARCTER);
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
     * T14_査定進捗管理の更新 <br>
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @exception SQLException
     */
    public void updT14(String upd_status,String upd_hoji_user,String upd_bu_cd,String upd_user) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(upd_status);
		exCstmt.setStringIn(upd_hoji_user);
		exCstmt.setStringIn(TENSOU);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(upd_bu_cd);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(GS.OFF);
		exCstmt.setStringIn(comDaiko_userId);
        exCstmt.setStringIn(upd_user);        
		if(form.isBu_cd_upd_flg()){
			exCstmt.setStringIn(GS.TRUE_CHARCTER);
		}else{
			exCstmt.setStringIn(GS.FALSE_CHARCTER);
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
     * T13_入力履歴の登録 <br>
     * 
     * @param String
     * @exception SQLException
     */
    public void insT13(String hanteiKbn) throws SQLException {
	    // INパラメータ取得
        String comUser_Nm = user_bean.getComUser_Nm();							// 担当者名日本語
        String comUser_Nm_En = user_bean.getComUser_Nm_En();					// 担当者名英語
        String comSyozokuSoshiki_Nm = user_bean.getComSyozokuSoshiki_Nm();		// 所属部署名日本語
        String comSyozokuSoshiki_Nm_En = user_bean.getComSyozokuSoshiki_Nm_En();// 所属部署名英語
        String comDaiko_user_nm = user_bean.getComDaiko_user_nm();				// 代行者名日本語
        String comDaiko_user_nm_en = user_bean.getComDaiko_user_nm_en();		// 代行者名英語
        String prevBunrui2 = tori_bean.getBunrui2();							// 転送元分類２
        String prevBu_cd = tori_bean.getBu_cd();								// 転送元部
        //課題No.222
        //追加開始
        String iraisakiUserId = Function.trim(form.getSelectedTantoId());		// 案件依頼先ユーザID
        //追加完了

		//ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(hanteiKbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(comUser_Nm);
        exCstmt.setStringIn(comUser_Nm_En);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm_En);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(NYURYOKU_KBN);
        exCstmt.setStringIn(userId);
       	exCstmt.setStringIn(comDaiko_userId);
       	exCstmt.setStringIn(comDaiko_user_nm);
       	exCstmt.setStringIn(comDaiko_user_nm_en);
        exCstmt.setStringIn(GS.COMMENT_VAL_98);
        exCstmt.setStringIn(form.getComment());
        exCstmt.setStringIn(prevBunrui2);
        exCstmt.setStringIn(prevBu_cd);
        //課題No.222
        //修正開始
        //exCstmt.setStringIn(null);
        exCstmt.setStringIn(iraisakiUserId);
        //修正完了
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
     * T12_コメントの削除 <br>
     * 
     * @exception SQLException
     */
    public void delT12() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1200, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(GS.COMMENT_VAL_98);
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
     * T12_コメントの登録 <br>
     * 
     * @exception SQLException
     */
    public void insT12() throws SQLException {
		//ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1200, sqlExec);
		exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(GS.COMMENT_VAL_98);
        exCstmt.setStringIn(TOROKU_POINT);
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
	 * T04_メール配信の登録 <br>
	 * 
     * @param  String
     * @param  String
	 * @exception SQLException
	 */
	public void insT04(String ins_tanto,String upd_user) throws SQLException {
		//ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(form.getHanyou1());
        // 部を指定して転送する場合    ：部コード設定あり、担当者設定なし
        // 担当者を指定して転送する場合：部コード設定なし、担当者設定あり
        exCstmt.setStringIn(form.getHanyou2());
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(ins_tanto);
        exCstmt.setStringIn(HAISINZUMI_FLG);
        exCstmt.setStringIn(form.toString());
        exCstmt.setStringIn(upd_user);        
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
}