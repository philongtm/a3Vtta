<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="KubunForm" name="02KubunForm" type="app.satei.form.KubunForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<script>
	function setChk(chk,obj) {
		form = document.forms[0];
		if (chk == 'hasanho') {
			parent.document.forms[0].chkFlgHasanho.value = getValue(obj);
		} else if (chk == 'kaishaho') {
			parent.document.forms[0].chkFlgKaishaho.value = getValue(obj);
		} else if (chk == 'koseho') {
			parent.document.forms[0].chkFlgKoseho.value = getValue(obj);
		} else if (chk == 'saiseho') {
			parent.document.forms[0].chkFlgSaiseho.value = getValue(obj);
		} else if (chk == 'shobun') {
			parent.document.forms[0].chkFlgShobun.value = getValue(obj);
		} else if (chk == 'sonota') {
			parent.document.forms[0].chkFlgSonota.value = getValue(obj);
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
		<DIV class="left"><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1103_HASANKOUSEI_HANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>&nbsp;&nbsp;<%=i18n.get(GL.OC1103_EXP)%></DIV>		
		<TABLE cellspacing="0" style="border:0px;width:100%;">
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgHasanho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgHasanho" onClick="setChk('hasanho',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgHasanho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgHasanho" onClick="setChk('hasanho',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_1)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP1)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgKaishaho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKaishaho" onClick="setChk('kaishaho',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgKaishaho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKaishaho" onClick="setChk('kaishaho',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_2)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP2)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgKoseho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKoseho" onClick="setChk('koseho',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgKoseho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgKoseho" onClick="setChk('koseho',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_3)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP3)%></DIV><BR>
			</TD>
		</TR>
		<logic:equal name="TorihikisakiBean" property="system_kbn" value="01">
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgSaiseho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSaiseho" onClick="setChk('saiseho',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgSaiseho" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSaiseho" onClick="setChk('saiseho',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_4)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP4)%></DIV><BR>
			</TD>
		</TR>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgShobun" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgShobun" onClick="setChk('shobun',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgShobun" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgShobun" onClick="setChk('shobun',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;"><%=i18n.get(GL.COMMON_5)%><%=i18n.get(GL.COMMON_DOT)%></DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP5)%></DIV><BR>
			</TD>
		</TR>
		</logic:equal>
		<TR style="border:0px;">
			<TD style="border:0px;width:5%;">
			<logic:equal name="KubunForm" property="chkFlgSonota" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSonota" onClick="setChk('sonota',this)" checked/></DIV>
			</logic:equal>
			<logic:notEqual name="KubunForm" property="chkFlgSonota" value="1">
				<DIV class="dottitle" style="width : 100%;"><input type=checkbox name="chkFlgSonota" onClick="setChk('sonota',this)"/></DIV>
			</logic:notEqual>
			<BR>
			</TD>
			<TD style="border:0px;width:5%;text-align:left;"><DIV class="dottitle" style="width : 100%;margin-top:4px;">
			<logic:equal name="TorihikisakiBean" property="system_kbn" value="01">
				<%=i18n.get(GL.COMMON_6)%><%=i18n.get(GL.COMMON_DOT)%>
			</logic:equal>
			<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
				<%=i18n.get(GL.COMMON_4)%><%=i18n.get(GL.COMMON_DOT)%>
			</logic:notEqual>
			</DIV><BR>
			</TD>
			<TD style="border:0px;width:90%;text-align:left;"><DIV class="dotbox" style="width : 100%;"><%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP6)%></DIV><BR>
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