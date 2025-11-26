/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.dbAcc;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.TorihikisakiBean;
import app.common.form.TairyuMeisaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.ExCallableStatement;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
* OZ6101_滞留債権明細照会タブ  DBアクセスクラス
*/
public class TairyuMeisaiDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private TairyuMeisaiForm form = null;				// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String KBN_VAL              = "kbn_val";
	private static final String KBN_HYOUJI_VAL       = "KBN_HYOUJI_VAL";
	private static final String ANKEN_NO_EDA         = "anken_no_eda";
	private static final String KEIYAKU_DENPYO_NO    = "keiyaku_denpyo_no";
	private static final String KINGAKU              = "kingaku";
	private static final String TUUKA_CD             = "tuuka_cd";	
	private static final String KANJO_CD             = "kanjo_cd";
	private static final String HANYO1               = "hanyo1";
	private static final String SHUSI_DT             = "shusi_dt";
	private static final String SYORI_DT             = "syori_dt";
	private static final String HANTEI_JIYUU         = "hantei_jiyuu";
	private static final String TAIRYU_KBN           = "tairyu_kbn";
	private static final String TAIRYU_HANTEI_NM     = "tairyu_hantei_nm";
	private static final String SOSHIKI              = "soshiki";
	private static final String KANJO_NM             = "kanjo_nm";
	private static final String BUNSYO_NO            = "bunsyo_no";
	private static final String INVOICE_NO           = "invoice_no";
	private static final String KOMOKU1              = "komoku1";
	private static final String KOMOKU3              = "komoku3";
	private static final String KANJO_UCHI_CD        = "kanjo_uchi_cd";
	private static final String KANJO_UCHI_NM        = "kanjo_uchi_nm";
	
	private static final String SHOW            = "show";			//区分キー（表示件数）
	private static final String common_OZ       = "common_OZ";	//区分キー (汎用項目(ラベル)

	private static final String SP_SS_OZ6101_SELECT_ICHIRAN 	= "SP_SS_OZ6101_SELECT_ICHIRAN";	//明細の一覧【リスト】取得プロシージャ
	
	// INパラメータ
	private String system_kbn;			// 取引先情報のシステム区分
	private String phase;				// 取引先情報のフェーズ
	private String anken_no;			// 滞留判定案件NO.

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public TairyuMeisaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;
		//ビーン取得
		cmnData = appContext.getCMN();
		torihikisaki_bean = cmnData.getTori_bean();
		form = (TairyuMeisaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		system_kbn = torihikisaki_bean.getSystem_kbn();
		phase = torihikisaki_bean.getPhase();
		anken_no = torihikisaki_bean.getAnken_no();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		system_kbn = GS.EMPTY_CHARCTER;
		phase = GS.EMPTY_CHARCTER;
		anken_no = GS.EMPTY_CHARCTER;
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
			rs = getKbnval(SHOW,system_kbn,cmnData.getComLangMode());
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
	 * 一覧情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void getMeisai() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ6101_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setResultSet(RESULTSET);		
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
	    	
	    	// ActionForm に取得値を格納
	    	List<MeisaisyosaiBean> ar_meisai = new ArrayList<MeisaisyosaiBean>();	// 明細配列
	    	
	    	int i = 0;
	    	while ( rs.next() ) {
	    	  		
	    		MeisaisyosaiBean listBean = new MeisaisyosaiBean();
	    
				// id
				listBean.setId(Function.getStringOfInt(i));
				
				// 滞留判定案件枝番Ｎｏ．
				listBean.setAnken_no_eda(rs.getString(ANKEN_NO_EDA));

				// 勘定科目コード or 勘定科目名称 
				if (system_kbn.equals(GS.GSS)){
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_UCHI_CD));
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_UCHI_NM));
				}else{
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_CD));
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_NM));
				}
                // 金額計
				if(null != rs.getString(KINGAKU)){
					if (system_kbn.equals(GS.GSS)){
                    
						// 国内
						listBean.setKingaku_kei(Function.format("##,###,###,###,###,##0.##", Function.getValueOfDouble(rs.getString(KINGAKU))));
					}else{
                    
						// 海外
						listBean.setKingaku_kei(Function.format("##,###,###,###,###,##0.00", Function.getValueOfDouble(rs.getString(KINGAKU))));
					}     
				}
				// 汎用１
				listBean.setHanyo1(rs.getString(HANYO1));
				
				// 契約No.
				listBean.setKeiyaku_denpyo_no(rs.getString(KEIYAKU_DENPYO_NO));
				
				// インボイスNo
				listBean.setInvoice_no(rs.getString(INVOICE_NO));
				
				// 収支予定日
				listBean.setSyusi_yoteibi(rs.getString(SHUSI_DT));
				
				// 勘定処理日
				listBean.setKanjo_syoribi(rs.getString(SYORI_DT));
				
				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				
				// 項目１
				listBean.setKomoku1(rs.getString(KOMOKU1));
				
				// 項目３
				listBean.setKomoku3(rs.getString(KOMOKU3));
			
				// 文書No.
				listBean.setBunsyo_no(rs.getString(BUNSYO_NO));

				// 判定事由
				listBean.setHantei_jiyu(rs.getString(HANTEI_JIYUU));

				// 滞留区分
				listBean.setTairyu_kbn(rs.getString(TAIRYU_KBN));

				// 滞留判定
				listBean.setTairyu_hantei(rs.getString(TAIRYU_HANTEI_NM));
				
	    		// 明細配列に取得情報を格納
	    		ar_meisai.add(i, listBean);
	    		i++;
	    		
	    		// 通貨コード
                form.setTuuka_cd(rs.getString(TUUKA_CD));
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
	 * 汎用項目(ラベル)取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void gethanyo() throws SQLException {

		ResultSet rs = null;

		try{
			//ResultSet取得
			rs = getKbnval(common_OZ,system_kbn,cmnData.getComLangMode());

			// ActionForm に取得値を格納
			int i = 0;
			
			while ( rs.next() ) {
				if(i==0){
					form.setHanyo1_title(rs.getString(KBN_HYOUJI_VAL));
				}
				if(i==1){
					form.setKomoku1(rs.getString(KBN_HYOUJI_VAL));
				}
				if(i==2){
					form.setKomoku3(rs.getString(KBN_HYOUJI_VAL));
				}
				i++;
			}
		
		} finally {
	    	if (rs != null) {
    			//Resultset close
    			rs.close();
    		}
	    }
	}	
}