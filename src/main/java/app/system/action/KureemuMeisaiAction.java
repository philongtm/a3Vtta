/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.action;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TenpuSentakuAction;
import app.system.bss.KureemuMeisaiBss;
import app.system.form.KureemuMeisaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

/**
 * OS3102 クレーム債権再設定_明細一覧 アクションクラス
 */
@Controller
@RequestMapping("/system/kureemuMeisai.do")
public class KureemuMeisaiAction extends AppMenuAction {

	private static final String KUREEMUMEISAIFORM = "04KureemuMeisaiForm";
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("mogitori", "mogitori");
        map.put("tenpu", "tenpu");
        map.put("regist", "regist");
        map.put("back", "back");
        map.put("show", "show");
        map.put("comment", "comment");
        map.put("syosai", "syosai");
		return map;
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
		KureemuMeisaiForm form = new KureemuMeisaiForm();
        appContext.setActionForm(form);
		// 機能共通セッションの明細情報を初期化
        SessionData cmnData = appContext.getCMN();
        
        cmnData.init_syosai_bean();        
        // ビジネスロジック実行
        KureemuMeisaiBss bss = new KureemuMeisaiBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(KUREEMUMEISAIFORM, form);
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
        // セッションスコープから01KureemuMeisaiFormを取得
    	KureemuMeisaiForm form = (KureemuMeisaiForm)appContext.getSessionActionForm(KUREEMUMEISAIFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの明細情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_syosai_bean();        
        // ビジネスロジック実行
        KureemuMeisaiBss bss = new KureemuMeisaiBss(appContext);            
        String result = bss.execute();
        // 前回表示時のページ設定をPagerにセット
        form.setPager(form.getId() + 1);
        return result;
    }

    /**
     * 【もぎ取り解除アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object mogitori(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	KureemuMeisaiBss bss = new KureemuMeisaiBss(appContext);
        // もぎ取り解除ビジネス処理
        bss.doKaijyo();
        // OS3101_クレーム債権_対象先一覧に遷移
        KureemuAction acc = new KureemuAction();
        acc.appReExecute(appContext);
        return GS.OS3101;
    }

    /**
     * 【添付選択アクション】 <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tenpu(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OS3102)を設定する。
        cmnData.setReturn_gamenId(GS.OS3102);
        // OZ1101_添付選択へ遷移する
		TenpuSentakuAction acc = new TenpuSentakuAction();
		acc.appExecute(appContext);
        return GS.OZ1101;
    }

    /**
     * 【登録アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object regist(AppContext appContext) throws Exception {
        // ビジネスロジック実行
    	KureemuMeisaiBss bss = new KureemuMeisaiBss(appContext);
        // 転送ビジネス処理
        boolean success = bss.doRegist();
        // 対象一覧に遷移する。
        if (success) {
        	KureemuAction acc = new KureemuAction();
        	acc.appReExecute(appContext);
        	return GS.OS3101;
        }
        return GS.OS3102;
    }

    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
        // OS3101_クレーム債権_対象先一覧に遷移
        KureemuAction acc = new KureemuAction();
        acc.appReExecute(appContext);
        return GS.OS3101;
    }
    
    /**
     * 【表示件数セレクトボックス処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */
    public Object show(AppContext appContext) throws Exception {
        // 表示件数の変更をPagerオブジェクトに設定
    	KureemuMeisaiForm form = (KureemuMeisaiForm)appContext.getActionForm();
        form.setPager();
        return GS.OS3102;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
        KureemuMeisaiForm form = (KureemuMeisaiForm)appContext.getActionForm();
        form.setPrevList();
        return GS.OS3102;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        // 表示部分の変更をListオブジェクトに設定
        KureemuMeisaiForm form = (KureemuMeisaiForm)appContext.getActionForm();
        form.setNextList();
        return GS.OS3102;       
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
        // 共)遷移元画面IDに当画面ID(OS3102)を設定する。
        cmnData.setReturn_gamenId(GS.OS3102);
        // OZ4101_コメント表示へ遷移する。
        SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
        acc.appExecute(appContext,cmnData.getTori_bean().getAnken_no());
        return GS.OZ4101;
    }

    /**
     * 【詳細リンク処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object syosai(AppContext appContext) throws Exception {

        // リンククリックされた明細のBeanを共)明細詳細情報に格納する。
    	KureemuMeisaiForm form = (KureemuMeisaiForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSyosai_bean((MeisaisyosaiBean)form.getAr_meisai().get(form.getId()));

        // OS3103_クレーム債権_明細詳細に遷移する。
        KureemuSyosaiAction acc = new KureemuSyosaiAction();
        acc.appExecute(appContext);
        return GS.OS3103;
    }
}