/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.common.action.SashimodoshiAction;
import app.common.action.SashimodoshiCommentAction;
import app.tairyu.bss.SenteisyosaiBss;
import app.tairyu.form.SenteisyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OB2102_対象先選定_選定先詳細 アクションクラス
 */
@Controller
@RequestMapping("/tairyu/senteisyosai.do")
public class SenteisyosaiAction extends AppMenuAction {

	private static final String SENTEISYOSAIFORM = "01SenteisyosaiForm";
	private static final String KIHON_JOHO       = "1";
	private static final String SAIKEN_MEISAI    = "2";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("back","back");
		map.put("sashimodoshi","sashimodoshi");
		map.put("mogitori_kaijo","mogitori_kaijo");	
		map.put("taisyogai","taisyogai");
		map.put("toroku","toroku");
		map.put("syonin","syonin");
		map.put("add","add");		
		map.put("comment","comment");
		map.put("kihon_joho","kihon_joho");
		map.put("saiken_meisai","saiken_meisai");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // sessionからActionForm取得
		SenteisyosaiForm form = (SenteisyosaiForm)appContext.getSessionActionForm(SENTEISYOSAIFORM);
	    if(form == null){
	    	form = new SenteisyosaiForm();
	        // sessionスコープにActionFormを登録
	        appContext.setSessionActionForm(SENTEISYOSAIFORM,form);
	    }
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // 共通タブ利用画面IDに当画面IDをセット
        SessionData cmnData = appContext.getCMN();
        cmnData.setTab_riyou_gamenId(form.toString());

        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
        String result = bss.executeInit();
        return result;
	}

	/**
	 * 【戻るボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object back(AppContext appContext) throws Exception {
	    
        // 遷移元画面IDより遷移先を決定
        SessionData cmnData = appContext.getCMN();
        String rtn_id = cmnData.getReturn_gamenId();
		
       	if(GS.OB2101.equals(rtn_id)) {
   		    SenteiAction acc = new SenteiAction();
   		    acc.appReExecute(appContext);
   		} else if(GS.OB2103.equals(rtn_id)) {
   		    SenteituikaAction acc = new SenteituikaAction();
   		    acc.appReExecute(appContext);
   		} else if(GS.OB2105.equals(rtn_id)) {
   		    KarikijuntuikaAction acc = new KarikijuntuikaAction();
   		    acc.appExecute(appContext);
   		} else if(GS.OB2104.equals(rtn_id)) {
   		    SenteisyoninAction acc = new SenteisyoninAction();
   		    acc.appReExecute(appContext);
   		} else if(GS.OS4101.equals(rtn_id)) {
   		    app.system.action.GolfAction acc = new app.system.action.GolfAction();
   		    acc.appReExecute(appContext);
   		}
   		return rtn_id;
	}

	/**
	 * 【差戻ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object sashimodoshi(AppContext appContext) throws Exception {
	    
        // 詳細画面IDに当画面IDをセット
		SenteisyosaiForm form = (SenteisyosaiForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSyosai_returnId(form.toString());
		
        // OZ2101_差戻先選択に遷移
		SashimodoshiAction acc = new SashimodoshiAction();
	    acc.appExecute(appContext);
	    return GS.OZ2101;
	}

	/**
	 * 【もぎ取り解除ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object mogitori_kaijo(AppContext appContext) throws Exception {
	    
        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
	    bss.mogitori_kaijo();
	    return back(appContext);
	}

	/**
	 * 【対象外ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object taisyogai(AppContext appContext) throws Exception {
	    
        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
	    if(!bss.doTaisyogai()){
		    return GS.OB2102;	    	
	    }
	    return back(appContext);
	}

	/**
	 * 【登録ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object toroku(AppContext appContext) throws Exception {
	    
        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
	    if(!bss.doToroku()){
		    return GS.OB2102;	    	
	    }
	    return back(appContext);
	}

	/**
	 * 【承認ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object syonin(AppContext appContext) throws Exception {
	    
        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
	    bss.doSyonin();
	    return back(appContext);
	}

	/**
	 * 【追加ボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object add(AppContext appContext) throws Exception {
	    
        // ビジネスロジック実行
	    SenteisyosaiBss bss = new SenteisyosaiBss(appContext);       	    
	    if(!bss.doAdd()){
		    return GS.OB2102;	    	
	    }

	    // 遷移元画面IDより遷移先を決定
        SessionData cmnData = appContext.getCMN();
        String rtn_id = cmnData.getReturn_gamenId();
        if(GS.OB2105.equals(rtn_id)){
   		    SenteiAction acc = new SenteiAction();
   		    acc.appReExecute(appContext);
    	    return GS.OB2101;
        }else{
    	    return back(appContext);
        }
	}

	/**
	 * 【コメントリンク処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object comment(AppContext appContext) throws Exception {
	    
        // 詳細画面IDに当画面IDをセット
		SenteisyosaiForm form = (SenteisyosaiForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSyosai_returnId(form.toString());
		
        // OZ4101_コメント表示に遷移
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
	    acc.appExecute(appContext,cmnData.getTori_bean().getAnken_no());
	    return GS.OZ4101;
	}

	/**
	 * 【基本情報照会タブ押下時の処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object kihon_joho(AppContext appContext){
		SenteisyosaiForm form = (SenteisyosaiForm)appContext.getActionForm();
		form.setTabValue(KIHON_JOHO);
		return GS.OB2102;
	}

	/**
	 * 【債権明細照会タブ押下時の処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object saiken_meisai(AppContext appContext){
		SenteisyosaiForm form = (SenteisyosaiForm)appContext.getActionForm();
		form.setTabValue(SAIKEN_MEISAI);
		return GS.OB2102;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//未使用
		return GS.OB2102;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//未使用
	    return GS.OB2102;	    
	}
	
}