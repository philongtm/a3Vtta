/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/7		SSC				基準日、決算期区分(プルダウン)引数の修正
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.TyusyutuJokenBean;
import app.UserBean;
import app.system.form.CyusyutujyokenForm;
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
* OS7110_抽出条件メンテナンス_一覧 DBアクセスクラス <br>
*/
public class CyusyutujyokenDbAcc extends CommonDbAcc {
	
	private AppContext appContext 	= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 	= null;				// 機能共通セッション
    private UserBean user_bean 	= null;				// ユーザ情報
    private CyusyutujyokenForm form 		= null;				// アクションフォーム

	private static final String KEY_SYSTEM_KBN 			= "system_kbn"; 	// 区分キー
	private static final String KEY_HANYO1 				= "hanyo1";			// 区分キー
	private static final String KEY_KESSANKI 				= "kessanki";		// 区分キー
	private static final String KEY_KIJYUNBI_KBN 			= "kijyunbi_kbn";	// 区分キー
	private static final String KEY_SHOW 					= "show";			// 区分キー
    private static final String NUM_FMT_KOKUNAI 	= "##,###,###,###,###,##0.##";      // 数字のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  	= "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外

    //Resultset用文字列    
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val"; 	// 区分表示値
	private static final String KBN_VAL					= "kbn_val";			// 区分値
	private static final String KIKAN_KAISHA_CD 			= "kikan_kaisha_cd";	// 基幹会社コード
	private static final String SATEI_KAISHA_NM 			= "satei_kaisha_nm";	// 基幹会社コード(表示用)
	private static final String KAKO_KTK_SANSYO 			= "kako_ktk_sansyo";	// 過去格付参照時点
	private static final String KAKO_KTK_TO 				= "kako_ktk_to";		// 過去格付(To)
	private static final String KAKO_KTK_FROM 			= "kako_ktk_from";		// 過去格付(From)
	private static final String KAKO_KTK_NM 				= "kako_ktk_nm";		// 過去格付(名称)
	private static final String KAKO_KTK_FLG 				= "kako_ktk_flg";		// 過去格付フラグ
	private static final String SATEI_NM 					= "satei_nm";			// 査定対象(名称)
	private static final String SATEI_FLG 				= "satei_flg";			// 査定対象
	private static final String TAIRYU_NM 				= "tairyu_nm";			// 滞留判定(名称)
	private static final String TAIRYU_FLG 				= "tairyu_flg";			// 実質滞留判定対象
	private static final String TAIRYU_TO 				= "tairyu_to";			// 滞留期間(To)
	private static final String TAIRYU_FROM 				= "tairyu_from";		// 滞留期間(From)
	private static final String TAIRYU_KI 				= "tairyu_ki";			// 滞留期間(月)
	private static final String KINGAKUJYOUKEN2 			= "kingakujyouken2";	// 金額条件２(金額)
	private static final String JYOUKEN2_TAIRYU 			= "jyouken2_tairyu";	// 金額条件２(滞留区分)
	private static final String JYOUKEN2_TAIRYU_NM		= "jyouken2_tairyu_nm";	// 金額条件２(滞留区分)名称
	private static final String TUUKA 					= "tuuka";				// (金額条件)通貨
	private static final String KINGAKUJYOUKEN 			= "kingakujyouken";		// 金額条件1(金額)
	private static final String KAKUZUKE 					= "kakuzuke";			// 格付
	private static final String JIYUU_NM_E 				= "jiyuu_nm_e";			// 条件名称(英語)
	private static final String JIYUU_NM_KJ 				= "jiyuu_nm_kj";		// 条件名称
	private static final String JIYUU_NM 					= "jiyuu_nm";			// 条件名称(日本語/英語)
	private static final String JIYUU_CD 					= "jiyuu_cd";			// 抽出事由
	private static final String KIJUNBI_NM 				= "kijunbi_nm";			// 基準日(名称)
	private static final String KIJUNBI_KBN 				= "kijunbi_kbn";		// 基準日
	private static final String KESAN_KI_NM 				= "kesan_ki_nm";		// 決算期区分(名称)
	private static final String HANKI_SIHANKI_KBN 		= "hanki_sihanki_kbn";	// 決算期区分
	private static final String MISE_CD 					= "mise_cd";			// 分類２
	private static final String SATEIKAISYA_CD 			= "sateikaisya_cd";		// 分類１
	private static final String SYSTEM_KBN_NM 			= "system_kbn_nm";		// システム区分名称
	private static final String SYSTEM_KBN 				= "system_kbn";			// システム区分
	private static final String JYOUKEN_NO 				= "jyouken_no";			// 条件No

