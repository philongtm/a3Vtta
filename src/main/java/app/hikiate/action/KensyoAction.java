/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2009/05/18		SSC				1.5次版機能組込 
002		2009/10/23		SSC				課題No.73 取引先区分プルダウン設定値を2.0次に合わせる
003		2009/12/2		SSC				課題No.181 年月の表示(英語版)MM/YYYYに統一
******************************************************************************/
package app.hikiate.action;

import app.SessionData;
import app.SessionDataZen;
import app.TorihikisakiBean;
import app.common.action.SashimodoshiCommentAction;
import app.common.action.TenpuSentakuAction;
import app.common.action.TensouAction;
import app.hikiate.bss.KensyoBss;
import app.hikiate.form.KensyoForm;
import app.syokai.action.SateisyosaiAction;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;
import common.struts.AppPagerActionForm;
import common.util.Function;
import common.util.HikiatekinExcel;
import common.util.InputCheck;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;


/**
 * 引当金検証画面アクションクラス
 */
@Controller
@RequestMapping("/hikiate/kensyo.do")
public class KensyoAction extends AppMenuAction {

	//////////////////////////////////////////////////////
	//障害票：588
	//チェックイン日：2008/6/5
	//対応者：SJA中島
	//概要：計算処理メソッドを削除。
	///////////////////////////////////////////////////////
	
	private String CLASSNAME = getClass().getName(); // クラス名
	
	/**=========================================
	 * ディスパッチマップ作成&変数初期化
	 *==========================================*/
	public HashMap getKeyMethodMap() {
	    //ディスパッチアップ作成
		HashMap map = new HashMap();
		map = super.getKeyMethodMap(map);
		map.put("back","back");
		map.put("forward","forward");
		map.put("firstPreserve","firstPreserve");
		map.put("register","register");
		map.put("download","download");
		map.put("release","release");
		map.put("result","result");
		map.put("appendSelection","appendSelection");
		
		map.put("syonin","syonin");
		map.put("saikensa","saikensa");
		map.put("comment","comment");
		
		return map;
	}


	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {
		
		//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
		appContext.setFocusField("indexSyonin");

		// 遷移元のActionFormより値を移行
		TorihikisakiBean toribean = appContext.getCMN().getTori_bean();
		
		SessionDataZen cmnData = appContext.getCMNZen();
		
		//この画面用のActionFormを作成
		KensyoForm form = new KensyoForm();

		form.setAnken_no(toribean.getAnken_no());
	    form.setTxtCdTorihiki(toribean.getKanjo_cd());
	    form.setTxtNameTorihiki(toribean.getKanjo_nm());
	    form.setPhase(toribean.getPhase());
	    form.setYm(toribean.getTaisyo_ym());
	    
	    // 課題No.181
	    // 追加開始
	    //form.setTxtFinalYmKijun(toribean.getTaisyo_ym());
	    form.setTxtFinalYmKijun(Function.insertYmSlash(toribean.getTaisyo_ym(),cmnData.getComLangMode()));
	    // 追加完了
	    
	    form.setMise_cd(toribean.getMise_cd());
	    form.setSystem_kbn(toribean.getSystem_kbn());
	    // No797, 2008/06/09, SJA渡辺, 案件の査定会社コードを保持するように修正
	    form.setSateikaisya_cd(cmnData.getAnken_satei_kaisya_cd());
	    //結合テスト障害No038対応 半期四半期区分をセット
	    //追加開始
	    form.setHanki_sihanki_kbn(cmnData.getHanki_sihanki_kbn());
	    //追加完了
	    
	    form.setInitmode(1);
	    
        // appContextのActionFormを上書き
        appContext.setActionForm(form);
	    
	    return select(appContext);
	}
	
	/**
	 * 【画面初期表示処理(遷移先より戻り時)】
	 */
	public Object appReExecute(AppContext appContext) throws Exception {

	    return select(appContext);
	    
	}
	
	public Object prevX(AppContext appContext) throws Exception {
	    // 使用しない
		return GS.OD1105;
	}
	
	public Object nextY(AppContext appContext) throws Exception {
	    // 使用しない
		return GS.OD1105;
	}
	
	/**
	 * 【戻るボタン処理】
	 */
	public Object back(AppContext appContext) throws Exception {

		// ActionFormをsessionから削除
		appContext.removeAttribute("02HikiatekinKensyoForm");
		
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		
		return GS.OD1101;
	}

