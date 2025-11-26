/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/17		SSC				課題No.117 2バイト文字対応
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
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
* LC2101 債権査定帳票  DBアクセスクラス
*/
public class SateiExcelDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;			// 機能共通セッション
	private TorihikisakiBean tori_bean = null;		// 取引先情報
	private AppContext appContext = null;			// ＡＰＰコンテキスト
	private AppPagerActionForm appform = null;		// アクションフォーム

	//ヘッダ部
	private static final String SAKUSEI_SYONIN_DT		= "sakusei_syonin_dt";
	private static final String TORIHIKI_NM			= "torihiki_nm";
	private static final String BUNRUI2_NM			= "bunrui2_nm";
	private static final String PHASE					= "phase";
	//業種・所在地
	private static final String SYOZAI_ADR			= "syozai_adr";
	private static final String SIC_SIM_NM			= "sic_sim_nm";
	//抽出事由
	private static final String JIYUU_CD				= "jiyuu_cd";
	private static final String TYSYUTU_FLG			= "tyusyutu_flg";
	private static final String JIYUU01 = "01";		//破産更生
	private static final String JIYUU02 = "02";		//貸倒懸念
	private static final String JIYUU03 = "03";		//リ企指定
	private static final String JIYUU04 = "04";		//M12
	private static final String JIYUU05 = "05";		//要注意
	private static final String JIYUU06 = "06";		//低格付
	private static final String JIYUU07 = "07";		//無格付
	private static final String JIYUU08 = "08";		//債務超過
	private static final String JIYUU09 = "09";		//赤字
	private static final String CHK_JA = "○";
	
	// 課題No.117
	// 追加開始
	//private static final String CHK_EN = "Ｙ";
	private static final String CHK_EN = "Y";
	//追加完了
	
	private static final String SEIJO          = "1";		//正常先
	private static final String YOTYUI         = "2";		//要注意先
	private static final String KASIDAOREKENEN = "3";		//貸倒懸念先
	private static final String HASANKOSEI     = "4";		//破産更生先
	//親会社名称
    private static final String OYAITTAI  = "oya_ittai";
    private static final String OYA_NM    = "oya_nm";
	//財務概要
	private static final String KANSAN_ISO_CURRENCY_NM = "kansan_iso_currency_nm";
	private static final String KESSAN_KI              = "kessan_ki";
	private static final String URIAGEDAKA             = "uriagedaka";
	private static final String URIAGESORIEKI          = "uriagesorieki";
	private static final String HANBAIHIKANRIHI        = "hanbaihikanrihi";
	private static final String EIGYORIEKI             = "eigyorieki";
	private static final String HANYOU1                = "hanyou1";
	private static final String HANYOU2                = "hanyou2";
	private static final String HANYOU3                = "hanyou3";
	private static final String TOKIJUNRIEKI           = "tokijunrieki";
	private static final String HAITOKIN               = "haitokin";
	private static final String GENKASYOKYAKUHI        = "genkasyokyakuhi";
	private static final String EIGYO_CF               = "eigyo_cf";
	private static final String RYUDOSISAN             = "ryudosisan";
	private static final String KOTEISISAN             = "koteisisan";
	private static final String SISANGOKEI             = "sisangokei";
	private static final String RYUDOHUSAI             = "ryudohusai";
	private static final String KOTEIHUSAI             = "koteihusai";
	private static final String HUSAIGOKEI             = "husaigokei";
	private static final String SIHONKIN               = "sihonkin";
	private static final String NAIBURYUHO             = "naiburyuho";
	private static final String JIKOSIHONGOKEI         = "jikosihongokei";
	//査定登録データ
	private static final String JIGYONAIYO        	= "jigyonaiyo";
	private static final String KABUNUSI_NM        	= "kabunusi_nm";
	private static final String KABUSU            	= "kabusu";
	private static final String HIRITU        	    = "hiritu";
	private static final String TAIRYU_KBN        	= "tairyu_kbn";
	private static final String TAIRYU_KBN_NM        	= "tairyu_kbn_nm";
	private static final String SEIJO_CHK          	= "seijo_chk";
	private static final String YOCHUI_CHK        	= "yochui_chk";
	private static final String TYOKA_CHK         	= "tyoka_chk";
	private static final String KANWA_CHK         	= "kanwa_chk";
	private static final String ENTAI_CHK         	= "entai_chk";
	private static final String HASANHO_CHK        	= "hasanho_chk";
	private static final String KAISHAHO_CHK        	= "kaishaho_chk";
	private static final String KOSEHO_CHK        	= "koseho_chk";
	private static final String SONOTA_CHK        	= "sonota_chk";
	private static final String TORIHIKISAKI_KBN		= "torihikisaki_kbn";
	private static final String SAIKEN_KBN_NM2		= "saiken_kbn_nm2";
	private static final String KOMOKU1   			= "komoku1";
	private static final String RYUHOSAIMU_2     		= "ryuhosaimu_2";
	private static final String OTH_RYUHOSAIMU       	= "oth_ryuhosaimu";
	private static final String HOZEN			        = "hozen";
	private static final String SONOTAKAISYU			= "sonotakaisyu";
	private static final String RIKO_KENEN			= "riko_kenen";
	private static final String TUIKA_HIKIATE_2		= "tuika_hikiate_2";
	private static final String KOMOKU2   			= "komoku2";
	private static final String HUDOSAN_K				= "hudosan_k";
	private static final String HUDOSAN_H				= "hudosan_h";
	private static final String DOSAN_K				= "dosan_k";
	private static final String DOSAN_H            	= "dosan_h";
	private static final String HOKEN_K            	= "hoken_k";
	private static final String HOKEN_H            	= "hoken_h";
	private static final String SONOTA_K            	= "sonota_k";
	private static final String SONOTA_H   			= "sonota_h";
	private static final String HYOUJI_TANI  			= "hyouji_tani";
	//勘定科目名称
	private static final String KBN_KEY			= "kanjo_nm_list";
	private static final String KBN_VAL			= "kbn_val";
	private static final String KBN_HYOUJI_VAL	= "kbn_hyouji_val";
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
	//引当データ
    private static final String TUUKA_CD					= "tuuka_cd";
    private static final String KANJO_HYOUJI_KBN			= "kanjo_hyouji_kbn";
    private static final String KINGAKU					= "kingaku";
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
	//第三者留保債務内訳
    private static final String KIKAN_TORI_CD		= "kikan_tori_cd";
    private static final String TORISAKI_NM		= "torisaki_nm";
    private static final String KANJO_NM			= "kanjo_nm";
	//コメント
    private static final String COMMENT_VAL	= "comment_val";
    private static final String TOROKU_POINT	= "toroku_point";
	private static final String SANSYO_POINT	= "'10','20','30','40','50','60','70','80','90'";

	private static final String SP_SS_L_SELECT_CHOHYOHEAD     = "SP_SS_L_SELECT_CHOHYOHEAD";		//ヘッダ部取得プロシージャ
	private static final String SP_SS_OL_SELECT_E0400         = "SP_SS_OL_SELECT_E0400";			//業種・所在地取得プロシージャ
	private static final String SP_SS_L_SELECT_E0200          = "SP_SS_L_SELECT_E0200";			//取引先名称取得プロシージャ;
	private static final String SP_SS_LC2101_SELECT_T0200     = "SP_SS_LC2101_SELECT_T0200";		//抽出事由チェックプロシージャ
	private static final String SP_SS_OL_SELECT_KAKUDUKE      = "SP_SS_OL_SELECT_KAKUDUKE";		//親会社名称取得プロシージャ
	private static final String SP_SS_OL_SELECT_ZAIMU         = "SP_SS_OL_SELECT_ZAIMU";			//財務概要取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1500         = "SP_SS_OL_SELECT_T1500";			//査定登録データ取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1700         = "SP_SS_OL_SELECT_T1700";			//引当金判定情報取得プロシージャ
	private static final String SP_SS_OL_SELECT_T2000         = "SP_SS_OL_SELECT_T2000";			//第三者留保債務内訳取得プロシージャ
	private static final String SP_SS_OL_SELECT_T1200         = "SP_SS_OL_SELECT_T1200";			//コメント取得プロシージャ
	
	//帳票言語
	private String tyohyo_lang;
	//帳票用フェーズ
	private String SAN = "30";

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SateiExcelDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
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
	 * ヘッダ部情報取得 <br>
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
				excel.selectCell(0,59,false);
				excel.setCellValue(rs.getString(SAKUSEI_SYONIN_DT));
				excel.selectCell(1,59,false);
				excel.setCellValue(rs.getString(BUNRUI2_NM));
				excel.selectCell(3,59,false);
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
     *  取引先名称取得<br>
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
				excel.selectCell(7,4,false);
				excel.setCellValue(rs.getString(TORIHIKI_NM));
			}
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
	}

	/**
	 * 業種・所在地取得 <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getGyosyu_syozaichi(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_E0400, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(tori_bean.getSyozaikoku_cd());
	    exCstmt.setStringIn(tori_bean.getShikibetu_cd());
	    exCstmt.setStringIn(tori_bean.getTaisyo_ym());
	    exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
	    exCstmt.setStringIn(tori_bean.getSyori_kaisu());
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
	    exCstmt.setStringIn(tori_bean.getMise_cd());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()){
				excel.selectCell(10,4,false);
				if(rs.getString(SYOZAI_ADR) != null){
					excel.setCellValue(rs.getString(SYOZAI_ADR));
				}else{
					excel.setCellValue(tori_bean.getSyozaichi());
				}
				excel.selectCell(20,4,false);
				excel.setCellValue(rs.getString(SIC_SIM_NM));
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
		
	/**
	 * 抽出事由取得 <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getTyusyutujiyu(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_LC2101_SELECT_T0200, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
	    exCstmt.setStringIn(tori_bean.getMise_cd());
	    exCstmt.setStringIn(tori_bean.getTaisyo_ym());
	    exCstmt.setStringIn(tori_bean.getSyori_kaisu());
	    exCstmt.setStringIn(tori_bean.getKanjo_cd());
	    exCstmt.setStringIn(tori_bean.getHanki_sihanki_kbn());
		exCstmt.setResultSet(RESULTSET);
			
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){
				if(JIYUU01.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,13,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU02.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,11,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU03.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(17,1,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU04.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,9,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU05.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,3,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU06.equals(rs.getString(JIYUU_CD)) || JIYUU07.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,1,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU08.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,7,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
				}else if(JIYUU09.equals(rs.getString(JIYUU_CD))){
					excel.selectCell(15,5,false);
					excel.setCellValue(rs.getString(TYSYUTU_FLG));
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
     *  親会社名称取得<br>
     * 
     * @throws SQLException
     */
    public void getOya_nm(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_KAKUDUKE, sqlExec);
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
		exCstmt.setStringIn(tori_bean.getMise_cd());
		exCstmt.setStringIn(tori_bean.getKanjo_cd());
		exCstmt.setStringIn(tori_bean.getSyori_kaisu());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setStringIn(appform.getLangMode());
		exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
		exCstmt.setResultSet(RESULTSET);
		
		try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	excel.selectCell(7,23,false);
            	excel.setCellValue(Function.trim(rs.getString(OYAITTAI)));
            	excel.selectCell(7,20,false);
            	excel.setCellValue(Function.trim(rs.getString(OYA_NM)));
            }
            
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }
    
	/**
	 * 財務概要取得 <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getZaimu(Excel excel) throws SQLException {
		int i = 1;
		int clm = 0;
		List<String> tuuka_list = new ArrayList<String>();

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_ZAIMU, sqlExec);
		exCstmt.setStringIn(tyohyo_lang);
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getTaisyo_ym());
	    exCstmt.setStringIn(tori_bean.getShikibetu_cd());
	    exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
	    exCstmt.setStringIn(tori_bean.getSyori_kaisu());
	    exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
	    exCstmt.setStringIn(tori_bean.getMise_cd());    
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){		
				switch (i){
					case 1:clm = 6;break;
					case 2:clm = 9;break;
					case 3:clm = 12;break;
					default:break;
				}
				// 通貨(表示単位)の表示
				excel.selectCell(29,clm,false);
				excel.setCellValue((Function.trim(rs.getString(KANSAN_ISO_CURRENCY_NM) +  GS.KAKKO_HIDARI + Function.trim(rs.getString(HYOUJI_TANI)) + GS.KAKKO_MIGI)));
				// ここまで
				excel.selectCell(30,clm,false);
				excel.setCellValue(Function.trim(rs.getString(KESSAN_KI)));
				excel.selectCell(31,clm,false);
				excel.setCellValue(rs.getInt(URIAGEDAKA));
				excel.selectCell(32,clm,false);
				excel.setCellValue(rs.getInt(URIAGESORIEKI));
				excel.selectCell(33,clm,false);
				excel.setCellValue(rs.getInt(HANBAIHIKANRIHI));
				excel.selectCell(34,clm,false);
				excel.setCellValue(rs.getInt(EIGYORIEKI));
				excel.selectCell(35,clm,false);
				excel.setCellValue(rs.getInt(HANYOU1));
				excel.selectCell(36,clm,false);
				excel.setCellValue(rs.getInt(HANYOU2));
				excel.selectCell(37,clm,false);
				excel.setCellValue(rs.getInt(HANYOU3));
				excel.selectCell(38,clm,false);
				excel.setCellValue(rs.getInt(TOKIJUNRIEKI));
				excel.selectCell(39,clm,false);
				excel.setCellValue(rs.getInt(HAITOKIN));
				excel.selectCell(40,clm,false);
				excel.setCellValue(rs.getInt(GENKASYOKYAKUHI));
				excel.selectCell(41,clm,false);
				excel.setCellValue(rs.getInt(EIGYO_CF));
				excel.selectCell(42,clm,false);
				excel.setCellValue(rs.getInt(RYUDOSISAN));
				excel.selectCell(43,clm,false);
				excel.setCellValue(rs.getInt(KOTEISISAN));
				excel.selectCell(44,clm,false);
				excel.setCellValue(rs.getInt(SISANGOKEI));
				excel.selectCell(45,clm,false);
				excel.setCellValue(rs.getInt(RYUDOHUSAI));
				excel.selectCell(46,clm,false);
				excel.setCellValue(rs.getInt(KOTEIHUSAI));
				excel.selectCell(47,clm,false);
				excel.setCellValue(rs.getInt(HUSAIGOKEI));
				excel.selectCell(48,clm,false);
				excel.setCellValue(rs.getInt(SIHONKIN));
				excel.selectCell(49,clm,false);
				excel.setCellValue(rs.getInt(NAIBURYUHO));
				excel.selectCell(50,clm,false);
				excel.setCellValue(rs.getInt(JIKOSIHONGOKEI));
				
				i++;	
			}
			if(i==1){
				excel.selectCell(29,14,false);
    			excel.setCellValue(GS.EMPTY_CHARCTER);
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
		
	/**
	 * 査定登録データ取得 <br>
	 * 
     * @param Excel
     * @return String
	 * @exception SQLException
	 */
	public String getSateiData(Excel excel) throws SQLException {
		int j = 1;
		String torihikisaki_kbn = null;
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
	    exCstmt.setStringIn(tori_bean.getAnken_no());
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getPhase());
		exCstmt.setStringIn(tyohyo_lang);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			while(rs.next()){
				//事業概要
				excel.selectCell(21,4,false);
				excel.setCellValue(rs.getString(JIGYONAIYO));
				for(int i=23;i<=27;i++){
					excel.selectCell(i,5,false);
					excel.setCellValue(rs.getString(KABUNUSI_NM + String.valueOf(j)));
					excel.selectCell(i,10,false);
					excel.setCellValue(rs.getString(KABUSU + String.valueOf(j)));
					excel.selectCell(i,13,false);
					excel.setCellValue(rs.getString(HIRITU + String.valueOf(j)));
					j++;
				}
				//取引先区分判定
				excel.selectCell(8,18,false);
				excel.setCellValue(rs.getString(TAIRYU_KBN));
				excel.selectCell(8,20,false);
				excel.setCellValue(rs.getString(TAIRYU_KBN_NM));
				if(KASIDAOREKENEN.equals(rs.getString(TORIHIKISAKI_KBN))){
					excel.selectCell(22,16,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}else if(HASANKOSEI.equals(rs.getString(TORIHIKISAKI_KBN))){
					excel.selectCell(41,16,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(SEIJO_CHK))){
					excel.selectCell(10,16,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(YOCHUI_CHK))){
					excel.selectCell(13,16,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(TYOKA_CHK))){
					excel.selectCell(25,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(KANWA_CHK))){
					excel.selectCell(29,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(ENTAI_CHK))){
					excel.selectCell(33,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(HASANHO_CHK))){
					excel.selectCell(44,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(KAISHAHO_CHK))){
					excel.selectCell(46,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(KOSEHO_CHK))){
					excel.selectCell(48,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				if(GS.ON.equals(rs.getString(SONOTA_CHK))){
					excel.selectCell(50,17,false);
					excel.setCellValue(getChkString(tyohyo_lang));
				}
				//債権区分・引当金判定
				excel.selectCell(6,36,false);
				excel.setCellValue(rs.getString(SAIKEN_KBN_NM2));
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KOMOKU1))))){
	            	excel.selectCell(33,40,false);
                	excel.setCellValue(rs.getDouble(KOMOKU1));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(RYUHOSAIMU_2))))){
	            	excel.selectCell(36,40,false);
                	excel.setCellValue(rs.getDouble(RYUHOSAIMU_2));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(OTH_RYUHOSAIMU))))){
	            	excel.selectCell(37,40,false);
                	excel.setCellValue(rs.getDouble(OTH_RYUHOSAIMU));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(HOZEN))))){
	            	excel.selectCell(39,40,false);
                	excel.setCellValue(rs.getDouble(HOZEN));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(SONOTAKAISYU))))){
	            	excel.selectCell(40,40,false);
                	excel.setCellValue(rs.getDouble(SONOTAKAISYU));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(RIKO_KENEN))))){
	            	excel.selectCell(43,40,false);
                	excel.setCellValue(rs.getDouble(RIKO_KENEN));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(TUIKA_HIKIATE_2))))){
					excel.selectCell(48,40,false);
					excel.setCellValue(rs.getDouble(TUIKA_HIKIATE_2));
				}
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KOMOKU2))))){
					excel.selectCell(49,40,false);
					excel.setCellValue(rs.getDouble(KOMOKU2));
				}
				excel.selectCell(26,48,false);
				excel.setCellValue(rs.getDouble(HUDOSAN_K));
				excel.selectCell(26,51,false);
				excel.setCellValue(rs.getDouble(DOSAN_K));
				excel.selectCell(26,54,false);
				excel.setCellValue(rs.getDouble(HOKEN_K));
				excel.selectCell(26,57,false);
				excel.setCellValue(rs.getDouble(SONOTA_K));
				excel.selectCell(27,48,false);
				excel.setCellValue(rs.getDouble(HUDOSAN_H));
				excel.selectCell(27,51,false);
				excel.setCellValue(rs.getDouble(DOSAN_H));
				excel.selectCell(27,54,false);
				excel.setCellValue(rs.getDouble(HOKEN_H));
				excel.selectCell(27,57,false);
				excel.setCellValue(rs.getDouble(SONOTA_H));
				//取引先区分の値を保持
				torihikisaki_kbn = rs.getString(TORIHIKISAKI_KBN);
			}
			return torihikisaki_kbn;
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
		
	/**
	 * チェック文字編集 <br>
	 * 
     * @param String
     * @return String
	 * @exception SQLException
	 */
	public String getChkString(String lang_mode) throws SQLException {
		String chk = null;
		if(GS.LANG_JA.equals(lang_mode)){
			chk = CHK_JA;
		}else{
			chk = CHK_EN;
		}
		return chk;
	}
	
	/**
	 * 勘定科目名称取得 <br>
     * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getKanjoKamokuNm(Excel excel) throws SQLException {
		ResultSet rs = null;
		int tempKanjoIdx = 0;
		try{
            //SQL実行
			rs = super.getKbnval(KBN_KEY,tori_bean.getSystem_kbn(),appform.getLangMode());
			while(rs.next()){
				tempKanjoIdx = Integer.parseInt(rs.getString(KBN_VAL));
				switch (tempKanjoIdx) {
					case KANJONM_UKETORITEGATA:
						excel.selectCell(20,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_YUSHUTSUUKETORITEGATA:
						excel.selectCell(21,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_URIKAKEKIN:
						excel.selectCell(22,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TORIHIKIMAEWATASHIKIN:
						excel.selectCell(23,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TATEKAEKIN:
						excel.selectCell(24,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_MISHUNYUKIN:
						excel.selectCell(25,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_MISHUSHUEKI:
						excel.selectCell(26,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_TANKIKASHITSUKEKIN:
						excel.selectCell(27,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_SASHIIREHOSHOKIN:
						excel.selectCell(28,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_KARIBARAIKIN:
						excel.selectCell(29,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_CHOKIKASHITSUKEKIN:
						excel.selectCell(30,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_SONOTATOSHI:
						excel.selectCell(31,34,false);
						excel.setCellValue(Function.trim(rs.getString(KBN_HYOUJI_VAL)));
						break;
					case KANJONM_HOSHOSAIMU:
						excel.selectCell(42,32,false);
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

	/**
     *  引当データを取得<br>
     * 
     * @param Excel
     * @throws SQLException
     */
	public void getHikiateData(Excel excel) throws SQLException{
		int tempHikiIdx = 0;
		int clm_kanjo = 40;
		ResultSet rs = null;
    	//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1700, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
		exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            while(rs.next()) {
            	//通貨コード
            	excel.selectCell(18,42,false);
       			excel.setCellValue(Function.replaceString(excel.getStringCellValue(),Function.trim(rs.getString(TUUKA_CD))));
            	excel.selectCell(18,59,false);
       			excel.setCellValue(Function.replaceString(excel.getStringCellValue(),Function.trim(rs.getString(TUUKA_CD))));
            	excel.selectCell(24,59,false);
       			excel.setCellValue(Function.replaceString(excel.getStringCellValue(),Function.trim(rs.getString(TUUKA_CD))));
        		tempHikiIdx = Integer.parseInt(rs.getString(KANJO_HYOUJI_KBN));
				if((GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
					continue;
				}
        		switch (tempHikiIdx) {
					case KANJO_UKETORITEGATA:
						excel.selectCell(20,40,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_YUSHUTSUUKETORITEGATA:
						excel.selectCell(21,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_URIKAKEKIN:
						excel.selectCell(22,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_TORIHIKIMAEWATASHIKIN:
						excel.selectCell(23,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_TATEKAEKIN:
						excel.selectCell(24,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_MISHUNYUKIN:
						excel.selectCell(25,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_MISHUSHUEKI:
						excel.selectCell(26,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_TANKIKASHITSUKEKIN:
						excel.selectCell(27,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_SASHIIREHOSHOKIN:
						excel.selectCell(28,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_KARIBARAIKIN:
						excel.selectCell(29,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_CHOKIKASHITSUKEKIN:
						excel.selectCell(30,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_SONOTATOSHI:
						excel.selectCell(31,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_HOSHOSAIMU:
						excel.selectCell(42,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
						break;
					case KANJO_KIBIKIATEKIN:
						excel.selectCell(45,clm_kanjo,false);
						excel.setCellValue(rs.getDouble(KINGAKU));
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
	 * 第三者留保債務内訳取得 <br>
	 * 
     * @param Excel
	 * @exception SQLException
	 */
	public void getOth_ryuho(Excel excel) throws SQLException {
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T2000, sqlExec);
	    exCstmt.setStringIn(tori_bean.getSystem_kbn());
	    exCstmt.setStringIn(tori_bean.getPhase());
	    exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			int i = 20;
			while(rs.next()){
				excel.selectCell(i,44,false);
				excel.setCellValue(rs.getString(KIKAN_TORI_CD));
				excel.selectCell(i,48,false);
				excel.setCellValue(rs.getString(TORISAKI_NM));
				excel.selectCell(i,52,false);
				excel.setCellValue(rs.getString(KANJO_NM));
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(KINGAKU))))){
					excel.selectCell(i,56,false);
					excel.setCellValue(rs.getDouble(KINGAKU));
				}
				i++;
			}
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
		
	/**
     *  コメントを取得<br>
     * 
     * @param Excel
     * @param String
     * @throws SQLException
     */
	public void getComment(Excel excel,String torihikisaki_kbn) throws SQLException{
		int point = 0;
		int row = 0;

		//判定根拠の出力セル判定
		if(SEIJO.equals(torihikisaki_kbn) || YOTYUI.equals(torihikisaki_kbn)){
			row = 17;
		}else if(KASIDAOREKENEN.equals(torihikisaki_kbn)){
			row = 36;
		}else if(HASANKOSEI.equals(torihikisaki_kbn)){
			row = 53;
		}
		ResultSet rs = null;
    	//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setStringIn(tori_bean.getPhase());
		exCstmt.setStringIn(SANSYO_POINT);
		exCstmt.setResultSet(RESULTSET);
		try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            while(rs.next()){
            	point = Integer.parseInt(rs.getString(TOROKU_POINT));
            	switch(point){
            		case 10:
        				excel.selectCell(56,1,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 20:
        				excel.selectCell(row,16,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 30:
        				excel.selectCell(6,44,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 40:
        				excel.selectCell(9,36,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 50:
        				excel.selectCell(29,48,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 60:
        				excel.selectCell(34,44,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 70:
        				excel.selectCell(40,44,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 80:
        				excel.selectCell(46,44,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
        				break;
            		case 90:
        				excel.selectCell(53,32,false);
        				excel.setCellValue(Function.trim(rs.getString(COMMENT_VAL)));
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