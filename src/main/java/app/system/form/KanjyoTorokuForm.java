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
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *  OS7109 勘定科目マスタメンテナンス_登録 アクションフォームクラス <br>
 */
public class KanjyoTorokuForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;          
    
    /**  リンククリックされた勘定先の明細.id */	
    private int id;    
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;
    
    /** システム */
    private String system_kbn;    
    
	/** システムセレクトボックス用配列 */
    private LinkedHashMap ar_system;
    
    /** 汎用１ */
    private String hanyo1;    
    
	/** 汎用１セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo1;
    
    /** 汎用２ */
    private String hanyo2;    
    
	/** 汎用２セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo2;
    
    /** 勘定科目コード */
    private String kanjo_cd;
    
    /** 勘定科目名称 */
    private String kanjo_nm;
    
    /** 内分類コード */
    private String kanjo_uchi_cd;
  
    /** 内分類名称 */
    private String kanjo_uchi_nm;
    
    /** 債権フラグ */
    private String saiken_flg;    
    
	/** 債権フラグセレクトボックス用配列 */
    private LinkedHashMap ar_saiken_flg;
    
	/** DR/CR区分 */
    private String drcr_kbn;
    
	/** DR/CR区分セレクトボックス用配列 */
    private LinkedHashMap ar_drcr_kbn;
    
    /** 表示区分 */
    private String hyoji_kbn; 
    
	/** 表示区分セレクトボックス用配列 */
    private LinkedHashMap ar_hyoji_kbn;    
    
    /** 満期日 */
    private String mankibi_flg;
    
    private String focusId;

	
    /**
	 * @return the focusId
	 */
	public String getFocusId() {
		return focusId;
	}

	/**
	 * @param focusId the focusId to set
	 */
	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}

	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the ar_drcr_kbn
	 */
	public LinkedHashMap getAr_drcr_kbn() {
		return ar_drcr_kbn;
	}

	/**
	 * @param ar_drcr_kbn the ar_drcr_kbn to set
	 */
	public void setAr_drcr_kbn(LinkedHashMap ar_drcr_kbn) {
		this.ar_drcr_kbn = ar_drcr_kbn;
	}

	/**
	 * @return the ar_saiken_flg
	 */
	public LinkedHashMap getAr_saiken_flg() {
		return ar_saiken_flg;
	}

	/**
	 * @param ar_saiken_flg the ar_saiken_flg to set
	 */
	public void setAr_saiken_flg(LinkedHashMap ar_saiken_flg) {
		this.ar_saiken_flg = ar_saiken_flg;
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
	 * @return the ar_system
	 */
	public LinkedHashMap getAr_system() {
		return ar_system;
	}

	/**
	 * @param ar_system the ar_system to set
	 */
	public void setAr_system(LinkedHashMap ar_system) {
		this.ar_system = ar_system;
	}

	/**
	 * @return the kanjo_cd
	 */
	public String getKanjo_cd() {
		return kanjo_cd;
	}

	/**
	 * @param kanjo_cd the kanjo_cd to set
	 */
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}

	/**
	 * @return the kanjo_nm
	 */
	public String getKanjo_nm() {
		return kanjo_nm;
	}

	/**
	 * @param kanjo_nm the kanjo_nm to set
	 */
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}

	/**
	 * @return the kanjo_uchi_cd
	 */
	public String getKanjo_uchi_cd() {
		return kanjo_uchi_cd;
	}

	/**
	 * @param kanjo_uchi_cd the kanjo_uchi_cd to set
	 */
	public void setKanjo_uchi_cd(String kanjo_uchi_cd) {
		this.kanjo_uchi_cd = kanjo_uchi_cd;
	}


	/**
	 * @return the saiken_flg
	 */
	public String getSaiken_flg() {
		return saiken_flg;
	}

	/**
	 * @param saiken_flg the saiken_flg to set
	 */
	public void setSaiken_flg(String saiken_flg) {
		this.saiken_flg = saiken_flg;
	}


	// 変数初期化
    public KanjyoTorokuForm() {
        super.gamenId = GS.OS7109;
        this.system_kbn = GS.GSS;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.saiken_flg = GS.EMPTY_CHARCTER;
        this.hyoji_kbn = GS.EMPTY_CHARCTER;
        this.drcr_kbn = GS.EMPTY_CHARCTER;
        this.kanjo_uchi_cd = GS.EMPTY_CHARCTER;
        this.kanjo_uchi_nm = GS.EMPTY_CHARCTER;
        this.mankibi_flg = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.ar_drcr_kbn = null;
        this.ar_hanyo1 = null;
        this.ar_hanyo2 = null;
        this.ar_saiken_flg = null;
        this.ar_show = null;
        this.ar_system = null;
        this.ar_hyoji_kbn = null;
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
	 * @return the ar_hyoji_kbn
	 */
	public LinkedHashMap getAr_hyoji_kbn() {
		return ar_hyoji_kbn;
	}

	/**
	 * @param ar_hyoji_kbn the ar_hyoji_kbn to set
	 */
	public void setAr_hyoji_kbn(LinkedHashMap ar_hyoji_kbn) {
		this.ar_hyoji_kbn = ar_hyoji_kbn;
	}

	/**
	 * @return the meisai_hyoji_kbn
	 */
	public String getMeisai_hyoji_kbn() {
		return hyoji_kbn;
	}

	/**
	 * @param meisai_hyoji_kbn the meisai_hyoji_kbn to set
	 */
	public void setMeisai_hyoji_kbn(String meisai_hyoji_kbn) {
		this.hyoji_kbn = meisai_hyoji_kbn;
	}

	/**
	 * @return the drcr_kbn
	 */
	public String getDrcr_kbn() {
		return drcr_kbn;
	}

	/**
	 * @param drcr_kbn the drcr_kbn to set
	 */
	public void setDrcr_kbn(String drcr_kbn) {
		this.drcr_kbn = drcr_kbn;
	}

	/**
	 * @return the meisai_drcr_kbn
	 */
	public String getMeisai_drcr_kbn() {
		return drcr_kbn;
	}

	/**
	 * @param meisai_drcr_kbn the meisai_drcr_kbn to set
	 */
	public void setMeisai_drcr_kbn(String meisai_drcr_kbn) {
		this.drcr_kbn = meisai_drcr_kbn;
	}

	/**
	 * @return the system_kbn
	 */
	public String getSystem_kbn() {
		return system_kbn;
	}

	/**
	 * @param system_kbn the system_kbn to set
	 */
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
    
    public void reset(ActionMapping mapping, HttpServletRequest request){
 
    }

	/**
	 * @return the hyoji_kbn
	 */
	public String getHyoji_kbn() {
		return hyoji_kbn;
	}

	/**
	 * @param hyoji_kbn the hyoji_kbn to set
	 */
	public void setHyoji_kbn(String hyoji_kbn) {
		this.hyoji_kbn = hyoji_kbn;
	}

	/**
	 * @return the kanjo_uchi_nm
	 */
	public String getKanjo_uchi_nm() {
		return kanjo_uchi_nm;
	}

	/**
	 * @param kanjo_uchi_nm the kanjo_uchi_nm to set
	 */
	public void setKanjo_uchi_nm(String kanjo_uchi_nm) {
		this.kanjo_uchi_nm = kanjo_uchi_nm;
	}

	/**
	 * @return the mankibi_flg
	 */
	public String getMankibi_flg() {
		return mankibi_flg;
	}

	/**
	 * @param mankibi_flg the mankibi_flg to set
	 */
	public void setMankibi_flg(String mankibi_flg) {
		this.mankibi_flg = mankibi_flg;
	}
}
