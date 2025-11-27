/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.form;

import config.adapter.struts.action.ActionForm;

/**
 * 取引先区分判定タブFormクラス
 */
public class TorihikisakiKubunHanteiSyokaiForm extends ActionForm {

	/**
	 * 滞留区分
	 */
	private String tairyu_kbn;
	/**
	 * 滞留区分コード
	 */
	private String tairyu_kbn_cd;
	/**
	 * 滞留区分名称
	 */
	private String tairyu_kbn_nm;
	/**
	 * 債権区分
	 */
	private String saiken_kbn;
	/**
	 * 債権区分名称
	 */
	private String saiken_kbn_nm;
	/**
	 * 正常先チェック
	 */
	private String seijo_chk;
	/**
	 * 要注意先チェック
	 */
	private String yochui_chk;
	/**
	 * 実質債務超過チェック
	 */
	private String tyoka_chk;
	/**
	 * 弁済条件緩和チェック
	 */
	private String kanwa_chk;
	/**
	 * 1年以上延滞チェック
	 */
	private String entai_chk;
	/**
	 * 破産法適用チェック
	 */
	private String hasanho_chk;
	/**
	 * 会社法適用チェック
	 */
	private String kaishaho_chk;
	/**
	 * 会社更生法適用チェック
	 */
	private String koseho_chk;
	/**
	 * 民事再生法適用チェック
	 */
	private String saiseho_chk;
	/**
	 * 取引停止処分チェック
	 */
	private String shobun_chk;
	/**
	 * その他チェック
	 */
	private String sonota_chk;
	/**
	 * 信用格付情報
	 */
	private String ktk;
	/**
	 * 親会社信用格付情報
	 */
	private String oya_ktk;
	/**
	 * 取引先名（漢字）
	 */
	private String business_nm_kj;
	/**
	 * 取引先名（英字）
	 */
	private String business_nm;
	/**
	 * 親会社一体判断
	 */
	private String oya_ittai_flg;
	/**
	 * 親会社独立判断
	 */
	private String oya_dokuritu_flg;
	/**
	 * 取引先区分判定根拠
	 */
	private String comment_val_20;
	/**
	 * 債券区分判定根拠
	 */
	private String comment_val_30;
	/**
	 * 債券区分判定発生経緯
	 */
	private String comment_val_40;
	/**
	 * 取引先判定区分判断
	 */
	private String hantei_no;
	/**
	 * 取引先判定名称
	 */
	private String tori_kbn_nm;
	/**
	 * 取引先区分詳細名称
	 */
	private String tori_kbn_nm_detail;
	/**
	 * 正常先名称
	 */
	private String seijo_nm;
	/**
	 * 要注意先名称
	 */
	private String yochui_nm;
	/**
	 * 実質債務超過名称
	 */
	private String tyoka_nm;
	/**
	 * 弁済条件緩和名称
	 */
	private String kanwa_nm;
	/**
	 * 1年以上延滞名称
	 */
	private String entai_nm;
	/**
	 * 破産法適用名称
	 */
	private String hasanho_nm;
	/**
	 * 会社法適用名称
	 */
	private String kaishaho_nm;
	/**
	 * 会社更生法適用名称
	 */
	private String koseho_nm;
	/**
	 * 民事再生法適用名称
	 */
	private String saiseho_nm;
	/**
	 * 取引停止処分名称
	 */
	private String shobun_nm;
	/**
	 * その他名称
	 */
	private String sonota_nm;
	/**
	 * 処理回数制御
	 */
	private int initmode;
	/** 親Duns No */
	private String oya_duns_no;
	
