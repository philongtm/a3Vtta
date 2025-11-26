/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/02/25		SSC				新規作成
002		2015/12/25		SSC				BP201601002 障害対応（督促メール送信機能）
003		2016/03/23		SSC				BJ201602002_部門廃止対応（一次）
******************************************************************************/

package app.syokai.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.form.TokusokumailForm;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* OS6105_督促メール送信選択 DBアクセスクラス
*/
public class TokusokumailDbAcc extends CommonDbAcc {

	private AppContext appContext						= null;				// ＡＰＰコンテキスト
	private SessionData cmnData							= null;				// 共通セッションデータ
	private UserBean user_bean							= null;				// ユーザ情報
	private TorihikisakiBean tori_bean					= null;				// 取引先情報
	private TokusokumailForm form						= null;			// アクションフォーム
	//Resultset用文字列
	private static final String BUMON_CD				= "bumon_cd";
	private static final String EMAIL_ADDR				= "email_addr";
	private static final String TOGO_ID					= "togo_id";
	private static final String BU_CD					= "bu_cd";

	private static final String HAISINZUMI_FLG			= "N";	//配信済フラグ'N'：未配信

	private static final String H_DAIKO					= "( 代行 ";
	private static final String H_DAIKO_EN				= "( proxy ";

	private static final String ID						= "id";				//id
	private static final String RADIO_ID				= "radio_id";		//ラジオボタンのID
	private static final String HANYOU1					= "hanyou1";		//汎用1
	private static final String SOSHIKI					= "soshiki";		//組織
	private static final String SINTYOKU 				= "sintyoku";		//進捗
	private static final String ANKEN_NO 				= "anken_no";		// 案件No.
	private static final String ORA 					= "ora";
	private static final String SATEI_KAISHA_CD 		= "satei_kaisha_cd";// 査定会社コード
	private static final String PHASE					= "phase";			// フェーズ(名)
	private static final String PHASE_ID				= "phase_id";			// フェーズ（ID）
	private static final String TANTOU					= "tantou";			// 担当者
	private static final String DAIKO					= "daiko";			// 代行者
	private static final String TANTOU_ID				= "tanto_user";		// 担当者ID
	private static final String DAIKO_USER_ID			= "daiko_user_id";	// 代行者ID
	private static final String SYORI					= "syori";			// 処理
	private static final String SYORI_DT				= "syori_dt";		// 処理日時
	private static final String TOROKU_POINT 			= "toroku_point";	// 登録箇所
	private static final String COMMENT_VAL 			= "comment_val";	// コメント内容
	private static final String LINK_FLG 				= "link_flg";		// リンクフラグ
	private static final String OPE_KBN 				= "ope_kbn";		// 表示フラグ
	private static final String SYORI_DT_TIL 			= "syori_dt_til";	// 処理日時タイトル
	private static final String SATEI_KI				= "satei_ki";		// 査定期
	private static final String KIJUNBI_KBN				= "kijunbi_kbn";	// 基準日区分
	private static final String KIJUN_BI				= "kijun_bi";		// 基準日
	private static final String HAISHIN_DT				= "haishin_dt";		// 配信日時
	private static final String HOZON_DT				= "hozon_dt";		// 保存日時

	private static final String NYURYOKU_TENSOU 		= "50";				// 50：転送
	private static final String NYURYOKU_SASHIMODOSHI	= "60";				// 60：差戻

