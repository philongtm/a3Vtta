/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/13		SSC				1.5次版無理矢理流用
0002	09/11/19		SSC				課題No.130 代行者表示制御
******************************************************************************/
package app.common.dbAcc;

import app.SessionDataZen;
import app.common.form.SashimodoshiCommentForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* コメント画面DBアクセスクラス
*/
public class SashimodoshiCommentDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private SashimodoshiCommentForm form = null;	// アクションフォーム	
	
	// INパラメータ
	private String anken_no;	// 案件Ｎｏ．
	private String point;		// 登録箇所（コメントテーブルのＰＫ）

	//課題No.130
    //追加開始
	private static final String HAKI_TUIKA		= "4";
	private static final String DAIKO_USER_NM		= "daiko_user_nm";
	private static final String TANTO_USER_NM		= "tanto_user_nm";
	private static final String H_DAIKO			= "( 代行 ";
	private static final String H_DAIKO_EN		= "( proxy ";
    //追加完了
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SashimodoshiCommentDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException  {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (SashimodoshiCommentForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		anken_no = form.getAnken_no();
	    ///////////////////////////////////////////////
	    //障害票：508
	    //チェックイン日：2008/5/27
	    //対応者：SJA中島
	    //概要：対象先、追加コメントの登録ポイント追加対応。
	    ///////////////////////////////////////////////
		point = "('97','98','99')";
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    anken_no = null;
	}
	

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		ResultSet rs = null;

	    // SQL作成
	    StringBuffer sql = new StringBuffer();
		sql.append("SELECT T.sasi_ten_flg,");
		sql.append("T.kikan_tori_cd AS kanjo_cd, ");
		sql.append("TM.business_nm_kj AS kanjo_nm_kj, ");
		sql.append("TM.business_nm AS kanjo_nm_en, ");
		sql.append("CM.comment_val, ");
	    //課題No.130
	    //追加開始
		sql.append("CM.toroku_div, ");
	    //追加完了
	    sql.append("TU.USER_NM, ");
		// No628, 2008/06/06, SJA渡辺, 言語モード英語時にユーザ名称（英語）を表示するように修正
	    sql.append("TU.FIRST_NM || ' ' || TU.LAST_NM AS USER_NM_EN, ");
	    sql.append("KTH.kbn_hyouji_val ");
		if(GS.OB1102.equals(cmnData.getReturnId())){
	    	sql.append("FROM SST_TAIRYU_STAT T");
	    }else {
	    	sql.append("FROM SST_SATEI_STAT T");
	    }
	    
		sql.append(" LEFT JOIN SSE_TAIHI TT ON ");
		sql.append("TRIM(T.kikan_tori_cd) || '00' = TT.kikan_tori_cd ");
		sql.append("AND T.ym = TT.ym ");
		sql.append("AND T.mise_cd = TT.office_cd ");
		sql.append("AND T.system_kbn = TT.system_kbn ");
		sql.append("LEFT JOIN SSE_TOGO_MST TM ON ");
		sql.append("TT.togo_tori_cd = TM.togo_tori_cd ");
		sql.append("AND TT.ym = TM.ym ");
		sql.append("AND TT.sikibetu_cd = TM.sikibetu_cd ");
		sql.append("AND TT.syori_kaisu = TM.syori_kaisu ");
	    ///////////////////////////////////////////////
	    //障害票：508
	    //チェックイン日：2008/5/27
	    //対応者：SJA中島
	    //概要：査定結果詳細画面にて表示するコメントは、対象外、追加コメントのみ
		//     なので、コメントテーブルのフェーズは30固定。
	    ///////////////////////////////////////////////
		if(GS.OS6102.equals(cmnData.getReturnId())){
			sql.append("LEFT JOIN ( SELECT anken_no, phase, comment_val,upd_user,toroku_div,UPD_DT FROM SST_COMMENT WHERE toroku_div in ('4') AND toroku_point in").append(point).append(" order by UPD_DT desc ) CM ON ");
		}else{
			sql.append("LEFT JOIN ( SELECT anken_no, phase, comment_val,upd_user,toroku_div,UPD_DT FROM SST_COMMENT WHERE toroku_div in ('2','3','4') AND toroku_point in").append(point).append(" order by UPD_DT desc ) CM ON ");
		}
		sql.append("T.anken_no = CM.anken_no ");
	    ///////////////////////////////////////////////
	    //障害票：508
	    //チェックイン日：2008/5/27
	    //対応者：SJA中島
	    //概要：査定結果詳細画面にて表示するコメントは、対象外、追加コメントのみ
		//     なので、コメントテーブルのフェーズは30固定。
	    ///////////////////////////////////////////////
		if(GS.OS6102.equals(cmnData.getReturnId())){
			sql.append("AND CM.phase ='30' ");
		}else{
			sql.append("AND T.phase = CM.phase ");
		}
		sql.append("LEFT JOIN SSM_TM_USER_MST TU ON ");
		sql.append("CM.upd_user=TU.TOGO_ID ");
		sql.append("LEFT JOIN (SELECT kbn_val,kbn_hyouji_val FROM SSP_KBN WHERE system_kbn = '").append(cmnData.getSystem_kbn()).append("' AND kbn_key = 'comment_kbn' AND lang_mode ='").append(cmnData.getComLangMode()).append("') KTH ON CM.toroku_div = KTH.kbn_val ");
		sql.append("WHERE T.anken_no = '");
		sql.append(anken_no);
		sql.append("'");

	    try {
	    	// SQL実行		
	    	rs = sqlExec.execQuery(sql.toString());
	    	InputCheck check = new InputCheck();
	    	// ActionForm に取得値を格納
	    	if(rs.next()) {
	    		form.setKanjo_cd(rs.getString("kanjo_cd"));	// 勘定先ＣＤ
	    		if ("Ja".equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs.getString("kanjo_nm_kj")))) {
	    			form.setKanjo_nm(rs.getString("kanjo_nm_kj"));	// 勘定先名称
	    		} else {
	    			form.setKanjo_nm(rs.getString("kanjo_nm_en"));	// 勘定先名称
	    		}
	    		
	    		// No628, 2008/06/06, SJA渡辺, 言語モード英語時にユーザ名称（英語）を表示するように修正
	    		if ("Ja".equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs.getString("USER_NM")))) {
	    			form.setUser_nm(rs.getString("USER_NM")); // 登録ユーザ名
	    		} else {
	    			form.setUser_nm(rs.getString("USER_NM_EN")); // 登録ユーザ名
	    		}
	    		
	    		form.setToroku_div(rs.getString("kbn_hyouji_val")); // 登録区分
	    		
	    	    //課題No.130
	    	    //追加開始
	    		setDaikousya(cmnData.getComLangMode(),rs.getString("toroku_div"));
	    	    //追加完了
	    		
	    		// 取得した差戻コメントを100バイト区切りで改行を入れたものに整形
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
	    		
	    		form.setComment(rs.getString("comment_val"));	// 差戻コメント
	    		//辻褄合わせ処理
	    		form.setKanjo_cd(cmnData.getKanjo_cd());
	    		form.setKanjo_nm(cmnData.getKanjo_nm());
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
	
    //課題No.130
    //追加開始
	private void setDaikousya(String langMode,String torokuDiv) throws SQLException{
		ResultSet rs = null;
	    StringBuffer sql = new StringBuffer();
	    StringBuffer userName = new StringBuffer();
	    
	    sql.append("SELECT ")
	    	.append("NY.tanto_user_nm,")
	    	.append("NY.daiko_user_nm ")
	    	.append("FROM ")
	    	.append("(SELECT ")
	    	.append("PG_SS_FUNCTION.SF_SS_ISLANG(NR.tanto_user_nm,NR.tanto_user_nm_en,'").append(langMode).append("') AS tanto_user_nm,")
	    	.append("PG_SS_FUNCTION.SF_SS_ISLANG(NR.daiko_user_nm,NR.daiko_user_nm_en,'").append(langMode).append("') AS daiko_user_nm ")
	    	.append("FROM ")
	    	.append("SST_NYURYOKU_HIST NR ")
	    	.append("WHERE ")
	    	.append("NR.anken_no = '").append(anken_no).append("' ");
	    if(HAKI_TUIKA.equals(torokuDiv)){
		    sql.append("AND NR.phase = '30' ")
		    	.append("AND NR.ope_kbn IN ('10','70') ");
	    }else{
		    sql.append("AND NR.ope_kbn = DECODE(").append(torokuDiv).append(",'2','60','3','50',NULL) ");
	    }
	    sql.append("ORDER BY NR.syori_dt DESC) NY ")
	    	.append("WHERE ROWNUM = 1");

	    try {
	    	rs = sqlExec.execQuery(sql.toString());
	    	if(rs.next()){
	            if(!Function.trim(rs.getString(DAIKO_USER_NM)).equals(GS.EMPTY_CHARCTER)){
                	userName.append(rs.getString(TANTO_USER_NM));
	                if(langMode.equals(GS.LANG_JA)){
	                	userName.append(H_DAIKO);
	                }else{
	                	userName.append(H_DAIKO_EN);
	                }
	                userName.append(Function.trim(rs.getString(DAIKO_USER_NM)))
	                	.append(GS.KAKKO_MIGI);
		    		form.setUser_nm(userName.toString());
	            }
	    	}
	    } finally {
	    	if (rs != null) {
	    			rs.close();
	    	}
	    }
	}
    //追加完了
}