/******************************************************************************
 著作権情報				:
 使用JDK バージョン		:1.5.0.18
 更新履歴
 No		日付			修正者			修正内容
 001		2009/06/30		SSC				新規作成
 002		2009/12/09		SSC				課題No.194 帳票種別プルダウン設定値修正
 003		2023/2/28		NELCO			債権査定基準変更対応「査定会社」制御対応
 004		2025/9/12		STi				F4.8.0_プラネット債権査定基準変更対応 査定会社の制御不要となったため削除
 ******************************************************************************/
package app.system.dbAcc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import app.SessionData;
import app.UserBean;
import app.system.form.DownloadForm;

import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS8101_帳票ダウンロード DBアクセスクラス <br>
 */
public class DownloadDbAcc extends CommonDbAcc {

    private AppContext appContext 	= null;				// ＡＰＰコンテキスト
    private SessionData cmnData 	= null;				// 機能共通セッション
    private UserBean user_bean 	= null;				// ユーザ情報
    private DownloadForm form 		= null;				// アクションフォーム

    //Resultset用文字列
    private static final String KBN_VAL					= "kbn_val";
    private static final String KBN_HYOUJI_VAL			= "kbn_hyouji_val";
    private static final String SATEI_KI 					= "satei_ki";
    private static final String HANKI_SIHANKI_KBN 		= "hanki_sihanki_kbn";
    private static final String SATEI_KAISHA_CD    		= "satei_kaisha_cd";
    private static final String HANYOU2      		        = "hanyou2";
    private static final String HANYOU2_HY		        = "hanyou2_hy";

    //2025/9/12_F4.8.0
    //債権査定基準変更対応
    //追加開始
//    private static final String SATEI_KAISHA_SJ    		= "SJ";
//    private static final String SATEI_KAISHA_PN    		= "PN";
    //追加完了
    //2025/9/12_F4.8.0_ここまで

    //定数
    private static final String TYOHYO_KIND 				= "tyohyo_kind";
    private static final String KESSANKI  				= "kessanki";
    private static final String SYORI_KAISU  				= "syori_kaisu";
    private static final String COMMON_OS81  				= "common_OS81";
    private static final String TASYA_RISK_LIST			= "他社リスクリスト";
    private static final String KBN_VAL00					= "00";
    private static final String KARIZIME2					= "2";
    private static final String BS_TAIHI           		= "017";
    private static final String BS_TAIHI_EN         		= "018";
    private static final String BS_SYOKAI           		= "019";
    private static final String BS_SYOKAI_EN        		= "0110";

    // 課題No.194
    // 追加開始
    private static final String COUNT_NUM        			= "cnt";
    // 追加完了

    private static final String SP_SS_OS8101_SELECT_P0200			= "SP_SS_OS8101_SELECT_P0200";		//帳票種別取得プロシージャ
    private static final String SP_SS_O_SELECT_MAXSATEIKI			= "SP_SS_O_SELECT_MAXSATEIKI";		//最新査定期取得プロシージャ
    private static final String SP_SS_OS8101_SELECT_M0200			= "SP_SS_OS8101_SELECT_M0200";		//汎用１取得プロシージャ
    private static final String SP_SS_OS_SELECT_BUNNRUI2			= "SP_SS_OS_SELECT_BUNNRUI2";		//汎用２取得プロシージャ

    // 課題No.194
    // 追加開始
    private static final String SP_SS_OS8101_SANSYOU_CHECK			= "SP_SS_OS8101_SANSYOU_CHECK";	//事務局権限チェック
    // 追加完了

    //2025/9/12_F4.8.0
    //債権査定基準変更対応
    //追加開始
//	private static final String HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI			= "0131";
//	private static final String HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI		= "0132";
//	private static final String HIKIATEKIN_KENSHOKEKKA_HONSYA				= "0133";
//	private static final String HIKIATEKIN_SATEIKEKKA_PN					= "013";
//	private static final String HIKIATEKIN_SATEIKEKKA_PN_EN					= "014";
//	private static final String HIKIATEKIN_KENSHOKEKKA_PN					= "015";
//	private static final String HIKIATEKIN_KENSHOKEKKA_PN_EN				= "016";
    //追加完了
    //2025/9/12_F4.8.0_ここまで

    // INパラメータ
    private String userId;				// ユーザＩＤ
    private String comLang_mode;		// 言語モード
    private String tyohyo_lang;		// 帳票出力言語

