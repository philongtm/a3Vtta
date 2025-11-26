/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.form;

import app.TorihikisakiBean;
import common.global.GS;
import common.struts.AppPagerActionForm;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * OC1106_査定_承認一覧 アクションフォームクラス
 * 
 */
public class SyoninForm extends AppPagerActionForm {
	
    private LinkedHashMap ar_show;		//表示件数セレクトボックス用配列
    private String sansyo_phase;       //参照フェーズ
    private int id;	            	//リンククリックされた取引先のID
    private String ikt_syonin;			//一括承認チッェクボックス用
      
    //変数初期化
    public SyoninForm() {
    	super.gamenId			= GS.OC1106;
        this.ar_show			= null;
        this.sansyo_phase		= GS.EMPTY_CHARCTER;
        this.id					= 0;
        this.ikt_syonin			= GS.EMPTY_CHARCTER;
        this.setPager(new ArrayList<TorihikisakiBean>());
        this.setAr_meisai(new ArrayList<TorihikisakiBean>());
    }
    
    /**
	 * @return 画面IDを戻します。
	 */
	public String toString(){
		return super.gamenId;
	}
    
    //表示件数配列
    public LinkedHashMap getAr_show() {
        return ar_show;
    }
    public void setAr_show(LinkedHashMap ar_show) {
        this.ar_show = ar_show;
    }
    //参照フェーズ
    public String getSansyo_phase() {
        return sansyo_phase;
    }
    public void setSansyo_phase(String sansyo_phase) {
        this.sansyo_phase = sansyo_phase;
    }
	//id
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    //チェックボックス
	public String getIkt_syonin() {
		return ikt_syonin;
	}    
	public void setIkt_syonin(String ikt_syonin) {
		this.ikt_syonin = ikt_syonin;
	}
    public void reset(ActionMapping mapping, HttpServletRequest request){
        //一括承認チェックボックス初期化
        this.ikt_syonin = GS.EMPTY_CHARCTER;
        //承認チェックボックス初期化
        List<TorihikisakiBean> list = super.getList();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
            	list.get(i).setSyonin_chk(GS.EMPTY_CHARCTER);
            }
        }
    }
}