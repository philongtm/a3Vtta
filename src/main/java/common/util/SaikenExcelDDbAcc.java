/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2009/05/26      SSC				1.5次版組込
002		2009/11/04		SSC				課題No.27 国内フォント統一対応
003		2009/11/17		SSC				課題No.117 2バイト文字対応
004		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
005		2009/12/8		SSC				課題No.192 ヘッダ部、金額出力部の修正
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.syokaiZen.form.SateiForm;
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
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
* 債権ExcelDBアクセスクラス
*/
public class SaikenExcelDDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;							// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;							// 共通セッションデータ
	private SateiForm form = null;									// アクションフォーム
	private  SimpleDateFormat sdfYMD = null;						// 一時ファイル作成時のフォーマット	
	private final String IN_FILE_NAME = "Template1";
	private final String IN_FILE_NAME_E = "Template1_e";
	private final String OUT_FILE_NAME = "債権明細一覧.xls";
	private final String OUT_FILE_NAME_E = "CreditDetails.xls";
	// No786, 2008/06/05, SJA渡辺, 英語版シート名の最初の文字を大文字に修正
	private final String SHEET_NAME = "債権明細一覧";			// 作成ファイルシート名
	private final String SHEET_NAME_E = "CreditDetails";			// 作成ファイルシート名
	
	// INパラメータ
	private String anken_no;						// 案件Ｎｏ．
	private String lang_mode;						// 言語モード
	private String fname;							// ファイル名
	private String sname;							// シート名

	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SaikenExcelDDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		form = (SateiForm)appContext.getActionForm();
		List meisaiList = form.getAr_meisai();
		anken_no = form.getAnken_no();
		lang_mode = cmnData.getComLangMode();


	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
		// INパラメータ
		anken_no = null;
	}
	

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException,Exception {
		
		List meisaiList = form.getAr_meisai();
		// No219, 2008/06/02, SJA渡辺, ヘッダが消えないように修正
		int j = 10;
// 障害票No594 2008/06/12 細野 帳票出力方法仕様変更対応
		String serch_ym = "";
		
		if("".equals(form.getObjectDate())){
			// No594, 2008/06/13, SJA渡辺, 対象年月に何もデータが入っていない場合、年月を検索条件に加えないように修正
			//serch_ym = form.getBaseDate();
		}else{
			serch_ym = form.getObjectDate();
		}
		// No219, 2008/05/31, SJA渡辺, 金額を四つ出すように修正
		// 債権残高合計
		double saiken_zankei=0;				
		// 保証債務合計
		double hosyo_kei = 0;				
		// 引当金合計
		double hikiate_kei = 0;
		
		// resultset
		ResultSet rs1;
		rs1 = null;
		
		ResultSet rs2 = null;
		
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
			fname = OUT_FILE_NAME;
			sname = SHEET_NAME;
		}else{
			tname = IN_FILE_NAME_E;
			fname = OUT_FILE_NAME_E;
			sname = SHEET_NAME_E;
		}
		// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
		POIFSFileSystem filein = new POIFSFileSystem(
				new FileInputStream( appContext.getRealPath(GS.EXCELDIR + tname + GS.DOTXLS)) );
		HSSFWorkbook wb = new HSSFWorkbook(filein);    

		HSSFSheet sheetD = wb.getSheetAt(0);
		int line = sheetD.getPhysicalNumberOfRows();
		for( int i=0; i<line; i++){
			HSSFRow rowD = sheetD.getRow(i) ;
			sheetD.removeRow(rowD) ; 
		}
		wb.setSheetName(0,"sheet1",HSSFWorkbook.ENCODING_UTF_16);    
      
		FileOutputStream fileOut = null;
		fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
        
		// ワークシートを変更する
		wb.setSheetName(1,sname,HSSFWorkbook.ENCODING_UTF_16);    

		// セルオブジェクトの作成（セル番号は0スタート）
		// ヘッダ部分の作成(1行目は項目名称)
		HSSFCell[] cellHd = new HSSFCell[100];
		HSSFSheet sheetW = wb.getSheetAt(1);
		
        String anken = "";
        for(int cnt = 0;cnt< meisaiList.size();cnt++){
        	HashMap map = (HashMap)meisaiList.get(cnt);
        	anken += "'" + map.get("anken_no") + "'";
   	        if(cnt != meisaiList.size()-1){
   	        	anken +=",";
   	        }
        }

		try{
			// SQL作成
			StringBuffer sql1 = new StringBuffer()
							.append("SELECT DISTINCT ")
							.append("TS.ym, ")
							.append("TS.kikan_tori_cd, ")
							.append("TM.business_nm_kj AS torihikisakimei, ")
							.append("TM.business_nm AS torihikisakimei_en, ")
							.append("SKM.ktk, ")
							.append("MS.satei_kaisya_nm, ")
							.append("MS.satei_kaisya_nm_e, ")
							.append("TS.phase, ")
							.append("SUBSTR(THM.tori_cd,1,7) AS tori_cd_7, ")
							.append("TM2.business_nm_kj AS torihikisakimei2, ")
							.append("TM2.business_nm AS torihikisakimei_en2, ")
							.append("SH.seisiki_bumon_cd, ")
//障害票No471　2008/05/24　細野　課を部に変更
							.append("SH.bu_cd, ")
							.append("SH.bu_nm, ")
							.append("SH.cell_cd, ")
							.append("SH.cell_nm, ")
							.append("THM.sateikaisya_cd, ")
							.append("THM.tori_cd_5, ")
							.append("THM.anken_no, ")
							.append("THM.anken_no_eda, ")
							.append("THM.kanjo_cd, ")
							.append("KM.kanjo_nm, ")
							// No219, 2008/06/02, SJA渡辺, 金額を四個表示するために修正。
							.append("KM.saiken_flg, ")
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
							.append("KTK.val AS tairyukbn, ")
							.append("KTH.val AS tairyuhantei, ")
							.append("THM.keiyaku_denpyo_no, ")
							.append("THM.kingaku, ")
							.append("TH.hantei_jiyuu ")
							.append("FROM ")
							.append("SST_TAIRYU_STAT TS LEFT JOIN ")
							.append("SSE_TAIHI TT ON ")
							.append("TS.ym = TT.ym ")
							.append("AND TS.mise_cd = TT.office_cd ")
							.append("AND TRIM(TS.kikan_tori_cd) || '00'= TT.kikan_tori_cd ")
							.append("AND TS.system_kbn = TT.system_kbn LEFT JOIN ")
							.append("SSE_TOGO_MST TM ON ")
							.append("TT.ym = TM.ym ")
							.append("AND TT.sikibetu_cd = TM.sikibetu_cd ")
							.append("AND TT.togo_tori_cd = TM.togo_tori_cd ")
							.append("AND TT.syori_kaisu = TM.syori_kaisu LEFT JOIN ")
							.append("SSE_KTK SKM ON ")
							.append("TT.ym = SKM.ym ")
							.append("AND TT.sikibetu_cd = SKM.sikibetu_cd ")
							.append("AND TT.togo_tori_cd = SKM.duns_no ")
							.append("AND TT.syori_kaisu = SKM.syori_kaisu LEFT JOIN ")
							.append("SSM_SATEIKAISYA MS ON ")
							.append("TRIM(TS.kaisha7_cd) = TRIM(MS.kaisha_cd) ")
						   	.append("AND TS.system_kbn = MS.system_kbn ")
						   	.append("AND TS.satei_kaisha_cd = MS.satei_kaisha_cd LEFT JOIN ")
							.append("SST_TAIRYUHANTEI TH ON ")
							.append("TS.anken_no = TH.anken_no LEFT JOIN ")
							.append("SST_TAIRYUHANTEIMEISAI THM ON ")
							.append("TH.anken_no = THM.anken_no ")
							.append("AND TH.anken_no_eda = THM.anken_no_eda LEFT JOIN ")
							.append("SSE_TAIHI TT2 ON ")
							.append("THM.ym = TT2.ym ")
							.append("AND THM.mise_cd = TT2.office_cd ")
							.append("AND TRIM(THM.tori_cd_syusei_7) = TT2.kikan_tori_cd ")
							.append("AND THM.system_kbn = TT2.system_kbn LEFT JOIN ")
							.append("SSE_TOGO_MST TM2 ON ")
							.append("TT2.ym = TM2.ym ")
							.append("AND TT2.sikibetu_cd = TM2.sikibetu_cd ")
							.append("AND TT2.togo_tori_cd = TM2.togo_tori_cd ")
							.append("AND TT2.syori_kaisu = TM2.syori_kaisu LEFT JOIN ")
//障害票No471　2008/05/24　細野　課を部に変更
							.append("VW_SS_SOHEN SH ON ")
							.append("TRIM(THM.kaisya_cd)  = SH.original_kaisya_cd ")
							.append("AND THM.cell_cd = SH.original_cell_cd LEFT JOIN ")
							.append("SSM_KANJO KM ON ")
							.append("THM.system_kbn = KM.system_kbn ")
							.append("AND THM.kanjo_cd = KM.kanjo_cd ")
							.append("AND THM.kanjo_uchi_cd = KM.kanjo_uchi_cd LEFT JOIN ")
							.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE system_kbn ='01' AND kbn_key = 'tairyu_kbn' AND lang_mode ='").append(lang_mode).append("') KTK ON ")
							.append("TH.tairyu_kbn = KTK.kbn_val LEFT JOIN ")
							.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE system_kbn ='01' AND kbn_key = 'tairyu_jdg' AND lang_mode ='").append(lang_mode).append("') KTH ON ")
							.append("TH.tairyu_hantei = KTH.kbn_val ")
							.append("WHERE ")
