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
 *  OS7105　業務フローパターンメンテナンス_登録 アクションフォームクラス<br>
 */
public class WorkFlowTorokuForm extends AppPagerActionForm {
	private static final long serialVersionUID = 1L; 		// serialVersionUID
	private String gamenFlg;									// 画面フラグ
	private String systemkbn;									// システム区分
	private String systemkbn_nm;								// システム区分名
	private LinkedHashMap ar_systemkbn;						// システム区分【リスト】
	private String hanyou1;									// 汎用１
	private LinkedHashMap ar_hanyou1;							// 汎用１【リスト】
	private String workflow_nm_ja;								// 業務フローパターン名称-ja
	private String workflow_nm_en;								// 業務フローパターン名称-en
	private boolean bl_tairyu_touroku;						// 滞留判定　登録(チェックボックス)
	private String tairyu_touroku;								// 滞留判定　登録
	private LinkedHashMap ar_tairyu_touroku;					// 滞留判定　登録(セレクトボックス)
	private boolean bl_tairyu_shounin;						// 滞留判定　承認(チェックボックス)
	private String tairyu_shounin;								// 滞留判定　承認
	private LinkedHashMap ar_tairyu_shounin;					// 滞留判定　承認(セレクトボックス)
	private boolean bl_tairyu_kenshou_touroku;				// 滞留判定検証　登録(チェックボックス)
	private String tairyu_kenshou_touroku;						// 滞留判定検証　登録
	private LinkedHashMap ar_tairyu_kenshou_touroku;			// 滞留判定検証　登録(セレクトボックス)
	private boolean bl_tairyu_kenshou_shounin;				// 滞留判定検証　承認(チェックボックス)
	private String tairyu_kenshou_shounin;						// 滞留判定検証　承認
	private LinkedHashMap ar_tairyu_kenshou_shounin;			// 滞留判定検証　承認(セレクトボックス)
	private boolean bl_taishou_touroku;						// 対象先選定　登録(チェックボックス)
	private String taishou_touroku;							// 対象先選定　登録
	private LinkedHashMap ar_taishou_touroku;					// 対象先選定　登録(セレクトボックス)
	private boolean bl_taishou_shounin;						// 対象先選定　承認(チェックボックス)
	private String taishou_shounin;							// 対象先選定　承認
	private LinkedHashMap ar_taishou_shounin;					// 対象先選定　承認(セレクトボックス)
	private boolean bl_tairyu_sa;								// 滞留判定　差戻
	private boolean bl_iti_touroku;							// 一次査定　登録(チェックボックス)
	private String iti_touroku;								// 一次査定　登録
	private LinkedHashMap ar_iti_touroku;						// 一次査定　登録(セレクトボックス)
	private boolean bl_iti_shounin;							// 一次査定　承認(チェックボックス)
	private String iti_shounin;								// 一次査定　承認
	private LinkedHashMap ar_iti_shounin;						// 一次査定　承認(セレクトボックス)
	private boolean bl_iti_kenshou_touroku;					// 一次査定検証　登録(チェックボックス)
	private String iti_kenshou_touroku;						// 一次査定検証　登録
	private LinkedHashMap ar_iti_kenshou_touroku;				// 一次査定検証　登録(セレクトボックス)
	private boolean bl_iti_kenshou_shounin;					// 一次査定検証　承認(チェックボックス)
	private String iti_kenshou_shounin;						// 一次査定検証　承認
	private LinkedHashMap ar_iti_kenshou_shounin;				// 一次査定検証　承認(セレクトボックス)
	private boolean bl_ni_touroku;							// 二次査定　登録(チェックボックス)
	private String ni_touroku;									// 二次査定　登録
	private LinkedHashMap ar_ni_touroku;						// 二次査定　登録(セレクトボックス)
	private boolean bl_ni_shounin;							// 二次査定　承認(チェックボックス)
	private String ni_shounin;									// 二次査定　承認
	private LinkedHashMap ar_ni_shounin;						// 二次査定　承認(セレクトボックス)
	private boolean bl_satei;									// 査定完了　差戻
	private String ni_satei;									// 二次査定区分
	private LinkedHashMap ar_ni_satei;							// 二次査定区分(セレクトボックス)
	private boolean ni_satei_flg;								// 二次査定区分使用可能フラグ	
	private boolean bl_hikiate_kenshou_touroku;				// 引当金検証　登録(チェックボックス)
	private String hikiate_kenshou_touroku;					// 引当金検証　登録
	private LinkedHashMap ar_hikiate_kenshou_touroku;			// 引当金検証　登録(セレクトボックス)
	private boolean bl_hikiate_kenshou_shounin;				// 引当金検証　承認(チェックボックス)
	private boolean bl_hikiate_kakunin_touroku;				// 引当金確認　登録(チェックボックス)
	private String hikiate_kakunin_touroku;					// 引当金確認　登録
	private LinkedHashMap ar_hikiate_kakunin_touroku;			// 引当金確認　登録(セレクトボックス)
	private boolean bl_hikiate_kakunin_shounin;				// 引当金確認  承認(チェックボックス)
	private boolean bl_kure_touroku;							// クレーム債権　登録(チェックボックス)
	private String kure_touroku;								// クレーム債権　登録
	private LinkedHashMap ar_kure_touroku;						// クレーム債権　登録(セレクトボックス)
	private boolean bl_kure_shounin;							// クレーム債権　承認(チェックボックス)
	private String kure_shounin;								// クレーム債権　承認
	private LinkedHashMap ar_kure_shounin;						// クレーム債権　承認(セレクトボックス)
	private boolean daikou_settei;							// 代行設定
	private boolean settei_mentenansu;						// 査定会社メンテナンス
	private boolean gyoumu_mentenansu;						// 業務フローパターンメンテナンス
	private boolean user_mentenansu;							// ユーザマスタメンテナンス
	private boolean kanjou_mentenansu;						// 勘定科目マスタメンテナンス
	private boolean honsha_mentenansu;						// 抽出条件メンテナンス（本社）
	private boolean jouken_mentenansu;						// 抽出条件メンテナンス
	private boolean chanpion_mentenansu;						// チャンピオン部メンテナンス
	private boolean kaiin_mentenansu;							// ゴルフ会員権メンテナンス
	private boolean renketu_upload;							// 連結区分マスタUPLOAD
	private boolean system_manager;							// システム管理者専用	
    private String p_name_jp;				// 業務フローパターン名称(日本語)(退避用)	
    private String p_name_en;				// 業務フローパターン名称(英語)(退避用)
	
