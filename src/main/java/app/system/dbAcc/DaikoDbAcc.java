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
import app.system.form.DaikoForm;
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
import java.util.List;
import java.util.Map;

/**
* OS7101_代行設定 DBアクセスクラス <br>
*/
public class DaikoDbAcc extends CommonDbAcc {
	
	private AppContext appContext 	= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 	= null;				// 機能共通セッション
    private UserBean user_bean 	= null;				// ユーザ情報
    private DaikoForm form 		= null;				// アクションフォーム

	private static final String ALL_GSS 					= "1"; 		// GSSのみ
	private static final String NONE_GSS 					= "2"; 		// GSSなし
	private static final String MIX_GSS 					= "3"; 		// GSS混在
	
	private static final String FLG_DAIKO 				= "1"; 		// 代行フラグ
    private static final String FLG_DEL_UNDELETE			= "0";		// 未削除
    private static final String FLG_DEL_DELETED			= "1";		// 削除済

    //Resultset用文字列    
	private static final String CNT 						= "cnt"; 			// 件数
	private static final String SYSTEM_KBN 				= "system_kbn"; 	// システム区分
	private static final String EMAIL_ADDR 				= "email_addr"; 	// E-MAILＡｄｄｒｅｓｓ
	private static final String USER_NM 					= "user_nm"; 		// 代行者名
	private static final String HIDAIKO_ID 				= "hidaiko_id"; 	// 被代行者ID
	private static final String DAIKO_ID 					= "daiko_id"; 		// 代行者ID
	private static final String TOGO_ID 					= "togo_id"; 		// 統合ID
	private static final String DEL_FLG 					= "del_flg"; 		// 削除フラグ
	private static final String DEL_ID 					= "del_id"; 		// ID
    
    private static final int TANTO_LIST_ID_SQL02			= 1;			//SQL02の区分ID
    private static final int TANTO_LIST_ID_SQL03			= 2;			//SQL03の区分ID
    private static final int TANTO_LIST_ID_SQL05			= 3;			//SQL05の区分ID
    private static final int TANTO_LIST_ID_SQL06			= 4;			//SQL06の区分ID
    private static final int TANTO_LIST_ID_SQL07			= 5;			//SQL07の区分ID

    private static final int DAIKO_ICHIRAN_INIT			= 1;			//代行者一覧取得（初期表示時）
    private static final int HIDAIKO_ICHIRAN_INIT		= 2;			//被代行者一覧取得（初期表示時）
    private static final int DAIKO_ICHIRAN_NORMOL		= 3;			//代行者一覧取得（初期表示時以外）
    private static final int HIDAIKO_ICHIRAN_NORMOL		= 4;			//被代行者一覧取得（初期表示時以外）

    private static final String SP_SS_OS7101_SELECT_KENGEN			= "SP_SS_OS7101_SELECT_KENGEN";			//処理権限チェックプロシージャ
    private static final String SP_SS_OS7101_SELECT_TANTO				= "SP_SS_OS7101_SELECT_TANTO";			//担当者一覧の取得プロシージャ
    private static final String SP_SS_OS7101_SELECT_TANTO01			= "SP_SS_OS7101_SELECT_TANTO01";		//担当者一覧の取得プロシージャ
    private static final String SP_SS_OS7101_SELECT_TANTO02			= "SP_SS_OS7101_SELECT_TANTO02";		//担当者一覧の取得プロシージャ
    private static final String SP_SS_OS7101_SELECT_M2400				= "SP_SS_OS7101_SELECT_M2400";			//業務フローパターンシステム区分【リスト】の取得プロシージャ
    private static final String SP_SS_OS7101_SELECT_DAIKO				= "SP_SS_OS7101_SELECT_DAIKO";			//代行者一覧の取得プロシージャ
    private static final String SP_SS_OS7101_SELECT_M0300				= "SP_SS_OS7101_SELECT_M0300";			//登録チェックプロシージャ
    private static final String SP_SS_OS7101_UPDATE_M0300				= "SP_SS_OS7101_UPDATE_M0300";			//M03_代行設定マスタ（SSM_DAIKO）の更新プロシージャ
    private static final String SP_SS_OS7101_INSERT_M0300				= "SP_SS_OS7101_INSERT_M0300";			//M03_代行設定マスタ（SSM_DAIKO）の登録プロシージャ
    private static final String SP_SS_OS7101_SELECT_T2200				= "SP_SS_OS7101_SELECT_T2200";			//代行排他チェックプロシージャ
    
