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
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * OS7103_査定会社メンテナンス_登録 アクションフォームクラス <br>
 */
public class SateikaisyaTorokuForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
	
    private String seniMode; 					// 画面遷移モード
    private String systemKbn; 					// システム区分
    private LinkedHashMap ar_systemKbn; 		// システム【リスト】
    private String hanyo1; 					// 汎用１
    private LinkedHashMap ar_hanyo1; 			// 汎用１【リスト】
    private String hanyo2; 					// 汎用２
    private String hanyo2Jp; 					// 汎用２名称(日本語)
    private String hanyo2En; 					// 汎用２名称(英語)
    private String hyojunJikokuCd; 			// 標準時刻コード
    private LinkedHashMap ar_hyojunJikoku; 	// 標準時刻【リスト】
    private String tyusyutu_taisyo_flg; 		// 抽出対象フラグ
    private String kaisyaCd; 					// 会社コード

    /**
     * 変数初期化 <br>
     */
    public SateikaisyaTorokuForm() {
        
        super.gamenId = GS.OS7103;
        
        this.seniMode = GS.EMPTY_CHARCTER;
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.ar_systemKbn = null;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.ar_hanyo1 = null;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.hanyo2Jp = GS.EMPTY_CHARCTER;
        this.hanyo2En = GS.EMPTY_CHARCTER;
        this.hyojunJikokuCd = GS.EMPTY_CHARCTER;
        this.ar_hyojunJikoku = null;
        this.tyusyutu_taisyo_flg = GS.OFF;
        this.kaisyaCd = GS.EMPTY_CHARCTER;
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
	 * @return the tyusyutu_taisyo_flg
	 */
	public String getTyusyutu_taisyo_flg() {
		return tyusyutu_taisyo_flg;
	}

	/**
	 * @param tyusyutu_taisyo_flg the tyusyutu_taisyo_flg to set
	 */
	public void setTyusyutu_taisyo_flg(String tyusyutu_taisyo_flg) {
		this.tyusyutu_taisyo_flg = tyusyutu_taisyo_flg;
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
	 * @return the hanyo2En
	 */
	public String getHanyo2En() {
		return hanyo2En;
	}

	/**
	 * @param hanyo2En the hanyo2En to set
	 */
	public void setHanyo2En(String hanyo2En) {
		this.hanyo2En = hanyo2En;
	}

	/**
	 * @return the hanyo2Jp
	 */
	public String getHanyo2Jp() {
		return hanyo2Jp;
	}

	/**
	 * @param hanyo2Jp the hanyo2Jp to set
	 */
	public void setHanyo2Jp(String hanyo2Jp) {
		this.hanyo2Jp = hanyo2Jp;
	}

	/**
	 * @return the seniMode
	 */
	public String getSeniMode() {
		return seniMode;
	}

	/**
	 * @param seniMode the seniMode to set
	 */
	public void setSeniMode(String seniMode) {
		this.seniMode = seniMode;
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
	 * @return the ar_hyojunJikoku
	 */
	public LinkedHashMap getAr_hyojunJikoku() {
		return ar_hyojunJikoku;
	}

	/**
	 * @param ar_hyojunJikoku the ar_hyojunJikoku to set
	 */
	public void setAr_hyojunJikoku(LinkedHashMap ar_hyojunJikoku) {
		this.ar_hyojunJikoku = ar_hyojunJikoku;
	}

	/**
	 * @return the hyojunJikokuCd
	 */
	public String getHyojunJikokuCd() {
		return hyojunJikokuCd;
	}

	/**
	 * @param hyojunJikokuCd the hyojunJikokuCd to set
	 */
	public void setHyojunJikokuCd(String hyojunJikokuCd) {
		this.hyojunJikokuCd = hyojunJikokuCd;
	}
	
    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	this.setTyusyutu_taisyo_flg(GS.OFF);
    }

	/**
	 * @return the kaisyaCd
	 */
	public String getKaisyaCd() {
		return kaisyaCd;
	}

	/**
	 * @param kaisyaCd the kaisyaCd to set
	 */
	public void setKaisyaCd(String kaisyaCd) {
		this.kaisyaCd = kaisyaCd;
	}
}