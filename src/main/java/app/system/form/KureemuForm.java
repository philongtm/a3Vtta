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
 * OS3101 クレーム債権再設定_対象先一覧 アクションフォームクラス
 * 
 */
public class KureemuForm extends AppPagerActionForm {

    /**  */
	private static final long serialVersionUID = 1L;
	
	private String tanto;						// 自担当分/汎用２ラジオボタン(1:自担当分、2:汎用２)
    private String sateiki;					// 査定期
    private LinkedHashMap ar_sateiki;			// 査定期セレクトボックス用配列
    private String sort_item;					// ソート項目
    private LinkedHashMap ar_sort_item;		// ソート項目セレクトボックス用配列
    private String sort_order;					// 整列方向
    private LinkedHashMap ar_sort_order;		// 整列方向セレクトボックス用配列
    private LinkedHashMap ar_show;				// 表示件数セレクトボックス用配列
    private String sansyo_phase;				// 参照フェーズ
    private String anken_no;					// リンククリックされた勘定先の明細.滞留判定案件No.
    private int id;							// リンククリックされた勘定先の明細.id
    private int kuremusaiken_misyori;			// クレーム債権.未処理件数
    private int kuremusaiken_syorityu;			// クレーム債権.処理中件数
    private int kuremusaiken_syoninmati;		// クレーム債権.承認待件数
    private String kuremusaiken_kanryo;			// クレーム債権.完了件数
      
    // 変数初期化
    public KureemuForm() {
    	super.gamenId = GS.OS3101;
        this.tanto = GS.EMPTY_CHARCTER;
        this.sateiki = GS.EMPTY_CHARCTER;
        this.ar_sateiki = null;
        this.sort_item = GS.EMPTY_CHARCTER;
        this.ar_sort_item = null;
        this.sort_order = GS.EMPTY_CHARCTER;
        this.ar_sort_order = null;
        this.ar_show = null;
        this.sansyo_phase = GS.EMPTY_CHARCTER;
        this.anken_no = GS.EMPTY_CHARCTER;
        this.id = 0;
        this.kuremusaiken_misyori = 0;
        this.kuremusaiken_syorityu = 0;
        this.kuremusaiken_syoninmati = 0;
        this.kuremusaiken_kanryo = GS.EMPTY_CHARCTER;
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
    //自担当分/汎用２ラジオボタン
    public String getTanto() {
        return this.tanto;
    }
    public void setTanto(String tanto) {
        this.tanto = tanto;
    }    
	//査定期
	public String getSateiki() {
		return sateiki;
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
	//ソート項目
	public String getSort_item() {
		return sort_item;
	}
	public void setSort_item(String sort_item) {
		this.sort_item = sort_item;
	}
	//ソート項目配列
	public LinkedHashMap getAr_sort_item() {
		return ar_sort_item;
	}
	public void setAr_sort_item(LinkedHashMap ar_sort_item) {
		this.ar_sort_item = ar_sort_item;
	}
	//整列方向
	public String getSort_order() {
		return sort_order;
	}
	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}
	//整列方向配列
	public LinkedHashMap getAr_sort_order() {
		return ar_sort_order;
	}
	public void setAr_sort_order(LinkedHashMap ar_sort_order) {
		this.ar_sort_order = ar_sort_order;
	}	
	//表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	//参照フェーズ
	public String getSansyo_phase() {
		return sansyo_phase;
	}
	public void setSansyo_phase(String sansyo_phase) {
		this.sansyo_phase = sansyo_phase;
	}
	//滞留判定案件No.
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the kuremusaiken_misyori
	 */
	public int getKuremusaiken_misyori() {
		return kuremusaiken_misyori;
	}

	/**
	 * @param kuremusaiken_misyori the kuremusaiken_misyori to set
	 */
	public void setKuremusaiken_misyori(int kuremusaiken_misyori) {
		this.kuremusaiken_misyori = kuremusaiken_misyori;
	}

	/**
	 * @return the kuremusaiken_syoninmati
	 */
	public int getKuremusaiken_syoninmati() {
		return kuremusaiken_syoninmati;
	}

	/**
	 * @param kuremusaiken_syoninmati the kuremusaiken_syoninmati to set
	 */
	public void setKuremusaiken_syoninmati(int kuremusaiken_syoninmati) {
		this.kuremusaiken_syoninmati = kuremusaiken_syoninmati;
	}

	/**
	 * @return the kuremusaiken_syorityu
	 */
	public int getKuremusaiken_syorityu() {
		return kuremusaiken_syorityu;
	}

	/**
	 * @param kuremusaiken_syorityu the kuremusaiken_syorityu to set
	 */
	public void setKuremusaiken_syorityu(int kuremusaiken_syorityu) {
		this.kuremusaiken_syorityu = kuremusaiken_syorityu;
	}

	/**
	 * @return the kuremusaiken_kanryo
	 */
	public String getKuremusaiken_kanryo() {
		return kuremusaiken_kanryo;
	}

	/**
	 * @param kuremusaiken_kanryo the kuremusaiken_kanryo to set
	 */
	public void setKuremusaiken_kanryo(String kuremusaiken_kanryo) {
		this.kuremusaiken_kanryo = kuremusaiken_kanryo;
	}
}