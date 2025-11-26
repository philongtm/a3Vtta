/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/03/20		SSC				新規作成
002		2016/02/18		SSC				BJ201602002_部門廃止対応（一次）
******************************************************************************/

package app.system.dbAcc;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.form.MailSoushinSentakuForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS7115 メール送信先選択 DBアクセスクラス <br>
 */
public class MailSoushinSentakuDbAcc extends CommonDbAcc {

	private SessionData cmnData = null;					// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private MailSoushinSentakuForm form = null;			// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private UserMaintenanceBean user_maintenance_bean;	// ユーザメンテナンスビーン
	private String workflowSystemkbn;					// 業務フローパターンシステム区分
	private String langMode;							// 共)言語モード

	// 検索条件のセレクトボックス取得用
	private static final String SATEI_KAISHA_CD			= "SATEI_KAISHA_CD";			// 査定会社コード(汎用1)
	private static final String SATEI_KAISHA_HYOUJI		= "satei_kaisha_hyouji";		// 査定会社コード名(汎用1表示用)
	private static final String HANYOU2  				= "hanyou2";					// 汎用2(表示用)
	private static final String HANYOU2_HY  			= "hanyou2_hy";					// 汎用2名(表示用)
	private static final String SYSTEM_KBN				= "SYSTEM_KBN";					// 基幹システム区分
	private static final String COMMON_OS61				= "common_OS61";				// KB.区分キー
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";
	private static final String KBN_VAL					= "kbn_val";


	// 一覧表示取得用
	private static final String KIJUN_BI 				= "KIJUN_BI";					// 基準日
	private static final String ID						= "id";							// id

	private static final String HANYOU1					= "hanyou1";					// 汎用1(表示用)
	private static final String HANYOU1_NM				= "hanyou1_nm";					// 汎用1名(表示用)
	private static final String HANYOU2_NM				= "hanyou2_nm";					// 汎用2名(表示用)
	private static final String HANYOU3					= "hanyou3";					// 汎用3(表示用)
	private static final String HANYOU3_NM				= "hanyou3_nm";					// 汎用3名(表示用)

	private static final String SATEI_KAISHA_NM			= "SATEI_KAISHA_NM";			// 査定会社名（取得用）
	private static final String BUMON_CD				= "BUMON_CD";					// 部門コード（取得用）
	private static final String BUMON_NM				= "BUMON_NM";					// 部門名（取得用）
	private static final String BU_CD					= "BU_CD";						// 部コード（取得用）
	private static final String BU_NM					= "BU_NM";						// 部名（取得用）


	private static final String SP_SS_OS7115_SELECT_M0200			= "SP_SS_OS7115_SELECT_M0200";
	private static final String SP_SS_OS7115_SELECT_BUNNRUI2		= "SP_SS_OS7115_SELECT_BUNNRUI2";
	private static final String SP_SS_OS_SELECT_KIJYUNBI			= "SP_SS_OS_SELECT_KIJYUNBI";
	private static final String SP_SS_OS7115_SELECT_ICHIRAN			= "SP_SS_OS7115_SELECT_ICHIRAN";

	public MailSoushinSentakuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (MailSoushinSentakuForm)appContext.getActionForm();

		workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
		langMode = cmnData.getComLangMode();
		user_maintenance_bean = cmnData.getUser_maintenance_bean();
	}

	/**
	 * 変数初期化 <br>
	 */
	public void initialize() {
		workflowSystemkbn = GS.EMPTY_CHARCTER;
		langMode = GS.EMPTY_CHARCTER;
	}

	/**
	 *
	 * 当画面の汎用項目ラベルを取得する <br>
	 *
	 * @throws SQLException
	 */
	public void getHanyouTitle() throws SQLException {
		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(COMMON_OS61,workflowSystemkbn,langMode);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyouTitle = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				ar_hanyouTitle.put(rs.getString(KBN_VAL),rs.getString(KBN_HYOUJI_VAL));
				i++;
			}
			// 汎用1ラベルを設定する
				form.setHanyou1Title(workflowSystemkbn);
			// 汎用2ラベルを設定する
			form.setHanyou2Title(ar_hanyouTitle.get("2"));
			// 汎用3ラベルを設定する
			form.setHanyou3Title(ar_hanyouTitle.get("3"));
		} finally {
			if (rs != null) {
				//Resultset close
				rs.close();
			}
		}
	}

	/**
	 *
	 * 汎用1セレクトボックスの設定値を取得する <br>
	 *
	 * @throws SQLException
	 */
	public void getHanyou1() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7115_SELECT_M0200, sqlExec);
		exCstmt.setResultSet(RESULTSET);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyou1 = new LinkedHashMap<String,String>();
			ar_hanyou1.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
			while ( rs.next() ) {
				//初期設定
				ar_hanyou1.put(rs.getString(SATEI_KAISHA_CD),rs.getString(SATEI_KAISHA_HYOUJI));
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
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7115_SELECT_BUNNRUI2, sqlExec);
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
	 * 最新の基準日を取得（最新年月で検索する場合） <br>
	 *
	 * @exception SQLException
	 */
	public void getKijyunbi() throws SQLException {
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_KIJYUNBI, sqlExec);
		exCstmt.setStringIn(form.getHanyou1());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setKensakuYm(rs.getString(KIJUN_BI));
			}
		} finally {
			if (rs != null) {
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
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS7115_SELECT_ICHIRAN, sqlExec);
		// 言語モード
		exCstmt.setStringIn(cmnData.getComLangMode());
		// 汎用1
		exCstmt.setStringIn(form.getHanyou1());
		// 汎用2
		exCstmt.setStringIn(form.getHanyou2());
		// 最新年月
		exCstmt.setStringIn(form.getKensakuYm());
		exCstmt.setResultSet(RESULTSET);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			List<HashMap<String, String>> ar_meisai = new ArrayList<HashMap<String, String>>();

			String maeHanyou1 = "";		// 前汎用1
			String maeHanyou2 = "";		// 前汎用2
			String maeHanyou3 = "";		// 前汎用3

			String imaHanyou1 = "";		// 現在の汎用1
			String imaHanyou2 = "";		// 現在の汎用2
			String imaHanyou3 = "";		// 現在の汎用3

			int i = 0; // idのカウント
			InputCheck inChk = new InputCheck();

			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();

				imaHanyou1 = rs.getString(SATEI_KAISHA_CD);
				imaHanyou2 = rs.getString(BUMON_CD);
				imaHanyou3 = rs.getString(BU_CD);

				if(!maeHanyou1.equals(imaHanyou1)){
					// 汎用1が前汎用1と異なる場合
					//システム区分
					hm.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
					//id
					hm.put(ID, Function.getStringOfInt(i));
					// 汎用1コード
					hm.put(HANYOU1,rs.getString(SATEI_KAISHA_CD));	// 汎用1
					// 汎用1名
					hm.put(HANYOU1_NM,rs.getString(SATEI_KAISHA_NM));
					// 汎用2コード
					hm.put(HANYOU2,"");
					// 汎用2名
					hm.put(HANYOU2_NM,"");
					// 汎用3コード
					hm.put(HANYOU3,"");
					// 汎用3名
					hm.put(HANYOU3_NM,"");

					// 明細配列に取得情報を格納
					ar_meisai.add(i, hm);
					i++;

					maeHanyou1 = new String(rs.getString(SATEI_KAISHA_CD));		// 前汎用1
					maeHanyou2 = "";											// 前汎用2
					hm = new HashMap<String, String>();
				}

				if(!maeHanyou2.equals(imaHanyou2)){
					// 汎用2が前汎用2と異なる場合
					//システム区分
					hm.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
					//id
					hm.put(ID, Function.getStringOfInt(i));
					// 汎用1コード
					hm.put(HANYOU1,rs.getString(SATEI_KAISHA_CD));	// 汎用1
					// 汎用1名
					hm.put(HANYOU1_NM,rs.getString(SATEI_KAISHA_NM));
					// 汎用2コード
					hm.put(HANYOU2,rs.getString(BUMON_CD));
					// 汎用2名
					hm.put(HANYOU2_NM,rs.getString(BUMON_NM));
					// 汎用3コード
					hm.put(HANYOU3,"");
					// 汎用3名
					hm.put(HANYOU3_NM,"");

					// 明細配列に取得情報を格納
					ar_meisai.add(i, hm);
					i++;

					maeHanyou2 = new String(rs.getString(BUMON_CD));			// 前汎用2
					maeHanyou3 = "";
					hm = new HashMap<String, String>();
				}

				if(GS.SATEIKAISYA_SJ.equals(imaHanyou1)){
					// 査定会社SJの場合のみ
					if(inChk.isNullBlank(maeHanyou3) &&!inChk.isNullBlank(imaHanyou3)){
						// 汎用3に値が入っているとき
						//システム区分
						hm.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
						//id
						hm.put(ID, Function.getStringOfInt(i));
						// 汎用1コード
						hm.put(HANYOU1,rs.getString(SATEI_KAISHA_CD));	// 汎用1
						// 汎用1名
						hm.put(HANYOU1_NM,rs.getString(SATEI_KAISHA_NM));
						// 汎用2コード
						hm.put(HANYOU2,rs.getString(BUMON_CD));
						// 汎用2名
						hm.put(HANYOU2_NM,rs.getString(BUMON_NM));
						// 汎用3コード
						hm.put(HANYOU3,rs.getString(BU_CD));
						// 汎用3名
						hm.put(HANYOU3_NM,rs.getString(BU_NM));

						// 明細配列に取得情報を格納
						ar_meisai.add(i, hm);
						i++;
					}
				}
			}

			// ActionForm に明細を格納
			form.setAr_meisai(ar_meisai);

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