/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2023/03/02		NELCO			新規作成
******************************************************************************/
package app.print.dbAcc;

import app.system.form.DownloadForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* LD2102 引当金額集計表（検証結果）  DBアクセスクラス
*/
public class KenshokekkaHonsyaExcelDbAcc extends CommonDbAcc {

	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	// 課題No.193
	// 追加開始
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	// 追加完了


	//引当金検証データ取得項目
	private static final String TAISYO_YM					= "taisyo_ym";
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	private static final String INIT_BUNRUI2            	= "init_bunrui2";
	private static final String HONBU_CD            	= "honbu_cd";
	private static final String INIT_BU_CD            	= "init_bu_cd";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String WB_COUNTRY_NM            	= "wb_country_nm";
	private static final String KTK             			= "ktk";
	private static final String BU_NM            			= "bu_nm";
	private static final String RYUHOSAIMU				= "ryuhosaimu";
	private static final String OTH_RYUHOSAIMU			= "oth_ryuhosaimu";
	private static final String HOZEN						= "hozen";
	private static final String SONOTAKAISYU				= "sonotakaisyu";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String HIKIATE_HOSEI				= "hikiate_hosei";
	private static final String UKETORI_TEGATA			= "uketori_tegata";
	private static final String YUSYUTU_UKETORI_TEGATA	= "yusyutu_uketori_tegata";
	private static final String URIKAKEKIN				= "urikakekin";
	private static final String TORIHIKI_MAEWATASHIKIN	= "torihiki_maewatashikin";
	private static final String TATEKAEKIN				= "tatekaekin";
	private static final String MISYUUNYUUKIN				= "misyuunyuukin";
	private static final String MISYUUSYUEKI				= "misyuusyueki";
	private static final String TANKI_KASITUKEKIN			= "tanki_kasitukekin";
	private static final String SASHIIRE_HOSYOKIN			= "sashiire_hosyokin";
	private static final String KARIBARAIKIN				= "karibaraikin";
	private static final String TYOKI_KASITUKEKIN			= "tyouki_kasitukekin";
	private static final String SONOTA_TOUSI				= "sonota_tousi";
	private static final String KOTEIKA_EIGYOU_SAIKEN		= "koteika_eigyou_saiken";
	private static final String HOSYO_SAIMUKEI			= "hosyo_saimukei";
	private static final String KIHIKIATEKIN				= "kihikiatekin";
	private static final String HIKIATEKIN_HANTEIKONKYO	= "hikiatekin_hanteikonkyo";
	private static final String SYORI_DT					= "syori_dt";
	private static final String TORIHIKISAKI_KBN         	= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";

