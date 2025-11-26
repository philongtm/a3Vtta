/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2014/03/30		SSC				BJ201408049 IA化対応時の機能改善
003		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app;


import app.system.form.TantouBean;
import common.global.GS;

import java.util.HashMap;
import java.util.List;

/**
 * ユーザメンテナンス情報Beanクラス
 * 
 */
public class UserMaintenanceBean {

	private String id						= null;	// 明細ID
	private String user_id					= null;	// ユーザID
    private String user_nm					= null;	// 氏名
    private String kaisya_nm				= null;	// 会社名
    private String syozoku_busyo_nm		= null;	// 所属部署名
    private String mail_address			= null;	// メールアドレス
	private String tyohyo_syuturyoku_lang	= null;	// 帳票出力言語
	private String mail_haishin			= null;	// メール配信
	private String admin_flg				= null;	// システム管理者フラグ
	private String workflow_h_nm			= null;	// 表示用業務フローパターン名称
	private List workflow_list				= null;	// 業務フローパターン【リスト】
	private String printout_lang_kbn		= null;	// 帳票出力言語(区分値)
	private List<HashMap<String,String>> soushinList = null;		// 送信先選択【リスト】
	private List<HashMap<String,String>> hyoujiSoushinList = null;	// 表示用送信先選択【リスト】
	private List<TantouBean> tantoSoshikiList = null;				// 担当組織【リスト】
	private String ken_System_Kbn = null;							// システム区分（担当組織選択検索条件）
	private String ken_Hanyou1 = null;								// 汎用1（担当組織選択検索条件）
	private String ken_Hanyou2 = null;								// 汎用2（担当組織選択検索条件）
	
	
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		id						= GS.EMPTY_CHARCTER;
		user_id					= GS.EMPTY_CHARCTER;
		user_nm					= GS.EMPTY_CHARCTER;
		kaisya_nm				= GS.EMPTY_CHARCTER;
		syozoku_busyo_nm		= GS.EMPTY_CHARCTER;
		mail_address			= GS.EMPTY_CHARCTER;
		tyohyo_syuturyoku_lang	= GS.EMPTY_CHARCTER;
		mail_haishin			= GS.EMPTY_CHARCTER;
		admin_flg				= GS.EMPTY_CHARCTER;
		workflow_h_nm			= GS.EMPTY_CHARCTER;
		workflow_list			= null;	
		printout_lang_kbn		= GS.EMPTY_CHARCTER;
		soushinList				= null;
		hyoujiSoushinList		= null;
		tantoSoshikiList		= null;
		ken_System_Kbn			= null;
		ken_Hanyou1				= null;
		ken_Hanyou2				= null;
	}
	
	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	
	//明細ID
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	//ユーザID
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	//氏名
	public String getUser_nm() {
		return user_nm;
	}
	public void setUser_nm(String user_nm) {
		this.user_nm = user_nm;
	}
	//会社名	
	public String getKaisya_nm() {
		return kaisya_nm;
	}
	public void setKaisya_nm(String kaisya_nm) {
		this.kaisya_nm = kaisya_nm;
	}
	//所属部署名
	public String getSyozoku_busyo_nm() {
		return syozoku_busyo_nm;
	}
	public void setSyozoku_busyo_nm(String syozoku_busyo_nm) {
		this.syozoku_busyo_nm = syozoku_busyo_nm;
	}
	//メールアドレス
	public String getMail_address() {
		return mail_address;
	}
	public void setMail_address(String mail_address) {
		this.mail_address = mail_address;
	}
	//帳票出力言語
	public String getTyohyo_syuturyoku_lang() {
		return tyohyo_syuturyoku_lang;
	}
	public void setTyohyo_syuturyoku_lang(String tyohyo_syuturyoku_lang) {
		this.tyohyo_syuturyoku_lang = tyohyo_syuturyoku_lang;
	}
	//メール配信
	public String getMail_haishin() {
		return mail_haishin;
	}
	public void setMail_haishin(String mail_haishin) {
		this.mail_haishin = mail_haishin;
	}
	//システム管理者フラグ	
	public String getAdmin_flg() {
		return 	admin_flg;
	}
	public void setAdmin_flg(String admin_flg) {
		this.admin_flg = admin_flg;
	}
	//表示用業務フローパターン名称	
	public String getWorkflow_h_nm() {
		return workflow_h_nm;
	}
	public void setWorkflow_h_nm(String workflow_h_nm) {
		this.workflow_h_nm = workflow_h_nm;
	}
	//業務フローパターン【リスト】	
	public List getWorkflow_list() {
		return workflow_list;
	}
	public void setWorkflow_list(List workflow_list) {
		this.workflow_list = workflow_list;
	}
	//業務フローパターン【リスト】	
	public String getPrintout_lang_kbn() {
		return printout_lang_kbn;
	}
	public void setpPrintout_lang_kbn(String printout_lang_kbn) {
		this.printout_lang_kbn = printout_lang_kbn;
	}

	//送信先選択【リスト】
	public List<HashMap<String, String>> getSoushinList() {
		return soushinList;
	}

	//送信先選択【リスト】
	public void setSoushinList(List<HashMap<String, String>> soushinList) {
		this.soushinList = soushinList;
	}

	//表示用送信先選択【リスト】
	public List<HashMap<String, String>> getHyoujiSoushinList() {
		return hyoujiSoushinList;
	}

	//表示用送信先選択【リスト】
	public void setHyoujiSoushinList(List<HashMap<String, String>> hyoujiSoushinList) {
		this.hyoujiSoushinList = hyoujiSoushinList;
	}

	//担当組織【リスト】
	public List<TantouBean> getTantoSoshikiList() {
		return tantoSoshikiList;
	}

	//担当組織【リスト】
	public void setTantoSoshikiList(List<TantouBean> tantoSoshikiList) {
		this.tantoSoshikiList = tantoSoshikiList;
	}

	//システム区分（担当組織選択検索条件）
	public String getKen_System_Kbn() {
		return ken_System_Kbn;
	}

	//システム区分（担当組織選択検索条件）
	public void setKen_System_Kbn(String ken_System_Kbn) {
		this.ken_System_Kbn = ken_System_Kbn;
	}

	//汎用1（担当組織選択検索条件）
	public String getKen_Hanyou1() {
		return ken_Hanyou1;
	}

	//汎用1（担当組織選択検索条件）
	public void setKen_Hanyou1(String ken_Hanyou1) {
		this.ken_Hanyou1 = ken_Hanyou1;
	}

	//汎用2（担当組織選択検索条件）
	public String getKen_Hanyou2() {
		return ken_Hanyou2;
	}

	//汎用2（担当組織選択検索条件）
	public void setKen_Hanyou2(String ken_Hanyou2) {
		this.ken_Hanyou2 = ken_Hanyou2;
	}
}