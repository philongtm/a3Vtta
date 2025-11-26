/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/03/02		SSC				BJ201602002 部門廃止対応（一次）_本部絞込対応
******************************************************************************/
package app;


import common.global.GS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ユーザ情報Beanクラス
 * 
 */
public class UserBean {

	//ユーザ情報
	private String comUserId					= null;	// ユーザID
	private String comUser_Nm					= null;	// ユーザ名日本語
	private String comUser_Nm_En				= null;	// ユーザ名英語
	private String comSystemManager_flg		= null;	// システム管理者フラグ
	private String comMailHaisinKbn			= null;	// メール配信区分
	private String comTyohyo_default_kbn		= null;	// 帳票出力デフォルト言語区分
	private String comWorkflowSystemkbn		= null;	// 業務フローパターンシステム区分	
	private String comWorkflowSateikaisya_cd	= null;	// 業務フローパターン査定会社コード	
	private String comWorkflowId	        	= null;	// 業務フローパターンID	
	private List<HashMap> 	comWorkflowList		= null;	// 業務フローパターン【リスト】	
	private String comTairyu_hantei_t_flg		= null;	// 実質滞留判定登録フラグ
	private String comTairyu_hantei_s_flg		= null;	// 実質滞留判定承認フラグ
	private String comTairyu_kensho_t_flg		= null;	// 実質滞留検証登録フラグ
	private String comTairyu_kensho_s_flg		= null;	// 実質滞留検証承認フラグ
	private String comTaishosaki_sentei_t_flg	= null;	// 対象先選定登録フラグ
	private String comTaishosaki_sentei_s_flg	= null;	// 対象先選定承認フラグ
	private String comIchiji_satei_t_flg		= null;	// 一次査定登録フラグ
	private String comIchiji_satei_s_flg		= null;	// 一次査定承認フラグ
	private String comIchiji_sateikensyo_t_flg	= null;	// 一次査定検証登録フラグ
	private String comIchiji_sateikensyo_s_flg	= null;	// 一次査定検証承認フラグ
	private String comNiji_satei_t_flg			= null;	// 二次査定登録フラグ
	private String comNiji_satei_s_flg			= null;	// 二次査定承認フラグ
	private String comHikiatekin_kensyo_t_flg	= null;	// 引当金検証登録フラグ
	private String comHikiatekin_kensyo_s_flg	= null;	// 引当金検証承認フラグ
	private String comHikiatekin_kakunin_t_flg	= null;	// 引当金確認登録フラグ
	private String comHikiatekin_kakunin_s_flg	= null;	// 引当金確認承認フラグ
	private String comClaim_reset_t_flg		= null;	// クレーム債権再設定登録フラグ
	private String comClaim_reset_s_flg		= null;	// クレーム債権再設定承認フラグ
	private String comDaikoset_flg				= null;	// 代行設定フラグ
	private String comSateikasya_flg			= null;	// 査定会社メンテナンスフラグフラグ
	private String comWorkflow_pattern_flg		= null;	// 業務フローパターンメンテナンスフラグ
	private String comUser_master_flg			= null;	// ユーザマスタメンテナンスフラグ
	private String comKanjo_master_flg			= null;	// 勘定科目マスタメンテナンスフラグ
	private String comJoken_master_hq_flg		= null;	// 抽出条件マスタメンテナンス（国内）フラグ
	private String comJoken_master_flg			= null;	// 抽出条件マスタメンテナンスフラグ
	private String comChampion_bu_flg			= null;	// チャンピオン部メンテナンスフラグ
	private String comGolf_kaiinken_flg		= null;	// ゴルフ会員権メンテナンスフラグ
	private String comRenketsu_upload_flg		= null;	// 連結区分マスタUPLOADフラグ
	private String comJimukyoku_sashi_flg		= null;	// 事務局経由差戻フラグ
	private String comSatei_kanryo_sashi_flg	= null;	// 査定完了後差戻フラグ
	private String comNiji_satei_kbn			= null;	// 二次査定区分
	private ArrayList comOparation				= null;	// 実施業務
	private String comEmailAddr				= null;	// メールアドレス
	private String comKaishaCd					= null;	// 会社コード
	private String comKaisha_Nm				= null;	// 会社名(日本語)
	private String comKaisha_Nm_En				= null;	// 会社名(英語)
	private String comSyozokuSoshikiCd			= null;	// 所属組織コード
	private String comSyozokuSoshiki_Nm		= null;	// 所属組織名称日本語
	private String comSyozokuSoshiki_Nm_En		= null;	// 所属組織名称英語
	private String comSyozokuBunrui1			= null;	// 分類１コード(海外：地域、国内：会社)
	private String comSyozokuBunrui2			= null;	// 分類２コード(海外：会社、国内：部門)
	private String comSyozokuBunrui3			= null;	// 分類３コード(海外：セル、国内：課)
	private String comSyozokuHonbuCd			= null;	// 本部コード
	private String comSyozokuBuCd				= null;	// 部コード
	private String comSyozokuBunrui1_Nm		= null;	// 分類１名称
	private String comSyozokuBunrui2_Nm		= null;	// 分類２名称
	private String comSyozokuBunrui3_Nm		= null;	// 分類３名称
	private String comSyozokuHonbu_Nm			= null;	// 本部名称
	private String comSyozokuBu_Nm				= null;	// 部名称
	private String comSansyoBunrui1			= null;	// 参照分類１コード
	private String comSansyoBunrui2			= null;	// 参照分類２コード
	private String comSansyoHonbuCd			= null;	// 参照本部コード
	private String comDaiko_userId   			= null;	// 代行ユーザID
	private String comDaiko_user_nm			= null;	// 代行者名(日本語)
	private String comDaiko_user_nm_en			= null;	// 代行者名(英語)
	private HashMap<String,String> comSansyososhiki_all = null;	// 全参照組織【マップ】
	
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		//ユーザ情報
		comUserId					= GS.EMPTY_CHARCTER;	// ユーザID
		comUser_Nm					= GS.EMPTY_CHARCTER;	// ユーザ名日本語
		comUser_Nm_En				= GS.EMPTY_CHARCTER;	// ユーザ名英語
		comSystemManager_flg		= GS.EMPTY_CHARCTER;	// システム管理者フラグ
		comMailHaisinKbn			= GS.EMPTY_CHARCTER;	// メール配信区分
		comTyohyo_default_kbn		= GS.EMPTY_CHARCTER;	// 帳票出力デフォルト言語区分
		comWorkflowSystemkbn		= GS.EMPTY_CHARCTER;	// 業務フローパターンシステム区分	
		comWorkflowSateikaisya_cd	= GS.EMPTY_CHARCTER;	// 業務フローパターン査定会社コード	
		comWorkflowId	        	= GS.EMPTY_CHARCTER;	// 業務フローパターンID	
		comWorkflowList		   		= null;					// 業務フローパターン【リスト】	
		comTairyu_hantei_t_flg		= GS.EMPTY_CHARCTER;	// 実質滞留判定登録フラグ
		comTairyu_hantei_s_flg		= GS.EMPTY_CHARCTER;	// 実質滞留判定承認フラグ
		comTairyu_kensho_t_flg		= GS.EMPTY_CHARCTER;	// 実質滞留検証登録フラグ
		comTairyu_kensho_s_flg		= GS.EMPTY_CHARCTER;	// 実質滞留検証承認フラグ
		comTaishosaki_sentei_t_flg	= GS.EMPTY_CHARCTER;	// 対象先選定登録フラグ
		comTaishosaki_sentei_s_flg	= GS.EMPTY_CHARCTER;	// 対象先選定承認フラグ
		comIchiji_satei_t_flg		= GS.EMPTY_CHARCTER;	// 一次査定登録フラグ
		comIchiji_satei_s_flg		= GS.EMPTY_CHARCTER;	// 一次査定承認フラグ
		comIchiji_sateikensyo_t_flg	= GS.EMPTY_CHARCTER;	// 一次査定検証登録フラグ
		comIchiji_sateikensyo_s_flg	= GS.EMPTY_CHARCTER;	// 一次査定検証承認フラグ
		comNiji_satei_t_flg			= GS.EMPTY_CHARCTER;	// 二次査定登録フラグ
		comNiji_satei_s_flg			= GS.EMPTY_CHARCTER;	// 二次査定承認フラグ
		comHikiatekin_kensyo_t_flg	= GS.EMPTY_CHARCTER;	// 引当金検証登録フラグ
		comHikiatekin_kensyo_s_flg	= GS.EMPTY_CHARCTER;	// 引当金検証承認フラグ
		comHikiatekin_kakunin_t_flg	= GS.EMPTY_CHARCTER;	// 引当金確認登録フラグ
		comHikiatekin_kakunin_s_flg	= GS.EMPTY_CHARCTER;	// 引当金確認承認フラグ
		comClaim_reset_t_flg		= GS.EMPTY_CHARCTER;	// クレーム債権再設定登録フラグ
		comClaim_reset_s_flg		= GS.EMPTY_CHARCTER;	// クレーム債権再設定承認フラグ
		comDaikoset_flg				= GS.EMPTY_CHARCTER;	// 代行設定フラグ
		comSateikasya_flg			= GS.EMPTY_CHARCTER;	// 査定会社メンテナンスフラグ
		comWorkflow_pattern_flg		= GS.EMPTY_CHARCTER;	// 業務フローパターンメンテナンスフラグ
		comUser_master_flg			= GS.EMPTY_CHARCTER;	// ユーザマスタメンテナンスフラグ
		comKanjo_master_flg			= GS.EMPTY_CHARCTER;	// 勘定科目マスタメンテナンスフラグ
		comJoken_master_hq_flg		= GS.EMPTY_CHARCTER;	// 抽出条件マスタメンテナンス（国内）フラグ
		comJoken_master_flg			= GS.EMPTY_CHARCTER;	// 抽出条件マスタメンテナンスフラグ
		comChampion_bu_flg			= GS.EMPTY_CHARCTER;	// チャンピオン部メンテナンスフラグ
		comGolf_kaiinken_flg		= GS.EMPTY_CHARCTER;	// ゴルフ会員権メンテナンスフラグ
		comRenketsu_upload_flg		= GS.EMPTY_CHARCTER;	// 連結区分マスタUPLOADフラグ
		comJimukyoku_sashi_flg		= GS.EMPTY_CHARCTER;	// 事務局経由差戻フラグ
		comSatei_kanryo_sashi_flg	= GS.EMPTY_CHARCTER;	// 査定完了後差戻フラグ
		comNiji_satei_kbn			= GS.EMPTY_CHARCTER;	// 二次査定区分
		comOparation				= null;					// 実施業務
		comEmailAddr				= GS.EMPTY_CHARCTER;	// メールアドレス
		comKaishaCd					= GS.EMPTY_CHARCTER;	// 会社コード
		comKaisha_Nm				= GS.EMPTY_CHARCTER;	// 会社名(日本語)
		comKaisha_Nm_En				= GS.EMPTY_CHARCTER;	// 会社名(英語)
		comSyozokuSoshikiCd			= GS.EMPTY_CHARCTER;	// 所属組織コード
		comSyozokuSoshiki_Nm		= GS.EMPTY_CHARCTER;	// 所属組織名称日本語
		comSyozokuSoshiki_Nm_En		= GS.EMPTY_CHARCTER;	// 所属組織名称英語
		comSyozokuBunrui1			= GS.EMPTY_CHARCTER;	// 分類１コード
		comSyozokuBunrui2			= GS.EMPTY_CHARCTER;	// 分類２コード
		comSyozokuBunrui3			= GS.EMPTY_CHARCTER;	// 分類３コード
		comSyozokuHonbuCd			= GS.EMPTY_CHARCTER;	// 本部コード
		comSyozokuBuCd				= GS.EMPTY_CHARCTER;	// 部コード
		comSyozokuBunrui1_Nm		= GS.EMPTY_CHARCTER;	// 分類１名称
		comSyozokuBunrui2_Nm		= GS.EMPTY_CHARCTER;	// 分類２名称
		comSyozokuBunrui3_Nm		= GS.EMPTY_CHARCTER;	// 分類３名称
		comSyozokuHonbu_Nm			= GS.EMPTY_CHARCTER;	// 本部名称
		comSyozokuBu_Nm				= GS.EMPTY_CHARCTER;	// 部名称
		comSansyoBunrui1			= GS.EMPTY_CHARCTER;	// 参照分類１コード
		comSansyoBunrui2			= GS.EMPTY_CHARCTER;	// 参照分類２コード
		comSansyoHonbuCd			= GS.EMPTY_CHARCTER;	// 参照本部コード
		comDaiko_userId   			= GS.EMPTY_CHARCTER;	// 代行ユーザID
		comDaiko_user_nm			= GS.EMPTY_CHARCTER;	// 代行者名(日本語)
		comDaiko_user_nm_en			= GS.EMPTY_CHARCTER;	// 代行者名(英語)		
		comSansyososhiki_all		= null;					// 全参照組織【マップ】
	}
	
	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	
	//ユーザID
	public String getComUserId() {
		return comUserId;
	}
	public void setComUserId(String comUserId) {
		this.comUserId = comUserId;
	}
	//ユーザ名日本語
	public String getComUser_Nm() {
		return comUser_Nm;
	}
	public void setComUser_Nm(String comUser_Nm) {
		this.comUser_Nm = comUser_Nm;
	}
	//ユーザ名英語
	public String getComUser_Nm_En() {
		return comUser_Nm_En;
	}
	public void setComUser_Nm_En(String comUser_Nm_En) {
		this.comUser_Nm_En = comUser_Nm_En;
	}
	//システム管理者フラグ	
	public String getComSystemManager_flg() {
		return comSystemManager_flg;
	}
	public void setComSystemManager_flg(String comSystemManager_flg) {
		this.comSystemManager_flg = comSystemManager_flg;
	}
	//帳票出力デフォルト言語区分	
	public String getComTyohyo_default_kbn() {
		return comTyohyo_default_kbn;
	}
	public void setComTyohyo_default_kbn(String comTyohyo_default_kbn) {
		this.comTyohyo_default_kbn = comTyohyo_default_kbn;
	}
	//業務フローパターンシステム区分	
	public String getComWorkflowSystemkbn() {
		return comWorkflowSystemkbn;
	}
	public void setComWorkflowSystemkbn(String comWorkflowSystemkbn) {
		this.comWorkflowSystemkbn = comWorkflowSystemkbn;
	}
	//業務フローパターン査定会社コード	
	public String getComWorkflowSateikaisya_cd() {
		return comWorkflowSateikaisya_cd;
	}
	public void setComWorkflowSateikaisya_cd(String comWorkflowSateikaisya_cd) {
		this.comWorkflowSateikaisya_cd = comWorkflowSateikaisya_cd;
	}
	//業務フローパターンID	
	public String getComWorkflowId() {
		return comWorkflowId;
	}
	public void setComWorkflowId(String comWorkflowId) {
		this.comWorkflowId = comWorkflowId;
	}
	//業務フローパターン【リスト】	
	public List<HashMap> getComWorkflowList() {
		return comWorkflowList;
	}
	public void setComWorkflowList(List<HashMap> comWorkflowList) {
		this.comWorkflowList = comWorkflowList;
	}
	//実質滞留判定登録フラグ
	public String getComTairyu_hantei_t_flg() {
		return comTairyu_hantei_t_flg;
	}
	public void setComTairyu_hantei_t_flg(String comTairyu_hantei_t_flg) {
		this.comTairyu_hantei_t_flg = comTairyu_hantei_t_flg;
	}
	//実質滞留判定承認フラグ
	public String getComTairyu_hantei_s_flg() {
		return comTairyu_hantei_s_flg;
	}
	public void setComTairyu_hantei_s_flg(String comTairyu_hantei_s_flg) {
		this.comTairyu_hantei_s_flg = comTairyu_hantei_s_flg;
	}
	//実質滞留検証登録フラグ
	public String getComTairyu_kensho_t_flg() {
		return comTairyu_kensho_t_flg;
	}
	public void setComTairyu_kensho_t_flg(String comTairyu_kensho_t_flg) {
		this.comTairyu_kensho_t_flg = comTairyu_kensho_t_flg;
	}
	//実質滞留検証承認フラグ
	public String getComTairyu_kensho_s_flg() {
		return comTairyu_kensho_s_flg;
	}
	public void setComTairyu_kensho_s_flg(String comTairyu_kensho_s_flg) {
		this.comTairyu_kensho_s_flg = comTairyu_kensho_s_flg;
	}
	//対象先選定登録フラグ
	public String getComTaishosaki_sentei_t_flg() {
		return comTaishosaki_sentei_t_flg;
	}
	public void setComTaishosaki_sentei_t_flg(String comTaishosaki_sentei_t_flg) {
		this.comTaishosaki_sentei_t_flg = comTaishosaki_sentei_t_flg;
	}
	//対象先選定承認フラグ
	public String getComTaishosaki_sentei_s_flg() {
		return comTaishosaki_sentei_s_flg;
	}
	public void setComTaishosaki_sentei_s_flg(String comTaishosaki_sentei_s_flg) {
		this.comTaishosaki_sentei_s_flg = comTaishosaki_sentei_s_flg;
	}
	//一次査定登録フラグ
	public String getComIchiji_satei_t_flg() {
		return comIchiji_satei_t_flg;
	}
	public void setComIchiji_satei_t_flg(String comIchiji_satei_t_flg) {
		this.comIchiji_satei_t_flg = comIchiji_satei_t_flg;
	}
	//一次査定承認フラグ
	public String getComIchiji_satei_s_flg() {
		return comIchiji_satei_s_flg;
	}
	public void setComIchiji_satei_s_flg(String comIchiji_satei_s_flg) {
		this.comIchiji_satei_s_flg = comIchiji_satei_s_flg;
	}
	//一次査定検証登録フラグ
	public String getComIchiji_sateikensyo_t_flg() {
		return comIchiji_sateikensyo_t_flg;
	}
	public void setComIchiji_sateikensyo_t_flg(String comIchiji_sateikensyo_t_flg) {
		this.comIchiji_sateikensyo_t_flg = comIchiji_sateikensyo_t_flg;
	}
	//一次査定検証承認フラグ
	public String getComIchiji_sateikensyo_s_flg() {
		return comIchiji_sateikensyo_s_flg;
	}
	public void setComIchiji_sateikensyo_s_flg(String comIchiji_sateikensyo_s_flg) {
		this.comIchiji_sateikensyo_s_flg = comIchiji_sateikensyo_s_flg;
	}
	//二次査定登録フラグ
	public String getComNiji_satei_t_flg() {
		return comNiji_satei_t_flg;
	}
	public void setComNiji_satei_t_flg(String comNiji_satei_t_flg) {
		this.comNiji_satei_t_flg = comNiji_satei_t_flg;
	}
	//二次査定承認フラグ
	public String getComNiji_satei_s_flg() {
		return comNiji_satei_s_flg;
	}
	public void setComNiji_satei_s_flg(String comNiji_satei_s_flg) {
		this.comNiji_satei_s_flg = comNiji_satei_s_flg;
	}
	//引当金検証登録フラグ
	public String getComHikiatekin_kensyo_t_flg() {
		return comHikiatekin_kensyo_t_flg;
	}
	public void setComHikiatekin_kensyo_t_flg(String comHikiatekin_kensyo_t_flg) {
		this.comHikiatekin_kensyo_t_flg = comHikiatekin_kensyo_t_flg;
	}	
	//引当金検証承認フラグ
	public String getComHikiatekin_kensyo_s_flg() {
		return comHikiatekin_kensyo_s_flg;
	}
	public void setComHikiatekin_kensyo_s_flg(String comHikiatekin_kensyo_s_flg) {
		this.comHikiatekin_kensyo_s_flg = comHikiatekin_kensyo_s_flg;
	}
	//引当金確認登録フラグ
	public String getComHikiatekin_kakunin_t_flg() {
		return comHikiatekin_kakunin_t_flg;
	}
	public void setComHikiatekin_kakunin_t_flg(String comHikiatekin_kakunin_t_flg) {
		this.comHikiatekin_kakunin_t_flg = comHikiatekin_kakunin_t_flg;
	}
	//引当金確認承認フラグ
	public String getComHikiatekin_kakunin_s_flg() {
		return comHikiatekin_kakunin_s_flg;
	}
	public void setComHikiatekin_kakunin_s_flg(String comHikiatekin_kakunin_s_flg) {
		this.comHikiatekin_kakunin_s_flg = comHikiatekin_kakunin_s_flg;
	}
	//クレーム債権再設定登録フラグ
	public String getComClaim_reset_t_flg() {
		return comClaim_reset_t_flg;
	}
	public void setComClaim_reset_t_flg(String comClaim_reset_t_flg) {
		this.comClaim_reset_t_flg = comClaim_reset_t_flg;
	}	
	//クレーム債権再設定承認フラグ
	public String getComClaim_reset_s_flg() {
		return comClaim_reset_s_flg;
	}
	public void setComClaim_reset_s_flg(String comClaim_reset_s_flg) {
		this.comClaim_reset_s_flg = comClaim_reset_s_flg;
	}
	//代行設定フラグ
	public String getComDaikoset_flg() {
		return comDaikoset_flg;
	}
	public void setComDaikoset_flg(String comDaikoset_flg) {
		this.comDaikoset_flg = comDaikoset_flg;
	}
	//査定会社メンテナンスフラグ
	public String getComSateikasya_flg() {
		return comSateikasya_flg;
	}
	public void setComSateikasya_flg(String comSateikasya_flg) {
		this.comSateikasya_flg = comSateikasya_flg;
	}
	//業務フローパターンメンテナンスフラグ
	public String getComWorkflow_pattern_flg() {
		return comWorkflow_pattern_flg;
	}
	public void setComWorkflow_pattern_flg(String comWorkflow_pattern_flg) {
		this.comWorkflow_pattern_flg = comWorkflow_pattern_flg;
	}
	//ユーザマスタメンテナンスフラグ
	public String getComUser_master_flg() {
		return comUser_master_flg;
	}
	public void setComUser_master_flg(String comUser_master_flg) {
		this.comUser_master_flg = comUser_master_flg;
	}
	//勘定科目マスタメンテナンスフラグ
	public String getComKanjo_master_flg() {
		return comKanjo_master_flg;
	}
	public void setComKanjo_master_flg(String comKanjo_master_flg) {
		this.comKanjo_master_flg = comKanjo_master_flg;
	}
	//抽出条件マスタメンテナンス（国内）フラグ
	public String getComJoken_master_hq_flg() {
		return comJoken_master_hq_flg;
	}
	public void setComJoken_master_hq_flg(String comJoken_master_hq_flg) {
		this.comJoken_master_hq_flg = comJoken_master_hq_flg;
	}
	//抽出条件マスタメンテナンスフラグ
	public String getComJoken_master_flg() {
		return comJoken_master_flg;
	}
	public void setComJoken_master_flg(String comJoken_master_flg) {
		this.comJoken_master_flg = comJoken_master_flg;
	}
	//チャンピオン部メンテナンスフラグ
	public String getComChampion_bu_flg() {
		return comChampion_bu_flg;
	}
	public void setComChampion_bu_flg(String comChampion_bu_flg) {
		this.comChampion_bu_flg = comChampion_bu_flg;
	}
	//ゴルフ会員権メンテナンスフラグ
	public String getComGolf_kaiinken_flg() {
		return comGolf_kaiinken_flg;
	}
	public void setComGolf_kaiinken_flg(String comGolf_kaiinken_flg) {
		this.comGolf_kaiinken_flg = comGolf_kaiinken_flg;
	}
	//連結区分マスタUPLOADフラグ
	public String getComRenketsu_upload_flg() {
		return comRenketsu_upload_flg;
	}
	public void setComRenketsu_upload_flg(String comRenketsu_upload_flg) {
		this.comRenketsu_upload_flg = comRenketsu_upload_flg;
	}
	//事務局経由差戻フラグ
	public String getComJimukyoku_sashi_flg() {
		return comJimukyoku_sashi_flg;
	}
	public void setComJimukyoku_sashi_flg(String comJimukyoku_sashi_flg) {
		this.comJimukyoku_sashi_flg = comJimukyoku_sashi_flg;
	}
	//査定完了後差戻フラグ
	public String getComSatei_kanryo_sashi_flg() {
		return comSatei_kanryo_sashi_flg;
	}
	public void setComSatei_kanryo_sashi_flg(String comSatei_kanryo_sashi_flg) {
		this.comSatei_kanryo_sashi_flg = comSatei_kanryo_sashi_flg;
	}
	//二次査定区分
	public String getComNiji_satei_kbn() {
		return comNiji_satei_kbn;
	}
	public void setComNiji_satei_kbn(String comNiji_satei_kbn) {
		this.comNiji_satei_kbn = comNiji_satei_kbn;
	}
	//実施業務
	public ArrayList getComOparation() {
		return comOparation;
	}
	public void setComOparation(ArrayList comOparation) {
		this.comOparation = comOparation;
	}
	//メールアドレス
	public String getComEmailAddr() {
		return comEmailAddr;
	}
	public void setComEmailAddr(String comEmailAddr) {
		this.comEmailAddr = comEmailAddr;
	}
	//会社コード
	public String getComKaishaCd() {
		return comKaishaCd;
	}
	public void setComKaishaCd(String comKaishaCd) {
		this.comKaishaCd = comKaishaCd;
	}
	//所属組織コード
	public String getComSyozokuSoshikiCd() {
		return comSyozokuSoshikiCd;
	}
	public void setComSyozokuSoshikiCd(String comSyozokuSoshikiCd) {
		this.comSyozokuSoshikiCd = comSyozokuSoshikiCd;
	}
	//所属組織名称日本語
	public String getComSyozokuSoshiki_Nm() {
		return comSyozokuSoshiki_Nm;
	}
	public void setComSyozokuSoshiki_Nm(String comSyozokuSoshiki_Nm) {
		this.comSyozokuSoshiki_Nm = comSyozokuSoshiki_Nm;
	}
	//所属組織名称英語
	public String getComSyozokuSoshiki_Nm_En() {
		return comSyozokuSoshiki_Nm_En;
	}
	public void setComSyozokuSoshiki_Nm_En(String comSyozokuSoshiki_Nm_En) {
		this.comSyozokuSoshiki_Nm_En = comSyozokuSoshiki_Nm_En;
	}
	//分類１コード
	public String getComSyozokuBunrui1() {
		return comSyozokuBunrui1;
	}
	public void setComSyozokuBunrui1(String comSyozokuBunrui1) {
		this.comSyozokuBunrui1 = comSyozokuBunrui1;
	}
	//分類２コード
	public String getComSyozokuBunrui2() {
		return comSyozokuBunrui2;
	}
	public void setComSyozokuBunrui2(String comSyozokuBunrui2) {
		this.comSyozokuBunrui2 = comSyozokuBunrui2;
	}
	//分類３コード
	public String getComSyozokuBunrui3() {
		return comSyozokuBunrui3;
	}
	public void setComSyozokuBunrui3(String comSyozokuBunrui3) {
		this.comSyozokuBunrui3 = comSyozokuBunrui3;
	}
	//本部コード
	public String getComSyozokuHonbuCd() {
		return comSyozokuHonbuCd;
	}
	public void setComSyozokuHonbuCd(String comSyozokuHonbuCd) {
		this.comSyozokuHonbuCd = comSyozokuHonbuCd;
	}
	//部コード
	public String getComSyozokuBuCd() {
		return comSyozokuBuCd;
	}
	public void setComSyozokuBuCd(String comSyozokuBuCd) {
		this.comSyozokuBuCd = comSyozokuBuCd;
	}
	//分類１名称
	public String getComSyozokuBunrui1_Nm() {
		return comSyozokuBunrui1_Nm;
	}
	public void setComSyozokuBunrui1_Nm(String comSyozokuBunrui1_Nm) {
		this.comSyozokuBunrui1_Nm = comSyozokuBunrui1_Nm;
	}
	//分類２名称
	public String getComSyozokuBunrui2_Nm() {
		return comSyozokuBunrui2_Nm;
	}
	public void setComSyozokuBunrui2_Nm(String comSyozokuBunrui2_Nm) {
		this.comSyozokuBunrui2_Nm = comSyozokuBunrui2_Nm;
	}
	//分類３名称
	public String getComSyozokuBunrui3_Nm() {
		return comSyozokuBunrui3_Nm;
	}
	public void setComSyozokuBunrui3_Nm(String comSyozokuBunrui3_Nm) {
		this.comSyozokuBunrui3_Nm = comSyozokuBunrui3_Nm;
	}
	//本部名称
	public String getComSyozokuHonbu_Nm() {
		return comSyozokuHonbu_Nm;
	}
	public void setComSyozokuHonbu_Nm(String comSyozokuHonbu_Nm) {
		this.comSyozokuHonbu_Nm = comSyozokuHonbu_Nm;
	}
	//部名称
	public String getComSyozokuBu_Nm() {
		return comSyozokuBu_Nm;
	}
	public void setComSyozokuBu_Nm(String comSyozokuBu_Nm) {
		this.comSyozokuBu_Nm = comSyozokuBu_Nm;
	}
	//参照分類１コード
	public String getComSansyoBunrui1() {
		return comSansyoBunrui1;
	}
	public void setComSansyoBunrui1(String comSansyoBunrui1) {
		this.comSansyoBunrui1 = comSansyoBunrui1;
	}
	//参照分類２コード
	public String getComSansyoBunrui2() {
		return comSansyoBunrui2;
	}
	public void setComSansyoBunrui2(String comSansyoBunrui2) {
		this.comSansyoBunrui2 = comSansyoBunrui2;
	}
	//参照本部コード
	public String getComSansyoHonbuCd() {
		return comSansyoHonbuCd;
	}
	public void setComSansyoHonbuCd(String comSansyoHonbuCd) {
		this.comSansyoHonbuCd = comSansyoHonbuCd;
	}
	//代行ユーザID
	public String getComDaiko_userId() {
		return comDaiko_userId;
	}
	public void setComDaiko_userId(String comDaiko_userId) {
		this.comDaiko_userId = comDaiko_userId;
	}
	//代行者名日本語
	public String getComDaiko_user_nm() {
		return comDaiko_user_nm;
	}
	public void setComDaiko_user_nm(String comDaiko_user_nm) {
		this.comDaiko_user_nm = comDaiko_user_nm;
	}
	//代行者名英語
	public String getComDaiko_user_nm_en() {
		return comDaiko_user_nm_en;
	}
	public void setComDaiko_user_nm_en(String comDaiko_user_nm_en) {
		this.comDaiko_user_nm_en = comDaiko_user_nm_en;
	}

	//会社名(日本語)
	public String getComKaisha_Nm() {
		return comKaisha_Nm;
	}
	public void setComKaisha_Nm(String comKaisha_Nm) {
		this.comKaisha_Nm = comKaisha_Nm;
	}

	//会社名(英語)
	public String getComKaisha_Nm_En() {
		return comKaisha_Nm_En;
	}
	public void setComKaisha_Nm_En(String comKaisha_Nm_En) {
		this.comKaisha_Nm_En = comKaisha_Nm_En;
	}

	//メール配信区分
	public String getComMailHaisinKbn() {
		return comMailHaisinKbn;
	}
	public void setComMailHaisinKbn(String comMailHaisinKbn) {
		this.comMailHaisinKbn = comMailHaisinKbn;
	}

	//全参照組織【マップ】
	public HashMap getComSansyososhiki_all() {
		return comSansyososhiki_all;
	}
	public void setComSansyososhiki_all(HashMap<String,String> comSansyososhiki_all) {
		this.comSansyososhiki_all = comSansyososhiki_all;
	}
}