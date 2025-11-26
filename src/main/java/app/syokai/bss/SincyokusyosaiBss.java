/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/03		SSC				課題No.20 取戻処理を案件単位に変更
003		2009/11/20		SSC				課題No.152 代行取戻対応
004		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
005		2016/03/23		SSC				BJ201602002_部門廃止対応（一次）
******************************************************************************/

package app.syokai.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.dbAcc.SincyokusyosaiDbAcc;
import app.syokai.form.SincyokusyosaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *  OS6104_進捗状況詳細 <br>
 */
public class SincyokusyosaiBss {

    private AppContext appContext = null;                         // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                           // ＤＢアクセス
    private SessionData cmnData = null;							// 機能共通セッション
    private TorihikisakiBean tori_bean = null;						// 取引先情報
    private SincyokusyosaiForm form = null;                    	// アクションフォーム
    private Log log = null;                                       // LOG
    private UserBean user_bean = null;							//ユーザ情報

    private static final String UPD_USER_ID_FLG_1 = "1";			// 更新ユーザID判定が'1'
    private static final String PHASE_FLG_1 = "1";				// フェーズ判定が'1'
    private static final String PHASE_FLG_2 = "2";				// フェーズ判定が'2'
	private static final String BUNRUI2 = "bunrui2";				// 分類2
	private static final String BU_CD = "bu_cd";					// 部コード

    /**
     * コンストラクタ
     */
    public SincyokusyosaiBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        cmnData = appContext.getCMN();
        form = (SincyokusyosaiForm)appContext.getActionForm();
        tori_bean = cmnData.getTori_bean();
        this.log = appContext.getLog();
        user_bean = cmnData.getUser_bean();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {

        // コネクションの取得
    	this.sqlExec = appContext.getSqlExecuter();
        SincyokusyosaiDbAcc dbacc = new SincyokusyosaiDbAcc(sqlExec, log, appContext);
        // 課題No.20
        // 修正開始
        // 取引先の最新査定進捗状況を取得
        //dbacc.getProgress();
        this.setProgress();
        // 修正完了
        // 課題No.152
        // 追加開始
        form.setTorimodoshiMoto(dbacc.getTorimodoshiMot());
        // 追加完了

        // 勘定先CDを設定する
        form.setKanjo_cd(tori_bean.getKanjo_cd());
        // 勘定先名称を設定する
        form.setKanjo_nm(tori_bean.getKanjo_nm());
        // 現在進捗を設定する
        //form.setSintyoku(tori_bean.getSintyoku());

		// 送信ボタンの表示非表示
		if("1".equals(user_bean.getComSystemManager_flg()) && GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			if(GS.STATUS_MISYORI.equals(tori_bean.getStatus())){
				// 案件が未処理の場合(督促メール送信選択画面遷移)
				form.setSendBtnFlg("1");
			} else if (!GS.STATUS_KANRYO.equals(tori_bean.getStatus())) {
				// 未処理・完了以外の場合（督促メールの送信）
				form.setSendBtnFlg("2");
			}
		}
		// 最新の配信年月日
		dbacc.getHaishinDt();

		// 最新の保存年月日
		dbacc.getHozonDt();

        return GS.OS6104;
    }

    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {


        // 課題No.20
        // 削除開始
        // コネクションの取得
    	// this.sqlExec = appContext.getSqlExecuter();
        // SincyokusyosaiDbAcc dbacc = new SincyokusyosaiDbAcc(sqlExec, log, appContext);
        // 取引先の最新査定進捗状況を取得
        // dbacc.getProgress();
        // 削除完了

        // 勘定先CDを設定する
        form.setKanjo_cd(tori_bean.getKanjo_cd());
        // 勘定先名称を設定する
        form.setKanjo_nm(tori_bean.getKanjo_nm());
        // 現在進捗を設定する
        //form.setSintyoku(tori_bean.getSintyoku());

		// 送信ボタンの表示非表示
		if("1".equals(user_bean.getComSystemManager_flg()) && GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			if(GS.STATUS_MISYORI.equals(tori_bean.getStatus())){
				// 案件が未処理の場合(督促メール送信選択画面遷移)
				form.setSendBtnFlg("1");
			} else if (!GS.STATUS_KANRYO.equals(tori_bean.getStatus())) {
				// 未処理・完了以外の場合（督促メールの送信）
				form.setSendBtnFlg("2");
			}
		}

        return GS.OS6104;
    }

