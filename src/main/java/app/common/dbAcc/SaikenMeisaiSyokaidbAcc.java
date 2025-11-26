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
import app.common.form.SaikenMeisaiSyokaiForm;
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
* OZ6105_債権明細照会タブ  DBアクセスクラス
*/
public class SaikenMeisaiSyokaidbAcc extends CommonDbAcc {
	
	private SessionData cmnData = null;				// 機能共通セッション
	private TorihikisakiBean torihikisaki_bean = null;	// 取引先情報
	private SaikenMeisaiSyokaiForm form = null;		// アクションフォーム
	private AppContext appContext = null;				// ＡＰＰコンテキスト

	//Resultset用文字列	
	private static final String KBN_VAL              = "kbn_val";
	private static final String KBN_HYOUJI_VAL       = "KBN_HYOUJI_VAL";
	private static final String TENPU_ANKEN_NO       = "tenpu_anken_no";
	private static final String TENPU_ANKEN_NO_EDA   = "tenpu_anken_no_eda";
	private static final String SATEI_ANKEN_NO_EDA   = "satei_anken_no_eda";
	private static final String KEIYAKU_DENPYO_NO    = "keiyaku_denpyo_no";
	private static final String KINGAKU              = "kingaku";
	private static final String TUUKA_CD             = "tuuka_cd";	
	private static final String KANJO_CD             = "kanjo_cd";
	private static final String HANYO1               = "hanyo1";
	private static final String SHUSI_DT             = "shusi_dt";
	private static final String SYORI_DT             = "syori_dt";
	private static final String HANTEI_JIYUU         = "hantei_jiyuu";
	private static final String TAIRYU_KBN           = "tairyu_kbn";
	private static final String TAIRYU_HANTEI        = "tairyu_hantei";
	private static final String SOSHIKI              = "soshiki";
	private static final String KANJO_NM             = "kanjo_nm";
	private static final String BUNSYO_NO            = "bunsyo_no";
	private static final String INVOICE_NO           = "invoice_no";
	private static final String KOMOKU1              = "komoku1";
	private static final String KOMOKU3              = "komoku3";
	private static final String SAIKEN_FLG           = "saiken_flg";
	private static final String KANJO_UCHI_CD        = "kanjo_uchi_cd";
	private static final String KANJO_UCHI_NM        = "kanjo_uchi_nm";
	
	private static final String SHOW                 = "show";		//区分キー（表示件数）
	private static final String common_OZ            = "common_OZ";	//区分キー (汎用項目(ラベル)
	
	private static final String KINGAKU_FORMAT_JA		= "##,###,###,###,###,##0.##";
	private static final String KINGAKU_FORMAT_EN		= "##,###,###,###,###,##0.00";
	private String IPAN_SAIKEN				="1";		// 勘定科目マスタ・債権フラグ・ 1: 査定対象債権
	private String KOTEI_EIGYO_SAIKEN		="2";		// 勘定科目マスタ・債権フラグ・ 2: 固定化営業債権
	private String HOSYO_SAIKEN			="3";		// 勘定科目マスタ・債権フラグ・ 3: 保証債務
	private String HIKIATEKIN				="9";		// 勘定科目マスタ・債権フラグ・ 9: 貸倒引当金
	
	private double	 tairyu_saimu	= 0;		// 滞留債権計
	private double	 saiken_zankei	= 0;		// 債権残高合計
	private double	 hosyo_saimu	= 0;		// 保証債務合計
	private double	 hikiatekin		= 0;		// 引当金合計
	
	private static final String SP_SS_OL_SELECT_SENTEIKEI 	= "SP_SS_OL_SELECT_SENTEIKEI";	//債権残高合計・保証債務合計・引当金合計・滞留債権計の取得
	private static final String SP_SS_OL_SELECT_SAIKENKEI 	= "SP_SS_OL_SELECT_SAIKENKEI";	//債権残高合計・保証債務合計・引当金合計・滞留債権計の取得
	private static final String SP_SS_OL_SELECT_ICHIRAN		= "SP_SS_OL_SELECT_ICHIRAN";	//明細一覧【リスト】を取得
	private static final String SP_SS_OZ6105_SELECT_ICHIRAN	= "SP_SS_OZ6105_SELECT_ICHIRAN";//明細一覧【リスト】を取得
	
