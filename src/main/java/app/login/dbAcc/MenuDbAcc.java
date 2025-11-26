/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/10/20		SSC				課題No.41 引当金検証案件カウント 
003		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応による修正
										(共通のメソッドとしたため「insertDateSlash」を削除)
004		2009/12/4		SSC				課題No.189 代行画面切替プルダウン変更時、被代行者のユーザ情報の設定値を修正										
005		2009/12/17		SSC				課題No.207 代行者リスト設定処理修正
006		2016/02/26		SSC				BJ201602002_部門廃止対応（一次）										
007		2016/03/02		SSC				BJ201602002_部門廃止対応（一次）_本部絞込対応
******************************************************************************/

package app.login.dbAcc;

import app.SessionData;
import app.UserBean;
import app.login.form.MenuForm;
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
import java.util.Map;

/**
 *  OS2101_メインメニュー DBアクセスクラス<br>
 */
public class MenuDbAcc extends CommonDbAcc {
	private UserBean user_bean = null;              	// ユーザ情報
    private MenuForm form = null;                 		// アクションフォーム

    //Resultset用文字列  
	private static final String EMAIL_ADDR					= "email_addr";
	private static final String TAIRYU_HANTEI_T_FLG 			= "tairyu_hantei_t_flg";						// 実質滞留判定登録フラグ
	private static final String TAIRYU_HANTEI_S_FLG 			= "tairyu_hantei_s_flg";						// 実質滞留判定承認フラグ
	private static final String TAIRYU_KENSHO_T_FLG 			= "tairyu_kensho_t_flg";						// 実質滞留検証登録フラグ
	private static final String TAIRYU_KENSHO_S_FLG 			= "tairyu_kensho_s_flg";						// 実質滞留検証承認フラグ
	private static final String TAISHOSAKI_SENTEI_T_FLG 		= "taishosaki_sentei_t_flg";					// 対象先選定登録フラグ
	private static final String TAISHOSAKI_SENTEI_S_FLG 		= "taishosaki_sentei_s_flg";					// 対象先選定承認フラグ
	private static final String ICHIJI_SATEI_TOROKU_T_FLG 	= "ichiji_satei_toroku_t_flg";					// 一次査定登録フラグ
	private static final String ICHIJI_SATEI_TOROKU_S_FLG 	= "ichiji_satei_toroku_s_flg";					// 一次査定承認フラグ
	private static final String ICHIJI_SATEI_KENSHO_T_FLG 	= "ichiji_satei_kensho_t_flg";					// 一次査定検証登録フラグ
	private static final String ICHIJI_SATEI_KENSHO_S_FLG 	= "ichiji_satei_kensho_s_flg";					// 一次査定検証承認フラグ
	private static final String NIJI_SATEI_T_FLG 				= "niji_satei_t_flg";							// 二次査定登録フラグ
	private static final String NIJI_SATEI_S_FLG 				= "niji_satei_s_flg";							// 二次査定承認フラグ
	private static final String HIKIATEKIN_KENSYO_T_FLG 		= "hikiatekin_kensyo_t_flg";					// 引当金検証登録フラグ
	private static final String HIKIATEKIN_KENSYO_S_FLG 		= "hikiatekin_kensyo_s_flg";					// 引当金検証承認フラグ
	private static final String HIKIATEKIN_KAKUNIN_T_FLG 		= "hikiatekin_kakunin_t_flg";					// 引当金確認登録フラグ
	private static final String HIKIATEKIN_KAKUNIN_S_FLG 		= "hikiatekin_kakunin_s_flg";					// 引当金確認承認フラグ
	private static final String CLAIM_RESET_T_FLG 			= "claim_reset_t_flg";							// クレーム債権再設定登録フラグ
	private static final String CLAIM_RESET_S_FLG 			= "claim_reset_s_flg";							// クレーム債権再設定承認フラグ
	private static final String DAIKOSET_FLG 					= "daikoset_flg";								// 代行設定フラグ
	private static final String SATEIKAISYA_FLG 				= "sateikaisya_flg";							// 査定会社メンテナンスフラグフラグ
	private static final String WORKFLOW_PATTERN_FLG 			= "workflow_pattern_flg";						// 業務フローパターンメンテナンスフラグ
	private static final String USER_MASTER_FLG 				= "user_master_flg";							// ユーザマスタメンテナンスフラグ
	private static final String KANJO_MASTER_FLG 				= "kanjo_master_flg";							// 勘定科目マスタメンテナンスフラグ
	private static final String JOKEN_MASTER_HQ_FLG 			= "joken_master_hq_flg";						// 抽出条件マスタメンテナンス（本社）フラグ
	private static final String JOKEN_MASTER_FLG 				= "joken_master_flg";							// 抽出条件マスタメンテナンスフラグ
	private static final String CHAMPION_BU_FLG 				= "champion_bu_flg";							// チャンピオン部メンテナンスフラグ
	private static final String GOLF_KAIINKEN_FLG 			= "golf_kaiinken_flg";							// ゴルフ会員権メンテナンスフラグ
	private static final String RENKETSU_UPLOAD_FLG 			= "renketsu_upload_flg";						// 連結区分マスタUPLOADフラグ
	private static final String JIMUKYOKU_SASHI_FLG 			= "jimukyoku_sashi_flg";						// 事務局経由差戻フラグ
	private static final String SATEI_KANRYO_SASHI_FLG 		= "satei_kanryo_sashi_flg";						// 査定完了後差戻フラグ
	private static final String NIJI_SATEI_KBN 				= "niji_satei_kbn";								// ２次査定区分
	private static final String BUNRUI2 						= "bunrui2";									// 分類２
	private static final String BUNRUI2_2 					= "bunrui2";									// 分類２
    private static final String PATTERN_ID 					= "pattern_id";            						// パターンID
    private static final String PATTERN_NAME 					= "pattern_name";            					// パターン名称
    private static final String SATEI_KI 						= "satei_ki";            						// 査定期
    private static final String HYOJI_SATEI_KI 				= "hyoji_satei_ki";            					// 表示用査定期
    private static final String TAISHO_YM_HYOJI				= "taisho_ym_hyoji";            				// 抽出処理日
    private static final String TAISHO_YM			 			= "taisho_ym";            						// 対象年月
    private static final String KIJUNBI_KBN 					= "kijunbi_kbn";            					// 基準日区分
    private static final String HIDAIKO_ID 					= "hidaiko_id";            						// 被代行者ID
    private static final String HIDAIKO_NM 					= "hidaiko_nm";            						// 被代行者名
    
