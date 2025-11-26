/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.system.form.SateikaisyaTorokuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
* OS7103_査定会社メンテナンス_登録 DBアクセスクラス <br>
*/
public class SateikaisyaTorokuDbAcc extends CommonDbAcc {
	
	private AppContext appContext 	= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 	= null;				// 機能共通セッション
    private UserBean user_bean 	= null;				// ユーザ情報
    private SateikaisyaTorokuForm form 		= null;				// アクションフォーム

	private static final String KEY_SYSTEM_KBN 			= "system_kbn"; 		// 区分キー
	private static final String KEY_HANYO1 				= "hanyo1";				// 区分キー
	private static final String KEY_TIMEZONE_CD 			= "timezone_cd";		// 区分キー

    //Resultset用文字列    
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val"; 	// 区分表示値
	private static final String KBN_VAL					= "kbn_val";			// 区分値

	private static final String CNT 						= "cnt"; 				// 件数

    private static final String SP_SS_O_SELECT_P0201					= "SP_SS_O_SELECT_P0201";				//システムセレクトボックスの設定値の取得チェックプロシージャ
    private static final String SP_SS_OL_SELECT_P0200					= "SP_SS_OL_SELECT_P0200";				//区分の取得プロシージャ
    private static final String SP_SS_OS7103_SELECT_M0400				= "SP_SS_OS7103_SELECT_M0400";			//存在チェックプロシージャ
    private static final String SP_SS_OS7103_INSERT_M0400				= "SP_SS_OS7103_INSERT_M0400";			//M04_査定会社設定マスタ(SSM_SATEIKAISYA)の登録プロシージャ
    private static final String SP_SS_OS7103_UPDATE_M0400				= "SP_SS_OS7103_UPDATE_M0400";			//M04_査定会社設定マスタ(SSM_SATEIKAISYA)の更新プロシージャ
    private static final String SP_SS_OS7103_SELECT_T0100				= "SP_SS_OS7103_SELECT_T0100";			//チェックプロシージャ
    private static final String SP_SS_OS7103_DELETE_M0400				= "SP_SS_OS7103_DELETE_M0400";			//M04_査定会社設定マスタの削除プロシージャ
    
