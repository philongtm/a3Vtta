/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.system.bss;

import app.system.dbAcc.WorkFlowIchiranDbAcc;
import app.system.form.WorkFlowIchiranForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

/**
 * OS7104_業務フローパターンメンテナンス_一覧 ビジネスロジッククラス <br>
 */
public class WorkFlowIchiranBss {
    private AppContext appContext = null;                         // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                           // ＤＢアクセス
    private WorkFlowIchiranForm form = null;                    	// アクションフォーム
    private Log log = null;                                       // LOG


    /**
     * コンストラクタ
     */
    public WorkFlowIchiranBss(AppContext appContext) throws Exception {
        this.appContext = appContext;    
        form = (WorkFlowIchiranForm)appContext.getActionForm();
        this.log = appContext.getLog();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowIchiranDbAcc dbacc = new WorkFlowIchiranDbAcc(sqlExec, log, appContext);

        // システムセレクトボックス
        dbacc.getSystemkbn();

        // 汎用1セレクトボックス
        dbacc.getHanyou1();

        // 表示件数セレクトボックス値取得
        dbacc.getShow();

        return GS.OS7104;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  
    	
    	if("2".equals(form.getGamen_flg())){
            // コネクションの取得
            this.sqlExec = appContext.getSqlExecuter();
            WorkFlowIchiranDbAcc dbacc = new WorkFlowIchiranDbAcc(sqlExec, log, appContext);
            if(form.getAr_meisai() != null && form.getAr_meisai().size() > 0){
            	dbacc.getItiran();
            }
            // 前回表示時のページ設定をPagerにセット
            form.setPager(form.getId() + 1);
    	}
    	
        return GS.OS7104;
    }
    
    /**
     * 
     *  検索処理<br>
     * 
     * @throws Exception
     */
    public String doSearch() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowIchiranDbAcc dbacc = new WorkFlowIchiranDbAcc(sqlExec, log, appContext);
        dbacc.getItiran();
        
        return GS.OS7104;
    }
    
    /**
     * 
     * P02_区分からシステムに紐づく分類１を取得する <br>
     * 
     * @throws Exception
     */
    public String doChange() throws Exception {
    	// コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        WorkFlowIchiranDbAcc dbacc = new WorkFlowIchiranDbAcc(sqlExec, log, appContext);
        dbacc.getHanyou1();
        
        return GS.OS7104;
    }
    
}
