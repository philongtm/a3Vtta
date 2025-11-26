/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/15		SSC				課題No.202 戻り時修正
******************************************************************************/

package app.syokai.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.syokai.bss.SateiBss;
import app.syokai.form.SateiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS6101_査定内容照会 アクションクラス <br>
 */
public class SateiAction extends AppMenuAction {

	private static final String SATEIFORM = "03SateiForm";
	
	/**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("satei","satei");
        map.put("search","search");
        map.put("show","show");
        map.put("change1","change1");
        map.put("change2","change2");
        map.put("detailPage", "detailPage");
        return map;
    }
    
    /**
     * 【画面初期表示処理(メニューリンクから遷移時)】
     */
    public Object appExecute(AppContext appContext) throws Exception {  
        
    	appContext.getCMN().setReturn_gamenId(GS.EMPTY_CHARCTER);
        // appContextのActionFormを上書き
    	SateiForm form = new SateiForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SATEIFORM, form);
        return result;
    }

    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】
     */
    public Object appReExecute(AppContext appContext) throws Exception {  
    	appContext.getCMN().setReturn_gamenId(GS.EMPTY_CHARCTER);

        appContext.removeActionFormExcept(SATEIFORM);
        // sessionからActionForm取得
        SateiForm form = (SateiForm)appContext.getSessionActionForm(SATEIFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);            
        String result = bss.execute();
        //課題No.202
        //修正開始
        //前回のページを表示
        form.setPager(form.getId() + 1);
        //修正完了
        
        return result;
    }
    
    /**
     * 
     * ラジオボタンの変換 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object satei(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);            
        String result = bss.doSatei();
        return result;
    }
    
    /**
     * 
     * 汎用1アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object change1(AppContext appContext) throws Exception {  
    	SateiForm form = (SateiForm)appContext.getSessionActionForm(SATEIFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);
        String result = bss.doChange1();
        
        return result;
    }
    
    /**
     * 
     * 汎用2アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object change2(AppContext appContext) throws Exception {  
    	SateiForm form = (SateiForm)appContext.getSessionActionForm(SATEIFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);
        String result = bss.doChange2();
        
        return result;
    }
    
    /**
     * 
     * 検索アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object search(AppContext appContext) throws Exception {  
    	SateiForm form = (SateiForm)appContext.getSessionActionForm(SATEIFORM);
    	// appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateiBss bss = new SateiBss(appContext);
        String result = bss.doSearch();
        
        return result;
    }
    
    /**
     * 
     * 勘定先CDリンクアクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object detailPage(AppContext appContext) throws Exception {
    	SateiForm form = (SateiForm)appContext.getSessionActionForm(SATEIFORM);
    	SessionData cmnData = appContext.getCMN();
    	// リンククリックされた勘定先の勘定先情報Beanを機能共通セッションに格納する。
    	cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));
    	// 共)遷移元画面IDに当画面IDを設定する
    	cmnData.setReturn_gamenId(form.toString());
    	// OS6102_査定内容詳細へ遷移する。
    	SateisyosaiAction acc = new SateisyosaiAction();
		acc.appExecute(appContext);
		return GS.OS6102;
    }
    
    /**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		SateiForm form = (SateiForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS6101;
	}
    
	/**
	 * 【次のXX件→】
	 */
	public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
		SateiForm form = (SateiForm)appContext.getActionForm();
        form.setNextList();
		return GS.OS6101;
	}

	/**
	 * 【←前のXX件】
	 */
	public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
		SateiForm form = (SateiForm)appContext.getActionForm();
        form.setPrevList();
		return GS.OS6101;
	}

}
