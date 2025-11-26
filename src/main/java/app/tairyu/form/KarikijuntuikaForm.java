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
 * OB2105_対象先選定_仮基準査定選択 アクションフォームクラス
 * 
 */
public class KarikijuntuikaForm extends AppPagerActionForm {

    private String sateiki;				// 査定期
    private String sateiki_hyouji;			// 査定期表示用
	private String kanjo_cd;				// 勘定先CD
    private String sateikaisya_cd;			// 査定会社CD
	private String kanjo_nm;				// 勘定先名称
	private String ankenNo;				// 案件No.
	private String phase;					// フェーズ
    private String hanyou2;				// 汎用２
    private String systemKbn;				// 基幹システム区分
    private String sasiTenFlg;				// 差戻転送フラグ
    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
	private int id;						// リンククリックされた勘定先の明細.id
      
    // 変数初期化
    public KarikijuntuikaForm() {
    	super.gamenId = GS.OB2105;
        this.ankenNo = GS.EMPTY_CHARCTER;
        this.phase = GS.EMPTY_CHARCTER;
        this.sateikaisya_cd = GS.EMPTY_CHARCTER;
        this.sateiki = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.sasiTenFlg = GS.EMPTY_CHARCTER;
        this.ar_show = null;
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
	
    //表示件数
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
    //差戻転送フラグ
	public String getSasiTenFlg() {
		return sasiTenFlg;
	}
	public void setSasiTenFlg(String sasiTenFlg) {
		this.sasiTenFlg = sasiTenFlg;
	}
    //案件No.
	public String getAnkenNo() {
		return ankenNo;
	}
	public void setAnkenNo(String ankenNo) {
		this.ankenNo = ankenNo;
	}
    //フェーズ
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
    //基幹システム区分
    public String getSystemKbn() {
		return systemKbn;
	}
	public void setSystemKbn(String systemKbn) {
		this.systemKbn = systemKbn;
	}
    //査定会社CD
    public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}
    //査定期表示用
    public String getSateiki_hyouji() {
		return sateiki_hyouji;
	}
	public void setSateiki_hyouji(String sateiki_hyouji) {
		this.sateiki_hyouji = sateiki_hyouji;
	}
    //査定期
    public String getSateiki() {
        return this.sateiki;
    }
    public void setSateiki(String sateiki) {
        this.sateiki = sateiki;
    }    
    //勘定先CD
    public String getKanjo_cd() {
        return Function.trim(this.kanjo_cd);
    }
    public void setKanjo_cd(String kanjo_cd) {
        this.kanjo_cd = kanjo_cd;
    }    
    //勘定先名称
    public String getKanjo_nm() {
        return Function.trim(this.kanjo_nm);
    }
    public void setKanjo_nm(String kanjo_nm) {
        this.kanjo_nm = kanjo_nm;
    }    
    //汎用２
    public String getHanyou2() {
        return this.hanyou2;
    }
    public void setHanyou2(String hanyou2) {
        this.hanyou2 = hanyou2;
    }    
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}