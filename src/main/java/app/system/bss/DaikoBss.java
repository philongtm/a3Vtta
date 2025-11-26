/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.UserBean;
import app.system.dbAcc.DaikoDbAcc;
import app.system.form.DaikoForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;

/**
 * OS7101_代行設定 ビジネスロジッククラス <br>
 */
public class DaikoBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private UserBean user_bean;				// ユーザ情報
    private DaikoForm form;					// 代行設定アクションフォーム

    private static final String SYSTEM_MANAGER			= "1";			// システム管理者
    private static final String FLG_DEL_UNDELETE			= "0";			// 未削除
    private static final String FLG_DEL_DELETED			= "1";			// 削除済

	private static final String ALL_GSS 					= "1";			// GSSのみ
	private static final String NONE_GSS 					= "2";			// GSSなし
	private static final String MIX_GSS 					= "3";			// GSS混在
	
    private static final int TANTO_LIST_ID_SQL02			= 1;			//SQL02の区分ID
    private static final int TANTO_LIST_ID_SQL03			= 2;			//SQL03の区分ID
    private static final int TANTO_LIST_ID_SQL05			= 3;			//SQL05の区分ID
    private static final int TANTO_LIST_ID_SQL06			= 4;			//SQL06の区分ID
    private static final int TANTO_LIST_ID_SQL07			= 5;			//SQL07の区分ID

    private static final int DAIKO_ICHIRAN_INIT			= 1;			//代行者一覧取得（初期表示時）
    private static final int HIDAIKO_ICHIRAN_INIT		= 2;			//被代行者一覧取得（初期表示時）
    private static final int DAIKO_ICHIRAN_NORMOL		= 3;			//代行者一覧取得（初期表示時以外）
    private static final int HIDAIKO_ICHIRAN_NORMOL		= 4;			//被代行者一覧取得（初期表示時以外）

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public DaikoBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        SessionData cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.form = (DaikoForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時) <br>
     * 
     * @param appContext AppContext
     * @return forward
     * @throws Exception Exception
     */
    public String executeInit() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DaikoDbAcc dbacc = new DaikoDbAcc(sqlExec, log, appContext);

        // 被代行者選択
    	if (SYSTEM_MANAGER.equals(user_bean.getComSystemManager_flg())) {
        	// ログインユーザがシステム管理者の場合
    		
    		// 入力/検索
    		form.setDisabledHidaiko(Boolean.FALSE);
    		form.setInHidaikosha(GS.EMPTY_CHARCTER);
    		// 担当者一覧を表示
    		dbacc.getTantoIchiran(TANTO_LIST_ID_SQL03);
    		form.setSelHidaikosha(GS.EMPTY_CHARCTER);
    		// 被代行者
    		form.setTxtHidaikosha(GS.EMPTY_CHARCTER);
    		form.setSelectedHidaikoshaId(GS.EMPTY_CHARCTER);
    	} else {
    		// ログインユーザがシステム管理者でない場合
    		
    		// ログインユーザの処理権限を取得する。
    		int cnt = dbacc.gelKengenCheck();
    		if (cnt == 0) {
				// いずれのフェーズでも承認権限を保持していない場合
    			
    			// 入力/検索
        		form.setDisabledHidaiko(Boolean.TRUE);
        		form.setInHidaikosha(GS.EMPTY_CHARCTER);
        		// 担当者一覧を表示
        		form.setAr_hidaiko_tanto(new ArrayList());
        		form.setSelHidaikosha(GS.EMPTY_CHARCTER);
        		// 被代行者
        		form.setTxtHidaikosha(user_bean.getComEmailAddr());
        		form.setSelectedHidaikoshaId(user_bean.getComUserId());
			} else {
				// いずれかのフェーズで承認権限を保持している場合
				
    			// 入力/検索
        		form.setDisabledHidaiko(Boolean.FALSE);
        		form.setInHidaikosha(GS.EMPTY_CHARCTER);
        		// 担当者一覧を表示
        		dbacc.getTantoIchiran(TANTO_LIST_ID_SQL02);
        		form.setSelHidaikosha(GS.EMPTY_CHARCTER);
        		// 被代行者
        		form.setTxtHidaikosha(user_bean.getComEmailAddr());
        		form.setSelectedHidaikoshaId(user_bean.getComUserId());
			}
    	}
    	
    	// 代行者選択
		// 入力/検索
    	form.setInDaikosha(GS.EMPTY_CHARCTER);
    	// チェッククラス
    	InputCheck check = new InputCheck();
    	// 担当者一覧を表示
    	if (!check.isNullBlank(form.getTxtHidaikosha())) {
    		// 被代行者が選択されている場合
    		
    		// 被代行者の保持する業務フローパターンシステム区分【リスト】の区分を取得する。
    		String systemKbnListKbn = dbacc.checkSystemKbnList();
    		if (ALL_GSS.equals(systemKbnListKbn)) {
            	// '01'：GSSのみ格納されている場合
    			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL05);
    		} else if(NONE_GSS.equals(systemKbnListKbn)) {
            	// '01'：GSSが存在しない場合
    			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL06);
    		} else if(MIX_GSS.equals(systemKbnListKbn)) {
            	// '01'：GSSが存在する 且つ（02'：FOCUS or 03'：MTSが存在する）場合
    			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL07);
    		}
    	} else {
			// 被代行者が選択されていない場合
    		form.setAr_daiko_tanto(new ArrayList());
    	}
    	// 代行者
    	form.setTxtDaikosha(GS.EMPTY_CHARCTER);
    	form.setSelectedDaikoshaId(GS.EMPTY_CHARCTER);

    	// 代行者一覧の表示
    	dbacc.setDaikoList(DAIKO_ICHIRAN_INIT);
    	// 被代行者一覧の表示
    	dbacc.setDaikoList(HIDAIKO_ICHIRAN_INIT);

        return GS.OS7101;
    }

    /**
     * 被代行者選択.＞処理 <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public void doHidaikoSentaku() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DaikoDbAcc dbacc = new DaikoDbAcc(sqlExec, log, appContext);
        // 代行者選択.担当者一覧の取得
        
        // 被代行者の保持する業務フローパターンシステム区分【リスト】の区分を取得する。
        String systemKbnListKbn = dbacc.checkSystemKbnList();
		if (ALL_GSS.equals(systemKbnListKbn)) {
        	// '01'：GSSのみ格納されている場合
			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL05);
		} else if(NONE_GSS.equals(systemKbnListKbn)) {
        	// '01'：GSSが存在しない場合
			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL06);
		} else if(MIX_GSS.equals(systemKbnListKbn)) {
        	// '01'：GSSが存在する 且つ（02'：FOCUS or 03'：MTSが存在する）場合
			dbacc.getTantoIchiran(TANTO_LIST_ID_SQL07);
		}
		// 入力検索
		form.setInDaikosha(GS.EMPTY_CHARCTER);
		// 選択された代行者
		form.setSelDaikosha(GS.EMPTY_CHARCTER);
    	// 代行者
    	form.setTxtDaikosha(GS.EMPTY_CHARCTER);
    	form.setSelectedDaikoshaId(GS.EMPTY_CHARCTER);
		
    	// 代行者一覧の表示
    	dbacc.setDaikoList(DAIKO_ICHIRAN_NORMOL);
    	// 被代行者一覧の表示
    	dbacc.setDaikoList(HIDAIKO_ICHIRAN_NORMOL);
    	// コミット
    	dbacc.commit();
    }

    /**
     * 代行設定処理 <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public void doDaikoSetei() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DaikoDbAcc dbacc = new DaikoDbAcc(sqlExec, log, appContext);
        // 入力チェック
        InputCheck check = new InputCheck();
        
        // 必須チェック
        if (check.isNullBlank(form.getTxtHidaikosha()) || check.isNullBlank(form.getTxtDaikosha())) {
			// 被代行者、または代行者が選択されていない場合
			appContext.setMsgCode(GL.ERR_REQUIREDITEM);
			return;
        }
        // 登録チェック
        String delFlg = dbacc.registCheck();
        if (FLG_DEL_UNDELETE.equals(delFlg)) {
        	// 選択された代行者が既に登録されている場合
			appContext.setMsgCode(GL.ERR_REGISTERED, GL.OS7101_DAIKOSHA_HIDAIKOSHA);
			return;
        }
        // M03_代行設定マスタの登録/更新
        if (FLG_DEL_DELETED.equals(delFlg)) {
        	// 取得した削除フラグが’1’：削除済の場合は更新処理を行う
        	dbacc.updateM03();
        } else {
        	// M03_代行設定マスタの登録を行う。
        	dbacc.insertM03();
        }

    	// 代行者一覧の表示
    	dbacc.setDaikoList(DAIKO_ICHIRAN_NORMOL);
    	// コミット
    	dbacc.commit();
    }

    /**
     * 削除処理 <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public void doDelete() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        DaikoDbAcc dbacc = new DaikoDbAcc(sqlExec, log, appContext);
        // 代行排他チェック
        if (dbacc.daikoHaitaCheck() > 0) {
        	// 削除対象の代行者がすでに代行者としてログインしている場合
			appContext.setMsgCode(GL.ERR_PERSONLOGIN);
			return;
        }
        // M03_代行設定マスタの論理削除
        dbacc.deleteM03();

    	// 代行者一覧の表示
    	dbacc.setDaikoList(DAIKO_ICHIRAN_NORMOL);
    	// コミット
    	dbacc.commit();
    }
}