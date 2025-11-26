<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="SyoninForm" name="01SyoninForm" type="app.tairyu.form.SyoninForm" />
<% Pager pager = SyoninForm.getPager(); %>
</HEAD>
<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="../../../image/navi001.gif" width="89" height="52">
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB1104)%></H1>
	
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syonin')">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>
		
		<DIV id="list">
			<html:form action="/tairyu/syonin" >
			
			<html:hidden property="anken_no" />
			<html:hidden property="id" />
			
				<DIV class="headerlist">
					<TABLE style="width:100%;" class="semaku">
						<TR style="width:100%;" class="semaku">
							<TR class="semaku">
							<TD style="width:40%;" class="semaku"><BR></TD>
							<%-- 表示件数セレクトボックス --%>
							<TD class="right" style="width:10%;" class="semaku"><%=i18n.get(GL.COMMON_SHOW)%></TD>
							<TD style="width:9%;" class="semaku"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="SyoninForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>

							<%-- ←前のXX件 --%>
							<TD class="right" style="width:13%;">
								<logic:notEqual name="SyoninForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SyoninForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SyoninForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- 次のXX件→ --%>
							<TD class="right" style="width:15%;">
								<logic:notEqual name="SyoninForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SyoninForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SyoninForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- XX/YY件 --%>
							<TD style="width:13%;" class="right" class="semaku">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
						<TR>
							<TD colspan="4" class="semaku"><br>
							</TD>
							<TD colspan="2" class="semaku">
								<DIV class="right"><%=i18n.get(GL.OB1104_IKKATUSYONIN)%>&nbsp;<html:checkbox onclick="doSubmit('ikatu_syonin')" property="ikt_syonin" /></DIV>
							</TD>
						</TR>
					</TABLE>
					</DIV>
					
					<%-- 一覧情報 --%>	
					<DIV class="mainlist">					
					<TABLE>
						<THEAD>
							<TR>
								<TH colspan="1" width="13%"><%=i18n.get(GL.OB1104_KANJO_CD)%></TH>
								<TH colspan="7" width="43%"><%=i18n.get(GL.OB1104_KANJO_NM)%></TH>
								<TH colspan="2" width="18%"><%=i18n.get(GL.OB1104_KINGAKU_TOTAL)%></TH>
								<TH width="8%"><%=i18n.get(GL.OB1104_TENPU)%></TH>
								<TH width="8%"><%=i18n.get(GL.OB1104_TAISYO_YM)%></TH>
								<TH rowspan="2" width="14%" style="text-align: center;"class="borderRight"><%=i18n.get(GL.OB1104_SYONIN)%></TH>
							</TR>
							<TR>
								<TH colspan="2" width="1%"><%=i18n.get(GL.OB1104_PHASE)%></TH>
								<TH colspan="1" width="10%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<TH colspan="6" width="40%"><%=i18n.get(GL.OB1104_SOSHIKI)%></TH>
								<TH colspan="3" width="10%"><%=i18n.get(GL.OB1104_TANTO_NM)%></TH>
							</TR>
						</THEAD>
						<TBODY>
							<% if(SyoninForm.getList() != null) { %>
								<nested:iterate name="SyoninForm" property="list" indexId="idx">
									<TR>
										<TD colspan="1" width="13%">
											<nested:equal property="link_flg" value="true">
												<a href="#" onClick="syosai('link_meisai','<nested:write property="anken_no" />','<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
											</nested:equal>
											<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;</TD>
										<TD colspan="7" width="42%"><nested:write property="kanjo_nm" />&nbsp;</TD>
										<TD colspan="2" width="20%" class="right"><nested:write property="kingaku" />&nbsp;</TD>
										<TD width="10%" class="right"><nested:write property="temp_cnt" />&nbsp;</TD>										
										<TD width="8%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
										<TD rowspan="2" width="10%" class="center borderBottom borderRight"><nested:checkbox property="syonin_chk" /></TD>
									</TR>
									<TR>
										<TD colspan="2" width="1%" class="borderBottom"><nested:write property="sintyoku" />&nbsp;</TD>
										<TD width="10%" class="borderBottom"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
										<TD colspan="6" width="40%" class="borderBottom"><nested:write property="soshiki" />&nbsp;</TD>
										<TD colspan="3" width="10%" class="borderBottom"><nested:write property="tanto_nm" />&nbsp;</TD>
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
