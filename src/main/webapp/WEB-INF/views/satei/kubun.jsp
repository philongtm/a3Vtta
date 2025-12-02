<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>
<link rel="stylesheet" href="<c:url value='/css/Satei.css' />" type="text/css">
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="KubunForm" name="02KubunForm" type="app.satei.form.KubunForm" />
<script>
	function setFocus(focus) {
		var form = document.forms[0];
		if(focus == 'null' || focus == '') {
		} else if(focus == 'seijo' || focus == 'KubunForm0') {
	      document.getElementById("iframe").contentWindow.document.forms[0].elements[focus].focus();
		} else {
			form.elements[focus].focus();
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
<logic:equal name="TorihikisakiBean" property="phase" value="40">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1103A)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="50">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1103B)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="60">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1103C)%></H1>
</logic:equal>

<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%>
<BR><BR><BR>
<%}%>
<html:form action="/satei/kubun">

<DIV id="submenu">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('forward')" style="WIDTH: 70px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sendBack')" style="WIDTH: 70px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('firstPreserve')" style="WIDTH: 70px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_TOP)%>" onclick="doSubmit('toHead')" style="WIDTH: 70px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_NEXT_SCREEN)%>" onclick="doSubmit('next')" style="WIDTH: 70px; HEIGHT: 22px">
		<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('back')" style="WIDTH: 70px; HEIGHT: 22px">
	<%} else {%>
		<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('forward')">
		<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sendBack')">
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('firstPreserve')">
		<input type="button" value="<%=i18n.get(GL.BTN_TOP)%>" onclick="doSubmit('toHead')">
		<input type="button" value="<%=i18n.get(GL.BTN_NEXT_SCREEN)%>" onclick="doSubmit('next')">
		<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('back')"><BR><BR>
	<%}%>
</DIV>
<DIV id="list">
<DIV class="mainlist">
<html:hidden property="chkFlgSeijo" />
<html:hidden property="chkFlgYochui" />

<html:hidden property="chkFlgTyoka" />
<html:hidden property="chkFlgKanwa" />
<html:hidden property="chkFlgEntai" />

