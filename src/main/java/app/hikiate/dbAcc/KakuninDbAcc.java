/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/27		SSC				課題No.60 引当金確認の区分設定 
003		2009/10/27		SSC				課題No.28 引当対象外/帳簿外対応 
004		2009/12/17		SSC				課題No.209 承認プルダウン修正
005		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
006		2016/03/18		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/

package app.hikiate.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.form.KakuninForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *  OD1102_引当金確認 DBアクセスクラス<br>
 */
public class KakuninDbAcc extends CommonDbAcc {
	private UserBean user_bean = null;              // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    private KakuninForm form = null;                 // アクションフォーム

	private static final String TAISHO_YM						= "taisho_ym";	
	private static final String TAISHO_YM_HYOUJI				= "taisho_ym_hyoji";	
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";
	private static final String TORIHIKISAKI2_KBN				= "torihikisaki2_kbn";			// 取引先区分
    private static final String SAIKEN_KBN				    = "saiken_kbn";					// 債権区分
    private static final String HANTEI_KBN_2					= "2";							// 判定査定区分
    private static final String POINT_01 						= "01";							// 前期と今期の登録箇所
    private static final String TOROKU_KBN_1					= "1";							// 登録区分:通常
	private static final String FLG_HAISIN_N					= "N";							//配信済みフラグ
	private static final String MODOSHI_FLG_1					= "1";						// 取戻不可フラグ:取戻不可   
	private static final String KIJUNBI_KBN_1					= "1";
	// 課題No.28
	// 追加開始
	private static final String HIKIATEKIN_SHOSAI				= "hikiatekin_shosai";			// 引当金詳細
	private static final String HIKIATEKIN_TAISHO				= "0";							// 引当金詳細区分値：引当金対象
	// 追加完了

    //Resultset用文字列  
	private static final String ANKEN_NO					= "anken_no";				// 査定案件No.
    private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";         // 表示値
    private static final String KBN_VAL					= "kbn_val";                // 表示キー
    private static final String TANTO_NAME				= "tanto_name";             // 担当者名
    private static final String TOGO_ID					= "togo_id";                // 統合ID
    private static final String KTK 						= "ktk";					// 信用格付情報
    private static final String OYAKTK 					= "oya_ktk";				// 親会社信用格付情報
    private static final String OYAITTAI 					= "oya_ittai";				// 親会社一体独立
    private static final String OYA_NM 					= "oya_nm";					// 親会社名称 
    private static final String TAIRYU_KBN_NM 			= "tairyu_kbn_nm";			// 滞留区分名称
    private static final String KONKI_TORIHIKISAKI_KBN 	= "konki_torihikisaki_kbn";	// 今期取引先区分
    private static final String KONKI_SAIKEN_KBN 			= "konki_saiken_kbn";		// 今期債権区分
    private static final String KARI_TORIHIKISAKI_KBN		= "torihikisaki_kbn";		// 取引先区分
    private static final String KARI_SAIKEN_KBN			= "saiken_kbn";				// 債権区分
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
      
