/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.WorkFlowBean;
import app.system.form.WorkFlowTorokuForm;
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
 *  OS7105　業務フローパターンメンテナンス_登録 DBアクセスクラス<BR>
 */
public class WorkFlowTorokuDbAcc extends CommonDbAcc {
    private SessionData cmnData = null;                 // 機能共通セッション
    private UserBean user_bean = null;                  // ユーザ情報
    private WorkFlowBean workBean = null;				  // 業務フローパターンメンテナンス情報Beanクラス
    private WorkFlowTorokuForm form = null;             // アクションフォーム
    private AppContext appContext = null;               // ＡＰＰコンテキスト
    
    InputCheck inChk = new InputCheck();

    //Resultset用文字列 
    private static final String JI_JISHI_PHASE       = "ji_jishi_phase";		// 次フェーズ
    private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
    private static final String KBN_VAL              = "kbn_val";
    private static final String SYSTEM_KBN           = "system_kbn";           	// システム区分キー
    private static final String HANYO_1              = "hanyo1";              	// 汎用1セレクトボックス
    private static final String NIJI_SATEI_KBN       = "niji_satei_kbn";			// 二次査定区分
    private static final String CNT                  = "cnt";						// カウント
    private static final String PATTERN_NAME_JP      = "pattern_name_jp";			// パターン名称（日本語）
    private static final String PATTERN_NAME_EN      = "pattern_name_en";			// パターン名称（英語）
    private static final String MAX_PATTERN_ID       = "max_pattern_id";
    private static final String GEN_PHASE            = "gen_phase";				// 現フェーズ
    private static final String JI_PHASE             = "ji_phase";				// 次フェーズ
    
    private static final String TAIRYU	= "[1-3]{1}[0-9]{1}";
    private static final String SATEI 	= "[4-6]{1}[0-9]{1}";
    private static final String HIKIATE 	= "[7-8]{1}[0-9]{1}";
      
    private static final String SP_SS_O_SELECT_P0202      	= "SP_SS_O_SELECT_P0202";
    private static final String SP_SS_OS7105_SELECT_M1700 	= "SP_SS_OS7105_SELECT_M1700";
    private static final String SP_SS_OS7105_SELECT_M1701 	= "SP_SS_OS7105_SELECT_M1701";
    private static final String SP_SS_OS7105_SELECT_M1800 	= "SP_SS_OS7105_SELECT_M1800";
    private static final String SP_SS_OS7105_INSERT_M1700 	= "SP_SS_OS7105_INSERT_M1700";
    private static final String SP_SS_OS7105_INSERT_M1800 	= "SP_SS_OS7105_INSERT_M1800";
    private static final String SP_SS_OS7105_SELECT_TSTATUS 	= "SP_SS_OS7105_SELECT_TSTATUS";
    private static final String SP_SS_OS7105_SELECT_SSTATUS	= "SP_SS_OS7105_SELECT_SSTATUS";
    private static final String SP_SS_OS7105_UPDATE_M1700		= "SP_SS_OS7105_UPDATE_M1700";
    private static final String SP_SS_OS7105_DELETE_M1800		= "SP_SS_OS7105_DELETE_M1800";
    private static final String SP_SS_OS7105_SELECT_M2400		= "SP_SS_OS7105_SELECT_M2400";
    private static final String SP_SS_OS7105_DELETE_M1700		= "SP_SS_OS7105_DELETE_M1700";
    private static final String SP_SS_OS7105_SELECT_TCOUNT	= "SP_SS_OS7105_SELECT_TCOUNT";
    private static final String SP_SS_OS7105_SELECT_SCOUNT	= "SP_SS_OS7105_SELECT_SCOUNT";
    
    private static final String ORA_MSG = "ORA-00001";


    // INパラメータ
    private String systemKbn;              // ユーザＩＤ
    private String hanyo1;              	// ユーザNM
    private String nameJp;					// 業務フローパターン名称(日本語)
    private String nameEn;					// 業務フローパターン名称(英語)
    private String pattern_id;				// パターンID
   
    /**
     * コンストラクタ
     * 
     * @param SqlExecuter
     * @param Log
     * @param AppContext
     */
    public WorkFlowTorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        workBean = cmnData.getWorkflow_bean();
        form = (WorkFlowTorokuForm)appContext.getActionForm();
        
