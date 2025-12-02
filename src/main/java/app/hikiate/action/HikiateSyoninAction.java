/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/24		SSC				課題No.161 引当金確認_承認 表示不正
******************************************************************************/

package app.hikiate.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.hikiate.bss.HikiateSyoninBss;
import app.hikiate.form.HikiateSyoninForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 *  OD1103_引当金確認_承認一覧 アクションクラス <br>
 */
@Controller
@RequestMapping("/hikiate/syonin_ichiran.do")
public class HikiateSyoninAction extends AppMenuAction {

	private static final String HIKIATESYONINFORM = "06HikiateSyoninForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("show","show");
        map.put("zikko","zikko");
        map.put("detailPage","detailPage");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
        // appContextのActionFormを上書き
        HikiateSyoninForm form = new HikiateSyoninForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        HikiateSyoninBss bss = new HikiateSyoninBss(appContext);            
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(HIKIATESYONINFORM, form);
        return result;
	}

    /**
     * 【画面初期表示処理(対象先一覧以外から遷移時)】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception {
    	
    	// 課題No.161
    	// 追加開始
    	appContext.removeActionFormExcept(HIKIATESYONINFORM);
    	HikiateSyoninForm form = (HikiateSyoninForm)appContext.getSessionActionForm(HIKIATESYONINFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        
        /*
        // appContextのActionFormを上書き
        HikiateSyoninForm form = new HikiateSyoninForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行        
        HikiateSyoninBss bss = new HikiateSyoninBss(appContext); 
        String result = bss.executeInit();  
        */
        // ビジネスロジック実行        
        HikiateSyoninBss bss = new HikiateSyoninBss(appContext); 
        String result = bss.execute();
        // 追加完了
        
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
    }
	
    /**
     * 
     * 承認実行アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object zikko(AppContext appContext) throws Exception {   
        // ビジネスロジック実行
        HikiateSyoninBss bss = new HikiateSyoninBss(appContext); 
        bss.doZikko();
        appExecute(appContext);
        return GS.OD1103;
    }
    
    /**
     * 
     *  勘定先CDリンクアクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object detailPage(AppContext appContext) throws Exception {   
    	
    	// クリックされた勘定先情報を機能共通セッションに格納
    	HikiateSyoninForm form = (HikiateSyoninForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

        // OD1104_引当金確認_承認に遷移
        HikiateSyoninSyosaiAction acc = new HikiateSyoninSyosaiAction();
        acc.appExecute(appContext);
    	// OD1104_引当金確認_承認へ遷移する
    	return GS.OD1104;
    }
    
    /**
     * 【表示件数セレクトボックス処理】
     */
    public Object show(AppContext appContext) throws Exception {
        // 表示件数の変更をPagerオブジェクトに設定
    	HikiateSyoninForm form = (HikiateSyoninForm)appContext.getActionForm();
        form.setPager();
        return GS.OD1103;
    }
    
    /**
     * 【←前のXX件】
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
    	HikiateSyoninForm form = (HikiateSyoninForm)appContext.getActionForm();
        form.setPrevList();
        return GS.OD1103;
    }
    
    /**
     * 【次のXX件→】
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
    	HikiateSyoninForm form = (HikiateSyoninForm)appContext.getActionForm();
        form.setNextList();
        return GS.OD1103;       
    }

}
