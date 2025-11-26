/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/10		SSC				課題No.35 案件0件時の承認処理修正 
003		2015/02/23		SSC				案件No.BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.dbAcc.SyoninDbAcc;
import app.tairyu.form.SyoninForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 * OB1104_実質滞留債権判定_承認一覧 ビジネス ロジッククラス <br>
 */
public class SyoninBss {

	private AppContext appContext = null;					                    				// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               					// ＤＢアクセス
	private Log log = null;									                				// LOG
	private SessionData cmnData;							                    				// 共通セッション
	private UserBean user_bean;								                				// ユーザービンー
	private SyoninForm form;                                                  					// アクションフォーム
    
    private static final String CHECKBOX_STATUS_ON			= "on";							// チェックボックスのステータス:オン
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SyoninBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (SyoninForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SyoninDbAcc dbacc = new SyoninDbAcc(sqlExec, log, appContext);
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
		
        // ログインユーザの参照フェーズ設定
        StringBuffer sansyo_phase = new StringBuffer();
        if (user_bean.getComTairyu_hantei_s_flg().equals(GS.ON) && user_bean.getComTairyu_kensho_s_flg().equals(GS.ON)){
            // 滞留判定・滞留判定検証両方
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI);
            sansyo_phase.append(GS.COMMA);
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI_KENSHO);
        }else if(user_bean.getComTairyu_hantei_s_flg().equals(GS.ON)){
            // 滞留判定のみ
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI);
        }else if(user_bean.getComTairyu_kensho_s_flg().equals(GS.ON)){
            // 滞留判定検証のみ
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI_KENSHO);   
        }
        form.setSansyo_phase(sansyo_phase.toString());

        // 一覧情報取得
        dbacc.getMeisai();        
        
        // T14_査定進捗管理の更新(ループ処理)
        // 課題No.35
        // 追加開始
        if(form.getAr_meisai() != null){
        // 追加完了
            for (int i = 0; i < form.getAr_meisai().size(); i++) {
            	TorihikisakiBean meisaiBean = (TorihikisakiBean)form.getAr_meisai().get(i);
            	dbacc.setMeisaiBean(meisaiBean);
            	dbacc.setInitT0800TorimodoshiFukaFlg();
            }        
        }
        
        dbacc.commit();
        
		return GS.OB1104;
	}
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     * 
     * @return
     * @throws Exception
     */
    public String execute() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyoninDbAcc dbacc = new SyoninDbAcc(sqlExec, log, appContext);

        // ログインユーザの参照フェーズ設定
        StringBuffer sansyo_phase = new StringBuffer();
        if (user_bean.getComTairyu_hantei_s_flg().equals(GS.ON) && user_bean.getComTairyu_kensho_s_flg().equals(GS.ON)){
            // 滞留判定・滞留判定検証両方
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI);
            sansyo_phase.append(GS.COMMA);
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI_KENSHO);
        }else if(user_bean.getComTairyu_hantei_s_flg().equals(GS.ON)){
            // 滞留判定のみ
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI);
        }else if(user_bean.getComTairyu_kensho_s_flg().equals(GS.ON)){
            // 滞留判定検証のみ
            sansyo_phase.append(GS.PHASE_TAIRYU_HANTEI_KENSHO);   
        }
        form.setSansyo_phase(sansyo_phase.toString());
        
        // 一覧情報取得
        dbacc.getMeisai();
    
        // T14_査定進捗管理の更新(ループ処理)
        // 課題No.35
        // 追加開始
        if(form.getAr_meisai() != null){
        // 追加完了
            for (int i = 0; i < form.getAr_meisai().size(); i++) {
            	TorihikisakiBean meisaiBean = (TorihikisakiBean)form.getAr_meisai().get(i);
            	dbacc.setMeisaiBean(meisaiBean);
            	dbacc.setInitT0800TorimodoshiFukaFlg();
            }
        }
        
        dbacc.commit();
        
        return GS.OB1104;
    }
    
    /**
     * 【承認実行ボタン押し処理】 <br>
     * 
     * @param appContext
     * @return 画面ID
     * @throws Exception
     */
    public String doSyonin(SyoninForm form) throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyoninDbAcc dbacc = new SyoninDbAcc(sqlExec, log, appContext);
        
        // 明細情報を取得
        List ar_meisai = form.getAr_meisai();    

        // 承認対象が存在しないとき
        // 課題No.35
        // 追加開始
        if(ar_meisai != null){
        // 追加完了
            if (ar_meisai.size() < 1) {
            	return GS.OB1104;
            }
        }else{
        	return GS.OB1104;
        }
        
        // 承認対象が存在かとかチェック
        boolean syonin_flg = this.chk_syonin();
        if (!syonin_flg) {
            appContext.setMsgCode(GL.ERR_INPUT, GL.OB1104_SYONIN);
            return GS.OB1104;
        }
        
        // チャンピオン部重複チェック
        boolean championbu = this.chk_Championbu();
        if (championbu) {
            appContext.setMsgCode(GL.ERR_DUPLICATION);
            return GS.OB1104;
        }
        
        // 明細一覧データが繰り返し
        for (int i = 0; i < ar_meisai.size(); i++) {            
            
        	TorihikisakiBean meisaiBean = (TorihikisakiBean) ar_meisai.get(i);
            
            // 承認チェックボックスオフの場合、次のデータへ
            if ((GS.EMPTY_CHARCTER).equals(meisaiBean.getSyonin_chk())) {
                continue;
            } 
            
            form.setMeisai_phase(meisaiBean.getPhase());
            // 次実施フェーズ、次開始ステータスを取得する
            this.getJisshiGyoumu();            
            
            // 処理区分を取得
            this.getJisshiGyoumuKbn();
            
            // 明細ビンーを設定する
            dbacc.setMeisaiBean(meisaiBean);
            
            switch (form.getJisshi_gyoumu_kbn()) {            
            case 0:
                // 処理③:T10_滞留判定の登録（SQL04）
                dbacc.setOBInsertT1000();
                
                // 処理④:T08_滞留判定進捗管理の更新（SQL05）
                form.setUpd_taityu_kbn("0");
                dbacc.setOUpdateT0800(); 
                
                // 処理⑥:T13_入力履歴の登録（SQL07）
                dbacc.setOInsertT1300();
                
                //処理⑩:T04_メール配信の登録を行う（SQL18）
                dbacc.setOBInsertT0400();
                break;
            case 1:
            	// 処理⑤:T08_滞留判定進捗管理の更新（SQL06）
            	form.setUpd_taityu_kbn("1");
                dbacc.setOUpdateT0800(); 
                
                // 処理⑥:T13_入力履歴の登録（SQL07）
                dbacc.setOInsertT1300();
                
                // 処理⑦:滞留判定済みチェック（SQL08）
                int cntAnken_no = dbacc.getOBSelectT0802();
                
                if (cntAnken_no >= 1) {
                	// 次のデータへ
                    continue;
                }
                
                // 処理⑧:T01_対象先の一次査定対象FLGをチェックする（SQL09）
                String ichiji_flg = dbacc.getOBSelectT0100();
                
                if (!"1".equals(ichiji_flg)) {
                	if (dbacc.selM09() != 0) {
                    	// 処理⑧:査定作成条件のチェックを行う。（SQL10）
                    	int satei_flg = dbacc.getOBSelectSateicheck(); 
                        if (satei_flg == 2) {
                        	// 次のデータへ
                        	continue;
                        }
                    }
                }

                // 処理⑧:チャンピオン部を再選定する（SQL19,20,21）
                // 共)ユーザ情報.業務フローパターンシステム区分が'01'：GSSの場合
                if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())) {
                	//現チャンピオン部、滞留債権額が最も大きい部を取得
                    String championbu_cd = dbacc.selCh();
                    String max_tairyu_bu_cd = dbacc.selT09();
                    // 現チャンピオン部と滞留債権額が最も大きい部が一致しない場合、T07_チャンピオン部を更新する。
                    if(max_tairyu_bu_cd != null){
                    	if(!championbu_cd.equals(max_tairyu_bu_cd)){
                            dbacc.updT07(championbu_cd);
                            dbacc.updT07_2(max_tairyu_bu_cd);
                        }
                    }
                }
                
                // 処理⑨:査定データ作成
            	// T01_対象先の査定案件Noの取得
            	String satei_anken_no = dbacc.getSateiAnkenNo();

            	// T16_引当金検討対象BS明細の登録（SQL11）
                dbacc.setOBInsertTairyumeisai(satei_anken_no);
                
                // T16_引当金検討対象BS明細の登録（SQL12）
                dbacc.setOBInsertMeisai(); 
                
                // T17_引当金判定表示用の登録（SQL13）
                dbacc.setOBInsertSateistat00(); 
                
                // T14_査定進捗管理の登録（SQL14）
                dbacc.setOBInsertSateistat01(); 
                
                // T01_対象先の更新（SQL15）
                dbacc.setOBUpdateT0100(); 
                
                // T08_滞留判定進捗管理の更新（SQL16）
                dbacc.setOBUpdateT0801();
                
                // T15_一次二次査定の登録（SQL17）
                dbacc.setOBInsertSatei(); 
                
                //処理⑩:T04_メール配信の登録を行う（SQL18）
                dbacc.setOBInsertT0400(); 
                break;
            case 2:
            	continue;
			default:
				break;
            }
        }

        // 一覧情報再表示
        dbacc.getMeisai();

        // T14_査定進捗管理の更新(ループ処理)
        // 課題No.35
        // 追加開始
        if(form.getAr_meisai() != null){
        // 追加完了
            for (int i = 0; i < form.getAr_meisai().size(); i++) {
            	TorihikisakiBean meisaiBean = (TorihikisakiBean)form.getAr_meisai().get(i);
            	dbacc.setMeisaiBean(meisaiBean);
            	dbacc.setInitT0800TorimodoshiFukaFlg();
            }
        }

        dbacc.commit();
        
        return GS.OB1104;
    }
    
    /**
     * 【一括承認チッェクボックス実行処理】 <br>
     * 
     * @param appContext
     * @return 画面ID
     * @throws Exception
     */
    public String doIkatuSyonin(SyoninForm form) throws Exception {

        List ar_meisai = form.getList();
        // 明細承認
        String syonin = GS.EMPTY_CHARCTER;        
        // 一括承認チッェクオン時、明細承認すべてチェックオンにする
        if (form.isIkt_syonin()) {
            syonin = CHECKBOX_STATUS_ON;
        }

        // 課題No.35
        // 追加開始
		if(ar_meisai != null){
	    // 追加完了
			for (int i = 0; i < ar_meisai.size(); i++) {
				TorihikisakiBean meisaiBean = (TorihikisakiBean) ar_meisai.get(i);
				meisaiBean.setSyonin_chk(syonin);
			}
		}

        // ActionForm に明細を格納
        //form.setAr_meisai(ar_meisai);
        
        return GS.OB1104;
    }
    
    /**
     * 【次実施フェーズ、次開始ステータスを取得する】 <br>
     * 
     * @throws Exception
     */
    private void getJisshiGyoumu() throws Exception {
        
        // 共)実施業務【リスト】
        List comOparation = user_bean.getComOparation();
        if (comOparation == null) {
            return;
        }
        
        for (Object oparation : comOparation) {
            // 実施業務
            Map mapOparation = (Map) oparation;
            // 実施フェーズ
            Object phase = mapOparation.get(GS.JISHI_PHASE);
            // 開始ステータス
            Object status = mapOparation.get(GS.KAISHI_STATUS);

            // 次開始ステータスを取得する。
            if (phase != null && phase.equals(form.getMeisai_phase()) && GS.STATUS_SYONIN_MACHI.equals(status)) {
                // 次実施フェーズ
                form.setJi_jishi_phase((String)mapOparation.get(GS.JI_JISHI_PHASE));
                // 次開始ステータス
                form.setJi_kaishi_status((String) mapOparation.get(GS.JI_KAISHI_STATUS));
                return;
            }
        }
    }
        
    /**
     * 【処理区分を取得】 <br>
     * 
     * @param appContext
     * @return 次実施フェーズ、次開始ステータス
     * @throws Exception
     */
    private void getJisshiGyoumuKbn() throws Exception {
        
    	if (form.getJi_jishi_phase().equals(GS.PHASE_TAIRYU_HANTEI_KENSHO)
    		&& form.getJi_kaishi_status().equals(GS.STATUS_MISYORI)) {
    		form.setJisshi_gyoumu_kbn(0);
    	} else if (form.getJi_jishi_phase().equals(GS.PHASE_ICHIJI_SATEI)
    				&& form.getJi_kaishi_status().equals(GS.STATUS_MISYORI)) {
    		form.setJisshi_gyoumu_kbn(1);
    	} else {
    		form.setJisshi_gyoumu_kbn(2);
    	}
    }    
    
    /**
     * チャンピオン部重複チェック <br>
     * 
     * @return
     * @throws Exception
     */
    private boolean chk_Championbu() throws Exception {        
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyoninDbAcc dbacc = new SyoninDbAcc(sqlExec, log, appContext);
        
        for (int i = 0; i < form.getAr_meisai().size(); i++) {    
        	
        	TorihikisakiBean meisaiBean = (TorihikisakiBean) form.getAr_meisai().get(i);
        	
            // 承認チェックボックスオフの場合、次のデータへ
            if ((GS.EMPTY_CHARCTER).equals(meisaiBean.getSyonin_chk())) {
                continue;
            }
            
            // 処理②:チャンピオン部重複チェック（SQL03）
            int cntBu_cd = 0;
            if ((GS.GSS).equals(user_bean.getComWorkflowSystemkbn())) {
            	dbacc.setMeisaiBean(meisaiBean);
                cntBu_cd = dbacc.getObSelectT0700(); 
            }
            
            // 2件以上の場合、エラーダイアログを表示し
            if (cntBu_cd >= 2) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 承認対象が存在かとかチェック <br>
     * 
     * @return
     * @throws Exception
     */
    private boolean chk_syonin() throws Exception {        
        
        // 承認チェックボックスチェック
        for (int i = 0; i < form.getAr_meisai().size(); i++) {  
        	TorihikisakiBean meisaiBean = (TorihikisakiBean) form.getAr_meisai().get(i);
            if ((CHECKBOX_STATUS_ON).equals(meisaiBean.getSyonin_chk())) {
                return true;
            }
        }
        
        return false;
    }
}
