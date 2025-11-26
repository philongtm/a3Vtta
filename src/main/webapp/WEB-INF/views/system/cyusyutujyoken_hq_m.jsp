<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="CyusyutujyokenForm" name="04CyusyutujyokenForm" type="app.system.form.CyusyutujyokenHqForm" />
<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御を追加 --%>
<%-- No790, 2008/06/12, SJA遠藤, セレクトボックスに初期フォーカスを当てない --%>
<%
String focus = "";
if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
}else{
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
%>
<script language="JavaScript">
	<%-- No459, 2008/05/31, SJA平道, プルダウン変更(画面再表示)でフォーカスを保持するように修正 --%>
	<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
	function setFocusId(event,val){
		document.forms[0].elements["focusId"].value = val;
		document.forms[0].elements["focusEvent"].value = event;
		document.forms[0].elements["focusId_satei"].value = event;
	
		form = document.forms[0];
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
		form.action = action;
	}
	<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
	<%-- No790, 2008/06/12, SJA遠藤, セレクトボックスに初期フォーカスを当てない --%>
	function changeFocus(val,event,sateiVal){
		if (val == '' && event == '' && sateiVal == ''){
		} else if (val == 'systemKbn' && sateiVal != 'tairyu_hantei' || sateiVal != '' && sateiVal != 'tairyu_hantei' && (event == '' || event == 'kbn_change' || event == 'sateikaisya_change' || event == 'mise_change')) {
			form = document.forms[0];
			if (val!="") {
				form.elements[val].focus();
				form.elements["focusId"].value = "";
				document.forms[0].elements["focusEvent"].value = "";
				document.forms[0].elements["focusId_satei"].value = "";
			}
		} else if(sateiVal == 'tairyu_hantei') {
			document.getElementById("iframe").contentWindow.document.forms[0].elements[sateiVal].focus();
		} else {
			setFocus('<%=focus%>');
		}
		
	<% if (focus.equals("syoriKbn") || !focus.equals("systemKbn")) { %>
		setFocus('<%=focus%>');
	<% } %> 
	}
</script>
<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御を追加 --%>
<%-- No877, 2008/06/19, SJA平林, 登録成功時にスクリプトエラーになる為、ならないように修正 --%>
<script>
	function setFocus(val){
<% if (focus.equals("systemKbn") || focus.equals("syoriKbn") || focus.equals("satei_kaisya_cd") || focus.equals("mise_cd")) { %>
		form = document.forms[0];
		if (val!="") {
    	form.elements[val].focus();
    }
<% } else if("".equals(focus)) {
   } else { %>
      document.getElementById("iframe").contentWindow.document.forms[0].elements[val].focus();
<%	} %>
  }
</script>	
</HEAD>
<BODY onload="changeFocus('<bean:write name="CyusyutujyokenForm" property="focusId" />','<bean:write name="CyusyutujyokenForm" property="focusEvent" />','<bean:write name="CyusyutujyokenForm" property="focusId_satei" />')">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="../../../image/navi001.gif" width="89" height="52">
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
<H1 class="title01"><%=i18n.get(GL.TITLE_04_00_02)%></H1>

<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
</DIV>

<DIV id="list">
<%-- No459, 2008/05/30, SJA渡辺, フォーカスの設定を追加 --%>
<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため、フォーカス削除 --%>
<html:form action="/system/cyusyutujyoken_hq" >

<html:hidden property="focusId" />
<html:hidden property="focusEvent" />
<html:hidden property="focusId_satei" />

	<DIV class="mainlist">
		<DIV class="CNDbox">
			<%=i18n.get(GL.LABEL_SYS)%>&nbsp;
			<html:select property="systemKbn" onchange="setFocusId('kbn_change','systemKbn')">
	    		<html:optionsCollection property="ar_system_kbn" value="value" label="key" />
	  	</html:select>
			&nbsp;&nbsp;
			<%=i18n.get(GL.LABEL_SYORI_KBN)%>&nbsp;
			<html:select property="syoriKbn" onchange="setFocusId('kbn_change','syoriKbn')">
	    		<html:optionsCollection property="ar_syori_kbn" value="value" label="key" />
	  	</html:select>
			&nbsp;&nbsp;
			<%=i18n.get(GL.LABEL_SATEI_CO)%>&nbsp;
			<html:select property="satei_kaisya_cd" onchange="setFocusId('sateikaisya_change','satei_kaisya_cd')" style="width:60px">
	    		<html:optionsCollection property="ar_satei_kaisya_cd" value="key" label="key" />
	  	</html:select>
			&nbsp;&nbsp;
			<%=i18n.get(GL.LABEL_MISE_CD)%>&nbsp;
			<html:select property="mise_cd" onchange="setFocusId('mise_change','mise_cd')" style="width:65px">
				<%-- No463, 2008/05/23, SJA渡辺, ブランクが複数でるのを修正 --%>
	    		<html:optionsCollection property="ar_mise_cd" value="value" label="key" />
	  	</html:select>
<%-- 要件No.四-13 2008/10/07 水口 決算期区分セレクトボックス追加 --%>
<%-- 追加開始 --%>
			&nbsp;&nbsp;
			<%=i18n.get(GL.LABEL_KESSANKI_KBN)%>&nbsp;
			<html:select property="kessanki_kbn" onchange="setFocusId('kessanki_change','kessanki_cd')">
	    		<html:optionsCollection property="kessanki_kbn_list" value="value" label="key" />
	  	</html:select>
<%-- 追加完了 --%>
		</DIV>
		<BR><BR><BR>
		
		<%-- 2008/06/11 新実 英語OSにて、タブ内に文字が入りきらない場合があるためフォントサイズを修正 --%>
		
		<logic:equal name="CyusyutujyokenForm" property="jokenKbn" value="1">
			<DIV id="tab"><span><%=i18n.get(GL.LABEL_KENTOU_J)%></span></DIV>
			<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<DIV id="tab"><a href="#" onClick="doSubmit('satei_joken')"><%=i18n.get(GL.LABEL_SATEI_TAISYOU)%><br><%=i18n.get(GL.LABEL_TAIRYU_J)%></a></DIV>
			<%} else {%>
			<DIV id="tab" style="font-size:8pt;"><a href="#" onClick="doSubmit('satei_joken')"><%=i18n.get(GL.LABEL_SATEI_TAISYOU)%><br><%=i18n.get(GL.LABEL_TAIRYU_J)%></a></DIV>
			<%}%>
			<iframe id="iframe" src="cyusyutujyoken_hq_m_kentou.jsp" width=100% height=700px></iframe>
		</logic:equal>
		<logic:equal name="CyusyutujyokenForm" property="jokenKbn" value="2">
			<DIV id="tab"><a href="#" onClick="doSubmit('kentou_joken')"><%=i18n.get(GL.LABEL_KENTOU_J)%></a></DIV>
			<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<DIV id="tab"><span><%=i18n.get(GL.LABEL_SATEI_TAISYOU)%><br><%=i18n.get(GL.LABEL_TAIRYU_J)%></span></DIV>
			<%} else {%>
			<DIV id="tab" style="font-size:8pt;"><span><%=i18n.get(GL.LABEL_SATEI_TAISYOU)%><br><%=i18n.get(GL.LABEL_TAIRYU_J)%></span></DIV>
			<%}%>
			<iframe id="iframe" src="cyusyutujyoken_hq_m_satei.jsp" width=100% height=500px></iframe>
		</logic:equal>

	</DIV>
</html:form>
</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>