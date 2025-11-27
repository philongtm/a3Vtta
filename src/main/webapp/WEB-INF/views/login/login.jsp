<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>
<link rel="stylesheet" href="<c:url value='/css/Login.css' />" type="text/css">
<script>
function upperCase(obj) {
	obj.value=obj.value.toUpperCase();
}
function checkOpener() {
	if(window.name != 'SAIKEN'){
		location.href = '../index.html';
	}
}
</script>
</HEAD>
<body id="login" onload="checkOpener();">
<CENTER>

<%-- ヘッダ部分 --%>
<DIV id="loginMain">

<DIV id="head">
	<%-- 言語切替え --%>
	<DIV class="headR">
		<a href="#" name="lnkJa" onClick="doSubmit('langJa');">Japanese</a>&nbsp;/&nbsp;<a href="#" name="lnkEn" onClick="doSubmit('langEn');">English</a>
	</DIV>
	<%-- ロゴ画像--%>
	<DIV class="headL">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" class="IMG1">
	 	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<%=contextPath%>/image/<%=i18n.get(GL.IMG_TITLE)%>.gif" class="IMG2">
	</DIV>
</DIV>

<BR>
<%-- コンテンツ部分 --%>
<DIV id="loginContents">
<DIV id="loginList">
<form action="<c:url value='/login/login.do' />" focus="userId">
<input type="hidden" name="txtKbnLang"/>
<input type="hidden" name="event"/>
	<table align="left">
	<tr>
		<td><%=i18n.get(GL.OS1101_USERID)%></td>
		<td><input type="text" name="userId" onblur="upperCase(userId)" size="18" maxlength="8"/></td>
	</tr>
	<tr>
  		<td><%=i18n.get(GL.OS1101_PASSWORD)%></td>
 		<td><input type="password" name="password" size="18" style="font-family:Arial; width:104px;" redisplay="false"/></td>
	</tr>
	<tr>
  		<td></td>
  		<td>
  		<input type="button" class="button" value="<%=i18n.get(GL.BTN_LOGIN)%>" onClick="doSubmit('login')">&nbsp;&nbsp;&nbsp;
  		<input type="button" class="button" value="<%=i18n.get(GL.BTN_CANCEL)%>" onClick="doSubmit('logout')">
  		</td>
	</tr>
	</table>
</form>
</DIV>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
<%
	session.invalidate();
%>