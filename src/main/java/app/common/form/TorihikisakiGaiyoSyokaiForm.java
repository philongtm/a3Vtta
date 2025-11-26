/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.form;

import org.apache.struts.action.ActionForm;

import java.util.List;

/**
 * OZ6104_引当金判定照会タブ  アクションフォームクラス
 */
public class TorihikisakiGaiyoSyokaiForm extends ActionForm {

	/** 
	 * 業種小分類
	 */
	private String sic_sm_nm;
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
	 * 保有率1
	 */
	private String hiritu1;
	/**
	 * 保有率2
	 */
	private String hiritu2;
	/**
	 * 保有率3
	 */
	private String hiritu3;
	/**
	 * 保有率4
	 */
	private String hiritu4;
	/**
	 * 保有率5
	 */
	private String hiritu5;
	/**
	 * 通貨コード
	 */
	private String tsuka_cd;
	/**
	 * 表示単位
	 */
	private String hyouji_tani;
	/**
	 * 決算概況
	 */
	private String comment_val;
	/**
	 *  財務情報【リスト】
	 */
	private List ar_zaimu;
	/**
	 *  汎用項目ラベル１
	 */
	private String hanyou1;
	/**
	 *  汎用項目ラベル２
	 */
	private String hanyou2;
	/**
	 *  汎用項目ラベル３
	 */
	private String hanyou3;
	
	/**
	 * @return sic_sm_nm を戻します。
	 */
	public String getSic_sm_nm() {
		return sic_sm_nm;
	}
	/**
	 * @param sic_sm_nm sic_sm_nm を設定。
	 */
	public void setSic_sm_nm(String sic_sm_nm) {
		this.sic_sm_nm = sic_sm_nm;
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
	 * @return ar_zaimu を戻します。
	 */
	public List getAr_zaimu() {
		return ar_zaimu;
	}
	/**
	 * @param ar_zaimu ar_zaimu を設定。
	 */
	public void setAr_zaimu(List ar_zaimu) {
		this.ar_zaimu = ar_zaimu;
	}
	/**
	 * @return hanyou1 を戻します。
	 */
	public String getHanyou1() {
		return hanyou1;
	}
	/**
	 * @param hanyou1 hanyou1 を設定。
	 */
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	/**
	 * @return hanyou2 を戻します。
	 */
	public String getHanyou2() {
		return hanyou2;
	}
	/**
	 * @param hanyou2 hanyou2 を設定。
	 */
	public void setHanyou2(String hanyou2) {
		this.hanyou2 = hanyou2;
	}
	/**
	 * @return hanyou3 を戻します。
	 */
	public String getHanyou3() {
		return hanyou3;
	}
	/**
	 * @param hanyou3 hanyou3 を設定。
	 */
	public void setHanyou3(String hanyou3) {
		this.hanyou3 = hanyou3;
	}
}
	
