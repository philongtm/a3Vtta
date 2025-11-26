/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2009/05/26      SSC				1.5次版組込
******************************************************************************/
package app.syokaiZen.dbAcc;

import app.SessionDataZen;
import app.syokaiZen.form.SateiForm;
import common.AppContext;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
* 滞留判定対象先一覧画面DBアクセスクラス
*/
public class SateiDbAcc extends CommonDbAcc {
	
	private final static String HANKI = "1";
   /** クラス名 */
	private final String CLASSNAME = getClass().getName();
	/** ＡＰＰコンテキスト */
	private AppContext appContext = null;

	/** 共通セッションデータ */
	private SessionDataZen cmnData = null;
	/** アクションフォーム */
	private SateiForm form = null;	
	
	// INパラメータ
	/** ユーザID(統合ID） */
	private String userId;
	/** 対象年月 */
	private String ym;
	/** ソート順 */
	private int sort;
	
	/** 検索モード判定フラグ */
	private int searchMode;
	/** 対象年月リスト作成用基準年月 */
	private String baseDate;
	/** 対象年月リスト作成用基準年月 */
	private String objectDate;
	/** 勘定先CD */
	private String kanjoCd;
	/** システム区分 */
	private String systemSection;
	//要件No.四-10
	//追加開始
	/** 半期四半期区分 */
	private String hanki_sihanki_kbn;
	//追加完了
	/** DUNS No */
	private String dnusNo;
	/** 勘定先名称 */
	private String kanjoName;
	/** 所属国 */
	private String country;
	/** 取引先区分 */
	private String dealSection;
	/** 債権区分 */
	private String claimSection;
	/** 抽出事由 */
	private String caseSelection;
	/** 査定会社コード */
	private String judgeCorp;
	/** 部門コード */
	private String bumonCode;
	/** 部コード */
	private String buCode;
	/** 対象年月リスト */
	private List dateList;
	/** 検索用ユーザ権限内査定会社リスト */
	private List sateiCorpList;
	
	/** 最大検索結果数 */
   private static final int maxCount = 1000;
	
	/**
	 * コンストラクタ
	 * 
	 * @param sqlExec
	 *            sqlExec を設定。
	 * @param appLog
	 *            appLog を設定。
	 */
	public SateiDbAcc(SqlExecuter sqlExec, Log log, AppContext appContext) {
		super(sqlExec, log);

		this.appContext = appContext;
		
		//ビーン取得
		cmnData = appContext.getCMNZenRe();
		form = (SateiForm)appContext.getActionForm();

		//ビーンの値を変数に設定
		userId = cmnData.getComUserId();
		ym = form.getYm();
		
		//フォーム値を取得（入力値をそのままSQLに使用するものは簡易エスケープ処理）
		sort = form.getSort();
		searchMode = form.getSearchMode();	
       baseDate = Function.StrReplace(form.getBaseDate(),"'","''");
       objectDate = Function.StrReplace(form.getObjectDate(),"'","''");
       kanjoCd = Function.StrReplace(form.getKanjoCd(),"'","''");
       systemSection = form.getSystemSection();
       dnusNo = Function.StrReplace(form.getDunsNo(),"'","''");
       kanjoName = Function.StrReplace(form.getKanjoName(),"'","''");
       country = form.getCountry();
       dealSection = form.getDealSection();
       claimSection = form.getClaimSection();
       caseSelection = form.getCaseSelection();
       judgeCorp = form.getJudgeCorp();
       bumonCode = form.getBumonCode();
		buCode = form.getBuCode();
		dateList = form.getDateList();
		sateiCorpList = form.getSateiCorp();
		//要件No.四-10
		//追加開始
		hanki_sihanki_kbn = form.getHanki_sihanki_kbn();
		//追加完了

	}
	
	/**
	 * 変数初期化
	 */
	public void initialize() {
	    // INパラメータ
	    userId = null;
	    ym = null;
	    sort = 0;
	}
	

