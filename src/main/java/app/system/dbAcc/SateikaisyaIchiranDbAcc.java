/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.SateiKaisyaBean;
import app.SessionData;
import app.UserBean;
import app.system.form.SateikaisyaIchiranForm;
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
* OS7102_査定会社メンテナンス_一覧 DBアクセスクラス <br>
*/
public class SateikaisyaIchiranDbAcc extends CommonDbAcc {
	
	private AppContext appContext 			= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 			= null;				// 機能共通セッション
    private UserBean user_bean 			= null;				// ユーザ情報
    private SateikaisyaIchiranForm form 	= null;				// アクションフォーム
	
	private static final String KEY_SYSTEM_KBN 			= "system_kbn"; 		// 区分キー
	private static final String KEY_HANYO1 				= "hanyo1";				// 区分キー
	private static final String KEY_SHOW 					= "show";				// 区分キー

    //Resultset用文字列    
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val"; 	// 区分表示値
	private static final String KBN_VAL					= "kbn_val";			// 区分値
	private static final String KIKAN_KAISHA_CD 			= "kikan_kaisha_cd";	// 基幹会社コード
	private static final String TIMEZONE_NM 				= "timezone_nm";		// 標準時刻(名称)
	private static final String TYUSHUTU_NM 				= "tyushutu_nm";		// 抽出対象(名称)
	private static final String TYUSHUTU_FLG 				= "tyushutu_flg";		// 抽出対象フラグ
	private static final String TIMEZONE_CD 				= "timezone_cd";		// 標準時刻コード
	private static final String SATEI_KAISHA_CD			= "satei_kaisha_cd";	// 査定会社コード
	private static final String SYSTEM_KBN_NM 			= "system_kbn_nm";		// システム区分名称
	private static final String SYSTEM_KBN 				= "system_kbn";			// システム区分
	private static final String SATEI_KAISYA_NM_E 		= "satei_kaisya_nm_e";	// 基幹会社名(英語)
	private static final String SATEI_KAISYA_NM 			= "satei_kaisya_nm";	// 基幹会社名(日本語)
	private static final String KAISHA_CD 				= "kaisha_cd";			// 会社コード
    
    private static final String SP_SS_O_SELECT_P0201					= "SP_SS_O_SELECT_P0201";				//区分の取得プロシージャ
    private static final String SP_SS_OL_SELECT_P0200					= "SP_SS_OL_SELECT_P0200";				//区分の取得プロシージャ
    private static final String SP_SS_OS7102_SELECT_M0400				= "SP_SS_OS7102_SELECT_M0400";			//査定会社一覧情報の取得プロシージャ
    
    // INパラメータ
    private String langMode;    // 共)言語モード)

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public SateikaisyaIchiranDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (SateikaisyaIchiranForm)appContext.getActionForm();

