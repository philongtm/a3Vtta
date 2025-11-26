/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
002		2009/12/1		SSC				課題No.158 年月表示(英語版)を(MM/YYYY)に対応
003		2009/12/3		SSC				課題No.185 ソート順に案件No枝番を追加
******************************************************************************/
package app.commonZen.dbAcc;

import app.SessionDataZen;
import app.commonZen.form.RyuhoSaimuSyokaiForm;
import common.AppContext;
import common.db.CommonDbAcc;
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

/**
 * 留保債務タブDBアクセスクラス
 */
public class RyuhoSaimuSyokaiDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	// アプリケーションContext
	private AppContext appContext = null;
	private SessionDataZen cmnData = null;	// 共通セッションデータ
	private RyuhoSaimuSyokaiForm form = null;	// アクションフォーム	

	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public RyuhoSaimuSyokaiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//Bean取得
		cmnData = appContext.getCMNZenRe();
		form = (RyuhoSaimuSyokaiForm)appContext.getActionForm();
	}

	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {

		ArrayList list = getRyuhoSaimuSyosaiList();

		// 明細件数に取得レコード数を設定
		//form.setCnt_meisai(rs.getMetaData().getColumnCount());
		form.setCnt_meisai(list.size());
		// 明細配列をFormにセット
		form.setAr_meisai(list);
		// Pagerにセット
		form.setPager(list);
	}
	
	public ArrayList getRyuhoSaimuSyosaiList() throws SQLException {
		
		ResultSet rs = null;
		// 検索条件項目取得
		// 案件の登録フェーズ
		String phase = cmnData.getPhase();
		// TODO 取引先コード
		String tori_cd = cmnData.getKanjo_cd();
		// 査定案件No
		String satei_anken_no = cmnData.getSatei_anken_no();		
		InputCheck check = new InputCheck();
		
		//障害No.0035
		//追加開始
		form.setSyoriKaisu(super.getAnkenNoSyoriKaisu(satei_anken_no));
		//追加完了
		
		// 課題No.158
		// 追加開始
		String langmode = cmnData.getComLangMode();
		// 追加完了

		///////////////////////////////////////////////////
		//障害票：267
		//チェックイン日：2008/5/17
		//対応者：SJA中島
		//概要：引当金BS明細などフェーズに依存しないため出力するように修正。
		///////////////////////////////////////////////////
		
        ///////////////////////////////////////////////////
		//障害票：453
		//チェックイン日：2008/5/26
		//対応者：SJA田中
		//概要：画面のメソッドと帳票のメソッドを共通化。
		///////////////////////////////////////////////////
		
		StringBuffer sql = new StringBuffer()
						   .append(" select ")
						   .append(" HB.satei_anken_no, ")
						   .append(" HB.satei_anken_no_eda, ")
						   .append(" HB.cell_cd, ")
						   .append(" HB.ka_cd, ")
						   .append(" HB.tori_cd, ")
						   .append(" SUBSTR(HB.tori_cd,1,7) AS tori_cd_7, ")
						   .append(" HB.kanjo_cd, ")
						   .append(" HB.kanjo_uchi_cd, ")
						   //.append(" TO_CHAR(HB.shusi_dt, 'yyyy/mm/dd') AS shusi_dt, ")
						   .append(" HB.keiyaku_denpyo_no, ")
						   .append(" HB.kaisya_cd,")
						   // 課題No.158
		        			// 追加開始
							.append("PG_SS_FUNCTION.SF_SS_ISLANG(TO_CHAR(HB.shusi_dt,'yyyy/mm/dd'),TO_CHAR(HB.shusi_dt,'mm/dd/yyyy'),'")
							.append(langmode) 
							.append("') AS shusi_dt,")
							/*
						   .append(" HB.shusi_dt, ")
						   */
							// 追加完了
						   .append(" HB.kingaku, ")
						   .append(" HB.tuuka_cd, ")
						   .append(" HB.system_kbn, ")	
						   .append(" RS.ryuhosaimu_kbn,")
						   .append(" RS.biko, ")
						   .append(" K.kanjo_nm, ")
						   .append(" K.kanjo_uchi_nm, ")
						   .append(" TM.business_nm_kj AS torihikisakimei, ")
						   .append(" TM.business_nm AS torihikisakimei_en, ")
						   .append(" MS.satei_kaisya_nm, ")
						   .append(" MS.satei_kaisya_nm_e, ")
						   .append(" TM2.business_nm_kj AS torihikisakimei2, ")
						   .append(" TM2.business_nm AS torihikisakimei_en2, ")
						   .append(" SSM.seisiki_bumon_cd, ")
						   .append(" SSM.bu_cd, ")
						   .append(" SSM.bu_nm, ")
						   .append(" SSM.cell_cd AS cell_cd2, ")
						   .append(" SSM.cell_nm ")
						   .append(" from ")
						   .append(" SST_HIKIATEBSMEISAI HB ")
						   .append(" LEFT JOIN SST_RYUHOSAIMU RS ON ")
						   .append(" HB.satei_anken_no=RS.anken_no ")
						   .append(" AND HB.satei_anken_no_eda=RS.anken_no_eda ")
						   .append(" AND RS.phase ='")
						   .append(phase)
						   .append("' ")
						   .append(" LEFT JOIN SSM_KANJO K ON ")
						   .append(" HB.system_kbn = K.system_kbn ")
						   .append(" AND HB.kanjo_cd = K.kanjo_cd ")
						   .append(" AND HB.kanjo_uchi_cd = K.kanjo_uchi_cd")
						   .append(" AND HB.sateikaisya_cd = K.sateikaisya_cd")
						   .append(" AND HB.mise_cd = K.mise_cd")
						   .append(" LEFT JOIN SST_SATEI_STAT SS ON ")
						   .append(" HB.satei_anken_no = SS.anken_no LEFT JOIN ")
						   .append(" SSE_TAIHI TT ON ")
						   .append(" SS.ym = TT.ym ")
						   .append(" AND SS.mise_cd = TT.office_cd ")
						   .append(" AND TRIM(SS.kikan_tori_cd) || '00' = TT.kikan_tori_cd ")
						   .append(" AND SS.satei_kaisha_cd = TT.satei_kaisha_cd ")
						   .append(" AND SS.system_kbn = TT.system_kbn LEFT JOIN ")
						   .append(" SSE_TOGO_MST TM ON ")
						   .append(" TT.ym = TM.ym ")
						   .append(" AND TT.sikibetu_cd = TM.sikibetu_cd ")
						   .append(" AND TT.togo_tori_cd = TM.togo_tori_cd ")
						   .append(" AND TT.satei_kaisha_cd = TM.satei_kaisha_cd ")
						   .append(" AND TT.syori_kaisu = TM.syori_kaisu LEFT JOIN ")
						   .append(" SSM_SATEIKAISYA MS ON ")
						   .append(" TRIM(SS.kaisha7_cd) = TRIM(MS.kaisha_cd)")
						   .append(" AND SS.system_kbn = MS.system_kbn")
						   .append(" AND SS.satei_kaisha_cd = MS.satei_kaisha_cd LEFT JOIN ")
						   .append(" SSE_TAIHI TT2 ON ")
							//障害No.0035
							//追加開始
						   .append("TT.syori_kaisu = TT2.syori_kaisu and")
							//追加完了
						   .append(" HB.ym = TT2.ym ")
						   .append(" AND HB.mise_cd = TT2.office_cd ")
						   .append(" AND TRIM(HB.tori_cd_5) = substr(TT2.kikan_tori_cd,1,5) ")
						   .append(" AND HB.sateikaisya_cd = TT2.satei_kaisha_cd ")
						   .append(" AND HB.system_kbn = TT2.system_kbn LEFT JOIN ")
						   .append(" SSE_TOGO_MST TM2 ON ")
						   .append(" TT2.ym = TM2.ym ")
						   .append(" AND TT2.sikibetu_cd = TM2.sikibetu_cd ")
						   .append(" AND TT2.togo_tori_cd = TM2.togo_tori_cd ")
						   .append(" AND TT2.satei_kaisha_cd = TM2.satei_kaisha_cd ")
						   .append(" AND TT2.syori_kaisu = TM2.syori_kaisu LEFT JOIN ")
// 障害票No499 2008/06/01 細野 組織変更情報取得もとの統一
						   .append(" VW_SS_SOHEN SSM ON ")
						   .append(" TRIM(HB.kaisya_cd)  = SSM.original_kaisya_cd ")
						   .append(" AND TRIM(HB.cell_cd) = SSM.original_cell_cd ")
						   .append(" AND TRIM(HB.ka_cd)  = SSM.ka_cd ")
						   .append(" where ")
						   .append(" HB.tori_cd_5 ='")
						   .append(tori_cd)
						   .append("' and HB.satei_anken_no ='")
						   .append(satei_anken_no)
						   .append("'")
							//障害No.0035
							//追加開始
							.append(" and TT.syori_kaisu = '")
							.append(form.getSyoriKaisu())
							//追加完了
						   .append("' AND K.saiken_flg ='4' ")
						    // 課題No.185
						    // 追加開始
							//.append("ORDER BY HB.ka_cd ASC, HB.cell_cd ASC, HB.kanjo_cd ASC, HB.kanjo_uchi_cd ASC, HB.kingaku DESC");
							.append("ORDER BY HB.ka_cd ASC, HB.cell_cd ASC, HB.kanjo_cd ASC, HB.kanjo_uchi_cd ASC, HB.kingaku DESC, HB.satei_anken_no_eda ASC");
							// 追加完了

		ArrayList list = new ArrayList();	
		try{
			// SQL実行		
			rs = sqlExec.execQuery(sql.toString());

			// 明細情報格納カウンタ
			int i = 0;

			while ( rs.next() ) {
				// 明細Beanクラス生成
				HashMap map = new HashMap();
			    map.put("satei_anken_no", rs.getString("satei_anken_no"));
			    map.put("satei_anken_no_eda", rs.getString("satei_anken_no_eda"));
			    map.put("cell_cd", rs.getString("cell_cd"));
			    map.put("ka_cd", rs.getString("ka_cd"));
			    map.put("kanjo_cd", rs.getString("kanjo_cd"));
			    map.put("kanjo_uchi_cd", rs.getString("kanjo_uchi_cd"));
			    //map.put("shusi_dt", rs.getString("shusi_dt"));
			    //map.put("keiyaku_denpyo_no", rs.getString("keiyaku_denpyo_no"));
			    map.put("kaisya_cd",rs.getString("kaisya_cd"));
			    // 課題No.158
			    // 追加開始
			    //map.put("shusi_dt", Function.insertDateSlash(rs.getDate("shusi_dt")));
			    map.put("shusi_dt", Function.insertDateSlash(rs.getString("shusi_dt"),langmode));
			    // 追加完了
			    map.put("keiyaku_denpyo_no", rs.getString("keiyaku_denpyo_no"));
			    map.put("kingaku", Function.format("###,###,###,##0",rs.getDouble("kingaku")));
			    map.put("kanjo_nm", rs.getString("kanjo_nm"));
			    map.put("kanjo_uchi_nm", rs.getString("kanjo_uchi_nm"));
			    map.put("torihikisakimei", rs.getString("torihikisakimei"));
			    map.put("torihikisakimei_en", rs.getString("torihikisakimei_en"));
			    map.put("satei_kaisya_nm", rs.getString("satei_kaisya_nm"));
			    map.put("satei_kaisya_nm_e", rs.getString("satei_kaisya_nm_e"));
			    map.put("tori_cd", rs.getString("tori_cd"));
			    map.put("tori_cd_7", rs.getString("tori_cd_7"));
			    map.put("torihikisakimei2",rs.getString("torihikisakimei2"));
			    map.put("torihikisakimei_en2", rs.getString("torihikisakimei_en2"));
			    map.put("seisiki_bumon_cd", rs.getString("seisiki_bumon_cd"));
			    map.put("bu_cd", rs.getString("bu_cd"));
			    map.put("bu_nm", rs.getString("bu_nm"));
			    map.put("cell_cd2", rs.getString("cell_cd2"));
			    map.put("cell_nm", rs.getString("cell_nm"));

				String biko = checkNull(check,rs.getString("biko"));
				///////////////////////////////////////////
				//障害表：482,486
				//チェックイン日：2008/5/29
				//対応者：SJA中島
				//概要：改行コードをBRタグに置き換える。
				///////////////////////////////////////////
				map.put("biko",biko);
				
				// 障害No369 2008.05.16 uechi 区分を格納
				map.put("ryuhosaimu_kbn", rs.getString("ryuhosaimu_kbn"));
				if(GS.LANG_JA.equals(cmnData.getComLangMode())){
					if("1".equals(rs.getString("ryuhosaimu_kbn"))){
						map.put("ryuhosaimu_hyoji_kbn","○");
					}else if("0".equals(rs.getString("ryuhosaimu_kbn"))){
						map.put("ryuhosaimu_hyoji_kbn","×");						
					}
				}else{
					if("1".equals(rs.getString("ryuhosaimu_kbn"))){
						// 障害No369 2008.05.16 uechi 全角Ｙ⇒半角Y
						map.put("ryuhosaimu_hyoji_kbn","Y");
					}else if("0".equals(rs.getString("ryuhosaimu_kbn"))){
						// 障害No369 2008.05.16 uechi 全角Ｎ⇒半角N
						map.put("ryuhosaimu_hyoji_kbn","N");						
					}
				}
			    
			    // 組織変更情報から表示項目取得
			    //map = getSoshiki(map);
					
			    // 留保債務から情報取得
			    //map = getRyuho(map);
			    
				// 通貨コードを取得(modify tuuka_cd 2008/4/15 nakajima)
			    map.put("tuuka_cd",Function.trim(rs.getString("tuuka_cd")));
			    
				// 明細配列に取得レコードを格納
			    list.add(i, map);
			    i++;   
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
		return list;
	}
	
//	/**
//	 * 通貨コードを取得し、mapにセットする。
//	 * @param map
//	 * @return
//	 * @throws SQLException
//	 */
//	private HashMap getTuuka_cd(HashMap map) throws SQLException{
//		ResultSet rs = null;
//		
//		// Duns No
//		String duns_no = cmnData.getDuns_no();
//		
//		StringBuffer sql = new StringBuffer()
//							.append(" select ")
//							.append(" DT.ISO_CURRENCY_NM ")
//							.append(" from ")
//							.append(" TM_DB_KIHON_TBL@VIR_SJLMA DC, ")
//							.append(" TM_DB_CURENCY_MST@VIR_SJLMA DT ")
//							.append(" where ")
//							.append(" DC.DUNS_NO ='")
//							.append(duns_no)
//							.append("' and DC.CURRENCY_CD = DT.WB_CURRENCY_CD ");
//		
//		try{
//			rs = sqlExec.execQuery(sql.toString());
//			while(rs.next()){
//				map.put("tuuka_cd",rs.getString("ISO_CURRENCY_NM"));
//			}
//		}finally{
//			if(rs != null){
//				try{
//					rs.close();
//				}catch(Exception e){
//					throw new SQLException(e.getMessage());
//					
//				}
//			}
//		}
//		return map;
//	}
	
//	/**
//	 * 留保債務テーブルから表示項目を取得する。
//	 * @param map
//	 * @return
//	 * @throws SQLException
//	 */
//	private HashMap getRyuho(HashMap map) throws SQLException {
//
//		ResultSet rs = null;
//		InputCheck check = new InputCheck();
//		
//		StringBuffer sql2 = new StringBuffer()
//							.append(" SELECT ")
//							.append(" RS.ryuhosaimu_kbn,")
//							.append(" RS.biko ")
//							.append(" FROM SST_RYUHOSAIMU RS ")
//							.append(" WHERE ")
//							.append(" RS.anken_no= '")
//							.append(map.get("satei_anken_no"))
//							.append("' and RS.anken_no_eda= '")
//							.append(map.get("satei_anken_no_eda"))
//							.append("' and RS.phase= '")
//							.append(cmnData.getPhase())
//							.append("'");
//		try{
//			rs = sqlExec.execQuery(sql2.toString());
//			map.put("ryuhosaimu_kbn",Function.getStringOfInt(0));
//			map.put("biko","");
//			while ( rs.next() ) {
//				map.put("ryuhosaimu_kbn",rs.getString("ryuhosaimu_kbn"));
//				String biko = checkNull(check,rs.getString("biko"));
//				///////////////////////////////////////////
//				//障害表：482,486
//				//チェックイン日：2008/5/29
//				//対応者：SJA中島
//				//概要：改行コードをBRタグに置き換える。
//				///////////////////////////////////////////
//				map.put("biko",biko);
//				
//				if(GS.LANG_JA.equals(cmnData.getComLangMode())){
//					if("1".equals(rs.getString("ryuhosaimu_kbn"))){
//						map.put("ryuhosaimu_hyoji_kbn","○");
//					}else{
//						map.put("ryuhosaimu_hyoji_kbn","×");						
//					}
//				}else{
//					if("1".equals(rs.getString("ryuhosaimu_kbn"))){
//						map.put("ryuhosaimu_hyoji_kbn","Ｙ");
//					}else{
//						map.put("ryuhosaimu_hyoji_kbn","Ｎ");						
//					}
//				}
//				
//			}
//		} finally {
//			if(rs != null){
//				try{
//					// Resultset close
//					rs.close();
//				} catch(Exception e) {
//					throw new SQLException(e.getMessage());
//				}
//			}
//		}
//		return map;
//	}
	

//	/**
//	 * 引数に指定されたHashMapに格納されているセルコードから、
//	 * 部名称、セル名称を取得し、HashMapに設定し返します。
//	 * @param map  設定するHashMap
//	 * @return  部名称、セル名称が格納されたHashMap
//	 * @throws SQLException
//	 */
//	private HashMap getSoshiki(HashMap map) throws SQLException {
//
//		ResultSet rs = null;
//
//		// 障害票No499 2008/05/27 細野 セルコード、名称を債権明細タブの取得法と統一する。		
//		StringBuffer sql2 = new StringBuffer()
//							.append("SELECT ")
//							.append("SH.cell_nm,")
//							.append("SH.bu_nm ")
//							.append("FROM VW_SS_SOHEN SH ")
//							.append("WHERE ")
//							.append("SH.original_cell_cd= '")
//							.append(map.get("cell_cd"))
//							.append("' and SH.original_kaisya_cd = '")
//							.append(map.get("kaisya_cd"))
//							.append("'");
//		try{
//			rs = sqlExec.execQuery(sql2.toString());
//			while ( rs.next() ) {
//				map.put("cell_nm",rs.getString("cell_nm"));
//				map.put("bu_nm",rs.getString("bu_nm"));
//			}
//		} finally {
//			if(rs != null){
//				try{
//					// Resultset close
//					rs.close();
//				} catch(Exception e) {
//					throw new SQLException(e.getMessage());
//				}
//			}
//		}
//		return map;
//	}
	/**
	 * NULLやブランクの場合、空文字を返す
	 * @return
	 */
	private String checkNull(InputCheck check,String val) {
		
		String result = val;
		
		if (check.isNullBlank(val)) {
			result = "";
		}
		
		return result;
	}
	/**
	 * 件数セレクトボックス値設定
	 */
	public void setShowKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("show",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap showKbn = new LinkedHashMap(getRsCount(rs));
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    showKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setShowList(showKbn);
			
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
	 * 区分値取得SQL取得
	 */
	private String getKbnSql(String key,String langMode) {
		
		StringBuffer sql = new StringBuffer().append("SELECT KB.kbn_hyouji_val,")
											.append("KB.kbn_val")
											.append(" FROM SSP_KBN KB")
											.append(" WHERE KB.kbn_key='")
											.append(key)
											.append("' and KB.lang_mode='")
											.append(langMode)
											.append("' and KB.system_kbn ='")
											.append(cmnData.getSystem_kbn())
											.append("' ORDER BY KB.kbn_order ASC");
		
		return sql.toString();
	}
	
	/**
	 * Resultsetの件数取得<br>
	 * 
	 * @param ResultSet
	 * @return int
	 */
	protected int getRsCount(ResultSet rs) {
	    try {
	        rs.last();
	        int count = rs.getRow();
	        rs.beforeFirst();
		    return count;
	    } catch(SQLException e) {
	        return 0;
	    }
	}
}
