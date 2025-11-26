/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.form;

import app.MeisaisyosaiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * OS3103_クレーム債権_明細詳細 アクションフォームクラス <br>
 */
public class KureemuSyosaiForm extends AppPagerActionForm {

    private static final long serialVersionUID = 1L; // serialVersionUID
    private MeisaisyosaiBean syosai_bean;				// 明細情報
    private String komoku1;							// 項目１タイトル
    private String komoku2;							// 項目２タイトル
    private String komoku3;							// 項目３タイトル
    private String komoku4;							// 項目４タイトル
    private String komoku5;							// 項目５タイトル
    private boolean kureemu_saiken;					// クレーム債権
    private int id; 									// リンククリックされた添付ファイル.id
    private List ar_anken_no;						// 滞留判定案件No
    
    /**
     * 変数初期化 <br>
     */
    public KureemuSyosaiForm() {
        
        super.gamenId = GS.OS3103;
        
        this.syosai_bean = null;
        this.komoku1 = GS.EMPTY_CHARCTER;
        this.komoku2 = GS.EMPTY_CHARCTER;
        this.komoku3 = GS.EMPTY_CHARCTER;
        this.komoku4 = GS.EMPTY_CHARCTER;
        this.komoku5 = GS.EMPTY_CHARCTER;
        this.ar_anken_no = null;
        this.kureemu_saiken = false;
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }
    
	/**
	 * @return the syosai_bean
	 */
	public MeisaisyosaiBean getSyosai_bean() {
		return syosai_bean;
	}

	/**
	 * @param syosai_bean the syosai_bean to set
	 */
	public void setSyosai_bean(MeisaisyosaiBean syosai_bean) {
		this.syosai_bean = syosai_bean;
	}

	/**
	 * @return the komoku1
	 */
	public String getKomoku1() {
		return komoku1;
	}

	/**
	 * @param komoku1 the komoku1 to set
	 */
	public void setKomoku1(String komoku1) {
		this.komoku1 = komoku1;
	}

	/**
	 * @return the komoku2
	 */
	public String getKomoku2() {
		return komoku2;
	}

	/**
	 * @param komoku2 the komoku2 to set
	 */
	public void setKomoku2(String komoku2) {
		this.komoku2 = komoku2;
	}

	/**
	 * @return the komoku3
	 */
	public String getKomoku3() {
		return komoku3;
	}

	/**
	 * @param komoku3 the komoku3 to set
	 */
	public void setKomoku3(String komoku3) {
		this.komoku3 = komoku3;
	}

	/**
	 * @return the komoku4
	 */
	public String getKomoku4() {
		return komoku4;
	}

	/**
	 * @param komoku4 the komoku4 to set
	 */
	public void setKomoku4(String komoku4) {
		this.komoku4 = komoku4;
	}

	/**
	 * @return the komoku5
	 */
	public String getKomoku5() {
		return komoku5;
	}

	/**
	 * @param komoku5 the komoku5 to set
	 */
	public void setKomoku5(String komoku5) {
		this.komoku5 = komoku5;
	}

	/**
	 * @return the kureemu_saiken
	 */
	public boolean isKureemu_saiken() {
		return kureemu_saiken;
	}

	/**
	 * @param kureemu_saiken the kureemu_saiken to set
	 */
	public void setKureemu_saiken(boolean kureemu_saiken) {
		this.kureemu_saiken = kureemu_saiken;
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

    public void reset(ActionMapping mapping, HttpServletRequest request){
        // クレーム債権
        this.kureemu_saiken = false;
    }
	/**
	 * @return the ar_anken_no
	 */
	public List getAr_anken_no() {
		return ar_anken_no;
	}

	/**
	 * @param ar_anken_no the ar_anken_no to set
	 */
	public void setAr_anken_no(List ar_anken_no) {
		this.ar_anken_no = ar_anken_no;
	}
}