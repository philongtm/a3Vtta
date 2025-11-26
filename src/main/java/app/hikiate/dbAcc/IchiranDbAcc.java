/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/25		SSC				課題No.163 代行者表示修正 
003		2016/03/28		SSC				BJ201602002_部門廃止対応（一次）_本部絞込対応 
******************************************************************************/
package app.hikiate.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.form.IchiranForm;
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
* OD1101_引当金確認_対象先一覧 DBアクセスクラス
*/
public class IchiranDbAcc extends CommonDbAcc {
    private SessionData cmnData = null;             // 機能共通セッション
    private UserBean user_bean = null;                  // ユーザ情報
    private IchiranForm form = null;                    // アクションフォーム
    private AppContext appContext = null;               // ＡＰＰコンテキスト

    //Resultset用文字列 
    private static final String COUNT                = "cnt";   
    private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
    private static final String KBN_VAL              = "kbn_val";
    private static final String SATEIKI              = "satei_ki";
    private static final String CNT                  = "cnt";

    // 課題No.163
    // 追加開始
	private static final String DAIKO					= "( 代行 ";
	private static final String DAIKO_EN				= "( proxy ";
	private static final String DAIKOUSHA				= "daikousha";
    // 追加完了
    
    // 査定案件No.
    private static final String ANKEN_NO             ="anken_no";
    // 基幹取引先コード
    private static final String KIKAN_TORI_CD        ="kikan_tori_cd";
    // 査定会社コード
    private static final String SATEI_KAISHA_CD      ="satei_kaisha_cd";
    // 初期分類２
    private static final String INIT_BUNRUI2         ="init_bunrui2";
    // 初期分類３
    private static final String INIT_BUNRUI3         ="init_bunrui3";
    // 初期部コード
    private static final String INIT_BU_CD           ="init_bu_cd";
    // フェーズ
    private static final String PHASE                ="phase";
    // ステータス
    private static final String STATUS               ="status";
    // 年月
    private static final String TAISYO_YM            ="taisyo_ym";
    // 差戻転送FLG
    private static final String SASI_TEN_FLG         ="sasi_ten_flg";
    // 案件保持ユーザID
    private static final String HOJI_USER_ID         ="hoji_user_id";
    // 基幹システム区分
    private static final String SYSTEM_KBN           ="system_kbn";
    // 基準日区分
    private static final String KIJUNBI_KBN          ="kijunbi_kbn";
    // 処理回数
    private static final String SYORI_KAISU          ="syori_kaisu";
    // 統合取引先コード
    private static final String TOGO_TORI_CD         ="togo_tori_cd";
    // 識別コード
    private static final String SHIKIBETU_CD         ="shikibetu_cd";
    // 取引先名
    private static final String BUSINESS_NM          ="business_nm";
    // 所在地
    private static final String SYOZAICHI             ="syozaichi";
    // ワールドベース国名称
    private static final String WB_COUNTRY_NM        ="wb_country_nm";
    // ワールドベース国コード
    private static final String WB_COUNTRY_CD        ="wb_country_cd";
    // 外部格付
    private static final String GAIBU_KTK            ="gaibu_ktk";
    // 格付機関
    private static final String KTK_KIKAN            ="ktk_kikan";
    // FSS
    private static final String FSS                  ="fss";
    // DUNS Rating
    private static final String DUNS_RATING          ="duns_rating";
    // 信用格付情報
    private static final String KTK                  ="ktk";
    // 親会社信用格付情報
    private static final String OYA_KTK              ="oya_ktk";
    // 親会社一体独立
    private static final String OYA_ITTAI_DOKURITU   ="oya_ittai_dokuritu";
    // 親会社取引先コード
    private static final String OYA_DUNS_NO          ="oya_duns_no";
    // 担当者
    private static final String TANTO_NM             ="tanto_nm";
    // 代行ユーザID
    private static final String DAIKO_USER_ID        ="daiko_user_id";
    // 初期分類２名称
    private static final String INIT_BUNRUI2_NM      ="init_bunrui2_nm";
    // 初期分類３名称
    private static final String INIT_BUNRUI3_NM      ="init_bunrui3_nm";
    // 組織
    private static final String SOSHIKI              ="soshiki";
    // 初期部名称
    private static final String INIT_BU_NM           ="init_bu_nm";
    // 金額
    private static final String SAIKEN_KINGAKU       ="saiken_kingaku";
    // 通貨コード
    private static final String TUUKA_CD             ="tuuka_cd";
    // 進捗
    private static final String SHINCHOKU            ="shinchoku";
    // 表示用年月
    private static final String TAISYO_YM_HYOJI      ="taisyo_ym_hyoji";
    // 会社コード
    private static final String KAISHA_CD            ="kaisha_cd";
    // 部コード
    private static final String BU_CD                ="bu_cd"; 
    // 半期四半期区分
    private static final String HANKI_SIHANKI_KBN    ="hanki_sihanki_kbn";
    // 分類１
    private static final String BUNRUI2              ="bunrui2";
    // 査定期
    private static final String SATEI_KI             ="satei_ki";
    // 親会社名称
    private static final String OYA_BUSINESS_NM      ="oya_business_nm";
    // 店コード
    private static final String MISE_CD              ="mise_cd";
    
    
    private static final String JITANTO         = "1";          //自担当分
    private static final String HANYO2          = "2";          //汎用２
    private static final String SORT_ITEM       = "sort_OD1101";    //区分キー（ソート項目）
    private static final String SORT_ORDER      = "sort_order"; //区分キー（整列方向）
    private static final String SHOW            = "show";           //区分キー（表示件数）
    private static final String HS_KBN_HANTEI   = "2";				// 判定査定区分 '2'： 一次・二次査定
    private static final String MOGITORI        = "30";         //入力区分(30:もぎ取り)
    private static final String HANKI_SIHANKI_KBN_2      = "2";						// 半期四半期区分:第1/3四半期：2
    private static final String IN_PHASE           = "'40','50','60','70','80'";
    
