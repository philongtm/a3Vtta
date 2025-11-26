/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/27		SSC				課題No.105 エラーメッセージ修正 
******************************************************************************/

package app.system.bss;

import app.SessionData;
import app.WorkFlowBean;
import app.system.dbAcc.WorkFlowTorokuDbAcc;
import app.system.form.WorkFlowTorokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * OS7105　業務フローパターンメンテナンス_登録 ビジネス ロジッククラス <br>
 */
public class WorkFlowTorokuBss {
	private AppContext appContext = null;                         // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                           // ＤＢアクセス
    private Log log = null;                                       // LOG
    private SessionData cmnData;                                  // 共通セッション
    private WorkFlowBean workBean;								   // 業務フローパターンメンテナンス情報Beanクラス
    private WorkFlowTorokuForm form;                              // アクションフォーム
    
    private static final String CNT                  = "cnt";						// カウント
    private static final String PATTERN_NAME_JP      = "pattern_name_jp";			// パターン名称（日本語）
    private static final String PATTERN_NAME_EN      = "pattern_name_en";			// パターン名称（英語）
    private static final String GEN_PHASE            = "gen_phase";				// 現フェーズ
    private static final String JI_PHASE             = "ji_phase";				// 次フェーズ
    
    private static final String[] TAIRYU_TOUROKU           = {"1030","2010","4010"};					// 滞留判定　登録
    private static final String[] TAIRYU_SHOUNIN           = {"2010","4010"};							// 滞留判定　承認	
    private static final String[] TAIRYU_KENSHOU_TOUROKU   = {"2030","4010"};							// 滞留判定検証　登録			
    private static final String[] TAIRYU_KENSHOU_SHOUNIN   = {"4010"};								// 滞留判定検証　承認			
    private static final String[] TAISHOU_TOUROKU          = {"3030"};								// 対象先選定　登録		
    private static final String[] TAISHOU_SHOUNIN          = {"4010"};								// 対象先選定　承認		
    private static final String[] ITI_TOUROKU              = {"4030","5010","6010","7010","8010"};	// 一次査定　登録	
    private static final String[] ITI_SHOUNIN              = {"5010","6010","7010","8010"};			// 一次査定　承認	
    private static final String[] ITI_KENSHOU_TOUROKU      = {"5030","6010","7010","8010"};			// 一次査定検証　登録			
    private static final String[] ITI_KENSHOU_SHOUNIN      = {"6010","7010","8010"};						// 一次査定検証　承認			
    private static final String[] NI_TOUROKU               = {"6030","7010","8010"};						// 二次査定　登録
    private static final String[] NI_SHOUNIN               = {"7010","8010"};								// 二次査定　承認
    private static final String[] HIKIATE_KENSHOU_TOUROKU  = {"7030"};									// 引当金検証　登録				
    private static final String[] HIKIATE_KAKUNIN_SHOUNIN  = {"8030"};									// 引当金確認　登録				
    private static final String[] KURE_TOUROKU             = {"6530"};									// クレーム債権　登録	
    private static final String[] KURE_SHOUNIN             = {"4010"};									// クレーム債権　承認	

    private static final int MAX_NUM_LENGTH				  = 200;
    
