/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.dbAcc.KureemuMeisaiDbAcc;
import app.system.form.KureemuMeisaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 * OS3102 クレーム債権再設定_明細一覧 ビジネスロジッククラス <br>
 */
public class KureemuMeisaiBss {
    
    private static final String INSERT_KBN_MT_KAIJYO    = "40";                 // 入力区分 '40': もぎ取り解除
    private static final String INSERT_KBN_REGIST       = "10";                 // 入力区分 '10': 登録
    
    private SessionData cmnData;            // 共通セッション
    private UserBean user_bean;             // ユーザー情報
    private TorihikisakiBean tori_bean;     // 取引先情報
    private KureemuMeisaiForm form;         // アクションフォーム

    private AppContext appContext       = null; // ＡＰＰコンテキスト
    private SqlExecuter sqlExec         = null; // ＤＢアクセス
    private KureemuMeisaiDbAcc dbacc	 = null; // SQL実行
    private Log log                     = null; // LOG

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception 
     */
    public KureemuMeisaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.tori_bean = cmnData.getTori_bean();
        this.form = (KureemuMeisaiForm) appContext.getActionForm();
        this.sqlExec = appContext.getSqlExecuter();
        this.dbacc = new KureemuMeisaiDbAcc(sqlExec, log, appContext);
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時) <br>
     * 
     * @return forward
     * @throws Exception 
     */
    public String executeInit() throws Exception {

        // 実施業務
        this.getOparation();
        
        // 承認担当者セレクトボックスを表示にする
        form.setFlg_disp_tanto(GS.ON);
        // 承認担当者セレクトボックス値取得
        dbacc.getTanto();

        // 表示件数セレクトボックス値取得
        dbacc.getShow();

        // 一覧情報取得
        dbacc.getMeisaiList();

        // 滞留判定データが作成済かチェック
        boolean isAnkenNo = dbacc.getAnkenNo();
        
        if (isAnkenNo) {
        	form.setToroku_anken_no(GS.EMPTY_CHARCTER);
        	for (int i = 0; i < form.getAr_meisai().size(); i++) {
                // T10_滞留判定の更新（件数分ループ）
        		MeisaisyosaiBean meisaiBean = (MeisaisyosaiBean)form.getAr_meisai().get(i);
        		dbacc.setInsertT1000(meisaiBean);
        		
        		// T16_引当金検討対象BS明細の更新
        		dbacc.setUpdateT1600(meisaiBean);
        	}
        	
        	dbacc.commit();
        }
        
        return GS.OS3102;
    }

    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
     * 
     * @return forward
     * @throws Exception 
     */
    public String execute() throws Exception {
        // 一覧情報取得
        dbacc.getMeisaiList();

        return GS.OS3102;
    }

    /**
     * 共)実施業務【リスト】より、実施フェーズが共)取引先情報.フェーズ、開始ステータスが'10'の実施業務を取得する。 <br>
     */
    private void getOparation() {
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
            if (phase != null && phase.equals(tori_bean.getPhase()) && GS.STATUS_MISYORI.equals(status)) {
                // 次実施フェーズ
                form.setJi_jishi_phase((String)mapOparation.get(GS.JI_JISHI_PHASE));
                // 次開始ステータス
                form.setJi_kaishi_status((String) mapOparation.get(GS.JI_KAISHI_STATUS));
            }
        }
    }

    /**
     * もぎ取り解除処理 <br>
     * 
     * @throws Exception 
     */
    public void doKaijyo() throws Exception {
        // T14_査定進捗管理の更新
        dbacc.setUpdateT1400("0");
        // T13_入力履歴の登録
        dbacc.setInsertT1300(INSERT_KBN_MT_KAIJYO);
        
        dbacc.commit();
    }

    /**
     * 登録処理 <br>
     * 
     * @return 登録成功フラグ
     * @throws Exception
     */
    public boolean doRegist() throws Exception {
    	
    	// チェッククラス
    	InputCheck check = new InputCheck();
    	if (check.isNullBlank(form.getSyonin_tanto())) {
			// 承認担当者が選択されていない場合
			appContext.setMsgCode(GL.ERR_SELECT, GL.OS3102_SHONINTANTOSHA);
			return false;
    	}
    	
        // 実施業務
        this.getOparation();
        
    	// T14_査定進捗管理の更新
    	dbacc.setUpdateT1400("1");
    	
        // T13_入力履歴の登録
        dbacc.setInsertT1300(INSERT_KBN_REGIST);
    	
        // T04_メール配信の登録
        dbacc.setInsertT0400();
    	    	
        dbacc.commit();
        
        // 登録成功の場合
        return true;
    }
}