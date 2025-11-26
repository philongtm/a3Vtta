/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.login.dbAcc;

import app.UserBean;
import app.login.form.LoginForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
* ログイン画面DBアクセスクラス
*/
public class LoginDbAcc extends CommonDbAcc {
	
	private LoginForm form = null;						//アクションフォーム

	//プロシージャ名
	private static final String SP_SS_OS_SELECT_IDCHECK		= "SP_SS_OS_SELECT_IDCHECK";	//ユーザIDチェック用プロシージャ
	private static final String SP_SS_OS1101_SELECT_M0200		= "SP_SS_OS1101_SELECT_M0200";	//参照権限チェック用プロシージャ
	private static final String SP_SS_OS1101_SELECT_T2200		= "SP_SS_OS1101_SELECT_T2200";	//代行排他チェック用プロシージャ
	private static final String SP_SS_OS_DELETE_T2200			= "SP_SS_OS_DELETE_T2200";		//代行削除用プロシージャ
	private static final String SP_SS_OS1101_DELETE_T2200		= "SP_SS_OS1101_DELETE_T2200";		//代行削除用プロシージャ
	private static final String SP_SS_OS_INSERT_T2200			= "SP_SS_OS_INSERT_T2200";		//代行登録用プロシージャ
	private static final String SP_SS_OS_SELECT_M2400			= "SP_SS_OS_SELECT_M2400";		//業務フローパターン取得用プロシージャ
	
	//カラム名
	//SP_SS_OS_SELECT_IDCHECK用
	private static final String TOGO_ID					= "togo_id";
	private static final String USER_NM					= "user_nm";
	private static final String USER_NM_E					= "user_nm_e";
	private static final String SOSHIKI_CD				= "soshiki_cd";
	private static final String SOSHIKI_NM				= "soshiki_nm";
	private static final String SOSHIKI_NM_E				= "soshiki_nm_e";
	private static final String COMPANY_CD				= "company_cd";
	private static final String COMPANY_NM				= "company_nm";
	private static final String COMPANY_NM_E				= "company_nm_e";
	private static final String ADMIN_FLG					= "admin_flg";
	private static final String MAIL_HAISIN_KBN			= "mail_haisin_kbn";
	private static final String EMAIL_ADDR				= "email_addr";
	private static final String PRINTOUT_DEFAULT_LANG_KBN	= "printout_default_lang_kbn";
    private static final String BU_CD 					= "bu_cd";
    private static final String BUMON_CD 					= "bumon_cd";
	//SP_SS_OS1101_SELECT_M0200用
	private static final String COUNT						= "cnt";
	//SP_SS_OS_SELECT_T2200用
	private static final String DAIKO_ID					= "daiko_id";
	//SP_SS_OS_SELECT_M2400用
	private static final String SYSTEM_KBN				= "system_kbn";
	private static final String SATEIKAISYA_CD			= "sateikaisya_cd";
	private static final String PATTERN_ID				= "pattern_id";
	private static final String DEFAULT_FLG				= "default_flg";

	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param log
	 *            log を設定。
	 * @param appContext
	 *            appContext を設定。
	 */
	public LoginDbAcc(SqlExecuter sqlExec,Log log,AppContext appContext) {
		super(sqlExec,log);
		form = (LoginForm)appContext.getActionForm();
	}

