<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="SateikaisyaIchiranForm" name="02SateikaisyaIchiranForm" type="app.system.form.SateikaisyaIchiranForm" />
<% Pager pager = SateikaisyaIchiranForm.getPager(); %>

<script>
function hanyo2Link(event, id) {
	form = document.forms[0];
	form.elements["id"].value = id;
	doSubmit(event);
}
</script>

</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7102)%></H1>
		<html:form action="/system/sateikaisyaichiran">
		<nested:hidden property="id"/>
		<%--検索用--%>
		<nested:hidden property="srhSystemKbn"/>
		<nested:hidden property="srhHanyo1"/>
		<nested:hidden property="srhHanyo2"/>
		<nested:hidden property="srhHanyo2Name"/>
		
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_SHINKI)%>" onclick="doSubmit('newRegist')" />
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')" />
		</DIV>

		<DIV id="list">
			<DIV class="mainlist">
				<TABLE style="border:0px;width:100%;table-layout:fixed;">
					<TR style="border:0px;">
						<%-- システム --%>
						<TD style="border:0px;width:10%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS7102_SYSTEM)%></DIV></TD>
						<TD style="border:0px;width:21%;text-align:left;margin-top:2px;"class="semaku">
							<nested:select property="systemKbn" onchange="doSubmit('systemKbn')" style="width:80">
								<nested:optionsCollection property="ar_systemKbn" value="value" label="key" />
							</nested:select>
						</TD>
						<%-- 汎用1 --%>
						<TD style="border:0px;width:10%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV>
						</TD>
			
						<TD style="border:0px;width:12%;text-align:left;margin-top:2px;"class="semaku">
							<nested:select property="hanyo1" style="width:80">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_hanyo1" value="value" label="key" />
							</nested:select>
						</TD>
					</TR>
					<TR>
						<%-- 汎用2 --%>
						<TD style="border:0px;width:5%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=SESSION_DATA_APP.getLbl_nm3()%></DIV>
						</TD>
						<TD style="border:0px;width:15%;" class="semaku">
							<nested:text property="hanyo2" style="width:80"></nested:text><%=i18n.get(GL.OS7102_ZENPOICHI)%>
						</TD>
						<%-- 汎用2名称 --%>
						<TD style="border:0px;width:8%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=SESSION_DATA_APP.getLbl_nm3()%><%=i18n.get(GL.OS7102_MEISYO)%></DIV>
						</TD>
						<TD  colspan=3 style="border:0px;" class="semaku">
							<nested:text property="hanyo2Name" style="width:100%" styleClass="doubleByte"></nested:text>
						</TD>
						<%-- 部分一致 --%>
						<TD style="border:0px;width:8%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS7102_BUBUNICHI)%></DIV>
						</TD>
					</TR>
					<%-- 課題No.218 ボタンのフォーマットを統一--%>
					<%-- 追加開始 --%>
				</TABLE>
					<DIV id="submenu"class="semaku">
						<%-- 検索ボタン --%>
						<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')" style="background:#CCCCCC;"/></P>
					</DIV>	
					<%-- 追加完了 --%>
				<br><br>
				<TABLE style="border:0px;width:100%;">
					<TR style="border:0px;">
						<TD style="border:0px;width:40%;"class="semaku"><BR></TD>
						<%-- 表示件数 --%>
						<TD align="right" style="border:0px;width:10%;"class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR>
						</TD>
						<TD style="border:0px;width:12%;"class="semaku">
							<html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="SateikaisyaIchiranForm" property="ar_show" value="value" label="key" />
							</html:select>
							<BR>
						</TD>
						<TD align="right" style="border:0px;width:10%;"class="semaku"><BR></TD>
						<%-- 前のXX件 --%>
						<TD style="border:0px;width:10%;">
							<logic:notEqual name="SateikaisyaIchiranForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SateikaisyaIchiranForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SateikaisyaIchiranForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- 次のXX件 --%>
						<TD style="border:0px;width:10%;">
							<logic:notEqual name="SateikaisyaIchiranForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SateikaisyaIchiranForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SateikaisyaIchiranForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<TD style="border:0px;width:15%;"class="semaku"><BR></TD>
						<%-- XX/YY件 --%>
						<TD style="border:0px;width:13%;text-align:right;"class="semaku">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%><BR>
						</TD>
					</TR>
				</TABLE>
			
				<TABLE style="border-left-color: #AAA;" border=0 cellSpacing=0 cellPadding=0 style="width=100%">
					<THEAD>
					<TR>
						<%-- 汎用2 --%>
						<TH width="7%"><p align=""><%=SESSION_DATA_APP.getLbl_nm3()%></p></TH>
						<%-- 汎用2名称(日本語) --%>
						<TH width="25%"><p align=""><%=SESSION_DATA_APP.getLbl_nm3()%><%=i18n.get(GL.OS7102_MEISYO)%><%=i18n.get(GL.OS7102_NIHONGO)%></p></TH>
						<%-- 汎用2名称(英語) --%>
						<TH width="30%"><p align=""><%=SESSION_DATA_APP.getLbl_nm3()%><%=i18n.get(GL.OS7102_MEISYO)%><%=i18n.get(GL.OS7102_EIGO)%></p></TH>
						<%-- システム --%>
						<TH width="9%"><p align=""><%=i18n.get(GL.OS7102_SYSTEM)%></p></TH>
						<%-- 汎用1 --%>
						<TH width="9%"><p align=""><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>
						<%-- 標準時刻 --%>
						<TH width="14%"><p align=""><%=i18n.get(GL.OS7102_HYOJUNJIKOKU)%></p></TH>
						<%-- 抽出<BR>対象 --%>
						<TH width="8%"><p align="center"><%=i18n.get(GL.OS7102_CHUSYUTUTAISYO)%></p></TH>
					</TR>
					</THEAD>
					<TFOOT></TFOOT>
					<TBODY>
					<nested:notEmpty property="list">
						<nested:iterate property="list" indexId="idx">
							<TR>
								<%-- 汎用2 --%>
								<TD width="7%">
									<a href="#" onClick="hanyo2Link('hanyo2Link', '<nested:write property="id" />')">
										<nested:write property="bunrui2"/>
									</a>
								</TD>
								<%-- 汎用2名称(日本語) --%>
								<TD width="25%"><nested:write property="bunrui2_nm_ja"/>&nbsp;</TD>
								<%-- 汎用2名称(英語) --%>
								<TD width="30%"><nested:write property="bunrui2_nm_en"/>&nbsp;</TD>
								<%-- システム --%>
								<TD width="9%"><nested:write property="system_kbn_nm"/>&nbsp;</TD>
								<%-- 汎用1 --%>
								<TD width="9%"><nested:write property="bunrui1"/>&nbsp;</TD>
								<%-- 標準時刻 --%>
								<TD width="14%"><nested:write property="standard_time_nm"/>&nbsp;</TD>
								<%-- 抽出<BR>対象 --%>
								<TD width="8%"><p align="center"><nested:write property="tyusyutu_taisyo_nm"/>&nbsp;</TD>
							</TR>
						</nested:iterate>
					</nested:notEmpty>
					</TBODY>
				</TABLE>
			</DIV>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