	/**
	 * 対象年月取得SQL実行処理 <br>
	 * 
	 * @exception SQLException
	 */
	public void executeYm() throws SQLException {
		
		ResultSet rs = null;
		
	    // SQL作成
	    String sql = "SELECT DISTINCT ym FROM SST_TAIRYU_STAT ORDER BY ym DESC";

	    try {
	    	// SQL実行		
	    	rs = sqlExec.execQuery(sql);
	    	
	    	// ActionForm に取得値を格納
	    	LinkedHashMap ar_ym = new LinkedHashMap(getRsCount(rs));
	    	while ( rs.next() ) {
	    		ar_ym.put(Function.insertYmSlash(rs.getString("ym")),rs.getString("ym"));
	    	}
	    	
	    	// 対象年月のデフォ値をActionForm に設定
	    	rs.beforeFirst();
	    	rs.next();		
	    	form.setYm(rs.getString("ym"));
	    	this.ym = form.getYm();
	    	
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
	 * 区分情報取得処理（区分マスタ） <br>
	 * 
	 * @param kbn_key 取得する区分キー
	 * @param def_key デフォルト値のキー（nullの場合、デフォルト値を設定しない）
	 * @return 区分値と区分表示値を格納したマップ
	 * @exception SQLException
	 */
	public LinkedHashMap selectKbnMap(String kbn_key, String def_key) throws SQLException {		
		ResultSet rs = null;
		
		String langMode = cmnData.getComLangMode();

	    try {
			// SQL実行(kbn_keyに該当する区分情報の取得)
	    	StringBuffer sql = new StringBuffer("SELECT " +
	    				"lang_mode, " +
	    				"kbn_val, " +
	    				"kbn_hyouji_val " +
	    			"FROM SSP_KBN " +
	    			"WHERE kbn_key = '")
					.append(kbn_key)
					.append("' AND lang_mode = '")
					.append(langMode)
					.append("' and system_kbn ='01'");
		    rs = sqlExec.execQuery(sql.toString());
		    
		    LinkedHashMap ar_system_kbn = new LinkedHashMap(1 + getRsCount(rs));
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
		}
	}

	/**
	 * 区分テーブルから対応する表示区分名を取得する。
	 * @param kbn_key
	 * @param def_key
	 * @return
	 * @throws SQLException
	 */
	public String getHyoujiVal(String kbn_key, String def_key) throws SQLException {		
		ResultSet rs = null;
		
		String langMode = cmnData.getComLangMode();
		String hyouji_val = null;
	    try {
			// SQL実行(kbn_keyに該当する区分情報の取得)
	    	StringBuffer sql = new StringBuffer("SELECT " +
	    				"lang_mode, " +
	    				"kbn_val, " +
	    				"kbn_hyouji_val " +
	    			"FROM SSP_KBN " +
	    			"WHERE kbn_key = '")
					.append(kbn_key)
					.append("' and system_kbn ='01'")
					.append(" AND lang_mode = '")
					.append(langMode)
					.append("' AND KBN_VAL='")
					.append(def_key)
					.append("'");
		    rs = sqlExec.execQuery(sql.toString());
		    
		    while (rs.next()) {
		    	hyouji_val = rs.getString("kbn_hyouji_val");
		    }
		    
		    return hyouji_val;

	    } finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * プルダウンメニュー用表示情報取得SQL実行処理
	 */
	public void executeSelectParam(AppContext appContext) throws SQLException {
		
		ResultSet rs = null;

		StringBuffer sql;
		String userId = cmnData.getComUserId();	
		List sateiCorpList = new ArrayList();
		
		// プルダウンメニュー用選択条件情報（査定会社名）取得SQL実行
		// ユーザー権限マスタからユーザーIDで検索を行い、権限内の査定会社名を取得する。
		sql= new StringBuffer()
					.append("SELECT ")
					.append("COUNT(satei_kaisha_cd) AS cnt ")
					.append("FROM ")
					.append("SSM_USERSANSYOUSOSIKI ")
					.append("WHERE ")
					.append("SATEI_KAISHA_CD IN ('SJ','PN')")
					.append(" AND togo_id='")
					.append(userId)
					.append("'");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			rs.next();
			String cnt = rs.getString("cnt");
			sqlExecAssessmentCom(userId, sateiCorpList, cnt);
			sateiCorpList = form.getSateiCorp();
			
			sqlExecJiyuuForPullDown();	
			
			sqlExecCountryForPullDown();
			
			// 障害票：814　2008/6/11　SJA中島　部門名は空欄にする。
			form.setBumon(new LinkedHashMap());
			
			// 部名プルダウンメニューは初期表示時は空欄のみ
			form.setBu(new LinkedHashMap());
			
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
	
	// No465, 2008/05/24, SJA渡辺, SQLのカーソルがたまる問題処理修正のため、SQLのメソッド化
	// 障害票：814　2008/6/11　SJA中島　査定会社選択時に、選択した査定会社に紐付く部門一覧を取得する
	/**
	 * プルダウンメニュー用選択条件情報（部門名）取得SQL実行
	 * @throws SQLException
	 */
	public void sqlExecBumonForPullDown(String sateiCorp) throws SQLException {
		ResultSet rs = null;
		StringBuffer sql;
		// 統合組織マスタテーブルに登録されている全部門名の取得。英文時もそのまま
		sql= new StringBuffer().append("SELECT DISTINCT bumon_nm, bumon_cd FROM SSM_TOGO_SOSHIKI WHERE");
		sql.append(" satei_kaisha_cd = '");
		sql.append(sateiCorp);
		sql.append("'");

		if("Y".equals(cmnData.getComEigyoKaiRecogKg()) ||
			"Y".equals(cmnData.getComEigyoKaiRegistKg()) ||
			"Y".equals(cmnData.getComEigyoRecogKg()) ||
			"Y".equals(cmnData.getComEigyoRegistKg())){
			// 営業担当、経理担当の場合には、担当部門コードで絞り込む。
			StringBuffer tanto_buf = new StringBuffer();
			// 検索条件にあわせ、ソート順リストの作成
			ArrayList list = cmnData.getComTantoCd();
			Iterator itr = cmnData.getComTantoCd().iterator();
			sql.append(" AND bumon_cd in (");
			if(!GS.EMPTY_CHARCTER.equals(Function.trim((String)cmnData.getComTantoCd().get(0)))){
				while(itr.hasNext()){
					tanto_buf.append("'");
					tanto_buf.append(itr.next());
					tanto_buf.append("',");
				}
			}else if(!GS.EMPTY_CHARCTER.equals(Function.trim(cmnData.getComBumonCd()))){
				tanto_buf.append("'");
				tanto_buf.append(cmnData.getComBumonCd());
				tanto_buf.append("',");
			}else{
				tanto_buf.append("' ',");
			}

			sql.append(tanto_buf.toString().substring(0,tanto_buf.toString().length()-1));
			sql.append(")");
		}

		try {
			rs = sqlExec.execQuery(sql.toString());
			LinkedHashMap bumon = new LinkedHashMap(getRsCount(rs));
			while (rs.next() ) {
				bumon.put(rs.getString("bumon_nm"), rs.getString("bumon_cd"));
			}
			form.setBumon(bumon);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	// No465, 2008/05/24, SJA渡辺, SQLのカーソルがたまる問題処理修正のため、SQLのメソッド化
	/**
	 * プルダウンメニュー用選択条件情報（国名）取得SQL実行
	 * @throws SQLException
	 */
	private void sqlExecCountryForPullDown() throws SQLException {
		ResultSet rs = null;
		StringBuffer sql;
		// D&B国テーブルに登録されている国名・ＩＳＯ国略称（２桁）を取得する
		sql= new StringBuffer().append("select DISTINCT WB_COUNTRY_NM, ISO_COUNTRY_NM_RYA2,WB_COUNTRY_CD from TM_DB_COUNTRY_MST@VIR_SJLMA ORDER BY WB_COUNTRY_NM ASC");
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			LinkedHashMap countryMap = new LinkedHashMap(getRsCount(rs));	
			while (rs.next() ) {
				countryMap.put(rs.getString("WB_COUNTRY_NM"), rs.getString("WB_COUNTRY_CD") + rs.getString("ISO_COUNTRY_NM_RYA2"));
			}
			form.setCountryMap(countryMap);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	// No465, 2008/05/24, SJA渡辺, SQLのカーソルがたまる問題処理修正のため、SQLのメソッド化
	/**
	 * プルダウンメニュー用抽出事由条件情報取得SQL実行
	 * @throws SQLException
	 */
	private void sqlExecJiyuuForPullDown() throws SQLException {
		ResultSet rs = null;
		StringBuffer sql;
		// 検討対象先抽出条件マスタに登録されている全抽出事由名称を取得、言語に合わせフォームに格納する
		if (cmnData.getComLangMode().equals("Ja")){
			//sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_kj FROM SSM_KENTOUJYOUKEN ORDER BY jiyuu_cd ASC");
			// No612, 2008/06/09, SJA渡辺, 検討対象先にある抽出事由コードと抽出事由コードが50のものをプルダウンの選択値にするように修正
			sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_kj FROM SSM_KENTOUJYOUKEN WHERE jiyuu_cd IN ((SELECT distinct jiyuu_cd FROM SST_KENTOUTAISYO)) OR jiyuu_cd = '50' ORDER BY jiyuu_cd ASC");
			// 最新のDB用検索SQL
			//sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_kj FROM SSM_KENTOUJYOUKEN WHERE SATEIKAISYA_CD ='").append(cmnData.getComSateiKaishaCd()).append("' ORDER BY jiyuu_cd ASC");
		}else{
			//sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_e FROM SSM_KENTOUJYOUKEN ORDER BY jiyuu_cd ASC");
			// No612, 2008/06/09, SJA渡辺, 検討対象先にある抽出事由コードと抽出事由コードが50のものをプルダウンの選択値にするように修正
			sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_e FROM SSM_KENTOUJYOUKEN WHERE jiyuu_cd IN ((SELECT distinct jiyuu_cd FROM SST_KENTOUTAISYO)) OR jiyuu_cd = '50' ORDER BY jiyuu_cd ASC");
			// 最新のDB用検索SQL
			//sql= new StringBuffer().append("SELECT DISTINCT jiyuu_cd, jiyuu_nm_e FROM SSM_KENTOUJYOUKEN WHERE SATEIKAISYA_CD ='").append(cmnData.getComSateiKaishaCd()).append("' ORDER BY jiyuu_cd ASC");
		}
		try {
			rs = sqlExec.execQuery(sql.toString());
			LinkedHashMap jiyuu = new LinkedHashMap(getRsCount(rs));
			while ( rs.next() ) {
				// No459, 2008/05/23, SJA渡辺, 名称がない場合、選択値に追加しないように修正
				if (cmnData.getComLangMode().equals("Ja")) {
					if (rs.getString("jiyuu_nm_kj") != null) {
						jiyuu.put(rs.getString("jiyuu_nm_kj"), rs.getString("jiyuu_cd"));
						//jiyuu.put(rs.getString("jiyuu_nm_kj"), rs.getString("jiyuu_nm_kj"));
					}
				} else {
					if (rs.getString("jiyuu_nm_e") != null) {
						jiyuu.put(rs.getString("jiyuu_nm_e"), rs.getString("jiyuu_cd"));
						//jiyuu.put(rs.getString("jiyuu_nm_e"), rs.getString("jiyuu_nm_e"));
					}
				}
			}
			form.setJiyuu(jiyuu);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
		
	}

	// No465, 2008/05/24, SJA渡辺, SQLのカーソルがたまる問題処理修正のため、SQLのメソッド化
	/**
	 * 査定会社取得SQL実行
	 * @throws SQLException
	 */
	private void sqlExecAssessmentCom(String userId, List sateiCorpList, String cnt) throws SQLException {
		ResultSet rs = null;
		StringBuffer sql;
		if("0".equals(cnt)){
			sql= new StringBuffer()
						.append("SELECT DISTINCT ")
						.append("satei_kaisha_cd ")
						.append("FROM ")
						.append("SSM_SATEIKAISYA ")
						.append("WHERE ")
						.append("kaisha_cd = '")
						.append(cmnData.getComKaishaCd())
						.append("'");
		}else{
			// No814, 2008/06/13, SJA遠藤, 査定会社マスタに存在する査定会社のみを取得するよう修正 
			sql= new StringBuffer()
						.append("SELECT DISTINCT ")
						.append("SS.satei_kaisha_cd ")
						.append("FROM ")
						.append("SSM_SATEIKAISYA SS ")
						.append("INNER JOIN ")
						.append("SSM_USERSANSYOUSOSIKI SU ")
						.append("ON ")
						.append("SS.SATEI_KAISHA_CD = SU.SATEI_KAISHA_CD ")
						.append("WHERE ")
						.append("SU.SATEI_KAISHA_CD IN ('SJ','PN')")
						.append(" AND togo_id='")
						.append(userId)
						.append("' ");
		}
		try {
			rs = sqlExec.execQuery(sql.toString());
			Set sateiCorp = new HashSet(getRsCount(rs));
			// ハッシュセットに査定会社名を入れ同名を排除、そのごリストに格納する
			while (rs.next()) {
				sateiCorp.add(rs.getString("satei_kaisha_cd"));
			}
			Iterator sateiCorpIterator = sateiCorp.iterator();
			while (sateiCorpIterator.hasNext()) {
				sateiCorpList.add(sateiCorpIterator.next());
			}
			
			form.setSateiCorp(sateiCorpList);
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * 部門名選択時の部名絞込みSQL実行<BR>
	 * 
	 * @param appContext
	 * @throws SQLException
	 */
	public void executeBu(AppContext appContext) throws SQLException {
		
		ResultSet rs = null;
		
		form = (SateiForm)appContext.getActionForm();
		
		String sql = "SELECT DISTINCT bu_nm, bu_cd FROM SSM_TOGO_SOSHIKI WHERE bumon_cd ='" + form.getBumonCode() + "'";
		
		try {
			rs = sqlExec.execQuery(sql);
			
			LinkedHashMap bu = new LinkedHashMap(getRsCount(rs));
			while ( rs.next() ) {
				bu.put(rs.getString("bu_nm"), rs.getString("bu_cd"));
			}
			form.setBu(bu);
			
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
	 *  滞留判定検索選択時・検索SQL実行
	 */
	public void executeTairyu() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer();
		int x=0;

		// あいまい検索条件時の検索条件指定用バッファ						/* No.837 */
		StringBuffer oLikeSql = new StringBuffer();							/* No.837 */

	    //取得した検索条件から検索SQLリスト作成
	    List sqlList = new ArrayList();
	    if (!kanjoCd.equals("")) 
	    	sqlList.add(" trim(T.kikan_tori_cd) like '" + kanjoCd + "%' ");
	    if (!systemSection.equals(""))
	    	sqlList.add(" T.system_kbn='" + systemSection + "' ");
	    if (!dnusNo.equals("")) 
	    	sqlList.add(" TT.togo_tori_cd='" + dnusNo + "' ");
	    if (!kanjoName.equals("")){
	    	/*
			sqlList.add(" (MT.business_nm_kj like '%" + kanjoName + "%' OR MT.business_nm like '%" + kanjoName + "%') "); No.837 */
			oLikeSql.append(" and (MT.business_nm_kj like '%" + kanjoName + "%' OR MT.business_nm like '%" + kanjoName + "%') ");/* No.837 */
			sqlList.add(" (MT.business_nm_kj is not null OR MT.business_nm is not null) ");
	    }
	    if (!country.equals("")){
	    	sqlList.add(" MT.wb_country_cd ='" + country.substring(0,3) + "' ");
	    	sqlList.add(" MT.iso_country_nm_rya2 ='" + country.substring(3) + "' ");
	    	oLikeSql.append(" and MT.wb_country_cd ='" + country.substring(0,3) + "' ");/* No.837 */
	    	oLikeSql.append(" and MT.iso_country_nm_rya2 ='" + country.substring(3) + "' ");/* No.837 */
	    }


	    if (!bumonCode.equals("")){
	    	sqlList.add(" T.honbu_cd ='" + bumonCode + "' ");
	    }else{
	    	sqlList.add(" T.honbu_cd IN (" + form.getSansyoBunrui2() + ") ");
	    	// 部門が選択されていないため、自分の担当部門のみ参照可とする。
			// 担当部門のみ選択可能とする。
	    	//if("Y".equals(cmnData.getComEigyoKaiRecogKg()) ||
	    	//	"Y".equals(cmnData.getComEigyoKaiRegistKg())||
	    	//	"Y".equals(cmnData.getComEigyoRecogKg()) ||
	    	//	"Y".equals(cmnData.getComEigyoRegistKg())){
	    	//	StringBuffer tanto_buf = new StringBuffer();
	    	//	tanto_buf.append(" T.honbu_cd in (");
	    	//	Iterator itr = cmnData.getComTantoCd().iterator();
	    	//	if(!GS.EMPTY_CHARCTER.equals(Function.trim((String)cmnData.getComTantoCd().get(0)))){
	    	//		while(itr.hasNext()){
	    	//			tanto_buf.append("'");
	    	//			tanto_buf.append(itr.next());
	    	//			tanto_buf.append("'");
	    	//			if(itr.hasNext()){
	    	//				tanto_buf.append(",");
	    	//			}
	    	//		}
	    	//	}else if(!GS.EMPTY_CHARCTER.equals(Function.trim(cmnData.getComBumonCd()))){
	    	//		tanto_buf.append("'");
	    	//		tanto_buf.append(cmnData.getComBumonCd());
	    	//		tanto_buf.append("'");
	    	//	}else{
	    	//		tanto_buf.append("' '");
	    	//	}
	    	//	tanto_buf.append(")");
	    	//	sqlList.add(tanto_buf.toString());
	    	//}
	    }
	    if (!buCode.equals(""))
	    	sqlList.add(" T.bu_cd ='" + buCode + "' ");
	    if (!judgeCorp.equals(""))
	    	sqlList.add(" T.satei_kaisha_cd='" + judgeCorp + "' ");
		// No.837, yyyy/MM/dd, SJA渡辺, 照会->査定結果照会画面の性能向上施策							/* No.837 */
		final StringBuffer cmnPart = new StringBuffer()
			.append(" SELECT DISTINCT ST.TAISHOGAI_FLG,T.system_kbn, T.status, ")
			.append(" T.phase, T.kikan_tori_cd,T.ym, T.satei_kaisha_cd, T.honbu_cd, T.bu_cd, T.ka_cd,T.mise_cd,MT.business_nm_kj,  MT.business_nm,M.kingaku,")
			.append(" TS1.bumon_nm, TS2.bu_nm, TS3.ka_nm, T.ka_cd, TT.togo_tori_cd, T.anken_no, P.phase_nm, S.status_nm, ")
			.append(" '(' || TRIM(tuuka_cd) || ')' AS tuuka_cd ")
			.append(" FROM SST_TAIRYU_STAT T")
			.append(" LEFT JOIN SST_SATEI_STAT ST ON")
			.append(" ST.ANKEN_NO = T.SATEI_ANKEN_NO ")
			.append(" LEFT JOIN (SELECT SATEI_KAISHA_CD, BUMON_CD, ")			/* No.837 */
			.append(" MAX(BUMON_NM) as bumon_nm FROM SSM_TOGO_SOSHIKI ")		/* No.837 */
			.append(" GROUP BY SATEI_KAISHA_CD, BUMON_CD ) TS1 on ")			/* No.837 */
			.append(" TS1.SATEI_KAISHA_CD = T.SATEI_KAISHA_CD AND")				/* No.837 */
			.append(" TS1.bumon_cd = T.honbu_cd ")								/* No.837 */
			.append(" LEFT JOIN (SELECT SATEI_KAISHA_CD, BU_CD, ")				/* No.837 */
			.append(" MAX(BU_NM) as bu_nm FROM SSM_TOGO_SOSHIKI ")				/* No.837 */
			.append(" GROUP BY SATEI_KAISHA_CD, BU_CD ) TS2 on ")				/* No.837 */
			.append(" TS2.SATEI_KAISHA_CD = T.SATEI_KAISHA_CD ")				/* No.837 */
			.append(" AND TS2.bu_cd = T.bu_cd ")								/* No.837 */
			.append(" LEFT JOIN (SELECT SATEI_KAISHA_CD, KA_CD, ")				/* No.837 */
			.append(" MAX(KA_NM) as ka_nm FROM SSM_TOGO_SOSHIKI ")				/* No.837 */
			.append(" GROUP BY SATEI_KAISHA_CD, KA_CD ) TS3 on ")				/* No.837 */
			.append(" TS3.SATEI_KAISHA_CD = T.SATEI_KAISHA_CD AND ")			/* No.837 */
			.append(" TS3.ka_cd = T.ka_cd ")									/* No.837 */
			/* No.837 性能改善施策によりコメントアウト
			.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS1 ON")
			.append(" TS1.satei_kaisha_cd = T.satei_kaisha_cd AND TS1.bumon_cd = T.honbu_cd")
			.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS2 ON")
			.append(" TS2.satei_kaisha_cd = T.satei_kaisha_cd AND TS2.bumon_cd = T.honbu_cd AND TS2.bu_cd = T.bu_cd")
			.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS3 ON")
			.append(" TS3.satei_kaisha_cd = T.satei_kaisha_cd AND TS3.bumon_cd = T.honbu_cd AND TS3.ka_cd = T.ka_cd")
			 No.837  */
			.append(" LEFT JOIN SSE_TAIHI TT ON")
			.append(" TT.kikan_tori_cd = trim(T.kikan_tori_cd)|| '00' AND TT.system_kbn = T.system_kbn AND TT.office_cd = T.mise_cd AND TT.ym = T.ym")
			.append(" LEFT JOIN SSE_TOGO_MST MT ON")
			.append(" MT.togo_tori_cd = TT.togo_tori_cd AND MT.sikibetu_cd = TT.sikibetu_cd AND MT.ym = TT.ym AND MT.syori_kaisu = TT.syori_kaisu ")
			.append(oLikeSql)													/* No.837 */
			//.append(" LEFT JOIN ( SELECT anken_no, SUM(kingaku) AS kingaku, hantei_flg, TRIM(tuuka_cd) AS tuuka_cd FROM SST_TAIRYUHANTEIMEISAI GROUP BY tuuka_cd, anken_no ) M ON")
			.append(" LEFT JOIN ( SELECT anken_no, SUM(kingaku) AS kingaku, hantei_flg,")/* No.837 */
			.append(" TRIM(tuuka_cd) AS tuuka_cd FROM SST_TAIRYUHANTEIMEISAI")	/* No.837 */
			.append(" GROUP BY tuuka_cd, hantei_flg, anken_no ) M ON")			/* No.837 */
			.append(" T.anken_no = M.anken_no")
			.append(" LEFT JOIN (SELECT kbn_key, kbn_val, kbn_hyouji_val AS phase_nm, LANG_MODE FROM SSP_KBN WHERE LANG_MODE='")
			.append(cmnData.getComLangMode())
			.append("' AND system_kbn = '01'")
			.append(" AND kbn_key='phase') P ON")
			.append(" P.kbn_val = T.phase")
			.append(" LEFT JOIN (SELECT kbn_key, kbn_val, kbn_hyouji_val AS status_nm, LANG_MODE FROM SSP_KBN WHERE LANG_MODE='")
			.append(cmnData.getComLangMode())
			.append("' AND system_kbn = '01'")
			.append(" AND kbn_key='status') S ON")
			.append(" S.kbn_val = T.status ")
			.append(" WHERE M.hantei_flg = '1'");

		/* No.837 性能向上施策の一環として、検索条件の指定方法を見直す。
		 絞込み条件として対象年月が指定されていた場合でも、検索用年月リストを
		作成・設定していたが、対象年月が指定されていた場合には検索用年月リストは
		チェック・指定しないよう修正する。
		//検索用年月リスト入っている年月をSQLに追記する
		//リスト内が空の場合、検索せずに終了する。
	    if (dateList.size() != 0) {
	    	cmnPart.append(" AND T.ym = ANY (");
	    	for (x=0; x<dateList.size(); x++) {
	    		cmnPart.append("'" + dateList.get(x) + "'");
	    		if (x<dateList.size()-1)
	    			cmnPart.append(", ");
	    	}
	    	cmnPart.append(") AND");
	    } else {
	    	return;
	    }
	    
		*/
	    // 障害票：　チェックイン日：2008/6/13　SJA中島　対象年月が入っていた場合、対象年月で絞り込む
	    if(!GS.EMPTY_CHARCTER.equals(Function.trim(objectDate))){
	    	cmnPart.append(" AND T.ym = '");								/* No.837 */
	    	cmnPart.append(objectDate);
	    	cmnPart.append("' AND ");										/* No.837 */
		} else {															/* No.837 */
			// 絞込条件として対象年月が指定されていない場合には、			/* No.837 */
			// 対象年月リストにて定義されている年月を条件として設定する。	/* No.837 */
			int iDataListSize = dateList.size();							/* No.837 */
			if (iDataListSize <= 0) {										/* No.837 */
				dateList = getSyoriYm(dateList,baseDate);
			}																/* No.837 */
			String tmpCreateConditionStr = createConditionStr("T.ym",dateList,true,true,true);
			if(GS.EMPTY_CHARCTER.equals(Function.trim(tmpCreateConditionStr))){
				cmnPart.append(" AND T.ym = '' AND ");
			}else{
				cmnPart.append(tmpCreateConditionStr);
			}
	    }
	    
	    // 検索査定会社の指定がない場合、ログインユーザーが参照可能な組織内のデータを検索する。
	    if (judgeCorp.equals("")) {
		/* No.837 性能改善施策によりコメントアウト
	        cmnPart.append(" T.satei_kaisha_cd = ANY(");
	        for (x=0; x<sateiCorpList.size(); x++) {
	            cmnPart.append("'" + sateiCorpList.get(x) + "'");
	            if (x<sateiCorpList.size()-1)
	                cmnPart.append(", ");
	        }
	        cmnPart.append(") ");
		*/
			cmnPart.append(createConditionStr("T.satei_kaisha_cd", 			/* No.837 */
									sateiCorpList, true, false, false));	/* No.837 */
	        if (sqlList.size()>0) 
	            cmnPart.append("AND");
	    }
	    
	    //検索SQLリストに入っている文章を追記する
	    for (x=0; x<sqlList.size(); x++) {
	    	cmnPart.append(sqlList.get(x));
	    	if (x<sqlList.size()-1) 
	    		cmnPart.append("AND");
	    }
			
		sql.append(cmnPart);
	    
	    // SQL - ORDER BY句作成
	    if(sort == 0) {
	        sql.append(" ORDER BY T.kikan_tori_cd ASC");
	    } else if(sort == 1) {
	    	// No417, 2008/05/21, SJA渡辺, 英語名称昇順でソートするように修正
	        sql.append(" ORDER BY MT.business_nm ASC");
	    } else if(sort == 2) {
	        sql.append(" ORDER BY T.ym ASC");
	    } else if(sort == 3) {
	        sql.append(" ORDER BY bumon_nm ASC");
	    } else if(sort == 4) {
	        sql.append(" ORDER BY M.kingaku ASC");
	    } else if(sort == 5) {
	        sql.append(" ORDER BY T.phase ASC, T.status ASC");
	    } 
       // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
	    //log.write(GS.LOG_INF,CLASSNAME, "実行SQL：" + sql);
	    
	    try {
	    	// SQL実行		
	    	rs = sqlExec.execQuery(sql.toString());
	    	// ActionForm 明細件数に取得レコード数を格納
	    	form.setCnt_meisai(getRsCount(rs));
	    	
	    	
	    	//検索結果が1000件を超えた場合、検索結果をクリアし処理を中断する
	    	if (form.getCnt_meisai()>maxCount) {
	    		rs.close();
	    		return;
	    	}
	    	
	    	// ActionForm に取得値を格納
	    	List ar_meisai = new ArrayList();	// 明細配列	
	    	int i = 0;
	    	int j = 0;
	    	while ( rs.next() ) {
	    		HashMap map = new HashMap();
	    		if("2".equals(rs.getString("TAISHOGAI_FLG"))){
	    			continue;
	    		}
	    		map.put("id", Function.getStringOfInt(i));
	    		if (cmnData.getComLangMode().equals("Ja")) { 
					// 障害対応：200807250001　2008/7/25 中島　企業名にNULLと出力されるのを抑止する
	    			if(rs.getString("business_nm_kj") == null){
	    				map.put("business_nm", Function.trim(rs.getString("business_nm")) + " ");					// 企業名(英語)
	    			}else{
	    				map.put("business_nm", Function.trim(rs.getString("business_nm_kj")) + " ");				// 企業名(日本語）
	    			}
	    		}else{
	    			map.put("business_nm", Function.trim(rs.getString("business_nm")) + " ");					// 企業名(英語)
	    		}
	    		
	    		map.put("y_m", Function.insertYmSlash(rs.getString("ym")));						// 『/』区切り年月
	    		map.put("kingaku", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("kingaku"))) + rs.getString("tuuka_cd"));				// 金額 (引当金判定表示用)
	    		map.put("shinchoku", rs.getString("phase_nm") + " " +rs.getString("status_nm"));		// 進捗状況(日本語)
	    		
	    		
   			///////////////////////////////////////
   			//障害票：321
   			//チェックイン日：2008/5/14
   			//対応者：SJA中島
   			//概要：結果照会画面で出力する組織を「部門コード　部名　課名（課コード）」に変更
   			///////////////////////////////////////
	    		if(rs.getString("bu_nm")!=null && rs.getString("ka_nm")!=null){
		    		map.put("soshiki", rs.getString("honbu_cd") + " " + rs.getString("bu_nm") + " " +
		    				rs.getString("ka_nm") + " (" + rs.getString("ka_cd") + ")");			// 詳細画面表示用組織名	    			
	    		}else if(rs.getString("bu_nm")!=null && rs.getString("ka_nm")==null){
		    		map.put("soshiki", rs.getString("honbu_cd") + " " + rs.getString("bu_nm"));    			
	    		}else if(rs.getString("bu_nm")==null && rs.getString("ka_nm")!=null){
	    			map.put("soshiki", rs.getString("honbu_cd") + " " + rs.getString("ka_nm") + " (" + rs.getString("ka_cd") + ")");
	    		}else{
	    			map.put("soshiki", rs.getString("honbu_cd"));
	    		}

		    	///////////////////////////////////////
		    	//障害票：618
		    	//チェックイン日：2008/6/8
		    	//対応者：SJA小森
		    	//概要：部門コードと部コードが同じ場合は一緒なので一つのみ表示
		    	////////////////////////////////////////
	    		/*
	    		if(rs.getString("bu_nm")!=null && rs.getString("ka_nm")!=null){
		    		map.put("soshiki_syosai", rs.getString("bumon_nm") + " " + rs.getString("bu_nm") + " (" + rs.getString("bu_cd") + ") " +
		    				rs.getString("ka_nm") + " (" + rs.getString("ka_cd") + ")");			// 詳細画面表示用組織名	    			
	    		}else if(rs.getString("bu_nm")!=null && rs.getString("ka_nm")==null){
		    		map.put("soshiki_syosai", rs.getString("bumon_nm") + " " + rs.getString("bu_nm") + " (" + rs.getString("bu_cd") + ") ");    			
	    		}else if(rs.getString("bu_nm")==null && rs.getString("ka_nm")!=null){
	    			map.put("soshiki_syosai", rs.getString("bumon_nm") + " " + rs.getString("ka_nm") + " (" + rs.getString("ka_cd") + ")");
	    		}else{
	    			map.put("soshiki_syosai", rs.getString("bumon_nm"));
	    		}
	    		*/
	    		String bumon_nm = rs.getString("bumon_nm");
	    		String bu_nm = rs.getString("bu_nm");
	    		String bu_cd = rs.getString("bu_cd");
	    		String ka_nm = rs.getString("ka_nm");
	    		String ka_cd = rs.getString("ka_cd");
	    		if(bu_nm != null && ka_nm != null){
	    			if(!bu_nm.equals(bumon_nm)){
	    				map.put("soshiki_syosai", bumon_nm + " " + bu_nm + " (" + bu_cd + ") " 
	    						+ ka_nm + " (" + ka_cd + ")");// 詳細画面表示用組織名
	    			}
	    			else{
	    				map.put("soshiki_syosai", bumon_nm + " " + " (" + bu_cd + ") " 
	    						+ ka_nm + " (" + ka_cd + ")");// 詳細画面表示用組織名	    				
	    			}
	    		}else if(bu_nm != null && ka_nm == null){
	    			if(!bu_nm.equals(bumon_nm)){
	    				map.put("soshiki_syosai", bumon_nm + " " + bu_nm + " (" + bu_cd + ") ");
	    			}
	    			else{
	    				map.put("soshiki_syosai", bumon_nm + " "  + " (" + bu_cd + ") ");	    				
	    			}
	    		}else if(bu_nm == null && ka_nm !=null){
	    			map.put("soshiki_syosai", bumon_nm + " " + ka_nm + " (" + ka_cd + ")");
	    		}else{
	    			map.put("soshiki_syosai", bumon_nm);
	    		}
				// 障害票No618　2008/06/08 Komori End
	    		map.put("phase", rs.getString("phase"));
	    		map.put("honbu", rs.getString("honbu_cd"));
	    		map.put("ym", rs.getString("ym"));
	    		map.put("anken_no", Function.trim(rs.getString("anken_no")));
	    		map.put("TOGO_TORI_CD", Function.trim(rs.getString("togo_tori_cd")));
	    		map.put("mise_cd",rs.getString("mise_cd"));
	    		map.put("system_kbn", rs.getString("system_kbn"));
	    		map.put("satei_kaisha_cd", rs.getString("satei_kaisha_cd"));					// 査定会社コード
	    		map.put("ka_cd",rs.getString("ka_cd"));
	    		map.put("kikan_tori_cd", Function.trim(rs.getString("kikan_tori_cd")));						// 基幹取引先コード
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
	    			//Resultset close
	    			rs.close();
	    		} catch (Exception e) {
	    			throw new SQLException(e.getMessage());
	    		}
	    	}
	    }
	}
	
	/**
	 * 査定検索選択時・検索SQL実行<br>
	 * 
	 * @exception SQLException
	 */
	public void executeSatei() throws SQLException {
		
		ResultSet rs = null;
		ResultSet rs2 = null;

	    // SQL作成
	    StringBuffer sql = new StringBuffer();
	    int x;
	    
	    //取得した検索条件から検索SQLリスト作成
	    List sqlList = new ArrayList();
	    if (!kanjoCd.equals("")) 
	    	sqlList.add(" trim(SS.kikan_tori_cd) LIKE '" + kanjoCd + "%' ");
		/* No.837 性能改善施策によりコメントアウト
	    if (!systemSection.equals(""))
	    	sqlList.add(" SS.system_kbn='" + systemSection + "' ");
		*/
	    if (!dnusNo.equals("")) 
	    	sqlList.add(" TT.togo_tori_cd='" + dnusNo + "' ");
	    if (!kanjoName.equals("")){
	    	sqlList.add(" (TM.business_nm_kj like '%" + kanjoName + "%' OR TM.business_nm like '%" + kanjoName + "%') ");
	    }
	    if (!country.equals("")){
	    	sqlList.add(" TM.wb_country_cd ='" + country.substring(0,3) + "' ");
	    	sqlList.add(" TM.iso_country_nm_rya2 ='" + country.substring(3) + "' ");
	    }
	    //  No422, 2008/05/21, SJA渡辺, 正常先・要注意先同一から正常先、要注意先それぞれ単独のものへ修正
	    if (!dealSection.equals(""))
	    	if (dealSection.equals("1"))
	    		sqlList.add(" (ST.seijo_chk=1) ");
	    	else if (dealSection.equals("2"))
	    		sqlList.add(" (ST.yochui_chk=1) ");
	    	else if (dealSection.equals("3"))
	    		sqlList.add(" (ST.tyoka_chk=1 or ST.kanwa_chk=1 or ST.entai_chk=1) ");
	    	else if (dealSection.equals("4"))
	    		sqlList.add(" (ST.hasanho_chk=1 or ST.kaishaho_chk=1 or ST.koseho_chk=1 or ST.saiseho_chk=1 or ST.shobun_chk=1 or ST.sonota_chk=1) ");
	    if (!claimSection.equals(""))
	    	sqlList.add(" ST.saiken_kbn ='" + claimSection +"' ");
	    
	    // 抽出事由はフラグを検索する
	    if (!caseSelection.equals("")) {
	    	// No612, 2008/06/09, SJA渡辺, 検索条件で指定した抽出事由と詳細画面に表示される抽出事由が合っていない問題修正
	    	if (caseSelection.equals("01"))
	    		sqlList.add(" VW.jiyuu_cd_01='1' ");
	    	else if (caseSelection.equals("02"))
	    		sqlList.add(" VW.jiyuu_cd_02='1' ");
	    	else if (caseSelection.equals("03"))
	    		sqlList.add(" VW.jiyuu_cd_03='1' ");
	    	else if (caseSelection.equals("04"))
	    		sqlList.add(" VW.jiyuu_cd_04='1' ");
	    	else if (caseSelection.equals("05"))
	    		sqlList.add(" VW.jiyuu_cd_05='1' ");
	    	else if (caseSelection.equals("06"))
	    		sqlList.add(" (VW.jiyuu_cd_06='1' or VW.jiyuu_cd_07='1') ");
	    	else if (caseSelection.equals("07"))
	    		sqlList.add(" (VW.jiyuu_cd_06='1' or VW.jiyuu_cd_07='1') ");
	    	else if (caseSelection.equals("08"))
	    		sqlList.add(" VW.jiyuu_cd_08='1' ");
	    	else if (caseSelection.equals("09"))
	    		sqlList.add(" VW.jiyuu_cd_09='1' ");
	    	else if (caseSelection.equals("50"))
	    		sqlList.add(" (VW.jiyuu_cd_50='1' and VW.SYORI_KAISU='1') ");
	    	//結合テスト障害NoIT044対応
	    	//四半期の抽出条件追加
	    	//追加開始
	    	else if (caseSelection.equals("30"))
	    		sqlList.add(" VW.jiyuu_cd_30='1' ");
	    	else if (caseSelection.equals("31"))
	    		sqlList.add(" (VW.jiyuu_cd_31='1'");
	    	else if (caseSelection.equals("32"))
	    		sqlList.add(" (VW.jiyuu_cd_32='1'");
	    	else if (caseSelection.equals("33"))
	    		sqlList.add(" VW.jiyuu_cd_33='1' ");
	    	else if (caseSelection.equals("34"))
	    		sqlList.add(" VW.jiyuu_cd_34='1' ");
	    	else if (caseSelection.equals("35"))
	    		sqlList.add(" VW.jiyuu_cd_35='1' ");
	    	//追加完了
	    	
	    	// No612, 2008/06/09, SJA渡辺, プルダウンで選択された抽出事由の事由名称で事由コードを取得するように修正
	    	/*StringBuffer jiyuuCdSql = new StringBuffer();
	    	jiyuuCdSql.append("SELECT distinct jiyuu_cd ");
	    	jiyuuCdSql.append("FROM SSM_KENTOUJYOUKEN KJ ");
	    	jiyuuCdSql.append("WHERE KT.jiyuu_cd = KJ.jiyuu_cd ");
	    	if ("Ja".equals(cmnData.getComLangMode())) {
	    		jiyuuCdSql.append("jiyuu_nm_kj='").append(caseSelection);
	    	} else {
	    		jiyuuCdSql.append("jiyuu_nm_e='").append(caseSelection);
	    	}
	    	jiyuuCdSql.append("'");
	    	
	    	try {
	    		StringBuffer jiyuuList = new StringBuffer();
	    		jiyuuList.append(" (");
	    		rs2 = sqlExec.execQuery(jiyuuCdSql.toString());
	    		// No612, 2008/06/09, SJA渡辺, 取得した事由コードに紐つくjiyuu_cd_○の値の検索条件を作成
	    		while (rs2.next()) {
	    			jiyuuList.append("VW.jiyuu_cd_")
					.append(rs2.getString("jiyuu_cd"))
					.append("='1' OR ");
	    		}
	    		sqlList.add(jiyuuList.toString().substring(0,jiyuuList.toString().length()-4) + ") ");
	    	} finally {
	    		if (rs2 != null) {
	    			try {
	    				rs2.close();
	    			} catch (Exception e) {
	    				throw new SQLException(e.getMessage());
	    			}
	    		}
	    	}*/
	    }

	    if (!judgeCorp.equals(""))
	    	sqlList.add(" SS.satei_kaisha_cd='" + judgeCorp + "' ");
	    if (!bumonCode.equals("")){
	    	sqlList.add(" SS.honbu_cd ='" + bumonCode + "' ");
	    }else{
	    	sqlList.add(" SS.honbu_cd IN (" + form.getSansyoBunrui2() + ") ");
	    	// 営業担当、会計担当は、自部門に絞る
	    	//if("Y".equals(cmnData.getComEigyoKaiRecogKg()) ||
	    	//		"Y".equals(cmnData.getComEigyoKaiRegistKg())||
	    	//		"Y".equals(cmnData.getComEigyoRecogKg()) ||
	    	//		"Y".equals(cmnData.getComEigyoRegistKg())){
	    	//	StringBuffer tanto_buf = new StringBuffer();
	    	//	tanto_buf.append(" SS.honbu_cd in (");
	    	//	Iterator itr = cmnData.getComTantoCd().iterator();
	    	//	if(!GS.EMPTY_CHARCTER.equals(Function.trim((String)cmnData.getComTantoCd().get(0)))){
	    	//		while(itr.hasNext()){
	    	//			tanto_buf.append("'");
	    	//			tanto_buf.append(itr.next());
	    	//			tanto_buf.append("'");
	    	//			if(itr.hasNext()){
	    	//				tanto_buf.append(",");
	    	//			}
	    	//		}
	    	//	}else if(!GS.EMPTY_CHARCTER.equals(Function.trim(cmnData.getComBumonCd()))){
	    	//		tanto_buf.append("'");
	    	//		tanto_buf.append(cmnData.getComBumonCd());
	    	//		tanto_buf.append("'");
	    	//	}else{
	    	//		tanto_buf.append("' '");
	    	//	}
	    	//	tanto_buf.append(") ");
	    	//	sqlList.add(tanto_buf.toString());
	    	//}

	    }
	    if (!buCode.equals(""))
	    	sqlList.add(" SS.bu_cd ='" + buCode + "' ");
	    
	    
//		final StringBuffer cmnPart = new StringBuffer()
//			.append(" SELECT DISTINCT SS.anken_no,TT.togo_tori_cd,SS.phase, SS.status, SS.kikan_tori_cd,SS.satei_kaisha_cd, SS.honbu_cd, TS1.bumon_nm, ")
//			.append(" SS.bu_cd, SS.taishogai_flg, TS2.bu_nm, SS.ka_cd, TM.business_nm_kj,TM.business_nm,TT.ym,")
//			.append(" ST.seijo_chk,ST.yochui_chk,ST.tyoka_chk,ST.kanwa_chk,ST.entai_chk,ST.hasanho_chk,ST.kaishaho_chk,ST.koseho_chk,ST.saiseho_chk,ST.shobun_chk,ST.sonota_chk,NVL(ST.saiken_kbn,HH70.saiken_kbn) as saiken_kbn,")
//			.append(" HH70.seijo_chk seijo_chk70,HH70.yochui_chk yochui_chk70,HH70.tyoka_chk tyoka_chk70,HH70.kanwa_chk kanwa_chk70,HH70.entai_chk entai_chk70,HH70.hasanho_chk hasanho_chk70,HH70.kaishaho_chk kaishaho_chk70,HH70.koseho_chk koseho_chk70,HH70.saiseho_chk saiseho_chk70,HH70.shobun_chk shobun_chk70,HH70.sonota_chk sonota_chk70,")
//			.append(" TM.country_nm,")
//			.append(" NVL(HH.kingaku,HH70.kingaku) as kingaku,")
//			.append(" '(' || TRIM(HH1.tuuka_cd) || ')' as tuuka_cd, ")
//			.append(" HH70.tuuka_cd as tuuka_cd70, ")
//			.append(" NVL(ST.tuika_hikiate,HH70.tuika_hikiate) as tuika_hikiate, P.phase_nm, S.status_nm,SS.mise_cd, SS.system_kbn, ")
//			.append(" VW.jiyuu_cd_01, VW.jiyuu_cd_02, VW.jiyuu_cd_03, VW.jiyuu_cd_04,VW.jiyuu_cd_05, VW.jiyuu_cd_06, VW.jiyuu_cd_07, VW.jiyuu_cd_08, VW.jiyuu_cd_09, ")
//			.append(" SS.ins_dt, SS.upd_dt ")
//			.append(" FROM SST_SATEI_STAT SS")
//			.append(" LEFT JOIN (SELECT SUM(kingaku) AS kingaku, TRIM(tuuka_cd) AS tuuka_cd, satei_anken_no, tori_cd, syori_kaisu, ym, mise_cd FROM SST_HIKIATEHANTEI")
//			.append(" WHERE kanjo_hyouji_kbn in ('00','01','02','03','04','05','06','07','08','09','10','11','12','16')")
//			.append(" GROUP BY tori_cd, tuuka_cd, satei_anken_no, ")
//			.append(" syori_kaisu, ym, mise_cd ) HH ON")
//			.append(" SS.ANKEN_NO = HH.SATEI_ANKEN_NO")
//			.append(" LEFT JOIN ")
//			.append(" SST_HIKIATEHANTEI HH1 ON")
//			.append(" SS.ANKEN_NO = HH1.SATEI_ANKEN_NO")
//			.append(" LEFT JOIN SST_SATEI ST ON")
//			.append(" ST.ANKEN_NO = HH1.SATEI_ANKEN_NO")
//			.append(" AND ST.PHASE = SS.PHASE")
//			.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS1 ON")
//			.append(" TS1.SATEI_KAISHA_CD = SS.SATEI_KAISHA_CD AND TS1.BUMON_CD = SS.HONBU_CD")
//			.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS2 ON")
//			.append(" TS2.SATEI_KAISHA_CD = SS.SATEI_KAISHA_CD AND TS2.BUMON_CD = SS.HONBU_CD AND TS2.BU_CD = SS.BU_CD")
//			.append(" LEFT JOIN SSE_TAIHI TT ON")
//			.append(" TT.KIKAN_TORI_CD = TRIM(SS.KIKAN_TORI_CD)|| '00' AND TT.SYSTEM_KBN = SS.SYSTEM_KBN AND TT.OFFICE_CD = SS.MISE_CD AND TT.YM = SS.YM")
//			.append(" LEFT JOIN SSE_TOGO_MST TM ON")
//			.append(" TM.TOGO_TORI_CD = TT.TOGO_TORI_CD AND TM.SIKIBETU_CD = TT.SIKIBETU_CD AND TM.YM = TT.YM AND TM.SYORI_KAISU = TT.SYORI_KAISU ")
//			.append(" LEFT JOIN VW_SS_SATEIKEKKASYOUKAI VW ON")
//			.append(" VW.YM = SS.YM AND VW.TORI_CD = SS.KIKAN_TORI_CD AND VW.SYORI_KAISU = TT.SYORI_KAISU AND VW.SATEIKAISYA_CD = SS.SATEI_KAISHA_CD AND VW.SYSTEM_KBN = SS.SYSTEM_KBN")
//			// No419、420, 2008/05/21, SJA渡辺, 前回実施データを一気に取得するように修正
//			.append(" LEFT JOIN (SELECT ST.seijo_chk,ST.yochui_chk,ST.tyoka_chk,ST.kanwa_chk,ST.entai_chk,ST.hasanho_chk,ST.kaishaho_chk,ST.koseho_chk,ST.saiseho_chk,ST.shobun_chk,ST.sonota_chk,ST.saiken_kbn,NVL(ST.tuika_hikiate,0) as tuika_hikiate,ST.hikiate_hosei, HH.SATEI_ANKEN_NO, NVL(HH.kingaku,0) as kingaku, '(' || TRIM(HH1.tuuka_cd) || ')' as tuuka_cd,  row_number() over (PARTITION BY HH1.TORI_CD,HH1.SYSTEM_KBN,HH1.SATEIKAISYA_CD,HH1.MISE_CD order by HH1.ym desc) ct,HH1.TORI_CD,HH1.SYSTEM_KBN,HH1.SATEIKAISYA_CD,HH1.MISE_CD  FROM SST_HIKIATEHANTEI HH1  LEFT JOIN (SELECT SUM(kingaku) AS kingaku, TRIM(tuuka_cd) AS tuuka_cd, satei_anken_no, tori_cd, syori_kaisu, ym, mise_cd FROM SST_HIKIATEHANTEI WHERE kanjo_hyouji_kbn in ('00','01','02','03','04','05','06','07','08','09','10','11','12','16') GROUP BY tori_cd, tuuka_cd, satei_anken_no,  syori_kaisu, ym, mise_cd ) HH ON HH1.SATEI_ANKEN_NO = HH.SATEI_ANKEN_NO LEFT JOIN SST_SATEI_STAT SS ON  HH1.SATEI_ANKEN_NO=SS.ANKEN_NO  LEFT JOIN SST_SATEI ST ON SS.ANKEN_NO=ST.ANKEN_NO  AND SS.PHASE = ST.PHASE ORDER BY HH1.YM DESC) HH70 ON HH70.TORI_CD=SS.kikan_tori_cd and HH70.SYSTEM_KBN=SS.system_kbn and HH70.SATEIKAISYA_CD=SS.satei_kaisha_cd and HH70.MISE_CD=SS.mise_cd")
//			.append(" LEFT JOIN (SELECT kbn_key, kbn_val, kbn_hyouji_val AS phase_nm, LANG_MODE FROM SSP_KBN WHERE LANG_MODE='")
//			.append(cmnData.getComLangMode())
//			.append("' AND kbn_key='phase') P ON")
//			.append(" P.kbn_val = SS.phase")
//			.append(" LEFT JOIN (SELECT kbn_key, kbn_val, kbn_hyouji_val AS status_Nm, LANG_MODE FROM SSP_KBN WHERE LANG_MODE='")
//			.append(cmnData.getComLangMode())
//			.append("' AND kbn_key='status') S ON")
//			.append(" S.kbn_val = SS.status")
//			.append(" WHERE ")
//			.append("SS.phase != '30' and ct=1 and (SS.taishogai_flg !='2' or SS.taishogai_flg IS NULL)");

	    // 障害No418 2008.05.21 uechi 取引先区分でソート対応
	    // 障害No543 2008.06.03 SJA中島 泣き別れ原因となっていた事由コードの取得を行わない。取得した事由コードは特に使用していないため問題なし
	    StringBuffer cmnPart = new StringBuffer();
	    cmnPart.append(" SELECT ");
	    cmnPart.append(" DISTINCT ");
	    cmnPart.append(" SS.anken_no,");
	    //要件No.四-10
	    //追加開始
	    cmnPart.append(" SS.hanki_sihanki_kbn,");
	    //追加完了
	    cmnPart.append(" TT.togo_tori_cd,");
	    cmnPart.append(" SS.phase, ");
	    cmnPart.append(" SS.status, ");
	    cmnPart.append(" SS.kikan_tori_cd,");
	    cmnPart.append(" SS.satei_kaisha_cd, ");
	    cmnPart.append(" SS.honbu_cd, ");
	    cmnPart.append(" TS1.bumon_nm,  ");
	    cmnPart.append(" SS.bu_cd, ");
	    cmnPart.append(" SS.taishogai_flg, ");
	    cmnPart.append(" TS2.bu_nm, ");
	    cmnPart.append(" SS.ka_cd, ");
	    cmnPart.append(" TM.business_nm_kj,");
	    cmnPart.append(" TM.business_nm,");
	    cmnPart.append(" TT.ym, ");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.seijo_chk, ST.seijo_chk),'0') seijo_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.yochui_chk, ST.yochui_chk),'0') yochui_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.tyoka_chk, ST.tyoka_chk),'0') tyoka_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.kanwa_chk, ST.kanwa_chk),'0') kanwa_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.entai_chk, ST.entai_chk),'0') entai_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.hasanho_chk, ST.hasanho_chk),'0') hasanho_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.kaishaho_chk, ST.kaishaho_chk),'0') kaishaho_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.koseho_chk, ST.koseho_chk),'0') koseho_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.saiseho_chk, ST.saiseho_chk),'0') saiseho_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.shobun_chk, ST.shobun_chk),'0') shobun_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.sonota_chk, ST.sonota_chk),'0') sonota_chk,");
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.saiken_kbn, ST.saiken_kbn),'0') as saiken_kbn, ");
	    cmnPart.append(" TM.country_nm, ");
	    // No449, 2008/05/23, SJA渡辺, 値がNULLの場合、0に変換するように修正
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.kingaku, HH.kingaku),'0') as kingaku, ");
	    cmnPart.append(" '(' || TRIM(HH1.tuuka_cd) || ')' as tuuka_cd,  ");
	    cmnPart.append(" HH70.tuuka_cd as tuuka_cd70,  ");
	    // No449, 2008/06/07, SJA中島, 値がNULLの場合、0に変換するように修正
	    cmnPart.append(" nvl(decode(SS.phase, '70', HH70.tuika_hikiate,ST.tuika_hikiate),'0') as tuika_hikiate, ");
	    cmnPart.append(" P.phase_nm, ");
	    cmnPart.append(" decode(SS.phase, '30', Z.status_nm_phase30, S.status_nm) status_nm, ");
	    cmnPart.append(" SS.mise_cd, ");
	    cmnPart.append(" SS.system_kbn,  ");
	    cmnPart.append(" SS.ins_dt, ");
	    cmnPart.append(" SS.upd_dt  ");
	    cmnPart.append(" FROM ");
	    cmnPart.append(" SST_SATEI_STAT SS ");
	    cmnPart.append(" LEFT JOIN (");
	    cmnPart.append(" SELECT ");
	    cmnPart.append(" SUM(kingaku) AS kingaku, ");
	    cmnPart.append(" TRIM(tuuka_cd) AS tuuka_cd, ");
	    cmnPart.append(" satei_anken_no, ");
	    cmnPart.append(" tori_cd, ");
	    cmnPart.append(" syori_kaisu, ");
	    cmnPart.append(" ym, ");
	    cmnPart.append(" mise_cd ");
	    cmnPart.append(" FROM ");
	    cmnPart.append(" SST_HIKIATEHANTEI ");
	    cmnPart.append(" WHERE ");
	    cmnPart.append(" kanjo_hyouji_kbn in (");
	    cmnPart.append(" '00','01','02','03','04','05','06',");
	    cmnPart.append(" '07','08','09','10','11','12','16') ");

		// No.837, yyyy/MM/dd, SJA渡辺, 性能改善施策
		if ("".equals(judgeCorp)) {												/* No.837 */
			cmnPart.append(createConditionStr("sateikaisya_cd",sateiCorpList,true,true,false));/* No.837 */
		} else {																/* No.837 */
			cmnPart.append(" AND sateikaisya_cd = '").append(judgeCorp).append("' ");/* No.837 */
		}																		/* No.837 */
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(objectDate))){				/* No.837 */
			cmnPart.append("AND ym = '");										/* No.837 */
			cmnPart.append(objectDate);											/* No.837 */
			cmnPart.append("' ");												/* No.837 */
		}else if (dateList.size() != 0) {										/* No.837 */
			cmnPart.append(createConditionStr("ym",dateList,true,true,false));	/* No.837 */
		} else {																/* No.837 */
			return;																/* No.837 */
		}																		/* No.837 */
		if (!systemSection.equals("")) {										/* No.837 */
			cmnPart.append(" AND system_kbn='" + systemSection + "' ");			/* No.837 */
		}																		/* No.837 */

	    cmnPart.append(" GROUP BY ");
	    cmnPart.append(" tori_cd, ");
	    cmnPart.append(" tuuka_cd, ");
	    cmnPart.append(" satei_anken_no,  ");
	    cmnPart.append(" syori_kaisu, ");
	    cmnPart.append(" ym, ");
	    cmnPart.append(" mise_cd ) HH ");
	    cmnPart.append(" ON SS.ANKEN_NO = HH.SATEI_ANKEN_NO ");
	    cmnPart.append(" LEFT JOIN  SST_HIKIATEHANTEI HH1 ");
	    cmnPart.append(" ON SS.ANKEN_NO = HH1.SATEI_ANKEN_NO ");
	    cmnPart.append(" LEFT JOIN SST_SATEI ST ");
	    cmnPart.append(" ON ST.ANKEN_NO = HH1.SATEI_ANKEN_NO ");
	    cmnPart.append(" AND ST.PHASE = SS.PHASE ");

		cmnPart.append(" LEFT JOIN (select satei_kaisha_cd,bumon_cd,max(bumon_nm) bumon_nm from SSM_TOGO_SOSHIKI ");/* No.837 */
		cmnPart.append(" group by satei_kaisha_cd,bumon_cd) TS1 ");				/* No.837 */
		cmnPart.append(" ON TS1.satei_kaisha_cd = SS.satei_kaisha_cd ");		/* No.837 */
		cmnPart.append(" AND TS1.BUMON_CD = SS.HONBU_CD ");						/* No.837 */
		cmnPart.append(" LEFT JOIN (select satei_kaisha_cd,bu_cd,max(bu_nm) bu_nm from SSM_TOGO_SOSHIKI ");/* No.837 */
		cmnPart.append(" group by satei_kaisha_cd,bu_cd) TS2 ");				/* No.837 */
		cmnPart.append(" ON TS2.satei_kaisha_cd = SS.satei_kaisha_cd ");		/* No.837 */
		cmnPart.append(" AND TS2.BU_CD = SS.BU_CD ");							/* No.837 */
		/* No.837 性能改善施策によりコメントアウト
	    cmnPart.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS1 ");
	    cmnPart.append(" ON TS1.SATEI_KAISHA_CD = SS.SATEI_KAISHA_CD ");
	    cmnPart.append(" AND TS1.BUMON_CD = SS.HONBU_CD ");
	    cmnPart.append(" LEFT JOIN SSM_TOGO_SOSHIKI TS2 ");
	    cmnPart.append(" ON TS2.SATEI_KAISHA_CD = SS.SATEI_KAISHA_CD ");
	    cmnPart.append(" AND TS2.BUMON_CD = SS.HONBU_CD ");
	    cmnPart.append(" AND TS2.BU_CD = SS.BU_CD ");								No.837 */
	    cmnPart.append(" LEFT JOIN SSE_TAIHI TT ");
	    cmnPart.append(" ON TT.KIKAN_TORI_CD = TRIM(SS.KIKAN_TORI_CD)|| '00' ");
	    cmnPart.append(" AND TT.SYSTEM_KBN = SS.SYSTEM_KBN ");
	    cmnPart.append(" AND TT.OFFICE_CD = SS.MISE_CD ");
	    cmnPart.append(" AND TT.YM = SS.YM ");
		cmnPart.append(" LEFT JOIN SSE_TOGO_MST TM ");							/* No.837 */
		cmnPart.append(" ON TM.YM = TT.YM ");									/* No.837 */
	    cmnPart.append(" AND TM.SIKIBETU_CD = TT.SIKIBETU_CD ");
		cmnPart.append(" AND TM.TOGO_TORI_CD = TT.TOGO_TORI_CD ");				/* No.837 */
	    cmnPart.append(" AND TM.SYORI_KAISU = TT.SYORI_KAISU  ");
		/* No.837 性能改善施策によりコメントアウト
	    cmnPart.append(" LEFT JOIN SSE_TOGO_MST TM ");
	    cmnPart.append(" ON TM.TOGO_TORI_CD = TT.TOGO_TORI_CD ");
	    cmnPart.append(" AND TM.SIKIBETU_CD = TT.SIKIBETU_CD ");
	    cmnPart.append(" AND TM.YM = TT.YM ");
	    cmnPart.append(" AND TM.SYORI_KAISU = TT.SYORI_KAISU  ");					No.837 */
	    cmnPart.append(" LEFT JOIN VW_SS_SATEIKEKKASYOUKAI VW ");
	    cmnPart.append(" ON VW.YM = SS.YM ");
	    //要件No.四-10
	    //追加開始
	    cmnPart.append(" AND VW.hanki_sihanki_kbn = SS.hanki_sihanki_kbn ");
	    //追加完了
	    cmnPart.append(" AND VW.TORI_CD = SS.KIKAN_TORI_CD ");
	    cmnPart.append(" AND VW.SYORI_KAISU = TT.SYORI_KAISU ");
	    cmnPart.append(" AND VW.SATEIKAISYA_CD = SS.SATEI_KAISHA_CD ");
	    cmnPart.append(" AND VW.SYSTEM_KBN = SS.SYSTEM_KBN ");
	    cmnPart.append(" LEFT JOIN (");
	    cmnPart.append(" SELECT ");
		/* No.837 性能改善施策によりコメントアウト
	    cmnPart.append(" ST.seijo_chk,");
	    cmnPart.append(" ST.yochui_chk,");
	    cmnPart.append(" ST.tyoka_chk,");
	    cmnPart.append(" ST.kanwa_chk,");
	    cmnPart.append(" ST.entai_chk,");
	    cmnPart.append(" ST.hasanho_chk,");
	    cmnPart.append(" ST.kaishaho_chk,");
	    cmnPart.append(" ST.koseho_chk,");
	    cmnPart.append(" ST.saiseho_chk,");
	    cmnPart.append(" ST.shobun_chk,");
	    cmnPart.append(" ST.sonota_chk,");
	    cmnPart.append(" ST.saiken_kbn,");
	    cmnPart.append(" NVL(ST.tuika_hikiate,0) as tuika_hikiate,");
	    cmnPart.append(" ST.hikiate_hosei, ");									  No.837 */
		cmnPart.append(" ST2.seijo_chk,");										/* No.837 */
		cmnPart.append(" ST2.yochui_chk,");										/* No.837 */
		cmnPart.append(" ST2.tyoka_chk,");										/* No.837 */
		cmnPart.append(" ST2.kanwa_chk,");										/* No.837 */
		cmnPart.append(" ST2.entai_chk,");										/* No.837 */
		cmnPart.append(" ST2.hasanho_chk,");									/* No.837 */
		cmnPart.append(" ST2.kaishaho_chk,");									/* No.837 */
		cmnPart.append(" ST2.koseho_chk,");										/* No.837 */
		cmnPart.append(" ST2.saiseho_chk,");									/* No.837 */
		cmnPart.append(" ST2.shobun_chk,");										/* No.837 */
		cmnPart.append(" ST2.sonota_chk,");										/* No.837 */
		cmnPart.append(" ST2.saiken_kbn,");										/* No.837 */
		cmnPart.append(" NVL(ST2.tuika_hikiate,0) as tuika_hikiate,");			/* No.837 */
		cmnPart.append(" ST2.hikiate_hosei, ");									/* No.837 */
	    cmnPart.append(" HH.SATEI_ANKEN_NO, ");
	    cmnPart.append(" NVL(HH.kingaku,0) as kingaku, ");
	    cmnPart.append(" '(' || TRIM(HH1.tuuka_cd) || ')' as tuuka_cd,  ");
	    cmnPart.append(" row_number() over (");
	    cmnPart.append(" PARTITION BY ");
	    cmnPart.append(" HH1.TORI_CD,");
	    cmnPart.append(" HH1.SYSTEM_KBN,");
	    cmnPart.append(" HH1.SATEIKAISYA_CD,");
	    cmnPart.append(" HH1.MISE_CD ");
	    cmnPart.append(" order by HH1.ym desc) ct,");
	    cmnPart.append(" HH1.TORI_CD,");
	    cmnPart.append(" HH1.SYSTEM_KBN,");
	    cmnPart.append(" HH1.SATEIKAISYA_CD,");
	    cmnPart.append(" HH1.MISE_CD  FROM SST_HIKIATEHANTEI HH1  ");
	    cmnPart.append(" LEFT JOIN (");
	    cmnPart.append(" SELECT SUM(kingaku) AS kingaku, ");
	    cmnPart.append(" TRIM(tuuka_cd) AS tuuka_cd, ");
	    cmnPart.append(" satei_anken_no, ");
	    cmnPart.append(" tori_cd, ");
	    cmnPart.append(" syori_kaisu, ");
	    cmnPart.append(" ym, ");
	    cmnPart.append(" mise_cd ");
	    cmnPart.append(" FROM SST_HIKIATEHANTEI ");
	    cmnPart.append(" WHERE kanjo_hyouji_kbn in (");
	    cmnPart.append(" '00','01','02','03','04','05','06',");
	    cmnPart.append(" '07','08','09','10','11','12','16') ");
	    cmnPart.append(" GROUP BY ");
	    cmnPart.append(" tori_cd, ");
	    cmnPart.append(" tuuka_cd, ");
	    cmnPart.append(" satei_anken_no,  ");
	    cmnPart.append(" syori_kaisu, ");
	    cmnPart.append(" ym, ");
	    cmnPart.append(" mise_cd ) HH ");
	    cmnPart.append(" ON HH1.SATEI_ANKEN_NO = HH.SATEI_ANKEN_NO ");
	    cmnPart.append(" LEFT JOIN SST_SATEI_STAT SS ");
	    cmnPart.append(" ON  HH1.SATEI_ANKEN_NO=SS.ANKEN_NO  ");
		/* No.837
	    cmnPart.append(" LEFT JOIN SST_SATEI ST ");
	    cmnPart.append(" ON SS.ANKEN_NO=ST.ANKEN_NO  ");
	    cmnPart.append(" AND SS.PHASE = ST.PHASE ");
	    cmnPart.append(" ORDER BY HH1.YM DESC) HH70 ");							   No.837 */
		cmnPart.append(" LEFT JOIN SST_SATEI ST2 ");							/* No.837 */
		cmnPart.append(" ON SS.ANKEN_NO=ST2.ANKEN_NO  ");						/* No.837 */
		cmnPart.append(" AND SS.PHASE = ST2.PHASE ");							/* No.837 */
		cmnPart.append(" ) HH70 ");
	    cmnPart.append(" ON HH70.TORI_CD=SS.kikan_tori_cd ");
	    cmnPart.append(" and HH70.SYSTEM_KBN=SS.system_kbn ");
	    cmnPart.append(" and HH70.SATEIKAISYA_CD=SS.satei_kaisha_cd ");
	    cmnPart.append(" and HH70.MISE_CD=SS.mise_cd ");
	    cmnPart.append(" LEFT JOIN (");
	    cmnPart.append(" SELECT ");
	    cmnPart.append(" kbn_key, ");
	    cmnPart.append(" kbn_val, ");
	    cmnPart.append(" kbn_hyouji_val AS phase_nm, ");
	    cmnPart.append(" LANG_MODE ");
	    cmnPart.append(" FROM SSP_KBN ");
	    cmnPart.append(" WHERE LANG_MODE='").append(cmnData.getComLangMode()).append("' ");
	    cmnPart.append(" AND system_kbn = '01' ");
	    cmnPart.append(" AND kbn_key='phase') P ");
	    cmnPart.append(" ON P.kbn_val = SS.phase ");
	    cmnPart.append(" LEFT JOIN (");
	    cmnPart.append(" SELECT ");
	    cmnPart.append(" kbn_key, ");
	    cmnPart.append(" kbn_val, ");
	    cmnPart.append(" kbn_hyouji_val AS status_Nm, ");
	    cmnPart.append(" LANG_MODE ");
	    cmnPart.append(" FROM SSP_KBN ");
	    cmnPart.append(" WHERE LANG_MODE='").append(cmnData.getComLangMode()).append("' ");
	    cmnPart.append(" AND system_kbn = '01' ");
	    cmnPart.append(" AND kbn_key='status') S ");
	    cmnPart.append(" ON S.kbn_val = SS.status ");
	    cmnPart.append(" LEFT JOIN (SELECT kbn_key, ");
	    cmnPart.append(" kbn_val, ");
	    cmnPart.append(" kbn_hyouji_val AS status_nm_phase30 ");
	    cmnPart.append(" FROM SSP_KBN ");
	    cmnPart.append(" WHERE LANG_MODE='").append(cmnData.getComLangMode()).append("' ");
	    cmnPart.append(" AND system_kbn = '01' ");
	    cmnPart.append(" AND kbn_key='status_phase30') Z ");
		cmnPart.append(" ON Z.kbn_val = SS.status ");
	    cmnPart.append(" WHERE ct=1 ");
	    // No473, 2008/05/24, SJA渡辺, 特殊権限の場合のみ表示するように修正
	    // No473,402, 2008/05/25, SJA渡辺, 特殊権限の場合のみ対象先選定の対象外にしている案件も表示するように修正し、
	    // 特殊権限以外の場合は対象先選定の対象外以外の案件が表示されるように修正。
	    if ("01".equals(cmnData.getComUniqueKg()) 
	    		|| "02".equals(cmnData.getComUniqueKg()) 
				|| "03".equals(cmnData.getComUniqueKg())) {
	    	cmnPart.append(" and (SS.taishogai_flg !='2' or SS.taishogai_flg IS NULL) ");
	    } else {
	    	cmnPart.append(" and ((SS.taishogai_flg != '1' and SS.taishogai_flg !='2') or SS.taishogai_flg IS NULL)");
	    }
		
		//検索用年月リスト入っている年月をSQLに追記する
		//リスト内が空の場合、検索せずに終了する。
	    // 障害票：　チェックイン日：2008/6/13　SJA中島　対象年月が入っていた場合、対象年月で絞り込む
	    if(!GS.EMPTY_CHARCTER.equals(Function.trim(objectDate))){
	    	cmnPart.append("AND SS.ym = '");
	    	cmnPart.append(objectDate);
	    	cmnPart.append("' AND");
	    }else if (dateList.size() != 0) {
			cmnPart.append(createConditionStr("SS.ym",dateList,true,true,true));/* No.837 */
			/* No.837 性能改善施策によりコメントアウト
	    	cmnPart.append("AND SS.ym = ANY (");
	    	for (x=0; x<dateList.size(); x++) {
	    		cmnPart.append("'" + dateList.get(x) + "'");
	    		if (x<dateList.size()-1)
	    			cmnPart.append(", ");
	    	}
	    	cmnPart.append(") AND");											   No.837 */
	    } else {
	    	return;
	    }
	    //要件No.四-10
	    //追加開始
	    cmnPart.append(" SS.hanki_sihanki_kbn = '")
			   .append(form.getHanki_sihanki_kbn())
			   .append("' AND ");
	    
	    //追加完了

		if (!systemSection.equals("")) {										/* No.837 */
			cmnPart.append(" SS.system_kbn='" + systemSection + "' AND ");		/* No.837 */
		}																		/* No.837 */

	    // 検索査定会社の指定がない場合、ログインユーザーが参照可能な組織内のデータを検索する。
	    if (judgeCorp.equals("")) {
			/* No.837
	        cmnPart.append(" SS.satei_kaisha_cd = ANY(");
	        for (x=0; x<sateiCorpList.size(); x++) {
	            cmnPart.append("'" + sateiCorpList.get(x) + "'");
	            if (x<sateiCorpList.size()-1)
	                cmnPart.append(", ");
	        }
	        cmnPart.append(") ");
	        if (sqlList.size()>0)
	            cmnPart.append("AND");												No.837 */
			cmnPart.append(createConditionStr("SS.satei_kaisha_cd",sateiCorpList,true,false,false));/* No.837 */
			if (sqlList.size()>0)
				cmnPart.append("AND");

	    }
	
	    //検索SQLリストに入っている文章を追記する
	    for (x=0; x<sqlList.size(); x++) {
	    	cmnPart.append(sqlList.get(x));
	    	if (x<sqlList.size()-1)
	    		cmnPart.append("AND");
	    }
	    	

	    sql.append(cmnPart);
	    
	    // 障害票：543　チェックイン日：2008/6/15　SJA中島　抽出事由ビューの検索条件を追加。
	    sql.append(" AND ((SS.PHASE='70' and VW.JIYUU_CD_50='1') or (SS.PHASE <> '70' and VW.JIYUU_CD_50='0')) ");
	    
	    // No547, 2008/06/07, SJA渡辺, 帳票と画面のソートを合わせるためにソートの第二引数に勘定先CDを指定するように修正
	    // SQL - ORDER BY句作成
	    if(sort == 0) {
	        sql.append("ORDER BY SS.kikan_tori_cd ASC");
	    } else if(sort == 1) {
	    	// No417, 2008/05/21, SJA渡辺, 言語モードにかかわらず、英語名称で昇順にソートするように修正。
	        /*if (cmnData.getComLangMode().equals("Ja"))
	            sql.append("ORDER BY TM.business_nm_kj ASC");
	        else if (cmnData.getComLangMode().equals("En"))*/
	            sql.append("ORDER BY TM.business_nm ASC,SS.kikan_tori_cd ASC");
	    } else if(sort == 2) {
	        sql.append("ORDER BY TT.ym ASC,SS.kikan_tori_cd ASC");
	    } else if(sort == 3) {
	    	// 障害No418 2008.05.21 uechi 取引先区分でソート対応
//	        sql.append("ORDER BY ST.seijo_chk, ST.yochui_chk, ST.tyoka_chk DESC");
	    	// No418, 2008/05/23, SJA渡辺, 取引先区分と債権区分のソートをそろえるように修正
	    	sql.append(" order by hasanho_chk ASC, kaishaho_chk ASC,");
	    	sql.append(" koseho_chk ASC, saiseho_chk ASC,");
	    	sql.append(" shobun_chk ASC, sonota_chk ASC,");
	    	sql.append(" tyoka_chk ASC, kanwa_chk ASC,");
	    	sql.append(" entai_chk ASC, yochui_chk ASC,");
	    	sql.append(" seijo_chk ASC,SS.kikan_tori_cd ASC");
	    	
	    } else if(sort == 4) {
	        sql.append("ORDER BY saiken_kbn ASC,SS.kikan_tori_cd ASC");
        //要件No.四-10　障害No.0002対応
		//追加開始
	    } else if(sort == 5) {
	    	sql.append("ORDER BY SS.honbu_cd ASC,SS.kikan_tori_cd ASC");   
	    } else if(sort == 6) {
	    	// No420, 2008/05/21, SJA渡辺, 昇順でソートするように修正。
	        sql.append("ORDER BY kingaku ASC,SS.kikan_tori_cd ASC");
	    } else if(sort == 7) {
	        sql.append("ORDER BY tuika_hikiate ASC,SS.kikan_tori_cd ASC");
	    } else if(sort == 8) {
	        sql.append("ORDER BY SS.phase ASC, SS.status ASC,SS.kikan_tori_cd ASC");
	    }
	    //追加完了
	    //} else if(sort == 5) {
	    	// No420, 2008/05/21, SJA渡辺, 昇順でソートするように修正。
	    //    sql.append("ORDER BY kingaku ASC,SS.kikan_tori_cd ASC");
	    //} else if(sort == 6) {
	    //    sql.append("ORDER BY tuika_hikiate ASC,SS.kikan_tori_cd ASC");
	    //} else if(sort == 7) {
	    //    sql.append("ORDER BY SS.phase ASC, SS.status ASC,SS.kikan_tori_cd ASC");
	    //} 
       // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
	    //log.write(GS.LOG_INF,CLASSNAME, "実行SQL：" + sql);
  
		try {
			// SQL実行		
			rs = sqlExec.execQuery(sql.toString());

			// ActionForm 明細件数に取得レコード数を格納
			form.setCnt_meisai(getRsCount(rs));
			
			//検索結果が1000件を超えた場合、処理を中断する
			if (form.getCnt_meisai()>maxCount) {
				rs.close();
				return;
			}
			
			String show_9 = getHyoujiVal("ktk","9");							/* No.837 */
			String show_W = getHyoujiVal("ktk","W");							/* No.837 */
			String show_T = getHyoujiVal("ktk","T");							/* No.837 */

			// ActionForm に取得値を格納
			List ar_meisai = new ArrayList();	// 明細配列	
			int i = 0;
			int j = 0;
			while ( rs.next() ) {
				HashMap map = new HashMap();
				map.put("id", Function.getStringOfInt(i));
				if (cmnData.getComLangMode().equals("Ja")) { 
					// 障害対応：200807250001　2008/7/25 中島　企業名にNULLと出力されるのを抑止する
					if(rs.getString("business_nm_kj") == null){
						map.put("business_nm", Function.trim(rs.getString("business_nm")) + " ");					// 企業名(英語)
					}else{
						map.put("business_nm", Function.trim(rs.getString("business_nm_kj")) + " ");				// 企業名(日本語）
					}
				}else{
					map.put("business_nm", Function.trim(rs.getString("business_nm")) + " ");					// 企業名(英語)
				}
				//要件No.四-10 四半期データの場合、対象年月に(Q)を付与
				//追加開始
				if (rs.getString("hanki_sihanki_kbn").equals(HANKI)){
					map.put("y_m", Function.insertYmSlash(rs.getString("ym")));
				}else{
					map.put("y_m", Function.addQuarter(Function.insertYmSlash(rs.getString("ym"))));   //(Q)付き年月
				}		
				//map.put("y_m", Function.insertYmSlash(rs.getString("ym")));						// 『/』区切り年月
				//追加完了
				//要件No.四-10　障害No.0001対応
				//追加開始
				map.put("hanki_sihanki_kbn", rs.getString("hanki_sihanki_kbn"));                    //半期四半期区分
				//追加完了
				
				//詳細ページタブ用のデータも合わせて登録
				map.put("phase", rs.getString("phase"));										// フェーズナンバー
				map.put("status", rs.getString("status"));

				map.put("ym", rs.getString("ym"));												// 年月(/区切り無し)
				map.put("anken_no", Function.trim(rs.getString("anken_no")));					// 査定案件No
				map.put("TOGO_TORI_CD", Function.trim(rs.getString("togo_tori_cd")));			// 統合取引先コード
				//引当金判定タブ追加分
				map.put("system_kbn", rs.getString("system_kbn"));								// システム区分
				map.put("mise_cd", rs.getString("mise_cd"));									// 店コード
				map.put("kikan_tori_cd", Function.trim(rs.getString("kikan_tori_cd")));			// 基幹取引先コード
				map.put("satei_kaisha_cd", rs.getString("satei_kaisha_cd"));

				// 障害No418 2008.05.21 uechi 取引先区分でソート対応
//				if("70".equals(rs.getString("phase"))){
//					////////////////////////////////////////////////////////////////
//					//障害票：342、343
//					//チェックイン日：2008/5/17
//					//対応者：SJA中島
//					//概要：引当金検証フェーズの場合、前回実施データを取得するように修正
//					////////////////////////////////////////////////////////////////
//					// 引当金検証時は、前回実施データを取得
//					/*getLastAnkenData(map);*/
//					// No419、420, 2008/05/21, SJA渡辺, 前回実施データを一気に取得するように修正
//					if(rs.getString("seijo_chk70")!=null){
//						map.put("seijo", rs.getString("seijo_chk70"));									// xxチェック（正常）
//					}else{
//						map.put("seijo", "0");
//					}
//					if(rs.getString("yochui_chk70")!=null){
//						map.put("yochui", rs.getString("yochui_chk70"));									// xxチェック（要注意）
//					}else{
//						map.put("yochui", "0");
//					}
//					if(rs.getString("tyoka_chk70")!=null){
//						map.put("tyoka", rs.getString("tyoka_chk70"));									// xxチェック（実質債務超過）
//					}else{
//						map.put("tyoka", "0");
//					}
//					if(rs.getString("kanwa_chk70")!=null){
//						map.put("kanwa", rs.getString("kanwa_chk70"));									// xxチェック（弁済条件緩和）
//					}else{
//						map.put("kanwa", "0");
//					}
//					if(rs.getString("entai_chk70")!=null){
//						map.put("entai", rs.getString("entai_chk70"));									// xxチェック（１年以上延滞チェック）
//					}else{
//						map.put("entai", "0");
//					}
//					if(rs.getString("hasanho_chk70")!=null){
//						map.put("hasanho", rs.getString("hasanho_chk70"));								// xxチェック（破産法適用チェック）
//					}else{
//						map.put("hasanho", "0");
//					}
//					if(rs.getString("kaishaho_chk70")!=null){
//						map.put("kaishaho", rs.getString("kaishaho_chk70"));								// xxチェック（会社法適用チェック）
//					}else{
//						map.put("kaishaho", "0");
//					}
//					if(rs.getString("koseho_chk70")!=null){
//						map.put("koseho", rs.getString("koseho_chk70"));									// xxチェック（会社更生法適用チェック）
//					}else{
//						map.put("koseho", "0");
//					}
//					if(rs.getString("saiseho_chk70")!=null){
//						map.put("saiseho", rs.getString("saiseho_chk70"));								// xxチェック（民事再生法適用チェック）
//					}else{
//						map.put("saiseho", "0");
//					}
//					if(rs.getString("shobun_chk70")!=null){
//						map.put("shobun", rs.getString("shobun_chk70"));									// xxチェック（取引停止処分チェック）
//					}else{
//						map.put("shobun", "0");
//					}
//					if(rs.getString("sonota_chk70")!=null){
//						map.put("sonota", rs.getString("sonota_chk70"));									// xxチェック（その他チェック）
//					}else{
//						map.put("sonota", "0");
//					}
//					map.put("tuika_hikiate", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("tuika_hikiate"))) + rs.getString("tuuka_cd70"));	// 追加引当金額
//					map.put("kingaku", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("kingaku"))) + rs.getString("tuuka_cd70") );				// 金額 (引当金判定表示用)
//				}else{
					/*  No.837 性能向上施策
					if(rs.getString("seijo_chk")!=null){
						map.put("seijo", rs.getString("seijo_chk"));									// xxチェック（正常）
					}else{
						map.put("seijo", "0");
					}
					if(rs.getString("yochui_chk")!=null){
						map.put("yochui", rs.getString("yochui_chk"));									// xxチェック（要注意）
					}else{
						map.put("yochui", "0");
					}
					if(rs.getString("tyoka_chk")!=null){
						map.put("tyoka", rs.getString("tyoka_chk"));									// xxチェック（実質債務超過）
					}else{
						map.put("tyoka", "0");
					}
					if(rs.getString("kanwa_chk")!=null){
						map.put("kanwa", rs.getString("kanwa_chk"));									// xxチェック（弁済条件緩和）
					}else{
						map.put("kanwa", "0");
					}
					if(rs.getString("entai_chk")!=null){
						map.put("entai", rs.getString("entai_chk"));									// xxチェック（１年以上延滞チェック）
					}else{
						map.put("entai", "0");
					}
					if(rs.getString("hasanho_chk")!=null){
						map.put("hasanho", rs.getString("hasanho_chk"));								// xxチェック（破産法適用チェック）
					}else{
						map.put("hasanho", "0");
					}
					if(rs.getString("kaishaho_chk")!=null){
						map.put("kaishaho", rs.getString("kaishaho_chk"));								// xxチェック（会社法適用チェック）
					}else{
						map.put("kaishaho", "0");
					}
					if(rs.getString("koseho_chk")!=null){
						map.put("koseho", rs.getString("koseho_chk"));									// xxチェック（会社更生法適用チェック）
					}else{
						map.put("koseho", "0");
					}
					if(rs.getString("saiseho_chk")!=null){
						map.put("saiseho", rs.getString("saiseho_chk"));								// xxチェック（民事再生法適用チェック）
					}else{
						map.put("saiseho", "0");
					}
					if(rs.getString("shobun_chk")!=null){
						map.put("shobun", rs.getString("shobun_chk"));									// xxチェック（取引停止処分チェック）
					}else{
						map.put("shobun", "0");
					}
					if(rs.getString("sonota_chk")!=null){
						map.put("sonota", rs.getString("sonota_chk"));									// xxチェック（その他チェック）
					}else{
						map.put("sonota", "0");
					}																No.837 */

					// xxチェック（正常）											/* No.837 */
					map.put("seijo", rs.getString("seijo_chk"));					/* No.837 */
					// xxチェック（要注意）											/* No.837 */
					map.put("yochui", rs.getString("yochui_chk"));					/* No.837 */
					// xxチェック（実質債務超過）									/* No.837 */
					map.put("tyoka", rs.getString("tyoka_chk"));					/* No.837 */
					// xxチェック（弁済条件緩和）									/* No.837 */
					map.put("kanwa", rs.getString("kanwa_chk"));					/* No.837 */
					// xxチェック（１年以上延滞チェック）							/* No.837 */
					map.put("entai", rs.getString("entai_chk"));					/* No.837 */
					// xxチェック（破産法適用チェック）								/* No.837 */
					map.put("hasanho", rs.getString("hasanho_chk"));				/* No.837 */
					// xxチェック（会社法適用チェック）								/* No.837 */
					map.put("kaishaho", rs.getString("kaishaho_chk"));				/* No.837 */
					// xxチェック（会社更生法適用チェック）							/* No.837 */
					map.put("koseho", rs.getString("koseho_chk"));					/* No.837 */
					// xxチェック（民事再生法適用チェック）							/* No.837 */
					map.put("saiseho", rs.getString("saiseho_chk"));				/* No.837 */
					// xxチェック（取引停止処分チェック）							/* No.837 */
					map.put("shobun", rs.getString("shobun_chk"));					/* No.837 */
					// xxチェック（その他チェック）									/* No.837 */
					map.put("sonota", rs.getString("sonota_chk"));					/* No.837 */
					// 債権区分														/* No.837 */
					map.put("saiken", rs.getString("saiken_kbn"));					/* No.837 */

					if ("1".equals(map.get("hasanho")) || "1".equals(map.get("kaishaho")) || "1".equals(map.get("koseho")) 
						|| "1".equals(map.get("saiseho")) || "1".equals(map.get("shobun")) || "1".equals(map.get("sonota"))){
						map.put("torihiki_kbn", show_T);
					} else if ("1".equals(map.get("tyoka")) || "1".equals(map.get("kanwa")) || "1".equals(map.get("entai"))){
						map.put("torihiki_kbn", show_W);
					} else if ( "1".equals(map.get("yochui"))){
						map.put("torihiki_kbn", show_9);					
					} else if ("1".equals(map.get("seijo"))){
						map.put("torihiki_kbn",appContext.getMsg("label.seijo2"));	// 「正常」					
					} else{
						// なにもしない
					}
				
					if (map.get("saiken")==null || "0".equals(map.get("saiken"))) {
						map.put("saiken_kbn", " ");
					} else if("1".equals(map.get("saiken"))) {
						map.put("saiken_kbn", appContext.getMsg("label.ippan"));	// 「一般」
					} else if ("2".equals(map.get("saiken"))) {
						map.put("saiken_kbn", show_W);
					} else if ("3".equals(map.get("saiken"))) {
						map.put("saiken_kbn", show_T);
					}
					
					// No448, 2008/05/23, SJA渡辺, フェーズが70の際は、前回実施時の通貨を使用するように修正。
				    // No427, 2008/05/23, SJA 関, ハードコードの定数化(フェーズ、ステータス)
					if(GS.PHASE_HIKIATEKIN_KENSYO.equals(rs.getString("phase"))){
						map.put("tuika_hikiate", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("tuika_hikiate"))) + rs.getString("tuuka_cd70"));	// 追加引当金額
						map.put("kingaku", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("kingaku"))) + rs.getString("tuuka_cd70") );				// 金額 (引当金判定表示用)
					} else {
						map.put("tuika_hikiate", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("tuika_hikiate"))) + rs.getString("tuuka_cd"));	// 追加引当金額
						map.put("kingaku", Function.format("##,###,###,###,##0", Function.getValueOfLong(rs.getString("kingaku"))) + rs.getString("tuuka_cd") );				// 金額 (引当金判定表示用)
					}
