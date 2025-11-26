/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/09		SSC				1.5次版に修正を施し流用
002		2008/05/14		SSC				1.5次版組込対応
******************************************************************************/
package common.db;

import common.global.GS;
import common.util.Log;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * ＤＢ接続を行う共通クラス
 * 
 */
public class SqlExecuter {
	private PooledConnection pooledCon = null;
	private Connection connection = null;			//コネクション
	private CallableStatement cstmt;
	private PreparedStatement pstmt;
	private Statement statement = null;
	private Log log; 								//ログクラス
		
	//ログメッセージ
	private static final String ERR_QUERY = 		"■SQL文（検索）の実行に失敗しました。";
	private static final String TRAN_START =		"■トランザクションを開始します。";
	private static final String ERR_TRAN_START = 	"■トランザクションの開始に失敗しました。";	
	private static final String COMMIT = 			"■コミットします。";
	private static final String ERR_COMMIT = 		"■コミットに失敗しました。";	
	private static final String ROLLBACK = 		"■ロールバックします。";
	private static final String ERR_ROLLBACK = 	"■ロールバックに失敗しました。";	
	private static final String ERR_TRAN = 		"■トランザクション（SQL文：登録・更新・削除）の実行に失敗しました。";

	/**
	 * コンストラクタ
	 */
	public SqlExecuter(Log log) {
		this.log = log;
		pooledCon = PooledConnection.getInstance(log);
		connection = pooledCon.getConnection();
	}
	
	/**
	 * Connectionの開放
	 *  
	 */
	public void disConnect() {
		try {
			if (connection != null) {
				pooledCon.releaseConnection(connection);
			}
		} finally {
			pooledCon = null;
			connection = null;
		}
	}

	public void destroy() {
		disConnect();
	}
	
	/**
	 *  SQL文(検索系)を実行し、ResultSetを返す。
	 * @param PreparedStatement
	 * @return ResultSet
	 * @exception SQLException
	 */
	public ResultSet execQuery(PreparedStatement pstmt) throws SQLException {	    
		try{
			ResultSet rs = pstmt.executeQuery();
			return new ExResultSet(rs,pstmt);
		}catch(SQLException e) {
			errLog(ERR_QUERY);
			errLog(e.getMessage());
			throw e;
		}
	}
	
	/**
	 * トランザクション(SQL文：登録・更新系)を実行し、処理結果フラグを返す。
	 * （単一SQL実行）
	 * @param PreparedStatement
	 * @exception SQLException
	 */
	public boolean execUpdate(PreparedStatement pstmt) throws SQLException {	    
		try{
			pstmt.execute();
		} catch (SQLException e) {
			errLog(ERR_TRAN);
			errLog(e.getMessage());
			rollback();
			throw e;
		}
		commit();
		return true;
	}

	/**
	 * トランザクション(SQL文：登録・更新系)を実行し、処理結果フラグを返す。
	 * （SQL複数回実行）
	 * @param PreparedStatement
	 * @exception SQLException
	 */
	public boolean execBatch(PreparedStatement pstmt) throws SQLException {	    
		try {
			beginTran();
			pstmt.executeBatch();
		} catch (SQLException e) {
			errLog(ERR_TRAN);
			errLog(e.getMessage());
			rollback();
			throw e;
		}
		commit();
		return true;
	}

	/**
	 * トランザクションの開始宣言を行う。
	 *
	 *  
	 * @exception SQLException
	 */
	public void beginTran() 
			throws SQLException {
		connect();
		log("beginTran()");
		try {
			//トランザクション開始
			if (connection != null) {
				connection.setAutoCommit(false);
				statement = connection.createStatement();
			}
		} catch (SQLException e) {
			errLog("■トランザクションの開始に失敗しました。");
			throw e;
		}
	}

	/**
	 * SQLの実行後のcommit処理を行う。
	 * 
	 * @exception SQLException
	 */
	public void commit() throws SQLException {
		connect();
		log(COMMIT);
		try {
			connection.commit();
		} catch (Exception e) {
			errLog(ERR_COMMIT);
			throw new SQLException();
		}
	}

	/**
	 * SQLの実行後のrollback処理を行う。
	 * 
	 * @exception SQLException
	 */
	public void rollback() throws SQLException {
		connect();
		log(ROLLBACK);
		try {
			connection.rollback();
		} catch (SQLException e) {
			errLog(ERR_ROLLBACK);
			throw e;
		}
	}

