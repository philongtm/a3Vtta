/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.action;

import app.SessionData;
import app.common.action.TenpuSentakuAction;
import app.tairyu.bss.SyosaiBss;
import app.tairyu.form.SyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OB1103_実質滞留債権判定_明細詳細 アクションクラス <br>
 */
public class SyosaiAction extends AppMenuAction {

	private static final String SYOSAIFORM = "01SyosaiForm"; // 実質滞留債権判定_明細一覧のフォーム
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("save","save");		
		map.put("back","back");		
		map.put("add","add");		
		map.put("download","download");		
		return map;
	}
		
	/**
	 * 【画面初期表示処理】 <br>
	 */
	public Object appExecute(AppContext appContext) throws Exception {
        // セッションスコープから01SyosaiFormを取得する。
        SyosaiForm form = (SyosaiForm)appContext.getSessionActionForm(SYOSAIFORM);
        if (form == null) {
            // appContextのActionFormを上書き
            form = new SyosaiForm();
            // sessionスコープにActionFormを登録
            appContext.setSessionActionForm(SYOSAIFORM,form);
        }
        // 明細情報をセットする。
        form.setSyosai_bean(appContext.getCMN().getSyosai_bean());
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SyosaiBss bss = new SyosaiBss(appContext); 
        bss.executeInit();

        return GS.OB1103;
	}

    /**
     * 【画面初期表示処理(明細一覧以外から遷移時)】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */
    public Object appReExecute(AppContext appContext) throws Exception {        
        // セッションスコープから01SyosaiFormを取得する。
        SyosaiForm form = (SyosaiForm)appContext.getSessionActionForm(SYOSAIFORM);
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        // ビジネスロジック実行
        SyosaiBss bss = new SyosaiBss(appContext);            
        String result = bss.execute();
        return result;
    }

    /**
     * 【保存アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object save(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        SyosaiBss bss = new SyosaiBss(appContext); 
        boolean result = bss.save();
        if (result) {
            // OB1102_実質滞留債権判定_明細一覧に遷移
            TorokuAction acc = new TorokuAction();
            acc.appReExecute(appContext);
            return GS.OB1102;
        }
        return GS.OB1103;
    }

    /**
     * 【戻るボタン処理】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object back(AppContext appContext) throws Exception {
        // OB1102_実質滞留債権判定_明細一覧に遷移
        TorokuAction acc = new TorokuAction();
        acc.appReExecute(appContext);
        return GS.OB1102;
    }

    /**
     * 【添付選択アクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object add(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OB1103)を設定する。
        cmnData.setReturn_gamenId(GS.OB1103);

        //添付選択に遷移
		TenpuSentakuAction acc = new TenpuSentakuAction();
		acc.appExecute(appContext);

        return GS.OZ1101;
    }

    /**
     * 【添付ファイル名リンクアクション】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object download(AppContext appContext) throws Exception {
        // ビジネスロジック実行
        SyosaiBss bss = new SyosaiBss(appContext); 
        bss.download();
        return null;
    }

    /**
     * 【←前のXX件】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object prevX(AppContext appContext) throws Exception {
        return GS.OB1103;
    }
    
    /**
     * 【次のXX件→】 <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object nextY(AppContext appContext) throws Exception {
        return GS.OB1103;       
    }
}