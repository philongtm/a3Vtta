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
import app.system.bss.KureemuSyoninBss;
import app.system.form.KureemuSyoninForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS3104_クレーム債権再設定_承認一覧 アクションクラス <br>
 */
public class KureemuSyoninAction extends AppMenuAction {

	private static final String KUREEMUSYONINFORM = "04KureemuSyoninForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
        map.put("link_meisai","link_meisai");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		KureemuSyoninForm form = new KureemuSyoninForm();
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   
        
	    // ビジネスロジック実行
        KureemuSyoninBss bss = new KureemuSyoninBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KUREEMUSYONINFORM, form);
        return result;
	}
       
    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception {        
        appContext.removeActionFormExcept(KUREEMUSYONINFORM);
        // sessionからActionForm取得
        KureemuSyoninForm form = (KureemuSyoninForm)appContext.getSessionActionForm(KUREEMUSYONINFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        KureemuSyoninBss bss = new KureemuSyoninBss(appContext);            
        String result = bss.execute();
        form.setPager(form.getId() + 1);
        return result;
    }
	
	/**
	 * 【表示件数セレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		KureemuSyoninForm form = (KureemuSyoninForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS3104;
	}
	
	/**
	 * 【←前のXX件】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KureemuSyoninForm form = (KureemuSyoninForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS3104;
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KureemuSyoninForm form = (KureemuSyoninForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS3104;	    
	}
    
    /**
     * 【勘定先CDリンク処理】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object link_meisai(AppContext appContext) throws Exception {

        // クリックされた勘定先情報を機能共通セッションに格納
        KureemuSyoninForm form = (KureemuSyoninForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

        // OS3105_クレーム債権再設定_承認に遷移
        KureemuSyoninSyosaiAction acc = new KureemuSyoninSyosaiAction();
        acc.appExecute(appContext);
        return GS.OS3105;
    }
}
