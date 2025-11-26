/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app;

import common.global.GS;

/**
 * 業務フローパターンメンテナンス情報Beanクラス
 * 
 */
public class WorkFlowBean {

	private String id							= null;	// 明細ID
	private String workflowId	    	    	= null;	// パターンID	
	private String display_workflowId	    	= null;	// 表示用パターンid
	private String workflowSystemkbn			= null;	// システム区分	
	private String workflowSystemkbn_nm		= null;	// システム区分名称	
	private String bunrui1						= null;	// 分類１	
	private String workflow_nm_ja	       		= null;	// 業務フローパターン名称（日本語）	
	private String workflow_nm_en	       		= null;	// 業務フローパターン名称（英語）	
	private String tairyu_hantei_t_flg			= null;	// 実質滞留判定登録フラグ
	private String tairyu_hantei_s_flg			= null;	// 実質滞留判定承認フラグ
	private String tairyu_kensho_t_flg			= null;	// 実質滞留検証登録フラグ
	private String tairyu_kensho_s_flg			= null;	// 実質滞留検証承認フラグ
	private String taishosaki_sentei_t_flg		= null;	// 対象先選定登録フラグ
	private String taishosaki_sentei_s_flg		= null;	// 対象先選定承認フラグ
	private String ichiji_satei_t_flg			= null;	// 一次査定登録フラグ
	private String ichiji_satei_s_flg			= null;	// 一次査定承認フラグ
	private String ichiji_sateikensyo_t_flg	= null;	// 一次査定検証登録フラグ
	private String ichiji_sateikensyo_s_flg	= null;	// 一次査定検証承認フラグ
	private String niji_satei_t_flg			= null;	// 二次査定登録フラグ
	private String niji_satei_s_flg			= null;	// 二次査定承認フラグ
	private String hikiatekin_kensyo_t_flg		= null;	// 引当金検証登録フラグ
	private String hikiatekin_kensyo_s_flg		= null;	// 引当金検証承認フラグ
	private String hikiatekin_kakunin_t_flg	= null;	// 引当金確認登録フラグ
	private String hikiatekin_kakunin_s_flg	= null;	// 引当金確認承認フラグ
	private String claim_reset_t_flg			= null;	// クレーム債権再設定登録フラグ
	private String claim_reset_s_flg			= null;	// クレーム債権再設定承認フラグ
	private String daikoset_flg				= null;	// 代行設定フラグ
	private String sateikasya_flg				= null;	// 査定会社メンテナンスフラグ
	private String workflow_pattern_flg		= null;	// 業務フローパターンメンテナンスフラグ
	private String user_master_flg				= null;	// ユーザマスタメンテナンスフラグ
	private String kanjo_master_flg			= null;	// 勘定科目マスタメンテナンスフラグ
	private String joken_master_hq_flg			= null;	// 抽出条件マスタメンテナンス（国内）フラグ
	private String joken_master_flg			= null;	// 抽出条件マスタメンテナンスフラグ
	private String champion_bu_flg				= null;	// チャンピオン部メンテナンスフラグ
	private String golf_kaiinken_flg			= null;	// ゴルフ会員権メンテナンスフラグ
	private String renketsu_upload_flg			= null;	// 連結区分マスタUPLOADフラグ
	private String jimukyoku_sashi_flg			= null;	// 事務局経由差戻フラグ
	private String satei_kanryo_sashi_flg		= null;	// 査定完了後差戻フラグ
	private String niji_satei_kbn				= null;	// 二次査定区分
	