    // INパラメータ
    private String langMode;    // 共)言語モード)

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public SateikaisyaTorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (SateikaisyaTorokuForm)appContext.getActionForm();

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
		// 汎用１【配列】
    	LinkedHashMap<String, String> ar_hanyo1 = this.getP0200KbnList(KEY_HANYO1, langMode, form.getSystemKbn());
		// 汎用１【配列】をセットする。
    	form.setAr_hanyo1(ar_hanyo1);
	}

    /**
     * 標準時刻セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getHyojunJikokuList() throws SQLException {
		// 標準時刻【配列】
    	LinkedHashMap<String, String> ar_hyojunJikoku = this.getP0200KbnList(KEY_TIMEZONE_CD, langMode, form.getSystemKbn());
		// 標準時刻【配列】をセットする。
    	form.setAr_hyojunJikoku(ar_hyojunJikoku);
	}

    /**
     * 同一システム内で 汎用2コードが既に、登録されていないかチェックを行う <br>
     * 
     * @return データある：true
     * @throws SQLException
     */
    public boolean checkHanyo2() throws SQLException {
    	
    	// 機)システム区分
    	String systemKbn = form.getSystemKbn();
    	// 機)汎用2
    	String hanyo2 = form.getHanyo2();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7103_SELECT_M0400, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo2);
		// result
		cstmt.setIntOut(CNT);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
    		if (cstmt.getInt(CNT) > 0) {
    			return true;    			
    		} else {
    			return false;
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
	}

    /**
     * M04_査定会社設定マスタ(SSM_SATEIKAISYA)の登録を行う <br>
     * 
     * @throws SQLException
     */
    public void insertM04() throws SQLException {

		// 代行ユーザID
    	String daikoUserId = user_bean.getComDaiko_userId();
		// システム区分
    	String systemKbn = form.getSystemKbn();
		// 汎用１
    	String hanyo1 = form.getHanyo1();
		// 汎用２
    	String hanyo2 = form.getHanyo2();
		// 汎用２名称(日本語)
    	String hanyo2NmJp = form.getHanyo2Jp();
		// 汎用２名称(英語)
    	String hanyo2NmEn = form.getHanyo2En();
		// ユーザID
    	String userId = user_bean.getComUserId();
    	// 抽出対象フラグ
    	String flgCyusyutuTaisyo = form.getTyusyutu_taisyo_flg();
    	// 標準時刻コード
    	String hyojunJikokuCd = form.getHyojunJikokuCd();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7103_INSERT_M0400, sqlExec);
		// パラメーター
		cstmt.setStringIn(daikoUserId);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(hanyo2NmJp);
		cstmt.setStringIn(hanyo2NmEn);
		cstmt.setStringIn(userId);
		cstmt.setStringIn(flgCyusyutuTaisyo);
		cstmt.setStringIn(hyojunJikokuCd);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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

    /**
     * M04_査定会社設定マスタ(SSM_SATEIKAISYA)の更新を行う <br>
     * 
     * @throws SQLException
     */
    public void updateM04() throws SQLException {

		// 代行ユーザID
    	String daikoUserId = user_bean.getComDaiko_userId();
		// システム区分
    	String systemKbn = form.getSystemKbn();
		// 汎用１
    	String hanyo1 = form.getHanyo1();
		// 汎用２
    	String hanyo2 = form.getHanyo2();
		// 汎用２名称(日本語)
    	String hanyo2NmJp = form.getHanyo2Jp();
		// 汎用２名称(英語)
    	String hanyo2NmEn = form.getHanyo2En();
		// ユーザID
    	String userId = user_bean.getComUserId();
    	// 抽出対象フラグ
    	String flgCyusyutuTaisyo = form.getTyusyutu_taisyo_flg();
    	// 標準時刻コード
    	String hyojunJikokuCd = form.getHyojunJikokuCd();
    	// 会社コード
    	String kaisyaCd = form.getKaisyaCd();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7103_UPDATE_M0400, sqlExec);
		// パラメーター
		cstmt.setStringIn(daikoUserId);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(hanyo2NmJp);
		cstmt.setStringIn(hanyo2NmEn);
		cstmt.setStringIn(userId);
		cstmt.setStringIn(flgCyusyutuTaisyo);
		cstmt.setStringIn(hyojunJikokuCd);
		cstmt.setStringIn(kaisyaCd);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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

    /**
     * 削除対象の査定会社がT01_対象先に登録されていないかチェックを行う <br>
     * 
     * @return データある：true
     * @throws SQLException
     */
    public boolean checkSateikaisya() throws SQLException {
    	
    	// 機)システム区分
    	String systemKbn = form.getSystemKbn();
    	// 機)汎用2
    	String hanyo2 = form.getHanyo2();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7103_SELECT_T0100, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo2);
		// result
		cstmt.setIntOut(CNT);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
    		if (cstmt.getInt(CNT) > 0) {
    			return true;    			
    		} else {
    			return false;
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
	}

    /**
     * M04_査定会社設定マスタ(SSM_SATEIKAISYA)の削除を行う <br>
     * 
     * @throws SQLException
     */
    public void deleteM04() throws SQLException {

		// システム区分
    	String systemKbn = form.getSystemKbn();
		// 汎用１
    	String hanyo1 = form.getHanyo1();
		// 汎用２
    	String hanyo2 = form.getHanyo2();
    	// 会社コード
    	String kaisyaCd = form.getKaisyaCd();
    	
		// ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7103_DELETE_M0400, sqlExec);
		// パラメーター
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(kaisyaCd);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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