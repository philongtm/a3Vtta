/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/17		SSC				案件No.D13493 改善対応
003		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
004		2014/05/21		SSC				案件No.D13493 改善対応（ファイル名文字化け追加対応）
******************************************************************************/
package app.print.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.print.dbAcc.TairyuExcelDbAcc;
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
 * OZ6107_債務明細照会タブ ビジネスロジッククラス
 */
public class TairyuExcelbss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;	// 共通セッション
	private AppPagerActionForm form;	

	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private String GAMEN_ID = "LB1101_";
	private String FILE_NM_JA = "実質滞留債権判定シート";
	private String FILE_NM_EN = "Judgment_of_Overdue_Dept_Sheet";
	private String EXTENTION  = ".xls";
	private static final int printendClm = 20;	//印刷範囲最終列

	/**
	 * コンストラクタ
	 */
	public TairyuExcelbss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.form = (AppPagerActionForm)appContext.getActionForm();
		torihikisaki_bean = cmnData.getTori_bean();
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 */
	public String execute() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TairyuExcelDbAcc dbacc = new TairyuExcelDbAcc(sqlExec, log, appContext);
		//Excelの作成
		Excel excel = new Excel(appContext,GAMEN_ID + torihikisaki_bean.getSystem_kbn()
				+ GS.UNDERLINE + form.getLangMode(),22);
		
		int i = 8;
		for(int j=0;j<=20;j++) {
			excel.copyCellStyle(i,j,j);
		}
		getHeader(excel);
		
		String ym = torihikisaki_bean.getTaisyo_ym_hyoji();	
		List ymList = new ArrayList<String>(2);
		ymList = ymHantei(ym);
		excel.selectCell(3,1,true);
		String title = excel.getStringCellValue();
		
		Function.replaceExpression(title, ymList);
		String Num = Function.replaceExpression(title, ymList);
		excel.setCellValue(Num);
		
		//ヘッダブ情報の取得 
		dbacc.getChohyo(excel);
		dbacc.getSelect(excel);	
		dbacc.getTairyu(excel);

		//ファイル名取得
	
		dbacc.getMeisai(excel);
		
		if(form.getLangMode().equals(GS.LANG_JA)) {
		String name = (FILE_NM_JA
						+ GS.HAIHUN
						+ torihikisaki_bean.getBunrui2().trim()
						+ GS.HAIHUN
						+ torihikisaki_bean.getKanjo_cd().trim()
						+ GS.HAIHUN
						+ torihikisaki_bean.getKanjo_nm()
						+ EXTENTION);
		excel.setDisplayFileName(name);
		
		}else{
		String name = (FILE_NM_EN
						+ GS.HAIHUN
						+ torihikisaki_bean.getBunrui2().trim()
						+ GS.HAIHUN
						+ torihikisaki_bean.getKanjo_cd().trim()
						+ GS.HAIHUN
						+ torihikisaki_bean.getKanjo_nm()
						+ EXTENTION);
		excel.setDisplayFileName(name);
		}		
		
		//印刷範囲設定
		excel.setPrintAria(0,0,printendClm,0,excel.getLastRowIndex());
		
		//ダウンロードの実行
		excel.download();

		//--ダウンロード
		return null;
	}
	
	private List<String> ymHantei(String ym){
		String[] ymHairetu = Function.StrSplitToken(torihikisaki_bean.getTaisyo_ym_hyoji(),GS.SLASH);
		
		//年月編集用
		List<String> ymList = new ArrayList<String>(2);
		int yyyy = 0;
		int mm = 1;
		
		//年と月のインデックス確定
		if(GS.LANG_EN.equals(cmnData.getComLangMode())){
			yyyy = 1;
			mm = 0;
		}
		//帳票の年と月のインデックス確定
		
		if(GS.LANG_EN.equals(form.getLangMode())){
			yyyy = 1;
			mm = 0;
		}
		if(GS.LANG_EN.equals(cmnData.getComLangMode()) && GS.LANG_EN.equals(form.getLangMode())){
			yyyy = 0;
			mm = 1;
		}
		ymList.add(ymHairetu[yyyy]);
		ymList.add(ymHairetu[mm]);
			return ymList;
			
		}
	
	private void getHeader(Excel excel) {
		//共)取引先情報.勘定先コード
		excel.selectCell(4,2,true);
		excel.setCellValue(torihikisaki_bean.getKanjo_cd());
		
		//共)取引先情報.信用格付情報
		excel.selectCell(4,5,true);
		String ktk = excel.getStringCellValue();
		String sin = ktk + Function.trim(torihikisaki_bean.getSinyoktk());
		excel.setCellValue(sin);
		
		//滞留案件No.
		excel.selectCell(0,21,true);
		excel.setCellValue(torihikisaki_bean.getAnken_no());
		
	}		
}
