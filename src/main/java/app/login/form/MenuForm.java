/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2016/02/26		SSC				BJ201602002_部門廃止対応（一次） 
******************************************************************************/
package app.login.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS2101_メインメニュー アクションフォームクラス
 * 
 */
public class MenuForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; 	// serialVersionUID
	
	// システム情報
	private String label1;					// ラベル名1
	private String label2;					// ラベル名2
	private String label3;					// ラベル名3
	private String label4;					// ラベル名4
	private String label5;					// ラベル名5
	private String label6;					// ラベル名6
	private String label7;					// ラベル名7
	private String label8;					// ラベル名8
	private String label9;					// ラベル名9
	private String label10;					// ラベル名10
	
	// 機能固有セッション
	private String pattern;				// 業務フローパターン
	private List ar_pattern;		 	    // 業務フローパターン【配列】
	private String pattern_id;				// 画面で選択された業務フローパターンのID
	private String pattern_system_kbn;		// 画面で選択された業務フローパターンのシステム区分
	private String pattern_sateikaisya_cd;	// 画面で選択された業務フローパターンの査定会社コード
	private String temp_pattern_id;				// tempID
	private String temp_pattern_system_kbn;		// tempシステム区分
	private String temp_pattern_sateikaisya_cd;	// temp査定会社コード
	private boolean pattern_flg;			// 業務フローパターンセレクトボックス表示制御用フラグ
	private String satei_ki;				// 査定期
	private LinkedHashMap ar_satei_ki;     // 査定期【配列】
	private String ym;						// 対象年月
	private LinkedHashMap ar_ym;   		// 対象年月【配列】
	private List ar_taisho_ym;   			// 対象年月【配列】
	private String daiko;					// 被代行者ID
	private LinkedHashMap ar_daiko;   		// 被代行者【配列】
	private String temp_daiko;				// temp被代行者ID
	private String first_ym;				// 初回年月
	private String middle_ym;				// 中間年月
	private String last_ym;				// 最終年月
	private String first_ym_hy;			// 初回年月表示用
	private String middle_ym_list;			// 中間年月表示用（リスト）
	private String last_ym_hy;				// 最終年月表示用
	private String sankou_systemkbn;		// 最終年月表示用
	private String sankou_satei_kaisha;	// 最終年月表示用
	private String sankou_bunrui2;			// 最終年月表示用	
	private List ar_status;				// ステータス情報【リスト】
	private List ar_satei_resualt;			// 査定結果情報【リスト】		
	private List ar_oparation;				// M17_業務フローパターンマスタから取得した各処理権限リスト
	private List ar_systemkbn;				// M02_ユーザ参照組織マスタから取得したログインユーザの全参照システム区分リスト
	private List ar_satei_kaisha;			// M02_ユーザ参照組織マスタから取得したログインユーザの全参照査定会社
	private List ar_bunrui2;				// M02_ユーザ参照組織マスタから取得したログインユーザの参照分類２
	
	// INパラメータ
	private String arg_system;				// 編集した業務フローパターンシステム区分
	private String arg_satei_kaisha;		// 編集した業務フローパターン査定会社コード
	private String arg_pattern_id;			// 編集した業務フローパターンID
	
	// ステータス情報【リスト】
	private String satei_kaisya_cd;		// 査定会社コード
	private String bunrui2;				// 分類２
	private String bunrui2_nm;				// 分類２名称
	private String bu_cd;					// 部コード
	private String bu_nm;					// 部名称
	private String soshiki;				// 組織
	private String jushin_bi;				// 受信日
	private String tairyuu_mi_shori;		// 実質滞留債権判定.未処理件数
	private String tairyuu_shori;			// 実質滞留債権判定.処理中件数
	private String tairyuu_zumi;			// 実質滞留債権判定.済み件数
	private String satei_mi_shori;			// 査定.未処理件数
	private String satei_ichi;				// 査定.一次査定中件数
	private String satei_ni;				// 査定.二次査定中件数
	private String satei_zumi;				// 査定.済み件数
	private String hanyou_mi_shori;		// 汎用３.未処理件数
	private String hanyou_zumi;			// 汎用３.済み件数

	// 査定結果情報【リスト】
	private String hasan_num_last;			// 貸倒懸念・破産更生債権判定先件数(最終月)
	private String hasan_num_middle;		// 貸倒懸念・破産更生債権判定先件数(中間月)
	private String hasan_num_first;		// 貸倒懸念・破産更生債権判定先件数(初回月)
	private String hasan_num_last_kakko;	// 貸倒懸念・破産更生債権判定先件数（）内(最終月)
	private String hasan_num_middle_kakko;	// 貸倒懸念・破産更生債権判定先件数（）内(中間月)
	private String hasan_num_first_kakko;	// 貸倒懸念・破産更生債権判定先件数（）内(初回月)
    
	/**
	 * コンストラクタ
	 */	
	public MenuForm() {
		super.gamenId = GS.OS2101;
		this.label1 = GS.EMPTY_CHARCTER;
		this.label2 = GS.EMPTY_CHARCTER;
		this.label3 = GS.EMPTY_CHARCTER;
		this.label4 = GS.EMPTY_CHARCTER;
		this.label5 = GS.EMPTY_CHARCTER;
		this.label6 = GS.EMPTY_CHARCTER;
		this.label7 = GS.EMPTY_CHARCTER;
		this.label8 = GS.EMPTY_CHARCTER;
		this.label9 = GS.EMPTY_CHARCTER;
		this.label10 = GS.EMPTY_CHARCTER;
		this.pattern = GS.EMPTY_CHARCTER;
		this.ar_pattern = null;
		this.pattern_id = GS.EMPTY_CHARCTER;
		this.pattern_system_kbn = GS.EMPTY_CHARCTER;
		this.pattern_sateikaisya_cd = GS.EMPTY_CHARCTER;
		this.temp_pattern_id = GS.EMPTY_CHARCTER;
		this.temp_pattern_system_kbn = GS.EMPTY_CHARCTER;
		this.temp_pattern_sateikaisya_cd = GS.EMPTY_CHARCTER;
		this.pattern_flg = false;
		this.satei_ki = GS.EMPTY_CHARCTER;
		this.ym = GS.EMPTY_CHARCTER;
		this.daiko = GS.EMPTY_CHARCTER;
		this.temp_daiko = GS.EMPTY_CHARCTER;
		this.first_ym = GS.EMPTY_CHARCTER;
		this.middle_ym = GS.EMPTY_CHARCTER;
		this.last_ym = GS.EMPTY_CHARCTER;
		this.first_ym_hy = GS.EMPTY_CHARCTER;
		this.middle_ym_list = GS.EMPTY_CHARCTER;
		this.last_ym_hy = GS.EMPTY_CHARCTER;
		this.satei_kaisya_cd = GS.EMPTY_CHARCTER;
		this.bunrui2 = GS.EMPTY_CHARCTER;
		this.bunrui2_nm = GS.EMPTY_CHARCTER;
		this.bu_cd = GS.EMPTY_CHARCTER;
		this.bu_nm = GS.EMPTY_CHARCTER;
		this.soshiki = GS.EMPTY_CHARCTER;
		this.jushin_bi = GS.EMPTY_CHARCTER;
		this.tairyuu_mi_shori = GS.EMPTY_CHARCTER;
		this.tairyuu_shori = GS.EMPTY_CHARCTER;
		this.tairyuu_zumi = GS.EMPTY_CHARCTER;
		this.satei_mi_shori = GS.EMPTY_CHARCTER;
		this.satei_ichi = GS.EMPTY_CHARCTER;
		this.satei_ni = GS.EMPTY_CHARCTER;
		this.satei_zumi = GS.EMPTY_CHARCTER;
		this.hanyou_mi_shori = GS.EMPTY_CHARCTER;
		this.hanyou_zumi = GS.EMPTY_CHARCTER;
		this.hasan_num_last = GS.EMPTY_CHARCTER;
		this.hasan_num_middle = GS.EMPTY_CHARCTER;
		this.hasan_num_first = GS.EMPTY_CHARCTER;
		this.hasan_num_last_kakko = GS.EMPTY_CHARCTER;
		this.hasan_num_middle_kakko = GS.EMPTY_CHARCTER;
		this.hasan_num_first_kakko = GS.EMPTY_CHARCTER;
		this.sankou_systemkbn = GS.EMPTY_CHARCTER;
		this.sankou_satei_kaisha = GS.EMPTY_CHARCTER;
		this.sankou_bunrui2 = GS.EMPTY_CHARCTER;
		this.arg_system = GS.EMPTY_CHARCTER;
		this.arg_satei_kaisha = GS.EMPTY_CHARCTER;
		this.arg_pattern_id = GS.EMPTY_CHARCTER;
		this.ar_status = null;
		this.ar_oparation = null;
		this.ar_satei_resualt = null;
		this.ar_systemkbn = null;
		this.ar_satei_kaisha = null;
		this.ar_taisho_ym = null;
		this.ar_bunrui2 = null;
	}
	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
	/**
	 * @return the ar_daiko
	 */
	public LinkedHashMap getAr_daiko() {
		return ar_daiko;
	}
	/**
	 * @param ar_daiko the ar_daiko to set
	 */
	public void setAr_daiko(LinkedHashMap ar_daiko) {
		this.ar_daiko = ar_daiko;
	}
	/**
	 * @return the ar_pattern
	 */
	public List getAr_pattern() {
		return ar_pattern;
	}
	/**
	 * @param ar_pattern the ar_pattern to set
	 */
	public void setAr_pattern(List ar_pattern) {
		this.ar_pattern = ar_pattern;
	}
	/**
	 * @return the pattern_flg
	 */
	public boolean getPattern_flg() {
		return pattern_flg;
	}
	/**
	 * @param pattern_flg the pattern_flg to set
	 */
	public void setPattern_flg(boolean pattern_flg) {
		this.pattern_flg = pattern_flg;
	}
	/**
	 * @return the ar_satei_ki
	 */
	public LinkedHashMap getAr_satei_ki() {
		return ar_satei_ki;
	}
	/**
	 * @param ar_satei_ki the ar_satei_ki to set
	 */
	public void setAr_satei_ki(LinkedHashMap ar_satei_ki) {
		this.ar_satei_ki = ar_satei_ki;
	}
	/**
	 * @return the ar_ym
	 */
	public LinkedHashMap getAr_ym() {
		return ar_ym;
	}
	/**
	 * @param ar_ym the ar_ym to set
	 */
	public void setAr_ym(LinkedHashMap ar_ym) {
		this.ar_ym = ar_ym;
	}
	/**
	 * @return the bu_cd
	 */
	public String getBu_cd() {
		return bu_cd;
	}
	/**
	 * @param bu_cd the bu_cd to set
	 */
	public void setBu_cd(String bu_cd) {
		this.bu_cd = bu_cd;
	}
	/**
	 * @return the bu_nm
	 */
	public String getBu_nm() {
		return bu_nm;
	}
	/**
	 * @param bu_nm the bu_nm to set
	 */
	public void setBu_nm(String bu_nm) {
		this.bu_nm = bu_nm;
	}
	/**
	 * @return the bunrui2
	 */
	public String getBunrui2() {
		return bunrui2;
	}
	/**
	 * @param bunrui2 the bunrui2 to set
	 */
	public void setBunrui2(String bunrui2) {
		this.bunrui2 = bunrui2;
	}
	/**
	 * @return the bunrui2_nm
	 */
	public String getBunrui2_nm() {
		return bunrui2_nm;
	}
	/**
	 * @param bunrui2_nm the bunrui2_nm to set
	 */
	public void setBunrui2_nm(String bunrui2_nm) {
		this.bunrui2_nm = bunrui2_nm;
	}
	/**
	 * @return the daiko
	 */
	public String getDaiko() {
		return daiko;
	}
	/**
	 * @param daiko the daiko to set
	 */
	public void setDaiko(String daiko) {
		this.daiko = daiko;
	}
	/**
	 * @return the temp_daiko
	 */
	public String getTemp_daiko() {
		return temp_daiko;
	}
	/**
	 * @param temp_daiko the temp_daiko to set
	 */
	public void setTemp_daiko(String temp_daiko) {
		this.temp_daiko = temp_daiko;
	}
	/**
	 * @return the first_ym
	 */
	public String getFirst_ym() {
		return first_ym;
	}
	/**
	 * @param first_ym the first_ym to set
	 */
	public void setFirst_ym(String first_ym) {
		this.first_ym = first_ym;
	}
	/**
	 * @return the first_ym_hy
	 */
	public String getFirst_ym_hy() {
		return first_ym_hy;
	}
	/**
	 * @param first_ym_hy the first_ym_hy to set
	 */
	public void setFirst_ym_hy(String first_ym_hy) {
		this.first_ym_hy = first_ym_hy;
	}
	/**
	 * @return the hanyou_mi_shori
	 */
	public String getHanyou_mi_shori() {
		return hanyou_mi_shori;
	}
	/**
	 * @param hanyou_mi_shori the hanyou_mi_shori to set
	 */
	public void setHanyou_mi_shori(String hanyou_mi_shori) {
		this.hanyou_mi_shori = hanyou_mi_shori;
	}
	/**
	 * @return the hanyou_zumi
	 */
	public String getHanyou_zumi() {
		return hanyou_zumi;
	}
	/**
	 * @param hanyou_zumi the hanyou_zumi to set
	 */
	public void setHanyou_zumi(String hanyou_zumi) {
		this.hanyou_zumi = hanyou_zumi;
	}
	/**
	 * @return the hasan_num_first
	 */
	public String getHasan_num_first() {
		return hasan_num_first;
	}
	/**
	 * @param hasan_num_first the hasan_num_first to set
	 */
	public void setHasan_num_first(String hasan_num_first) {
		this.hasan_num_first = hasan_num_first;
	}
	/**
	 * @return the hasan_num_first_kakko
	 */
	public String getHasan_num_first_kakko() {
		return hasan_num_first_kakko;
	}
	/**
	 * @param hasan_num_first_kakko the hasan_num_first_kakko to set
	 */
	public void setHasan_num_first_kakko(String hasan_num_first_kakko) {
		this.hasan_num_first_kakko = hasan_num_first_kakko;
	}
	/**
	 * @return the hasan_num_last
	 */
	public String getHasan_num_last() {
		return hasan_num_last;
	}
	/**
	 * @param hasan_num_last the hasan_num_last to set
	 */
	public void setHasan_num_last(String hasan_num_last) {
		this.hasan_num_last = hasan_num_last;
	}
	/**
	 * @return the hasan_num_last_kakko
	 */
	public String getHasan_num_last_kakko() {
		return hasan_num_last_kakko;
	}
	/**
	 * @param hasan_num_last_kakko the hasan_num_last_kakko to set
	 */
	public void setHasan_num_last_kakko(String hasan_num_last_kakko) {
		this.hasan_num_last_kakko = hasan_num_last_kakko;
	}
	/**
	 * @return the hasan_num_middle
	 */
	public String getHasan_num_middle() {
		return hasan_num_middle;
	}
	/**
	 * @param hasan_num_middle the hasan_num_middle to set
	 */
	public void setHasan_num_middle(String hasan_num_middle) {
		this.hasan_num_middle = hasan_num_middle;
	}
	/**
	 * @return the hasan_num_middle_kakko
	 */
	public String getHasan_num_middle_kakko() {
		return hasan_num_middle_kakko;
	}
	/**
	 * @param hasan_num_middle_kakko the hasan_num_middle_kakko to set
	 */
	public void setHasan_num_middle_kakko(String hasan_num_middle_kakko) {
		this.hasan_num_middle_kakko = hasan_num_middle_kakko;
	}
	/**
	 * @return the jushin_bi
	 */
	public String getJushin_bi() {
		return jushin_bi;
	}
	/**
	 * @param jushin_bi the jushin_bi to set
	 */
	public void setJushin_bi(String jushin_bi) {
		this.jushin_bi = jushin_bi;
	}
	/**
	 * @return the label1
	 */
	public String getLabel1() {
		return label1;
	}
	/**
	 * @param label1 the label1 to set
	 */
	public void setLabel1(String label1) {
		this.label1 = label1;
	}
	/**
	 * @return the label2
	 */
	public String getLabel2() {
		return label2;
	}
	/**
	 * @param label2 the label2 to set
	 */
	public void setLabel2(String label2) {
		this.label2 = label2;
	}
	/**
	 * @return the label3
	 */
	public String getLabel3() {
		return label3;
	}
	/**
	 * @param label3 the label3 to set
	 */
	public void setLabel3(String label3) {
		this.label3 = label3;
	}
	/**
	 * @return the label4
	 */
	public String getLabel4() {
		return label4;
	}
	/**
	 * @param label4 the label4 to set
	 */
	public void setLabel4(String label4) {
		this.label4 = label4;
	}
	/**
	 * @return the label5
	 */
	public String getLabel5() {
		return label5;
	}
	/**
	 * @param label5 the label5 to set
	 */
	public void setLabel5(String label5) {
		this.label5 = label5;
	}
	/**
	 * @return the label6
	 */
	public String getLabel6() {
		return label6;
	}
	/**
	 * @param label6 the label6 to set
	 */
	public void setLabel6(String label6) {
		this.label6 = label6;
	}
	/**
	 * @return the label7
	 */
	public String getLabel7() {
		return label7;
	}
	/**
	 * @param label7 the label7 to set
	 */
	public void setLabel7(String label7) {
		this.label7 = label7;
	}
	/**
	 * @return the label8
	 */
	public String getLabel8() {
		return label8;
	}
	/**
	 * @param label8 the label8 to set
	 */
	public void setLabel8(String label8) {
		this.label8 = label8;
	}
	/**
	 * @return the label9
	 */
	public String getLabel9() {
		return label9;
	}
	/**
	 * @param label9 the label9 to set
	 */
	public void setLabel9(String label9) {
		this.label9 = label9;
	}
	/**
	 * @return the label10
	 */
	public String getLabel10() {
		return label10;
	}
	/**
	 * @param label10 the label10 to set
	 */
	public void setLabel10(String label10) {
		this.label10 = label10;
	}
	/**
	 * @return the last_ym
	 */
	public String getLast_ym() {
		return last_ym;
	}
	/**
	 * @param last_ym the last_ym to set
	 */
	public void setLast_ym(String last_ym) {
		this.last_ym = last_ym;
	}
	/**
	 * @return the last_ym_hy
	 */
	public String getLast_ym_hy() {
		return last_ym_hy;
	}
	/**
	 * @param last_ym_hy the last_ym_hy to set
	 */
	public void setLast_ym_hy(String last_ym_hy) {
		this.last_ym_hy = last_ym_hy;
	}
	/**
	 * @return the middle_ym
	 */
	public String getMiddle_ym() {
		return middle_ym;
	}
	/**
	 * @param middle_ym the middle_ym to set
	 */
	public void setMiddle_ym(String middle_ym) {
		this.middle_ym = middle_ym;
	}
	/**
	 * @return the middle_ym_list
	 */
	public String getMiddle_ym_list() {
		return middle_ym_list;
	}
	/**
	 * @param middle_ym_list the middle_ym_list to set
	 */
	public void setMiddle_ym_list(String middle_ym_list) {
		this.middle_ym_list = middle_ym_list;
	}
	/**
	 * @return the pattern
	 */
	public String getPattern() {
		return pattern;
	}
	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	/**
	 * @return the satei_ichi
	 */
	public String getSatei_ichi() {
		return satei_ichi;
	}
	/**
	 * @param satei_ichi the satei_ichi to set
	 */
	public void setSatei_ichi(String satei_ichi) {
		this.satei_ichi = satei_ichi;
	}
	/**
	 * @return the satei_kaisya_cd
	 */
	public String getSatei_kaisya_cd() {
		return satei_kaisya_cd;
	}
	/**
	 * @param satei_kaisya_cd the satei_kaisya_cd to set
	 */
	public void setSatei_kaisya_cd(String satei_kaisya_cd) {
		this.satei_kaisya_cd = satei_kaisya_cd;
	}
	/**
	 * @return the satei_ki
	 */
	public String getSatei_ki() {
		return satei_ki;
	}
	/**
	 * @param satei_ki the satei_ki to set
	 */
	public void setSatei_ki(String satei_ki) {
		this.satei_ki = satei_ki;
	}
	/**
	 * @return the satei_mi_shori
	 */
	public String getSatei_mi_shori() {
		return satei_mi_shori;
	}
	/**
	 * @param satei_mi_shori the satei_mi_shori to set
	 */
	public void setSatei_mi_shori(String satei_mi_shori) {
		this.satei_mi_shori = satei_mi_shori;
	}
	/**
	 * @return the satei_ni
	 */
	public String getSatei_ni() {
		return satei_ni;
	}
	/**
	 * @param satei_ni the satei_ni to set
	 */
	public void setSatei_ni(String satei_ni) {
		this.satei_ni = satei_ni;
	}
	/**
	 * @return the satei_zumi
	 */
	public String getSatei_zumi() {
		return satei_zumi;
	}
	/**
	 * @param satei_zumi the satei_zumi to set
	 */
	public void setSatei_zumi(String satei_zumi) {
		this.satei_zumi = satei_zumi;
	}
	/**
	 * @return the soshiki
	 */
	public String getSoshiki() {
		return soshiki;
	}
	/**
	 * @param soshiki the soshiki to set
	 */
	public void setSoshiki(String soshiki) {
		this.soshiki = soshiki;
	}
	/**
	 * @return the tairyuu_mi_shori
	 */
	public String getTairyuu_mi_shori() {
		return tairyuu_mi_shori;
	}
	/**
	 * @param tairyuu_mi_shori the tairyuu_mi_shori to set
	 */
	public void setTairyuu_mi_shori(String tairyuu_mi_shori) {
		this.tairyuu_mi_shori = tairyuu_mi_shori;
	}
	/**
	 * @return the tairyuu_shori
	 */
	public String getTairyuu_shori() {
		return tairyuu_shori;
	}
	/**
	 * @param tairyuu_shori the tairyuu_shori to set
	 */
	public void setTairyuu_shori(String tairyuu_shori) {
		this.tairyuu_shori = tairyuu_shori;
	}
	/**
	 * @return the tairyuu_zumi
	 */
	public String getTairyuu_zumi() {
		return tairyuu_zumi;
	}
	/**
	 * @return the ar_satei_resualt
	 */
	public List getAr_satei_resualt() {
		return ar_satei_resualt;
	}
	/**
	 * @param ar_satei_resualt the ar_satei_resualt to set
	 */
	public void setAr_satei_resualt(List ar_satei_resualt) {
		this.ar_satei_resualt = ar_satei_resualt;
	}
	/**
	 * @return the ar_status
	 */
	public List getAr_status() {
		return ar_status;
	}
	/**
	 * @param ar_status the ar_status to set
	 */
	public void setAr_status(List ar_status) {
		this.ar_status = ar_status;
	}
	/**
	 * @param tairyuu_zumi the tairyuu_zumi to set
	 */
	public void setTairyuu_zumi(String tairyuu_zumi) {
		this.tairyuu_zumi = tairyuu_zumi;
	}
	/**
	 * @return the ym
	 */
	public String getYm() {
		return ym;
	}
	/**
	 * @param ym the ym to set
	 */
	public void setYm(String ym) {
		this.ym = ym;
	}
	/**
	 * @return the ar_oparation
	 */
	public List getAr_oparation() {
		return ar_oparation;
	}
	/**
	 * @param ar_oparation the ar_oparation to set
	 */
	public void setAr_oparation(List ar_oparation) {
		this.ar_oparation = ar_oparation;
	}
	/**
	 * @return the ar_bunrui2
	 */
	public List getAr_bunrui2() {
		return ar_bunrui2;
	}
	/**
	 * @param ar_bunrui2 the ar_bunrui2 to set
	 */
	public void setAr_bunrui2(List ar_bunrui2) {
		this.ar_bunrui2 = ar_bunrui2;
	}
	/**
	 * @return the ar_satei_kaisha
	 */
	public List getAr_satei_kaisha() {
		return ar_satei_kaisha;
	}
	/**
	 * @param ar_satei_kaisha the ar_satei_kaisha to set
	 */
	public void setAr_satei_kaisha(List ar_satei_kaisha) {
		this.ar_satei_kaisha = ar_satei_kaisha;
	}
	/**
	 * @return the ar_systemkbn
	 */
	public List getAr_systemkbn() {
		return ar_systemkbn;
	}
	/**
	 * @param ar_systemkbn the ar_systemkbn to set
	 */
	public void setAr_systemkbn(List ar_systemkbn) {
		this.ar_systemkbn = ar_systemkbn;
	}
	/**
	 * @return the sankou_bunrui2
	 */
	public String getSankou_bunrui2() {
		return sankou_bunrui2;
	}
	/**
	 * @param sankou_bunrui2 the sankou_bunrui2 to set
	 */
	public void setSankou_bunrui2(String sankou_bunrui2) {
		this.sankou_bunrui2 = sankou_bunrui2;
	}
	/**
	 * @return the sankou_satei_kaisha
	 */
	public String getSankou_satei_kaisha() {
		return sankou_satei_kaisha;
	}
	/**
	 * @param sankou_satei_kaisha the sankou_satei_kaisha to set
	 */
	public void setSankou_satei_kaisha(String sankou_satei_kaisha) {
		this.sankou_satei_kaisha = sankou_satei_kaisha;
	}
	/**
	 * @return the sankou_systemkbn
	 */
	public String getSankou_systemkbn() {
		return sankou_systemkbn;
	}
	/**
	 * @param sankou_systemkbn the sankou_systemkbn to set
	 */
	public void setSankou_systemkbn(String sankou_systemkbn) {
		this.sankou_systemkbn = sankou_systemkbn;
	}
	/**
	 * @return the arg_pattern_id
	 */
	public String getArg_pattern_id() {
		return arg_pattern_id;
	}
	/**
	 * @param arg_pattern_id the arg_pattern_id to set
	 */
	public void setArg_pattern_id(String arg_pattern_id) {
		this.arg_pattern_id = arg_pattern_id;
	}
	/**
	 * @return the arg_satei_kaisha
	 */
	public String getArg_satei_kaisha() {
		return arg_satei_kaisha;
	}
	/**
	 * @param arg_satei_kaisha the arg_satei_kaisha to set
	 */
	public void setArg_satei_kaisha(String arg_satei_kaisha) {
		this.arg_satei_kaisha = arg_satei_kaisha;
	}
	/**
	 * @return the arg_system
	 */
	public String getArg_system() {
		return arg_system;
	}
	/**
	 * @param arg_system the arg_system to set
	 */
	public void setArg_system(String arg_system) {
		this.arg_system = arg_system;
	}
	/**
	 * @return the ar_taisho_ym
	 */
	public List getAr_taisho_ym() {
		return ar_taisho_ym;
	}
	/**
	 * @param ar_taisho_ym the ar_taisho_ym to set
	 */
	public void setAr_taisho_ym(List ar_taisho_ym) {
		this.ar_taisho_ym = ar_taisho_ym;
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
	 * @return the pattern_system_kbn
	 */
	public String getPattern_system_kbn() {
		return pattern_system_kbn;
	}
	/**
	 * @param pattern_system_kbn the pattern_system_kbn to set
	 */
	public void setPattern_system_kbn(String pattern_system_kbn) {
		this.pattern_system_kbn = pattern_system_kbn;
	}
	/**
	 * @return the pattern_sateikaisya_cd
	 */
	public String getPattern_sateikaisya_cd() {
		return pattern_sateikaisya_cd;
	}
	/**
	 * @param pattern_sateikaisya_cd the pattern_sateikaisya_cd to set
	 */
	public void setPattern_sateikaisya_cd(String pattern_sateikaisya_cd) {
		this.pattern_sateikaisya_cd = pattern_sateikaisya_cd;
	}
	/**
	 * @return the temp_pattern_id
	 */
	public String getTemp_pattern_id() {
		return temp_pattern_id;
	}
	/**
	 * @param temp_pattern_id the temp_pattern_id to set
	 */
	public void setTemp_pattern_id(String temp_pattern_id) {
		this.temp_pattern_id = temp_pattern_id;
	}
	/**
	 * @return the temp_pattern_system_kbn
	 */
	public String getTemp_pattern_system_kbn() {
		return temp_pattern_system_kbn;
	}
	/**
	 * @param temp_pattern_system_kbn the temp_pattern_system_kbn to set
	 */
	public void setTemp_pattern_system_kbn(String temp_pattern_system_kbn) {
		this.temp_pattern_system_kbn = temp_pattern_system_kbn;
	}
	/**
	 * @return the temp_pattern_sateikaisya_cd
	 */
	public String getTemp_pattern_sateikaisya_cd() {
		return temp_pattern_sateikaisya_cd;
	}
	/**
	 * @param temp_pattern_sateikaisya_cd the temp_pattern_sateikaisya_cd to set
	 */
	public void setTemp_pattern_sateikaisya_cd(String temp_pattern_sateikaisya_cd) {
		this.temp_pattern_sateikaisya_cd = temp_pattern_sateikaisya_cd;
	}
}