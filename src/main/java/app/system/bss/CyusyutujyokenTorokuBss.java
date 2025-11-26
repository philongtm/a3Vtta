/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/12/21		SSC				課題No.220 システムプルダウン変更時、汎用２プルダウンを初期化 
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.TyusyutuJokenBean;
import app.system.dbAcc.CyusyutujyokenTorokuDbAcc;
import app.system.form.CyusyutujyokenTorokuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * OS7111_抽出条件メンテナンス_登録 ビジネスロジッククラス <br>
 */
public class CyusyutujyokenTorokuBss {

	private AppContext appContext = null;		// ＡＰＰコンテキスト
    private SqlExecuter sqlExec = null;		// ＤＢアクセス
    private Log log = null;					// LOG
    private SessionData cmnData;				// セッション情報
    private TyusyutuJokenBean joken_bean;		// 抽出条件情報
    private CyusyutujyokenTorokuForm form;		// OS7111_抽出条件メンテナンス_登録アクションフォーム

    private static final int ERR_ORA_00001				= -1;			//エラーコードORA_00001
    private static final String USD = "USD";
	private static final String JPY = "JPY";
	private static final String QUARTER_CHARCTER = "2";
    /**
     * コンストラクタ <br>
     * 
     * @param appContext AppContext
     * @throws Exception Exception
     */
    public CyusyutujyokenTorokuBss(AppContext appContext) throws Exception {
        this.appContext = appContext;
        this.log = appContext.getLog();
        this.cmnData = appContext.getCMN();
        this.joken_bean = cmnData.getJoken_bean();
        this.form = (CyusyutujyokenTorokuForm)appContext.getActionForm();
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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);
        // システムセレクトボックス
        dbacc.getSystemKbnList();
        // 汎用１セレクトボックス
        dbacc.getHanyo1List();
        // 汎用２セレクトボックス
        dbacc.getHanyo2List();
        // 決算期区分セレクトボックス
        dbacc.getKesanKbnList();
        // 基準日セレクトボックス
        dbacc.getKijunbiList();
        // 格付セレクトボックス
        dbacc.getKakudukeList();
        // 金額基準2（滞留区分）セレクトボックス
        dbacc.getKingakuJoken2KbnList();
        // 過去格付（From、To）セレクトボックス
        dbacc.getKakokakudukeList();
        // （金額基準2）通貨
        if (GS.GSS.equals(form.getSystemKbn())) {
			form.setTukaCd(GS.KAKKO_HIDARI + JPY + GS.KAKKO_MIGI);
		} else {
			form.setTukaCd(GS.KAKKO_HIDARI + USD + GS.KAKKO_MIGI);
		}

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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);

        // 条件No
        form.setJokenNo(joken_bean.getJoken_no());
        
        // 抽出事由
        form.setCyusyutuJiyu(joken_bean.getTyusyutu_jiyu());
        // 条件名称(日本語)
        form.setJyokenNmJp(joken_bean.getJoken_nm());
        // 条件名称(英語)
        form.setJyokenNmEn(joken_bean.getJoken_nm_en());
        
        // システム
        form.setSystemKbn(joken_bean.getSystem_kbn_nm());
        // 汎用１
        form.setHanyo1(joken_bean.getBunrui1());
        // 汎用２
        form.setHanyo2(joken_bean.getBunrui2());
        // 決算期区分
        form.setKesanKbn(joken_bean.getKessanki_kbn());
        // 基準日
        form.setKijunbi(joken_bean.getKijunbi());
        
        // 格付
        form.setKakuduke(joken_bean.getKtk());
        // 金額基準1
        form.setKingakuJyoken1(joken_bean.getKingaku1_kingaku());
        // （金額条件）通貨
        form.setTukaCd(joken_bean.getKingaku_tuuka());
        // 金額基準2（滞留区分）
        form.setKingakuJyoken2Kbn(joken_bean.getKingaku2_tairyu());
        // 金額基準2（金額）
        form.setKingakuJyoken2(joken_bean.getKingaku2_kingaku());
        // 滞留期間（From）
        form.setTairyuKikanFrom(joken_bean.getTairyu_kikan_from());
        // 滞留期間（To）
        form.setTairyuKikanTo(joken_bean.getTairyu_kikan_to());
        // 実施滞留判定対象
        form.setTairyuTaisyo(joken_bean.getTairyu_hantei_taisyo());
        // 査定対象
        form.setSateiTaisyo(joken_bean.getSatei_taisyo());
        // 過去格付フラグ
        form.setKakoKakudukeFlg(joken_bean.getKako_ktk_flg());
        // 過去格付（From）
        form.setKakoKakudukeFrom(joken_bean.getKako_ktk_from());
        // 過去格付（To）
        form.setKakoKakudukeTo(joken_bean.getKako_ktk_to());
        // 過去格付参照時点
        form.setKakoKakudukeJiten(joken_bean.getKako_ktk_sansyo_jiten());

        // 格付セレクトボックス
        dbacc.getKakudukeList();
        // 金額基準2（滞留区分）セレクトボックス
        dbacc.getKingakuJoken2KbnList();
        // 過去格付（From、To）セレクトボックス
        dbacc.getKakokakudukeList();

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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);
    	// 入力チェック
    	if (!this.doCheckRegist()) {
    		return false;
    	}
    	// 重複チェック
    	if (dbacc.checkJiyuM07()) {
    		// エラーメッセージ(err.registered、機)条件名称(日本語)、機)条件名称(英語))を表示する。
    		String jyokennm;
    		
    		if(form.getJyokenNmJp().equals(GS.EMPTY_CHARCTER)) {
    			jyokennm = form.getJyokenNmEn();
    		}
    		else if(form.getJyokenNmEn().equals(GS.EMPTY_CHARCTER)) {
    			jyokennm = form.getJyokenNmJp();
    		}
    		else {
    			jyokennm = form.getJyokenNmJp() + GS.COMMA + form.getJyokenNmEn();
    		}		
    		
    		appContext.setMsgCd(GL.ERR_REGISTERED,  jyokennm);
    		return false;
    	}
    	// 最大条件NOを取得する。
    	int maxJokenNo = dbacc.getMaxJokenNo();
        // M07_検討対象先抽出条件マスタの登録
        if (dbacc.insertM07(maxJokenNo) == ERR_ORA_00001) {
        	// 登録時に一意制約違反(ORA-00001)になった場合、エラーメッセージ(err.Re-registration)表示する。
    		appContext.setMsgCode(GL.ERR_RE_REGISTRATION);
    		return false;
        }
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
    	
    	// 抽出事由が未入力の場合
    	String jiyu = form.getCyusyutuJiyu();
    	if (check.isNullBlank(jiyu)) {
            // エラーメッセージ(err.input、ラベル名キー(抽出事由))を表示する
            appContext.setMsgCode(GL.ERR_INPUT, GL.OS7111_CHUSYUTUJIYU);
            return false;
    	}
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
    	String hanyo2 = form.getHanyo2();
    	if (check.isNullBlank(hanyo2)) {
            // エラーメッセージ(err.select、共)ラベル名６)を表示する
            appContext.setMsgCd(GL.ERR_SELECT, cmnData.getLbl_nm6());
            return false;
    	}
        // 決算期区分が未設定の場合は
    	String kesanKbn = form.getKesanKbn();
    	if (check.isNullBlank(kesanKbn)) {
            // エラーメッセージ(err.select、ラベル名(決算期区分))を表示する
            appContext.setMsgCode(GL.ERR_SELECT, GL.OS7111_KESANKIKBN);
            return false;
    	}
        // 基準日が未設定の場合は
    	String kijunbi = form.getKijunbi();
    	if (check.isNullBlank(kijunbi)) {
            // エラーメッセージ(err.select、ラベル名(基準日))を表示する
            appContext.setMsgCode(GL.ERR_SELECT, GL.OS7111_KIJYUNBI);
            return false;
    	}
    	// 更新チェック
    	if (!this.doCheckUpdate()) {
    		return false;
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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);

		// 汎用１セレクトボックス値取得
		dbacc.getHanyo1List();
		
		// 課題No.220
		// 追加開始
        // 汎用２セレクトボックス
        dbacc.getHanyo2List();
        // 追加完了
        
        // 決算期区分セレクトボックス
        dbacc.getKesanKbnList();
        // 基準日セレクトボックス
        dbacc.getKijunbiList();
		
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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);

		// 汎用２セレクトボックス値取得
		dbacc.getHanyo2List();	
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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);
    	// 入力チェック
    	if (!this.doCheckUpdate()) {
    		return false;
    	}
        
        // M07_検討対象先抽出条件マスタの更新
        dbacc.updateM07();
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
        // 条件名称(日本語)が40バイト以上が入力された場合
    	String jokenNmJp = form.getJyokenNmJp();
        if (check.lenB(jokenNmJp) > 40) {
			// エラーメッセージ(err.length、ラベル名キー(条件名称(日本語))を表示する
            appContext.setMsgCode(GL.ERR_LENGTH, GL.OS7111_JYOKENNIHONNGO);
            return false;
		}
        // 条件名称(英語)が60バイト以上が入力された場合
    	String jokenNmEn = form.getJyokenNmEn();
        if (check.lenB(jokenNmEn) > 60) {
			// エラーメッセージ(err.length、ラベル名キー(条件名称(英語))を表示する
            appContext.setMsgCode(GL.ERR_LENGTH, GL.OS7111_JYOKENEIGO);
            return false;
		}
        // 滞留期間が半角数字以外で入力された場合
		String tairyuFrom = form.getTairyuKikanFrom();
        if (!check.isNullBlank(tairyuFrom)) {
    		if (!check.isNumeric(tairyuFrom)) {
    			// エラーメッセージ(err.numericOnly、ラベル名キー(滞留期間))を表示する
    			appContext.setMsgCode(GL.ERR_NUMERICONLY, GL.OS7111_TAIRYUKIKAN);
    			return false;
    		}
        }
		String tairyuTo = form.getTairyuKikanTo();
        if (!check.isNullBlank(tairyuTo)) {
    		if (!check.isNumeric(tairyuTo)) {
    			// エラーメッセージ(err.numericOnly、ラベル名キー(滞留期間))を表示する
    			appContext.setMsgCode(GL.ERR_NUMERICONLY, GL.OS7111_TAIRYUKIKAN);
    			return false;
    		}
        }
    	// 金額が整数部13桁以上、または小数部が、3桁以上の場合
		String kingaku1 = form.getKingakuJyoken1();
		if (!this.checkKingaku(kingaku1, 13, 3)) {
			// エラーメッセージ(err.digits、ラベル名キー(金額、'12'、'2'))を表示する
			List<String> params = new ArrayList<String>();
			params.add(GL.ERR_DIGITS);
			params.add(GL.OS7111_KINGAKU);
			params.add("12");
			params.add("2");
			appContext.setMsgCode(params);
			return false;
		}
		String kingaku2 = form.getKingakuJyoken2();
		if (!this.checkKingaku(kingaku2, 13, 3)) {
			// エラーメッセージ(err.digits、ラベル名キー(金額、'12'、'2'))を表示する
			List<String> params = new ArrayList<String>();
			params.add(GL.ERR_DIGITS);
			params.add(GL.OS7111_KINGAKU);
			params.add("12");
			params.add("2");
			appContext.setMsgCode(params);
			return false;
		}
		// 決算期区
		String kesankiKbn = form.getKesanKbn();
		// [過去格付条件]
		String kakoFlg = form.getKakoKakudukeFlg();
		String kakoFrom = form.getKakoKakudukeFrom();
		String kakoTo = form.getKakoKakudukeTo();
		String kakoJiten = form.getKakoKakudukeJiten();
		// 決算期区分が第1/3四半期以外
		if (!QUARTER_CHARCTER.equals(kesankiKbn)) {
			// [過去格付条件]に値が設定されている場合
			if (GS.ON.equals(kakoFlg) || !check.isNullBlank(kakoFrom) || !check.isNullBlank(kakoTo)
					|| !check.isNullBlank(kakoJiten)) {
				// エラーメッセージ(err.notRating)を表示する
				appContext.setMsgCode(GL.ERR_NOTRATING);
				return false;
			}
		}
        // 条件名称(日本語)に入力禁止文字が含まれている場合
		if (!check.isNullBlank(jokenNmJp) && check.haveKinshiMoji(jokenNmJp)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < jokenNmJp.length(); i++) {
				String kinshiChar = jokenNmJp.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}
        // 条件名称(英語)に入力禁止文字が含まれている場合
		if (!check.isNullBlank(jokenNmEn) && check.haveKinshiMoji(jokenNmEn)) {
			// エラーメッセージ(err.prohibitted、入力禁止文字)を表示する。
			for (int i = 0; i < jokenNmEn.length(); i++) {
				String kinshiChar = jokenNmEn.substring(i, i + 1);
				if (check.haveKinshiMoji(kinshiChar)) {
					// チェックで最初に該当した入力禁止文字をエラーダイアログに組み込んで表示。
					appContext.setMsgCd(GL.ERR_PROHIBITTED, kinshiChar);
					return false;
				}
			}
		}
        // 過去格付参照時点が半角数字以外で入力された場合
        if (!check.isNullBlank(kakoJiten)) {
    		if (!check.isNumeric(kakoJiten)) {
				// エラーメッセージ(err.numericOnly、ラベル名キー(過去格付参照時点))を表示する
    			appContext.setMsgCode(GL.ERR_NUMERICONLY, GL.OS7111_KAKOJITEN);
    			return false;
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
        CyusyutujyokenTorokuDbAcc dbacc = new CyusyutujyokenTorokuDbAcc(sqlExec, log, appContext);
    	// 存在チェック
    	if (dbacc.checkJiyuT02()) {
    		
    		// エラーメッセージ(err.notDelete、機)条件名称(日本語)、機)条件名称(英語))を表示する。
    		if (GS.LANG_JA.equals(cmnData.getComLangMode())) {
				appContext.setMsgCd(GL.ERR_NOTREGISTERD, form.getJyokenNmJp());
			} else {
				appContext.setMsgCd(GL.ERR_NOTREGISTERD, form.getJyokenNmEn());
			}
    		return false;
    	}
 
        // M07_検討対象先抽出条件マスタの削除
        dbacc.deleteM07();
        // コミット
        dbacc.commit();

        return true;
    }
    
    /**
     * 金額の長さをチェックする。 <br>
     * 
     * @param kingaku 金額
     * @param intLen 小数点前の長さ
     * @param decLen 小数点後の長さ
     * @return 適合の場合：true
     */
    private boolean checkKingaku(String kingaku, int intLen, int decLen) {
		InputCheck check = new InputCheck();
		if (check.isNullBlank(kingaku)) {
			return true;
		}
		kingaku = kingaku.replace(GS.COMMA, GS.EMPTY_CHARCTER);
		if (kingaku.indexOf(".") > 0) {
			if (kingaku.indexOf(".") >= intLen) {
				return false;
			}
			if (kingaku.length() - kingaku.indexOf(".") - 1 >= decLen) {
				return false;
			}
		} else {
			if (kingaku.length() >= intLen) {
				return false;
			}
		}
		return true;
	}
}