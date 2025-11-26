/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2009/05/26      SSC				1.5次版組込
002     2009/10/20      SSC				課題No.43 MMYYYYによる検索追加
002     2009/12/2       SSC				課題No.32,課題No.91
										国内帳票(BS照会表、BS対比表、集計表(期中)､集計表(期末))
 										レスポンス改善のため新規作成による対応(1.0次のソースを削除)
******************************************************************************/
package app.syokaiZen.action;

import app.SessionDataZen;
import app.syokaiZen.bss.SateiBss;
import app.syokaiZen.form.SateiForm;
import app.system.form.DownloadForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.util.Function;
import common.util.SaikenExcelD;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
//import common.util.SyukeiExcel;
// 追加完了


/**
 * 査定結果照会画面アクションクラス
 */
public class SateiAction extends AppMenuAction {
    
    /** クラス名 */
	private String CLASSNAME = getClass().getName();

	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("tanto","tanto");
		map.put("sort","sort");
		map.put("view","view");
		map.put("back","back");
		map.put("syosai","syosai");
		map.put("search", "search");
		map.put("downloadSyukei", "downloadSyukei");
		map.put("downloadBSS", "downloadBSS");
		map.put("downloadBST", "downloadBST");
		map.put("bumonChange", "bumonChange");
		map.put("modeChange", "modeChange");
		map.put("sateisyosai", "sateisyosai");
		map.put("downloadMeisai", "downloadMeisai");
		//障害票：814　2008/6/11　SJA中島　査定会社選択時に、選択した査定会社に紐付く部門一覧を取得するアクション追加
		map.put("sateiCorpChange","sateiCorpChange");
		// No594, 2008/06/12, SJA渡辺, ダウンロードボタンアクション追加
		map.put("download","download");
		map.put("downloadChange","downloadChange");
		
	
		return map;
	}
	
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {    
	    HttpServletRequest request = appContext.getRequest();
	    SessionDataZen cmnData = appContext.getCMNZen();

	    // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
	    //appContext.getLog().write(0, CLASSNAME, cmnData.getComNijiStRegistKg());
	    
	    // この画面用のActionFormを作成
	    SateiForm form = new SateiForm();
	    form.setInitMode(1);
	    
	    // No410, 2008/05/23, SJA渡辺, チャンピオン部からの遷移はないので処理コメントアウト
	    // チャンピオン部からの遷移の場合のみ、フラグを立てる
	    /*if (cmnData.getReturnId().equals(GS.SYSTEM_CHAMPION)){
	        form.setSearchFlag(1);			// 1:チャンピオン部遷移設定
	    }*/
	    cmnData.setReturnId(GS.SYOKAI_SATEI);
	    
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
        
	    return select(appContext);
	}
	
    // No407, 2008/06/01, SJA渡辺, ページ表示個所修正
	/**
	 * 【画面初期表示処理(遷移先より戻り時)】
	 */
	public Object appReExecute(AppContext appContext) throws Exception {
		SessionDataZen cmnData = appContext.getCMNZen();
		SateiForm form = (SateiForm)appContext.getActionForm();
		cmnData.setReturnId(GS.SYOKAI_SATEI);
		Object result = search(appContext);
    	form.setPager(form.getIdForPager());
    	return result;    
	}
	

	/**
	 * 【ソート順コンボボックス処理】
	 */
	public Object sort(AppContext appContext) throws Exception {
	    SateiForm form = (SateiForm) appContext.getActionForm();
	    
	    // 検索結果が表示されている場合のみ、特定ソート順で再検索する
	    if(form.getList() != null) {
	    	// No423, 2008/05/22, SJA渡辺, 指定条件での検索を行うように修正(検索時の条件を退避させるため)。
	    	search(appContext);
	    }
	    	return GS.RC_OK;
	}
	
	/**
	 * 戻るボタン処理
	 */
	public Object back(AppContext appContext) throws Exception {
	    String returnId = appContext.getCMNZen().getReturnId();
	    SateiForm form = (SateiForm) appContext.getActionForm();
	    SessionDataZen cmnData = appContext.getCMNZen();
	    
	    // チャンピオン部からの画面遷移で検索していた場合、チャンピオン部画面に戻る
	    if (form.getSearchFlag()==1){
		    app.system.action.ChampionAction acc = new app.system.action.ChampionAction();
		    acc.appExecute(appContext);
		    return "system_champion";
	    }
	    else
	        return "menu";
	}
	
	// 障害票：814　2008/6/11　SJA中島　査定会社選択時に、選択した査定会社に紐付く部門一覧を取得する
	/**
	 * 【査定会社名選択時、部名変更コンボボックス処理】
	 */
	public Object sateiCorpChange(AppContext appContext) throws Exception {
		
	    // 査定会社名から部門名の検索
	    SateiBss bss = new SateiBss(appContext);   
	    bss.executeSateiCorp();
	    
		return GS.RC_OK;
	}
	
	/**
	 * 【部門名選択時、部名変更コンボボックス処理】
	 */
	public Object bumonChange(AppContext appContext) throws Exception {
		
	    // 部門名から部名の検索
	    SateiBss bss = new SateiBss(appContext);   
	    bss.executeBumon();
	    
		return GS.RC_OK;
	}
	
	/**
	 * 【検索モード変更・ラジオボタン処理】
	 */
	public Object modeChange(AppContext appContext) throws Exception {
	    SateiForm form = (SateiForm) appContext.getActionForm();
		SessionDataZen cmnData = appContext.getCMNZen();
	    
		//検索結果のクリア
		form.setCnt_meisai(0);
		form.setAr_meisai(new ArrayList());
		form.setPager(new ArrayList());
		
		// No423, 2008/05/22, SJA渡辺, 退避させていた検索ボタン押下時のデータの初期化を行うよう修正
		// 検索用退避データの初期化
		form.setInitSaveData();
		
		// No424, 2008/05/22, SJA渡辺, 抽出事由とソート順を初期値に設定
		form.setSort(0);
		form.setCaseSelection("");
		// No594, 2008/06/12, SJA渡辺, 〆区分を初期値に設定
		form.setKaisu("");
		
		//ソート順表示リストの内容を変更する
		SateiBss bss = new SateiBss(appContext);   
		bss.executeSelectParam();
		
		return GS.RC_OK;
	}

	//課題No.43
	//追加開始
    /**
     * 【年月変換処理】 <br>
     * @return String
     */
    private String convert(String val,String langMode){
    	StringBuffer rtn = new StringBuffer();
    	String tmp = Function.trim(val);
    	if(GS.EMPTY_CHARCTER.equals(tmp)){
    		rtn.append(GS.EMPTY_CHARCTER);
    	}else if(tmp.length() < 6){
    		rtn.append(tmp);
    	}else if(GS.LANG_EN.equals(langMode)){
    		rtn.append(tmp.substring(2)).append(tmp.substring(0,2));
    	}else{
    		rtn.append(tmp);
    	}
    	return rtn.toString(); 
    }
	//追加完了
    
	/**
	 * 【指定条件で検索の実行】
	 */
	public String search(AppContext appContext) throws Exception {
		DownloadForm dlForm = (DownloadForm) appContext.getActionForm();
		SessionDataZen cmnData = appContext.getCMNZen();
		SateiForm form = new SateiForm();
		
		// 入力欄内・テキストボックス項目の空白削除
		//課題No.43
		//修正開始
		//form.setYm(Function.trim(dlForm.getTaisyo_ym()));//対象年月
		//form.setBaseDate(Function.trim(dlForm.getSateiki()));//基準年月(査定期)
		//form.setObjectDate(Function.trim(dlForm.getTaisyo_ym()));//対象年月(査定期)
		form.setYm(this.convert(dlForm.getTaisyo_ym(),cmnData.getComLangMode()));//対象年月
		form.setBaseDate(this.convert(dlForm.getSateiki(),cmnData.getComLangMode()));//基準年月(査定期)
		form.setObjectDate(this.convert(dlForm.getTaisyo_ym(),cmnData.getComLangMode()));//対象年月(査定期)
		//修正完了
		form.setKanjoCd(Function.trim(dlForm.getKanjo_cd()));//勘定先CD
	    form.setKanjoName(Function.trim(dlForm.getKanjo_nm()));//勘定先名称
	    form.setDunsNo(Function.trim(dlForm.getDuns_no()));//Duns_No.
	    form.setSystemSection("01");//基幹システム区分
	    form.setBumonCode(Function.trim(dlForm.getHanyo2()));//部門コード
	    form.setJudgeCorp(Function.trim(dlForm.getHanyo1()));//査定会社コード
	    form.setKaisu(Function.trim(dlForm.getSime_kbn()));//〆区分
	    form.setHanki_sihanki_kbn(Function.trim(dlForm.getHanki_sihanki_kbn()));
	    if(GS.EMPTY_CHARCTER.equals(form.getJudgeCorp())){
		    form.setSansyoBunrui2(Function.trim(dlForm.getHanyo2_1()));
	    }else{
		    form.setSansyoBunrui2(Function.trim(dlForm.getHanyo2_2()));
	    }
	    
		//検索結果のクリア
		form.setCnt_meisai(0);
		form.setAr_meisai(new ArrayList());
		form.setPager(new ArrayList());
		
		//検索モード設定
		if(dlForm.getList_type().equals("011") || dlForm.getList_type().equals("012")){
			form.setSearchMode(1);
		}else{
			form.setSearchMode(0);
		}
	    
		//言語モード設定
		if(dlForm.getList_type().equals("011") || dlForm.getList_type().equals("013") || dlForm.getList_type().equals("015")
			|| dlForm.getList_type().equals("017") || dlForm.getList_type().equals("019")){
			cmnData.setComLangMode(GS.LANG_JA);
		}else{
			cmnData.setComLangMode(GS.LANG_EN);
		}

		//帳票出力用にアクションフォーム設定
		appContext.setActionForm(form);

		// 検索の実行
		SateiBss bss = new SateiBss(appContext);
		bss.executeSelectParam();
		bss.execute();
		
		return dlForm.getList_type();
	}
	
	/**
	 * 集計表ダウンロードボタン押下時の処理
	 */
	// 課題No.32,課題No.91
	// 追加開始
	/*
	public Object downloadSyukei(AppContext appContext) throws Exception {
		SateiForm form = (SateiForm)appContext.getActionForm();
		List meisaiList = form.getAr_meisai();
	    if(meisaiList.size() == 0){
			appContext.setMsgCode("warning.0004");
	    }else{
		    SyukeiExcel syukei = new SyukeiExcel(appContext);
		    syukei.execute();
	    }
	    
	    return GS.RC_OK;
	}
	*/
	// 追加完了
	
	/**
	 * BS照会表ダウンロードリンク選択時の処理
	 */
	// 課題No.32,課題No.91
	// 追加開始
	/*
	public Object downloadBSS(AppContext appContext) throws Exception {
		SateiForm form = (SateiForm)appContext.getActionForm();
		List meisaiList = form.getAr_meisai();
	    if(meisaiList.size() == 0){
			appContext.setMsgCode("warning.0004");
	    }else{
		    BSSyokaiExcel bsSyokai = new BSSyokaiExcel(appContext);
		    bsSyokai.execute();
	    }
	    
	    return GS.RC_OK;
	}
	*/
	// 追加完了
	
	/**
	 * BS対比表ダウンロードリンク選択時の処理
	 */
	// 課題No.32,課題No.91
	// 追加開始
	/*
	public Object downloadBST(AppContext appContext) throws Exception {
		SateiForm form = (SateiForm)appContext.getActionForm();
		List meisaiList = form.getAr_meisai();
	    if(meisaiList.size() == 0){
			appContext.setMsgCode("warning.0004");
	    }else{
		    BSTaihiExcel bsTaihi = new BSTaihiExcel(appContext);
		    bsTaihi.execute();
	    }
	    
	    
	    return GS.RC_OK;
	}
	*/
	// 追加完了
	
	
	/**
	 * 明細ダウンロードボタン押下時の処理
	 */
	public Object downloadMeisai(AppContext appContext) throws Exception {
		SateiForm form = (SateiForm)appContext.getActionForm();
		List meisaiList = form.getAr_meisai();
	    if(meisaiList.size() == 0){
			appContext.setMsgCode("warning.0004");
	    }else{
		    SaikenExcelD saiken = new SaikenExcelD(appContext);
		    saiken.execute();
	    }
	    
	    return GS.RC_OK;
	}
	
	// No594, 2008/06/12, SJA渡辺, ダウンロードボタン押下時の処理
	/**
	 * 帳票ダウンロードボタン押下時の処理
	 */
	public Object download(AppContext appContext) throws Exception {
		
		SateiForm form = (SateiForm)appContext.getActionForm();
		String tyohyo = form.getTyohyo();
		
		SateiBss bss = new SateiBss(appContext);
		
		// ダウンロードチェックボックスにチェックがついているか判定
		if (form.getDownloadFlg() == 1 && bss.downloadCheck()) {
			System.out.println("テスト");
			// 帳票種別により呼び出しメソッド変更
			
			if ("1".equals(tyohyo)) {
				// 課題No.158
				// 追加開始
				//downloadSyukei(appContext);
			} else if ("2".equals(tyohyo)) {
				//downloadSyukei(appContext);
			} else if ("3".equals(tyohyo)) {
				//downloadBSS(appContext);
			} else if ("4".equals(tyohyo)) {
				// No830, 2008/06/12, SJA渡辺, 現在対比するデータがないためダウンロード不可とし（一時的にコメントアウト）、エラーメッセージ表示
				// 管理票No200807081024, 2008/07/09, SJA渡辺, ダウンロード不可解除
				//downloadBST(appContext);
				// 追加完了
				//appContext.setMsgCode("err.0115");
				//appContext.setFocusField("tyohyo");
			} else if ("5".equals(tyohyo)) {
				System.out.println("５がとおりますた");
				downloadMeisai(appContext);
			}
		}
    	///////////////////////////////////////
    	//障害票：861
    	//チェックイン日：2008/6/15
    	//対応者：SJA小森
    	//概要：ダウンロードチェックボックスにチェックを入れない際メッセージを表示する。
    	////////////////////////////////////////
		// No861, 2008/06/20, SJA渡辺, エラーメッセージ内容不正修正(SateiBss.javaに処理移す)
//		else{
//			appContext.setMsgCode("err.0116");
			// No861, 2008/06/15, SJA遠藤, エラーメッセージの追加に伴い、フォーカス移動処理の追加
//			appContext.setFocusField("objectDate");
//		}
		// 障害票No861　2008/06/15 Komori End				
		
		return GS.RC_OK;
	}
	
	// No549, 2008/06/12, SJA渡辺, ダウンロードチェックボックス押下処理
	public Object downloadChange(AppContext appContext) throws Exception {
		// リセットメソッド呼び出し、リロード
		SateiForm form = (SateiForm)appContext.getActionForm();
		if (form.getSearchMode() == 0) {
			form.setKaisu("");
			form.setTyohyo("1");
		} else {
			form.setKaisu("");
			form.setTyohyo("5");
		}
		return GS.RC_OK;
	}
	
	/**
	 * 【表示件数コンボボックス処理】
	 */
	public Object view(AppContext appContext) throws Exception {
	    SateiForm form = (SateiForm) appContext.getActionForm();

		// 表示件数の変更をPagerオブジェクトに設定
		form.setPager();
		
	    return GS.RC_OK;
	}

	/**
	 * 【勘定先CDリンク処理】
	 */
	public Object sateisyosai(AppContext appContext) throws Exception {
	    SessionDataZen cmnData = appContext.getCMNZen();
	    SateiForm form = (SateiForm) appContext.getActionForm();
	    
	    HashMap map = (HashMap)form.getAr_meisai().get(form.getId());
	    
	    cmnData.setYm((String)map.get("ym"));
	    cmnData.setSatei_anken_no(form.getAnken_no());
	    cmnData.setAnken_ka_cd((String)map.get("ka_cd"));
	    cmnData.setAnken_kikan_tori_cd((String)map.get("kikan_tori_cd"));
	    cmnData.setAnken_mise_cd((String)map.get("mise_cd"));
	    cmnData.setAnken_satei_kaisya_cd((String)map.get("satei_kaisha_cd"));
	    cmnData.setAnken_system_kbn((String)map.get("system_kbn"));
	    form.setIdForPager(Function.getValueOfInt((String)map.get("id")) + 1);
	    
	    //cmnData.setReturnId(GS.SYOKAI_SATEI);
	    
	    // 詳細画面の表示処理実行
	    //SateisyosaiBss bss = new SateisyosaiBss(appContext);
		//SateisyosaiAction acc = new SateisyosaiAction();
	    //acc.appExecute(appContext);
	    
	    // 遷移元のActionFormをsessionから削除
	    appContext.removeAttribute("03SateiForm");
	    
	    return "satei_syosai";
	}


	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext) throws Exception {   
	    SateiForm form = (SateiForm) appContext.getActionForm();
	    SessionDataZen cmnData = appContext.getCMNZen();
	    // session取得（Pager用の処理）
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
	    HttpSession session = appContext.getRequest().getSession( true );
  
	    // 検索実行
	    SateiBss bss = new SateiBss(appContext);       	    
	    if(form.getInitMode() == 1) { // 画面初期表示時
	        String result = bss.executeInit();
	        
	        // チャンピオン部からの遷移の場合、フォームに各値を入れ検索を行う、空白削除も一応実行
	        // TODO NullPointerException対策・本データではNullは来ない？
	        if (form.getSearchFlag()==1) {
	            if (cmnData.getYm()!=null) form.setBaseDate(cmnData.getYm());
	            if (cmnData.getKanjo_cd()!=null) form.setKanjoCd(Function.trim(cmnData.getKanjo_cd()));
	            if (cmnData.getKanjo_nm()!=null) form.setKanjoName(Function.trim(cmnData.getKanjo_nm()));
	            if (cmnData.getSystem_kbn()!=null) form.setSystemSection(Function.trim(cmnData.getSystem_kbn()));
	            if (cmnData.getCountry_nm()!=null) form.setCountry(Function.trim(cmnData.getCountry_nm()));
	            bss.execute();
	        }
	        
	        // sessionスコープにActionFormを登録（Pager用の処理）
	        session.setAttribute("03SateiForm", form);

	        return result;	
	    } else { // 画面初期表示時以外
			return GS.RC_OK;
	    }
	}
	
	
	public Object prevX(AppContext appContext) throws Exception {
	    SateiForm form = (SateiForm)appContext.getActionForm();
	    
		// 表示部分の変更をListオブジェクトに設定
		form.setPrevList();
		
		appContext.setActionForm(form);
		
	    return GS.RC_OK;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
	    SateiForm form = (SateiForm)appContext.getActionForm();
	    
		// 表示部分の変更をListオブジェクトに設定
		form.setNextList();
		
		appContext.setActionForm(form);
		
	    return GS.RC_OK;	    
	}
}