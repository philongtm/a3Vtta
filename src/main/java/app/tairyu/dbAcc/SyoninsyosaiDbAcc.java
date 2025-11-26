/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/30		SSC				課題No.129 案件保持ユーザ更新処理修正
003		2009/12/17		SSC(坂本) 		課題No.205 査定先金額判定条件の変更
004		2015/02/23		SSC(前多)		案件No.BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.tairyu.form.SyoninsyosaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * OB1105_実質滞留債権判定_承認 DBアクセスクラス <br>
 */
public class SyoninsyosaiDbAcc extends CommonDbAcc {

	private AppContext appContext = null;				                          						// ＡＰＰコンテキスト
	private SessionData cmnData = null;					                          					// 機能共通セッション
	private UserBean user_bean = null;					                          						// ユーザ情報
	private SyoninsyosaiForm form = null;
	private TorihikisakiBean meisaiBean;

    private static final String SP_SS_OB_SELECT_T0700         = "SP_SS_OB_SELECT_T0700";              // チャンピオン部重複チェックプロシージャ
    private static final String SP_SS_OB_INSERT_T1000         = "SP_SS_OB_INSERT_T1000";              // T10_滞留判定の登録プロシージャ
    private static final String SP_SS_O_UPDATE_T0800         	 = "SP_SS_O_UPDATE_T0800";              // T10_滞留判定の登録プロシージャ
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
    private static final String SP_SS_OB_UPDATE_T0801         = "SP_SS_OB_UPDATE_T0801";               // T08_滞留判定進捗管理の更新プロシージャ
    private static final String SP_SS_OB_INSERT_SATEI         = "SP_SS_OB_INSERT_SATEI";              // T15_一次二次査定の登録プロシージャ
    private static final String SP_SS_O_INSERT_T0400          = "SP_SS_O_INSERT_T0400";               // T04_メール配信の登録を行うプロシージャ
    private static final String SP_SS_OB_SELECT_M0900         = "SP_SS_OB_SELECT_M0900";				 // 査定データ作成条件有無チェックプロシージャ
    private static final String SP_SS_OB_SELECT_CHAMPIONBU            = "SP_SS_OB_SELECT_CHAMPIONBU";	 // 現チャンピオン部取得
    private static final String SP_SS_OB_SELECT_T0900                 = "SP_SS_OB_SELECT_T0900";		 // 滞留債権額が最も大きい部を取得
    private static final String SP_SS_OB_UPDATE_T0700                 = "SP_SS_OB_UPDATE_T0700";		 // T07_チャンピオン部の更新（フラグをはずす）
    private static final String SP_SS_OB_UPDATE_T0700_2               = "SP_SS_OB_UPDATE_T0700_2";		 // T07_チャンピオン部の更新（フラグを立てる）

    private static final String SATEI_ANKEN_NO          		= "satei_anken_no"; 					 // 査定案件No
    private static final String BUNRUI2          				= "bunrui2"; 							 // 分類２
    private static final String BU_CD          					= "bu_cd"; 							 // 部コード
    private static final String HAISINZUMI_FLG 				= "N";									 // 配信済みフラグ
    private static final String ANKEN_NO          			= "anken_no";							 // 案件No
    private static final String ANKEN_NO_EDA          		= "anken_no_eda";						 // 案件No枝番
    private static final String ANKEN_NO_CNT					= "anken_no_cnt";						 // 案件Noコンット
    private static final String ICHIJI_FLG					= "ichiji_flg";						 	 // 一次査定対象フラグ
    private static final String KINGAKUJYOUKEN				= "kingakujyouken";						 // 金額条件
    private static final String KINGAKUKEI					= "kingakukei";						 	 // 金額計
    private static final String TORIMODOSHI_FUKA_FLG_YES		= "1";						 	 		 // 取戻不可
    private static final String BU_CD_CNT						= "bu_cd_cnt";							 // 部コードのカウント件数
    private static final String TAIRYU_HANTEI					= "1";						 	 		 // 滞留判定
    private static final String NYURYOKU_KBN_SYONIN			= "80";						 	 		 // 入力区分(承認)
	private static final String CNT 							= "cnt";								 // 件数
	private static final String CHAMPIONBU_CD					= "championbu_cd";						// 現チャンピオン部コード
	private static final String MAX_TAIRYU_BU_CD				= "max_tairyu_bu_cd";					// 滞留債権額が最も大きい部

    /**
	 * コンストラクタ <br>
	 *
	 * @param sqlExec
	 * @param log
	 * @param appcontext
	 */
	public SyoninsyosaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		form = (SyoninsyosaiForm)appContext.getActionForm();

		// ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();

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
        String mise_cd = meisaiBean.getMise_cd();		//課題対応No,205	店コード追加
        int cnt = 0;
    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
        cstmt = new ExCallableStatement(SP_SS_OB_SELECT_M0900, sqlExec);
        cstmt.setStringIn(system_kbn);
        cstmt.setStringIn(sateikaisya_cd);						//課題対応No,205	査定会社コード追加
        cstmt.setStringIn(mise_cd);						//課題対応No,205	店コード追加
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
     * T07_チャンピオン部の更新（フラグを立てる） <br>
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
                if(GS.GSS.equals(meisaiBean.getSystem_kbn())){
                    exCstmt.setStringIn(meisaiBean.getBu_cd());
                } else {
                	exCstmt.setStringIn(GS.EMPTY_CHARCTER);
                }
            } else {
                exCstmt.setStringIn(form.getBunrui2());
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
            exCstmt.setStringIn(GS.OB1105);
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
     * 明細ビンーを設定する <br>
     *
     * @param meisaiBean
     * @throws SQLException
     */
    public void setMeisaiBean(TorihikisakiBean meisaiBean) throws SQLException {
    	this.meisaiBean = meisaiBean;
    }
}