    private static final String NUM_FMT_KOKUNAI = "##,###,###,###,###,##0.00";      // 数字のフォーマット：海外
    private static final String NUM_FMT_KAIGAI  = "##,###,###,###,###,##0";      	   // 数字のフォーマット：国内
    
    private static final String SP_SS_O_SELECT_T2600           = "SP_SS_O_SELECT_T2600";          //査定期取得用プロシージャ
    private static final String SP_SS_O_SELECT_T1400                    = "SP_SS_O_SELECT_T1400";            //自担当分/汎用２ラジオボタン初期判定用プロシージャ
    private static final String SP_SS_O_SELECT_T1401    = "SP_SS_O_SELECT_T1401";         //進捗件数取得プロシージャ
    private static final String SP_SS_OD1101_SELECT_ICHIRAN     = "SP_SS_OD1101_SELECT_ICHIRAN";    //滞留判定一覧情報取得プロシージャ
    private static final String SP_SS_O_SELECT_T1402     = "SP_SS_O_SELECT_T1402";    //もぎ取りチェック処理プロシージャ
    private static final String SP_SS_O_UPDATE_T1401   = "SP_SS_O_UPDATE_T1401";  //もぎ取り処理プロシージャ
	private static final String SP_SS_O_INSERT_T1300       	= "SP_SS_O_INSERT_T1300";			//入力履歴の登録を行う
    // INパラメータ
    private String userId;              // ユーザＩＤ
    private String workflowSateikaisya_cd;       // ユーザ情報.業務フローパターン査定会社コード
    private String sansyoBunrui2;       // 参照分類２コード
    private String workflowSystemkbn;   // 業務フローパターンシステム区分
    private String workflowsateikaisya_cd;	// ユーザ情報.業務フローパターン査定会社コード
    private String sansyoHonbuCd;       // 参照本部コード

    /**
     * コンストラクタ
     * 
     * @param SqlExecuter
     * @param Log
     * @param AppContext
     */
    public IchiranDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
        super(sqlExec, log);
        this.appContext = appcontext;

        //ビーン取得
        cmnData = appContext.getCMN();
        user_bean = cmnData.getUser_bean();
        form = (IchiranForm)appContext.getActionForm();

