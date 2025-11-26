/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.dbAcc.SenteiDbAcc;
import app.tairyu.form.SenteiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

/**
 * OB2101_対象先選定_選定実行 ビジネスロジッククラス
 */
public class SenteiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	private UserBean user_bean;								// 共通セッション
	private SenteiForm form;									// アクションフォーム

	/**
	 * コンストラクタ
	 */
	public SenteiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (SenteiForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時)
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteiDbAcc dbacc = new SenteiDbAcc(sqlExec, log, appContext);

		//自担当分/汎用２ラジオボタン初期判定
		dbacc.getInitTanto();

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 一覧情報取得
		dbacc.getMeisai();

		return GS.OB2101;
	}
	
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時)
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteiDbAcc dbacc = new SenteiDbAcc(sqlExec, log, appContext);

		// 一覧情報取得
		dbacc.getMeisai();
	
		return GS.OB2101;
	}

	/**
	 * もぎ取り処理
	 */
	public boolean doMogitori() throws Exception {

		// 処理結果フラグ
	    boolean result = false;
	    TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
		String hiji_user = listBean.getHoji_user_id();
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteiDbAcc dbacc = new SenteiDbAcc(sqlExec, log, appContext);

		//もぎ取りチェック
		if(dbacc.checkMogitori()){
			// T14_査定進捗管理の更新
			dbacc.updT14();	
			
			// T13_入力履歴の登録(もぎ取り済の場合は実行しない)
			if(!user_bean.getComUserId().equals(hiji_user)){
				dbacc.insT13();	
			}
			// COMMIT
			dbacc.commit();	
			
			result = true;
		}else{
    		appContext.setMsgCode(GL.ERR_TAKEN);
		}					

		return result;
	}
}