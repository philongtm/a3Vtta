/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0_67
更新履歴
No		日付			修正者			修正内容
001		2015/03/20		SSC				新規作成
******************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;



/**
 * OS7115_メール送信先選択 アクションフォームクラス <br>
 */
public class MailSoushinSentakuForm extends AppPagerActionForm {
	private static final long serialVersionUID = 1L;
	private String hanyou1Title;								// 汎用1タイトル
	private String hanyou1;										// 汎用1
	private LinkedHashMap<String,String> ar_hanyou1;			// 汎用1セレクトボックス用配列
	private String hanyou2Title;								// 汎用2タイトル
	private String hanyou2;										// 汎用2
	private LinkedHashMap<String,String> ar_hanyou2;			// 汎用2セレクトボックス用配列
	private String hanyou3Title;								// 汎用3タイトル
	private String systemKbn;									// システム区分
	private int id;												// チェックボックス

	private String kensakuFlg;									// 検索条件のフラグ 1：最新、2：全件
	private String kensakuYm;									// 検索条件の年月（最新の場合のみ）

	private String soushin_chk;									// チェック情報
	private String hanei_flg;									// 反映フラグ
	private List<HashMap<String, String>> soushin_meisai;		// ユーザーマスタメンテナンス_登録へ渡す明細
	private String errChkFlg;										// エラーチェック用のフラグ

	/**
	 * 変数初期化 <br>
	 */
	public MailSoushinSentakuForm() {
		super.gamenId = GS.OS7115;
		this.hanyou1Title = GS.EMPTY_CHARCTER;
		this.hanyou1 = GS.EMPTY_CHARCTER;
		this.ar_hanyou1 = null;
		this.hanyou2Title = GS.EMPTY_CHARCTER;
		this.hanyou2 = GS.EMPTY_CHARCTER;
		this.ar_hanyou2 = null;
		this.hanyou3Title = GS.EMPTY_CHARCTER;
		this.id = 0;

		this.kensakuFlg = GS.EMPTY_CHARCTER;
		this.kensakuYm = GS.EMPTY_CHARCTER;

		this.soushin_chk = GS.EMPTY_CHARCTER;
		this.hanei_flg = "0";
		this.soushin_meisai = null;
		this.errChkFlg = "0";

		this.setPager(new ArrayList<List>());
		this.setAr_meisai(new ArrayList<List>());
	}

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString() {
		return super.gamenId;
	}

	/**
	 * 汎用1タイトル
	 * @return hanyou1Title
	 */
	public String getHanyou1Title() {
		return hanyou1Title;
	}

	/**
	 * 汎用1タイトル
	 * @param hanyou1Title
	 */
	public void setHanyou1Title(String hanyou1Title) {
		this.hanyou1Title = hanyou1Title;
	}
	/**
	 * 汎用1
	 * @return hanyou1
	 */
	public String getHanyou1() {
		return hanyou1;
	}

	/**
	 * 汎用1
	 * @param hanyou1
	 */
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}

	/**
	 * 汎用1セレクトボックスのリスト
	 * @return ar_hanyou1
	 */
	public LinkedHashMap<String, String> getAr_hanyou1() {
		return ar_hanyou1;
	}

	/**
	 * 汎用1セレクトボックスのリスト
	 * @param ar_hanyou1
	 */
	public void setAr_hanyou1(LinkedHashMap<String, String> ar_hanyou1) {
		this.ar_hanyou1 = ar_hanyou1;
	}

	/**
	 * 汎用2タイトル
	 * @return hanyou2Title
	 */
	public String getHanyou2Title() {
		return hanyou2Title;
	}

	/**
	 * 汎用2タイトル
	 * @param hanyou2Title
	 */
	public void setHanyou2Title(String hanyou2Title) {
		this.hanyou2Title = hanyou2Title;
	}

	/**
	 * 汎用2
	 * @return hanyou2
	 */
	public String getHanyou2() {
		return hanyou2;
	}

	/**
	 * 汎用2
	 * @param hanyou2
	 */
	public void setHanyou2(String hanyou2) {
		this.hanyou2 = hanyou2;
	}

	/**
	 * 汎用2セレクトボックスのリスト
	 * @return ar_hanyou2
	 */
	public LinkedHashMap<String, String> getAr_hanyou2() {
		return ar_hanyou2;
	}

	/**
	 * 汎用2セレクトボックスのリスト
	 * @param ar_hanyou2
	 */
	public void setAr_hanyou2(LinkedHashMap<String, String> ar_hanyou2) {
		this.ar_hanyou2 = ar_hanyou2;
	}

	/**
	 * 汎用3タイトル
	 * @return hanyou2Title
	 */
	public String getHanyou3Title() {
		return hanyou3Title;
	}

	/**
	 * 汎用3タイトル
	 * @param hanyou2Title
	 */
	public void setHanyou3Title(String hanyou3Title) {
		this.hanyou3Title = hanyou3Title;
	}


	/**
	 * システム区分
	 * @return systemKbn
	 */
	public String getSystemKbn() {
		return systemKbn;
	}

	/**
	 * システム区分
	 * @param systemKbn
	 */
	public void setSystemKbn(String systemKbn) {
		this.systemKbn = systemKbn;
	}
	/**
	 * チェックボックス
	 * @return id
	 */
	public int getId() {
		return id;
	}

	/**
	 * チェックボックス
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 検索フラグ<BR>
	 *  1：最新、2：全件
	 * @return kensaku_saishin
	 */
	public String getKensakuFlg() {
		return kensakuFlg;
	}

	/**
	 * 検索フラグ<BR>
	 *  1：最新、2：全件
	 * @param kensakuFlg
	 */
	public void setKensakuFlg(String kensakuFlg) {
		this.kensakuFlg = kensakuFlg;
	}


	/**
	 * 検索用最新年月
	 * @return kensakuYm
	 */
	public String getKensakuYm() {
		return kensakuYm;
	}

	/**
	 * 検索用最新年月
	 * @param kensakuYm
	 */
	public void setKensakuYm(String kensakuYm) {
		this.kensakuYm = kensakuYm;
	}

	/**
	 * チェック情報
	 * @return soushin_chk
	 */
	public String getSoushin_chk() {
		return soushin_chk;
	}

	/**
	 * チェック情報
	 * @param soushin_chk
	 */
	public void setSoushin_chk(String soushin_chk) {
		this.soushin_chk = soushin_chk;
	}

	/**
	 * 汎用1
	 * @return hanei_flg
	 */
	public String getHanei_flg() {
		return hanei_flg;
	}

	/**
	 * 汎用1
	 * @param hanei_flg
	 */
	public void setHanei_flg(String hanei_flg) {
		this.hanei_flg = hanei_flg;
	}

	/**
	 * ユーザーマスタメンテナンス_登録へ渡す明細
	 * @return soushin_meisai
	 */
	public List<HashMap<String, String>> getSoushin_meisai() {
		return soushin_meisai;
	}

	/**
	 * ユーザーマスタメンテナンス_登録へ渡す明細
	 * @param soushin_meisai
	 */
	public void setSoushin_meisai(List<HashMap<String, String>> soushin_meisai) {
		this.soushin_meisai = soushin_meisai;
	}

	/**
	 * エラーチェック用のフラグ
	 * @return errChk
	 */
	public String getErrChkFlg() {
		return errChkFlg;
	}

	/**
	 * エラーチェック用のフラグ
	 * @param errChk
	 */
	public void setErrChkFlg(String errChkFlg) {
		this.errChkFlg = errChkFlg;
	}


}
