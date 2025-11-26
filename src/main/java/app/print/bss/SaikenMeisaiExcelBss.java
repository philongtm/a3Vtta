/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.print.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.print.dbAcc.SaikenMeisaiExcelDbAcc;
import app.system.form.DownloadForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * LC2101 債権査定帳票 ビジネスロジッククラス
 */
public class SaikenMeisaiExcelBss {


	private AppContext appContext = null;			// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;			// ＤＢアクセス
	private Log log = null;						// LOG
	private AppPagerActionForm appform = null;		// アクションフォーム
	private TorihikisakiBean tori_bean = null;		// 取引先情報
	private SessionData cmnData = null;			// 機能共通セッション
	
	// 使用テンプレート
	private static final String TEMPLATE   = "LC";
	// 出力ファイル名
	private static final String OUTFILENAME_JA = "債権明細一覧.xls";
	private static final String OUTFILENAME_EN = "CreditDetails.xls";
	// シート名
	private static final String SHEETNAME_JA_1101 = "債権明細一覧";
	private static final String SHEETNAME_EN_1101 = "CreditDetails";
	// タイトル
	private static final String TITLE_JA = "債権明細一覧";
	private static final String TITLE_EN = "List for Details of Receivables";	
	// シート番号
	private static final int SAIKENMEISAI = 1;

	private static final int StartRow = 10;	//明細出力開始行(債権明細帳票)
	private static final int endClm = 23;	//明細出力最終列(債権明細帳票)
	private static final String WARNING0004 = "warning.0004";	
		
	/**
	 * コンストラクタ
	 */
	public SaikenMeisaiExcelBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.appform = (AppPagerActionForm)appContext.getActionForm();
		this.cmnData = appContext.getCMN();
		this.tori_bean =cmnData.getTori_bean();
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 */
	public void execute() throws Exception {
		//Excelの作成
		Excel excel = new Excel(appContext,this.getTemplateNm(),endClm + 1);
		
		//ファイル名設定
		this.setFileNm(excel);
		
		//債権明細帳票出力
		boolean result = this.saikenMeisai(excel);
		
		//出力データが存在する場合、ダウンロード実行
		if(result){
			excel.download();
		}
	}

	/**
	 * 【債権明細帳票出力】
	 */
	public boolean saikenMeisai(Excel excel) throws Exception {
		String title = null;
		boolean result = true;
		boolean isData = true;

		//シート選択
		excel.selectSheet(SAIKENMEISAI);

		//シート名設定
		if(GS.LANG_JA.equals(appform.getLangMode())){
			excel.setSheetName(SAIKENMEISAI,SHEETNAME_JA_1101);
		}else{
			excel.setSheetName(SAIKENMEISAI,SHEETNAME_EN_1101);
		}

		//スタイルの設定
		this.setStyle(excel,StartRow,endClm);

		//タイトル出力
		excel.selectCell(3,11,false);
		if(GS.OS6102.equals(appform.toString())){
			title = Function.replaceExpression(excel.getStringCellValue(),this.ymHantei(tori_bean.getTaisyo_ym()));
			excel.setCellValue(title);
		}else{
			if(GS.LANG_JA.equals(appform.getLangMode())){
				excel.setCellValue(TITLE_JA);
			}else{
				excel.setCellValue(TITLE_EN);
			}
		}

		//DBアクセスを行い帳票出力
		this.sqlExec = appContext.getSqlExecuter();
		SaikenMeisaiExcelDbAcc dbacc = new SaikenMeisaiExcelDbAcc(sqlExec, log, appContext);

		//ヘッダ情報の取得 
		if(GS.OS6102.equals(appform.toString())){
			dbacc.getHeader(excel);
			dbacc.getKanjoNm(excel);
			dbacc.getTotal_kingaku(excel);
		}else{
			DownloadForm form = (DownloadForm)appContext.getActionForm();
			dbacc.getTotal_kingaku2(excel,form);
		}

		//一覧情報取得
		if(GS.OS6102.equals(appform.toString())){
			isData = dbacc.getMeisai(excel);
			if(!isData){
				appContext.setMsgCode(WARNING0004);
				result = false;
			}
		}else{
			DownloadForm form = (DownloadForm)appContext.getActionForm();
			isData = dbacc.getMeisai2(excel,form);
			if(!isData){
				appContext.setMsgCode(WARNING0004);
				result = false;
			}
		}
		return result;
	}

	/**
	 * 【テンプレート名編集】
	 * 
	 * @throws Exception
	 */
	private String getTemplateNm() throws Exception {
		String system_kbn;
		if(GS.OS6102.equals(appform.toString())){
			system_kbn = tori_bean.getSystem_kbn();
		}else{
			DownloadForm form = (DownloadForm)appContext.getActionForm();
			system_kbn = Function.removeSingle(form.getSearch_system_kbn());
		}
		StringBuffer template = new StringBuffer();
		template.append(TEMPLATE)
				.append(GS.UNDERLINE)
				.append(system_kbn)
				.append(GS.UNDERLINE)
				.append(appform.getLangMode());
		return template.toString();
	}

	/**
	 * 【ファイル名設定】
	 * 
	 * @throws Exception
	 */
	private void setFileNm(Excel excel) throws Exception {
		if(GS.LANG_JA.equals(appform.getLangMode())){
			excel.setDisplayFileName(OUTFILENAME_JA);
		}else{
			excel.setDisplayFileName(OUTFILENAME_EN);
		}
	}

	/**
	 * 【スタイル設定】
	 * 
	 * @throws Exception
	 */
	private void setStyle(Excel excel,int start,int end) throws Exception {
		for(int i=0;i<=end;i++){
			excel.copyCellStyle(start,i,i);
		}
	}

	/**
	 * 出力用に対象年月編集 <br>
	 * 
	 * @throws Exception
	 */
	private List<String> ymHantei(String ym) throws Exception {
		List<String> ymList = new ArrayList<String>(2);
		String yyyy = ym.substring(0,4);
		String mm = ym.substring(4);
		if(GS.LANG_JA.equals(appform.getLangMode())){
			ymList.add(yyyy);
			ymList.add(mm);
		}else{
			ymList.add(mm);
			ymList.add(yyyy);
		}
		return ymList;
	}
}