    private static final String SATEIKAISYA_CD 				= "sateikaisya_cd";            					// 査定会社コード
    private static final String SATEI_KAISHA_CD 				= "satei_kaisha_cd";            				// 査定会社コード
    private static final String JUSHIN_BI						= "jushin_bi";            						// 受信日
    private static final String JUSHIN_BI_HYOJI				= "jushin_bi_hyoji";            				// 受信日表示用
    private static final String NIJU_JUSHIN_FLG				= "niju_jushin_flg";            				// 2重受信フラグ
    private static final String BUNRUI2_NM 					= "bunrui2_nm";            						// 分類２名称
    private static final String BU_NM 						= "bu_nm";            							// 部名称
    private static final String BU_CD 						= "bu_cd";            							// 部コード
    private static final String BUMON_CD 						= "bumon_cd";            						// 部門コード
    private static final String SOSHIKI 						= "soshiki";            						// 組織
    private static final String TAIRYUU_MI_SHORI				= "tairyuu_mi_shori";            				// 滞留判定未処理件数
    private static final String TE 							= "te";            								// 滞留判定未処理件数
    private static final String TE_NULL 						= "te_null";            						// 滞留判定未処理件数（部コードNULL）
    private static final String TAIRYUU_SHORI					= "tairyuu_shori";            					// 滞留判定処理中件数
    private static final String TK 							= "tk";            								// 滞留判定処理中件数
    private static final String TK_NULL 						= "tk_null";            						// 滞留判定処理中件数（部コードNULL）
    private static final String TAIRYUU_ZUMI					= "tairyuu_zumi";            					// 滞留判定済み件数
    private static final String TK2 							= "tk2";            							// 滞留判定済み件数
    private static final String TK2_NULL 						= "tk2_null";            						// 滞留判定済み件数（部コードNULL）
    private static final String SATEI_MI_SHORI 				= "satei_mi_shori";            					// 査定未処理件数
    private static final String IE1 							= "ie1";            							// 査定未処理件数
    private static final String IE1_NULL 						= "ie1_null";            						// 査定未処理件数（部コードNULL）
    private static final String SATEI_ICHI					= "satei_ichi";            						// 一次査定中件数
    private static final String IK2 							= "ik2";            							// 一次査定中件数
    private static final String IK2_NULL 						= "ik2_null";            						// 一次査定中件数（部コードNULL）
    private static final String SATEI_NI						= "satei_ni";            						// 二次査定中件数
    private static final String NI1 							= "ni1";            							// 二次査定中件数
    private static final String NI1_NULL 						= "ni1_null";            						// 二次査定中件数（部コードNULL）
    private static final String SATEI_ZUMI					= "satei_zumi";            						// 査定済み件数
    private static final String NI2 							= "ni2";            							// 査定済み件数
    private static final String NI2_NULL 						= "ni2_null";            						// 査定済み件数（部コードNULL）
    private static final String NI3 							= "ni3";            							// 引当金検証未処理件数
    private static final String NI3_NULL 						= "ni3_null";            						// 引当金検証未処理件数（部コードNULL）
    private static final String NI4 							= "ni4";            							// 引当金検証済み件数
    private static final String NI4_NULL 						= "ni4_null";            						// 引当金検証済み件数（部コードNULL）
    private static final String NI15 							= "ni15";            							// 引当金確認未処理件数
    private static final String NI16 							= "ni16";            							// 引当金確認済み件数
    private static final String HANYOU_MI_SYORI 				= "hanyou_mi_syori";       						// 引当金確認未処理件数
    private static final String HANYOU_ZUMI 					= "hanyou_zumi";            					// 引当金確認済み件数
    private static final String SHUKAI_KENSU 					= "shukai_kensu";            					// 査定結果件数（初回）
    private static final String SHUKAI_KENSU_NULL 			= "shukai_kensu_null";            				// 査定結果件数（初回,部コードNULL）
    private static final String TYUKAN_KENSU 					= "tyukan_kensu";            					// 査定結果件数（中間）
    private static final String TYUKAN_KENSU_NULL				= "tyukan_kensu_null";            				// 査定結果件数（中間,部コードNULL）
    private static final String SAISHU_KENSU 					= "saishu_kensu";            					// 査定結果件数（最終）
    private static final String SAISHU_KENSU_NULL 			= "saishu_kensu_null";            				// 査定結果件数（最終,部コードNULL）
    private static final String SHUKAI_TAIRYU 				= "shukai_tairyu";            					// 滞留件数(初回)
    private static final String TYUKAN_TAIRYU 				= "tyukan_tairyu";            					// 滞留件数（中間）
    private static final String SAISHU_TAIRYU 				= "saishu_tairyu";            					// 滞留件数（最終）
	private static final String HASAN_NUM_LAST 				= "hasan_num_last";								// 貸倒懸念・破産更生債権判定先件数(最終月)
	private static final String HASAN_NUM_MIDDLE 				= "hasan_num_middle";							// 貸倒懸念・破産更生債権判定先件数(中間月)
	private static final String HASAN_NUM_FIRST 				= "hasan_num_first";							// 貸倒懸念・破産更生債権判定先件数(初回月)
	private static final String HASAN_NUM_LAST_KAKKO 			= "hasan_num_last_kakko";						// 貸倒懸念・破産更生債権判定先件数（）内(最終月)
	private static final String HASAN_NUM_MIDDLE_KAKKO 		= "hasan_num_middle_kakko";						// 貸倒懸念・破産更生債権判定先件数（）内(中間月)
	private static final String HASAN_NUM_FIRST_KAKKO 		= "hasan_num_first_kakko";						// 貸倒懸念・破産更生債権判定先件数（）内(初回月)
	private static final String OUT_NUM_COUNT 				= "out_num_count";								// 代行排他チェックnum
	private static final String TOGO_ID 						= "togo_id";									// 統合ID
	private static final String USER_NM 						= "user_nm";									// ユーザ名(日本語)
	private static final String USER_NM_E 					= "user_nm_e";									// ユーザ名(英語)
	private static final String COMPANY_CD 					= "company_cd";									// 組織コード
	private static final String COMPANY_NM 					= "company_nm";									// 組織名(日本語)
	private static final String COMPANY_NM_E 					= "company_nm_e";								// 組織名(英語)
	private static final String SOSHIKI_CD 					= "soshiki_cd";									// 会社コード
	private static final String SOSHIKI_NM 					= "soshiki_nm";									// 会社名(日本語)
	private static final String SOSHIKI_NM_E 					= "soshiki_nm_e";								// 会社名(英語)
	private static final String ADMIN_FLG 					= "admin_flg";									// 管理者フラグ
	private static final String MAIL_HAISIN_KBN 				= "mail_haisin_kbn";							// メール配信区分
	private static final String PRINTOUT_DEFAULT_LANG_KBN 	= "printout_default_lang_kbn";					// 帳票出力デフォルト言語区分
	private static final String SYSTEM_KBN 					= "system_kbn";									// システム区分
	private static final String PATTERN 						= "pattern_id";									// パターンID
	private static final String DEFAULT_FLG 					= "default_flg";								// 既定フラグ
	private static final String KEY_VAL 						= "common";										// 区分キー
    private static final String KBN_HYOUJI_VAL 				= "kbn_hyouji_val";         					// 表示値
    private static final String KBN_VAL 						= "kbn_val";                					// 表示キー
    private static final String ID 							= "id";  		              					// ID
    //追加(2.5次仕様のため)
    private static final String YM			 				= "ym";            								// 対象年月
    private static final String HANYOU_SYORI 					= "hanyou_syori";            					// 引当金確認処理中件数
    private static final String NI17 							= "ni17";            							// 引当金確認処理中件数
    private static final String NI18 							= "ni18";            							// 引当金検証処理中件数
    private static final String NI18_NULL 					= "ni18_null";            						// 引当金検証処理中件数（部コードNULL）
    //追加(部門廃止対応)
	private static final String SANSYO_HONBU_CD 			= "sansyo_honbu_cd";							// 参照本部コード


