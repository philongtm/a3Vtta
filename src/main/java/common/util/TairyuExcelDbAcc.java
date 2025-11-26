/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/05/18		SSC				1.5次版機能組込
002		2009/10/28		SSC				課題No.06 国内一括取込対応
003		2009/11/04		SSC				課題No.27 国内フォント統一対応
004		2009/11/17		SSC				課題No.117 2バイト文字対応
005		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
006		2014/03/14		SSC				案件No.D13493 改善対応
007		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.syokai.form.SateisyosaiForm;
import app.tairyu.form.TorokuForm;
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
import java.util.Date;

/**
* 滞留ExcelDBアクセスクラス
*/
public class TairyuExcelDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;							// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;							// 共通セッションデータ
	private  SimpleDateFormat sdfYMD = null;						// 一時ファイル作成時のフォーマット	
	private final String IN_FILE_NAME = "Template1";
	private final String IN_FILE_NAME_E = "Template1_e";
	private final String OUT_FILE_NAME = "実質滞留債権判定";
	private final String OUT_FILE_NAME_E = "Credit";
	private final String EXTENTION  = ".xls";
	private final String SHEET_NAME = "実質滞留債権判定シート";	// 作成ファイルシート名
	private final String SHEET_NAME_E = "Credit";					// 作成ファイルシート名
	
	private static final int Start = 0;			//印刷範囲の設定
	private static final int endClm = 18;		//印刷範囲の設定
	
	// INパラメータ
	private String returnId;						// 遷移元画面ID
	private String anken_no;						// 案件Ｎｏ．
