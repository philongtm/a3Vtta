/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.common.action;

import app.SessionDataZen;
import app.common.bss.SashimodoshiCommentBss;
import app.common.form.SashimodoshiCommentForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;


/**
 * 差戻コメント画面アクションクラス
 */
@Controller
@RequestMapping("/common/sashimodoshiComment.do")
public class SashimodoshiCommentAction extends AppMenuAction {

	private String CLASSNAME = getClass().getName(); // クラス名
	private SessionDataZen cmnData = null;	// 共通セッションデータ
    private static final String SYORI						= "syori";			// 処理
    private static final String COMMENT_VAL 				= "comment_val";	// コメント内容
    private static final String ACTIONFORMNAME			= "05SashimodoshiCommentForm";
    private static final String TANTOU					= "tantou";			// 担当者

	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("back","back");

		return map;
	}


	public Object prevX(AppContext appContext) throws Exception {
	    // 未使用
	    return GS.OZ4101;
	}

	public Object nextY(AppContext appContext) throws Exception {
	    // 未使用
	    return GS.OZ4101;
	}

	/**
	 * 【画面初期表示処理】
	 *
	 *  メソッド呼び出し時は、AppContextに遷移元のActionFormを
	 * 　残したままにしてください。
	 *  @param AppContext
	 *  @param String anken_no　：案件Ｎｏ．
	 */
	public Object appExecute(AppContext appContext, String anken_no) throws Exception {

	    // この画面用のActionFormを作成
	    SashimodoshiCommentForm form = new SashimodoshiCommentForm();
	    form.setAnken_no(anken_no);
	    form.setReturnForm(appContext.getActionForm());

        // appContextのActionFormを上書き
        appContext.setActionForm(form);

	    return select(appContext);
	}

	/**
	 * 【画面初期表示処理】
	 *
	 *  @param AppContext
	 *  @param SateiSincyokuForm
	 */
	public Object appExecute(AppContext appContext,HashMap<String, String> map) throws Exception {

	    cmnData = appContext.getCMNZen();
	    // この画面用のActionFormを作成
	    SashimodoshiCommentForm form = new SashimodoshiCommentForm();
	    form.setReturnForm(appContext.getActionForm());
        // appContextのActionFormを上書き
        appContext.setActionForm(form);

    	form.setKanjo_cd(cmnData.getKanjo_cd());
    	form.setKanjo_nm(cmnData.getKanjo_nm());
    	form.setUser_nm((String)map.get(TANTOU));
		form.setToroku_div((String)map.get(SYORI));
		form.setComment((String)map.get(COMMENT_VAL));

        appContext.setSessionActionForm(ACTIONFORMNAME,form);

	    return GS.OZ4101;
	}

	/**
	 * 【戻るボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {
	    // 遷移元画面取得
	    String returnId = appContext.getCMNZen().getReturnId();

	    // appContextのActionFormを遷移元のものに変更
	    SashimodoshiCommentForm form = (SashimodoshiCommentForm)appContext.getActionForm();
	    appContext.setActionForm(form.getReturnForm());

		// ActionFormをsessionから削除
		appContext.removeAttribute("05SashimodoshiCommentForm");

	    // 遷移元に遷移
		if(returnId.equals(GS.OB1102)) {
		    app.tairyu.action.TorokuAction acc = new app.tairyu.action.TorokuAction();
		    acc.appReExecute(appContext);
			return GS.OB1102;
		} else if(returnId.equals(GS.OS6102)) {
		    app.syokai.action.SateisyosaiAction acc = new app.syokai.action.SateisyosaiAction();
		    acc.appExecute(appContext);
		    return GS.OS6102;
		} else if(returnId.equals(GS.OS6104)) {
		    app.syokai.action.SincyokusyosaiAction acc = new app.syokai.action.SincyokusyosaiAction();
		    acc.appExecute(appContext);
		    return GS.OS6104;
		} else if(returnId.equals(GS.OC1102)) {
		    app.satei.action.TorokuAction acc = new app.satei.action.TorokuAction();
		    acc.appExecute(appContext);
		    return GS.OC1102;
		//OD1102_引当金確認追加
		} else if(returnId.equals(GS.OD1102)) {
		    app.hikiate.action.KakuninAction acc = new app.hikiate.action.KakuninAction();
		    acc.appExecute(appContext);
		    return GS.OD1102;
		} else if(returnId.equals(GS.OS3102)) {
		    app.system.action.KureemuMeisaiAction acc = new app.system.action.KureemuMeisaiAction();
		    acc.appReExecute(appContext);
		    return GS.OS3102;
		} else if(returnId.equals(GS.OD1105)){
			app.hikiate.action.KensyoAction acc = new app.hikiate.action.KensyoAction();
		    acc.appReExecute(appContext);
		    return GS.OD1105;
		} else if(returnId.equals(GS.OB2102)){
			app.tairyu.action.SenteisyosaiAction acc = new app.tairyu.action.SenteisyosaiAction();
		    acc.appExecute(appContext);
		    return GS.OB2102;
		} else if(returnId.equals(GS.OB2105)){
			app.tairyu.action.KarikijuntuikaAction acc = new app.tairyu.action.KarikijuntuikaAction();
		    acc.appExecute(appContext);
		    return GS.OB2105;
		} else if(returnId.equals(GS.OS6105)) {
		    app.syokai.action.TokusokumailAction acc = new app.syokai.action.TokusokumailAction();
		    acc.appExecute(appContext);
		    return GS.OS6105;
		} else {
		    return GS.OS2101;
		}
	}

	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext) throws Exception {
	    // session取得（Pager用の処理）
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
	    HttpSession session = appContext.getRequest().getSession( true );

	    SashimodoshiCommentForm form = (SashimodoshiCommentForm)appContext.getActionForm();

	    // 検索実行
	    SashimodoshiCommentBss bss = new SashimodoshiCommentBss(appContext);

	    String result = bss.execute();

	    // sessionスコープにActionFormを登録
	    form = (SashimodoshiCommentForm)appContext.getActionForm();
	    session.setAttribute("05SashimodoshiCommentForm", form);

	    return result;
	}
}