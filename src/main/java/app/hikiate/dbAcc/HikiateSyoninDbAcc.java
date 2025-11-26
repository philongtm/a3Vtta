/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/25		SSC				課題No.163 代行者表示修正 
003		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正 
******************************************************************************/
package app.hikiate.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.form.HikiateSyoninForm;
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
 *  OD1103 引当金確認_承認一覧 DBアクセスクラス<br>
 */
public class HikiateSyoninDbAcc extends CommonDbAcc {
    private SessionData cmnData = null;             // 機能共通セッション
    private UserBean user_bean = null;                  // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    private HikiateSyoninForm form = null;                    // アクションフォーム
    private AppContext appContext = null;               // ＡＰＰコンテキスト

    // 課題No.163
    // 追加開始
	private static final String DAIKO					= "( 代行 ";
	private static final String DAIKO_EN				= "( proxy ";
    // 追加完了
	
    //Resultset用文字列  
    private static final String KBN_HYOUJI_VAL           = "kbn_hyouji_val";
    private static final String KBN_VAL                  = "kbn_val";

    private static final String MISE_CD                 = "mise_cd";					// 店コード
    private static final String ANKEN_NO                 = "anken_no";				// 査定案件No.
    private static final String KIKAN_TORI_CD            = "kikan_tori_cd";			// 基幹取引先コード
    private static final String SATEI_KAISHA_CD          = "satei_kaisha_cd";			// 査定会社コード
    private static final String INIT_BUNRUI2             = "init_bunrui2";			// 初期分類２
    private static final String INIT_BUNRUI3             = "init_bunrui3";			// 初期分類３
    private static final String INIT_BU_CD               = "init_bu_cd";				// 初期部コード
    private static final String PHASE                    = "phase";					// フェーズ
    private static final String STATUS                   = "status";					// ステータス
    private static final String YM                       = "ym";						// 年月
    private static final String SASI_TEN_FLG             = "sasi_ten_flg";			// 差戻転送FLG
    private static final String HOJI_USER_ID             = "hoji_user_id";			// 案件保持ユーザID
    private static final String SYSTEM_KBN               = "system_kbn";				// 基幹システム区分
    private static final String KIJUNBI_KBN              = "kijunbi_kbn";			    // 基準日区分
    private static final String SYORI_KAISU              = "syori_kaisu";				// 処理回数
    private static final String BUSINESS_NM              = "business_nm";				// 親会社名称
    private static final String TORI_NM                  = "tori_nm";					// 取引先名
    private static final String ADR                      = "adr";						// 所在地
    private static final String WB_COUNTRY_NM            = "wb_country_nm";			// ワールドベース国名称
    private static final String GAIBU_KTK                = "gaibu_ktk";				//  外部格付
    private static final String KTK_KIKAN                = "ktk_kikan";				// 格付機関
    private static final String FSS                      = "fss";						// FSS
    private static final String DUNS_RATING              = "duns_rating";				// DUNS Rating   
    private static final String KTK                      = "ktk";						// 信用格付情報
    private static final String OYA_KTK                  = "oya_ktk";					// 親会社信用格付情報
    private static final String OYA_ITTAI_DOKURITU       = "oya_ittai_dokuritu";		// 親会社一体独立
    private static final String OYA_DUNS_NO              = "oya_duns_no";				// 親会社取引先コード
    private static final String TANTO_USER_NM            = "tanto_user_nm";			// 担当者名
    private static final String DAIKO_USER_NM            = "daiko_user_nm";			// 代行ユーザ名
    private static final String INIT_BUNRUI2_NM          = "init_bunrui2_nm";			// 初期分類２名称
    private static final String INIT_BUNRUI3_NM          = "init_bunrui3_nm";			// 初期分類３名称
    private static final String ORGA                     = "orga";					// 組織
    private static final String INIT_BU_NM               = "init_bu_nm";				// 初期部名称
    private static final String KINGAKU                  = "kingaku";					// 金額
    private static final String TUUKA_CD                 = "tuuka_cd";				// 通貨コード
    private static final String TORIHIKISAKI_KBN_NM      = "torihikisaki_kbn_nm";		// 取引先区分名称
    private static final String SAIKEN_KBN_NM            = "saiken_kbn_nm";			// 債権区分名称
    private static final String HIKAKU_TORIHIKISAKI_KBN  = "hikaku_torihikisaki_kbn";	// 取引先区分比較用
    private static final String HIKAKU_SAIKEN_KBN        = "hikaku_saiken_kbn";		// 債権区分比較用
    private static final String HYOUZIYOU_YM             = "hyouziyou_ym";			// 表示用年月
    private static final String KAISHA_CD                = "kaisha_cd";				// 会社コード
    private static final String BU_CD                    = "bu_cd"; 					// 部コード
    private static final String DAIKO_USER_ID            = "daiko_user_id";			// 代行ユーザID
    private static final String HANKI_SIHANKI_KBN        = "hanki_sihanki_kbn";	    // 半期四半期区分
    private static final String BUNRUI2                  = "bunrui2";					// 分類2
    private static final String SATEI_KI                 = "satei_ki";				// 査定期
    private static final String TORIHIKISAKI_KBN         = "torihikisaki_kbn";		// 取引先区分
    private static final String SAIKEN_KBN               = "saiken_kbn";				// 債権区分

