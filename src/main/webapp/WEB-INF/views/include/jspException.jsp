<%@ page contentType="text/html;charset=Windows-31J" isErrorPage="true" isThreadSafe="true" %>
<%@ page import="java.util.*" %>
<%@ page import="common.*" %>
<%@ page import="common.global.*" %>
<%@ page import="common.util.*" %>

<%
	/* クライアント側の「キャッシュ」を無効化 */
	Calendar objCal1=Calendar.getInstance();
	Calendar objCal2=Calendar.getInstance();
	objCal2.set(1970,0,1,0,0,0);
	response.setDateHeader("Last-Modified",objCal1.getTime().getTime());
	response.setDateHeader("Expires",objCal2.getTime().getTime());
	response.setHeader("Pragma","no-cache,must-revalidate");
	response.setHeader("Cache-Control","no-cache");
%>

<%
	JspMessage text = new JspMessage(session);
	AppContext app = AppContext.getInstance(request);

	Log log;
	if( (app==null) || ((log=app.getLog())==null) ){
		log = new Log(null);
		//log.setFilename(request.getRemoteHost());
	}
	log.write(GS.LOG_ERR,"","■ＪＳＰ例外発生");
	log.write(GS.LOG_ERR,"",(Exception)exception);
	/* AppActionServletでlog.close()が行われる */
%>
<script>
	if(top.window.name=="SAIKEN"){
		top.location.replace('<%=GS.WEB_INCLUDE+"error1.jsp"%>');
	} else {
		location.replace('<%=GS.WEB_INCLUDE+"error1.jsp"%>');
	}
</script>