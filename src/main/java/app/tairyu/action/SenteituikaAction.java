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
import app.tairyu.bss.SenteituikaBss;
import app.tairyu.form.SenteituikaForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OB2103_対象先選定_追加対象先選択 アクションクラス
 */
@Controller
@RequestMapping("/tairyu/senteituika.do")
public class SenteituikaAction extends AppMenuAction {

	private static final String SENTEITUIKAFORM = "01SenteituikaForm";
	private static final String ACTIONFORMPATTERN = "01SenteiForm|01SenteituikaForm";
		
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("OB2101","OB2101");
		map.put("show","show");
		map.put("search","search");
		map.put("syosai","syosai");
		return map;
	}
	
	/**
	 * 【画面初期表示処理(OB2101_対象先選定_選定実行から遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // appContextのActionFormを上書き
		SenteituikaForm form = new SenteituikaForm();
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();        
	    // ビジネスロジック実行
	    SenteituikaBss bss = new SenteituikaBss(appContext);       	    
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SENTEITUIKAFORM,form);
        return result;
	}

	/**
	 * 【画面初期表示処理(OB2101_対象先選定_選定実行以外から遷移時)】
	 */
	
	public Object appReExecute(AppContext appContext) throws Exception {        
		appContext.removeActionFormPattern(ACTIONFORMPATTERN);
	    // sessionからActionForm取得
	    SenteituikaForm form = (SenteituikaForm)appContext.getSessionActionForm(SENTEITUIKAFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
	    // ビジネスロジック実行
	    SenteituikaBss bss = new SenteituikaBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
	}
	
	/**
	 * 【戻る処理】
	 */
	public Object OB2101(AppContext appContext) throws Exception {
	    //OB2101_対象先選定_選定実行に遷移
		SenteiAction acc = new SenteiAction();
	    acc.appReExecute(appContext);
	    return GS.OB2101;
	}

	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		SenteituikaForm form = (SenteituikaForm)appContext.getActionForm();
		form.setPager();
		return GS.OB2103;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteituikaForm form = (SenteituikaForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OB2103;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		SenteituikaForm form = (SenteituikaForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OB2103;	    
	}
	
	/**
	 * 【検索処理】
	 */
	public Object search(AppContext appContext) throws Exception {

	    // ビジネスロジック実行
		SenteituikaBss bss = new SenteituikaBss(appContext);
		bss.search();
	    return GS.OB2103;
	}
	
	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object syosai(AppContext appContext) throws Exception {

        // クリックされた勘定先情報を機能共通セッションに格納
		SenteituikaForm form = (SenteituikaForm)appContext.getActionForm();
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