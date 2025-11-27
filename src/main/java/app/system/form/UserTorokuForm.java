/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2009/11/04		SSC				課題No.21 ユーザマスタ　参照組織一覧の追加・削除制御対応
003		2010/01/08		SSC				課題No.230 ログインユーザでログインユーザを更新時対応
004		2011/06/30		SSC				案件No.D9059 他地域の権限を持つユーザでも、自分の担当地域権限は変更できるよう対応
005		2015/03/30		SSC				BJ201408049 IA化対応時の機能改善
006		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.system.form;

import app.SessionData;
import app.UserBean;
import common.AppContext;
import common.global.GS;
import common.struts.AppPagerActionForm;
import config.adapter.struts.action.ActionMapping;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  OS7107_ユーザマスタメンテナンス_登録 アクションフォームクラス <br>
 */
public class UserTorokuForm extends AppPagerActionForm {
									                // ユーザービンー
    
    /**  */
    private static final long serialVersionUID = 1L; 
    
    //課題No.230
    //追加開始
    /** ログインユーザIDフラグ */
    private boolean user_id_flg;
    //追加完了

	/**  リンククリックされた勘定先の明細.id */	
    private int id;    
    
    /** ユーザID */
    private String user_id; 
    
	/** 氏名 */
    private String user_nm;
    
	/** 会社名 */
    private String company_nm;
    
	/** 所属組織名 */
    private String soshiki_nm;
    
	/** メールアドレス */
    private String email_addr;
    
	/** メール配信 */
    private String mail_haisin_kbn;
    
	/** 帳票出力言語 */
    private String printout_default_lang_kbn;
    
    /** 業務フロー */
    private String gyoumu_huro;
    
	/** 業務フローセレクトボックス用配列 */
    private LinkedHashMap ar_gyoumu_huro; 
    
	/** 業務フローセレクトボックス用配列 */
    private LinkedHashMap ar_gyoumu_huro_hid; 
    
	/** 対象のステム管理者フラグ */
    private String admin_kanri_flg;
    
    /** ログインユーザのシステム管理者フラグ */
    private String admini_flg;
    
	/** 業務フローパターン【リスト】 */
	private List ar_workFlow;	
    
	/** 汎用１セレクトボックス用配列 */
    private LinkedHashMap ar_hanyo1;
    
	/** システム管理者削除フラグ */
    private String admin_delete_flg;
    
	/** チェック判断フラグ */
    private String chk_handan_flg;
    
	/** 表示件数セレクトボックス用配列 */
    private LinkedHashMap ar_show;

	/** タイトル */
    private String title;
    
	/** 業務フロー一覧 */
    private List ar_gyoumu_huro_itiran;
    
    private String gyoumu_huro_index;
    
    private String default_flg_checked;
    
	/** 既定フラグのステータス */  
    private String rdo_status;
        
	/** 既定フラグチェックオフのid */  
    private String gyoumu_itiran_id;
        
	/** ログインユーザ */  
	private String login_user_flg;
	
	/** ログインユーザ */  
	private String clk_hanyou1_cd;
	
	private String system_kbn;
	
	private String sateikaisya_cd;
	
	private String hanyou1;
	
	// 課題No.21 ユーザマスタ　参照組織一覧の追加・削除制御対応
	private AppContext appContext = null;				                              // ＡＰＰコンテキスト
	private UserBean user_bean = null;
	private SessionData cmnData = null;					                          // 機能共通セッション
	
	// 案件No.D9059 担当チェック情報をhiddenで保持 
    private String[] tantou_chk_hidden		= null;

	// メール配信先の一覧
	/** メール配信先の一覧【リスト】 */
	private List<HashMap<String, String>> ar_haishinsaki;
	/** 削除ボタン */
	private String haishinDel;
	/** 最新の基準日 */
	private String saishinYm;
	/** 汎用3タイトル */
	private String hanyou3Title;
	/** メール配信先表示フラグ（01のみ） */
	private String haishinHyoujiFlg;
	/** 一覧でメール配信先の削除・追加対象ID */
	private String haishinHyoujiId;

	// 担当組織の一覧
	/** ログインユーザ担当組織【リスト】 */
	private List<HashMap<String, String>> ar_loginUserSoshiki;
	/** 汎用4ラベル表示フラグ 表示：1、非表示：0 */
	private String hanyou4LabelFlg;
	
	/** 楽観排他用 ユーザーレベルマスタの更新日時を保持 */
	private Date userLastUPD_DT;
    
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

	/**
	 * @return the gyoumu_itiran_id
	 */
	public String getGyoumu_itiran_id() {
		return gyoumu_itiran_id;
	}

