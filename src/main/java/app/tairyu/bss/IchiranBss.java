/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.UserBean;
import app.tairyu.dbAcc.IchiranDbAcc;
import app.tairyu.form.IchiranForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

/**
 * OB1101_実質滞留債権判定_対象先一覧 ビジネスロジッククラス <br>
 */
public class IchiranBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	private UserBean user_bean;								// 共通セッション
	private IchiranForm form;									// アクションフォーム
	private static final String PHASE_TAIRYU_HANTEI = "'10'";	// フェーズ：滞留判定
	private static final String PHASE_TAIRYU_KENSYO = "'20'";	// フェーズ：滞留判定検証

	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext AppContext
	 * @throws Exception 
	 */
	public IchiranBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (IchiranForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 遷移先
	 * @throws Exception 
	 */
	public String executeInit() throws Exception {
		
		//ログインユーザの参照フェーズ設定
		StringBuffer sansyo_phase = new StringBuffer();
		if (user_bean.getComTairyu_hantei_t_flg().equals(GS.ON) && user_bean.getComTairyu_kensho_t_flg().equals(GS.ON)){
			//滞留判定・滞留判定検証両方
			sansyo_phase.append(PHASE_TAIRYU_HANTEI);
			sansyo_phase.append(GS.COMMA);
			sansyo_phase.append(PHASE_TAIRYU_KENSYO);
		}else if(user_bean.getComTairyu_hantei_t_flg().equals(GS.ON)){
			//滞留判定のみ
			sansyo_phase.append(PHASE_TAIRYU_HANTEI);
		}else if(user_bean.getComTairyu_kensho_t_flg().equals(GS.ON)){
			//滞留判定検証のみ
			sansyo_phase.append(PHASE_TAIRYU_KENSYO);	
		}
		form.setSansyo_phase(sansyo_phase.toString());

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);

		// 査定期取得
		dbacc.getSateiki();

		//自担当分/汎用２ラジオボタン初期判定
		dbacc.getInitTanto();

		// 各進捗件数取得
		dbacc.getSintyoku();

		// ソート順セレクトボックス値取得
		dbacc.getSort();

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 一覧情報取得
		dbacc.getMeisai();
        
		return GS.OB1101;
	}
	
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return 遷移先
	 * @throws Exception 
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);

		// 各進捗件数取得
		dbacc.getSintyoku();
		// 一覧情報取得
		dbacc.getMeisai();
        
		return GS.OB1101;
	}

	/**
	 * もぎ取り処理 <br>
	 * 
	 * @return 処理結果フラグ
	 * @throws Exception 
	 */
	public boolean doMogitori() throws Exception {

		// 処理結果フラグ
	    boolean result = false;
	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);

		//もぎ取りチェック
		if(dbacc.checkMogitori()){
			// もぎ取り処理
			dbacc.doMogitoriUpdate();
			dbacc.doMogitoriInsert();
			dbacc.commit();
			result = true;
		}else{
    		appContext.setMsgCode(GL.ERR_TAKEN);
		}					

		return result;
	}
}