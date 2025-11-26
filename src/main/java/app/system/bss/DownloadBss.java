/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/10/20		SSC				残課題対応 項番32 国内版集計表対応
003		2009/11/16		SSC				残課題対応 項番91 BS対比表、BS照会表 レスポンス対応
004		2009/12/09		SSC				課題No.195 国内帳票ダウンロード時、必須入力チェックに分類２を追加
005		2009/12/23		SSC				課題No.206 滞留判定明細(本社)　新規作成
006		2023/02/28		NELCO			債権査定基準変更対応 査定会社制御対応
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.UserBean;
import app.system.dbAcc.DownloadDbAcc;
import app.system.form.DownloadForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS8101_帳票ダウンロード ビジネスロジッククラス <br>
 */
public class DownloadBss {

	//帳票種別判別用
	private static final String TAIRYUMEISAI_HO     = "011";	//滞留判定明細(国内用)
	private static final String TAIRYUMEISAI_HO_EN  = "012";	//CreditDetails(For Domestic)
	private static final String SATEIKEKKA_HO       = "013";	//引当金額集計表(査定結果)(PN用)
	private static final String SATEIKEKKA_HO_EN    = "014";	//Reserve Amount Total Table(Result of Assessment)(For PN)
	private static final String KENSYOKEKKA         = "015";	//引当金額集計表(検証結果)(PN用)
	private static final String KENSYOKEKKA_EN      = "016";	//Reserve Amount Total Table(Verification)(For PN)
	private static final String BS_TAIHI            = "017";
	private static final String BS_TAIHI_EN         = "018";
	private static final String BS_SYOKAI           = "019";
	private static final String BS_SYOKAI_EN        = "0110";
	private static final String TAIRYUMEISAI_L      = "021";
	private static final String TAIRYUMEISAI_L_EN   = "022";
	private static final String SATEIKEKKA_L        = "023";
	private static final String SATEIKEKKA_L_EN     = "024";
	private static final String KAKUNINKEKKA_L      = "025";
	private static final String KAKUNINKEKKA_L_EN   = "026";
	private static final String TAIRYUMEISAI        = "031";
	private static final String TAIRYUMEISAI_EN     = "032";
	private static final String SATEIKEKKA          = "033";
	private static final String SATEIKEKKA_EN       = "034";
	private static final String KAKUNINKEKKA        = "035";
	private static final String KAKUNINKEKKA_EN     = "036";
	private static final String TASYA_RISK          = "00";
	//債権査定基準変更対応
	//追加開始
	private static final String HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI			= "0131";		//引当金額集計表(査定結果)(本社用：初回月) 0131
	private static final String HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI		= "0132";		//引当金額集計表(査定結果)(本社用：初回月以外) 0132
	private static final String HIKIATEKIN_KENSHOKEKKA_HONSYA				= "0133";		//引当金額集計表(検証結果)(本社用) 0133
	//追加完了

	//帳票種別(海外)
	private static final String LIST_TAIRYUMEISAI_HO     = "1";	//滞留判定明細（国内用）
	private static final String LIST_SATEIKEKKA_HO       = "2";	//引当金額集計表（査定結果）（本社用）
	private static final String LIST_KENSYOKEKKA         = "3";	//引当金額集計表（検証結果）
	private static final String LIST_BS_TAIHI            = "4";	//BS対比表
	private static final String LIST_BS_SYOKAI           = "5";	//BS照会表
	private static final String LIST_TAIRYUMEISAI        = "6";	//滞留判定明細(レジャーサイズ含む)
	private static final String LIST_SATEIKEKKA          = "7";	//引当金額集計表（査定結果）（レジャーサイズ含む）
	private static final String LIST_KAKUNINKEKKA        = "8";	//引当金額集計表（確認結果）（レジャーサイズ含む）
	private static final String LIST_TASYA_RISK          = "9";	//他社リスクリスト

	//債権査定基準変更対応
	//追加開始
	private static final String LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI			= "10";	//引当金額集計表(査定結果)(本社用：初回月)
	private static final String LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI		= "11";	//引当金額集計表(査定結果)(本社用：初回月以外)
	private static final String LIST_HIKIATEKIN_KENSHOKEKKA_HONSYA					= "12";	//引当金額集計表(検証結果)(本社用)
	private static final String LIST_TEST_PROCEDURE = "13";

