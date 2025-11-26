/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/04		SSC				課題No.21 ユーザマスタ 参照組織一覧の追加・削除制御対応
003		2009/11/13		SSC				課題No.110 ユーザマスタ 帳票選択の制御対応
004		2009/11/25		SSC				課題No.112 2バイト文字対応
005		2009/11/25		SSC				課題No.115 ユーザマスタ登録 チェックの制御対応
006		2009/11/25		SSC				課題No.107 ユーザマスタ登録 チェック追加
007		2009/11/26		SSC				課題No.196 ユーザマスタ 参照組織一覧の追加・削除制御対応
										(地域統括者の場合も統括チェックボックスを使用可能にする)
008		2009/12/10		SSC				課題No.196 既定ラジオボタン制御、参照チェック修正　追加
009		2009/12/20		SSC				課題No.215 追加ボタン押下時の組織情報の初期化を削除
010		2010/01/08		SSC				課題No.228 課題No.196による既定ラジオボタン制御修正
011		2010/01/08		SSC				課題No.229 業務フローパターン未選択時の参照チェックボックスチェックスキップ
012		2010/01/08		SSC				課題No.230 ログインユーザでログインユーザを更新時対応
013		2010/01/12		SSC				課題No.231 ログインユーザでログインユーザを更新時対応
014		2011/06/30		SSC				案件No.D9059 他地域の権限を持つユーザでも、自分の担当地域権限は変更できるよう対応
015		2011/07/29		SSC				案件No.D9059 全権限削除時にチェックボックス反映処理でエラーとなっていたため対応
016		2015/03/30		SSC				BJ201408049 IA化対応時の機能改善
017		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.dbAcc.UserIchiranDbAcc;
import app.system.dbAcc.UserTorokuDbAcc;
import app.system.form.TantouBean;
import app.system.form.UserTorokuForm;
import app.system.form.WorkFlowListBean;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS7107　ユーザマスタメンテナンス_登録 ビジネス ロジッククラス <br>
 */
public class UserTorokuBss {

	private AppContext appContext = null;					                    // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               	// ＤＢアクセス
	private Log log = null;									                // LOG
	private SessionData cmnData;							                    // 共通セッション
	private UserBean user_bean;								                // ユーザービンー
	private UserMaintenanceBean user_maintenance_bean;							// ユーザメンテナンスビンー
	private UserTorokuForm form;                                               // アクションフォーム	
	UserTorokuDbAcc dbacc = null;
	
	private static final String DELETE_BTN_HYOUJI_YES		= "1";				// 業務フローパターン一覧の削除ボタン：表示
	private static final String DELETE_BTN_HYOUJI_NO		= "0";				// 業務フローパターン一覧の削除ボタン：表示ではない
	private static final String MAIL_HAISHIN_HYOUJI_JA	= "○";				// ○_Ja
	private static final String MAIL_HAISHIN_HYOUJI_EN	= "Y";				// Y_En
    private static final String CHECKBOX_VALUE_ON			= "1";				// チェックボックスのステータス:オン
    private static final String CHECKBOX_VALUE_OFF		= "0";				// チェックボックスのステータス:オフ
	private static final String SPACE						= "       ";		// 半角スペース7桁

	
	// メール配信先登録用
	private static final String ID									="ID";
	private static final String SYSTEM_KBN_H						="SYSTEM_KBN";
	private static final String HANYOU1FLG							="HANYOU1FLG";	// 汎用1フラグ
	private static final String HANYOU1								="HANYOU1";		// 汎用1
	private static final String HANYOU1_NM							="HANYOU1_NM";	// 汎用1名
	private static final String HANYOU2FLG							="HANYOU2FLG";	// 汎用2フラグ
	private static final String HANYOU2								="HANYOU2";		// 汎用2
	private static final String HANYOU2_NM							="HANYOU2_NM";	// 汎用2名
	private static final String HANYOU3FLG							="HANYOU3FLG";	// 汎用3フラグ
	private static final String HANYOU3								="HANYOU3";		// 汎用3
	private static final String HANYOU3_NM							="HANYOU3_NM";	// 汎用3名
	// 担当組織一覧用
	private static final String HANYOU4								="HANYOU4";
	

    /**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public UserTorokuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.user_maintenance_bean = cmnData.getUser_maintenance_bean();
		this.form = (UserTorokuForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {

		// 課題No.230
		// 追加開始
		//ログインユーザと対象のユーザが同じ場合フラグを設定
		if(Function.trim(user_bean.getComUserId()).equals(Function.trim(user_maintenance_bean.getUser_id()))){
			form.setUser_id_flg(Boolean.TRUE);
		}
		// 追加完了

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new UserTorokuDbAcc(sqlExec, log, appContext);

		// ユーザID
		form.setUser_id(user_maintenance_bean.getUser_id());
		
		// 楽観排他：対象ユーザのユーザ情報の最新更新日時を取得
		form.setUserLastUPD_DT(dbacc.getSelectM0101());
		
		// 氏名
		form.setUser_nm(user_maintenance_bean.getUser_nm());
		
		// 会社名
		form.setCompany_nm(user_maintenance_bean.getKaisya_nm());
		
		// 所属組織名
		form.setSoshiki_nm(user_maintenance_bean.getSyozoku_busyo_nm());
		
		// メールアドレス
		form.setEmail_addr(user_maintenance_bean.getMail_address());

		// システム管理者
		form.setAdmin_kanri_flg(user_maintenance_bean.getAdmin_flg());

		// メール配信
		form.setMail_haisin_kbn(CHECKBOX_VALUE_OFF);
		if (GS.LANG_JA.equals(cmnData.getComLangMode())) {
			if (MAIL_HAISHIN_HYOUJI_JA.equals(user_maintenance_bean.getMail_haishin())) {
				form.setMail_haisin_kbn(CHECKBOX_VALUE_ON);
			}
		} else {
			if (MAIL_HAISHIN_HYOUJI_EN.equals(user_maintenance_bean.getMail_haishin())) {
				form.setMail_haisin_kbn(CHECKBOX_VALUE_ON);
			}
		}
		// 課題No.110 ユーザマスタ 帳票選択の制御対応
		// 追加開始
		
		// 帳票出力言語(日本語:１、英語:２、出力時に選択:３)
		if(!Function.trim(user_maintenance_bean.getPrintout_lang_kbn()).equals(GS.EMPTY_CHARCTER)) {
			form.setPrintout_default_lang_kbn(user_maintenance_bean.getPrintout_lang_kbn());
		}else{
			//帳票力言語がnullの場合、デフォルト値として出力時に選択:3を設定
			form.setPrintout_default_lang_kbn("3");
		}
		
		// 追加完了
		
		// 汎用1
		dbacc.getHanyo1();
		
        String hanyou1 = GS.EMPTY_CHARCTER;
        if (form.getAr_hanyo1() != null) {
    		hanyou1 = getFormatHanyo1Text();
    		form.setHanyou1(hanyou1);
        }
        
		// 業務フローセレクトボックス
		dbacc.getGyoumu_huro();		

        form.setDefault_flg_checked(GS.EMPTY_CHARCTER);

		// 初期化の一覧データを設定
		this.initMeisai(user_maintenance_bean.getWorkflow_list());	

		// 担当組織一覧の表示
		this.usertantoHyouji();

		// 現在の業務フローから、汎用4のラベル表示/非表示を判断する
		if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())
				&& GS.SATEIKAISYA_SJ.equals(user_bean.getComWorkflowSateikaisya_cd())) {
			// 双日本社の場合：表示
			form.setHanyou4LabelFlg(GS.ON);
		}else{
			// 双日本社以外の場合：非表示
			form.setHanyou4LabelFlg(GS.OFF);
		}
		
		// メール配信先表示の判定
		this.haishinHyouji();
		// メール配信先を取得する
		dbacc.getKijyunbi();
		dbacc.getHaishinsaki();
		
		return GS.OS7107;
	}
    
    /**
     * 【登録処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String toroku() throws Exception {  

		InputCheck inpChk = new InputCheck();

		// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        UserTorokuDbAcc dbacc = new UserTorokuDbAcc(sqlExec, log, appContext);
        
		// メール配信チェック
		if("1".equals(form.getHaishinHyoujiFlg())){
			if("1".equals(form.getMail_haisin_kbn())){
				// メール配信がONの時、メール配信先が0件はエラー
				List<HashMap<String, String>> haishinChk = (List<HashMap<String, String>>)form.getAr_haishinsaki();
				if(haishinChk == null || haishinChk.size() == 0){
					List<String> msgList = new ArrayList<String>();
					msgList.add(GL.ERR_CHECK);
					msgList.add(GL.OS7107_MAIL_HAISHIN);
					msgList.add(GL.OS7107_TITLE_HAISHINSAKI);
					appContext.setMsgCode(msgList);
					return GS.OS7107;
				}
			} else {
				// メール配信フラグがOFFの時、メール配信先が1件以上はエラー
				List<HashMap<String, String>> haishinChk = (List<HashMap<String, String>>)form.getAr_haishinsaki();
				if(haishinChk != null && haishinChk.size() != 0){
					List<String> msgList = new ArrayList<String>();
					msgList.add(GL.ERR_CHECK);
					msgList.add(GL.OS7107_TITLE_HAISHINSAKI);
					msgList.add(GL.OS7107_MAIL_HAISHIN);
					appContext.setMsgCode(msgList);
					return GS.OS7107;
				}
			}
			// メール配信先 権限チェック
			if(!this.chkMailListKengen()){
				List<String> msgList = new ArrayList<String>();
				msgList.add(GL.ERR_SAMSYO_NOTSET2);
				msgList.add(GL.OS7107_TITLE_HAISHINSAKI);
				msgList.add(GL.OS7107_TITLE_HAISHINSAKI);
				appContext.setMsgCode(msgList);
				return GS.OS7107;
			}
		}

        /*
        // 規定チェック
		if (form.getAr_gyoumu_huro_itiran().size() > 0) {
			if (GS.EMPTY_CHARCTER.equals(form.getDefault_flg_checked())) {
		    	appContext.setMsgCode(GL.ERR_SELECT,GL.OS7107_DEFAULT_FLG);
		        return GS.OS7107;
			}
		}
		*/
    	// メール配信と帳票出力言語のみ変更の場合ではない
		if (!("0").equals(form.getChk_handan_flg())) {
		
    		// システム管理者存在チェック
    		if ("1".equals(form.getAdmin_delete_flg())) {
    			int cnt = dbacc.getSelectM0100();
    			if (cnt == 1) {
    	        	appContext.setMsgCode(GL.ERR_SYSTEM);
    	        	return GS.OS7107;
    			}
    		}
    		
    		// 課題No.115
    		// 追加開始
    		//ログインユーザと対象のユーザが同じ場合下記２つのチェックはおこなわない。
    		// 課題No.230
    		// 修正開始
    		//if(!Function.trim(user_bean.getComUserId()).equals(Function.trim(user_maintenance_bean.getUser_id()))){
    		if(!form.isUser_id_flg()){
    			// 修正完了
    			
	    		// 対象ユーザログインチェック
	    		int taisyo_cnt = dbacc.getSelectT2201();
	    		if (taisyo_cnt >= 1) {
		        	appContext.setMsgCode(GL.ERR_NOTUPDATE);
		        	return GS.OS7107;	
	    		}
	    		
	    		// 代行者ログインチェック
	    		int daiko_cnt = dbacc.getSelectT2200();
	    		if (daiko_cnt >= 1) {
		        	appContext.setMsgCode(GL.ERR_NOTUPDATE);
		        	return GS.OS7107;	
	    		}
    		}
    		// 追加完了
    		
    		// もぎ取り、承認待ちチェック
    		int t0800_cnt = dbacc.getSelectT0800();
    		if (t0800_cnt >= 1) {
	        	appContext.setMsgCode(GL.ERR_UPDATE);
	        	return GS.OS7107;	
    		}   
    		
    		// もぎ取り、承認待ちチェック
    		int t1400_cnt = dbacc.getSelectT1400();
    		if (t1400_cnt >= 1) {
	        	appContext.setMsgCode(GL.ERR_UPDATE);
	        	return GS.OS7107;	
    		} 	
    	}
		/*
		// 課題No.107
		// 追加開始
		
       	// 参照組織一覧チェックボックスの設定
    	for (int j = 0; j < form.getAr_meisai().size(); j++) {
    		TantouBean tantouBean = (TantouBean)form.getAr_meisai().get(j);
    			result = getToukatuStat(tantouBean);
    			if(result == false){			
    				result = getTantouStat(tantouBean);
    			}
    			if(result == true){
    				sansyo_check = true;
    			}
      	}
    	*/
		
