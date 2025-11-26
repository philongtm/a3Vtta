/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.UserMaintenanceBean;
import app.system.bss.UserIchiranBss;
import app.system.form.UserIchiranForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS7106_ユーザマスタメンテナンス_一覧 アクションクラス <br>
 */
public class UserIchiranAction extends AppMenuAction {

	private static final String USERICHIRANFORM = "06UserIchiranForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
		map.put("hanyo1","hanyo1");
		map.put("search","search");
		map.put("link_toroku","link_toroku");	
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		UserIchiranForm form = new UserIchiranForm();
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();           
        
	    // ビジネスロジック実行
        UserIchiranBss bss = new UserIchiranBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(USERICHIRANFORM, form);
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
        appContext.removeActionFormExcept(USERICHIRANFORM);
        // sessionからActionForm取得
        UserIchiranForm form = (UserIchiranForm)appContext.getSessionActionForm(USERICHIRANFORM);

        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        UserIchiranBss bss = new UserIchiranBss(appContext);            
        String result = bss.execute();
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
		UserIchiranForm form = (UserIchiranForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS7106;
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
		UserIchiranForm form = (UserIchiranForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS7106;
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
		UserIchiranForm form = (UserIchiranForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS7106;	    
	}

	/**
	 * 【汎用１セレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object hanyo1(AppContext appContext) throws Exception {
		UserIchiranBss bss = new UserIchiranBss(appContext);       	    
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
		UserIchiranBss bss = new UserIchiranBss(appContext);		
        String result = bss.doSearch();
		return result;
	}
	
	/**
	 * 【新規作成】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object link_toroku(AppContext appContext) throws Exception {
		
        // クリックされた勘定先情報を機能共通セッションに格納
		UserIchiranForm form = (UserIchiranForm)appContext.getActionForm();   
        
        // リンククリックされたユーザIDのユーザメンテ情報Beanを機能共通セッションに格納する
        UserMaintenanceBean user_main_bean = (UserMaintenanceBean)form.getAr_meisai().get(form.getId());
        
        // 対象のユーザに設定されている業務フローパターンを取得する
		UserIchiranBss bss = new UserIchiranBss(appContext);
        bss.gyoumuhuro_patan_list(user_main_bean);
        
        UserTorokuAction acc = new UserTorokuAction();
		acc.appExecute(appContext);
		return GS.OS7107;
	}
}
