/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2016/03/29		SSC				新規作成
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.UserMaintenanceBean;
import app.system.bss.TantoSoshikiSentakuBss;
import app.system.form.TantoSoshikiSentakuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS7116_担当組織選択 アクションクラス <br>
 */
public class TantoSoshikiSentakuAction extends AppMenuAction  {

	private static final String TANTOSOSHIKIFORM = "07TantoSoshikiSentakuForm";
	private SessionData cmnData = null;		// 機能共通セッション
	
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
		TantoSoshikiSentakuForm form = new TantoSoshikiSentakuForm();
		appContext.setActionForm(form);
		// ビジネスロジック実行
		TantoSoshikiSentakuBss bss = new TantoSoshikiSentakuBss(appContext);
		String result = bss.executeInit();
		// sessionスコープにActionFormを登録
		appContext.setSessionActionForm(TANTOSOSHIKIFORM, form);
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
		TantoSoshikiSentakuForm form = (TantoSoshikiSentakuForm)appContext.getSessionActionForm(TANTOSOSHIKIFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		TantoSoshikiSentakuBss bss = new TantoSoshikiSentakuBss(appContext);
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
		TantoSoshikiSentakuForm form = (TantoSoshikiSentakuForm)appContext.getSessionActionForm(TANTOSOSHIKIFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		TantoSoshikiSentakuBss bss = new TantoSoshikiSentakuBss(appContext);
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
		TantoSoshikiSentakuForm form = (TantoSoshikiSentakuForm)appContext.getSessionActionForm(TANTOSOSHIKIFORM);
		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		TantoSoshikiSentakuBss bss = new TantoSoshikiSentakuBss(appContext);
		String result = bss.doCloose();

		cmnData = appContext.getCMN();
		UserMaintenanceBean umbean = cmnData.getUser_maintenance_bean();
		umbean.setTantoSoshikiList(form.getAr_sentakuHaneiList());
		cmnData.setUser_maintenance_bean(umbean);
		
		return result;
	}

	/**
	 * 閉じるアクション
	 */
	public Object close(AppContext appContext) throws Exception {
		appContext.removeActionForm(TANTOSOSHIKIFORM);

		// 共通セッションを空にする
		cmnData = appContext.getCMN();

		UserMaintenanceBean umbean = cmnData.getUser_maintenance_bean();
		umbean.setTantoSoshikiList(null);	// 担当組織【リスト】
		umbean.setKen_System_Kbn(null);		// システム区分（検索条件）
		umbean.setKen_Hanyou1(null);		// 汎用1（検索条件）
		umbean.setKen_Hanyou2(null);		// 汎用2（検索条件）

		cmnData.setUser_maintenance_bean(umbean);

		return GS.OS7116;
	}

	@Override
	public Object prevX(AppContext appContext) throws Exception {
		return null;
	}

	@Override
	public Object nextY(AppContext appContext) throws Exception {
		return null;
	}

}