	// 障害票：551　チェックイン日：2008/5/31　対応者：SJA中島　概要：案件のステータスを追加
	private String status;
	private String phase;
	

	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	
	public String getOya_duns_no() {
		return oya_duns_no;
	}
	public void setOya_duns_no(String oya_duns_no) {
		this.oya_duns_no = oya_duns_no;
	}
	/**
	 * @return tairyu_kbn_cd を戻します。
	 */
	public String getTairyu_kbn_cd() {
		return tairyu_kbn_cd;
	}
	/**
	 * @param tairyu_kbn_cd tairyu_kbn_cd を設定。
	 */
	public void setTairyu_kbn_cd(String tairyu_kbn_cd) {
		this.tairyu_kbn_cd = tairyu_kbn_cd;
	}
	/**
	 * @return entai_nm を戻します。
	 */
	public String getEntai_nm() {
		return entai_nm;
	}
	/**
	 * @param entai_nm entai_nm を設定。
	 */
	public void setEntai_nm(String entai_nm) {
		this.entai_nm = entai_nm;
	}
	/**
	 * @return hasanho_nm を戻します。
	 */
	public String getHasanho_nm() {
		return hasanho_nm;
	}
	/**
	 * @param hasanho_nm hasanho_nm を設定。
	 */
	public void setHasanho_nm(String hasanho_nm) {
		this.hasanho_nm = hasanho_nm;
	}
	/**
	 * @return kaishaho_nm を戻します。
	 */
	public String getKaishaho_nm() {
		return kaishaho_nm;
	}
	/**
	 * @param kaishaho_nm kaishaho_nm を設定。
	 */
	public void setKaishaho_nm(String kaishaho_nm) {
		this.kaishaho_nm = kaishaho_nm;
	}
	/**
	 * @return kanwa_nm を戻します。
	 */
	public String getKanwa_nm() {
		return kanwa_nm;
	}
	/**
	 * @param kanwa_nm kanwa_nm を設定。
	 */
	public void setKanwa_nm(String kanwa_nm) {
		this.kanwa_nm = kanwa_nm;
	}
	/**
	 * @return koseho_nm を戻します。
	 */
	public String getKoseho_nm() {
		return koseho_nm;
	}
	/**
	 * @param koseho_nm koseho_nm を設定。
	 */
	public void setKoseho_nm(String koseho_nm) {
		this.koseho_nm = koseho_nm;
	}
	/**
	 * @return saiseho_nm を戻します。
	 */
	public String getSaiseho_nm() {
		return saiseho_nm;
	}
	/**
	 * @param saiseho_nm saiseho_nm を設定。
	 */
	public void setSaiseho_nm(String saiseho_nm) {
		this.saiseho_nm = saiseho_nm;
	}
	/**
	 * @return seijo_nm を戻します。
	 */
	public String getSeijo_nm() {
		return seijo_nm;
	}
	/**
	 * @param seijo_nm seijo_nm を設定。
	 */
	public void setSeijo_nm(String seijo_nm) {
		this.seijo_nm = seijo_nm;
	}
	/**
	 * @return shobun_nm を戻します。
	 */
	public String getShobun_nm() {
		return shobun_nm;
	}
	/**
	 * @param shobun_nm shobun_nm を設定。
	 */
	public void setShobun_nm(String shobun_nm) {
		this.shobun_nm = shobun_nm;
	}
	/**
	 * @return sonota_nm を戻します。
	 */
	public String getSonota_nm() {
		return sonota_nm;
	}
	/**
	 * @param sonota_nm sonota_nm を設定。
	 */
	public void setSonota_nm(String sonota_nm) {
		this.sonota_nm = sonota_nm;
	}
	/**
	 * @return tyoka_nm を戻します。
	 */
	public String getTyoka_nm() {
		return tyoka_nm;
	}
	/**
	 * @param tyoka_nm tyoka_nm を設定。
	 */
	public void setTyoka_nm(String tyoka_nm) {
		this.tyoka_nm = tyoka_nm;
	}
	/**
	 * @return yochui_nm を戻します。
	 */
	public String getYochui_nm() {
		return yochui_nm;
	}
	/**
	 * @param yochui_nm yochui_nm を設定。
	 */
	public void setYochui_nm(String yochui_nm) {
		this.yochui_nm = yochui_nm;
	}
	/**
	 * @return tori_kbn_nm_detail を戻します。
	 */
	public String getTori_kbn_nm_detail() {
		return tori_kbn_nm_detail;
	}
	/**
	 * @param tori_kbn_nm_detail tori_kbn_nm_detail を設定。
	 */
	public void setTori_kbn_nm_detail(String tori_kbn_nm_detail) {
		this.tori_kbn_nm_detail = tori_kbn_nm_detail;
	}
	/**
	 * @return saiken_kbn_nm を戻します。
	 */
	public String getSaiken_kbn_nm() {
		return saiken_kbn_nm;
	}
	/**
	 * @param saiken_kbn_nm saiken_kbn_nm を設定。
	 */
	public void setSaiken_kbn_nm(String saiken_kbn_nm) {
		this.saiken_kbn_nm = saiken_kbn_nm;
	}
	/**
	 * @return tori_kbn_nm を戻します。
	 */
	public String getTori_kbn_nm() {
		return tori_kbn_nm;
	}
	/**
	 * @param tori_kbn_nm tori_kbn_nm を設定。
	 */
	public void setTori_kbn_nm(String tori_kbn_nm) {
		this.tori_kbn_nm = tori_kbn_nm;
	}
	/**
	 * @return hantei_no を戻します。
	 */
	public String getHantei_no() {
		return hantei_no;
	}
	/**
	 * @param hantei_no hantei_no を設定。
	 */
	public void setHantei_no(String hantei_no) {
		this.hantei_no = hantei_no;
	}
	/**
	 * @return business_nm を戻します。
	 */
	public String getBusiness_nm() {
		return business_nm;
	}
	/**
	 * @param business_nm business_nm を設定。
	 */
	public void setBusiness_nm(String business_nm) {
		this.business_nm = business_nm;
	}
	/**
	 * @return business_nm_kj を戻します。
	 */
	public String getBusiness_nm_kj() {
		return business_nm_kj;
	}
	/**
	 * @param business_nm_kj business_nm_kj を設定。
	 */
	public void setBusiness_nm_kj(String business_nm_kj) {
		this.business_nm_kj = business_nm_kj;
	}
	/**
	 * @return comment_val_20 を戻します。
	 */
	public String getComment_val_20() {
		return comment_val_20;
	}
	/**
	 * @param comment_val_20 comment_val_20 を設定。
	 */
	public void setComment_val_20(String comment_val_20) {
		this.comment_val_20 = comment_val_20;
	}
	/**
	 * @return comment_val_30 を戻します。
	 */
	public String getComment_val_30() {
		return comment_val_30;
	}
	/**
	 * @param comment_val_30 comment_val_30 を設定。
	 */
	public void setComment_val_30(String comment_val_30) {
		this.comment_val_30 = comment_val_30;
	}
	/**
	 * @return comment_val_40 を戻します。
	 */
	public String getComment_val_40() {
		return comment_val_40;
	}
	/**
	 * @param comment_val_40 comment_val_40 を設定。
	 */
	public void setComment_val_40(String comment_val_40) {
		this.comment_val_40 = comment_val_40;
	}
	/**
	 * @return entai_chk を戻します。
	 */
	public String getEntai_chk() {
		return entai_chk;
	}
	/**
	 * @param entai_chk entai_chk を設定。
	 */
	public void setEntai_chk(String entai_chk) {
		this.entai_chk = entai_chk;
	}
	/**
	 * @return hasanho_chk を戻します。
	 */
	public String getHasanho_chk() {
		return hasanho_chk;
	}
	/**
	 * @param hasanho_chk hasanho_chk を設定。
	 */
	public void setHasanho_chk(String hasanho_chk) {
		this.hasanho_chk = hasanho_chk;
	}
	/**
	 * @return kaishaho_chk を戻します。
	 */
	public String getKaishaho_chk() {
		return kaishaho_chk;
	}
	/**
	 * @param kaishaho_chk kaishaho_chk を設定。
	 */
	public void setKaishaho_chk(String kaishaho_chk) {
		this.kaishaho_chk = kaishaho_chk;
	}
	/**
	 * @return kanwa_chk を戻します。
	 */
	public String getKanwa_chk() {
		return kanwa_chk;
	}
	/**
	 * @param kanwa_chk kanwa_chk を設定。
	 */
	public void setKanwa_chk(String kanwa_chk) {
		this.kanwa_chk = kanwa_chk;
	}
	/**
	 * @return koseho_chk を戻します。
	 */
	public String getKoseho_chk() {
		return koseho_chk;
	}
	/**
	 * @param koseho_chk koseho_chk を設定。
	 */
	public void setKoseho_chk(String koseho_chk) {
		this.koseho_chk = koseho_chk;
	}
	/**
	 * @return ktk を戻します。
	 */
	public String getKtk() {
		return ktk;
	}
	/**
	 * @param ktk ktk を設定。
	 */
	public void setKtk(String ktk) {
		this.ktk = ktk;
	}
	/**
	 * @return oya_dokuritu_flg を戻します。
	 */
	public String getOya_dokuritu_flg() {
		return oya_dokuritu_flg;
	}
	/**
	 * @param oya_dokuritu_flg oya_dokuritu_flg を設定。
	 */
	public void setOya_dokuritu_flg(String oya_dokuritu_flg) {
		this.oya_dokuritu_flg = oya_dokuritu_flg;
	}
	/**
	 * @return oya_ittai_flg を戻します。
	 */
	public String getOya_ittai_flg() {
		return oya_ittai_flg;
	}
	/**
	 * @param oya_ittai_flg oya_ittai_flg を設定。
	 */
	public void setOya_ittai_flg(String oya_ittai_flg) {
		this.oya_ittai_flg = oya_ittai_flg;
	}
	/**
	 * @return oya_ktk を戻します。
	 */
	public String getOya_ktk() {
		return oya_ktk;
	}
	/**
	 * @param oya_ktk oya_ktk を設定。
	 */
	public void setOya_ktk(String oya_ktk) {
		this.oya_ktk = oya_ktk;
	}
	/**
	 * @return saiken_kbn を戻します。
	 */
	public String getSaiken_kbn() {
		return saiken_kbn;
	}
	/**
	 * @param saiken_kbn saiken_kbn を設定。
	 */
	public void setSaiken_kbn(String saiken_kbn) {
		this.saiken_kbn = saiken_kbn;
	}