	//	 変数初期化
    public WorkFlowTorokuForm() {
    	super.gamenId = GS.OS7105;
    	this.gamenFlg = GS.EMPTY_CHARCTER;
        this.systemkbn = GS.EMPTY_CHARCTER;
        this.systemkbn_nm = GS.EMPTY_CHARCTER;
        this.ar_systemkbn = null;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.ar_hanyou1 = null;
        this.workflow_nm_ja = GS.EMPTY_CHARCTER;
        this.workflow_nm_en = GS.EMPTY_CHARCTER;
        this.bl_tairyu_touroku = false;
    	this.tairyu_touroku = GS.EMPTY_CHARCTER;
    	this.ar_tairyu_touroku = null;
    	this.bl_tairyu_shounin = false;
    	this.tairyu_shounin = GS.EMPTY_CHARCTER;
    	this.ar_tairyu_shounin = null;
    	this.bl_tairyu_kenshou_touroku = false;
    	this.tairyu_kenshou_touroku = GS.EMPTY_CHARCTER;
    	this.ar_tairyu_kenshou_touroku = null;
    	this.bl_tairyu_kenshou_shounin = false;
    	this.tairyu_kenshou_shounin = GS.EMPTY_CHARCTER;
    	this.ar_tairyu_kenshou_shounin = null;
    	this.bl_taishou_touroku = false;
    	this.taishou_touroku = GS.EMPTY_CHARCTER;
    	this.ar_taishou_touroku = null;
    	this.bl_taishou_shounin = false;
    	this.taishou_shounin = GS.EMPTY_CHARCTER;
    	this.ar_taishou_shounin = null;
    	this.bl_tairyu_sa = false;
    	this.bl_iti_touroku = false;
    	this.iti_touroku = GS.EMPTY_CHARCTER;
    	this.ar_iti_touroku = null;
    	this.bl_iti_shounin = false;
    	this.iti_shounin = GS.EMPTY_CHARCTER;
    	this.ar_iti_shounin = null;
    	this.bl_iti_kenshou_touroku = false;
    	this.iti_kenshou_touroku = GS.EMPTY_CHARCTER;
    	this.ar_iti_kenshou_touroku = null;
    	this.bl_iti_kenshou_shounin = false;
    	this.iti_kenshou_shounin = GS.EMPTY_CHARCTER;
    	this.ar_iti_kenshou_shounin = null;
    	this.bl_ni_touroku = false;
    	this.ni_touroku = GS.EMPTY_CHARCTER;
    	this.ar_ni_touroku = null;
    	this.bl_ni_shounin = false;
    	this.ni_shounin = GS.EMPTY_CHARCTER;
    	this.ar_ni_shounin = null;
    	this.bl_satei = false;
    	this.ni_satei = GS.EMPTY_CHARCTER;
    	this.ar_ni_satei = null;
    	this.ni_satei_flg = false;
    	this.bl_hikiate_kenshou_touroku = false;
    	this.hikiate_kenshou_touroku = GS.EMPTY_CHARCTER;
    	this.ar_hikiate_kenshou_touroku = null;
    	this.bl_hikiate_kenshou_touroku = false;
    	this.bl_hikiate_kakunin_shounin = false;
    	this.bl_kure_touroku = false;
    	this.kure_touroku = GS.EMPTY_CHARCTER;
    	this.ar_kure_touroku = null;
    	this.bl_kure_shounin = false;
    	this.kure_shounin = GS.EMPTY_CHARCTER;
    	this.ar_kure_shounin = null;
    	this.daikou_settei = false;
    	this.settei_mentenansu = false;
    	this.gyoumu_mentenansu = false;
    	this.user_mentenansu = false;
    	this.kanjou_mentenansu = false;
    	this.honsha_mentenansu = false;
    	this.jouken_mentenansu = false;
    	this.chanpion_mentenansu = false;
    	this.kaiin_mentenansu = false;
    	this.renketu_upload = false;
    	this.system_manager = false;
        this.setPager(new ArrayList());
        this.setAr_meisai(new ArrayList());
        this.p_name_jp = GS.EMPTY_CHARCTER;
        this.p_name_en = GS.EMPTY_CHARCTER;
    }
	
