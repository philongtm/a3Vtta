/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.action;

import app.satei.bss.RyuhosaimuBss;
import app.satei.form.RyuhosaimuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OC1105_査定_留保債務登録 アクションクラス<br>
 */
@Controller
@RequestMapping("/satei/ryuhosaimu.do")
public class RyuhosaimuAction extends AppMenuAction {

	private static final String RYUHOSAIMUFORM		= "02RyuhosaimuForm";
	private static final String BACK					= "back";			//戻る
	private static final String VIEW					= "view";			//表示件数
	private static final String PRESERVE				= "preserve";		//保存×
	private static final String IKKATU				= "ikkatu";			//一括判定

    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(RYUHOSAIMUFORM,RYUHOSAIMUFORM);
        map.put(BACK,BACK);
        map.put(VIEW,VIEW);
        map.put(PRESERVE,PRESERVE);
        map.put(IKKATU,IKKATU);
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {

		//常にアクションフォームを生成
		RyuhosaimuForm form = new RyuhosaimuForm();
        appContext.setActionForm(form);

        //以下初期表示処理
        RyuhosaimuBss bss = new RyuhosaimuBss(appContext);
        bss.execInit(appContext);
        
        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(RYUHOSAIMUFORM,form);

	    return form.toString();
	}
	
	/**
	 * 【表示件数セレクトボックス処理】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object view(AppContext appContext) throws Exception {
		//表示件数の変更
		RyuhosaimuForm form = (RyuhosaimuForm)appContext.getActionForm();
		form.setPager();
	    return form.toString();
	}
		
	/**
	 * 【←前のXX件】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		RyuhosaimuForm form = (RyuhosaimuForm)appContext.getActionForm();
		form.setPrevList();
	    return form.toString();
	}
	
	/**
	 * 【次のXX件→】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		RyuhosaimuForm form = (RyuhosaimuForm)appContext.getActionForm();
		form.setNextList();
	    return form.toString();
	}
    
	/**
	 * 【保存ボタン処理】
	 */
	public Object preserve(AppContext appContext) throws Exception {

		RyuhosaimuBss bss = new RyuhosaimuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//保存処理
		bss.execHozon();

	    return appContext.getActionForm().toString();
	}

	/**
	 * 【一括判定チェック処理】
	 */
	public Object ikkatu(AppContext appContext) throws Exception {

		RyuhosaimuBss bss = new RyuhosaimuBss(appContext);

		//一括判定処理
		bss.execIkkatu();

	    return appContext.getActionForm().toString();
	}

	/**
	 * 【戻るボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {

		HikiateAction acc = new HikiateAction();
		acc.appExecute(appContext);

	    return GS.OC1104;
	}
}