/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;

import java.util.List;
import java.util.Map;

/**
 *  OZ6110_実質滞留債権判定進捗照会タブ アクションフォームクラス <br>
 */
public class TairyuSincyokuForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; 	// serialVersionUID
	
	private List<Map<String, String>> ar_sosiki;			// 組織一覧
	private List<Map<String, String>> ar_sinchoku;			// 進捗一覧
	private int id_sosiki;									// リンクされた組織ID
	private int id_sinchoku;								// リンクされた進捗ID
	private String syori_dt_til;							// 処理日時タイトル
	private String srh_anken_no;							// 検索用案件No.
	private String torimodoshi_fuka_flg;
	private String upd_user_id_flg;
	/**
	 * 変数初期化 <br>
	 */
	public TairyuSincyokuForm() {
		super.gamenId = GS.OZ6110;
		this.ar_sosiki = null;
		this.ar_sinchoku = null;
		this.id_sosiki = 0;
		this.id_sinchoku = 0;
		this.torimodoshi_fuka_flg = GS.EMPTY_CHARCTER;
		this.upd_user_id_flg = GS.EMPTY_CHARCTER;
		this.syori_dt_til = GS.EMPTY_CHARCTER;
		this.srh_anken_no = GS.EMPTY_CHARCTER;
	}
	
	public String toString() {
		return super.gamenId;
	}

	/**
	 * @return the ar_sinchoku
	 */
	public List<Map<String, String>> getAr_sinchoku() {
		return ar_sinchoku;
	}

	/**
	 * @param ar_sinchoku the ar_sinchoku to set
	 */
	public void setAr_sinchoku(List<Map<String, String>> ar_sinchoku) {
		this.ar_sinchoku = ar_sinchoku;
	}

	/**
	 * @return the ar_sosiki
	 */
	public List<Map<String, String>> getAr_sosiki() {
		return ar_sosiki;
	}

	/**
	 * @param ar_sosiki the ar_sosiki to set
	 */
	public void setAr_sosiki(List<Map<String, String>> ar_sosiki) {
		this.ar_sosiki = ar_sosiki;
	}

	/**
	 * @return the id_sinchoku
	 */
	public int getId_sinchoku() {
		return id_sinchoku;
	}

	/**
	 * @param id_sinchoku the id_sinchoku to set
	 */
	public void setId_sinchoku(int id_sinchoku) {
		this.id_sinchoku = id_sinchoku;
	}

	/**
	 * @return the id_sosiki
	 */
	public int getId_sosiki() {
		return id_sosiki;
	}

	/**
	 * @param id_sosiki the id_sosiki to set
	 */
	public void setId_sosiki(int id_sosiki) {
		this.id_sosiki = id_sosiki;
	}

	/**
	 * @return the syori_dt_til
	 */
	public String getSyori_dt_til() {
		return syori_dt_til;
	}

	/**
	 * @param syori_dt_til the syori_dt_til to set
	 */
	public void setSyori_dt_til(String syori_dt_til) {
		this.syori_dt_til = syori_dt_til;
	}

	/**
	 * @return the srh_anken_no
	 */
	public String getSrh_anken_no() {
		return srh_anken_no;
	}

	/**
	 * @param srh_anken_no the srh_anken_no to set
	 */
	public void setSrh_anken_no(String srh_anken_no) {
		this.srh_anken_no = srh_anken_no;
	}
	public String getTorimodoshi_fuka_flg() {
		return torimodoshi_fuka_flg;
	}
	public void setTorimodoshi_fuka_flg(String torimodoshi_fuka_flg) {
		this.torimodoshi_fuka_flg = torimodoshi_fuka_flg;
	}
	public String getUpd_user_id_flg() {
		return upd_user_id_flg;
	}
	public void setUpd_user_id_flg(String upd_user_id_flg) {
		this.upd_user_id_flg = upd_user_id_flg;
	}
}
