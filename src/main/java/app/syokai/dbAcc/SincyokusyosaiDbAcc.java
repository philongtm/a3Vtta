/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/03		SSC				課題No.20 取戻処理を案件単位に変更
003		2009/11/20		SSC				課題No.152 代行取戻対応
004		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
005		2015/12/25		SSC				BP201601002 障害対応（督促メール送信機能）
006		2016/03/23		SSC				BJ201602002_部門廃止対応（一次）
******************************************************************************/

package app.syokai.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.form.SincyokusyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
* OS6104_進捗状況詳細 DBアクセスクラス <br>
*/
public class SincyokusyosaiDbAcc extends CommonDbAcc {
	private SessionData cmnData = null;							// 機能共通セッション
	private UserBean user_bean = null;								// ユーザ情報
	private TorihikisakiBean tori_bean = null;						// 取引先情報
	private SincyokusyosaiForm form = null;						// アクションフォーム
	private AppContext appContext = null;							// ＡＰＰコンテキスト

	//	Resultset用文字列
	private static final String TOROKU_GAMEN	= "1";
    //課題No.152
    //修正開始
	//private static final String YM			= "ym";				// 年月
	//private static final String ANKEN_NO		= "anken_no";		//案件No.
	//private static final String PHASE			= "phase";			//フェーズ
	//private static final String STATUS		= "status";			//ステータス
	//private static final String SIN_TYOKU		= "sin_tyoku";		//進捗
	//private static final String SASI_TEN_FLG	= "sasi_ten_flg";	//差戻・転送FLG
	//private static final String PHASE_HANDAN	= "phase_handan";	//フェーズ判定
	//private static final String INIT_BUNRUI2 = "init_bunrui2";	// 初期分類2
    //修正完了
	private static final String TANTO_USER				= "tanto_user";					//担当者
	private static final String TORIMODOSHI_FUKA_FLG		= "torimodoshi_fuka_flg";		//取戻不可フラグ
	private static final String UPD_USER_HANDAN 			= "upd_user_handan";			//更新ユーザID判定
	private static final String BUNRUI2					= "bunrui2";					//分類2
	private static final String BU_CD						= "bu_cd";						//部コード
	private static final String TENSOMOTO_BUNRUI2			= "tensomoto_bunrui2";			//転送元分類2
	private static final String TENSOMOTO_BU				= "tensomoto_bu";				//転送元部
	private static final String HAISHIN_DT					= "haishin_dt";					//配信日時
	private static final String HOZON_DT					= "hozon_dt";					//保存日時
	private static final String HAISINZUMI_FLG				= "N";							//配信済フラグ'N'：未配信

	// INパラメータ
    private String userId;                                                                  // ユーザＩＤ
    //課題No.152
    //削除開始
    //private String systemkbn;
	//private String mise_cd;
	//private String satei_ki;
	//private String tori_cd;
    //削除完了
	private String satei_kaisha_cd;

    //課題No.152
    //削除開始
	//private static final String SP_SS_OS6104_SELECT_SINCYOKU	= "SP_SS_OS6104_SELECT_SINCYOKU";       // 最新査定進捗状況を取得処理
    //削除完了
    private static final String SP_SS_OS6104_SELECT_CHECK	= "SP_SS_OS6104_SELECT_CHECK";       		// 取戻チェック処理
    private static final String SP_SS_O_UPDATE_T0800	= "SP_SS_O_UPDATE_T0800";       				// T08滞留判定進捗管理の更新(代行)
    private static final String SP_SS_O_UPDATE_T0801	= "SP_SS_O_UPDATE_T0801";       				// T08滞留判定進捗管理の更新
    private static final String SP_SS_O_UPDATE_T1400	= "SP_SS_O_UPDATE_T1400";       				// T14査定進捗管理の更新(代行)
    private static final String SP_SS_O_UPDATE_T1402	= "SP_SS_O_UPDATE_T1402";       				// T14査定進捗管理の更新
    private static final String SP_SS_OS6104_UPDATE_T0400	= "SP_SS_OS6104_UPDATE_T0400"; 				// メール配信のスキップ処理
    private static final String SP_SS_O_INSERT_T1300	= "SP_SS_O_INSERT_T1300";       				// 入力履歴の登録処理
    private static final String SP_SS_OS6104_SELECT_T1300	= "SP_SS_OS6104_SELECT_T1300"; 				// 転送元組織の取得
	private static final String SP_SS_O_SELECT_SANSYO = "SP_SS_O_SELECT_SANSYO";						// ユーザの参照権限取得用プロシージャ

