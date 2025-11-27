<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>


<bean:define id="SashimodoshiCommentForm" name="05SashimodoshiCommentForm" type="app.common.form.SashimodoshiCommentForm" />
<%-- セッションデータ定義 --%>
<bean:define id="SESSION_DATA_APP_ZEN" name="app.SessionDataZen" type="app.SessionDataZen" scope="session" />
<%-- No459, 2008/05/30, SJA渡辺, フォーカスの設定を追加 --%>
<script>
	function cngFocus() {
		document.forms[0].firstFocus.focus();
	}
</script>
</HEAD>
<BODY onload="cngFocus();">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
 	<%-- ヘルプリンク --%>
	<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<html:form action="/common/sashimodoshiComment">
<H1 class="title01"><%=i18n.get(GL.LABEL_SHOW_C)%></H1>

<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')" name="firstFocus">
</DIV>

<DIV id="list">

	<DIV class="headlist">
		<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.LABEL_CUST_CD)%></DIV><DIV class="ReadOnlybox" style="width:10%"><bean:write name="SashimodoshiCommentForm" property="kanjo_cd" /></DIV>
		&nbsp;&nbsp;&nbsp;
		<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.LABEL_TORIHIKISAKI_NM)%></DIV><DIV class="ReadOnlybox" style="width:67%;word-break:break-all;"><bean:write name="SashimodoshiCommentForm" property="kanjo_nm" /></DIV>
	</DIV>
	<DIV class="headlist">
		<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.LABEL_COMMENT_KBN)%></DIV><DIV class="ReadOnlybox" style="width:10%"><bean:write name="SashimodoshiCommentForm" property="toroku_div" /></DIV>
		&nbsp;&nbsp;&nbsp;
		
		<%if(SESSION_DATA_APP_ZEN.getComLangMode().equals("Ja")) {%>
		<DIV class="dottitle" style="width:15%"><%=i18n.get(GL.LABEL_COMMENT_INS_USR)%></DIV><DIV class="ReadOnlybox" style="width:40%;word-break:break-all;"><bean:write name="SashimodoshiCommentForm" property="user_nm" /></DIV>
		<%} else {%>
		<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.LABEL_COMMENT_INS_USR)%></DIV><DIV class="ReadOnlybox" style="width:40%;word-break:break-all;"><bean:write name="SashimodoshiCommentForm" property="user_nm" /></DIV>
		<%}%>
	</DIV>
	<DIV class="mainlist">
	<BR>
	<BR>
	<%=i18n.get(GL.LABEL_COMMENT_NAIYO)%>
	<TABLE style="border:0px;width:100%;table-layout:fixed;">
	<TR style="border:0px;">
		<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;">
		  <DIV class="ReadOnlybox" style="width:100%;">
			<%-- 障害表：482,486 チェックイン日：2008/5/29 対応者：SJA中島 概要：BRタグを認識するように修正 --%>
			<%-- 障害表：482,486 チェックイン日：2008/5/31 対応者：SJA中島 概要：<pre>に変更 --%>
			<%-- No486, 2008/06/03, SJA内田
						コメント欄が適切に折り返され横に伸びないように修正。
						コメント欄の最下行に不要な改行が入らないように修正
						<pre>タグ直後の改行は無視される為、予め改行をして置く(先頭行を改行した場合の対策)
				--%>
				
				<%-- No.827 2008/06/13 新実 フォントを改めて指定することで、フォントの変化を回避。 --%>
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="SashimodoshiCommentForm" property="comment" /></font></pre>&nbsp;
        	
      </DIV>
    </TD>
	</TR>
	</TABLE>
	</DIV>

</DIV>
</html:form>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>