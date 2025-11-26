/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.hikiate.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

/**
 *  OD1104_引当金確認_承認 アクションフォームクラス<br>
 */
public class HikiateSyoninSyosaiForm extends AppPagerActionForm {

	private static final long serialVersionUID = 1L; // serialVersionUID
	private String hanyou1;							// 汎用１
	private String soshiki_nm;							// 組織
	private String kanjo_cd;							// 勘定先CD
	private String kanjo_nm;							// 勘定先名称
	private String tabId;								// 1:引当金確認、2:引当金検証
	
	public HikiateSyoninSyosaiForm() {
		super.gamenId = GS.OD1104;
		this.hanyou1 = GS.EMPTY_CHARCTER;
		this.soshiki_nm = GS.EMPTY_CHARCTER;
		this.kanjo_cd = GS.EMPTY_CHARCTER;
		this.kanjo_nm = GS.EMPTY_CHARCTER;
	}
	
	public String toString() {
		
		return super.gamenId;
	}

	public String getHanyou1() {
		return hanyou1;
	}

	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}

	public String getKanjo_cd() {
		return kanjo_cd;
	}

	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}

	public String getKanjo_nm() {
		return kanjo_nm;
	}

	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}

	public String getSoshiki_nm() {
		return soshiki_nm;
	}

	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}

	public String getTabId() {
		return tabId;
	}

	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
}
