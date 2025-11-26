/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/10/21		SSC				課題No.65 パスワードログ出力削除
******************************************************************************/
package common.db;

import common.global.GS;
import common.util.Log;
import common.util.Profile;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *  ＤＢのコネクションプールクラス
 * 
 */
public class PooledConnection {

  /** 自分自身のインスタンス */
  static private PooledConnection me = null;

  private static String rootDir = null;
  private static final String VALIDATION_QUERY = "select 1 from dual";
  
  private PoolingDataSource ds = null;
  private GenericObjectPool pool;

  /** 最大プールコネクション数 */
  private int dbMaxConn = 0;

  /** リトライ回数 **/
  private int TIMEOUT = 0;

  private Log log;
  
  private SQLException e;
  
  private boolean clearRequest = false;

  /**
   * コンストラクタ。
   * <pre>
   * DBとのコネクションを確立してプールする。
   * </pre>
   */
  private PooledConnection(Log log) {
  	this.log = log;
  	
  	log.write(GS.LOG_INF,getClass().getName(),"PooledConnection");
    
    // JDBCドライバ名
    String dbName = "";
    // DB接続URL
    String dbURL = "";
    // DB接続ユーザ名
    String dbUser = "";
    // DB接続パスワード
    String dbPassword = "";
    
	dbName     = Profile.getString(GS.PROFILE_DB_NAME,"");
	dbMaxConn  = Profile.getInt(GS.PROFILE_DB_MAXCONN,0);
	TIMEOUT    = Profile.getInt(GS.PROFILE_DB_TIMEOUT,0);

	log.write(GS.LOG_INF,getClass().getName(),"rootDir : " + rootDir);

	// コンテキスト毎のURLを取得
	dbURL = Profile.getString(rootDir+".URL","");
	if(dbURL.length()==0) {
		// コンテキスト毎のURLがない場合はデフォルトを取得
		log.write(GS.LOG_INF,getClass().getName(),"not found : " + rootDir+".URL");
		dbURL = Profile.getString(GS.PROFILE_DB_URL,"");
	}
	log.write(GS.LOG_INF,getClass().getName(),"dbURL : " + dbURL);

	// コンテキスト毎のユーザを取得
	dbUser = Profile.getString(rootDir+".User","");
	if(dbUser.length()==0) {
		// コンテキスト毎のURLがない場合はデフォルトを取得
		log.write(GS.LOG_INF,getClass().getName(),"not found : " + rootDir+".User");
		dbUser = Profile.getString(GS.PROFILE_DB_USER,"");
	}
	log.write(GS.LOG_INF,getClass().getName(),"dbUser : " + dbUser);
	
	// コンテキスト毎のパスワードを取得
	dbPassword = Profile.getString(rootDir+".Password","");
	if(dbPassword.length()==0) {
		// コンテキスト毎のURLがない場合はデフォルトを取得
		//課題No.65
		//削除開始
		//log.write(GS.LOG_INF,getClass().getName(),"not found : " + rootDir+".Password");
		//削除完了
		dbPassword = Profile.getString(GS.PROFILE_DB_PASSWORD,"");
	}
	//課題No.65
	//削除開始
	//log.write(GS.LOG_INF,getClass().getName(),"dbPassword : " + dbPassword);
	//削除完了
	
/*
 StackObjectPool(PoolableObjectFactory factory, int max, int init) 
 指定された factory を新規インスタンスの生成に使用し、 "休止した"インスタンスの数を max に制限し、 
 初期化時に少なくとも init 個のインスタンスを格納できる容量を確保する、新たな StackObjectPool を生成します。 
 */
	try{
	    // 使用するJDBCドライバの登録
	    Class.forName(dbName);

	    pool = new GenericObjectPool(null);
	    pool.setTestOnBorrow(true);
	    pool.setMaxActive(dbMaxConn);
	    pool.setMaxIdle(dbMaxConn);
	    pool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
	    ConnectionFactory conFactory = new DriverManagerConnectionFactory(dbURL, dbUser, dbPassword);
	    /*
	    PoolableConnectionFactoryのコンストラクタの引数
	     connFactory		Connection オブジェクトを生成するためのConnectionFactoryインスタンス 
		 pool				Connectionのプーリングに用いるObjectPoolインスタンス 
		 stmtPoolFactory	PreparedStatementオブジェクトをプーリングする場合、プーリング用の
		 					KeyedObjectPoolオブジェクトを生成するためのKeyedObjectPoolFactoryインスタンス。
		 					PreparedStatementオブジェクトをプーリングしない場合はnullを指定 
		 validationQuery	Connectionが有効であるかどうかを検査するためのSQL文 
		 defaultReadOnly	プールから取り出されたConnectionを読み込み専用にする 
		 defaultAutoCommit	プールから取り出されたConnectionを自動コミットモードにする 
		*/
	    new PoolableConnectionFactory(conFactory, pool, null, VALIDATION_QUERY, false, false);
	    ds = new PoolingDataSource(pool);
	} catch(Exception e){
		e.printStackTrace();
	}
  }

