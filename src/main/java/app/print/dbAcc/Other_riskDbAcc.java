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
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* LC3101_留保債務帳票  DBアクセスクラス
*/
public class Other_riskDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private AppPagerActionForm app_form = null;
	private DownloadForm form = null;					// アクションフォーム

	//Resultset用文字列	
	private static final String MISE_CD				= "mise_cd";
	private static final String KANJO_CD            	= "kanjo_cd";
	private static final String KANJO_NM            	= "kanjo_nm";
	private static final String DUNS_NO    			= "togo_tori_cd";
	private static final String YM    				= "ym";
	private static final String SAKUSEI_DT    		= "sakusei_dt";
	private static final String OTHER_RISK_FLG_S    	= "other_risk_flg_s";
	private static final String OTHER_RISK_FLG_T    	= "other_risk_flg_t";
	private static final String KINGAKU_S   		 	= "kingaku_s";
	private static final String KINGAKU_T    			= "kingaku_t";
	
	private static final String WARNING0004 = "warning.0004";	
	private static final String KA    				= "可";
	private static final String HI   				 	= "否";
	private static final String ZERO    				= "0";
	private static final String ICHI    				= "1";
	private static final String HAIHUN				= "-";
	
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";			//帳票のヘッダ部を取得するプロシージャ
	private static final String SP_SS_LE1101_SELECT_ICHIRAN 	= "SP_SS_LE1101_SELECT_ICHIRAN";		//帳票に出力する一覧情報を取得するプロシージャ


	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public Other_riskDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;
		//ビーン取得
		app_form = (AppPagerActionForm)appContext.getActionForm();
		form = (DownloadForm)appContext.getActionForm();

	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	}
		
	
	/**
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public boolean getMeisai(Excel excel) throws SQLException {
		String tairyuhantei_ka_hi	= GS.EMPTY_CHARCTER;
		
		boolean result = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LE1101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(app_form.getLangMode());
		exCstmt.setStringIn(form.getSearch_system_kbn());
		exCstmt.setStringIn(form.getSearch_sateikaisya_cd());
		exCstmt.setStringIn(Function.trim(form.getSearch_bunrui2()));
		exCstmt.setStringIn(form.getSearch_taisyo_ym());
		exCstmt.setStringIn(form.getSearch_sateiki());
		exCstmt.setStringIn(form.getHanki_sihanki_kbn());
		exCstmt.setStringIn(Function.addSingleQuotation(form.getDuns_no()));
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKanjo_cd()));
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKanjo_nm()));
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
	    				excel.selectRow(i,true);
				    	for(int j=0;j<=9;j++){
				    			
					    	switch (j) {
						        case 0:
							    	excel.selectCell(j,true);
						        	excel.setCellStyle(j);
									excel.setCellValue(rs.getString(MISE_CD));
									break;
						        case 1:
									excel.selectCell(j,true);
									excel.setCellStyle(j);
									excel.setCellValue(rs.getString(KANJO_CD));
									break;
						        case 2:
									excel.selectCell(j,true);
							    	excel.setCellStyle(j);
									excel.setCellValue(rs.getString(KANJO_NM));
									break;
						        case 3:
						        	//滞留判定可否
						        	tairyuhantei_ka_hi = Tairyu_ka_hi(Function.trim(rs.getString(OTHER_RISK_FLG_T)),Function.trim(rs.getString(OTHER_RISK_FLG_S)));
									excel.selectCell(j,true);
									excel.setCellStyle(j);
									excel.setCellValue(tairyuhantei_ka_hi);
									break;
						        case 4:				        	
									//金額の判断
						        	if(Function.trim(rs.getString(KINGAKU_S)).equals(GS.EMPTY_CHARCTER)){
						        		excel.selectCell(j,true);
						        		excel.setCellStyle(j);
										excel.setCellValue(rs.getDouble(KINGAKU_T));
										break;
						        	} else {
						        		excel.selectCell(j,true);
						        		excel.setCellStyle(j);
										excel.setCellValue(rs.getDouble(KINGAKU_S));
										break;
						        	}
						        case 5:
									excel.selectCell(j,true);
									excel.setCellStyle(j);
									excel.setCellValue(GS.EMPTY_CHARCTER);
									break;
									
						        case 6:
									excel.selectCell(j,true);
									excel.setCellStyle(j);
									excel.setCellValue(GS.EMPTY_CHARCTER);
									break;
						        case 7:
									excel.selectCell(j,true);
									excel.setCellStyle(j);
									excel.setCellValue(rs.getString(DUNS_NO));
									break;
						        case 8:
									excel.selectCell(j,true);
									excel.setCellStyle(j);									
									excel.setCellValue(rs.getString(YM));
									break;
								default:
									break;
					    	}//switch文
					    	
				    	}//for文
				    	i++;
	    		}//while文     
	    		if(!result){
	    			appContext.setMsgCode(WARNING0004);
	    		}
	    		return result;	    		
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	//作成日・承認日取得
	public void getYm(Excel excel) throws SQLException {
		
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD2, sqlExec);
		
		exCstmt.setStringIn(app_form.getLangMode());
	    exCstmt.setStringIn(GS.EMPTY_CHARCTER);
	    exCstmt.setStringIn(GS.EMPTY_CHARCTER);
	    exCstmt.setStringIn(app_form.toString());
		exCstmt.setResultSet(RESULTSET);
		
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){
			//作成日を取得
			excel.selectRow(0,true);
			excel.selectCell(8,true);
			excel.setCellValue(rs.getString(SAKUSEI_DT));
			}
			
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}
	
	//滞留判定可否
	public String Tairyu_ka_hi(String tairyu_flg, String satei_flg)  {

		String tairyu = GS.EMPTY_CHARCTER;
		
		if(tairyu_flg.equals(ZERO) && satei_flg.equals(ICHI)) {
			tairyu = KA;
		}
		else if (tairyu_flg.equals(GS.EMPTY_CHARCTER) && satei_flg.equals(ICHI)){
			tairyu = HAIHUN;
		}
		else if(tairyu_flg.equals(ICHI)){
			tairyu = HI;
		}		
		
		return tairyu;	

	}
	
}