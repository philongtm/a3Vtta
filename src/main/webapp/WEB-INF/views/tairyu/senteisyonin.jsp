<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="SenteisyoninForm" name="01SenteisyoninForm" type="app.tairyu.form.SenteisyoninForm" />
<% Pager pager = SenteisyoninForm.getPager(); %>
<%
String focus = "";
if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
	focus = "";
}else{
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
%>
<script>
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
    	form.elements[val].focus();
    }
	}
</script>	
</HEAD>
<BODY onload="setFocus('<%=focus%>')">
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB2104)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syonin')">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>

		<DIV id="list">
			<html:form action="/tairyu/senteisyonin" >

				<%-- 勘定先リンククリック時の引数 --%>
				<html:hidden property="anken_no" />
				<html:hidden property="id" />

				<DIV class="headerlist">
					<TABLE>
						<TR style="width:100%;">
							<TD style="width:40%;"><BR></TD>

							<%-- 表示件数セレクトボックス --%>
							<TD class="right" style="width:10%;"><%=i18n.get(GL.COMMON_SHOW)%><BR></TD>
							<TD style="width:9%;" class="semaku"><html:select property="view" onchange="doSubmit('show')" style="width:70">
    							<html:optionsCollection name="SenteisyoninForm" property="ar_show" value="value" label="key" /></html:select><BR>
							</TD>
			
							<%-- ←前のXX件 --%>
							<TD style="width:13%;">
								<logic:notEqual name="SenteisyoninForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteisyoninForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SenteisyoninForm" property="xen" /></a>
									<%}%>
								</logic:notEqual><BR>
							</TD>

							<%-- 次のXX件→ --%>
							<TD style="width:15%;">&nbsp;&nbsp;
								<logic:notEqual name="SenteisyoninForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteisyoninForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SenteisyoninForm" property="yen" /></a>
									<%}%>
								</logic:notEqual><BR>
							</TD>
	
							<%-- XX/YY件 --%>
							<TD class="right" style="width:13%;">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
		
						<TR>
							<TD colspan="4" class="semaku"><br></TD>
							<TD colspan="2" class="semaku">
								<DIV class="right"><%=i18n.get(GL.COMMON_APPROVE_ALL)%>&nbsp;
									<html:checkbox name="SenteisyoninForm" property="ikkatu_syonin" value="on" onclick="doSubmit('ikkatu_syonin')"/>
								</DIV>
							</TD>
						</TR>
					</TABLE>
				</DIV>		
				<%-- 一覧情報 --%>					
				<DIV class="mainlist">
					<TABLE>
						<THEAD>
							<TR>
								<TH width="11%"><%=i18n.get(GL.OB2104_KANJO_CD)%></TH>
								<TH width="38%"><%=i18n.get(GL.OB2104_KANJO_NM)%></TH>
								<TH width="20%"><%=i18n.get(GL.OB2104_SAIKEN_KINGAKU)%></TH>
								<TH colspan="2" width="25%"><%=i18n.get(GL.OB2104_REASON)%></TH>			
								<TH rowspan="2" style="text-align: center;"class="borderRight"><%=i18n.get(GL.COMMON_APPROVE)%></TH>
							</TR>
							<TR>
								<TH width="11%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<TH width="38%"><%=i18n.get(GL.OB2104_SOSHIKI)%></TH>
								<TH colspan="2" width="32%"><%=i18n.get(GL.OB2104_TANTO_NM)%></TH>
								<TH width="15%"><%=i18n.get(GL.OB2104_SENTEI_KBN)%></TH>
							</TR>
						</THEAD>
						<TBODY>
							<% if(SenteisyoninForm.getList() != null) { %>
								<nested:iterate name="SenteisyoninForm" property="list" indexId="idx">
									<TR>
										<TD width="11%"><a href="#" onClick="syosai('syosai','<nested:write property="anken_no" />','<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>&nbsp;</TD>
										<TD width="38%"><nested:write property="kanjo_nm" />&nbsp;</TD>
										<TD width="20%" class="right"><nested:write property="saiken_kingaku" />&nbsp;</TD>
										<TD colspan="2" width="25%"><nested:write property="jiyu_nm" />&nbsp;</TD>
										<TD rowspan="2" class="center borderRight borderBottom"><nested:checkbox property="syonin_chk" value="on" /></TD>
									</TR>
									<TR>
										<TD width="11%" class="borderBottom"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
										<TD width="38%" class="borderBottom"><nested:write property="soshiki" />&nbsp;</TD>
										<TD colspan="2" width="32%" class="borderBottom"><nested:write property="tanto_nm" />&nbsp;</TD>
										<TD width="13%" class="borderBottom"><nested:write property="sentei_kbn" />&nbsp;</TD>
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