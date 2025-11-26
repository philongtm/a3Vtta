/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/25		SSC				課題No.224 査定会社を新規登録後、査定会社一覧画面に遷移した際に,
										地域プルダウンを登録をした査定会社のシステム区分で再取得するよう修正。
******************************************************************************/
package app.system.bss;

import app.system.dbAcc.SateikaisyaIchiranDbAcc;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS7102_査定会社メンテナンス_一覧 ビジネスロジッククラス <br>
 */
public class SateikaisyaIchiranBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public SateikaisyaIchiranBss(AppContext appContext) throws Exception {
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
		SateikaisyaIchiranDbAcc dbacc = new SateikaisyaIchiranDbAcc(sqlExec, log, appContext);

		// システムセレクトボックス値取得
		dbacc.getSystemKbnList();

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();	

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		return GS.OS7102;
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
		SateikaisyaIchiranDbAcc dbacc = new SateikaisyaIchiranDbAcc(sqlExec, log, appContext);
		
		// 課題No.224
		// 追加開始
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();
		// 追加完了
		
		// 一覧情報取得
		dbacc.getMeisaiList();
        
		return GS.OS7102;
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
        SateikaisyaIchiranDbAcc dbacc = new SateikaisyaIchiranDbAcc(sqlExec, log, appContext);

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();	
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
        SateikaisyaIchiranDbAcc dbacc = new SateikaisyaIchiranDbAcc(sqlExec, log, appContext);
    	// コミット
    	dbacc.commit();
    }
}