/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.login.form;

import common.global.GS;
import common.struts.AppActionForm;
import common.struts.adapter.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * OS1101_ログイン アクションフォームクラス
 * 
 */
public class LoginForm extends AppActionForm {

	private String userId		= GS.EMPTY_CHARCTER;		//ユーザＩＤ
	private String password	= GS.EMPTY_CHARCTER;		//パスワード
	private String txtKbnLang	= GS.EMPTY_CHARCTER;		//言語モード

	/**
	 * コンストラクタ
	 */	
	public LoginForm() {
		super.gamenId = GS.OS1101;
	}

	public void appValidate(ActionMapping mapping, HttpServletRequest request) {
	}

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	/**
	 * @return password を戻します。
	 */
	public String getPassword() {
		return password;
	}	
	/**
	 * @param password を設定。
	 */
	public void setPassword(String password) {
		this.password =  super.getVal(password);
	}
	
	/**
	 * @return userId を戻します。
	 */
	public String getUserId() {
		return userId;
	}	
	/**
	 * @param userId を設定。
	 */
	public void setUserId(String userId) {
		this.userId = super.getVal(userId);
	}
	
	/**
	 * @return txtKbnLang を戻します。
	 */
	public String getTxtKbnLang() {
		return txtKbnLang;
	}	
	/**
	 * @param txtKbnLang を設定。
	 */
	public void setTxtKbnLang(String txtKbnLang) {
		this.txtKbnLang = txtKbnLang;
	}
}