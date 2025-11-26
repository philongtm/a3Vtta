/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/27		SSC				課題No.28 引当対象外/帳簿外対応 
******************************************************************************/
package app.print.dbAcc;

import app.TorihikisakiBean;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Excel;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
* LD1101_引当金確認帳票  DBアクセスクラス
*/
public class KakuninExcelDbAcc extends CommonDbAcc {
	
	private AppContext appContext					= null;//APPコンテキスト
	private AppPagerActionForm appForm				= null;//アクションフォーム親クラス

	private static boolean FALSE			= false;
	private static final int KARIKIJUNBI_CELL	= 15;
	private static final int KONKI_CELL			= 21;
	private static final String KIJUNBI_KBN_1		= "1";
	private static final String PATTERN = ".*\\{[0-9]\\}"; 
	
	//帳票引当金情報部分の開始行
	private static final int STAT_KANJONM			= 22;
	//帳票勘定科目名称のカラム
	private static final int KANJOKAMOKUNM_COL			= 3;
	
	//ヘッダ情報取得
	private static final String PHASE					= "phase";
	private static final String SAKUSEI_SYONIN_DT		= "sakusei_syonin_dt";
	private static final String BUNRUI2_NM			= "bunrui2_nm";
	private static final String TORIHIKI_NM			= "torihiki_nm";
	//勘定科目名称取得
	private static final String KBN_KEY				= "kanjo_nm_list";
	private static final String KBN_VAL				= "kbn_val";
	private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";
	private static final int KANJONM_UKETORITEGATA			= 1;
	private static final int KANJONM_YUSHUTSUUKETORITEGATA	= 2;
	private static final int KANJONM_URIKAKEKIN				= 3;
	private static final int KANJONM_TORIHIKIMAEWATASHIKIN	= 4;
	private static final int KANJONM_TATEKAEKIN				= 5;
	private static final int KANJONM_MISHUNYUKIN				= 6;
	private static final int KANJONM_MISHUSHUEKI				= 7;
	private static final int KANJONM_TANKIKASHITSUKEKIN		= 8;
	private static final int KANJONM_SASHIIREHOSHOKIN		= 9;
	private static final int KANJONM_KARIBARAIKIN			= 10;
	private static final int KANJONM_CHOKIKASHITSUKEKIN		= 11;
	private static final int KANJONM_SONOTATOSHI				= 12;
	private static final int KANJONM_HOSHOSAIMU				= 13;
	private static final int EXCEL_HOSHOSAIMUIDX				= 45;
	//対象年月リスト取得
	private static final String TAISHO_YM						= "taisho_ym";	
	private static final String TAISHO_YM_HYOUJI				= "taisho_ym_hyoji";	
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";

