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

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OB1101_実質滞留債権判定_対象先一覧 アクションフォームクラス <br>
 */
public class IchiranForm extends AppPagerActionForm {

	private static final long serialVersionUID = 1L;
	private String tanto;					// 自担当分/汎用２ラジオボタン(1:自担当分、2:汎用２)
    private String sateiki;				// 査定期
    private LinkedHashMap ar_sateiki;		// 査定期セレクトボックス用配列
    private String sort_item;				// ソート項目
    private LinkedHashMap ar_sort_item;	// ソート項目セレクトボックス用配列
    private String sort_order;				// 整列方向
    private LinkedHashMap ar_sort_order;	// 整列方向セレクトボックス用配列
    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private String sansyo_phase;			// 参照フェーズ
    private String anken_no;				// リンククリックされた勘定先の明細.滞留判定案件No.
    private int id;						// リンククリックされた勘定先の明細.id
    private int hantei_misyori;			// 判定登録未処理件数
    private int hantei_syorityu;			// 判定登録処理中件数
    private int hantei_syoninmati;			// 判定登録承認待ち件数
    private int hantei_kanryo;				// 判定登録完了件数
    private int kensyo_misyori;			// 判定検証未処理件数
    private int kensyo_syorityu;			// 判定検証処理中件数
    private int kensyo_syoninmati;			// 判定検証承認待ち件数   
    private int kensyo_kanryo;				// 判定検証完了件数   
      
    /**
     * 変数初期化 <br>
     */
    public IchiranForm() {
    	super.gamenId = GS.OB1101;
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
        this.hantei_misyori = 0;
        this.hantei_syorityu = 0;
        this.hantei_syoninmati = 0;
        this.hantei_kanryo = 0;
        this.kensyo_misyori = 0;
        this.kensyo_syorityu = 0;
        this.kensyo_syoninmati = 0;
        this.kensyo_kanryo = 0;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    }

	/**
	 * 画面IDを戻します。 <br>
	 * 
	 * @return 画面ID
	 */
	public String toString(){
		return super.gamenId;
	}

	// アクセスメソッド   
    /**
     * 自担当分/汎用２ラジオボタン <br>
     * 
     * @return 自担当分/汎用２ラジオボタン
     */
    public String getTanto() {
        return this.tanto;
    }
    
    /**
     * 自担当分/汎用２ラジオボタン <br>
     * 
     * @param tanto
     */
    public void setTanto(String tanto) {
        this.tanto = tanto;
    }    

    /**
	 * 査定期 <br>
	 * 
	 * @return 査定期
	 */
	public String getSateiki() {
		return sateiki;
	}
	/**
	 * 査定期 <br>
	 * 
	 * @param sateiki
	 */
	public void setSateiki(String sateiki) {
		this.sateiki = sateiki;
	}    

	/**
	 * 査定期配列 <br>
	 * 
	 * @return 査定期配列
	 */
	public LinkedHashMap getAr_sateiki() {
		return ar_sateiki;
	}
	/**
	 * 査定期配列 <br>
	 * 
	 * @param ar_sateiki
	 */
	public void setAr_sateiki(LinkedHashMap ar_sateiki) {
		this.ar_sateiki = ar_sateiki;
	}

	/**
	 * ソート項目 <br>
	 * 
	 * @return ソート項目
	 */
	public String getSort_item() {
		return sort_item;
	}
	/**
	 * ソート項目 <br>
	 * 
	 * @param sort_item
	 */
	public void setSort_item(String sort_item) {
		this.sort_item = sort_item;
	}

	/**
	 * ソート項目配列 <br>
	 * 
	 * @return ソート項目配列
	 */
	public LinkedHashMap getAr_sort_item() {
		return ar_sort_item;
	}
	/**
	 * ソート項目配列 <br>
	 * 
	 * @param ar_sort_item
	 */
	public void setAr_sort_item(LinkedHashMap ar_sort_item) {
		this.ar_sort_item = ar_sort_item;
	}

