/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/20		SSC				1.5次版機能組込
002		09/06/01		SSC				IT161対応：勘定先コードリンク押下時の処理
******************************************************************************/
package app.system.action;

import app.SessionData;
import app.SessionDataZen;
import app.TorihikisakiBean;
import app.system.bss.ChampionBss;
import app.system.form.ChampionForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;


/**
 * チャンピオン部メンテナンス画面アクションクラス
 */
public class ChampionAction extends AppMenuAction {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("toroku","toroku");
		map.put("syosai","syosai");
		
		return map;
	}
	
	/**=========================================
	 * 共通セッション＆ActionFormBeanオブジェクト取得
	 *==========================================*/	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
	    ChampionForm form = (ChampionForm)appContext.getActionForm();
		form.setPrevList();
	    return GS.OS5101;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
	    ChampionForm form = (ChampionForm)appContext.getActionForm();
		form.setNextList();
	    return GS.OS5101;	    
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {
	    // この画面用のActionFormを作成
	    ChampionForm form = new ChampionForm();
	    
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    
	    return select(appContext, 1);
	}

	/**
	 * 【画面初期表示処理(遷移先より戻り時)】
	 */
	public Object appReExecute(AppContext appContext) throws Exception {
	    ChampionForm form = (ChampionForm)appContext.getSessionActionForm("04ChampionForm");
	    appContext.setActionForm(form);
        return select(appContext, 2);
	}
	
	/**
	 * 【登録処理】
	 */
	public Object toroku(AppContext appContext) throws Exception {
	    ChampionBss bss = new ChampionBss(appContext);
		
		if (bss.torokuCheck()) {
			// 登録処理
			bss.doUpdate();
		} else {
			// 選択項目なし
			appContext.setMsgCode("err.0058");
		}
			
	    return GS.OS5101;
	}
	
	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object syosai(AppContext appContext) throws Exception {
		SessionDataZen cmnData = appContext.getCMNZen();
		//IT161対応
		SessionData cmn = appContext.getCMN();
		TorihikisakiBean toriBean = new TorihikisakiBean();
		//IT161ここまで
		
	    // リンク先で必要な情報のセット
	    ChampionForm form = (ChampionForm)appContext.getActionForm();
		HashMap map = (HashMap)form.getAr_meisai().get(form.getId());
		cmnData.setYm((String)map.get("ym"));
	    cmnData.setKanjo_cd((String)map.get("kanjo_cd"));
	    cmnData.setKanjo_nm((String)map.get("kanjo_nm"));
	    cmnData.setSystem_kbn((String)map.get("system_kbn"));
	    cmnData.setCountry_nm((String)map.get("syozaikoku"));
	    // No410, 2008/05/23, SJA渡辺, セッション情報に店コードと査定会社コード追加修正
	    cmnData.setAnken_mise_cd((String)map.get("mise_cd"));
	    cmnData.setAnken_satei_kaisya_cd((String)map.get("kaisya"));
	    cmnData.setSyoriCnt((String)map.get("syori_kaisu"));
	    /* ダミー */
	    cmnData.setPhase("0");
	    cmnData.setSatei_anken_no("");
	    
		//IT161対応
		//年月編集用
		StringBuffer ymHensyugo = new StringBuffer();
		String ym = (String)map.get("ym");
		String[] ymHairetu = new String[2];
	    toriBean.setTaisyo_ym(ym);
	    if(GS.LANG_JA.equals(cmn.getComLangMode())){
			ymHairetu[0] = ym.substring(0,4);
			ymHairetu[1] = ym.substring(4,6);
	    }else{
			ymHairetu[0] = ym.substring(4,6);
			ymHairetu[1] = ym.substring(0,4);
	    }
    	ymHensyugo.append(ymHairetu[0]).append(GS.SLASH).append(ymHairetu[1]);
	    toriBean.setTaisyo_ym_hyoji(ymHensyugo.toString());
	    toriBean.setKanjo_cd((String)map.get("kanjo_cd"));
	    toriBean.setKanjo_nm((String)map.get("kanjo_nm"));
	    toriBean.setSystem_kbn((String)map.get("system_kbn"));
	    toriBean.setSyozaikoku((String)map.get("syozaikoku"));
	    // No410, 2008/05/23, SJA渡辺, セッション情報に店コードと査定会社コード追加修正
	    toriBean.setMise_cd((String)map.get("mise_cd"));
	    toriBean.setSateikaisya_cd((String)map.get("kaisya"));
	    toriBean.setSyori_kaisu((String)map.get("syori_kaisu"));
	    /* ダミー */
	    toriBean.setPhase("0");
	    toriBean.setAnken_no("");
	    cmn.setTori_bean(toriBean);
		//IT161ここまで

		// 戻り時の画面IDセット
	    cmnData.setReturnId(GS.OS5101);
	    //IT161対応
	    appContext.getCMN().setReturn_gamenId(GS.OS5101);
	    //IT161ここまで
	    
	    // リンク先（債権査定結果照会画面）の処理を呼び出す
	    app.syokai.action.SateisyosaiAction acc = new app.syokai.action.SateisyosaiAction();
	    acc.appExecute(appContext);
	    
		// ActionFormをsessionから削除
		appContext.removeAttribute("04ChampionForm");
	    
	    return GS.OS6102;
	}

	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext, int initmode) throws Exception {    		
	    // session取得（Pager用の処理）
		// No426, 2008/05/23, SJA細野, オブジェクト変数名を即した名称に修正。
		HttpSession session = appContext.getRequest().getSession( true );

	    ChampionForm form = (ChampionForm)appContext.getActionForm();
		
	    // 検索実行
	    ChampionBss bss = new ChampionBss(appContext);       	    
	    if(initmode == 1) { 
	    	// 画面初期表示時
	        String result = bss.execute();

	        // sessionスコープにActionFormを登録（Pager用の処理）
	        session.setAttribute("04ChampionForm", form);

	        return result;
	    } else if(initmode == 2) { 
	    	// 遷移先より戻り時
	        // sessionスコープにActionFormを登録（Pager用の処理）
	        session.setAttribute("04ChampionForm", form);

	        return GS.OS5101;
	    } else { 
	    	// 画面初期表示時以外
			return bss.execute();
	    }
	}
}