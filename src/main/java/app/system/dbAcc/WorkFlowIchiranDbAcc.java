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
import app.system.form.WorkFlowIchiranForm;
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
 *  OS7104_業務フローパターンメンテナンス_一覧 DBアクセスクラス<BR>
 */
public class WorkFlowIchiranDbAcc extends CommonDbAcc {
    private SessionData cmnData = null;                 // 機能共通セッション
    private UserBean user_bean = null;                  // ユーザ情報
    private WorkFlowIchiranForm form = null;                    // アクションフォーム
    private AppContext appContext = null;               // ＡＰＰコンテキスト

    //Resultset用文字列  
    private static final String KBN_HYOUJI_VAL            = "kbn_hyouji_val";
    private static final String KBN_VAL                   = "kbn_val";
    private static final String PATTERN_ID                = "pattern_id";
    private static final String PATTERN_ID_HYOJI          = "pattern_id_hyoji";
    private static final String PATTERN_NAME_JP           = "pattern_name_jp";
    private static final String PATTERN_NAME_EN           = "pattern_name_en";
    private static final String SYSTEM_KBN_NM             = "system_kbn_nm";           	
    private static final String SATEIKAISYA_CD            = "sateikaisya_cd";				// 汎用1
    private static final String HANYO_1                   = "hanyo1";              		//汎用1セレクトボックス
    private static final String SHOW                      = "show";                 		//区分キー（表示件数)
    private static final String SYSTEM_KBN                = "system_kbn";					//システム区分キー
    private static final String TAIRYU_HANTEI_T_FLG       = "tairyu_hantei_t_flg";		// 実質滞留判定登録フラグ
    private static final String TAIRYU_HANTEI_S_FLG       = "tairyu_hantei_s_flg";		// 実質滞留判定承認フラグ
    private static final String TAIRYU_KENSHO_T_FLG       = "tairyu_kensho_t_flg";		// 実質滞留検証登録フラグ
    private static final String TAIRYU_KENSHO_S_FLG       = "tairyu_kensho_s_flg";		// 実質滞留検証承認フラグ
    private static final String TAISHOSAKI_SENTEI_T_FLG   = "taishosaki_sentei_t_flg";	// 対象先選定登録フラグ
    private static final String TAISHOSAKI_SENTEI_S_FLG   = "taishosaki_sentei_s_flg";	// 対象先選定承認フラグ
    private static final String ICHIJI_SATEI_TOROKU_T_FLG = "ichiji_satei_toroku_t_flg";	// 一次査定登録フラグ
    private static final String ICHIJI_SATEI_TOROKU_S_FLG = "ichiji_satei_toroku_s_flg";	// 一次査定承認フラグ
    private static final String ICHIJI_SATEI_KENSHO_T_FLG = "ichiji_satei_kensho_t_flg";	// 一次査定検証登録フラグ
    private static final String ICHIJI_SATEI_KENSHO_S_FLG = "ichiji_satei_kensho_s_flg";	// 一次査定検証承認フラグ
    private static final String NIJI_SATEI_T_FLG          = "niji_satei_t_flg";			// 二次査定登録フラグ
    private static final String NIJI_SATEI_S_FLG          = "niji_satei_s_flg";			// 二次査定承認フラグ
    private static final String HIKIATEKIN_KENSYO_T_FLG   = "hikiatekin_kensyo_t_flg";	// 引当金検証登録フラグ
    private static final String HIKIATEKIN_KENSYO_S_FLG   = "hikiatekin_kensyo_s_flg";	// 引当金検証承認フラグ
    private static final String HIKIATEKIN_KAKUNIN_T_FLG  = "hikiatekin_kakunin_t_flg";	// 引当金確認登録フラグ
    private static final String HIKIATEKIN_KAKUNIN_S_FLG  = "hikiatekin_kakunin_s_flg";	// 引当金確認承認フラグ
    private static final String CLAIM_RESET_T_FLG         = "claim_reset_t_flg";			// クレーム債権再設定登録フラグ
    private static final String CLAIM_RESET_S_FLG         = "claim_reset_s_flg";			// クレーム債権再設定承認フラグ
    private static final String DAIKOSET_FLG              = "daikoset_flg";				// 代行設定フラグ
    private static final String SATEIKAISYA_FLG           = "sateikaisya_flg";			// 査定会社メンテナンスフラグ
    private static final String WORKFLOW_PATTERN_FLG      = "workflow_pattern_flg";		// 業務フローパターンメンテナンスフラグ
    private static final String USER_MASTER_FLG           = "user_master_flg";			// ユーザマスタメンテナンスフラグ
    private static final String KANJO_MASTER_FLG          = "kanjo_master_flg";			// 勘定科目マスタメンテナンスフラグ
    private static final String JOKEN_MASTER_HQ_FLG       = "joken_master_hq_flg";		// 抽出条件マスタメンテナンス（本社）フラグ
    private static final String JOKEN_MASTER_FLG          = "joken_master_flg";			// 抽出条件マスタメンテナンスフラグ
    private static final String CHAMPION_BU_FLG           = "champion_bu_flg";			// チャンピオン部メンテナンスフラグ
    private static final String GOLF_KAIINKEN_FLG         = "golf_kaiinken_flg";			// ゴルフ会員権メンテナンスフラグ
    private static final String RENKETSU_UPLOAD_FLG       = "renketsu_upload_flg";		// 連結区分マスタUPLOADフラグ
    private static final String JIMUKYOKU_SASHI_FLG       = "jimukyoku_sashi_flg";		// 事務局経由差戻フラグ
    private static final String SATEI_KANRYO_SASHI_FLG    = "satei_kanryo_sashi_flg";		// 査定完了後差戻フラグ
    private static final String NIJI_SATEI_KBN            = "niji_satei_kbn";				// ２次査定区分
    private static final String ADMIN_SENYO_FLG           = "admin_senyo_flg";			// システム管理者専用フラグ

    
    // システムセレクトボックスの設定値を取得する
    private static final String SP_SS_O_SELECT_P0202 = "SP_SS_O_SELECT_P0202";
    private static final String SP_SS_OS7104_SELECT_M1700 = "SP_SS_OS7104_SELECT_M1700";
    
    
    // INパラメータ
    private String systemKbn;              // ユーザＩＤ
    private String hanyo1;              // ユーザNM
    private String workFlowNm;
    private String workflowSystemkbn;   // 業務フローパターンシステム区分
    
