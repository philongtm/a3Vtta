/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.print.bss;

import app.print.dbAcc.SateiKekkaExcelDbAcc;
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
 * LD2101 引当金額集計表（期中） ビジネスロジッククラス
 */
public class SateiKekkaExcelBss {

	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG
	private DownloadForm form = null;			// アクションフォーム

	private static final String TEMPLATE   = "LD2101_1";						// 使用テンプレート
	private static final String OUTFILENAME_JA = "引当金集計表\\.xls";		// 出力ファイル名
	private static final String OUTFILENAME_EN = "Total.xls";					// 出力ファイル名
	private static final String SHEETNAME_JA = "引当金集計表 期中";			// シート名
	private static final String SHEETNAME_EN = "Reserve Amount Total Table";	// シート名
	private static final int StartRow = 4;	//明細出力開始行
	private static final int endClm = 62;	//明細出力最終列
	private static final int printendClm = 54;
	
	/**
	 * コンストラクタ
	 */
	public SateiKekkaExcelBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.form = (DownloadForm)appContext.getActionForm();
	}

	/**
	 * 【帳票ダウンロード処理】
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
		SateiKekkaExcelDbAcc dbacc = new SateiKekkaExcelDbAcc(sqlExec, log, appContext);

		//年月出力
		this.setYm(excel);
		
		//ヘッダ情報の取得 
		dbacc.getHeader(excel);

		//一覧情報取得
		boolean result = dbacc.getMeisai(excel);
		
		//印刷範囲設定
		excel.setPrintAria(0,0,printendClm,0,excel.getLastRowIndex());

		if(result){
			excel.download();
		}
	}

	/**
	 * 【テンプレート名編集】
	 * 
	 * @throws Exception
	 */
	private String getTemplateNm() throws Exception {
		StringBuffer template = new StringBuffer();
		template.append(TEMPLATE)
				.append(GS.UNDERLINE)
				.append(Function.removeSingle(form.getSearch_system_kbn()))
				.append(GS.UNDERLINE)
				.append(form.getLangMode());
		return template.toString();
	}

	/**
	 * 【年月出力】
	 * 
	 * @throws Exception
	 */
	private void setYm(Excel excel) throws Exception {
		//年月
		List<String> list_ym = new ArrayList<String>();
		excel.selectCell(1,0,true);
		String title_ym = excel.getStringCellValue();
		String taisyo_ym = form.getSearch_taisyo_ym();
		if(form.getLangMode().equals(GS.LANG_JA)){
			list_ym.add(taisyo_ym.substring(0,4));
			list_ym.add(taisyo_ym.substring(4));
		}else{
			list_ym.add(taisyo_ym.substring(4));
			list_ym.add(taisyo_ym.substring(0,4));
		}
		excel.setCellValue(Function.replaceExpression(title_ym,list_ym));
	}
}