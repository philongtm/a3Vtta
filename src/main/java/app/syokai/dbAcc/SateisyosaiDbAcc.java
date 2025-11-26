/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/01		SSC				課題No.178 組織取得処理修正
003		2009/12/16		SSC				課題No.197 査定実施チェック追加対応
******************************************************************************/

package app.syokai.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.form.SateisyosaiForm;
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
* OS6102_査定内容詳細 DBアクセスクラス <br>
*/
public class SateisyosaiDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private TorihikisakiBean tori_bean = null;			// 取引先情報
	private SateisyosaiForm form = null;				// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	
	//	Resultset用文字列
	private static final String HIHYOUZI_FLG 					= "hihyouzi_flg";						// コメント非表示フラグ
	private static final String ANKEN_NO						= "anken_no";							// 案件No.
	private static final String ANKEN_PHASE					= "anken_phase";						// 案件フェーズ
	private static final String PHASE_HY						= "phase_hy";							// フェーズ表示値
	private static final String PHASE							= "phase";								// フェーズ表示値
    //課題No.197
    //追加開始
	private static final String CNT          			        = "cnt";
    //追加完了
	private static final String SYSTEM_KBN            		= "system_kbn";							// システム区分
	private static final String SATEIKAISYA_CD            	= "satei_kaisha_cd";					// 査定会社コード
	private static final String MISE_CD            			= "mise_cd";							// 店コード
	private static final String SATEI_KI            			= "satei_ki";							// 査定期
	private static final String KIKAN_TORI_CD            		= "kikan_tori_cd";						// 勘定先CD
	private static final String TORI_NM            			= "tori_nm";							// 勘定先名称
	private static final String SHINCHOKU_KBN					= "shinchoku_kbn";						// 進捗区分
	private static final String SHINCHOKU						= "shinchoku";							// 進捗
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
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";
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
	
	private static final String SYRI_DT_TITLE 				= "syri_dt_title";						// 処理日タイトル
	private static final String TOUROKU_TANTO 				= "touroku_tanto";						// 登録担当者
	private static final String TOUROKU_SYORI_DT 				= "touroku_syori_dt";					// 登録処理日
	private static final String SHOUNIN_TANTO 				= "shounin_tanto";						// 承認担当者
	private static final String SHOUNIN_SYORI_DT 				= "shounin_syori_dt";					// 承認処理日
	private static final String C_PHASE 						= "c_phase";							// 帳票用フェーズ
	
	// INパラメータ
	private String workflowSystemkbn;  
	
	private static final String SP_SS_OS6102_SELECT_T1200		= "SP_SS_OS6102_SELECT_T1200";        	 // コメント表示リンク非表示フラグを取得処理
    private static final String SP_SS_OS6102_SELECT_T1400		= "SP_SS_OS6102_SELECT_T1400";        	 // フェーズセレクトボックス取得用案件No.を取得処理
    private static final String SP_SS_OS6102_SELECT_T1401		= "SP_SS_OS6102_SELECT_T1401";        	 // フェーズセレクトボックス取得用案件No.を取得処理    
    private static final String SP_SS_OS6102_SELECT_T1000		= "SP_SS_OS6102_SELECT_T1000";        	 // フェーズ【リスト】処理（滞留時）
    private static final String SP_SS_OS6102_SELECT_T1500		= "SP_SS_OS6102_SELECT_T1500";     		 // フェーズ【リスト】処理（査定時）
    private static final String SP_SS_OS6102_SELECT_TAIRYU	= "SP_SS_OS6102_SELECT_TAIRYU";        	 // 取引先情報を取得処理（滞留時）
    private static final String SP_SS_OS6102_SELECT_SATEI		= "SP_SS_OS6102_SELECT_SATEI";        	 // 取引先情報を取得処理（査定時）    
    private static final String SP_SS_OS6102_SELECT_TANTOUSHA	= "SP_SS_OS6102_SELECT_TANTOUSHA";       // 登録/承認担当者【リスト】を取得処理    
    private static final String SP_SS_OS6102_SELECT_PHASE		= "SP_SS_OS6102_SELECT_PHASE";        	 // フェーズ【リスト】を取得処理
    //課題No.197
    //追加開始
    private static final String SP_SS_OS6102_SELECT_SATEICHK	= "SP_SS_OS6102_SELECT_SATEICHK";        // 査定実施中案件チェック
    //追加完了

    /**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SateisyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		tori_bean = cmnData.getTori_bean();
		form = (SateisyosaiForm)appContext.getActionForm();
		
        // ビーンの値を変数に設定
		this.workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
	}
	
	/**
	 * 変数初期化 <br>
	 */
	public void initialize() {
	}
	
	/**
	 * コメント表示リンク非表示フラグを取得処理 <br>
	 * 
	 * @return コメント表示リンク非表示フラグ
	 * @exception SQLException
	 */
	public String getComentHihyoujiFlg() throws SQLException {

		String hihyouzi_flg = GS.EMPTY_CHARCTER;
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_T1200, sqlExec);
            cstmt.setStringIn(tori_bean.getAnken_no());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			if ( rs.next() ) {
				hihyouzi_flg = rs.getString(HIHYOUZI_FLG);
			}
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
		return hihyouzi_flg;
	}
	
	/**
	 * フェーズセレクトボックス取得用案件No.を取得処理 <br>
	 * 移元画面IDが｢OS6101]
	 * @exception SQLException
	 */
	public void getAnken_no() throws SQLException {
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_T1400, sqlExec);
            cstmt.setStringIn(tori_bean.getSystem_kbn());
            cstmt.setStringIn(tori_bean.getSateikaisya_cd());
            cstmt.setStringIn(tori_bean.getMise_cd());
            cstmt.setStringIn(tori_bean.getKanjo_cd());
            cstmt.setStringIn(tori_bean.getSatei_ki());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				form.setKensaku_anken_no(rs.getString(ANKEN_NO));	    			
			}
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * フェーズセレクトボックス取得用案件No.を取得処理 <br>
	 * 移元画面IDが｢OD1102][引当金検証]
	 * @exception SQLException
	 */
	public void getAnken_no_hiki() throws SQLException {

		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_T1401, sqlExec);
            cstmt.setStringIn(tori_bean.getSystem_kbn());
            cstmt.setStringIn(tori_bean.getSateikaisya_cd());
            cstmt.setStringIn(tori_bean.getMise_cd());
            cstmt.setStringIn(tori_bean.getKanjo_cd());
            cstmt.setStringIn(tori_bean.getSatei_ki());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				form.setKensaku_anken_no(rs.getString(ANKEN_NO));	    			
			}
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}


	
	/**
	 * フェーズ【リスト】を取得 <br>
	 * 滞留判定の場合
	 * @exception SQLException
	 */
	public void getPhase_ts() throws SQLException {

		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_T1000, sqlExec);
            cstmt.setStringIn(tori_bean.getSystem_kbn());
            cstmt.setStringIn(cmnData.getComLangMode());
            cstmt.setStringIn(tori_bean.getAnken_no());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_phase = new LinkedHashMap<String,String>();
			while ( rs.next() ) {
				ar_phase.put(rs.getString(PHASE_HY), rs.getString(ANKEN_PHASE));
			}
			
			form.setAr_phase(ar_phase);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}

	/**
	 * フェーズ【リスト】を取得 <br>
	 * 査定の場合
	 * @exception SQLException
	 */
	public void getPhase_th() throws SQLException {

		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_T1500, sqlExec);
            cstmt.setStringIn(tori_bean.getSystem_kbn());
            cstmt.setStringIn(cmnData.getComLangMode());
            cstmt.setStringIn(form.getKensaku_anken_no());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_phase = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				//IT134対応
				if (i == 0 && (GS.OD1102.equals(cmnData.getReturn_gamenId()) || GS.OD1105.equals(cmnData.getReturn_gamenId()))) {
				//IT134ここまで
					// フェーズセレクトボックスの初期値として、機)案件フェーズにフェーズセレクトボックス設定値の1件目を設定する。
					form.setAnken_phase(rs.getString(ANKEN_PHASE));
				}
				ar_phase.put(rs.getString(PHASE_HY), rs.getString(ANKEN_PHASE));
				i++;
			}
			
			form.setAr_phase(ar_phase);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * フェーズ【リスト】を取得する。(読取専用セレクトボックス用) <br>
	 * 
	 * @exception SQLException
	 */
	public void getPhase_read() throws SQLException {
		
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_PHASE, sqlExec);
            cstmt.setStringIn(tori_bean.getSystem_kbn());
            cstmt.setStringIn(cmnData.getComLangMode());
            cstmt.setStringIn(tori_bean.getAnken_no());
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            
			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_phase = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {
				if (i == 0) {
					form.setAnken_phase(rs.getString(PHASE));
				}
				ar_phase.put(rs.getString(PHASE_HY), rs.getString(PHASE));
				i++;
			}
			
			form.setAr_phase(ar_phase);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 登録/承認担当者【リスト】を取得する。 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTantoList() throws SQLException {
		
		//IT161対応
		if(GS.EMPTY_CHARCTER.equals(Function.trim(form.getAnken_phase()))){
			return;
		}
		//IT161ここまで

		// 共)取引先情報.基幹システム区分
		String systemKbn = tori_bean.getSystem_kbn();
		// 共)取引先情報.査定会社コード
		String sateiKaisyaCd = tori_bean.getSateikaisya_cd(); 
		// 共)取引先情報.初期分類2
		String initBunrui2 = tori_bean.getInit_bunrui2();
		// 共)言語モード
		String langMode = cmnData.getComLangMode();
		// 機)案件フェーズ
		String[] ar_anken_phase = form.getAnken_phase().split(",");
		// 機)案件フェーズ.案件No.
		String ankenNo = ar_anken_phase[0];
		// 機)案件フェーズ.フェーズ
		String phase = ar_anken_phase[1];

		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		try{
            // ExCallableStatement生成
            cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_TANTOUSHA, sqlExec);
            cstmt.setStringIn(systemKbn);
            cstmt.setStringIn(sateiKaisyaCd);
            cstmt.setStringIn(initBunrui2);
            cstmt.setStringIn(langMode);
            cstmt.setStringIn(ankenNo);
            cstmt.setStringIn(phase);
            
            cstmt.setResultSet(RESULTSET);

            // SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            List<Map<String, String>> ar_toroku_shounin = new ArrayList<Map<String, String>>();
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				Map<String,String> toroku_shounin = new HashMap<String,String>();
				toroku_shounin.put(PHASE, rs.getString(PHASE));
				toroku_shounin.put(TOUROKU_SYORI_DT, rs.getString(TOUROKU_SYORI_DT));
				toroku_shounin.put(SHOUNIN_SYORI_DT, rs.getString(SHOUNIN_SYORI_DT));
				toroku_shounin.put(TOUROKU_TANTO, rs.getString(TOUROKU_TANTO));
				toroku_shounin.put(SHOUNIN_TANTO, rs.getString(SHOUNIN_TANTO));
				ar_toroku_shounin.add(toroku_shounin);
				form.setSyoribiTitle(rs.getString(SYRI_DT_TITLE));
			}
			
			form.setAr_toroku_shounin(ar_toroku_shounin);
			
		} finally {
	    	if (rs != null) {
    			// Resultset close
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 取引先情報を取得する <br>
	 * 滞留判定検索時
	 * @exception SQLException
	 */
	public void getToriInfoTairyu() throws SQLException {

		if(GS.EMPTY_CHARCTER.equals(Function.trim(form.getAnken_phase()))){
			return;
		}

		// 案件フェーズ
		String anken_phase[] = form.getAnken_phase().split(",");
	
		//ExCallableStatement生成（滞留判定検索時）
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_TAIRYU, sqlExec);
			
		cstmt.setStringIn(cmnData.getComLangMode());
		cstmt.setStringIn(anken_phase[0]);
		cstmt.setStringIn(anken_phase[1]);
		cstmt.setResultSet(RESULTSET);
		
	    try {
			//SQL実行	
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);	 
	    	
	    	if ( rs.next() ) {
				
				//フェーズ
				tori_bean.setPhase(rs.getString(PHASE));
				
				//ステータス
				tori_bean.setStatus(rs.getString(STATUS));
				
				//進捗
				tori_bean.setSintyoku(rs.getString(SHINCHOKU));
				
				//金額計
				tori_bean.setKingaku(rs.getString(KINGAKU));
				
				//通貨コード
				tori_bean.setTuuka_cd(rs.getString(TUUKA_CD));
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
	}	
	
	/**
	 * 取引先情報を取得する <br>
	 * 査定検索時
	 * @exception SQLException
	 */
	public void getToriInfoSatei() throws SQLException {
		
		//課題No.178
		//追加開始
		String systemKbn = cmnData.getTori_bean().getSystem_kbn();
		//追加完了

		if(GS.EMPTY_CHARCTER.equals(Function.trim(form.getAnken_phase()))){
			return;
		}
		
		// 案件フェーズ
		String anken_phase[] = form.getAnken_phase().split(",");
		
		//ExCallableStatement生成
		ExCallableStatement cstmt = null;
		ResultSet rs = null;
		cstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_SATEI, sqlExec);
		
		cstmt.setStringIn(cmnData.getComLangMode());
		cstmt.setStringIn(anken_phase[0]);
		cstmt.setStringIn(anken_phase[1]);
		//課題No.178
		//修正開始
		//cstmt.setStringIn(workflowSystemkbn);
		cstmt.setStringIn(systemKbn);
		//修正完了
		cstmt.setResultSet(RESULTSET);
		
	    try {
			//SQL実行	
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);	 
	    	
	    	if ( rs.next() ) {
				// 取引先情報
	    		TorihikisakiBean tori_bean = new TorihikisakiBean();
	    		
				// 基準日区分
	    		tori_bean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));

	    		// 進捗区分
				tori_bean.setSincyoku_kbn(rs.getString(SHINCHOKU_KBN));
				
				// 案件No.
				tori_bean.setAnken_no(rs.getString(ANKEN_NO));
				
				// 勘定先CD
				tori_bean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
				
				//システム区分
				tori_bean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				
				//店コード
				tori_bean.setMise_cd(rs.getString(MISE_CD));
				
				//査定会社コード
				tori_bean.setSateikaisya_cd(rs.getString(SATEIKAISYA_CD));
				
				//分類２
				tori_bean.setBunrui2(rs.getString(BUNRUI2));	
				
				//初期分類２
				tori_bean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
				
				//部コード
				tori_bean.setBu_cd(rs.getString(BU_CD));
				
				//初期部コード
				tori_bean.setInit_bu_cd(rs.getString(INIT_BU_CD));
				
				//初期分類３
				tori_bean.setInit_bunrui3(rs.getString(INIT_BUNRUI3));
				
				//査定期
				tori_bean.setSatei_ki(rs.getString(SATEI_KI));
				
				//処理回数
				tori_bean.setSyori_kaisu(rs.getString(SYORI_KAISU));
				
				//対象年月
				tori_bean.setTaisyo_ym(rs.getString(YM));
				
				//対象年月(画面表示用)
				tori_bean.setTaisyo_ym_hyoji(rs.getString(YM_HY));
				
				//フェーズ
				tori_bean.setPhase(rs.getString(PHASE));
				
				//帳票用フェーズ
				tori_bean.setC_phase(rs.getString(C_PHASE));
				
				//ステータス
				tori_bean.setStatus(rs.getString(STATUS));
				
				//差戻転送FLG
				tori_bean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));
				
				//進捗
				tori_bean.setSintyoku(rs.getString(SHINCHOKU));
				
				//会社コード
				tori_bean.setKaisya_cd(rs.getString(KAISHA_CD));
				
				//統合取引先コード
				tori_bean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				
				//識別コード
				tori_bean.setShikibetu_cd(rs.getString(SIKIBETU_CD));
				
				//勘定先名称
				tori_bean.setKanjo_nm(rs.getString(TORI_NM));
				
				//所在国
				tori_bean.setSyozaikoku(rs.getString(WB_COUNTRY_NM));

				//格付
				tori_bean.setSinyoktk(rs.getString(KTK));
				
				//組織
				tori_bean.setSoshiki(rs.getString(ORA));
				
				//金額計
				tori_bean.setKingaku(rs.getString(KINGAKU));
				
				//通貨コード
				tori_bean.setTuuka_cd(rs.getString(TUUKA_CD));
					
				// 半期四半期区分
				tori_bean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
					
				// 抽出事由
				tori_bean.setJiyu_nm(rs.getString(JIYOU_NM));
					
				// 所在国コード
				tori_bean.setSyozaikoku_cd(rs.getString(WB_COUNTRY_CD));
				tori_bean.setWb_country_cd(rs.getString(WB_COUNTRY_CD));
				
				// 所在地
				tori_bean.setSyozaichi(rs.getString(ADDRESS));
					
				// 親会社取引先コード
				tori_bean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
					
				// FSS
				tori_bean.setFss(rs.getString(FSS));
					
				// DUNS Rating
				tori_bean.setDuns_rating(rs.getString(DUNS_RATING));
					
				// 親会社格付
				tori_bean.setOya_ktk(rs.getString(OYA_KTK));
					
				// 外部格付
				tori_bean.setGaibu_ktk(rs.getString(GAIBU_KTK));
					
				// 格付機関
				tori_bean.setKtk_kikan(rs.getString(KTK_KIKAN));
					
				// 親会社一体独立
				tori_bean.setOya_ittai_dokuritu(rs.getString(OYA_ITTAI_DOKURITU));
				
				// IT147対応
				// 追加引当金額
				tori_bean.setTuika_kingaku(rs.getString(TUIKA_HIKIATE));
				// IT147ここまで
					
				// 取引先区分
				tori_bean.setTori_kbn(rs.getString(TORI_KBN));
					
				// 照会用債権区分
				tori_bean.setSai_kbn(rs.getString(SAIKEN_KBN_S));
				
				cmnData.setTori_bean(tori_bean);
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
	}	
    /**
     * 査定中案件存在チェック <br>
     * 
     * @exception SQLException
     */
    public boolean getSateiCheck() throws SQLException {
    	boolean rtn = Boolean.TRUE;
    	//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6102_SELECT_SATEICHK,sqlExec);
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
        exCstmt.setStringIn(tori_bean.getSateikaisya_cd());
        exCstmt.setStringIn(tori_bean.getMise_cd());
        exCstmt.setStringIn(tori_bean.getTaisyo_ym());
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(tori_bean.getKanjo_cd());
        exCstmt.setStringIn(tori_bean.getHanki_sihanki_kbn());
		exCstmt.setIntOut(CNT);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
			if(exCstmt.getInt(CNT) != 0){
				rtn = Boolean.FALSE;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
        return rtn;
    }
}