    /**
     * コンストラクタ <br>
     *
     * @param sqlExec SqlExecuter
     * @param log Log
     * @param appcontext AppContext
     */
    public DownloadDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;
        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (DownloadForm)appContext.getActionForm();
        //ビーンの値を変数に設定
        userId = user_bean.getComUserId();
        comLang_mode = cmnData.getComLangMode();
        tyohyo_lang = user_bean.getComTyohyo_default_kbn();
    }

    /**
     * 変数初期化 <br>
     */
    public void initialize() {
        userId = GS.EMPTY_CHARCTER;
        comLang_mode = GS.EMPTY_CHARCTER;
    }

    /**
     * 帳票種別セレクトボックス設定値取得 <br>
     *
     * @exception SQLException
     */
    public void getList_type(String system_kbn) throws SQLException {
        ResultSet rs = null;
        LinkedHashMap<String,String> ar_list_type = new LinkedHashMap<String,String>();
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS8101_SELECT_P0200, sqlExec);
        exCstmt.setStringIn(TYOHYO_KIND);
        exCstmt.setStringIn(system_kbn);
        exCstmt.setStringIn(tyohyo_lang);
        exCstmt.setResultSet(RESULTSET);

        try {
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            ar_list_type.put(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
            while(rs.next()) {
                //他社リスクの重複を避ける
                if(TASYA_RISK_LIST.equals(rs.getString(KBN_HYOUJI_VAL))){
                    ar_list_type.put(rs.getString(KBN_HYOUJI_VAL),KBN_VAL00);
                    continue;
                }
                //BS対比表・BS照会表は対象先選定の処理権限、またはシステム管理者のみ表示
                if(BS_TAIHI.equals(rs.getString(KBN_VAL)) || BS_TAIHI_EN.equals(rs.getString(KBN_VAL))
                        || BS_SYOKAI.equals(rs.getString(KBN_VAL)) || BS_SYOKAI_EN.equals(rs.getString(KBN_VAL))){
                    // 課題No.194
                    // 追加開始
					/*
					if(GS.OFF.equals(user_bean.getComTaishosaki_sentei_t_flg()) && GS.OFF.equals(user_bean.getComTaishosaki_sentei_s_flg())
							&& GS.OFF.equals(user_bean.getComSystemManager_flg())){
						continue;
					}
					*/
                    // 事務局権限を有していない場合、リストには追加しない。
                    if(getCheckOS8101() == 0 && GS.OFF.equals(user_bean.getComSystemManager_flg())){
                        continue;
                    }
                }
                // 追加完了
                ar_list_type.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
            }
            // ActionForm に取得値を格納
            form.setAr_list_type(ar_list_type);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 最新査定期取得 <br>
     *
     * @exception SQLException
     */
    public void getSateiki() throws SQLException {
        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_MAXSATEIKI, sqlExec);
        exCstmt.setStringIn(user_bean.getComWorkflowSateikaisya_cd());
        exCstmt.setStringIn(user_bean.getComSansyoBunrui2());
        exCstmt.setStringIn(user_bean.getComWorkflowSystemkbn());
        exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            // ActionForm に取得値を格納
            if(rs.next()) {
                if(GS.LANG_EN.equals(comLang_mode)){
                    form.setSateiki(rs.getString(SATEI_KI).substring(4) + rs.getString(SATEI_KI).substring(0,4));
                }else{
                    form.setSateiki(rs.getString(SATEI_KI));
                }
                form.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 半期四半期区分セレクトボックス設定値取得 <br>
     *
     * @exception SQLException
     */
    public void getHanki_sihanki_kbn() throws SQLException {
        ResultSet rs = null;
        LinkedHashMap<String,String> ar_hanki_sihanki_kbn = new LinkedHashMap<String,String>();
        try{
            rs = getKbnval(KESSANKI,user_bean.getComWorkflowSystemkbn(),comLang_mode);
            ar_hanki_sihanki_kbn.put(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
            while ( rs.next() ) {
                ar_hanki_sihanki_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
            }
            // ActionForm に取得値を格納
            form.setAr_hanki_sihanki_kbn(ar_hanki_sihanki_kbn);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 〆区分セレクトボックス設定値取得 <br>
     *
     * @exception SQLException
     */
    public void getSime_kbn() throws SQLException {
        ResultSet rs = null;
        LinkedHashMap<String,String> ar_sime_kbn = new LinkedHashMap<String,String>();
        try{
            rs = getKbnval(SYORI_KAISU,GS.GSS,comLang_mode);
            ar_sime_kbn.put(GS.EMPTY_CHARCTER, GS.EMPTY_CHARCTER);
            while ( rs.next() ) {
                //仮〆2は格納しない
                if(KARIZIME2.equals(rs.getString(KBN_VAL))){
                    continue;
                }
                ar_sime_kbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
            }
            // ActionForm に取得値を格納
            form.setAr_sime_kbn(ar_sime_kbn);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 汎用項目タイトル取得 <br>
     *
     * @exception SQLException
     */
    public void getHanyoTitle() throws SQLException {
        ResultSet rs = null;
        try{
            rs = getKbnval(COMMON_OS81,user_bean.getComWorkflowSystemkbn(),comLang_mode);
            int i=1;
            while ( rs.next() ) {
                if(i==1){
                    form.setHanyou1Title(rs.getString(KBN_HYOUJI_VAL));
                }else if(i==2){
                    form.setHanyou2Title(rs.getString(KBN_HYOUJI_VAL));
                }
                i++;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 汎用１セレクトボックス設定値取得 <br>
     *
     * @exception SQLException
     */
    public void getHanyo1() throws SQLException {
        ResultSet rs = null;
        LinkedHashMap<String,String> ar_hanyo1 = new LinkedHashMap<String,String>();
        List<String> list_hanyo1 = new ArrayList<String>();
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS8101_SELECT_M0200, sqlExec);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(form.getSearch_system_kbn());
        exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            // ActionForm に取得値を格納
            while(rs.next()) {
                ar_hanyo1.put(rs.getString(SATEI_KAISHA_CD), rs.getString(SATEI_KAISHA_CD));
                list_hanyo1.add(rs.getString(SATEI_KAISHA_CD));
//2025/09/12 F4.8.0プラネット査定基準変更
//				if(form.getList_type().equals(HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI)
//						||form.getList_type().equals(HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI)
//						|| form.getList_type().equals(HIKIATEKIN_KENSHOKEKKA_HONSYA)){
//					ar_hanyo1.put(SATEI_KAISHA_SJ, SATEI_KAISHA_SJ);
//					list_hanyo1.add(SATEI_KAISHA_SJ);
//
//				}
//				else if(form.getList_type().equals(HIKIATEKIN_SATEIKEKKA_PN)
//						||form.getList_type().equals(HIKIATEKIN_KENSHOKEKKA_PN)
//						||form.getList_type().equals(HIKIATEKIN_SATEIKEKKA_PN_EN)
//						||form.getList_type().equals(HIKIATEKIN_KENSHOKEKKA_PN_EN)){
//					ar_hanyo1.put(SATEI_KAISHA_PN, SATEI_KAISHA_PN);
//					list_hanyo1.add(SATEI_KAISHA_PN);
//				}
//				else{
//					ar_hanyo1.put(rs.getString(SATEI_KAISHA_CD), rs.getString(SATEI_KAISHA_CD));
//					list_hanyo1.add(rs.getString(SATEI_KAISHA_CD));
//				}
//2025/9/12 F4.8.0_プラネット査定基準変更_ここまで
            }
            form.setAr_hanyo1(ar_hanyo1);
            form.setList_hanyo1_all(list_hanyo1);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 汎用２取得① <br>
     *
     * @exception SQLException
     */
    public void getHanyo2_1(String hanyo1) throws SQLException {
        ResultSet rs = null;
        List<String> list_hanyo2 = new ArrayList<String>();
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_BUNNRUI2, sqlExec);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(hanyo1);
        exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            // ActionForm に取得値を格納
            while(rs.next()) {
                list_hanyo2.add(rs.getString(HANYOU2));
            }
            form.setTemp_hanyo2(list_hanyo2);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * 汎用２取得② <br>
     *
     * @exception SQLException
     */
    public void getHanyo2_2() throws SQLException {
        ResultSet rs = null;
        LinkedHashMap<String,String> ar_hanyo2 = new LinkedHashMap<String,String>();
        List<String> list_hanyo2 = new ArrayList<String>();
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_BUNNRUI2, sqlExec);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(form.getHanyo1());
        exCstmt.setResultSet(RESULTSET);
        try {
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            // ActionForm に取得値を格納
            while(rs.next()) {
                ar_hanyo2.put(rs.getString(HANYOU2_HY), rs.getString(HANYOU2));
                list_hanyo2.add(rs.getString(HANYOU2));
            }
            form.setAr_hanyo2(ar_hanyo2);
            form.setTemp_hanyo2(list_hanyo2);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }


    // 課題No.194
    // 追加開始
    /**
     * 事務局権限チェック <br>
     *
     * @exception SQLException
     */
    public int getCheckOS8101() throws SQLException {

        ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS8101_SANSYOU_CHECK, sqlExec);
            exCstmt.setStringIn(user_bean.getComUserId());
            exCstmt.setIntOut(COUNT_NUM);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);

            // 件数
            int cnt = 0;
            cnt = exCstmt.getInt(COUNT_NUM);

            return cnt;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }
    // 追加完了
}