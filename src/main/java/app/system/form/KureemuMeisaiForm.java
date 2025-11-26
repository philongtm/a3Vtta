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
import java.util.LinkedHashMap;

/**
 * OS3102 クレーム債権再設定_明細一覧 アクションフォームクラス <br>
 * 
 */
public class KureemuMeisaiForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
    private LinkedHashMap ar_show; 					// 表示件数セレクトボックス用配列
    private LinkedHashMap ar_tanto; 					// 承認担当者セレクトボックス用配列
    private String syonin_tanto; 						// 承認担当者
    private String flg_disp_sashimodoshi;				// 差戻ボタン表示フラグ
    private String flg_disp_upload_all;				// 一括取り込みボタン表示フラグ
    private String flg_disp_tanto; 					// 承認担当者セレクトボックス表示フラグ
    private String flg_disp_comment; 					// コメントリンク表示フラグ
    private String tuuka_cd; 							// 通貨
    private String anken_no; 							// リンククリックされた滞留判定案件No.
    private String tairyuhantei; 						// リンククリックされた滞留判定
    private String ex_tairyuhantei; 					// リンククリックされた滞留判定
    private String ji_jishi_phase;						// 次実施フェーズ
    private String ji_kaishi_status;					// 次開始ステータス
    private int id; 									// リンククリックされた明細.id
    private String toroku_anken_no;					
    
    /**
     * 変数初期化 <br>
     */
    public KureemuMeisaiForm() {
        
        super.gamenId = GS.OS3102;
        
        this.ar_show = null;
        this.ar_tanto = null;
        this.syonin_tanto = GS.EMPTY_CHARCTER;
        this.flg_disp_sashimodoshi = GS.OFF;
        this.flg_disp_upload_all = GS.OFF;
        this.flg_disp_tanto = GS.OFF;
        this.flg_disp_comment = GS.OFF;
        this.tuuka_cd = GS.EMPTY_CHARCTER;
        this.anken_no = GS.EMPTY_CHARCTER;
        this.tairyuhantei = GS.EMPTY_CHARCTER;
        this.ex_tairyuhantei = GS.EMPTY_CHARCTER;
        this.ji_jishi_phase = GS.EMPTY_CHARCTER;
        this.ji_kaishi_status = GS.EMPTY_CHARCTER;
        this.toroku_anken_no = GS.EMPTY_CHARCTER;
        this.id = 0;
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
     * @return the syonin_tanto
     */
    public String getSyonin_tanto() {
        return syonin_tanto;
    }

    /**
     * @param syonin_tanto the syonin_tanto to set
     */
    public void setSyonin_tanto(String syonin_tanto) {
        this.syonin_tanto = syonin_tanto;
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
     * @return the ar_tanto
     */
    public LinkedHashMap getAr_tanto() {
        return ar_tanto;
    }

    /**
     * @param ar_tanto the ar_tanto to set
     */
    public void setAr_tanto(LinkedHashMap ar_tanto) {
        this.ar_tanto = ar_tanto;
    }

    /**
     * @return the flg_disp_sashimodoshi
     */
    public String getFlg_disp_sashimodoshi() {
        return flg_disp_sashimodoshi;
    }

    /**
     * @param flg_disp_sashimodoshi the flg_disp_sashimodoshi to set
     */
    public void setFlg_disp_sashimodoshi(String flg_disp_sashimodoshi) {
        this.flg_disp_sashimodoshi = flg_disp_sashimodoshi;
    }

    /**
     * @return the flg_disp_upload_all
     */
    public String getFlg_disp_upload_all() {
        return flg_disp_upload_all;
    }

    /**
     * @param flg_disp_upload_all the flg_disp_upload_all to set
     */
    public void setFlg_disp_upload_all(String flg_disp_upload_all) {
        this.flg_disp_upload_all = flg_disp_upload_all;
    }

    /**
     * @return the flg_disp_tanto
     */
    public String getFlg_disp_tanto() {
        return flg_disp_tanto;
    }

    /**
     * @param flg_disp_tanto the flg_disp_tanto to set
     */
    public void setFlg_disp_tanto(String flg_disp_tanto) {
        this.flg_disp_tanto = flg_disp_tanto;
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
     * @return the ex_tairyuhantei
     */
    public String getEx_tairyuhantei() {
        return ex_tairyuhantei;
    }

    /**
     * @param ex_tairyuhantei the ex_tairyuhantei to set
     */
    public void setEx_tairyuhantei(String ex_tairyuhantei) {
        this.ex_tairyuhantei = ex_tairyuhantei;
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
     * @return the tairyuhantei
     */
    public String getTairyuhantei() {
        return tairyuhantei;
    }

    /**
     * @param tairyuhantei the tairyuhantei to set
     */
    public void setTairyuhantei(String tairyuhantei) {
        this.tairyuhantei = tairyuhantei;
    }

    /**
     * @return the flg_disp_comment
     */
    public String getFlg_disp_comment() {
        return flg_disp_comment;
    }

    /**
     * @param flg_disp_comment the flg_disp_comment to set
     */
    public void setFlg_disp_comment(String flg_disp_comment) {
        this.flg_disp_comment = flg_disp_comment;
    }

	/**
	 * @return the tuuka_cd
	 */
	public String getTuuka_cd() {
		return tuuka_cd;
	}

	/**
	 * @param tuuka_cd the tuuka_cd to set
	 */
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
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
	 * @return the toroku_anken_no
	 */
	public String getToroku_anken_no() {
		return toroku_anken_no;
	}

	/**
	 * @param toroku_anken_no the toroku_anken_no to set
	 */
	public void setToroku_anken_no(String toroku_anken_no) {
		this.toroku_anken_no = toroku_anken_no;
	}
}