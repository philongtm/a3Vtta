/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/12/16		SSC				BJ201612070 SSO対応
003		2017/01/04		SSC				BJ201612070 SSO対応
******************************************************************************/
package app.login.bss;

import app.SessionData;
import app.UserBean;
import app.login.dbAcc.LoginDbAcc;
import app.login.form.LoginForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.struts.AppLocale;
import common.util.Log;


/**
 * ログイン画面ビジネスロジッククラス
 */
public class LoginBss {

	private AppContext appContext	= null;								//APPコンテキスト
	private SqlExecuter sqlExec	= null;								//DBアクセス
	private Log log				= null;								//ログ
	private SessionData cmnData	= null;								//機能共通セッション
	private LoginForm form			= null;								//機能固有セッション
	
	private String REPLACE_USERIDPASSWORD = "replace.userIdPassWord";	//エラーメッセージ置換文字列キー

	/**
	 * コンストラクタ
	 */
	public LoginBss(AppContext appContext) {
		this.appContext = appContext;
		this.sqlExec = appContext.getSqlExecuter();
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.form = (LoginForm)appContext.getActionForm();
	}

	/**
	 * 入力チェック
	 */
	public boolean inputCheck() throws Exception {	
		boolean result = true;
		if(form.getUserId().equals(GS.EMPTY_CHARCTER) || form.getPassword().equals(GS.EMPTY_CHARCTER)){
			appContext.setMsgCode(GL.ERR_INPUT,this.REPLACE_USERIDPASSWORD);
			result = false;
		}
		return result;
	}

	/**
	 * ログイン
	 */
	public boolean logIn() throws Exception {	
		boolean result = false;

		//LDAP認証
		if(!this.execLDAP()){
			form.setPassword(GS.EMPTY_CHARCTER);
			appContext.setMsgCode(GL.ERR_INVALID,REPLACE_USERIDPASSWORD);
			return result;
		}

		//DBアクセスクラス生成
		LoginDbAcc dbAcc = new LoginDbAcc(sqlExec, log, appContext);

		//ユーザ情報Bean生成
		UserBean userBean = new UserBean();

		//ユーザIDチェック
		if(!dbAcc.userIdCheck(userBean)){
			appContext.setMsgCode(GL.ERR_LOGIN);
			return result;
		}

		//参照権限チェック
		if(!dbAcc.sansyouCheck()){
			appContext.setMsgCode(GL.ERR_LOGIN);
			return result;
		}

		//代行排他チェック
		if(!dbAcc.daikouCheck()){
			appContext.setMsgCode(GL.ERR_PERSONLOGIN);
			return result;
		}

		//代行排他設定
		dbAcc.daikouDelete();
		dbAcc.daikouInsert();

		//コミット
		dbAcc.commit();

		//業務フローパターンリスト取得
		dbAcc.setUserGyomu(userBean);
		
		cmnData.setUser_bean(userBean);
		result = true;

		return result;
	}

	/**
	 * ログイン画面戻り時の処理
	 */	
	public void back() throws Exception {
		//DBアクセスクラス生成
		LoginDbAcc dbAcc = new LoginDbAcc(sqlExec, log, appContext);
		//代行削除処理
		dbAcc.daikouDeleteLogoff();
		//コミット
		dbAcc.commit();
	}

	/**
	 * 言語モード設定処理
	 */	
	public void setLangMode() {
	    if(form.getTxtKbnLang() == null || form.getTxtKbnLang().equals(GS.EMPTY_CHARCTER)){
	    	AppLocale.setDefault(appContext.getRequest());
	    }else if(form.getTxtKbnLang().equals(GS.LANG_EN)){
	    	AppLocale.setEn(appContext.getSession());
	    	appContext.setSessionLangMode(GS.LANG,form.getTxtKbnLang());
	    }else{
	    	AppLocale.setJa(appContext.getSession());
	    	appContext.setSessionLangMode(GS.LANG,form.getTxtKbnLang());
	    }
	    cmnData.setComLangMode((String)appContext.getSession().getAttribute(GS.LANG));
	}

	/**
	 * LDAP認証
	 */	
	private boolean execLDAP() throws Exception{
		boolean result = false;
		LoginCertification certification = new LoginCertification(appContext);
		result = certification.execAtt(form.getUserId(),form.getPassword());
		return result;
	}

	/**
	 * SSOユーザ取得
	 */
	public void setSSOUser() {
		if(form.getUserId().equals(GS.EMPTY_CHARCTER)){
			form.setUserId(appContext.getRequest().getHeader(GS.REQUEST_HEADER_TOGO_ID));
		}
	}

	/**
	 * SSOログイン判定
	 *   開発：false（ログイン画面表示）
	 *   本番：true（ログイン画面非表示）
	 */
	public boolean checkSSOLogin() throws Exception{
		if( GS.SSO_LOGIN == null ||
			! GS.SSO_LOGIN.equals("false")){
			return true;
		}
		if(!(form.getUserId().equals(GS.EMPTY_CHARCTER))) {
			return true;
		}
		return false;
	}
}