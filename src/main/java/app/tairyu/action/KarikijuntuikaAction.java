/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.action.SashimodoshiCommentAction;
import app.tairyu.bss.KarikijuntuikaBss;
import app.tairyu.form.KarikijuntuikaForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OB2105_対象先選定_仮基準査定選択 アクションクラス
 */
public class KarikijuntuikaAction extends AppMenuAction {

	private static final String KARIKIJUNTUIKAFORM = "01KarikijuntuikaForm";
	private static final String ACTIONFORMPATTERN = "01SenteiForm|01KarikijuntuikaForm";
	private static final String BACKOB2101			= "backOB2101";
	private static final String SYOSAI				= "syosai";
	private static final String RELEASE				= "release";
	private static final String ADDCANCEL				= "addCancel";
	private static final String COMMENTLINK			= "commentLink";
	private static final String SHOW					= "show";
		
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(BACKOB2101,BACKOB2101);
		map.put(SYOSAI,SYOSAI);
		map.put(RELEASE,RELEASE);
		map.put(ADDCANCEL,ADDCANCEL);
		map.put(COMMENTLINK,COMMENTLINK);
		map.put(SHOW,SHOW);
		return map;
	}
	
	/**
	 * 【画面初期表示処理(OB2101_対象先選定_選定実行から遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
	    // sessionから当画面とOB2101_対象先選定_選定実行以外のActionForm削除
		appContext.removeActionFormPattern(ACTIONFORMPATTERN);
	    // sessionからActionForm取得
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getSessionActionForm(KARIKIJUNTUIKAFORM);
	    
		if(form != null){
		    // appContextのActionFormを上書き
	        appContext.setActionForm(form);
			// 機能共通セッションの取引先情報を初期化
	        SessionData cmnData = appContext.getCMN();
	        cmnData.init_tori_bean();
		}else{
			form = new KarikijuntuikaForm();
		    // appContextのActionFormを上書き
	        appContext.setActionForm(form);
		    // ビジネスロジック実行
	        KarikijuntuikaBss bss = new KarikijuntuikaBss(appContext);       	    
	        bss.execute();
		}
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KARIKIJUNTUIKAFORM,form);
        return form.toString();
	}

	/**
	 * 【戻る処理】
	 */
	public Object backOB2101(AppContext appContext) throws Exception {
	    //OB2101_対象先選定_選定実行に遷移
		SenteiAction acc = new SenteiAction();
	    acc.appReExecute(appContext);
	    return GS.OB2101;
	}

	/**
	 * 【もぎ取り解除処理】
	 */
	public Object release(AppContext appContext) throws Exception {

		//もぎ取り解除処理
        KarikijuntuikaBss bss = new KarikijuntuikaBss(appContext);       	    
		bss.execKaijo();

		//OB2101_対象先選定_選定実行に遷移
		SenteiAction acc = new SenteiAction();
	    acc.appReExecute(appContext);
	    return GS.OB2101;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getActionForm();
		form.setPrevList();
	    return form.toString();
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 表示部分の変更をListオブジェクトに設定
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getActionForm();
		form.setNextList();
	    return form.toString();
	}
	
	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object syosai(AppContext appContext) throws Exception {

        // クリックされた勘定先情報を機能共通セッションに格納
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        TorihikisakiBean toriBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
        toriBean.setAnken_no(form.getAnkenNo());
        toriBean.setPhase(form.getPhase());
        toriBean.setBunrui2(form.getHanyou2());
        cmnData.setTori_bean(toriBean);

        //遷移元画面IDに当画面IDをセット
        cmnData.setReturn_gamenId(form.toString());

        //OB2102_対象先選定_選定詳細に遷移
		SenteisyosaiAction acc = new SenteisyosaiAction();
	    acc.appExecute(appContext);

        return GS.OB2102;
	}

	/**
	 * 【コメントリンク処理】
	 */
	public Object commentLink(AppContext appContext) throws Exception {

		//もぎ取り解除処理
        KarikijuntuikaBss bss = new KarikijuntuikaBss(appContext);       	    
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getActionForm();
		
		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(form.toString());
		
		//コメント表示画面に遷移の前処理
		bss.zenSashiCom();
		
		//コメント表示画面に遷移
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
		acc.appExecute(appContext,form.getAnkenNo());

		return GS.OZ4101;
	}
	
	/**
	 * 【追加中止処理】
	 */
	public Object addCancel(AppContext appContext) throws Exception {

		//追加中止処理
        KarikijuntuikaBss bss = new KarikijuntuikaBss(appContext);       	    
        bss.execCancel();

		//OB2101_対象先選定_選定実行に遷移
		SenteiAction acc = new SenteiAction();
	    acc.appReExecute(appContext);
	    return GS.OB2101;
	}

	/**
	 * 【表示件数セレクトボックス処理】
	 */
	public Object show(AppContext appContext) throws Exception {
		// 表示件数の変更をPagerオブジェクトに設定
		KarikijuntuikaForm form = (KarikijuntuikaForm)appContext.getActionForm();
		form.setPager();
		return GS.OB2105;
	}
}