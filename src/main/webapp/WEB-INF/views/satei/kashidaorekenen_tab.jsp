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
		if (chk == 'tyoka') {
			parent.document.forms[0].chkFlgTyoka.value = getValue(obj);
		} else if (chk == 'kanwa') {
			parent.document.forms[0].chkFlgKanwa.value = getValue(obj);
		} else if (chk == 'entai') {
			parent.document.forms[0].chkFlgEntai.value = getValue(obj);
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
		<DIV class="left"><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1103_KASHITAORE_HANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>&nbsp;&nbsp;<%=i18n.get(GL.OC1103_EXP)%></DIV>
		
	<TABLE cellspacing="0" style="border:0px;width:100%;">
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;"><DIV style="width : 100%;">
			<logic:equal name="KubunForm" property="chkFlgTyoka" value="1">
				<DIV class="dottitle" style="width : 100%;margin-top:6px;"><input type=checkbox name="chkFlgTyoka" onClick="setChk('tyoka',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgTyoka" value="1">
				<DIV class="dottitle" style="width : 100%;margin-top:6px;"><input type=checkbox name="chkFlgTyoka" onClick="setChk('tyoka',this)"/></DIV>
			</logic:notEqual>
			</DIV><BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:11px;"><%=i18n.get(GL.COMMON_1)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_KASHITAORE_EXP1)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;"><DIV style="width : 100%;">
			<logic:equal name="KubunForm" property="chkFlgKanwa" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKanwa" onClick="setChk('kanwa',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgKanwa" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKanwa" onClick="setChk('kanwa',this)"/></DIV>
			</logic:notEqual>
			</DIV><BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:10px;"><%=i18n.get(GL.COMMON_2)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_KASHITAORE_EXP2)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;"><DIV style="width : 100%;">
			<logic:equal name="KubunForm" property="chkFlgEntai" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgEntai" onClick="setChk('entai',this)" checked></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgEntai" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgEntai" onClick="setChk('entai',this)"></DIV>
			</logic:notEqual>
			</DIV><BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_3)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_KASHITAORE_EXP3)%></DIV><BR>
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