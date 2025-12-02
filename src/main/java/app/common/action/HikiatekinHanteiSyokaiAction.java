/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.common.bss.HikiatekinHanteiSyokaiBss;
import app.common.form.HikiatekinHanteiSyokaiForm;
import common.AppContext;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ6104_引当金判定照会タブ  アクションクラス
 */
@Controller
@RequestMapping("/common/hikiatekin_hantei.do")
public class HikiatekinHanteiSyokaiAction extends AppMenuAction {

	private static final String HIKIATEKINHANTEISYOKAIFORM = "HikiatekinHanteiSyokaiForm";
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		return map;
	}
	
	/**
	 * 画面初期表示処理
	 * @param appContext アプリケーションContext
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		
		// アクションフォームを取得
		HikiatekinHanteiSyokaiForm form = (HikiatekinHanteiSyokaiForm)appContext.getSessionActionForm(HIKIATEKINHANTEISYOKAIFORM);
        SessionData cmnData = appContext.getCMN();
		String result = cmnData.getTab_riyou_gamenId();
		if(form == null){
			form = new HikiatekinHanteiSyokaiForm();
			appContext.setSessionActionForm(HIKIATEKINHANTEISYOKAIFORM,form);
			//appContextのActionFormを上書き
		    appContext.setActionForm(form);
		    // ビジネスロジック実行
		    HikiatekinHanteiSyokaiBss bss = new HikiatekinHanteiSyokaiBss(appContext);  
	        bss.execute();
		}
        //共通タブ利用画面に遷移
        return result;
	}
	
	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		//空実装
	    return null;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//空実装
	    return null;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//空実装
		return null;
	}
}
