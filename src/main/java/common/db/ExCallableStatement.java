/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/13		SSC				1.5次版に修正を施し流用
******************************************************************************/
package common.db;

import common.util.Function;
import common.util.JCalendar;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 拡張CallableStatementクラス
 * 
 */
public class ExCallableStatement {

	private static final String ERROR_PROCNAME	= "指定されたパラメータ名称が見つかりません。指定されたパラメータ名称は";
	private static final String ERROR_TYPES		= "パラメータの型と取得メソッドが一致しません。要求するメソッドは";
	private static final String ERROR_UNDEFINED	= "パラメータのタイプが未定義です。";
	private static final String ERROR_EXECMODE	= "起動モードに誤りがあります。executeMode:";
	private static final String MSG_PARAMNAME		= " paramName:";
	private static final String MSG_INDEX			= " index:";
	private static final String MSG_STRING		= "getString(java.lang.String)";
	private static final String MSG_INT			= "getInt(java.lang.String)";
	private static final String MSG_LONG			= "getLong(java.lang.String)";
	private static final String MSG_DOUBLE		= "getDouble(java.lang.String)";
	private static final String MSG_TIMESTAMP		= "getTimestamp(java.lang.String)";
	private static final String MSG_DATE			= "getDate(java.lang.String)";
	private static final String MSG_RESULTSET		= "getResultSet(java.lang.String)";
	
	private static final String CONSTRUCT_PROC	= "{call ";
	private static final String CONSTRUCT_FUNC	= "{call ? := ";
	private static final String CONSTRUCT_KAKKO	= "(";
	private static final String CONSTRUCT_APPEND	= "?,";
	private static final String CONSTRUCT_END	= "?,?,?,?,?)}";
		
	private CallableStatement cstmt	= null;
	private Map<String,ParamMapBean> paramMap				= null;
	private ArrayList<String> mapIndex			= null;
	private String procName			= null;
	private SqlExecuter sqlExec		= null;
	private int index					= 0;
	
	private String result				= null;		// 処理結果コード
	private JCalendar execDate			= null;		// 処理日時
	private int sqlCode				= 0;		// SQLコード
	private String oraMsg				= null;		// Oracleメッセージ
	private String msg					= null; 	// 表示可能な結果メッセージ
	
	private boolean rsValid			= false;	// ResultSet取得フラグ
	private int executeMode			= 0;		// 動作モード

	public static int MODE_PROCEDURE	= 1;		// 動作モード＿PROCEDURE起動
	public static int MODE_FUNCTION	= 2;		// 動作モード＿FUNCTION起動
	
