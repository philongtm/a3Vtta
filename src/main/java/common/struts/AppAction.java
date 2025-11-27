/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/13		SSC				1.5次版に修正を施し流用
002		2008/03/03		SSC				メニューリンククリック判定追加
003		2009/04/21		SSC				ヘルプ機能作成
004		2009/05/20		SSC				帳票ダウンロード選択画面実装方法変更
005		2009/05/29		SSC				査定一覧の参照判定方法変更
006		2016/12/16		SSC				BJ201612070 SSO対応
007		2017/01/04		SSC				BJ201612070 SSO対応
******************************************************************************/
package common.struts;

import app.UserBean;
import common.AppContext;
import common.db.CommonDbAcc;
import common.global.GS;
import common.util.Function;
import common.util.Log;
import common.util.SetteiDbAcc;
import common.util.TempFile;
import config.adapter.struts.action.Action;
import config.adapter.struts.action.ActionForm;
import config.adapter.struts.action.ActionForward;
import config.adapter.struts.action.ActionMapping;
import config.adapter.struts.upload.FormFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 拡張アクションクラス
 * 
 */
public abstract class AppAction extends Action {

	private String CLASSNAME = getClass().getName(); //クラス名
    private Log log = new Log();
	private static final String STR_Q = "?";	
	private static final String STR_AMP = "&";	
	private static final String STR_EVENT = "event=";	
	private static final String REPLACE_EVENT = "event=\"{1}\"";
	private static final String CLOSE_JSP = "/close.jsp";
	private static final String FORWARD = "rc={1} forward={2}";
	private static final String UPLOAD_START = "Upload start - {1} ({2})";
	private static final String UPLOAD_END = "Upload END - {1}";
	private static final String ERR_EXP_JSP = "/include/servletException.jsp";
	private static final String KEY_JSP = "ERRJSP";
	private static final String ERR_JSP = "error41.jsp";
	private static final String ERR_ONLINE = "オンライン停止時間帯のため使用できません。";
	private static final String MENU_LINK = "^menuLink.+";
	private static final String SATEIICHIRAN_LINK = "menuLinkOC110(1|6)(A|B)";
	private static final String DOWNLOAD = "^download[1-2]";
	private static final String DOWNLOAD_JA		= "1";
	private static final String DOWNLOAD_EN		= "2";
	private static final String HELP_PATH = "../help/{1}";
	private static final String KEGEN_ERROR_JSP = "/login/loginError.jsp";
	
	/**
	 * アプリケーション用のexecute()メソッド
	 * 
	 * @param appContext AppContext
	 * @return ActionForwardオブジェクト
	 */
	public abstract Object appExecute(AppContext appContext) throws Exception;
	public abstract HashMap getKeyMethodMap();

	/**
	 * コンストラクタ
	 */
	public AppAction() {
	}
	
	/**
	 * カスタムexecute()メソッド
	 * 共通の前処理を行い、アプリケーション用のexecute()メソッドを呼び出す。
	 * 
	 * @param mapping
	 * 				アクションマッピングオブジェクト
	 * @param form
	 * 				アクションフォームのインスタンス
	 * @param request
	 * 				HTTPリクエストオブジェクト
	 * @param response
	 * 				HTTPレスポンスオブジェクト
	 * @return ActionForwardオブジェクト
	 */
	public final ActionForward execute(ActionMapping mapping,
		 	  ActionForm form,
		 	  HttpServletRequest request,
		 	  HttpServletResponse response)
		throws Exception {

		//アプリケーションのコンテキストを取得
		AppContext appContext = AppContext.getInstance(request);

		//セッションデータのメッセージを初期化
		String msg = null;
		appContext.setMsgCode(msg);
		
		//ログイン制御
		SetteiDbAcc setteiDbAcc = new SetteiDbAcc(appContext.getSqlExecuter(),log,appContext);
		if(!setteiDbAcc.execute()){
			request.setAttribute(KEY_JSP,ERR_JSP);
			log.write(GS.LOG_ERR,GS.EMPTY_CHARCTER,ERR_ONLINE);
			return new ActionForward(ERR_EXP_JSP);
		}
		
		//アクションフォームの検証メソッドでの例外処理
		Exception validateException;
		if( (validateException = (Exception)appContext.getRequest().getAttribute(GS.VALIDATEEXCEPTION))!=null ) {
			throw validateException;
		}

		//アクションフォームを登録
		appContext.setActionForm(form);

		Object findForward;
		String methodName;

		//ディスパッチマップを取得
		HashMap keyMethodMap = getKeyMethodMap();
		String event = request.getParameter(GS.EVENT);
		
		//複数イベントが発生したときの対応
		if(event != null){
			String[] event_1 = Function.StrSplitToken(event,STR_Q);
			if(event_1.length > 1){
				//2つ目のイベントを実行するようにする。
				String[] split_event = event_1[event_1.length - 1].split(STR_EVENT);
				event = split_event[1];
			}
		}
		
		log.write(GS.LOG_INF,CLASSNAME,Function.replaceString(REPLACE_EVENT,event));
		if(Function.matches(event,MENU_LINK)){
			appContext.removeActionFormAll();
		}if(Function.matches(event,DOWNLOAD)){
			//帳票言語処理
			event = this.appDownload(event,appContext);
		}
		if(Function.matches(event,SATEIICHIRAN_LINK)){
			event = this.appSatei(event,appContext);
		}
		if( (event!=null) && (event.length()>0) ) {
			if((keyMethodMap != null) && ((methodName = (String)keyMethodMap.get(event))!=null)){
				try{
					//実行するメソッドを取得
					Class[] types = new Class[] { AppContext.class };
					Method method = this.getClass().getMethod(methodName, types);
					//アクションを実行
					findForward = method.invoke(this, new Object[]{appContext});
				}catch(InvocationTargetException  e){
					log.write(GS.LOG_ERR,CLASSNAME,e.getCause().getMessage());
					throw (Exception)e.getTargetException();
				}
			}else{
				//デフォルトのアクションを実行
				findForward = appExecute(appContext);
			}
		} else {
			//Actionの連結時(ActionからのAction遷移)
			//デフォルトのアクションを実行
			findForward = appExecute(appContext);
		}
		
		keyMethodMap = null;

		//JSP又はActionに遷移
		if( findForward instanceof String ) {
		    
			String ff = (String)findForward;
		    
		    // ログアウト
		    if( ff.equals(GS.RC_LOGOUT) ) {
				return new ActionForward(CLOSE_JSP);
			}

		    // 権限エラー
		    if( ff.equals(GS.RC_KENGEN_ERROR) ) {
				return new ActionForward(KEGEN_ERROR_JSP);
			}

		    // クローズ
		    if( ff.equals(GS.RC_CLOSE) ) {
				return new ActionForward(CLOSE_JSP);
			}
		    
			//通常の画面遷移
		    ActionForward af = mapping.findForward(ff);
		    String path = af.getPath();
		    this.appHelp(appContext,ff);
		    List list = new ArrayList();
		    list.add(ff);
		    list.add(path);
			log.write(GS.LOG_INF,CLASSNAME,Function.replaceExpression(FORWARD,list));
		    if(path.indexOf(STR_EVENT)>0) {
			    return af;
		    } else {
		    	//eventパラメータがない場合は前回のeventを初期化
			    af = null;
			    StringBuffer sb = new StringBuffer().append(path);
			    if(path.indexOf(STR_Q)>0){
			    	//xxxxxx.do?AAA=111
			    	sb.append(STR_AMP);
			    	sb.append(STR_EVENT);
				    return new ActionForward(sb.toString());
			    } else {
			    	//xxxxxx.do
			    	sb.append(STR_Q);
			    	sb.append(STR_EVENT);
				    return new ActionForward(sb.toString());
			    }
		    }
		}

		//ファイルのダウンロード
		if( findForward instanceof TempFile ) {
			appContext.getSqlExecuter().disConnect();
			request.setAttribute(GS.DOWNLOADCONTEXT,findForward);
			return new ActionForward(GS.A00_DOWNLOAD+GS.ACTION);
		}
		//その他
		return null;
	}