<html:hidden property="chkFlgHasanho" />
<html:hidden property="chkFlgKaishaho" />
<html:hidden property="chkFlgKoseho" />
<html:hidden property="chkFlgSaiseho" />
<html:hidden property="chkFlgShobun" />
<html:hidden property="chkFlgSonota" />
	<TABLE class="OC1103BorderNone" style="width:100%;">
		<TR>
		<TD style="width:8%;" class="OC1103headerContents"><DIV class="dottitle"><%=i18n.get(GL.OC1103_KANJO)%></DIV><BR>
		</TD>
		<TD colspan="2" style="width:11%;" class="OC1103headerContents"><DIV class="ReadOnlybox" style="width:100%;"><bean:write  name="TorihikisakiBean" property="kanjo_cd" /></DIV>
		</TD>
		<TD style="width:10%;" class="OC1103headerContents">
		</TD>
		<TD style="width:8%;" class="OC1103headerContents"><DIV class="dottitle"><%=i18n.get(GL.OC1103_KANJO_NM)%></DIV><BR>
		</TD>
		<TD colspan="4" class="OC1103headerContents" style="width:33%;"><DIV class="ReadOnlybox" style="width:95%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" style="width:7%;"><DIV class="dottitle"><%=i18n.get(GL.OC1103_SYOZAIKOKU)%></DIV><BR>
		</TD>
		<TD colspan="2" class="OC1103headerContents" style="width:25%;"><DIV class="ReadOnlybox" style="width:100%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="syozaikoku" /></DIV><BR>
		</TD>
		</TR>
		
		<TR>
		<TD class="OC1103headerContents" ><DIV class="dottitle"><%=i18n.get(GL.OC1103_DUNS_NO)%></DIV><BR>
		</TD>
		<TD colspan="3" class="OC1103headerContents" style="text-align:left;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="TorihikisakiBean" property="togo_tori_cd"  /></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" ><DIV class="dottitle"><%=i18n.get(GL.OC1103_SYOZAITI)%></DIV><BR>
		</TD>
		<TD colspan="7" class="OC1103headerContents">
			<DIV class="ReadOnlybox" style="width:100%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="syozaichi" /></DIV><BR>
		</TD>
		</TR>
		
		<TR>
		<%-- IT037対応 --%>
		<TD class="OC1103headerContents" ><DIV class="dottitle"><%=i18n.get(GL.OC1103_TAIRYU_KBN)%></DIV><BR>
		</TD>
		<TD style="width:5%;" class="OC1103headerContents" ><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="KubunForm" property="txtKbnTairyu" /></DIV><BR>
		</TD>
		<TD colspan="2" class="OC1103headerContents" ><DIV class="ReadOnlybox" style="width:95%;"><bean:write name="KubunForm" property="txtKbnTairyuNm" /></DIV><BR>
		</TD>
		<%-- IT037ここまで --%>
		<TD class="OC1103headerContents" ><DIV class="dottitle"><%=i18n.get(GL.OC1103_KTK)%></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" style="width:10%;"><DIV class="ReadOnlybox" style="width:65px;text-align:center;"><bean:write name="TorihikisakiBean" property="sinyoktk" /></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" style="width:6%;"><DIV class="dottitle"><%=i18n.get(GL.OC1103_OYA_KAISHA)%></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" style="width:6%;"><DIV class="ReadOnlybox" style="width:40px;text-align:center;">
		<bean:write name="TorihikisakiBean" property="oya_ktk"/></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" colspan="3" style="width:29%;"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="oya_business_nm"/></DIV><BR>
		</TD>
		<TD class="OC1103headerContents" style="width:14%;" class="center">
			<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="oya_ittai_dokuritu"/></DIV><BR>
		</TD>
		</TR>		
	</TABLE>
	<TABLE class="OC1103BorderNone" style="width:60%;">
		<TR>
		<TD class="OC1103headerContents" style="width:25%;"><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1103_TORI_KBN_HANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
		</TD>
		<TD class="OC1103ToriVal">
		<html:select property="kbnToriSelect" onchange="doSubmit('changetori')">
			<html:optionsCollection name="KubunForm" property="toriSelectList" value="value" label="key" />
		</html:select>
		</TD>
		</TR>
	</TABLE>
	<div style="line-height:5pt">&nbsp;</div>
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
	<logic:equal name="KubunForm" property="currentTab" value="1">
		<DIV id="tab"><span><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="2">
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="3">
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>

	<%} else {%>

	<logic:equal name="KubunForm" property="currentTab" value="1">
		<DIV id="tab" style="font-size:8pt;"><span><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="2">
		<DIV id="tab" style="font-size:8pt;"><span id="disable"><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="3">
		<DIV id="tab" style="font-size:8pt;"><span id="disable"><%=i18n.get(GL.TAB_SEIJO_YOUTYUUI)%></span></DIV>
		<DIV id="tab"><span id="disable"><%=i18n.get(GL.TAB_KASHITAORE)%></span></DIV>
		<DIV id="tab"><span><%=i18n.get(GL.TAB_HASAN_KOSEI)%></span></DIV>
	</logic:equal>

	<%}%>

	<logic:equal name="KubunForm" property="currentTab" value="1">
	<iframe id="iframe" src="../satei/seijo_yotyui_tab.jsp" width=100% height=145px></iframe>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="2">
	<iframe id="iframe" src="../satei/kashidaorekenen_tab.jsp" width=100% height=200px></iframe>
	</logic:equal>
	<logic:equal name="KubunForm" property="currentTab" value="3">
	<iframe id="iframe" src="../satei/hasankosei_tab.jsp" width=100% height=250px></iframe>
	</logic:equal>

	<TABLE class="OC1103ContentsComment">
		<TR>
		<TD class="OC1103CommentLabel"><%=i18n.get(GL.OC1103_HANTEI_KONKYO)%><BR>
		</TD>
		<TD class="OC1103CommentVal"><html:textarea name="KubunForm" property="txtValKonkyo" rows="4" style="width:100%" /><BR>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE class="OC1103ContentsSaiken">
		<TR>
		<TD class="OC1103SaikenLabel"><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1103_SAIKEN_KBN_HANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%><BR>
		</TD>
		<TD class="OC1103SaikenVal">
		<html:select property="kbnSaiken">
			<html:optionsCollection name="KubunForm" property="saikenList" value="value" label="key" />
		</html:select><BR>
		</TD>
		</TR>
	</TABLE>

	<TABLE class="OC1103ContentsComment">
		<TR>
		<TD class="OC1103CommentLabel"><%=i18n.get(GL.OC1103_HANTEI_JIYU)%><BR>
		</TD>
		<TD class="OC1103CommentVal"><html:textarea name="KubunForm" property="txtValJiyuu" rows="4" style="width:100%" /><BR>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE class="OC1103ContentsComment">
		<TR >
		<TD class="OC1103CommentLabel"><%=i18n.get(GL.OC1103_HASSEI_KEII)%><BR>
		</TD>
		<TD class="OC1103CommentVal"><html:textarea name="KubunForm" property="txtValKeii" rows="4" style="width:100%" /><BR>
		</TD>
		</TR>
	</TABLE>

</DIV>
</DIV>
</html:form>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>