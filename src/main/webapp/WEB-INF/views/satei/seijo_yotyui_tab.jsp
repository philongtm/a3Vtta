<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="KubunForm" name="02KubunForm" type="app.satei.form.KubunForm" />
<script>
	function setChk(chk,obj) {
		form = document.forms[0];
		if (chk == 'seijo') {
			parent.document.forms[0].chkFlgSeijo.value = getValue(obj);
			parent.document.forms[0].chkFlgYochui.value = 0;
			for (i=0;i<form.getElementsByTagName("input").length;i++) {
				if (form.getElementsByTagName("input")[i].name == 'chkFlgYochui') {
					form.getElementsByTagName("input")[i].checked = false;
				}
			}
		} else if (chk == 'yotyui') {
			parent.document.forms[0].chkFlgYochui.value = getValue(obj);
			parent.document.forms[0].chkFlgSeijo.value = 0;
			for (i=0;i<form.getElementsByTagName("input").length;i++) {
				if (form.getElementsByTagName("input")[i].name == 'chkFlgSeijo') {
					form.getElementsByTagName("input")[i].checked = false;
				}
			}
		}
	}
	function getValue(obj) {
		if (obj.checked) {
			return 1;
		} else {
			return 0;
		}
	}
</script>
</HEAD>
<BODY onload="">
<CENTER>

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<DIV id="list">
<html:form action="/satei/kubun">
	<DIV class="mainlist">
		<DIV class="left"><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1103_TORI_KBN_HANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%></DIV>
		
	<TABLE cellspacing="0" style="border:0px;width:100%;">
		<TR style="border:0px;">
			<TD style=";border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgSeijo" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSeijo" onClick="setChk('seijo',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgSeijo" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSeijo" onClick="setChk('seijo',this)"/></DIV>
			</logic:notEqual>
			</TD>
			<TD style=";border:0px;width:10%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.OC1103_SEIJOSAKI)%></DIV><BR>
			</TD>
			<TD style=";border:0px;width:85%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_SEIJO_EXP)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style=";border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgYochui" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgYochui" onClick="setChk('yotyui',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgYochui" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgYochui" onClick="setChk('yotyui',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style=";border:0px;width:10%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.OC1103_YOCHUISAKI)%></DIV><BR>
			</TD>
			<TD style=";border:0px;width:85%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_YOCHUI_EXP)%></DIV><BR>
			</TD>
		</TR>
	</TABLE>
	</DIV>
</html:form>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>