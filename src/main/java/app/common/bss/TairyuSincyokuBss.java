/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/11/03		SSC				課題No.20 取戻処理を案件単位に変更
******************************************************************************/
package app.common.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.common.dbAcc.TairyuSincyokuDbAcc;
import app.common.form.TairyuSincyokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GS;
import common.util.Log;

import java.util.List;
import java.util.Map;

/**
 * OZ6110_実質滞留債権判定進捗照会タブ ビジネス ロジッククラス <br>
 */
public class TairyuSincyokuBss {

	private AppContext appContext = null;					                    				// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               					// ＤＢアクセス
	private Log log = null;									                				// LOG
	private TairyuSincyokuForm form;                                                  			// アクションフォーム
    private static final String ANKEN_NO 					= "anken_no";						// 案件No.
    private static final String HOJI_FLG					= "1";
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public TairyuSincyokuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.form = (TairyuSincyokuForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得 <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TairyuSincyokuDbAcc dbacc = new TairyuSincyokuDbAcc(sqlExec, log, appContext);
		
		// 取引先の滞留判定組織一覧の表示
		dbacc.getSosikiList();
		
		// 組織一覧データある場合
		if (form.getAr_sosiki() != null && form.getAr_sosiki().size() > 0) {
			// 検索用案件No.を取得する
			this.getAnkenNo();
			
	        // 取引先の滞留判定進捗一覧の表示
	        dbacc.getSinchokuList();
		}
        
		return GS.OS6104;
	}
	
	/**
	 * 進捗処理 <br>
	 * 
	 * @throws Exception
	 */
	public void sinchoku() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TairyuSincyokuDbAcc dbacc = new TairyuSincyokuDbAcc(sqlExec, log, appContext);
		
		// 選択された組織情報
		Map<String, String> sosiki = form.getAr_sosiki().get(form.getId_sosiki());
		form.setSrh_anken_no(GS.SINGLE_QUOTATION + sosiki.get(ANKEN_NO) + GS.SINGLE_QUOTATION);
		
        // 取引先の滞留判定進捗一覧の表示
        dbacc.getSinchokuList();
	}
	
	/**
	 * 検索用案件No.を取得する <br>
	 * 
	 * @throws Exception
	 */
	private void getAnkenNo() throws Exception  {
		// 共)取引先情報を取得する
        SessionData cmnData = appContext.getCMN();
        TorihikisakiBean tori_bean = cmnData.getTori_bean();
        // 検索用案件No.(一件目)
        String srh_anken_no1 = GS.EMPTY_CHARCTER;
        // 共)取引先情報.案件No.
        String toriAnkenNo = tori_bean.getAnken_no();
        // 組織情報リスト
        List<Map<String, String>> ar_sosiki = form.getAr_sosiki();
        
        // 該当フラグ
        boolean gaito_flg = false;
        for (int i = 0; i < ar_sosiki.size(); i++) {
        	// 組織情報
        	Map<String, String> sosiki = ar_sosiki.get(i);
        	// 組織一覧.案件No.
        	String sosikiAnken = sosiki.get(ANKEN_NO);
        	// 共)取引先情報.案件No.が機)組織一覧.案件No.のいずれかに該当する場合
        	if (toriAnkenNo.equals(sosikiAnken)) {
        		gaito_flg = true;
        	}
        	// 機)組織一覧.案件No.を繋がる。
        	if (i == 0) {
        		srh_anken_no1 = GS.SINGLE_QUOTATION + sosiki.get(ANKEN_NO) + GS.SINGLE_QUOTATION;
        	}
        }
        
        //課題No.20
        //修正開始
        //if (gaito_flg && GS.TORIMODOSHI_KA.equals(form.getTorimodoshi_fuka_flg()) && HOJI_FLG.equals(form.getUpd_user_id_flg())) {
        //修正完了
        if (gaito_flg) {
            // 共)取引先情報.案件No.が機)組織一覧.案件No.のいずれかに該当する場合
        	form.setSrh_anken_no(GS.SINGLE_QUOTATION + tori_bean.getAnken_no() + GS.SINGLE_QUOTATION);
        } else {
        	// 該当無しの場合
        	form.setSrh_anken_no(srh_anken_no1);
        }
	}
}
