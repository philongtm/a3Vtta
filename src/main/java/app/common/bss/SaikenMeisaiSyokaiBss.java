/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.common.dbAcc.SaikenMeisaiSyokaidbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.util.Log;

/**
 * OZ6105_債権明細照会タブ ビジネスロジッククラス
 */
public class SaikenMeisaiSyokaiBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	
	/**
	 * コンストラクタ
	 */
	public SaikenMeisaiSyokaiBss(AppContext appContext) throws Exception {
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
		SaikenMeisaiSyokaidbAcc dbacc = new SaikenMeisaiSyokaidbAcc(sqlExec, log, appContext);
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
	
		// 債権残高合計・保証債務合計・引当金合計・滞留債権計の合計を取得 
		if(dbacc.getSenteikei()){
			
			dbacc.getSaikenkei();
			
		}
		//明細の一覧【リスト】を取得

		if(dbacc.getMeisai()){
			
			dbacc.getMeisai2();
		}
		
		//汎用項目(ラベル)を取得
		dbacc.gethanyo();
		
		return cmnData.getReturn_gamenId();
	}

}