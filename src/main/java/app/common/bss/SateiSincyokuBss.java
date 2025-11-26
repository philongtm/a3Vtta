/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.bss;

import app.common.dbAcc.SateiSincyokuDbAcc;
import app.common.form.SateiSincyokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.util.HashMap;

/**
 * OZ6109_査定進捗照会タブ ビジネス ロジッククラス <br>
 */
public class SateiSincyokuBss {

	private AppContext appContext = null;					                    				// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               					// ＤＢアクセス
	private Log log = null;									                				// LOG
	private SateiSincyokuForm form;                                                  			// アクションフォーム
    private static final String ANKEN_NO 					= "anken_no";						// 案件No.
    
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public SateiSincyokuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.form = (SateiSincyokuForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public void executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SateiSincyokuDbAcc dbacc = new SateiSincyokuDbAcc(sqlExec, log, appContext);
		
		// 取引先の案件No.を取得する
		dbacc.getAnkenNo();
		
		// シングルコーテーションで括り、カンマで区切る
		this.getFormatAnkenNo();
		
        // 一覧情報取得
        dbacc.getHandanList();
	}
	
	/**
	 * シングルコーテーションで括り、カンマで区切る <br>
	 * 
	 * @throws Exception
	 */
	private void getFormatAnkenNo() throws Exception  {
        
        String anken_no_ser = GS.EMPTY_CHARCTER;
        
        for (int i = 0; i < form.getAr_anken_no().size(); i++) {
        	HashMap hm = (HashMap) form.getAr_anken_no().get(i);
        	
        	if (GS.EMPTY_CHARCTER.equals(anken_no_ser)) {
        		anken_no_ser = GS.SINGLE_QUOTATION + String.valueOf(hm.get(ANKEN_NO)) + GS.SINGLE_QUOTATION;
        	} else {
        		anken_no_ser += GS.COMMA + GS.SINGLE_QUOTATION + String.valueOf(hm.get(ANKEN_NO)) + GS.SINGLE_QUOTATION;
        	}
        }
        
        if (GS.EMPTY_CHARCTER.equals(anken_no_ser)) {
    		anken_no_ser = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
        }
        
        // ActionForm に明細を格納
        form.setAnken_no_ser(anken_no_ser);
	}
}
