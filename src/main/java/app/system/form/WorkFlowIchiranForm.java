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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *  OS7104_業務フローパターンメンテナンス_一覧 アクションフォームクラス
 */
public class WorkFlowIchiranForm extends AppPagerActionForm {
	private static final long serialVersionUID = 1L; 		// serialVersionUID
	private String systemkbn;									// システム区分
	private String systemkbn_nm;								// システム区分名称
	private LinkedHashMap ar_systemkbn;						// システム区分【リスト】
	private String hanyou1;									// 汎用１
	private LinkedHashMap ar_hanyou1;							// 汎用１【リスト】
	private String workflow_nm;								// 業務フローパターン名称
	private LinkedHashMap ar_show;                     		// 表示件数セレクトボックス用配列
	private int id;											// リンククリックされた業務フロー情報Beanのid
	private String gamen_flg;									// 画面フラグ

	//	 変数初期化
    public WorkFlowIchiranForm() {
    	super.gamenId = GS.OS7104;
        this.systemkbn = GS.EMPTY_CHARCTER;
        this.systemkbn_nm = GS.EMPTY_CHARCTER;
        this.ar_systemkbn = null;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.ar_hanyou1 = null;
        this.workflow_nm = GS.EMPTY_CHARCTER;
        this.ar_show = null;
        this.id = 0;
        this.gamen_flg = "1";
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    }
	
	/**
	 * @return 画面IDを戻します。
	 */
	public String toString() {
		return super.gamenId;
	}
	
	// システム区分
	public String getSystemkbn() {
		return systemkbn;
	}
	public void setSystemkbn(String systemkbn) {
		this.systemkbn = systemkbn;
		
	}
	
	// システム区分【リスト】
	public LinkedHashMap getAr_systemkbn() {
		return ar_systemkbn;
	}
	public void setAr_systemkbn(LinkedHashMap ar_systemkbn) {
		this.ar_systemkbn = ar_systemkbn;
	}
	
	// 汎用１【リスト】
	public LinkedHashMap getAr_hanyou1() {
		return ar_hanyou1;
	}
	public void setAr_hanyou1(LinkedHashMap ar_hanyou1) {
		this.ar_hanyou1 = ar_hanyou1;
	}

	// 汎用１
	public String getHanyou1() {
		return hanyou1;
	}
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	
	// 業務フローパターン名称
	public String getWorkflow_nm() {
		return workflow_nm;
	}
	public void setWorkflow_nm(String workflow_nm) {
		this.workflow_nm = workflow_nm;
	}
	
	// 表示件数セレクトボックス用配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	
	// リンククリックされた業務フロー情報Beanのid
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	// 画面フラグ
	public String getGamen_flg() {
		return gamen_flg;
	}
	public void setGamen_flg(String gamen_flg) {
		this.gamen_flg = gamen_flg;
	}

	public String getSystemkbn_nm() {
		return systemkbn_nm;
	}

	public void setSystemkbn_nm(String systemkbn_nm) {
		this.systemkbn_nm = systemkbn_nm;
	}
}