    private static final String SHOW                     = "show";           			//区分キー（表示件数）
    private static final String PHASE_80                 = "80";          			// フェーズ:引当金検証
    private static final String PHASE_70                 = "70";          			// フェーズ:引当金確認
    private static final String HANKI_SIHANKI_KBN_2      = "2";						// 半期四半期区分:第1/3四半期：2
    private static final String MODOSHI_FLG_1            = "1";						// 取戻不可フラグ:取戻不可
    private static final String STATUS_40                = "40";						// ステータス:完了
    private static final String HANTEI_KBN_2             = "2";						// 判定査定区分
    private static final String OPE_KBN_80               = "80";						// 入力区分:承認
    
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0";      	   // 数字のフォーマット：国内
    
    private static final String SP_SS_OD1103_SELECT_ICHIRAN 			= "SP_SS_OD1103_SELECT_ICHIRAN";		// 承認対象の勘定先の一覧を取得する
    private static final String SP_SS_O_UPDATE_T1400					= "SP_SS_O_UPDATE_T1400";				// 案件の進捗を更新する
    private static final String SP_SS_O_INSERT_T1300					= "SP_SS_O_INSERT_T1300";               // T13_入力履歴（SST_NYURYOKU_HIST）の登録プロシージャ
    private static final String SP_SS_O_UPDATE_T1403					= "SP_SS_O_UPDATE_T1403";     			// T14_査定進捗管理（SST_SATEI_STAT）の更新             
    
    // INパラメータ
    private String userId;              	// ユーザＩＤ
	private String sansyoBunrui2;			// 参照分類２コード
    private String workflowSystemkbn;   	// 業務フローパターンシステム区分
    private String workflowsateikaisya_cd;	// 業務フローパターン査定会社コード
    private String langMode;				// 共)言語モード

    /**
     * コンストラクタ
     * 
     * @param SqlExecuter
     * @param Log
     * @param AppContext
     */
    public HikiateSyoninDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        form = (HikiateSyoninForm)appContext.getActionForm();

