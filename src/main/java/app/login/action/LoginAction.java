/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/12/16		SSC				BJ201612070 SSO対応
******************************************************************************/
package app.login.action;

import app.SessionData;
import app.login.bss.LoginBss;
import app.login.form.LoginForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppAction;
import common.struts.AppLocale;

import java.util.HashMap;

/**
 * OS1101_ログイン アクションクラス
 */
public class LoginAction extends AppAction {

	//ディスパッチマップキー名・アクションメソッド名
	private static final String LOGIN_ACTION		= "login";
	private static final String LOGOUT_ACTION		= "logout";
	private static final String LANG_JA_ACTION	= "langJa";
	private static final String LANG_EN_ACTION	= "langEn";

	private SessionData cmnData = null;

	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
		HashMap<String,String> map = new HashMap<String,String>();
		map.put(LOGIN_ACTION,LOGIN_ACTION);
		map.put(LOGOUT_ACTION,LOGOUT_ACTION);
		map.put(LANG_JA_ACTION,LANG_JA_ACTION);
		map.put(LANG_EN_ACTION,LANG_EN_ACTION);
		return map;
	}

	/**
	 * メニュー画面より遷移時のアクション
	 */
	public Object appExecute(AppContext appContext) throws Exception {

		appContext.setActionForm(appContext.getSessionActionForm(GS.LOGINFORM));
		LoginBss bss = new LoginBss(appContext);
		//代行削除処理
		bss.back();
		
	    return GS.OS1101;
	}
	
	/**
	 * ログイン
	 */
	public Object login(AppContext appContext) throws Exception {
		LoginBss bss = new LoginBss(appContext);

		//SSO_リクエストヘッダー取得
		bss.setSSOUser();

		//言語モード設定
		bss.setLangMode();

		//SSO_ログイン画面表示判定
		if (!bss.checkSSOLogin()){
			//入力チェック処理
			if(!bss.inputCheck()){
				return GS.OS1101;
			}
		}

		//ログイン処理
		if(!bss.logIn()){
			return GS.RC_KENGEN_ERROR;
		}
	    
		//遷移処理(OS2101_メインメニュー)
		MenuAction acc = new MenuAction();
		acc.appExecute(appContext);
		return GS.OS2101;
	}

	/**
	 * ログアウト
	 */
	public Object logout(AppContext appContext) throws Exception {
		return GS.RC_CLOSE;
	}

	/**
	 * 日本語モード
	 */
	public Object langJa(AppContext appContext) throws Exception {
	    LoginForm form = (LoginForm)appContext.getActionForm();
		AppLocale.setJa(appContext.getSession());
		form.setTxtKbnLang(GS.LANG_JA);		
		return GS.OS1101;
	}

	/**
	 * 英語モード
	 */
	public Object langEn(AppContext appContext) throws Exception {
	    LoginForm form = (LoginForm)appContext.getActionForm();
		AppLocale.setEn(appContext.getSession());
		form.setTxtKbnLang(GS.LANG_EN);
		return GS.OS1101;
	}
}