/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/26		SSC				課題No.104 入力チェック修正 
003		2009/11/26		SSC				課題No.142 DR/CR区分更新処理修正
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.UserBean;
import app.system.dbAcc.KanjyoTorokuDbAcc;
import app.system.form.KanjyoTorokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

/**
 * OS7109 勘定科目マスタメンテナンス_登録 ビジネス ロジッククラス <br>
 */
public class KanjyoTorokuBss {

	private AppContext appContext = null;					                    // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						               	// ＤＢアクセス
	private Log log = null;									                // LOG
	private SessionData cmnData;							                    // 共通セッション
	private UserBean user_bean;								                // ユーザービンー
	private KanjyoTorokuForm form;                                             // アクションフォーム
	
	// フォーカス設定用のID
	private static final String KANJO_CD				= "kanjo_cd";			// 勘定科目コード
	private static final String KANJO_NM				= "kanjo_nm";			// 勘定科目名称
	private static final String KANJO_UCHI_CD			= "kanjo_uchi_cd";		// 内分類コード
	private static final String KANJO_UCHI_NM			= "kanjo_uchi_nm";		// 内分類名称
	private static final String HANYO1				= "hanyo1";				// 汎用１
	private static final String HANYO2				= "hanyo2";				// 汎用２
	private static final String SAIKEN_FLG			= "saiken_flg";			// 債権フラグ
	private static final String DRCR_KBN				= "drcr_kbn";			// DR/CR区分
	
	// 課題No.104
	// 追加開始
	private static final String UTIBUNRUI_PATTERN	= "^[0-9a-zA-Z]+\\s?[0-9a-zA-Z]+$";
	// 追加完了
	
