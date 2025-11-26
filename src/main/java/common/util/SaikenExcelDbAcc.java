/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/11/04		SSC				課題No.27 国内フォント統一対応
002		2009/11/17		SSC				課題No.102 債権無しの場合の処理追加
003		2009/11/17		SSC				課題No.117 2バイト文字対応
004		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
005		2009/12/8		SSC				課題No.192 ヘッダ部、金額出力部の修正
******************************************************************************/
package common.util;

import app.SessionDataZen;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GL;
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

// No, 2008/05/26, SJA渡辺, 査定結果詳細画面と各帳票の処理の共有化修正
/**
* 債権ExcelDBアクセスクラス
*/
public class SaikenExcelDbAcc extends CommonDbAcc {

	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;							// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;							// 共通セッションデータ
	private  SimpleDateFormat sdfYMD = null;						// 一時ファイル作成時のフォーマット	
	private final String IN_FILE_NAME = "Template1";
	private final String IN_FILE_NAME_E = "Template1_e";
	private final String OUT_FILE_NAME = "債権明細一覧.xls";
	private final String OUT_FILE_NAME_E = "CreditDetails.xls";
	// No786, 2008/06/05, SJA渡辺, シート名に「シート」をいれないように修正
	private final String SHEET_NAME = "債権明細一覧";			// 作成ファイルシート名
	private final String SHEET_NAME_E = "CreditDetails";			// 作成ファイルシート名
	
	// INパラメータ
	private String anken_no;						// 案件Ｎｏ．
	private String lang_mode;						// 言語モード
	private String fname;							// ファイル名
	private String sname;							// シート名
	private String phase;
	private String kikan_tori_cd;
	private String torihikisakimei;
	private String ym;
	// resultset
//	private ResultSet rs1;
	// resultset
//	private ResultSet rs2;
//	private ResultSet rs3;
	
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SaikenExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		anken_no = cmnData.getSatei_anken_no();
		phase = cmnData.getPhase();
		lang_mode = cmnData.getComLangMode();
		
		kikan_tori_cd = cmnData.getKanjo_cd();
		torihikisakimei = cmnData.getKanjo_nm();
		ym = cmnData.getYm();
		

