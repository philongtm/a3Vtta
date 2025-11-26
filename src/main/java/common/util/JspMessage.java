/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import common.global.GS;

import javax.servlet.http.HttpSession;
import java.util.ResourceBundle;

/**
 * メッセージ取得クラス<br>
 * 「MessageResources_XX.properties」プロパティファイルからメッセージを取得します。
 * 
 */
public class JspMessage {

	private ResourceBundle rb = null;

	private boolean ja;
	private boolean en;
	
	/**
	 * コンストラクタ
	 * 2008/5/19新実が修正。要チェック。
	 */
	public JspMessage(HttpSession session) {
		rb = (ResourceBundle)session.getAttribute(GS.RB);
		if(session.getAttribute(GS.LANG).equals(GS.LANG_JA)){
		    ja = true;
		}else{
		    en = true;
		}
	}
	
	/**
	 * 指定されるエラーコードに対応するエラーメッセージを取得する。
	 * 
	 * @param code
	 *			エラーコード
	 * @return エラーメッセージ
	 */
	public String get(String code) {
		String msg = null;
		try {
			msg = rb.getString(code);
		} catch(Exception e){}
		
		if( msg == null ) {
			msg = code;
		}
		return msg;
	}

	/**
	 * 英語モードの判定
	 * 
	 * @return true/英語 false/英語以外
	 */
	public boolean isEn() {
		return en;
	}
	/**
	 * 日本語モードの判定
	 * 
	 * @return true/日本語 false/日本語以外
	 */
	public boolean isJa() {
		return ja;
	}
}