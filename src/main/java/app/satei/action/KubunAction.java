/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/11		SSC				案件No.D13493 改善対応
******************************************************************************/
package app.satei.action;

import app.common.action.SashimodoshiAction;
import app.common.action.TensouAction;
import app.satei.bss.KubunBss;
import app.satei.form.KubunForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OC1103_査定_取引先区分 アクションクラス<br>
 */
@Controller
@RequestMapping("/satei/kubun.do")
public class KubunAction extends AppMenuAction {

	private static final String KUBUNFORM				= "02KubunForm";
	private static final String TOHEAD				= "toHead";
	private static final String FORWARD				= "forward";
	private static final String SENDBACK				= "sendBack";
	private static final String FIRSTPRESERVE			= "firstPreserve";
	private static final String NEXT					= "next";
	private static final String SEIJO_YOTYUI_TAB		= "seijo_yotyui";
	private static final String KASHIDAORE_TAB		= "kashidaore";
	private static final String HASAN_TAB				= "hasan";
	private static final String BACK					= "back";
	private static final String CHANGETORI			= "changetori";

	//タブ選択値
	private static final String SEIJOYOTYUI_TABVAL	= "1";
	private static final String KASHIDAORE_TABVAL		= "2";
	private static final String HASANKOUSEI_TABVAL	= "3";

	//入力履歴登録判定用
	private static final boolean HOZON_TRUE		= true;
	private static final boolean HOZON_FALSE		= false;

	//取引先区分選択値
	private static final String SEIJOYOTYUI_SELECTVAL	= "1";
	private static final String KASHIDAORE_SELECTVAL			= "2";
	private static final String HASANKOUSEI_SELECTVAL		= "3";

	
    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(TOHEAD,TOHEAD);
        map.put(FORWARD,FORWARD);
        map.put(SENDBACK,SENDBACK);
        map.put(FIRSTPRESERVE,FIRSTPRESERVE);
        map.put(NEXT,NEXT);
        map.put(SEIJO_YOTYUI_TAB,SEIJO_YOTYUI_TAB);
        map.put(KASHIDAORE_TAB,KASHIDAORE_TAB);
        map.put(HASAN_TAB,HASAN_TAB);
        map.put(BACK,BACK);
        map.put(CHANGETORI, CHANGETORI);
		return map;
	}
	
	/**
	 * 【メニューへボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {
		//OC1101_査定_対象先一覧に遷移
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		return GS.OC1101;
	}

	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {

		//sessionからActionForm取得
        KubunForm form = (KubunForm)appContext.getSessionActionForm(KUBUNFORM);

        if(form == null){
        	form = new KubunForm();
            //appContextのActionFormを上書き
            appContext.setActionForm(form);
        }else{
    	    return form.toString();
        }
        
        //以下初期表示処理
		KubunBss bss = new KubunBss(appContext);
        bss.execInit(appContext);
        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KUBUNFORM,form);
	    return form.toString();
	}
	
	/**
	 * 【未使用メソッド】
	 */
	public Object prevX(AppContext appContext) throws Exception {
		return appContext.getActionForm().toString();
	}
	public Object nextY(AppContext appContext) throws Exception {
		return appContext.getActionForm().toString();
	}
	
	/**
	 * 【先頭へボタン処理】
	 */
	public Object toHead(AppContext appContext) throws Exception {

		KubunBss bss = new KubunBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//取引先区分設定
		bss.setToriKbn();
		
		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//OC1102_査定_取引先概要に遷移
		TorokuAction acc = new TorokuAction();
		acc.appExecute(appContext);

		return GS.OC1102;
	}

	/**
	 * 【転送ボタン処理】
	 */
	public Object forward(AppContext appContext) throws Exception {

		KubunBss bss = new KubunBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//取引先区分設定
		bss.setToriKbn();

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);
		
		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OZ3101_転送先選択に遷移
		TensouAction acc = new TensouAction();
		acc.appExecute(appContext);

		return GS.OZ3101;
	}
	
	/**
	 * 【差戻ボタン処理】
	 */
	public Object sendBack(AppContext appContext) throws Exception {

		KubunBss bss = new KubunBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//取引先区分設定
		bss.setToriKbn();

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//OZ2101_差戻先選択に遷移
		SashimodoshiAction acc = new SashimodoshiAction();
		acc.appExecute(appContext);

		return GS.OZ2101;
	}
	
	/**
	 * 【一次保存ボタン処理】
	 */
	public Object firstPreserve(AppContext appContext) throws Exception {

		KubunBss bss = new KubunBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//取引先区分設定
		bss.setToriKbn();

		//保存処理(入力履歴登録)
		bss.execHozon(HOZON_TRUE);

	    return appContext.getActionForm().toString();
	}

	/**
	 * 【次画面ボタン処理】
	 */
	public Object next(AppContext appContext) throws Exception {

		KubunBss bss = new KubunBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//取引先区分設定
		bss.setToriKbn();

		//保存処理(入力履歴登録)
		bss.execHozon(HOZON_TRUE);

		//OC1104_査定_引当金判定に遷移
		HikiateAction acc = new HikiateAction();
		acc.appExecute(appContext);

	    return GS.OC1104;
	}

	/**
	 * 【正常・要注意先タブ選択処理】
	 */
	public Object seijo_yotyui(AppContext appContext) throws Exception {

		KubunForm form = (KubunForm)appContext.getActionForm();
		
		//チェックがどちらもOFFの場合、正常先にチェック
		if(!(GS.ON.equals(form.getChkFlgSeijo())) && !(GS.ON.equals(form.getChkFlgYochui()))){
			form.setChkFlgSeijo(GS.ON);
		}

		//カレントタブ設定
		form.setCurrentTab(SEIJOYOTYUI_TABVAL);
		
		//一般債権
		form.setKbnSaiken(GS.IPPAN_SAIKEN);
		
	    return form.toString();
	}
	
	/**
	 * 【貸倒懸念先タブ選択処理】
	 */
	public Object kashidaore(AppContext appContext) throws Exception {

		KubunForm form = (KubunForm)appContext.getActionForm();
		
		//カレントタブ設定
		form.setCurrentTab(KASHIDAORE_TABVAL);

		//貸倒懸念債権
		form.setKbnSaiken(GS.KASIDAORE_SAIKEN);
		
	    return form.toString();
	}
	
	/**
	 * 【破産更生先タブ選択処理】
	 */
	public Object hasan(AppContext appContext) throws Exception {

		KubunForm form = (KubunForm)appContext.getActionForm();
		
		//破産更生債権
		form.setKbnSaiken(GS.HASANKOUSEI_SAIKEN);
		
		//カレントタブ設定
		form.setCurrentTab(HASANKOUSEI_TABVAL);

	    return form.toString();
	}
	
	/**
	 * 【取引先区分選択処理】
	 */
	public Object changetori(AppContext appContext) throws Exception {
		
		KubunForm form = (KubunForm)appContext.getActionForm();
		Object rtn;
		
		if (SEIJOYOTYUI_SELECTVAL.equals(form.getKbnToriSelect())) {
			rtn = seijo_yotyui(appContext);
		} else if (KASHIDAORE_SELECTVAL.equals(form.getKbnToriSelect())) {
			rtn = kashidaore(appContext);
		} else {
			rtn = hasan(appContext);
		}
		
		return rtn;
	}

}