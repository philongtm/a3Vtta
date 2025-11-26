/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0.67
更新履歴
No		日付			修正者			修正内容
001		2016/03/29		SSC				新規作成 
******************************************************************************/
package app.system.bss;

import app.SessionData;
import app.UserBean;
import app.UserMaintenanceBean;
import app.system.dbAcc.TantoSoshikiSentakuDbAcc;
import app.system.form.TantoSoshikiSentakuForm;
import app.system.form.TantouBean;
import common.AppContext;
import common.db.SqlExecuter;
import common.global.GL;
import common.global.GS;
import common.util.Function;
import common.util.InputCheck;
import common.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * OS7116_担当組織選択 ビジネス ロジッククラス <br>
 * 
 */
public class TantoSoshikiSentakuBss {

	private AppContext appContext = null;							// APPコンテキスト
	private SqlExecuter sqlExec = null;								// DBアクセス
	private Log log = null;											// LOG
	private TantoSoshikiSentakuDbAcc dbacc = null;					// DBアクセスクラス
	private TantoSoshikiSentakuForm form = null;					// アクションフォーム 
	private UserBean user_bean = null;								// ユーザ情報

	private SessionData cmnData;									// 機能共通セッション
	private UserMaintenanceBean user_maintenance_bean;				// ユーザメンテナンス情報Bean

	private static final String SYSTEM_KBN			= "SYSTEM_KBN";		// システム区分
	private static final String HANYOU1				= "HANYOU1";		// 汎用1
	private static final String HANYOU1_NM			= "HANYOU1_NM";		// 汎用1名称
	private static final String HANYOU2  			= "HANYOU2";		// 汎用2
	private static final String HANYOU2_NM  		= "HANYOU2_NM";		// 汎用2名称
	private static final String HANYOU3				= "HANYOU3";		// 汎用3
	private static final String HANYOU3_NM			= "HANYOU3_NM";		// 汎用3名称
	private static final String ID					= "ID";				// ID
	private static final String SENTAKU_FLG			= "SENTAKU_FLG";	// 選択フラグ

	/**
	 * コンストラクタ
	 */
	public TantoSoshikiSentakuBss(AppContext appContext) {
		this.appContext = appContext;
		this.log = appContext.getLog();

		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		user_maintenance_bean = cmnData.getUser_maintenance_bean();
		form = (TantoSoshikiSentakuForm)appContext.getActionForm();	
	}

	/**
	 * 画面初期表示値取得(メニューリンクから遷移時)
	 */
	public String executeInit() throws Exception {
		
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new TantoSoshikiSentakuDbAcc(sqlExec, log, appContext);

		// セレクトボックス初期値
		form.setSystemKbn(user_bean.getComWorkflowSystemkbn());
		form.setHanyou1(user_bean.getComWorkflowSateikaisya_cd());
		form.setHanyou2("");

		// 汎用1セレクトボックスの設定値を取得する
		dbacc.getHanyou1();

		// 汎用2セレクトボックスの設定値を取得する
		dbacc.getHanyou2();

		// 一覧を取得する（設定中のワークフローで検索）
		dbacc.getMeisai();
		// 対象ユーザの権限
		dbacc.getTaiUserSoshikiList();
		// ログインユーザの統轄分類1
		dbacc.getLogUserBunrui1List();
		// 一覧情報の作成
		this.setTaiUserKengen();
		
		// 検索条件を共通セッションに格納
		user_maintenance_bean.setKen_System_Kbn(user_bean.getComWorkflowSystemkbn());
		user_maintenance_bean.setKen_Hanyou1(user_bean.getComWorkflowSateikaisya_cd());
		user_maintenance_bean.setKen_Hanyou2("");

		//現在の業務フローから、汎用3のラベル表示/非表示を判断する
		if (GS.GSS.equals(user_bean.getComWorkflowSystemkbn())
				&& GS.SATEIKAISYA_SJ.equals(user_bean.getComWorkflowSateikaisya_cd())) {
			// 双日本社の場合：表示
			form.setHanyou3LabelFlg(GS.ON);
		}else{
			// 双日本社以外の場合：非表示
			form.setHanyou3LabelFlg(GS.OFF);
		}

		return GS.OS7116;
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
		dbacc = new TantoSoshikiSentakuDbAcc(sqlExec, log, appContext);
		form.setSystemKbn(null);
		form.setHanyou2(null);
		// 汎用2セレクトボックスの設定値を取得する
		dbacc.getHanyou2();
		return GS.OS7116;
	}
	
	/**
	 * 検索アクション <br>
	 *
	 * @return
	 * @throws Exception
	 */
	public String doSearch() throws Exception {
		// 検索したときの条件を設定する
		// コネクションの取得
		this.sqlExec = appContext.getSqlExecuter();
		dbacc = new TantoSoshikiSentakuDbAcc(sqlExec, log, appContext);
		
		// 検索条件の退避
		user_maintenance_bean.setKen_System_Kbn(form.getSystemKbn());
		user_maintenance_bean.setKen_Hanyou1(form.getHanyou1());
		user_maintenance_bean.setKen_Hanyou2(form.getHanyou2());
		
		// 一覧取得
		dbacc.getMeisai();
		// 一覧表示データの作成
		this.setTaiUserKengen();

		return GS.OS7116;
	}

