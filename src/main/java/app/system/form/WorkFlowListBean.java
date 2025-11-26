/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.form;

import common.global.GS;

/**
 *  業務フローパターンビンー <br>
 */
public class WorkFlowListBean{
                
    /** 明細.id */
    private String id;
    
    /** 業務フローパターンシステム区分 */
    private String system_kbn;
    
    /** 業務フローパターン査定会社コード */
    private String sateikaisya_cd;
    
    /** 業務フローパターン既定フラグ */
    private String default_flg;
    
    /** パターンID */
    private String pattern_id;
    
    /** パターン名称 */
    private String pattern_name_jp;
    
    /** システム管理者専用フラグ */
    private String admin_senyo_flg;
    
    /** 削除ボタン */
    private String delete_btn_flg;
    
	// 変数初期化
    public WorkFlowListBean() {
        this.system_kbn = GS.EMPTY_CHARCTER;
        this.sateikaisya_cd = GS.EMPTY_CHARCTER;
        this.default_flg = GS.EMPTY_CHARCTER;
        this.pattern_id = GS.EMPTY_CHARCTER;
        this.pattern_name_jp = GS.EMPTY_CHARCTER;
        this.admin_senyo_flg = GS.EMPTY_CHARCTER;
    }

	/**
	 * @return the admin_senyo_flg
	 */
	public String getAdmin_senyo_flg() {
		return admin_senyo_flg;
	}

	/**
	 * @param admin_senyo_flg the admin_senyo_flg to set
	 */
	public void setAdmin_senyo_flg(String admin_senyo_flg) {
		this.admin_senyo_flg = admin_senyo_flg;
	}

	/**
	 * @return the default_flg
	 */
	public String getDefault_flg() {
		return default_flg;
	}

	/**
	 * @param default_flg the default_flg to set
	 */
	public void setDefault_flg(String default_flg) {
		this.default_flg = default_flg;
	}

	/**
	 * @return the pattern_id
	 */
	public String getPattern_id() {
		return pattern_id;
	}

	/**
	 * @param pattern_id the pattern_id to set
	 */
	public void setPattern_id(String pattern_id) {
		this.pattern_id = pattern_id;
	}

	/**
	 * @return the pattern_name_jp
	 */
	public String getPattern_name_jp() {
		return pattern_name_jp;
	}

	/**
	 * @param pattern_name_jp the pattern_name_jp to set
	 */
	public void setPattern_name_jp(String pattern_name_jp) {
		this.pattern_name_jp = pattern_name_jp;
	}

	/**
	 * @return the sateikaisya_cd
	 */
	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}

	/**
	 * @param sateikaisya_cd the sateikaisya_cd to set
	 */
	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}

	/**
	 * @return the system_kbn
	 */
	public String getSystem_kbn() {
		return system_kbn;
	}

	/**
	 * @param system_kbn the system_kbn to set
	 */
	public void setSystem_kbn(String system_kbn) {
		this.system_kbn = system_kbn;
	}

	/**
	 * @return the delete_btn_flg
	 */
	public String getDelete_btn_flg() {
		return delete_btn_flg;
	}

	/**
	 * @param delete_btn_flg the delete_btn_flg to set
	 */
	public void setDelete_btn_flg(String delete_btn_flg) {
		this.delete_btn_flg = delete_btn_flg;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}    

	
}