	/**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public KanjyoTorokuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.form = (KanjyoTorokuForm) appContext.getActionForm();
	}
	
	/**
	 * 画面初期表示値取得(メニューリンクから遷移時) <br>
	 * 
	 * @return 画面ＩＤ
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoTorokuDbAcc dbacc = new KanjyoTorokuDbAcc(sqlExec, log, appContext);

		// システムセレクトボックス値取得
		dbacc.getSystem();
		
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();
		
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
		
		// DR/CR区分セレクトボックス値取得
		dbacc.getDrcrkbn();
		
		// 債権フラグセレクトボックス値取得
		dbacc.getSaiken_flg();
		
		// 表示区分セレクトボックス値取得
		dbacc.getHyojikbn();
		
		return GS.OS7109;
	}
    
    /**
     * 【システムセレクトボックス処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String system() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KanjyoTorokuDbAcc dbacc = new KanjyoTorokuDbAcc(sqlExec, log, appContext);
    
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();
		
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
		
		// 債権フラグセレクトボックス値取得	
		dbacc.getSaiken_flg();       
        
		// DR/CR区分セレクトボックス値取得
		dbacc.getDrcrkbn();
		
		// 表示区分セレクトボックス値取得
		dbacc.getHyojikbn();
		
		//課題No.142
		//追加開始
		form.setDrcr_kbn(GS.EMPTY_CHARCTER);
		//追加完了
		form.setMankibi_flg(GS.EMPTY_CHARCTER);
		form.setKanjo_uchi_cd(GS.EMPTY_CHARCTER);
		form.setKanjo_uchi_nm(GS.EMPTY_CHARCTER);
		
        return GS.OS7109;
    }
    
    /**
     * 【汎用１セレクトボックス処理】 <br>
     * 
     * @return
     * @throws Exception
     */
    public String hanyo1() throws Exception {  

        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        KanjyoTorokuDbAcc dbacc = new KanjyoTorokuDbAcc(sqlExec, log, appContext);
    
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
        
        return GS.OS7109;
    }
    
	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時) <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String execute() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoTorokuDbAcc dbacc = new KanjyoTorokuDbAcc(sqlExec, log, appContext);

		// システムセレクトボックス値取得
		dbacc.getSystem();
		
		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1();
		
		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2();
		
		// 債権フラグセレクトボックス値取得
		dbacc.getSaiken_flg();
		
		return GS.OS7109;
	}
    
	/**
	 * 登録処理 <br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public String doInsert() throws Exception {	

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		KanjyoTorokuDbAcc dbacc = new KanjyoTorokuDbAcc(sqlExec, log, appContext);
		InputCheck inpChk = new InputCheck();		
		
		// 汎用１セレクトボックスがブランクの場合
		if (GS.EMPTY_CHARCTER.equals(form.getHanyo1())) {
			appContext.setMsgCode(GL.ERR_SELECT, cmnData.getLbl_nm1());
			appContext.setFocusField(HANYO1);
			return GS.OS7109;
		}
		
		// 汎用２セレクトボックスがブランクの場合
		if (GS.EMPTY_CHARCTER.equals(form.getHanyo2())) {
			appContext.setMsgCode(GL.ERR_SELECT, cmnData.getLbl_nm6());			
			appContext.setFocusField(HANYO2);
			return GS.OS7109;
		}
	
		// 債権フラグセレクトボックスがブランクの場合
		if (GS.EMPTY_CHARCTER.equals(form.getSaiken_flg())) {
			appContext.setFocusField(SAIKEN_FLG);
			appContext.setMsgCode(GL.ERR_SELECT, GL.OS7109_SAIKEN_FLG);
			return GS.OS7109;
		}
		
		// 勘定科目コードが未入力の場合
		if (GS.EMPTY_CHARCTER.equals(form.getKanjo_cd())) {
			// 勘定科目コードがフォーカス
			appContext.setFocusField(KANJO_CD);
			appContext.setMsgCode(GL.ERR_INPUT, GL.OS7109_KANJO_CD);
			return GS.OS7109;
		}
		
		// 勘定科目名称が未入力の場合
		if (GS.EMPTY_CHARCTER.equals(form.getKanjo_nm())) {
			appContext.setFocusField(KANJO_NM);
			appContext.setMsgCode(GL.ERR_INPUT, GL.OS7109_KANJO_NM);
			return GS.OS7109;
		}
		
		// 勘定科目コードが半角英数字以外で入力された場合
		if (!inpChk.isNumLetter(form.getKanjo_cd())) {
			appContext.setFocusField(KANJO_CD);
			appContext.setMsgCode(GL.ERR_NUMERICONLY, GL.OS7109_KANJO_CD);
			return GS.OS7109;
		}
		
		// 勘定科目名称が60バイトを超える場合
		if (inpChk.lenB(form.getKanjo_nm()) > 60) {
			appContext.setFocusField(KANJO_NM);
			appContext.setMsgCode(GL.ERR_LENGTH, GL.OS7109_KANJO_NM);
			return GS.OS7109;
		}
		
    	// 勘定科目名称に入力禁止文字が含まれている場合
		if (inpChk.haveKinshiMoji(form.getKanjo_nm())) {
			// エラーダイアログ（err.prohibitted）を表示する。
			for (int i = 0; i < form.getKanjo_nm().length(); i++) {
				String kinshiChar = form.getKanjo_nm().substring(i, i + 1);
				if (inpChk.haveKinshiMoji(kinshiChar)) {
					appContext.setFocusField(KANJO_NM);
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);					
					return GS.OS7109;
				}
			}
		}
				
		// システムセレクトボックスで「GSS」が選択されている場合
		if (GS.GSS.equals(form.getSystem_kbn())) {
			
			// 内分類コードが未入力の場合
			// 課題No.104
			// 修正開始
			//if (GS.EMPTY_CHARCTER.equals(form.getKanjo_uchi_cd())) {
			if (GS.EMPTY_CHARCTER.equals(Function.trim(form.getKanjo_uchi_cd()))) {
			// 修正完了
				appContext.setFocusField(KANJO_UCHI_CD);
				appContext.setMsgCode(GL.ERR_INPUT, GL.OS7109_KANJO_UCHI_CD);
				return GS.OS7109;
			}
			
			// 内分類名称が未入力の場合 
			if (GS.EMPTY_CHARCTER.equals(form.getKanjo_uchi_nm())) {
				appContext.setFocusField(KANJO_UCHI_NM);
				appContext.setMsgCode(GL.ERR_INPUT, GL.OS7109_KANJO_UCHI_NM);
				return GS.OS7109;
			}
			
			// 課題No.104
			// 修正開始
			// 内分類コードが半角英数字以外で入力された場合
			if (!Function.matches(form.getKanjo_uchi_cd(),UTIBUNRUI_PATTERN)) {
				appContext.setFocusField(KANJO_UCHI_CD);
				appContext.setMsgCode(GL.ERR_HANKAKUONLY, GL.OS7109_KANJO_UCHI_CD);
				return GS.OS7109;
			}
			//if (!inpChk.isNumLetter(form.getKanjo_uchi_cd())) {
				//appContext.setFocusField(KANJO_UCHI_CD);
				//appContext.setMsgCode(GL.ERR_NUMERICONLY, GL.OS7109_KANJO_UCHI_CD);
				//return GS.OS7109;
			//}
			// 修正完了
			
			// 内分類名称が50バイトを超える場合
			if (inpChk.lenB(form.getKanjo_uchi_nm()) > 50) {
				appContext.setFocusField(KANJO_UCHI_NM);
				appContext.setMsgCode(GL.ERR_LENGTH, GL.OS7109_KANJO_UCHI_NM);
				return GS.OS7109;
			}
			
	    	// 内分類名称に入力禁止文字が含まれている場合
			if (inpChk.haveKinshiMoji(form.getKanjo_uchi_nm())) {
				// エラーダイアログ（err.prohibitted）を表示する。
				for (int i = 0; i < form.getKanjo_uchi_nm().length(); i++) {
					String kinshiChar = form.getKanjo_uchi_nm().substring(i, i + 1);
					if (inpChk.haveKinshiMoji(kinshiChar)) {
						appContext.setFocusField(KANJO_UCHI_NM);
						// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
						appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);					
						return GS.OS7109;
					}
				}
			}
						
		} else if (GS.FOCUS.equals(form.getSystem_kbn())
					|| GS.MTS.equals(form.getSystem_kbn())) {
			
			// 債権フラグセレクトボックスが「対象外」以外の場合で、DR/CR区分がブランクの場合
			if ((!"0".equals(form.getSaiken_flg()))
				&& GS.EMPTY_CHARCTER.equals(form.getDrcr_kbn())) {
				appContext.setFocusField(DRCR_KBN);
				appContext.setMsgCode(GL.ERR_SELECT, GL.OS7109_DRCR_KBN);
				return GS.OS7109;
			}
		}
		
		// 重複チェック
		if (dbacc.getKanjyoCdCnt() >= 1) {
			appContext.setFocusField(KANJO_CD);
			appContext.setMsgCode(GL.ERR_CC_REGISTERED, GL.OS7109_KANJO_CD);
			return GS.OS7109;
		}
		
		// M14_勘定科目マスタの登録
		dbacc.setInsertM1400();
		
		
		return GS.OS7108;
	}
}