	/**
	 * 【転送ボタン処理】
	 */
	public Object forward(AppContext appContext) throws Exception {

		syonin(appContext);
		
		// 次画面へ行くので、一時保存しておく。
		// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
		if(!GS.RC_NG.equals(firstPreserve(appContext,false))){
			SessionDataZen cmnData = appContext.getCMNZen();
			SessionData cmn = appContext.getCMN();
		    // 戻り時の画面ＩＤセット
			cmnData.setReturnId(GS.OD1105);
			cmn.setReturn_gamenId(GS.OD1105);
			
		    TensouAction acc = new TensouAction();
		    acc.appExecute(appContext);
			
		    return GS.OZ3101;
			
		}else{
			return GS.OD1105;
		}
	}
	
	/**
	 * 【査定結果ボタン処理】
	 */
	public Object result(AppContext appContext) throws Exception {
		KensyoForm form = (KensyoForm)appContext.getActionForm();		
		syonin(appContext);
		// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
		if(!GS.RC_NG.equals(firstPreserve(appContext,false))){
			SessionDataZen cmnData = appContext.getCMNZen();
			SessionData cmn = appContext.getCMN();
		    // 戻り時の画面ＩＤセット
			cmnData.setReturnId(GS.OD1105);
			cmn.setReturn_gamenId(GS.OD1105);
			////////////////////////////////////////////////////
			//障害票：330
			//チェックイン日：2008/05/15
			//対応者：SJA中島
			//概要：査定結果詳細画面へ遷移する際に、必要な条件をセットする。
			////////////////////////////////////////////////////
		    cmnData.setYm(form.getTxtYmSatei());
		    cmnData.setKanjo_cd(form.getTxtCdTorihiki());
		    cmnData.setKanjo_nm(form.getTxtNameTorihiki());
		    cmnData.setPhase(cmn.getTori_bean().getPhase());
		    cmnData.setSatei_anken_no(form.getSateiAnkenNo());
			
			SateisyosaiAction acc = new SateisyosaiAction();
			acc.appExecute(appContext);
			
			return GS.OS6102;
		} else {
			return GS.OD1105;
		}
	}

	/**
	 * 【一次保存ボタン処理】
	 */	
	// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
	public Object firstPreserve(AppContext appContext) throws Exception {
		firstPreserve(appContext, true);
		return GS.OD1105;
	}
	

	// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
	public Object firstPreserve(AppContext appContext,boolean isNyuryokuFlg) throws Exception {

		syonin(appContext);
		
		if (inputCheck(appContext)) {
			return GS.RC_NG;
		}
		
		KensyoBss bss = new KensyoBss(appContext);
		if (!(bss.firstPreserveExecute(isNyuryokuFlg))) {
		    throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc");
			//return GS.RC_NG;
		}
		
		return GS.RC_OK;
	}
	
	/**
	 * 【登録ボタン処理】
	 */
	public Object register(AppContext appContext) throws Exception {
		
		syonin(appContext);
		
		KensyoForm form = (KensyoForm)appContext.getActionForm();
		// 承認担当者チェック
		if ("".equals(form.getIndexSyonin())) {
			appContext.setMsgCode("err.0004");
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("indexSyonin");
			return GS.OD1105;
		}
		
		if (inputCheck(appContext)) {
			return GS.OD1105;
		}
		
		KensyoBss bss = new KensyoBss(appContext);
		if (bss.registerExecute()) {
			// ActionFormをsessionから削除
			appContext.removeAttribute("02HikiatekinKensyoForm");
			
			IchiranAction acc = new IchiranAction();
			acc.appReExecute(appContext);
			
			return GS.OD1101;
		} else {
		    throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc");
			//return GS.RC_OK;
		}
	}
	
