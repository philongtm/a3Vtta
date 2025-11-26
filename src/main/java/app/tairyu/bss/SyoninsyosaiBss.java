/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/02/23		SSC				案件No.BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.dbAcc.SyoninsyosaiDbAcc;
import app.tairyu.form.SyoninsyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 * OB1105_実質滞留債権判定_承認 ビジネス ロジッククラス <br>
 */
public class SyoninsyosaiBss {

	private AppContext appContext = null;					                   // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               // ＤＢアクセス
	private Log log = null;									               // LOG
	private SessionData cmnData;							                   // 共通セッション
	private UserBean user_bean;								               // ユーザービンー
	private SyoninsyosaiForm form = null;	
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SyoninsyosaiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		form = (SyoninsyosaiForm)appContext.getActionForm();
	}
    
    /**
     * 【承認実行ボタン押し処理】 <br>
     * 
     * @param appContext
     * @return 画面ID
     * @throws Exception
     */
    public String doSyonin() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SyoninsyosaiDbAcc dbacc = new SyoninsyosaiDbAcc(sqlExec, log, appContext);
        
        // 明細情報を取得
        TorihikisakiBean meisaiBean = appContext.getCMN().getTori_bean(); 
                
        form.setMeisai_phase(meisaiBean.getPhase());
        // 次実施フェーズ、次開始ステータスを取得する
        this.getJisshiGyoumu();
        
        // 処理区分を取得
        this.getJisshiGyoumuKbn();
        
        // 明細ビンーを設定する
        dbacc.setMeisaiBean(meisaiBean);
        
        boolean syori_kbn = true;
        switch (form.getJisshi_gyoumu_kbn()) {            
        case 0:
            // 処理②:T10_滞留判定の登録（SQL02）
            dbacc.setOBInsertT1000();
                        
            // 処理③:T08_滞留判定進捗管理の更新（SQL03）
            form.setUpd_taityu_kbn("0");
            dbacc.setOUpdateT0800(); 
            
            // 処理⑤:T13_入力履歴の登録（SQL05）
            dbacc.setOInsertT1300();            
            
            //処理⑩:T04_メール配信の登録を行う（SQL18）
            dbacc.setOBInsertT0400();  
            
            break;   
        case 1:
            // 処理①:チャンピオン部重複チェック（SQL01）
            int cntBu_cd = 0;
            if ((GS.GSS).equals(user_bean.getComWorkflowSystemkbn())) {
                cntBu_cd = dbacc.getObSelectT0700(); 
            }
            
            // 2件以上の場合、エラーダイアログを表示し
            if (cntBu_cd >= 2) {
                appContext.setMsgCode(GL.ERR_DUPLICATION);
                return GS.OB1105;
            }
            
            // 処理④:T08_滞留判定進捗管理の更新（SQL04）
            form.setUpd_taityu_kbn("1");
            dbacc.setOUpdateT0800(); 
            
            // 処理⑤:T13_入力履歴の登録（SQL05）
            dbacc.setOInsertT1300();
            
            // 処理⑥:滞留判定済みチェック（SQL06）
            int cntAnken_no = dbacc.getOBSelectT0802();
            
            if (cntAnken_no >= 1) {
            	syori_kbn = false;
            }
            
            // 処理⑦:T01_対象先の一次査定対象FLGをチェックする（SQL07）
            String ichiji_flg = dbacc.getOBSelectT0100();
            
            if (!"1".equals(ichiji_flg)) {
            	if (dbacc.selM09() != 0) {
                    // 処理⑦:査定作成条件のチェックを行う。（SQL08）
                    int satei_flg = dbacc.getOBSelectSateicheck(); 
                    if (satei_flg == 2) {
                    	syori_kbn = false;
                    }
                }

            }
            
            if (syori_kbn) {            	

				// 処理⑨:チャンピオン部を再選定する（SQL22,23,24,25）
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

            	// 処理⑧:査定データ作成

            	// T01_対象先の査定案件Noの取得
            	String satei_anken_no = dbacc.getSateiAnkenNo();

                // T16_引当金検討対象BS明細の登録（SQL09）
                dbacc.setOBInsertTairyumeisai(satei_anken_no);
                
                // T16_引当金検討対象BS明細の登録（SQL10）
                dbacc.setOBInsertMeisai(); 
                
                // T17_引当金判定表示用の登録（SQL11）
                dbacc.setOBInsertSateistat00(); 
                
                // T14_査定進捗管理の登録（SQL12）
                dbacc.setOBInsertSateistat01(); 
                
                // T01_対象先の更新（SQL13）
                dbacc.setOBUpdateT0100();
                
                // T08_滞留判定進捗管理の更新（SQL14）
                dbacc.setOBUpdateT0801();
                
                // T15_一次二次査定の登録（SQL15）
                dbacc.setOBInsertSatei(); 
                
                //処理⑩:T04_メール配信の登録を行う（SQL18）
                dbacc.setOBInsertT0400();  
            }
            
            break;
        case 2:
            return GS.OB1104;
		default:
			break;
        }
        
        dbacc.commit();
        
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
    
}

