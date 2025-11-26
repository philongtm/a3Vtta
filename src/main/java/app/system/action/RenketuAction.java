/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/14		SSC				1.5次版機能組込
******************************************************************************/
package app.system.action;

import app.system.bss.RenketuBss;
import app.system.form.RenketuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.util.InputCheck;

import javax.servlet.http.HttpSession;
import java.util.HashMap;


/**
 * 連結区分マスタUPLOAD画面アクションクラス
 */
public class RenketuAction extends AppMenuAction {

	private String CLASSNAME = getClass().getName(); // クラス名

	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("view","view");
		map.put("renketu_touroku","touroku");	// 登録ボタン押下
		map.put("renketu_download","download");	// ダウンロードボタン押下
		
		return map;
	}

		
	public Object prevX(AppContext appContext) throws Exception {
		// 処理なし
	    return GS.RC_OK;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
		// 処理なし
	    return GS.RC_OK;	    
	}
	
	/**
	 * 【初期画面表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {   	    
		// この画面用のActionFormを作成
	    RenketuForm form = new RenketuForm();
	    
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
        HttpSession session = appContext.getRequest().getSession( true );

        // sessionスコープにActionFormを登録（Pager用の処理）
        form = (RenketuForm)appContext.getActionForm();
        session.setAttribute("04RenketuForm", form);
        appContext.setActionForm(form);

		// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
		appContext.setFocusField("fileUp");
        
	    return GS.OS7113;
	}
	
	/**
	 * 【アップロードボタン押下処理】
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object touroku(AppContext appContext) throws Exception {
	    RenketuForm form = (RenketuForm)appContext.getActionForm();
	    InputCheck check = new InputCheck();
    	// No484, 2008/05/30, SJA渡辺, 入力禁止文字存在チェック追加
	    if (form.getFileUp() != null && check.haveKinshiMoji(form.getFileUp().toString())) {
	    	appContext.setMsgCode("err.0109");
 			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
 			appContext.setFocusField("fileUp");
			return GS.OS7113;
	    }
		RenketuBss bss=new RenketuBss(appContext);		
		bss.uploadFile(form.getFileUp());
		
		return GS.OS7113;
	}
	
	/**
	 * 【ダウンロードボタン押下処理】
	 * @return
	 * @throws Exception
	 */
	public Object download(AppContext appContext) throws Exception {
		RenketuBss bss=new RenketuBss(appContext);
		
		// ダウンロード処理
		bss.downloadFile();
		
		return GS.OS7113;
	}

}