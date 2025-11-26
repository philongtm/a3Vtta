/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/04		SSC				課題No.21 ユーザマスタ　参照組織一覧の追加・削除制御対応
003		2015/03/30		SSC				BJ201408049 IA化対応時の機能改善
004		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.bss.UserTorokuBss;
import app.system.form.TantouBean;
import app.system.form.UserTorokuForm;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS7107_ユーザマスタメンテナンス_登録 DBアクセスクラス <br>
 */
public class UserTorokuDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private UserMaintenanceBean user_maintenance_bean;								  // ユーザメンテナンスビンー
	private UserTorokuForm form = null;						                      // アクションフォーム
	
	// Resultset用文字列       
	private static final String SATEI_KAISHA_HYOUJI  				= "satei_kaisha_hyouji";
	private static final String PATTERN_ID      					= "code";  
	private static final String PATTERN_NM      					= "pattern_nm"; 
	private static final String SANKO_SYSTEM_KBN_KEY				= "SYSTEM_KBN";
	
    private static final String BUNRUI1	   						= "bunrui1";   
    private static final String BUNRUI1_NM						= "bunrui1_nm";   
    private static final String BUNRUI2	   						= "bunrui2";    
    private static final String BUNRUI2_NM      					= "bunrui2_nm"; 	
    private static final String SYSTEM_KBN      					= "system_kbn";							// システム区分
    private static final String COUNT_NUM							= "cnt";
	
	// メール配信先
	private static final String ID									="ID";
	private static final String SYSTEM_KBN_H						="SYSTEM_KBN";
	private static final String HANYOU1FLG							="HANYOU1FLG";	// 汎用1フラグ
	private static final String HANYOU1								="HANYOU1";		// 汎用1
	private static final String HANYOU1_NM							="HANYOU1_NM";	// 汎用1名
	private static final String HANYOU2FLG							="HANYOU2FLG";	// 汎用2フラグ
	private static final String HANYOU2								="HANYOU2";		// 汎用2
	private static final String HANYOU2_NM							="HANYOU2_NM";	// 汎用2名
	private static final String HANYOU3FLG							="HANYOU3FLG";	// 汎用3フラグ
	private static final String HANYOU3								="HANYOU3";		// 汎用3
	private static final String HANYOU3_NM							="HANYOU3_NM";	// 汎用3名
	private static final String KIJUN_BI 							="KIJUN_BI";	// 基準日

	// 担当組織一覧
	private static final String SATEI_KAISHA_CD						="SATEI_KAISHA_CD";	// 査定会社コード
	private static final String TANTOU_BUMON_CD						="TANTOU_BUMON_CD";	// 担当部門コード
	private static final String TANTOU_HONBU_CD						="TANTOU_HONBU_CD";	// 担当本部コード
	private static final String HONBU_CD							="HONBU_CD";		// 本部コード
	private static final String HONBU_NM							="HONBU_NM";		// 本部名称
	private static final String HANYOU4								="HANYOU4";			// 汎用4

	// 楽観排他
	private static final String UPD_DT								="UPD_DT";			// 更新日時
	
	private static final String SP_SS_OS_SELECT_M0200		   		= "SP_SS_OS_SELECT_M0200";        		// 汎用１セレクトボックスの設定値を取得する
    private static final String SP_SS_OS7107_SELECT_M1700         = "SP_SS_OS7107_SELECT_M1700";          // 業務フローセレクトボックスの設定値を取得する		
    private static final String SP_SS_OS7107_SELECT_M0100			= "SP_SS_OS7107_SELECT_M0100";    		// システム管理者存在チェック
    private static final String SP_SS_OS_SELECT_T2201				= "SP_SS_OS_SELECT_T2201";    			// 対象ユーザログインチェック
    private static final String SP_SS_OS_SELECT_T2200				= "SP_SS_OS_SELECT_T2200";    			// 代行者ログインチェック
    private static final String SP_SS_OS7107_SELECT_T0800			= "SP_SS_OS7107_SELECT_T0800";    		// もぎ取り、承認待ちチェック(T08_滞留判定進捗管理)
    private static final String SP_SS_OS7107_SELECT_T1400			= "SP_SS_OS7107_SELECT_T1400";    		// もぎ取り、承認待ちチェック(T14_査定進捗管理)
    private static final String SP_SS_OS7107_SELECT_M0101			= "SP_SS_OS7107_SELECT_M0101";    		// ユーザ情報の存在チェック
    private static final String SP_SS_OS7107_UPDATE_M0100			= "SP_SS_OS7107_UPDATE_M0100";    		// ユーザ情報の更新
    private static final String SP_SS_OS7107_INSERT_M0100			= "SP_SS_OS7107_INSERT_M0100";    		// ユーザ情報の登録		
    private static final String SP_SS_OS7107_DELETE_M0200			= "SP_SS_OS7107_DELETE_M0200";    		// M02_ユーザ参照組織マスタの削除
    private static final String SP_SS_OS7107_INSERT_M0200			= "SP_SS_OS7107_INSERT_M0200";    		// M02_ユーザ参照組織マスタの登録
    private static final String SP_SS_OS7107_DELETE_M2400			= "SP_SS_OS7107_DELETE_M2400";    		// M24_ユーザ業務マスタの削除
    private static final String SP_SS_OS7107_INSERT_M2400			= "SP_SS_OS7107_INSERT_M2400";    		// M24_ユーザ業務マスタの登録
	// メール配信先
	private static final String SP_SS_OS7107_DELETE_M3000			= "SP_SS_OS7107_DELETE_M3000";			// M30メール配信先マスタの削除
	private static final String SP_SS_OS7107_INSERT_M3000			= "SP_SS_OS7107_INSERT_M3000";			// M30メール配信先マスタの登録
	private static final String SP_SS_OS7107_SELECT_HAISHIN			= "SP_SS_OS7107_SELECT_HAISHIN";		// 対象ユーザのメール配信先の取得
	private static final String SP_SS_OS_SELECT_KIJYUNBI			= "SP_SS_OS_SELECT_KIJYUNBI";			// 最新の査定期を取得
	// 担当組織一覧
	private static final String SP_SS_OS_SELECT_M0200_2				= "SP_SS_OS_SELECT_M0200_2";			// ログインユーザの担当組織を取得
	private static final String SP_SS_OS7107_SELECT_TANTO			= "SP_SS_OS7107_SELECT_TANTO";			// 対象ユーザの担当組織一覧を取得
	private static final String SP_SS_OS7107_SELECT_HONBU			= "SP_SS_OS7107_SELECT_HONBU";			// 部門・部に対する本部を取得する
	
    /**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public UserTorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		user_maintenance_bean = cmnData.getUser_maintenance_bean();
		form = (UserTorokuForm)appContext.getActionForm();
		
		//form.setAdmini_flg(user_bean.getComSystemManager_flg());
		
	}	
	
	/**
	 * 対象ログインユーザの参照可能な汎用１を取得 <br>
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
			while ( rs.next() ) {
				ar_hanyo1.put(rs.getString(SATEI_KAISHA_HYOUJI),rs.getString(SATEI_KAISHA_HYOUJI));	    			
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
	 * 業務フローセレクトボックス取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getGyoumu_huro() throws SQLException {

		ResultSet rs = null;

		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_M1700, sqlExec);
            exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY));
            exCstmt.setStringIn(form.getHanyou1());
            exCstmt.setStringIn(cmnData.getComLangMode());
			// 課題No.21 ユーザマスタ 参照組織一覧の追加・削除制御対応
            // 追加開始
            // システム管理者チェックボックスが表示　かつ　チェックオンの場合 システム管理者専用業務フローを取得
            if(user_bean.getComSystemManager_flg().equals(GS.ON) && form.getAdmin_kanri_flg().equals(GS.ON)){
                exCstmt.setStringIn(form.getAdmin_kanri_flg());            	
            }else{
                exCstmt.setStringIn(GS.OFF);  
            }
            // 追加完了
            exCstmt.setResultSet(RESULTSET);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_gyoumu_huro = new LinkedHashMap<String,String>();
			LinkedHashMap<String,String> ar_gyoumu_huro_hid = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				if (i == 0) {
					form.setGyoumu_huro(rs.getString(PATTERN_ID));
				}
				ar_gyoumu_huro.put(rs.getString(PATTERN_NM),rs.getString(PATTERN_ID));	
				ar_gyoumu_huro_hid.put(rs.getString(PATTERN_ID), rs.getString(PATTERN_NM));
				i++;
			}
			
			form.setAr_gyoumu_huro_hid(ar_gyoumu_huro_hid);
			form.setAr_gyoumu_huro(ar_gyoumu_huro);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * システム管理者存在チェック <br>
	 * 
	 * @exception SQLException
	 */
	public int getSelectM0100() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_M0100, sqlExec);
            exCstmt.setIntOut(COUNT_NUM);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);
			
            return cnt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 対象ユーザログインチェック <br>
	 * 
	 * @exception SQLException
	 */
	public int getSelectT2201() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_T2201, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setIntOut(COUNT_NUM);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);
			
            return cnt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 代行者ログインチェック <br>
	 * 
	 * @exception SQLException
	 */
	public int getSelectT2200() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_T2200, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setIntOut(COUNT_NUM);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);
			
            return cnt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
		
	/**
	 * もぎ取り、承認待ちチェック(T08_滞留判定進捗管理) <br>
	 * 
	 * @exception SQLException
	 */
	public int getSelectT0800() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_T0800, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setIntOut(COUNT_NUM);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);
			
            return cnt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * もぎ取り、承認待ちチェック(T14_査定進捗管理) <br>
	 * 
	 * @exception SQLException
	 */
	public int getSelectT1400() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_T1400, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setIntOut(COUNT_NUM);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);
			
            return cnt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * ユーザ情報の楽観排他<br>
	 * ユーザーレベルマスタの該当レコードから、更新日時を取得する
	 * @return ユーザレベルマスタの更新日時（レコードが存在しない場合はNULL）
	 * @throws SQLException
	 */
	public Date getSelectM0101() throws Exception {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_M0101, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setStringOut(UPD_DT);
            
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
            // 更新日時
			Date updDt = new Date();
			SimpleDateFormat sdf =new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
			String dt = exCstmt.getString(UPD_DT);
			if(dt != null && !" ".equals(dt)){
				updDt = sdf.parse(dt);
			} else {
				updDt = null;
			}
			return updDt;
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * M01_ユーザレベルマスタの更新 <br>
	 * 
	 * @exception SQLException
	 */
	public void setUpdateM0100() throws SQLException {
		
        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_UPDATE_M0100, sqlExec);
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(form.getAdmin_kanri_flg());
            exCstmt.setStringIn(form.getMail_haisin_kbn());
            exCstmt.setStringIn(form.getPrintout_default_lang_kbn());
            exCstmt.setStringIn(form.getUser_id());
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
	 * M01ユーザレベルマスタの登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void setInsertM0100() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_INSERT_M0100, sqlExec);
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(form.getAdmin_kanri_flg());
            exCstmt.setStringIn(form.getMail_haisin_kbn());
            exCstmt.setStringIn(form.getPrintout_default_lang_kbn());
            exCstmt.setStringIn(form.getUser_id());
            
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
	 * M02_ユーザ参照組織マスタの削除 <br>
	 * 
	 * @exception SQLException
	 */
	public void setDeleteM0200() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_DELETE_M0200, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
          
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
	 * M02_ユーザ参照組織マスタの登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void setInsertM0200(TantouBean tantouBean) throws SQLException {

		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_INSERT_M0200, sqlExec);
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(tantouBean.getHanyou1_cd());
            exCstmt.setStringIn(tantouBean.getHanyou2_cd());
            exCstmt.setStringIn(tantouBean.getHanyou4_cd());
            exCstmt.setStringIn(tantouBean.getSystem_kbn());
            exCstmt.setStringIn(form.getUser_id());
                   
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
	 * M24_ユーザ業務マスタの削除 <br>
	 * 
	 * @exception SQLException
	 */
	public void setDeleteM2400() throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_DELETE_M2400, sqlExec);
            exCstmt.setStringIn(form.getUser_id());
            
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
	 * M24_ユーザ業務マスタの登録 <br>
	 * 
	 * @exception SQLException
	 */
	public void setInsertM2400(WorkFlowListBean wf_bean) throws SQLException {

        ResultSet rs = null;
		try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_INSERT_M2400, sqlExec);
            exCstmt.setStringIn(user_bean.getComDaiko_userId());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(form.getUser_id());
            exCstmt.setStringIn(wf_bean.getSystem_kbn());
            exCstmt.setStringIn(wf_bean.getSateikaisya_cd());
            exCstmt.setStringIn(wf_bean.getPattern_id());
            exCstmt.setStringIn(wf_bean.getDefault_flg());
            
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
     * 担当組織一覧 情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai() throws SQLException {

        ResultSet rs = null;
        // ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_TANTO , sqlExec);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(user_maintenance_bean.getUser_id());
        exCstmt.setResultSet(RESULTSET);
        
        try {
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            
            int i = 0;
            if (form.getAr_meisai().size() > 0) {
            	i = form.getAr_meisai().size();
            }
            
            while ( rs.next() ) {
                
            	TantouBean listBean = new TantouBean();
                
                // (汎用１)コード
            	listBean.setHanyou1_cd(rs.getString(BUNRUI1));
            	
                // (汎用１)名称
            	listBean.setHanyou1_nm(rs.getString(BUNRUI1_NM));
            	
                // (汎用２)コード
            	listBean.setHanyou2_cd(rs.getString(BUNRUI2));
            	
                // (汎用２)名称
            	listBean.setHanyou2_nm(rs.getString(BUNRUI2_NM));
        		
				// (汎用４)コード
				listBean.setHanyou4_cd(rs.getString(HONBU_CD));

				// (汎用４)名称
				listBean.setHanyou4_nm(rs.getString(HONBU_NM));
    		
				// 基幹システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));

				// 処理対象外フラグ(初期値は処理対象外"1"を入れる)
				listBean.setTaisyogaiFlg(GS.ON);

				form.getAr_meisai().add(i, listBean);
				i++;
            }
            form.setView(form.getAr_meisai().size() + 1);
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
	 * 最新の基準日を取得 <br>
	 *
	 * @exception SQLException
	 */
	public void getKijyunbi() throws SQLException {
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_KIJYUNBI, sqlExec);
		exCstmt.setStringIn("");
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setSaishinYm(rs.getString(KIJUN_BI));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 対象ユーザのメール配信先を取得
	 * @throws Exception 
	 */
	public void getHaishinsaki() throws Exception {
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_HAISHIN, sqlExec);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
		exCstmt.setStringIn(user_maintenance_bean.getUser_id());
		exCstmt.setStringIn(form.getSaishinYm());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			int i = 0;

			// ActionForm に取得値を格納
			List<HashMap<String, String>> ar_haishin = new ArrayList<HashMap<String, String>>();
			UserTorokuBss bss = new UserTorokuBss(appContext);

			while (rs.next()) {

				HashMap<String, String> hm = new HashMap<String, String>();
				//id
				hm.put(ID, Function.getStringOfInt(i));
				//システム区分
				hm.put(SYSTEM_KBN_H, rs.getString(SYSTEM_KBN_H));
				
				//汎用1フラグ
				hm.put(HANYOU1FLG, rs.getString(HANYOU1FLG));
				//汎用1コード
				hm.put(HANYOU1, rs.getString(HANYOU1));
				//汎用1名
				hm.put(HANYOU1_NM, rs.getString(HANYOU1_NM));
				//汎用2フラグ
				hm.put(HANYOU2FLG, rs.getString(HANYOU2FLG));
				//汎用2コード
				hm.put(HANYOU2, rs.getString(HANYOU2));
				//汎用2名
				hm.put(HANYOU2_NM, rs.getString(HANYOU2_NM));
				//汎用3コード
				hm.put(HANYOU3FLG, rs.getString(HANYOU3FLG));
				//汎用3名
				hm.put(HANYOU3, rs.getString(HANYOU3));
				//汎用3フラグ
				hm.put(HANYOU3_NM, rs.getString(HANYOU3_NM));

				if((ar_haishin == null || ar_haishin.size() == 0) || bss.reMeilSentakuChk(hm,ar_haishin)){
					// 重複がない場合、配列に取得情報を格納
					ar_haishin.add(i, hm);
					i++;
				}
			}

			// ActionForm に明細を格納
			form.setAr_haishinsaki(ar_haishin);
			// セッション格納する
			user_maintenance_bean.setHyoujiSoushinList(ar_haishin);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * メール配信先マスタから対象ユーザのレコードを削除
	 * @throws SQLException
	 */
	public void setDeleteM3000() throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_DELETE_M3000, sqlExec);
		exCstmt.setStringIn(user_maintenance_bean.getUser_id());

		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}	

	/**
	 * メール配信先マスタに対象ユーザのレコードを追加
	 * @throws SQLException
	 */
	public void setInsertM3000(HashMap<String, String> hm) throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_INSERT_M3000, sqlExec);
		
		exCstmt.setStringIn(user_maintenance_bean.getUser_id());	// 統合ID
		exCstmt.setStringIn(hm.get(SYSTEM_KBN_H));					// システム区分
		exCstmt.setStringIn(hm.get(HANYOU1));						// 査定会社コード
		exCstmt.setStringIn(hm.get(HANYOU2));						// 配信部門コード
		exCstmt.setStringIn(hm.get(HANYOU3));						// 配信部コード
		exCstmt.setStringIn(user_bean.getComUserId());				// 登録更新ユーザID
		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}	

	/**
	 * ログインユーザの担当組織を取得する（コードのみ）
	 * @throws SQLException
	 */
	public void getLoginUserSoshikiList() throws SQLException {
		// ResultSet取得
		ResultSet rs = null;
		try{
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0200_2, sqlExec);
			exCstmt.setStringIn(user_bean.getComUserId());
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			List<HashMap<String, String>> loginUserSoshiki = new ArrayList<HashMap<String, String>>();
			int i = 0;

			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put(SYSTEM_KBN ,rs.getString(SYSTEM_KBN));// 基幹システム区分 
				hm.put(HANYOU1 ,rs.getString(SATEI_KAISHA_CD));// 査定会社コード
				hm.put(HANYOU2 ,rs.getString(TANTOU_BUMON_CD));// 担当部門コード
				hm.put(HANYOU4 ,rs.getString(TANTOU_HONBU_CD));// 本部コード

				loginUserSoshiki.add(i, hm);
				i++;
			}

			form.setAr_loginUserSoshiki(loginUserSoshiki);

		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}
	}

	/**
	 * 部門・部に対する本部を取得する<br>
	 * （メール配信先一覧・担当組織一覧入力チェック）<br>
	 * 
	 * @throws SQLException 
	 */
	public ArrayList<String> getHonbuCd(String system_kbn, String hanyou1_cd, String hanyou2_cd, String hanyou3_cd) throws SQLException{
		// 本部リスト（返却用）
		ArrayList<String> honbuList = new ArrayList<String>();

		// ResultSet取得
		ResultSet rs = null;
		try{
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7107_SELECT_HONBU, sqlExec);
			exCstmt.setStringIn(system_kbn);// システム区分
			exCstmt.setStringIn(hanyou1_cd);// 汎用1コード
			exCstmt.setStringIn(hanyou2_cd);// 汎用2コード
			exCstmt.setStringIn(hanyou3_cd);// 汎用3コード
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			while ( rs.next() ) {
				honbuList.add(rs.getString(HONBU_CD));
			}

			return honbuList;
		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}		
	}

}