	/**
	 * 検索結果に対象ユーザの設定済み権限のチェックを入れる
	 */
	public void setTaiUserKengen(){
		InputCheck inChk = new InputCheck();

		// 検索結果一覧（DBより取得）
		List<HashMap<String, String>> kensakuList = new ArrayList<HashMap<String, String>>();
		kensakuList = (List<HashMap<String, String>>)form.getAr_meisai();
		HashMap<String, String> kensakuHm;

		// 対象ユーザ権限一覧
		List<HashMap<String, String>> taiUserTantoList = new ArrayList<HashMap<String, String>>();
		taiUserTantoList = form.getAr_taiUserTantoList();
		HashMap<String, String> taiUserTantoHm;

		// ログインユーザの統轄分類1
		ArrayList<String> ar_logUserBunrui1List = new ArrayList<String>(form.getAr_logUserBunrui1List());

		if(kensakuList != null && kensakuList.size() > 0){
			/**
			 * ログインユーザに統轄分類1がある場合、検索結果に統轄チェックボックスを追加
			 */
			if(ar_logUserBunrui1List != null && ar_logUserBunrui1List.size() > 0){
				// 編集後の検索結果一覧
				List<HashMap<String, String>> hyoujiList = new ArrayList<HashMap<String, String>>();
				HashMap<String, String> hyoujiHm;

				String maeKekkaHanyou1 = "";	// ひとつ前の汎用1
				String kensakuHanyo1 = "";
				int kaisya;

				for(int i = 0; kensakuList.size() > i ; i++){
					// 検索結果の件数分繰り返す
					kensakuHm = kensakuList.get(i);
					// ひとつ前の汎用1と異なる、かつログインユーザに統轄分類1がある場合
					if (!maeKekkaHanyou1.equals(kensakuHm.get(HANYOU1))) {
						kensakuHanyo1 = kensakuHm.get(HANYOU1);
						kaisya = ar_logUserBunrui1List.indexOf(kensakuHanyo1);

						if(kaisya != -1 && !maeKekkaHanyou1.equals(kensakuHm.get(HANYOU1))){
							// 統轄で設定ありの場合、会社権限設定追加
							hyoujiHm = new HashMap<String, String>();
							
							hyoujiHm.put(ID,Function.getStringOfInt(hyoujiList.size()));	// ID
							hyoujiHm.put(SYSTEM_KBN ,kensakuHm.get(SYSTEM_KBN));			// 基幹システム区分 
							hyoujiHm.put(HANYOU1,kensakuHm.get(HANYOU1));					// 汎用1コード
							hyoujiHm.put(HANYOU1_NM ,kensakuHm.get(HANYOU1_NM));			// 汎用1名称
							hyoujiHm.put(HANYOU2 ,"");										// 汎用2コード
							hyoujiHm.put(HANYOU2_NM ,"");									// 汎用2名称
							hyoujiHm.put(HANYOU3 ,"");										// 汎用3コード
							hyoujiHm.put(HANYOU3_NM ,"");									// 汎用3名称

							hyoujiList.add(hyoujiList.size(),hyoujiHm);
						}
					}
					kensakuHm.put(ID,Function.getStringOfInt(hyoujiList.size()));	// ID
					hyoujiList.add(kensakuHm);
					maeKekkaHanyou1 = kensakuHm.get(HANYOU1);
				}
				// 検索結果一覧（DBより取得）に設定し直し
				kensakuList = new ArrayList<HashMap<String, String>>(hyoujiList);
			}

			/**
			 * 検索結果と対象ユーザの権限が同じ場合、担当フラグをオンにする
			 */

			for(int n = 0 ; kensakuList.size() > n ; n++){
				// 検索結果分繰り返す
				kensakuHm = kensakuList.get(n);

				for(int m = 0; taiUserTantoList.size() > m ; m++){
					// 対象ユーザの権限分繰り返す
					taiUserTantoHm = taiUserTantoList.get(m);

					if(kensakuHm.get(HANYOU1).equals(taiUserTantoHm.get(HANYOU1))){
						// 会社が同じ
						if(kensakuHm.get(HANYOU2).equals(taiUserTantoHm.get(HANYOU2))){
							// 部門が同じ
							if (GS.SATEIKAISYA_SJ.equals(kensakuHm.get(HANYOU1))
									&& kensakuHm.get(HANYOU3).equals(taiUserTantoHm.get(HANYOU3))) {
								// 査定会社SJかつ本部が同じ場合、担当フラグをオンにする
								kensakuHm.put(SENTAKU_FLG,GS.ON);
								kensakuList.set(n,kensakuHm);	// 検索結果にセットしなおす
								break;

							} else if (GS.SATEIKAISYA_SJ.equals(kensakuHm.get(HANYOU1))
									&& !kensakuHm.get(HANYOU3).equals(taiUserTantoHm.get(HANYOU3))){
								// 査定会社SJかつ本部が異なる場合、次の検索結果へ
								continue;
							} else {
								// 査定会社SJ以外で部門が同じ場合
								kensakuHm.put(SENTAKU_FLG,GS.ON);
								kensakuList.set(n,kensakuHm);	// 検索結果にセットしなおす
								break;
							}
							
						} else if(inChk.isNullBlank(kensakuHm.get(HANYOU2)) && inChk.isNullBlank(taiUserTantoHm.get(HANYOU2))) {
							// 部門が空（会社単位の権限）で同じ場合、担当フラグをオンにする
							kensakuHm.put(SENTAKU_FLG,GS.ON);
							kensakuList.set(n,kensakuHm);	// 検索結果にセットしなおす
							break;
							
						} else {
							// 部門が異なる場合、次の検索結果へ
							continue;
						}
					} else {
						// 会社が異なる場合、次の検索結果へ
						continue;
					}
				}
				if(!GS.ON.equals(kensakuHm.get(SENTAKU_FLG))){
					kensakuHm.put(SENTAKU_FLG,GS.OFF);
					kensakuList.set(n,kensakuHm);	// 検索結果にセットしなおす
				}
			}
			form.setAr_kensakuKekkaList(kensakuList);// 共通セッションへ引き渡し用
			form.setAr_meisai(kensakuList);// 画面表示用
		}
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
		List<HashMap<String, String>> ar_meisai = form.getAr_kensakuKekkaList();
		// 明細一件分を格納
		HashMap<String, String> ar_meisai_hm;
		// 選択された明細
		List<TantouBean> sentaku_ar_meisai = new ArrayList<TantouBean>();
		TantouBean tantouBean;

		// 選択されたチェックボックス（列番号）
		if(!inChk.isNullBlank(form.getTanto_chk())){
			String[] chkBoxList = form.getTanto_chk().split(",", 0);
			// 選択されたチェックボックスの件数（チェックのループ回数）
			int length = chkBoxList.length;
			// 選択されたチェックボックスの番号
			int chkNo = 0;

			String hanyou1 = "";		// 汎用1
			String hanyou2 = "";		// 汎用2
			String maehanyou1 = "";		// 前汎用1
			String maehanyou2 = "";		// 前汎用2
			String maehanyou3 = "";		// 前汎用3

			for (int i =0 ;length > i; i++){
				// 選択されたチェックボックスの番号から、該当する明細を取得する
				chkNo = Integer.parseInt(chkBoxList[i]);
				ar_meisai_hm = ar_meisai.get(chkNo);

				// チェックされていたら汎用を比較する
				hanyou1 = ar_meisai_hm.get(HANYOU1);
				hanyou2 = ar_meisai_hm.get(HANYOU2);

				if (maehanyou1.equals(hanyou1)
						&& inChk.isNullBlank(maehanyou2)
						&& inChk.isNullBlank(maehanyou3)) {
					if(!inChk.isNullBlank(hanyou2)){
						// 前が会社指定の場合、以降で部門・本部の指定をしていたらエラーにし、処理終了
						errMsg = GL.ERR_HANEI;
						break;
					}
				}
				tantouBean = new TantouBean();
				// 共通セッションに格納
				tantouBean.setSystem_kbn(ar_meisai_hm.get(SYSTEM_KBN));		// システム区分
				tantouBean.setHanyou1_cd(hanyou1);							// 汎用1
				tantouBean.setHanyou1_nm(ar_meisai_hm.get(HANYOU1_NM));		// 汎用1名称
				tantouBean.setHanyou2_cd(hanyou2);							// 汎用2
				tantouBean.setHanyou2_nm(ar_meisai_hm.get(HANYOU2_NM));		// 汎用2名称
				tantouBean.setHanyou4_cd(ar_meisai_hm.get(HANYOU3));							// 汎用3
				tantouBean.setHanyou4_nm(ar_meisai_hm.get(HANYOU3_NM));		// 汎用3名称
				tantouBean.setTaisyogaiFlg(GS.OFF);							// 対象外フラグ（処理対象：0）

				sentaku_ar_meisai.add(tantouBean);
				maehanyou1 = hanyou1;					// 前汎用1
				maehanyou2 = hanyou2;					// 前汎用2
				maehanyou3 = ar_meisai_hm.get(HANYOU3);	// 前汎用3

			}

			// エラーメッセージがある場合
			if(GL.ERR_HANEI.equals(errMsg)){
				List<String> msgList = new ArrayList<String>();
				msgList.add(errMsg);
				appContext.setMsgCode(msgList);
				form.setErrChkFlg("1");	// エラーチェックのフラグ
			} else {
				// 上記以外の場合、フォームにセットする
				form.setAr_sentakuHaneiList(sentaku_ar_meisai);
			}
		}

		return GS.OS7116;
	}
}
