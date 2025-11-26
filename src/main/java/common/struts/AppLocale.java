/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2016/12/16		SSC				BJ201612070 SSO対応
******************************************************************************/
package common.struts;

import common.global.GS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @author Administrator
 *
 * TODO この生成された型コメントのテンプレートを変更するには次へジャンプ:
 * ウィンドウ - 設定 - Java - コード・スタイル - コード・テンプレート
 */
public class AppLocale {

	public static void setDefault(HttpServletRequest request){

		boolean ja;
		HttpSession session = request.getSession(false);
		if( session.getAttribute(GS.LANG) == null || session.getAttribute(GS.LANG).equals(GS.EMPTY_CHARCTER)) {
			String langStr = request.getHeader(GS.REQUEST_HEADER_SSO_LANG);
			if ( (langStr != null) && (langStr.startsWith(GS.LOCALE_JA)) ) {
				session.setAttribute(GS.LANG,GS.LANG_JA);
				ja = true;
			} else {
				session.setAttribute(GS.LANG,GS.LANG_EN);
				ja = false;
			}	
		} else {
			ja = session.getAttribute(GS.LANG).equals(GS.LANG_JA);
		}

		ResourceBundle rb;
		session.removeAttribute(GS.RB);
		if( ja ) {
			rb = ResourceBundle.getBundle(GS.PROPERTY_ERRORMSG,Locale.JAPAN);
		} else {
			rb = ResourceBundle.getBundle(GS.PROPERTY_ERRORMSG,Locale.ENGLISH);
		}
		session.setAttribute(GS.RB,rb);
	}

	public static void setJa(HttpSession session) {
		session.setAttribute(GS.LANG,GS.LANG_JA);
		session.removeAttribute(GS.RB);
		session.setAttribute(GS.RB,ResourceBundle.getBundle(GS.PROPERTY_ERRORMSG,Locale.JAPAN));
	}
	
	public static void setEn(HttpSession session) {
		session.setAttribute(GS.LANG,GS.LANG_EN);
		session.removeAttribute(GS.RB);
		session.setAttribute(GS.RB,ResourceBundle.getBundle(GS.PROPERTY_ERRORMSG,Locale.ENGLISH));
	}

	public static ResourceBundle getResourceBundle(HttpSession session) {
		ResourceBundle rb = null;
		rb = (ResourceBundle)session.getAttribute(GS.RB);
		return rb;
	}
}