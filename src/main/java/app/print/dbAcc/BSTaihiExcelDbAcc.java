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
* LD2104 BS対比表 DBアクセスクラス
*/
public class BSTaihiExcelDbAcc extends CommonDbAcc {
	
	private DownloadForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列
	private static final String SAKUSEI_DT            	= "sakusei_dt";
	
	// 課題No.193
	// 追加開始
	private static final String BUNRUI1_NM            	= "bunrui1_nm";
	// 追加完了
	
	private static final String KIKAN_TORI_CD            	= "kikan_tori_cd";
	//今期分データ取得
	private static final String SATEI_KI            		= "satei_ki";
	private static final String TOGO_TORI_CD				= "togo_tori_cd";
	private static final String BUSINESS_NM				= "business_nm";
	private static final String INIT_BUNRUI2            	= "init_bunrui2";
	private static final String INIT_BU_CD            	= "init_bu_cd";
	private static final String BU_NM						= "bu_nm";
	private static final String TORIHIKISAKI_KBN			= "torihikisaki_kbn";
	private static final String SAIKEN_KBN   				= "saiken_kbn";
	private static final String IPPANSAIKEN   			= "ippansaiken";
	private static final String KOTEIKA_EIGYOU_SAIKEN		= "koteika_eigyou_saiken";
	private static final String HOSYO_SAIMUKEI			= "hosyo_saimukei";
	private static final String SONSITUHIKIATEKIN			= "sonsituhikiatekin";
	private static final String RENKETSU_KBN				= "renketsu_kbn";
	private static final String RYUHOSAIMUKEI				= "ryuhosaimukei";
	private static final String RIKO_KENEN				= "riko_kenen";
	private static final String HIKIATEKIN				= "hikiatekin";
	private static final String SAIKENZANDAKA				= "saikenzandaka";
	private static final String HOSYOUSAIMUKEI1			= "hosyousaimukei1";
	private static final String HOSYOUSAIMUKEI2			= "hosyousaimukei2";
	private static final String HOSYOUSAIMUKEI3			= "hosyousaimukei3";
	private static final String ANKEN_NO					= "anken_no";

	//前期分データ取得
	private static final String ZEN_SATEI_KI            		= "zen_satei_ki";
	private static final String ZEN_TOGO_TORI_CD				= "zen_togo_tori_cd";
	private static final String ZEN_BUSINESS_NM				= "zen_business_nm";
	private static final String ZEN_INIT_BUNRUI2            	= "zen_init_bunrui2";
	private static final String ZEN_INIT_BU_CD            	= "zen_init_bu_cd";
	private static final String ZEN_BU_NM						= "zen_bu_nm";
	private static final String ZEN_TORIHIKISAKI_KBN			= "zen_torihikisaki_kbn";
	private static final String ZEN_SAIKEN_KBN   				= "zen_saiken_kbn";
	private static final String ZEN_IPPANSAIKEN   			= "zen_ippansaiken";
	private static final String ZEN_KOTEIKA_EIGYOU_SAIKEN		= "zen_koteika_eigyou_saiken";
	private static final String ZEN_HOSYO_SAIMUKEI			= "zen_hosyo_saimukei";
	private static final String ZEN_SONSITUHIKIATEKIN			= "zen_sonsituhikiatekin";
	private static final String ZEN_RENKETSU_KBN				= "zen_renketsu_kbn";
	private static final String ZEN_RYUHOSAIMUKEI				= "zen_ryuhosaimukei";
	private static final String ZEN_RIKO_KENEN				= "zen_riko_kenen";
	private static final String ZEN_HIKIATEKIN				= "zen_hikiatekin";
	private static final String ZEN_SAIKENZANDAKA				= "zen_saikenzandaka";
	private static final String ZEN_HOSYOUSAIMUKEI1			= "zen_hosyousaimukei1";
	private static final String ZEN_HOSYOUSAIMUKEI2			= "zen_hosyousaimukei2";
	private static final String ZEN_HOSYOUSAIMUKEI3			= "zen_hosyousaimukei3";
	private static final String ZEN_ANKEN_NO					= "zen_anken_no";
	
	//計算式
	private static final String FORMULA3 = "O{1}-AF{1}";
	private static final String FORMULA4 = "AI{1}-AJ{1}";

	private static final String WARNING0004 = "warning.0004";	
	
