/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.system.dbAcc.GolfDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS4101_ゴルフ会員権一覧 ビジネスロジッククラス <br>
 */
public class GolfBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public GolfBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
    }

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 遷移先
	 * @throws Exception 
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		GolfDbAcc dbacc = new GolfDbAcc(sqlExec, log, appContext);

		// 査定期取得
		dbacc.getSateiki();
		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 対象年月取得
		dbacc.getTaiyoYm();

		// 一覧情報取得
		dbacc.getMeisaiList();
        
		return GS.OS4101;
	}
	
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return 遷移先
	 * @throws Exception 
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		GolfDbAcc dbacc = new GolfDbAcc(sqlExec, log, appContext);

		// 対象年月取得
		dbacc.getTaiyoYm();

		// 一覧情報取得
		dbacc.getMeisaiList();
        
		return GS.OS4101;
	}
}