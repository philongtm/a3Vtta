/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.system.action;

import app.SessionData;
import app.WorkFlowBean;
import app.system.bss.WorkFlowIchiranBss;
import app.system.form.WorkFlowIchiranForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 *  OS7104　業務フローパターンメンテナンス_一覧 アクションクラス <br>
 */
public class WorkFlowIchiranAction extends AppMenuAction {
	
	private static final String WORKFLOWICHIRANFORM = "04WorkFlowIchiranForm";

	/**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("detail","detail");
        map.put("kensaku","kensaku");
        map.put("show","show");
        map.put("change", "change");
        return map;
    }
	
    /**
     * 【画面初期表示処理(メニューリンクから遷移時)】
     */
    public Object appExecute(AppContext appContext) throws Exception {  
        
        // appContextのActionFormを上書き
    	WorkFlowIchiranForm form = new WorkFlowIchiranForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        WorkFlowIchiranBss bss = new WorkFlowIchiranBss(appContext);
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(WORKFLOWICHIRANFORM, form);
        return result;
    }

    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】
     */
    public Object appReExecute(AppContext appContext) throws Exception {  
    	
        appContext.removeActionFormExcept(WORKFLOWICHIRANFORM);
        // sessionからActionForm取得
        WorkFlowIchiranForm form = (WorkFlowIchiranForm)appContext.getSessionActionForm(WORKFLOWICHIRANFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        WorkFlowIchiranBss bss = new WorkFlowIchiranBss(appContext);            
        String result = bss.execute();

        return result;
    }
    
    /**
     * 
     * P02_区分からシステムに紐づく分類１を取得する <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object change(AppContext appContext) throws Exception {
    	WorkFlowIchiranBss bss = new WorkFlowIchiranBss(appContext);
    	bss.doChange();
    	return GS.OS7104;
    }
    
    /**
     * 
     * 【検索一覧情報取得】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object kensaku(AppContext appContext) throws Exception {
    	WorkFlowIchiranBss bss = new WorkFlowIchiranBss(appContext);
        String result = bss.doSearch();
        return result;
    }
    
    /**
     * 
     * 【明細情報取得】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object detail(AppContext appContext) throws Exception {
    	WorkFlowIchiranForm form = (WorkFlowIchiranForm)appContext.getActionForm();
    	SessionData cmnData = appContext.getCMN();
    	// OS7105_業務フローパターンメンテナンス_登録に遷移
    	WorkFlowTorokuAction acc = new WorkFlowTorokuAction();
    	// ボタン遷移とリンク遷移
    	if("1".equals(form.getGamen_flg())){
    		acc.appExecute(appContext);
    	}else if("2".equals(form.getGamen_flg())){
    		cmnData.setWorkflow_bean((WorkFlowBean)form.getAr_meisai().get(form.getId()));
    		acc.appReExecute(appContext);
    	}
	    
	    return GS.OS7105;
    }
    
    /**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		WorkFlowIchiranForm form = (WorkFlowIchiranForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS7104;
	}
    
    /**
     * 【←前のXX件】
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
    	WorkFlowIchiranForm form = (WorkFlowIchiranForm)appContext.getActionForm();
        form.setPrevList();
        return GS.OS7104;
    }
    
    /**
     * 【次のXX件→】
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
    	WorkFlowIchiranForm form = (WorkFlowIchiranForm)appContext.getActionForm();
        form.setNextList();
        return GS.OS7104;       
    }

}
