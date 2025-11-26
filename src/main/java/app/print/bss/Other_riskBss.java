/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.print.bss;

import app.print.dbAcc.Other_riskDbAcc;
import app.system.form.DownloadForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * LE1101 他社リスクリストビジネスロジッククラス
 */
public class Other_riskBss{

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private DownloadForm form = null;							// アクションフォーム
	
	private String F_NAME = "他社リスクリスト.xls";			// ファイル名
	private String S_NAME = "他社リスクリスト";				// シート名
	private String TEMPLATE = "LE1101";						// テンプレート
	
	
	/**
	 * コンストラクタ
	 */
	public Other_riskBss(AppContext appContext) throws Exception {
		
		
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.form = (DownloadForm)appContext.getActionForm();
	
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 */
	public String execute() throws Exception {
		
		String title = GS.EMPTY_CHARCTER;
		List<String> ymList = new ArrayList<String>(2);
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		Other_riskDbAcc dbacc = new Other_riskDbAcc(sqlExec, log, appContext);
		
		//Excelの作成
		Excel excel = new Excel(appContext,TEMPLATE,9);
		excel.setDisplayFileName(F_NAME);
		excel.setSheetName(0,S_NAME);
			
		//セルスタイルの作成
		int i = 7;
		for(int j=0;j<=8;j++) {
			excel.copyCellStyle(i,j,j);
		}
		
		
        //査定期取得
		ymList.add((form.getSearch_sateiki()).substring(0,4));
		ymList.add((form.getSearch_sateiki()).substring(4,6));
		
       	//タイトル表示
		excel.selectCell(3,0,true);
		title = excel.getStringCellValue();
		title = Function.replaceExpression(title,ymList);
		excel.setCellValue(title);

		//作成日を取得
		dbacc.getYm(excel);
		// 一覧情報取得
		boolean result = dbacc.getMeisai(excel);
		
		//ダウンロード実行		
		if(result){
			excel.download();
		}
		
		return null;
	}
}