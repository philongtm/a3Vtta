/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.system.form.GolfForm;
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
* OS4101_ゴルフ会員権一覧 DBアクセスクラス <br>
*/
public class GolfDbAcc extends CommonDbAcc {
	
	private static final String TAISHO_YM = "taisho_ym";
	private AppContext appContext 	= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 	= null;				// 機能共通セッション
    private UserBean user_bean 	= null;				// ユーザ情報
    private GolfForm form 			= null;				// アクションフォーム

	private static final String KEY_SHOW 					= "show";				// 区分キー
    private static final String NUM_FMT_KOKUNAI 	= "##,###,###,###,###,##0.##";      // 数字のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  	= "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外

    //Resultset用文字列    
	private static final String SATEI_KI					= "satei_ki"; 			// 査定期
	private static final String HYOJI_SATEI_KI			= "hyoji_satei_ki"; 	// 
	private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val"; 	// 区分表示値
	private static final String KBN_VAL					= "kbn_val";			// 区分値
	private static final String YM 						= "ym"; 				// 対象年月
	private static final String COUNTRY_NM 				= "country_nm"; 		// 所在国
	private static final String ANKEN_NO 					= "anken_no"; 			// 案件No
	private static final String ORA 						= "ora"; 				// 組織
	private static final String SYORI_KAISU 				= "syori_kaisu"; 		// 処理回数
	private static final String KIJUNBI_KBN 				= "kijunbi_kbn"; 		// 基準日区分
	private static final String MISE_CD 					= "mise_cd"; 			// 店コード
	private static final String SYSTEM_KBN 				= "system_kbn"; 		// システム区分
	private static final String HANKI_SIHANKI_KBN 		= "hanki_sihanki_kbn"; 	// 半期・四半期区分
	private static final String TOGO_TORI_CD 				= "togo_tori_cd"; 		// 統合取引先コード
	private static final String OYA_HANDAN 				= "oya_handan"; 		// 親会社一体独立
	private static final String OYAKISYSINYOKTK 			= "oyakisysinyoktk"; 	// 親会社格付
	private static final String OYA_DUNS_NO 				= "oya_duns_no"; 		// 親会社Duns_No
	private static final String DUNS_RATING 				= "duns_rating"; 		// DUNS Rating
	private static final String FSS 						= "fss"; 				// FSS
	private static final String KTKKIKAN 					= "ktkkikan"; 			// 格付機関
	private static final String GAIBUKTK 					= "gaibuktk"; 			// 外部格付
	private static final String LASTSINYOKTK 				= "lastsinyoktk"; 		// 信用格付情報
	private static final String BUNRUI2 					= "bunrui2"; 			// 分類２
	private static final String SATEIKAISYA_CD 			= "sateikaisya_cd"; 	// 査定会社コード
	private static final String YM_HY 					= "ym_hy"; 				// 対象年月（画面表示用）
	private static final String KASHI_KIN 				= "kashi_kin"; 			// 貸倒引当金
	private static final String KOTEI_SAIKENN 			= "kotei_saikenn"; 		// 固定化営業債権
	private static final String TUUKA_CD 					= "tuuka_cd"; 			// 通貨
	private static final String SAIKEN_ZANKI 				= "saiken_zanki"; 		// 債権残計
	private static final String TORI_NM 					= "tori_nm"; 			// 勘定先名称
	private static final String KIKAN_TORI_CD 			= "kikan_tori_cd"; 		// 勘定先CD

    private static final String SP_SS_O_SELECT_T2600					= "SP_SS_O_SELECT_T2600";			//査定期セレクトボックスの設定値の取得プロシージャ
    private static final String SP_SS_OL_SELECT_P0200					= "SP_SS_OL_SELECT_P0200";			//表示件数セレクトボックスの設定値の取得プロシージャ
    private static final String SP_SS_O_SELECT_M2200					= "SP_SS_O_SELECT_M2200";			//対象年月の取得プロシージャ
    private static final String SP_SS_OS4101_SELECT_ICHIRAN			= "SP_SS_OS4101_SELECT_ICHIRAN";	//一覧情報の取得プロシージャ
    private static final String SP_SS_OS4101_SELECT_T1000				= "SP_SS_OS4101_SELECT_T1000";		//最大処理回数の取得プロシージャ
    
    // INパラメータ
    private String langMode;    // 共)言語モード)

