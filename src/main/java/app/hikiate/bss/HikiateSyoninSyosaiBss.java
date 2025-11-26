/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.hikiate.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.dbAcc.HikiateSyoninSyosaiDbAcc;
import app.hikiate.form.HikiateSyoninSyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.util.Map;

/**
 * OD1104_引当金確認_承認 ビジネスロジッククラス<br>
 */
public class HikiateSyoninSyosaiBss {
    private AppContext appContext = null;						// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;						// ＤＢアクセス
    private Log log = null;									// LOG
    private SessionData cmnData;								// 共通セッション
    private UserBean user_bean;								// 共通セッション
    private TorihikisakiBean tori_bean;     					// 取引先情報
    private HikiateSyoninSyosaiForm form;						// アクションフォーム
    
    private static final String JISHI_PHASE 				= "JISHI_PHASE";            // 実施フェーズ
    private static final String KAISHI_STATUS 			= "KAISHI_STATUS";          // 開始ステータス
    private static final String STATUS_30 				= "30";            			// 開始ステータス
    private static final String KAKUNIN_TAB 				= "1";            			// 開始ステータス
    private static final String KENSYO_TAB 				= "2";            			// 開始ステータス

    /**
     * コンストラクタ
     */
    public HikiateSyoninSyosaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;       
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.tori_bean = cmnData.getTori_bean();
        this.form = (HikiateSyoninSyosaiForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
    	
    	// 汎用１を設定する
    	form.setHanyou1(tori_bean.getSateikaisya_cd());
    	// 組織を設定する
    	form.setSoshiki_nm(tori_bean.getSoshiki());
    	// 勘定先CDを設定する
    	form.setKanjo_cd(tori_bean.getKanjo_cd());
    	// 勘定先名称を設定する
    	form.setKanjo_nm(tori_bean.getKanjo_nm());
    	// 表示タブ判定用変数に設定する
    	if(GS.PHASE_HIKIATEKIN_KAKUNIN.equals(tori_bean.getPhase())){
        	form.setTabId(KAKUNIN_TAB);
    	}else{
        	form.setTabId(KENSYO_TAB);
    	}
    	
        return GS.OD1104;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  
    	
    	// 汎用１を設定する
    	form.setHanyou1(tori_bean.getSateikaisya_cd());
    	// 組織を設定する
    	form.setSoshiki_nm(tori_bean.getSoshiki_nm());
    	// 勘定先CDを設定する
    	form.setKanjo_cd(tori_bean.getKanjo_cd());
    	// 勘定先名称を設定する
    	form.setKanjo_nm(tori_bean.getKanjo_nm());
    	// 表示タブ判定用変数に設定する
    	if(GS.PHASE_HIKIATEKIN_KAKUNIN.equals(tori_bean.getPhase())){
        	form.setTabId(KAKUNIN_TAB);
    	}else{
        	form.setTabId(KENSYO_TAB);
    	}
        
        return GS.OD1104;
    }
    
    /**
     * 
     *  承認実行処理<br>
     * 
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public boolean doZikko() throws Exception {  
    	
    	if(user_bean.getComOparation() == null){
    		return false;
    	}
    	
    	String phase = null;
        // 共)ユーザ情報.実施業務【リスト】から、実施フェーズ = 共)取引先情報.フェーズで取得した実施業務の実施フェーズ
        for(int i = 0; i < user_bean.getComOparation().size(); i++){
        	Map<String,String> map = (Map<String,String>) user_bean.getComOparation().get(i);
        	if(map.get(JISHI_PHASE) != null && map.get(JISHI_PHASE).equals(tori_bean.getPhase())){
        		
        		// 実施フェーズ
        		phase = map.get(JISHI_PHASE);
        	}
        }
    	
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        HikiateSyoninSyosaiDbAcc dbacc = new HikiateSyoninSyosaiDbAcc(sqlExec, log, appContext);
    	
    	// T14_査定進捗管理の更新
    	dbacc.uptT14(phase);
    	// T13_入力履歴の登録
    	dbacc.intT13();
        // コミット処理
        dbacc.commit();
        
        return true;
    }
}
