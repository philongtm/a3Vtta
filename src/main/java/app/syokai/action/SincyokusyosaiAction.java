/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
001		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/

package app.syokai.action;

import app.SessionData;
import app.common.action.SateiSincyokuAction;
import app.common.action.TairyuSincyokuAction;
import app.syokai.bss.SincyokusyosaiBss;
import app.syokai.form.SincyokusyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS6104_進捗状況詳細 アクションクラス <br>
 */
public class SincyokusyosaiAction extends AppMenuAction {

	private static final String SINCYOKUSYOSAIFORM = "03SincyokusyosaiForm";

	/**
     * ディスパッチマップ作成
     */
    public HashMap getKeyMethodMap() {
        // ディスパッチアップ作成
        HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("tori","tori");
        map.put("back","back");
        map.put("tairyu", "tairyu");
        map.put("tokusokuPage", "tokusokuPage");
        map.put("mailSend","mailSend");
        map.put("mailSave","mailSave");
        return map;
    }

    /**
     * 【画面初期表示処理(メニューリンクから遷移時)】
     */
    public Object appExecute(AppContext appContext) throws Exception {

        // セッションスコープから03SincyokusyosaiFormを取得
    	SincyokusyosaiForm form = (SincyokusyosaiForm)appContext.getSessionActionForm(SINCYOKUSYOSAIFORM);
		if (form != null) {
			// 03SincyokusyosaiFormが存在しない場合
			return GS.OS6102;
		}
        // appContextのActionFormを上書き
    	form = new SincyokusyosaiForm();
        appContext.getCMN().setTab_riyou_gamenId(form.toString());
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokusyosaiBss bss = new SincyokusyosaiBss(appContext);
        // タブのデフォルト
        form.setKarento_tab("2");
        String result = bss.executeInit();
        // 実質滞留判定タブの表示
        if("1".equals(form.getKarento_tab())){
        	TairyuSincyokuAction acc = new TairyuSincyokuAction();
    		acc.appExecute(appContext);
        // 査定タブの表示
    	}else if("2".equals(form.getKarento_tab())){
    		SateiSincyokuAction acc = new SateiSincyokuAction();
    		acc.appExecute(appContext);
    	}

        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SINCYOKUSYOSAIFORM, form);
        return result;
    }

    /**
     * 【画面初期表示処理(メニューリンク以外から遷移時)】
     */
    public Object appReExecute(AppContext appContext) throws Exception {

        appContext.removeActionFormExcept(SINCYOKUSYOSAIFORM);
        // sessionからActionForm取得
        SincyokusyosaiForm form = (SincyokusyosaiForm)appContext.getSessionActionForm(SINCYOKUSYOSAIFORM);
        appContext.getCMN().setTab_riyou_gamenId(form.toString());

        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SincyokusyosaiBss bss = new SincyokusyosaiBss(appContext);
        String result = bss.execute();

        return result;
    }

    /**
     *
     * 取戻アクション <br>
     *
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tori(AppContext appContext) throws Exception {
    	SincyokusyosaiBss bss = new SincyokusyosaiBss(appContext);
    	// 取戻処理
    	if(bss.doTori()){
    		// 共)共通タブ利用画面IDにNULLを設定し
    		SessionData cmnData = appContext.getCMN();
    		cmnData.setTab_riyou_gamenId(null);
    		// OS6103_進捗状況照会へ遷移する
    		SincyokuAction acc = new SincyokuAction();
    		acc.appReExecute(appContext);
    		return GS.OS6103;
    	}else{
    		// 本画面へ遷移する
    		return GS.OS6104;
    	}
    }

    /**
     *
     * タブアクション <br>
     *
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tairyu(AppContext appContext) throws Exception {
    	SincyokusyosaiForm form = (SincyokusyosaiForm)appContext.getSessionActionForm(SINCYOKUSYOSAIFORM);
        // 実質滞留判定タブの表示
        if("1".equals(form.getKarento_tab())){
    		TairyuSincyokuAction acc = new TairyuSincyokuAction();
    		acc.appExecute(appContext);
        // 査定タブの表示
    	}else if("2".equals(form.getKarento_tab())){
    		SateiSincyokuAction acc = new SateiSincyokuAction();
    		acc.appExecute(appContext);
    	}

    	return GS.OS6104;
    }

    /**
     *
     * 戻るアクション <br>
     *
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object back(AppContext appContext) throws Exception {
    	// 共)共通タブ利用画面IDにNULLを設定し
		SessionData cmnData = appContext.getCMN();
		cmnData.setTab_riyou_gamenId(null);
		SincyokuAction acc = new SincyokuAction();
		acc.appReExecute(appContext);
		// OS6103_進捗状況照会へ遷移する
		return GS.OS6103;
    }

	/**
	 *
	 * 督促メール送信選択画面に遷移 <br>
	 *
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object tokusokuPage(AppContext appContext) throws Exception {
		// 共)共通タブ利用画面IDにNULLを設定し
		SessionData cmnData = appContext.getCMN();
		cmnData.setTab_riyou_gamenId(null);

		// OS6105_督促メール送信選択画面に遷移
		TokusokumailAction acc = new TokusokumailAction();
		acc.appExecute(appContext);
		return GS.OS6105;
	}

	/**
	 * メール送信アクション
	 */
	public Object mailSend(AppContext appContext) throws Exception {
		// メール送信フラグに1（送信）を設定
		SincyokusyosaiForm form = (SincyokusyosaiForm)appContext.getActionForm();
		form.setMailSendFlg("1");
		// ビジネスロジック実行
		SincyokusyosaiBss bss = new SincyokusyosaiBss(appContext);
		String result = bss.doInsT04();

		if(GS.OS6104.equals(result)){
			// 入力チェックでエラー
			bss.execute();
			return result;
		}

		// 共)共通タブ利用画面IDにNULLを設定し
		SessionData cmnData = appContext.getCMN();
		cmnData.setTab_riyou_gamenId(null);
		SincyokuAction acc = new SincyokuAction();
		acc.appReExecute(appContext);
		// OS6103_進捗状況照会へ遷移する
		return result;
	}

	/**
	 * メール未配信アクション
	 */
	public Object mailSave(AppContext appContext) throws Exception {

		// メール送信フラグに2（未配信）を設定
		SincyokusyosaiForm form = (SincyokusyosaiForm)appContext.getActionForm();
		form.setMailSendFlg("2");
		// ビジネスロジック実行
		SincyokusyosaiBss bss = new SincyokusyosaiBss(appContext);
		String result = bss.doInsT04();

		if(GS.OS6104.equals(result)){
			// 入力チェックでエラー
			bss.execute();
			return result;
		}

		// 共)共通タブ利用画面IDにNULLを設定し
		SessionData cmnData = appContext.getCMN();
		cmnData.setTab_riyou_gamenId(null);
		SincyokuAction acc = new SincyokuAction();
		acc.appReExecute(appContext);
		// OS6103_進捗状況照会へ遷移する
		return result;
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
