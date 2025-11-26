/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2023/03/01		NELCO			新規作成
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

/**
* 引当金額集計表(査定結果)(本社用：初回月)  DBアクセスクラス
*/
public class SateikekkaHonsyaSyokaiExcelDbAcc extends CommonDbAcc {

	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	private static final String BUNRUI2 = "init_bunrui2";
	private static final String HONBU_CD = "honbu_cd";
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
	private static final String KEIJYO_AKAJI			= "keijyo_akaji";
	private static final String TOUKI_AKAJI_KYU				= "touki_akaji_kyu";
	private static final String TAIRYU_KBN				= "tairyu_kbn";
	private static final String TEI_KTK					= "tei_ktk";
	private static final String YOTYUUI					= "yotyuui";
	private static final String AKAJI					= "akaji";
	private static final String SAIMU_TYOKA				= "saimu_tyoka";
	private static final String M12						= "m12";
	private static final String KASIDAORE_KENEN			= "kasidaore_kenen";
	private static final String HASAN_KOSEI				= "hasan_kosei";
	private static final String RIKI_SITEI				= "riki_sitei";
	//private static final String ZENGAKUHIKIATEZUMIKINGAKU	= "AV{1}+AW{1}+AX{1}";

	private static final String TOUKI_AKAJI_NEW					= "touki_akaji";
	private static final String SAIMU_TYOKA_NEW				= "saimu_tyoka_new";
	private static final String M12_NEW						= "m12_new";
	private static final String KASIDAORE_KENEN_NEW			= "kasidaore_kenen_new";
	private static final String HASAN_KOSEI_NEW				= "hasan_kosei_new";
	private static final String RIKI_SITEI_NEW				= "riki_sitei_new";
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
	private static final String RYUHOSAIMU				= "ryuhosaimu";
	private static final String OTH_RYUHOSAIMU			= "oth_ryuhosaimu";
	private static final String HOZEN						= "hozen";
	private static final String SONOTAKAISYU				= "sonotakaisyu";
	private static final String HOSYO_SAIMUKEI			= "hosyo_saimukei";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String KIHIKIATEKIN				= "kihikiatekin";
	private static final String TUIKA_HIKIATE				= "tuika_hikiate";
	private static final String KOTEIKA_EIGYOU_SAIKEN					= "koteika_eigyou_saiken";
	private static final int StartRow = 4;	//明細出力開始行
	private static final int endClm = 61;	//明細出力最終列

	private static final String WARNING0004 = "warning.0004";
	//Excel計算式
	private static final String FORMULA1 = "SUM(AI{1}:AT{1})";
	private static final String FORMULA2 = "AU{1}+AV{1}";
	private static final String FORMULA3 = "IF(AW{1}<SUM(AZ{1}:BA{1}),IF(AW{1}<0,0,AW{1}),SUM(AZ{1}:BA{1}))";
	private static final String FORMULA4 = "SUM(BB{1}:BD{1})";
	private static final String FORMULA5 = "AW{1}+AY{1}-(BB{1}+BC{1}+BD{1})-BF{1}";
	private static final String FORMULA6 = "SUM(BF{1}:BH{1})";

	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD3101_SELECT_ICHIRAN 	= "SP_SS_LD3101_SELECT_ICHIRAN";//一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	/**
	 * コンストラクタ
	 *
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */

	public SateikekkaHonsyaSyokaiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
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
				excel.selectCell(0,61,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				//分類1名称
				excel.selectCell(1,61,true);
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD3101_SELECT_ICHIRAN, sqlExec);
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
	    		String zengakuHikiateZumi = getZengakuHikiateZumi(rs.getString(KOTEIKA_EIGYOU_SAIKEN),calcForm1(rs.getString(UKETORI_TEGATA), rs.getString(YUSYUTU_UKETORI_TEGATA), rs.getString(URIKAKEKIN), rs.getString(TORIHIKI_MAEWATASHIKIN), rs.getString(TATEKAEKIN), rs.getString(MISYUUNYUUKIN), rs.getString(MISYUUSYUEKI), rs.getString(TANKI_KASITUKEKIN), rs.getString(SASHIIRE_HOSYOKIN), rs.getString(KARIBARAIKIN), rs.getString(TYOKI_KASITUKEKIN), rs.getString(SONOTA_TOUSI)), rs.getString(HOSYO_SAIMUKEI),rs.getString(KIHIKIATEKIN));

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
		    			case 14: excel.setCellValue(akajiZeroFilter(rs.getString(KEIJYO_AKAJI)));break;
		    			case 15: excel.setCellValue(akajiZeroFilter(rs.getString(TOUKI_AKAJI_KYU)));break;
		    			case 16: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
		    			case 17: excel.setCellValue(rs.getString(TEI_KTK));break;
		    			case 18: excel.setCellValue(rs.getString(YOTYUUI));break;
		    			case 19: excel.setCellValue(rs.getString(AKAJI));break;
		    			case 20: excel.setCellValue(rs.getString(SAIMU_TYOKA));break;
		    			case 21: excel.setCellValue(rs.getString(M12));break;
		    			case 22: excel.setCellValue(rs.getString(KASIDAORE_KENEN));break;
		    			case 23: excel.setCellValue(rs.getString(HASAN_KOSEI));break;
		    			case 24: excel.setCellValue(rs.getString(RIKI_SITEI));break;
		    			case 25: excel.setCellValue(" ");break;
		    			case 26: excel.setCellValue(" ");break;
		    			case 27: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
		    						excel.setCellValue("");
		    					 }
		    					 else{
		    						excel.setCellValue(akajiZeroFilter(rs.getString(TOUKI_AKAJI_NEW)));
		    					 }break;
		    			case 28: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
    								excel.setCellValue("");
    					 		 }
		    					 else{
		    						 excel.setCellValue(rs.getString(SAIMU_TYOKA_NEW));
		    					 }break;
		    			case 29: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
									excel.setCellValue("");
		    					 }
   					 			 else{
   					 				 excel.setCellValue(rs.getString(M12_NEW));
   					 			 }break;
		    			case 30: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
									excel.setCellValue("");
   					 			 }
		    					 else{
		    						excel.setCellValue(rs.getString(KASIDAORE_KENEN_NEW));
		    					 }break;
		    			case 31: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
									excel.setCellValue("");
		    					 }
		    					 else{
		    						excel.setCellValue(rs.getString(HASAN_KOSEI_NEW));
		    					 }break;
		    			case 32: if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
									excel.setCellValue("");
   					 			 }
		    					 else{
		    						excel.setCellValue(rs.getString(RIKI_SITEI_NEW));
		    					 }break;
		    			case 33: excel.setCellValue(" ");break;

    					case 34: excel.setCellValue(Function.getValueOfDouble(rs.getString(UKETORI_TEGATA)));break;
		    			case 35: excel.setCellValue(Function.getValueOfDouble(rs.getString(YUSYUTU_UKETORI_TEGATA)));break;
		    			case 36: excel.setCellValue(Function.getValueOfDouble(rs.getString(URIKAKEKIN)));break;
		    			case 37: excel.setCellValue(Function.getValueOfDouble(rs.getString(TORIHIKI_MAEWATASHIKIN)));break;
		    			case 38: excel.setCellValue(Function.getValueOfDouble(rs.getString(TATEKAEKIN)));break;
		    			case 39: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUNYUUKIN)));break;
		    			case 40: excel.setCellValue(Function.getValueOfDouble(rs.getString(MISYUUSYUEKI)));	break;
		    			case 41: excel.setCellValue(Function.getValueOfDouble(rs.getString(TANKI_KASITUKEKIN)));break;
		    			case 42: excel.setCellValue(Function.getValueOfDouble(rs.getString(SASHIIRE_HOSYOKIN)));break;
		    			case 43: excel.setCellValue(Function.getValueOfDouble(rs.getString(KARIBARAIKIN)));break;
		    			case 44: excel.setCellValue(Function.getValueOfDouble(rs.getString(TYOKI_KASITUKEKIN)));break;
		    			case 45: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTA_TOUSI)));break;
		    			case 46:
			    			formula = Function.replaceExpression(FORMULA1,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 47: excel.setCellValue(Function.getValueOfDouble(rs.getString(KOTEIKA_EIGYOU_SAIKEN)));break;
		    			case 48:
			    			formula = Function.replaceExpression(FORMULA2,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 49: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOSYO_SAIMUKEI)));break;
		    			case 50: excel.setCellValue(Function.getValueOfDouble(rs.getString(RIKO_KENEN)));break;
		    			case 51: excel.setCellValue(Function.getValueOfDouble(rs.getString(RYUHOSAIMU)));break;
		    			case 52: excel.setCellValue(Function.getValueOfDouble(rs.getString(OTH_RYUHOSAIMU)));break;
		    			case 53:
			    			formula = Function.replaceExpression(FORMULA3,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 54: excel.setCellValue(Function.getValueOfDouble(rs.getString(HOZEN)));break;
		    			case 55: excel.setCellValue(Function.getValueOfDouble(rs.getString(SONOTAKAISYU)));break;
		    			case 56:
			    			formula = Function.replaceExpression(FORMULA4,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 57: excel.setCellValue(Function.getValueOfDouble(rs.getString(KIHIKIATEKIN)));break;
		    			case 58:
			    			formula = Function.replaceExpression(FORMULA5,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
		    			case 59: excel.setCellValue(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)));break;
		    			case 60:
			    			formula = Function.replaceExpression(FORMULA6,String.valueOf(i+1));
			    			excel.setCellFormula(formula);
			    			break;
	    				default:break;
		    		}
		    	}
				excel.selectCell(25, true);
				excel.setCellStyle(25);
    			excel.setCellValue(getKyuKijunGaitou(rs.getString(TOUKI_AKAJI_KYU), rs.getString(TAIRYU_KBN), rs.getString(TEI_KTK), rs.getString(YOTYUUI), rs.getString(AKAJI), rs.getString(SAIMU_TYOKA), rs.getString(M12), rs.getString(KASIDAORE_KENEN),rs.getString(HASAN_KOSEI), rs.getString(RIKI_SITEI)));

    			excel.selectCell(26, true);
    			excel.setCellStyle(26);
    			excel.setCellValue(getZengakuHikiateZumi(rs.getString(KOTEIKA_EIGYOU_SAIKEN),calcForm1(rs.getString(UKETORI_TEGATA), rs.getString(YUSYUTU_UKETORI_TEGATA), rs.getString(URIKAKEKIN), rs.getString(TORIHIKI_MAEWATASHIKIN), rs.getString(TATEKAEKIN), rs.getString(MISYUUNYUUKIN), rs.getString(MISYUUSYUEKI), rs.getString(TANKI_KASITUKEKIN), rs.getString(SASHIIRE_HOSYOKIN), rs.getString(KARIBARAIKIN), rs.getString(TYOKI_KASITUKEKIN), rs.getString(SONOTA_TOUSI)), rs.getString(HOSYO_SAIMUKEI),rs.getString(KIHIKIATEKIN)));

    			excel.selectCell(33, true);
    			excel.setCellStyle(33);
    			if(zengakuHikiateZumi != null && zengakuHikiateZumi.equals("1")){
    				excel.setCellValue("");
    			}
    			else{
    				excel.setCellValue(getSateiTaisyoKijunGaitou(rs.getString(TOUKI_AKAJI_NEW), rs.getString(SAIMU_TYOKA_NEW), rs.getString(M12_NEW), rs.getString(KASIDAORE_KENEN_NEW), rs.getString(HASAN_KOSEI_NEW), rs.getString(RIKI_SITEI_NEW)));
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
	public String getKyuKijunGaitou(String toukiAkaji, String tairyuKbn, String teiKtk, String youChuui, String akaji, String saimuChouka, String m12, String kashiDaoreKenen, String hasanKousei, String ribuShitei){
		String kyuKijunGaitou = "";
		if((toukiAkaji != null && toukiAkaji.equals("1"))
				|| (tairyuKbn != null && tairyuKbn.equals("1"))
				|| (teiKtk != null && teiKtk.equals("1"))
				|| (youChuui != null && youChuui.equals("1"))
				|| (akaji != null && akaji.equals("1"))
				|| (saimuChouka != null && saimuChouka.equals("1"))
				|| (m12 != null && m12.equals("1"))
				|| (kashiDaoreKenen != null && kashiDaoreKenen.equals("1"))
				|| (hasanKousei != null && hasanKousei.equals("1"))
				|| (ribuShitei != null && ribuShitei.equals("1"))){

			kyuKijunGaitou = "1";
			return kyuKijunGaitou;
		}
		else{
			return kyuKijunGaitou;
		}
	}
	public String getSateiTaisyoKijunGaitou(String toukiAkaji, String saimuChouka, String m12, String kashiDaoreKenen, String hasanKousei, String ribuShitei){
		String sateiTaisyoKijunGaitou = "";
		if((toukiAkaji != null && toukiAkaji.equals("1"))
				|| (saimuChouka != null && saimuChouka.equals("1"))
				|| (m12 != null && m12.equals("1"))
				|| (kashiDaoreKenen != null && kashiDaoreKenen.equals("1"))
				|| (hasanKousei != null && hasanKousei.equals("1"))
				|| (ribuShitei != null && ribuShitei.equals("1"))){

			sateiTaisyoKijunGaitou = "1";
			return sateiTaisyoKijunGaitou;
		}
		else{
			return sateiTaisyoKijunGaitou;
		}
	}
	public String getZengakuHikiateZumi(String koteiKa, String saikenZanDaka, String hoshoSaimu, String kiHikiateKinKingaku){
		ArrayList<String> list = new ArrayList<String>();
		long zengakuHikiateZumiKingakuVar = 0;
		list.add(koteiKa);
		list.add(hoshoSaimu);
		list.add(saikenZanDaka);

		for(String value : list){
			if(value != null){
				zengakuHikiateZumiKingakuVar += Long.parseLong(value);
			}
		}
		String zengakuHikiateZumi = "";
		if(kiHikiateKinKingaku != null && (zengakuHikiateZumiKingakuVar == Long.parseLong(kiHikiateKinKingaku))){
			zengakuHikiateZumi = "1";
		}
		else{
			zengakuHikiateZumi = "";
		}
		return zengakuHikiateZumi;
	}
	public String calcForm1(String ukeTori, String yuShutsu, String uriKakeKin, String toriHikiMae, String tateKaeKin, String miShuNyuKin, String miShuuShuuEki ,String tanKi, String hoshoKin, String kariBaraiKin, String chouKi, String sonoTa){
		ArrayList<String> list = new ArrayList<String>();
		long amount = 0;

		list.add(ukeTori);
		list.add(yuShutsu);
		list.add(uriKakeKin);
		list.add(toriHikiMae);
		list.add(tateKaeKin);
		list.add(miShuNyuKin);
		list.add(miShuuShuuEki);
		list.add(tanKi);
		list.add(hoshoKin);
		list.add(kariBaraiKin);
		list.add(chouKi);
		list.add(sonoTa);

		for(String value : list){
			if(value != null){
				amount +=Long.parseLong(value);
			}
		}
		String returnValue = Long.toString(amount);
		return returnValue;
	}

	public String akajiZeroFilter(String akaji){
		if(akaji != null && akaji.equals("0")){
			return "";
		}
		else{
			return akaji;
		}
	}
}