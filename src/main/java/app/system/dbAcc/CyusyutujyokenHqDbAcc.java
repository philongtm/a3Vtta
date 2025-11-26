/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/5/19		SSC				1.5次版機能組込
002		2009/12/17		SSC(坂本)		課題No.205 査定先金額判定条件の変更
003		2009/12/25		SSC				課題No.226 検討条件マスタ登録時のカラム追加
******************************************************************************/
package app.system.dbAcc;

import app.SessionDataZen;
import app.system.form.CyusyutujyokenHqForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
* 抽出条件メンテナンス画面DBアクセスクラス
*/
public class CyusyutujyokenHqDbAcc extends CommonDbAcc {
	
	private final String CLASSNAME = getClass().getName();
	private AppContext appContext = null;		// ＡＰＰコンテキスト

	private SessionDataZen cmnData = null;		// 共通セッションデータ
	private CyusyutujyokenHqForm form = null;	// アクションフォーム	
	
	// INパラメータ
	private String userId;						// ユーザＩＤ（統合ＩＤ）
	//要件No.四-13　ハッシュマップ及び結果セット取得用キー
	//追加開始
	private static final String KAKO_KTK_FLG = "kako_ktk_flg";
	private static final String KAKO_KTK_FROM = "kako_ktk_from";
	private static final String KAKO_KTK_TO = "kako_ktk_to";
	private static final String KAKO_KTK_SANSYO = "kako_ktk_sansyo";
	private static final String GENZAI_KOTEI_SAIKENGAKU_JYOGEN = "genzai_kotei_saikengaku_jyogen";
	private static final String GENZAI_KOTEI_SAIKENGAKU_KAGEN = "genzai_kotei_saikengaku_kagen";
	private static final String KAKO_KOTEI_SAIKENGAKU_JYOGEN = "kako_kotei_saikengaku_jyogen";
	private static final String KAKO_KOTEI_SAIKENGAKU_KAGEN = "kako_kotei_saikengaku_kagen";
	private static final String KOTEI_SAIKENGAKU_SANSYO = "kotei_saikengaku_sansyo";
	private static final String FLGSAKI_FLG = "flgsaki_flg";
	//追加完了
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public CyusyutujyokenHqDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) throws SQLException {
		super(sqlExec, log);

		this.appContext = appContext;
		//ビーン取得
		cmnData = appContext.getCMNZen();
		form = (CyusyutujyokenHqForm)appContext.getActionForm();
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
	 * 検討対象先情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void selectKentouTaisyouList() throws SQLException {		
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs_saiken = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
		PreparedStatement ps_saiken = null;
		
		String langMode = cmnData.getComLangMode();

	    try {
	    	String systemKbn = form.getSystemKbn();
	    	String syoriKbn = "0"+form.getSyoriKbn();
	    	String satei_kaisya_cd = form.getSatei_kaisya_cd();
	    	String mise_cd = form.getMise_cd();
	    	//要件No.四-13　メモリ無駄使い修正及び決算期項目追加対応
	    	//削除開始
			// SQL実行(検討対象先情報取得)
//	    	String sql = "SELECT " +
//		    			"KM.jyouken_no, " +
//		    			"KM.system_kbn, " +
//		    			"KM.jiyuu_cd, " +
//		    			"KM.jiyuu_nm_kj, " +
//		    			"KM.jiyuu_nm_e, " +
//		    			"KM.kakuzuke, " +
//		    			"KM.tuuka, " +
//		    			"KM.kingakujyouken AS kingaku, " +
//		    			"KM.tairyu_from, " +
//		    			"KM.tairyu_to, " +
//		    			"KM.saimutyouka_flg, " +
//		    			"KM.akaji_flg, " +
//		    			"KM.riki_flg, " +
//		    			"KM.kentou_flg, " +
//		    			"KM.tairyu_flg, " +
//		    			"KM.satei_flg, " +
//		    			"KM.mukakuzuke_flg " +
//	    			"FROM SSM_KENTOUJYOUKEN KM " +
//	    			"WHERE " +
//		    			"KM.system_kbn = ? AND " +
//		    			"KM.syori_kbn = ? AND " +
//		    			"KM.SATEIKAISYA_CD = ? AND " +
//		    			"KM.MISE_CD = ? " +
//	    			"ORDER BY jiyuu_cd";
	    	//削除完了
	    	//追加開始
	    	String kessankiKbn = form.getKessanki_kbn();
			// SQL実行(検討対象先情報取得)
	    	StringBuffer sql = new StringBuffer().append("SELECT ")
												.append("KM.jyouken_no, ")
												.append("KM.system_kbn, ")
												.append("KM.jiyuu_cd, ")
												.append("KM.jiyuu_nm_kj, ")
												.append("KM.jiyuu_nm_e, ")
												.append("KM.kakuzuke, ")
												.append("KM.tuuka, ")
												.append("KM.kingakujyouken AS kingaku, ")
												.append("KM.tairyu_from, ")
												.append("KM.tairyu_to, ")
												.append("KM.saimutyouka_flg, ")
												.append("KM.akaji_flg, ")
												.append("KM.riki_flg, ")
												.append("KM.kentou_flg, ")
												.append("KM.tairyu_flg, ")
												.append("KM.satei_flg, ")
												.append("KM.mukakuzuke_flg, ")
												.append("KM.kako_ktk_flg, ")
												.append("KM.kako_ktk_from, ")
												.append("KM.kako_ktk_to, ")
												.append("KM.kako_ktk_sansyo, ")
												.append("KM.genzai_kotei_saikengaku_jyogen, ")
												.append("KM.genzai_kotei_saikengaku_kagen, ")
												.append("KM.kako_kotei_saikengaku_jyogen, ")
												.append("KM.kako_kotei_saikengaku_kagen, ")
												.append("KM.kotei_saikengaku_sansyo, ")
												.append("KM.flgsaki_flg ")
												.append("FROM SSM_KENTOUJYOUKEN KM ")
												.append("WHERE ")
												.append("KM.system_kbn = ? AND ")
												.append("KM.syori_kbn = ? AND ")
												.append("KM.SATEIKAISYA_CD = ? AND ")
												.append("KM.MISE_CD = ? AND ")
												.append("KM.HANKI_SIHANKI_KBN = ? ")
												.append("ORDER BY jiyuu_cd");
	    	//追加完了
	    	
	    	con = sqlExec.getConnection();
	    	ps = con.prepareStatement(sql.toString());
	    	ps.setString(1, systemKbn);			// システム区分
	    	ps.setString(2, syoriKbn);			// 処理区分
	    	ps.setString(3, satei_kaisya_cd);			// 査定会社コード
	    	ps.setString(4, mise_cd);			// 店コード
	    	//要件No.四-13
	    	//追加開始
	    	ps.setString(5, kessankiKbn);			//決算期区分
	    	//追加完了
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	
		    // ActionForm に取得値を格納
		    List kentou_list = new ArrayList();							// 明細配列
			int i = 0;
			while ( rs.next() ) {
				String jiyuuCd = rs.getString("jiyuu_cd");
			    HashMap map = new HashMap();
			    map.put("jyouken_no", rs.getString("jyouken_no"));		// 条件No
			    map.put("system_kbn", rs.getString("system_kbn"));		// システム区分
			    map.put("jiyuu_cd", jiyuuCd);							// 抽出事由コード
			    
			    // 抽出事由名称
			    String jiyuu_nm = "";
			    if ( GS.LANG_JA.equals(langMode) ){
			    	// Jpの場合
			    	jiyuu_nm = rs.getString("jiyuu_nm_kj");
			    } else if ( GS.LANG_EN.equals(langMode) ) {
			    	// Enの場合
			    	jiyuu_nm = rs.getString("jiyuu_nm_e");
			    }
			    if (jiyuu_nm != null) {
			    	jiyuu_nm = jiyuu_nm.trim();
			    }
				map.put("jiyuu_nm", jiyuu_nm);
			    
			    map.put("kakuzuke", rs.getString("kakuzuke"));			// 格付
			    map.put("tuuka", rs.getString("tuuka"));
			    map.put("kingaku", 
			    	Function.format("##,###,###,###,##0.##", rs.getDouble("kingaku")));
			    map.put("tairyu_from", rs.getString("tairyu_from"));
			    map.put("tairyu_to", rs.getString("tairyu_to"));
		    	map.put("saimutyouka_flg", rs.getString("saimutyouka_flg"));
		    	map.put("akaji_flg", rs.getString("akaji_flg"));
		    	map.put("riki_flg", rs.getString("riki_flg"));
			    map.put("kentou_flg", rs.getString("kentou_flg"));
		    	map.put("tairyu_flg", rs.getString("tairyu_flg"));
		    	map.put("satei_flg", rs.getString("satei_flg"));
		    	map.put("mukakuzuke_flg", rs.getString("mukakuzuke_flg"));
		    	//要件No.四-13　障害No.0003対応
		    	//追加開始
		    	map.put(KAKO_KTK_FLG, GS.EMPTY_CHARCTER);
		    	map.put(KAKO_KTK_FROM, GS.EMPTY_CHARCTER);
		    	map.put(KAKO_KTK_TO, GS.EMPTY_CHARCTER);
		    	map.put(KAKO_KTK_SANSYO, GS.EMPTY_CHARCTER);
		    	map.put(GENZAI_KOTEI_SAIKENGAKU_JYOGEN, GS.EMPTY_CHARCTER);
		    	map.put(GENZAI_KOTEI_SAIKENGAKU_KAGEN, GS.EMPTY_CHARCTER);
		    	map.put(KAKO_KOTEI_SAIKENGAKU_JYOGEN, GS.EMPTY_CHARCTER);
		    	map.put(KAKO_KOTEI_SAIKENGAKU_KAGEN, GS.EMPTY_CHARCTER);
		    	map.put(KOTEI_SAIKENGAKU_SANSYO, GS.EMPTY_CHARCTER);
		    	map.put(FLGSAKI_FLG, GS.EMPTY_CHARCTER);
		    	//追加完了
		    	//要件No.四-13　表示用データ取得
		    	//追加開始
		    	if(rs.getString(KAKO_KTK_FLG) != null){
		    		map.put(KAKO_KTK_FLG, Function.trim(rs.getString(KAKO_KTK_FLG)));
		    	}
		    	if(rs.getString(KAKO_KTK_FROM) != null){
		    		map.put(KAKO_KTK_FROM, Function.trim(rs.getString(KAKO_KTK_FROM)));
		    	}
		    	if(rs.getString(KAKO_KTK_TO) != null){
		    		map.put(KAKO_KTK_TO, Function.trim(rs.getString(KAKO_KTK_TO)));
		    	}
		    	if(rs.getString(KAKO_KTK_SANSYO) != null){
		    		map.put(KAKO_KTK_SANSYO, Integer.toString(rs.getInt(KAKO_KTK_SANSYO)));
		    	}
		    	if(rs.getString(GENZAI_KOTEI_SAIKENGAKU_JYOGEN) != null){
		    		map.put(GENZAI_KOTEI_SAIKENGAKU_JYOGEN, Function.format("##,###,###,###,##0.##", rs.getDouble(GENZAI_KOTEI_SAIKENGAKU_JYOGEN)));
		    	}
		    	if(rs.getString(GENZAI_KOTEI_SAIKENGAKU_KAGEN) != null){
		    		map.put(GENZAI_KOTEI_SAIKENGAKU_KAGEN, Function.format("##,###,###,###,##0.##", rs.getDouble(GENZAI_KOTEI_SAIKENGAKU_KAGEN)));
		    	}
		    	if(rs.getString(KAKO_KOTEI_SAIKENGAKU_JYOGEN) != null){
		    		map.put(KAKO_KOTEI_SAIKENGAKU_JYOGEN, Function.format("##,###,###,###,##0.##", rs.getDouble(KAKO_KOTEI_SAIKENGAKU_JYOGEN)));
		    	}
		    	if(rs.getString(KAKO_KOTEI_SAIKENGAKU_KAGEN) != null){
		    		map.put(KAKO_KOTEI_SAIKENGAKU_KAGEN, Function.format("##,###,###,###,##0.##", rs.getDouble(KAKO_KOTEI_SAIKENGAKU_KAGEN)));
		    	}
		    	if(rs.getString(KOTEI_SAIKENGAKU_SANSYO) != null){
		    		map.put(KOTEI_SAIKENGAKU_SANSYO, Integer.toString(rs.getInt(KOTEI_SAIKENGAKU_SANSYO)));
		    	}
		    	if(rs.getString(FLGSAKI_FLG) != null){
		    		map.put(FLGSAKI_FLG, Function.trim(rs.getString(FLGSAKI_FLG)));
		    	}
		    	//追加完了
			    
		    	//要件No.四-13　メモリ無駄使い修正及び決算期項目追加対応
		    	//削除開始
			    // SQL実行(債権フラグ情報取得)
//			    String sql_saiken = "SELECT " +
//							    		"KS.jyouken_no, " +
//							    		"KS.saiken_flg, " +
//							    		"KS.kentou_flg, " +
//							    		"KS.tairyu_flg, " +
//							    		"KS.data_flg " +
//						    		"FROM SSM_KENTOUSAIKEN KS " +
//						    		"WHERE KS.system_kbn = ? AND " +
//						    		"KS.syori_kbn = ? AND " +
//						    		"KS.jiyuu_cd = ? AND " +
//						    		"KS.SATEIKAISYA_CD = ? AND " +
//						    		"KS.MISE_CD = ? AND " +
//						    		"KS.saiken_flg IN('1', '2', '3', '9')";
		    	//削除完了
		    	//追加開始
			    StringBuffer sql_saiken = new StringBuffer().append("SELECT ")
															.append("KS.jyouken_no, ")
															.append("KS.saiken_flg, ")
															.append("KS.kentou_flg, ")
															.append("KS.tairyu_flg, ")
															.append("KS.data_flg ")
															.append("FROM SSM_KENTOUSAIKEN KS ")
															.append("WHERE KS.system_kbn = ? AND ")
															.append("KS.syori_kbn = ? AND ")
															.append("KS.jiyuu_cd = ? AND ")
															.append("KS.SATEIKAISYA_CD = ? AND ")
															.append("KS.MISE_CD = ? AND ")
															.append("KS.HANKI_SIHANKI_KBN = ? AND ")
															.append("KS.saiken_flg IN('1', '2', '3', '9')");
		    	//追加完了
			    
				///////////////////////////////////////
				//障害票：465
				//チェックイン日：2008/5/25
				//対応者：上田
				//概要：ResultSetの循環使用対応
				////////////////////////////////////////
//		    	ps = con.prepareStatement(sql_saiken, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//		    	ps.setString(1, systemKbn);
//		    	ps.setString(2, syoriKbn);
//		    	ps.setString(3, jiyuuCd);
//		    	ps.setString(4, satei_kaisya_cd);
//		    	ps.setString(5, mise_cd);
//		    	rs_saiken = ps.executeQuery();
		    	ps_saiken = con.prepareStatement(sql_saiken.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		    	ps_saiken.setString(1, systemKbn);
		    	ps_saiken.setString(2, syoriKbn);
		    	ps_saiken.setString(3, jiyuuCd);
		    	ps_saiken.setString(4, satei_kaisya_cd);
		    	ps_saiken.setString(5, mise_cd);
		    	//要件No.四-13
		    	//追加開始
		    	ps_saiken.setString(6, kessankiKbn);	//決算期区分
		    	//追加完了
		    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
		    	rs_saiken = sqlExec.execQueryP(ps_saiken);
			    
			    // チェックボックス表示用フラグ配列作成
			    ArrayList dataFlgList = new ArrayList();
			    ArrayList kentouFlgList = new ArrayList();
			    ArrayList tairyuFlgList = new ArrayList();
			    ArrayList saikenFlgList = new ArrayList();
			    ArrayList jyoukenNoList = new ArrayList();
			    
			    // リスト表示用の債権フラグ文字列作成
			    StringBuffer sbData = new StringBuffer();
			    StringBuffer sbKentou = new StringBuffer();
			    StringBuffer sbTairyu = new StringBuffer();
			    
			    while ( rs_saiken.next() ) {
			    	boolean isData = false;
			    	String saiken_flg = rs_saiken.getString("saiken_flg");
			    	if ("1".equals(rs_saiken.getString("data_flg"))) {
			    		sbData.append(saiken_flg).append(" ");
			    		dataFlgList.add(saiken_flg);
			    		isData = true;
			    	}
			    	if ("1".equals(rs_saiken.getString("kentou_flg"))) {
			    		sbKentou.append(saiken_flg).append(" ");
			    		kentouFlgList.add(saiken_flg);
			    		isData = true;
			    	}
			    	if ("1".equals(rs_saiken.getString("tairyu_flg"))) {
			    		sbTairyu.append(saiken_flg).append(" ");
			    		tairyuFlgList.add(saiken_flg);
			    		isData = true;
			    	}
			    	if (isData) {
			    		// 該当レコードの情報を取得
				    	saikenFlgList.add(saiken_flg);							// 債権フラグ
				    	jyoukenNoList.add(rs_saiken.getString("jyouken_no"));	// 条件No
			    	}
			    }
			    map.put("saiken_data_flg", sbData.toString().trim().replace(' ', ','));
			    map.put("saiken_kentou_flg", sbKentou.toString().trim().replace(' ', ','));
			    map.put("saiken_tairyu_flg", sbTairyu.toString().trim().replace(' ', ','));
			    map.put("data_flg_list", dataFlgList);
			    map.put("kentou_flg_list", kentouFlgList);
			    map.put("tairyu_flg_list", tairyuFlgList);
			    map.put("saiken_flg_list", saikenFlgList);
			    map.put("jyouken_flg_list", jyoukenNoList);
			    
			    // 明細配列に取得レコードを格納
			    kentou_list.add(i, map);
			    i++;
			}
			form.setKentouList(kentou_list); 

	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
			if (rs_saiken != null) {
				try {
					rs_saiken.close();
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
			if (ps_saiken != null) {
				try {
					ps_saiken.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
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
	 * 査定対象先情報取得処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void selectSateiTaisyouList() throws SQLException {		
		ResultSet rs = null;
		PreparedStatement ps = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
    	Connection con = null;

	    try {
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// コネクションの取得
//	    	Connection con = sqlExec.getConnection();
	    	con = sqlExec.getConnection();

	    	//要件No.四-13　メモリ無駄使い修正
	    	//削除開始
			// SQL作成(査定対象先情報取得)
//	    	String sql = "SELECT " +
//		    			"SJ.jyouken_no, " +
//		    			"SJ.system_kbn, " +
//		    			"SJ.tairyu_hantei, " +
//						"KB.kbn_hyouji_val AS tairyu_hantei_nm, " +
//		    			"SJ.tuuka, " +
//		    			"SJ.kingakujyouken AS kingaku " +
//	    			"FROM SSM_SATEITAISYOU SJ " +
//	    				"LEFT JOIN SSP_KBN KB ON " +
//						"KB.kbn_key = 'tairyu_jdg' AND " +
//						"KB.kbn_val = SJ.tairyu_hantei AND " +
//						"KB.lang_mode = ? " +
//					"WHERE SJ.system_kbn = ? " +
//					"ORDER BY tairyu_hantei";
	    	//削除完了
	    	//追加開始

	    	StringBuffer sql = new StringBuffer().append("SELECT ")
												.append("SJ.jyouken_no, ")
												.append("SJ.system_kbn, ")
												.append("SJ.tairyu_hantei, ")
												.append("KB.kbn_hyouji_val AS tairyu_hantei_nm, ")
												.append("SJ.tuuka, ")
												.append("SJ.kingakujyouken AS kingaku ")
												.append("FROM SSM_SATEITAISYOU SJ ")
												.append("LEFT JOIN SSP_KBN KB ON ")
												.append("KB.kbn_key = 'tairyu_jdg' AND ")
												.append("KB.kbn_val = SJ.tairyu_hantei AND ")
												.append("KB.system_kbn = SJ.system_kbn AND ")
												.append("KB.lang_mode = ? ")
												.append("WHERE SJ.system_kbn = ? ")
												.append("AND SJ.satei_kaisha_cd = ? ")	//課題No.205　査定会社コード追加
												.append("AND SJ.mise_cd = ? ")			//課題No.205　店コード追加
												.append("ORDER BY tairyu_hantei");
	    	//追加完了
	
			// SQL実行(選択者情報取得)
	    	ps = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ps.setString(1, cmnData.getComLangMode());
	    	ps.setString(2, form.getSystemKbn());
	    	ps.setString(3, form.getSatei_kaisya_cd());		//課題No.205　査定会社コード追加
	    	ps.setString(4, form.getMise_cd());				//課題No.205　店コード追加
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	
		    // ActionForm に取得値を格納
		    List satei_list = new ArrayList();							// 明細配列
			int i = 0;	
			while ( rs.next() ) {
			    HashMap map = new HashMap();
			    map.put("jyouken_no", rs.getString("jyouken_no"));				// 条件No
			    map.put("tairyu_hantei", rs.getString("tairyu_hantei"));		// 滞留判定
			    map.put("tairyu_hantei_nm", rs.getString("tairyu_hantei_nm"));	// 滞留判定
			    map.put("tuuka", rs.getString("tuuka"));						// 通貨
			    map.put("kingaku", 												// 金額
			    	Function.format("##,###,###,###,##0.##", rs.getDouble("kingaku")));			
			    
			    // 明細配列に取得レコードを格納
			    satei_list.add(i, map);
			    i++;
			}
			form.setSateiList(satei_list); 

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
	 * 検討対象先条件新規登録前処理（存在チェック） <br>
	 * 
	 * @return 存在する場合true、存在しない場合false
	 * @exception Exception
	 */
	public boolean checkKentouTaisyou() throws Exception {	
		boolean result = false; 
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;

	    try {
	    	String systemKbn = form.getSystemKbn();
	    	String syoriKbn = "0" + form.getSyoriKbn();
	    	String jiyuuCd = form.getJiyuu_cd();
	    	String satei_kaisya_cd = form.getSatei_kaisya_cd();
	    	String mise_cd = form.getMise_cd();

	    	//要件No.四-13　メモリ無駄使い修正及び決算期項目追加対応
	    	//削除開始
			// SQL実行(kbn_keyに該当する区分情報の取得)
//	    	String sql = "SELECT " +
//	    				"jyouken_no " +
//	    			"FROM SSM_KENTOUJYOUKEN " +
//	    			"WHERE " +
//		    			"system_kbn = ? AND " +
//		    			"syori_kbn = ? AND " +
//		    			"jiyuu_cd = ? AND " + 
//		    			"SATEIKAISYA_CD = ? AND " + 
//		    			"MISE_CD = ? "; 
	    	//削除開始
	    	//追加開始
	    	String kessankiKbn = form.getKessanki_kbn();
	    	StringBuffer sql = new StringBuffer().append("SELECT ")
												.append("jyouken_no ")
												.append("FROM SSM_KENTOUJYOUKEN ")
												.append("WHERE ")
												.append("system_kbn = ? AND ")
												.append("syori_kbn = ? AND ")
												.append("jiyuu_cd = ? AND ") 
												.append("SATEIKAISYA_CD = ? AND ") 
												.append("MISE_CD = ? AND ") 
	    										.append("HANKI_SIHANKI_KBN = ?");
	    	//追加完了
	    	con = sqlExec.getConnection();
	    	ps = con.prepareStatement(sql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ps.setString(1, systemKbn);			// システム区分
	    	ps.setString(2, syoriKbn);			// 処理区分
	    	ps.setString(3, jiyuuCd);			// 事由CD
	    	ps.setString(4, satei_kaisya_cd);	// 査定会社コード
	    	ps.setString(5, mise_cd);			// 店コード
	    	//要件No.四-13
	    	//追加開始
	    	ps.setString(6, kessankiKbn);			// 決算期項目
	    	//追加完了
	    	//障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
		    
		    if (getRsCount(rs) > 0) {
		    	result = true;
		    }
		    
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
	    return result;
	}
	
	/**
	 * 検討対象先条件新規登録処理 <br>
	 * 
	 * @return 登録成功true、登録失敗false
	 * @exception Exception
	 */
	public boolean insertKentouTaisyou() throws Exception {
		boolean result = true;
	    PreparedStatement ps = null;
		ResultSet rs = null;
	    Connection selCon = null;
	    Connection con = null;

	    try {
			// 検索処理用コネクションの取得
	    	selCon = sqlExec.getConnection();
	    	
			// SQL実行(現在の条件No最大値を取得)：SSM_KENTOUJYOUKEN
	    	// No883, 2008/06/20, SJA渡辺, TO_NUMBERを使用することで数値にできる値の最大値を取得するように修正
	    	//要件No.四-13　メモリ無駄使い修正
	    	//削除開始
//	    	String selSql1 = "SELECT " +
//	    				"MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no " +
//	    			"FROM SSM_KENTOUJYOUKEN";
	    	//削除完了
	    	//追加開始
	    	StringBuffer selSql1 = new StringBuffer().append("SELECT ")
													.append("MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no ")
													.append("FROM SSM_KENTOUJYOUKEN");
	    	//追加完了
	    	ps = selCon.prepareStatement(selSql1.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	    	int maxJokenNo = 1;
		    if (getRsCount(rs) > 0) {
			    while(rs.next()) {
			    	maxJokenNo = Integer.parseInt(rs.getString("joken_no")) + 1;
			    }
		    }
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		    
			// SQL実行(現在の条件No最大値を取得)：SSM_KENTOUSAIKEN
	    	// No883, 2008/06/20, SJA渡辺, TO_NUMBERを使用することで数値にできる値の最大値を取得するように修正
	    	//要件No.四-13　メモリ無駄使い修正
	    	//削除開始
//	    	String selSql2 = "SELECT " +
//	    				"MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no " +
//	    			"FROM SSM_KENTOUSAIKEN";
	    	//削除完了
	    	//追加開始
	    	StringBuffer selSql2 = new StringBuffer().append("SELECT ")
													.append("MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no ")
													.append("FROM SSM_KENTOUSAIKEN");
	    	//追加完了
	    	ps = selCon.prepareStatement(selSql2.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	    	int maxJokenNoK = 1;
		    if (getRsCount(rs) > 0) {
			    while(rs.next()) {
			    	maxJokenNoK = Integer.parseInt(rs.getString("joken_no")) + 1;
			    }
		    }
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}

			// 更新日付取得
			String sysdate = sqlExec.getDate().toString();
			// 言語モード取得
			String langMode = cmnData.getComLangMode();
		    
		    String kakuzuke = form.getKakuzuke();	// 格付
		    String mukakutukeFlg = "";				// 無格付フラグ
		    if ("NON".equals(kakuzuke)) {
		    	kakuzuke = "";
		    	mukakutukeFlg = "1";
		    }
		    
		    
		    // 更新処理用コネクションの取得
	    	con = sqlExec.beginTranP();
		    
		    //---------------------------------------------
		    // SQL実行(検討対象先抽出条件マスタレコード登録)
		    //---------------------------------------------
	    	//要件No.四-13　メモリ無駄使い修正及び決算期項目追加対応
	    	//削除開始
//		    StringBuffer sql = new StringBuffer().append("INSERT INTO " +
//		    		"SSM_KENTOUJYOUKEN VALUES " +
//		    		"(TO_CHAR(?, 'FM0000'), " +
//		    		"?, ?, ?, ?, ?, ?, ?, " +
//					"?, ?, ?, ?, ?, " +
//					"?, ?, ?, ?, ?, " +
//					"?, ?, " +
//					"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), " +
//					"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'))");
	    	//削除完了
	    	//追加開始
		    StringBuffer sql = new StringBuffer().append("INSERT INTO ")
												.append("SSM_KENTOUJYOUKEN VALUES ")
												.append("(TO_CHAR(?, 'FM0000'), ")
												.append("?, ?, ?, ?, ?, ?, ?, ")
												.append("?, ?, ?, ?, ?, ")
												.append("?, ?, ?, ?, ?, ")
												.append("?, ?, ")
												.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
												.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
												// 課題No.226
												// 追加開始
		    									//.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		    									.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		    									// 追加完了
	    	//追加完了

		    // PreparedStatementの作成
	    	ps = con.prepareStatement(sql.toString());

	    	// PreparedStatementへ値設定
			int cnt = 0;		// 全21項目
	    	ps.setInt(++cnt, maxJokenNo);							// 条件No
	    	ps.setString(++cnt, form.getSystemKbn());				// システム区分
	    	ps.setString(++cnt, form.getSatei_kaisya_cd());			// 査定会社CD
	    	ps.setString(++cnt, form.getMise_cd());					// 店CD
	    	ps.setString(++cnt, "0"+form.getSyoriKbn());				// 処理区分
	    	ps.setString(++cnt, form.getJiyuu_cd());				// 抽出事由CD
		    if ( GS.LANG_JA.equals(langMode) ){
		    	// Jpの場合
		    	// No612, 2008/06/07, SJA渡辺, 抽出事由名称を登録する前にTRIMするように修正
		    	ps.setString(++cnt, Function.trim(form.getJiyuu_nm()));			// 抽出事由名称
		    	ps.setString(++cnt, "");							// 抽出事由名称（英字）
		    } else if ( GS.LANG_EN.equals(langMode) ) {
		    	// Enの場合
		    	ps.setString(++cnt, "");							// 抽出事由名称
		    	// No612, 2008/06/07, SJA渡辺, 抽出事由名称を登録する前にTRIMするように修正
		    	ps.setString(++cnt, Function.trim(form.getJiyuu_nm()));			// 抽出事由名称（英字）
		    }
	    	ps.setString(++cnt, kakuzuke);							// 格付
	    	ps.setString(++cnt, form.getTuuka_kentou());			// 通貨
	    	String kiganku = Function.removeComma(form.getKingaku_kentou());	// 金額
	    	if (kiganku.length() != 0) {
	    		ps.setDouble(++cnt, Double.parseDouble(kiganku));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.DOUBLE);
	    	}
	    	if (form.getTairyu_from().length() != 0) {				// 滞留期間（FROM）
		    	ps.setInt(++cnt, Integer.parseInt(form.getTairyu_from()));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.INTEGER);
	    	}
	    	if (form.getTairyu_to().length() != 0) {				// 滞留期間（TO）
		    	ps.setInt(++cnt, Integer.parseInt(form.getTairyu_to()));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.INTEGER);
	    	}
	    	ps.setString(++cnt, form.getSaimutyouka_flg());
	    	ps.setString(++cnt, form.getAkaji_flg());
	    	ps.setString(++cnt, form.getRiki_flg());
	    	ps.setString(++cnt, form.getKentou_flg());
	    	ps.setString(++cnt, form.getTairyu_flg());
	    	ps.setString(++cnt, form.getSatei_flg());
	    	ps.setString(++cnt, mukakutukeFlg);
	    	ps.setString(++cnt, userId);
	    	ps.setString(++cnt, sysdate);
	    	ps.setString(++cnt, userId);
	    	ps.setString(++cnt, sysdate);
	    	//要件No.四-13　抽出条件追加
	    	//追加開始
	    	ps.setString(++cnt, Function.trim(form.getKessanki_kbn()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkFlg()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkFrom()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkTo()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkSansyo()));
	    	ps.setString(++cnt, Function.trim(form.getGenzaiKoteiSaikengakuJyogen()));
	    	ps.setString(++cnt, Function.trim(form.getGenzaiKoteiSaikengakuKagen()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKoteiSaikengakuJyogen()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKoteiSaikengakuKagen()));
	    	ps.setString(++cnt, Function.trim(form.getKoteiSaikengakuSansyo()));
	    	ps.setString(++cnt, Function.trim(form.getFlgsakiFlg()));
	    	//追加完了
	    	// 課題No.226
	    	// 追加開始
	    	ps.setString(++cnt, GS.EMPTY_CHARCTER);
	    	ps.setString(++cnt, GS.EMPTY_CHARCTER);
	    	ps.setString(++cnt, GS.EMPTY_CHARCTER);
	    	// 追加完了

	    	// PreparedStatement実行
	    	result = sqlExec.execBatchP(ps, false, true);		// commitなし

		    //--------------------------------------------------
		    // SQL実行(検討対象先債権フラグ条件マスタレコード登録)
		    //--------------------------------------------------
			String[] saikenFlg = {"1", "2", "3", "9"};
			List kentouList = Arrays.asList(form.getSaiken_kentou_flg());	// 登録する検討対象フラグ情報
			List tairyuList = Arrays.asList(form.getSaiken_tairyu_flg());	// 登録する滞留判定フラグ情報
			List dataList = Arrays.asList(form.getSaiken_data_flg());		// 登録するデータ作成フラグ情報
			
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
	    	ps = con.prepareStatement(INS_SSM_KENTOUSAIKEN.toString());
			for (int i=0; i < saikenFlg.length; i++) {
				// フラグ情報の取得
			    List flgInfo = createSaikenFlgInfo(saikenFlg[i], kentouList, tairyuList, dataList);
			    
			    // 登録判定
			    if (flgInfo.contains("1")) {
			    	ps.setInt(1, maxJokenNoK++);
			    	ps.setString(2, form.getSystemKbn());
			    	
			    	//Bug272 Komori Michio 2008/05/13 Start 
			    	//原因：テーブルのカラム順とプリペアードのセット順があっていなかった
			    	//ps.setString(3, "0"+form.getSyoriKbn());
			    	//ps.setString(4, form.getSatei_kaisya_cd());
			    	//ps.setString(5, form.getMise_cd());
			    	ps.setString(3, form.getSatei_kaisya_cd());
			    	ps.setString(4, form.getMise_cd());	
			    	ps.setString(5, "0"+form.getSyoriKbn());
			    	//Bug272 Komori Michio 2008/05/13 End			    	
			    	ps.setString(6, form.getJiyuu_cd());
					ps.setString(7, saikenFlg[i]);				// 債権フラグ
					ps.setString(8, flgInfo.get(0).toString());	// 検討対象フラグ
					ps.setString(9, flgInfo.get(1).toString());	// 滞留判定フラグ
					ps.setString(10, flgInfo.get(2).toString());	// データ作成フラグ
			    	ps.setString(11, userId);
			    	ps.setString(12, sysdate);
			    	ps.setString(13, userId);
			    	ps.setString(14, sysdate);
			    	//要件No.四-13
			    	//追加開始
			    	ps.setString(15, Function.trim(form.getKessanki_kbn()));	//決算期区分
					//追加完了
			    	ps.addBatch();
			    }
			}
			result = sqlExec.execBatchPB(ps, true, result);		// commitあり
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// No465, 2008/05/26, SJA渡辺, コネクションは閉じないように修正
			/*if (selCon != null) {
				try {
					selCon.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}*/
	    }
		
		return result;
	}
	
	/**
	 * 検討対象先条件更新登録処理 <br>
	 * 
	 * @return 更新成功true、更新失敗false
	 * @exception Exception
	 */
	public boolean updateKentouTaisyou() throws Exception {
		boolean result = false;
	    PreparedStatement ps = null;		// 検討対象先抽出条件マスタ更新用PreparedStatement
	    PreparedStatement psUpd = null;		// 検討対象先債権フラグ条件マスタ更新用PreparedStatement
	    PreparedStatement psDel = null;		// 検討対象先債権フラグ条件マスタ削除用PreparedStatement
	    PreparedStatement psIns = null;		// 検討対象先債権フラグ条件マスタ登録用PreparedStatement
	    Connection con = null;
	    ResultSet rs = null;

	    try {
			// コネクションの取得
	    	con = sqlExec.getConnection();
	    	
			// SQL実行(現在の条件No最大値を取得)：SSM_KENTOUSAIKEN
	    	// No883, 2008/06/20, SJA渡辺, TO_NUMBERを使用することで数値にできる値の最大値を取得するように修正
	    	//要件No.四-13　メモリ無駄使い修正
	    	//削除開始
//	    	String selSql = "SELECT " +
//	    				"MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no " +
//	    			"FROM SSM_KENTOUSAIKEN";
	    	//削除完了
	    	//追加開始
	    	StringBuffer selSql = new StringBuffer().append("SELECT ")
													.append("MAX(TO_NUMBER(JYOUKEN_NO)) as joken_no ")
													.append("FROM SSM_KENTOUSAIKEN");
	    	//追加完了
	    	ps = con.prepareStatement(selSql.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	    	int maxJokenNo = 1;
		    if (getRsCount(rs) > 0) {
			    while(rs.next()) {
			    	maxJokenNo = Integer.parseInt(rs.getString("joken_no")) + 1;
			    }
		    }
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
	    	
			// 更新日付取得
			String sysdate = sqlExec.getDate().toString();
		    // 言語モード取得
			String langMode = cmnData.getComLangMode();
	
			// 更新情報の取得
		    String no = form.getNo_kentou();					// 条件No(検討対象先抽出条件マスタ)
		    String[] no_arr = form.getNo_kentou_flg();			// 条件No(検討対象先債権フラグ条件マスタ)
		    String kakuzuke = form.getKakuzuke();		// 格付
		    String mukakutukeFlg = "";					// 無格付フラグ
		    if ("NON".equals(kakuzuke)) {
		    	kakuzuke = "";
		    	mukakutukeFlg = "1";
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
		    // コネクションの取得
		    con = sqlExec.beginTranP();
		    
		    //---------------------------------------------
		    // SQL実行(検討対象先抽出条件マスタレコード更新)
		    //---------------------------------------------
		    // No459, 2008/05/23, SJA渡辺, 言語モードにより名称を登録する際、異なる言語モードの名称に空文字設定しないように修正
		    StringBuffer sql = new StringBuffer("UPDATE SSM_KENTOUJYOUKEN SET ");
		    							if (GS.LANG_JA.equals(langMode)) {
		    								sql.append("jiyuu_nm_kj = ?, ");
		    							} else {
		    								sql.append("jiyuu_nm_e = ?, ");
		    							}
		    							sql.append("kakuzuke = ?, ")
										.append("tuuka = ?, ")
										.append("kingakujyouken = ?, ")
										.append("tairyu_from = ?, ")
										.append("tairyu_to = ?, ")
										.append("saimutyouka_flg = ?, ")
										.append("akaji_flg = ?, ")
										.append("riki_flg = ?, ")
										.append("kentou_flg = ?, ")
										.append("tairyu_flg = ?, ")
										.append("satei_flg = ?, ")
										.append("mukakuzuke_flg = ?, ")
										.append("upd_user = ?, ")
										.append("upd_dt = TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
										//要件No.四-13	抽出条件追加
										//追加開始
										.append("kako_ktk_flg = ?, ")
										.append("kako_ktk_from = ?, ")
										.append("kako_ktk_to = ?, ")
										.append("kako_ktk_sansyo = ?, ")
										.append("genzai_kotei_saikengaku_jyogen = ?, ")
										.append("genzai_kotei_saikengaku_kagen = ?, ")
										.append("kako_kotei_saikengaku_jyogen = ?, ")
										.append("kako_kotei_saikengaku_kagen = ?, ")
										.append("kotei_saikengaku_sansyo = ?, ")
										.append("flgsaki_flg = ? ")
										//追加完了
										.append("WHERE jyouken_no = ?");
	    	// PreparedStatementの作成
	    	ps = con.prepareStatement(sql.toString());
	    	
	    	// PreparedStatementへ値設定
			int cnt = 0;
		    if ( GS.LANG_JA.equals(langMode) ){
		    	// Jpの場合
		    	// No612, 2008/06/07, SJA渡辺, 抽出事由名称を登録する前にTRIMするように修正
		    	ps.setString(++cnt, Function.trim(form.getJiyuu_nm()));			// 抽出事由名称
		    } else if ( GS.LANG_EN.equals(langMode) ) {
		    	// Enの場合
		    	// No612, 2008/06/07, SJA渡辺, 抽出事由名称を登録する前にTRIMするように修正
		    	ps.setString(++cnt, Function.trim(form.getJiyuu_nm()));			// 抽出事由名称（英字）
		    }
	    	ps.setString(++cnt, kakuzuke);							// 格付
	    	ps.setString(++cnt, form.getTuuka_kentou());			// 通貨
	    	String kiganku = Function.removeComma(form.getKingaku_kentou());	// 金額
	    	if (kiganku.length() != 0) {
	    		ps.setDouble(++cnt, Double.parseDouble(kiganku));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.DOUBLE);
	    	}
	    	if (form.getTairyu_from().length() != 0) {				// 滞留期間（FROM）
		    	ps.setInt(++cnt, Integer.parseInt(form.getTairyu_from()));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.INTEGER);
	    	}
	    	if (form.getTairyu_to().length() != 0) {				// 滞留期間（TO）
		    	ps.setInt(++cnt, Integer.parseInt(form.getTairyu_to()));
	    	} else {
	    		ps.setNull(++cnt, java.sql.Types.INTEGER);
	    	}
	    	ps.setString(++cnt, form.getSaimutyouka_flg());
	    	ps.setString(++cnt, form.getAkaji_flg());
	    	ps.setString(++cnt, form.getRiki_flg());
	    	ps.setString(++cnt, form.getKentou_flg());
	    	ps.setString(++cnt, form.getTairyu_flg());
	    	ps.setString(++cnt, form.getSatei_flg());
	    	ps.setString(++cnt, mukakutukeFlg);
	    	ps.setString(++cnt, userId);
	    	ps.setString(++cnt, sysdate);
			//要件No.四-13　抽出条件追加
			//追加開始
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkFlg()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkFrom()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkTo()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKtkSansyo()));
	    	ps.setString(++cnt, Function.trim(form.getGenzaiKoteiSaikengakuJyogen()));
	    	ps.setString(++cnt, Function.trim(form.getGenzaiKoteiSaikengakuKagen()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKoteiSaikengakuJyogen()));
	    	ps.setString(++cnt, Function.trim(form.getKakoKoteiSaikengakuKagen()));
	    	ps.setString(++cnt, Function.trim(form.getKoteiSaikengakuSansyo()));
	    	ps.setString(++cnt, Function.trim(form.getFlgsakiFlg()));
			//追加完了
	    	ps.setString(++cnt, no);
	    	
	    	// PreparedStatement実行
	    	result = sqlExec.execBatchP(ps, false, true);		// commitなし

		    //--------------------------------------------------
		    // SQL実行(検討対象先債権フラグ条件マスタレコード更新)
		    //--------------------------------------------------
		    if (result) {
				String[] saikenFlg = {"1", "2", "3", "9"};
				List saikenList = Arrays.asList(form.getSaiken_all_flg());		// 更新前の債権フラグ情報
				List kentouList = Arrays.asList(form.getSaiken_kentou_flg());	// 更新する検討対象フラグ情報
				List tairyuList = Arrays.asList(form.getSaiken_tairyu_flg());	// 更新する滞留判定フラグ情報
				List dataList = Arrays.asList(form.getSaiken_data_flg());		// 更新するデータ作成フラグ情報
				// PreparedStatementの作成
		    	psUpd = con.prepareStatement(UPD_SSM_KENTOUSAIKEN);
		    	psDel = con.prepareStatement(DEL_SSM_KENTOUSAIKEN);
		    	psIns = con.prepareStatement(INS_SSM_KENTOUSAIKEN.toString());
		    	
				int dataCnt = 0;	// 更新または削除したデータ件数
				for (int i=0; i < saikenFlg.length; i++) {
					// フラグ情報の取得
				    List flgInfo = createSaikenFlgInfo(saikenFlg[i], kentouList, tairyuList, dataList);
					if (saikenList.contains(saikenFlg[i])) {
					    
					    if (flgInfo.contains("1")) {
					    	// 更新処理
					    	psUpd.setString(1, flgInfo.get(0).toString());		// 検討対象フラグ
					    	psUpd.setString(2, flgInfo.get(1).toString());		// 滞留判定フラグ
					    	psUpd.setString(3, flgInfo.get(2).toString());		// データ作成フラグ
					    	psUpd.setString(4, userId);
					    	psUpd.setString(5, sysdate);
					    	psUpd.setString(6, no_arr[dataCnt]);				// 更新対象の条件No
					    	psUpd.addBatch();
					    } else {
					    	// 削除処理
					    	psDel.setString(1, no_arr[dataCnt]);				// 削除対象の条件No
					    	psDel.addBatch();
					    }
					    dataCnt++;
					} else {
					    if (flgInfo.contains("1")) {
							// 登録処理
					    	psIns.setInt(1, maxJokenNo++);
					    	psIns.setString(2, form.getSystemKbn());
					    	psIns.setString(3, form.getSatei_kaisya_cd());
					    	psIns.setString(4, form.getMise_cd());
					    	psIns.setString(5, "0"+form.getSyoriKbn());
					    	psIns.setString(6, form.getJiyuu_cd());
					    	psIns.setString(7, saikenFlg[i]);					// 債権フラグ
					    	psIns.setString(8, flgInfo.get(0).toString());		// 検討対象フラグ
					    	psIns.setString(9, flgInfo.get(1).toString());		// 滞留判定フラグ
					    	psIns.setString(10, flgInfo.get(2).toString());		// データ作成フラグ
					    	psIns.setString(11, userId);
					    	psIns.setString(12, sysdate);
					    	psIns.setString(13, userId);
					    	psIns.setString(14, sysdate);
					    	//要件No.四-13
					    	//追加開始
					    	ps.setString(15, Function.trim(form.getKessanki_kbn()));	//決算期区分
							//追加完了
					    	psIns.addBatch();
						}
					}
				}
				// PreparedStatement実行
				result = sqlExec.execBatchPB(psUpd, false, result);			// commitなし
				result = sqlExec.execBatchPB(psDel, false, result);			// commitなし
				result = sqlExec.execBatchPB(psIns, true, result);			// commitあり
		    }
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (psUpd != null) {
				try {
					psUpd.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (psDel != null) {
				try {
					psDel.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (psIns != null) {
				try {
					psIns.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
		
		return result;
	}
	
	/**
	 * 検討対象先条件削除処理 <br>
	 * 
	 * @return 削除成功true、削除失敗false
	 * @exception Exception
	 */
	public boolean deleteKentouTaisyou() throws Exception {
		boolean result = false;
	    PreparedStatement ps = null;
	    Connection con = null;
		
	    try {		
	    	// 削除情報の取得
		    String no = form.getNo_kentou();			// 条件No(検討対象先抽出条件マスタ)
		    String[] no_arr = form.getNo_kentou_flg();	// 条件No(検討対象先債権フラグ条件マスタ)

		    // コネクションの取得
		    con = sqlExec.beginTranP();

		    //---------------------------------------------
		    // SQL実行(検討対象先抽出条件マスタレコード削除)
		    //---------------------------------------------
	    	ps = con.prepareStatement("DELETE SSM_KENTOUJYOUKEN WHERE jyouken_no = ?");
	    	ps.setString(1, no);						// 条件No
	    	result = sqlExec.execBatchP(ps, false, true);	// commitなし
		
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		    //--------------------------------------------------
		    // SQL実行(検討対象先債権フラグ条件マスタレコード削除)
		    //--------------------------------------------------
	    	ps = con.prepareStatement(DEL_SSM_KENTOUSAIKEN);
			for (int i=0; i < no_arr.length; i++) {
				if (no_arr[i] != null) {
					ps.setString(1, no_arr[i]);			// 条件No
					ps.addBatch();
				}
			}
	    	result = sqlExec.execBatchPB(ps, true, result);		// commitあり
	    } finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
		
		return result;
	}
	
	/**
	 * 査定対象先条件新規登録前処理（存在チェック） <br>
	 * 
	 * @return 存在する場合true、存在しない場合false
	 * @exception Exception
	 */
	public boolean checkSateiTaisyou() throws Exception {	
		boolean result = false; 
		ResultSet rs = null;
		PreparedStatement ps = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
    	Connection con = null;

	    try {
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			// コネクションの取得
//	    	Connection con = sqlExec.getConnection();
	    	con = sqlExec.getConnection();
	    	
			// SQL実行(kbn_keyに該当する区分情報の取得)
	    	String sql = "SELECT " +
	    				"jyouken_no " +
	    			"FROM SSM_SATEITAISYOU " +
	    			"WHERE system_kbn = ? AND tairyu_hantei = ?" +
	    			"AND satei_kaisha_cd = ? AND mise_cd = ?";		//査定会社コード、店コード追加
	    	
	    	ps = con.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ps.setString(1, form.getSystemKbn());			// システム区分
	    	ps.setString(2, form.getTairyu_hantei());		// 滞留判定
	    	ps.setString(3, form.getSatei_kaisya_cd());		// 課題No.205 査定会社コード判定
	    	ps.setString(4, form.getMise_cd());				// 課題No.205 店コード判定
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
		    
		    if (getRsCount(rs) > 0) {
		    	result = true;
		    }
		    
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
	    return result;
	}
	
	/**
	 * 査定対象先条件新規登録処理 <br>
	 * 
	 * @return 登録成功true、登録失敗false
	 * @exception Exception
	 */
	public boolean insertSateiTaisyou() throws Exception {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		ResultSet rs = null;
		
		try {
			// SQL実行(現在の条件No最大値を取得)：SSM_KENTOUSAIKEN
	    	// No883, 2008/06/20, SJA渡辺, TO_NUMBERを使用することで数値にできる値の最大値を取得するように修正
	    	String selSql = "SELECT " +
	    				"NVL(MAX(TO_NUMBER(JYOUKEN_NO)),'0') as joken_no " +
	    			"FROM SSM_SATEITAISYOU";

		    con = sqlExec.getConnection();
	    	ps = con.prepareStatement(selSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	    	int maxJokenNo = 1;
		    if (getRsCount(rs) > 0) {
			    while(rs.next()) {
			    	maxJokenNo = Integer.parseInt(Function.trim(rs.getString("joken_no"))) + 1;
			    }
		    }
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
		    
			// 更新日付取得
			String sysdate = sqlExec.getDate().toString();
	
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
			// コネクションの取得
		    con = sqlExec.beginTranP();
		    
		    /**************************************/
	    	/**課題No.205 査定先金額判定条件変更  */
	    	/**チェックイン日：2009/12/17         */
	    	/**対応者：SSC(坂本)                  */
	    	/**************************************/
		    
		    // SQL実行(査定対象先条件マスタレコード登録)
		    String sql = "INSERT INTO " +
			    		"SSM_SATEITAISYOU VALUES (" +
			    		"TO_CHAR(?, 'FM0000'), " +
			    		"?, ?, ?, ?, ?, ?, " +		//課題対応No.205　追加カラム"?"２個追加
			    		"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), " +
			    		"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'))";
		    
		    ps = con.prepareStatement(sql);
		    
		    ps.setInt(1, maxJokenNo);
		    ps.setString(2, form.getSystemKbn());
		    ps.setString(3, form.getTairyu_hantei());
		    ps.setString(4, form.getTuuka_satei());
	    	String kiganku = Function.removeComma(form.getKingaku_satei());	// 金額
	    	if (kiganku.length() != 0) {
	    		ps.setDouble(5, Double.parseDouble(kiganku));
	    	} else {
	    		ps.setNull(5, java.sql.Types.DOUBLE);
	    	}
	    	
	    	ps.setString(6,form.getSatei_kaisya_cd());				//課題対応No.205 査定会社コード追加
	    	ps.setString(7,form.getMise_cd());						//課題対応No.205 店コード追加
		    ps.setString(8, userId);
		    ps.setString(9, sysdate);
		    ps.setString(10, userId);
		    ps.setString(11, sysdate);
		    
	    	result = sqlExec.execBatchP(ps, true, true);			// commitあり
	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
		
		return result;
	}
	
	/**
	 * 査定対象先条件更新登録処理 <br>
	 * 
	 * @return 更新成功true、更新失敗false
	 * @exception Exception
	 */
	public boolean updateSateiTaisyou() throws Exception {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		
		try {
			// 更新日付取得
			String sysdate = sqlExec.getDate().toString();
	
			// 更新情報の取得
		    String no = form.getNo_satei();				// 条件No
		    String tuuka = form.getTuuka_satei();		// 通貨
	
		    // コネクションの取得
		    con = sqlExec.beginTranP();
		    
		    /**************************************/
	    	/**課題No.205 査定先金額判定条件変更  */
	    	/**チェックイン日：2009/12/17         */
	    	/**対応者：SSC(坂本)                  */
	    	/**************************************/
		    
		    // SQL実行(査定対象先条件マスタレコード更新)
		    String sql = "UPDATE " +
		    			"SSM_SATEITAISYOU SET " +
			    			"tuuka = ?, " +
			    			"kingakujyouken = ?, " +
			    			"satei_kaisha_cd = ?, " +	//課題対応No.205　査定会社コード追加　
			    			"mise_cd = ?, " +			//課題対応No.205　店コード追加
			    			"upd_user = ?, " +
			    			"upd_dt = TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss') " +
		    			"WHERE jyouken_no = ?";
	    	ps = con.prepareStatement(sql);
	    	ps.setString(1, tuuka);
	    	String kiganku = Function.removeComma(form.getKingaku_satei());	// 金額
	    	if (kiganku.length() != 0) {
	    		ps.setDouble(2, Double.parseDouble(kiganku));
	    	} else {
	    		ps.setNull(2, java.sql.Types.DOUBLE);
	    	}
	    	ps.setString(3,form.getSatei_kaisya_cd());	//課題対応No.205　査定会社コード追加
	    	ps.setString(4,form.getMise_cd());			//課題対応No.205　店コード追加
	    	ps.setString(5, userId);
	    	ps.setString(6, sysdate);
	    	ps.setString(7, no);
	    	result = sqlExec.execBatchP(ps, true, true);			// commitあり
	    } finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
		
		return result;
	}
	
	/**
	 * 査定対象先条件削除処理 <br>
	 * 
	 * @return 削除成功true、削除失敗false
	 * @exception Exception
	 */
	public boolean deleteSateiTaisyou() throws Exception {
		boolean result = false;
		PreparedStatement ps = null;
		Connection con = null;
		
		try {
		    // コネクションの取得
		    con = sqlExec.beginTranP();
		    
		    // SQL実行(査定対象先条件マスタレコード削除)
	    	String sql = "DELETE SSM_SATEITAISYOU WHERE jyouken_no = ?";
		    ps = con.prepareStatement(sql);
	    	ps.setString(1, form.getNo_satei());
	    	result = sqlExec.execBatchP(ps, true, true);			// commitあり
		} finally {
			if (ps != null) {
				try {
					ps.close();
				} catch (Exception e) {
					throw new Exception(e.getMessage());
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
		
		return result;
	}
	
	/**
	 * 区分情報取得処理（区分マスタ） <br>
	 * 
	 * @param kbn_key 取得する区分キー
	 * @param def_key デフォルト値のキー（nullの場合、デフォルト値を設定しない）
	 * @return 区分値と区分表示値を格納したマップ
	 * @exception SQLException
	 */
	public LinkedHashMap selectKbnMap(String kbn_key, String def_key) throws SQLException {		
		ResultSet rs = null;
		PreparedStatement ps = null;
		///////////////////////////////////////
		//障害票：465
		//チェックイン日：2008/5/25
		//対応者：上田
		//概要：ResultSetの循環使用対応
		////////////////////////////////////////
    	Connection con = null;

	    try {
			///////////////////////////////////////
			//障害票：465
			//チェックイン日：2008/5/25
			//対応者：上田
			//概要：ResultSetの循環使用対応
			////////////////////////////////////////
		    // コネクションの取得
//	    	Connection con = sqlExec.getConnection();
	    	con = sqlExec.getConnection();

	    	//要件No.四-13　メモリの無駄使い修正　
	    	//削除開始
			// SQL実行(kbn_keyに該当する区分情報の取得)
// 	    	String sql = "SELECT " +
//  		    				"lang_mode, " +
// 		    				"kbn_val, " +
//		    				"kbn_hyouji_val " +
//		    			"FROM SSP_KBN " +
//		    			"WHERE " +
//			    			"TRIM(kbn_key) = ? AND " +
//			    			"lang_mode = ? " +
//			    			"order by kbn_order asc";
	    	//削除完了
	    	//追加開始
	    	StringBuffer sql = new StringBuffer().append("SELECT ")
												.append("lang_mode, ")
												.append("kbn_val, ")
												.append("kbn_hyouji_val ")
												.append("FROM SSP_KBN ")
												.append("WHERE ")
												.append("TRIM(kbn_key) = ? AND ")
												.append("lang_mode = ? AND ")
												.append("system_kbn = ? ")
												.append("order by kbn_order asc");
	    	//追加完了
	    	//要件No.四-13　決算期項目取得の場合はdesc　
	    	//追加開始
	    	String reSql;
	    	if(kbn_key.equals("kessanki")){
	    		reSql = Function.StrReplace(sql.toString(),"asc","desc");
	    	}else{
	    		reSql = sql.toString();
	    	}
	    	//追加完了
		    ps = con.prepareStatement(reSql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	    	ps.setString(1, kbn_key);
	    	ps.setString(2, cmnData.getComLangMode());
	    	ps.setString(3, "01");
	    	// 障害管理：200808250005 2008/8/25 中島 文字化け対策済みResultSetを取得するように修正
	    	rs = sqlExec.execQueryP(ps);
	    	//要件No.四-13　決算期項目の場合は+1しない
	    	//修正・追加開始
	    	//LinkedHashMap ar_system_kbn = new LinkedHashMap(1 + getRsCount(rs));
	    	LinkedHashMap ar_system_kbn;
	    	if(kbn_key.equals("kessanki")){
	    		ar_system_kbn = new LinkedHashMap();
	    	}else{
	    		ar_system_kbn = new LinkedHashMap(1 + getRsCount(rs));
	    	}
	    	//修正・追加完了
	    	if (def_key != null) {
		    	ar_system_kbn.put("", def_key);
		    }
		    while (rs.next()) {
				ar_system_kbn.put(rs.getString("kbn_hyouji_val"), rs.getString("kbn_val").trim());
		    }
		    
		    return ar_system_kbn;

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
	 * 格付セレクトボックス値設定
	 */
	public void setKakutukeKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("ktk",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap ktkKbn = new LinkedHashMap(getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    ktkKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    ktkKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			ktkKbn.put(appContext.getMsg("label.mukakutsuke"),"NON");
			
			form.setKakutukeList(ktkKbn);
	
			// 初めの値をブランクに設定
			form.setKakuzuke("");
			
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
	 * 査定会社コードセレクトボックス値設定
	 */
	public void setSateiKaisya() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("satei_kaisya_cd",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap ktkKbn = new LinkedHashMap(getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    ktkKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    ktkKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
//			ktkKbn.put(appContext.getMsg("label.mukakutsuke"),"NON");
			
			form.setAr_satei_kaisya_cd(ktkKbn);
	
			// 初めの値をブランクに設定
			form.setSatei_kaisya_cd("");
			
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
	 * 店コードセレクトボックス値設定
	 */
	public void setMiseCd(String satei_kaisya_cd) throws SQLException {
		
		ResultSet rs = null;
		
		// No462, 2008/05/31, SJA渡辺, 区分テーブルからSJもPNもNICを取得するように修正
		StringBuffer sql = new StringBuffer().append("SELECT kbn_hyouji_val")
											.append(" FROM SSP_KBN")
											.append(" WHERE kbn_key='mise_cd'")
											.append(" and lang_mode='")
											.append(cmnData.getComLangMode())
											.append("' and kbn_val='")
											.append(satei_kaisya_cd)
											.append("' and system_kbn = '01'");
		/*if ("PN".equals(satei_kaisya_cd)) {
			satei_kaisya_cd = "SJ";
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("KIKAN_KAISHA_CD ");
		sql.append("FROM SSM_SATEIKAISYA ");
		sql.append("WHERE SATEI_KAISHA_CD ='");
		sql.append(satei_kaisya_cd);
		sql.append("'");*/
		
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap ktkKbn = new LinkedHashMap(this.getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    ktkKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // TODO 勘定科目に表示する項目の作成(暫定)
			    String kbn_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    ktkKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
//			ktkKbn.put(appContext.getMsg("label.mukakutsuke"),"NON");
			
			form.setAr_mise_cd(ktkKbn);
	
			// 初めの値をブランクに設定
			form.setMise_cd("");
			
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
	 * 検討対象先債権フラグ条件マスタ更新用の情報作成
	 * 
	 * @param 債権フラグ情報
	 * @param 入力された検討対象フラグのリスト
	 * @param 入力された滞留判定フラグのリスト
	 * @param 入力されたデータ作成フラグのリスト
	 * @return フラグ更新用の情報
	 * @exception Exception
	 */
	private List createSaikenFlgInfo(String saikenFlg, List kentouList, List tairyuList, List dataList) throws Exception {
		List flgInfo = new ArrayList();
	    // 検討対象フラグ
	    if (kentouList.contains(saikenFlg)) {
	    	flgInfo.add("1");
	    } else {
	    	flgInfo.add("");
	    }
	    // 滞留判定フラグ
	    if (tairyuList.contains(saikenFlg)) {
	    	flgInfo.add("1");
	    } else {
	    	flgInfo.add("");
	    }
	    // データ作成フラグ
	    if (dataList.contains(saikenFlg)) {
	    	flgInfo.add("1");
	    } else {
	    	flgInfo.add("");
	    }
	    
	    return flgInfo;
	}

	// 検討対象先債権フラグ条件マスタレコード削除SQL
	private static final String DEL_SSM_KENTOUSAIKEN = "DELETE SSM_KENTOUSAIKEN WHERE jyouken_no = ?";
	
	//要件No.四-13　メモリ無駄使い修正及び決算期項目追加対応
	//削除開始
	// 検討対象先債権フラグ条件マスタレコード登録SQL
//	private static final String INS_SSM_KENTOUSAIKEN = "INSERT INTO SSM_KENTOUSAIKEN VALUES (" +
//					"TO_CHAR(?, 'FM0000'), " +
//					"?, ?, ?, ?, ?, ?, ?, ?, ?, " +
//					"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), " +
//					"?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'))";
	//削除完了
	//追加開始
	private static final StringBuffer INS_SSM_KENTOUSAIKEN = new StringBuffer().append("INSERT INTO SSM_KENTOUSAIKEN VALUES (")
																				.append("TO_CHAR(?, 'FM0000'), ")
																				.append("?, ?, ?, ?, ?, ?, ?, ?, ?, ")
																				.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
																				.append("?, TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss'), ")
																				.append("?)");
	//追加完了
	// 検討対象先債権フラグ条件マスタレコード更新SQL
	private static final String UPD_SSM_KENTOUSAIKEN = "UPDATE SSM_KENTOUSAIKEN SET " +
					"kentou_flg = ?, " +
					"tairyu_flg = ?, " +
					"data_flg = ?, " +
					"upd_user = ?, " +
					"upd_dt = TO_DATE(?, 'yyyy/mm/dd hh24:mi:ss') " +
					"WHERE jyouken_no = ?";
	
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
											.append("' and KB.system_kbn = '01' ")
											.append("ORDER BY KB.kbn_order ASC");
		
		return sql.toString();
	}
}