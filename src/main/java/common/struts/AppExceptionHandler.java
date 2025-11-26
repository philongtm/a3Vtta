/******************************************************************************
著作権情報				:
使用JDK バージョン		: 1.4.2.05
更新履歴
No		日付			修正者			修正内容
******************************************************************************/
package common.struts;

import common.AppContext;
import common.global.GS;
import common.util.Log;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ExceptionHandler;
import org.apache.struts.config.ExceptionConfig;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**
 * 拡張RequestProcessorクラス
 * サーブレットリクエストに対するstrutsコントローラが実行するロジック。
 * SessionBean関係の処理をここで行う。
 * 
 */
public class AppExceptionHandler extends ExceptionHandler {

	/* (非 Javadoc)
	 * @see org.apache.struts.action.ExceptionHandler#execute(java.lang.Exception, org.apache.struts.config.ExceptionConfig, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(
			Exception ex, 
			ExceptionConfig conf,
			ActionMapping mapping, 
			ActionForm form, 
			HttpServletRequest request,
			HttpServletResponse response
			) throws ServletException {

		AppContext app = AppContext.getInstance(request);
		Log appLog = app.getLog();

		if(ex instanceof  SQLException ){
			// ＤＢアクセスエラーが発生しました。
			request.setAttribute("ERRJSP","error2.jsp");
			appLog.write(GS.LOG_ERR,"","■ＤＢアクセスエラー発生");
		} else {
			if( app.getSqlExecuter().isError() ) {
				// ＤＢアクセスエラーが発生しました。
				request.setAttribute("ERRJSP","error2.jsp");
				appLog.write(GS.LOG_ERR,"","■ＤＢアクセスエラー発生");
				app.getSqlExecuter().getConnectException();
				appLog.write(GS.LOG_ERR,"",app.getSqlExecuter().getConnectException());
				return super.execute(app.getSqlExecuter().getConnectException(), conf, mapping, form, request, response);
			}
			// システムエラーが発生しました。
			request.setAttribute("ERRJSP","error1.jsp");
			appLog.write(GS.LOG_ERR,"","■サーブレット例外発生");
		}
		appLog.write(GS.LOG_ERR,"",ex);
		
		return super.execute(ex, conf, mapping, form, request, response);
	}
}