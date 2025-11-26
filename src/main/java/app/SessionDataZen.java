/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001     2008/10/29      中野            結合テスト障害No003 対象先選定_選定先詳細の仕様変更対応
002     09/11/13		SSC				課題No.09 保有文書添付仕様変更
003		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app;


import common.util.JCalendar;

import java.util.ArrayList;

/**
 * アプリケーション共通のセッションデータクラス
 * 
 */
public class SessionDataZen {
    // ユーザパラメータ
	// 障害票No:294
	// チェックイン日:2008/05/15
	// 対応者:細野
	// 対応概要:代行設定時代行者の権限を共通セッションに再設定
	private String comInitUserId				= null;	// 初回ユーザID
	// ここまで
	private String comUserId					= null;	// ユーザID
	private String comTanto_User_Nm				= null;	// ユーザ名日本語
	private String comTanto_User_Nm_En			= null;	// ユーザ名英語
	
	private String comEigyoRegistKg 			= null;	// 営業登録権限
	private String comEigyoRecogKg				= null;	// 営業承認権限
	private String comEigyoKaiRegistKg			= null;	// 営業会計登録権限
	private String comEigyoKaiRecogKg			= null;	// 営業会計承認権限
	private String comNijiStKbn					= null;	// ２次査定区分
	private String comNijiStRegistKg			= null;	// ２次査定登録権限
	private String comNijiStRecogKg				= null;	// ２次査定承認権限
	private String comUniqueKg					= null;	// 特殊権限
	private String comSoshikiCd					= null;	// 所属組織コード
	private String comSoshiki_Nm				= null;	// 所属組織名日本語
	private String comSoshiki_Nm_En				= null;	// 所属組織名英語
	private String comKaishaCd					= null;	// 会社コード
	private String comSateiKaishaCd				= null;	// 査定会社コード
	private ArrayList comTantoCd					= null;	// 担当部門コード
	private String comBumonCd					= null;	// 部門コード
	private String comBuCd						= null;	// 部コード
	private String comKaCd						= null;	// 課コード
	private String comEmailAddr					= null;	// メールアドレス
    private String c_phase;			// 帳票用フェーズ
	
	//　画面処理系パラメータ
	// 障害票No388 2008/05/17 細野 追加対象先選定対応
	private String orgreturnId						= null; // 前遷移元画面ID
	// ここまで
	private String returnId						= null; // 遷移元画面ID
	private String phase						= null; // フェーズ
	private String status						= null; // ステータス
	private String kanjo_cd						= null; // 勘定先コード
	private String kanjo_nm						= null; // 勘定先名称
	//課題No.09
	//追加開始
	private String satei_ki					= null; // 査定期
	//追加完了
	private String ym							= null; // 基準年月
	private String satei_anken_no				= null; // 査定案件No
	private String duns_no						= null; // 統合取引先コード(DUNS NO.)
	private String system_kbn					= null; // システム区分
	private String mise_cd 						= null; // 店コード
	private String selectSateiKaishaCd			= null; // 選択査定会社コード
	private String country_nm					= null; // 国名
	private String jiyuu_nm						= null; // 抽出事由名称
	private String kbn							= null; // 区分
	private String soshiki_nm					= null; // 組織名称
	private String lastAnken_no				= null; // 前回実行案件No
	private String satei_gamen					= null; // 査定登録画面
	// add tuuka_cd 2008/4/15 nakajima
	private String tuuka						= null; // 通貨コード
	private String tairyu_anken_no				= null; // 滞留案件No
	private String ankenKtk					= null; //格付
	private String bunsyo_no					= null; // 文書No
	private String satei_bumon_cd 				= null; // 査定部門コード
	private String satei_bu_cd 				= null; // 査定部コード
	private String anken_no_eda				= null; // 案件No枝番
	///////////////////////////////////////
	//障害票：381
	//チェックイン日：2008/5/16
	//対応者：SJA中島
	//概要：対象先選定確認から、対象先選定に差し戻しができない問題対応
	////////////////////////////////////////
	private String sentei_syosai_id			= null; // 選定詳細画面ID

	// 障害票：413 2008/5/20 細野 基準日表示対応
	private String syori_cnt					=null;	// 処理回数
	
