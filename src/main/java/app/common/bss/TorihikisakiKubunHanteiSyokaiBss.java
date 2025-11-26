/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.common.dbAcc.TorihikisakiKubunHanteiSyokaiDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.util.Log;

/**
 * OZ6103_取引先区分判定照会タブ ビジネスロジッククラス
 */
public class TorihikisakiKubunHanteiSyokaiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション

	/**
	 * コンストラクタ
	 */
	public TorihikisakiKubunHanteiSyokaiBss(AppContext appContext) throws Exception {
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
		TorihikisakiKubunHanteiSyokaiDbAcc dbacc = new TorihikisakiKubunHanteiSyokaiDbAcc(sqlExec, log, appContext);
		
		// 取引先情報の基本情報を取得
		dbacc.getexecute();
		// コメント類を取得
		dbacc.getcomment();

		return cmnData.getTab_riyou_gamenId();
	}

}