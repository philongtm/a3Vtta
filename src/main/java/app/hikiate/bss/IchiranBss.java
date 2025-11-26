/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.hikiate.bss;

import app.TorihikisakiBean;
import app.hikiate.dbAcc.IchiranDbAcc;
import app.hikiate.form.IchiranForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

/**
 * OD1101_引当金確認_対象先一覧 ビジネスロジッククラス
 */
public class IchiranBss {

    private AppContext appContext = null;                       // ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                     // ＤＢアクセス
    private Log log = null;                                 // LOG
    private IchiranForm form;                                   // アクションフォーム

    /**
     * コンストラクタ
     */
    public IchiranBss(AppContext appContext) throws Exception {
        this.appContext = appContext;       
        this.log = appContext.getLog();
        this.form = (IchiranForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);

        //参照フェーズ設定
        this.setSansyoPhase();
        
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
        
        return GS.OD1101;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);

        // 各進捗件数取得
        dbacc.getSintyoku();
        // 一覧情報取得
        dbacc.getMeisai();
        
        return GS.OD1101;
    }
    
    /**
     * 一括もぎ取り処理
     */
    public boolean doIkkatuMogitori() throws Exception {

        // 処理結果フラグ
        boolean result = false;
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);
        
        form = (IchiranForm)appContext.getActionForm();
        String[] checkBox = form.getSelectedMountains();
        // もぎ取りチェックボックスがオンの勘定先が存在しない場合
        if(checkBox == null || checkBox.length ==0 ) {
        	appContext.setMsgCode(GL.ERR_CHECKTAKE);
        	return true;
        }
        for(int i=0;i<checkBox.length;i++){
        	// チェック用パラメータ取得
            TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(Integer.parseInt(checkBox[i]));
        	if(dbacc.checkIkkatuMogitori(listBean)){
            	// もぎ取り処理
                // T14_査定進捗管理の更新
                dbacc.uptT1401(listBean);
                // 入力履歴の登録を行う
                dbacc.intT1300(listBean);
                
                result = true;
            }else{
                appContext.setMsgCode(GL.ERR_TORITAKEN,listBean.getKanjo_cd());

                return false;
            }
        }    
        if(result){
        	// コミット処理
            dbacc.commit();
        }
        
        return result;
    }

    /**
     * もぎ取り処理
     */
    public boolean doMogitori() throws Exception {

        // 処理結果フラグ
        boolean result = false;
    
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        IchiranDbAcc dbacc = new IchiranDbAcc(sqlExec, log, appContext);  
        // チェック用パラメータ取得
        TorihikisakiBean listBean = (TorihikisakiBean)form.getAr_meisai().get(form.getId());
        //もぎ取りチェック
        if(dbacc.checkMogitori()){
        	// T14_査定進捗管理の更新
            dbacc.uptT1401(listBean);
            // 入力履歴の登録を行う
            dbacc.intT1300(listBean);
            // コミット処理
            dbacc.commit();
            result = true;
        }else{
            appContext.setMsgCode(GL.ERR_TORITAKEN,listBean.getKanjo_cd());
        }   

        return result;
    }
    
	/**
	 * 参照フェーズ設定<br>
	 */
	private void setSansyoPhase(){
		if (appContext.getCMN().getUser_bean().getComHikiatekin_kensyo_t_flg().equals(GS.ON)){
            form.setSansyo_phase(GS.PHASE_HIKIATEKIN_KENSYO);
        }else{
            form.setSansyo_phase(GS.PHASE_HIKIATEKIN_KAKUNIN);
        }
	}
}