		/*rs1 = null;
		rs2 = null;
		rs3 = null;*/
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
	public void execute(ArrayList list) throws SQLException,Exception {
		// No501, 2008/05/31, SJA渡辺, ResultSetをメソッドの中で定義するように修正
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
        ///////////////////////////////////////////////////
		//障害票：453
		//チェックイン日：2008/5/28
		//対応者：SJA渡辺
		//概要：画面のメソッドと帳票のメソッドを共通化。
		///////////////////////////////////////////////////
		// SQL作成
		/*StringBuffer sql1 = new StringBuffer()
							.append("SELECT DISTINCT ")
							.append("SS.ym, ")
							.append("SS.kikan_tori_cd, ")
							.append("TM.business_nm_kj AS torihikisakimei, ")
							.append("TM.business_nm AS torihikisakimei_en, ")
							.append("SKM.ktk, ")
							.append("MS.satei_kaisya_nm, ")
							.append("MS.satei_kaisya_nm_e, ")
							.append("SS.phase, ")
							.append("SUBSTR(HM.tori_cd,1,7) AS tori_cd_7, ")
							.append("TM2.business_nm_kj AS torihikisakimei2, ")
							.append("TM2.business_nm AS torihikisakimei_en2, ")
							.append("SH.seisiki_bumon_cd, ")
							// 障害票No371　2008/05/15　細野　部名、部コードに変更
							.append("SH.bu_cd, ")
							.append("SH.bu_nm, ")
							.append("SH.cell_cd, ")
							.append("SH.cell_nm, ")
							.append("HM.tori_cd, ")
							.append("HM.cell_cd, ")
							.append("HM.kanjo_cd, ")
							.append("KM.kanjo_nm, ")
							.append("HM.kanjo_uchi_cd, ")
							.append("KM.kanjo_uchi_nm, ")
							// 障害票No:348①
							// チェックイン日：2008/05/14
							// 修正者：細野
							// 日付フォーマット不正の改修
							.append("TO_CHAR(HM.shusi_dt, 'yyyy/mm/dd') AS shusi_dt, ")
							.append("TO_CHAR(HM.manki_dt, 'yyyy/mm/dd') AS manki_dt, ")
							.append("TO_CHAR(HM.syori_dt, 'yyyy/mm/dd') AS syori_dt, ")
							// ここまで
							.append("KTK.val AS tairyukbn, ")
							.append("KTH.val AS tairyuhantei, ")
							.append("HM.keiyaku_denpyo_no, ")
							.append("HM.kingaku, ")
							.append("TH.hantei_jiyuu ")
							.append("FROM ")
							.append("SST_SATEI_STAT SS LEFT JOIN ")
							.append("SSE_TAIHI TT ON ")
							.append("SS.ym = TT.ym ")
							.append("AND SS.mise_cd = TT.office_cd ")
							.append("AND TRIM(SS.kikan_tori_cd) || '00' = TT.kikan_tori_cd ")
							.append("AND SS.system_kbn = TT.system_kbn LEFT JOIN ")
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
							.append("TRIM(SS.satei_kaisha_cd) = TRIM(MS.satei_kaisha_cd) ")
							// 障害票No349　2008/05/15　細野　会社コードを条件に追加
							.append("AND SS.kaisha7_cd = MS.kaisha_cd LEFT JOIN ")
							.append("SST_HIKIATEBSMEISAI HM ON ")
							.append("SS.anken_no = HM.satei_anken_no LEFT JOIN ")
							.append("SSE_TAIHI TT2 ON ")
							.append("HM.ym = TT2.ym ")
							.append("AND HM.mise_cd = TT2.office_cd ")
							.append("AND TRIM(HM.tori_cd_5) = substr(TT2.kikan_tori_cd,1,5) ")
							.append("AND HM.system_kbn = TT2.system_kbn LEFT JOIN ")
							.append("SSE_TOGO_MST TM2 ON ")
							.append("TT2.ym = TM2.ym ")
							.append("AND TT2.sikibetu_cd = TM2.sikibetu_cd ")
							.append("AND TT2.togo_tori_cd = TM2.togo_tori_cd ")
							.append("AND TT2.syori_kaisu = TM2.syori_kaisu LEFT JOIN ")
							// 障害票No371　2008/05/15　細野　部名、部コードに変更
							.append("VW_SS_SOHEN SH ON ")
							.append("TRIM(HM.kaisya_cd)  = SH.original_kaisya_cd ")
							.append("AND HM.cell_cd = SH.original_cell_cd LEFT JOIN ")
							.append("SSM_KANJO KM ON ")
							.append("HM.system_kbn = KM.system_kbn ")
							.append("AND HM.kanjo_cd = KM.kanjo_cd ")
							.append("AND HM.kanjo_uchi_cd = KM.kanjo_uchi_cd LEFT JOIN ")
							.append("SST_TAIRYUHANTEI TH ON ")
							.append("HM.anken_no = TH.anken_no ")
							.append("AND HM.anken_no_eda = TH.anken_no_eda LEFT JOIN ")
							.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn' AND lang_mode ='").append(lang_mode).append("') KTK ON ")
							.append("TH.tairyu_kbn = KTK.kbn_val LEFT JOIN ")
							.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_jdg' AND lang_mode ='").append(lang_mode).append("') KTH ON ")
							.append("TH.tairyu_hantei = KTH.kbn_val ")
							.append("WHERE ")
							// 障害票No349　2008/05/17　細野　判定事由複数出力の不具合対応
							.append("TH.phase = (select MAX(phase) from SST_TAIRYUHANTEI where HM.anken_no = anken_no AND HM.anken_no_eda = anken_no_eda) ")
							.append("AND SS.anken_no = '")
							.append(anken_no)
							.append("' AND KM.saiken_flg in ('1','2','3','9') ")
							.append(" ORDER BY HM.tori_cd, HM.cell_cd");*/
		boolean sql3Flg = false;
		StringBuffer sql3 = null;
		StringBuffer sql1 = new StringBuffer()
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

		if ("".equals(anken_no)) {
			if (list != null) {
				//課題No.102
				//修正開始
				if(list.size() == 0){
					appContext.setMsgCode(GL.ERR_OUTPUT);
					return;
				}
				//修正完了
				String sateiAnkenNo = (String)((HashMap)list.get(0)).get("satei_anken_no");
					sql3 = new StringBuffer()
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
							.append(sateiAnkenNo)
							.append("'");
				sql3Flg = true;
			}
		}
		StringBuffer sql4 = new StringBuffer()
		.append("SELECT kbn_hyouji_val ")
		.append("FROM SSP_KBN ")
		.append("WHERE kbn_key = 'tairyu_jdg' ")
		.append("AND system_kbn = '01' ")
		.append("AND kbn_val = '1' ")
		.append("AND lang_mode = '")
		.append(cmnData.getComLangMode())
		.append("'");
		
		
		// 件数チェック
		/*if(!rs1.next()){		
			appContext.setMsgCode("warning.0004");
			log.write(GS.LOG_WAR,CLASSNAME,"warning.0004");		
			if(rs1 != null) {
				rs1.close();
			}
			return;
		}*/
		/*if(list == null || list.size() == 0){		
			appContext.setMsgCode("warning.0004");
			log.write(GS.LOG_WAR,CLASSNAME,"warning.0004");		
			return;
		}*/
		
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
		wb.setSheetName(0,"sheet1");    
      
		FileOutputStream fileOut = null;
		try{
			fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
           
			// ワークシートを変更する
			wb.setSheetName(1,sname);    

			// セルオブジェクトの作成（セル番号は0スタート）
			// ヘッダ部分の作成(1行目は項目名称)
			HSSFCell[] cellHd = new HSSFCell[100];
			HSSFSheet sheetW = wb.getSheetAt(1);

			// SQL実行		
			rs1 = sqlExec.execQuery(sql1.toString());
			// SQL実行		
			rs2 = sqlExec.execQuery(sql2.toString());
			//rs2.next();
			if (sql3Flg) {
				rs3 = sqlExec.execQuery(sql3.toString());
			}
			
			rs4 = sqlExec.execQuery(sql4.toString());
			
			// No219, 2008/05/31, SJA渡辺, 金額を四つ出すように修正
			// 債権残高合計
			double saiken_zankei=0;				
			// 保証債務合計
			double hosyo_kei = 0;				
			// 引当金合計
			double hikiate_kei = 0;

			if(list != null && list.size() != 0){
				Iterator itr = list.iterator();
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
			if ( rs1.next() ) {
				for (int i=0; i<10; i++ ) {
					HSSFRow row = sheetW.getRow(i);
					if(row != null){
	               	
						// 課題No.192
						// 追加開始
						//for (int j=0; j<19; j++ ) {
						for (int j=0; j<20; j++ ) {
						// 追加完了
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
						if (rs2.next()) {
							if(GS.STATUS_KANRYO.equals(cmnData.getStatus()) && !GS.EMPTY_CHARCTER.equals(Function.trim(rs2.getString("syori_dt")))){
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
									cellHd[18].setCellValue( yyyymm + dd + "日" + "　承認");
								} else {
									
									// 課題No.117
									// 追加開始
									//cellHd[18].setCellValue( yyyymmdd + "　Approve");
									cellHd[18].setCellValue( yyyymmdd + "  Approve");
									// 追加完了 
								}
							}else{
								// 承認以外は空白を挿入
								cellHd[18].setCellValue("");
							}
						} else {
							// 承認以外は空白を挿入
							cellHd[18].setCellValue("");
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
							cellHd[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(ym)+ "末基準)");
						} else {
							// 課題No.158
							// 追加開始
							//cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym)+ ")");
							cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
							// 追加完了
						}
	
					// 勘定先CD、格付、フェーズ行
					}else if(i == 4){
						if("".equals(cmnData.getKanjo_cd())){
							
						}else{
							cellHd[1].setCellValue(kikan_tori_cd);
							cellHd[5].setCellValue(rs1.getString("ktk"));
						}
						
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(saiken_zankei);
						
	//	            	if(mode == 0){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							// 照会の場合
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定結果＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定結果＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定結果＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証結果＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定結果＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定中＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定中＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定中＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証中＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定中＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						} else {
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							// 課題No.27 書式設定(フォントを統一するため「＜＞」を半角に修正)
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Result>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Result>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Result>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Result>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Result>");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Processing＞>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Processing>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Processing>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Processing>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Processing>");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						}

	//	            	}else{
	//	            		// 債権の場合
	//		            	if((rs1.getString("phase")).equals("10")){
	//			           		cellHd[18].setCellValue("＜判定結果＞");
	//		            	}else{
	//			           		cellHd[18].setCellValue("＜判定検証結果＞");
	//		            	}
	//	            	}
					// 勘定先名行
					}else if(i == 5){
						if("".equals(torihikisakimei)){
							
						}else{
							/*if(lang_mode.equals("Ja")==true){
								if(rs1.getString("torihikisakimei") == null){
									cellHd[1].setCellValue(rs1.getString("torihikisakimei_en"));
								}else{
									cellHd[1].setCellValue(rs1.getString("torihikisakimei"));
								}
							}else{
								cellHd[1].setCellValue(rs1.getString("torihikisakimei_en"));
							}*/
							cellHd[1].setCellValue(torihikisakimei);
						}
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hosyo_kei);
					} else if (i == 6) {
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hikiate_kei);
					} else if (i == 7) {
						// No828, 2008/06/12, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
						if (rs4.next()) {
							
							// 課題No.192
							// 追加開始
							//cellHd[18].setCellValue(Function.trim(rs4.getString("kbn_hyouji_val")));
							cellHd[19].setCellValue(Function.trim(rs4.getString("kbn_hyouji_val")));
							// 追加完了
						}
					}
				}
			} else if (sql3Flg && rs3.next()) {
				for (int i=0; i<8; i++ ) {
					HSSFRow row = sheetW.getRow(i);
					if(row != null){
	               	
						// 課題No.192
						// 追加開始
						//for (int j=0; j<19; j++ ) {
						for (int j=0; j<20; j++ ) {
						// 追加完了
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
						cellHd[18].setCellValue("");
		            	
					// 査定会社名
					}else if(i == 1){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[18].setCellValue(rs3.getString("satei_kaisya_nm"));
						} else {
							cellHd[18].setCellValue(rs3.getString("satei_kaisya_nm_e"));
						}
	
					// タイトル行	
					}else if(i == 3){
						// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(ym)+ "末基準)");
						} else {
							// 課題No.158
							// 追加開始
							//cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym)+ ")");
							cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
							// 追加完了
						}
	
					// 勘定先CD、格付、フェーズ行
					}else if(i == 4){
						if("".equals(cmnData.getKanjo_cd())){
							
						}else{
							cellHd[1].setCellValue(kikan_tori_cd);
							cellHd[5].setCellValue(rs3.getString("ktk"));
						}
						
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(saiken_zankei);
						
	//	            	if(mode == 0){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							// 照会の場合
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定結果＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定結果＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定結果＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証結果＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定結果＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定中＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定中＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定中＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証中＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定中＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						} else {
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Result>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Result>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Result>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Result>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Result>");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Processing>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Processing>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Processing>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Processing>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Processing>");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						}

	//	            	}else{
	//	            		// 債権の場合
	//		            	if((rs1.getString("phase")).equals("10")){
	//			           		cellHd[18].setCellValue("＜判定結果＞");
	//		            	}else{
	//			           		cellHd[18].setCellValue("＜判定検証結果＞");
	//		            	}
	//	            	}
	
					// 勘定先名行
					}else if(i == 5){
						if("".equals(torihikisakimei)){
							
						}else{
							cellHd[1].setCellValue(torihikisakimei);
						}
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hosyo_kei);
					} else if (i == 6) {
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hikiate_kei);
					} else if (i == 7) {
						// No828, 2008/06/12, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
						if (rs4.next()) {
							
							// 課題No.192
							// 追加開始
							//cellHd[18].setCellValue(Function.trim(rs4.getString("kbn_hyouji_val")));
							cellHd[19].setCellValue(Function.trim(rs4.getString("kbn_hyouji_val")));
							// 追加完了
							
						}
					}
				}
			} else {
				for (int i=0; i<10; i++ ) {
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
						cellHd[18].setCellValue("");
		            	
					// 査定会社名
					}else if(i == 1){
						cellHd[18].setCellValue("");
	
					// タイトル行	
					}else if(i == 3){
						// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[0].setCellValue("債権明細一覧(" + Function.insertYmNengetu(ym)+ "末基準)");
						} else {
							// 課題No.158
							// 追加開始
							//cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym)+ ")");
							cellHd[0].setCellValue("Credit Listing (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
							// 追加完了
						}
	
					// 勘定先CD、格付、フェーズ行
					}else if(i == 4){
						if("".equals(cmnData.getKanjo_cd())){
							
						}else{
							cellHd[1].setCellValue(kikan_tori_cd);
							cellHd[5].setCellValue("");
						}
						
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(saiken_zankei);
						
	//	            	if(mode == 0){
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
		            		// 照会の場合
						if ("Ja".equals(lang_mode)) {
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定結果＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定結果＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定結果＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証結果＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定結果＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("＜対象先選定中＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("＜クレーム債権再設定中＞");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("＜一次査定中＞");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("＜一次査定検証中＞");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("＜二次査定中＞");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						} else {
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Result>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Result>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Result>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Result>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Result>");
								}else{
									cellHd[18].setCellValue("");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || (phase).equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[18].setCellValue("<Select Customer Processing>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[18].setCellValue("<Judgment of Claim Debt Processing>");
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI)){
									cellHd[18].setCellValue("<Primary Assessment Processing>");
									// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス50を定数化
								}else if((phase).equals(GS.PHASE_ICHIJI_SATEI_KENSYO)){
									cellHd[18].setCellValue("<Primary Assessment Verification Processing>");
	                            // 障害対応:  2008/07/30 中島 二次査定フェーズの時のみ出力するようにする 
								}else if((phase).equals(GS.PHASE_NIJI_SATEI)){
									cellHd[18].setCellValue("<Secondary Assessment Processing>");
								}else{
									cellHd[18].setCellValue("");
								}
							}
						}

	//	            	}else{
	//	            		// 債権の場合
	//		            	if((rs1.getString("phase")).equals("10")){
	//			           		cellHd[18].setCellValue("＜判定結果＞");
	//		            	}else{
	//			           		cellHd[18].setCellValue("＜判定検証結果＞");
	//		            	}
	//	            	}
	
					// 勘定先名行
					}else if(i == 5){
						if("".equals(torihikisakimei)){
							
						}else{
							cellHd[1].setCellValue(torihikisakimei);
						}
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hosyo_kei);
					} else if (i == 6) {
						// No526, 2008/06/11, SJA渡辺, セルに直接金額を入れるように修正
						cellHd[17].setCellValue(hikiate_kei);
					} else if (i == 7) {
						// No828, 2008/06/12, SJA渡辺, 区分テーブルの6ヶ月超滞留、6month Overを入れるように修正
						if (rs4.next()) {
							cellHd[18].setCellValue(Function.trim(rs4.getString("kbn_hyouji_val")));
						}
					}
				}
			}
			// 明細部分の作成(8行目から)
			int j = 10;
           
			//rs1.beforeFirst();
			HSSFRow rowM[] = new HSSFRow[list.size() + 10];
			
			// 課題No.27 書式設定
			// 追加開始
			HSSFCellStyle[] Style = new HSSFCellStyle[19];
			for(int i =0; i<=18; i++) {
				Style[i] = sheetW.getRow(10).getCell((short)i).getCellStyle();
			}
			// 追加完了
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
*/
   	        if (list != null) {
   	        	Iterator itr = list.iterator();
   	        	while (itr.hasNext()) {
   	        		HashMap map = (HashMap)itr.next();
   	        		// j行目の作成
   	        		rowM[j] = sheetW.createRow(j);
   	        		
   	        		HSSFCell[] cellData = new HSSFCell[100];
   	        		
   	        		for (int i=0; i<19; i++ ) {
   	        			cellData[i] = rowM[j].createCell((short)i);
   	        			// cellData[i].setEncoding(HSSFCell.ENCODING_UTF_16);
   	        			
   						// 課題No.27 書式設定
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
   					// No483, 2008/05/29, SJA渡辺, 改行前に余分な中黒(・)が表示されないように修正
					// No483, 2008/06/01, SJA平道, タブ前に余分な中黒(・)が表示されないように修正
   	        		if ((String)map.get("hantei_jiyuu") != null) {
   	        			cellData[18].setCellValue( ((String)map.get("hantei_jiyuu")).replaceAll("\r\n","\n").replaceAll("\t"," ") );		// 判定事由
   	        		}
   	        		j++;
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
			// Resultset close
			if(rs2 != null) {
				rs2.close();
			}
			if(rs3 != null) {
				rs3.close();
			}
			if(rs4 != null) {
				rs4.close();
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
		acc.execute( new ActionMapping(), appContext.getActionForm(), req, res );
	    
		tmp.delete();
	}
}