/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
002		2015/03/18		SSC				案件No.BJ201408049 IA化対応時の機能改善 
******************************************************************************/

package app.common.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import common.util.Function;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * OZ2101_差戻先選択 アクションフォームクラス
 * 
 */
public class SashimodoshiForm extends AppPagerActionForm {

    private String gamen_mode;					// 画面モード（1:滞留フェーズ差戻、2:査定フェーズ差戻、3：引当金確認フェーズ差戻、4：事務局差戻、5：新規差戻先選択）   
    private boolean tairyu_sashi_flg;			// 滞留フェーズ差戻ラジオボタン表示判定用
    private boolean to_tairyu_sashi_flg;		// 滞留フェーズ差戻ラジオボタン表示判定用(査定から滞留)
    private boolean satei_sashi_flg;			// 査定フェーズ差戻ラジオボタン表示判定用
    private boolean hikiate_sashi_flg;		// 引当金確認フェーズ差戻ラジオボタン表示判定用
    private boolean jimu_sashi_flg;			// 事務局差戻ラジオボタン表示判定用
    private boolean jimuSashiKariFlg;			// 事務局差戻ラジオボタン表示判定用(仮基準日追加)
	private boolean sinki_sashi_flg;			// 新規差戻先選択ラジオボタン表示判定用
	private String jishi_phase;				/* 実施フェーズ	1:滞留フェーズ(滞留判定 OR 滞留判定検証)			*/
												/*			 	2:査定フェーズ(一次査定 OR 一次査定検証 OR 二次査定)*/
    private String sashi_comment;				// 差戻コメント
    private boolean hikiateFlg;				// 引当金開始FLG
    private boolean tairyuFlg;				// 滞留判定データ有無FLG
    private boolean bu_cd_upd_flg;			// 部コード更新判定フラグ(true:更新する、false:更新しない)
	private boolean satei_anken_no_upd_flg;	// 査定案件No更新判定フラグ(true:更新する、false:更新しない)
    private List<String> ar_tairyu_anken_no;	// 滞留判定案件No【リスト】
    private String sashikbn;					// 差戻区分
    private LinkedHashMap ar_Sashikbn;			// 差戻区分セレクトボックス用配列
    private String sashiphase;					// 差戻フェーズ
    private LinkedHashMap ar_Sashiphase;		// 差戻フェーズセレクトボックス用配列
    private String hanyou2;					// 汎用２
    private LinkedHashMap ar_Hanyou2;			// 汎用２セレクトボックス用配列
    private String hanyou3;					// 汎用３
    private LinkedHashMap ar_Hanyou3;			// 汎用３セレクトボックス用配列
	private String txtTanto;	 				// 担当者アドレス
	private String inTanto;	 				// 入力/検索
	private String selectedTantoId;			// 担当者ID
    private String tanto;						// 担当者
    private List ar_Tanto;						// 担当者セレクトボックス用配列
    private String tanto_bumon_cd;				// 担当所属部門
    private String tanto_bu_cd;				// 担当所属部コード
    private List<RirekiListBean> ar_rireki;	// 履歴情報【リスト】  
	private String dt_title;					// 処理日時タイトル
    private int id;							// 明細Listで選択された列番号
	private String sahimodoshiType;			/* 差戻種別  1:滞留フェーズ差戻(滞留フェーズ内)					*/
    											/*			 2:新規差戻先選択差戻(滞留フェーズ内)				*/
    											/*			 3:滞留フェーズ差戻(査定から滞留フェーズに差戻)		*/
    											/*			 4:新規差戻先選択差戻(査定から滞留フェーズに差戻)	*/
    											/*			 5:査定フェーズ差戻									*/
    											/*			 6:新規差戻先選択差戻(査定フェーズ内)				*/
    											/*			 7:事務局差戻										*/
    											/*			 8:引当金確認フェーズ差戻							*/
   												/*			 9:新規差戻先選択差戻(引当金検証/確認フェーズ内)　　*/
     
	
    // 変数初期化
    public SashimodoshiForm() {
    	super.gamenId = GS.OZ2101;
    	this.gamen_mode = GS.EMPTY_CHARCTER;
        this.sashi_comment = GS.EMPTY_CHARCTER;
        this.jishi_phase = GS.EMPTY_CHARCTER;
        this.tairyu_sashi_flg = false;
        this.to_tairyu_sashi_flg = false;
        this.satei_sashi_flg = false;
        this.hikiate_sashi_flg = false;
        this.jimu_sashi_flg = false;
        this.jimuSashiKariFlg = false;
        this.sinki_sashi_flg = false;
        this.hikiateFlg = false;
        this.tairyuFlg = false;
        this.bu_cd_upd_flg = false;
        this.satei_anken_no_upd_flg = false;
        this.ar_tairyu_anken_no = null;
        this.sashikbn = GS.EMPTY_CHARCTER;
        this.ar_Sashikbn = null;
        this.sashiphase = GS.EMPTY_CHARCTER;
        this.ar_Sashiphase = null;
        this.hanyou2 = GS.EMPTY_CHARCTER;
        this.ar_Hanyou2 = null;
        this.hanyou3 = GS.EMPTY_CHARCTER;
        this.ar_Hanyou3 = null;
        this.txtTanto = GS.EMPTY_CHARCTER;
        this.inTanto = GS.EMPTY_CHARCTER;
        this.selectedTantoId = GS.EMPTY_CHARCTER;
        this.tanto = GS.EMPTY_CHARCTER;
        this.ar_Tanto = null;
        this.tanto_bumon_cd = GS.EMPTY_CHARCTER;
        this.tanto_bu_cd = GS.EMPTY_CHARCTER;
        this.ar_rireki = null;
		this.dt_title = GS.EMPTY_CHARCTER;
		this.id = -1;
		this.sahimodoshiType = GS.EMPTY_CHARCTER;
    }

