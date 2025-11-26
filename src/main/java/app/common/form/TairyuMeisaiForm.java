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
 * OZ6101_滞留債権明細照会タブ  アクションフォームクラス
 * 
 */
public class TairyuMeisaiForm extends AppPagerActionForm {

    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private String tuuka_cd;				// 通貨
    private String hanyo1_title;			// 汎用1
    private String komoku1;				// 項目1
    private String komoku3;				// 項目3
    private int id;						// リンククリックされた勘定先の明細.id
    
    // 変数初期化
    public TairyuMeisaiForm() {
    	super.gamenId = GS.OZ6101;
    	
        this.ar_show = null;
        this.tuuka_cd = null;
        this.hanyo1_title = null;
        this.komoku1 = null;
        this.komoku3 = null;
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
	//通貨
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	//汎用1
	public String getHanyo1_title() {
		return hanyo1_title;
	}
	public void setHanyo1_title(String hanyo1_title) {
		this.hanyo1_title = hanyo1_title;
	}
	//項目1
	public String getKomoku1() {
		return komoku1;
	}
	public void setKomoku1(String komoku1) {
		this.komoku1 = komoku1;
	}
	//項目3
	public String getKomoku3() {
		return komoku3;
	}
	public void setKomoku3(String komoku3) {
		this.komoku3 = komoku3;
	}
	//リンククリックされた勘定先の明細.id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}