	private static final String SP_SS_OS_SELECT_HAISHINYMD = "SP_SS_OS_SELECT_HAISHINYMD";				// 最新の督促メール配信日時を取得
	private static final String SP_SS_O_INSERT_T2700 = "SP_SS_O_INSERT_T2700";							// T27_督促メール配信登録用プロシージャ
	private static final String SP_SS_TOKUSOKUMAIL = "SP_SS_TOKUSOKUMAIL";								// 督促メール配信
	private static final String SP_SS_OS_SELECT_KENGEN = "SP_SS_OS_SELECT_KENGEN";						//ユーザ権限取得
	private static final String SP_SS_OS_SELECT_HOZONYMD = "SP_SS_OS_SELECT_HOZONYMD";					// 最新の督促メール保存日時を取得

    private static final String OPE_KBN 				= "90";											 // 入力区分
    private static final String OPE_KBN50 			= "50";											 // 入力区分(転送)
    //課題No.152
    //追加開始
    private static final String OPE_KBN10 			= "10";											// 入力区分(登録)
    private static final String TENSOU_SASHI_KBN 		= "2";											// 入力区分(登録)
    private static final String UPD_USER_ID_FLG_1		= "1";											// 更新ユーザID判定
    //追加完了


    /**
	 * コンストラクタ <br>
	 *
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SincyokusyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		tori_bean = cmnData.getTori_bean();
		form = (SincyokusyosaiForm)appContext.getActionForm();

        // ビーンの値を変数に設定
        this.userId = user_bean.getComUserId();					//共)ユーザ情報.ユーザID
        //課題No.152
        //削除開始
        //this.systemkbn = tori_bean.getSystem_kbn();				//共)取引先情報.システム区分
		//this.mise_cd = tori_bean.getMise_cd();					//共)取引先情報.店コード
		//this.satei_ki = tori_bean.getSatei_ki();				//共)取引先情報.査定期
		//this.tori_cd = tori_bean.getKanjo_cd();					//共)取引先情報.取引先CD
        //削除完了
		this.satei_kaisha_cd = tori_bean.getSateikaisya_cd();	//共)取引先情報.査定会社コード
	}

    // 課題No.20
    // 削除開始
	/**
	 * 最新査定進捗状況取得処理 <br>
	 *
	 * @exception SQLException
	 */
	/*public void getProgress() throws SQLException {

		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6104_SELECT_SINCYOKU, sqlExec);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(satei_ki);
		exCstmt.setStringIn(tori_cd);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();	// 明細配列

	    	while ( rs.next() ) {

				//対象年月
				tori_bean.setTaisyo_ym(rs.getString(YM));

				//フェーズ
				tori_bean.setPhase(rs.getString(PHASE));

				//ステータス
				tori_bean.setStatus(rs.getString(STATUS));

				// 進捗
				tori_bean.setSintyoku(rs.getString(SIN_TYOKU));

				// 差戻・転送FLG
				tori_bean.setSasi_ten_flg(rs.getString(SASI_TEN_FLG));

				// 案件No.
				tori_bean.setAnken_no(rs.getString(ANKEN_NO));

				// 初期分類2
				tori_bean.setInit_bunrui2(rs.getString(INIT_BUNRUI2));

				// 分類2
				tori_bean.setBunrui2(rs.getString(BUNRUI2));

				// 部コード
				tori_bean.setBu_cd(rs.getString(BU_CD));

				// 取戻不可フラグ
				form.setTorimodoshi_fuka_flg(rs.getString(TORIMODOSHI_FUKA_FLG));

				//更新ユーザID判定
				form.setUpd_user_id_flg(rs.getString(UPD_USER_HANDAN));

				//フェーズ判定
				form.setPhase_hantei(rs.getString(PHASE_HANDAN));

				//カレントタブ
				form.setKarento_tab(rs.getString(PHASE_HANDAN));
				if("2".equals(rs.getString(SASI_TEN_FLG))){
					// メール用画面ID
					form.setMail_gamen_id("OZ3101");
				}else{
					// メール用画面ID
					form.setMail_gamen_id(form.toString());
				}


				form.setSintyoku(rs.getString(SIN_TYOKU));

				ar_meisai.add(tori_bean);
	    	}

		    // ActionForm に明細を格納
		    form.setAr_meisai(ar_meisai);

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
	}*/
    // 削除完了

