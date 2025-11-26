/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.form;

import app.MeisaisyosaiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OB1103_実質滞留債権判定_明細詳細 アクションフォームクラス <br>
 */
public class SyosaiForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
    private LinkedHashMap ar_tairyu_jdg; 				// 滞留判定セレクトボックス用配列
    private List<Map> ar_tenpu; 						// 添付ファイル情報用配列
    private MeisaisyosaiBean syosai_bean;				// 明細情報
    private String komoku1;							// 項目１タイトル
    private String komoku2;							// 項目２タイトル
    private String komoku3;							// 項目３タイトル
    private String komoku4;							// 項目４タイトル
    private String komoku5;							// 項目５タイトル
    private String[] ar_kaijyo_chk;
    private int id; 									// リンククリックされた添付ファイル.id

    /**
     * 変数初期化 <br>
     */
    public SyosaiForm() {
        
        super.gamenId = GS.OB1103;
        
        this.ar_tairyu_jdg = null;
        this.ar_tenpu = null;
        this.syosai_bean = null;
        this.komoku1 = GS.EMPTY_CHARCTER;
        this.komoku2 = GS.EMPTY_CHARCTER;
        this.komoku3 = GS.EMPTY_CHARCTER;
        this.komoku4 = GS.EMPTY_CHARCTER;
        this.komoku5 = GS.EMPTY_CHARCTER;
        this.ar_kaijyo_chk = null;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

    /**
     * @return the ar_tairyu_jdg
     */
    public LinkedHashMap getAr_tairyu_jdg() {
        return ar_tairyu_jdg;
    }

    /**
     * @param ar_tairyu_jdg the ar_tairyu_jdg to set
     */
    public void setAr_tairyu_jdg(LinkedHashMap ar_tairyu_jdg) {
        this.ar_tairyu_jdg = ar_tairyu_jdg;
    }

	/**
	 * @return the ar_tenpu
	 */
	public List<Map> getAr_tenpu() {
		return ar_tenpu;
	}

	/**
	 * @param ar_tenpu the ar_tenpu to set
	 */
	public void setAr_tenpu(List<Map> ar_tenpu) {
		this.ar_tenpu = ar_tenpu;
	}

	/**
	 * @return the syosai_bean
	 */
	public MeisaisyosaiBean getSyosai_bean() {
		return syosai_bean;
	}

	/**
	 * @param syosai_bean the syosai_bean to set
	 */
	public void setSyosai_bean(MeisaisyosaiBean syosai_bean) {
		this.syosai_bean = syosai_bean;
	}

	/**
	 * @return the komoku1
	 */
	public String getKomoku1() {
		return komoku1;
	}

	/**
	 * @param komoku1 the komoku1 to set
	 */
	public void setKomoku1(String komoku1) {
		this.komoku1 = komoku1;
	}

	/**
	 * @return the komoku2
	 */
	public String getKomoku2() {
		return komoku2;
	}

	/**
	 * @param komoku2 the komoku2 to set
	 */
	public void setKomoku2(String komoku2) {
		this.komoku2 = komoku2;
	}

	/**
	 * @return the komoku3
	 */
	public String getKomoku3() {
		return komoku3;
	}

	/**
	 * @param komoku3 the komoku3 to set
	 */
	public void setKomoku3(String komoku3) {
		this.komoku3 = komoku3;
	}

	/**
	 * @return the komoku4
	 */
	public String getKomoku4() {
		return komoku4;
	}

	/**
	 * @param komoku4 the komoku4 to set
	 */
	public void setKomoku4(String komoku4) {
		this.komoku4 = komoku4;
	}

	/**
	 * @return the komoku5
	 */
	public String getKomoku5() {
		return komoku5;
	}

	/**
	 * @param komoku5 the komoku5 to set
	 */
	public void setKomoku5(String komoku5) {
		this.komoku5 = komoku5;
	}

	/**
	 * @return the ar_kaijyo_chk
	 */
	public String[] getAr_kaijyo_chk() {
		return ar_kaijyo_chk;
	}

	/**
	 * @param ar_kaijyo_chk the ar_kaijyo_chk to set
	 */
	public void setAr_kaijyo_chk(String[] ar_kaijyo_chk) {
		this.ar_kaijyo_chk = ar_kaijyo_chk;
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

}