/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
003		2009/11/04		SSC				課題No.27 国内フォント統一対応
004		2009/11/17		SSC				課題No.117 2バイト文字対応 
005		2009/12/1		SSC				課題No.171 留保債務の表示不正 対応
006		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
007		2009/12/2		SSC				課題No.179 帳票_保有株(ラベル) 対応
008		2009/12/8		SSC				課題No.192 ヘッダ部、金額出力部の修正
009		2010/01/15		SSC				課題No.233 決算期表示変更＆出力財務データ不正
010		2014/03/14		SSC				案件No.D13493 改善対応
011		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.commonZen.form.HikiatekinHanteiSyokaiForm;
import app.commonZen.form.TorihikisakiGaiyoSyokaiForm;
import app.commonZen.form.TorihikisakiKubunHanteiSyokaiForm;
import app.satei.form.HikiateForm;
import app.syokai.form.SateisyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.AppDownloadAction;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import common.struts.adapter.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

//No219, 2008/05/29, SJA渡辺, 帳票をまとめる処理追加
/**
* 査定詳細ExcelDBアクセスクラス
*/
public class SateiSyosaiExcelDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;
	private SessionDataZen cmnData = null;						// 共通セッションデータ
	private SimpleDateFormat sdfYMD = null;					// 一時ファイル作成時のフォーマット	
	private final String IN_FILE_NAME = "Template6";
	private final String IN_FILE_NAME_E = "Template6_e";
	private final String OUT_FILE_NAME = "査定詳細";
	private final String OUT_FILE_NAME_E = "SateiSyosai";
	private final String EXTENTION  = ".xls";
	// No786, 2008/06/05, SJA渡辺, シート名に「シート」をいれないように修正
	private final String SHEET_NAME0 = "判定シート";			// 作成ファイルシート名
	private final String SHEET_NAME1 = "債権明細一覧";			// 作成ファイルシート名
	private final String SHEET_NAME2 = "債務明細一覧";			// 作成ファイルシート名
	private final String SHEET_NAME_E0 = "Judgment";				// 作成ファイルシート名
	private final String SHEET_NAME_E1 = "CreditDetails";				// 作成ファイルシート名
	private final String SHEET_NAME_E2 = "Debt";				// 作成ファイルシート名
	
	
	// INパラメータ
	private String returnId;						// 遷移元画面ID
	private String anken_no;						// 案件Ｎｏ．
	private String phase;							// フェーズ
	private String c_phase;							// フェーズ
	private String ym;								// 年月
	private String lang_mode;						// 言語モード
	private String swork;							// String work
	private String yyyymm;							// 年月
	private String dd;								// 日
	private String kikan_tori_cd;
	
	private int mode;
	private int cnt;
	private int chk1;
	private int chk2;
	private int chk3;
	
	private String fname;							// ファイル名
	private String sname0;							// シート名
	private String sname1;							// シート名
	private String sname2;							// シート名
	private String torihikisakimei;
	
	private int phase_no;							// フェーズＮｏ．
	
	
	
	/**
	 * コンストラクタ
	 */
	public SateiSyosaiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);
		this.appContext = appContext;
		
		cmnData = appContext.getCMNZenRe();
		returnId = cmnData.getReturnId();
		
		if(returnId.equals(GS.OS6102)){
			SateisyosaiForm form = (SateisyosaiForm)appContext.getActionForm();
			phase = cmnData.getPhase();
			c_phase = cmnData.getC_phase();
			anken_no = cmnData.getSatei_anken_no();
			ym = cmnData.getYm();
			kikan_tori_cd = cmnData.getKanjo_cd();
			torihikisakimei = cmnData.getKanjo_nm();
			mode = 0;
		}else{
			HikiateForm form = (HikiateForm)appContext.getActionForm();
			anken_no = cmnData.getSatei_anken_no();
			phase = cmnData.getPhase();
			c_phase = cmnData.getC_phase();
			ym = cmnData.getYm();
			kikan_tori_cd = cmnData.getKanjo_cd();
			torihikisakimei = cmnData.getKanjo_nm();
			mode = 1;
		}

		lang_mode = cmnData.getComLangMode();		
		
		chk1 = 0;
		chk2 = 0;
		chk3 = 0;
		
	}
	
	/**
	 * 検索検索SQL実行処理
	 * @throws SQLException
	 * @throws Exception
	 */
	public void execute(TorihikisakiGaiyoSyokaiForm torihikisakiGaiyoSyokaiForm,
			TorihikisakiKubunHanteiSyokaiForm torihikisakiKubunHanteiSyokaiForm,
			HikiatekinHanteiSyokaiForm hikiatekinHanteiSyokaiForm,
			ArrayList saikenMeisaiList,
			ArrayList ryuhoSaimuList) throws SQLException,Exception {

		boolean ryuhoFlag = false;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;
		ResultSet rs8 = null;
		ResultSet rs6 = null;
		
		StringBuffer sql8 = new StringBuffer()
		.append("SELECT ")
		.append("TO_CHAR(MAX(syori_dt),'yyyymmdd') AS syori_dt ")
		.append("FROM ")
		.append("SST_NYURYOKU_HIST ")
		.append("WHERE ")
		.append("anken_no ='")
		.append(anken_no)
		.append("' ")
		.append("AND phase = '")
		.append(phase)
		.append("' ")
		.append("AND ope_kbn = '80'");
		
		StringBuffer sql1 = new StringBuffer()
		.append("SELECT DISTINCT ")
		.append("MS.satei_kaisya_nm, ")
		.append("MS.satei_kaisya_nm_e ")
		.append(" FROM ")
		.append("SST_SATEI_STAT SS LEFT JOIN ")
		.append("SSM_SATEIKAISYA MS ON ")
		.append("TRIM(SS.satei_kaisha_cd) = TRIM(MS.satei_kaisha_cd) ")
		.append("AND SS.kaisha7_cd = MS.kaisha_cd ")
		.append("WHERE SS.anken_no = '")
		.append(anken_no)
		.append("'");
		
		// エクセルの一時ファイルを作成する
		sdfYMD = new SimpleDateFormat("yyyyMMdd");
		
		String suffix = sdfYMD.format(new Date());
		File dir = new File(AppContext.getTmpDir());
		
		if( dir.isDirectory()==false ) {
			dir.mkdir();
		}
		
		File tmpExcel = File.createTempFile(suffix+".",".xls",dir);
		dir = null;
		
		// 使用するテンプレート名
		String tname;
		
		// 作成した一時ファイル(エクセル)の内容を書き込む
		// 新規ワークブックを作成する
		if(lang_mode.equals("Ja")){
			fname = OUT_FILE_NAME
						+ GS.HAIHUN
						+ cmnData.getSatei_bumon_cd()
						+ GS.HAIHUN
						+ kikan_tori_cd
						+ GS.HAIHUN
						+ torihikisakimei
						+ EXTENTION;
			tname = IN_FILE_NAME;
			sname0 = SHEET_NAME0;
			sname1 = SHEET_NAME1;
			sname2 = SHEET_NAME2;
		}else{
			tname = IN_FILE_NAME_E;
			fname = OUT_FILE_NAME_E
						+ GS.HAIHUN
						+ cmnData.getSatei_bumon_cd()
						+ GS.HAIHUN
						+ kikan_tori_cd
						+ GS.HAIHUN
						+ torihikisakimei
						+ EXTENTION;
			sname0 = SHEET_NAME_E0;
			sname1 = SHEET_NAME_E1;
			sname2 = SHEET_NAME_E2;
		}
		// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
		POIFSFileSystem filein = new POIFSFileSystem(
				new FileInputStream( appContext.getRealPath(GS.EXCELDIR + tname + GS.DOTXLS)) );
		HSSFWorkbook wb = new HSSFWorkbook(filein);    	
		
		FileOutputStream fileOut = null;
		
		HSSFCellStyle style_ALIGN_RIGHT = wb.createCellStyle();
		style_ALIGN_RIGHT.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		HSSFCellStyle style_ALIGN_RIGHT_LINEB = wb.createCellStyle();
		style_ALIGN_RIGHT_LINEB.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style_ALIGN_RIGHT_LINEB.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		
		try{
			fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
			
			// ワークシートを変更する
			wb.setSheetName(0,sname0);    
			// ワークシートを変更する
			wb.setSheetName(1,sname1);
			// ワークシートを変更する
			wb.setSheetName(2,sname2);
			
			// セルオブジェクトの作成（セル番号は0スタート）
			// ヘッダ部分の作成(1行目は項目名称)
			HSSFCell[] cellHd = new HSSFCell[100];
			HSSFCell[] cellHd1 = new HSSFCell[100];
			HSSFCell[] cellHd2 = new HSSFCell[100];
			
			// ここで使用するシートを指定。
			HSSFSheet sheetW = wb.getSheetAt(0);
			HSSFSheet sheetW1 = wb.getSheetAt(1);
			HSSFSheet sheetW2 = wb.getSheetAt(2);
			
			if (ryuhoSaimuList == null || ryuhoSaimuList.size() == 0) {
				wb.setSheetName(2,"sheet2");
				ryuhoFlag = true;
			}
			
			try {
				// SQL実行		
				rs8 = sqlExec.execQuery(sql8.toString());
				rs1 = sqlExec.execQuery(sql1.toString());
				
				ArrayList othRyuhosaimuList = hikiatekinHanteiSyokaiForm.getOth_ryuhosaimu_uchiwake();
				ArrayList zaimuList = torihikisakiGaiyoSyokaiForm.getZaimu_gaiyo();
								
				for (int i=0; i<61; i++ ) {
					HSSFRow row = sheetW.getRow(i);
					if(row != null){
						
						for (int j=0; j<60; j++ ) {
							cellHd[j] = row.getCell((short)j);
                            /*
							if(cellHd[j] != null){
								cellHd[j].setEncoding(HSSFCell.ENCODING_UTF_16);
							}
                            */
						}
					}
					
					// 日付行
					if(i == 0){
						if(mode == 0){
							if (GS.STATUS_KANRYO.equals(cmnData.getStatus()) && rs8.next() && !GS.EMPTY_CHARCTER.equals(Function.trim(rs8.getString("syori_dt")))){
								//ステータスが承認のみ表示
								yyyymm = Function.insertYmNengetu(rs8.getString("syori_dt").substring(0,6));
								dd = rs8.getString("syori_dt").substring(6,8);
								
								// 課題No.158
								// 追加開始
								//String yyyymmdd = Function.insertDateSlash(rs8.getString("syori_dt").substring(0,8));
								String yyyymmdd = Function.insertDateSlash(rs8.getString("syori_dt").substring(0,8),lang_mode);
								// 追加完了
								
								// 照会の場合
								// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
								if ("Ja".equals(lang_mode)) {
									cellHd[59].setCellValue( yyyymm + dd + "日" + "　承認");
								} else {
									// 課題No.117 
									// 追加開始
									//cellHd[59].setCellValue( yyyymmdd + "　Approve");
									cellHd[59].setCellValue( yyyymmdd + "  Approve");
									// 追加完了
								}
							} else {
								// 承認以外は空白を挿入
								cellHd[59].setCellValue("");
							}
						}else{
							String sysdate = sdfYMD.format(new Date());
							yyyymm = Function.insertYmNengetu(sysdate.substring(0,6));
							dd = sysdate.substring(6,8);
							
							// 課題No.158
							// 追加開始
							//String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8));
							String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8),lang_mode);
							// 追加完了
							
							// 引当金判定の場合
							// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								cellHd[59].setCellValue( yyyymm + dd + "日" + "　作成");
							} else {
								// 課題No.117 
								// 追加開始
								//cellHd[59].setCellValue( yyyymmdd + "　Creation");
								cellHd[59].setCellValue( yyyymmdd + "  Creation");
								// 追加完了
							}
						}
						
						// 査定会社名
					}else if(i == 1){
						//cellHd[59].setCellValue(rs1.getString("satei_kaisya_nm"));
						if (rs1.next()) {
							if ("Ja".equals(lang_mode)) {
								cellHd[59].setCellValue(rs1.getString("satei_kaisya_nm"));
							} else {
								cellHd[59].setCellValue(rs1.getString("satei_kaisya_nm_e"));
							}
						} else {
							cellHd[59].setCellValue("");
						}
						
						
						// タイトル行	
					}else if(i == 2){
						//cellHd[1].setCellValue("取引先区分・債権区分・引当金　判定シート（" + Function.insertYmNengetu(rs1.getString("ym"))+ "末基準)");
						// No, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[1].setCellValue("取引先区分・債権区分・引当金　判定シート（" + Function.insertYmNengetu(ym)+ "末基準)");
						} else {
						// 2008/08/27 SJA佐々木　200808270007 単語の区切りのスペースを調整
							
							// 課題No.158
							// 追加開始
							//cellHd[1].setCellValue("Customer  Credit Category  Calculation of Reserves  Sheet(On the basis of " + Function.insertYmSlash(ym)+ ")");
							cellHd[1].setCellValue("Customer  Credit Category  Calculation of Reserves  Sheet(On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
							// 追加完了
							
						}
						
						// フェーズ行
					}else if(i == 3){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							// 照会の場合
							if(mode == 0){
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[59].setCellValue("＜対象先選定結果＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd[59].setCellValue("＜クレーム債権再設定結果＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd[59].setCellValue("＜一次査定結果＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd[59].setCellValue("＜一次査定検証結果＞");
									}else{
										cellHd[59].setCellValue("＜二次査定結果＞");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[59].setCellValue("＜対象先選定中＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd[59].setCellValue("＜クレーム債権再設定中＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd[59].setCellValue("＜一次査定中＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd[59].setCellValue("＜一次査定検証中＞");
									}else{
										cellHd[59].setCellValue("＜二次査定中＞");
									}
								}
							}else{
								// 引当金判定の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[59].setCellValue("＜一次査定中＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[59].setCellValue("＜一次査定検証中＞");
								}else{
									cellHd[59].setCellValue("＜二次査定中＞");
								}
							}
						} else {
							// 照会の場合
							if(mode == 0){
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[59].setCellValue("<Select Customer Result>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd[59].setCellValue("<Judgment of Claim Debt Result>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd[59].setCellValue("<Primary Assessment Result>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd[59].setCellValue("<Primary Assessment Verification Result>");
									}else{
										cellHd[59].setCellValue("<Secondary Assessment Result>");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[59].setCellValue("<Select Customer Processing>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd[59].setCellValue("<Judgment of Claim Debt Processing>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd[59].setCellValue("<Primary Assessment Processing>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd[59].setCellValue("<Primary Assessment Verification Processing>");
									}else{
										cellHd[59].setCellValue("<Secondary Assessment Processing>");
									}
								}
							}else{
								// 引当金判定の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[59].setCellValue("<Primary Assessment>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[59].setCellValue("<Primary Assessment Verification>");
								}else{
									cellHd[59].setCellValue("<Secondary Assessment>");
								}
							}
						}
						// 取引先CD、DUNS　No、債権区分、判定事由(登録区分30)
					}else if(i == 6){
						
						cellHd[4].setCellValue(kikan_tori_cd);
						cellHd[11].setCellValue(torihikisakiGaiyoSyokaiForm.getTogo_tori_cd());
						
						cellHd[36].setCellValue(torihikisakiKubunHanteiSyokaiForm.getSaiken_kbn_nm());
						
						
						if (torihikisakiKubunHanteiSyokaiForm.getComment_val_30() != null) {
							cellHd[44].setCellValue(torihikisakiKubunHanteiSyokaiForm.getComment_val_30().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						
						// 取引先名、信用格付、信用格付、親会社一体判断
					}else if(i == 7){
						
						if(lang_mode.equals("Ja")==true){
							if(torihikisakiGaiyoSyokaiForm.getBusiness_nm_kj() == null){
								cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getBusiness_nm());
							}else{
								cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getBusiness_nm_kj());
							}
						}else{
							cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getBusiness_nm());
						}
						
						
						if(torihikisakiKubunHanteiSyokaiForm.getKtk() != null){
							cellHd[16].setCellValue(torihikisakiKubunHanteiSyokaiForm.getKtk());
						}
						
						
						if (torihikisakiKubunHanteiSyokaiForm.getOya_duns_no() != null 
								&& Function.trim(torihikisakiKubunHanteiSyokaiForm.getOya_duns_no()).length() > 0) {
							if(torihikisakiKubunHanteiSyokaiForm.getOya_ktk() != null){
								cellHd[18].setCellValue(torihikisakiKubunHanteiSyokaiForm.getOya_ktk());
							}
						}
						
						if ("Ja".equals(lang_mode) && torihikisakiKubunHanteiSyokaiForm.getBusiness_nm_kj() != null) {
							cellHd[20].setCellValue(torihikisakiKubunHanteiSyokaiForm.getBusiness_nm_kj());
						} else {
							if (torihikisakiKubunHanteiSyokaiForm.getBusiness_nm() != null) {
								cellHd[20].setCellValue(torihikisakiKubunHanteiSyokaiForm.getBusiness_nm());
							}
						}
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if (torihikisakiKubunHanteiSyokaiForm.getOya_duns_no() != null 
								&& Function.trim(torihikisakiKubunHanteiSyokaiForm.getOya_duns_no()).length() > 0) {
							if(torihikisakiKubunHanteiSyokaiForm.getOya_ittai_flg() != null){
								if("1".equals(torihikisakiKubunHanteiSyokaiForm.getOya_ittai_flg())==true){
									cellHd[23].setCellValue(getKbnNm("oya_ittai_flg",torihikisakiKubunHanteiSyokaiForm.getOya_ittai_flg(),lang_mode));
								}
							}
							if(torihikisakiKubunHanteiSyokaiForm.getOya_dokuritu_flg() != null){
								if("1".equals(torihikisakiKubunHanteiSyokaiForm.getOya_dokuritu_flg())==true){
									cellHd[23].setCellValue(getKbnNm("oya_dokuritu_flg",torihikisakiKubunHanteiSyokaiForm.getOya_dokuritu_flg(),lang_mode));
								}
							}
						}
						
						// 滞留区分、名称
					}else if(i == 8){
						
						cellHd[18].setCellValue(torihikisakiKubunHanteiSyokaiForm.getTairyu_kbn_cd());
						cellHd[20].setCellValue(torihikisakiKubunHanteiSyokaiForm.getTairyu_kbn_nm());
						
						// 所在国、発生経緯
					}else if(i == 9){
						
						cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getWb_country_cd_nm());
						
						
						if (torihikisakiKubunHanteiSyokaiForm.getComment_val_40() != null) {
							cellHd[36].setCellValue(torihikisakiKubunHanteiSyokaiForm.getComment_val_40().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 所在地、正常先チェック
					}else if(i == 10){
						
						cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getAddress());
						
						if(torihikisakiKubunHanteiSyokaiForm.getSeijo_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getSeijo_chk())==true){
								if("Ja".equals(lang_mode)==true){
									cellHd[16].setCellValue("○");
								}else{
									cellHd[16].setCellValue("Y");
								}
								chk1 = 1;
							}
						}
						
						// 要注意先チェック
					}else if(i == 13){
						
						if(torihikisakiKubunHanteiSyokaiForm.getYochui_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getYochui_chk())==true){
								if("Ja".equals(lang_mode)==true){
									cellHd[16].setCellValue("○");
								}else{
									cellHd[16].setCellValue("Y");
								}
								chk1 = 1;
							}
						}
						
						// 抽出事由
					}else if(i == 15){
						
						ArrayList jiyuuCdList = torihikisakiGaiyoSyokaiForm.getJiyuuCdList();
						if (jiyuuCdList != null && jiyuuCdList.size() > 0) {
							Iterator itr = jiyuuCdList.iterator();
							while ( itr.hasNext() ) {
								String jiyuu_cd = (String)itr.next();
								// 破産更生
								if("01".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[13].setCellValue("◆");
									}else{
										cellHd[13].setCellValue("Y");
									}
								}
								// 貸倒懸念
								if("02".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[11].setCellValue("◆");
									}else{
										cellHd[11].setCellValue("Y");
									}
								}
								// M12
								if("04".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[9].setCellValue("◆");
									}else{
										cellHd[9].setCellValue("Y");
									}
								}
								// 要注意
								if("05".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[3].setCellValue("◆");
									}else{
										cellHd[3].setCellValue("Y");
									}
								}
								// 低格付
								if("06".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[1].setCellValue("◆");
									}else{
										cellHd[1].setCellValue("Y");
									}
								}
								// 低格付
								if("07".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[1].setCellValue("◆");
									}else{
										cellHd[1].setCellValue("Y");
									}
								}
								// 債務超過
								if("08".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[7].setCellValue("◆");
									}else{
										cellHd[7].setCellValue("Y");
									}
								}
								// 赤字
								if("09".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[5].setCellValue("◆");
									}else{
										cellHd[5].setCellValue("Y");
									}
								}
							}
						}
						
					}else if(i == 17){
						
						ArrayList jiyuuCdList = torihikisakiGaiyoSyokaiForm.getJiyuuCdList();
						if (jiyuuCdList != null && jiyuuCdList.size() > 0) {
							Iterator itr = jiyuuCdList.iterator();
							while ( itr.hasNext() ) {
								String jiyuu_cd = (String)itr.next();
								// リ企指定
								if("03".equals(jiyuu_cd)==true ){
									if("Ja".equals(lang_mode)==true){
										cellHd[1].setCellValue("◆");
									}else{
										cellHd[1].setCellValue("Y");
									}
								}
							}
						}
						
						
						if (torihikisakiKubunHanteiSyokaiForm.getComment_val_20() != null) {
							if(chk1 == 1){
								cellHd[16].setCellValue(torihikisakiKubunHanteiSyokaiForm.getComment_val_20().replaceAll("\r\n","\n").replaceAll("\t"," "));
							}
						}
						// 単位
					} else if (i == 18) {
						// No526, 2008/06/05, SJA渡辺, 英語モードの際の単位追加
						if ("Ja".equals(lang_mode)) {
							cellHd[42].setCellValue("単位：円");
							cellHd[59].setCellValue("単位：円");
						} else {
							cellHd[42].setCellValue("Unit:JPY");
							cellHd[59].setCellValue("Unit:JPY");
						}
						
						// 業種、取引先コード、勘定先名称、勘定科目、金額、受取手形
					}else if(i == 20){
						
						if(lang_mode.equals("Ja")==true){
							if(torihikisakiGaiyoSyokaiForm.getSic_sm_nm_kj()==null){
								cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getSic_sm_cd_nm_en());
							}else{
								cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getSic_sm_cd_nm_kj());
							}
						}else{
							cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getSic_sm_cd_nm_en());
						}
						
						HashMap map = (HashMap)othRyuhosaimuList.get(0);
						cellHd[44].setCellValue((String)map.get("kikan_tori_cd"));
						cellHd[48].setCellValue((String)map.get("torisaki_nm"));
						cellHd[52].setCellValue((String)map.get("kanjo_nm"));
						
						if (!"".equals(Function.trim((String)map.get("kingaku")))) {
							cellHd[56].setCellValue((String)map.get("kingaku"));
						}
						
						if(hikiatekinHanteiSyokaiForm.getUketoritegata() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getUketoritegata()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getUketoritegata());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getUketoritegata())));
						}
						
						// 業種内容、輸出受取手形
					}else if(i == 21){
						
						HashMap map = (HashMap)othRyuhosaimuList.get(1);
						cellHd[44].setCellValue((String)map.get("kikan_tori_cd"));
						cellHd[48].setCellValue((String)map.get("torisaki_nm"));
						cellHd[52].setCellValue((String)map.get("kanjo_nm"));
						if(!"".equals(Function.trim((String)map.get("kingaku")))){
							cellHd[56].setCellValue((String)map.get("kingaku"));
						}
						cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getJigyonaiyo());
						
						if(hikiatekinHanteiSyokaiForm.getYusyutu_uketoritegata() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getYusyutu_uketoritegata()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getYusyutu_uketoritegata());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getYusyutu_uketoritegata())));
						}
						
						// 貸倒懸念先、売掛金
					}else if(i == 22){
						
						HashMap map = (HashMap)othRyuhosaimuList.get(2);
						cellHd[44].setCellValue((String)map.get("kikan_tori_cd"));
						cellHd[48].setCellValue((String)map.get("torisaki_nm"));
						cellHd[52].setCellValue((String)map.get("kanjo_nm"));
						if(!"".equals(Function.trim((String)map.get("kingaku")))){
							cellHd[56].setCellValue((String)map.get("kingaku"));
						}
						cellHd[4].setCellValue(torihikisakiGaiyoSyokaiForm.getJigyonaiyo());
						
						if(torihikisakiKubunHanteiSyokaiForm.getTyoka_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getKanwa_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getEntai_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getTyoka_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getKanwa_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getEntai_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[16].setCellValue("○");
								}else{
									cellHd[16].setCellValue("Y");
								}
								chk2 = 1;
							}
						}
						
						if(hikiatekinHanteiSyokaiForm.getUrikakekin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getUrikakekin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getUrikakekin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getUrikakekin())));
						}
						
						
						// 株主構成1、取引前渡金
					}else if(i == 23){
						
						cellHd[5].setCellValue(torihikisakiGaiyoSyokaiForm.getKabunusi_nm1());
						
						if(torihikisakiGaiyoSyokaiForm.getKabusu1()!=null){
							cellHd[10].setCellValue(torihikisakiGaiyoSyokaiForm.getKabusu1());
							
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							// 課題No179
							// 追加開始
							/*
							if ("Ja".equals(lang_mode)) {
								cellHd[12].setCellValue("株");
							} else {
								cellHd[12].setCellValue("Stock");
							}
							*/
							// 追加完了
						}

						
						if(torihikisakiGaiyoSyokaiForm.getHiritu1()!=null){
							cellHd[13].setCellValue(torihikisakiGaiyoSyokaiForm.getHiritu1());
							// 課題No.179
							// 追加開始
							//cellHd[14].setCellValue("%");
							// 追加完了
						}
						
						if(hikiatekinHanteiSyokaiForm.getTorihikimaetokin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getTorihikimaetokin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getTorihikimaetokin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getTorihikimaetokin())));
						}
						
						// 株主構成2、立替金
					}else if(i == 24){
						
						cellHd[5].setCellValue(torihikisakiGaiyoSyokaiForm.getKabunusi_nm2());
						if(torihikisakiGaiyoSyokaiForm.getKabusu2()!=null){
							cellHd[10].setCellValue(torihikisakiGaiyoSyokaiForm.getKabusu2());
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							// 課題No179
							// 追加開始
							/*
							if ("Ja".equals(lang_mode)) {
								cellHd[12].setCellValue("株");
							} else {
								cellHd[12].setCellValue("Stock");
							}
							*/
							// 追加完了
						}
						if(torihikisakiGaiyoSyokaiForm.getHiritu2()!=null){
							cellHd[13].setCellValue(torihikisakiGaiyoSyokaiForm.getHiritu2());
							// 課題No179
							// 追加開始
							//cellHd[14].setCellValue("%");
							// 追加完了
						}
						if(hikiatekinHanteiSyokaiForm.getTatekaekin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getTatekaekin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getTatekaekin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getTatekaekin())));
						}
						
						// No526, 2008/06/05, SJA渡辺, 英語モードの際の単位追加
						if ("Ja".equals(lang_mode)) {
							cellHd[59].setCellValue("単位：円");
						} else {
							cellHd[59].setCellValue("Unit:JPY");
						}
						// 株主構成3、実質債務超過、未収入金
					}else if(i == 25){
						
						cellHd[5].setCellValue(torihikisakiGaiyoSyokaiForm.getKabunusi_nm3());
						if(torihikisakiGaiyoSyokaiForm.getKabusu3()!=null){
							cellHd[10].setCellValue(torihikisakiGaiyoSyokaiForm.getKabusu3());
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							// 課題No179
							// 追加開始
							/*
							if ("Ja".equals(lang_mode)) {
								cellHd[12].setCellValue("株");
							} else {
								cellHd[12].setCellValue("Stock");
							}
							*/
							// 追加完了
						}
						if(torihikisakiGaiyoSyokaiForm.getHiritu3()!=null){
							cellHd[13].setCellValue(torihikisakiGaiyoSyokaiForm.getHiritu3());
							// 課題No179
							// 追加開始
							//cellHd[14].setCellValue("%");
							// 追加完了
						}
						
						
						if(torihikisakiKubunHanteiSyokaiForm.getTyoka_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getTyoka_chk())==true){
								if("Ja".equals(lang_mode)==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						
						
						if(hikiatekinHanteiSyokaiForm.getMisyunyukin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getMisyunyukin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getMisyunyukin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getMisyunyukin())));
						}
						
						
						// 株主構成4、契約額（不動産担保、動産担保、貿易保険、その他)、未収収益
					}else if(i == 26){
						
						cellHd[5].setCellValue(torihikisakiGaiyoSyokaiForm.getKabunusi_nm4());
						if(torihikisakiGaiyoSyokaiForm.getKabusu4()!=null){
							cellHd[10].setCellValue(torihikisakiGaiyoSyokaiForm.getKabusu4());
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							// 課題No179
							// 追加開始
							/*
							if ("Ja".equals(lang_mode)) {
								cellHd[12].setCellValue("株");
							} else {
								cellHd[12].setCellValue("Stock");
							}
							*/
							// 追加完了
						}
						if(torihikisakiGaiyoSyokaiForm.getHiritu4()!=null){
							cellHd[13].setCellValue(torihikisakiGaiyoSyokaiForm.getHiritu4());
							// 課題No179
							// 追加開始
							//cellHd[14].setCellValue("%");
							// 追加完了
						}
						
						
						if(hikiatekinHanteiSyokaiForm.getHudosan_k() != null ){
							cellHd[48].setCellValue(hikiatekinHanteiSyokaiForm.getHudosan_k());
						}
						if(hikiatekinHanteiSyokaiForm.getDosan_k() != null ){
							cellHd[51].setCellValue(hikiatekinHanteiSyokaiForm.getDosan_k());
						}
						if(hikiatekinHanteiSyokaiForm.getHoken_k() != null ){
							cellHd[54].setCellValue(hikiatekinHanteiSyokaiForm.getHoken_k());
						}
						if(hikiatekinHanteiSyokaiForm.getSonota_k() != null ){
							cellHd[57].setCellValue(hikiatekinHanteiSyokaiForm.getSonota_k());
						}
						
						if(hikiatekinHanteiSyokaiForm.getMisyusyueki() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getMisyusyueki()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getMisyusyueki());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getMisyusyueki())));
						}
						
						
						// 株主構成5、評価額（不動産担保、動産担保、貿易保険、その他)、短期貸付金
					}else if(i == 27){
						
						cellHd[5].setCellValue(torihikisakiGaiyoSyokaiForm.getKabunusi_nm5());
						if(torihikisakiGaiyoSyokaiForm.getKabusu5()!=null){
							cellHd[10].setCellValue(torihikisakiGaiyoSyokaiForm.getKabusu5());
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							// 課題No179
							// 追加開始
							/*
							if ("Ja".equals(lang_mode)) {
								cellHd[12].setCellValue("株");
							} else {
								cellHd[12].setCellValue("Stock");
							}
							*/
							// 追加完了
						}
						if(torihikisakiGaiyoSyokaiForm.getHiritu5()!=null){
							cellHd[13].setCellValue(torihikisakiGaiyoSyokaiForm.getHiritu5());
							// 課題No179
							// 追加開始
							//cellHd[14].setCellValue("%");
							// 追加完了
						}
						
						if(hikiatekinHanteiSyokaiForm.getHudosan_h() != null ){
							cellHd[48].setCellValue(hikiatekinHanteiSyokaiForm.getHudosan_h());
						}
						if(hikiatekinHanteiSyokaiForm.getDosan_h() != null ){
							cellHd[51].setCellValue(hikiatekinHanteiSyokaiForm.getDosan_h());
						}
						if(hikiatekinHanteiSyokaiForm.getHoken_h() != null ){
							cellHd[54].setCellValue(hikiatekinHanteiSyokaiForm.getHoken_h());
						}
						if(hikiatekinHanteiSyokaiForm.getSonota_h() != null ){
							cellHd[57].setCellValue(hikiatekinHanteiSyokaiForm.getSonota_h());
						}
						
						if(hikiatekinHanteiSyokaiForm.getTanki_kashitsukekin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getTanki_kashitsukekin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getTanki_kashitsukekin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getTanki_kashitsukekin())));
						}
						// 差入保証金
					}else if(i == 28){
						
						if(hikiatekinHanteiSyokaiForm.getSashiire_hosyokin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getSashiire_hosyokin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getSashiire_hosyokin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getSashiire_hosyokin())));
						}
						
						// 弁済条件緩和、その他内容(登録区分50)、仮払金
					}else if(i == 29){
						
						if(torihikisakiKubunHanteiSyokaiForm.getKanwa_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getKanwa_chk())==true){
								if("Ja".equals(lang_mode)==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						
						/* コメントアウト 2009/08/20
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if(torihikisakiGaiyoSyokaiForm.getKansan_tuka_cd()!=null){
							if ("Ja".equals(lang_mode)) {
								if("0280".equals(torihikisakiGaiyoSyokaiForm.getKansan_tuka_cd()) == true){
									cellHd[14].setCellValue("単位：百万円");
								}else{
									cellHd[14].setCellValue("単位：千");
								}
							} else {
								if("0280".equals(torihikisakiGaiyoSyokaiForm.getKansan_tuka_cd()) == true){
									cellHd[14].setCellValue("Unit：Million");
								}else{
									cellHd[14].setCellValue("Unit：Thousand");
								}
							}
						}else{
							if ("Ja".equals(lang_mode)) {
								cellHd[14].setCellValue("単位：");
							} else {
								cellHd[14].setCellValue("Unit：");
							}
						}
						*/
						
						// 通貨(表示単位)の追加　2009/8/20
						cnt = 0;
						String tuuka_hyoujitani = GS.EMPTY_CHARCTER;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							
							if(!Function.trim((String)map.get("ISO_CURRENCY_NM")).equals(GS.EMPTY_CHARCTER) && !Function.trim((String)map.get("hyouji_tani")).equals(GS.EMPTY_CHARCTER)){
								tuuka_hyoujitani = Function.trim((String)map.get("ISO_CURRENCY_NM")) + GS.KAKKO_HIDARI + Function.trim((String)map.get("hyouji_tani")) + GS.KAKKO_MIGI;
							} else {
								tuuka_hyoujitani = GS.EMPTY_CHARCTER;
							}
							
							if(cnt == 0){
								cellHd[6].setCellValue(tuuka_hyoujitani);
							}else if(cnt == 1){
								cellHd[9].setCellValue(tuuka_hyoujitani);
							}else{
								cellHd[12].setCellValue(tuuka_hyoujitani);
							}
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getKaribaraikin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getKaribaraikin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getKaribaraikin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getKaribaraikin())));
						}
						
						if (hikiatekinHanteiSyokaiForm.getSonota_naiyo() != null) {
							cellHd[48].setCellValue(hikiatekinHanteiSyokaiForm.getSonota_naiyo().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 決算期、長期貸付金
					}else if(i == 30){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							//課題No.233
							//修正開始
							if(cnt == 0){
								//cellHd[6].setCellValue((String)map.get("kessanki"));
								cellHd[6].setCellValue((String)map.get("kessan_ki"));
							}else if(cnt == 1){
								//cellHd[9].setCellValue((String)map.get("kessanki"));
								cellHd[9].setCellValue((String)map.get("kessan_ki"));
							}else{
								//cellHd[12].setCellValue((String)map.get("kessanki"));
								cellHd[12].setCellValue((String)map.get("kessan_ki"));
							}
							//修正完了
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getTyoki_kashitsukekin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getTyoki_kashitsukekin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getTyoki_kashitsukekin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getTyoki_kashitsukekin())));
						}
						
						
						// 売上高、その他投資
					}else if(i == 31){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("uriagedaka"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("uriagedaka"));
							}else{
								cellHd[12].setCellValue((String)map.get("uriagedaka"));
							}
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getSonota_toshi() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getSonota_toshi()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getSonota_toshi());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getSonota_toshi())));
						}
						
						// 売上総利益
					}else if(i == 32){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("uriagesorieki"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("uriagesorieki"));
							}else{
								cellHd[12].setCellValue((String)map.get("uriagesorieki"));
							}
							cnt++;
						}
						
						// １年以上延滞、販売管理費、固定化営業債権
					}else if(i == 33){
						
						if(torihikisakiKubunHanteiSyokaiForm.getEntai_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getEntai_chk())==true){
								if("Ja".equals(lang_mode)==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("hanbaihikanrihi"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("hanbaihikanrihi"));
							}else{
								cellHd[12].setCellValue((String)map.get("hanbaihikanrihi"));
							}
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getKoteika_eigyosaiken() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getKoteika_eigyosaiken()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getKoteika_eigyosaiken());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getKoteika_eigyosaiken())));
						}
						
						// 営業利益、その他回収の内容(登録区分60)
					}else if(i == 34){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("eigyorieki"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("eigyorieki"));
							}else{
								cellHd[12].setCellValue((String)map.get("eigyorieki"));
							}
							cnt++;
						}
						
						if (hikiatekinHanteiSyokaiForm.getSonota_kaisyu_naiyo() != null) {
							cellHd[44].setCellValue(hikiatekinHanteiSyokaiForm.getSonota_kaisyu_naiyo().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 経常利益
					}else if(i == 35){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("keijorieki"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("keijorieki"));
							}else{
								cellHd[12].setCellValue((String)map.get("keijorieki"));
							}
							cnt++;
						}
						
						// 留保債務、特別利益
					}else if(i == 36){
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getRyuhosaimu());
						if (hikiatekinHanteiSyokaiForm.getRyuhosaimu() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getRyuhosaimu()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getRyuhosaimu())));
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("toku_rieki"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("toku_rieki"));
							}else{
								cellHd[12].setCellValue((String)map.get("toku_rieki"));
							}
							cnt++;
						}
						// 貸倒懸念債権(登録区分20)
						
						if (torihikisakiKubunHanteiSyokaiForm.getComment_val_20() != null) {
							if(chk2 == 1){
								cellHd[16].setCellValue(torihikisakiKubunHanteiSyokaiForm.getComment_val_20().replaceAll("\r\n","\n").replaceAll("\t"," "));
							}
						}
						
						// 第三者留保債務、特別損失
					}else if(i == 37){
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getOth_ryuhosaimu());
						if (hikiatekinHanteiSyokaiForm.getOth_ryuhosaimu() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getOth_ryuhosaimu()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getOth_ryuhosaimu())));
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("toku_sonsitu"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("toku_sonsitu"));
							}else{
								cellHd[12].setCellValue((String)map.get("toku_sonsitu"));
							}
							cnt++;
						}
						
						// 当期純利益
					}else if(i == 38){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("tokijunrieki"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("tokijunrieki"));
							}else{
								cellHd[12].setCellValue((String)map.get("tokijunrieki"));
							}
							cnt++;
						}
						
						// 保全、配当金
					}else if(i == 39){
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getHozen());
						if (hikiatekinHanteiSyokaiForm.getHozen() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getHozen()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getHozen())));
						}
						
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("haitokin"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("haitokin"));
							}else{
								cellHd[12].setCellValue((String)map.get("haitokin"));
							}
							cnt++;
						}
						
						// その他回収、減価償却費、履行請求懸念の内容説明(登録区分70)
					}else if(i == 40){
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getSonotakaisyu());
						if (hikiatekinHanteiSyokaiForm.getSonotakaisyu() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getSonotakaisyu()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getSonotakaisyu())));
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("genkasyokyakuhi"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("genkasyokyakuhi"));
							}else{
								cellHd[12].setCellValue((String)map.get("genkasyokyakuhi"));
							}
							cnt++;
						}
						
						
						if (hikiatekinHanteiSyokaiForm.getRikoseikyu_kenen_naiyo() != null) {
							cellHd[44].setCellValue(hikiatekinHanteiSyokaiForm.getRikoseikyu_kenen_naiyo().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 破産更生先、営業ｷｬｯｼｭﾌﾛｰ
					}else if(i == 41){
						
						if(torihikisakiKubunHanteiSyokaiForm.getHasanho_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getKaishaho_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getKoseho_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getSaiseho_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getShobun_chk() != null 
								&& torihikisakiKubunHanteiSyokaiForm.getSonota_chk() != null){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getHasanho_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getKaishaho_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getKoseho_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getSaiseho_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getShobun_chk())==true 
									|| "1".equals(torihikisakiKubunHanteiSyokaiForm.getSonota_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[16].setCellValue("○");
								}else{
									cellHd[16].setCellValue("Y");
								}
								chk3 = 1;
							}
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("eigyo_cf"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("eigyo_cf"));
							}else{
								cellHd[12].setCellValue((String)map.get("eigyo_cf"));
							}
							cnt++;
						}
						
						// 流動資産、保証債務合計
					}else if(i == 42){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("ryudosisan"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("ryudosisan"));
							}else{
								cellHd[12].setCellValue((String)map.get("ryudosisan"));
							}
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getHosyosaimu_gokei() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getHosyosaimu_gokei()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getHosyosaimu_gokei());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getHosyosaimu_gokei())));
						}
						
						// 履行請求懸念、 固定資産
					}else if(i == 43){
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getRiko_kenen());
						if (hikiatekinHanteiSyokaiForm.getRiko_kenen() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getRiko_kenen()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getRiko_kenen())));
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("koteisisan"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("koteisisan"));
							}else{
								cellHd[12].setCellValue((String)map.get("koteisisan"));
							}
							cnt++;
						}
						
						
						// 破産法適用、資産合計
					}else if(i == 44){
						
						if(torihikisakiKubunHanteiSyokaiForm.getHasanho_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getHasanho_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("sisangokei"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("sisangokei"));
							}else{
								cellHd[12].setCellValue((String)map.get("sisangokei"));
							}
							cnt++;
						}
						
						// 流動負債、既引当金⑥
					}else if(i == 45){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("ryudohusai"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("ryudohusai"));
							}else{
								cellHd[12].setCellValue((String)map.get("ryudohusai"));
							}
							cnt++;
						}
						
						if(hikiatekinHanteiSyokaiForm.getKibikiatekin() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getKibikiatekin()).length() > 0){
							//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getKibikiatekin());
							cellHd[40].setCellValue(Function.getValueOfDouble( Function.removeComma(hikiatekinHanteiSyokaiForm.getKibikiatekin())));
						}
						
						// 会社法適用、固定負債、引当金算定根拠(登録区分80)
					}else if(i == 46){
						
						if(torihikisakiKubunHanteiSyokaiForm.getKaishaho_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getKaishaho_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("koteihusai"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("koteihusai"));
							}else{
								cellHd[12].setCellValue((String)map.get("koteihusai"));
							}
							cnt++;
						}
						
						if (hikiatekinHanteiSyokaiForm.getHikiatekin_konkyo_naiyo() != null) {
							cellHd[44].setCellValue(hikiatekinHanteiSyokaiForm.getHikiatekin_konkyo_naiyo().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 負債合計
					}else if(i == 47){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("husaigokei"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("husaigokei"));
							}else{
								cellHd[12].setCellValue((String)map.get("husaigokei"));
							}
							cnt++;
						}
						
						// 会社更生法適用、追加引当金、 資本金
					}else if(i == 48){
						
						if(torihikisakiKubunHanteiSyokaiForm.getKoseho_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getKoseho_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						
						//cellHd[40].setCellValue(hikiatekinHanteiSyokaiForm.getTuika_hikiate());
						if (hikiatekinHanteiSyokaiForm.getTuika_hikiate() != null 
								&& Function.trim(hikiatekinHanteiSyokaiForm.getTuika_hikiate()).length() > 0) {
							cellHd[40].setCellValue(Function.getValueOfLong( Function.removeComma(hikiatekinHanteiSyokaiForm.getTuika_hikiate())));
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("sihonkin"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("sihonkin"));
							}else{
								cellHd[12].setCellValue((String)map.get("sihonkin"));
							}
							cnt++;
						}
						
						// 内部留保等
					}else if(i == 49){
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {
							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("naiburyuho"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("naiburyuho"));
							}else{
								cellHd[12].setCellValue((String)map.get("naiburyuho"));
							}
							cnt++;
						}
						
						
						// 民事再生法適用、自己資本合計
					}else if(i == 50){
						
						if(torihikisakiKubunHanteiSyokaiForm.getSaiseho_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getSaiseho_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						//rs3.beforeFirst();
						cnt = 0;
						
						Iterator itr = zaimuList.iterator();
						while ( itr.hasNext() ) {

							HashMap map = (HashMap)itr.next();
							if(cnt == 0){
								cellHd[6].setCellValue((String)map.get("jikosihongokei"));
							}else if(cnt == 1){
								cellHd[9].setCellValue((String)map.get("jikosihongokei"));
							}else{
								cellHd[12].setCellValue((String)map.get("jikosihongokei"));
							}
							cnt++;
						}
						
						// 取引停止処分、外部格付、今後の回収見通し等(登録区分90)
					}else if(i == 52){
						
						if(torihikisakiKubunHanteiSyokaiForm.getShobun_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getShobun_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}

						cellHd[6].setCellValue(torihikisakiGaiyoSyokaiForm.getKtk_kikan());
						
						if (hikiatekinHanteiSyokaiForm.getKaisyu_naiyo() != null) {
							cellHd[32].setCellValue(hikiatekinHanteiSyokaiForm.getKaisyu_naiyo().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 外部信用情報
					}else if(i == 53){
						
						cellHd[6].setCellValue(torihikisakiGaiyoSyokaiForm.getGaibu_ktk());
						cellHd[9].setCellValue(torihikisakiGaiyoSyokaiForm.getFss());
						cellHd[12].setCellValue(torihikisakiGaiyoSyokaiForm.getDuns_rating());
						
						
						// その他
					}else if(i == 54){
						
						if(torihikisakiKubunHanteiSyokaiForm.getSonota_chk() != null ){
							if("1".equals(torihikisakiKubunHanteiSyokaiForm.getSonota_chk())==true){
								if(lang_mode.equals("Ja")==true){
									cellHd[17].setCellValue("○");
								}else{
									cellHd[17].setCellValue("Y");
								}
							}
						}
						
						// 決算概況(登録区分10)
					}else if(i == 56){
						
						if (torihikisakiGaiyoSyokaiForm.getComment_val() != null) {
							cellHd[1].setCellValue(torihikisakiGaiyoSyokaiForm.getComment_val().replaceAll("\r\n","\n").replaceAll("\t"," "));
						}
						
						// 破産更生債権(登録区分20)
					}else if(i == 57){
						
						if (torihikisakiKubunHanteiSyokaiForm.getComment_val_20() != null) {
							if(chk3 == 1){
								cellHd[16].setCellValue(torihikisakiKubunHanteiSyokaiForm.getComment_val_20().replaceAll("\r\n","\n").replaceAll("\t"," "));
							}
						}
						
					}
				}
			} finally {
				if (rs1!=null) {
					rs1.close();
				}
				if(rs8!=null) {
					rs8.close();
				}
			}
			
			try {
				// No219, 2008/05/31, SJA渡辺, 金額を四つ出すように修正
				// 債権残高合計
				double saiken_zankei=0;				
				// 保証債務合計
				double hosyo_kei = 0;				
				// 引当金合計
				double hikiate_kei = 0;

				if(saikenMeisaiList != null && saikenMeisaiList.size() != 0){
					Iterator itr = saikenMeisaiList.iterator();
					while(itr.hasNext()){
						HashMap map = (HashMap)itr.next();
						// 債権フラグを取得。
						String saiken_flg_value = (String)map.get("saiken_flg");
						if(	"1".equals(saiken_flg_value) ||
							    "2".equals(saiken_flg_value)){
								// 債権残高合計＝リストに表示されている一般債権、固定化営業債権(債権フラグ='1'、'2')の合計
								saiken_zankei += Function.getValueOfLongC((String)map.get("kingaku"));
						}else if("3".equals(saiken_flg_value)){
							// 保証債務合計＝リストに表示されている保証債務(債権フラグ='3')の合計
							hosyo_kei += Function.getValueOfLongC((String)map.get("kingaku"));
						}else if("9".equals(saiken_flg_value)){
							// 引当金合計＝リストに表示されている引当金(債権フラグ='9')の合計
							hikiate_kei += Function.getValueOfLongC((String)map.get("kingaku"));
						}
					}
				}
				
				StringBuffer sql4 = new StringBuffer()
				.append("SELECT DISTINCT ")
				.append("SKM.ktk, ")
				.append("MS.satei_kaisya_nm, ")
				.append("MS.satei_kaisya_nm_e ")
				.append(" FROM ")
				.append("SST_SATEI_STAT SS LEFT JOIN ")
				.append("SSE_TAIHI TT ON ")
				.append("SS.ym = TT.ym ")
				.append("AND SS.mise_cd = TT.office_cd ")
				.append("AND TRIM(SS.kikan_tori_cd) || '00' = TT.kikan_tori_cd ")
				.append("AND SS.system_kbn = TT.system_kbn LEFT JOIN ")
				.append("SSE_KTK SKM ON ")
				.append("TT.ym = SKM.ym ")
				.append("AND TT.sikibetu_cd = SKM.sikibetu_cd ")
				.append("AND TT.togo_tori_cd = SKM.duns_no ")
				.append("AND TT.syori_kaisu = SKM.syori_kaisu LEFT JOIN ")
				.append("SSM_SATEIKAISYA MS ON ")
				.append("TRIM(SS.satei_kaisha_cd) = TRIM(MS.satei_kaisha_cd) ")
				// 障害票No349　2008/05/15　細野　会社コードを条件に追加
				.append("AND SS.kaisha7_cd = MS.kaisha_cd ")
				.append("WHERE SS.anken_no = '")
				.append(anken_no)
				.append("'");
				
				
				StringBuffer sql2 = new StringBuffer()
				.append("SELECT ")
				.append("TO_CHAR(MAX(syori_dt),'yyyymmdd') AS syori_dt ")
				.append("FROM ")
				.append("SST_NYURYOKU_HIST ")
				.append("WHERE ")
				.append("anken_no ='")
				.append(anken_no)
				.append("' ")
				.append("AND phase = '")
				.append(phase)
				.append("' ")
				.append("AND ope_kbn = '80'");
				
				StringBuffer sql6 = new StringBuffer()
				.append("SELECT kbn_hyouji_val ")
				.append("FROM SSP_KBN ")
				.append("WHERE kbn_key = 'tairyu_jdg' ")
				.append("AND kbn_val = '1' ")
				.append("AND system_kbn ='")
				.append(cmnData.getSystem_kbn())
				.append("' AND lang_mode = '")
				.append(cmnData.getComLangMode())
				.append("'");
				
				rs4 = sqlExec.execQuery(sql4.toString());
				// SQL実行		
				rs2 = sqlExec.execQuery(sql2.toString());
				
				rs6 = sqlExec.execQuery(sql6.toString());
				
				if ( rs4.next() ) {
					for (int i=0; i<10; i++ ) {
						HSSFRow row1 = sheetW1.getRow(i);
						if(row1 != null){
							// 課題No.192
							// 追加開始
							//for (int j=0; j<19; j++ ) {
							for (int j=0; j<20; j++ ) {
							// 追加完了
								cellHd1[j] = row1.getCell((short)j);
                                /*
								if(cellHd1[j] != null){
									cellHd1[j].setEncoding(HSSFCell.ENCODING_UTF_16);
								}
                                */
							}
						}
						
						// 日付行
						if(i == 0){
								if(mode == 0){
									if(GS.STATUS_KANRYO.equals(cmnData.getStatus()) && rs2.next() && !GS.EMPTY_CHARCTER.equals(Function.trim(rs2.getString("syori_dt")))){
										//ステータスが承認のみ表示
										String yyyymm = Function.insertYmNengetu(rs2.getString("syori_dt").substring(0,6));
										String dd = rs2.getString("syori_dt").substring(6,8);
										
										// 課題No.158
										// 追加開始
										//String yyyymmdd = Function.insertDateSlash(rs2.getString("syori_dt").substring(0,8));
										String yyyymmdd = Function.insertDateSlash(rs2.getString("syori_dt").substring(0,8),lang_mode);
										// 追加完了
										
										// 照会の場合
										// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
										if ("Ja".equals(lang_mode)) {
											cellHd1[18].setCellValue( yyyymm + dd + "日" + "　承認");
										} else {
											
											// 課題No.117 
											// 追加開始
											//cellHd1[18].setCellValue( yyyymmdd + "　Approve");
											cellHd1[18].setCellValue( yyyymmdd + "  Approve");
											// 追加完了
										}
									}else{
										// 承認以外は空白を挿入
										cellHd1[18].setCellValue("");
									}
								}else{
									String sysdate = sdfYMD.format(new Date());
									yyyymm = Function.insertYmNengetu(sysdate.substring(0,6));
									dd = sysdate.substring(6,8);
									// 課題No.158
									// 追加開始
									//String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8));
									String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8),lang_mode);
									// 追加完了
									// 引当金判定の場合
									// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
									if ("Ja".equals(lang_mode)) {
										cellHd1[18].setCellValue( yyyymm + dd + "日" + "　作成");
									} else {
										
										// 課題No.117 
										// 追加開始
										//cellHd1[18].setCellValue( yyyymmdd + "　Creation");
										cellHd1[18].setCellValue( yyyymmdd + "  Creation");
										// 追加完了
									}
								}
							
							// 査定会社名
						}else if(i == 1){
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								cellHd1[18].setCellValue(rs4.getString("satei_kaisya_nm"));
							} else {
								cellHd1[18].setCellValue(rs4.getString("satei_kaisya_nm_e"));
							}
							
							// タイトル行	
						}else if(i == 3){
							// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								cellHd1[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(ym)+ "末基準)");
							} else {
								// 課題No.158
								// 追加開始
								//cellHd1[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym)+ ")");
								cellHd1[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
								// 追加完了
							}
							
							// 勘定先CD、格付、フェーズ行
						}else if(i == 4){
							if("".equals(kikan_tori_cd)){
								
							}else{
								cellHd1[1].setCellValue(kikan_tori_cd);
								cellHd1[5].setCellValue(rs4.getString("ktk"));
							}
							
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(saiken_zankei);
							
							//	            	if(mode == 0){
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("＜対象先選定結果＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("＜クレーム債権再設定結果＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("＜一次査定結果＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("＜一次査定検証結果＞");
									}else{
										cellHd1[18].setCellValue("＜二次査定結果＞");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("＜対象先選定中＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("＜クレーム債権再設定中＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("＜一次査定中＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("＜一次査定検証中＞");
									}else{
										cellHd1[18].setCellValue("＜二次査定中＞");
									}
								}
							} else {
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("<Select Customer Result>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("<Judgment of Claim Debt Result>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("<Primary Assessment Result>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("<Primary Assessment Verification Result>");
									}else{
										cellHd1[18].setCellValue("<Secondary Assessment Result>");
									}									
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("<Select Customer Processing>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("<Judgment of Claim Debt Processing>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("<Primary Assessment Processing>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("<Primary Assessment Verification Processing>");
									}else{
										cellHd1[18].setCellValue("<Secondary Assessment Processing>");
									}
								}
							}
							
							
							
							// 勘定先名行
						}else if(i == 5){
							if("".equals(torihikisakimei)){
								
							}else{
								
								cellHd1[1].setCellValue(torihikisakimei);
							}
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(hosyo_kei);
						} else if (i == 6) {
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(hikiate_kei);

						} else if (i == 7) {
							// No828, 2008/06/12, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
							if (rs6.next()) {
								
								// 課題No.192
								// 追加開始
								//cellHd1[18].setCellValue(Function.trim(rs6.getString("kbn_hyouji_val")));
								cellHd1[19].setCellValue(Function.trim(rs6.getString("kbn_hyouji_val")));
								// 追加完了
							}
						}
					}
				} else {
					for (int i=0; i<10; i++ ) {
						HSSFRow row1 = sheetW1.getRow(i);
						if(row1 != null){
							
							for (int j=0; j<19; j++ ) {
								cellHd1[j] = row1.getCell((short)j);
                                /*
								if(cellHd1[j] != null){
									cellHd1[j].setEncoding(HSSFCell.ENCODING_UTF_16);
								}
                                */
							}
						}
						
						// 日付行
						if(i == 0){
							cellHd1[18].setCellValue("");
							
							// 査定会社名
						}else if(i == 1){
							cellHd1[18].setCellValue("");
							
							// タイトル行	
						}else if(i == 3){
							// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								cellHd1[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(ym)+ "末基準)");
							} else {
								// 課題No.158
								// 追加開始
								//cellHd1[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym)+ ")");
								cellHd1[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
								// 追加完了
							}
							
							// 勘定先CD、格付、フェーズ行
						}else if(i == 4){
							if("".equals(kikan_tori_cd)){
								
							}else{
								cellHd1[1].setCellValue(kikan_tori_cd);
								cellHd1[5].setCellValue("");
							}
							
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(saiken_zankei);
							
							//	            	if(mode == 0){
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("＜対象先選定結果＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("＜クレーム債権再設定結果＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("＜一次査定結果＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("＜一次査定検証結果＞");
									}else{
										cellHd1[18].setCellValue("＜二次査定結果＞");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("＜対象先選定中＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("＜クレーム債権再設定中＞");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("＜一次査定中＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("＜一次査定検証中＞");
									}else{
										cellHd1[18].setCellValue("＜二次査定中＞");
									}
								}
							} else {
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("<Select Customer Result>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("<Judgment of Claim Debt Result>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("<Primary Assessment Result>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("<Primary Assessment Verification Result>");
									}else{
										cellHd1[18].setCellValue("<Secondary Assessment Result>");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd1[18].setCellValue("<Select Customer Processing>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd1[18].setCellValue("<Judgment of Claim Debt Processing>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd1[18].setCellValue("<Primary Assessment Processing>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd1[18].setCellValue("<Primary Assessment Verification Processing>");
									}else{
										cellHd1[18].setCellValue("<Secondary Assessment Processing>");
									}
								}
							}

							// 勘定先名行
						}else if(i == 5){
							if("".equals(torihikisakimei)){
								
							}else{
								cellHd1[1].setCellValue(torihikisakimei);
							}
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(hosyo_kei);
						} else if (i == 6) {
							// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
							cellHd1[17].setCellValue(hikiate_kei);
	
						} else if (i == 7) {
							// No828, 2008/06/12, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
							if (rs6.next()) {
								cellHd1[18].setCellValue(Function.trim(rs6.getString("kbn_hyouji_val")));
							}
						}
					}
				}
				
				// 明細部分の作成(10行目から)
				int j = 10;
				
				//rs1.beforeFirst();
				HSSFRow rowM[] = new HSSFRow[saikenMeisaiList.size() + 10];
				
				// 課題No.27
				// 追加開始
				HSSFCellStyle[] Style1 = new HSSFCellStyle[19];
				for(int i =0; i<=18; i++) {
					Style1[i] = sheetW1.getRow(10).getCell((short)i).getCellStyle();
				}
/*
				HSSFCellStyle style1 = wb.createCellStyle();
				style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// 障害票No:348③
				// チェックイン日：2008/05/14
				// 修正者：細野
				// フォントサイズ不正の改修
				HSSFFont font1 = wb.createFont();
				font1.setFontHeightInPoints((short)10);
				font1.setFontName("ＭＳ Ｐゴシック");
				style1.setFont(font1);
				// No483, 2008/05/30, SJA中島, セル内改行を許可するように修正
				style1.setWrapText(true);
				// ここまで
				
				HSSFCellStyle style2 = wb.createCellStyle();
				style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
				style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
				// 障害票No:348③
				// チェックイン日：2008/05/14
				// 修正者：細野
				// フォントサイズ不正および数値カンマ編集の改修
				HSSFFont font2 = wb.createFont();
				font2.setFontHeightInPoints((short)10);
				font2.setFontName("ＭＳ Ｐゴシック");
				style2.setFont(font2);
				style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
				// ここまで
				// 追加完了
*/				
				if (saikenMeisaiList != null) {
					Iterator itr = saikenMeisaiList.iterator();
					while (itr.hasNext()) {
						HashMap map = (HashMap)itr.next();
						// j行目の作成
						rowM[j] = sheetW1.createRow(j);
						
						HSSFCell[] cellData = new HSSFCell[100];
						
						for (int i=0; i<19; i++ ) {
							cellData[i] = rowM[j].createCell((short)i);
							// cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
							
							// 課題No.27
							// 追加開始
							cellData[i].setCellStyle(Style1[i]);
/*							
							if(i == 17){
								cellData[i].setCellStyle(style2);
							}else{
								cellData[i].setCellStyle(style1);
							}
*/							
							// 追加完了
						}
						
						cellData[0].setCellValue( (String)map.get("tori_cd_7") );	// 勘定先７桁
						if(lang_mode.equals("Ja")==true){
							if(map.get("torihikisakimei2") == null){
								cellData[1].setCellValue( (String)map.get("torihikisakimei_en2") );	// 勘定先名称
							}else{
								cellData[1].setCellValue( (String)map.get("torihikisakimei2") );	// 勘定先名称
							}
						}else{
							cellData[1].setCellValue( (String)map.get("torihikisakimei_en2") );	// 勘定先名称
						}
						cellData[2].setCellValue( (String)map.get("seisiki_bumon_cd") );	// 部門
						cellData[3].setCellValue( (String)map.get("bu_cd") );				// 部課コード
						cellData[4].setCellValue( (String)map.get("bu_nm") );				// 部課名称
						cellData[5].setCellValue( (String)map.get("cell_cd") );			// セルCD
						cellData[6].setCellValue( (String)map.get("cell_nm") );				// セル
						cellData[7].setCellValue( (String)map.get("kanjo_cd") );			// 勘定科目CD
						cellData[8].setCellValue( (String)map.get("kanjo_nm") );			// 勘定科目名称
						cellData[9].setCellValue( (String)map.get("kanjo_uchi_cd") );		// 内分類
						cellData[10].setCellValue( (String)map.get("kanjo_uchi_nm") );	// 内分類名称
						cellData[11].setCellValue( (String)map.get("shusi_dt") );			// 収支予定日
						cellData[12].setCellValue( (String)map.get("manki_dt") );			// 満期日
						cellData[13].setCellValue( (String)map.get("syori_dt") );			// 勘定処理日
						cellData[14].setCellValue( (String)map.get("tairyu_kbn") );		// 滞留区分
						cellData[15].setCellValue( (String)map.get("tairyu_jdg") );		// 滞留判定
						cellData[16].setCellValue( (String)map.get("keiyaku_denpyo_no") );// 契約No.
						cellData[17].setCellValue( Function.getValueOfLong(Function.removeComma((String)map.get("kingaku"))) );			// 金額(円)
						if ((String)map.get("hantei_jiyuu") != null) {
							cellData[18].setCellValue( ((String)map.get("hantei_jiyuu")).replaceAll("\r\n","\n").replaceAll("\t"," ") );		// 判定事由
						}
						j++;
					}
				}
			} finally {
				if (rs4!=null) {
					rs4.close();
				}
				if(rs2!=null) {
					rs2.close();
				}
				if(rs6!=null) {
					rs6.close();
				}
			}
			if (!ryuhoFlag) {
				try {
					StringBuffer sql = new StringBuffer()
					.append("SELECT ")
					.append("TO_CHAR(MAX(syori_dt),'yyyymmdd') AS syori_dt ")
					.append("FROM ")
					.append("SST_NYURYOKU_HIST ")
					.append("WHERE ")
					.append("anken_no ='")
					.append(anken_no)
					.append("' ")
					.append("AND phase = '")
					.append(phase)
					.append("' ")
					.append("AND ope_kbn = '80'");
					
					StringBuffer sql5 = new StringBuffer()
					.append("SELECT DISTINCT ")
					.append("MS.satei_kaisya_nm, ")
					.append("MS.satei_kaisya_nm_e ")
					.append(" FROM ")
					.append("SST_SATEI_STAT SS LEFT JOIN ")
					.append("SSM_SATEIKAISYA MS ON ")
					.append("TRIM(SS.satei_kaisha_cd) = TRIM(MS.satei_kaisha_cd) ")
					.append("AND SS.kaisha7_cd = MS.kaisha_cd ")
					.append("WHERE SS.anken_no = '")
					.append(anken_no)
					.append("'");
					
					rs = sqlExec.execQuery(sql.toString());
					rs5 = sqlExec.execQuery(sql5.toString());
					
					for (int i=0; i<8; i++ ) {
						
						HSSFRow row3 = sheetW2.getRow(i);
						if(row3 != null){
							for (int j=0; j<19; j++ ) {
								cellHd2[j] = row3.getCell((short)j);
                                /*
								if(cellHd2[j] != null){
									cellHd2[j].setEncoding(HSSFCell.ENCODING_UTF_16);
								}
                                */
							}
						}
						
						// 日付行
						if(i == 0){
								if(mode == 0){
									if(GS.STATUS_KANRYO.equals(cmnData.getStatus()) && rs.next() && !GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString("syori_dt")))){
										//ステータスが承認のみ表示
										String yyyymm = Function.insertYmNengetu(rs.getString("syori_dt").substring(0,6));
										String dd = rs.getString("syori_dt").substring(6,8);
										
										// 課題No.158
										// 追加開始
										//String yyyymmdd = Function.insertDateSlash(rs.getString("syori_dt").substring(0,8));
										String yyyymmdd = Function.insertDateSlash(rs.getString("syori_dt").substring(0,8),lang_mode);
										// 追加完了
										
										// 照会の場合
										// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
										if ("Ja".equals(lang_mode)) {
											cellHd2[15].setCellValue( yyyymm + dd + "日" + "　承認");
										} else {
											
											// 課題No.117 
											// 追加開始
											//cellHd2[15].setCellValue( yyyymmdd + "　Approve");
											cellHd2[15].setCellValue( yyyymmdd + "  Approve");
											// 追加完了
										}
									}else{
										// 承認以外は空白を挿入
										cellHd2[15].setCellValue("");
									}
								}else{
									String sysdate = sdfYMD.format(new Date());
									yyyymm = Function.insertYmNengetu(sysdate.substring(0,6));
									dd = sysdate.substring(6,8);
									// 課題No.158
									// 追加開始
									//String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8));
									String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8),lang_mode);
									// 追加完了
									// 引当金判定の場合
									// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
									if ("Ja".equals(lang_mode)) {
										cellHd2[15].setCellValue( yyyymm + dd + "日" + "　作成");
									} else {
										
										// 課題No.117 
										// 追加開始
										//cellHd2[15].setCellValue( yyyymmdd + "　Creation");
										cellHd2[15].setCellValue( yyyymmdd + "  Creation");
										// 追加完了
									}
								}
							// 査定会社名
						}else if(i == 1){
							//								cellHd2[15].setCellValue(rs1.getString("satei_kaisya_nm"));
							//cellHd2[15].setCellValue((String)map1.get("satei_kaisya_nm"));
							if (rs5.next()) {
								// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
								if ("Ja".equals(lang_mode)) {
									cellHd2[15].setCellValue(rs5.getString("satei_kaisya_nm"));
								} else {
									cellHd2[15].setCellValue(rs5.getString("satei_kaisya_nm_e"));
								}
							} else {
								cellHd2[15].setCellValue("");
							}
							
							// タイトル行	
						}else if(i == 2){
							//								cellHd2[0].setCellValue("債務明細一覧(" + Function.insertYmNengetu(rs5.getString("ym"))+ "末基準)");
							// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								cellHd2[0].setCellValue("債務明細一覧(" + Function.insertYmNengetu(ym) + "末基準)");
							} else {
								// 課題No158
								// 追加開始
								cellHd2[0].setCellValue("Debt Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode) + ")");
								//cellHd2[0].setCellValue("Debt Listing (On the basis of " + Function.insertYmSlash(ym) + ")");
								// 追加完了
							}
							// 勘定先CD、格付、フェーズ行
						}else if(i == 3){
							//								cellHd2[1].setCellValue(rs1.getString("kikan_tori_cd"));
							cellHd2[1].setCellValue(kikan_tori_cd);
							//		 	            		if(mode == 0){
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd2[15].setCellValue("＜対象先選定結果＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd2[15].setCellValue("＜クレーム債権再設定結果＞");
									}else if(phase.equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd2[15].setCellValue("＜一次査定結果＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if(phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd2[15].setCellValue("＜一次査定検証結果＞");
									}else{
										cellHd2[15].setCellValue("＜二次査定結果＞");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd2[15].setCellValue("＜対象先選定中＞");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd2[15].setCellValue("＜クレーム債権再設定中＞");
									}else if(phase.equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd2[15].setCellValue("＜一次査定中＞");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if(phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd2[15].setCellValue("＜一次査定検証中＞");
									}else{
										cellHd2[15].setCellValue("＜二次査定中＞");
									}
								}
							} else {
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd2[15].setCellValue("<Select Customer Result>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd2[15].setCellValue("<Judgment of Claim Debt Result>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd2[15].setCellValue("<Primary Assessment Result>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd2[15].setCellValue("<Primary Assessment Verification Result>");
									}else{
										cellHd2[15].setCellValue("<Secondary Assessment Result>");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd2[15].setCellValue("<Select Customer Processing>");
									}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
										cellHd2[15].setCellValue("<Judgment of Claim Debt Processing>");
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
										cellHd2[15].setCellValue("<Primary Assessment Processing>");
										// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
									}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
										cellHd2[15].setCellValue("<Primary Assessment Verification Processing>");
									}else{
										cellHd2[15].setCellValue("<Secondary Assessment Processing>");
									}
								}
							}
							
							// 勘定先名行
						}else if(i == 4){
							
							cellHd2[1].setCellValue(torihikisakimei);
						}
					}
					//}
					
					// 明細部分の作成(8行目から)
					int j = 7;
					
					HSSFRow rowM2[] = new HSSFRow[ryuhoSaimuList.size() + 7];
					
					// 課題No.27 国内フォント統一対応
					// 追加開始
					HSSFCellStyle[] Style2 = new HSSFCellStyle[16];
					for(int i =0; i<=15; i++) {
						Style2[i] = sheetW2.getRow(7).getCell((short)i).getCellStyle();
					}
	/*
					HSSFCellStyle style3 = wb.createCellStyle();
					style3.setBorderTop(HSSFCellStyle.BORDER_THIN);
					style3.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					style3.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					style3.setBorderRight(HSSFCellStyle.BORDER_THIN);
					// 障害票No:370③
					// チェックイン日：2008/05/15
					// 修正者：細野
					// フォントサイズ不正の改修
					HSSFFont font3 = wb.createFont();
					font3.setFontHeightInPoints((short)10);
					font3.setFontName("ＭＳ Ｐゴシック");
					style3.setFont(font3);
					// No483, 2008/05/30, SJA中島, セル内改行を許可するように修正
					style3.setWrapText(true);
					// ここまで			
					HSSFCellStyle style4 = wb.createCellStyle();
					style4.setBorderTop(HSSFCellStyle.BORDER_THIN);
					style4.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					style4.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					style4.setBorderRight(HSSFCellStyle.BORDER_THIN);
					// 障害票No:370③
					// チェックイン日：2008/05/15
					// 修正者：細野
					// フォントサイズ不正および数値カンマ編集の改修
					HSSFFont font4 = wb.createFont();
					font4.setFontHeightInPoints((short)10);
					font4.setFontName("ＭＳ Ｐゴシック");
					style4.setFont(font4);
					style4.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
					
*/
					// 追加完了
					//				        while ( rs1.next() ) {
					Iterator itr2 = ryuhoSaimuList.iterator();
					while ( itr2.hasNext() ) {
						HashMap map = (HashMap)itr2.next();
						// j行目の作成
						rowM2[j] = sheetW2.createRow(j);
						
						HSSFCell[] cellData = new HSSFCell[100];
						
						for (int i=0; i<16; i++ ) {
							cellData[i] = rowM2[j].createCell((short)i);
							// cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
							
							// 課題No.27
							// 追加開始
							cellData[i].setCellStyle(Style2[i]);
/*							
							cellData[i].setCellValue("ＭＳ Ｐゴシック");
							cellData[i].setCellStyle(style3);
							if(i == 13){
								cellData[i].setCellStyle(style4);
							}else{
								cellData[i].setCellStyle(style3);
							}
*/
							// 追加完了
						}
						
						
						cellData[0].setCellValue( (String)map.get("tori_cd_7") );	// 勘定先７桁
						if("Ja".equals(lang_mode)){
							if( (String)map.get("torihikisakimei2") == null){
								cellData[1].setCellValue( (String)map.get("torihikisakimei_en2") );	// 勘定先名称
							}else{
								cellData[1].setCellValue( (String)map.get("torihikisakimei2") );	// 勘定先名称
							}
						}else{
							cellData[1].setCellValue( (String)map.get("torihikisakimei_en2") );	// 勘定先名称
						}
						cellData[2].setCellValue( (String)map.get("seisiki_bumon_cd") );	// 部門
						// 障害No371 2008.05.16 uechi 課コードを部コードに修正
						cellData[3].setCellValue( (String)map.get("bu_cd") );				// 部課コード
						// 障害No371 2008.05.16 uechi 課名称を部名称に修正
						cellData[4].setCellValue( (String)map.get("bu_nm") );				// 部課名称
						// 障害管理：200808220002 2008/8/22 中島 帳票に出力するセルCDを引当金BS明細のセルコード(組織変更情報のオリジナルセルコードと同等)に変更。
						cellData[5].setCellValue( (String)map.get("cell_cd") );			// セルCD
						cellData[6].setCellValue( (String)map.get("cell_nm") );				// セル
						cellData[7].setCellValue( (String)map.get("kanjo_cd") );			// 勘定科目CD
						cellData[8].setCellValue( (String)map.get("kanjo_nm") );			// 勘定科目名称
						cellData[9].setCellValue( (String)map.get("kanjo_uchi_cd") );		// 内分類
						cellData[10].setCellValue( (String)map.get("kanjo_uchi_nm") );		// 内分類名称
						cellData[11].setCellValue( (String)map.get("shusi_dt") );			// 収支予定日
						cellData[12].setCellValue( (String)map.get("keiyaku_denpyo_no") );	// 契約No.
						cellData[13].setCellValue( Function.getValueOfLong( Function.removeComma((String)map.get("kingaku"))) );			// 金額(円)
						//課題No.171
						//追加開始
						//if( (String)map.get("ryuhosaimu_kbn") !=null ){
							if( (Function.trim((String)map.get("ryuhosaimu_kbn"))).equals("1")==true ){	// 留保債務
								if(lang_mode.equals("Ja")==true){
									cellData[14].setCellValue("○");
								}else{
									cellData[14].setCellValue("Y");
								}
							}else{
								if(lang_mode.equals("Ja")==true){
									cellData[14].setCellValue("×");
								}else{
									cellData[14].setCellValue("N");
								}
							}
						/*
						} else {
							cellData[14].setCellValue("");
						}
						*/
						// 追加完了
						if ((String)map.get("biko") != null) {
							cellData[15].setCellValue( ((String)map.get("biko")).replaceAll("\r\n","\n").replaceAll("\t"," ") );				// 備考
						}
						j++;
						
					}
				} finally {
					if (rs!=null) {
						rs.close();
					}
					if(rs5!=null) {
						rs5.close();
					}
				}
			}
			//作成したワークブックを保存する
			wb.write(fileOut);
			
			fileOut.flush();
			
		} finally {
			// No786, 2008/06/05, SJA渡辺, fileOutがnullチェック追加
			if (fileOut != null) {
				fileOut.close();
			}
			// Resultset close
			if(rs1 != null) {
				rs1.close();
			}
			if(rs8 != null) {
				rs8.close();
			}
			if(rs2 != null) {
				rs2.close();
			}
			if(rs4 != null) {
				rs4.close();
			}
			if(rs != null) {
				rs.close();
			}
			if(rs5 != null) {
				rs5.close();
			}
			if(rs6 != null) {
				rs6.close();
			}
		}
		
		
		// 作成したエクセルファイルのダウンロード用tmpファイルを作成する
		TempFile tmp = new TempFile(fname);
		
		FileInputStream fis = null;
		BufferedInputStream  is = null;
		OutputStream os = null;
		
		int contents = 0;
		
		// 一時ファイルに、エクセル情報を書き込む
		try {
			fis = new FileInputStream(tmpExcel.getAbsolutePath());
			
			is = new BufferedInputStream (fis);
			os = new FileOutputStream(tmp.getPath());
			
			while ((contents = is.read()) != -1){
				os.write(contents);
			}
			os.flush();
			
		} finally {
			if (fis!=null) {
				fis.close();
				fis = null;
			}
			if(is!=null) {
				is.close();
				is = null;
			}
			if(os!=null) {
				os.close();
				os = null;
			}
		}
		
		// エクセルの一時ファイルを削除
		tmpExcel.delete();
		
		// セッションデータにtmpファイルをセット
		HttpServletRequest req = appContext.getRequest();
		HttpServletResponse res = appContext.getResponse();
		
		// リクエストスコープのデータを登録
		req.setAttribute(GS.DOWNLOADCONTEXT,tmp);
		
		// ダウンロード
		AppDownloadAction acc = new AppDownloadAction();
		if(returnId.equals(GS.OS6102)==true){
			SateisyosaiForm form = (SateisyosaiForm)appContext.getActionForm();
			acc.execute( new ActionMapping(), form, req, res );
		}else{
			HikiateForm form = (HikiateForm)appContext.getActionForm();
			acc.execute( new ActionMapping(), form, req, res );
		}
		
		tmp.delete();
	}
	
	/**
	 * 区分名称を取得する
	 */
	private String getKbnNm(String kbn_key, String kbn_val, String langMode) throws SQLException {
		
		ResultSet rs = null;
		
		String result = "";
		
		try {
		
			StringBuffer sql = new StringBuffer().append("SELECT KBN_HYOUJI_VAL")
												.append(" FROM SSP_KBN")
												.append(" WHERE KBN_KEY='")
												.append(kbn_key)
												.append("' and LANG_MODE='")
												.append(langMode)
												.append("' and SYSTEM_KBN ='")
												.append(cmnData.getSystem_kbn())
												.append("' and KBN_VAL='")
												.append(kbn_val).append("'");

			rs = sqlExec.execQuery(sql.toString());
			
			if (rs.next()) {
				result = rs.getString("KBN_HYOUJI_VAL");
			}
			
			return result;
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
}
