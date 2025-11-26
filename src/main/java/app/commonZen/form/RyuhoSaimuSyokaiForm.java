/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/10/31		水口			結合テスト 　障害No.0035対応　処理回数を追加
******************************************************************************/
package app.commonZen.form;

import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 留保債務タブFormクラス
 */
public class RyuhoSaimuSyokaiForm extends AppPagerActionForm {

	/**
	 * 債務総計
	 */
	private String saimu_kei;
	/**
	 * 留保債務計
	 */
	private String ryuhosaimu_kei;
	
	/**
	 * 処理回数制御
	 */
	private int initmode;
    /** 件数セレクトボックス値 */
    private LinkedHashMap showList;
    
	//障害No.0035
	//追加開始
    /** 処理回数 */
    private String syoriKaisu;
	//追加完了
    
    // No871, 2008/06/16, SJA渡辺, コンストラクタで初期化するように修正
    /** コンストラクタ */
    public RyuhoSaimuSyokaiForm() {
    	this.setPager(new ArrayList());
		this.setAr_meisai(new ArrayList());
    }
	
    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
    
    
	public LinkedHashMap getShowList() {
		return showList;
	}
	public void setShowList(LinkedHashMap showList) {
		this.showList = showList;
	}
	public int getInitmode() {
		return initmode;
	}
	public void setInitmode(int initmode) {
		this.initmode = initmode;
	}
	/**
	 * @return ryuhosaimu_kei を戻します。
	 */
	public String getRyuhosaimu_kei() {
		return ryuhosaimu_kei;
	}
	/**
	 * @param ryuhosaimu_kei ryuhosaimu_kei を設定。
	 */
	public void setRyuhosaimu_kei(String ryuhosaimu_kei) {
		this.ryuhosaimu_kei = ryuhosaimu_kei;
	}
	/**
	 * @return saimu_kei を戻します。
	 */
	public String getSaimu_kei() {
		return saimu_kei;
	}
	/**
	 * @param saimu_kei saimu_kei を設定。
	 */
	public void setSaimu_kei(String saimu_kei) {
		this.saimu_kei = saimu_kei;
	}
	//障害No.0035
	//追加開始
	/**
	 * @return syoriKaisu を戻します。
	 */
	public String getSyoriKaisu() {
		return syoriKaisu;
	}
	/**
	 * @param syoriKaisu syoriKaisu を設定。
	 */
	public void setSyoriKaisu(String syoriKaisu) {
		this.syoriKaisu = syoriKaisu;
	}
	//追加完了
}
