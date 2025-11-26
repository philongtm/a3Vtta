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

import java.util.LinkedHashMap;

/**
 * OS4101_ゴルフ会員権一覧 アクションフォームクラス <br>
 */
public class GolfForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private String sateiki; 							// 査定期
    private LinkedHashMap ar_sateiki; 					// 査定期【リスト】
    private LinkedHashMap ar_show; 					// 表示件数【リスト】
	private int id; 									// ID
    private String taisyoYm; 							// 対象年月
    private String shorikaisu; 						// 最大処理回数
    private String maxSateiki; 						// 最新査定期

    /**
     * 変数初期化 <br>
     */
    public GolfForm() {
        
        super.gamenId = GS.OS4101;
        
        this.sateiki = GS.EMPTY_CHARCTER;
        this.ar_sateiki = null;
        this.ar_show = null;
    	this.id = 0;
    	this.taisyoYm = GS.EMPTY_CHARCTER;
    	this.shorikaisu = GS.EMPTY_CHARCTER;
    	this.maxSateiki = GS.EMPTY_CHARCTER;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

	/**
	 * @return the ar_sateiki
	 */
	public LinkedHashMap getAr_sateiki() {
		return ar_sateiki;
	}

	/**
	 * @param ar_sateiki the ar_sateiki to set
	 */
	public void setAr_sateiki(LinkedHashMap ar_sateiki) {
		this.ar_sateiki = ar_sateiki;
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

	/**
	 * @return the sateiki
	 */
	public String getSateiki() {
		return sateiki;
	}

	/**
	 * @param sateiki the sateiki to set
	 */
	public void setSateiki(String sateiki) {
		this.sateiki = sateiki;
	}

	/**
	 * @return the shorikaisu
	 */
	public String getShorikaisu() {
		return shorikaisu;
	}

	/**
	 * @param shorikaisu the shorikaisu to set
	 */
	public void setShorikaisu(String shorikaisu) {
		this.shorikaisu = shorikaisu;
	}

	/**
	 * @return the taisyoYm
	 */
	public String getTaisyoYm() {
		return taisyoYm;
	}

	/**
	 * @param taisyoYm the taisyoYm to set
	 */
	public void setTaisyoYm(String taisyoYm) {
		this.taisyoYm = taisyoYm;
	}
	/**
	 * @return the maxSateiki
	 */
	public String getMaxSateiki() {
		return maxSateiki;
	}

	/**
	 * @param maxSateiki the maxSateiki to set
	 */
	public void setMaxSateiki(String maxSateiki) {
		this.maxSateiki = maxSateiki;
	}
}