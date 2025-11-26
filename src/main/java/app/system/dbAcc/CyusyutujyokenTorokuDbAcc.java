/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.TyusyutuJokenBean;
import app.UserBean;
import app.system.form.CyusyutujyokenTorokuForm;
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
* OS7111_抽出条件メンテナンス_登録 DBアクセスクラス <br>
*/
public class CyusyutujyokenTorokuDbAcc extends CommonDbAcc {
	
	private static final String JOKEN_NO = "jokenNo";
	private static final String CNT = "cnt";
	private AppContext appContext 				= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 				= null;				// 機能共通セッション
    private UserBean user_bean 				= null;				// ユーザ情報
    private CyusyutujyokenTorokuForm form 		= null;				// アクションフォーム
    private TyusyutuJokenBean joken_bean		= null;							// 抽出条件情報

	private static final String KEY_SYSTEM_KBN 			= "system_kbn"; 		// 区分キー
	private static final String KEY_HANYO1 				= "hanyo1";				// 区分キー
	private static final String KEY_KESSANKI 				= "kessanki";			// 区分キー
	private static final String KEY_KIJYUNBI_KBN 			= "kijyunbi_kbn";		// 区分キー
	private static final String KEY_KAKO_KAKUZUKE 		= "kako_kakuzuke";		// 区分キー
	private static final String KEY_TAIRYU_KBN 			= "tairyu_kbn";			// 区分キー
	private static final String KEY_KAKUZUKE 				= "kakuzuke";			// 区分キー

    //Resultset用文字列    
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val"; 	// 区分表示値
	private static final String KBN_VAL					= "kbn_val";			// 区分値
	private static final String SATEI_KAISHA_NM 			= "satei_kaisha_nm";	// 基幹会社コード(表示用)
	private static final String KIKAN_KAISHA_CD 			= "kikan_kaisha_cd";	// 基幹会社コード

    private static final String SP_SS_O_SELECT_P0201					= "SP_SS_O_SELECT_P0201";			//区分の取得プロシージャ
    private static final String SP_SS_OL_SELECT_P0200					= "SP_SS_OL_SELECT_P0200";			//区分の取得プロシージャ
    private static final String SP_SS_OS_SELECT_M0400					= "SP_SS_OS_SELECT_M0400";			//汎用２セレクトボックスの取得プロシージャ
    private static final String SP_SS_OS7111_SELECT_M0700				= "SP_SS_OS7111_SELECT_M0700";		//チェックプロシージャ
    private static final String SP_SS_OS7111_SELECT_M0701				= "SP_SS_OS7111_SELECT_M0701";		//条件NoのMAX値の取得プロシージャ
    private static final String SP_SS_OS7111_INSERT_M0700				= "SP_SS_OS7111_INSERT_M0700";		//M07_検討対象先抽出条件マスタの登録プロシージャ
    private static final String SP_SS_OS7111_UPDATE_M0700				= "SP_SS_OS7111_UPDATE_M0700";		//M07_検討対象先抽出条件マスタの更新プロシージャ
    private static final String SP_SS_OS7111_SELECT_T0200				= "SP_SS_OS7111_SELECT_T0200";		//チェックプロシージャ
    private static final String SP_SS_OS7111_DELETE_M0700				= "SP_SS_OS7111_DELETE_M0700";		//M07_検討対象先抽出条件マスタの更新プロシージャ
    
    // INパラメータ
    private String langMode;    		// 共)言語モード)
    private String comSystemKbn;    	// 共)業務フローパターンシステム区分

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public CyusyutujyokenTorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (CyusyutujyokenTorokuForm)appContext.getActionForm();
        joken_bean = cmnData.getJoken_bean();

