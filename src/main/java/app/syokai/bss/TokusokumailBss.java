/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/02/25		SSC				新規作成
 ******************************************************************************/
package app.syokai.bss;

import app.SessionData;
import app.TorihikisakiBean;
import app.UserBean;
import app.syokai.dbAcc.TokusokumailDbAcc;
import app.syokai.form.TokusokumailForm;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OS6105_督促メール送信選択 ビジネスロジッククラス
 */
public class TokusokumailBss {

	private AppContext appContext = null; // ＡＰＰコンテキスト
	private SqlExecuter sqlExec = null; // ＤＢアクセス
	private Log log = null; // LOG
	private SessionData cmnData; // 共通セッション
	private UserBean user_bean; // ユーザ情報
	private TorihikisakiBean tori_bean; // 取引先情報
	private TokusokumailForm form; // アクションフォーム

	private static final String ANKEN_NO				= "anken_no";		// 案件No.


	/**
	 * コンストラクタ
	 */
	public TokusokumailBss(AppContext appContext) throws Exception {
		this.appContext = appContext;
		this.log = appContext.getLog();
		this.cmnData = appContext.getCMN();
		this.user_bean = cmnData.getUser_bean();
		this.tori_bean = cmnData.getTori_bean();
		this.form = (TokusokumailForm) appContext.getActionForm();
	}

	/**
	 * 【画面初期表示処理(進捗状況詳細からの遷移時)】
	 *
	 * @return 督促メール送信選択画面の画面ID
	 * @throws Exception
	 */
	public String executeInit() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TokusokumailDbAcc dbacc = new TokusokumailDbAcc(sqlExec, log,
				appContext);

		/*
		 * 送信先選択セレクトボックス
		 */
		dbacc.getSoushinbuList();
		form.setSoushinbu("10");


		// カレントタブ
		form.setKarento_tab(tori_bean.getPhase_handan());
		// 前回の配信日時を取得
		dbacc.getHaishinDt(tori_bean.getAnken_no());
		// 前回の保存日時を取得
		dbacc.getHozonDt(tori_bean.getAnken_no());

		InputCheck check = new InputCheck();

		if(form.getKarento_tab().equals("2")){
			/*
			 * 査定タブ
			 */

			dbacc.getMaeSateikiS();

			if(!check.isNullBlank(form.getMae_sateiki())){
				// 前査定期がある
				if(!tori_bean.getSatei_ki().equals(form.getMae_sateiki())){
					// 今と前の査定期が異なる場合、基準日区分をnullにする
					form.setMae_kijunbi_kbn(null);
				}

				// 取引先の案件No.を取得する
				dbacc.getAnkenNo();
				// シングルコーテーションで括り、カンマで区切る
				this.getFormatAnkenNo();
				// 取引先の滞留判定進捗一覧の表示
				dbacc.getSinchokuList(form.getAnken_no_ser());

			}
		} else {
			/*
			 * 滞留タブ
			 */
			dbacc.getMaeSateikiT();
			if(!check.isNullBlank(form.getMae_sateiki())){
				// 前査定期がある
				if(!tori_bean.getSatei_ki().equals(form.getMae_sateiki())){
					// 今と前の査定期が異なる場合、基準日区分をnullにする
					form.setMae_kijunbi_kbn(null);
				}

				// 取引先の滞留判定組織一覧の表示
				dbacc.getSosikiList();
				// 組織一覧データある場合
				if (form.getAr_sosiki() != null && form.getAr_sosiki().size() > 0) {

					// 検索用案件No.を取得する
					this.getAnkenNo();

					// 取引先の滞留判定進捗一覧の表示
					dbacc.getSinchokuList(form.getSrh_anken_no());
				}
				// コメント用画面ID
				form.setMail_gamen_id("OZ3101");
			}
		}
		/*
		 * 新規担当者選択
		 */
		// 担当者一覧取得
		dbacc.getTantoIchiran();