	/**
	 * 入力チェック
	 * true：入力エラー有、false：入力エラー無
	 */
	private boolean inputCheck(AppContext appContext) throws SQLException {
	    // 処理結果フラグ
		boolean result = false;
		KensyoForm form = (KensyoForm)appContext.getActionForm();
		KensyoBss bss = new KensyoBss(appContext);
		
		InputCheck check = new InputCheck();
		
		// TODO 全ての項目の「整数12桁以内で入力してください」追加せよ
		// 留保債務
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalRyuhosaimu()),"err.0071","err.0070","err.0070"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalRyuhosaimu");
			return true;
		}
		
		// 第三者留保債務
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalRyuhosaimu3()),"err.0041","err.0040","err.0040"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalRyuhosaimu3");
			return true;
		}
		
		// 保全
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalHozen()),"err.0073","err.0072","err.0072"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalHozen");
			return true;
		}
		
		// その他回収
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalKingakuSonota()),"err.0035","err.0034","err.0034"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalKingakuSonota");
			return true;
		}
		
		// 保証債務合計
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalKingaku14()),"err.0087","err.0086","err.0086"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalKingaku14");
			return true;
		}
		
		// 履行請求懸念
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalKingakuKenen()),"err.0037","err.0036","err.0036"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalKingakuKenen");
			return true;
		}
		
		// 引当金補正額
		if (!(bss.checkKingaku(check,Function.removeComma(form.getFinalKingakuHosei()),"err.0075","err.0074","err.0074"))) {
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalKingakuHosei");
			return true;
		}
		
		// 「引当金算定根拠」の内容説明 入力バイトチェック
		if (!(check.islength(form.getFinalCmtValKonkyo(),500))) {
			appContext.setMsgCode("err.0067");
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			appContext.setFocusField("finalCmtValKonkyo");
			return true;
		// No484, 2008/05/29, SJA渡辺, 入力禁止文字存在チェック追加
		} else if (check.haveKinshiMoji(form.getFinalCmtValKonkyo())) {
	    	 appContext.setMsgCode("err.0109");
				//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
				appContext.setFocusField("finalCmtValKonkyo");
			 return true;
	    }
		
		// 取引先区分・債権区分が変更されたかチェック
		// 課題No.73
		// 削除開始
		/*if (!(form.getFinalKbnTorihiki().equals(form.getTxtKbnTorihiki()) && 
				form.getFinalKbnSaiken().equals(form.getTxtKbnSaiken()))) {
			appContext.setMsgCode("err.0026");
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			//No809, 2008/06/11, SJA遠藤, エラー時のフォーカス設定間違いを修正
			if(!(form.getFinalKbnTorihiki().equals(form.getTxtKbnTorihiki()))){
				appContext.setFocusField("finalKbnTorihiki");
				return true;
			}
			if(!(form.getFinalKbnSaiken().equals(form.getTxtKbnSaiken()))){
				appContext.setFocusField("finalKbnSaiken");
				return true;
			}
		}*/
		// 削除完了
		
		return result;
	}
	
	/**
	 * 【ダウンロードボタン処理】
	 */
	public Object download(AppContext appContext) throws Exception {
		
		syonin(appContext);
		
		SessionDataZen cmnData = appContext.getCMNZen();
		SessionData cmn = appContext.getCMN();
		cmnData.setComLangMode(((AppPagerActionForm)appContext.getActionForm()).getLangMode());
	    // 戻り時の画面ＩＤセット
		cmnData.setReturnId(GS.OD1105);
		cmn.setReturn_gamenId(GS.OD1105);

		HikiatekinExcel chohyo = new HikiatekinExcel(appContext);
		chohyo.execute();
		
		return null;
	}
	
	/**
	 * 【もぎ取り解除ボタン処理】
	 */
	public Object release(AppContext appContext) throws Exception {
		
		//allCalc(appContext);
		
		KensyoBss bss = new KensyoBss(appContext);
		if (!(bss.releaseExecute())) {
		    throw new SQLException();
			//appContext.setMsgCode("err.system.dbacc");
			//return GS.RC_OK;
		}
		
		// ActionFormをsessionから削除
		appContext.removeAttribute("02HikiatekinKensyoForm");
		
		IchiranAction acc = new IchiranAction();
		acc.appReExecute(appContext);
		
		return GS.OD1101;
	}
	
	/**
	 * 【添付選択ボタン処理】
	 */
	public Object appendSelection(AppContext appContext) throws Exception {
		
		syonin(appContext);

		SessionDataZen cmnData = appContext.getCMNZen();
		SessionData cmn = appContext.getCMN();
		KensyoForm form = (KensyoForm)appContext.getActionForm();
	    // 戻り時の画面ＩＤセット
		
	    // 戻り時の画面ＩＤセット
	    cmnData.setReturnId(GS.OD1105);
		cmn.setReturn_gamenId(GS.OD1105);
		
	    // 査定案件Noセット
	    cmnData.setSatei_anken_no(form.getAnken_no());
	    /////////////////////////////////////////////
	    //障害票：427
	    //チェックイン日：2008/5/26
	    //対応者：SJA中島
	    //概要：セッションデータのフェーズ値を共通定数クラスに定義した
	    //     値をセットする。
	    /////////////////////////////////////////////
		cmnData.setPhase(GS.PHASE_HIKIATEKIN_KENSYO);
	    
		TenpuSentakuAction acc = new TenpuSentakuAction();
		acc.appExecute(appContext);
		
	    return GS.OZ1101;
	}
	
	/**
	 * 【承認担当者セレクトボックス処理】
	 */
	public Object syonin(AppContext appContext) throws Exception {

		KensyoForm form = (KensyoForm)appContext.getActionForm();
		
		List recoName = form.getRecoName();
		
		if (recoName != null && recoName.size() > 0) {
			if ("".equals(form.getIndexSyonin())) {
				form.setTantoSyonin("");
				form.setBumonSyonin("");
			} else {
				HashMap map = (HashMap)recoName.get(Function.getValueOfInt(form.getIndexSyonin()));
				
				form.setTantoSyonin((String)map.get("togo_id"));
				form.setBumonSyonin((String)map.get("SOSHIKI_CD"));
			}
			
			appContext.setActionForm(form);
		}
		
	    return GS.OD1105;
	}
	
	// 取引先区分・債権区分が変更されたかチェック
	// 課題No.73
	// 削除開始
	/*public Object saikensa(AppContext appContext) throws Exception {
		
		syonin(appContext);

		KensyoForm form = (KensyoForm)appContext.getActionForm();
		
		// 取引先区分・債権区分が変更されたかチェック
		if (!(form.getFinalKbnTorihiki().equals(form.getTxtKbnTorihiki()) && 
				form.getFinalKbnSaiken().equals(form.getTxtKbnSaiken()))) {
			appContext.setMsgCode("err.0026");
			//No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御追加
			if(!(form.getFinalKbnTorihiki().equals(form.getTxtKbnTorihiki()))){
				appContext.setFocusField("finalKbnTorihiki");
				
			}else if(!(form.getFinalKbnSaiken().equals(form.getTxtKbnSaiken()))){
				appContext.setFocusField("finalKbnSaiken");
			}
		}
	    return GS.OD1105;
	}*/
	// 削除完了

	/**
	 * 【差戻コメントリンク処理】
	 */
	public Object comment(AppContext appContext) throws Exception {
		
		// 一次保存
		// 障害表：540　チェックイン日：2008/6/3　SJA中島　一次保存の入力履歴は、一次保存ボタン押下時のみとする。
		if(!GS.RC_NG.equals(firstPreserve(appContext,false))){
			SessionDataZen cmnData = appContext.getCMNZen();
			SessionData cmn = appContext.getCMN();
			KensyoForm form = (KensyoForm)appContext.getActionForm();
			
			// 戻り時の画面ＩＤセット
			cmnData.setReturnId(GS.OD1105);
			cmn.setReturn_gamenId(GS.OD1105);
			
			// 差戻先選択【画面初期表示処理】
			SashimodoshiCommentAction acc = new SashimodoshiCommentAction();
			acc.appExecute(appContext, form.getAnken_no());
			
			return GS.OZ4101;
		} else {
			return GS.OD1105;
		}
	}
	
	
	/**=========================================
	 * 明細情報取得
	 *==========================================*/
	public Object select(AppContext appContext) throws Exception {    
		KensyoForm form = (KensyoForm)appContext.getActionForm();
		// No426, 2008/05/22, SJA渡辺, オブジェクト変数名を即した名称に修正。
		HttpSession session = appContext.getRequest().getSession( true );
	    
	    //検索実行
	    KensyoBss bss = new KensyoBss(appContext);	        	    
	    if(form.getInitmode() == 1) { // 画面初期表示時
	        String result = bss.execute();
	        form.setInitmode(0);
	        session.setAttribute("02HikiatekinKensyoForm", form);
	        return result;
	    } else { // 画面初期表示時以外
			return bss.execute();
	    }
	}
}