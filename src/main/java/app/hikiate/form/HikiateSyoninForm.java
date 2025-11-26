/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/

package app.hikiate.form;

import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;

/**
 *  OD1103 引当金確認_承認一覧 アクションフォームクラス<br>
 */
public class HikiateSyoninForm extends AppPagerActionForm {
	
	private static final long serialVersionUID = 1L; 	// serialVersionUID
	private boolean check_box_all;		// 一括承認チェックボックス
	private int id;						// 勘定先情報のid
	private String kanjo_cd;				// 勘定先情報.勘定先CD
	private String kanjo_nm;				// 勘定先情報.勘定先名称
	private String torihikisaki_kbn;		// 取引先区分名称
	private String saiken_kbn;				// 債権区分名称
	private String sateikaisya_cd;			// 汎用１
	private String soshiki_nm;				// 組織
	private String kingaku;				// 追加引当金額(金額)
	private String tuuka_cd;				// 追加引当金額(通貨コード)
	private String tanto_nm;				// 担当者
	private String taisyo_ym;				// 対象年月
    private String sort_item;               // ソート項目
    private LinkedHashMap ar_sort_item;     // ソート項目セレクトボックス用配列
    private String sort_order;              // 整列方向
    private LinkedHashMap ar_sort_order;    // 整列方向セレクトボックス用配列
    private LinkedHashMap ar_show;          // 表示件数セレクトボックス用配列

	public HikiateSyoninForm() {
		
		super.gamenId = GS.OD1103;
		this.check_box_all = false;
		this.id = 0;
		this.kanjo_cd = GS.EMPTY_CHARCTER;
		this.kanjo_nm = GS.EMPTY_CHARCTER;
		this.torihikisaki_kbn = GS.EMPTY_CHARCTER;
		this.saiken_kbn = GS.EMPTY_CHARCTER;
		this.sateikaisya_cd = GS.EMPTY_CHARCTER;
		this.soshiki_nm = GS.EMPTY_CHARCTER;
		this.kingaku = GS.EMPTY_CHARCTER;
		this.tuuka_cd = GS.EMPTY_CHARCTER;
		this.tanto_nm = GS.EMPTY_CHARCTER;
		this.taisyo_ym = GS.EMPTY_CHARCTER;
	}
	
	public String toString() {
		return super.gamenId;
	}
	public boolean isCheck_box_all() {
		return check_box_all;
	}

	public void setCheck_box_all(boolean check_box_all) {
		this.check_box_all = check_box_all;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKanjo_cd() {
		return kanjo_cd;
	}

	public void setKanjo_cd(String kanjo_cd) {
		this.kanjo_cd = kanjo_cd;
	}

	public String getKanjo_nm() {
		return kanjo_nm;
	}

	public void setKanjo_nm(String kanjo_nm) {
		this.kanjo_nm = kanjo_nm;
	}

	public String getKingaku() {
		return kingaku;
	}

	public void setKingaku(String kingaku) {
		this.kingaku = kingaku;
	}

	public String getSaiken_kbn() {
		return saiken_kbn;
	}

	public void setSaiken_kbn(String saiken_kbn) {
		this.saiken_kbn = saiken_kbn;
	}

	public String getSateikaisya_cd() {
		return sateikaisya_cd;
	}

	public void setSateikaisya_cd(String sateikaisya_cd) {
		this.sateikaisya_cd = sateikaisya_cd;
	}

	public String getSoshiki_nm() {
		return soshiki_nm;
	}

	public void setSoshiki_nm(String soshiki_nm) {
		this.soshiki_nm = soshiki_nm;
	}

	public String getTaisyo_ym() {
		return taisyo_ym;
	}

	public void setTaisyo_ym(String taisyo_ym) {
		this.taisyo_ym = taisyo_ym;
	}

	public String getTanto_nm() {
		return tanto_nm;
	}

	public void setTanto_nm(String tanto_nm) {
		this.tanto_nm = tanto_nm;
	}

	public String getTorihikisaki_kbn() {
		return torihikisaki_kbn;
	}

	public void setTorihikisaki_kbn(String torihikisaki_kbn) {
		this.torihikisaki_kbn = torihikisaki_kbn;
	}

	public String getTuuka_cd() {
		return tuuka_cd;
	}

	public void setTuuka_cd(String tuuka_cd) {
		this.tuuka_cd = tuuka_cd;
	}
	
	
	public LinkedHashMap getAr_show() {
		return ar_show;
	}

	public void setAr_show(LinkedHashMap ar_show) {
		this.ar_show = ar_show;
	}

	public LinkedHashMap getAr_sort_item() {
		return ar_sort_item;
	}

	public void setAr_sort_item(LinkedHashMap ar_sort_item) {
		this.ar_sort_item = ar_sort_item;
	}

	public LinkedHashMap getAr_sort_order() {
		return ar_sort_order;
	}

	public void setAr_sort_order(LinkedHashMap ar_sort_order) {
		this.ar_sort_order = ar_sort_order;
	}

	public String getSort_item() {
		return sort_item;
	}

	public void setSort_item(String sort_item) {
		this.sort_item = sort_item;
	}

	public String getSort_order() {
		return sort_order;
	}

	public void setSort_order(String sort_order) {
		this.sort_order = sort_order;
	}
    public void reset(ActionMapping mapping, HttpServletRequest request){
        //一括承認チェックボックス初期化
        this.check_box_all = false;
        //承認チェックボックス初期化
        List<TorihikisakiBean> list = super.getList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
            	list.get(i).setSyonin_chk(GS.EMPTY_CHARCTER);
            }
        }
    }
}
