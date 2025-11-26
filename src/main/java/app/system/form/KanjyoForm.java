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
import common.struts.adapter.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  OS7108_勘定科目マスタメンテナンス_一覧・登録 アクションフォームクラス <br>
 */
public class KanjyoForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;          
    
    /**  リンククリックされた勘定先の明細.id */	
    private int id;    
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;
    
    /** システム */
    private String system_kbn;        
    
	/** システムセレクトボックス用配列 */
    private LinkedHashMap ar_system;
    
    /** 汎用１ */
    private String hanyo1;
    
	/** 汎用１セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo1;
    
    /** 汎用２ */
    private String hanyo2;    
   
	/** 汎用２セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo2;
    
    /** 勘定科目コード */
    private String kanjo_cd;
    
    /** 勘定科目名称 */
    private String kanjo_nm;    
    
    /** 内分類コード */
    private String kanjo_uchi_cd;    
  
    /** ヘッダの債権フラグ */
    private String saiken_flg;    
    
	/** ヘッダの債権フラグセレクトボックス用配列 */
    private LinkedHashMap ar_saiken_flg;
    
    /** 明細の債権フラグ */
    private String meisai_saiken_flg; 
    
	/** 明細の債権フラグセレクトボックス用配列 */
    private LinkedHashMap ar_meisai_saiken_flg;
    
	/** 明細のDR/CR区分 */
    private String meisai_drcr_kbn;
    
	/** 明細のDR/CR区分セレクトボックス用配列 */
    private LinkedHashMap ar_drcr_kbn;
    
    /** 明細の表示区分 */
    private String meisai_hyoji_kbn; 
    
	/** 明細の表示区分セレクトボックス用配列 */
    private LinkedHashMap ar_hyoji_kbn;
    
	/** 勘定科目情報セレクトボックス用配列 */
    private LinkedHashMap ar_kanjyo;
    
    private String action_flg;
    
    /** 検索時のシステム */
    private String search_system_kbn;      
     
    /** 検索時の汎用１ */
    private String search_hanyo1;  
 
    /** 検索時の汎用２ */
    private String search_hanyo2;  
    
    /** 検索時の勘定科目コード */
    private String search_kanjo_cd;
    
    /** 検索時の勘定科目名称 */
    private String search_kanjo_nm;    
    
    /** 検索時の内分類コード */
    private String search_kanjo_uchi_cd;    
  
    /** 検索時のヘッダの債権フラグ */
    private String search_saiken_flg;    
 
    /**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the ar_drcr_kbn
	 */
	public LinkedHashMap getAr_drcr_kbn() {
		return ar_drcr_kbn;
	}

	/**
	 * @param ar_drcr_kbn the ar_drcr_kbn to set
	 */
	public void setAr_drcr_kbn(LinkedHashMap ar_drcr_kbn) {
		this.ar_drcr_kbn = ar_drcr_kbn;
	}

	/**
	 * @return the ar_kanjyo
	 */
	public LinkedHashMap getAr_kanjyo() {
		return ar_kanjyo;
	}

	/**
	 * @param ar_kanjyo the ar_kanjyo to set
	 */
	public void setAr_kanjyo(LinkedHashMap ar_kanjyo) {
		this.ar_kanjyo = ar_kanjyo;
	}

	/**
	 * @return the ar_saiken_flg
	 */
	public LinkedHashMap getAr_saiken_flg() {
		return ar_saiken_flg;
	}

	/**
	 * @param ar_saiken_flg the ar_saiken_flg to set
	 */
	public void setAr_saiken_flg(LinkedHashMap ar_saiken_flg) {
		this.ar_saiken_flg = ar_saiken_flg;
	}

	/**
	 * @return the ar_show
	 */
	public LinkedHashMap getAr_show() {
		return ar_show;
	}

	/**
	 * @param ar_show the ar_show to set
	 */
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}

	/**
	 * @return the ar_system
	 */
	public LinkedHashMap getAr_system() {
		return ar_system;
	}

	/**
	 * @param ar_system the ar_system to set
	 */
	public void setAr_system(LinkedHashMap ar_system) {
		this.ar_system = ar_system;
	}

	/**
	 * @return the kanjo_cd
	 */
	public String getKanjo_cd() {
		return kanjo_cd;
	}

	/**
	 * @param kanjo_cd the kanjo_cd to set
	 */
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}

	/**
	 * @return the kanjo_nm
	 */
	public String getKanjo_nm() {
		return kanjo_nm;
	}

	/**
	 * @param kanjo_nm the kanjo_nm to set
	 */
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}

	/**
	 * @return the kanjo_uchi_cd
	 */
	public String getKanjo_uchi_cd() {
		return kanjo_uchi_cd;
	}

	/**
	 * @param kanjo_uchi_cd the kanjo_uchi_cd to set
	 */
	public void setKanjo_uchi_cd(String kanjo_uchi_cd) {
		this.kanjo_uchi_cd = kanjo_uchi_cd;
	}


	/**
	 * @return the saiken_flg
	 */
	public String getSaiken_flg() {
		return saiken_flg;
	}

	/**
	 * @param saiken_flg the saiken_flg to set
	 */
	public void setSaiken_flg(String saiken_flg) {
		this.saiken_flg = saiken_flg;
	}


	// 変数初期化
    public KanjyoForm() {
        super.gamenId = GS.OS7108;
        this.system_kbn = GS.GSS;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.hanyo2 = GS.EMPTY_CHARCTER;
        this.saiken_flg = GS.EMPTY_CHARCTER;
        this.meisai_saiken_flg = GS.EMPTY_CHARCTER;
        this.meisai_hyoji_kbn = GS.EMPTY_CHARCTER;
        this.meisai_drcr_kbn = GS.EMPTY_CHARCTER;
        this.kanjo_uchi_cd = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.ar_drcr_kbn = null;
        this.ar_kanjyo = null;
        this.ar_hanyo1 = null;
        this.ar_hanyo2 = null;
        this.ar_saiken_flg = null;
        this.ar_show = null;
        this.ar_system = null;
        this.ar_hyoji_kbn = null;
        this.ar_meisai_saiken_flg = null;
        this.search_system_kbn = GS.EMPTY_CHARCTER;
        this.search_hanyo1 = GS.EMPTY_CHARCTER;
        this.search_hanyo2 = GS.EMPTY_CHARCTER;
        this.search_kanjo_cd = GS.EMPTY_CHARCTER;
        this.search_kanjo_nm = GS.EMPTY_CHARCTER;
        this.search_kanjo_uchi_cd = GS.EMPTY_CHARCTER;
        this.search_saiken_flg = GS.EMPTY_CHARCTER;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the ar_hanyo1
	 */
	public LinkedHashMap getAr_hanyo1() {
		return ar_hanyo1;
	}

	/**
	 * @param ar_hanyo1 the ar_hanyo1 to set
	 */
	public void setAr_hanyo1(LinkedHashMap ar_hanyo1) {
		this.ar_hanyo1 = ar_hanyo1;
	}

	/**
	 * @return the ar_hanyo2
	 */
	public LinkedHashMap getAr_hanyo2() {
		return ar_hanyo2;
	}

	/**
	 * @param ar_hanyo2 the ar_hanyo2 to set
	 */
	public void setAr_hanyo2(LinkedHashMap ar_hanyo2) {
		this.ar_hanyo2 = ar_hanyo2;
	}

	/**
	 * @return the hanyo1
	 */
	public String getHanyo1() {
		return hanyo1;
	}

	/**
	 * @param hanyo1 the hanyo1 to set
	 */
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}

	/**
	 * @return the hanyo2
	 */
	public String getHanyo2() {
		return hanyo2;
	}

	/**
	 * @param hanyo2 the hanyo2 to set
	 */
	public void setHanyo2(String hanyo2) {
		this.hanyo2 = hanyo2;
	}

	/**
	 * @return the ar_hyoji_kbn
	 */
	public LinkedHashMap getAr_hyoji_kbn() {
		return ar_hyoji_kbn;
	}

	/**
	 * @param ar_hyoji_kbn the ar_hyoji_kbn to set
	 */
	public void setAr_hyoji_kbn(LinkedHashMap ar_hyoji_kbn) {
		this.ar_hyoji_kbn = ar_hyoji_kbn;
	}

	/**
	 * @return the ar_meisai_saiken_flg
	 */
	public LinkedHashMap getAr_meisai_saiken_flg() {
		return ar_meisai_saiken_flg;
	}

	/**
	 * @param ar_meisai_saiken_flg the ar_meisai_saiken_flg to set
	 */
	public void setAr_meisai_saiken_flg(LinkedHashMap ar_meisai_saiken_flg) {
		this.ar_meisai_saiken_flg = ar_meisai_saiken_flg;
	}

	/**
	 * @return the meisai_hyoji_kbn
	 */
	public String getMeisai_hyoji_kbn() {
		return meisai_hyoji_kbn;
	}

	/**
	 * @param meisai_hyoji_kbn the meisai_hyoji_kbn to set
	 */
	public void setMeisai_hyoji_kbn(String meisai_hyoji_kbn) {
		this.meisai_hyoji_kbn = meisai_hyoji_kbn;
	}

	/**
	 * @return the meisai_saiken_flg
	 */
	public String getMeisai_saiken_flg() {
		return meisai_saiken_flg;
	}

	/**
	 * @param meisai_saiken_flg the meisai_saiken_flg to set
	 */
	public void setMeisai_saiken_flg(String meisai_saiken_flg) {
		this.meisai_saiken_flg = meisai_saiken_flg;
	}

	/**
	 * @return the drcr_kbn
	 */
	public String getDrcr_kbn() {
		return meisai_drcr_kbn;
	}

	/**
	 * @param drcr_kbn the drcr_kbn to set
	 */
	public void setDrcr_kbn(String drcr_kbn) {
		this.meisai_drcr_kbn = drcr_kbn;
	}

	/**
	 * @return the meisai_drcr_kbn
	 */
	public String getMeisai_drcr_kbn() {
		return meisai_drcr_kbn;
	}

	/**
	 * @param meisai_drcr_kbn the meisai_drcr_kbn to set
	 */
	public void setMeisai_drcr_kbn(String meisai_drcr_kbn) {
		this.meisai_drcr_kbn = meisai_drcr_kbn;
	}

	/**
	 * @return the system_kbn
	 */
	public String getSystem_kbn() {
		return system_kbn;
	}

	/**
	 * @param system_kbn the system_kbn to set
	 */
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}
    
    public void reset(ActionMapping mapping, HttpServletRequest request){
        // 明細一覧が初期化にする
        List<KanjyoBean> meisaiBean = super.getList();
        if (meisaiBean != null) {
            for (int i = 0; i < meisaiBean.size(); i++) {
                meisaiBean.get(i).setMankibi_flg(GS.EMPTY_CHARCTER);
            }
        }
     
    }

	/**
	 * @return the action_flg
	 */
	public String getAction_flg() {
		return action_flg;
	}

	/**
	 * @param action_flg the action_flg to set
	 */
	public void setAction_flg(String action_flg) {
		this.action_flg = action_flg;
	}

	/**
	 * @return the search_hanyo1
	 */
	public String getSearch_hanyo1() {
		return search_hanyo1;
	}

	/**
	 * @param search_hanyo1 the search_hanyo1 to set
	 */
	public void setSearch_hanyo1(String search_hanyo1) {
		this.search_hanyo1 = search_hanyo1;
	}

	/**
	 * @return the search_hanyo2
	 */
	public String getSearch_hanyo2() {
		return search_hanyo2;
	}

	/**
	 * @param search_hanyo2 the search_hanyo2 to set
	 */
	public void setSearch_hanyo2(String search_hanyo2) {
		this.search_hanyo2 = search_hanyo2;
	}

	/**
	 * @return the search_kanjo_cd
	 */
	public String getSearch_kanjo_cd() {
		return search_kanjo_cd;
	}

	/**
	 * @param search_kanjo_cd the search_kanjo_cd to set
	 */
	public void setSearch_kanjo_cd(String search_kanjo_cd) {
		this.search_kanjo_cd = search_kanjo_cd;
	}

	/**
	 * @return the search_kanjo_nm
	 */
	public String getSearch_kanjo_nm() {
		return search_kanjo_nm;
	}

	/**
	 * @param search_kanjo_nm the search_kanjo_nm to set
	 */
	public void setSearch_kanjo_nm(String search_kanjo_nm) {
		this.search_kanjo_nm = search_kanjo_nm;
	}

	/**
	 * @return the search_kanjo_uchi_cd
	 */
	public String getSearch_kanjo_uchi_cd() {
		return search_kanjo_uchi_cd;
	}

	/**
	 * @param search_kanjo_uchi_cd the search_kanjo_uchi_cd to set
	 */
	public void setSearch_kanjo_uchi_cd(String search_kanjo_uchi_cd) {
		this.search_kanjo_uchi_cd = search_kanjo_uchi_cd;
	}

	/**
	 * @return the search_saiken_flg
	 */
	public String getSearch_saiken_flg() {
		return search_saiken_flg;
	}

	/**
	 * @param search_saiken_flg the search_saiken_flg to set
	 */
	public void setSearch_saiken_flg(String search_saiken_flg) {
		this.search_saiken_flg = search_saiken_flg;
	}

	/**
	 * @return the search_system_kbn
	 */
	public String getSearch_system_kbn() {
		return search_system_kbn;
	}

	/**
	 * @param search_system_kbn the search_system_kbn to set
	 */
	public void setSearch_system_kbn(String search_system_kbn) {
		this.search_system_kbn = search_system_kbn;
	}
}
