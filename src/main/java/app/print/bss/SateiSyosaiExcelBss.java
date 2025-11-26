/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/19		SSC				課題No.132 値がnullの場合のブランクで出力されるよう修正
003		2014/03/17		SSC				案件No.D13493 改善対応
004		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
******************************************************************************/
package app.print.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.print.dbAcc.RyuuhoExcelDbAcc;
import app.print.dbAcc.SaikenMeisaiExcelDbAcc;
import app.print.dbAcc.SateiExcelDbAcc;
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
 * LC1101 債権明細一覧 ビジネスロジッククラス
 */
public class SateiSyosaiExcelBss {

	private AppContext appContext = null;			// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;			// ＤＢアクセス
	private Log log = null;						// LOG
	private AppPagerActionForm appform = null;		// アクションフォーム
	private TorihikisakiBean tori_bean = null;		// 取引先情報
	private SessionData cmnData = null;			// 機能共通セッション
	
	// 使用テンプレート
	private static final String TEMPLATE   = "LC";
	// 出力ファイル名
	private static final String OUTFILENAME_JA = "査定詳細";
	private static final String OUTFILENAME_EN = "Details";
	private static final String EXTENTION = ".xls";
	// シート名
	private static final String SHEETNAME_JA_1101 = "債権明細一覧";
	private static final String SHEETNAME_EN_1101 = "CreditDetails";
	private static final String SHEETNAME_JA_2101 = "判定シート";
	private static final String SHEETNAME_EN_2101 = "Judgment";
	private static final String SHEETNAME_JA_3101 = "債務明細一覧";
	private static final String SHEETNAME_EN_3101 = "List for Details of Payables";
	// シート番号
	private static final int SAIKENSATEI = 0;
	private static final int SAIKENMEISAI = 1;
	private static final int SAIMUMEISAI = 2;

	private static final int StartRow_1101 = 10;	//明細出力開始行(債権明細帳票)
	private static final int endClm_1101 = 23;	//明細出力最終列(債権明細帳票)
	private static final int StartRow_3101 = 7;	//明細出力開始行(債務明細帳票)
	private static final int endClm_3101 = 14;	//明細出力最終列(債務明細帳票)
		
	/**
	 * コンストラクタ
	 */
	public SateiSyosaiExcelBss(AppContext appContext) throws Exception {
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
		Excel excel = new Excel(appContext,this.getTemplateNm(),endClm_1101 + 1);
		
		//ファイル名設定
		this.setFileNm(excel);
		
		//債権明細帳票出力
		this.saikenMeisai(excel);
		//債権査定帳票出力
		this.saikenSatei(excel);
		//債務明細帳票出力
		this.saimuMeisai(excel);

		//ダウンロード実行
		excel.download();
	}

	/**
	 * 【債権明細帳票出力】
	 */
	public void saikenMeisai(Excel excel) throws Exception {
		String title = null;

		//シート選択
		excel.selectSheet(SAIKENMEISAI);

		//シート名設定
		if(GS.LANG_JA.equals(appform.getLangMode())){
			excel.setSheetName(SAIKENMEISAI,SHEETNAME_JA_1101);
		}else{
			excel.setSheetName(SAIKENMEISAI,SHEETNAME_EN_1101);
		}

		//スタイルの設定
		this.setStyle(excel,StartRow_1101,endClm_1101);

		//タイトル出力
		excel.selectCell(3,11,false);
		title = Function.replaceExpression(excel.getStringCellValue(),this.ymHantei(tori_bean.getTaisyo_ym()));
		excel.setCellValue(title);

		//DBアクセスを行い帳票出力
		this.sqlExec = appContext.getSqlExecuter();
		SaikenMeisaiExcelDbAcc dbacc = new SaikenMeisaiExcelDbAcc(sqlExec, log, appContext);

		//ヘッダ情報の取得 
		dbacc.getHeader(excel);
		dbacc.getKanjoNm(excel);
		dbacc.getTotal_kingaku(excel);

		//一覧情報取得
		dbacc.getMeisai(excel);
	}

