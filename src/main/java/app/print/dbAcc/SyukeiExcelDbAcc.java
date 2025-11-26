/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/09		SSC				課題No.193 ヘッダ部、会社名の表示方法を統一する
003		2010/04/20		SSC				引当金集計表の補正後引当金額計算式を変更
004		2016/04/06		SSC				部門廃止対応(二次) 引当金額集計表(査定結果)に「本部」欄を追加
******************************************************************************/
package app.print.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
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
* LD2102 引当金額集計表（査定結果）  DBアクセスクラス
*/
public class SyukeiExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean tori_bean = null;			// 取引先情報
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	
	// 課題No.193
	// 追加開始
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	// 追加完了
	
	private static final String TAISYO_YM					= "taisyo_ym";
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	private static final String INIT_BUNRUI2            	= "init_bunrui2";
	private static final String HONBU_CD            	= "honbu_cd";
	private static final String INIT_BU_CD            	= "init_bu_cd";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String WB_COUNTRY_NM            	= "wb_country_nm";
	private static final String KTK             			= "ktk";
	private static final String OYA_KTK					= "oya_ktk";
	private static final String OYA_BUSINESS_NM			= "oya_business_nm";
	private static final String RYUHOSAIMU				= "ryuhosaimu";
	private static final String OTH_RYUHOSAIMU			= "oth_ryuhosaimu";
	private static final String HOZEN						= "hozen";
	private static final String SONOTAKAISYU				= "sonotakaisyu";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String TUIKA_HIKIATE				= "tuika_hikiate";
	private static final String TYOKA_CK					= "tyoka_chk";
	private static final String KAWA_CHK					= "kanwa_chk";
	private static final String ENTAI_CHK					= "entai_chk";
	private static final String HASANHO_CHK				= "hasanho_chk";
	private static final String KAISHAHO_CHK				= "kaishaho_chk";
	private static final String KOSEHO_CHK				= "koseho_chk";
	private static final String SAISEHO_CHK				= "saiseho_chk";
	private static final String SHOBUN_CHK				= "shobun_chk";
	private static final String SONOTA_CHK				= "sonota_chk";
	private static final String BU_NM						= "bu_nm";
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
	private static final String TEI_KTK					= "tei_ktk";
	private static final String YOTYUUI					= "yotyuui";
	private static final String AKAJI						= "akaji";
	private static final String SAIMU_TYOKA				= "saimu_tyoka";
	private static final String M12						= "m12";
	private static final String KASIDAORE_KENEN			= "kasidaore_kenen";
	private static final String HASAN_KOSEI				= "hasan_kosei";
	private static final String RIKI_SITEI				= "riki_sitei";
	private static final String HIKIATEKIN_HANTEIKONKYO	= "hikiatekin_hanteikonkyo";
	private static final String TORIHIKISAKI_KBN			= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";
	private static final String TAIRYU_KBN				= "tairyu_kbn";
	private static final String SINTYOKU				= "sintyoku";

	private static final String WARNING0004 = "warning.0004";	
	
	private static final int StartRow = 4;				//明細出力開始行
	private static final int startClm = 0;				//明細出力開始列(引当金検証データ)
	private static final int endClm = 59;				//明細出力最終列(引当金検証データ)

	//計算式
	private static final String FORMULA1 = "SUM(W{1}:AH{1})";
	private static final String FORMULA2 = "AI{1}+AJ{1}";
	private static final String FORMULA3 = "IF(AK{1}<SUM(AL{1}:AM{1}),IF(AK{1}<0,0,AK{1}),SUM(AL{1}:AM{1}))";
	private static final String FORMULA4 = "SUM(AN{1}:AP{1})";
	private static final String FORMULA5 = "AK{1}-(AN{1}+AO{1}+AP{1})+AS{1}-AT{1}";
	//private static final String FORMULA6 = "AS{1}-AU{1}"; 
	private static final String FORMULA6 = "AT{1}+AV{1}";
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD2102_SELECT_ICHIRAN3 	= "SP_SS_LD2102_SELECT_ICHIRAN3"; //一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public SyukeiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		form = (DownloadForm)appContext.getActionForm();
		//form = (DownloadForm)appContext.getActionForm();
		cmnData = appContext.getCMN();
		tori_bean =cmnData.getTori_bean();
		tyohyo_lang = form.getLangMode();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータr
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
				
				// 課題No.193
				// 追加開始
				//作成日
				excel.selectCell(1,49,true);
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
	 * 一覧情報取得処理 <br>
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2102_SELECT_ICHIRAN3, sqlExec);
		
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
	    		
	    		//明細情報を設定
				for (int j=startClm; j<=endClm; j++ ) {
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				switch (j){
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
	    				case 11: excel.setCellValue(rs.getString(OYA_KTK));break;
	    				case 12: excel.setCellValue(rs.getString(OYA_BUSINESS_NM));break;
	    				case 13: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
	    				case 14: excel.setCellValue(rs.getString(TEI_KTK));break;
	    				case 15: excel.setCellValue(rs.getString(YOTYUUI));break;
	    				case 16: excel.setCellValue(rs.getString(AKAJI));break;
	    				case 17: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
	    				case 18: excel.setCellValue(rs.getString(M12));break;
	    				case 19: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
	    				case 20: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
	    				case 21: excel.setCellValue(rs.getString(RIKI_SITEI));break;
	    				case 22: excel.setCellValue(Function.getValueOfLong(rs.getString(UKETORI_TEGATA)));break;
	    				case 23: excel.setCellValue(Function.getValueOfLong(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
	    				case 24: excel.setCellValue(Function.getValueOfLong(rs.getString(URIKAKEKIN)));break;
	    				case 25: excel.setCellValue(Function.getValueOfLong(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
	    				case 26: excel.setCellValue(Function.getValueOfLong(rs.getString(TATEKAEKIN)));break;
	    				case 27: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUNYUUKIN)));break;
	    				case 28: excel.setCellValue(Function.getValueOfLong(rs.getString(MISYUUSYUEKI)));break;
	    				case 29: excel.setCellValue(Function.getValueOfLong(rs.getString(TANKI_KASITUKEKIN)));break;
	    				case 30: excel.setCellValue(Function.getValueOfLong(rs.getString(SASHIIRE_HOSYOKIN)));break;
	    				case 31: excel.setCellValue(Function.getValueOfLong(rs.getString(KARIBARAIKIN)));break;
	    				case 32: excel.setCellValue(Function.getValueOfLong(rs.getString(TYOKI_KASITUKEKIN)));break;
	    				case 33: excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTA_TOUSI)));break;
	    				case 34: 
							formula = Function.replaceExpression(FORMULA1,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;			
	    				case 35: excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEIKA_EIGYOU_SAIKEN)));break;
					    case 36:
							formula = Function.replaceExpression(FORMULA2,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
	    				case 37: excel.setCellValue(Function.getValueOfLong(rs.getString(RYUHOSAIMU)));break;
	    				case 38: excel.setCellValue(Function.getValueOfLong(rs.getString(OTH_RYUHOSAIMU)));break;
					    case 39:
							formula = Function.replaceExpression(FORMULA3,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
	    				case 40: excel.setCellValue(Function.getValueOfLong(rs.getString(HOZEN)));break;
	    				case 41: excel.setCellValue(Function.getValueOfLong(rs.getString(SONOTAKAISYU)));break;
					    case 42:
							formula = Function.replaceExpression(FORMULA4,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
	    				case 43: excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYO_SAIMUKEI)));break;
	    				case 44: excel.setCellValue(Function.getValueOfLong(rs.getString(RIKO_KENEN)));break;
	    				case 45: excel.setCellValue(Function.getValueOfLong(rs.getString(KIHIKIATEKIN)));break;
					    case 46:
							formula = Function.replaceExpression(FORMULA5,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
	    				case 47: excel.setCellValue(Function.getValueOfLong(rs.getString(TUIKA_HIKIATE)));break;
					    case 48:
							formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 49: excel.setCellValue(rs.getString(HIKIATEKIN_HANTEIKONKYO));break;
	    				case 50: excel.setCellValue(rs.getString(TYOKA_CK));break;
	    				case 51: excel.setCellValue(rs.getString(KAWA_CHK));break;
	    				case 52: excel.setCellValue(rs.getString(ENTAI_CHK));break;
	    				case 53: excel.setCellValue(rs.getString(HASANHO_CHK));break;
	    				case 54: excel.setCellValue(rs.getString(KAISHAHO_CHK));break;
	    				case 55: excel.setCellValue(rs.getString(KOSEHO_CHK));break;
	    				case 56: excel.setCellValue(rs.getString(SAISEHO_CHK));break;
	    				case 57: excel.setCellValue(rs.getString(SHOBUN_CHK));break;
	    				case 58: excel.setCellValue(rs.getString(SONOTA_CHK));break;
	    				case 59: excel.setCellValue(rs.getString(SINTYOKU));break;
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