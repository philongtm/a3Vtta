/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/02		SSC				課題No.28 引当対象外/帳簿外対応 
******************************************************************************/

package app.common.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  OZ6106_引当金確認照会タブ DBアクセスクラス<br>
 */

public class KakuninSyokaiDbAcc extends CommonDbAcc {

	private UserBean user_bean = null;              // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    
	private static final String TAISHO_YM						= "taisho_ym";	
	private static final String TAISHO_YM_HYOUJI				= "taisho_ym_hyoji";	
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";
	private static final String KIJUNBI_KBN_1					= "1";
    private static final String SAIKEN_KBN					= "saiken_kbn";			// 債権区分
   
    //Resultset用文字列  
	// 課題No.28
	// 追加開始
	private static final String HIKIATEKIN_SHOSAI_NM		= "hikiatekin_shosai_nm";	// 引当金詳細
	// 追加完了
	private static final String ANKEN_NO					= "anken_no";				// 査定案件No.
    private static final String KTK 						= "ktk";					// 信用格付情報
    private static final String OYAKTK 					= "oya_ktk";				// 親会社信用格付情報
    private static final String OYAITTAI 					= "oya_ittai";				// 親会社一体独立
    private static final String OYA_NM 					= "oya_nm";					// 親会社名称 
    private static final String TAIRYU_KBN_NM 			= "tairyu_kbn_nm";			// 滞留区分名称
    private static final String KONKI_TORIHIKISAKI_KBN 	= "torihikisaki_kbn_nm2";	// 今期取引先区分
    private static final String KONKI_SAIKEN_KBN 			= "saiken_kbn_nm2";			// 今期債権区分
    private static final String KOMOKU1 					= "komoku1";				// 項目１
    private static final String RYUHOSAIMU 				= "ryuhosaimu";				// 留保債務
    private static final String OTH_RYUHOSAIMU 			= "oth_ryuhosaimu";			// 第三者留保債務
    private static final String HOZEN 					= "hozen";					// 保全
    private static final String SONOTAKAISYU 				= "sonotakaisyu";			// その他回収
    private static final String RIKO_KENEN 				= "riko_kenen";				// 履行請求懸念
    private static final String TUIKA_HIKIATE 			= "tuika_hikiate";			// 追加引当金額
    private static final String KOMOKU2 					= "komoku2";				// 項目２
    private static final String TAIRYU_KBN 				= "tairyu_kbn";				// 滞留区分
    private static final String KIJUN_SAIKEN_KBN 			= "kijun_saiken_kbn";		// 仮基準日債権区分名称
    private static final String KIJUN_TORIHIKISAKI_KBN 	= "kijun_torihikisaki_kbn";	// 仮基準日取引先区分名称
    private static final String SAIKEN_KBN_NM 			= "saiken_kbn_nm";			// 債権区分名称
    private static final String TORIHIKISAKI_KBN_NM 		= "torihikisaki_kbn_nm";	// 取引先区分名称
    private static final String TUUKA_CD					= "tuuka_cd";				// 通貨コード
    private static final String KANJO_HYOUJI_KBN			= "kanjo_hyouji_kbn";		// 勘定科目表示区分
    private static final String KINGAKU					= "kingaku";				// 金額
    private static final String TOROKU_POINT				= "toroku_point";			// 登録箇所
    private static final String COMMENT_VAL				= "comment_val";			// コメント内容
    private static final String OUTPHASE 					= "outPhase";				// 輸出フェーズ
    private static final String OUTHYOJIYM 				= "outHyojiYm";				// 輸出表示用年月
    private static final String OUTYM 					= "outYm";					// 輸出年月
    private static final String OUTSYORIKAISU 			= "outSyoriKaisu";			// 輸出処理回数
      