	//結果詳細用
	private String anken_system_kbn			= null; // 基幹システム区分
	private String anken_ka_cd					= null; // 課コード
	private String anken_satei_kaisya_cd		= null; // 査定会社コード
	private String anken_mise_cd				= null; // 店コード
	private String anken_kikan_tori_cd			= null; // 基幹取引先コード
	
	
	// 固定パラメータ
	private String daikoUserId					= null;	// 代行ユーザID
	private String comExecMode 					= null;	// 起動モード
	private String comLangMode					= null;	// 言語モード
	private JCalendar comSystemDate				= null;	// システム日付

	//結合テスト障害No003対応
	//追加開始
	private String hanki_sihanki_kbn			= null;	// 半期四半期区分
	//追加完了

	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		// 障害票No:294
		// チェックイン日:2008/05/15
		// 対応者:細野
		// 対応概要:代行設定時代行者の権限を共通セッションに再設定
		comInitUserId 				= "";	// 初回ユーザID
		// ここまで
		comUserId 					= "";	// ユーザID
		comTanto_User_Nm			= "";	// ユーザ名日本語
		comTanto_User_Nm_En			= "";	// ユーザ名英語

		comEigyoRegistKg			= "";	// 営業登録権限
		comEigyoRecogKg				= "";	// 営業承認権限
		comEigyoKaiRegistKg			= "";	// 営業会計登録権限
		comEigyoKaiRecogKg			= "";	// 営業会計承認権限
		comNijiStKbn				= "";	// ２次査定区分
		comNijiStRegistKg			= "";	// ２次査定登録権限
		comNijiStRecogKg			= "";	// ２次査定承認権限
		comUniqueKg					= "";	// 特殊権限
		comSoshikiCd				= "";	// 所属組織コード
		comSoshiki_Nm				= "";	// 所属組織名日本語
		comSoshiki_Nm_En			= "";	// 所属組織名英語
		comKaishaCd					= "";	// 会社コード
		comSateiKaishaCd			= "";	// 査定会社コード
		comTantoCd					= new ArrayList();	// 担当部門コード
		comBumonCd					= "";	// 部門コード
		comBuCd						= "";	// 部コード
		comKaCd						= "";	// 課コード
		comEmailAddr				= "";	// メールアドレス

