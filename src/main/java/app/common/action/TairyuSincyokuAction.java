/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.common.bss.TairyuSincyokuBss;
import app.common.form.TairyuSincyokuForm;
import app.syokai.form.SincyokusyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.util.Function;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OZ6110_実質滞留債権判定進捗照会タブ アクションクラス <br>
 */
@Controller
@RequestMapping("/common/tairyu_sincyoku.do")
public class TairyuSincyokuAction extends AppMenuAction {

	private static final String TAIRYUSINCYOKUFORM = "05tairyuSincyokuForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("sinchoku","sinchoku");
		map.put("comment","comment");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {	

	    // appContextのActionFormを上書き
		SincyokusyosaiForm oyaForm = (SincyokusyosaiForm)appContext.getActionForm();
		TairyuSincyokuForm form = (TairyuSincyokuForm)appContext.getSessionActionForm(TAIRYUSINCYOKUFORM);
		if (form != null) {
			return GS.OS6104;
		}
		form = new TairyuSincyokuForm();
		form.setTorimodoshi_fuka_flg(Function.trim(oyaForm.getTorimodoshi_fuka_flg()));
		form.setUpd_user_id_flg(Function.trim(oyaForm.getUpd_user_id_flg()));
        appContext.setActionForm(form);       
        
	    // ビジネスロジック実行
        TairyuSincyokuBss bss = new TairyuSincyokuBss(appContext);       	    
        String result = bss.executeInit();
        
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(TAIRYUSINCYOKUFORM, form);
        return result;
	}

    /**
     * 【進捗アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object sinchoku(AppContext appContext) throws Exception {
	    // ビジネスロジック実行
        TairyuSincyokuBss bss = new TairyuSincyokuBss(appContext);       	    
        bss.sinchoku();
        
        // 当画面へ遷移。
		return GS.OS6104;
    }

    /**
     * 【コメント表示リンクアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object comment(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDを設定する。
        cmnData.setReturn_gamenId(cmnData.getTab_riyou_gamenId());
        
        TairyuSincyokuForm form = (TairyuSincyokuForm)appContext.getActionForm();
		SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
        acc.appExecute(appContext,(HashMap<String, String>)form.getAr_sinchoku().get(form.getId_sinchoku()));
        
        // OZ4101_コメント表示へ遷移する。
        return GS.OZ4101;
    }
    
	/**
	 * 【←前のXX件】未実装 <br>
	 */	
	public Object prevX(AppContext appContext) throws Exception {
	    return null;
	}
	
	/**
	 * 【次のXX件→】未実装 <br>
	 */	
	public Object nextY(AppContext appContext) throws Exception {
	    return null;    
	}
}
