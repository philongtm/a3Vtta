/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.common.bss.TairyuMeisaiBss;
import app.common.form.TairyuMeisaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ6101_滞留債権明細照会タブ  アクションクラス
 */
@Controller
@RequestMapping("/common/tairyu_meisai.do")
public class TairyuMeisaiAction extends AppMenuAction {

	private static final String TAIRYUMEISAIFORM = "05TairyuMeisaiForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("show","show");
		map.put("tenpu","tenpu");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	
	public Object appExecute(AppContext appContext) throws Exception {        

		TairyuMeisaiForm form = (TairyuMeisaiForm)appContext.getSessionActionForm(TAIRYUMEISAIFORM);
        SessionData cmnData = appContext.getCMN();
		String result = cmnData.getTab_riyou_gamenId();
		if(form == null){
			form = new TairyuMeisaiForm();
			appContext.setSessionActionForm(TAIRYUMEISAIFORM,form);
			// appContextのActionFormを上書き
	        appContext.setActionForm(form);
		    // ビジネスロジック実行
	        TairyuMeisaiBss bss = new TairyuMeisaiBss(appContext);
	        bss.execute();
		}
		//ページ設定
		form.setPager(form.getId() + 1);
        //共通タブ利用画面に遷移
        return result;
	}

	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		TairyuMeisaiForm form = (TairyuMeisaiForm)appContext.getActionForm();
		form.setPager();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		TairyuMeisaiForm form = (TairyuMeisaiForm)appContext.getActionForm();
		form.setPrevList();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		TairyuMeisaiForm form = (TairyuMeisaiForm)appContext.getActionForm();
		form.setNextList();
        SessionData cmnData = appContext.getCMN();
        //共通タブ利用画面に遷移
        return cmnData.getTab_riyou_gamenId();
	}

	/**
	 * 【添付参照処理】
	 */
	public Object tenpu(AppContext appContext) throws Exception {
	    // クリックされた勘定先情報を機能共通セッションに格納
		TairyuMeisaiForm form = (TairyuMeisaiForm)appContext.getActionForm();
	    SessionData cmnData = appContext.getCMN();
	    cmnData.setSyosai_bean((MeisaisyosaiBean)form.getAr_meisai().get(form.getId()));
	       
	    // 遷移元画面IDの設定
	    if(GS.OB1105.equals(cmnData.getTab_riyou_gamenId())) {
	    	cmnData.setReturn_gamenId(cmnData.getTab_riyou_gamenId());
	    }else{
	       	cmnData.setSyosai_returnId(cmnData.getTab_riyou_gamenId());
	    }
	       
	    //OZ1102_添付内容照会に遷移
	    TenpuSyokaiAction acc = new TenpuSyokaiAction();
	    acc.appExecute(appContext);
	    return GS.OZ1102;
	}
}