	/**
	 * 整列方向 <br>
	 * 
	 * @return 整列方向
	 */
	public String getSort_order() {
		return sort_order;
	}
	/**
	 * 整列方向 <br>
	 * 
	 * @param sort_order
	 */
	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}

	/**
	 * 整列方向配列 <br>
	 * 
	 * @return 整列方向配列
	 */
	public LinkedHashMap getAr_sort_order() {
		return ar_sort_order;
	}
	/**
	 * 整列方向配列 <br>
	 * 
	 * @param ar_sort_order
	 */
	public void setAr_sort_order(LinkedHashMap ar_sort_order) {
		this.ar_sort_order = ar_sort_order;
	}	

	/**
	 * 表示件数配列 <br>
	 * 
	 * @return 表示件数配列
	 */
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	
	/**
	 * 表示件数配列 <br>
	 * 
	 * @param ar_show
	 */
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}

	/**
	 * 参照フェーズ <br>
	 * 
	 * @return 参照フェーズ
	 */
	public String getSansyo_phase() {
		return sansyo_phase;
	}
	/**
	 * 参照フェーズ <br>
	 * 
	 * @param sansyo_phase
	 */
	public void setSansyo_phase(String sansyo_phase) {
		this.sansyo_phase = sansyo_phase;
	}

	/**
	 * 滞留判定案件No. <br>
	 * 
	 * @return 滞留判定案件No.
	 */
	public String getAnken_no() {
		return anken_no;
	}
	/**
	 * 滞留判定案件No. <br>
	 * 
	 * @param anken_no
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}

	/**
	 * id <br>
	 * 
	 * @return id
	 */
	public int getId() {
		return id;
	}
	/**
	 * id <br>
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 判定登録未処理件数 <br>
	 * 
	 * @return 判定登録未処理件数
	 */
	public int getHantei_misyori() {
		return hantei_misyori;
	}
	/**
	 * 判定登録未処理件数 <br>
	 * 
	 * @param hantei_misyori
	 */
	public void setHantei_misyori(int hantei_misyori) {
		this.hantei_misyori = hantei_misyori;
	}

	/**
	 * 判定登録処理中件数 <br>
	 * 
	 * @return 判定登録処理中件数
	 */
	public int getHantei_syorityu() {
		return hantei_syorityu;
	}
	/**
	 * 判定登録処理中件数 <br>
	 * 
	 * @param hantei_syorityu
	 */
	public void setHantei_syorityu(int hantei_syorityu) {
		this.hantei_syorityu = hantei_syorityu;
	}

	/**
	 * 判定登録承認待ち件数 <br>
	 * 
	 * @return 判定登録承認待ち件数
	 */
	public int getHantei_syoninmati() {
		return hantei_syoninmati;
	}
	/**
	 * 判定登録承認待ち件数 <br>
	 * 
	 * @param hantei_syoninmati
	 */
	public void setHantei_syoninmati(int hantei_syoninmati) {
		this.hantei_syoninmati = hantei_syoninmati;
	}

	/**
	 * 判定登録完了件数 <br>
	 * 
	 * @return 判定登録完了件数
	 */
	public int getHantei_kanryo() {
		return hantei_kanryo;
	}
	/**
	 * 判定登録完了件数 <br>
	 * 
	 * @param hantei_kanryo
	 */
	public void setHantei_kanryo(int hantei_kanryo) {
		this.hantei_kanryo = hantei_kanryo;
	}

	/**
	 * 判定検証未処理件数 <br>
	 * 
	 * @return 判定検証未処理件数
	 */
	public int getKensyo_misyori() {
		return kensyo_misyori;
	}
	/**
	 * 判定検証未処理件数 <br>
	 * 
	 * @param kensyo_misyori
	 */
	public void setKensyo_misyori(int kensyo_misyori) {
		this.kensyo_misyori = kensyo_misyori;
	}

	/**
	 * 判定検証処理中件数 <br>
	 * 
	 * @return 判定検証処理中件数
	 */
	public int getKensyo_syorityu() {
		return kensyo_syorityu;
	}
	/**
	 * 判定検証処理中件数 <br>
	 * 
	 * @param kensyo_syorityu
	 */
	public void setKensyo_syorityu(int kensyo_syorityu) {
		this.kensyo_syorityu = kensyo_syorityu;
	}

	/**
	 * 判定検証承認待ち件数 <br>
	 * 
	 * @return 判定検証承認待ち件数
	 */
	public int getKensyo_syoninmati() {
		return kensyo_syoninmati;
	}
	/**
	 * 判定検証承認待ち件数 <br>
	 * 
	 * @param kensyo_syoninmati
	 */
	public void setKensyo_syoninmati(int kensyo_syoninmati) {
		this.kensyo_syoninmati = kensyo_syoninmati;
	}

	/**
	 * 判定検証完了件数 <br>
	 * 
	 * @return 判定検証完了件数
	 */
	public int getKensyo_kanryo() {
		return kensyo_kanryo;
	}
	/**
	 * 判定検証完了件数 <br>
	 * 
	 * @param kensyo_kanryo
	 */
	public void setKensyo_kanryo(int kensyo_kanryo) {
		this.kensyo_kanryo = kensyo_kanryo;
	}
}