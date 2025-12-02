<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>
<script>
	function downloadSubmit() {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		form = document.forms[0];
		action = form.action;
		form.target = "SAIKEN";
		form.action += "?<%=GS.EVENT%>=" + "torikomi";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
</script>
</HEAD>
<BODY onselectstart="return false" >
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<%--コンテンツ部分--%>
<DIV id="contents">
	<DIV id="list">
		<html:form action="/tairyu/toroku" 
    	       method="POST" 
    	       enctype="multipart/form-data">
		<BR><BR>
		<DIV class="mainlist">
			<TABLE style="border:0px;width:100%;" table-layout:fixed;>
				<TR style="border:0px">
					<TD style="border:0px;width:80%;word-break:break-all;">
						<input type="file" name="fileUp" value="" onkeydown="return keycancel();" onbeforeeditfocus="return false;" style="padding: 2px; width:100%;background-color: #F8F8FF;" class="singleByte">
					</TD>
				</TR>
			</TABLE>
		</DIV>
		<DIV id="submenu" class="semaku">
			<BR>
			<input type="button" value="<%=i18n.get(GL.BTN_UPLOAD_ALL)%>" onclick="downloadSubmit();window.close();"/>
			<input type="button" value="<%=i18n.get(GL.BTN_CLOSE)%>" onclick="javascript:window.close();">
		</DIV>
		</html:form>
	</DIV>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>