		orgreturnId					= "";	// 前遷移元画面ID
		returnId					= "";	// 遷移元画面ID
		phase						= ""; 	// フェーズ
		status						= ""; 	// ステータス
		kanjo_cd					= ""; 	// 勘定先コード
		kanjo_nm					= ""; 	// 勘定先名称
		ym							= ""; 	// 基準年月
		satei_anken_no				= ""; 	// 査定案件No
		duns_no						= ""; 	// 統合取引先コード(DUNS NO.)
		system_kbn					= ""; 	// システム区分
		mise_cd						= ""; 	// 店コード
		selectSateiKaishaCd			= "";	// 選択査定会社コード
		country_nm					= "";	// 国名
		jiyuu_nm					= "";	// 抽出事由名称
		kbn							= "";	// 区分
		soshiki_nm					= "";	// 組織名称
		lastAnken_no				= "";   // 前回実行案件No
		satei_gamen					= "";	// 査定登録画面
		daikoUserId 				= "";
		comExecMode 				= ""; 	// 起動モード
		comLangMode					= "";	// 言語モード
		// 障害票：413 2008/5/20 細野 基準日表示対応
		syori_cnt					= "";	// 処理回数
		comSystemDate				= null;	// システム日付
		tuuka						= null;
		tairyu_anken_no				= null;
		ankenKtk					= null;
		bunsyo_no					= null;
		satei_bumon_cd				= null;
		satei_bu_cd					= null;
		anken_no_eda				= null;
		sentei_syosai_id			= "";
		//結合テスト障害No003対応
		//追加開始
		hanki_sihanki_kbn			= "";	// 半期四半期区分
		//追加完了
		//課題No.09
		//追加開始
		satei_ki					= "";
		//追加完了
		}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}
	
	//課題No.09
	//追加開始
	public String getSatei_ki() {
		return satei_ki;
	}
	public void setSatei_ki(String satei_ki) {
		this.satei_ki = satei_ki;
	}
	//追加完了
	public String getSentei_syosai_id() {
		return sentei_syosai_id;
	}
	public void setSentei_syosai_id(String sentei_syosai_id) {
		this.sentei_syosai_id = sentei_syosai_id;
	}
	public String getAnken_ka_cd() {
		return anken_ka_cd;
	}
	public void setAnken_ka_cd(String anken_ka_cd) {
		this.anken_ka_cd = anken_ka_cd;
	}
	public String getAnken_kikan_tori_cd() {
		return anken_kikan_tori_cd;
	}
	public void setAnken_kikan_tori_cd(String anken_kikan_tori_cd) {
		this.anken_kikan_tori_cd = anken_kikan_tori_cd;
	}
	public String getAnken_mise_cd() {
		return anken_mise_cd;
	}
	public void setAnken_mise_cd(String anken_mise_cd) {
		this.anken_mise_cd = anken_mise_cd;
	}
	public String getAnken_satei_kaisya_cd() {
		return anken_satei_kaisya_cd;
	}
	public void setAnken_satei_kaisya_cd(String anken_satei_kaisya_cd) {
		this.anken_satei_kaisya_cd = anken_satei_kaisya_cd;
	}
	public String getAnken_system_kbn() {
		return anken_system_kbn;
	}
	public void setAnken_system_kbn(String anken_system_kbn) {
		this.anken_system_kbn = anken_system_kbn;
	}
	public String getAnken_no_eda() {
		return anken_no_eda;
	}
	public void setAnken_no_eda(String anken_no_eda) {
		this.anken_no_eda = anken_no_eda;
	}
	public String getSatei_bumon_cd() {
		return satei_bumon_cd;
	}
	public void setSatei_bumon_cd(String satei_bumon_cd) {
		this.satei_bumon_cd = satei_bumon_cd;
	}
	public String getSatei_bu_cd() {
		return satei_bu_cd;
	}
	public void setSatei_bu_cd(String satei_bu_cd) {
		this.satei_bu_cd = satei_bu_cd;
	}
	public String getTairyu_anken_no() {
		return tairyu_anken_no;
	}
	public void setTairyu_anken_no(String tairyu_anken_no) {
		this.tairyu_anken_no = tairyu_anken_no;
	}
	
	public String getAnkenKtk() {
		return ankenKtk;
	}
	public void setAnkenKtk(String ankenKtk) {
		this.ankenKtk = ankenKtk;
	}
	
	public String getBunsyo_no() {
		return bunsyo_no;
	}
	public void setBunsyo_no(String bunsyo_no) {
		this.bunsyo_no = bunsyo_no;
	}
	/**
	 * @return tuuka を戻します。
	 */
	public String getTuuka() {
		return tuuka;
	}
	/**
	 * @param tuuka tuuka を設定。
	 */
	public void setTuuka(String tuuka) {
		this.tuuka = tuuka;
	}
	
	// 障害票No:294
	// チェックイン日:2008/05/15
	// 対応者:細野
	// 対応概要:代行設定時代行者の権限を共通セッションに再設定
	/**
	 * @return 初回ユーザID を戻します。
	 */
	public String getComInitUserId() {
		return comInitUserId;
	}
	/**
	 * @param 初回ユーザID を設定。
	 */
	public void setComInitUserId(String comInitUserId) {
		this.comInitUserId = comInitUserId;
	}
	// ここまで
	
	/**
	 * @return ユーザID を戻します。
	 */
	public String getComUserId() {
		return comUserId;
	}
	/**
	 * @param ユーザID を設定。
	 */
	public void setComUserId(String comUserId) {
		this.comUserId = comUserId;
	}

	/**
	 * @return 代行ユーザID を戻します。
	 */
	public String getDaikoUserId() {
		return daikoUserId;
	}
	/**
	 * @param 代行ユーザID を設定。
	 */
	public void setDaikoUserId(String daikoUserId) {
		this.daikoUserId = daikoUserId;
	}
	/**
	 * @return 所属組織コード を戻します。
	 */
	public String getComSoshikiCd() {
		return comSoshikiCd;
	}
	/**
	 * @param comSoshikiCd　所属組織コード を設定。
	 */
	public void setComSoshikiCd(String comSoshikiCd) {
		this.comSoshikiCd = comSoshikiCd;
	}
	/**
	 * @return 言語モード を戻します。
	 */
	public String getComLangMode() {
		return comLangMode;
	}
	/**
	 * @param comLangMode 言語モード を設定。
	 */
	public void setComLangMode(String comLangMode) {
		this.comLangMode = comLangMode;
	}

	// 障害票：413 2008/5/20 細野 基準日表示対応
	/**
	 * @return 処理回数 を戻します。
	 */
	public String getSyoriCnt() {
		return syori_cnt;
	}
	/**
	 * @param SyoriCnt 言語処理回数 を設定。
	 */
	public void setSyoriCnt(String syori_cnt) {
		this.syori_cnt = syori_cnt;
	}

	/**
	 * @return システム日付 を戻します。
	 */
	public JCalendar getComSystemDate() {
		return comSystemDate;
	}
	/**
	 * @param なし システム日付 を設定。
	 */
	public void setComSystemDate(JCalendar jc) {
	    this.comSystemDate = jc;
	}
		
	/**
	 * @return 営業登録権限 を戻します。
	 */
	public String getComEigyoRegistKg() {
		return comEigyoRegistKg;
	}
	/**
	 * @param comEigyoRegistKg 営業登録権限 を設定。
	 */
	public void setComEigyoRegistKg(String comEigyoRegistKg) {
		this.comEigyoRegistKg = comEigyoRegistKg;
	}	
	/**
	 * @return 営業承認権限 を戻します。
	 */
	public String getComEigyoRecogKg() {
		return comEigyoRecogKg;
	}
	/**
	 * @param comEigyoRecogKg 営業承認権限 を設定。
	 */
	public void setComEigyoRecogKg(String comEigyoRecogKg) {
		this.comEigyoRecogKg = comEigyoRecogKg;
	}	
	/**
	 * @return 営業会計登録権限 を戻します。
	 */
	public String getComEigyoKaiRegistKg() {
		return comEigyoKaiRegistKg;
	}
	/**
	 * @param comEigyoKaiRegistKg 営業会計登録権限 を設定。
	 */
	public void setComEigyoKaiRegistKg(String comEigyoKaiRegistKg) {
		this.comEigyoKaiRegistKg = comEigyoKaiRegistKg;
	}	
	/**
	 * @return 営業会計承認権限 を戻します。
	 */
	public String getComEigyoKaiRecogKg() {
		return comEigyoKaiRecogKg;
	}
	/**
	 * @param comEigyoRegistKg 営業会計承認権限 を設定。
	 */
	public void setComEigyoKaiRecogKg(String comEigyoKaiRecogKg) {
		this.comEigyoKaiRecogKg = comEigyoKaiRecogKg;
	}	
	/**
	 * @return ２次査定区分 を戻します。
	 */
	public String getComNijiStKbn() {
		return comNijiStKbn;
	}
	/**
	 * @param comNijiStKbn ２次査定区分 を設定。
	 */
	public void setComNijiStKbn(String comNijiStKbn) {
		this.comNijiStKbn = comNijiStKbn;
	}	
	/**
	 * @return ２次査定登録権限 を戻します。
	 */
	public String getComNijiStRegistKg() {
		return comNijiStRegistKg;
	}
	/**
	 * @param comNijiStRegistKg ２次査定登録権限 を設定。
	 */
	public void setComNijiStRegistKg(String comNijiStRegistKg) {
		this.comNijiStRegistKg = comNijiStRegistKg;
	}	
	/**
	 * @return ２次査定承認権限 を戻します。
	 */
	public String getComNijiStRecogKg() {
		return comNijiStRecogKg;
	}
	/**
	 * @param comNijiStRecogKg ２次査定承認権限 を設定。
	 */
	public void setComNijiStRecogKg(String comNijiStRecogKg) {
		this.comNijiStRecogKg = comNijiStRecogKg;
	}
	/**
	 * @return 特殊権限 を戻します。
	 */
	public String getComUniqueKg() {
		return comUniqueKg;
	}
	/**
	 * @param comUniqueKg 特殊権限 を設定。
	 */
	public void setComUniqueKg(String comUniqueKg) {
		this.comUniqueKg = comUniqueKg;
	}
	
	/**
	 * @return 会社コード を戻します。
	 */
	public String getComKaishaCd() {
		return comKaishaCd;
	}
	/**
	 * @param comKaishaCd 会社コード を設定。
	 */
	public void setComKaishaCd(String comKaishaCd) {
		this.comKaishaCd = comKaishaCd;
	}
	
	/**
	 * @return 査定会社コード を戻します。
	 */
	public String getComSateiKaishaCd() {
		return comSateiKaishaCd;
	}
	/**
	 * @param comSateiKaishaCd 査定会社コード を設定。
	 */
	public void setComSateiKaishaCd(String comSateiKaishaCd) {
		this.comSateiKaishaCd = comSateiKaishaCd;
	}
	
	// 障害票No　2008/05/17　細野　担当部門の初期化
	/**
	 * @ 担当部門コード を初期化します。
	 */
	public void InitComTantoCd() {
		comTantoCd					= new ArrayList();	// 担当部門コード
	}
	
	/**
	 * @return 担当部門コード を戻します。
	 */
	public ArrayList getComTantoCd() {
		return comTantoCd;
	}
	/**
	 * @param comTantoCd 担当部門コード を設定。
	 */
	public void setComTantoCd(String comTantoCdString) {
		this.comTantoCd.add(comTantoCdString);
	}
	
	/**
	 * @return 部門コード を戻します。
	 */
	public String getComBumonCd() {
		return comBumonCd;
	}
	/**
	 * @param comBumonCd 部門コード を設定。
	 */
	public void setComBumonCd(String comBumonCd) {
		this.comBumonCd = comBumonCd;
	}
	
	/**
	 * @return 部コード を戻します。
	 */
	public String getComBuCd() {
		return comBuCd;
	}
	/**
	 * @param comBuCd 部コード を設定。
	 */
	public void setComBuCd(String comBuCd) {
		this.comBuCd = comBuCd;
	}
	
	/**
	 * @return 課コード を戻します。
	 */
	public String getComKaCd() {
		return comKaCd;
	}
	/**
	 * @param comKaCd 課コード を設定。
	 */
	public void setComKaCd(String comKaCd) {
		this.comKaCd = comKaCd;
	}
	
	/**
	 * @return メールアドレス を戻します。
	 */
	public String getComEmailAddr() {
		return comEmailAddr;
	}
	/**
	 * @param comKaCd メールアドレス を設定。
	 */
	public void setComEmailAddr(String comEmailAddr) {
		this.comEmailAddr = comEmailAddr;
	}
		
	/**
	 * @return 担当者名日本語 を戻します。
	 */
	public String getComTanto_User_Nm() {
		return comTanto_User_Nm;
	}
	/**
	 * @param comTanto_User_Nm 担当者名日本語 を設定。
	 */
	public void setComTanto_User_Nm(String comTanto_User_Nm) {
		this.comTanto_User_Nm = comTanto_User_Nm;
	}
	
	/**
	 * @return 担当者名英語 を戻します。
	 */
	public String getComTanto_User_Nm_En() {
		return comTanto_User_Nm_En;
	}
	/**
	 * @param comTanto_User_Nm_En 担当者名英語 を設定。
	 */
	public void setComTanto_User_Nm_En(String comTanto_User_Nm_En) {
		this.comTanto_User_Nm_En = comTanto_User_Nm_En;
	}
	
	/**
	 * @return 所属部署名日本語 を戻します。
	 */
	public String getComSoshiki_Nm() {
		return comSoshiki_Nm;
	}
	/**
	 * @param comSoshiki_Nm 所属部署名日本語 を設定。
	 */
	public void setComSoshiki_Nm(String comSoshiki_Nm) {
		this.comSoshiki_Nm = comSoshiki_Nm;
	}
	
	/**
	 * @return 所属部署名英語 を戻します。
	 */
	public String getComSoshiki_Nm_En() {
		return comSoshiki_Nm_En;
	}
	/**
	 * @param comSoshiki_Nm_En 所属部署名英語 を設定。
	 */
	public void setComSoshiki_Nm_En(String comSoshiki_Nm_En) {
		this.comSoshiki_Nm_En = comSoshiki_Nm_En;
	}
	/**
	 * @return returnId 遷移元画面IDを戻します。
	 */
	public String getReturnId() {
		return returnId;
	}
	/**
	 * @param returnId 遷移元画面ID
	 */
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}
	/**
	 * @return orgreturnId 前遷移元画面IDを戻します。
	 */
	public String getOrgReturnId() {
		return orgreturnId;
	}
	/**
	 * @param orgreturnId 前遷移元画面ID
	 */
	public void setOrgReturnId(String orgreturnId) {
		this.orgreturnId = orgreturnId;
	}

	/**
	 * @return 勘定先コード を戻します。
	 */
	public String getKanjo_cd() {
		return kanjo_cd;
	}
	/**
	 * @param kanjo_cd 勘定先コード を設定。
	 */
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}
	/**
	 * @return 勘定先名称 を戻します。
	 */
	public String getKanjo_nm() {
		return kanjo_nm;
	}
	/**
	 * @param kanjo_nm 勘定先名称 を設定。
	 */
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}
	/**
	 * @return フェーズ を戻します。
	 */
	public String getPhase() {
		return phase;
	}
	/**
	 * @param phase フェーズ を設定。
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}
	/**
	 * @return 基準年月 を戻します。
	 */
	public String getYm() {
		return ym;
	}
	/**
	 * @param ym 基準年月 を設定。
	 */
	public void setYm(String ym) {
		this.ym = ym;
	}
	/**
	 * @return 起動モード を戻します。
	 */
	public String getComExecMode() {
		return comExecMode;
	}
	/**
	 * @param comExecMode 起動モード を設定。
	 */
	public void setComExecMode(String comExecMode) {
		this.comExecMode = comExecMode;
	}
	/**
	 * @return satei_anken_no を戻します。
	 */
	public String getSatei_anken_no() {
		return satei_anken_no;
	}
	/**
	 * @param satei_anken_no satei_anken_no を設定。
	 */
	public void setSatei_anken_no(String satei_anken_no) {
		this.satei_anken_no = satei_anken_no;
	}
	/**
	 * @return mise_cd を戻します。
	 */
	public String getMise_cd() {
		return mise_cd;
	}
	/**
	 * @param mise_cd mise_cd を設定。
	 */
	public void setMise_cd(String mise_cd) {
		this.mise_cd = mise_cd;
	}
	/**
	 * @return system_kbn を戻します。
	 */
	public String getSystem_kbn() {
		return system_kbn;
	}
	/**
	 * @param system_kbn system_kbn を設定。
	 */
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
	/**
	 * @return duns_no を戻します。
	 */
	public String getDuns_no() {
		return duns_no;
	}
	/**
	 * @param duns_no duns_no を設定。
	 */
	public void setDuns_no(String duns_no) {
		this.duns_no = duns_no;
	}
	
	public String getSelectSateiKaishaCd() {
		return selectSateiKaishaCd;
	}
	public void setSelectSateiKaishaCd(String selectSateiKaishaCd) {
		this.selectSateiKaishaCd = selectSateiKaishaCd;
	}
	/**
	 * @return 国名 を戻します。
	 */
	public String getCountry_nm() {
		return country_nm;
	}
	/**
	 * @param country_nm 国名 を設定。
	 */
	public void setCountry_nm(String country_nm) {
		this.country_nm = country_nm;
	}
	
	/**
	 * @return 抽出事由名称 を戻します。
	 */
	public String getJiyuu_nm() {
		return jiyuu_nm;
	}
	/**
	 * @param jiyuu_nm 抽出事由名称 を設定。
	 */
	public void setJiyuu_nm(String jiyuu_nm) {
		this.jiyuu_nm = jiyuu_nm;
	}
	
	/**
	 * @return 区分 を戻します。
	 */
	public String getKbn() {
		return kbn;
	}
	/**
	 * @param kbn 区分 を設定。
	 */
	public void setKbn(String kbn) {
		this.kbn = kbn;
	}
	/**
	 * @return 組織名称 を戻します。
	 */
	public String getSoshiki_nm() {
		return soshiki_nm;
	}
	/**
	 * @param kbn 組織名称 を設定。
	 */
	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}
	
	
	/**
	 * @return 前回実行案件No を戻します。
	 */
	public String getLastAnken_no() {
		return lastAnken_no;
	}
	/**
	 * @param lastAnken_no 前回実行案件No を設定。
	 */
	public void setLastAnken_no(String lastAnken_no) {
		this.lastAnken_no = lastAnken_no;
	}
	
	/**
	 * @return 査定登録画面 を戻します。
	 */
	public String getSatei_gamen() {
		return satei_gamen;
	}
	/**
	 * @param satei_gamen 査定登録画面 を設定。
	 */
	public void setSatei_gamen(String satei_gamen) {
		this.satei_gamen = satei_gamen;
	}
	//結合テスト障害No003対応
	//追加開始
	/**
	 * @param 半期四半期区分 を設定。
	 */
	public void setHanki_sihanki_kbn(String hanki_sihanki_kbn) {
		this.hanki_sihanki_kbn = hanki_sihanki_kbn;
	}
	
	/**
	 * @return 半期四半期区分 を戻します。
	 */
	public String getHanki_sihanki_kbn() {
		return hanki_sihanki_kbn;
	}
	//追加完了
	public String getC_phase() {
		return c_phase;
	}
	public void setC_phase(String c_phase) {
		this.c_phase = c_phase;
	}
}