    // プロシージャ名称
    private static final String SP_SS_OS2101_SELECT_M1700					= "SP_SS_OS2101_SELECT_M1700";					// ログインユーザの各処理権限を取得する
    private static final String SP_SS_OS2101_SELECT_M0200					= "SP_SS_OS2101_SELECT_M0200";              	// ログインユーザの業務フローパターンから参照分類２を取得する
    private static final String SP_SS_OS2101_SELECT_M1800					= "SP_SS_OS2101_SELECT_M1800";					// 実施業務【リスト】を取得する
    private static final String SP_SS_OS2101_SELECT_M1701					= "SP_SS_OS2101_SELECT_M1701";					// 業務フロー切替セレクトボックスの設定値を取得する
    private static final String SP_SS_O_SELECT_T2600						= "SP_SS_O_SELECT_T2600";						// 査定期セレクトボックスの設定値を取得する
    private static final String SP_SS_O_SELECT_M2200						= "SP_SS_O_SELECT_M2200";						// 対象年月リストを取得する
    private static final String SP_SS_OS2101_SELECT_HIDAIKO				= "SP_SS_OS2101_SELECT_HIDAIKO";				// 代行画面切替セレクトボックスの設定値を取得する
    private static final String SP_SS_OS2101_SELECT_STATUS				= "SP_SS_OS2101_SELECT_STATUS";					// ステータスを取得する
    private static final String SP_SS_OS2101_SELECT_SATEIKEKKA			= "SP_SS_OS2101_SELECT_SATEIKEKKA";				// 査定結果を取得する
    private static final String SP_SS_OS2101_SELECT_T2200					= "SP_SS_OS2101_SELECT_T2200";					// 代行排他チェックを行う
    private static final String SP_SS_OS2101_DELETE_T2200					= "SP_SS_OS2101_DELETE_T2200";					// T22_代行排他（SST_DAIKOHAITA）を削除する
    private static final String SP_SS_OS_INSERT_T2200						= "SP_SS_OS_INSERT_T2200";          	        // T22_代行排他（SST_DAIKOHAITA）を登録する
    private static final String SP_SS_OS_SELECT_IDCHECK					= "SP_SS_OS_SELECT_IDCHECK";        	        // 被代行者のユーザ情報を取得する
    private static final String SP_SS_OS_SELECT_M2400						= "SP_SS_OS_SELECT_M2400";						// 被代行者IDの業務フローパターン【リスト】を取得する
    private static final String SP_SS_OS2101_SELECT_M0201					= "SP_SS_OS2101_SELECT_M0201";					// 全参照システム区分を取得する
    private static final String SP_SS_OS2101_SELECT_M0202					= "SP_SS_OS2101_SELECT_M0202";					// 全参照査定会社を取得する
    private static final String SP_SS_OS2101_SELECT_BUNRUI2				= "SP_SS_OS2101_SELECT_BUNRUI2";				// 参照分類２を取得する
    private static final String SP_SS_OS2101_SELECT_M2200					= "SP_SS_OS2101_SELECT_M2200";					// 対象年月セレクトボックスの設定値を取得する

    // INパラメータ
    private String langMode;			// 共)言語モード

