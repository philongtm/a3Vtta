/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/03		SSC				課題No.30 パフォーマンスアップ対応
003		2009/11/03		SSC				課題No.152 取戻ユーザチェック処理削除
004		2009/12/24		SSC				課題No.210 所在国検索変更対応
005		2009/01/19		SSC				課題No.236 フェーズプルダウン全参照システム区分対応
005		2014/02/24		SSC				案件No.D12883 米国SAP対応
006		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
00		2015/12/14		SSC				案件No.BP201512020 照会画面の部検索時不具合対応
******************************************************************************/

package app.syokai.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.form.SincyokuForm;
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
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS6103 進捗状況照会 DBアクセスクラス <br>
 */
public class SincyokuDbAcc extends CommonDbAcc {

	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private SincyokuForm form = null;					// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	private String workflowSystemkbn;					// 業務フローパターンシステム区分
	private String langMode;							// 共)言語モード
	private String userId;								// ユーザＩＤ
	// 未使用定数削除開始
	//private static final String SIHANKI		 		= "2";		// 四半期
	// 削除完了
	// 課題No.30
	// 追加開始
	private static final String DAIKO							= "( 代行 ";
	private static final String DAIKO_EN						= "( proxy ";
	// 追加完了

	private static final String SHOW            		= "show";						//区分キー（表示件数）
	private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";
	private static final String KBN_VAL				= "kbn_val";
	private static final String PHASE_KBN 			= "phase";						// KB.区分キー:フェーズ
	private static final String STATUS_KBN 			= "status";						// KB.区分キー:ステータス
	//課題No.210
	//修正開始
	//private static final String COUNTRY_NM			= "country_nm";					// 国名称
	private static final String WB_COUNTRY_NM			= "wb_country_nm";				// 国名称
	//private static final String COUNTRY_NM_HY			= "country_nm_hy";				// 国名称表示用
	private static final String COUNTRY_KENSAKU		= "country_kensaku";			// 国検索
	//修正完了
	private static final String COMMON_OS61			= "common_OS61";				// KB.区分キー
	private static final String SATEI_KAISHA_CD		= "SATEI_KAISHA_CD";			// 査定会社コード (汎用1)
	private static final String SATEI_KAISHA_HYOUJI	= "satei_kaisha_hyouji";		// 査定会社コード (汎用1表示用)
	private static final String HANYOU2  				= "hanyou2";					// 汎用2
	private static final String HANYOU2_HY  			= "hanyou2_hy";					// 汎用2(表示用)
	private static final String SYSTEM_KBN			= "SYSTEM_KBN";					// 基幹システム区分
	private static final String HANYOU3  				= "hanyou3";					// 汎用3
	private static final String HANYOU3_HY  			= "hanyou3_hy";					// 汎用3(表示用)
	private static final String SATEIKAISYA_CD		= "satei_kaisha_cd";			// 査定会社コード
	private static final String MISE_CD				= "mise_cd";					// 店コード
	private static final String SATEI_KI				= "satei_ki";					// 査定期
	private static final String KIKAN_TORI_CD			= "kikan_tori_cd";				// 勘定先CD
	private static final String TORI_NM				= "tori_nm";					// 勘定先名称
	// 課題No.30
	// 削除開始
	//private static final String HENSHU_HY				= "henshu_hy";					// 表示用編集値
	// 削除完了
	private static final String SANKO_SATEIKAISYA_CD_KEY	= "SATEIKAISYA_CD";			// 査定会社コード
	private static final String SANKO_SYSTEM_KBN_KEY		= "SYSTEM_KBN";				// システム区分
	private static final String SANKO_BUNRUI2_KEY 		= "BUNRUI2";				// 分類２
	private static final String HANKI_SIHANKI_KBN			= "hanki_sihanki_kbn";		// 半期四半期区分
	// 課題No.30
	// 追加開始
	private static final String SOSHIKI 					= "soshiki";				// 組織
	private static final String HYOJI_YM 					= "hyoji_ym";				// 表示用年月
	private static final String KANKEISHA_TANTOU 			= "kankeisha_tantou";		// 担当者
	private static final String KANKEISHA_DAIKO 			= "kankeisha_daiko";		// 代行者
	private static final String KANKEISHA_TANTOU_ID 		= "kankeisha_tantou_id";	// 担当者ID
	private static final String KANKEISHA_DAIKO_ID 			= "kankeisha_daiko_id";		// 代行者ID
	private static final String SINTYOKU 					= "sintyoku";				// 進捗
	private static final String ANKEN_NO					= "anken_no";				// 案件No.
	private static final String YM						= "ym";						// 年月
	private static final String PHASE						= "phase";					// フェーズ
	private static final String SASI_TEN_FLG				= "sasi_ten_flg";			// 差戻転送フラグ
	private static final String TORIMODOSHI_FUKA_FLG		= "torimodoshi_fuka_flg";	// 取戻不可フラグ
	// 課題No.152
	// 削除開始
	private static final String STATUS					= "status";					// ステータス
	//private static final String KOUSIN_USER_HANDAN		= "kousin_user_handan";		// 更新ユーザID判定
	// 削除完了
	// 課題No.236
	// 追加開始
	private static final String SYSTEM_KBN_GSS			= "'01'";					// 基幹システム区分(GSS)
	// 追加完了
	private static final String PHASE_HANDAN				= "phase_handan";			// フェーズ判定
	private static final String INIT_BUNRUI2				= "init_bunrui2";			// 初期分類2
	private static final String BUNRUI2					= "bunrui2";				// 分類2
	private static final String BU_CD						= "bu_cd";					// 部コード
	// 追加完了
	private static final int MAX_RECDOE				= 1000;							// 最大記録