	//帳票種別(国内)
	private static final String CHOHYO_SYUKEISATEI_JA		= "013";
	private static final String CHOHYO_SYUKEISATEI_EN		= "014";
	private static final String CHOHYO_SYUKEIKENSYO_JA	= "015";
	private static final String CHOHYO_SYUKEIKENSYO_EN	= "016";
	//課題No.91
	//追加開始
	private static final String CHOHYO_TAIHI_JA			= "017";
	private static final String CHOHYO_TAIHI_EN			= "018";
	private static final String CHOHYO_SYOKAI_JA			= "019";
	private static final String CHOHYO_SYOKAI_EN			= "0110";
	//追加完了
	//債権査定基準変更対応
	//追加開始
	private static final String CHOHYO_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI			= "0131";		//引当金額集計表(査定結果)(本社用：初回月) 0131
	private static final String CHOHYO_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI		= "0132";		//引当金額集計表(査定結果)(本社用：初回月以外) 0132
	private static final String CHOHYO_HIKIATEKIN_KENSHOKEKKA_HONSYA				= "0133";		//引当金額集計表(検証結果)(本社用) 0133
	//追加完了

	//要素名
	private static final String KANJO_CD  = "kanjo_cd";
    private static final String KANJO_NM  = "kanjo_nm";
    private static final String DUNS_NO   = "duns_no";
    private static final String SATEIKI   = "sateiki";
    private static final String TAISYO_YM = "taisyo_ym";
    private static final String SIME_KBN = "sime_kbn";

    // 課題No.195
    // 追加開始
    private static final String HANYOU1 = "hanyo1";
    // 追加完了

    //マップ取得用キー
	private static final String SYSTEM_KBN = "SYSTEM_KBN";
    //スタイル
	private static final String BGCOLOR_FFC1E0  = "background-color:#FFC1E0;";

	// 課題No.195
	// 追加開始
	private static final String BGCOLOR_FFC1E02  = "background-color:#FFC1E0;width:70px;";
	private static final String BGCOLOR_FFFFFF2  = "background-color:#FFFFFF;width:70px;";
	// 追加完了

	private static final String BGCOLOR_FFFFFF  = "background-color:#FFFFFF;";



	private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private UserBean user_bean;				// ユーザ情報
    private SessionData cmnData = null;		// 機能共通セッション
    private DownloadForm form;					// アクションフォーム

    /**
     * コンストラクタ <br>
     *
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public DownloadBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.form = (DownloadForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得<br>
     *
     * @throws Exception Exception
     */
    public void execute() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DownloadDbAcc dbacc = new DownloadDbAcc(sqlExec, log, appContext);

        // 帳票種別セレクトボックス設定値取得
        dbacc.getList_type(getSystemkbn_all());
        // 最新査定期取得
        dbacc.getSateiki();
        // 半期四半期区分セレクトボックス設定値取得
        dbacc.getHanki_sihanki_kbn();
        // 〆区分セレクトボックス設定値取得
        dbacc.getSime_kbn();
        // 汎用項目タイトル取得
        dbacc.getHanyoTitle();

