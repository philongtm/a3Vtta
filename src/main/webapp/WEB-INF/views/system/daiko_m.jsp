<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="DaikoForm" name="04DaikoForm" type="app.system.form.DaikoForm" />

<script>
	function AddHidaiko() {
		form = document.forms[0];
		var n = form.elements["selHidaikosha"].selectedIndex;
		if(n > -1) {
			form.elements["txtHidaikosha"].value = form.elements["selHidaikosha"].options[n].text;
			form.elements["selectedHidaikoshaId"].value = form.elements["selHidaikosha"].options[n].value;
			doSubmit("hidaikoSentaku");
		}
	}
	function AddDaiko() {
		form = document.forms[0];
		var n = form.elements["selDaikosha"].selectedIndex;
		if(n > -1) {
			form.elements["txtDaikosha"].value = form.elements["selDaikosha"].options[n].text;
			form.elements["selectedDaikoshaId"].value = form.elements["selDaikosha"].options[n].value;
		}
	}
	function doDelete(idx) {
		form = document.forms[0];
		form.elements["delId"].value = idx;
		doSubmit("delete");
	}
	function doInputSearch(txtSrh) {
		var selDaikosha;
		var lstHidTantoV;
		var lstHidTantoL;
		if (txtSrh.name=="inHidaikosha") {
			selDaikosha = document.forms[0].elements["selHidaikosha"];
			lstHidTantoV = document.getElementsByName("hidHidaiko_tantoV");
			lstHidTantoL = document.getElementsByName("hidHidaiko_tantoL");
		} else {
			selDaikosha = document.forms[0].elements["selDaikosha"];
			lstHidTantoV = document.getElementsByName("hidDaiko_tantoV");
			lstHidTantoL = document.getElementsByName("hidDaiko_tantoL");
		}
		selDaikosha.options.length = 0; 
		for(var i=0; i<lstHidTantoL.length; i++) {
			if (txtSrh.value == "" || lstHidTantoL[i].value.indexOf(txtSrh.value) == 0) {
				var newItem = new Option(lstHidTantoL[i].value, lstHidTantoV[i].value);
				selDaikosha.options.add(newItem);
				
			}
		}
	}
</script>

