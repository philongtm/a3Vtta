/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2008/09/16      中野            要件No.四-10 半期四半期区分プルダウン追加による修正
002     2009/12/14      春日            課題No.198 件数チェックの削除
******************************************************************************/
package app.syokaiZen.bss;

import app.SessionDataZen;
import app.syokaiZen.dbAcc.SateiDbAcc;
import app.syokaiZen.form.SateiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Function;
import common.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 滞留判定対象先一覧画面ビジネスロジッククラス
 */
public class SateiBss {

    /** クラス名 */
	private String CLASSNAME = getClass().getName();
	
    /** ＡＰＰコンテキスト */
	private AppContext appContext = null;
	/** ＤＢアクセス */
	private SqlExecuter sqlExec = null;
	/** ログ */
	private Log log = null;

	/** 共通セッション */
	private SessionDataZen cmnData;
	/** データ格納フォーム */
	private SateiForm form;
	
	/** 年月リストの内要素数 */
	private static final int maxYmList	= 3; 
	/** 最大検索結果数 */
	private static final int maxCount	= 1000;
	/** 所属国リスト内先頭に配置する所属国名称 */
	private static final String japan = "JAPAN"; 
	
	/**
	 * コンストラクタ
	 */
	public SateiBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZenRe();
		form = (SateiForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得
	 */
	public String executeInit() throws Exception {

	    // 各プルダウンメニュー内の内容を作成する。
		executeSelectParam();
	    
		// 画面内注意文の取得・フォームへ格納
		form.setInfoMsg(appContext.getMsg("msg.0017"));
		
        // No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
		appContext.setFocusField("baseDate");
		
		return GS.RC_OK;
	}
	
	/**
	 * 各プルダウンメニュー内の内容作成
	 */
	public void executeSelectParam() throws Exception {
	    
	    // TODO 設計書にあわせ決めうちでリストを作成してます。

    	///////////////////////////////////////
    	//障害票：826
    	//チェックイン日：2008/6/15
    	//対応者：SJA小森
    	//概要：ソート順セレクトボックスの要素を区分テーブルから取得するようにする。
    	////////////////////////////////////////
		//修正戻し 2008/7/23 元平
	
		LinkedHashMap sortMap = new LinkedHashMap();
		LinkedHashMap toriKbnMap = new LinkedHashMap();

		// 検索条件にあわせ、ソート順リストの作成

		int searchInt = form.getSearchMode();
		if (searchInt==0) {
			sortMap.put(appContext.getMsg("label.cust_cd"), "0");		// 勘定先CD
			sortMap.put(appContext.getMsg("label.cust_nm"), "1");		// 勘定先名称
			sortMap.put(appContext.getMsg("label.month"), "2");			// 対象年月
			sortMap.put(appContext.getMsg("label.cust_category"), "3");	// 債権区分
			sortMap.put(appContext.getMsg("label.cred_category"), "4");	// 取引先区分
			//要件No.四-10
			//追加開始
			sortMap.put(appContext.getMsg("label.bumon"), "5");		    // 部門
			sortMap.put(appContext.getMsg("label.total_amount"), "6");	// 総債権残
			sortMap.put(appContext.getMsg("label.reserve"), "7");		// 追加引当金
			sortMap.put(appContext.getMsg("label.progress"), "8");		// 進捗
			//追加完了
			//sortMap.put(appContext.getMsg("label.total_amount"), "5");	// 総債権残
			//sortMap.put(appContext.getMsg("label.reserve"), "6");		// 引当金額
			//sortMap.put(appContext.getMsg("label.progress"), "7");		// 進捗
		} else {
			sortMap.put(appContext.getMsg("label.cust_cd"), "0");		// 勘定先CD
			sortMap.put(appContext.getMsg("label.cust_nm"), "1");		// 勘定先名称
			sortMap.put(appContext.getMsg("label.month"), "2");			// 対象年月
			sortMap.put(appContext.getMsg("label.dept"), "3");			// 組織
			sortMap.put(appContext.getMsg("label.a_ttl"), "4");			// 金額計
			sortMap.put(appContext.getMsg("label.progress"), "5");		// 進捗
		}
		form.setSortList(sortMap);
	
		// 障害票No826　2008/06/15 Komori End	
	
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
    	///////////////////////////////////////
    	//障害票：826
    	//チェックイン日：2008/6/15
    	//対応者：SJA小森
    	//概要：ソート順セレクトボックスの要素を区分テーブルから取得するようにする。
    	////////////////////////////////////////
		//修正戻し 2008/7/23　元平
//		String sortPattern = "SORT_PATTERN_03";
//		int searchInt = form.getSearchMode();
//		if (0 != form.getSearchMode()) {
//			sortPattern = "SORT_PATTERN_04";
//		}	
//		setSortKbn(sortPattern);
		// 障害票No826　2008/06/15 Komori End		
		
		//DBから他プルダウンメニュー用情報を取得する
		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
		dbacc.setTorihikiKbn();
		dbacc.setShowKbn();
		// TODO
		dbacc.setKaisuKbn();
		dbacc.setTyohyoKbn();
		
		dbacc.executeSelectParam(appContext);
		
		LinkedHashMap countryMap = form.getCountryMap();
		LinkedHashMap sortCountryMap = new LinkedHashMap();
		Set set = countryMap.keySet();
		String key = japan;
		String value;
		Iterator iterator = set.iterator();
		
		while(iterator.hasNext()){
		   value = (String) countryMap.get(key);
		   
		   sortCountryMap.put(key, value);
		   key = (String) iterator.next();
		}
		
		form.setCountryMap(sortCountryMap);

		dbacc.setSystemKbn();
		dbacc.setSaikenKbn();
		//要件No.四-10対応 半期四半期区分プルダウン値取得
		//追加開始
		dbacc.setHanki_sihanki_Kbn();
		//追加完了

		// 障害票：814　2008/6/11　SJA中島　部門名引継ぎ処理追加
		dbacc.sqlExecBumonForPullDown(form.getJudgeCorp());
		
		// 障害No424 2008.05.21 uechi 部名称を引継ぎ
		dbacc.executeBu(appContext);
		
		// コネクションの開放
		appContext.destroy();
		
	}
	///////////////////////////////////////
	//障害票：826
	//チェックイン日：2008/6/15
	//対応者：SJA小森
	//概要：ソート順セレクトボックスの要素を区分テーブルから取得するようにする。
	////////////////////////////////////////
	//修正戻し 2008/7/23 元平
//	/**
//	 * ソート順セレクトボックス値設定
//	 */
//	public void setSortKbn(String screenid) throws SQLException {
//		
//		ResultSet rs = null;
//		String sql = Function.getKbnSql(screenid, cmnData.getComLangMode());
//	    String sortKey = "";
//		try {
//			rs = sqlExec.execQuery(sql);
//		    LinkedHashMap sortMap = new LinkedHashMap();
//			if ( rs.next() ) {
//				sortKey = Function.trim(rs.getString("kbn_val"));
//			} 
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (Exception e) {
//					throw new SQLException(e.getMessage());
//				}
//			}
//		}		
//
//		rs = null;
//		sql = Function.getKbnSql(sortKey,cmnData.getComLangMode());
//		try {
//			rs = sqlExec.execQuery(sql);
//		    LinkedHashMap sortMap = new LinkedHashMap();
//			while ( rs.next() ) {
//			    String kbn_val = Function.trim(rs.getString("kbn_val"));
//			    String kbn_hyouji_val = Function.trim(rs.getString("kbn_hyouji_val"));
//			    sortMap.put(kbn_hyouji_val, kbn_val );
//			} // while
//			form.setSortList(sortMap);
//		} finally {
//			if (rs != null) {
//				try {
//					rs.close();
//				} catch (Exception e) {
//					throw new SQLException(e.getMessage());
//				}
//			}
//		}
//	}	
	// 障害票No826　2008/06/15 Komori End

	// 障害票：814　2008/6/11　SJA中島　査定会社選択時に、選択した査定会社に紐付く部門一覧を取得する
	/**
	 * 査定会社名選択時の、表示部名の絞込み処理
	 */
	public String executeSateiCorp() throws Exception {
		//コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();

	    form.setBu(new LinkedHashMap());
	    form.setBuCode("");

		// 部門名に空白が選択された場合、部名プルダウンメニューを削除する
		if (GS.EMPTY_CHARCTER.equals(form.getJudgeCorp())) {
		    form.setBumon(new LinkedHashMap());
		    form.setBumonCode("");
		    return GS.RC_OK;
		}
		
		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
		
		// DBから選択部門名配下の部名を取得する
		dbacc.sqlExecBumonForPullDown(form.getJudgeCorp());
		
		// コネクションの開放
		appContext.destroy();
		
		return GS.RC_OK;
		
	}
	
	/**
	 * 部門名選択時の、表示部名の絞込み処理
	 */
	public String executeBumon() throws Exception {
		//コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// 部門名に空白が選択された場合、部名プルダウンメニューを削除する
		if (form.getBumonCode().equals("")) {
		    form.setBu(new LinkedHashMap());
		    form.setBuCode("");
		    return GS.RC_OK;
		}
		
		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
		
		// DBから選択部門名配下の部名を取得する
		dbacc.executeBu(appContext);
		
		// コネクションの開放
		appContext.destroy();
		
		return GS.RC_OK;
		
	}
	
	/**
	 * 対象先検索
	 */
	public String execute() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		int searchMode = form.getSearchMode();
		int errorFlag = 0;

		
		// 入力チェック、必須条件パターンが入力されていない場合リロードしエラーを表示
		//(1) 基準年月・勘定先コード・システム区分
    	//if (!form.getBaseDate().trim().equals("") && !form.getKanjoCd().trim().equals("") && !form.getSystemSection().equals(""))  
    		//errorFlag++;
    	//(2) 基準年月・所属国・企業名
    	//if (!form.getBaseDate().trim().equals("") && !form.getCountry().equals("") && !form.getKanjoName().trim().equals("")) 
    		//errorFlag++;
    	//(3) 基準年月・会社
    	//if (!form.getBaseDate().trim().equals("") && !form.getJudgeCorp().equals("")) 
    		//errorFlag++;
    		
		//if (errorFlag == 0) {
 			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
 			//appContext.setFocusField("baseDate");
    		//appContext.setMsgCode(GL.W_INPUT_0001);
			//return GS.RC_RELOAD;
		//}
		
		int count;													
		List dateList = new ArrayList();							//検索用日時の格納
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");		//検索用日時を使用するフォーマット
		
		//検索用基準年月から、検索用年月のリストを作成する。
		String baseDate = Function.trim(form.getBaseDate());
		if (baseDate.equals("")) {
	    	try {
//	    		for (count=0; count<=maxYmList-1 ;count++) {
//	    			dateList.add(Function.calcYmNngetsu(baseDate,-count));
//	    		}    		
	    		// DBから対象処理年月を取得する
	    		// DBから画面表示する値を取得し、セッションに格納
	    		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
	    		dateList = dbacc.getSyoriYm(dateList,baseDate);
	    	} catch (Exception e){
	    		dateList.clear();
	    	}
		}
		
	    //検索用年月日リストを格納する
	    form.setDateList(dateList);
		
		// DBから画面表示する値を取得し、セッションに格納
		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
		// 検索モードにあわせ、処理を変更
		if (searchMode==0)
			dbacc.executeSatei();
		else if (searchMode==1)
			dbacc.executeTairyu();
		
		
		// 課題No.198
		// 追加開始
		/*
		//検索結果が1000件を超えた場合、終了しエラーを表示する
		if (form.getCnt_meisai()>maxCount) {
    		appContext.setMsgCode("err.0091");
    		return GS.RC_RELOAD;
		}
		// 追加完了
		*/
		
		
		// 取得したデータから、画面表示用の文字を判定しマップに格納する。
		// 取引区分・債権区分・事由区分　それぞれ設計書に合わせ決めうちで各文字を格納しています。
		List meisaiList = form.getAr_meisai();
		for (int x=0; x<meisaiList.size(); x++) {
			Map map = (HashMap) meisaiList.get(x);
			
			if (searchMode==0){
			
				if (map.get("hasanho").equals("1") || map.get("kaishaho").equals("1") || map.get("koseho").equals("1") 
					|| map.get("saiseho").equals("1") || map.get("shobun").equals("1") || map.get("sonota").equals("1")){
					map.put("torihiki_kbn", dbacc.getHyoujiVal("ktk","T"));
				} else if (map.get("tyoka").equals("1") || map.get("kanwa").equals("1") || map.get("entai").equals("1")){
					map.put("torihiki_kbn",dbacc.getHyoujiVal("ktk","W"));
				} else if ( map.get("yochui").equals("1")){
					map.put("torihiki_kbn", dbacc.getHyoujiVal("ktk","9"));					
				} else if (map.get("seijo").equals("1")){
					map.put("torihiki_kbn",appContext.getMsg("label.seijo2"));	// 「正常」					
				} else{
					// なにもしない
				}
	
				if (map.get("saiken")==null || map.get("saiken").equals("0"))
				    map.put("saiken_kbn", " ");
				else if(map.get("saiken").equals("1"))
					map.put("saiken_kbn", appContext.getMsg("label.ippan"));	// 「一般」
				else if (map.get("saiken").equals("2"))
					map.put("saiken_kbn", dbacc.getHyoujiVal("ktk","W"));
				else if (map.get("saiken").equals("3"))
					map.put("saiken_kbn", dbacc.getHyoujiVal("ktk","T"));

				
				if (map.get("jiyuu9")!=null)
					map.put("jiyuu", appContext.getMsg("label.shitei"));		// 「リ企・指定」
				else if (map.get("jiyuu8")!=null || map.get("jiyuu7")!=null)
					map.put("jiyuu", appContext.getMsg("teikaku"));				// 「低格付」
				else if (map.get("jiyuu6")!=null)
					map.put("jiyuu", appContext.getMsg("youtyuui2"));			// 「要注意」
				else if (map.get("jiyuu5")!=null)
					map.put("jiyuu", appContext.getMsg("label.m12"));			// 「M12」
				else if (map.get("jiyuu4")!=null)
					map.put("jiyuu", appContext.getMsg("label.akaji"));			// 「赤字」
				else if (map.get("jiyuu3")!=null)
					map.put("jiyuu", appContext.getMsg("label.saimuchoka"));	// 「債務超過」
				else if (map.get("jiyuu2")!=null)
					map.put("jiyuu", appContext.getMsg("label.kashitaore2"));	// 「貸倒懸念」
				else if (map.get("jiyuu1")!=null)
					map.put("jiyuu", appContext.getMsg("label.hasan_kosei2"));	// 「破産更生」

			}
			meisaiList.set(x, map);
		}
		form.setAr_meisai(meisaiList);

		// コネクションの開放
		appContext.destroy();
		
		return GS.OS8101;
	}
	
	// No594, 2008/06/12, SJA渡辺, ダウンロードチェック追加
	/**
	 * ダウンロードチェックメソッド
	 */
	public boolean downloadCheck() throws Exception {
		
		boolean result = false;
		
		this.sqlExec = appContext.getSqlExecuter();
		
		String tyohyo = form.getTyohyo();	// 帳票種別
		String kaisu = form.getKaisu();		// 〆区分
		String objectDate = Function.trim(form.getObjectDate());	// 対象年月
		
		SateiDbAcc dbacc = new SateiDbAcc(sqlExec, log, appContext);
		boolean monthChk = dbacc.checkFinalMonth(objectDate);
		
		// 期末か判定
		if (monthChk) {
			// No594, 2008/06/13, SJA渡辺, 最終年月での滞留判定明細のチェック追加
			if (!("1".equals(tyohyo) || "5".equals(tyohyo))) {
				if ("".equals(kaisu)) {
					appContext.setMsgCode("err.0114");
					appContext.setFocusField("kaisu");
				} else {
					result = true;
				}
			} else {
				// 〆区分をクリア
				form.setKaisu("");
				result = true;
			}
		} else {
			// 〆区分をクリア
			form.setKaisu("");
			// 引当金額集計表(検証結果)はダウンロード不可
			if (!("2".equals(tyohyo))) {
				result = true;
			// No861, 2008/06/20, SJA渡辺, 期末で無い場合、期末帳票選択時エラーメッセージ表示
			} else {
				appContext.setMsgCode("err.0116");
				appContext.setFocusField("objectDate");
			}
		}
		
		appContext.destroy();
		
		return result;
	}
	
}