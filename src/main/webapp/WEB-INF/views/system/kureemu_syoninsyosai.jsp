<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
</HEAD>
<BODY onload="">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<H1 class="title01"><%=i18n.get(GL.TITLE_OS3105)%></H1>

<DIV id="submenu">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
	<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')">
	<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syonin')">
	<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
	<%}else{%>
	<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')"style="WIDTH: 90px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syonin')"style="WIDTH: 90px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')"style="font-size:10px;WIDTH: 90px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')"style="WIDTH: 90px; HEIGHT: 22px">
	<%}%>
</DIV>

<DIV id="list">
<html:form action="/system/kureemusyoninSyosai">
	
	<DIV class="headlist">
		<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV><DIV class="ReadOnlybox" style="width=5%; margin-bottom:2px;"><bean:write name="TorihikisakiBean" property="sateikaisya_cd" /></DIV>
		&nbsp;&nbsp;&nbsp;
		<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=i18n.get(GL.OB1105_SOSHIKI)%></DIV><DIV class="ReadOnlybox" style="width=80%; margin-bottom:2px;"><bean:write name="TorihikisakiBean" property="soshiki" /></DIV>
		<br>
		<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1105_KANJO_CD)%></DIV><DIV class="ReadOnlybox" style="width=9%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
		&nbsp;&nbsp;&nbsp;
		<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1105_KANJO_NM)%></DIV><DIV class="ReadOnlybox" style="width=68%; word-break:break-all; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV>
		<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1105_SHINYOUKAKUDUKE)%></DIV><DIV class="ReadOnlybox" style="width=5%; margin-top:2px;text-align: center;"><bean:write name="TorihikisakiBean" property="sinyoktk" /></DIV>
	</DIV>

	<DIV class="mainlist">
	<DIV id="tab"><span><%=i18n.get(GL.TITLE_OZ6105)%></span></DIV>
	<iframe src="../common/saiken_meisai.jsp" width=100% height=800px>

	</DIV>

</html:form>
</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>