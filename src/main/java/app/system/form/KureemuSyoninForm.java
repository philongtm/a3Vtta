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
 * OS3104_クレーム債権再設定_承認一覧 アクションフォームクラス <br>
 */
public class KureemuSyoninForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;

    /**  リンククリックされた勘定先の明細.id */	
    private int id;                     
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;             
    
    /** リンククリックされた勘定先の明細.滞留判定案件No. */
    private String anken_no;   
    
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    
    // 変数初期化
    public KureemuSyoninForm() {
        super.gamenId = GS.OS3104;
        this.anken_no = GS.EMPTY_CHARCTER;
        this.ar_show = null;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }
    
	/**
	 * @return the ar_show
	 */
	public LinkedHashMap getAr_show() {
		return ar_show;
	}

	/**
	 * @param ar_show the ar_show to set
	 */
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	/**
	 * @return the anken_no
	 */
	public String getAnken_no() {
		return anken_no;
	}
	/**
	 * @param anken_no the anken_no to set
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
}
