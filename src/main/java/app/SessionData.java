/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/02/26		SSC				BJ201602002_部門廃止対応（一次）
******************************************************************************/
package app;

import common.global.GS;

/**
 * アプリケーション共通のセッションデータクラス
 * 
 */
public class SessionData {

	//ユーザ情報
	private UserBean user_bean	= null;	

	//取引先情報
	private TorihikisakiBean tori_bean	= null;	
	
	//明細詳細情報
	private MeisaisyosaiBean syosai_bean = null;	
	
	//査定会社メンテナンス情報
	private SateiKaisyaBean sateikaisya_bean = null;	

	//業務フローパターンメンテナンス情報
	private WorkFlowBean workflow_bean = null;	

	//ユーザメンテナンス情報
	private UserMaintenanceBean user_maintenance_bean = null;

	//抽出条件メンテナンス情報
	private TyusyutuJokenBean joken_bean = null;
	
	//システム情報
	private String sateiSansyoFlg				= null;
	private String comLangMode					= null;	// 言語モード
	private String return_gamenId				= null;	// 遷移元画面ID
	private String syosai_returnId				= null;	// 詳細画面ID
	private String tab_riyou_gamenId			= null;	// 共通タブ利用画面ID
	private String lbl_nm1						= null;	// ラベル名１
	private String lbl_nm2						= null;	// ラベル名２
	private String lbl_nm3						= null;	// ラベル名３
	private String lbl_nm4						= null;	// ラベル名４
	private String lbl_nm5						= null;	// ラベル名５
	private String lbl_nm6						= null;	// ラベル名６
	private String lbl_nm7						= null;	// ラベル名７
	private String lbl_nm8						= null;	// ラベル名８
	private String lbl_nm9						= null;	// ラベル名９
	private String lbl_nm10						= null;	// ラベル名１０
	

	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {

		//ユーザ情報
		user_bean = null;
		
		//取引先情報
		tori_bean = null;

		//明細詳細情報
		syosai_bean = null;

		//査定会社メンテナンス情報
		sateikaisya_bean = null;	

		//業務フローパターンメンテナンス情報
		workflow_bean = null;	

		//ユーザメンテナンス情報
		user_maintenance_bean = null;	

		//抽出条件メンテナンス情報
		joken_bean = null;
		
		//システム情報
		comLangMode			= GS.EMPTY_CHARCTER;	// 言語モード
		return_gamenId		= GS.EMPTY_CHARCTER;	// 遷移元画面ID
		syosai_returnId		= GS.EMPTY_CHARCTER;	// 詳細画面ID
		tab_riyou_gamenId	= GS.EMPTY_CHARCTER;	// 共通タブ利用画面ID
		lbl_nm1				= GS.EMPTY_CHARCTER;	// ラベル名１
		lbl_nm2				= GS.EMPTY_CHARCTER;	// ラベル名２
		lbl_nm3				= GS.EMPTY_CHARCTER;	// ラベル名３
		lbl_nm4				= GS.EMPTY_CHARCTER;	// ラベル名４
		lbl_nm5				= GS.EMPTY_CHARCTER;	// ラベル名５
		lbl_nm6				= GS.EMPTY_CHARCTER;	// ラベル名６
		lbl_nm7				= GS.EMPTY_CHARCTER;	// ラベル名７
		lbl_nm8				= GS.EMPTY_CHARCTER;	// ラベル名８
		lbl_nm9				= GS.EMPTY_CHARCTER;	// ラベル名９
		lbl_nm10			= GS.EMPTY_CHARCTER;	// ラベル名１０
	}
	
	/**
	 * ユーザ情報の初期化処理を行う。
	 */
	public void init_user_bean() {
		user_bean = null;
	}
	
	/**
	 * 取引先情報の初期化処理を行う。
	 */
	public void init_tori_bean() {
		tori_bean = null;
	}

	/**
	 * 明細詳細情報の初期化処理を行う。
	 */
	public void init_syosai_bean() {
		syosai_bean = null;
	}

	/**
	 * 査定会社メンテナンス情報の初期化処理を行う。
	 */
	public void init_sateikaisya_bean() {
		sateikaisya_bean = null;
	}

	/**
	 * 業務フローパターンメンテナンス情報の初期化処理を行う。
	 */
	public void init_workflow_bean() {
		workflow_bean = null;
	}

	/**
	 * ユーザメンテナンス情報の初期化処理を行う。
	 */
	public void init_UserMaintenance_bean() {
		user_maintenance_bean = null;
	}

	/**
	 * 抽出条件メンテナンス情報の初期化処理を行う。
	 */
	public void init_joken_bean() {
		joken_bean = null;
	}

	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	

