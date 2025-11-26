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

/**
 *  OZ6109_査定進捗照会タブ アクションフォームクラス <br>
 */
public class SateiSincyokuForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; 	// serialVersionUID
	private String syouri_dt_t;							// 処理日時タイトル
	private String anken_no_ser;	    					// 検索用案件No.

    private int id;										//リンククリックされたid
	private String phase;									// フェーズ
	private String hanyou1;								// 汎用１
	private String soshiki_nm;								// 組織
	private String tanto_nm;								// 担当者
	private String syouri;									// 処理
	private String syouri_dt;								// 処理日時
	private String anken_no;								// 案件No.
    private String ope_kbn;		        				// 入力区分
    private String toroku_point;    						// 登録箇所
    private String comment_val;            				// コメント内容
    private List ar_anken_no;								// 査定案件Noリスト
    
	public SateiSincyokuForm() {
		
		super.gamenId = GS.OZ6109;
		this.id = 0;
		this.syouri_dt_t = GS.EMPTY_CHARCTER;
		this.anken_no_ser = GS.EMPTY_CHARCTER;
		this.ar_anken_no = null;
		this.phase = GS.EMPTY_CHARCTER;
		this.hanyou1 = GS.EMPTY_CHARCTER;
		this.soshiki_nm = GS.EMPTY_CHARCTER;
		this.tanto_nm = GS.EMPTY_CHARCTER;
		this.syouri = GS.EMPTY_CHARCTER;
		this.syouri_dt = GS.EMPTY_CHARCTER;
		this.anken_no = GS.EMPTY_CHARCTER;
		this.ope_kbn = GS.EMPTY_CHARCTER;
		this.toroku_point = GS.EMPTY_CHARCTER;
		this.comment_val = GS.EMPTY_CHARCTER;
	}
	
	public String toString() {
		return super.gamenId;
	}

	/**
	 * @return the anken_no
	 */
	public String getAnken_no() {
		return anken_no;
	}

	/**
	 * @param anken_no the anken_no to set
	 */
	public void setAnken_no(String anken_no) {
		this.anken_no = anken_no;
	}

	/**
	 * @return the anken_no_ser
	 */
	public String getAnken_no_ser() {
		return anken_no_ser;
	}

	/**
	 * @param anken_no_ser the anken_no_ser to set
	 */
	public void setAnken_no_ser(String anken_no_ser) {
		this.anken_no_ser = anken_no_ser;
	}

	/**
	 * @return the comment_val
	 */
	public String getComment_val() {
		return comment_val;
	}

	/**
	 * @param comment_val the comment_val to set
	 */
	public void setComment_val(String comment_val) {
		this.comment_val = comment_val;
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
	 * @return the ope_kbn
	 */
	public String getOpe_kbn() {
		return ope_kbn;
	}

	/**
	 * @param ope_kbn the ope_kbn to set
	 */
	public void setOpe_kbn(String ope_kbn) {
		this.ope_kbn = ope_kbn;
	}

	/**
	 * @return the phase
	 */
	public String getPhase() {
		return phase;
	}

	/**
	 * @param phase the phase to set
	 */
	public void setPhase(String phase) {
		this.phase = phase;
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
	 * @return the syouri
	 */
	public String getSyouri() {
		return syouri;
	}

	/**
	 * @param syouri the syouri to set
	 */
	public void setSyouri(String syouri) {
		this.syouri = syouri;
	}

	/**
	 * @return the syouri_dt
	 */
	public String getSyouri_dt() {
		return syouri_dt;
	}

	/**
	 * @param syouri_dt the syouri_dt to set
	 */
	public void setSyouri_dt(String syouri_dt) {
		this.syouri_dt = syouri_dt;
	}

	/**
	 * @return the syouri_dt_t
	 */
	public String getSyouri_dt_t() {
		return syouri_dt_t;
	}

	/**
	 * @param syouri_dt_t the syouri_dt_t to set
	 */
	public void setSyouri_dt_t(String syouri_dt_t) {
		this.syouri_dt_t = syouri_dt_t;
	}

	/**
	 * @return the tanto_nm
	 */
	public String getTanto_nm() {
		return tanto_nm;
	}

	/**
	 * @param tanto_nm the tanto_nm to set
	 */
	public void setTanto_nm(String tanto_nm) {
		this.tanto_nm = tanto_nm;
	}

	/**
	 * @return the toroku_point
	 */
	public String getToroku_point() {
		return toroku_point;
	}

	/**
	 * @param toroku_point the toroku_point to set
	 */
	public void setToroku_point(String toroku_point) {
		this.toroku_point = toroku_point;
	}

	/**
	 * @return the ar_anken_no
	 */
	public List getAr_anken_no() {
		return ar_anken_no;
	}

	/**
	 * @param ar_anken_no the ar_anken_no to set
	 */
	public void setAr_anken_no(List ar_anken_no) {
		this.ar_anken_no = ar_anken_no;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
