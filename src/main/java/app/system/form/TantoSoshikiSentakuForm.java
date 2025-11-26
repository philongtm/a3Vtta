/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.7.0.67
更新履歴
No		日付			修正者			修正内容
001		2016/03/28		SSC				新規作成 
******************************************************************************/
package app.system.form;


import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;



/**
 * OS7116_担当組織選択 アクションフォームクラス <br>
 *
 */
public class TantoSoshikiSentakuForm extends AppPagerActionForm {

	private static final long serialVersionUID = 1L; 

	private List<HashMap<String, String>> ar_taiUserTantoList ;				// 対象ユーザ 担当先組織リスト
	private ArrayList<String> ar_logUserBunrui1List;						// ログインユーザ 統轄分類1リスト
	private List<HashMap<String, String>> ar_kensakuKekkaList ;				// 検索結果
	private LinkedHashMap<String, String> ar_hanyou1;						// セレクトボックス 汎用1
	private LinkedHashMap<String, String> ar_hanyou2;						// セレクトボックス 汎用2
	private String systemKbn;												// 検索条件 システム区分
	private String hanyou1;													// 検索条件 汎用1
	private String hanyou2;													// 検索条件 汎用2

	private String hanyou3LabelFlg;											// 汎用4ラベル表示フラグ 表示：1、非表示：0
	private String tanto_chk;												// チェック情報
	private String hanei_flg;												// 反映フラグ
	private String errChkFlg;												// エラーチェック用のフラグ
	private List<TantouBean> ar_sentakuHaneiList;							// 共通セッション格納用 

	/**
	 * 初期化処理を行う。
	 */
	public void initialize() {
		this.ar_taiUserTantoList = null;
		this.ar_logUserBunrui1List = null;
		this.ar_kensakuKekkaList = null;
		this.ar_hanyou1 = null;
		this.ar_hanyou2 = null;
		this.systemKbn = GS.EMPTY_CHARCTER;
		this.hanyou1 = GS.EMPTY_CHARCTER;
		this.hanyou2 = GS.EMPTY_CHARCTER;
		this.hanyou3LabelFlg = "0";
		this.tanto_chk = GS.EMPTY_CHARCTER;
		this.hanei_flg = "0";
		this.errChkFlg = "0";
		this.ar_sentakuHaneiList = null;

		this.setPager(new ArrayList<List>());
		this.setAr_meisai(new ArrayList<List>());
	}





	@Override
	/**
	 * 画面ID<br>
	 * @return 画面ID
	 */
	public String toString(){
		return super.gamenId;
	}

	/**
	 * 対象ユーザ 担当先組織リスト
	 * @return ar_taiUserTantoList
	 */
	public List<HashMap<String, String>> getAr_taiUserTantoList() {
		return ar_taiUserTantoList;
	}

	/**
	 * 対象ユーザ 担当先組織リスト
	 * @param ar_taiUserTantoList セットする ar_taiUserTantoList
	 */
	public void setAr_taiUserTantoList(List<HashMap<String, String>> ar_taiUserTantoList) {
		this.ar_taiUserTantoList = ar_taiUserTantoList;
	}

	/**
	 * ログインユーザ 統轄分類1リスト
	 * @return ar_logUserBunrui1List
	 */
	public ArrayList<String> getAr_logUserBunrui1List() {
		return ar_logUserBunrui1List;
	}

	/**
	 * ログインユーザ 統轄分類1リスト
	 * @param ar_logUserBunrui1List セットする ar_logUserBunrui1List
	 */
	public void setAr_logUserBunrui1List(ArrayList<String> ar_logUserBunrui1List) {
		this.ar_logUserBunrui1List = ar_logUserBunrui1List;
	}

	/**
	 * 検索結果
	 * @return ar_kensakuKekkaList
	 */
	public List<HashMap<String, String>> getAr_kensakuKekkaList() {
		return ar_kensakuKekkaList;
	}

	/**
	 * 検索結果
	 * @param ar_kensakuKekkaList セットする ar_kensakuKekkaList
	 */
	public void setAr_kensakuKekkaList(
			List<HashMap<String, String>> ar_kensakuKekkaList) {
		this.ar_kensakuKekkaList = ar_kensakuKekkaList;
	}

