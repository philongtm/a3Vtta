<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="WorkFlowIchiranForm" name="04WorkFlowIchiranForm" type="app.system.form.WorkFlowIchiranForm" />
<% Pager pager = WorkFlowIchiranForm.getPager(); %>

<script language="javascript">
	function detailPage(syori,idx) {
		switch(syori){
			case 'sinki':
				document.getElementById("gamenFlg").value = "1";
				doSubmit('detail');
				break;
			case 'system_gyomuhuro_toroku':
				document.getElementById("gamenFlg").value = "2";
				document.getElementById("selectIdx").value = idx;
				doSubmit('detail');
				break;
		}
	}

	<%/***********************************************************
		IEバグ対応
		テキストボックスがFORMに一つしかない場合に備え
		非表示テキストボックスを追加
	************************************************************/%>
	function blockEnterKey() {
		var f = document.forms;
		for(var elm, i = 0; elm = f[i]; i++) {
   			var input = document.createElement('input');
   			input.style.display = 'none';
 			elm.appendChild(input);
		}
	}
</script>
</HEAD>


<BODY onload="blockEnterKey()">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
	<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<%-- 業務フローパターン一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7104)%></H1>
		<DIV id="submenu">
			<%-- 新規作成 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_SHINKI)%>" onclick="detailPage('sinki','0')">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>
		
		<DIV id="list">
		<html:form action="/system/workflowichiran" >
			<html:hidden property="id" styleId="selectIdx"/>
			<html:hidden property="gamen_flg" styleId="gamenFlg"/>
			<DIV class="headerlist">
				<TABLE style="width:100%;">
					<TR >
						<%-- システム --%>
						<TD style="width:3%;"><%=i18n.get(GL.OS7104_SYSTEM)%></TD>
						<TD style="width:5%;">
							<html:select property="systemkbn" style="width:100" onchange="doSubmit('change')">
								<html:optionsCollection name="WorkFlowIchiranForm" property="ar_systemkbn" value="value" label="key" />
							</html:select>
						</TD>
						<%-- 汎用1 --%>
						<TD style="width:4%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TD>
						<TD style="width:17%;"class="semaku">
							<html:select property="hanyou1" style="width:70">
								<html:option value=""></html:option>
								<html:optionsCollection name="WorkFlowIchiranForm" property="ar_hanyou1" value="value" label="key" />
							</html:select>
						</TD>
					<TR>
						<TD style="width:17%;" class="semaku" colspan="2">
						<%-- 業務フローパターン名称 --%>
							<DIV ><%=i18n.get(GL.OS7104_WORKFLOW_NM)%></DIV>
						</TD>
						<TD style="width:30%;" class="semaku" colspan="2" >
							<html:text property="workflow_nm" style="width:80%" maxlength="200" styleClass="doubleByte"/><%=i18n.get(GL.OS7104_BUBUN_ITTI)%>
						</TD>
						<%-- 課題No.218 ボタンのフォーマットを統一--%>
						<%-- 追加開始 --%>
					</TR>
				</TABLE>
					<DIV id="submenu"class="semaku">
						<%-- 検索ボタン --%>
						<input type="button" value="&nbsp;<%=i18n.get(GL.BTN_SEARCH)%>&nbsp;" onclick="doSubmit('kensaku')" style="background:#CCCCCC"/>
					</DIV>
						<%-- 追加完了 --%>
				<TABLE style="width:100%;">
					<TR >
						<TD style="width:37%;"class="semaku"><BR>
						</TD>
							
						<TD align="right" style="width:10%;"class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR>
						</TD>
						<TD style="border:0px;width:9%;"class="semaku">
							<html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="WorkFlowIchiranForm" property="ar_show" value="value" label="key" />
							</html:select>
						</TD>
						
						<TD align="right" style="width:13%;"class="semaku"><BR></TD>
						<%-- ←前のXX件 --%>
						<TD style="width:10%;">
							<logic:notEqual name="WorkFlowIchiranForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="WorkFlowIchiranForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="WorkFlowIchiranForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- 次のXX件→ --%>
						<TD style="width:10%;">
							<logic:notEqual name="WorkFlowIchiranForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="WorkFlowIchiranForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="WorkFlowIchiranForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<TD style="width:5%;"class="semaku"><BR></TD>
						<%-- XX/YY件 --%>
						<TD style="width:13%;">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
						</TD>
					</TR>
				</TABLE>
				</DIV>
				<DIV class="mainlist">
					<TABLE style="width=100%" style="border-left-color: #AAA;">
						
						<TR>
							<%-- パターンID --%>
							<TH width="9%"><p align="left"><%=i18n.get(GL.OS7104_PATTERN_ID)%></p></TH>
							<%-- システム --%>
							<TH width="9%"><p align="left"><%=i18n.get(GL.OS7104_SYSTEM)%></p></TH>
							<%-- 汎用1 --%>
							<TH width="18%"><p align="left"><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>
							<%-- 業務フローパターン名称(日本語) --%>
							<TH width="28%"><p align="left"><%=i18n.get(GL.OS7104_WORKFLOW_NM_JP)%></p></TH>
							<%-- 業務フローパターン名称(英語) --%>
							<TH width="32%"><p align="left"><%=i18n.get(GL.OS7104_WORKFLOW_NM_EN)%></p></TH>
						</TR>
					<% if(WorkFlowIchiranForm.getList() != null) { %>	
						<nested:iterate name="WorkFlowIchiranForm" property="list" indexId="idx">
							<TR>
								<TD >
									<a href="#" onClick="detailPage('system_gyomuhuro_toroku','<nested:write property="id" />')">
										<nested:write property="display_workflowId" />
									</a>
								</TD>
								<TD ><nested:write property="workflowSystemkbn_nm" /></TD>
								<TD ><nested:write property="bunrui1" /></TD>
								<TD ><nested:write property="workflow_nm_ja" /></TD>
								<TD ><nested:write property="workflow_nm_en" /></TD>
							</TR>
						</nested:iterate>
					<% } %>
				</TABLE>
			</DIV>
		</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>