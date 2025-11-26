/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.SessionDataZen;
import app.common.action.SashimodoshiAction;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TensouAction;
import app.print.bss.TairyuExcelbss;
import app.tairyu.bss.TorokuBss;
import app.tairyu.form.TorokuForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.struts.AppPagerActionForm;
import common.util.TairyuExcel;

import java.util.HashMap;

/**
 * OB1102_実質滞留債権判定_明細一覧 アクションクラス
 */
public class TorokuAction extends AppMenuAction {

	private static final String TOROKUFORM = "01TorokuForm"; // 実質滞留債権判定_明細一覧のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("kaijyo", "kaijyo");
        map.put("tensou", "tensou");
        map.put("sashi", "sashi");
        map.put("save", "save");
        map.put("regist", "regist");
        map.put("download", "download");
        map.put("torikomi", "torikomi");
        map.put("back", "back");
        map.put("show", "show");
        map.put("comment", "comment");
        map.put("syosai", "syosai");
        map.put("tairyu_hantei", "tairyu_hantei");     
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
        TorokuForm form = new TorokuForm();
        appContext.setActionForm(form);
		// 機能共通セッションの明細情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_syosai_bean();        
        // ビジネスロジック実行
        TorokuBss bss = new TorokuBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(TOROKUFORM, form);
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
        // セッションスコープから01TorokuFormを取得
        TorokuForm form = (TorokuForm)appContext.getSessionActionForm(TOROKUFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
		// 機能共通セッションの明細情報を初期化
        SessionData cmnData = appContext.getCMN();
        cmnData.init_syosai_bean();        
        // ビジネスロジック実行
        TorokuBss bss = new TorokuBss(appContext);            
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
    public Object kaijyo(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        TorokuBss bss = new TorokuBss(appContext);
        // もぎ取り解除ビジネス処理
        bss.doKaijyo();
        // OB1101_実質滞留債権判定_対象先一覧に遷移
        IchiranAction acc = new IchiranAction();
        acc.appReExecute(appContext);
        return GS.OB1101;
    }

    /**
     * 【転送アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object tensou(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        TorokuBss bss = new TorokuBss(appContext);
        // 一時保存処理
        bss.doSaveNyuryoku(true);
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OB1102)を設定する。
        cmnData.setReturn_gamenId(GS.OB1102);
        // OZ3101_転送先選択へ遷移する。
        TensouAction acc = new TensouAction();
        acc.appExecute(appContext);
        return GS.OZ3101;
    }

    /**
     * 【差戻しアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object sashi(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OB1102)を設定する。
        cmnData.setReturn_gamenId(GS.OB1102);
        // OZ2101_差戻先選択へ遷移する。
        SashimodoshiAction acc = new SashimodoshiAction();
        acc.appExecute(appContext);
      return GS.OZ2101;
    }

    /**
     * 【一時保存アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object save(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        TorokuBss bss = new TorokuBss(appContext);
        // 転送ビジネス処理
        bss.doTempSave();
        // 本画面へ戻る。
        return GS.OB1102;
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
        TorokuBss bss = new TorokuBss(appContext);
        // 登録処理
        boolean success = bss.doRegist();
        // 対象一覧に遷移する。
        if (success) {
        	IchiranAction acc = new IchiranAction();
        	acc.appReExecute(appContext);
        	return GS.OB1101;
        }
        return GS.OB1102;
    }

    /**
     * 【ダウンロードアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    
    public Object download(AppContext appContext) throws Exception {

		String systemKbn = appContext.getCMN().getTori_bean().getSystem_kbn();

		if(systemKbn.equals(GS.GSS)){
			//国内版帳票
			SessionDataZen cmnData = appContext.getCMNZen();
			cmnData.setReturnId(appContext.getActionForm().toString());
			cmnData.setComLangMode(((AppPagerActionForm)appContext.getActionForm()).getLangMode());
		    TairyuExcel chohyoTairyu = new TairyuExcel(appContext);
		    chohyoTairyu.execute();
			//エラーメッセージが存在する場合は当画面にリターン
			if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
				return appContext.getActionForm().toString();
			}
		}else{
			//海外帳票
	        TairyuExcelbss bss = new TairyuExcelbss(appContext);
	        bss.execute();
		}

        return null;
    }

    /**
     * 【一括取り込みアクション】
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object torikomi(AppContext appContext) throws Exception {
    	TorokuBss bss=new TorokuBss(appContext);		
		bss.doTorikomi();
        return GS.OB1102;
    }

    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
        // OB1101_実質滞留債権判定_対象先一覧に遷移
        IchiranAction acc = new IchiranAction();
        acc.appReExecute(appContext);
        return GS.OB1101;
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
        TorokuForm form = (TorokuForm)appContext.getActionForm();
        form.setPager();
        return GS.OB1102;
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
        TorokuForm form = (TorokuForm)appContext.getActionForm();
        form.setPrevList();
        return GS.OB1102;
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
        TorokuForm form = (TorokuForm)appContext.getActionForm();
        form.setNextList();
        return GS.OB1102;       
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
        // 共)遷移元画面IDに当画面ID(OB1102)を設定する。
        cmnData.setReturn_gamenId(GS.OB1102);
        
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
        TorokuBss bss = new TorokuBss(appContext);
        // 一時保存処理
        bss.doSaveNyuryoku(true);

        // リンククリックされた明細のBeanを共)明細詳細情報に格納する。
		TorokuForm form = (TorokuForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSyosai_bean((MeisaisyosaiBean)form.getAr_meisai().get(form.getId()));

        //OB1103_実質滞留債権判定_明細詳細に遷移する。
        SyosaiAction acc = new SyosaiAction();
        acc.appExecute(appContext);
        return GS.OB1103;
    }

    /**
     * 【滞留判定セレクトボックス変更アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object tairyu_hantei(AppContext appContext) throws Exception {
        TorokuBss bss = new TorokuBss(appContext);
        // 一時保存処理
        bss.doSaveNyuryoku(false);
    	// 選択値を変更した明細のBeanを共)明細詳細情報に格納する。
		TorokuForm form = (TorokuForm)appContext.getActionForm();
        SessionData cmnData = appContext.getCMN();
        cmnData.setSyosai_bean((MeisaisyosaiBean)form.getAr_meisai().get(form.getId()));
        //OB1103_実質滞留債権判定_明細詳細に遷移する。
        SyosaiAction acc = new SyosaiAction();
        acc.appExecute(appContext);
        return GS.OB1103;
    }
}