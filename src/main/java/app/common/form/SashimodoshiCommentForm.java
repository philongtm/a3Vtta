/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package app.common.form;

import common.struts.adapter.action.ActionForm;

public class SashimodoshiCommentForm extends ActionForm {

    private String anken_no;		// 案件Ｎｏ．
    private ActionForm returnForm;	// 遷移元のActuinForm
    
    private String kanjo_cd;	// 勘定先ＣＤ
    private String kanjo_nm;	// 勘定先名称
    private String comment;		// 差戻コメント
    private String user_nm;	// 登録ユーザ名
    private String toroku_div; // 登録区分(2:差戻.3:転送,4:対象外,追加)
    
    
    // 変数初期化
    public SashimodoshiCommentForm() {
        this.anken_no = "";
        this.returnForm = null;
        this.kanjo_cd = "";
        this.kanjo_nm = "";
        this.comment = "";
        this.user_nm = "";
        this.toroku_div = "";
    }


    // アクセスメソッド
    
    public String getAnken_no() {
        return this.anken_no;
    }
    public void setAnken_no(String anken_no) {
        this.anken_no = anken_no;
    }

    public ActionForm getReturnForm() {
        return this.returnForm;
    }
    public void setReturnForm(ActionForm returnForm) {
        this.returnForm = returnForm;
    }
    
    public String getKanjo_cd() {
        return this.kanjo_cd;
    }
    public void setKanjo_cd(String kanjo_cd) {
        this.kanjo_cd = kanjo_cd;
    }

    public String getKanjo_nm() {
        return this.kanjo_nm;
    }
    public void setKanjo_nm(String kanjo_nm) {
        this.kanjo_nm = kanjo_nm;
    }

    public String getComment() {
        return this.comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
	public String getToroku_div() {
		return toroku_div;
	}
	public void setToroku_div(String toroku_div) {
		this.toroku_div = toroku_div;
	}
	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
}