		//課題No.229
		//追加開始
		int arGyoumuHuroIchiranSize = 0;
		if(form.getAr_gyoumu_huro_itiran() != null){
			arGyoumuHuroIchiranSize = form.getAr_gyoumu_huro_itiran().size();
		}
		//追加完了
		// 参照権限チェック
		if(form.getAr_meisai() != null && form.getAr_meisai().size() > 0){
			//課題No.229
			//修正開始
			if(arGyoumuHuroIchiranSize != 0){
				List<WorkFlowListBean> workFlowList = new ArrayList<WorkFlowListBean>(form.getAr_gyoumu_huro_itiran());
				WorkFlowListBean workFlow = new WorkFlowListBean();

				List<TantouBean> tantoSoshikiList = form.getAr_meisai();
				TantouBean tantoSoshiki = new TantouBean();

				boolean chkFlg; // チェックフラグ
				for(int i = 0; workFlowList.size() > i ; i++){
					// 業務フロー分チェックを行う
					workFlow = workFlowList.get(i);
					chkFlg = false; // チェックNG

					for(int n = 0 ; tantoSoshikiList.size() > n ; n++){
						tantoSoshiki = tantoSoshikiList.get(n);
						if(workFlow.getSateikaisya_cd().equals(tantoSoshiki.getHanyou1_cd())){
							chkFlg = true; // 担当組織一覧に業務フローと同じ査定会社あり
							break;
						}
					}
					if(!chkFlg){
						// 担当組織一覧に業務フローと同じ査定会社ない場合、エラー
						appContext.setMsgCode(GL.ERR_SANSYOUCHECK);
						return GS.OS7107;
					}
				}
			}
			//修正完了
		} else if(arGyoumuHuroIchiranSize != 0){
			// 担当組織一覧に業務フローと同じ査定会社ない場合、エラー
			appContext.setMsgCode(GL.ERR_SANSYOUCHECK);
			return GS.OS7107;
		}

    	
 /*
    	// 参照組織チェック
    	if(sansyo_check == false){
    		appContext.setMsgCode(GL.ERR_SANSYOUCHECK);
    		return GS.OS7107;
    	}
    	*/
    	// 追加完了
		
    	// M01_ユーザレベルマスタに対象ユーザ情報が登録されているか確認する
    	Date userUPD_DT = dbacc.getSelectM0101();
    	
    	// ユーザレベルマスタにデータが存在する場合
    	if(userUPD_DT != null){
    		// 更新日時あり
    		if(form.getUserLastUPD_DT() != null){
    			// 初期表示に取得した、データの最新更新日時あり
    			if(userUPD_DT.compareTo(form.getUserLastUPD_DT()) != 0){
        			// 別ユーザに先に更新されたので排他エラー
    	    		appContext.setMsgCode(GL.ERR_CHANGEDDATA);
    	    		return GS.OS7107;
    			}
    			// M01_ユーザレベルマスタの更新
        		dbacc.setUpdateM0100();
    		} else {
    			// 別ユーザに先に登録されたので排他エラー
	    		appContext.setMsgCode(GL.ERR_CHANGEDDATA);
	    		return GS.OS7107;
    		}
        } else {
    		// 更新日時なし（該当なし）
    		if(form.getUserLastUPD_DT() != null){
    			// 別ユーザに先に削除されたので排他エラー
	    		appContext.setMsgCode(GL.ERR_CHANGEDDATA);
	    		return GS.OS7107;
    		} else {
        		// M01ユーザレベルマスタの新規登録
        		dbacc.setInsertM0100();
    		}
    	}
    	
		// 課題No.107
		// 追加開始
/*
       	// 参照組織一覧チェックボックスのチェック設定
    	for (int j = 0; j < form.getAr_meisai().size(); j++) {
    		TantouBean tantouBean = (TantouBean)form.getAr_meisai().get(j);
    		if (!getToukatuStat(tantouBean)) {
    			getTantouStat(tantouBean);
    		}
    	}
*/    	
    	// 追加完了
       	// M02_ユーザ参照組織マスタの削除
       	dbacc.setDeleteM0200();
        // M02_ユーザ参照組織マスタの登録
        this.user(dbacc);
    
    	// M24_ユーザ業務マスタの削除
    	dbacc.setDeleteM2400();
    	
    	// M24_ユーザ業務マスタの登録
    	for (int i = 0; i < form.getAr_gyoumu_huro_itiran().size(); i++) {
    		WorkFlowListBean wf_bean = (WorkFlowListBean)form.getAr_gyoumu_huro_itiran().get(i);
    		wf_bean.setDefault_flg("0");
    		if (Function.getStringOfInt(i).equals(form.getDefault_flg_checked())) {
    			wf_bean.setDefault_flg("1");
    		} 
    		dbacc.setInsertM2400(wf_bean);
    	}
    	
		// M30_メール配信先マスタの削除
		dbacc.setDeleteM3000();
		
		if("1".equals(form.getHaishinHyoujiFlg())){
			// M30_メール配信先マスタの登録(画面表示ありの場合のみ)
			HashMap<String, String> hm = new HashMap<String, String>();
			String sp = "       ";
			if(form.getAr_haishinsaki() != null){
				for(int i = 0; i < form.getAr_haishinsaki().size();i++){
					hm = form.getAr_haishinsaki().get(i);
					if(inpChk.isNullBlank(hm.get(HANYOU2))){
						// 汎用2がnullの場合、汎用2・3に半角スペース
						hm.put(HANYOU2, sp);
						hm.put(HANYOU3, sp);
					} else if(inpChk.isNullBlank(hm.get(HANYOU3))){
						// 汎用3がnullの場合、汎用3に半角スペース
						hm.put(HANYOU3, sp);
					}
					dbacc.setInsertM3000(hm);
				}
			}
		}

    	dbacc.commit();
    	