	//検索用案件No.取得
	private static final String OUT_ANKEN_NO			= "anken_no";
	//検索用アイテム取得
	private static final String OUT_PHASE				= "phase";
	private static final String OUT_HYOJI_YM			= "hyoji_ym";
	private static final String OUT_YM				= "ym";
	private static final String OUT_SYORI_KAISU		= "syori_kaisu";
	//基本情報取得
    private static final String KTK 					= "ktk";		//信用格付情報
    private static final String OYAKTK 				= "oya_ktk";	//親会社信用格付情報
    private static final String OYAITTAI 				= "oya_ittai";	//親会社一体独立
    private static final String OYA_NM 				= "oya_nm";		//親会社名称 
	//査定データ取得
    private static final String TAIRYU_KBN_NM 			= "tairyu_kbn_nm";			//滞留区分名称
    private static final String KOMOKU1 					= "komoku1";				//項目１
    private static final String RYUHOSAIMU 				= "ryuhosaimu";				//留保債務
    private static final String OTH_RYUHOSAIMU 			= "oth_ryuhosaimu";			//第三者留保債務
    private static final String HOZEN 					= "hozen";					//保全
    private static final String SONOTAKAISYU 				= "sonotakaisyu";			//その他回収
    private static final String RIKO_KENEN 				= "riko_kenen";				//履行請求懸念
    private static final String TUIKA_HIKIATE 			= "tuika_hikiate";			//追加引当金額
    private static final String KOMOKU2 					= "komoku2";				//項目２
    private static final String TAIRYU_KBN 				= "tairyu_kbn";				//滞留区分
    private static final String KIJUN_SAIKEN_KBN 			= "kijun_saiken_kbn";		//仮基準日債権区分名称
    private static final String KIJUN_TORIHIKISAKI_KBN 	= "kijun_torihikisaki_kbn";	//仮基準日取引先区分名称
    private static final String SAIKEN_KBN_NM 			= "saiken_kbn_nm";			//債権区分名称
    private static final String TORIHIKISAKI_KBN_NM 		= "torihikisaki_kbn_nm";	//取引先区分名称
	// 課題No.28
	// 追加開始
	private static final String HIKIATEKIN_SHOSAI_NM			= "hikiatekin_shosai_nm";			// 引当金詳細
	// 追加完了
	//コメントデータ取得
    private static final String POINT_01 					= "01";						//前期と今期の登録箇所
    private static final String POINT_80 					= "80";						//仮基準日の登録箇所   
    private static final String COMMENT_VAL				= "comment_val";			//コメント内容
	//引当データ取得
    private static final String TUUKA_CD					= "tuuka_cd";				//通貨コード
    private static final String KANJO_HYOUJI_KBN			= "kanjo_hyouji_kbn";		//勘定科目表示区分
    private static final String KINGAKU					= "kingaku";				//金額
	private static final int KANJO_UKETORITEGATA			= 1;
	private static final int KANJO_YUSHUTSUUKETORITEGATA	= 2;
	private static final int KANJO_URIKAKEKIN			= 3;
	private static final int KANJO_TORIHIKIMAEWATASHIKIN	= 4;
	private static final int KANJO_TATEKAEKIN			= 5;
	private static final int KANJO_MISHUNYUKIN			= 6;
	private static final int KANJO_MISHUSHUEKI			= 7;
	private static final int KANJO_TANKIKASHITSUKEKIN	= 8;
	private static final int KANJO_SASHIIREHOSHOKIN		= 9;
	private static final int KANJO_KARIBARAIKIN			= 10;
	private static final int KANJO_CHOKIKASHITSUKEKIN	= 11;
	private static final int KANJO_SONOTATOSHI			= 12;
	private static final int KANJO_HOSHOSAIMU			= 14;
	private static final int KANJO_KIBIKIATEKIN			= 15;

    //プロシージャ
	private static final String SP_SS_L_SELECT_CHOHYOHEAD 	= "SP_SS_L_SELECT_CHOHYOHEAD";	//帳票ヘッダ取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1400			= "SP_SS_OL_SELECT_T1400";		//前期案件No.取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1401			= "SP_SS_OL_SELECT_T1401";		//仮基準日案件No.取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1402			= "SP_SS_OL_SELECT_T1402";		//検索用マップ取得プロシージャ
    private static final String SP_SS_OL_SELECT_KAKUDUKE		= "SP_SS_OL_SELECT_KAKUDUKE";	//前期・仮基準日の基本情報取得プロシージャ
    private static final String SP_SS_OL_SELECT_T1500			= "SP_SS_OL_SELECT_T1500";		//査定データ取得プロシージャ
    private static final String SP_SS_OL_SELECT_T1700			= "SP_SS_OL_SELECT_T1700";		//引当データ取得プロシージャ(仮基準日用)
    private static final String SP_SS_OL_SELECT_T1800			= "SP_SS_OL_SELECT_T1800";		//引当データ取得プロシージャ(今期・前期用)
    private static final String SP_SS_OL_SELECT_T1200			= "SP_SS_OL_SELECT_T1200";		//コメント類取得プロシージャ
    private static final String SP_SS_L_SELECT_E0200			= "SP_SS_L_SELECT_E0200";		//取引先名称取得プロシージャ;
	private static final String SP_SS_OZ6108_SELECT_MEISAI	= "SP_SS_OZ6108_SELECT_MEISAI"; //前期・仮基準日の明細取得用プロシージャ
	private static final String SP_SS_O_SELECT_M2200			= "SP_SS_O_SELECT_M2200";
    /**
	 * コンストラクタ
	 * 
	 * @param AppContext
	 */
	public KakuninExcelDbAcc(AppContext appcontext) {
		super(appcontext.getSqlExecuter(),appcontext.getLog());
		this.appContext = appcontext;
		appForm = (AppPagerActionForm)appContext.getActionForm();
	}
    
