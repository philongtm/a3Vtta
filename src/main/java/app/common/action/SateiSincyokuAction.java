/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.common.bss.SateiSincyokuBss;
import app.common.form.SateiSincyokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

/**
 * OZ6109_査定進捗照会タブ アクションクラス <br>
 */
@Controller
@RequestMapping("/common/Satei_sincyoku.do")
public class SateiSincyokuAction extends AppMenuAction {

	private static final String SATEISINCYOKUFORM = "05sateiSincyokuForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("comment","comment");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		SateiSincyokuForm form = new SateiSincyokuForm();
        appContext.setActionForm(form);
        
	    // ビジネスロジック実行
        SateiSincyokuBss bss = new SateiSincyokuBss(appContext);       	    
        bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SATEISINCYOKUFORM, form);
        
        return GS.OZ6109;
	}

    /**
     * 【コメント表示リンクアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object comment(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDを設定する。
        cmnData.setReturn_gamenId(cmnData.getTab_riyou_gamenId());
        
		SateiSincyokuForm form = (SateiSincyokuForm)appContext.getActionForm();
        SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
		List<HashMap<String, String>> list = (List<HashMap<String, String>>)form.getAr_meisai();
        acc.appExecute(appContext,(HashMap<String, String>)list.get(form.getId()));
        
        // OZ4101_コメント表示へ遷移する。
        return GS.OZ4101;
    }
    
	/**
	 * 【←前のXX件】未実装 <br>
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    return GS.OZ6109;
	}
	
	/**
	 * 【次のXX件→】未実装 <br>
	 */	
	public Object nextY(AppContext appContext) throws Exception {
	    return GS.OZ6109;    
	}
}
