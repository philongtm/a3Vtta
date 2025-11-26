/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/15		SSC				課題No.205 査定先金額判定条件の変更
003		2009/12/17		SSC				課題No.209 承認プルダウン修正
004		2015/02/23		SSC				案件No.BJ201408049 IA化対応時の機能改善
005		2016/03/18		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.tairyu.dbAcc;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.TorokuForm;
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
* OB1102_実質滞留債権判定_明細一覧 DBアクセスクラス
*/
public class TorokuDbAcc extends CommonDbAcc {

	private UserBean user_bean = null;              // ユーザ情報
    private TorihikisakiBean tori_bean = null;      // 取引先情報
    private TorokuForm form = null;                 // アクションフォーム

    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.##";      // 数字のフォーマット：国内
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
    private static final String CST_ZERO		   = "0";

    private static final String SHOW				= "show";			//区分キー（表示件数）
    private static final String TAIRYU_JDG		= "tairyu_jdg";		//区分キー（滞留判定）
    private static final String HS_KBN_HANTEI		= "1";				//判定査定区分 '1'： 滞留判定
	private static final String FLG_HAISIN_N		= "N";				//配信済みフラグ

    //Resultset用文字列
    private static final String OUT_PRE_ANKEN_NO		= "OUT_PRE_ANKEN_NO";       // 前回実施案件No.
    private static final String ICHIJI_FLG			= "ichiji_flg";         	// 一次査定対象フラグ
    private static final String KINGAKU_KEI			= "KINGAKUKEI";             // 金額計
    private static final String KINGAKU_JYOUKEN		= "KINGAKUJYOUKEN";         // 金額条件
    private static final String OUT_SATEI_ANKEN_NO	= "OUT_SATEI_ANKEN_NO";     // 査定案件NO
    private static final String OUT_BUNRUI2			= "OUT_BUNRUI2";            // 分類２
    private static final String OUT_BU_CD			= "OUT_BU_CD";            // 分類３
    private static final String HANTEI_JIYUU			= "hantei_jiyuu";           // 判定事由
    private static final String TAIRYU_HANTEI			= "tairyu_hantei";          // 滞留判定
    private static final String TAIRYU_KBN			= "tairyu_kbn";             // 滞留区分
    private static final String KANJO_NM				= "kanjo_nm";               // 勘定科目
    private static final String KANJO_CD				= "kanjo_cd";               // 勘定科目CD
    private static final String JIMUSHO_CD			= "jimusho_cd";             // 事務所コード
    private static final String KOMOKU5				= "komoku5";                // 項目５
    private static final String KOMOKU4				= "komoku4";                // 項目４
    private static final String KOMOKU3				= "komoku3";                // 項目３
    private static final String KOMOKU2				= "komoku2";                // 項目２
    private static final String KOMOKU1				= "komoku1";                // 項目１
    private static final String INVOICE_NO			= "invoice_no";             // インボイスNo（*1）
    private static final String KEIYAKU_DENPYO_NO		= "keiyaku_denpyo_no";      // 契約伝票No.（*1）
    private static final String TUUKA_CD				= "tuuka_cd";               // 通貨コード
    private static final String KINGAKU				= "kingaku";       			// 金額計
    private static final String SYORI_DT				= "syori_dt";               // 勘定処理日（*1）
    private static final String MANKI_DT				= "manki_dt";               // 満期日（*1）
    private static final String SHUSI_DT				= "shusi_dt";               // 収支予定日（*1）
    private static final String CELL					= "cell";                   // セル名称
    private static final String ANKEN_NO_EDA			= "anken_no_eda";           // 案件No.枝番
    private static final String KBN_HYOUJI_VAL		= "kbn_hyouji_val";         // 表示値
    private static final String KBN_VAL				= "kbn_val";                // 表示キー
    private static final String TANTO_NAME			= "tanto_name";             // 担当者名
    private static final String TOGO_ID				= "togo_id";                // 統合ID
	private static final String PRE_HANTEI_JIYUU		= "pre_hantei_jiyuu";		// 前回判定事由
	private static final String PRE_TAIRYU_HANTEI		= "pre_tairyu_hantei";		// 前回滞留判定
	private static final String CNT 					= "cnt";					// 件数
	private static final String SATEI_ANKEN_NO		= "satei_anken_no";			// 査定案件No
	private static final String CHAMPIONBU_CD		= "championbu_cd";			// 現チャンピオン部コード
	private static final String MAX_TAIRYU_BU_CD	= "max_tairyu_bu_cd";		// 滞留債権額が最も大きい部

