/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/30		SSC				課題No.175 金額フォーマット修正
003		2009/12/16		SSC				課題No.197 査定実施チェック追加対応
004		2009/12/24		SSC				課題No.210 所在国検索変更対応
005		2014/02/24		SSC				案件No.D12883 米国SAP対応
006		2015/12/14		SSC				案件No.BP201512020 照会画面の部検索時不具合対応
******************************************************************************/

package app.syokai.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.form.SateiForm;
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
* OS6101_査定内容照会 DBアクセスクラス <br>
*/
public class SateiDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private SateiForm form = null;						// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	
	//	Resultset用文字列
	private static final String KBN_VAL						= "kbn_val";							// 区分値
	private static final String KBN_HYOUJI_VAL				= "kbn_hyouji_val";						// 区分表示値
	private static final String KESSANKI					    = "kessanki"; 							// 区分キー（半期・四半期区分）
	private static final String TORIHIKISAKI2_KBN				= "torihikisaki2_kbn"; 					// 区分キー（取引先区分）
	private static final String SAIKEN_KBN					= "saiken_kbn"; 						// 区分キー（債権区分）
	private static final String JIYUU_CD						= "jiyuu_cd"; 							// 抽出事由コード
	private static final String JIYUU_NM						= "jiyuu_nm"; 							// 抽出事由名称
	private static final String SORT_SATEI         			= "sort_OS6101_satei";					// 区分キー（査定検索時）
	private static final String SORT_TAIRYU         			= "sort_OS6101_tairyu";					// 区分キー（滞留判定検索時）
	private static final String SORT_ORDER      				= "sort_order";							// 区分キー（整列方向）
	
	//課題No.210
	//修正開始
	//private static final String COUNTRY_NM					= "country_nm";							// 国名称
	//private static final String COUNTRY_NM_HY  				= "country_nm_hy";						// 国名称(表示用)
	private static final String COUNTRY_KENSAKU				= "country_kensaku";					// 国検索
	//修正完了
	private static final String SATEI_KAISHA_CD  				= "satei_kaisha_cd";					// 査定会社コード
	private static final String SATEI_KAISHA_HYOUJI  			= "satei_kaisha_hyouji";				// 査定会社コード(表示用)
	private static final String HANYOU2  						= "hanyou2";							// 汎用2
	private static final String HANYOU2_HY  					= "hanyou2_hy";							// 汎用2(表示用)
	private static final String HANYOU3  						= "hanyou3";							// 汎用3
	private static final String HANYOU3_HY  					= "hanyou3_hy";							// 汎用3(表示用)
	private static final String PHASE						    = "phase"; 								// 区分キー（フェーズ）	
	private static final String COMMON_OS61				    = "common_OS61"; 						// 区分キー（汎用）
	private static final String SHOW            				= "show";								// 区分キー（表示件数）
	private static final String SYSTEM_KBN            		= "system_kbn";							// システム区分
	private static final String SATEI_KAISYA_CD            	= "satei_kaisha_cd";					// 査定会社コード
	private static final String SATEIKAISYA_CD				= "sateikaisya_cd";						// 査定会社コード
	private static final String MISE_CD            			= "mise_cd";							// 店コード
	private static final String SATEI_KI            			= "satei_ki";							// 査定期
	private static final String KIKAN_TORI_CD            		= "kikan_tori_cd";						// 勘定先CD
	private static final String TORI_NM            			= "tori_nm";							// 勘定先名称
	private static final String PATTERN_ID            		= "pattern_id";							// 業務フローパターンID
	private static final String SHINCHOKU_KBN					= "shinchoku_kbn";						// 進捗区分
	private static final String SHINCHOKU						= "shinchoku";							// 進捗
	private static final String ANKEN_NO						= "anken_no";							// 案件No.
	private static final String BUNRUI2						= "bunrui2";							// 分類２
	private static final String INIT_BUNRUI2					= "init_bunrui2";						// 初期分類２
	private static final String BU_CD							= "bu_cd";								// 部コード
	private static final String INIT_BU_CD					= "init_bu_cd";						 	// 初期部コード
	private static final String INIT_BUNRUI3					= "init_bunrui3";						// 初期分類３
	private static final String SYORI_KAISU					= "syori_kaisu";						// 処理回数
	private static final String YM							= "ym";									// 対象年月
	private static final String YM_HY							= "ym_hy";								// 対象年月(表示用)
	private static final String STATUS						= "status";								// ステータス
	private static final String SASI_TEN_FLG					= "sasi_ten_flg";						// 差戻転送FLG
	private static final String KAISHA_CD						= "kaisha_cd";							// 会社コード
	private static final String TOGO_TORI_CD					= "togo_tori_cd";						// 統合取引先コード
	private static final String SIKIBETU_CD					= "sikibetu_cd";						// 識別コード
	private static final String WB_COUNTRY_NM					= "wb_country_nm";						// 所在国
	private static final String KTK							= "ktk";								// 格付
	private static final String ORA							= "ora";								// 組織
	private static final String KINGAKU						= "kingaku";							// 金額計
	private static final String TUUKA_CD						= "tuuka_cd";							// 通貨コード
	private static final String OYA_NM						= "oya_nm";								// 親会社名称
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";						// 基準日区分
	private static final String HANKI_SIHANKI_KBN				= "hanki_sihanki_kbn";					// 半期四半期区分
	private static final String JIYOU_NM						= "jiyou_nm";							// 抽出事由
	private static final String WB_COUNTRY_CD					= "wb_country_cd";						// 所在国コード
	private static final String ADDRESS						= "address";							// 所在地
	private static final String OYA_DUNS_NO					= "oya_duns_no";						// 親会社取引先コード
	private static final String FSS							= "fss";								// FSS
	private static final String DUNS_RATING					= "duns_rating";						// DUNS Rating
	private static final String OYA_KTK						= "oya_ktk";							// 親会社格付
	private static final String GAIBU_KTK						= "gaibu_ktk";							// 外部格付
	private static final String KTK_KIKAN						= "ktk_kikan";							// 格付機関
	private static final String OYA_ITTAI_DOKURITU			= "oya_ittai_dokuritu";					// 親会社一体独立
	private static final String TUIKA_HIKIATE					= "tuika_hikiate";						// 追加引当金額
	private static final String TORI_KBN						= "tori_kbn";							// 取引先区分
	private static final String SAIKEN_KBN_S					= "saiken_kbn";							// 照会用債権区分
	private static final String SANKO_SATEIKAISYA_CD_KEY		= "SATEIKAISYA_CD";						// 査定会社コード
	private static final String SANKO_SYSTEM_KBN_KEY			= "SYSTEM_KBN";							// システム区分
	private static final String SANKO_BUNRUI2_KEY 			= "BUNRUI2";							// 分類２
	
	private static final int MAX_RECDOE						= 1000;									// 最大記録
	// INパラメータ
    private String userId;                                                                             // ユーザＩＤ
	private String workflowSystemkbn;  
	
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0";      	   // 数字のフォーマット：国内
	
	private static final String SP_SS_O_SELECT_MAXSATEIKI		= "SP_SS_O_SELECT_MAXSATEIKI";        	 // 最新査定期取得処理
    private static final String SP_SS_OL_SELECT_P0200		   	= "SP_SS_OL_SELECT_P0200";        		 // システムセレクトボックス設定値取得処理
    private static final String SP_SS_OS_SELECT_G0400		   	= "SP_SS_OS_SELECT_G0400";        		 // 所在国セレクトボックスの設定値を取得処理
    private static final String SP_SS_O_SELECT_HYOUJIVAL		= "SP_SS_O_SELECT_HYOUJIVAL";        	 // 取引先区分セレクトボックスの設定値を取得処理
    private static final String SP_SS_OS6101_SELECT_M0700		= "SP_SS_OS6101_SELECT_M0700";        	 // 抽出事由セレクトボックスの設定値を取得処理
    private static final String SP_SS_OS_SELECT_M0200		   	= "SP_SS_OS_SELECT_M0200";        		 // 汎用1セレクトボックスの設定値を取得処理
    private static final String SP_SS_OS_SELECT_BUNNRUI2		= "SP_SS_OS_SELECT_BUNNRUI2";        	 // 汎用2セレクトボックスの設定値を取得処理
    private static final String SP_SS_OS_SELECT_M1200			= "SP_SS_OS_SELECT_M1200";        		 // 汎用3セレクトボックスの設定値を取得処理
    private static final String SP_SS_OS6101_SELECT_ICHIRANTS	= "SP_SS_OS6101_SELECT_ICHIRANTS";    	 // 一覧情報を取得処理（滞留判定検索時）
    private static final String SP_SS_OS6101_SELECT_ICHIRANSS	= "SP_SS_OS6101_SELECT_ICHIRANSS";    	 // 一覧情報を取得処理（査定検索時）
    private static final String SP_SS_OS6101_SELECT_M1700		= "SP_SS_OS6101_SELECT_M1700";    		 // 対象外参照権限の取得
    
    /**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SateiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (SateiForm)appContext.getActionForm();
		
        // ビーンの値を変数に設定
        this.userId = user_bean.getComUserId();
		this.workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
	}
	
	/**
	 * 変数初期化 <br>
	 */
	public void initialize() {
	}
	
	/**
	 * 最新査定期取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSateiki() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            String sateiki = GS.EMPTY_CHARCTER;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_MAXSATEIKI, sqlExec);
            exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());         
            exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
            exCstmt.setStringIn(workflowSystemkbn);  
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				while ( rs.next() ) {
					sateiki = Function.trim(rs.getString(SATEI_KI));
				}
				if(!(GS.EMPTY_CHARCTER.equals(sateiki)) && GS.LANG_EN.equals(cmnData.getComLangMode())){
					sateiki = sateiki.substring(4) + sateiki.substring(0,4);
				}
				form.setSateiki(sateiki);	    			
				
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}
	
	/**
	 * 半期・四半期区分セレクトボックスの設定値を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanki_sihanki() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(KESSANKI);
            exCstmt.setStringIn(cmnData.getComLangMode());         
            exCstmt.setStringIn(workflowSystemkbn);
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				LinkedHashMap<String,String> ar_hanki_sihanki = new LinkedHashMap<String,String>();
				while ( rs.next() ) {
					ar_hanki_sihanki.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				}
				
				form.setAr_hanki_sihanki(ar_hanki_sihanki);
					
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}
	
	/**
	 * 所在国【リスト】取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getCountry() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_G0400, sqlExec);
            //exCstmt.setStringIn(WB_COUNTRY_NM);
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				LinkedHashMap<String,String> ar_country = new LinkedHashMap<String,String>();
				while ( rs.next() ) {
					//初期設定
		    		//課題No.210
		    		//修正開始
					//ar_country.put(rs.getString(COUNTRY_NM),rs.getString(COUNTRY_NM_HY));
		    		ar_country.put(rs.getString(COUNTRY_KENSAKU),rs.getString(WB_COUNTRY_NM));	     			
		    		//修正完了
				}
				
				form.setAr_country(ar_country);
				
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}

	/**
	 * 取引先区分セレクトボックスの設定値を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTorihikisaki_kbn() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_HYOUJIVAL, sqlExec);
            exCstmt.setStringIn(TORIHIKISAKI2_KBN);
            exCstmt.setStringIn(userId);         
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				LinkedHashMap<String,String> ar_torihikisaki_kbn = new LinkedHashMap<String,String>();
				while ( rs.next() ) {
					ar_torihikisaki_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				}
				
				form.setAr_torihikisaki_kbn(ar_torihikisaki_kbn);
				
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}
	
	/**
	 * 債権区分セレクトボックスの設定値を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSaiken_kbn() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_HYOUJIVAL, sqlExec);
            exCstmt.setStringIn(SAIKEN_KBN);
            exCstmt.setStringIn(userId);         
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				LinkedHashMap<String,String> ar_saiken_kbn = new LinkedHashMap<String,String>();
				while ( rs.next() ) {
					ar_saiken_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				}
				
				form.setAr_saiken_kbn(ar_saiken_kbn);
				
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}
	
	/**
	 * 抽出事由セレクトボックスの設定値を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTyusyutu() throws SQLException {

			// ResultSet取得
            ResultSet rs = null;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6101_SELECT_M0700, sqlExec);
    		// 全参照システム区分
    		exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY));
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setResultSet(RESULTSET);
            try{
	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				LinkedHashMap<String,String> ar_tyusyutu = new LinkedHashMap<String,String>();
				while ( rs.next() ) {
					ar_tyusyutu.put(rs.getString(JIYUU_NM),rs.getString(JIYUU_CD));	    			
				}
				
				form.setAr_tyusyutu(ar_tyusyutu);
				
			} finally {
		    	if (rs != null) {
	    			// Resultset close
	    			rs.close();
	    		}
		    }
	}
	
	/**
	 * 汎用項目ラベル【リスト】取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyou() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(COMMON_OS61,workflowSystemkbn,cmnData.getComLangMode());

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
	 * 汎用1【リスト】取得 <br>
	 * 
	 * @exception SQLException
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
            exCstmt.setStringIn(form.getSateiki());
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
	 * ソート順セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSort() throws SQLException {

		ResultSet rs = null;

		//ソート項目取得
		try{
			//ResultSet取得
			if (form.getSatei_tairyu().equals("2")){
				
				//査定検索時
				rs = getKbnval(SORT_SATEI,workflowSystemkbn,cmnData.getComLangMode());				
			}else if (form.getSatei_tairyu().equals("1")){
				
				//滞留判定検索時
				rs = getKbnval(SORT_TAIRYU,workflowSystemkbn,cmnData.getComLangMode());
			}
			

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sort_item = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSort_item(rs.getString(KBN_VAL));
				}
				ar_sort_item.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			form.setAr_sort_item(ar_sort_item);
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	    
		rs = null;
		
		//整列方向取得
		try{
			//ResultSet取得
			rs = getKbnval(SORT_ORDER,workflowSystemkbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sort_order = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//初期設定
				if(i==0){
					form.setSort_order(rs.getString(KBN_VAL));
				}
				ar_sort_order.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));	    			
				i++;
			}
			form.setAr_sort_order(ar_sort_order);	    	
		} finally {
	    	if (rs != null) {
    			//Resultset close
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
			rs = getKbnval(SHOW,workflowSystemkbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_show = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				ar_show.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
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
	 * 対象外参照権限の取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public String getKengen() throws SQLException {
		
		String returnValue = null;
		
        ResultSet rs = null;
		try{
			
			List<HashMap> ar_workflowList = null;	// 業務フローパターン【リスト】
			
			// 業務フローパターン【リスト】
			ar_workflowList = user_bean.getComWorkflowList();
			
			StringBuffer sansyo_systemkbn = new StringBuffer();
			
			for (int i = 0; i < ar_workflowList.size(); i++){
				
				HashMap has_workflowList =(HashMap)ar_workflowList.get(i);
				// ResultSet取得
	            // ExCallableStatement生成
	            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6101_SELECT_M1700, sqlExec);
	            exCstmt.setStringIn((String)has_workflowList.get(SYSTEM_KBN));
	            exCstmt.setStringIn((String)has_workflowList.get(SATEIKAISYA_CD));
	            exCstmt.setStringIn((String)has_workflowList.get(PATTERN_ID));
	            exCstmt.setResultSet(RESULTSET);

	            // SQL実行 
	            exCstmt.execute();
	            isError(exCstmt);
	            rs = exCstmt.getResultSet(RESULTSET);
	            
				// ActionForm に取得値を格納
				if (rs.next()) {

		            if (i!=0) {
		            	sansyo_systemkbn.append(",'||'");
		            }
					sansyo_systemkbn.append("''"+rs.getString(SYSTEM_KBN)+"''");
				}
			}
			
			returnValue = "'"+sansyo_systemkbn.toString()+"'";
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
		
		return returnValue;
	}
	
	/**
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public boolean getMeisai(String bunrui2,String sansyo_systemkbn) throws SQLException {
		boolean returnValue = true;
		ResultSet rs = null;
		ExCallableStatement exCstmt = null;
		if ("2".equals(form.getSatei_tairyu())){
			
			//ExCallableStatement生成（査定検索時）
			exCstmt = new ExCallableStatement(SP_SS_OS6101_SELECT_ICHIRANSS, sqlExec);
			// 共)言語モード
			exCstmt.setStringIn("'"+cmnData.getComLangMode()+"'");
			// 機)処理日FROM検索
			exCstmt.setStringIn("'"+form.getKensaku_syori_dtFrom()+"'");
			// 機)処理日TO検索
			exCstmt.setStringIn("'"+form.getKensaku_syori_dtTo()+"'");
			// 機)関係者検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_parties()));
			// 機)査定期検索
			exCstmt.setStringIn(form.getKensaku_sateiki());
			// 機)対象年月検索
			exCstmt.setStringIn(form.getKensaku_month());
			// 機)勘定先CD検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_cd()));
			// 機)DUNS No.検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_duns_no()));
			// 機)勘定先名称検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_nm()));
			// 機)所在国検索
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
			// 機)全参照システム区分
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY));
			// 機)全参照査定会社コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SATEIKAISYA_CD_KEY));
			// 機)全参照分類2コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_BUNRUI2_KEY));
			// 機)システム区分検索
			exCstmt.setStringIn(form.getKensaku_systemKbn());
			// 機)汎用1検索
			exCstmt.setStringIn(form.getKensaku_hanyou1());
			// 機)参照分類２コード検索
			exCstmt.setStringIn(bunrui2);
			// 機)汎用2検索
			exCstmt.setStringIn(form.getKensaku_hanyou2());
			// 機)汎用3検索
			exCstmt.setStringIn(form.getKensaku_hanyou3());
			// 機)処理日FROM
			exCstmt.setStringIn(form.getSyori_dtFrom());
			// 機)処理日TO
			exCstmt.setStringIn(form.getSyori_dtTo());
			// ソート項目
			exCstmt.setStringIn(form.getSort_item());
			// 整列方向
			exCstmt.setStringIn(form.getSort_order());
			// 機)対象外参照可システム区分
			exCstmt.setStringIn(sansyo_systemkbn);
			// 機)抽出事由検索
			exCstmt.setStringIn(form.getKensaku_tyusyutu());
			// 機)取引先区分検索
			exCstmt.setStringIn(form.getKensaku_torihikisaki_kbn());
			// 機)債権区分検索
			exCstmt.setStringIn(form.getKensaku_saiken_kbn());
			// 機)半期・四半期区分検索
			exCstmt.setStringIn(form.getKensaku_hanki_sihanki());
			exCstmt.setResultSet(RESULTSET);
			
			
		}else if ("1".equals(form.getSatei_tairyu())){

			//ExCallableStatement生成（滞留判定検索時）
			exCstmt = new ExCallableStatement(SP_SS_OS6101_SELECT_ICHIRANTS, sqlExec);
			// 共)言語モード
			exCstmt.setStringIn("'"+cmnData.getComLangMode()+"'");
			// 機)処理日FROM検索
			exCstmt.setStringIn("'"+form.getKensaku_syori_dtFrom()+"'");
			// 機)処理日TO検索
			exCstmt.setStringIn("'"+form.getKensaku_syori_dtTo()+"'");
			// 機)関係者検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_parties()));
			// 機)査定期検索
			exCstmt.setStringIn(form.getKensaku_sateiki());
			// 機)対象年月検索
			exCstmt.setStringIn(form.getKensaku_month());
			// 機)勘定先CD検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_cd()));
			// 機)DUNS No.検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_duns_no()));
			// 機)勘定先名称検索
			exCstmt.setStringIn(Function.addSingleQuotation(form.getKensaku_kanjo_nm()));
			// 機)所在国検索
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
			// 機)全参照システム区分
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SYSTEM_KBN_KEY));
			// 機)全参照査定会社コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_SATEIKAISYA_CD_KEY));
			// 機)全参照分類2コード
			exCstmt.setStringIn((String)user_bean.getComSansyososhiki_all().get(SANKO_BUNRUI2_KEY));
			// 機)システム区分検索
			exCstmt.setStringIn(form.getKensaku_systemKbn());
			// 機)汎用1検索
			exCstmt.setStringIn(form.getKensaku_hanyou1());
			// 機)参照分類２コード検索
			exCstmt.setStringIn(bunrui2);
			// 機)汎用2検索
			exCstmt.setStringIn(form.getKensaku_hanyou2());
			// 機)汎用3検索
			exCstmt.setStringIn(form.getKensaku_hanyou3());
			// 機)処理日FROM
			exCstmt.setStringIn(form.getSyori_dtFrom());
			// 機)処理日TO
			exCstmt.setStringIn(form.getSyori_dtTo());
			// ソート項目
			exCstmt.setStringIn(form.getSort_item());
			// 整列方向
			exCstmt.setStringIn(form.getSort_order());
			exCstmt.setResultSet(RESULTSET);
		}

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
				// id
				listBean.setId(Function.getStringOfInt(i));
				
				// 進捗区分
				listBean.setSincyoku_kbn(rs.getString(SHINCHOKU_KBN));
				
				// 案件No.
				listBean.setAnken_no(rs.getString(ANKEN_NO));
				
				// 勘定先CD
				listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
				
				//システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				
				//店コード
				listBean.setMise_cd(rs.getString(MISE_CD));
				
				//査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEI_KAISYA_CD));
				
				//分類２
				listBean.setBunrui2(rs.getString(BUNRUI2));	
				
				//初期分類２
				listBean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
				
				//部コード
				listBean.setBu_cd(rs.getString(BU_CD));
				
				//初期部コード
				listBean.setInit_bu_cd(rs.getString(INIT_BU_CD));
				
				//初期分類３
				listBean.setInit_bunrui3(rs.getString(INIT_BUNRUI3));
				
				//査定期
				listBean.setSatei_ki(rs.getString(SATEI_KI));
				
				//処理回数
				listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));
				
				//対象年月
				listBean.setTaisyo_ym(rs.getString(YM));
				
				//対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(YM_HY));
				
				//フェーズ
				listBean.setPhase(rs.getString(PHASE));
				
				//ステータス
				listBean.setStatus(rs.getString(STATUS));
				
				//差戻転送FLG
				listBean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));
				
				//進捗
				listBean.setSintyoku(rs.getString(SHINCHOKU));
				
				//会社コード
				listBean.setKaisya_cd(rs.getString(KAISHA_CD));
				
				//統合取引先コード
				listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				
				//識別コード
				listBean.setShikibetu_cd(rs.getString(SIKIBETU_CD));
				
				//勘定先名称
				listBean.setKanjo_nm(rs.getString(TORI_NM));
				
				//所在国
				listBean.setSyozaikoku(rs.getString(WB_COUNTRY_NM));
				
				//格付
				listBean.setSinyoktk(rs.getString(KTK));
				
				//組織
				listBean.setSoshiki(rs.getString(ORA));
				
				//課題No.175
				//修正開始
				//金額計
				//listBean.setKingaku(formatKingaku(Function.getValueOfDouble(rs.getString(KINGAKU)),workflowSystemkbn));
				listBean.setKingaku(formatKingaku(Function.getValueOfDouble(rs.getString(KINGAKU)),listBean.getSystem_kbn()));
				//修正完了
				
				//通貨コード
				listBean.setTuuka_cd(rs.getString(TUUKA_CD));
				
				//課題No.197
				//追加開始
				//半期四半期区分
				listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
				if("2".equals(rs.getString(HANKI_SIHANKI_KBN))){
					listBean.setTaisyo_ym_hyoji(Function.addQuarter(rs.getString(YM_HY)));
				}
				//追加完了

				if ("2".equals(form.getSatei_tairyu())){
					
					//課題No.197
					//削除開始
					// 半期四半期区分
					//listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
					//if("2".equals(rs.getString(HANKI_SIHANKI_KBN))){
						//listBean.setTaisyo_ym_hyoji(Function.addQuarter(rs.getString(YM_HY)));
					//}
					//削除完了
					
					// 基準日区分
					listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
					
					// 抽出事由
					listBean.setJiyu_nm(rs.getString(JIYOU_NM));
					
					// 所在国コード
					listBean.setSyozaikoku_cd(rs.getString(WB_COUNTRY_CD));
					listBean.setWb_country_cd(rs.getString(WB_COUNTRY_CD));
					
					// 所在地
					listBean.setSyozaichi(rs.getString(ADDRESS));
					
					// 親会社取引先コード
					listBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
					
					// FSS
					listBean.setFss(rs.getString(FSS));
					
					// DUNS Rating
					listBean.setDuns_rating(rs.getString(DUNS_RATING));
					
					// 親会社格付
					listBean.setOya_ktk(rs.getString(OYA_KTK));
					
					// 外部格付
					listBean.setGaibu_ktk(rs.getString(GAIBU_KTK));
					
					// 格付機関
					listBean.setKtk_kikan(rs.getString(KTK_KIKAN));
					
					// 親会社一体独立
					listBean.setOya_ittai_dokuritu(rs.getString(OYA_ITTAI_DOKURITU));
					
					// 親会社名称
					listBean.setOya_business_nm(rs.getString(OYA_NM));
					
					//課題No.175
					//修正開始
					// 追加引当金額
					//listBean.setTuika_kingaku(formatKingaku(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)),workflowSystemkbn));
					listBean.setTuika_kingaku(formatKingaku(Function.getValueOfDouble(rs.getString(TUIKA_HIKIATE)),listBean.getSystem_kbn()));
					//修正完了
					
					// 照会用取引先区分
					listBean.setTori_kbn_nm(rs.getString(TORI_KBN));
					
					// 照会用債権区分
					listBean.setSai_kbn_nm(rs.getString(SAIKEN_KBN_S));
					
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
	    			//Resultset close
	    			rs.close();
	    		} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
	    		}
	    	}
	    }
	    
	    return returnValue;
	}
	
	/**
	 * 参照分類２コード取得 <br>
	 * 
	 * @exception SQLException
	 */
	public String getBunrui2() throws SQLException {
		String returnValue = null;
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
            
			StringBuffer sbf = new StringBuffer();
			int i = 0;
			while ( rs.next() ) {

				if(i!=0){
					sbf.append(",");
				}
				returnValue = sbf.append("'"+rs.getString(HANYOU2)+"'").toString();
				i++;
			}
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
		return returnValue;
	}
	
    /**
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    public String formatKingaku(double kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if (systemKbn.equals(GS.GSS)){
            //国内
        	formatKingaku = Function.format(NUM_FMT_KAIGAI, kingaku);
        }else{
            //海外
        	formatKingaku = Function.format(NUM_FMT_KOKUNAI, kingaku);
        }
        return formatKingaku;
    }
}