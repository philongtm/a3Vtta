/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.form;

import common.struts.adapter.action.ActionForm;

import java.util.ArrayList;

/**
 * 取引先概要タブFormクラス
 */
public class TorihikisakiGaiyoSyokaiForm extends ActionForm {

	/**
	 * Duns No
	 */
	private String duns_no;
	/**
	 * Sic Code
	 */
	private String sic_code;
	/**
	 * 所在地
	 */
	private String address;

	/**
	 * 処理回数
	 */
	private String cnt;
	/**
	 * 基幹取引先コード
	 */
	private String kikan_tori_cd;
	/**
	 * 統合取引先コード
	 */
	private String togo_tori_cd;
	/**
	 * 業種コード＋業種小分類(漢字)
	 */
	private String sic_sm_cd_nm_kj;
	/**
	 * 業種コード＋業種小分類(英語)
	 */
	private String sic_sm_cd_nm_en;
	/**
	 * 事業内容
	 */
	private String jigyonaiyo;
	/**
	 * 株主名称1
	 */
	private String kabunusi_nm1;
	/**
	 * 株主名称2
	 */
	private String kabunusi_nm2;
	/**
	 * 株主名称3
	 */
	private String kabunusi_nm3;
	/**
	 * 株主名称4
	 */
	private String kabunusi_nm4;
	/**
	 * 株主名称5
	 */
	private String kabunusi_nm5;
	/**
	 * 保有株数1
	 */
	private String kabusu1;
	/**
	 * 保有株数2
	 */
	private String kabusu2;
	/**
	 * 保有株数3
	 */
	private String kabusu3;
	/**
	 * 保有株数4
	 */
	private String kabusu4;
	/**
	 * 保有株数5
	 */
	private String kabusu5;
	/**
	 * 保有比率1
	 */
	private String hiritu1;
	/**
	 * 保有比率2
	 */
	private String hiritu2;
	/**
	 * 保有比率3
	 */
	private String hiritu3;
	/**
	 * 保有比率4
	 */
	private String hiritu4;
	/**
	 * 保有比率5
	 */
	private String hiritu5;
	/**
	 * 抽出事由コード
	 */
	private String jiyuu_cd;
	/**
	 * 抽出事由(漢字)
	 */
	private String jiyuu_nm_kj;
	/**
	 * 抽出事由(英字)
	 */
	private String jiyuu_nm_en;
	/**
	 * 通貨コード
	 */
	private String tsuka_cd;
	/**
	 * 財務概要(過去3ヵ年)
	 */
	private ArrayList zaimu_gaiyo = new ArrayList(3);
	/**
	 * 表示単位
	 */
	private String hyouji_tani;
	/**
	 * 外部格付機関
	 */
	private String ktk_kikan;
	/**
	 * 外部格付
	 */
	private String gaibu_ktk;
	/**
	 * FSS
	 */
	private String fss;
	/**
	 * DUNS Rating
	 */
	private String duns_rating;
	/**
	 * 決算概況
	 */
	private String comment_val;
	/**
	 * 案件No
	 */
	private String anken_no;
	/**
	 * 取引先名(漢字)
	 */
	private String business_nm_kj;
	/**
	 * 取引先名(英語)
	 */
	private String business_nm;
	/**
	 * ワールドベース国コード＋国名
	 */
	private String wb_country_cd_nm;
	/** ワールドベース国コード */
	private String wb_country_cd;
	
	/**
	 * システム区分
	 */
	private String system_kbn;
	
	/**
	 * 処理回数制御
	 */
	private int initmode;
	
	/**
	 * 換算通貨コード
	 */
	private String kansan_tuka_cd;
	// No453, 2008/05/28, SJA渡辺, 画面のメソッドと帳票のメソッドを共通化。
	/** 抽出事由コードリスト */
	private ArrayList jiyuuCdList;
	/** 業種小分類（日本語） */
	private String sic_sm_nm_kj;
	
	
	
