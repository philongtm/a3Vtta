/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/17		SSC				課題No.102 債権無しの場合の処理追加 
******************************************************************************/

package app.syokai.action;

import app.SessionData;
import app.SessionDataZen;
import app.TorihikisakiBean;
import app.common.action.HikiatekinHanteiSyokaiAction;
import app.common.action.KakuninSyokaiAction;
import app.common.action.KensyoSyokaiAction;
import app.common.action.RyuhoSaimuSyokaiAction;
import app.common.action.SaikenMeisaiSyokaiAction;
import app.common.action.SashimodoshiAction;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TairyuMeisaiAction;
import app.common.action.TenpuSyokaiAction;
import app.common.action.TorihikisakiGaiyoSyokaiAction;
import app.common.action.TorihikisakiKubunHanteiSyokaiAction;
import app.common.form.KensyoSyokaiForm;
import app.hikiate.action.KakuninAction;
import app.hikiate.action.KensyoAction;
import app.print.bss.KakuninExcelBss;
import app.print.bss.SaikenMeisaiExcelBss;
import app.print.bss.SateiSyosaiExcelBss;
import app.print.bss.TairyuExcelbss;
import app.syokai.bss.SateisyosaiBss;
import app.syokai.form.SateisyosaiForm;
import app.system.action.ChampionAction;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.struts.AppPagerActionForm;
import common.util.Function;
import common.util.HikiatekinExcel;
import common.util.SaikenExcel;
import common.util.SateiSyosaiExcel;
import common.util.TairyuExcel;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS6102_査定内容詳細 アクションクラス <br>
 */
@Controller
@RequestMapping("/syokai/sateisyosai.do")
public class SateisyosaiAction extends AppMenuAction {

	private static final String SATEISYOSAIFORM = "03SateisyosaiForm"; // フォーム名
	
    private static final int TAB_TORIHIKI_GAIYO 			= 1;				// 取引先概要タブ
    private static final int TAB_SAIKEN_KUBUN 			= 2;				// 取引先・債権区分判定タブ
    private static final int TAB_HIKIATE_HANTEI 			= 3;				// 引当金判定タブ
    private static final int TAB_SAIKEN_MEISAI 			= 4;				// 債権明細タブ
    private static final int TAB_RYUHO_SAIMU 			= 5;				// 留保債務タブ
    private static final int TAB_TAIRYU_SAIKEN 			= 6;				// 滞留債権明細タブ
    private static final int TAB_HIKIATE_KAKUNIN 		= 7;				// 引当金確認タブ
    private static final int TAB_HIKIATE_KENSYO 			= 8;				// 引当金検証タブ
    //TODO path
    private static final String PATH_TORIHIKI_GAIYO 		= "../common/torihikisaki_gaiyo.jsp";			// 取引先概要タブ
    private static final String PATH_SAIKEN_KUBUN 		= "../common/torihikisaki_kubun_hantei.jsp";	// 取引先・債権区分判定タブ
    private static final String PATH_HIKIATE_HANTEI 		= "../common/hikiatekin_hantei.jsp";			// 引当金判定タブ
    private static final String PATH_SAIKEN_MEISAI 		= "../common/saiken_meisai.jsp";				// 債権明細タブ
    private static final String PATH_RYUHO_SAIMU 			= "../common/ryuho_saimu.jsp";					// 留保債務タブ
    private static final String PATH_TAIRYU_SAIKEN 		= "../common/tairyu_meisai.jsp";				// 滞留債権明細タブ
    private static final String PATH_HIKIATE_KAKUNIN 		= "../common/kakunin.jsp";						// 引当金確認タブ
    private static final String PATH_HIKIATE_KENSYO 		= "../common/kensyo.jsp";				// 引当金検証タブ
    