	 /**
     * 取戻チェックを行う <br>
     *
     * @throws SQLException
     */
    public void getCheck() throws SQLException {

    	ResultSet rs = null;
        try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6104_SELECT_CHECK, sqlExec);
            // 課題No.152
            // 削除開始
            //exCstmt.setStringIn(userId);
            // 削除完了
            exCstmt.setStringIn(form.getPhase_hantei());
            exCstmt.setStringIn(tori_bean.getAnken_no());
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            if(rs.next()){
                // 取戻不可フラグ
    			form.setTorimodoshi_fuka_flg(rs.getString(TORIMODOSHI_FUKA_FLG));

                // 課題No.152
                // 削除開始
    			// 更新ユーザID判定
    			//form.setUpd_user_id_flg(rs.getString(UPD_USER_HANDAN));
                // 削除完了
            }else{
            	// 取戻不可フラグ
    			form.setTorimodoshi_fuka_flg(GS.EMPTY_CHARCTER);

                // 課題No.152
                // 削除開始
    			// 更新ユーザID判定
    			//form.setUpd_user_id_flg(GS.EMPTY_CHARCTER);
                // 削除完了
            }

        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

	 /**
     * 転送元組織の取得 <br>
     *
     * @throws SQLException
     */
    // 課題No.152
    // 修正開始
    public Map<String,String> getTorimodoshiMot() throws SQLException {
    //public Map<String,String> getTensoumotoSoshiki() throws SQLException {
    	String opeKbn = OPE_KBN10;
    	if(TENSOU_SASHI_KBN.equals(tori_bean.getSasi_ten_flg())){
    		opeKbn = OPE_KBN50;
    	}
    // 修正完了

    	ResultSet rs = null;
    	try{
            // ExCallableStatement生成
            ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS6104_SELECT_T1300, sqlExec);
            exCstmt.setStringIn(tori_bean.getAnken_no());
            exCstmt.setStringIn(tori_bean.getPhase());
            // 課題No.152
            // 修正開始
            //exCstmt.setStringIn(OPE_KBN50);
            exCstmt.setStringIn(opeKbn);
            //exCstmt.setStringIn(userId);
            // 修正完了
            exCstmt.setResultSet(RESULTSET);

            // SQL実行
            exCstmt.execute();
            isError(exCstmt);
            rs = exCstmt.getResultSet(RESULTSET);
            Map<String,String> map = new LinkedHashMap<String,String>();
            if(rs.next()){
            	map.put(BUNRUI2,rs.getString(TENSOMOTO_BUNRUI2));
            	map.put(BU_CD,rs.getString(TENSOMOTO_BU));
                // 課題No.152
                // 追加開始
            	map.put(TANTO_USER,rs.getString(TANTO_USER));
                // 追加完了
            }
            // 課題No.152
            // 追加開始
            if(userId.equals(Function.trim(map.get(TANTO_USER)))){
            	form.setUpd_user_id_flg(UPD_USER_ID_FLG_1);
            }
            // 追加完了
            return map;
        } finally {
            if (rs != null) {
                // Resultset close
                rs.close();
            }
        }
    }

	 /**
     * 参照組織のチェックを行う <br>
     *
     * @throws SQLException
     */
    public boolean chkSansyososhiki(String bunrui2,String bu_cd) throws SQLException {
    	boolean result = false;
    	// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		cstmt = new ExCallableStatement(SP_SS_O_SELECT_SANSYO,sqlExec);
		cstmt.setStringIn(userId);
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(bunrui2);
		cstmt.setStringIn(bu_cd);
		cstmt.setStringIn(tori_bean.getTaisyo_ym());
		cstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			cstmt.execute();
			isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

			if(rs.next()) {
				result = true;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
		return result;
    }

    /**
	 * T08_滞留判定進捗管理の更新 <br>
     * 共)取引先情報.差戻・転送FLGが'2'ではない場合<br>
     *
     * @exception SQLException
	 */
	public void updTairyuStatDaiko() throws SQLException {

    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T08_滞留判定進捗管理
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0800, sqlExec);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // ステータス
        cstmt.setStringIn(GS.STATUS_SYORICHU);

        // 案件保持ユーザID
        cstmt.setStringIn(userId);

        // 差戻・転送フラグ
        cstmt.setStringIn(tori_bean.getSasi_ten_flg());

        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);

        //共）ユーザ情報.代行ユーザIDがNOT NULLの場合
        if (user_bean.getComDaiko_userId() != null) {

        	//代行ユーザID
        	cstmt.setStringIn(user_bean.getComDaiko_userId());

        	//更新ユーザID
        	cstmt.setStringIn(user_bean.getComDaiko_userId());
        }else{
        	//代行ユーザID
        	cstmt.setStringIn(null);

        	//更新ユーザID
        	cstmt.setStringIn(userId);

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
	 * T08_滞留判定進捗管理の更新 <br>
     * 共)取引先情報.差戻・転送FLGが'2'の場合<br>
     *
     * @exception SQLException
	 */
	public void updTairyuStat(String bunrui2,String bu_cd) throws SQLException {

		ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T08_滞留判定進捗管理
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T0801, sqlExec);

        // 課題No.152
        // 修正開始
        //代行ユーザID
        //cstmt.setStringIn(tori_bean.getDaiko_user_id());
        cstmt.setStringIn(user_bean.getComDaiko_userId());
        // 修正完了

    	//更新ユーザID
    	cstmt.setStringIn(userId);

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // 分類２コード
        cstmt.setStringIn(bunrui2);

        // 部コード
        cstmt.setStringIn(bu_cd);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());
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
	 * T14_査定進捗管理の更新 <br>
     * 共)取引先情報.差戻・転送FLGが'2'ではない場合<br>
     *
     * @exception SQLException
	 */
	public void updSateiStatDaiko() throws SQLException {

    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T14_査定進捗管理
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1400, sqlExec);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // ステータス
        cstmt.setStringIn(GS.STATUS_SYORICHU);

        // 案件保持ユーザID
        cstmt.setStringIn(userId);

        // 査定登録画面
        cstmt.setStringIn(TOROKU_GAMEN);

        // 差戻・転送フラグ
        cstmt.setStringIn(tori_bean.getSasi_ten_flg());

        //共）ユーザ情報.代行ユーザIDがNOT NULLの場合
        if (user_bean.getComDaiko_userId() != null) {

        	//代行ユーザID
        	cstmt.setStringIn(user_bean.getComDaiko_userId());

        	//更新ユーザID
        	cstmt.setStringIn(user_bean.getComDaiko_userId());
        }else{
        	//代行ユーザID
        	cstmt.setStringIn(null);

        	//更新ユーザID
        	cstmt.setStringIn(userId);

        }

        // 取戻不可フラグ
        cstmt.setStringIn(GS.TORIMODOSHI_HUKA);

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
	 * T14_査定進捗管理の更新 <br>
     * 共)取引先情報.差戻・転送FLGが'2'の場合<br>
     *
     * @exception SQLException
	 */
	public void updSateiStat(String bunrui2,String bu_cd) throws SQLException {

    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T14_査定進捗管理
        cstmt = new ExCallableStatement(SP_SS_O_UPDATE_T1402, sqlExec);

        //代行ユーザID
        // 課題No.152
        // 修正開始
        //cstmt.setStringIn(tori_bean.getDaiko_user_id());
    	cstmt.setStringIn(user_bean.getComDaiko_userId());
        // 修正完了

    	//更新ユーザID
    	cstmt.setStringIn(userId);

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // 分類２コード
        cstmt.setStringIn(bunrui2);

        // 部コード
        cstmt.setStringIn(bu_cd);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());
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
	 * メール配信のスキップを行う <br>
     *
     * @exception SQLException
	 */
	public void updMailHaishin() throws SQLException {

    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T08_滞留判定進捗管理
        cstmt = new ExCallableStatement(SP_SS_OS6104_UPDATE_T0400, sqlExec);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());

        // 査定会社コード
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());

        // 対象年月
        cstmt.setStringIn(tori_bean.getTaisyo_ym());

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // ステータス
        cstmt.setStringIn(tori_bean.getStatus());

        // メール用画面ID
        cstmt.setStringIn(form.getMail_gamen_id());

        // ユーザID
        cstmt.setStringIn(userId);

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
	 * T13_入力履歴の更新 <br>
     *
     * @exception SQLException
	 */
	public void addNyuryokuHist() throws SQLException {

    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
        // T13_入力履歴
        cstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);

        // 案件No.
        cstmt.setStringIn(tori_bean.getAnken_no());

        // 判定査定区分
        cstmt.setStringIn(form.getPhase_hantei());

        // 査定会社コード
        cstmt.setStringIn(tori_bean.getSateikaisya_cd());

        // ユーザID
        cstmt.setStringIn(userId);

        // ユーザ名日本語
        cstmt.setStringIn(user_bean.getComUser_Nm());

        // ユーザ名英語
        cstmt.setStringIn(user_bean.getComUser_Nm_En());

        // 所属部署名日本語
        cstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm());

        // 所属部署名英語
        cstmt.setStringIn(user_bean.getComSyozokuSoshiki_Nm_En());

        // フェーズ
        cstmt.setStringIn(tori_bean.getPhase());

        // 入力区分
        cstmt.setStringIn(OPE_KBN);

        // 登録ユーザID
        cstmt.setStringIn(userId);

        if (user_bean.getComDaiko_userId() != null){

        	 // 代行ユーザID
            cstmt.setStringIn(user_bean.getComDaiko_userId());

            // 代行者名日本語
            cstmt.setStringIn(user_bean.getComDaiko_user_nm());

            // 代行者名英語
            cstmt.setStringIn(user_bean.getComDaiko_user_nm_en());
        }else{

        	// 代行ユーザID
            cstmt.setStringIn(null);

            // 代行者名日本語
            cstmt.setStringIn(null);

            // 代行者名英語
            cstmt.setStringIn(null);
        }

        // 登録箇所
        cstmt.setStringIn(null);

        // コメント内容
        cstmt.setStringIn(null);

        // 転送元分類２
        cstmt.setStringIn(null);

        // 転送元部
        cstmt.setStringIn(null);

        // 承認ユーザID
        cstmt.setStringIn(null);

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
	 * ユーザ権限取得 <br>
	 *
	 * @param RirekiBean
	 * @return boolean
	 * @exception SQLException
	 */
	public boolean isUserKengen(String tanto_user_id) throws SQLException {
		boolean result = false;
		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_KENGEN, sqlExec);
		exCstmt.setStringIn(tanto_user_id);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(tori_bean.getPhase());
		exCstmt.setStringIn(tori_bean.getStatus());
		exCstmt.setStringIn(tori_bean.getBu_cd());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			if(rs.next()) {
				result = true;
			}
			return result;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 最新の督促メール配信日時を取得 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void getHaishinDt() throws SQLException {
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_HAISHINYMD, sqlExec);
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setHaishin_Dt(rs.getString(HAISHIN_DT));
			} else {
				// 配信日時がない
				if("Ja".equals(cmnData.getComLangMode())){
					form.setHaishin_Dt("なし");
				} else {
					form.setHaishin_Dt("No Data");
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * 最新の督促メール保存日時を取得 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void getHozonDt() throws SQLException {
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_HOZONYMD, sqlExec);
		exCstmt.setStringIn(tori_bean.getSystem_kbn());
		exCstmt.setStringIn(tori_bean.getBunrui2());
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(tori_bean.getAnken_no());
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			if(rs.next()) {
				form.setHozon_Dt(rs.getString(HOZON_DT));
			} else {
				// 保存日時がない
				if("Ja".equals(cmnData.getComLangMode())){
					form.setHozon_Dt("なし");
				} else {
					form.setHozon_Dt("No Data");
				}
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	/**
	 * T27_督促メール配信の登録 <br>
	 *
	 * @param  String 配信先担当者
	 * @param  String ログインユーザ
	 * @exception SQLException
	 */
	public void insT27(String haishinsaki,String upd_user) throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T2700, sqlExec);
		exCstmt.setStringIn(tori_bean.getAnken_no());		// 案件NO.
		exCstmt.setStringIn(satei_kaisha_cd);				// 査定会社コード
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());		// 年月
		exCstmt.setStringIn(tori_bean.getPhase());						// フェーズ
		exCstmt.setStringIn(tori_bean.getStatus());					// ステータス
		exCstmt.setStringIn(tori_bean.getBunrui2());	// 配信先部門
		exCstmt.setStringIn(tori_bean.getBu_cd());		// 配信先部
		exCstmt.setStringIn(haishinsaki);				// 配信先担当者
		exCstmt.setStringIn(upd_user);					// 依頼元担当者
		exCstmt.setStringIn(HAISINZUMI_FLG);			// 配信済みフラグ
		exCstmt.setStringIn(upd_user);					// 登録更新ユーザID

		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}

	/**
	 * 督促メール配信 <br>
	 *
	 * @param  String
	 * @param  String
	 * @exception SQLException
	 */
	public void sendTokusokuMail(String upd_user) throws SQLException {
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_TOKUSOKUMAIL, sqlExec);
		exCstmt.setStringIn(upd_user);
		exCstmt.setStringIn(userId);
		exCstmt.setStringIn(tori_bean.getSystem_kbn());	// システム区分

		//SQL実行
		exCstmt.execute();
		isError(exCstmt);
	}
}