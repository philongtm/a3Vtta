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
import app.UserMaintenanceBean;
import app.system.dbAcc.UserIchiranDbAcc;
import app.system.form.UserIchiranForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.ArrayList;

/**
 * OS7106_ユーザマスタメンテナンス_一覧 ビジネス ロジッククラス <br>
 */
public class UserIchiranBss {

	private AppContext appContext = null;					                    // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               	// ＤＢアクセス
	private Log log = null;									                // LOG
	private SessionData cmnData;							                    // 共通セッション
	private UserBean user_bean;								                // ユーザービンー
	private UserIchiranForm form;                                              // アクションフォーム
	
	private static final String INSERT 				= "insert";
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public UserIchiranBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (UserIchiranForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		UserIchiranDbAcc dbacc = new UserIchiranDbAcc(sqlExec, log, appContext);
		
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();		
		
		// 業務フローセレクトボックス値取得
		dbacc.getGyoumu_huro();
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();
		
		return GS.OS7106;
	}
    
    /**
     * 【汎用１セレクトボックス処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String hanyo1() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        UserIchiranDbAcc dbacc = new UserIchiranDbAcc(sqlExec, log, appContext);
    
		// 業務フローセレクトボックス値取得
		dbacc.getGyoumu_huro();
        
        return GS.OS7106;
    }
    
    /**
     * 【検索処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String doSearch() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        UserIchiranDbAcc dbacc = new UserIchiranDbAcc(sqlExec, log, appContext);
                        
		// 検索条件を格納する
        form.setUser_id_search(form.getUser_id());
        form.setUser_nm_search(form.getUser_nm());
        form.setEmail_search(form.getEmail());
        form.setHanyo1_search(form.getHanyo1());
        form.setGyoumu_huro_search(form.getGyoumu_huro());
        
        // ActionForm に明細を格納
        form.setAr_meisai(new ArrayList<UserMaintenanceBean>());
        
        // ページ設定       
        form.setPager(new ArrayList<UserMaintenanceBean>());
        
		// 検索キーチェック
		if (GS.EMPTY_CHARCTER.equals(form.getGyoumu_huro())) {
			if (GS.EMPTY_CHARCTER.equals(form.getUser_id())
				&& GS.EMPTY_CHARCTER.equals(form.getUser_nm())
				&& GS.EMPTY_CHARCTER.equals(form.getEmail())
				&& GS.EMPTY_CHARCTER.equals(form.getHanyo1())) {
				appContext.setMsgCode(GL.ERR_SEARCH);
				return GS.OS7106;
			}
		}
        
		// 明細情報取得
		dbacc.getMeisai();
				
		// ユーザ一覧が200件以上の場合
		if (form.getAr_meisai().size() >= 200) {	
            // ActionForm に明細を格納
			form.setAr_meisai(new ArrayList<UserMaintenanceBean>());
            // ページ設定       
            form.setPager(new ArrayList<UserMaintenanceBean>());            
			appContext.setMsgCode(GL.ERR_SEARCHOVER, "200");
			return GS.OS7106;
		}
		
        return GS.OS7106;
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
				
		// 登録画面「戻る」にするとき
		if (!(INSERT).equals(form.getAction_flg())) {
			return GS.OS7106;			
		}
		
		// 検索条件が退避する
		form.setUser_id(form.getUser_id_search());
		form.setUser_nm(form.getUser_nm_search());
		form.setEmail(form.getEmail_search());
		form.setHanyo1(form.getHanyo1_search());
		form.setGyoumu_huro(form.getGyoumu_huro_search());
		
		// 明細情報取得
		dbacc.getMeisai();
		
		return GS.OS7106;
		
	}
	
    /**
     * 【業務フローパターンを取得する処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public void gyoumuhuro_patan_list(UserMaintenanceBean user_main_bean) throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        UserIchiranDbAcc dbacc = new UserIchiranDbAcc(sqlExec, log, appContext);
    
		// 業務フローセレクトボックス値取得
		dbacc.getGyoumuhuroPatanList(user_main_bean);
    }
    
}