	/**
	 * 【債権査定帳票出力】
	 */
	public void saikenSatei(Excel excel) throws Exception {
		//シート選択
		excel.selectSheet(SAIKENSATEI);
		//シート名設定
		if(GS.LANG_JA.equals(appform.getLangMode())){
			excel.setSheetName(SAIKENSATEI,SHEETNAME_JA_2101);
		}else{
			excel.setSheetName(SAIKENSATEI,SHEETNAME_EN_2101);
		}
		//タイトル出力
		String title = null;
		excel.selectCell(2,1,false);
		title = Function.replaceExpression(excel.getStringCellValue(),this.ymHantei(tori_bean.getTaisyo_ym()));
		excel.setCellValue(title);
		//取引先概要
		excel.selectCell(6,4,false);
		excel.setCellValue(tori_bean.getKanjo_cd());
		excel.selectCell(6,11,false);
		excel.setCellValue(tori_bean.getTogo_tori_cd());
		excel.selectCell(9,4,false);		
		excel.setCellValue(getSyozaikoku(tori_bean.getSyozaikoku_cd(),tori_bean.getSyozaikoku()));
		//財務概要
		excel.selectCell(52,6,false);
		excel.setCellValue(tori_bean.getKtk_kikan());
		excel.selectCell(53,6,false);
		excel.setCellValue(tori_bean.getGaibu_ktk());
		excel.selectCell(53,9,false);
		excel.setCellValue(tori_bean.getFss());
		excel.selectCell(53,12,false);
		excel.setCellValue(tori_bean.getDuns_rating());
		//取引先区分判定
		excel.selectCell(7,16,false);
		excel.setCellValue(tori_bean.getSinyoktk());
		excel.selectCell(7,18,false);
		excel.setCellValue(tori_bean.getOya_ktk());
		
		//DBアクセスを行い帳票出力
		this.sqlExec = appContext.getSqlExecuter();
		SateiExcelDbAcc dbacc = new SateiExcelDbAcc(sqlExec, log, appContext);
		//ヘッダ部
		dbacc.getHeader(excel);
		//取引先名称
		dbacc.getKanjoNm(excel);
		//業種・所在地
		dbacc.getGyosyu_syozaichi(excel);
		//抽出事由
		dbacc.getTyusyutujiyu(excel);
		//親会社名称
		dbacc.getOya_nm(excel);
		//財務概要
		dbacc.getZaimu(excel);
		//査定登録データ
		String torihikisaki_kbn = dbacc.getSateiData(excel);
		//勘定科目名称
		dbacc.getKanjoKamokuNm(excel);
		//引当金判定データ
		dbacc.getHikiateData(excel);
		//第三者留保債務内訳
		dbacc.getOth_ryuho(excel);
		//コメント
		dbacc.getComment(excel,torihikisaki_kbn);
	}

	/**
	 * 【債務明細帳票出力】
	 */
	public void saimuMeisai(Excel excel) throws Exception {
		String title = null;

		//シート選択
		excel.selectSheet(SAIMUMEISAI);
		//スタイルの設定
		this.setStyle(excel,StartRow_3101,endClm_3101);

		//ヘッダ部情報出力	
		excel.selectCell(2,0,false);
		title = Function.replaceExpression(excel.getStringCellValue(),this.ymHantei(tori_bean.getTaisyo_ym()));
		excel.setCellValue(title);
		excel.selectCell(3,1,true);
		excel.setCellValue(tori_bean.getKanjo_cd());
		excel.selectCell(4,1,true);
		excel.setCellValue(tori_bean.getKanjo_nm());

		//DBアクセスを行い帳票出力
		this.sqlExec = appContext.getSqlExecuter();
		RyuuhoExcelDbAcc dbacc = new RyuuhoExcelDbAcc(sqlExec, log, appContext);

		//ヘッダ部情報の取得
		dbacc.getHeader(excel);

		// 一覧情報取得
		boolean result = dbacc.getMeisai(excel);

		//シート名設定
		if(result){
			if(appform.getLangMode().equals(GS.LANG_JA)){
				excel.setSheetName(SAIMUMEISAI,SHEETNAME_JA_3101);
			} else{
				excel.setSheetName(SAIMUMEISAI,SHEETNAME_EN_3101);
			}
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
				.append(tori_bean.getSystem_kbn())
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
			excel.setDisplayFileName(OUTFILENAME_JA
										+ GS.HAIHUN
										+ tori_bean.getBunrui2().trim()
										+ GS.HAIHUN
										+ tori_bean.getKanjo_cd().trim()
										+ GS.HAIHUN
										+ tori_bean.getKanjo_nm()
										+ EXTENTION);
		}else{
			excel.setDisplayFileName(OUTFILENAME_EN
										+ GS.HAIHUN
										+ tori_bean.getBunrui2().trim()
										+ GS.HAIHUN
										+ tori_bean.getKanjo_cd().trim()
										+ GS.HAIHUN
										+ tori_bean.getKanjo_nm()
										+ EXTENTION);
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

	/**
	 * 【所在国編集】
	 * 
	 * @throws Exception
	 */
	private String getSyozaikoku(String kuni_cd,String kuni_nm) throws Exception {
		// 課題No.132
		// 追加開始
		StringBuffer syozaikoku = new StringBuffer(GS.EMPTY_CHARCTER);
		if(!Function.trim(kuni_cd).equals(GS.EMPTY_CHARCTER)){
		// 追加完了
				syozaikoku.append(kuni_cd)
					.append(GS.KAKKO_HIDARI)
					.append(kuni_nm)
					.append(GS.KAKKO_MIGI);
			}
		return syozaikoku.toString();
	}

}