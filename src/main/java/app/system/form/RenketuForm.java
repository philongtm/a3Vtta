/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package app.system.form;

import common.struts.AppPagerActionForm;
import common.struts.adapter.upload.FormFile;



public class RenketuForm extends AppPagerActionForm {
	
	// アップロードファイル
	private FormFile fileUp;	// アップロードファイル

    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }

    // 変数初期化
    public RenketuForm() {
        this.fileUp = null;
    }
    
	// アップロードファイル
	public FormFile getFileUp() {
		return fileUp;
	}

	// アップロードファイル
	public void setFileUp(FormFile fileUp) {
		this.fileUp = fileUp;
	}

}