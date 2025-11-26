/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/02/25		SSC				新規作成
******************************************************************************/
package app.syokai.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * OS6105_督促メール送信先選択 アクションフォームクラス
 *
 */
public class TokusokumailForm extends AppPagerActionForm {

	private String soushinbu;								// 選択された送信部
	private LinkedHashMap<String,String> ar_soushinbu;		// 送信部セレクトボックス

	private String karento_tab;								// カレントタブ
	private String mail_gamen_id;							// メール用画面ID
	private String kanjo_cd;								// 勘定先CD
	private String kanjo_nm;								// 勘定先名称
	private String mae_sateiki;								// 前査定期
	private String mae_kijunbi_kbn;							// 基準日区分
	private String mae_kijunbi;								// 基準日
	private String radio_id;								// 選択されたラジオボタンのID

	//査定進捗
	private String syouri_dt_t;								// 処理日時タイトル
	private String anken_no_ser;							// 検索用案件No.
	private int id;											// リンククリックされたid
	private String phase;									// フェーズ(名)
	private String phase_id;								// フェーズ(ID)
	private String hanyou1;									// 汎用１
	private String soshiki_nm;								// 組織
	private String tanto_nm;								// 担当者名
	private String tanto_id;								// 担当者ID
	private String daiko_id;								// 代行者ID
	private String syouri;									// 処理
	private String syouri_dt;								// 処理日時
	private String anken_no;								// 案件No.
	private String ope_kbn;									// 入力区分
	private String toroku_point;							// 登録箇所
	private String comment_val;								// コメント内容
	private List<HashMap<String,String>> ar_anken_no;		// 査定案件Noリスト

	//滞留進捗
	private List<Map<String, String>> ar_sosiki;			// 組織一覧
	private List<HashMap<String,String>> ar_sinchoku;			// 進捗一覧
	private int id_sosiki;									// リンクされた組織ID
	private int id_sinchoku;								// リンクされた進捗ID
	private String syori_dt_til;							// 処理日時タイトル
	private String srh_anken_no;							// 検索用案件No.
	//新規担当者
	private String inTanto;	 								// 入力/検索
	private String txtTanto;	 							// 担当者アドレス
	private String selectedTantoId;							// 担当者ID
	private String tanto;									// 担当者
	private List<Map<String, String>> ar_Tanto;				// 担当者セレクトボックス用配列
	private String tanto_bumon_cd;							// 担当所属部門
	private String tanto_bu_cd;								// 担当所属部コード
	//督促メール
	private String haishin_Dt;								// 最新の配信日時
	private String hozon_Dt;								// 最新の保存日時
	private String mailSendFlg;								// メール送信フラグ


