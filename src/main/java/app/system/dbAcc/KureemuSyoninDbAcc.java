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
import app.system.form.KureemuSyoninForm;
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
 * OS3104_クレーム債権再設定_承認一覧 DBアクセスクラス <br>
 */
public class KureemuSyoninDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private KureemuSyoninForm form = null;						                          // アクションフォーム
	
	// Resultset用文字列    
	private static final String KBN_VAL						= "kbn_val";
	private static final String KBN_HYOUJI_VAL				= "kbn_hyouji_val";
	private static final String ANKEN_NO						= "anken_no";
	private static final String KIKAN_TORI_CD					= "kikan_tori_cd";
	private static final String BUSINESS_NM					= "business_nm";
	private static final String KINGAKU						= "kingaku";
	private static final String TUUKA_CD						= "tuuka_cd";
	private static final String YM							= "ym";
	private static final String HYOJI_YM						= "hyoji_ym";
	private static final String SATEI_KAISHA_CD				= "satei_kaisha_cd";
	private static final String INIT_BUNRUI2					= "init_bunrui2";
	private static final String INIT_BU_CD					= "init_bu_cd";
	private static final String BU_CD							= "bu_cd";
	private static final String INIT_BUNRUI3					= "init_bunrui3";
	private static final String INIT_BUNRUI2_NM				= "init_bunrui2_nm";
	private static final String INIT_BU_NM					= "init_bu_nm";
	private static final String INIT_BUNRUI3_NM				= "init_bunrui3_nm";
	private static final String TANTO_USER_NM					= "tanto_user_nm";
	private static final String KTK							= "ktk";
	private static final String SASI_TEN_FLG					= "sasi_ten_flg";
	private static final String SATEI_KI						= "satei_ki";
	private static final String SYSTEM_KBN					= "system_kbn";
	private static final String MISE_CD						= "mise_cd";
	private static final String KIJUNBI_KBN					= "kijunbi_kbn";
	private static final String SYORI_KAISU					= "syori_kaisu";
	private static final String DAIKO_USER_NM					= "daiko_user_nm";
	private static final String PHASE							= "phase";
	private static final String STATUS						= "status";
	private static final String BUNSYO_NO						= "bunsyo_no";
	private static final String TOGO_TORI_CD					= "togo_tori_cd";
	private static final String HANKI_SIHANKI_KBN				= "hanki_sihanki_kbn";
	private static final String SIKIBETU_CD					= "sikibetu_cd";
	private static final String SOSHIKI						= "soshiki";
	private static final String SHINCHOKU						= "shinchoku";
	private static final String HOJI_USER_ID					= "hoji_user_id";
	private static final String DAIKO_USER_ID					= "daiko_user_id";

	private static final String SHOW							= "show"; 								// 区分キー（表示件数）
	private static final String DAIKO							= "( 代行 ";
	private static final String DAIKO_EN						= "( proxy ";
	private static final String COMMA							= "*";
    
	// INパラメータ
    private String userId;                                                                             // ユーザＩＤ
	private String workflowSystemkbn;                                                                  // 業務フローパターンシステム区分
	private String comWorkflowSateikaisya_cd;  														// 業務フローパターン査定会社コード
	
    private static final String SP_SS_OS3104_SELECT_ICHIRAN   = "SP_SS_OS3104_SELECT_ICHIRAN";        // 一覧情報取得プロシージャ
    private static final String SP_SS_OS3104_UPDATE_T1400     = "SP_SS_OS3104_UPDATE_T1400";          // T14_査定進捗管理の更新プロシージャ
    
    private static final String BUNRUI2          				= "bunrui2"; 							 // 分類２
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public KureemuSyoninDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (KureemuSyoninForm)appContext.getActionForm();
		
        // ビーンの値を変数に設定
        this.userId = user_bean.getComUserId();
		this.workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
		this.comWorkflowSateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
	}
	
    /**
     * 変数初期化 <br>
     * 
     */
    public void initialize() {
        // INパラメータ
        userId = GS.EMPTY_CHARCTER;
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        comWorkflowSateikaisya_cd = GS.EMPTY_CHARCTER;
    }
    
	/**
	 * 表示件数セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getShow() throws SQLException {


		// ResultSet取得
        ResultSet rs = null;
		try{
            
			rs = getKbnval(SHOW,workflowSystemkbn,cmnData.getComLangMode());
            
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
    			// Resultset close
    			rs.close();
    		}
	    }
	}
    
    /**
     * T14_査定進捗管理の更新 <br>
     * 
     * @throws SQLException
     */
    public void setUpdateT1400() throws SQLException {

		// ResultSet取得
        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS3104_UPDATE_T1400, sqlExec);
            exCstmt.setStringIn(workflowSystemkbn);
            exCstmt.setStringIn(comWorkflowSateikaisya_cd);
            exCstmt.setStringIn(user_bean.getComDaiko_userId());  
            exCstmt.setStringIn(userId);

            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            
        } finally {
            if (rs != null) {
                // Resultset close
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
        // ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS3104_SELECT_ICHIRAN, sqlExec);
        exCstmt.setStringIn(workflowSystemkbn);
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(cmnData.getComLangMode());        
        exCstmt.setResultSet(RESULTSET);
        
        try {
            // SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();   
            InputCheck check = new InputCheck();
            int i = 0;
            while ( rs.next() ) {
                
                TorihikisakiBean listBean = new TorihikisakiBean();
                
                // id
                listBean.setId(Function.getStringOfInt(i));
                
                // 滞留判定案件Ｎｏ．
                listBean.setAnken_no(rs.getString(ANKEN_NO));
                
                // 勘定先コード
                listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
                
                // 勘定先名称
                listBean.setKanjo_nm(rs.getString(BUSINESS_NM));
                
                // 金額計
                if (rs.getString(SYSTEM_KBN).equals(GS.GSS)){
                    
                    // 国内
                    listBean.setKingaku(Function.format("##,###,###,###,###,##0.##", Function.getValueOfDouble(rs.getString(KINGAKU))) + rs.getString(TUUKA_CD));
                }else{
                    
                    // 海外
                    listBean.setKingaku(Function.format("##,###,###,###,###,##0.00", Function.getValueOfDouble(rs.getString(KINGAKU))) + rs.getString(TUUKA_CD));
                }       
                
                // 対象年月表示用
				if ("2".equals(rs.getString(HANKI_SIHANKI_KBN))) {
					listBean.setTaisyo_ym_hyoji(rs.getString(HYOJI_YM) + "(Q)");
				} else {
					listBean.setTaisyo_ym_hyoji(rs.getString(HYOJI_YM));
				}
                
                // 対象年月
                listBean.setTaisyo_ym(rs.getString(YM));
                
                // 査定会社コード
                listBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
                
                // 分類２
                listBean.setBunrui2(rs.getString(BUNRUI2));
                
                // 初期分類２
                listBean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));
                
                // 初期部コード
                listBean.setInit_bu_cd(rs.getString(INIT_BU_CD));
                
                // 部コード
                listBean.setBu_cd(rs.getString(BU_CD));
                
                // 初期分類３
                listBean.setInit_bunrui3(rs.getString(INIT_BUNRUI3));
                
                // 初期分類２名称
                listBean.setInit_bunrui2_nm(rs.getString(INIT_BUNRUI2_NM));
                
                // 初期部名称
                listBean.setInit_bu_nm(rs.getString(INIT_BU_NM));
                         
                // 初期分類３名称
                listBean.setInit_bunrui3_nm(rs.getString(INIT_BUNRUI3_NM));
                
                // 組織
                listBean.setSoshiki(rs.getString(SOSHIKI));
                                         
                // 代行者名
                StringBuffer daiko_nm = new StringBuffer(GS.EMPTY_CHARCTER);
                
                // 代行者名日本語 or 代行者名英語が存在する場合
                if(!check.isNullBlank(rs.getString(DAIKO_USER_NM))){
                    if(cmnData.getComLangMode().equals(GS.LANG_JA)){    
                        daiko_nm.append(DAIKO)
                                .append(rs.getString(DAIKO_USER_NM)); 
                    } else {
                        daiko_nm.append(DAIKO_EN)
                                .append(rs.getString(DAIKO_USER_NM));
                    }
                    
                    daiko_nm.append(GS.SPACE_CHARCTER)
                            .append(GS.KAKKO_MIGI);                 
                }
                
                // 担当者
                if(!check.isNullBlank(rs.getString(TANTO_USER_NM))){
                    listBean.setTanto_nm(rs.getString(TANTO_USER_NM) + daiko_nm);
                }                
                
                // 進捗
                //listBean.setSintyoku(rs.getString(SHINCHOKU));
                
                // 差戻しの場合、* || ' '(半角スペース)  || 進捗
                if(("1").equals(rs.getString(SASI_TEN_FLG))){
                    listBean.setSintyoku(COMMA + GS.SPACE_CHARCTER + rs.getString(SHINCHOKU));
                }
                
                // 信用格付情報
                listBean.setSinyoktk(rs.getString(KTK));
                
                // 添付COUNT
                listBean.setTemp_cnt(rs.getString(BUNSYO_NO));                
                
                // フェーズ
                listBean.setPhase(rs.getString(PHASE));
                
                // ステータス
                listBean.setStatus(rs.getString(STATUS));
                
                // 差戻・転送フラグ
                listBean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));
                
                // 査定期
                listBean.setSatei_ki(rs.getString(SATEI_KI));

                // システム区分
                listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
                
                // 店コード
                listBean.setMise_cd(rs.getString(MISE_CD));
                
                // 基準日区分
                listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
                
                // 処理回数
                listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));

                // 半期・四半期区分
                listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
                
                // 統合取引先コード
                listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
                
                // 識別コード
                listBean.setShikibetu_cd(rs.getString(SIKIBETU_CD));
                
                // 案件保持ユーザ
                listBean.setHoji_user_id(rs.getString(HOJI_USER_ID));
        
                // 代行ユーザID
                listBean.setDaiko_user_id(rs.getString(DAIKO_USER_ID));
                
                // リンク表示フラグ制御
                listBean.setLink_flg(true);

                // 明細承認チッェクボックス
                listBean.setSyonin_chk("0");
                
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
                    // Resultset close
                    rs.close();
                } catch (Exception e) {
                    throw new SQLException(e.getMessage());
                }
            }
        }
    }
}
