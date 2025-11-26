/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/20		SSC				1.5次版機能組込
******************************************************************************/
package app.system.bss;

import app.SessionDataZen;
import app.system.dbAcc.ChampionDbAcc;
import app.system.form.ChampionForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.sql.SQLException;


/**
 * チャンピオン部メンテンナンス画面ビジネスロジッククラス
 */
public class ChampionBss {

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;		// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;				// 共通セッション
	private ChampionForm form;
	
	
	/**
	 * コンストラクタ
	 */
	public ChampionBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZen();
		form = (ChampionForm)appContext.getActionForm();
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
		ChampionDbAcc dbacc = new ChampionDbAcc(sqlExec, log, appContext);
		
		dbacc.execute();
		
		return GS.OS5101;
	}

	/**
	 * 更新処理
	 * 
	 * @throws Exception
	 */
	public void doUpdate() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
			
		ChampionDbAcc dbacc = new ChampionDbAcc(sqlExec, log, appContext);
		// 登録処理
		if (dbacc.doUpdate()) {
			// 画面表示情報再取得処理
			dbacc.execute();
		} else {
			throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc");		// 更新失敗
		}
		
		// コネクションの開放
		appContext.destroy();
	}
	
	/**
	 * 登録前入力チェック
	 * 
	 * @return 選択情報ありの場合true、選択情報なしの場合false
	 * @throws Exception
	 */
	public boolean torokuCheck() throws Exception {
	    // 処理結果フラグ
	    boolean result = false;

///////////////////////////////////////
//	  障害票No359
//	  チェックイン日 2008/05/17
//	  対応者 　福士
//	  修正概要
//	  ②エラーチェックロジックのミスを修正。
///////////////////////////////////////
		// 選択値リストを取得	
		String chk = form.getChk();		// 部明細のラジオボタンの値
		
		
		// 選択情報リストチェック		
		if(chk.equals("null") == false) {	
			result = true;
			form.setSelectArr(chk.split("/"));
		}
//ここまで（障害票No359　②）
		return result;
	}
}