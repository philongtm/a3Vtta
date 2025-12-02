/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.common.bss.KihonJohoSyokaiBss;
import app.common.form.KihonJohoSyokaiForm;
import common.AppContext;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ6108_基本情報照会タブ アクションクラス
 */
@Controller
@RequestMapping("/common/kihon_joho.do")
public class KihonJohoSyokaiAction extends AppMenuAction {

	private static final String KIHONJOHOSYOKAIFORM = "KihonJohoSyokaiForm";

	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		return map;
	}

	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
	    
		// アクションフォームを取得
		KihonJohoSyokaiForm form = (KihonJohoSyokaiForm)appContext.getSessionActionForm(KIHONJOHOSYOKAIFORM);
        SessionData cmnData = appContext.getCMN();
		String result = cmnData.getTab_riyou_gamenId();
		if(form == null){
			form = new KihonJohoSyokaiForm();
	        // sessionスコープにActionFormを登録
			appContext.setSessionActionForm(KIHONJOHOSYOKAIFORM,form);
			// appContextのActionFormを上書き
	        appContext.setActionForm(form);
		    // ビジネスロジック実行
	        KihonJohoSyokaiBss bss = new KihonJohoSyokaiBss(appContext);
	        bss.execute();
		}
        //共通タブ利用画面に遷移
        return result;
	}

	/**
	 * 【←前のXX件】未実装
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    return null;
	}
	
	/**
	 * 【次のXX件→】未実装
	 */	
	public Object nextY(AppContext appContext) throws Exception {
	    return null;
	}
}