    private static final String SP_SS_O_SELECT_SHONINSHA              = "SP_SS_O_SELECT_SHONINSHA";               // 承認担当者セレクトボックスの設定値取得プロシージャ
    private static final String SP_SS_OB1102_SELECT_T0800             = "SP_SS_OB1102_SELECT_T0800";              // 前回実施案件No.取得プロシージャ
    private static final String SP_SS_OB1102_SELECT_ICHIRAN           = "SP_SS_OB1102_SELECT_ICHIRAN";            // 明細一覧情報取得プロシージャ
    private static final String SP_SS_O_UPDATE_T0800                  = "SP_SS_O_UPDATE_T0800";                   // T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新プロシージャ
    private static final String SP_SS_O_INSERT_T1300                  = "SP_SS_O_INSERT_T1300";                   // T13_入力履歴（SST_NYURYOKU_HIST）の登録プロシージャ
    private static final String SP_SS_OB_UPDATE_T1000                 = "SP_SS_OB_UPDATE_T1000";                  // T10_滞留判定（SST_TAIRYUHANTEI）の更新プロシージャ
    private static final String SP_SS_OB_SELECT_T0700                 = "SP_SS_OB_SELECT_T0700";                  // チャンピオン部重複チェックプロシージャ
    private static final String SP_SS_OB_INSERT_T1000                 = "SP_SS_OB_INSERT_T1000";                  // T10_滞留判定の登録プロシージャ
    private static final String SP_SS_OB_SELECT_T0800                 = "SP_SS_OB_SELECT_T0800";                  // 滞留判定済みチェックプロシージャ
    private static final String SP_SS_OB_SELECT_T0100                 = "SP_SS_OB_SELECT_T0100";                  // T01_対象先の一次査定対象FLGをチェックプロシージャ
    private static final String SP_SS_OB_SELECT_SATEICHECK            = "SP_SS_OB_SELECT_SATEICHECK";             // 査定データ作成条件チェックプロシージャ
    private static final String SP_SS_OB_SELECT_T0101                 = "SP_SS_OB_SELECT_T0101";                  // T01_対象先の査定案件No取得プロシージャ
    private static final String SP_SS_OB_INSERT_TAIRYUMEISAI          = "SP_SS_OB_INSERT_TAIRYUMEISAI";           // T16_引当金検討対象BS明細の登録プロシージャ
    private static final String SP_SS_OB_INSERT_MEISAI                = "SP_SS_OB_INSERT_MEISAI";                 // T16_引当金検討対象BS明細の登録（留保債務データ）プロシージャ
    private static final String SP_SS_OB_INSERT_SATEISTAT00           = "SP_SS_OB_INSERT_SATEISTAT00";            // T17_引当金判定表示用の登録プロシージャ
    private static final String SP_SS_OB_INSERT_SATEISTAT01           = "SP_SS_OB_INSERT_SATEISTAT01";            // T14_査定進捗管理の登録プロシージャ
    private static final String SP_SS_OB_UPDATE_T0100                 = "SP_SS_OB_UPDATE_T0100";                  // T01_対象先の更新プロシージャ
    private static final String SP_SS_OB_UPDATE_T0801                 = "SP_SS_OB_UPDATE_T0801";                  // T08_滞留判定進捗管理の更新プロシージャ
    private static final String SP_SS_OB_INSERT_SATEI                 = "SP_SS_OB_INSERT_SATEI";                  // T15_一次二次査定の登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400                  = "SP_SS_O_INSERT_T0400";                   // T04_メール配信の登録プロシージャ
    private static final String SP_SS_OB1102_SELECT_P0200             = "SP_SS_OB1102_SELECT_P0200";
    private static final String SP_SS_OB_SELECT_M0900                 = "SP_SS_OB_SELECT_M0900";					 // 査定データ作成条件有無チェックプロシージャ
    private static final String SP_SS_OB_SELECT_CHAMPIONBU            = "SP_SS_OB_SELECT_CHAMPIONBU";				 // 現チャンピオン部取得
    private static final String SP_SS_OB_SELECT_T0900                 = "SP_SS_OB_SELECT_T0900";					 // 滞留債権額が最も大きい部を取得
    private static final String SP_SS_OB_UPDATE_T0700                 = "SP_SS_OB_UPDATE_T0700";					 // T07_チャンピオン部の更新（フラグをはずす）
    private static final String SP_SS_OB_UPDATE_T0700_2               = "SP_SS_OB_UPDATE_T0700_2";					 // T07_チャンピオン部の更新（フラグを立てる）

    // INパラメータ
    private String workflowSystemkbn;   // 業務フローパターンシステム区分
    private String comLangMode;         // 共)言語モード
    private String systemKbn;           // 共)取引先情報.システム区分
    private String ankenNo;             // 共)取引先情報.案件No.

    // OUTパラメータ
    private String sateiAnkenNo = null;     // 査定案件NO
    private int sateiAnkenNoEda = 0;        // 査定案件NO枝番
    private String outAnkenNoT14 = null;    // 査定案件NO
    private String outBunrui2T14 = null;    // 分類２
    private String outBu_cdT14 = null;    // 分類３



