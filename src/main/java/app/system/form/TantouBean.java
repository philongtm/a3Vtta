/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/03/14		SSC				BJ201602002 部門廃止対応（一次）
******************************************************************************/
package app.system.form;


import common.global.GS;

/**
 * 担当情報【リスト】
 * 
 */
public class TantouBean {

	/** (汎用１)コード */
	private String hanyou1_cd				= null;	
	
	/** (汎用１)名称 */
    private String hanyou1_nm				= null;
    
	/** (汎用２)コード */
    private String hanyou2_cd				= null;	
    
	/** (汎用２)名称 */
    private String hanyou2_nm				= null;	
    
	/** (汎用４)コード */
    private String hanyou4_cd				= null;	
    
	/** (汎用４)名称 */
    private String hanyou4_nm				= null;	
    
	/** 基幹システム区分 */    
	private String system_kbn				= null;	
    
	/** 処理対象外フラグ 0：処理対象、1：処理対象外 */
    private String taisyogaiFlg				= null;	
    
	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		hanyou1_cd				= GS.EMPTY_CHARCTER;
		hanyou1_nm				= GS.EMPTY_CHARCTER;
		hanyou2_cd				= GS.EMPTY_CHARCTER;
		hanyou2_nm				= GS.EMPTY_CHARCTER;
		hanyou4_cd				= GS.EMPTY_CHARCTER;
		hanyou4_nm				= GS.EMPTY_CHARCTER;
		system_kbn	    	   	= GS.EMPTY_CHARCTER;
	}
	
	/**
	 * オブジェクトの破棄を行う。
	 */
	public void destroy() {
		initialize();
	}

	// アクセスメソッド
	/**
	 * @return the hanyou1_cd
	 */
	public String getHanyou1_cd() {
		return hanyou1_cd;
	}

	/**
	 * @param hanyou1_cd the hanyou1_cd to set
	 */
	public void setHanyou1_cd(String hanyou1_cd) {
		this.hanyou1_cd = hanyou1_cd;
	}

	/**
	 * @return the hanyou1_nm
	 */
	public String getHanyou1_nm() {
		return hanyou1_nm;
	}

	/**
	 * @param hanyou1_nm the hanyou1_nm to set
	 */
	public void setHanyou1_nm(String hanyou1_nm) {
		this.hanyou1_nm = hanyou1_nm;
	}

	/**
	 * @return the hanyou2_cd
	 */
	public String getHanyou2_cd() {
		return hanyou2_cd;
	}

	/**
	 * @param hanyou2_cd the hanyou2_cd to set
	 */
	public void setHanyou2_cd(String hanyou2_cd) {
		this.hanyou2_cd = hanyou2_cd;
	}

	/**
	 * @return the hanyou2_nm
	 */
	public String getHanyou2_nm() {
		return hanyou2_nm;
	}

	/**
	 * @param hanyou2_nm the hanyou2_nm to set
	 */
	public void setHanyou2_nm(String hanyou2_nm) {
		this.hanyou2_nm = hanyou2_nm;
	}

	/**
	 * (汎用４)コード
	 * @return the hanyou4_cd
	 */
	public String getHanyou4_cd() {
		return hanyou4_cd;
	}

	/**
	 * (汎用４)コード
	 * @param hanyou4_cd the hanyou4_cd to set
	 */
	public void setHanyou4_cd(String hanyou4_cd) {
		this.hanyou4_cd = hanyou4_cd;
	}

	/**
	 * (汎用４)名称
	 * @return the hanyou4_nm
	 */
	public String getHanyou4_nm() {
		return hanyou4_nm;
	}

	/**
	 * (汎用４)名称
	 * @param hanyou4_nm the hanyou4_nm to set
	 */
	public void setHanyou4_nm(String hanyou4_nm) {
		this.hanyou4_nm = hanyou4_nm;
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
	 * 対象外フラグ 処理対象：0、対象外：1
	 * @return taisyogaiFlg
	 */
	public String getTaisyogaiFlg() {
		return taisyogaiFlg;
	}

	/**
	 * 対象外フラグ 処理対象：0、対象外：1
	 * @param taisyogaiFlg the taisyogaiFlg to set
	 */
	public void setTaisyogaiFlg(String taisyogaiFlg) {
		this.taisyogaiFlg = taisyogaiFlg;
	}
}