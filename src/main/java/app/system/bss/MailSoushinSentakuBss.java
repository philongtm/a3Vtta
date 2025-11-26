/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/03/23		SSC				新規作成
******************************************************************************/

package app.system.bss;

import app.system.dbAcc.MailSoushinSentakuDbAcc;
import app.system.form.MailSoushinSentakuForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OS7115 メール送信先選択 ビジネス ロジッククラス <br>
 */
public class MailSoushinSentakuBss {
	private AppContext appContext = null;					// ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null;						// ＤＢアクセス
	private MailSoushinSentakuForm form = null;				// アクションフォーム
	private Log log = null;									// LOG

	private static final String HANYOU1					= "hanyou1";					// 汎用1(表示用)
	private static final String HANYOU2  				= "hanyou2";					// 汎用2(表示用)
	private static final String HANYOU3					= "hanyou3";					// 汎用3(表示用)

	/**
	 * コンストラクタ
	 */
	public MailSoushinSentakuBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		form = (MailSoushinSentakuForm)appContext.getActionForm();
		this.log = appContext.getLog();
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時)
	 */
	public String executeInit() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		MailSoushinSentakuDbAcc dbacc = new MailSoushinSentakuDbAcc(sqlExec, log, appContext);

		// 当画面の汎用項目ラベルを取得する
		dbacc.getHanyouTitle();

		// 汎用1セレクトボックスの設定値を取得する
		dbacc.getHanyou1();

		// 汎用2セレクトボックスの設定値を取得する
		dbacc.getHanyou2();

		// 最新のみ取得
		form.setKensakuFlg("1");

		return GS.OS7115;
	}

	/**
	 * 画面初期表示値取得(メニューリンク以外から遷移時)
	 */
	public String execute() throws Exception {
		// 検索したときの条件を取得する
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		MailSoushinSentakuDbAcc dbacc = new MailSoushinSentakuDbAcc(sqlExec, log, appContext);
		dbacc.getMeisai();

		return GS.OS7115;
	}

	/**
	 *
	 * 汎用2セレクトボックスの設定値を取得する <br>
	 *
	 * @return
	 * @throws Exception
	 */
	public String doChange1() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		MailSoushinSentakuDbAcc dbacc = new MailSoushinSentakuDbAcc(sqlExec, log, appContext);
		form.setHanyou2(null);
		// 汎用2セレクトボックスの設定値を取得する
		dbacc.getHanyou2();

		return GS.OS7115;
	}

	/**
	 *
	 * 検索アクション <br>
	 *
	 * @return
	 * @throws Exception
	 */
	public String doSearch() throws Exception {
		// 検索したときの条件を設定する
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		MailSoushinSentakuDbAcc dbacc = new MailSoushinSentakuDbAcc(sqlExec, log, appContext);
		if("1".equals(form.getKensakuFlg())){
			// 最新のみ取得の場合
			dbacc.getKijyunbi();
		} else {
			form.setKensakuYm("");
		}
		dbacc.getMeisai();

		return GS.OS7115;
	}


	/**
	 * 選択反映ボタン 入力チェック <br>
	 *
	 * @return
	 * @throws Exception
	 */
	public String doCloose() throws Exception {
		InputCheck inChk = new InputCheck();
		String errMsg = GL.ERR_NOTSET;

		// 検索結果の明細
		List<HashMap<String, String>> ar_meisai = (List<HashMap<String, String>>)form.getAr_meisai();
		// 明細一件分を格納
		HashMap<String, String> ar_meisai_hm;
		// 選択された明細
		List<HashMap<String, String>> sentaku_ar_meisai = new ArrayList<HashMap<String, String>>();

		// 選択されたチェックボックス（列番号）
		if(!inChk.isNullBlank(form.getSoushin_chk())){
			String[] chkBoxList = form.getSoushin_chk().split(",", 0);
			// 選択されたチェックボックスの件数（チェックのループ回数）
			int length = chkBoxList.length;
			// 選択されたチェックボックスの番号
			int chkNo = 0;

			String maeHanyou1 = "";		// 前汎用1
			String maeHanyou2 = "";		// 前汎用2
			String maeHanyou3 = "";		// 前汎用3

			String hanyou1 = "";		// 汎用1
			String hanyou2 = "";		// 汎用2
			String hanyou3 = "";		// 汎用3


			for (int i =0 ;length > i; i++){
				// 選択されたチェックボックスの番号から、該当する明細を取得する
				chkNo = Integer.parseInt(chkBoxList[i]);
				ar_meisai_hm = ar_meisai.get(chkNo);

				// チェックされていたら汎用を比較する
				hanyou1 = ar_meisai_hm.get(HANYOU1);
				hanyou2 = ar_meisai_hm.get(HANYOU2);
				hanyou3 = ar_meisai_hm.get(HANYOU3);

				if (maeHanyou1.equals(hanyou1) && inChk.isNullBlank(maeHanyou2) && inChk.isNullBlank(maeHanyou3)) {
					if(!inChk.isNullBlank(hanyou2)){
						// 前が会社指定の場合、以降で部門・部の指定をしていたらエラーにし、処理終了
						errMsg = GL.ERR_HANEI;
						break;
					}
				} else if (maeHanyou1.equals(hanyou1) && maeHanyou2.equals(hanyou2) && inChk.isNullBlank(maeHanyou3)){
					if(!inChk.isNullBlank(hanyou3)){
						// 前が部門指定の場合、以降で部の指定をしていたらエラーにし、処理終了
						errMsg = GL.ERR_HANEI;
						break;
					}
				}
				// 次のチェック用に、前汎用に格納する
				maeHanyou1 = ar_meisai_hm.get(HANYOU1);
				maeHanyou2 = ar_meisai_hm.get(HANYOU2);
				maeHanyou3 = ar_meisai_hm.get(HANYOU3);

				sentaku_ar_meisai.add(ar_meisai_hm);
			}

			// エラーメッセージがある場合
			if(GL.ERR_HANEI.equals(errMsg)){
				List<String> msgList = new ArrayList<String>();
				msgList.add(errMsg);
				appContext.setMsgCode(msgList);
				form.setErrChkFlg("1");	// エラーチェックのフラグ
			} else {
				// 上記以外の場合、フォームにセットする
				form.setSoushin_meisai(sentaku_ar_meisai);
			}
		}

		return GS.OS7115;
	}


}