    /**
     * コンストラクタ
     *
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public TorokuDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);

        //ビーン取得
        SessionData cmnData = appcontext.getCMN();
        user_bean = cmnData.getUser_bean();
        tori_bean = cmnData.getTori_bean();
        form = (TorokuForm)appcontext.getActionForm();

        //ビーンの値を変数に設定
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        comLangMode = cmnData.getComLangMode();
        systemKbn = tori_bean.getSystem_kbn();
        ankenNo = tori_bean.getAnken_no();
    }

    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        systemKbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
        ankenNo = GS.EMPTY_CHARCTER;
    }

    /**
     * 承認担当者セレクトボックス設定値取得処理 <br>
     *
     * @exception SQLException
     */
    public void getTanto() throws SQLException {

    	// 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 分類２
        String bunrui2 = tori_bean.getBunrui2();
        // 部コード
        String bu_cd = tori_bean.getBu_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();

        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_SELECT_SHONINSHA, sqlExec);
        // 言語モード
        cstmt.setStringIn(comLangMode);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 分類２
        cstmt.setStringIn(bunrui2);
        // 部コード
        cstmt.setStringIn(bu_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 次実施フェーズ
        cstmt.setStringIn(form.getJi_jishi_phase());
        // 次開始ステータス
        cstmt.setStringIn(form.getJi_kaishi_status());
        cstmt.setStringIn(GS.EMPTY_CHARCTER);
        cstmt.setStringIn(Function.trim(systemKbn));
        cstmt.setResultSet(RESULTSET);

        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_tanto = new LinkedHashMap<String,String>();
            while ( rs.next() ) {
				//課題No.209
				//修正開始
            	//ar_tanto.put(rs.getString(TANTO_NAME),rs.getString(TOGO_ID));
                ar_tanto.put(rs.getString(TOGO_ID),rs.getString(TANTO_NAME));
				//修正完了
            }
            form.setAr_tanto(ar_tanto);
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
     * 表示件数セレクトボックス設定値取得処理 <br>
     *
     * @exception SQLException
     */
    public void getShow() throws SQLException {

    	ResultSet rs = null;
        try {
            // ResultSet取得
            rs = getKbnval(SHOW, workflowSystemkbn, comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String, String> ar_show = new LinkedHashMap<String, String>();
            while (rs.next()) {
                ar_show.put(rs.getString(KBN_HYOUJI_VAL), rs.getString(KBN_VAL));
            }
            form.setAr_show(ar_show);
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
     * 滞留判定セレクトボックス設定値取得処理 <br>
     *
     * @exception SQLException
     */
    public void getTairyuJdg() throws SQLException {

    	ResultSet rs = null;
        try{
            //ResultSet取得
            rs = getKbnval(TAIRYU_JDG, workflowSystemkbn, comLangMode);

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_tairyu_jdg = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
                ar_tairyu_jdg.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
                i++;
            }
            form.setAr_tairyu_jdg(ar_tairyu_jdg);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }

    /**
     * 前回実施案件No.取得 <br>
     *
     * @return 前回実施案件No.
     * @exception SQLException
     */
    public String getPreAnkenNo() throws SQLException {
    	// 査定会社コード
    	String sateikaisya_cd = tori_bean.getSateikaisya_cd();
    	// 店コード
    	String mise_cd = tori_bean.getMise_cd();
    	// 取引先コード
    	String kanjo_cd = tori_bean.getKanjo_cd();
    	// 査定期
    	String satei_ki = tori_bean.getSatei_ki();

    	// 前回実施案件No.
        String preAnkenNo = GS.EMPTY_CHARCTER;
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB1102_SELECT_T0800, sqlExec);
        cstmt.setStringIn(systemKbn);
        cstmt.setStringIn(sateikaisya_cd);
        cstmt.setStringIn(mise_cd);
        cstmt.setStringIn(kanjo_cd);
        cstmt.setStringIn(satei_ki);

        cstmt.setStringOut(OUT_PRE_ANKEN_NO);

        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);

            // 前回実施案件No.取得
            preAnkenNo = cstmt.getString(OUT_PRE_ANKEN_NO);
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
        return preAnkenNo;
    }

    /**
     * 一覧情報取得処理 <br>
     *
     * @param preAnkenNo 前回実施案件No.
     * @exception SQLException
     */
    public boolean getMeisaiList(String preAnkenNo,String kensakuPhase) throws SQLException {
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 初期分類２
        String init_bunrui2 = tori_bean.getInit_bunrui2();
        // 初期部コード
        String init_bu_cd = tori_bean.getInit_bu_cd();
        // 滞留判定検証初期表示フラグ
        boolean tairyukensyoFlg = false;

        // 入力項目チェック共通クラス
    	InputCheck check = new InputCheck();
        //ExCallableStatement生成
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB1102_SELECT_ICHIRAN, sqlExec);
        cstmt.setStringIn(comLangMode);
        cstmt.setStringIn(sateikaisya_cd);
        cstmt.setStringIn(init_bunrui2);
        cstmt.setStringIn(init_bu_cd);
        cstmt.setStringIn(ankenNo);
        cstmt.setStringIn(preAnkenNo);
        cstmt.setStringIn(systemKbn);
        cstmt.setStringIn(kensakuPhase);
        cstmt.setResultSet(RESULTSET);

        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            // ActionForm に取得値を格納
            List<MeisaisyosaiBean> ar_meisai = new ArrayList<MeisaisyosaiBean>();   // 明細配列
            int i = 0;
            while ( rs.next() ) {
            	// 金額計（通貨）タイトル
            	if (i == 0) {
            		form.setTuuka_cd(rs.getString(TUUKA_CD));
            	}

                // 明細情報
                MeisaisyosaiBean listBean = new MeisaisyosaiBean();

                // id
                listBean.setId(Function.getStringOfInt(i));

                // 案件No.枝番
                listBean.setAnken_no_eda(rs.getString(ANKEN_NO_EDA));

                // セル名称
                listBean.setCell_nm(rs.getString(CELL));

                // 収支予定日
                listBean.setSyusi_yoteibi(rs.getString(SHUSI_DT));

                // 満期日
                listBean.setMankibi(rs.getString(MANKI_DT));

                // 勘定処理日
                listBean.setKanjo_syoribi(rs.getString(SYORI_DT));

                // 金額計
                listBean.setKingaku_kei(formatKingaku(rs.getDouble(KINGAKU), systemKbn));

                // 通貨コード
                listBean.setTuuka_cd(rs.getString(TUUKA_CD));

                // 契約伝票No.
                listBean.setKeiyaku_denpyo_no(Function.trim(rs.getString(KEIYAKU_DENPYO_NO)));

                // インボイスNo
               	listBean.setInvoice_no(rs.getString(INVOICE_NO));

                // 項目１
                listBean.setKomoku1(rs.getString(KOMOKU1));

                // 項目２
                listBean.setKomoku2(rs.getString(KOMOKU2));

                // 項目３
                listBean.setKomoku3(rs.getString(KOMOKU3));

                // 項目４
                listBean.setKomoku4(rs.getString(KOMOKU4));

                // 項目５
                listBean.setKomoku5(rs.getString(KOMOKU5));

                // 事務所コード
                listBean.setJimusyo_cd(rs.getString(JIMUSHO_CD));

                // 勘定科目CD
                listBean.setKanjo_kamoku_cd(rs.getString(KANJO_CD));

                // 勘定科目
                listBean.setKanjo_kamoku_nm(rs.getString(KANJO_NM));

                // 滞留区分
                listBean.setTairyu_kbn(rs.getString(TAIRYU_KBN));

                // 滞留判定
                String tairyu_hantei = rs.getString(TAIRYU_HANTEI);

                // 判定事由
                String hantei_jiyuu = rs.getString(HANTEI_JIYUU);
                // 共)取引先情報.フェーズが'10'：滞留判定の場合
													// ↓注意
                if (GS.PHASE_TAIRYU_HANTEI.equals(tori_bean.getPhase()) && GS.GSS.equals(tori_bean.getSystem_kbn())) {
                	// 滞留判定のデータが存在しない場合
                	if (check.isNullBlank(tairyu_hantei)) {
                		// 前回滞留判定を設定
                		tairyu_hantei = rs.getString(PRE_TAIRYU_HANTEI);
                		// 前回判定事由を設定
                		hantei_jiyuu = rs.getString(PRE_HANTEI_JIYUU);
                	}
                }
                										// ↓注意
                if(GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(kensakuPhase)){
					if (check.isNullBlank(tairyu_hantei)) {
						tairyukensyoFlg = true;
					}
                }

                // 滞留判定
                listBean.setTairyu_hantei(tairyu_hantei);

                // 判定事由
                listBean.setHantei_jiyu(hantei_jiyuu);

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
        return tairyukensyoFlg;
    }

    /**
     * T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新（もぎ取り解除時） <br>
     *
     * @exception SQLException
     */
    public void updT08MogitoriKaijyo() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // フェーズ
        String phase = tori_bean.getPhase();
        // 差戻・転送フラグ
        String sasi_ten_flg = tori_bean.getSasi_ten_flg();

        // T08_滞留判定進捗管理.案件保持ユーザIDをNULLで更新する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
        // 滞留判定案件No.
        cstmt.setStringIn(ankenNo);
        // フェーズ
        cstmt.setStringIn(phase);
        // ステータス
        cstmt.setStringIn(GS.STATUS_MISYORI);
        // 案件保持ユーザID
        cstmt.setStringIn(null);
        // 差戻・転送フラグ
        cstmt.setStringIn(sasi_ten_flg);
        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        // 代行ユーザID
        cstmt.setStringIn(null);
        // 更新ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        try {
            //SQL実行
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
     * T13_入力履歴（SST_NYURYOKU_HIST）の登録 <br>
     *
     * @param insertKbn 入力区分
     * @exception SQLException
     */
    public void insT13(String insertKbn) throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 担当者名日本語
        String comUser_Nm = user_bean.getComUser_Nm();
        // 担当者名英語
        String comUser_Nm_En = user_bean.getComUser_Nm_En();
        // 所属部署名日本語
        String comSyozokuSoshiki_Nm = user_bean.getComSyozokuSoshiki_Nm();
        // 所属部署名英語
        String comSyozokuSoshiki_Nm_En = user_bean.getComSyozokuSoshiki_Nm_En();
        // フェーズ
        String phase = tori_bean.getPhase();
        // 代行者名日本語
        String comDaiko_user_nm = user_bean.getComDaiko_user_nm();
        // 代行者名英語
        String comDaiko_user_nm_en = user_bean.getComDaiko_user_nm_en();
        // 承認担当ユーザID
        String syonin_tanto = form.getSyonin_tanto();

        // T13_入力履歴の登録
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
        // 案件NO
        cstmt.setStringIn(ankenNo);
        // 判定査定区分
        cstmt.setStringIn(HS_KBN_HANTEI);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 担当ユーザID
        cstmt.setStringIn(comUserId);
        // 担当者名日本語
        cstmt.setStringIn(comUser_Nm);
        // 担当者名英語
        cstmt.setStringIn(comUser_Nm_En);
        // 所属部署名日本語
        cstmt.setStringIn(comSyozokuSoshiki_Nm);
        // 所属部署名英語
        cstmt.setStringIn(comSyozokuSoshiki_Nm_En);
        // フェーズ
        cstmt.setStringIn(phase);
        // 入力区分
        cstmt.setStringIn(insertKbn);
        // 登録ユーザID
        cstmt.setStringIn(comUserId);

        if (comDaiko_userId == null) {
            // 通常時
            // 代行ユーザID
            cstmt.setStringIn(null);
            // 代行者名日本語
            cstmt.setStringIn(null);
            // 代行者名英語
            cstmt.setStringIn(null);
        } else {
            // 代行時
            // 代行ユーザID
            cstmt.setStringIn(comDaiko_userId);
            // 代行者名日本語
            cstmt.setStringIn(comDaiko_user_nm);
            // 代行者名英語
            cstmt.setStringIn(comDaiko_user_nm_en);
        }
        // 登録箇所
        cstmt.setStringIn(null);
        // コメント内容
        cstmt.setStringIn(null);
        // 転送元分類2
        cstmt.setStringIn(null);
        // 転送元部
        cstmt.setStringIn(null);
        // 承認担当ユーザID
        cstmt.setStringIn(syonin_tanto);
        try {
            //SQL実行
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
     * T10_滞留判定の更新（件数分ループ） <br>
     *
     * @exception SQLException
     */
    public void updT10() throws SQLException {
        // ユーザID
        String userId = user_bean.getComUserId();
        // 代行ユーザID
        String daikoUserId = user_bean.getComDaiko_userId();
        // 滞留判定案件No
        String anken_no = tori_bean.getAnken_no();
        // フェーズ
        String phase = tori_bean.getPhase();

        // 明細リスト
        List ar_meisai = form.getAr_meisai();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        for (Object obj : ar_meisai) {
            MeisaisyosaiBean meisaiBean = (MeisaisyosaiBean)obj;
            // 滞留判定
            String tairyu_hantei = meisaiBean.getTairyu_hantei();
            // 判定事由
            String hantei_jiyu = meisaiBean.getHantei_jiyu();
            // T10_滞留判定の更新（件数分ループ）
            cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T1000, sqlExec);
            // 滞留判定
            cstmt.setStringIn(toZero(tairyu_hantei));
            // 判定事由
            cstmt.setStringIn(hantei_jiyu);
            // ユーザID
            cstmt.setStringIn(userId);
            // 代行ユーザID
            cstmt.setStringIn(daikoUserId);
            // 滞留判定案件No
            cstmt.setStringIn(anken_no);
            // 滞留判定案件No枝番
            cstmt.setStringIn(meisaiBean.getAnken_no_eda());
            // フェーズ
            cstmt.setStringIn(phase);

            try {
                //SQL実行
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
    }

    /**
     * T10_滞留判定の更新（件数分ループ） <br>
     *
     * @exception SQLException
     */
    public void updT10Kaijo() throws SQLException {
        // ユーザID
        String userId = user_bean.getComUserId();
        // 代行ユーザID
        String daikoUserId = user_bean.getComDaiko_userId();
        // 滞留判定案件No
        String anken_no = tori_bean.getAnken_no();
        // フェーズ
        String phase = tori_bean.getPhase();

        // 明細リスト
        List ar_meisai = form.getAr_meisai();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        for (Object obj : ar_meisai) {
            MeisaisyosaiBean meisaiBean = (MeisaisyosaiBean)obj;
            // T10_滞留判定の更新（件数分ループ）
            cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T1000, sqlExec);
            // 滞留判定
            cstmt.setStringIn(GS.EMPTY_CHARCTER);
            // 判定事由
            cstmt.setStringIn(GS.EMPTY_CHARCTER);
            // ユーザID
            cstmt.setStringIn(userId);
            // 代行ユーザID
            cstmt.setStringIn(daikoUserId);
            // 滞留判定案件No
            cstmt.setStringIn(anken_no);
            // 滞留判定案件No枝番
            cstmt.setStringIn(meisaiBean.getAnken_no_eda());
            // フェーズ
            cstmt.setStringIn(phase);

            try {
                //SQL実行
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
    }

    /**
     * T10_滞留判定の更新（滞留判定セレクトボックス変更時） <br>
     *
     * @exception SQLException
     */
    public void updT10_2() throws SQLException {
        // ユーザID
        String userId = user_bean.getComUserId();
        // 代行ユーザID
        String daikoUserId = user_bean.getComDaiko_userId();
        // 滞留判定案件No
        String anken_no = tori_bean.getAnken_no();
        // フェーズ
        String phase = tori_bean.getPhase();

        // 明細リスト
        List ar_meisai = form.getAr_meisai();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        for (Object obj : ar_meisai) {
            MeisaisyosaiBean meisaiBean = (MeisaisyosaiBean)obj;
            if(String.valueOf(form.getId()).equals(meisaiBean.getId())){
            	continue;
            }
            // 滞留判定
            String tairyu_hantei = meisaiBean.getTairyu_hantei();
            // 判定事由
            String hantei_jiyu = meisaiBean.getHantei_jiyu();
            // T10_滞留判定の更新（件数分ループ）
            cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T1000, sqlExec);
            // 滞留判定
            cstmt.setStringIn(tairyu_hantei);
            // 判定事由
            cstmt.setStringIn(hantei_jiyu);
            // ユーザID
            cstmt.setStringIn(userId);
            // 代行ユーザID
            cstmt.setStringIn(daikoUserId);
            // 滞留判定案件No
            cstmt.setStringIn(anken_no);
            // 滞留判定案件No枝番
            cstmt.setStringIn(meisaiBean.getAnken_no_eda());
            // フェーズ
            cstmt.setStringIn(phase);

            try {
                //SQL実行
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
    }

    /**
     * T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新（保存処理時） <br>
     *
     * @exception SQLException
     */
    public void updT08Save() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // フェーズ
        String phase = tori_bean.getPhase();
        // 差戻・転送フラグ
        String sasi_ten_flg = tori_bean.getSasi_ten_flg();

        // T08_滞留判定進捗管理.案件保持ユーザIDをNULLで更新する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
        // 滞留判定案件No.
        cstmt.setStringIn(ankenNo);
        // フェーズ
        cstmt.setStringIn(phase);
        // ステータス
        cstmt.setStringIn(GS.STATUS_SYORICHU);
        // 案件保持ユーザID
        cstmt.setStringIn(comUserId);
        // 差戻・転送フラグ
        cstmt.setStringIn(sasi_ten_flg);
        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        // 代行ユーザID
        cstmt.setStringIn(comDaiko_userId);
        // 更新ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        try {
            //SQL実行
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
     * チャンピオン部重複チェック <br>
     *
     * @return 件数
     * @exception SQLException
     */
    public int selT07() throws SQLException {
        // 件数
        int count = 0;

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();

        // 件数を取得する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0700, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);

        cstmt.setIntOut(CNT);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);

            count = cstmt.getInt(CNT);
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
        return count;
    }

    /**
     * T10_滞留判定の登録（SELECT結果をINSERT） <br>
     *
     * @exception SQLException
     */
    public void insT10Select() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();

        // T10_滞留判定の登録（SELECT結果をINSERT）
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_T1000, sqlExec);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 案件No
        cstmt.setStringIn(ankenNo);

        try {
            //SQL実行
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
     * T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新①（登録アクション処理⑥時） <br>
     *
     * @exception SQLException
     */
    public void updT08Regist6() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 機)承認担当者
        String syoninTanto = form.getSyonin_tanto();

        // T08_滞留判定進捗管理.案件保持ユーザIDをNULLで更新する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
        // 滞留判定案件No.
        cstmt.setStringIn(ankenNo);
        // フェーズ
        cstmt.setStringIn(form.getJi_jishi_phase());
        // ステータス
        cstmt.setStringIn(form.getJi_kaishi_status());
        // 案件保持ユーザID
        cstmt.setStringIn(syoninTanto);
        // 差戻・転送フラグ
        cstmt.setStringIn(null);
        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_KA);
        // 代行ユーザID
        cstmt.setStringIn(comDaiko_userId);
        // 更新ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        try {
            //SQL実行
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
     * T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新②（登録アクション処理⑦時） <br>
     *
     * @exception SQLException
     */
    public void updT08Regist7() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();

        // T08_滞留判定進捗管理.案件保持ユーザIDをNULLで更新する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
        // 滞留判定案件No.
        cstmt.setStringIn(ankenNo);
        // フェーズ
        cstmt.setStringIn(form.getJi_jishi_phase());
        // ステータス
        cstmt.setStringIn(form.getJi_kaishi_status());
        // 案件保持ユーザID
        cstmt.setStringIn(null);
        // 差戻・転送フラグ
        cstmt.setStringIn(null);
        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        // 代行ユーザID
        cstmt.setStringIn(comDaiko_userId);
        // 更新ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        try {
            //SQL実行
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
     * T08_滞留判定進捗管理（SST_TAIRYU_STAT）の更新③（登録アクション処理⑧時） <br>
     *
     * @exception SQLException
     */
    public void updT08Regist8() throws SQLException {
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // フェーズ
        String phase = tori_bean.getPhase();

        // T08_滞留判定進捗管理.案件保持ユーザIDをNULLで更新する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);
        // 滞留判定案件No.
        cstmt.setStringIn(ankenNo);
        // フェーズ
        cstmt.setStringIn(phase);
        // ステータス
        cstmt.setStringIn(GS.STATUS_KANRYO);
        // 案件保持ユーザID
        //課題No.129
        //修正開始
        //cstmt.setStringIn(tori_bean.getHoji_user_id());
        cstmt.setStringIn(null);
        //修正完了
        // 差戻・転送フラグ
        cstmt.setStringIn(null);
        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);
        // 代行ユーザID
        cstmt.setStringIn(comDaiko_userId);
        // 更新ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        try {
            //SQL実行
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
     * 滞留判定済みチェック <br>
     *
     * @return 件数
     * @exception SQLException
     */
    public int selT08KanryoCheck() throws SQLException {
        // 件数
        int count = 0;

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();

        // 件数を取得する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0800, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 店コード
        cstmt.setStringIn(mise_cd);

        cstmt.setIntOut(CNT);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            count = cstmt.getInt(CNT);
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
        return count;
    }

    /**
     * T01_対象先の一次査定対象FLGをチェック <br>
     *
     * @return 一次査定対象FLG
     * @exception SQLException
     */
    public String selT01IchijiSateiFlgCheck() throws SQLException {
        // 一次査定対象フラグ
        String ichiji_flg = GS.EMPTY_CHARCTER;

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();

        // T01_対象先の一次査定対象FLGを取得する。
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0100, sqlExec);
        // システム区分
		cstmt.setStringIn(system_kbn);
		// 査定会社コード
		cstmt.setStringIn(sateikaisya_cd);
		// 対象年月
		cstmt.setStringIn(taisyo_ym);
		// 取引先コード
		cstmt.setStringIn(kanjo_cd);
		// 店コード
		cstmt.setStringIn(mise_cd);
		// 半期・四半期区分
		cstmt.setStringIn(hanki_sihanki_kbn);
		// 処理回数
		cstmt.setStringIn(syori_kaisu);
        // 一次査定対象フラグ
        cstmt.setStringOut(ICHIJI_FLG);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);

            ichiji_flg = cstmt.getString(ICHIJI_FLG);
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
        return ichiji_flg;
    }

    /**
     * 査定データ作成条件有無チェック <br>
     *
     * @return int
     * @exception SQLException
     */
    public int selM09() throws SQLException {
        String system_kbn = tori_bean.getSystem_kbn();
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();	//課題対応No,205	査定会社コード追加
        String mise_cd = tori_bean.getMise_cd();				//課題対応No,205	店コード追加
        int cnt = 0;
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_M0900, sqlExec);
        cstmt.setStringIn(system_kbn);
        cstmt.setStringIn(sateikaisya_cd);						//課題対応No,205	査定会社コード追加
        cstmt.setStringIn(mise_cd);								//課題対応No,205	店コード追加
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
     * @return 一次査定対象FLG true:査定対象
     * @exception SQLException
     */
    public boolean selSateiDataCheck() throws SQLException {
        // 金額条件
        double kingakuJyoken = 0;
        // 金額計
        double kingakuKei = 0;

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_SATEICHECK, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 店コード
        cstmt.setStringIn(mise_cd);

        cstmt.setResultSet(RESULTSET);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);

            if ( rs.next() ) {
                // 金額条件
                kingakuJyoken = rs.getDouble(KINGAKU_JYOUKEN);
                // 金額計
                kingakuKei = rs.getDouble(KINGAKU_KEI);
                // 金額条件 <= 金額計 の場合、査定対象とする
                if (kingakuJyoken<=kingakuKei) {
                    return true;
                }
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
        return false;
    }

    /**
     * 現チャンピオン部取得 <br>
     *
     * @param tori_bean
     * @return 現チャンピオン部
     * @exception SQLException
     */

    public String selCh() throws SQLException {
    	String championbu_cd = GS.EMPTY_CHARCTER;
    	String system_kbn = tori_bean.getSystem_kbn();
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        String taisyo_ym = tori_bean.getTaisyo_ym();
        String kanjo_cd = tori_bean.getKanjo_cd();
        String mise_cd = tori_bean.getMise_cd();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_CHAMPIONBU, sqlExec);
		cstmt.setStringIn(system_kbn);
		cstmt.setStringIn(sateikaisya_cd);
		cstmt.setStringIn(taisyo_ym);
		cstmt.setStringIn(kanjo_cd);
		cstmt.setStringIn(mise_cd);
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
     * @param tori_bean
     * @return 滞留債権額が最も大きい部
     * @exception SQLException
     */

    public String selT09() throws SQLException {
    	String max_tairyu_bu_cd = GS.EMPTY_CHARCTER;
    	String system_kbn = tori_bean.getSystem_kbn();
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        String taisyo_ym = tori_bean.getTaisyo_ym();
        String kanjo_cd = tori_bean.getKanjo_cd();
        String mise_cd = tori_bean.getMise_cd();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_T0900, sqlExec);
		cstmt.setStringIn(system_kbn);
		cstmt.setStringIn(sateikaisya_cd);
		cstmt.setStringIn(taisyo_ym);
		cstmt.setStringIn(kanjo_cd);
		cstmt.setStringIn(mise_cd);
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
     * @param tori_bean
     * @param championbu_cd
     * @exception SQLException
     */
    public void updT07(String championbu_cd) throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();

         // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0700, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 現チャンピオン部
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
     * T07_チャンピオン部の更新（フラグを立てる） <br>
     *
     * @param tori_bean
     * @param max_tairyu_bu_cd
     * @exception SQLException
     */
    public void updT07_2(String max_tairyu_bu_cd) throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();

         // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0700_2, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 滞留債権額が最も大きい部
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
    	String system_kbn = tori_bean.getSystem_kbn();
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        String taisyo_ym = tori_bean.getTaisyo_ym();
        String kanjo_cd = tori_bean.getKanjo_cd();
        String mise_cd = tori_bean.getMise_cd();
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();
        String syori_kaisu = tori_bean.getSyori_kaisu();

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
     * T16_引当金検討対象BS明細の登録（SELECT結果をINSERT） <br>
     *
     * @exception SQLException
     */
    public void insT16TairyuMeisai(String satei_anken_no) throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 査定期
        String satei_ki = tori_bean.getSatei_ki();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_TAIRYUMEISAI, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定期
        cstmt.setStringIn(satei_ki);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // 査定案件NO
        cstmt.setStringIn(satei_anken_no);

        // 査定案件NO
        cstmt.setStringOut(OUT_SATEI_ANKEN_NO);
        // 査定案件NO枝番
        cstmt.setIntOut(ANKEN_NO_EDA);
        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);

