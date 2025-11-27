/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.form;

import common.global.GS;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 *  OS7108_勘定科目マスタメンテナンス_一覧・登録 アクションフォームビンー <br>
 */
public class KanjyoBean{
    
    /**  */
    private static final long serialVersionUID = 1L;          
    
    /**  リンククリックされた勘定先の明細.id */	
    private String id;    
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;
    
    /** システム */
    private String system_kbn;    
    
	/** システムセレクトボックス用配列 */
    private LinkedHashMap ar_system;
    
    /** システム表示名 */
    private String system_kbn_nm;
    
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
    
    /** 勘定科目 */
    private String kanjo;
    
    /** 内分類コード */
    private String kanjo_uchi_cd;    
  
    /** 内分類 */
    private String kanjo_uchi; 
    
    /** 明細の債権フラグ */
    private String meisai_saiken_flg; 
    
	/** 明細の債権フラグセレクトボックス用配列 */
    private LinkedHashMap ar_meisai_saiken_flg;
    
	/** 明細のDR/CR区分 */
    private String meisai_drcr_kbn;
    
	/** 明細のDR/CR区分セレクトボックス用配列 */
    private LinkedHashMap ar_drcr_kbn;
    
    /** 明細の表示区分 */
    private String meisai_hyouji_kbn; 
    
	/** 明細の表示区分セレクトボックス用配列 */
    private LinkedHashMap ar_hyoji_kbn;
    
	/** 勘定科目情報セレクトボックス用配列 */
    private LinkedHashMap ar_kanjyo;
    
    /** 満期日 */
    private String mankibi_flg; 
    
    
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
	 * @return the ar_kanjyo
	 */
	public LinkedHashMap getAr_kanjyo() {
		return ar_kanjyo;
	}

	/**
	 * @param ar_kanjyo the ar_kanjyo to set
	 */
	public void setAr_kanjyo(LinkedHashMap ar_kanjyo) {
		this.ar_kanjyo = ar_kanjyo;
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



	// 変数初期化
    public KanjyoBean() {
        this.system_kbn = GS.GSS;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.meisai_saiken_flg = GS.EMPTY_CHARCTER;
        this.meisai_saiken_flg = GS.EMPTY_CHARCTER;
        this.meisai_hyouji_kbn = GS.EMPTY_CHARCTER;
        this.meisai_drcr_kbn = GS.EMPTY_CHARCTER;
        this.kanjo_uchi_cd = GS.EMPTY_CHARCTER;
        this.mankibi_flg = GS.EMPTY_CHARCTER;
        this.system_kbn_nm = GS.EMPTY_CHARCTER;
        this.ar_drcr_kbn = null;
        this.ar_kanjyo = null;
        this.ar_hanyo1 = null;
        this.ar_hanyo2 = null;
        this.ar_meisai_saiken_flg = null;
        this.ar_show = null;
        this.ar_system = null;
        this.ar_hyoji_kbn = null;
    }    

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
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
	 * @return the kanjo
	 */
	public String getKanjo() {
		return kanjo;
	}

	/**
	 * @param kanjo the kanjo to set
	 */
	public void setKanjo(String kanjo) {
		this.kanjo = kanjo;
	}

	/**
	 * @return the kanjo_uchi
	 */
	public String getKanjo_uchi() {
		return kanjo_uchi;
	}

	/**
	 * @param kanjo_uchi the kanjo_uchi to set
	 */
	public void setKanjo_uchi(String kanjo_uchi) {
		this.kanjo_uchi = kanjo_uchi;
	}

	/**
	 * @return the ar_meisai_saiken_flg
	 */
	public LinkedHashMap getAr_meisai_saiken_flg() {
		return ar_meisai_saiken_flg;
	}

	/**
	 * @param ar_meisai_saiken_flg the ar_meisai_saiken_flg to set
	 */
	public void setAr_meisai_saiken_flg(LinkedHashMap ar_meisai_saiken_flg) {
		this.ar_meisai_saiken_flg = ar_meisai_saiken_flg;
	}

	/**
	 * @return the meisai_drcr_kbn
	 */
	public String getMeisai_drcr_kbn() {
		return meisai_drcr_kbn;
	}

	/**
	 * @param meisai_drcr_kbn the meisai_drcr_kbn to set
	 */
	public void setMeisai_drcr_kbn(String meisai_drcr_kbn) {
		this.meisai_drcr_kbn = meisai_drcr_kbn;
	}


	/**
	 * @return the meisai_hyouji_kbn
	 */
	public String getMeisai_hyouji_kbn() {
		return meisai_hyouji_kbn;
	}

	/**
	 * @param meisai_hyouji_kbn the meisai_hyouji_kbn to set
	 */
	public void setMeisai_hyouji_kbn(String meisai_hyouji_kbn) {
		this.meisai_hyouji_kbn = meisai_hyouji_kbn;
	}

	/**
	 * @return the meisai_saiken_flg
	 */
	public String getMeisai_saiken_flg() {
		return meisai_saiken_flg;
	}

	/**
	 * @param meisai_saiken_flg the meisai_saiken_flg to set
	 */
	public void setMeisai_saiken_flg(String meisai_saiken_flg) {
		this.meisai_saiken_flg = meisai_saiken_flg;
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

	/**
	 * @return the system_kbn_nm
	 */
	public String getSystem_kbn_nm() {
		return system_kbn_nm;
	}

	/**
	 * @param system_kbn_nm the system_kbn_nm to set
	 */
	public void setSystem_kbn_nm(String system_kbn_nm) {
		this.system_kbn_nm = system_kbn_nm;
	}
}