        systemKbn = form.getSystemkbn();
        hanyo1 = form.getHanyou1();
        nameJp = form.getWorkflow_nm_ja();
        nameEn = form.getWorkflow_nm_en();


    }
    
    /**
     * システム取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getSystemkbn() throws SQLException {

    	ResultSet rs = null;

        try{
            //ResultSet取得
            rs = getKbnval(SYSTEM_KBN,cmnData.getComLangMode(),GS.EMPTY_CHARCTER,SP_SS_O_SELECT_P0202);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_systemkbn = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
            	// 初期設定
                if(i==0){
                    form.setSystemkbn(rs.getString(KBN_VAL));
                }
            	ar_systemkbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_systemkbn(ar_systemkbn);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 汎用1セレクトボックス取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getHanyou1() throws SQLException {

    	ResultSet rs = null;

        try{
        	if(GS.EMPTY_CHARCTER.equals(systemKbn)){
        		systemKbn = GS.GSS;
        	}
        	
            //ResultSet取得
            rs = getKbnval(HANYO_1,systemKbn,cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_hanyou1 = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
                ar_hanyou1.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_hanyou1(ar_hanyou1);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 
     *  次フェーズ取得処理<br>
     * 
     * @throws SQLException
     */
    public LinkedHashMap<String,String> getTugiPhase(String[] strNum) throws SQLException {
    	
    	ResultSet rs = null;
    	
    	// ActionForm に取得値を格納
        LinkedHashMap<String,String> ar_phase = new LinkedHashMap<String,String>();

        try{
        	if("2".equals(form.getGamenFlg())){
        		systemKbn = workBean.getWorkflowSystemkbn();
        	}
        	
            //ResultSet取得
            rs = getKbnval(JI_JISHI_PHASE,systemKbn,cmnData.getComLangMode());

            int i = 0;
            while ( rs.next() ) {
            	for(int j =0; j < strNum.length; j++){
            		if(strNum[j].equals(rs.getString(KBN_VAL))){
            			ar_phase.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL)); 
            			break;
            		}
            	}                  
                i++;
            }
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
        
        return ar_phase;
    }
    
    /**
     * 2次査定区分セレクトボックス取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getNiSatei() throws SQLException {

    	ResultSet rs = null;

        try{
            //ResultSet取得
            rs = getKbnval(NIJI_SATEI_KBN,GS.GSS,cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_ni_satei = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
            	ar_ni_satei.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_ni_satei(ar_ni_satei);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
	 * 区分値取得処理 <br>
	 * @param String
	 * @param String
	 * @param String
	 * @param String
	 * 
	 * @exception SQLException
	 */
	public ResultSet getKbnval(String key,String langmode,String system_kbn,String proName) throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(proName, sqlExec);
		if (!GS.EMPTY_CHARCTER.equals(key)) {
			exCstmt.setStringIn(key);
		}
		if (!GS.EMPTY_CHARCTER.equals(langmode)) {
			exCstmt.setStringIn(langmode);
		}
		if (!GS.EMPTY_CHARCTER.equals(system_kbn)) {
			exCstmt.setStringIn(system_kbn);
		}
		
		//resultSet
		exCstmt.setResultSet(RESULTSET);

		//SQL実行	
    	exCstmt.execute();
    	isError(exCstmt);
		ResultSet rs = exCstmt.getResultSet(RESULTSET);

    	return rs;
	}
	
	/**
	 * 
	 *  重複チェック<br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Map<String,String> selM1700() throws SQLException {
        
        // 処理結果フラグ
		HashMap<String,String> resMap = new HashMap<String,String>();

        //ExCallableStatement生成
        ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_M1700, sqlExec);
        exCstmt.setStringIn(systemKbn);
        exCstmt.setStringIn(hanyo1);
        exCstmt.setStringIn(nameJp);
        exCstmt.setStringIn(nameEn);
        exCstmt.setStringIn(form.getGamenFlg());
        exCstmt.setStringIn(form.getP_name_jp());
        exCstmt.setStringIn(form.getP_name_en());
       
        exCstmt.setResultSet(RESULTSET);
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            
            if (rs.next()) {
            	// カウント
            	resMap.put(CNT, rs.getString(CNT));
            	// パターン名称（日本語）
            	resMap.put(PATTERN_NAME_JP, rs.getString(PATTERN_NAME_JP));
            	// パターン名称（英語）
            	resMap.put(PATTERN_NAME_EN, rs.getString(PATTERN_NAME_EN));
            }
            
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
	 *  パターンIDのMAX値を取得<br>
	 * 
	 * @throws SQLException
	 */
	public void selM1701() throws SQLException {
		
		int result = 0;

		// ExCallableStatement生成
        ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_M1701, sqlExec);
        exCstmt.setStringIn(systemKbn);
        exCstmt.setStringIn(hanyo1);
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            if (rs.next()) {
            	result = rs.getInt(MAX_PATTERN_ID);
            	pattern_id = Integer.toString(result + 1);
            }
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
	}
	
	/**
	 * 
	 *  M17_業務フローパターンマスタ（SSM_WORKFLOW_PATTERN）の登録を行う<br>
	 * 
	 * @throws SQLException
	 */
	public boolean intM1700() throws SQLException {
		
		// ExCallableStatement生成
        ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_INSERT_M1700, sqlExec);
        // システム区分
        exCstmt.setStringIn(form.getSystemkbn());
        //  汎用１
        exCstmt.setStringIn(form.getHanyou1());
        // パターンID
        exCstmt.setStringIn(pattern_id);
        // 業務フローパターン名称-ja
        exCstmt.setStringIn(form.getWorkflow_nm_ja());
        // 業務フローパターン名称-en
        exCstmt.setStringIn(form.getWorkflow_nm_en());
        // 滞留判定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_touroku()));
        // 滞留判定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_shounin()));
        // 滞留判定検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_kenshou_touroku()));
        // 滞留判定検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_kenshou_shounin()));
        // 対象先選定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_taishou_touroku()));
        // 対象先選定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_taishou_shounin()));
        // 一次査定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_touroku()));
        // 一次査定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_shounin()));
        // 一次査定検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_kenshou_touroku()));
        // 一次査定検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_kenshou_shounin()));
        // 二次査定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_ni_touroku()));
        // 二次査定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_ni_shounin()));
        // 引当金検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kenshou_touroku()));
        // 引当金検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kenshou_shounin()));
        // 引当金確認　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kakunin_touroku()));
        // 引当金確認  承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kakunin_shounin()));
        // クレーム債権　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_kure_touroku()));
        // クレーム債権　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_kure_shounin()));
        // 代行設定
        exCstmt.setStringIn(Function.convFlag(form.isDaikou_settei()));
        // 査定会社メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isSettei_mentenansu()));
        // 業務フローパターンメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isGyoumu_mentenansu()));
        // ユーザマスタメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isUser_mentenansu()));
        // 勘定科目マスタメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isKanjou_mentenansu()));
        // 抽出条件メンテナンス（本社）
        exCstmt.setStringIn(Function.convFlag(form.isHonsha_mentenansu()));
        // 抽出条件メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isJouken_mentenansu()));
        // チャンピオン部メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isChanpion_mentenansu()));
        // ゴルフ会員権メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isKaiin_mentenansu()));
        // 連結区分マスタupload
        exCstmt.setStringIn(Function.convFlag(form.isRenketu_upload()));
        // 滞留判定　差戻
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_sa()));
        // 査定完了　差戻
        exCstmt.setStringIn(Function.convFlag(form.isBl_satei()));
        // 二次査定区分
        exCstmt.setStringIn(form.getNi_satei());
        // システム管理者専用	
        exCstmt.setStringIn(Function.convFlag(form.isSystem_manager()));
        // ユーザ情報.代行ユーザIDがNULLの場合
        if (inChk.isNullBlank(user_bean.getComDaiko_userId())) {
        	exCstmt.setStringIn(user_bean.getComUserId());
        }else{	//ユーザ情報.代行ユーザIDがNOT NULLの場合 
        	exCstmt.setStringIn(user_bean.getComDaiko_userId());
        }
        
        try {
            // SQL実行
        	exCstmt.execute();
        	String msg = exCstmt.getOraMsg();
        	if(ORA_MSG.equals(msg.subSequence(0, 9))){
        		
        		return false;
        	}
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
        
        return true;

	}
	
	/**
	 * 
	 *  M18_実施業務マスタの登録<br>
	 * 
	 * @throws SQLException
	 */
	public void intM1800() throws SQLException {
		
        ResultSet rs = null;
		//0-1:フェーズ、2-3:ステータス
	    String[] str1 = {"1010","1030","2010",
	    				"2030","3010","3030",
	    				"4010","4030","5010",
	    				"5030","6010","6030",
	    				"7010","7030","8010",
	    				"8030","6510","6530",
			    		//対象先選定30登録時は仮基準査定追加90も登録する
	    				"9010","9030"};
	    
	    // セレクトボックス
	    String[] str2 = {form.getTairyu_touroku(),
			    		form.getTairyu_shounin(),
			    		form.getTairyu_kenshou_touroku(),
			    		form.getTairyu_kenshou_shounin(),
			    		form.getTaishou_touroku(),
			    		form.getTaishou_shounin(),
			    		form.getIti_touroku(),
			    		form.getIti_shounin(),
			    		form.getIti_kenshou_touroku(),
			    		form.getIti_kenshou_shounin(),
			    		form.getNi_touroku(),
			    		form.getNi_shounin(),
			    		form.getHikiate_kenshou_touroku(),
			    		"7030",
			    		form.getHikiate_kakunin_touroku(),
			    		"8030",
			    		form.getKure_touroku(),
			    		form.getKure_shounin(),
			    		//対象先選定30登録時は仮基準査定追加90も登録する
	    				"9030","4010"};
	    
	    // チェックボックス
	    String[] str3 = {Function.convFlag(form.isBl_tairyu_touroku()),
			    		Function.convFlag(form.isBl_tairyu_shounin()),
			    		Function.convFlag(form.isBl_tairyu_kenshou_touroku()),
			    		Function.convFlag(form.isBl_tairyu_kenshou_shounin()),
			    		Function.convFlag(form.isBl_taishou_touroku()),
			    		Function.convFlag(form.isBl_taishou_shounin()),
			    		Function.convFlag(form.isBl_iti_touroku()),
			    		Function.convFlag(form.isBl_iti_shounin()),
			    		Function.convFlag(form.isBl_iti_kenshou_touroku()),
			    		Function.convFlag(form.isBl_iti_kenshou_shounin()),
			    		Function.convFlag(form.isBl_ni_touroku()),
			    		Function.convFlag(form.isBl_ni_shounin()),
			    		Function.convFlag(form.isBl_hikiate_kenshou_touroku()),
			    		Function.convFlag(form.isBl_hikiate_kenshou_shounin()),
			    		Function.convFlag(form.isBl_hikiate_kakunin_touroku()),
			    		Function.convFlag(form.isBl_hikiate_kakunin_shounin()),
			    		Function.convFlag(form.isBl_kure_touroku()),
			    		Function.convFlag(form.isBl_kure_shounin()),
			    		//対象先選定30登録時は仮基準査定追加90も登録する
			    		Function.convFlag(form.isBl_taishou_touroku()),
			    		Function.convFlag(form.isBl_taishou_shounin())};

	    //システム区分GSSの場合は仮基準査定追加を登録しない
	    int hairetsulength = str1.length;
	    if("1".equals(form.getGamenFlg()) && GS.GSS.equals(form.getSystemkbn())){
	    	hairetsulength = hairetsulength - 2;
	    }else if("2".equals(form.getGamenFlg()) && GS.GSS.equals(workBean.getWorkflowSystemkbn())){
	    	hairetsulength = hairetsulength - 2;
	    }
	    
		for(int i = 0; i < hairetsulength; i++){
			// チェックボックスでチェックオンの場合
			if("1".equals(str3[i])){
				ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_INSERT_M1800, sqlExec);
				if("1".equals(form.getGamenFlg())){
					// システム区分
			        exCstmt.setStringIn(form.getSystemkbn());
			        //  汎用１
			        exCstmt.setStringIn(form.getHanyou1());
			        // パターンID
			        exCstmt.setStringIn(pattern_id);
				}else if("2".equals(form.getGamenFlg())){
					// システム区分
			        exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
			        //  汎用１
			        exCstmt.setStringIn(workBean.getBunrui1());
			        // パターンID
			        exCstmt.setStringIn(workBean.getWorkflowId());
				}

				// 実施フェーズ
				exCstmt.setStringIn(str1[i].substring(0, 2));
				// 開始ステータス
				exCstmt.setStringIn(str1[i].substring(2, 4));
				
				//実施フェーズ完了フラグ
				 if(true == Function.matches(str1[i].substring(0, 2),TAIRYU) 
						 && true == Function.matches(str2[i].substring(0, 2),SATEI)){
					 
					 exCstmt.setStringIn("1");
				}else if(true == Function.matches(str1[i].substring(0, 2),SATEI) && true == Function.matches(str2[i].substring(0, 2),HIKIATE)){
					
					exCstmt.setStringIn("1");
				}else if (true == Function.matches(str1[i].substring(0, 2),HIKIATE) && str1[i].substring(2, 4).equals("30")){
		
					exCstmt.setStringIn("1");
				}else if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(str1[i].substring(0, 2))
						&& true == Function.matches(str2[i].substring(0, 2),SATEI)){
					
					exCstmt.setStringIn("1");
				}else {
					
					exCstmt.setStringIn("0");
				}
				
				// 次実施フェーズ
					exCstmt.setStringIn(str2[i].substring(0, 2));
				
				// 次開始ステータス
				if (true == Function.matches(str1[i].substring(0, 2),HIKIATE) && str1[i].substring(2, 4).equals("30")) {

					//引当金確認承認、引当金検証承認の場合
					exCstmt.setStringIn("40");
				}else{

					exCstmt.setStringIn(str2[i].substring(2, 4));
				}
					
				// ユーザ情報.代行ユーザIDがNULLの場合
		        if (inChk.isNullBlank(user_bean.getComDaiko_userId())) {
		        	exCstmt.setStringIn(user_bean.getComUserId());
		        }else{	//ユーザ情報.代行ユーザIDがNOT NULLの場合 
		        	exCstmt.setStringIn(user_bean.getComDaiko_userId());
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
	}
	
	/**
	 * 
	 *  M18_実施業務マスタからフェーズ、ステータスの設定値を取得する<br>
	 *
	 */
	public List<Map<String,String>> selM1800() throws SQLException {
		
		List<Map<String,String>> resList = new ArrayList<Map<String,String>>(); 
		
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_M1800, sqlExec);
		// システム区分
        exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
        //  汎用１
        exCstmt.setStringIn(workBean.getBunrui1());
        // パターンID 
        exCstmt.setStringIn(workBean.getWorkflowId());
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            int i = 0;
            while (rs.next()) {
            	HashMap<String,String> map = new HashMap<String,String>();
            	// 現フェーズ
            	map.put(GEN_PHASE, rs.getString(GEN_PHASE));
            	// 次フェーズ
            	map.put(JI_PHASE, rs.getString(JI_PHASE));
            	resList.add(map);
            	i++;
            }
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
        
        return resList;
	}
	
	/**
	 * 
	 * 更新対象の業務フローパターンを保持しているユーザの滞留案件情報を取得する <br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int sel_ts() throws SQLException {
		
		int result = 0;
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_TSTATUS, sqlExec);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(workBean.getWorkflowId());
		exCstmt.setResultSet(RESULTSET);
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if (rs.next()) {
            	result = rs.getInt(CNT);
            }
            
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
	 * 更新対象の業務フローパターンを保持しているユーザの査定案件情報を取得する <br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int sel_ss() throws SQLException {
		
		int result = 0;
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_SSTATUS, sqlExec);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setStringIn(workBean.getWorkflowId());
		exCstmt.setResultSet(RESULTSET);
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if (rs.next()) {
            	result = rs.getInt(CNT);
            }
            
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
	 * M17_業務フローパターンマスタの更新 <br>
	 * 
	 * @throws SQLException
	 */
	public void upt_M17() throws SQLException {
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_UPDATE_M1700, sqlExec);
		// システム区分
		exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
		// 汎用１
		exCstmt.setStringIn(workBean.getBunrui1());
		exCstmt.setStringIn(workBean.getWorkflowId());		
        // 業務フローパターン名称-ja
        exCstmt.setStringIn(form.getWorkflow_nm_ja());
        // 業務フローパターン名称-en
        exCstmt.setStringIn(form.getWorkflow_nm_en());
        // 滞留判定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_touroku()));
        // 滞留判定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_shounin()));
        // 滞留判定検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_kenshou_touroku()));
        // 滞留判定検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_kenshou_shounin()));
        // 対象先選定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_taishou_touroku()));
        // 対象先選定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_taishou_shounin()));
        // 一次査定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_touroku()));
        // 一次査定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_shounin()));
        // 一次査定検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_kenshou_touroku()));
        // 一次査定検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_iti_kenshou_shounin()));
        // 二次査定　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_ni_touroku()));
        // 二次査定　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_ni_shounin()));
        // 引当金検証　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kenshou_touroku()));
        // 引当金検証　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kenshou_shounin()));
        // 引当金確認　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kakunin_touroku()));
        // 引当金確認  承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_hikiate_kakunin_shounin()));
        // クレーム債権　登録(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_kure_touroku()));
        // クレーム債権　承認(チェックボックス)
        exCstmt.setStringIn(Function.convFlag(form.isBl_kure_shounin()));
        // 代行設定
        exCstmt.setStringIn(Function.convFlag(form.isDaikou_settei()));
        // 査定会社メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isSettei_mentenansu()));
        // 業務フローパターンメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isGyoumu_mentenansu()));
        // ユーザマスタメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isUser_mentenansu()));
        // 勘定科目マスタメンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isKanjou_mentenansu()));
        // 抽出条件メンテナンス（本社）
        exCstmt.setStringIn(Function.convFlag(form.isHonsha_mentenansu()));
        // 抽出条件メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isJouken_mentenansu()));
        // チャンピオン部メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isChanpion_mentenansu()));
        // ゴルフ会員権メンテナンス
        exCstmt.setStringIn(Function.convFlag(form.isKaiin_mentenansu()));
        // 連結区分マスタupload
        exCstmt.setStringIn(Function.convFlag(form.isRenketu_upload()));
        // 滞留判定　差戻
        exCstmt.setStringIn(Function.convFlag(form.isBl_tairyu_sa()));
        // 査定完了　差戻
        exCstmt.setStringIn(Function.convFlag(form.isBl_satei()));
        // チェックオフにした場合
        if(form.isBl_ni_touroku() || form.isBl_ni_shounin()){
        	// 二次査定区分
            exCstmt.setStringIn(form.getNi_satei());
        }else{
        	// 二次査定区分
        	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        }
        
        // システム管理者専用	
        exCstmt.setStringIn(Function.convFlag(form.isSystem_manager()));
        // ユーザ情報.代行ユーザIDがNULLの場合
        if (inChk.isNullBlank(user_bean.getComDaiko_userId())) {
        	exCstmt.setStringIn(user_bean.getComUserId());
        }else{	//ユーザ情報.代行ユーザIDがNOT NULLの場合 
        	exCstmt.setStringIn(user_bean.getComDaiko_userId());
        }
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
	}
	
	/**
	 * 
	 * M18_実施業務マスタの削除 <br>
	 * 
	 * @throws SQLException
	 */
	public void del_M18() throws SQLException {
		
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_DELETE_M1800, sqlExec);
		// システム区分
		exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
		// 汎用１
		exCstmt.setStringIn(workBean.getBunrui1());
		exCstmt.setStringIn(workBean.getWorkflowId());
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
	}
	
	/**
	 * 
	 * 存在チェック <br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int sel_M24() throws SQLException {
		
		int result = 0;
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_M2400, sqlExec);
		// システム区分
		exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
		// 汎用１
		exCstmt.setStringIn(workBean.getBunrui1());
		exCstmt.setStringIn(workBean.getWorkflowId());
		exCstmt.setResultSet(RESULTSET);
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if (rs.next()) {
            	result = rs.getInt(CNT);
            }
            
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
	 * M17_業務フローパターンマスタ（SSM_WORKFLOW_PATTERN）の削除を行う <br>
	 * 
	 * @throws SQLException
	 */
	public void del_M17() throws SQLException {
		
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_DELETE_M1700, sqlExec);
		// システム区分
		exCstmt.setStringIn(workBean.getWorkflowSystemkbn());
		// 汎用１
		exCstmt.setStringIn(workBean.getBunrui1());
		exCstmt.setStringIn(workBean.getWorkflowId());
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
	}
	
	/**
	 * 
	 * 更新対象の業務フローパターンの未完了の滞留案件情報を取得する <br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int che_ts() throws SQLException {
		
		int result = 0;
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_TCOUNT, sqlExec);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(hanyo1);
		exCstmt.setResultSet(RESULTSET);
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if (rs.next()) {
            	result = rs.getInt(CNT);
            }
            
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
	 * 更新対象の業務フローパターンの未完了の査定案件情報を取得する <br>
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int che_st() throws SQLException {
		
		int result = 0;
        ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7105_SELECT_SCOUNT, sqlExec);
		exCstmt.setStringIn(systemKbn);
		exCstmt.setStringIn(hanyo1);
		exCstmt.setResultSet(RESULTSET);
		
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if (rs.next()) {
            	result = rs.getInt(CNT);
            }
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
		
		return result;
	}
	
}

