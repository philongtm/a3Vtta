/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.tairyu.form;

import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OB2101_対象先選定_承認一覧 アクションフォームクラス
 * 
 */
public class SenteisyoninForm extends AppPagerActionForm {

    private LinkedHashMap ar_show;			// 表示件数セレクトボックス用配列
    private String ikkatu_syonin;			// 一括承認チェックボックス
    private String anken_no;				// リンククリックされた勘定先の明細.査定案件No.
    private int id;						// リンククリックされた勘定先の明細.id
	private static final String CHECKBOX_STATUS_OFF  = "off";			//承認チェック
      
    // 変数初期化
    public SenteisyoninForm() {
    	super.gamenId = GS.OB2104;
        this.ar_show = null;
        this.ikkatu_syonin = CHECKBOX_STATUS_OFF;
        this.anken_no = GS.EMPTY_CHARCTER;
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
	// 表示件数配列
	public LinkedHashMap getAr_show() {
		return ar_show;
	}
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}
    // 一括承認チェックボックス
    public String getIkkatu_syonin() {
        return ikkatu_syonin;
    }
    public void setIkkatu_syonin(String ikkatu_syonin) {
        this.ikkatu_syonin = ikkatu_syonin;
    }
	// 査定案件No.
	public String getAnken_no() {
		return anken_no;
	}
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}
	// id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	// reset
	public void reset(ActionMapping mapping, HttpServletRequest request){
		setIkkatu_syonin(CHECKBOX_STATUS_OFF);
        List meisaiBean = super.getList();
        if (meisaiBean != null) {
            for (int i = 0; i < meisaiBean.size(); i++) {
            	TorihikisakiBean tori_bean = (TorihikisakiBean)meisaiBean.get(i);
            	tori_bean.setSyonin_chk(CHECKBOX_STATUS_OFF);
            }
        }
	}
}