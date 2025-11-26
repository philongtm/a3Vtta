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
import app.system.bss.KanjyoTorokuBss;
import app.system.form.KanjyoForm;
import app.system.form.KanjyoTorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS7109 勘定科目マスタメンテナンス_登録 アクションクラス <br>
 */
public class KanjyoTorokuAction extends AppMenuAction {

	private static final String KANJYOTOROKUFORM 	= "09KanjyoTorokuForm";
	private static final String KANJYOFORM 		= "08KanjyoForm";
	private static final String INSERT 			= "insert";
	private static final String BACK 				= "back";
	
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
		map.put("insert","insert");
		map.put("back","back");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		KanjyoTorokuForm form = new KanjyoTorokuForm();
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   
        
	    // ビジネスロジック実行
        KanjyoTorokuBss bss = new KanjyoTorokuBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KANJYOTOROKUFORM, form);
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
        appContext.removeActionFormExcept(KANJYOTOROKUFORM);
        // sessionからActionForm取得
        KanjyoTorokuForm form = (KanjyoTorokuForm)appContext.getSessionActionForm(KANJYOTOROKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        KanjyoBss bss = new KanjyoBss(appContext);            
        String result = bss.execute();
        form.setPager(form.getId() + 1);
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
		// 未使用
	    return GS.OS7109;
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// // 未使用
	    return GS.OS7109;	    
	}
	
	/**
	 * 【システムセレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object system(AppContext appContext) throws Exception {
		KanjyoTorokuBss bss = new KanjyoTorokuBss(appContext);       	    
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
		KanjyoTorokuBss bss = new KanjyoTorokuBss(appContext);       	    
        String result = bss.hanyo1();
		return result;
	}
	
	/**
	 * 【登録処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object insert(AppContext appContext) throws Exception {
		KanjyoTorokuBss bss = new KanjyoTorokuBss(appContext);       	    
        String result = bss.doInsert();
        if (GS.OS7108.equals(result)) {            
        	KanjyoAction acc = new KanjyoAction();
            KanjyoForm form = (KanjyoForm)appContext.getSessionActionForm(KANJYOFORM);
            form.setAction_flg(INSERT);
            appContext.setSessionActionForm(KANJYOFORM, form);
            acc.appReExecute(appContext);
        }
		return result;
	}
	
    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
    	KanjyoAction acc = new KanjyoAction();
        KanjyoForm form = (KanjyoForm)appContext.getSessionActionForm(KANJYOFORM);
        form.setAction_flg(BACK);
        appContext.setSessionActionForm(KANJYOFORM, form);
        acc.appReExecute(appContext);
        // ActionFormをsessionから削除
        appContext.removeActionForm(KANJYOTOROKUFORM);
        return GS.OS7108;
    }
}