	/**
	 * @return 画面IDを戻します。
	 */
	public String toString() {
		return super.gamenId;
	}
	
	// システム区分
	public String getSystemkbn() {
		return systemkbn;
	}
	public void setSystemkbn(String systemkbn) {
		this.systemkbn = systemkbn;
		
	}
	
	// システム区分【リスト】
	public LinkedHashMap getAr_systemkbn() {
		return ar_systemkbn;
	}
	public void setAr_systemkbn(LinkedHashMap ar_systemkbn) {
		this.ar_systemkbn = ar_systemkbn;
	}
	
	// 汎用１【リスト】
	public LinkedHashMap getAr_hanyou1() {
		return ar_hanyou1;
	}
	public void setAr_hanyou1(LinkedHashMap ar_hanyou1) {
		this.ar_hanyou1 = ar_hanyou1;
	}

	// 汎用１
	public String getHanyou1() {
		return hanyou1;
	}
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	
	// 業務フローパターン名称
	public String getWorkflow_nm_ja() {
		return workflow_nm_ja;
	}
	public void setWorkflow_nm_ja(String workflow_nm_ja) {
		this.workflow_nm_ja = workflow_nm_ja;
	}

	public String getWorkflow_nm_en() {
		return workflow_nm_en;
	}
	public void setWorkflow_nm_en(String workflow_nm_en) {
		this.workflow_nm_en = workflow_nm_en;
	}
	
	public LinkedHashMap getAr_hikiate_kenshou_touroku() {
		return ar_hikiate_kenshou_touroku;
	}

	public void setAr_hikiate_kenshou_touroku(LinkedHashMap ar_hikiate_kenshou_touroku) {
		this.ar_hikiate_kenshou_touroku = ar_hikiate_kenshou_touroku;
	}

