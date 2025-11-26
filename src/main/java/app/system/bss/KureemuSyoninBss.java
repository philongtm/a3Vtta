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
import app.system.dbAcc.KureemuSyoninDbAcc;
import app.system.form.KureemuSyoninForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS3104_クレーム債権再設定_承認一覧 ビジネス ロジッククラス <br>
 */
public class KureemuSyoninBss {

	private AppContext appContext = null;					                    				// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               					// ＤＢアクセス
	private Log log = null;									                				// LOG
	private SessionData cmnData;							                    				// 共通セッション
	private UserBean user_bean;								                				// ユーザービンー
	private KureemuSyoninForm form;                                                  			// アクションフォーム

	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public KureemuSyoninBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (KureemuSyoninForm) appContext.getActionForm();
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
		KureemuSyoninDbAcc dbacc = new KureemuSyoninDbAcc(sqlExec, log, appContext);
		
		// 表示件数セレクトボックス値取得
		dbacc.getShow();

        // 一覧情報取得
        dbacc.getMeisai();        
        
        // T14_査定進捗管理の更新
        dbacc.setUpdateT1400();
        
        dbacc.commit();
        
		return GS.OS3104;
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
        KureemuSyoninDbAcc dbacc = new KureemuSyoninDbAcc(sqlExec, log, appContext);
        
        // 一覧情報取得
        dbacc.getMeisai();
    
        // T14_査定進捗管理の更新
        dbacc.setUpdateT1400();
        
        return GS.OS3104;
    }
}
