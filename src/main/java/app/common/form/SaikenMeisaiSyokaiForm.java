/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * OZ6101_滞留債権照会タブ  アクションフォームクラス
 * 
 */
public class SaikenMeisaiSyokaiForm extends AppPagerActionForm {

    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private String saiken_zankei;			// 債権残高合計
    private String hosyo_saimu;			// 保証債務合計
    private String hikiatekin;				// 引当金合計
    private String tairyu_saimu;			// 滞留債権系
    private String tuuka_cd;				// 通貨
    private String hanyo1_title;			// 汎用1
    private String komoku1;				// 項目1
    private String komoku3;				// 項目3
    private int id;						// リンククリックされた勘定先の明細.id
      
    // 変数初期化
    public SaikenMeisaiSyokaiForm() {
    	
    	super.gamenId = GS.OZ6105;
        this.ar_show = null;
        this.tuuka_cd = null;
        this.saiken_zankei = null;
        this.hosyo_saimu = null;
        this.hikiatekin = null;
        this.tairyu_saimu = null;
        this.hanyo1_title = null;
        this.komoku1 = null;
        this.komoku3 = null;
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
    // アクセスメソッド   

	//表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
	//通貨
	public String getTuuka_cd() {
		return tuuka_cd;
	}
	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	//債権残高合計
	public String getSaiken_zankei() {
		return saiken_zankei;
	}
	public void setSaiken_zankei(String saiken_zankei) {
		this.saiken_zankei = saiken_zankei;
	}
	//保証債務合計
	public String getHosyo_saimu() {
		return hosyo_saimu;
	}

	public void setHosyo_saimu(String hosyo_saimu) {
		this.hosyo_saimu = hosyo_saimu;
	}
	//引当金合計
	public String getHikiatekin() {
		return hikiatekin;
	}
	public void setHikiatekin(String hikiatekin) {
		this.hikiatekin = hikiatekin;
	}
	//滞留債権系
	public String getTairyu_saimu() {
		return tairyu_saimu;
	}

	public void setTairyu_saimu(String tairyu_saimu) {
		this.tairyu_saimu = tairyu_saimu;
	}
	//汎用1
	public String getHanyo1_title() {
		return hanyo1_title;
	}
	public void setHanyo1_title(String hanyo1_title) {
		this.hanyo1_title = hanyo1_title;
	}
	//項目1
	public String getKomoku1() {
		return komoku1;
	}
	public void setKomoku1(String komoku1) {
		this.komoku1 = komoku1;
	}
	//項目3
	public String getKomoku3() {
		return komoku3;
	}
	public void setKomoku3(String komoku3) {
		this.komoku3 = komoku3;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}