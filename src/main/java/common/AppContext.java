/******************************************************************************
著作権情報				:
プロジェクト			: SAMPLE
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
001		2008/01/09		SSC				1.5次版に修正を施し流用
002		2008/05/13		SSC				1.5次機能吸収のため修正
003		2009/05/28		SSC				1.5次版権限取得処理追加
004     2009/11/13		SSC				課題No.09 保有文書添付仕様変更
******************************************************************************/
package common;

import app.MeisaisyosaiBean;
import app.SessionData;
import app.SessionDataZen;
import app.TorihikisakiBean;
import app.UserBean;
import common.db.CommonDbAcc;
import common.db.SqlExecuter;
import common.global.GS;
import common.struts.AppLocale;
import common.util.Function;
import common.util.Log;
import common.struts.adapter.action.ActionForm;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ＡＰＰコンテキスト
 * 
 */
public class AppContext {

	private static String tmpDir = null;
	private static String helpDir = null;
	private ServletContext servletContext = null;
	private HttpServletRequest request = null;
	private HttpSession session = null;
	private HttpServletResponse response = null;
	private ActionForm actionForm = null;
	private SqlExecuter sqlExecuter = null;
	private Log log = null;
	private String focusField = "";

	/**
	 * @return AppContext を戻します。
	 */
	public static AppContext getInstance(HttpServletRequest request) {
		return (AppContext)request.getAttribute(GS.APPCONTEXT);
	}
	
	/* (非 Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		this.destroy();
	}

	/**
	 * コンストラクタ
	 * @param servletContext
	 * @param request
	 * @param response
	 */
	public AppContext(
			ServletContext servletContext,
			HttpServletRequest request, 
			HttpServletResponse response ) {
		this.servletContext = servletContext;
		this.request = request;
		this.session = request.getSession();
		this.response = response;
		log = new Log(this);
		if(tmpDir==null) {
			tmpDir = getRealPath(GS.TMPDIR);
		}
		if(helpDir==null) {
			helpDir = getRealPath(GS.HELPDIR);
		}
	}

	/**
	 * @return log を戻します。
	 */
	public Log getLog() {
		return log;
	}
	
	/**
	 * @return sqlExecuter を戻します。
	 */
	public SqlExecuter getSqlExecuter() {
	    if(sqlExecuter == null) {
	        sqlExecuter = new SqlExecuter(log);
	    }
		return sqlExecuter;
	}
	
	/**
	 * sqlExecuterオブジェクトを破棄。
	 */
	public void destroy() {
	    if(sqlExecuter != null) {
	        sqlExecuter.destroy();
	    }
	}
	
