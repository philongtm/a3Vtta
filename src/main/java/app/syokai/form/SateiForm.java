/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/4/27		ZDF				新規作成
******************************************************************************/
package app.syokai.form;


import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OS6101_査定内容照会 アクションフォームクラス <br>
 */
public class SateiForm extends AppPagerActionForm {
    
    private static final long serialVersionUID = 1L;
    private String satei_tairyu;				// 査定検索/滞留判定検索
    private String sateiki;					// 査定期
    private String hanki_sihanki;				// 半期・四半期区分
    private LinkedHashMap ar_hanki_sihanki;	// 半期・四半期区分用配列
    private String month;						// 対象年月
    private String kanjo_cd;					// 勘定先CD
    private String duns_no;					// DUNS No.
    private String kanjo_nm;					// 勘定先名称
    private String country;					// 所在国
    private LinkedHashMap ar_country;			// 所在国セレクトボックス用配列
    private String torihikisaki_kbn;			// 取引先区分
    private LinkedHashMap ar_torihikisaki_kbn;	// 取引先区分用配列    
    private String saiken_kbn;					// 債権区分
    private LinkedHashMap ar_saiken_kbn;		// 債権区分用配列    
    private String tyusyutu;					// 抽出事由
    private LinkedHashMap ar_tyusyutu;			// 抽出事由用配列    
    private String hanyou1Title;				// 汎用1タイトル
    private String hanyou1;					// 汎用1
    private LinkedHashMap ar_hanyou1;			// 汎用1セレクトボックス用配列
    private String hanyou2Title;				// 汎用2タイトル
    private String hanyou2;					// 汎用2
    private LinkedHashMap ar_hanyou2;			// 汎用2セレクトボックス用配列    
    private String systemKbn;					// システム区分
    private String bunrui2;					// 参照分類２コード
    private String hanyou3Title;				// 汎用3タイトル
    private String hanyou3;					// 汎用3
    private LinkedHashMap ar_hanyou3;			// 汎用3セレクトボックス用配列
    private String sort_item;					// ソート項目
    private LinkedHashMap ar_sort_item;		// ソート項目セレクトボックス用配列
    private String sort_order;					// 整列方向
    private LinkedHashMap ar_sort_order;		// 整列方向セレクトボックス用配列
    private LinkedHashMap ar_show;				// 表示件数セレクトボックス用配列
    private String parties;					// 関係者
    private String syori_dtFrom;				// 処理日FROM
    private String syori_dtTo;					// 処理日TO
    private int id;                     		// リンククリックされた勘定先の勘定先情報Bean.id    
    private String sansyo_systemKbn;			// 全参照システム区分
    private String sansyo_satei_kaisya_cd;		// 全参照査定会社コード
    private String sansyo_bunrui2;				// 全参照分類2コード
    private String kensaku_sateiki;			// 査定期検索
    private String kensaku_hanki_sihanki;		// 半期・四半期区分検索
    private String kensaku_month;				// 対象年月検索
    private String kensaku_kanjo_cd;			// 勘定先CD検索
    private String kensaku_duns_no;			// DUNS No.検索
    private String kensaku_kanjo_nm;			// 勘定先名称検索
    private String kensaku_country;			// 所在国検索    
    private String kensaku_torihikisaki_kbn;	// 取引先区分検索
    private String kensaku_saiken_kbn;			// 債権区分検索
    private String kensaku_tyusyutu;			// 抽出事由検索
    private String kensaku_hanyou1;			// 汎用1検索
    private String kensaku_hanyou2;			// 汎用2検索
    private String kensaku_systemKbn;			// システム区分検索
    private String kensaku_bunrui2;			// 参照分類２コード検索
    private String kensaku_hanyou3;			// 汎用3検索
    private String kensaku_parties;			// 関係者検索
    private String kensaku_syori_dtFrom;		// 処理日FROM検索
    private String kensaku_syori_dtTo;			// 処理日TO検索
    
