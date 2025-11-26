/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/5/19		SSC				1.5次版機能組込
002		2009/11/12		SSC				課題No.99 一覧表示修正
003     2009/12/17		SSC(坂本)		課題No.205 査定先金額条件の変更に伴う
										入力チェックの追加
******************************************************************************/
package app.system.bss;

import app.SessionDataZen;
import app.system.dbAcc.CyusyutujyokenHqDbAcc;
import app.system.form.CyusyutujyokenHqForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 抽出条件メンテナンス画面ビジネスロジッククラス
 */
public class CyusyutujyokenHqBss {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// ＬＯＧ

	private SessionDataZen cmnData;				// 共通セッション
	private CyusyutujyokenHqForm form;
	
	private static final String ERR_SATEIKAISYA_CD_205 = "satei_kaisya_cd";	//課題対応No.205
	private static final String ERR_MISE_CD_205 = "mise_cd";					//課題対応No.205
	/**
	 * コンストラクタ
	 */
	public CyusyutujyokenHqBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMNZen();
		this.form = (CyusyutujyokenHqForm)appContext.getActionForm();
	}
	
	/**
	 * 画面表示項目検索処理
	 * 
	 * @return GS.RC_OK
	 * @throws Exception
	 */
	public String execute() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
		
		// 区分情報の設定
	    form.setAr_system_kbn(dbacc.selectKbnMap("gss_system_kbn", "00"));	// システム区分
	    form.setAr_syori_kbn(dbacc.selectKbnMap("syori_kbn", "00"));	// 処理区分
	    form.setAr_tairyu_jdg(dbacc.selectKbnMap("tairyu_jdg", "0"));	// 滞留判定
		//要件No.四-13　決算期区分セレクトボックス設定値取得
		//追加開始
	    form.setKessanki_kbn_list(dbacc.selectKbnMap("kessanki", null));	//決算期区分
	    //追加完了
	    
	    // No566, 2008/06/14, SJA渡辺, 店セレクトボックスに初期値を設定するように修正
	    LinkedHashMap miseKbn = new LinkedHashMap();
	    miseKbn.put("","");
	    form.setAr_mise_cd(miseKbn);
	    
	    dbacc.setKakutukeKbn();
	    dbacc.setSateiKaisya();
	    //form.setAr_mise_cd(new LinkedHashMap());
	    
		// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    // No790, 2008/06/12, SJA遠藤, セレクトボックスに初期フォーカスを当てない
		appContext.setFocusField("");
		
		return GS.OS7114;
	}

	/**
	 * 検討対象先情報および査定対象先情報の取得処理
	 * 
	 * @throws Exception
	 */
	public String selectSyosaiList() throws Exception {
		
		String systemKbn = form.getSystemKbn();
		String syoriKbn = form.getSyoriKbn();
		
		System.out.println(systemKbn);
		
		if (!Function.trim(systemKbn).equals("00")) {
			// コネクションの取得
			this.sqlExec = appContext.getSqlExecuter();
				
			CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
			
			if (!Function.trim(syoriKbn).equals("00")) {
				// 検討対象先情報の取得
				dbacc.selectKentouTaisyouList();
			}
			// 査定対象先情報の取得
			dbacc.selectSateiTaisyouList();
			
		}
		
		return GS.OS7114;
	}
	
	/**
	 * 検討対象先登録処理
	 * 
	 * @throws Exception
	 */
	public void torokuKentouTaisyou() throws Exception {
		boolean result = false;
		boolean isNew = false;

		InputCheck check = new InputCheck();

		if ("1".equals(form.getType())) {
			isNew = true;
			
			// 共通項目選択チェック
			if ("00".equals(form.getSystemKbn())) {
				appContext.setMsgCode("err.0079");		// システム未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("systemKbn");
				return;
			}
			if ("00".equals(form.getSyoriKbn())) {
				appContext.setMsgCode("err.0080");		// 処理区分未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("syoriKbn");
				return;
			}
//			No878, 2008/06/18, SJA平林, 査定会社、店コードをそれぞれ選択しないで登録ボタンを押下すると、DBアクセスエラーとなるため、チェック追加		
			if ("".equals(form.getSatei_kaisya_cd())) {
				appContext.setMsgCode("err.0118");		// 査定会社未選択
	    		appContext.setFocusField("satei_kaisya_cd");
				return;
			}
			if ("".equals(form.getMise_cd())) {
				appContext.setMsgCode("err.0119");		// 店コード未選択
	    		appContext.setFocusField("mise_cd");
				return;
			}
		}
		
		// 抽出事由入力チェック
		String jiyuu_cd = form.getJiyuu_cd();
		if (check.isNullBlank(jiyuu_cd)) {
			if (isNew) {
				appContext.setMsgCode("err.0023");		// 抽出事由未入力
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("jiyuu_cd");
				
			}
	    	//Bug272対応時検出Bug Komori Michio 2008/05/13 Start 
			else{
				appContext.setMsgCode("err.0027");		// 抽出事由未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("jiyuu_cd");
			}
	    	//Bug272対応時検出Bug Komori Michio 2008/05/13 End		
			return;
		} else if (check.islength(jiyuu_cd, 1)) {
			form.setJiyuu_cd("0" + jiyuu_cd);
		}
		
		// 条件名称チェック
		int len = 40;
	    if ( GS.LANG_EN.equals(cmnData.getComLangMode()) ){
	    	len = 60;
	    }
    	if (!check.islength(form.getJiyuu_nm(), len)) {
			appContext.setMsgCode("err.0087");			// 条件名称長さ不正
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("jiyuu_nm");
			return;
    	}
		
		// 滞在期間チェック
		if ((!check.isNullBlank(form.getTairyu_from()) && 
				!check.isNumeric(form.getTairyu_from()))) {
			appContext.setMsgCode("err.0086");			// 滞在期間半角数字以外
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tairyu_from");
			return;
		}
				
		if ((!check.isNullBlank(form.getTairyu_to()) && 
				!check.isNumeric(form.getTairyu_to()))) {
			appContext.setMsgCode("err.0086");			// 滞在期間半角数字以外
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tairyu_to");
			return;
		}
		
		// 通貨チェック
		if (!check.islength(form.getTuuka_kentou(), 3)) {
			appContext.setMsgCode("err.0088");			// 滞在期間長さ不正
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tuuka_kentou");
			return;
		}
		
		// 金額チェック
		if (!checkKingaku(check, Function.removeComma(form.getKingaku_kentou()))) {
			appContext.setMsgCode("err.0022");			// 金額不正
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("kingaku_kentou");
			return;
		}
		
		// No484, 2008/05/29, SJA渡辺, 入力禁止文字存在チェック追加
    	if (checkChar(check,form.getJiyuu_nm())) { 
    		appContext.setMsgCode("err.0109");
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("jiyuu_nm");
			return;
		}
    	if (checkChar(check,form.getTairyu_from())) {
    		appContext.setMsgCode("err.0109");
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tairyu_from");
			return;
		}	
    	if (checkChar(check,form.getTairyu_to())) {
    		appContext.setMsgCode("err.0109");
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tairyu_to");
			return;
		}
    	if (checkChar(check,form.getTuuka_kentou())) {
    		appContext.setMsgCode("err.0109");
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tuuka_kentou");
    		return;
    	}
    	
    	//要件No.四-13　追加抽出条件項目の入力チェック
    	//追加開始
    	//過去格付From
		//障害No.0002
		//追加開始　条件に空文字判定を追加
    	if(!(Function.trim(form.getKakoKtkFrom()).equals(GS.EMPTY_CHARCTER)) && !(Function.isInt(form.getKakoKtkFrom()))){
    	//追加完了
    		appContext.setMsgCode("err.0127");
    		appContext.setFocusField("kakoKtkFrom");
    		//障害No.0001
    		//追加開始
			return;
    		//追加完了
    	}
    	//過去格付To
		//障害No.0002
		//追加開始　条件に空文字判定を追加
    	if(!(Function.trim(form.getKakoKtkTo()).equals(GS.EMPTY_CHARCTER)) && !(Function.isInt(form.getKakoKtkTo()))){
        //追加完了
    		appContext.setMsgCode("err.0127");
    		appContext.setFocusField("kakoKtkTo");
			return;
    	}
    	//過去格付参照時点
		//障害No.0002
		//追加開始　条件に空文字判定を追加
    	if(!(Function.trim(form.getKakoKtkSansyo()).equals(GS.EMPTY_CHARCTER)) && !(Function.isInt(form.getKakoKtkSansyo()))){
        //追加完了
    		appContext.setMsgCode("err.0128");
    		appContext.setFocusField("kakoKtkSansyo");
			return;
    	}
		//現在固定化営業債権額下限
		if (!checkKingaku(check, Function.removeComma(form.getGenzaiKoteiSaikengakuKagen()))) {
			appContext.setMsgCode("err.0129");
    		appContext.setFocusField("genzaiKoteiSaikengakuKagen");
			return;
		}
		//現在固定化営業債権額上限
		if (!checkKingaku(check, Function.removeComma(form.getGenzaiKoteiSaikengakuJyogen()))) {
			appContext.setMsgCode("err.0129");
    		appContext.setFocusField("genzaiKoteiSaikengakuJyogen");
			return;
		}
		//過去固定化営業債権額下限
		if (!checkKingaku(check, Function.removeComma(form.getKakoKoteiSaikengakuKagen()))) {
			appContext.setMsgCode("err.0130");
    		appContext.setFocusField("kakoKoteiSaikengakuKagen");
			return;
		}
		//過去固定化営業債権額上限
		if (!checkKingaku(check, Function.removeComma(form.getKakoKoteiSaikengakuJyogen()))) {
			appContext.setMsgCode("err.0130");
    		appContext.setFocusField("kakoKoteiSaikengakuJyogen");
			return;
		}
    	//過去格付参照時点
		//障害No.0002
        //追加開始
    	if(!(Function.trim(form.getKoteiSaikengakuSansyo()).equals(GS.EMPTY_CHARCTER)) && !(Function.isInt(form.getKoteiSaikengakuSansyo()))){
        //追加完了
    		appContext.setMsgCode("err.0131");
    		appContext.setFocusField("koteiSaikengakuSansyo");
			return;
    	}
    	//追加完了

    	//要件No.四-13　追加抽出条件項目の組み合わせチェック
    	//追加開始
    	if(checkKumiawase()){
    		appContext.setMsgCode("err.0132");
    		appContext.setFocusField("kakoKtkFlg");
			return;
    	}
    	//追加完了
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
		
		if (isNew) {
			// 存在チェック
			if (dbacc.checkKentouTaisyou()) {
				appContext.setMsgCode("err.0024");		// 登録済
				// No554, 2008/06/06, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("jiyuu_cd");
				return;
			}
			// 新規登録
			result = dbacc.insertKentouTaisyou();
		} else {
			// 更新登録
			result = dbacc.updateKentouTaisyou();
		}
			
		if (result) {
			// 査定対象先情報の再取得
			dbacc.selectKentouTaisyouList();
			
			// No795, 2008/06/07, SJA渡辺, 更新した事由コードの債権フラグ設定を再設定するように修正。
			String tmp_jiyuu_cd = form.getJiyuu_cd();
			List list = form.getKentouList();
			Iterator itr = list.iterator();
			while (itr.hasNext()) {
				HashMap map = (HashMap)itr.next();
				if (tmp_jiyuu_cd.equals((String)map.get("jiyuu_cd"))) {
					form.setSaiken_data_flg((String[])((ArrayList)map.get("data_flg_list")).toArray(new String[4]));
					form.setSaiken_kentou_flg((String[])((ArrayList)map.get("kentou_flg_list")).toArray(new String[4]));
					form.setSaiken_tairyu_flg((String[])((ArrayList)map.get("tairyu_flg_list")).toArray(new String[4]));
					form.setSaiken_all_flg((String[])((ArrayList)map.get("saiken_flg_list")).toArray(new String[4])); 
					form.setNo_kentou_flg((String[])((ArrayList)map.get("jyouken_flg_list")).toArray(new String[4]));
					break;
				}
			}
		} else {
			throw new SQLException();
		    //appContext.setMsgCode("err.system.dbacc"); 		// 登録失敗
		}
	}
	
	/**
	 * 検討対象先削除処理
	 * 
	 * @throws Exception
	 */
	public void sakujoKentouTaisyou() throws Exception {
		boolean result = false;

		InputCheck check = new InputCheck();
		
		// 管理票No200808070005, 2008/08/07, SJA平道, 抽出事由の入力チェックはJSPで行う
//		// 抽出事由入力チェック
//		if (check.isNullBlank(form.getJiyuu_cd())) {
//			appContext.setMsgCode("err.0023");			// 抽出事由未入力
//			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
//    		appContext.setFocusField("jiyuu_cd");
//			return;
//		}
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
		
		// 削除処理
		if (dbacc.deleteKentouTaisyou()) {
			// 査定対象先情報の再取得
			dbacc.selectKentouTaisyouList();
		} else {
		    throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc"); 	// 削除失敗
		}
	}
	
	/**
	 * 査定対象先登録処理
	 * 
	 * @throws Exception
	 */
	public void torokuSateiTaisyou() throws Exception {
		boolean result = false;
		boolean isNew = false;
		
		InputCheck check = new InputCheck();
		
		if ("1".equals(form.getType())) {
			isNew = true;
			
			// 共通項目選択チェック
			if ("00".equals(form.getSystemKbn())) {
				appContext.setMsgCode("err.0079");		// システム未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("systemKbn");
				return;
			}
		}
		/***********************************************************/
		/**課題No.205　査定先金額条件の変更                        */
		/**対応内容　査定会社コード・店コード選択項目チェックを追加*/
		/**対応日:2009/12/17　                                   　*/
		/**対応者:SSC(坂本)　　　　　　　　　　　　　　　　        */
		/***********************************************************/
		if (GS.EMPTY_CHARCTER.equals(form.getSatei_kaisya_cd())) {
			isNew = true;
			
			// 査定会社項目選択チェック
			if (GS.EMPTY_CHARCTER.equals(form.getSatei_kaisya_cd())) {
				appContext.setMsgCode(GL.ERR_SELECT, GL.LABEL_SATEI_CO);		// 査定会社コード未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField(ERR_SATEIKAISYA_CD_205);
				return;
			}
		}
		
		if (GS.EMPTY_CHARCTER.equals(form.getMise_cd())) {
			isNew = true;
			
			// 店コード項目選択チェック
			if (GS.EMPTY_CHARCTER.equals(form.getMise_cd())) {
				appContext.setMsgCode(GL.ERR_SELECT, GL.LABEL_MISE_CD);		// 店コード未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField(ERR_MISE_CD_205);
				return;
			}
		}
		/**課題No.205対応ここまで*/
		
		
		// 滞留判定入力チェック
		// 管理票No200808070007, 2008/08/07, SJA平道, 更新時も未選択の場合にメッセージを出力する
		if ("0".equals(form.getTairyu_hantei())) {
			//if (isNew) {
				// No537, 2008/06/05, SJA渡辺, 重複する、または、使用されていないプロパティ値削除による修正
				appContext.setMsgCode("err.0002");		// 滞留判定未選択
				// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
	    		appContext.setFocusField("tairyu_hantei");
			//}
			return;
		}
		
		// 通貨チェック
		if (!check.islength(form.getTuuka_satei(), 3)) {
			appContext.setMsgCode("err.0088");			// 滞在期間長さ不正
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tuuka_satei");
			return;
		}

		// 金額チェック
		if (!checkKingaku(check, Function.removeComma(form.getKingaku_satei()))) {
			appContext.setMsgCode("err.0022");			// 金額不正
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("kingaku_satei");
			return;
		}
		
		// No484, 2008/05/29, SJA渡辺, 入力禁止文字存在チェック追加
    	if (checkChar(check,form.getTuuka_satei())) {
    		appContext.setMsgCode("err.0109");
			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
    		appContext.setFocusField("tuuka_satei");
    		return;
    	}
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
			
		try {
			if (isNew) {
				// 存在チェック
				if (dbacc.checkSateiTaisyou()) {
					appContext.setMsgCode("err.0025");			// 登録済
					// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
		    		appContext.setFocusField("jiyuu_cd");
					return;
				}
				// 新規登録
				result = dbacc.insertSateiTaisyou();
			} else {
				// 更新登録
				result = dbacc.updateSateiTaisyou();
			}
				
			if (result) {
				// 査定対象先情報の再取得
				dbacc.selectSateiTaisyouList();
			} else {
			    throw new SQLException();
				//appContext.setMsgCode("err.system.dbacc"); 		// 登録失敗
			}
		} finally {
			// コネクションの開放
			appContext.destroy();
		}
	}
	
	/**
	 * 査定対象先削除処理
	 * 
	 * @throws Exception
	 */
	public void sakujoSateiTaisyou() throws Exception {
		// 管理票No200808070005, 2008/08/07, SJA平道, 滞留判定の入力チェックはJSPで行う
//		// 滞留判定入力チェック
//		if ("0".equals(form.getTairyu_hantei())) {
//			// No537, 2008/06/05, SJA渡辺, 重複する、または、使用されていないプロパティ値削除による修正
//			appContext.setMsgCode("err.0002");				// 滞留判定未選択
//			// No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加
//    		appContext.setFocusField("tairyu_hantei");
//			return;
//		}
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
			
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
		if (dbacc.deleteSateiTaisyou()) {
			// 査定対象先情報の再取得
			dbacc.selectSateiTaisyouList();
		} else {
		    throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc"); 		// 削除失敗
		}
	}

	/**
	 * 査定会社に紐付く店コード検索処理
	 * 
	 * @return GS.RC_OK
	 * @throws Exception
	 */
	public String executeSateiKaisya() throws Exception {	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		CyusyutujyokenHqDbAcc dbacc = new CyusyutujyokenHqDbAcc(sqlExec, log, appContext);
		
		dbacc.setMiseCd(form.getSatei_kaisya_cd());
        
		return GS.OS7114;
	}
	
	
	/**
	 * 金額入力値のチェック
	 * 
	 * @param check 入力チェックオブジェクト
	 * @param kingakuStr 金額の文字列
	 * @return 正常な値の場合true、不正な値の場合false 
	 * @throws Exception
	 */
	private boolean checkKingaku(InputCheck check, String kingaku) throws Exception {
		
		if (!(check.isNullBlank(kingaku))) {
			// 数値チェック
			if (!(Function.isDouble(kingaku))) {
				return false;
			}
			// 入力lengthチェック
			int idx = kingaku.indexOf('.');
			if (idx == -1) {
				if (kingaku.length() > 12) {
					return false;
				}
			} else {
				if (idx > 12 || kingaku.length() - idx > 3) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 入力禁止文字列入力チェック
	 * @param check
	 * @param val
	 * @return
	 * @throws Exception
	 */
	private boolean checkChar(InputCheck check, String val) throws Exception {
		boolean result = false;
		if (!check.isNullBlank(val)) {
			if (check.haveKinshiMoji(val)) {
				result = true;
			}
		}
		return result;
	}

	//要件No.四-13　追加抽出条件項目の組み合わせチェック
	//追加開始
	/**
	 * 追加抽出条件項目の組み合わせチェック
	 * @return 不正の場合false 
	 * @throws Exception
	 */
	private boolean checkKumiawase() throws Exception {
		boolean result = false;
		//入力判定用カウンタ
		int flg = 0;
		//①「信用格付新規W･T格先」抽出条件入力判定
		if(form.getKakoKtkFlg().equals("1")){
			flg++;
		}else if(form.getKakoKtkFrom() != null && !(Function.trim(form.getKakoKtkFrom())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getKakoKtkTo() != null && !(Function.trim(form.getKakoKtkTo())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getKakoKtkSansyo() != null && !(Function.trim(form.getKakoKtkSansyo())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}
		//②「固定化営業債権新規計上先」抽出条件入力判定
		if(form.getGenzaiKoteiSaikengakuKagen() != null && !(Function.trim(form.getGenzaiKoteiSaikengakuKagen())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getGenzaiKoteiSaikengakuJyogen() != null && !(Function.trim(form.getGenzaiKoteiSaikengakuJyogen())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getKakoKoteiSaikengakuKagen() != null && !(Function.trim(form.getKakoKoteiSaikengakuKagen())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getKakoKoteiSaikengakuJyogen() != null && !(Function.trim(form.getKakoKoteiSaikengakuJyogen())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}else if(form.getKoteiSaikengakuSansyo() != null && !(Function.trim(form.getKoteiSaikengakuSansyo())).equals(GS.EMPTY_CHARCTER)){
			flg++;
		}
		//③「フラグ先」抽出条件入力判定
		if(form.getFlgsakiFlg().equals("1")){
			flg++;
		}
		//抽出条件項目の組み合わせ判定
		//①②③のいずれかのみ入力があれば下記の処理は通らない
		if(flg > 1){
			result = true;
		}
		return result;
	}
	//追加完了
}