		return GS.OS6105;
	}

	/**
	 * 【画面初期表示処理】
	 *
	 * @return 督促メール送信選択画面の画面ID
	 * @throws Exception
	 */
	public String execute() throws Exception {
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TokusokumailDbAcc dbacc = new TokusokumailDbAcc(sqlExec, log,
				appContext);

		/*
		 * 送信先選択セレクトボックス
		 */
		dbacc.getSoushinbuList();

		InputCheck check = new InputCheck();

		// 前査定期、前基準日区分、前基準日、進捗一覧を初期化する
		form.setMae_sateiki(null);
		form.setMae_kijunbi_kbn(null);
		form.setMae_kijunbi(null);
		form.setAr_sinchoku(null);

		if(form.getKarento_tab().equals("2")){
			/*
			 * 査定タブ
			 */

			dbacc.getMaeSateikiS();

			if(!check.isNullBlank(form.getMae_sateiki())){
				// 前査定期がある
				if(!tori_bean.getSatei_ki().equals(form.getMae_sateiki())){
					// 今と前の査定期が異なる場合、基準日区分をnullにする
					form.setMae_kijunbi_kbn(null);
				}

				// 取引先の案件No.を取得する
				dbacc.getAnkenNo();
				// シングルコーテーションで括り、カンマで区切る
				this.getFormatAnkenNo();
				// 取引先の滞留判定進捗一覧の表示
				dbacc.getSinchokuList(form.getAnken_no_ser());
			}
		} else {
			/*
			 * 滞留タブ
			 */
			dbacc.getMaeSateikiT();
			if(!check.isNullBlank(form.getMae_sateiki())){
				// 前査定期がある
				if(!tori_bean.getSatei_ki().equals(form.getMae_sateiki())){
					// 今と前の査定期が異なる場合、基準日区分をnullにする
					form.setMae_kijunbi_kbn(null);
				}

				// 取引先の滞留判定組織一覧の表示
				dbacc.getSosikiList();
				// 組織一覧データある場合
				if (form.getAr_sosiki() != null && form.getAr_sosiki().size() > 0) {

					// 検索用案件No.を取得する
					this.getAnkenNo();

					// 取引先の滞留判定進捗一覧の表示
					dbacc.getSinchokuList(form.getSrh_anken_no());
				}
			}
		}

		form.setRadio_id(GS.EMPTY_CHARCTER);

		return GS.OS6105;
	}

	/**
	 * 検索用案件No.を取得する <br>
	 *
	 * @throws Exception
	 */
	private void getAnkenNo() throws Exception {
		// 共)取引先情報を取得する
		SessionData cmnData = appContext.getCMN();
		TorihikisakiBean tori_bean = cmnData.getTori_bean();
		// 検索用案件No.(一件目)
		String srh_anken_no1 = GS.EMPTY_CHARCTER;
		// 案件No.
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
				srh_anken_no1 = GS.SINGLE_QUOTATION + sosiki.get(ANKEN_NO)
						+ GS.SINGLE_QUOTATION;
			}
		}

		if (gaito_flg) {
			// 共)取引先情報.案件No.が機)組織一覧.案件No.のいずれかに該当する場合
			form.setSrh_anken_no(GS.SINGLE_QUOTATION + tori_bean.getAnken_no()
					+ GS.SINGLE_QUOTATION);
		} else {
			// 該当無しの場合
			form.setSrh_anken_no(srh_anken_no1);
		}
	}

	/**
	 * シングルコーテーションで括り、カンマで区切る <br>
	 *
	 * @throws Exception
	 */
	private void getFormatAnkenNo() throws Exception {

		String anken_no_ser = GS.EMPTY_CHARCTER;

		for (int i = 0; i < form.getAr_anken_no().size(); i++) {
			HashMap<String, String> hm = (HashMap<String, String>) form
					.getAr_anken_no().get(i);

			if (GS.EMPTY_CHARCTER.equals(anken_no_ser)) {
				anken_no_ser = GS.SINGLE_QUOTATION
						+ String.valueOf(hm.get(ANKEN_NO))
						+ GS.SINGLE_QUOTATION;
			} else {
				anken_no_ser += GS.COMMA + GS.SINGLE_QUOTATION
						+ String.valueOf(hm.get(ANKEN_NO))
						+ GS.SINGLE_QUOTATION;
			}
		}

		if (GS.EMPTY_CHARCTER.equals(anken_no_ser)) {
			anken_no_ser = GS.SINGLE_QUOTATION + GS.SINGLE_QUOTATION;
		}

		// ActionForm に明細を格納
		form.setAnken_no_ser(anken_no_ser);
	}

	/**
	 * 進捗処理 <br>
	 *
	 * @throws Exception
	 */
	public void sinchoku() throws Exception {

		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TokusokumailDbAcc dbacc = new TokusokumailDbAcc(sqlExec, log,
				appContext);

		// 選択された組織情報
		Map<String, String> sosiki = form.getAr_sosiki().get(
				form.getId_sosiki());
		form.setSrh_anken_no(GS.SINGLE_QUOTATION + sosiki.get(ANKEN_NO)
				+ GS.SINGLE_QUOTATION);

		// 取引先の滞留判定進捗一覧の表示
		dbacc.getSinchokuList(form.getSrh_anken_no());
	}

	/**
	 * 27_督促メール配信の登録・送信
	 *
	 * @throws Exception
	 */
	public String doInsT04() throws Exception {
		InputCheck check = new InputCheck();
		String upd_user = user_bean.getComUserId();
		String tanto = null;

		// 入力チェック
		if ("10".equals(form.getSoushinbu())) {
			if(check.isNullBlank(form.getRadio_id())){
				// 直近の担当者、査定/滞留タブで未選択の場合エラー
				List<String> list = new ArrayList<String>();
				list.add(GL.ERR_SELECT);
				list.add(GL.OS6105_TANTO);
				appContext.setMsgCode(list);
				return GS.OS6105;
			}

			// ラジオボタンで選択された進捗情報を取得
			Map<String,String> shincyoku = form.getAr_sinchoku().get(Integer.parseInt(form.getRadio_id()));

			if (check.isNullBlank(shincyoku.get("daiko_user_id"))) {
				form.setTanto_id(shincyoku.get("tanto_user"));
				tanto = shincyoku.get("tanto_user");
			} else {
				form.setTanto_id(shincyoku.get("daiko_user_id"));
				tanto = shincyoku.get("daiko_user_id");
			}

			//権限チェック（担当したフェーズにより処理・参照権限チェックを実施）
			if(!chkUserKengen(tanto)){
				// 担当者に処理・参照権限がない場合、エラー
				return GS.OS6105;
			}

		} else {
			// 前回の担当者以外、未選択の場合エラー
			if (check.isNullBlank(form.getTxtTanto())) {
				List<String> list = new ArrayList<String>();
				list.add(GL.ERR_SELECT);
				list.add(GL.OS6105_TANTO);
				appContext.setMsgCode(list);
				return GS.OS6105;
			}

			tanto = form.getSelectedTantoId();
			if(!chkUserKengen(tanto)){
				// 担当者に処理・参照権限がない場合、エラー
				return GS.OS6105;
			}
		}


		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TokusokumailDbAcc dbacc = new TokusokumailDbAcc(sqlExec, log,
				appContext);

		dbacc.getTanto_Syozoku(tanto);


		if (!check.isNullBlank(user_bean.getComDaiko_userId())) {
			upd_user = user_bean.getComDaiko_userId();
		}

		// メール配信登録
		dbacc.insT27(tanto, upd_user);

		// メール送信フラグが1の場合、メール配信処理の実行
		if ("1".equals(form.getMailSendFlg())) {
			dbacc.sendTokusokuMail(tanto);
		}
		//コミット
		dbacc.commit();

		return GS.OS6104;
	}


	/**
	 * ユーザ権限チェック
	 *
	 * @param tanto 担当ユーザID
	 * @return チェック結果
	 * @throws Exception
	 */

	public boolean chkUserKengen(String tanto) throws Exception {
    	String errMsg = GL.ERR_NOTSET;
    	boolean result = true;

    	// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		TokusokumailDbAcc dbacc = new TokusokumailDbAcc(sqlExec, log, appContext);

		// ユーザの処理権限チェック
		if(!dbacc.isUserKengen(tanto)){
			errMsg = GL.ERR_SELECT3;
			result = false;
		}

		// メッセージの作成
		if(!result){
			List<String> msgList = new ArrayList<String>();
			msgList.add(errMsg);
			appContext.setMsgCode(msgList);
		}

		return result;
	}

}