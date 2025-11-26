/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OB2101_対象先選定_選定実行 アクションフォームクラス
 * 
 */
public class SenteiForm extends AppPagerActionForm {

    private String tanto;					// 自担当分/汎用２ラジオボタン(1:自担当分、2:汎用２)
    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private String anken_no;				// リンククリックされた勘定先の明細.査定案件No.
    private int id;						// リンククリックされた勘定先の明細.id
      
    // 変数初期化
    public SenteiForm() {
    	super.gamenId = GS.OB2101;
        this.tanto = GS.EMPTY_CHARCTER;
        this.ar_show = null;
        this.anken_no = GS.EMPTY_CHARCTER;
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
    //自担当分/汎用２ラジオボタン
    public String getTanto() {
        return this.tanto;
    }
    public void setTanto(String tanto) {
        this.tanto = tanto;
    }    
	//表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	//査定案件No.
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}