	private String admin_senyo_flg				= null;	// システム管理者専用フラグ
	private String jishi_phase					= null;	// 実施フェーズ
	private String kaishi_status				= null;	// 開始ステータス
	private String jishi_phase_kanryo_flg		= null;	// 実施フェーズ完了フラグ
	private String ji_jishi_phase				= null;	// 次実施フェーズ
	private String ji_kaishi_status			= null;	// 次開始ステータス

	
	
	
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		id							= GS.EMPTY_CHARCTER;
		workflowId					= GS.EMPTY_CHARCTER;
		display_workflowId			= GS.EMPTY_CHARCTER;
		workflowSystemkbn			= GS.EMPTY_CHARCTER;
		workflowSystemkbn_nm		= GS.EMPTY_CHARCTER;
		bunrui1						= GS.EMPTY_CHARCTER;
		workflow_nm_ja				= GS.EMPTY_CHARCTER;
		workflow_nm_en	    	   	= GS.EMPTY_CHARCTER;
		tairyu_hantei_t_flg			= GS.EMPTY_CHARCTER;
		tairyu_hantei_s_flg			= GS.EMPTY_CHARCTER;
		tairyu_kensho_t_flg			= GS.EMPTY_CHARCTER;
		tairyu_kensho_s_flg			= GS.EMPTY_CHARCTER;
		taishosaki_sentei_t_flg		= GS.EMPTY_CHARCTER;
		taishosaki_sentei_s_flg		= GS.EMPTY_CHARCTER;
		ichiji_satei_t_flg			= GS.EMPTY_CHARCTER;
		ichiji_satei_s_flg			= GS.EMPTY_CHARCTER;
		ichiji_sateikensyo_t_flg	= GS.EMPTY_CHARCTER;
		ichiji_sateikensyo_s_flg	= GS.EMPTY_CHARCTER;
		niji_satei_t_flg			= GS.EMPTY_CHARCTER;
		niji_satei_s_flg			= GS.EMPTY_CHARCTER;
		hikiatekin_kensyo_t_flg		= GS.EMPTY_CHARCTER;
		hikiatekin_kensyo_s_flg		= GS.EMPTY_CHARCTER;
		hikiatekin_kakunin_t_flg	= GS.EMPTY_CHARCTER;
		hikiatekin_kakunin_s_flg	= GS.EMPTY_CHARCTER;
		claim_reset_t_flg			= GS.EMPTY_CHARCTER;
		claim_reset_s_flg			= GS.EMPTY_CHARCTER;
		daikoset_flg				= GS.EMPTY_CHARCTER;
		sateikasya_flg				= GS.EMPTY_CHARCTER;
		workflow_pattern_flg		= GS.EMPTY_CHARCTER;
		user_master_flg				= GS.EMPTY_CHARCTER;
		kanjo_master_flg			= GS.EMPTY_CHARCTER;
		joken_master_hq_flg			= GS.EMPTY_CHARCTER;
		joken_master_flg			= GS.EMPTY_CHARCTER;
		champion_bu_flg				= GS.EMPTY_CHARCTER;
		golf_kaiinken_flg			= GS.EMPTY_CHARCTER;
		renketsu_upload_flg			= GS.EMPTY_CHARCTER;
		jimukyoku_sashi_flg			= GS.EMPTY_CHARCTER;
		satei_kanryo_sashi_flg		= GS.EMPTY_CHARCTER;
		niji_satei_kbn				= GS.EMPTY_CHARCTER;
		admin_senyo_flg				= GS.EMPTY_CHARCTER;
		jishi_phase					= GS.EMPTY_CHARCTER;
		kaishi_status				= GS.EMPTY_CHARCTER;
		jishi_phase_kanryo_flg		= GS.EMPTY_CHARCTER;
		ji_jishi_phase				= GS.EMPTY_CHARCTER;
		ji_kaishi_status			= GS.EMPTY_CHARCTER;
	}
	
	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	
	//明細ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//パターンID
	public String getWorkflowId() {
		return workflowId;
	}
	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}
	// 表示用パターンId
	
	public String getDisplay_workflowId() {
		return display_workflowId;
	}

	public void setDisplay_workflowId(String display_workflowId) {
		this.display_workflowId = display_workflowId;
	}
	//システム区分	
	public String getWorkflowSystemkbn() {
		return workflowSystemkbn;
	}
	public void setWorkflowSystemkbn(String workflowSystemkbn) {
		this.workflowSystemkbn = workflowSystemkbn;
	}
	//システム区分名称
	public String getWorkflowSystemkbn_nm() {
		return workflowSystemkbn_nm;
	}
	public void setWorkflowSystemkbn_nm(String workflowSystemkbn_nm) {
		this.workflowSystemkbn_nm = workflowSystemkbn_nm;
	}
	//分類１
	public String getBunrui1() {
		return bunrui1;
	}
	public void setBunrui1(String bunrui1) {
		this.bunrui1 = bunrui1;
	}
	//業務フローパターン名称（日本語）
	public String getWorkflow_nm_ja() {
		return workflow_nm_ja;
	}
	public void setWorkflow_nm_ja(String workflow_nm_ja) {
		this.workflow_nm_ja = workflow_nm_ja;
	}
	//業務フローパターン名称（英語）
	public String getWorkflow_nm_en() {
		return workflow_nm_en;
	}
	public void setWorkflow_nm_en(String workflow_nm_en) {
		this.workflow_nm_en = workflow_nm_en;
	}
	//実質滞留判定登録フラグ
	public String getTairyu_hantei_t_flg() {
		return tairyu_hantei_t_flg;
	}
	public void setTairyu_hantei_t_flg(String tairyu_hantei_t_flg) {
		this.tairyu_hantei_t_flg = tairyu_hantei_t_flg;
	}
	//実質滞留判定承認フラグ
	public String getTairyu_hantei_s_flg() {
		return tairyu_hantei_s_flg;
	}
	public void setTairyu_hantei_s_flg(String tairyu_hantei_s_flg) {
		this.tairyu_hantei_s_flg = tairyu_hantei_s_flg;
	}
	//実質滞留検証登録フラグ
	public String getTairyu_kensho_t_flg() {
		return tairyu_kensho_t_flg;
	}
	public void setTairyu_kensho_t_flg(String tairyu_kensho_t_flg) {
		this.tairyu_kensho_t_flg = tairyu_kensho_t_flg;
	}
	//実質滞留検証承認フラグ
	public String getTairyu_kensho_s_flg() {
		return tairyu_kensho_s_flg;
	}
	public void setTairyu_kensho_s_flg(String tairyu_kensho_s_flg) {
		this.tairyu_kensho_s_flg = tairyu_kensho_s_flg;
	}
	//対象先選定登録フラグ
	public String getTaishosaki_sentei_t_flg() {
		return taishosaki_sentei_t_flg;
	}
	public void setTaishosaki_sentei_t_flg(String taishosaki_sentei_t_flg) {
		this.taishosaki_sentei_t_flg = taishosaki_sentei_t_flg;
	}
	//対象先選定承認フラグ
	public String getTaishosaki_sentei_s_flg() {
		return taishosaki_sentei_s_flg;
	}
	public void setTaishosaki_sentei_s_flg(String taishosaki_sentei_s_flg) {
		this.taishosaki_sentei_s_flg = taishosaki_sentei_s_flg;
	}
	//一次査定登録フラグ
	public String getIchiji_satei_t_flg() {
		return ichiji_satei_t_flg;
	}
	public void setIchiji_satei_t_flg(String ichiji_satei_t_flg) {
		this.ichiji_satei_t_flg = ichiji_satei_t_flg;
	}
	//一次査定承認フラグ
	public String getIchiji_satei_s_flg() {
		return ichiji_satei_s_flg;
	}
	public void setIchiji_satei_s_flg(String ichiji_satei_s_flg) {
		this.ichiji_satei_s_flg = ichiji_satei_s_flg;
	}
	//一次査定検証登録フラグ
	public String getIchiji_sateikensyo_t_flg() {
		return ichiji_sateikensyo_t_flg;
	}
	public void setIchiji_sateikensyo_t_flg(String ichiji_sateikensyo_t_flg) {
		this.ichiji_sateikensyo_t_flg = ichiji_sateikensyo_t_flg;
	}
	//一次査定検証承認フラグ
	public String getIchiji_sateikensyo_s_flg() {
		return ichiji_sateikensyo_s_flg;
	}
	public void setIchiji_sateikensyo_s_flg(String ichiji_sateikensyo_s_flg) {
		this.ichiji_sateikensyo_s_flg = ichiji_sateikensyo_s_flg;
	}
	//二次査定登録フラグ
	public String getNiji_satei_t_flg() {
		return niji_satei_t_flg;
	}
	public void setNiji_satei_t_flg(String niji_satei_t_flg) {
		this.niji_satei_t_flg = niji_satei_t_flg;
	}
	//二次査定承認フラグ
	public String getNiji_satei_s_flg() {
		return niji_satei_s_flg;
	}
	public void setNiji_satei_s_flg(String niji_satei_s_flg) {
		this.niji_satei_s_flg = niji_satei_s_flg;
	}
	//引当金検証登録フラグ
	public String getHikiatekin_kensyo_t_flg() {
		return hikiatekin_kensyo_t_flg;
	}
	public void setHikiatekin_kensyo_t_flg(String hikiatekin_kensyo_t_flg) {
		this.hikiatekin_kensyo_t_flg = hikiatekin_kensyo_t_flg;
	}	
	//引当金検証承認フラグ
	public String getHikiatekin_kensyo_s_flg() {
		return hikiatekin_kensyo_s_flg;
	}
	public void setHikiatekin_kensyo_s_flg(String hikiatekin_kensyo_s_flg) {
		this.hikiatekin_kensyo_s_flg = hikiatekin_kensyo_s_flg;
	}
	//引当金確認登録フラグ
	public String getHikiatekin_kakunin_t_flg() {
		return hikiatekin_kakunin_t_flg;
	}
	public void setHikiatekin_kakunin_t_flg(String hikiatekin_kakunin_t_flg) {
		this.hikiatekin_kakunin_t_flg = hikiatekin_kakunin_t_flg;
	}
	//引当金確認承認フラグ
	public String getHikiatekin_kakunin_s_flg() {
		return hikiatekin_kakunin_s_flg;
	}
	public void setHikiatekin_kakunin_s_flg(String hikiatekin_kakunin_s_flg) {
		this.hikiatekin_kakunin_s_flg = hikiatekin_kakunin_s_flg;
	}
	//クレーム債権再設定登録フラグ
	public String getClaim_reset_t_flg() {
		return claim_reset_t_flg;
	}
	public void setClaim_reset_t_flg(String claim_reset_t_flg) {
		this.claim_reset_t_flg = claim_reset_t_flg;
	}	
	//クレーム債権再設定承認フラグ
	public String getClaim_reset_s_flg() {
		return claim_reset_s_flg;
	}
	public void setClaim_reset_s_flg(String claim_reset_s_flg) {
		this.claim_reset_s_flg = claim_reset_s_flg;
	}
	//代行設定フラグ
	public String getDaikoset_flg() {
		return daikoset_flg;
	}
	public void setDaikoset_flg(String daikoset_flg) {
		this.daikoset_flg = daikoset_flg;
	}
	//査定会社メンテナンスフラグ
	public String getSateikasya_flg() {
		return sateikasya_flg;
	}
	public void setSateikasya_flg(String sateikasya_flg) {
		this.sateikasya_flg = sateikasya_flg;
	}
	//業務フローパターンメンテナンスフラグ
	public String getWorkflow_pattern_flg() {
		return workflow_pattern_flg;
	}
	public void setWorkflow_pattern_flg(String workflow_pattern_flg) {
		this.workflow_pattern_flg = workflow_pattern_flg;
	}
	//ユーザマスタメンテナンスフラグ
	public String getUser_master_flg() {
		return user_master_flg;
	}
	public void setUser_master_flg(String user_master_flg) {
		this.user_master_flg = user_master_flg;
	}
	//勘定科目マスタメンテナンスフラグ
	public String getKanjo_master_flg() {
		return kanjo_master_flg;
	}
	public void setKanjo_master_flg(String kanjo_master_flg) {
		this.kanjo_master_flg = kanjo_master_flg;
	}
	//抽出条件マスタメンテナンス（国内）フラグ
	public String getJoken_master_hq_flg() {
		return joken_master_hq_flg;
	}
	public void setJoken_master_hq_flg(String joken_master_hq_flg) {
		this.joken_master_hq_flg = joken_master_hq_flg;
	}
	//抽出条件マスタメンテナンスフラグ
	public String getJoken_master_flg() {
		return joken_master_flg;
	}
	public void setJoken_master_flg(String joken_master_flg) {
		this.joken_master_flg = joken_master_flg;
	}
	//チャンピオン部メンテナンスフラグ
	public String getChampion_bu_flg() {
		return champion_bu_flg;
	}
	public void setChampion_bu_flg(String champion_bu_flg) {
		this.champion_bu_flg = champion_bu_flg;
	}
	//ゴルフ会員権メンテナンスフラグ
	public String getGolf_kaiinken_flg() {
		return golf_kaiinken_flg;
	}
	public void setGolf_kaiinken_flg(String golf_kaiinken_flg) {
		this.golf_kaiinken_flg = golf_kaiinken_flg;
	}
	//連結区分マスタUPLOADフラグ
	public String getRenketsu_upload_flg() {
		return renketsu_upload_flg;
	}
	public void setRenketsu_upload_flg(String renketsu_upload_flg) {
		this.renketsu_upload_flg = renketsu_upload_flg;
	}
	//事務局経由差戻フラグ
	public String getJimukyoku_sashi_flg() {
		return jimukyoku_sashi_flg;
	}
	public void setJimukyoku_sashi_flg(String jimukyoku_sashi_flg) {
		this.jimukyoku_sashi_flg = jimukyoku_sashi_flg;
	}
	//査定完了後差戻フラグ
	public String getSatei_kanryo_sashi_flg() {
		return satei_kanryo_sashi_flg;
	}
	public void setSatei_kanryo_sashi_flg(String satei_kanryo_sashi_flg) {
		this.satei_kanryo_sashi_flg = satei_kanryo_sashi_flg;
	}
	//二次査定区分
	public String getNiji_satei_kbn() {
		return niji_satei_kbn;
	}
	public void setNiji_satei_kbn(String niji_satei_kbn) {
		this.niji_satei_kbn = niji_satei_kbn;
	}
	//システム管理者専用フラグ
	public String getadmin_senyo_flg() {
		return admin_senyo_flg;
	}
	public void setadmin_senyo_flg(String admin_senyo_flg) {
		this.admin_senyo_flg = admin_senyo_flg;
	}
	//実施フェーズ
	public String getjishi_phase() {
		return jishi_phase;
	}
	public void setjishi_phase(String jishi_phase) {
		this.jishi_phase = jishi_phase;
	}
	//開始ステータス
	public String getkaishi_status() {
		return kaishi_status;
	}
	public void setkaishi_status(String kaishi_status) {
		this.kaishi_status = kaishi_status;
	}
	//実施フェーズ完了フラグ
	public String getjishi_phase_kanryo_flg() {
		return jishi_phase_kanryo_flg;
	}
	public void setjishi_phase_kanryo_flg(String jishi_phase_kanryo_flg) {
		this.jishi_phase_kanryo_flg = jishi_phase_kanryo_flg;
	}
	//次実施フェーズ
	public String getji_jishi_phase() {
		return ji_jishi_phase;
	}
	public void setji_jishi_phase(String ji_jishi_phase) {
		this.ji_jishi_phase = ji_jishi_phase;
	}
	//次開始ステータス
	public String ji_kaishi_status() {
		return ji_kaishi_status;
	}
	public void ji_kaishi_status(String ji_kaishi_status) {
		this.ji_kaishi_status = ji_kaishi_status;
	}

}