	/**
	 * ログ出力を行う。
	 * 
	 * @param String
	 *            メッセージ
	 * @exception SQLException
	 */
	private void log(String message) {
		log.write(GS.LOG_INF, getClass().getName(), message);
	}
	
	/**
	 * エラーログ出力を行う。
	 * 
	 * @param String
	 *            メッセージ
	 * @exception SQLException
	 */
	private void errLog(String message) {
		log.write(GS.LOG_ERR, getClass().getName(), message);
	}
	
	/**
	 * エラー有無を取得する
	 * @return true/エラー有 false/エラー無
	 */
	public boolean isError() {
		boolean flg = false;
		if(pooledCon!=null){
			flg = true;
		}
		if(pooledCon.getException()!=null){
			return true;			
		}
		return flg;
	}
	
	/**
	 * エラー発生時の例外を取得する
	 * @return SQLException
	 */
	public SQLException getConnectException() {
		return pooledCon.getException();
	}

	/**
	 * CallableStatementを返す。
	 * @param boolean
	 * 		   false：戻り値(結果セットではない)を返さないタイプ
	 * 		   true：戻り値を返すタイプ
	 * @param String
	 *         ストアドプロシージャ名
	 * 		　 例)sp_ss(?,?)
	 * @return CallableStatement
	 */
	public CallableStatement getCallableStatement(String sp) throws SQLException{
		cstmt = connection.prepareCall(sp);
		return cstmt;
	}

	/**
	 * PreparedStatementを返す。
	 * @param String
	 *         SQL文字列
	 * @return PreparedStatement
	 */
	public PreparedStatement getPreparedStatement(String sql) throws SQLException{
		pstmt = connection.prepareStatement(sql);
		return pstmt;
	}
	
	//↓以下1.5専用メソッド
	/**
	 * SQL文(検索系)を実行し、ResultSetを返す。
	 * 
	 * @param strSql
	 *            実行可能SQL文
	 * @return ResultSet
	 * @exception SQLException
	 */
	public ResultSet execQuery(String strSql) 
				throws SQLException {
		connect();
		try {	
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = statement.executeQuery(strSql);
			return new ExResultSet(rs,statement);
		} catch (SQLException e) {
			errLog("■SQL文（検索）の実行に失敗しました。");
			log.write(GS.LOG_ERR,"",strSql);
			throw e;
		}
	}
	private void connect() throws SQLException {
		if(pooledCon==null) {
			pooledCon = PooledConnection.getInstance(log);
			connection = pooledCon.getConnection();
			if( connection == null ) {
				throw getConnectException();
			}
		}
	}
	/**
	 *  SQL文(検索系)を実行し、ResultSetを返す。
	 * @param pstmt
	 *            PreparedStatement
	 * @return ResultSet
	 * @exception SQLException
	 */
	public ResultSet execQueryP(PreparedStatement pstmt) throws SQLException {	    
		try {

			ResultSet rs = pstmt.executeQuery();
			
			return new ExResultSet(rs,pstmt);

		} catch (SQLException e) {
			errLog("■SQL文（検索）の実行に失敗しました。");
			errLog(e.getMessage());

			throw e;
		}
	}

