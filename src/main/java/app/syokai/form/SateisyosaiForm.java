/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.5.0_17
更新履歴
No		日付			修正者			修正内容
001		2009/4/27		WYH 			新規作成 
******************************************************************************/

package app.syokai.form;


import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OS6102_査定内容詳細 アクションフォームクラス <br>
 */
public class SateisyosaiForm extends AppPagerActionForm {
    
    private static final long serialVersionUID = 1L;
    private String btn_sasimodo_flg;							// 差戻ボタン非表示フラグ
    private String btn_download_flg;							// ダウンロードボタン非表示フラグ
    private String link_comment_flg;							// コメント表示リンク非表示フラグ
    private String disp_phase_flg;								// フェーズ表示フラグ
    private String kensaku_anken_no;							// 検索用案件No.
    private LinkedHashMap<String, String> ar_phase;			// フェーズ用配列
    private String anken_phase;								// 案件フェーズ
    private List<Map<String, String>> ar_toroku_shounin;		// 登録/承認担当者用配列
    private TorihikisakiBean tori_taihi;						// 取引先情報退避   
    private String syoribiTitle;								// 処理日時タイトル
    private int currentTab;									// 現タブ
    private String dispTabTorihikiGaiyo; 						// 取引先概要タブ
    private String dispTabSaikenKubun; 						// 取引先・債権区分判定タブ
    private String dispTabHikiateHantei; 						// 引当金判定タブ
    private String dispTabSaikenMeisai; 						// 債権明細タブ
    private String dispTabRyuhoSaimu; 							// 留保債務タブ
    private String dispTabTairyuSaiken; 						// 滞留債権明細タブ
    private String dispTabHikiateKakunin; 						// 引当金確認タブ
    private String dispTabHikiateKensyo; 						// 引当金検証タブ
    private String jspPath; 									// タブのJSPファイルのパス
	  
    /**
     * 変数初期化 <br>
     */
    public SateisyosaiForm() {
        super.gamenId = GS.OS6102;
        this.btn_sasimodo_flg = GS.EMPTY_CHARCTER;
        this.btn_download_flg = GS.EMPTY_CHARCTER;
        this.link_comment_flg = GS.EMPTY_CHARCTER;
        this.disp_phase_flg = GS.ON;
        this.kensaku_anken_no = GS.EMPTY_CHARCTER;
        this.ar_phase = null;
        this.anken_phase = GS.EMPTY_CHARCTER;
        this.ar_toroku_shounin = null;
        this.tori_taihi = null;
        this.syoribiTitle = GS.EMPTY_CHARCTER;
        this.currentTab = 1;
        
        this.dispTabTorihikiGaiyo = GS.OFF;
        this.dispTabSaikenKubun = GS.OFF;
        this.dispTabHikiateHantei = GS.OFF;
        this.dispTabSaikenMeisai = GS.OFF;
        this.dispTabRyuhoSaimu = GS.OFF;
        this.dispTabTairyuSaiken = GS.OFF;
        this.dispTabHikiateKakunin = GS.OFF;
        this.dispTabHikiateKensyo = GS.OFF;
        
        this.jspPath = GS.EMPTY_CHARCTER;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }
 
    /**
     * 差戻ボタン非表示フラグ <br>
     * 
     * @return the btn_sasimodo_flg
     */
    public String getBtn_sasimodo_flg() {
        return btn_sasimodo_flg;
    }
    
    /**
     * 査定検索/滞留判定検索<br>
     * 
     * @param btn_sasimodo_flg the btn_sasimodo_flg to set
     */
    public void setBtn_sasimodo_flg(String btn_sasimodo_flg) {
        this.btn_sasimodo_flg = btn_sasimodo_flg;
    }
    
    /**
     * ダウンロードボタン非表示フラグ <br>
     * 
     * @return the btn_download_flg
     */
    public String getBtn_download_flg() {
        return btn_download_flg;
    }
    