    private static final String SP_SS_O_SELECT_SHONINSHA              = "SP_SS_O_SELECT_SHONINSHA";				//承認担当者セレクトボックスの設定値取得プロシージャ
    private static final String SP_SS_OL_SELECT_T1400                 = "SP_SS_OL_SELECT_T1400";              	//前期データ取得用案件Noを取得する
    private static final String SP_SS_OL_SELECT_T1401                 = "SP_SS_OL_SELECT_T1401";					//仮基準日データ取得用案件Noを取得する
    private static final String SP_SS_OL_SELECT_T1402                 = "SP_SS_OL_SELECT_T1402";					//前期データ・仮基準日データ取得用案件No.のフェーズ及び年月を取得
    private static final String SP_SS_OL_SELECT_T1500                 = "SP_SS_OL_SELECT_T1500";					//滞留区分、債権区分、取引先区分、引当金確認情報を取得する
    private static final String SP_SS_OL_SELECT_T1700                 = "SP_SS_OL_SELECT_T1700";					//仮基準日の引当金確認情報を取得
    private static final String SP_SS_OL_SELECT_T1800                 = "SP_SS_OL_SELECT_T1800";					//前期、今期の引当金確認情報を取得
    private static final String SP_SS_OL_SELECT_T1200                 = "SP_SS_OL_SELECT_T1200";					//区分判定根拠・引当金算定根拠を取得する。
    private static final String SP_SS_O_UPDATE_T1400                  = "SP_SS_O_UPDATE_T1400";					//案件の進捗を更新する
    private static final String SP_SS_O_DELETE_T1200                  = "SP_SS_O_DELETE_T1200";					//区分判定根拠の削除を行う
    private static final String SP_SS_O_INSERT_T1200                  = "SP_SS_O_INSERT_T1200";					//区分判定根拠の登録(データの再作成)を行う
    private static final String SP_SS_OD1102_UPDATE_T1500             = "SP_SS_OD1102_UPDATE_T1500";				//変更された取引先区分・債権区分の更新を行う
    private static final String SP_SS_OD1102_UPDATE_T1501             = "SP_SS_OD1102_UPDATE_T1501";				//変更された取引先区分・債権区分の更新を行う
    private static final String SP_SS_O_INSERT_T0400                  = "SP_SS_O_INSERT_T0400";					//T04_メール配信の登録プロシージャ
    private static final String SP_SS_O_INSERT_T1300                  = "SP_SS_O_INSERT_T1300";					//T13_入力履歴（SST_NYURYOKU_HIST）の登録プロシージャ
    private static final String SP_SS_OL_SELECT_KAKUDUKE              = "SP_SS_OL_SELECT_KAKUDUKE";				//前期・仮基準日の格付情報・親会社情報、今期の親会社名称を取得する
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
    public KakuninDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);

        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        form = (KakuninForm)appcontext.getActionForm();

        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
        systemKbn = tori_bean.getSystem_kbn();

        sateiKaishaCd = tori_bean.getSateikaisya_cd();
        miseCd = tori_bean.getMise_cd();
        kanCd = tori_bean.getKanjo_cd();
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
     * 承認担当者セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getTanto() throws SQLException {
        
        String comSyozokuBunrui1 = tori_bean.getSateikaisya_cd();
        String bunrui2 = tori_bean.getBunrui2();
        String bu_cd = tori_bean.getBu_cd();
        String taisyo_ym = tori_bean.getTaisyo_ym();
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_SHONINSHA, sqlExec);
        cstmt.setStringIn(comLangMode);
        cstmt.setStringIn(comSyozokuBunrui1);
        cstmt.setStringIn(bunrui2);
        cstmt.setStringIn(bu_cd);
        cstmt.setStringIn(taisyo_ym);
        cstmt.setStringIn(form.getJi_jishi_phase());
        cstmt.setStringIn(form.getJi_kaishi_status());
        cstmt.setStringIn(GS.EMPTY_CHARCTER);
        cstmt.setStringIn(Function.trim(systemKbn));
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_tanto = new LinkedHashMap<String,String>();
            while ( rs.next() ) {
				//課題No.209
				//修正開始
            	//ar_tanto.put(rs.getString(TANTO_NAME),rs.getString(TOGO_ID));                    
                ar_tanto.put(rs.getString(TOGO_ID),rs.getString(TANTO_NAME));                    
				//修正完了
            }
            form.setAr_shonin_tanto(ar_tanto);
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
        
    }
    
	// 課題No.28
	// 追加開始
    /**
     * 
     *  引当金詳細【リスト】を取得する<br>
     * 
     * @throws SQLException
     */
    public void getHikiatekinShosai() throws SQLException {
        ResultSet rs = null;
        try{
            //ResultSet取得
            rs = getKbnval(HIKIATEKIN_SHOSAI,workflowSystemkbn,comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_kbn = new LinkedHashMap<String,String>();
        	ar_kbn.put(GS.EMPTY_CHARCTER,HIKIATEKIN_TAISHO);
            int i = 0;
            while ( rs.next() ) {
            	ar_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
                i++;
            }
            form.setAr_hikiatekin_shosai(ar_kbn);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }   
	// 追加完了

    /**
     * 
     *  取引先区分【リスト】を取得する<br>
     * 
     * @throws SQLException
     */
    public void getTorihiki() throws SQLException {

        ResultSet rs = null;

        try{
            //ResultSet取得
            rs = getKbnval(TORIHIKISAKI2_KBN,workflowSystemkbn,comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_kbn = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
            	ar_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_konki_torihikisaki_kbn(ar_kbn);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }   
    
    /**
     * 
     *  債権区分【リスト】を取得<br>
     * 
     * @throws SQLException
     */
    public void getSaiken() throws SQLException {

        ResultSet rs = null;

        try{
            //ResultSet取得
            rs = getKbnval(SAIKEN_KBN,workflowSystemkbn,comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_kbn = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
            	ar_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_konki_saiken_kbn(ar_kbn);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
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

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1400, sqlExec);
    	ResultSet rs = null;
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

		//ExCallableStatement生成
    	ResultSet rs = null;
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
            	resMap.put(KONKI_TORIHIKISAKI_KBN, rs.getString(KONKI_TORIHIKISAKI_KBN));
            	resMap.put(KONKI_SAIKEN_KBN, rs.getString(KONKI_SAIKEN_KBN));
            	resMap.put(KARI_TORIHIKISAKI_KBN, rs.getString(KARI_TORIHIKISAKI_KBN));
            	resMap.put(KARI_SAIKEN_KBN, rs.getString(KARI_SAIKEN_KBN));
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
            	resMap.put(HIKIATEKIN_SHOSAI, rs.getString(HIKIATEKIN_SHOSAI));
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
	 * 査定期内対象年月取得 <br>
     * @return Map<String,String> 対象年月
	 * @exception SQLException
	 */
	public Map<String,String> getTaishoYmMap(String sateiki) throws SQLException {

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
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
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;

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
     * 
     *  案件の進捗を更新する<br>
     * 
     * @param status
     * @param hojiUserId
     * @param modoshiFlg
     * @throws SQLException
     */
    public void uptT14(String status,String hojiUserId,String modoshiFlg) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(form.getJishi_phase());				// フェーズ
		exCstmt.setStringIn(status);							// ステータス
		exCstmt.setStringIn(hojiUserId);						// 案件保持ユーザID
        exCstmt.setStringIn(null); 								// 査定登録画面
		exCstmt.setStringIn(tori_bean.getSasi_ten_flg());		// 転送フラグ
		exCstmt.setStringIn(null);								// 代行ユーザID
		if(tori_bean.getDaiko_user_id() == null || tori_bean.getDaiko_user_id().length() == 0){
			exCstmt.setStringIn(user_bean.getComUserId());		// 更新ユーザID = ユーザID
		}else{
			exCstmt.setStringIn(tori_bean.getDaiko_user_id());		// 更新ユーザID = 代行ユーザID
		}
		
		exCstmt.setStringIn(modoshiFlg);
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  案件の進捗を更新する<br>
     * 
     * @param status
     * @param hojiUserId
     * @throws SQLException
     */
    public void uptT14(String status,String hojiUserId) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(form.getJishi_phase());				// フェーズ
		exCstmt.setStringIn(status);							// ステータス
		exCstmt.setStringIn(hojiUserId);						// 案件保持ユーザID
        exCstmt.setStringIn(null); 								// 査定登録画面
		exCstmt.setStringIn(tori_bean.getSasi_ten_flg());		// 転送フラグ
		exCstmt.setStringIn(tori_bean.getDaiko_user_id());		// 代行ユーザID
		exCstmt.setStringIn(user_bean.getComUserId());			// ユーザID
		exCstmt.setStringIn(MODOSHI_FLG_1);
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  入力履歴の登録を行う<br>
     * 
     * @throws SQLException
     */
    public void intT13(String kbn) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());					// 査定案件No
		exCstmt.setStringIn(HANTEI_KBN_2);								// 判定査定区分
		exCstmt.setStringIn(tori_bean.getSateikaisya_cd());				// 査定会社コード
		exCstmt.setStringIn(user_bean.getComUserId());					// ユーザID
		exCstmt.setStringIn(user_bean.getComUser_Nm());					// ユーザ名日本語
		exCstmt.setStringIn(user_bean.getComUser_Nm_En());				// ユーザ名英語
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());		// 所属部署名日本語
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());	// 所属部署名英語
		exCstmt.setStringIn(tori_bean.getPhase());						// フェーズ
		exCstmt.setStringIn(kbn);										// 入力区分
		exCstmt.setStringIn(user_bean.getComUserId());					// 登録ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());			// 代行ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm());			// 代行者名日本語
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());		// 代行者名英語
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 登録箇所
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// コメント内容
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元分類２
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元部
		exCstmt.setStringIn(form.getShonin_tanto());					// 承認ユーザID
		
		try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
		
    }
    
    /**
     * 
     *  コメント削除<br>
     * 
     * @throws SQLException
     */
    public void delT12() throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1200, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(tori_bean.getPhase());				// フェーズ
		exCstmt.setStringIn(POINT_01);							// 登録箇所
		
		try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  コメント類の再作成を行う<br>
     * 
     * @throws SQLException
     */
    public void intT12() throws SQLException {
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1200, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(tori_bean.getPhase());				// フェーズ
		exCstmt.setStringIn(POINT_01);							// 登録箇所
		exCstmt.setStringIn(TOROKU_KBN_1);						// 登録区分
		exCstmt.setStringIn(form.getKonki_commond());			// コメント内容
		exCstmt.setStringIn(user_bean.getComUserId());			// 登録ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());	// 代行者ID
			
		try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  変更された取引先区分・債権区分の更新を行う<br>
     * 
     * @throws SQLException
     */
    public void uptT15(String torihikisakiKbn,String saikenKbn,String hikiatekin_shosai) throws SQLException {
    	//課題No.60
    	//修正開始
    	//String torihikisakiKbnZen = GS.EMPTY_CHARCTER;
    	//String saikenKbnZen = GS.EMPTY_CHARCTER;
    	String torihikisakiKbnZen = Function.trim(form.getHiddn_torihikisaki_kbn());
    	String saikenKbnZen = Function.trim(form.getHiddn_saiken_kbn());
    	//if(!(GS.EMPTY_CHARCTER.equals(torihikisakiKbn) && GS.EMPTY_CHARCTER.equals(saikenKbn))){
    		//if(GS.EMPTY_CHARCTER.equals(Function.trim(form.getHiddn_saiken_kbn())) && GS.EMPTY_CHARCTER.equals(Function.trim(form.getHiddn_torihikisaki_kbn()))){
        	if(GS.EMPTY_CHARCTER.equals(torihikisakiKbnZen) && GS.EMPTY_CHARCTER.equals(saikenKbnZen)){
        		torihikisakiKbnZen = torihikisakiKbn;
        		saikenKbnZen = saikenKbn;
        	//}else{
        		//torihikisakiKbnZen = Function.trim(form.getHiddn_torihikisaki_kbn());
        		//saikenKbnZen = Function.trim(form.getHiddn_saiken_kbn());
        	}
    	//}    	
        //修正完了
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1102_UPDATE_T1500, sqlExec);
		exCstmt.setStringIn(tori_bean.getDaiko_user_id());				// 共）ユーザ情報.代行ユーザID
		exCstmt.setStringIn(torihikisakiKbn);							// 機)今期.取引先区分
		exCstmt.setStringIn(saikenKbn);									// 機)今期.債権区分
		exCstmt.setStringIn(torihikisakiKbnZen);						// 機)今期.変更前取引先区分
		exCstmt.setStringIn(saikenKbnZen);								// 機)今期.変更前債権区分
		exCstmt.setStringIn(hikiatekin_shosai);							// 機)引当金詳細
		exCstmt.setStringIn(user_bean.getComUserId());					// 共）ユーザ情報.ユーザID
		exCstmt.setStringIn(tori_bean.getAnken_no());					// 共）取引先情報.査定案件NO.
		exCstmt.setStringIn(tori_bean.getPhase());						// 共）取引先情報.フェーズ
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  変更された取引先区分・債権区分の更新を行う<br>
     * 
     * @throws SQLException
     */
    public void uptT15_konki(String torihikisakiKbn,String saikenKbn,String hikiatekin_shosai) throws SQLException { 
    	//課題No.60
    	//修正開始
    	/*InputCheck inChk = new InputCheck();
    	// 取引先区分セレクトボックス・債権区分セレクトボックスが変更された場合
    	if((!inChk.isNullBlank(form.getHiddn_torihikisaki_kbn()) && form.getHiddn_torihikisaki_kbn().equals(form.getKonki_torihikisaki_kbn())) || 
    			(!inChk.isNullBlank(form.getHiddn_saiken_kbn()) && form.getHiddn_saiken_kbn().equals(form.getKonki_saiken_kbn()))){
    		return;
    	}*/
    	//修正完了
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1102_UPDATE_T1501, sqlExec);
		exCstmt.setStringIn(tori_bean.getDaiko_user_id());				// 共）ユーザ情報.代行ユーザID
		exCstmt.setStringIn(torihikisakiKbn);							// 機)今期.取引先区分
		exCstmt.setStringIn(saikenKbn);									// 機)今期.債権区分
		// 課題No.28
		// 追加開始
		exCstmt.setStringIn(hikiatekin_shosai);							// 機)引当金詳細
		// 追加完了
		exCstmt.setStringIn(user_bean.getComUserId());					// 共）ユーザ情報.ユーザID
		exCstmt.setStringIn(tori_bean.getAnken_no());					// 共）取引先情報.査定案件NO.
		exCstmt.setStringIn(tori_bean.getPhase());						// 共）取引先情報.フェーズ
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  変更された取引先区分・債権区分の更新を行う<br>
     * 
     * @throws SQLException
     */
    public void uptT15_zenki(String torihikisakiKbn,String saikenKbn) throws SQLException { 
    	InputCheck inChk = new InputCheck();
    	// 取引先区分セレクトボックス・債権区分セレクトボックスが変更された場合
    	if((!inChk.isNullBlank(form.getHiddn_torihikisaki_kbn()) && form.getHiddn_torihikisaki_kbn().equals(form.getKonki_torihikisaki_kbn())) || 
    			(!inChk.isNullBlank(form.getHiddn_saiken_kbn()) && form.getHiddn_saiken_kbn().equals(form.getKonki_saiken_kbn()))){
    		return;
    	}
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1102_UPDATE_T1501, sqlExec);
		exCstmt.setStringIn(tori_bean.getDaiko_user_id());				// 共）ユーザ情報.代行ユーザID
		exCstmt.setStringIn(torihikisakiKbn);							// 機)今期.取引先区分
		exCstmt.setStringIn(saikenKbn);									// 機)今期.債権区分
		// 課題No.28
		// 追加開始
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		// 追加完了
		exCstmt.setStringIn(user_bean.getComUserId());					// 共）ユーザ情報.ユーザID
		exCstmt.setStringIn(form.getKijun_anken_no());					// 共）取引先情報.査定案件NO.
		exCstmt.setStringIn(form.getKijun_phase());						// 共）取引先情報.フェーズ
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    
    /**
     * 
     *  変更された取引先区分・債権区分の更新を行う<br>
     * 
     * @throws SQLException
     */
    public void uptT15_init(String kijunAnkenNo,String kijunPhase) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1102_UPDATE_T1501, sqlExec);
		exCstmt.setStringIn(tori_bean.getDaiko_user_id());				// 共）ユーザ情報.代行ユーザID
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		// 課題No.28
		// 追加開始
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		// 追加完了
		exCstmt.setStringIn(user_bean.getComUserId());					// 共）ユーザ情報.ユーザID
		exCstmt.setStringIn(kijunAnkenNo);					// 共）取引先情報.査定案件NO.
		exCstmt.setStringIn(kijunPhase);						// 共）取引先情報.フェーズ
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
    /**
     * 
     * T04_メール配信の登録(SQL04) <br>
     * 
     * @throws SQLException
     */
    public void intT04() throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
		// 共）ユーザ情報.代行ユーザIDがNULLの場合
		exCstmt.setStringIn(user_bean.getComUserId());						// ユーザID
		exCstmt.setStringIn(tori_bean.getAnken_no());						// 案件No
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());						// 対象年月
		exCstmt.setStringIn(tori_bean.getSateikaisya_cd());					// 査定会社コード
		exCstmt.setStringIn(tori_bean.getBunrui2());						// 分類２
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);								// 部コード
		exCstmt.setStringIn(form.getJi_jishi_phase());						// 次実施フェーズ
		exCstmt.setStringIn(form.getJi_kaishi_status());					// 次開始ステータス
		exCstmt.setStringIn(form.getShonin_tanto());						// 配信先担当
		exCstmt.setStringIn(FLG_HAISIN_N);									// 配信済みフラグ
		exCstmt.setStringIn(form.toString());								// 画面ID	
		if(user_bean.getComDaiko_userId() == null || user_bean.getComDaiko_userId().length() == 0){
			exCstmt.setStringIn(user_bean.getComUserId());					// ユーザID
		}else{
			exCstmt.setStringIn(user_bean.getComDaiko_userId());			// 代行ユーザID
		}
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
    }
}
