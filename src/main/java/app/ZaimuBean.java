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
 * 財務情報Beanクラス
 */
public class ZaimuBean {

	private String uriagedaka;				//売上高
	private String uriagesorieki;			//売上総利益
	private String hanbaihikanrihi;		//販売管理費
	private String eigyorieki;				//営業利益
	private String hanyou1;				//汎用1
	private String hanyou2;				//汎用2
	private String hanyou3;				//汎用3
	private String tokijunrieki;			//当期純利益
	private String haitokin;				//配当金
	private String genkasyokyakuhi;		//減価償却費
	private String eigyo_cf;				//営業キャッシュフロー
	private String ryudosisan;				//流動資産
	private String koteisisan;				//固定資産
	private String sisangokei;				//資産合計
	private String ryudohusai;				//流動負債
	private String koteihusai;				//固定負債
	private String husaigokei;				//負債合計
	private String sihonkin;				//資本金
	private String naiburyuho;				//内部留保等
	private String jikosihongokei;			//自己資本合計
	private String tantai_renketu;			//単体連結
	private String kessan_ki;				//決算期
	private String tukaCd;					//通貨コード
	private String hyoujiTani;				//表示単位

    // 変数初期化
    public ZaimuBean() {
    	this.uriagedaka					= GS.EMPTY_CHARCTER;
    	this.uriagesorieki				= GS.EMPTY_CHARCTER;
    	this.hanbaihikanrihi			= GS.EMPTY_CHARCTER;
    	this.eigyorieki					= GS.EMPTY_CHARCTER;
    	this.hanyou1					= GS.EMPTY_CHARCTER;
    	this.hanyou2					= GS.EMPTY_CHARCTER;
    	this.hanyou3					= GS.EMPTY_CHARCTER;
    	this.tokijunrieki				= GS.EMPTY_CHARCTER;
    	this.haitokin					= GS.EMPTY_CHARCTER;
    	this.genkasyokyakuhi			= GS.EMPTY_CHARCTER;
    	this.eigyo_cf					= GS.EMPTY_CHARCTER;
    	this.ryudosisan					= GS.EMPTY_CHARCTER;
    	this.koteisisan					= GS.EMPTY_CHARCTER;
    	this.sisangokei					= GS.EMPTY_CHARCTER;
    	this.ryudohusai					= GS.EMPTY_CHARCTER;
    	this.koteihusai					= GS.EMPTY_CHARCTER;
    	this.husaigokei					= GS.EMPTY_CHARCTER;
    	this.sihonkin					= GS.EMPTY_CHARCTER;
    	this.naiburyuho					= GS.EMPTY_CHARCTER;
    	this.jikosihongokei				= GS.EMPTY_CHARCTER;
    	this.tantai_renketu				= GS.EMPTY_CHARCTER;
    	this.kessan_ki					= GS.EMPTY_CHARCTER;
    	this.tukaCd						= GS.EMPTY_CHARCTER;
    	this.hyoujiTani					= GS.EMPTY_CHARCTER;
    }

	//営業キャッシュフロー
	public String getEigyo_cf() {
		return eigyo_cf;
	}
	public void setEigyo_cf(String eigyo_cf) {
		this.eigyo_cf = eigyo_cf;
	}
	//営業利益
	public String getEigyorieki() {
		return eigyorieki;
	}
	public void setEigyorieki(String eigyorieki) {
		this.eigyorieki = eigyorieki;
	}
	//減価償却費
	public String getGenkasyokyakuhi() {
		return genkasyokyakuhi;
	}
	public void setGenkasyokyakuhi(String genkasyokyakuhi) {
		this.genkasyokyakuhi = genkasyokyakuhi;
	}
	//配当金
	public String getHaitokin() {
		return haitokin;
	}
	public void setHaitokin(String haitokin) {
		this.haitokin = haitokin;
	}
	//販売管理費
	public String getHanbaihikanrihi() {
		return hanbaihikanrihi;
	}
	public void setHanbaihikanrihi(String hanbaihikanrihi) {
		this.hanbaihikanrihi = hanbaihikanrihi;
	}
	//汎用1
	public String getHanyou1() {
		return hanyou1;
	}
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	//汎用2
	public String getHanyou2() {
		return hanyou2;
	}
	public void setHanyou2(String hanyou2) {
		this.hanyou2 = hanyou2;
	}
	//汎用3
	public String getHanyou3() {
		return hanyou3;
	}
	public void setHanyou3(String hanyou3) {
		this.hanyou3 = hanyou3;
	}
	//負債合計
	public String getHusaigokei() {
		return husaigokei;
	}
	public void setHusaigokei(String husaigokei) {
		this.husaigokei = husaigokei;
	}
	//自己資本合計
	public String getJikosihongokei() {
		return jikosihongokei;
	}
	public void setJikosihongokei(String jikosihongokei) {
		this.jikosihongokei = jikosihongokei;
	}
	//決算期
	public String getKessan_ki() {
		return kessan_ki;
	}
	public void setKessan_ki(String kessan_ki) {
		this.kessan_ki = kessan_ki;
	}
	//固定負債
	public String getKoteihusai() {
		return koteihusai;
	}
	public void setKoteihusai(String koteihusai) {
		this.koteihusai = koteihusai;
	}
	//固定資産
	public String getKoteisisan() {
		return koteisisan;
	}
	public void setKoteisisan(String koteisisan) {
		this.koteisisan = koteisisan;
	}
	//内部留保等
	public String getNaiburyuho() {
		return naiburyuho;
	}
	public void setNaiburyuho(String naiburyuho) {
		this.naiburyuho = naiburyuho;
	}
	//流動負債
	public String getRyudohusai() {
		return ryudohusai;
	}
	public void setRyudohusai(String ryudohusai) {
		this.ryudohusai = ryudohusai;
	}
	//流動資産
	public String getRyudosisan() {
		return ryudosisan;
	}
	public void setRyudosisan(String ryudosisan) {
		this.ryudosisan = ryudosisan;
	}
	//資本金
	public String getSihonkin() {
		return sihonkin;
	}
	public void setSihonkin(String sihonkin) {
		this.sihonkin = sihonkin;
	}
	//資産合計
	public String getSisangokei() {
		return sisangokei;
	}
	public void setSisangokei(String sisangokei) {
		this.sisangokei = sisangokei;
	}
	//単体連結
	public String getTantai_renketu() {
		return tantai_renketu;
	}
	public void setTantai_renketu(String tantai_renketu) {
		this.tantai_renketu = tantai_renketu;
	}
	//当期純利益
	public String getTokijunrieki() {
		return tokijunrieki;
	}
	public void setTokijunrieki(String tokijunrieki) {
		this.tokijunrieki = tokijunrieki;
	}
	//売上高
	public String getUriagedaka() {
		return uriagedaka;
	}
	public void setUriagedaka(String uriagedaka) {
		this.uriagedaka = uriagedaka;
	}
	//売上総利益
	public String getUriagesorieki() {
		return uriagesorieki;
	}
	public void setUriagesorieki(String uriagesorieki) {
		this.uriagesorieki = uriagesorieki;
	}
	 //通貨コード
	 public String getTukaCd() {
		return tukaCd;
	}
	 public void setTukaCd(String tukaCd) {
		this.tukaCd = tukaCd;
	 }
	 //表示単位
	public String getHyoujiTani() {
		return hyoujiTani;
	}
	public void setHyoujiTani(String hyoujiTani) {
		this.hyoujiTani = hyoujiTani;
	}
}