	/**
	 * @return app.SessionDataZenを生成後、戻します。
	 */
	public SessionDataZen getCMNZen() throws SQLException {
		
		//1.5次版機能超無理矢理吸収処理開始!!
		
		HttpSession session;
		SessionData CMN = null;
		
		if( (request!=null) && ((session=request.getSession())!=null) ) {
			CMN = (SessionData)session.getAttribute(GS.CMN_SES_NM);
		} else {
			return null;
		}
		
		TorihikisakiBean toriBean = CMN.getTori_bean();
		UserBean userBean = CMN.getUser_bean();
		MeisaisyosaiBean meisaiBean = CMN.getSyosai_bean();
		SessionDataZen CMNZen = new SessionDataZen();
		String tmpBunrui2 = null;
		String ar_tmp_bunrui2[] = null;
		
		//システム情報設定
		CMNZen.setComLangMode(new String(CMN.getComLangMode()));
		if(GS.EMPTY_CHARCTER.equals(Function.trim(CMN.getSyosai_returnId()))){
			CMNZen.setReturnId(new String(Function.trim(CMN.getReturn_gamenId())));
			CMNZen.setOrgReturnId(GS.EMPTY_CHARCTER);
		}else{
			CMNZen.setReturnId(new String(Function.trim(CMN.getSyosai_returnId())));
			CMNZen.setOrgReturnId(new String(Function.trim(CMN.getReturn_gamenId())));
		}
		
		//ユーザ情報設定～所属等～
		CMNZen.setComInitUserId(new String(Function.trim(userBean.getComUserId())));
		if(!GS.EMPTY_CHARCTER.equals(Function.trim(userBean.getComDaiko_userId()))){
			CMNZen.setComUserId(new String(Function.trim(userBean.getComDaiko_userId())));
			CMNZen.setDaikoUserId(new String(Function.trim(userBean.getComDaiko_userId())));
		}else{
			CMNZen.setComUserId(new String(Function.trim(userBean.getComUserId())));
			CMNZen.setDaikoUserId(new String(Function.trim(userBean.getComUserId())));
		}
		CMNZen.setComTanto_User_Nm(new String(Function.trim(userBean.getComUser_Nm())));
		CMNZen.setComTanto_User_Nm_En(new String(Function.trim(userBean.getComUser_Nm_En())));
		CMNZen.setComSoshikiCd(new String(Function.trim(userBean.getComSyozokuSoshikiCd())));
		CMNZen.setComSoshiki_Nm(new String(Function.trim(userBean.getComSyozokuSoshiki_Nm())));
		CMNZen.setComSoshiki_Nm_En(new String(Function.trim(userBean.getComSyozokuSoshiki_Nm_En())));
		CMNZen.setComKaishaCd(new String(Function.trim(userBean.getComKaishaCd())));
		CMNZen.setComSateiKaishaCd(new String(Function.trim(userBean.getComWorkflowSateikaisya_cd())));
		CMNZen.setComBumonCd(new String(Function.trim(userBean.getComSyozokuBunrui2())));
		CMNZen.setComBuCd(new String(Function.trim(userBean.getComSyozokuBuCd())));
		CMNZen.setComKaCd(new String(Function.trim(userBean.getComSyozokuBunrui3())));
		CMNZen.setComEmailAddr(new String(Function.trim(userBean.getComEmailAddr())));
		//1.5次版担当部門コード処理
		CMNZen.InitComTantoCd();
		tmpBunrui2 = Function.removeSingle(userBean.getComSansyoBunrui2());
		ar_tmp_bunrui2 = Function.StrSplitToken(Function.trim(tmpBunrui2),",");
		if(ar_tmp_bunrui2 != null){
			for(int i=0;i < ar_tmp_bunrui2.length;i++){
				CMNZen.setComTantoCd(ar_tmp_bunrui2[i]);
			}
		}
		//ユーザ情報設定～権限等～
		CommonDbAcc dbAcc = new CommonDbAcc(this.getSqlExecuter(),this.getLog());
		dbAcc.getKokunaiKengen(userBean,CMNZen);

		//取引先情報設定
		if(toriBean != null){
			CMNZen.setC_phase(new String(Function.trim(toriBean.getC_phase())));
			CMNZen.setPhase(new String(Function.trim(toriBean.getPhase())));
			CMNZen.setStatus(new String(Function.trim(toriBean.getStatus())));
			CMNZen.setKanjo_cd(new String(Function.trim(toriBean.getKanjo_cd())));
			CMNZen.setKanjo_nm(new String(Function.trim(toriBean.getKanjo_nm())));
			//課題No.09
			//追加開始
			CMNZen.setSatei_ki(new String(Function.trim(toriBean.getSatei_ki())));
			//追加完了
			CMNZen.setYm(new String(Function.trim(toriBean.getTaisyo_ym())));
			CMNZen.setSatei_anken_no(new String(Function.trim(toriBean.getAnken_no())));
			CMNZen.setTairyu_anken_no(new String(Function.trim(toriBean.getAnken_no())));
			CMNZen.setLastAnken_no(new String(Function.trim(toriBean.getLast_anken_no())));
			CMNZen.setDuns_no(new String(Function.trim(toriBean.getTogo_tori_cd())));
			CMNZen.setSystem_kbn(new String(Function.trim(toriBean.getSystem_kbn())));
			CMNZen.setMise_cd(new String(Function.trim(toriBean.getMise_cd())));
			CMNZen.setAnken_satei_kaisya_cd(new String(Function.trim(toriBean.getSateikaisya_cd())));
			CMNZen.setCountry_nm(new String(Function.trim(toriBean.getSyozaikoku())));
			CMNZen.setJiyuu_nm(new String(Function.trim(toriBean.getJiyu_nm())));
			CMNZen.setSoshiki_nm(new String(Function.trim(toriBean.getSoshiki_nm())));
			CMNZen.setSatei_gamen(new String(Function.trim(toriBean.getSatei_toroku_gamen())));
			CMNZen.setTuuka(new String(Function.trim(toriBean.getTuuka_cd())));
			CMNZen.setAnkenKtk(new String(Function.trim(toriBean.getSinyoktk())));
			CMNZen.setSatei_bumon_cd(new String(Function.trim(toriBean.getBunrui2())));
			CMNZen.setSatei_bu_cd(new String(Function.trim(toriBean.getBu_cd())));
			CMNZen.setHanki_sihanki_kbn(new String(Function.trim(toriBean.getHanki_sihanki_kbn())));
			CMNZen.setSyoriCnt(new String(Function.trim(toriBean.getSyori_kaisu())));
			if(meisaiBean != null){
				CMNZen.setAnken_no_eda(new String(Function.trim(meisaiBean.getAnken_no_eda())));
				CMNZen.setBunsyo_no(new String(Function.trim(meisaiBean.getBunsyo_no())));
				if(!(GS.EMPTY_CHARCTER.equals(Function.trim(meisaiBean.getTenpu_anken_no())))){
					CMNZen.setTairyu_anken_no(new String(Function.trim(meisaiBean.getTenpu_anken_no())));
					CMNZen.setAnken_no_eda(new String(Function.trim(meisaiBean.getTenpu_anken_no_eda())));
				}
			}
		}
		
		//セッションスコープに設定
		session.setAttribute(GS.CMN_SES_NM_ZEN,CMNZen);
		
		return CMNZen;
	}	

