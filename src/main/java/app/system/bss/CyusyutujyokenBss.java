/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/12/7		SSC				基準日、決算期区分(プルダウン)引数の修正
003		2009/12/21		SSC				課題No.220 システムプルダウン変更時、汎用２プルダウンを初期化
******************************************************************************/
package app.system.bss;

import app.system.dbAcc.CyusyutujyokenDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS7110_抽出条件メンテナンス_一覧 ビジネスロジッククラス <br>
 */
public class CyusyutujyokenBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public CyusyutujyokenBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
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
		CyusyutujyokenDbAcc dbacc = new CyusyutujyokenDbAcc(sqlExec, log, appContext);

		// システムセレクトボックス値取得
		dbacc.getSystemKbnList();

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();	

		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2List();

		// 決算期区分セレクトボックス値取得
		dbacc.getKesanKbnList();

		// 基準日セレクトボックス値取得
		dbacc.getKijunbiList();

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		return GS.OS7110;
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
		CyusyutujyokenDbAcc dbacc = new CyusyutujyokenDbAcc(sqlExec, log, appContext);

		// 一覧情報取得
		dbacc.getMeisaiList();
        
		return GS.OS7110;
	}

    /**
     * システム変更処理 <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public void doChangeSystemKbn() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        CyusyutujyokenDbAcc dbacc = new CyusyutujyokenDbAcc(sqlExec, log, appContext);

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();
		
		// 課題No.220
		// 追加開始
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2List();
		// 追加完了
		
		// 課題No.100
		// 追加開始
		
		// 決算期区分セレクトボックス値取得
		dbacc.getKesanKbnList();
		
		// 基準日セレクトボックス値取得
		dbacc.getKijunbiList();
		
		// 追加完了
		

    }

    /**
     * 汎用１変更処理 <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public void doChangeHanyo1() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        CyusyutujyokenDbAcc dbacc = new CyusyutujyokenDbAcc(sqlExec, log, appContext);

		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2List();	
    }
}