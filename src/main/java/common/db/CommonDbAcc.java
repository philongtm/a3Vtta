
/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/09		SSC				新規作成
002		2008/04/21		SSC				ヘルプ機能作成
003		2009/05/28		SSC				1.5次版権限取得処理追加
******************************************************************************/
package common.db;

import app.SessionDataZen;
import app.UserBean;
import common.global.GS;
import common.util.Function;
import common.util.JCalendar;
import common.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 共通ＤＢアクセスクラス
 * 
 */
public class CommonDbAcc {

	// 変数宣言
	public static final String	ERR_PROC	= "00";
	public static final String	SUC_PROC	= "01";
	public static final String	WAR_PROC	= "02";

	protected SqlExecuter sqlExec = null;					//DB接続
	protected Log log = null;								//ログ
	protected static final String RESULTSET = "Resultset";

	private static final String SP_SS_O_SELECT_M2100 = "SP_SS_O_SELECT_M2100";	//ヘルプ画像パス取得用プロシージャ
	private static final String SP_SS_OL_SELECT_P0200 = "SP_SS_OL_SELECT_P0200";	//区分値取得用プロシージャ
	private static final String SP_SS_SYSDATE = "SP_SS_SYSDATE";					//SYSDATE取得用プロシージャ
	private static final String SP_SS_TIMESTAMP = "SP_SS_TIMESTAMP";				//TIMESTAMP取得用プロシージャ
	private static final String RET_SYSDATE = "RET_SYSDATE";
	private static final String RET_TIMESTAMP = "RET_TIMESTAMP";
	private static final String DB_ACC_INIT = "DBアクセスクラスを生成します。";
	private static final String DB_ACC_ERR = "ストアド実行エラー";
	private static final String SP_SS_O_COMMIT          		= "SP_SS_O_COMMIT";	//コミット実行 DL.CNC 2009.04.01 追加
	   
	private static final String PATH = "path";	//ヘルプ画像パス取得用カラム名

	//以下1.5次版用定数
	private static final String SYORI_KAISU = "syori_kaisu";
	private static final StringBuffer syoriKaisuSql = new StringBuffer()
															.append("select ")
															.append("TS.syori_kaisu ")
															.append("from ")
															.append("SST_SATEI_STAT SS ")
															.append("left join SST_TAISYOUSAKI TS ")
															.append("on ")
															.append("SS.anken_no = TS.SATEI_ANKEN_NO ")
															.append("where ")
															.append("SS.anken_no = ?");
	//1次版権限取得用文字列
	private static final String SP_SS_OL_NAIGAI					= "SP_SS_OL_NAIGAI";			//プロシージャ
	private static final String OUT_EIGYOU_TOUROKU_KGN			= "OUT_EIGYOU_TOUROKU_KGN";
	private static final String OUT_EIGYOU_SYOUNIN_KGN			= "OUT_EIGYOU_SYOUNIN_KGN";
	private static final String OUT_EIGYOU_KAIKEI_TOUROKU_KGN		= "OUT_EIGYOU_KAIKEI_TOUROKU_KGN";
	private static final String OUT_EIGYOU_KAIKEI_SYOUNIN_KGN		= "OUT_EIGYOU_KAIKEI_SYOUNIN_KGN";
	private static final String OUT_NIJI_SATEI_KBN				= "OUT_NIJI_SATEI_KBN";
	private static final String OUT_NIJI_SATEI_TOUROKU_KGN		= "OUT_NIJI_SATEI_TOUROKU_KGN";
	private static final String OUT_NIJI_SATEI_SYOUNIN_KGN		= "OUT_NIJI_SATEI_SYOUNIN_KGN";
	private static final String OUT_TOKUSYU_KGN					= "OUT_TOKUSYU_KGN";

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 */
	public CommonDbAcc(SqlExecuter	sqlExec, Log log) {
		this.sqlExec	= sqlExec;
		this.log		= log;
		log.write(GS.LOG_INF,getClass().getName(),DB_ACC_INIT);
	}

	public void write(String className,
			JCalendar execDate, int sqlCode,
			String oraMsg, String msg) {
		log.write(GS.LOG_ERR,className,execDate.toString());
		log.write(GS.LOG_ERR,className,Integer.toString(sqlCode));
		log.write(GS.LOG_ERR,className,oraMsg);
		log.write(GS.LOG_ERR,className,msg);
	}

