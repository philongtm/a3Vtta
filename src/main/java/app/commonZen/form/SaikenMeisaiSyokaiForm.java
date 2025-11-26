/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/05/21		SSC				1.5次版機能組込
******************************************************************************/
package app.commonZen.form;

import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 債権明細タブFormクラス
 */
public class SaikenMeisaiSyokaiForm extends AppPagerActionForm {

	/**
	 * index
	 */
	private String indexId;
	/**
	 * 債権残高合計
	 */
	private String saimu_zandaka;
	/**
	 * 保証債務合計
	 */
	private String hosyo_saimu;
	/**
	 * 引当金合計
	 */
	private String hikiatekin;
	/**
	 * 滞留債権計
	 */
	private String tairyu_saimu;
	
	/**
	 * 処理回数制御
	 */
	private int initmode;
    /** 件数セレクトボックス値 */
    private LinkedHashMap showList;
	
	private String hozen;
	private String sonotakaisyu;
	private String riko_kenen;
	
	
    // No871, 2008/06/16, SJA渡辺, コンストラクタで初期化するように修正
    /** コンストラクタ */
    public SaikenMeisaiSyokaiForm() {
    	this.setPager(new ArrayList());
		this.setAr_meisai(new ArrayList());
    }

    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	
	public String getHozen() {
		return hozen;
	}
	public void setHozen(String hozen) {
		this.hozen = hozen;
	}
	public String getRiko_kenen() {
		return riko_kenen;
	}
	public void setRiko_kenen(String riko_kenen) {
		this.riko_kenen = riko_kenen;
	}
	public String getSonotakaisyu() {
		return sonotakaisyu;
	}
	public void setSonotakaisyu(String sonotakaisyu) {
		this.sonotakaisyu = sonotakaisyu;
	}
	
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	
	public LinkedHashMap getShowList() {
		return showList;
	}
	public void setShowList(LinkedHashMap showList) {
		this.showList = showList;
	}
	/**
	 * @return hikiatekin を戻します。
	 */
	public String getHikiatekin() {
		return hikiatekin;
	}
	/**
	 * @param hikiatekin hikiatekin を設定。
	 */
	public void setHikiatekin(String hikiatekin) {
		this.hikiatekin = hikiatekin;
	}
	/**
	 * @return hosyo_saimu を戻します。
	 */
	public String getHosyo_saimu() {
		return hosyo_saimu;
	}
	/**
	 * @param hosyo_saimu hosyo_saimu を設定。
	 */
	public void setHosyo_saimu(String hosyo_saimu) {
		this.hosyo_saimu = hosyo_saimu;
	}
	/**
	 * @return indexId を戻します。
	 */
	public String getIndexId() {
		return indexId;
	}
	/**
	 * @param indexId indexId を設定。
	 */
	public void setIndexId(String indexId) {
		this.indexId = indexId;
	}
	/**
	 * @return saimu_zandaka を戻します。
	 */
	public String getSaimu_zandaka() {
		return saimu_zandaka;
	}
	/**
	 * @param saimu_zandaka saimu_zandaka を設定。
	 */
	public void setSaimu_zandaka(String saimu_zandaka) {
		this.saimu_zandaka = saimu_zandaka;
	}
	/**
	 * @return tairyu_saimu を戻します。
	 */
	public String getTairyu_saimu() {
		return tairyu_saimu;
	}
	/**
	 * @param tairyu_saimu tairyu_saimu を設定。
	 */
	public void setTairyu_saimu(String tairyu_saimu) {
		this.tairyu_saimu = tairyu_saimu;
	}
}
