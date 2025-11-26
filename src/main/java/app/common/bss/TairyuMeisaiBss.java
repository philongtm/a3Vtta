/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.common.dbAcc.TairyuMeisaiDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.util.Log;

/**
 * OZ6101_滞留債権明細照会タブ ビジネスロジッククラス
 */
public class TairyuMeisaiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション

	/**
	 * コンストラクタ
	 */
	public TairyuMeisaiBss(AppContext appContext) throws Exception {
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
		TairyuMeisaiDbAcc dbacc = new TairyuMeisaiDbAcc(sqlExec, log, appContext);
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
		
		// 一覧情報取得
		dbacc.getMeisai();
		
		//汎用項目(ラベル)取得
		dbacc.gethanyo();
		
		return cmnData.getReturn_gamenId();
	}

}