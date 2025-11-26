/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/09/08		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.tairyu.form;

import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OB1104_実質滞留債権判定_承認一覧 アクションフォームクラス <br>
 */
public class SyoninForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;

    /**  リンククリックされた勘定先の明細.id */	
    private int id;                     
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;

	/** 一括承認チッェクボックス用 */
    private boolean ikt_syonin;
    
    /** 参照フェーズ */
    private String sansyo_phase;
    
    /** リンククリックされた勘定先の明細.滞留判定案件No. */
    private String anken_no;               
    
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
     * @return the sansyo_phase
     */
    public String getSansyo_phase() {
        return sansyo_phase;
    }
    /**
     * @param sansyo_phase the sansyo_phase to set
     */
    public void setSansyo_phase(String sansyo_phase) {
        this.sansyo_phase = sansyo_phase;
    }
    // 変数初期化
    public SyoninForm() {
        super.gamenId = GS.OB1104;
        this.ikt_syonin = false;
        this.sansyo_phase = GS.EMPTY_CHARCTER;
        this.ji_jishi_phase = GS.EMPTY_CHARCTER;
        this.ji_kaishi_status = GS.EMPTY_CHARCTER;
        this.meisai_phase = GS.EMPTY_CHARCTER;
        this.satei_anken_no = GS.EMPTY_CHARCTER;
        this.bunrui2 = GS.EMPTY_CHARCTER;
        this.upd_taityu_kbn = GS.EMPTY_CHARCTER;
        this.tairyu_anken_no = GS.EMPTY_CHARCTER;
        this.jisshi_gyoumu_kbn = 0;
        this.tairyu_anken_eda = 0;        
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
    
    public void reset(ActionMapping mapping, HttpServletRequest request){
        // 一括承認が初期化にする
        this.ikt_syonin = false;
        // 明細一覧が初期化にする
        List ar_list = super.getList();
        if (ar_list != null) {
            for (int i = 0; i < ar_list.size(); i++) {
            	TorihikisakiBean meisaiBean = (TorihikisakiBean) ar_list.get(i);
            	meisaiBean.setSyonin_chk(GS.EMPTY_CHARCTER);
            }
        }
    }
    /**
     * @return the ikt_syonin
     */
    public boolean isIkt_syonin() {
        return ikt_syonin;
    }
    /**
     * @param ikt_syonin the ikt_syonin to set
     */
    public void setIkt_syonin(boolean ikt_syonin) {
        this.ikt_syonin = ikt_syonin;
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
}