        // 課題No.159
        // 追加開始
    	// スタイル設定
    	setStyle();
    	// 追加完了
    }

    /**
     * ログインユーザの全参照システム区分取得<br>
     *
     * @throws Exception Exception
     */
    public String getSystemkbn_all() throws Exception {
        HashMap soshiki_all = user_bean.getComSansyososhiki_all();
        String systemkbn_all = (String)soshiki_all.get(SYSTEM_KBN);
        return systemkbn_all;
    }

    /**
     * 帳票種別セレクトボックス変更処理<br>
     *
     * @throws Exception Exception
     */
    public void list_type() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DownloadDbAcc dbacc = new DownloadDbAcc(sqlExec, log, appContext);
    	String list_type = form.getList_type();

    	//設定値初期化
    	initSettei();
    	//帳票種別判定
    	chkListType(list_type);
    	//スタイル設定
    	setStyle();
    	//〆区分表示制御
    	disp_sime_kbn(list_type);
    	//半期四半期区分表示制御
    	disp_hanki_sihanki_kbn(list_type);
    	//帳票システム区分の設定
    	setSerch_system_kbn(list_type);
    	//汎用１セレクトボックス設定値取得
    	dbacc.getHanyo1();
    	//全汎用１の設定
    	sethanyo1_all();
    	//全汎用２_１の設定
    	sethanyo2_1();
    }

    /**
     * 〆区分表示制御<br>
     *
     * @throws Exception Exception
     */
    public void disp_sime_kbn(String list_type) throws Exception {
    	//「引当金額集計表（検証結果）」を選択した場合、〆区分セレクトボックスを表示
    	if(KENSYOKEKKA.equals(list_type) || KENSYOKEKKA_EN.equals(list_type)
			//債権査定基準変更対応
			//追加開始
     		|| HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(list_type)){
			//追加完了
			form.setSime_kbn_hyoji_flg(true);
    	}else{
       		form.setSime_kbn_hyoji_flg(false);
    	}
    }

    /**
     * 半期四半期区分表示制御<br>
     *
     * @throws Exception Exception
     */
    public void disp_hanki_sihanki_kbn(String list_type) throws Exception {
    	//滞留判定関連の帳票を選択した場合、半期四半期区分セレクトボックスを非表示
    	if(TAIRYUMEISAI_HO.equals(list_type) || TAIRYUMEISAI_HO_EN.equals(list_type)
    			|| TAIRYUMEISAI.equals(list_type) || TAIRYUMEISAI_EN.equals(list_type)
    			|| TAIRYUMEISAI_L.equals(list_type) || TAIRYUMEISAI_L_EN.equals(list_type)){
       		form.setHanki_sihanki_kbn_hyoji_flg(false);
    	}else{
       		form.setHanki_sihanki_kbn_hyoji_flg(true);
    	}
    }

    /**
     * 帳票システム区分の設定<br>
     *
     * @throws Exception Exception
     */
    public void setSerch_system_kbn(String list_type) throws Exception {
    	InputCheck check = new InputCheck();
    	StringBuffer systemkbn = new StringBuffer();

    	String sytem_val = GS.EMPTY_CHARCTER;
    	if(!check.isNullBlank(list_type)){
    		sytem_val = list_type.substring(0,2);
    	}
    	//他社リスクリストの場合、MTSとFOCUSを両方設定
    	if(TASYA_RISK.equals(sytem_val)){
    		systemkbn.append(GS.SINGLE_QUOTATION)
    				 .append(GS.MTS)
    				 .append(GS.SINGLE_QUOTATION)
    				 .append(GS.COMMA)
    				 .append(GS.SINGLE_QUOTATION)
    				 .append(GS.FOCUS)
    				 .append(GS.SINGLE_QUOTATION);
    	}else{
    		systemkbn.append(GS.SINGLE_QUOTATION)
			 		 .append(sytem_val)
			 		 .append(GS.SINGLE_QUOTATION);
    	}
    	form.setSearch_system_kbn(systemkbn.toString());
    }

    /**
     * 全汎用１の設定<br>
     *
     * @throws Exception Exception
     */
    public void sethanyo1_all() throws Exception {
    	List list_hanyo1 = form.getList_hanyo1_all();
    	StringBuffer hanyo1_all = new StringBuffer();
    	for (int i = 0; i < list_hanyo1.size(); i++) {
        	if (i == 0) {
        		hanyo1_all.append(GS.SINGLE_QUOTATION)
        				  .append(list_hanyo1.get(i))
        				  .append(GS.SINGLE_QUOTATION);
        	} else {
        		hanyo1_all.append(GS.COMMA)
        				  .append(GS.SINGLE_QUOTATION)
        				  .append(list_hanyo1.get(i))
        				  .append(GS.SINGLE_QUOTATION);
        	}
        }
        form.setHanyo1_all(hanyo1_all.toString());
    }

    /**
     * 全汎用２_１の設定<br>
     *
     * @throws Exception Exception
     */
    public void sethanyo2_1() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DownloadDbAcc dbacc = new DownloadDbAcc(sqlExec, log, appContext);

        List list_hanyo1 = form.getList_hanyo1_all();
    	StringBuffer hanyo2_1 = new StringBuffer(GS.EMPTY_CHARCTER);
        for (int i = 0; i < list_hanyo1.size(); i++) {
    		// 分類２取得
    		dbacc.getHanyo2_1((String)list_hanyo1.get(i));
            // 取得した分類２をシングルコートで括り、カンマ区切りで設定
            for (int j = 0; j < form.getTemp_hanyo2().size(); j++) {
            	if (GS.EMPTY_CHARCTER.equals(hanyo2_1.toString())) {
            		hanyo2_1.append(GS.SINGLE_QUOTATION)
                			.append(form.getTemp_hanyo2().get(j))
                			 .append(GS.SINGLE_QUOTATION);
               	} else {
               		hanyo2_1.append(GS.COMMA)
               	   		    .append(GS.SINGLE_QUOTATION)
               				.append(form.getTemp_hanyo2().get(j))
               				.append(GS.SINGLE_QUOTATION);
               	}
            }
        }
        form.setHanyo2_1(hanyo2_1.toString());
    }

    /**
     * 汎用１セレクトボックス変更処理<br>
     *
     * @throws Exception Exception
     */
    public void hanyo1() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DownloadDbAcc dbacc = new DownloadDbAcc(sqlExec, log, appContext);

    	//汎用２セレクトボックス設定値取得
    	dbacc.getHanyo2_2();

    	StringBuffer hanyo2_2 = new StringBuffer(GS.EMPTY_CHARCTER);
        // 取得した分類２をシングルコートで括り、カンマ区切りで設定
        for (int i = 0; i < form.getTemp_hanyo2().size(); i++) {
        	if (GS.EMPTY_CHARCTER.equals(hanyo2_2.toString())) {
        		hanyo2_2.append(GS.SINGLE_QUOTATION)
            			.append(form.getTemp_hanyo2().get(i))
            			 .append(GS.SINGLE_QUOTATION);
           	} else {
           		hanyo2_2.append(GS.COMMA)
           	   		    .append(GS.SINGLE_QUOTATION)
           				.append(form.getTemp_hanyo2().get(i))
           				.append(GS.SINGLE_QUOTATION);
           	}
        }
        form.setHanyo2_2(hanyo2_2.toString());
    }

    /**
     * ダウンロード処理<br>
     *
     * @throws Exception Exception
     */
    public String download() throws Exception {
		String rtnFwd = null;
    	String selected_list = form.getSelectedlist();

    	//検索条件の設定
    	setSearchItem();

    	if(LIST_TAIRYUMEISAI.equals(selected_list)){
    		//LC1101_債権明細一覧
    		app.print.bss.SaikenMeisaiExcelBss bss = new app.print.bss.SaikenMeisaiExcelBss(appContext);
    		bss.execute();
    	}else if(LIST_SATEIKEKKA.equals(selected_list)){
    		//LD2101_引当金額集計表(期中)
    		app.print.bss.SateiKekkaExcelBss bss = new app.print.bss.SateiKekkaExcelBss(appContext);
    		bss.execute();
    	}else if(LIST_KAKUNINKEKKA.equals(selected_list)){
    		//LD2101_引当金額集計表(期末)
    		app.print.bss.KakuninKekkaExcelBss bss = new app.print.bss.KakuninKekkaExcelBss(appContext);
    		bss.execute();
    	}else if(LIST_TASYA_RISK.equals(selected_list)){
    		//LE1101_他社リスクリスト
    		app.print.bss.Other_riskBss bss = new app.print.bss.Other_riskBss(appContext);
    		bss.execute();
    	}else if(CHOHYO_SYUKEISATEI_JA.equals(form.getList_type()) || CHOHYO_SYUKEISATEI_EN.equals(form.getList_type())){
    		//国内帳票_引当金額集計表(期中)
    		app.print.bss.SyukeiExcelBss bss = new app.print.bss.SyukeiExcelBss(appContext);
    		bss.execute();
    	}else if(CHOHYO_SYUKEIKENSYO_JA.equals(form.getList_type()) || CHOHYO_SYUKEIKENSYO_EN.equals(form.getList_type())){
    		//国内帳票_引当金額集計表(期末)
    		app.print.bss.KensyokekkaExcelBss bss = new app.print.bss.KensyokekkaExcelBss(appContext);
    		bss.execute();

    	//課題No.91
    	//追加開始

    	}else if(CHOHYO_TAIHI_JA.equals(form.getList_type()) || CHOHYO_TAIHI_EN.equals(form.getList_type())){
    		//国内帳票_BS対比表
    		app.print.bss.BSTaihiExcelBss bss = new app.print.bss.BSTaihiExcelBss(appContext);
    		bss.execute();
    	}else if(CHOHYO_SYOKAI_JA.equals(form.getList_type()) || CHOHYO_SYOKAI_EN.equals(form.getList_type())){
    		//国内帳票_BS照会表
    		app.print.bss.BSSyokaiExceBss bss = new app.print.bss.BSSyokaiExceBss(appContext);
    		bss.execute();
    	//追加完了

    	// 課題No.206
    	}else if(TAIRYUMEISAI_HO.equals(form.getList_type()) || TAIRYUMEISAI_HO_EN.equals(form.getList_type())){
    		//国内帳票_滞留判定明細
    		app.print.bss.TairyuSaikenExcelBss bss = new app.print.bss.TairyuSaikenExcelBss(appContext);
    		bss.execute();
    	//追加完了

 		//債権査定基準変更対応
 		//追加開始

     	}else if(CHOHYO_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(form.getList_type())){
    		//引当金額集計表(査定結果)(本社用：初回月)
    		app.print.bss.SateikekkaHonsyaSyokaiExcelBss bss = new app.print.bss.SateikekkaHonsyaSyokaiExcelBss(appContext);
    		bss.execute();
       	}else if(CHOHYO_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(form.getList_type())){
    		//引当金額集計表(査定結果)(本社用：初回月以外)
    		app.print.bss.SateikekkaHonsyaSyokaiIgaiExcelBss bss = new app.print.bss.SateikekkaHonsyaSyokaiIgaiExcelBss(appContext);
    		bss.execute();
       	}else if(CHOHYO_HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(form.getList_type())){
    		//引当金額集計表(検証結果)(本社用)
    		app.print.bss.KenshokekkaHonsyaExcelBss bss = new app.print.bss.KenshokekkaHonsyaExcelBss(appContext);
    		bss.execute();
		//追加完了
		}

 		//エラーメッセージが存在する場合は当画面にリターン
		if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			rtnFwd = appContext.getActionForm().toString();
		}

    	return rtnFwd;
    }

    /**
     * 設定値初期化<br>
     *
     * @throws Exception Exception
     */
    public void initSettei() throws Exception {
    	form.setAr_hanyo1(new LinkedHashMap());
    	form.setAr_hanyo2(new LinkedHashMap());
    	form.setHanyo1(GS.EMPTY_CHARCTER);
    	form.setHanyo2(GS.EMPTY_CHARCTER);
    }

    /**
     * 帳票種別判定<br>
     *
     * @throws Exception Exception
     */
    public void chkListType(String list_type) throws Exception {
    	//帳票種別判定
    	if(TAIRYUMEISAI_HO.equals(list_type) || TAIRYUMEISAI_HO_EN.equals(list_type)){
    		form.setSelectedlist(LIST_TAIRYUMEISAI_HO);
    	}else if(SATEIKEKKA_HO.equals(list_type) || SATEIKEKKA_HO_EN.equals(list_type)
    			//債権査定基準変更対応
    			//追加開始
    			|| HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(list_type)				//引当金額集計表(査定結果)(本社用：初回月)
    			|| HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(list_type)){			//引当金額集計表(査定結果)(初回月以外追加)
				//追加完了
    		form.setSelectedlist(LIST_SATEIKEKKA_HO);
    	}else if(KENSYOKEKKA.equals(list_type) || KENSYOKEKKA_EN.equals(list_type)
    			//債権査定基準変更対応
    			//追加開始
    			|| HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(list_type)){					//引当金額集計表(検証結果)(本社用)
				//追加完了
    		form.setSelectedlist(LIST_KENSYOKEKKA);
    	}else if(BS_TAIHI.equals(list_type) || BS_TAIHI_EN.equals(list_type)){
    		form.setSelectedlist(LIST_BS_TAIHI);
    	}else if(BS_SYOKAI.equals(list_type) || BS_SYOKAI_EN.equals(list_type)){
    		form.setSelectedlist(LIST_BS_SYOKAI);
    	}else if(TAIRYUMEISAI.equals(list_type) || TAIRYUMEISAI_EN.equals(list_type)
    			|| TAIRYUMEISAI_L.equals(list_type) || TAIRYUMEISAI_L_EN.equals(list_type)){
    		form.setSelectedlist(LIST_TAIRYUMEISAI);
    	}else if(SATEIKEKKA.equals(list_type) || SATEIKEKKA_EN.equals(list_type)
    			|| SATEIKEKKA_L.equals(list_type) || SATEIKEKKA_L_EN.equals(list_type)){
    		form.setSelectedlist(LIST_SATEIKEKKA);
    	}else if(KAKUNINKEKKA.equals(list_type) || KAKUNINKEKKA_EN.equals(list_type)
    			|| KAKUNINKEKKA_L.equals(list_type) || KAKUNINKEKKA_L_EN.equals(list_type)){
    		form.setSelectedlist(LIST_KAKUNINKEKKA);
    	}else if(TASYA_RISK.equals(list_type)){
    		form.setSelectedlist(LIST_TASYA_RISK);
	//追加完了

    	}else{
    		form.setSelectedlist(GS.EMPTY_CHARCTER);
    	}
    	//出力言語タイプ設定
    	if(TAIRYUMEISAI_HO.equals(list_type) || SATEIKEKKA_HO.equals(list_type) || KENSYOKEKKA.equals(list_type)
    			|| BS_TAIHI.equals(list_type) || BS_SYOKAI.equals(list_type) || TAIRYUMEISAI.equals(list_type)
    			|| TAIRYUMEISAI_L.equals(list_type) || SATEIKEKKA.equals(list_type) || SATEIKEKKA_L.equals(list_type)
    			|| KAKUNINKEKKA.equals(list_type) || KAKUNINKEKKA_L.equals(list_type) || TASYA_RISK.equals(list_type)
    			//債権査定基準変更対応
    			//追加開始
    			|| HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(list_type)		//引当金額集計表(査定結果)(本社用：初回月)
    			|| HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(list_type)	//引当金額集計表(査定結果)(初回月以外追加)
    			|| HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(list_type)){			//引当金額集計表(検証結果)(本社用)
				//追加完了
    		form.setLangMode(GS.LANG_JA);
    	}else{
    		form.setLangMode(GS.LANG_EN);
    	}
    }

    /**
     * スタイル設定<br>
     *
     * @throws Exception Exception
     */
    public void setStyle() throws Exception {
    	String selectedList = form.getSelectedlist();
		form.setBgcolorSateiki(BGCOLOR_FFFFFF);
		form.setBgcolorTaisyo_ym(BGCOLOR_FFFFFF);
    	// 課題No.195
    	// 追加開始
		form.setBgcolorHanyou1(BGCOLOR_FFFFFF2);
		// 追加完了

    	if(LIST_TAIRYUMEISAI_HO.equals(selectedList) || LIST_TAIRYUMEISAI.equals(selectedList)
    			|| LIST_KENSYOKEKKA.equals(selectedList) || LIST_KAKUNINKEKKA.equals(selectedList)
    			|| LIST_TASYA_RISK.equals(selectedList)
    			//課題No.91
	   			//追加開始
    			|| LIST_BS_TAIHI.equals(selectedList) || LIST_BS_SYOKAI.equals(selectedList)
    		//	{
    			//追加完了
    			//債権査定基準変更対応
    			//追加開始
     			|| LIST_HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(selectedList))
   				{
   				//追加完了
    		form.setBgcolorSateiki(BGCOLOR_FFC1E0);
    	}else if(LIST_SATEIKEKKA_HO.equals(selectedList) || LIST_SATEIKEKKA.equals(selectedList)
//    			|| LIST_BS_TAIHI.equals(selectedList) || LIST_BS_SYOKAI.equals(selectedList)
    			//債権査定基準変更対応
    			//追加開始
     			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(selectedList)
     			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(selectedList)){
     			//追加完了

    		form.setBgcolorTaisyo_ym(BGCOLOR_FFC1E0);
    	}

    	// 課題No.195
    	// 追加開始
    	if(LIST_TAIRYUMEISAI_HO.equals(selectedList) || LIST_SATEIKEKKA_HO.equals(selectedList)
    			|| LIST_KENSYOKEKKA.equals(selectedList) || LIST_BS_TAIHI.equals(selectedList)
    			|| LIST_BS_SYOKAI.equals(selectedList)
    			//債権査定基準変更対応
    			//追加開始
      			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(selectedList)
     			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(selectedList)
     			|| LIST_HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(selectedList)
     			//追加完了
    	){
      		form.setBgcolorHanyou1(BGCOLOR_FFC1E02);
    	}
    	// 追加完了
    }

    /**
     * 入力チェック<br>
     *
     * @throws Exception Exception
     */
    public boolean chkNyuryoku() throws Exception {
		boolean rs = true;
    	String selected_list = form.getSelectedlist();
		if(!isRequiredChk(selected_list)){
			//必須入力チェック
			rs = false;
		}else if(!isRelateChk(selected_list)){
			//関連チェック
			rs = false;
		}else if(!isByteChk()){
			//入力バイトチェック
			rs = false;
		}else if(!isKinshiMojiChk()){
			//入力禁止文字入力チェック
			rs = false;
		}else if(!isNumChk()){
			//数値チェック
			rs = false;
		}
		return rs;
    }

    /**
     * 必須入力チェック<br>
     *
     * @throws Exception Exception
     */
    public boolean isRequiredChk(String selected_list) throws Exception {
		boolean rs = false;
		//帳票種別
    	if(GS.EMPTY_CHARCTER.equals(selected_list)){
    		appContext.setMsgCode(GL.ERR_SELECT,GL.OS8101_LIST_TYPE);
    	}else{
    		rs = true;
    	}
		return rs;
    }

    /**
     * 関連チェック<br>
     *
     * @throws Exception Exception
     */
    public boolean isRelateChk(String selected_list) throws Exception {
    	InputCheck check = new InputCheck();
    	if(LIST_TAIRYUMEISAI_HO.equals(selected_list) || LIST_TAIRYUMEISAI.equals(selected_list)
    			|| LIST_KENSYOKEKKA.equals(selected_list) || LIST_KAKUNINKEKKA.equals(selected_list)
    			|| LIST_TASYA_RISK.equals(selected_list)

    			//課題No.91
    			//追加開始
    			|| LIST_BS_TAIHI.equals(selected_list) || LIST_BS_SYOKAI.equals(selected_list)
    			//追加完了

    			//債権査定基準変更対応
    			//追加開始
     			|| LIST_HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(selected_list)
     			//追加完了
    			){

    		//査定期
    		if(check.isNullBlank(form.getSateiki())){
        		appContext.setMsgCode(GL.ERR_INPUT,GL.COMMON_ASSESSING_PERIOD);
    			appContext.setFocusField(SATEIKI);
        		return false;
    		}
    	}
    	if(LIST_SATEIKEKKA_HO.equals(selected_list)	|| LIST_SATEIKEKKA.equals(selected_list)

    			//課題No91
    			//追加開始
//    			|| LIST_BS_TAIHI.equals(selected_list) || LIST_BS_SYOKAI.equals(selected_list)
    			//追加完了

    			//債権査定基準変更対応
				//追加開始
      			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(selected_list)
     			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(selected_list)
				//追加完了
    			){

    		//対象年月
    		if(check.isNullBlank(form.getTaisyo_ym())){
        		appContext.setMsgCode(GL.ERR_INPUT,GL.OS8101_TAISYO_YM);
    			appContext.setFocusField(TAISYO_YM);
        		return false;
    		}
    	}
    	if(LIST_KENSYOKEKKA.equals(selected_list)){
    		//〆区分
    		if(check.isNullBlank(form.getSime_kbn())){
        		appContext.setMsgCode(GL.ERR_SELECT,GL.REPLACE_SIMEKBN);
    			appContext.setFocusField(SIME_KBN);
        		return false;
    		}
    	}

    	// 課題No.195
    	// 追加開始
    	if(LIST_TAIRYUMEISAI_HO.equals(selected_list) || LIST_SATEIKEKKA_HO.equals(selected_list)
    			|| LIST_KENSYOKEKKA.equals(selected_list) || LIST_BS_TAIHI.equals(selected_list)
    			|| LIST_BS_SYOKAI.equals(selected_list)
    			//債権査定基準変更対応
				//追加開始
      			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAI.equals(selected_list)
     			|| LIST_HIKIATEKIN_SATEIKEKKA_HONSYA_SYOKAIIGAI.equals(selected_list)
     			|| LIST_HIKIATEKIN_KENSHOKEKKA_HONSYA.equals(selected_list)
    			){
    			//追加完了
     		//汎用２
    		if(check.isNullBlank(form.getHanyo1())){
        		appContext.setMsgCode(GL.ERR_SELECT,form.getHanyou1Title());
    			appContext.setFocusField(HANYOU1);
        		return false;
    		}
    	}
    	// 追加完了

    	return true;
    }

    /**
     * 入力バイトチェック<br>
     *
     * @throws Exception Exception
     */
    public boolean isByteChk() throws Exception {
    	InputCheck check = new InputCheck();
		boolean rs = false;

		if(!(check.islength(form.getKanjo_cd(),12))){
			//勘定先CD
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OS8101_KANJO_CD);
			appContext.setFocusField(KANJO_CD);
		}else if(!(check.islength(form.getKanjo_nm(),120))) {
			//勘定先名称
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OS8101_KANJO_NM);
			appContext.setFocusField(KANJO_NM);
		}else if(!(check.islength(form.getDuns_no(),9))) {
			//DUNS_NO.
			appContext.setMsgCode(GL.ERR_LENGTH,GL.OS8101_DUNS_NO);
			appContext.setFocusField(DUNS_NO);
		}else{
			rs = true;
		}
    	return rs;
    }

    /**
     * 【入力禁止文字入力チェック処理】 <br>
     * @return boolean
     */
    private boolean isKinshiMojiChk() throws Exception{
    	InputCheck check = new InputCheck();
		String kinshiChar = GS.EMPTY_CHARCTER;
    	String yousoNm = null;
    	boolean rs = true;

		if(!GS.EMPTY_CHARCTER.equals(form.getKanjo_cd())){
			//勘定先CD
			for (int i = 0; i < form.getKanjo_cd().length(); i++) {
				kinshiChar = form.getKanjo_cd().substring(i,i + 1);
				if(check.haveKinshiMoji(kinshiChar)) {
					//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
					appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
			    	rs = false;
					break;
				}
			}
	    	yousoNm = KANJO_CD;
		}
		if(rs == true && !GS.EMPTY_CHARCTER.equals(form.getKanjo_nm())){
			//勘定先名称
			for (int i = 0; i < form.getKanjo_nm().length(); i++) {
				kinshiChar = form.getKanjo_nm().substring(i,i + 1);
				if(check.haveKinshiMoji(kinshiChar)) {
					//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
					appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
			    	rs = false;
					break;
				}
			}
	    	yousoNm = KANJO_NM;
		}
		if(rs == true && !GS.EMPTY_CHARCTER.equals(form.getDuns_no())){
			//DUNS_NO.
			for (int i = 0; i < form.getDuns_no().length(); i++) {
				kinshiChar = form.getDuns_no().substring(i,i + 1);
				if(check.haveKinshiMoji(kinshiChar)) {
					//チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示
					appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
			    	rs = false;
					break;
				}
			}
	    	yousoNm = DUNS_NO;
		}
		if(rs == false){
			appContext.setFocusField(yousoNm);
		}
    	return rs;
    }

    /**
     * 【数値チェック処理】 <br>
     * @return boolean
     */
    private boolean isNumChk() throws Exception{
    	InputCheck check = new InputCheck();
		List<String> msglist = new ArrayList<String>();

		String sateiki = form.getSateiki();
		String taisyo_ym = form.getTaisyo_ym();
		String yousoNm = null;
    	String GL_LABEL = null;
    	boolean rs = true;
   		if(!check.isNullBlank(sateiki) && (check.lenB(sateiki) != 6 || !check.isNumeric(sateiki))){
   	    	//査定期
			GL_LABEL = GL.COMMON_ASSESSING_PERIOD;
			yousoNm = SATEIKI;
			rs = false;
   		}else if(!check.isNullBlank(taisyo_ym) && (check.lenB(taisyo_ym) != 6 || !check.isNumeric(taisyo_ym))){
   	    	//対象年月
			GL_LABEL = GL.OS8101_TAISYO_YM;
			yousoNm = TAISYO_YM;
			rs = false;
   		}
		msglist.add(GL.ERR_DIGITS2);
		msglist.add(GL_LABEL);
		msglist.add(GL.REPLACE_6);

		if(rs == false){
			appContext.setMsgCode(msglist);
			appContext.setFocusField(yousoNm);
		}
		return rs;
    }

    /**
     * 【検索条件設定】 <br>
     * @return boolean
     */
    private void setSearchItem() throws Exception{
    	InputCheck check = new InputCheck();
    	String hanyo1 = form.getHanyo1();
    	String hanyo2 = form.getHanyo2();
    	String sateiki = form.getSateiki();
    	String taisyo_ym = form.getTaisyo_ym();

    	//検索用組織の設定
    	if(check.isNullBlank(hanyo1)){
    		form.setSearch_sateikaisya_cd(form.getHanyo1_all());
    		form.setSearch_bunrui2(form.getHanyo2_1());
    	}else if(check.isNullBlank(hanyo2)){
    		form.setSearch_sateikaisya_cd(GS.SINGLE_QUOTATION + hanyo1 + GS.SINGLE_QUOTATION);
    		form.setSearch_bunrui2(form.getHanyo2_2());
    	}else{
    		form.setSearch_sateikaisya_cd(GS.SINGLE_QUOTATION + hanyo1 + GS.SINGLE_QUOTATION);
    		form.setSearch_bunrui2(GS.SINGLE_QUOTATION + hanyo2 + GS.SINGLE_QUOTATION);
    	}


    	//検索用査定期、対象年月の設定
    	if(GS.LANG_EN.equals(cmnData.getComLangMode())){
    		if(!check.isNullBlank(sateiki)){
    			sateiki = sateiki.substring(2) + sateiki.substring(0,2);
    		}
    		if(!check.isNullBlank(taisyo_ym)){
    			taisyo_ym = taisyo_ym.substring(2) + taisyo_ym.substring(0,2);
    		}
    	}
   		form.setSearch_sateiki(sateiki);
   		form.setSearch_taisyo_ym(taisyo_ym);
    }
}