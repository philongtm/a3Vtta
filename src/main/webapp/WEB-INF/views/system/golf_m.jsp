<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="GolfForm" name="04GolfForm" type="app.system.form.GolfForm" />
<% Pager pager = GolfForm.getPager(); %>

<script>
function link(event, id) {
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS4101)%></H1>
		<html:form action="/system/golf">
		<nested:hidden property="id"/>
		
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')" />
		</DIV>
		
		<DIV id="list">
			<DIV class="leftbox">
		
			</DIV>
			<DIV class="rightbox">
		
			</DIV>
		
			<DIV class="mainlist">
			
				<TABLE style="border:0px;width:100%;">
					<TR style="border:0px;width:100%;">
						<%-- 査定期セレクトボックス --%>
						<TD style="border:0px;width:8%;">&nbsp;<%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%>
						</TD>
						<TD style="border:0px;width:14%;text-align:left;">
							<html:select property="sateiki" onchange="doSubmit('sateiki')" style="width:80">
								<html:optionsCollection name="GolfForm" property="ar_sateiki" value="value" label="key" />
							</html:select>
						</TD>
						<TD style="border:0px; width:10%;">&nbsp;</TD>
						<%-- 表示件数セレクトボックス --%>
						<TD style="border:0px;width:10%;">&nbsp;&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.COMMON_SHOW)%></TD>
						<TD style="border:0px;width:9%;text-align:left;">
							<html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="GolfForm" property="ar_show" value="value" label="key" />
							</html:select>
						</TD>
						<TD align="right" style="border:0px;width:3%;">
						</TD>
						<%-- ←前のXX件 --%>
						<TD style="border:0px;width:13%;">
							<logic:notEqual name="GolfForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="GolfForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="GolfForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- 次のXX件→ --%>
						<TD style="border:0px;width:13%;">
							<logic:notEqual name="GolfForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="GolfForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="GolfForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						<%-- XX/YY件 --%>
						<TD style="text-align:right;border:0px;width:12%;">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%><BR>
						</TD>
					</TR>
				</TABLE>
				<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
				<THEAD>
				<TR>
					<%-- 勘定先CD --%>
					<TH width="12%" colspan="2"><%=i18n.get(GL.OS4101_KANJYOCD)%></TH>
					<%-- 勘定先名称 --%>
					<TH width="30%" ><%=i18n.get(GL.OS4101_KANJYONAME)%></TH>
					<%-- 債権残計 --%>
					<TH width="20%" colspan="3"><%=i18n.get(GL.OS4101_SAIKENZANKEI)%></TH>
					<%-- 固定化営業債権 --%>
					<TH width="20%" colspan="2"><%=i18n.get(GL.OS4101_KOTEIKA)%></TH>
					<%-- 貸倒引当金 --%>
					<TH class="borderRight"><%=i18n.get(GL.OS4101_KASHIDAORE)%></TH>
				</TR>
				<TR>
					<%-- 汎用１ --%>
					<TH width="9%" ><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
					<%-- 組織 --%>
					<TH colspan="4"><%=i18n.get(GL.OS4101_SOSHIKI)%></TH>
					<%-- 対象年月 --%>
					<TH colspan="2"><%=i18n.get(GL.OS4101_TAISYOYM)%></TH>
					<%-- 所在国 --%>
					<TH colspan="2" class="borderRight"><%=i18n.get(GL.OS4101_SHOZAIKOKU)%></TH>
				</TR>
				</THEAD>
				
				<TBODY>
				<nested:notEmpty property="list">
					<nested:iterate property="list" indexId="idx">
						<TR>
							<%-- 勘定先CD --%>
							<TD width="10%" colspan="2">&nbsp;
							<nested:equal property="link_flg" value="true">
								<a href="#" onClick="link('kanjoCdLink', '<nested:write property="id" />')">
									<nested:write property="kanjo_cd"/>
								</a>
							</nested:equal>
							<nested:equal property="link_flg" value="false">
								<nested:write property="kanjo_cd"/>
							</nested:equal>
							</TD>
							<%-- 勘定先名称 --%>
							<TD width="30%">
								<nested:write property="kanjo_nm"/>&nbsp;
							</TD>
							<%-- 債権残計 --%>
							<TD width="20%" colspan="3" class="right">&nbsp;
								<nested:write property="saiken_kingaku"/>
							</TD>
							<%-- 固定化営業債権 --%>
							<TD width="20%" colspan="2" class="right">&nbsp;
								<nested:write property="koteika_saiken"/>
							</TD>
							<%-- 貸倒引当金 --%>
							<TD class="right" style="border-right-color:#000000;">&nbsp;
								<nested:write property="kasi_hikiatekin"/>
							</TD>
						</TR>
						<TR>
							<%-- 汎用１ --%>
							<TD width="9%" style="border-bottom-color:#000000;">&nbsp;
								<nested:write property="sateikaisya_cd"/>
							</TD>
							<%-- 組織 --%>
							<TD colspan="4" style="border-bottom-color:#000000;">&nbsp;
								<nested:write property="soshiki"/>
							</TD>
							<%-- 対象年月 --%>
							<TD colspan="2" style="border-bottom-color:#000000;">&nbsp;
								<nested:write property="taisyo_ym_hyoji"/>
							</TD>
							<%-- 所在国 --%>
							<TD colspan="2" style="border-bottom-color:#000000;border-right-color:#000000;">&nbsp;
								<nested:write property="syozaikoku"/>
							</TD>
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
