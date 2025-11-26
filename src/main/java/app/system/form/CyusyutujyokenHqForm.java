/************************************************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/5/19		SSC				1.5次版機能組込
************************************************************************************************************/
package app.system.form;

import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
//追加完了

/**
 * 抽出条件メンテナンス画面Formクラス
 */
public class CyusyutujyokenHqForm extends AppPagerActionForm {

	//要件No.四-13　
	//追加開始
	private String kessanki_kbn;					//1:第2/4四半期、2:第1/3四半期
	private LinkedHashMap kessanki_kbn_list;		//決算期区分設定値格納マップ
	private String kakoKtkFlg;						//過去格付フラグ
	private String kakoKtkFrom;					//過去格付From
	private String kakoKtkTo;						//過去格付To
	private String kakoKtkSansyo;					//過去格付参照時点
	private String genzaiKoteiSaikengakuJyogen;	//現在固定化営業債権額上限
	private String genzaiKoteiSaikengakuKagen;		//現在固定化営業債権額下限
	private String kakoKoteiSaikengakuJyogen;		//過去固定化営業債権額上限
	private String kakoKoteiSaikengakuKagen;		//過去固定化営業債権額下限
	private String koteiSaikengakuSansyo;			//固定化営業債権額参照時点
	private String flgsakiFlg;						//フラグ先フラグ
	//追加完了

    private LinkedHashMap ar_system_kbn;	// セレクトボックス用システム区分配列
    private LinkedHashMap ar_syori_kbn;	// セレクトボックス用処理区分配列
    private LinkedHashMap ar_tairyu_jdg;	// セレクトボックス用滞留条件配列
    private LinkedHashMap ar_satei_kaisya_cd;	// セレクトボックス用査定会社配列
    private LinkedHashMap ar_mise_cd;	// セレクトボックス用店コード配列

    /** 格付セレクトボックス値 */
    private LinkedHashMap kakutukeList;
	
	private String systemKbn;		// システム区分(01:GSS、02:MTS、03:FOCUS)
	private String syoriKbn;		// 処理区分(01:仮基準月、02:中間月、03:中間月追加)
	private String satei_kaisya_cd;// 査定会社コード(1:SJ、2:PN)
	private String mise_cd;		// 店コード(NIC:NIC、PNC:PNC)

	
	private String type;			// 1:新規登録、2:更新、3:削除
	private String type_old_kentou;// 検討対象先条件タブの選択済の値(1:新規登録、2:更新、3:削除)
	private String type_old_satei;	// 査定対象先条件タブの選択済の値(1:新規登録、2:更新、3:削除)
	
	private String jokenKbn;		// 1:検討対象先条件、2:査定対象先条件
	private List kentouList;		// 検討対象先の一覧
	private List sateiList;		// 査定対象先の一覧

	private String no_kentou;		// 条件No(検討)
	private String jiyuu_cd;		// 抽出事由
	private String jiyuu_nm;		// 抽出事由名称
	private String kakuzuke;		// 格付
	private String tairyu_from;	// 滞留期間(開始)
	private String tairyu_to;		// 滞留期間(終了)
	private String tuuka_kentou;	// 通貨(検討)
	private String kingaku_kentou;	// 金額(検討)
	private String kentou_flg;			// 検討対象フラグ(null or "1")
	private String tairyu_flg;			// 滞留判定フラグ(null or "1")
	private String satei_flg;			// 査定対象フラグ(null or "1")
	private String saimutyouka_flg;	// 債務超過フラグ(null or "1")
	private String akaji_flg;			// 赤字フラグ(null or "1")
	private String riki_flg;			// リ企指フラグ(null or 1)
	private String[] saiken_data_flg;		// データ作成フラグ(債権フラグ)
	private String[] saiken_kentou_flg;	// 検討対象フラグ(債権フラグ)
	private String[] saiken_tairyu_flg;	// 滞留判定フラグ(債権フラグ)
	private String[] saiken_all_flg;		// 債権フラグ(ALL)
	private String[] no_kentou_flg;		// 条件No(債権フラグ)
	