	public LinkedHashMap getAr_iti_kenshou_shounin() {
		return ar_iti_kenshou_shounin;
	}

	public void setAr_iti_kenshou_shounin(LinkedHashMap ar_iti_kenshou_shounin) {
		this.ar_iti_kenshou_shounin = ar_iti_kenshou_shounin;
	}

	public LinkedHashMap getAr_iti_kenshou_touroku() {
		return ar_iti_kenshou_touroku;
	}

	public void setAr_iti_kenshou_touroku(LinkedHashMap ar_iti_kenshou_touroku) {
		this.ar_iti_kenshou_touroku = ar_iti_kenshou_touroku;
	}

	public LinkedHashMap getAr_iti_shounin() {
		return ar_iti_shounin;
	}

	public void setAr_iti_shounin(LinkedHashMap ar_iti_shounin) {
		this.ar_iti_shounin = ar_iti_shounin;
	}

	public LinkedHashMap getAr_iti_touroku() {
		return ar_iti_touroku;
	}

	public void setAr_iti_touroku(LinkedHashMap ar_iti_touroku) {
		this.ar_iti_touroku = ar_iti_touroku;
	}

	public LinkedHashMap getAr_kure_shounin() {
		return ar_kure_shounin;
	}

	public void setAr_kure_shounin(LinkedHashMap ar_kure_shounin) {
		this.ar_kure_shounin = ar_kure_shounin;
	}

	public LinkedHashMap getAr_kure_touroku() {
		return ar_kure_touroku;
	}

	public void setAr_kure_touroku(LinkedHashMap ar_kure_touroku) {
		this.ar_kure_touroku = ar_kure_touroku;
	}

	public LinkedHashMap getAr_ni_satei() {
		return ar_ni_satei;
	}

	public void setAr_ni_satei(LinkedHashMap ar_ni_satei) {
		this.ar_ni_satei = ar_ni_satei;
	}

	public LinkedHashMap getAr_ni_shounin() {
		return ar_ni_shounin;
	}

	public void setAr_ni_shounin(LinkedHashMap ar_ni_shounin) {
		this.ar_ni_shounin = ar_ni_shounin;
	}

	public LinkedHashMap getAr_ni_touroku() {
		return ar_ni_touroku;
	}

	public void setAr_ni_touroku(LinkedHashMap ar_ni_touroku) {
		this.ar_ni_touroku = ar_ni_touroku;
	}

	public LinkedHashMap getAr_tairyu_kenshou_shounin() {
		return ar_tairyu_kenshou_shounin;
	}

	public void setAr_tairyu_kenshou_shounin(LinkedHashMap ar_tairyu_kenshou_shounin) {
		this.ar_tairyu_kenshou_shounin = ar_tairyu_kenshou_shounin;
	}

	public LinkedHashMap getAr_tairyu_kenshou_touroku() {
		return ar_tairyu_kenshou_touroku;
	}

	public void setAr_tairyu_kenshou_touroku(LinkedHashMap ar_tairyu_kenshou_touroku) {
		this.ar_tairyu_kenshou_touroku = ar_tairyu_kenshou_touroku;
	}

	public LinkedHashMap getAr_tairyu_shounin() {
		return ar_tairyu_shounin;
	}

	public void setAr_tairyu_shounin(LinkedHashMap ar_tairyu_shounin) {
		this.ar_tairyu_shounin = ar_tairyu_shounin;
	}

	public LinkedHashMap getAr_tairyu_touroku() {
		return ar_tairyu_touroku;
	}

	public void setAr_tairyu_touroku(LinkedHashMap ar_tairyu_touroku) {
		this.ar_tairyu_touroku = ar_tairyu_touroku;
	}

	public LinkedHashMap getAr_taishou_shounin() {
		return ar_taishou_shounin;
	}

	public void setAr_taishou_shounin(LinkedHashMap ar_taishou_shounin) {
		this.ar_taishou_shounin = ar_taishou_shounin;
	}

	public LinkedHashMap getAr_taishou_touroku() {
		return ar_taishou_touroku;
	}

	public void setAr_taishou_touroku(LinkedHashMap ar_taishou_touroku) {
		this.ar_taishou_touroku = ar_taishou_touroku;
	}

