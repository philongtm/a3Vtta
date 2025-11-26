/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.system.bss.KureemuSyosaiBss;
import app.system.form.KureemuSyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS3103_クレーム債権_明細詳細 アクションクラス <br>
 */
public class KureemuSyosaiAction extends AppMenuAction {

	private static final String KUREEMSYOSAIFORM = "04KureemSyosai";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("save","save");		
		map.put("back","back");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {
        // appContextのActionFormを上書き
		KureemuSyosaiForm form = new KureemuSyosaiForm();
        // 明細情報をセットする。
        form.setSyosai_bean(appContext.getCMN().getSyosai_bean());
        appContext.setActionForm(form);
        // ビジネスロジック実行
        KureemuSyosaiBss bss = new KureemuSyosaiBss(appContext); 
        bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KUREEMSYOSAIFORM,form);
        return GS.OS3103;
	}
    
    /**
     * 【保存アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object save(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        KureemuSyosaiBss bss = new KureemuSyosaiBss(appContext); 
        boolean result = bss.save();
        if (result) {
            // OS3102_クレーム債権_明細一覧に遷移
            KureemuMeisaiAction acc = new KureemuMeisaiAction();
            acc.appReExecute(appContext);
            return GS.OS3102;
        }
        return GS.OS3103;
    }

    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
        // OS3102_クレーム債権_明細一覧に遷移
        KureemuMeisaiAction acc = new KureemuMeisaiAction();
        acc.appReExecute(appContext);
        return GS.OS3102;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        return GS.OS3103;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        return GS.OS3103;       
    }
}