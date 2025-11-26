/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

/**
 * OZ6103_取引先区分判定照会タブ  アクションフォームクラス
 * 
 */
public class TorihikisakiKubunHanteiSyokaiForm extends AppPagerActionForm {
	
	private String tairyu_kbn;				//滞留区分
	private String tairyu_kbn_nm;			//滞留区分名称	
	private String seijo_chk;				//正常先チェック
	private String yochui_chk;				//要注意先チェック
	private String tyoka_chk;				//実質債務超過チェック
	private String kanwa_chk;				//弁済条件緩和チェック
	private String entai_chk;				//1年以上延滞チェック
	private String hasanho_chk;			//破産法適用チェック
	private String kaishaho_chk;			//会社法適用チェック
	private String koseho_chk;				//会社構成法適用チェック
	private String saiseho_chk;			//民事再生法適用チェック
	private String shobun_chk;				//取引停止処分チェック
	private String sonota_chk;				//その他チェック
	private String comment_val_20;			//判定根拠
	private String saiken_kbn;				//債権区分
	private String comment_val_30;			//判定事由
	private String comment_val_40;			//発生経緯
	private String torihikisaki_kbn;		//取引先区分
	private String torihikisaki_kbn_nm;	//取引先区分名称
		  
    // 変数初期化
    public TorihikisakiKubunHanteiSyokaiForm() {
    	
    	super.gamenId = GS.OZ6103;
    	
    	this.tairyu_kbn = GS.EMPTY_CHARCTER;
    	this.tairyu_kbn_nm = GS.EMPTY_CHARCTER;
    	this.seijo_chk = GS.EMPTY_CHARCTER;
    	this.yochui_chk = GS.EMPTY_CHARCTER;
    	this.tyoka_chk = GS.EMPTY_CHARCTER;
    	this.kanwa_chk = GS.EMPTY_CHARCTER;
    	this.entai_chk = GS.EMPTY_CHARCTER;
    	this.hasanho_chk = GS.EMPTY_CHARCTER;
    	this.kaishaho_chk = GS.EMPTY_CHARCTER;
    	this.koseho_chk = GS.EMPTY_CHARCTER;
    	this.saiseho_chk = GS.EMPTY_CHARCTER;
    	this.shobun_chk = GS.EMPTY_CHARCTER;
    	this.sonota_chk = GS.EMPTY_CHARCTER;
    	this.comment_val_20 = GS.EMPTY_CHARCTER;
    	this.saiken_kbn = GS.EMPTY_CHARCTER;
    	this.comment_val_30 = GS.EMPTY_CHARCTER;
    	this.comment_val_40 = GS.EMPTY_CHARCTER;
    	this.torihikisaki_kbn = GS.EMPTY_CHARCTER;
    	this.torihikisaki_kbn_nm = GS.EMPTY_CHARCTER;
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	
    // アクセスメソッド   
	//滞留区分
	public String getTairyu_kbn() {
		return tairyu_kbn;
	}
	public void setTairyu_kbn(String tairyu_kbn) {
		this.tairyu_kbn = tairyu_kbn;
	}
	//滞留区分名称
	public String getTairyu_kbn_nm() {
		return tairyu_kbn_nm;
	}
	public void setTairyu_kbn_nm(String tairyu_kbn_nm) {
		this.tairyu_kbn_nm = tairyu_kbn_nm;
	}
	//正常先チェック
	public String getSeijo_chk() {
		return seijo_chk;
	}
	public void setSeijo_chk(String seijo_chk) {
		this.seijo_chk = seijo_chk;
	}
	//要注意先チェック
	public String getYochui_chk() {
		return yochui_chk;
	}
	public void seYochui_chk(String yochui_chk) {
		this.yochui_chk = yochui_chk;
	}
	///実質債務超過チェック
	public String getTyoka_chk() {
		return tyoka_chk;
	}
	public void setTyoka_chk(String tyoka_chk) {
		this.tyoka_chk = tyoka_chk;
	}
	//弁済条件緩和チェック
	public String getKanwa_chk() {
		return kanwa_chk;
	}
	public void setKanwa_chk(String kanwa_chk) {
		this.kanwa_chk = kanwa_chk;
	}
	//1年以上延滞チェック
	public String getEntai_chk() {
		return entai_chk;
	}
	public void setEntai_chk(String entai_chk) {
		this.entai_chk = entai_chk;
	}
	//破産法適用チェック
	public String getHasanho_chk() {
		return hasanho_chk;
	}
	public void setHasanho_chk(String hasanho_chk) {
		this.hasanho_chk = hasanho_chk;
	}
	//会社法適用チェック
	public String getKaishaho_chk() {
		return kaishaho_chk;
	}
	public void setKaishaho_chk(String kaishaho_chk) {
		this.kaishaho_chk = kaishaho_chk;
	}
	///会社構成法適用チェック
	public String getKoseho_chk() {
		return koseho_chk;
	}
	public void setKoseho_chk(String koseho_chk) {
		this.koseho_chk = koseho_chk;
	}
	///民事再生法適用チェック
	public String getSaiseho_chk() {
		return saiseho_chk;
	}
	public void setSaiseho_chk(String saiseho_chk) {
		this.saiseho_chk = saiseho_chk;
	}
	//取引停止処分チェック
	public String getShobun_chk() {
		return shobun_chk;
	}
	public void setShobun_chk(String shobun_chk) {
		this.shobun_chk = shobun_chk;
	}
	//その他チェック
	public String getSonota_chk() {
		return sonota_chk;
	}
	public void setSonota_chk(String sonota_chk) {
		this.sonota_chk = sonota_chk;
	}
	//判定根拠		
	public String getComment_val_20() {
		return comment_val_20;
	}
	public void setComment_val_20(String comment_val_20) {
		this.comment_val_20 = comment_val_20;
	}
	//債権区分	
	public String getSaiken_kbn() {
		return saiken_kbn;
	}
	public void setSaiken_kbn(String saiken_kbn) {
		this.saiken_kbn = saiken_kbn;
	}
	///判定事由			
	public String getComment_val_30() {
		return comment_val_30;
	}
	public void setComment_val_30(String comment_val_30) {
		this.comment_val_30 = comment_val_30;
	}
	//発生経緯		
	public String getComment_val_40() {
		return comment_val_40;
	}
	public void setComment_val_40(String comment_val_40) {
		this.comment_val_40 = comment_val_40;
	}
	//取引先区分				
	public String getTorihikisaki_kbn() {
		return torihikisaki_kbn;
	}
	public void setTorihikisaki_kbn(String torihikisaki_kbn) {
		this.torihikisaki_kbn = torihikisaki_kbn;
	}
	//取引先区分名称				
	public String getTorihikisaki_kbn_nm() {
		return torihikisaki_kbn_nm;
	}
	public void setTorihikisaki_kbn_nm(String torihikisaki_kbn_nm) {
		this.torihikisaki_kbn_nm = torihikisaki_kbn_nm;
	}
}