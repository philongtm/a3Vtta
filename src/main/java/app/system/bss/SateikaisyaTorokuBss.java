/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.SateiKaisyaBean;
import app.SessionData;
import app.system.dbAcc.SateikaisyaTorokuDbAcc;
import app.system.form.SateikaisyaTorokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

/**
 * OS7103_査定会社メンテナンス_登録 ビジネスロジッククラス <br>
 */
public class SateikaisyaTorokuBss {

    private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private SessionData cmnData;				// セッション情報
    private SateiKaisyaBean satei_kaisya_bean;				// 共)査定会社情報
    private SateikaisyaTorokuForm form;					// 査定会社メンテナンス_登録アクションフォーム

    
    private static final int MAX_LEN_HANYO2				= 3;			// 汎用２の最大長さ
    private static final int MAX_LEN_HANYO2NM			= 80;			// 汎用２名称の最大長さ

    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public SateikaisyaTorokuBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.satei_kaisya_bean = cmnData.getSateikaisya_bean();
        this.form = (SateikaisyaTorokuForm)appContext.getActionForm();
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)新規 <br>
     * 
     * @return forward
     * @throws Exception Exception
     */
    public String executeInitRegist() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);
        // システムセレクトボックス
        dbacc.getSystemKbnList();
        // 汎用１セレクトボックス
        dbacc.getHanyo1List();
        // 標準時刻セレクトボックス
        dbacc.getHyojunJikokuList();

        return GS.OS7103;
    }

    /**
     * 画面初期表示値取得(メニューリンクから遷移時)更新 <br>
     * 
     * @return forward
     * @throws Exception Exception
     */
    public String executeInitUpdate() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);

        // システム
        form.setSystemKbn(satei_kaisya_bean.getSystem_kbn());
        // 汎用１
        form.setHanyo1(satei_kaisya_bean.getBunrui1());
        // 汎用２
        form.setHanyo2(satei_kaisya_bean.getBunrui2());
        // 汎用２名称(日本語)
        form.setHanyo2Jp(satei_kaisya_bean.getBunrui2_nm_ja());
        // 汎用２名称(英語) 
        form.setHanyo2En(satei_kaisya_bean.getBunrui2_nm_en());
        // 抽出対象
        form.setTyusyutu_taisyo_flg(satei_kaisya_bean.getTyusyutu_taisyo_flg());
        // 標準時刻
        form.setHyojunJikokuCd(satei_kaisya_bean.getStandard_time());
        // 会社コード
        form.setKaisyaCd(satei_kaisya_bean.getKaisya_cd());
        
        // 標準時刻セレクトボックス
        dbacc.getHyojunJikokuList();

        return GS.OS7103;
    }

    /**
     * 登録処理 <br>
     * 
     * @return 成功フラグ
     * @throws Exception Exception
     */
    public boolean doRegist() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);
    	// 入力チェック
    	if (!this.doCheckRegist()) {
    		return false;
    	}
    	// 重複チェック
    	if (dbacc.checkHanyo2()) {
    		// エラーメッセージ(err.registered、共)ラベル名３)を表示する
    		appContext.setMsgCd(GL.ERR_REGISTERED ,appContext.getCMN().getLbl_nm3());
    		return false;
    	}
        
        // M04_査定会社設定マスタの登録
        dbacc.insertM04();
        // コミット
        dbacc.commit();

        return true;
    }

    /**
     * 登録するときの入力チェック <br>
     * 
     * @return エラーなし：true
     * @throws Exception Exception
     */
    private boolean doCheckRegist() throws Exception {
    	
    	// 入力チェック
    	InputCheck check = new InputCheck();
    	// システムが未設定の場合は
    	String systemKbn = form.getSystemKbn();
    	if (check.isNullBlank(systemKbn)) {
            // エラーメッセージ(err.select、ラベル名キー(システム))を表示する
            appContext.setMsgCode(GL.ERR_SELECT, GL.OS7103_SYSTEM);
            return false;
    	}
        // 汎用１が未設定の場合は
    	String hanyo1 = form.getHanyo1();
    	if (check.isNullBlank(hanyo1)) {
            // エラーメッセージ(err.select、共)ラベル名１)を表示する
            appContext.setMsgCd(GL.ERR_SELECT, cmnData.getLbl_nm1());
            return false;
    	}
        // 汎用２が未設定の場合は
    	String hanyo_2 = form.getHanyo2();
    	if (check.isNullBlank(hanyo_2)) {
            // エラーメッセージ(err.select、共)ラベル名３)を表示する
            appContext.setMsgCd(GL.ERR_SELECT, cmnData.getLbl_nm3());
            return false;
    	}
        // 汎用２が3バイトを超える場合
    	String hanyo2 = form.getHanyo2();
        if (check.lenB(hanyo2) > MAX_LEN_HANYO2) {
			// エラーメッセージ(err.length、共)ラベル名３)を表示する。
            appContext.setMsgCd(GL.ERR_LENGTH, cmnData.getLbl_nm3());
            return false;
		}
        // 汎用２名称(日本語)がが80バイトを超える場合
        String hanyo2Jp = form.getHanyo2Jp();
        if (check.lenB(hanyo2Jp) > MAX_LEN_HANYO2NM) {
			// エラーメッセージ(err.length、共)ラベル名３)を表示する。
            appContext.setMsgCd(GL.ERR_LENGTH, cmnData.getLbl_nm3()
					+ appContext.getMsg(GL.OS7103_MEISYO) + appContext.getMsg(GL.OS7103_NIHONGO));
            return false;
		}
        // 汎用２名称（英語）が80バイトを超える場合
        String hanyo2En = form.getHanyo2En();
        if (check.lenB(hanyo2En) > MAX_LEN_HANYO2NM) {
			// エラーメッセージ(err.length、共)ラベル名３)を表示する。
            appContext.setMsgCd(GL.ERR_LENGTH, cmnData.getLbl_nm3()
					+ appContext.getMsg(GL.OS7103_MEISYO) + appContext.getMsg(GL.OS7103_EIGO));
            return false;
		}
        // 汎用２名称(日本語)に入力禁止文字が含まれている場合
		if (!check.isNullBlank(hanyo2Jp) && check.haveKinshiMoji(hanyo2Jp)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < hanyo2Jp.length(); i++) {
				String kinshiChar = hanyo2Jp.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}
        // 汎用２名称（英語）に入力禁止文字が含まれている場合
		if (!check.isNullBlank(hanyo2En) && check.haveKinshiMoji(hanyo2En)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < hanyo2En.length(); i++) {
				String kinshiChar = hanyo2En.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}

        return true;
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
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();	
    }

    /**
     * 更新処理 <br>
     * 
     * @return 成功フラグ
     * @throws Exception Exception
     */
    public boolean doUpdate() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);
    	// 入力チェック
    	if (!this.doCheckUpdate()) {
    		return false;
    	}
        
        // M04_査定会社設定マスタの更新
        dbacc.updateM04();
        // コミット
        dbacc.commit();

        return true;
    }

    /**
     * 更新するときの入力チェック <br>
     * 
     * @return エラーなし：true
     * @throws Exception Exception
     */
    private boolean doCheckUpdate() throws Exception {
    	// 入力チェック
    	InputCheck check = new InputCheck();
        // 汎用２名称(日本語)がが80バイトを超える場合
        String hanyo2Jp = form.getHanyo2Jp();
        if (check.lenB(hanyo2Jp) > MAX_LEN_HANYO2NM) {
			// エラーメッセージ(err.length、共)ラベル名３)を表示する。
            appContext.setMsgCd(GL.ERR_LENGTH, cmnData.getLbl_nm3()
					+ appContext.getMsg(GL.OS7103_MEISYO) + appContext.getMsg(GL.OS7103_NIHONGO));
            return false;
		}
        // 汎用２名称（英語）が80バイトを超える場合
        String hanyo2En = form.getHanyo2En();
        if (check.lenB(hanyo2En) > MAX_LEN_HANYO2NM) {
			// エラーメッセージ(err.length、共)ラベル名３)を表示する。
            appContext.setMsgCd(GL.ERR_LENGTH, cmnData.getLbl_nm3()
					+ appContext.getMsg(GL.OS7103_MEISYO) + appContext.getMsg(GL.OS7103_EIGO));
            return false;
		}
        // 汎用２名称(日本語)に入力禁止文字が含まれている場合
		if (!check.isNullBlank(hanyo2Jp) && check.haveKinshiMoji(hanyo2Jp)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < hanyo2Jp.length(); i++) {
				String kinshiChar = hanyo2Jp.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}
        // 汎用２名称（英語）に入力禁止文字が含まれている場合
		if (!check.isNullBlank(hanyo2En) && check.haveKinshiMoji(hanyo2En)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < hanyo2En.length(); i++) {
				String kinshiChar = hanyo2En.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}

        return true;
    }

    /**
     * 削除処理 <br>
     * 
     * @return 成功フラグ
     * @throws Exception Exception
     */
    public boolean doDelete() throws Exception {
        // コネクションの取得
        this.sqlExec = appContext.getSqlExecuter();
        SateikaisyaTorokuDbAcc dbacc = new SateikaisyaTorokuDbAcc(sqlExec, log, appContext);
    	// 存在チェック
    	if (dbacc.checkSateikaisya()) {
    		// エラーメッセージ(err.notDelete)を表示する
    		appContext.setMsgCode(GL.ERR_NOTERASES);
    		return false;
    	}
        
        // M04_査定会社設定マスタの削除
        dbacc.deleteM04();
        // コミット
        dbacc.commit();

        return true;
    }
}