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
import app.system.bss.GolfBss;
import app.system.form.GolfForm;
import app.tairyu.action.SenteisyosaiAction;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS4101_ゴルフ会員権一覧 アクションクラス <br>
 */
public class GolfAction extends AppMenuAction {

	private static final String GOLFFORM = "04GolfForm"; // ゴルフ会員権のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("sateiki","sateiki");
		map.put("show","show");
		map.put("kanjoCdLink","kanjoCdLink");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // appContextのActionFormを上書き
		GolfForm form = new GolfForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
        GolfBss bss = new GolfBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(GOLFFORM,form);
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
		appContext.removeActionFormExcept(GOLFFORM);
	    GolfForm form = (GolfForm)appContext.getSessionActionForm(GOLFFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
        GolfBss bss = new GolfBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
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
	    GolfBss bss = new GolfBss(appContext);       	    
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
		GolfForm form = (GolfForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS4101;
	}

	/**
	 * 【勘定先CDリンク処理】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	public Object kanjoCdLink(AppContext appContext) throws Exception {

		// クリックされた勘定先情報を機能共通セッションに格納
		GolfForm form = (GolfForm) appContext.getActionForm();
		SessionData cmnData = appContext.getCMN();
		cmnData.setTori_bean((TorihikisakiBean) form.getAr_meisai().get(form.getId()));
		// 共)遷移元画面IDに当画面ID(OS4101)を設定する。
		cmnData.setReturn_gamenId(GS.OS4101);
		// OB2102_対象先選定_選定先詳細に遷移する。
		SenteisyosaiAction acc = new SenteisyosaiAction();
		acc.appExecute(appContext);
		return GS.OB2102;
	}

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		GolfForm form = (GolfForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS4101;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		GolfForm form = (GolfForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS4101;	    
    }
}