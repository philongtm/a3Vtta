/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package common.util;

import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* 設定テーブルアクセスクラス
*/
public class SetteiDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;
	private static final String SP_SS_SETTEI = "SP_SS_SETTEI";
	private static final String LOGIN_FLG = "login_flg";
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SetteiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);
		this.appContext = appContext;
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	}

	/**
	 * 設定テーブル情報取得SQL実行処理 <br>
	 * 
	 * 設定テーブル（SSP_SETTEI）から情報を取得する。
	 * @exception SQLException
	 */
	public boolean execute() throws SQLException{
		
		ResultSet rs = null;
		boolean result = true;
		
		//設定テーブル（SSP_SETTEI）から情報取得
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_SETTEI, sqlExec);
		exCstmt.setStringOut(LOGIN_FLG);
		
		try{			
			exCstmt.execute();
			isError(exCstmt);
			if(GS.ON.equals(exCstmt.getString(LOGIN_FLG))){
				result = false;
			}
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
	    	}
	    }
	    return result;
	}
}