	/**
	 * @return app.SessionDataZenを戻します。
	 */
	public SessionDataZen getCMNZenRe() {
		return (SessionDataZen)session.getAttribute(GS.CMN_SES_NM_ZEN);
	}	

	/**
	 * @return app.SessionData を戻します。
	 */
	public SessionData getCMN() {
		HttpSession session;
		if( (request!=null) && ((session=request.getSession())!=null) ) {
			return (SessionData)session.getAttribute("app.SessionData");
		} else {
			return null;
		}
	}	

	/**
	 * @return actionForm を戻します。
	 */
	public ActionForm getActionForm() {
		return actionForm;
	}
	
	/**
	 * @param actionForm actionForm を設定。
	 */
	public void setActionForm(ActionForm actionForm) {
		this.actionForm = actionForm;
	}
	
	/**
	 * @param actionFormName actionForm をrequestから削除。
	 */
	public void removeAttribute(String actionFormName) {
		request.removeAttribute(actionFormName);
	}
	
	/**
	 * @param actionFormName セッションスコープにactionFormを設定。
	 */
	public void setSessionActionForm(String actionFormName,ActionForm form) {
		session.setAttribute(actionFormName,form);
	}
	
	/**
	 * @param actionFormName セッションスコープにactionFormを設定。
	 */
	public void setSessionLangMode(String langKey,String langMode) {
		session.setAttribute(langKey,langMode);
	}

	/**
	 * @param actionFormName セッションスコープよりactionFormを取得。
	 */
	public ActionForm getSessionActionForm(String actionFormName) {
		return (ActionForm)session.getAttribute(actionFormName);
	}

	/**
	 * @param actionFormName actionFormNameのActionForm以外をsessionから削除。
	 */
	public void removeActionFormExcept(String actionFormName) {
		this.getCMN().setReturn_gamenId(GS.EMPTY_CHARCTER);
		this.getCMN().setTab_riyou_gamenId(GS.EMPTY_CHARCTER);
		this.getCMN().setSyosai_returnId(GS.EMPTY_CHARCTER);
		String sesName = null;
		Enumeration attrNames = session.getAttributeNames();

		while(attrNames.hasMoreElements()){
			sesName = (String)attrNames.nextElement();
			if(actionFormName.equals(sesName) || GS.LANG.equals(sesName) || GS.RB.equals(sesName)
					|| GS.LOCALE.equals(sesName) || GS.APP_SESSION.equals(sesName)
					|| GS.LOGINFORM.equals(sesName) || GS.MENUFORM.equals(sesName)){
				continue;
			}else{
				session.removeAttribute(sesName);
			}
		}
	}

	/**
	 * ActionFormをsessionから削除。
	 */
	public void removeActionFormAll() {
		this.getCMN().setReturn_gamenId(GS.EMPTY_CHARCTER);
		this.getCMN().setTab_riyou_gamenId(GS.EMPTY_CHARCTER);
		this.getCMN().setSyosai_returnId(GS.EMPTY_CHARCTER);

		String sesName = null;
		Enumeration attrNames = session.getAttributeNames();
		while(attrNames.hasMoreElements()){
			sesName = (String)attrNames.nextElement();
			if(GS.LANG.equals(sesName) || GS.RB.equals(sesName)
					|| GS.LOCALE.equals(sesName) || GS.APP_SESSION.equals(sesName)
					|| GS.LOGINFORM.equals(sesName) || GS.MENUFORM.equals(sesName)){
				continue;
			}else{
				session.removeAttribute(sesName);
			}
		}
	}

	/**
	 * @param actionFormName actionForm をsessionから削除。
	 */
	public void removeActionForm(String actionFormName) {
		request.getSession().removeAttribute(actionFormName);
	}

	/**
	 * @param actionFormName actionFormNameのパターンのActionForm以外をsessionから削除。
	 */
	public void removeActionFormPattern(String actionFormName) {
		String sesName = null;
		Enumeration attrNames = session.getAttributeNames();
		while(attrNames.hasMoreElements()){
			sesName = (String)attrNames.nextElement();
			if(actionFormName.equals(sesName) || GS.LANG.equals(sesName) || GS.RB.equals(sesName)
					|| GS.LOCALE.equals(sesName) || GS.APP_SESSION.equals(sesName)
					|| GS.LOGINFORM.equals(sesName) || GS.MENUFORM.equals(sesName)
					|| Function.matches(sesName,actionFormName)){
				continue;
			}else{
				session.removeAttribute(sesName);
			}
		}
	}

