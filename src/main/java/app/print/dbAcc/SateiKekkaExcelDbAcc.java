/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
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
* LD2101 引当金額集計表（期中）  DBアクセスクラス
*/
public class SateiKekkaExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String WB_COUNTRY_NM            	= "wb_country_nm";
	private static final String MISE_CD            		= "mise_cd";
	private static final String BU_CD            			= "bu_cd";
	private static final String BU_NM            			= "bu_nm";
	private static final String KA_CD            			= "ka_cd";
	private static final String KA_NM            			= "ka_nm";
	private static final String CELL_CD            		= "cell_cd";
	private static final String CELL_NM            		= "cell_nm";
	private static final String TORIHIKISAKI_KBN         	= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";
	private static final String KTK             			= "ktk";
	private static final String OYA_KTK      				= "oya_ktk";
	private static final String OYA_BUSINESS_NM			= "oya_business_nm";
	private static final String TAIRYU_KBN				= "tairyu_kbn";
	private static final String TEI_KTK					= "tei_ktk";
	private static final String YOTYUUI					= "yotyuui";
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
	private static final String KOMOKU1					= "komoku1";
	private static final String RYUHOSAIMU				= "ryuhosaimu";
	private static final String OTH_RYUHOSAIMU			= "oth_ryuhosaimu";
	private static final String HOZEN						= "hozen";
	private static final String SONOTAKAISYU				= "sonotakaisyu";
	private static final String HOSYO_SAIMUKEI			= "hosyo_saimukei";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String KIHIKIATEKIN				= "kihikiatekin";
	private static final String TUIKA_HIKIATE				= "tuika_hikiate";
	private static final String KOMOKU2					= "komoku2";
	private static final String TUUKA_CD					= "tuuka_cd";
	private static final String COMMENT_VAL				= "comment_val";
	private static final String TYOKA_CHK					= "tyoka_chk";
	private static final String KANWA_CHK					= "kanwa_chk";
	private static final String ENTAI_CHK					= "entai_chk";
	private static final String HASANHO_CHK				= "hasanho_chk";
	private static final String KAISHAHO_CHK				= "kaishaho_chk";
	private static final String KOSEHO_CHK				= "koseho_chk";
	private static final String SONOTA_CHK				= "sonota_chk";
	private static final String SHINCHOKU					= "shinchoku";
	
	private static final int StartRow = 4;	//明細出力開始行
	private static final int endClm = 62;	//明細出力最終列
	
	private static final String WARNING0004 = "warning.0004";	
	//Excel計算式
	private static final String FORMULA1 = "SUM(Z{1}:AK{1})";
	private static final String FORMULA2 = "AL{1}+AM{1}";
	private static final String FORMULA3 = "IF(AN{1}<SUM(AO{1}:AP{1}),IF(AN{1}<0,0,AN{1}),SUM(AO{1}:AP{1}))";
	private static final String FORMULA4 = "SUM(AQ{1}:AS{1})";
	private static final String FORMULA5 = "AN{1}-(AQ{1}+AR{1}+AS{1})+AV{1}-AW{1}";
	private static final String FORMULA6 = "SUM(AY{1}:AZ{1})";

	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD2101_SELECT_NOTKONKI 	= "SP_SS_LD2101_SELECT_NOTKONKI";//一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public SateiKekkaExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
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
				excel.selectCell(0,54,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				//分類1名称
				excel.selectCell(1,54,true);
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_NOTKONKI, sqlExec);
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
		    			case 4: excel.setCellValue(rs.getString(MISE_CD));break;
		    			case 5: excel.setCellValue(rs.getString(BU_CD));break;
		    			case 6: excel.setCellValue(rs.getString(BU_NM));break;
		    			case 7: excel.setCellValue(rs.getString(KA_CD));break;
		    			case 8: excel.setCellValue(rs.getString(KA_NM));break;
		    			case 9: excel.setCellValue(rs.getString(CELL_CD));break;
		    			case 10: excel.setCellValue(rs.getString(CELL_NM));	break;
		    			case 11: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    			case 12: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
		    			case 13: excel.setCellValue(rs.getString(KTK));break;
		    			case 14: excel.setCellValue(rs.getString(OYA_KTK));break;
		    			case 15: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
		    			case 16: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 17: excel.setCellValue(rs.getString(TEI_KTK));break;
		    			case 18: excel.setCellValue(rs.getString(YOTYUUI));break;
		    			case 19: excel.setCellValue(rs.getString(AKAJI));break;
		    			case 20: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
		    			case 21: excel.setCellValue(rs.getString(M12));break;
		    			case 22: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
		    			case 23: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
		    			case 24: excel.setCellValue(rs.getString(RIKI_SITEI));break;
		    			case 25: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 26: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 27: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 28: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 29: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 30: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 31: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 32: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 33: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 34: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 35: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 36: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 37:
			    			formula = Function.replaceExpression(FORMULA1,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 38: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOMOKU1)));break;
		    			case 39:
			    			formula = Function.replaceExpression(FORMULA2,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 40: excel.setCellValue(Function.getValueOfDouble(rs.getString(RYUHOSAIMU)));break;
		    			case 41: excel.setCellValue(Function.getValueOfDouble(rs.getString(OTH_RYUHOSAIMU)));break;
		    			case 42:
			    			formula = Function.replaceExpression(FORMULA3,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 43: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOZEN)));break;
		    			case 44: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTAKAISYU)));break;
		    			case 45:
			    			formula = Function.replaceExpression(FORMULA4,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 46: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 47: excel.setCellValue(Function.getValueOfDouble(rs.getString(RIKO_KENEN)));break;
		    			case 48: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 49:
			    			formula = Function.replaceExpression(FORMULA5,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 50: excel.setCellValue(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)));break;
		    			case 51: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOMOKU2)));break;
		    			case 52:
			    			formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 53: excel.setCellValue(rs.getString(TUUKA_CD));break;
		    			case 54: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			case 55:
		    				if(GS.ON.equals(rs.getString(TYOKA_CHK))){
		    					excel.setCellValue(rs.getString(TYOKA_CHK));
		    				}
		    				break;
		    			case 56:
		    				if(GS.ON.equals(rs.getString(KANWA_CHK))){
		    					excel.setCellValue(rs.getString(KANWA_CHK));
		    				}
		    				break;
	    				case 57:
		    				if(GS.ON.equals(rs.getString(ENTAI_CHK))){
		    					excel.setCellValue(rs.getString(ENTAI_CHK));
		    				}
		    				break;
		    			case 58:
		    				if(GS.ON.equals(rs.getString(HASANHO_CHK))){
		    					excel.setCellValue(rs.getString(HASANHO_CHK));
		    				}
		    				break;
		    			case 59:
		    				if(GS.ON.equals(rs.getString(KAISHAHO_CHK))){
		    					excel.setCellValue(rs.getString(KAISHAHO_CHK));
		    				}
		    				break;
		    			case 60:
		    				if(GS.ON.equals(rs.getString(KOSEHO_CHK))){
		    					excel.setCellValue(rs.getString(KOSEHO_CHK));
		    				}
		    				break;
		    			case 61:
		    				if(GS.ON.equals(rs.getString(SONOTA_CHK))){
		    					excel.setCellValue(rs.getString(SONOTA_CHK));
		    				}
		    				break;
		    			case 62: excel.setCellValue(rs.getString(SHINCHOKU));break;
	    				default:break;
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