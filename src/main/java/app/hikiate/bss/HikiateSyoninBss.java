/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.hikiate.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.hikiate.dbAcc.HikiateSyoninDbAcc;
import app.hikiate.form.HikiateSyoninForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 *  OD1103 引当金確認_承認一覧 ビジネスロジッククラス<br>
 */
public class HikiateSyoninBss {
    private static final String CHECKBOX_STATUS_ON		= "1";		//チェックボックスのステータス:オン
    private AppContext appContext = null;						// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;                     	// ＤＢアクセス
    private Log log = null;                                 	// LOG
    private SessionData cmnData;                               // 共通セッション
    private UserBean user_bean;                             	// 共通セッション
    private TorihikisakiBean tori_bean;     					// 取引先情報
    private HikiateSyoninForm form;                            // アクションフォーム
    
    private static final String JISHI_PHASE 				= "JISHI_PHASE";            // 実施フェーズ

    /**
     * コンストラクタ
     */
    public HikiateSyoninBss(AppContext appContext) throws Exception {
        this.appContext = appContext;       
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.user_bean = cmnData.getUser_bean();
        this.form = (HikiateSyoninForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)
     */
    public String executeInit() throws Exception {
        
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        HikiateSyoninDbAcc dbacc = new HikiateSyoninDbAcc(sqlExec, log, appContext);
        
        // 表示件数セレクトボックス値取得
        dbacc.getShow();
        
        // 一覧情報取得
        dbacc.getMeisai();
        
        // 取戻不可処理
        dbacc.uptT14();
        // コミット処理
        dbacc.commit();
        
        return GS.OD1103;
    }
    
    /**
     * 画面初期表示値取得(メニューリンク以外から遷移時)
     */
    public String execute() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        HikiateSyoninDbAcc dbacc = new HikiateSyoninDbAcc(sqlExec, log, appContext);
        
        // 表示件数セレクトボックス値取得
        dbacc.getShow();
        
        // 一覧情報取得
        dbacc.getMeisai();
        
        return GS.OD1103;
    }
    
    /**
     * 
     *  承認実行処理<br>
     * 
     * @return
     * @throws Exception
     */
    public boolean doZikko() throws Exception { 
    	
        List<TorihikisakiBean> list = form.getAr_meisai();
        boolean syonin_flg = false;
        for(int i=0;i<list.size();i++){       
            if ((CHECKBOX_STATUS_ON).equals(list.get(i).getSyonin_chk())){
                syonin_flg = true;
                break;
            }
        }
        if(!syonin_flg){
    		appContext.setMsgCode(GL.ERR_INPUT,GL.OD1103_SYONIN);
    		return syonin_flg;
        }
    	HikiateSyoninDbAcc dbacc = null;
    	for(int i = 0; i < list.size(); i++){
    		tori_bean = (TorihikisakiBean)list.get(i);
            if (!(CHECKBOX_STATUS_ON).equals(tori_bean.getSyonin_chk())){
                continue;
            }
    		cmnData.setTori_bean(tori_bean);
    		// コネクションの取得
            this.sqlExec = appContext.getSqlExecuter();
            dbacc = new HikiateSyoninDbAcc(sqlExec, log, appContext);
            
    		String phase = null;
            // 共)ユーザ情報.実施業務【リスト】
            for(int j = 0; j < user_bean.getComOparation().size(); j++){
            	Map<String,String> map = (Map<String,String>) user_bean.getComOparation().get(j);
            	if(map.get(JISHI_PHASE) != null && map.get(JISHI_PHASE).equals(tori_bean.getPhase())){
            		// 実施フェーズ
            		phase = map.get(JISHI_PHASE);
            	}
            }
            // T14_査定進捗管理の更新
        	dbacc.uptT14(phase);
        	// T13_入力履歴の登録
        	dbacc.intT13();
    	}

        // コミット処理
        dbacc.commit();
        
    	return syonin_flg;
    }
    

}