	// 変数初期化
	public TokusokumailForm() {
		super.gamenId = GS.OS6105;
		this.soushinbu = GS.EMPTY_CHARCTER;
		this.karento_tab = GS.EMPTY_CHARCTER;
		this.mail_gamen_id = GS.EMPTY_CHARCTER;
		this.kanjo_cd = GS.EMPTY_CHARCTER;
		this.kanjo_nm = GS.EMPTY_CHARCTER;
		this.mae_sateiki = GS.EMPTY_CHARCTER;
		this.mae_kijunbi_kbn = GS.EMPTY_CHARCTER;
		this.mae_kijunbi = GS.EMPTY_CHARCTER;
		this.ar_soushinbu = null;
		this.radio_id = GS.EMPTY_CHARCTER;
		//査定進捗
		this.syouri_dt_t = GS.EMPTY_CHARCTER;
		this.anken_no_ser = GS.EMPTY_CHARCTER;
		this.id = 0;
		this.ar_anken_no = null;
		this.phase = GS.EMPTY_CHARCTER;
		this.phase_id = GS.EMPTY_CHARCTER;
		this.hanyou1 = GS.EMPTY_CHARCTER;
		this.soshiki_nm = GS.EMPTY_CHARCTER;
		this.tanto_nm = GS.EMPTY_CHARCTER;
		this.tanto_id = GS.EMPTY_CHARCTER;
		this.syouri = GS.EMPTY_CHARCTER;
		this.syouri_dt = GS.EMPTY_CHARCTER;
		this.anken_no = GS.EMPTY_CHARCTER;
		this.ope_kbn = GS.EMPTY_CHARCTER;
		this.toroku_point = GS.EMPTY_CHARCTER;
		this.comment_val = GS.EMPTY_CHARCTER;
		//滞留進捗
		this.ar_sosiki = null;
		this.ar_sinchoku = null;
		this.id_sosiki = 0;
		this.id_sinchoku = 0;
		this.syori_dt_til = GS.EMPTY_CHARCTER;
		this.srh_anken_no = GS.EMPTY_CHARCTER;
		//新規担当者
        this.inTanto = GS.EMPTY_CHARCTER;
		this.txtTanto = GS.EMPTY_CHARCTER;
		this.selectedTantoId = GS.EMPTY_CHARCTER;
		this.tanto = GS.EMPTY_CHARCTER;
		this.ar_Tanto = null;
		this.tanto_bumon_cd = GS.EMPTY_CHARCTER;
		this.tanto_bu_cd = GS.EMPTY_CHARCTER;
		//督促メール
		this.haishin_Dt = GS.EMPTY_CHARCTER;
		this.hozon_Dt = GS.EMPTY_CHARCTER;
		this.mailSendFlg = GS.EMPTY_CHARCTER;
	}

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

	// アクセスメソッド

	/**
	 * 選択された送信部<br>
	 *
	 * @return ar_soushinbu
	 */
	public LinkedHashMap<String,String> getAr_soushinbu() {
		return ar_soushinbu;
	}

	/**
	 * 選択された送信部<br>
	 *
	 * @param ar_soushinbu
	 */
	public void setAr_soushinbu(LinkedHashMap<String,String> ar_soushinbu) {
		this.ar_soushinbu = ar_soushinbu;
	}
	/**
	 * 送信部<br>
	 *
	 * @return soushinbu
	 */
	public String getSoushinbu() {
		return soushinbu;
	}

	/**
	 * 送信部<br>
	 *
	 * @param soushinbu
	 */
	public void setSoushinbu(String soushinbu) {
		this.soushinbu = soushinbu;
	}

	/**
	 * カレントタブ<br>
	 *
	 * @return the karento_tab
	 */
	public String getKarento_tab() {
		return karento_tab;
	}

	/**
	 * カレントタブ<br>
	 *
	 * @param karento_tab
	 */
	public void setKarento_tab(String karento_tab) {
		this.karento_tab = karento_tab;
	}

	/**
	 * メール用画面ID<br>
	 *
	 * @return mail_gamen_id
	 */
	public String getMail_gamen_id() {
		return mail_gamen_id;
	}

	/**
	 * メール用画面ID<br>
	 *
	 * @param mail_gamen_id
	 */
	public void setMail_gamen_id(String mail_gamen_id) {
		this.mail_gamen_id = mail_gamen_id;
	}

	/**
	 * 前査定期<br>
	 * @return mae_sateiki
	 */
	public String getMae_sateiki() {
		return mae_sateiki;
	}

	/**
	 * 前査定期<br>
	 *
	 * @param mae_sateiki
	 */
	public void setMae_sateiki(String mae_sateiki) {
		this.mae_sateiki = mae_sateiki;
	}

	/**
	 * 前基準日区分<br>
	 *
	 * @return mae_kijunbi_kbn
	 */
	public String getMae_kijunbi_kbn() {
		return mae_kijunbi_kbn;
	}

	/**
	 * 前基準日区分<br>
	 *
	 * @param mae_kijunbi_kbn
	 */
	public void setMae_kijunbi_kbn(String mae_kijunbi_kbn) {
		this.mae_kijunbi_kbn = mae_kijunbi_kbn;
	}
	/**
	 * 前基準日<br>
	 *
	 * @return mae_kijunbi_kbn
	 */
	public String getMae_kijunbi() {
		return mae_kijunbi;
	}