    private static final String SP_SS_O_SELECT_P0201					= "SP_SS_O_SELECT_P0201";				//区分の取得プロシージャ
    private static final String SP_SS_OL_SELECT_P0200					= "SP_SS_OL_SELECT_P0200";				//区分の取得プロシージャ
    private static final String SP_SS_OS_SELECT_M0400					= "SP_SS_OS_SELECT_M0400";				//汎用2セレクトボックスの設定値の取得プロシージャ
    private static final String SP_SS_OS7110_SELECT_M0700				= "SP_SS_OS7110_SELECT_M0700";			//抽出条件一覧情報の取得プロシージャ
    
    // INパラメータ
    private String langMode;    // 共)言語モード)

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public CyusyutujyokenDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (CyusyutujyokenForm)appContext.getActionForm();

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
     * SP_SS_OL_SELECT_P0200を利用して、区分リストを取得する。 <br>
     * 
     * @param key 区分キー
     * @param langMode 言語モード
     * @param systemKbn システム区分
     * @return 区分リスト
     * @throws SQLException
     */
    private LinkedHashMap<String, String> getP0200KbnList(String key, String langMode,
			String systemKbn) throws SQLException {
    	// 区分リスト
    	LinkedHashMap<String, String> ar_kbn = new LinkedHashMap<String, String>();
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);