		//課題No.230
		//修正開始
    	if(form.isUser_id_flg()){
            return GS.OS1101;
    	}else{
            return GS.OS7106;
    	}
		//修正完了
    }
    
    /**
     * 【業務フローパターン削除処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String delete() throws Exception {  
		
        // チェック判断フラグに'1'：チェック対象を設定する 
        form.setChk_handan_flg("1");
        
        // 選択した業務フローパターンを一覧を取得
        String del_index = form.getGyoumu_huro_index();
        
        List<WorkFlowListBean> ar_gh_itiran = form.getAr_gyoumu_huro_itiran();
        
        WorkFlowListBean workFlowListBean = (WorkFlowListBean)ar_gh_itiran.get(Integer.parseInt(del_index));
        
		boolean wfListcheck = false;
		if(!this.workFlowListcheck(workFlowListBean)){
			// 削除対象の業務フローと同じ査定会社のフローがないとき
			if(!this.tantougaicheck(workFlowListBean)){
				return GS.OS7107;
			}
		}else{
			// 削除対象の業務フローと同じ査定会社のフローがあるとき
			wfListcheck = true;
		}

        //課題No.230,231対応
        //修正開始
        if(form.isUser_id_flg() && user_bean.getComWorkflowId().equals(workFlowListBean.getPattern_id())
        		&& user_bean.getComWorkflowSystemkbn().equals(workFlowListBean.getSystem_kbn())
        		&& user_bean.getComWorkflowSateikaisya_cd().equals(workFlowListBean.getSateikaisya_cd())){
        	appContext.setMsgCode(GL.ERR_LOGIN_PATTERNID);
            return GS.OS7107;
        }else{
            ar_gh_itiran.remove(Integer.parseInt(del_index));        
        }
        //修正完了

		for (int i = 0; i < ar_gh_itiran.size(); i++) {
			ar_gh_itiran.get(i).setId(Function.getStringOfInt(i));
		}
		
		// 既定ラジオボタンのオンを再設定する
        boolean default_chk_flg = false;
		for (int i = 0; i < ar_gh_itiran.size(); i++) {
			if ("1".equals(ar_gh_itiran.get(i).getDefault_flg())) {
				form.setDefault_flg_checked(Function.getStringOfInt(i));
				default_chk_flg = true;
			}
		}
		
		// 既定ラジオボタンのオンにする
		if (!default_chk_flg) {
			form.setDefault_flg_checked("0");
		}
		
		// 業フロー一覧をセット
        form.setAr_gyoumu_huro_itiran(ar_gh_itiran);
		
		// メール配信先の表示フラグ設定
		this.haishinHyouji();
		if(!wfListcheck){
			// 担当組織の再表示
			this.setTantoSoshiki_WFdel(workFlowListBean);
			
			if("1".equals(form.getHaishinHyoujiFlg())){
				// メール配信先の表示ありのとき、再設定
				this.setHaishinsaki_WFdel(workFlowListBean);
			}
		}
		
        return GS.OS7107;
    }
    
    /**
     * 【業務フローパターン追加処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String tuika() throws Exception {      

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
    	UserTorokuForm form = (UserTorokuForm)appContext.getActionForm(); 
        
        // チェック判断フラグに'1'：チェック対象を設定する 
        form.setChk_handan_flg("1");        
		
        // 課題No.215
        // 追加開始
        // form.setAr_meisai(new ArrayList<TantouBean>());
        // 追加完了
        
        List<WorkFlowListBean> ar_gh_itiran = new ArrayList<WorkFlowListBean>();
		
        // 選択している業務フローパターンを一覧に追加
		String[] gyoumu_huro = form.getGyoumu_huro().split(",");
		WorkFlowListBean wf_bean = new WorkFlowListBean();
		
		// id
		wf_bean.setId(Function.getStringOfInt(form.getAr_gyoumu_huro_itiran().size()));
		
		// システム区分
		wf_bean.setSystem_kbn(gyoumu_huro[0]);
		
		// 査定会社コード        		
		wf_bean.setSateikaisya_cd(gyoumu_huro[1]);
		
		// 課題No.228
		// 下記課題対応の誤り削除
		// 既定ラジオボタン
		// 課題No.196
		// 業務フローパターンが1件も設定されていない場合、既定ラジオボタンをチェックオンにする
		//if(ar_gh_itiran.size() <= 0){
			//wf_bean.setDefault_flg(GS.OFF);
		//}else{
			//wf_bean.setDefault_flg(gyoumu_huro[2]);
		//}
		// 追加完了
		// 削除完了

		// パターンID       		
		wf_bean.setPattern_id(gyoumu_huro[3]);
		
		// パターン名称
		wf_bean.setPattern_name_jp(form.getAr_gyoumu_huro_hid().get(form.getGyoumu_huro()).toString());
		
		// システム管理者専用フラグ       		
		wf_bean.setAdmin_senyo_flg(gyoumu_huro[4]);
		
		// 削除ボタン
		wf_bean.setDelete_btn_flg(DELETE_BTN_HYOUJI_NO);
		
		if (chk_sateikaisya_cd(form.getAr_hanyo1(), gyoumu_huro[1])) {
			wf_bean.setDelete_btn_flg(DELETE_BTN_HYOUJI_YES);
		}
		
		// 課題No.228
		// 下記課題対応の誤り削除
		// 課題No.196
		// 追加開始
		// 業務フローパターンが1件も設定されていない場合、既定ラジオボタンをチェックオンにする
		//if(ar_gh_itiran.size() == 0){
			//form.setDefault_flg_checked(GS.OFF);
		//}else{
			//form.setDefault_flg_checked(form.getDefault_flg_checked());	
		//}
		// 追加完了
		// 削除完了
		
		ar_gh_itiran = form.getAr_gyoumu_huro_itiran();

		// 課題No.228
		// 課題No.196の誤り修正
		if(ar_gh_itiran.size() == 0){
			form.setDefault_flg_checked(GS.OFF);
		}else{
			form.setDefault_flg_checked(form.getDefault_flg_checked());	
		}
		// 修正完了
		 
		for (int i = 0; i < ar_gh_itiran.size(); i++) {
			WorkFlowListBean wflow_bean = (WorkFlowListBean) ar_gh_itiran.get(i);
			if (wflow_bean.getPattern_id().equals(wf_bean.getPattern_id()) 
				&& wflow_bean.getSateikaisya_cd().equals(wf_bean.getSateikaisya_cd())
				&& wflow_bean.getSystem_kbn().equals(wf_bean.getSystem_kbn())) {
	        	appContext.setMsgCode(GL.ERR_NOADD);
	            return GS.OS7107;
			}
		}
		ar_gh_itiran.add(wf_bean);
		
		// 業務フローパターン一覧
		form.setAr_gyoumu_huro_itiran(ar_gh_itiran);		
		

		// メール配信先の表示フラグ設定
		this.haishinHyouji();
        return GS.OS7107;
    }
    
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		UserIchiranDbAcc dbacc = new UserIchiranDbAcc(sqlExec, log, appContext);
		
		// 明細情報取得
		dbacc.getMeisai();
		
		return GS.OS7106;
		
	}
    
    /**
     * 業務フローパターン【リスト】の査定会社コードが汎用1【リスト】に存在チッェク <br>
     * 
     * @return
     * @throws Exception
     */
    public boolean chk_sateikaisya_cd(LinkedHashMap ar_hanyo1,String sateikaisya_cd) throws Exception {  

    	Object[] hanyou1 = ar_hanyo1.values().toArray();
    	for (int i = 0; i < hanyou1.length; i++) {
    		if (hanyou1[i].equals(sateikaisya_cd)) {
    			return true;
    		}
    	}
        
        return false;
    }
    
    /**
     * 汎用1【リスト】から汎用1を取得する <br>
     * 
     * @return
     * @throws Exception
     */
    private String getFormatHanyo1Text() throws Exception {  
 		
        String hanyou1 = GS.EMPTY_CHARCTER;
    	Object[] hanyo1 = form.getAr_hanyo1().values().toArray();
        for (int i = 0; i < hanyo1.length; i++) { 
        	if (!GS.EMPTY_CHARCTER.equals(hanyo1[i])) {
            	hanyou1 += "'" + hanyo1[i] + "'";  
            	if (i != hanyo1.length - 1) {
                	hanyou1 += ",";  
            	}
        	}
        }
            
        InputCheck inpChk = new InputCheck();
        if (inpChk.isNullBlank(hanyou1)) {
        	return "''";
        }
        return hanyou1;
    }
    
    /**
     * システム管理者チェックボックス <br>
     * 
     * @return
     * @throws Exception
     */
    public String admin_kanri() throws Exception {  
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        UserTorokuDbAcc dbacc = new UserTorokuDbAcc(sqlExec, log, appContext);    	       
        
    	// システム管理者チェックボックスがチェックオンの場合
    	if ("1".equals(form.getAdmin_kanri_flg())) {
    		
    		// システム管理者削除フラグに'0'：未対象を設定
    		form.setAdmin_delete_flg("0");
    		
    		// チェック判断フラグに'1'：チェック対象を設定する
    		form.setChk_handan_flg("1");
    		
            String hanyou1 = GS.EMPTY_CHARCTER;
            if (form.getAr_hanyo1() != null) {
        		hanyou1 = getFormatHanyo1Text();
        		form.setHanyou1(hanyou1);
            }
            
    		// 業務フローセレクトボックス
    		dbacc.getGyoumu_huro();          
                		
    		// 初期化の一覧データを設定
    		this.initMeisai(form.getAr_gyoumu_huro_itiran());
    		
    	} else {
    		List<WorkFlowListBean> ar_gh_itiran = new ArrayList<WorkFlowListBean>();
    		List<WorkFlowListBean> ar_dell_itiran = new ArrayList<WorkFlowListBean>();

			// 削除対象の業務フローの担当組織チェック
			for (int i = 0; i < form.getAr_gyoumu_huro_itiran().size(); i++) {
				WorkFlowListBean wf_bean = (WorkFlowListBean) form.getAr_gyoumu_huro_itiran().get(i);
				if (!"1".equals(wf_bean.getAdmin_senyo_flg())) {
					// システム管理者専用ではない業務フロー
					ar_gh_itiran.add(wf_bean);
				} else {
					// システム管理者専用の業務フロー
					if(!this.workFlowListcheck(wf_bean)){
						// 削除対象の業務フローと同じ査定会社のフローがないとき
						if(!this.tantougaicheck(wf_bean)){
							// 担当組織に処理対象外が含まれている
							form.setAdmin_kanri_flg(GS.ON);
							return GS.OS7107;
						}
						ar_dell_itiran.add(wf_bean);
					}
				}
			}
    		
    		// システム管理者削除フラグに'1'：未対象を設定
    		form.setAdmin_delete_flg("1");
    		
    		// チェック判断フラグに'1'：チェック対象を設定する
    		form.setChk_handan_flg("1");
    		 		
            String hanyou1 = GS.EMPTY_CHARCTER;
            if (form.getAr_hanyo1() != null) {
        		hanyou1 = getFormatHanyo1Text();
        		form.setHanyou1(hanyou1);
            }
            
    		// 業務フローセレクトボックス
    		dbacc.getGyoumu_huro();
    		
    		// システム管理者専用フラグが'1'の業務フローパターンの担当組織を削除
    		for (int i = 0; i < ar_dell_itiran.size(); i++) {
    			WorkFlowListBean wf_bean = (WorkFlowListBean) ar_dell_itiran.get(i);

				// 担当組織一覧から削除する
				this.setTantoSoshiki_WFdel(wf_bean);
				if ("1".equals(form.getHaishinHyoujiFlg())
						&& (GS.SATEIKAISYA_SJ.equals(wf_bean.getSateikaisya_cd())
								|| GS.SATEIKAISYA_PN.equals(wf_bean.getSateikaisya_cd()))) {
					// メール配信先一覧から削除する
					this.setHaishinsaki_WFdel(wf_bean);
				}
			}

    		if (ar_gh_itiran.size() > 0) {
    			
    			// 業務フローパターン一覧
        		form.setAr_gyoumu_huro_itiran(ar_gh_itiran);
        		
        		WorkFlowListBean wf_bean = new WorkFlowListBean();
        		
        		for (int i = 0; i < ar_gh_itiran.size(); i++) {
        			ar_gh_itiran.get(i).setId(Function.getStringOfInt(i));
        			               		
        			// 削除ボタン
            		wf_bean.setDelete_btn_flg(DELETE_BTN_HYOUJI_NO);
        			if (chk_sateikaisya_cd(form.getAr_hanyo1(), ar_gh_itiran.get(i).getSateikaisya_cd())) {
        				ar_gh_itiran.get(i).setDelete_btn_flg(DELETE_BTN_HYOUJI_YES);
        			}        			
        		}
        		
        		// 業務フローパターン一覧
        		form.setAr_gyoumu_huro_itiran(ar_gh_itiran);
        		
        		return GS.OS7107;
    		}
    		
			// 業務フローパターン一覧
			form.setAr_gyoumu_huro_itiran(ar_gh_itiran);
   	}
    	
		return GS.OS7107;
    }
    

    /**
     * 初期化の一覧データを取得(担当組織、メール配信先以外) <br>
     * 
     * @throws Exception
     */
    private void initMeisai(List workflow_list) throws Exception {
    	
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
    	  
    	// 業務フローパターン一覧リスト
		List<WorkFlowListBean> ar_gyoumu_huro_itiran = new ArrayList<WorkFlowListBean>();           
        
		for (int i = 0; i < workflow_list.size(); i++) {
			 
			// 登録画面の業務フローパターン一覧用リストのビンー
			WorkFlowListBean toroku_wf_bean = new WorkFlowListBean();	
			
			// 共)ユーザメンテ情報より取得した既定業務フローパターンのマップ
			WorkFlowListBean itiran_wf_bean = (WorkFlowListBean) workflow_list.get(i);
			
			// id
			toroku_wf_bean.setId(Function.getStringOfInt(i));						
			
			// 業務フローパターンシステム区分
			toroku_wf_bean.setSystem_kbn(itiran_wf_bean.getSystem_kbn());
			
			// 業務フローパターン査定会社コード
			toroku_wf_bean.setSateikaisya_cd(itiran_wf_bean.getSateikaisya_cd());
			
			// 既定ラジオボタン
			toroku_wf_bean.setDefault_flg(itiran_wf_bean.getDefault_flg());
			if ("1".equals(itiran_wf_bean.getDefault_flg())) {
				form.setDefault_flg_checked(Function.getStringOfInt(i));
			}
			
			// パターンID
			toroku_wf_bean.setPattern_id(itiran_wf_bean.getPattern_id());
			
			// パターン名称
			toroku_wf_bean.setPattern_name_jp(itiran_wf_bean.getPattern_name_jp());
			
			// システム管理者専用フラグ
			toroku_wf_bean.setAdmin_senyo_flg(itiran_wf_bean.getAdmin_senyo_flg());
						
			// 削除ボタン
			toroku_wf_bean.setDelete_btn_flg(DELETE_BTN_HYOUJI_NO);
			if (chk_sateikaisya_cd(form.getAr_hanyo1(), itiran_wf_bean.getSateikaisya_cd())) {
				toroku_wf_bean.setDelete_btn_flg(DELETE_BTN_HYOUJI_YES);
			}			
			
			ar_gyoumu_huro_itiran.add(toroku_wf_bean);
			
		}

		// 業務フローパターン一覧
		form.setAr_gyoumu_huro_itiran(ar_gyoumu_huro_itiran);		
        
    }

    /**
     * ユーザ参照組織の登録を行う <br>
     * 
     * @param id
     * @return
     * @throws Exception
     */
    public void user(UserTorokuDbAcc dbacc) throws Exception {
		InputCheck inpChk = new InputCheck();

		List<TantouBean> userSoshikiList = form.getAr_meisai();
    	TantouBean tantouBean = new TantouBean();

		for(int i = 0; i < userSoshikiList.size(); i++){
			tantouBean = userSoshikiList.get(i);
			if(inpChk.isNullBlank(tantouBean.getHanyou2_cd())){
				// 会社権限：汎用2、汎用4に空白を設定
				tantouBean.setHanyou2_cd(SPACE);
				tantouBean.setHanyou4_cd("  ");
			} else if (inpChk.isNullBlank(tantouBean.getHanyou4_cd())){
				// 部門権限（SJ以外）：汎用4に空白を設定
				tantouBean.setHanyou4_cd("  ");
			}
    		dbacc.setInsertM0200(tantouBean);
    	}
    }

	/**
	 * メール配信先表示の判定
	 */
	public void haishinHyouji(){
		List gyoumu_huro_itiran = form.getAr_gyoumu_huro_itiran();
		WorkFlowListBean wf_bean;
		boolean chkFlg = false;
		
		// 業務フローにシステム区分01が含まれている場合のみ、配信先を表示する
		for(int i=0;i < gyoumu_huro_itiran.size();i++){
			wf_bean = (WorkFlowListBean)gyoumu_huro_itiran.get(i);

			if(GS.GSS.equals(wf_bean.getSystem_kbn())){
				chkFlg = true;
			}
		}
		
		if(!chkFlg){
			// 01が含まれていない場合
			List<HashMap<String, String>> ar_haishin = new ArrayList<HashMap<String, String>>();
			form.setAr_haishinsaki(ar_haishin);
			form.setHaishinHyoujiFlg("");
		} else {
			form.setHaishinHyoujiFlg("1");
		}
	}

	/**
	 * メール送信先削除(業務フロー)<br>
	 * 業務フローが削除されたとき、対象の業務フローと同じ査定会社のフローがないときに実施
	 * @param wf_bean 削除対象の業務フロー
	 * @throws Exception
	 */
	public void setHaishinsaki_WFdel(WorkFlowListBean wf_bean) throws Exception{

		// 選択した業務フローパターンを取得
		String delSystem_kbn = wf_bean.getSystem_kbn();			// システム区分
		String delSateikaisya_cd = wf_bean.getSateikaisya_cd();	// 査定会社コード

		// メール配信先
		HashMap<String, String> hm;
		List<HashMap<String, String>> ar_haishin = new ArrayList<HashMap<String, String>>();
		String systemKbn = "";
		String hanyou1 = "";

		// フローに一件も削除対象のパターン違いが存在しない場合、該当の配信先を削除する
		if(form.getAr_haishinsaki() != null && form.getAr_haishinsaki().size() != 0){
			for(int i=0;i < form.getAr_haishinsaki().size();i++){
				hm = form.getAr_haishinsaki().get(i);
				systemKbn = hm.get(SYSTEM_KBN_H);	// 配信先のシステム区分
				hanyou1 = hm.get(HANYOU1);			// 配信先の汎用1
				
				if ((!delSystem_kbn.equals(systemKbn) && !delSateikaisya_cd.equals(hanyou1))
						|| (delSystem_kbn.equals(systemKbn) && !delSateikaisya_cd.equals(hanyou1))) {
					// システム区分と査定会社が異なる、もしくはシステム区分が同じで査定会社が異なる場合
					ar_haishin.add(hm);
				}
			}
		}
		
		// ActionForm に配信先を格納
		form.setAr_haishinsaki(ar_haishin);
		// セッション格納する
		user_maintenance_bean.setHyoujiSoushinList(ar_haishin);
	}

	/**
	 * メール送信先追加/削除(担当組織選択画面より遷移時、査定会社PNのみ)
	 * @throws Exception
	 */
	public String setHaishinsakiStat() throws Exception{
		InputCheck inpChk = new InputCheck();

		// 作業用メール配信先
		HashMap<String, String> hm;
		List<HashMap<String, String>> add_haishinList = new ArrayList<HashMap<String, String>>();
		// 現在の配信リスト
		List<HashMap<String, String>> mailHaishinList = form.getAr_haishinsaki();
		// 担当組織一覧に査定会社PNがある場合
		List<TantouBean> add_haishinPNList = new ArrayList<TantouBean>();
		TantouBean add_haishinPN = new TantouBean();

		// PN会社権限設定フラグ
		boolean checkKaisya_PN = false;

		// 対象ユーザの担当組織一覧
		List<TantouBean> taiUserSoshikiList = form.getAr_meisai();
		TantouBean taiSoshiki = new TantouBean();
		String taiSystemKbn = null;		// 対象システム区分
		String taiHanyou1 = null;			// 対象ユーザ汎用1コード
		String taiHanyou2 = null;			// 対象ユーザ汎用2コード

		
		String ken_Hanyou1 = user_maintenance_bean.getKen_Hanyou1();	// 汎用1（担当組織選択検索条件）

		if(inpChk.isNullBlank(ken_Hanyou1) || (!inpChk.isNullBlank(ken_Hanyou1) && GS.SATEIKAISYA_PN.equals(ken_Hanyou1))){
			// 担当組織一覧に査定会社PNを会社単位で設定しているか
			for(int i = 0 ; taiUserSoshikiList.size() > i ; i++){
				taiSoshiki = taiUserSoshikiList.get(i);
				taiSystemKbn = taiSoshiki.getSystem_kbn();
				taiHanyou1 = taiSoshiki.getHanyou1_cd();
				taiHanyou2 = taiSoshiki.getHanyou2_cd();

				if(GS.GSS.equals(taiSystemKbn) && GS.SATEIKAISYA_PN.equals(taiHanyou1) && inpChk.isNullBlank(taiHanyou2)){
					// 担当組織一覧に査定会社PNを会社単位で設定している場合
					checkKaisya_PN = true;
					add_haishinPNList.add(taiSoshiki);
					break;
				} else if (GS.GSS.equals(taiSystemKbn) && GS.SATEIKAISYA_PN.equals(taiHanyou1) && !inpChk.isNullBlank(taiHanyou2)){
					// 担当組織一覧に査定会社PNを部門単位で設定している場合
					add_haishinPNList.add(taiSoshiki);
				}
			}

			String mailSystemKbn = null;
			String mailHanyou1 = null;
			String mailHanyou2 = null;
	
			if(add_haishinPNList != null && add_haishinPNList.size() != 0 ){
				// 担当組織一覧に査定会社PNあり
				if(mailHaishinList == null){
					// 現在の配信リストがnullの時、初期化する
					mailHaishinList = new ArrayList<HashMap<String, String>>();
				}
				
				for(int n = 0 ; mailHaishinList.size() > n ; n++){
					hm = new HashMap<String, String>();
					hm = mailHaishinList.get(n);
					mailSystemKbn = hm.get(SYSTEM_KBN_H);
					mailHanyou1 = hm.get(HANYOU1);
					mailHanyou2 = hm.get(HANYOU2);
	
					if(checkKaisya_PN){
						if(GS.GSS.equals(mailSystemKbn) && GS.SATEIKAISYA_PN.equals(mailHanyou1) && !inpChk.isNullBlank(mailHanyou2)){
							// 担当組織一覧にPNが会社単位あり、メール配信先にPNが部門単位あり：何もしない
							form.setAr_haishinsaki(mailHaishinList);
							// セッション格納する
							user_maintenance_bean.setHyoujiSoushinList(mailHaishinList);
	
							form.setHaishinHyoujiId(null);
	
							return GS.OS7107;
						}
					}
					if(!GS.SATEIKAISYA_PN.equals(mailHanyou1)){
						// システム区分01かつ査定会社PN以外の権限を追加する
						add_haishinList.add(hm);
					}
				}
	
				if(checkKaisya_PN){
					
					// 会社権限を追加する
					hm = new HashMap<String, String>();
					add_haishinPN = add_haishinPNList.get(0);
					//id
					hm.put(ID, Function.getStringOfInt(add_haishinList.size()));
					//システム区分
					hm.put(SYSTEM_KBN_H, add_haishinPN.getSystem_kbn());
	
					//汎用1フラグ
					hm.put(HANYOU1FLG, "1");
					//汎用1コード
					hm.put(HANYOU1, add_haishinPN.getHanyou1_cd());
					//汎用1名
					hm.put(HANYOU1_NM, add_haishinPN.getHanyou1_nm());
					//汎用2フラグ（チェックオフ）
					hm.put(HANYOU2FLG, "0");
					//汎用2コード（未設定）
					hm.put(HANYOU2, "");
					//汎用2名（未設定）
					hm.put(HANYOU2_NM, "");
					//汎用3コード
					hm.put(HANYOU3FLG, "0");
					//汎用3名（未設定）
					hm.put(HANYOU3, "");
					//汎用3フラグ（未設定）
					hm.put(HANYOU3_NM, "");
	
					// 配列に取得情報を格納
					add_haishinList.add(hm);
	
				} else {
					// 査定会社PNの権限を追加する
					for(int m = 0 ;add_haishinPNList.size() > m ; m++){
						hm = new HashMap<String, String>();
						add_haishinPN = add_haishinPNList.get(m);
	
						//id
						hm.put(ID, Function.getStringOfInt(add_haishinList.size()));
						//システム区分
						hm.put(SYSTEM_KBN_H, add_haishinPN.getSystem_kbn());
	
						//汎用1フラグ
						hm.put(HANYOU1FLG, "1");
						//汎用1コード
						hm.put(HANYOU1, add_haishinPN.getHanyou1_cd());
						//汎用1名
						hm.put(HANYOU1_NM, add_haishinPN.getHanyou1_nm());
						//汎用2フラグ
						hm.put(HANYOU2FLG, "1");
						//汎用2コード
						hm.put(HANYOU2, add_haishinPN.getHanyou2_cd());
						//汎用2名
						hm.put(HANYOU2_NM, add_haishinPN.getHanyou2_nm());
						//汎用3コード
						hm.put(HANYOU3FLG, "0");
						//汎用3名（未設定）
						hm.put(HANYOU3, "");
						//汎用3フラグ（未設定）
						hm.put(HANYOU3_NM, "");
	
						// 配列に取得情報を格納
						add_haishinList.add(hm);
					}
				}
				// ActionForm に配信先を格納
				form.setAr_haishinsaki(add_haishinList);
				// セッション格納する
				user_maintenance_bean.setHyoujiSoushinList(add_haishinList);
	
			} else {
				
				// 担当組織一覧に査定会社PNなし：何もしない
				for(int n = 0 ; mailHaishinList.size() > n ; n++){
					hm = new HashMap<String, String>();
					hm = mailHaishinList.get(n);
					mailSystemKbn = hm.get(SYSTEM_KBN_H);
					mailHanyou1 = hm.get(HANYOU1);
					mailHanyou2 = hm.get(HANYOU2);
	
					if(!GS.SATEIKAISYA_PN.equals(mailHanyou1)){
						// システム区分01かつ査定会社PN以外の権限を追加する
						add_haishinList.add(hm);
					}
				}
				form.setAr_haishinsaki(add_haishinList);
				// セッション格納する
				user_maintenance_bean.setHyoujiSoushinList(add_haishinList);
			}
		}
		form.setHaishinHyoujiId(null);

		return GS.OS7107;
	}

	

	/**
	 * メール送信先選択からの再表示 <br>
	 * 
	 * @return 画面ID
	 */
	public String reMeilSentaku() throws Exception{
		// チェック判断フラグに'1'：チェック対象を設定する 
		form.setChk_handan_flg("1");

		InputCheck inpChk = new InputCheck();
		// ユーザマスタ登録画面のメール配信先
		List<HashMap<String, String>> ar_haishin =form.getAr_haishinsaki();
		// メール送信先選択画面から取得したメール配信先
		List<HashMap<String, String>> soushinList = user_maintenance_bean.getSoushinList();
		HashMap<String, String> hm1; 
		HashMap<String, String> hm2;
		String han2Flg;
		String han3Flg;
		
		if(soushinList != null && soushinList.size() != 0){
			for(int i = 0; i < soushinList.size(); i++){
				// 現配信先の末尾に追加

				hm1 = new HashMap<String, String>();
				// メール送信先選択画面から取得したメール配信先
				hm1 = soushinList.get(i);
				hm2 = new HashMap<String, String>();
				
				//id
				if(ar_haishin == null || ar_haishin.size() == 0){
					hm2.put(ID, "0");
				} else {
					hm2.put(ID, Function.getStringOfInt(ar_haishin.size()));
				}
				//システム区分
				hm2.put(SYSTEM_KBN_H, hm1.get(SYSTEM_KBN_H));

				//汎用1フラグ
				han2Flg = hm1.get("hanyou2");
				if(inpChk.isNullBlank(han2Flg)){
					hm2.put(HANYOU1FLG, "1");
				} else {
					hm2.put(HANYOU1FLG, "0");
				}
				//汎用1コード
				hm2.put(HANYOU1, hm1.get("hanyou1"));
				//汎用1名
				hm2.put(HANYOU1_NM, hm1.get("hanyou1_nm"));
				//汎用2フラグ
				han3Flg = hm1.get("hanyou3");
				if(inpChk.isNullBlank(han3Flg) && !inpChk.isNullBlank(han2Flg)){
					hm2.put(HANYOU2FLG, "1");
				} else {
					hm2.put(HANYOU2FLG, "0");
				}
				//汎用2コード
				hm2.put(HANYOU2, hm1.get("hanyou2"));
				//汎用2名（未設定）
				hm2.put(HANYOU2_NM, hm1.get("hanyou2_nm"));
				//汎用3コード
				if(inpChk.isNullBlank(han3Flg)){
					hm2.put(HANYOU3FLG, "0");
				} else {
					hm2.put(HANYOU3FLG, "1");
				}
				//汎用3名（未設定）
				hm2.put(HANYOU3, hm1.get("hanyou3"));
				//汎用3フラグ（未設定）
				hm2.put(HANYOU3_NM, hm1.get("hanyou3_nm"));
				
				if(!this.reMeilSentakuChk(hm2,null)){
					// 重複が一件でもあればエラー
					List<String> msgList = new ArrayList<String>();
					msgList.add(GL.ERR_HANEI);
					appContext.setMsgCode(msgList);
					
					return GS.OS7107;
				}

				// 配列に取得情報を格納
				ar_haishin.add(hm2);
			}
			// ActionForm に配信先を格納
			form.setAr_haishinsaki(ar_haishin);
			// セッション格納する
			user_maintenance_bean.setHyoujiSoushinList(ar_haishin);

		}

		return GS.OS7107;
	}
	
	/**
	 * メール配信先の削除
	 * 
	 * @return 画面ID
	 */
	public String delHaishinsaki(){
		// チェック判断フラグに'1'：チェック対象を設定する 
		form.setChk_handan_flg("1");

		List<HashMap<String, String>> ar_haishin =form.getAr_haishinsaki();
		String delNo = form.getHaishinDel(); // 削除対象の配信先No

		ar_haishin.remove(Function.getValueOfIntC(delNo));
		form.setAr_haishinsaki(ar_haishin);
		form.setHaishinDel(null);
		return GS.OS7107;
	}
	
	
	/**
	 * メール送信先選択からの再表示（重複チェック） <br>
	 * @param sentakuHM
	 * @param ar_haishin
	 * @return
	 */
	public boolean reMeilSentakuChk(HashMap<String, String> sentakuHM,List<HashMap<String, String>> ar_haishin){
		boolean chkans = true; // 正常
		InputCheck inpChk = new InputCheck();
		
		// これから追加する配信先
		String hanyou1_s = Function.trim(sentakuHM.get(HANYOU1));
		String hanyou2_s = Function.trim(sentakuHM.get(HANYOU2));
		String hanyou3_s = Function.trim(sentakuHM.get(HANYOU3));

		// すでに設定済みの配信先
		String hanyou1_f = "";
		String hanyou2_f = "";
		String hanyou3_f = "";
		if(ar_haishin == null){
			ar_haishin = form.getAr_haishinsaki();	
		}
		HashMap<String, String> hm;
		// フォームに設定済みの配信先分チェックする
		for(int i=0 ;i < ar_haishin.size(); i++){

			hm = new HashMap<String, String>();
			hm = ar_haishin.get(i);
			hanyou1_f = hm.get(HANYOU1);
			hanyou2_f = Function.trim(hm.get(HANYOU2));
			hanyou3_f = Function.trim(hm.get(HANYOU3));

			if(inpChk.isNullBlank(hanyou2_s)){
				// 会社単位の追加の場合
				if (hanyou1_s.equals(hanyou1_f)){
					// すでに同じ会社が配信先にある場合はエラー
					chkans = false;
					break;
				}
			}else if(inpChk.isNullBlank(hanyou3_s)){
				// 部門単位の追加の場合
				if (hanyou1_s.equals(hanyou1_f) && inpChk.isNullBlank(hanyou2_f)){
					// すでに同じ会社が配信先にある場合はエラー
					chkans = false;
					break;
				}else if (hanyou1_s.equals(hanyou1_f) && hanyou2_s.equals(hanyou2_f)){
					// すでに同じ部門が配信先にある場合はエラー
					chkans = false;
					break;
				}
			}else{
				// 部単位の追加の場合
				if (hanyou1_s.equals(hanyou1_f) && inpChk.isNullBlank(hanyou2_f)){
					// すでに同じ会社が配信先にある場合はエラー
					chkans = false;
					break;
				}else if (hanyou1_s.equals(hanyou1_f) && hanyou2_s.equals(hanyou2_f) && inpChk.isNullBlank(hanyou3_f)){
					// すでに同じ部門が配信先にある場合はエラー
					chkans = false;
					break;
				}else if (hanyou1_s.equals(hanyou1_f) && hanyou2_s.equals(hanyou2_f) && hanyou3_s.equals(hanyou3_f)){
					// すでに同じ部が配信先にある場合はエラー
					chkans = false;
					break;
				}

			}
		}
		return chkans;
	}
	
	/**
	 * 一覧表示
	 * @throws Exception
	 */
	public void usertantoHyouji() throws Exception{
		dbacc.getLoginUserSoshikiList();	// ログインユーザの権限取得
		dbacc.getMeisai();					// 対象ユーザの権限取得

		// 対象ユーザの担当権限がない場合は、処理を行わない
		if(form.getAr_meisai() != null && form.getAr_meisai().size() != 0){
			InputCheck inpChk = new InputCheck();

			// 対象ユーザの担当組織一覧
			List<TantouBean> taiUserSoshikiList = form.getAr_meisai();
			TantouBean taiSoshiki = new TantouBean();
			String taiHanyou1_cd = null;	// 対象ユーザ汎用1コード
			String taiHanyou2_cd = null;	// 対象ユーザ汎用2コード
			String taiHanyou4_cd = null;	// 対象ユーザ汎用4コード

			// ログインユーザの担当組織一覧
			List<HashMap<String, String>> logUserSoshikiList = form.getAr_loginUserSoshiki();
			HashMap<String, String> logSoshiki = new HashMap<String, String>();
			String logHanyou1_cd = null;	// ログインユーザ汎用1コード
			String logHanyou2_cd = null;	// ログインユーザ汎用2コード
			String logHanyou4_cd = null;	// ログインユーザ汎用3コード

			
			for(int i = 0 ;taiUserSoshikiList.size() > i ; i++){
				// 対象ユーザの担当組織の取得
				taiSoshiki = taiUserSoshikiList.get(i);
				taiHanyou1_cd = taiSoshiki.getHanyou1_cd();
				taiHanyou2_cd = taiSoshiki.getHanyou2_cd();
				taiHanyou4_cd = taiSoshiki.getHanyou4_cd();
				// 処理対象外フラグ（初期値：対象外）
				taiSoshiki.setTaisyogaiFlg(GS.ON);

				for(int n = 0 ;logUserSoshikiList.size() > n ; n++ ){
					// ログインユーザの担当組織の取得
					logSoshiki = logUserSoshikiList.get(n);
					logHanyou1_cd = logSoshiki.get(HANYOU1);
					logHanyou2_cd = logSoshiki.get(HANYOU2);
					logHanyou4_cd = logSoshiki.get(HANYOU4);

					if(logHanyou1_cd.equals(taiHanyou1_cd)){
						// 査定会社が同じ場合
						if(inpChk.isNullBlank(logHanyou2_cd) && inpChk.isNullBlank(taiHanyou2_cd)){
							// 対象・ログインユーザが同じ会社権限で処理対象
							taiSoshiki.setTaisyogaiFlg(GS.OFF);
							
						} else if(inpChk.isNullBlank(logHanyou2_cd) && !inpChk.isNullBlank(taiHanyou2_cd)){
								// ログインユーザが会社権限かつ対象ユーザが部門・本部権限で処理対象
								taiSoshiki.setTaisyogaiFlg(GS.OFF);
								
						} else if(logHanyou2_cd.equals(taiHanyou2_cd)){
							// 部門が同じ
							if(GS.SATEIKAISYA_SJ.equals(taiHanyou1_cd) && logHanyou4_cd.equals(taiHanyou4_cd)){
								// 本部が同じで処理対象（双日本社のみ）
								taiSoshiki.setTaisyogaiFlg(GS.OFF);
								
							} else if(!GS.SATEIKAISYA_SJ.equals(taiHanyou1_cd)) {
								// 部門が同じで処理対象（双日本社以外）
								taiSoshiki.setTaisyogaiFlg(GS.OFF);
							}
						}
					}
					if(GS.OFF.equals(taiSoshiki.getTaisyogaiFlg())){
						// 処理対象外フラグが処理対象の場合、次のチェックを行う
						break;
					}
				}
				// 対象ユーザの担当組織一覧をセット
				form.getAr_meisai().set(i,taiSoshiki);
			}
		}
		
	}

	/**
	 * 業務フローの重複チェック<br>
	 * 業務フローを削除した場合、削除した業務フローと同じ査定会社が一覧に含まれるかどうかを返す
	 * @param wf_bean 削除対象の業務フロー
	 * @return true:削除フローと同じ査定会社あり、false:なし
	 */
	public boolean workFlowListcheck(WorkFlowListBean wf_bean) {
		// 選択した業務フローパターンを取得
		String delSystem_kbn = wf_bean.getSystem_kbn();			// システム区分
		String delSateikaisya_cd = wf_bean.getSateikaisya_cd();	// 査定会社コード
		String delPattern_id = wf_bean.getPattern_id();			// パターンID

		List ar_gh_itiran = form.getAr_gyoumu_huro_itiran();
		WorkFlowListBean wfList;
		boolean delFlg = false; // 削除フローと同じ査定会社なし

		for(int i=0;i < ar_gh_itiran.size();i++){
			wfList = (WorkFlowListBean)ar_gh_itiran.get(i);

			if (delSystem_kbn.equals(wfList.getSystem_kbn())
					&& delSateikaisya_cd.equals(wfList.getSateikaisya_cd())
					&& !delPattern_id.equals(wfList.getPattern_id())) {
				// 業務フローにパターン違いが一つでもある場合
				delFlg = true;
				break;
			}
		}
		return delFlg;
	}

	
	/**
	 * 担当組織 対象外チェック(業務フロー削除)<br>
	 * 業務フローが削除されたとき、対象の業務フローと同じ査定会社のフローがないときに実施<br>
	 * 削除対象の業務フローと同じ査定会社の業務フローがない場合に、担当組織を削除できるか判定
	 * @param wf_bean 削除対象の業務フロー
	 * @return true:業務フローと同じ査定会社を削除可能、false:削除不可
	 * @throws Exception 
	 */
	public boolean tantougaicheck(WorkFlowListBean wf_bean) throws Exception {
		// 選択した業務フローパターンを取得
		String delSystem_kbn = wf_bean.getSystem_kbn();			// システム区分
		String delSateikaisya_cd = wf_bean.getSateikaisya_cd();	// 査定会社コード
		boolean chkFlg = true; // 削除不可の担当組織なし

		// 対象ユーザの担当組織一覧
		TantouBean taiSoshiki = new TantouBean();
		String systemKbn = null;	// 対象ユーザシステム区分
		String hanyou1 = null;		// 対象ユーザ汎用1コード
		String taisyogaiFlg = null;	// 対象ユーザ対象外フラグ

		// 該当の担当組織を削除する
		if(form.getAr_meisai() != null && form.getAr_meisai().size() != 0){
			for(int i=0;i < form.getAr_meisai().size();i++){
				taiSoshiki = (TantouBean)form.getAr_meisai().get(i);
				systemKbn = taiSoshiki.getSystem_kbn();		// 担当組織システム区分
				hanyou1 = taiSoshiki.getHanyou1_cd();		// 担当組織の汎用1
				taisyogaiFlg = taiSoshiki.getTaisyogaiFlg();// 担当組織の対象外フラグ
				
				if (delSystem_kbn.equals(systemKbn)
						&& delSateikaisya_cd.equals(hanyou1)
						&& GS.ON.equals(taisyogaiFlg)) {
					// システム区分と査定会社同じで対象外フラグがONの時エラー
					List<String> msgList = new ArrayList<String>();
					msgList.add(GL.ERR_NOTDELETE);
					msgList.add(GL.OS7107_FUKATANTOSOSHIKI);
					appContext.setMsgCode(msgList);

					chkFlg = false;
					break;
				}
			}
		}
		return chkFlg;
	}

	/**
	 * 担当組織削除(業務フロー)<br>
	 * 業務フローが削除されたとき、対象の業務フローと同じ査定会社のフローがないときに実施
	 * @param wf_bean 削除対象の業務フロー
	 */
	public void setTantoSoshiki_WFdel(WorkFlowListBean wf_bean) {
		// 選択した業務フローパターンを取得
		String delSystem_kbn = wf_bean.getSystem_kbn();			// システム区分
		String delSateikaisya_cd = wf_bean.getSateikaisya_cd();	// 査定会社コード

		// 対象ユーザの担当組織一覧
		List<TantouBean> taiUserSoshikiList = new ArrayList<TantouBean>();
		TantouBean taiSoshiki = new TantouBean();
		String systemKbn = null;	// 対象ユーザシステム区分
		String hanyou1 = null;		// 対象ユーザ汎用1コード

		// 該当の担当組織を削除する
		if(form.getAr_meisai() != null && form.getAr_meisai().size() != 0){
			for(int i=0;i < form.getAr_meisai().size();i++){
				taiSoshiki = (TantouBean)form.getAr_meisai().get(i);
				systemKbn = taiSoshiki.getSystem_kbn();		// 担当組織システム区分
				hanyou1 = taiSoshiki.getHanyou1_cd();		// 担当組織の汎用1
				
				if ((!delSystem_kbn.equals(systemKbn) && !delSateikaisya_cd.equals(hanyou1))
						|| (delSystem_kbn.equals(systemKbn) && !delSateikaisya_cd.equals(hanyou1))) {
					// システム区分と査定会社が異なる、もしくはシステム区分が同じで査定会社が異なる場合
					taiUserSoshikiList.add(taiUserSoshikiList.size(), taiSoshiki);
				}
			}
		}
		// ActionForm に配信先を格納
		form.setAr_meisai(taiUserSoshikiList);
	}

	/**
	 * 担当組織選択画面からの再表示<br>
	 * @return 画面ID
	 * @throws Exception
	 */
	public String reTantoSoshiki() throws Exception{
		// チェック判断フラグに'1'：チェック対象を設定する 
		form.setChk_handan_flg("1");

		// 業務フローとのチェック
		if(this.tantoSoshikiWorkFlowChk()){
			// 担当組織重複チェック
			if(this.tantoSoshikiChk()){
				// メール配信先が表示ありの場合、メール配信先の再設定をする
				if("1".equals(form.getHaishinHyoujiFlg())){
					this.setHaishinsakiStat();
				}
			} else {
				return GS.OS7107; // チェックNG
			}
		} else {
			return GS.OS7107; // チェックNG
		}
		return GS.OS7107;
	}

	/**
	 * 担当組織選択画面からの再表示時のチェック<br>
	 * 担当組織選択画面で選択した担当組織の査定会社と同じ業務フローパターンが
	 * あるかどうかをチェックする<br>
	 * @return true:チェックOK、false:チェックNG
	 * @throws Exception
	 */
	public boolean tantoSoshikiWorkFlowChk() throws Exception{
		InputCheck inpChk = new InputCheck();
		boolean retAns = false; // チェック結果 NG
		
		// 担当組織選択画面で選択した担当組織を取得
		List<TantouBean> addTantoSoshikiList = user_maintenance_bean.getTantoSoshikiList();
		String ken_Hanyou1 = user_maintenance_bean.getKen_Hanyou1();	// 汎用1（担当組織選択検索条件）

		if(addTantoSoshikiList != null && addTantoSoshikiList.size() != 0){
			// 担当組織選択画面から追加する担当組織がある場合
			
			List<WorkFlowListBean> workFlowList = new ArrayList<WorkFlowListBean>(form.getAr_gyoumu_huro_itiran());
			WorkFlowListBean workFlow = new WorkFlowListBean();

			TantouBean addTantoSoshiki = new TantouBean();

			if(inpChk.isNullBlank(ken_Hanyou1)){
				// 担当組織選択画面で全件検索して、担当組織を選択した
				for(int i = 0 ; addTantoSoshikiList.size() > i ; i++){
					// ポップアップから追加する担当組織分チェックを行う
					addTantoSoshiki = addTantoSoshikiList.get(i);
					retAns = false; // 初期化

					for(int n = 0; workFlowList.size() > n ; n++){
						// 業務フロー分チェックを行う
						workFlow = workFlowList.get(n);

						if(workFlow.getSateikaisya_cd().equals(addTantoSoshiki.getHanyou1_cd())){
							// 同じだったらチェックOK
							retAns = true;
							break;
						}
					}
					if(!retAns){
						// 業務フローの査定会社と同じ査定会社ではない場合エラー
						List<String> msgList = new ArrayList<String>();
						msgList.add(GL.ERR_SELECT2);
						msgList.add(GL.OS7107_ADDTANTOSOSHIKI);
						msgList.add(GL.OS7107_GYOUMU_HURO);
						appContext.setMsgCode(msgList);
						break;
					}
				}

			} else {
				// 担当組織選択画面で会社もしくは部門検索して、担当組織を選択した
				for(int i = 0; workFlowList.size() > i ; i++){
					// 業務フロー分チェックを行う
					workFlow = workFlowList.get(i);

					if(workFlow.getSateikaisya_cd().equals(ken_Hanyou1)){
						// 同じだったらチェックOKとして返却
						retAns = true;
						break;
					}
				}
				if(!retAns){
					// 業務フローの査定会社と同じ検索条件ではない場合エラー
					List<String> msgList = new ArrayList<String>();
					msgList.add(GL.ERR_SELECT2);
					msgList.add(GL.OS7107_ADDTANTOSOSHIKI);
					msgList.add(GL.OS7107_GYOUMU_HURO);
					appContext.setMsgCode(msgList);
				}
			}
		} else {
			// 追加0件の場合は担当組織削除のため、チェックOK
			retAns = true;
		}
		return retAns;
	}

	/**
	 * 担当組織選択画面からの再表示<br>
	 * 担当組織選択画面で選択した担当組織が既に画面に設定されているかどうかをチェックする<br>
	 * @return true:チェックOK、false:チェックNG
	 * @throws Exception
	 */
	public boolean tantoSoshikiChk() throws Exception{
		InputCheck inpChk = new InputCheck();

		// 画面.担当組織一覧を取得
		List<TantouBean> oldUserSoshikiList = form.getAr_meisai();
		List<TantouBean> newUserSoshikiList = new ArrayList<TantouBean>();
		TantouBean oldSoshiki = new TantouBean();
		String oldSystem_kbn = null;	// 対象ユーザシステム区分
		String oldHanyou1_cd = null;	// 対象ユーザ汎用1コード
		String oldHanyou2_cd = null;	// 対象ユーザ汎用2コード

		// 担当組織選択画面で選択した担当組織を取得
		List<TantouBean> addTantoSoshikiList = user_maintenance_bean.getTantoSoshikiList();
		String ken_System_Kbn = user_maintenance_bean.getKen_System_Kbn();// システム区分（担当組織選択検索条件）
		String ken_Hanyou1 = user_maintenance_bean.getKen_Hanyou1();	// 汎用1（担当組織選択検索条件）
		String ken_Hanyou2 = user_maintenance_bean.getKen_Hanyou2();	// 汎用2（担当組織選択検索条件）

		TantouBean addSoshiki = new TantouBean();
		String addHanyou1_cd = null;	// 選択画面汎用1コード
		String addHanyou2_cd = null;	// 選択画面汎用2コード

		if(oldUserSoshikiList != null && oldUserSoshikiList.size() != 0 ){
			// 画面.担当組織一覧が1件以上ある場合
			if(addTantoSoshikiList == null){
				addTantoSoshikiList = new ArrayList<TantouBean>();
			}

			if(inpChk.isNullBlank(ken_Hanyou1) && inpChk.isNullBlank(ken_Hanyou2)){
				// 検索条件なしの場合、担当組織選択画面で選択した担当組織を画面.担当組織一覧に設定する
				String oldTaiFlg = "";
				for(int n = 0 ; oldUserSoshikiList.size() > n ; n++){
					// 画面.担当組織一覧分繰り返す
					oldSoshiki = oldUserSoshikiList.get(n);
					oldTaiFlg = oldSoshiki.getTaisyogaiFlg();
					
					if(GS.ON.equals(oldTaiFlg)){
							List<String> msgList = new ArrayList<String>();
							msgList.add(GL.ERR_HANEI);
							appContext.setMsgCode(msgList);
							return false; // チェックNG
					}
				}
				oldUserSoshikiList = new ArrayList<TantouBean> (addTantoSoshikiList);
				form.setAr_meisai(oldUserSoshikiList);

			} else if(!inpChk.isNullBlank(ken_System_Kbn) && !inpChk.isNullBlank(ken_Hanyou1) && inpChk.isNullBlank(ken_Hanyou2)){
				// 会社検索の場合

				for(int n = 0 ; oldUserSoshikiList.size() > n ; n++){
					// 画面.担当組織一覧分繰り返す
					oldSoshiki = oldUserSoshikiList.get(n);
					oldSystem_kbn = oldSoshiki.getSystem_kbn();
					oldHanyou1_cd = oldSoshiki.getHanyou1_cd();
					
					if(oldSystem_kbn.equals(ken_System_Kbn) && oldHanyou1_cd.equals(ken_Hanyou1)){
						// 検索条件と同じ担当組織
						if(GS.ON.equals(oldSoshiki.getTaisyogaiFlg())){
							// 会社権限でかつ処理対象外の場合エラー
							List<String> msgList = new ArrayList<String>();
							msgList.add(GL.ERR_HANEI);
							appContext.setMsgCode(msgList);
							
							return false; // チェックNG
						} else {
							continue;
						}
					}else{
						// 会社違いは設定
						newUserSoshikiList.add(oldSoshiki);
					}
				}
				
				for(int m = 0 ;addTantoSoshikiList.size() > m ; m++){
					// 画面.担当組織一覧の末尾に担当組織選択画面で選択した担当組織を再設定する
					addSoshiki = addTantoSoshikiList.get(m);
					newUserSoshikiList.add(addSoshiki);
				}
				// 画面.担当組織一覧に再設定する
				form.setAr_meisai(newUserSoshikiList);
			} else if(!inpChk.isNullBlank(ken_Hanyou1) && !inpChk.isNullBlank(ken_Hanyou2)){
				// 部門検索の場合

				boolean checkSoshiki = false;
				boolean checkHokabumon = false;

				// 重複エラーチェック：担当組織選択画面で会社単位で選択しているか
				for(int i = 0 ; addTantoSoshikiList.size() > i ; i++){
					addSoshiki = addTantoSoshikiList.get(i);
					addHanyou1_cd = addSoshiki.getHanyou1_cd();
					addHanyou2_cd = addSoshiki.getHanyou2_cd();

					if(addHanyou1_cd.equals(ken_Hanyou1) && inpChk.isNullBlank(addHanyou2_cd)){
						// 担当組織選択画面で会社単位で選択している場合、チェック対象
						checkSoshiki = true;
					}
				}
				for(int n = 0 ; oldUserSoshikiList.size() > n ; n++){
					// 画面.担当組織一覧分繰り返す
					oldSoshiki = oldUserSoshikiList.get(n);
					oldSystem_kbn = oldSoshiki.getSystem_kbn();
					oldHanyou1_cd = oldSoshiki.getHanyou1_cd();
					oldHanyou2_cd = oldSoshiki.getHanyou2_cd();

					if(oldSystem_kbn.equals(ken_System_Kbn) && oldHanyou1_cd.equals(ken_Hanyou1) && Function.strEquals(oldHanyou2_cd,ken_Hanyou2)){
						// 検索条件と同じ担当組織はあとで設定する
						continue;
					} else if(oldHanyou1_cd.equals(ken_Hanyou1) && !Function.strEquals(oldHanyou2_cd,ken_Hanyou2)) {
						// 検索条件と異なる部門の場合
						checkHokabumon = true;
						if(inpChk.isNullBlank(oldHanyou2_cd) && GS.ON.equals(oldSoshiki.getTaisyogaiFlg())){
							// 会社権限でかつ処理対象外の場合エラー
							List<String> msgList = new ArrayList<String>();
							msgList.add(GL.ERR_HANEI);
							appContext.setMsgCode(msgList);

							return false; // チェックNG

						} else if(checkSoshiki && inpChk.isNullBlank(oldHanyou2_cd)){
							// 同じ会社を再設定のため、重複でエラー
							List<String> msgList = new ArrayList<String>();
							msgList.add(GL.ERR_HANEI);
							appContext.setMsgCode(msgList);

							return false; // チェックNG
						} else if(inpChk.isNullBlank(oldHanyou2_cd)){
							// 会社は後で設定する
							continue;
						}
					}
					// 会社・部門違いは設定
					newUserSoshikiList.add(oldSoshiki);
					continue;
				}

				for(int m = 0 ;addTantoSoshikiList.size() > m ; m++){
					// 画面.担当組織一覧の末尾に担当組織選択画面で選択した担当組織を再設定する
					addSoshiki = addTantoSoshikiList.get(m);
					if(checkHokabumon && inpChk.isNullBlank(addSoshiki.getHanyou2_cd())){
						// 検索条件と異なる部門がある、かつ会社権限を追加はエラー
						List<String> msgList = new ArrayList<String>();
						msgList.add(GL.ERR_HANEI);
						appContext.setMsgCode(msgList);
						return false; // チェックNG
					}
					newUserSoshikiList.add(addSoshiki);
				}
				// 画面.担当組織一覧に再設定する
				form.setAr_meisai(newUserSoshikiList);
			}
		} else {
				// 画面.担当組織一覧が0件の場合
				if(addTantoSoshikiList != null && addTantoSoshikiList.size() != 0){
					// 担当組織選択画面で選択した担当組織がある場合、担当組織一覧にそのまま設定
					oldUserSoshikiList = new ArrayList<TantouBean> (addTantoSoshikiList);
					form.setAr_meisai(oldUserSoshikiList);
				}
		}

		return true; // チェックOK
	}

	/**
	 * メール配信先一覧 権限チェック<br>
	 * @return true：チェックOK（メール配信先一覧0件も含む）、false：チェックNG
	 * @throws Exception
	 */
	public boolean chkMailListKengen() throws Exception{

		
		// 担当組織一覧
		List<TantouBean> tantoSoshikiList = form.getAr_meisai();
		TantouBean tantoSoshiki = new TantouBean();
		String tantoSystem_kbn = null;	// システム区分
		String tantoHanyou1_cd = null;	// 汎用1コード
		String tantoHanyou2_cd = null;	// 汎用2コード
		String tantoHonbu_cd = null;	// 汎用4（本部）コード

		// メール配信先一覧
		List<HashMap<String, String>> mailHaishinList = form.getAr_haishinsaki();

		InputCheck inpChk = new InputCheck();

		boolean kengenFlg_SJ = false; // 担当組織一覧にSJの会社権限の設定なし
		boolean kengenFlg_PN = false; // 担当組織一覧にPNの会社権限の設定なし
		ArrayList<String> bumonList_PN = new ArrayList<String>(); // PNの部門リスト
		ArrayList<String> bumonList_SJ = new ArrayList<String>(); // SJの部門リスト
		
		if((tantoSoshikiList == null || tantoSoshikiList.size() == 0) 
			&& (mailHaishinList != null && mailHaishinList.size() != 0)){
			// 担当組織0件、メール配信先1件以上
			return false;
		} else if(mailHaishinList == null || mailHaishinList.size() == 0) {
			// 担当組織1件以上、メール配信先0件
			return true;
		}

		for (int i = 0; tantoSoshikiList.size() > i; i++) {
			tantoSoshiki = tantoSoshikiList.get(i);
			tantoSystem_kbn = tantoSoshiki.getSystem_kbn();
			tantoHanyou1_cd = tantoSoshiki.getHanyou1_cd();
			tantoHanyou2_cd = tantoSoshiki.getHanyou2_cd();

			if(GS.GSS.equals(tantoSystem_kbn)) {
				if(GS.SATEIKAISYA_SJ.equals(tantoHanyou1_cd) && inpChk.isNullBlank(tantoHanyou2_cd)) {
					kengenFlg_SJ = true; // 担当組織一覧にSJの会社権限あり
				} else if (GS.SATEIKAISYA_PN.equals(tantoHanyou1_cd) && inpChk.isNullBlank(tantoHanyou2_cd)) {
					kengenFlg_PN = true; // 担当組織一覧にPNの会社権限あり
				} else if (GS.SATEIKAISYA_SJ.equals(tantoHanyou1_cd) && !inpChk.isNullBlank(tantoHanyou2_cd)) {
					bumonList_SJ.add(tantoHanyou2_cd); // 担当組織一覧にSJの本部権限あり
				} else if (GS.SATEIKAISYA_PN.equals(tantoHanyou1_cd) && !inpChk.isNullBlank(tantoHanyou2_cd)) {
					bumonList_PN.add(tantoHanyou2_cd); // 担当組織一覧にPNの部門権限あり
				}
			}
		}

		if(kengenFlg_SJ && kengenFlg_PN) {
			// SJ・PNともに担当組織一覧に会社権限あり:チェックしない
			return true;
		}

		this.sqlExec = appContext.getSqlExecuter();
		UserTorokuDbAcc dbacc = new UserTorokuDbAcc(sqlExec, log, appContext);
		HashMap<String, String> hm = new HashMap<String, String>();
		String mailSystemKbn = null;	// メール配信先システム区分
		String mailHanyou1 = null;		// メール配信先汎用1コード
		String mailHanyou2 = null;		// メール配信先汎用2コード
		String mailHanyou3 = null;		// メール配信先汎用3（部）コード

		ArrayList<String> honbuList = new ArrayList<String>();
		String honbu = null;
		int listNo;
		boolean retAns = false; // チェック結果 NG

		// ■担当組織一覧にSJ・PNの会社権限なしのときのチェック■
		for (int n = 0; mailHaishinList.size() > n; n++) {
			retAns = false; // チェック結果 NG

			hm = mailHaishinList.get(n);
			mailSystemKbn = hm.get(SYSTEM_KBN_H);
			mailHanyou1 = hm.get(HANYOU1);
			mailHanyou2 = hm.get(HANYOU2);
			mailHanyou3 = Function.trim(hm.get(HANYOU3));

			if ((kengenFlg_SJ && GS.SATEIKAISYA_SJ.equals(mailHanyou1))
					|| (kengenFlg_PN && GS.SATEIKAISYA_PN.equals(mailHanyou1))) {
				// 担当組織一覧に会社権限あり:チェックしない
				retAns = true;
			}

			if(!kengenFlg_SJ && GS.SATEIKAISYA_SJ.equals(mailHanyou1)) {
				if(bumonList_SJ == null || bumonList_SJ.size() == 0) {
					// 部門(本部)が担当組織一覧にない
					retAns = false;
					break;
				} else {
					// 部門(本部)が担当組織一覧にある
					listNo = bumonList_SJ.indexOf(mailHanyou2);

					if(listNo == -1) {
						// メール配信先の部門と同じ部門が担当組織一覧にない
						retAns = false;
						break;
					}
				}

				// SJ：メール配信先の設定値から、本部コードを取得する
				honbuList = dbacc.getHonbuCd(mailSystemKbn, mailHanyou1, mailHanyou2, mailHanyou3);

				if(honbuList == null || honbuList.size() == 0) {
					// 本部コードが空のときNG
					retAns = false;
					break;
				}

				if(inpChk.isNullBlank(mailHanyou3)) {
					// ■メール配信先を部門で設定
					for (int m = 0; tantoSoshikiList.size() > m; m++) {
						tantoSoshiki = tantoSoshikiList.get(m);
						tantoHanyou1_cd = tantoSoshiki.getHanyou1_cd();
						tantoHanyou2_cd = tantoSoshiki.getHanyou2_cd();
						tantoHonbu_cd = tantoSoshiki.getHanyou4_cd();

						if(mailHanyou1.equals(tantoHanyou1_cd) && mailHanyou2.equals(tantoHanyou2_cd)) {
							// 担当組織一覧、会社コードと部門コードが同じ場合、本部リストに存在するかチェック
							listNo = honbuList.indexOf(tantoHonbu_cd);

							if(listNo == -1) {
								// メール配信先の部門に紐づく本部が担当組織一覧にない
								retAns = false;
							} else {
								// 紐づく本部があるときは、リストから本部コードを削除
								honbuList.remove(listNo);
								retAns = true;
							}
						}
					}
					if(honbuList != null && honbuList.size() != 0) {
						// 本部コードが空ではないときNG
						retAns = false;
					}
				} else {
					// ■メール配信先を部で設定
					honbu = honbuList.get(0);

					for (int m = 0; tantoSoshikiList.size() > m; m++) {
						tantoSoshiki = tantoSoshikiList.get(m);
						tantoHanyou1_cd = tantoSoshiki.getHanyou1_cd();
						tantoHanyou2_cd = tantoSoshiki.getHanyou2_cd();
						tantoHonbu_cd = tantoSoshiki.getHanyou4_cd();

						if(mailHanyou1.equals(tantoHanyou1_cd)
								&& mailHanyou2.equals(tantoHanyou2_cd)
								&& honbu.equals(tantoHonbu_cd)) {
							// 本部コードが同じ配信先がある
							retAns = true;
							break;
						}
					}
				}
			} else if (!kengenFlg_PN && GS.SATEIKAISYA_PN.equals(mailHanyou1)) {
				// PN：査定会社コードがPNの場合、担当組織一覧の部門リストに存在するかチェック

				if(bumonList_PN == null || bumonList_PN.size() == 0) {
					// 部門が担当組織一覧にない
					retAns = false;
					break;
				}

				listNo = bumonList_PN.indexOf(mailHanyou2);

				if(listNo == -1) {
					// メール配信先の部門と同じ部門が担当組織一覧にない
					retAns = false;
				} else {
					// 同じ部門があるときは、リストからコードを削除
					bumonList_PN.remove(listNo);
					retAns = true;
				}
			}

			if(!retAns){
				break;	// チェックエラーの場合
			}
		}

		return retAns;
	}
}
