/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.system.bss.KureemuBss;
import app.system.form.KureemuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS3101 クレーム債権再設定_対象先一覧 アクションクラス
 */
@Controller
@RequestMapping("/system/kureemu.do")
public class KureemuAction extends AppMenuAction {

	private static final String KUREEMUFORM = "04KureemuForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("tanto","tanto");
		map.put("sateiki","sateiki");
		map.put("sort_item","sort_item");
		map.put("sort_order","sort_order");
		map.put("show","show");
		map.put("mogitori","mogitori");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // appContextのActionFormを上書き
		KureemuForm form = new KureemuForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
        KureemuBss bss = new KureemuBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KUREEMUFORM,form);
        return result;
	}

	/**
	 * 【画面初期表示処理(メニューリンク以外から遷移時)】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	
	public Object appReExecute(AppContext appContext) throws Exception {        
		appContext.removeActionFormExcept(KUREEMUFORM);
		KureemuForm form = (KureemuForm)appContext.getSessionActionForm(KUREEMUFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
	    KureemuBss bss = new KureemuBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
	}
	
	/**
	 * 【自担当分/汎用２ラジオボタン処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object tanto(AppContext appContext) throws Exception {
	    KureemuBss bss = new KureemuBss(appContext);       	    
        String result = bss.execute();
		return result;
	}

	/**
	 * 【査定期セレクトボックス処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object sateiki(AppContext appContext) throws Exception {
	    KureemuBss bss = new KureemuBss(appContext);       	    
        String result = bss.execute();
		return result;
	}

	/**
	 * 【ソート項目セレクトボックス処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object sort_item(AppContext appContext) throws Exception {
		KureemuBss bss = new KureemuBss(appContext);       	    
        String result = bss.execute();
		return result;
	}

	/**
	 * 【整列方向セレクトボックス処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object sort_order(AppContext appContext) throws Exception {
	    KureemuBss bss = new KureemuBss(appContext);       	    
        String result = bss.execute();
		return result;
	}
	
	/**
	 * 【表示件数セレクトボックス処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		KureemuForm form = (KureemuForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS3101;
	}

	/**
	 * 【←前のXX件】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KureemuForm form = (KureemuForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS3101;
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KureemuForm form = (KureemuForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS3101;	    
	}

	/**
	 * 【勘定先CDリンク処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object mogitori(AppContext appContext) throws Exception {

		// もぎ取り処理
		KureemuBss bss = new KureemuBss(appContext);
		if(bss.doMogitori()) {
	        // クリックされた勘定先情報を機能共通セッションに格納
			KureemuForm form = (KureemuForm)appContext.getActionForm();
	        SessionData cmnData = appContext.getCMN();
	        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

		    // OS3102_クレーム債権_明細一覧に遷移
			KureemuMeisaiAction acc = new KureemuMeisaiAction();
		    acc.appExecute(appContext);
		    return GS.OS3102;
		} else {
		    return GS.OS3101;
		}
	}
}