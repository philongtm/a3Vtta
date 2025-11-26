/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.action;

import app.common.action.HikiatekinHanteiSyokaiAction;
import app.common.action.RyuhoSaimuSyokaiAction;
import app.common.action.SaikenMeisaiSyokaiAction;
import app.common.action.SashimodoshiAction;
import app.common.action.TenpuSyokaiAction;
import app.common.action.TorihikisakiGaiyoSyokaiAction;
import app.common.action.TorihikisakiKubunHanteiSyokaiAction;
import app.satei.bss.SyoninSyosaiBss;
import app.satei.form.SyoninSyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OC1107_査定_承認 アクションクラス <br>
 */
public class SyoninSyosaiAction extends AppMenuAction {

	private static final String SYONINSYOSAIFORM		= "02SyoninSyosaiForm";
	private static final String SYONINEXECUTE			= "syoninExecute";
	private static final String SASHIMODOSHI			= "sashimodoshi";
	private static final String BACK					= "back";
	private static final String SAIKEN_MEISAI			= "saiken_meisai";
	private static final String TORI_GAIYO			= "tori_gaiyo";
	private static final String TORI_KBN				= "tori_kbn";
	private static final String HIKIATE_HANTEI		= "hikiate_hantei";
	private static final String RYUHO_SAIMU			= "ryuho_saimu";
	private static final String TENP					= "tenpu";

	//タブ値
	private static final String SAIKEN_MEISAI_TAB		= "1";
	private static final String TORI_GAIYO_TAB		= "2";
	private static final String TORI_KBN_TAB			= "3";
	private static final String HIKIATE_HANTEI_TAB	= "4";
	private static final String RYUHO_SAIMU_TAB		= "5";
	
    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(SYONINEXECUTE,SYONINEXECUTE);
		map.put(SASHIMODOSHI,SASHIMODOSHI);
        map.put(BACK,BACK);
        map.put(SAIKEN_MEISAI,SAIKEN_MEISAI);
        map.put(BACK,BACK);
        map.put(TORI_GAIYO,TORI_GAIYO);
        map.put(TORI_KBN,TORI_KBN);
        map.put(HIKIATE_HANTEI,HIKIATE_HANTEI);
        map.put(RYUHO_SAIMU,RYUHO_SAIMU);
        map.put(TENP,TENP);
		return map;
	}
	
	/**
	 * 【画面初期表示処理】<br>
	 */
	public Object appExecute(AppContext appContext) throws Exception{	

        //sessionからActionForm取得
        SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getSessionActionForm(SYONINSYOSAIFORM);
        
        if(form == null){
        	form = new SyoninSyosaiForm();
            //appContextのActionFormを上書き
            appContext.setActionForm(form);
        }else{
            return GS.OC1107;
        }
        appContext.getCMN().setTab_riyou_gamenId(form.toString());

	    //OZ6105_債権明細照会タブ作成
        SaikenMeisaiSyokaiAction saiken_meisai_acc = new SaikenMeisaiSyokaiAction();
        saiken_meisai_acc.appExecute(appContext);
	    //OZ6102_取引先概要照会タブ作成
        TorihikisakiGaiyoSyokaiAction torihikisaki_gaiyo_acc = new TorihikisakiGaiyoSyokaiAction();
		torihikisaki_gaiyo_acc.appExecute(appContext);
	    //OZ6103_取引先区分判定照会タブ作成
	    TorihikisakiKubunHanteiSyokaiAction torihikisaki_kubunhantei_acc = new TorihikisakiKubunHanteiSyokaiAction();
	    torihikisaki_kubunhantei_acc.appExecute(appContext);
	    //OZ6104_引当金判定照会タブ作成
	    HikiatekinHanteiSyokaiAction hikiatekin_hantei_acc = new HikiatekinHanteiSyokaiAction();
	    hikiatekin_hantei_acc.appExecute(appContext);
	    //OZ6107_債務明細照会タブ作成
	    RyuhoSaimuSyokaiAction ryuhosaimu_acc = new RyuhoSaimuSyokaiAction();
	    ryuhosaimu_acc.appExecute(appContext);

        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SYONINSYOSAIFORM,form);

        return GS.OC1107;
	}
	
	/**
	 * 【戻るボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {

		//OC1106_査定_承認一覧に遷移
		SyoninAction acc = new SyoninAction();
		acc.appReExecute(appContext);
		
		return GS.OC1106;	  
	}
	
	/**
	 * 債権明細タブ押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object saiken_meisai(AppContext appContext){
		SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getActionForm();
		form.setTabValue(SAIKEN_MEISAI_TAB);
		return GS.OC1107;
	}
	
	/**
	 * 取引先概要タブ押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object tori_gaiyo(AppContext appContext){
		SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getActionForm();
		form.setTabValue(TORI_GAIYO_TAB);
		return GS.OC1107;
	}
	
	/**
	 * 取引先区分判定タブ押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object tori_kbn(AppContext appContext){
		SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getActionForm();
		form.setTabValue(TORI_KBN_TAB);
		return GS.OC1107;
	}
	
	/**
	 * 引当金判定タブ押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object hikiate_hantei(AppContext appContext){
		SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getActionForm();
		form.setTabValue(HIKIATE_HANTEI_TAB);
		return GS.OC1107;
	}
	
	/**
	 * 留保債務タブ押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object ryuho_saimu(AppContext appContext){
		SyoninSyosaiForm form = (SyoninSyosaiForm)appContext.getActionForm();
		form.setTabValue(RYUHO_SAIMU_TAB);
		return GS.OC1107;
	}

	/**
	 * 差戻しボタン押下時の処理
	 * @param appContext
	 * @return
	 */
	public Object sashimodoshi(AppContext appContext) throws Exception{

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		SashimodoshiAction acc = new SashimodoshiAction();
		acc.appExecute(appContext);

		return GS.OZ2101;
	}

	/**
	 * 添付照会
	 * @param appContext
	 * @return
	 */
	public Object tenpu(AppContext appContext) throws Exception {

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		TenpuSyokaiAction acc = new TenpuSyokaiAction();
		acc.appExecute(appContext);
	    
		return GS.OZ1102;
	}
	
	/**
	 * 【承認実行ボタン押し処理】 <br>
	 * 
	 * @param appContext
	 * @return 画面ID
	 * @throws Exception
	 */
	public Object syoninExecute(AppContext appContext) throws Exception {

		//ビジネスロジック実行
        SyoninSyosaiBss bss = new SyoninSyosaiBss(appContext);
        bss.doSyonin();
        
        SyoninAction acc = new SyoninAction();
        acc.appReExecute(appContext);
        
	    return GS.OC1106;
	}
	
	/**
	 * 【未使用メソッド】 <br>
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    return GS.OC1106;
	}
	public Object nextY(AppContext appContext) throws Exception {
	    return GS.OC1106;
	}
}
