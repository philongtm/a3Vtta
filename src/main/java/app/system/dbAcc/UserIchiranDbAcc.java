/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/25		SSC				課題No.101 業務フロープルダウン制御 対応
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.form.UserIchiranForm;
import app.system.form.WorkFlowListBean;
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
 * OS7106_ユーザマスタメンテナンス_一覧 DBアクセスクラス <br>
 */
public class UserIchiranDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private UserIchiranForm form = null;						                          // アクションフォーム
	
	// Resultset用文字列       
	private static final String SATEI_KAISHA_HYOUJI  				= "satei_kaisha_hyouji";
	private static final String PATTERN_ID      					= "pattern_id";  
	private static final String PATTERN_NM      					= "pattern_nm"; 	
	private static final String KBN_HYOUJI_VAL       				= "kbn_hyouji_val";
	private static final String KBN_VAL              				= "kbn_val";   
	
    private static final String MEISAI_TOGO_ID	   				= "togo_id";
    private static final String MEISAI_USER_NM	   				= "user_nm";
    private static final String MEISAI_COMPANY_NM	   				= "company_nm";
    private static final String MEISAI_SOSHIKI_NM	   				= "soshiki_nm";
    private static final String MEISAI_EMAIL_ADDR	   				= "email_addr";   
    private static final String ADMIN_FLG			   				= "admin_flg";   
    private static final String MEISAI_PRINTOUT_DEFAULT_LANG_KBN	= "printout_default_lang_kbn";   
    private static final String MEISAI_MAIL_HAISIN_KBN	   		= "mail_haisin_kbn";    
	private static final String MEISAI_PATTERN_NM      			= "pattern_nm"; 	
	
    private static final String PATAN_SYSTEM_KBN   				= "system_kbn";						    // 業務フローパターンシステム区分						
    private static final String PATAN_SATEIKAISYA_CD   			= "sateikaisya_cd";						// 業務フローパターン査定会社コード
    private static final String PATAN_DEFAULT_FLG	   				= "default_flg";						// 業務フローパターン既定フラグ
    private static final String PATAN_PATTERN_ID	   				= "pattern_id";							// パターンID
    private static final String PATAN_PATTERN_NAME_JP	   			= "pattern_name_jp";   					// パターン名称
    private static final String PATAN_ADMIN_SENYO_FLG				= "admin_senyo_flg";					// システム管理者専用フラグ
    private static final String PRINTOUT_LANG_KBN					= "printout_lang_kbn";					// 帳票言語(区分値)
	
	private static final String SHOW 								= "show";                              	// 区分キー（表示件数）
	
    private static final String NOSET_KEY_JA     					= "未設定";
    private static final String NOSET_KEY_EN      				= "Unsetting";
    private static final String NOSET_VALUE      					= "-1";
    private static final String BR		      					= "<br/>";								// 業務フローは改行して表示用
	
    private static final String SP_SS_OS_SELECT_M0200		   		= "SP_SS_OS_SELECT_M0200";        		// 汎用１セレクトボックスの設定値を取得する
    private static final String SP_SS_OS_SELECT_M1700         	= "SP_SS_OS_SELECT_M1700";          	// 業務フローセレクトボックスの設定値を取得する
    private static final String SP_SS_OL_SELECT_P0200		   		= "SP_SS_OL_SELECT_P0200";        		
    private static final String SP_SS_OS_SELECT_USERICHIRAN		= "SP_SS_OS_SELECT_USERICHIRAN";    	// 検索アクション    		
    private static final String SP_SS_OS7106_SELECT_WORKFLOW		= "SP_SS_OS7106_SELECT_WORKFLOW";    	// 業務フローパターンを取得する
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public UserIchiranDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (UserIchiranForm)appContext.getActionForm();
		
	}	
	
	/**
	 * 汎用１セレクトボックス取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo1() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0200, sqlExec);
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyo1 = new LinkedHashMap<String,String>();
			int i = 0;
			ar_hanyo1.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			StringBuffer sb = null;
			while ( rs.next() ) {
				sb = new StringBuffer(rs.getString(SATEI_KAISHA_HYOUJI))
							.append(",")
							.append(rs.getString(PATAN_SYSTEM_KBN));
				ar_hanyo1.put(rs.getString(SATEI_KAISHA_HYOUJI),sb.toString());	    			
				i++;
			}

			// 初期設定
			StringBuffer st = null;
				st = new StringBuffer(user_bean.getComWorkflowSateikaisya_cd())
										.append(",")
										.append(user_bean.getComWorkflowSystemkbn());

				// 初期設定
				form.setHanyo1(st.toString());
				
				form.setAr_hanyo1(ar_hanyo1);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 業務フローセレクトボックス取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getGyoumu_huro() throws SQLException {

		ResultSet rs = null;
		String system_kbn = null;
		String hanyo1 = null;
		
		// 課題No.101
		// 追加開始
		// 業務フローパターンを初期化
		form.setGyoumu_huro(GS.EMPTY_CHARCTER);
		// 追加完了
		
		if(!(form.getHanyo1().equals(GS.EMPTY_CHARCTER))){
			String str[] = Function.StrSplitToken(form.getHanyo1(),",");
			
			system_kbn = str[1];
			hanyo1 = str[0];
		}

		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M1700, sqlExec);
            exCstmt.setStringIn(system_kbn);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(hanyo1);
            exCstmt.setResultSet(RESULTSET);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_gyoumu_huro = new LinkedHashMap<String,String>();
		
			ar_gyoumu_huro.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);			
			if (form.getHanyo1() == null 
				|| GS.EMPTY_CHARCTER.equals(form.getHanyo1())) {
					if(GS.LANG_JA.equals(cmnData.getComLangMode())){
						ar_gyoumu_huro.put(NOSET_KEY_JA,NOSET_VALUE);
					}else if(GS.LANG_EN.equals(cmnData.getComLangMode())) {
						ar_gyoumu_huro.put(NOSET_KEY_EN,NOSET_VALUE);
					}
					
			} else {
				int i = 0;
				while ( rs.next() ) {
					ar_gyoumu_huro.put(rs.getString(PATTERN_NM),rs.getString(PATTERN_ID));	    			
					i++;
				}
			}
			
			form.setAr_gyoumu_huro(ar_gyoumu_huro);
			
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
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

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(SHOW);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
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
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
    /**
     * 一覧情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai() throws SQLException {

        ResultSet rs = null;
        
		String hanyo1 = null;
		
		if(!(form.getHanyo1().equals(GS.EMPTY_CHARCTER))){

			String str[] = Function.StrSplitToken(form.getHanyo1(),",");
			hanyo1 = str[0];
		}
		
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_USERICHIRAN, sqlExec);
        exCstmt.setStringIn(form.getGyoumu_huro());
        exCstmt.setStringIn(cmnData.getComLangMode());        
        exCstmt.setStringIn(hanyo1);
        exCstmt.setStringIn(Function.addSingleQuotation(form.getUser_id()));
        exCstmt.setStringIn(Function.addSingleQuotation(form.getUser_nm()));
        exCstmt.setStringIn(Function.addSingleQuotation(form.getEmail()));   
        exCstmt.setResultSet(RESULTSET);
        
        try {
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<UserMaintenanceBean> ar_meisai = new ArrayList<UserMaintenanceBean>();   

            int i = 0;
            while ( rs.next() ) {
                
            	UserMaintenanceBean listBean = new UserMaintenanceBean();
                
                // id
            	listBean.setId(Function.getStringOfInt(i));
                // ユーザID
            	listBean.setUser_id(rs.getString(MEISAI_TOGO_ID));
            	
                // 氏名
            	listBean.setUser_nm(rs.getString(MEISAI_USER_NM));
                
                // 会社名
                listBean.setKaisya_nm(rs.getString(MEISAI_COMPANY_NM));
                
                // 所属組織名
                listBean.setSyozoku_busyo_nm(rs.getString(MEISAI_SOSHIKI_NM));
                
                // メールアドレス
                listBean.setMail_address(rs.getString(MEISAI_EMAIL_ADDR));
                
                // システム管理者フラグ
                listBean.setAdmin_flg(rs.getString(ADMIN_FLG));
                
                // 帳票出力言語
                listBean.setTyohyo_syuturyoku_lang(rs.getString(MEISAI_PRINTOUT_DEFAULT_LANG_KBN));
                
                // メール配信
                listBean.setMail_haishin(rs.getString(MEISAI_MAIL_HAISIN_KBN));
                
                // 帳票出力言語(区分値)
                listBean.setpPrintout_lang_kbn(rs.getString(PRINTOUT_LANG_KBN));

                // 表示用業務フローパターン名称
                String resualt = rs.getString(MEISAI_PATTERN_NM);
                if (resualt != null) {
                	resualt = rs.getString(MEISAI_PATTERN_NM).replaceAll("№1∇№1", BR);   	
                } else {
                	resualt = GS.EMPTY_CHARCTER;
                }
                listBean.setWorkflow_h_nm(resualt);
                
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
                    // Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
    }
    
	/**
	 * 業務フローパターンを取得する <br>
	 * 
	 * @exception SQLException
	 */
	public void getGyoumuhuroPatanList(UserMaintenanceBean user_main_bean) throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7106_SELECT_WORKFLOW, sqlExec);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(user_main_bean.getUser_id());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            // ActionForm に取得値を格納
            List<WorkFlowListBean> ar_meisai = new ArrayList<WorkFlowListBean>(); 
            
			while ( rs.next() ) {
				
				WorkFlowListBean listBean = new WorkFlowListBean();
	            
				// システム区分
				listBean.setSystem_kbn(rs.getString(PATAN_SYSTEM_KBN));
				
				// 査定会社コード
				listBean.setSateikaisya_cd(rs.getString(PATAN_SATEIKAISYA_CD));
				
				// 既定フラグ
				listBean.setDefault_flg(rs.getString(PATAN_DEFAULT_FLG));
				
				// パターンID
				listBean.setPattern_id(rs.getString(PATAN_PATTERN_ID));
				
				// パターン名称
				listBean.setPattern_name_jp(rs.getString(PATAN_PATTERN_NAME_JP));
				
				// システム管理者専用フラグ
				listBean.setAdmin_senyo_flg(rs.getString(PATAN_ADMIN_SENYO_FLG));

				ar_meisai.add(listBean);
			}
			
			user_main_bean.setWorkflow_list(ar_meisai);
			
			cmnData.setUser_maintenance_bean(user_main_bean);	
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
}
