/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/11/06		SSC				新規作成
002		2009/12/09		SSC				課題No.193 ヘッダ部、会社名の表示方法を統一する
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
* LD2103 BS照会表 DBアクセスクラス
*/
public class BSSyokaiExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	
	// 課題No.193
	// 追加開始
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	// 追加完了
	
	private static final String SATEI_KI            		= "satei_ki";
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String INIT_BUNRUI2            	= "init_bunrui2";
	private static final String INIT_BU_CD            	= "init_bu_cd";
	private static final String BU_NM						= "bu_nm";
	private static final String KTK             			= "ktk";
	private static final String TAIRYU_KBN				= "tairyu_kbn";
	private static final String TORIHIKISAKI_KBN			= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";
	private static final String YM   						= "ym";
	private static final String RYUDOSISAN            	= "ryudosisan";
	private static final String KOTEISISAN				= "koteisisan";
	private static final String KOTEIKAEIGYOSAIKEN		= "koteikaeigyosaiken";
	private static final String KASIDAOREHIKIATEKIN		= "kasidaorehikiatekin";
	private static final String SONSITUHIKIATEKIN			= "sonsituhikiatekin";
	private static final String HOSYOSAIMU				= "hosyosaimu";
	private static final String RENKETU_KBN				= "renketsu_kbn";

	private static final String WARNING0004 = "warning.0004";	
	
	private static final int StartRow = 5;				//明細出力開始行
	private static final int startClm = 0;				//明細出力開始列(BS照会表)
	private static final int endClm = 17;				//明細出力最終列(BS照会表)
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD2103_SELECT_ICHIRAN 	= "SP_SS_LD2103_SELECT_ICHIRAN"; //一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public BSSyokaiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		form = (DownloadForm)appContext.getActionForm();
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
				excel.selectCell(0,17,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				
				// 課題No.193
				// 追加開始
				//分類１
				excel.selectCell(1,17,true);
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2103_SELECT_ICHIRAN, sqlExec);
		
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
		    		ym_hantei = rs.getString(SATEI_KI);
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
	    				case 3: excel.setCellValue(rs.getString(INIT_BUNRUI2));break;
	    				case 4: excel.setCellValue(rs.getString(INIT_BU_CD));break;
	    				case 5: excel.setCellValue(rs.getString(BU_NM));break;
	    				case 6: excel.setCellValue(rs.getString(KTK));break;
	    				case 7: excel.setCellValue(rs.getString(TAIRYU_KBN));break;
	    				case 8: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
	    				case 9: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
	    				case 10:excel.setCellValue(rs.getString(YM));break;
	    				case 11:excel.setCellValue(Function.getValueOfLong(rs.getString(RYUDOSISAN)));break;
	    				case 12:excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEISISAN)));break;
	    				case 13:excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEIKAEIGYOSAIKEN)));break;
	    				case 14:excel.setCellValue(Function.getValueOfLong(rs.getString(KASIDAOREHIKIATEKIN)));break;
	    				case 15:excel.setCellValue(Function.getValueOfLong(rs.getString(SONSITUHIKIATEKIN)));break;
	    				case 16:excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYOSAIMU)));break;
	    				case 17:excel.setCellValue(rs.getString(RENKETU_KBN));break;
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