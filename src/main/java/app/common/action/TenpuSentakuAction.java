/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
0001	09/05/15		SSC				1.5次版機能組込
0002	09/05/15		SSC				OD1102引当金確認対応
0003	09/10/20		SSC				課題No.52 HTMLファイルオープン対応
0004	09/11/13		SSC				課題No.09 保有文書添付仕様変更
******************************************************************************/
package app.common.action;

import app.SessionDataZen;
import app.common.bss.TenpuSentakuBss;
import app.common.form.TenpuSentakuForm;
import app.hikiate.action.KakuninAction;
import app.hikiate.action.KensyoAction;
import app.hikiate.form.KakuninForm;
import app.hikiate.form.KensyoForm;
import app.satei.action.HikiateAction;
import app.satei.form.HikiateForm;
import app.system.action.KureemuMeisaiAction;
import app.system.action.KureemuSyosaiAction;
import app.system.form.KureemuMeisaiForm;
import app.system.form.KureemuSyosaiForm;
import app.tairyu.action.SyoninsyosaiAction;
import app.tairyu.action.SyosaiAction;
import app.tairyu.form.SyoninsyosaiForm;
import app.tairyu.form.SyosaiForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.util.InputCheck;

import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 共通_滞留債権明細照会タブ アクションクラス
 */
public class TenpuSentakuAction extends AppMenuAction {
	
	// クラス名
	private String CLASSNAME = getClass().getName();
	private String FORMNAME = "app.common.form.TenpuSentakuForm";
	private final String TENPU_CHECK			= "tenpuCheck";
	private final String SAKUJO_CHECK			= "sakujoCheck";
	private final String CHK_BOX_ON			= "on";
	private final String CHK_BOX_OFF			= "off";
	
	
	/**
	 * ディスパッチマップ作成&変数初期化処理
	 */
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("ok","ok");
		map.put("touroku","touroku");
		map.put("back","back");
		map.put("download","download");
		map.put("setTenpu","setTenpu");
		map.put("setSakujo","setSakujo");
		
