/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/11		SSC				案件No.D13493 改善対応
******************************************************************************/
package app.satei.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.struts.adapter.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;

/**
 * OC1103_査定_取引先区分 アクションフォームクラス
 * 
 */
public class KubunForm extends AppPagerActionForm {
	
    private LinkedHashMap saikenList;		//債権区分リスト
	private String kbnSaiken;				//債権区分(1:一般債権、2:貸倒懸念債権、3:破産更正債権)
    private LinkedHashMap toriSelectList;	//取引先区分選択リスト
	private String kbnToriSelect;			//取引先区分選択(1:一般債権、2:貸倒懸念債権、3:破産更正債権)
    private String currentTab;				//カレントタブ(1:正常先・要注意先、2:貸倒懸念債権、3:破産更正債権)
	private String kbnTorihiki;			//取引先区分判定(1:正常先、2:要注意先、3:貸倒懸念先、4:破産更正先)
    private String txtKbnTairyu;			//滞留区分
	private String txtKbnTairyuNm;			//滞留区分名称
    private String chkFlgSeijo;			//正常先チェック
	private String chkFlgYochui;			//要注意先チェック
	private String chkFlgTyoka;			//実質債務超過チェック(貸倒懸念先)
	private String chkFlgKanwa;			//弁済条件緩和チェック(貸倒懸念先)
	private String chkFlgEntai;			//1年以上延滞チェック(貸倒懸念先)
	private String chkFlgHasanho;			//破産法適用チェック(破産更生先)
	private String chkFlgKaishaho;			//会社法適用チェック(破産更生先)
	private String chkFlgKoseho;			//会社更生法適用チェック(破産更生先)
	private String chkFlgSaiseho;			//民事再生法適用チェック(破産更生先)	※国内版のみ
	private String chkFlgShobun;			//取引停止処分チェック(破産更生先)		※国内版のみ
	private String chkFlgSonota;			//その他チェック(破産更生先)
	private String txtValKonkyo;			//判定根拠(登録ポイント20)
	private String txtValJiyuu;			//判定事由(登録ポイント30)
	private String txtValKeii;				//発生経緯(登録ポイント40)
		