	/**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}

    // アクセスメソッド

	//画面モード    
    public String getGamen_mode() {
        return this.gamen_mode;
    }
    public void setGamen_mode(String gamen_mode) {
        this.gamen_mode = gamen_mode;
    }

    //差戻コメント
    public String getSashi_comment() {
        return Function.trim(this.sashi_comment);
    }
    public void setSashi_comment(String sashi_comment) {
        this.sashi_comment = sashi_comment;
    }
    //実施フェーズ
    public String getJishi_phase() {
        return this.jishi_phase;
    }
    public void setJishi_phase(String jishi_phase) {
        this.jishi_phase = jishi_phase;
    }
	//滞留フェーズ差戻ラジオボタン表示判定用
	public boolean isTairyu_sashi_flg() {
		return tairyu_sashi_flg;
	}
	public void setTairyu_sashi_flg(boolean tairyu_sashi_flg) {
		this.tairyu_sashi_flg = tairyu_sashi_flg;
	}
	//滞留フェーズ差戻ラジオボタン表示判定用(査定から滞留)
	public boolean isTo_tairyu_sashi_flg() {
		return to_tairyu_sashi_flg;
	}
	public void setTo_tairyu_sashi_flg(boolean to_tairyu_sashi_flg) {
		this.to_tairyu_sashi_flg = to_tairyu_sashi_flg;
	}
	//査定フェーズ差戻ラジオボタン表示判定用
	public boolean isSatei_sashi_flg() {
		return satei_sashi_flg;
	}
	public void setSatei_sashi_flg(boolean satei_sashi_flg) {
		this.satei_sashi_flg = satei_sashi_flg;
	}
	//引当金確認フェーズ差戻ラジオボタン表示判定用
	public boolean isHikiate_sashi_flg() {
		return hikiate_sashi_flg;
	}
	public void setHikiate_sashi_flg(boolean hikiate_sashi_flg) {
		this.hikiate_sashi_flg = hikiate_sashi_flg;
	}
	//事務局差戻ラジオボタン表示判定用
	public boolean isJimu_sashi_flg() {
		return jimu_sashi_flg;
	}
	public void setJimu_sashi_flg(boolean jimu_sashi_flg) {
		this.jimu_sashi_flg = jimu_sashi_flg;
	}
	//事務局差戻ラジオボタン表示判定用(仮基準日追加)
    public boolean isJimuSashiKariFlg() {
		return jimuSashiKariFlg;
	}
	public void setJimuSashiKariFlg(boolean jimuSashiKariFlg) {
		this.jimuSashiKariFlg = jimuSashiKariFlg;
	}
	//新規差戻先選択ラジオボタン表示判定用
	public boolean isSinki_sashi_flg() {
		return sinki_sashi_flg;
	}
	public void setSinki_sashi_flg(boolean sinki_sashi_flg) {
		this.sinki_sashi_flg = sinki_sashi_flg;
	}    
	//引当金開始FLG
	public boolean isHikiateFlg() {
		return hikiateFlg;
	}
	public void setHikiateFlg(boolean hikiateFlg) {
		this.hikiateFlg = hikiateFlg;
	}
    //滞留判定データ有無FLG
	public boolean isTairyuFlg() {
		return tairyuFlg;
	}
	public void setTairyuFlg(boolean tairyuFlg) {
		this.tairyuFlg = tairyuFlg;
	}
    //部コード更新判定フラグ
	public boolean isBu_cd_upd_flg() {
		return bu_cd_upd_flg;
	}
	public void setBu_cd_upd_flg(boolean bu_cd_upd_flg) {
		this.bu_cd_upd_flg = bu_cd_upd_flg;
	}
    //査定案件No更新判定フラグ
	public boolean isSatei_anken_no_upd_flg() {
		return satei_anken_no_upd_flg;
	}
	public void setSatei_anken_no_upd_flg(boolean satei_anken_no_upd_flg) {
		this.satei_anken_no_upd_flg = satei_anken_no_upd_flg;
	}
	//滞留判定案件No【リスト】
	public List<String> getAr_tairyu_anken_no() {
		return ar_tairyu_anken_no;
	}
	public void setAr_tairyu_anken_no(List<String> ar_tairyu_anken_no) {
		this.ar_tairyu_anken_no = ar_tairyu_anken_no;
	}
	//差戻区分
	public String getSashikbn() {
		return sashikbn;
	}
	public void setSashikbn(String sashikbn) {
		this.sashikbn = sashikbn;
	}
	//差戻区分配列
	public LinkedHashMap getAr_Sashikbn() {
		return ar_Sashikbn;
	}
	public void setAr_Sashikbn(LinkedHashMap ar_Sashikbn) {
		this.ar_Sashikbn = ar_Sashikbn;
	}
	//差戻フェーズ
	public String getSashiphase() {
		return sashiphase;
	}
	public void setSashiphase(String sashiphase) {
		this.sashiphase = sashiphase;
	}	
	//差戻フェーズ配列
	public LinkedHashMap getAr_Sashiphase() {
		return ar_Sashiphase;
	}
	public void setAr_Sashiphase(LinkedHashMap ar_Sashiphase) {
		this.ar_Sashiphase = ar_Sashiphase;
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
	//汎用３
	public String getHanyou3() {
		return hanyou3;
	}
	public void setHanyou3(String hanyou3) {
		this.hanyou3 = hanyou3;
	}
	//汎用３配列
	public LinkedHashMap getAr_Hanyou3() {
		return ar_Hanyou3;
	}
	public void setAr_Hanyou3(LinkedHashMap ar_Hanyou3) {
		this.ar_Hanyou3 = ar_Hanyou3;
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
	//履歴情報【リスト】
	public List<RirekiListBean> getAr_rireki() {
		return ar_rireki;
	}
	public void setAr_rireki(List<RirekiListBean> ar_rireki) {
		this.ar_rireki = ar_rireki;
	}
	//処理日時タイトル
	public String getDt_title() {
		return dt_title;
	}
	public void setDt_title(String dt_title) {
		this.dt_title = dt_title;
	}
	//id
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	//差戻種別
	public String getSahimodoshiType() {
		return sahimodoshiType;
	}
	public void setSahimodoshiType(String sahimodoshiType) {
		this.sahimodoshiType = sahimodoshiType;
	}
}