		return map;
	}
	
	/**
	 * 画面初期表示処理
	 * @param appContext アプリケーションContext
	 */
	public Object appExecute(AppContext appContext) throws Exception {
	    ////////////////////////////////////////////////
		//障害票：510
		//チェックイン日：2008/5/27
		//対応者：SJA中島
		//概要：初回時のみ、添付選択画面Formを生成するように修正。
		////////////////////////////////////////////////
		
		if(!FORMNAME.equals(appContext.getActionForm().getClass().getName())){
			SessionDataZen cmnData = appContext.getCMNZen();
            // No414, 2008/05/29, SJA 関, 不要なログ出力の削除
		    //Log log = appContext.getLog();
			TenpuSentakuForm form = new TenpuSentakuForm();
			form.setInitmode(1);
			
			if(GS.OB1103.equals(cmnData.getReturnId())){
				// 滞留判定詳細画面からの遷移
		        form.setSyosaiForm((SyosaiForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getTairyu_anken_no());		
				form.setTori_cd(cmnData.getKanjo_cd());
				form.setAnken_no_eda(cmnData.getAnken_no_eda());
				// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OB1105.equals(cmnData.getReturnId())){
				// 滞留画面承認からの遷移
		        form.setSyoninSyosaiForm((SyoninsyosaiForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getTairyu_anken_no());						
				form.setTori_cd(cmnData.getKanjo_cd());
				// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OC1104.equals(cmnData.getReturnId())){
				// 引当金判定からの遷移
				form.setHikiateForm((HikiateForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getSatei_anken_no());
				form.setTori_cd(cmnData.getKanjo_cd());
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OD1105.equals(cmnData.getReturnId())){
				// 引当金検証からの遷移
				form.setKensyoForm((KensyoForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getSatei_anken_no());
				form.setTori_cd(cmnData.getKanjo_cd());
				// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OD1102.equals(cmnData.getReturnId())){
				// 引当金確認からの遷移
				form.setKakuninForm((KakuninForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getSatei_anken_no());
				form.setTori_cd(cmnData.getKanjo_cd());
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OS3102.equals(cmnData.getReturnId())){
				// クレーム債権再設定 明細一覧からの遷移
				form.setKureemuMeisaiForm((KureemuMeisaiForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getSatei_anken_no());
				form.setTori_cd(cmnData.getKanjo_cd());
				// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}else if(GS.OS3103.equals(cmnData.getReturnId())){
				// クレーム債権再設定 明細詳細からの遷移
				form.setKureemuSyosaiForm((KureemuSyosaiForm)appContext.getActionForm());
				form.setAnken_no(cmnData.getSatei_anken_no());
				form.setTori_cd(cmnData.getKanjo_cd());
				// No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
				form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
			}

	        //フェーズ1の時には、01で固定
	        form.setKikan_sys_kbn(cmnData.getSystem_kbn());
		    appContext.setActionForm(form);
			//log.write(GS.LOG_INF,CLASSNAME,"【画面初期表示処理】");
		}
		
	    return select(appContext);
	}
	
	/**
	 * 登録ボタン押下時の処理
	 * @param appContext アプリケーションContext
	 */
	public Object touroku(AppContext appContext) throws Exception {
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		
		TenpuSentakuBss bss=new TenpuSentakuBss(appContext);
		
		if(form.getList().size() == 0){
			appContext.setMsgCode("err.0059");
			return GS.OZ1101;
		}

		bss.tourokuFile(form.getList());
		
		// 再検索
		select(appContext);
		
		return GS.OZ1101;
	}
	
	/**
	 * OKボタン押下時の処理
	 * @param appContext アプリケーションContext
	 */
	public Object ok(AppContext appContext) throws Exception {
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		InputCheck check = new InputCheck();
    	// No484, 2008/05/30, SJA渡辺, 入力禁止文字存在チェック追加
		if (form.getFileUp() != null && check.haveKinshiMoji(form.getFileUp().toString())) {
			appContext.setMsgCode("err.0109");
			form.setFileUp(null);
			//No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("fileUp");
			return GS.OZ1101;
		}
		TenpuSentakuBss bss=new TenpuSentakuBss(appContext);
		bss.uploadFile(form.getFileUp());
		// ファイル情報を消去する(アップロード最大サイズを超えたとき、前に実行した情報を入れてしまっているため)
		form.setFileUp(null);
		return GS.OZ1101;
	}
	
	/**
	 * 戻るボタン押下時の処理
	 * @param appContext アプリケーションContext
	 */
	public Object back(AppContext appContext) throws Exception {
	    SessionDataZen cmnData = appContext.getCMNZen();
	    TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
	    
		if(GS.OB1103.equals(cmnData.getReturnId())){
			// 滞留判定再表示アクションを実行。
			appContext.setActionForm(form.getSyosaiForm());
			SyosaiAction acc = new SyosaiAction();
			acc.appReExecute(appContext);
		}else if(GS.OB1105.equals(cmnData.getReturnId())){
			// 滞留承認再表示アクションを実行。
			appContext.setActionForm(form.getSyoninSyosaiForm());
			SyoninsyosaiAction acc = new SyoninsyosaiAction();
			acc.appExecute(appContext);
		}else if(GS.OC1104.equals(cmnData.getReturnId())){
			// 引当金判定再表示アクションを実行。
			appContext.setActionForm(form.getHikiateForm());
			HikiateAction acc = new HikiateAction();
			acc.appExecute(appContext);
		}else if(GS.OD1105.equals(cmnData.getReturnId())){
			// 引当金検証再表示アクションを実行。
			appContext.setActionForm(form.getKensyoForm());
			KensyoAction acc = new KensyoAction();
			acc.appReExecute(appContext);
		}else if(GS.OD1102.equals(cmnData.getReturnId())){
			// 引当金確認再表示アクションを実行。
			appContext.setActionForm(form.getKakuninForm());
			KakuninAction acc = new KakuninAction();
			acc.appReExecute(appContext);
		}else if(GS.OS3102.equals(cmnData.getReturnId())){
			// クレーム債権再設定 明細一覧再表示アクションを実行。
			appContext.setActionForm(form.getKureemuMeisaiForm());
			KureemuMeisaiAction acc = new KureemuMeisaiAction();
			acc.appReExecute(appContext);
		}else if(GS.OS3103.equals(cmnData.getReturnId())){
			// クレーム債権再設定 明細詳細再表示アクションを実行。
			appContext.setActionForm(form.getKureemuSyosaiForm());
			KureemuSyosaiAction acc = new KureemuSyosaiAction();
			acc.appExecute(appContext);
		}
		return (String)cmnData.getReturnId();
	}
	
	/**
	 * ファイルリンク押下時の処理
	 * @param appContext アプリケーションContext
	 */
	public Object download(AppContext appContext) throws Exception {
		TenpuSentakuBss bss=new TenpuSentakuBss(appContext);
		bss.downloadFile();
		
		// 課題No.52
		// 修正開始
		if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			return GS.OZ1101;
		}
        return null;
		// 修正完了
	}
	
	/**
	 * 添付チェックボックスの値が変わったときの処理
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object setTenpu(AppContext appContext) throws Exception {
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		
		int idx = new Integer(form.getIndexId()).intValue();
		
		ArrayList list = form.getList();
		HashMap map = (HashMap)list.get(idx);
		
		//課題No.09
		//削除開始
		// 添付、削除チェックボックス排他チェック
		/*if(map.get("tenpuCheck").equals("off") && map.get("sakujoCheck").equals("on")){
			appContext.setMsgCode("err.0060");
			//No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("sakujo(" + Integer.toString(idx) + ")");
			return GS.OZ1101;
		}*/
		//削除完了

		//課題No.09
		//修正開始
		if(map.get(TENPU_CHECK).equals(CHK_BOX_ON)){
			map.put(TENPU_CHECK,CHK_BOX_OFF);
		}else{
			map.put(TENPU_CHECK,CHK_BOX_ON);
			map.put(SAKUJO_CHECK,CHK_BOX_OFF);
		}
		//修正完了
		list.set(idx,map);
		form.setList(list);
		appContext.setActionForm(form);		
		return GS.OZ1101;
	}

	/**
	 * 削除チェックボックスの値が変わったときの処理
	 * @param appContext
	 * @return
	 * @throws Exception
	 */
	public Object setSakujo(AppContext appContext) throws Exception {
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		
		int idx = new Integer(form.getIndexId()).intValue();
		
		ArrayList list = form.getList();
		HashMap map = (HashMap)list.get(idx);
		
		//課題No.09
		//削除開始
		// 添付、削除チェックボックス排他チェック
		/*if(map.get("sakujoCheck").equals("off") && map.get("tenpuCheck").equals("on")){
			appContext.setMsgCode("err.0060");
			//No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("tenpu(" + Integer.toString(idx) + ")");
			return GS.OZ1101;
		}*/
		//削除完了
		
		//課題No.09
		//修正開始
		if(map.get(SAKUJO_CHECK).equals(CHK_BOX_ON)){
			map.put(SAKUJO_CHECK,CHK_BOX_OFF);
		}else{
			map.put(SAKUJO_CHECK,CHK_BOX_ON);
			map.put(TENPU_CHECK,CHK_BOX_OFF);
		}
		//修正完了
		list.set(idx,map);
		form.setList(list);
		appContext.setActionForm(form);
		return GS.OZ1101;
	}
	
	/**
	 * 添付ファイル情報を取得し、FormBeanにセットする。
	 * @param appContext アプリケーションContext
	 */
	public Object select(AppContext appContext) throws Exception {    
		HttpSession session = appContext.getRequest().getSession( true );
		TenpuSentakuForm form = (TenpuSentakuForm)appContext.getActionForm();
		
		TenpuSentakuBss bss = new TenpuSentakuBss(appContext);
		String result = bss.execute();
		if(form.getInitmode() == 1) { // 画面初期表示時
	        // sessionスコープにActionFormを登録（Pager用の処理）
			
	        session.setAttribute("TenpuSentakuForm", form);
	        form.setInitmode(0);
	        appContext.setActionForm(form);
	    }
		return result;
	}
		
	public Object prevX(AppContext appContext) throws Exception {
		// 空実装
	    return GS.OZ1101;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
		// 空実装
	    return GS.OZ1101;
	}
}
