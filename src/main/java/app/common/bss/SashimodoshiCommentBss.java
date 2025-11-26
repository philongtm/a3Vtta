/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package app.common.bss;

import app.common.dbAcc.SashimodoshiCommentDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;


/**
 * 差戻コメント画面ビジネスロジッククラス
 */
public class SashimodoshiCommentBss {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	
	
	/**
	 * コンストラクタ
	 */
	public SashimodoshiCommentBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
	}

	
	/**
	 * 検索
	 */
	public String execute() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		SashimodoshiCommentDbAcc dbacc = new SashimodoshiCommentDbAcc(sqlExec, log, appContext);
		dbacc.execute();
		
		// コネクションの開放
		appContext.destroy();		
        
		return GS.OZ4101;
	}
}