    /**
	 * システム日付を取得（Dateクラス形式）
	 * 
	 * @return システム日付(Timestamp:yyyy/mm/dd hh24:mi:ss)
	 * @exception SQLException
	 */
	public String getDate() throws SQLException {
		connect();

		String retSystemDate = null;
		String strSql = "SELECT TO_CHAR(systimestamp,'yyyy/mm/dd hh24:mi:ss') TIMESTAMP FROM DUAL";
		ResultSet rs = null;
		try {
			rs = execQuery(strSql);
			if (rs.next()) {
			    retSystemDate = rs.getString("TIMESTAMP");
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return retSystemDate;
	}
	
	/**
	 * トランザクションで実行する(SQL文：登録・更新系)を設定。
	 * 
	 * 【！】実行前にbeginTran()を実行する必要あり。(初回のみ)
	 * 【！】複数実行するSQLがある場合は、SQL毎にこのメソッドを呼び出す。
	 * @param strSql
	 *            実行可能SQL文
	 * @exception SQLException
	 */
	public void addBatch(String strSql) throws SQLException {
	    try {
	        statement.addBatch(strSql);
	    } catch(SQLException e) {
			errLog("addBatch()に失敗しました。");
			log.write(GS.LOG_ERR,"",strSql);
			throw e;	        
	    }
	}
	
	/**
	 * トランザクション(SQL文：登録・更新系)を実行し、処理結果フラグを返す。
	 * 
	 * 【！】実行前にaddBatch()を実行し、トランザクション内容を設定する必要あり。
	 * @param strSql
	 *            実行可能SQL文
	 * @return boolean(true:処理成功、false:処理失敗）
	 * @exception Exception
	 */
	public boolean execBatch() throws Exception {	    
		try {
			statement.executeBatch();
			
			commit();
			
			return true;
		} catch (Exception e) {
			errLog("■トランザクション（SQL文：登録・更新・削除）の実行に失敗しました。");
			errLog(e.getMessage());
			rollback();
			return false;
		} finally {
		    if(statement != null){
		        statement.close();
		    }
		}
	}
	
	/**
	 * トランザクション(SQL文：登録・更新系)を実行し、処理結果フラグを返す。
	 * 　（単一SQL実行）
	 * @param pstmt
	 *            PreparedStatement
	 * @param comflg
	 *            コミット実行フラグ(true:実行、false:未実行）
	 * @param execflg
	 *            実行フラグ(true:処理実行、false:何もしない）
	 * @return boolean(true:処理成功、false:処理失敗）
	 * @exception Exception
	 */
	public boolean execBatchP(PreparedStatement pstmt, boolean comflg, boolean execflg) throws Exception {	    
		try {
			if (execflg) {
				pstmt.execute();
			
				if (comflg) {
					commit();
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			errLog("■トランザクション（SQL文：登録・更新・削除）の実行に失敗しました。");
			errLog(e.getMessage());
			rollback();
			return false;
		}
	}

    /**
	 * システム日付を取得（タイムスタンプクラス形式）
	 * 
	 * @return システム日付(Timestamp:yyyy-mm-dd hh24:mi:ssxff)
	 * @exception SQLException
	 */
	public java.sql.Timestamp getSystemDate() 
						throws SQLException {
		connect();

		java.sql.Timestamp retSystemDate = null;
		String strSql = "SELECT TO_CHAR(systimestamp,'yyyy-mm-dd hh24:mi:ssxff') TIMESTAMP FROM DUAL";
		ResultSet rs = null;
		try {
			rs = execQuery(strSql);
			if (rs.next()) {
			    retSystemDate = rs.getTimestamp("TIMESTAMP");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return retSystemDate;
	}
	
	/**
	 * 今使用しているコネクションを返却する。 （ストアドプロシジャを呼び出す場合にのみ使用する）
	 * 
	 * @return connection
	 */
	public Connection getConnection() throws SQLException {
		connect();
		//コネクションを返却
		return connection;
	}

	/**
	 * トランザクションの開始宣言を行い、今使用しているコネクションを返却する。
	 * 
	 * @return connection
	 * @exception SQLException
	 */
	public Connection beginTranP() 
			throws SQLException {
		connect();
		log("beginTran()");
		try {
			//トランザクション開始
			if (connection != null) {
				connection.setAutoCommit(false);
			}
			return connection;
		} catch (SQLException e) {
			errLog("■トランザクションの開始に失敗しました。");
			throw e;
		}
	}
	/**
	 * トランザクション(SQL文：登録・更新系)を実行し、処理結果フラグを返す。
	 * 　（同一SQL複数回実行）
	 * @param pstmt
	 *            PreparedStatement
	 * @param comflg
	 *            コミット実行フラグ(true:実行、false:未実行）
	 * @param execflg
	 *            実行フラグ(true:処理実行、false:何もしない）
	 * @return boolean(true:処理成功、false:処理失敗）
	 * @exception Exception
	 */
	public boolean execBatchPB(PreparedStatement pstmt, boolean comflg, boolean execflg) throws Exception {	    
		try {
			if (execflg) {
				pstmt.executeBatch();
			
				if (comflg) {
					commit();
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			errLog("■トランザクション（SQL文：登録・更新・削除）の実行に失敗しました。");
			errLog(e.getMessage());
			rollback();
			return false;
		}
	}
}