    //変数初期化
    public KubunForm() {
    	super.gamenId			= GS.OC1103;
        this.saikenList			= null;
        this.toriSelectList		= null;
        this.currentTab			= GS.EMPTY_CHARCTER;
        this.kbnTorihiki		= GS.EMPTY_CHARCTER;
        this.kbnSaiken			= GS.EMPTY_CHARCTER;
        this.kbnToriSelect		= GS.EMPTY_CHARCTER;
        this.txtKbnTairyu		= GS.EMPTY_CHARCTER;
        this.txtKbnTairyuNm		= GS.EMPTY_CHARCTER;
        this.chkFlgSeijo		= GS.OFF;
        this.chkFlgYochui		= GS.OFF;
        this.chkFlgTyoka		= GS.OFF;
        this.chkFlgKanwa		= GS.OFF;
        this.chkFlgEntai		= GS.OFF;
        this.chkFlgHasanho		= GS.OFF;
        this.chkFlgKaishaho		= GS.OFF;
        this.chkFlgKoseho		= GS.OFF;
        this.chkFlgSaiseho		= GS.OFF;
        this.chkFlgShobun		= GS.OFF;
        this.chkFlgSonota		= GS.OFF;
        this.txtValKonkyo		= GS.EMPTY_CHARCTER;
        this.txtValJiyuu		= GS.EMPTY_CHARCTER;
        this.txtValKeii			= GS.EMPTY_CHARCTER;
    }

    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    /**
	 * @return 1年以上延滞チェック(貸倒懸念先)
	 */
	public String getChkFlgEntai() {
		return chkFlgEntai;
	}
	public void setChkFlgEntai(String chkFlgEntai) {
		this.chkFlgEntai = chkFlgEntai;
	}
    /**
	 * @return 破産法適用チェック(破産更生先)
	 */
	public String getChkFlgHasanho() {
		return chkFlgHasanho;
	}
	public void setChkFlgHasanho(String chkFlgHasanho) {
		this.chkFlgHasanho = chkFlgHasanho;
	}
    /**
	 * @return 会社法適用チェック(破産更生先)
	 */
	public String getChkFlgKaishaho() {
		return chkFlgKaishaho;
	}
	public void setChkFlgKaishaho(String chkFlgKaishaho) {
		this.chkFlgKaishaho = chkFlgKaishaho;
	}
    /**
	 * @return 弁済条件緩和チェック(貸倒懸念先)
	 */
	public String getChkFlgKanwa() {
		return chkFlgKanwa;
	}
	public void setChkFlgKanwa(String chkFlgKanwa) {
		this.chkFlgKanwa = chkFlgKanwa;
	}
    /**
	 * @return 会社更生法適用チェック(破産更生先)
	 */
	public String getChkFlgKoseho() {
		return chkFlgKoseho;
	}
	public void setChkFlgKoseho(String chkFlgKoseho) {
		this.chkFlgKoseho = chkFlgKoseho;
	}
    /**
	 * @return 民事再生法適用チェック(破産更生先)
	 */
	public String getChkFlgSaiseho() {
		return chkFlgSaiseho;
	}
	public void setChkFlgSaiseho(String chkFlgSaiseho) {
		this.chkFlgSaiseho = chkFlgSaiseho;
	}
    /**
	 * @return 正常先チェック
	 */
	public String getChkFlgSeijo() {
		return chkFlgSeijo;
	}
	public void setChkFlgSeijo(String chkFlgSeijo) {
		this.chkFlgSeijo = chkFlgSeijo;
	}
    /**
	 * @return 取引停止処分チェック(破産更生先)
	 */
	public String getChkFlgShobun() {
		return chkFlgShobun;
	}
	public void setChkFlgShobun(String chkFlgShobun) {
		this.chkFlgShobun = chkFlgShobun;
	}
    /**
	 * @return その他チェック(破産更生先)
	 */
	public String getChkFlgSonota() {
		return chkFlgSonota;
	}
	public void setChkFlgSonota(String chkFlgSonota) {
		this.chkFlgSonota = chkFlgSonota;
	}
    /**
	 * @return 実質債務超過チェック(貸倒懸念先)
	 */
	public String getChkFlgTyoka() {
		return chkFlgTyoka;
	}
	public void setChkFlgTyoka(String chkFlgTyoka) {
		this.chkFlgTyoka = chkFlgTyoka;
	}
    /**
	 * @return 要注意先チェック
	 */
	public String getChkFlgYochui() {
		return chkFlgYochui;
	}
	public void setChkFlgYochui(String chkFlgYochui) {
		this.chkFlgYochui = chkFlgYochui;
	}
    /**
	 * @return カレントタブ
	 */
	public String getCurrentTab() {
		return currentTab;
	}
	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}
    /**
	 * @return 債権区分リスト
	 */
	public String getKbnSaiken() {
		return kbnSaiken;
	}
	public void setKbnSaiken(String kbnSaiken) {
		this.kbnSaiken = kbnSaiken;
	}
    /**
	 * @return 取引先区分判定(1:正常先、2:要注意先、3:貸倒懸念先、4:破産更正先)
	 */
	public String getKbnTorihiki() {
		return kbnTorihiki;
	}
	public void setKbnTorihiki(String kbnTorihiki) {
		this.kbnTorihiki = kbnTorihiki;
	}
    /**
	 * @return 債権区分リスト
	 */
	public LinkedHashMap getSaikenList() {
		return saikenList;
	}
	public void setSaikenList(LinkedHashMap saikenList) {
		this.saikenList = saikenList;
	}
    /**
	 * @return 滞留区分
	 */
	public String getTxtKbnTairyu() {
		return txtKbnTairyu;
	}
	public void setTxtKbnTairyu(String txtKbnTairyu) {
		this.txtKbnTairyu = txtKbnTairyu;
	}
	/**
	 * @return 滞留区分名称
	 */
	public String getTxtKbnTairyuNm() {
		return txtKbnTairyuNm;
	}
	public void setTxtKbnTairyuNm(String txtKbnTairyuNm) {
		this.txtKbnTairyuNm = txtKbnTairyuNm;
	}
    /**
	 * @return 取引先区分選択リスト
	 */
    public LinkedHashMap getToriSelectList() {
		return toriSelectList;
	}
	public void setToriSelectList(LinkedHashMap toriSelectList) {
		this.toriSelectList = toriSelectList;
	}
    /**
	 * @return 取引先区分選択
	 */
	public String getKbnToriSelect() {
		return kbnToriSelect;
	}
	public void setKbnToriSelect(String kbnToriSelect) {
		this.kbnToriSelect = kbnToriSelect;
	}
	/**
	 * @return 判定事由(登録ポイント30)
	 */
	public String getTxtValJiyuu() {
		return txtValJiyuu;
	}
	public void setTxtValJiyuu(String txtValJiyuu) {
		this.txtValJiyuu = this.getVal(txtValJiyuu);
	}
    /**
	 * @return 発生経緯(登録ポイント40)
	 */
	public String getTxtValKeii() {
		return txtValKeii;
	}
	public void setTxtValKeii(String txtValKeii) {
		this.txtValKeii = this.getVal(txtValKeii);
	}
    /**
	 * @return 判定根拠(登録ポイント20)
	 */
	public String getTxtValKonkyo() {
		return txtValKonkyo;
	}
	public void setTxtValKonkyo(String txtValKonkyo) {
		this.txtValKonkyo = this.getVal(txtValKonkyo);
	}
    //リセット
    public void reset(ActionMapping mapping, HttpServletRequest request){
        //チェックボックスオフ
        this.chkFlgSeijo		= GS.OFF;
        this.chkFlgYochui		= GS.OFF;
        this.chkFlgTyoka		= GS.OFF;
        this.chkFlgKanwa		= GS.OFF;
        this.chkFlgEntai		= GS.OFF;
        this.chkFlgHasanho		= GS.OFF;
        this.chkFlgKaishaho		= GS.OFF;
        this.chkFlgKoseho		= GS.OFF;
        this.chkFlgSaiseho		= GS.OFF;
        this.chkFlgShobun		= GS.OFF;
        this.chkFlgSonota		= GS.OFF;
    }

    /**
	 * NULLなら空文字変換、それ以外はトリム
	 * @param String
	 * 				ストリングオブジェクト
	 */	
	public String getVal(String val) {
	 	if(val == null){
	 		val = GS.EMPTY_CHARCTER;
	 	}else{
	 		val = val.trim();
	 	}
	 	return val;
	}
}