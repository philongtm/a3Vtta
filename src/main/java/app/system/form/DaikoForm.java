/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.List;

/**
 * OS7101_代行設定 アクションフォームクラス <br>
 */
public class DaikoForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private List ar_hidaiko_tanto; 					// 被代行者.担当者一覧【配列】
	private String selHidaikosha; 						// 画面で選択された　被代行者　の値を設定。
	private String txtHidaikosha; 						// 選択された被代行者Mail
	private String selectedHidaikoshaId;				// 選択された被代行者ID
	private List ar_hidaiko_system_kbn; 				// 被代行者システム区分【リスト】
	private List ar_daiko_tanto;						// 代行者.担当者一覧【配列】
	private String selDaikosha;						// 画面で選択された　代行者　の値を設定。
	private String txtDaikosha;						// 選択された代行者Mail
	private String selectedDaikoshaId;					// 選択された代行者ID
	private List ar_daikosha; 							// 代行者一覧【リスト】
	private List ar_hidaikosha; 						// 被代行者一覧【リスト】
	private String inHidaikosha; 						// 被代行者(入力／検索)
	private String inDaikosha; 						// 代行者(入力／検索)
	private Boolean disabledHidaiko; 					// 被代行者(入力／検索)表示可否
	private int delId; 								// 削除したい代行者のインデックス

    /**
     * 変数初期化 <br>
     */
    public DaikoForm() {
        
        super.gamenId = GS.OS7101;
        
        this.ar_hidaiko_tanto = null;
        this.selHidaikosha = GS.EMPTY_CHARCTER;
        this.txtHidaikosha = GS.EMPTY_CHARCTER;
    	this.ar_hidaiko_system_kbn = null;
    	this.ar_daiko_tanto = null;
    	this.selDaikosha = GS.EMPTY_CHARCTER;
    	this.txtDaikosha = GS.EMPTY_CHARCTER;
    	this.ar_daikosha = null;
    	this.ar_hidaikosha = null;
    	this.inHidaikosha = GS.EMPTY_CHARCTER;
    	this.inDaikosha = GS.EMPTY_CHARCTER;
    	this.disabledHidaiko = Boolean.FALSE;
    	this.delId = 0;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

	/**
	 * @return the ar_daiko_tanto
	 */
	public List getAr_daiko_tanto() {
		return ar_daiko_tanto;
	}

	/**
	 * @param ar_daiko_tanto the ar_daiko_tanto to set
	 */
	public void setAr_daiko_tanto(List ar_daiko_tanto) {
		this.ar_daiko_tanto = ar_daiko_tanto;
	}

	/**
	 * @return the ar_daikosha
	 */
	public List getAr_daikosha() {
		return ar_daikosha;
	}

	/**
	 * @param ar_daikosha the ar_daikosha to set
	 */
	public void setAr_daikosha(List ar_daikosha) {
		this.ar_daikosha = ar_daikosha;
	}

	/**
	 * @return the ar_hidaiko_system_kbn
	 */
	public List getAr_hidaiko_system_kbn() {
		return ar_hidaiko_system_kbn;
	}

	/**
	 * @param ar_hidaiko_system_kbn the ar_hidaiko_system_kbn to set
	 */
	public void setAr_hidaiko_system_kbn(List ar_hidaiko_system_kbn) {
		this.ar_hidaiko_system_kbn = ar_hidaiko_system_kbn;
	}

	/**
	 * @return the ar_hidaiko_tanto
	 */
	public List getAr_hidaiko_tanto() {
		return ar_hidaiko_tanto;
	}

	/**
	 * @param ar_hidaiko_tanto the ar_hidaiko_tanto to set
	 */
	public void setAr_hidaiko_tanto(List ar_hidaiko_tanto) {
		this.ar_hidaiko_tanto = ar_hidaiko_tanto;
	}

	/**
	 * @return the ar_hidaikosha
	 */
	public List getAr_hidaikosha() {
		return ar_hidaikosha;
	}

	/**
	 * @param ar_hidaikosha the ar_hidaikosha to set
	 */
	public void setAr_hidaikosha(List ar_hidaikosha) {
		this.ar_hidaikosha = ar_hidaikosha;
	}

	/**
	 * @return the selDaikosha
	 */
	public String getSelDaikosha() {
		return selDaikosha;
	}

	/**
	 * @param selDaikosha the selDaikosha to set
	 */
	public void setSelDaikosha(String selDaikosha) {
		this.selDaikosha = selDaikosha;
	}

	/**
	 * @return the selHidaikosha
	 */
	public String getSelHidaikosha() {
		return selHidaikosha;
	}

	/**
	 * @param selHidaikosha the selHidaikosha to set
	 */
	public void setSelHidaikosha(String selHidaikosha) {
		this.selHidaikosha = selHidaikosha;
	}

	/**
	 * @return the txtDaikosha
	 */
	public String getTxtDaikosha() {
		return txtDaikosha;
	}

	/**
	 * @param txtDaikosha the txtDaikosha to set
	 */
	public void setTxtDaikosha(String txtDaikosha) {
		this.txtDaikosha = txtDaikosha;
	}

	/**
	 * @return the txtHidaikosha
	 */
	public String getTxtHidaikosha() {
		return txtHidaikosha;
	}

	/**
	 * @param txtHidaikosha the txtHidaikosha to set
	 */
	public void setTxtHidaikosha(String txtHidaikosha) {
		this.txtHidaikosha = txtHidaikosha;
	}

	/**
	 * @return the indaikosha
	 */
	public String getInDaikosha() {
		return inDaikosha;
	}

	/**
	 * @param inDaikosha the inDaikosha to set
	 */
	public void setInDaikosha(String inDaikosha) {
		this.inDaikosha = inDaikosha;
	}

	/**
	 * @return the inHidaikosha
	 */
	public String getInHidaikosha() {
		return inHidaikosha;
	}

	/**
	 * @param inHidaikosha the inHidaikosha to set
	 */
	public void setInHidaikosha(String inHidaikosha) {
		this.inHidaikosha = inHidaikosha;
	}

	/**
	 * @return the disabledHidaiko
	 */
	public Boolean getDisabledHidaiko() {
		return disabledHidaiko;
	}

	/**
	 * @param disabledHidaiko the disabledHidaiko to set
	 */
	public void setDisabledHidaiko(Boolean disabledHidaiko) {
		this.disabledHidaiko = disabledHidaiko;
	}

	/**
	 * @return the delId
	 */
	public int getDelId() {
		return delId;
	}

	/**
	 * @param delId the delId to set
	 */
	public void setDelId(int delId) {
		this.delId = delId;
	}

	/**
	 * @return the selectedDaikoshaId
	 */
	public String getSelectedDaikoshaId() {
		return selectedDaikoshaId;
	}

	/**
	 * @param selectedDaikoshaId the selectedDaikoshaId to set
	 */
	public void setSelectedDaikoshaId(String selectedDaikoshaId) {
		this.selectedDaikoshaId = selectedDaikoshaId;
	}

	/**
	 * @return the selectedHidaikoshaId
	 */
	public String getSelectedHidaikoshaId() {
		return selectedHidaikoshaId;
	}

	/**
	 * @param selectedHidaikoshaId the selectedHidaikoshaId to set
	 */
	public void setSelectedHidaikoshaId(String selectedHidaikoshaId) {
		this.selectedHidaikoshaId = selectedHidaikoshaId;
	}
}