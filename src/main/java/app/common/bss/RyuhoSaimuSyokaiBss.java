/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.common.dbAcc.RyuhoSaimuSyokaiDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.util.Log;

/**
 * OZ6107_債務明細照会タブ ビジネスロジッククラス
 */
public class RyuhoSaimuSyokaiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	

	/**
	 * コンストラクタ
	 */
	public RyuhoSaimuSyokaiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
	}

	/**
	 * 【画面初期表示値取得】
	 */
	public String execute() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		RyuhoSaimuSyokaiDbAcc dbacc = new RyuhoSaimuSyokaiDbAcc(sqlExec, log, appContext);
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
		//債務総計・留保債務計の取得
		dbacc.getSaimukei();
		// 一覧情報取得
		dbacc.getMeisai();
		
		return cmnData.getTab_riyou_gamenId();
	}
}