//障害票No441　2008/05/28　細野　実質滞留判定と同様に、条件に現フェーズを取得するように修正。
							.append("TS.phase = TH.phase AND THM.hantei_flg = '1' ")
							// No219, 2008/06/02, SJA渡辺, 金額を四個表示するために修正。
							.append("AND KM.saiken_flg in ('1','2','3','9') ");
							// No594, 2008/06/13, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if (!("".equals(serch_ym))) {
								sql1.append("AND TS.ym = '").append(serch_ym).append("' ");
							}
							if(!("".equals(anken))){
								sql1.append("AND TS.anken_no in (").append(anken).append(") ");
							}
							sql1.append("order by THM.sateikaisya_cd,THM.tori_cd_5,THM.anken_no,THM.anken_no_eda ");
							
							
			StringBuffer sql2 = new StringBuffer()
							.append("SELECT kbn_hyouji_val ")
							.append("FROM SSP_KBN ")
							.append("WHERE kbn_key = 'tairyu_jdg' ")
							.append("AND kbn_val = '1' ")
							.append("AND lang_mode = '")
							.append(cmnData.getComLangMode())
							.append("' and system_kbn ='01'");


			// SQL実行		
			rs1 = sqlExec.execQuery(sql1.toString());
			rs2 = sqlExec.execQuery(sql2.toString());

			if(rs1.next()){
				// No219, 2008/06/02, SJA渡辺, ヘッダが消えないように修正
				for (int i=0; i<10; i++ ) {
					HSSFRow row = sheetW.getRow(i);
					if(row != null){
		               	
						// 課題No.192
						// 追加開始
						//for (int ii=0; ii<19; ii++ ) {
						for (int ii=0; ii<20; ii++ ) {
						// 追加完了
							cellHd[ii] = row.getCell((short)ii);
							if(cellHd[ii] != null){
								cellHd[ii].setEncoding(HSSFCell.ENCODING_UTF_16);
	
							}
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
						// 照会の場合
						// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						
						
						// 課題No.
						// 追加開始
						if ("Ja".equals(lang_mode)) {
							//cellHd[18].setCellValue( yyyymm + dd + "日" + "　承認");
							cellHd[18].setCellValue( yyyymm + dd + "日" + "　作成");
						} else {
							// 課題No.117 
							// 追加開始
							//cellHd[18].setCellValue( yyyymmdd + "　Approve");
							//cellHd[18].setCellValue( yyyymmdd + "  Approve");
							cellHd[18].setCellValue( yyyymmdd + "  Creation");
							// 追加完了
						}
						// 追加完了
				            	
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
						if ("Ja".equals(lang_mode)) {
							if(meisaiList.size() == 1){
								cellHd[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(rs1.getString("ym"))+ "末基準)");
							}else{
								// 複数になるため月は出さない
								cellHd[0].setCellValue("債権明細一覧");
							}
						} else {
							// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
							if(meisaiList.size() == 1){
								// 課題No.158
								// 追加開始
								//cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(rs1.getString("ym"))+ ")");
								cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(rs1.getString("ym"),lang_mode)+ ")");
								// 追加完了
							}else{
								// 複数になるため月は出さない
								cellHd[0].setCellValue("Credit Listing");
							}
						}
								
					}else if(i == 4){
						// 勘定先コードに指定があるか？
						if("".equals(form.getKanjoCd())){
									
						}else{
							cellHd[1].setCellValue(rs1.getString("kikan_tori_cd"));
							cellHd[5].setCellValue(rs1.getString("ktk"));
						}
						cellHd[18].setCellValue("");
													
					// 勘定先名行
					}else if(i == 5){
						// 勘定先名に指定があるか？
						if("".equals(form.getKanjoName())){
									
						}else{
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
					} else if (i == 7) {
						// No828, 2008/06/13, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
						if (rs2.next()) {
							
							// 課題No.192
							// 追加開始
							//cellHd[18].setCellValue(Function.trim(rs2.getString("kbn_hyouji_val")));
							cellHd[19].setCellValue(Function.trim(rs2.getString("kbn_hyouji_val")));
							// 追加完了
						}
					}
				}
			}
				
			// 明細部分の作成(10行目から)
	           
			rs1.beforeFirst();
			
			// 課題No.27
			// 追加開始
			HSSFCellStyle[] Style = new HSSFCellStyle[19];
			for(int i =0; i<=18; i++) {
				Style[i] = sheetW.getRow(10).getCell((short)i).getCellStyle();
			}
			// 追加完了
			
/* 			
			// No219, 2008/06/02, SJA渡辺, ヘッダが消えないように修正
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
 */
			while ( rs1.next() ) {
				// j行目の作成
				HSSFRow rowM[] = new HSSFRow[1];
           		rowM[0] = sheetW.createRow(j);
	            
           		HSSFCell[] cellData = new HSSFCell[100];
	            
           		for (int i=0; i<19; i++ ) {
           			cellData[i] = rowM[0].createCell((short)i);
           			cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
					// 課題No.27
					// 追加開始
					// スタイルの設定
					cellData[i].setCellStyle(Style[i]);
					// 追加完了
/*           			
           			if(i == 17){
           				cellData[i].setCellStyle(style2);
           			}else{
           				cellData[i].setCellStyle(style1);
           			}
*/
           		}
		            				
           		cellData[0].setCellValue( rs1.getString("tori_cd_7") );						// 勘定先７桁
				if(lang_mode.equals("Ja")==true){
					if(rs1.getString("torihikisakimei2") == null){
		           		cellData[1].setCellValue( rs1.getString("torihikisakimei_en2") );	// 勘定先名称
					}else{
		           		cellData[1].setCellValue( rs1.getString("torihikisakimei2") );		// 勘定先名称
					}
				}else{
	           		cellData[1].setCellValue( rs1.getString("torihikisakimei_en2") );		// 勘定先名称
				}
           		cellData[2].setCellValue( rs1.getString("seisiki_bumon_cd") );				// 部門
           		cellData[3].setCellValue( rs1.getString("bu_cd") );							// 部課コード
           		cellData[4].setCellValue( rs1.getString("bu_nm") );							// 部課名称
           		cellData[5].setCellValue( rs1.getString("cell_cd") );						// セルCD
           		cellData[6].setCellValue( rs1.getString("cell_nm") );						// セル
           		cellData[7].setCellValue( rs1.getString("kanjo_cd") );						// 勘定科目CD
           		cellData[8].setCellValue( rs1.getString("kanjo_nm") );						// 勘定科目名称
           		cellData[9].setCellValue( rs1.getString("kanjo_uchi_cd") );					// 内分類
           		cellData[10].setCellValue( rs1.getString("kanjo_uchi_nm") );				// 内分類名称
           		cellData[11].setCellValue( rs1.getString("shusi_dt") );						// 収支予定日
           		cellData[12].setCellValue( rs1.getString("manki_dt") );						// 満期日
           		cellData[13].setCellValue( rs1.getString("syori_dt") );						// 勘定処理日
           		cellData[14].setCellValue( rs1.getString("tairyukbn") );					// 滞留区分
           		cellData[15].setCellValue( rs1.getString("tairyuhantei") );					// 滞留判定
           		cellData[16].setCellValue( rs1.getString("keiyaku_denpyo_no") );			// 契約No.
           		cellData[17].setCellValue( Function.getValueOfLong(rs1.getString("kingaku")) );			// 金額(円)
				// No483, 2008/05/29, SJA渡辺, 改行前に余分な中黒(・)が表示されないように修正
				// No483, 2008/06/01, SJA平道, タブ前に余分な中黒(・)が表示されないように修正
           		if (rs1.getString("hantei_jiyuu") != null) {
           			cellData[18].setCellValue( rs1.getString("hantei_jiyuu").replaceAll("\r\n","\n").replaceAll("\t"," ") );		// 判定事由
           		}
           		j++;
	           		
				// No219, 2008/06/02, SJA渡辺, 金額を四個表示するために修正。
           		String saikenFlg = rs1.getString("saiken_flg");	           		
           		if(	"1".equals(saikenFlg) ||
					    "2".equals(saikenFlg)){
					// 債権残高合計＝リストに表示されている一般債権、固定化営業債権(債権フラグ='1'、'2')の合計
					saiken_zankei += Function.getValueOfDouble(rs1.getString("kingaku"));
				}else if("3".equals(saikenFlg)){
					// 保証債務合計＝リストに表示されている保証債務(債権フラグ='3')の合計
					hosyo_kei += Function.getValueOfDouble(rs1.getString("kingaku"));
				}else if("9".equals(saikenFlg)){
					// 引当金合計＝リストに表示されている引当金(債権フラグ='9')の合計
					hikiate_kei += Function.getValueOfDouble(rs1.getString("kingaku"));
				}
			}
				
			// No219, 2008/06/02, SJA渡辺, 金額を四個表示するために修正。
			
			// 課題No.192
			// 追加開始
			/*
			cellHd[15] = sheetW.getRow(4).getCell((short)15);
			cellHd[15].setCellValue(saiken_zankei);
			cellHd[18] = sheetW.getRow(5).getCell((short)18);
			cellHd[18].setCellValue(hosyo_kei);
			cellHd[18] = sheetW.getRow(6).getCell((short)18);
			cellHd[18].setCellValue(hikiate_kei);
			*/
			cellHd[15] = sheetW.getRow(4).getCell((short)17);
			cellHd[15].setCellValue(saiken_zankei);
			cellHd[18] = sheetW.getRow(5).getCell((short)17);
			cellHd[18].setCellValue(hosyo_kei);
			cellHd[18] = sheetW.getRow(6).getCell((short)17);
			cellHd[18].setCellValue(hikiate_kei);
			
			// 追加完了

			//作成したワークブックを保存する
			wb.write(fileOut);
           
			fileOut.flush();

		} finally {
			// No786, 2008/06/05, SJA渡辺, fileOutがnullチェック追加
			if (fileOut != null) {
				fileOut.close();
			}
	    	if (rs1 != null) {
	    		try {
	    			//Resultset close
	    			rs1.close();
	    		} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
	    		}
	    	}
	    	if (rs2 != null) {
	    		rs2.close();
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
		acc.execute( new ActionMapping(), form, req, res );
	    
		tmp.delete();
	}
}