/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.tairyu.bss.SenteisyoninBss;
import app.tairyu.form.SenteisyoninForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OB2104_対象先選定_承認一覧 アクションクラス
 */
@Controller
@RequestMapping("/tairyu/senteisyonin.do")
public class SenteisyoninAction extends AppMenuAction {

	private static final String SENTEISYONINFORM = "01SenteisyoninForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("ikkatu_syonin","ikkatu_syonin");
		map.put("syonin","syonin");
		map.put("show","show");
		map.put("syosai","syosai");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // appContextのActionFormを上書き
		SenteisyoninForm form = new SenteisyoninForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
        SenteisyoninBss bss = new SenteisyoninBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SENTEISYONINFORM,form);
        return result;
	}

	/**
	 * 【画面初期表示処理(メニューリンク以外から遷移時)】
	 */
	
	public Object appReExecute(AppContext appContext) throws Exception {        
		appContext.removeActionFormExcept(SENTEISYONINFORM);
	    // sessionからActionForm取得
		SenteisyoninForm form = (SenteisyoninForm)appContext.getSessionActionForm(SENTEISYONINFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
        SenteisyoninBss bss = new SenteisyoninBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
	}
	
	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		SenteisyoninForm form = (SenteisyoninForm)appContext.getActionForm();
		form.setPager();
		return GS.OB2104;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteisyoninForm form = (SenteisyoninForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OB2104;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteisyoninForm form = (SenteisyoninForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OB2104;	    
	}
	
	/**
	 * 【一括承認チッェクボックス実行処理】 <BR/>
	 */
	public Object ikkatu_syonin(AppContext appContext) throws Exception {

        // ビジネスロジック実行
        SenteisyoninBss bss = new SenteisyoninBss(appContext);  
        bss.doIkatuSyonin();
        
        return GS.OB2104;
	}

	/**
	 * 【承認実行処理】 <BR/>
	 */
	public Object syonin(AppContext appContext) throws Exception {

        // ビジネスロジック実行
        SenteisyoninBss bss = new SenteisyoninBss(appContext);  
        bss.doSyonin();

        return GS.OB2104;
	}
	
	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object syosai(AppContext appContext) throws Exception {

        // クリックされた勘定先情報を機能共通セッションに格納
		SenteisyoninForm form = (SenteisyoninForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

	    //遷移元画面IDに当画面IDをセット
        cmnData.setReturn_gamenId(form.toString());

        //OB2102_対象先選定_選定詳細に遷移
		SenteisyosaiAction acc = new SenteisyosaiAction();
	    acc.appExecute(appContext);

        return GS.OB2102;
	}
}