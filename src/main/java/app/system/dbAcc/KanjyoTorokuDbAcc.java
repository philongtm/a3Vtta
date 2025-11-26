/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/26		SSC				課題No.104 重複チェック修正 
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.system.form.KanjyoTorokuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;

/**
 * OS7109 勘定科目マスタメンテナンス_登録 DBアクセスクラス <br>
 */
public class KanjyoTorokuDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private KanjyoTorokuForm form = null;					                          // アクションフォーム
	
	// Resultset用文字列       
	private static final String KBN_HYOUJI_VAL       	= "kbn_hyouji_val";
	private static final String KBN_VAL              	= "kbn_val";   
	private static final String SATEI_KAISHA_NM      	= "satei_kaisha_nm";  
    private static final String DRCR_KBN		   		= "drcr_kbn";
    private static final String KANJO_HYOUJI_KBN		= "kanjo_hyouji_kbn"; 
	private static final String HANYO1 				= "hanyo1";                    							// 区分キー（債権フラグ）
	private static final String SAIKEN_FLG			= "saiken_flg";
	private static final String SYSTEM_KBN      		= "system_kbn";											// システム区分
    private static final String KANJYO_CD_CNT			= "KANJYO_CD_CNT";										// 勘定科目コードが既に登録されていないかチェックを行う件数																				
    
	// INパラメータ
    private String userId;                                                                             		// ユーザＩＤ
	
    private static final String SP_SS_O_SELECT_P0202		   			= "SP_SS_O_SELECT_P0202";        		
    private static final String SP_SS_OS_SELECT_M0400         		= "SP_SS_OS_SELECT_M0400";          	// 汎用２セレクトボックス取得処理
    private static final String SP_SS_OS7109_INSERT_M1400         	= "SP_SS_OS7109_INSERT_M1400";          // M14_勘定科目マスタの更新
    private static final String SP_SS_OS7108_SELECT_M1401         	= "SP_SS_OS7108_SELECT_M1401";          // 重複チェック(入力された勘定科目コードが、同一システム、同一汎用１、同一汎用２で既に存在する場合)
    private static final String SP_SS_OL_SELECT_P0200		   			= "SP_SS_OL_SELECT_P0200";        		
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public KanjyoTorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (KanjyoTorokuForm)appContext.getActionForm();
		
        // ビーンの値を変数に設定
        this.userId = user_bean.getComUserId();
	}
	
    /**
     * 変数初期化 <br>
     * 
     */
    public void initialize() {
        // INパラメータ
        userId = GS.EMPTY_CHARCTER;
    }
    
    /**
     * システムセレクトボックス設定値取得処理 <br>
     * 
     * @return
     * @throws SQLException
     */
    public void getSystem() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_P0202, sqlExec);
            exCstmt.setStringIn(SYSTEM_KBN);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setResultSet(RESULTSET);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_system = new LinkedHashMap<String,String>();
			
			int i = 0;

			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSystem_kbn(rs.getString(KBN_VAL));
				}
				ar_system.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			
			form.setAr_system(ar_system);

        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
	
	/**
	 * 汎用１セレクトボックス取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo1() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(HANYO1);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyo1 = new LinkedHashMap<String,String>();
			int i = 0;
			ar_hanyo1.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				ar_hanyo1.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}

			form.setAr_hanyo1(ar_hanyo1);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 汎用２セレクトボックス取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo2() throws SQLException {

		ResultSet rs = null;

		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0400, sqlExec);
            exCstmt.setStringIn(form.getHanyo1());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyo2 = new LinkedHashMap<String,String>();
			int i = 0;
			ar_hanyo2.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				ar_hanyo2.put(rs.getString(SATEI_KAISHA_NM),rs.getString(SATEI_KAISHA_NM));	    			
				i++;
			}
			
			form.setAr_hanyo2(ar_hanyo2);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * ヘッダ部債権フラグセレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSaiken_flg() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(SAIKEN_FLG);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_saiken_flg = new LinkedHashMap<String,String>();
			int i = 0;
			
			ar_saiken_flg.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			
			while ( rs.next() ) {
				ar_saiken_flg.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			
			form.setAr_saiken_flg(ar_saiken_flg);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * DR/CR区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getDrcrkbn() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(DRCR_KBN);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_drcr_kbn = new LinkedHashMap<String,String>();
			int i = 0;
			ar_drcr_kbn.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				ar_drcr_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			
			form.setAr_drcr_kbn(ar_drcr_kbn);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 表示区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHyojikbn() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(KANJO_HYOUJI_KBN);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hyoji_kbn = new LinkedHashMap<String,String>();
			int i = 0;
			
			ar_hyoji_kbn.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			
			while ( rs.next() ) {
				ar_hyoji_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));

				i++;
			}
			
			form.setAr_hyoji_kbn(ar_hyoji_kbn);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
    /**
     * M14_勘定科目マスタの登録 <br>
     * 
     * @exception SQLException
     */
    public void setInsertM1400() throws SQLException {

        ResultSet rs = null;
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7109_INSERT_M1400, sqlExec);
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(form.getSystem_kbn());        
        exCstmt.setStringIn(form.getKanjo_cd());
        exCstmt.setStringIn(form.getKanjo_nm());
        exCstmt.setStringIn(form.getKanjo_uchi_cd());
        exCstmt.setStringIn(form.getKanjo_uchi_nm());
        exCstmt.setStringIn(form.getSaiken_flg());
        if (GS.EMPTY_CHARCTER.equals(form.getMankibi_flg())) {
            exCstmt.setStringIn("0");
        } else {
            exCstmt.setStringIn("1");
        }
        exCstmt.setStringIn(form.getHyoji_kbn());
        exCstmt.setStringIn(form.getHanyo1());
        exCstmt.setStringIn(form.getHanyo2().trim());   	        
        exCstmt.setStringIn(form.getDrcr_kbn());
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        
        try {
        	
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
                    
            commit();
            
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
	 * 重複チェック <br>
	 * 
	 * @exception SQLException
	 */
	public int getKanjyoCdCnt() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		 try{
	            // ExCallableStatement生成
	            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7108_SELECT_M1401, sqlExec);
	            exCstmt.setStringIn(form.getHanyo1());
	            exCstmt.setStringIn(form.getHanyo2());  
	            exCstmt.setStringIn(form.getSystem_kbn());
	            exCstmt.setStringIn(form.getKanjo_cd());
	            // 課題No.104
	            // 追加開始
	            exCstmt.setStringIn(Function.trim(form.getKanjo_uchi_cd()));
	            // 追加完了
	            exCstmt.setIntOut(KANJYO_CD_CNT);
	            
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            
	            // 件数
	            int cntKanjyo_cd = 0;
	            cntKanjyo_cd = exCstmt.getInt(KANJYO_CD_CNT);

	            return cntKanjyo_cd;
	        } finally {
	            if (rs != null) {
	                // Resultset close
	                rs.close();
	            }
	        }
	}
}
