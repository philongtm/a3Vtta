/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.form;

import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OC1101_査定_対象先一覧 アクションフォームクラス
 * 
 */
public class IchiranForm extends AppPagerActionForm {
	
    private String sateiki;					//査定期
	private String tanto;						//自担当分/汎用２ラジオボタン(1:自担当分、2:汎用２)
    private LinkedHashMap ar_sateiki;			//査定期セレクトボックス用配列
    private LinkedHashMap ar_show;				//表示件数セレクトボックス用配列
    private String sort_item;					//ソート項目
    private LinkedHashMap ar_sort_item;		//ソート項目セレクトボックス用配列
    private String sort_order;					//整列方向
    private LinkedHashMap ar_sort_order;		//整列方向セレクトボックス用配列
    private String sansyo_phase;       		//参照フェーズ
    private int id;	            			//リンククリックされた取引先のID
    private int itijisatei_misyori;			//一次査定登録未処理件数
    private int itijisatei_syorityu;			//一次査定登録処理中件数
    private int itijisatei_syoninmati;			//一次査定登録承認待件数
    private int itijisatei_kanryo;				//一次査定登録完了件数
    private int itijisateikensyo_misyori;		//一次査定検証未処理件数
    private int itijisateikensyo_syorityu;		//一次査定検証処理中件数
    private int itijisateikensyo_syoninmati;	//一次査定検証承認待件数   
    private int itijisateikensyo_kanryo;		//一次査定検証完了件数
    private int nijisatei_misyori;				//二次査定登録未処理件数
    private int nijisatei_syorityu;			//二次査定登録処理中件数
    private int nijisatei_syoninmati;			//二次査定登録承認待件数
    private int nijisatei_kanryo;				//二次査定登録完了件数
    private int hanyou3_misyori;				//国内：引当金検証未処理件数、海外：引当金確認未処理件数
    private int hanyou3_syorityu;				//国内：引当金検証処理中件数、海外：引当金確認処理中件数
    private int hanyou3_syoninmati;			//国内：引当金検証承認待ち件数、海外：引当金確認承認待件数
    private int hanyou3_kanryo;				//国内：引当金検証完了件数、海外：引当金確認完了件数
      
    //変数初期化
    public IchiranForm() {
    	super.gamenId			= GS.OC1101;
        this.sateiki			= GS.EMPTY_CHARCTER;
        this.ar_sateiki			= null;
        this.tanto = GS.EMPTY_CHARCTER;
        this.ar_show			= null;
        this.sort_item			= GS.EMPTY_CHARCTER;
        this.ar_sort_item		= null;
        this.sort_order			= GS.EMPTY_CHARCTER;
        this.ar_sort_order		= null;
        this.sansyo_phase		= GS.EMPTY_CHARCTER;
        this.id					= 0;
        this.itijisatei_misyori					= 0;
        this.itijisatei_syorityu				= 0;
        this.itijisatei_syoninmati				= 0;
        this.itijisatei_kanryo					= 0;
        this.itijisateikensyo_misyori			= 0;
        this.itijisateikensyo_syorityu			= 0;
        this.itijisateikensyo_syoninmati		= 0;
        this.itijisateikensyo_kanryo			= 0;
        this.nijisatei_misyori					= 0;
        this.nijisatei_syorityu					= 0;
        this.nijisatei_syoninmati				= 0;
        this.nijisatei_kanryo					= 0;
        this.hanyou3_misyori					= 0;
        this.hanyou3_syorityu					= 0;
        this.hanyou3_syoninmati					= 0;
        this.hanyou3_kanryo						= 0;
        this.setPager(new ArrayList<TorihikisakiBean>());
        this.setAr_meisai(new ArrayList<TorihikisakiBean>());
    }
    
    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
    
