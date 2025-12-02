<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="UserIchiranForm" name="06UserIchiranForm" type="app.system.form.UserIchiranForm" />
<% Pager pager = UserIchiranForm.getPager(); %>
<script language="JavaScript">

	function toroku(event,id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
</script>
</HEAD>
<BODY>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7106)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>

		<DIV id="list">
			<html:form action="/system/userIchiran" >
				
				<html:hidden property="id" />				

				<DIV class="headerlist">
					<TABLE>
						<TR>
							<%--  ユーザID --%>
							<TD style="width:3%;" class="semaku">
								<DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS7106_USER_ID)%></DIV>
							</TD>
							<TD style="width:8%;" class="semaku">
								<html:text property="user_id" style="width:80"/><%=i18n.get(GL.OS7106_FIRST_LIKE)%>
							</TD>
							<%--  氏名 --%>
							<TD style="width:4%;" class="semaku">
								<DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS7106_USER_NM)%></DIV>
							</TD>
							<TD style=";width:18%;" class="semaku">
								<html:text property="user_nm" style="width:260;ime-mode: active;"/><%=i18n.get(GL.OS7106_ALL_LIKE)%>
							</TD>
							<TD style="width:4%;"></TD>
						</TR>
					</TABLE>
					<TABLE style="width:100%">
						<%--  E-Mail --%>
						<TD style="width:3%;" class="semaku">
							<DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS7106_EMAIL)%></DIV>
						</TD>
						<TD style="width:36%;" class="semaku">
							<html:text property="email" style="width:350"/><%=i18n.get(GL.OS7106_FIRST_LIKE)%>
						</TD>
						</TR>
					</TABLE>
					<TABLE style="width:100%">
						<TR>
							<%--  汎用１セレクトボックス --%>
							<TD style="width:8%"><%=SESSION_DATA_APP.getLbl_nm1()%></TD>
							<TD style="width:12%"><html:select property="hanyo1" onchange="doSubmit('hanyo1')"   style="width:80">
									<html:optionsCollection name="UserIchiranForm" property="ar_hanyo1" value="value" label="key" /></html:select>
							</TD>
							<%--  業務フローセレクトボックス --%>
							<TD style="width:10%"><%=i18n.get(GL.OS7106_GYOUMU_HURO)%></TD>
							<%-- 課題No.217 プルダウン文字切れ対応　--%>
							<%-- 追加開始--%>
							<TD style="width:70%"><html:select property="gyoumu_huro">
							<%-- 追加完了--%>
									<html:optionsCollection name="UserIchiranForm" property="ar_gyoumu_huro" value="value" label="key" /></html:select>
							</TD>			
							<%-- 課題No.218 ボタンのフォーマットを統一--%>
							<%-- 追加開始 --%>
						</TR>						
					</TABLE>
					<DIV id="submenu"class="semaku">
						<%-- 検索ボタン --%>
						<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')" style="background:#CCCCCC;align:center">
					</DIV>	
					<%-- 追加完了 --%>

					<BR/><BR/>
					
					<TABLE style="width:100%;">
				
						<TR>
							<%--  表示件数セレクトボックス --%>
							<TD colspan="2" style="width:40%;" class="semaku"><br></TD>
							<TD style="width:10%;" class="right" class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR></TD>
							<TD style="width:10%;"class="semaku"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="UserIchiranForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>
							<TD colspan="4" class="semaku"></TD>
							<TD style="width:10%;">
								<logic:notEqual name="UserIchiranForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="UserIchiranForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="UserIchiranForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<TD style="width:10%;">
								<logic:notEqual name="UserIchiranForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="UserIchiranForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="UserIchiranForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<TD style="width:15%;" class="right" class="semaku">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
					</TABLE>				
				</DIV>
				
				<%-- 一覧情報 --%>					
				<DIV class="mainlist">
					<TABLE>
						<THEAD>
							<TR>
								<%-- ユーザID --%>
								<TH style="width:7%;"><%=i18n.get(GL.OS7106_USER_ID)%></TH>
								<%-- 氏名 --%>
								<TH style="width:15%;" colspan="1"><%=i18n.get(GL.OS7106_USER_NM)%></TH>
								<%-- 会社名 --%>
								<TH style="width:15%;" colspan="1"><%=i18n.get(GL.OS7106_KAISYA_NM)%></TH>
								<%-- 所属組織名 --%>
								<TH style="width:40%;" colspan="2"class="borderRight"><%=i18n.get(GL.OS7106_SYOZOKUSOSHIKI_NM)%></TH>								
							</TR>					
							<TR>
								<%-- メールアドレス --%>
								<TH style="width:3%;" colspan="2"><%=i18n.get(GL.OS7106_MAIL_ADDR)%></TH>
								<%-- 業務フロー --%>
								<TH style="width:8%;"><%=i18n.get(GL.OS7106_GYOUMU_HURO_NM)%></TH>
								<%-- 帳票出力言語  --%>
								<TH style="width:10%;" colspan="1"><%=i18n.get(GL.OS7106_PRINTOUT_DEFAULT_LANG_KBN)%></TH>
								<%-- メール配信 --%>
								<TH style="text-align:center;" style="width:10%;" colspan="1"class="borderRight"><%=i18n.get(GL.OS7106_MAIL_HAISHIN)%></TH>
							</TR>	
						</THEAD>
						<TBODY>
								<% if(UserIchiranForm.getList() != null) { %>
									<nested:iterate name="UserIchiranForm" property="list" indexId="idx">
										<TR>
											<TD width="7%"><a href="#" onClick="toroku('link_toroku','<nested:write property="id" />')"><nested:write property="user_id" /></a></TD>
											<TD width="15%"><nested:write property="user_nm" />&nbsp;</TD>
											<TD width="15%"><nested:write property="kaisya_nm" />&nbsp;</TD>
											<TD width="40%" colspan="2" class="borderRight"><nested:write property="syozoku_busyo_nm" />&nbsp;</TD>
										</TR>
										<TR>
											<TD width="3%" colspan="2" class="borderBottom"><nested:write property="mail_address" />&nbsp;</TD>
											<bean:define id="wf" name="list" property="workflow_h_nm"/>
											<TD width="8%" class="borderBottom"><%=wf%>&nbsp;</TD>
											<TD width="10%" class="borderBottom"><nested:write property="tyohyo_syuturyoku_lang" />&nbsp;</TD>
											<TD style="text-align:center;" width="10%" class="borderBottom borderRight"><nested:write property="mail_haishin" />&nbsp;</TD>
										</TR>
									</nested:iterate>
								<% } %>
						</TBODY>
					</TABLE>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>