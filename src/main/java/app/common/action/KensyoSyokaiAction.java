/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		09/06/01		SSC				1.5次版機能組込
******************************************************************************/
package app.common.action;

import app.SessionData;
import app.SessionDataZen;
import app.common.bss.KensyoSyokaiBss;
import app.common.form.KensyoSyokaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * 引当金検証タブアクションクラス
 */
public class KensyoSyokaiAction extends AppMenuAction {

	// クラス名
	private String CLASSNAME = getClass().getName();

	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("hyoji","hyoji");
		
		return map;
	}
	
	/**
	 * 画面初期表示処理
	 * @param appContext アプリケーションContext
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		SessionDataZen cmnData = appContext.getCMNZen();
	    
        // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
	    //Log log = appContext.getLog();
		KensyoSyokaiForm form = new KensyoSyokaiForm();
        form.setHyoji("1"); // 仮〆
		// 障害票：413 2008/5/20 細野 基準日表示対応
        cmnData.setSyoriCnt("1");
        
        form.setInitmode(1);
	    appContext.setActionForm(form);
	    
		//log.write(GS.LOG_INF,CLASSNAME,"【画面初期表示処理】");
	    return select(appContext,false);
	}
	
	/**
	 * 表示データセレクトボックス変更処理
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object hyoji(AppContext appContext) throws Exception {	
		SessionDataZen cmnData = appContext.getCMNZen();
		SessionData cmn = appContext.getCMN();
		
		select(appContext,true);
		
		String rtn_val = null;
		// No335, 2008/05/30, SJA渡辺, 添付照会ボタン追加により条件修正
		// タブを使用している画面ごとにReturnを変更する。
		if(	GS.OC1106.equals(cmnData.getReturnId()) ||
			GS.OD1104.equals(cmn.getTab_riyou_gamenId()) ||
			GS.OD1105.equals(cmnData.getReturnId()) ||
			GS.OC1107.equals(cmnData.getReturnId())){
			// 引当金確認承認
			rtn_val = GS.OD1104;
		}else{
			// 査定結果照会
			rtn_val = GS.OS6102;
		}
		
		return rtn_val;
	}
	
	public Object prevX(AppContext appContext) throws Exception {
		// 処理なし
		return null;
	}

	public Object nextY(AppContext appContext) throws Exception {
		// 処理なし
		return null;
	}
	
	/**
	 * 明細情報を取得し、FormBeanにセットする。
	 * @param appContext アプリケーションContext
	 * @param syori_flg true:表示データ変更時 false:初期表示時
	 */
	public Object select(AppContext appContext,boolean syori_flg) throws Exception {    
		HttpSession session = appContext.getRequest().getSession( true );
		KensyoSyokaiForm form = (KensyoSyokaiForm)appContext.getActionForm();
		String result = null;
		KensyoSyokaiBss bss = new KensyoSyokaiBss(appContext);
		if(syori_flg){
			result = bss.reExecute();
		}else{
			result = bss.execute();
			if(form.getInitmode() == 1) { // 画面初期表示時
		        // sessionスコープにActionFormを登録（Pager用の処理）
		        session.setAttribute("HikiatekinKensyoSyokaiForm", form);
		        form.setInitmode(0);
		        appContext.setActionForm(form);
		    }
		}
		return result;
		
	}
	
}
