/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/15		SSC				1.5次版機能組込
******************************************************************************/
package app.common.form;

import common.struts.AppPagerActionForm;

public class TenpuSyokaiForm extends AppPagerActionForm {
	
	/** 案件No */
	private String ankenNo;
	/** 取引先CD */
	private String toriCd;
	/** フェーズ */
	private String phase;
	/** 文書No */
	private String bunsyo_no;
	
    // No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するプロパティ追加
	// 前画面からの取得情報(査定会社コード)
	private String sateikaisya_cd;
	
	private int id;
	/**
	 * 処理回数制御
	 */
	private int initmode;
	
	public String toString() {
		return super.gamenId;
	}

	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
	/**
	 * 滞留明細Form
	 */
	private TairyuMeisaiForm tairyuMeisaiForm;
	
	/**
	 * @return tairyuMeisaiForm を戻します。
	 */
	public TairyuMeisaiForm getTairyuMeisaiForm() {
		return tairyuMeisaiForm;
	}
	/**
	 * @param tairyuMeisaiForm tairyuMeisaiForm を設定。
	 */
	public void setTairyuMeisaiForm(TairyuMeisaiForm tairyuMeisaiForm) {
		this.tairyuMeisaiForm = tairyuMeisaiForm;
	}
	
    // 変数初期化
    public TenpuSyokaiForm() {
    	// TODO 遷移元からデータが受け取れるようになったらデフォルト値を変更
    	/*ankenNo = "0711000000";
    	toriCd = "1769900     ";
    	phase = "70";*/
    	ankenNo = null;
    	toriCd = null;
    	phase = null;
    	bunsyo_no = null;
    }
	public String getBunsyo_no() {
		return bunsyo_no;
	}
	public void setBunsyo_no(String bunsyo_no) {
		this.bunsyo_no = bunsyo_no;
	}    
	public String getAnkenNo() {
		return ankenNo;
	}
	public void setAnkenNo(String ankenNo) {
		this.ankenNo = ankenNo;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}	
	public String getToriCd() {
		return toriCd;
	}
	public void setToriCd(String toriCd) {
		this.toriCd = toriCd;
	}
}
