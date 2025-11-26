/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.dbAcc.KureemuSyoninSyosaiDbAcc;
import app.system.form.KureemuSyoninSyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 * OS3105_クレーム債権再設定_承認 ビジネス ロジッククラス <br>
 */
public class KureemuSyoninSyosaiBss {

	private AppContext appContext = null;					                   // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               // ＤＢアクセス
	private Log log = null;									               // LOG
	private SessionData cmnData;							                   // 共通セッション
	private UserBean user_bean;								               // ユーザービンー
	private KureemuSyoninSyosaiForm form;    
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public KureemuSyoninSyosaiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (KureemuSyoninSyosaiForm) appContext.getActionForm();
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
        KureemuSyoninSyosaiDbAcc dbacc = new KureemuSyoninSyosaiDbAcc(sqlExec, log, appContext);
        
        // 明細情報を取得
        TorihikisakiBean meisaiBean = appContext.getCMN().getTori_bean();        
        
        form.setMeisai_phase(meisaiBean.getPhase());
        // 次実施フェーズ、次開始ステータスを取得する
        this.getJisshiGyoumu();     
        
        // T14_査定進捗管理の更新
        dbacc.setOUpdateT1400();
        
        // T13_入力履歴の登録
        dbacc.setOInsertT1300();
        
        // T04_メール配信の登録
        dbacc.setOBInsertT0400();
        
        dbacc.commit();
        
        return GS.OS3104;
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
}
