<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="SenteituikaForm" name="01SenteituikaForm" type="app.tairyu.form.SenteituikaForm" />
<% Pager pager = SenteituikaForm.getPager(); %>
<link rel="stylesheet" href="<c:url value='/css/Tairyu.css' />" type="text/css">
</HEAD>
<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB2103)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('OB2101')">
		</DIV>

		<DIV id="list">
			<%
				String focus = "";
				if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
					focus = "sateiki";
				}else{
					focus = (String)request.getAttribute(GS.FOCUS_FIELD);
				}
			%>
			<html:form action="/tairyu/senteituika" focus="<%= focus %>">

				<%-- 勘定先リンククリック時の引数 --%>
				<html:hidden property="anken_no" />
				<html:hidden property="id" />

				<DIV class="headerlist">

					<%-- 査定期セレクトボックス --%>
					<TABLE style="width:85%;">
						<TR>
							<TD style="width:8%;"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%><BR></TD>
							<TD style="width:14%;"><html:select property="sateiki" style="width:80">
								<html:optionsCollection name="SenteituikaForm" property="ar_sateiki" value="value" label="key" />
  								</html:select><BR></TD>
							<TD style="width:63%;"></TD>
  						</TR>
  					</TABLE>
					<BR>

					<%-- 検索項目 --%>
					<TABLE class="semaku" style="width:85%;table-layout:fixed;">
						<TR>
							<TD class="semaku" style="width:10%;"><%=i18n.get(GL.OB2103_KANJO_CD)%><BR></TD>
							<TD class="semaku" style="width:25%;"><html:text property="kanjo_cd" style="width:50%;" maxlength="12" /><%=i18n.get(GL.COMMON_ZENPOUICCHI)%><BR></TD>
							<TD class="semaku" style="width:12%;">&nbsp;&nbsp;<%=i18n.get(GL.OB2103_KANJO_NM)%><BR></TD>
							<TD class="semaku" colspan="2" style="width:53%;"><html:text property="kanjo_nm" styleClass="doubleByte" style="width:70%;"  maxlength="120"/><%=i18n.get(GL.COMMON_BUBUNICCHI)%><BR></TD>
						</TR>
						<TR>
							<TD class="semaku" style="width:10%;"><%=SESSION_DATA_APP.getLbl_nm1()%><BR></TD>
							<TD class="semaku" style="width:10%;"><DIV class="ReadOnlybox" style="width:80%;"><%=SESSION_DATA_APP.getUser_bean().getComWorkflowSateikaisya_cd()%></DIV></TD>
							<%if(!SESSION_DATA_APP.getUser_bean().getComWorkflowSystemkbn().equals(GS.GSS)) {%>
								<TD class="semaku" style="width:12%;">&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm3()%><BR></TD>
								<TD class="semaku" style="width:38%;"><html:select property="hanyou2" style="width:80">
									<html:optionsCollection name="SenteituikaForm" property="ar_hanyou2" value="value" label="key" />
  									</html:select></TD>
							<%} else {%>
								<TD colspan="2" style="width:65%;"><BR></TD>
							<%}%>
							<TD style="width:15%;" class="right semaku"><input class="button" type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')"><BR></TD>
					</TABLE>

					<TABLE>
						<TR style="width:100%;">
							<TD style="width:40%;"><BR></TD>

							<%-- 表示件数 selectBox --%>
							<TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SHOW)%></TD>
							<TD style="width:9%;"><html:select property="view" onchange="doSubmit('show')" style="width:70">
   								<html:optionsCollection name="SenteituikaForm" property="ar_show" value="value" label="key" />
 									</html:select><BR></TD>

							<%-- ←前のXX件 --%>
							<TD style="width:13%;">
								<logic:notEqual name="SenteituikaForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteituikaForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteituikaForm" property="xen" /></a>
									<%}%>
								</logic:notEqual><BR>
							</TD>

							<%-- 次のXX件→ --%>
							<TD style="width:14%;">&nbsp;&nbsp;
								<logic:notEqual name="SenteituikaForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteituikaForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteituikaForm" property="yen" /></a>
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
									<TH width="11%"><%=i18n.get(GL.OB2103_KANJO_CD)%></TH>
									<TH><%=i18n.get(GL.OB2103_KANJO_NM)%></TH>
									<TH width="9%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
									<TH width="9%"><%=SESSION_DATA_APP.getLbl_nm7()%></TH>
									<TH width="9%"><%=i18n.get(GL.OB2103_TAISYO_YM)%></TH>
									<TH width="9%"><%=i18n.get(GL.OB2103_KTK)%></TH>
									<TH width="17%"><%=i18n.get(GL.OB2103_SAIKEN_KINGAKU)%></TH>
								</TR>
							</THEAD>
							<TBODY>
								<% if(SenteituikaForm.getList() != null) { %>
									<nested:iterate name="SenteituikaForm" property="list" indexId="idx">
										<TR>
											<TD><a href="#" onClick="syosai('syosai','<nested:write property="anken_no" />','<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>&nbsp;</TD>
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