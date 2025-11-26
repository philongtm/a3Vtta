/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.SateiKaisyaBean;
import app.SessionData;
import app.system.bss.SateikaisyaIchiranBss;
import app.system.form.SateikaisyaIchiranForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OS7102_査定会社メンテナンス_一覧 アクションクラス <br>
 */
public class SateikaisyaIchiranAction extends AppMenuAction {

	private static final String SATEIKAISYAICHIRANFORM = "02SateikaisyaIchiranForm"; // 査定会社メンテナンス_一覧のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("newRegist", "newRegist");
		map.put("systemKbn", "systemKbn");		
		map.put("show", "show");		
		map.put("search", "search");		
		map.put("hanyo2Link", "hanyo2Link");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
		// 機能共通セッションの査定会社情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_sateikaisya_bean();
        // appContextのActionFormを上書き
		SateikaisyaIchiranForm form = new SateikaisyaIchiranForm();
		appContext.setActionForm(form);
        // ビジネスロジック実行
        SateikaisyaIchiranBss bss = new SateikaisyaIchiranBss(appContext); 
        String result = bss.executeInit();
        // 一覧初期化
        List ar_meisai = new ArrayList();
	    // ActionForm に明細を格納
	    form.setAr_meisai(ar_meisai);    
	    // ページ設定
	    form.setPager(ar_meisai);
        // セッションスコープに02SateikaisyaIchiranFormを登録する。
        appContext.setSessionActionForm(SATEIKAISYAICHIRANFORM, form);
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
		appContext.removeActionFormExcept(SATEIKAISYAICHIRANFORM);
	    SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getSessionActionForm(SATEIKAISYAICHIRANFORM);
	    // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_sateikaisya_bean();
	    // ビジネスロジック実行
	    SateikaisyaIchiranBss bss = new SateikaisyaIchiranBss(appContext);   	    
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
        cmnData.setSateikaisya_bean(new SateiKaisyaBean());
        // 査定会社登録画面へ遷移する。
    	SateikaisyaTorokuAction act = new SateikaisyaTorokuAction();
    	act.appExecuteRegist(appContext);
        // 遷移先を指定する。
        return GS.OS7103;
    }

    /**
     * 【システムアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object systemKbn(AppContext appContext) throws Exception {
	    SateikaisyaIchiranBss bss = new SateikaisyaIchiranBss(appContext);       	    
        bss.doChangeSystemKbn();
		return GS.OS7102;
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
		SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS7102;
    }

    /**
     * 【検索アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object search(AppContext appContext) throws Exception {
    	// 査定会社一覧フォーム
    	SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getActionForm();
    	// 検索キーをセットする。
    	form.setSrhSystemKbn(form.getSystemKbn()); // システム
    	form.setSrhHanyo1(form.getHanyo1()); // 汎用１
    	form.setSrhHanyo2(form.getHanyo2()); // 汎用２
    	form.setSrhHanyo2Name(form.getHanyo2Name()); // 汎用２名称
    	
        // ビジネスロジック実行
    	SateikaisyaIchiranBss bss = new SateikaisyaIchiranBss(appContext); 
        bss.execute();
        // 遷移先を指定する。
        return GS.OS7102;
    }

    /**
     * 【汎用２リンクアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object hanyo2Link(AppContext appContext) throws Exception {
    	SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSateikaisya_bean((SateiKaisyaBean)form.getAr_meisai().get(form.getId()));
        // 査定会社登録画面へ遷移する。
    	SateikaisyaTorokuAction act = new SateikaisyaTorokuAction();
    	act.appExecuteUpdate(appContext);
        // 遷移先を指定する。
        return GS.OS7103;
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
		SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS7102;
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
		SateikaisyaIchiranForm form = (SateikaisyaIchiranForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS7102;	    
    }
}