        //ビーンの値を変数に設定
        userId = user_bean.getComUserId();
        sansyoBunrui2 = user_bean.getComSansyoBunrui2();
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        workflowsateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
        langMode = cmnData.getComLangMode();
    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        userId = GS.EMPTY_CHARCTER;
        sansyoBunrui2 = GS.EMPTY_CHARCTER;
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        langMode = GS.EMPTY_CHARCTER;
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
    public void getMeisai() throws SQLException {

    	String phase = PHASE_80;
    	if(user_bean.getComHikiatekin_kensyo_s_flg().equals(GS.ON)){
    		phase = PHASE_70;
    	}
        // 課題No.163
        // 追加開始
    	String daiko = GS.EMPTY_CHARCTER;
    	if(GS.LANG_JA.equals(cmnData.getComLangMode())){
        	daiko = DAIKO;
    	}else{
        	daiko = DAIKO_EN;
    	}
        // 追加完了
    	
        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1103_SELECT_ICHIRAN, sqlExec);
        // 言語モード
        exCstmt.setStringIn(langMode);
        // 査定会社コード
        exCstmt.setStringIn(workflowsateikaisya_cd);
        // フェーズ
        exCstmt.setStringIn(phase);
        // 分類2
        exCstmt.setStringIn(sansyoBunrui2);
        // ユーザID
        exCstmt.setStringIn(userId);
        // 業務フローパターンシステム区分
        exCstmt.setStringIn(workflowSystemkbn);
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();   // 明細配列
            InputCheck check = new InputCheck();
            int i = 0;
            while ( rs.next() ) {
                boolean link_flg = false;
                TorihikisakiBean listBean = new TorihikisakiBean();               
                //id
                listBean.setId(Function.getStringOfInt(i));               
                // 査定案件Nｏ．
                listBean.setAnken_no(rs.getString(ANKEN_NO));
                // 店コード
                listBean.setMise_cd(rs.getString(MISE_CD));
                // 勘定先コード
                listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
                // 勘定先名称
                listBean.setKanjo_nm(rs.getString(TORI_NM));
                // 所在国
                listBean.setSyozaikoku(rs.getString(WB_COUNTRY_NM));
                // 所在地
                listBean.setSyozaichi(rs.getString(ADR));
                // 金額計
                listBean.setKingaku(formatKingaku(rs.getString(KINGAKU),workflowSystemkbn));
                // 対象年月
                listBean.setTaisyo_ym(rs.getString(YM));
                // 査定会社コード
                listBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
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
                listBean.setSoshiki(rs.getString(ORGA));
                // システム区分
                listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
                // 案件保持ユーザID
                listBean.setHoji_user_id(rs.getString(HOJI_USER_ID));
                // 担当者名
                listBean.setTanto_nm(rs.getString(TANTO_USER_NM));
                // 基準日区分
                listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
                // 処理回数
                listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));
                // 外部格付
                listBean.setGaibu_ktk(rs.getString(GAIBU_KTK));
                // 格付機関
                listBean.setKtk_kikan(rs.getString(KTK_KIKAN));
                // FSS
                listBean.setFss(rs.getString(FSS));
                // DUNS Rating
                listBean.setDuns_rating(rs.getString(DUNS_RATING));
                // 信用格付情報
                listBean.setSinyoktk(rs.getString(KTK));
                // 親会社信用格付情報
                listBean.setOya_ktk(rs.getString(OYA_KTK));
                // 親会社一体独立
                listBean.setOya_ittai_dokuritu(rs.getString(OYA_ITTAI_DOKURITU));
                // フェーズ
                listBean.setPhase(rs.getString(PHASE));
                // ステータス
                listBean.setStatus(rs.getString(STATUS));
                // 差戻・転送フラグ
                listBean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));
                // リンク表示フラグ制御
                if((check.isNullBlank(rs.getString(HOJI_USER_ID)))){
                    link_flg = true;
                } 
                if(!(check.isNullBlank(rs.getString(HOJI_USER_ID)) && rs.getString(HOJI_USER_ID).trim().equals(userId))){
                    link_flg = true;
                }
                listBean.setLink_flg(link_flg);
                // 表示用年月
                listBean.setTaisyo_ym_hyoji(rs.getString(HYOUZIYOU_YM));
                
                if(Function.trim(rs.getString(TORIHIKISAKI_KBN)).equals(Function.trim(rs.getString(HIKAKU_TORIHIKISAKI_KBN))) && 
                Function.trim(rs.getString(SAIKEN_KBN)).equals(Function.trim(rs.getString(HIKAKU_SAIKEN_KBN)))){
                	// 差異FLG
                    listBean.setDiffer_flg(true);
                }else{
                	listBean.setDiffer_flg(false);
                }
                if(GS.GSS.equals(listBean.getSystem_kbn())){
                	listBean.setDiffer_flg(true);
                }
                
                // 取引先区分名称
                listBean.setTori_kbn_nm(rs.getString(TORIHIKISAKI_KBN_NM));
                // 債権区分名称
                listBean.setSai_kbn_nm(rs.getString(SAIKEN_KBN_NM));
                
                // 親会社取引先コード
                listBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
                // 通貨コード
                listBean.setTuuka_cd(rs.getString(TUUKA_CD));
                // 会社コード
                listBean.setKaisya_cd(rs.getString(KAISHA_CD));
                // 半期四半期区分
                listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
                if(HANKI_SIHANKI_KBN_2.equals(rs.getString(HANKI_SIHANKI_KBN))){
                    // 表示用年月
                    listBean.setTaisyo_ym_hyoji(Function.addQuarter(rs.getString(HYOUZIYOU_YM)));
                }
                
                // 分類２
                listBean.setBunrui2(rs.getString(BUNRUI2));
                // 代行ユーザID
                listBean.setDaiko_user_id(rs.getString(DAIKO_USER_ID));

                if(rs.getString(DAIKO_USER_NM) != null && rs.getString(DAIKO_USER_NM).trim().length() > 0 ){
                	// 代行者名がNULLで無い場合、担当者名 || '(' || 代行者名 || ')'
                    // 課題No.163
                    // 修正開始
                	//listBean.setTanto_nm(rs.getString(TANTO_USER_NM)+"("+(rs.getString(DAIKO_USER_NM)).trim()+")");
                	listBean.setTanto_nm(rs.getString(TANTO_USER_NM)+daiko+(rs.getString(DAIKO_USER_NM)).trim()+GS.KAKKO_MIGI);
                    // 修正完了
                }
                
                // 取引先区分
                listBean.setTori_kbn(rs.getString(TORIHIKISAKI_KBN));
                // 債権区分
                listBean.setSai_kbn(rs.getString(SAIKEN_KBN));
                // 取引先区分比較用
                listBean.setTori_kbn_cp(rs.getString(HIKAKU_TORIHIKISAKI_KBN));
                // 債権区分比較用
                listBean.setSai_kbn_cp(rs.getString(HIKAKU_SAIKEN_KBN));
                // 査定期
                listBean.setSatei_ki(rs.getString(SATEI_KI));
                // 親会社名称
                listBean.setOya_business_nm(rs.getString(BUSINESS_NM));
                //承認チェックボックス
                listBean.setSyonin_chk(GS.EMPTY_CHARCTER);
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
     * 
     *  案件の進捗を更新する<br>
     * 
     * @param status
     * @param hojiUserId
     * @param modoshiFlg
     * @throws SQLException
     */
    public void uptT14(String phase) throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());			// 査定案件No
		exCstmt.setStringIn(phase);								// フェーズ
		exCstmt.setStringIn(STATUS_40);							// ステータス
		//課題No.129
		//修正開始
		//exCstmt.setStringIn(user_bean.getComUserId());		// 案件保持ユーザID
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);					// 案件保持ユーザID
		//修正完了
        exCstmt.setStringIn(null); 								// 査定登録画面
		exCstmt.setStringIn(null);								// 転送フラグ
		exCstmt.setStringIn(null);								// 代行ユーザID
		if(tori_bean.getDaiko_user_id() == null || tori_bean.getDaiko_user_id().length() == 0){
			exCstmt.setStringIn(user_bean.getComUserId());			// 更新ユーザID = ユーザID
		}else{
			exCstmt.setStringIn(tori_bean.getDaiko_user_id());			// 更新ユーザID = 代行ユーザID
		}
		
		exCstmt.setStringIn(MODOSHI_FLG_1);
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
     * 
     *  入力履歴の登録を行う<br>
     * 
     * @throws SQLException
     */
    public void intT13() throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());					// 査定案件No
		exCstmt.setStringIn(HANTEI_KBN_2);								// 判定査定区分
		exCstmt.setStringIn(tori_bean.getSateikaisya_cd());				// 査定会社コード
		exCstmt.setStringIn(user_bean.getComUserId());					// ユーザID
		exCstmt.setStringIn(user_bean.getComUser_Nm());					// ユーザ名日本語
		exCstmt.setStringIn(user_bean.getComUser_Nm_En());				// ユーザ名英語
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());		// 所属部署名日本語
		exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());	// 所属部署名英語
		exCstmt.setStringIn(tori_bean.getPhase());						// フェーズ
		exCstmt.setStringIn(OPE_KBN_80);								// 入力区分
		exCstmt.setStringIn(user_bean.getComUserId());					// 登録ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());			// 代行ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm());			// 代行者名日本語
		exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());		// 代行者名英語
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 登録箇所
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// コメント内容
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元分類２
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 転送元部
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);							// 承認ユーザID
		
		try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
     * 
     * T14_査定進捗管理（SST_SATEI_STAT）の更新 <br>
     * 
     * @throws SQLException
     */
    public void uptT14() throws SQLException { 
    	
    	String phase = PHASE_80;
    	if(user_bean.getComHikiatekin_kensyo_s_flg().equals(GS.ON)){
    		phase = PHASE_70;
    	}
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1403, sqlExec);
		exCstmt.setStringIn(user_bean.getComUserId());			// ユーザ情報.ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());	// 代行ユーザID
		exCstmt.setStringIn(sansyoBunrui2);						// 参照分類２コード
		exCstmt.setStringIn(phase);							// フェーズ
		exCstmt.setStringIn(workflowsateikaisya_cd);			// 業務フローパターン査定会社コード
		
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
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
     * システム区分により、金額をフォーマットする。 <br>
     * 
     * @param kingaku 金額
     * @param systemKbn システム区分
     * @return フォーマットされた金額
     */
    public String formatKingaku(String kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if(kingaku !=null && kingaku.length() > 0){
        	if (systemKbn.equals(GS.GSS)){
                //国内
            	formatKingaku = Function.format(NUM_FMT_KAIGAI, Double.parseDouble(kingaku));
            }else{
                //海外
            	formatKingaku = Function.format(NUM_FMT_KOKUNAI, Double.parseDouble(kingaku));
            }
        }
        
        return formatKingaku;
    }
    
}
