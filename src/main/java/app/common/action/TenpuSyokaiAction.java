/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/15		SSC				1.5次版機能組込
002		09/10/20		SSC				課題No.52 HTMLファイルオープン対応
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.SessionDataZen;
import app.common.bss.TenpuSyokaiBss;
import app.common.form.TenpuSyokaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppDownloadAction;
import common.struts.AppMenuAction;
import common.util.TempFile;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 添付内容照会画面アクションクラス
 */
public class TenpuSyokaiAction extends AppMenuAction {

	private String CLASSNAME = getClass().getName(); // クラス名
		
	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("back","back");
		map.put("download","download");
		
		return map;
	}

	
	public Object prevX(AppContext appContext) throws Exception {
		return GS.OZ1102;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
		return GS.OZ1102;
	}


	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
		SessionDataZen cmnData = appContext.getCMNZen();
		
		//この画面用のActionFormを作成
		TenpuSyokaiForm form = new TenpuSyokaiForm();
	    
	    form.setAnkenNo(cmnData.getTairyu_anken_no());
	    form.setPhase(cmnData.getPhase());
	    form.setInitmode(1);
	    form.setBunsyo_no(cmnData.getBunsyo_no());
	    // No797, 2008/06/09, SJA渡辺, 査定会社コードを保持するように修正
	    form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    
	    return select(appContext);
	    
	}
	
	/**
	 * 【ファイルダウンロード処理】
	 */
	public Object download(AppContext appContext) throws Exception {
		
		TenpuSyokaiForm form = (TenpuSyokaiForm)appContext.getActionForm();
    	HttpServletRequest req = appContext.getRequest();
    	HttpServletResponse res = appContext.getResponse();
    	AppDownloadAction acc = new AppDownloadAction();
    
	    // 一時ファイル作成
	    TenpuSyokaiBss bss = new TenpuSyokaiBss(appContext);
	    
	    // 管理票No200808120004, 2008/08/12, SJA渡辺, ファイルサーバーにファイルが無い場合、エラーメッセージを表示するように修正
	    try {
	    	TempFile temp = (TempFile)bss.downloadExecute();
	    	
	    	// リクエストスコープのデータを登録
	    	req.setAttribute(GS.DOWNLOADCONTEXT,temp);
	    	
	    	// ダウンロード
	    	acc.execute(new ActionMapping(),form,req,res);
	    } catch(Exception e) {
	    	appContext.setMsgCode("err.0122");
	    }

	    // 課題No.52
	    // 修正開始
	    if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			return GS.OZ1102;
		}
	    
        return null;
	    // 修正完了
	}
	
	/**
	 * 【戻るボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {
		SessionDataZen cmnData = appContext.getCMNZen();
		SessionData cmnData2 = appContext.getCMN();
        //IT231対応
		appContext.getCMN().init_syosai_bean();
        //IT231ここまで
		cmnData2.setSyosai_returnId(GS.EMPTY_CHARCTER);

	    // 遷移元画面へ遷移
		return cmnData.getReturnId();
	}

	
	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext) throws Exception {
		//session取得（Pager用の処理）
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
		HttpSession session = appContext.getRequest().getSession( true );
	    TenpuSyokaiForm form = (TenpuSyokaiForm)appContext.getActionForm();
	    
	    //検索実行
	    TenpuSyokaiBss bss = new TenpuSyokaiBss(appContext);	        	    
	    if(form.getInitmode() == 1) { // 画面初期表示時
	        String result = bss.execute();

	        // sessionスコープにActionFormを登録（Pager用の処理）
	        session.setAttribute("05TenpuSyokaiForm", form);
	        form.setInitmode(0);
	        appContext.setActionForm(form);

	        return result;
	    } else { // 画面初期表示時以外
			String result =  bss.execute();
			return result;
	    }
	}
}
