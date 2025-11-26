/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
003		2016/03/25		SSC				案件No.BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.common.action;

import app.common.bss.TensouBss;
import app.common.form.TensouForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OZ3101_転送先選択 アクションクラス <br>
 */
public class TensouAction extends AppMenuAction {

	private static final String TENSOUFORM = "05TensouForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("tensou","tensou");
		map.put("hanyo1","hanyo1");
		map.put("hanyo2","hanyo2");
		map.put("back","back");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
		// アクションフォームを取得
		TensouForm form = (TensouForm)appContext.getSessionActionForm(TENSOUFORM);
		if(form == null){
			form = new TensouForm();
			appContext.setSessionActionForm(TENSOUFORM,form);
		}
		//appContextのActionFormを上書き
	    appContext.setActionForm(form);
	    // ビジネスロジック実行
	    TensouBss bss = new TensouBss(appContext);  
        bss.execute();

		return GS.OZ3101;
	}
	
	/**
	 * 【汎用１セレクトボックス処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object hanyo1(AppContext appContext) throws Exception {
    	TensouForm form = (TensouForm)appContext.getSessionActionForm(TENSOUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    // ビジネスロジック実行
		TensouBss bss = new TensouBss(appContext);  
        bss.hanyo1();
		return GS.OZ3101;
	}

	/**
	 * 【汎用２セレクトボックス処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object hanyo2(AppContext appContext) throws Exception {
    	TensouForm form = (TensouForm)appContext.getSessionActionForm(TENSOUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    // ビジネスロジック実行
		TensouBss bss = new TensouBss(appContext);  
        bss.hanyo2();
		return GS.OZ3101;
	}

	/**
	 * 【転送処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object tensou(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
		TensouBss bss = new TensouBss(appContext);  
        if(!bss.doTensou()){
    		return GS.OZ3101;
        }

		//遷移先画面を判定
        String rtnGamenid = null;
        String senimotoGamenid = appContext.getCMN().getReturn_gamenId();
        
		if(senimotoGamenid.equals(GS.OB1102)) {
		    app.tairyu.action.IchiranAction acc = new app.tairyu.action.IchiranAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OB1101;
		} else if(senimotoGamenid.equals(GS.OC1102) || senimotoGamenid.equals(GS.OC1103) || senimotoGamenid.equals(GS.OC1104)) {
		    app.satei.action.IchiranAction acc = new app.satei.action.IchiranAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OC1101;
		} else if(senimotoGamenid.equals(GS.OD1102) || senimotoGamenid.equals(GS.OD1105)) {
			app.hikiate.action.IchiranAction acc = new app.hikiate.action.IchiranAction();
			acc.appReExecute(appContext);
		    rtnGamenid = GS.OD1101;
		} else {
			app.login.action.MenuAction app = new app.login.action.MenuAction();
		    app.appReExecute(appContext);
		    rtnGamenid = GS.OS2101;
		}
        return rtnGamenid;
	}

	/**
	 * 【戻るボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object back(AppContext appContext) throws Exception {
		//遷移先画面を判定
        String rtnGamenid = appContext.getCMN().getReturn_gamenId();
		if(rtnGamenid.equals(GS.OB1102)) {
		    app.tairyu.action.TorokuAction acc = new app.tairyu.action.TorokuAction();
		    acc.appReExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1102)) {
		    app.satei.action.TorokuAction acc = new app.satei.action.TorokuAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1103)) {
		    app.satei.action.KubunAction acc = new app.satei.action.KubunAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1104)) {
		    app.satei.action.HikiateAction acc = new app.satei.action.HikiateAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OD1102)) {
		    app.hikiate.action.KakuninAction acc = new app.hikiate.action.KakuninAction();
		    acc.appReExecute(appContext);
		} else if(rtnGamenid.equals(GS.OD1105)) {
		    app.hikiate.action.KensyoAction acc = new app.hikiate.action.KensyoAction();
		    acc.appExecute(appContext);
		} else {
			rtnGamenid = GS.OS2101;
			app.login.action.MenuAction app = new app.login.action.MenuAction();
		    app.appReExecute(appContext);
		}
        return rtnGamenid;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		//空実装
	    return null;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		//空実装
		return null;
	}
}