	private String no_satei;			// 条件No(査定)
	private String syskbn_satei;		// システム区分(査定)
	private String tairyu_hantei;		// 滞留判定
	private String tairyu_hantei_nm;	// 滞留判定名称
	private String tuuka_satei;		// 通貨(査定)
	private String kingaku_satei;		// 金額(査定)
	
	private int selectIdx;				// リストの選択インデックス
	
    // No459, 2008/05/31, SJA平道, 画面再表示でフォーカスを保持するように修正
    private String focusId;			// 画面のフォーカス情報
    private String focusId_satei;		// 画面のフォーカス情報(査定)
    // No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加
    private String focusEvent;         // 画面でのイベント判別
    
    /**
     * コンストラクタ
     */
    public CyusyutujyokenHqForm() {

    	//要件No.四-13　追加抽出条件項目及び決算期区分セレクトボックス初期値設定
    	//追加開始    	
    	this.kessanki_kbn = "2";
    	this.kakoKtkFlg   = "0";
    	kakoKtkFrom   = GS.EMPTY_CHARCTER;
    	kakoKtkTo   = GS.EMPTY_CHARCTER;
    	kakoKtkSansyo   = GS.EMPTY_CHARCTER;
    	genzaiKoteiSaikengakuJyogen   = GS.EMPTY_CHARCTER;
    	genzaiKoteiSaikengakuKagen   = GS.EMPTY_CHARCTER;
    	kakoKoteiSaikengakuJyogen   = GS.EMPTY_CHARCTER;
    	kakoKoteiSaikengakuKagen   = GS.EMPTY_CHARCTER;
    	koteiSaikengakuSansyo   = GS.EMPTY_CHARCTER;
    	this.flgsakiFlg   = GS.EMPTY_CHARCTER;
    	//追加完了

    	this.ar_system_kbn = null;
    	this.ar_syori_kbn = null;
    	this.ar_tairyu_jdg = null;
    	
    	// 変数初期化
    	this.type = "2";
    	this.type_old_kentou = "2";
    	this.type_old_satei = "2";
    	this.jokenKbn = "1";
    	this.systemKbn = "00";
    	this.syoriKbn = "00";

    	this.kentouList = null;
    	this.sateiList = null;

    	this.no_kentou = null;
    	this.jiyuu_nm = null;
    	this.kakuzuke = "";
    	this.tairyu_from = null;
    	this.tairyu_to = null;
    	this.tuuka_kentou = null;
    	this.kingaku_kentou = null;
    	this.kentou_flg = null;
    	this.tairyu_flg = null;
    	this.satei_flg = null;
    	this.saimutyouka_flg = null;
    	this.akaji_flg = null;
    	this.riki_flg = null;
    	this.saiken_data_flg = new String[4];
    	this.saiken_kentou_flg = new String[4];
    	this.saiken_tairyu_flg = new String[4];
    	this.saiken_all_flg = new String[4];
    	this.no_kentou_flg = new String[4];

    	this.no_satei = null;
    	this.tairyu_hantei = "0";
    	this.tairyu_hantei_nm = null;
    	this.tuuka_satei = null;
    	this.kingaku_satei = null;
    	
    	this.selectIdx = -1;
    	
    	this.kakutukeList = null;
    	this.ar_satei_kaisya_cd = null;
    	this.ar_mise_cd = null;
    	this.satei_kaisya_cd=null;
    	this.mise_cd = null;

    	this.focusId = "";
    	this.focusId_satei = "";
    	
    	// No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加
    	this.focusEvent = "";
    }
    
