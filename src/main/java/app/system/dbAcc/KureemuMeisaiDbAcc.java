/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/27		SSC				課題No.169 滞留判定取得 
003		2009/12/17		SSC				課題No.209 承認プルダウン修正
004		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
005		2016/03/18		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.system.dbAcc;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.form.KureemuMeisaiForm;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
* OS3102 クレーム債権再設定_明細一覧 DBアクセスクラス
*/
public class KureemuMeisaiDbAcc extends CommonDbAcc {

	private UserBean user_bean = null;              // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    private KureemuMeisaiForm form = null;                 // アクションフォーム
    
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.##";      // 数字のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
	
    private static final String SHOW				= "show";			// 区分キー（表示件数）
    private static final String HS_KBN_HANTEI		= "2";				// 判定査定区分 '2'： 一次・二次査定
	private static final String FLG_HAISIN_N		= "N";				// 配信済みフラグ
    private static final String TORIMODOSHI_FUKA	= "1";				// 取戻不可フラグ '1'：取戻不可
    private static final String TORIMODOSHI_KA	= "0";				// 取戻不可フラグ '0'：取戻可
    private static final String ANKEN_NO			= "anken_no";		// T10_滞留判定登録した案件noキー
    
    //Resultset用文字列    
    private static final String HANTEI_JIYUU			= "hantei_jiyuu";           // 判定事由
    private static final String TAIRYU_HANTEI			= "tairyu_hantei";          // 滞留判定
	//課題No.169
	//追加開始
    private static final String TAIRYUHANTEI			= "tairyuHantei";	        // 滞留判定
	//追加完了
    private static final String TAIRYU_KBN_CD			= "tairyu_kbn_cd";			// 滞留区分コード
    private static final String TAIRYU_KBN			= "tairyu_kbn";             // 滞留区分
    private static final String KANJO_NM				= "kanjo_nm";               // 勘定科目
    private static final String KANJO_CD				= "kanjo_cd";               // 勘定科目CD
    private static final String JIMUSHO_CD			= "jimusho_cd";             // 事務所コード
    private static final String KOMOKU5				= "komoku5";                // 項目５
    private static final String KOMOKU4				= "komoku4";                // 項目４
    private static final String KOMOKU3				= "komoku3";                // 項目３
    private static final String KOMOKU2				= "komoku2";                // 項目２
    private static final String KOMOKU1				= "komoku1";                // 項目１
    private static final String INVOICE_NO			= "invoice_no";             // インボイスNo（*1）
    private static final String KEIYAKU_DENPYO_NO		= "keiyaku_denpyo_no";      // 契約伝票No.（*1）
    private static final String TUUKA_CD				= "tuuka_cd";               // 通貨コード 
    private static final String KINGAKU				= "kingaku";       			// 金額計
    private static final String SYORI_DT				= "syori_dt";               // 勘定処理日（*1）
    private static final String MANKI_DT				= "manki_dt";               // 満期日（*1）
    private static final String SHUSI_DT				= "shusi_dt";               // 収支予定日（*1）
    private static final String CELL					= "cell";                   // セル名称
    private static final String SATEI_ANKEN_NO_EDA	= "satei_anken_no_eda";     // 案件No.枝番
    private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";         // 表示値
    private static final String KBN_VAL				= "kbn_val";                // 表示キー
    private static final String TANTO_NAME			= "tanto_name";             // 担当者名
    private static final String TOGO_ID				= "togo_id";                // 統合ID
   
    private static final String SP_SS_O_SELECT_SHONINSHA              = "SP_SS_O_SELECT_SHONINSHA";               // 承認担当者セレクトボックスの設定値取得プロシージャ
    private static final String SP_SS_OS_SELECT_TAIRYU             	= "SP_SS_OS_SELECT_TAIRYU";              	// 前回実施案件No.取得プロシージャ
    private static final String SP_SS_OS3102_SELECT_ICHIRAN           = "SP_SS_OS3102_SELECT_ICHIRAN";            // 明細一覧情報取得プロシージャ
    private static final String SP_SS_O_UPDATE_T1400                  = "SP_SS_O_UPDATE_T1400";                   // T14_査定進捗管理（SST_SATEI_STAT）を更新するプロシージャ
    private static final String SP_SS_O_INSERT_T1300                  = "SP_SS_O_INSERT_T1300";                   // T13_入力履歴（SST_NYURYOKU_HIST）の登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400                  = "SP_SS_O_INSERT_T0400";                   // T04_メール配信の登録プロシージャ
    private static final String SP_SS_OS3102_INSERT_T1000             = "SP_SS_OS3102_INSERT_T1000";              // T10_滞留判定の登録をするプロシージャ    
    private static final String SP_SS_OS3102_UPDATE_T1600             = "SP_SS_OS3102_UPDATE_T1600";              // T16_引当金検討対象BS明細を更新するプロシージャ
    