	//査定データ取得項目
	private static final String TAISYO_YM_1				= "taisyo_ym_1";
	private static final String KTK_1             		= "ktk_1";
	private static final String OYA_KTK_1					= "oya_ktk_1";
	private static final String OYA_BUSINESS_NM_1			= "oya_business_nm_1";
	private static final String KESSANKI_1				= "kessanki_1";
	private static final String RYUHOSAIMU_1				= "ryuhosaimu_1";
	private static final String OTH_RYUHOSAIMU_1			= "oth_ryuhosaimu_1";
	private static final String HOZEN_1					= "hozen_1";
	private static final String SONOTAKAISYU_1			= "sonotakaisyu_1";
	private static final String RIKO_KENEN_1				= "riko_kenen_1";
	private static final String TUIKA_HIKIATE_1			= "tuika_hikiate_1";
	private static final String UKETORI_TEGATA_1			= "uketori_tegata_1";
	private static final String YUSYUTU_UKETORI_TEGATA_1	= "yusyutu_uketori_tegata_1";
	private static final String URIKAKEKIN_1				= "urikakekin_1";
	private static final String TORIHIKI_MAEWATASHIKIN_1	= "torihiki_maewatashikin_1";
	private static final String TATEKAEKIN_1				= "tatekaekin_1";
	private static final String MISYUUNYUUKIN_1			= "misyuunyuukin_1";
	private static final String MISYUUSYUEKI_1			= "misyuusyueki_1";
	private static final String TANKI_KASITUKEKIN_1		= "tanki_kasitukekin_1";
	private static final String SASHIIRE_HOSYOKIN_1		= "sashiire_hosyokin_1";
	private static final String KARIBARAIKIN_1			= "karibaraikin_1";
	private static final String TYOKI_KASITUKEKIN_1		= "tyouki_kasitukekin_1";
	private static final String SONOTA_TOUSI_1			= "sonota_tousi_1";
	private static final String KOTEIKA_EIGYOU_SAIKEN_1	= "koteika_eigyou_saiken_1";
	private static final String HOSYO_SAIMUKEI_1			= "hosyo_saimukei_1";
	private static final String KIHIKIATEKIN_1			= "kihikiatekin_1";
	private static final String AKAJI						= "akaji";
	private static final String SAIMU_TYOKA				= "saimu_tyoka";
	private static final String M12						= "m12";
	private static final String KASIDAORE_KENEN			= "kasidaore_kenen";
	private static final String RIKI_SITEI				= "riki_sitei";
	private static final String HASAN_KOSEI				= "hasan_kosei";
	private static final String HIKIATEKIN_HANTEIKONKYO_1	= "hikiatekin_hanteikonkyo_1";
	private static final String SYORI_DT_1				= "syori_dt_1";
	private static final String TORIHIKISAKI_KBN_1		= "torihikisaki_kbn_1";
	private static final String SAIKEN_KBN_1   			= "saiken_kbn_1";
	private static final String TAIRYU_KBN_1				= "tairyu_kbn_1";
	private static final String RENKETSU_KBN   				= "renketsu_kbn";


	//案件No取得用

	private static final String WARNING0004 = "warning.0004";

	private static final int StartRow = 5;				//明細出力開始行
	private static final int startClm = 0;				//明細出力開始列
	private static final int endClm = 84;				//明細出力最終列

	//引当金検証データ計算式
	private static final String KEKKA_IPPAN_SAIKENGOKEI = "SUM(L{1}:W{1})";
	private static final String KEKKA_SAIKENZANDAGOKEI_A = "X{1}+Y{1}";
	private static final String KEKKA_RYUHOSAIMUKEI_C = "IF(Z{1}<SUM(AC{1}:AD{1}),IF(Z{1}<0,0,Z{1}),SUM(AC{1}:AD{1}))";
	private static final String KEKKA_HOSENKEI = "SUM(AE{1}:AG{1})";
	private static final String FORMULA5 = "Z{1}+AB{1}-(AE{1}+AF{1}+AG{1})-AI{1}";
	private static final String FORMULA6 = "AI{1}+AK{1}"; //
	//案件No.D9059 補正後引当控除後残高削除(空白)対応 コメント化
	//private static final String FORMULA7 = "AI{1}-AJ{1}";

	//査定データ計算式
	private static final String IPPANSAIKANGOKEI = "SUM(BE{1}:BP{1})";
	private static final String SAIKANZANDAGOKEI_A = "BQ{1}+BR{1}";
	private static final String FORMULA10 = "IF(BS{1}<SUM(BV{1}:BW{1}),IF(BS{1}<0,0,BS{1}),SUM(BV{1}:BW{1}))";
	private static final String FORMULA11 = "SUM(BX{1}:BZ{1})";
	private static final String FORMULA12 = "BS{1}+BU{1}-(BX{1}+BY{1}+BZ{1})-CB{1}";
	private static final String FORMULA13 = "CB{1}+CD{1}";

	//プロシージャ
	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD3103_SELECT_ICHIRAN 	= "SP_SS_LD3103_SELECT_ICHIRAN"; //一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;

	/**
	 * コンストラクタ
	 *
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */

	public KenshokekkaHonsyaExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		this.log = appContext.getLog();
		form = (DownloadForm)appContext.getActionForm();
		tyohyo_lang = form.getLangMode();

	}

	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		tyohyo_lang = GS.EMPTY_CHARCTER;
	}

	/**
	 * ヘッダ部情報取得 <br>
	 *
     * @param Excel
	 * @exception SQLException
	 */
	public void getHeader(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD2, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(Function.removeSingle(form.getSearch_sateikaisya_cd()));
	    exCstmt.setStringIn(Function.removeSingle(form.getSearch_system_kbn()));
	    exCstmt.setStringIn(form.toString());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()){
				//作成日
				excel.selectCell(1,84,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));

				// 課題No.193
				// 追加開始
				//分類１
				excel.selectCell(2,84,true);
				excel.setCellValue(rs.getString(BUNRUI1_NM));
				// 追加完了
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 一覧情報取得処理(引当金検証データ) <br>
	 *
     * @param Excel
	 * @exception SQLException
	 */
	public boolean getMeisai(Excel excel) throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		String formula = null;

		String ym_hantei = GS.EMPTY_CHARCTER;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD3103_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(form.getSime_kbn());
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setStringIn(form.getSearch_system_kbn());
		exCstmt.setStringIn(form.getSearch_sateikaisya_cd());
		exCstmt.setStringIn(form.getSearch_bunrui2());
		exCstmt.setStringIn(form.getSearch_taisyo_ym());
		exCstmt.setStringIn(form.getSearch_sateiki());
		exCstmt.setStringIn(form.getHanki_sihanki_kbn());
		exCstmt.setStringIn(Function.addSingleQuotation(form.getDuns_no()));
		exCstmt.setStringIn(form.getKanjo_cd());
		exCstmt.setStringIn(form.getKanjo_nm());
		exCstmt.setResultSet(RESULTSET);

		try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	rs = exCstmt.getResultSet(RESULTSET);

    		//明細開始行
	    	int i = StartRow;
	    	while ( rs.next() ) {

	    		result = true;
	    		//年月表示判定
	    		if(i==StartRow){
		    		ym_hantei = rs.getString(TAISYO_YM);
	    		}
	    		//明細に複数対象年月が存在する場合、ヘッダ部の年月は表示しない
	    		if(!ym_hantei.equals(rs.getString(TAISYO_YM))){
	    			ym_hantei = GS.EMPTY_CHARCTER;
	    		}
	    		excel.selectRow(i, true);

	    		//基本情報を設定
				for (int j=startClm; j<=endClm; j++ ) {
		    		excel.selectCell(j, true);
		    		excel.setCellStyle(j);
    				switch (j){
    					//引当金検証データ取得
	    				case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
	    				case 1: excel.setCellValue(rs.getString(TOGO_TORI_CD));break;
	    				case 2: excel.setCellValue(rs.getString(BUSINESS_NM));break;
	    				case 3: excel.setCellValue(rs.getString(WB_COUNTRY_NM));break;
	    				case 4: excel.setCellValue(rs.getString(INIT_BUNRUI2));break;
	    				case 5: excel.setCellValue(rs.getString(HONBU_CD));break;
	    				case 6: excel.setCellValue(rs.getString(INIT_BU_CD));break;
	    				case 7: excel.setCellValue(rs.getString(BU_NM));break;
	    				case 8: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
	    				case 9: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
	    				case 10: excel.setCellValue(rs.getString(KTK));break;
	    				case 11: excel.setCellValue(Function.getValueOfLong(rs.getString(UKETORI_TEGATA)));break;
	    				case 12: excel.setCellValue(Function.getValueOfLong(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
	    				case 13: excel.setCellValue(Function.getValueOfLong(rs.getString(URIKAKEKIN)));break;
	    				case 14: excel.setCellValue(Function.getValueOfLong(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
	    				case 15: excel.setCellValue(Function.getValueOfLong(rs.getString(TATEKAEKIN)));break;
	    				case 16: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUNYUUKIN)));break;
	    				case 17: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUSYUEKI)));break;
	    				case 18: excel.setCellValue(Function.getValueOfLong(rs.getString(TANKI_KASITUKEKIN)));break;
	    				case 19: excel.setCellValue(Function.getValueOfLong(rs.getString(SASHIIRE_HOSYOKIN)));break;
	    				case 20: excel.setCellValue(Function.getValueOfLong(rs.getString(KARIBARAIKIN)));break;
	    				case 21: excel.setCellValue(Function.getValueOfLong(rs.getString(TYOKI_KASITUKEKIN)));break;
	    				case 22: excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTA_TOUSI)));break;
	    				case 23:
			    			formula = Function.replaceExpression(KEKKA_IPPAN_SAIKENGOKEI,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 24: excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEIKA_EIGYOU_SAIKEN)));break;
	    				case 25:
			    			formula = Function.replaceExpression(KEKKA_SAIKENZANDAGOKEI_A,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 26: excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYO_SAIMUKEI)));break;
	    				case 27: excel.setCellValue(Function.getValueOfLong(rs.getString(RIKO_KENEN)));break;
	    				case 28: excel.setCellValue(Function.getValueOfLong(rs.getString(RYUHOSAIMU)));break;
	    				case 29: excel.setCellValue(Function.getValueOfLong(rs.getString(OTH_RYUHOSAIMU)));break;
	    				case 30:
			    			formula = Function.replaceExpression(KEKKA_RYUHOSAIMUKEI_C,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 31: excel.setCellValue(Function.getValueOfLong(rs.getString(HOZEN)));break;
	    				case 32: excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTAKAISYU)));break;
	    				case 33:
			    			formula = Function.replaceExpression(KEKKA_HOSENKEI,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 34: excel.setCellValue(Function.getValueOfLong(rs.getString(KIHIKIATEKIN)));break;
	    				case 35:
			    			formula = Function.replaceExpression(FORMULA5,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 36: excel.setCellValue(Function.getValueOfLong(rs.getString(HIKIATE_HOSEI)));break;
	    				case 37:
			    			formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 38: excel.setCellValue(" ");break;
			    		//案件No.D9059 補正後引当控除後残高削除(空白)対応 コメント化
	    				//case 37:
			    			//formula = Function.replaceExpression(FORMULA7,String.valueOf(i+1));
			    			//excel.setCellFormula(formula);
			    			//break;
	    				case 39: excel.setCellValue(rs.getString(HIKIATEKIN_HANTEIKONKYO));break;
	    				case 40: excel.setCellValue(rs.getString(SYORI_DT));break;

	    				//査定データ取得
	    				case 41: excel.setCellValue(rs.getString(TAISYO_YM_1));break;
	    				case 42: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN_1));break;
	    				case 43: excel.setCellValue(rs.getString(SAIKEN_KBN_1));break;
	    				case 44: excel.setCellValue(rs.getString(RENKETSU_KBN));break;
	    				case 45: excel.setCellValue(rs.getString(KTK_1));break;
	    				case 46: excel.setCellValue(rs.getString(OYA_KTK_1));break;
	    				case 47: excel.setCellValue(rs.getString(OYA_BUSINESS_NM_1));break;
	    				case 48: excel.setCellValue(rs.getString(TAIRYU_KBN_1));break;
	    				case 49: excel.setCellValue(rs.getString(KESSANKI_1));break;
	    				case 50: excel.setCellValue(rs.getString(AKAJI));break;
	    				case 51: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
	    				case 52: excel.setCellValue(rs.getString(M12));break;
	    				case 53: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
	    				case 54: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
	    				case 55: excel.setCellValue(rs.getString(RIKI_SITEI));break;
	    				case 56: excel.setCellValue(Function.getValueOfLong(rs.getString(UKETORI_TEGATA_1)));break;
	    				case 57: excel.setCellValue(Function.getValueOfLong(rs.getString(YUSYUTU_UKETORI_TEGATA_1)));break;
	    				case 58: excel.setCellValue(Function.getValueOfLong(rs.getString(URIKAKEKIN_1)));break;
	    				case 59: excel.setCellValue(Function.getValueOfLong(rs.getString(TORIHIKI_MAEWATASHIKIN_1)));break;
	    				case 60: excel.setCellValue(Function.getValueOfLong(rs.getString(TATEKAEKIN_1)));break;
	    				case 61: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUNYUUKIN_1)));break;
	    				case 62: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUSYUEKI_1)));break;
	    				case 63: excel.setCellValue(Function.getValueOfLong(rs.getString(TANKI_KASITUKEKIN_1)));break;
	    				case 64: excel.setCellValue(Function.getValueOfLong(rs.getString(SASHIIRE_HOSYOKIN_1)));break;
	    				case 65: excel.setCellValue(Function.getValueOfLong(rs.getString(KARIBARAIKIN_1)));break;
	    				case 66: excel.setCellValue(Function.getValueOfLong(rs.getString(TYOKI_KASITUKEKIN_1)));break;
	    				case 67: excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTA_TOUSI_1)));break;
	    				case 68:
			    			formula = Function.replaceExpression(IPPANSAIKANGOKEI,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 69: excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEIKA_EIGYOU_SAIKEN_1)));break;
	    				case 70:
			    			formula = Function.replaceExpression(SAIKANZANDAGOKEI_A,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 71:excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYO_SAIMUKEI_1)));break;
	    				case 72:excel.setCellValue(Function.getValueOfLong(rs.getString(RIKO_KENEN_1)));break;
	    				case 73:excel.setCellValue(Function.getValueOfLong(rs.getString(RYUHOSAIMU_1)));break;
	    				case 74:excel.setCellValue(Function.getValueOfLong(rs.getString(OTH_RYUHOSAIMU_1)));break;
	    				case 75:
			    			formula = Function.replaceExpression(FORMULA10,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 76:excel.setCellValue(Function.getValueOfLong(rs.getString(HOZEN_1)));break;
	    				case 77:excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTAKAISYU_1)));break;
	    				case 78:
			    			formula = Function.replaceExpression(FORMULA11,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				case 79:excel.setCellValue(Function.getValueOfLong(rs.getString(KIHIKIATEKIN_1)));break;
	    				case 80:
			    			formula = Function.replaceExpression(FORMULA12,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
			    			// 課題No.211
			    			// 追加開始
	    				//case 81:excel.setCellValue(rs.getString(TUIKA_HIKIATE_1));break;
	    				case 81:excel.setCellValue(Function.getValueOfLong(rs.getString(TUIKA_HIKIATE_1)));break;
	    					// 追加完了
	    				case 82:
	    					formula = Function.replaceExpression(FORMULA13,String.valueOf(i+1));
	    					excel.setCellFormula(formula);
	    					break;
	    				case 83:excel.setCellValue(rs.getString(HIKIATEKIN_HANTEIKONKYO_1));break;
	    				case 84:excel.setCellValue(rs.getString(SYORI_DT_1));
	    				break;

	    				default:break;
		    		}
		    	}
	    	i++;
	    	}

	    	//年月出力
	    	this.setYm(excel,ym_hantei);

    		if(!result){
    			appContext.setMsgCode(WARNING0004);
    		}
    		return result;

	    } finally {
	    	if (rs != null) {
	    		rs.close();
	    	}
	    }
	}

	/**
	 * 【年月出力】
	 *
	 * @throws Exception
	 */
	private void setYm(Excel excel,String ym)  {
		List<String> list_ym = new ArrayList<String>();
		String title_ym = GS.EMPTY_CHARCTER;
		String[] ymHairetu = null;
		excel.selectCell(1,0,true);
		String title = excel.getStringCellValue();
		if(!GS.EMPTY_CHARCTER.equals(ym)){
			ymHairetu = Function.StrSplitToken(ym,GS.SLASH);
			list_ym.add(ymHairetu[0]);
			list_ym.add(ymHairetu[1]);
			title_ym = Function.replaceExpression(title,list_ym);
		}
		excel.setCellValue(title_ym);
	}

}