    /**
     * @return 画面IDを戻します。
     */
    public String toString(){
        return super.gamenId;
    }
	/**
	 * @return mise_cd を戻します。
	 */
	public String getMise_cd() {
		return mise_cd;
	}
	/**
	 * @param mise_cd mise_cd を設定。
	 */
	public void setMise_cd(String mise_cd) {
		this.mise_cd = mise_cd;
	}
	/**
	 * @return satei_kaisya_cd を戻します。
	 */
	public String getSatei_kaisya_cd() {
		return satei_kaisya_cd;
	}
	/**
	 * @param satei_kaisya_cd satei_kaisya_cd を設定。
	 */
	public void setSatei_kaisya_cd(String satei_kaisya_cd) {
		this.satei_kaisya_cd = satei_kaisya_cd;
	}
	/**
	 * @return ar_mise_cd を戻します。
	 */
	public LinkedHashMap getAr_mise_cd() {
		return ar_mise_cd;
	}
	/**
	 * @param ar_mise_cd ar_mise_cd を設定。
	 */
	public void setAr_mise_cd(LinkedHashMap ar_mise_cd) {
		this.ar_mise_cd = ar_mise_cd;
	}
	/**
	 * @return ar_satei_kaisya_cd を戻します。
	 */
	public LinkedHashMap getAr_satei_kaisya_cd() {
		return ar_satei_kaisya_cd;
	}
	/**
	 * @param ar_satei_kaisya_cd ar_satei_kaisya_cd を設定。
	 */
	public void setAr_satei_kaisya_cd(LinkedHashMap ar_satei_kaisya_cd) {
		this.ar_satei_kaisya_cd = ar_satei_kaisya_cd;
	}
	public LinkedHashMap getKakutukeList() {
		return kakutukeList;
	}
	public void setKakutukeList(LinkedHashMap kakutukeList) {
		this.kakutukeList = kakutukeList;
	}
	/**
	 * @return ar_system_kbn を取得。
	 */
	public LinkedHashMap getAr_system_kbn() {
		return ar_system_kbn;
	}
	/**
	 * @param ar_system_kbn ar_system_kbn を設定。
	 */
	public void setAr_system_kbn(LinkedHashMap ar_system_kbn) {
		this.ar_system_kbn = ar_system_kbn;
	}
    
	/**
	 * @return ar_syoriKbn を取得。
	 */
	public LinkedHashMap getAr_syori_kbn() {
		return ar_syori_kbn;
	}
	/**
	 * @param ar_syoriKbn ar_syoriKbn を設定。
	 */
	public void setAr_syori_kbn(LinkedHashMap ar_syori_kbn) {
		this.ar_syori_kbn = ar_syori_kbn;
	}
	
	/**
	 * @return ar_tairyu_jdg を取得。
	 */
	public LinkedHashMap getAr_tairyu_jdg() {
		return ar_tairyu_jdg;
	}
	/**
	 * @param ar_tairyu_jdg ar_tairyu_jdg を設定。
	 */
	public void setAr_tairyu_jdg(LinkedHashMap ar_tairyu_jdg) {
		this.ar_tairyu_jdg = ar_tairyu_jdg;
	}
	
    /**
     * @return type を取得。
     */
    public String getType() {
        return this.type;
    }
    /**
     * @param type type を設定。
     */
    public void setType(String type) {
        this.type = type;
    }
    
	/**
	 * @return type_old_kentou を取得。
	 */
	public String getType_old_kentou() {
		return type_old_kentou;
	}
	/**
	 * @param type_old_kentou type_old_kentou を設定。
	 */
	public void setType_old_kentou(String type_old_kentou) {
		this.type_old_kentou = type_old_kentou;
	}
	
	/**
	 * @return type_old_satei を取得。
	 */
	public String getType_old_satei() {
		return type_old_satei;
	}
	/**
	 * @param type_old_satei type_old_satei を設定。
	 */
	public void setType_old_satei(String type_old_satei) {
		this.type_old_satei = type_old_satei;
	}
	
    /**
     * @return jokenKbn を取得。
     */
    public String getJokenKbn() {
        return this.jokenKbn;
    }
    /**
     * @param jokenKbn jokenKbn を設定。
     */
    public void setJokenKbn(String jokenKbn) {
        this.jokenKbn = jokenKbn;
    }
    
   /**
    * @return systemKbn を取得。
    */
   public String getSystemKbn() {
       return this.systemKbn;
   }
   /**
    * @param systemKbn systemKbn を設定。
    */
   public void setSystemKbn(String systemKbn) {
       this.systemKbn = systemKbn;
   }
   
   /**
    * @return syoriKbn を取得。
    */
   public String getSyoriKbn() {
       return this.syoriKbn;
   }
   /**
    * @param syoriKbn syoriKbn を設定。
    */
   public void setSyoriKbn(String syoriKbn) {
       this.syoriKbn = syoriKbn;
   }
   
