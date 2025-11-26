/******************************************************************************
著作権情報				:
使用JDK バージョン		:1.5.0.18
更新履歴
No		日付			修正者			修正内容
001		2009/06/30		SSC				新規作成
002		2009/10/20		SSC				残課題対応 項番32 国内版集計表対応
003		2009/11/16		SSC				残課題対応 項番91 BS対比表、BS照会表 レスポンス対応
004		2009/12/23		SSC				課題No.206 滞留判定明細(本社)　新規作成
******************************************************************************/
package app.system.action;

import app.syokaiZen.action.SateiAction;
import app.system.bss.DownloadBss;
import app.system.form.DownloadForm;
import common.AppContext;
import common.global.GS;
import common.struts.AppMenuAction;

import java.util.HashMap;

/**
 * OS8101_帳票ダウンロード アクションクラス
 */
public class DownloadAction extends AppMenuAction {

	private static final String DOWNLOADFORM = "04DownloadForm";
	
	//帳票
	private static final String CHOHYO_TAIRYUMEISAI_JA	= "011";
	private static final String CHOHYO_TAIRYUMEISAI_EN	= "012";
	private static final String CHOHYO_SYUKEISATEI_JA		= "013";
	private static final String CHOHYO_SYUKEISATEI_EN		= "014";
	private static final String CHOHYO_SYUKEIKENSYO_JA	= "015";
	private static final String CHOHYO_SYUKEIKENSYO_EN	= "016";
	private static final String CHOHYO_TAIHI_JA			= "017";
	private static final String CHOHYO_TAIHI_EN			= "018";
	private static final String CHOHYO_SYOKAI_JA			= "019";
	private static final String CHOHYO_SYOKAI_EN			= "0110";
	
	/**
	 * ディスパッチマップ作成
	 */
	public HashMap getKeyMethodMap() {
	    // ディスパッチマップ作成
		HashMap<String,String> map = new HashMap<String,String>();
		map = super.getKeyMethodMap(map);
		map.put("list_type","list_type");
		map.put("hanyo1","hanyo1");
		map.put("download","download");
		return map;
	}
	
	/**
	 * 【画面初期表示処理】
	 */
	public Object appExecute(AppContext appContext) throws Exception {	
		// 機能共通セッションの取引先情報を初期化
        appContext.getCMN().init_tori_bean();        
        // アクションフォームを取得
		DownloadForm form = (DownloadForm)appContext.getSessionActionForm(DOWNLOADFORM);
		if(form == null){
			form = new DownloadForm();
			appContext.setSessionActionForm(DOWNLOADFORM,form);
			//appContextのActionFormを上書き
		    appContext.setActionForm(form);
		    // ビジネスロジック実行
		    DownloadBss bss = new DownloadBss(appContext);
	        bss.execute();
		}
        return GS.OS8101;
	}

	/**
	 * 【帳票種別処理】
	 */
	public Object list_type(AppContext appContext) throws Exception {	
	    DownloadBss bss = new DownloadBss(appContext);
        bss.list_type();
        return GS.OS8101;
	}

	/**
	 * 【汎用１セレクトボックス処理】
	 */
	public Object hanyo1(AppContext appContext) throws Exception {	
	    DownloadBss bss = new DownloadBss(appContext);
        bss.hanyo1();
        return GS.OS8101;
	}

