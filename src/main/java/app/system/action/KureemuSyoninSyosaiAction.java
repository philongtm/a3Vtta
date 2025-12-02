/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/24		SSC				課題No.162 クレーム債権再設定承認 表示不正
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.common.action.SaikenMeisaiSyokaiAction;
import app.common.action.SashimodoshiAction;
import app.common.action.TenpuSyokaiAction;
import app.system.bss.KureemuSyoninSyosaiBss;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS3105_クレーム債権再設定_承認 アクションクラス <br>
 */
@Controller
@RequestMapping("/system/kureemusyoninSyosai.do")
public class KureemuSyoninSyosaiAction extends AppMenuAction {
	
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
		map.put("tenpu","tenpu");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	                
	    
        // 共通タブ使用のため共通セッションに値セット
	    SessionData cmnData = appContext.getCMN();
        cmnData.setReturn_gamenId(GS.OS3105);
        cmnData.setTab_riyou_gamenId(GS.OS3105);
        
        // 共通タブ初期表示メソッド呼び出し
        SaikenMeisaiSyokaiAction acc = new SaikenMeisaiSyokaiAction();
        acc.appExecute(appContext);
        
        return GS.OS3105;
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
        
        // 共)遷移元画面IDに当画面ID(OS3105)を設定する
        cmnData.setReturn_gamenId(GS.OS3105);
        
        // 差戻先選択【画面初期表示処理】
        SashimodoshiAction acc = new SashimodoshiAction();
        acc.appExecute(appContext);
        
        return GS.OZ2101;
    }
    
    /**
     * 【添付参照ボタン処理】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tenpu(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        //SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OS3105)を設定する。
        //cmnData.setReturn_gamenId(GS.OS3105);
        
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
        //OZ1102_添付内容照会に遷移
        TenpuSyokaiAction acc = new TenpuSyokaiAction();
        acc.appExecute(appContext);
	    return GS.OZ1102;

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
        KureemuSyoninSyosaiBss bss = new KureemuSyoninSyosaiBss(appContext);
        
        String result = bss.doSyonin();        
        
        if (result.equals(GS.OS3104)) {            
        	KureemuSyoninAction acc = new KureemuSyoninAction();
        	// 課題No.162
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
	    return GS.OS3105;
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
	    return GS.OS3105;	    
	}
	
    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
    	KureemuSyoninAction acc = new KureemuSyoninAction();
        acc.appReExecute(appContext);
        return GS.OS3104;
    }
	
}