   /**
    * @return kentouList を取得。
    */
   public List getKentouList() {
       return this.kentouList;
   }
   /**
    * @param kentouList kentouList を設定。
    */
   public void setKentouList(List kentouList) {
       this.kentouList = kentouList;
   }
   
   /**
    * @return sateiList を取得。
    */
   public List getSateiList() {
       return this.sateiList;
   }
   /**
    * @param sateiList sateiList を設定。
    */
   public void setSateiList(List sateiList) {
       this.sateiList = sateiList;
   }

	/**
	 * @return no_kentou を取得。
	 */
	public String getNo_kentou() {
		return no_kentou;
	}
	/**
	 * @param no_kentou no_kentou を設定。
	 */
	public void setNo_kentou(String no_kentou) {
		this.no_kentou = no_kentou;
	}
	
	/**
	 * @return jiyuu_cd を取得。
	 */
	public String getJiyuu_cd() {
		return jiyuu_cd;
	}
	/**
	 * @param jiyuu_cd jiyuu_cd を設定。
	 */
	public void setJiyuu_cd(String jiyuu_cd) {
		this.jiyuu_cd = jiyuu_cd;
	}
	
	/**
	 * @return jiyuu_nm を取得。
	 */
	public String getJiyuu_nm() {
		return jiyuu_nm;
	}
	/**
	 * @param jiyuu_nm jiyuu_nm を設定。
	 */
	public void setJiyuu_nm(String jiyuu_nm) {
		this.jiyuu_nm = jiyuu_nm;
	}
	
	/**
	 * @return kakuzuke を取得。
	 */
	public String getKakuzuke() {
		return kakuzuke;
	}
	/**
	 * @param kakuzuke kakuzuke を設定。
	 */
	public void setKakuzuke(String kakuzuke) {
		this.kakuzuke = kakuzuke;
	}
	
	/**
	 * @return tairyu_from を取得。
	 */
	public String getTairyu_from() {
		return tairyu_from;
	}
	/**
	 * @param tairyu_from tairyu_from を設定。
	 */
	public void setTairyu_from(String tairyu_from) {
		this.tairyu_from = tairyu_from;
	}
	
	/**
	 * @return tairyu_to を取得。
	 */
	public String getTairyu_to() {
		return tairyu_to;
	}
	/**
	 * @param tairyu_to tairyu_to を設定。
	 */
	public void setTairyu_to(String tairyu_to) {
		this.tairyu_to = tairyu_to;
	}
	
	/**
	 * @return tuuka_kentou を取得。
	 */
	public String getTuuka_kentou() {
		return tuuka_kentou;
	}
	/**
	 * @param tuuka_kentou tuuka_kentou を設定。
	 */
	public void setTuuka_kentou(String tuuka_kentou) {
		this.tuuka_kentou = tuuka_kentou;
	}
	
	/**
	 * @return kingaku_kentou を取得。
	 */
	public String getKingaku_kentou() {
		return kingaku_kentou;
	}
	/**
	 * @param kingaku_kentou kingaku_kentou を設定。
	 */
	public void setKingaku_kentou(String kingaku_kentou) {
		this.kingaku_kentou = kingaku_kentou;
	}
	
	/**
	 * @return kentou_flg を取得。
	 */
	public String getKentou_flg() {
		if (kentou_flg == null) {
			kentou_flg = "";
		}
		return kentou_flg;
	}
	/**
	 * @param kentou_flg kentou_flg を設定。
	 */
	public void setKentou_flg(String kentou_flg) {
		this.kentou_flg = kentou_flg;
	}
	
	/**
	 * @return tairyu_flg を取得。
	 */
	public String getTairyu_flg() {
		if (tairyu_flg == null) {
			tairyu_flg = "";
		}
		return tairyu_flg;
	}
	/**
	 * @param tairyu_flg tairyu_flg を設定。
	 */
	public void setTairyu_flg(String tairyu_flg) {
		this.tairyu_flg = tairyu_flg;
	}
	
	/**
	 * @return satei_flg を取得。
	 */
	public String getSatei_flg() {
		if (satei_flg == null) {
			satei_flg = "";
		}
		return satei_flg;
	}
	/**
	 * @param satei_flg satei_flg を設定。
	 */
	public void setSatei_flg(String satei_flg) {
		this.satei_flg = satei_flg;
	}
	