	// INパラメータ
	private String system_kbn;			// システム区分
	private String phase;				// フェーズ
	private String anken_no;			// 滞留判定案件NO.
	private String tori_cd;			// 取引先コード
	private String satei_kaisya_cd;	// 査定会社コード
	private String mise_cd;			// 店コード
	private String ym;					// 年月

	/**
	 * コンストラクタ
	 * 
	 * @param SqlExecuter
	 * @param Log
	 * @param AppContext
	 */
	public SaikenMeisaiSyokaidbAcc(SqlExecuter sqlExec, Log log, AppContext appcontext) {		
		super(sqlExec, log);
		this.appContext = appcontext;
		
		//ビーン取得;
		cmnData = appContext.getCMN();
		torihikisaki_bean = cmnData.getTori_bean();
		form = (SaikenMeisaiSyokaiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		system_kbn = torihikisaki_bean.getSystem_kbn();
		phase = torihikisaki_bean.getPhase();
		anken_no = torihikisaki_bean.getAnken_no();	
		tori_cd = Function.trim(torihikisaki_bean.getKanjo_cd());
		satei_kaisya_cd = torihikisaki_bean.getSateikaisya_cd();
		mise_cd = torihikisaki_bean.getMise_cd();
		ym = torihikisaki_bean.getTaisyo_ym();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
		system_kbn			 = GS.EMPTY_CHARCTER;
		phase				 = GS.EMPTY_CHARCTER;
		anken_no			 = GS.EMPTY_CHARCTER;
		tori_cd				 = GS.EMPTY_CHARCTER;
		satei_kaisya_cd		 = GS.EMPTY_CHARCTER;
		mise_cd				 = GS.EMPTY_CHARCTER;
		ym					 = GS.EMPTY_CHARCTER;
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
	 * 債権残高合計・保証債務合計・引当金合計・滞留債権計の取得 <br>
	 * 
	 * @exception SQLException
	 */
	
	public boolean getSenteikei() throws SQLException {
		
		boolean result = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_SENTEIKEI, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(anken_no);
		exCstmt.setResultSet(RESULTSET);		
	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
			
		 int i = 0;
		 while ( rs.next() ) {
			 if(null != rs.getString(KINGAKU)) {	
		
				 //債権残高合計
				 if(IPAN_SAIKEN.equals(rs.getString(SAIKEN_FLG)) || KOTEI_EIGYO_SAIKEN.equals(rs.getString(SAIKEN_FLG))){
					 saiken_zankei += Function.getValueOfDouble(rs.getString(KINGAKU))*100;
		   		 }
				 //保証債務合計
				 else if(HOSYO_SAIKEN.equals(rs.getString(SAIKEN_FLG))) {
					 hosyo_saimu	 += Function.getValueOfDouble(rs.getString(KINGAKU))*100;	
				 }
				 //引当金合計
				 else if(HIKIATEKIN.equals(rs.getString(SAIKEN_FLG))) {
					 hikiatekin	 += Function.getValueOfDouble(rs.getString(KINGAKU))*100;
		   		 }
				 //滞留債権
				 if(IPAN_SAIKEN.equals(rs.getString(TAIRYU_HANTEI))){
					 tairyu_saimu	+= Function.getValueOfDouble(rs.getString(KINGAKU))*100;
				 }
			 }
			 i++;
		 }
		 
		 if(i == 0 && (cmnData.getReturn_gamenId().equals(GS.OS5101) || cmnData.getReturn_gamenId().equals(GS.OS4101) || cmnData.getReturn_gamenId().equals(GS.OB2105) || cmnData.getReturn_gamenId().equals(GS.OB2103))){
			 result = true;
		 }else{
	    	if (system_kbn.equals(GS.GSS)){
	    		//国内
	    		form.setTairyu_saimu(Function.format(KINGAKU_FORMAT_JA,tairyu_saimu/100));
	    		form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_JA,saiken_zankei/100));
	    		form.setHosyo_saimu(Function.format(KINGAKU_FORMAT_JA,hosyo_saimu/100));
	    		form.setHikiatekin(Function.format(KINGAKU_FORMAT_JA,hikiatekin/100));
	    	}else{
	    		//海外
	    		form.setTairyu_saimu(Function.format(KINGAKU_FORMAT_EN,tairyu_saimu/100));
	    		form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_EN,saiken_zankei/100));
	    		form.setHosyo_saimu(Function.format(KINGAKU_FORMAT_EN,hosyo_saimu/100));
	    		form.setHikiatekin(Function.format(KINGAKU_FORMAT_EN,hikiatekin/100));
	    	}
		 }
		 
	    return result;
	    
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
	 * 債権残高合計・保証債務合計・引当金合計・滞留債権計の取得 <br>
	 * 
	 * @exception SQLException
	 */
	
	public void getSaikenkei() throws SQLException {
		
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_SAIKENKEI, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(Function.trim(tori_cd));
		exCstmt.setStringIn(satei_kaisya_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(ym);
		exCstmt.setResultSet(RESULTSET);

	    try {
			//SQL実行
	    	exCstmt.execute();
	    	isError(exCstmt);
			rs = exCstmt.getResultSet(RESULTSET);
					
			 while ( rs.next() ) {

				 if(null != rs.getString(KINGAKU)) {
					 
					 //債権残高合計
					 if(IPAN_SAIKEN.equals(rs.getString(SAIKEN_FLG)) || KOTEI_EIGYO_SAIKEN.equals(rs.getString(SAIKEN_FLG))){
						 saiken_zankei += Function.getValueOfDouble(rs.getString(KINGAKU))*100;
			   		 }
					 
					 //保証債務合計
					 else if(HOSYO_SAIKEN.equals(rs.getString(SAIKEN_FLG))) {
						 hosyo_saimu	 += Function.getValueOfDouble(rs.getString(KINGAKU))*100;	
					 }
					 //引当金合計
					 else if(HIKIATEKIN.equals(rs.getString(SAIKEN_FLG))) {
						 hikiatekin	 += Function.getValueOfDouble(rs.getString(KINGAKU))*100;
			   		 }
					 /*滞留債権
					 if(IPAN_SAIKEN.equals(rs.getString(TAIRYU_HANTEI))){ 
						 tairyu_saimu	+= Function.getValueOfLongC(rs.getString(KINGAKU));	
					 }*/
				 }
			 }
	    	if (system_kbn.equals(GS.GSS)){
	    		//国内
	    		form.setTairyu_saimu(Function.format(KINGAKU_FORMAT_JA,tairyu_saimu/100));
	    		form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_JA,saiken_zankei/100));
	    		form.setHosyo_saimu(Function.format(KINGAKU_FORMAT_JA,hosyo_saimu/100));
	    		form.setHikiatekin(Function.format(KINGAKU_FORMAT_JA,hikiatekin/100));
	    	}else{
	    		//海外
	    		form.setTairyu_saimu(Function.format(KINGAKU_FORMAT_EN,tairyu_saimu/100));
	    		form.setSaiken_zankei(Function.format(KINGAKU_FORMAT_EN,saiken_zankei/100));
	    		form.setHosyo_saimu(Function.format(KINGAKU_FORMAT_EN,hosyo_saimu/100));
	    		form.setHikiatekin(Function.format(KINGAKU_FORMAT_EN,hikiatekin/100));
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
	 * (b),(a)以外の場合
	 * 
	 * @exception SQLException
	 */
	
	public boolean getMeisai() throws SQLException {
		boolean result = false;
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OL_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(anken_no);
		exCstmt.setStringIn(phase);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(torihikisaki_bean.getKijunbi_kbn());
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
				
				// 査定案件枝番Ｎｏ．
				listBean.setAnken_no_eda(rs.getString(SATEI_ANKEN_NO_EDA));
				
				// 添付紹会用案件Ｎｏ．
				listBean.setTenpu_anken_no(rs.getString(TENPU_ANKEN_NO));
				listBean.setTenpu_anken_no_eda(rs.getString(TENPU_ANKEN_NO_EDA));
				
				// 勘定科目コード & 勘定科目名称
				if(system_kbn.equals(GS.GSS)){
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
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
					}else{
                    
						// 海外
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
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
				listBean.setTairyu_hantei(rs.getString(TAIRYU_HANTEI));
				
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
		    
			 if(i == 0 && (cmnData.getReturn_gamenId().equals(GS.OS5101) || cmnData.getReturn_gamenId().equals(GS.OB2105) || cmnData.getReturn_gamenId().equals(GS.OS4101) || cmnData.getReturn_gamenId().equals(GS.OB2103))){
				 result = true;
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
	    return result;
	}	
	
	/**
	 * 一覧情報取得処理 <br>
	 * (a) 共)遷移元画面IDがOS4101_ゴルフ会員権一覧、OB2103_対象先選定_追加対象先選択で かつ、
	 * 		T16_引当金検討対象先BS明細にデータが存在しない場合
	 * 
	 * @exception SQLException
	 */
												
	public void getMeisai2() throws SQLException {
		ResultSet rs = null;
		//ExCallableStatement生成
		ExCallableStatement exCstmt = new ExCallableStatement(SP_SS_OZ6105_SELECT_ICHIRAN, sqlExec);
		exCstmt.setStringIn(system_kbn);
		exCstmt.setStringIn(cmnData.getComLangMode());
		exCstmt.setStringIn(Function.trim(tori_cd));
		exCstmt.setStringIn(satei_kaisya_cd);
		exCstmt.setStringIn(mise_cd);
		exCstmt.setStringIn(ym);
		exCstmt.setStringIn(torihikisaki_bean.getKijunbi_kbn());
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
						
				// 滞留区分
				listBean.setTairyu_kbn(rs.getString(TAIRYU_KBN));

				// 収支予定日
				listBean.setSyusi_yoteibi(rs.getString(SHUSI_DT));
				
				// 契約No.
				listBean.setKeiyaku_denpyo_no(rs.getString(KEIYAKU_DENPYO_NO));
				
                // 金額計
				if(null != rs.getString(KINGAKU)){
					if (system_kbn.equals(GS.GSS)){
                    
						// 国内
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_JA, Function.getValueOfDouble(rs.getString(KINGAKU))));
					}else{
                    
						// 海外
						listBean.setKingaku_kei(Function.format(KINGAKU_FORMAT_EN, Function.getValueOfDouble(rs.getString(KINGAKU))));
					}     
				}
				// 勘定科目コード & 勘定科目名称
				if(system_kbn.equals(GS.GSS)){
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_UCHI_CD));
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_UCHI_NM));
				}else{
					listBean.setKanjo_kamoku_cd(rs.getString(KANJO_CD));
					listBean.setKanjo_kamoku_nm(rs.getString(KANJO_NM));
				}
				
				// 勘定処理日
				listBean.setKanjo_syoribi(rs.getString(SYORI_DT));
					
				// 組織
				listBean.setSoshiki(rs.getString(SOSHIKI));						
					
				// インボイスNo
				listBean.setInvoice_no(rs.getString(INVOICE_NO));
				
				if (system_kbn.equals(GS.MTS) || system_kbn.equals(GS.FOCUS)) {
			
					// 項目１
					listBean.setKomoku1(rs.getString(KOMOKU1));
				
					// 項目３
					listBean.setKomoku3(rs.getString(KOMOKU3));
	
				}
				// 汎用１
				listBean.setHanyo1(rs.getString(HANYO1));
			
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
	 * 汎用項目ラベル【リスト】取得 <br>
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