    /**
     * ダウンロードボタン非表示フラグ<br>
     * 
     * @param btn_download_flg the btn_download_flg to set
     */
    public void setBtn_download_flg(String btn_download_flg) {
        this.btn_download_flg = btn_download_flg;
    }
    
    
    /**
     * コメント表示リンク非表示フラグ <br>
     * 
     * @return the link_comment_flg
     */
    public String getLink_comment_flg() {
        return link_comment_flg;
    }
    
    /**
     * コメント表示リンク非表示フラグ<br>
     * 
     * @param link_comment_flg the link_comment_flg to set
     */
    public void setLink_comment_flg(String link_comment_flg) {
        this.link_comment_flg = link_comment_flg;
    }
    
    /**
	 * 検索用案件No.<br>
	 * 
	 * @return the kensaku_anken_no
	 */
	public String getKensaku_anken_no() {
		return kensaku_anken_no;
	}
	
	/**
	 * 検索用案件No. <br>
	 * 
	 * @param kensaku_anken_no the kensaku_anken_no to set
	 */
	public void setKensaku_anken_no(String kensaku_anken_no) {
		this.kensaku_anken_no = kensaku_anken_no;
	}
	
    /**
     * 案件フェーズ<br>
     * 
     * @return the anken_phase
     */
    public String getAnken_phase() {
        return anken_phase;
    }
    
    /**
     * 案件フェーズ<br>
     * 
     * @param anken_phase the anken_phase to set
     */
    public void setAnken_phase(String anken_phase) {
        this.anken_phase = anken_phase;
    }
	
    /**
	 * 処理日時タイトル <br>
	 * 
	 * @return the syoribiTitle
	 */
	public String getSyoribiTitle() {
		return syoribiTitle;
	}
	
	/**
	 * 処理日時タイトル <br>
	 * 
	 * @param syoribiTitle the syoribiTitle to set
	 */
	public void setSyoribiTitle(String syoribiTitle) {
		this.syoribiTitle = syoribiTitle;
	}

	/**
	 * 現タブ <br>
	 * 
	 * @return the currentTab
	 */
	public int getCurrentTab() {
		return currentTab;
	}