	private static final int StartRow = 5;				//明細出力開始行
	private static final int startClm = 0;				//明細出力開始列(BS対比表)
	private static final int endClm = 36;				//明細出力最終列(BS対比表)
	
	private static final String SP_SS_L_SELECT_CHOHYOHEAD2 	= "SP_SS_L_SELECT_CHOHYOHEAD2";	 //帳票ヘッダ部取得プロシージャ
	private static final String SP_SS_LD2104_SELECT_ICHIRAN 	= "SP_SS_LD2104_SELECT_ICHIRAN"; //一覧情報取得プロシージャ

	// INパラメータ
	private String tyohyo_lang;
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public BSTaihiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
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
				excel.selectCell(0,36,true);
				excel.setCellValue(rs.getString(SAKUSEI_DT));
				
				// 課題No.193
				// 追加開始
				//分類１
				excel.selectCell(1,36,true);
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
		String formula = GS.EIGYO_RECOG_ID;
		double ryuhosaimukei = 0;

		String ym_hantei = GS.EMPTY_CHARCTER;
		String zen_ym_hantei = GS.EMPTY_CHARCTER;
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LD2104_SELECT_ICHIRAN, sqlExec);
		
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
		//一覧情報を取得
		exCstmt.setResultSet(RESULTSET);
		try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	rs = exCstmt.getResultSet(RESULTSET);

    		//明細開始行
	    	int i = StartRow;
	    	//査定期取得時に使用
	    	while ( rs.next() ) {
	    		result = true;
	    		//年月表示判定(今期)
	    		if(!Function.trim(rs.getString(SATEI_KI)).equals(GS.EMPTY_CHARCTER)){
		    		ym_hantei = Function.trim(rs.getString(SATEI_KI));
	    		}
	    		if(!Function.trim(rs.getString(ZEN_SATEI_KI)).equals(GS.EMPTY_CHARCTER)){
		    		zen_ym_hantei = Function.trim(rs.getString(ZEN_SATEI_KI));
	    		}
	    		
	    		excel.selectRow(i, true);
	    		//明細情報を設定
				for (int j=startClm; j<=endClm; j++ ) {
					
		    		excel.selectCell(j, true);
    				excel.setCellStyle(j);
    				
    				//今期分データが存在しない場合
    				if(Function.trim(rs.getString(ANKEN_NO)).equals(GS.EMPTY_CHARCTER)){
    					switch (j){
        				//今期分データを出力
    	    				case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
    	    				case 1: excel.setCellValue(rs.getString(ZEN_TOGO_TORI_CD));break;
    	    				case 2: excel.setCellValue(rs.getString(ZEN_BUSINESS_NM));break;
    	    				case 3: excel.setCellValue(rs.getString(ZEN_INIT_BUNRUI2));break;
    	    				case 4: excel.setCellValue(rs.getString(ZEN_INIT_BU_CD));break;
    	    				case 5: excel.setCellValue(rs.getString(ZEN_BU_NM));break;
    	    				default:break;
    					}
    				//今期分データが存在する場合
    				}else{
	    				switch (j){
	    				//今期分データを出力
		    				case 0: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
		    				case 1: excel.setCellValue(rs.getString(TOGO_TORI_CD));break;
		    				case 2: excel.setCellValue(rs.getString(BUSINESS_NM));break;
		    				case 3: excel.setCellValue(rs.getString(INIT_BUNRUI2));break;
		    				case 4: excel.setCellValue(rs.getString(INIT_BU_CD));break;
		    				case 5: excel.setCellValue(rs.getString(BU_NM));break;
		    				case 6: excel.setCellValue(rs.getString(TORIHIKISAKI_KBN));break;
		    				case 7: excel.setCellValue(rs.getString(SAIKEN_KBN));break;
		    				case 8:excel.setCellValue(Function.getValueOfLong(rs.getString(IPPANSAIKEN)));break;
		    				case 9:excel.setCellValue(Function.getValueOfLong(rs.getString(KOTEIKA_EIGYOU_SAIKEN)));break;
		      				case 10:excel.setCellValue(Function.getValueOfLong(rs.getString(SAIKENZANDAKA)));break;
		    				case 11:
		    					ryuhosaimukei = Function.getValueOfLong(rs.getString(RYUHOSAIMUKEI));
			    					if(ryuhosaimukei > Function.getValueOfLong(rs.getString(SAIKENZANDAKA))){
			    						if(Function.getValueOfLong(rs.getString(SAIKENZANDAKA)) < 0){
			    							excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYOUSAIMUKEI3)));
			    						}else{
			    							excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYOUSAIMUKEI2)));
			    						}
			    					}else{
			    						excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYOUSAIMUKEI1)));
			    					}
		    					break;
		    				case 12:excel.setCellValue(Function.getValueOfLong(rs.getString(HOSYO_SAIMUKEI)));break;
		    				case 13:excel.setCellValue(Function.getValueOfLong(rs.getString(RIKO_KENEN)));break;
		    				
		    				case 14:excel.setCellValue(Function.getValueOfLong(rs.getString(HIKIATEKIN)));break;
	
		    				case 15:excel.setCellValue(Function.getValueOfLong(rs.getString(SONSITUHIKIATEKIN)));
		    					break;
		    				case 16:excel.setCellValue(rs.getString(RENKETSU_KBN));break;
		    				default:break;
	    				}
    				}
    				
    				if(!Function.trim(rs.getString(ZEN_ANKEN_NO)).equals(GS.EMPTY_CHARCTER)){
	    				switch (j){
	    				
		    			//前期分データを出力
	    					case 17: excel.setCellValue(rs.getString(KIKAN_TORI_CD));break;
		    				case 18: excel.setCellValue(rs.getString(ZEN_TOGO_TORI_CD));break;
		    				case 19: excel.setCellValue(rs.getString(ZEN_BUSINESS_NM));break;
		    				case 20: excel.setCellValue(rs.getString(ZEN_INIT_BUNRUI2));break;
		    				case 21: excel.setCellValue(rs.getString(ZEN_INIT_BU_CD));break;
		    				case 22: excel.setCellValue(rs.getString(ZEN_BU_NM));break;
		    				case 23: excel.setCellValue(rs.getString(ZEN_TORIHIKISAKI_KBN));break;
		    				case 24: excel.setCellValue(rs.getString(ZEN_SAIKEN_KBN));break;
		    				case 25:
		    					excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_IPPANSAIKEN)));break;
		    				case 26:excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_KOTEIKA_EIGYOU_SAIKEN)));break;
		    				case 27:
		    					excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_SAIKENZANDAKA)));
		    					break;
		    				case 28:
		    					ryuhosaimukei = Function.getValueOfLong(rs.getString(ZEN_RYUHOSAIMUKEI));
		    					
			    					if(ryuhosaimukei > Function.getValueOfLong(rs.getString(ZEN_SAIKENZANDAKA))){
			    						if(Function.getValueOfLong(rs.getString(ZEN_SAIKENZANDAKA)) < 0) {
			    							excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_HOSYOUSAIMUKEI3)));
			    						}else {
			    							excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_HOSYOUSAIMUKEI2)));
			    						}
			    					}else{			    		
			    						excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_HOSYOUSAIMUKEI1)));
			    					}
		    					break;
		    				case 29:excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_HOSYO_SAIMUKEI)));break;
		    				case 30:excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_RIKO_KENEN)));break;
		    				
		    				case 31:excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_HIKIATEKIN)));break;
		    				case 32:excel.setCellValue(Function.getValueOfLong(rs.getString(ZEN_SONSITUHIKIATEKIN)));
		    					break;
		    				case 33:excel.setCellValue(rs.getString(ZEN_RENKETSU_KBN));break;
		    				default:break;
			    		}
    				}
    				switch (j){
						case 34:
							formula = Function.replaceExpression(FORMULA3,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
						case 35:break;
						case 36:
							formula = Function.replaceExpression(FORMULA4,String.valueOf(i+1));
							excel.setCellFormula(formula);
							break;
    				}
				}
				i++;
	    	}
	    	//今期年月出力(タイトル部)
	    	this.setYm(excel,ym_hantei,1,0);
	    	//今期年月出力(一覧部)
	    	this.setYm(excel,ym_hantei,3,0);
	    	//前期年月出力(一覧部)
	    	this.setYm(excel,zen_ym_hantei,3,17);
	    	
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
	private void setYm(Excel excel,String ym, int i, int j)  {
		List<String> list_ym = new ArrayList<String>();
		String title_ym = GS.EMPTY_CHARCTER;
		String[] ymHairetu = null;
		excel.selectCell(i,j,true);
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