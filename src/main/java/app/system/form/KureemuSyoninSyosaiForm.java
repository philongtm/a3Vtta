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

/**
 * OS3105_クレーム債権再設定_承認 アクションフォームクラス <br>
 */
public class KureemuSyoninSyosaiForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;

    /**  リンククリックされた勘定先の明細.id */	
    private int id;                     
    
	/** 汎用１ */
    String kaisya;
    
    /** 組織 */
    String soshiki;
    
    /** 勘定先CD */
    String kanjo_cd;
    
    /** 勘定先名称 */
    String kanjo_nm;
    
    /** 信用格付 */ 
    String sinyoktk;
        
    /** 次実施フェーズ */
    private String ji_jishi_phase;
    
    /** 次開始ステータス */
    private String ji_kaishi_status;
    
    /** 機)明細.フェーズ */
    private String meisai_phase;
    
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
    public KureemuSyoninSyosaiForm() {
        super.gamenId = GS.OS3105;
        this.kaisya = GS.EMPTY_CHARCTER;
        this.soshiki = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;  
        this.kanjo_nm = GS.EMPTY_CHARCTER;  
        this.sinyoktk = GS.EMPTY_CHARCTER;
        this.ji_jishi_phase = GS.EMPTY_CHARCTER;
        this.ji_kaishi_status = GS.EMPTY_CHARCTER;
        this.meisai_phase = GS.EMPTY_CHARCTER;
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
     * @return the sinyoktk
     */
    public String getSinyoktk() {
        return sinyoktk;
    }
    /**
     * @param sinyoktk the sinyoktk to set
     */
    public void setSinyoktk(String sinyoktk) {
        this.sinyoktk = sinyoktk;
    }
    /**
     * @return the soshiki
     */
    public String getSoshiki() {
        return soshiki;
    }
    /**
     * @param soshiki the soshiki to set
     */
    public void setSoshiki(String soshiki) {
        this.soshiki = soshiki;
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
    /**
     * @return the kaisya
     */
    public String getKaisya() {
        return kaisya;
    }
    /**
     * @param kaisya the kaisya to set
     */
    public void setKaisya(String kaisya) {
        this.kaisya = kaisya;
    }
	/**
	 * @return the ji_jishi_phase
	 */
	public String getJi_jishi_phase() {
		return ji_jishi_phase;
	}
	/**
	 * @param ji_jishi_phase the ji_jishi_phase to set
	 */
	public void setJi_jishi_phase(String ji_jishi_phase) {
		this.ji_jishi_phase = ji_jishi_phase;
	}
	/**
	 * @return the ji_kaishi_status
	 */
	public String getJi_kaishi_status() {
		return ji_kaishi_status;
	}
	/**
	 * @param ji_kaishi_status the ji_kaishi_status to set
	 */
	public void setJi_kaishi_status(String ji_kaishi_status) {
		this.ji_kaishi_status = ji_kaishi_status;
	}
	/**
	 * @return the meisai_phase
	 */
	public String getMeisai_phase() {
		return meisai_phase;
	}
	/**
	 * @param meisai_phase the meisai_phase to set
	 */
	public void setMeisai_phase(String meisai_phase) {
		this.meisai_phase = meisai_phase;
	}

}