            // 査定案件NO
            sateiAnkenNo = cstmt.getString(OUT_SATEI_ANKEN_NO);
            // 査定案件NO枝番
            sateiAnkenNoEda = cstmt.getInt(ANKEN_NO_EDA);
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
     * T16_引当金検討対象BS明細の登録（留保債務データ） <br>
     *
     * @exception SQLException
     */
    public void insT16Meisai() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_MEISAI, sqlExec);
        // 査定案件NO
        cstmt.setStringIn(sateiAnkenNo);
        // 査定案件NO枝番
        cstmt.setStringIn(String.valueOf(sateiAnkenNoEda));
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);

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
     * T17_引当金判定表示用の登録（SELECT結果をINSERT） <br>
     *
     * @exception SQLException
     */
    public void insT17HikiateHyoji() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEISTAT00, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }

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
     * T14_査定進捗管理の登録（SELECT結果をINSERT） <br>
     *
     * @exception SQLException
     */
    public void insT14SateiShinchoku() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();
        // 基準日区分
        String kijunbi_kbn = tori_bean.getKijunbi_kbn();
        // 査定期
        String satei_ki = tori_bean.getSatei_ki();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEISTAT01, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 基準日区分
        cstmt.setStringIn(kijunbi_kbn);
        // 査定期
        cstmt.setStringIn(satei_ki);

        // 査定案件NO
        cstmt.setStringOut(OUT_SATEI_ANKEN_NO);
        // 分類２
        cstmt.setStringOut(OUT_BUNRUI2);
        // 部コード
        cstmt.setStringOut(OUT_BU_CD);

        try {
            // SQL実行
            cstmt.execute();
            isError(cstmt);

            // 査定案件NO
            outAnkenNoT14 = cstmt.getString(OUT_SATEI_ANKEN_NO);
            // 分類２
            outBunrui2T14 = cstmt.getString(OUT_BUNRUI2);
            // 部コード
            outBu_cdT14 = cstmt.getString(OUT_BU_CD);

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
     * T01_対象先の更新 <br>
     *
     * @exception SQLException
     */
    public void updT01Taishosaki() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0100, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 査定案件NO
        cstmt.setStringIn(outAnkenNoT14);

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
     * T08_滞留判定進捗管理の更新 <br>
     *
     * @exception SQLException
     */
    public void updT08Select() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T0801, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }

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
     * T15_一次二次査定の登録（SELECT結果をINSERT） <br>
     *
     * @exception SQLException
     */
    public void insT15Satei() throws SQLException {

        // システム区分
        String system_kbn = tori_bean.getSystem_kbn();
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 店コード
        String mise_cd = tori_bean.getMise_cd();
        // 取引先コード
        String kanjo_cd = tori_bean.getKanjo_cd();
        // 処理回数
        String syori_kaisu = tori_bean.getSyori_kaisu();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();
        // 半期・四半期区分
        String hanki_sihanki_kbn = tori_bean.getHanki_sihanki_kbn();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_INSERT_SATEI, sqlExec);
        // システム区分
        cstmt.setStringIn(system_kbn);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 店コード
        cstmt.setStringIn(mise_cd);
        // 取引先コード
        cstmt.setStringIn(kanjo_cd);
        // 処理回数
        cstmt.setStringIn(syori_kaisu);
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }
        // 半期・四半期区分
        cstmt.setStringIn(hanki_sihanki_kbn);

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
     * T04_メール配信の登録 <br>
     *
     * @exception SQLException
     */
    public void insT04Mail() throws SQLException {

        // 案件No.
        String ankenNo = null;
        // 配信先部門
        String haishinBumon = null;
        // 配信先部
        String haishinBu = null;
        // 配信先担当
        String haishinTanto = null;

        if (GS.PHASE_TAIRYU_HANTEI.equals(form.getJi_jishi_phase())
                || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(form.getJi_jishi_phase())) {
            //機)次実施フェーズが滞留判定(フェーズ：10) or 滞留判定検証（フェーズ：20）の場合
            // 案件No.
            ankenNo = this.ankenNo;
            // 配信先部門
            haishinBumon = tori_bean.getBunrui2();
            haishinBu = tori_bean.getBu_cd();

        } else if (GS.PHASE_ICHIJI_SATEI.equals(form.getJi_jishi_phase())) {
            // 機)次実施フェーズが一次査定(フェーズ：40)の場合
            // 案件No.
            ankenNo = outAnkenNoT14;
            // 配信先部門
            haishinBumon = outBunrui2T14;
            haishinBu = outBu_cdT14;
        }
        // システム区分が01以外の場合、部コード空にする
        if(!GS.GSS.equals(tori_bean.getSystem_kbn())){
        	haishinBu = null;
        }

        if (GS.STATUS_SYONIN_MACHI.equals(form.getJi_kaishi_status())) {
            // 機)次開始ステータスが承認(ステータス：30)の場合
            // 配信先担当
            haishinTanto = form.getSyonin_tanto();
        } else {
            // 機)次開始ステータスが承認(ステータス：30)以外の場合
            // 配信先担当
            haishinTanto = null;
        }
        // 対象年月
        String taisyo_ym = tori_bean.getTaisyo_ym();
        // 査定会社コード
        String sateikaisya_cd = tori_bean.getSateikaisya_cd();
        // ユーザID
        String comUserId = user_bean.getComUserId();
        // 代行ユーザID
        String comDaiko_userId = user_bean.getComDaiko_userId();

        // 査定データ作成条件
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_O_INSERT_T0400, sqlExec);
        // ユーザID
        cstmt.setStringIn(comUserId);
        // 案件No
        cstmt.setStringIn(ankenNo);
        // 対象年月
        cstmt.setStringIn(taisyo_ym);
        // 査定会社コード
        cstmt.setStringIn(sateikaisya_cd);
        // 分類２
        cstmt.setStringIn(haishinBumon);
        // 部コード
        cstmt.setStringIn(haishinBu);
        // 次実施フェーズ
        cstmt.setStringIn(form.getJi_jishi_phase());
        // 次開始ステータス
        cstmt.setStringIn(form.getJi_kaishi_status());
        // 配信先担当
        cstmt.setStringIn(haishinTanto);
        // 配信済みフラグ
        cstmt.setStringIn(FLG_HAISIN_N);
        // 画面ID
        cstmt.setStringIn(form.toString());
        // ユーザID
        if (comDaiko_userId == null) {
            cstmt.setStringIn(comUserId);
        } else {
            cstmt.setStringIn(comDaiko_userId);
        }

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
     * 区分値取得（一括取り込み） <br>
     *
     * @exception SQLException
     */
    public String selP02(String tairyuhantei) throws SQLException {
    	String kbn_val = null;
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	cstmt = new ExCallableStatement(SP_SS_OB1102_SELECT_P0200, sqlExec);
        cstmt.setStringIn(TAIRYU_JDG);
        cstmt.setStringIn(systemKbn);
        cstmt.setStringIn(tairyuhantei);
        cstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
            rs = cstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	kbn_val = rs.getString(KBN_VAL);
            }
            return kbn_val;
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }

    /**
     * T10_滞留判定の更新（一括取り込み） <br>
     *
     * @exception SQLException
     */
    public void updTorikomi(String anken_no_eda,String tairyu_hantei,String hantei_jiyu) throws SQLException {
        String userId = user_bean.getComUserId();
        String daikoUserId = user_bean.getComDaiko_userId();
        String anken_no = tori_bean.getAnken_no();
        String phase = tori_bean.getPhase();

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_UPDATE_T1000, sqlExec);
        cstmt.setStringIn(toZero(tairyu_hantei));
        cstmt.setStringIn(hantei_jiyu);
        cstmt.setStringIn(userId);
        cstmt.setStringIn(daikoUserId);
        cstmt.setStringIn(anken_no);
        cstmt.setStringIn(anken_no_eda);
        cstmt.setStringIn(phase);
        try {
            //SQL実行
            cstmt.execute();
            isError(cstmt);
        } finally {
            if (rs != null) {
            	rs.close();
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
	 * 空文字をゼロに変換
	 */
	private String toZero(String val){
		if(GS.EMPTY_CHARCTER.equals(Function.trim(val))){
			return CST_ZERO;
		}
		return val;
	}
}