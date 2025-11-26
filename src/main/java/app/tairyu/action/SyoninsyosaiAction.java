/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/24		SSC	            障害管理表CO002対応 滞留判定承認画面による表示不正
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.common.action.SashimodoshiAction;
import app.common.action.TairyuMeisaiAction;
import app.tairyu.bss.SyoninsyosaiBss;
import app.tairyu.form.SyoninsyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OB1105_実質滞留債権判定_承認 アクションクラス <br>
 */
public class SyoninsyosaiAction extends AppMenuAction {

	private static final String SYONINSYOSAIFORM = "01SyoninsyosaiForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("syonin","syonin");
        map.put("sashimodoshi","sashimodoshi");
		map.put("back","back");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	                
	    
		// アクションフォームを取得
		SyoninsyosaiForm form = (SyoninsyosaiForm)appContext.getSessionActionForm(SYONINSYOSAIFORM);
		if(form == null){
			form = new SyoninsyosaiForm();
			appContext.setSessionActionForm(SYONINSYOSAIFORM,form);
			//appContextのActionFormを上書き
		    appContext.setActionForm(form);
			// 共通タブ使用のため共通セッションに値セット
		    SessionData cmnData = appContext.getCMN();
	        cmnData.setTab_riyou_gamenId(GS.OB1105);
	        
	        // 共通タブ初期表示メソッド呼び出し
	        TairyuMeisaiAction acc = new TairyuMeisaiAction();
	        acc.appExecute(appContext);
		}
        
        return GS.OB1105;
	}
    
    /**
     * 【差戻ボタン処理】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object sashimodoshi(AppContext appContext) throws Exception {
        
        SessionData cmnData = appContext.getCMN();
        
        // 共)遷移元画面IDに当画面ID(OB1105)を設定する
        cmnData.setReturn_gamenId(GS.OB1105);
        
        // 差戻先選択【画面初期表示処理】
        SashimodoshiAction acc = new SashimodoshiAction();
        acc.appExecute(appContext);
        
        return GS.OZ2101;
    }

	/**
	 * 【承認実行ボタン押し処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object syonin(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        SyoninsyosaiBss bss = new SyoninsyosaiBss(appContext);
        
        String result = bss.doSyonin();        
        
        if (result.equals(GS.OB1104)) {            
            SyoninAction acc = new SyoninAction();
            // 障害管理表CO002対応
            // 追加開始
            //acc.appExecute(appContext);
            acc.appReExecute(appContext);
            // 追加完了
        }
        
        return result;
	}
	
	/**
	 * 【←前のXX件】 <br>
	 * 
	 * @param appContext
	 * @return 
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    // 未使用
	    return GS.OB1105;
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext
	 * @return 
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
        // 未使用
	    return GS.OB1105;	    
	}
	
    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
    	SyoninAction acc = new SyoninAction();
        acc.appReExecute(appContext);
        // ActionFormをsessionから削除
        appContext.removeActionForm(SYONINSYOSAIFORM);
        return GS.OB1104;
    }
	
}
