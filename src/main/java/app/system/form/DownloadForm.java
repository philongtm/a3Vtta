/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
004		2009/12/09		SSC				課題No.195 国内帳票ダウンロード時、必須入力チェックに分類２を追加  
******************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * OS8101_帳票ダウンロード アクションフォームクラス <br>
 */
public class DownloadForm extends AppPagerActionForm {

	private String selectedlist; 					// 帳票種別
	private String list_type; 						// 画面で選択された帳票種別(システム区分+区分値)
	private LinkedHashMap ar_list_type; 			// 帳票種別セレクトボックス【リスト】
	private String sateiki; 						// 画面で入力された査定期
	private String hanki_sihanki_kbn;				// 画面で選択された半期四半期区分
	private LinkedHashMap ar_hanki_sihanki_kbn;	// 半期四半期区分セレクトボックス【リスト】
	private boolean hanki_sihanki_kbn_hyoji_flg;	// 半期四半期区分表示判定用フラグ
	private String taisyo_ym; 						// 画面で入力された対象年月
	private String sime_kbn;						// 画面で選択された〆区分
	private LinkedHashMap ar_sime_kbn;				// 〆区分セレクトボックス【リスト】
	private boolean sime_kbn_hyoji_flg;			// 〆区分表示判定用フラグ
	private String kanjo_cd; 						// 画面で入力された勘定先CD
	private String kanjo_nm; 						// 画面で入力された勘定先名称
	private String duns_no; 						// 画面で入力されたDUNS No.
	private String hanyou1Title;					// 汎用１タイトル名
	private String hanyou2Title;					// 汎用２タイトル名
	private String hanyo1; 						// 画面で選択された汎用１
	private LinkedHashMap ar_hanyo1; 				// 汎用１セレクトボックス【リスト】
	private String hanyo2; 						// 画面で選択された汎用２
	private LinkedHashMap ar_hanyo2; 				// 汎用２セレクトボックス【リスト】
	private String hanyo1_all; 					// 全汎用1(シングルコートで括り、カンマ区切りで設定)
	private List list_hanyo1_all; 					// 全汎用1【リスト】
	private String hanyo2_1; 						// 分類２（帳票種別選択時に設定）(シングルコートで括り、カンマ区切りで設定)
	private String hanyo2_2;	 					// 分類２（汎用１選択時に設定）(シングルコートで括り、カンマ区切りで設定)
	private List temp_hanyo2; 						// temp汎用２
	private String search_system_kbn;				// 帳票出力用システム区分
	private String search_sateikaisya_cd;			// 帳票出力用査定会社コード
	private String search_bunrui2;					// 帳票出力用分類２
	private String search_sateiki;					// 帳票出力用査定期
	private String search_taisyo_ym;				// 帳票出力用対象年月
	private String bgcolorSateiki;					// 査定期スタイル
	private String bgcolorTaisyo_ym;				// 対象年月スタイル
	private String bgcolorHanyou1;					// 汎用１スタイル

    /**
     * 変数初期化 <br>
     */
    public DownloadForm() {
        super.gamenId = GS.OS8101;
        this.selectedlist = GS.EMPTY_CHARCTER;
        this.list_type = GS.EMPTY_CHARCTER;
        this.ar_list_type = null;
        this.sateiki = GS.EMPTY_CHARCTER;
        this.hanki_sihanki_kbn = GS.EMPTY_CHARCTER;
    	this.ar_hanki_sihanki_kbn = null;
    	this.hanki_sihanki_kbn_hyoji_flg = true;
        this.taisyo_ym = GS.EMPTY_CHARCTER;
        this.sime_kbn = GS.EMPTY_CHARCTER;
    	this.ar_sime_kbn = null;
    	this.sime_kbn_hyoji_flg = false;
    	this.kanjo_cd = GS.EMPTY_CHARCTER;
    	this.kanjo_nm = GS.EMPTY_CHARCTER;
    	this.duns_no = GS.EMPTY_CHARCTER;
    	this.hanyou1Title = GS.EMPTY_CHARCTER;
    	this.hanyou2Title = GS.EMPTY_CHARCTER;
    	this.hanyo1 = GS.EMPTY_CHARCTER;
    	this.ar_hanyo1 = new LinkedHashMap();
    	this.hanyo2 = GS.EMPTY_CHARCTER;
    	this.ar_hanyo2 = new LinkedHashMap();
    	this.hanyo1_all = GS.EMPTY_CHARCTER;
    	this.list_hanyo1_all = null;
    	this.hanyo2_1 = GS.EMPTY_CHARCTER;
    	this.hanyo2_2 = GS.EMPTY_CHARCTER;
    	this.temp_hanyo2 = null;
    	this.search_system_kbn = GS.EMPTY_CHARCTER;
    	this.search_sateikaisya_cd = GS.EMPTY_CHARCTER;
    	this.search_bunrui2 = GS.EMPTY_CHARCTER;
    	this.search_sateiki = GS.EMPTY_CHARCTER;
    	this.search_taisyo_ym = GS.EMPTY_CHARCTER;
    	this.bgcolorSateiki = GS.EMPTY_CHARCTER;
    	this.bgcolorTaisyo_ym = GS.EMPTY_CHARCTER;
    	this.bgcolorHanyou1 = GS.EMPTY_CHARCTER;
    }
    
	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    // アクセスメソッド