    /**
     * コンストラクタ
     * 
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public MenuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);

        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (MenuForm)appcontext.getActionForm();

        //ビーンの値を変数に設定
        langMode = cmnData.getComLangMode();

    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
    	langMode = GS.EMPTY_CHARCTER;
    }

    /**
     * ログインユーザの各処理権限を取得する <br>
     * 
     * @exception SQLException
     */
    public void setKengen() throws SQLException {
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M1700, sqlExec);
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        cstmt.setStringIn(user_bean.getComWorkflowId());
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            while ( rs.next() ) {
				// 実質滞留判定登録フラグ
            	user_bean.setComTairyu_hantei_t_flg(rs.getString(TAIRYU_HANTEI_T_FLG));
				// 実質滞留判定承認フラグ
            	user_bean.setComTairyu_hantei_s_flg(rs.getString(TAIRYU_HANTEI_S_FLG));
				// 実質滞留検証登録フラグ
            	user_bean.setComTairyu_kensho_t_flg(rs.getString(TAIRYU_KENSHO_T_FLG));
				// 実質滞留検証承認フラグ
            	user_bean.setComTairyu_kensho_s_flg(rs.getString(TAIRYU_KENSHO_S_FLG));
				// 対象先選定登録フラグ
            	user_bean.setComTaishosaki_sentei_t_flg(rs.getString(TAISHOSAKI_SENTEI_T_FLG));
				// 対象先選定承認フラグ
            	user_bean.setComTaishosaki_sentei_s_flg(rs.getString(TAISHOSAKI_SENTEI_S_FLG));
				// 一次査定登録フラグ
            	user_bean.setComIchiji_satei_t_flg(rs.getString(ICHIJI_SATEI_TOROKU_T_FLG));
				// 一次査定承認フラグ
            	user_bean.setComIchiji_satei_s_flg(rs.getString(ICHIJI_SATEI_TOROKU_S_FLG));
				// 一次査定検証登録フラグ
            	user_bean.setComIchiji_sateikensyo_t_flg(rs.getString(ICHIJI_SATEI_KENSHO_T_FLG));
				// 一次査定検証承認フラグ
            	user_bean.setComIchiji_sateikensyo_s_flg(rs.getString(ICHIJI_SATEI_KENSHO_S_FLG));
				// 二次査定登録フラグ
            	user_bean.setComNiji_satei_t_flg(rs.getString(NIJI_SATEI_T_FLG));
				// 二次査定承認フラグ
            	user_bean.setComNiji_satei_s_flg(rs.getString(NIJI_SATEI_S_FLG));
				// 引当金検証登録フラグ
            	user_bean.setComHikiatekin_kensyo_t_flg(rs.getString(HIKIATEKIN_KENSYO_T_FLG));
				// 引当金検証承認フラグ
            	user_bean.setComHikiatekin_kensyo_s_flg(rs.getString(HIKIATEKIN_KENSYO_S_FLG));
				// 引当金確認登録フラグ
            	user_bean.setComHikiatekin_kakunin_t_flg(rs.getString(HIKIATEKIN_KAKUNIN_T_FLG));
				// 引当金確認承認フラグ
            	user_bean.setComHikiatekin_kakunin_s_flg(rs.getString(HIKIATEKIN_KAKUNIN_S_FLG));
				// クレーム債権再設定登録フラグ
            	user_bean.setComClaim_reset_t_flg(rs.getString(CLAIM_RESET_T_FLG));
				// クレーム債権再設定承認フラグ
            	user_bean.setComClaim_reset_s_flg(rs.getString(CLAIM_RESET_S_FLG));
				// 代行設定フラグ
            	user_bean.setComDaikoset_flg(rs.getString(DAIKOSET_FLG));
				// 査定会社メンテナンスフラグフラグ
            	user_bean.setComSateikasya_flg(rs.getString(SATEIKAISYA_FLG));
				// 業務フローパターンメンテナンスフラグ
            	user_bean.setComWorkflow_pattern_flg(rs.getString(WORKFLOW_PATTERN_FLG));
				// ユーザマスタメンテナンスフラグ
            	user_bean.setComUser_master_flg(rs.getString(USER_MASTER_FLG));
				// 勘定科目マスタメンテナンスフラグ
            	user_bean.setComKanjo_master_flg(rs.getString(KANJO_MASTER_FLG));
				// 抽出条件マスタメンテナンス（本社）フラグ
            	user_bean.setComJoken_master_hq_flg(rs.getString(JOKEN_MASTER_HQ_FLG));
				// 抽出条件マスタメンテナンスフラグ
            	user_bean.setComJoken_master_flg(rs.getString(JOKEN_MASTER_FLG));
				// チャンピオン部メンテナンスフラグ
            	user_bean.setComChampion_bu_flg(rs.getString(CHAMPION_BU_FLG));
				// ゴルフ会員権メンテナンスフラグ
            	user_bean.setComGolf_kaiinken_flg(rs.getString(GOLF_KAIINKEN_FLG));
				// 連結区分マスタUPLOADフラグ
            	user_bean.setComRenketsu_upload_flg(rs.getString(RENKETSU_UPLOAD_FLG));
				// 事務局経由差戻フラグ
            	user_bean.setComJimukyoku_sashi_flg(rs.getString(JIMUKYOKU_SASHI_FLG));
				// 査定完了後差戻フラグ
            	user_bean.setComSatei_kanryo_sashi_flg(rs.getString(SATEI_KANRYO_SASHI_FLG));
				// ２次査定区分
            	user_bean.setComNiji_satei_kbn(rs.getString(NIJI_SATEI_KBN));
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
     * ログインユーザの業務フローパターンから参照分類２を取得する <br>
     * 
     * @exception SQLException
     */
    public void getBunrui2() throws SQLException {
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M0200, sqlExec);
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        cstmt.setStringIn(user_bean.getComUserId());
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    

            List<Map<String, String>> ar_comOparation = new ArrayList<Map<String, String>>();

            while ( rs.next() ) {

                Map<String, String> hm = new HashMap<String, String>();
                // 分類２コード
                hm.put(BUNRUI2, rs.getString(BUNRUI2));
                // 参照本部コード
                hm.put(SANSYO_HONBU_CD, rs.getString(SANSYO_HONBU_CD));

                ar_comOparation.add(hm);
             }
            
            form.setAr_oparation(ar_comOparation);
            
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
     * 実施業務【リスト】を取得する <br>
     * 
     * @exception SQLException
     */
    public void getJisshigyoumuList() throws SQLException {
        
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M1800, sqlExec);
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        cstmt.setStringIn(user_bean.getComWorkflowId());
        cstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);    
            
            ArrayList<Map<String, String>> ar_comOparation = new ArrayList<Map<String, String>>();