	public boolean isBl_hikiate_kakunin_shounin() {
		return bl_hikiate_kakunin_shounin;
	}

	public void setBl_hikiate_kakunin_shounin(boolean bl_hikiate_kakunin_shounin) {
		this.bl_hikiate_kakunin_shounin = bl_hikiate_kakunin_shounin;
	}

	public boolean isBl_hikiate_kenshou_shounin() {
		return bl_hikiate_kenshou_shounin;
	}

	public void setBl_hikiate_kenshou_shounin(boolean bl_hikiate_kenshou_shounin) {
		this.bl_hikiate_kenshou_shounin = bl_hikiate_kenshou_shounin;
	}

	public boolean isBl_hikiate_kenshou_touroku() {
		return bl_hikiate_kenshou_touroku;
	}

	public void setBl_hikiate_kenshou_touroku(boolean bl_hikiate_kenshou_touroku) {
		this.bl_hikiate_kenshou_touroku = bl_hikiate_kenshou_touroku;
	}

	public boolean isBl_iti_kenshou_shounin() {
		return bl_iti_kenshou_shounin;
	}

	public void setBl_iti_kenshou_shounin(boolean bl_iti_kenshou_shounin) {
		this.bl_iti_kenshou_shounin = bl_iti_kenshou_shounin;
	}

	public boolean isBl_iti_kenshou_touroku() {
		return bl_iti_kenshou_touroku;
	}

	public void setBl_iti_kenshou_touroku(boolean bl_iti_kenshou_touroku) {
		this.bl_iti_kenshou_touroku = bl_iti_kenshou_touroku;
	}

	public boolean isBl_iti_shounin() {
		return bl_iti_shounin;
	}

	public void setBl_iti_shounin(boolean bl_iti_shounin) {
		this.bl_iti_shounin = bl_iti_shounin;
	}

	public boolean isBl_iti_touroku() {
		return bl_iti_touroku;
	}

	public void setBl_iti_touroku(boolean bl_iti_touroku) {
		this.bl_iti_touroku = bl_iti_touroku;
	}

	public boolean isBl_kure_shounin() {
		return bl_kure_shounin;
	}

	public void setBl_kure_shounin(boolean bl_kure_shounin) {
		this.bl_kure_shounin = bl_kure_shounin;
	}

	public boolean isBl_kure_touroku() {
		return bl_kure_touroku;
	}

	public void setBl_kure_touroku(boolean bl_kure_touroku) {
		this.bl_kure_touroku = bl_kure_touroku;
	}

	public boolean isBl_ni_shounin() {
		return bl_ni_shounin;
	}

	public void setBl_ni_shounin(boolean bl_ni_shounin) {
		this.bl_ni_shounin = bl_ni_shounin;
	}

	public boolean isBl_ni_touroku() {
		return bl_ni_touroku;
	}

	public void setBl_ni_touroku(boolean bl_ni_touroku) {
		this.bl_ni_touroku = bl_ni_touroku;
	}

	public boolean isBl_satei() {
		return bl_satei;
	}

	public void setBl_satei(boolean bl_satei) {
		this.bl_satei = bl_satei;
	}

	public boolean isBl_tairyu_kenshou_shounin() {
		return bl_tairyu_kenshou_shounin;
	}

	public void setBl_tairyu_kenshou_shounin(boolean bl_tairyu_kenshou_shounin) {
		this.bl_tairyu_kenshou_shounin = bl_tairyu_kenshou_shounin;
	}

	public boolean isBl_tairyu_kenshou_touroku() {
		return bl_tairyu_kenshou_touroku;
	}

	public void setBl_tairyu_kenshou_touroku(boolean bl_tairyu_kenshou_touroku) {
		this.bl_tairyu_kenshou_touroku = bl_tairyu_kenshou_touroku;
	}

	public boolean isBl_tairyu_sa() {
		return bl_tairyu_sa;
	}

	public void setBl_tairyu_sa(boolean bl_tairyu_sa) {
		this.bl_tairyu_sa = bl_tairyu_sa;
	}

	public boolean isBl_tairyu_shounin() {
		return bl_tairyu_shounin;
	}

	public void setBl_tairyu_shounin(boolean bl_tairyu_shounin) {
		this.bl_tairyu_shounin = bl_tairyu_shounin;
	}