	/**
	 * セレクトボックス 汎用1
	 * @return ar_hanyou1
	 */
	public LinkedHashMap<String, String> getAr_hanyou1() {
		return ar_hanyou1;
	}

	/**
	 * セレクトボックス 汎用1
	 * @param ar_hanyou1 セットする ar_hanyou1
	 */
	public void setAr_hanyou1(LinkedHashMap<String, String> ar_hanyou1) {
		this.ar_hanyou1 = (LinkedHashMap<String, String>) ar_hanyou1;
	}

	/**
	 * セレクトボックス 汎用2
	 * @return ar_hanyou2
	 */
	public LinkedHashMap<String, String> getAr_hanyou2() {
		return ar_hanyou2;
	}

	/**
	 * セレクトボックス 汎用2
	 * @param ar_hanyou2 セットする ar_hanyou2
	 */
	public void setAr_hanyou2(LinkedHashMap<String, String> ar_hanyou2) {
		this.ar_hanyou2 = ar_hanyou2;
	}

	/**
	 * 検索条件 システム区分
	 * @return systemKbn
	 */
	public String getSystemKbn() {
		return systemKbn;
	}

	/**
	 * 検索条件 システム区分
	 * @param systemKbn セットする systemKbn
	 */
	public void setSystemKbn(String systemKbn) {
		this.systemKbn = systemKbn;
	}

	/**
	 * 検索条件 汎用1
	 * @return hanyou1
	 */
	public String getHanyou1() {
		return hanyou1;
	}

	/**
	 * 検索条件 汎用1
	 * @param hanyou1 セットする hanyou1
	 */
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}

	/**
	 * 検索条件 汎用2
	 * @return hanyou2
	 */
	public String getHanyou2() {
		return hanyou2;
	}

	/**
	 * 検索条件 汎用2
	 * @param hanyou2 セットする hanyou2
	 */
	public void setHanyou2(String hanyou2) {
		this.hanyou2 = hanyou2;
	}

	/**
	 * 汎用3ラベル表示フラグ 表示：1、非表示：0
	 * @return hanyou4LabelFlg
	 */
	public String getHanyou3LabelFlg() {
		return hanyou3LabelFlg;
	}

	/**
	 * 汎用3ラベル表示フラグ 表示：1、非表示：0
	 * @param hanyou4LabelFlg セットする hanyou4LabelFlg
	 */
	public void setHanyou3LabelFlg(String hanyou3LabelFlg) {
		this.hanyou3LabelFlg = hanyou3LabelFlg;
	}

	/**
	 * チェック情報
	 * @return soushin_chk
	 */
	public String getTanto_chk() {
		return tanto_chk;
	}

	/**
	 * チェック情報
	 * @param soushin_chk セットする soushin_chk
	 */
	public void setTanto_chk(String tanto_chk) {
		this.tanto_chk = tanto_chk;
	}

	/**
	 * 反映フラグ
	 * @return hanei_flg
	 */
	public String getHanei_flg() {
		return hanei_flg;
	}

	/**
	 * 反映フラグ
	 * @param hanei_flg セットする hanei_flg
	 */
	public void setHanei_flg(String hanei_flg) {
		this.hanei_flg = hanei_flg;
	}

	/**
	 * エラーチェック用のフラグ
	 * @return errChkFlg
	 */
	public String getErrChkFlg() {
		return errChkFlg;
	}

	/**
	 * エラーチェック用のフラグ
	 * @param errChkFlg セットする errChkFlg
	 */
	public void setErrChkFlg(String errChkFlg) {
		this.errChkFlg = errChkFlg;
	}

	/**
	 * 共通セッション格納用
	 * @return sentakuHaneiList
	 */
	public List<TantouBean> getAr_sentakuHaneiList() {
		return ar_sentakuHaneiList;
	}

	/**
	 * 共通セッション格納用
	 * @param sentakuHaneiList セットする sentakuHaneiList
	 */
	public void setAr_sentakuHaneiList(List<TantouBean> ar_sentakuHaneiList) {
		this.ar_sentakuHaneiList = ar_sentakuHaneiList;
	}

}
