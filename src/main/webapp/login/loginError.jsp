<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<html>
	<head>
		<%@ include file = "../include/jspHeader.jsp" %>
		<%@ include file = "../include/jspUtil.jsp" %>
		<link rel="stylesheet" href="../css/CommonStyle.css" type="text/css">
	</head>
	<body>
		<form>
			<br><br><br><br><br><br>
			<table width="100%" cellpadding="0" style="border:0px;">
				<tr style="border:0px;">
					<%-- ログインエラー --%>
					<td align="center" style="border:0px;">
						<%=i18n.get(GL.SYSTEM_CLOSE)%>
					</td>
				</tr>
			</table>
			<br><br>
		</form>
	</body>
</html>

<%
	session.invalidate();
%>