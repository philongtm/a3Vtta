/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.hikiate.action;

import app.SessionData;
import app.common.action.SashimodoshiAction;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TenpuSentakuAction;
import app.common.action.TensouAction;
import app.hikiate.bss.KakuninBss;
import app.hikiate.form.KakuninForm;
import app.print.bss.KakuninExcelBss;
import app.syokai.action.SateisyosaiAction;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 *  OD1102_引当金確認<br>
 */
@Controller
@RequestMapping("/hikiate/kakunin.do")
public class KakuninAction extends AppMenuAction {

	private static final String KAKUNINFORM = "06KakuninForm"; // 引当金確認のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("kaijyo", "kaijyo");		// もぎ取り解除処理
        map.put("tensou", "tensou");		// 転送アクション
        map.put("sashi", "sashi");			// 差戻アクション
        map.put("save", "save");			// 一次保存
        map.put("regist", "regist");		// 登録アクション
        map.put("syosai", "syosai");		// 査定結果アクション
        map.put("temp", "temp");			// 添付選択アクション
        map.put("comment", "comment");		// コメント表示リンクアクション
        map.put("menu", "menu");			// メニューへアクション
        map.put("download", "download");	// 帳票ダウンロードアクション
		return map;
	}
	
	/**
	 * 【ダウンロード処理】
	 */
	public Object download(AppContext appContext) throws Exception {
		KakuninExcelBss bss = new KakuninExcelBss(appContext);
		bss.execute();
		return null;
	}
	
	/**
	 * 【画面初期表示処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
        // appContextのActionFormを上書き
        KakuninForm form = new KakuninForm();
        appContext.setActionForm(form);     
        KakuninBss bss = new KakuninBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KAKUNINFORM, form);
        return result;
	}

    /**
     * 【画面初期表示処理(対象先一覧以外から遷移時)】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception {        
        // appContextのActionFormを上書き
        KakuninForm form = new KakuninForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        KakuninBss bss = new KakuninBss(appContext); 
        String result = bss.execute();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KAKUNINFORM, form);
        return result;
    }
    
    /**
     * 
     *  もぎ取り解除アクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object kaijyo(AppContext appContext) throws Exception {
    	
    	KakuninBss bss = new KakuninBss(appContext); 
    	bss.doKaijyo();
    	IchiranAction acc = new IchiranAction();
    	acc.appReExecute(appContext);
        return GS.OD1101;
    }
    
    /**
     * 
     * 転送アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tensou(AppContext appContext) throws Exception {
    	
    	KakuninBss bss = new KakuninBss(appContext); 
    	if(!bss.doTenso()){
    		return GS.OD1102;
    	}else{
            // ActionFormをsessionから削除
            appContext.removeActionForm(KAKUNINFORM);
            // 機能共通セッションを取得する。
            SessionData cmnData = appContext.getCMN();
            // 共)遷移元画面IDに当画面ID(OD1102)を設定する。
            cmnData.setReturn_gamenId(GS.OD1102);
            // OZ3101_転送先選択へ遷移する。
    		TensouAction acc = new TensouAction();
    		acc.appExecute(appContext);
    		return GS.OZ3101;
    	}
    }
    
    /**
     * 
     * 差戻アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object sashi(AppContext appContext) throws Exception {
    	
    	KakuninBss bss = new KakuninBss(appContext); 
    	if(!bss.doSashi()){
    		return GS.OD1102;
    	}else{
            // ActionFormをsessionから削除
            appContext.removeActionForm(KAKUNINFORM);
            // 機能共通セッションを取得する。
            SessionData cmnData = appContext.getCMN();
            // 共)遷移元画面IDに当画面ID(OD1102)を設定する。
            cmnData.setReturn_gamenId(GS.OD1102);
            // OZ2101_差戻先選択へ遷移する。
            SashimodoshiAction acc = new SashimodoshiAction();
    		acc.appExecute(appContext);
    		return GS.OZ2101;
    	}
    }
    
    /**
     * 
     * 一時保存アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object save(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	KakuninBss bss = new KakuninBss(appContext);
        // 一時保存ビジネス処理
        bss.doTempSave();
        // 本画面へ戻る。
        return GS.OD1102;
    }
    
    /**
     * 
     * 登録アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object regist(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	KakuninBss bss = new KakuninBss(appContext);
        //  登録ビジネス処理
    	if(!bss.doRegist()){
    		// 本画面へ戻る。
            return GS.OD1102;
    	}else{
    		IchiranAction acc = new IchiranAction();
        	acc.appReExecute(appContext);
    		return GS.OD1101;
    	}
    }
    
    /**
     * 
     * メニューへアクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object menu(AppContext appContext) throws Exception {
    	IchiranAction acc = new IchiranAction();
    	acc.appReExecute(appContext);
		return GS.OD1101;
    }
    
    /**
     * 
     * 査定結果アクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object syosai(AppContext appContext) throws Exception {
    	// 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // ビジネスロジック実行
    	KakuninBss bss = new KakuninBss(appContext);
        //  査定結果ビジネス処理
    	if(!bss.doSyosai()){
    		
    		return GS.OD1102;
    	}else{
    		// 共)遷移元画面IDに当画面ID(OD1102)を設定する。
            cmnData.setReturn_gamenId(GS.OD1102);
            SateisyosaiAction acc = new SateisyosaiAction();
        	acc.appExecute(appContext);
    		// OS6102_査定内容詳細へ遷移する。
            return GS.OS6102;
    	}
    }
    
    /**
     * 
     * 添付選択アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object temp(AppContext appContext) throws Exception {
    	// 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // ビジネスロジック実行
    	KakuninBss bss = new KakuninBss(appContext);
        //  査定結果ビジネス処理
    	if(!bss.doTemp()){
    		
    		return GS.OD1102;
    	}else{
    		// 共)遷移元画面IDに当画面ID(OD1102)を設定する。
            cmnData.setReturn_gamenId(GS.OD1102);
    		//添付選択に遷移
    		TenpuSentakuAction acc = new TenpuSentakuAction();
    		acc.appExecute(appContext);
            return GS.OZ1101;
    	}
    }
    
    /**
     * 
     *  コメント表示リンクアクション<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object comment(AppContext appContext) throws Exception {
    	// 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // ビジネスロジック実行
    	KakuninBss bss = new KakuninBss(appContext);
        //  査定結果ビジネス処理
    	if(!bss.doComment()){
    		
    		return GS.OD1102;
    	}else{
    		// 共)遷移元画面IDに当画面ID(OD1102)を設定する。
            cmnData.setReturn_gamenId(GS.OD1102);
    		//コメント表示画面に遷移
    		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
    		acc.appExecute(appContext,appContext.getCMN().getTori_bean().getAnken_no());
            return GS.OZ4101;
    	}
    }
	
    /**
     * 
     * 【次のXX件→】<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
	public Object nextY(AppContext appContext) throws Exception {
		
		return null;
	}

	/**
     * 
     * 【←前のXX件】<br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
	public Object prevX(AppContext appContext) throws Exception {
		
		return null;
	}

}