        //ビーンの値を変数に設定
        userId = user_bean.getComUserId();
        workflowSateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
        sansyoBunrui2 = user_bean.getComSansyoBunrui2();
        workflowSystemkbn = user_bean.getComWorkflowSystemkbn();
        workflowsateikaisya_cd = user_bean.getComWorkflowSateikaisya_cd();
        sansyoHonbuCd = user_bean.getComSansyoHonbuCd();
    }
    
    /**
     * 変数初期化
     */
    public void initialize() {
        // INパラメータ
        userId = GS.EMPTY_CHARCTER;
        workflowSateikaisya_cd = GS.EMPTY_CHARCTER;
        sansyoBunrui2 = GS.EMPTY_CHARCTER;
        workflowSystemkbn = GS.EMPTY_CHARCTER;
        sansyoHonbuCd = GS.EMPTY_CHARCTER;
    }

    /**
     * ソート順セレクトボックス設定値取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getSort() throws SQLException {

        ResultSet rs = null;

        //ソート項目取得
        try{
            //ResultSet取得
            rs = getKbnval(SORT_ITEM,workflowSystemkbn,cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_sort_item = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
                //初期設定
                if(i==0){
                    form.setSort_item(rs.getString(KBN_VAL));
                }
                ar_sort_item.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                   
                i++;
            }
            form.setAr_sort_item(ar_sort_item);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
        
        rs = null;
        
        //整列方向取得
        try{
            //ResultSet取得
            rs = getKbnval(SORT_ORDER,workflowSystemkbn,cmnData.getComLangMode());

            // ActionForm に取得値を格納
            LinkedHashMap<String,String> ar_sort_order = new LinkedHashMap<String,String>();
            int i = 0;
            while ( rs.next() ) {
                //初期設定
                if(i==0){
                    form.setSort_order(rs.getString(KBN_VAL));
                }
                ar_sort_order.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));                  
                i++;
            }
            form.setAr_sort_order(ar_sort_order);           
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
                //Resultset close
                rs.close();
            }
        }
    }   
    
    /**
     * 査定期取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getSateiki() throws SQLException {

        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T2600, sqlExec);
        exCstmt.setStringIn(cmnData.getComLangMode());
        exCstmt.setStringIn(workflowSateikaisya_cd);
        exCstmt.setStringIn(sansyoBunrui2);
        exCstmt.setStringIn(workflowSystemkbn);
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
                    form.setSateiki(rs.getString(SATEIKI));
                }
                ar_sateiki.put(Function.insertYmSlash(rs.getString(SATEIKI),cmnData.getComLangMode()),rs.getString(SATEIKI));                   
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
     * 自担当分/汎用２ラジオボタン初期判定処理 <br>
     * 
     * @exception SQLException
     */
    public void getInitTanto() throws SQLException {
        
        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1400, sqlExec);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(form.getSateiki());
        exCstmt.setStringIn(workflowSystemkbn);
        exCstmt.setStringIn(workflowsateikaisya_cd);
        exCstmt.setStringIn(form.getSansyo_phase());
        exCstmt.setIntOut(COUNT);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
        
            //自担当分/汎用２ラジオボタン初期値設定
            if(exCstmt.getInt(COUNT) > 0) {
                form.setTanto(JITANTO);
            }else{
                form.setTanto(HANYO2);
            }
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }
        }
    }

    /**
     * 各進捗件数取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getSintyoku() throws SQLException {
        
        //各進捗件数を初期化する
        form.setIchiji_misyori(0);
        form.setIchiji_syorityu(0);
        form.setIchiji_syoninmati(0);
        form.setIchiji_kanryo(0);
        form.setKensyo_misyori(0);
        form.setKensyo_syorityu(0);
        form.setKensyo_syoninmati(0);
        form.setKensyo_kanryo(0);
        form.setNiji_misyori(0);
        form.setNiji_syorityu(0);
        form.setNiji_syoninmati(0);
        form.setNiji_kanryo(0);
        form.setKakunin_misyori(0);
        form.setKakunin_syorityu(0);
        form.setKakunin_syoninmati(0);
        form.setKakunin_kanryo(0);
        
        ResultSet rs = null;
        //ExCallableStatement生成
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1401, sqlExec);
        exCstmt.setStringIn(form.getSateiki());
        exCstmt.setStringIn(workflowSystemkbn);
        exCstmt.setStringIn(workflowSateikaisya_cd);
        exCstmt.setStringIn(sansyoBunrui2);
        exCstmt.setStringIn(IN_PHASE);
        exCstmt.setResultSet(RESULTSET);
        
        try {
            //SQL実行 
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);       
            
            // ActionForm に取得値を格納
            while(rs.next()) {
                if (GS.PHASE_ICHIJI_SATEI.equals(rs.getString(PHASE))) {
                    if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
                        //一次査定未処理件数を設定
                        form.setIchiji_misyori(rs.getInt(CNT));
                    } else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
                        //一次査定処理中件数を設定
                        form.setIchiji_syorityu(rs.getInt(CNT));
                    } else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
                        //一次査定承認待ち件数を設定
                        form.setIchiji_syoninmati(rs.getInt(CNT));
                    } else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
                        //一次査定完了件数を設定
                        form.setIchiji_kanryo(rs.getInt(CNT));
                    }   
                } else if (GS.PHASE_ICHIJI_SATEI_KENSYO.equals(rs.getString(PHASE))) {
                    if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
                        //一次査定検証未処理件数を設定
                        form.setKensyo_misyori(rs.getInt(CNT));
                    } else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
                        //一次査定検証処理中件数を設定
                        form.setKensyo_syorityu(rs.getInt(CNT));
                    } else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
                        //一次査定検証承認待ち件数を設定
                        form.setKensyo_syoninmati(rs.getInt(CNT));
                    } else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
                        //一次査定検証完了件数を設定
                        form.setKensyo_kanryo(rs.getInt(CNT));
                    }                   
                } else if (GS.PHASE_NIJI_SATEI.equals(rs.getString(PHASE))) {
                    if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
                        //二次査定未処理件数を設定
                        form.setNiji_misyori(rs.getInt(CNT));
                    } else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
                        //二次査定処理中件数を設定
                        form.setNiji_syorityu(rs.getInt(CNT));
                    } else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
                        //二次査定承認待ち件数を設定
                        form.setNiji_syoninmati(rs.getInt(CNT));
                    } else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
                        //二次査定完了件数を設定
                        form.setNiji_kanryo(rs.getInt(CNT));
                    }                   
                } else if (GS.PHASE_HIKIATEKIN_KAKUNIN.equals(rs.getString(PHASE)) && !(user_bean.getComWorkflowSystemkbn().equals(GS.GSS))) {
                    if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
                        //引当金確認証未処理件数を設定
                        form.setKakunin_misyori(rs.getInt(CNT));
                    } else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
                        //引当金確認処理中件数を設定
                        form.setKakunin_syorityu(rs.getInt(CNT));
                    } else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
                        //引当金確認承認待ち件数を設定
                        form.setKakunin_syoninmati(rs.getInt(CNT));
                    } else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
                        //引当金確認完了件数を設定
                        form.setKakunin_kanryo(rs.getInt(CNT));
                    }
                } else if (GS.PHASE_HIKIATEKIN_KENSYO.equals(rs.getString(PHASE)) && user_bean.getComWorkflowSystemkbn().equals(GS.GSS)) {
                    if (GS.STATUS_MISYORI.equals(rs.getString(STATUS))) {
                        //引当金確認証未処理件数を設定
                        form.setKakunin_misyori(rs.getInt(CNT));
                    } else if (GS.STATUS_SYORICHU.equals(rs.getString(STATUS))) {
                        //引当金確認処理中件数を設定
                        form.setKakunin_syorityu(rs.getInt(CNT));
                    } else if (GS.STATUS_SYONIN_MACHI.equals(rs.getString(STATUS))) {
                        //引当金確認承認待ち件数を設定
                        form.setKakunin_syoninmati(rs.getInt(CNT));
                    } else if (GS.STATUS_KANRYO.equals(rs.getString(STATUS))) {
                        //引当金確認完了件数を設定
                        form.setKakunin_kanryo(rs.getInt(CNT));
                    }
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
    }   

    /**
     * 一覧情報取得処理 <br>
     * 
     * @exception SQLException
     */
    public void getMeisai() throws SQLException {

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
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OD1101_SELECT_ICHIRAN, sqlExec);
        // ユーザ情報.業務フローパターン査定会社コード
        exCstmt.setStringIn(workflowsateikaisya_cd);
        // 参照分類２コード
        exCstmt.setStringIn(sansyoBunrui2);
        // 参照本部コード
        exCstmt.setStringIn(sansyoHonbuCd);
        // 査定期
        exCstmt.setStringIn(form.getSateiki());
        // ユーザID
        exCstmt.setStringIn(userId);
        // 自担当分/汎用２選択値
        exCstmt.setStringIn(form.getTanto());
        // ソート項目
        exCstmt.setStringIn(form.getSort_item());
        // 整列方向
        exCstmt.setStringIn(form.getSort_order());
        // 業務フローパターンシステム区分
        exCstmt.setStringIn(workflowSystemkbn);
        // 言語モード
        exCstmt.setStringIn(cmnData.getComLangMode());          
        // フェーズ
        exCstmt.setStringIn(form.getSansyo_phase());          
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
                
                // 勘定先コード
                listBean.setKanjo_cd(rs.getString(KIKAN_TORI_CD));
                
                // 勘定先名称
                listBean.setKanjo_nm(rs.getString(BUSINESS_NM));
                
                // 所在国
                listBean.setSyozaikoku(rs.getString(WB_COUNTRY_NM));
                
                // 所在地
                listBean.setSyozaichi(rs.getString(SYOZAICHI));
                
                String kingaku = Function.trim(rs.getString(SAIKEN_KINGAKU));

                // 金額計
                listBean.setSaiken_kingaku(formatKingaku(kingaku,workflowSystemkbn));
                
                // 対象年月
                listBean.setTaisyo_ym(rs.getString(TAISYO_YM));
                
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
                listBean.setSoshiki(rs.getString(SOSHIKI));
                
                // システム区分
                listBean.setSystem_kbn(rs.getString(SYSTEM_KBN));
                
                // 案件保持ユーザID
                listBean.setHoji_user_id(rs.getString(HOJI_USER_ID));
                
                // 担当者
                listBean.setTanto_nm(rs.getString(TANTO_NM));
                
                // 代行ユーザID
                listBean.setDaiko_user_id(rs.getString(DAIKO_USER_ID));
                if(!check.isNullBlank(rs.getString(DAIKO_USER_ID))){
                	// 代行者名がNULLで無い場合、担当者名 || '(' || 代行者名 || ')'
                    // 課題No.163
                    // 修正開始
                	//listBean.setTanto_nm(rs.getString(TANTO_NM)+"("+rs.getString(DAIKO_USER_ID).trim()+")");
                	listBean.setTanto_nm(rs.getString(TANTO_NM)+daiko+rs.getString(DAIKOUSHA).trim()+GS.KAKKO_MIGI);
                    // 修正完了
                }
                
                // 進捗
                listBean.setSintyoku(rs.getString(SHINCHOKU));
                
                // 基準日区分
                listBean.setKijunbi_kbn(rs.getString(KIJUNBI_KBN));
                
                // 処理回数
                listBean.setSyori_kaisu(rs.getString(SYORI_KAISU));
                
                // 統合取引先コード
                listBean.setTogo_tori_cd(rs.getString(TOGO_TORI_CD));
                
                // 識別コード
                listBean.setShikibetu_cd(rs.getString(SHIKIBETU_CD));
                
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
                listBean.setSasi_ten_flg(Function.trim(rs.getString(SASI_TEN_FLG)));
                // 差戻転送フラグが｢1：差戻｣の場合、'* '(半角スペース) || 進捗
                if("1".equals(rs.getString(SASI_TEN_FLG))){
                	//  進捗
                    listBean.setSintyoku("* " +rs.getString(SHINCHOKU));
                }

                // リンク表示フラグ制御
                if(check.isNullBlank(rs.getString(HOJI_USER_ID))){
                    link_flg = true;
                }
                if(!check.isNullBlank(rs.getString(HOJI_USER_ID)) && rs.getString(HOJI_USER_ID).trim().equals(userId)){
                    link_flg = true;
                }
                if (rs.getString(STATUS).equals(GS.STATUS_SYONIN_MACHI)) {
					link_flg = false;
				}
                listBean.setLink_flg(link_flg);
                
                // 表示用年月
                listBean.setTaisyo_ym_hyoji(rs.getString(TAISYO_YM_HYOJI));
                
                // 親会社取引先コード
                listBean.setOya_duns_no(rs.getString(OYA_DUNS_NO));
                
                // ワールドベース国コード
                listBean.setWb_country_cd(rs.getString(WB_COUNTRY_CD));
                listBean.setSyozaikoku_cd(rs.getString(WB_COUNTRY_CD));
                
                // 通貨コード
                listBean.setTuuka_cd(rs.getString(TUUKA_CD));
                
                // 会社コード
                listBean.setKaisya_cd(rs.getString(KAISHA_CD));
                
                // 半期四半期区分
                listBean.setHanki_sihanki_kbn(rs.getString(HANKI_SIHANKI_KBN));
                // 第1/3四半期データの場合は水色で年月に'(Q)'を付加して表示する
                if(HANKI_SIHANKI_KBN_2.equals(rs.getString(HANKI_SIHANKI_KBN))){
                	// 表示用年月
                    listBean.setTaisyo_ym_hyoji(Function.addQuarter(rs.getString(TAISYO_YM_HYOJI)));
                }
                
                // 分類２
                listBean.setBunrui2(rs.getString(BUNRUI2));
                
                // 査定期
                listBean.setSatei_ki(rs.getString(SATEI_KI));
                
                // 親会社名称
                listBean.setOya_business_nm(rs.getString(OYA_BUSINESS_NM));
                
                // 店コード
                listBean.setMise_cd(rs.getString(MISE_CD));

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
     * 一括もぎ取りチェック処理 <br>
     * @return
     * @throws SQLException
     */
    public boolean checkIkkatuMogitori(TorihikisakiBean listBean) throws SQLException {
    	
    	// 処理結果フラグ
        boolean result = false;
        
        String anken_no = listBean.getAnken_no();           // 滞留判定案件No.
        String hoji_user = listBean.getHoji_user_id();  // 案件保持ユーザ
        String phase = listBean.getPhase();             // フェーズ(一覧表示時)
        String status = listBean.getStatus();           // ステータス(一覧表示時)
        String mogitori_phase = null;                   // フェーズ(もぎ取りチェック時)
        String mogitori_status = null;                  // ステータス(もぎ取りチェック時)
        
        // ExCallableStatement生成
    	ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1402, sqlExec);
        exCstmt.setStringIn(anken_no);
        exCstmt.setResultSet(RESULTSET);
        
        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
            	hoji_user = rs.getString(HOJI_USER_ID);
                mogitori_phase = Function.trim(rs.getString(PHASE));
                mogitori_status = Function.trim(rs.getString(STATUS));
            	if((hoji_user == null || hoji_user.equals(userId)) && mogitori_phase.equals(phase) && mogitori_status.equals(status)) {
        			result = true;
            	}
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
     * もぎ取りチェック処理 <br>
     * @return boolean(true:もぎ取り可、false:もぎ取り不可)
     * @exception SQLException
     */
    public boolean checkMogitori() throws SQLException {
        
        // 処理結果フラグ
        boolean result = false;
        // チェック用パラメータ取得
        TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
        String anken_no = form.getAnken_no();           // 滞留判定案件No.
        String hoji_user = listBean.getHoji_user_id();  // 案件保持ユーザ
        String phase = listBean.getPhase();             // フェーズ(一覧表示時)
        String status = listBean.getStatus();           // ステータス(一覧表示時)
        String mogitori_phase = null;                   // フェーズ(もぎ取りチェック時)
        String mogitori_status = null;                  // ステータス(もぎ取りチェック時)

        //ExCallableStatement生成
    	ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_T1402, sqlExec);
        exCstmt.setStringIn(anken_no);
        exCstmt.setResultSet(RESULTSET);

        try{
            //SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);  
            if(rs.next()){
            	hoji_user = rs.getString(HOJI_USER_ID);
                mogitori_phase = Function.trim(rs.getString(PHASE));
                mogitori_status = Function.trim(rs.getString(STATUS));
            	if((hoji_user == null || hoji_user.equals(userId)) && mogitori_phase.equals(phase) && mogitori_status.equals(status)) {
        			result = true;
            	}
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
     *  案件の進捗を更新する。(もぎ取り処理)<br>
     * 
     * @throws SQLException
     */
    public void uptT1401(TorihikisakiBean listBean) throws SQLException {

    	// ExCallableStatement生成
    	ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1401, sqlExec); 
        // 滞留判定案件No.
        exCstmt.setStringIn(listBean.getAnken_no());
        // 代行ユーザID
        exCstmt.setStringIn(user_bean.getComDaiko_userId());
        // ユーザID
        exCstmt.setStringIn(user_bean.getComUserId());
        // 機)勘定先情報.分類２
        exCstmt.setStringIn(listBean.getBunrui2());
        // 共)ユーザ情報.分類２
        exCstmt.setStringIn(user_bean.getComSyozokuBunrui2());
        // 機)勘定先情報.部コード
        exCstmt.setStringIn(listBean.getBu_cd());
        // 機)ユーザ情報.部コード
        exCstmt.setStringIn(user_bean.getComSyozokuBuCd());
        
        try {
              //SQL実行
              exCstmt.execute();
              isError(exCstmt);
        } finally {
            if (rs != null) {
                //Resultset close
                rs.close();
            }   
        }
	        
    }
    
    /**
     * 
     *  入力履歴の登録を行う<br>
     * 
     * @throws SQLException
     */
    public void intT1300(TorihikisakiBean listBean) throws SQLException {
    	
    	// ExCallableStatement生成
    	ResultSet rs = null;
        ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
        // 案件保持ユーザ
        String hoji_user = listBean.getHoji_user_id(); 
        
        // ログインユーザが既にもぎ取り済みの場合は更新・登録は行わない
		if(userId.equals(hoji_user)) {
			return;
		}
        
        // 案件No.
        exCstmt.setStringIn(listBean.getAnken_no());
        // 判定査定区分
        exCstmt.setStringIn(HS_KBN_HANTEI);
        // 査定会社コード
        exCstmt.setStringIn(listBean.getSateikaisya_cd());
        // ユーザID
        exCstmt.setStringIn(user_bean.getComUserId());
        // ユーザ名日本語
        exCstmt.setStringIn(user_bean.getComUser_Nm());
        // ユーザ名英語
        exCstmt.setStringIn(user_bean.getComUser_Nm_En());
        // 所属部署名日本語
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());
        // 所属部署名英語
        exCstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());
        // フェーズ
        exCstmt.setStringIn(listBean.getPhase());
        // 入力区分
        exCstmt.setStringIn(MOGITORI);
        // 登録ユーザID
        exCstmt.setStringIn(user_bean.getComUserId());

        if (user_bean.getComDaiko_userId() == null) {
            // 通常時
            // 代行ユーザID                           
        	exCstmt.setStringIn(null);
            // 代行者名日本語
        	exCstmt.setStringIn(null);
            // 代行者名英語
        	exCstmt.setStringIn(null);        
        } else {
            // 代行時
            // 代行ユーザID                           
        	exCstmt.setStringIn(user_bean.getComDaiko_userId());
            // 代行者名日本語
        	exCstmt.setStringIn(user_bean.getComDaiko_user_nm());
            // 代行者名英語
        	exCstmt.setStringIn(user_bean.getComDaiko_user_nm_en());        
        }
        // 登録箇所
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        // コメント内容
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        // 転送元分類２
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
		// 転送元部
		exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        // 承認ユーザID
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        
        try {
                //SQL実行
                exCstmt.execute();
                isError(exCstmt);
        } finally {
            if (rs != null) {
                //Resultset close
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
    public String formatKingaku(String kingaku, String systemKbn) {
        String formatKingaku = GS.EMPTY_CHARCTER;
        if(!(GS.EMPTY_CHARCTER.equals(kingaku))){
            if (systemKbn.equals(GS.GSS)){
                //国内
            	formatKingaku = Function.format(NUM_FMT_KAIGAI,Function.getValueOfDouble(kingaku));
            }else{
                //海外
            	formatKingaku = Function.format(NUM_FMT_KOKUNAI,Function.getValueOfDouble(kingaku));
            }
        }
        return formatKingaku;
    }
}