    //表示件数配列
    public LinkedHashMap getAr_show() {
        return ar_show;
    }
    public void setAr_show(LinkedHashMap ar_show) {
        this.ar_show = ar_show;
    }
    //査定期リスト
	public LinkedHashMap getAr_sateiki() {
		return ar_sateiki;
	}
	public void setAr_sateiki(LinkedHashMap ar_sateiki) {
		this.ar_sateiki = ar_sateiki;
	}
    //査定期
	public String getSateiki() {
		return sateiki;
	}
	public void setSateiki(String sateiki) {
		this.sateiki = sateiki;
	}
    //ソート項目
	public String getSort_item() {
		return sort_item;
	}
	public void setSort_item(String sort_item) {
		this.sort_item = sort_item;
	}
    //ソート配列
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
    //参照フェーズ
    public String getSansyo_phase() {
        return sansyo_phase;
    }
    public void setSansyo_phase(String sansyo_phase) {
        this.sansyo_phase = sansyo_phase;
    }
	//id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //汎用３完了
	public int getHanyou3_kanryo() {
		return hanyou3_kanryo;
	}
	public void setHanyou3_kanryo(int hanyou3_kanryo) {
		this.hanyou3_kanryo = hanyou3_kanryo;
	}
    //汎用３完了
	public int getHanyou3_misyori() {
		return hanyou3_misyori;
	}
	public void setHanyou3_misyori(int hanyou3_misyori) {
		this.hanyou3_misyori = hanyou3_misyori;
	}
    //汎用３完了
	public int getHanyou3_syoninmati() {
		return hanyou3_syoninmati;
	}
	public void setHanyou3_syoninmati(int hanyou3_syoninmati) {
		this.hanyou3_syoninmati = hanyou3_syoninmati;
	}
    //汎用３処理中
	public int getHanyou3_syorityu() {
		return hanyou3_syorityu;
	}
	public void setHanyou3_syorityu(int hanyou3_syorityu) {
		this.hanyou3_syorityu = hanyou3_syorityu;
	}
    //一次査定完了
	public int getItijisatei_kanryo() {
		return itijisatei_kanryo;
	}
	public void setItijisatei_kanryo(int itijisatei_kanryo) {
		this.itijisatei_kanryo = itijisatei_kanryo;
	}
    //一次査定未処理
	public int getItijisatei_misyori() {
		return itijisatei_misyori;
	}
	public void setItijisatei_misyori(int itijisatei_misyori) {
		this.itijisatei_misyori = itijisatei_misyori;
	}
    //一次査定承認待
	public int getItijisatei_syoninmati() {
		return itijisatei_syoninmati;
	}
	public void setItijisatei_syoninmati(int itijisatei_syoninmati) {
		this.itijisatei_syoninmati = itijisatei_syoninmati;
	}
    //一次査定処理中
	public int getItijisatei_syorityu() {
		return itijisatei_syorityu;
	}
	public void setItijisatei_syorityu(int itijisatei_syorityu) {
		this.itijisatei_syorityu = itijisatei_syorityu;
	}
    //一次査定検証完了
	public int getItijisateikensyo_kanryo() {
		return itijisateikensyo_kanryo;
	}
	public void setItijisateikensyo_kanryo(int itijisateikensyo_kanryo) {
		this.itijisateikensyo_kanryo = itijisateikensyo_kanryo;
	}
    //一次査定検証未処理
	public int getItijisateikensyo_misyori() {
		return itijisateikensyo_misyori;
	}
	public void setItijisateikensyo_misyori(int itijisateikensyo_misyori) {
		this.itijisateikensyo_misyori = itijisateikensyo_misyori;
	}
    //一次査定検証承認待
	public int getItijisateikensyo_syoninmati() {
		return itijisateikensyo_syoninmati;
	}
	public void setItijisateikensyo_syoninmati(int itijisateikensyo_syoninmati) {
		this.itijisateikensyo_syoninmati = itijisateikensyo_syoninmati;
	}
    //一次査定検証処理中
	public int getItijisateikensyo_syorityu() {
		return itijisateikensyo_syorityu;
	}
	public void setItijisateikensyo_syorityu(int itijisateikensyo_syorityu) {
		this.itijisateikensyo_syorityu = itijisateikensyo_syorityu;
	}
    //二次査定完了
	public int getNijisatei_kanryo() {
		return nijisatei_kanryo;
	}
	public void setNijisatei_kanryo(int nijisatei_kanryo) {
		this.nijisatei_kanryo = nijisatei_kanryo;
	}
    //二次査定未処理
	public int getNijisatei_misyori() {
		return nijisatei_misyori;
	}
	public void setNijisatei_misyori(int nijisatei_misyori) {
		this.nijisatei_misyori = nijisatei_misyori;
	}
    //二次査定承認待
	public int getNijisatei_syoninmati() {
		return nijisatei_syoninmati;
	}
	public void setNijisatei_syoninmati(int nijisatei_syoninmati) {
		this.nijisatei_syoninmati = nijisatei_syoninmati;
	}
    //二次査定処理中
	public int getNijisatei_syorityu() {
		return nijisatei_syorityu;
	}
	public void setNijisatei_syorityu(int nijisatei_syorityu) {
		this.nijisatei_syorityu = nijisatei_syorityu;
	}
    //担当ラジオボタン
	public String getTanto() {
		return tanto;
	}
	public void setTanto(String tanto) {
		this.tanto = tanto;
	}
    //リセット
    public void reset(ActionMapping mapping, HttpServletRequest request){
        //Beanのもぎ取りチェックボックス初期化
        List<TorihikisakiBean> list = super.getList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
            	list.get(i).setMogitori_chk(GS.EMPTY_CHARCTER);
            }
        }
    }
}