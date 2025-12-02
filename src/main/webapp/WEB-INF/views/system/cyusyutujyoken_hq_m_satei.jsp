<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="CyusyutujyokenForm" name="04CyusyutujyokenForm" type="app.system.form.CyusyutujyokenHqForm" />
<% 
	List list = CyusyutujyokenForm.getSateiList();
	String type = CyusyutujyokenForm.getType();
	boolean readonlyFlg = false;
	if(!"1".equals(type)) {
		readonlyFlg = true;
	}
%>
<script>
	function cyusyutujyokenLink(event, id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["selectIdx"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
	<%-- 管理票No200808070001, 2008/08/07, SJA平道, 適切なポップアップを出力するように未入力チェックを追加 --%>
	function cyusyutujyokenSakujo(event, name) {
		if (name == null || name == "") {
			alert('<%=i18n.get(GL.CONFIRM_0017)%>');
		} else {
			if(window.confirm(name + ' <%=i18n.get(GL.CONFIRM_0004)%>')){
			  document.forms[0].elements["focusEvent"].value = event;
				form = document.forms[0];
				action = form.action;
				form.target = "_top";
				form.action += "?<%=GS.EVENT%>=" + event;
				form.submit();
			}
		}
	}
	
	<%-- No459, 2008/05/31, SJA平道, プルダウン変更(画面再表示)でフォーカスを保持するように修正 --%>
	<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
	function setFocusId(event,val){
		document.forms[0].elements["focusId_satei"].value = val;
	  document.forms[0].elements["focusEvent"].value = event;
	  
		form = document.forms[0];
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
		form.action = action;
	}
	
	function changeFocus(val){
		form = document.forms[0];
		if (val!="") {
    		form.elements[val].focus();
      	form.elements["focusId_satei"].value = "";
    }
	}
	<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
	function doToroku(event) {
	 	document.forms[0].elements["focusEvent"].value = event;
	  document.forms[0].elements["focusId_satei"].value = event;
	  
	 	if(blockSubmit()==false) return;

	 	form = document.forms[0];
	 	action = form.action;
	 	form.target = "_top";
	 	form.action += "?event=" + event;
	 	form.submit();
	}
</script>


</HEAD>
<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のためonlord削除 --%>
<BODY onload="">

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<DIV id="list">
<html:form action="/system/cyusyutujyoken_hq">
<html:hidden property="selectIdx" />
<%-- No535, 2008/06/04, SJA渡辺, 滞留判定セレクトボックス選択でスクリプトエラー修正 --%>
<html:hidden property="focusId_satei" />
<html:hidden property="focusEvent" />

	<DIV class="headlist">
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<html:radio onclick="doSubmit('type')" property="type" value="1" /><span><%=i18n.get(GL.LABEL_NEW_REGISTER)%>&nbsp;</span>
		<html:radio onclick="doSubmit('type')" property="type" value="2" /><span><%=i18n.get(GL.LABEL_UPDATE)%>&nbsp;</span>
		<html:radio onclick="doSubmit('type')" property="type" value="3" /><span><%=i18n.get(GL.LABEL_DELETE)%>&nbsp;</span>
		<BR><BR>
		
		&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.LABEL_TAIRYU)%>&nbsp;&nbsp;
		<html:select property="tairyu_hantei" onchange="setFocusId('kbn_change','tairyu_hantei')" disabled="<%=readonlyFlg%>">
    	<html:optionsCollection property="ar_tairyu_jdg" value="value" label="key" />
  	</html:select>
		&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.LABEL_TUUKA)%>&nbsp;&nbsp;
		<html:text name="CyusyutujyokenForm" property="tuuka_satei" maxlength="3" />
		&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.LABEL_KINGAKU)%>&nbsp;&nbsp;
		<html:text name="CyusyutujyokenForm" property="kingaku_satei" style="text-align:right;padding-right:1px;" />
		&nbsp;&nbsp;&nbsp;
		
		<% if("3".equals(type)) { %>
		<input type="button" value="&nbsp;<%=i18n.get(GL.BTN_DELETE)%>&nbsp;" onclick="cyusyutujyokenSakujo('satei_sakujo','<bean:write name="CyusyutujyokenForm" property="tairyu_hantei_nm" />')" style="background:#CCCCCC;">
		<% } else { %>
		<input type="button" value="&nbsp;<%=i18n.get(GL.BTN_REGISTER)%>&nbsp;" onclick="doToroku('satei_touroku')" style="background:#CCCCCC;"><%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のためdoSubmitの名称変更 --%>
		<% } %>

	</DIV>
	
	<BR><BR>
	<DIV class="mainlist">
		<TABLE style="width:40%" border=0 cellSpacing=0 cellPadding=0>
		<THEAD>
		<TR>
			<TH width="35%"><%=i18n.get(GL.LABEL_TAIRYU)%></TH>
			<TH width="20%"><%=i18n.get(GL.LABEL_TUUKA)%></TH>
			<TH width="45%"><%=i18n.get(GL.LABEL_KINGAKU)%></TH>
		</TR>
		</THEAD>
		<TBODY>
		<% if(list != null) { %>
		<logic:iterate id="meisai" collection="<%=list%>" type="java.util.HashMap" indexId="idx">
		<TR>
			<TD class="center">
				<a href="#" onClick="cyusyutujyokenLink('tairyu_link',<%=idx%>)">
					<bean:write name="meisai" property="tairyu_hantei_nm" />
				</a>
			</TD>
			<TD class="center"><bean:write name="meisai" property="tuuka" />&nbsp;</TD>
			<TD class="right"><bean:write name="meisai" property="kingaku" />&nbsp;</TD>
		</TR>
		</logic:iterate>
		<% } %>

		</TBODY>
		</TABLE>
	</DIV>
</html:form>
</DIV>
</DIV>
</BODY>
</HTML>