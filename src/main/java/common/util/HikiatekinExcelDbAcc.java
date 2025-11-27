/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2009/5/18       SSC             1.5次機能組込
002		2009/10/23		SSC				課題No.60 引当金検証時の取引先区分・債権区分設定
003		2009/10/23		SSC				課題No.73 取引先区分プルダウン設定値を2.0次に合わせる
004		2009/11/04		SSC				課題No.27 国内フォント統一対応
005		2009/11/17		SSC				課題No.117 2バイト文字対応 
006		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
007		2014/03/14		SSC				案件No.D13493 改善対応
008		2014/05/15		SSC				案件No.D13493 改善対応（ファイル名文字化け対応）
009		2015.04.20		SSC				BJ201502003 ICISプロジェクト対応
******************************************************************************/
package common.util;

import app.SessionDataZen;
import app.hikiate.form.KensyoForm;
import app.syokai.form.SateisyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.AppDownloadAction;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import config.adapter.struts.action.ActionMapping;

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
* 引当金検証DBアクセスクラス
*/
public class HikiatekinExcelDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;						// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;						// 共通セッションデータ
	private  SimpleDateFormat sdfYMD = null;					// 一時ファイル作成時のフォーマット	
	private final String IN_FILE_NAME = "Template4";
	private final String IN_FILE_NAME_E = "Template4_e";
	private final String OUT_FILE_NAME = "引当金検証";
	private final String OUT_FILE_NAME_E = "Verification";
	private final String EXTENTION  = ".xls";
	private final String SHEET_NAME = "引当金検証シート";		// 作成ファイルシート名
	private final String SHEET_NAME_E = "Verification";		// 作成ファイルシート名
	
	// INパラメータVerification
	private String returnId;						// 遷移元画面ID
	private String anken_no;						// 案件Ｎｏ．(最終月)
	private String satei_anken_no;					// 案件Ｎｏ．(初回、中間月)
	private String lang_mode;						// 言語モード
	private String fname;							// ファイル名
	private String sname;							// シート名
	private String ym;								// 最終月年月
	private String before_ym;						// 初回、中間月年月
	private String satei_kaisya;					// 査定会社
	private String syori_cnt;						// 処理回数

	// resultset
	private int mode;
	private int cnt;
	private int chk1;
	private int chk2;
	private int chk3;
	private boolean rs1_flg = false;
	private boolean rs2_flg = false;
	private boolean rs4_flg = false;
	private boolean rs5_flg = false;
	private boolean rs7_flg = false;
	private boolean rs8_flg = false;
	
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public HikiatekinExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		returnId = cmnData.getReturnId();
		if(returnId.equals(GS.OS6102)==true){
			SateisyosaiForm form = (SateisyosaiForm)appContext.getActionForm();
			anken_no = cmnData.getSatei_anken_no();
			ym = cmnData.getYm();
			satei_kaisya = cmnData.getAnken_satei_kaisya_cd();
			// 障害票：413 2008/5/20 細野 基準日表示対応
			syori_cnt = cmnData.getSyoriCnt(); 
			mode = 0;
		}else{
			KensyoForm form = (KensyoForm)appContext.getActionForm();
			anken_no = form.getAnken_no();
			satei_anken_no = form.getSateiAnkenNo();
			ym = form.getYm();
			// No623, 2008/06/10, SJA渡辺, 案件の査定会社コードを使用するように修正
			//satei_kaisya = cmnData.getComSateiKaishaCd();
			satei_kaisya = cmnData.getAnken_satei_kaisya_cd();
			// 障害票：413 2008/5/20 細野 基準日表示対応
			syori_cnt = "1"; 
			mode = 1;
		}
		lang_mode = cmnData.getComLangMode();
		// No414,517, 2008/05/31, SJA渡辺, 不要なログ出力の削除
		//log.write(GS.LOG_INF,CLASSNAME,anken_no);
		
		chk1 = 0;
		chk2 = 0;
		chk3 = 0;
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		returnId = null;
	    anken_no = null;
	    syori_cnt = "";
	}
	

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException,Exception {

	    //////////////////////////////////
	    //障害票：501
	    //チェックイン日：2008/05/28
	    //対応者：SJA 渡辺
	    //概要：変数のスコープ
	    //////////////////////////////////
		ResultSet rs;
		ResultSet rs1;
		ResultSet rs2;
		ResultSet rs4;
		ResultSet rs5;
		ResultSet rs6;
		ResultSet rs7;
		ResultSet rs8;
		ResultSet rs9;
		ResultSet rs10;
		TempFile tmp ;
		FileInputStream fis;
		BufferedInputStream  is;
		OutputStream os;
		File tmpExcel;
		FileOutputStream fileOut;
		rs = null;
		rs1 = null;
		rs2 = null;
		rs4 = null;
		rs5 = null;
		rs6 = null;
		rs7 = null;
		rs8 = null;
		rs9 = null;
		rs10 = null;
		fileOut = null;
		fis = null;
		is = null;
		os = null;
		
		
		try{
			// 基準年
			String kijun_year = ym.substring(0,4);
			// 基準月
			String kijun_month = ym.substring(4,6);
			// 査定年月(初回月)
			StringBuffer satei_ym_syokai = new StringBuffer();
			// 査定年月(中間月)
			StringBuffer satei_ym_chukan = new StringBuffer();
			// 初回月・中間月査定案件No
			String satei_anken_no_syokai = null;
			//要件No.四-16
			//追加開始
			//半期四半期区分
			String hanki_sihanki_kbn = "";			
			//追加完了
			// 初回月・中間月の年月を取得。
			StringBuffer sql10 = new StringBuffer()
			.append(" select ")
			.append(" SK.kikan, ")
			.append(" SK.SYOKAI_ZUKI, ")
			.append(" SK.CYUUKAN_ZUKI, ")
			//要件No.四-16
			//追加開始
			.append(" SK.hanki_sihanki_kbn ")
			//追加完了
			.append(" from ")
			.append(" SSM_SYORIZUKI SK, ")
			.append(" SST_SATEI_STAT ST ")
			.append(" where ")
			.append(" SK.kaisha_cd = ST.mise_cd || '0' ")
			//要件No.四-16
			//追加開始
			.append(" and ST.hanki_sihanki_kbn = SK.hanki_sihanki_kbn ")
			//追加完了
			.append(" and ST.anken_no = '")
			.append(anken_no)
			.append("' and (SK.syokai_zuki ='")
			.append(kijun_month)
			.append("' or SK.cyuukan_zuki ='")
			.append(kijun_month)
			.append("' or SK.saisyu_zuki ='")
			.append(kijun_month)
			.append("')");
			
			
			// 初回月・中間月の月をセット。
			rs10 = sqlExec.execQuery(sql10.toString());
			int syokai_year = Function.getValueOfInt(kijun_year);
			while(rs10.next()){
				// 障害票：413 2008/5/20 細野 基準日表示対応
				//障害No.CO001対応
				//修正開始
				if("2".equals(rs10.getString("kikan")) && "1".equals(rs10.getString("hanki_sihanki_kbn"))){
				//修正完了
					if(kijun_month.equals(rs10.getString("SYOKAI_ZUKI"))){
						satei_ym_syokai.append(Function.getStringOfInt(syokai_year)).append(rs10.getString("SYOKAI_ZUKI"));
						syokai_year = Function.getValueOfInt(kijun_year) +1;
						satei_ym_chukan.append(kijun_year).append(rs10.getString("CYUUKAN_ZUKI"));
					}else{
						satei_ym_chukan.append(kijun_year).append(rs10.getString("CYUUKAN_ZUKI"));
						syokai_year = Function.getValueOfInt(kijun_year) -1;
						satei_ym_syokai.append(Function.getStringOfInt(syokai_year)).append(rs10.getString("SYOKAI_ZUKI"));
					}
				}else{
					satei_ym_syokai.append(kijun_year).append(rs10.getString("SYOKAI_ZUKI"));
					satei_ym_chukan.append(kijun_year).append(rs10.getString("CYUUKAN_ZUKI"));
				}
				//要件No.四-16
				//追加開始
				hanki_sihanki_kbn = rs10.getString("hanki_sihanki_kbn");
				//追加完了
			}
			
			//要件No.四-16
			//追加開始
			StringBuffer jokenYm = new StringBuffer();
			// 課題No.60，73
			// 修正開始
			if (hanki_sihanki_kbn.equals("1")){
				//jokenYm.append("HH.ym in ('")
				jokenYm.append("SS.ym in ('")
					   .append(satei_ym_syokai.toString())
					   .append("','")
					   .append(satei_ym_chukan.toString())
					   .append("')");
			}else{
				//jokenYm.append("HH.ym = '")
				jokenYm.append("SS.ym = '")
					   .append(satei_ym_syokai.toString())
					   .append("'");
			}
			// 修正完了
			//追加完了
			
			// 課題No.60，73
			// 修正開始
			// 初回月・中間月の査定案件No、システム区分、を取得。
			// 障害票：413 2008/5/20 細野 基準日表示対応
			/*StringBuffer sql = new StringBuffer()
			.append(" select ")
			.append(" distinct HH.satei_anken_no,HH.ym ")
			.append(" from ")
			.append(" SST_HIKIATEHANTEI HH ")
			.append(" where ")
			.append(" HH.system_kbn = '")
			.append(cmnData.getSystem_kbn())
			.append("' and HH.sateikaisya_cd = '")
			.append(satei_kaisya)
			.append("' and HH.mise_cd = '")
			.append(cmnData.getMise_cd())
			//要件No.四-16
			//追加開始
			.append("' and HH.hanki_sihanki_kbn = '")
			.append(hanki_sihanki_kbn)
			.append("' and ")
			.append(jokenYm.toString())
			//追加完了
         	//.append("' and HH.ym in ('")
			//.append(satei_ym_syokai.toString())
			//.append("','")
			//.append(satei_ym_chukan.toString())
			.append(" and HH.syori_kaisu = '0' ") // 初回月・中間月は処理回数0で固定
			.append(" and HH.tori_cd = '")
			.append(cmnData.getKanjo_cd())
			.append("'")
			.append(" order by HH.ym desc");*/
			StringBuffer sql  = new StringBuffer().append("SELECT SS.anken_no as satei_anken_no,")
			.append("SS.ym")
			.append(" FROM SST_SATEI_STAT SS ")
			.append(" WHERE SS.system_kbn = '" )
			.append(cmnData.getSystem_kbn())
			.append("' and SS.satei_kaisha_cd = '")
			.append(satei_kaisya)
			.append("' and SS.mise_cd='" )
			.append(cmnData.getMise_cd())
			.append("' and SS.syori_kaisu = '0'")
			.append(" and SS.phase = '60'")
			.append(" and SS.status = '40'")
			.append(" and SS.kikan_tori_cd = '")
			.append(cmnData.getKanjo_cd())
			.append("' and SS.hanki_sihanki_kbn = '")
			.append(hanki_sihanki_kbn)
			.append("' and ")
			.append(jokenYm.toString())
			.append(" ORDER BY SS.ym DESC");
			// 修正完了
			
			// 初回月・中間月査定案件Noセット。
			rs = sqlExec.execQuery(sql.toString());
			if(rs.next()){
				satei_anken_no_syokai = rs.getString("satei_anken_no");
				before_ym = Function.trim(rs.getString("ym"));
			}
			
			// SQL作成
			StringBuffer sql4 = new StringBuffer().append("SELECT KTH.val AS ktkkikan,")
			.append(" KTS.val AS gaibuktk,")
			// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
			.append(" KTS2.val2 AS oya_ittai_flg_nm,")
			.append(" KTS3.val3 AS oya_dokuritu_flg_nm,")
			.append(" TM.BUSINESS_NM_KJ,")
			.append(" TM.BUSINESS_NM,")
			.append(" KK.fss,")
			.append(" KK.duns_rating,")
			.append(" KK.ktk,")
			.append(" KK.oya_ktk,")
			.append(" KK.oya_ittai_flg,")
			.append(" KK.oya_dokuritu_flg,")
			.append(" KK.oya_duns_no,")
			.append(" TR.kikan_tori_cd,")
			.append(" TR.torisaki_nm,")
			.append(" TR.kanjo_nm,")
			.append(" TR.kingaku")
			.append(" FROM SST_SATEI_STAT SS")
			.append(" LEFT JOIN SSE_TAIHI TT ON")
			.append(" SS.ym = TT.ym")
			.append(" AND SS.mise_cd = TT.office_cd")
			.append(" AND TRIM(SS.kikan_tori_cd) || '00'= TT.kikan_tori_cd")
			.append(" AND SS.system_kbn = TT.system_kbn")
			.append(" LEFT JOIN SST_OTH_RYUHOSAIMU TR ON")
			.append(" SS.anken_no = TR.anken_no")
			.append(" LEFT JOIN SSE_KTK KK ON")
			.append(" TT.ym = KK.ym")
			.append(" AND TT.sikibetu_cd = KK.sikibetu_cd")
			.append(" AND TT.togo_tori_cd = KK.duns_no")
			.append(" AND TT.syori_kaisu = KK.syori_kaisu")
			.append(" LEFT JOIN TM_TOGO_MST@VIR_SJLMA TM ON ")
			.append(" TM.TOGO_TORI_CD = KK.oya_duns_no")
			.append(" LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val,")
			.append(" kbn_hyouji_val AS val")
			.append(" FROM SSP_KBN")
			.append(" WHERE kbn_key = 'ktk_kikan'")
			.append(" AND lang_mode ='")
			.append(lang_mode)
			.append("') KTH ON")
			.append(" KK.ktk_kikan = KTH.kbn_val")
			.append(" LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val,")
			.append(" kbn_hyouji_val AS val")
			.append(" FROM SSP_KBN")
			.append(" WHERE kbn_key = 'gaibu_ktk'")
			.append(" AND lang_mode ='")
			.append(lang_mode)
			.append("') KTS ON")
			.append(" KK.gaibu_ktk = KTS.kbn_val")
			.append(" LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val2,")
			.append(" kbn_hyouji_val AS val2")
			.append(" FROM SSP_KBN")
			.append(" WHERE kbn_key = 'oya_ittai_flg'")
			.append(" AND lang_mode ='")
			.append(lang_mode)
			.append("') KTS2 ON")
			.append(" KK.oya_ittai_flg = KTS2.kbn_val2")
			.append(" LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val3,")
			.append(" kbn_hyouji_val AS val3")
			.append(" FROM SSP_KBN")
			.append(" WHERE kbn_key = 'oya_dokuritu_flg'")
			.append(" AND lang_mode ='")
			.append(lang_mode)
			.append("') KTS3 ON")
			.append(" KK.oya_dokuritu_flg = KTS3.kbn_val3")
			.append(" WHERE SS.anken_no = '")
			.append(satei_anken_no_syokai)
			.append("'");
			
			// SQL実行		
			rs4 = sqlExec.execQuery(sql4.toString());
			
			// SQL作成
			StringBuffer sql1 = new StringBuffer()
			.append("SELECT ")
			.append("SS.ym, ")
			.append("SS.phase, ")
			.append("MS.satei_kaisya_nm, ")
			.append("MS.satei_kaisya_nm_e, ")
			.append("SS.kikan_tori_cd, ")
			.append("TT.togo_tori_cd, ")
			.append("TM.business_nm_kj, ")
			.append("TM.business_nm, ")
			.append("DK.HQ_COUNTRY_NM, ")
			.append("DK.HQ_COUNTRY_CD, ")
			.append("DK.BUSINESS_NM_KJ, ")
			.append("DK.STATE_NM_KJ, ")
			.append("DK.CITY_NM_KJ, ")
			.append("DK.STREET_ADR_KJ, ")
			.append("DK.STREET_ADR_KJ2, ")
			.append("DK.STREET_ADR, ")
			.append("DK.STREET_ADR2, ")
			.append("DK.CITY_NM, ")
			.append("DK.STATE_NM, ")
			.append("VK.JIYUU_CD_01, VK.JIYUU_CD_02, VK.JIYUU_CD_03, VK.JIYUU_CD_04, VK.JIYUU_CD_05, VK.JIYUU_CD_06, VK.JIYUU_CD_07, VK.JIYUU_CD_08, VK.JIYUU_CD_09 ")
			.append("FROM ")
			.append("SST_SATEI_STAT SS LEFT JOIN ")
			.append("SSE_TAIHI TT ON ")
			.append("SS.ym = TT.ym ")
			.append("AND SS.mise_cd = TT.office_cd ")
			.append("AND TRIM(SS.kikan_tori_cd) || '00' = TT.kikan_tori_cd ")
			.append("AND SS.system_kbn = TT.system_kbn LEFT JOIN ")
			.append("SSM_SATEIKAISYA MS ON ")
			.append("TRIM(SS.kaisha7_cd) = TRIM(MS.kaisha_cd) LEFT JOIN ")
			.append("SSE_TOGO_MST TM ON ")
			.append("TT.ym = TM.ym ")
			.append("AND TT.sikibetu_cd = TM.sikibetu_cd ")
			.append("AND TT.togo_tori_cd = TM.togo_tori_cd ")
			.append("AND TT.syori_kaisu = TM.syori_kaisu LEFT JOIN ")
			.append("SSE_DB_KIHON_TBL DK ON ")
			.append("DK.ym = TT.ym ")
			.append("AND DK.sikibetu_cd = TT.sikibetu_cd ")
			.append("AND DK.duns_no = TT.togo_tori_cd ")
			.append("AND DK.syori_kaisu = TT.syori_kaisu LEFT JOIN ")
			.append("VW_SS_KENTOUTAISYOU VK ON ")
			.append("SS.ym = VK.ym ")
			.append("AND SS.mise_cd = VK.mise_cd ")
			.append("AND TRIM(SS.kikan_tori_cd) || '00'= VK.tori_cd ")
			.append("AND SS.system_kbn = VK.system_kbn ")
			//要件No.四-16
			//追加開始
			.append("AND SS.hanki_sihanki_kbn = VK.hanki_sihanki_kbn ")
			//追加完了
			.append("WHERE SS.anken_no = '")
			.append(satei_anken_no_syokai)
			.append("'");
			
			// SQL実行		
			rs1 = sqlExec.execQuery(sql1.toString());
			
			// 障害票：　チェックイン日：2008/6/14　SJA中島　滞留区分名称を区分テーブルから取得
			// SQL作成(初回、中間月データ取得)
			StringBuffer sql2 = new StringBuffer()
			.append("SELECT ")
			.append("DK.LINE_BUSINESS, ")
			.append("SI.SIC_NM_KJ, ")
			.append("SI.SIC_NM_E, ")
			.append("ST.jigyonaiyo, ")
			.append("ST.kabunusi_nm1, ST.kabunusi_nm2, ST.kabunusi_nm3, ST.kabunusi_nm4, ST.kabunusi_nm5, ")
			.append("ST.kabusu1, ST.kabusu2, ST.kabusu3, ST.kabusu4, ST.kabusu5, ")
			.append("ST.hiritu1, ST.hiritu2, ST.hiritu3, ST.hiritu4, ST.hiritu5, ")
			.append("KTK.val AS tairyukbn, ")
			.append("KTKN.val AS tairyu_kbn_nm, ")
			.append("KTS.val AS saikenkbn, ")
			// 課題No.60，73
			// 修正開始
			//.append("ST.seijo_chk, ")
			//.append("ST.yochui_chk, ST.tyoka_chk, ST.kanwa_chk, ST.entai_chk, ST.hasanho_chk, ST.kaishaho_chk, ST.koseho_chk, ST.saiseho_chk, ST.shobun_chk, ST.sonota_chk, ")
			.append("ST.torihikisaki_kbn, ")
			// 修正完了
			.append("ST.ryuhosaimu, ST.oth_ryuhosaimu, ST.hozen, ST.sonotakaisyu, ST.riko_kenen, ST.tuika_hikiate, ST.hikiate_hosei, ")
			.append("ST.hudosan_k, ST.hudosan_h, ST.dosan_k, ST.dosan_h, ST.hoken_k, ST.hoken_h, ST.sonota_k, ST.sonota_h ")
			.append("FROM ")
			.append("SST_SATEI_STAT SS LEFT JOIN ")
			.append("SSE_TAIHI TT ON ")
			.append("SS.ym = TT.ym AND ")
			.append("SS.mise_cd = TT.office_cd AND ")
			.append("TRIM(SS.kikan_tori_cd)|| '00' = TT.kikan_tori_cd AND ")
			.append("SS.system_kbn = TT.system_kbn LEFT JOIN ")
			.append("TM_DB_KIHON_TBL@VIR_SJLMA DK ON ")
			.append("TT.togo_tori_cd = DK.DUNS_NO LEFT JOIN ")
			.append("TM_DB_SIC_MST@VIR_SJLMA SI ON ")
			.append("substr(DK.SIC_CD1,1,2) = SI.SIC_MID_CD LEFT JOIN ")
			.append("SST_SATEI ST ON ")
			.append("SS.anken_no = ST.anken_no and ")
			.append("SS.phase=ST.phase LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn_nm' AND lang_mode ='").append(lang_mode).append("') KTKN ON ")
			.append("ST.tairyu_kbn = KTKN.kbn_val LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn' AND lang_mode ='").append(lang_mode).append("') KTK ON ")
			.append("ST.tairyu_kbn = KTK.kbn_val LEFT JOIN ")
			.append("(SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'saiken_kbn' AND lang_mode ='").append(lang_mode).append("') KTS ON ")
			.append("ST.saiken_kbn = KTS.kbn_val ")
			.append("WHERE ")
			.append("SS.anken_no = '").append(satei_anken_no_syokai).append("'");
			// SQL実行(初回、中間月データ取得)		
			rs2 = sqlExec.execQuery(sql2.toString());
			
			
			// SQL作成(初回、中間月データ取得)
			StringBuffer sql5 = new StringBuffer()
			.append("SELECT ")
			.append("CM.comment_val, ")
			.append("CM.toroku_point ")
			.append("FROM ")
			.append("SST_SATEI_STAT SS LEFT JOIN ")
			.append("SST_COMMENT CM ON ")
			.append("SS.anken_no = CM.anken_no AND ")
			.append("SS.phase = CM.phase ")
			.append("WHERE ")
			.append("SS.anken_no ='").append(satei_anken_no_syokai).append("' ")
			.append("AND toroku_point IN('80')");
			// SQL実行(初回、中間月データ取得)		
			rs5 = sqlExec.execQuery(sql5.toString());
			
			// SQL作成(初回、中間月データ取得)
			StringBuffer sql6 = new StringBuffer()
			.append("SELECT ")
			.append("HH.kingaku, ")
			.append("HH.kanjo_hyouji_kbn ")
			.append("FROM ")
			.append("SST_SATEI_STAT SS LEFT JOIN ")
			.append("SST_HIKIATEHANTEI HH ON ")
			.append("SS.anken_no = HH.satei_anken_no AND ")
			.append("SS.system_kbn = HH.system_kbn AND ")
			.append("SS.satei_kaisha_cd = HH.sateikaisya_cd AND ")
			.append("SS.mise_cd = HH.mise_cd AND ")
			.append("SS.ym = HH.ym AND ")
			.append("trim(SS.kikan_tori_cd) = trim(HH.tori_cd) ")
			.append("WHERE SS.anken_no ='").append(satei_anken_no_syokai).append("' ")
			.append("AND kanjo_hyouji_kbn IN('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16')");
			
			// SQL実行(初回、中間月データ取得)		
			rs6 = sqlExec.execQuery(sql6.toString());
			
			
			// SQL作成(最終月データ取得)
			// 課題No.60，73
			// 修正開始
			//StringBuffer sql7 = new StringBuffer().append("SELECT SS.status,DK.LINE_BUSINESS, SI.SIC_NM_KJ, SI.SIC_NM_E, ST.jigyonaiyo, ST.kabunusi_nm1, ST.kabunusi_nm2, ST.kabunusi_nm3, ST.kabunusi_nm4, ST.kabunusi_nm5, ST.kabusu1, ST.kabusu2, ST.kabusu3, ST.kabusu4, ST.kabusu5, ST.hiritu1, ST.hiritu2, ST.hiritu3, ST.hiritu4, ST.hiritu5, KTK.val AS tairyukbn, ST.tairyu_kbn_nm, KTS.val AS saikenkbn, ST.seijo_chk, ST.yochui_chk, ST.tyoka_chk, ST.kanwa_chk, ST.entai_chk, ST.hasanho_chk, ST.kaishaho_chk, ST.koseho_chk, ST.saiseho_chk, ST.shobun_chk, ST.sonota_chk, ST.ryuhosaimu, ST.oth_ryuhosaimu, ST.hozen, ST.sonotakaisyu, ST.riko_kenen, ST.tuika_hikiate, ST.hikiate_hosei, ST.hudosan_k, ST.hudosan_h, ST.dosan_k, ST.dosan_h, ST.hoken_k, ST.hoken_h, ST.sonota_k, ST.sonota_h FROM SST_SATEI_STAT SS LEFT JOIN SSE_TAIHI TT ON SS.ym = TT.ym AND SS.mise_cd = TT.office_cd AND TRIM(SS.kikan_tori_cd)|| '00' = TT.kikan_tori_cd AND SS.system_kbn = TT.system_kbn LEFT JOIN TM_DB_KIHON_TBL@VIR_SJLMA DK ON TT.togo_tori_cd = DK.DUNS_NO LEFT JOIN TM_DB_SIC_MST@VIR_SJLMA SI ON substr(DK.SIC_CD1,1,2) = SI.SIC_MID_CD LEFT JOIN SST_SATEI ST ON SS.anken_no = ST.anken_no and SS.phase=ST.phase LEFT JOIN (SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn' AND lang_mode ='").append(lang_mode).append("') KTK ON ST.tairyu_kbn = KTK.kbn_val LEFT JOIN (SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'saiken_kbn' AND lang_mode ='").append(lang_mode).append("') KTS ON ST.saiken_kbn = KTS.kbn_val WHERE anken_no = '").append(anken_no).append("'");
			StringBuffer sql7 = new StringBuffer().append("SELECT SS.status,DK.LINE_BUSINESS, SI.SIC_NM_KJ, SI.SIC_NM_E, ST.jigyonaiyo, ST.kabunusi_nm1, ST.kabunusi_nm2, ST.kabunusi_nm3, ST.kabunusi_nm4, ST.kabunusi_nm5, ST.kabusu1, ST.kabusu2, ST.kabusu3, ST.kabusu4, ST.kabusu5, ST.hiritu1, ST.hiritu2, ST.hiritu3, ST.hiritu4, ST.hiritu5, KTK.val AS tairyukbn, ST.tairyu_kbn_nm, KTS.val AS saikenkbn, NVL(ST.henkogo_torihikisaki_kbn,ST.torihikisaki_kbn) AS torihikisaki_kbn, ST.ryuhosaimu, ST.oth_ryuhosaimu, ST.hozen, ST.sonotakaisyu, ST.riko_kenen, ST.tuika_hikiate, ST.hikiate_hosei, ST.hudosan_k, ST.hudosan_h, ST.dosan_k, ST.dosan_h, ST.hoken_k, ST.hoken_h, ST.sonota_k, ST.sonota_h FROM SST_SATEI_STAT SS LEFT JOIN SSE_TAIHI TT ON SS.ym = TT.ym AND SS.mise_cd = TT.office_cd AND TRIM(SS.kikan_tori_cd)|| '00' = TT.kikan_tori_cd AND SS.system_kbn = TT.system_kbn LEFT JOIN TM_DB_KIHON_TBL@VIR_SJLMA DK ON TT.togo_tori_cd = DK.DUNS_NO LEFT JOIN TM_DB_SIC_MST@VIR_SJLMA SI ON substr(DK.SIC_CD1,1,2) = SI.SIC_MID_CD LEFT JOIN SST_SATEI ST ON SS.anken_no = ST.anken_no and SS.phase=ST.phase LEFT JOIN (SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'tairyu_kbn' AND lang_mode ='").append(lang_mode).append("') KTK ON ST.tairyu_kbn = KTK.kbn_val LEFT JOIN (SELECT TRIM(kbn_val) AS kbn_val, kbn_hyouji_val AS val FROM SSP_KBN WHERE kbn_key = 'saiken_kbn' AND lang_mode ='").append(lang_mode).append("') KTS ON NVL(ST.henkogo_saiken_kbn,ST.saiken_kbn) = KTS.kbn_val WHERE SS.anken_no = '").append(anken_no).append("'");
			// 修正完了
			// SQL実行(最終月データ取得)	
			rs7 = sqlExec.execQuery(sql7.toString());
			
			
			// SQL作成(最終月データ取得)
			StringBuffer sql8 = new StringBuffer().append("SELECT CM.comment_val, CM.toroku_point FROM SST_SATEI_STAT SS LEFT JOIN SST_COMMENT CM ON SS.anken_no = CM.anken_no AND SS.phase = CM.phase WHERE SS.anken_no ='").append(anken_no).append("' AND toroku_point IN('00')");
			
			// SQL実行(最終月データ取得)		
			rs8 = sqlExec.execQuery(sql8.toString());
			
			// SQL作成(最終月データ取得)
			//StringBuffer sql9 = new StringBuffer().append("SELECT HS.kingaku, HS.kanjo_hyouji_kbn FROM SST_SATEI_STAT SS LEFT JOIN SST_KENSYOUSUM HS ON SS.anken_no = HS.satei_anken_no AND SS.system_kbn = HS.system_kbn AND SS.satei_kaisha_cd = HS.sateikaisya_cd AND SS.mise_cd = HS.mise_cd AND SS.ym = HS.ym AND trim(SS.kikan_tori_cd) = trim(HS.tori_cd) WHERE SS.anken_no ='").append(anken_no).append("' AND HS.kanjo_hyouji_kbn IN('01','02','03','04','05','06','07','08','09','10','11','12','13','14','15','16')");
			///////////////////////////////////////////////
			//障害票：413
			//チェックイン日：2008/5/28
			//対応者：SJA中島
			//概要：引当金検証サマリの検索条件を査定案件Noから、システム区分、査定会社コード、店コード、年月、処理回数、取引先コードに変更。
			///////////////////////////////////////////////

			StringBuffer sql9 = new StringBuffer()
								.append(" select ")
								.append(" HS.kanjo_hyouji_kbn, ")
								.append(" HS.kingaku ")
								.append(" from ")
								.append(" SST_KENSYOUSUM HS ")
								.append(" where ")
								.append(" HS.system_kbn = '")
								.append(cmnData.getSystem_kbn())
								.append("' and HS.sateikaisya_cd = '")
								.append(satei_kaisya)
								.append("' and HS.mise_cd = '")
								.append(cmnData.getMise_cd())
								.append("' and HS.ym = '")
								.append(ym)
								.append("' and HS.syori_kaisu = '") 
								.append(syori_cnt)
								.append("' and HS.tori_cd = '")
								.append(cmnData.getKanjo_cd())
								.append("' and HS.kanjo_hyouji_kbn in(")
								.append("'01','02','03','04','05','06','07','08','09','10','11','12','14','15','16'")
								.append(")");

			
			// SQL実行(最終月データ取得)		
			rs9 = sqlExec.execQuery(sql9.toString());

			// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
			// 引当金検証の一時保存有無取得
			boolean isSave = isIchijiSave();
			
			// エクセルの一時ファイルを作成する
			sdfYMD = new SimpleDateFormat("yyyyMMdd");
			
			String suffix = sdfYMD.format(new Date());
			File dir = new File(AppContext.getTmpDir());
			
			if( dir.isDirectory()==false ) {
				dir.mkdir();
			}
			
			tmpExcel = File.createTempFile(suffix+".",".xls",dir);
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
			
			fileOut = new FileOutputStream( tmpExcel.getAbsolutePath() );
           
			// ワークシートを変更する
			wb.setSheetName(0,sname);    
          
			// セルオブジェクトの作成（セル番号は0スタート）
			// ヘッダ部分の作成(1行目は項目名称)
			HSSFCell[] cellHd = new HSSFCell[100];
           
//障害票No766 2008/06/10 細野 フェーズが引当金検証時DBエラーとなる件の対応
			if(rs1.next()){
				rs1_flg = true;
			}
//障害票No766 2008/06/09 細野 フェーズが引当金検証時DBエラーとなる件の対応
			if(rs2.next()){
				rs2_flg = true;
			}
//障害票No766 2008/06/10 細野 フェーズが引当金検証時DBエラーとなる件の対応
			if(rs4.next()){
				rs4_flg = true;
			}
			if(rs5.next()){
				rs5_flg = true;
			}

//障害票No766 2008/06/09 細野 フェーズが引当金検証時DBエラーとなる件の対応
			if(rs7.next()){
				rs7_flg = true;
			}
           
			if(rs8.next()){
				rs8_flg = true;
			}
			
			HSSFSheet sheetW = wb.getSheetAt(0);

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

				// 見出し
				if(i == 0){
					// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						cellHd[29].setCellValue( Function.insertYmNengetu(ym)+ "期決算資料");
					} else {
						// 課題No.158
						// 追加開始
						//cellHd[29].setCellValue("Closing of Accounts Material of Period on " +Function.insertYmSlash(ym) );
						cellHd[29].setCellValue("Closing of Accounts Material of Period on " +Function.insertYmSlash(ym,lang_mode) );
						// 追加完了
					}
					
				// 日付行
				}else if(i == 1){
					String sysdate = sdfYMD.format(new Date());
					String yyyymm = Function.insertYmNengetu(sysdate.substring(0,6));
					// 課題No.158
					// 追加開始
					//String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8));
					String yyyymmdd = Function.insertDateSlash(sysdate.substring(0,8),lang_mode);
					// 追加完了
					String dd = sysdate.substring(6,8);
					//処理日時(査定内容詳細よりダウンロード時　かつ　ステータスが'40'の場合　処理日時を表示する)
					String syori_dt = ny(sysdate,lang_mode);
					// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						if(mode == 0){
							// 照会の場合
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(rs7_flg && GS.STATUS_KANRYO.equals(rs7.getString("status"))){
								//処理日時を表示(日本語)
								cellHd[29].setCellValue(syori_dt);							
							}else{
								cellHd[29].setCellValue("");
							}
							
						}else{
							// 引当金判定の場合
							cellHd[29].setCellValue( yyyymm + dd + "日" + "　作成");
						}
					} else {
						if(mode == 0){
							// 照会の場合
							// No427, 2008/06/14, SJA渡辺, フェーズ、ステータス40を定数化
							if(rs7_flg && GS.STATUS_KANRYO.equals(rs7.getString("status"))){
								//処理日時を表示(英語)
								cellHd[29].setCellValue(syori_dt);							
							}else{
								cellHd[29].setCellValue("");
							}
							
						}else{
							
							// 引当金判定の場合
							// 課題No.117 
							// 追加開始
							//cellHd[29].setCellValue( yyyymmdd + "　Creation");
							cellHd[29].setCellValue( yyyymmdd + "  Creation");
							// 追加完了
						}
					}
	            	
				// 査定会社名
				}else if(i == 2){
					// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						if (rs1_flg && rs1.getString("satei_kaisya_nm") != null) {
							cellHd[29].setCellValue(rs1.getString("satei_kaisya_nm"));
						}
					} else {
						if (rs1_flg && rs1.getString("satei_kaisya_nm_e") != null) {
							cellHd[29].setCellValue(rs1.getString("satei_kaisya_nm_e"));
						}
					}

				// タイトル行	
				}else if(i == 3){
					// No526, 2008/06/04, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						cellHd[1].setCellValue("引当金 検証シート (" + Function.insertYmNengetu(ym)+ "末基準)");
					} else {
						// 課題No.158
						// 追加開始
						// cellHd[1].setCellValue("Verification of Reserves Sheet (On the basis of " + Function.insertYmSlash(ym)+ ")");
						cellHd[1].setCellValue("Verification of Reserves Sheet (On the basis of " + Function.insertYmSlash(ym,lang_mode)+ ")");
						// 追加完了
					}

				// フェーズ行
				}else if(i == 4){
					if(mode == 0){
						// 照会の場合
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[29].setCellValue("＜対象先選定結果＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[29].setCellValue("＜クレーム債権再設定結果＞");
								}else {
									cellHd[29].setCellValue("＜引当金検証結果＞");
								}								
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[29].setCellValue("＜対象先選定中＞");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[29].setCellValue("＜クレーム債権再設定中＞");
								}else {
									cellHd[29].setCellValue("＜引当金検証中＞");
								}								
							}
						} else {
							if(cmnData.getStatus().equals(GS.STATUS_KANRYO)){
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[29].setCellValue("<Select Customer Result>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[29].setCellValue("<Judgment of Claim Debt Result>");
								} else {
									cellHd[29].setCellValue("<Verification Result>");
								}
							}else{
								if(cmnData.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI) || cmnData.getPhase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
									cellHd[29].setCellValue("<Select Customer Processing>");
								}else if(cmnData.getC_phase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) || cmnData.getPhase().equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI)){
									cellHd[29].setCellValue("<Judgment of Claim Debt Processing>");
								} else {
									cellHd[29].setCellValue("<Verification Processing>");
								}
							}
						}
					}else{
						// 一次二次の場合
						// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
						if ("Ja".equals(lang_mode)) {
							cellHd[29].setCellValue("＜引当金検証中＞");
						} else {
							cellHd[29].setCellValue("<Verification of Reserves>");
						}
					}
					
				// 取引先CD、DUNS　No、判定事由(登録区分30)
				}else if(i == 7){
					if(rs1_flg){
						cellHd[4].setCellValue(rs1.getString("kikan_tori_cd"));
						cellHd[11].setCellValue(rs1.getString("togo_tori_cd"));
					}

				// 取引先名、信用格付、信用格付、親会社一体判断
				}else if(i == 8){
					if(rs1_flg){
						if(lang_mode.equals("Ja")==true){
							if(rs1.getString("business_nm_kj") == null){
								cellHd[4].setCellValue(rs1.getString("business_nm"));
							}else{
								cellHd[4].setCellValue(rs1.getString("business_nm_kj"));
							}
						}else{
							cellHd[4].setCellValue(rs1.getString("business_nm"));
						}
					}
					if(rs4_flg && rs4.getString("ktk") != null){
						cellHd[17].setCellValue(rs4.getString("ktk"));
					}
					
					// 親会社がある場合に親格付けを表示する
					if (rs4_flg && rs4.getString("oya_duns_no" ) != null && Function.trim(rs4.getString("oya_duns_no" )).length() > 0) {
						if(rs4_flg && rs4.getString("oya_ktk") != null){
							cellHd[19].setCellValue(rs4.getString("oya_ktk"));
						}
					}
					if(rs4_flg){
						if(lang_mode.equals("Ja")==true){
							if(rs4.getString("BUSINESS_NM_KJ") == null){
								if(rs4.getString("BUSINESS_NM") != null){
									cellHd[21].setCellValue(rs4.getString("BUSINESS_NM"));
								}
							}else{
								cellHd[21].setCellValue(rs4.getString("BUSINESS_NM_KJ"));
							}
						}else{
							if(rs4.getString("BUSINESS_NM") != null){
								cellHd[21].setCellValue(rs4.getString("BUSINESS_NM"));
							}
						}
					}
					
					// 親会社がある場合に一体、独立フラグは表示する
					// 親会社一体フラグと独立フラグが同時になりたつことはない。
					// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if (rs4_flg && rs4.getString("oya_duns_no" ) != null && Function.trim(rs4.getString("oya_duns_no" )).length() > 0) {
						if(rs4.getString("oya_ittai_flg") != null){
							if(rs4.getString("oya_ittai_flg").equals("1")==true){
								cellHd[26].setCellValue(rs4.getString("oya_ittai_flg_nm"));
							}
						}	
						if(rs4.getString("oya_dokuritu_flg") != null){
							if(rs4.getString("oya_dokuritu_flg").equals("1")==true){
								cellHd[26].setCellValue(rs4.getString("oya_dokuritu_flg_nm"));
							}
						}
					}

				// 滞留区分、名称
				}else if(i == 9){
					if(rs2_flg){
						cellHd[19].setCellValue(rs2.getString("tairyukbn"));
						cellHd[21].setCellValue(rs2.getString("tairyu_kbn_nm"));
					}

				// 基準、基準（仮〆）
				}else if(i == 11){
					// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						cellHd[1].setCellValue( Function.insertYmNengetu(before_ym)+ "末基準");
					} else {
						// 課題No.158
						// 追加開始
						//cellHd[1].setCellValue("On the basis of " + Function.insertYmSlash(before_ym) );
						cellHd[1].setCellValue("On the basis of " + Function.insertYmSlash(before_ym,lang_mode) );
						// 追加完了
					}

					// 障害票：413 2008/5/20 細野 基準日表示対応
					// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						if("1".equals(syori_cnt)){
							cellHd[16].setCellValue( Function.insertYmNengetu(ym)+ "末基準（仮〆）");
						}else if("3".equals(syori_cnt)){
							cellHd[16].setCellValue( Function.insertYmNengetu(ym)+ "末基準（本〆）");
						}else if("4".equals(syori_cnt)){
							cellHd[16].setCellValue( Function.insertYmNengetu(ym)+ "末基準（〆後修正）");
						}
					} else {
						if("1".equals(syori_cnt)){
							// 課題No.158
							// 追加開始
							//cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym)+ " (Closing 1)");
							cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym,lang_mode)+ " (Closing 1)");
							// 追加完了
						}else if("3".equals(syori_cnt)){
							// 課題No.158
							// 追加開始
							//cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym)+ " (Closing 3)");
							cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym,lang_mode)+ " (Closing 3)");
							// 追加完了
						}else if("4".equals(syori_cnt)){
							// 課題No.158
							// 追加開始
							//cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym)+ " (Closing 4)");
							cellHd[16].setCellValue("On the basis of " + Function.insertYmSlash(ym,lang_mode)+ " (Closing 4)");
						}
					}

				// 取引区分、債権区分
				}else if(i == 14){
					
					// No526, 2008/06/02, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					String kbnVal = "0";
					String kbnVal_kijun = "0";
					// 取引先区分
					if(rs2_flg){
						// 課題No.60,73
						// 修正開始
						/*if ("1".equals(rs2.getString("seijo_chk"))) {
							kbnVal = "1";
						} else if ("1".equals(rs2.getString("yochui_chk"))) {
							kbnVal = "2";
						} else if ("1".equals(rs2.getString("tyoka_chk")) || 
								"1".equals(rs2.getString("kanwa_chk")) || 
								"1".equals(rs2.getString("entai_chk"))) {
							kbnVal = "3";
						} else if ("1".equals(rs2.getString("hasanho_chk")) || 
								"1".equals(rs2.getString("kaishaho_chk")) || 
								"1".equals(rs2.getString("koseho_chk")) || 
								"1".equals(rs2.getString("saiseho_chk")) || 
								"1".equals(rs2.getString("shobun_chk")) || 
								"1".equals(rs2.getString("sonota_chk"))) {
							kbnVal = "4";
						}*/
						kbnVal = Function.trim(rs2.getString("torihikisaki_kbn"));
						kbnVal_kijun = Function.trim(rs7.getString("torihikisaki_kbn"));

						String toriKbnNm = getTorihikiKbnNm("torihikisaki2_kbn",kbnVal,lang_mode);
						cellHd[4].setCellValue(toriKbnNm);
						String toriKbnNm_kijun = getTorihikiKbnNm("torihikisaki2_kbn",kbnVal_kijun,lang_mode);
						//cellHd[19].setCellValue(toriKbnNm);
						cellHd[19].setCellValue(toriKbnNm_kijun);

						cellHd[11].setCellValue(rs2.getString("saikenkbn"));
						//cellHd[26].setCellValue(rs2.getString("saikenkbn"));
						cellHd[26].setCellValue(rs7.getString("saikenkbn"));
						// 修正完了
					}

				// 基準月
				}else if(i == 18){
					// No526, 2008/06/11, SJA渡辺, 英語モードの際、英語名称を表示するように修正
					if ("Ja".equals(lang_mode)) {
						cellHd[16].setCellValue("□引当金判定 (" + Function.insertYmNengetu(ym)+ "末基準)");
					} else {
						// No526, 2008/06/13, SJA渡辺, 文字が入りきらないため、省略する
						//cellHd[16].setCellValue("□Calculation of Reserves (On the basis of " + Function.insertYmSlash(ym)+ ")");
						// 課題No.158
						// 追加開始
						//cellHd[16].setCellValue("   Calculation of Reserves (" + Function.insertYmSlash(ym)+ ")");
						cellHd[16].setCellValue("   Calculation of Reserves (" + Function.insertYmSlash(ym,lang_mode)+ ")");
						// 追加完了
					}
	
				// 受取手形
				}else if(i == 20){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("01") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("01") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}
					
				// 輸出受取手形
				}else if(i == 21){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("02") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("02") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 売掛金
				}else if(i == 22){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("03") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("03") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 取引前渡金
				}else if(i == 23){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("04") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("04") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 立替金
				}else if(i == 24){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("05") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("05") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 未収入金
				}else if(i == 25){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("06") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("06") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 未収収益
				}else if(i == 26){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("07") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("07") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 短期貸付金
				}else if(i == 27){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("08") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("08") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 差入保証金
				}else if(i == 28){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("09") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("09") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 仮払金
				}else if(i == 29){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("10") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("10") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 長期貸付金
				}else if(i == 30){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("11") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("11") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// その他投資
				}else if(i == 31){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("12") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("12") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 固定化営業債権
				}else if(i == 33){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("16") == true){
								//cellHd[9].setCellValue(rs6.getString("kingaku"));
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("16") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 留保債務
				}else if(i == 36){
					if(rs2_flg && rs2.getString("ryuhosaimu") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("ryuhosaimu")));
					}
					// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("ryuhosaimu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("ryuhosaimu")));						
						}
					} else {
						// No768, 2008/06/11, SJA渡辺, 査定結果照会画面の場合は表示しないように修正
						if(mode == 1 && rs2_flg && rs2.getString("ryuhosaimu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs2.getString("ryuhosaimu")));
						}
					}
					
				// 第三者留保債務
				}else if(i == 37){
					if(rs2_flg && rs2.getString("oth_ryuhosaimu") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("oth_ryuhosaimu")));						
					}
					// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("oth_ryuhosaimu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("oth_ryuhosaimu")));						
						}
					} else {
						// No768, 2008/06/11, SJA渡辺, 査定結果照会画面の場合は表示しないように修正
						if(mode == 1 && rs2_flg && rs2.getString("oth_ryuhosaimu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs2.getString("oth_ryuhosaimu")));						
						}
					}
					
				// 保全
				}else if(i == 39){
					if(rs2_flg && rs2.getString("hozen") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("hozen")));						
					}
					// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("hozen") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("hozen")));						
						}
					} else {
						// No768, 2008/06/11, SJA渡辺, 査定結果照会画面の場合は表示しないように修正
						if(mode == 1 && rs2_flg && rs2.getString("hozen") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs2.getString("hozen")));	
						}
					}
					
				// その他回収
				}else if(i == 40){
					if(rs2_flg && rs2.getString("sonotakaisyu") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("sonotakaisyu")));												
					}
					// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("sonotakaisyu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("sonotakaisyu")));						
						}
					} else {
						// No768, 2008/06/11, SJA渡辺, 査定結果照会画面の場合は表示しないように修正
						if(mode == 1 && rs2_flg && rs2.getString("sonotakaisyu") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs2.getString("sonotakaisyu")));												
						}
					}

				// 保証債務合計
				}else if(i == 42){
					String saimuKei = "";
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("14") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("14") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

				// 履行請求懸念
				}else if(i == 43){
					if(rs2_flg && rs2.getString("riko_kenen") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("riko_kenen")));												
					}
					// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("riko_kenen") != null){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("riko_kenen")));												
						}
					} else {
						// No768, 2008/06/11, SJA渡辺, 査定結果照会画面の場合は表示しないように修正
						if(mode == 1 && rs2_flg && rs2.getString("riko_kenen") != null){
							cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("riko_kenen")));												
						}
					}
					
				// 既引当金⑥
				}else if(i == 45){
					rs6.beforeFirst();
					while ( rs6.next() ) {
						if(rs6.getString("kanjo_hyouji_kbn") != null ){
							if(rs6.getString("kanjo_hyouji_kbn").equals("15") == true){
								cellHd[9].setCellValue(Function.getValueOfDouble(rs6.getString("kingaku")));
							}	
						}
					}	
					rs9.beforeFirst();
					while ( rs9.next() ) {
						if(rs9.getString("kanjo_hyouji_kbn") != null ){
							if(rs9.getString("kanjo_hyouji_kbn").equals("15") == true){
								cellHd[24].setCellValue(Function.getValueOfDouble(rs9.getString("kingaku")));
							}	
						}
					}

					
				// 引当金補正額
				}else if(i == 47){
					///////////////////////////////////////////////
					//障害票：413
					//チェックイン日：2008/5/28
					//対応者：SJA中島
					//概要：仮〆以外は引当金補正額を出力しないように修正。
					///////////////////////////////////////////////
					// No768, 2008/06/10, SJA渡辺, 一次保存されていなければ表示しないように修正
					if (isSave) {
						if(rs7_flg && rs7.getString("hikiate_hosei") != null && "1".equals(syori_cnt)){
							cellHd[24].setCellValue(Function.getValueOfDouble(rs7.getString("hikiate_hosei")));												
						}
					}

				// 追加引当金額
				}else if(i == 48){
					if(rs2_flg && rs2.getString("tuika_hikiate") != null){
						cellHd[9].setCellValue(Function.getValueOfDouble(rs2.getString("tuika_hikiate")));												
					}
				}else if(i==52){
//障害票No470 2008/05/28 細野 セル位置の修正2⇒1
					// No483, 2008/05/29, SJA渡辺, 改行前に余分な中黒(・)が表示されないように修正
					// No483, 2008/06/01, SJA平道, タブ前に余分な中黒(・)が表示されないように修正
					if(rs5_flg && rs5.getString("comment_val") != null){
						cellHd[1].setCellValue(rs5.getString("comment_val").replaceAll("\r\n","\n").replaceAll("\t"," "));						
					}

					if(rs8_flg && rs8.getString("comment_val") != null){
						cellHd[16].setCellValue(rs8.getString("comment_val").replaceAll("\r\n","\n").replaceAll("\t"," "));												
					}

				}
				
				
			}
			//作成したワークブックを保存する
			wb.write(fileOut);
           
			fileOut.flush();


		// 作成したエクセルファイルのダウンロード用tmpファイルを作成する
		tmp = new TempFile(fname);
		

		
		int contents = 0;

		// 一時ファイルに、エクセル情報を書き込む

			fis = new FileInputStream(tmpExcel.getAbsolutePath());

			is = new BufferedInputStream (fis);
			os = new FileOutputStream(tmp.getPath());
       	
			while ((contents = is.read()) != -1){
				os.write(contents);
			}
			os.flush();
			
		} finally {
			// No786, 2008/06/05, SJA渡辺, fileOutがnullチェック追加
			if (fileOut != null) {
				fileOut.close();
			}
           
			// Resultset close
			if(rs != null) {
				rs.close();
			}
			if(rs1 != null) {
				rs1.close();
			}
			if(rs2 != null) {
				rs2.close();
			}
			if(rs4 != null) {
				rs4.close();
			}
			if(rs5 != null) {
				rs5.close();
			}
			if(rs6 != null) {
				rs6.close();
			}
			if(rs7 != null) {
				rs7.close();
			}
			if(rs8 != null) {
				rs8.close();
			}
			if(rs9 != null) {
				rs9.close();
			}
			if(rs10 != null) {
				rs10.close();
			}
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
			KensyoForm form = (KensyoForm)appContext.getActionForm();
			acc.execute( new ActionMapping(), form, req, res );
		}
	    
		tmp.delete();
	}
	
	/**
	 * 取引先区分名称を取得する
	 */
	private String getTorihikiKbnNm(String kbn_key, String kbn_val, String langMode) throws SQLException {
		
		ResultSet rs = null;
		
		String result = "";
		
		try {
		
			StringBuffer sql = new StringBuffer().append("SELECT KBN_HYOUJI_VAL")
												.append(" FROM SSP_KBN")
												.append(" WHERE KBN_KEY='")
												.append(kbn_key)
												.append("' and LANG_MODE='")
												.append(langMode)
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
	
	/**
	 * 一時保存されたことがあるか判定
	 * 
	 * @exception SQLException
	 */
	private boolean isIchijiSave() throws SQLException {
		// No768, 2008/06/08, SJA平道, 一時保存前の入力項目には初回月・中間月最終確定の金額を表示するように修正
		boolean result = false;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer().append("SELECT COUNT(CM.anken_no) AS cnt")
											.append(" FROM SST_COMMENT CM")
											.append(" WHERE CM.anken_no='")
											.append(anken_no)
											.append("' and CM.phase='70'")
											.append(" and CM.toroku_point='00'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			if (rs.next()) {
				if(rs.getInt("cnt") > 0) {
					result = true;
				}
			}
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return result;
	}
	
	/**
	 * 入力履歴より、処理日時を取得する
	 * 障害対応IT512(2009/9/16)
	 */
	private String ny(String sysdate, String lang_mode) throws SQLException {
		// 入力履歴より、処理日時を取得する
		String syonin_dt = "syonin_dt";
		String result = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer().append("SELECT PG_SS_FUNCTION.SF_SS_GETCHOHYODT2(SUBSTR(N.syori_dt,0,8),'")
											.append(lang_mode)
											.append("') AS syonin_dt")
											.append(" FROM SST_NYURYOKU_HIST N")
											.append(" WHERE N.anken_no='")
											.append(anken_no)
											.append("' and N.phase='70'")
											.append(" and N.ope_kbn='80'");
		try {
			rs = sqlExec.execQuery(sql.toString());
			if (rs.next()) {
				result = rs.getString(syonin_dt);
			}
		} finally {
			if (rs != null) {
				try {
					//Resultset close
					rs.close();
				} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return result;
	}
}