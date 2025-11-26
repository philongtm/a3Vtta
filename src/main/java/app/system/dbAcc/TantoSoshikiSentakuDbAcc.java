/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0.67
更新履歴
No		日付			修正者			修正内容
001		2016/03/28		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.form.TantoSoshikiSentakuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS7116_担当組織選択 DBアクセスクラス <br>
 * 
 */
public class TantoSoshikiSentakuDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private SessionData cmnData = null;					// 機能共通セッション
	private UserMaintenanceBean user_maintenance_bean;	// ユーザメンテナンスビンー
	private UserBean user_bean = null;					// ユーザ情報
	private TantoSoshikiSentakuForm form = null;		// 担当組織選択アクションフォーム

	private static final String ID									= "ID";				// ID
	private static final String SYSTEM_KBN							="SYSTEM_KBN";
	private static final String SATEI_KAISHA_CD						="SATEI_KAISHA_CD";	// 査定会社コード
	private static final String HANYOU1								="HANYOU1";			// 汎用1
	private static final String HANYOU1_NM							="HANYOU1_NM";		// 汎用1名称
	private static final String BUNRUI1								="bunrui1";			// 分類1
	private static final String BUNRUI1_CD							="BUNRUI1_CD";		// 分類1
	private static final String BUNRUI1_NM							="BUNRUI1_NM";		// 分類1名称
	private static final String HANYOU2								="HANYOU2";			// 汎用2
	private static final String HANYOU2_NM							="HANYOU2_NM";		// 汎用2名称
	private static final String HANYOU2_HY							="hanyou2_hy";		// 汎用2名称（セレクトボックス）
	private static final String BUNRUI2_CD							="BUNRUI2_CD";		// 分類2
	private static final String BUNRUI2_NM							="BUNRUI2_NM";		// 分類2名称
	
	private static final String TANTOU_BUMON_CD						="TANTOU_BUMON_CD";	// 担当部門コード
	private static final String TANTOU_HONBU_CD						="TANTOU_HONBU_CD";	// 担当本部コード
	private static final String HONBU_CD							="HONBU_CD";		// 本部コード
	private static final String HONBU_NM							="HONBU_NM";		// 本部名称
	private static final String HANYOU3								="HANYOU3";			// 汎用4
	private static final String HANYOU3_NM							="HANYOU3_NM";		// 汎用4名称
	private static final String SATEI_KAISHA_HYOUJI  				="satei_kaisha_hyouji";	// 査定会社名称（表示用）

	// 対象ユーザの担当組織（分類1、分類2、本部）のコードを取得
	private static final String SP_SS_OS_SELECT_M0200_2				= "SP_SS_OS_SELECT_M0200_2";
	// ログインユーザの統括分類1（査定会社単位の権限）を取得
	private static final String SP_SS_OS7116_SELECT_M0200			= "SP_SS_OS7116_SELECT_M0200"; 
	// 汎用1セレクトボックスの設定値を取得
	private static final String SP_SS_OS_SELECT_M0200				= "SP_SS_OS_SELECT_M0200";
	// 汎用2セレクトボックスの設定値を取得
	private static final String SP_SS_OS_SELECT_BUNNRUI2			= "SP_SS_OS_SELECT_BUNNRUI2";
	// 検索結果 担当組織（分類1、分類2、本部）のコード・名称取得
	private static final String SP_SS_OS7116_SELECT_ICHIRAN 		= "SP_SS_OS7116_SELECT_ICHIRAN";

	/**
	 * コンストラクタ <br>
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public TantoSoshikiSentakuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		user_maintenance_bean = cmnData.getUser_maintenance_bean();
		form = (TantoSoshikiSentakuForm)appContext.getActionForm();

	}

	/**
	 * 対象ユーザの担当組織を取得する（コードのみ）
	 * @throws SQLException
	 */
	public void getTaiUserSoshikiList() throws SQLException {
		// ResultSet取得
		ResultSet rs = null;
		try{
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0200_2, sqlExec);
			exCstmt.setStringIn(user_maintenance_bean.getUser_id());
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			List<HashMap<String, String>> taiUserTantoList = new ArrayList<HashMap<String, String>>();
			int i = 0;

			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();

				hm.put(SYSTEM_KBN ,rs.getString(SYSTEM_KBN));	// 基幹システム区分 
				hm.put(HANYOU1 ,rs.getString(SATEI_KAISHA_CD));	// 査定会社コード
				hm.put(HANYOU2 ,rs.getString(TANTOU_BUMON_CD));	// 担当部門コード
				hm.put(HANYOU3 ,rs.getString(TANTOU_HONBU_CD));	// 本部コード

				taiUserTantoList.add(i, hm);
				i++;
			}

			form.setAr_taiUserTantoList(taiUserTantoList);

		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}
	}

	/**
	 * ログインユーザの統括分類1（査定会社単位の権限）を取得
	 * @throws SQLException
	 */
	public void getLogUserBunrui1List() throws SQLException {
		// ResultSet取得
		ResultSet rs = null;
		try{
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7116_SELECT_M0200, sqlExec);
			exCstmt.setStringIn(user_bean.getComUserId());
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			ArrayList<String> bunrui1List  = new ArrayList<String>();
			while ( rs.next() ) {
				bunrui1List.add(rs.getString(BUNRUI1));	// 統轄分類1
			}

			form.setAr_logUserBunrui1List(bunrui1List);

		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}
	}

	/**
	 * 汎用1【リスト】取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyou1() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0200, sqlExec);
		exCstmt.setStringIn(user_bean.getComUserId());
		exCstmt.setResultSet(RESULTSET);
		
		try {
			//SQL実行	
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyou1 = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				ar_hanyou1.put(rs.getString(SATEI_KAISHA_CD),rs.getString(SATEI_KAISHA_HYOUJI));
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
	 * 汎用2【リスト】取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyou2() throws SQLException {

		ResultSet rs = null;
		try{
			// ResultSet取得
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_BUNNRUI2, sqlExec);
			exCstmt.setStringIn(user_bean.getComUserId());
			exCstmt.setStringIn(form.getHanyou1());
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyou2 = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				ar_hanyou2.put(rs.getString(HANYOU2_HY),rs.getString(HANYOU2));	
				form.setSystemKbn(rs.getString(SYSTEM_KBN));
			}
			form.setAr_hanyou2(ar_hanyou2);
			
		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}
	}

	/**
	 * 検索結果 担当組織（分類1、分類2、本部）のコード・名称取得 <br>
	 * @throws SQLException
	 */
	public void getMeisai() throws SQLException {
		ResultSet rs = null;
		try{
			// ResultSet取得
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7116_SELECT_ICHIRAN, sqlExec);
			exCstmt.setStringIn(user_bean.getComUserId());		// ユーザID
			exCstmt.setStringIn(cmnData.getComLangMode());		// 言語モード
			exCstmt.setStringIn(form.getSystemKbn());			// システム区分
			exCstmt.setStringIn(form.getHanyou1());				// 汎用1
			exCstmt.setStringIn(form.getHanyou2());				// 汎用2
			exCstmt.setResultSet(RESULTSET);

			// SQL実行 
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			List<HashMap<String, String>> tantoList = new ArrayList<HashMap<String, String>>();
			int i = 0;

			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(ID ,Function.getStringOfInt(i));			// ID
				hm.put(SYSTEM_KBN ,rs.getString(SYSTEM_KBN));	// 基幹システム区分 
				hm.put(HANYOU1,rs.getString(BUNRUI1_CD));		// 汎用1コード
				hm.put(HANYOU1_NM ,rs.getString(BUNRUI1_NM));	// 汎用1名称
				hm.put(HANYOU2 ,rs.getString(BUNRUI2_CD));		// 汎用2コード
				hm.put(HANYOU2_NM ,rs.getString(BUNRUI2_NM));	// 汎用2名称
				hm.put(HANYOU3 ,rs.getString(HONBU_CD));		// 本部コード
				hm.put(HANYOU3_NM ,rs.getString(HONBU_NM));		// 本部名称

				tantoList.add(i, hm);
				i++;
			}

			form.setAr_meisai(tantoList);

		} finally {
			if (rs != null) {
				// Resultset close
				rs.close();
			}
		}
	}
}