        //ビーンの値を変数に設定
        langMode = cmnData.getComLangMode();
    }
    
    /**
     * 変数初期化 <br>
     */
    public void initialize() {
        // INパラメータ
    }

    /**
     * SP_SS_OL_SELECT_P0200を利用して、区分リストを取得する。 <br>
     * 
     * @param key 区分キー
     * @param langMode 言語モード
     * @param systemKbn システム区分
     * @return 区分リスト
     * @throws SQLException
     */
    private LinkedHashMap<String, String> getP0200KbnList(String key, String langMode,
			String systemKbn) throws SQLException {
    	// 区分リスト
    	LinkedHashMap<String, String> ar_kbn = new LinkedHashMap<String, String>();
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);

		cstmt.setStringIn(key);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(systemKbn);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
	    	while ( rs.next() ) {
				//初期設定
	    		ar_kbn.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));	    			
	    	}
		} finally {
			if (rs != null) {
				try {
					// Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return ar_kbn;
	}

    /**
     * SP_SS_O_SELECT_P0201を利用して、区分リストを取得する。 <br>
     * 
     * @param key 区分キー
     * @param langMode 言語モード
     * @param systemKbn システム区分
     * @return 区分リスト
     * @throws SQLException
     */
    private LinkedHashMap<String, String> getP0201KbnList(String key, String langMode,
			String systemKbn) throws SQLException {
    	// 区分リスト
    	LinkedHashMap<String, String> ar_kbn = new LinkedHashMap<String, String>();
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_O_SELECT_P0201, sqlExec);

		cstmt.setStringIn(key);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(systemKbn);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
	    	while ( rs.next() ) {
				//初期設定
	    		ar_kbn.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));	    			
	    	}
		} finally {
			if (rs != null) {
				try {
					// Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		return ar_kbn;
	}

    /**
     * システムセレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSystemKbnList() throws SQLException {
		// システム【配列】
    	LinkedHashMap<String, String> ar_systemKbn = this.getP0201KbnList(KEY_SYSTEM_KBN, langMode, GS.GSS);
		// システム【配列】をセットする。
    	form.setAr_systemKbn(ar_systemKbn);
    	// システム
    	if (ar_systemKbn.size() > 0) {
    		form.setSystemKbn(GS.MTS);
    	}
	}

    /**
     * 汎用１セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getHanyo1List() throws SQLException {
		// システム【配列】
    	LinkedHashMap<String, String> ar_hanyo1 = this.getP0200KbnList(KEY_HANYO1, langMode, form.getSystemKbn());
		// システム【配列】をセットする。
    	form.setAr_hanyo1(ar_hanyo1);
	}

    /**
     * 表示件数セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getShow() throws SQLException {
		// システム【配列】
    	LinkedHashMap<String, String> ar_show = this.getP0200KbnList(KEY_SHOW, langMode, user_bean.getComWorkflowSystemkbn());
		// システム【配列】をセットする。
    	form.setAr_show(ar_show);
	}

    /**
     * 査定会社一覧情報を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getMeisaiList() throws SQLException {
    	InputCheck check = new InputCheck();
    	// 一覧明細
    	List<SateiKaisyaBean> ar_meisai = new ArrayList<SateiKaisyaBean>();
    	// 機)システム区分(検索用)
    	String systemKbn = form.getSrhSystemKbn();
    	// システム区分(検索用)がヌルの場合
    	if (check.isNullBlank(systemKbn)) {
    		// 明細を表示しない
    		return;
    	}
    	// 機)汎用1(検索用)
    	String hanyo1 = form.getSrhHanyo1();
    	// 機)汎用2(検索用)
    	String hanyo2 = form.getSrhHanyo2();
    	// 機)汎用２名称(検索用)
    	String hanyo2Nm = form.getSrhHanyo2Name();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7102_SELECT_M0400, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(Function.addSingleQuotation(hanyo2));
		cstmt.setStringIn(Function.addSingleQuotation(hanyo2Nm));
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			int i = 0;
	    	while ( rs.next() ) {
	    		// 抽出条件情報
	    		SateiKaisyaBean sateiBean = new SateiKaisyaBean();
	    		
				// id
	    		sateiBean.setId(Function.getStringOfInt(i));
				// 会社コード
	    		sateiBean.setKaisya_cd(rs.getString(KAISHA_CD));
				// 分類２
	    		sateiBean.setBunrui2(rs.getString(KIKAN_KAISHA_CD));
				// 分類２名称(日本語)
	    		sateiBean.setBunrui2_nm_ja(rs.getString(SATEI_KAISYA_NM));
				// 分類２名称(英語)
	    		sateiBean.setBunrui2_nm_en(rs.getString(SATEI_KAISYA_NM_E));
				// システム区分
	    		sateiBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				// システム区分名称
	    		sateiBean.setSystem_kbn_nm(rs.getString(SYSTEM_KBN_NM));
				// 分類１
	    		sateiBean.setBunrui1(rs.getString(SATEI_KAISHA_CD));
				// 標準時刻コード
	    		if (!check.isNullBlank(rs.getString(TIMEZONE_CD))) {
		    		sateiBean.setStandard_time(rs.getString(TIMEZONE_CD).trim());
	    		}
				// 抽出対象フラグ
	    		sateiBean.setTyusyutu_taisyo_flg(rs.getString(TYUSHUTU_FLG));
				// 抽出対象(名称)
	    		sateiBean.setTyusyutu_taisyo_nm(rs.getString(TYUSHUTU_NM));
				// 標準時刻(名称)
	    		sateiBean.setStandard_time_nm(rs.getString(TIMEZONE_NM));
	    		
	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, sateiBean);
	    		i++;   
	    	}
	    	
		    // ActionForm に明細を格納
		    form.setAr_meisai(ar_meisai);    
		    // ページ設定
		    form.setPager(ar_meisai);
		    
		} finally {
			if (rs != null) {
				try {
					// Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
}