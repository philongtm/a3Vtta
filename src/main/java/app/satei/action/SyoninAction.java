/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.satei.bss.SyoninBss;
import app.satei.form.SyoninForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OC1104_査定_承認一覧 アクションクラス <br>
 */
@Controller
@RequestMapping("/satei/syonin.do")
public class SyoninAction extends AppMenuAction {

	private static final String SYONINFORM		= "02SyoninForm";
	private static final String SHOW				= "show";
	private static final String SYONIN			= "syonin";
	private static final String IKATU_SYONIN		= "ikatu_syonin";
	private static final String LINK_CLICK		= "link_click";

    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(SHOW,SHOW);
		map.put(SYONIN,SYONIN);
        map.put(IKATU_SYONIN,IKATU_SYONIN);
        map.put(LINK_CLICK,LINK_CLICK);
		return map;
	}
	
	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    //appContextのActionFormを上書き
		SyoninForm form = new SyoninForm();
        appContext.setActionForm(form);

		//機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   

	    //ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);       	    
        bss.executeInit();

        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SYONINFORM,form);

        return form.toString();
	}
       
    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception{

    	//session御掃除
        appContext.removeActionFormExcept(SYONINFORM);
        //sessionからActionForm取得
        SyoninForm form = (SyoninForm)appContext.getSessionActionForm(SYONINFORM);

        //appContextのActionFormを上書き
        appContext.setActionForm(form);

        //機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        
        //ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);            
        bss.execute();
        
        //前回のページを表示
        form.setPager(form.getId() + 1);
        
        return form.toString();
    }
	
	/**
	 * 【表示件数セレクトボックス処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object show(AppContext appContext) throws Exception {
		//表示件数の変更
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setPager();
	    return form.toString();
	}
	
	/**
	 * 【承認実行ボタン押し処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object syonin(AppContext appContext) throws Exception {

		//ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);

        //承認チェックボックスが全てオフの場合、エラーダイアログを表示し処理終了
        if (!bss.isSyouninCheckBox()){
            appContext.setMsgCode(GL.ERR_INPUT,GL.OC1106_SYONIN);
            return appContext.getActionForm().toString();
        }
        
        //承認処理
        bss.doSyonin();
        
	    return appContext.getActionForm().toString();
	}
	
	/**
	 * 【一括承認チッェクボックス実行処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object ikatu_syonin(AppContext appContext) throws Exception {

        //ビジネスロジック実行
        SyoninBss bss = new SyoninBss(appContext);  
        bss.doIkatuSyonin();
        
	    return appContext.getActionForm().toString();
	}
	
	/**
	 * 【←前のXX件】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setPrevList();
	    return form.toString();
	}
	
	/**
	 * 【次のXX件→】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		SyoninForm form = (SyoninForm)appContext.getActionForm();
		form.setNextList();
	    return form.toString();
	}
    
    /**
     * 【勘定先CDリンク処理】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object link_click(AppContext appContext) throws Exception {

        //クリックされた勘定先情報を機能共通セッションに格納
        SyoninForm form = (SyoninForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setTori_bean((TorihikisakiBean)form.getAr_meisai().get(form.getId()));

        //OC1107_査定_承認に遷移
        SyoninSyosaiAction acc = new SyoninSyosaiAction();
        acc.appExecute(appContext);
	    return GS.OC1107;
    }
}
