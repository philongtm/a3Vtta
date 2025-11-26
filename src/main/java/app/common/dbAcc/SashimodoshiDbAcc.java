/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/22		SSC				課題No.222 照会関係者検索対応
003		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
004		2016/03/24		SSC				案件No.BJ201602002 部門廃止対応（一次）
005		2016/04/18		SSC				案件No.BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.common.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.common.form.RirekiBean;
import app.common.form.RirekiListBean;
import app.common.form.SashimodoshiForm;
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
* OZ2101_差戻先選択 DBアクセスクラス
*/
public class SashimodoshiDbAcc extends CommonDbAcc {
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SessionData cmnData = null;		// 共通セッションデータ
	private UserBean user_bean = null;			// ユーザ情報
	private TorihikisakiBean tori_bean = null;	// 取引先情報
	private SashimodoshiForm form = null;		// アクションフォーム

	//画面モード
	//追加開始
	private static final String JIMU_MODE     = "4";
	private static final String SINKI_MODE    = "5";
	//追加完了
	private static final String KARITUIKA_MODE= "6";
	//追加開始
	//差戻種別判定用
	private static final String TYPE3         = "3";
	//追加完了
	//Resultset用文字列	
	private static final String KIJUNBI              = "kijunbi";
	private static final String CNT                  = "cnt";
	private static final String ANKEN_NO             = "anken_no";
	private static final String TANTO_USER           = "tanto_user";
	private static final String TANTO_USER_NM        = "tanto_user_nm";
	private static final String SOSHIKI              = "soshiki";
	private static final String PHASE                = "phase";
	private static final String OPE_KBN              = "ope_kbn";
	private static final String SATEI_KAISHA_CD      = "satei_kaisha_cd";
	private static final String SYORI_DT             = "syori_dt";
	private static final String SYORI                = "syori";
	private static final String PHASE_NM             = "phase_nm";
	private static final String BUNRUI2              = "bunrui2";
	private static final String DT_TITLE             = "dt_title";
	private static final String KBN_HYOUJI_VAL       = "kbn_hyouji_val";
	private static final String KBN_VAL              = "kbn_val";
	private static final String BUMON_CD             = "bumon_cd";
	private static final String BUMON_NM             = "bumon_nm";
	private static final String EMAIL_ADDR           = "email_addr";
	private static final String TOGO_ID              = "togo_id";
	private static final String BU_CD                = "bu_cd";
	private static final String BU_NM                = "bu_nm";
	private static final String KIJUNBI_KBN_1        = "1";
	private static final String KIJUNBI_KBN          = "kijunbi_kbn";
	private static final String TAISHO_YM            = "taisho_ym";	
	//スタイル設定用
	private static final String RIGHT                = "borderRight";
	private static final String BOTTOM               = "borderBottom";
	private static final String BOTTOM_RIGHT         = "borderBottom borderRight";

	private static final String NYURYOKU_KBN    = "60";	//入力区分:差戻
	private static final String OPE80           = "80";	//入力区分:承認
	private static final String PHASE_SATEI     = "2";	//査定フェーズ判定用
	private static final String SASHIMODOSHI    = "1";	//差戻・転送フラグ
	private static final String TOROKU_POINT    = "2";	//登録箇所：差戻
	private static final String HAISINZUMI_FLG  = "N";	//配信済フラグ'N'：未配信
    private static final String SPACE           = "       ";	// 半角スペース7個
	
