/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OB2102_対象先選定_選定先詳細 アクションフォームクラス
 * 
 */
public class SenteisyosaiForm extends AppPagerActionForm {

    private boolean sasimodoshi_flg;		// 差戻ボタン表示フラグ
    private boolean tyusyutujiyu_flg;		// 抽出事由セレクトボックス表示フラグ
    private String tabValue;				// タブ判定用
    private String taisyogai_kbn;			// 対象外区分
    private LinkedHashMap ar_taisyogai;	// 対象外区分セレクトボックス用配列
    private String syonin_tanto;			// 承認担当者
    private LinkedHashMap ar_syonin_tanto;	// 承認担当者セレクトボックス用配列
    private String tyusyutu_jiyu;			// 抽出事由
    private LinkedHashMap ar_tyusyutu_jiyu;// 抽出事由セレクトボックス用配列
    private String comment;				// 対象外・追加コメント
      
    // 変数初期化
    public SenteisyosaiForm() {
    	super.gamenId = GS.OB2102;
        this.sasimodoshi_flg = false;
        this.tyusyutujiyu_flg = false;
        this.tabValue = "1";
        this.taisyogai_kbn = GS.EMPTY_CHARCTER;
        this.ar_taisyogai = null;
        this.syonin_tanto = GS.EMPTY_CHARCTER;
        this.ar_syonin_tanto = null;
        this.tyusyutu_jiyu = GS.EMPTY_CHARCTER;
        this.ar_tyusyutu_jiyu = null;
        this.comment = GS.EMPTY_CHARCTER;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
    // アクセスメソッド   
    //差戻ボタン表示フラグ
    public boolean getSasimodoshi_flg() {
        return this.sasimodoshi_flg;
    }
    public void setSasimodoshi_flg(boolean sasimodoshi_flg) {
        this.sasimodoshi_flg = sasimodoshi_flg;
    }    
    //抽出事由セレクトボックス表示フラグ
    public boolean getTyusyutujiyu_flg() {
        return this.tyusyutujiyu_flg;
    }
    public void setTyusyutujiyu_flg(boolean tyusyutujiyu_flg) {
        this.tyusyutujiyu_flg = tyusyutujiyu_flg;
    }    
	//タブ判定用
	public String getTabValue() {
		return tabValue;
	}
	public void setTabValue(String tabValue) {
		this.tabValue = tabValue;
	}
	//対象外区分
	public String getTaisyogai_kbn() {
		return taisyogai_kbn;
	}
	public void setTaisyogai_kbn(String taisyogai_kbn) {
		this.taisyogai_kbn = taisyogai_kbn;
	}
    //対象外区分配列
	public LinkedHashMap getAr_taisyogai() {
		return ar_taisyogai;
	}
	public void setAr_taisyogai(LinkedHashMap ar_taisyogai) {
		this.ar_taisyogai = ar_taisyogai;
	}
	//承認担当者
	public String getSyonin_tanto() {
		return syonin_tanto;
	}
	public void setSyonin_tanto(String syonin_tanto) {
		this.syonin_tanto = syonin_tanto;
	}
    //承認担当者配列
	public LinkedHashMap getAr_syonin_tanto() {
		return ar_syonin_tanto;
	}
	public void setAr_syonin_tanto(LinkedHashMap ar_syonin_tanto) {
		this.ar_syonin_tanto = ar_syonin_tanto;
	}
	//抽出事由
	public String getTyusyutu_jiyu() {
		return tyusyutu_jiyu;
	}
	public void setTyusyutu_jiyu(String tyusyutu_jiyu) {
		this.tyusyutu_jiyu = tyusyutu_jiyu;
	}
    //抽出事由配列
	public LinkedHashMap getAr_tyusyutu_jiyu() {
		return ar_tyusyutu_jiyu;
	}
	public void setAr_tyusyutu_jiyu(LinkedHashMap ar_tyusyutu_jiyu) {
		this.ar_tyusyutu_jiyu = ar_tyusyutu_jiyu;
	}
	//対象外・追加コメント
	public String getComment() {
		return Function.trim(comment);
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
}