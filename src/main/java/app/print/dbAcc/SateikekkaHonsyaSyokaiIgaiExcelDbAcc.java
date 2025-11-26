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

/**
* LD3102 引当金額集計表（初回月以外）  DBアクセスクラス
*/
public class SateikekkaHonsyaSyokaiIgaiExcelDbAcc extends CommonDbAcc {

	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	private static final String BUNRUI2 = "init_bunrui2";
	private static final String HONBU_CD = "honbu_cd";
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String WB_COUNTRY_NM            	= "wb_country_nm";
	private static final String BU_CD            			= "init_bu_cd";
	private static final String BU_NM            			= "bu_nm";
	private static final String TORIHIKISAKI_KBN         	= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";
	private static final String RENKETSU_KBN   				= "renketsu_kbn";
	private static final String KTK             			= "ktk";
	private static final String OYA_KTK      				= "oya_ktk";
	private static final String OYA_BUSINESS_NM			= "oya_business_nm";
	private static final String TAIRYU_KBN				= "tairyu_kbn";
	private static final String AKAJI						= "akaji";
	private static final String SAIMU_TYOKA				= "saimu_tyoka";
	private static final String M12						= "m12";
	private static final String KASIDAORE_KENEN			= "kasidaore_kenen";
	private static final String HASAN_KOSEI				= "hasan_kosei";
	private static final String RIKI_SITEI				= "riki_sitei";
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
	private static final String KOTEIKA_EIGYOU_SAIKEN					= "koteika_eigyou_saiken";
	private static final String RYUHOSAIMU				= "ryuhosaimu";
	private static final String OTH_RYUHOSAIMU			= "oth_ryuhosaimu";
	private static final String HOZEN						= "hozen";
	private static final String SONOTAKAISYU				= "sonotakaisyu";
	private static final String HOSYO_SAIMUKEI			= "hosyo_saimukei";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String KIHIKIATEKIN				= "kihikiatekin";
	private static final String TUIKA_HIKIATE				= "tuika_hikiate";
	private static final String COMMENT_VAL				= "hikiatekin_hanteikonkyo";
	private static final String SHINCHOKU					= "sintyoku";


	private static final int StartRow = 4;	//明細出力開始行
	private static final int endClm = 49;	//明細出力最終列

	private static final String WARNING0004 = "warning.0004";
	//Excel計算式
	private static final String IPPANSAIKENGOKEI = "SUM(V{1}:AG{1})";
	private static final String SAIKENZANDAKAGOKEIA = "AH{1}+AI{1}";
	private static final String RYUHOSAIMUKC = "IF(AJ{1}<SUM(AM{1}:AN{1}),IF(AJ{1}<0,0,AJ{1}),SUM(AM{1}:AN{1}))";
	private static final String HOZENKEI = "SUM(AO{1}:AQ{1})";
	private static final String FORMULA5 = "AJ{1}+AL{1}-(AO{1}+AP{1}+AQ{1})-AS{1}";
	private static final String HOSEIGOHIKIATEKINGAKU = "AS{1}+AU{1}";

	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD3102_SELECT_ICHIRAN 	= "SP_SS_LD3102_SELECT_ICHIRAN";//一覧情報取得プロシージャ
	// INパラメータ
	private String tyohyo_lang;

	/**
	 * コンストラクタ
	 *
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */

	public SateikekkaHonsyaSyokaiIgaiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
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
				excel.selectCell(0,49,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				//分類1名称
				excel.selectCell(1,49,true);
				excel.setCellValue(rs.getString(BUNRUI1_NM));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}

	/**
	 * 一覧情報取得処理 <br>
	 *
     * @param Excel
	 * @exception SQLException
	 */
	public boolean getMeisai(Excel excel) throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		String formula = null;

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD3102_SELECT_ICHIRAN, sqlExec);
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
	    		//明細が存在する場合
	    		result = true;
	    		excel.selectRow(i, true);
				for (int j=0; j<=endClm; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//取得値を設定
    				switch (j){
		    			case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
		    			case 1: excel.setCellValue(rs.getString(TOGO_TORI_CD));break;
		    			case 2: excel.setCellValue(rs.getString(BUSINESS_NM));break;
		    			case 3: excel.setCellValue(rs.getString(WB_COUNTRY_NM));break;
		    			case 4: excel.setCellValue(rs.getString(BUNRUI2));break;
		    			case 5: excel.setCellValue(rs.getString(HONBU_CD));break;
		    			case 6: excel.setCellValue(rs.getString(BU_CD));break;
		    			case 7: excel.setCellValue(rs.getString(BU_NM));break;
		    			case 8: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    			case 9: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
 		    			case 10: excel.setCellValue(rs.getString(RENKETSU_KBN));break;
 		    			case 11: excel.setCellValue(rs.getString(KTK));break;
		    			case 12: excel.setCellValue(rs.getString(OYA_KTK));break;
		    			case 13: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
		    			case 14: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 15: excel.setCellValue(rs.getString(AKAJI));break;
		    			case 16: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
		    			case 17: excel.setCellValue(rs.getString(M12));break;
		    			case 18: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
		    			case 19: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
		    			case 20: excel.setCellValue(rs.getString(RIKI_SITEI));break;
		    			case 21: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 22: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 23: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 24: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 25: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 26: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 27: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 28: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 29: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 30: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 31: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 32: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 33:
			    			formula = Function.replaceExpression(IPPANSAIKENGOKEI,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 34: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOTEIKA_EIGYOU_SAIKEN)));break;
		    			case 35:
			    			formula = Function.replaceExpression(SAIKENZANDAKAGOKEIA,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 36: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 37: excel.setCellValue(Function.getValueOfDouble(rs.getString(RIKO_KENEN)));break;
		    			case 38: excel.setCellValue(Function.getValueOfDouble(rs.getString(RYUHOSAIMU)));break;
		    			case 39: excel.setCellValue(Function.getValueOfDouble(rs.getString(OTH_RYUHOSAIMU)));break;
		    			case 40:
			    			formula = Function.replaceExpression(RYUHOSAIMUKC,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 41: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOZEN)));break;
		    			case 42: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTAKAISYU)));break;
		    			case 43:
			    			formula = Function.replaceExpression(HOZENKEI,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 44: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 45:
			    			formula = Function.replaceExpression(FORMULA5,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 46: excel.setCellValue(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)));break;
		    			case 47:
			    			formula = Function.replaceExpression(HOSEIGOHIKIATEKINGAKU,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 48: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			case 49: excel.setCellValue(rs.getString(SHINCHOKU));break;
		    		}
		    	}
	    		i++;
	    	}
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
}