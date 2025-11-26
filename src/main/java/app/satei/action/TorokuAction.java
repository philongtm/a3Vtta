/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.action;

import app.common.action.SashimodoshiAction;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TensouAction;
import app.satei.bss.TorokuBss;
import app.satei.form.TorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OC1102_査定_取引先概要 アクションクラス<br>
 */
public class TorokuAction extends AppMenuAction {

	private static final String TOROKUFORM			= "02TorokuForm";
	private static final String RELEASE				= "release";
	private static final String FORWARD				= "forward";
	private static final String SENDBACK				= "sendBack";
	private static final String FIRSTPRESERVE			= "firstPreserve";
	private static final String NEXT					= "next";
	private static final String COMMENT_LINK			= "commentLink";
	private static final String BACK					= "back";

	//入力履歴登録判定用
	private static final boolean HOZON_TRUE		= true;
	private static final boolean HOZON_FALSE		= false;

    /**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put(RELEASE,RELEASE);
        map.put(FORWARD,FORWARD);
        map.put(SENDBACK,SENDBACK);
        map.put(FIRSTPRESERVE,FIRSTPRESERVE);
        map.put(NEXT,NEXT);
        map.put(COMMENT_LINK,COMMENT_LINK);
        map.put(BACK,BACK);
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		
		//sessionからActionForm取得
        TorokuForm form = (TorokuForm)appContext.getSessionActionForm(TOROKUFORM);

        if(form == null){
        	form = new TorokuForm();
            //appContextのActionFormを上書き
            appContext.setActionForm(form);
        }else{
    	    return form.toString();
        }
        
        //以下初期表示処理
		TorokuBss bss = new TorokuBss(appContext);
        bss.execInit(appContext);
        
        //sessionスコープにActionFormを登録
        appContext.setSessionActionForm(TOROKUFORM,form);

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
	 * 【メニューへボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {
		//OC1101_査定_対象先一覧に遷移
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		return GS.OC1101;
	}

	/**
	 * 【もぎ取り解除ボタン処理】
	 */
	public Object release(AppContext appContext) throws Exception {

		TorokuBss bss = new TorokuBss(appContext);

		//もぎ取り解除処理
		bss.execKaijo();

		//OC1101_査定_対象先一覧に遷移
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);

		return GS.OC1101;
	}

	/**
	 * 【転送ボタン処理】
	 */
	public Object forward(AppContext appContext) throws Exception {

		TorokuBss bss = new TorokuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

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

		TorokuBss bss = new TorokuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

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
	 * 【コメントリンク処理】
	 */
	public Object commentLink(AppContext appContext) throws Exception {

		TorokuBss bss = new TorokuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴未登録)
		bss.execHozon(HOZON_FALSE);

		//遷移元画面IDを設定
		appContext.getCMN().setReturn_gamenId(appContext.getActionForm().toString());
		
		//コメント表示画面に遷移
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
		acc.appExecute(appContext,appContext.getCMN().getTori_bean().getAnken_no());

		return GS.OZ4101;
	}
	
	/**
	 * 【一次保存ボタン処理】
	 */
	public Object firstPreserve(AppContext appContext) throws Exception {

		TorokuBss bss = new TorokuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴登録)
		bss.execHozon(HOZON_TRUE);

	    return appContext.getActionForm().toString();
	}

	/**
	 * 【次画面ボタン処理】
	 */
	public Object next(AppContext appContext) throws Exception {

		TorokuBss bss = new TorokuBss(appContext);

		//入力チェック
		if(!bss.check()){
			return appContext.getActionForm().toString();
		}

		//保存処理(入力履歴登録)
		bss.execHozon(HOZON_TRUE);

		//OC1103_査定_取引先区分判定に遷移
		KubunAction acc = new KubunAction();
		acc.appExecute(appContext);

	    return GS.OC1103;
	}
}