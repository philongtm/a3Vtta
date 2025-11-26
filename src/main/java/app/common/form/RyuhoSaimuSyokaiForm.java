/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OZ6107_債務明細照会タブ  アクションフォームクラス
 * 
 */
public class RyuhoSaimuSyokaiForm extends AppPagerActionForm {

	
    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
	private String saimu_kei;				// 債務総計
	private String ryuhosaimu_kei;			// 留保債務計
	private String tuuka_cd;					// (通貨)
	private int id;						// リンククリックされた勘定先の明細.id
	  
    // 変数初期化
    public RyuhoSaimuSyokaiForm() {
    	
    	super.gamenId = GS.OZ6107;
        this.ar_show = null;
        this.saimu_kei = null;
        this.ryuhosaimu_kei = null;
        this.tuuka_cd = null;
        this.id = 0;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
       
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
    // アクセスメソッド   

	//表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	//債権総計
	public String getSaimu_kei() {
		return saimu_kei;
	}
	public void setSaimu_kei(String saimu_kei) {
		this.saimu_kei = saimu_kei;
	}
	//留保債務総計
	public String getRyuhosaimu_kei() {
		return ryuhosaimu_kei;
	}
	public void setRyuhosaimu_kei(String ryuhosaimu_kei) {
		this.ryuhosaimu_kei = ryuhosaimu_kei;
	}
	//(通貨)
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
		
}