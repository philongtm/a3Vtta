/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.system.dbAcc.KureemuDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

/**
 * OS3101 クレーム債権再設定_対象先一覧 ビジネスロジッククラス
 */
public class KureemuBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG

	/**
	 * コンストラクタ
	 */
	public KureemuBss(AppContext appContext) throws Exception {
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
		KureemuDbAcc dbacc = new KureemuDbAcc(sqlExec, log, appContext);

		// 査定期取得
		dbacc.getSateiki();

		//自担当分/汎用２ラジオボタン初期判定
		dbacc.getInitTanto();

		// 各進捗件数取得
		dbacc.getSintyoku();

		// ソート順セレクトボックス値取得
		dbacc.getSort();

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		// 一覧情報取得
		dbacc.getMeisai();
        
		return GS.OS3101;
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
		KureemuDbAcc dbacc = new KureemuDbAcc(sqlExec, log, appContext);

		// 各進捗件数取得
		dbacc.getSintyoku();
		// 一覧情報取得
		dbacc.getMeisai();
        
		return GS.OS3101;
	}

	/**
	 * もぎ取り処理 <br>
	 * 
	 * @return 処理結果フラグ
	 * @throws Exception 
	 */
	public boolean doMogitori() throws Exception {

		// 処理結果フラグ
	    boolean result = false;
	
	    // コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KureemuDbAcc dbacc = new KureemuDbAcc(sqlExec, log, appContext);

		//もぎ取りチェック
		if(dbacc.checkMogitori()){
			// もぎ取り処理
			dbacc.doMogitoriUpdate();
			dbacc.doMogitoriInsert();
			dbacc.commit();
			result = true;
		}else{
    		appContext.setMsgCode(GL.ERR_TAKEN);
		}					

		return result;
	}
}