    private String workflowSystemkbn;   // 業務フローパターンシステム区分
    private String comLangMode;         // 共)言語モード
    private String systemKbn;           // 共)取引先情報.システム区分
    private String ankenNo;             // 共)取引先情報.案件No.


    /**
     * コンストラクタ
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public KureemuMeisaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);

        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        form = (KureemuMeisaiForm)appcontext.getActionForm();

        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
        systemKbn = tori_bean.getSystem_kbn();
        ankenNo = tori_bean.getAnken_no();
    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        systemKbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
        ankenNo = GS.EMPTY_CHARCTER;
    }

    /**
     * 承認担当者セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getTanto() throws SQLException {
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_SHONINSHA, sqlExec);
        cstmt.setStringIn(comLangMode);
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());
        cstmt.setStringIn(tori_bean.getBunrui2());
        cstmt.setStringIn(tori_bean.getBu_cd());
        cstmt.setStringIn(tori_bean.getTaisyo_ym());
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
            form.setAr_tanto(ar_tanto);
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
     * 表示件数セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getShow() throws SQLException {

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        try {
            // ResultSet取得
            rs = getKbnval(SHOW, workflowSystemkbn, comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String, String> ar_show = new LinkedHashMap<String, String>();
            while (rs.next()) {
                ar_show.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));
            }
            form.setAr_show(ar_show);
        } finally {
            if (rs != null) {
                try {
                    // Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
    }

    /**
     * 滞留判定データ作成済チェック <br>
     * 
     * @return 前回実施案件No.
     * @exception SQLException
     */
    public boolean getAnkenNo() throws SQLException {
    	
    	boolean result = false;
    	//ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS_SELECT_TAIRYU, sqlExec);
        cstmt.setStringIn(tori_bean.getAnken_no());
        cstmt.setResultSet(RESULTSET);

        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);  

