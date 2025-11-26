package app.print.bss;
/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/12/18		SSC				新規作成 
******************************************************************************/

import app.print.dbAcc.TairyuSaikenExcelDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Log;

/**
 * LB1102 債権明細一覧
 */
public class TairyuSaikenExcelBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private AppPagerActionForm form;	

	private static final String TITTLE   = "債権明細一覧";		// タイトル
	private static final String TITTLE_EN   = "Credit Listing";	// タイトルTITTLE
	private static final String TEMPLATE   = "Template1";			// 使用テンプレート(日本語)
	private static final String TEMPLATE_E   = "Template1_e";		// 使用テンプレート(英語)
	private final String OUT_FILE_NAME = "債権明細一覧.xls";
	private final String OUT_FILE_NAME_E = "CreditDetails.xls";
	private final String SHEET_NAME = "債権明細一覧";				// 作成ファイルシート名
	private final String SHEET_NAME_E = "CreditDetails";			// 作成ファイルシート名
	private static final int SAIKEN_MEISAI = 1;					//シート番号
	private static final String SHEET0 = "sheet0";				// シート名(不要なシート名)
	private static final int StartRow = 10;						//明細出力開始行
	private static final int endClm = 18;						//明細出力最終列
	private static final int printendClm = 18;					//印刷範囲最終列

	/**
	 * コンストラクタ
	 */
	public TairyuSaikenExcelBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.form = (AppPagerActionForm)appContext.getActionForm();
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		
		//Excelの作成
		Excel excel = new Excel(appContext,this.getTemplate(TEMPLATE,TEMPLATE_E),endClm+1);
		
		//シート選択
		excel.selectSheet(SAIKEN_MEISAI);
		
		//ファイル名・シート名設定
		excel.setDisplayFileName(getTemplate(OUT_FILE_NAME,OUT_FILE_NAME_E));
		excel.setSheetName(1,getTemplate(SHEET_NAME,SHEET_NAME_E));

		//不要なシートはマクロで削除する為、シート名を設定
		excel.setSheetName(0,SHEET0);
		
		//スタイルの設定
		for(int i=0;i<=endClm;i++){
			excel.copyCellStyle(StartRow,i,i);
		}
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TairyuSaikenExcelDbAcc dbacc = new TairyuSaikenExcelDbAcc(sqlExec, log, appContext);
		//ヘッダ情報の取得 
		dbacc.getHeader(excel);
		
		// タイトルの設定
		excel.selectCell(3,0,true);
		excel.setCellValue(getTemplate(TITTLE,TITTLE_EN));
		
		//一覧情報取得
		boolean result = dbacc.getMeisai(excel);
		
		//印刷範囲設定
		excel.setPrintAria(SAIKEN_MEISAI,0,printendClm,0,excel.getLastRowIndex());

		//ダウンロードの実行
		if(result){
			excel.download();
		}
	}

	/**
	 * 【テンプレートを返す】
	 * 
	 * @throws Exception
	 */
	private String getTemplate(String TEMPLATE, String TEMPLATE_E) throws Exception {
		String Template = GS.EMPTY_CHARCTER;
		if(GS.LANG_JA.equals(form.getLangMode())){
			Template = TEMPLATE;
		}else{
			Template = TEMPLATE_E;
		}
		return Template;
	}
}
