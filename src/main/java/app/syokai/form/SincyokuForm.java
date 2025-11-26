/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/4/13		ZHUDF				新規作成
******************************************************************************/
package app.syokai.form;


import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OS6103_進捗状況照会 アクションフォームクラス <br>
 */
public class SincyokuForm extends AppPagerActionForm {
    
    private static final long serialVersionUID = 1L;
    private String month;						// 対象年月
    private String kanjo_cd;					// 勘定先CD
    private String duns_no;					// DUNS No.
    private String kanjo_nm;					// 勘定先名称
    private String country;					// 所在国
    private LinkedHashMap ar_country;			// 所在国セレクトボックス用配列
    private String phase;						// フェーズ
    private LinkedHashMap ar_phase;			// フェーズセレクトボックス用配列
    private String status;						// ステータス
    private LinkedHashMap ar_status;			// ステータスセレクトボックス用配列
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
    private String parties;					// 関係者
    private String syori_dtFrom;				// 処理日FROM
    private String syori_dtTo;					// 処理日TO
    private int id;                     		// リンククリックされた勘定先の勘定先情報Bean.id
    private LinkedHashMap ar_show;				// 表示件数セレクトボックス用配列
    private String key_kbn;					// 区分キー(汎用2【リスト】取得用)
    private String kensaku_month;				// 対象年月検索
    private String kensaku_kanjo_cd;			// 勘定先CD検索
    private String kensaku_duns_no;			// DUNS No.検索
    private String kensaku_kanjo_nm;			// 勘定先名称検索
    private String kensaku_country;			// 所在国検索
    private String kensaku_phase;				// フェーズ検索
    private String kensaku_status;				// ステータス検索
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
    public SincyokuForm() {
        super.gamenId = GS.OS6103;
        this.month = GS.EMPTY_CHARCTER;
        this.kanjo_cd = GS.EMPTY_CHARCTER;
        this.duns_no = GS.EMPTY_CHARCTER;
        this.kanjo_nm = GS.EMPTY_CHARCTER;
        this.country = GS.EMPTY_CHARCTER;
        this.ar_country = null;
        this.phase = GS.EMPTY_CHARCTER;
        this.ar_phase = null;
        this.status = GS.EMPTY_CHARCTER;
        this.ar_status = null;
        this.hanyou1Title = GS.EMPTY_CHARCTER;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.ar_hanyou1 = null;
        this.hanyou2Title = GS.EMPTY_CHARCTER;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.ar_hanyou2 = null;
        this.systemKbn = GS.EMPTY_CHARCTER;
        this.hanyou3Title = GS.EMPTY_CHARCTER;
        this.hanyou3 = GS.EMPTY_CHARCTER;
        this.ar_hanyou3 = null;
        this.parties = GS.EMPTY_CHARCTER;
        this.syori_dtFrom = GS.EMPTY_CHARCTER;
        this.syori_dtTo = GS.EMPTY_CHARCTER;
        this.kensaku_month = GS.EMPTY_CHARCTER;
        this.kensaku_kanjo_cd = GS.EMPTY_CHARCTER;
        this.kensaku_duns_no = GS.EMPTY_CHARCTER;
        this.kensaku_kanjo_nm = GS.EMPTY_CHARCTER;
        this.kensaku_country = GS.EMPTY_CHARCTER;
        this.kensaku_phase = GS.EMPTY_CHARCTER;
        this.kensaku_status = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou1 = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou2 = GS.EMPTY_CHARCTER;
        this.kensaku_systemKbn = GS.EMPTY_CHARCTER;
        this.kensaku_bunrui2 = GS.EMPTY_CHARCTER;
        this.kensaku_hanyou3 = GS.EMPTY_CHARCTER;
        this.kensaku_parties = GS.EMPTY_CHARCTER;
        this.kensaku_syori_dtFrom = GS.EMPTY_CHARCTER;
        this.kensaku_syori_dtTo = GS.EMPTY_CHARCTER;       
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
     * フェーズ<br>
     * 
     * @return the phase
     */
    public String getPhase() {
        return phase;
    }
    
    /**
     * フェーズ<br>
     * 
     * @param phase the phase to set
     */
    public void setPhase(String phase) {
        this.phase = phase;
    }
    
    /**
	 * フェーズセレクトボックス用配列 <br>
	 * 
	 * @return the ar_phase
	 */
	public LinkedHashMap getAr_phase() {
		return ar_phase;
	}
	
	/**
	 * フェーズセレクトボックス用配列 <br>
	 * 
	 * @param ar_phase the ar_phase to set
	 */
	public void setAr_phase(LinkedHashMap ar_phase) {
		this.ar_phase = ar_phase;
	}
	
    /**
     * ステータス<br>
     * 
     * @return the status
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * ステータス<br>
     * 
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
	 * ステータスセレクトボックス用配列 <br>
	 * 
	 * @return the ar_status
	 */
	public LinkedHashMap getAr_status() {
		return ar_status;
	}
	
	/**
	 * ステータスセレクトボックス用配列 <br>
	 * 
	 * @param ar_status the ar_status to set
	 */
	public void setAr_status(LinkedHashMap ar_status) {
		this.ar_status = ar_status;
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
     * 区分キー(汎用2【リスト】取得用)<br>
     * 
     * @return the key_kbn
     */
    public String getKey_kbn() {
        return key_kbn;
    }
    
    /**
     * 区分キー(汎用2【リスト】取得用)<br>
     * 
     * @param key_kbn the key_kbn to set
     */
    public void setKey_kbn(String key_kbn) {
        this.key_kbn = key_kbn;
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
     * フェーズ検索<br>
     * 
     * @return the kensaku_phase
     */
    public String getKensaku_phase() {
        return kensaku_phase;
    }
    
    /**
     * フェーズ検索<br>
     * 
     * @param kensaku_phase the kensaku_phase to set
     */
    public void setKensaku_phase(String kensaku_phase) {
        this.kensaku_phase = kensaku_phase;
    }
    
    /**
     * ステータス検索<br>
     * 
     * @return the kensaku_status
     */
    public String getKensaku_status() {
        return kensaku_status;
    }
    
    /**
     * ステータス検索<br>
     * 
     * @param kensaku_status the kensaku_status to set
     */
    public void setKensaku_status(String kensaku_status) {
        this.kensaku_status = kensaku_status;
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
