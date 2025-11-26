/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.form.KureemuSyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* OS3103_クレーム債権_明細詳細 DBアクセスクラス <br>
*/
public class KureemuSyosaiDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;				// ＡＰＰコンテキスト
    private SessionData cmnData = null;				// 機能共通セッション
    private UserBean user_bean = null;					// ユーザ情報
    private TorihikisakiBean tori_bean = null;			// 取引先情報
    private MeisaisyosaiBean syosai_bean = null;		// 明細情報
    private KureemuSyosaiForm form = null;				// アクションフォーム

    private static final String KOMOKU_TITLE						= "common_OS3103";					//区分キー（項目タイトル名）

    //Resultset用文字列    
    private static final String TAIRYU_HANTEI_OVER4     			= "4";                  			// 滞留判定 「クレーム債権」
    private static final String KBN_HYOUJI_VAL					= "kbn_hyouji_val";         		// 表示値
    private static final String ANKEN_NO							= "anken_no";                		// 案件Noキー
    
    private static final String SP_SS_OS3103_UPDATE_T1000			= "SP_SS_OS3103_UPDATE_T1000";		// T10_滞留判定（SST_TAIRYUHANTEI）の更新プロシージャ
    private static final String SP_SS_OS_SELECT_TAIRYU        	= "SP_SS_OS_SELECT_TAIRYU";         // 前回実施案件No.取得プロシージャ
    
    // INパラメータ
    private String workflowSystemkbn;    // 業務フローパターンシステム区分

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public KureemuSyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        syosai_bean = cmnData.getSyosai_bean();
        form = (KureemuSyosaiForm)appContext.getActionForm();

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

		// ResultSet取得
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
     * T10_滞留判定の滞留判定案件Noを取得 <br>
     * 
     * @return 滞留判定案件No
     * @exception SQLException
     */
    public String getTairyuAnkenNo() throws SQLException {
    	
    	String ankenno = GS.EMPTY_CHARCTER;
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

            if (rs.next()) {
            	ankenno = rs.getString(ANKEN_NO);
            }
            return ankenno;
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
	public void setUpdateT1000(String anken_no) throws SQLException {
        // T10_滞留判定の更新
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS3103_UPDATE_T1000, sqlExec);
        if (form.isKureemu_saiken()) {
        	cstmt.setStringIn(TAIRYU_HANTEI_OVER4);
        } else {
        	cstmt.setStringIn(null);
        }
        cstmt.setStringIn(Function.trim(syosai_bean.getHantei_jiyu()));
        cstmt.setStringIn(user_bean.getComUserId());
        cstmt.setStringIn(anken_no);
        cstmt.setIntIn(Integer.parseInt(syosai_bean.getId())+1);
        cstmt.setStringIn(tori_bean.getPhase());
        
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