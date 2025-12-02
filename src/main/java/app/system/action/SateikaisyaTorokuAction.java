/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.system.bss.SateikaisyaTorokuBss;
import app.system.form.SateikaisyaIchiranForm;
import app.system.form.SateikaisyaTorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS7103_査定会社メンテナンス_登録 アクションクラス <br>
 */
@Controller
@RequestMapping("/system/sateikaisyatoroku.do")
public class SateikaisyaTorokuAction extends AppMenuAction {

	private static final String MODE_REGIST = "1"; // 遷移モード：登録
	private static final String MODE_UPDATE = "2"; // 遷移モード：更新
	private static final String SATEIKAISYATOROKUFORM = "03SateikaisyaTorokuForm"; // 査定会社メンテナンス_登録のフォーム
	private static final String SATEIKAISYAICHIRANFORM = "02SateikaisyaIchiranForm"; // 査定会社メンテナンス_一覧のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("regist", "regist");
		map.put("systemKbn", "systemKbn");
		map.put("update", "update");		
		map.put("delete", "delete");		
		map.put("back", "back");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】新規 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
	 */
	public Object appExecuteRegist(AppContext appContext) throws Exception {    
        // appContextのActionFormを上書き
		SateikaisyaTorokuForm form = new SateikaisyaTorokuForm();
		form.setSeniMode(MODE_REGIST);
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext); 
        String result = bss.executeInitRegist();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SATEIKAISYATOROKUFORM, form);
        return result;
	}
	
	/**
	 * 【画面初期表示処理】更新 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
	 */
	public Object appExecuteUpdate(AppContext appContext) throws Exception {    
        // appContextのActionFormを上書き
		SateikaisyaTorokuForm form = new SateikaisyaTorokuForm();
        form.setSeniMode(MODE_UPDATE);
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext); 
        String result = bss.executeInitUpdate();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(SATEIKAISYATOROKUFORM, form);
        return result;
	}

    /**
     * 【登録処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object regist(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext); 
        boolean result = bss.doRegist();
        if (result) {
        	// 査定会社設定メンテナンス_一覧フォーム
    	    SateikaisyaIchiranForm ichiranForm = (SateikaisyaIchiranForm)appContext.getSessionActionForm(SATEIKAISYAICHIRANFORM);
        	// 査定会社設定メンテナンス_登録フォーム
    	    SateikaisyaTorokuForm torokuForm = (SateikaisyaTorokuForm)appContext.getSessionActionForm(SATEIKAISYATOROKUFORM);
    	    // 検索キーをセットする。
    	    ichiranForm.setSystemKbn(torokuForm.getSystemKbn());
    	    ichiranForm.setSrhSystemKbn(torokuForm.getSystemKbn());
    	    ichiranForm.setHanyo1(torokuForm.getHanyo1());
    	    ichiranForm.setSrhHanyo1(torokuForm.getHanyo1());
    	    ichiranForm.setHanyo2(torokuForm.getHanyo2());
    	    ichiranForm.setSrhHanyo2(torokuForm.getHanyo2());
            // OS7102_査定会社設定メンテナンス_一覧へ遷移する。
        	SateikaisyaIchiranAction acc = new SateikaisyaIchiranAction();
            acc.appReExecute(appContext);
            return GS.OS7102;
        }
        // 遷移先を指定する。
        return GS.OS7103;
    }

    /**
     * 【システムアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object systemKbn(AppContext appContext) throws Exception {
    	SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext);       	    
        bss.doChangeSystemKbn();
		return GS.OS7103;
    }

    /**
     * 【更新処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object update(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext); 
        boolean result = bss.doUpdate();
        if (result) {
            // OS7102_査定会社設定メンテナンス_一覧へ遷移する。
        	SateikaisyaIchiranAction acc = new SateikaisyaIchiranAction();
            acc.appReExecute(appContext);
            return GS.OS7102;
        }
        // 遷移先を指定する。
        return GS.OS7103;
    }

    /**
     * 【削除処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object delete(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	SateikaisyaTorokuBss bss = new SateikaisyaTorokuBss(appContext); 
        boolean result = bss.doDelete();
        if (result) {
            // OS7102_査定会社設定メンテナンス_一覧へ遷移する。
        	SateikaisyaIchiranAction acc = new SateikaisyaIchiranAction();
            acc.appReExecute(appContext);
            return GS.OS7102;
        }
        // 遷移先を指定する。
        return GS.OS7103;
    }

    /**
     * 【戻る処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
        // OS7102_査定会社設定メンテナンス_一覧へ遷移する。
    	SateikaisyaIchiranAction acc = new SateikaisyaIchiranAction();
        acc.appReExecute(appContext);
        return GS.OS7102;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        return null;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        return null;       
    }
}