            if(rs.next()) {
            	//作成済
                return result; 
            }
            result = true;
            return result;
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
     * 一覧情報取得処理 <br>
     * 
     * @throws SQLException
     */
    public void getMeisaiList() throws SQLException {
    	
        // 入力項目チェック共通クラス
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS3102_SELECT_ICHIRAN, sqlExec);
        cstmt.setStringIn(comLangMode);
        cstmt.setStringIn(tori_bean.getKijunbi_kbn());
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());
        cstmt.setStringIn(tori_bean.getInit_bunrui2());
        cstmt.setStringIn(tori_bean.getInit_bu_cd());
        cstmt.setStringIn(ankenNo);
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<MeisaisyosaiBean> ar_meisai = new ArrayList<MeisaisyosaiBean>();
            int i = 0;
            while ( rs.next() ) {
            	// 金額計（通貨）タイトル
            	if (i == 0) {
            		form.setTuuka_cd(rs.getString(TUUKA_CD));
            	}
                
                // 明細情報
                MeisaisyosaiBean listBean = new MeisaisyosaiBean();
                
                // id
                listBean.setId(Function.getStringOfInt(i));
                
                // 案件No.枝番
                listBean.setAnken_no_eda(rs.getString(SATEI_ANKEN_NO_EDA));

                // セル名称
                listBean.setCell_nm(rs.getString(CELL));

                // 収支予定日
                listBean.setSyusi_yoteibi(rs.getString(SHUSI_DT));
                
                // 満期日
                listBean.setMankibi(rs.getString(MANKI_DT));

                // 勘定処理日
                listBean.setKanjo_syoribi(rs.getString(SYORI_DT));

                // 金額計
                listBean.setKingaku_kei(formatKingaku(rs.getDouble(KINGAKU), systemKbn));

                // 通貨コード
                listBean.setTuuka_cd(rs.getString(TUUKA_CD));

                // 契約伝票No.
                listBean.setKeiyaku_denpyo_no(Function.trim(rs.getString(KEIYAKU_DENPYO_NO)));

                // インボイスNo
              	listBean.setInvoice_no(rs.getString(INVOICE_NO));

                // 項目１
                listBean.setKomoku1(rs.getString(KOMOKU1));

                // 項目２
                listBean.setKomoku2(rs.getString(KOMOKU2));

                // 項目３
                listBean.setKomoku3(rs.getString(KOMOKU3));

                // 項目４
                listBean.setKomoku4(rs.getString(KOMOKU4));

                // 項目５
                listBean.setKomoku5(rs.getString(KOMOKU5));

                // 事務所コード
                listBean.setJimusyo_cd(rs.getString(JIMUSHO_CD));

                // 勘定科目CD
                listBean.setKanjo_kamoku_cd(rs.getString(KANJO_CD));

                // 勘定科目
                listBean.setKanjo_kamoku_nm(rs.getString(KANJO_NM));

                // 滞留区分
                listBean.setTairyu_kbn(rs.getString(TAIRYU_KBN));

                // 滞留判定
                String tairyu_hantei = rs.getString(TAIRYU_HANTEI);

                // 判定事由
                String hantei_jiyuu = rs.getString(HANTEI_JIYUU);
                
                // 滞留判定
                listBean.setTairyu_hantei(tairyu_hantei);

            	//課題No.169
            	//追加開始
                // 滞留判定
                listBean.setTairyuHantei(Function.trim(rs.getString(TAIRYUHANTEI)));
            	//追加完了

                // 判定事由
                listBean.setHantei_jiyu(hantei_jiyuu);

                // 滞留区分コード
                listBean.setTairyu_kbn_cd(rs.getString(TAIRYU_KBN_CD));
                
                // 明細配列に取得情報を格納
                ar_meisai.add(i, listBean);
                i++;   
            }
                
            // ActionForm に明細を格納
            form.setAr_meisai(ar_meisai);    
            // ページ設定
            form.setPager(ar_meisai);
        
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
     * T14_査定進捗管理の更新（もぎ取り解除時） <br>
     * 
     * @exception SQLException
     */
    public void setUpdateT1400(String syori_flg) throws SQLException {

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
        cstmt.setStringIn(ankenNo);
        if ("0".equals(syori_flg)) {
        	 cstmt.setStringIn(tori_bean.getPhase());
        	 cstmt.setStringIn(GS.STATUS_MISYORI);
             cstmt.setStringIn(null);
             cstmt.setStringIn(null); // 査定登録画面
             cstmt.setStringIn(tori_bean.getSasi_ten_flg());
        } else {        	
        	cstmt.setStringIn(form.getJi_jishi_phase());
        	cstmt.setStringIn(form.getJi_kaishi_status());
        	cstmt.setStringIn(form.getSyonin_tanto());
            cstmt.setStringIn(null); // 査定登録画面
        	cstmt.setStringIn(null);
        }
        cstmt.setStringIn(null); // 代行ユーザID
        if (user_bean.getComDaiko_userId() == null) {
            cstmt.setStringIn(user_bean.getComUserId());
        } else {
            cstmt.setStringIn(user_bean.getComDaiko_userId());
        }
        if ("0".equals(syori_flg)) {
            cstmt.setStringIn(TORIMODOSHI_FUKA);
        } else {
            cstmt.setStringIn(TORIMODOSHI_KA);
        }
        
        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
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
     * T13_入力履歴（SST_NYURYOKU_HIST）の登録 <br>
     * 
     * @param insertKbn 入力区分
     * @exception SQLException
     */
    public void setInsertT1300(String insertKbn) throws SQLException {
    	
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
        cstmt.setStringIn(ankenNo);
        cstmt.setStringIn(HS_KBN_HANTEI);
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());
        cstmt.setStringIn(user_bean.getComUserId());
        cstmt.setStringIn(user_bean.getComUser_Nm());
        cstmt.setStringIn(user_bean.getComUser_Nm_En());
        cstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
        cstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());
        cstmt.setStringIn(tori_bean.getPhase());
        cstmt.setStringIn(insertKbn);
        cstmt.setStringIn(user_bean.getComUserId());        
        if (user_bean.getComDaiko_userId() == null) {       
            cstmt.setStringIn(null);
            cstmt.setStringIn(null);
            cstmt.setStringIn(null);
        } else {
            cstmt.setStringIn(user_bean.getComDaiko_userId());
            cstmt.setStringIn(user_bean.getComDaiko_user_nm());
            cstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
        }
        cstmt.setStringIn(null);
        cstmt.setStringIn(null);
        cstmt.setStringIn(null);
        cstmt.setStringIn(null);
        cstmt.setStringIn(form.getSyonin_tanto());
        
        try {
        	
            //SQL実行
            cstmt.execute();
            isError(cstmt);
            
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
     * T04_メール配信の登録 <br>
     * 
     * @exception SQLException
     */
    public void setInsertT0400() throws SQLException {
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
        // ユーザID
        cstmt.setStringIn(user_bean.getComUserId());
        // 案件No
        cstmt.setStringIn(tori_bean.getAnken_no());
        // 対象年月
        cstmt.setStringIn(tori_bean.getTaisyo_ym());
        // 査定会社コード
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());
        // 分類２
        cstmt.setStringIn(tori_bean.getBunrui2());
        // 部コード(システム区分01の場合のみ設定)
        if(GS.GSS.equals(tori_bean.getSystem_kbn())){
            cstmt.setStringIn(tori_bean.getBu_cd());
        } else {
        	cstmt.setStringIn(GS.EMPTY_CHARCTER);
        }
        // 次実施フェーズ
        cstmt.setStringIn(form.getJi_jishi_phase());
        // 次開始ステータス
        cstmt.setStringIn(form.getJi_kaishi_status());
        // 配信先担当
        cstmt.setStringIn(form.getSyonin_tanto());
        // 配信済みフラグ
        cstmt.setStringIn(FLG_HAISIN_N);
        // 画面ID
        cstmt.setStringIn(form.toString());
        // ユーザID
        if (user_bean.getComDaiko_userId() == null) {
            cstmt.setStringIn(user_bean.getComUserId());
        } else {
            cstmt.setStringIn(user_bean.getComDaiko_userId());
        }
        
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
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
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    private String formatKingaku(double kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI, kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI, kingaku);
        }
        return formatKingaku;
    }
    
    /**
     * T10_滞留判定の登録をする <br>
     * 
     * @exception SQLException
     */
    public void setInsertT1000(MeisaisyosaiBean meisaiBean) throws SQLException {
    		
    		ExCallableStatement cstmt = null;
    		ResultSet rs = null;
            cstmt = new ExCallableStatement(SP_SS_OS3102_INSERT_T1000, sqlExec);
            cstmt.setStringIn(user_bean.getComUserId());
            cstmt.setStringIn(user_bean.getComDaiko_userId());
            cstmt.setStringIn(systemKbn);
            cstmt.setStringIn(tori_bean.getSatei_ki());
            cstmt.setIntIn(Integer.parseInt(meisaiBean.getId()));
            cstmt.setStringIn(tori_bean.getPhase());
            cstmt.setStringIn(meisaiBean.getTairyu_kbn_cd());
            cstmt.setStringIn(form.getToroku_anken_no());
            cstmt.setStringOut(ANKEN_NO);
            
            try {
                //SQL実行
                cstmt.execute();
                isError(cstmt);
                
                if (GS.EMPTY_CHARCTER.equals(form.getToroku_anken_no())) {
                    form.setToroku_anken_no(cstmt.getString(ANKEN_NO));
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
                        
    	}
    
    /**
     * T16_引当金検討対象BS明細の更新 <br>
     * 
     * @exception SQLException
     */
    public void setUpdateT1600(MeisaisyosaiBean meisaiBean) throws SQLException {
    		
			ExCallableStatement cstmt = null;
			ResultSet rs = null;
            cstmt = new ExCallableStatement(SP_SS_OS3102_UPDATE_T1600, sqlExec);
            cstmt.setStringIn(user_bean.getComUserId());
            cstmt.setStringIn(user_bean.getComDaiko_userId());
            cstmt.setStringIn(form.getToroku_anken_no());
            cstmt.setIntIn(Integer.parseInt(meisaiBean.getId()));
            cstmt.setStringIn(tori_bean.getAnken_no());
            cstmt.setStringIn(meisaiBean.getAnken_no_eda());
            
            try {
                //SQL実行
                cstmt.execute();
                isError(cstmt);
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