// 障害票No525　2008/05/30　細野　未承認案件に承認日付が表示される。
	private String phase;							// フェーズ
	private String soshiki;						// 案件Ｎｏ．
	private String lang_mode;						// 言語モード
	private String fname;							// ファイル名
	private String sname;							// シート名

	// resultset
	private int mode;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public TairyuExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		returnId = cmnData.getReturnId();
		lang_mode = cmnData.getComLangMode();
		if(returnId.equals(GS.OS6102)==true){
			SateisyosaiForm form = (SateisyosaiForm)appContext.getActionForm();
// 障害票No525　2008/05/30　細野　未承認案件に承認日付が表示される。
			phase = cmnData.getPhase();
			anken_no = cmnData.getSatei_anken_no();
			mode = 0;
		}else{
			TorokuForm form = (TorokuForm)appContext.getActionForm();
			phase = cmnData.getPhase();
			anken_no = cmnData.getSatei_anken_no();
			mode = 1;
		}
		
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
		// INパラメータ
		returnId = null;
		anken_no = null;
	}
	

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException,Exception {
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;

		// 障害No396 2008.05.19 中島 条件に現フェーズを取得するように修正。
		// 障害票No527　2008/06/01　細野　フェーズ切替え不正対応
		// SQL作成
		StringBuffer sql1 = new StringBuffer()
						.append("SELECT ")
						.append("TS.ym, ")
						.append("TS.kikan_tori_cd, ")
						.append("TM.business_nm_kj AS torihikisakimei, ")
						.append("TM.business_nm AS torihikisakimei_en, ")
						.append("SKM.ktk, ")
						.append("MS.satei_kaisya_nm, ")
						.append("MS.satei_kaisya_nm_e, ")
						.append("TS.phase, ")
						.append("THM.tori_cd_syusei_7, ")
// 障害票No471　2008/05/24　細野　オリジナル取引先コードに変更
						.append("SUBSTR(THM.tori_cd,1,7) AS tori_cd_7, ")
						.append("SH.seisiki_bumon_cd, ")
//障害票No471　2008/05/24　細野　課を部に変更
						.append("SH.bu_cd, ")
						.append("SH.bu_nm, ")
						.append("SH.cell_cd, ")
						.append("SH.cell_nm, ")
						.append("THM.anken_no, ")
						.append("THM.anken_no_eda, ")
						.append("THM.kanjo_cd, ")
						.append("KM.kanjo_nm, ")
						.append("THM.kanjo_uchi_cd, ")
						.append("KM.kanjo_uchi_nm, ")
						// 課題No.158 
						// 追加開始
						.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(THM.shusi_dt,'yyyy/mm/dd'),TO_CHAR(THM.shusi_dt,'mm/dd/yyyy'),'")
						.append(lang_mode) 
						.append("') AS shusi_dt,")
						.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(THM.manki_dt,'yyyy/mm/dd'),TO_CHAR(THM.manki_dt,'mm/dd/yyyy'),'")
						.append(lang_mode) 
						.append("') AS manki_dt,")
						.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(THM.syori_dt,'yyyy/mm/dd'),TO_CHAR(THM.syori_dt,'mm/dd/yyyy'),'")
						.append(lang_mode) 
						.append("') AS syori_dt,")
						/*
						.append("TO_CHAR(THM.shusi_dt, 'yyyy/mm/dd') AS shusi_dt, ")
						.append("TO_CHAR(THM.manki_dt, 'yyyy/mm/dd') AS manki_dt, ")
						.append("TO_CHAR(THM.syori_dt, 'yyyy/mm/dd') AS syori_dt, ")
						*/
						// 追加完了		
						.append("THM.keiyaku_denpyo_no, ")
						.append("THM.kingaku ")
						.append("FROM ")
						.append("SST_TAIRYU_STAT TS LEFT JOIN ")
						.append("SSE_TAIHI TT ON ")
						.append("TS.ym = TT.ym ")
						.append("AND TS.mise_cd = TT.office_cd ")
						.append("AND TRIM(TS.kikan_tori_cd) || '00'= TT.kikan_tori_cd ")
						.append("AND TS.system_kbn = TT.system_kbn ")
						.append("AND TS.satei_kaisha_cd = TT.satei_kaisha_cd LEFT JOIN ")
						.append("SSE_TOGO_MST TM ON ")
						.append("TT.ym = TM.ym ")
						.append("AND TT.sikibetu_cd = TM.sikibetu_cd ")
						.append("AND TT.togo_tori_cd = TM.togo_tori_cd ")
						.append("AND TT.syori_kaisu = TM.syori_kaisu ")
						.append("AND TT.satei_kaisha_cd = TM.satei_kaisha_cd LEFT JOIN ")
						.append("SSE_KTK SKM ON ")
						.append("TT.ym = SKM.ym ")
						.append("AND TT.sikibetu_cd = SKM.sikibetu_cd ")
						.append("AND TT.togo_tori_cd = SKM.duns_no ")
						.append("AND TT.syori_kaisu = SKM.syori_kaisu ")
						.append("AND TT.satei_kaisha_cd = SKM.satei_kaisha_cd LEFT JOIN ")
						.append("SSM_SATEIKAISYA MS ON ")
						.append("TRIM(TS.kaisha7_cd) = TRIM(MS.kaisha_cd) ")
						.append("AND TS.system_kbn = MS.system_kbn ")
						.append("AND TS.satei_kaisha_cd = MS.satei_kaisha_cd LEFT JOIN ")
						.append("SST_TAIRYUHANTEIMEISAI THM ON ")
						.append("TS.anken_no = THM.anken_no LEFT JOIN ")
//障害票No471　2008/05/24　細野　課を部に変更
						.append("VW_SS_SOHEN SH ON ")
						.append("TRIM(THM.kaisya_cd)  = SH.original_kaisya_cd ")
						.append("AND THM.cell_cd = SH.original_cell_cd LEFT JOIN ")
						.append("SSM_KANJO KM ON ")
						.append("THM.system_kbn = KM.system_kbn ")
						.append("AND THM.kanjo_cd = KM.kanjo_cd ")
						.append("AND THM.kanjo_uchi_cd = KM.kanjo_uchi_cd ")
						.append("AND THM.sateikaisya_cd = KM.sateikaisya_cd ")
						.append("AND THM.mise_cd = KM.mise_cd ")
						.append("WHERE ")
						.append("THM.hantei_flg = '1' ")
						.append("AND TS.anken_no = '").append(anken_no).append("' ")
						//障害No.0037
						//追加開始
						.append("AND TT.syori_kaisu = '0' ")
						.append("ORDER BY THM.anken_no_eda ");
						//追加完了
						// 障害No396 2008.05.18 uechi 枝番の最大値のデータを取得するよう修正
						//.append("AND TH.ANKEN_NO_EDA = ")
						//.append("(SELECT MAX(ANKEN_NO_EDA) FROM SST_TAIRYUHANTEI TTH WHERE TH.anken_no = TTH.anken_no AND TH.phase = TTH.phase)");


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
		if(lang_mode.equals("Ja")==true){
			tname = IN_FILE_NAME;
			fname = OUT_FILE_NAME
						+ GS.HAIHUN
						+ cmnData.getSatei_bumon_cd()
						+ GS.HAIHUN
						+ cmnData.getKanjo_cd()
						+ GS.HAIHUN
						+ cmnData.getKanjo_nm()
						+ EXTENTION;
			sname = SHEET_NAME;
		}else{
			tname = IN_FILE_NAME_E;
			fname = OUT_FILE_NAME_E
						+ GS.HAIHUN
						+ cmnData.getSatei_bumon_cd()
						+ GS.HAIHUN
						+ cmnData.getKanjo_cd()
						+ GS.HAIHUN
						+ cmnData.getKanjo_nm()
						+ EXTENTION;
			sname = SHEET_NAME_E;
		}
		// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
		POIFSFileSystem filein = new POIFSFileSystem(
				new FileInputStream( appContext.getRealPath(GS.EXCELDIR + tname + GS.DOTXLS)) );
		HSSFWorkbook wb = new HSSFWorkbook(filein);    	
 
		HSSFSheet sheetD = wb.getSheetAt(1);
		int line = sheetD.getPhysicalNumberOfRows();
		for( int i=0; i<line; i++){
			HSSFRow rowD = sheetD.getRow(i) ;
			sheetD.removeRow(rowD) ; 
		}
		wb.setSheetName(1,"sheet1");    
       
		FileOutputStream fileOut = null;
		
		try{
			// SQL実行		
			rs1 = sqlExec.execQuery(sql1.toString());
	        // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
			//System.out.println(sql1);
			// 件数チェック
			if(!rs1.next()){		
				appContext.setMsgCode("warning.0004");
				if(rs1 != null) {
					rs1.close();
				}
				return;
			}
			
//	 障害票No525　2008/05/30　細野　未承認案件に承認日付が表示される。
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
			
			// SQL実行		
			rs2 = sqlExec.execQuery(sql2.toString());
			
			fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
			// ワークシートを変更する
			wb.setSheetName(0,sname);    
          
			// セルオブジェクトの作成（セル番号は0スタート）
			// ヘッダ部分の作成(1行目は項目名称)
			HSSFCell[] cellHd = new HSSFCell[100];
           
			HSSFSheet sheetW = wb.getSheetAt(0);

			rs1.beforeFirst();

			// 課題No.28対応
			// 追加開始
			ResultSet rsTairyujdg = null;
			StringBuffer sqlTairyujdg = new StringBuffer()
												.append("SELECT ")
												.append("kbn_hyouji_val ")
												.append("FROM ")
												.append("SSP_KBN ")
												.append("WHERE ")
												.append("kbn_key = 'tairyu_jdg' ")
												.append("AND system_kbn = '01' ")
												.append("AND lang_mode = '")
												.append(lang_mode)
												.append("'");
			rsTairyujdg = sqlExec.execQuery(sqlTairyujdg.toString());
			int counta = 0;
			int hiddenIdx = 19;
			HSSFRow ankenRow = sheetW.getRow(counta);
			if(ankenRow == null){
				ankenRow = sheetW.createRow(counta);
			}
			HSSFCell ankenCell = ankenRow.getCell((short)hiddenIdx);
			if(ankenCell == null){
				ankenCell = ankenRow.createCell((short)hiddenIdx);
			}
			// ankenCell.setEncoding(HSSFCell.ENCODING_UTF_16);
			ankenCell.setCellValue(cmnData.getSatei_anken_no());
			counta++;
			while (rsTairyujdg.next()) {
				HSSFRow tairyuRow = sheetW.getRow(counta);
				if(tairyuRow == null){
					tairyuRow = sheetW.createRow(counta);
				}
				HSSFCell tairyuCell = tairyuRow.getCell((short)hiddenIdx);
				if(tairyuCell == null){
					tairyuCell = tairyuRow.createCell((short)hiddenIdx);
				}
				// tairyuCell.setEncoding(HSSFCell.ENCODING_UTF_16);
				tairyuCell.setCellValue(Function.trim(rsTairyujdg.getString("kbn_hyouji_val")));
				counta++;
			}
			// 追加完了

			while ( rs1.next() ) {
				for (int i=0; i<8; i++ ) {
					HSSFRow row = sheetW.getRow(i);
					if(row != null){
	               	
						for (int j=0; j<19; j++ ) {
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
						String sysdate = sdfYMD.format(new Date());
						String yyyymm = Function.insertYmNengetu(sysdate.substring(0,6));
						String dd = sysdate.substring(6,8);
						// 課題No.158
						// 追加開始
						//String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8));
						String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8),lang_mode);
						// 追加完了
						if(mode == 0){
// 障害票No525　2008/05/30　細野　未承認案件に承認日付が表示される。
							if (rs2.next() && !GS.EMPTY_CHARCTER.equals(Function.trim(rs2.getString("syori_dt")))){
								//ステータスが承認のみ表示
								yyyymm = Function.insertYmNengetu(rs2.getString("syori_dt").substring(0,6));
								dd = rs2.getString("syori_dt").substring(6,8);
								// 課題No.158
								// 追加開始
								yyyymmdd = Function.insertDateSlash(rs2.getString("syori_dt").substring(0,8),lang_mode);
								//yyyymmdd = Function.insertDateSlash(rs2.getString("syori_dt").substring(0,8));
								// 追加完了
								// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修
								// 照会の場合
								// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
								if ("Ja".equals(lang_mode)) {
									cellHd[18].setCellValue( yyyymm + dd + "日" + "　承認");
								// 課題No.117 
								// 追加開始
								} else {
									//cellHd[18].setCellValue( yyyymmdd + "　Approve");
									cellHd[18].setCellValue( yyyymmdd + "  Approve");
							    // 追加完了
								}
// 障害票No525　2008/06/01　細野　進んだレコード参照状態をまき戻す。
								rs2.beforeFirst();
							} else {
								// 承認以外は空白を挿入
								cellHd[18].setCellValue("");
							}								
						}else{
							// 滞留の場合
							// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if ("Ja".equals(lang_mode)) {
							cellHd[18].setCellValue( yyyymm + dd + "日" + "　作成");
							} else {
								// 課題No.117 
								// 追加開始
								//cellHd[18].setCellValue( yyyymmdd + "　Creation");
								cellHd[18].setCellValue( yyyymmdd + "  Creation");
								// 追加完了
							}
						}
		            	
					// 査定会社名
					}else if(i == 1){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[18].setCellValue(rs1.getString("satei_kaisya_nm"));
						} else {
							cellHd[18].setCellValue(rs1.getString("satei_kaisya_nm_e"));
						}
						
					// タイトル行	
					}else if(i == 3){
						// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[0].setCellValue("実質滞留債権判定シート(" + Function.insertYmNengetu(rs1.getString("ym"))+ "末基準)");
						} else {
							// 課題No.158
							// 追加開始
							//cellHd[0].setCellValue("Judgment of Overdue Debt Sheet (On the basis of " + Function.insertYmSlash(rs1.getString("ym"))+ ")");
							cellHd[0].setCellValue("Judgment of Overdue Debt Sheet (On the basis of " + Function.insertYmSlash(rs1.getString("ym"),lang_mode)+ ")");
							// 追加完了
						}
	
					// 勘定先CD、格付、フェーズ行
					}else if(i == 4){
						cellHd[1].setCellValue(rs1.getString("kikan_tori_cd"));
						cellHd[5].setCellValue(rs1.getString("ktk"));
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							if(mode == 0){
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[18].setCellValue("＜対象先選定結果＞");
									}else if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
										cellHd[18].setCellValue("＜滞留判定結果＞");
									}else{
										cellHd[18].setCellValue("＜滞留判定検証結果＞");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[18].setCellValue("＜対象先選定中＞");
									}else if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
										cellHd[18].setCellValue("＜滞留判定中＞");
									}else{
										cellHd[18].setCellValue("＜滞留判定検証中＞");
									}
								}
							}else{
								// 滞留の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
								if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
									cellHd[18].setCellValue("＜滞留判定中＞");
								}else{
									cellHd[18].setCellValue("＜滞留判定検証中＞");
								}
							}
						} else {
							if(mode == 0){
								// 照会の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
								if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[18].setCellValue("<Select Customer Result>");
									}else if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
										cellHd[18].setCellValue("<Judgment Result>");
									}else{
										cellHd[18].setCellValue("<Judgment Verification Result>");
									}
								}else{
									if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
										cellHd[18].setCellValue("<Select Customer Processing>");
									}else if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
										cellHd[18].setCellValue("<Judgment Processing>");
									}else{
										cellHd[18].setCellValue("<Judgment Verification Processing>");
									}
								}
							}else{
								// 滞留の場合
								// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス10を定数化
								if(GS.PHASE_TAIRYU_HANTEI.equals(phase)){
									cellHd[18].setCellValue("<Judgment>");
								}else{
									cellHd[18].setCellValue("<Judgment Verification>");
								}
							}
						}
	
					// 勘定先名行
					}else if(i == 5){
						if(lang_mode.equals("Ja")==true){
							if(rs1.getString("torihikisakimei") == null){
								cellHd[1].setCellValue(rs1.getString("torihikisakimei_en"));
							}else{
								cellHd[1].setCellValue(rs1.getString("torihikisakimei"));
							}
						}else{
							cellHd[1].setCellValue(rs1.getString("torihikisakimei_en"));
						}
							
					}
				}

			}
			
			// 明細部分の作成(8行目から)
			int j = 8;
           
			rs1.beforeFirst();
			HSSFRow rowM[] = new HSSFRow[getRsCount(rs1) + 8];
			
			// 課題No.27 国内フォント統一対応
			// 追加開始
			HSSFCellStyle[] Style = new HSSFCellStyle[20];
			for(int i =0; i<20; i++) {
				Style[i] = sheetW.getRow(8).getCell((short)i).getCellStyle();
			}
			
