/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.dbAcc;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SyosaiForm;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* OB1103_実質滞留債権判定_明細詳細 DBアクセスクラス <br>
*/
public class SyosaiDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;				// ＡＰＰコンテキスト
    private SessionData cmnData = null;				// 機能共通セッション
    private UserBean user_bean = null;					// ユーザ情報
    private TorihikisakiBean tori_bean = null;			// 取引先情報
    private MeisaisyosaiBean syosai_bean = null;		// 明細情報
    private SyosaiForm form = null;					// アクションフォーム

    private static final String KOMOKU_TITLE			= "common_OB1103";			//区分キー（項目タイトル名）
    private static final String TAIRYU_JDG			= "tairyu_jdg";				//区分キー（滞留判定）

    //Resultset用文字列    
    private static final String ID	 				= "id"; 					// ID
    private static final String FILE_NM 				= "file_nm"; 				// ファイル名
	private static final String JITU_FILE_NM 			= "jitu_file_nm"; 			// 実ファイル名
	private static final String BUNSYO_NO 			= "bunsyo_no"; 				// 文書No.
    private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";         // 表示値
    private static final String KBN_VAL				= "kbn_val";                // 表示キー
    
    private static final String SP_SS_O_SELECT_TEMPU			= "SP_SS_O_SELECT_TEMPU";			//添付ファイル情報取得プロシージャ
    private static final String SP_SS_OB_UPDATE_T1000			= "SP_SS_OB_UPDATE_T1000";			//T10_滞留判定（SST_TAIRYUHANTEI）の更新プロシージャ
    private static final String SP_SS_O_DELETE_T1100			= "SP_SS_O_DELETE_T1100";			//T11_文書添付の削除プロシージャ
    
    // INパラメータ
    private String workflowSystemkbn;    // 業務フローパターンシステム区分

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public SyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        syosai_bean = cmnData.getSyosai_bean();
        form = (SyosaiForm)appContext.getActionForm();

        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
    }
    
    /**
     * 変数初期化 <br>
     */
    public void initialize() {
        // INパラメータ
        workflowSystemkbn = GS.EMPTY_CHARCTER;
    }

    /**
     * 汎用項目タイトル名【リスト】を取得 <br>
     * 
     * @exception SQLException
     */
    public void getKomokuTitles() throws SQLException {

    	ResultSet rs = null;
        try{
            //ResultSet取得
            rs = getKbnval(KOMOKU_TITLE, workflowSystemkbn, cmnData.getComLangMode());

            int i = 0;
            while ( rs.next() ) {
                i++;
            	switch (i) {
				case 1:
					form.setKomoku1(rs.getString(KBN_HYOUJI_VAL));
					break;
				case 2:
					form.setKomoku2(rs.getString(KBN_HYOUJI_VAL));
					break;
				case 3:
					form.setKomoku3(rs.getString(KBN_HYOUJI_VAL));
					break;
				case 4:
					form.setKomoku4(rs.getString(KBN_HYOUJI_VAL));
					break;
				case 5:
					form.setKomoku5(rs.getString(KBN_HYOUJI_VAL));
					break;
				default:
					break;
				}
            }
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * 滞留判定セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getTairyuJdg() throws SQLException {

    	ResultSet rs = null;
        try{
            //ResultSet取得
            rs = getKbnval(TAIRYU_JDG, workflowSystemkbn, cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_tairyu_jdg = new LinkedHashMap<String,String>();
            while ( rs.next() ) {
                ar_tairyu_jdg.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                    
            }
            form.setAr_tairyu_jdg(ar_tairyu_jdg);            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }

    /**
     * 添付ファイル情報の取得 <br>
     * 
     * @exception SQLException
     */
    public void getTenpu() throws SQLException {
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_TEMPU, sqlExec);
        cstmt.setStringIn(tori_bean.getAnken_no());
        cstmt.setStringIn(syosai_bean.getAnken_no_eda());
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<Map> ar_tenpu = new ArrayList<Map>();   // 添付ファイル配列
            int i = 0;
            while ( rs.next() ) {
                
                // 明細情報
            	Map<String, String> listBean = new HashMap<String, String>();
                
                // id
                listBean.put(ID, Function.getStringOfInt(i));
                
                // 文書No.
                listBean.put(BUNSYO_NO, rs.getString(BUNSYO_NO));
                
                // 実ファイル名
                listBean.put(JITU_FILE_NM, rs.getString(JITU_FILE_NM));
                
                // ファイル名
                listBean.put(FILE_NM, rs.getString(FILE_NM));

                // 明細配列に取得情報を格納
                ar_tenpu.add(i, listBean);
                i++;   
            }
            
            // 解除フラグ
            form.setAr_kaijyo_chk(new String[0]);
                
            // ActionForm に明細を格納
            form.setAr_tenpu(ar_tenpu);    
        
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
	 * T10_滞留判定の更新 <br>
     * 
     * @exception SQLException
	 */
	public void updT10TairyuHantei() throws SQLException {
        // 滞留判定
        String tairyu_hantei = syosai_bean.getTairyu_hantei();
        // 判定事由
        String hantei_jiyu = syosai_bean.getHantei_jiyu();
        // ユーザID
        String userId = user_bean.getComUserId();
        // 代行ユーザID
        String daikoUserId = user_bean.getComDaiko_userId();
        // 滞留判定案件No
        String anken_no = tori_bean.getAnken_no();
        // 滞留判定案件No枝番
        String anken_no_eda = syosai_bean.getAnken_no_eda();
        // フェーズ
        String phase = tori_bean.getPhase();
        
        // T10_滞留判定の更新
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T1000, sqlExec);
        // 滞留判定
        cstmt.setStringIn(tairyu_hantei);
        // 判定事由
        cstmt.setStringIn(hantei_jiyu);
        // ユーザID
        cstmt.setStringIn(userId);
        // 代行ユーザID
        cstmt.setStringIn(daikoUserId);
        // 滞留判定案件No
        cstmt.setStringIn(anken_no);
        // 滞留判定案件No枝番
        cstmt.setStringIn(anken_no_eda);
        // フェーズ
        cstmt.setStringIn(phase);
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
	 * T11_文書添付の削除 <br>
     * 
     * @exception SQLException
	 */
	public void delT11BunsyoTenpu() throws SQLException {
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
        // 案件No.
        String ankenNo = tori_bean.getAnken_no();
        // 添付ファイルの配列
        List<Map> ar_tenpu = form.getAr_tenpu();
        // 選択された添付情報IDのリスト
        String[] ar_kaijyo_chk = form.getAr_kaijyo_chk();
    	// 添付解除チェックボックスにチェックがある場合、T11_文書添付から削除する
        for (int i = 0; i < ar_kaijyo_chk.length; i++) {
        	// 選択された添付情報を取得する。
        	Map tenpu = ar_tenpu.get(Integer.parseInt(ar_kaijyo_chk[i]));
            // 文書No
            String busyoNo = (String)tenpu.get(BUNSYO_NO);
            // T11_文書添付の削除
            cstmt = new ExCallableStatement(SP_SS_O_DELETE_T1100, sqlExec);
            // 案件No.
            cstmt.setStringIn(ankenNo);
            // 文書No
            cstmt.setStringIn(busyoNo);

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
}