	/**
	 * 査定期内対象年月取得 <br>
     * @return Map<String,String> 対象年月
	 * @exception SQLException
	 */
	public Map<String,String> getTaishoYmMap(TorihikisakiBean toriBean) throws SQLException {

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	Map<String,String> ymMap = new HashMap<String,String>();;
		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		cstmt.setStringIn(toriBean.getSatei_ki());
		cstmt.setStringIn(toriBean.getSystem_kbn());
		cstmt.setStringIn(appForm.getLangMode());
		cstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	while ( rs.next() ) {
	    		if(KIJUNBI_KBN_1.equals(rs.getString(KIJUNBI_KBN))){
	            	ymMap.put(TAISHO_YM,rs.getString(TAISHO_YM));
	            	ymMap.put(TAISHO_YM_HYOUJI,rs.getString(TAISHO_YM_HYOUJI));
	            	ymMap.put(KIJUNBI_KBN,rs.getString(KIJUNBI_KBN));
	    		}
	    	}
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	    return ymMap;
	}	

	/**
     * 
     *  査定データを取得<br>
     * @param Excel
     * @param TorihikisakiBean
     * @param String
     * @param String
     * @param int
     * 
     * @throws SQLException
     */
	public void getSateiData(Excel excel,TorihikisakiBean toriBean,String kensakuAnkenNo,String kensakuPhase,int START_CELL) throws SQLException{
    	ExCallableStatement exCstmt = null;
    	ResultSet rs = null;
    	//ExCallableStatement生成
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500,sqlExec);
		exCstmt.setStringIn(kensakuAnkenNo);
		exCstmt.setStringIn(Function.trim(toriBean.getSystem_kbn()));
		exCstmt.setStringIn(kensakuPhase);
		exCstmt.setStringIn(appForm.getLangMode());
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	excel.selectCell(19,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(TAIRYU_KBN)));
            	excel.selectCell(20,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(TAIRYU_KBN_NM)));
            	if(START_CELL == KARIKIJUNBI_CELL){
                	excel.selectCell(21,START_CELL,FALSE);
                	excel.setCellValue(Function.trim(rs.getString(KIJUN_TORIHIKISAKI_KBN)));
                	excel.selectCell(22,START_CELL,FALSE);
                	excel.setCellValue(Function.trim(rs.getString(KIJUN_SAIKEN_KBN)));
                	excel.selectCell(36,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KOMOKU1))))){
                    	excel.setCellValue(rs.getDouble(KOMOKU1));
    				}
                	excel.selectCell(39,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(RYUHOSAIMU))))){
                    	excel.setCellValue(rs.getDouble(RYUHOSAIMU));
    				}
                	excel.selectCell(40,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(OTH_RYUHOSAIMU))))){
                    	excel.setCellValue(rs.getDouble(OTH_RYUHOSAIMU));
    				}
                	excel.selectCell(42,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(HOZEN))))){
                    	excel.setCellValue(rs.getDouble(HOZEN));
    				}
                	excel.selectCell(43,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(SONOTAKAISYU))))){
                    	excel.setCellValue(rs.getDouble(SONOTAKAISYU));
    				}
                	excel.selectCell(46,START_CELL,FALSE);
    				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(RIKO_KENEN))))){
                    	excel.setCellValue(rs.getDouble(RIKO_KENEN));
    				}
                	excel.selectCell(51,START_CELL,FALSE);
					if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(TUIKA_HIKIATE))))){
	                	excel.setCellValue(rs.getDouble(TUIKA_HIKIATE));
					}
                	excel.selectCell(52,START_CELL,FALSE);
					if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KOMOKU2))))){
	                	excel.setCellValue(rs.getDouble(KOMOKU2));
					}
					//今期の取引先区分・債権区分が未登録の場合、仮基準日の取引先区分・債権区分を出力する
                	excel.selectCell(21,KONKI_CELL,FALSE);
                	if(GS.EMPTY_CHARCTER.equals(Function.trim(excel.getStringCellValue()))){
                    	excel.setCellValue(Function.trim(rs.getString(KIJUN_TORIHIKISAKI_KBN)));
                	}
                	excel.selectCell(22,KONKI_CELL,FALSE);
                	if(GS.EMPTY_CHARCTER.equals(Function.trim(excel.getStringCellValue()))){
                    	excel.setCellValue(Function.trim(rs.getString(KIJUN_SAIKEN_KBN)));
                	}
            	}else{
                	excel.selectCell(21,START_CELL,FALSE);
                	excel.setCellValue(Function.trim(rs.getString(TORIHIKISAKI_KBN_NM)));
                	excel.selectCell(22,START_CELL,FALSE);
                	excel.setCellValue(Function.trim(rs.getString(SAIKEN_KBN_NM)));
            	}
        		//課題No.28
        		//追加開始
            	if(START_CELL == KONKI_CELL){
                	excel.selectCell(55,START_CELL,FALSE);
                	excel.setCellValue(Function.trim(rs.getString(HIKIATEKIN_SHOSAI_NM)));
            	}
        		//追加完了            		
            }
        } finally {
            if(rs != null) {
            	rs.close();
            }
        }
	}

	/**
     * 
     *  コメントを取得<br>
     * @param Excel
     * @param String
     * @param String
     * @param int
     * 
     * @throws SQLException
     */
	public void getComment(Excel excel,String kensakuAnkenNo,String kensakuPhase,int START_CELL) throws SQLException{
		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
    	String point = POINT_01;
    	if(START_CELL == KARIKIJUNBI_CELL){
    		point = POINT_80;
    	}
    	//ExCallableStatement生成
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
		exCstmt.setStringIn(kensakuAnkenNo);
		exCstmt.setStringIn(kensakuPhase);
		exCstmt.setStringIn(point);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
        		//課題No.28
        		//修正開始
            	if(START_CELL == KARIKIJUNBI_CELL){
            		//excel.selectCell(58,1,FALSE);
                	excel.selectCell(60,1,FALSE);
            	}else if(START_CELL == KONKI_CELL){
            		//excel.selectCell(61,1,FALSE);
                	excel.selectCell(63,1,FALSE);
            	}else{
                	//excel.selectCell(55,1,FALSE);
                	excel.selectCell(57,1,FALSE);
            	}
        		//修正完了
            	excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
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
     * @param TorihikisakiBean
     * @throws SQLException
     */
	public void getKanjoNm(Excel excel,TorihikisakiBean toriBean) throws SQLException{

		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
    	//ExCallableStatement生成
		exCstmt = new ExCallableStatement(SP_SS_L_SELECT_E0200, sqlExec);
		exCstmt.setStringIn(appForm.getLangMode());
		exCstmt.setStringIn(Function.trim(toriBean.getTaisyo_ym()));
		exCstmt.setStringIn(Function.trim(toriBean.getShikibetu_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getTogo_tori_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getSyori_kaisu()));
		exCstmt.setStringIn(Function.trim(toriBean.getSystem_kbn()));
		exCstmt.setStringIn(Function.trim(toriBean.getSateikaisya_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getMise_cd()));
		exCstmt.setResultSet(RESULTSET);
		
		try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            while(rs.next()) {
            	excel.selectCell(8,4,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(TORIHIKI_NM)));
			}
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
	}

	/**
     * 
     *  引当データを取得<br>
     * @param Excel
     * @param TorihikisakiBean
     * @param String
     * @param int
     * 
     * @throws SQLException
     */
	public void getHikiateData(Excel excel,TorihikisakiBean toriBean,String kensakuAnkenNo,int START_CELL) throws SQLException{
		
		boolean flg = false;
    	String HIKIATE_PRO = SP_SS_OL_SELECT_T1800;
		int tempHikiIdx = 0;
    	if(START_CELL == KARIKIJUNBI_CELL){
    		HIKIATE_PRO = SP_SS_OL_SELECT_T1700;
    	}
		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
    	//ExCallableStatement生成
		exCstmt = new ExCallableStatement(HIKIATE_PRO, sqlExec);
		exCstmt.setStringIn(kensakuAnkenNo);
		exCstmt.setStringIn(toriBean.getSystem_kbn());
		exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            
            while(rs.next()) {
            	flg = true;
        		tempHikiIdx = Integer.parseInt(rs.getString(KANJO_HYOUJI_KBN));
    			excel.selectCell(12,26,FALSE);
        		//EXCELより取得した通貨コードが置換されていない場合
        		if(Function.matches(excel.getStringCellValue(),PATTERN)) {
	        		excel.setCellValue(Function.replaceString(excel.getStringCellValue(),Function.trim(rs.getString(TUUKA_CD))));
        		} 
        
				switch (tempHikiIdx) {
					case KANJO_UKETORITEGATA:
						excel.selectCell(23,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_YUSHUTSUUKETORITEGATA:
						excel.selectCell(24,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_URIKAKEKIN:
						excel.selectCell(25,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TORIHIKIMAEWATASHIKIN:
						excel.selectCell(26,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TATEKAEKIN:
						excel.selectCell(27,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_MISHUNYUKIN:
						excel.selectCell(28,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_MISHUSHUEKI:
						excel.selectCell(29,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TANKIKASHITSUKEKIN:
						excel.selectCell(30,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_SASHIIREHOSHOKIN:
						excel.selectCell(31,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_KARIBARAIKIN:
						excel.selectCell(32,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_CHOKIKASHITSUKEKIN:
						excel.selectCell(33,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_SONOTATOSHI:
						excel.selectCell(34,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_HOSHOSAIMU:
						excel.selectCell(45,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_KIBIKIATEKIN:
						excel.selectCell(48,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					default:
						break;
				}	
			}
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
	}

    /**
     * 
     * 引数で指定された査定期の表示用査定期を返す <br>
     * 
     * @param String
     * @param String
     * @return String
     * @throws Exception
     */
    private String getSateikiHyouji(String sateiki,String langMode){
    	StringBuffer sb = new StringBuffer();
    	String[] nengetsu = new String[2];
    	nengetsu[0] = sateiki.substring(0,4);
    	nengetsu[1] = sateiki.substring(4);

    	if(GS.LANG_JA.equals(langMode)){
    		sb.append(nengetsu[0]);
    		sb.append(GS.SLASH);
    		sb.append(nengetsu[1]);
    	}else{
    		sb.append(nengetsu[1]);
    		sb.append(GS.SLASH);
    		sb.append(nengetsu[0]);
    	}
    	
    	return sb.toString();
    }

    /**
     * 
     *  明細データを取得<br>
     * @param Excel
     * @param TorihikisakiBean
     * @param String
     * @param int
     * 
     * @throws SQLException
     */
	public boolean getDataMeisai(Excel excel,TorihikisakiBean toriBean,String ym,int START_CELL) throws SQLException{
		
		boolean result = false;
		int tempHikiIdx = 0;
		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
    	//ExCallableStatement生成
		exCstmt = new ExCallableStatement(SP_SS_OZ6108_SELECT_MEISAI, sqlExec);
		exCstmt.setStringIn(toriBean.getSystem_kbn());
		exCstmt.setStringIn(toriBean.getSateikaisya_cd());
		exCstmt.setStringIn(toriBean.getKanjo_cd());
		exCstmt.setStringIn(toriBean.getMise_cd());
		exCstmt.setStringIn(ym);
		exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            while(rs.next()) {
            	result = true;
        		tempHikiIdx = Integer.parseInt(rs.getString(KANJO_HYOUJI_KBN));
    			excel.selectCell(12,26,FALSE);
    			//EXCELより取得した通貨コードが置換されていない場合
        		if(Function.matches(excel.getStringCellValue(),PATTERN)) {
	        		excel.setCellValue(Function.replaceString(excel.getStringCellValue(),Function.trim(rs.getString(TUUKA_CD))));
        		} 
        		
				switch (tempHikiIdx) {
					case KANJO_UKETORITEGATA:
						excel.selectCell(23,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_YUSHUTSUUKETORITEGATA:
						excel.selectCell(24,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_URIKAKEKIN:
						excel.selectCell(25,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TORIHIKIMAEWATASHIKIN:
						excel.selectCell(26,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TATEKAEKIN:
						excel.selectCell(27,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_MISHUNYUKIN:
						excel.selectCell(28,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_MISHUSHUEKI:
						excel.selectCell(29,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_TANKIKASHITSUKEKIN:
						excel.selectCell(30,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_SASHIIREHOSHOKIN:
						excel.selectCell(31,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_KARIBARAIKIN:
						excel.selectCell(32,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_CHOKIKASHITSUKEKIN:
						excel.selectCell(33,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_SONOTATOSHI:
						excel.selectCell(34,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_HOSHOSAIMU:
						excel.selectCell(45,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					case KANJO_KIBIKIATEKIN:
						excel.selectCell(48,START_CELL,FALSE);
						if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
							excel.setCellValue(rs.getDouble(KINGAKU));
						}
						break;
					default:
						break;
				}	
			}
            if(result){
        		//年月
        		excel.selectCell(14,START_CELL,false);
    			excel.setCellValue(this.getSateikiHyouji(ym,appForm.getLangMode()));
            }
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
        return result;
	}

	/**
     * 
     *  基本情報を取得<br>
     * 
     * @throws SQLException
     */
    public void getInf(Excel excel,TorihikisakiBean toriBean,HashMap<String,String> kensakuMap,int START_CELL) throws SQLException {

		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
    	// ExCallableStatement生成
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_KAKUDUKE,sqlExec);
		exCstmt.setStringIn(Function.trim(toriBean.getSystem_kbn()));
		exCstmt.setStringIn(Function.trim(toriBean.getMise_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getKanjo_cd()));
		exCstmt.setStringIn(kensakuMap.get(OUT_SYORI_KAISU));
		exCstmt.setStringIn(kensakuMap.get(OUT_YM));
		exCstmt.setStringIn(appForm.getLangMode());
		exCstmt.setStringIn(Function.trim(toriBean.getSateikaisya_cd()));
		exCstmt.setResultSet(RESULTSET);
		
		try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	excel.selectCell(15,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(KTK)));
            	excel.selectCell(16,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(OYAKTK)));
            	excel.selectCell(17,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(OYA_NM)));
            	excel.selectCell(18,START_CELL,FALSE);
            	excel.setCellValue(Function.trim(rs.getString(OYAITTAI)));
            }
            
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }
    
	/**
	 * 仮基準日案件No.取得処理 <br>
	 * @param TorihikisakiBean
	 * 
	 * @exception SQLException
	 */
	public String getKariKijunbiAnkenNo(TorihikisakiBean toriBean) throws SQLException{

		String kensakuAnkenNo = GS.EMPTY_CHARCTER;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = null;
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1401,sqlExec);

		exCstmt.setStringIn(toriBean.getSystem_kbn());
		exCstmt.setStringIn(toriBean.getSateikaisya_cd());
		exCstmt.setStringIn(toriBean.getMise_cd());
		exCstmt.setStringIn(toriBean.getKanjo_cd());
		exCstmt.setStringIn(toriBean.getSatei_ki());
		//outパラメータ
		exCstmt.setStringOut(OUT_ANKEN_NO);

		//SQL実行
	    exCstmt.execute();
	    isError(exCstmt);
	    kensakuAnkenNo = Function.trim(exCstmt.getString(OUT_ANKEN_NO));
	    
	    return kensakuAnkenNo;
	}	

	/**
	 * 前期案件No.取得処理 <br>
	 * @param TorihikisakiBean
	 * 
	 * @exception SQLException
	 */
	public String getZenkiAnkenNo(TorihikisakiBean toriBean) throws SQLException{

		String kensakuAnkenNo = GS.EMPTY_CHARCTER;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = null;
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1400,sqlExec);
		exCstmt.setStringIn(Function.trim(toriBean.getSystem_kbn()));
		exCstmt.setStringIn(Function.trim(toriBean.getSateikaisya_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getMise_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getKanjo_cd()));
		exCstmt.setStringIn(Function.trim(toriBean.getSatei_ki()));
		exCstmt.setStringIn(Function.trim(toriBean.getSyori_kaisu()));
		exCstmt.setStringIn(Function.trim(toriBean.getPhase()));
		//outパラメータ
		exCstmt.setStringOut(OUT_ANKEN_NO);

		//SQL実行
	    exCstmt.execute();
	    isError(exCstmt);
	    kensakuAnkenNo = Function.trim(exCstmt.getString(OUT_ANKEN_NO));
	    
	    return kensakuAnkenNo;
	}	

	/**
	 * 検索アイテム取得処理 <br>
	 * @param TorihikisakiBean
	 * 
	 * @exception SQLException
	 */
	public HashMap<String,String> getKensakuMap(Excel excel,String kensakuAnkenNo,TorihikisakiBean toriBean,int START_CELL) throws SQLException{

		HashMap<String,String> kensakuMap = new HashMap<String,String>();
		kensakuMap.put(OUT_ANKEN_NO,kensakuAnkenNo);
		
		if(START_CELL == KONKI_CELL){
		    kensakuMap.put(OUT_PHASE,Function.trim(toriBean.getPhase()));
		    kensakuMap.put(OUT_HYOJI_YM,Function.trim(toriBean.getTaisyo_ym_hyoji()));
		    kensakuMap.put(OUT_YM,Function.trim(toriBean.getTaisyo_ym()));
		    kensakuMap.put(OUT_SYORI_KAISU,Function.trim(toriBean.getSyori_kaisu()));
		    return kensakuMap;
		}
		
		//ExCallableStatement生成
		ExCallableStatement exCstmt = null;
		exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1402,sqlExec);

		exCstmt.setStringIn(kensakuAnkenNo);
		exCstmt.setStringIn(appForm.getLangMode());
		//outパラメータ
		exCstmt.setStringOut(OUT_PHASE);
		exCstmt.setStringOut(OUT_HYOJI_YM);
		exCstmt.setStringOut(OUT_YM);
		exCstmt.setStringOut(OUT_SYORI_KAISU);

		//SQL実行
	    exCstmt.execute();
	    isError(exCstmt);
	    kensakuMap.put(OUT_PHASE,Function.trim(exCstmt.getString(OUT_PHASE)));
	    kensakuMap.put(OUT_HYOJI_YM,Function.trim(exCstmt.getString(OUT_HYOJI_YM)));
	    kensakuMap.put(OUT_YM,Function.trim(exCstmt.getString(OUT_YM)));
	    kensakuMap.put(OUT_SYORI_KAISU,Function.trim(exCstmt.getString(OUT_SYORI_KAISU)));
		//年月
		excel.selectCell(14,START_CELL,false);
		excel.setCellValue(Function.trim(exCstmt.getString(OUT_HYOJI_YM)));

		return kensakuMap;
	}	

	/**
	 * ヘッダ部情報取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHeader(Excel excel,TorihikisakiBean toriBean) throws SQLException {

		ExCallableStatement exCstmt = null;
		ResultSet rs = null;
		exCstmt = new ExCallableStatement(SP_SS_L_SELECT_CHOHYOHEAD,sqlExec);
		
		exCstmt.setStringIn(appForm.getLangMode());
	    exCstmt.setStringIn(toriBean.getPhase());
	    exCstmt.setStringIn(toriBean.getStatus());
	    exCstmt.setStringIn(toriBean.getAnken_no());
	    exCstmt.setStringIn(toriBean.getKaisya_cd());
	    exCstmt.setStringIn(toriBean.getSateikaisya_cd());
	    exCstmt.setStringIn(toriBean.getSystem_kbn());
	    exCstmt.setStringIn(toriBean.getMise_cd());
	    exCstmt.setStringIn(appForm.toString());
		exCstmt.setResultSet(RESULTSET);
		
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			while(rs.next()){
				//作成日・承認日
				excel.selectCell(1,27,FALSE);
				excel.setCellValue(Function.trim(rs.getString(SAKUSEI_SYONIN_DT)));
				//分類２名称
				excel.selectCell(2,27,FALSE);
				excel.setCellValue(Function.trim(rs.getString(BUNRUI2_NM)));
				//フェーズ
				excel.selectCell(4,27,FALSE);
				excel.setCellValue(Function.trim(rs.getString(PHASE)));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * フラグ区分セレクトボック取得処理 <br>
	 * @exception SQLException
	 */
	public void getKanjoKamokuNm(Excel excel,TorihikisakiBean toriBean) throws SQLException {

		ResultSet rs = null;
		int tempKanjoIdx = 0;
		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY,toriBean.getSystem_kbn(),appForm.getLangMode());
			while(rs.next()){
				tempKanjoIdx = Integer.parseInt(rs.getString(KBN_VAL));
				switch (tempKanjoIdx) {
					case KANJONM_UKETORITEGATA:
						excel.selectCell(STAT_KANJONM + KANJONM_UKETORITEGATA,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_YUSHUTSUUKETORITEGATA:
						excel.selectCell(STAT_KANJONM + KANJONM_YUSHUTSUUKETORITEGATA,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_URIKAKEKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_URIKAKEKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TORIHIKIMAEWATASHIKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_TORIHIKIMAEWATASHIKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TATEKAEKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_TATEKAEKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_MISHUNYUKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_MISHUNYUKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_MISHUSHUEKI:
						excel.selectCell(STAT_KANJONM + KANJONM_MISHUSHUEKI,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TANKIKASHITSUKEKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_TANKIKASHITSUKEKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_SASHIIREHOSHOKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_SASHIIREHOSHOKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_KARIBARAIKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_KARIBARAIKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_CHOKIKASHITSUKEKIN:
						excel.selectCell(STAT_KANJONM + KANJONM_CHOKIKASHITSUKEKIN,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_SONOTATOSHI:
						excel.selectCell(STAT_KANJONM + KANJONM_SONOTATOSHI,KANJOKAMOKUNM_COL,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_HOSHOSAIMU:
						//↓以下注意
						excel.selectCell(EXCEL_HOSHOSAIMUIDX,KANJOKAMOKUNM_COL - 2,FALSE);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					default:
						break;
				}
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}	
}