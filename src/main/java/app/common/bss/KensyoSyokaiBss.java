/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
002		2009/5/19		SSC 			1.5次版機能組込 
******************************************************************************/
package app.common.bss;

import app.common.dbAcc.KensyoSyokaiDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * 引当金検証タブビジネスロジッククラス
 */
public class KensyoSyokaiBss {
	
	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	
	/**
	 * コンストラクタ
	 */
	public KensyoSyokaiBss(AppContext appContext) {
		this.appContext = appContext;		
		this.log = appContext.getLog();
	}
	
	/**
	 * 対象先検索を行う。
	 */
	public String execute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		KensyoSyokaiDbAcc dbacc = new KensyoSyokaiDbAcc(sqlExec, log, appContext);
		dbacc.setSyoriKaisuKbn();
		dbacc.execute();
		
		// コネクションの開放
		appContext.destroy();		
		
		return GS.OD1105;
	}
	/**
	 * 対象先検索を行う。
	 */
	public String reExecute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		KensyoSyokaiDbAcc dbacc = new KensyoSyokaiDbAcc(sqlExec, log, appContext);
		dbacc.execute_saisyu();
		
		// コネクションの開放
		appContext.destroy();		
		
		return GS.OD1105;
	}
}
