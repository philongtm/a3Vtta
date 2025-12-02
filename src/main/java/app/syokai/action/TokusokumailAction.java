/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/02/25		SSC				新規作成
******************************************************************************/

package app.syokai.action;

import app.SessionData;
import app.common.action.SashimodoshiCommentAction;
import app.syokai.bss.TokusokumailBss;
import app.syokai.form.TokusokumailForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

/**
 * OS6105_督促メール送信選択 アクションクラス <br>
 */
@Controller
@RequestMapping("/syokai/tokusokumail.do")
public class TokusokumailAction extends AppMenuAction {

	private static final String TOKUSOKUMAILFORM = "03TokusokumailForm";

	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
		// ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("back","back");								//戻る
		map.put("mailSend","mailSend");						//メール送信
		map.put("mailSave","mailSave");						//メール未配信
		map.put("soushinsaki","soushinsaki");				//送信先セレクトボックス
		map.put("tairyu","tairyu");							//直近の案件担当者タブの切り替え
		map.put("sinchoku","sinchoku");						//進捗（滞留タブ）
		map.put("comment","comment");						//コメント（滞留タブ）
		return map;
	}

	/**
	 * 【画面初期表示処理(進捗状況詳細からの遷移時)】
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		// appContextのActionFormを上書き
		TokusokumailForm form = new TokusokumailForm();
		appContext.setActionForm(form);

		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.executeInit();

		// sessionスコープにActionFormを登録
		appContext.setSessionActionForm(TOKUSOKUMAILFORM, form);
		return result;
	}

	/**
	 * 【画面初期表示処理(タブ操作など進捗状況詳細以外からの遷移時)】
	 */
	public Object appReExecute(AppContext appContext) throws Exception {

		appContext.removeActionFormExcept(TOKUSOKUMAILFORM);
		// sessionからActionForm取得
		TokusokumailForm form = (TokusokumailForm)appContext.getSessionActionForm(TOKUSOKUMAILFORM);

		// appContextのActionFormを上書き
		appContext.setActionForm(form);
		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.execute();

		// sessionスコープにActionFormを登録
		appContext.setSessionActionForm(TOKUSOKUMAILFORM, form);

		return result;
	}

	/**
	 * メール送信アクション
	 */
	public Object mailSend(AppContext appContext) throws Exception {
		// メール送信フラグに1（送信）を設定
		TokusokumailForm form = (TokusokumailForm)appContext.getActionForm();
		form.setMailSendFlg("1");
		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.doInsT04();

		SincyokusyosaiAction acc = new SincyokusyosaiAction();
		acc.appExecute(appContext);

		return result;
	}

	/**
	 * メール未配信アクション
	 */
	public Object mailSave(AppContext appContext) throws Exception {

		// メール送信フラグに2（未配信）を設定
		TokusokumailForm form = (TokusokumailForm)appContext.getActionForm();
		form.setMailSendFlg("2");
		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.doInsT04();

		SincyokusyosaiAction acc = new SincyokusyosaiAction();
		acc.appExecute(appContext);

		return result;
	}

	/**
	 * 送信先セレクトボックス選択アクション
	 * （画面表示の初期化）
	 */
	public Object soushinsaki(AppContext appContext) throws Exception {

		// 現送信部から新送信部に切替
		TokusokumailForm oldForm = (TokusokumailForm)appContext.getActionForm();
		String soushinbu = oldForm.getSoushinbu();
		if("10".equals(soushinbu)){
			soushinbu = "20"; // 新規
		} else {
			soushinbu = "10"; // 直近
		}

		// appContextのActionFormを上書き
		TokusokumailForm form = new TokusokumailForm();
		appContext.setActionForm(form);

		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.executeInit();

		form.setSoushinbu(soushinbu);

		// sessionスコープにActionFormを登録
		appContext.setSessionActionForm(TOKUSOKUMAILFORM, form);

		return result;
	}

	/**
	 * 直近の案件担当者タブアクション<br>
	 * （査定タブ/滞留タブの切り替え）<br>
	 */
	public Object tairyu(AppContext appContext) throws Exception {

		// 送信部を保持する
		TokusokumailForm form = (TokusokumailForm)appContext.getActionForm();

		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		String result = bss.execute();
		appContext.setSessionActionForm(TOKUSOKUMAILFORM, form);


		return result;
	}

	/**
	 * 進捗（滞留タブ）アクション
	 */
	public Object sinchoku(AppContext appContext) throws Exception {
		// 送信部を保持する
		TokusokumailForm form = (TokusokumailForm)appContext.getActionForm();

		// ビジネスロジック実行
		TokusokumailBss bss = new TokusokumailBss(appContext);
		bss.sinchoku();

		appContext.setSessionActionForm(TOKUSOKUMAILFORM, form);

		// 当画面へ遷移。
		return GS.OS6105;
	}

	/**
	 * コメント（滞留タブ）アクション
	 */
	public Object comment(AppContext appContext) throws Exception {
		// 機能共通セッションを取得する。
		SessionData cmnData = appContext.getCMN();
		// 共)遷移元画面IDを設定する。
		cmnData.setReturn_gamenId(GS.OS6105);

		TokusokumailForm form = (TokusokumailForm)appContext.getActionForm();
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
		List<HashMap<String, String>> list = (List<HashMap<String, String>>)form.getAr_sinchoku();

		if("1".equals(form.getKarento_tab())){
			//カレントタブが滞留
			acc.appExecute(appContext,(HashMap<String, String>)list.get(form.getId_sinchoku()));
		} else {
			//カレントタブが査定
			acc.appExecute(appContext,(HashMap<String, String>)list.get(form.getId()));
		}

		// OZ4101_コメント表示へ遷移する。
		return GS.OZ4101;
	}

	/**
	 * 戻るアクション <br>
	 */
	public Object back(AppContext appContext) throws Exception {
		appContext.removeActionForm(TOKUSOKUMAILFORM);
		SincyokusyosaiAction acc = new SincyokusyosaiAction();
		acc.appExecute(appContext);


		// OS6104_進捗状況詳細へ遷移する
		return GS.OS6104;
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

}
