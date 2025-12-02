<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="TenpuSyokaiForm" name="05TenpuSyokaiForm" type="app.common.form.TenpuSyokaiForm" />
<%--@ page import="common.util.Pager" --%>
<% 
	List list = TenpuSyokaiForm.getList();
%>
<script>
	function indexCheck(index) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["id"].value = index;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "download";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
</script>
</HEAD>
<BODY onload="">
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
<H1 class="title01"><%=i18n.get(GL.TITLE_05_01)%></H1>

<html:form action="/common/tenpu_syokai" >
<html:hidden property="id" />

<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
</DIV>

<DIV id="list">

	<DIV class="mainlist">
	<BR><BR><BR>

		<TABLE border=0 cellSpacing=0 cellPadding=0 class"tenpu" style="border-left-color: #AAA;">
		<THEAD>
		<TR>
			<%-- 課題No.09 文書添付仕様変更 --%>
			<%-- 修正開始 --%>
			<%-- <TH style="text-align:center;width:64%;"><%=i18n.get(GL.LABEL_FILE_NM)%></TH> --%>
			<%-- <TH style="text-align:center;width:18%;"><%=i18n.get(GL.LABEL_PHASE)%></TH> --%>
			<%-- <TH style="text-align:center;width:18%;"><%=i18n.get(GL.LABEL_OWNER)%></TH> --%>
			<TH style="text-align:center;width:62%;"><%=i18n.get(GL.LABEL_FILE_NM)%></TH>
			<TH style="text-align:center;width:10%;"><%=i18n.get(GL.LABEL_EDABAN)%></TH>
			<TH style="text-align:center;width:18%;"><%=i18n.get(GL.LABEL_PHASE)%></TH>
			<TH style="text-align:center;width:10%;"><%=i18n.get(GL.LABEL_OWNER)%></TH>
			<%-- 修正完了 --%>
		</TR>
		</THEAD>
		<% if(list != null) { %>
		<logic:iterate id="meisai" collection="<%=list%>" type="java.util.HashMap">
		<TBODY>
		<TR>
			<%-- 課題No.52 添付ファイルダウンロード対応 --%>
			<%-- 修正開始 --%>
			<%-- <TD><a href="<%=GS.WEB_COMMON+GS.TENPU_SYOKAI+GS.ACTION%>?<%=GS.EVENT%>=download&index=<%=(String)meisai.get("id")%>"><bean:write name="meisai" property="file_nm" /></a>&nbsp;</TD> --%>
			<TD><a href="#" onclick="windowOpen('download','<%=(String)meisai.get("id")%>','<bean:write name="meisai" property="file_nm" />')">
			<bean:write name="meisai" property="file_nm" /></a>&nbsp;</TD>
			<%-- 修正完了 --%>
			<%-- 課題No.09 文書添付仕様変更 --%>
			<%-- 追加開始 --%>
			<TD class="center"><bean:write name="meisai" property="edaban" />&nbsp;</TD>
			<%-- 追加完了 --%>
			<TD class="center"><bean:write name="meisai" property="phase" />&nbsp;</TD>
			<TD class="center"><bean:write name="meisai" property="syoyuu_kaisha_cd" />&nbsp;</TD>
		</TR>
		</logic:iterate>
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