	//ユーザ情報
	public UserBean getUser_bean() {
		return user_bean;
	}
	public void setUser_bean(UserBean user_bean) {
		this.user_bean = user_bean;
	}
	//取引先情報
	public TorihikisakiBean getTori_bean() {
		return tori_bean;
	}
	public void setTori_bean(TorihikisakiBean tori_bean) {
		this.tori_bean = tori_bean;
	}
	//明細詳細情報
	public MeisaisyosaiBean getSyosai_bean() {
		return syosai_bean;
	}
	public void setSyosai_bean(MeisaisyosaiBean syosai_bean) {
		this.syosai_bean = syosai_bean;
	}
	//査定会社メンテナンス情報
	public SateiKaisyaBean getSateikaisya_bean() {
		return sateikaisya_bean;
	}
	public void setSateikaisya_bean(SateiKaisyaBean sateikaisya_bean) {
		this.sateikaisya_bean = sateikaisya_bean;
	}
	//業務フローパターンメンテナンス情報
	public WorkFlowBean getWorkflow_bean() {
		return workflow_bean;
	}
	public void setWorkflow_bean(WorkFlowBean workflow_bean) {
		this.workflow_bean = workflow_bean;
	}
	//ユーザメンテナンス情報
	public UserMaintenanceBean getUser_maintenance_bean() {
		return user_maintenance_bean;
	}
	public void setUser_maintenance_bean(UserMaintenanceBean user_maintenance_bean) {
		this.user_maintenance_bean = user_maintenance_bean;
	}
	//抽出条件メンテナンス情報
	public TyusyutuJokenBean getJoken_bean() {
		return joken_bean;
	}
	public void setJoken_bean(TyusyutuJokenBean joken_bean) {
		this.joken_bean = joken_bean;
	}	
	//言語モード
	public String getComLangMode() {
		return comLangMode;
	}
	public void setComLangMode(String comLangMode) {
		this.comLangMode = comLangMode;
	}
	//遷移元画面ID
	public String getReturn_gamenId() {
		return return_gamenId;
	}
	public void setReturn_gamenId(String return_gamenId) {
		this.return_gamenId = return_gamenId;
	}
	//詳細画面ID
	public String getSyosai_returnId() {
		return syosai_returnId;
	}
	public void setSyosai_returnId(String syosai_returnId) {
		this.syosai_returnId = syosai_returnId;
	}
	//共通タブ利用画面ID
	public String getTab_riyou_gamenId() {
		return tab_riyou_gamenId;
	}
	public void setTab_riyou_gamenId(String tab_riyou_gamenId) {
		this.tab_riyou_gamenId = tab_riyou_gamenId;
	}
	//ラベル名１
	public String getLbl_nm1() {
		return lbl_nm1;
	}
	public void setLbl_nm1(String lbl_nm1) {
		this.lbl_nm1 = lbl_nm1;
	}
	//ラベル名２
	public String getLbl_nm2() {
		return lbl_nm2;
	}
	public void setLbl_nm2(String lbl_nm2) {
		this.lbl_nm2 = lbl_nm2;
	}
	//ラベル名３
	public String getLbl_nm3() {
		return lbl_nm3;
	}
	public void setLbl_nm3(String lbl_nm3) {
		this.lbl_nm3 = lbl_nm3;
	}
	//ラベル名４
	public String getLbl_nm4() {
		return lbl_nm4;
	}
	public void setLbl_nm4(String lbl_nm4) {
		this.lbl_nm4 = lbl_nm4;
	}
	//ラベル名５
	public String getLbl_nm5() {
		return lbl_nm5;
	}
	public void setLbl_nm5(String lbl_nm5) {
		this.lbl_nm5 = lbl_nm5;
	}
	//ラベル名６
	public String getLbl_nm6() {
		return lbl_nm6;
	}
	public void setLbl_nm6(String lbl_nm6) {
		this.lbl_nm6 = lbl_nm6;
	}
	//ラベル名７
	public String getLbl_nm7() {
		return lbl_nm7;
	}
	public void setLbl_nm7(String lbl_nm7) {
		this.lbl_nm7 = lbl_nm7;
	}
	//ラベル名８
	public String getLbl_nm8() {
		return lbl_nm8;
	}
	public void setLbl_nm8(String lbl_nm8) {
		this.lbl_nm8 = lbl_nm8;
	}
	//ラベル名９
	public String getLbl_nm9() {
		return lbl_nm9;
	}
	public void setLbl_nm9(String lbl_nm9) {
		this.lbl_nm9 = lbl_nm9;
	}
	//ラベル名１０
	public String getLbl_nm10() {
		return lbl_nm10;
	}
	public void setLbl_nm10(String lbl_nm10) {
		this.lbl_nm10 = lbl_nm10;
	}
	public String getSateiSansyoFlg() {
		return sateiSansyoFlg;
	}
	public void setSateiSansyoFlg(String sateiSansyoFlg) {
		this.sateiSansyoFlg = sateiSansyoFlg;
	}
}