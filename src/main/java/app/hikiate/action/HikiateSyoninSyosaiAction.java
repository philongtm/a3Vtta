/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/24		SSC				課題No.161 引当金確認_承認 表示不正
003		2015/06/02		SSC				BJ201502003 ICISプロジェクト対応
******************************************************************************/

package app.hikiate.action;

import app.SessionData;
import app.common.action.KakuninSyokaiAction;
import app.common.action.KensyoSyokaiAction;
import app.common.action.SashimodoshiAction;
import app.common.action.TenpuSyokaiAction;
import app.hikiate.bss.HikiateSyoninSyosaiBss;
import app.hikiate.form.HikiateSyoninSyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OD1104 引当金確認_承認 アクションクラス <br>
 */
public class HikiateSyoninSyosaiAction extends AppMenuAction {
	
	private static final String HIKIATESYONINSYOSAIFORM = "06HikiateSyoninSyosaiForm"; // 引当金確認_承認のフォーム
    private static final String KAKUNIN_TAB 				= "1";            			// 開始ステータス
	
	/**
	 * ディスパッチマップ作成 <br>
	 */
	public HashMap getKeyMethodMap() {

	    //ディスパッチアップ作成
		HashMap<String,String> map = new HashMap<String,String>();
        map = super.getKeyMethodMap(map);
        map.put("sashi", "sashi");		// 差戻アクション
        map.put("zikko", "zikko");		// 承認実行アクション
        map.put("tenpu", "tenpu");		// 添付参照アクション
        map.put("back", "back");		// 戻るアクション
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
        HikiateSyoninSyosaiForm form = new HikiateSyoninSyosaiForm();
        appContext.setActionForm(form);

        // ビジネスロジック実行
        HikiateSyoninSyosaiBss bss = new HikiateSyoninSyosaiBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(HIKIATESYONINSYOSAIFORM, form);
        
        // 共通タブ使用のため共通セッションに値セット
        SessionData cmnData = appContext.getCMN();
        cmnData.setTab_riyou_gamenId(form.toString());
        
        // 共通タブ初期表示メソッド呼び出し
        if(KAKUNIN_TAB.equals(form.getTabId())){
            KakuninSyokaiAction acc = new KakuninSyokaiAction();
            acc.appExecute(appContext);
        }else{
        	KensyoSyokaiAction acc = new KensyoSyokaiAction();
            acc.appExecute(appContext);
        }
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
        HikiateSyoninSyosaiForm form = new HikiateSyoninSyosaiForm();
        appContext.setActionForm(form);
        // ビジネスロジック実行
        HikiateSyoninSyosaiBss bss = new HikiateSyoninSyosaiBss(appContext); 
        String result = bss.executeInit();
        // sessionスコープにActionFormを登録
        appContext.setSessionActionForm(HIKIATESYONINSYOSAIFORM, form);
        // 共通タブ使用のため共通セッションに値セット
        SessionData cmnData = appContext.getCMN();
        cmnData.setTab_riyou_gamenId(form.toString());
        
        // 共通タブ初期表示メソッド呼び出し
        if(KAKUNIN_TAB.equals(form.getTabId())){
            KakuninSyokaiAction acc = new KakuninSyokaiAction();
            acc.appExecute(appContext);
        }else{
        	KensyoSyokaiAction acc = new KensyoSyokaiAction();
            acc.appExecute(appContext);
        }
        return result;
    }
    
    /**
     * 差戻アクション <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */ 
    public Object sashi(AppContext appContext) throws Exception {
        // 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OD1104)を設定する。
        cmnData.setReturn_gamenId(GS.OD1104);
        // OZ2101_差戻先選択へ遷移する。
		SashimodoshiAction acc = new SashimodoshiAction();
		acc.appExecute(appContext);
        return GS.OZ2101;
    }
    
    /**
     * 
     * 承認実行アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object zikko(AppContext appContext) throws Exception {   
        // ビジネスロジック実行
    	HikiateSyoninSyosaiBss bss = new HikiateSyoninSyosaiBss(appContext); 
        if(bss.doZikko()){
        	HikiateSyoninAction acc = new HikiateSyoninAction();
        	// 課題No.161
        	// 追加開始
        	//acc.appExecute(appContext);
        	acc.appReExecute(appContext);
        	// 追加完了
        	// OD1103_引当金確認_承認一覧へ遷移する
        	return GS.OD1103;
        }else{
        	 return GS.OD1104;
        }
    }
    
    /**
     * 
     * 添付参照アクション <br>
     * 
     * @param appContext
     * @return
     * @throws Exception
     */
    public Object tenpu(AppContext appContext) throws Exception {   
    	// 機能共通セッションを取得する。
        SessionData cmnData = appContext.getCMN();
        // 共)遷移元画面IDに当画面ID(OD1104)を設定する。
        cmnData.setReturn_gamenId(GS.OD1104);
        // OZ1102_添付内容照会へ遷移する
		TenpuSyokaiAction acc = new TenpuSyokaiAction();
		acc.appExecute(appContext);
	    
        return GS.OZ1102;
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
    	HikiateSyoninAction acc = new HikiateSyoninAction();
    	acc.appReExecute(appContext);
    	// OD1103_引当金確認_承認一覧へ遷移する
        return GS.OD1103;
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