	private static final String SP_SS_OZ2101_SELECT_M2200     = "SP_SS_OZ2101_SELECT_M2200";		//基準日取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_T1400     = "SP_SS_OZ2101_SELECT_T1400";		//引当金確認開始済チェック用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_RIREKI00  = "SP_SS_OZ2101_SELECT_RIREKI00";	//滞留判定(査定から滞留判定へ差戻)の履歴情報取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_RIREKI01  = "SP_SS_OZ2101_SELECT_RIREKI01";	//滞留判定(滞留判定フェーズ内差戻)の履歴情報取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_RIREKI02  = "SP_SS_OZ2101_SELECT_RIREKI02";	//査定の履歴情報取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_T_ANKENNO = "SP_SS_OZ2101_SELECT_T_ANKENNO";	//滞留判定案件Noリスト取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_PHASE     = "SP_SS_OZ2101_SELECT_PHASE";		//差戻フェーズ取得用プロシージャ
	private static final String SP_SS_O_SELECT_M1200          = "SP_SS_O_SELECT_M1200";			//汎用２（右）セレクトボックスの設定値（国内）取得用プロシージャ
	private static final String SP_SS_O_SELECT_TANTO          = "SP_SS_O_SELECT_TANTO";			//担当者の一覧取得用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_KENGEN    = "SP_SS_OZ2101_SELECT_KENGEN";		//差戻先ユーザの処理権限取得用プロシージャ
	private static final String SP_SS_O_SELECT_SANSYO         = "SP_SS_O_SELECT_SANSYO";			//差戻先ユーザの参照権限取得用プロシージャ
	private static final String SP_SS_O_SELECT_SYOZOKU        = "SP_SS_O_SELECT_SYOZOKU";			//担当者の所属組織取得用プロシージャ
	private static final String SP_SS_OZ_UPDATE_T0800         = "SP_SS_OZ_UPDATE_T0800";			//T08_滞留判定進捗管理更新用プロシージャ
	private static final String SP_SS_OZ2101_DELETE_T1000     = "SP_SS_OZ2101_DELETE_T1000";		//T10_滞留判定削除用プロシージャ
	private static final String SP_SS_OZ_UPDATE_T1400         = "SP_SS_OZ_UPDATE_T1400";			//T14_査定進捗管理更新用プロシージャ
	private static final String SP_SS_OZ2101_DELETE_T1400     = "SP_SS_OZ2101_DELETE_T1400";		//T14_査定進捗管理削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1500          = "SP_SS_O_DELETE_T1500";			//T15_一次二次査定削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1600          = "SP_SS_O_DELETE_T1600";			//T16_引当金検討対象BS明細削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1700          = "SP_SS_O_DELETE_T1700";			//T17_引当金判定表示用削除用プロシージャ
	private static final String SP_SS_O_DELETE_T1900          = "SP_SS_O_DELETE_T1900";			//T19_留保債務削除用プロシージャ
	private static final String SP_SS_O_DELETE_T2000          = "SP_SS_O_DELETE_T2000";			//T20_第三者留保債務削除取得用プロシージャ
	private static final String SP_SS_OZ2101_DELETE_T1100     = "SP_SS_OZ2101_DELETE_T1100";		//T11_文書添付削除用プロシージャ
	private static final String SP_SS_O_INSERT_T1300          = "SP_SS_O_INSERT_T1300";			//T13_入力履歴登録用プロシージャ
	private static final String SP_SS_OZ2101_DELETE_T1200     = "SP_SS_OZ2101_DELETE_T1200";		//T12_コメント削除用プロシージャ
	private static final String SP_SS_O_INSERT_T1200          = "SP_SS_O_INSERT_T1200";			//T12_コメント登録用プロシージャ
	private static final String SP_SS_OZ2101_SELECT_JIYU      = "SP_SS_OZ2101_SELECT_JIYU";		//抽出事由判定用プロシージャ
	private static final String SP_SS_OZ2101_UPDATE_T0100     = "SP_SS_OZ2101_UPDATE_T0100";		//T01_対象先更新用プロシージャ
	private static final String SP_SS_OZ2101_INSERT_T1400     = "SP_SS_OZ2101_INSERT_T1400";		//T14_査定進捗管理登録用プロシージャ
	private static final String SP_SS_O_SELECT_M2200          = "SP_SS_O_SELECT_M2200";			//対象年月(初回月)取得用プロシージャ
	private static final String SP_SS_OB2102_DELETE_TAIHI     = "SP_SS_OB2102_DELETE_TAIHI";		//退避情報削除用プロシージャ
	private static final String SP_SS_OZ2101_INSERT_TAIHI     = "SP_SS_OZ2101_INSERT_TAIHI";		//退避情報登録用プロシージャ
	private static final String SP_SS_OS_SELECT_M1200_2        = "SP_SS_OS_SELECT_M1200_2";			//汎用３セレクトボックスの設定値（国内）取得用プロシージャ
	private static final String SP_SS_O_SELECT_TANTO_HONBU    = "SP_SS_O_SELECT_TANTO_HONBU";		//担当者の一覧取得用プロシージャ（部選択時）
	private static final String SP_SS_OZ2101_INSERT_T0400_2   = "SP_SS_OZ2101_INSERT_T0400_2";		//T04_メール配信登録用プロシージャ（新規差戻先選択時）

	// INパラメータ
	private String userId;				// ユーザＩＤ
	private String satei_kaisha_cd;	// 査定会社コード
	private String mise_cd;			// 店コード
	private String systemkbn;			// システム区分
    private String comLangMode;        // 共)言語モード
	private String anken_no;			// 案件No
	private String phase;				// フェーズ
	private String taisyo_ym;			// 対象年月
	private String sateiki;			// 査定期
	private String tori_cd;			// 取引先コード
	private String comDaiko_userId;	// 代行ユーザID
	
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 * @param appLog
	 */
	public SashimodoshiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		super(sqlExec, log);
		this.appContext = appcontext;
		
		//ビーン取得
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		tori_bean = cmnData.getTori_bean();
		form = (SashimodoshiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = user_bean.getComUserId();
        comLangMode = cmnData.getComLangMode();
		systemkbn = tori_bean.getSystem_kbn();
		satei_kaisha_cd = tori_bean.getSateikaisya_cd();
		mise_cd = tori_bean.getMise_cd();
        anken_no = tori_bean.getAnken_no();
        phase = tori_bean.getPhase();
		taisyo_ym = tori_bean.getTaisyo_ym();
		sateiki = tori_bean.getSatei_ki();
		tori_cd = tori_bean.getKanjo_cd();
        comDaiko_userId = user_bean.getComDaiko_userId();

	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    userId = GS.EMPTY_CHARCTER;
	    satei_kaisha_cd = GS.EMPTY_CHARCTER;
	    systemkbn = GS.EMPTY_CHARCTER;
        comLangMode = GS.EMPTY_CHARCTER;
	}
		
