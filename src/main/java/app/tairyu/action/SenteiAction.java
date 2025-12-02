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
import app.tairyu.bss.SenteiBss;
import app.tairyu.form.SenteiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OB2101_対象先選定_選定実行 アクションクラス
 */
@Controller
@RequestMapping("/tairyu/sentei.do")
public class SenteiAction extends AppMenuAction {

	private static final String SENTEIFORM = "01SenteiForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("tanto","tanto");
		map.put("show","show");
		map.put("OB2103","OB2103");
		map.put("mogitori","mogitori");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // appContextのActionFormを上書き
		SenteiForm form = new SenteiForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
	    SenteiBss bss = new SenteiBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SENTEIFORM,form);
        return result;
	}

	/**
	 * 【画面初期表示処理(メニューリンク以外から遷移時)】
	 */
	
	public Object appReExecute(AppContext appContext) throws Exception {        
		appContext.removeActionFormExcept(SENTEIFORM);
	    // sessionからActionForm取得
	    SenteiForm form = (SenteiForm)appContext.getSessionActionForm(SENTEIFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
	    SenteiBss bss = new SenteiBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
	}
	
	/**
	 * 【自担当分/汎用２ラジオボタン処理】
	 */
	public Object tanto(AppContext appContext) throws Exception {
	    SenteiBss bss = new SenteiBss(appContext);       	    
        String result = bss.execute();
		return result;
	}
	
	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		SenteiForm form = (SenteiForm)appContext.getActionForm();
		form.setPager();
	    return GS.OB2101;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteiForm form = (SenteiForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OB2101;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteiForm form = (SenteiForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OB2101;	    
	}
	
	/**
	 * 【追加対象先選択ボタン処理】
	 */
	public Object OB2103(AppContext appContext) throws Exception {
	    
	    //OB2103_対象先選定_追加対象先選択に遷移
	    SenteituikaAction acc = new SenteituikaAction();
	    acc.appExecute(appContext);
		    
	    return GS.OB2103;
	}

	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object mogitori(AppContext appContext) throws Exception {

		// もぎ取り処理
		SenteiBss bss = new SenteiBss(appContext);
		if(bss.doMogitori()) {
	        // クリックされた勘定先情報を機能共通セッションに格納
			SenteiForm form = (SenteiForm)appContext.getActionForm();
	        SessionData cmnData = appContext.getCMN();
	        TorihikisakiBean toriBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
	        cmnData.setTori_bean(toriBean);

	        if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(toriBean.getPhase())){
	    	    //OB2105_対象先選定_仮基準査定選択に遷移
	    	    KarikijuntuikaAction acc = new KarikijuntuikaAction();
	    	    acc.appExecute(appContext);
			    return GS.OB2105;
	        }else{
		        //遷移元画面IDに当画面IDをセット
		        cmnData.setReturn_gamenId(form.toString());
		        //OB2102_対象先選定_選定詳細に遷移
		        SenteisyosaiAction acc = new SenteisyosaiAction();
			    acc.appExecute(appContext);
			    return GS.OB2102;
	        }
		} else {
		    return GS.OB2101;
		}
	}
}