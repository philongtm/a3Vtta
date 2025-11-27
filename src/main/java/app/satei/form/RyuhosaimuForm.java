/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.form;

import app.MeisaisyosaiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OC1105_査定_留保債務登録 アクションフォームクラス
 * 
 */
public class RyuhosaimuForm extends AppPagerActionForm {
	
    private String saimusoukei;			//債務総計
    private String ryuhosaimukei;			//留保債務計
    private String chkIkkatu;				//一括判定チェックボックス
    private LinkedHashMap showList;		//件数セレクトボックス
    private String tuka;					//通貨コード
    
    public RyuhosaimuForm() {
    	super.gamenId						= GS.OC1105;
		this.showList 						= null;
		this.chkIkkatu 						= GS.OFF;
		this.saimusoukei 					= GS.EMPTY_CHARCTER;
		this.ryuhosaimukei 					= GS.EMPTY_CHARCTER;
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
	 * チェックボックス用リセット処理
	 */	
	public void reset(ActionMapping aMap, HttpServletRequest req){

		//一括判定チェックボックス初期化
		this.setChkIkkatu(GS.OFF);
		
		//Beanの判定チェックボックス初期化、備考トリム
        List<MeisaisyosaiBean> list = super.getList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
            	list.get(i).setRyuhosaimu(GS.EMPTY_CHARCTER);
            	list.get(i).setBiko(Function.trim(list.get(i).getBiko()));
            }
        }
	}

	//一括判定チェックボックス
	public String getChkIkkatu() {
		return chkIkkatu;
	}
	public void setChkIkkatu(String chkIkkatu) {
		this.chkIkkatu = chkIkkatu;
	}

	//留保債務計
	public String getRyuhosaimukei() {
		return ryuhosaimukei;
	}
	public void setRyuhosaimukei(String ryuhosaimukei) {
		this.ryuhosaimukei = ryuhosaimukei;
	}

	//債務総計
	public String getSaimusoukei() {
		return saimusoukei;
	}
	public void setSaimusoukei(String saimusoukei) {
		this.saimusoukei = saimusoukei;
	}

	//表示件数セレクトボックス
	public LinkedHashMap getShowList() {
		return showList;
	}
	public void setShowList(LinkedHashMap showList) {
		this.showList = showList;
	}

	//通貨
	public String getTuka() {
		return tuka;
	}
	public void setTuka(String tuka) {
		this.tuka = tuka;
	}
}