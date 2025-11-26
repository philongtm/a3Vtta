/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
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
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* LC3101_留保債務帳票  DBアクセスクラス
*/
public class SaikenMeisaiExcelDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;			// 機能共通セッション
	private TorihikisakiBean tori_bean = null;		// 取引先情報
	private AppContext appContext = null;			// ＡＰＰコンテキスト
	private AppPagerActionForm appform = null;		// アクションフォーム

	//Resultset用文字列	
	private static final String SAKUSEI_SYONIN_DT		= "sakusei_syonin_dt";
	private static final String TORIHIKI_NM			= "torihiki_nm";
	private static final String SAIKEN_FLG			= "saiken_flg";
	private static final String TAIRYU_HANTEI			= "tairyu_hantei";
	private static final String KINGAKU				= "kingaku";
	private static final String BUNRUI1_NM			= "bunrui1_nm";
	private static final String PHASE					= "phase";
	private static final String KIKAN_TORI_CD        	= "kikan_tori_cd";
	private static final String BUSINESS_NM			= "business_nm";
	private static final String KTK             		= "ktk";
	private static final String MISE_CD            	= "mise_cd";
	private static final String JIMUSHO_CD			= "jimusho_cd";
	private static final String BU_CD					= "bu_cd";
	private static final String BU_NM					= "bu_nm";
	private static final String KA_CD					= "ka_cd";
	private static final String KA_NM					= "ka_nm";
	private static final String CELL_CD				= "cell_cd";
	private static final String CELL_NM				= "cell_nm";
	private static final String KANJO_CD            	= "kanjo_cd";
	private static final String KANJO_NM            	= "kanjo_nm";
	private static final String SHUSI_DT            	= "shusi_dt";
	private static final String SYORI_DT            	= "syori_dt";
	private static final String TAIRYU_KBN			= "tairyu_kbn";
	private static final String INVOICE_NO   			= "invoice_no";
	private static final String KOMOKU1   			= "komoku1";
	private static final String KOMOKU2   			= "komoku2";
	private static final String KOMOKU3   			= "komoku3";
	private static final String KEIYAKU_DENPYO_NO   	= "keiyaku_denpyo_no";
	private static final String TUUKA_CD    			= "tuuka_cd";
	private static final String HANTEI_JIYUU 			= "hantei_jiyuu";
	private static final String BUNRUI2_NM 			= "bunrui2_nm";

	private static final String SAIKEN = "1";		//査定対象債権
	private static final String SAIMU = "3";		//保証債務
	private static final String HIKIATE = "9";	//貸倒引当金
	private static final String TAIRYU = "1";		//6ヶ月超滞留
	private static final int StartRow = 10;		//明細出力開始行
	private static final int endClm = 23;		//明細出力最終列
	private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";	//フォーマット

	private static final String SP_SS_L_SELECT_CHOHYOHEAD     = "SP_SS_L_SELECT_CHOHYOHEAD";		//ヘッダ部取得プロシージャ
    private static final String SP_SS_L_SELECT_E0200          = "SP_SS_L_SELECT_E0200";			//取引先名称取得プロシージャ;
	private static final String SP_SS_OL_SELECT_SENTEIKEI     = "SP_SS_OL_SELECT_SENTEIKEI";		//合計金額取得プロシージャ
	private static final String SP_SS_LC1101_SELECT_ICHIRAN   = "SP_SS_LC1101_SELECT_ICHIRAN";	//一覧情報取得プロシージャ
	private static final String SP_SS_LC1101_SELECT_SAIKENKEI = "SP_SS_LC1101_SELECT_SAIKENKEI";	//合計金額取得プロシージャ
	private static final String SP_SS_LC1101_SELECT_ICHIRAN2  = "SP_SS_LC1101_SELECT_ICHIRAN2";	//一覧情報取得プロシージャ

	private String tyohyo_lang;
	private String outKanjoNm;

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SaikenMeisaiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		cmnData = appContext.getCMN();
		tori_bean = cmnData.getTori_bean();
		appform = (AppPagerActionForm)appContext.getActionForm();
		tyohyo_lang = appform.getLangMode();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
		tyohyo_lang = GS.EMPTY_CHARCTER;
	}

	/**
	 * ヘッダ部情報取得(査定内容詳細よりダウンロード時) <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getHeader(Excel excel) throws SQLException {
		String phase = tori_bean.getPhase();
		if(tori_bean.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
			phase = tori_bean.getC_phase();
		}
		
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(tori_bean.getStatus());
	    exCstmt.setStringIn(tori_bean.getAnken_no());
	    exCstmt.setStringIn(tori_bean.getKaisya_cd());
	    exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getMise_cd());
	    exCstmt.setStringIn(appform.toString());
		exCstmt.setResultSet(RESULTSET);
		
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()){
				excel.selectCell(0,23,false);
				excel.setCellValue(rs.getString(SAKUSEI_SYONIN_DT));
				excel.selectCell(1,23,false);
				excel.setCellValue(rs.getString(BUNRUI2_NM));
				excel.selectCell(4,23,false);
				excel.setCellValue(rs.getString(PHASE));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
		
	/**
     * 
     *  取引先名称を取得<br>
     * @param Excel
     * @throws SQLException
     */
	public void getKanjoNm(Excel excel) throws SQLException{
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_E0200, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setStringIn(Function.trim(tori_bean.getTaisyo_ym()));
		exCstmt.setStringIn(Function.trim(tori_bean.getShikibetu_cd()));
		exCstmt.setStringIn(Function.trim(tori_bean.getTogo_tori_cd()));
		exCstmt.setStringIn(Function.trim(tori_bean.getSyori_kaisu()));
		exCstmt.setStringIn(Function.trim(tori_bean.getSystem_kbn()));
		exCstmt.setStringIn(Function.trim(tori_bean.getSateikaisya_cd()));
		exCstmt.setStringIn(Function.trim(tori_bean.getMise_cd()));
		exCstmt.setResultSet(RESULTSET);
		try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()) {
            	//明細出力用に保持
            	outKanjoNm = rs.getString(TORIHIKI_NM);
			}
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
	}

	/**
	 * 合計金額取得(査定内容詳細よりダウンロード時)  <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getTotal_kingaku(Excel excel) throws SQLException {
		double saiken = 0;
		double saimu = 0;
		double hikiate = 0;
		double tairyu = 0;
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_SENTEIKEI, sqlExec);
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){
				if(SAIKEN.equals(rs.getString(SAIKEN_FLG))){
					saiken = saiken + Function.getValueOfDouble(rs.getString(KINGAKU));
				}else if(SAIMU.equals(rs.getString(SAIKEN_FLG))){
					saimu = saimu + Function.getValueOfDouble(rs.getString(KINGAKU));
				}else if(HIKIATE.equals(rs.getString(SAIKEN_FLG))){
					hikiate = hikiate + Function.getValueOfDouble(rs.getString(KINGAKU));
				}
				if(TAIRYU.equals(rs.getString(TAIRYU_HANTEI))){
					tairyu = tairyu + Function.getValueOfDouble(rs.getString(KINGAKU));
				}
			}
			//債権総計
			excel.selectCell(4,22,false);
			excel.setCellValue(saiken);
			//保証債務合計
			excel.selectCell(5,22,false);
			excel.setCellValue(saimu);
			//引当金合計
			excel.selectCell(6,22,false);
			excel.setCellValue(hikiate);
			//滞留債権計
			excel.selectCell(7,22,false);
			excel.setCellValue(tairyu);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 一覧情報取得処理(査定内容詳細よりダウンロード時) <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public boolean getMeisai(Excel excel) throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LC1101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(tori_bean.getAnken_no());
	    exCstmt.setStringIn(tori_bean.getKijunbi_kbn());
		exCstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			//明細出力
    		int i = StartRow; 
    		while ( rs.next() ) {
    			result = true;
    			excel.selectRow(i,true);
		    	for(int j=0;j<=endClm;j++){
			    	excel.selectCell(j,true);
		        	excel.setCellStyle(j);
			    	switch (j) {
			    		case 0: excel.setCellValue(tori_bean.getKanjo_cd());break;
	    				case 1: excel.setCellValue(outKanjoNm);break;
	    				case 2: excel.setCellValue(tori_bean.getSinyoktk());break;
	    				case 3: excel.setCellValue(rs.getString(MISE_CD));break;
	    				case 4: excel.setCellValue(rs.getString(JIMUSHO_CD));break;
	    				case 5: excel.setCellValue(rs.getString(BU_CD));break;
	    				case 6: excel.setCellValue(rs.getString(BU_NM));break;
	    				case 7: excel.setCellValue(rs.getString(KA_CD));break;
	    				case 8: excel.setCellValue(rs.getString(KA_NM));break;
	    				case 9: excel.setCellValue(rs.getString(CELL_CD));break;
	    				case 10: excel.setCellValue(rs.getString(CELL_NM));	break;
	    				case 11: excel.setCellValue(rs.getString(KANJO_CD));break;
	    				case 12: excel.setCellValue(rs.getString(KANJO_NM));break;
	    				case 13: excel.setCellValue(rs.getString(SHUSI_DT));break;
	    				case 14: excel.setCellValue(rs.getString(SYORI_DT));break;
	    				case 15: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
	    				case 16: excel.setCellValue(rs.getString(TAIRYU_HANTEI));break;
	    				case 17: excel.setCellValue(rs.getString(INVOICE_NO));break;
	    				case 18: excel.setCellValue(rs.getString(KOMOKU1));break;
	    				case 19: excel.setCellValue(rs.getString(KOMOKU2));break;
	    				case 20: excel.setCellValue(rs.getString(KOMOKU3));break;
	    				case 21: excel.setCellValue(rs.getString(KEIYAKU_DENPYO_NO));break;
	    				case 22: excel.setCellValue(Function.format(NUM_FMT_KAIGAI,Function.getValueOfDouble(rs.getString(KINGAKU))) + Function.trim(rs.getString(TUUKA_CD)));break;
	    				case 23: excel.setCellValue(rs.getString(HANTEI_JIYUU));break;
	    				default:break;
			    	}
		    	}
		    	i++;
    		}
    		return result;
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}

	/**
	 * 合計金額取得(帳票ダウンロードよりダウンロード時) <br>
	 * 
     * @param Excel
     * @param DownloadForm
	 * @exception SQLException
	 */
	public void getTotal_kingaku2(Excel excel,DownloadForm form) throws SQLException {
		double saiken = 0;
		double saimu = 0;
		double hikiate = 0;
		double tairyu = 0;
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LC1101_SELECT_SAIKENKEI, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(form.getSearch_system_kbn());
	    exCstmt.setStringIn(form.getSearch_sateiki());
	    exCstmt.setStringIn(form.getSearch_sateikaisya_cd());
	    exCstmt.setStringIn(form.getSearch_bunrui2());
	    exCstmt.setStringIn(form.getSearch_taisyo_ym());
	    exCstmt.setStringIn(Function.addSingleQuotation(form.getDuns_no()));
	    exCstmt.setStringIn(form.getKanjo_cd());
	    exCstmt.setStringIn(form.getKanjo_nm());
	    exCstmt.setStringIn(form.toString());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			int i=0;
			while(rs.next()){
				if(i==0){
					excel.selectCell(0,23,false);
					excel.setCellValue(rs.getString(SAKUSEI_SYONIN_DT));
				}
				if(SAIKEN.equals(rs.getString(SAIKEN_FLG))){
					saiken = saiken + Function.getValueOfDouble(rs.getString(KINGAKU));
				}else if(SAIMU.equals(rs.getString(SAIKEN_FLG))){
					saimu = saimu + Function.getValueOfDouble(rs.getString(KINGAKU));
				}else if(HIKIATE.equals(rs.getString(SAIKEN_FLG))){
					hikiate = hikiate + Function.getValueOfDouble(rs.getString(KINGAKU));
				}
				if(TAIRYU.equals(rs.getString(TAIRYU_HANTEI))){
					tairyu = tairyu + Function.getValueOfDouble(rs.getString(KINGAKU));
				}
			}
			//債権総計
			excel.selectCell(4,22,false);
			excel.setCellValue(saiken);
			//保証債務合計
			excel.selectCell(5,22,false);
			excel.setCellValue(saimu);
			//引当金合計
			excel.selectCell(6,22,false);
			excel.setCellValue(hikiate);
			//滞留債権計
			excel.selectCell(7,22,false);
			excel.setCellValue(tairyu);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 一覧情報取得処理(帳票ダウンロードよりダウンロード時) <br>
	 * 
     * @param Excel
     * @param DownloadForm
	 * @exception SQLException
	 */
	public boolean getMeisai2(Excel excel,DownloadForm form) throws SQLException {
		boolean result = false;
		String bunrui1_hantei = GS.EMPTY_CHARCTER;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LC1101_SELECT_ICHIRAN2, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(form.getSearch_system_kbn());
	    exCstmt.setStringIn(form.getSearch_sateiki());
	    exCstmt.setStringIn(form.getSearch_sateikaisya_cd());
	    exCstmt.setStringIn(form.getSearch_bunrui2());
	    exCstmt.setStringIn(form.getSearch_taisyo_ym());
	    exCstmt.setStringIn(Function.addSingleQuotation(form.getDuns_no()));
	    exCstmt.setStringIn(form.getKanjo_cd());
	    exCstmt.setStringIn(form.getKanjo_nm());
		exCstmt.setResultSet(RESULTSET);
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			//明細出力
    		int i = StartRow; 
    		while ( rs.next() ) {
    			result = true;
    			//分類１表示判定
	    		if(i==StartRow){
		    		bunrui1_hantei = rs.getString(BUNRUI1_NM);
	    		}
	    		//明細に複数分類１が存在する場合、ヘッダ部の分類１は表示しない
	    		if(!bunrui1_hantei.equals(rs.getString(BUNRUI1_NM))){
	    			bunrui1_hantei = GS.EMPTY_CHARCTER;
	    		}
    			excel.selectRow(i,true);
		    	for(int j=0;j<=endClm;j++){
			    	excel.selectCell(j,true);
		        	excel.setCellStyle(j);
			    	switch (j) {
			    		case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
	    				case 1: excel.setCellValue(rs.getString(BUSINESS_NM));break;
	    				case 2: excel.setCellValue(rs.getString(KTK));break;
	    				case 3: excel.setCellValue(rs.getString(MISE_CD));break;
	    				case 4: excel.setCellValue(rs.getString(JIMUSHO_CD));break;
	    				case 5: excel.setCellValue(rs.getString(BU_CD));break;
	    				case 6: excel.setCellValue(rs.getString(BU_NM));break;
	    				case 7: excel.setCellValue(rs.getString(KA_CD));break;
	    				case 8: excel.setCellValue(rs.getString(KA_NM));break;
	    				case 9: excel.setCellValue(rs.getString(CELL_CD));break;
	    				case 10: excel.setCellValue(rs.getString(CELL_NM));	break;
	    				case 11: excel.setCellValue(rs.getString(KANJO_CD));break;
	    				case 12: excel.setCellValue(rs.getString(KANJO_NM));break;
	    				case 13: excel.setCellValue(rs.getString(SHUSI_DT));break;
	    				case 14: excel.setCellValue(rs.getString(SYORI_DT));break;
	    				case 15: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
	    				case 16: excel.setCellValue(rs.getString(TAIRYU_HANTEI));break;
	    				case 17: excel.setCellValue(rs.getString(INVOICE_NO));break;
	    				case 18: excel.setCellValue(rs.getString(KOMOKU1));break;
	    				case 19: excel.setCellValue(rs.getString(KOMOKU2));break;
	    				case 20: excel.setCellValue(rs.getString(KOMOKU3));break;
	    				case 21: excel.setCellValue(rs.getString(KEIYAKU_DENPYO_NO));break;
	    				case 22: excel.setCellValue(Function.format(NUM_FMT_KAIGAI,Function.getValueOfDouble(rs.getString(KINGAKU))) + Function.trim(rs.getString(TUUKA_CD)));break;
	    				case 23: excel.setCellValue(rs.getString(HANTEI_JIYUU));break;
	    				default:break;
			    	}
		    	}
		    	i++;
    		}
    		//分類１名称の表示
    		if(!GS.EMPTY_CHARCTER.equals(bunrui1_hantei)){
		    	excel.selectCell(1,23,false);
				excel.setCellValue(bunrui1_hantei);
    		}
    		return result;
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
}