/*
			// 追加完了

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
			style2.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			// 障害票No:348③
			// チェックイン日：2008/05/14
			// 修正者：細野
			// フォントサイズ不正の改修
  	        HSSFFont font2 = wb.createFont();
   	        font2.setFontHeightInPoints((short)10);
   	        font2.setFontName("ＭＳ Ｐゴシック");
   	        style2.setFont(font2);
   	        style2.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0"));
  	        // ここまで
*/   	        
			while ( rs1.next() ) {
				// j行目の作成
				rowM[j] = sheetW.createRow(j);
	            
				HSSFCell[] cellData = new HSSFCell[100];
	            
				// 課題No.28対応
				// 修正開始
				//for (int i=0; i<19; i++ ) {
				for (int i=0; i<20; i++ ) {
				// 修正完了
					cellData[i] = rowM[j].createCell((short)i);
					// cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
					
					// 課題No.27 国内フォント統一対応
					// 追加開始
					// スタイルの設定
					cellData[i].setCellStyle(Style[i]);
					// 追加完了
/*					
					if(i==17){
						cellData[i].setCellStyle(style2);
					// 課題No.28対応
					// 修正開始
					}else if(i!=19){
					// 修正完了
						cellData[i].setCellStyle(style1);
					}
*/										
				}
							
// 障害票No471　2008/05/24　細野　オリジナル取引先コードに変更
				cellData[0].setCellValue( rs1.getString("tori_cd_7") );			// オリジナルの7桁
				if(lang_mode.equals("Ja")==true){								// 勘定先名称
					if(rs1.getString("torihikisakimei") == null){
						cellData[1].setCellValue(rs1.getString("torihikisakimei_en"));
					}else{
						cellData[1].setCellValue(rs1.getString("torihikisakimei"));
					}
				}else{
					cellData[1].setCellValue(rs1.getString("torihikisakimei_en"));
				}
				cellData[2].setCellValue( rs1.getString("seisiki_bumon_cd") );	// 部門
				cellData[3].setCellValue( rs1.getString("bu_cd") );				// 部コード
				cellData[4].setCellValue( rs1.getString("bu_nm") );				// 部名称
				cellData[5].setCellValue( rs1.getString("cell_cd") );			// セルCD
				cellData[6].setCellValue( rs1.getString("cell_nm") );			// セル
				cellData[7].setCellValue( rs1.getString("kanjo_cd") );			// 勘定科目CD
				cellData[8].setCellValue( rs1.getString("kanjo_nm") );			// 勘定科目名称
				cellData[9].setCellValue( rs1.getString("kanjo_uchi_cd") );		// 内分類
				cellData[10].setCellValue( rs1.getString("kanjo_uchi_nm") );	// 内分類名称
				cellData[11].setCellValue( rs1.getString("shusi_dt") );			// 収支予定日
				cellData[12].setCellValue( rs1.getString("manki_dt") );			// 満期日
				cellData[13].setCellValue( rs1.getString("syori_dt") );			// 勘定処理日

// 障害票No527　2008/06/01　細野　フェーズ切替え不正対応
				StringBuffer sql3 = new StringBuffer()
										.append("SELECT ")
										.append("KTK.val AS tairyukbn, ") 
										.append("KTH.val AS tairyuhantei, ") 
										.append("TH.hantei_jiyuu ") 
										.append("FROM ")
										.append("SST_TAIRYUHANTEI TH LEFT JOIN ") 
		// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
										.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn' AND system_kbn ='01' AND lang_mode ='").append(lang_mode).append("') KTK ON ") 
										.append("TH.tairyu_kbn = KTK.kbn_val LEFT JOIN ") 
										.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_jdg' AND system_kbn ='01' AND lang_mode ='").append(lang_mode).append("') KTH ON ") 
										.append("TH.tairyu_hantei = KTH.kbn_val ") 
										.append("WHERE ")
										.append("TH.anken_no = '").append( rs1.getString("anken_no") ).append("' ")
										.append("AND TH.anken_no_eda = '").append( rs1.getString("anken_no_eda") ).append("' ")
										.append("AND TH.phase = '").append(phase).append("'");				
				// SQL実行
				rs3 = sqlExec.execQuery(sql3.toString());
				
				if(rs3.next()){
					cellData[14].setCellValue( rs3.getString("tairyukbn") );		// 滞留区分
					cellData[15].setCellValue( rs3.getString("tairyuhantei") );		// 滞留判定
					// No483, 2008/05/29, SJA渡辺, 改行前に余分な中黒(・)が表示されないように修正
					// No483, 2008/06/01, SJA平道, タブ前に余分な中黒(・)が表示されないように修正
					if (rs3.getString("hantei_jiyuu") != null) {
						cellData[18].setCellValue( rs3.getString("hantei_jiyuu").replaceAll("\r\n","\n").replaceAll("\t"," ") );		// 判定事由
					}
					
				}
				
				cellData[16].setCellValue( rs1.getString("keiyaku_denpyo_no") );// 契約No.
				cellData[17].setCellValue( Function.getValueOfLong(rs1.getString("kingaku")) );			// 金額(円)
				// 課題No.28対応
				// 修正開始
				cellData[19].setCellValue(rs1.getString("anken_no_eda"));
				// 修正完了

				if(rs3 != null) {
					rs3.close();
				}
				
				j++;
			}
			
			// 印刷範囲の設定
			wb.setPrintArea(Start,Start,endClm,Start,sheetW.getLastRowNum());
			
			//作成したワークブックを保存する
			//アプリからのシート削除は行わない
			//wb.removeSheetAt(1);
			wb.write(fileOut);
			fileOut.flush();
			
		} finally {
			if(fileOut != null) {
				fileOut.close();
			}	
			// Resultset close
			if(rs1 != null) {
				rs1.close();
			}
			if(rs2 != null) {
				rs2.close();
			}
			if(rs3 != null) {
				rs3.close();
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
			TorokuForm form = (TorokuForm)appContext.getActionForm();
			acc.execute( new ActionMapping(), form, req, res );
		}
		tmp.delete();
	}
	/**
	 * Resultsetの件数取得<br>
	 * 
	 * @param ResultSet
	 * @return int
	 */
	protected int getRsCount(ResultSet rs) {
	    try {
	        rs.last();
	        int count = rs.getRow();
	        rs.beforeFirst();
		    return count;
	    } catch(SQLException e) {
	        return 0;
	    }
	}
}