            while ( rs.next() ) {
                
        		Map<String, String> hm = new HashMap<String, String>();
                // 実施フェーズ
        		hm.put(GS.JISHI_PHASE, rs.getString(GS.JISHI_PHASE));
                // 開始ステータス
        		hm.put(GS.KAISHI_STATUS, rs.getString(GS.KAISHI_STATUS));
                // 次実施フェーズ
        		hm.put(GS.JI_JISHI_PHASE, rs.getString(GS.JI_JISHI_PHASE));
                // 次開始ステータス
        		hm.put(GS.JI_KAISHI_STATUS, rs.getString(GS.JI_KAISHI_STATUS));
                // 実施フェーズ完了フラグ
        		hm.put(GS.JISHI_PHASE_KANRYO_FLG, rs.getString(GS.JISHI_PHASE_KANRYO_FLG));
                
        		ar_comOparation.add(hm);
            }

            user_bean.setComOparation(ar_comOparation);
            
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
     * 業務フロー切替セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getPatternName() throws SQLException {

        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M1701, sqlExec);
        cstmt.setStringIn(form.getArg_system());
        cstmt.setStringIn(form.getArg_satei_kaisha());
        cstmt.setStringIn(form.getArg_pattern_id());
        cstmt.setStringIn(langMode);
        cstmt.setStringIn(user_bean.getComUserId());
        cstmt.setResultSet(RESULTSET);