	/**
	 * 国内版権限取得処理 <br>
	 * @param SessionDataZen
	 * 
	 * @exception SQLException
	 */
	public void getKokunaiKengen(UserBean userBean,SessionDataZen cmnZen) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_NAIGAI, sqlExec);

		exCstmt.setStringIn(userBean.getComWorkflowSystemkbn());
		exCstmt.setStringIn(userBean.getComWorkflowSateikaisya_cd());
		exCstmt.setStringIn(userBean.getComWorkflowId());
		//outパラメータ
		exCstmt.setStringOut(OUT_EIGYOU_TOUROKU_KGN);
		exCstmt.setStringOut(OUT_EIGYOU_SYOUNIN_KGN);
		exCstmt.setStringOut(OUT_EIGYOU_KAIKEI_TOUROKU_KGN);
		exCstmt.setStringOut(OUT_EIGYOU_KAIKEI_SYOUNIN_KGN);
		exCstmt.setStringOut(OUT_NIJI_SATEI_KBN);
		exCstmt.setStringOut(OUT_NIJI_SATEI_TOUROKU_KGN);
		exCstmt.setStringOut(OUT_NIJI_SATEI_SYOUNIN_KGN);
		exCstmt.setStringOut(OUT_TOKUSYU_KGN);

		//SQL実行
		try{
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	cmnZen.setComEigyoRegistKg(exCstmt.getString(OUT_EIGYOU_TOUROKU_KGN));
	    	cmnZen.setComEigyoRecogKg(exCstmt.getString(OUT_EIGYOU_SYOUNIN_KGN));
	    	cmnZen.setComEigyoKaiRegistKg(exCstmt.getString(OUT_EIGYOU_KAIKEI_TOUROKU_KGN));
	    	cmnZen.setComEigyoKaiRecogKg(exCstmt.getString(OUT_EIGYOU_KAIKEI_SYOUNIN_KGN));
	    	cmnZen.setComNijiStKbn(Function.trim(exCstmt.getString(OUT_NIJI_SATEI_KBN)));
	    	cmnZen.setComNijiStRegistKg(exCstmt.getString(OUT_NIJI_SATEI_TOUROKU_KGN));
	    	cmnZen.setComNijiStRecogKg(exCstmt.getString(OUT_NIJI_SATEI_SYOUNIN_KGN));
	    	cmnZen.setComUniqueKg(Function.trim(exCstmt.getString(OUT_TOKUSYU_KGN)));
		}catch(SQLException e){
			e.getStackTrace();
			log.write(GS.LOG_INF,getClass().getName(),e.getMessage());
			throw e;
		}
	}	
	/**
	 * 区分値取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * 
	 * @exception SQLException
	 */
	public ResultSet getKbnval(String key,String system_kbn,String langmode) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);

		exCstmt.setStringIn(key);
		exCstmt.setStringIn(langmode);
		exCstmt.setStringIn(system_kbn);		
		//resultSet
		exCstmt.setResultSet(RESULTSET);

		//SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
		ResultSet rs = exCstmt.getResultSet(RESULTSET);

    	return rs;
	}	
	/**
	 * 画像パス取得処理 <br>
	 * @param String
	 * @param String
	 * 
	 * @exception SQLException
	 */
	public String getGazouPath(String gamen_id,String lang_mode,String sysKbn) throws SQLException {

		String path = GS.EMPTY_CHARCTER;
		//ExCallableStatement生成
		sqlExec.getConnection();
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_M2100, sqlExec);

		exCstmt.setStringIn(Function.stringCutter(gamen_id,6));
		exCstmt.setStringIn(lang_mode);
		exCstmt.setStringIn(sysKbn);
		//resultSet
		exCstmt.setResultSet(RESULTSET);

		try{
			//SQL実行	
			exCstmt.execute();
    		isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){
				path = Function.trim(rs.getString(PATH));
			}
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
		
    	return path;
	}	

	/**
	 * Sysdate(yyyy/mm/dd hh24:mi:ss)取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public String getSysdate() throws SQLException {

		String retSysdate = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_SYSDATE, sqlExec);
		//resultSet
		exCstmt.setStringOut(RET_SYSDATE);
    	ResultSet rs = null;
		
		try{
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			retSysdate = Function.trim(exCstmt.getString(RET_SYSDATE));
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
    	
    	return retSysdate;
	}	

	/**
	 * Timestamp(yyyy-mm-dd hh24:mi:ssxff)取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public String getTimestamp() throws SQLException {

		String retTimestamp = null;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_TIMESTAMP, sqlExec);
		//resultSet
		exCstmt.setStringOut(RET_TIMESTAMP);

		try{
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			retTimestamp = Function.trim(exCstmt.getString(RET_TIMESTAMP));
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
    	
    	return retTimestamp;
	}	
	
	/**
	 * ストアド処理結果のチェック<br>
	 * 
	 * @param ExCallableStatement
	 */	public void isError(ExCallableStatement exCstmt) throws SQLException {
		//出力パラメータ取得
		String result = exCstmt.getResult();		//処理結果コード

		//処理失敗もしくは警告
		if (Function.strEquals(result,CommonDbAcc.ERR_PROC) || Function.strEquals(result,CommonDbAcc.WAR_PROC)) {
			this.write(getClass().getName(),exCstmt.getExecDate(),exCstmt.getSqlCode(),exCstmt.getOraMsg(),exCstmt.getMsg());
			throw new SQLException(DB_ACC_ERR);
		}
	}
	 
	    
    /**
     * コミット処理 <br>
     * 
     * @throws SQLException
     */
    public void commit() throws SQLException {

    	ResultSet rs = null;
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_COMMIT, sqlExec);
        try{
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
	 * 案件No処理回数取得メソッド<br>
	 * @param String
	 *         案件No
	 * @return String
	 *         処理回数
	 */
	protected String getAnkenNoSyoriKaisu(String ankenNo) throws SQLException{
		Connection con = null;
		PreparedStatement syoriKaisuSqlP = null;
		ResultSet syoriKaisuRs = null;
		String strSyoriKaisu = null;
    	con = sqlExec.getConnection();
    	try{
    		syoriKaisuSqlP = con.prepareStatement(syoriKaisuSql.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    		syoriKaisuSqlP.setString(1,ankenNo);
    		syoriKaisuRs = sqlExec.execQueryP(syoriKaisuSqlP);
    		while(syoriKaisuRs.next()){
    			strSyoriKaisu = syoriKaisuRs.getString(SYORI_KAISU);
    		}
    	}finally{
	    	if (syoriKaisuRs != null) {
	    		try {
	    			syoriKaisuRs.close();
	    		} catch (Exception e) {
					throw new SQLException(e.getMessage());
	    		}
	    	}
    	}
		return strSyoriKaisu;
	}	
}
