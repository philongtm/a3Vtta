/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/16		SSC				課題No.114 検索値退避処理 
******************************************************************************/
package app.tairyu.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OB2103_対象先選定_追加対象先選択 アクションフォームクラス
 * 
 */
public class SenteituikaForm extends AppPagerActionForm {

    private String anken_no;				// 案件No
    private String sateiki;				// 査定期
    private LinkedHashMap ar_sateiki;		// 査定期セレクトボックス用配列
    private String taisho_ym;				// 対象年月
    private String kanjo_cd;				// 勘定先CD
    private String kanjo_nm;				// 勘定先名称
    private String hanyou2;				// 汎用２
    private LinkedHashMap ar_hanyou2;		// 汎用２セレクトボックス用配列
    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private int id;						// リンククリックされた勘定先の明細.id
	//課題No.114
	//追加開始
    private String kensaku_sateiki;				// 検索用査定期
	private String kensaku_kanjo_cd;				// 検索用勘定先CD
    private String kensaku_kanjo_nm;				// 検索用勘定先名称
    private String kensaku_hanyou2;				// 検索用汎用２
	//追加完了
      
    // 変数初期化
    public SenteituikaForm() {
    	super.gamenId = GS.OB2103;
        this.anken_no = GS.EMPTY_CHARCTER;
        this.sateiki = GS.EMPTY_CHARCTER;
        this.ar_sateiki = null;
        this.taisho_ym = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.ar_hanyou2 = null;
        this.ar_show = null;
        this.id = 0;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    	//課題No.114
    	//追加開始
        this.kensaku_sateiki = GS.EMPTY_CHARCTER;;
        this.kensaku_kanjo_cd = GS.EMPTY_CHARCTER;
        this.kensaku_kanjo_nm = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou2 = GS.EMPTY_CHARCTER;;
    	//追加完了
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	
    // アクセスメソッド
    //案件No
    public String getAnken_no() {
        return this.anken_no;
    }
    public void setAnken_no(String anken_no) {
        this.anken_no = anken_no;
    }    
    //査定期
    public String getSateiki() {
        return this.sateiki;
    }
    public void setSateiki(String sateiki) {
        this.sateiki = sateiki;
    }    
	//査定期配列
	public LinkedHashMap getAr_sateiki() {
		return ar_sateiki;
	}
	public void setAr_sateiki(LinkedHashMap ar_sateiki) {
		this.ar_sateiki = ar_sateiki;
	}
    //対象年月
    public String getTaisho_ym() {
        return this.taisho_ym;
    }
    public void setTaisho_ym(String taisho_ym) {
        this.taisho_ym = taisho_ym;
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
	//汎用２配列
	public LinkedHashMap getAr_hanyou2() {
		return ar_hanyou2;
	}
	public void setAr_hanyou2(LinkedHashMap ar_hanyou2) {
		this.ar_hanyou2 = ar_hanyou2;
	}
	//表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//課題No.114
	//追加開始
    public String getKensaku_sateiki() {
		return kensaku_sateiki;
	}
	public void setKensaku_sateiki(String kensaku_sateiki) {
		this.kensaku_sateiki = kensaku_sateiki;
	}
	public String getKensaku_kanjo_cd() {
		return kensaku_kanjo_cd;
	}
	public void setKensaku_kanjo_cd(String kensaku_kanjo_cd) {
		this.kensaku_kanjo_cd = kensaku_kanjo_cd;
	}
	public String getKensaku_kanjo_nm() {
		return kensaku_kanjo_nm;
	}
	public void setKensaku_kanjo_nm(String kensaku_kanjo_nm) {
		this.kensaku_kanjo_nm = kensaku_kanjo_nm;
	}
	public String getKensaku_hanyou2() {
		return kensaku_hanyou2;
	}
	public void setKensaku_hanyou2(String kensaku_hanyou2) {
		this.kensaku_hanyou2 = kensaku_hanyou2;
	}
	//追加完了
}