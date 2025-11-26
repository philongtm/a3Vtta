<%@ page language="java" contentType="text/html; charset=shift_jis"  isThreadSafe="true" %>
<%@ page import="java.util.*" %>
<%@ page import="common.struts.*" %>
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

	AppLocale.setDefault(request);
	JspMessage i18n = new JspMessage(session);
%>

<script>
function closeWin() {
	window.returnValue="<%=GS.ERROR%>";
	if(top.window.name!="SAIKEN"){
		window.open('../close.jsp','SAIKEN','width=10,height=10,left=2000,top=2000');
	}
	window.close();
}
</script>

<html>
<%-- No.804 2008/06/10 新実 エラーメッセージが指定フォントで表示されるよう修正 --%>
<head>
<link rel="stylesheet" href="../../../css/CommonStyle.css" type="text/css">
</head>
<body>
<form>
<br><br><br><br><br><br>
  <table width="100%" cellpadding="0" style="border:0px;">
    <tr style="border:0px;">
      <td align="center" style="border:0px;">
		<%=i18n.get(GL.SYSTEM_STOPTIME2)%>
      </td>
    </tr>
  </table>
<br><br>
<center>
	<input type="button" onclick="closeWin();" value="<%=i18n.get(GL.BTN_CLOSE)%>">
</center>
</form>
</body>
</html>

<%
	session.invalidate();
%>