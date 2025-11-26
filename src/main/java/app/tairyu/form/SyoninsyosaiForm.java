/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;

/**
 * OB1105_実質滞留債権判定_承認 アクションフォームクラス <br>
 */
public class SyoninsyosaiForm extends AppPagerActionForm {
    
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
    
    /** 処理区分(０：滞留判定検証登録、１：一次査定登録、２：そのた) */
    private int jisshi_gyoumu_kbn;
    
    /** T08_滞留判定進捗管理の更新区分(０：滞留判定検証登録、１：一次査定登録) */
    private String upd_taityu_kbn;
    
    /** T16_引当金検討対象BS明細の登録した査定案件No*/
    private String tairyu_anken_no;
    
    /** T16_引当金検討対象BS明細の登録した査定案件NO枝番 */
    private int tairyu_anken_eda;
    
    /** T14_査定進捗管理の登録した査定案件NO */   
    private String satei_anken_no;
    
    /** T14_査定進捗管理の登録した分類２ */
    private String bunrui2;
    
    /** T14_査定進捗管理の登録した部コード */
    private String bu_cd;
    
    /** リンククリックされた勘定先の明細.滞留判定案件No. */
    private String anken_no;   
    
    /** OB1104_実質滞留債権判定_承認一覧Form */ 
    SyoninForm ex_form = null;
    /**
     * @return the ex_form
     */
    public SyoninForm getEx_form() {
        return ex_form;
    }
    /**
     * @param ex_form the ex_form to set
     */
    public void setEx_form(SyoninForm ex_form) {
        this.ex_form = ex_form;
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
	

    // 変数初期化
    public SyoninsyosaiForm() {
        super.gamenId = GS.OB1105;
        this.kaisya = GS.EMPTY_CHARCTER;
        this.soshiki = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;  
        this.kanjo_nm = GS.EMPTY_CHARCTER;  
        this.sinyoktk = GS.EMPTY_CHARCTER;
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
	 * @return the bunrui2
	 */
	public String getBunrui2() {
		return bunrui2;
	}
	/**
	 * @param bunrui2 the bunrui2 to set
	 */
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	/**
	 * @return bu_cd
	 */
	public String getBu_cd() {
		return bu_cd;
	}
	/**
	 * @param bu_cd the bu_cd to set
	 */
	public void setBu_cd(String bu_cd) {
		this.bu_cd = bu_cd;
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
	 * @return the jisshi_gyoumu_kbn
	 */
	public int getJisshi_gyoumu_kbn() {
		return jisshi_gyoumu_kbn;
	}
	/**
	 * @param jisshi_gyoumu_kbn the jisshi_gyoumu_kbn to set
	 */
	public void setJisshi_gyoumu_kbn(int jisshi_gyoumu_kbn) {
		this.jisshi_gyoumu_kbn = jisshi_gyoumu_kbn;
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
	/**
	 * @return the satei_anken_no
	 */
	public String getSatei_anken_no() {
		return satei_anken_no;
	}
	/**
	 * @param satei_anken_no the satei_anken_no to set
	 */
	public void setSatei_anken_no(String satei_anken_no) {
		this.satei_anken_no = satei_anken_no;
	}
	/**
	 * @return the tairyu_anken_eda
	 */
	public int getTairyu_anken_eda() {
		return tairyu_anken_eda;
	}
	/**
	 * @param tairyu_anken_eda the tairyu_anken_eda to set
	 */
	public void setTairyu_anken_eda(int tairyu_anken_eda) {
		this.tairyu_anken_eda = tairyu_anken_eda;
	}
	/**
	 * @return the tairyu_anken_no
	 */
	public String getTairyu_anken_no() {
		return tairyu_anken_no;
	}
	/**
	 * @param tairyu_anken_no the tairyu_anken_no to set
	 */
	public void setTairyu_anken_no(String tairyu_anken_no) {
		this.tairyu_anken_no = tairyu_anken_no;
	}
	/**
	 * @return the upd_taityu_kbn
	 */
	public String getUpd_taityu_kbn() {
		return upd_taityu_kbn;
	}
	/**
	 * @param upd_taityu_kbn the upd_taityu_kbn to set
	 */
	public void setUpd_taityu_kbn(String upd_taityu_kbn) {
		this.upd_taityu_kbn = upd_taityu_kbn;
	}

}