</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7101)%></H1>
		<html:form action="/system/daiko">
		<html:hidden name="DaikoForm" property="selectedHidaikoshaId"/>
		<html:hidden name="DaikoForm" property="selectedDaikoshaId"/>
		<html:hidden name="DaikoForm" property="delId"/>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')" />
		</DIV>

		<DIV id="list">
			<DIV class="mainlist">
				<DIV class="headlist">
					<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
						<%-- 被代行者選択 --%>
						<TR>[&nbsp;<%=i18n.get(GL.OS7101_HIDAIKOSHASENTAKU)%>&nbsp;]&nbsp;</TR>
						<TR>
							<%-- 入力／検索 --%>
							<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OS7101_NYURYOKUKENSAKU)%></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
								<DIV class="box" style="margin-bottom:2px;">
									<bean:define id="disabledHidaiko" name="DaikoForm" property="disabledHidaiko" type="java.lang.Boolean" />
									<nested:text property="inHidaikosha" disabled="<%=disabledHidaiko%>" style="width:60%;" onkeyup="doInputSearch(this)" />
								</DIV>
							</TD>
						</TR>
						<TR>
							<%-- 担当者選択 --%>
							<TD width="7%"><DIV class="dottitle"><%=i18n.get(GL.OS7101_TANTOSHASENTAKU)%></DIV></TD>
							<%-- 担当者一覧 --%>
							<TD>
								<DIV style="float:left;margin-left:5px;width:35%;">
									<DIV class="boxtitle"><%=i18n.get(GL.OS7101_TANROSHAICHIRAN)%></DIV>
									<html:select name="DaikoForm" property="selHidaikosha" size="8" ondblclick="AddHidaiko()" style="width:100%;">
										<html:optionsCollection name="DaikoForm" property="ar_hidaiko_tanto" value="togo_id" label="email_addr"/>
									</html:select>
									<logic:iterate id="hidaiko_tanto" name="DaikoForm" property="ar_hidaiko_tanto">
										<input type="hidden" name="hidHidaiko_tantoV" value="<bean:write name='hidaiko_tanto' property='togo_id'/>"/>
										<input type="hidden" name="hidHidaiko_tantoL" value="<bean:write name='hidaiko_tanto' property='email_addr'/>"/>
									</logic:iterate>
								</DIV> 
								<html:button property="addHidaikosha" style="float:left;background:#CCCCCC;margin:20px 10px;" onclick="AddHidaiko()">
									&nbsp;&gt;&nbsp;
								</html:button>
								<%-- 被代行者 --%>
				  				<DIV style="float:left;width:35%;">
				  					<DIV class="boxtitle"><%=i18n.get(GL.OS7101_HIDAIKOSHA)%></DIV>
				  					<html:text name="DaikoForm" readonly="true" property="txtHidaikosha" style="width:100%;background-color: #F8F8FF;border: solid 1px #AAA;" />
				  				</DIV>
				  			</TD>
						</TR>
					</TABLE>
					<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
						<%-- 代行者選択 --%>
						<TR>[&nbsp;<%=i18n.get(GL.OS7101_DAIKOSHASENTAKU)%>&nbsp;]&nbsp;</TR>
						<TR>
							<%-- 入力／検索 --%>
							<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OS7101_NYURYOKUKENSAKU)%></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
								<DIV class="box" style="margin-bottom:2px;">
									<nested:text property="inDaikosha" style="width:60%;" onkeyup="doInputSearch(this)" />
								</DIV>
							</TD>
						</TR>
						<TR>
							<%-- 担当者選択 --%>
							<TD width="7%"><DIV class="dottitle"><%=i18n.get(GL.OS7101_TANTOSHASENTAKU)%></DIV></TD>
							<%-- 担当者一覧 --%>
							<TD>
								<DIV style="float:left;margin-left:5px;width:35%;">
									<DIV class="boxtitle"><%=i18n.get(GL.OS7101_TANROSHAICHIRAN)%></DIV>
									<html:select name="DaikoForm" property="selDaikosha" size="8" ondblclick="AddDaiko()" style="width:100%;">
										<html:optionsCollection name="DaikoForm" property="ar_daiko_tanto" value="togo_id" label="email_addr"/>
									</html:select>
									<logic:iterate id="daiko_tanto" name="DaikoForm" property="ar_daiko_tanto">
										<input type="hidden" name="hidDaiko_tantoV" value="<nested:write name='daiko_tanto' property='togo_id'/>"/>
										<input type="hidden" name="hidDaiko_tantoL" value="<nested:write name='daiko_tanto' property='email_addr'/>"/>
									</logic:iterate>
								</DIV> 
								<html:button property="addDaikosha" style="float:left;background:#CCCCCC;margin:20px 10px;" onclick="AddDaiko()">
									&nbsp;&gt;&nbsp;
								</html:button>
								<%-- 代行者 --%>
				  				<DIV style="float:left;width:35%;">
					  				<DIV class="boxtitle"><%=i18n.get(GL.OS7101_DAIKOSHA)%></DIV>
				  					<html:text name="DaikoForm" readonly="true" property="txtDaikosha" style="width:100%;background-color: #F8F8FF;border: solid 1px #AAA;" />
									<BR><BR>
									<CENTER>
									<%-- 代行設定 --%>
										<html:button property="setDaiko" style="background:#CCCCCC; padding:2px;" onclick="doSubmit('daikoSetei')">
											&nbsp;<%=i18n.get(GL.BTN_DAIKOSETEI)%>&nbsp;
										</html:button>
									</CENTER>
				  				</DIV>
				  			</TD>
						</TR>
					</TABLE>
				</DIV>
				<BR><BR>
				<%-- 代行者一覧 --%>
				[&nbsp;<%=i18n.get(GL.OS7101_DAIKOSHAICHIRAN)%>&nbsp;]
				<BR>
				<%-- ログインユーザに代わって処理を行うユーザ一覧 --%>
				<font color="#FF0000">&nbsp;&nbsp;<%=i18n.get(GL.OS7101_DAIKOSHAMSG)%></font>
				<TABLE style="width:85%;border-left-color: #AAA;" border=0 cellSpacing=0 cellPadding=0>
					<THEAD>
					<TR>
						<%-- 代行者 --%>
						<TH width="44%"><%=i18n.get(GL.OS7101_DAIKOSHA)%></TH>
						<%-- 代行者名 --%>
						<TH width="44%"><%=i18n.get(GL.OS7101_DAIKOSHAMEI)%></TH>
						<%-- 削除 --%>
						<TH width="12%"><%=i18n.get(GL.OS7101_DELETE)%></TH>
					</TR>
					</THEAD>
					<TBODY>
						<nested:iterate property="ar_daikosha" indexId="idx">
						<TR>
							<TD width="44%"><nested:write property="email_addr"/></TD>
							<TD width="44%"><nested:write property="user_nm"/></TD>
							<%-- 削除 --%>
							<TD style="width:12%;text-align:center;">
							<input type="button" value="<%=i18n.get(GL.BTN_DELETE)%>" onclick="doDelete('<nested:write property="del_id"/>')" style="background:#CCCCCC;"></TD>
						</TR>
						</nested:iterate>
					</TBODY>	
				</TABLE>
				<BR><BR>
				<%-- 被代行者一覧 --%>
				[&nbsp;<%=i18n.get(GL.OS7101_HIDAIKOSHAICHIRAN)%>&nbsp;]
				<BR>
				<%-- &nbsp;&nbsp;ログインユーザが代行者として登録されているユーザ一覧 --%>
				<font color="#FF0000">&nbsp;&nbsp;<%=i18n.get(GL.OS7101_HIDAIKOSHAMSG)%></font>
				<TABLE style="width:75%;border-left-color: #AAA;" border=0 cellSpacing=0 cellPadding=0>
					<THEAD>
					<TR>
						<%-- 被代行者 --%>
						<TH width="50%"><%=i18n.get(GL.OS7101_HIDAIKOSHA)%></TH>
						<%-- 被代行者名 --%>
						<TH width="50%"><%=i18n.get(GL.OS7101_HIDAIKOSHAMEI)%></TH>
					</TR>
					</THEAD>
					<TBODY>
						<nested:iterate property="ar_hidaikosha" indexId="idx">
						<TR>
							<TD width="50%"><nested:write property="email_addr"/></TD>
							<TD width="50%"><nested:write property="user_nm"/></TD>
						</TR>
						</nested:iterate>
					</TBODY>	
				</TABLE>
			</DIV>
		</DIV>
		</html:form>
	</DIV>
</DIV>

</CENTER>
<script type="text/javascript" language="JavaScript">
  <!--
  var focusControl = document.forms["04DaikoForm"].elements["inHidaikosha"];

  if (focusControl.type != "hidden" && !focusControl.disabled) {
     focusControl.focus();
  }
  // -->
</script>
</BODY>
</HTML>
