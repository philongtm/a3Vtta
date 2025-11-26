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
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* LB_1101 実質滞留債権判定帳票タブ  DBアクセスクラス
*/
public class TairyuExcelDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private AppPagerActionForm app_form = null;

	//Resultset用文字列	
	private static final String SHUSI_DT            	= "shusi_dt";
	private static final String KINGAKU             	= "kingaku";
	private static final String TUUKA_CD				= "tuuka_cd";
	private static final String SYORI_DT				= "syori_dt";
	private static final String TAIRYU_KBN			= "tairyu_kbn";
	private static final String KOMOKU1				= "komoku1";
	private static final String KOMOKU2				= "komoku2";
	private static final String KOMOKU3				= "komoku3";
	private static final String PHASE					= "phase";
	private static final String ANKEN_NO_EDA			= "anken_no_eda";
	private static final String BUNRUI2_NM			= "bunrui2_nm";
	private static final String TORIHIKI_NM			= "torihiki_nm";
	private static final String TAIRYU_JDG			= "tairyu_jdg";
	private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";
	private static final String JIMUSHO_CD			= "jimusho_cd";
	private static final String BU_NM					= "bu_nm";
	private static final String KA_CD					= "ka_cd";
	private static final String KA_NM					= "ka_nm";
	private static final String CELL_CD				= "cell_cd";
	private static final String CELL_NM				= "cell_nm";
	private static final String KANJO_CD				= "kanjo_cd";
	private static final String KANJO_NM				= "kanjo_nm";
	private static final String KEIYAKU_DENPYO_NO		= "keiyaku_denpyo_no";
	private static final String INVOICE_NO			= "invoice_no";
	private static final String TAIRYU_HANTEI			= "tairyu_hantei";
	private static final String HANTEI_JIYUU			= "hantei_jiyuu";
	private static final String SAKUSEI_SYONIN_DT		= "sakusei_syonin_dt";

	private static final String SP_SS_L_SELECT_E0200		 	= "SP_SS_L_SELECT_E0200";
	//取引先名称を取得するプロシージャ
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD		= "SP_SS_L_SELECT_CHOHYOHEAD";
	//ヘッダ情報を取得するプロシージャ

	private static final String SP_SS_LB1101_SELECT_ICHIRAN	= "SP_SS_LB1101_SELECT_ICHIRAN";
	//取引先の実質滞留債権判定明細一覧を取得するプロシージャ

	private static final String SP_SS_OL_SELECT_P0200			= "SP_SS_OL_SELECT_P0200";
	//滞留判定リストの設定値を取得するプロシージャ
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public TairyuExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		torihikisaki_bean = cmnData.getTori_bean();
		app_form = (AppPagerActionForm)appContext.getActionForm();
		
	}

	/**
	 * ヘッダ部情報取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getChohyo(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD, sqlExec);
		exCstmt.setStringIn(app_form.getLangMode());
	    exCstmt.setStringIn(torihikisaki_bean.getPhase());
	    exCstmt.setStringIn(torihikisaki_bean.getStatus());
	    exCstmt.setStringIn(torihikisaki_bean.getAnken_no());
	    exCstmt.setStringIn(torihikisaki_bean.getKaisya_cd());
	    exCstmt.setStringIn(torihikisaki_bean.getSateikaisya_cd());
	    exCstmt.setStringIn(torihikisaki_bean.getSystem_kbn());
	    exCstmt.setStringIn(Function.trim(torihikisaki_bean.getMise_cd()));
	    exCstmt.setStringIn(app_form.toString());
		exCstmt.setResultSet(RESULTSET);	
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
		while(rs.next()){
			//作成日・承認日
			excel.selectCell(0,20,true);
			excel.setCellValue(rs.getString(SAKUSEI_SYONIN_DT));
			//分類２名称
			excel.selectCell(1,20,true);
			excel.setCellValue(rs.getString(BUNRUI2_NM));
			//フェーズ名
			excel.selectCell(4,20,true);
			excel.setCellValue(rs.getString(PHASE));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    	
		}
	}
	    	
	public void getSelect(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exBstmt = new ExCallableStatement(SP_SS_L_SELECT_E0200, sqlExec);
		
		exBstmt.setStringIn(app_form.getLangMode());
	    exBstmt.setStringIn(torihikisaki_bean.getTaisyo_ym());
	    exBstmt.setStringIn(Function.trim(torihikisaki_bean.getShikibetu_cd()));
	    exBstmt.setStringIn(Function.trim(torihikisaki_bean.getTogo_tori_cd()));
	    exBstmt.setStringIn(torihikisaki_bean.getSyori_kaisu());
	    exBstmt.setStringIn(torihikisaki_bean.getSystem_kbn());
	    exBstmt.setStringIn(torihikisaki_bean.getSateikaisya_cd());
	    exBstmt.setStringIn(Function.trim(torihikisaki_bean.getMise_cd()));
		exBstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exBstmt.execute();
	    	isError(exBstmt);
			rs = exBstmt.getResultSet(RESULTSET);
		while(rs.next()){
		//取引先情報.勘定先名称
		excel.selectCell(5,2,true);
		excel.setCellValue(rs.getString(TORIHIKI_NM));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    	
		}
	}
	
	public void getTairyu(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);

	    exCstmt.setStringIn(TAIRYU_JDG);
		exCstmt.setStringIn(app_form.getLangMode());
	    exCstmt.setStringIn(torihikisaki_bean.getSystem_kbn());
		exCstmt.setResultSet(RESULTSET);
		try{
			exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			int i = 2;
			while ( rs.next() ) {
				excel.selectCell(i,21,true);
				excel.setCellValue(rs.getString(KBN_HYOUJI_VAL));
				i++;
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
		public void getMeisai(Excel excel) throws SQLException {
		ResultSet rs = null;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LB1101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(app_form.getLangMode());
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(torihikisaki_bean.getSystem_kbn());
		exCstmt.setStringIn(torihikisaki_bean.getPhase());
		exCstmt.setStringIn(torihikisaki_bean.getInit_bunrui2());
		exCstmt.setStringIn(torihikisaki_bean.getInit_bu_cd());
		exCstmt.setStringIn(torihikisaki_bean.getAnken_no());
		exCstmt.setResultSet(RESULTSET);		
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
	    	int i = 8;
	    	while ( rs.next() ) {
	    		
	    		if(i == 8) {
	    			//通貨コード
	    			excel.selectCell(7,10,true);
	    			String tuu = excel.getStringCellValue();
	    			String tcd = tuu + rs.getString(TUUKA_CD);
	    			excel.setCellValue(tcd);
	    		}
	    		
	    		excel.selectRow(i,true);
	    		for(int j = 0;j<21;j++){
	    			switch(j){
	    				case 0:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(ANKEN_NO_EDA));
	    					excel.setCellStyle(j);
	    					break;
	    				case 1:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(JIMUSHO_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 2:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(torihikisaki_bean.getInit_bu_cd());
	    					excel.setCellStyle(j);
	    					break;
	    				case 3:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(BU_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 4:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KA_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 5:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KA_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 6:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(CELL_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 7:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(CELL_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 8:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_CD));
	    					excel.setCellStyle(j);
	    					break;
	    				case 9:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KANJO_NM));
	    					excel.setCellStyle(j);
	    					break;
	    				case 10:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getDouble(KINGAKU));
	    					excel.setCellStyle(j);
	    					break;
	    				case 11:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(SHUSI_DT));
	    					excel.setCellStyle(j);
	    					break;
	    				case 12:
		    				excel.selectCell(j,true);
		    				excel.setCellValue(rs.getString(SYORI_DT));
		    				excel.setCellStyle(j);
		    				break;
	    				case 13:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(Function.trim(rs.getString(KOMOKU2)));
	    					excel.setCellStyle(j);
	    					break;
	    				case 14:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KEIYAKU_DENPYO_NO));
	    					excel.setCellStyle(j);
	    					break;
	    				case 15:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(INVOICE_NO));
	    					excel.setCellStyle(j);
	    					break;
	    				case 16:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KOMOKU1));
	    					excel.setCellStyle(j);
	    					break;
	    				case 17:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(KOMOKU3));
	    					excel.setCellStyle(j);
	    					break;
	    				case 18:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(TAIRYU_KBN));
	    					excel.setCellStyle(j);
	    					break;
	    				case 19:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(TAIRYU_HANTEI));
	    					excel.setCellStyle(j);
	    					break;
	    				case 20:
	    					excel.selectCell(j,true);
	    					excel.setCellValue(rs.getString(HANTEI_JIYUU));
	    					excel.setCellStyle(j);
	    					break;
	    				default:
	    					break;
	    		}
	    		
	    	}
	    		i++;
	    	}
	    }finally{
	    	if (rs != null) {
    			rs.close();
	    		}
	    	}
		}
	}
