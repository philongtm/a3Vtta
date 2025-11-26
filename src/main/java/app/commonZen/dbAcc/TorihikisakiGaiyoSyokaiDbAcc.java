/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
002		2009/11/19		SSC				課題No.132 値がnullの場合のブランクで出力されるよう修正
009		2010/01/15		SSC				課題No.233 決算期表示変更＆出力財務データ不正
******************************************************************************/
package app.commonZen.dbAcc;

import app.SessionDataZen;
import app.commonZen.form.TorihikisakiGaiyoSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 取引先概要タブDBアクセスクラス
 */
public class TorihikisakiGaiyoSyokaiDbAcc extends CommonDbAcc {

	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private TorihikisakiGaiyoSyokaiForm form=null;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public TorihikisakiGaiyoSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		form = (TorihikisakiGaiyoSyokaiForm)appContext.getActionForm();
	}
	
	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		
		ResultSet rs = null;
		// 査定案件No
		String satei_anken_no = cmnData.getSatei_anken_no();
		
		try {
			getTorihikisakiGaiyoForm(satei_anken_no);
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * @param satei_anken_no
	 * @throws SQLException
	 */
	public TorihikisakiGaiyoSyokaiForm getTorihikisakiGaiyoForm(String satei_anken_no) throws SQLException {
		ResultSet rs = null;

		String sLangMode = cmnData.getComLangMode();						/* No.845 */

		///////////////////////////////////////////////////
		//障害票：267
		//チェックイン日：2008/5/17
		//対応者：SJA中島
		//概要：統合退避マスタ、統合対比退避などフェーズに依存しないため出力するように修正。
		///////////////////////////////////////////////////
		
        ///////////////////////////////////////////////////
		//障害票：453
		//チェックイン日：2008/5/28
		//対応者：SJA渡辺
		//概要：画面のメソッドと帳票のメソッドを共通化。
		///////////////////////////////////////////////////
	    //////////////////////////////////////////////////
		//障害票：825
		//チェックイン日：2008/6/13
		//対応者：SJA中島
		//概要：国名称をD＆B国テーブルから取得するように変更
		//////////////////////////////////////////////////

		//////////////////////////////////////////////////
		//障害票：845
		//チェックイン日：2008/7/XX
		//対応者：SJA佐々木
		//概要：性能改善施策の一環として、以下の対応を実施。
		//  ・DBアクセス回数の削減および処理効率の改善
		//  ・不要項目・メソッドの削除（コメントアウト）
		//////////////////////////////////////////////////

		// 査定進捗管理、統合対比対比、統合マスタ退避検索SQL
		StringBuffer sql = new StringBuffer()
						.append("select distinct ")
						.append("SS.kikan_tori_cd, ")
						.append("SS.system_kbn, ")
						.append("TT.TOGO_TORI_CD, ")
						.append("TM.BUSINESS_NM_KJ, ")
						.append("TM.BUSINESS_NM, ")
						.append("TM.WB_COUNTRY_CD,")
						// TODO 国コードは、現時点ではワールドベース国コード。ISO国コードになるかも
						// 課題No.132
						// 追加開始
						//.append("TM.WB_COUNTRY_CD || '(' || DCM.WB_COUNTRY_NM || ')' as country_cd_nm, ")
						.append("DECODE(TM.WB_COUNTRY_CD,null,null,TM.WB_COUNTRY_CD || '(' || DCM.WB_COUNTRY_NM || ')') as country_cd_nm,")
						// 追加完了
						.append("TM.STATE_NM_KJ,")
						.append("TM.CITY_NM_KJ,")
						.append("TM.STREET_ADR_KJ,")
						.append("TM.STREET_ADR,")
						.append("TM.CITY_NM,")
						.append("TM.STATE_NM, ")
						.append(" TH.STATE_NM as TH_STATE_NM, ")
						.append(" TH.CITY_NM as TH_CITY_NM, ")
						.append("TH.STREET_ADR as TH_STREET_ADR,")
						.append("TH.STREET_ADR2 as TH_STREET_ADR2,")
						.append("KT.gaibu_ktk, ")
						.append("KT.ktk_kikan, ")
						.append("KT.fss, ")
						.append("KT.duns_rating, ")
						.append("KT.duns_no, ")
						.append("DM.SIC_SM_CD, ")
						.append("DM.SIC_SM_NM_KJ, ")
						// 課題No.132
						// 追加開始
						//.append("DM.SIC_SM_CD || '(' || DM.SIC_SM_NM_KJ || ')' as sic_sm_cd_nm_kj, ")
						//.append("DM.SIC_SM_CD || '(' || DM.SIC_SM_NM_E || ')' as sic_sm_cd_nm_en ")
						.append("DECODE(DM.SIC_SM_CD,null,null,DM.SIC_SM_CD || '(' || DM.SIC_SM_NM_KJ || ')') as sic_sm_cd_nm_kj, ")
						.append("DECODE(DM.SIC_SM_CD,null,null,DM.SIC_SM_CD || '(' || DM.SIC_SM_NM_E || ')') as sic_sm_cd_nm_en ")
						// 追加完了
						.append(",kb1.KBN_HYOUJI_VAL as ktk_kikan_nm, ") 	/* No.845 */
						.append("kb2.KBN_HYOUJI_VAL as gaibu_ktk_nm, ")		/* No.845 */
						.append("ST.jigyonaiyo, ST.kabunusi_nm1, ")			/* No.845 */
						.append("ST.kabunusi_nm2, ST.kabunusi_nm3, ")		/* No.845 */
						.append("ST.kabunusi_nm4, ST.kabunusi_nm5, ")		/* No.845 */
						.append("ST.kabusu1, ST.kabusu2, ST.kabusu3, ")		/* No.845 */
						.append("ST.kabusu4, ST.kabusu5, ST.hiritu1, ")		/* No.845 */
						.append("ST.hiritu2, ST.hiritu3, ST.hiritu4, ")		/* No.845 */
						.append("ST.hiritu5, ")								/* No.845 */
						.append("CM.comment_val ")							/* No.845 */
						.append("from ")
						.append("SST_SATEI_STAT SS ")
						.append("LEFT JOIN SSE_TAIHI TT ON ")
						.append("trim(SS.kikan_tori_cd) || '00' = TT.kikan_tori_cd ")
						.append("and SS.ym = TT.ym ")
						.append("and SS.system_kbn = TT.system_kbn ")
						.append("and SS.mise_cd = TT.office_cd LEFT JOIN ")
						.append("SSE_TOGO_MST TM ON ")
						.append("TT.togo_tori_cd = TM.togo_tori_cd ")
						.append("and TT.sikibetu_cd = TM.sikibetu_cd ")
						.append("and TT.syori_kaisu = TM.syori_kaisu ")
						.append("and TT.ym = TM.ym LEFT JOIN ")
						.append("SSE_KTK KT ON ")
						.append("TT.togo_tori_cd=KT.duns_no ")
						.append("and TT.ym = KT.ym ")
						.append("and TT.sikibetu_cd = KT.sikibetu_cd ")
						.append("and TT.syori_kaisu = KT.syori_kaisu LEFT JOIN ")
						.append("SSP_KBN KB1 on ")
						.append("KB1.KBN_KEY = 'ktk_kikan' and ")			/* No.845 */ 
						.append("KB1.system_kbn ='")
						.append(cmnData.getSystem_kbn())
						.append("' and KB1.LANG_MODE = '")
						.append(sLangMode).append("' and ")					/* No.845 */
						.append("KB1.KBN_VAL = KT.ktk_kikan ")				/* No.845 */
						.append("left join SSP_KBN KB2 on ")				/* No.845 */
						.append("KB2.KBN_KEY = 'gaibu_ktk' and ")			/* No.845 */
						.append("KB1.system_kbn ='")
						.append(cmnData.getSystem_kbn())
						.append("' and KB1.LANG_MODE = '")
						.append(sLangMode).append("' and ")					/* No.845 */
						.append("KB2.KBN_VAL = KT.gaibu_ktk ")				/* No.845 */
						.append(" LEFT JOIN TM_DB_KIHON_TBL@VIR_SJLMA DC ON ")
						.append("DC.DUNS_NO = TT.togo_tori_cd LEFT JOIN ")
						.append("TM_DB_SIC_SM_MST@VIR_SJLMA DM ON ")
						.append("DC.SIC_CD1 = DM.SIC_SM_CD ")
						.append(" LEFT JOIN TM_DB_KIHON_TBL@VIR_SJLMA TH ON")
						.append(" TT.togo_tori_cd = TH.DUNS_NO ")
			// 障害票：825　チェックイン日：2008/6/13　SJA中島　国名をD＆B国テーブルから取得
						.append(" LEFT JOIN TM_DB_COUNTRY_MST@VIR_SJLMA DCM ON ")
						.append(" TM.WB_COUNTRY_CD = DCM.WB_COUNTRY_CD AND ")
						.append(" TM.ISO_COUNTRY_NM_RYA2 = DCM.ISO_COUNTRY_NM_RYA2 ")
						.append(" LEFT JOIN SST_SATEI ST ON ")				/* No.845 */
						.append("SS.ANKEN_NO = ST.ANKEN_NO AND ")			/*No.845*/ 
						.append("ST.phase = '").append(cmnData.getPhase()).append("' ")/* No.845 */
						.append(" LEFT JOIN SST_COMMENT CM ON ")			/* No.845 */
						.append("ST.anken_no = CM.anken_no and ")			/* No.845 */
						.append("ST.phase = CM.phase and ")					/* No.845 */
						.append("CM.toroku_point = '10' ")					/* No.845 */
						.append("where ")
						.append("SS.anken_no ='")
						.append(satei_anken_no)
						.append("'");
		
		try{
			InputCheck check = new InputCheck();

			rs = sqlExec.execQuery(sql.toString());
			if(rs.next()){
				form.setKikan_tori_cd(rs.getString("kikan_tori_cd"));

				form.setTogo_tori_cd(rs.getString("TOGO_TORI_CD"));
				if ("Ja".equals(sLangMode) && 								/* No.845 */
					!(check.isNullBlank(rs.getString("BUSINESS_NM_KJ")))) {
					form.setBusiness_nm_kj(rs.getString("BUSINESS_NM_KJ"));
				} else {
					form.setBusiness_nm_kj(rs.getString("BUSINESS_NM"));
				}
				form.setBusiness_nm(rs.getString("BUSINESS_NM"));
				String sWbCountryCd = rs.getString("WB_COUNTRY_CD");		/* No.845 */
				form.setWb_country_cd(sWbCountryCd);						/* No.845 */
				form.setWb_country_cd_nm(rs.getString("country_cd_nm"));
				if(!"369".equals(sWbCountryCd)){							/* No.845 */
					// No877, 2008/06/20, SJA渡辺, TM_DB_KIHON_TBLにデータがあるかのチェックを追加修正
					String sThStreetAddr = rs.getString("TH_STREET_ADR");	/* No.845 */
					String sThStreetAddr2 = rs.getString("TH_STREET_ADR2");	/* No.845 */
					String sThCityNm = rs.getString("TH_CITY_NM");			/* No.845 */
					String sThStateNm = rs.getString("TH_STATE_NM");		/* No.845 */
					if (!(check.isNullBlank(sThStreetAddr))|| 				/* No.845 */
						!(check.isNullBlank(sThStreetAddr2))||				/* No.845 */
						!(check.isNullBlank(sThCityNm))|| 					/* No.845 */
						!(check.isNullBlank(sThStateNm))) {					/* No.845 */
						StringBuffer oAddrStr = new StringBuffer(48);		/* No.845 */
						oAddrStr.append(Function.trim(sThStreetAddr))		/* No.845 */
								.append(" ")								/* No.845 */
								.append(Function.trim(sThStreetAddr2))		/* No.845 */
								.append(" ")								/* No.845 */
								.append(Function.trim(sThCityNm))			/* No.845 */
								.append(" ")								/* No.845 */
								.append(Function.trim(sThStateNm));			/* No.845 */
						form.setAddress(oAddrStr.toString());				/* No.845 */
					} else {
						form = setCountryAddr(form, check, rs, sLangMode);	/* No.845 */
					}
				}else{
					form = setCountryAddr(form, check, rs, sLangMode);		/* No.845 */
				}
				form.setFss(rs.getString("fss"));
				form.setDuns_rating(rs.getString("duns_rating"));
				form.setDuns_no(rs.getString("duns_no"));
				form.setSystem_kbn(rs.getString("system_kbn"));
				form.setSic_code(rs.getString("SIC_SM_CD"));
				form.setSic_sm_nm_kj(rs.getString("SIC_SM_NM_KJ"));
				if(form.getSic_code() != null){
					form.setSic_sm_cd_nm_kj(rs.getString("sic_sm_cd_nm_kj"));
					form.setSic_sm_cd_nm_en(rs.getString("sic_sm_cd_nm_en"));
				}
				
				//getSST_SATEI(satei_anken_no);								/* No.845 */
				form.setJigyonaiyo(rs.getString("jigyonaiyo"));				/* No.845 */
				form.setKabunusi_nm1(rs.getString("kabunusi_nm1"));			/* No.845 */
				form.setKabunusi_nm2(rs.getString("kabunusi_nm2"));			/* No.845 */
				form.setKabunusi_nm3(rs.getString("kabunusi_nm3"));			/* No.845 */
				form.setKabunusi_nm4(rs.getString("kabunusi_nm4"));			/* No.845 */
				form.setKabunusi_nm5(rs.getString("kabunusi_nm5"));			/* No.845 */
				String sKabusu1 = rs.getString("kabusu1");					/* No.845 */
				String sKabusu2 = rs.getString("kabusu2");					/* No.845 */
				String sKabusu3 = rs.getString("kabusu3");					/* No.845 */
				String sKabusu4 = rs.getString("kabusu4");					/* No.845 */
				String sKabusu5 = rs.getString("kabusu5");					/* No.845 */
				if (sKabusu1 != null) {										/* No.845 */
					form.setKabusu1(Function.format(						/* No.845 */
						"#,###,###,##0", Function.getValueOfLong(sKabusu1)));/* No.845 */
				}															/* No.845 */
				if (sKabusu2 != null) {										/* No.845 */
					form.setKabusu2(Function.format(						/* No.845 */
						"#,###,###,##0", Function.getValueOfLong(sKabusu2)));/* No.845 */
				}															/* No.845 */
				if (sKabusu3 != null) {										/* No.845 */
					form.setKabusu3(Function.format(						/* No.845 */
						"#,###,###,##0", Function.getValueOfLong(sKabusu3)));/* No.845 */
				}															/* No.845 */
				if (sKabusu4 != null) {										/* No.845 */
					form.setKabusu4(Function.format(						/* No.845 */
						"#,###,###,##0", Function.getValueOfLong(sKabusu4)));/* No.845 */
				}															/* No.845 */
				if (sKabusu5 != null) {										/* No.845 */
					form.setKabusu5(Function.format(						/* No.845 */
						"#,###,###,##0", Function.getValueOfLong(sKabusu5)));/* No.845 */
				}															/* No.845 */
				form.setHiritu1(formatViewKingaku(rs.getString("hiritu1")));/* No.845 */
				form.setHiritu2(formatViewKingaku(rs.getString("hiritu2")));/* No.845 */
				form.setHiritu3(formatViewKingaku(rs.getString("hiritu3")));/* No.845 */
				form.setHiritu4(formatViewKingaku(rs.getString("hiritu4")));/* No.845 */
				form.setHiritu5(formatViewKingaku(rs.getString("hiritu5")));/* No.845 */

				// 抽出事由取得
				getTyushutsujiyu(form);

				// コメント取得
				//getComment(form);											/* No.845 */
				// 1レコード毎にコメントをSelectしないよう修正				/* No.845 */
				form.setComment_val(rs.getString("comment_val"));			/* No.845 */

				// 財務概況取得
				// getZaimu(form);											/* No.845 */
				// 1レコード毎に財務概況情報をselectしないよう修正			/* No.845 */
				form = createZaiGai(form, cmnData.getSatei_anken_no());		/* No.845 */
				// 通貨コード取得
				//getTuuka_cd(form);

				// 外部監査基幹取得
				form.setKtk_kikan(rs.getString("ktk_kikan_nm"));			/* No.845 */
				// 外部監査基幹格付取得
				form.setGaibu_ktk(rs.getString("gaibu_ktk_nm"));			/* No.845 */

			}
			getEmptyZaimu(form);
			
			return form;
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}

	///////////////////////////////////////////////////
	//障害票：267
	//チェックイン日：2008/5/17
	//対応者：SJA中島
	//概要：フェーズに依存する一次二次査定テーブルの値を別に取得するように
	//      新規メソッドを追加。
	///////////////////////////////////////////////////
	/**
	 * 一次二次査定テーブルからデータを取得
	 * @param satei_anken_no
	 * @throws SQLException
	 */
	/* No.845
	private void getSST_SATEI(String satei_anken_no) throws SQLException{
		// フェーズ
		String phase = cmnData.getPhase();
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer()
						.append("select distinct ")
						.append("ST.jigyonaiyo, ")
						.append("ST.kabunusi_nm1, ")
						.append("ST.kabunusi_nm2, ")
						.append("ST.kabunusi_nm3, ")
						.append("ST.kabunusi_nm4, ")
						.append("ST.kabunusi_nm5, ")
						.append("ST.kabusu1, ")
						.append("ST.kabusu2, ")
						.append("ST.kabusu3, ")
						.append("ST.kabusu4, ")
						.append("ST.kabusu5, ")
						.append("ST.hiritu1, ")
						.append("ST.hiritu2, ")
						.append("ST.hiritu3, ")
						.append("ST.hiritu4, ")
						.append("ST.hiritu5 ")
						.append("from ")
						.append("SST_SATEI ST ")
						.append("where ")
						.append("ST.anken_no ='")
						.append(satei_anken_no)
						.append("' and ST.phase='")
						.append(phase)
						.append("'");
		
		try{
			InputCheck check = new InputCheck();
			
			rs = sqlExec.execQuery(sql.toString());
			if(rs.next()){
				form.setJigyonaiyo(rs.getString("jigyonaiyo"));
				form.setKabunusi_nm1(rs.getString("kabunusi_nm1"));
				form.setKabunusi_nm2(rs.getString("kabunusi_nm2"));
				form.setKabunusi_nm3(rs.getString("kabunusi_nm3"));
				form.setKabunusi_nm4(rs.getString("kabunusi_nm4"));
				form.setKabunusi_nm5(rs.getString("kabunusi_nm5"));
				form.setKabusu1(Function.format("#,###,###,##0", Function.getValueOfLong(rs.getString("kabusu1"))));
				form.setKabusu2(Function.format("#,###,###,##0", Function.getValueOfLong(rs.getString("kabusu2"))));
				form.setKabusu3(Function.format("#,###,###,##0", Function.getValueOfLong(rs.getString("kabusu3"))));
				form.setKabusu4(Function.format("#,###,###,##0", Function.getValueOfLong(rs.getString("kabusu4"))));
				form.setKabusu5(Function.format("#,###,###,##0", Function.getValueOfLong(rs.getString("kabusu5"))));
				form.setHiritu1(formatViewKingaku(rs.getString("hiritu1")));
				form.setHiritu2(formatViewKingaku(rs.getString("hiritu2")));
				form.setHiritu3(formatViewKingaku(rs.getString("hiritu3")));
				form.setHiritu4(formatViewKingaku(rs.getString("hiritu4")));
				form.setHiritu5(formatViewKingaku(rs.getString("hiritu5")));
			}
		}finally {
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}

	}
	*/
	
	private String formatViewKingaku(String val){
		if(val == null){
			return "";
		}
		
		return Function.format("##0.##", Function.getValueOfDouble(val) * 100);
	}
	
	/**
	 * コメントを取得する。
	 * @throws SQLException
	 */
	/* No.845
	private void getComment(TorihikisakiGaiyoSyokaiForm form) throws SQLException{
		ResultSet rs = null;
		// 査定案件No
		String satei_anken_no = cmnData.getSatei_anken_no();
		// フェーズ
		String phase = cmnData.getPhase();
		
		StringBuffer sql = new StringBuffer()
							.append(" select ")
							.append("CM.comment_val ")
							.append(" from ")
							.append("SST_SATEI ST LEFT JOIN ")
							.append("SST_COMMENT CM ON ")
							.append("ST.anken_no = CM.anken_no ")
							.append("and ST.phase = CM.phase ")
							.append(" where ")
							.append("ST.anken_no ='")
							.append(satei_anken_no)
							.append("' and CM.toroku_point = '")
							.append("10' and ST.phase='")// 取引先概要決算概況
							.append(phase)
							.append("'");						
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
				form.setComment_val(rs.getString("comment_val"));
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}
	*/


	/**
	 * 通貨コードを取得し、mapにセットする。
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	/* No.845
	private void getTuuka_cd(TorihikisakiGaiyoSyokaiForm form) throws SQLException{
		ResultSet rs = null;
		
		// Duns No
		String duns_no = form.getDuns_no();
		
		StringBuffer sql = new StringBuffer()
							.append(" select ")
							.append(" DT.ISO_CURRENCY_NM ")
							.append(" from ")
							.append(" TM_DB_KIHON_TBL@VIR_SJLMA DC, ")
							.append(" TM_DB_CURENCY_MST@VIR_SJLMA DT ")
							.append(" where ")
							.append(" DC.DUNS_NO ='")
							.append(duns_no)
							.append("' and DC.CURRENCY_CD = DT.WB_CURRENCY_CD ");
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				form.setTsuka_cd(rs.getString("ISO_CURRENCY_NM"));
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}
	*/

	/**
	 * 取引先に紐付いた抽出事由を取得する。
	 * @param form
	 * @throws SQLException
	 */
	private void getTyushutsujiyu(TorihikisakiGaiyoSyokaiForm form) throws SQLException{
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer()
							.append(" select distinct")
							.append(" KM.jiyuu_nm_kj, ")
							.append(" KM.jiyuu_nm_e, ")
							.append(" KN.jiyuu_cd ")
							.append(" from SST_KENTOUTAISYO KN, ")
							.append(" SSM_KENTOUJYOUKEN KM, ")
							.append(" SST_SATEI_STAT SS ")
							.append(" where ")
							.append(" KN.system_kbn ='")
							.append(form.getSystem_kbn())
							/*.append("' and KN.sateikaisya_cd ='")
							.append(cmnData.getComSateiKaishaCd())
							.append("' and KN.ym ='")*/
							// No612, 2008/06/07, 結合条件の査定会社コードを案件Noの査定会社コードと紐つけるように修正
							.append("' and KN.sateikaisya_cd = SS.satei_kaisha_cd")
							//要件No.四-16
							//追加開始
							.append(" and KN.hanki_sihanki_kbn = SS.hanki_sihanki_kbn")
							//追加完了
							.append(" and KN.ym ='")
							.append(Function.removeDateSlash(cmnData.getYm()))
							.append("' and KN.tori_cd ='")
							.append(form.getKikan_tori_cd())
							.append("' and KN.jiyuu_cd = KM.jiyuu_cd")
							.append(" and '0' || SS.ym_kbn = KM.syori_kbn ")
							.append(" and KN.system_kbn = KM.system_kbn ")
							.append(" AND KM.MISE_CD = SS.MISE_CD ")
							.append(" AND KM.SATEIKAISYA_CD = SS.SATEI_KAISHA_CD ")
							//要件No.四-16
							//追加開始
							.append(" and KM.hanki_sihanki_kbn = SS.hanki_sihanki_kbn")
							//追加完了
							.append(" and SS.anken_no ='")
							.append(cmnData.getSatei_anken_no())
							.append("' ORDER BY KN.jiyuu_cd ASC");
							
		try{
			rs = sqlExec.execQuery(sql.toString());
			StringBuffer list_kj = new StringBuffer();
			StringBuffer list_en = new StringBuffer();
			ArrayList jiyuuCdList = new ArrayList();
			int i = 0;
			while(rs.next()){
				if(rs.getString("jiyuu_nm_kj") != null){
					list_kj.append(Function.trim(rs.getString("jiyuu_nm_kj")));
					list_kj.append(",");
				}
				if(rs.getString("jiyuu_nm_e") != null){
					list_en.append(Function.trim(rs.getString("jiyuu_nm_e")));
					list_en.append(",");
				}
				jiyuuCdList.add(i,rs.getString("jiyuu_cd"));
				i++;
			}
			form.setJiyuu_nm_kj(Function.left(list_kj.toString(),list_kj.length()-1));
			form.setJiyuu_nm_en(Function.left(list_en.toString(),list_en.length()-1));
			form.setJiyuuCdList(jiyuuCdList);
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 過去3ヵ年分の財務概況を取得する。
	 * @param form
	 * @throws SQLException
	 */
	/* No.845
	private void getZaimu(TorihikisakiGaiyoSyokaiForm form) throws SQLException{
		
		ResultSet rs = null;
		ArrayList list = form.getZaimu_gaiyo();
		
		try{
			for(int flg_cnt = 0;flg_cnt < 3 ;flg_cnt++){
				HashMap map = new HashMap();
				
				StringBuffer sql = new StringBuffer()
								.append("select ")
								.append("ZM.kessanki, ")
								.append("ZM.kansan_tuka_cd, ")
								.append("ZM.exchangerate,")
								.append("ZM.uriagedaka, ")
								.append("ZM.uriagesorieki, ")
								.append("ZM.hanbaihikanrihi, ")
								.append("ZM.eigyorieki, ")
								.append("ZM.keijorieki, ")
								.append("ZM.toku_rieki, ")
								.append("ZM.toku_sonsitu, ")
								.append("ZM.tokijunrieki, ")
								.append("ZM.haitokin, ")
								.append("ZM.genkasyokyakuhi, ")
								.append("ZM.eigyo_cf, ")
								.append("ZM.ryudosisan, ")
								.append("ZM.koteisisan, ")
								.append("ZM.sisangokei, ")
								.append("ZM.ryudohusai, ")
								.append("ZM.koteihusai, ")
								.append("ZM.husaigokei, ")
								.append("ZM.sihonkin, ")
								.append("ZM.naiburyuho, ")
								.append("ZM.jikosihongokei, ")
								.append("ZM.zaimu_flg, ")
								.append("DT.ISO_CURRENCY_NM ")
								.append("from SST_SATEI_STAT SS ")
								.append("LEFT JOIN SSE_TAIHI TT ON ")
								.append(" SS.ym = TT.ym ")
								.append(" AND SS.mise_cd = TT.office_cd ")
								.append(" AND TRIM(SS.kikan_tori_cd)|| '00' = TT.kikan_tori_cd ")
								.append(" AND SS.system_kbn = TT.system_kbn ")
								.append("LEFT JOIN SSE_ZAIMU ZM ON ")
								.append(" TT.ym = ZM.ym ")
								.append(" AND TT.sikibetu_cd = ZM.sikibetu_cd ")
								.append(" AND TT.togo_tori_cd = ZM.duns_no ")
								.append(" AND TT.syori_kaisu = ZM.syori_kaisu ")
								.append("LEFT JOIN TM_DB_CURENCY_MST@VIR_SJLMA DT ON ")
								.append(" ZM.kansan_tuka_cd=DT.WB_CURRENCY_CD ")
								.append("where ")
								.append("SS.anken_no = '")
								.append(cmnData.getSatei_anken_no())
								.append("' and ZM.new3_flg = '")
								.append(Function.format("#",flg_cnt+1))
								.append("'");
				
				for(int zaimu_flg_cnt=3;zaimu_flg_cnt>=1;zaimu_flg_cnt--){
					StringBuffer sql1 = new StringBuffer()
										.append(sql.toString())								
										.append(" and zaimu_flg='")
										.append(zaimu_flg_cnt)
										.append("'")
										.append(" ORDER BY ZM.syori_kaisu desc");
					// No501, 2008/06/01, SJA渡辺, oracleでカーソルがたまっていく問題を修正。
					try {
						rs = sqlExec.execQuery(sql1.toString());
						if(!rs.next()){
							// 該当するデータがないため、処理終了。
							*/
							/*if(rs != null){
							 try{
							 rs.close();
							 }catch(Exception e){
							 throw new SQLException(e.getMessage());
							 }
							 }*//*
							continue;
						}
						
						map.put("kansan_tuka_cd", rs.getString("kansan_tuka_cd"));
						map.put("ISO_CURRENCY_NM", rs.getString("ISO_CURRENCY_NM"));
						if ((flg_cnt+1) == 3) {
							form.setKansan_tuka_cd((String)map.get("kansan_tuka_cd"));
							form.setTsuka_cd((String)map.get("ISO_CURRENCY_NM"));
						}
						
						// レート計算用
						double erate = 0;
						erate = Function.getValueOfDouble(rs.getString("exchangerate"));
						
						map.put("uriagedaka", calcRate(rs.getString("uriagedaka"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("uriagesorieki", calcRate(rs.getString("uriagesorieki"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("hanbaihikanrihi", calcRate(rs.getString("hanbaihikanrihi"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("eigyorieki", calcRate(rs.getString("eigyorieki"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("keijorieki", calcRate(rs.getString("keijorieki"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("toku_rieki", calcRate(rs.getString("toku_rieki"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("toku_sonsitu", calcRate(rs.getString("toku_sonsitu"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("tokijunrieki", calcRate(rs.getString("tokijunrieki"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("haitokin", calcRate(rs.getString("haitokin"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("genkasyokyakuhi", calcRate(rs.getString("genkasyokyakuhi"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("eigyo_cf", calcRate(rs.getString("eigyo_cf"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("ryudosisan", calcRate(rs.getString("ryudosisan"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("koteisisan", calcRate(rs.getString("koteisisan"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("sisangokei", calcRate(rs.getString("sisangokei"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("ryudohusai", calcRate(rs.getString("ryudohusai"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("koteihusai", calcRate(rs.getString("koteihusai"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("husaigokei", calcRate(rs.getString("husaigokei"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("sihonkin", calcRate(rs.getString("sihonkin"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("naiburyuho", calcRate(rs.getString("naiburyuho"), erate, (String)map.get("kansan_tuka_cd")));
						map.put("jikosihongokei", calcRate(rs.getString("jikosihongokei"), erate, (String)map.get("kansan_tuka_cd")));
						
						map.put("zaimu_flg", rs.getString("zaimu_flg"));
						map.put("kessanki", Function.insertYmSlash_Cutter(rs.getString("kessanki")));
						
						list.add(map);
						break;
					}finally{
						if(rs != null){
							try{
								rs.close();
							}catch(Exception e){
								throw new SQLException(e.getMessage());
							}
						}
					}
				}
				form.setZaimu_gaiyo(list);
			}

		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch(Exception e){
	    			throw new SQLException(e.getMessage());
				}
			}
		}
	}
	*/

	/**
	 * レート計算。
	 * 
	 * @param val
	 * @param erate
	 * @param tuka_cd
	 * @return
	 */
	private String calcRate(String val, double erate, String tuka_cd) {
		
		double dwork = 0;
		if (val != null) {
			dwork = Function.getValueOfDouble(val);
			dwork = dwork * erate;
			
			if ("0280".equals(tuka_cd)) {
				dwork = dwork / 1000000.0;
				return Function.format("###,###,###,###,###,###,##0",dwork);
			} else {
				dwork = dwork / 1000.0;
				return Function.format("###,###,###,###,###,###,##0",dwork);
			}
		} else {
			return null;
		}

	}
	
	/**
	 * 過去3ヵ年分の財務概況が取得できなかった場合に空のデータを挿入する。
	 * @param form
	 */
	//2008 06/06 新実 全角スペースを半角スペースに置換し、英語OSでの文字化けを修正
	
	private void getEmptyZaimu(TorihikisakiGaiyoSyokaiForm form){
		ArrayList list = form.getZaimu_gaiyo();

		for(int i = list.size();i<3;i++){
			HashMap map = new HashMap();
			map.put("kessanki", " ");
			map.put("kansan_tuka_cd", " ");
			map.put("uriagedaka", " ");
			map.put("uriagesorieki", " ");
			map.put("hanbaihikanrihi", " ");
			map.put("eigyorieki", " ");
			map.put("keijorieki", " ");
			map.put("toku_rieki", " ");
			map.put("toku_sonsitu", " ");
			map.put("tokijunrieki", " ");
			map.put("haitokin", " ");
			map.put("genkasyokyakuhi", " ");
			map.put("eigyo_cf", " ");
			map.put("ryudosisan", " ");
			map.put("koteisisan", " ");
			map.put("sisangokei", " ");
			map.put("ryudohusai", " ");
			map.put("koteihusai", " ");
			map.put("husaigokei", " ");
			map.put("sihonkin", " ");
			map.put("naiburyuho", " ");
			map.put("jikosihongokei", " ");
			map.put("zaimu_flg", " ");
			list.add(map);
		}
		form.setZaimu_gaiyo(list);
	}
		
	/**
	 * 表示単位以下か判定
	 */
	private boolean underUnit(long unit,long val) {
		if (Math.abs(val) < unit) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * 百万単位で表示のため十万単位で四捨五入(マイナス値の場合切り捨て)
	 */
	private long roundJaVal(long val) {
		long result;

		double value = (double)val / 1000000;
		if (val >= 0) {
			result = Math.round(value);
		} else {
			// 表示単位以下切り捨て
			result = (long)Math.floor(value);
		}
		
		return result;
	}
	
	/*
	 * 千単位で表示のため百単位で四捨五入(マイナス値の場合切り捨て)
	 */
	private long roundEnVal(long val) {
		long result;
		
		double value = (double)val / 1000;
		if (val >= 0) {
			result = Math.round(value);
		} else {
			// 表示単位以下切り捨て
			result = (long)Math.floor(value);
		}
		
		return result;
	}	
	
	/**
	 * 区分テーブルから情報取得。
	 * @param map
	 * @param key
	 * @param val
	 * @return
	 * @throws SQLException
	 */
	/* No.845
	private String getHyojiKbn(TorihikisakiGaiyoSyokaiForm form,String key,String mode,String val) throws SQLException {

		ResultSet rs = null;
		String rtn_val= null;
		
		// 表示区分を取得するためのSQL文を作成。
		StringBuffer sql5 = new StringBuffer()
							.append("SELECT ")
							.append("KB.KBN_HYOUJI_VAL ")
							.append("FROM SSP_KBN KB ")
							.append("WHERE ")
							.append("KB.KBN_KEY='")
							.append(key)
							.append("' and KB.LANG_MODE='")
							.append(mode)
							.append("' and KB.KBN_VAL='")
							.append(val)
							.append("'");
		try{
			rs = sqlExec.execQuery(sql5.toString());
			while(rs.next()){
				rtn_val = rs.getString("KBN_HYOUJI_VAL");
			}
		} finally {
			if(rs != null){
				try{
					// Resultset close
					rs.close();
				} catch(Exception e) {
	    			throw new SQLException(e.getMessage());
				}
			}
		}
		return rtn_val;
	}
	*/

	/**
	 * 国情報等の登録.
	 * <p>
	 * 国情報等といった画面へ出力する情報をFormへ登録する処理を行う。
	 * </p>
	 * @param oForm 登録対象のForm
	 * @param oCheck 入力情報
	 * @param oRs 検索結果テーブル
	 * @param sLangMode 言語モード
	 * @return 情報登録後のForm
	 * @throws SQLException DBアクセスエラーが発生した場合
	 */
	private TorihikisakiGaiyoSyokaiForm setCountryAddr(						/* No.845 */
				TorihikisakiGaiyoSyokaiForm oForm, 							/* No.845 */
				InputCheck oCheck, ResultSet oRs, 							/* No.845 */
				String sLangMode) throws SQLException {						/* No.845 */

		// Formへ登録する情報を格納するバッファ。							/* No.845 */
		// 性能への影響を考慮し、StringBufferを用いる						/* No.845 */
		StringBuffer oAddrStr = new StringBuffer(48);						/* No.845 */

		String sStateNmKj = oRs.getString("STATE_NM_KJ");					/* No.845 */
		String sCityNmKj = oRs.getString("CITY_NM_KJ");						/* No.845 */
		String sStreetAdrKj = oRs.getString("STREET_ADR_KJ");				/* No.845 */
		if ("Ja".equals(sLangMode) && 										/* No.845 */
				(!(oCheck.isNullBlank(sStateNmKj))||						/* No.845 */
					!(oCheck.isNullBlank(sCityNmKj))||						/* No.845 */
					!(oCheck.isNullBlank(sStreetAdrKj)))) {					/* No.845 */
			oAddrStr.append(Function.trim(sStateNmKj))						/* No.845 */
					.append(" ")											/* No.845 */
					.append(Function.trim(sCityNmKj))						/* No.845 */
					.append(" ")											/* No.845 */
					.append(Function.trim(sStreetAdrKj));					/* No.845 */
			oForm.setAddress(oAddrStr.toString());							/* No.845 */
		} else {															/* No.845 */
			String sStreetAdr = oRs.getString("STREET_ADR");				/* No.845 */
			String sCityNm = oRs.getString("CITY_NM");						/* No.845 */
			String sStateNm = oRs.getString("STATE_NM");					/* No.845 */
			oAddrStr.append(Function.trim(sStreetAdr))						/* No.845 */
					.append(" ")											/* No.845 */
					.append(Function.trim(sCityNm))							/* No.845 */
					.append(" ")											/* No.845 */
					.append(Function.trim(sStateNm));						/* No.845 */
			oForm.setAddress(oAddrStr.toString());							/* No.845 */
		}																	/* No.845 */
		return oForm;														/* No.845 */
	}																		/* No.845 */

	/**
	 * 財務概況の取得・設定.
	 * <p>
	 * DBアクセスを行い、財務概況情報を取得する。<br>
	 * また、その情報をFormへ設定し、設定後のFormをリターンする。
	 * </p>
	 *
	 * @param oForm 財務概況を登録する対象のForm
	 * @param sAnkenNo 案件番号
	 * @return 財務概況設定後のForm
	 * @throws SQLException DBアクセスエラーが発生した場合
	 */
	private TorihikisakiGaiyoSyokaiForm createZaiGai(						/* No.845 */
						TorihikisakiGaiyoSyokaiForm oForm, 					/* No.845 */
						String sAnkenNo) throws SQLException {				/* No.845 */

		// SQL文字列の作成													/* No.845 */
		StringBuffer oSqlStr = new StringBuffer(4096);						/* No.845 */
		//課題No.233
		//修正開始
		//oSqlStr.append("select z0.syori_kaisu, ")							/* No.233 */
		oSqlStr.append("select ZM.syori_kaisu, ")							/* No.233 */
				.append("PG_SS_FUNCTION.SF_SS_GETNENGETSUABB(ZM.kessanki,ZM.SYSTEM_KBN,'" + cmnData.getComLangMode() + "') AS kessan_ki, ")/* No.233 */
				.append("ZM.kessanki, ZM.kansan_tuka_cd, ")					/* No.845 */
				.append("ZM.exchangerate, ZM.uriagedaka, ")					/* No.845 */
				.append("ZM.uriagesorieki, ZM.hanbaihikanrihi, ")			/* No.845 */
				.append("ZM.eigyorieki, ZM.keijorieki, ")					/* No.845 */
				.append("ZM.toku_rieki, ZM.toku_sonsitu, ")					/* No.845 */
				.append("ZM.tokijunrieki, ZM.haitokin, ")					/* No.845 */
				.append("ZM.genkasyokyakuhi, ZM.eigyo_cf, ")				/* No.845 */
				.append("ZM.ryudosisan, ZM.koteisisan, ")					/* No.845 */
				.append("ZM.sisangokei, ZM.ryudohusai, ")					/* No.845 */
				.append("ZM.koteihusai, ZM.husaigokei, ")					/* No.845 */
				.append("ZM.sihonkin, ZM.naiburyuho, ")						/* No.845 */
				.append("ZM.jikosihongokei, ZM.zaimu_flg, ")				/* No.845 */
				// 表示単位の名称を取得(2009/8/20)
				.append("KB.kbn_hyouji_val,")
				// ここまで
				.append("ZM.jikosihongokei, ZM.zaimu_flg, ")				/* No.845 */
				.append("ZM.new3_flg, DT.ISO_CURRENCY_NM ")					/* No.845 */
				.append("from SST_SATEI_STAT SS LEFT JOIN ")				/* No.845 */
				.append("SSE_TAIHI TT ON SS.ym = TT.ym and ")				/* No.845 */
				.append("SS.mise_cd = TT.office_cd and ")					/* No.845 */
				.append("TRIM(SS.kikan_tori_cd)|| '00' = TT.kikan_tori_cd and ")/* No.845 */
				.append("SS.system_kbn = TT.system_kbn ")					/* No.845 */
				.append("and SS.satei_kaisha_cd = TT.satei_kaisha_cd ")		/* No.233 */
				.append("and SS.syori_kaisu = TT.syori_kaisu ")				/* No.233 */
				.append("left join SSE_ZAIMU zm on ")						/* No.845 */
				.append("TT.ym = ZM.ym and ")								/* No.845 */
				.append("TT.sikibetu_cd = ZM.sikibetu_cd and ")				/* No.845 */
				.append("TT.togo_tori_cd = ZM.duns_no and ")				/* No.845 */
				.append("TT.syori_kaisu = ZM.syori_kaisu ")					/* No.845 */
				.append("and TT.satei_kaisha_cd = ZM.satei_kaisha_cd ")		/* No.233 */
				.append("and TT.office_cd = ZM.office_cd ")					/* No.233 */
				.append("and TT.system_kbn = ZM.system_kbn ")				/* No.233 */
				//.append("inner join (select z.ym, ")						/* No.233 */
				//.append("z.sikibetu_cd, z.duns_no, z.kessanki, ")			/* No.233 */
				//.append("MAX(z.syori_kaisu) syori_kaisu from ")			/* No.233 */
				//.append("SSE_ZAIMU z group by z.ym, z.sikibetu_cd, ")		/* No.233 */
				//.append("z.duns_no, z.kessanki ) z0 on ")					/* No.233 */
				//.append("zm.ym = z0.ym and ")								/* No.233 */
				//.append("zm.sikibetu_cd = z0.sikibetu_cd and ")			/* No.233 */
				//.append("zm.duns_no = z0.duns_no and ")					/* No.233 */
				//.append("zm.kessanki = z0.kessanki and ")					/* No.233 */
				//.append("zm.syori_kaisu = z0.syori_kaisu ")				/* No.233 */
				.append("LEFT JOIN TM_DB_CURENCY_MST@VIR_SJLMA DT ON ")		/* No.845 */
				.append("ZM.kansan_tuka_cd=DT.WB_CURRENCY_CD ")				/* No.845 */
				
				// 表示単位の名称を取得(2009/8/20)
				.append("LEFT JOIN SSP_KBN KB on ")
				.append("KB.KBN_KEY = 'hyouji_tani' and ") 
				.append("KB.system_kbn ='")
				.append(cmnData.getSystem_kbn())
				.append("' and KB.LANG_MODE = '")
				.append(cmnData.getComLangMode()).append("' and ")
				.append("KB.KBN_VAL = ZM.kansan_tuka_cd ")
				// ここまで
				
				.append("where zm.zaimu_flg in ('1', '2', '3') and ")		/* No.845 */
				.append("zm.new3_flg in ('1', '2', '3') and ")				/* No.845 */
				.append("SS.anken_no = '")									/* No.845 */
				.append(sAnkenNo).append(" ' ")								/* No.845 */
				.append("order by zm.new3_flg ");		/* No.845 */
		//修正完了

		ResultSet rs = null;												/* No.845 */
		try {																/* No.845 */
			rs = sqlExec.execQuery(oSqlStr.toString());						/* No.845 */
			
			ArrayList oParamList = oForm.getZaimu_gaiyo();					/* No.845 */
			String sBeforeNew3Flg = null;									/* No.845 */
			while (rs.next()) {												/* No.845 */
				String sNew3Flg = rs.getString("new3_flg");
				if (sBeforeNew3Flg != null && sBeforeNew3Flg.equals(sNew3Flg)) {
					continue;
				}

				HashMap oParamMap = new HashMap();							/* No.845 */

				oParamMap.put("kansan_tuka_cd", rs.getString("kansan_tuka_cd"));/* No.845 */
				oParamMap.put("ISO_CURRENCY_NM", rs.getString("ISO_CURRENCY_NM"));/* No.845 */
				form.setKansan_tuka_cd((String)oParamMap.get("kansan_tuka_cd"));/* No.845 */
				form.setTsuka_cd((String)oParamMap.get("ISO_CURRENCY_NM"));	/* No.845 */
				double erate = Function.getValueOfDouble(rs.getString("exchangerate"));/* No.845 */

				oParamMap.put("uriagedaka", calcRate(rs.getString("uriagedaka"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("uriagesorieki", calcRate(rs.getString("uriagesorieki"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("hanbaihikanrihi", calcRate(rs.getString("hanbaihikanrihi"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("eigyorieki", calcRate(rs.getString("eigyorieki"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("keijorieki", calcRate(rs.getString("keijorieki"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("toku_rieki", calcRate(rs.getString("toku_rieki"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("toku_sonsitu", calcRate(rs.getString("toku_sonsitu"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("tokijunrieki", calcRate(rs.getString("tokijunrieki"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("haitokin", calcRate(rs.getString("haitokin"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("genkasyokyakuhi", calcRate(rs.getString("genkasyokyakuhi"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("eigyo_cf", calcRate(rs.getString("eigyo_cf"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("ryudosisan", calcRate(rs.getString("ryudosisan"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("koteisisan", calcRate(rs.getString("koteisisan"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("sisangokei", calcRate(rs.getString("sisangokei"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("ryudohusai", calcRate(rs.getString("ryudohusai"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("koteihusai", calcRate(rs.getString("koteihusai"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("husaigokei", calcRate(rs.getString("husaigokei"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("sihonkin", calcRate(rs.getString("sihonkin"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("naiburyuho", calcRate(rs.getString("naiburyuho"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("jikosihongokei", calcRate(rs.getString("jikosihongokei"), erate, (String)oParamMap.get("kansan_tuka_cd")));/* No.845 */
				oParamMap.put("zaimu_flg", rs.getString("zaimu_flg"));		/* No.845 */
				oParamMap.put("kessanki", Function.insertYmSlash_Cutter(rs.getString("kessanki")));/* No.845 */
				// 表示単位の名称を取得(2009/8/20)
				oParamMap.put("hyouji_tani", Function.trim(rs.getString("kbn_hyouji_val")));
				// ここまで
				//課題No.233
				//追加開始
				oParamMap.put("kessan_ki",rs.getString("kessan_ki"));
				//追加完了

				oParamList.add(oParamMap);									/* No.845 */
			}																/* No.845 */
			oForm.setZaimu_gaiyo(oParamList);								/* No.845 */
		}finally{															/* No.845 */
			if(rs != null){													/* No.845 */
				try{														/* No.845 */
					rs.close();												/* No.845 */
				}catch(Exception e){										/* No.845 */
					throw new SQLException(e.getMessage());					/* No.845 */
				}															/* No.845 */
			}																/* No.845 */
		}																	/* No.845 */

		return oForm;														/* No.845 */
	}																		/* No.845 */


}