	/**
	 * @return saimutyouka_flg を取得。
	 */
	public String getSaimutyouka_flg() {
		if (saimutyouka_flg == null) {
			saimutyouka_flg = "";
		}
		return saimutyouka_flg;
	}
	/**
	 * @param saimutyouka_flg saimutyouka_flg を設定。
	 */
	public void setSaimutyouka_flg(String saimutyouka_flg) {
		this.saimutyouka_flg = saimutyouka_flg;
	}
	
	/**
	 * @return akaji_flg を取得。
	 */
	public String getAkaji_flg() {
		if (akaji_flg == null) {
			akaji_flg = "";
		}
		return akaji_flg;
	}
	/**
	 * @param akaji_flg akaji_flg を設定。
	 */
	public void setAkaji_flg(String akaji_flg) {
		this.akaji_flg = akaji_flg;
	}
	
	/**
	 * @return riki_flg を取得。
	 */
	public String getRiki_flg() {
		if (riki_flg == null) {
			riki_flg = "";
		}
		return riki_flg;
	}
	/**
	 * @param riki_flg riki_flg を設定。
	 */
	public void setRiki_flg(String riki_flg) {
		this.riki_flg = riki_flg;
	}
	
	/**
	 * @return saiken_data_flg を取得。
	 */
	public String[] getSaiken_data_flg() {
		return saiken_data_flg;
	}
	/**
	 * @param saiken_data_flg saiken_data_flg を設定。
	 */
	public void setSaiken_data_flg(String[] saiken_data_flg) {
		this.saiken_data_flg = saiken_data_flg;
	}
	
	/**
	 * @return saiken_kentou_flg を取得。
	 */
	public String[] getSaiken_kentou_flg() {
		return saiken_kentou_flg;
	}
	/**
	 * @param saiken_kentou_flg saiken_kentou_flg を設定。
	 */
	public void setSaiken_kentou_flg(String[] saiken_kentou_flg) {
		this.saiken_kentou_flg = saiken_kentou_flg;
	}
	
	/**
	 * @return saiken_all_flg を取得。
	 */
	public String[] getSaiken_all_flg() {
		return saiken_all_flg;
	}
	/**
	 * @param saiken_all_flg saiken_all_flg を設定。
	 */
	public void setSaiken_all_flg(String[] saiken_all_flg) {
		this.saiken_all_flg = saiken_all_flg;
	}
	
	/**
	 * @return no_kentou_flg を取得。
	 */
	public String[] getNo_kentou_flg() {
		return no_kentou_flg;
	}
	/**
	 * @param no_kentou_flg no_kentou_flg を設定。
	 */
	public void setNo_kentou_flg(String[] no_kentou_flg) {
		this.no_kentou_flg = no_kentou_flg;
	}
	
	/**
	 * @return saiken_tairyu_flg を取得。
	 */
	public String[] getSaiken_tairyu_flg() {
		return saiken_tairyu_flg;
	}
	/**
	 * @param saiken_tairyu_flg saiken_tairyu_flg を設定。
	 */
	public void setSaiken_tairyu_flg(String[] saiken_tairyu_flg) {
		this.saiken_tairyu_flg = saiken_tairyu_flg;
	}
	
	/**
	 * @return no_satei を取得。
	 */
	public String getNo_satei() {
		return no_satei;
	}
	/**
	 * @param no_satei no_satei を設定。
	 */
	public void setNo_satei(String no_satei) {
		this.no_satei = no_satei;
	}
	
	/**
	 * @return syskbn_satei を取得。
	 */
	public String getSyskbn_satei() {
		return syskbn_satei;
	}
	/**
	 * @param syskbn_satei syskbn_satei を設定。
	 */
	public void setSyskbn_satei(String syskbn_satei) {
		this.syskbn_satei = syskbn_satei;
	}
	
	/**
	 * @return tairyu_hantei を取得。
	 */
	public String getTairyu_hantei() {
		return tairyu_hantei;
	}
	/**
	 * @param tairyu_hantei tairyu_hantei を設定。
	 */
	public void setTairyu_hantei(String tairyu_hantei) {
		this.tairyu_hantei = tairyu_hantei;
	}
	