        //ビーンの値を変数に設定
        langMode = cmnData.getComLangMode();
        comSystemKbn = user_bean.getComWorkflowSystemkbn();
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
		// 決算期区【配列】
    	LinkedHashMap<String, String> ar_kesanKbn = this.getP0200KbnList(KEY_KESSANKI, langMode, form.getSystemKbn());
		// 決算期区【配列】をセットする。
    	form.setAr_kesanKbn(ar_kesanKbn);
	}

    /**
     * 基準日セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKijunbiList() throws SQLException {
		// 基準日【配列】
    	LinkedHashMap<String, String> ar_kijyunbi = this.getP0200KbnList(KEY_KIJYUNBI_KBN, langMode, form.getSystemKbn());
		// 基準日【配列】をセットする。
    	form.setAr_kijunbi(ar_kijyunbi);
	}

    /**
     * 格付セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKakudukeList() throws SQLException {
		// 格付【配列】
    	LinkedHashMap<String, String> ar_kakuduke = this.getP0200KbnList(KEY_KAKUZUKE, langMode, comSystemKbn);
		// 格付【配列】をセットする。
    	form.setAr_kakuduke(ar_kakuduke);
	}

    /**
     * 金額基準2(滞留区分)セレクトボックス設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKingakuJoken2KbnList() throws SQLException {
		// 金額基準2(滞留区分)【配列】
    	LinkedHashMap<String, String> ar_kingakuJoken2Kbn = this.getP0200KbnList(KEY_TAIRYU_KBN, langMode, comSystemKbn);
		// 金額基準2(滞留区分)【配列】をセットする。
    	form.setAr_kingakuJyoken2Kbn(ar_kingakuJoken2Kbn);
	}

    /**
     * 過去格付(From)、過去格付(From)セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getKakokakudukeList() throws SQLException {
		// 過去格付【配列】
    	LinkedHashMap<String, String> ar_kakoKakuduke = this.getP0200KbnList(KEY_KAKO_KAKUZUKE, langMode, comSystemKbn);
		// 過去格付【配列】をセットする。
       	form.setAr_kakoKakudukeFrom(ar_kakoKakuduke);
       	form.setAr_kakoKakudukeTo(ar_kakoKakuduke);
	}

    /**
     * データ存在チェック <br>
     * 
     * @return データある：true
     * @throws SQLException
     */
    public boolean checkJiyuM07() throws SQLException {
    	
    	// 機)システム区分
    	String systemKbn = form.getSystemKbn();
    	// 機)汎用１
    	String hanyo1 = form.getHanyo1();
    	// 機)汎用2
    	String hanyo2 = form.getHanyo2();
    	// 機)決算期区分
    	String kesanKbn = form.getKesanKbn();
    	// 機)基準日
    	String kijunbi = form.getKijunbi();
    	// 機)抽出事由
    	String jiyu = form.getCyusyutuJiyu();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_SELECT_M0700, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(kesanKbn);
		cstmt.setStringIn(kijunbi);
		cstmt.setStringIn(jiyu);
		// result
		cstmt.setIntOut(CNT);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
    		if (cstmt.getInt(CNT) > 0) {
    			return true;    			
    		} else {
    			return false;
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
     * 現在の条件NoのMAX値を取得する <br>
     * 
     * @return 条件NoのMAX値
     * @throws SQLException
     */
    public int getMaxJokenNo() throws SQLException {
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_SELECT_M0701, sqlExec);
		// result
		cstmt.setIntOut(JOKEN_NO);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
    		return cstmt.getInt(JOKEN_NO);
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
     * M07_検討対象先抽出条件マスタの登録 <br>
     * 
     * @param maxJokenNo 条件NoのMAX値
     * @return sqlCode
     * @throws SQLException
     */
    public int insertM07(int maxJokenNo) throws SQLException {

		// 格付
    	String kakuduke = form.getKakuduke();
		// 代行ユーザID
    	String daikoUserId = user_bean.getComDaiko_userId();
		// 条件No
    	int jokenNo = maxJokenNo + 1;
		// システム区分
    	String systemKbn = form.getSystemKbn();
		// 汎用１
    	String hanyo1 = form.getHanyo1();
		// 汎用２
    	String hanyo2 = form.getHanyo2();
		// 基準日
    	String kijunbi = form.getKijunbi();
		// 抽出事由
    	String jiyu = form.getCyusyutuJiyu();
		// 条件名称(日本語)
    	String jokenNmJp = form.getJyokenNmJp();
		// 条件名称(英語)
    	String jokenNmEn = form.getJyokenNmEn();
		// 通貨
    	String tuka = form.getTukaCd().replace(GS.KAKKO_HIDARI, GS.EMPTY_CHARCTER).replace(GS.KAKKO_MIGI, GS.EMPTY_CHARCTER);
		// 金額条件１
    	String kingakuJoken1 = form.getKingakuJyoken1();
		// 滞留期間(From)
    	String tairyuKikanFrom = form.getTairyuKikanFrom();
		// 滞留期間(To)
    	String tairyuKikanTo = form.getTairyuKikanTo();
		// 実質滞留判定対象
    	String tairyuTaisyo = form.getTairyuTaisyo();
		// 査定対象
    	String sateiTaisyo = form.getSateiTaisyo();
		// 決算期区分
    	String kesanKbn = form.getKesanKbn();
		// 過去格付フラグ
    	String kakoKakudukeFlg = form.getKakoKakudukeFlg();
		// 過去格付参照時点
    	String kakoKakudukeJiten = form.getKakoKakudukeJiten();
		// 金額条件２(滞留区分)
    	String kingakuJyoken2Kbn = form.getKingakuJyoken2Kbn();
		// 金額条件２
    	String kingakuJyoken2 = form.getKingakuJyoken2();
		// ユーザID
    	String userId = user_bean.getComUserId();
		// 過去格付(From)
    	String kakoKakudukeFrom = form.getKakoKakudukeFrom();
		// 過去格付(To)
    	String kakoKakudukeTo = form.getKakoKakudukeTo();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_INSERT_M0700, sqlExec);
		// パラメーター
		cstmt.setStringIn(kakuduke);
		cstmt.setStringIn(daikoUserId);
		cstmt.setIntIn(jokenNo);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(kijunbi);
		cstmt.setStringIn(jiyu);
		cstmt.setStringIn(jokenNmJp);
		cstmt.setStringIn(jokenNmEn);
		cstmt.setStringIn(tuka);
		cstmt.setDoubleIn(Function.getValueOfDoubleC(kingakuJoken1));
		cstmt.setStringIn(tairyuKikanFrom);
		cstmt.setStringIn(tairyuKikanTo);
		cstmt.setStringIn(tairyuTaisyo);
		cstmt.setStringIn(sateiTaisyo);
		cstmt.setStringIn(kesanKbn);
		cstmt.setStringIn(kakoKakudukeFlg);
		cstmt.setStringIn(kakoKakudukeJiten);
		cstmt.setStringIn(kingakuJyoken2Kbn);
		cstmt.setDoubleIn(Function.getValueOfDoubleC(kingakuJyoken2));
		cstmt.setStringIn(userId);
		cstmt.setStringIn(kakoKakudukeFrom);
		cstmt.setStringIn(kakoKakudukeTo);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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
		return cstmt.getSqlCode();
	}

    /**
     * M07_検討対象先抽出条件マスタの更新 <br>
     * 
     * @throws SQLException
     */
    public void updateM07() throws SQLException {

		// 格付
		String kakuduke = form.getKakuduke();
		// 代行ユーザID
		String daikoUserId = user_bean.getComDaiko_userId();
		// 条件No
		String jokenNo = form.getJokenNo();
		// 条件名称(日本語)
		String jokenNmJp = form.getJyokenNmJp();
		// 条件名称(英語)
		String jokenNmEn = form.getJyokenNmEn();
		// 金額条件１
		String kingakuJoken1 = form.getKingakuJyoken1();
		// 滞留期間(From)
		String tairyuKikanFrom = form.getTairyuKikanFrom();
		// 滞留期間(To)
		String tairyuKikanTo = form.getTairyuKikanTo();
		// 実質滞留判定対象
		String tairyuTaisyo = form.getTairyuTaisyo();
		// 査定対象
		String sateiTaisyo = form.getSateiTaisyo();
		// 過去格付フラグ
		String kakoKakudukeFlg = form.getKakoKakudukeFlg();
		// 過去格付参照時点
		String kakoKakudukeJiten = form.getKakoKakudukeJiten();
		// 金額条件２(滞留区分)
		String kingakuJyoken2Kbn = form.getKingakuJyoken2Kbn();
		// 金額条件２
		String kingakuJyoken2 = form.getKingakuJyoken2();
		// ユーザID
		String userId = user_bean.getComUserId();
		// 過去格付(From)
		String kakoKakudukeFrom = form.getKakoKakudukeFrom();
		// 過去格付(To)
		String kakoKakudukeTo = form.getKakoKakudukeTo();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_UPDATE_M0700, sqlExec);
		// パラメーター
		cstmt.setStringIn(kakuduke);
		cstmt.setStringIn(daikoUserId);
		cstmt.setStringIn(jokenNo);
		cstmt.setStringIn(jokenNmJp);
		cstmt.setStringIn(jokenNmEn);
		cstmt.setDoubleIn(Function.getValueOfDoubleC(kingakuJoken1));
		cstmt.setStringIn(tairyuKikanFrom);
		cstmt.setStringIn(tairyuKikanTo);
		cstmt.setStringIn(tairyuTaisyo);
		cstmt.setStringIn(sateiTaisyo);
		cstmt.setStringIn(kakoKakudukeFlg);
		cstmt.setStringIn(kakoKakudukeJiten);
		cstmt.setStringIn(kingakuJyoken2Kbn);
		cstmt.setDoubleIn(Function.getValueOfDoubleC(kingakuJyoken2));
		cstmt.setStringIn(userId);
		cstmt.setStringIn(kakoKakudukeFrom);
		cstmt.setStringIn(kakoKakudukeTo);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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
     * データ存在チェック <br>
     * 
     * @return データある：true
     * @throws SQLException
     */
    public boolean checkJiyuT02() throws SQLException {
    	
    	// 機)システム区分
    	//String systemKbn = form.getSystemKbn();
    	String systemKbn = joken_bean.getSystem_kbn();
    	// 機)汎用１
    	String hanyo1 = form.getHanyo1();
    	// 機)汎用2
    	String hanyo2 = form.getHanyo2();
    	// 機)決算期区分
    	String kesanKbn = form.getKesanKbn();
    	// 機)抽出事由
    	String jiyu = form.getCyusyutuJiyu();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_SELECT_T0200, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(hanyo1);
		cstmt.setStringIn(hanyo2);
		cstmt.setStringIn(kesanKbn);
		cstmt.setStringIn(jiyu);;
		
		// result
		cstmt.setIntOut(CNT);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
    		if (cstmt.getInt(CNT) > 0) {
    			return true;    			
    		} else {
    			return false;
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
     * M07_検討対象先抽出条件マスタの削除 <br>
     * 
     * @throws SQLException
     */
    public void deleteM07() throws SQLException {

		// 条件No
		String jokenNo = form.getJokenNo();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS7111_DELETE_M0700, sqlExec);
		// パラメーター
		cstmt.setStringIn(jokenNo);
		
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
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
}