    //帳票種別
    public String getSelectedlist() {
        return selectedlist;
    }
    public void setSelectedlist(String selectedlist) {
        this.selectedlist = selectedlist;
    }
    //帳票種別(システム区分+区分値)
    public String getList_type() {
        return list_type;
    }
    public void setList_type(String list_type) {
        this.list_type = list_type;
    }
    //帳票種別セレクトボックス【リスト】
	public LinkedHashMap getAr_list_type() {
		return ar_list_type;
	}
	public void setAr_list_type(LinkedHashMap ar_list_type) {
		this.ar_list_type = ar_list_type;
	}
	//査定期
	public String getSateiki() {
		return Function.trim(sateiki);
	}
	public void setSateiki(String sateiki) {
		this.sateiki = sateiki;
	}
	//半期四半期区分
	public String getHanki_sihanki_kbn() {
		return hanki_sihanki_kbn;
	}
	public void setHanki_sihanki_kbn(String hanki_sihanki_kbn) {
		this.hanki_sihanki_kbn = hanki_sihanki_kbn;
	}
	//半期四半期区分セレクトボックス【リスト】
	public LinkedHashMap getAr_hanki_sihanki_kbn() {
		return ar_hanki_sihanki_kbn;
	}
	public void setAr_hanki_sihanki_kbn(LinkedHashMap ar_hanki_sihanki_kbn) {
		this.ar_hanki_sihanki_kbn = ar_hanki_sihanki_kbn;
	}
	//半期四半期区分表示判定用フラグ
	public boolean isHanki_sihanki_kbn_hyoji_flg() {
		return hanki_sihanki_kbn_hyoji_flg;
	}
	public void setHanki_sihanki_kbn_hyoji_flg(boolean hanki_sihanki_kbn_hyoji_flg) {
		this.hanki_sihanki_kbn_hyoji_flg = hanki_sihanki_kbn_hyoji_flg;
	}
	//対象年月
	public String getTaisyo_ym() {
		return Function.trim(taisyo_ym);
	}
	public void setTaisyo_ym(String taisyo_ym) {
		this.taisyo_ym = taisyo_ym;
	}
	//〆区分
	public String getSime_kbn() {
		return sime_kbn;
	}
	public void setSime_kbn(String sime_kbn) {
		this.sime_kbn = sime_kbn;
	}
	//〆区分セレクトボックス【リスト】
	public LinkedHashMap getAr_sime_kbn() {
		return ar_sime_kbn;
	}
	public void setAr_sime_kbn(LinkedHashMap ar_sime_kbn) {
		this.ar_sime_kbn = ar_sime_kbn;
	}
	//〆区分表示判定用フラグ
	public boolean isSime_kbn_hyoji_flg() {
		return sime_kbn_hyoji_flg;
	}
	public void setSime_kbn_hyoji_flg(boolean sime_kbn_hyoji_flg) {
		this.sime_kbn_hyoji_flg = sime_kbn_hyoji_flg;
	}
	//勘定先CD
	public String getKanjo_cd() {
		return Function.trim(kanjo_cd);
	}
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}
	//勘定先名称
	public String getKanjo_nm() {
		return Function.trim(kanjo_nm);
	}
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}
	//DUNS No.
	public String getDuns_no() {
		return Function.trim(duns_no);
	}
	public void setDuns_no(String duns_no) {
		this.duns_no = duns_no;
	}
	//汎用１タイトル名
	public String getHanyou1Title() {
		return hanyou1Title;
	}
	public void setHanyou1Title(String hanyou1Title) {
		this.hanyou1Title = hanyou1Title;
	}
	//汎用２タイトル名
	public String getHanyou2Title() {
		return hanyou2Title;
	}
	public void setHanyou2Title(String hanyou2Title) {
		this.hanyou2Title = hanyou2Title;
	}
	//汎用１
	public String getHanyo1() {
		return hanyo1;
	}
	public void setHanyo1(String hanyo1) {
		this.hanyo1 = hanyo1;
	}
	//汎用１セレクトボックス【リスト】
	public LinkedHashMap getAr_hanyo1() {
		return ar_hanyo1;
	}
	public void setAr_hanyo1(LinkedHashMap ar_hanyo1) {
		this.ar_hanyo1 = ar_hanyo1;
	}
	//汎用２
	public String getHanyo2() {
		return hanyo2;
	}
	public void setHanyo2(String hanyo2) {
		this.hanyo2 = hanyo2;
	}
	//汎用２セレクトボックス【リスト】
	public LinkedHashMap getAr_hanyo2() {
		return ar_hanyo2;
	}
	public void setAr_hanyo2(LinkedHashMap ar_hanyo2) {
		this.ar_hanyo2 = ar_hanyo2;
	}
	//全汎用1
	public String getHanyo1_all() {
		return hanyo1_all;
	}
	public void setHanyo1_all(String hanyo1_all) {
		this.hanyo1_all = hanyo1_all;
	}
	//全汎用1【リスト】
	public List getList_hanyo1_all() {
		return list_hanyo1_all;
	}
	public void setList_hanyo1_all(List list_hanyo1_all) {
		this.list_hanyo1_all = list_hanyo1_all;
	}
	//分類２（帳票種別選択時に設定）
	public String getHanyo2_1() {
		return hanyo2_1;
	}
	public void setHanyo2_1(String hanyo2_1) {
		this.hanyo2_1 = hanyo2_1;
	}
	//分類２（汎用１選択時に設定）
	public String getHanyo2_2() {
		return hanyo2_2;
	}
	public void setHanyo2_2(String hanyo2_2) {
		this.hanyo2_2 = hanyo2_2;
	}
	//temp汎用２
	public List getTemp_hanyo2() {
		return temp_hanyo2;
	}
	public void setTemp_hanyo2(List temp_hanyo2) {
		this.temp_hanyo2 = temp_hanyo2;
	}
	//帳票出力用システム区分
	public String getSearch_system_kbn() {
		return search_system_kbn;
	}
	public void setSearch_system_kbn(String search_system_kbn) {
		this.search_system_kbn = search_system_kbn;
	}
	//帳票出力用査定会社コード
	public String getSearch_sateikaisya_cd() {
		return search_sateikaisya_cd;
	}
	public void setSearch_sateikaisya_cd(String search_sateikaisya_cd) {
		this.search_sateikaisya_cd = search_sateikaisya_cd;
	}
	//帳票出力用分類２
	public String getSearch_bunrui2() {
		return search_bunrui2;
	}
	public void setSearch_bunrui2(String search_bunrui2) {
		this.search_bunrui2 = search_bunrui2;
	}
	//帳票出力用査定期
	public String getSearch_sateiki() {
		return search_sateiki;
	}
	public void setSearch_sateiki(String search_sateiki) {
		this.search_sateiki = search_sateiki;
	}
	//帳票出力用対象年月
	public String getSearch_taisyo_ym() {
		return search_taisyo_ym;
	}
	public void setSearch_taisyo_ym(String search_taisyo_ym) {
		this.search_taisyo_ym = search_taisyo_ym;
	}
	//査定期スタイル
	public String getBgcolorSateiki() {
		return bgcolorSateiki;
	}
	public void setBgcolorSateiki(String bgcolorSateiki) {
		this.bgcolorSateiki = bgcolorSateiki;
	}
	//対象年月スタイル
	public String getBgcolorTaisyo_ym() {
		return bgcolorTaisyo_ym;
	}
	public void setBgcolorTaisyo_ym(String bgcolorTaisyo_ym) {
		this.bgcolorTaisyo_ym = bgcolorTaisyo_ym;
	}
	//汎用１スタイル
	public String getBgcolorHanyou1() {
		return bgcolorHanyou1;
	}
	public void setBgcolorHanyou1(String bgcolorHanyou1) {
		this.bgcolorHanyou1 = bgcolorHanyou1;
	}
}