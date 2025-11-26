/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/20		SSC				1.5次版機能組込
002		09/11/12		SSC				課題No.106 チャンピオン部登録処理修正
******************************************************************************/
package app.system.dbAcc;

import app.SessionDataZen;
import app.system.form.ChampionForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
* チャンピオン部メンテナンス画面DBアクセスクラス
*/
public class ChampionDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;		// 共通セッションデータ
	private ChampionForm form = null;			// アクションフォーム	
	
	// INパラメータ
	private String userId;						// ユーザＩＤ（統合ＩＤ）
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public ChampionDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (ChampionForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = cmnData.getComUserId();
	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    userId = null;
	}
	
	/**
	 * 検索SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void execute() throws SQLException {
		ResultSet rs = null;
		ResultSet rs_bu = null;
		PreparedStatement ps = null;
		PreparedStatement ps_bu = null;
		
		Connection con = null;

	    try {
	    	
	    	// 査定会社コードの取得。
	    	ArrayList satei_kaisya_list = getUserSateiKaisya();
	    	String satei_kaisya_list_cnt = "";
			for(int cnt =0;cnt < satei_kaisya_list.size();cnt ++){
				satei_kaisya_list_cnt = satei_kaisya_list_cnt + "?"+",";
			}
	    	
	    	// コネクションの取得
	    	con = sqlExec.getConnection();
	    	
	    	// No410, 2008/05/23, SJA渡辺, 通貨コード等取得個所修正
			// SQL作成(取引先毎の集約レコード)
		    StringBuffer sql = new StringBuffer("SELECT CP.system_kbn, ")
									.append("CP.satei_kaisha_cd AS kaisya, ")
									//課題No.106
									//追加開始
									.append("CP.satei_ki, ")
									.append("CP.kijunbi_kbn, ")
									//追加完了
									.append("CP.ym, ")
									.append("CP.mise_cd, ")
									.append("CP.syori_kaisu, ")
									.append("CP.tori_cd AS kanjo_cd, ")
									.append("TM.business_nm_kj, ")
									.append("TM.business_nm, ")
									.append("TM.country_nm AS syozaikoku, ")
									.append("CP_TMP.saiken_total, ")
									// 障害対応：200807240003　2008/7/24 中島 通貨コードをＤ＆Ｂ通貨から取得するように修正
									.append("BS.TUUKA_CD AS tuuka_nm ")
									.append("FROM ")
									.append("SST_CHAMPIONBU CP ")
									//障害対応：200807240003　2008/7/24　中島　引当金BS明細を使用しないように修正
									.append("LEFT JOIN TM_TAIHI_TBL@VIR_SJLMA TH ")
									.append("ON TRIM(CP.tori_cd) || '00' = TH.kikan_tori_cd ")
									.append("AND CP.MISE_CD=TH.OFFICE_CD ")
									.append("AND CP.SYSTEM_KBN = TH.SYSTEM_KBN ")
									.append("AND TH.DEL_FLAG = '0' ")
									.append("LEFT JOIN TM_TOGO_MST@VIR_SJLMA TM ON ")
									.append("TH.togo_tori_cd = TM.togo_tori_cd ")
									.append("LEFT JOIN SSW_BSMEISAI BS ON ")
									.append("TRIM(CP.tori_cd) = TRIM(BS.TORI_CD_5) ")
									.append("AND CP.MISE_CD=BS.MISE_CD ")
									.append("AND CP.SYSTEM_KBN = BS.SYSTEM_KBN ")
									.append("AND CP.SATEI_KAISHA_CD = BS.SATEIKAISYA_CD ")
									.append("LEFT JOIN ")
									.append("(SELECT system_kbn, ")
									.append("satei_kaisha_cd, ")
									.append("ym, ")
									.append("tori_cd, ")
									.append("syori_kaisu, ")
									.append("SUM(kingaku) AS saiken_total ")
									.append("FROM ")
									.append("SST_CHAMPIONBU ")
									.append("GROUP BY ")
									.append("system_kbn, ")
									.append("satei_kaisha_cd, ")
									.append("ym, ")
									.append("syori_kaisu, ")
									.append("tori_cd) CP_TMP ")
									.append("ON CP.system_kbn = CP_TMP.system_kbn ")
									.append("AND CP.satei_kaisha_cd = CP_TMP.satei_kaisha_cd ")
									.append("AND CP.ym = CP_TMP.ym ")
									.append("AND CP.syori_kaisu = CP_TMP.syori_kaisu ")
									.append("AND CP.tori_cd = CP_TMP.tori_cd ")
									.append("WHERE CP.champion_bu_jyufuku_flg = ? ")
									.append("AND CP.kingaku !='0' ")
									.append("AND CP.satei_kaisha_cd in (")
									.append(satei_kaisya_list_cnt.substring(0,satei_kaisya_list_cnt.length()-1))
		    					 	.append(") GROUP BY ")
									.append("CP.system_kbn, ")
									.append("CP.satei_kaisha_cd, ")
									//課題No.106
									//追加開始
									.append("CP.satei_ki, ")
									.append("CP.kijunbi_kbn, ")
									//追加完了
									.append("CP.ym, ")
									.append("CP.syori_kaisu, ")
									.append("CP.mise_cd, ")
									.append("CP.tori_cd, ")
									.append("TM.business_nm_kj, ")
									.append("TM.business_nm, ")
									.append("TM.country_nm, ")
									.append("CP_TMP.saiken_total, ")
									.append("BS.TUUKA_CD ")
									.append("ORDER BY kanjo_cd, ym DESC, kaisya");
									
		
			// SQL実行(取引先毎の集約レコ\ード)
            // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
		    //log.write(GS.LOG_INF,CLASSNAME, "実行SQL(取引先毎)：" + sql);
	    	ps = con.prepareStatement(sql.toString());
	    	int psCnt = 0;
	    	ps.setString(++psCnt, "1");			// チャンピオン部重複フラグ
	    	// No523 2008/5/30 SJA中島 ログインユーザの査定会社コードを検索条件に追加
	    	Iterator itr = satei_kaisya_list.iterator();
	    	while(itr.hasNext()){
		    	ps.setString(++psCnt,(String)itr.next());			// ログインユーザ査定会社コード
	    	}
	    	// 障害管理：200808250004 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	
	        // ActionForm 明細件数に取得レコード数を格納
			form.setCnt_meisai(getRsCount(rs));
	
		    // ActionForm に取得値を格納
		    List ar_meisai = new ArrayList();	// 明細配列	
			int i = 0;
			InputCheck check = new InputCheck();
			
			while ( rs.next() ) {
			    HashMap map = new HashMap();
			    map.put("system_kbn", rs.getString("system_kbn"));			// システム区分
			    map.put("kanjo_cd", rs.getString("kanjo_cd"));				// 勘定先ＣＤ
			    if ("Ja".equals(cmnData.getComLangMode()) && !(check.isNullBlank(rs.getString("business_nm_kj")))) {
			    	map.put("kanjo_nm", rs.getString("business_nm_kj"));				// 勘定先名称
				} else {
					map.put("kanjo_nm", rs.getString("business_nm"));				// 勘定先名称
				}
			    map.put("syozaikoku", rs.getString("syozaikoku"));			// 所在国
				//課題No.106
				//追加開始
			    map.put("satei_ki", rs.getString("satei_ki"));				// 査定期
			    map.put("kijunbi_kbn", rs.getString("kijunbi_kbn"));		// 基準日区分
				//追加完了
			    map.put("ym", rs.getString("ym"));							// 対象年月
			    map.put("ymSlash", 
			    		Function.insertYmSlash(rs.getString("ym")));		// 対象年月(スラッシュ付)
			    map.put("saiken_total", 
			    		Function.format("#,###,###,###,###,##0.##", rs.getDouble("saiken_total")));	// 債権残計
			    map.put("kaisya", rs.getString("kaisya"));					// 会社
			    map.put("tuuka_nm", rs.getString("tuuka_nm"));				// 通貨コード
			    map.put("mise_cd", rs.getString("mise_cd"));				// 店コード
			    map.put("syori_kaisu", rs.getString("syori_kaisu"));		// 処理回数
			    
			    
			    
			    // SQL作成(部毎のレコード)
			    StringBuffer sql_bu = new StringBuffer("SELECT CP.bu_cd AS soshiki_cd, ")
												.append("CP.bu_nm AS soshiki_nm, ")
												.append("CP.seisiki_bumon_cd, ")
												.append("SUM(CP.kingaku) AS saiken_total_so, ")
												.append("TH_TMP.tairyu_total ")
												.append("FROM ")
												.append("SST_CHAMPIONBU CP ")
												.append("LEFT JOIN ")
												.append("(SELECT ")
												.append("system_kbn, sateikaisya_cd, ym, tori_cd_5, ")
												.append("SUBSTR(ka_cd, 1, 5) AS bu_cd, ")
												.append("SUM(kingaku) AS tairyu_total ")
												.append("FROM ")
												.append("SST_TAIRYUHANTEIMEISAI ")
												.append("GROUP BY ")
												.append("system_kbn, sateikaisya_cd, ym, tori_cd_5, ")
												.append("SUBSTR(ka_cd, 1, 5)) ")
												.append("TH_TMP ")
												.append("ON ")
												.append("CP.system_kbn = TH_TMP.system_kbn AND ")
												.append("CP.satei_kaisha_cd = TH_TMP.sateikaisya_cd AND ")
												.append("CP.ym = TH_TMP.ym AND ")
												.append("TRIM(CP.tori_cd) = TH_TMP.tori_cd_5 AND ")
												.append("SUBSTR(CP.bu_cd , 1, 5) = TH_TMP.bu_cd ")
												.append("WHERE ")
												.append("CP.champion_bu_jyufuku_flg = ? AND ")
												.append("CP.kingaku != '0' AND ")
												.append("CP.system_kbn = ? AND ")
												.append("CP.satei_kaisha_cd = ? AND ")
												.append("CP.ym = ? AND ")
												.append("CP.syori_kaisu = ? AND ")
												.append("CP.tori_cd = ? ")
												.append("GROUP BY ")
												.append("CP.system_kbn, ")
												.append("CP.satei_kaisha_cd, ")
												.append("CP.ym, ")
												.append("CP.syori_kaisu, ")
												.append("CP.tori_cd, ")
												.append("CP.bu_cd, ")
												.append("CP.bu_nm, ")
												.append("CP.seisiki_bumon_cd, ")
												.append("TH_TMP.tairyu_total ")
												.append("ORDER BY CP.bu_cd");
	
				// SQL実行(部毎のレコード)		
                // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
			    //log.write(GS.LOG_INF,CLASSNAME, "実行SQL(部毎)：" + sql_bu);

			    List ar_meisai_bu = new ArrayList();	// 明細配列(部)
		    	
		    	try{
		    		ps_bu = con.prepareStatement(sql_bu.toString());
		    		ps_bu.setString(1, "1");								// チャンピオン部重複フラグ
		    		ps_bu.setString(2, rs.getString("system_kbn"));		// システム区分
		    		ps_bu.setString(3, rs.getString("kaisya"));			// 会社コード
		    		ps_bu.setString(4, rs.getString("ym"));				// 年月
		    		ps_bu.setString(5, rs.getString("syori_kaisu"));		// 処理回数
		    		ps_bu.setString(6, rs.getString("kanjo_cd"));			// 取引先コード
		    		// 障害管理：200808250004 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
			    	rs_bu = sqlExec.execQueryP(ps_bu);
			    	
    			    int j = 0;
    			    while ( rs_bu.next() ) {
    				    HashMap map_bu = new HashMap();
    				    map_bu.put("soshiki_cd", rs_bu.getString("soshiki_cd"));				// 組織コード
    				    map_bu.put("soshiki_cd_view", rs_bu.getString("soshiki_cd").trim());	// 組織コード
    				    map_bu.put("soshiki_nm", Function.trim(rs_bu.getString("soshiki_nm")));				// 組織名称	
    				    map_bu.put("seisiki_bumon_cd", rs_bu.getString("seisiki_bumon_cd"));	// 正式部門				
    				    map_bu.put("tairyu_total", 
			    		Function.format("###,###,###,###,##0.##", rs_bu.getDouble("tairyu_total")));	// 滞留判定対象金額計
	   				    map_bu.put("saiken_total_so", 
			    		Function.format("###,###,###,###,##0.##", rs_bu.getDouble("saiken_total_so")));	// 債権残計（組織毎）

	   				    ar_meisai_bu.add(j, map_bu);
	   				    j++;
	   			    }
    			    map.put("bu_meisai", ar_meisai_bu);
    			    map.put("count", Integer.toString(j + 1));		// 勘定先毎の明細行数（部のレコード件数 + 1）
		    	}finally{
		    		if(rs_bu != null){
		    			try{
		    				rs_bu.close();
		    			}catch(Exception e){
		    				throw new SQLException(e.getMessage());
		    			}
		    		}
		    		if(ps_bu != null){
		    			try{
		    				ps_bu.close();
		    			}catch(Exception e){
		    				throw new SQLException(e.getMessage());
		    			}
		    		}
		    	}
			    
			    // 明細配列に取得レコードを格納
			    ar_meisai.add(i, map);
			    i++;
			}
		    
			// ActionForm に明細配列を格納
			form.setAr_meisai(ar_meisai);    
			form.setPager(ar_meisai);
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// No465, 2008/05/26, SJA渡辺, コネクションは閉じないように修正
			/*if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}*/
		}
	}
	
	/**
	 * 登録処理 <br>
	 * 
	 * @return 登録成功true、登録失敗false
	 * @exception SQLException
	 */
	public boolean doUpdate() throws Exception {
		// 更新日付取得
		String sysdate = sqlExec.getDate();
		    
		StringBuffer sql = null;
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/17
//対応者 　福士
//修正概要
//③登録値の取得ミスを修正。
//////////////////////////////////////	/
		List list = form.getAr_meisai();
		String[] selectArr = form.getSelectArr();
		String[] index = new String[2];

		for (int i = 0; i < selectArr.length; i++) {
			index = selectArr[i].split("-");
			
			// 更新情報取得
			HashMap map = (HashMap)list.get(Integer.parseInt(index[0]));
		    String system_kbn = (String)map.get("system_kbn");		// システム区分
		    String kaisya = (String)map.get("kaisya");				// 会社
			//課題No.106
			//追加開始
		    String satei_ki = (String)map.get("satei_ki");			// 査定期
		    String kijunbi_kbn = (String)map.get("kijunbi_kbn");	// 基準日区分
			//追加完了
		    String taisyo_ym = (String)map.get("ym");				// 対象年月
		    String mise_cd = (String)map.get("mise_cd");			// 店コード
		    if (mise_cd == null) {
		    	mise_cd ="";
		    }
		    String kanjo_cd = (String)map.get("kanjo_cd");			// 勘定先ＣＤ
		    String kanjo_nm = (String)map.get("kanjo_nm");			// 勘定先名称
//		  障害票No345　2008/05/19　細野　処理回数の取得
		    String syori_kaisu = (String)map.get("syori_kaisu");	// 処理回数

		    List list_bu = (List)map.get("bu_meisai");
		    HashMap map_select_bu = (HashMap)list_bu.get(Integer.parseInt(index[1]));
		    String select_bu_cd = (String)map_select_bu.get("soshiki_cd");	// 選択した部コード
		    String select_bu_nm = (String)map_select_bu.get("soshiki_nm");	// 選択した部名称
		    String select_sei_bu_cd = (String)map_select_bu.get("seisiki_bumon_cd");	// 選択した正式部門
		    if (select_sei_bu_cd == null) {
		    	select_sei_bu_cd = "";
		    }
						    
			// 登録前処理
		    //課題No.106
			//追加開始
		   	ResultSet rsKaCd = null;
		   	String ka_cd = GS.EMPTY_CHARCTER;
		   	sql = null;
		   	sql = new StringBuffer().append("SELECT MIN(M12.ka_cd) AS ka_cd FROM SSM_TOGO_SOSHIKI M12 WHERE ")
		   							.append("M12.system_kbn = '")
		  					 		.append(system_kbn)
		   							.append("' AND M12.satei_kaisha_cd = '")
							   		.append(kaisya)
							   		.append("' AND M12.ym = '")
							   		.append(taisyo_ym)
							   		.append("' AND M12.bumon_cd = '")
									.append(select_sei_bu_cd)
									.append("' AND M12.bu_cd = '")
									.append(select_bu_cd)
									.append("'");
		   	try {
		   		rsKaCd = sqlExec.execQuery(sql.toString());
		   		while(rsKaCd.next()){
		   			ka_cd = rsKaCd.getString("ka_cd");
		   		}
		   	} finally {
		   		if (rsKaCd != null) {
		   			try {
		   				rsKaCd.close();
		   			} catch (Exception e) {
		   				throw new SQLException(e.getMessage());
		   			}
		   		}
		   	}
			//追加完了
		   	
		    //課題No.106
			//削除開始
			/*ArrayList ymList = new ArrayList(); 
			for (int j = 0; j < 12; j++) {
			   	ResultSet rs = null;
						    	
			   	String set_taisyo_ym = Function.calcYmNngetsu(taisyo_ym, j+1);
						    	
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				// StringBufferの初期化
			   	sql = null;
			   	// 登録前存在チェック
			   	sql = new StringBuffer().append("SELECT tori_cd FROM SST_CHAMPIONBU WHERE ")
							.append("system_kbn = '")
							.append(system_kbn)
							.append("' AND satei_kaisha_cd = '")
							.append(kaisya)
							.append("' AND ym = '")
							.append(set_taisyo_ym)
							.append("' AND tori_cd = '")
							.append(kanjo_cd)
							.append("' AND bu_cd = '")
							.append(select_bu_cd)
							.append("'");
				// No465, 2008/05/24, SJA渡辺, SQLのカーソルがたまる問題処理修正のため、ResultSetをfinallyの中でcloseするよう修正
			   	try {
			   		rs = sqlExec.execQuery(sql.toString());
			   		
			   		if (getRsCount(rs) == 0) {
			   			ymList.add(set_taisyo_ym);
			   		}
			   	} finally {
			   		if (rs != null) {
			   			try {
			   				rs.close();
			   			} catch (Exception e) {
			   				throw new SQLException(e.getMessage());
			   			}
			   		}
			   	}
			}*/
			//削除完了
						
			// 更新・登録処理開始
			sqlExec.beginTran();
							
			for (int k = 0; k < list_bu.size(); k++) {
			    HashMap map_bu = (HashMap)list_bu.get(k);
			    String soshiki_cd = (String)map_bu.get("soshiki_cd");	// 組織コード
							    
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				// StringBufferの初期化
			   	sql = null;
			    // SQL設定（チャンピオン部情報更新）
			   	sql = new StringBuffer().append("UPDATE SST_CHAMPIONBU CP SET ");
			   	if (k == Integer.parseInt(index[1])) {
			   		sql.append("CP.champion_bu_flg = '1', ");
// 障害票No345　2008/05/19　細野　チャンピオンフラグのクリア
			   	}else{
			   		sql.append("CP.champion_bu_flg = '', ");
			   	}
// 障害票No345　2008/05/19　細野　フラグをNull,条件に処理回数を加える
			   	sql.append("CP.champion_bu_jyufuku_flg = '', CP.upd_user= '")
				   .append(userId)
				   .append("', CP.upd_dt= TO_DATE('")
				   .append(sysdate)
				   .append("', 'yyyy/mm/dd hh24:mi:ss') WHERE CP.system_kbn = '")
				   .append(system_kbn)
				   .append("' AND CP.satei_kaisha_cd = '")
				   .append(kaisya)
				   .append("' AND CP.ym = '")
				   .append(taisyo_ym)
				   .append("' AND CP.tori_cd = '")
				   .append(kanjo_cd)
				   .append("' AND CP.syori_kaisu = '")
				   .append(syori_kaisu)
				   .append("' AND CP.bu_cd = '")
				   .append(soshiki_cd)
				   .append("'");
			   	sqlExec.addBatch(sql.toString());
			}
						    
		    //課題No.106
			//削除開始
			// SQL設定（チャンピオン部情報登録）
			/*for (int l = 0; l < ymList.size(); l++) {
						    			    	
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
				// StringBufferの初期化
			   	sql = null;
				// 登録処理
			    sql = new StringBuffer().append("INSERT INTO SST_CHAMPIONBU VALUES ('")
							.append(system_kbn)
							.append("', '")
							.append(kaisya)
							.append("', '")
							.append(ymList.get(l))
							.append("', '")
							.append("1")				// 処理回数："1"を設定
							.append("', '")
							.append(mise_cd)
							.append("', '")
							.append(kanjo_cd)
							.append("', '")
							.append(kanjo_nm)
							.append("', '")
							.append(select_bu_cd)
							.append("', '")
							.append(select_bu_nm)
							.append("', '")
							.append(select_sei_bu_cd)
//障害票No345　2008/05/19　細野　フラグをNullに設定
							.append("', '0', '', '', NULL, NULL, NULL, NULL, '")
							.append(userId)
							.append("', TO_DATE('")
							.append(sysdate)
							.append("', 'yyyy/mm/dd hh24:mi:ss'), '")
							.append(userId)
							.append("', TO_DATE('")
							.append(sysdate)
							.append("', 'yyyy/mm/dd hh24:mi:ss'), '")
			    sqlExec.addBatch(sql.toString());
			}*/
			//削除完了
						    
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// StringBufferの初期化
		   	sql = null;
			// SQL設定（査定進捗情報登録）
			sql = new StringBuffer().append("UPDATE SST_SATEI_STAT SS SET ")
								.append("SS.bu_cd='")
								.append(select_bu_cd)
								//課題No.106
								//追加開始
								.append("', SS.init_bu_cd='")
								.append(select_bu_cd)
								.append("', SS.bunrui2='")
								.append(select_sei_bu_cd)
								.append("', SS.init_bunrui2='")
								.append(select_sei_bu_cd)
								//追加完了
//障害票No345　2008/05/19　細野　本部コードを設定する
								.append("', SS.honbu_cd='")
								.append(select_sei_bu_cd)
								.append("', SS.upd_user='")
								.append(userId)
								.append("', SS.upd_dt= TO_DATE('")
								.append(sysdate)
								.append("', 'yyyy/mm/dd hh24:mi:ss') ")
								.append("WHERE SS.system_kbn = '")
								.append(system_kbn)
								.append("' AND SS.satei_kaisha_cd = '")
								.append(kaisya)
								.append("' AND SS.ym = '")
								.append(taisyo_ym)
								.append("' AND SS.kikan_tori_cd = '")
								.append(kanjo_cd)
								.append("'");
			sqlExec.addBatch(sql.toString());
		}
//		ここまで（障害票No359　③）		
		return sqlExec.execBatch();
	}
	
	///////////////////////////////////////////////////
	//障害票：523
	//チェックイン日：2008/5/30
	//担当者：SJA中島
	// ログインユーザの査定会社の確定
	/////////////////////////////////////////////////////
	/**
	 * ユーザの所属する査定会社を返します。
	 * @return 査定会社コード
	 * @throws SQLException
	 */
	private ArrayList getUserSateiKaisya() throws SQLException{
		ResultSet rs = null;
	    StringBuffer sql = null;    
	    ArrayList satei_kaisya_cd = new ArrayList();
	    try {
	    	sql = new StringBuffer();
	    	sql.append("SELECT DISTINCT SATEI_KAISHA_CD ");
	    	sql.append("FROM SSM_TM_USER_MST TU,SSM_SATEIKAISYA SK ");
	    	sql.append(" WHERE TU.TOGO_ID='");
	    	sql.append(cmnData.getComUserId());
	    	sql.append("' AND TU.COMPANY_CD = SK.KAISHA_CD ");
	    	sql.append("AND NOT EXISTS (SELECT 1 FROM SSM_USERSANSYOUSOSIKI US WHERE US.TOGO_ID='");
	    	sql.append(cmnData.getComUserId());
	    	sql.append("') ");
	    	sql.append("UNION ");
	    	sql.append("SELECT DISTINCT SATEI_KAISHA_CD ");
	    	sql.append("FROM SSM_USERSANSYOUSOSIKI US ");
	    	sql.append("WHERE US.TOGO_ID='");
	    	sql.append(cmnData.getComUserId());
	    	sql.append("'");    
	    	rs = sqlExec.execQuery(sql.toString());
	    	
	    	while ( rs.next() ) {
	    		satei_kaisya_cd.add(rs.getString("satei_kaisha_cd"));
	    	}
	    	return satei_kaisya_cd;
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