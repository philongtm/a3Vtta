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
import app.syokai.bss.SincyokuBss;
import app.syokai.form.SincyokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS6103_進捗状況照会 アクションクラス <br>
 */
@Controller
@RequestMapping("/syokai/sincyoku.do")
public class SincyokuAction extends AppMenuAction {
	
	private static final String SINCYOKUFORM = "03SincyokuForm";
	
	/**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("detail","detail");
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
        
        // appContextのActionFormを上書き
    	SincyokuForm form = new SincyokuForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokuBss bss = new SincyokuBss(appContext);
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SINCYOKUFORM, form);
        return result;
    }

    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】
     */
    public Object appReExecute(AppContext appContext) throws Exception {  
    	
        appContext.removeActionFormExcept(SINCYOKUFORM);
        // sessionからActionForm取得
        SincyokuForm form = (SincyokuForm)appContext.getSessionActionForm(SINCYOKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokuBss bss = new SincyokuBss(appContext);            
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
     * 汎用1アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object change1(AppContext appContext) throws Exception {  
    	SincyokuForm form = (SincyokuForm)appContext.getSessionActionForm(SINCYOKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokuBss bss = new SincyokuBss(appContext);
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
    	SincyokuForm form = (SincyokuForm)appContext.getSessionActionForm(SINCYOKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokuBss bss = new SincyokuBss(appContext);
        String result = bss.doChange2();
        
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
    	SincyokuForm form = (SincyokuForm)appContext.getSessionActionForm(SINCYOKUFORM);
    	SessionData cmnData = appContext.getCMN();
    	// OS6104_業務フローパターンメンテナンス_登録に遷移
    	SincyokusyosaiAction acc = new SincyokusyosaiAction();
    	cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));
		acc.appExecute(appContext);
		return GS.OS6104;
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
    	SincyokuForm form = (SincyokuForm)appContext.getSessionActionForm(SINCYOKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokuBss bss = new SincyokuBss(appContext);
        String result = bss.doSearch();
        
        return result;
    }
    
    /**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		SincyokuForm form = (SincyokuForm)appContext.getActionForm();
		form.setPager();
	    return GS.OS6103;
	}

	/**
	 * 【次のXX件→】
	 */
	public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
		SincyokuForm form = (SincyokuForm)appContext.getActionForm();
        form.setNextList();
		return GS.OS6103;
	}

	/**
	 * 【←前のXX件】
	 */
	public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
		SincyokuForm form = (SincyokuForm)appContext.getActionForm();
        form.setPrevList();
		return GS.OS6103;
	}

}
