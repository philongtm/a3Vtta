/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

/**
 * OC1106_査定_承認 アクションフォームクラス
 * 
 */
public class SyoninSyosaiForm extends AppPagerActionForm {
	
	private String tabValue;			//カレントタブ
      
    //変数初期化
    public SyoninSyosaiForm() {
    	super.gamenId			= GS.OC1107;
    	tabValue = "2";
    }
    
    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    /**
	 * @return カレントタブ値を戻します。
	 */
	public String getTabValue() {
		return tabValue;
	}

    /**
	 * @return カレントタブ値を設定。
	 */
	public void setTabValue(String tabValue) {
		this.tabValue = tabValue;
	}
}