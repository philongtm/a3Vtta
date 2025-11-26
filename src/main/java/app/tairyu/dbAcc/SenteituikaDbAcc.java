/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.50 査定期表示対応 
003		2009/11/16		SSC				課題No.114 検索値退避処理 
******************************************************************************/
package app.tairyu.dbAcc;


import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SenteituikaForm;
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
* OB2103_対象先選定_追加対象先選択 DBアクセスクラス
*/
public class SenteituikaDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;				// 機能共通セッション
	private UserBean user_bean = null;					// ユーザ情報
	private SenteituikaForm form = null;				// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String TAISHO_YM            = "taisho_ym";	
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KBN_VAL              = "kbn_val";
	private static final String TORI_CD              = "tori_cd";
	private static final String BUSINESS_NM          = "business_nm";
	private static final String SAIKEN_KINGAKU       = "saiken_kingaku";
	private static final String TUUKA_CD             = "tuuka_cd";	
	private static final String YM                   = "ym";
	private static final String YM_HYOJI             = "ym_hyoji";
	private static final String SATEIKAISYA_CD       = "sateikaisya_cd";
	private static final String SOSHIKI              = "soshiki";
	private static final String KTK                  = "ktk";
	private static final String GAIBU_KTK            = "gaibu_ktk";
	private static final String KTK_KIKAN            = "ktk_kikan";
	private static final String FSS                  = "fss";
	private static final String DUNS_RATING          = "duns_rating";
	private static final String OYA_DUNS_NO          = "oya_duns_no";
	private static final String OYA_KTK              = "oya_ktk";
	private static final String OYA_ITTAI_DOKURITU   = "oya_ittai_dokuritu";
	private static final String TOGO_TORI_CD         = "togo_tori_cd";
	private static final String SATEI_KI             = "satei_ki";
	private static final String HYOJI_SATEI_KI       = "hyoji_satei_ki";
	private static final String KIKAN_KAISHA_CD      = "kikan_kaisha_cd";	
	private static final String HANKI_SIHANKI_KBN    = "hanki_sihanki_kbn";
	private static final String SYSTEM_KBN           = "system_kbn";
	private static final String MISE_CD              = "mise_cd";
	private static final String KIJUNBI_KBN          = "kijunbi_kbn";
	private static final String SYORI_KAISU          = "syori_kaisu";
	// 課題No.50
	// 追加開始
	private static final String SATEIKI_HYOJI			= "satei_ki_hyoji";
	// 追加完了

	private static final String SHOW            = "show";			//区分キー（表示件数）
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.##";   // 金額のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";   // 金額のフォーマット：海外
	
	private static final String SP_SS_OB2103_SELECT_T2500     = "SP_SS_OB2103_SELECT_T2500";		//査定期取得プロシージャ
	private static final String SP_SS_OB2103_SELECT_M0400     = "SP_SS_OB2103_SELECT_M0400";		//汎用２取得処理プロシージャ
	private static final String SP_SS_O_SELECT_M2200          = "SP_SS_O_SELECT_M2200";			//対象年月取得処理プロシージャ
	private static final String SP_SS_OB2103_SELECT_ICHIRAN   = "SP_SS_OB2103_SELECT_ICHIRAN";	//追加対象先一覧情報取得プロシージャ
	
	// INパラメータ
	private String satei_kaisha_cd;	// 業務フローパターン査定会社コード
	private String sansyoBunrui2;		// 参照分類２コード
	private String workflowSystemkbn;	// 業務フローパターンシステム区分
    private String comLangMode;        // 共)言語モード

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SenteituikaDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (SenteituikaForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		satei_kaisha_cd = user_bean.getComWorkflowSateikaisya_cd();
		sansyoBunrui2 = user_bean.getComSansyoBunrui2();
		workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    satei_kaisha_cd = GS.EMPTY_CHARCTER;
	    sansyoBunrui2 = GS.EMPTY_CHARCTER;
	    workflowSystemkbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
	}

	/**
	 * 査定期取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSateiki() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2103_SELECT_T2500, sqlExec);
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(comLangMode);
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
				// 国内は最新査定期のみ格納
	    		if(workflowSystemkbn.equals(GS.GSS) && i==0){
		    		ar_sateiki.put(rs.getString(HYOJI_SATEI_KI),rs.getString(SATEI_KI));
	    		}else if(!workflowSystemkbn.equals(GS.GSS)){
		    		ar_sateiki.put(rs.getString(HYOJI_SATEI_KI),rs.getString(SATEI_KI));
	    		}
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
	 * 汎用２取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyou2() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2103_SELECT_M0400, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(sansyoBunrui2);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
	    	// ActionForm に取得値を格納
	    	LinkedHashMap<String,String> ar_hanyou2 = new LinkedHashMap<String,String>();
    		ar_hanyou2.put(GS.EMPTY_CHARCTER,GS.EMPTY_CHARCTER);	    			

	    	int i = 0;
	    	while ( rs.next() ) {
	    		ar_hanyou2.put(rs.getString(KIKAN_KAISHA_CD),rs.getString(KIKAN_KAISHA_CD));	    			
	    		i++;
	    	}
	    	form.setAr_hanyou2(ar_hanyou2);	
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
			rs = getKbnval(SHOW,workflowSystemkbn,comLangMode);

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
	 * 年月取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getTaisho_ym() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		//課題No.114
		//修正開始
		//exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(form.getKensaku_sateiki());
		//修正完了
		exCstmt.setStringIn(workflowSystemkbn);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	StringBuffer sbTaisho_ym = new StringBuffer();
	    	int i = 0;
	    	while ( rs.next() ) {
	    		// シングルクォートで括りカンマ区切りで格納
	    		if(i == 0){
	    			sbTaisho_ym.append(GS.SINGLE_QUOTATION)
	    					   .append(rs.getString(TAISHO_YM))
	    					   .append(GS.SINGLE_QUOTATION);
	    		}else{
	    			sbTaisho_ym.append(GS.COMMA)
	    			           .append(GS.SINGLE_QUOTATION)
	    					   .append(rs.getString(TAISHO_YM))
	    					   .append(GS.SINGLE_QUOTATION);
	    		}
	    		i++;
	    	}
	    	if(i==0){
    			sbTaisho_ym.append(GS.SINGLE_QUOTATION)
				   .append(GS.SINGLE_QUOTATION);
	    	}
	    	
	    	// ActionForm に取得値を格納
	    	form.setTaisho_ym(sbTaisho_ym.toString());	
	    } finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
	
	/**
	 * 検索処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getMeisai() throws SQLException {

		ResultSet rs = null;
		InputCheck check = new InputCheck();
        String formatType = GS.EMPTY_CHARCTER;

        //金額のフォーマット
        if(GS.GSS.equals(workflowSystemkbn)){
        	formatType = NUM_FMT_KOKUNAI;
        }else{
        	formatType = NUM_FMT_KAIGAI;
        }
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2103_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getTaisho_ym());
		exCstmt.setStringIn(workflowSystemkbn);
		//課題No.114
		//修正開始
		//exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setStringIn(form.getKensaku_hanyou2());
		exCstmt.setStringIn(sansyoBunrui2);
		//exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(form.getKensaku_sateiki());
		//exCstmt.setStringIn(form.getKanjo_cd());
		exCstmt.setStringIn(form.getKensaku_kanjo_cd());
		//exCstmt.setStringIn(form.getKanjo_nm());
		exCstmt.setStringIn(form.getKensaku_kanjo_nm());
		//修正完了
		exCstmt.setStringIn(comLangMode);
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
				
				//id
				listBean.setId(Function.getStringOfInt(i));
				
				// 勘定先コード
				listBean.setKanjo_cd(rs.getString(TORI_CD));
				
				// 勘定先名称
				listBean.setKanjo_nm(rs.getString(BUSINESS_NM));

				// 債権残計
				if(!check.isNullBlank(rs.getString(SAIKEN_KINGAKU))){
					listBean.setSaiken_kingaku(Function.format(formatType,rs.getDouble(SAIKEN_KINGAKU))+ rs.getString(TUUKA_CD));
				}
				
				// 対象年月
				listBean.setTaisyo_ym(rs.getString(YM));

				// 対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(YM_HYOJI));

				// 査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEIKAISYA_CD));
				
				// 分類２(海外のみ設定)
				if (!GS.GSS.equals(workflowSystemkbn)){
					listBean.setBunrui2(rs.getString(MISE_CD));
				}
				
				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				
				// 信用格付情報
				listBean.setSinyoktk(rs.getString(KTK));
				
				// 外部格付
				listBean.setGaibu_ktk(rs.getString(GAIBU_KTK));
				
				// 格付機関
				listBean.setKtk_kikan(rs.getString(KTK_KIKAN));
				
				// FSS
				listBean.setFss(rs.getString(FSS));
				
				// DUNS Rating
				listBean.setDuns_rating(rs.getString(DUNS_RATING));
				
				// 親会社DUNS_NO
				listBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
				
				// 親会社格付
				listBean.setOya_ktk(rs.getString(OYA_KTK));
				
				// 親会社一体独立
				listBean.setOya_ittai_dokuritu(rs.getString(OYA_ITTAI_DOKURITU));
				
				// 統合取引先コード
				listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
				
				// フェーズ(30:対象先選定 固定)
				listBean.setPhase(GS.PHASE_TAISHOSAKI_SENTEI);
				
				// 査定期
				listBean.setSatei_ki(rs.getString(SATEI_KI));

				// 課題No.50
				// 追加開始
				// 査定期表示用
				listBean.setSatei_ki_hyouji(rs.getString(SATEIKI_HYOJI));
				// 追加完了
				
				// 半期・四半期区分
				listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));

				// システム区分
				listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
				
				// 店コード
				listBean.setMise_cd(rs.getString(MISE_CD));
				
				// 基準日区分
				listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
				
				// 処理回数
				listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));
				
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
	}	

}