	public boolean isBl_tairyu_touroku() {
		return bl_tairyu_touroku;
	}

	public void setBl_tairyu_touroku(boolean bl_tairyu_touroku) {
		this.bl_tairyu_touroku = bl_tairyu_touroku;
	}

	public boolean isBl_taishou_shounin() {
		return bl_taishou_shounin;
	}

	public void setBl_taishou_shounin(boolean bl_taishou_shounin) {
		this.bl_taishou_shounin = bl_taishou_shounin;
	}

	public boolean isBl_taishou_touroku() {
		return bl_taishou_touroku;
	}

	public void setBl_taishou_touroku(boolean bl_taishou_touroku) {
		this.bl_taishou_touroku = bl_taishou_touroku;
	}

	public boolean isChanpion_mentenansu() {
		return chanpion_mentenansu;
	}

	public void setChanpion_mentenansu(boolean chanpion_mentenansu) {
		this.chanpion_mentenansu = chanpion_mentenansu;
	}

	public boolean isDaikou_settei() {
		return daikou_settei;
	}

	public void setDaikou_settei(boolean daikou_settei) {
		this.daikou_settei = daikou_settei;
	}

	public boolean isGyoumu_mentenansu() {
		return gyoumu_mentenansu;
	}

	public void setGyoumu_mentenansu(boolean gyoumu_mentenansu) {
		this.gyoumu_mentenansu = gyoumu_mentenansu;
	}


	public String getHikiate_kenshou_touroku() {
		return hikiate_kenshou_touroku;
	}

	public void setHikiate_kenshou_touroku(String hikiate_kenshou_touroku) {
		this.hikiate_kenshou_touroku = hikiate_kenshou_touroku;
	}

	public boolean isHonsha_mentenansu() {
		return honsha_mentenansu;
	}

	public void setHonsha_mentenansu(boolean honsha_mentenansu) {
		this.honsha_mentenansu = honsha_mentenansu;
	}

	public String getIti_kenshou_shounin() {
		return iti_kenshou_shounin;
	}

	public void setIti_kenshou_shounin(String iti_kenshou_shounin) {
		this.iti_kenshou_shounin = iti_kenshou_shounin;
	}

	public String getIti_kenshou_touroku() {
		return iti_kenshou_touroku;
	}

	public void setIti_kenshou_touroku(String iti_kenshou_touroku) {
		this.iti_kenshou_touroku = iti_kenshou_touroku;
	}

	public String getIti_shounin() {
		return iti_shounin;
	}

	public void setIti_shounin(String iti_shounin) {
		this.iti_shounin = iti_shounin;
	}

	public String getIti_touroku() {
		return iti_touroku;
	}

	public void setIti_touroku(String iti_touroku) {
		this.iti_touroku = iti_touroku;
	}

	public boolean isJouken_mentenansu() {
		return jouken_mentenansu;
	}

	public void setJouken_mentenansu(boolean jouken_mentenansu) {
		this.jouken_mentenansu = jouken_mentenansu;
	}

	public boolean isKaiin_mentenansu() {
		return kaiin_mentenansu;
	}

	public void setKaiin_mentenansu(boolean kaiin_mentenansu) {
		this.kaiin_mentenansu = kaiin_mentenansu;
	}

	public boolean isKanjou_mentenansu() {
		return kanjou_mentenansu;
	}

	public void setKanjou_mentenansu(boolean kanjou_mentenansu) {
		this.kanjou_mentenansu = kanjou_mentenansu;
	}

	public String getKure_shounin() {
		return kure_shounin;
	}

	public void setKure_shounin(String kure_shounin) {
		this.kure_shounin = kure_shounin;
	}

	public String getKure_touroku() {
		return kure_touroku;
	}

	public void setKure_touroku(String kure_touroku) {
		this.kure_touroku = kure_touroku;
	}

	public String getNi_satei() {
		return ni_satei;
	}

	public void setNi_satei(String ni_satei) {
		this.ni_satei = ni_satei;
	}

	public String getNi_shounin() {
		return ni_shounin;
	}

	public void setNi_shounin(String ni_shounin) {
		this.ni_shounin = ni_shounin;
	}

