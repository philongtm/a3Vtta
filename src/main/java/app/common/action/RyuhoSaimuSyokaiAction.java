/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.common.bss.RyuhoSaimuSyokaiBss;
import app.common.form.RyuhoSaimuSyokaiForm;
import common.AppContext;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OZ6107_債務明細照会タブ  アクションフォームクラス
 */
public class RyuhoSaimuSyokaiAction extends AppMenuAction {

	private static final String RYUHOSAIMUSYOKAIFORM = "RyuhoSaimuSyokaiForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	
	public Object appExecute(AppContext appContext) throws Exception {     

		//アクションフォーム取得
		RyuhoSaimuSyokaiForm form = (RyuhoSaimuSyokaiForm)appContext.getSessionActionForm(RYUHOSAIMUSYOKAIFORM);
        SessionData cmnData = appContext.getCMN();
		String result = cmnData.getTab_riyou_gamenId();
		if(form == null){
			form = new RyuhoSaimuSyokaiForm();
			appContext.setSessionActionForm(RYUHOSAIMUSYOKAIFORM,form);
			// appContextのActionFormを上書き
	        appContext.setActionForm(form);
	        // ビジネスロジック実行
	        RyuhoSaimuSyokaiBss bss = new RyuhoSaimuSyokaiBss(appContext);
	        bss.execute();
		}
		//ページ設定
		form.setPager(form.getId() + 1);
        //共通タブ利用画面に遷移
        return result;
	}

	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		RyuhoSaimuSyokaiForm form = (RyuhoSaimuSyokaiForm)appContext.getActionForm();
		form.setPager();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		RyuhoSaimuSyokaiForm form = (RyuhoSaimuSyokaiForm)appContext.getActionForm();
		form.setPrevList();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		RyuhoSaimuSyokaiForm form = (RyuhoSaimuSyokaiForm)appContext.getActionForm();
		form.setNextList();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}
}