    // INパラメータ
    private String langMode;    // 共)言語モード)

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public DaikoDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (DaikoForm)appContext.getActionForm();

        //ビーンの値を変数に設定
        langMode = cmnData.getComLangMode();
    }
    
    /**
     * 変数初期化 <br>
     */
    public void initialize() {
        // INパラメータ
    }

    /**
     * 担当者一覧を表示 <br>
     * 
     * @param processId 処理ID
     * @exception SQLException
     */
    public void getTantoIchiran(int processId) throws SQLException {
		// 担当者一覧【配列】
		List<Map> ar_tanto = new ArrayList<Map>();
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		// パラメータのSQL_IDを利用して各プロシージャを実行し、担当者一覧配列を取得する。
		switch (processId) {
		case TANTO_LIST_ID_SQL02:
			// ExCallableStatement生成
			cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_TANTO, sqlExec);
			cstmt.setStringIn(user_bean.getComUserId());
			cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
			cstmt.setStringIn(user_bean.getComSystemManager_flg());
			// 被代行者.担当者一覧【配列】をセットする。
			form.setAr_hidaiko_tanto(ar_tanto);
			break;
		case TANTO_LIST_ID_SQL03:
			// ExCallableStatement生成
			cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_TANTO, sqlExec);
			cstmt.setStringIn(user_bean.getComUserId());
			cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
			cstmt.setStringIn(user_bean.getComSystemManager_flg());
			// 被代行者.担当者一覧【配列】をセットする。
			form.setAr_hidaiko_tanto(ar_tanto);
			break;
		case TANTO_LIST_ID_SQL05:
			// ExCallableStatement生成
			cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_TANTO01, sqlExec);
			cstmt.setStringIn(form.getSelectedHidaikoshaId());
			cstmt.setStringIn(ALL_GSS);
			cstmt.setStringIn(user_bean.getComUserId());
			// 代行者.担当者一覧【配列】をセットする。
			form.setAr_daiko_tanto(ar_tanto);
			break;
		case TANTO_LIST_ID_SQL06:
			// ExCallableStatement生成
			cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_TANTO01, sqlExec);
			cstmt.setStringIn(form.getSelectedHidaikoshaId());
			cstmt.setStringIn(NONE_GSS);
			cstmt.setStringIn(user_bean.getComUserId());
			// 代行者.担当者一覧【配列】をセットする。
			form.setAr_daiko_tanto(ar_tanto);
			break;
		case TANTO_LIST_ID_SQL07:
			// ExCallableStatement生成
			cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_TANTO02, sqlExec);
			cstmt.setStringIn(form.getSelectedHidaikoshaId());
			cstmt.setStringIn(user_bean.getComUserId());
			// 代行者.担当者一覧【配列】をセットする。
			form.setAr_daiko_tanto(ar_tanto);
			break;
		default:
			break;
		}
		cstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAILＡｄｄｒｅｓｓ
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
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
     * 承認権限を保持しているかチェックする <br>
     * 
     * @return 一次査定対象FLG
     * @exception SQLException
     */
    public int gelKengenCheck() throws SQLException {
		// 件数
		int cnt = 0;

		// 承認権限を保持しているかチェックする。
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_KENGEN, sqlExec);
		// ユーザーID
		cstmt.setStringIn(user_bean.getComUserId());
		// データの件数
		cstmt.setIntOut(CNT);
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);

			cnt = cstmt.getInt(CNT);
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
		return cnt;
	}

    /**
     * 被代行者の保持する業務フローパターンシステム区分【リスト】をチェックする。 <br>
     * 
     * @return システム区分リストの種別 <br>
     *         "1" '01'：GSSのみ格納されている場合 <br>
     *         "2" '01'：GSSが存在しない場合 <br>
     *         "3" '01'：GSSが存在する 且つ（02'：FOCUS or 03'：MTSが存在する）場合 <br>
     * @exception SQLException
     */
    public String checkSystemKbnList() throws SQLException {
		// '01'：GSSがあるかどうかのフラグ
		boolean hasGss = false;
		// '02'：FOCUSがあるかどうかのフラグ
		boolean hasFocus = false;
		// '03'：MTSがあるかどうかのフラグ
		boolean hasMts = false;

		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_M2400, sqlExec);
		cstmt.setStringIn(form.getSelectedHidaikoshaId());
		cstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			// 業務フローパターンシステム区分【リスト】をチェックする。
			while (rs.next()) {
				String systemKbn = rs.getString(SYSTEM_KBN);
				if (GS.GSS.equals(systemKbn)) {
					// '01'：GSS
					hasGss = true;
				} else if (GS.FOCUS.equals(systemKbn)) {
					// '02'：FOCUS
					hasFocus = true;
				} else if (GS.MTS.equals(systemKbn)) {
					// '03'：MTS
					hasMts = true;
				}
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
		if (hasGss && !hasFocus && !hasMts) {
			// '01'：GSSのみ格納されている場合
			return ALL_GSS;
		} else if (!hasGss) {
			// '01'：GSSが存在しない場合
			return NONE_GSS;
		} else if (hasFocus || hasMts) {
			// '01'：GSSが存在する 且つ（02'：FOCUS or 03'：MTSが存在する）場合
			return MIX_GSS;
		}
		return null;
	}

    /**
     * 代行者(被代行者)一覧を取得する <br>
     * 
     * @param processId 処理ID
     * @exception SQLException
     */
    public void setDaikoList(int processId) throws SQLException {
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_DAIKO, sqlExec);
		// 担当者一覧【配列】
		List<Map> ar_daiko = new ArrayList<Map>();
		// パラメータのSQL_IDを利用して各プロシージャを実行し、担当者一覧配列を取得する。
		switch (processId) {
		case DAIKO_ICHIRAN_INIT:
			cstmt.setStringIn(user_bean.getComUserId());
			cstmt.setStringIn(langMode);
			cstmt.setStringIn(FLG_DAIKO);
			// 代行者一覧【配列】をセットする。
			form.setAr_daikosha(ar_daiko);
			break;
		case HIDAIKO_ICHIRAN_INIT:
			cstmt.setStringIn(user_bean.getComUserId());
			cstmt.setStringIn(langMode);
			cstmt.setStringIn(null);
			// 被代行者一覧【配列】をセットする。
			form.setAr_hidaikosha(ar_daiko);
			break;
		case DAIKO_ICHIRAN_NORMOL:
			cstmt.setStringIn(form.getSelectedHidaikoshaId());
			cstmt.setStringIn(langMode);
			cstmt.setStringIn(FLG_DAIKO);
			// 代行者一覧【配列】をセットする。
			form.setAr_daikosha(ar_daiko);
			break;
		case HIDAIKO_ICHIRAN_NORMOL:
			cstmt.setStringIn(form.getSelectedHidaikoshaId());
			cstmt.setStringIn(langMode);
			cstmt.setStringIn(null);
			// 被代行者一覧【配列】をセットする。
			form.setAr_hidaikosha(ar_daiko);
			break;
		default:
			break;
		}
		cstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			int i = 0;
			while (rs.next()) {
				// 代行者情報
				Map<String, String> daikosha = new HashMap<String, String>();
				// ID
				daikosha.put(DEL_ID, Function.getStringOfInt(i));
				if (DAIKO_ICHIRAN_INIT == processId || DAIKO_ICHIRAN_NORMOL == processId) {
					// 代行者ID
					daikosha.put(DAIKO_ID, rs.getString(DAIKO_ID));
				}
				// 被代行者ID
				daikosha.put(HIDAIKO_ID, rs.getString(HIDAIKO_ID));
				// 代行者名
				daikosha.put(USER_NM, rs.getString(USER_NM));
				// E-MAILＡｄｄｒｅｓｓ
				daikosha.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 明細配列に取得情報を格納
				ar_daiko.add(i, daikosha);
				i++;
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
     * 登録チェック <br>
     * 
	 * @return 削除フラグ
     * @exception SQLException
     */
	public String registCheck() throws SQLException {
		// 削除フラグ
		String delFlg = null;
		
        // 被代行者
        String hidaikosha = form.getSelectedHidaikoshaId();
        // 代行者
        String daikosha = form.getSelectedDaikoshaId();
        
        // 登録チェック
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_M0300, sqlExec);
        // 被代行者
        cstmt.setStringIn(hidaikosha);
        // 代行者
        cstmt.setStringIn(daikosha);

		cstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			if (rs.next()) {
				// 削除フラグ
				delFlg = rs.getString(DEL_FLG);
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
        return delFlg;
    }

	/**
     * M03_代行設定マスタ（SSM_DAIKO）の更新（代行設定時） <br>
     * 
     * @exception SQLException
     */
	public void updateM03() throws SQLException {
		
        // 更新ユーザID
        String userId = user_bean.getComUserId();
        // 被代行者
        String hidaikosha = form.getSelectedHidaikoshaId();
        // 代行者
        String daikosha = form.getSelectedDaikoshaId();
        
        // M03_代行設定マスタ（SSM_DAIKO）の更新（代行設定時）
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        cstmt = new ExCallableStatement(SP_SS_OS7101_UPDATE_M0300, sqlExec);
        // 削除フラグ
        cstmt.setStringIn(FLG_DEL_UNDELETE);
        // 更新ユーザID
        cstmt.setStringIn(userId);
        // 被代行者
        cstmt.setStringIn(hidaikosha);
        // 代行者
        cstmt.setStringIn(daikosha);

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
     * M03_代行設定マスタ（SSM_DAIKO）の登録 <br>
     * 
     * @exception SQLException
     */
	public void insertM03() throws SQLException {
		
        // 被代行者
        String hidaikosha = form.getSelectedHidaikoshaId();
        // 代行者
        String daikosha = form.getSelectedDaikoshaId();
        // ユーザID
        String userId = user_bean.getComUserId();
        
        // M03_代行設定マスタ（SSM_DAIKO）の更新（代行設定時）
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        cstmt = new ExCallableStatement(SP_SS_OS7101_INSERT_M0300, sqlExec);
        // 被代行者
        cstmt.setStringIn(hidaikosha);
        // 代行者
        cstmt.setStringIn(daikosha);
        // ユーザID
        cstmt.setStringIn(userId);

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
     * 代行排他チェック <br>
     * 
	 * @return 件数
     * @exception SQLException
     */
	public int daikoHaitaCheck() throws SQLException {
		// 件数
		int cnt = 0;
		
		// 代行者情報
		Map daikoshaInfo = (Map)form.getAr_daikosha().get(form.getDelId());
        // 代行者
        String daikosha = (String)daikoshaInfo.get(DAIKO_ID);
        
        // 登録チェック
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        cstmt = new ExCallableStatement(SP_SS_OS7101_SELECT_T2200, sqlExec);
        // 代行者
        cstmt.setStringIn(daikosha);

		cstmt.setIntOut(CNT);
        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
			// 件数
            cnt = cstmt.getInt(CNT);
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
        return cnt;
    }

	/**
     * M03_代行設定マスタ（SSM_DAIKO）の更新（削除時） <br>
     * 
     * @exception SQLException
     */
	public void deleteM03() throws SQLException {
		// 代行者情報
		Map daikoshaInfo = (Map)form.getAr_daikosha().get(form.getDelId());
        // 更新ユーザID
        String userId = user_bean.getComUserId();
        // 被代行者
        String hidaikosha = (String)daikoshaInfo.get(HIDAIKO_ID);
        // 代行者
        String daikosha = (String)daikoshaInfo.get(DAIKO_ID);
        
        // M03_代行設定マスタ（SSM_DAIKO）の更新（削除時）
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        cstmt = new ExCallableStatement(SP_SS_OS7101_UPDATE_M0300, sqlExec);
        // 削除フラグ
        cstmt.setStringIn(FLG_DEL_DELETED);
        // 更新ユーザID
        cstmt.setStringIn(userId);
        // 被代行者
        cstmt.setStringIn(hidaikosha);
        // 代行者
        cstmt.setStringIn(daikosha);

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