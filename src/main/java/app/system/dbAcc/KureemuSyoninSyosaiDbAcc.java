/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.form.KureemuSyoninSyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OS3105_クレーム債権再設定_承認 DBアクセスクラス <br>
 */
public class KureemuSyoninSyosaiDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                          						// ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          					// 機能共通セッション
	private UserBean user_bean = null;					                          						// ユーザ情報
    private TorihikisakiBean tori_bean;     															// 取引先情報
	private KureemuSyoninSyosaiForm form;    
	
    private static final String SP_SS_O_UPDATE_T1400         	 = "SP_SS_O_UPDATE_T1400";              // T14_査定進捗管理を更新するプロシージャ
    private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";               // T13_入力履歴の登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400          = "SP_SS_O_INSERT_T0400";               // T04_メール配信の登録を行うプロシージャ
    
    private static final String HAISINZUMI_FLG 				= "N";									 // 配信済みフラグ
    private static final String TAIRYU_HANTEI					= "2";						 	 		 // 滞留判定
    private static final String NYURYOKU_KBN_SYONIN			= "80";						 	 		 // 入力区分(承認)
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public KureemuSyoninSyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        this.form = (KureemuSyoninSyosaiForm) appContext.getActionForm();
	}
    
	/**
     * T14_査定進捗管理の更新 <br>
     * 
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOUpdateT1400() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
            exCstmt.setStringIn(tori_bean.getAnken_no());
            exCstmt.setStringIn(form.getJi_jishi_phase());
            exCstmt.setStringIn(form.getJi_kaishi_status());            
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null); // 査定登録画面
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(GS.TORIMODOSHI_HUKA);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * T13_入力履歴の登録 <br>
     * 
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOInsertT1300() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
            exCstmt.setStringIn(tori_bean.getAnken_no());
            exCstmt.setStringIn(TAIRYU_HANTEI);
            exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(user_bean.getComUser_Nm());
            exCstmt.setStringIn(user_bean.getComUser_Nm_En());
            exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
            exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());
            exCstmt.setStringIn(tori_bean.getPhase());
            exCstmt.setStringIn(NYURYOKU_KBN_SYONIN);
            exCstmt.setStringIn(user_bean.getComUserId());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(null);
                exCstmt.setStringIn(null);
                exCstmt.setStringIn(null);
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
                exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
                exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
            }
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * T04_メール配信の登録 <br>
     * 
     * @param phase
     * @param status
     * @throws SQLException
     */
    public void setOBInsertT0400() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
            exCstmt.setStringIn(user_bean.getComUserId());                
            exCstmt.setStringIn(tori_bean.getAnken_no());
            exCstmt.setStringIn(tori_bean.getTaisyo_ym());
            exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
            exCstmt.setStringIn(tori_bean.getBunrui2());
            // 部コード(システム区分01の場合のみ設定)
            if(GS.GSS.equals(tori_bean.getSystem_kbn())){
            	exCstmt.setStringIn(tori_bean.getBu_cd());
            } else {
            	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
            }
            exCstmt.setStringIn(form.getJi_jishi_phase());             
            exCstmt.setStringIn(form.getJi_kaishi_status());
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(HAISINZUMI_FLG);
            exCstmt.setStringIn(GS.OS3104);
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());                
            } else {                
                exCstmt.setStringIn(user_bean.getComDaiko_userId());               
            }
            
            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
}