    /**
     *
     * 取戻処理 <br>
     *
     * @return
     * @throws Exception
     */
    public boolean doTori() throws Exception {
        // 課題No.152
        // 追加開始
    	Map<String,String> map = form.getTorimodoshiMoto();
        // 追加完了

    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SincyokusyosaiDbAcc dbacc = new SincyokusyosaiDbAcc(sqlExec, log, appContext);
        // 取戻チェック
        dbacc.getCheck();
        // 課題No.152
        // 修正開始
        // 取戻不可フラグが｢0：取戻可｣、機)更新ユーザID判定が'1'の場合
        //if(!GS.TORIMODOSHI_KA.equals(form.getTorimodoshi_fuka_flg()) ||
        					//!UPD_USER_ID_FLG_1.equals(form.getUpd_user_id_flg())){
        if(!GS.TORIMODOSHI_KA.equals(form.getTorimodoshi_fuka_flg())){
        // 修正完了
        	appContext.setMsgCode(GL.ERR_TORIMODOSHI);
        	return false;
        }

        String bunrui2 = tori_bean.getBunrui2();
        String bu_cd = tori_bean.getBu_cd();
        //ユーザの参照組織チェック
        if("2".equals(tori_bean.getSasi_ten_flg())){
            // 課題No.152
            // 削除開始
        	//Map<String,String> map = dbacc.getTensoumotoSoshiki();
            // 削除開始
        	bunrui2 = map.get(BUNRUI2);
        	bu_cd = map.get(BU_CD);
        	if(!dbacc.chkSansyososhiki(bunrui2,bu_cd)){
            	appContext.setMsgCode(GL.ERR_NOTSANSYO);
            	return false;
    		}
    	}

        // T08_滞留判定進捗管理の更新
        if(PHASE_FLG_1.equals(form.getPhase_hantei())){
        	if(!"2".equals(tori_bean.getSasi_ten_flg())){
        		// 共)取引先情報.差戻・転送FLGが'2'ではない場合
        		dbacc.updTairyuStatDaiko();
        	}else{
        		// 共)取引先情報.差戻・転送FLGが'2'の場合
        		dbacc.updTairyuStat(bunrui2,bu_cd);
        	}

        // T14_査定進捗管理の更新
        }else if(PHASE_FLG_2.equals(form.getPhase_hantei())){
        	if(!"2".equals(tori_bean.getSasi_ten_flg())){
        		// 共)取引先情報.差戻・転送FLGが'2'ではない場合
        		dbacc.updSateiStatDaiko();
        	}else{
        		// 共)取引先情報.差戻・転送FLGが'2'の場合
        		dbacc.updSateiStat(bunrui2,bu_cd);
        	}
        }

        // T04_メール配信の更新
        dbacc.updMailHaishin();
        // T13_入力履歴の登録
        dbacc.addNyuryokuHist();
        // コミット処理
        dbacc.commit();
        //タブの再表示
    	form.setKarento_tab(form.getPhase_hantei());

        return true;
    }

    // 課題No.20
    // 追加開始
	/**
	 * 初期設定処理 <br>
	 *
	 * @exception SQLException
	 */
	public void setProgress() {
		// 取戻不可フラグ
		form.setTorimodoshi_fuka_flg(tori_bean.getTorimodoshi_fuka_flg());
	    //課題No.152
	    //削除開始
		//更新ユーザID判定
		//form.setUpd_user_id_flg(tori_bean.getKousin_user_handan());
	    //削除完了
		//フェーズ判定
		form.setPhase_hantei(tori_bean.getPhase_handan());
		//カレントタブ
		form.setKarento_tab(tori_bean.getPhase_handan());
		if("2".equals(tori_bean.getSasi_ten_flg())){
			// メール用画面ID
			form.setMail_gamen_id("OZ3101");
		}else{
			// メール用画面ID
			form.setMail_gamen_id(form.toString());
		}
		form.setSintyoku(tori_bean.getSintyoku());
	}
    // 追加完了


	/**
	 * T27_督促メール配信の登録・送信
	 *
	 * @throws Exception
	 */
	public String doInsT04() throws Exception {

		String tanto = tori_bean.getHoji_user_id();
		String upd_user = user_bean.getComUserId();

		// 権限チェック（担当したフェーズにより処理・参照権限チェックを実施）
		String chkPhase = tori_bean.getPhase();

		if(GS.PHASE_TAIRYU_HANTEI.equals(chkPhase) ||				//滞留判定
				GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(chkPhase) ||	//滞留判定検証
				GS.PHASE_ICHIJI_SATEI.equals(chkPhase) ||			//一次査定
				GS.PHASE_ICHIJI_SATEI_KENSYO.equals(chkPhase) ||	//一次査定検証
				GS.PHASE_NIJI_SATEI.equals(chkPhase) ||				//二次査定
				GS.PHASE_HIKIATEKIN_KENSYO.equals(chkPhase)){		//引当金確認
			if(!chkUserKengen(tanto)){
				// 担当者に処理・参照権限がない場合、エラー
				return GS.OS6104;
			}
		}

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SincyokusyosaiDbAcc dbacc = new SincyokusyosaiDbAcc(sqlExec, log, appContext);

		// メール配信登録
		dbacc.insT27(tanto, upd_user);

		// メール送信フラグが1の場合、メール配信処理の実行
		if ("1".equals(form.getMailSendFlg())) {
			dbacc.sendTokusokuMail(tanto);
		}
		//コミット
		dbacc.commit();

		return GS.OS6103;
	}

	/**
	 * ユーザ権限チェック
	 *
	 * @param tanto 担当ユーザID
	 * @return チェック結果
	 * @throws Exception
	 */

	public boolean chkUserKengen(String tanto) throws Exception {
    	String errMsg = GL.ERR_NOTSET;
    	boolean result = true;

    	// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SincyokusyosaiDbAcc dbacc = new SincyokusyosaiDbAcc(sqlExec, log, appContext);

		// ユーザの処理権限チェック
		if(!dbacc.isUserKengen(tanto)){
			errMsg = GL.ERR_SELECT3;
			result = false;

		}
		// メッセージの作成
		if(!result){
			List<String> msgList = new ArrayList<String>();
			msgList.add(errMsg);
			appContext.setMsgCode(msgList);
		}

		return result;
	}

}