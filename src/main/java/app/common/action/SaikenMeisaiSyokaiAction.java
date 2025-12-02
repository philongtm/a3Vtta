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
import app.common.bss.SaikenMeisaiSyokaiBss;
import app.common.form.SaikenMeisaiSyokaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ6105_債権明細照会タブ  アクションフォームクラス
 */
@Controller
@RequestMapping("/common/saiken_meisai.do")
public class SaikenMeisaiSyokaiAction extends AppMenuAction {

	private static final String SAIKENMEISAIFORM = "SaikenMeisaiSyokaiForm";
	
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

		SaikenMeisaiSyokaiForm form = (SaikenMeisaiSyokaiForm)appContext.getSessionActionForm(SAIKENMEISAIFORM);
        SessionData cmnData = appContext.getCMN();
		String result = cmnData.getTab_riyou_gamenId();
		if(form == null){
			form = new SaikenMeisaiSyokaiForm();
			appContext.setSessionActionForm(SAIKENMEISAIFORM,form);
			// appContextのActionFormを上書き
			appContext.setActionForm(form);
			// ビジネスロジック実行
			SaikenMeisaiSyokaiBss bss = new SaikenMeisaiSyokaiBss(appContext);  
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
		SaikenMeisaiSyokaiForm form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();
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
		SaikenMeisaiSyokaiForm form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();
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
		SaikenMeisaiSyokaiForm form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();
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
			SaikenMeisaiSyokaiForm form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();
	        SessionData cmnData = appContext.getCMN();
	        cmnData.setSyosai_bean((MeisaisyosaiBean)form.getAr_meisai().get(form.getId()));
	        
	        //遷移元画面IDの設定
	        if(GS.OC1107.equals(cmnData.getTab_riyou_gamenId())) {
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
