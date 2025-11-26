/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.commonZen.dbAcc.SaikenMeisaiSyokaiDbAcc;
import app.commonZen.form.SaikenMeisaiSyokaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;

import java.util.ArrayList;

/**
 * 滞留Excel
 */
public class SaikenExcel {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;				// 共通セッション
	
	
	/**
	 * コンストラクタ
	 */
	public SaikenExcel(AppContext appContext) throws Exception {
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
		
		// No453, 2008/05/26, SJA渡辺, 共通クラスを利用して債権明細リスト取得するように修正。
		appContext.setActionForm(new SaikenMeisaiSyokaiForm());
		SaikenMeisaiSyokaiDbAcc dbacc1 = new SaikenMeisaiSyokaiDbAcc(sqlExec, log, appContext);
		// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
		//ArrayList list = dbacc1.getSaikenMeisaiComList(cmnData.getKanjo_cd(),cmnData.getYm(),cmnData.getComSateiKaishaCd(),cmnData);
		ArrayList list = dbacc1.getSaikenMeisaiComList(cmnData.getKanjo_cd(),cmnData.getYm(),cmnData.getAnken_satei_kaisya_cd(),cmnData);
		
		// DBから出力する値を取得し、セッションに格納
		SaikenExcelDbAcc dbacc2 = new SaikenExcelDbAcc(sqlExec, log, appContext);
		dbacc2.execute(list);
		
		// コネクションの開放
		appContext.destroy();		
        
		return GS.RC_OK;
	}


}