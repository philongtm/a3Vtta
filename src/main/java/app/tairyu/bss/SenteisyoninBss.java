/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/10		SSC				課題No.35 案件0件時の承認処理修正 
******************************************************************************/
package app.tairyu.bss;


import app.TorihikisakiBean;
import app.tairyu.dbAcc.SenteisyoninDbAcc;
import app.tairyu.form.SenteisyoninForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.List;

/**
 * OB2104_対象先選定_承認一覧 ビジネスロジッククラス
 */
public class SenteisyoninBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SenteisyoninForm form;								// アクションフォーム

	private static final String CHECKBOX_STATUS_ON  = "on";	// チェックボックスのステータス:オン
	private static final String CHECKBOX_STATUS_OFF = "off";	// チェックボックスのステータス:オフ
	private static final String TAISHOGAI = "1";				// 対象外
	private static final String GOLF_ON = "2";				// ゴルフ会員権

	/**
	 * コンストラクタ
	 */
	public SenteisyoninBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.form = (SenteisyoninForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時)
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteisyoninDbAcc dbacc = new SenteisyoninDbAcc(sqlExec, log, appContext);

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 一覧情報取得
		dbacc.getMeisai();

		// T14_査定進捗管理の更新
       	dbacc.upd_torimodoshi_flg();
        
       	// コミット
       	dbacc.commit();
       	
		return GS.OB2104;
	}
	
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時)
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteisyoninDbAcc dbacc = new SenteisyoninDbAcc(sqlExec, log, appContext);

		// 一覧情報取得
		dbacc.getMeisai();
	
		// T14_査定進捗管理の更新
       	dbacc.upd_torimodoshi_flg();
 
       	// コミット
       	dbacc.commit();
       	
		return GS.OB2104;
	}

    /**
     * 【一括承認チッェクボックス処理】 <br>
     */
    public void doIkatuSyonin() throws Exception {

        List list = form.getList();
        
        String syonin = CHECKBOX_STATUS_OFF;
        
        // 一括承認チッェクオン時、画面に表示されている明細の承認を全てチェックオンにする
        if (CHECKBOX_STATUS_ON.equals(form.getIkkatu_syonin())) {
            syonin = CHECKBOX_STATUS_ON;
        }
        
        // 課題No.35
        // 追加開始
        if(list != null){
            for (int i = 0; i < list.size(); i++) {
            	TorihikisakiBean tori_bean = (TorihikisakiBean)list.get(i); 
            	tori_bean.setSyonin_chk(syonin);
            }
        }
        // 追加完了
    }
	
    /**
     * 【承認実行処理】 <br>
     */
    public void doSyonin() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteisyoninDbAcc dbacc = new SenteisyoninDbAcc(sqlExec, log, appContext);


        // 承認チェックボックスチェック
		boolean syonin_flg = true;
		
        // 課題No.35
        // 追加開始
		if(form.getAr_meisai() == null){
			return;
		}
        // 追加完了

		for (int i = 0; i < form.getAr_meisai().size(); i++) {            
        	TorihikisakiBean meisaiBean = (TorihikisakiBean)form.getAr_meisai().get(i);
        	if ((CHECKBOX_STATUS_ON).equals(meisaiBean.getSyonin_chk())) {
                syonin_flg = false;
                break;
            }
        }
        
        // 承認チェックボックスが全てオフの場合、エラーダイアログを表示し、処理終了
        if (syonin_flg) {
            appContext.setMsgCode(GL.ERR_INPUT, GL.COMMON_APPROVE);
            return;
        }
        for (int i = 0; i < form.getAr_meisai().size(); i++) {            
        	TorihikisakiBean meisaiBean = (TorihikisakiBean)form.getAr_meisai().get(i);
        	//承認チェックがオンの案件のみ承認実行
        	if(!meisaiBean.getSyonin_chk().equals(CHECKBOX_STATUS_ON)) {
        		continue;
        	}
        	//T14_査定進捗管理の更新
       		dbacc.updT14(meisaiBean);

       		//T13_入力履歴の登録
       		dbacc.insT13(meisaiBean);

       		//対象外、ゴルフ会員権の場合、
       		if(TAISHOGAI.equals(meisaiBean.getTaisyogai_flg()) || GOLF_ON.equals(meisaiBean.getGolf_flg())){
       			//査定データ削除
       			//T15_一次二次査定の削除
       			dbacc.delT15(meisaiBean);
       			//T16_引当金検討対象BS明細の削除
       			dbacc.delT16(meisaiBean);
       			//T17_引当金判定表示用の削除
       			dbacc.delT17(meisaiBean);
       			//T19_留保債務の削除
       			dbacc.delT19(meisaiBean);
       			//T20_第三者留保債務の削除
       			dbacc.delT20(meisaiBean);
       			//T04_メール配信は登録しない
       			continue;
       		}	
   			//T04_メール配信の登録
       		dbacc.insT04(meisaiBean);

        }
		// 一覧情報再取得
		dbacc.getMeisai();
	
		// T14_査定進捗管理の更新
       	dbacc.upd_torimodoshi_flg();

        // コミット
       	dbacc.commit();

    }
}