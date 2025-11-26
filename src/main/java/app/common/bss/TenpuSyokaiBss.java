/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/15		SSC				1.5次版機能組込
******************************************************************************/
package app.common.bss;

import app.SessionDataZen;
import app.common.dbAcc.TenpuSyokaiDbAcc;
import app.common.form.TenpuSyokaiForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Ftp;
import common.util.Function;
import common.util.Log;
import common.util.Profile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/**
 * 添付内容照会画面ビジネスロジッククラス
 */
public class TenpuSyokaiBss {
	
	private static String FTPDIR = Profile.getString(GS.PROFILE_FTPDIR,"");

	private String CLASSNAME = getClass().getName(); // クラス名
	
	private AppContext appContext = null;	// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;		// ＤＢアクセス
	private Log log = null;					// LOG

	private SessionDataZen cmnData;	// 共通セッション
	private TenpuSyokaiForm form;

	/**
	 * コンストラクタ
	 */
	public TenpuSyokaiBss(AppContext appContext) throws SQLException {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		cmnData = appContext.getCMNZen();
		form = null;
	}

	/**
	 * 対象先検索
	 */
	public String execute() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		
		// DBから画面表示する値を取得し、セッションに格納
		TenpuSyokaiDbAcc dbacc = new TenpuSyokaiDbAcc(sqlExec, log, appContext);
		dbacc.execute();
		
		// コネクションの開放
		appContext.destroy();		
		
		return GS.OZ1102;
	}
	
	/**
	 * ダウンロード処理
	 */
	public Object downloadExecute() throws Exception {
		
		form = (TenpuSyokaiForm)appContext.getActionForm();

	    //int i = form.getId();
	    int i = Function.getValueOfInt(appContext.getRequest().getParameter("index"));
	    
	    List list = (List)form.getAr_meisai();
	    HashMap map = (HashMap)list.get(i);
	    
	   
	    Ftp ftp = new Ftp();
	    return ftp.Get(FTPDIR,map.get("jitu_file_nm").toString(),map.get("file_nm").toString());
	}
}