    /**
     * コンストラクタ <br>
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public GolfDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (GolfForm)appContext.getActionForm();

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
	 * 査定期取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSateiki() throws SQLException {

		String sateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
		String sansyoBunrui2 = user_bean.getComSansyoBunrui2();
		String workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T2600, sqlExec);
		exCstmt.setStringIn(langMode);
		exCstmt.setStringIn(sateikaisya_cd);
		exCstmt.setStringIn(sansyoBunrui2);
	    exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_sateiki = new LinkedHashMap<String,String>();
	    	int i = 0;
	    	boolean flg = false;
	    	while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSateiki(rs.getString(SATEI_KI));
				}
	    		ar_sateiki.put(rs.getString(HYOJI_SATEI_KI), rs.getString(SATEI_KI));	    			
	    		flg = true;
	    		i++;
	    	}
	    	if(!flg){
		    	// 査定期が取得できなかった場合、ブランクをセット
		    	ar_sateiki.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);
	    	}
	    	form.setAr_sateiki(ar_sateiki);	
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
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
     * 表示件数セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getShow() throws SQLException {
		// システム【配列】
    	LinkedHashMap<String, String> ar_show = this.getP0200KbnList(KEY_SHOW, langMode, user_bean.getComWorkflowSystemkbn());
		// システム【配列】をセットする。
    	form.setAr_show(ar_show);
	}

    /**
     * 対象年月を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getTaiyoYm() throws SQLException {

    	// 機)査定期
    	String sateiki = form.getSateiki();
    	// システム区分
    	String systemKbn = user_bean.getComWorkflowSystemkbn();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(langMode);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			StringBuffer taisyoYm = new StringBuffer(GS.EMPTY_CHARCTER);
			int i = 0;
	    	while ( rs.next() ) {
				if (i != 0) {
					taisyoYm.append(",");
				}
				// 対象年月
				taisyoYm.append("'" + rs.getString(TAISHO_YM) + "'");
				i++;
	    	}
	    	if(GS.EMPTY_CHARCTER.equals(taisyoYm.toString())){
	    		taisyoYm.append("''");
	    	}
	    	
		    // ActionForm に対象年月を格納
		    form.setTaisyoYm(taisyoYm.toString());    
		    
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
     * 一覧情報を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getMeisaiList() throws SQLException {
    	
		// 国内（共)ユーザ情報.業務フローパターンシステム区分が'01'：GSS）の場合
		if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())) {
			// T01_対象先の最大処理回数を取得
			this.getMaxSyorikaisu();
		}
    	
    	// 一覧明細
    	List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();
    	// 機)査定期
    	String sateiki = form.getSateiki();
    	// 共)ユーザ情報.業務フローパターンシステム区分
    	String systemKbn = user_bean.getComWorkflowSystemkbn();
    	// 共)ユーザ情報.業務フローパターン査定会社コード
    	String sateikaisyaCd = user_bean.getComWorkflowSateikaisya_cd();
    	// 共)ユーザ情報.参照分類２コード
    	String bunrui2 = user_bean.getComSansyoBunrui2();
    	// 機)対象年月
    	String taisyoYm = form.getTaisyoYm();
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS4101_SELECT_ICHIRAN, sqlExec);
		cstmt.setStringIn(langMode);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(sateikaisyaCd);
		cstmt.setStringIn(bunrui2);
		cstmt.setStringIn(taisyoYm);

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
	    		TorihikisakiBean toriBean = new TorihikisakiBean();
	    		
				// id
	    		toriBean.setId(Function.getStringOfInt(i));
				// 勘定先CD
	    		toriBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
				// 勘定先名称
	    		toriBean.setKanjo_nm(rs.getString(TORI_NM));
	    		// 通貨
	    		String tuuka_cd = rs.getString(TUUKA_CD);
				// 債権残計
	    		String saiken_zanki = formatKingaku(rs.getDouble(SAIKEN_ZANKI), systemKbn);
	    		toriBean.setSaiken_kingaku(contract(saiken_zanki, tuuka_cd));
				// 固定化営業債権
	    		if (GS.GSS.equals(systemKbn)) {
	    			// 国内
		    		String kotei_saikenn = formatKingaku(rs.getDouble(KOTEI_SAIKENN), systemKbn);
		    		toriBean.setKoteika_saiken(contract(kotei_saikenn, tuuka_cd));
	    		} else {
	    			// 海外
		    		toriBean.setKoteika_saiken("-");
	    		}
				// 貸倒引当金
	    		String kashi_kin = formatKingaku(rs.getDouble(KASHI_KIN), systemKbn);
	    		toriBean.setKasi_hikiatekin(contract(kashi_kin, tuuka_cd));
				// 対象年月
	    		toriBean.setTaisyo_ym(rs.getString(YM));
				// 対象年月（画面表示用）
	    		toriBean.setTaisyo_ym_hyoji(rs.getString(YM_HY));
				// 査定会社コード
	    		toriBean.setSateikaisya_cd(rs.getString(SATEIKAISYA_CD));
				// 分類２
	    		toriBean.setBunrui2(rs.getString(BUNRUI2));
				// 信用格付情報
	    		toriBean.setSinyoktk(rs.getString(LASTSINYOKTK));
				// 外部格付
	    		toriBean.setGaibu_ktk(rs.getString(GAIBUKTK));
				// 格付機関
	    		toriBean.setKtk_kikan(rs.getString(KTKKIKAN));
				// FSS
	    		toriBean.setFss(rs.getString(FSS));
				// DUNS Rating
	    		toriBean.setDuns_rating(rs.getString(DUNS_RATING));
				// 親会社Duns_No
	    		toriBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
				// 親会社格付
	    		toriBean.setOya_ktk(rs.getString(OYAKISYSINYOKTK));
				// 親会社一体独立
	    		toriBean.setOya_ittai_dokuritu(rs.getString(OYA_HANDAN));
				// 統合取引先コード
	    		toriBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				// フェーズ
	    		toriBean.setPhase(GS.PHASE_TAISHOSAKI_SENTEI);
				// 査定期
	    		toriBean.setSatei_ki(rs.getString(SATEI_KI));
				// 半期・四半期区分
	    		toriBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
				// システム区分
	    		toriBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				// 店コード
	    		toriBean.setMise_cd(rs.getString(MISE_CD));
				// 基準日区分
	    		toriBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
				// 処理回数
	    		toriBean.setSyori_kaisu(rs.getString(SYORI_KAISU));
				// 組織
	    		toriBean.setSoshiki(rs.getString(ORA));
				// 案件No
	    		toriBean.setAnken_no(rs.getString(ANKEN_NO));
				// 所在国
	    		toriBean.setSyozaikoku(rs.getString(COUNTRY_NM));
	    		
	    		// リンク表示フラグ制御
	    		if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())) {
	    			// 国内
	    			if (form.getShorikaisu().equals(toriBean.getSyori_kaisu()) && form.getMaxSateiki().equals(toriBean.getSatei_ki())) {
	    				// 最新のデータのみ、勘定先CDリンク押下可とする。
	    				toriBean.setLink_flg(true);
	    			} else {
	    				// 勘定先CDリンク押下可とする。
	    				toriBean.setLink_flg(false);
	    			}
	    		} else {
	    			// 海外
	    			toriBean.setLink_flg(true);
	    		}
	    		
	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, toriBean);
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
     * 最大処理回数を取得する。 <br>
     * 
     * @throws SQLException
     */
    public void getMaxSyorikaisu() throws SQLException {

    	// システム区分
    	String systemKbn = user_bean.getComWorkflowSystemkbn();
    	// 査定会社コード
    	String sateikaisyaCd = user_bean.getComWorkflowSateikaisya_cd();
    	
		// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
    	cstmt = new ExCallableStatement(SP_SS_OS4101_SELECT_T1000, sqlExec);
		cstmt.setStringIn(systemKbn);
		cstmt.setStringIn(sateikaisyaCd);
		// resultSet
		cstmt.setResultSet(RESULTSET);
		try {

			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);
			
			String shorikaisu = GS.EMPTY_CHARCTER;
			String max_sateiki = GS.EMPTY_CHARCTER;
	    	if ( rs.next() ) {
				// 最大処理回数
	    		shorikaisu = rs.getString(SYORI_KAISU);
	    		max_sateiki = rs.getString(SATEI_KI);
	    	}
	    	
		    // ActionForm に最大処理回数を格納する。
		    form.setShorikaisu(shorikaisu);    
		    form.setMaxSateiki(max_sateiki);    
		    
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
    
    /**
     * 文字列を連接する。 <br>
     * 
     * @param str1
     * @param str2
     * @return 連結した文字列
     */
    private String contract(String str1, String str2) {
		str1 = str1 == null ? GS.EMPTY_CHARCTER : str1;
		str2 = str2 == null ? GS.EMPTY_CHARCTER : str2;
		return str1 + str2;
	}
}