	/**
	 * @return request を戻します。
	 */
	public HttpServletRequest getRequest() {
		return request;
	}
	
	/**
	 * @return session を戻します。
	 */
	public HttpSession getSession() {
		return session;
	}

	/**
	 * @return response を戻します。
	 */
	public HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * ブラウザに表示するメッセージを設定します。
	 * 
	 * @param msgCode メッセージ
	 */
	public void setMessage(String message) {
		request.setAttribute(GS.MESSAGECONTEXT,message);
	}
	
	/**
	 * ブラウザに表示するエラーメッセージを設定します。
	 * 
	 * @param msgCode メッセージコード
	 */
	public void setMsgCode(String msgCode) {
		if( msgCode == null) {
			request.removeAttribute(GS.MESSAGECONTEXT);
		} else {
			request.setAttribute(GS.MESSAGECONTEXT,getMsg(msgCode));
		}
	}
	
	/**
	 * メッセージの有無を判定
	 * @return true/メッセージ有り false/メッセージ無し
	 */
	public boolean haveMessage() {
		return (request.getAttribute(GS.MESSAGECONTEXT) != null);
	}

	/**
	 * ブラウザに表示するエラーメッセージを設定します。
	 * インデックス0　→　メッセージ取得用キー
	 * インデックス0以外　→　置換文字列取得用キー
	 * @param List
	 * 		　 メッセージ取得用キー格納リスト
	 */
	public void setMsgCode(List list) throws Exception {
		String expression = getMsg((String)list.get(0));
		int j = 0;
		for(int i=1;i<list.size();i++){
			list.set(j,getMsg((String)list.get(i)));
			j++;
		}
		list.remove(j);
		String msg;
		msg = Function.replaceExpression(expression,list);
		request.setAttribute( GS.MESSAGECONTEXT, msg );
	}

	/**
	 * ブラウザに表示するエラーメッセージを設定します。
	 * @param msgCode1 メッセージ取得用キー
	 * @param msgCode2 置換文字列取得用キー
	 */
	public void setMsgCode(String msgCode1,String msgCode2) throws Exception {
		String expression = getMsg(msgCode1);
		String strRep = getMsg(msgCode2);
		String msg = Function.replaceString(expression,strRep);
		request.setAttribute( GS.MESSAGECONTEXT, msg );
	}

	/**
	 * ブラウザに表示するエラーメッセージを設定します。
	 * @param msgCode メッセージ取得用キー
	 * @param msg	   置換文字列
	 */
	public void setMsgCd(String msgCode,String msg) throws Exception {
		String expression = getMsg(msgCode);
		String message = Function.replaceString(expression,msg);
		request.setAttribute( GS.MESSAGECONTEXT,message);
	}

	/**
	 * メッセージプロパティから日本語または英語のメッセージを取得します。
	 * 
	 * @param msgCode メッセージコード
	 */
	public String getMsg(String msgCode) {
		String msg = null;
		try {
			ResourceBundle rb = AppLocale.getResourceBundle(request.getSession());
			msg = rb.getString(msgCode);
		} catch(Exception e){}
		
		if( msg == null ) {
			msg = msgCode;
		}
		return msg;
	}
	
	/**
	 * 仮想パスに対応する実際のパスを取得する。
	 * @param path 仮想パス
	 * @return 絶対パス
	 */
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	/**
	 * リクエストされたURIのうち、リクエストのコンテキストを指す部分を返す。 
	 * <pre>
	 * コンテキストパスは通常リクエストURIの最初に来ます。
	 * コンテキストパスは "/" から始まりますが、"/" では終わりません。
	 * </pre>
	 * @return リクエストされたURIのうち、リクエストのコンテキストを指す部分
	 */
	public String getContextPath() {
		return request.getContextPath();
	}
	
	/**
	 * @return tmpDir を戻します。
	 */
	public static String getTmpDir() {
		return tmpDir;
	}
	
	/**
	 * @return helpDir を戻します。
	 */
	public static String getHelpDir() {
		return helpDir;
	}

	/**
	 * @return focusField を戻します。
	 */
	public String getFocusField() {
		return focusField;
	}
	/**
	 * @param focusField focusField を設定。
	 */
	public void setFocusField(String focusField) {
		this.focusField = focusField;
		request.setAttribute(GS.FOCUS_FIELD,this.focusField);
	}
}