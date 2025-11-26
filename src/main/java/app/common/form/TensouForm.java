/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
003		2017/02/21		SSC				案件No.BJ201702058 転送機能の改善対応 
******************************************************************************/
package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * OZ3101_転送先選択 アクションフォームクラス
 * 
 */
public class TensouForm extends AppPagerActionForm {

    private String comment;					// 転送コメント
    private boolean bu_cd_upd_flg;			// 部コード更新判定フラグ(true:更新する、false:更新しない)
    private String hanyou1;					// 汎用１
    private LinkedHashMap ar_Hanyou1;			// 汎用１セレクトボックス用配列
    private String hanyou2;					// 汎用２
    private LinkedHashMap ar_Hanyou2;			// 汎用２セレクトボックス用配列
	private String txtTanto;	 				// 担当者アドレス
	private String inTanto;	 				// 入力/検索
	private String selectedTantoId;			// 担当者ID
    private String tanto;						// 担当者
    private List ar_Tanto;						// 担当者セレクトボックス用配列
    private String tanto_bumon_cd;				// 担当所属部門
    private String tanto_bu_cd;				// 担当所属部コード
    private boolean hanyouHyoujiFlg;			// 汎用１～３表示フラグ(true:表示する、false:表示しない)
	
    // 変数初期化
    public TensouForm() {
    	super.gamenId = GS.OZ3101;
        this.comment = GS.EMPTY_CHARCTER;
        this.bu_cd_upd_flg = false;
        this.hanyou1 = GS.EMPTY_CHARCTER;
        this.ar_Hanyou1 = null;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.ar_Hanyou2 = null;
        this.txtTanto = GS.EMPTY_CHARCTER;
        this.inTanto = GS.EMPTY_CHARCTER;
        this.selectedTantoId = GS.EMPTY_CHARCTER;
        this.tanto = GS.EMPTY_CHARCTER;
        this.ar_Tanto = null;
        this.tanto_bumon_cd = GS.EMPTY_CHARCTER;
        this.tanto_bu_cd = GS.EMPTY_CHARCTER;
        this.hanyouHyoujiFlg = true;
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    // アクセスメソッド

    //転送コメント
    public String getComment() {
        return Function.trim(this.comment);
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    //部コード更新判定フラグ
	public boolean isBu_cd_upd_flg() {
		return bu_cd_upd_flg;
	}
	public void setBu_cd_upd_flg(boolean bu_cd_upd_flg) {
		this.bu_cd_upd_flg = bu_cd_upd_flg;
	}
	//汎用１
	public String getHanyou1() {
		return hanyou1;
	}
	public void setHanyou1(String hanyou1) {
		this.hanyou1 = hanyou1;
	}
	//汎用１配列
	public LinkedHashMap getAr_Hanyou1() {
		return ar_Hanyou1;
	}
	public void setAr_Hanyou1(LinkedHashMap ar_Hanyou1) {
		this.ar_Hanyou1 = ar_Hanyou1;
	}
	//汎用２
	public String getHanyou2() {
		return hanyou2;
	}
	public void setHanyou2(String hanyou2) {
		this.hanyou2 = hanyou2;
	}
	//汎用２配列
	public LinkedHashMap getAr_Hanyou2() {
		return ar_Hanyou2;
	}
	public void setAr_Hanyou2(LinkedHashMap ar_Hanyou2) {
		this.ar_Hanyou2 = ar_Hanyou2;
	}
	//担当者アドレス
	public String getTxtTanto() {
		return txtTanto;
	}
	public void setTxtTanto(String txtTanto) {
		this.txtTanto = txtTanto;
	}
	//入力/検索
	public String getInTanto() {
		return inTanto;
	}
	public void setInTanto(String inTanto) {
		this.inTanto = inTanto;
	}
	//担当者ID
	public String getSelectedTantoId() {
		return selectedTantoId;
	}
	public void setSelectedTantoId(String selectedTantoId) {
		this.selectedTantoId = selectedTantoId;
	}
	//担当者
	public String getTanto() {
		return tanto;
	}
	public void setTanto(String tanto) {
		this.tanto = tanto;
	}
	//担当者配列
	public List getAr_Tanto() {
		return ar_Tanto;
	}
	public void setAr_Tanto(List ar_Tanto) {
		this.ar_Tanto = ar_Tanto;
	}
	//担当所属部門
	public String getTanto_bumon_cd() {
		return tanto_bumon_cd;
	}
	public void setTanto_bumon_cd(String tanto_bumon_cd) {
		this.tanto_bumon_cd = tanto_bumon_cd;
	}
	//担当所属部コード
	public String getTanto_bu_cd() {
		return tanto_bu_cd;
	}
	public void setTanto_bu_cd(String tanto_bu_cd) {
		this.tanto_bu_cd = tanto_bu_cd;
	}

	//汎用１～３表示フラグ(true:表示する、false:表示しない)
	public boolean isHanyouHyoujiFlg() {
		return hanyouHyoujiFlg;
	}
	public void setHanyouHyoujiFlg(boolean hanyouHyoujiFlg) {
		this.hanyouHyoujiFlg = hanyouHyoujiFlg;
	}
}