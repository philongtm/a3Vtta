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
import app.system.form.KanjyoBean;
import app.system.form.KanjyoForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * OS7108_勘定科目マスタメンテナンス_一覧・登録 DBアクセスクラス <br>
 */
public class KanjyoDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private KanjyoForm form = null;						                          // アクションフォーム
	
	// Resultset用文字列       
	private static final String KBN_HYOUJI_VAL       	= "kbn_hyouji_val";
	private static final String KBN_VAL              	= "kbn_val";   
	private static final String SATEI_KAISHA_NM      	= "satei_kaisha_nm";  
	
    private static final String SYSTEM_KBN_NM        	= "system_kbn_nm";
    private static final String SATEIKAISYA_CD   		= "sateikaisya_cd";
    private static final String MISE_CD		   		= "mise_cd";
    private static final String DRCR_KBN		   		= "drcr_kbn";
    private static final String DRCR_KBN_NM	   		= "drcr_kbn_nm";
    private static final String KANJO_HYOUJI_KBN		= "kanjo_hyouji_kbn";
    private static final String KANJO_HYOUJI_KBN_NM	= "kanjo_hyouji_kbn_nm";
    private static final String MANKIBI_FLG		   	= "mankibi_fig";
    private static final String KANJO_CD	   			= "kanjo_cd";
    private static final String KANJO_NM	   			= "kanjo_nm";
    private static final String KANJO_UCHI_CD	   		= "kanjo_uchi_cd";
    private static final String KANJO_UCHI_NM   		= "kanjo_uchi_nm";
    private static final String SAIKEN_FLG_NM 		= "saiken_flg_nm";
    private static final String KANJO		   			= "kanjo";
    private static final String KANJO_UCHI	   		= "kanjo_uchi";
    private static final String INS_USER	   			= "ins_user";
    private static final String INS_DT	   			= "ins_dt";
    private static final String UPD_USER	   			= "upd_user";
    private static final String UPD_DT	   			= "upd_dt";
    
	private static final String SHOW 					= "show";                                               // 区分キー（表示件数）
	private static final String HANYO1 				= "hanyo1";                                             // 区分キー（債権フラグ）
	private static final String SAIKEN_FLG			= "saiken_flg";
	private static final String CHOHYO_LS1101			= "chohyo_LS1101";										//区分キー(帳票ヘッダ部)
	
    private static final String NOSET_KEY_JA     		= "未設定";
    private static final String NOSET_KEY_EN     		= "Unsetting";
    private static final String NOSET_VALUE      		= "-1";
    
    private static final String SYSTEM_KBN      		= "system_kbn";											// システム区分
    private static final String CHECKBOX_STATUS_ON	= "on";													// チェックボックスのステータス:オン
    private static final String CHECKBOX_STATUS_OFF	= "0";													// チェックボックスのステータス:オフ
    	
    private static final String SP_SS_O_SELECT_P0202		   			= "SP_SS_O_SELECT_P0202";        		// システムセレクトボックス設定値取得処理
    private static final String SP_SS_OS_SELECT_M0400         		= "SP_SS_OS_SELECT_M0400";          	// 汎用２セレクトボックス取得処理
    private static final String SP_SS_OL_SELECT_M1400         		= "SP_SS_OL_SELECT_M1400";          		// 検索アクションプロシージャ
    private static final String SP_SS_OS7108_UPDATE_M1400         	= "SP_SS_OS7108_UPDATE_M1400";          // M14_勘定科目マスタの更新

    private static final String SP_SS_OL_SELECT_P0200		   			= "SP_SS_OL_SELECT_P0200";        		
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public KanjyoDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (KanjyoForm)appContext.getActionForm();
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

		// ResultSet取得
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
			
			if(cmnData.getComLangMode().equals(GS.LANG_JA)){
				ar_saiken_flg.put(NOSET_KEY_JA,NOSET_VALUE);
			}else{
				ar_saiken_flg.put(NOSET_KEY_EN,NOSET_VALUE);
			}
			
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSaiken_flg(GS.EMPTY_CHARCTER);
				}
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
	 * 明細部債権フラグセレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getMeisai_saiken_flg() throws SQLException {

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
	    	
			form.setAr_meisai_saiken_flg(ar_saiken_flg);
			
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
            exCstmt.setStringIn(form.getSystem_kbn());
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
     * 一覧情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai() throws SQLException {

        ResultSet rs = null;
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_M1400, sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(form.getSystem_kbn());        
        exCstmt.setStringIn(form.getHanyo1());
        exCstmt.setStringIn(form.getHanyo2());
        exCstmt.setStringIn(form.getKanjo_cd());
        exCstmt.setStringIn(form.getKanjo_nm());
        exCstmt.setStringIn(form.getKanjo_uchi_cd());
        exCstmt.setStringIn(form.getSaiken_flg());        
        exCstmt.setResultSet(RESULTSET);
        
        try {
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<KanjyoBean> ar_meisai = new ArrayList<KanjyoBean>();   

            int i = 0;
            while ( rs.next() ) {
                
            	KanjyoBean listBean = new KanjyoBean();
                
                // id
            	listBean.setId(Function.getStringOfInt(i));
                
                // システム区分表示名
            	listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
            	
                // システム区分表示名
            	listBean.setSystem_kbn_nm(rs.getString(SYSTEM_KBN_NM));
                
                // 汎用１
                listBean.setHanyo1(rs.getString(SATEIKAISYA_CD));
                
                // 汎用２
                listBean.setHanyo2(rs.getString(MISE_CD));
                
                // 勘定科目コード
                listBean.setKanjo_cd(rs.getString(KANJO_CD));
                
                // 内分類コード
                listBean.setKanjo_uchi_cd(rs.getString(KANJO_UCHI_CD));
                
                // 勘定科目
                listBean.setKanjo(rs.getString(KANJO));
                
                // 内分類
                listBean.setKanjo_uchi(rs.getString(KANJO_UCHI));
                
                // DR／CR区分
                listBean.setMeisai_drcr_kbn(rs.getString(DRCR_KBN));
                
                // 債権フラグ
                listBean.setMeisai_saiken_flg(rs.getString(SAIKEN_FLG));
                
                // 表示区分
                listBean.setMeisai_hyouji_kbn(rs.getString(KANJO_HYOUJI_KBN));
                
                // 満期日優先
                listBean.setMankibi_flg(CHECKBOX_STATUS_OFF);
                if (("1").equals(rs.getString(MANKIBI_FLG))) {
                	listBean.setMankibi_flg(CHECKBOX_STATUS_ON);
                }
                
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
     * M14_勘定科目マスタの更新処理 <br>
     * 
     * @exception SQLException
     */
    public void setUpdateM1400(KanjyoBean meisaiBean) throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7108_UPDATE_M1400, sqlExec);
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(meisaiBean.getSystem_kbn());        
        exCstmt.setStringIn(meisaiBean.getMeisai_saiken_flg());
        if (GS.EMPTY_CHARCTER.equals(meisaiBean.getMankibi_flg())) {
        	exCstmt.setStringIn("0");
        } else {
        	exCstmt.setStringIn("1");
        }
        exCstmt.setStringIn(meisaiBean.getMeisai_hyouji_kbn());
        exCstmt.setStringIn(meisaiBean.getMeisai_drcr_kbn());
        exCstmt.setStringIn(meisaiBean.getKanjo_cd());
        exCstmt.setStringIn(meisaiBean.getHanyo1());
        exCstmt.setStringIn(meisaiBean.getHanyo2());
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(meisaiBean.getKanjo_uchi_cd());
        
        try {
        	
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
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
     * CSVヘッダ部情報取得処理 <br>
     * 
     * @return
     * @throws SQLException
     */
    public void getHeader(PrintWriter pw) throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(CHOHYO_LS1101);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(form.getSystem_kbn());
            exCstmt.setResultSet(RESULTSET);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            //CSV出力
            int i =0;
			while ( rs.next() ) {
				
				pw.print(rs.getString(KBN_HYOUJI_VAL));
				if(form.getSystem_kbn().equals(GS.GSS)){
					if(i < 15) {
						pw.print(GS.COMMA);
					}
				}else{
					if(i < 14) {
						pw.print(GS.COMMA);
					}
				}
				i++;
			}
				//改行をする
				pw.println();
					
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * CSV一覧情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai_csv(PrintWriter pw) throws SQLException {

        ResultSet rs = null;
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_M1400, sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(form.getSystem_kbn());        
        exCstmt.setStringIn(form.getHanyo1());
        exCstmt.setStringIn(form.getHanyo2());
        exCstmt.setStringIn(form.getKanjo_cd());
        exCstmt.setStringIn(form.getKanjo_nm());
        exCstmt.setStringIn(form.getKanjo_uchi_cd());
        exCstmt.setStringIn(form.getSaiken_flg());        
        exCstmt.setResultSet(RESULTSET);
        
        
        try {
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            while ( rs.next() ) {
            		          
                // システム区分表示名
            	pw.print((formatCsv(rs.getString(SYSTEM_KBN_NM))));
            	pw.print(GS.COMMA);
            	
                // 汎用１
            	pw.print(formatCsv(rs.getString(SATEIKAISYA_CD)));
            	pw.print(GS.COMMA);
            	
                // 汎用２
            	pw.print(formatCsv(rs.getString(MISE_CD)));
            	pw.print(GS.COMMA);
            	
                // 勘定科目コード
            	pw.print(formatCsv(rs.getString(KANJO_CD)));
            	pw.print(GS.COMMA);
            	
                // 勘定科目名称
            	pw.print(formatCsv(rs.getString(KANJO_NM)));
            	pw.print(GS.COMMA);
            	
            	if(form.getSystem_kbn().equals(GS.GSS)) {
            		// 国内の場合
	                // 内分類コード
            		pw.print(formatCsv(rs.getString(KANJO_UCHI_CD)));
	            	pw.print(GS.COMMA);
	            
	            	// 内分類名称
            		pw.print(formatCsv(rs.getString(KANJO_UCHI_NM)));
	            	pw.print(GS.COMMA);
            	}
                // 債権フラグ
            	pw.print(formatCsv(rs.getString(SAIKEN_FLG)));
            	pw.print(GS.COMMA);
            	
                // 債権フラグ名称
            	pw.print(formatCsv(rs.getString(SAIKEN_FLG_NM)));
            	pw.print(GS.COMMA);
            	
                // 表示区分
            	pw.print(formatCsv(rs.getString(KANJO_HYOUJI_KBN)));
            	pw.print(GS.COMMA);
            	
                // 表示区分名称
            	pw.print(formatCsv(rs.getString(KANJO_HYOUJI_KBN_NM)));
            	pw.print(GS.COMMA);
            	
            	if(!form.getSystem_kbn().equals(GS.GSS)){
	            	// 海外の場合
	                // DR／CR区分
	            	pw.print(formatCsv(rs.getString(DRCR_KBN)));
	            	pw.print(GS.COMMA);
	            	
	                // DR／CR区分名称
	            	pw.print(formatCsv(Function.trim(rs.getString(DRCR_KBN_NM))));
	            	pw.print(GS.COMMA);
            	}
            	
            	if(form.getSystem_kbn().equals(GS.GSS)){
	                // 満期日優先フラグ
            		
	            	pw.print(formatCsv(rs.getString(MANKIBI_FLG)));
	            	pw.print(GS.COMMA);
            	}
            	
                // 登録ユーザID
            	pw.print(formatCsv(rs.getString(INS_USER)));
            	pw.print(GS.COMMA);
            	
                // 登録日時
            	pw.print(formatCsv(rs.getString(INS_DT)));
            	pw.print(GS.COMMA);
            	
                // 更新ユーザID
            	pw.print(formatCsv(rs.getString(UPD_USER)));
            	pw.print(GS.COMMA);
            	
                // 更新日時
            	pw.println(formatCsv(rs.getString(UPD_DT)));
            }
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
     * CSVのフォーマットする(ダブルクォーテーション)を付加する <br>
     * 
     * @param 取得した値
     * @return フォーマットされた値
     */
    private StringBuffer formatCsv(String csv) {
    	
    	StringBuffer sb = null;
       	sb = new StringBuffer(GS.DOUBLE_QUOTATION)
       							.append(Function.trim(csv))
       							.append(GS.DOUBLE_QUOTATION);
        return sb;
    }
    
    
}
