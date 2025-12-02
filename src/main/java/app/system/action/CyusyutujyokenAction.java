/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.TyusyutuJokenBean;
import app.system.bss.CyusyutujyokenBss;
import app.system.form.CyusyutujyokenForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OS7110_抽出条件メンテナンス_一覧 アクションクラス <br>
 */
@Controller
@RequestMapping("/system/cyusyutujyoken.do")
public class CyusyutujyokenAction extends AppMenuAction {

	private static final String CYUSYUTUJYOKENFORM = "10CyusyutujyokenForm"; // 抽出条件メンテナンス_一覧のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("newRegist", "newRegist");
		map.put("systemKbn", "systemKbn");		
		map.put("hanyo1", "hanyo1");		
		map.put("show", "show");		
		map.put("search", "search");		
		map.put("jiyuLink", "jiyuLink");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
		// 機能共通セッションの抽出条件情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_joken_bean();
        // appContextのActionFormを上書き
        CyusyutujyokenForm form = new CyusyutujyokenForm();
		appContext.setActionForm(form);
        // ビジネスロジック実行
		CyusyutujyokenBss bss = new CyusyutujyokenBss(appContext); 
        String result = bss.executeInit();
        // 一覧初期化
        List ar_meisai = new ArrayList();
	    // ActionForm に明細を格納
	    form.setAr_meisai(ar_meisai);    
	    // ページ設定
	    form.setPager(ar_meisai);
        // セッションスコープに10CyusyutujyokenFormを登録する。
        appContext.setSessionActionForm(CYUSYUTUJYOKENFORM, form);
        return result;
	}

	/**
	 * 【画面初期表示処理(メニューリンク以外から遷移時)】 <br>
	 * 
	 * @param appContext AppContext
	 * @return 遷移先
	 * @throws Exception 
	 */
	
	public Object appReExecute(AppContext appContext) throws Exception {        
		appContext.removeActionFormExcept(CYUSYUTUJYOKENFORM);
		CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getSessionActionForm(CYUSYUTUJYOKENFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの抽出条件情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_joken_bean();
	    // ビジネスロジック実行
        CyusyutujyokenBss bss = new CyusyutujyokenBss(appContext);   	    
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
	}

    /**
     * 【新規作成アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object newRegist(AppContext appContext) throws Exception {
        SessionData cmnData = appContext.getCMN();
        cmnData.setJoken_bean(new TyusyutuJokenBean());
        // 抽出条件登録画面へ遷移する。
        CyusyutujyokenTorokuAction act = new CyusyutujyokenTorokuAction();
    	act.appExecuteRegist(appContext);
        // 遷移先を指定する。
        return GS.OS7111;
    }

    /**
     * 【システムアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object systemKbn(AppContext appContext) throws Exception {
    	CyusyutujyokenBss bss = new CyusyutujyokenBss(appContext);       	    
        bss.doChangeSystemKbn();
		return GS.OS7110;
    }

    /**
     * 【汎用１アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object hanyo1(AppContext appContext) throws Exception {
    	CyusyutujyokenBss bss = new CyusyutujyokenBss(appContext);       	    
        bss.doChangeHanyo1();
		return GS.OS7110;
    }

    /**
     * 【表示件数アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
    	CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS7110;
    }

    /**
     * 【検索アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object search(AppContext appContext) throws Exception {
    	// 抽出条件一覧フォーム
    	CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getActionForm();
    	// 検索キーをセットする。
    	form.setSrhHanyo1(form.getHanyo1()); // 汎用１
    	form.setSrhHanyo2(form.getHanyo2()); // 汎用２
    	form.setSrhJyokenNm(form.getJyokenNm()); // 条件名称
    	form.setSrhKesanKbn(form.getKesanKbn()); // 決算日区分
    	form.setSrhKijyunbi(form.getKijyunbi()); // 基準日
    	form.setSrhSystemKbn(form.getSystemKbn()); // システム
    	
        // ビジネスロジック実行
    	CyusyutujyokenBss bss = new CyusyutujyokenBss(appContext); 
        bss.execute();
        // 遷移先を指定する。
        return GS.OS7110;
    }

    /**
     * 【抽出事由リンクアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object jiyuLink(AppContext appContext) throws Exception {
    	CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setJoken_bean((TyusyutuJokenBean)form.getAr_meisai().get(form.getId()));
        // 抽出条件登録画面へ遷移する。
        CyusyutujyokenTorokuAction act = new CyusyutujyokenTorokuAction();
    	act.appExecuteUpdate(appContext);
        // 遷移先を指定する。
        return GS.OS7111;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
    	CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS7110;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
    	CyusyutujyokenForm form = (CyusyutujyokenForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS7110;	    
    }
}