    /**
     * コンストラクタ
     */
    public WorkFlowTorokuBss(AppContext appContext) throws Exception {
        this.appContext = appContext;       
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.workBean = cmnData.getWorkflow_bean();
        this.form = (WorkFlowTorokuForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(新規ボタンから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);

        // システムセレクトボックス
        dbacc.getSystemkbn();

        // 汎用1セレクトボックス
        dbacc.getHanyou1();      
        
        // 2次査定区分セレクトボックス値取得
        dbacc.getNiSatei();

        // 滞留判定　登録セレクトボックス値取得
        form.setAr_tairyu_touroku(dbacc.getTugiPhase(TAIRYU_TOUROKU));
        
        
        // 滞留判定　承認セレクトボックス値取得
        form.setAr_tairyu_shounin(dbacc.getTugiPhase(TAIRYU_SHOUNIN));
        
        // 滞留判定検証　登録セレクトボックス値取得
        form.setAr_tairyu_kenshou_touroku(dbacc.getTugiPhase(TAIRYU_KENSHOU_TOUROKU));
        
        // 滞留判定検証　承認セレクトボックス値取得
        form.setAr_tairyu_kenshou_shounin(dbacc.getTugiPhase(TAIRYU_KENSHOU_SHOUNIN));
        
        // 対象先選定　登録セレクトボックス値取得
        form.setAr_taishou_touroku(dbacc.getTugiPhase(TAISHOU_TOUROKU));
        
        // 対象先選定　承認セレクトボックス値取得
        form.setAr_taishou_shounin(dbacc.getTugiPhase(TAISHOU_SHOUNIN));
        
        // 一次査定　登録セレクトボックス値取得
        form.setAr_iti_touroku(dbacc.getTugiPhase(ITI_TOUROKU));
        
        // 一次査定　承認セレクトボックス値取得
        form.setAr_iti_shounin(dbacc.getTugiPhase(ITI_SHOUNIN));
        
        // 一次査定検証　登録セレクトボックス値取得
        form.setAr_iti_kenshou_touroku(dbacc.getTugiPhase(ITI_KENSHOU_TOUROKU));
        
        // 一次査定検証　承認セレクトボックス値取得
        form.setAr_iti_kenshou_shounin(dbacc.getTugiPhase(ITI_KENSHOU_SHOUNIN));
        
        // 二次査定　登録セレクトボックス値取得
        form.setAr_ni_touroku(dbacc.getTugiPhase(NI_TOUROKU));
        
        // 二次査定　承認セレクトボックス値取得
        form.setAr_ni_shounin(dbacc.getTugiPhase(NI_SHOUNIN));
        
        // 引当金検証　登録セレクトボックス値取得
        form.setAr_hikiate_kenshou_touroku(dbacc.getTugiPhase(HIKIATE_KENSHOU_TOUROKU));
        
        // 引当金検証　承認セレクトボックス値取得
        form.setAr_hikiate_kakunin_touroku(dbacc.getTugiPhase(HIKIATE_KAKUNIN_SHOUNIN));
        
        // クレーム債権　登録セレクトボックス値取得
        form.setAr_kure_touroku(dbacc.getTugiPhase(KURE_TOUROKU));
        
        // クレーム債権　承認セレクトボックス値取得
        form.setAr_kure_shounin(dbacc.getTugiPhase(KURE_SHOUNIN));
        
        return GS.OS7105;
    }
    
    /**
     * 画面初期表示値取得(リンクから遷移時)
     */
    public String execute() throws Exception {  
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
        
        // システム区分
        form.setSystemkbn(workBean.getWorkflowSystemkbn());
        
        // システム区分名称
        form.setSystemkbn_nm(workBean.getWorkflowSystemkbn_nm());

        // 汎用1セレクトボックス
        form.setHanyou1(workBean.getBunrui1());
        
        // 業務フローパターン名称(日本語)
        form.setWorkflow_nm_ja(workBean.getWorkflow_nm_ja());
        
        // 業務フローパターン名称(英語)
        form.setWorkflow_nm_en(workBean.getWorkflow_nm_en());
        
        //更新する前の業務フローパターン名称をFormに格納
        form.setP_name_jp(workBean.getWorkflow_nm_ja());
        form.setP_name_en(workBean.getWorkflow_nm_en());
        
        // 2次査定区分セレクトボックス値取得
        dbacc.getNiSatei();

        // 滞留判定　登録セレクトボックス値取得
        form.setAr_tairyu_touroku(dbacc.getTugiPhase(TAIRYU_TOUROKU));
        
        // 滞留判定　承認セレクトボックス値取得
        form.setAr_tairyu_shounin(dbacc.getTugiPhase(TAIRYU_SHOUNIN));
        
        // 滞留判定検証　登録セレクトボックス値取得
        form.setAr_tairyu_kenshou_touroku(dbacc.getTugiPhase(TAIRYU_KENSHOU_TOUROKU));
        
        // 滞留判定検証　承認セレクトボックス値取得
        form.setAr_tairyu_kenshou_shounin(dbacc.getTugiPhase(TAIRYU_KENSHOU_SHOUNIN));
        
        // 対象先選定　登録セレクトボックス値取得
        form.setAr_taishou_touroku(dbacc.getTugiPhase(TAISHOU_TOUROKU));
        
        // 対象先選定　承認セレクトボックス値取得
        form.setAr_taishou_shounin(dbacc.getTugiPhase(TAISHOU_SHOUNIN));
        
        // 一次査定　登録セレクトボックス値取得
        form.setAr_iti_touroku(dbacc.getTugiPhase(ITI_TOUROKU));
        
        // 一次査定　承認セレクトボックス値取得
        form.setAr_iti_shounin(dbacc.getTugiPhase(ITI_SHOUNIN));
        
        // 一次査定検証　登録セレクトボックス値取得
        form.setAr_iti_kenshou_touroku(dbacc.getTugiPhase(ITI_KENSHOU_TOUROKU));
        
        // 一次査定検証　承認セレクトボックス値取得
        form.setAr_iti_kenshou_shounin(dbacc.getTugiPhase(ITI_KENSHOU_SHOUNIN));
        
        // 二次査定　登録セレクトボックス値取得
        form.setAr_ni_touroku(dbacc.getTugiPhase(NI_TOUROKU));
        
        // 二次査定　承認セレクトボックス値取得
        form.setAr_ni_shounin(dbacc.getTugiPhase(NI_SHOUNIN));
        
        // 引当金検証　登録セレクトボックス値取得
        form.setAr_hikiate_kenshou_touroku(dbacc.getTugiPhase(HIKIATE_KENSHOU_TOUROKU));
        
        // 引当金検証　承認セレクトボックス値取得
        form.setAr_hikiate_kakunin_touroku(dbacc.getTugiPhase(HIKIATE_KAKUNIN_SHOUNIN));
        
        // クレーム債権　登録セレクトボックス値取得
        form.setAr_kure_touroku(dbacc.getTugiPhase(KURE_TOUROKU));
        
        // クレーム債権　承認セレクトボックス値取得
        form.setAr_kure_shounin(dbacc.getTugiPhase(KURE_SHOUNIN));
        
        // 滞留判定　登録(チェックボックス)
        form.setBl_tairyu_touroku(Function.convFlag(workBean.getTairyu_hantei_t_flg()));
        
        // 滞留判定　承認(チェックボックス)
        form.setBl_tairyu_shounin(Function.convFlag(workBean.getTairyu_hantei_s_flg()));
        
        // 滞留判定検証　登録(チェックボックス)
        form.setBl_tairyu_kenshou_touroku(Function.convFlag(workBean.getTairyu_kensho_t_flg()));
        
        // 滞留判定検証　承認(チェックボックス)
        form.setBl_tairyu_kenshou_shounin(Function.convFlag(workBean.getTairyu_kensho_s_flg()));
        
        // 対象先選定　登録(チェックボックス)
        form.setBl_taishou_touroku(Function.convFlag(workBean.getTaishosaki_sentei_t_flg()));
        
        // 対象先選定　承認(チェックボックス)
        form.setBl_taishou_shounin(Function.convFlag(workBean.getTaishosaki_sentei_s_flg()));
        
        // 滞留判定　差戻
        form.setBl_tairyu_sa(Function.convFlag(workBean.getJimukyoku_sashi_flg()));
        
        // 一次査定　登録(チェックボックス)
        form.setBl_iti_touroku(Function.convFlag(workBean.getIchiji_satei_t_flg()));
        
        // 一次査定　承認(チェックボックス)
        form.setBl_iti_shounin(Function.convFlag(workBean.getIchiji_satei_s_flg()));
        
        // 一次査定検証　登録(チェックボックス)
        form.setBl_iti_kenshou_touroku(Function.convFlag(workBean.getIchiji_sateikensyo_t_flg()));
        
        // 一次査定検証　承認(チェックボックス)
        form.setBl_iti_kenshou_shounin(Function.convFlag(workBean.getIchiji_sateikensyo_s_flg()));
        
        // 二次査定　登録(チェックボックス)
        form.setBl_ni_touroku(Function.convFlag(workBean.getNiji_satei_t_flg()));
        
        // 二次査定　承認(チェックボックス)
        form.setBl_ni_shounin(Function.convFlag(workBean.getNiji_satei_s_flg()));
        
        // 査定完了　差戻
        form.setBl_satei(Function.convFlag(workBean.getSatei_kanryo_sashi_flg()));
        
        // 二次査定区分
        form.setNi_satei(workBean.getNiji_satei_kbn());
        // 引当金検証　登録(チェックボックス)
        form.setBl_hikiate_kenshou_touroku(Function.convFlag(workBean.getHikiatekin_kensyo_t_flg()));
        
        // 引当金検証　承認(チェックボックス)
        form.setBl_hikiate_kenshou_shounin(Function.convFlag(workBean.getHikiatekin_kensyo_s_flg()));
        
        // 引当金確認　登録(チェックボックス)
        form.setBl_hikiate_kakunin_touroku(Function.convFlag(workBean.getHikiatekin_kakunin_t_flg()));
        
        // 引当金確認  承認(チェックボックス)
        form.setBl_hikiate_kakunin_shounin(Function.convFlag(workBean.getHikiatekin_kakunin_s_flg()));
        
        // クレーム債権　登録(チェックボックス)
        form.setBl_kure_touroku(Function.convFlag(workBean.getClaim_reset_t_flg()));
        
        // クレーム債権　承認(チェックボックス)
        form.setBl_kure_shounin(Function.convFlag(workBean.getClaim_reset_s_flg()));
        
        // 代行設定
        form.setDaikou_settei(Function.convFlag(workBean.getDaikoset_flg()));
        
        // 査定会社メンテナンス
        form.setSettei_mentenansu(Function.convFlag(workBean.getSateikasya_flg()));
        
        // 業務フローパターンメンテナンス
        form.setGyoumu_mentenansu(Function.convFlag(workBean.getWorkflow_pattern_flg()));
        
        // ユーザマスタメンテナンス
        form.setUser_mentenansu(Function.convFlag(workBean.getUser_master_flg()));
        
        // 勘定科目マスタメンテナンス
        form.setKanjou_mentenansu(Function.convFlag(workBean.getKanjo_master_flg()));
        
        // 抽出条件メンテナンス（本社）
        form.setHonsha_mentenansu(Function.convFlag(workBean.getJoken_master_hq_flg()));
        
        // 抽出条件メンテナンス
        form.setJouken_mentenansu(Function.convFlag(workBean.getJoken_master_flg()));
        
        // チャンピオン部メンテナンス
        form.setChanpion_mentenansu(Function.convFlag(workBean.getChampion_bu_flg()));
        
        // ゴルフ会員権メンテナンス
        form.setKaiin_mentenansu(Function.convFlag(workBean.getGolf_kaiinken_flg()));
        
        // 連結区分マスタUPLOAD
        form.setRenketu_upload(Function.convFlag(workBean.getRenketsu_upload_flg()));
        
        // システム管理者専用
        form.setSystem_manager(Function.convFlag(workBean.getadmin_senyo_flg()));
        

        List<Map<String,String>> list = new ArrayList<Map<String,String>>();
        list = dbacc.selM1800();
        for(int i = 0; i < list.size(); i++){
        	Map<String,String> map = (Map<String,String>)list.get(i);
        	switch (Integer.parseInt(map.get(GEN_PHASE))) {
        		// 滞留判定　登録
        		case 1010:
        			form.setTairyu_touroku(map.get(JI_PHASE));
        			break;
        		// 滞留判定　承認
        		case 1030:
        			form.setTairyu_shounin(map.get(JI_PHASE));
        			break;
        		// 滞留判定検証　登録
        		case 2010:
        			form.setTairyu_kenshou_touroku(map.get(JI_PHASE));
        			break;
        		// 滞留判定検証　承認
        		case 2030:
        			form.setTairyu_kenshou_shounin(map.get(JI_PHASE));
        			break;
        		// 対象先選定　登録
        		case 3010:
        			form.setTaishou_touroku(map.get(JI_PHASE));
        			break;
        		// 対象先選定　承認
        		case 3030:
        			form.setTaishou_shounin(map.get(JI_PHASE));
        			break;
        		// 一次査定　登録
        		case 4010:
        			form.setIti_touroku(map.get(JI_PHASE));
        			break;
        		// 一次査定　承認
        		case 4030:
        			form.setIti_shounin(map.get(JI_PHASE));
        			break;
        		// 一次査定検証　登録
        		case 5010:
        			form.setIti_kenshou_touroku(map.get(JI_PHASE));
        			break;
        		// 一次査定検証　承認
        		case 5030:
        			form.setIti_kenshou_shounin(map.get(JI_PHASE));
        			break;
        		// 二次査定　登録
        		case 6010:
        			form.setNi_touroku(map.get(JI_PHASE));
        			break;
        		// 二次査定　承認
        		case 6030:
        			form.setNi_shounin(map.get(JI_PHASE));
        			break;
        		// 引当金検証　登録
        		case 7010:
        			form.setHikiate_kenshou_touroku(map.get(JI_PHASE));
        			break;
        		// 引当金確認　登録
        		case 8010:
        			form.setHikiate_kakunin_touroku(map.get(JI_PHASE));
        			break;
        		// クレーム債権　登録
        		case 6510:
        			form.setKure_touroku(map.get(JI_PHASE));
        			break;
        		// クレーム債権　承認
        		case 6530:
        			form.setKure_shounin(map.get(JI_PHASE));
        			break;
            	// 仮基準査定追加
        		default:
        			break;
        	}
        }
        
        // システムセレクトボックスが「GSS」（システム区分が'01'）で、二次査定の登録がチェックオンになっている時
        if(GS.GSS.equals(workBean.getWorkflowSystemkbn()) && GS.ON.equals(workBean.getNiji_satei_t_flg())){
        	form.setNi_satei_flg(true);
        }
        // システムセレクトボックスが「GSS」（システム区分が'01'）で、二次査定の承認がチェックオンになっている時
        if(GS.GSS.equals(workBean.getWorkflowSystemkbn()) && GS.ON.equals(workBean.getNiji_satei_s_flg())){
        	form.setNi_satei_flg(true);
        }
        
        
        return GS.OS7105;
    }
    
    /**
     * 
     * P02_区分からシステムに紐づく分類１を取得する <br>
     * 
     * @return
     * @throws Exception
     */
    public String doChange() throws Exception { 
    	
        // システムセレクトボックスが「GSS」（システム区分が'01'）で、二次査定の登録がチェックオンになっている時
        if(GS.GSS.equals(form.getSystemkbn()) && form.isBl_ni_touroku() || GS.GSS.equals(form.getSystemkbn()) && form.isBl_ni_shounin()){
        	form.setNi_satei_flg(true);
        }else{
        	form.setNi_satei_flg(false);
        }
    	
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
        dbacc.getHanyou1();
    	return GS.OS7105;
    }
    
    /**
     * 
     *  登録処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doToroku() throws Exception { 
    	
    	boolean result = true;
    	
        // システムセレクトボックスが「GSS」（システム区分が'01'）で、二次査定の登録、承認がチェックオンになっている時
        if(GS.GSS.equals(form.getSystemkbn()) && form.isBl_ni_touroku() || GS.GSS.equals(form.getSystemkbn()) && form.isBl_satei() ){
        	form.setNi_satei_flg(true);
        }else{
        	form.setNi_satei_flg(false);
        }
    	
    	// 入力チェック
    	if(!chkToroku()) {
    		
    		return false;
    	}
    	
    	// 重複チェック
    	if(!chkJuhuku()) {
    		
    		return false;
    	}
    	
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
        // パターンIDのMAX値を取得
        dbacc.selM1701();
        // M17_業務フローパターンマスタ（SSM_WORKFLOW_PATTERN）の登録を行う
        if(!dbacc.intM1700()){
        	// 登録時に一意制約違反（ORA-00001）になった場合、エラーメッセージ(err.Re-registration)表示する
        	appContext.setMsgCode(GL.ERR_RE_REGISTRATION);
        	return false;
        }
        // M18_実施業務マスタ（SSM_OPERATION）の登録を行う
        dbacc.intM1800();
        // コミット処理
        dbacc.commit();
    		
    	return result;
    }
    
    /**
     * 
     *  更新処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doUpdate() throws Exception { 
    	
    	// 入力チェック
    	if(!chkToroku()) {
    		
    		return false;
    	}
    	
    	// 重複チェック
    	if(!chkJuhuku()) {
    		
    		return false;
    	}
    	
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
        
    	// もぎ取り・承認待ちチェック
    	if(dbacc.sel_ts() > 0 || dbacc.sel_ss() > 0){
    		appContext.setMsgCode(GL.ERR_NOTEXECUTE);
    		return false;
    	}
    	
    	// 案件未完了チェック
    	if(dbacc.che_ts() > 0 || dbacc.che_st() > 0){
    		appContext.setMsgCode(GL.ERR_NOTANKEN);
    		return false;
    	}
    	
    	// M17_業務フローパターンマスタの更新
    	dbacc.upt_M17();
    	// M18_実施業務マスタの削除
    	dbacc.del_M18();
    	// M18_実施業務マスタの登録
    	dbacc.intM1800();
        // コミット処理
        dbacc.commit();
        
    	return true;
    }
    
    /**
     * 
     * 削除処理 <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doDelete() throws Exception { 
    	
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
    	
    	// 存在チェック
    	if(dbacc.sel_M24() > 0){
    		// 削除対象の業務フローパターンが、既にユーザに設定されている場合(SQL14の結果が１件以上取得された場合)はエラーメッセージ(err.Deletion)を表示する
    		appContext.setMsgCode(GL.ERR_DELETION);
    		return false;
    	}
    	
    	// 案件未完了チェック
    	if(dbacc.che_ts() > 0 || dbacc.che_st() > 0){
    		appContext.setMsgCode(GL.ERR_NOTANKEN);
    		return false;
    	}
    	
    	// M17_業務フローパターンマスタの削除を行う
    	dbacc.del_M17();
    	// M18_実施業務マスタの削除を行う
    	dbacc.del_M18();
        // コミット処理
        dbacc.commit();
    	
    	return true;
    }
    
    /**
     * 
     *  入力チェック<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkToroku() throws Exception { 
    	
    	boolean result = true;
    	
    	InputCheck inChk = new InputCheck();
    	// 新規も場合
    	if("1".equals(form.getGamenFlg())){
        	// 汎用１が未選択の場合
        	if(inChk.isNullBlank(form.getHanyou1())){
        		
        		appContext.setMsgCode(GL.ERR_SELECT,appContext.getCMN().getLbl_nm1());
        		return false;
        	}
    	}

    	// 業務フローパターン名称(日本語)が未入力の場合
    	if (inChk.isNullBlank(form.getWorkflow_nm_ja())) {
    		
    		appContext.setMsgCode(GL.ERR_INPUT,GL.OS7105_NAME_JP);
    		return false;
    	}
    	
    	// 業務フローパターン名称(日本語)が200バイトを超える場合
    	if (!inChk.islength(form.getWorkflow_nm_ja(), MAX_NUM_LENGTH)) {
    		
    		appContext.setMsgCode(GL.ERR_LENGTH,GL.OS7105_NAME_JP);
    		return false;
    	}
    	
    	// 業務フローパターン名称(日本語)に入力禁止文字が含まれている場合
    	if (chkKinshiChar(form.getWorkflow_nm_ja())) {
    		
    		return false;
    	}
    	
    	// 業務フローパターン名称(英語)が未入力の場合
    	if (inChk.isNullBlank(form.getWorkflow_nm_en())) {
    		
    		appContext.setMsgCode(GL.ERR_INPUT,GL.OS7105_NAME_EN);
    		return false;
    	}
    	
    	// 業務フローパターン名称（英語）が200バイトを超える場合
    	if (!inChk.islength(form.getWorkflow_nm_en(), MAX_NUM_LENGTH)) {
    		
    		appContext.setMsgCode(GL.ERR_LENGTH,GL.OS7105_NAME_EN);
    		return false;
    	}
    	
    	// 業務フローパターン名称（英語）に入力禁止文字が含まれている場合
    	if (chkKinshiChar(form.getWorkflow_nm_en())) {
    		
    		return false;
    	}
    	
    	// 課題No.105
    	// 修正開始
    	List<String> msgList = new ArrayList<String>();
    	msgList.add(GL.ERR_SELECT2);
    	// 次フェーズセレクトボックスが未設定の場合
    	// 滞留判定 登録
    	if (form.isBl_tairyu_touroku() && inChk.isNullBlank(form.getTairyu_touroku())) {
        	msgList.add(GL.OS7105_TAIRYU_HANTEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAIRYU_HANTEI_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 滞留判定 承認
    	if (form.isBl_tairyu_shounin() && inChk.isNullBlank(form.getTairyu_shounin())) {
        	msgList.add(GL.OS7105_TAIRYU_HANTEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAIRYU_HANTEI_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 滞留判定検証 登録
    	if (form.isBl_tairyu_kenshou_touroku() && inChk.isNullBlank(form.getTairyu_kenshou_touroku())) {
        	msgList.add(GL.OS7105_TAIRYU_KENSHOU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAIRYU_KENSHOU_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 滞留判定検証 承認
    	if (form.isBl_tairyu_kenshou_shounin() && inChk.isNullBlank(form.getTairyu_kenshou_shounin())) {
        	msgList.add(GL.OS7105_TAIRYU_KENSHOU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAIRYU_KENSHOU_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 対象先選定　登録
    	if (form.isBl_taishou_touroku() && inChk.isNullBlank(form.getTaishou_touroku())) {
        	msgList.add(GL.OS7105_TAISHOU_SENTEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAISHOU_SENTEI_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 対象先選定　承認
    	if (form.isBl_taishou_shounin() && inChk.isNullBlank(form.getTaishou_shounin())) {
        	msgList.add(GL.OS7105_TAISHOU_SENTEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_TAISHOU_SENTEI_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 一次査定　登録
    	if (form.isBl_iti_touroku() && inChk.isNullBlank(form.getIti_touroku())) {
        	msgList.add(GL.OS7105_ITI_SATEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_ITI_SATEI_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 一次査定　承認
    	if (form.isBl_iti_shounin() && inChk.isNullBlank(form.getIti_shounin())) {
        	msgList.add(GL.OS7105_ITI_SATEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_ITI_SATEI_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 一次査定検証　登録
    	if (form.isBl_iti_kenshou_touroku() && inChk.isNullBlank(form.getIti_kenshou_touroku())) {
        	msgList.add(GL.OS7105_ITI_KENSHOU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_ITI_KENSHOU_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 一次査定検証　承認
    	if (form.isBl_iti_kenshou_shounin() && inChk.isNullBlank(form.getIti_kenshou_shounin())) {
        	msgList.add(GL.OS7105_ITI_KENSHOU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_ITI_KENSHOU_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 二次査定　登録
    	if (form.isBl_ni_touroku() && inChk.isNullBlank(form.getNi_touroku())) {
        	msgList.add(GL.OS7105_NI_SATEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_NI_SATEI_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 二次査定　承認
    	if (form.isBl_ni_shounin() && inChk.isNullBlank(form.getNi_shounin())) {
        	msgList.add(GL.OS7105_NI_SATEI);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_NI_SATEI_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 二次査定の登録、承認がチェックオンになっている時、二次査定区分セレクトボックスが未設定の場合
    	if (form.isNi_satei_flg() && GS.EMPTY_CHARCTER.equals(form.getNi_satei())) {
    		
    		appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_NI_SATEI_KBN);
    		return false;
    	}
    	
    	// 引当金検証　登録
    	if (form.isBl_hikiate_kenshou_touroku() && inChk.isNullBlank(form.getHikiate_kenshou_touroku())) {
        	msgList.add(GL.OS7105_HIKIATE_KENSHOU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_HIKIATE_KENSHOU_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// 引当金確認　登録
    	if (form.isBl_hikiate_kakunin_touroku() && inChk.isNullBlank(form.getHikiate_kakunin_touroku())) {
        	msgList.add(GL.OS7105_HIKIATE_KAKUNIN);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_HIKIATE_KAKUNIN_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// クレーム債権　登録
    	if (form.isBl_kure_touroku() && inChk.isNullBlank(form.getKure_touroku())) {
        	msgList.add(GL.OS7105_KUREMU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_KUREMU_S);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	
    	// クレーム債権　承認
    	if (form.isBl_kure_shounin() && inChk.isNullBlank(form.getKure_shounin())) {
        	msgList.add(GL.OS7105_KUREMU);
        	msgList.add(GL.OS7105_WORK_FLOW);
    		//appContext.setMsgCode(GL.ERR_SELECT,GL.OS7105_KUREMU_T);
    		appContext.setMsgCode(msgList);
    		return false;
    	}
    	// 修正完了
    	
    	return result;
    }
    
    /**
     * 
     *  重複チェック<br>
     * 
     * @return
     * @throws Exception 
     */
    public boolean chkJuhuku() throws Exception {
    	
    	boolean result = true;
    	String workflowNmJa = form.getWorkflow_nm_ja();
    	String workflowNmEn = form.getWorkflow_nm_en();
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowTorokuDbAcc dbacc = new WorkFlowTorokuDbAcc(sqlExec, log, appContext);
        
        Map<String,String> map = dbacc.selM1700();
        // 重複チェック
        if (Integer.parseInt(map.get(CNT)) > 0) {
        	// 名称日本語重複の場合
        	if(workflowNmJa.equals(map.get(PATTERN_NAME_JP))){
        		appContext.setMsgCode(GL.ERR_REGISTERED,GL.OS7105_NAME_JP);
        		return false;
        	}
        	// 名称英語重複の場合
        	if(workflowNmEn.equals(map.get(PATTERN_NAME_EN))){
        		appContext.setMsgCode(GL.ERR_REGISTERED,GL.OS7105_NAME_EN);
        		return false;
        	}
        }
    	
    	return result;
    }
    
    /**
     * 
     * チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示 <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chkKinshiChar(String str) throws Exception {
    	InputCheck inChk = new InputCheck();
    	// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する
    	for(int i = 0; i < str.length(); i++){
    		String kinshiChar = str.substring(i, i + 1);
    		if (inChk.haveKinshiMoji(kinshiChar)) {
				// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
				appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
				return true;
			}
    	}
    	return false;
    }
    
}
