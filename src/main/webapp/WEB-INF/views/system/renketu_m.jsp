<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="RenketuForm" name="04RenketuForm" type="app.system.form.RenketuForm" />
<%--@ page import="common.util.Pager" --%>
<script>
	function downloadSubmit() {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "renketu_download";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
	//2022/06/14 Fix bug No.22 START
	function blockEnter(e) {
		if (e.keyCode=='0xD'){
			window.event.returnValue = false;
			return false;
		}
	}
	//2022/06/14 Fix bug No.22 END
</script>

</HEAD>
<BODY onload="">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="../image/navi001.gif" width="89" height="52">
	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="../image/<%=i18n.get(GL.IMG_TITLE)%>.gif" height="54">
 	<%-- ヘルプリンク --%>
	<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<%-- 連絡区分マスタUPLOAD --%>
<H1 class="title01"><%=i18n.get(GL.TITLE_RENKETU)%></H1>
<DIV id="submenu">
	<%-- マスタダウンロード --%>
	<input type="button" value="<%=i18n.get(GL.LINK_MASTER_DL)%>" onclick="downloadSubmit()" style="width:120"/>

	&nbsp;&nbsp;&nbsp;
	<%-- 戻る --%>
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
</DIV>

<DIV id="list">
<%-- No459, 2008/05/30, SJA渡辺, フォーカスの設定を追加 --%>
<%-- No554, 2008/06/05, SJA平林, エラー時のフォーカス制御を追加 --%>
<%
String focus = "";
if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
	focus = "fileUp";
}else{
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
%>
<html:form action="/system/renketu" 
           method="POST" 
           enctype="multipart/form-data" focus="<%= focus %>">
	
	<BR><BR>
	<DIV class="mainlist">
		<table style="border:0px;width:100%;" table-layout:fixed;>
			<tr style="border:0px">
				<td style="border:0px;width:10%;"><%=i18n.get(GL.LABEL_UP_FILE)%></td><%-- 登録ファイル名 --%>
				<td style="border:0px;width:80%;word-break:break-all;">
					<%-- No862, 2008/06/16, SJA渡辺, 言語モードでIMEコントロールを切り替えるように修正 --%>
					<%-- No862, 2008/06/16, SJA渡辺, 初期値が英数字入力モードに修正 --%>
					<html:file name="RenketuForm" property="fileUp" size="100" style="width:100%; height:20; align:center" styleClass="singleByte" onkeydown="blockEnter(event)"/>
				</td>
				<td style="border:0px" class="center">
					<controller maxFileSize="100M"/>
					<input style="text-align; center; background-color:#CCCCCC; width:80; height:20" type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmitNon('renketu_touroku')"/>
				</td>
			</tr>
		</table>

	</DIV>
</html:form>
</DIV>

</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>