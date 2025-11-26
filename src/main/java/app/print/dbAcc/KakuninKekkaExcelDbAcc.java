/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/02		SSC				課題No.28 引当対象外/帳簿外対応
003		2010/01/15		SSC				課題No.234 汎用1表示対応
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
* LD2101 引当金額集計表（期末）  DBアクセスクラス
*/
public class KakuninKekkaExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
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
	private static final String TAIRYU_KBN_NM				= "tairyu_kbn_nm";
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
	private static final String SATEI_KAISHA_CD			= "satei_kaisha_cd";
	private static final String OYA_ITTAI_DOKURITU		= "oya_ittai_dokuritu";
	private static final String SYORI_DT					= "syori_dt";
	private static final String ENKA						= "enka";
	private static final String DORUKA					= "doruka";
	private static final String ANKEN_NO					= "anken_no";
	private static final String SYSTEM_KBN				= "system_kbn";
	private static final String SATEI_KI					= "satei_ki";
	private static final String SYORI_KAISU				= "syori_kaisu";
	private static final String PHASE						= "phase";
	private static final String TAISYO_YM					= "taisyo_ym";
	private static final String KESSANKI					= "kessanki";
	// 課題No.28
	// 追加開始
	private static final String HIKIATEKIN_SHOSAI_NM		= "hikiatekin_shosai_nm";	// 引当金詳細
	// 追加完了
	// 課題No.234
	// 追加開始
	private static final String BUNRUI1_NM				= "bunrui1_nm";
	// 追加完了
	private static final String WARNING0004 = "warning.0004";	
	private static final String KIJUNBI_KBN_1				= "1";
	private static final String KIJUNBI_KBN_9				= "9";
	
	private static final int StartRow = 5;					//明細出力開始行
	private static final int startClm_kihon = 0;				//明細出力開始列(基本情報)
	private static final int endClm_kihon = 11;				//明細出力最終列(基本情報)
	private static final int startClm_zenki = 12;			//明細出力開始列(前期)
	private static final int endClm_zenki = 48;				//明細出力最終列(前期)
	private static final int startClm_kari = 49;				//明細出力開始列(仮基準日)
	private static final int endClm_kari = 97;				//明細出力最終列(仮基準日)
	private static final int startClm_konki = 98;			//明細出力開始列(今期)
	// 課題No.28
	// 修正開始
	//private static final int endClm_konki = 133;				//明細出力最終列(今期)
	private static final int endClm_konki = 134;				//明細出力最終列(今期)
	//private static final int startClm_kansan = 134;			//明細出力開始列(今期)
	private static final int startClm_kansan = 135;			//明細出力開始列(今期)
	// 修正完了
	// 課題No.28
	// 修正開始
	// private static final int endClm_kansan = 144;
	private static final int endClm_kansan = 145;				//明細出力最終列(今期)
	// 修正完了
	//前期明細計算式
	private static final String FORMULA1 = "SUM(V{1}:AG{1})";
	private static final String FORMULA2 = "AH{1}+AI{1}";
	//仮基準日明細計算式
	private static final String FORMULA6 = "SUM(BP{1}:CA{1})";
	private static final String FORMULA7 = "CB{1}+CC{1}";
	private static final String FORMULA8 = "IF(CD{1}<SUM(CE{1}:CF{1}),IF(CD{1}<0,0,CD{1}),SUM(CE{1}:CF{1}))";
	private static final String FORMULA9 = "SUM(CG{1}:CI{1})";
	private static final String FORMULA10 = "CD{1}-(CG{1}+CH{1}+CI{1})+CL{1}-CM{1}";
	private static final String FORMULA11 = "SUM(CO{1}:CP{1})";
	//今期明細計算式
	private static final String FORMULA12 = "SUM(DC{1}:DN{1})";
	private static final String FORMULA13 = "DO{1}+DP{1}";
	//ドル換算後金額情報計算式
	// 課題No.28
	// 修正開始
	/*private static final String FORMULA17 = "EE{1}/EF{1}";
	private static final String FORMULA18 = "IF(EB{1}=\"USD\",1,EG{1})";
	private static final String FORMULA19 = "AS{1}*EH{1}";
	private static final String FORMULA20 = "(DO{1}+DX{1})*EH{1}";
	private static final String FORMULA21 = "DZ{1}*EH{1}";
	private static final String FORMULA22 = "EN{1}*EH{1}";
	private static final String FORMULA23 = "EJ{1}-EK{1}";
	private static final String FORMULA24 = "DZ{1}-AS{1}";*/
	private static final String FORMULA17 = "EF{1}/EG{1}";
	private static final String FORMULA18 = "IF(EB{1}=\"USD\",1,EH{1})";
	private static final String FORMULA19 = "AS{1}*EI{1}";
	private static final String FORMULA20 = "(DO{1}+DX{1})*EI{1}";
	private static final String FORMULA21 = "DZ{1}*EI{1}";
	private static final String FORMULA22 = "EO{1}*EI{1}";
	private static final String FORMULA23 = "EK{1}-EL{1}";
	private static final String FORMULA24 = "DZ{1}-AS{1}";
	// 修正完了

	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD2101_SELECT_ICHIRAN 	= "SP_SS_LD2101_SELECT_ICHIRAN"; //一覧情報取得プロシージャ
	private static final String SP_SS_LD2101_SELECT_ICHIRAN2 	= "SP_SS_LD2101_SELECT_ICHIRAN2";//一覧情報取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1400 		= "SP_SS_OL_SELECT_T1400"; 		 //前期案件No取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1401 		= "SP_SS_OL_SELECT_T1401"; 		 //仮基準日案件No取得プロシージャ
	private static final String SP_SS_LD2101_SELECT_MEISAI	= "SP_SS_LD2101_SELECT_MEISAI"; //明細取得用プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public KakuninKekkaExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
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
				//課題No.28
				//修正開始
				//excel.selectCell(1,133,true);
				excel.selectCell(1,134,true);
				//修正完了
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				//課題No.234
				//追加開始
				excel.selectCell(2,134,true);
				excel.setCellValue(Function.trim(rs.getString(BUNRUI1_NM)));
				//追加完了
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
		String systemkbn = null;
		String satei_kaisha_cd = null;
		String mise_cd = null;
		String tori_cd = null;
		String sateiki = null;
		String syori_kaisu = null;
		String phase = null;
		String ym_hantei = GS.EMPTY_CHARCTER;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_ICHIRAN, sqlExec);
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
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
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
				for (int j=startClm_kihon; j<=endClm_kihon; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				switch (j){
    					case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
    					case 1: excel.setCellValue(rs.getString(TOGO_TORI_CD));break;
    					case 2: excel.setCellValue(rs.getString(BUSINESS_NM));break;
    					case 3: excel.setCellValue(rs.getString(WB_COUNTRY_NM));break;
    					case 4: excel.setCellValue(rs.getString(SATEI_KAISHA_CD));break;
    					case 5: excel.setCellValue(rs.getString(MISE_CD));break;
    					case 6: excel.setCellValue(rs.getString(BU_CD));break;
    					case 7: excel.setCellValue(rs.getString(BU_NM));break;
    					case 8: excel.setCellValue(rs.getString(KA_CD));break;
    					case 9: excel.setCellValue(rs.getString(KA_NM));break;
    					case 10: excel.setCellValue(rs.getString(CELL_CD));break;
    					case 11: excel.setCellValue(rs.getString(CELL_NM));	break;
    					default:break;
		    		}
		    	}

				for (int j=startClm_konki; j<=endClm_konki; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//今期データを設定
    				switch (j){
		    			case 98: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    			case 99: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
		    			case 100: excel.setCellValue(rs.getString(KTK));break;
		    			case 101: excel.setCellValue(rs.getString(OYA_KTK));break;
		    			case 102: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
		    			case 103: excel.setCellValue(rs.getString(OYA_ITTAI_DOKURITU));break;
		    			case 104: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 105: excel.setCellValue(rs.getString(TAIRYU_KBN_NM));break;
		    			case 106: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 107: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 108: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 109: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 110: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 111: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 112: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 113: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 114: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 115: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 116: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 117: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 118:
			    			formula = Function.replaceExpression(FORMULA12,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 120:
			    			formula = Function.replaceExpression(FORMULA13,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 127: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 129: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 131: excel.setCellValue(rs.getString(TUUKA_CD));break;
		    			// 課題No.28
		    			// 修正開始
		    			case 132: excel.setCellValue(Function.trim(rs.getString(HIKIATEKIN_SHOSAI_NM)));break;
		    			//case 132: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			//case 133: excel.setCellValue(rs.getString(SYORI_DT));break;
		    			case 133: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			case 134: excel.setCellValue(rs.getString(SYORI_DT));break;
		    			// 修正完了
			    		default:break;
		    		}
		    	}
				//ドル換算後金額情報を設定
				for (int j=startClm_kansan; j<=endClm_kansan; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				switch (j){
    					// 課題No.28
	    				// 修正開始
    					/*case 134: excel.setCellValue(Function.getValueOfDouble(rs.getString(ENKA)));break;
	    				case 135: excel.setCellValue(Function.getValueOfDouble(rs.getString(DORUKA)));break;
		    			case 136:
			    			formula = Function.replaceExpression(FORMULA17,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 137:
			    			formula = Function.replaceExpression(FORMULA18,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 138:
			    			formula = Function.replaceExpression(FORMULA19,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 139:
			    			formula = Function.replaceExpression(FORMULA20,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 140:
			    			formula = Function.replaceExpression(FORMULA21,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 141:
			    			formula = Function.replaceExpression(FORMULA22,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 142:
			    			formula = Function.replaceExpression(FORMULA23,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 143:
			    			formula = Function.replaceExpression(FORMULA24,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
    					default:break;*/
						case 135: excel.setCellValue(Function.getValueOfDouble(rs.getString(ENKA)));break;
						case 136: excel.setCellValue(Function.getValueOfDouble(rs.getString(DORUKA)));break;
						case 137:
							formula = Function.replaceExpression(FORMULA17,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 138:
							formula = Function.replaceExpression(FORMULA18,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 139:
							formula = Function.replaceExpression(FORMULA19,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 140:
							formula = Function.replaceExpression(FORMULA20,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 141:
							formula = Function.replaceExpression(FORMULA21,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 142:
							formula = Function.replaceExpression(FORMULA22,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 143:
							formula = Function.replaceExpression(FORMULA23,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 144:
							formula = Function.replaceExpression(FORMULA24,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						default:break;
	    				// 修正完了
		    		}
		    	}
				systemkbn = rs.getString(SYSTEM_KBN);
				satei_kaisha_cd = rs.getString(SATEI_KAISHA_CD);
				mise_cd = rs.getString(MISE_CD);
				tori_cd = rs.getString(KIKAN_TORI_CD);
				sateiki = rs.getString(SATEI_KI);
				syori_kaisu = rs.getString(SYORI_KAISU);
				phase = rs.getString(PHASE);
				//前期案件No取得
				String anken_no_zenki = getAnkenNo_zenki(systemkbn,satei_kaisha_cd,mise_cd,tori_cd,sateiki,syori_kaisu,phase);
				//前期明細取得
				if(GS.EMPTY_CHARCTER.equals(Function.trim(anken_no_zenki))){
					getMeisai_9(excel,i,systemkbn,satei_kaisha_cd,mise_cd,tori_cd,sateiki);
				}else{
					getMeisai_zenki(excel,i,anken_no_zenki);
				}
				//仮基準日案件No取得
				String anken_no_kari = getAnkenNo_kari(systemkbn,satei_kaisha_cd,mise_cd,tori_cd,sateiki);
				//仮基準日明細取得
				if(GS.EMPTY_CHARCTER.equals(Function.trim(anken_no_kari))){
					getMeisai_1(excel,i,systemkbn,satei_kaisha_cd,mise_cd,tori_cd,sateiki);
				}else{
					getMeisai_kari(excel,i,anken_no_kari);
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
	 * 前期案件No取得 <br>
	 * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @return String
	 * @exception SQLException
	 */
	public String getAnkenNo_zenki(String systemkbn,String satei_kaisha_cd,String mise_cd,String tori_cd,String sateiki,String syori_kaisu,String phase) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1400, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(tori_cd);
		exCstmt.setStringIn(sateiki);
		exCstmt.setStringIn(syori_kaisu);
	    exCstmt.setStringIn(phase);
		exCstmt.setStringOut(ANKEN_NO);
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	return exCstmt.getString(ANKEN_NO);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}	
		
	/**
	 * 仮基準日案件No取得 <br>
	 * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @return String
	 * @exception SQLException
	 */
	public String getAnkenNo_kari(String systemkbn,String satei_kaisha_cd,String mise_cd,String tori_cd,String sateiki) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1401, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(tori_cd);
		exCstmt.setStringIn(sateiki);
		exCstmt.setStringOut(ANKEN_NO);
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	return exCstmt.getString(ANKEN_NO);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}	
		
	/**
	 * 一覧情報取得(前期)処理 <br>
	 * 
     * @param Excel
     * @param int
     * @param String
	 * @exception SQLException
	 */
	private void getMeisai_zenki(Excel excel,int i,String anken_no) throws SQLException {
		ResultSet rs = null;
		String formula = null;

		if(anken_no == null){
    		//セルスタイルのみ設定
			for (int j=startClm_zenki; j<=endClm_zenki; j++ ) {
	    		excel.selectCell(j, true);
				excel.setCellStyle(j);
			}
			return;
		}
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			int cnt = 0;
	    	if ( rs.next() ) {
				for (int j=startClm_zenki; j<=endClm_zenki; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//取得値を設定
    				switch (j){
		    			case 12: excel.setCellValue(rs.getString(TAISYO_YM));break;
		    			case 13: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    			case 14: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
		    			case 15: excel.setCellValue(rs.getString(KTK));break;
		    			case 16: excel.setCellValue(rs.getString(OYA_KTK));break;
		    			case 17: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
		    			case 18: excel.setCellValue(rs.getString(OYA_ITTAI_DOKURITU));break;
		    			case 19: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 20: excel.setCellValue(rs.getString(TAIRYU_KBN_NM));break;
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
			    			formula = Function.replaceExpression(FORMULA1,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 35:
			    			formula = Function.replaceExpression(FORMULA2,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 42: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 44: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 46: excel.setCellValue(rs.getString(TUUKA_CD));break;
		    			case 47: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			case 48: excel.setCellValue(rs.getString(SYORI_DT));break;
	    				default:break;
		    		}
				}
				cnt++;
	    	}
	    	if(cnt==0){
	    		//セルスタイルのみ設定
				for (int j=startClm_zenki; j<=endClm_zenki; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
				}
	    	}
	    } finally {
	    	if (rs != null) {
	    		rs.close();
	    	}
	    }
	}

	/**
	 * 一覧情報取得(査定未実施)処理 <br>
	 * 
     * @param Excel
     * @param int
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
	 * @exception SQLException
	 */
	private void getMeisai_9(Excel excel,int i,String systemkbn,String satei_kaisha_cd,String mise_cd,String tori_cd,String sateiki) throws SQLException {
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		String formula = null;

		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_MEISAI, sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_cd);
		cstmt.setStringIn(mise_cd);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(KIJUNBI_KBN_9);
		cstmt.setStringIn(tyohyo_lang);
		cstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			int cnt = 0;
	    	if ( rs.next() ) {
				for (int j=startClm_zenki; j<=endClm_zenki; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//取得値を設定
    				switch (j){
		    			case 12: excel.setCellValue(rs.getString(TAISYO_YM));break;
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
			    			formula = Function.replaceExpression(FORMULA1,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 35:
			    			formula = Function.replaceExpression(FORMULA2,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 46: excel.setCellValue(rs.getString(TUUKA_CD));break;
	    				default:break;
		    		}
				}
				cnt++;
	    	}
	    	if(cnt==0){
	    		//セルスタイルのみ設定
				for (int j=startClm_zenki; j<=endClm_zenki; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
				}
	    	}
	    } finally {
	    	if (rs != null) {
	    		rs.close();
	    	}
	    }
	}

	/**
	 * 一覧情報取得(査定未実施)処理 <br>
	 * 
     * @param Excel
     * @param int
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
	 * @exception SQLException
	 */
	public void getMeisai_1(Excel excel,int i,String systemkbn,String satei_kaisha_cd,String mise_cd,String tori_cd,String sateiki) throws SQLException {
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		String formula = null;

		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_MEISAI,sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_cd);
		cstmt.setStringIn(mise_cd);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(KIJUNBI_KBN_1);
		cstmt.setStringIn(tyohyo_lang);
		cstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			int cnt = 0;
	    	if ( rs.next() ) {
				for (int j=startClm_kari; j<=endClm_kari; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//取得値を設定
    				switch (j){
		    			case 49: excel.setCellValue(rs.getString(TAISYO_YM));break;
		    			case 67: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 68: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 69: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 70: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 71: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 72: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 73: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 74: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 75: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 76: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 77: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 78: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 79:
			    			formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 81:
			    			formula = Function.replaceExpression(FORMULA7,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 84:
			    			formula = Function.replaceExpression(FORMULA8,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 87:
			    			formula = Function.replaceExpression(FORMULA9,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 91:
			    			formula = Function.replaceExpression(FORMULA10,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 94:
			    			formula = Function.replaceExpression(FORMULA11,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 95: excel.setCellValue(rs.getString(TUUKA_CD));break;
	    				default:break;
		    		}
		    	}
				cnt++;
	    	}
	    	if(cnt==0){
	    		//セルスタイルのみ設定
	    		for (int j=startClm_kari; j<=endClm_kari; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
				}
	    	}
	    } finally {
	    	if (rs != null) {
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 一覧情報取得(仮基準日)処理 <br>
	 * 
     * @param Excel
     * @param int
     * @param String
	 * @exception SQLException
	 */
	public void getMeisai_kari(Excel excel,int i,String anken_no) throws SQLException {
		ResultSet rs = null;
		String formula = null;
		if(anken_no == null){
    		//セルスタイルのみ設定
    		for (int j=startClm_kari; j<=endClm_kari; j++ ) {
	    		excel.selectCell(j, true);
				excel.setCellStyle(j);
			}
			return;
		}

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2101_SELECT_ICHIRAN2, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			int cnt = 0;
	    	if ( rs.next() ) {
				for (int j=startClm_kari; j<=endClm_kari; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				//取得値を設定
    				switch (j){
		    			case 49: excel.setCellValue(rs.getString(TAISYO_YM));break;
		    			case 50: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    			case 51: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
		    			case 52: excel.setCellValue(rs.getString(KTK));break;
		    			case 53: excel.setCellValue(rs.getString(OYA_KTK));break;
		    			case 54: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
		    			case 55: excel.setCellValue(rs.getString(OYA_ITTAI_DOKURITU));break;
		    			case 56: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 57: excel.setCellValue(rs.getString(TAIRYU_KBN_NM));break;
		    			case 58: excel.setCellValue(rs.getString(KESSANKI));break;
		    			case 59: excel.setCellValue(rs.getString(TEI_KTK));break;
		    			case 60: excel.setCellValue(rs.getString(YOTYUUI));break;
		    			case 61: excel.setCellValue(rs.getString(AKAJI));break;
		    			case 62: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
		    			case 63: excel.setCellValue(rs.getString(M12));break;
		    			case 64: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
		    			case 65: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
		    			case 66: excel.setCellValue(rs.getString(RIKI_SITEI));break;
		    			case 67: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 68: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 69: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 70: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 71: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 72: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 73: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 74: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 75: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 76: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 77: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 78: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 79:
			    			formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 80: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOMOKU1)));break;
		    			case 81:
			    			formula = Function.replaceExpression(FORMULA7,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 82: excel.setCellValue(Function.getValueOfDouble(rs.getString(RYUHOSAIMU)));break;
		    			case 83: excel.setCellValue(Function.getValueOfDouble(rs.getString(OTH_RYUHOSAIMU)));break;
		    			case 84:
			    			formula = Function.replaceExpression(FORMULA8,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 85: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOZEN)));break;
		    			case 86: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTAKAISYU)));break;
		    			case 87:
			    			formula = Function.replaceExpression(FORMULA9,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 88: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 89: excel.setCellValue(Function.getValueOfDouble(rs.getString(RIKO_KENEN)));break;
		    			case 90: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 91:
			    			formula = Function.replaceExpression(FORMULA10,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 92: excel.setCellValue(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)));break;
		    			case 93: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOMOKU2)));break;
		    			case 94:
			    			formula = Function.replaceExpression(FORMULA11,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 95: excel.setCellValue(rs.getString(TUUKA_CD));break;
		    			case 96: excel.setCellValue(rs.getString(COMMENT_VAL));break;
		    			case 97: excel.setCellValue(rs.getString(SYORI_DT));break;
	    				default:break;
		    		}
		    	}
				cnt++;
	    	}
	    	if(cnt==0){
	    		//セルスタイルのみ設定
	    		for (int j=startClm_kari; j<=endClm_kari; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
				}
	    	}
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