	/**
	 * ヘルプ画面表示処理
	 */
	private void appHelp(AppContext appContext,String gamen_id) throws Exception{

		UserBean userBean = appContext.getCMN().getUser_bean();
		if(userBean == null){
			return;
		}
		String lang_mode = appContext.getCMN().getComLangMode();
		String helpPath = null;
		String path = null;
		//ヘルプindex.htmlのパス取得
		CommonDbAcc acc = new CommonDbAcc(appContext.getSqlExecuter(),appContext.getLog());
		path = acc.getGazouPath(gamen_id,lang_mode,userBean.getComWorkflowSystemkbn());
		helpPath = Function.replaceExpression(HELP_PATH,path);
		appContext.getRequest().setAttribute(GS.HELP_PATH,helpPath);
	}

	/**
	 * 帳票言語選択画面表示処理
	 */
	private String appDownload(String event,AppContext appContext) throws Exception{
		
		StringBuffer sb = new StringBuffer(event);
		sb.deleteCharAt(event.length() - 1);
		
		if(event.endsWith(DOWNLOAD_JA)){
			((AppPagerActionForm)appContext.getActionForm()).setLangMode(GS.LANG_JA);
		}else if(event.endsWith(DOWNLOAD_EN)){
			((AppPagerActionForm)appContext.getActionForm()).setLangMode(GS.LANG_EN);
		}else{
			return event;
		}

		return sb.toString();
	}

	/**
	 * 査定一覧選択処理
	 */
	private String appSatei(String event,AppContext appContext) throws Exception{
		
		StringBuffer sb = new StringBuffer(event);
		sb.deleteCharAt(event.length() - 1);
		
		appContext.getCMN().setSateiSansyoFlg("1");
		if(event.endsWith("A")){
			appContext.getCMN().setSateiSansyoFlg("1");
		}else if(event.endsWith("B")){
			appContext.getCMN().setSateiSansyoFlg("2");
		}

		return sb.toString();
	}

	/**
	 * ファイルのアップロードを行う。
	 * 
	 * @param inFile アップロードファイルクラス
	 * @return 一時ファイルクラス
	 * 			null/エラー(最大アップロードファイル以上)
	 * @throws Exception
	 */
	public TempFile Upload(FormFile inFile) throws Exception {

		TempFile tmp = null;
		InputStream is = null;
		OutputStream os = null;
	    List list = new ArrayList();

		byte[] buf = new byte[1024];
		int length;

        try {
			if(inFile.getFileSize() > 0) {
				list.add(inFile.getFileName());
				list.add(Integer.toString(inFile.getFileSize()));
        		log.write(GS.LOG_INF,CLASSNAME,Function.replaceExpression(UPLOAD_START,list));
            	tmp = new TempFile(inFile.getFileName());
				is = inFile.getInputStream();
		        os = new FileOutputStream(tmp.getPath());
				while ( (length = is.read(buf)) != -1 ) {
					os.write(buf, 0, length);
				}
				log.write(GS.LOG_INF,CLASSNAME,Function.replaceString(UPLOAD_END,inFile.getFileName()));
        	}

        } finally {
        	buf = null;
        	if(is!=null) {
        		is.close();
        		is = null;
        	}
        	if(os!=null) {
        		os.close();
        		os = null;
        	}
        }
        
		return tmp;
	}
}