        try{
            //ResultSet取得
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
    		List<Map> ar_pattern = new ArrayList<Map>();
			int i = 0;
            while ( rs.next() ) {
				Map<String, String> pattern = new HashMap<String, String>();
				pattern.put(ID, String.valueOf(i));
				pattern.put(PATTERN_ID, rs.getString(PATTERN_ID));
				pattern.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
				pattern.put(SATEIKAISYA_CD, rs.getString(SATEIKAISYA_CD));
				pattern.put(PATTERN_NAME, rs.getString(PATTERN_NAME));

				// 明細配列に取得情報を格納
				ar_pattern.add(i, pattern);
				//初期設定
				if(GS.ON.equals(rs.getString(DEFAULT_FLG))){
					form.setPattern(String.valueOf(i));
				}
				i++;
            }
            form.setAr_pattern(ar_pattern);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 査定期セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSateiKi() throws SQLException {

        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_T2600, sqlExec);
        cstmt.setStringIn(langMode);
        cstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        cstmt.setStringIn(user_bean.getComSansyoBunrui2());
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setResultSet(RESULTSET);

        try{
            //ResultSet取得
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            int i = 0;
            LinkedHashMap<String,String> ar_satei_ki = new LinkedHashMap<String,String>();
            while ( rs.next() ) {
            	if(i == 0){
            		form.setSatei_ki(rs.getString(SATEI_KI));
            	}
            	ar_satei_ki.put(rs.getString(HYOJI_SATEI_KI),rs.getString(SATEI_KI));  
            	i++;
            }    
            
            form.setAr_satei_ki(ar_satei_ki);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
        
    /**
     * 対象年月リストを取得する <br>
     * 
     * @exception SQLException
     */
    public void getYmListAll() throws SQLException {

    	String sateKi = null;
    	sateKi = form.getSatei_ki();
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	
        //ExCallableStatement生成
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
        cstmt.setStringIn(sateKi);
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setStringIn(langMode);
        cstmt.setResultSet(RESULTSET);

        try{
            //ResultSet取得
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            ArrayList<Map<String, String>> ym_list = new ArrayList<Map<String, String>>();
            while ( rs.next() ) { 
            	
            	Map<String, String> hm = new HashMap<String, String>();
				// 表示用年月
        		hm.put(TAISHO_YM_HYOJI, rs.getString(TAISHO_YM_HYOJI));
                // 対象年月
        		hm.put(TAISHO_YM, rs.getString(TAISHO_YM));
        		// 基準日区分
        		hm.put(KIJUNBI_KBN, rs.getString(KIJUNBI_KBN));

        		ym_list.add(hm);
            }
            form.setAr_taisho_ym(ym_list);
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 対象年月セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getYmList() throws SQLException {
    	String sateKi = null;
    	sateKi = form.getSatei_ki();
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	
        //ExCallableStatement生成
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M2200, sqlExec);
        cstmt.setStringIn(sateKi);
        cstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        cstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        cstmt.setStringIn(langMode);
        cstmt.setResultSet(RESULTSET);

        try{
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_ym = new LinkedHashMap<String,String>();
            ar_ym.put(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
            while ( rs.next() ) { 
        		ar_ym.put(rs.getString(TAISHO_YM_HYOJI), rs.getString(TAISHO_YM));
            }
            form.setYm(GS.EMPTY_CHARCTER);
            form.setAr_ym(ar_ym);                
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }
    
    /**
     * 代行画面切替セレクトボックスの設定値を取得する <br>
     * 
     * @exception SQLException
     */
    public void getDaikoList() throws SQLException {
    	
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_HIDAIKO, sqlExec);
        cstmt.setStringIn(langMode);
        cstmt.setStringIn(user_bean.getComUserId());
        cstmt.setResultSet(RESULTSET);

        try{
            //ResultSet取得
            //SQL実行 
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_daiko = new LinkedHashMap<String,String>();
            //課題No.207
            //修正開始
            ar_daiko.put(user_bean.getComUserId(),GS.EMPTY_CHARCTER);
            while ( rs.next() ) {
            	ar_daiko.put(rs.getString(HIDAIKO_ID),rs.getString(HIDAIKO_NM));                    
            //修正完了
            }
            form.setAr_daiko(ar_daiko);
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * ステータスを取得する <br>
     * 
     * @exception SQLException
     */
    public void getStatus() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_STATUS, sqlExec);
        exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        exCstmt.setStringIn(form.getSatei_ki());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(user_bean.getComSansyoHonbuCd());
        exCstmt.setStringIn(form.getYm());
        exCstmt.setResultSet(RESULTSET);
        
        String systemKbn = user_bean.getComWorkflowSystemkbn();
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<Map<String, String>> ar_status = new ArrayList<Map<String, String>>();
            String bu_cd = null;

            while ( rs.next() ) {
                Map<String, String> hm = new HashMap<String, String>();
                
                // 対象年月
                hm.put(YM, Function.insertYmSlash(rs.getString(YM),langMode));
                
				// 査定会社コード
                hm.put(SATEIKAISYA_CD, rs.getString(SATEIKAISYA_CD));
				// 分類２
                hm.put(BUNRUI2, rs.getString(BUNRUI2));
				// 分類２名称
                hm.put(BUNRUI2_NM, rs.getString(BUNRUI2_NM));
				// 部コード
                hm.put(BU_CD, rs.getString(BU_CD));
                bu_cd = rs.getString(BU_CD);
				// 部名称
                hm.put(BU_NM, rs.getString(BU_NM));
				// 組織
                hm.put(SOSHIKI, rs.getString(SOSHIKI));
				// 受信日
                hm.put(JUSHIN_BI, rs.getString(JUSHIN_BI));
				// 受信日表示用
                // 課題No.158
                // 追加開始
                // hm.put(JUSHIN_BI_HYOJI, this.insertDateSlash(rs.getString(JUSHIN_BI),langMode));
                hm.put(JUSHIN_BI_HYOJI, Function.insertDateSlash(rs.getString(JUSHIN_BI),langMode));
                // 追加完了
                // 2重受信フラグ
                hm.put(NIJU_JUSHIN_FLG, rs.getString(NIJU_JUSHIN_FLG));
                
                // 実質滞留債権判定.未処理件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(TAIRYUU_MI_SHORI, rs.getString(TE_NULL));
                } else {
                	hm.put(TAIRYUU_MI_SHORI, rs.getString(TE));
                }
                // 実質滞留債権判定.処理中件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(TAIRYUU_SHORI, rs.getString(TK_NULL));
                } else {
                	hm.put(TAIRYUU_SHORI, rs.getString(TK));
                }
                // 実質滞留債権判定.済み件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(TAIRYUU_ZUMI, rs.getString(TK2_NULL));
                } else {
                	hm.put(TAIRYUU_ZUMI, rs.getString(TK2));
                }
                // 査定.未処理件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(SATEI_MI_SHORI, rs.getString(IE1_NULL));
                } else {
                	hm.put(SATEI_MI_SHORI, rs.getString(IE1));
                }
                // 査定.一次査定中件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(SATEI_ICHI, rs.getString(IK2_NULL));
                } else {
                	hm.put(SATEI_ICHI, rs.getString(IK2));
                }
                // 査定.二次査定中件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(SATEI_NI, rs.getString(NI1_NULL));
                } else {
                	hm.put(SATEI_NI, rs.getString(NI1));
                }
                // 査定.済み件数
                if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(SATEI_ZUMI, rs.getString(NI2_NULL));
                } else {
                	hm.put(SATEI_ZUMI, rs.getString(NI2));
                }
                // 汎用３.未処理件数
                if (GS.MTS.equals(systemKbn) || GS.FOCUS.equals(systemKbn)) {
                	hm.put(HANYOU_MI_SYORI, rs.getString(NI15));
                } else if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(HANYOU_MI_SYORI, rs.getString(NI3_NULL));
                } else if (GS.GSS.equals(systemKbn)) {
                	hm.put(HANYOU_MI_SYORI, rs.getString(NI3));
                }
                // 汎用３.処理中件数
                if (GS.MTS.equals(systemKbn) || GS.FOCUS.equals(systemKbn)) {
                	hm.put(HANYOU_SYORI, rs.getString(NI17));
                } else if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(HANYOU_SYORI, rs.getString(NI18_NULL));
                } else {
                	hm.put(HANYOU_SYORI, rs.getString(NI18));
                }
                // 汎用３.済み件数
                if (GS.MTS.equals(systemKbn) || GS.FOCUS.equals(systemKbn)) {
                	hm.put(HANYOU_ZUMI, rs.getString(NI16));
                } else if (GS.GSS.equals(systemKbn) && (bu_cd == null && Function.trim(bu_cd).length() == 0)) {
                	hm.put(HANYOU_ZUMI, rs.getString(NI4_NULL));
                } else if (GS.GSS.equals(systemKbn)) {
                	hm.put(HANYOU_ZUMI, rs.getString(NI4));
                }
                
                bu_cd = null;
                ar_status.add(hm);
            }
            
            // ActionForm に明細を格納
            form.setAr_status(ar_status);
        
            // ページ設定  
            form.setPager(ar_status);
            
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
     * 査定結果を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSateiKeka() throws SQLException {

        ResultSet rs = null;
        InputCheck check = new InputCheck();
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_SATEIKEKKA, sqlExec);
        exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        exCstmt.setStringIn(form.getSatei_ki());
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(user_bean.getComSansyoHonbuCd());
        exCstmt.setStringIn(form.getFirst_ym());
        exCstmt.setStringIn(form.getMiddle_ym());
        exCstmt.setStringIn(form.getLast_ym());
        exCstmt.setResultSet(RESULTSET);
        String systemKbn = user_bean.getComWorkflowSystemkbn();
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<Map<String, String>> ar_satei_keka = new ArrayList<Map<String, String>>();
            String bu_cd = null;

            while ( rs.next() ) {
                Map<String, String> hm = new HashMap<String, String>();
				// 査定会社コード
                hm.put(SATEI_KAISHA_CD, rs.getString(SATEI_KAISHA_CD));
				// 分類２
                hm.put(BUNRUI2_2, rs.getString(BUNRUI2_2));
				// 分類２名称
                hm.put(BUNRUI2_NM, rs.getString(BUNRUI2_NM));
				// 部コード
                hm.put(BU_CD, rs.getString(BU_CD));
                bu_cd = rs.getString(BU_CD);
				// 部名称
                hm.put(BU_NM, rs.getString(BU_NM));
				// 組織
                hm.put(SOSHIKI, rs.getString(SOSHIKI));
                // 貸倒懸念・破産更生債権判定先件数(最終月)
                if (GS.GSS.equals(systemKbn) && (check.isNullBlank(bu_cd))) {
                	hm.put(HASAN_NUM_LAST, rs.getString(SAISHU_KENSU_NULL));
                } else {
                	hm.put(HASAN_NUM_LAST, rs.getString(SAISHU_KENSU));
                }
                // 貸倒懸念・破産更生債権判定先件数(中間月)
                if (GS.GSS.equals(systemKbn) && (check.isNullBlank(bu_cd))) {
                	hm.put(HASAN_NUM_MIDDLE, rs.getString(TYUKAN_KENSU_NULL));
                } else {
                	hm.put(HASAN_NUM_MIDDLE, rs.getString(TYUKAN_KENSU));
                }
                // 貸倒懸念・破産更生債権判定先件数(初回月)
                if (GS.GSS.equals(systemKbn) && (check.isNullBlank(bu_cd))) {
                	hm.put(HASAN_NUM_FIRST, rs.getString(SHUKAI_KENSU_NULL));
                } else {
                	hm.put(HASAN_NUM_FIRST, rs.getString(SHUKAI_KENSU));
                }

                // 括弧を追加
                String saishu_tairyu = GS.KAKKO_HIDARI + rs.getString(SAISHU_TAIRYU) + GS.KAKKO_MIGI;
                // 貸倒懸念・破産更生債権判定先件数（）内(最終月)
                hm.put(HASAN_NUM_LAST_KAKKO, saishu_tairyu);                

                // 括弧を追加
                String tyukan_tairyu = GS.KAKKO_HIDARI + rs.getString(TYUKAN_TAIRYU) + GS.KAKKO_MIGI;
                // 貸倒懸念・破産更生債権判定先件数（）内(中間月)
                hm.put(HASAN_NUM_MIDDLE_KAKKO, tyukan_tairyu);
                               
                // 括弧を追加
                String shukai_tairyu = GS.KAKKO_HIDARI + rs.getString(SHUKAI_TAIRYU) + GS.KAKKO_MIGI;
                // 貸倒懸念・破産更生債権判定先件数（）内(初回月)
                hm.put(HASAN_NUM_FIRST_KAKKO, shukai_tairyu);
                bu_cd = null;

                ar_satei_keka.add(hm);
            }

            // ActionForm に明細を格納
            form.setAr_satei_resualt(ar_satei_keka);
        
            // ページ設定  
            form.setPager(ar_satei_keka);
            
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
     * 代行排他チェックを行う <br>
     * @return boolean(true:0)
     * @exception SQLException
     */
    public boolean setCheckDaiko() throws SQLException {
        
        // 処理結果フラグ
        boolean result = false;
        int count = 0;

        //ExCallableStatement生成
    	ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_T2200, sqlExec);
        exCstmt.setStringIn(form.getDaiko());
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        exCstmt.setIntOut(OUT_NUM_COUNT);

        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            
            count = exCstmt.getInt(OUT_NUM_COUNT);
            	
        	if (count == 0) {
        		result = true;
        	}
            
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }

        return result;
    }
    
    /**
     * 
     *  T22_代行排他（SST_DAIKOHAITA）を削除する<br>
     * 
     * @throws SQLException
     */
    public void setDeleteT2200() throws SQLException { 
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_DELETE_T2200, sqlExec);
		exCstmt.setStringIn(user_bean.getComUserId());		
		exCstmt.setStringIn(user_bean.getComDaiko_userId());		
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
     *  T22_代行排他（SST_DAIKOHAITA）を登録する<br>
     * 
     * @throws SQLException
     */
    public void setInsertT2200() throws SQLException {
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_INSERT_T2200, sqlExec);
		exCstmt.setStringIn(user_bean.getComUserId());						// 共)ユーザ情報.ユーザID
		exCstmt.setStringIn(user_bean.getComDaiko_userId());				// 共）ユーザ情報.代行ユーザID
		exCstmt.setStringIn(form.getDaiko());								// 機)被代行者ID
			
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
     * 被代行者のユーザ情報を取得する <br>
     * 
     * @exception SQLException
     */
    public void getDaikoUser() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_IDCHECK, sqlExec);
        exCstmt.setStringIn(form.getDaiko());
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    

            while ( rs.next() ) {
				// ユーザID
            	user_bean.setComUserId(rs.getString(TOGO_ID));
				// ユーザ名日本語
            	user_bean.setComUser_Nm(rs.getString(USER_NM));
				// ユーザ名英語
            	user_bean.setComUser_Nm_En(rs.getString(USER_NM_E));
            	
            	// 課題No.189
            	// 追加開始
            	/*
				// 所属組織コード
            	user_bean.setComSyozokuSoshikiCd(rs.getString(COMPANY_CD));
				// 所属組織名称日本語
            	user_bean.setComSyozokuSoshiki_Nm(rs.getString(COMPANY_NM));
				// 所属組織名称英語
            	user_bean.setComSyozokuSoshiki_Nm_En(rs.getString(COMPANY_NM_E));
				// 会社コード
            	user_bean.setComKaishaCd(rs.getString(SOSHIKI_CD));
				// 会社名日本語
            	user_bean.setComKaisha_Nm(rs.getString(SOSHIKI_NM));            	
				// 会社名英語
            	user_bean.setComKaisha_Nm_En(rs.getString(SOSHIKI_NM_E));
            	*/
            	
				// 所属組織コード
            	user_bean.setComSyozokuSoshikiCd(rs.getString(SOSHIKI_CD));
				// 所属組織名称日本語
            	user_bean.setComSyozokuSoshiki_Nm(rs.getString(SOSHIKI_NM));
				// 所属組織名称英語
            	user_bean.setComSyozokuSoshiki_Nm_En(rs.getString(SOSHIKI_NM_E));
				// 会社コード
            	user_bean.setComKaishaCd(rs.getString(COMPANY_CD));
				// 会社名日本語
            	user_bean.setComKaisha_Nm(rs.getString(COMPANY_NM));            	
				// 会社名英語
            	user_bean.setComKaisha_Nm_En(rs.getString(COMPANY_NM_E));
            	
            	// 追加完了
            	
				// システム管理者フラグ
            	user_bean.setComSystemManager_flg(rs.getString(ADMIN_FLG));
				// メール配信区分
            	user_bean.setComMailHaisinKbn(rs.getString(MAIL_HAISIN_KBN));            	
				// メールアドレス
            	user_bean.setComEmailAddr(Function.trim(rs.getString(EMAIL_ADDR)));
				// 帳票出力デフォルト言語区分
            	user_bean.setComTyohyo_default_kbn(rs.getString(PRINTOUT_DEFAULT_LANG_KBN));
				// 分類２コード
            	user_bean.setComSyozokuBunrui2(rs.getString(BUMON_CD));
				// 部コード
            	user_bean.setComSyozokuBuCd(rs.getString(BU_CD));
            	break;
            };
        
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
     * 被代行者IDの業務フローパターン【リスト】を取得する <br>
     * 
     * @exception SQLException
     */
    public void getPattern() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M2400, sqlExec);
        exCstmt.setStringIn(form.getDaiko());
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<HashMap> ar_comWorkflowList = new ArrayList<HashMap>();

            while ( rs.next() ) {
            	HashMap<String, String> hm = new HashMap<String, String>();
				// システム区分
                hm.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
				// 査定会社コード
                hm.put(SATEIKAISYA_CD, rs.getString(SATEIKAISYA_CD));
				// システム区分
                hm.put(PATTERN, rs.getString(PATTERN));
				// 既定フラグ
                hm.put(DEFAULT_FLG, rs.getString(DEFAULT_FLG));

                ar_comWorkflowList.add(hm);
            }
            
            user_bean.setComWorkflowList(ar_comWorkflowList);
        
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
     *  各画面で使用する汎用項目のタイトル名を取得する<br>
     * 
     * @throws SQLException
     */
    public void getHanyouLabel() throws SQLException {

        ResultSet rs = null;
        String val_kbn = null;
        
        try{
            //ResultSet取得
            rs = getKbnval(KEY_VAL,user_bean.getComWorkflowSystemkbn(),langMode);

            while ( rs.next() ) {
            	val_kbn = rs.getString(KBN_VAL);
            	if ("1".equals(val_kbn)) {
            		form.setLabel1(rs.getString(KBN_HYOUJI_VAL));
            	} else if("2".equals(val_kbn)) {
            		form.setLabel2(rs.getString(KBN_HYOUJI_VAL));
            	} else if("3".equals(val_kbn)) {
            		form.setLabel3(rs.getString(KBN_HYOUJI_VAL));
            	} else if("4".equals(val_kbn)) {
            		form.setLabel4(rs.getString(KBN_HYOUJI_VAL));
            	} else if("5".equals(val_kbn)) {
            		form.setLabel5(rs.getString(KBN_HYOUJI_VAL));
            	} else if("6".equals(val_kbn)) {
            		form.setLabel6(rs.getString(KBN_HYOUJI_VAL));
            	} else if("7".equals(val_kbn)) {
            		form.setLabel7(rs.getString(KBN_HYOUJI_VAL));
            	} else if("8".equals(val_kbn)) {
            		form.setLabel8(rs.getString(KBN_HYOUJI_VAL));
            	} else if("9".equals(val_kbn)) {
            		form.setLabel9(rs.getString(KBN_HYOUJI_VAL));
            	} else if("10".equals(val_kbn)) {
            		form.setLabel10(rs.getString(KBN_HYOUJI_VAL));
            	}
            }
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }
    
    /**
     * 全参照システム区分を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSystemKbn() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M0201, sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            ArrayList<Map<String, String>> ar_systemkbn = new ArrayList<Map<String, String>>();

            while ( rs.next() ) {
                Map<String, String> hm = new HashMap<String, String>();
				// システム区分
                hm.put(SYSTEM_KBN, rs.getString(SYSTEM_KBN));
                
                ar_systemkbn.add(hm);
            }
            
            form.setAr_systemkbn(ar_systemkbn);
            
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
     * 全参照査定会社を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSateiKaisha() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_M0202, sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            ArrayList<Map<String, String>> ar_satei_kaisha = new ArrayList<Map<String, String>>();

            while ( rs.next() ) {
                Map<String, String> hm = new HashMap<String, String>();
				// 査定会社コード
                hm.put(SATEI_KAISHA_CD, rs.getString(SATEI_KAISHA_CD));
                
                ar_satei_kaisha.add(hm);
            }
            
            form.setAr_satei_kaisha(ar_satei_kaisha);
        
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
     * 参照分類２を取得する <br>
     * 
     * @exception SQLException
     */
    public void getSansyoBunrui2() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS2101_SELECT_BUNRUI2, sqlExec);
        exCstmt.setStringIn(user_bean.getComUserId());
        exCstmt.setStringIn(form.getSankou_satei_kaisha());
        exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);    
            
            // ActionForm に取得値を格納
            List<Map<String, String>> ar_bunrui2 = new ArrayList<Map<String, String>>();

            while ( rs.next() ) {
                Map<String, String> hm = new HashMap<String, String>();
				// 分類２
                hm.put(BUNRUI2, rs.getString(BUNRUI2));
				// 参照本部コード
                hm.put(SANSYO_HONBU_CD, rs.getString(SANSYO_HONBU_CD));
                
                ar_bunrui2.add(hm);
            }
            
            form.setAr_bunrui2(ar_bunrui2);
        
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
    
	// 課題No.158
    // 削除開始
	/**
	 * 8桁のスラッシュなし年月にスラッシュを加える
	 * @param date 8桁のスラッシュなし日付  
	 * @return 10桁のスラッシュあり日付
	 */
/*
	public String insertDateSlash(String ym,String langmode) {
		if (ym == null || ym.length() != 8) {
			return ym;
		}
		
		if (langmode.equals(GS.LANG_JA)){
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(ym.substring(0,4));
			sbYm.append("/");
			sbYm.append(ym.substring(4,6));
			sbYm.append("/");
			sbYm.append(ym.substring(6,8));
			return sbYm.toString();			
		}else{
			StringBuffer sbYm = new StringBuffer();
			sbYm.append(ym.substring(4,6));
			sbYm.append("/");
			sbYm.append(ym.substring(6,8));
			sbYm.append("/");
			sbYm.append(ym.substring(0,4));
			return sbYm.toString();			
		}
	}
*/	
	// 削除完了
}
