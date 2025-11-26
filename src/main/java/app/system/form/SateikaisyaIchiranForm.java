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
 * OS7102_査定会社メンテナンス_一覧 アクションフォームクラス <br>
 */
public class SateikaisyaIchiranForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private String systemKbn; 					// システム区分
    private String srhSystemKbn; 				// システム区分(検索用)
    private LinkedHashMap ar_systemKbn; 		// システム【リスト】
    private String hanyo1; 					// 汎用１
    private String srhHanyo1; 					// 汎用１(検索用)
    private LinkedHashMap ar_hanyo1; 			// 汎用１【リスト】
    private String hanyo2; 					// 汎用２
    private String srhHanyo2; 					// 汎用２(検索用)
    private String hanyo2Name; 				// 汎用２名称
    private String srhHanyo2Name; 				// 汎用２名称(検索用)
    private LinkedHashMap ar_show; 			// 表示件数【リスト】
    private int id; 							// ID

    /**
     * 変数初期化 <br>
     */
    public SateikaisyaIchiranForm() {
        
        super.gamenId = GS.OS7102;
        
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.srhSystemKbn = GS.EMPTY_CHARCTER;
        this.ar_systemKbn = null;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.srhHanyo1 = GS.EMPTY_CHARCTER;
        this.ar_hanyo1 = null;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.srhHanyo2 = GS.EMPTY_CHARCTER;
        this.hanyo2Name = GS.EMPTY_CHARCTER;
        this.srhHanyo2Name = GS.EMPTY_CHARCTER;
        this.ar_show = null;
        this.id = 0;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

	/**
	 * @return the ar_hanyo1
	 */
	public LinkedHashMap getAr_hanyo1() {
		return ar_hanyo1;
	}

	/**
	 * @param ar_hanyo1 the ar_hanyo1 to set
	 */
	public void setAr_hanyo1(LinkedHashMap ar_hanyo1) {
		this.ar_hanyo1 = ar_hanyo1;
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
	 * @return the ar_systemKbn
	 */
	public LinkedHashMap getAr_systemKbn() {
		return ar_systemKbn;
	}

	/**
	 * @param ar_systemKbn the ar_systemKbn to set
	 */
	public void setAr_systemKbn(LinkedHashMap ar_systemKbn) {
		this.ar_systemKbn = ar_systemKbn;
	}

	/**
	 * @return the hanyo1
	 */
	public String getHanyo1() {
		return hanyo1;
	}

	/**
	 * @param hanyo1 the hanyo1 to set
	 */
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}

	/**
	 * @return the hanyo2
	 */
	public String getHanyo2() {
		return hanyo2;
	}

	/**
	 * @param hanyo2 the hanyo2 to set
	 */
	public void setHanyo2(String hanyo2) {
		this.hanyo2 = hanyo2;
	}

	/**
	 * @return the hanyo2Name
	 */
	public String getHanyo2Name() {
		return hanyo2Name;
	}

	/**
	 * @param hanyo2Name the hanyo2Name to set
	 */
	public void setHanyo2Name(String hanyo2Name) {
		this.hanyo2Name = hanyo2Name;
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
	 * @return the srhHanyo1
	 */
	public String getSrhHanyo1() {
		return srhHanyo1;
	}

	/**
	 * @param srhHanyo1 the srhHanyo1 to set
	 */
	public void setSrhHanyo1(String srhHanyo1) {
		this.srhHanyo1 = srhHanyo1;
	}

	/**
	 * @return the srhHanyo2
	 */
	public String getSrhHanyo2() {
		return srhHanyo2;
	}

	/**
	 * @param srhHanyo2 the srhHanyo2 to set
	 */
	public void setSrhHanyo2(String srhHanyo2) {
		this.srhHanyo2 = srhHanyo2;
	}

	/**
	 * @return the srhHanyo2Name
	 */
	public String getSrhHanyo2Name() {
		return srhHanyo2Name;
	}

	/**
	 * @param srhHanyo2Name the srhHanyo2Name to set
	 */
	public void setSrhHanyo2Name(String srhHanyo2Name) {
		this.srhHanyo2Name = srhHanyo2Name;
	}

	/**
	 * @return the srhSystemKbn
	 */
	public String getSrhSystemKbn() {
		return srhSystemKbn;
	}

	/**
	 * @param srhSystemKbn the srhSystemKbn to set
	 */
	public void setSrhSystemKbn(String srhSystemKbn) {
		this.srhSystemKbn = srhSystemKbn;
	}

	/**
	 * @return the systemKbn
	 */
	public String getSystemKbn() {
		return systemKbn;
	}

	/**
	 * @param systemKbn the systemKbn to set
	 */
	public void setSystemKbn(String systemKbn) {
		this.systemKbn = systemKbn;
	}
}