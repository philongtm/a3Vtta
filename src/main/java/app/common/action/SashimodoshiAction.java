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

import app.SessionData;
import app.common.bss.SashimodoshiBss;
import app.common.form.SashimodoshiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ2101_差戻先選択 アクションクラス <br>
 */
@Controller
@RequestMapping("/common/sashimodoshi.do")
public class SashimodoshiAction extends AppMenuAction {

	private static final String SASHIMODOSHIFORM = "05SashimodoshiForm";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("gamen_mode","gamen_mode");
		map.put("sashi_phase","sashi_phase");
		map.put("hanyo2","hanyo2");
		map.put("hanyo3","hanyo3");
		map.put("sashimodoshi","sashimodoshi");
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
		SashimodoshiForm form = (SashimodoshiForm)appContext.getSessionActionForm(SASHIMODOSHIFORM);
		if(form == null){
			form = new SashimodoshiForm();
			appContext.setSessionActionForm(SASHIMODOSHIFORM,form);
		}
		//appContextのActionFormを上書き
	    appContext.setActionForm(form);
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        bss.execute();

		return GS.OZ2101;
	}
	
	/**
	 * 【差戻種別ラジオボタン処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object gamen_mode(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        bss.gamen_mode();
		return GS.OZ2101;
	}
	
	/**
	 * 【差戻フェーズセレクトボックス処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object sashi_phase(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        bss.sashi_phase();
		return GS.OZ2101;
	}
	
	/**
	 * 【汎用２セレクトボックス処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object hanyo2(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        bss.hanyo2();
		return GS.OZ2101;
	}
	
	/**
	 * 【汎用３セレクトボックス処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object hanyo3(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        bss.hanyo3();
		return GS.OZ2101;
	}
	
	/**
	 * 【差戻処理】
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object sashimodoshi(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
	    SashimodoshiBss bss = new SashimodoshiBss(appContext);
        if(!bss.doSashimodoshi()){
    		return GS.OZ2101;
        }
        
        SessionData cmnData = appContext.getCMN();
        String senimotoGamenid = null;
        String rtnGamenid = null;
		//遷移先画面を判定
       	senimotoGamenid = cmnData.getReturn_gamenId();
        
		if(senimotoGamenid.equals(GS.OB1102)) {
		    app.tairyu.action.IchiranAction acc = new app.tairyu.action.IchiranAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OB1101;
		} else if(senimotoGamenid.equals(GS.OB1105)) {
		    app.tairyu.action.SyoninAction acc = new app.tairyu.action.SyoninAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OB1104;
		} else if(senimotoGamenid.equals(GS.OB2101)) {
		    app.tairyu.action.SenteiAction acc = new app.tairyu.action.SenteiAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OB2101;
		} else if(senimotoGamenid.equals(GS.OB2104)) {
		    app.tairyu.action.SenteisyoninAction acc = new app.tairyu.action.SenteisyoninAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OB2104;
		} else if(senimotoGamenid.equals(GS.OC1102) || senimotoGamenid.equals(GS.OC1103) || senimotoGamenid.equals(GS.OC1104)) {
		    app.satei.action.IchiranAction acc = new app.satei.action.IchiranAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OC1101;
		} else if(senimotoGamenid.equals(GS.OC1107)) {
		    app.satei.action.SyoninAction acc = new app.satei.action.SyoninAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OC1106;
		} else if(senimotoGamenid.equals(GS.OD1102)) {
		    app.hikiate.action.IchiranAction acc = new app.hikiate.action.IchiranAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OD1101;
		} else if(senimotoGamenid.equals(GS.OS6102)) {
		    app.syokai.action.SateiAction acc = new app.syokai.action.SateiAction();
		    acc.appReExecute(appContext);
		    rtnGamenid = GS.OS6101;
		} else if(senimotoGamenid.equals(GS.OS3105)) {
			app.system.action.KureemuSyoninAction acc = new app.system.action.KureemuSyoninAction();
			acc.appReExecute(appContext);
		    rtnGamenid = GS.OS3104;
		} else {
			app.login.action.MenuAction app = new app.login.action.MenuAction();
		    app.appReExecute(appContext);
		    rtnGamenid = GS.OS2101;
		}
		cmnData.setSyosai_returnId(GS.EMPTY_CHARCTER);
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
        SessionData cmnData = appContext.getCMN();
        String rtnGamenid = null;
		//遷移先画面を判定
        if(GS.EMPTY_CHARCTER.equals(cmnData.getSyosai_returnId())){
        	rtnGamenid = cmnData.getReturn_gamenId();
        }else{
        	rtnGamenid = cmnData.getSyosai_returnId();
        }
        
		if(rtnGamenid.equals(GS.OB1102)) {
		    app.tairyu.action.TorokuAction acc = new app.tairyu.action.TorokuAction();
		    acc.appReExecute(appContext);
		} else if(rtnGamenid.equals(GS.OB1105)) {
		    app.tairyu.action.SyoninsyosaiAction acc = new app.tairyu.action.SyoninsyosaiAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OB2102)) {
		    app.tairyu.action.SenteisyosaiAction acc = new app.tairyu.action.SenteisyosaiAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1102)) {
		    app.satei.action.TorokuAction acc = new app.satei.action.TorokuAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1103)) {
		    app.satei.action.KubunAction acc = new app.satei.action.KubunAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1104)) {
		    app.satei.action.HikiateAction acc = new app.satei.action.HikiateAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OC1107)) {
		    app.satei.action.SyoninSyosaiAction acc = new app.satei.action.SyoninSyosaiAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OD1102)) {
		    app.hikiate.action.KakuninAction acc = new app.hikiate.action.KakuninAction();
		    acc.appReExecute(appContext);
		} else if(rtnGamenid.equals(GS.OD1104)) {
		    app.hikiate.action.HikiateSyoninSyosaiAction acc = new app.hikiate.action.HikiateSyoninSyosaiAction();
		    acc.appReExecute(appContext);
		} else if(rtnGamenid.equals(GS.OS6102)) {
		    app.syokai.action.SateisyosaiAction acc = new app.syokai.action.SateisyosaiAction();
		    acc.appExecute(appContext);
		} else if(rtnGamenid.equals(GS.OS3105)) {
			app.system.action.KureemuSyoninSyosaiAction acc = new app.system.action.KureemuSyoninSyosaiAction();
			acc.appExecute(appContext);
		} else {
			app.login.action.MenuAction app = new app.login.action.MenuAction();
		    app.appReExecute(appContext);
			rtnGamenid = GS.OS2101;
		}
		cmnData.setSyosai_returnId(GS.EMPTY_CHARCTER);
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