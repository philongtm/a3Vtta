/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.system.bss.KanjyoBss;
import app.system.form.KanjyoForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS7108_勘定科目マスタメンテナンス_一覧・登録 アクションクラス <br>
 */
@Controller
@RequestMapping("/system/kanjyo.do")
public class KanjyoAction extends AppMenuAction {

	private static final String KANJYOFORM = "08KanjyoForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
		map.put("system","system");
		map.put("hanyo1","hanyo1");
		map.put("search","search");
		map.put("update","update");
		map.put("sinki","sinki");	
		map.put("download","download");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		KanjyoForm form = new KanjyoForm();
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   
        
	    // ビジネスロジック実行
        KanjyoBss bss = new KanjyoBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KANJYOFORM, form);
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
        appContext.removeActionFormExcept(KANJYOFORM);
        // sessionからActionForm取得
        KanjyoForm form = (KanjyoForm)appContext.getSessionActionForm(KANJYOFORM);

        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        KanjyoBss bss = new KanjyoBss(appContext);            
        String result = bss.execute();
        //form.setPager(form.getId() + 1);
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
		KanjyoForm form = (KanjyoForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS7108;
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
		KanjyoForm form = (KanjyoForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS7108;
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
		KanjyoForm form = (KanjyoForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS7108;	    
	}
	
	/**
	 * 【システムセレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object system(AppContext appContext) throws Exception {
		KanjyoBss bss = new KanjyoBss(appContext);       	    
        String result = bss.system();
		return result;
	}

	/**
	 * 【汎用１セレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object hanyo1(AppContext appContext) throws Exception {
		KanjyoBss bss = new KanjyoBss(appContext);       	    
        String result = bss.hanyo1();
		return result;
	}
	
	/**
	 * 【検索処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object search(AppContext appContext) throws Exception {
		KanjyoBss bss = new KanjyoBss(appContext);		
        String result = bss.doSearch();
		return result;
	}
	
	/**
	 * 【更新処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object update(AppContext appContext) throws Exception {
		KanjyoBss bss = new KanjyoBss(appContext);
        String result = bss.doUpdate();
		return result;
	}
	
	/**
	 * 【新規作成】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object sinki(AppContext appContext) throws Exception {
		KanjyoTorokuAction acc = new KanjyoTorokuAction();  
		KanjyoForm form = (KanjyoForm)appContext.getActionForm();
		appContext.setSessionActionForm(KANJYOFORM, form);
		acc.appExecute(appContext);
		return GS.OS7109;
	}
	
	/**
	 * 【ダウンロード】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object download(AppContext appContext) throws Exception {
		
		KanjyoBss bss = new KanjyoBss(appContext);
		bss.download(appContext);
		return null;
	}
}