    //IT113対応
    private static final String FORMNM_TORIHIKISAKIGAIYOSYOKAIFORM		= "TorihikisakiGaiyoSyokaiForm";//取引先概要照会タブ
    private static final String FORMNM_TORIHIKISAKIKUBUNHANTEISYOKAIFORM	= "TorihikisakiKubunHanteiSyokaiForm";//取引先債権区分判定タブ
    private static final String FORMNM_HIKIATEKINHANTEISYOKAIFORM			= "HikiatekinHanteiSyokaiForm";//引当金判定タブ
    private static final String FORMNM_SAIKENMEISAIFORM					= "SaikenMeisaiSyokaiForm";//債権明細タブ
    private static final String FORMNM_RYUHOSAIMUSYOKAIFORM				= "RyuhoSaimuSyokaiForm";//留保債務タブ
    private static final String FORMNM_TAIRYUMEISAIFORM					= "05TairyuMeisaiForm";//滞留債権明細タブ
    private static final String FORMNM_KAKUNINSYOKAIFORM					= "KakuninSyokaiForm";//引当金確認タブ
    private static final String FORMNM_KENSYOFORM							= "HikiatekinKensyoSyokaiForm";//引当金検証タブ
    //IT113ここまで
    
    
    /**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("sasiModosi","sasiModosi");
        map.put("tenpuSansyo","tenpuSansyo");
        map.put("download", "download");
        map.put("back", "back");
        map.put("comment", "comment");
        map.put("phase", "phase");
        map.put("changeTab", "changeTab");
        return map;
    }
    
    /**
     * 【画面初期表示処理】
     */
    public Object appExecute(AppContext appContext) throws Exception {
        
        // セッションスコープから03SincyokusyosaiFormを取得
    	SateisyosaiForm form = (SateisyosaiForm)appContext.getSessionActionForm(SATEISYOSAIFORM);
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
		if (form != null) {
			// 03SincyokusyosaiFormが存在しない場合
			return GS.OS6102;
		}
    	form = new SateisyosaiForm();
        appContext.setActionForm(form);
        // 共)共通タブ利用画面IDに当画面IDを設定する。
        cmnData.setTab_riyou_gamenId(form.toString());
        
        // ビジネスロジック実行
        SateisyosaiBss bss = new SateisyosaiBss(appContext);
        bss.executeInit();
        
        // 該当タブの表示
        this.initCurrentTab(form,appContext);
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SATEISYOSAIFORM, form);
        return GS.OS6102;
    }
    
    //IT113対応
    /**
     * 【該当タブオブジェクト初期化処理】
     */
    private void initTabForm(AppContext appContext)throws Exception  {
    	appContext.removeActionForm(FORMNM_TORIHIKISAKIGAIYOSYOKAIFORM);
    	appContext.removeActionForm(FORMNM_TORIHIKISAKIKUBUNHANTEISYOKAIFORM);
    	appContext.removeActionForm(FORMNM_HIKIATEKINHANTEISYOKAIFORM);
    	appContext.removeActionForm(FORMNM_SAIKENMEISAIFORM);
    	appContext.removeActionForm(FORMNM_RYUHOSAIMUSYOKAIFORM);
    	appContext.removeActionForm(FORMNM_TAIRYUMEISAIFORM);
    	appContext.removeActionForm(FORMNM_KAKUNINSYOKAIFORM);
    	appContext.removeActionForm(FORMNM_KENSYOFORM);
    }
    //IT113ここまで
    
    /**
     * 【該当タブ初期表示処理】
     */
    private void initCurrentTab(SateisyosaiForm form,AppContext appContext)throws Exception  {
    	
        //IT113対応
    	//タブ初期化
    	this.initTabForm(appContext);
        //IT113ここまで
    	
    	// 現在のタブ
    	int tabIndex = form.getCurrentTab();
    	switch (tabIndex) {
		case TAB_TORIHIKI_GAIYO:
			// 取引先概要タブ
			TorihikisakiGaiyoSyokaiAction acc_OZ6102 = new TorihikisakiGaiyoSyokaiAction();
				acc_OZ6102.appExecute(appContext);
				form.setJspPath(PATH_TORIHIKI_GAIYO);
			break;
		case TAB_SAIKEN_KUBUN:
			// 取引先・債権区分判定タブ
			TorihikisakiKubunHanteiSyokaiAction acc_OZ6103 = new TorihikisakiKubunHanteiSyokaiAction();
			acc_OZ6103.appExecute(appContext);
			 	form.setJspPath(PATH_SAIKEN_KUBUN);
			break;
		case TAB_HIKIATE_HANTEI:
			// 引当金判定タブ
			HikiatekinHanteiSyokaiAction acc_OZ6104 = new HikiatekinHanteiSyokaiAction();
			acc_OZ6104.appExecute(appContext);
			form.setJspPath(PATH_HIKIATE_HANTEI);
			break;
		case TAB_SAIKEN_MEISAI:
			// 債権明細タブ
			SaikenMeisaiSyokaiAction acc_OZ6105 = new SaikenMeisaiSyokaiAction();
			acc_OZ6105.appExecute(appContext);
			form.setJspPath(PATH_SAIKEN_MEISAI);
			break;
		case TAB_RYUHO_SAIMU:
			// 留保債務タブ
			RyuhoSaimuSyokaiAction acc_OZ6107 = new RyuhoSaimuSyokaiAction();
			acc_OZ6107.appExecute(appContext);
			form.setJspPath(PATH_RYUHO_SAIMU);
			break;
		case TAB_TAIRYU_SAIKEN:
			// 滞留債権明細タブ
			TairyuMeisaiAction accOZ6101 = new TairyuMeisaiAction();
			accOZ6101.appExecute(appContext);
			form.setJspPath(PATH_TAIRYU_SAIKEN);
			break;
		case TAB_HIKIATE_KAKUNIN:
			// 引当金確認タブ
			KakuninSyokaiAction acc_OZ6106 = new KakuninSyokaiAction();
			acc_OZ6106.appExecute(appContext);
			form.setJspPath(PATH_HIKIATE_KAKUNIN);
			break;
		case TAB_HIKIATE_KENSYO:
			// 引当金検証タブ
			KensyoSyokaiAction acc = new KensyoSyokaiAction();
			acc.appExecute(appContext);
			form.setJspPath(PATH_HIKIATE_KENSYO);
			break;
		default:
			break;
		}
    }
    /**
     * 
     * 差戻アクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object sasiModosi(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
    	// 共)詳細元画面IDに当画面IDを設定
        cmnData.setSyosai_returnId(GS.OS6102);
    	
    	// OZ2101_差戻先選択へ遷移する。
		SashimodoshiAction acc = new SashimodoshiAction();
		acc.appExecute(appContext);
    	return GS.OZ2101;
    }
    
    /**
     * 
     * 添付参照アクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object tenpuSansyo(AppContext appContext) throws Exception {  
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
    	// 共)詳細元画面IDに当画面IDを設定
        cmnData.setSyosai_returnId(GS.OS6102);
		TenpuSyokaiAction acc = new TenpuSyokaiAction();
		acc.appExecute(appContext);
    	
    	// OZ1102_添付内容照会へ遷移する。
    	return GS.OZ1102;
    }
    
    /**
     * 
     * ダウンロードアクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object download(AppContext appContext) throws Exception {
		String systemKbn = appContext.getCMN().getTori_bean().getSystem_kbn();
		String rtnFwd = null;
		if(systemKbn.equals(GS.GSS)){
			//国内版帳票メソッドに飛ばす!!
			rtnFwd = downloadKokunai(appContext);
		}else{
			//海外帳票
			rtnFwd = downloadKaigai(appContext);
		}
		
		return rtnFwd;
    }
    
    /**
     * 
     * 戻るアクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object back(AppContext appContext) throws Exception {
    	// 共)タブ利用画面IDにNULLを設定
		SessionData cmnData = appContext.getCMN();
		cmnData.setTab_riyou_gamenId(GS.EMPTY_CHARCTER);
		cmnData.setSyosai_returnId(GS.EMPTY_CHARCTER);
		TorihikisakiBean toriBeanTaihi = null;
		SateisyosaiForm form = (SateisyosaiForm)appContext.getActionForm();
		// 遷移元画面に遷移する。
		
		if(GS.OS6101.equals(cmnData.getReturn_gamenId())){
			SateiAction acc = new SateiAction();
			acc.appReExecute(appContext);
			return GS.OS6101;
		}else if(GS.OD1105.equals(appContext.getCMN().getReturn_gamenId())){
			//引当金検証の場合
			toriBeanTaihi = new TorihikisakiBean();
			BeanUtils.copyProperties(toriBeanTaihi,form.getTori_taihi());
			cmnData.setTori_bean(toriBeanTaihi);
	        KensyoAction acc = new KensyoAction();
		    acc.appExecute(appContext);
		}else if(GS.OD1102.equals(appContext.getCMN().getReturn_gamenId())){
			//引当金確認の場合
			toriBeanTaihi = new TorihikisakiBean();
			BeanUtils.copyProperties(toriBeanTaihi,form.getTori_taihi());
			cmnData.setTori_bean(toriBeanTaihi);
	        KakuninAction acc = new KakuninAction();
		    acc.appReExecute(appContext);
		}else if(GS.OS5101.equals(appContext.getCMN().getReturn_gamenId())){
			//チャンピオン部メンテの場合
			ChampionAction acc = new ChampionAction();
		    acc.appReExecute(appContext);
		}
		appContext.removeActionForm("03SateisyosaiForm");
		return cmnData.getReturn_gamenId();
    }
    
    /**
     * 
     * コメント表示アクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object comment(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
    	// 共)詳細元画面IDに当画面IDを設定
        cmnData.setSyosai_returnId(GS.OS6102);
    	
    	// コメント表示画面に遷移
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
		acc.appExecute(appContext,appContext.getCMN().getTori_bean().getAnken_no());

    	return GS.OZ4101;
    }
    
    /**
     * 
     * フェーズアクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object phase(AppContext appContext) throws Exception {

        // ビジネスロジック実行
        SateisyosaiBss bss = new SateisyosaiBss(appContext);
        bss.changePhase();

        //IT113対応
        // 該当タブの表示
        this.initCurrentTab((SateisyosaiForm)appContext.getActionForm(),appContext);
        //IT113ここまで
        
		return GS.OS6102;
    }
    
    /**
     * 
     * タブ選択アクション <br>
     * 
     * @param appContext
     * @return 遷移先
     * @throws Exception
     */
    public Object changeTab(AppContext appContext) throws Exception {  
        // セッションスコープから03SincyokusyosaiFormを取得
    	SateisyosaiForm form = (SateisyosaiForm)appContext.getSessionActionForm(SATEISYOSAIFORM);

        // ビジネスロジック実行
        SateisyosaiBss bss = new SateisyosaiBss(appContext);
        bss.changeTab();
        
        // 該当タブの表示
        this.initCurrentTab(form,appContext);
    	
    	return GS.OS6102;
    }
    
	/**
	 * 【次のXX件→】
	 */
	public Object nextY(AppContext appContext) throws Exception {

		return null;
	}

	/**
	 * 【←前のXX件】
	 */
	public Object prevX(AppContext appContext) throws Exception {

		return null;
	}
	
	
	/**
	 * 【ダウンロードボタン処理】
	 */
	private String downloadKaigai(AppContext appContext) throws Exception {
    	// 現在のタブ
    	int tabIndex = ((SateisyosaiForm)appContext.getActionForm()).getCurrentTab();
		String rtnVal = null;
    	
    	switch (tabIndex) {
    	
		case TAB_TAIRYU_SAIKEN:
			System.out.println(3);
			//滞留債権明細タブ
			TairyuExcelbss tairyu_bss = new TairyuExcelbss(appContext);
			tairyu_bss.execute();
			break;
		case TAB_HIKIATE_KAKUNIN:
			//引当金確認タブ
			KakuninExcelBss bss = new KakuninExcelBss(appContext);
			bss.execute();
			break;
		default:
			if(GS.OS6101.equals(Function.trim(appContext.getCMN().getReturn_gamenId()))){
				//債権査定帳票・債権明細帳票・債務明細帳票
				SateiSyosaiExcelBss syosai_bss = new SateiSyosaiExcelBss(appContext);
				syosai_bss.execute();
			}else{
				//債権明細帳票
				SaikenMeisaiExcelBss saiken_bss = new SaikenMeisaiExcelBss(appContext);
				saiken_bss.execute();
			}
			break;
		}
		return rtnVal;
	}

	//以下1.5次版帳票処理↓
	/**
	 * 【ダウンロードボタン処理】
	 */
	private String downloadKokunai(AppContext appContext) throws Exception {
		
		SessionDataZen cmnData = appContext.getCMNZen();
		cmnData.setOrgReturnId(new String(cmnData.getReturnId()));
		cmnData.setReturnId(appContext.getActionForm().toString());
		cmnData.setComLangMode(((AppPagerActionForm)appContext.getActionForm()).getLangMode());
		String rtnVal = null;

		// 現在のタブ
    	int tabIndex = ((SateisyosaiForm)appContext.getActionForm()).getCurrentTab();
    	
    	switch (tabIndex) {
    	case TAB_TAIRYU_SAIKEN:
			//滞留債権明細タブ
    	    TairyuExcel chohyoTairyu = new TairyuExcel(appContext);
    	    chohyoTairyu.execute();
			break;
		case TAB_HIKIATE_KENSYO:
			// 引当金検証タブ
			KensyoSyokaiForm kensyoForm = (KensyoSyokaiForm)appContext.getSessionActionForm("HikiatekinKensyoSyokaiForm");
			cmnData.setSyoriCnt(kensyoForm.getHyoji());
			HikiatekinExcel chohyo = new HikiatekinExcel(appContext);
			chohyo.execute();
			break;
		default:
			if(GS.OS6101.equals(Function.trim(appContext.getCMN().getReturn_gamenId()))){
				SateiSyosaiExcel satei = new SateiSyosaiExcel(appContext);
				satei.execute();
			}else{
	    		SaikenExcel saiken = new SaikenExcel(appContext);
			    saiken.execute();
			}
			break;
		}
		if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			//課題No.102
			//修正開始
			//rtnVal = appContext.getActionForm().toString();
			rtnVal = GS.OS6102;
			//修正完了
		}
		return rtnVal;
	}
}