  /**
   * 自身のインスタンスを戻すメソッド。
   * <pre>
   * 自身が生成されていなければ生成したオブジェクト、既に生成されていればそのオブジェクトを
   * 戻す。
   * </pre>
   * @return PooledConnectionのインスタンス
   */
  public static synchronized PooledConnection getInstance(Log log) {
  	if (me == null) {
  		me = new PooledConnection(log);
	}
  	return me;
  }

  /**
   * プールされているコネクションを戻すメソッド(タイムアウトあり)。
   * <pre>
   * フリーなコネクションを保持しており有効であれば、そのコネクションを戻す。
   * フリーで有効なコネクションが無い場合、指定されたMAXのコネクションを確保していなければ新たにコネクション
   * を確立して、それを戻す。有効なコネクションが無い場合、リトライする。
   * </pre>
   * @return コネクション = null:有効なコネクションが無い/ != null:有効なコネクション
   */
  public Connection getConnection() {
  	Connection con = null;
  	
	Exception err = null;
	
	for( int i=1; i<=TIMEOUT; i++ ) {
	 	try{
	   		con = ConnectionIO(null);
	 		if(con==null) {
	 			log.write(GS.LOG_WAR,getClass().getName(),"getConnection ＤＢ接続のリトライ実行 （" + i + "/" + TIMEOUT + "）connection full");
		 		try{Thread.sleep(1000);}catch(Exception e1){}
		 		continue;
	 		}
	   		break;
	 	} catch(SQLException e){
	 		err = e;
	 		log.write(GS.LOG_ERR,getClass().getName(),"getConnection ＤＢ接続のリトライ実行（" + i + "/" + TIMEOUT + "）" + e.toString() );
	 		try{Thread.sleep(1000);	}catch(Exception e1){}
	 	} catch(Exception e) {
	 		err = e;
	 		break;
	  	}
 	}
	if(con==null){

		if(err!=null){
	 		setError(err,err.toString());
		}else {
			setError(new Exception(),"getConnection ＤＢ接続失敗 connection full (numActive:"+pool.getNumActive()+" numIdle:"+pool.getNumIdle()+")");
		}
	}

	return con;
  }

  	/**
  	 * DBとのコネクションを開放するメソッド。
  	 * <pre>
  	 * 渡されたコネクションをコネクションプールに戻す。
  	 * </pre>
  	 * @param con コネクション
  	 */
	public void releaseConnection(Connection con)	{
		try {
			ConnectionIO(con);
		} catch (SQLException e) {
		}
	}

  	private synchronized Connection ConnectionIO(Connection con) throws SQLException {
  		// GET CONNECTION
  		if(con == null){
			int active = pool.getNumActive();
			int idle = pool.getNumIdle();
	 		if( (dbMaxConn==0) || ((active+idle)<dbMaxConn) ) {
	 			numLog("ConnectionIO:getConnection1 - start");
	 			Connection c = ds.getConnection(); 
	 			numLog("ConnectionIO:getConnection1 - end");
	  			return c;
	 		} else 	if( idle > 0 ) {
	 			numLog("ConnectionIO:getConnection2 - start");
	 			Connection c = ds.getConnection(); 
	 			numLog("ConnectionIO:getConnection2 - end");
	  			return c;
	 		} else {
	 			numLog("ConnectionIO:getConnection - connection full");
	 			return null;
	 		}
  		
  		// RELEASE CONNECTION
  		} else {
			try{
				numLog("ConnectionIO:releaseConnection - start");
				con.close();
				numLog("ConnectionIO:releaseConnection - end");
				// コネクションプールのクリア
				if(clearRequest){
					clearRequest = false;
					pool.clear();
			  		numLog("ConnectionIO:releaseConnection - clear");
				}
			} catch(Exception e){
				setError(e,"releaseConnection コネクション返却失敗");
			}
			return null;
  		}
  	}
  
  /**
   * コネクションプールのクリアを行う。
   * releaseConnection()で実行される。
   */
  public void clear() {
  	clearRequest = true;
  }
 
  /**
   * オブジェクトのファイナライズメソッド。
   * <pre>
   * allReleaseメソッドを呼び出す。
   * </pre>
   */
//  protected void finalize() throws Throwable {
//    trace("PooledConnection finalize start.", GS.LOG_INF);
//    allRelease();
//    trace("PooledConnection finalize end.", GS.LOG_INF);
//    super.finalize();
//  }
//  
//  private void trace(String msg,int level){
//  	log.write(level,getClass().getName(),msg);
//  }
  
  private void setError(Exception e, String msg) {
  	this.e = new SQLException(msg);
  	this.e.setStackTrace(e.getStackTrace());
  }
  
  public SQLException getException() {
  	return e;
  }
  
  private void numLog(String msg){
	log.write(GS.LOG_INF,getClass().getName(), msg + " : numActive:"+pool.getNumActive()+" numIdle:"+pool.getNumIdle());
  }

	/**
	 * @param contextPath contextPath を設定。
	 */
	public static void setRootDir(String rootDir) {
		PooledConnection.rootDir = rootDir;
	}
}
