/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.common.dbAcc.TorihikisakiGaiyoSyokaiDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.util.Log;


/**
 * OZ6102_取引先概要照会タブビジネスロジッククラス
 */
public class TorihikisakiGaiyoSyokaiBss {
	
	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション

	/**
	 * コンストラクタ
	 */
	public TorihikisakiGaiyoSyokaiBss(AppContext appContext) throws Exception {
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
		TorihikisakiGaiyoSyokaiDbAcc dbacc = new TorihikisakiGaiyoSyokaiDbAcc(sqlExec, log, appContext);
		
		//所在地、業種を取得
		dbacc.getshozaichi();
		//コメント取得
		dbacc.getcomment();
		//事業内容・株主構成取得
		dbacc.getKabunusi_kousei();
		//汎用項目ラベル【リスト】取得
		dbacc.getHanyo();
		// 財務情報【リスト】取得
		dbacc.getZaimu();

		return cmnData.getTab_riyou_gamenId();
	}

}
