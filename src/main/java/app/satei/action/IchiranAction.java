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
import app.satei.bss.IchiranBss;
import app.satei.form.IchiranForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OC1101_査定_対象先一覧 アクションクラス <br>
 */
@Controller
@RequestMapping("/satei/ichiran.do")
public class IchiranAction extends AppMenuAction {

	private static final String ICHIRANFORM			= "02IchiranForm";
	private static final String TANTO					= "tanto";
	private static final String SORT_ITEM				= "sort_item";
	private static final String SORT_ORDER			= "sort_order";
	private static final String ALLMOGITORI			= "allMogitori";
	private static final String MOGITORI				= "mogitori";
	private static final String SATEIKI				= "sateiki";
	private static final String SHOW					= "show";
	private static final String KUBUNHANTEI_GAMEN		= "2";
	private static final String HIKIATEHANTEI_GAMEN	= "3";

    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(TANTO,TANTO);
		map.put(SORT_ITEM,SORT_ITEM);
		map.put(SORT_ORDER,SORT_ORDER);
        map.put(SHOW,SHOW);
        map.put(ALLMOGITORI,ALLMOGITORI);
        map.put(MOGITORI,MOGITORI);
        map.put(SATEIKI,SATEIKI);
		return map;
	}
	
	/**
	 * 【担当ラジオボタン処理】<br>
	 */
	public Object tanto(AppContext appContext) throws Exception {	
        //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        bss.execute();
        return appContext.getActionForm().toString();
	}

	/**
	 * 【ソートセレクトボックス処理】<br>
	 */
	public Object sort_item(AppContext appContext) throws Exception {	
        //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        bss.execute();
        return appContext.getActionForm().toString();
	}
	public Object sort_order(AppContext appContext) throws Exception {	
        //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        bss.execute();
        return appContext.getActionForm().toString();
	}

	/**
	 * 【査定期セレクトボックス処理】<br>
	 */
	public Object sateiki(AppContext appContext) throws Exception {	
        //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        bss.execute();
        return appContext.getActionForm().toString();
	}

	/**
	 * 【画面初期表示処理(メニューリンクから遷移時)】<br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

		//appContextのActionFormを上書き
		IchiranForm form = new IchiranForm();
        appContext.setActionForm(form);

		//機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();   

	    //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);       	    
        bss.executeInit();

        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(ICHIRANFORM,form);

        return form.toString();
	}
       
    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception{

    	//session御掃除
        appContext.removeActionFormExcept(ICHIRANFORM);
        //sessionからActionForm取得
        IchiranForm form = (IchiranForm)appContext.getSessionActionForm(ICHIRANFORM);

        //appContextのActionFormを上書き
        appContext.setActionForm(form);

        //機能共通セッションの取引先情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_tori_bean();
        
        //ビジネスロジック実行
        IchiranBss bss = new IchiranBss(appContext);            
        bss.execute();
        
        //前回のページを表示
        form.setPager(form.getId() + 1);
        
        return form.toString();
    }
	
	/**
	 * 【表示件数セレクトボックス処理】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object show(AppContext appContext) throws Exception {
		//表示件数の変更
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setPager();
	    return form.toString();
	}
		
	/**
	 * 【一括もぎ取りボタン実行処理】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object allMogitori(AppContext appContext) throws Exception {

        //ビジネスロジック実行
		IchiranBss bss = new IchiranBss(appContext);  

		//もぎ取りチェックボックスが全てオフの場合、エラーダイアログを表示し処理終了
        if (!bss.isMogitoriCheckBox()){
            appContext.setMsgCode(GL.ERR_CHECKTAKE);
            return appContext.getActionForm().toString();
        }
        bss.doAllMogitori();
        bss.execute();
        
	    return appContext.getActionForm().toString();
	}
	
	/**
	 * 【←前のXX件】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setPrevList();
	    return form.toString();
	}
	
	/**
	 * 【次のXX件→】<br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//表示部分の変更をListオブジェクトに設定
		IchiranForm form = (IchiranForm)appContext.getActionForm();
		form.setNextList();
	    return form.toString();
	}
    
    /**
     * 【勘定先CDリンク処理】<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object mogitori(AppContext appContext) throws Exception {

        //クリックされた勘定先情報を機能共通セッションに格納
    	IchiranForm form = (IchiranForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        TorihikisakiBean toriBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
        
        //ビジネスロジック実行
		IchiranBss bss = new IchiranBss(appContext);  
        if(!bss.doMogitori(toriBean)){
        	return form.toString();
        }else if(toriBean.getSatei_toroku_gamen().equals(KUBUNHANTEI_GAMEN)){
            cmnData.setTori_bean(toriBean);
            //OC1103_査定_取引先区分判定に遷移
            KubunAction acc = new KubunAction();
            acc.appExecute(appContext);
    	    return GS.OC1103;
        }else if(toriBean.getSatei_toroku_gamen().equals(HIKIATEHANTEI_GAMEN)){
            cmnData.setTori_bean(toriBean);
            //OC1104_査定_引当金判定に遷移
            HikiateAction acc = new HikiateAction();
            acc.appExecute(appContext);
    	    return GS.OC1104;
        }else{
            cmnData.setTori_bean(toriBean);
            //OC1102_査定_取引先概要に遷移
            TorokuAction acc = new TorokuAction();
            acc.appExecute(appContext);
    	    return GS.OC1102;
        }
    }
}
