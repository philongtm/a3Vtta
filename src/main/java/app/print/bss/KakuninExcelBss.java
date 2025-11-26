/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/17		SSC				課題No.124 英語版引当金確認帳票名称修正
003		2014/03/17		SSC				案件No.D13493 改善対応
004		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
******************************************************************************/
package app.print.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.print.dbAcc.KakuninExcelDbAcc;
import common.AppContext;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LD1101_引当金確認帳票 ビジネスロジッククラス
 */
public class KakuninExcelBss {

	private AppContext appContext				= null;//ＡＰＰコンテキスト
	private SessionData cmnData				= null;//ＡＰＰコンテキスト
	private AppPagerActionForm appForm			= null;//アクションform
	private Excel excel						= null;
	private TorihikisakiBean toriBean			= null;	// 取引先情報
	
	private final String EXTENTION  = ".xls";
	private final String TEMPLATE_EXCEL		= "LD1101_{1}_{2}";
	private final String DISP_FILE_NAME_JA		= "引当金確認";
	//課題No.124
	//修正開始
	//private final String DISP_FILE_NAME_EN	= "Verification.xls";
	private final String DISP_FILE_NAME_EN		= "Confirmation";
	//修正完了
	private final String SHEET_NAME_JA			= "引当金確認シート";
	//課題No.124
	//修正開始
	//private final String SHEET_NAME_EN		= "Verification";
	private final String SHEET_NAME_EN			= "Confirmation";
	//修正完了
    private static final String HAIHUN		= "-";
	
	//検索用アイテム
	private final String PHASE_MAP_KEY			= "phase";
	private final String ANKENNO_MAP_KEY		= "anken_no";
	private HashMap<String,String> kensakuMap	= null;

	private static final int ZENKI_CELL			= 9;
	private static final int KARIKIJUNBI_CELL	= 15;
	private static final int KONKI_CELL			= 21;
	private static boolean FALSE					= false;
	private static final String TAISHO_YM			= "taisho_ym";	
	private static final String PATTERN = ".*\\{[0-9]\\}"; 

