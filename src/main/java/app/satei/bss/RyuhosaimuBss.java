/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成 
******************************************************************************/
package app.satei.bss;

import app.MeisaisyosaiBean;
import app.satei.dbAcc.RyuhosaimuDbAcc;
import app.satei.form.RyuhosaimuForm;
import common.AppContext;
import common.global.GL;
import common.global.GS;
import common.util.Function;

import java.util.List;

/**
 * OC1105_査定_留保債務登録 ビジネス ロジッククラス <br>
 */
public class RyuhosaimuBss extends SateiBss{

	private RyuhosaimuForm form	= null;		//アクションフォーム

	//要素名
    private static final String BIKO			= "list[{1}].biko";

    /**
	 * コンストラクタ <br>
	 * 
	 * @param appContext
	 * @throws Exception
	 */
	public RyuhosaimuBss(AppContext appContext) throws Exception {
		super(appContext);
		this.form = (RyuhosaimuForm)appContext.getActionForm();
	}
    
    /**
     * 【一括チェック処理】 <br>
     * @return boolean
     */
    public void execIkkatu() throws Exception{

        List<MeisaisyosaiBean> list = form.getList();

        //一括判定チェックボックスの判定
        String chk = GS.OFF;
        if (form.getChkIkkatu().equals(GS.ON)) {
        	chk = GS.ON;
        }
        //画面表示分の明細を全て一括判定チェックボックスと同じにする
        if (list != null) {
            for(int i=0;i<list.size();i++){
            	list.get(i).setRyuhosaimu(chk);
            }    	
        }
    }

    /**
     * 【入力チェック処理】 <br>
     * @return boolean
     */
    public boolean check() throws Exception{
    	
    	boolean rs = TRUE;

    	if(!this.isByteChk()){
			//備考入力バイトチェック
        	rs = FALSE;
		}else if(!this.isKinshiMojiChk()){
			//入力禁止文字入力チェック
        	rs = FALSE;
		}
    	
    	return rs;
    }
        
    /**
     * 【入力バイトチェック処理】 <br>
     * @return boolean
     */
    private boolean isByteChk() throws Exception{
    	
    	boolean rs = TRUE;
    	
    	//Beanの備考入力欄チェック
        List<MeisaisyosaiBean> list = form.getAr_meisai();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
        		if(!(check.islength(list.get(i).getBiko(),1000))){
        			appContext.setMsgCode(GL.ERR_LENGTH,GL.OC1105_BIKOU);
        			appContext.setFocusField(Function.replaceString(BIKO,Integer.toString(i)));
        			rs = FALSE;
        			break;
        		}
            }
        }
    	return rs;
    }

    /**
     * 【入力禁止文字入力チェック処理】 <br>
     * @return boolean
     */
    private boolean isKinshiMojiChk() throws Exception{
    	
    	boolean rs = TRUE;

    	//Beanの備考入力欄チェック
        List<MeisaisyosaiBean> list = form.getAr_meisai();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
        		if(!GS.EMPTY_CHARCTER.equals(list.get(i).getBiko())){
        			rs = super.isKinshiChk(list.get(i).getBiko());
        		}
        		if(rs == FALSE){
        			appContext.setFocusField(Function.replaceString(BIKO,Integer.toString(i)));
        			break;
        		}
            }
        }
    	return rs;
    }

    /**
	 * 初期表示処理<br>
     * @param AppContext
	 */
	public void execInit(AppContext appContext) throws Exception{
		
		RyuhosaimuDbAcc dbacc = new RyuhosaimuDbAcc(appContext);
		
		//表示件数セレクトボックス値取得
		dbacc.getShow();
		
		//債務総計・留保債務計取得
		dbacc.getKei(tori_bean);
		
		//留保債務取得
		dbacc.getRyuhosaimuData(tori_bean);
	}
	
	/**
	 * 保存処理<br>
     * @param boolean
	 */
	public void execHozon() throws Exception{

    	RyuhosaimuDbAcc dbacc = new RyuhosaimuDbAcc(appContext);
		//留保債務削除
		dbacc.deleteRyuhoSaimu(tori_bean);
    	
        List<MeisaisyosaiBean> list = form.getAr_meisai();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
        		//留保債務登録
        		dbacc.insertRyuhoSaimu(tori_bean,list.get(i));
            }
        }
		
		//債務総計・留保債務計取得
		dbacc.getKei(tori_bean);
				
		//査定内容登録
		dbacc.updateSateiData(tori_bean);
		
		//コミット
		dbacc.commit();
	}
}