	/**
	 * 現タブ <br>
	 * 
	 * @param currentTab the currentTab to set
	 */
	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}

	/**
	 * フェーズ用配列 <br>
	 * 
	 * @return the ar_phase
	 */
	public LinkedHashMap<String, String> getAr_phase() {
		return ar_phase;
	}

	/**
	 * フェーズ用配列 <br>
	 * 
	 * @param ar_phase the ar_phase to set
	 */
	public void setAr_phase(LinkedHashMap<String, String> ar_phase) {
		this.ar_phase = ar_phase;
	}

	/**
     * 取引先情報退避<br>
     * 
	 * @return the tori_taihi
	 */
	public TorihikisakiBean getTori_taihi() {
		return tori_taihi;
	}

	/**
     * 取引先情報退避<br>
     * 
	 * @param tori_taihi the tori_taihi to set
	 */
	public void setTori_taihi(TorihikisakiBean tori_taihi) {
		this.tori_taihi = tori_taihi;
	}

	/**
	 * フェーズ表示フラグ<br>
	 * 
	 * @return the disp_phase_flg
	 */
	public String getDisp_phase_flg() {
		return disp_phase_flg;
	}

	/**
	 * フェーズ表示フラグ<br>
	 * 
	 * @param disp_phase_flg the disp_phase_flg to set
	 */
	public void setDisp_phase_flg(String disp_phase_flg) {
		this.disp_phase_flg = disp_phase_flg;
	}

	/**
	 * @return the dispTabHikiateHantei
	 */
	public String getDispTabHikiateHantei() {
		return dispTabHikiateHantei;
	}

	/**
	 * @param dispTabHikiateHantei the dispTabHikiateHantei to set
	 */
	public void setDispTabHikiateHantei(String dispTabHikiateHantei) {
		this.dispTabHikiateHantei = dispTabHikiateHantei;
	}

	/**
	 * @return the dispTabHikiateKakunin
	 */
	public String getDispTabHikiateKakunin() {
		return dispTabHikiateKakunin;
	}

	/**
	 * @param dispTabHikiateKakunin the dispTabHikiateKakunin to set
	 */
	public void setDispTabHikiateKakunin(String dispTabHikiateKakunin) {
		this.dispTabHikiateKakunin = dispTabHikiateKakunin;
	}

	/**
	 * @return the dispTabHikiateKensyo
	 */
	public String getDispTabHikiateKensyo() {
		return dispTabHikiateKensyo;
	}

	/**
	 * @param dispTabHikiateKensyo the dispTabHikiateKensyo to set
	 */
	public void setDispTabHikiateKensyo(String dispTabHikiateKensyo) {
		this.dispTabHikiateKensyo = dispTabHikiateKensyo;
	}

	/**
	 * @return the dispTabRyuhoSaimu
	 */
	public String getDispTabRyuhoSaimu() {
		return dispTabRyuhoSaimu;
	}

	/**
	 * @param dispTabRyuhoSaimu the dispTabRyuhoSaimu to set
	 */
	public void setDispTabRyuhoSaimu(String dispTabRyuhoSaimu) {
		this.dispTabRyuhoSaimu = dispTabRyuhoSaimu;
	}

	/**
	 * @return the dispTabSaikenKubun
	 */
	public String getDispTabSaikenKubun() {
		return dispTabSaikenKubun;
	}

	/**
	 * @param dispTabSaikenKubun the dispTabSaikenKubun to set
	 */
	public void setDispTabSaikenKubun(String dispTabSaikenKubun) {
		this.dispTabSaikenKubun = dispTabSaikenKubun;
	}

	/**
	 * @return the dispTabSaikenMeisai
	 */
	public String getDispTabSaikenMeisai() {
		return dispTabSaikenMeisai;
	}

	/**
	 * @param dispTabSaikenMeisai the dispTabSaikenMeisai to set
	 */
	public void setDispTabSaikenMeisai(String dispTabSaikenMeisai) {
		this.dispTabSaikenMeisai = dispTabSaikenMeisai;
	}

	/**
	 * @return the dispTabTairyuSaiken
	 */
	public String getDispTabTairyuSaiken() {
		return dispTabTairyuSaiken;
	}

	/**
	 * @param dispTabTairyuSaiken the dispTabTairyuSaiken to set
	 */
	public void setDispTabTairyuSaiken(String dispTabTairyuSaiken) {
		this.dispTabTairyuSaiken = dispTabTairyuSaiken;
	}

	/**
	 * @return the dispTabTorihikiGaiyo
	 */
	public String getDispTabTorihikiGaiyo() {
		return dispTabTorihikiGaiyo;
	}

	/**
	 * @param dispTabTorihikiGaiyo the dispTabTorihikiGaiyo to set
	 */
	public void setDispTabTorihikiGaiyo(String dispTabTorihikiGaiyo) {
		this.dispTabTorihikiGaiyo = dispTabTorihikiGaiyo;
	}

	/**
	 * @return the ar_toroku_shounin
	 */
	public List<Map<String, String>> getAr_toroku_shounin() {
		return ar_toroku_shounin;
	}

	/**
	 * @param ar_toroku_shounin the ar_toroku_shounin to set
	 */
	public void setAr_toroku_shounin(List<Map<String, String>> ar_toroku_shounin) {
		this.ar_toroku_shounin = ar_toroku_shounin;
	}

	/**
	 * @return the jspPath
	 */
	public String getJspPath() {
		return jspPath;
	}

	/**
	 * @param jspPath the jspPath to set
	 */
	public void setJspPath(String jspPath) {
		this.jspPath = jspPath;
	}
}