	/**
	 * 【ダウンロード処理】
	 *　・課題対応により帳票ダウンロード画面から出力する国内の帳票は、
	 *	海外のメソッドを通過するよう修正。
	 *　(帳票ダウンロード画面の引数で明細を出力するため)
	 */
	public Object download(AppContext appContext) throws Exception {	
	    DownloadBss bss = new DownloadBss(appContext);
		DownloadForm form = (DownloadForm)appContext.getActionForm();
		String rtnFwd = GS.OS8101;
		
		//システム区分シングルコーテーション補正
		StringBuffer sb = new StringBuffer();
		sb.append(GS.SINGLE_QUOTATION)
		.append(GS.GSS)
		.append(GS.SINGLE_QUOTATION);

	    if(bss.chkNyuryoku()){
			//システム区分判定処理
	    	//課題No91
	    	//追加開始
/*	    	
		    if(sb.toString().equals(form.getSearch_system_kbn()) &&
		    		!CHOHYO_SYUKEISATEI_JA.equals(form.getList_type()) &&
		    		!CHOHYO_SYUKEISATEI_EN.equals(form.getList_type()) &&
		    		!CHOHYO_SYUKEIKENSYO_JA.equals(form.getList_type()) &&
		    		!CHOHYO_SYUKEIKENSYO_EN.equals(form.getList_type())
		    ){
*/		    
	    	
	    	// 課題No.206
	    	// 追加開始
	    	/*
		    if(sb.toString().equals(form.getSearch_system_kbn()) && (
		    		CHOHYO_TAIRYUMEISAI_JA.equals(form.getList_type()) ||
		    		CHOHYO_TAIRYUMEISAI_EN.equals(form.getList_type()))
		    		){
				    	if(GS.EMPTY_CHARCTER.equals(form.getHanki_sihanki_kbn()) && !(CHOHYO_TAIRYUMEISAI_JA.equals(form.getList_type()) && !(CHOHYO_TAIRYUMEISAI_EN.equals(form.getList_type())))){
				        	appContext.setMsgCode(GL.ERR_SELECT,GL.ERR_KESSANKIKBN);
				    	}else{
					    	//国内帳票
					    	rtnFwd = this.downloadKokunai(appContext);
				    	}
		    }else{
		    	*/
		    	//海外帳票　※国内帳票(集計表(期中、期末))は海外帳票メソッドを使用
		    	rtnFwd = bss.download();
		    //}
		    	// 追加開始
	    }
        return rtnFwd;
	}

	/**
	 * 【←前のXX件】
	 */	
	public Object prevX(AppContext appContext) throws Exception {
		// 未実装
	    return null;
	}
	
	/**
	 * 【次のXX件→】
	 */	
	public Object nextY(AppContext appContext) throws Exception {
		// 未実装
	    return null;
	}

	
	//以下1.5次版帳票処理↓
	/**
	 * 【ダウンロードボタン処理】
	 */
	private String downloadKokunai(AppContext appContext) throws Exception {
		SateiAction acc = new SateiAction();
		String chohyo = null;
		String rtnFwd = GS.OS8101;
		
		//国内帳票出力用一覧取得処理
		chohyo = acc.search(appContext);
		//エラーメッセージが存在する場合は当画面にリターン
		if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			return rtnFwd;
		}
		//国内帳票出力処理
		if(chohyo.equals(CHOHYO_TAIRYUMEISAI_JA) || chohyo.equals(CHOHYO_TAIRYUMEISAI_EN)){
			acc.downloadMeisai(appContext);
		/*国内集計表は、海外帳票メソッドを通る(2.5次課題対応)
		}else if(chohyo.equals(CHOHYO_SYUKEISATEI_JA) || chohyo.equals(CHOHYO_SYUKEISATEI_EN)){
			((SateiForm)appContext.getActionForm()).setTyohyo("1");
			acc.downloadSyukei(appContext);
		}else if(chohyo.equals(CHOHYO_SYUKEIKENSYO_JA) || chohyo.equals(CHOHYO_SYUKEIKENSYO_EN)){
			((SateiForm)appContext.getActionForm()).setTyohyo("2");
			acc.downloadSyukei(appContext);
		*/
		//課題No.91
		//追加開始
/*			
		}else if(chohyo.equals(CHOHYO_TAIHI_JA) || chohyo.equals(CHOHYO_TAIHI_EN)){
			acc.downloadBST(appContext);
		}else if(chohyo.equals(CHOHYO_SYOKAI_JA) || chohyo.equals(CHOHYO_SYOKAI_EN)){
			acc.downloadBSS(appContext);
*/			
		}else{
			return rtnFwd;
		}
		//エラーメッセージが存在する場合は当画面にリターン
		if(appContext.getRequest().getAttribute(GS.MESSAGECONTEXT) != null){
			return rtnFwd;
		}
		return null;
	}
}