/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/16		SSC				課題No.114 検索値退避処理 
******************************************************************************/
package app.tairyu.bss;

import app.SessionData;
import app.UserBean;
import app.tairyu.dbAcc.SenteituikaDbAcc;
import app.tairyu.form.SenteituikaForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

/**
 * OB2103_対象先選定_追加対象先選択 ビジネスロジッククラス
 */
public class SenteituikaBss {

	private AppContext appContext = null;						// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private Log log = null;									// LOG
	private SessionData cmnData;								// 共通セッション
	private UserBean user_bean;								// 共通セッション
	private SenteituikaForm form;								// アクションフォーム

	private static final String MSG      = "replace.kanjo_cdkanjo_nm";
	private static final String KANJO_CD = "kanjo_cd";
	private static final String KANJO_NM = "kanjo_nm";

	/**
	 * コンストラクタ
	 */
	public SenteituikaBss(AppContext appContext) throws Exception {
		this.appContext = appContext;		
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (SenteituikaForm)appContext.getActionForm();
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時)
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteituikaDbAcc dbacc = new SenteituikaDbAcc(sqlExec, log, appContext);

		// 査定期セレクトボックス値取得
		dbacc.getSateiki();

		// 汎用２セレクトボックス値取得(海外のみ)
		if (!user_bean.getComWorkflowSystemkbn().equals(GS.GSS)){
			dbacc.getHanyou2();
		}

		// 表示件数セレクトボックス値取得
		dbacc.getShow();

		return GS.OB2103;
	}
	
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時)
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteituikaDbAcc dbacc = new SenteituikaDbAcc(sqlExec, log, appContext);

		// 一覧情報取得
		dbacc.getMeisai();
	
		return GS.OB2103;
	}

	/**
	 * 検索処理
	 */
	public void search() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		SenteituikaDbAcc dbacc = new SenteituikaDbAcc(sqlExec, log, appContext);

		// 入力チェック
		if(!chkNyuryoku()){
			return;
		}
		// 課題No.114
		// 追加開始
		// 検索値退避処理
    	form.setKensaku_sateiki(new String(Function.trim(form.getSateiki())));
    	form.setKensaku_kanjo_cd(new String(Function.trim(form.getKanjo_cd())));
    	form.setKensaku_kanjo_nm(new String(Function.trim(form.getKanjo_nm())));
    	form.setKensaku_hanyou2(new String(Function.trim(form.getHanyou2())));
		// 追加完了

		// 対象年月取得
		dbacc.getTaisho_ym();

		// 一覧情報取得
		dbacc.getMeisai();
	
		return;
	}

	/**
	 * 入力チェック
	 */
	public boolean chkNyuryoku() throws Exception {	

		InputCheck check = new InputCheck();
    	int kanjo_cd_len = check.lenB(form.getKanjo_cd());
    	int kanjo_nm_len = check.lenB(form.getKanjo_nm());

    	
    	// 勘定先CD、勘定先名称がどちらも未入力の場合エラー
	    if(kanjo_cd_len == 0  && kanjo_nm_len == 0){
            appContext.setMsgCode(GL.ERR_INPUT,MSG);
			appContext.setFocusField(KANJO_CD);
			return false;
	    }
	    
	    // 勘定先名称が4byte未満の場合エラー
	    if( 0 < kanjo_nm_len && kanjo_nm_len < 4){
            appContext.setMsgCode(GL.ERR_LENGTH,GL.OB2101_KANJO_NM);
			appContext.setFocusField(KANJO_NM);
			return false;
	    }

	    // 入力禁止文字が含まれている場合エラー
		if (check.haveKinshiMoji(form.getKanjo_nm())) {
			for (int i = 0; i < form.getKanjo_nm().length(); i++) {
				String kinshiChar = form.getKanjo_nm().substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED,kinshiChar);
					appContext.setFocusField(KANJO_NM);
					return false;
				}
			}
		}
	    
		return true;
	
	}
}