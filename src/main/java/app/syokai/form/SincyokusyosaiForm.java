/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/2/13		BJN				新規作成
002		2009/11/20		SSC				課題No.152 代行取戻対応
003		2015/03/18		SSC				BJ201408049 IA化対応時の機能改善
******************************************************************************/
package app.syokai.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.Map;

/**
 * OS6104_進捗状況詳細 アクションフォームクラス <br>
 */
public class SincyokusyosaiForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L;
    private String torimodoshi_fuka_flg;		// 取戻不可フラグ
    private String upd_user_id_flg;			// 更新ユーザID判定
    private String phase_hantei;				// フェーズ判定
    private String karento_tab;				// カレントタブ
    private String mail_gamen_id;				// メール用画面ID
    private String kanjo_cd;					// 勘定先CD
    private String kanjo_nm;					// 勘定先名称
    private String sintyoku;					// 現在進捗
    // 課題No.152
    // 追加開始
    private Map<String,String> torimodoshiMoto;// 取戻元マップ
    // 追加完了

	//督促メール
    private String mailSendFlg;					// メール送信フラグ
	private String haishin_Dt;					// 最新の配信日時
	private String hozon_Dt;					// 最新の保存日時
	private String sendBtnFlg;					// 送信ボタンの表示/非表示フラグ

    /**
     * 変数初期化 <br>
     */
    public SincyokusyosaiForm() {
        super.gamenId = GS.OS6104;
        this.torimodoshi_fuka_flg = GS.EMPTY_CHARCTER;
        this.upd_user_id_flg = GS.EMPTY_CHARCTER;
        this.phase_hantei = GS.EMPTY_CHARCTER;
        this.karento_tab = GS.EMPTY_CHARCTER;
        this.mail_gamen_id = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.sintyoku = GS.EMPTY_CHARCTER;
        // 課題No.152
        // 追加開始
        this.torimodoshiMoto = null;
        // 追加完了
		//督促メール
		this.haishin_Dt = GS.EMPTY_CHARCTER;
		this.hozon_Dt = GS.EMPTY_CHARCTER;
		this.mailSendFlg = GS.EMPTY_CHARCTER;
		this.sendBtnFlg = GS.EMPTY_CHARCTER;

    }

	/**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

    // 課題No.152
    // 追加開始
    /**
     * 取戻元マップ <br>
     *
     * @return 取戻元マップを返します。
     */
    public Map<String, String> getTorimodoshiMoto() {
		return torimodoshiMoto;
	}
    /**
     * 取戻元マップ <br>
     *
     */
	public void setTorimodoshiMoto(Map<String, String> torimodoshiMoto) {
		this.torimodoshiMoto = torimodoshiMoto;
	}
    // 追加完了

    /**
     * 取戻不可フラグ <br>
     *
     * @return the torimodoshi_fuka_flg
     */
    public String getTorimodoshi_fuka_flg() {
        return torimodoshi_fuka_flg;
    }

    /**
     * 取戻不可フラグ<br>
     *
     * @param torimodoshi_fuka_flg the torimodoshi_fuka_flg to set
     */
    public void setTorimodoshi_fuka_flg(String torimodoshi_fuka_flg) {
        this.torimodoshi_fuka_flg = torimodoshi_fuka_flg;
    }

    /**
     * 更新ユーザID判定<br>
     *
     * @return the upd_user_id_flg
     */
    public String getUpd_user_id_flg() {
        return upd_user_id_flg;
    }

    /**
     * 更新ユーザID判定<br>
     *
     * @param upd_user_id_flg the upd_user_id_flg to set
     */
    public void setUpd_user_id_flg(String upd_user_id_flg) {
        this.upd_user_id_flg = upd_user_id_flg;
    }

    /**
     * フェーズ判定<br>
     *
     * @return the phase_hantei
     */
    public String getPhase_hantei() {
        return phase_hantei;
    }

    /**
     * フェーズ判定<br>
     *
     * @param phase_hantei the phase_hantei to set
     */
    public void setPhase_hantei(String phase_hantei) {
        this.phase_hantei = phase_hantei;
    }

    /**
     * カレントタブ<br>
     *
     * @return the karento_tab
     */
    public String getKarento_tab() {
        return karento_tab;
    }

    /**
     * カレントタブ<br>
     *
     * @param karento_tab the karento_tab to set
     */
    public void setKarento_tab(String karento_tab) {
        this.karento_tab = karento_tab;
    }

    /**
     * メール用画面ID<br>
     *
     * @return the karento_tab
     */
    public String getMail_gamen_id() {
        return mail_gamen_id;
    }

    /**
     * メール用画面ID<br>
     *
     * @param mail_gamen_id the mail_gamen_id to set
     */
    public void setMail_gamen_id(String mail_gamen_id) {
        this.mail_gamen_id = mail_gamen_id;
    }

    /**
     *
     * 勘定先CD <br>
     *
     * @return
     */
	public String getKanjo_cd() {
		return kanjo_cd;
	}
	/**
	 *
	 * 勘定先CD <br>
	 *
	 * @param kanjo_cd
	 */
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}
	/**
	 *
	 * 勘定先名称 <br>
	 *
	 * @return
	 */
	public String getKanjo_nm() {
		return kanjo_nm;
	}
	/**
	 *
	 * 勘定先名称 <br>
	 *
	 * @param kanjo_nm
	 */
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}
	/**
	 *
	 * 現在進捗 <br>
	 *
	 * @return
	 */
	public String getSintyoku() {
		return sintyoku;
	}
	/**
	 *
	 * 現在進捗 <br>
	 *
	 * @param sintyoku
	 */
	public void setSintyoku(String sintyoku) {
		this.sintyoku = sintyoku;
	}

	/**
	 *
	 * メール送信フラグ <br>
	 *
	 * @return mailSendFlg
	 */
	public String getMailSendFlg() {
		return mailSendFlg;
	}

	/**
	 *
	 * メール送信フラグ <br>
	 *
	 * @param mailSendFlg
	 */
	public void setMailSendFlg(String mailSendFlg) {
		this.mailSendFlg = mailSendFlg;
	}
	/**
	 * 最新の配信日時
	 *
	 * @return
	 */
	public String getHaishin_Dt() {
		return haishin_Dt;
	}

	/**
	 * 最新の配信日時
	 *
	 * @param haishin_Dt
	 */
	public void setHaishin_Dt(String haishin_Dt) {
		this.haishin_Dt = haishin_Dt;
	}

	/**
	 * 最新の保存日時
	 *
	 * @return
	 */
	public String getHozon_Dt() {
		return hozon_Dt;
	}

	/**
	 * 最新の保存日時
	 *
	 * @param hozon_Dt
	 */
	public void setHozon_Dt(String hozon_Dt) {
		this.hozon_Dt = hozon_Dt;
	}

	/**
	 * 送信ボタンフラグ
	 * @return btnFlg
	 */
	public String getSendBtnFlg() {
		return sendBtnFlg;
	}

	/**
	 * 送信ボタンフラグ
	 * @param btnFlg セットする btnFlg
	 */
	public void setSendBtnFlg(String sendBtnFlg) {
		this.sendBtnFlg = sendBtnFlg;
	}
}
