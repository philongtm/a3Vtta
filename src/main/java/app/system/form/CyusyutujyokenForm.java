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
 * OS7110_抽出条件メンテナンス_一覧 アクションフォームクラス <br>
 */
public class CyusyutujyokenForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private String systemKbn; 					// システム区分
    private String srhSystemKbn; 				// システム区分(検索用)
    private LinkedHashMap ar_systemKbn; 		// システム区分【リスト】
    private String hanyo1; 					// 汎用1
    private String srhHanyo1; 					// 汎用1(検索用)
    private LinkedHashMap ar_hanyo1; 			// 汎用1【リスト】
    private String hanyo2; 					// 汎用2
    private String srhHanyo2; 					// 汎用2(検索用)
    private LinkedHashMap ar_hanyo2; 			// 汎用2【リスト】
    private String kesanKbn; 					// 決算期区分
    private String srhKesanKbn; 				// 決算期区分(検索用)
    private LinkedHashMap ar_kesanKbn; 		// 決算期区分【リスト】
    private String kijyunbi; 					// 基準日
    private String srhKijyunbi; 				// 基準日(検索用)
    private LinkedHashMap ar_kijyunbi; 		// 基準日【リスト】
    private String jyokenNm; 					// 条件名称
    private String srhJyokenNm; 				// 条件名称(検索用)
    private LinkedHashMap ar_show; 			// 表示件数【リスト】
    private int id; 							// ID

    /**
     * 変数初期化 <br>
     */
    public CyusyutujyokenForm() {
        
        super.gamenId = GS.OS7110;
        
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.srhSystemKbn = GS.EMPTY_CHARCTER;
        this.ar_systemKbn = null;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.srhHanyo1 = GS.EMPTY_CHARCTER;
        this.ar_hanyo1 = null;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.srhHanyo2 = GS.EMPTY_CHARCTER;
        this.ar_hanyo2 = null;
        this.kesanKbn = GS.EMPTY_CHARCTER;
        this.srhKesanKbn = GS.EMPTY_CHARCTER;
        this.ar_kesanKbn = null;
        this.kijyunbi = GS.EMPTY_CHARCTER;
        this.srhKijyunbi = GS.EMPTY_CHARCTER;
        this.ar_kijyunbi = null;
        this.jyokenNm = GS.EMPTY_CHARCTER;
        this.srhJyokenNm = GS.EMPTY_CHARCTER;
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
	 * @return the ar_hanyo2
	 */
	public LinkedHashMap getAr_hanyo2() {
		return ar_hanyo2;
	}

	/**
	 * @param ar_hanyo2 the ar_hanyo2 to set
	 */
	public void setAr_hanyo2(LinkedHashMap ar_hanyo2) {
		this.ar_hanyo2 = ar_hanyo2;
	}

	/**
	 * @return the ar_kesanKbn
	 */
	public LinkedHashMap getAr_kesanKbn() {
		return ar_kesanKbn;
	}

	/**
	 * @param ar_kesanKbn the ar_kesanKbn to set
	 */
	public void setAr_kesanKbn(LinkedHashMap ar_kesanKbn) {
		this.ar_kesanKbn = ar_kesanKbn;
	}

	/**
	 * @return the ar_kijyunbi
	 */
	public LinkedHashMap getAr_kijyunbi() {
		return ar_kijyunbi;
	}

	/**
	 * @param ar_kijyunbi the ar_kijyunbi to set
	 */
	public void setAr_kijyunbi(LinkedHashMap ar_kijyunbi) {
		this.ar_kijyunbi = ar_kijyunbi;
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
	 * @return the jyokenNm
	 */
	public String getJyokenNm() {
		return jyokenNm;
	}

	/**
	 * @param jyokenNm the jyokenNm to set
	 */
	public void setJyokenNm(String jyokenNm) {
		this.jyokenNm = jyokenNm;
	}

	/**
	 * @return the kesanKbn
	 */
	public String getKesanKbn() {
		return kesanKbn;
	}

	/**
	 * @param kesanKbn the kesanKbn to set
	 */
	public void setKesanKbn(String kesanKbn) {
		this.kesanKbn = kesanKbn;
	}

	/**
	 * @return the kijyunbi
	 */
	public String getKijyunbi() {
		return kijyunbi;
	}

	/**
	 * @param kijyunbi the kijyunbi to set
	 */
	public void setKijyunbi(String kijyunbi) {
		this.kijyunbi = kijyunbi;
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
	 * @return the srhJyokenNm
	 */
	public String getSrhJyokenNm() {
		return srhJyokenNm;
	}

	/**
	 * @param srhJyokenNm the srhJyokenNm to set
	 */
	public void setSrhJyokenNm(String srhJyokenNm) {
		this.srhJyokenNm = srhJyokenNm;
	}

	/**
	 * @return the srhKesanKbn
	 */
	public String getSrhKesanKbn() {
		return srhKesanKbn;
	}

	/**
	 * @param srhKesanKbn the srhKesanKbn to set
	 */
	public void setSrhKesanKbn(String srhKesanKbn) {
		this.srhKesanKbn = srhKesanKbn;
	}

	/**
	 * @return the srhKijyunbi
	 */
	public String getSrhKijyunbi() {
		return srhKijyunbi;
	}

	/**
	 * @param srhKijyunbi the srhKijyunbi to set
	 */
	public void setSrhKijyunbi(String srhKijyunbi) {
		this.srhKijyunbi = srhKijyunbi;
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