	/**
	 * コンストラクタ
	 * 
	 * @param procName プロシージャ名称
	 * @param sqlExec SqlExecuterのインスタンス
	 */
	public ExCallableStatement(String procName, SqlExecuter sqlExec) {
		this.procName = procName;
		this.sqlExec = sqlExec;
		this.paramMap = new HashMap<String,ParamMapBean>();
		this.mapIndex = new ArrayList<String>();
		this.executeMode = MODE_PROCEDURE;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param procName プロシージャ名称
	 * @param sqlExec SqlExecuterのインスタンス
	 * @param executeMode 動作モード
	 */
	public ExCallableStatement(String procName, SqlExecuter sqlExec, int executeMode) {
		this.procName = procName;
		this.sqlExec = sqlExec;
		this.paramMap = new HashMap<String,ParamMapBean>();
		this.mapIndex = new ArrayList<String>();
		this.executeMode = executeMode;
	}

	private void setObject(String paramName, Object paramValue, boolean inFlg, boolean outFlg, int type) {
		this.index += 1;
		ParamMapBean bean = new ParamMapBean();
		bean.setIndex(this.index);
		bean.setType(type);
		bean.setParamName(paramName);
		bean.setParamValue(paramValue);
		bean.setInFlg(inFlg);
		bean.setOutFlg(outFlg);
		paramMap.put(paramName,bean);
		mapIndex.add(paramName);
	}
	
	/**
	 * String用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setStringIn(String paramName, String paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.VARCHAR);
	}
	
	/**
	 * int用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setIntIn(String paramName, int paramValue) {
		setObject(paramName,new Integer(paramValue),true,false,java.sql.Types.INTEGER);
	}

	/**
	 * int用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setIntIn(String paramName, Integer paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.INTEGER);
	}

	/**
	 * long用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setLongIn(String paramName, long paramValue) {
		setObject(paramName,new Long(paramValue),true,false,java.sql.Types.BIGINT);
	}

	/**
	 * long用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setLongIn(String paramName, Long paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.BIGINT);
	}

	/**
	 * double用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDoubleIn(String paramName, double paramValue) {
		setObject(paramName,new Double(paramValue),true,false,java.sql.Types.DOUBLE);
	}
	
	/**
	 * double用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDoubleIn(String paramName, Double paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.DOUBLE);
	}
	
	/**
	 * Timestamp用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setTimestampIn(String paramName, Timestamp paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.TIMESTAMP);
	}

	/**
	 * Date用INパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDateIn(String paramName, Date paramValue) {
		setObject(paramName,paramValue,true,false,java.sql.Types.DATE);
	}
	
	/**
	 * String用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setStringIn(String paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.VARCHAR);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.VARCHAR);
	}
	
	/**
	 * int用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setIntIn(int paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.INTEGER);
		paramName.append(index + 1);
		setObject(paramName.toString(),new Integer(paramValue),true,false,java.sql.Types.INTEGER);
	}

	/**
	 * int用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setIntIn(Integer paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.INTEGER);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.INTEGER);
	}

	/**
	 * long用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setLongIn(long paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.BIGINT);
		paramName.append(index + 1);
		setObject(paramName.toString(),new Long(paramValue),true,false,java.sql.Types.BIGINT);
	}

	/**
	 * long用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setLongIn(Long paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.BIGINT);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.BIGINT);
	}

	/**
	 * double用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setDoubleIn(double paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.DOUBLE);
		paramName.append(index + 1);
		setObject(paramName.toString(),new Double(paramValue),true,false,java.sql.Types.DOUBLE);
	}

	/**
	 * double用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setDoubleIn(Double paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.DOUBLE);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.DOUBLE);
	}

	/**
	 * Timestamp用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setTimestampIn(Timestamp paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.TIMESTAMP);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.TIMESTAMP);
	}

	/**
	 * Date用INパラメータ設定
	 * 
	 * @param paramValue 値
	 */
	public void setDateIn(Date paramValue) {
		StringBuffer paramName = new StringBuffer();
		paramName.append(java.sql.Types.DATE);
		paramName.append(index + 1);
		setObject(paramName.toString(),paramValue,true,false,java.sql.Types.DATE);
	}
	
	/**
	 * String用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setStringInOut(String paramName, String paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.VARCHAR);
	}

	/**
	 * int用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setIntInOut(String paramName, int paramValue) {
		setObject(paramName,new Integer(paramValue),true,true,java.sql.Types.INTEGER);
	}

	/**
	 * int用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setIntInOut(String paramName, Integer paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.INTEGER);
	}

	/**
	 * long用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setLongInOut(String paramName, long paramValue) {
		setObject(paramName,new Long(paramValue),true,true,java.sql.Types.BIGINT);
	}

	/**
	 * long用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setLongInOut(String paramName, Long paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.BIGINT);
	}

	/**
	 * double用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDoubleInOut(String paramName, double paramValue) {
		setObject(paramName,new Double(paramValue),true,true,java.sql.Types.DOUBLE);
	}

	/**
	 * double用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDoubleInOut(String paramName, Double paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.DOUBLE);
	}

	/**
	 * Timestamp用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setTimestampInOut(String paramName, Timestamp paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.TIMESTAMP);
	}

	/**
	 * Date用IN,OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 * @param paramValue 値
	 */
	public void setDateInOut(String paramName, Date paramValue) {
		setObject(paramName,paramValue,true,true,java.sql.Types.DATE);
	}

	/**
	 * String用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setStringOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.VARCHAR);
	}

	/**
	 * int用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setIntOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.INTEGER);
	}

	/**
	 * long用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setLongOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.BIGINT);
	}

	/**
	 * double用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setDoubleOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.DOUBLE);
	}

	/**
	 * Timestamp用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setTimestampOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.TIMESTAMP);
	}

	/**
	 * Date用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setDateOut(String paramName) {
		setObject(paramName,null,false,true,java.sql.Types.DATE);
	}

	/**
	 * ResultSet用OUTパラメータ設定
	 * 
	 * @param paramName パラメータ名
	 */
	public void setResultSet(String paramName) {
		setObject(paramName,null,false,true,OracleTypes.CURSOR);
	}
	
	/**
	 * String取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したString
	 * @throws SQLException
	 */
	public String getString(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.VARCHAR) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_STRING);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return null;
		} else {
			return Function.convert((String)bean.getParamValue());
		}
	}

	/**
	 * int取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したint
	 * @throws SQLException
	 */
	public int getInt(String paramName) throws SQLException{
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.INTEGER) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_INT);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return 0;
		} else {
			Integer intObject = (Integer)bean.getParamValue();
			return intObject.intValue();
		}
	}

	/**
	 * long取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したlong
	 * @throws SQLException
	 */
	public long getLong(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.BIGINT) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_LONG);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return 0;
		} else {
			Long longObject = (Long)bean.getParamValue();
			return longObject.longValue();
		}
	}

	/**
	 * double取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したdouble
	 * @throws SQLException
	 */
	public double getDouble(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.DOUBLE) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_STRING);
			exceptionMsg.append(MSG_DOUBLE);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return 0;
		} else {
			Double doubleObject = (Double)bean.getParamValue();
			return doubleObject.doubleValue();
		}
	}

	/**
	 * Timestamp取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したTimestamp
	 * @throws SQLException
	 */
	public Timestamp getTimestamp(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.TIMESTAMP) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_TIMESTAMP);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return null;
		} else {
			return (Timestamp)bean.getParamValue();
		}
	}

	/**
	 * Date取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したDate
	 * @throws SQLException
	 */
	public Date getDate(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != java.sql.Types.DATE) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_DATE);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getParamValue() == null) {
			return null;
		} else {
			return (Date)bean.getParamValue();
		}
	}

	/**
	 * ResultSet取得
	 * 
	 * @param paramName パラメータ名
	 * @return パラメータより取得したResultSet
	 * @throws SQLException
	 */
	public ResultSet getResultSet(String paramName) throws SQLException {
		ParamMapBean bean = (ParamMapBean)paramMap.get(paramName);
		if (bean == null) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_PROCNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (bean.getType() != OracleTypes.CURSOR) {
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_TYPES);
			exceptionMsg.append(MSG_RESULTSET);
			exceptionMsg.append(MSG_PARAMNAME);
			exceptionMsg.append(paramName);
			throw new SQLException(exceptionMsg.toString());
		}
		if (rsValid) {
			return new ExResultSet((ResultSet)bean.getParamValue(),cstmt);
		} else {
			return null; 
		}
	}
	
	private String constructProcName() throws SQLException {
		StringBuffer construct = new StringBuffer();
		if (executeMode == MODE_PROCEDURE) {
			// PROCEDUREモード
			construct.append(CONSTRUCT_PROC);
			construct.append(procName);
			if (index >= 0) {
				construct.append(CONSTRUCT_KAKKO);
				for (int i = 1; i <= index; i++) {
					construct.append(CONSTRUCT_APPEND);
				}
			}
			construct.append(CONSTRUCT_END);
		} else if (executeMode == MODE_FUNCTION) {
			// FUNCTIONモード
			int bufIndex = index - 1;	// 戻り値の分、indexから1を引く
			construct.append(CONSTRUCT_FUNC);
			construct.append(procName);
			if (bufIndex >= 1) {
				construct.append(CONSTRUCT_KAKKO);
				for (int i = 1; i <= bufIndex; i++) {
					construct.append(CONSTRUCT_APPEND);
				}
			}
			construct.append(CONSTRUCT_END);
		} else {
			// 想定外のexecuteMode
			StringBuffer exceptionMsg = new StringBuffer();
			exceptionMsg.append(ERROR_EXECMODE);
			exceptionMsg.append(executeMode);
			throw new SQLException(exceptionMsg.toString());
		}
		return construct.toString();
	}
	
	/**
	 * ストアド実行メソッド
	 * 
	 * @throws SQLException
	 */
	public void execute() throws SQLException {

		ParamMapBean bean = null;
		List<ParamMapBean> bufList = new ArrayList<ParamMapBean>();

		try {
			cstmt = sqlExec.getCallableStatement(constructProcName());
			for (int i = 0; i < index; i++) {
				String paramName = (String)mapIndex.get(i);
				bean = (ParamMapBean)paramMap.get(paramName);
				cstmt.registerOutParameter(bean.getIndex(), bean.getType());
				bufList.add(bean);
			}
			cstmt.registerOutParameter(index + 1, java.sql.Types.VARCHAR);		// 処理結果コード
			cstmt.registerOutParameter(index + 2, java.sql.Types.TIMESTAMP);	// 処理日時
			cstmt.registerOutParameter(index + 3, java.sql.Types.INTEGER);		// SQLコード
			cstmt.registerOutParameter(index + 4, java.sql.Types.VARCHAR);		// Oracleメッセージ
			cstmt.registerOutParameter(index + 5, java.sql.Types.VARCHAR);		// 表示可能な結果メッセージ
			for (int i = 0; i < index; i++) {
				bean = (ParamMapBean)bufList.get(i);
				if (bean.isInFlg()) {
					switch (bean.getType()) {
						case java.sql.Types.VARCHAR:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.VARCHAR);
							} else {
								cstmt.setString(bean.getIndex(), (String)bean.getParamValue());
							}
							break;
						case java.sql.Types.INTEGER:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.INTEGER);
							} else {
								Integer intObject = (Integer)bean.getParamValue();
								cstmt.setInt(bean.getIndex(), intObject.intValue());
							}
							break;
						case java.sql.Types.BIGINT:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.BIGINT);
							} else {
								Long longObject = (Long)bean.getParamValue();
								cstmt.setLong(bean.getIndex(), longObject.longValue());
							}
							break;
						case java.sql.Types.DOUBLE:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.DOUBLE);
							} else {
								Double doubleObject = (Double)bean.getParamValue();
								cstmt.setDouble(bean.getIndex(), doubleObject.doubleValue());
							}
							break;
						case java.sql.Types.TIMESTAMP:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.TIMESTAMP);
							} else {
								cstmt.setTimestamp(bean.getIndex(), (Timestamp)bean.getParamValue());
							}
							break;
						case java.sql.Types.DATE:
							if (bean.getParamValue() == null) {
								cstmt.setNull(bean.getIndex(), java.sql.Types.DATE);
							} else {
								cstmt.setDate(bean.getIndex(), (Date)bean.getParamValue());
							}
							break;
						case OracleTypes.CURSOR:
							break;
						default:
							StringBuffer exceptionMsg = new StringBuffer();
							exceptionMsg.append(ERROR_UNDEFINED);
							exceptionMsg.append(MSG_INDEX);
							exceptionMsg.append(bean.getIndex());
							exceptionMsg.append(MSG_PARAMNAME);
							exceptionMsg.append(bean.getParamName());
							throw new SQLException(exceptionMsg.toString());
					}
				}
			}
			cstmt.execute();
		
			result		= Function.trim(cstmt.getString(index + 1));	// 処理結果コード
			if (Function.strEquals(result, CommonDbAcc.ERR_PROC) || Function.strEquals(result, CommonDbAcc.WAR_PROC)) {
				rsValid = false;	// 処理結果コードがエラー、警告の場合はカーソル(ResultSet)を取得しない
			} else {
				rsValid = true;		// 処理結果コードが正常の場合はカーソル(ResultSet)を取得する
			}
			execDate	= new JCalendar(cstmt.getTimestamp(index + 2));	// 処理日時
			sqlCode		= cstmt.getInt(index + 3);						// SQLコード
			oraMsg		= Function.trim(cstmt.getString(index + 4));	// Oracleメッセージ
			msg			= Function.trim(cstmt.getString(index + 5)); 	// 表示可能な結果メッセージ

			for (int i = 0; i < index; i++) {
				bean = (ParamMapBean)bufList.get(i);
				if (bean.isOutFlg()) {
					switch (bean.getType()) {
						case java.sql.Types.VARCHAR:
							bean.setParamValue(cstmt.getString(bean.getIndex()));
							break;
						case java.sql.Types.INTEGER:
							bean.setParamValue(new Integer(cstmt.getInt(bean.getIndex())));
							break;
						case java.sql.Types.BIGINT:
							bean.setParamValue(new Long(cstmt.getLong(bean.getIndex())));
							break;
						case java.sql.Types.DOUBLE:
							bean.setParamValue(new Double(cstmt.getDouble(bean.getIndex())));
							break;
						case java.sql.Types.TIMESTAMP:
							bean.setParamValue(cstmt.getTimestamp(bean.getIndex()));
							break;
						case java.sql.Types.DATE:
							bean.setParamValue(cstmt.getDate(bean.getIndex()));
							break;
						case OracleTypes.CURSOR:
							if (rsValid) {
								bean.setParamValue(cstmt.getObject(bean.getIndex()));
							} else {
								try {
									ResultSet rs = (ResultSet)cstmt.getObject(bean.getIndex());
									if (rs != null) {
										rs.close();
									}
								} catch (SQLException e) {}
								bean.setParamValue(null);
							}
							break;
						default:
							StringBuffer exceptionMsg = new StringBuffer();
							exceptionMsg.append(ERROR_UNDEFINED);
							exceptionMsg.append(MSG_INDEX);
							exceptionMsg.append(bean.getIndex());
							exceptionMsg.append(MSG_PARAMNAME);
							exceptionMsg.append(bean.getParamName());
							throw new SQLException(exceptionMsg.toString());
					}
				}
			}
		} finally {
			if(cstmt != null){
				if(bean == null){
					cstmt.close();
				}else if(OracleTypes.CURSOR != bean.getType()){
					cstmt.close();
				}
			}
		}
	}
	
	/**
	 * @return 処理日時 を戻します。
	 */
	public JCalendar getExecDate() {
		return execDate;
	}
	/**
	 * @return 表示可能な結果メッセージ を戻します。
	 */
	public String getMsg() {
		return msg;
	}
	/**
	 * @return Oracleメッセージ を戻します。
	 */
	public String getOraMsg() {
		return oraMsg;
	}
	/**
	 * @return 処理結果コード を戻します。
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @return SQLコード を戻します。
	 */
	public int getSqlCode() {
		return sqlCode;
	}
}