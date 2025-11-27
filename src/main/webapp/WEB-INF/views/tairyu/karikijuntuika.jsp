<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="KarikijuntuikaForm" name="01KarikijuntuikaForm" type="app.tairyu.form.KarikijuntuikaForm" />
<% Pager pager = KarikijuntuikaForm.getPager(); %>
<link rel="stylesheet" href="<c:url value='/css/Tairyu.css' />" type="text/css">
<script>
	function karikijuntuikaSyosai(event,id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		form = document.forms[0];	
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
</script>
</HEAD>
<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB2105)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('release')">
			<input type="button" value="<%=i18n.get(GL.BTN_ADDSTOP)%>" onclick="doSubmit('addCancel')">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('backOB2101')">
			<br>
			<br>
			<logic:equal name="KarikijuntuikaForm" property="sasiTenFlg" value="1">
				<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('commentLink')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
			</logic:equal>
			<logic:equal name="KarikijuntuikaForm" property="sasiTenFlg" value="2">
				<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('commentLink')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
			</logic:equal>
		</DIV>

		<DIV id="list">
			<html:form action="/tairyu/karikijuntuika">

				<%-- 勘定先リンククリック時の引数 --%>
				<html:hidden property="id" />

				<DIV class="headerlist">
					<TABLE style="border:0px;width:85%;table-layout:fixed;">
						<TR style="border:0px;">
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:10%;"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%><BR></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:20%;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="KarikijuntuikaForm" property="sateiki_hyouji" /></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:70%;"></TD>
						</TR>
					</TABLE>
					<TABLE style="border:0px;width:85%;table-layout:fixed;">
						<TR style="border:0px;">
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:10%;"><%=i18n.get(GL.OB2105_KANJO_CD)%><BR></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:25%;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="KarikijuntuikaForm" property="kanjo_cd" /></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:12%;">&nbsp;&nbsp;<%=i18n.get(GL.OB2105_KANJO_NM)%><BR></TD>
							<TD colspan="2" style="border:0px;margin: 0 0 0 0;padding: 0px;width:53%;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="KarikijuntuikaForm" property="kanjo_nm" /></DIV></TD>
						</TR>
						<TR style="border:0px;">
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:10%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:25%;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="KarikijuntuikaForm" property="sateikaisya_cd" /></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:12%;">&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm3()%><BR></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;width:10%;"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="KarikijuntuikaForm" property="hanyou2" /></DIV></TD>
						</TR>
					</TABLE>
				<TABLE style="border:0px;width:100%;">
					<TR style="width:100%;">
						<TD style="width:48%;"><BR></TD>
						<%-- 表示件数 selectBox --%>
						<TD class="right" style="width:10%;"><%=i18n.get(GL.COMMON_SHOW)%></TD>
						<TD style="width:9%;"><html:select property="view" onchange="doSubmit('show')" style="width:70">
   							<html:optionsCollection name="KarikijuntuikaForm" property="ar_show" value="value" label="key" />
 							</html:select><BR>
						</TD>
						<%-- ←前のXX件 --%>
						<TD style="width:13%;">
							<logic:notEqual name="KarikijuntuikaForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="KarikijuntuikaForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="KarikijuntuikaForm" property="xen" /></a>
								<%}%>
							</logic:notEqual><BR>
						</TD>
						<%-- 次のXX件→ --%>
						<TD style="width:14%;">&nbsp;&nbsp;
							<logic:notEqual name="KarikijuntuikaForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="KarikijuntuikaForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="KarikijuntuikaForm" property="yen" /></a>
								<%}%>
							</logic:notEqual><BR>
						</TD>
						<%-- XX/YY件 --%>
						<TD class="right" style="width:12%;">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
						</TD>
					</TR>
				</TABLE>
				</DIV>
				<%-- 一覧情報 --%>					
				<DIV class="mainlist">
						<TABLE style="border-left-color:#AAA">
							<THEAD>
								<TR>
									<TH width="11%"><%=i18n.get(GL.OB2105_KANJO_CD)%></TH>
									<TH><%=i18n.get(GL.OB2105_KANJO_NM)%></TH>
									<TH width="9%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
									<TH width="9%"><%=SESSION_DATA_APP.getLbl_nm7()%></TH>
									<TH width="9%"><%=i18n.get(GL.OB2105_TAISYO_YM)%></TH>
									<TH width="9%"><%=i18n.get(GL.OB2105_KTK)%></TH>
									<TH width="17%"><%=i18n.get(GL.OB2105_SAIKEN_KINGAKU)%></TH>
								</TR>
							</THEAD>
							<TBODY>
								<% if(KarikijuntuikaForm.getList() != null) { %>
									<nested:iterate name="KarikijuntuikaForm" property="list" indexId="idx">
										<TR>
											<TD><a href="#" onClick="karikijuntuikaSyosai('syosai','<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>&nbsp;</TD>
											<TD><nested:write property="kanjo_nm" />&nbsp;</TD>
											<TD><nested:write property="sateikaisya_cd" />&nbsp;</TD>
											<TD><nested:write property="soshiki" />&nbsp;</TD>
											<TD><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
											<TD><nested:write property="sinyoktk" />&nbsp;</TD>
											<TD class="right"><nested:write property="saiken_kingaku" />&nbsp;</TD>
										</TR>
									</nested:iterate>
								<% } %>
							</TBODY>
						</TABLE>
					</DIV>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>