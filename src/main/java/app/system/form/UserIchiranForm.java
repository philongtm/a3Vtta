/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 *  OS7106_ユーザマスタメンテナンス_一覧 アクションフォームクラス <br>
 */
public class UserIchiranForm extends AppPagerActionForm {
    
    /**  */
    private static final long serialVersionUID = 1L;          
    
    /**  リンククリックされた勘定先の明細.id */	
    private int id;    
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;
    
    /** 汎用１ */
    private String hanyo1;
    
	/** 汎用１セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo1;
    
    /** 業務フロー */
    private String gyoumu_huro;    
    
	/** 業務フローセレクトボックス用配列 */
    private LinkedHashMap ar_gyoumu_huro;
    
    /** ユーザID */
    private String user_id; 
    
	/** 氏名 */
    private String user_nm;
    
	/** E-Mail */
    private String email;
    
    private String action_flg;    
    
    /** ユーザID(検索用) */
    private String user_id_search;    
    
    /** 氏名(検索用) */
    private String user_nm_search;
    
    /** E-Mail(検索用) */
    private String email_search;
    
    /** 汎用１(検索用) */
    private String hanyo1_search;
    
    /** 業務フロー(検索用) */
    private String gyoumu_huro_search;
    
    /**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the ar_show
	 */
	public LinkedHashMap getAr_show() {
		return ar_show;
	}

	/**
	 * @param ar_show the ar_show to set
	 */
	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}

	// 変数初期化
    public UserIchiranForm() {
        super.gamenId = GS.OS7106;
        this.user_id = GS.EMPTY_CHARCTER;
        this.user_nm = GS.EMPTY_CHARCTER;
        this.hanyo1 = GS.EMPTY_CHARCTER;
        this.email = GS.EMPTY_CHARCTER;
        this.gyoumu_huro = GS.EMPTY_CHARCTER;
        this.hanyo1_search = GS.EMPTY_CHARCTER;
        this.action_flg = GS.EMPTY_CHARCTER;
        this.user_id_search = GS.EMPTY_CHARCTER;
        this.user_nm_search = GS.EMPTY_CHARCTER;
        this.email_search = GS.EMPTY_CHARCTER;
        this.gyoumu_huro_search = GS.EMPTY_CHARCTER;
        this.ar_hanyo1 = null;
        this.ar_gyoumu_huro = null;
        this.ar_show = null;
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

	/**
	 * @return the ar_hanyo1
	 */
	public LinkedHashMap getAr_hanyo1() {
		return ar_hanyo1;
	}

	/**
	 * @param ar_hanyo1 the ar_hanyo1 to set
	 */
	public void setAr_hanyo1(LinkedHashMap ar_hanyo1) {
		this.ar_hanyo1 = ar_hanyo1;
	}

	/**
	 * @return the hanyo1
	 */
	public String getHanyo1() {
		return hanyo1;
	}

	/**
	 * @param hanyo1 the hanyo1 to set
	 */
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}

	/**
	 * @return the ar_gyoumu_huro
	 */
	public LinkedHashMap getAr_gyoumu_huro() {
		return ar_gyoumu_huro;
	}

	/**
	 * @param ar_gyoumu_huro the ar_gyoumu_huro to set
	 */
	public void setAr_gyoumu_huro(LinkedHashMap ar_gyoumu_huro) {
		this.ar_gyoumu_huro = ar_gyoumu_huro;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the gyoumu_huro
	 */
	public String getGyoumu_huro() {
		return gyoumu_huro;
	}

	/**
	 * @param gyoumu_huro the gyoumu_huro to set
	 */
	public void setGyoumu_huro(String gyoumu_huro) {
		this.gyoumu_huro = gyoumu_huro;
	}

	/**
	 * @return the user_id
	 */
	public String getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	/**
	 * @return the user_nm
	 */
	public String getUser_nm() {
		return user_nm;
	}

	/**
	 * @param user_nm the user_nm to set
	 */
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}

	/**
	 * @return the hanyo1_search
	 */
	public String getHanyo1_search() {
		return hanyo1_search;
	}

	/**
	 * @param hanyo1_search the hanyo1_search to set
	 */
	public void setHanyo1_search(String hanyo1_search) {
		this.hanyo1_search = hanyo1_search;
	}

	/**
	 * @return the action_flg
	 */
	public String getAction_flg() {
		return action_flg;
	}

	/**
	 * @param action_flg the action_flg to set
	 */
	public void setAction_flg(String action_flg) {
		this.action_flg = action_flg;
	}

	/**
	 * @return the email_search
	 */
	public String getEmail_search() {
		return email_search;
	}

	/**
	 * @param email_search the email_search to set
	 */
	public void setEmail_search(String email_search) {
		this.email_search = email_search;
	}

	/**
	 * @return the gyoumu_huro_search
	 */
	public String getGyoumu_huro_search() {
		return gyoumu_huro_search;
	}

	/**
	 * @param gyoumu_huro_search the gyoumu_huro_search to set
	 */
	public void setGyoumu_huro_search(String gyoumu_huro_search) {
		this.gyoumu_huro_search = gyoumu_huro_search;
	}

	/**
	 * @return the user_id_search
	 */
	public String getUser_id_search() {
		return user_id_search;
	}

	/**
	 * @param user_id_search the user_id_search to set
	 */
	public void setUser_id_search(String user_id_search) {
		this.user_id_search = user_id_search;
	}

	/**
	 * @return the user_nm_search
	 */
	public String getUser_nm_search() {
		return user_nm_search;
	}

	/**
	 * @param user_nm_search the user_nm_search to set
	 */
	public void setUser_nm_search(String user_nm_search) {
		this.user_nm_search = user_nm_search;
	}
	
}
