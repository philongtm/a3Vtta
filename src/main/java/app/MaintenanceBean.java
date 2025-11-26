/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app;

import common.global.GS;

/**
 * マスタメンテナンス情報Beanクラス
 */
public class MaintenanceBean {

    private String pattern_id;		// パターンID

    
    
    // 変数初期化
    public MaintenanceBean() {
        this.pattern_id = GS.EMPTY_CHARCTER;
    }

    
    // アクセスメソッド   

    //id
	public String getPattern_id() {
		return pattern_id;
	}
	public void setPattern_id(String pattern_id) {
		this.pattern_id = pattern_id;
	}     
}