	/**
	 * コンストラクタ
	 */
	public KakuninExcelBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.appForm = (AppPagerActionForm)appContext.getActionForm();
		this.cmnData = appContext.getCMN();
		this.toriBean = cmnData.getTori_bean();
	}

	/**
	 * 出力用に対象年月編集 <br>
	 * 
	 * @exception SQLException
	 */
	private List<String> ymHantei(String ym) throws Exception {
		String[] ymHairetu = Function.StrSplitToken(ym,GS.SLASH);
		//年月編集用
		List<String> ymList = new ArrayList<String>();
		ymList.add(GS.EMPTY_CHARCTER);
		ymList.add(GS.EMPTY_CHARCTER);
		int yyyyIdxPrint = 0;
		int mmIdxPrint = 1;
		int yyyyIdx = 0;
		int mmIdx = 1;
		//年と月のインデックス確定
		if(GS.LANG_EN.equals(cmnData.getComLangMode())){
			yyyyIdx = 1;
			mmIdx = 0;
		}
		//帳票の年と月のインデックス確定
		if(GS.LANG_EN.equals(appForm.getLangMode())){
			yyyyIdxPrint = 1;
			mmIdxPrint = 0;
		}
		ymList.set(yyyyIdxPrint,ymHairetu[yyyyIdx]);
		ymList.set(mmIdxPrint,ymHairetu[mmIdx]);
		return ymList;
	}

	/**
	 * 出力用に対象年月編集 <br>
	 * 
	 * @exception SQLException
	 */
	private String ymHanteiSyousai(String ym) throws Exception {
		StringBuffer sb = new StringBuffer();
		String[] ymHairetu = Function.StrSplitToken(ym,GS.SLASH);
		//年月編集用
		List<String> ymList = new ArrayList<String>();
		ymList.add(GS.EMPTY_CHARCTER);
		ymList.add(GS.EMPTY_CHARCTER);
		int yyyyIdxPrint = 0;
		int mmIdxPrint = 1;
		int yyyyIdx = 0;
		int mmIdx = 1;
		//年と月のインデックス確定
		if(GS.LANG_EN.equals(cmnData.getComLangMode())){
			yyyyIdx = 1;
			mmIdx = 0;
		}
		//帳票の年と月のインデックス確定
		if(GS.LANG_EN.equals(appForm.getLangMode())){
			yyyyIdxPrint = 1;
			mmIdxPrint = 0;
		}
		ymList.set(yyyyIdxPrint,ymHairetu[yyyyIdx]);
		ymList.set(mmIdxPrint,ymHairetu[mmIdx]);
		sb.append(ymList.get(0)).append(GS.SLASH).append(ymList.get(1));
		return sb.toString();
	}

	/**
	 * 出力用に査定期編集 <br>
	 * 
	 * @exception SQLException
	 */
	private List<String> sateiYmHantei(String ym) throws Exception {
		//年月編集用
		List<String> ymList = new ArrayList<String>();
		ymList.add(GS.EMPTY_CHARCTER);
		ymList.add(GS.EMPTY_CHARCTER);
		String[] ymHairetu = new String[2];
		int yyyyIdxPrint = 0;
		int mmIdxPrint = 1;

		ymHairetu[0] = ym.substring(0,4);
		ymHairetu[1] = ym.substring(4);

		//帳票の年と月のインデックス確定
		if(GS.LANG_EN.equals(appForm.getLangMode())){
			yyyyIdxPrint = 1;
			mmIdxPrint = 0;
		}
		ymList.set(yyyyIdxPrint,ymHairetu[0]);
		ymList.set(mmIdxPrint,ymHairetu[1]);
		return ymList;
	}


	/**
	 * テンプレート名編集 <br>
	 * 
	 * @return String
	 * @exception SQLException
	 */
	private String replaceExpression(String ym) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(toriBean.getSystem_kbn());
		list.add(appForm.getLangMode());
		String rtnString = Function.replaceExpression(TEMPLATE_EXCEL,list);
		return rtnString;
	}

	/**
	 * 【帳票ダウンロード項目の取得】
	 */
	public void execute() throws Exception{
		//Excelの作成
		excel = new Excel(appContext,this.replaceExpression(TEMPLATE_EXCEL),0);
		//Excelの表示名・シート名設定
		if(GS.LANG_JA.equals(appForm.getLangMode())){
			excel.setDisplayFileName(DISP_FILE_NAME_JA
										+ GS.HAIHUN
										+ toriBean.getBunrui2().trim()
										+ GS.HAIHUN
										+ toriBean.getKanjo_cd().trim()
										+ GS.HAIHUN
										+ toriBean.getKanjo_nm().trim()
										+ EXTENTION);
			excel.setSheetName(0,SHEET_NAME_JA);
		}else{
			excel.setDisplayFileName(DISP_FILE_NAME_EN
										+ GS.HAIHUN
										+ toriBean.getBunrui2().trim()
										+ GS.HAIHUN
										+ toriBean.getKanjo_cd().trim()
										+ GS.HAIHUN
										+ toriBean.getKanjo_nm().trim()
										+ EXTENTION);
			excel.setSheetName(0,SHEET_NAME_EN);
		}
		
		//DBアクセスを行わず帳票出力
		this.notDbAcc(excel);
		
		//DBアクセスを行い帳票出力
		KakuninExcelDbAcc dbacc = new KakuninExcelDbAcc(appContext);
		
		//ヘッダ部情報の取得
		dbacc.getHeader(excel,toriBean);
		dbacc.getKanjoNm(excel,toriBean);
		
		//勘定科目名称の取得
		dbacc.getKanjoKamokuNm(excel,toriBean);

		//今期情報の取得
		kensakuMap = this.getKensakuItem(dbacc,excel,KONKI_CELL);
		if(kensakuMap != null){
			this.getData(dbacc,excel,toriBean.getAnken_no(),toriBean.getPhase(),KONKI_CELL);
		}
		
		//仮基準日情報の取得
		kensakuMap = this.getKensakuItem(dbacc,excel,KARIKIJUNBI_CELL);
		if(kensakuMap != null){
			this.getData(dbacc,excel,kensakuMap.get(ANKENNO_MAP_KEY),kensakuMap.get(PHASE_MAP_KEY),KARIKIJUNBI_CELL);
		}else{
			this.getDataMeisai(dbacc,excel,KARIKIJUNBI_CELL);
		}

		//前期情報の取得
		kensakuMap = this.getKensakuItem(dbacc,excel,ZENKI_CELL);
		if(kensakuMap != null){
			this.getData(dbacc,excel,kensakuMap.get(ANKENNO_MAP_KEY),kensakuMap.get(PHASE_MAP_KEY),ZENKI_CELL);
		}else{
			this.getDataMeisai(dbacc,excel,ZENKI_CELL);
		}
				
		//テンプレートの単位が通貨コードで置換されていない場合、空文字で置換
		excel.selectCell(12,26,FALSE);
		if(Function.matches(excel.getStringCellValue(),PATTERN)) {
    		excel.setCellValue(Function.replaceString(excel.getStringCellValue(),GS.EMPTY_CHARCTER));
		}

		//ダウンロード実行
		excel.download();
	}

	/**
	 * 【DBアクセスの必要の無い項目の帳票出力】
	 */
	private void notDbAcc(Excel excel) throws Exception {
		String tempYmYou = null;
		
		//決算資料年月
		excel.selectCell(0,27,FALSE);
		tempYmYou = Function.replaceExpression(excel.getStringCellValue(),this.sateiYmHantei(toriBean.getSatei_ki()));
		excel.setCellValue(tempYmYou);
		//タイトル部
		excel.selectCell(3,1,FALSE);
		tempYmYou = Function.replaceExpression(excel.getStringCellValue(),this.ymHantei(toriBean.getTaisyo_ym_hyoji()));
		excel.setCellValue(tempYmYou);
		//取引先コード
		excel.selectRow(7,FALSE);
		excel.selectCell(4,FALSE);
		excel.setCellValue(toriBean.getKanjo_cd());
		//Duns No.
		excel.selectCell(12,FALSE);
		excel.setCellValue(toriBean.getTogo_tori_cd());
		
		//今期年月
		excel.selectCell(14,21,FALSE);
		tempYmYou = this.ymHanteiSyousai(toriBean.getTaisyo_ym_hyoji());
		excel.setCellValue(tempYmYou);

		//ハイフンセット
    	excel.selectCell(36,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(36,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);

    	excel.selectCell(39,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(39,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(40,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(40,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(41,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(41,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(42,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(42,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(43,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(43,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(46,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(46,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(49,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(49,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(51,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(51,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(52,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(52,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	
    	excel.selectCell(53,KONKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
    	excel.selectCell(53,ZENKI_CELL,FALSE);
    	excel.setCellValue(HAIHUN);
	}

	/**
	 * 【基本情報＆検索アイテム取得】
	 */
	private HashMap<String,String> getKensakuItem(KakuninExcelDbAcc dbacc,Excel excel,int START_CELL) throws Exception {
		String kensakuAnkenNo = GS.EMPTY_CHARCTER;
		kensakuMap = null;

		//案件No.の取得
		if(START_CELL == KARIKIJUNBI_CELL){
			kensakuAnkenNo = dbacc.getKariKijunbiAnkenNo(toriBean);
		}else if(START_CELL == ZENKI_CELL){
			kensakuAnkenNo = dbacc.getZenkiAnkenNo(toriBean);
		}else{
			kensakuAnkenNo = Function.trim(toriBean.getAnken_no());
		}

		//案件No.が存在しない場合、ここでリターン
		if(GS.EMPTY_CHARCTER.equals(kensakuAnkenNo)){
			return kensakuMap;
		}
		
		//案件No.のフェーズの取得
		kensakuMap = dbacc.getKensakuMap(excel,kensakuAnkenNo,toriBean,START_CELL);

		//基本情報取得
		dbacc.getInf(excel,toriBean,kensakuMap,START_CELL);

		return kensakuMap;
	}

	/**
	 * 【データ取得】
	 */
	private void getData(KakuninExcelDbAcc dbacc,Excel excel,String kensakuAnkenNo,String kensakuPhase,int START_CELL) throws Exception {
		//査定データの取得
		dbacc.getSateiData(excel,toriBean,kensakuAnkenNo,kensakuPhase,START_CELL);
		
		//引当データの取得
		dbacc.getHikiateData(excel,toriBean,kensakuAnkenNo,START_CELL);

		//コメントの取得
		dbacc.getComment(excel,kensakuAnkenNo,kensakuPhase,START_CELL);
	}

	/**
	 * 【データ取得】
	 */
	private void getDataMeisai(KakuninExcelDbAcc dbacc,Excel excel,int START_CELL) throws Exception {
		//明細データの取得
        if(KARIKIJUNBI_CELL == START_CELL){
            Map<String,String> ymMap = dbacc.getTaishoYmMap(toriBean);
            dbacc.getDataMeisai(excel,toriBean,ymMap.get(TAISHO_YM),START_CELL);
        }else{
            String zenSateiki = this.getZenSateiki(toriBean.getSatei_ki());
            dbacc.getDataMeisai(excel,toriBean,zenSateiki,START_CELL);
        }
	}

    /**
     * 
     * 引数で指定された査定期の前査定期を返す <br>
     * 
     * @param String
     * @param String
     * @return String
     * @throws Exception
     */
    private String getZenSateiki(String sateiki) throws Exception {
		DateFormat df = null;
    	String result = null;
    	Calendar cal = Calendar.getInstance();
    	String[] nengetsu = new String[2];
    	nengetsu[0] = sateiki.substring(0,4);
    	nengetsu[1] = sateiki.substring(4);
		df = new SimpleDateFormat("yyyyMM");
    	cal.set(Integer.parseInt(nengetsu[0]),Integer.parseInt(nengetsu[1]),1);
    	cal.setTime(cal.getTime());
    	cal.add(Calendar.MONTH,-7);
    	result = df.format(cal.getTime());
    	
    	return result;
    }
}