    /**
     * コンストラクタ
     * 
     * @param SqlExecuter
     * @param Log
     * @param AppContext
     */
    public WorkFlowIchiranDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (WorkFlowIchiranForm)appContext.getActionForm();
        
        systemKbn = form.getSystemkbn();
        hanyo1 = form.getHanyou1();
        workFlowNm = form.getWorkflow_nm();

        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
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
     * 表示件数セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getShow() throws SQLException {

        ResultSet rs = null;

        try{
            //ResultSet取得
            rs = getKbnval(SHOW,workflowSystemkbn,cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
                ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
                i++;
            }
            form.setAr_show(ar_show);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 
     *  一覧情報の取得<br>
     * 
     * @throws SQLException
     */
    public void getItiran() throws SQLException {
    	
    	ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7104_SELECT_M1700, sqlExec);
        // システム区分
        exCstmt.setStringIn(systemKbn);
        // 汎用1
        exCstmt.setStringIn(hanyo1);
        // パターン名称
        exCstmt.setStringIn(Function.addSingleQuotation(workFlowNm));
        // 言語モード
        exCstmt.setStringIn(cmnData.getComLangMode());   
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<WorkFlowBean> ar_meisai = new ArrayList<WorkFlowBean>();   // 明細配列

            int i = 0;
            while ( rs.next() ) {

                WorkFlowBean listBean = new WorkFlowBean();
                //id
                listBean.setId(Function.getStringOfInt(i));
                // パターンID
                listBean.setWorkflowId(rs.getString(PATTERN_ID));
                // 表示用パターンID 
                listBean.setDisplay_workflowId(rs.getString(PATTERN_ID_HYOJI));
                // システム区分
                listBean.setWorkflowSystemkbn(rs.getString(SYSTEM_KBN));
                // システム区分名称
                listBean.setWorkflowSystemkbn_nm(rs.getString(SYSTEM_KBN_NM));
                // 分類１(査定会社コード)
                listBean.setBunrui1(rs.getString(SATEIKAISYA_CD));
                // 業務フローパターン名称(日本語)
                listBean.setWorkflow_nm_ja(rs.getString(PATTERN_NAME_JP));
                // 業務フローパターン名称(英語)
                listBean.setWorkflow_nm_en(rs.getString(PATTERN_NAME_EN));
                // 実質滞留判定登録フラグ
                listBean.setTairyu_hantei_t_flg(rs.getString(TAIRYU_HANTEI_T_FLG));
                // 実質滞留判定承認フラグ
                listBean.setTairyu_hantei_s_flg(rs.getString(TAIRYU_HANTEI_S_FLG));
                // 実質滞留検証登録フラグ
                listBean.setTairyu_kensho_t_flg(rs.getString(TAIRYU_KENSHO_T_FLG));
                // 実質滞留検証承認フラグ
                listBean.setTairyu_kensho_s_flg(rs.getString(TAIRYU_KENSHO_S_FLG));
                // 対象先選定登録フラグ
                listBean.setTaishosaki_sentei_t_flg(rs.getString(TAISHOSAKI_SENTEI_T_FLG));
                // 対象先選定承認フラグ
                listBean.setTaishosaki_sentei_s_flg(rs.getString(TAISHOSAKI_SENTEI_S_FLG));
                // 一次査定登録フラグ
                listBean.setIchiji_satei_t_flg(rs.getString(ICHIJI_SATEI_TOROKU_T_FLG));
                // 一次査定承認フラグ
                listBean.setIchiji_satei_s_flg(rs.getString(ICHIJI_SATEI_TOROKU_S_FLG));
                // 一次査定検証登録フラグ
                listBean.setIchiji_sateikensyo_t_flg(rs.getString(ICHIJI_SATEI_KENSHO_T_FLG));
                // 一次査定検証承認フラグ
                listBean.setIchiji_sateikensyo_s_flg(rs.getString(ICHIJI_SATEI_KENSHO_S_FLG));
                // 二次査定登録フラグ
                listBean.setNiji_satei_t_flg(rs.getString(NIJI_SATEI_T_FLG));
                // 二次査定承認フラグ
                listBean.setNiji_satei_s_flg(rs.getString(NIJI_SATEI_S_FLG));
                // 引当金検証登録フラグ
                listBean.setHikiatekin_kensyo_t_flg(rs.getString(HIKIATEKIN_KENSYO_T_FLG));
                // 引当金検証承認フラグ
                listBean.setHikiatekin_kensyo_s_flg(rs.getString(HIKIATEKIN_KENSYO_S_FLG));
                // 引当金確認登録フラグ
                listBean.setHikiatekin_kakunin_t_flg(rs.getString(HIKIATEKIN_KAKUNIN_T_FLG));
                // 引当金確認承認フラグ
                listBean.setHikiatekin_kakunin_s_flg(rs.getString(HIKIATEKIN_KAKUNIN_S_FLG));
                // クレーム債権再設定登録フラグ
                listBean.setClaim_reset_t_flg(rs.getString(CLAIM_RESET_T_FLG));
                // クレーム債権再設定承認フラグ
                listBean.setClaim_reset_s_flg(rs.getString(CLAIM_RESET_S_FLG));
                // 代行設定フラグ
                listBean.setDaikoset_flg(rs.getString(DAIKOSET_FLG));
                // 査定会社メンテナンスフラグ
                listBean.setSateikasya_flg(rs.getString(SATEIKAISYA_FLG));
                // 業務フローパターンメンテナンスフラグ
                listBean.setWorkflow_pattern_flg(rs.getString(WORKFLOW_PATTERN_FLG));
                // ユーザマスタメンテナンスフラグ
                listBean.setUser_master_flg(rs.getString(USER_MASTER_FLG));
                // 勘定科目マスタメンテナンスフラグ
                listBean.setKanjo_master_flg(rs.getString(KANJO_MASTER_FLG));
                // 抽出条件マスタメンテナンス（国内）フラグ
                listBean.setJoken_master_hq_flg(rs.getString(JOKEN_MASTER_HQ_FLG));
                // 抽出条件マスタメンテナンスフラグ
                listBean.setJoken_master_flg(rs.getString(JOKEN_MASTER_FLG));
                // チャンピオン部メンテナンスフラグ
                listBean.setChampion_bu_flg(rs.getString(CHAMPION_BU_FLG));
                // ゴルフ会員権メンテナンスフラグ
                listBean.setGolf_kaiinken_flg(rs.getString(GOLF_KAIINKEN_FLG));
                // 連結区分マスタUPLOADフラグ
                listBean.setRenketsu_upload_flg(rs.getString(RENKETSU_UPLOAD_FLG));
                // 事務局経由差戻フラグ
                listBean.setJimukyoku_sashi_flg(rs.getString(JIMUKYOKU_SASHI_FLG));
                // 査定完了後差戻フラグ
                listBean.setSatei_kanryo_sashi_flg(rs.getString(SATEI_KANRYO_SASHI_FLG));
                // 二次査定区分
                listBean.setNiji_satei_kbn(rs.getString(NIJI_SATEI_KBN));
                // システム管理者専用フラグ
                listBean.setadmin_senyo_flg(rs.getString(ADMIN_SENYO_FLG));

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
    
}
