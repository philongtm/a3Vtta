/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/04/06		SSC				部門廃止対応(二次) 
										引当金額集計表(検証結果)に「本部」列追加の為、
										明細出力最終列と印刷範囲最終列を変更
******************************************************************************/
package app.print.bss;

import app.print.dbAcc.KensyokekkaExcelDbAcc;
import app.system.form.DownloadForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Excel;
import common.util.Log;

/**
 * LD2102 引当金額集計表（検証結果） ビジネスロジッククラス
 */
public class KensyokekkaExcelBss {

	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;	// ＤＢアクセス
	private Log log = null;				// LOG
	private DownloadForm form = null;		// アクションフォーム
	
	private static final String TEMPLATE   = "Template5";						// 使用テンプレート(日本語)
	private static final String TEMPLATE_E   = "Template5_e";					// 使用テンプレート(英語)
	private static final String OUTFILENAME_JA = "引当金集計表\\.xls";		// 出力ファイル名
	private static final String OUTFILENAME_EN = "Total.xls";					// 出力ファイル名
	private static final String SHEETNAME_JA = "引当金額集計表_期末";			// シート名
	private static final String SHEETNAME_EN = "Total1";						// シート名
	private static final String SHEET0 = "sheet0";							// シート名(不要なシート)
	private static final String SHEET2 = "sheet2";							// シート名(不要なシート)
	private static final String SHEET3 = "sheet3";							// シート名(不要なシート)
	private static final int KENSYOKEKKA = 1;								// シート選択
	private static final int StartRow = 5;		//明細出力開始行
	private static final int endClm = 85;		//明細出力最終列
	private static final int printendClm = 85;	//印刷範囲最終列

	/**
	 * コンストラクタ
	 */
	public KensyokekkaExcelBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.form = (DownloadForm)appContext.getActionForm();
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 * 
	 * @throws Exception
	 */
	public void execute() throws Exception {
		//Excelの作成
		Excel excel = new Excel(appContext,this.getTemplateNm(),endClm+1);
		
		//シート選択
		excel.selectSheet(KENSYOKEKKA);
		
		//ファイル名・シート名設定
		if(GS.LANG_JA.equals(form.getLangMode())){
			excel.setDisplayFileName(OUTFILENAME_JA);
			excel.setSheetName(1,SHEETNAME_JA);
		}else{
			excel.setDisplayFileName(OUTFILENAME_EN);
			excel.setSheetName(1,SHEETNAME_EN);
		}
		
		//不要なシートはマクロで削除する為、シート名を設定
		excel.setSheetName(0,SHEET0);
		excel.setSheetName(2,SHEET2);
		excel.setSheetName(3,SHEET3);
		
		//スタイルの設定
		for(int i=0;i<=endClm;i++){
			excel.copyCellStyle(StartRow,i,i);
		}
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KensyokekkaExcelDbAcc dbacc = new KensyokekkaExcelDbAcc(sqlExec, log, appContext);
		//ヘッダ情報の取得 
		dbacc.getHeader(excel);
		//一覧情報取得
		boolean result = dbacc.getMeisai(excel);
		//印刷範囲設定
		excel.setPrintAria(1,0,printendClm,0,excel.getLastRowIndex());
	
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
	private String getTemplateNm() throws Exception {
		if(GS.LANG_JA.equals(form.getLangMode())){
			return TEMPLATE;
		}else{
			return TEMPLATE_E;
		}
	}
}