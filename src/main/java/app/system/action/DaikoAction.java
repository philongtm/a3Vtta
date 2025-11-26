/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.system.bss.DaikoBss;
import app.system.form.DaikoForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS7101_代行設定 アクションクラス <br>
 */
public class DaikoAction extends AppMenuAction {

	private static final String DAIKOFORM = "04DaikoForm"; // 代行設定のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("hidaikoSentaku", "hidaikoSentaku");
		map.put("daikoSetei", "daikoSetei");		
		map.put("delete", "delete");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
        // appContextのActionFormを上書き
        DaikoForm form = new DaikoForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        DaikoBss bss = new DaikoBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(DAIKOFORM,form);
        return result;
	}

    /**
     * 【被代行者選択.＞アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object hidaikoSentaku(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        DaikoBss bss = new DaikoBss(appContext); 
        bss.doHidaikoSentaku();
        // 遷移先を指定する。
        return GS.OS7101;
    }

    /**
     * 【代行設定アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object daikoSetei(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        DaikoBss bss = new DaikoBss(appContext); 
        bss.doDaikoSetei();
        // 遷移先を指定する。
        return GS.OS7101;
    }

    /**
     * 【削除アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object delete(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        DaikoBss bss = new DaikoBss(appContext); 
        bss.doDelete();
        // 遷移先を指定する。
        return GS.OS7101;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        return null;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        return null;       
    }
}