	/**
	 * 
	 * ユーザIDチェック処理
	 * 
	 * @exception SQLException
	 */
	public boolean userIdCheck(UserBean userBean) throws SQLException {

    	boolean result = false;
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_IDCHECK,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
		exCstmt.setResultSet(RESULTSET);
	    
		//プロシージャ実行	
	    try {
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//機能共通セッションに取得値を格納
			while(rs.next()){
	    		result = true;
	    		userBean.setComUserId(Function.trim(rs.getString(TOGO_ID)));
	    		userBean.setComUser_Nm(Function.trim(rs.getString(USER_NM)));
	    		userBean.setComUser_Nm_En(Function.trim(rs.getString(USER_NM_E)));
	    		userBean.setComSyozokuSoshikiCd(Function.trim(rs.getString(SOSHIKI_CD)));
	    		userBean.setComSyozokuSoshiki_Nm(Function.trim(rs.getString(SOSHIKI_NM)));
	    		userBean.setComSyozokuSoshiki_Nm_En(Function.trim(rs.getString(SOSHIKI_NM_E)));
	    		userBean.setComKaishaCd(Function.trim(rs.getString(COMPANY_CD)));
	    		userBean.setComKaisha_Nm(Function.trim(rs.getString(COMPANY_NM)));
	    		userBean.setComKaisha_Nm_En(Function.trim(rs.getString(COMPANY_NM_E)));
	    		userBean.setComEmailAddr(Function.trim(rs.getString(EMAIL_ADDR)));
	    		userBean.setComSystemManager_flg(Function.trim(rs.getString(ADMIN_FLG)));
	    		userBean.setComMailHaisinKbn(Function.trim(rs.getString(MAIL_HAISIN_KBN)));
	    		userBean.setComTyohyo_default_kbn(Function.trim(rs.getString(PRINTOUT_DEFAULT_LANG_KBN)));
            	userBean.setComSyozokuBunrui2(rs.getString(BUMON_CD));
            	userBean.setComSyozokuBuCd(rs.getString(BU_CD));

			}
	    } finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	    return result;
	}

	/**
	 * 
	 * 参照権限チェック処理
	 * 
	 * @exception SQLException
	 */
	public boolean sansyouCheck() throws SQLException {

    	boolean result = false;
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS1101_SELECT_M0200,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
		exCstmt.setIntOut(COUNT);
	    
		//プロシージャ実行	
	    try {
	    	exCstmt.execute();
	    	isError(exCstmt);

    		if(exCstmt.getInt(COUNT) > 0) {
    			result = true;
    		}
	    } finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	    return result;
	}

	/**
	 * 
	 * 代行排他チェック処理
	 * 
	 * @exception SQLException
	 */
	public boolean daikouCheck() throws SQLException {

    	boolean result = true;
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS1101_SELECT_T2200,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
		exCstmt.setResultSet(RESULTSET);
	    
		//プロシージャ実行	
	    try {
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			//代行排他チェック
			while(rs.next()){
				if(Function.trim(rs.getString(DAIKO_ID)).equals(form.getUserId())){
		    		result = true;
		    		break;
				}else{
					result = false;
				}
	    	}
	    } finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	    return result;
	}

	/**
	 * 
	 * 代行登録処理
	 * 
	 * @exception SQLException
	 */
	public void daikouInsert() throws SQLException {

		String strNull = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_INSERT_T2200,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
		exCstmt.setStringIn(strNull);
		exCstmt.setStringIn(form.getUserId());
	    
		//プロシージャ実行	
	    exCstmt.execute();
	    isError(exCstmt);
	}

	/**
	 * 
	 * 代行削除処理
	 * 
	 * @exception SQLException
	 */
	public void daikouDelete() throws SQLException {

		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_DELETE_T2200,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
	    
		//プロシージャ実行	
	    exCstmt.execute();
	    isError(exCstmt);
	}

	/**
	 * 
	 * 代行削除処理
	 * 
	 * @exception SQLException
	 */
	public void daikouDeleteLogoff() throws SQLException {

		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS1101_DELETE_T2200,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());

		//プロシージャ実行	
	    exCstmt.execute();
	    isError(exCstmt);
	}

	/**
	 * 
	 * 業務フローパターン取得処理
	 * 
	 * @exception SQLException
	 */
	public void setUserGyomu(UserBean userBean) throws SQLException {

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M2400,this.sqlExec);
		exCstmt.setStringIn(form.getUserId());
		exCstmt.setResultSet(RESULTSET);
    	List<HashMap> list = new ArrayList<HashMap>();	//業務フローパターンリスト

		//プロシージャ実行	
	    try {
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//業務フローパターンをリストに格納
			while(rs.next()){
				HashMap<String,String> map = new HashMap<String,String>();
	    		map.put(SYSTEM_KBN,Function.trim(rs.getString(SYSTEM_KBN)));
	    		map.put(SATEIKAISYA_CD,Function.trim(rs.getString(SATEIKAISYA_CD)));
	    		map.put(PATTERN_ID,Function.trim(rs.getString(PATTERN_ID)));
	    		map.put(DEFAULT_FLG,Function.trim(rs.getString(DEFAULT_FLG)));
	    		list.add(map);
	    	}
			
			//機能共通セッションのユーザ情報Beanに設定
			userBean.setComWorkflowList(list);

	    } finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
}