	/**
	 * @param gyoumu_itiran_id the gyoumu_itiran_id to set
	 */
	public void setGyoumu_itiran_id(String gyoumu_itiran_id) {
		this.gyoumu_itiran_id = gyoumu_itiran_id;
	}

	// 変数初期化
    public UserTorokuForm(AppContext appcontext) {
        super.gamenId = GS.OS7107;
        //課題No.230
        //追加開始
        this.user_id_flg = Boolean.FALSE;
        //追加完了
        this.system_kbn = GS.EMPTY_CHARCTER;
        this.sateikaisya_cd = GS.EMPTY_CHARCTER;
        this.user_id = GS.EMPTY_CHARCTER;
        this.user_nm = GS.EMPTY_CHARCTER;
        this.company_nm = GS.EMPTY_CHARCTER;
        this.soshiki_nm = GS.EMPTY_CHARCTER;
        this.email_addr = GS.EMPTY_CHARCTER;
        this.mail_haisin_kbn = GS.EMPTY_CHARCTER;
        this.printout_default_lang_kbn = GS.EMPTY_CHARCTER;
        this.gyoumu_huro = GS.EMPTY_CHARCTER;
        this.admin_kanri_flg = GS.EMPTY_CHARCTER;
        this.admini_flg = GS.EMPTY_CHARCTER; 
        this.admin_delete_flg = GS.EMPTY_CHARCTER;
        this.gyoumu_huro_index = GS.EMPTY_CHARCTER;
        this.gyoumu_itiran_id = GS.EMPTY_CHARCTER;
        this.default_flg_checked = GS.EMPTY_CHARCTER;
        this.login_user_flg = GS.EMPTY_CHARCTER;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.clk_hanyou1_cd = GS.EMPTY_CHARCTER;
        this.chk_handan_flg = "0";
        this.title = GS.EMPTY_CHARCTER;
        this.ar_gyoumu_huro = null;
        this.ar_hanyo1 = null;
        this.ar_show = null;
        this.ar_workFlow = null;
        this.ar_gyoumu_huro_itiran = null;
        this.ar_gyoumu_huro_hid = null;
        this.rdo_status = null;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
		this.appContext = appcontext;
		cmnData = appContext.getCMN();
		user_bean = cmnData.getUser_bean();
		// 案件No.D9059 担当チェック情報をhiddenで保持
		this.tantou_chk_hidden = null;
		
		// メール配信先
		this.ar_haishinsaki = null;
		this.haishinDel = null;
		this.saishinYm = null;
		this.hanyou3Title = null;
		this.haishinHyoujiFlg = null;
		this.haishinHyoujiId = null;
		// 担当組織一覧
		this.ar_loginUserSoshiki = null;
		this.hanyou4LabelFlg = null;
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
	 * @return the gyoumu_huro
	 */
	public String getGyoumu_huro() {
		return gyoumu_huro;
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
	 * @return the admin_delete_flg
	 */
	public String getAdmin_delete_flg() {
		return admin_delete_flg;
	}

	/**
	 * @param admin_delete_flg the admin_delete_flg to set
	 */
	public void setAdmin_delete_flg(String admin_delete_flg) {
		this.admin_delete_flg = admin_delete_flg;
	}

	/**
	 * @return the admin_kanri_flg
	 */
	public String getAdmin_kanri_flg() {
		return admin_kanri_flg;
	}

	/**
	 * @param admin_kanri_flg the admin_kanri_flg to set
	 */
	public void setAdmin_kanri_flg(String admin_kanri_flg) {
		this.admin_kanri_flg = admin_kanri_flg;
	}

	/**
	 * @return the ar_workFlow
	 */
	public List getAr_workFlow() {
		return ar_workFlow;
	}

	/**
	 * @param ar_workFlow the ar_workFlow to set
	 */
	public void setAr_workFlow(List ar_workFlow) {
		this.ar_workFlow = ar_workFlow;
	}

	/**
	 * @return the chk_handan_flg
	 */
	public String getChk_handan_flg() {
		return chk_handan_flg;
	}

	/**
	 * @param chk_handan_flg the chk_handan_flg to set
	 */
	public void setChk_handan_flg(String chk_handan_flg) {
		this.chk_handan_flg = chk_handan_flg;
	}

	/**
	 * @return the company_nm
	 */
	public String getCompany_nm() {
		return company_nm;
	}

	/**
	 * @param company_nm the company_nm to set
	 */
	public void setCompany_nm(String company_nm) {
		this.company_nm = company_nm;
	}

	/**
	 * @return the email_addr
	 */
	public String getEmail_addr() {
		return email_addr;
	}

	/**
	 * @param email_addr the email_addr to set
	 */
	public void setEmail_addr(String email_addr) {
		this.email_addr = email_addr;
	}

	/**
	 * @return the mail_haisin_kbn
	 */
	public String getMail_haisin_kbn() {
		return mail_haisin_kbn;
	}

	/**
	 * @param mail_haisin_kbn the mail_haisin_kbn to set
	 */
	public void setMail_haisin_kbn(String mail_haisin_kbn) {
		this.mail_haisin_kbn = mail_haisin_kbn;
	}

	/**
	 * @return the printout_default_lang_kbn
	 */
	public String getPrintout_default_lang_kbn() {
		return printout_default_lang_kbn;
	}

	/**
	 * @param printout_default_lang_kbn the printout_default_lang_kbn to set
	 */
	public void setPrintout_default_lang_kbn(String printout_default_lang_kbn) {
		this.printout_default_lang_kbn = printout_default_lang_kbn;
	}

	/**
	 * @return the soshiki_nm
	 */
	public String getSoshiki_nm() {
		return soshiki_nm;
	}

	/**
	 * @param soshiki_nm the soshiki_nm to set
	 */
	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the gyoumu_huro_index
	 */
	public String getGyoumu_huro_index() {
		return gyoumu_huro_index;
	}

	/**
	 * @param gyoumu_huro_index the gyoumu_huro_index to set
	 */
	public void setGyoumu_huro_index(String gyoumu_huro_index) {
		this.gyoumu_huro_index = gyoumu_huro_index;
	}
	
    public void reset(ActionMapping mapping, HttpServletRequest request){
    	// 課題No.21 ユーザマスタ　参照組織一覧の追加・削除制御対応
    	// 追加開始
    	if(user_bean.getComSystemManager_flg().equals(GS.ON)){
        	this.setAdmin_kanri_flg("0");	
    	}
    	// 追加完了
    	this.setMail_haisin_kbn("0");
    }

	/**
	 * @return the ar_gyoumu_huro_hid
	 */
	public LinkedHashMap getAr_gyoumu_huro_hid() {
		return ar_gyoumu_huro_hid;
	}

	/**
	 * @param ar_gyoumu_huro_hid the ar_gyoumu_huro_hid to set
	 */
	public void setAr_gyoumu_huro_hid(LinkedHashMap ar_gyoumu_huro_hid) {
		this.ar_gyoumu_huro_hid = ar_gyoumu_huro_hid;
	}

	/**
	 * @return the rdo_status
	 */
	public String getRdo_status() {
		return rdo_status;
	}

	/**
	 * @param rdo_status the rdo_status to set
	 */
	public void setRdo_status(String rdo_status) {
		this.rdo_status = rdo_status;
	}

	/**
	 * @return the ar_gyoumu_huro_itiran
	 */
	public List getAr_gyoumu_huro_itiran() {
		return ar_gyoumu_huro_itiran;
	}

	/**
	 * @param ar_gyoumu_huro_itiran the ar_gyoumu_huro_itiran to set
	 */
	public void setAr_gyoumu_huro_itiran(List ar_gyoumu_huro_itiran) {
		this.ar_gyoumu_huro_itiran = ar_gyoumu_huro_itiran;
	}

	/**
	 * @return the default_flg_checked
	 */
	public String getDefault_flg_checked() {
		return default_flg_checked;
	}

	/**
	 * @param default_flg_checked the default_flg_checked to set
	 */
	public void setDefault_flg_checked(String default_flg_checked) {
		this.default_flg_checked = default_flg_checked;
	}

	/**
	 * @return the login_user_flg
	 */
	public String getLogin_user_flg() {
		return login_user_flg;
	}

	/**
	 * @param login_user_flg the login_user_flg to set
	 */
	public void setLogin_user_flg(String login_user_flg) {
		this.login_user_flg = login_user_flg;
	}

	/**
	 * @return the clk_hanyou1_cd
	 */
	public String getClk_hanyou1_cd() {
		return clk_hanyou1_cd;
	}

	/**
	 * @param clk_hanyou1_cd the clk_hanyou1_cd to set
	 */
	public void setClk_hanyou1_cd(String clk_hanyou1_cd) {
		this.clk_hanyou1_cd = clk_hanyou1_cd;
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
	 * @return the hanyou1
	 */
	public String getHanyou1() {
		return hanyou1;
	}

	/**
	 * @param hanyou1 the hanyou1 to set
	 */
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	/**
	 * ログインユーザのシステム管理者フラグ
	 */
	public String getAdmini_flg() {
		return admini_flg;
	}

	public void setAdmini_flg(String admini_flg) {
		this.admini_flg = admini_flg;
	}
    
    //課題No.230
    //追加開始
	/**
	 * ログインユーザと更新対象ユーザの判定
	 */
    public boolean isUser_id_flg() {
		return user_id_flg;
	}

    public void setUser_id_flg(boolean user_id_flg) {
		this.user_id_flg = user_id_flg;
	}
    //追加完了
    
	// 案件No.D9059 担当チェック情報をhiddenで保持
	/**
	 * @param tantou_chk_hidden the tantou_chk_hidden to set
	 */
	public void setTantou_chk_hidden(String[] tantou_chk_hidden) {
		this.tantou_chk_hidden = tantou_chk_hidden;
	}

	/**
	 * @return the tantou_chk_hidden
	 */
	public String[] getTantou_chk_hidden() {
		return tantou_chk_hidden;
	}

	/**
	 * メール配信先の一覧【リスト】
	 * @return ar_haishinsaki
	 */
	public List<HashMap<String, String>> getAr_haishinsaki() {
		return ar_haishinsaki;
	}

	/**
	 * メール配信先の一覧【リスト】
	 * @param ar_haishinsaki
	 */
	public void setAr_haishinsaki(List<HashMap<String, String>> ar_haishinsaki) {
		this.ar_haishinsaki = ar_haishinsaki;
	}

	/**
	 * 削除ボタン
	 * @return haishinDel
	 */
	public String getHaishinDel() {
		return haishinDel;
	}

	/**
	 * 削除ボタン
	 * @param haishinDel
	 */
	public void setHaishinDel(String haishinDel) {
		this.haishinDel = haishinDel;
	}

	/**
	 * 最新の査定期
	 * @return saishinYm
	 */
	public String getSaishinYm() {
		return saishinYm;
	}

	/**
	 * 最新の査定期
	 * @param saishinYm
	 */
	public void setSaishinYm(String saishinYm) {
		this.saishinYm = saishinYm;
	}

	/**
	 * 汎用3タイトル
	 * @return hanyou3Title
	 */
	public String getHanyou3Title() {
		return hanyou3Title;
	}

	/**
	 * 汎用3タイトル
	 * @param hanyou3Title
	 */
	public void setHanyou3Title(String hanyou3Title) {
		this.hanyou3Title = hanyou3Title;
	}

	/**
	 * メール配信先表示フラグ（01のみ）
	 * @return haishinHyoujiFlg
	 */
	public String getHaishinHyoujiFlg() {
		return haishinHyoujiFlg;
	}

	/**
	 * メール配信先表示フラグ（01のみ）
	 * @param haishinHyoujiFlg
	 */
	public void setHaishinHyoujiFlg(String haishinHyoujiFlg) {
		this.haishinHyoujiFlg = haishinHyoujiFlg;
	}

	/**
	 * 一覧でメール配信先の削除・追加対象ID
	 * @return haishinHyoujiId
	 */
	public String getHaishinHyoujiId() {
		return haishinHyoujiId;
	}

	/**
	 * 一覧でメール配信先の削除・追加対象ID
	 * @param haishinHyoujiId
	 */
	public void setHaishinHyoujiId(String haishinHyoujiId) {
		this.haishinHyoujiId = haishinHyoujiId;
	}

	/**
	 * ログインユーザ担当組織【リスト】
	 * @return ar_loginUserSoshiki
	 */
	public List<HashMap<String, String>> getAr_loginUserSoshiki() {
		return ar_loginUserSoshiki;
	}

	/**
	 * ログインユーザ担当組織【リスト】
	 * @param ar_loginUserSoshiki セットする ar_loginUserSoshiki
	 */
	public void setAr_loginUserSoshiki(
			List<HashMap<String, String>> ar_loginUserSoshiki) {
		this.ar_loginUserSoshiki = ar_loginUserSoshiki;
	}

	/**
	 * 汎用4ラベル表示フラグ 表示：1、非表示：0
	 * @return hanyou4LabelFlg
	 */
	public String getHanyou4LabelFlg() {
		return hanyou4LabelFlg;
	}

	/**
	 * 汎用4ラベル表示フラグ 表示：1、非表示：0
	 * @param hanyou4LabelFlg セットする hanyou4LabelFlg
	 */
	public void setHanyou4LabelFlg(String hanyou4LabelFlg) {
		this.hanyou4LabelFlg = hanyou4LabelFlg;
	}

	/**
	 * 楽観排他用 ユーザーレベルマスタの更新日時を保持
	 * @return userLastUPD_DT
	 */
	public Date getUserLastUPD_DT() {
		return userLastUPD_DT;
	}

	/**
	 * 楽観排他用 ユーザーレベルマスタの更新日時を保持
	 * @param userLastUPD_DT セットする userLastUPD_DT
	 */
	public void setUserLastUPD_DT(Date userLastUPD_DT) {
		this.userLastUPD_DT = userLastUPD_DT;
	}

}
