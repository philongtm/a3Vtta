/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/26		SSC				課題No.50 査定期表示対応 
003		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正 
******************************************************************************/
package app.tairyu.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.KarikijuntuikaForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.global.GS;
import common.util.Function;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
* OB2103_対象先選定_追加対象先選択 DBアクセスクラス
*/
public class KarikijuntuikaDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;				// 機能共通セッション
	private KarikijuntuikaForm form = null;			// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト
	private UserBean user_bean;						// ユーザ情報

	//Resultset用文字列	
	private static final String TAISHO_YM            = "taisho_ym";	
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
	private static final String HANKI_SIHANKI_KBN    = "hanki_sihanki_kbn";
	private static final String SYSTEM_KBN           = "system_kbn";
	private static final String MISE_CD              = "mise_cd";
	private static final String KIJUNBI_KBN          = "kijunbi_kbn";
	private static final String SYORI_KAISU          = "syori_kaisu";
	// 課題No.50
	// 追加開始
	private static final String SATEIKI_HYOJI			= "satei_ki_hyoji";
	// 追加完了
	private static final String DELETE_ID0           = "0";			//査定関連テーブル削除時の処理ID
    private static final String HS_KBN_HANTEI        = "2";			//査定
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KBN_VAL              = "kbn_val";
	private static final String SHOW                 = "show";			//区分キー（表示件数）
	private static final String KIJUNBI_KBN_9        = "9";				//区分キー（表示件数）

    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";   // 金額のフォーマット：海外
	
	private static final String SP_SS_OB2105_SELECT_ICHIRAN   = "SP_SS_OB2105_SELECT_ICHIRAN";	//仮基準査定選択一覧情報取得プロシージャ
	private static final String SP_SS_O_SELECT_M2200          = "SP_SS_O_SELECT_M2200";			//対象年月取得処理プロシージャ
	private static final String SP_SS_O_DELETE_T1500          = "SP_SS_O_DELETE_T1500";			//T15_一次二次査定削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1600          = "SP_SS_O_DELETE_T1600";			//T16_引当金検討対象BS明細削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1700          = "SP_SS_O_DELETE_T1700";			//T17_引当金判定表示用削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1200          = "SP_SS_O_DELETE_T1200";			//T12_コメント削除プロシージャ
	private static final String SP_SS_OB2102_UPDATE_T1400     = "SP_SS_OB2102_UPDATE_T1400";		//T14_査定進捗更新用プロシージャ
    private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";       	//入力履歴登録プロシージャ

    /**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public KarikijuntuikaDbAcc(AppContext appcontext) {
		super(appcontext.getSqlExecuter(),appcontext.getLog());
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		form = (KarikijuntuikaForm)appContext.getActionForm();
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
			rs = getKbnval(SHOW,user_bean.getComWorkflowSystemkbn(),cmnData.getComLangMode());

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
	public String getTaisho_ym() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(form.getSystemKbn());
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	StringBuffer sbTaisho_ym = new StringBuffer();
	    	int i = 0;

	    	while ( rs.next() ) {
	    		if(KIJUNBI_KBN_9.equals(rs.getString(KIJUNBI_KBN))){
	    			continue;
	    		}
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
	    	
	    	return sbTaisho_ym.toString();	
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
	public void getMeisai(String taisyo_ym) throws SQLException {

		ResultSet rs = null;
        String formatType = NUM_FMT_KAIGAI;

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2105_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(form.getSateikaisya_cd());
		exCstmt.setStringIn(form.getSystemKbn());
		exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setStringIn(form.getSateiki());
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setStringIn(form.getKanjo_cd());
		exCstmt.setStringIn(cmnData.getComLangMode());
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
				if(!GS.EMPTY_CHARCTER.equals(Function.trim(rs.getString(SAIKEN_KINGAKU)))){
					listBean.setSaiken_kingaku(Function.format(formatType,rs.getDouble(SAIKEN_KINGAKU))+ rs.getString(TUUKA_CD));
				}
				
				// 対象年月
				listBean.setTaisyo_ym(rs.getString(YM));

				// 対象年月(画面表示用)
				listBean.setTaisyo_ym_hyoji(rs.getString(YM_HYOJI));

				// 査定会社コード
				listBean.setSateikaisya_cd(rs.getString(SATEIKAISYA_CD));
				
				// 分類２(海外のみ設定)
				listBean.setBunrui2(rs.getString(MISE_CD));
				
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

    /**
     * T15_一次二次査定の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT15() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1500, sqlExec);
		exCstmt.setStringIn(form.getAnkenNo());
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(DELETE_ID0);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T16_引当金検討対象BS明細の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT16() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1600, sqlExec);
		exCstmt.setStringIn(form.getAnkenNo());
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T17_引当金判定表示用の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT17() throws SQLException {
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1700, sqlExec);
		exCstmt.setStringIn(form.getAnkenNo());
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
	 * T12_コメントの削除 <br>
	 * 
	 * @exception SQLException
	 */
	public void delT12() throws SQLException {

		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1200, sqlExec);
		exCstmt.setStringIn(form.getAnkenNo());			
        exCstmt.setStringIn(form.getPhase());
        exCstmt.setStringIn(GS.COMMENT_VAL_97);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T13_入力履歴の登録 <br>
     * 
     * @throws SQLException
     */
    public void insT13(String nyuryokuKbn) throws SQLException {

    	//ストアドプロシージャ生成
    	ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300,sqlExec);
        exCstmt.setStringIn(form.getAnkenNo());
        exCstmt.setStringIn(HS_KBN_HANTEI);
        exCstmt.setStringIn(form.getSateikaisya_cd());            
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComUser_Nm());
        exCstmt.setStringIn(user_bean.getComUser_Nm_En());            
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());            
        exCstmt.setStringIn(form.getPhase());
        exCstmt.setStringIn(nyuryokuKbn);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
        exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);

        //SQL実行
        exCstmt.execute();
        isError(exCstmt);
    }

	/**
     * T14_査定進捗管理の更新<br>
     * 
     * @exception SQLException
     */
    public void updT14(String stat,String sasiTenFlg,String taisyogaiFlg) throws SQLException {

    	//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(form.getAnkenNo());
		exCstmt.setStringIn(form.getPhase());
		exCstmt.setStringIn(stat);
	    //課題No.129
	    //修正開始
		//exCstmt.setStringIn(user_bean.getComUserId());
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
	    //修正完了
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		exCstmt.setStringIn(sasiTenFlg);
		exCstmt.setStringIn(taisyogaiFlg);
		exCstmt.setStringIn(user_bean.getComDaiko_userId());
		exCstmt.setStringIn(user_bean.getComUserId());
		exCstmt.setStringIn(GS.ON);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}	
		}
    }   
}