    private static final String SP_SS_OL_SELECT_T1400                 = "SP_SS_OL_SELECT_T1400";              	// 前期データ取得用案件Noを取得する
    private static final String SP_SS_OL_SELECT_T1401                 = "SP_SS_OL_SELECT_T1401";					// 仮基準日データ取得用案件Noを取得する
    private static final String SP_SS_OL_SELECT_T1402                 = "SP_SS_OL_SELECT_T1402";					// 前期データ・仮基準日データ取得用案件No.のフェーズ及び年月を取得
    private static final String SP_SS_OL_SELECT_T1500                 = "SP_SS_OL_SELECT_T1500";					// 滞留区分、債権区分、取引先区分、引当金確認情報を取得する
    private static final String SP_SS_OL_SELECT_T1700                 = "SP_SS_OL_SELECT_T1700";					// 仮基準日の引当金確認情報を取得
    private static final String SP_SS_OL_SELECT_T1800                 = "SP_SS_OL_SELECT_T1800";					// 前期、今期の引当金確認情報を取得
    private static final String SP_SS_OL_SELECT_T1200                 = "SP_SS_OL_SELECT_T1200";					// 区分判定根拠・引当金算定根拠を取得する。
    private static final String SP_SS_OL_SELECT_KAKUDUKE              = "SP_SS_OL_SELECT_KAKUDUKE";				// 前期・仮基準日の格付情報・親会社情報、今期の親会社名称を取得する
	private static final String SP_SS_OZ6108_SELECT_MEISAI            = "SP_SS_OZ6108_SELECT_MEISAI";				//前期・仮基準日の明細取得用プロシージャ
	private static final String SP_SS_O_SELECT_M2200                  = "SP_SS_O_SELECT_M2200";

    // INパラメータ
    private String workflowSystemkbn;   // 業務フローパターンシステム区分
    private String comLangMode;         // 共)言語モード
    private String sateiKaishaCd;		 // 共)取引先情報.査定会社コード
    private String miseCd;				 // 店コード
    private String kanCd;				 // 取引先コード
    private String sateiKi;			 // 査定期
    private String syoriKaisu;			 // 処理回数
    private String phase;				 // フェーズ
    private String systemKbn;           // 共)取引先情報.システム区分
    
