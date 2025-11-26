/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正 
******************************************************************************/

package app.hikiate.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OD1104_引当金確認_承認 DBアクセスクラス <br>
 */
public class HikiateSyoninSyosaiDbAcc extends CommonDbAcc {
    private SessionData cmnData = null;             // 機能共通セッション
    private UserBean user_bean = null;                  // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    private AppContext appContext = null;               // ＡＰＰコンテキスト

    private static final String MODOSHI_FLG_1            = "1";						// 取戻不可フラグ:取戻不可
    private static final String STATUS_40                = "40";						// ステータス:完了
    private static final String HANTEI_KBN_2             = "2";						// 判定査定区分
    private static final String OPE_KBN_80               = "80";						// 入力区分:承認
    

    private static final String SP_SS_O_UPDATE_T1400					= "SP_SS_O_UPDATE_T1400";				// 案件の進捗を更新する
    private static final String SP_SS_O_INSERT_T1300					= "SP_SS_O_INSERT_T1300";               // T13_入力履歴（SST_NYURYOKU_HIST）の登録プロシージャ

    /**
     * コンストラクタ
     * 
     * @param SqlExecuter
     * @param Log
     * @param AppContext
     */
    public HikiateSyoninSyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();

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
    public void uptT14(String phase) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(phase);								// フェーズ
		exCstmt.setStringIn(STATUS_40);							// ステータス
		//課題No.129
		//修正開始
		//exCstmt.setStringIn(user_bean.getComUserId());		// 案件保持ユーザID
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);					// 案件保持ユーザID
		//修正完了
        exCstmt.setStringIn(null); 								// 査定登録画面
		exCstmt.setStringIn(null);								// 転送フラグ
		exCstmt.setStringIn(null);								// 代行ユーザID
		if(tori_bean.getDaiko_user_id() == null || tori_bean.getDaiko_user_id().length() == 0){
			exCstmt.setStringIn(user_bean.getComUserId());			// 更新ユーザID = ユーザID
		}else{
			exCstmt.setStringIn(tori_bean.getDaiko_user_id());			// 更新ユーザID = 代行ユーザID
		}
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
    public void intT13() throws SQLException { 
    	
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
		exCstmt.setStringIn(OPE_KBN_80);								// 入力区分
		exCstmt.setStringIn(user_bean.getComUserId());					// 登録ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());			// 代行ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm());			// 代行者名日本語
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());		// 代行者名英語
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 登録箇所
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// コメント内容
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元分類２
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元部
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 承認ユーザID
		
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