    /**
     * 変数初期化 <br>
     */
    public SateiForm() {
        super.gamenId = GS.OS6101;
        this.satei_tairyu = GS.EMPTY_CHARCTER;
        this.sateiki = GS.EMPTY_CHARCTER;
        this.hanki_sihanki = GS.EMPTY_CHARCTER;
        this.ar_hanki_sihanki = null;
        this.month = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.duns_no = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.country = GS.EMPTY_CHARCTER;
        this.ar_country = null;
        this.torihikisaki_kbn = GS.EMPTY_CHARCTER;
        this.ar_torihikisaki_kbn = null;
        this.saiken_kbn = GS.EMPTY_CHARCTER;
        this.ar_saiken_kbn = null;
        this.tyusyutu = GS.EMPTY_CHARCTER;
        this.ar_tyusyutu = null;
        this.hanyou1Title = GS.EMPTY_CHARCTER;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.ar_hanyou1 = null;
        this.hanyou2Title = GS.EMPTY_CHARCTER;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.ar_hanyou2 = null;
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.bunrui2 = GS.EMPTY_CHARCTER;
        this.kensaku_sateiki = GS.EMPTY_CHARCTER;
        this.hanyou3Title = GS.EMPTY_CHARCTER;
        this.hanyou3 = GS.EMPTY_CHARCTER;
        this.ar_hanyou3 = null;
        this.sort_item = GS.EMPTY_CHARCTER;
        this.ar_sort_item = null;
        this.sort_order = GS.EMPTY_CHARCTER;
        this.ar_sort_order = null;
        this.ar_show = null;
        this.parties = GS.EMPTY_CHARCTER;
        this.syori_dtFrom = GS.EMPTY_CHARCTER;
        this.syori_dtTo = GS.EMPTY_CHARCTER;
        this.id = 0;
        this.sansyo_systemKbn = GS.EMPTY_CHARCTER;
        this.sansyo_satei_kaisya_cd = GS.EMPTY_CHARCTER;
        this.sansyo_bunrui2 = GS.EMPTY_CHARCTER;
        this.kensaku_hanki_sihanki = GS.EMPTY_CHARCTER;
        this.kensaku_month = GS.EMPTY_CHARCTER;
        this.kensaku_kanjo_cd = GS.EMPTY_CHARCTER;
        this.kensaku_duns_no = GS.EMPTY_CHARCTER;
        this.kensaku_kanjo_nm = GS.EMPTY_CHARCTER;
        this.kensaku_country = GS.EMPTY_CHARCTER;
        this.kensaku_torihikisaki_kbn = GS.EMPTY_CHARCTER;
        this.kensaku_saiken_kbn = GS.EMPTY_CHARCTER;
        this.kensaku_tyusyutu = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou1 = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou2 = GS.EMPTY_CHARCTER;
        this.kensaku_systemKbn = GS.EMPTY_CHARCTER;
        this.kensaku_bunrui2 = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou3 = GS.EMPTY_CHARCTER;
        this.kensaku_parties = GS.EMPTY_CHARCTER;
        this.kensaku_syori_dtFrom = GS.EMPTY_CHARCTER;
        this.kensaku_syori_dtTo = GS.EMPTY_CHARCTER;
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
     * 査定検索/滞留判定検索 <br>
     * 
     * @return the satei_tairyu
     */
    public String getSatei_tairyu() {
        return satei_tairyu;
    }
    
    /**
     * 査定検索/滞留判定検索<br>
     * 
     * @param satei_tairyu the satei_tairyu to set
     */
    public void setSatei_tairyu(String satei_tairyu) {
        this.satei_tairyu = satei_tairyu;
    }
    
    /**
     * 査定期 <br>
     * 
     * @return the sateiki
     */
    public String getSateiki() {
        return sateiki;
    }
    
    /**
     * 査定期<br>
     * 
     * @param sateiki the sateiki to set
     */
    public void setSateiki(String sateiki) {
        this.sateiki = sateiki;
    }
    
    
    /**
     * 半期・四半期区分 <br>
     * 
     * @return the hanki_sihanki
     */
    public String getHanki_sihanki() {
        return hanki_sihanki;
    }
    
    /**
     * 半期・四半期区分<br>
     * 
     * @param hanki_sihanki the hanki_sihanki to set
     */
    public void setHanki_sihanki(String hanki_sihanki) {
        this.hanki_sihanki = hanki_sihanki;
    }
    
    /**
	 * 半期・四半期区分用配列 <br>
	 * 
	 * @return the ar_hanki_sihanki
	 */
	public LinkedHashMap getAr_hanki_sihanki() {
		return ar_hanki_sihanki;
	}
	
	/**
	 * 半期・四半期区分用配列 <br>
	 * 
	 * @param ar_hanki_sihanki the ar_hanki_sihanki to set
	 */
	public void setAr_hanki_sihanki(LinkedHashMap ar_hanki_sihanki) {
		this.ar_hanki_sihanki = ar_hanki_sihanki;
	}
    
    /**
     * 対象年月 <br>
     * 
     * @return the month
     */
    public String getMonth() {
        return month;
    }
    
    /**
     * 対象年月<br>
     * 
     * @param month the month to set
     */
    public void setMonth(String month) {
        this.month = month;
    }
    
    /**
     * 勘定先CD<br>
     * 
     * @return the kanjo_cd
     */
    public String getKanjo_cd() {
        return kanjo_cd;
    }
    
    /**
     * 勘定先CD<br>
     * 
     * @param kanjo_cd the kanjo_cd to set
     */
    public void setKanjo_cd(String kanjo_cd) {
        this.kanjo_cd = kanjo_cd;
    }
    
    /**
     * DUNS No.<br>
     * 
     * @return the duns_no
     */
    public String getDuns_no() {
        return duns_no;
    }
    
    /**
     * DUNS No.<br>
     * 
     * @param duns_no the duns_no to set
     */
    public void setDuns_no(String duns_no) {
        this.duns_no = duns_no;
    }
    
    /**
     * 勘定先名称<br>
     * 
     * @return the kanjo_nm
     */
    public String getKanjo_nm() {
        return kanjo_nm;
    }
    
    /**
     * 勘定先名称<br>
     * 
     * @param kanjo_nm the kanjo_nm to set
     */
    public void setKanjo_nm(String kanjo_nm) {
        this.kanjo_nm = kanjo_nm;
    }
    
    /**
     * 所在国<br>
     * 
     * @return the country
     */
    public String getCountry() {
        return country;
    }
    
    /**
     * 所在国<br>
     * 
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }
   
	/**
	 * 所在国セレクトボックス用配列 <br>
	 * 
	 * @return the ar_country
	 */
	public LinkedHashMap getAr_country() {
		return ar_country;
	}
	
	/**
	 * 所在国セレクトボックス用配列 <br>
	 * 
	 * @param ar_country the ar_country to set
	 */
	public void setAr_country(LinkedHashMap ar_country) {
		this.ar_country = ar_country;
	}
	
    /**
     * 取引先区分<br>
     * 
     * @return the torihikisaki_kbn
     */
    public String getTorihikisaki_kbn() {
        return torihikisaki_kbn;
    }
    
    /**
     * 取引先区分<br>
     * 
     * @param torihikisaki_kbn the torihikisaki_kbn to set
     */
    public void setTorihikisaki_kbn(String torihikisaki_kbn) {
        this.torihikisaki_kbn = torihikisaki_kbn;
    }
    
    /**
	 * 取引先区分用配列 <br>
	 * 
	 * @return the ar_torihikisaki_kbn
	 */
	public LinkedHashMap getAr_torihikisaki_kbn() {
		return ar_torihikisaki_kbn;
	}
	
	/**
	 * 取引先区分用配列 <br>
	 * 
	 * @param ar_torihikisaki_kbn the ar_torihikisaki_kbn to set
	 */
	public void setAr_torihikisaki_kbn(LinkedHashMap ar_torihikisaki_kbn) {
		this.ar_torihikisaki_kbn = ar_torihikisaki_kbn;
	}
	
    /**
     * 債権区分<br>
     * 
     * @return the saiken_kbn
     */
    public String getSaiken_kbn() {
        return saiken_kbn;
    }
    
    /**
     * 債権区分<br>
     * 
     * @param saiken_kbn the saiken_kbn to set
     */
    public void setSaiken_kbn(String saiken_kbn) {
        this.saiken_kbn = saiken_kbn;
    }
    
    /**
	 * 債権区分用配列 <br>
	 * 
	 * @return the ar_saiken_kbn
	 */
	public LinkedHashMap getAr_saiken_kbn() {
		return ar_saiken_kbn;
	}
	
	/**
	 * 債権区分用配列 <br>
	 * 
	 * @param ar_saiken_kbn the ar_saiken_kbn to set
	 */
	public void setAr_saiken_kbn(LinkedHashMap ar_saiken_kbn) {
		this.ar_saiken_kbn = ar_saiken_kbn;
	}
	
    /**
     * 抽出事由<br>
     * 
     * @return the tyusyutu
     */
    public String getTyusyutu() {
        return tyusyutu;
    }
    
    /**
     * 抽出事由<br>
     * 
     * @param tyusyutu the tyusyutu to set
     */
    public void setTyusyutu(String tyusyutu) {
        this.tyusyutu = tyusyutu;
    }
	
    /**
	 * 抽出事由用配列 <br>
	 * 
	 * @return the ar_tyusyutu
	 */
	public LinkedHashMap getAr_tyusyutu() {
		return ar_tyusyutu;
	}
	
	/**
	 * 抽出事由用配列 <br>
	 * 
	 * @param ar_tyusyutu the ar_tyusyutu to set
	 */
	public void setAr_tyusyutu(LinkedHashMap ar_tyusyutu) {
		this.ar_tyusyutu = ar_tyusyutu;
	}
	
    /**
     * 汎用1タイトル<br>
     * 
     * @return the hanyou1Title
     */
    public String getHanyou1Title() {
        return hanyou1Title;
    }
    
    /**
     * 汎用1タイトル<br>
     * 
     * @param hanyou1Title the hanyou1Title to set
     */
    public void setHanyou1Title(String hanyou1Title) {
        this.hanyou1Title = hanyou1Title;
    }
	
    /**
     * 汎用1<br>
     * 
     * @return the hanyou1
     */
    public String getHanyou1() {
        return hanyou1;
    }
    
    /**
     * 汎用1<br>
     * 
     * @param hanyou1 the hanyou1 to set
     */
    public void setHanyou1(String hanyou1) {
        this.hanyou1 = hanyou1;
    }
	
    /**
	 * 汎用1セレクトボックス用配列 <br>
	 * 
	 * @return the ar_status
	 */
	public LinkedHashMap getAr_hanyou1() {
		return ar_hanyou1;
	}
	
	/**
	 * 汎用1セレクトボックス用配列 <br>
	 * 
	 * @param ar_hanyou1 the ar_hanyou1 to set
	 */
	public void setAr_hanyou1(LinkedHashMap ar_hanyou1) {
		this.ar_hanyou1 = ar_hanyou1;
	}
	
	 /**
     * 汎用2タイトル<br>
     * 
     * @return the hanyou2Title
     */
    public String getHanyou2Title() {
        return hanyou2Title;
    }
    
    /**
     * 汎用2タイトル<br>
     * 
     * @param hanyou2Title the hanyou2Title to set
     */
    public void setHanyou2Title(String hanyou2Title) {
        this.hanyou2Title = hanyou2Title;
    }
	
    /**
     * 汎用2<br>
     * 
     * @return the hanyou2
     */
    public String getHanyou2() {
        return hanyou2;
    }
    
    /**
     * 汎用2<br>
     * 
     * @param hanyou2 the hanyou2 to set
     */
    public void setHanyou2(String hanyou2) {
        this.hanyou2 = hanyou2;
    }
	
    /**
	 * 汎用2セレクトボックス用配列 <br>
	 * 
	 * @return the ar_status
	 */
	public LinkedHashMap getAr_hanyou2() {
		return ar_hanyou2;
	}
	
	/**
	 * 汎用2セレクトボックス用配列 <br>
	 * 
	 * @param ar_hanyou2 the ar_hanyou2 to set
	 */
	public void setAr_hanyou2(LinkedHashMap ar_hanyou2) {
		this.ar_hanyou2 = ar_hanyou2;
	}
	
    /**
     * システム区分<br>
     * 
     * @return the systemKbn
     */
    public String getSystemKbn() {
        return systemKbn;
    }
    
    /**
     * システム区分<br>
     * 
     * @param systemKbn the systemKbn to set
     */
    public void setSystemKbn(String systemKbn) {
        this.systemKbn = systemKbn;
    }
    
    /**
     * 参照分類２コード<br>
     * 
     * @return the bunrui2
     */
    public String getBunrui2() {
        return bunrui2;
    }
    
    /**
     * 参照分類２コード<br>
     * 
     * @param bunrui2 the bunrui2 to set
     */
    public void setBunrui2(String bunrui2) {
        this.bunrui2 = bunrui2;
    }
	
    /**
     * 汎用3タイトル<br>
     * 
     * @return the hanyou3Title
     */
    public String getHanyou3Title() {
        return hanyou3Title;
    }
    
    /**
     * 汎用3タイトル<br>
     * 
     * @param hanyou3Title the hanyou3Title to set
     */
    public void setHanyou3Title(String hanyou3Title) {
        this.hanyou3Title = hanyou3Title;
    }
	
    /**
     * 汎用3<br>
     * 
     * @return the hanyou3
     */
    public String getHanyou3() {
        return hanyou3;
    }
    
    /**
     * 汎用3<br>
     * 
     * @param hanyou3 the hanyou3 to set
     */
    public void setHanyou3(String hanyou3) {
        this.hanyou3 = hanyou3;
    }
	
    /**
	 * 汎用3セレクトボックス用配列 <br>
	 * 
	 * @return the ar_hanyou3
	 */
	public LinkedHashMap getAr_hanyou3() {
		return ar_hanyou3;
	}
	
	/**
	 * 汎用3セレクトボックス用配列 <br>
	 * 
	 * @param ar_hanyou3 the ar_hanyou3 to set
	 */
	public void setAr_hanyou3(LinkedHashMap ar_hanyou3) {
		this.ar_hanyou3 = ar_hanyou3;
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
     * 関係者<br>
     * 
     * @return the parties
     */
    public String getParties() {
        return parties;
    }
    
    /**
     * 関係者<br>
     * 
     * @param parties the parties to set
     */
    
    public void setParties(String parties) {
        this.parties = parties;
    }
    
	/**
     * 処理日FROM<br>
     * 
     * @return the syori_dtFrom
     */
    public String getSyori_dtFrom() {
        return syori_dtFrom;
    }
    
    /**
     * 処理日FROM<br>
     * 
     * @param syori_dtFrom the syori_dtFrom to set
     */
    public void setSyori_dtFrom(String syori_dtFrom) {
        this.syori_dtFrom = syori_dtFrom;
    }
	    
	/**
     * 処理日TO<br>
     * 
     * @return the syori_dtTo
     */
    public String getSyori_dtTo() {
        return syori_dtTo;
    }
    
    /**
     * 処理日TO<br>
     * 
     * @param syori_dtTo the syori_dtTo to set
     */
    public void setSyori_dtTo(String syori_dtTo) {
        this.syori_dtTo = syori_dtTo;
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
     * 全参照システム区分<br>
     * 
     * @return the sansyo_systemKbn
     */
    public String getSansyo_systemKbn() {
        return sansyo_systemKbn;
    }
    
    /**
     * 全参照システム区分<br>
     * 
     * @param sansyo_systemKbn the sansyo_systemKbn to set
     */
    public void setSansyo_systemKbn(String sansyo_systemKbn) {
        this.sansyo_systemKbn = sansyo_systemKbn;
    }
    
	/**
     * 全参照査定会社コード<br>
     * 
     * @return the sansyo_satei_kaisya_cd
     */
    public String getSansyo_satei_kaisya_cd() {
        return sansyo_satei_kaisya_cd;
    }
    
    /**
     * 全参照査定会社コード<br>
     * 
     * @param sansyo_satei_kaisya_cd the sansyo_satei_kaisya_cd to set
     */
    public void setSansyo_satei_kaisya_cd(String sansyo_satei_kaisya_cd) {
        this.sansyo_satei_kaisya_cd = sansyo_satei_kaisya_cd;
    }
    
    /**
     * 全参照分類2コード<br>
     * 
     * @return the sansyo_bunrui2
     */
    public String getSansyo_bunrui2() {
        return sansyo_bunrui2;
    }
    
    /**
     * 全参照分類2コード<br>
     * 
     * @param sansyo_bunrui2 the sansyo_bunrui2 to set
     */
    public void setSansyo_bunrui2(String sansyo_bunrui2) {
        this.sansyo_bunrui2 = sansyo_bunrui2;
    }
    
    /**
     * 査定期検索<br>
     * 
     * @return the kensaku_sateiki
     */
    public String getKensaku_sateiki() {
        return kensaku_sateiki;
    }
    
    /**
     * 査定期検索<br>
     * 
     * @param kensaku_sateiki the kensaku_sateiki to set
     */
    public void setKensaku_sateiki(String kensaku_sateiki) {
        this.kensaku_sateiki = kensaku_sateiki;
    }
    
    /**
     * 半期・四半期区分検索<br>
     * 
     * @return the kensaku_hanki_sihanki
     */
    public String getKensaku_hanki_sihanki() {
        return kensaku_hanki_sihanki;
    }
    
    /**
     * 半期・四半期区分検索<br>
     * 
     * @param kensaku_hanki_sihanki the kensaku_hanki_sihanki to set
     */
    public void setKensaku_hanki_sihanki(String kensaku_hanki_sihanki) {
        this.kensaku_hanki_sihanki = kensaku_hanki_sihanki;
    }
    
    /**
     * 対象年月検索<br>
     * 
     * @return the kensaku_month
     */
    public String getKensaku_month() {
        return kensaku_month;
    }
    
    /**
     * 対象年月検索<br>
     * 
     * @param kensaku_month the kensaku_month to set
     */
    public void setKensaku_month(String kensaku_month) {
        this.kensaku_month = kensaku_month;
    }
    
    /**
     * 勘定先CD検索<br>
     * 
     * @return the kensaku_kanjo_cd
     */
    public String getKensaku_kanjo_cd() {
        return kensaku_kanjo_cd;
    }
    
    /**
     * 勘定先CD検索<br>
     * 
     * @param kensaku_kanjo_cd the kensaku_kanjo_cd to set
     */
    public void setKensaku_kanjo_cd(String kensaku_kanjo_cd) {
        this.kensaku_kanjo_cd = kensaku_kanjo_cd;
    }
    
    /**
     * DUNS No.検索<br>
     * 
     * @return the kensaku_duns_no
     */
    public String getKensaku_duns_no() {
        return kensaku_duns_no;
    }
    
    /**
     * DUNS No.検索<br>
     * 
     * @param kensaku_duns_no the kensaku_duns_no to set
     */
    public void setKensaku_duns_no(String kensaku_duns_no) {
        this.kensaku_duns_no = kensaku_duns_no;
    }
    
    /**
     * 勘定先名称検索<br>
     * 
     * @return the kensaku_kanjo_nm
     */
    public String getKensaku_kanjo_nm() {
        return kensaku_kanjo_nm;
    }
    
    /**
     * 勘定先名称検索<br>
     * 
     * @param kensaku_kanjo_nm the kensaku_kanjo_nm to set
     */
    public void setKensaku_kanjo_nm(String kensaku_kanjo_nm) {
        this.kensaku_kanjo_nm = kensaku_kanjo_nm;
    }
    
    /**
     * 所在国検索<br>
     * 
     * @return the kensaku_country
     */
    public String getKensaku_country() {
        return kensaku_country;
    }
    
    /**
     * 所在国検索<br>
     * 
     * @param kensaku_country the kensaku_country to set
     */
    public void setKensaku_country(String kensaku_country) {
        this.kensaku_country = kensaku_country;
    }
    
    /**
     * 取引先区分検索<br>
     * 
     * @return the kensaku_torihikisaki_kbn
     */
    public String getKensaku_torihikisaki_kbn() {
        return kensaku_torihikisaki_kbn;
    }
    
    /**
     * 取引先区分検索<br>
     * 
     * @param kensaku_torihikisaki_kbn the kensaku_torihikisaki_kbn to set
     */
    public void setKensaku_torihikisaki_kbn(String kensaku_torihikisaki_kbn) {
        this.kensaku_torihikisaki_kbn = kensaku_torihikisaki_kbn;
    }
    
    /**
     * 債権区分検索<br>
     * 
     * @return the kensaku_saiken_kbn
     */
    public String getKensaku_saiken_kbn() {
        return kensaku_saiken_kbn;
    }
    
    /**
     * 債権区分検索<br>
     * 
     * @param kensaku_saiken_kbn the kensaku_saiken_kbn to set
     */
    public void setKensaku_saiken_kbn(String kensaku_saiken_kbn) {
        this.kensaku_saiken_kbn = kensaku_saiken_kbn;
    }
    
    /**
     * 抽出事由検索<br>
     * 
     * @return the kensaku_tyusyutu
     */
    public String getKensaku_tyusyutu() {
        return kensaku_tyusyutu;
    }
    
    /**
     * 抽出事由検索<br>
     * 
     * @param kensaku_tyusyutu the kensaku_tyusyutu to set
     */
    public void setKensaku_tyusyutu(String kensaku_tyusyutu) {
        this.kensaku_tyusyutu = kensaku_tyusyutu;
    }
    
    /**
     * 汎用1検索<br>
     * 
     * @return the kensaku_hanyou1
     */
    public String getKensaku_hanyou1() {
        return kensaku_hanyou1;
    }
    
    /**
     * 汎用1検索<br>
     * 
     * @param kensaku_hanyou1 the kensaku_hanyou1 to set
     */
    public void setKensaku_hanyou1(String kensaku_hanyou1) {
        this.kensaku_hanyou1 = kensaku_hanyou1;
    }
    
    /**
     * 汎用2検索<br>
     * 
     * @return the kensaku_hanyou2
     */
    public String getKensaku_hanyou2() {
        return kensaku_hanyou2;
    }
    
    /**
     * 汎用2検索<br>
     * 
     * @param kensaku_hanyou2 the kensaku_hanyou2 to set
     */
    public void setKensaku_hanyou2(String kensaku_hanyou2) {
        this.kensaku_hanyou2 = kensaku_hanyou2;
    }
    
    /**
     * システム区分検索<br>
     * 
     * @return the kensaku_systemKbn
     */
    public String getKensaku_systemKbn() {
        return kensaku_systemKbn;
    }
    
    /**
     * システム区分検索<br>
     * 
     * @param kensaku_systemKbn the kensaku_systemKbn to set
     */
    public void setKensaku_systemKbn(String kensaku_systemKbn) {
        this.kensaku_systemKbn = kensaku_systemKbn;
    }
    
    /**
     * 参照分類２コード検索<br>
     * 
     * @return the kensaku_bunrui2
     */
    public String getKensaku_bunrui2() {
        return kensaku_bunrui2;
    }
    
    /**
     * 参照分類２コード検索<br>
     * 
     * @param kensaku_bunrui2 the kensaku_bunrui2 to set
     */
    public void setKensaku_bunrui2(String kensaku_bunrui2) {
        this.kensaku_bunrui2 = kensaku_bunrui2;
    }
    
    /**
     * 汎用3検索<br>
     * 
     * @return the kensaku_hanyou3
     */
    public String getKensaku_hanyou3() {
        return kensaku_hanyou3;
    }
    
    /**
     * 汎用3検索<br>
     * 
     * @param kensaku_hanyou3 the kensaku_hanyou3 to set
     */
    public void setKensaku_hanyou3(String kensaku_hanyou3) {
        this.kensaku_hanyou3 = kensaku_hanyou3;
    }
    
    /**
     * 関係者検索<br>
     * 
     * @return the kensaku_parties
     */
    public String getKensaku_parties() {
        return kensaku_parties;
    }
    
    /**
     * 関係者検索<br>
     * 
     * @param kensaku_parties the kensaku_parties to set
     */
    public void setKensaku_parties(String kensaku_parties) {
        this.kensaku_parties = kensaku_parties;
    }
    
    /**
     * 処理日FROM検索<br>
     * 
     * @return the kensaku_syori_dtFrom
     */
    public String getKensaku_syori_dtFrom() {
        return kensaku_syori_dtFrom;
    }
    
    /**
     * 処理日FROM検索<br>
     * 
     * @param kensaku_syori_dtFrom the kensaku_syori_dtFrom to set
     */
    public void setKensaku_syori_dtFrom(String kensaku_syori_dtFrom) {
        this.kensaku_syori_dtFrom = kensaku_syori_dtFrom;
    }
    
    /**
     * 処理日TO検索<br>
     * 
     * @return the kensaku_syori_dtTo
     */
    public String getKensaku_syori_dtTo() {
        return kensaku_syori_dtTo;
    }
    
    /**
     * 処理日TO検索<br>
     * 
     * @param kensaku_syori_dtTo the kensaku_syori_dtTo to set
     */
    public void setKensaku_syori_dtTo(String kensaku_syori_dtTo) {
        this.kensaku_syori_dtTo = kensaku_syori_dtTo;
    }
  
}
