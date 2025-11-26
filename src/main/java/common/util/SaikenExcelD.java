/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import app.SessionDataZen;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;

/**
 * 滞留Excel
 */
public class SaikenExcelD {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;				// 共通セッション
	
	
	/**
	 * コンストラクタ
	 */
	public SaikenExcelD(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZenRe();
	}
	
	/**
	 * 出力項目検索処理
	 * 
	 * @return GS.RC_OK
	 * @throws Exception
	 */
	public String execute() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから出力する値を取得し、セッションに格納
		SaikenExcelDDbAcc dbacc = new SaikenExcelDDbAcc(sqlExec, log, appContext);
		dbacc.execute();
		
		// コネクションの開放
		appContext.destroy();		
        
		return GS.RC_OK;
	}


}