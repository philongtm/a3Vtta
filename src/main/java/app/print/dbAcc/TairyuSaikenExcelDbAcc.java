/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/12/18		SSC				新規作成 
******************************************************************************/
package app.print.dbAcc;

import app.system.form.DownloadForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* LB1102 債権明細一覧
*/
public class TairyuSaikenExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String SAKUSEI_DT			= "SAKUSEI_DT";
	private static final String BUNRUI1_NM			= "BUNRUI1_NM";
	private static final String BUSINESS_NM			= "business_nm";
	private static final String TORI_CD_7				= "tori_cd_7";
	private static final String BU_CD					= "bu_cd";
	private static final String BU_NM					= "bu_nm";
	private static final String CELL_CD				= "cell_cd";
	private static final String CELL_NM				= "cell_nm";
	private static final String INIT_BUNRUI2			= "init_bunrui2";	
	private static final String KANJO_CD				= "kanjo_cd";
	private static final String KANJO_NM				= "kanjo_nm";
	private static final String KANJO_UCHI_CD			= "kanjo_uchi_cd";
	private static final String KANJO_UCHI_NM			= "kanjo_uchi_nm";
	private static final String SAIKEN_FLG			= "saiken_flg";
	private static final String SHUSI_DT				= "shusi_dt";
	private static final String MANKI_DT				= "manki_dt";
	private static final String SYORI_DT				= "syori_dt";
	private static final String KEIYAKU_DENPYO_NO		= "keiyaku_denpyo_no";
	private static final String KINGAKU				= "kingaku";
	private static final String HANTEI_JIYUU			= "hantei_jiyuu";
	private static final String TAIRYU_KBN			= "tairyu_kbn";
	private static final String TAIRYU_HANTEI			= "tairyu_hantei";
	
	private static final String SAIKEN				= "1";
	private static final String SAIMU					= "3";
	private static final String HIKIATE				= "9";
	

	//エラーメッセージ
	private static final String WARNING0004 = "warning.0004";	;

	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LB1102_SELECT_ICHIRAN 	= "SP_SS_LB1102_SELECT_ICHIRAN"; //一覧情報取得プロシージャ
	
	// 帳票言語
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public TairyuSaikenExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;
		form = (DownloadForm)appContext.getActionForm();
		tyohyo_lang = form.getLangMode();;
		
	}

	/**
	 * ヘッダ部情報取得 <br>
	 * 
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
				excel.selectCell(0,18,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				
				//分類１
				excel.selectCell(1,18,true);
				excel.setCellValue(rs.getString(BUNRUI1_NM));

			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 *　明細情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
		public boolean getMeisai(Excel excel) throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		
		// ヘッダ部合計金額
		double saiken = 0;
		double saimu = 0;
		double hikiate = 0;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LB1102_SELECT_ICHIRAN, sqlExec);
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
			
	    	int i = 10;
	    	while ( rs.next() ) {
	    		
	    		result = true;
	
	    		excel.selectRow(i, true);
	    
	    		for(int j = 0;j<19;j++){
	    			
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				
	    			switch(j){
	    				case 0:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(TORI_CD_7));
	    					excel.setCellStyle(j);
	    					break;
	    				case 1:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(BUSINESS_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 2:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(INIT_BUNRUI2));
	    					excel.setCellStyle(j);
	    					break;
	    				case 3:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(BU_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 4:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(BU_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 5:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(CELL_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 6:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(CELL_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 7:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 8:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 9:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_UCHI_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 10:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_UCHI_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 11:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(SHUSI_DT));
	    					excel.setCellStyle(j);
	    					break;
	    				case 12:
		    				excel.selectCell(j,true);
		    				excel.setCellValue(rs.getString(MANKI_DT));
		    				excel.setCellStyle(j);
		    				break;
	    				case 13:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(Function.trim(rs.getString(SYORI_DT)));
	    					excel.setCellStyle(j);
	    					break;
	    				case 14:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(TAIRYU_KBN));
	    					excel.setCellStyle(j);
	    					break;
	    				case 15:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(TAIRYU_HANTEI));
	    					excel.setCellStyle(j);
	    					break;
	    				case 16:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KEIYAKU_DENPYO_NO));
	    					excel.setCellStyle(j);
	    					break;
	    				case 17:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getDouble(KINGAKU));
	    					excel.setCellStyle(j);
	    					break;
	    				case 18:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(HANTEI_JIYUU));
	    					excel.setCellStyle(j);
	    					break;
	    				default:
	    					break;
	    			}
	    		}
	    		i++;
	    		
    			// ヘッダ部金額の計算
    			if(SAIKEN.equals(rs.getString(SAIKEN_FLG))){
					saiken = saiken + rs.getDouble(KINGAKU);
				}else if(SAIMU.equals(rs.getString(SAIKEN_FLG))){
					saimu = saimu + rs.getDouble(KINGAKU);
				}else if(HIKIATE.equals(rs.getString(SAIKEN_FLG))){
					hikiate = hikiate + rs.getDouble(KINGAKU);
				}

	    	}
			//債権総計
			excel.selectCell(4,17,true);
			excel.setCellValue(saiken);
			//保証債務合計
			excel.selectCell(5,17,true);
			excel.setCellValue(saimu);		
			//引当金合計
			excel.selectCell(6,17,true);
			excel.setCellValue(hikiate);
			
    		if(!result){
    			appContext.setMsgCode(WARNING0004);
    		}

    		return result;

	    }finally{
	    	if (rs != null) {
    			rs.close();
	    		}
	    	}
		}
	}