	private static final String SP_SS_OS6105_SELECT_MAESATEI = "SP_SS_OS6105_SELECT_MAESATEI"; //直前の査定期の有無を取得する(滞留)
	private static final String SP_SS_OS6105_SELECT_MAESATEI2 = "SP_SS_OS6105_SELECT_MAESATEI2"; //直前の査定期の有無を取得する（査定）
	private static final String SP_SS_OS6105_SELECT_SOSHIKI = "SP_SS_OS6105_SELECT_SOSHIKI";	//取引先に対し滞留判定を行った組織を取得する。
	private static final String SP_SS_OS6105_SELECT_T1300 = "SP_SS_OS6105_SELECT_T1300";		//取引先の進捗一覧を取得する。
	private static final String SP_SS_OS6105_SELECT_T1400 = "SP_SS_OS6105_SELECT_T1400";		//取引先の査定案件を取得する
	private static final String SP_SS_OS6105_SELECT_TANTO = "SP_SS_OS6105_SELECT_TANTO";					//担当者の一覧取得用プロシージャ
	private static final String SP_SS_OS_SELECT_KENGEN = "SP_SS_OS_SELECT_KENGEN";				//ユーザ権限取得
	private static final String SP_SS_O_SELECT_SYOZOKU = "SP_SS_O_SELECT_SYOZOKU";				//担当者の所属組織取得用プロシージャ
	private static final String SP_SS_OS_SELECT_HAISHINYMD = "SP_SS_OS_SELECT_HAISHINYMD";		//最新の督促メール配信日時を取得
	private static final String SP_SS_O_INSERT_T2700 = "SP_SS_O_INSERT_T2700";					//T27_督促メール配信登録用プロシージャ
	private static final String SP_SS_TOKUSOKUMAIL = "SP_SS_TOKUSOKUMAIL";						//督促メール配信
	private static final String SP_SS_OS_SELECT_HOZONYMD = "SP_SS_OS_SELECT_HOZONYMD";			//最新の督促メール保存日時を取得

	//送信先選択セレクトボックス
	private static final String KBN_HYOUJI_VAL 			= "KBN_HYOUJI_VAL";
	private static final String KBN_VAL				= "kbn_val";
	private static final String SOUSHINBU				= "t_mail_address";
	private static final String SP_SS_OL_SELECT_P0200	= "SP_SS_OL_SELECT_P0200";


	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String satei_kaisha_cd;		// 査定会社コード
	private String systemkbn;			// システム区分
	private String anken_no;			// 案件No
	private String phase;				// フェーズ
	private String status;				// ステータス
	private String taisyo_ym;			// 対象年月
	private String comLangMode;			// 共)言語モード


