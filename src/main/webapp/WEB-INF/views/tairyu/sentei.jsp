<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="SenteiForm" name="01SenteiForm" type="app.tairyu.form.SenteiForm" />
<% Pager pager = SenteiForm.getPager(); %>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB2101)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_OB2103)%>" onclick="doSubmit('OB2103')" style="WIDTH: 117px;">  
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')" style="WIDTH: 117px;">
		</DIV>

		<DIV id="list">
			<html:form action="/tairyu/sentei">

				<%-- 勘定先リンククリック時の引数 --%>
				<html:hidden property="id" />	
				<html:hidden property="anken_no" />	
	
				<DIV class="headerlist">
	
					<TABLE>
						<TR style="width:100%;">
							<%-- 自担当分/汎用２ラジオボタン --%>
							<TD style="width:2%;">
								<html:radio onclick="doSubmit('tanto')" property="tanto" value="1"/><BR></TD>
							<TD style="width:8%;"><%=i18n.get(GL.COMMON_MYTASKS)%><BR></TD>
							<TD style="width:2%;">
								<html:radio onclick="doSubmit('tanto')" property="tanto" value="2"/><BR></TD>
							<TD style="width:10%;"><%=SESSION_DATA_APP.getLbl_nm5()%><BR></TD>
							<TD style="width:18%;"><BR></TD>
		
							<%-- 表示件数セレクトボックス --%>
							<TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SHOW)%></TD>
							<TD style="width:9%;"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="SenteiForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>
						
							<%-- ←前のXX件 --%>
							<TD style="width:13%;">
								<logic:notEqual name="SenteiForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteiForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteiForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- 次のXX件→ --%>
							<TD style="width:14%;">
								<logic:notEqual name="SenteiForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteiForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteiForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
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
					<TABLE>
						<THEAD>
							<TR>
								<TH width="11%"><%=i18n.get(GL.OB2101_KANJO_CD)%></TH>
								<TH width="46%"><%=i18n.get(GL.OB2101_KANJO_NM)%></TH>
								<TH width="9%"><%=i18n.get(GL.OB2101_TAISYO_YM)%></TH>
								<TH width="17%"><%=i18n.get(GL.OB2101_TAIRYU_KINGAKU)%></TH>
								<TH width="17%"class="borderRight"><%=i18n.get(GL.OB2101_SAIKEN_KINGAKU)%></TH>
							</TR>
							<TR>
								<TH style="width:11%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<TH style="width:45%;"><%=i18n.get(GL.OB2101_SOSHIKI)%></TH>
								<TH colspan="2" style="width:27%;"><%=i18n.get(GL.OB2101_TANTO_NM)%></TH>
								<TH style="width:17%;"class="borderRight"><%=i18n.get(GL.OB2101_REASON)%></TH>
							</TR>
						</THEAD>
						<TBODY>
							<% if(SenteiForm.getList() != null) { %>
								<nested:iterate name="SenteiForm" property="list" indexId="idx">
									<TR>
										<TD width="11%">
											<nested:equal property="link_flg" value="true">
												<a href="#" onClick="mogitoriConfirm('mogitori','<nested:write property="anken_no" />','<nested:write property="id" />','<nested:write property="tanto_nm" />')"><nested:write property="kanjo_cd" /></a>
											</nested:equal>
											<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;</TD>
										<TD width="46%"><nested:write property="kanjo_nm" />&nbsp;</TD>
										<TD width="9%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
										<TD width="17%" class="right"><nested:write property="tairyu_kingaku" />&nbsp;</TD>
										<TD width="17%" class="right borderRight"><nested:write property="saiken_kingaku" />&nbsp;</TD>
									</TR>
									<TR>
										<TD width="11%" class="borderBottom"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
										<TD width="45%" class="borderBottom"><nested:write property="soshiki" />&nbsp;</TD>
										<TD colspan="2" width="27%" class="borderBottom"><nested:write property="tanto_nm" />&nbsp;</TD>
										<TD width="17%" class="borderBottom borderRight"><nested:write property="jiyu_nm" />&nbsp;</TD>
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