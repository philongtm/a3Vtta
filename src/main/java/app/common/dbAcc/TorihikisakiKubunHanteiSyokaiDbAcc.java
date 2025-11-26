/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.dbAcc;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.form.TorihikisakiKubunHanteiSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
* OZ6103_取引先区分判定照会タブ  DBアクセスクラス
*/
public class TorihikisakiKubunHanteiSyokaiDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;						// 機能共通セッション
	private TorihikisakiBean tori_bean = null;					// 取引先情報
	private TorihikisakiKubunHanteiSyokaiForm form = null;		// アクションフォーム
	private AppContext appContext = null;						// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String TAIRYU_KBN           = "tairyu_kbn";				//滞留区分
	private static final String TAIRYU_KBN_NM        = "tairyu_kbn_nm";			//滞留区分名称	
	private static final String SEIHO_CHK            = "seijo_chk";				//正常先チェック
	private static final String YOCHUI_CHK           = "yochui_chk";				//要注意先チェック
	private static final String TYOKA_CHK            = "tyoka_chk";				//実質債務超過チェック
	private static final String KANWA_CHK            = "kanwa_chk";				//弁済条件緩和チェック
	private static final String ENTAI_CHK            = "entai_chk";				//1年以上延滞チェック
	private static final String HASANHO_CHK          = "hasanho_chk";				//破産法適用チェック
	private static final String KAISHAHO_CHK         = "kaishaho_chk";			//会社法適用チェック
	private static final String KOSEHO_CHK           = "koseho_chk";				//会社構成法適用チェック
	private static final String SAISEHO_CHK          = "saiseho_chk";				//民事再生法適用チェック
	private static final String SHOBUN_CHK           = "shobun_chk";				//取引停止処分チェック
	private static final String SONOTA_CHK           = "sonota_chk";				//その他チェック
	private static final String SAIKEN_KBN_NM        = "saiken_kbn_nm2";			//債権区分
	private static final String TORIHIKISAKI_KBN     = "torihikisaki_kbn";		//取引先区分
	private static final String TORIHIKISAKI_KBN_NM  = "torihikisaki_kbn_nm2";	//取引先区分名称
	private static final String TOROKU_POINT         = "toroku_point";			//登録箇所
	private static final String COMMENT_VAL          = "comment_val";				//コメント内容

	private static final String SP_SS_OL_SELECT_T1500 	= "SP_SS_OL_SELECT_T1500";		//取引先概要・債権区分を取得
	private static final String SP_SS_OL_SELECT_T1200 	= "SP_SS_OL_SELECT_T1200";		//当画面のコメント類を取得
	
	// INパラメータ
	private String system_kbn;		// システム区分
	private String phase;			// フェーズ
	private String anken_no;		// 案件No

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public TorihikisakiKubunHanteiSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
	super(sqlExec, log);
	this.appContext = appcontext;

	//ビーン取得
	cmnData = appContext.getCMN();
	tori_bean = cmnData.getTori_bean();
	form = (TorihikisakiKubunHanteiSyokaiForm)appContext.getActionForm();
		
	//ビーンの値を変数に設定
	system_kbn = tori_bean.getSystem_kbn();
	phase = tori_bean.getPhase();
	anken_no = tori_bean.getAnken_no();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		system_kbn	= GS.EMPTY_CHARCTER;
		phase 		= GS.EMPTY_CHARCTER;
		anken_no 	= GS.EMPTY_CHARCTER;
	}

	/**
	 * 取引先概要・債権区分を取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getexecute() throws SQLException {
				
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1500, sqlExec);
	    exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(system_kbn);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);

			//ActionForm に取得値を格納
            if (rs.next()) {
				//滞留区分
				form.setTairyu_kbn(rs.getString(TAIRYU_KBN));
				//滞留区分名称
				form.setTairyu_kbn_nm(rs.getString(TAIRYU_KBN_NM));
				//正常先チェック
				form.setSeijo_chk(rs.getString(SEIHO_CHK));
				//要注意先チェック			
				form.seYochui_chk(rs.getString(YOCHUI_CHK));
				//実質債務超過チェック
				form.setTyoka_chk(rs.getString(TYOKA_CHK));
				//弁済条件緩和チェック
				form.setKanwa_chk(rs.getString(KANWA_CHK));
				//1年以上延滞チェック
				form.setEntai_chk(rs.getString(ENTAI_CHK));
				//破産法適用チェック
				form.setHasanho_chk(rs.getString(HASANHO_CHK));
				//会社法適用チェック
				form.setKaishaho_chk(rs.getString(KAISHAHO_CHK));
				//会社構成法適用チェック
				form.setKoseho_chk(rs.getString(KOSEHO_CHK));
				//民事再生法適用チェック
				form.setSaiseho_chk(rs.getString(SAISEHO_CHK));
				//取引停止処分チェック
				form.setShobun_chk(rs.getString(SHOBUN_CHK));
				//その他チェック
				form.setSonota_chk(rs.getString(SONOTA_CHK));
				//債権区分
				form.setSaiken_kbn(Function.trim(rs.getString(SAIKEN_KBN_NM)));
				//取引先区分
				form.setTorihikisaki_kbn(Function.trim(rs.getString(TORIHIKISAKI_KBN)));
				//取引先区分名称
				form.setTorihikisaki_kbn_nm(rs.getString(TORIHIKISAKI_KBN_NM));
            }	
            
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
	
	/**
	 * 当画面のコメントを取得<br>
	 * 
	 * @exception SQLException
	 */
	public void getcomment() throws SQLException {

		//登録箇所の編集
		StringBuffer toroku_point = new StringBuffer();
		toroku_point.append(GS.SINGLE_QUOTATION)
					.append(GS.COMMENT_VAL_20)
					.append(GS.SINGLE_QUOTATION)
					.append(GS.COMMA)
					.append(GS.SINGLE_QUOTATION)					
					.append(GS.COMMENT_VAL_30)
					.append(GS.SINGLE_QUOTATION)
					.append(GS.COMMA)
					.append(GS.SINGLE_QUOTATION)
					.append(GS.COMMENT_VAL_40)
					.append(GS.SINGLE_QUOTATION);

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_T1200, sqlExec);
	    exCstmt.setStringIn(anken_no);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(toroku_point.toString());
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
		    while ( rs.next() ) {
		    	if(GS.COMMENT_VAL_20.equals(rs.getString(TOROKU_POINT))){
		    		// 取引先区分判定根拠
		    		form.setComment_val_20(rs.getString(COMMENT_VAL));		    		
		    	}else if(GS.COMMENT_VAL_30.equals(rs.getString(TOROKU_POINT))){
		    		// 債権区分判定根拠
		    		form.setComment_val_30(rs.getString(COMMENT_VAL));		    		
		    	}else if(GS.COMMENT_VAL_40.equals(rs.getString(TOROKU_POINT))){
		    		// 債権区分判定発生経緯
		    		form.setComment_val_40(rs.getString(COMMENT_VAL));
		    	}
		    }
	    } finally {
	    	if (rs != null) {
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
}