		cstmt.setStringIn(key);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(systemKbn);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
	    	while ( rs.next() ) {
				//初期設定
	    		ar_kbn.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));	    			
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
		return ar_kbn;
	}

    /**
     * SP_SS_O_SELECT_P0201を利用して、区分リストを取得する。 <br>
     * 
     * @param key 区分キー
     * @param langMode 言語モード
     * @param systemKbn システム区分
     * @return 区分リスト
     * @throws SQLException
     */
    private LinkedHashMap<String, String> getP0201KbnList(String key, String langMode,
			String systemKbn) throws SQLException {
    	// 区分リスト
    	LinkedHashMap<String, String> ar_kbn = new LinkedHashMap<String, String>();
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_O_SELECT_P0201, sqlExec);

		cstmt.setStringIn(key);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(systemKbn);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
	    	while ( rs.next() ) {
				//初期設定
	    		ar_kbn.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));	    			
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
		return ar_kbn;
	}

    /**
     * システムセレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSystemKbnList() throws SQLException {
		// システム【配列】
    	LinkedHashMap<String, String> ar_systemKbn = this.getP0201KbnList(KEY_SYSTEM_KBN, langMode, GS.GSS);
		// システム【配列】をセットする。
    	form.setAr_systemKbn(ar_systemKbn);
    	// システム
    	if (ar_systemKbn.size() > 0) {
    		form.setSystemKbn(GS.MTS);
    	}
	}

    /**
     * 汎用１セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getHanyo1List() throws SQLException {
		// 汎用１【配列】
    	LinkedHashMap<String, String> ar_hanyo1 = this.getP0200KbnList(KEY_HANYO1, langMode, form.getSystemKbn());
		// 汎用１【配列】をセットする。
    	form.setAr_hanyo1(ar_hanyo1);
	}

    /**
     * 汎用２セレクトボックスの設定値を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getHanyo2List() throws SQLException {
    	// 区分リスト
    	LinkedHashMap<String, String> ar_hanyo2 = new LinkedHashMap<String, String>();
    	// 機）システム区分
    	String systemKbn = form.getSystemKbn();
    	// 機）汎用１
    	String hanyo1 = form.getHanyo1();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS_SELECT_M0400, sqlExec);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(systemKbn);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
	    	while ( rs.next() ) {
				//初期設定
	    		ar_hanyo2.put(rs.getString(SATEI_KAISHA_NM), rs.getString(KIKAN_KAISHA_CD));	    			
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
		form.setAr_hanyo2(ar_hanyo2);
	}

    /**
     * 決算期区分セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKesanKbnList() throws SQLException {
		// 決算期区分【配列】

    	// 課題No.100 
    	// 追加開始
    	//LinkedHashMap<String, String> ar_kesanKbn = this.getP0200KbnList(KEY_KESSANKI, langMode, user_bean.getComWorkflowSystemkbn());
    	LinkedHashMap<String, String> ar_kesanKbn = this.getP0200KbnList(KEY_KESSANKI, langMode, form.getSystemKbn());
    	// 追加完了
    	
		// 決算期区分【配列】をセットする。
    	form.setAr_kesanKbn(ar_kesanKbn);
	}

    /**
     * 基準日セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKijunbiList() throws SQLException {
		// 基準日【配列】
    	
    	// 課題No.100
    	// 追加開始
    	//LinkedHashMap<String, String> ar_kijyunbi = this.getP0200KbnList(KEY_KIJYUNBI_KBN, langMode, user_bean.getComWorkflowSystemkbn());
    	LinkedHashMap<String, String> ar_kijyunbi = this.getP0200KbnList(KEY_KIJYUNBI_KBN, langMode, form.getSystemKbn());
    	// 追加完了
    	
		// 基準日【配列】をセットする。
    	form.setAr_kijyunbi(ar_kijyunbi);
	}

    /**
     * 表示件数セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getShow() throws SQLException {
		// 表示件数【配列】
    	LinkedHashMap<String, String> ar_show = this.getP0200KbnList(KEY_SHOW, langMode, user_bean.getComWorkflowSystemkbn());
		// 表示件数【配列】をセットする。
    	form.setAr_show(ar_show);
	}

    /**
     * 抽出条件一覧情報を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getMeisaiList() throws SQLException {
    	// 一覧明細
    	List<TyusyutuJokenBean> ar_meisai = new ArrayList<TyusyutuJokenBean>();
    	// 機)システム区分(検索用)
    	String systemKbn = form.getSrhSystemKbn();
    	// システム区分(検索用)がヌルの場合
    	InputCheck check = new InputCheck();
    	if (check.isNullBlank(systemKbn)) {
    		// 明細を表示しない
    		return;
    	}
    	// 機)汎用1(検索用)
    	String hanyo1 = form.getSrhHanyo1();
    	// 機）汎用2(検索用)
    	String hanyo2 = form.getSrhHanyo2();
    	// 機)決算期区分(検索用)
    	String kesanKbn = form.getSrhKesanKbn();
    	// 機)基準日(検索用)
    	String kijunbi = form.getSrhKijyunbi();
    	// 機)条件名称(検索用)
    	String jyokenNm = form.getSrhJyokenNm();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7110_SELECT_M0700, sqlExec);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(kesanKbn);
		cstmt.setStringIn(kijunbi);
		cstmt.setStringIn(Function.addSingleQuotation(jyokenNm));
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			int i = 0;
	    	while ( rs.next() ) {
	    		// 抽出条件情報
	    		TyusyutuJokenBean jyokenBean = new TyusyutuJokenBean();
	    		
				// id
	    		jyokenBean.setId(Function.getStringOfInt(i));
				// 条件No
	    		jyokenBean.setJoken_no(rs.getString(JYOUKEN_NO));
				// システム区分
	    		jyokenBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				// システム区分名称
	    		jyokenBean.setSystem_kbn_nm(rs.getString(SYSTEM_KBN_NM));
				// 分類１
	    		jyokenBean.setBunrui1(rs.getString(SATEIKAISYA_CD));
				// 分類２
	    		jyokenBean.setBunrui2(rs.getString(MISE_CD));
				// 決算期区分
	    		jyokenBean.setKessanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
				// 決算期区分(名称)
	    		jyokenBean.setKessanki_kbn_nm(rs.getString(KESAN_KI_NM));
				// 基準日
	    		jyokenBean.setKijunbi(rs.getString(KIJUNBI_KBN));
				// 基準日(名称)
	    		jyokenBean.setKijunbi_nm(rs.getString(KIJUNBI_NM));
				// 抽出事由
	    		jyokenBean.setTyusyutu_jiyu(rs.getString(JIYUU_CD));
				// 条件名称(日本語/英語)
	    		jyokenBean.setJoken_nm_ja_en(rs.getString(JIYUU_NM));
				// 条件名称
	    		jyokenBean.setJoken_nm(rs.getString(JIYUU_NM_KJ));
				// 条件名称(英語)
	    		jyokenBean.setJoken_nm_en(rs.getString(JIYUU_NM_E));
				// 格付
	    		jyokenBean.setKtk(rs.getString(KAKUZUKE));
				// 金額条件1(金額)
	    		jyokenBean.setKingaku1_kingaku(formatKingaku(rs.getDouble(KINGAKUJYOUKEN), systemKbn));
				// (金額条件)通貨
	    		jyokenBean.setKingaku_tuuka(rs.getString(TUUKA));
				// 金額条件２(滞留区分)
	    		jyokenBean.setKingaku2_tairyu(rs.getString(JYOUKEN2_TAIRYU));
				// 金額条件２(滞留区分)
	    		jyokenBean.setKingaku2_tairyu_nm(rs.getString(JYOUKEN2_TAIRYU_NM));
				// 金額条件２(金額)
	    		jyokenBean.setKingaku2_kingaku(formatKingaku(rs.getDouble(KINGAKUJYOUKEN2), systemKbn));
				// 滞留期間(月)
	    		jyokenBean.setTairyu_kikan_tuki(rs.getString(TAIRYU_KI));
				// 滞留期間(From)
	    		jyokenBean.setTairyu_kikan_from(rs.getString(TAIRYU_FROM));
				// 滞留期間(To)
	    		jyokenBean.setTairyu_kikan_to(rs.getString(TAIRYU_TO));
				// 実質滞留判定対象
	    		jyokenBean.setTairyu_hantei_taisyo(rs.getString(TAIRYU_FLG));
				// 滞留判定(名称)
	    		jyokenBean.setTairyu_hantei_nm(rs.getString(TAIRYU_NM));
				// 査定対象
	    		jyokenBean.setSatei_taisyo(rs.getString(SATEI_FLG));
				// 査定対象(名称)
	    		jyokenBean.setSatei_taisyo_nm(rs.getString(SATEI_NM));
				// 過去格付フラグ
	    		jyokenBean.setKako_ktk_flg(rs.getString(KAKO_KTK_FLG));
				// 過去格付(名称)
	    		jyokenBean.setKako_ktk_nm(rs.getString(KAKO_KTK_NM));
				// 過去格付(From)
	    		jyokenBean.setKako_ktk_from(rs.getString(KAKO_KTK_FROM));
				// 過去格付(To)
	    		jyokenBean.setKako_ktk_to(rs.getString(KAKO_KTK_TO));
				// 過去格付参照時点
	    		jyokenBean.setKako_ktk_sansyo_jiten(rs.getString(KAKO_KTK_SANSYO));
	    		
	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, jyokenBean);
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
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    private String formatKingaku(double kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI, kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KAIGAI, kingaku);
        }
        return formatKingaku;
    }
}