	/**
	 * コンストラクタ
	 *
	 * @param sqlExec
	 * @param appLog
	 */
	public TokusokumailDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		tori_bean = cmnData.getTori_bean();
		form = (TokusokumailForm)appContext.getActionForm();
		comLangMode = cmnData.getComLangMode();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
		systemkbn = tori_bean.getSystem_kbn();
		satei_kaisha_cd = tori_bean.getSateikaisya_cd();
		anken_no = tori_bean.getAnken_no();
		phase = tori_bean.getPhase();
		status = tori_bean.getStatus();
		taisyo_ym = tori_bean.getTaisyo_ym();
	}

	/**
	 * 変数初期化
	 */
	public void initialize() {
		// INパラメータ
		comLangMode = GS.EMPTY_CHARCTER;
		userId = GS.EMPTY_CHARCTER;
		satei_kaisha_cd = GS.EMPTY_CHARCTER;
		systemkbn = GS.EMPTY_CHARCTER;
	}


	/**
	 * 送信先選択部【リスト】を取得 <br>
	 *
	 * @exception SQLException
	 */
	public void getSoushinbuList() throws SQLException {

		// ResultSet取得
		ResultSet rs = null;
		try{
			// ExCallableStatement生成
			ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
			exCstmt.setStringIn(SOUSHINBU);
			exCstmt.setStringIn(cmnData.getComLangMode());
			exCstmt.setStringIn(systemkbn);
			exCstmt.setResultSet(RESULTSET);

			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> soushinbu = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				soushinbu.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
			}

			form.setAr_soushinbu(soushinbu);

		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}



	/**
	 * 担当者名編集処理<br>
	 *
	 * @param tanto 担当者名
	 * @param daiko 代行者名
	 * @return 編集された担当名
	 */
	protected String getTanto_nm(String tanto,String daiko) throws SQLException{
		StringBuffer tanto_nm = new StringBuffer(Function.trim(tanto));
		if(!Function.trim(daiko).equals(GS.EMPTY_CHARCTER)){
			//代行者名日本語 or 代行者名英語が存在する場合
			if(comLangMode.equals(GS.LANG_JA)){
				tanto_nm.append(H_DAIKO);
			}else{
				tanto_nm.append(H_DAIKO_EN);
			}
			tanto_nm.append(Function.trim(Function.trim(daiko)))
					.append(GS.KAKKO_MIGI);
		}
		return tanto_nm.toString();
	}

	/**
	 * 直前の査定期の有無を取得する（滞留判定） <br>
	 *
	 * @exception SQLException
	 */
	public void getMaeSateikiT() throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_MAESATEI, sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getMise_cd());
		cstmt.setStringIn(tori_bean.getSatei_ki());
		cstmt.setStringIn(tori_bean.getTaisyo_ym());
		cstmt.setStringIn(tori_bean.getKanjo_cd());
		cstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			int count = 0;
			while(rs.next()) {
				if(count == 0 && !tori_bean.getSatei_ki().equals(rs.getString(SATEI_KI))){
					// 1件目で査定期が異なる場合
					form.setMae_sateiki(rs.getString(SATEI_KI));
					form.setMae_kijunbi(rs.getString(KIJUN_BI));
					form.setMae_kijunbi_kbn(rs.getString(KIJUNBI_KBN));
					break;
				} else if(count == 1 ){
					// 2件以上の場合、直前の査定期あり
					form.setMae_sateiki(rs.getString(SATEI_KI));
					form.setMae_kijunbi(rs.getString(KIJUN_BI));
					form.setMae_kijunbi_kbn(rs.getString(KIJUNBI_KBN));
					break;
				}
				count++;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 直前の査定期の有無を取得する（査定） <br>
	 *
	 * @exception SQLException
	 */
	public void getMaeSateikiS() throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_MAESATEI2, sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getMise_cd());
		cstmt.setStringIn(tori_bean.getSatei_ki());
		cstmt.setStringIn(tori_bean.getTaisyo_ym());
		cstmt.setStringIn(tori_bean.getKanjo_cd());
		cstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			int count = 0;
			while(rs.next()) {
				// 1件目で査定期が異なる場合(査定未実施)
				if(count == 0 && !tori_bean.getSatei_ki().equals(rs.getString(SATEI_KI))){
					form.setMae_sateiki(rs.getString(SATEI_KI));
					form.setMae_kijunbi_kbn(rs.getString(KIJUNBI_KBN));
					form.setMae_kijunbi(null);
					break;
				} else if(count == 1 ){
					// 2件以上の場合
					form.setMae_sateiki(rs.getString(SATEI_KI));
					form.setMae_kijunbi_kbn(rs.getString(KIJUNBI_KBN));
					form.setMae_kijunbi(null);
					break;
				}
				count++;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}


	/**
	 * 取引先に対し滞留判定を行った組織を取得する。 <br>
	 *
	 * @exception SQLException
	 */
	public void getSosikiList() throws SQLException {

		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_SOSHIKI, sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getMise_cd());
		cstmt.setStringIn(form.getMae_sateiki());
		cstmt.setStringIn(form.getMae_kijunbi_kbn());
		cstmt.setStringIn(tori_bean.getKanjo_cd());
		cstmt.setStringIn(comLangMode);
		cstmt.setResultSet(RESULTSET);

		try {
			//SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			// 組織情報リスト
			List<Map<String, String>> ar_sosiki = new ArrayList<Map<String, String>>();
			int i = 0;
			while (rs.next()) {
				// 組織情報
				Map<String, String> sosiki = new HashMap<String, String>();
				// ID
				sosiki.put(ID, Function.getStringOfInt(i));
				// 汎用1
				sosiki.put(HANYOU1, rs.getString(SATEI_KAISHA_CD));
				// 組織
				sosiki.put(SOSHIKI, rs.getString(ORA));
				// 進捗
				sosiki.put(SINTYOKU, rs.getString(SINTYOKU));
				// 案件No.
				sosiki.put(ANKEN_NO, rs.getString(ANKEN_NO));

				ar_sosiki.add(i, sosiki);
				i++;
			}

			form.setAr_sosiki(ar_sosiki);

		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}

	}

	/**
	 * 取引先の進捗一覧を取得する <br>
	 *
	 * @exception SQLException
	 */
	public void getSinchokuList(String srh_anken_no) throws SQLException {
		// 組織にはデータがない場合
		if (!form.getKarento_tab().equals("2") && (form.getAr_sosiki()==null || form.getAr_sosiki().size()==0)) {
			return;
		}
		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_T1300, sqlExec);
		cstmt.setStringIn(comLangMode);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getBunrui2());
		cstmt.setStringIn(srh_anken_no);
		cstmt.setResultSet(RESULTSET);

		try {
			//SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			List<HashMap<String, String>> ar_meisai = new ArrayList<HashMap<String, String>>();

			int i = 0;
			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();
				//id
				hm.put(ID, Function.getStringOfInt(i));
				//ラジオボタンのID
				hm.put(RADIO_ID, Function.getStringOfInt(i));
				// フェーズ
				hm.put(PHASE, rs.getString(PHASE));
				hm.put(PHASE_ID, rs.getString(PHASE_ID));
				// 汎用1
				hm.put(HANYOU1, rs.getString(HANYOU1));
				// 組織
				hm.put(SOSHIKI, rs.getString(ORA));
				// 担当者
				hm.put(TANTOU, getTanto_nm(rs.getString(TANTOU),rs.getString(DAIKO)));
				// 担当者ID
				hm.put(TANTOU_ID, rs.getString(TANTOU_ID));
				// 代行者ID
				hm.put(rs.getString(DAIKO_USER_ID),rs.getString(DAIKO_USER_ID));
				// 処理
				hm.put(SYORI, rs.getString(SYORI));
				// 処理日時
				hm.put(SYORI_DT, rs.getString(SYORI_DT));
				// 査定案件Nｏ．
				hm.put(ANKEN_NO, rs.getString(ANKEN_NO));
				// 入力区分
				hm.put(OPE_KBN, rs.getString(OPE_KBN));
				// 登録箇所
				hm.put(TOROKU_POINT, rs.getString(TOROKU_POINT));
				// コメント内容
				hm.put(COMMENT_VAL, rs.getString(COMMENT_VAL));
				// 処理日時タイトル
				if (i == 0) {
					form.setSyouri_dt_t(rs.getString(SYORI_DT_TIL));
				}
				// リンク表示フラグ制御
				String link_flg = "0";
				if (NYURYOKU_TENSOU.equals(rs.getString(OPE_KBN))
					|| NYURYOKU_SASHIMODOSHI.equals(rs.getString(OPE_KBN))) {
					link_flg = "1";
				}

				hm.put(LINK_FLG, link_flg);

				// 明細配列に取得情報を格納
				ar_meisai.add(i, hm);
				i++;
			}

			// ActionForm に明細を格納
			form.setView(i++);
			form.setAr_sinchoku(ar_meisai);
			// ページ設定
			form.setPager(ar_meisai);


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
	 * 取引先の査定案件を取得する<br>
	 *
	 * @exception SQLException
	 */
	public void getAnkenNo() throws SQLException {
		// 査定案件Noリスト
		List<HashMap<String, String>> ar_anken_no = new ArrayList<HashMap<String, String>>();

		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_T1400, sqlExec);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getMise_cd());
		cstmt.setStringIn(form.getMae_sateiki());
		cstmt.setStringIn(form.getMae_kijunbi_kbn());
		cstmt.setStringIn(tori_bean.getKanjo_cd());
		cstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			while ( rs.next() ) {
				HashMap<String, String> hm = new HashMap<String, String>();
				hm.put(ANKEN_NO, rs.getString(ANKEN_NO));
				ar_anken_no.add(hm);
			}

			form.setAr_anken_no(ar_anken_no);

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
	 * 担当者一覧を表示 <br>
	 *
	 * @exception SQLException
	 */
	public void getTantoIchiran() throws SQLException {
		List<Map<String, String>> ar_tanto = new ArrayList<Map<String, String>>();  // 担当者一覧【配列】
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6105_SELECT_TANTO, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(tori_bean.getBu_cd());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAIL Address
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
			}
			form.setAr_Tanto(ar_tanto);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 担当者の所属組織取得 <br>
	 *
	 * @param String 担当ユーザID
	 * @exception SQLException
	 */
	public void getTanto_Syozoku(String tanto) throws SQLException {
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_SYOZOKU, sqlExec);
		exCstmt.setStringIn(tanto);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setTanto_bumon_cd(rs.getString(BUMON_CD));
				form.setTanto_bu_cd(rs.getString(BU_CD));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * ユーザ権限有無取得 <br>
	 *
	 * @param RirekiBean
	 * @return boolean
	 * @exception SQLException
	 */
	public boolean isUserKengen(String tanto_user_id) throws SQLException {
		boolean result = false;
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_KENGEN, sqlExec);
		exCstmt.setStringIn(tanto_user_id);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(status);
		exCstmt.setStringIn(tori_bean.getBu_cd());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			if(rs.next()) {
				result = true;
			}
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 最新の督促メール配信日時を取得 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void getHaishinDt(String tokuAnknenNo) throws SQLException {
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_HAISHINYMD, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(tokuAnknenNo);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setHaishin_Dt(rs.getString(HAISHIN_DT));
			} else {
				// 配信日時がない
				if("Ja".equals(cmnData.getComLangMode())){
					form.setHaishin_Dt("なし");
				} else {
					form.setHaishin_Dt("No Data");
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 最新の督促メール保存日時を取得 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void getHozonDt(String tokuAnknenNo) throws SQLException {
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_HOZONYMD, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(tokuAnknenNo);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setHozon_Dt(rs.getString(HOZON_DT));
			} else {
				// 保存日時がない
				if("Ja".equals(cmnData.getComLangMode())){
					form.setHozon_Dt("なし");
				} else {
					form.setHozon_Dt("No Data");
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * T27_督促メール配信の登録 <br>
	 *
	 * @param  String 配信先担当者
	 * @param  String ログインユーザ
	 * @exception SQLException
	 */
	public void insT27(String haishinsaki,String upd_user) throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T2700, sqlExec);
		exCstmt.setStringIn(anken_no);					// 案件NO.
		exCstmt.setStringIn(satei_kaisha_cd);			// 査定会社コード
		exCstmt.setStringIn(taisyo_ym);					// 年月
		exCstmt.setStringIn(phase);						// フェーズ
		exCstmt.setStringIn(status);					// ステータス
		exCstmt.setStringIn(form.getTanto_bumon_cd());	// 配信先部門
		exCstmt.setStringIn(form.getTanto_bu_cd());		// 配信先部
		exCstmt.setStringIn(haishinsaki);				// 配信先担当者
		exCstmt.setStringIn(upd_user);					// 依頼元担当者
		exCstmt.setStringIn(HAISINZUMI_FLG);			// 配信済みフラグ
		exCstmt.setStringIn(upd_user);					// 登録更新ユーザID

		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}

	/**
	 * 督促メール配信 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void sendTokusokuMail(String upd_user) throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_TOKUSOKUMAIL, sqlExec);
		exCstmt.setStringIn(upd_user);					// 配信元ユーザID
		exCstmt.setStringIn(userId);					// 配信先ユーザID
		exCstmt.setStringIn(systemkbn);	// システム区分
		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}
}