	private static final String SP_SS_O_SELECT_HYOUJIVAL			= "SP_SS_O_SELECT_HYOUJIVAL";
	private static final String SP_SS_OS_SELECT_G0400				= "SP_SS_OS_SELECT_G0400";
	private static final String SP_SS_OS_SELECT_M0200				= "SP_SS_OS_SELECT_M0200";
	private static final String SP_SS_OS_SELECT_BUNNRUI2			= "SP_SS_OS_SELECT_BUNNRUI2";
	private static final String SP_SS_OS_SELECT_M1200				= "SP_SS_OS_SELECT_M1200";
    private static final String SP_SS_OS6103_SELECT_ICHIRAN		= "SP_SS_OS6103_SELECT_ICHIRAN";

	public SincyokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (SincyokuForm)appContext.getActionForm();

		workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
		langMode = cmnData.getComLangMode();
		userId = user_bean.getComUserId();
	}

	/**
	 * 変数初期化 <br>
	 */
	public void initialize() {
	    workflowSystemkbn = GS.EMPTY_CHARCTER;
	    langMode = GS.EMPTY_CHARCTER;
		userId = GS.EMPTY_CHARCTER;

	}

	/**
	 *
	 * フェーズセレクトボックスの設定値を取得する <br>
	 *
	 * @throws SQLException
	 */
	public void getPhase() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_HYOUJIVAL, sqlExec);
		exCstmt.setStringIn(PHASE_KBN);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(langMode);
		exCstmt.setResultSet(RESULTSET);

		//課題No.236
		//追加開始
		String systemKbn[] = Function.StrSplitToken((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY),GS.COMMA);
		boolean kensyoFlg = Boolean.FALSE;
		boolean kakuninFlg = Boolean.FALSE;
		//追加完了
		if(systemKbn != null){
			for(int i = 0;i<systemKbn.length;i++){
				if(SYSTEM_KBN_GSS.equals(systemKbn[i])){
					kensyoFlg = Boolean.TRUE;
				}else{
					kakuninFlg = Boolean.TRUE;
				}
			}
		}

		try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_phase = new LinkedHashMap<String,String>();
	    	int i = 0;
	    	while ( rs.next() ) {
	    		//課題No.236
	    		//修正開始
	    		if(GS.PHASE_HIKIATEKIN_KENSYO.equals(rs.getString(KBN_VAL))) {
	    			if(kensyoFlg){
			    		ar_phase.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
	    			}
	    		}else if(GS.PHASE_HIKIATEKIN_KAKUNIN.equals(rs.getString(KBN_VAL))) {
	    			if(kakuninFlg){
			    		ar_phase.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
	    			}
	    		}else{
		    		ar_phase.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
	    		}
	    		i++;
	    	}
	    	form.setAr_phase(ar_phase);
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}

	/**
	 *
	 * ステータスセレクトボックスの設定値を取得する <br>
	 *
	 * @throws SQLException
	 */
	public void getStatus() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_HYOUJIVAL, sqlExec);
		exCstmt.setStringIn(STATUS_KBN);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(langMode);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_status = new LinkedHashMap<String,String>();
	    	int i = 0;
	    	while ( rs.next() ) {
				//初期設定
	    		ar_status.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
	    		i++;
	    	}
	    	form.setAr_status(ar_status);
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}

	/**
	 *
	 * 所在国セレクトボックスの設定値を取得する <br>
	 *
	 * @throws SQLException
	 */
	public void getCountry() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_G0400, sqlExec);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_country = new LinkedHashMap<String,String>();
	    	int i = 0;
	    	while ( rs.next() ) {
				//初期設定
	    		//課題No.210
	    		//修正開始
	    		//ar_country.put(rs.getString(COUNTRY_NM_HY),rs.getString(COUNTRY_NM));
	    		ar_country.put(rs.getString(COUNTRY_KENSAKU),rs.getString(WB_COUNTRY_NM));
	    		//修正完了
	    		i++;
	    	}
	    	form.setAr_country(ar_country);
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
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
			form.setHanyou1Title(ar_hanyouTitle.get("1"));
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0200, sqlExec);
		exCstmt.setStringIn(userId);
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
            exCstmt.setStringIn(userId);
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
	 * 汎用3【リスト】取得 <br>
	 *
	 * @exception SQLException
	 */
	public void getHanyou3() throws SQLException {


        ResultSet rs = null;
		try{
			// ResultSet取得
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M1200, sqlExec);
            exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());		// 共)ユーザ情報.業務フローパターンシステム区分
            exCstmt.setStringIn(form.getSystemKbn());						// 機)システム区分
            exCstmt.setStringIn(form.getHanyou1());
            exCstmt.setStringIn(form.getHanyou2());
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(form.getMonth());
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_hanyou3 = new LinkedHashMap<String,String>();
			InputCheck check = new InputCheck();
			String hanyo3 = null;			//現在のカーソルの部コード
			String hanyo3_hy = null;		//現在のカーソルの部名称
			String hanyo3_mae = null;		//前のカーソルの部コード
			String hanyo3_comma = null;		//カンマで結合した部コード
			
			while ( rs.next() ) {
				hanyo3 = Function.trim(rs.getString(HANYOU3));
				hanyo3_hy = rs.getString(HANYOU3_HY);
				
				// 2行目以降のカーソル
				if(!check.isNullBlank(hanyo3_mae)){
					// 1つの名称に対して複数のコードが紐付くとき、複数コードをカンマ区切りで保持
					if(hanyo3_mae.equals(hanyo3_hy)){
						hanyo3_comma = hanyo3_comma + GS.SINGLE_QUOTATION + GS.COMMA + GS.SINGLE_QUOTATION + hanyo3;
					}else{
					// 1つの名称に対してコードが1つのとき、単一コードを保持
						hanyo3_comma = hanyo3;
					}
				//最初のカーソル
				}else{
					hanyo3_comma = hanyo3;
				}
				ar_hanyou3.put(hanyo3_hy,hanyo3_comma);
				hanyo3_mae = hanyo3_hy;
			}

			form.setAr_hanyou3(ar_hanyou3);

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

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(SHOW,workflowSystemkbn,langMode);

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
    			//Resultset close
    			rs.close();
    		}
	    }
	}

	/**
	 * 一覧情報取得処理 <br>
	 *
	 * @exception SQLException
	 */
	public boolean getMeisai() throws SQLException {

		boolean returnValue = true;

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6103_SELECT_ICHIRAN, sqlExec);
		// 共)言語モード
		exCstmt.setStringIn(langMode);
		// システム区分
		exCstmt.setStringIn(form.getSystemKbn());
		// ---------------------- レビュー指摘事項記録表No.075により ----- 090508 CNC.BJN Upd ↓------------
		if (GS.EMPTY_CHARCTER.equals(String.valueOf(user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY)))) {
			// 全参照システム区分は空のとき、シングルクォーテーションを追加する
			exCstmt.setStringIn(GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION);
		} else {
			// 全参照システム区分
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY));
		}
		if (GS.EMPTY_CHARCTER.equals(String.valueOf(user_bean.getComSansyososhiki_all().get(SANKO_SATEIKAISYA_CD_KEY)))) {
			// 全参照査定会社コードは空のとき、シングルクォーテーションを追加する
			exCstmt.setStringIn(GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION);
		} else {
			// 全参照査定会社コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SATEIKAISYA_CD_KEY));
		}
		if (GS.EMPTY_CHARCTER.equals(String.valueOf(user_bean.getComSansyososhiki_all().get(SANKO_BUNRUI2_KEY)))) {
			// 全参照分類２コードコードは空のとき、シングルクォーテーションを追加する
			exCstmt.setStringIn(GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION);
		} else {
			// 全参照分類２コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_BUNRUI2_KEY));
		}
		// ---------------------- レビュー指摘事項記録表No.075により ----- 090508 CNC.BJN Upd ↑------------
		// 対象年月
		exCstmt.setStringIn(form.getKensaku_month());
		// フェーズ
		exCstmt.setStringIn(form.getKensaku_phase());
		// ステータス
		exCstmt.setStringIn(form.getKensaku_status());
		// 汎用1
		exCstmt.setStringIn(form.getKensaku_hanyou1());
		// 汎用2
		exCstmt.setStringIn(form.getKensaku_hanyou2());
		// 汎用3
		exCstmt.setStringIn(form.getKensaku_hanyou3());
		// 処理日FROM
		exCstmt.setStringIn(form.getKensaku_syori_dtFrom());
		// 処理日TO
		exCstmt.setStringIn(form.getKensaku_syori_dtTo());
		// 関係者
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_parties()));
		// 勘定先CD
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_cd()));
		// 勘定先名称
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_nm()));
		// DUNS No.
		exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_duns_no()));
		// 課題No.210
		// 修正開始
		// 所在国
		//exCstmt.setStringIn(form.getKensaku_country());
		String[] countryKensaku = Function.StrSplitToken(form.getKensaku_country(),GS.SLASH);
		if(countryKensaku != null){
			exCstmt.setStringIn(countryKensaku[0]);
			exCstmt.setStringIn(countryKensaku[1]);
		}else{
			exCstmt.setStringIn(GS.EMPTY_CHARCTER);
			exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		}
		// 修正完了
		// 課題No.152
		// 削除開始
		// ユーザID
		//exCstmt.setStringIn(userId);
		// 削除完了

		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();	// 明細配列
	    	int i = 0;
	    	while ( rs.next() ) {
				TorihikisakiBean listBean = new TorihikisakiBean();
				// 検索結果が1000件以上の場合
				if(i > MAX_RECDOE - 1){
					returnValue = false;
					break;
				}

				//半期四半期区分
				listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
				//id
				listBean.setId(Function.getStringOfInt(i));

				//システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));

				//査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEIKAISYA_CD));

				//店コード
				listBean.setMise_cd(rs.getString(MISE_CD));

				//査定期
				listBean.setSatei_ki(rs.getString(SATEI_KI));

				// 勘定先CD
				listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));

				// 勘定先名称
				listBean.setKanjo_nm(rs.getString(TORI_NM));

				// 課題No.30
				// 修正開始
				// 表示用編集値
				// String[] str = rs.getString(HENSHU_HY).split("№2∇№2");
				// 組織
				//listBean.setSoshiki(str[0]);
				// 対象年月(画面表示用)
				//listBean.setTaisyo_ym_hyoji(str[1]);
				// 担当者
				//listBean.setTanto_nm(str[2]);
				// 進捗
				//listBean.setSintyoku(str[3]);
				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				// 対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(HYOJI_YM));
				// 担当者
				listBean.setTanto_nm(getTantou(rs.getString(KANKEISHA_TANTOU),rs.getString(KANKEISHA_DAIKO)));
				// 案件保持ユーザID
				listBean.setHoji_user_id(rs.getString(KANKEISHA_TANTOU_ID));
				// 進捗
				listBean.setSintyoku(rs.getString(SINTYOKU));
				// 修正完了
				// 追加開始
				// 案件No.
				listBean.setAnken_no(rs.getString(ANKEN_NO));
				// 年月
				listBean.setTaisyo_ym(rs.getString(YM));
				// フェーズ
				listBean.setPhase(rs.getString(PHASE));
				// 差戻転送フラグ
				listBean.setSasi_ten_flg(Function.trim(rs.getString(SASI_TEN_FLG)));
				// 取戻不可フラグ
				listBean.setTorimodoshi_fuka_flg(Function.trim(rs.getString(TORIMODOSHI_FUKA_FLG)));
				// 課題No.152
				// 削除開始
				// 更新ユーザ判定
				//listBean.setKousin_user_handan(rs.getString(KOUSIN_USER_HANDAN));
				// 削除完了
				// フェーズ判定
				listBean.setPhase_handan(rs.getString(PHASE_HANDAN));
				// 初期分類2
				listBean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
				// 分類2
				listBean.setBunrui2(rs.getString(BUNRUI2));
				// 部コード
				listBean.setBu_cd(Function.trim(rs.getString(BU_CD)));
				// 追加完了
				// ステータス
				listBean.setStatus(rs.getString(STATUS));
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
	    			//Resultset close
	    			rs.close();
	    		} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
	    		}
	    	}
	    }

	    return returnValue;
	}

	// 課題No.30
	// 追加開始
	/**
     * 担当者名編集処理<br>
     *
     * @param tanto 担当者名
     * @param daiko 代行者名
     * @return 編集された担当名
     */
    protected String getTantou(String tanto,String daiko) throws SQLException{
        StringBuffer tanto_nm = new StringBuffer(Function.trim(tanto));
        if(!Function.trim(daiko).equals(GS.EMPTY_CHARCTER)){
            //代行者名日本語 or 代行者名英語が存在する場合
            if(cmnData.getComLangMode().equals(GS.LANG_JA)){
            	tanto_nm.append(DAIKO);
            }else{
            	tanto_nm.append(DAIKO_EN);
            }
            tanto_nm.append(Function.trim(daiko))
                    .append(GS.KAKKO_MIGI);
        }
        return tanto_nm.toString();
    }
	// 追加完了
}