	/**
	 * 前基準日<br>
	 *
	 * @param mae_kijunbi_kbn
	 */
	public void setMae_kijunbi(String mae_kijunbi) {
		this.mae_kijunbi = mae_kijunbi;
	}

	/**
	 * 選択されたラジオボタンのID
	 * @return radio_id
	 */
	public String getRadio_id() {
		return radio_id;
	}

	/**
	 * 選択されたラジオボタンのID
	 * @param radio_id
	 */
	public void setRadio_id(String radio_id) {
		this.radio_id = radio_id;
	}

	/**
	 *
	 * 勘定先CD <br>
	 *
	 * @return
	 */
	public String getKanjo_cd() {
		return kanjo_cd;
	}
	/**
	 *
	 * 勘定先CD <br>
	 *
	 * @param kanjo_cd
	 */
	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}
	/**
	 *
	 * 勘定先名称 <br>
	 *
	 * @return
	 */
	public String getKanjo_nm() {
		return kanjo_nm;
	}
	/**
	 *
	 * 勘定先名称 <br>
	 *
	 * @param kanjo_nm
	 */
	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}

	/**
	 * 案件No.
	 *
	 * @return anken_no
	 */
	public String getAnken_no() {
		return anken_no;
	}

	/**
	 * 案件No.
	 *
	 * @param anken_no
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}

	/**
	 * 検索用案件No.
	 *
	 * @return anken_no_ser
	 */
	public String getAnken_no_ser() {
		return anken_no_ser;
	}

	/**
	 * 検索用案件No.
	 *
	 * @param anken_no_ser
	 */
	public void setAnken_no_ser(String anken_no_ser) {
		this.anken_no_ser = anken_no_ser;
	}

	/**
	 * コメント内容
	 *
	 * @return comment_val
	 */
	public String getComment_val() {
		return comment_val;
	}

	/**
	 * コメント内容
	 *
	 * @param comment_val
	 */
	public void setComment_val(String comment_val) {
		this.comment_val = comment_val;
	}

	/**
	 * 汎用１
	 *
	 * @return hanyou1
	 */
	public String getHanyou1() {
		return hanyou1;
	}

	/**
	 * 汎用１
	 *
	 * @param hanyou1
	 */
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}

	/**
	 * 入力区分
	 *
	 * @return ope_kbn
	 */
	public String getOpe_kbn() {
		return ope_kbn;
	}

	/**
	 * 入力区分
	 *
	 * @param ope_kbn
	 */
	public void setOpe_kbn(String ope_kbn) {
		this.ope_kbn = ope_kbn;
	}

	/**
	 * フェーズ（名）
	 *
	 * @return phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * フェーズ（名）
	 *
	 * @param phase
	 */
	public void setPhase(String phase) {
		this.phase = phase;
	}

	/**
	 * フェーズ（ID）
	 * @return phase_id
	 */
	public String getPhase_id() {
		return phase_id;
	}

	/**
	 * フェーズ（ID）
	 * @param phase_id
	 */
	public void setPhase_id(String phase_id) {
		this.phase_id = phase_id;
	}

	/**
	 * 組織
	 *
	 * @return soshiki_nm
	 */
	public String getSoshiki_nm() {
		return soshiki_nm;
	}

	/**
	 * 組織
	 *
	 * @param soshiki_nm
	 */
	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}

	/**
	 * 処理
	 *
	 * @return syouri
	 */
	public String getSyouri() {
		return syouri;
	}

	/**
	 * 処理
	 *
	 * @param syouri
	 */
	public void setSyouri(String syouri) {
		this.syouri = syouri;
	}

	/**
	 * 処理日時
	 *
	 * @return syouri_dt
	 */
	public String getSyouri_dt() {
		return syouri_dt;
	}

	/**
	 * 処理日時
	 *
	 * @param syouri_dt
	 */
	public void setSyouri_dt(String syouri_dt) {
		this.syouri_dt = syouri_dt;
	}

	/**
	 * 処理日時タイトル
	 *
	 * @return syouri_dt_t
	 */
	public String getSyouri_dt_t() {
		return syouri_dt_t;
	}

	/**
	 * 処理日時タイトル
	 *
	 * @param syouri_dt_t
	 */
	public void setSyouri_dt_t(String syouri_dt_t) {
		this.syouri_dt_t = syouri_dt_t;
	}

	/**
	 * 担当者名
	 *
	 * @return tanto_nm
	 */
	public String getTanto_nm() {
		return tanto_nm;
	}

	/**
	 * 担当者名
	 *
	 * @param tanto_nm
	 */
	public void setTanto_nm(String tanto_nm) {
		this.tanto_nm = tanto_nm;
	}

	/**
	 * 担当者ID
	 *
	 * @return tanto_id
	 */
	public String getTanto_id() {
		return tanto_id;
	}

	/**
	 * 担当者ID
	 *
	 * @param tanto_nm
	 */
	public void setTanto_id(String tanto_id) {
		this.tanto_id = tanto_id;
	}

	/**
	 * 登録箇所
	 *
	 * @return toroku_point
	 */
	public String getToroku_point() {
		return toroku_point;
	}

	/**
	 * 登録箇所
	 *
	 * @param toroku_point
	 */
	public void setToroku_point(String toroku_point) {
		this.toroku_point = toroku_point;
	}

	/**
	 * 査定案件Noリスト
	 *
	 * @return ar_anken_no
	 */
	public List<HashMap<String,String>> getAr_anken_no() {
		return ar_anken_no;
	}

	/**
	 * 査定案件Noリスト
	 *
	 * @param ar_anken_no
	 */
	public void setAr_anken_no(List<HashMap<String,String>> ar_anken_no) {
		this.ar_anken_no = ar_anken_no;
	}

	/**
	 * リンククリックされたid
	 *
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * リンククリックされたid
	 *
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 進捗一覧
	 *
	 * @return ar_sinchoku
	 */
	public List<HashMap<String,String>> getAr_sinchoku() {
		return ar_sinchoku;
	}

	/**
	 * 進捗一覧
	 *
	 * @param ar_sinchoku
	 */
	public void setAr_sinchoku(List<HashMap<String,String>> ar_sinchoku) {
		this.ar_sinchoku = ar_sinchoku;
	}

	/**
	 * 組織一覧
	 *
	 * @return ar_sosiki
	 */
	public List<Map<String, String>> getAr_sosiki() {
		return ar_sosiki;
	}

	/**
	 * 組織一覧
	 *
	 * @param ar_sosiki
	 */
	public void setAr_sosiki(List<Map<String, String>> ar_sosiki) {
		this.ar_sosiki = ar_sosiki;
	}

	/**
	 * リンクされた進捗ID
	 *
	 * @return id_sinchoku
	 */
	public int getId_sinchoku() {
		return id_sinchoku;
	}

	/**
	 * リンクされた進捗ID
	 *
	 * @param id_sinchoku
	 */
	public void setId_sinchoku(int id_sinchoku) {
		this.id_sinchoku = id_sinchoku;
	}

	/**
	 * リンクされた組織ID
	 *
	 * @return id_sosiki
	 */
	public int getId_sosiki() {
		return id_sosiki;
	}

	/**
	 * リンクされた組織ID
	 *
	 * @param id_sosiki
	 */
	public void setId_sosiki(int id_sosiki) {
		this.id_sosiki = id_sosiki;
	}

	/**
	 * 処理日時タイトル
	 *
	 * @return syori_dt_til
	 */
	public String getSyori_dt_til() {
		return syori_dt_til;
	}

	/**
	 * 処理日時タイトル
	 *
	 * @param syori_dt_til
	 */
	public void setSyori_dt_til(String syori_dt_til) {
		this.syori_dt_til = syori_dt_til;
	}

	/**
	 * 検索用案件No.
	 *
	 * @return srh_anken_no
	 */
	public String getSrh_anken_no() {
		return srh_anken_no;
	}

	/**
	 * 検索用案件No.
	 *
	 * @param srh_anken_no
	 */
	public void setSrh_anken_no(String srh_anken_no) {
		this.srh_anken_no = srh_anken_no;
	}
	/**
	 * 入力/検索
	 *
	 * @return
	 */
	public String getInTanto() {
		return inTanto;
	}
	/**
	 * 入力/検索
	 *
	 * @param inTanto
	 */
	public void setInTanto(String inTanto) {
		this.inTanto = inTanto;
	}

	/**
	 * 担当者アドレス
	 *
	 * @return
	 */
	public String getTxtTanto() {
		return txtTanto;
	}

	/**
	 * 担当者アドレス
	 *
	 * @param txtTanto
	 */
	public void setTxtTanto(String txtTanto) {
		this.txtTanto = txtTanto;
	}

	/**
	 * 担当者ID
	 *
	 * @return
	 */
	public String getSelectedTantoId() {
		return selectedTantoId;
	}

	/**
	 * 担当者ID
	 *
	 * @param selectedTantoId
	 */
	public void setSelectedTantoId(String selectedTantoId) {
		this.selectedTantoId = selectedTantoId;
	}

	/**
	 * 担当者
	 *
	 * @return
	 */
	public String getTanto() {
		return tanto;
	}

	/**
	 * 担当者
	 *
	 * @param tanto
	 */
	public void setTanto(String tanto) {
		this.tanto = tanto;
	}

	/**
	 * 担当者配列
	 *
	 * @return
	 */
	public List<Map<String, String>> getAr_Tanto() {
		return ar_Tanto;
	}

	/**
	 * 担当者配列
	 *
	 * @param ar_Tanto
	 */
	public void setAr_Tanto(List<Map<String, String>> ar_Tanto) {
		this.ar_Tanto = ar_Tanto;
	}

	/**
	 * 担当所属部門
	 *
	 * @return
	 */
	public String getTanto_bumon_cd() {
		return tanto_bumon_cd;
	}

	/**
	 * 担当所属部門
	 *
	 * @param tanto_bumon_cd
	 */
	public void setTanto_bumon_cd(String tanto_bumon_cd) {
		this.tanto_bumon_cd = tanto_bumon_cd;
	}

	/**
	 * 担当所属部コード
	 *
	 * @return
	 */
	public String getTanto_bu_cd() {
		return tanto_bu_cd;
	}

	/**
	 * 担当所属部コード
	 *
	 * @param tanto_bu_cd
	 */
	public void setTanto_bu_cd(String tanto_bu_cd) {
		this.tanto_bu_cd = tanto_bu_cd;
	}

	/**
	 * 最新の配信日時
	 *
	 * @return
	 */
	public String getHaishin_Dt() {
		return haishin_Dt;
	}

	/**
	 * 最新の配信日時
	 *
	 * @param haishin_Dt
	 */
	public void setHaishin_Dt(String haishin_Dt) {
		this.haishin_Dt = haishin_Dt;
	}

	/**
	 * 最新の保存日時
	 *
	 * @return
	 */
	public String getHozon_Dt() {
		return hozon_Dt;
	}

	/**
	 * 最新の保存日時
	 *
	 * @param hozon_Dt
	 */
	public void setHozon_Dt(String hozon_Dt) {
		this.hozon_Dt = hozon_Dt;
	}

	/**
	 * メール送信フラグ
	 *
	 * @return mailSend_Flg
	 */
	public String getMailSendFlg() {
		return mailSendFlg;
	}

	/**
	 * メール送信フラグ
	 *
	 * @param mailSend_Flg
	 */
	public void setMailSendFlg(String mailSendFlg) {
		this.mailSendFlg = mailSendFlg;
	}

	/**
	 * 代行者ID
	 * @return daiko_id
	 */
	public String getDaiko_id() {
		return daiko_id;
	}

	/**
	 * 代行者ID
	 * @param daiko_id
	 */
	public void setDaiko_id(String daiko_id) {
		this.daiko_id = daiko_id;
	}


}