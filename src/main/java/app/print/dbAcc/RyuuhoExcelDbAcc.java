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
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* LC3101_留保債務帳票  DBアクセスクラス
*/
public class RyuuhoExcelDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private AppPagerActionForm app_form = null;

	//Resultset用文字列	
	private static final String SAKUSEI_SYONIN_DT		= "sakusei_syonin_dt";
	private static final String BUNRUI2_NM			= "bunrui2_nm";
	private static final String PHASE					= "phase";
	
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
	private static final String KEIYAKU_DENPYO_NO   	= "keiyaku_denpyo_no";
	private static final String INVOICE_NO   			= "invoice_no";
	private static final String KINGAKU				= "kingaku";
	private static final String RYUHOSAIMU_KBN    	= "ryuhosaimu_kbn";
	private static final String BIKO    				= "biko";
	private static final String TUUKA_CD    			= "tuuka_cd";
	private static final String SAN    				= "30";
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD 	= "SP_SS_L_SELECT_CHOHYOHEAD";	//帳票のヘッダ部を取得するプロシージャ
	private static final String SP_SS_OL_SELECT_RYUHO 		= "SP_SS_OL_SELECT_RYUHO";		//一覧情報を取得するプロシージャ

	// INパラメータ
	private String tyohyo_lang;
	private String phase;
	private String status;
	private String anken_no;
	private String kaisya_cd;
	private String sateikaisya_cd;
	private String system_kbn;
	private String mise_cd;
	private String gamen_id;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public RyuuhoExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		torihikisaki_bean = cmnData.getTori_bean();
		app_form = (AppPagerActionForm)appContext.getActionForm();
	
		//ビーンの値を変数に設定
		tyohyo_lang = app_form.getLangMode();
		phase = torihikisaki_bean.getPhase();
		status = torihikisaki_bean.getStatus();
		anken_no = torihikisaki_bean.getAnken_no();
		kaisya_cd = torihikisaki_bean.getKaisya_cd();
		sateikaisya_cd = torihikisaki_bean.getSateikaisya_cd();
		system_kbn = torihikisaki_bean.getSystem_kbn();
		mise_cd = torihikisaki_bean.getMise_cd();
		gamen_id = app_form.toString(); 
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		tyohyo_lang = GS.EMPTY_CHARCTER;
		phase = GS.EMPTY_CHARCTER;
		status = GS.EMPTY_CHARCTER;
		anken_no = GS.EMPTY_CHARCTER;
		kaisya_cd = GS.EMPTY_CHARCTER;
		sateikaisya_cd = GS.EMPTY_CHARCTER;
		system_kbn = GS.EMPTY_CHARCTER;
		mise_cd = GS.EMPTY_CHARCTER;
	}

	/**
	 * ヘッダ部情報取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHeader(Excel excel) throws SQLException {
		
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD, sqlExec);
		if(torihikisaki_bean.getC_phase().equals(GS.PHASE_TAISHOSAKI_SENTEI)){
			phase = torihikisaki_bean.getC_phase();
		}
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(status);
	    exCstmt.setStringIn(anken_no);
	    exCstmt.setStringIn(kaisya_cd);
	    exCstmt.setStringIn(sateikaisya_cd);
	    exCstmt.setStringIn(system_kbn);
	    exCstmt.setStringIn(mise_cd);
	    exCstmt.setStringIn(gamen_id);
		exCstmt.setResultSet(RESULTSET);
		
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			while(rs.next()){
			//帳票の作成
			excel.selectRow(0,true);
			excel.selectCell(14,true);
			excel.setCellValue(rs.getString(SAKUSEI_SYONIN_DT));
			excel.selectRow(1,true);
			excel.selectCell(14,true);
			excel.setCellValue(rs.getString(BUNRUI2_NM));
			excel.selectRow(3,true);
			excel.selectCell(14,true);
			excel.setCellValue(rs.getString(PHASE));
						
			}
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
		
	
	/**
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public boolean getMeisai(Excel excel) throws SQLException {
		boolean result = false;
		String kingaku = GS.EMPTY_CHARCTER;
		String tuuka = GS.EMPTY_CHARCTER;;
		
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_RYUHO, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setResultSet(RESULTSET);
				
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
			//帳票の作成
	    		int i = 7; 
	    		while ( rs.next() ) {
	    			result = true;
	    			tuuka = rs.getString(TUUKA_CD);
	    			
	    			excel.selectRow(i,true);
			    	for(int j=0;j<=14;j++){
			    			
				    	switch (j) {
					        case 0:
						    	excel.selectCell(j,true);
					        	excel.setCellStyle(j);
								excel.setCellValue(rs.getString(JIMUSHO_CD));
								break;
					        case 1:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(BU_CD));
								break;
					        case 2:
								excel.selectCell(j,true);
						    	excel.setCellStyle(j);
								excel.setCellValue(rs.getString(BU_NM));
								break;
					        case 3:	
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(KA_CD));
								break;
					        case 4:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(KA_NM));
								break;
					        case 5:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(CELL_CD));
								break;
					        case 6:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(CELL_NM));
								break;
					        case 7:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(KANJO_CD));
								break;
					        case 8:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(KANJO_NM));
								break;
					        case 9:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(SHUSI_DT));
								break;
					        case 10:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(INVOICE_NO));
								break;
					        case 11:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(KEIYAKU_DENPYO_NO));
								break;
					        case 12:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getDouble(KINGAKU));
								excel.setCellStyle(j);
								break;
					        case 13:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(RYUHOSAIMU_KBN));
								break;
					        case 14:
								excel.selectCell(j,true);
								excel.setCellStyle(j);
								excel.setCellValue(rs.getString(BIKO));
								break;
							default:
								break;
				    	}//switch文
			    	}//for文
			    	i++;
	    		}//while文
	    		
    			//ラベル用、通貨コードを取得
    			excel.selectCell(6,12,true);
    			kingaku = excel.getStringCellValue();
    			excel.setCellValue(kingaku + tuuka);
	    		return result;
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
}