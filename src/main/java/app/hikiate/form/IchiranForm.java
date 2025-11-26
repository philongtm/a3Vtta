/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.hikiate.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OD1101_実質滞留債権判定_対象先一覧 アクションフォームクラス
 * 
 */
public class IchiranForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; // serialVersionUID
    private String tanto;                   // 自担当分/汎用２ラジオボタン(1:自担当分、2:汎用２)
    private String sateiki;                 // 査定期
    private LinkedHashMap ar_sateiki;       // 査定期セレクトボックス用配列
    private String sort_item;               // ソート項目
    private LinkedHashMap ar_sort_item;     // ソート項目セレクトボックス用配列
    private String sort_order;              // 整列方向
    private LinkedHashMap ar_sort_order;    // 整列方向セレクトボックス用配列
    private LinkedHashMap ar_show;          // 表示件数セレクトボックス用配列
    private String sansyo_phase;            // 参照フェーズ
    private String anken_no;                // リンククリックされた勘定先の明細.滞留判定案件No.
    private int id;                         // リンククリックされた勘定先の明細.id
    private int ichiji_misyori;             // 一次査定未処理件数
    private int ichiji_syorityu;            // 一次査定処理中件数
    private int ichiji_syoninmati;          // 一次査定承認待ち件数
    private int ichiji_kanryo;              // 一次査定完了件数
    private int kensyo_misyori;             // 一次査定検証未処理件数
    private int kensyo_syorityu;            // 一次査定検証処理中件数
    private int kensyo_syoninmati;          // 一次査定検証承認待ち件数   
    private int kensyo_kanryo;              // 一次査定検証完了件数   
    private int niji_misyori;               // 二次査定未処理件数
    private int niji_syorityu;              // 二次査定処理中件数
    private int niji_syoninmati;            // 二次査定承認待ち件数
    private int niji_kanryo;                // 二次査定完了件数
    private int kakunin_misyori;            // 引当金確未処理件数
    private int kakunin_syorityu;           // 引当金確処理中件数
    private int kakunin_syoninmati;         // 引当金確承認待ち件数
    private int kakunin_kanryo;             // 引当金確完了件数
    private String[] selectedMountains;     // チェックボックス	
      
    // 変数初期化
    public IchiranForm() {
    	super.gamenId = GS.OD1101;
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
        this.ichiji_misyori = 0;
        this.ichiji_syorityu = 0;
        this.ichiji_syoninmati = 0;
        this.ichiji_kanryo = 0;
        this.kensyo_misyori = 0;
        this.kensyo_syorityu = 0;
        this.kensyo_syoninmati = 0;
        this.kensyo_kanryo = 0;
        this.niji_misyori = 0;
        this.niji_syorityu = 0;
        this.niji_syoninmati = 0;
        this.niji_kanryo = 0;
        this.kakunin_misyori = 0;
        this.kakunin_syorityu = 0;
        this.kakunin_syoninmati = 0;
        this.kakunin_kanryo = 0;
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
    //一次査定未処理件数
    public int getIchiji_misyori() {
        return ichiji_misyori;
    }
    public void setIchiji_misyori(int ichiji_misyori) {
        this.ichiji_misyori = ichiji_misyori;
    }
    //一次査定処理中件数
    public int getIchiji_syorityu() {
        return ichiji_syorityu;
    }
    public void setIchiji_syorityu(int ichiji_syorityu) {
        this.ichiji_syorityu = ichiji_syorityu;
    }
    //一次査定承認待ち件数
    public int getIchiji_syoninmati() {
        return ichiji_syoninmati;
    }
    public void setIchiji_syoninmati(int ichiji_syoninmati) {
        this.ichiji_syoninmati = ichiji_syoninmati;
    }
    //一次査定完了件数
    public int getIchiji_kanryo() {
        return ichiji_kanryo;
    }
    public void setIchiji_kanryo(int ichiji_kanryo) {
        this.ichiji_kanryo = ichiji_kanryo;
    }
    //一次査定検証未処理件数
    public int getKensyo_misyori() {
        return kensyo_misyori;
    }
    public void setKensyo_misyori(int kensyo_misyori) {
        this.kensyo_misyori = kensyo_misyori;
    }
    //一次査定検証処理中件数
    public int getKensyo_syorityu() {
        return kensyo_syorityu;
    }
    public void setKensyo_syorityu(int kensyo_syorityu) {
        this.kensyo_syorityu = kensyo_syorityu;
    }
    //一次査定検証承認待ち件数   
    public int getKensyo_syoninmati() {
        return kensyo_syoninmati;
    }
    public void setKensyo_syoninmati(int kensyo_syoninmati) {
        this.kensyo_syoninmati = kensyo_syoninmati;
    }
    //一次査定検証完了件数   
    public int getKensyo_kanryo() {
        return kensyo_kanryo;
    }
    public void setKensyo_kanryo(int kensyo_kanryo) {
        this.kensyo_kanryo = kensyo_kanryo;
    }
    //二次査定未処理件数
    public int getNiji_misyori() {
        return niji_misyori;
    }
    public void setNiji_misyori(int niji_misyori) {
        this.niji_misyori = niji_misyori;
    }
    //二次査定処理中件数
    public int getNiji_syorityu() {
        return niji_syorityu;
    }
    public void setNiji_syorityu(int niji_syorityu) {
        this.niji_syorityu = niji_syorityu;
    }
    //二次査定承認待ち件数
    public int getNiji_syoninmati() {
        return niji_syoninmati;
    }
    public void setNiji_syoninmati(int niji_syoninmati) {
        this.niji_syoninmati = niji_syoninmati;
    }
    //二次査定完了件数
    public int getNiji_kanryo() {
        return niji_kanryo;
    }
    public void setNiji_kanryo(int niji_kanryo) {
        this.niji_kanryo = niji_kanryo;
    }
    //引当金確未処理件数
    public int getKakunin_misyori() {
        return kakunin_misyori;
    }
    public void setKakunin_misyori(int kakunin_misyori) {
        this.kakunin_misyori = kakunin_misyori;
    }
    //引当金確処理中件数
    public int getKakunin_syorityu() {
        return kakunin_syorityu;
    }
    public void setKakunin_syorityu(int kakunin_syorityu) {
        this.kakunin_syorityu = kakunin_syorityu;
    }
    //引当金確承認待ち件数
    public int getKakunin_syoninmati() {
        return kakunin_syoninmati;
    }
    public void setKakunin_syoninmati(int kakunin_syoninmati) {
        this.kakunin_syoninmati = kakunin_syoninmati;
    }
    //引当金確完了件数
    public int getKakunin_kanryo() {
        return kakunin_kanryo;
    }
    public void setKakunin_kanryo(int kakunin_kanryo) {
        this.kakunin_kanryo = kakunin_kanryo;
    }
    //チェックボックス
	public String[] getSelectedMountains() {
		return selectedMountains;
	}
	public void setSelectedMountains(String[] selectedMountains) {
		this.selectedMountains = selectedMountains;
	}
    
}