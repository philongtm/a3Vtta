/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.system.action;

import app.SessionData;
import app.system.bss.WorkFlowTorokuBss;
import app.system.form.WorkFlowIchiranForm;
import app.system.form.WorkFlowTorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS7105　業務フローパターンメンテナンス_登録 <br>
 */
@Controller
@RequestMapping("/system/workflowtoroku.do")
public class WorkFlowTorokuAction extends AppMenuAction {
	
	private static final String WORKFLOWICHIRANFORM = "04WorkFlowIchiranForm";
	private static final String WORKFLOWTOROKUFORM = "05WorkFlowTorokuForm";

	/**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("change", "change");
        map.put("toroku","toroku");
        map.put("update", "update");
        map.put("delete", "delete");
        map.put("back", "back");
        return map;
    }
	
    /**
     * 【画面初期表示処理(新規ボタンから遷移時)】
     */
    public Object appExecute(AppContext appContext) throws Exception {  
        
    	// sessionからActionForm取得
        WorkFlowIchiranForm inform = new WorkFlowIchiranForm();
        inform = (WorkFlowIchiranForm)appContext.getSessionActionForm(WORKFLOWICHIRANFORM);
        String gamenflg = inform.getGamen_flg();
        
        // 本画面form
        WorkFlowTorokuForm outform = new WorkFlowTorokuForm();
        outform.setGamenFlg(gamenflg);
        appContext.setActionForm(outform);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_workflow_bean();
        // ビジネスロジック実行
        WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);
        String result = bss.executeInit();
        appContext.setSessionActionForm(WORKFLOWTOROKUFORM, outform);
        return result;
    }

    /**
     * 【画面初期表示処理(リンクから遷移時)】
     */
    public Object appReExecute(AppContext appContext) throws Exception {        

    	// sessionからActionForm取得
        WorkFlowIchiranForm inform = new WorkFlowIchiranForm();
        inform = (WorkFlowIchiranForm)appContext.getSessionActionForm(WORKFLOWICHIRANFORM);
        String gamenflg = inform.getGamen_flg();
        
        // 本画面form
        WorkFlowTorokuForm outform = new WorkFlowTorokuForm();
        outform.setGamenFlg(gamenflg);
        // appContextのActionFormを上書き
        appContext.setActionForm(outform);
        // ビジネスロジック実行
        WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);            
        String result = bss.execute();
        appContext.setSessionActionForm(WORKFLOWTOROKUFORM, outform);
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
        // ビジネスロジック実行
    	WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);
    	String result = bss.doChange();
    	return result;
    }
    
    /**
     * 
     *  新規の「登録」ボタンアクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object toroku(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);
    	if(bss.doToroku()){
        	WorkFlowIchiranAction acc = new WorkFlowIchiranAction();
        	acc.appReExecute(appContext);
    		return GS.OS7104;
    	}else{
    		return GS.OS7105;
    	}

	}
    
    /**
     * 
     *  更新の「登録」ボタンアクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object update(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);
    	if(bss.doUpdate()){
        	WorkFlowIchiranAction acc = new WorkFlowIchiranAction();
        	acc.appReExecute(appContext);
    		return GS.OS7104;
    	}else{
    		return GS.OS7105;
    	}
    }
    
    /**
     * 
     * 削除アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object delete(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	WorkFlowTorokuBss bss = new WorkFlowTorokuBss(appContext);
    	if(bss.doDelete()){
        	WorkFlowIchiranAction acc = new WorkFlowIchiranAction();
        	acc.appReExecute(appContext);
    		return GS.OS7104;
    	}else{
    		return GS.OS7105;
    	}
    }
    
    /**
     * 
     * 戻るアクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object back(AppContext appContext) throws Exception {
    	WorkFlowIchiranAction acc = new WorkFlowIchiranAction();
    	acc.appReExecute(appContext);
    	return GS.OS7104;
    }
	
    /**
     * 【次のXX件→】
     */
	public Object nextY(AppContext appContext) throws Exception {

		return null;
	}

	/**
	 * 【←前のXX件】
	 */
	public Object prevX(AppContext appContext) throws Exception {

		return null;
	}

}