    /**
     * コンストラクタ
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    
    
    public KakuninSyokaiDbAcc	(SqlExecuter sqlExec, Log log, AppContext appcontext) {
    	
        super(sqlExec, log);
   
        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        
        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
        systemKbn = tori_bean.getSystem_kbn();
        sateiKaishaCd = tori_bean.getSateikaisya_cd();
        miseCd = tori_bean.getMise_cd();
        kanCd = Function.trim(tori_bean.getKanjo_cd());
        sateiKi = tori_bean.getSatei_ki();
        syoriKaisu = tori_bean.getSyori_kaisu();
        phase = tori_bean.getPhase();
    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        systemKbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
        sateiKaishaCd = GS.EMPTY_CHARCTER;
        miseCd = GS.EMPTY_CHARCTER;
        kanCd = GS.EMPTY_CHARCTER;
        sateiKi = GS.EMPTY_CHARCTER;
        syoriKaisu = GS.EMPTY_CHARCTER;
        phase = GS.EMPTY_CHARCTER;
    }
    
    /**
     * 
     *  前期データ取得用案件Noを取得<br>
     * 
     * @throws SQLException
     */
    public String getZenkiAnkenNo() throws SQLException {
	    // 処理結果フラグ
	    String result = null;
		ResultSet rs = null;

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1400, sqlExec);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(sateiKaishaCd);		
		exCstmt.setStringIn(miseCd);
		exCstmt.setStringIn(kanCd);
		exCstmt.setStringIn(sateiKi);
		exCstmt.setStringIn(syoriKaisu);
		exCstmt.setStringIn(phase);
		exCstmt.setStringOut(ANKEN_NO);
	
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	result = exCstmt.getString(ANKEN_NO);
	     } finally {
        	if (rs != null) {
       			//Resultset close
       			rs.close();
        	}
	     } 
	     
	     return result;
    }
    
    /**
     * 
     *  仮基準日データ取得用案件Noを取得<br>
     * 
     * @throws SQLException
     */
    public String getKijunAnkenNo() throws SQLException {
	    // 処理結果フラグ
	    String result = null;
		ResultSet rs = null;
	    
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1401, sqlExec);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(sateiKaishaCd);		
		exCstmt.setStringIn(miseCd);
		exCstmt.setStringIn(kanCd);
		exCstmt.setStringIn(sateiKi);
		exCstmt.setStringOut(ANKEN_NO);
			
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	result = exCstmt.getString(ANKEN_NO);
	     } finally {
        	if (rs != null) {
       			//Resultset close
       			rs.close();
        	}
	     } 
	     
	     return result;
    }
    
    /**
     * 
     *  前期データ・仮基準日データ取得用案件No.のフェーズ及び年月を取得<br>
     * 
     * @throws SQLException
     */
    public Map<String,String> getYm(String ankenNo) throws SQLException {
    	Map<String,String> resMap = new HashMap<String,String>();  	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1402, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(comLangMode);		
		exCstmt.setStringOut(OUTPHASE);
		exCstmt.setStringOut(OUTHYOJIYM);
		exCstmt.setStringOut(OUTYM);
		exCstmt.setStringOut(OUTSYORIKAISU);
		
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	resMap.put(OUTPHASE, exCstmt.getString(OUTPHASE));
	    	resMap.put(OUTHYOJIYM, exCstmt.getString(OUTHYOJIYM));
	    	resMap.put(OUTYM, exCstmt.getString(OUTYM));
	    	resMap.put(OUTSYORIKAISU, exCstmt.getString(OUTSYORIKAISU));
	     } finally {
        	if (rs != null) {
       			//Resultset close
       			rs.close();
        	}
	     }
	     
	     return resMap;
    }
    
    /**
     * 
     *  前期データ・仮基準日データの基本情報を取得<br>
     * 
     * @return
     * @throws SQLException
     */
    public Map<String,String> getKtk(String ym,String syoriKaisu) throws SQLException {	
    	Map<String,String> resMap = new HashMap<String,String>();

    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_KAKUDUKE, sqlExec);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(miseCd);
		exCstmt.setStringIn(kanCd);
		exCstmt.setStringIn(syoriKaisu);
		exCstmt.setStringIn(ym);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(sateiKaishaCd);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	resMap.put(KTK, rs.getString(KTK));
            	resMap.put(OYAKTK, rs.getString(OYAKTK));
            	resMap.put(OYAITTAI, rs.getString(OYAITTAI));
            	resMap.put(OYA_NM, rs.getString(OYA_NM));
            }
            
        } finally {
            if (rs != null) {
                try {
                    //Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
		
    	return resMap;
    }
    
    /*/**
     * 
     * 今期の親会社名称を取得 <br>
     * 
     * @param ym
     * @param syoriKaisu
     * @return
     * @throws SQLException
     */
    /*public String getCompanyNm() throws SQLException {
    	String result = "OUT_OYA_KAISYA";
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_E0200, sqlExec);
		exCstmt.setStringIn(shikibetuCd);			// 識別コード
		exCstmt.setStringIn(toriCd);				// 取引先コード	
		exCstmt.setStringIn(syoriKaisu);			// 処理回数
		exCstmt.setStringIn(ym);					// 対象年月
		exCstmt.setStringIn(comLangMode);			// 言語モード
		exCstmt.setStringIn(systemKbn);				// 共)取引先情報.基幹システム区分
		exCstmt.setStringIn(sateiKaishaCd);			// 共)取引先情報.査定会社コード
		exCstmt.setStringIn(miseCd);				// 共)取引先情報.店コード
		exCstmt.setStringOut(result);				// 親会社名称
		
		try{
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
	    	result = exCstmt.getString(result);
	     } finally {
        	if (rs != null) {
       			//Resultset close
       			rs.close();
        	}
	     } 
	     
    	return result;
    }*/
    
    /**
     * 
     *  滞留区分、債権区分、取引先区分、引当金確認情報を取得する<br>
     * 
     * @return
     * @throws SQLException
     */
    public Map<String,String> getSelT15(String ankenNo,String phase) throws SQLException { 
    	Map<String,String> resMap = new HashMap<String,String>();
    	ResultSet rs = null;
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	resMap.put(TAIRYU_KBN_NM, rs.getString(TAIRYU_KBN_NM));
            	resMap.put(KONKI_TORIHIKISAKI_KBN, rs.getString(TORIHIKISAKI_KBN_NM));
            	resMap.put(KONKI_SAIKEN_KBN, rs.getString(SAIKEN_KBN));
            	resMap.put(KOMOKU1, rs.getString(KOMOKU1));
            	resMap.put(RYUHOSAIMU, rs.getString(RYUHOSAIMU));
            	resMap.put(OTH_RYUHOSAIMU, rs.getString(OTH_RYUHOSAIMU));
            	resMap.put(HOZEN, rs.getString(HOZEN));
            	resMap.put(SONOTAKAISYU, rs.getString(SONOTAKAISYU));
            	resMap.put(RIKO_KENEN, rs.getString(RIKO_KENEN));
            	resMap.put(TUIKA_HIKIATE, rs.getString(TUIKA_HIKIATE));
            	resMap.put(KOMOKU2, rs.getString(KOMOKU2));
            	resMap.put(TAIRYU_KBN, rs.getString(TAIRYU_KBN));
            	resMap.put(KIJUN_SAIKEN_KBN, rs.getString(KIJUN_SAIKEN_KBN));
            	resMap.put(KIJUN_TORIHIKISAKI_KBN, rs.getString(KIJUN_TORIHIKISAKI_KBN));
            	resMap.put(SAIKEN_KBN_NM, rs.getString(SAIKEN_KBN_NM));
            	resMap.put(TORIHIKISAKI_KBN_NM, rs.getString(TORIHIKISAKI_KBN_NM));
            	//課題No.28
            	//追加開始
            	resMap.put(HIKIATEKIN_SHOSAI_NM,Function.trim(rs.getString(HIKIATEKIN_SHOSAI_NM)));
            	//追加完了
            }
            
        } finally {
            if (rs != null) {
                try {
                    //Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
		
    	return resMap;
    }
    
    /**
     * 
     * 仮基準日の引当金確認情報を取得 <br>
     * 
     * @param ankenNo
     * @return
     * @throws SQLException
     */
    public List<Map<String,String>> getSelT17(String ankenNo) throws SQLException { 
    	
    	List<Map<String,String>> resList = new ArrayList<Map<String,String>>();
    	ResultSet rs = null;
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1700, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            	
            int i = 0;
            while ( rs.next() ) {
            	Map<String,String> map = new HashMap<String,String>();
            	map.put(TUUKA_CD, rs.getString(TUUKA_CD));
            	map.put(KANJO_HYOUJI_KBN, rs.getString(KANJO_HYOUJI_KBN));
            	map.put(KINGAKU, rs.getString(KINGAKU));
            	resList.add(map);
            	i++;
            }
            
        } finally {
            if (rs != null) {
                try {
                    //Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
		
    	return resList;
    }
    
    /**
     * 
     *  前期、今期の引当金確認情報を取得<br>
     * 
     * @param ankenNo
     * @return
     * @throws SQLException
     */
    public List<Map<String,String>> getSelT18(String ankenNo) throws SQLException { 
    	
    	List<Map<String,String>> resList = new ArrayList<Map<String,String>>();
    	ResultSet rs = null;
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1800, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            	
            int i = 0;
            while ( rs.next() ) {
            	Map<String,String> map = new HashMap<String,String>();
            	map.put(Function.trim(TUUKA_CD), rs.getString(TUUKA_CD));
            	map.put(KANJO_HYOUJI_KBN, rs.getString(KANJO_HYOUJI_KBN));
            	map.put(KINGAKU, rs.getString(KINGAKU));
            	resList.add(map);
            	i++;
            }
            
        } finally {
            if (rs != null) {
                try {
                    //Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
		
    	return resList;
    }
    
    /**
     * 
     *  区分判定根拠・引当金算定根拠を取得する<br>
     * 
     * @param ankenNo
     * @param phase
     * @param point
     * @return
     * @throws SQLException
     */
    public Map<String,String> getSelT12(String ankenNo,String phase,String point) throws SQLException { 
    	
    	Map<String,String> resMap = new HashMap<String,String>();
    	ResultSet rs = null;
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(point);
		exCstmt.setResultSet(RESULTSET);
		
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	resMap.put(TOROKU_POINT, rs.getString(TOROKU_POINT));
            	resMap.put(COMMENT_VAL, rs.getString(COMMENT_VAL));
            }
            
        } finally {
            if (rs != null) {
                try {
                    //Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
		
    	return resMap;
    }
    
	/**
	 * 査定期内対象年月取得 <br>
     * @return Map<String,String> 対象年月
	 * @exception SQLException
	 */
	public Map<String,String> getTaishoYmMap(String sateiki) throws SQLException {

		ResultSet rs = null;
		ExCallableStatement cstmt = null;
    	Map<String,String> ymMap = new HashMap<String,String>();
		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(workflowSystemkbn);
		cstmt.setStringIn(comLangMode);
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
	 * 明細情報取得処理 <br>
     * @return List<Map<String,String>> 明細
     * @param  ym 対象年月
	 * @exception SQLException
	 */
	public List<Map<String,String>> getMeisai(String ym) throws SQLException {

    	List<Map<String,String>> resList = new ArrayList<Map<String,String>>();
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;

		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_OZ6108_SELECT_MEISAI, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(sateiKaishaCd);
		cstmt.setStringIn(kanCd);
		cstmt.setStringIn(miseCd);
		cstmt.setStringIn(ym);
		cstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			while ( rs.next() ) {
            	Map<String,String> map = new HashMap<String,String>();
            	map.put(TUUKA_CD, rs.getString(TUUKA_CD));
            	map.put(KANJO_HYOUJI_KBN, rs.getString(KANJO_HYOUJI_KBN));
            	map.put(KINGAKU, rs.getString(KINGAKU));
            	resList.add(map);
			}
	    } finally {
	    	if (rs != null) {
	    			rs.close();
	    	}
	    }
    	return resList;
	}
}