	/**
	 * @return saiseho_chk を戻します。
	 */
	public String getSaiseho_chk() {
		return saiseho_chk;
	}
	/**
	 * @param saiseho_chk saiseho_chk を設定。
	 */
	public void setSaiseho_chk(String saiseho_chk) {
		this.saiseho_chk = saiseho_chk;
	}
	/**
	 * @return seijo_chk を戻します。
	 */
	public String getSeijo_chk() {
		return seijo_chk;
	}
	/**
	 * @param seijo_chk seijo_chk を設定。
	 */
	public void setSeijo_chk(String seijo_chk) {
		this.seijo_chk = seijo_chk;
	}
	/**
	 * @return shobun_chk を戻します。
	 */
	public String getShobun_chk() {
		return shobun_chk;
	}
	/**
	 * @param shobun_chk shobun_chk を設定。
	 */
	public void setShobun_chk(String shobun_chk) {
		this.shobun_chk = shobun_chk;
	}
	/**
	 * @return sonota_chk を戻します。
	 */
	public String getSonota_chk() {
		return sonota_chk;
	}
	/**
	 * @param sonota_chk sonota_chk を設定。
	 */
	public void setSonota_chk(String sonota_chk) {
		this.sonota_chk = sonota_chk;
	}
	/**
	 * @return tairyu_kbn を戻します。
	 */
	public String getTairyu_kbn() {
		return tairyu_kbn;
	}
	/**
	 * @param tairyu_kbn tairyu_kbn を設定。
	 */
	public void setTairyu_kbn(String tairyu_kbn) {
		this.tairyu_kbn = tairyu_kbn;
	}
	/**
	 * @return tairyu_kbn_nm を戻します。
	 */
	public String getTairyu_kbn_nm() {
		return tairyu_kbn_nm;
	}
	/**
	 * @param tairyu_kbn_nm tairyu_kbn_nm を設定。
	 */
	public void setTairyu_kbn_nm(String tairyu_kbn_nm) {
		this.tairyu_kbn_nm = tairyu_kbn_nm;
	}
	/**
	 * @return tyoka_chk を戻します。
	 */
	public String getTyoka_chk() {
		return tyoka_chk;
	}
	/**
	 * @param tyoka_chk tyoka_chk を設定。
	 */
	public void setTyoka_chk(String tyoka_chk) {
		this.tyoka_chk = tyoka_chk;
	}
	/**
	 * @return yochui_chk を戻します。
	 */
	public String getYochui_chk() {
		return yochui_chk;
	}
	/**
	 * @param yochui_chk yochui_chk を設定。
	 */
	public void setYochui_chk(String yochui_chk) {
		this.yochui_chk = yochui_chk;
	}
}
