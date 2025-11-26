/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2023/03/02		NELCO			新規作成
******************************************************************************/
package app.print.bss;

import app.print.dbAcc.KenshokekkaHonsyaExcelDbAcc;
import app.system.form.DownloadForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Excel;
import common.util.Log;

/**
 * 引当金額集計表(検証結果)(本社用) ビジネスロジッククラス
 */
public class KenshokekkaHonsyaExcelBss {

	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;	// ＤＢアクセス
	private Log log = null;				// LOG
	private DownloadForm form = null;		// アクションフォーム

	private static final String TEMPLATE   = "LD3103";						// 使用テンプレート(日本語)
	private static final String OUTFILENAME_JA = "引当金集計表_検証結果.xls";		// 出力ファイル名
	private static final String OUTFILENAME_EN = "Total.xls";					// 出力ファイル名
	private static final String SHEETNAME_JA = "引当金額集計表_検証結果";			// シート名
	private static final String SHEETNAME_EN = "Total1";						// シート名
	private static final int StartRow = 5;		//明細出力開始行
	private static final int endClm = 84;		//明細出力最終列
	private static final int printendClm = 85;	//印刷範囲最終列
	/**
	 * コンストラクタ
	 */
	public KenshokekkaHonsyaExcelBss(AppContext appContext) throws Exception {
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

		//ファイル名・シート名設定
		if(GS.LANG_JA.equals(form.getLangMode())){
			excel.setDisplayFileName(OUTFILENAME_JA);
			excel.setSheetName(0,SHEETNAME_JA);
		}else{
			excel.setDisplayFileName(OUTFILENAME_EN);
			excel.setSheetName(0,SHEETNAME_EN);
		}

		//スタイルの設定
		for(int i=0;i<=endClm;i++){
			excel.copyCellStyle(StartRow,i,i);
		}
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KenshokekkaHonsyaExcelDbAcc dbacc = new KenshokekkaHonsyaExcelDbAcc(sqlExec, log, appContext);
		//ヘッダ情報の取得
		dbacc.getHeader(excel);
		//一覧情報取得
		boolean result = dbacc.getMeisai(excel);
		//印刷範囲設定
		excel.setPrintAria(0,0,printendClm,0,excel.getLastRowIndex());

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
 		return TEMPLATE;
	}
}