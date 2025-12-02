/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/03/23		SSC				新規作成
******************************************************************************/

package app.system.action;

import app.SessionData;
import app.UserMaintenanceBean;
import app.system.bss.MailSoushinSentakuBss;
import app.system.form.MailSoushinSentakuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS7115_メール送信先選択 アクションクラス <br>
 */
@Controller
@RequestMapping("/system/mailsoushin_sentaku.do")
public class MailSoushinSentakuAction extends AppMenuAction {

	private static final String MAILSOUSHINFORM = "07MailSoushinSentakuForm";
	private SessionData cmnData = null;			// 機能共通セッション

	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
		// ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("appExecute","appExecute");
		map.put("search","search");
		map.put("change1","change1");
		map.put("reflection", "reflection");
		map.put("close", "close");
		return map;
	}

	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {

		// appContextのActionFormを上書き
		MailSoushinSentakuForm form = new MailSoushinSentakuForm();
		appContext.setActionForm(form);
		// ビジネスロジック実行
		MailSoushinSentakuBss bss = new MailSoushinSentakuBss(appContext);
		String result = bss.executeInit();
		// sessionスコープにActionFormを登録
		appContext.setSessionActionForm(MAILSOUSHINFORM, form);
		return result;
	}

	/**
	 * 【画面初期表示処理(メニューリンク以外から遷移時)】
	 */
	public Object appReExecute(AppContext appContext) throws Exception {

		appContext.removeActionFormExcept(MAILSOUSHINFORM);
		// sessionからActionForm取得
		MailSoushinSentakuForm form = (MailSoushinSentakuForm)appContext.getSessionActionForm(MAILSOUSHINFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		MailSoushinSentakuBss bss = new MailSoushinSentakuBss(appContext);
		String result = bss.execute();

		return result;
	}

	/**
	 *
	 * 汎用1アクション <br>
	 *
	 * @param appContext
	 * @return forward
	 * @throws Exception
	 */
	public Object change1(AppContext appContext) throws Exception {
		MailSoushinSentakuForm form = (MailSoushinSentakuForm)appContext.getSessionActionForm(MAILSOUSHINFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		MailSoushinSentakuBss bss = new MailSoushinSentakuBss(appContext);
		String result = bss.doChange1();

		return result;
	}

	/**
	 *
	 * 検索アクション <br>
	 *
	 * @param appContext
	 * @return forward
	 * @throws Exception
	 */
	public Object search(AppContext appContext) throws Exception {
		MailSoushinSentakuForm form = (MailSoushinSentakuForm)appContext.getSessionActionForm(MAILSOUSHINFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		MailSoushinSentakuBss bss = new MailSoushinSentakuBss(appContext);
		String result = bss.doSearch();

		return result;
	}

	/**
	 * 選択反映アクション
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object reflection(AppContext appContext) throws Exception {
		MailSoushinSentakuForm form = (MailSoushinSentakuForm)appContext.getSessionActionForm(MAILSOUSHINFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		MailSoushinSentakuBss bss = new MailSoushinSentakuBss(appContext);
		String result = bss.doCloose();

		cmnData = appContext.getCMN();
		UserMaintenanceBean umbean = cmnData.getUser_maintenance_bean();
		umbean.setSoushinList(form.getSoushin_meisai());
		cmnData.setUser_maintenance_bean(umbean);
		
		return result;
	}

	/**
	 * 閉じるアクション
	 */
	public Object close(AppContext appContext) throws Exception {
		appContext.removeActionForm(MAILSOUSHINFORM);
		cmnData = appContext.getCMN();
		UserMaintenanceBean umbean = cmnData.getUser_maintenance_bean();
		umbean.setSoushinList(null);
		cmnData.setUser_maintenance_bean(umbean);

		return GS.OS7115;
	}


	/**
	 * 【次のXX件→】
	 */
	public Object nextY(AppContext appContext) throws Exception {
		return null;
	}

	/**
	 * 【←前のXX件】
	 */
	public Object prevX(AppContext appContext) throws Exception {
		return null;
	}

}
