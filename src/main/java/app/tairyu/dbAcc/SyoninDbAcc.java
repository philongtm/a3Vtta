/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正
003		2009/12/17		SSC(坂本)		課題No.205 査定先金額判定条件の変更
004		2015/02/23		SSC(前多)		案件No.BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SyoninForm;
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
 * OB1104_実質滞留債権判定_承認一覧 DBアクセスクラス <br>
 */
public class SyoninDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          // 機能共通セッション
	private UserBean user_bean = null;					                              // ユーザ情報
	private SyoninForm form = null;						                          // アクションフォーム
	private TorihikisakiBean meisaiBean;

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
	private static final String CNT 							= "cnt";								 // 件数
	private static final String CHAMPIONBU_CD					= "championbu_cd";			// 現チャンピオン部コード
	private static final String MAX_TAIRYU_BU_CD				= "max_tairyu_bu_cd";		// 滞留債権額が最も大きい部

	private static final String SHOW							= "show"; 								// 区分キー（表示件数）
	private static final String DAIKO							= "( 代行 ";
	private static final String DAIKO_EN						= "( proxy ";
	private static final String COMMA							= "*";

	// INパラメータ
    private String userId;                                                                             // ユーザＩＤ
	private String workflowSystemkbn;                                                                  // 業務フローパターンシステム区分

    private static final String SP_SS_OB1104_SELECT_ICHIRAN   = "SP_SS_OB1104_SELECT_ICHIRAN";        // 滞留判定一覧情報取得プロシージャ
    private static final String SP_SS_OB_SELECT_T0700         = "SP_SS_OB_SELECT_T0700";              // チャンピオン部重複チェックプロシージャ
    private static final String SP_SS_OB_INSERT_T1000         = "SP_SS_OB_INSERT_T1000";              // T10_滞留判定の登録プロシージャ
    private static final String SP_SS_O_UPDATE_T0800          = "SP_SS_O_UPDATE_T0800";               // T08_滞留判定進捗管理の更新プロシージャ
    private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";               // T13_入力履歴の登録プロシージャ
    private static final String SP_SS_OB_SELECT_T0800         = "SP_SS_OB_SELECT_T0800";              // 滞留判定済みチェックプロシージャ
    private static final String SP_SS_OB_SELECT_T0100         = "SP_SS_OB_SELECT_T0100";              // T01_対象先の一次査定対象FLGをチェックプロシージャ
    private static final String SP_SS_OB_SELECT_SATEICHECK    = "SP_SS_OB_SELECT_SATEICHECK";         // 査定データ作成条件チェックプロシージャ
    private static final String SP_SS_OB_SELECT_T0101         = "SP_SS_OB_SELECT_T0101";              // T01_対象先の査定案件No取得プロシージャ
    private static final String SP_SS_OB_INSERT_TAIRYUMEISAI  = "SP_SS_OB_INSERT_TAIRYUMEISAI";       // T16_引当金検討対象BS明細の登録プロシージャ
    private static final String SP_SS_OB_INSERT_MEISAI        = "SP_SS_OB_INSERT_MEISAI";             // T16_引当金検討対象BS明細の登録（留保債務データ）プロシージャ
    private static final String SP_SS_OB_INSERT_SATEISTAT00   = "SP_SS_OB_INSERT_SATEISTAT00";        // T17_引当金判定表示用の登録プロシージャ
    private static final String SP_SS_OB_INSERT_SATEISTAT01   = "SP_SS_OB_INSERT_SATEISTAT01";        // T14_査定進捗管理の登録プロシージャ
    private static final String SP_SS_OB_UPDATE_T0100         = "SP_SS_OB_UPDATE_T0100";              // T01_対象先の更新プロシージャ
    private static final String SP_SS_OB_UPDATE_T0801         = "SP_SS_OB_UPDATE_T0801";              // T08_滞留判定進捗管理の更新プロシージャ
    private static final String SP_SS_OB_INSERT_SATEI         = "SP_SS_OB_INSERT_SATEI";              // T15_一次二次査定の登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400          = "SP_SS_O_INSERT_T0400";               // T04_メール配信の登録を行うプロシージャ
    private static final String SP_SS_OB_SELECT_M0900         = "SP_SS_OB_SELECT_M0900";				 // 査定データ作成条件有無チェックプロシージャ
    private static final String SP_SS_OL_SELECT_P0200		   	= "SP_SS_OL_SELECT_P0200";        		 // システムセレクトボックス設定値取得処理
    private static final String SP_SS_OB_SELECT_CHAMPIONBU            = "SP_SS_OB_SELECT_CHAMPIONBU";	 // 現チャンピオン部取得
    private static final String SP_SS_OB_SELECT_T0900                 = "SP_SS_OB_SELECT_T0900";		 // 滞留債権額が最も大きい部を取得
    private static final String SP_SS_OB_UPDATE_T0700                 = "SP_SS_OB_UPDATE_T0700";		 // T07_チャンピオン部の更新（フラグをはずす）
    private static final String SP_SS_OB_UPDATE_T0700_2               = "SP_SS_OB_UPDATE_T0700_2";		 // T07_チャンピオン部の更新（フラグを立てる）

    private static final String SATEI_ANKEN_NO          		= "satei_anken_no"; 					 // 査定案件No
    private static final String BUNRUI2          				= "bunrui2"; 							 // 分類２
    private static final String HAISINZUMI_FLG 				= "N";									 // 配信済みフラグ
    private static final String ANKEN_NO_EDA          		= "anken_no_eda";						 // 案件No枝番
    private static final String ANKEN_NO_CNT					= "anken_no_cnt";						 // 案件Noコンット
    private static final String ICHIJI_FLG					= "ichiji_flg";						 	 // 一次査定対象フラグ
    private static final String KINGAKUJYOUKEN				= "kingakujyouken";						 // 金額条件
    private static final String KINGAKUKEI					= "kingakukei";						 	 // 金額計
    private static final String TORIMODOSHI_FUKA_FLG_YES		= "1";						 	 		 // 取戻不可
    private static final String BU_CD_CNT						= "bu_cd_cnt";							 // 部コードのカウント件数
    private static final String TAIRYU_HANTEI					= "1";						 	 		 // 滞留判定
    private static final String NYURYOKU_KBN_SYONIN			= "80";						 	 		 // 入力区分(承認)

	/**
	 * コンストラクタ <br>
	 *
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SyoninDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		form = (SyoninForm)appContext.getActionForm();

        // ビーンの値を変数に設定
        this.userId = user_bean.getComUserId();
		this.workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
	}

    /**
     * 変数初期化 <br>
     *
     */
    public void initialize() {
        // INパラメータ
        userId = GS.EMPTY_CHARCTER;
        workflowSystemkbn = GS.EMPTY_CHARCTER;
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
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_P0200, sqlExec);
            exCstmt.setStringIn(SHOW);
            exCstmt.setStringIn(cmnData.getComLangMode());
            exCstmt.setStringIn(workflowSystemkbn);
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

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
     * チャンピオン部重複チェック <br>
     *
     * @return
     * @throws SQLException
     */
    public int getObSelectT0700() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0700, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setIntOut(BU_CD_CNT);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            // チャンピオン部をカウント
            int cntBu_cd = 0;
            cntBu_cd = exCstmt.getInt(BU_CD_CNT);

            return cntBu_cd;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * T10_滞留判定の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertT1000() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_T1000, sqlExec);
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(meisaiBean.getAnken_no());

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
     * T08_滞留判定進捗管理の更新 <br>
     *
     * @param meisaiBean
     * @param next_phase
     * @param next_status
     * @param flg
     * @throws SQLException
     */
    public void setOUpdateT0800() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
            exCstmt.setStringIn(meisaiBean.getAnken_no());
            if ("0".equals(form.getUpd_taityu_kbn())) {
                exCstmt.setStringIn(form.getJi_jishi_phase());
                exCstmt.setStringIn(form.getJi_kaishi_status());
                exCstmt.setStringIn(null);
                exCstmt.setStringIn(null);
            } else {
                exCstmt.setStringIn(meisaiBean.getPhase());
                exCstmt.setStringIn(GS.STATUS_KANRYO);
                //課題No.129
                //修正開始
                //exCstmt.setStringIn(meisaiBean.getHoji_user_id());
                exCstmt.setStringIn(null);
                //修正完了
                exCstmt.setStringIn(null);
            }
            exCstmt.setStringIn(TORIMODOSHI_FUKA_FLG_YES);
            exCstmt.setStringIn(null);
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }

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
     * T13_入力履歴の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOInsertT1300() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
            exCstmt.setStringIn(meisaiBean.getAnken_no());
            exCstmt.setStringIn(TAIRYU_HANTEI);
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setStringIn(user_bean.getComUser_Nm());
            exCstmt.setStringIn(user_bean.getComUser_Nm_En());
            exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
            exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());
            exCstmt.setStringIn(meisaiBean.getPhase());
            exCstmt.setStringIn(NYURYOKU_KBN_SYONIN);
            exCstmt.setStringIn(user_bean.getComUserId());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(null);
                exCstmt.setStringIn(null);
                exCstmt.setStringIn(null);
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
                exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
                exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
            }
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(null);

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
     * 滞留判定済みチェック <br>
     *
     * @param meisaiBean
     * @return
     * @throws SQLException
     */
    public int getOBSelectT0802() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0800, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setIntOut(ANKEN_NO_CNT);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            // 件数
            int cntAnken_no = 0;
            cntAnken_no = exCstmt.getInt(ANKEN_NO_CNT);

            return cntAnken_no;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * T01_対象先の一次査定対象FLGをチェック <br>
     *
     * @param meisaiBean
     * @return
     * @throws SQLException
     */
    public String getOBSelectT0100() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0100, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringOut(ICHIJI_FLG);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            // 一次査定対象フラグ
            String ichiji_flg = GS.EMPTY_CHARCTER;

            ichiji_flg = exCstmt.getString(ICHIJI_FLG);

            return ichiji_flg;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * 査定データ作成条件有無チェック <br>
     *
     * @return int
     * @exception SQLException
     */
    public int selM09() throws SQLException {
        String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();		//課題対応No,205	査定会社コード追加
        String mise_cd = meisaiBean.getMise_cd();        			//課題対応No,205	店コード追加
        int cnt = 0;
    	ExCallableStatement cstmt = null;
        ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_M0900, sqlExec);
        cstmt.setStringIn(system_kbn);
        cstmt.setStringIn(sateikaisya_cd);							//課題対応No,205	店コード追加
        cstmt.setStringIn(mise_cd);									//課題対応No,205	店コード追加
        cstmt.setIntOut(CNT);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            cnt = cstmt.getInt(CNT);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
        return cnt;
    }

    /**
     * 査定データ作成条件チェック <br>
     *
     * @param meisaiBean
     * @return １：査定対象、２：査定対象ではない
     * @throws SQLException
     */
    public int getOBSelectSateicheck() throws SQLException {

        ResultSet rs = null;
        try{
            int satei_flg = 2;
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_SELECT_SATEICHECK, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);

            if (rs.next()) {
                if (rs.getDouble(KINGAKUJYOUKEN) <= rs.getDouble(KINGAKUKEI)) {
                    satei_flg = 1;
                }
            }

            return satei_flg;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * 現チャンピオン部取得 <br>
     *
     * @param meisaiBean
     * @return 現チャンピオン部
     * @exception SQLException
     */

    public String selCh() throws SQLException {
    	String championbu_cd = GS.EMPTY_CHARCTER;
    	String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();
        String kanjo_cd = meisaiBean.getKanjo_cd();
        String mise_cd = meisaiBean.getMise_cd();
        String taisyo_ym = meisaiBean.getTaisyo_ym();
        String satei_ki = meisaiBean.getSatei_ki();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_CHAMPIONBU, sqlExec);
		cstmt.setStringIn(system_kbn);
		cstmt.setStringIn(sateikaisya_cd);
		cstmt.setStringIn(kanjo_cd);
		cstmt.setStringIn(mise_cd);
		cstmt.setStringIn(taisyo_ym);
		cstmt.setStringIn(satei_ki);
        cstmt.setStringOut(CHAMPIONBU_CD);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            championbu_cd = cstmt.getString(CHAMPIONBU_CD);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
        return championbu_cd;
    }

    /**
     * 滞留債権額が最も大きい部を取得 <br>
     *
     * @param meisaiBean
     * @return 滞留債権額が最も大きい部
     * @exception SQLException
     */

    public String selT09() throws SQLException {
    	String max_tairyu_bu_cd = GS.EMPTY_CHARCTER;
    	String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();
        String kanjo_cd = meisaiBean.getKanjo_cd();
        String mise_cd = meisaiBean.getMise_cd();
        String taisyo_ym = meisaiBean.getTaisyo_ym();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0900, sqlExec);
		cstmt.setStringIn(system_kbn);
		cstmt.setStringIn(sateikaisya_cd);
		cstmt.setStringIn(kanjo_cd);
		cstmt.setStringIn(mise_cd);
		cstmt.setStringIn(taisyo_ym);
		cstmt.setStringOut(MAX_TAIRYU_BU_CD);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            max_tairyu_bu_cd = cstmt.getString(MAX_TAIRYU_BU_CD);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
        return max_tairyu_bu_cd;
    }

    /**
     * T07_チャンピオン部の更新（フラグをはずす） <br>
     *
     * @param meisaiBean
     * @param championbu_cd
     * @exception SQLException
     */

    public void updT07(String championbu_cd) throws SQLException {
        String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();
        String kanjo_cd = meisaiBean.getKanjo_cd();
        String mise_cd = meisaiBean.getMise_cd();
        String taisyo_ym = meisaiBean.getTaisyo_ym();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0700, sqlExec);
        cstmt.setStringIn(system_kbn);
        cstmt.setStringIn(sateikaisya_cd);
        cstmt.setStringIn(kanjo_cd);
        cstmt.setStringIn(mise_cd);
        cstmt.setStringIn(taisyo_ym);
        cstmt.setStringIn(championbu_cd);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
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
     * T07_チャンピオン部の更新（フラグをたてる） <br>
     *
     * @param meisaiBean
     * @param max_tairyu_bu_cd
     * @exception SQLException
     */

    public void updT07_2(String max_tairyu_bu_cd) throws SQLException {
        String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();
        String kanjo_cd = meisaiBean.getKanjo_cd();
        String mise_cd = meisaiBean.getMise_cd();
        String taisyo_ym = meisaiBean.getTaisyo_ym();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0700_2, sqlExec);
        cstmt.setStringIn(system_kbn);
        cstmt.setStringIn(sateikaisya_cd);
        cstmt.setStringIn(kanjo_cd);
        cstmt.setStringIn(mise_cd);
        cstmt.setStringIn(taisyo_ym);
        cstmt.setStringIn(max_tairyu_bu_cd);

        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
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
     * 査定案件No取得 <br>
     *
     * @return String
     * @exception SQLException
     */
    public String getSateiAnkenNo() throws SQLException {
    	String satei_anken_no = GS.EMPTY_CHARCTER;
    	String system_kbn = meisaiBean.getSystem_kbn();
        String sateikaisya_cd = meisaiBean.getSateikaisya_cd();
        String taisyo_ym = meisaiBean.getTaisyo_ym();
        String kanjo_cd = meisaiBean.getKanjo_cd();
        String mise_cd = meisaiBean.getMise_cd();
        String hanki_sihanki_kbn = meisaiBean.getHanki_sihanki_kbn();
        String syori_kaisu = meisaiBean.getSyori_kaisu();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0101, sqlExec);
		cstmt.setStringIn(system_kbn);
		cstmt.setStringIn(sateikaisya_cd);
		cstmt.setStringIn(taisyo_ym);
		cstmt.setStringIn(kanjo_cd);
		cstmt.setStringIn(mise_cd);
		cstmt.setStringIn(hanki_sihanki_kbn);
		cstmt.setStringIn(syori_kaisu);
        cstmt.setStringOut(SATEI_ANKEN_NO);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            satei_anken_no = cstmt.getString(SATEI_ANKEN_NO);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
        return satei_anken_no;
    }

    /**
     * T16_引当金検討対象BS明細の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertTairyumeisai(String satei_anken_no) throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_TAIRYUMEISAI, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSatei_ki());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            exCstmt.setStringIn(satei_anken_no);
            exCstmt.setStringOut(ANKEN_NO);
            exCstmt.setIntOut(ANKEN_NO_EDA);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            form.setTairyu_anken_no(exCstmt.getString(ANKEN_NO));
            form.setTairyu_anken_eda(exCstmt.getInt(ANKEN_NO_EDA));

        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * T16_引当金検討対象BS明細の登録（留保債務データ） <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertMeisai() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_MEISAI, sqlExec);
            exCstmt.setStringIn(form.getTairyu_anken_no());
            exCstmt.setStringIn(String.valueOf(form.getTairyu_anken_eda()));
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());

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
     * T17_引当金判定表示用の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertSateistat00() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEISTAT00, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }

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
     * T14_査定進捗管理の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertSateistat01() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEISTAT01, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(meisaiBean.getKijunbi_kbn());
            exCstmt.setStringIn(meisaiBean.getSatei_ki());
            exCstmt.setStringOut(SATEI_ANKEN_NO);
            exCstmt.setStringOut(BUNRUI2);
            exCstmt.setStringOut(BU_CD);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            form.setSatei_anken_no(exCstmt.getString(SATEI_ANKEN_NO));
            form.setBunrui2(exCstmt.getString(BUNRUI2));
            form.setBu_cd(exCstmt.getString(BU_CD));

        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

    /**
     * T01_対象先の更新 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBUpdateT0100() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0100, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(form.getSatei_anken_no());

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
     * T08_滞留判定進捗管理の更新 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBUpdateT0801() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0801, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }

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
     * T15_一次二次査定の登録 <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setOBInsertSatei() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEI, sqlExec);
            exCstmt.setStringIn(meisaiBean.getSystem_kbn());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getMise_cd());
            exCstmt.setStringIn(meisaiBean.getKanjo_cd());
            exCstmt.setStringIn(meisaiBean.getSyori_kaisu());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }
            exCstmt.setStringIn(meisaiBean.getHanki_sihanki_kbn());

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
     * T04_メール配信の登録 <br>
     *
     * @throws SQLException
     */
    public void setOBInsertT0400() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
            exCstmt.setStringIn(user_bean.getComUserId());
            if ((GS.PHASE_TAIRYU_HANTEI_KENSHO).equals(form.getJi_jishi_phase())) {
                exCstmt.setStringIn(meisaiBean.getAnken_no());
            } else {
                exCstmt.setStringIn(form.getSatei_anken_no());
            }
            exCstmt.setStringIn(meisaiBean.getTaisyo_ym());
            exCstmt.setStringIn(meisaiBean.getSateikaisya_cd());
            if ((GS.PHASE_TAIRYU_HANTEI_KENSHO).equals(form.getJi_jishi_phase())) {
                exCstmt.setStringIn(meisaiBean.getBunrui2());
                // システム区分が01の場合、部門コードを設定する
                if(GS.GSS.equals(meisaiBean.getSystem_kbn())){
                    exCstmt.setStringIn(meisaiBean.getBu_cd());
                } else {
                	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
                }
            } else {
                exCstmt.setStringIn(form.getBunrui2());
                // システム区分が01の場合、部門コードを設定する
                if(GS.GSS.equals(meisaiBean.getSystem_kbn())){
                	exCstmt.setStringIn(form.getBu_cd());
                } else {
                	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
                }
            }
            exCstmt.setStringIn(form.getJi_jishi_phase());
            exCstmt.setStringIn(form.getJi_kaishi_status());
            exCstmt.setStringIn(null);
            exCstmt.setStringIn(HAISINZUMI_FLG);
            exCstmt.setStringIn(GS.OB1104);
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }

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
     * T08_滞留判定進捗管理の更新(取戻不可で更新する) <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setInitT0800TorimodoshiFukaFlg() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
            exCstmt.setStringIn(meisaiBean.getAnken_no());
            exCstmt.setStringIn(meisaiBean.getPhase());
            exCstmt.setStringIn(meisaiBean.getStatus());
            exCstmt.setStringIn(meisaiBean.getHoji_user_id());
            exCstmt.setStringIn(meisaiBean.getSasi_ten_flg());
            exCstmt.setStringIn(TORIMODOSHI_FUKA_FLG_YES);
            exCstmt.setStringIn(meisaiBean.getDaiko_user_id());
            if (user_bean.getComDaiko_userId() == null) {
                exCstmt.setStringIn(user_bean.getComUserId());
            } else {
                exCstmt.setStringIn(user_bean.getComDaiko_userId());
            }

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
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB1104_SELECT_ICHIRAN, sqlExec);
        exCstmt.setStringIn(form.getSansyo_phase());
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(workflowSystemkbn);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
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
                listBean.setTaisyo_ym_hyoji(rs.getString(HYOJI_YM));

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
                listBean.setSintyoku(rs.getString(SHINCHOKU));

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

    /**
     * 明細ビンーを設定する <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setMeisaiBean(TorihikisakiBean meisaiBean) throws SQLException {
    	this.meisaiBean = meisaiBean;
    }
}
