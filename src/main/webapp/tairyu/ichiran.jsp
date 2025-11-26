<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="IchiranForm" name="01IchiranForm" type="app.tairyu.form.IchiranForm" />
<% Pager pager = IchiranForm.getPager(); %>
</HEAD>
<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="../image/navi001.gif" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="../image/<%=i18n.get(GL.IMG_TITLE)%>.gif" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB1101)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>

		<DIV id="list">
			<html:form action="/tairyu/ichiran" >
				
				<%-- 勘定先リンククリック時の引数 --%>
				<html:hidden property="anken_no" />
				<html:hidden property="id" />

				<%-- 自担当分/汎用２ラジオボタン --%>
				<DIV class="leftbox">
					<html:radio onclick="doSubmit('tanto')" property="tanto" value="1" /><DIV class="top3"><%=i18n.get(GL.COMMON_MYTASKS)%>&nbsp;</DIV>
					<html:radio onclick="doSubmit('tanto')" property="tanto" value="2" /><DIV class="top3"><%=SESSION_DATA_APP.getLbl_nm2()%></DIV>
				</DIV>

				<%-- 各進捗件数 --%>
				<DIV class="rightbox">
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TABLE class="titl">
						<TR><TD class="nonBorder">&nbsp;</TD></TR>
						<TR><TD class="nonBorder"><%=i18n.get(GL.OB1101_TAIRYUHANTEI)%></TD></TR>
						<TR><TD class="nonBorder"><%=i18n.get(GL.OB1101_TAIRYUHANTEI_K)%></TD></TR>
					</TABLE>
					<%} else {%>
					<TABLE class="titl">
						<TR><TD class="nonBorder">&nbsp;</TD></TR>
						<TR><TD class="nonBorder"><DIV class="top10"><%=i18n.get(GL.OB1101_TAIRYUHANTEI)%></DIV></TD></TR>
						<TR><TD class="nonBorder"><DIV class="top3"><%=i18n.get(GL.OB1101_TAIRYUHANTEI_K)%></DIV></TD></TR>
					</TABLE>
					<%}%>
					<TABLE class="tbl">
						<TR>
							<TH><%=i18n.get(GL.COMMON_UNPROCESSED)%></TH>
							<TH><%=i18n.get(GL.COMMON_PROCESSING)%></TH>
							<TH><%=i18n.get(GL.COMMON_WATING_APPROVAL)%></TH>
							<TH><%=i18n.get(GL.COMMON_COMPLETE)%></TH>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="hantei_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="hantei_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="hantei_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="hantei_kanryo" /></TD>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="kensyo_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="kensyo_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="kensyo_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="kensyo_kanryo" /></TD>
						</TR>
					</TABLE>
				</DIV>

				<DIV class="headerlist">
					<TABLE>
						<TR style="width:100%;">
							<%-- 査定期セレクトボックス --%>
							<TD style="width:8%;"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%></TD>
							<TD style="width:14%;">
								<html:select property="sateiki" onchange="doSubmit('sateiki')" style="width:80">
									<html:optionsCollection name="IchiranForm" property="ar_sateiki" value="value" label="key" /></html:select>
							</TD>

							<%-- ソート順セレクトボックス --%>
							<TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SORT)%>
							<%-- ソート項目 --%>		
							<TD style="width:13%;">
								<html:select property="sort_item" onchange="doSubmit('sort_item')" style="width:100">
									<html:optionsCollection name="IchiranForm" property="ar_sort_item" value="value" label="key" /></html:select>
							</TD>
							<%-- 整列方向 --%>
							<TD style="width:3%;">
								<html:select property="sort_order" onchange="doSubmit('sort_order')" style="width:70">
									<html:optionsCollection name="IchiranForm" property="ar_sort_order" value="value" label="key" /></html:select>
							</TD>

							<%-- 表示件数セレクトボックス --%>
							<TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SHOW)%></TD>
							<TD style="width:9%;"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="IchiranForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>

							<%-- ←前のXX件 --%>
							<TD style="width:13%;">
								<logic:notEqual name="IchiranForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- 次のXX件→ --%>
							<TD style="width:14%;">
								<logic:notEqual name="IchiranForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="yen" /></a>
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
								<TH colspan="2" width="11%"><%=i18n.get(GL.OB1101_KANJO_CD)%></TH>
								<TH width="49%"><%=i18n.get(GL.OB1101_KANJO_NM)%></TH>
								<TH colspan="3" width="16%"><%=i18n.get(GL.OB1101_COUNTRY)%></TH>
								<TH colspan="3" width="15%"><%=i18n.get(GL.OB1101_KINGAKU_TOTAL)%></TH>
								<TH width="9%"class="borderRight"><%=i18n.get(GL.OB1101_TAISYO_YM)%></TH>
							</TR>
							<TR>
								<TH width="6%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<TH colspan="2" width="58%"><%=i18n.get(GL.OB1101_SOSHIKI)%></TH>
								<TH colspan="4" width="16%"><%=i18n.get(GL.OB1101_TANTO_NM)%></TH>
								<TH colspan="3" width="22%"class="borderRight"><%=i18n.get(GL.OB1101_PROGRESS)%></TH>
							</TR>
						</THEAD>
						<TBODY>
							<% if(IchiranForm.getList() != null) { %>
								<nested:iterate name="IchiranForm" property="list" indexId="idx">
									<TR>
										<TD colspan="2" width="11%">
											<nested:equal property="link_flg" value="true">
												<a href="#" onClick="mogitoriConfirm('mogitori','<nested:write property="anken_no" />','<nested:write property="id" />','<nested:write property="tanto_nm" />')"><nested:write property="kanjo_cd" /></a>
											</nested:equal>
											<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;</TD>
										<TD width="49%" class=""><nested:write property="kanjo_nm" />&nbsp;</TD>
										<TD colspan="3" width="16%"><nested:write property="syozaikoku" />&nbsp;</TD>
										<TD colspan="3" width="15%" class="right"><nested:write property="kingaku" />&nbsp;</TD>
										<TD width="9%" class="borderRight"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
									</TR>
									<TR>
										<TD width="6%" class="borderBottom"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
										<TD colspan="2" width="58%" class="borderBottom "><nested:write property="soshiki" />&nbsp;</TD>
										<TD colspan="4" width="16%" class="borderBottom "><nested:write property="tanto_nm" />&nbsp;</TD>
										<TD colspan="3" width="22%" class="borderBottom borderRight">
											<nested:write property="sintyoku" />&nbsp;
										</TD>
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