//				}
				// 債権区分
				// map.put("saiken", rs.getString("saiken_kbn"));					/* No.837 */

				map.put("shinchoku", rs.getString("phase_nm") +"　" + rs.getString("status_nm"));	// 進捗（フェーズ・ステータス）
				map.put("honbu", rs.getString("honbu_cd"));
				
		    	///////////////////////////////////////
		    	//障害票：618
		    	//チェックイン日：2008/6/8
		    	//対応者：SJA小森
		    	//概要：部門コードと部コードが同じ場合は一緒なので一つのみ表示
		    	////////////////////////////////////////
	    		/*				
				map.put("soshiki", rs.getString("bumon_nm") + " " + rs.getString("bu_nm") + " (" + rs.getString("bu_cd") + ")");
				
				*/
	    		String bumon_nm = rs.getString("bumon_nm");
	    		String bu_nm = rs.getString("bu_nm");
	    		String bu_cd = rs.getString("bu_cd");
   			if(bu_nm != null && !bu_nm.equals(bumon_nm)){	
   				map.put("soshiki", bumon_nm + " " + bu_nm + " (" + bu_cd + ")");
   			}else{
   	    		map.put("soshiki", bumon_nm + " " + " (" + bu_cd + ")");    				
   			}
				// 障害票No618　2008/06/08 Komori End
   			
		    	///////////////////////////////////////
		    	//障害票：618
		    	//チェックイン日：2008/6/8
		    	//対応者：SJA小森
		    	//概要：部門コードと部コードが同じ場合は一緒なので一つのみ表示
		    	////////////////////////////////////////
   			/*
				if(rs.getString("bu_nm") != null){
					map.put("soshiki_syosai", Function.trim(rs.getString("bumon_nm")) + " " + Function.trim(rs.getString("bu_nm")) + " (" + rs.getString("bu_cd") + ")");
				}else{
					map.put("soshiki_syosai", Function.trim(rs.getString("bumon_nm")));
				}
				*/
   			if(bu_nm != null && !bu_nm.equals(bumon_nm)){	
					map.put("soshiki_syosai", Function.trim(bumon_nm) + " " + Function.trim(bu_nm) + " (" + bu_cd + ")");
				}else{
					// No872, 2008/06/16, SJA渡辺, 組織名称にNULLが表示されないように修正
					if (bu_cd != null) {
						map.put("soshiki_syosai", Function.trim(bumon_nm) + " " + " (" + bu_cd + ")");
					} else {
						map.put("soshiki_syosai", Function.trim(bumon_nm));
					}
				}
				// 障害票No618　2008/06/08 Komori End	
				
				
				if(rs.getString("taishogai_flg")!=null){
					map.put("taishogai", "0" + rs.getString("taishogai_flg"));
				}else{
					map.put("taishogai", "00");
				}
				map.put("ins_dt", rs.getString("ins_dt"));
				map.put("upd_dt", rs.getString("upd_dt"));
				
				
				
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
					//Resultset close
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}
	/**
	 * システムセレクトボックス値設定
	 */
	public void setSystemKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("system_kbn",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap systemKbn = new LinkedHashMap(getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    systemKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    systemKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setSystemList(systemKbn);
	
			// 障害No424 2008.05.21 uechi システム区分がクリアされるのを修正
			// 初めの値をブランクに設定
//			form.setSystemSection("");
			
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
	
	/**要件No.四-10対応
	 * 半期四半期区分セレクトボックス値設定
	 */
	public void setHanki_sihanki_Kbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("kessanki",cmnData.getComLangMode()));
		String reSql = Function.StrReplace(sql.toString(),"ASC","DESC");
		
		try {
			rs = sqlExec.execQuery(reSql);
			
		    LinkedHashMap hanki_sihanki_Kbn_List = new LinkedHashMap(getRsCount(rs));
		    	   		    
			while ( rs.next() ) {
			    
			    // 半期四半期区分に表示する項目の作成
				String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    hanki_sihanki_Kbn_List.put(kbn_hyouji_val, kbn_val );
			} // while
		    
			form.setHanki_sihanki_kbn_List(hanki_sihanki_Kbn_List);
	
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
	 * 債権セレクトボックス値設定
	 */
	public void setSaikenKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("saiken_kbn",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap saikenKbn = new LinkedHashMap(getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    saikenKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    saikenKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setSaikenList(saikenKbn);
	
			// 初めの値をブランクに設定
			form.setClaimSection("");
			
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
	 * 取引先セレクトボックス値設定
	 */
	public void setTorihikiKbn() throws SQLException {
		
		ResultSet rs = null;
		
		// No422, 2008/05/21, SJA渡辺, 正常先・要注意先同一から正常先、要注意先それぞれ単独のものへ修正
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("torihikisaki2_kbn",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap torihikiKbn = new LinkedHashMap(getRsCount(rs));
		    
		    // 先頭にブランクの項目を追加する
		    torihikiKbn.put("","");
		    
			while ( rs.next() ) {
			    
			    // 勘定科目に表示する項目の作成
			    String kbn_val = Function.trim(rs.getString("kbn_val"));
			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
			    
			    torihikiKbn.put(kbn_hyouji_val, kbn_val );
			} // while
			
			form.setToriKbnList(torihikiKbn);
	
			// 初めの値をブランクに設定
			form.setDealSection("");
			
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
	 * 処理年月リストを返します。
	 * @param list
	 * @return
	 */
	public List getSyoriYm(List list,String ym) throws SQLException{
		ResultSet rs = null;
		String year = null;
		String month = null;
		
		if (!(ym == null || ym.length() != 6)) {
			year = ym.substring(0,4);
			month = ym.substring(4,6);
		}else{
			return list;
		}
		int thisTime = Function.getValueOfInt(ym);
		
		
		StringBuffer part = new StringBuffer();
		List sateiCorpList = form.getSateiCorp();
		if (judgeCorp.equals("")) {
			part.append("SK.satei_kaisha_cd = ANY(");
			for (int i=0; i<sateiCorpList.size(); i++) {
				part.append("'" + sateiCorpList.get(i) + "'");
				if (i<sateiCorpList.size()-1) {
					part.append(", ");
				}
			}
			part.append(")");
		} else {
			part.append("SK.satei_kaisha_cd = '").append(judgeCorp).append("'");
		}
		
		//結合テスト障害対応No008
		//追加開始
		StringBuffer kbnSql = new StringBuffer();
		if (form.getSearchMode() == 0){
			kbnSql.append("SY.hanki_sihanki_kbn = '")
				  .append(hanki_sihanki_kbn)
				  .append("' and ");
		}else{
			kbnSql.append("SY.hanki_sihanki_kbn = '")
			  	  .append(HANKI)
				  .append("' and ");
		}
		//追加完了
		// No416, 2008/05/22, SJA渡辺, 最終月の値のみ検索でヒットするように修正。
		StringBuffer sql = new StringBuffer()
							.append("select distinct ")
							.append("SY.SYOKAI_ZUKI,")
							.append("SY.CYUUKAN_ZUKI,")
							.append("SY.SAISYU_ZUKI ")
							.append("from ")
							.append("SSM_SYORIZUKI SY ")
							.append(",SSM_SATEIKAISYA SK")
							.append(" where ")
							//結合テスト障害対応No008
							//追加開始
							.append(kbnSql.toString())
							//追加完了
							//結合テスト障害対応No006
							//追加開始
							//.append("SY.hanki_sihanki_kbn = '")
							//.append(hanki_sihanki_kbn)
							//.append("' and ")
							//追加完了
							.append("SY.saisyu_zuki='")
							.append(month)
							.append("' and trim(SY.KAISHA_CD) = trim(SK.KIKAN_KAISHA_CD) || '0'")
							.append(" and ").append(part.toString());
		
		try{
			rs = sqlExec.execQuery(sql.toString());
			while(rs.next()){
				int lastTime = 0;
				// 年取得
				StringBuffer result_syokai = new StringBuffer().append(year);
				result_syokai.append(rs.getString("SYOKAI_ZUKI"));
				// 年度チェック
				lastTime = Function.getValueOfInt(result_syokai.toString());
				if(lastTime > thisTime){
					// 取得した前回年月が、基準年月よりも未来になっていた場合、
					// 前回実施年度を1年戻す。
					result_syokai.replace(0,4,Function.format("####",Function.getValueOfInt(year)-1));
				}
				list.add(result_syokai.toString());
				
				StringBuffer result_tyuukan = new StringBuffer().append(year);
				result_tyuukan.append(rs.getString("CYUUKAN_ZUKI"));
				// 年度チェック
				lastTime = Function.getValueOfInt(result_tyuukan.toString());
				if(lastTime > thisTime){
					// 取得した前回年月が、基準年月よりも未来になっていた場合、
					// 前回実施年度を1年戻す。
					result_tyuukan.replace(0,4,Function.format("####",Function.getValueOfInt(year)-1));
				}
				list.add(result_tyuukan.toString());
				
				StringBuffer result_saisyu = new StringBuffer().append(year);
				result_saisyu.append(rs.getString("SAISYU_ZUKI"));
				// 年度チェック
				lastTime = Function.getValueOfInt(result_saisyu.toString());
				if(lastTime > thisTime){
					// 取得した前回年月が、基準年月よりも未来になっていた場合、
					// 前回実施年度を1年戻す。
					result_saisyu.replace(0,4,Function.format("####",Function.getValueOfInt(year)-1));
				}
				list.add(result_saisyu.toString());
			}
		}finally{
			if(rs != null){
				try{
					rs.close();
				}catch (Exception e){
					throw new SQLException(e.getMessage());
				}
			}
		}
		return list;
	}
	
	// No594, 2008/06/12, SJA渡辺, 〆区分セレクトボックス値取得メソッド追加
	/**
	 * 〆区分セレクトボックス値設定
	 */
	public void setKaisuKbn() throws SQLException {
		
		ResultSet rs = null;
		
		StringBuffer sql = new StringBuffer().append(this.getKbnSql("syori_kaisu",cmnData.getComLangMode()));
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap kaisuKbn = new LinkedHashMap(getRsCount(rs));
		    
			while ( rs.next() ) {
			    
				if (!("2".equals(Function.trim(rs.getString("kbn_val"))))) {
					// 勘定科目に表示する項目の作成
					String kbn_val = Function.trim(rs.getString("kbn_val"));
					String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
					
					kaisuKbn.put(kbn_hyouji_val, kbn_val );
				}
			} // while
			
			form.setKaisuList(kaisuKbn);
			
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
	
	// No594, 2008/06/12, SJA渡辺, 帳票セレクトボックス値取得メソッド追加
	/**
	 * 帳票セレクトボックス値設定
	 */
	public void setTyohyoKbn() throws SQLException {
		
		ResultSet rs = null;
		
		// 追加対応：No46　BS照会、BS対比票については、リスク管理部、リスク管理部/法務部担当のみ参照できるように修正
		//StringBuffer sql = new StringBuffer().append(Function.getKbnSql("tyohyo_kind",cmnData.getComLangMode()));
		String key = "tyohyo_kind";
		String langMode = cmnData.getComLangMode();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT KB.kbn_hyouji_val,");
		sql.append("KB.kbn_val");
		sql.append(" FROM SSP_KBN KB");
		sql.append(" WHERE KB.kbn_key='");
		sql.append(key);
		sql.append("' and KB.lang_mode='");
		sql.append(langMode);
		sql.append("' AND system_kbn = '01' ");
		if("1".equals(cmnData.getComNijiStKbn()) ||
		   "3".equals(cmnData.getComNijiStKbn())){
			// リスク管理部、リスク管理部/法務部担当の場合は、帳票のセレクトボックスの値すべて取得
		}else{
			// 上記以外は、BS照会、BS対比票のセレクトボックス値は取得しない
			sql.append(" and kbn_val in ('1','2','5') ");
		}
		sql.append(" ORDER BY KB.kbn_order ASC");
		try {
			rs = sqlExec.execQuery(sql.toString());
			
		    LinkedHashMap tyohyoKbn = new LinkedHashMap(getRsCount(rs));
		    int searchInt = form.getSearchMode();
		    
			while ( rs.next() ) {
			    
				if (searchInt == 0) {
					if (!("5".equals(Function.trim(rs.getString("kbn_val"))))) {
						// 勘定科目に表示する項目の作成
						String kbn_val = Function.trim(rs.getString("kbn_val"));
						String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
						
						tyohyoKbn.put(kbn_hyouji_val, kbn_val );
					}
					form.setTyohyo("1");
				} else {
					if ("5".equals(Function.trim(rs.getString("kbn_val")))) {
						// 勘定科目に表示する項目の作成
						String kbn_val = Function.trim(rs.getString("kbn_val"));
						String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
						
						tyohyoKbn.put(kbn_hyouji_val, kbn_val );
					}
					form.setTyohyo("5");
				}
			} // while
			
			form.setTyohyoList(tyohyoKbn);
			
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
	
	// No594, 2008/06/12, SJA渡辺, 最終月判定メソッド追加
	/**
	 * 最終月判定
	 */
	public boolean checkFinalMonth(String objectDate) throws SQLException {
		
		ResultSet rs = null;
		boolean result = false;
		String year = null;
		String month = null;
		
		if (!(objectDate == null || objectDate.length() != 6)) {
			year = objectDate.substring(0,4);
			month = objectDate.substring(4,6);
		}else{
			return false;
		}
		
		StringBuffer part = new StringBuffer();
		List sateiCorpList = form.getSateiCorp();
		if (judgeCorp.equals("")) {
			part.append("SK.satei_kaisha_cd = ANY(");
			for (int i=0; i<sateiCorpList.size(); i++) {
				part.append("'" + sateiCorpList.get(i) + "'");
				if (i<sateiCorpList.size()-1) {
					part.append(", ");
				}
			}
			part.append(")");
		} else {
			part.append("SK.satei_kaisha_cd = '").append(judgeCorp).append("'");
		}
		
		//結合テスト障害対応No008
		//追加開始
		StringBuffer kbnSql = new StringBuffer();
		if (form.getSearchMode() == 0){
			kbnSql.append("SY.hanki_sihanki_kbn = '")
				  .append(hanki_sihanki_kbn)
				  .append("' and ");
		}else{
			kbnSql.append("SY.hanki_sihanki_kbn = '")
			  	  .append(HANKI)
				  .append("' and ");
		}
		//追加完了
		
		StringBuffer sql = new StringBuffer()
							.append("select COUNT(distinct SY.SAISYU_ZUKI) cnt ")
							.append("from ")
							.append("SSM_SYORIZUKI SY ")
							.append(",SSM_SATEIKAISYA SK")
							.append(" where ")
							//結合テスト障害対応No008
							//追加開始
							.append(kbnSql.toString())
							//追加完了
							//結合テスト障害対応No006
							//追加開始
							//.append("SY.hanki_sihanki_kbn='")
							//.append(hanki_sihanki_kbn)
							//.append("' and ")
							//追加完了
							.append("SY.saisyu_zuki='")
							.append(month)
							.append("' and trim(SY.KAISHA_CD) = trim(SK.KIKAN_KAISHA_CD) || '0'")
							.append(" and ").append(part.toString());
		
		try {
			rs = sqlExec.execQuery(sql.toString());
			
			if (rs.next()) {
				if (rs.getInt("cnt") > 0) {
					result = true;
				}
			}
			
			return result;
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (Exception e) {
					throw new SQLException(e.getMessage());
				}
			}
		}
	}

	/**
	 * 検索条件文字列作成.
	 * <p>
	 * 検索条件文字列を作成する。指定値はリストで受け取り、
	 * リストサイズにより指定方法を変更する。<br>
	 * また、受け取ったリストが空の場合には空文字をリターンする。
	 * 
	 * @param sParamStr 検索条件として指定する、項目名称
	 * @param oDataList 条件として指定する値
	 * @param bEquals イコール条件か否かを示すフラグ。<br>
	 * true  の場合には、 " = " もしくは " IN "を指定する。<br>
	 * false の場合には、 " <> " もしくは " NOT IN " を指定する。
	 * @param bAddStart 文字列の先頭に「AND」を指定するか否かを示すフラグ。
	 * @param bAddEnd 文字列の末尾に「AND」を指定するか否かを示すフラグ。
	 * @return 条件文字列。但し、oDataListが空の場合には空文字を返す。
	 */
	private String createConditionStr(String sParamStr, List oDataList, 
				boolean bEquals, boolean bAddStart, boolean bAddEnd) {		/* No.837 */

		int iDataListSize = oDataList.size();								/* No.837 */
		if (iDataListSize == 0) {											/* No.837 */
			return "";														/* No.837 */
		}																	/* No.837 */

		// 編集後文字列を格納するためのバッファ								/* No.837 */
		StringBuffer oRetStrBuf = new StringBuffer();						/* No.837 */

		if (bAddStart) {													/* No.837 */
			oRetStrBuf.append(" AND ");										/* No.837 */
		}																	/* No.837 */

		// データサイズにより、作成する文字列を変更する						/* No.837 */
		if (iDataListSize == 1) {											/* No.837 */
			String sCheckStr = bEquals ? " = '" : " <> '";					/* No.837 */
			oRetStrBuf.append(sParamStr).append(sCheckStr)					/* No.837 */
					  .append(oDataList.get(0)).append("' ");				/* No.837 */
		} else {															/* No.837 */
			String sCheckStr = bEquals ? " IN (" : " NOT IN (";				/* No.837 */
			oRetStrBuf.append(sParamStr).append(sCheckStr);					/* No.837 */
			for (int i = 0; i < iDataListSize; i++) {						/* No.837 */
				oRetStrBuf.append("'").append(oDataList.get(i)).append("'");/* No.837 */
				if (i < iDataListSize - 1)									/* No.837 */
					oRetStrBuf.append(", ");								/* No.837 */
			}																/* No.837 */
			oRetStrBuf.append(") ");										/* No.837 */
		}																	/* No.837 */

		if (bAddEnd) {														/* No.837 */
			oRetStrBuf.append(" AND ");										/* No.837 */
		}																	/* No.837 */

		return oRetStrBuf.toString();										/* No.837 */
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
											.append("' and KB.system_kbn ='01'")
											.append(" ORDER BY KB.kbn_order ASC");
		
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