	/**
	 * @return tairyu_hantei_nm を取得。
	 */
	public String getTairyu_hantei_nm() {
		return tairyu_hantei_nm;
	}
	/**
	 * @param tairyu_hantei_nm tairyu_hantei_nm を設定。
	 */
	public void setTairyu_hantei_nm(String tairyu_hantei_nm) {
		this.tairyu_hantei_nm = tairyu_hantei_nm;
	}
	
	/**
	 * @return tuuka_satei を取得。
	 */
	public String getTuuka_satei() {
		return tuuka_satei;
	}
	/**
	 * @param tuuka_satei tuuka_satei を設定。
	 */
	public void setTuuka_satei(String tuuka_satei) {
		this.tuuka_satei = tuuka_satei;
	}
	
	/**
	 * @return kingaku_satei を取得。
	 */
	public String getKingaku_satei() {
		return kingaku_satei;
	}
	/**
	 * @param kingaku_satei kingaku_satei を設定。
	 */
	public void setKingaku_satei(String kingaku_satei) {
		this.kingaku_satei = kingaku_satei;
	}
	
	/**
	 * @return selectIdx を取得。
	 */
	public int getSelectIdx() {
		return selectIdx;
	}
	/**
	 * @param selectIdx selectIdx を設定。
	 */
	public void setSelectIdx(int selectIdx) {
		this.selectIdx = selectIdx;
	}

	/**
	 * @return focusId を戻します。
	 */
	public String getFocusId() {
		return focusId;
	}
	/**
	 * @param focusId focusId を設定。
	 */
	public void setFocusId(String focusId) {
		this.focusId = focusId;
	}
	
	/**
	 * @return focusId_satei を戻します。
	 */
	public String getFocusId_satei() {
		return focusId_satei;
	}
	/**
	 * @param focusId_satei focusId_satei を設定。
	 */
	public void setFocusId_satei(String focusId_satei) {
		this.focusId_satei = focusId_satei;
	}
	// No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 
	/**
	 * @return focusEvent を戻します。
	 */
	public String getFocusEvent() {
		return focusEvent;
	}
	/**
	 * @param focusEvent focusEvent を設定。
	 */
	public void setFocusEvent(String focusEvent) {
		this.focusEvent = focusEvent;
	}

