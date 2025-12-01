<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="SyoninSyosaiForm" name="02SyoninSyosaiForm" type="app.satei.form.SyoninSyosaiForm"/>
</HEAD>
<BODY onload="">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<html:form action="/satei/syonin_syosai">
<logic:equal name="TorihikisakiBean" property="phase" value="40">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1107A)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="50">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1107B)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="60">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1107C)%></H1>
</logic:equal>

<DIV id="submenu">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')">
		<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syoninExecute')">
		<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')">
		<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
	<%} else {%>
		<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')"style="WIDTH: 90px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syoninExecute')"style="WIDTH: 90px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')"style="font-size:10px;WIDTH: 90px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')"style="WIDTH: 90px; HEIGHT: 22px">
	<%}%>
</DIV>
<DIV id="list">
	<DIV class="headlist">
		<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV><DIV class="ReadOnlybox" style="width=5%; margin-bottom:2px;"><bean:write name="TorihikisakiBean" property="sateikaisya_cd" /></DIV>
		&nbsp;&nbsp;&nbsp;
		<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=i18n.get(GL.OC1107_SOSHIKI)%></DIV><DIV class="ReadOnlybox" style="width=80%; margin-bottom:2px;"><bean:write name="TorihikisakiBean" property="soshiki" /></DIV>
		<br>
		<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OC1107_KANJO_CD)%></DIV><DIV class="ReadOnlybox" style="width=9%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
		&nbsp;&nbsp;&nbsp;
		<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OC1107_KANJO_NM)%></DIV><DIV class="ReadOnlybox" style="width=68%; word-break:break-all; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV>
	</DIV>

	<DIV class="mainlist">
		<logic:equal name="SyoninSyosaiForm" property="tabValue" value="1">
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_gaiyo')"><%=i18n.get(GL.TAB_TORI_GAIYO)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_kbn')"><%=i18n.get(GL.TAB_TORI_KBN)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('hikiate_hantei')"><%=i18n.get(GL.TAB_HIKIATE_HANTEI)%></a></DIV>
			<DIV id="tab"><span><%=i18n.get(GL.TAB_SAIKEN_MEISAI)%></span></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('ryuho_saimu')"><%=i18n.get(GL.TAB_RYUHO_SAIMU)%></a></DIV>
			<iframe src="../common/saiken_meisai.jsp" width=100% height=700px></iframe>
		</logic:equal>
		<logic:equal name="SyoninSyosaiForm" property="tabValue" value="2">
			<DIV id="tab"><span><%=i18n.get(GL.TAB_TORI_GAIYO)%></span></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_kbn')"><%=i18n.get(GL.TAB_TORI_KBN)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('hikiate_hantei')"><%=i18n.get(GL.TAB_HIKIATE_HANTEI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('saiken_meisai')"><%=i18n.get(GL.TAB_SAIKEN_MEISAI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('ryuho_saimu')"><%=i18n.get(GL.TAB_RYUHO_SAIMU)%></a></DIV>
			<iframe src="../common/torihikisaki_gaiyo.jsp" width=100% height=700px></iframe>
		</logic:equal>
		<logic:equal name="SyoninSyosaiForm" property="tabValue" value="3">
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_gaiyo')"><%=i18n.get(GL.TAB_TORI_GAIYO)%></a></DIV>
			<DIV id="tab"><span><%=i18n.get(GL.TAB_TORI_KBN)%></span></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('hikiate_hantei')"><%=i18n.get(GL.TAB_HIKIATE_HANTEI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('saiken_meisai')"><%=i18n.get(GL.TAB_SAIKEN_MEISAI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('ryuho_saimu')"><%=i18n.get(GL.TAB_RYUHO_SAIMU)%></a></DIV>
			<iframe src="../common/torihikisaki_kubun_hantei.jsp" width=100% height=650px></iframe>
		</logic:equal>
		<logic:equal name="SyoninSyosaiForm" property="tabValue" value="4">
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_gaiyo')"><%=i18n.get(GL.TAB_TORI_GAIYO)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_kbn')"><%=i18n.get(GL.TAB_TORI_KBN)%></a></DIV>
			<DIV id="tab"><span><%=i18n.get(GL.TAB_HIKIATE_HANTEI)%></span></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('saiken_meisai')"><%=i18n.get(GL.TAB_SAIKEN_MEISAI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('ryuho_saimu')"><%=i18n.get(GL.TAB_RYUHO_SAIMU)%></a></DIV>
			<iframe src="../common/hikiatekin_hantei.jsp" width=100% height=700px></iframe>
		</logic:equal>
		<logic:equal name="SyoninSyosaiForm" property="tabValue" value="5">
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_gaiyo')"><%=i18n.get(GL.TAB_TORI_GAIYO)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('tori_kbn')"><%=i18n.get(GL.TAB_TORI_KBN)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('hikiate_hantei')"><%=i18n.get(GL.TAB_HIKIATE_HANTEI)%></a></DIV>
			<DIV id="tab"><a href="#" onclick="doSubmit('saiken_meisai')"><%=i18n.get(GL.TAB_SAIKEN_MEISAI)%></a></DIV>
			<DIV id="tab"><span><%=i18n.get(GL.TAB_RYUHO_SAIMU)%></span></DIV>
			<iframe src="../common/ryuho_saimu.jsp" width=100% height=700px></iframe>
		</logic:equal>
	</html:form>
	</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>