	/**
	 * 基準日取得処理 <br>
	 * 
	 * @return    String 
	 * @exception SQLException
	 */
	public String getKijunbi() throws SQLException {
		String kijunbi = null; //基準日
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_M2200, sqlExec);
		exCstmt.setStringIn(sateiki);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
		    if (rs.next()) {
		    	kijunbi = rs.getString(KIJUNBI);
		    }
		    return kijunbi;
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}

	/**
	 * 引当金検証/確認開始済みチェック <br>
     * 
     * @param  String
	 * @return boolean(True:開始済み、False:未開始) 
	 * @exception SQLException
	 */
	public boolean isHikiate_kaishi(String kijunbi) throws SQLException {
		boolean hikiate_flg = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_T1400, sqlExec);
		exCstmt.setStringIn(sateiki);
		exCstmt.setStringIn(kijunbi);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setIntOut(CNT);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
		    if (exCstmt.getInt(CNT) > 0) {
		    	//1件以上の場合、Trueをセット
		    	hikiate_flg = true;
		    }
		    return hikiate_flg;
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 滞留判定案件No <br>
	 * 
	 * @return boolean(True:有、False:無) 
	 * @exception SQLException
	 */
	public boolean isExsit_Tairyu() throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_T_ANKENNO, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
            ArrayList<String> ar_tairyu_anken_no = new ArrayList<String>();
            while (rs.next()) {
            	//取得した滞留判定案件Ｎｏを格納
            	ar_tairyu_anken_no.add(rs.getString(ANKEN_NO));
		    	result = true;
            }
            form.setAr_tairyu_anken_no(ar_tairyu_anken_no);
            return result;
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 差戻先一覧取得(滞留フェーズ内差戻時) <br>
	 * 
	 * @exception SQLException
	 */
	public void getTairyu_sashimodoshisaki() throws SQLException {
		ResultSet rs = null;
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_RIREKI01, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			List<RirekiListBean> ar_meisai = new ArrayList<RirekiListBean>();	// 明細配列
	    	RirekiListBean listBean = new RirekiListBean();						// 履歴リストBean(チェックボックス判定用)
	    	List<RirekiBean> rireki_list = new ArrayList<RirekiBean>();			// 履歴リスト
	    	int i = 0;
	    	while (rs.next()) {
	    		RirekiBean rirekiBean = new RirekiBean();	//履歴情報Bean
	    		if(i==0){
					// 処理日時タイトル
					if(!check.isNullBlank(rs.getString(DT_TITLE))){
						form.setDt_title(GS.KAKKO_HIDARI + rs.getString(DT_TITLE) + GS.KAKKO_MIGI);
					}
	    		}
	    		// id
	    		rirekiBean.setId(Function.getStringOfInt(i));
				// 案件Ｎｏ．
	    		rirekiBean.setAnken_no(rs.getString(ANKEN_NO));
				// 担当ユーザID
	    		rirekiBean.setTanto_user_id(rs.getString(TANTO_USER));
				// 査定会社コード
	    		rirekiBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
				// 分類２
	    		rirekiBean.setBunrui2(rs.getString(BUNRUI2));
				// 担当者名
	    		rirekiBean.setTanto_nm(rs.getString(TANTO_USER_NM));
				// 組織
	    		rirekiBean.setSoshiki(rs.getString(SOSHIKI));
				// 入力区分
	    		rirekiBean.setNyuryoku_kbn(rs.getString(OPE_KBN));
				// フェーズ
	    		rirekiBean.setPhase(rs.getString(PHASE));
				// フェーズ名称
	    		rirekiBean.setPhase_nm(rs.getString(PHASE_NM));
				// ステータス
				if(OPE80.equals(rs.getString(OPE_KBN))){
					rirekiBean.setStatus(GS.STATUS_SYONIN_MACHI);
				}else{
					rirekiBean.setStatus(GS.STATUS_SYORICHU);
				}
				// 処理
				rirekiBean.setSyori(rs.getString(SYORI));
				// 処理日時
				rirekiBean.setSyori_dt(rs.getString(SYORI_DT));
				// 履歴情報を格納
				rireki_list.add(i, rirekiBean);
	    		i++;
	    	}
			//フェーズ選択ラジオボタン
			listBean.setRadio_id(GS.EMPTY_CHARCTER);
			//組織選択チェックボックス
			listBean.setChk_id(GS.EMPTY_CHARCTER);
		    // ActionForm に明細を格納
			listBean.setRireki_list(rireki_list);
			ar_meisai.add(0,listBean);
		    form.setAr_rireki(ar_meisai);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 差戻先一覧取得(査定から滞留判定に差戻時) <br>
	 * 
	 * @exception SQLException
	 */
	public void getTairyu_sashimodoshisaki2() throws SQLException {
		InputCheck check = new InputCheck();
		List<RirekiListBean> ar_meisai = new ArrayList<RirekiListBean>();	// 明細配列	
        for (int i = 0; i < form.getAr_tairyu_anken_no().size(); i++) {
        	List<String> ankenList = form.getAr_tairyu_anken_no();
    		ResultSet rs = null;
    		boolean soshiki_chk = false;

    		//ExCallableStatement生成
    		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_RIREKI00, sqlExec);
    		exCstmt.setStringIn(ankenList.get(i));
    		exCstmt.setStringIn(comLangMode);
    		exCstmt.setStringIn(systemkbn);
    		exCstmt.setResultSet(RESULTSET);

    		try {
    			//SQL実行
    			exCstmt.execute();
    			isError(exCstmt);
    			rs = exCstmt.getResultSet(RESULTSET);
    			// ActionForm に取得値を格納
    			RirekiListBean listBean = null;			// 履歴リストBean(チェックボックス判定用)
    			List<RirekiBean> rirekilist = null;		// 履歴リスト
			
    			int j = 0;
    			while ( rs.next() ) {
    				RirekiBean rirekiBean = new RirekiBean();	//履歴情報Bean
    				if(j==0){
    					soshiki_chk = true;
    					listBean = new RirekiListBean();
    					rirekilist = new ArrayList<RirekiBean>();
    					rirekilist.add(j, rirekiBean);
    					listBean.setRadio_id(GS.EMPTY_CHARCTER);    //フェーズ選択ラジオボタン
    					listBean.setChk_id(GS.EMPTY_CHARCTER);    	//組織選択チェックボックス
    					listBean.setRireki_list(rirekilist);
       	       		}else{
    					soshiki_chk = false;
    					rirekilist.add(j, rirekiBean);
    				}
   					// 処理日時タイトル
   					if(!check.isNullBlank(rs.getString(DT_TITLE))){
   						form.setDt_title(GS.KAKKO_HIDARI + rs.getString(DT_TITLE) + GS.KAKKO_MIGI);
    	    		}
    	    		// id
   					rirekiBean.setId(Function.getStringOfInt(j));
    				// 案件Ｎｏ．
					rirekiBean.setAnken_no(rs.getString(ANKEN_NO));
    				// 担当ユーザID
					rirekiBean.setTanto_user_id(rs.getString(TANTO_USER));
    				// 査定会社コード
					rirekiBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
    				// 分類２
					rirekiBean.setBunrui2(rs.getString(BUNRUI2));
    				// 担当者名
					rirekiBean.setTanto_nm(rs.getString(TANTO_USER_NM));
    				// 組織
					rirekiBean.setSoshiki(rs.getString(SOSHIKI));
    				// 入力区分
					rirekiBean.setNyuryoku_kbn(rs.getString(OPE_KBN));
    				// フェーズ
					rirekiBean.setPhase(rs.getString(PHASE));
    				// フェーズ名称
					rirekiBean.setPhase_nm(rs.getString(PHASE_NM));
    				// ステータス
    				if(OPE80.equals(rs.getString(OPE_KBN))){
    					rirekiBean.setStatus(GS.STATUS_SYONIN_MACHI);
    				}else{
    					rirekiBean.setStatus(GS.STATUS_SYORICHU);
    				}
    				// 処理
    				rirekiBean.setSyori(rs.getString(SYORI));
    				// 処理日時
    				rirekiBean.setSyori_dt(rs.getString(SYORI_DT));
    				// 組織選択チェック表示判定
    				rirekiBean.setSoshiki_chk(soshiki_chk);
        			//TDスタイル設定
    				rirekiBean.setTd_style(RIGHT);
    				j++;
    			}
    			if(j>0){
    			//TDスタイル設定
    			rirekilist.get(j-1).setTd_styleBottom(BOTTOM);
    			rirekilist.get(j-1).setTd_style(BOTTOM_RIGHT);
    			}
    			//案件単位でBeanを格納
				ar_meisai.add(i, listBean);
    		}finally {
				if (rs != null) {
					rs.close();
				}
			}
        }
        //アクションフォームに格納
		form.setAr_rireki(ar_meisai);
	}
	
	/**
	 * 差戻先一覧取得(査定フェーズ、引当金確認フェーズ差戻時) <br>
	 * 
	 * @exception SQLException
	 */
	public void getSatei_sashimodoshisaki() throws SQLException {
		ResultSet rs = null;
		InputCheck check = new InputCheck();
		String phase = tori_bean.getPhase();

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_RIREKI02, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			List<RirekiListBean> ar_meisai = new ArrayList<RirekiListBean>();	// 明細配列
	    	RirekiListBean listBean = new RirekiListBean();						// 履歴リストBean(チェックボックス判定用)
	    	List<RirekiBean> rireki_list = new ArrayList<RirekiBean>();			// 履歴リスト
	    	int i = 0;
	    	while (rs.next()) {
	    		boolean clear_flg = false;
	    		
		    	//取引先情報のフェーズが対象先選定の場合
	    		if(phase.equals(GS.PHASE_TAISHOSAKI_SENTEI) && !phase.equals(rs.getString(PHASE))){
    				//取得したフェーズが対象先選定以外の場合、明細をクリア
    				clear_flg = true;

        			//取引先情報のフェーズが仮基準査定追加の場合
	    		}else if(phase.equals(GS.PHASE_KARIKIJUN_SATEI_TUIKA) && !phase.equals(rs.getString(PHASE))){
    				//取得したフェーズが仮基準査定追加以外の場合、明細をクリア
    				clear_flg = true;

    			//取引先情報のフェーズがクレーム債権再設定の場合
	    		}else if(phase.equals(GS.PHASE_KUREMU_SAIKEN_SAISETTEI) && !phase.equals(rs.getString(PHASE))){
    				//取得したフェーズがクレーム債権再設定以外の場合、明細をクリア
    				clear_flg = true;

	    		//取引先情報のフェーズが査定の場合
	    		}else if(PHASE_SATEI.equals(form.getJishi_phase())){
		    		if(!GS.PHASE_ICHIJI_SATEI.equals(rs.getString(PHASE)) && !GS.PHASE_ICHIJI_SATEI_KENSYO.equals(rs.getString(PHASE)) && !GS.PHASE_NIJI_SATEI.equals(rs.getString(PHASE))) {
		    			//取得したフェーズが査定以外の場合、明細をクリア
		    			clear_flg = true;
		    		}else if(Integer.parseInt(phase) < Integer.parseInt(rs.getString(PHASE))){
		    			//取引先情報のフェーズより先のフェーズの履歴情報は取得しない
		    			continue;
		    		}
	    		}

    			if(clear_flg){
    				//明細クリア
    				rireki_list.clear();
	    			i = 0;
	    			continue;
    			}
	    		RirekiBean rirekiBean = new RirekiBean();
	    		if(i==0){
					// 処理日時タイトル
					if(!check.isNullBlank(rs.getString(DT_TITLE))){
						form.setDt_title(GS.KAKKO_HIDARI + rs.getString(DT_TITLE) + GS.KAKKO_MIGI);
					}
	    		}
	    		// id
	    		rirekiBean.setId(Function.getStringOfInt(i));
				// 案件Ｎｏ．
	    		rirekiBean.setAnken_no(rs.getString(ANKEN_NO));
				// 担当ユーザID
	    		rirekiBean.setTanto_user_id(rs.getString(TANTO_USER));
				// 査定会社コード
	    		rirekiBean.setSateikaisya_cd(rs.getString(SATEI_KAISHA_CD));
				// 分類２
	    		rirekiBean.setBunrui2(rs.getString(BUNRUI2));
				// 担当者名
	    		rirekiBean.setTanto_nm(rs.getString(TANTO_USER_NM));
				// 組織
	    		rirekiBean.setSoshiki(rs.getString(SOSHIKI));
				// 入力区分
	    		rirekiBean.setNyuryoku_kbn(rs.getString(OPE_KBN));
				// フェーズ
	    		rirekiBean.setPhase(rs.getString(PHASE));
				// フェーズ名称
	    		rirekiBean.setPhase_nm(rs.getString(PHASE_NM));
				// ステータス
				if(OPE80.equals(rs.getString(OPE_KBN))){
					rirekiBean.setStatus(GS.STATUS_SYONIN_MACHI);
				}else{
					rirekiBean.setStatus(GS.STATUS_SYORICHU);
				}
				// 処理
				rirekiBean.setSyori(rs.getString(SYORI));
				// 処理日時
				rirekiBean.setSyori_dt(rs.getString(SYORI_DT));
				// 履歴情報を格納
				rireki_list.add(i, rirekiBean);
	    		i++;
	    	}
			//フェーズ選択ラジオボタン
			listBean.setRadio_id(GS.EMPTY_CHARCTER);
			//組織選択チェックボックス
			listBean.setChk_id(GS.EMPTY_CHARCTER);
		    // ActionForm に明細を格納
			listBean.setRireki_list(rireki_list);
			ar_meisai.add(0,listBean);
		    form.setAr_rireki(ar_meisai);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 差戻区分セレクトボックス設定値取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSashi_kbn() throws SQLException {
		ResultSet rs = null;
		try{
			//ResultSet取得
			rs = getKbnval(PHASE,systemkbn,comLangMode);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sashikbn = new LinkedHashMap<String,String>();
			while (rs.next()) {
				//仮基準査定追加の場合
				if(KARITUIKA_MODE.equals(form.getGamen_mode())){
					//仮基準査定追加を格納する
					if(GS.PHASE_KARIKIJUN_SATEI_TUIKA.equals(rs.getString(KBN_VAL))){
						ar_sashikbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
					}
				//現フェーズが一次二次査定、且つ滞留判定データが存在する　または現フェーズがクレーム債権再設定の場合
				//対象先選定のみ格納する
				}else if(((GS.PHASE_ICHIJI_SATEI.equals(phase) || GS.PHASE_ICHIJI_SATEI_KENSYO.equals(phase) || GS.PHASE_NIJI_SATEI.equals(phase)) && form.isTairyuFlg())
					|| GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(phase)){
					if(GS.PHASE_TAISHOSAKI_SENTEI.equals(rs.getString(KBN_VAL))){
						ar_sashikbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
					}
				}else{
					//上記以外　対象先選定とクレーム債権再設定を格納する
					if(GS.PHASE_TAISHOSAKI_SENTEI.equals(rs.getString(KBN_VAL)) || GS.PHASE_KUREMU_SAIKEN_SAISETTEI.equals(rs.getString(KBN_VAL))){
						ar_sashikbn.put(rs.getString(KBN_HYOUJI_VAL),rs.getString(KBN_VAL));
					}
				}
			}
			form.setAr_Sashikbn(ar_sashikbn);
		} finally {
	    	if (rs != null) {
    			rs.close();
    		}
	    }
	}
	
	/**
	 * 差戻フェーズセレクトボックス設定値取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSashiphase() throws SQLException {
		ResultSet rs = null;
		boolean tairyuset_flg = false;

		//査定から滞留判定フェーズに差戻可能か判定
		if(form.isTairyuFlg() && GS.OFF.equals(user_bean.getComJimukyoku_sashi_flg()) && !form.isHikiateFlg()){
			tairyuset_flg = true;
		}

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_PHASE, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(comLangMode);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			LinkedHashMap<String,String> ar_sashiphase = new LinkedHashMap<String,String>();
			int i = 0;
			while ( rs.next() ) {

				//査定から滞留判定に差戻不可の場合、滞留判定フェーズは格納しない
				if(phase.equals(GS.PHASE_ICHIJI_SATEI) || phase.equals(GS.PHASE_ICHIJI_SATEI_KENSYO) || phase.equals(GS.PHASE_NIJI_SATEI)){
					if(!tairyuset_flg && (GS.PHASE_TAIRYU_HANTEI.equals(rs.getString(PHASE)) || GS.PHASE_TAIRYU_HANTEI_KENSHO.equals(rs.getString(PHASE)))){
						continue;
					}
				}
				//初期設定
				if(i==0){
					form.setSashiphase(rs.getString(PHASE));
				}
				ar_sashiphase.put(rs.getString(PHASE_NM),rs.getString(PHASE));
				i++;
			}
			form.setAr_Sashiphase(ar_sashiphase);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 汎用２セレクトボックス設定値取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getHanyo2() throws SQLException {
		ResultSet rs = null;
		String hanyo2 = Function.trim(tori_bean.getBunrui2());
		LinkedHashMap<String,String> ar_hanyo2 = new LinkedHashMap<String,String>();

		//初期設定
		form.setHanyou2(hanyo2);
		
		//海外の場合、差戻対象案件の分類2のみ格納
		if(!GS.GSS.equals(systemkbn)){
			ar_hanyo2.put(hanyo2,hanyo2);
			form.setAr_Hanyou2(ar_hanyo2);
			return;
		}

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_M1200, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				//海外または差戻フェーズが二次査定の場合、差戻対象案件の分類2のみ格納
				if(GS.GSS.equals(systemkbn) && GS.PHASE_NIJI_SATEI.equals(form.getSashiphase())){
					if(!hanyo2.equals(rs.getString(BUMON_CD))){
						continue;
					}
				}
				ar_hanyo2.put(rs.getString(BUMON_NM),rs.getString(BUMON_CD));	
			}
			form.setAr_Hanyou2(ar_hanyo2);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
	/**
	 * 汎用３セレクトボックス設定値取得 <br>
	 *
	 * @exception SQLException
	 */
	public void getHanyo3() throws SQLException {
		ResultSet rs = null;
		String hanyo3 = Function.trim(tori_bean.getBunrui3());
		String buCd = Function.trim(tori_bean.getBu_cd());
		String firstBuCd = "";
		LinkedHashMap<String,String> ar_hanyo3 = new LinkedHashMap<String,String>();

		//初期設定
		form.setHanyou3(hanyo3);

		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OS_SELECT_M1200_2, sqlExec);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			// ActionForm に取得値を格納
			while ( rs.next() ) {
				//汎用２（右）セレクトボックスで選択された分類２に紐付く分類３のみ格納
				ar_hanyo3.put(rs.getString(BU_NM),rs.getString(BU_CD));
				if (firstBuCd.length() == 0) {
					//セレクトボックス一番目の部コード
					firstBuCd = Function.trim(rs.getString(BU_CD));
				}
			}
			// 初期値設定（SJ)
			if(GS.GSS.equals(tori_bean.getSystem_kbn()) && 
			   GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
				if (ar_hanyo3.containsValue(buCd)) {
					form.setHanyou3(buCd);
				} else {
					form.setHanyou3(firstBuCd);
				}
			}
			form.setAr_Hanyou3(ar_hanyo3);
	    } finally {
	    	if (rs != null) {
    			rs.close();
	    	}
	    }
	}
	
    /**
     * 担当者一覧を表示 <br>
     * 
     * @exception SQLException
     */
    public void getTantoIchiran() throws SQLException {
		// 担当者一覧【配列】
		List<Map> ar_tanto = new ArrayList<Map>();

		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_TANTO, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setStringIn(form.getSashiphase());
		exCstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAILＡｄｄｒｅｓｓ
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
			}
			form.setAr_Tanto(ar_tanto);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 担当者一覧（本部単位）を表示 <br>
     * 
     * @exception SQLException
     */
    public void getTantoIchiran2() throws SQLException {
		// 担当者一覧【配列】
		List<Map> ar_tanto = new ArrayList<Map>();

		// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_TANTO_HONBU, sqlExec);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(form.getHanyou2());
		exCstmt.setStringIn(form.getSashiphase());
		exCstmt.setStringIn(form.getHanyou3());
		exCstmt.setStringIn(tori_bean.getTaisyo_ym());
		exCstmt.setResultSet(RESULTSET);

		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			// ActionForm に取得値を格納
			int i = 0;
			while (rs.next()) {
				// 担当者情報
				Map<String, String> tanto = new HashMap<String, String>();
				// E-MAILＡｄｄｒｅｓｓ
				tanto.put(EMAIL_ADDR, rs.getString(EMAIL_ADDR));
				// 統合ID
				tanto.put(TOGO_ID, rs.getString(TOGO_ID));
				// 明細配列に取得情報を格納
				ar_tanto.add(i, tanto);
				i++;
			}
			form.setAr_Tanto(ar_tanto);
		} finally {
			if (rs != null) {
				rs.close();
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
    public boolean isUserKengen(RirekiBean rirekiBean) throws SQLException {
    	boolean result = false;
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_KENGEN, sqlExec);
		exCstmt.setStringIn(rirekiBean.getTanto_user_id());
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(rirekiBean.getPhase());
		exCstmt.setStringIn(rirekiBean.getStatus());
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
     * ユーザ参照権限チェック <br>
     * 
     * @param RirekiBean
	 * @return boolean
     * @exception SQLException
     */
    public boolean isUserSansyoKengen(RirekiBean rirekiBean) throws SQLException {
    	boolean result = false;
    	// ExCallableStatement生成
    	ResultSet rs = null;
    	ExCallableStatement cstmt = null;
		cstmt = new ExCallableStatement(SP_SS_O_SELECT_SANSYO,sqlExec);
		cstmt.setStringIn(rirekiBean.getTanto_user_id());
		cstmt.setStringIn(satei_kaisha_cd);
		cstmt.setStringIn(tori_bean.getBunrui2());
		cstmt.setStringIn(tori_bean.getBu_cd());
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
     * 担当者の所属組織取得 <br>
     * 
     * @exception SQLException
     */
    public void getTanto_Syozoku() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_SYOZOKU, sqlExec);
		exCstmt.setStringIn(form.getSelectedTantoId());
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setResultSet(RESULTSET);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			if(rs.next()) {
				form.setTanto_bumon_cd(rs.getString(BUMON_CD));
				form.setTanto_bu_cd(rs.getString(BU_CD));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T08_滞留判定進捗管理の更新 <br>
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @exception SQLException
     */
    public void updT08(String upd_phase,String upd_status,String upd_hoji_user,String upd_anken_no,String upd_bunrui2,String upd_bu_cd) throws SQLException {
		InputCheck check = new InputCheck();
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ_UPDATE_T0800, sqlExec);
		exCstmt.setStringIn(upd_anken_no);
		exCstmt.setStringIn(upd_phase);
		exCstmt.setStringIn(upd_status);
		exCstmt.setStringIn(upd_hoji_user);
		exCstmt.setStringIn(SASHIMODOSHI);
		exCstmt.setStringIn(upd_bunrui2);
		exCstmt.setStringIn(upd_bunrui2);
		exCstmt.setStringIn(upd_bu_cd);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(GS.ON);
		exCstmt.setStringIn(comDaiko_userId);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }
		if(form.isBu_cd_upd_flg()){
			exCstmt.setStringIn(GS.TRUE_CHARCTER);
		}else{
			exCstmt.setStringIn(GS.FALSE_CHARCTER);
		}
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T10_滞留判定の削除 <br>
     * 
     * @param String
     * @param String
     * @exception SQLException
     */
    public void delT10(String anken_no,String sashiphase) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_DELETE_T1000, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T14_査定進捗管理の更新 <br>
     * 
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @param String
     * @exception SQLException
     */
    public void updT14(String upd_phase,String upd_status,String upd_hoji_user,String upd_bunrui2,String upd_bu_cd) throws SQLException {
		InputCheck check = new InputCheck();
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ_UPDATE_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(upd_phase);
		exCstmt.setStringIn(upd_status);
		exCstmt.setStringIn(upd_hoji_user);
		exCstmt.setStringIn(SASHIMODOSHI);
		exCstmt.setStringIn(upd_bunrui2);
		exCstmt.setStringIn(upd_bunrui2);
		exCstmt.setStringIn(upd_bu_cd);
		exCstmt.setStringIn(null);
		exCstmt.setStringIn(GS.ON);
		exCstmt.setStringIn(comDaiko_userId);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }
		if(form.isBu_cd_upd_flg()){
			exCstmt.setStringIn(GS.TRUE_CHARCTER);
		}else{
			exCstmt.setStringIn(GS.FALSE_CHARCTER);
		}
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 抽出事由判定 <br>
     * 
     * @exception SQLException
     */
    public int selJiyu() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_SELECT_JIYU, sqlExec);
		exCstmt.setStringIn(tori_bean.getKijunbi_kbn());
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(taisyo_ym);
		exCstmt.setStringIn(tori_bean.getSyori_kaisu());
		exCstmt.setStringIn(tori_bean.getHanki_sihanki_kbn());
		exCstmt.setStringIn(tori_cd);
		exCstmt.setIntOut(CNT);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
			int cnt = exCstmt.getInt(CNT);
			return cnt;
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T01_対象先の更新 <br>
     * 
     * @param String
     * @exception SQLException
     */
    public void updT01(String ichiji_flg) throws SQLException {
		InputCheck check = new InputCheck();
    	// ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_UPDATE_T0100, sqlExec);
		exCstmt.setStringIn(ichiji_flg);
		if(check.isNullBlank(comDaiko_userId)){
	        exCstmt.setStringIn(userId);        
        } else {
        	exCstmt.setStringIn(comDaiko_userId);
        }
		exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(tori_cd);
       	exCstmt.setStringIn(tori_bean.getHanki_sihanki_kbn());
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T14_査定進捗管理の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT14() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_DELETE_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T15_一次二次査定の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT15(String delete_id,String sashiphase) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1500, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		exCstmt.setStringIn(delete_id);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T16_引当金検討対象BS明細の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT16() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1600, sqlExec);
		exCstmt.setStringIn(anken_no);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T17_引当金判定表示用の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT17() throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1700, sqlExec);
		exCstmt.setStringIn(anken_no);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T19_留保債務の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT19(String delete_id,String sashiphase) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T1900, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		exCstmt.setStringIn(delete_id);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T20_第三者留保債務の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT20(String delete_id,String sashiphase) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_DELETE_T2000, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		exCstmt.setStringIn(delete_id);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T11_文書添付の削除 <br>
     * 
     * @exception SQLException
     */
    public void delT11(String delete_id,String sashiphase,String phase,String ankenNo) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_DELETE_T1100, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		exCstmt.setStringIn(delete_id);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T13_入力履歴の登録 <br>
     * 
     * @param String
     * @exception SQLException
     */
    public void insT13(String hanteiKbn) throws SQLException {

	    // INパラメータ取得
        String comUser_Nm = user_bean.getComUser_Nm();							// 担当者名日本語
        String comUser_Nm_En = user_bean.getComUser_Nm_En();					// 担当者名英語
        String comSyozokuSoshiki_Nm = user_bean.getComSyozokuSoshiki_Nm();		// 所属部署名日本語
        String comSyozokuSoshiki_Nm_En = user_bean.getComSyozokuSoshiki_Nm_En();// 所属部署名英語
        String comDaiko_user_nm = user_bean.getComDaiko_user_nm();				// 代行者名日本語
        String comDaiko_user_nm_en = user_bean.getComDaiko_user_nm_en();		// 代行者名英語
		//課題No.222
		//追加開始
		List ar_rireki = form.getAr_rireki();
        String gamenMode = form.getGamen_mode();								// 画面モード
        String sahiType = form.getSahimodoshiType();							// 差戻タイプ
        String iraisakiUserId = GS.EMPTY_CHARCTER;								// 依頼先ユーザID
        if(SINKI_MODE.equals(gamenMode)){
        	iraisakiUserId = form.getSelectedTantoId();
        }else if(!JIMU_MODE.equals(gamenMode) && !KARITUIKA_MODE.equals(gamenMode) && !TYPE3.equals(sahiType)){
        	RirekiListBean listBean = (RirekiListBean)ar_rireki.get(0);
			if(!GS.EMPTY_CHARCTER.equals(listBean.getRadio_id())){
				int id = Integer.parseInt(listBean.getRadio_id());
				List<RirekiBean> rireki_list = listBean.getRireki_list();
				RirekiBean rirekiBean = rireki_list.get(id);
				iraisakiUserId = rirekiBean.getTanto_user_id();
			}
		}
        //追加完了

		//ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1300, sqlExec);
		exCstmt.setStringIn(anken_no);
        exCstmt.setStringIn(hanteiKbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(userId);
        exCstmt.setStringIn(comUser_Nm);
        exCstmt.setStringIn(comUser_Nm_En);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm);
        exCstmt.setStringIn(comSyozokuSoshiki_Nm_En);
        exCstmt.setStringIn(phase);
        exCstmt.setStringIn(NYURYOKU_KBN);
        exCstmt.setStringIn(userId);
       	exCstmt.setStringIn(comDaiko_userId);
       	exCstmt.setStringIn(comDaiko_user_nm);
       	exCstmt.setStringIn(comDaiko_user_nm_en);
        exCstmt.setStringIn(GS.COMMENT_VAL_99);
        exCstmt.setStringIn(form.getSashi_comment());
        exCstmt.setStringIn(null);
        exCstmt.setStringIn(null);
		//課題No.222
		//修正開始
        //exCstmt.setStringIn(null);
        exCstmt.setStringIn(iraisakiUserId);
		//修正完了
		
		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
    }
 
    /**
     * T12_コメントの削除 <br>
     * 
     * @exception SQLException
     */
    public void delT12(String delete_id,String sashiphase,String phase,String ankenNo) throws SQLException {
    	// ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_DELETE_T1200, sqlExec);
		exCstmt.setStringIn(ankenNo);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(sashiphase);
		exCstmt.setStringIn(delete_id);
		try {
			// SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * T12_コメントの登録 <br>
     * 
	 * @param String
     * @param String
     * @exception SQLException
     */
    public void insT12(String ins_anken_no,String ins_phase) throws SQLException {

		//ExCallableStatement生成
    	ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_INSERT_T1200, sqlExec);
		exCstmt.setStringIn(ins_anken_no);
        exCstmt.setStringIn(ins_phase);
        exCstmt.setStringIn(GS.COMMENT_VAL_99);
        exCstmt.setStringIn(TOROKU_POINT);
        exCstmt.setStringIn(form.getSashi_comment());
        exCstmt.setStringIn(userId);
       	exCstmt.setStringIn(comDaiko_userId);

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
    }
 
    /**
	 * T04_メール配信の登録 <br>
	 * 
     * @param  String
     * @param  String
     * @param  String
     * @param  String
     * @param  String
	 * @exception SQLException
	 */
	public void insT04(String ins_anken_no,String ins_phase,String ins_status,String ins_bumon,String ins_bu,String ins_tanto) throws SQLException {
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_INSERT_T0400_2, sqlExec);
        exCstmt.setStringIn(userId);
		exCstmt.setStringIn(ins_anken_no);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(ins_bumon);
		if(GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			exCstmt.setStringIn(ins_bu);
		}else{
			exCstmt.setStringIn(SPACE);
		}
        exCstmt.setStringIn(ins_phase);
        exCstmt.setStringIn(ins_status);
        exCstmt.setStringIn(ins_tanto);
        exCstmt.setStringIn(HAISINZUMI_FLG);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
	 * T04_メール配信の登録（新規差戻先選択時） <br>
	 * 
     * @param  String
     * @param  String
     * @param  String
     * @param  String
     * @param  String
     * @param  String
	 * @exception SQLException
	 */
	public void insT04_2(String ins_anken_no,String ins_phase,String ins_status,String ins_bumon,String ins_bu,String ins_tanto) throws SQLException {
		InputCheck check = new InputCheck();
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_INSERT_T0400_2, sqlExec);
        exCstmt.setStringIn(userId);
		exCstmt.setStringIn(ins_anken_no);
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(ins_bumon);
		exCstmt.setStringIn(ins_bu);
        exCstmt.setStringIn(ins_phase);
        exCstmt.setStringIn(ins_status);
        exCstmt.setStringIn(ins_tanto);
        exCstmt.setStringIn(HAISINZUMI_FLG);
		if(check.isNullBlank(comDaiko_userId)){
            // 通常時
	        exCstmt.setStringIn(userId);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}
	
	
    /**
     * T14_査定進捗管理の作成 <br>
     * @exception SQLException
     */
    public String insT14(String kariYm) throws SQLException {
	    String result = null;
    	// ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_INSERT_T1400, sqlExec);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(systemkbn);
		exCstmt.setStringIn(sateiki);
		exCstmt.setStringIn(kariYm);
		exCstmt.setStringIn(comDaiko_userId);
	    exCstmt.setStringIn(userId);
		exCstmt.setStringOut(ANKEN_NO);
		// SQL実行
		exCstmt.execute();
		isError(exCstmt);
    	result = exCstmt.getString(ANKEN_NO);
    	return result;
	}
    
	/**
	 * 初回月対象年月取得 <br>
     * @return String 対象年月
	 * @exception SQLException
	 */
	public String getKariYm() throws SQLException {

    	ExCallableStatement cstmt = null;
    	ResultSet rs = null;
    	String result = GS.EMPTY_CHARCTER;
		//ExCallableStatement生成
		cstmt = new ExCallableStatement(SP_SS_O_SELECT_M2200, sqlExec);
		cstmt.setStringIn(sateiki);
		cstmt.setStringIn(systemkbn);
		cstmt.setStringIn(comLangMode);
		cstmt.setResultSet(RESULTSET);
	    
	    try {
			//SQL実行	
	    	cstmt.execute();
	    	isError(cstmt);
			rs = cstmt.getResultSet(RESULTSET);

	    	// ActionForm に取得値を格納
	    	while ( rs.next() ) {
	    		if(KIJUNBI_KBN_1.equals(rs.getString(KIJUNBI_KBN))){
	    			result = rs.getString(TAISHO_YM);
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
	 * T04_メール配信の登録 <br>
	 * 
     * @param  String
     * @param  String
     * @param  String
	 * @exception SQLException
	 */
	public void insT04(String kariAnkenNo,String kariYm,String mailStat) throws SQLException {
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_INSERT_T0400_2, sqlExec);
        exCstmt.setStringIn(userId);
		exCstmt.setStringIn(kariAnkenNo);
        exCstmt.setStringIn(kariYm);
        exCstmt.setStringIn(satei_kaisha_cd);
		exCstmt.setStringIn(tori_bean.getBunrui2());
		if(GS.SATEIKAISYA_SJ.equals(tori_bean.getSateikaisya_cd())){
			exCstmt.setStringIn(tori_bean.getBu_cd());
		}else{
			exCstmt.setStringIn(SPACE);
		}
        exCstmt.setStringIn(form.getSashikbn());
        exCstmt.setStringIn(mailStat);
        exCstmt.setStringIn(GS.EMPTY_CHARCTER);
        exCstmt.setStringIn(HAISINZUMI_FLG);
		if(GS.EMPTY_CHARCTER.equals(Function.trim(comDaiko_userId))){
            // 通常時
	        exCstmt.setStringIn(userId);
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }

		try {
			//SQL実行
			exCstmt.execute();
			isError(exCstmt);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

    /**
     * 退避情報の削除 <br>
     * 
     * @param  String
     * @exception SQLException
     */
    public void delTaihi(String kariYm) throws SQLException {
        
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OB2102_DELETE_TAIHI, sqlExec);
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(satei_kaisha_cd);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(kariYm);
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
        exCstmt.setStringIn(tori_cd);
        
        try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }

    /**
     * 退避情報の削除 <br>
     * 
     * @param	String
     * @exception SQLException
     */
    public void insTaihi(String kariYm) throws SQLException {
        
		//ExCallableStatement生成
		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ2101_INSERT_TAIHI, sqlExec);
        exCstmt.setStringIn(kariYm);
		if(GS.EMPTY_CHARCTER.equals(Function.trim(comDaiko_userId))){
            // 通常時
	        exCstmt.setStringIn(userId);        
        } else {
            // 代行時
        	exCstmt.setStringIn(comDaiko_userId);
        }
        exCstmt.setStringIn(taisyo_ym);
        exCstmt.setStringIn(tori_bean.getShikibetu_cd());
        exCstmt.setStringIn(tori_bean.getTogo_tori_cd());
        exCstmt.setStringIn(tori_bean.getSyori_kaisu());
        exCstmt.setStringIn(systemkbn);
        exCstmt.setStringIn(mise_cd);
        exCstmt.setStringIn(tori_cd);
        exCstmt.setStringIn(satei_kaisha_cd);
        
		try {
            // SQL実行
        	exCstmt.execute();
            isError(exCstmt);
        } finally {
            if (rs != null) {
            	rs.close();
            }
        }
    }
}