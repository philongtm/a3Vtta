/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/04		SSC				課題No.21 ユーザマスタ 参照組織一覧の追加・削除制御対応
003		2010/01/08		SSC				課題No.230 ログインユーザでログインユーザを更新時対応
004		2015/03/23		SSC				BJ201408049 IA化対応時の機能改善
005		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
006		2016/12/21		SSC				BJ201612070 SSO対応
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.system.bss.UserIchiranBss;
import app.system.bss.UserTorokuBss;
import app.system.form.UserIchiranForm;
import app.system.form.UserTorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS7107　ユーザマスタメンテナンス_登録 アクションクラス <br>
 */
public class UserTorokuAction extends AppMenuAction {

	private static final String USERTOROKUFORM 	= "07UserTorokuForm";
	private static final String USERICHIRANFORM 	= "06UserIchiranForm";
	private static final String INSERT 			= "insert";
	private static final String BACK 				= "back";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("tuika","tuika");
		map.put("toroku","toroku");
		map.put("delete","delete");
		map.put("search","search");
		map.put("admin_kanri","admin_kanri");
		map.put("back","back");	
		map.put("toukatu","toukatu");
		map.put("meilSentaku", "meilSentaku");
		map.put("reMeilSentaku","reMeilSentaku");
		map.put("delHaishinsaki","delHaishinsaki");
		map.put("reTantoSoshiki","reTantoSoshiki");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		// 課題No.21 ユーザマスタ　参照組織一覧の追加・削除制御対応
		// 追加開始
		UserTorokuForm form = new UserTorokuForm(appContext);
		// 追加完了
        appContext.setActionForm(form);       
	    
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   
        
	    // ビジネスロジック実行
        UserTorokuBss bss = new UserTorokuBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(USERTOROKUFORM, form);
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
        appContext.removeActionFormExcept(USERTOROKUFORM);
        // sessionからActionForm取得
        UserIchiranForm form = (UserIchiranForm)appContext.getSessionActionForm(USERTOROKUFORM);

        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        // ビジネスロジック実行
        UserIchiranBss bss = new UserIchiranBss(appContext);            
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
	    return GS.OS7107;
	}
	
	
	/**
	 * 【←前のXX件】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    return GS.OS7107;
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
	    return GS.OS7107;	    
	}

	/**
	 * 【登録処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object toroku(AppContext appContext) throws Exception {
		UserTorokuBss bss = new UserTorokuBss(appContext);       	    
        String result = bss.toroku();
        if (GS.OS7106.equals(result)) {            
        	UserIchiranAction acc = new UserIchiranAction();
        	UserIchiranForm form = (UserIchiranForm)appContext.getSessionActionForm(USERICHIRANFORM);
            form.setAction_flg(INSERT);
            appContext.setSessionActionForm(USERICHIRANFORM, form);
            acc.appReExecute(appContext);
        }else if(GS.OS1101.equals(result)){
			appContext.getCMN().init_UserMaintenance_bean();
			appContext.removeActionFormAll();
        	result = (String) menuLinkOS1101(appContext);
        }
		return result;
	}
	
	/**
	 * 【業務フローパターン削除処理】 <br>
	 * 
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object delete(AppContext appContext) throws Exception {
		UserTorokuBss bss = new UserTorokuBss(appContext);       	    
        String result = bss.delete();
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
    	UserIchiranAction acc = new UserIchiranAction();
    	UserIchiranForm form = (UserIchiranForm)appContext.getSessionActionForm(USERICHIRANFORM);
        form.setAction_flg(BACK);
        appContext.setSessionActionForm(USERICHIRANFORM, form);
        acc.appReExecute(appContext);
        // ActionFormをsessionから削除
        appContext.removeActionForm(USERTOROKUFORM);
        return GS.OS7106;
    }
	
    /**
     * 【システム管理者アクション処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object admin_kanri(AppContext appContext) throws Exception {
        UserTorokuBss bss = new UserTorokuBss(appContext); 
        String result = bss.admin_kanri();
        return result;
    }
    	
    /**
     * 【業務フローパターン追加アクション処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object tuika(AppContext appContext) throws Exception {        
        UserTorokuBss bss = new UserTorokuBss(appContext); 
        String result = bss.tuika();
        return result;
    }

	/**
	 * メール送信先選択画面より遷移
	 * @param appContext
	 * @return forward
	 * @throws Exception
	 */
	public Object reMeilSentaku(AppContext appContext) throws Exception {
		UserTorokuBss bss = new UserTorokuBss(appContext);
		String result = bss.reMeilSentaku();

		return result;
	}

	/**
	 * メール配信先の削除ボタン
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object delHaishinsaki(AppContext appContext) throws Exception {
		UserTorokuBss bss = new UserTorokuBss(appContext);
		String result = bss.delHaishinsaki();
		
		return result;
	}
	
	/**
	 * 担当組織選択画面より遷移
	 * @param appContext
	 * @return forward
	 * @throws Exception
	 */
	public Object reTantoSoshiki(AppContext appContext) throws Exception {
		UserTorokuBss bss = new UserTorokuBss(appContext);
		String result = bss.reTantoSoshiki();

		return result;
	}
}