	public String getNi_touroku() {
		return ni_touroku;
	}

	public void setNi_touroku(String ni_touroku) {
		this.ni_touroku = ni_touroku;
	}

	public boolean isRenketu_upload() {
		return renketu_upload;
	}

	public void setRenketu_upload(boolean renketu_upload) {
		this.renketu_upload = renketu_upload;
	}

	public boolean isSettei_mentenansu() {
		return settei_mentenansu;
	}

	public void setSettei_mentenansu(boolean settei_mentenansu) {
		this.settei_mentenansu = settei_mentenansu;
	}

	public boolean isSystem_manager() {
		return system_manager;
	}

	public void setSystem_manager(boolean system_manager) {
		this.system_manager = system_manager;
	}

	public String getTairyu_kenshou_shounin() {
		return tairyu_kenshou_shounin;
	}

	public void setTairyu_kenshou_shounin(String tairyu_kenshou_shounin) {
		this.tairyu_kenshou_shounin = tairyu_kenshou_shounin;
	}

	public String getTairyu_kenshou_touroku() {
		return tairyu_kenshou_touroku;
	}

	public void setTairyu_kenshou_touroku(String tairyu_kenshou_touroku) {
		this.tairyu_kenshou_touroku = tairyu_kenshou_touroku;
	}

	public String getTairyu_shounin() {
		return tairyu_shounin;
	}

	public void setTairyu_shounin(String tairyu_shounin) {
		this.tairyu_shounin = tairyu_shounin;
	}

	public String getTairyu_touroku() {
		return tairyu_touroku;
	}

	public void setTairyu_touroku(String tairyu_touroku) {
		this.tairyu_touroku = tairyu_touroku;
	}

	public String getTaishou_shounin() {
		return taishou_shounin;
	}

	public void setTaishou_shounin(String taishou_shounin) {
		this.taishou_shounin = taishou_shounin;
	}

	public String getTaishou_touroku() {
		return taishou_touroku;
	}

	public void setTaishou_touroku(String taishou_touroku) {
		this.taishou_touroku = taishou_touroku;
	}

	public boolean isUser_mentenansu() {
		return user_mentenansu;
	}

	public void setUser_mentenansu(boolean user_mentenansu) {
		this.user_mentenansu = user_mentenansu;
	}

	public LinkedHashMap getAr_hikiate_kakunin_touroku() {
		return ar_hikiate_kakunin_touroku;
	}

	public void setAr_hikiate_kakunin_touroku(LinkedHashMap ar_hikiate_kakunin_touroku) {
		this.ar_hikiate_kakunin_touroku = ar_hikiate_kakunin_touroku;
	}

	public boolean isBl_hikiate_kakunin_touroku() {
		return bl_hikiate_kakunin_touroku;
	}

	public void setBl_hikiate_kakunin_touroku(boolean bl_hikiate_kakunin_touroku) {
		this.bl_hikiate_kakunin_touroku = bl_hikiate_kakunin_touroku;
	}

	public String getHikiate_kakunin_touroku() {
		return hikiate_kakunin_touroku;
	}

	public void setHikiate_kakunin_touroku(String hikiate_kakunin_touroku) {
		this.hikiate_kakunin_touroku = hikiate_kakunin_touroku;
	}

	public boolean isNi_satei_flg() {
		return ni_satei_flg;
	}

	public void setNi_satei_flg(boolean ni_satei_flg) {
		this.ni_satei_flg = ni_satei_flg;
	}

	public String getGamenFlg() {
		return gamenFlg;
	}

	public void setGamenFlg(String gamenFlg) {
		this.gamenFlg = gamenFlg;
	}

	public String getSystemkbn_nm() {
		return systemkbn_nm;
	}

	public void setSystemkbn_nm(String systemkbn_nm) {
		this.systemkbn_nm = systemkbn_nm;
	}

	public String getP_name_jp() {
		return p_name_jp;
	}

	public void setP_name_jp(String p_name_jp) {
		this.p_name_jp = p_name_jp;
	}

	public String getP_name_en() {
		return p_name_en;
	}

	public void setP_name_en(String p_name_en) {
		this.p_name_en = p_name_en;
	}
	
	
}
