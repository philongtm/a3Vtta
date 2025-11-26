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
import app.tairyu.bss.SyoninBss;
import app.tairyu.form.SyoninForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OB1104_実質滞留債権判定_承認一覧 アクションクラス <br>
 */
public class SyoninAction extends AppMenuAction {

	private static final String SYONINFORM = "01SyoninForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
		map.put("syonin","syonin");
        map.put("ikatu_syonin","ikatu_syonin");
        map.put("link_meisai","link_meisai");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		SyoninForm form = new SyoninForm();
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   
        
	    // ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SYONINFORM, form);
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
        appContext.removeActionFormExcept(SYONINFORM);
        // sessionからActionForm取得
        SyoninForm form = (SyoninForm)appContext.getSessionActionForm(SYONINFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);            
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
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setPager();
	    return GS.OB1104;
	}
	
	/**
	 * 【承認実行ボタン押し処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object syonin(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);
        
        // アクションファームを取得
        SyoninForm form = (SyoninForm)appContext.getActionForm();
        String result = bss.doSyonin(form);
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SYONINFORM, form);
        
        return result;
	}
	
	/**
	 * 【一括承認チッェクボックス実行処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object ikatu_syonin(AppContext appContext) throws Exception {

        // ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);  
        
        // アクションファームを取得
        SyoninForm form = (SyoninForm)appContext.getActionForm(); 
        String result = bss.doIkatuSyonin(form);
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SYONINFORM, form);
        
        return result;
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
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OB1104;
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
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OB1104;	    
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
        SyoninForm form = (SyoninForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

        // OB1105_実質滞留債権判定_承認に遷移
        SyoninsyosaiAction acc = new SyoninsyosaiAction();
        acc.appExecute(appContext);
        return GS.OB1105;
    }
}