	public String getWb_country_cd() {
		return wb_country_cd;
	}
	public void setWb_country_cd(String wb_country_cd) {
		this.wb_country_cd = wb_country_cd;
	}
	public String getSic_sm_nm_kj() {
		return sic_sm_nm_kj;
	}
	public void setSic_sm_nm_kj(String sic_sm_nm_kj) {
		this.sic_sm_nm_kj = sic_sm_nm_kj;
	}
	public ArrayList getJiyuuCdList() {
		return jiyuuCdList;
	}
	public void setJiyuuCdList(ArrayList jiyuuCdList) {
		this.jiyuuCdList = jiyuuCdList;
	}
	/**
	 * @return kansan_tuka_cd を戻します。
	 */
	public String getKansan_tuka_cd() {
		return kansan_tuka_cd;
	}
	/**
	 * @param kansan_tuka_cd kansan_tuka_cd を設定。
	 */
	public void setKansan_tuka_cd(String kansan_tuka_cd) {
		this.kansan_tuka_cd = kansan_tuka_cd;
	}
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	
	/**
	 * @return jiyuu_nm_en を戻します。
	 */
	public String getJiyuu_nm_en() {
		return jiyuu_nm_en;
	}
	/**
	 * @param jiyuu_nm_en jiyuu_nm_en を設定。
	 */
	public void setJiyuu_nm_en(String jiyuu_nm_en) {
		this.jiyuu_nm_en = jiyuu_nm_en;
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
	 * @return address_kj を戻します。
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address_kj address_kj を設定。
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return cnt を戻します。
	 */
	public String getCnt() {
		return cnt;
	}
	/**
	 * @param cnt cnt を設定。
	 */
	public void setCnt(String cnt) {
		this.cnt = cnt;
	}
	/**
	 * @return comment_val を戻します。
	 */
	public String getComment_val() {
		return comment_val;
	}
	/**
	 * @param comment_val comment_val を設定。
	 */
	public void setComment_val(String comment_val) {
		this.comment_val = comment_val;
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
	/**
	 * @return duns_rating を戻します。
	 */
	public String getDuns_rating() {
		return duns_rating;
	}
	/**
	 * @param duns_rating duns_rating を設定。
	 */
	public void setDuns_rating(String duns_rating) {
		this.duns_rating = duns_rating;
	}
	/**
	 * @return fss を戻します。
	 */
	public String getFss() {
		return fss;
	}
	/**
	 * @param fss fss を設定。
	 */
	public void setFss(String fss) {
		this.fss = fss;
	}
	/**
	 * @return gaibu_ktk を戻します。
	 */
	public String getGaibu_ktk() {
		return gaibu_ktk;
	}
	/**
	 * @param gaibu_ktk gaibu_ktk を設定。
	 */
	public void setGaibu_ktk(String gaibu_ktk) {
		this.gaibu_ktk = gaibu_ktk;
	}
	/**
	 * @return hiritu1 を戻します。
	 */
	public String getHiritu1() {
		return hiritu1;
	}
	/**
	 * @param hiritu1 hiritu1 を設定。
	 */
	public void setHiritu1(String hiritu1) {
		this.hiritu1 = hiritu1;
	}
	/**
	 * @return hiritu2 を戻します。
	 */
	public String getHiritu2() {
		return hiritu2;
	}
	/**
	 * @param hiritu2 hiritu2 を設定。
	 */
	public void setHiritu2(String hiritu2) {
		this.hiritu2 = hiritu2;
	}
	/**
	 * @return hiritu3 を戻します。
	 */
	public String getHiritu3() {
		return hiritu3;
	}
	/**
	 * @param hiritu3 hiritu3 を設定。
	 */
	public void setHiritu3(String hiritu3) {
		this.hiritu3 = hiritu3;
	}
	/**
	 * @return hiritu4 を戻します。
	 */
	public String getHiritu4() {
		return hiritu4;
	}
	/**
	 * @param hiritu4 hiritu4 を設定。
	 */
	public void setHiritu4(String hiritu4) {
		this.hiritu4 = hiritu4;
	}
	/**
	 * @return hiritu5 を戻します。
	 */
	public String getHiritu5() {
		return hiritu5;
	}
	/**
	 * @param hiritu5 hiritu5 を設定。
	 */
	public void setHiritu5(String hiritu5) {
		this.hiritu5 = hiritu5;
	}
	/**
	 * @return hyouji_tani を戻します。
	 */
	public String getHyouji_tani() {
		return hyouji_tani;
	}
	/**
	 * @param hyouji_tani hyouji_tani を設定。
	 */
	public void setHyouji_tani(String hyouji_tani) {
		this.hyouji_tani = hyouji_tani;
	}
	/**
	 * @return jigyonaiyo を戻します。
	 */
	public String getJigyonaiyo() {
		return jigyonaiyo;
	}
	/**
	 * @param jigyonaiyo jigyonaiyo を設定。
	 */
	public void setJigyonaiyo(String jigyonaiyo) {
		this.jigyonaiyo = jigyonaiyo;
	}
	/**
	 * @return jiyuu_cd を戻します。
	 */
	public String getJiyuu_cd() {
		return jiyuu_cd;
	}
	/**
	 * @param jiyuu_cd jiyuu_cd を設定。
	 */
	public void setJiyuu_cd(String jiyuu_cd) {
		this.jiyuu_cd = jiyuu_cd;
	}
	/**
	 * @return jiyuu_nm を戻します。
	 */
	public String getJiyuu_nm_kj() {
		return jiyuu_nm_kj;
	}
	/**
	 * @param jiyuu_nm jiyuu_nm を設定。
	 */
	public void setJiyuu_nm_kj(String jiyuu_nm) {
		this.jiyuu_nm_kj = jiyuu_nm;
	}
	/**
	 * @return kabunusi_nm1 を戻します。
	 */
	public String getKabunusi_nm1() {
		return kabunusi_nm1;
	}
	/**
	 * @param kabunusi_nm1 kabunusi_nm1 を設定。
	 */
	public void setKabunusi_nm1(String kabunusi_nm1) {
		this.kabunusi_nm1 = kabunusi_nm1;
	}
	/**
	 * @return kabunusi_nm2 を戻します。
	 */
	public String getKabunusi_nm2() {
		return kabunusi_nm2;
	}
	/**
	 * @param kabunusi_nm2 kabunusi_nm2 を設定。
	 */
	public void setKabunusi_nm2(String kabunusi_nm2) {
		this.kabunusi_nm2 = kabunusi_nm2;
	}
	/**
	 * @return kabunusi_nm3 を戻します。
	 */
	public String getKabunusi_nm3() {
		return kabunusi_nm3;
	}
	/**
	 * @param kabunusi_nm3 kabunusi_nm3 を設定。
	 */
	public void setKabunusi_nm3(String kabunusi_nm3) {
		this.kabunusi_nm3 = kabunusi_nm3;
	}
	/**
	 * @return kabunusi_nm4 を戻します。
	 */
	public String getKabunusi_nm4() {
		return kabunusi_nm4;
	}
	/**
	 * @param kabunusi_nm4 kabunusi_nm4 を設定。
	 */
	public void setKabunusi_nm4(String kabunusi_nm4) {
		this.kabunusi_nm4 = kabunusi_nm4;
	}
	/**
	 * @return kabunusi_nm5 を戻します。
	 */
	public String getKabunusi_nm5() {
		return kabunusi_nm5;
	}
	/**
	 * @param kabunusi_nm5 kabunusi_nm5 を設定。
	 */
	public void setKabunusi_nm5(String kabunusi_nm5) {
		this.kabunusi_nm5 = kabunusi_nm5;
	}
	/**
	 * @return kabusu1 を戻します。
	 */
	public String getKabusu1() {
		return kabusu1;
	}
	/**
	 * @param kabusu1 kabusu1 を設定。
	 */
	public void setKabusu1(String kabusu1) {
		this.kabusu1 = kabusu1;
	}
	/**
	 * @return kabusu2 を戻します。
	 */
	public String getKabusu2() {
		return kabusu2;
	}
	/**
	 * @param kabusu2 kabusu2 を設定。
	 */
	public void setKabusu2(String kabusu2) {
		this.kabusu2 = kabusu2;
	}
	/**
	 * @return kabusu3 を戻します。
	 */
	public String getKabusu3() {
		return kabusu3;
	}
	/**
	 * @param kabusu3 kabusu3 を設定。
	 */
	public void setKabusu3(String kabusu3) {
		this.kabusu3 = kabusu3;
	}
	/**
	 * @return kabusu4 を戻します。
	 */
	public String getKabusu4() {
		return kabusu4;
	}
	/**
	 * @param kabusu4 kabusu4 を設定。
	 */
	public void setKabusu4(String kabusu4) {
		this.kabusu4 = kabusu4;
	}
	/**
	 * @return kabusu5 を戻します。
	 */
	public String getKabusu5() {
		return kabusu5;
	}
	/**
	 * @param kabusu5 kabusu5 を設定。
	 */
	public void setKabusu5(String kabusu5) {
		this.kabusu5 = kabusu5;
	}
	/**
	 * @return kikan_tori_cd を戻します。
	 */
	public String getKikan_tori_cd() {
		return kikan_tori_cd;
	}
	/**
	 * @param kikan_tori_cd kikan_tori_cd を設定。
	 */
	public void setKikan_tori_cd(String kikan_tori_cd) {
		this.kikan_tori_cd = kikan_tori_cd;
	}
	/**
	 * @return ktk_kikan を戻します。
	 */
	public String getKtk_kikan() {
		return ktk_kikan;
	}
	/**
	 * @param ktk_kikan ktk_kikan を設定。
	 */
	public void setKtk_kikan(String ktk_kikan) {
		this.ktk_kikan = ktk_kikan;
	}
	/**
	 * @return sic_code を戻します。
	 */
	public String getSic_code() {
		return sic_code;
	}
	/**
	 * @param sic_code sic_code を設定。
	 */
	public void setSic_code(String sic_code) {
		this.sic_code = sic_code;
	}
	/**
	 * @return sic_sm_nm_en を戻します。
	 */
	public String getSic_sm_cd_nm_en() {
		return sic_sm_cd_nm_en;
	}
	/**
	 * @param sic_sm_nm_en sic_sm_nm_en を設定。
	 */
	public void setSic_sm_cd_nm_en(String sic_sm_nm_en) {
		this.sic_sm_cd_nm_en = sic_sm_nm_en;
	}
	/**
	 * @return sic_sm_nm_kj を戻します。
	 */
	public String getSic_sm_cd_nm_kj() {
		return sic_sm_cd_nm_kj;
	}
	/**
	 * @param sic_sm_nm_kj sic_sm_nm_kj を設定。
	 */
	public void setSic_sm_cd_nm_kj(String sic_sm_nm_kj) {
		this.sic_sm_cd_nm_kj = sic_sm_nm_kj;
	}
	/**
	 * @return togo_tori_cd を戻します。
	 */
	public String getTogo_tori_cd() {
		return togo_tori_cd;
	}
	/**
	 * @param togo_tori_cd togo_tori_cd を設定。
	 */
	public void setTogo_tori_cd(String togo_tori_cd) {
		this.togo_tori_cd = togo_tori_cd;
	}
	/**
	 * @return tsuka_cd を戻します。
	 */
	public String getTsuka_cd() {
		return tsuka_cd;
	}
	/**
	 * @param tsuka_cd tsuka_cd を設定。
	 */
	public void setTsuka_cd(String tsuka_cd) {
		this.tsuka_cd = tsuka_cd;
	}
	/**
	 * @return zaimu_gaiyo を戻します。
	 */
	public ArrayList getZaimu_gaiyo() {
		return zaimu_gaiyo;
	}
	/**
	 * @param zaimu_gaiyo zaimu_gaiyo を設定。
	 */
	public void setZaimu_gaiyo(ArrayList zaimu_gaiyo) {
		this.zaimu_gaiyo = zaimu_gaiyo;
	}
	/**
	 * @return anken_no を戻します。
	 */
	public String getAnken_no() {
		return anken_no;
	}
	/**
	 * @param anken_no anken_no を設定。
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
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
	 * @return wb_country_cd を戻します。
	 */
	public String getWb_country_cd_nm() {
		return wb_country_cd_nm;
	}
	/**
	 * @param wb_country_cd wb_country_cd を設定。
	 */
	public void setWb_country_cd_nm(String wb_country_cd_nm) {
		this.wb_country_cd_nm = wb_country_cd_nm;
	}
}
	
