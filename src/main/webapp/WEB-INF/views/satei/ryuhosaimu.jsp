<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>
<link rel="stylesheet" href="<c:url value='/css/Satei.css' />" type="text/css">
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="RyuhosaimuForm" name="02RyuhosaimuForm" type="app.satei.form.RyuhosaimuForm" />
<% 
	Pager pager = RyuhosaimuForm.getPager();
	List list = RyuhosaimuForm.getList();
%>

<script>
	function setFocus(focus) {
		if(focus == 'null' || focus == '') {
		} else {
			document.forms[0].elements[focus].focus();
		}
	}
</script>
</HEAD>
<BODY onload="setFocus('<%= request.getAttribute(GS.FOCUS_FIELD) %>')">
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
<logic:equal name="TorihikisakiBean" property="phase" value="40">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1105A)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="50">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1105B)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="60">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1105C)%></H1>
</logic:equal>
<html:form action="/satei/ryuhosaimu">
<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_SAVE)%>" onclick="doSubmit('preserve')">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
</DIV>

<DIV id="list">
	<DIV class="mainlist">
		<TABLE style="width:100%;border-collapse: collapse;"class="semakuBorderNone">
		<TR class="OC1105BorderNone">
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1105_TORI_CD)%></DIV><BR>
		</TD>
		<TD style="width:12%;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write  name="TorihikisakiBean" property="kanjo_cd" /></DIV><BR>
		</TD>
		<TD style="width:4%;"class="semakuBorderNone"><BR>
		</TD>
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1105_TORI_NM)%></DIV><BR>
		</TD>
		<TD style="width:64%;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV><BR>
		</TD>
		</TR>
		</TABLE>
		
		<TABLE style="width:100%;border-collapse: collapse;"class="semakuBorderNone">
		<TR class="OC1105BorderNone">&nbsp;</TR>
		<TR class="OC1105BorderNone">&nbsp;</TR>
		</TD>
		<TD style="text-align: left;width=11%;border:0px;"><%=i18n.get(GL.OC1105_SAIMUSOUKEI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%></TD>
			<TD style="text-align:right;width=14%;border:0px;">&nbsp;<bean:write name="RyuhosaimuForm" property="saimusoukei" /></TD>
			<TD style="text-align: right;width=21%;border:0px;">
			</TD>
		<TD style="width:8%;"class="semakuBorderNone"><%=i18n.get(GL.COMMON_SHOW)%><BR>
		</TD>
		<TD style="width:8%;"class="semakuBorderNone"><html:select property="view" onchange="doSubmit('view')">
    		<html:optionsCollection name="RyuhosaimuForm" property="showList" value="value" label="key" />
  		</html:select><BR>
		</TD>
		<TD style="width:15%;"class="semakuBorderNone"><logic:notEqual name="RyuhosaimuForm" property="x" value="">
         <%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
            <a href="#" onClick="doSubmit('prevX')"><bean:write name="RyuhosaimuForm" property="x" /></a>
         <%} else {%>
            <a href="#" onClick="doSubmit('prevX')"><bean:write name="RyuhosaimuForm" property="xen" /></a>
         <%}%>   
      </logic:notEqual><BR>
		</TD>
		<TD style="width:15%;"class="semakuBorderNone"><logic:notEqual name="RyuhosaimuForm" property="y" value="">
         <%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
            <a href="#" onClick="doSubmit('nextY')"><bean:write name="RyuhosaimuForm" property="y" /></a>
         <%} else {%>
            <a href="#" onClick="doSubmit('nextY')"><bean:write name="RyuhosaimuForm" property="yen" /></a>
         <%}%>   
      </logic:notEqual><BR>
		</TD>
		<TD style="width:8%;" class="right semakuBorderNone"><%=pager.getLastIndexOfCurrentPage()%>/<%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%><BR>
		</TD>
		</TR>
		<TR class="OC1105BorderNone">
			<TD style="text-align: left;width=11%;border:0px;"><%=i18n.get(GL.OC1105_RYUHOSAIMUKEI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%></TD>
			<TD style="text-align: right;width=14%;border:0px;">&nbsp;<bean:write name="RyuhosaimuForm" property="ryuhosaimukei" /></TD>
			<TD style="text-align: right;width=21%;border:0px;"></TD>
			<TD style="width:8%;"class="semakuBorderNone"></TD>
			<TD style="width:8%;"class="semakuBorderNone"></TD>
			<TD align="right" style="border:0px;width:15%;"></TD>
			<TD colspan="2" style="text-align: right;width=23%;border:0px;"><%=i18n.get(GL.OC1105_IKKATUHANTEI)%>&nbsp;&nbsp;
				<html:checkbox name="RyuhosaimuForm" property="chkIkkatu" value="1" onclick="doSubmit('ikkatu')" />
			</TD>
		</TR>
		</TABLE>
		<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
		<THEAD>
		<TR>
			<TH colspan=2 style="width:31%;"><%=i18n.get(GL.OC1105_KANJO_NM)%></TH>
			<TH colspan=6 style="width:50%;"><%=i18n.get(GL.OC1105_SOSHIKI)%></TH>
			<TH style="width:19%;"class="borderRight"><%=i18n.get(GL.OC1105_KANJO_KAMOKU)%></TH>
		</TR>
		<TR>
			<TH style="width:14%;"><%=i18n.get(GL.OC1105_SHUSI_DT)%></TH>
			<TH colspan=2 style="width:21%;"><%=i18n.get(GL.OC1105_KINGAKU_TOTAL)%><logic:notEqual name="RyuhosaimuForm" property="tuka" value=""><bean:write name="RyuhosaimuForm" property="tuka" /></logic:notEqual></TH>
			<TH style="width:13%;"><%=i18n.get(GL.OC1105_KEIYAKU_NO)%></TH>
			<TH colspan=1 style="width:5%;"><%=i18n.get(GL.OC1105_HANTEI)%></TH>
			<TH colspan=4 style="width:47%;"class="borderRight"><%=i18n.get(GL.OC1105_BIKOU)%></TH>
		</TR>
		</THEAD>
		<TBODY>
		<% if(list != null) { %>
		<nested:iterate name="RyuhosaimuForm" property="list" indexId="idx">
		<TR>
			<TD colspan=2 style="width:31%;"><bean:write name="TorihikisakiBean" property="kanjo_nm" />&nbsp;</TD>
			<TD colspan=6 style="width:50%;"><nested:write property="soshiki" />&nbsp;</TD>
			<TD style="width:19%;border-right-color:#000000;"><nested:write property="kanjo_kamoku_nm" />&nbsp;</TD>
		</TR>
		<TR>
			<TD style="width:14%;border-bottom-color:#000000;"><nested:write property="syusi_yoteibi" />&nbsp;</TD>
			<TD colspan=2 style="width:21%;border-bottom-color:#000000;" class="right"><nested:write property="kingaku_kei" />&nbsp;</TD>
			<TD style="width:13%;border-bottom-color:#000000;"><nested:write property="keiyaku_denpyo_no" />&nbsp;</TD>
			<TD colspan=1 style="width:5%;border-bottom-color:#000000;" class="center">
			<nested:checkbox property="ryuhosaimu" value="1"/>&nbsp;
			</TD>
			<TD colspan=4 style="width:47%;height:3.5em;border-bottom-color:#000000;border-right-color:#000000;"><nested:textarea property="biko" style="width:100%;height:100%;"/>&nbsp;</TD>
		</TR>
		</nested:iterate>
		<% } %>
		</TBODY>
		</TABLE>
	</DIV>

</html:form>
</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>