	//要件No.四-13　追加抽出条件項目アクセッサメソッド
	//追加開始
	/**
	 * @return kessanki_kbn を戻します。
	 */
	public String getKessanki_kbn() {
		return kessanki_kbn;
	}
	/**
	 * @param kessanki_kbn kessanki_kbn を設定。
	 */
	public void setKessanki_kbn(String kessanki_kbn) {
		this.kessanki_kbn = kessanki_kbn;
	}
	/**
	 * @return kessanki_kbn_list を戻します。
	 */
	public LinkedHashMap getKessanki_kbn_list() {
		return kessanki_kbn_list;
	}
	/**
	 * @param kessanki_kbn_list kessanki_kbn_list を設定。
	 */
	public void setKessanki_kbn_list(LinkedHashMap kessanki_kbn_list) {
		this.kessanki_kbn_list = kessanki_kbn_list;
	}
	/**
	 * @return kakoKtkFlg を戻します。
	 */
	public String getKakoKtkFlg() {
		return kakoKtkFlg;
	}
	/**
	 * @param kakoKtkFlg kakoKtkFlg を設定。
	 */
	public void setKakoKtkFlg(String kakoKtkFlg) {
		this.kakoKtkFlg = kakoKtkFlg;
	}
	/**
	 * @return flgsakiFlg を戻します。
	 */
	public String getFlgsakiFlg() {
		return flgsakiFlg;
	}
	/**
	 * @param flgsakiFlg flgsakiFlg を設定。
	 */
	public void setFlgsakiFlg(String flgsakiFlg) {
		this.flgsakiFlg = flgsakiFlg;
	}
	/**
	 * @return genzaiKoteiSaikengakuJyogen を戻します。
	 */
	public String getGenzaiKoteiSaikengakuJyogen() {
		return genzaiKoteiSaikengakuJyogen;
	}
	/**
	 * @param genzaiKoteiSaikengakuJyogen genzaiKoteiSaikengakuJyogen を設定。
	 */
	public void setGenzaiKoteiSaikengakuJyogen(
			String genzaiKoteiSaikengakuJyogen) {
		this.genzaiKoteiSaikengakuJyogen = genzaiKoteiSaikengakuJyogen;
	}
	/**
	 * @return genzaiKoteiSaikengakuKagen を戻します。
	 */
	public String getGenzaiKoteiSaikengakuKagen() {
		return genzaiKoteiSaikengakuKagen;
	}
	/**
	 * @param genzaiKoteiSaikengakuKagen genzaiKoteiSaikengakuKagen を設定。
	 */
	public void setGenzaiKoteiSaikengakuKagen(String genzaiKoteiSaikengakuKagen) {
		this.genzaiKoteiSaikengakuKagen = genzaiKoteiSaikengakuKagen;
	}
	/**
	 * @return kakoKoteiSaikengakuJyogen を戻します。
	 */
	public String getKakoKoteiSaikengakuJyogen() {
		return kakoKoteiSaikengakuJyogen;
	}
	/**
	 * @param kakoKoteiSaikengakuJyogen kakoKoteiSaikengakuJyogen を設定。
	 */
	public void setKakoKoteiSaikengakuJyogen(String kakoKoteiSaikengakuJyogen) {
		this.kakoKoteiSaikengakuJyogen = kakoKoteiSaikengakuJyogen;
	}
	/**
	 * @return kakoKoteiSaikengakuKagen を戻します。
	 */
	public String getKakoKoteiSaikengakuKagen() {
		return kakoKoteiSaikengakuKagen;
	}
	/**
	 * @param kakoKoteiSaikengakuKagen kakoKoteiSaikengakuKagen を設定。
	 */
	public void setKakoKoteiSaikengakuKagen(String kakoKoteiSaikengakuKagen) {
		this.kakoKoteiSaikengakuKagen = kakoKoteiSaikengakuKagen;
	}
	/**
	 * @return kakoKtkFrom を戻します。
	 */
	public String getKakoKtkFrom() {
		return kakoKtkFrom;
	}
	/**
	 * @param kakoKtkFrom kakoKtkFrom を設定。
	 */
	public void setKakoKtkFrom(String kakoKtkFrom) {
		this.kakoKtkFrom = kakoKtkFrom;
	}
	/**
	 * @return kakoKtkSansyo を戻します。
	 */
	public String getKakoKtkSansyo() {
		return kakoKtkSansyo;
	}
	/**
	 * @param kakoKtkSansyo kakoKtkSansyo を設定。
	 */
	public void setKakoKtkSansyo(String kakoKtkSansyo) {
		this.kakoKtkSansyo = kakoKtkSansyo;
	}
	/**
	 * @return kakoKtkTo を戻します。
	 */
	public String getKakoKtkTo() {
		return kakoKtkTo;
	}
	/**
	 * @param kakoKtkTo kakoKtkTo を設定。
	 */
	public void setKakoKtkTo(String kakoKtkTo) {
		this.kakoKtkTo = kakoKtkTo;
	}
	/**
	 * @return koteiSaikengakuSansyo を戻します。
	 */
	public String getKoteiSaikengakuSansyo() {
		return koteiSaikengakuSansyo;
	}
	/**
	 * @param koteiSaikengakuSansyo koteiSaikengakuSansyo を設定。
	 */
	public void setKoteiSaikengakuSansyo(String koteiSaikengakuSansyo) {
		this.koteiSaikengakuSansyo = koteiSaikengakuSansyo;
	}
	//追加完了

	// No795, 2008/06/07, SJA渡辺, 債権フラグ設定のリセットメソッド作成
	public void reset(ActionMapping mapping, HttpServletRequest request){
		setSaiken_data_flg(new String[4]);
		setSaiken_kentou_flg(new String[4]);
		setSaiken_tairyu_flg(new String[4]);
		//要件No.四-13
		//追加開始
		setKakoKtkFlg("0");
		setFlgsakiFlg(GS.EMPTY_CHARCTER);
		//追加完了
	}
}