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
 * 査定会社メンテナンス情報Beanクラス
 * 
 */
public class SateiKaisyaBean {

	private String id					= null;	// 明細ID
	private String kaisya_cd			= null;	// 会社コード
    private String bunrui1				= null;	// 分類１
    private String bunrui2				= null;	// 分類２
    private String bunrui2_nm_ja		= null;	// 分類２名称（日本語）
    private String bunrui2_nm_en		= null;	// 分類２名称（英語）
	private String system_kbn			= null;	// システム区分	
	private String system_kbn_nm		= null;	// システム区分名称	
	private String standard_time		= null;	// 標準時刻	
	private String tyusyutu_taisyo_flg = null;	// 抽出対象フラグ
	private String standard_time_nm	= null;	// 標準時刻(名称)
	private String tyusyutu_taisyo_nm = null;	// 抽出対象(名称)
	
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		id					= GS.EMPTY_CHARCTER;
		kaisya_cd			= GS.EMPTY_CHARCTER;
		bunrui1				= GS.EMPTY_CHARCTER;
		bunrui2				= GS.EMPTY_CHARCTER;
		bunrui2_nm_ja		= GS.EMPTY_CHARCTER;
		bunrui2_nm_en		= GS.EMPTY_CHARCTER;
		system_kbn	       	= GS.EMPTY_CHARCTER;
		system_kbn_nm	   	= GS.EMPTY_CHARCTER;
		standard_time		= GS.EMPTY_CHARCTER;
		tyusyutu_taisyo_flg	= GS.EMPTY_CHARCTER;
		standard_time_nm	= GS.EMPTY_CHARCTER;
		tyusyutu_taisyo_nm	= GS.EMPTY_CHARCTER;
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
	//会社コード
	public String getKaisya_cd() {
		return kaisya_cd;
	}
	public void setKaisya_cd(String kaisya_cd) {
		this.kaisya_cd = kaisya_cd;
	}
	//分類１
	public String getBunrui1() {
		return bunrui1;
	}
	public void setBunrui1(String bunrui1) {
		this.bunrui1 = bunrui1;
	}
	//分類２	
	public String getBunrui2() {
		return bunrui2;
	}
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	//分類２名称（日本語）
	public String getBunrui2_nm_ja() {
		return bunrui2_nm_ja;
	}
	public void setBunrui2_nm_ja(String bunrui2_nm_ja) {
		this.bunrui2_nm_ja = bunrui2_nm_ja;
	}
	//分類２名称（英語）
	public String getBunrui2_nm_en() {
		return bunrui2_nm_en;
	}
	public void setBunrui2_nm_en(String bunrui2_nm_en) {
		this.bunrui2_nm_en = bunrui2_nm_en;
	}
	//システム区分
	public String getSystem_kbn() {
		return system_kbn;
	}
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
	//システム区分名称
	public String getSystem_kbn_nm() {
		return system_kbn_nm;
	}
	public void setSystem_kbn_nm(String system_kbn_nm) {
		this.system_kbn_nm = system_kbn_nm;
	}
	//標準時刻
	public String getStandard_time() {
		return standard_time;
	}
	public void setStandard_time(String standard_time) {
		this.standard_time = standard_time;
	}
	//抽出対象フラグ
	public String getTyusyutu_taisyo_flg() {
		return tyusyutu_taisyo_flg;
	}
	public void setTyusyutu_taisyo_flg(String tyusyutu_taisyo_flg) {
		this.tyusyutu_taisyo_flg = tyusyutu_taisyo_flg;
	}
	//標準時刻(名称)
	public String getStandard_time_nm() {
		return standard_time_nm;
	}
	public void setStandard_time_nm(String standard_time_nm) {
		this.standard_time_nm = standard_time_nm;
	}
	//抽出対象(名称)
	public String getTyusyutu_taisyo_nm() {
		return tyusyutu_taisyo_nm;
	}
	public void setTyusyutu_taisyo_nm(String tyusyutu_taisyo_nm) {
		this.tyusyutu_taisyo_nm = tyusyutu_taisyo_nm;
	}
}
