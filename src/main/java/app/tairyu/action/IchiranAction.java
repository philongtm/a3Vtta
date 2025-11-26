/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.tairyu.bss.IchiranBss;
import app.tairyu.form.IchiranForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OB1101_実質滞留債権判定_対象先一覧 アクションクラス <br>
 */
public class IchiranAction extends AppMenuAction {

	private static final String ICHIRANFORM = "01IchiranForm";
	
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
	    IchiranForm form = new IchiranForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
	    IchiranBss bss = new IchiranBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(ICHIRANFORM,form);
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
		appContext.removeActionFormExcept(ICHIRANFORM);
	    IchiranForm form = (IchiranForm)appContext.getSessionActionForm(ICHIRANFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
	    IchiranBss bss = new IchiranBss(appContext);   	    
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
	    IchiranBss bss = new IchiranBss(appContext);       	    
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
	    IchiranBss bss = new IchiranBss(appContext);       	    
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
		IchiranBss bss = new IchiranBss(appContext);       	    
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
	    IchiranBss bss = new IchiranBss(appContext);       	    
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
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setPager();
	    return GS.OB1101;
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
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OB1101;
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
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OB1101;	    
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
		IchiranBss bss = new IchiranBss(appContext);
		if(bss.doMogitori()) {
	        // クリックされた勘定先情報を機能共通セッションに格納
			IchiranForm form = (IchiranForm)appContext.getActionForm();
	        SessionData cmnData = appContext.getCMN();
	        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

		    //OB1102_実質滞留債権判定_明細一覧に遷移
			TorokuAction acc = new TorokuAction();
		    acc.appExecute(appContext);
		    return GS.OB1102;
		} else {
		    return GS.OB1101;
		}
	}
}