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
import app.common.form.RyuhoSaimuSyokaiForm;
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
* OZ6107_債務明細照会タブ  DBアクセスクラス
*/
public class RyuhoSaimuSyokaiDbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private RyuhoSaimuSyokaiForm form = null;			// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String KBN_VAL             	= "kbn_val";
	private static final String KBN_HYOUJI_VAL		= "KBN_HYOUJI_VAL";
	private static final String SATEI_ANKEN_NO_EDA	= "satei_anken_no_eda";
	private static final String SOSHIKI             	= "soshiki";
	private static final String KANJO_CD            	= "kanjo_cd";
	private static final String KANJO_NM            	= "kanjo_nm";
	private static final String SHUSI_DT            	= "shusi_dt";
	private static final String KEIYAKU_DENPYO_NO   	= "keiyaku_denpyo_no";
	private static final String KINGAKU             	= "kingaku";
	private static final String RYUHOSAIMU_KBN      	= "ryuhosaimu_kbn";
	private static final String BIKO					= "biko";
	private static final String TUUKA_CD				= "tuuka_cd";
	private static final String KANJO_UCHI_CD			= "kanjo_uchi_cd";
	private static final String KANJO_UCHI_NM			= "kanjo_uchi_nm";
	private static final String KINGAKU_FORMAT_JA		= "##,###,###,###,###,##0.##";
	private static final String KINGAKU_FORMAT_EN		= "##,###,###,###,###,##0.00";

	private static final String SHOW            = "show";			//区分キー（表示件数）	
	private static final String SP_SS_O_SELECT_RYUHOKEI 	= "SP_SS_O_SELECT_RYUHOKEI";	//明細の合計を取得するプロシージャ
	private static final String SP_SS_OL_SELECT_RYUHOKEI 	= "SP_SS_OL_SELECT_RYUHO";		//一覧情報を取得するプロシージャ

	// INパラメータ
	private String system_kbn;			// システム区分
	private String phase;				// フェーズ
	private String anken_no;			// 査定案件No
	
	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	
	public RyuhoSaimuSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {
		
		super(sqlExec, log);
		this.appContext = appcontext;

		//ビーン取得
		cmnData = appContext.getCMN();
		torihikisaki_bean = cmnData.getTori_bean();
		form = (RyuhoSaimuSyokaiForm)appContext.getActionForm();
		
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
	 * 債務総計・留保債務計取得 <br>
	 * 
	 * @exception SQLException
	 */
	public void getSaimukei() throws SQLException {

		//変数定義
		String RYUHO_SAIMU = "1";
		double saimu_kei = 0;
		double ryuhosaimu_kei = 0;

		ResultSet rs = null;
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_O_SELECT_RYUHOKEI, sqlExec);
		exCstmt.setStringIn(system_kbn);
	    exCstmt.setStringIn(phase);
	    exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);
		try{
			//SQL実行	
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
			//ActionForm に取得値を格納
			
			
		    while ( rs.next() ) {		    	
		    	
			    	//留保債務計
		    		if(RYUHO_SAIMU.equals(rs.getString(RYUHOSAIMU_KBN))){
		    			ryuhosaimu_kei += Function.getValueOfDouble(rs.getString(KINGAKU))*100;	
		    		}
			    	//債務総計
		    		saimu_kei += Function.getValueOfDouble(rs.getString(KINGAKU))*100;
		    		}
		    
	    	if (system_kbn.equals(GS.GSS)){
	    		//国内
	    		form.setSaimu_kei(Function.format(KINGAKU_FORMAT_JA,saimu_kei/100));
	    		form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_JA,ryuhosaimu_kei/100));
	    	}else{
	    		//海外
	    		form.setSaimu_kei(Function.format(KINGAKU_FORMAT_EN,saimu_kei/100));
	    		form.setRyuhosaimu_kei(Function.format(KINGAKU_FORMAT_EN,ryuhosaimu_kei/100));
	    	}		    	
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
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_RYUHOKEI, sqlExec);
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
				
				//id
				listBean.setId(Function.getStringOfInt(i));
				
				// 査定案件No枝番
				listBean.setAnken_no_eda(rs.getString(SATEI_ANKEN_NO_EDA));
				
				if (system_kbn.equals(GS.GSS)){
					// 科目
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_UCHI_CD));
					
					// 科目名称
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_UCHI_NM));
				}else{
					// 科目
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_CD));
					
					// 科目名称
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_NM));
				}

				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));
				
				// 収支予定日
				listBean.setSyusi_yoteibi(rs.getString(SHUSI_DT));
				
				// 金額計
				if (null != rs.getString(KINGAKU)) {
					if (system_kbn.equals(GS.GSS)){
						//国内
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
					}else{
						//海外
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
					}
				}
				
				// 契約No.
				listBean.setKeiyaku_denpyo_no(rs.getString(KEIYAKU_DENPYO_NO));
				
				// 留保債務				
				listBean.setRyuhosaimu(rs.getString(RYUHOSAIMU_KBN));
				
				// 備考
				listBean.setBiko(rs.getString(BIKO));
								
				// (通貨)
				form.setTuuka_cd(rs.getString(TUUKA_CD));
				
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
	    		//Resultset close
	    		rs.close();
	    	}
	    }
	}
}