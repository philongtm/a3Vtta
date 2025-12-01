<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>
<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="SincyokusyosaiForm" name="03SincyokusyosaiForm" type="app.syokai.form.SincyokusyosaiForm" />
<% Pager pager = SincyokusyosaiForm.getPager(); %>

<script language="javascript">
	function doTairyu(tab){
		document.getElementById("karento_tab").value = tab;
		doSubmit('tairyu');
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
		<%-- OS6104 OS6104 進捗状況詳細  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS6104)%></H1>
		<DIV id="submenu">
			<%-- 送信（システム管理者のみ表示） --%>
			<% if ("1".equals(SincyokusyosaiForm.getSendBtnFlg())) { %>
				<%-- 督促メール送信選択画面 --%>
				<input type="button" value="<%=i18n.get(GL.BTN_MAILSEND)%>" onclick="doSubmit('tokusokuPage')">
			<%} else if ("2".equals(SincyokusyosaiForm.getSendBtnFlg())) { %>
				<%-- 督促メールの登録・送信 --%>
				<input type="button" value="<%=i18n.get(GL.BTN_MAILSEND)%>" onclick="doSubmitNonTokusokuMailTiming('<%=SESSION_DATA_APP.getComLangMode()%>')">
			<%}%>
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		</DIV>

		<DIV id="list">
			<html:form action="/syokai/sincyokusyosai" >
				<input type="hidden" id="send" value="<%=i18n.get(GL.CONFIRM_SEND)%>" />
				<input type="hidden" id="lastMailDate" value="<%=i18n.get(GL.CONFIRM_LASTMAILDATE)%>" />
				<input type="hidden" id="lastSaveDate" value="<%=i18n.get(GL.CONFIRM_LASTSAVEDATE)%>" />
				<input type="hidden" id="syoukakko" value="<%=i18n.get(GL.COMMON_SYOUKAKKO) %>" />
				<input type="hidden" id="syoukakkoToji" value="<%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%>" />
				<input type="hidden" id="haishinDt" value="<%= SincyokusyosaiForm.getHaishin_Dt() %>" />
				<input type="hidden" id="hozonDt" value="<%= SincyokusyosaiForm.getHozon_Dt() %>" />
				<nested:hidden property="karento_tab" styleId="karento_tab"/>
				<DIV class="mainlist">
					<TABLE style="border:0px;width:85%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
						<TR style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 勘定先CD --%>
								<DIV class="dottitle"><%=i18n.get(GL.OS6104_KENSAKU_KANJO_CD)%></DIV><BR>
							</TD>
							<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="kanjo_cd"/></DIV><BR>
							</TD>
							<TD style="border:0px;width:4%;margin: 0px;padding: 0px;"><BR></TD>
							<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 勘定先名称 --%>
								<DIV class="dottitle"><%=i18n.get(GL.OS6104_KENSAKU_KANJO_NM)%></DIV><BR>
							</TD>
							<TD style="border:0px;width:51%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="kanjo_nm"/></DIV><BR>
							</TD>
						</TR>
					</TABLE>
					<BR>
					<TABLE style="border:0px;width:40%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
						<TR style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<TD style="border:0px;width:5%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 現在進捗 --%>
								<DIV class="dottitle"><%=i18n.get(GL.OS6104_GENZAI_SHINCHOKU)%></DIV><BR>
							</TD>
							<TD style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="sintyoku"/></DIV>
							</TD>
							<TD style="border:0px;width:5%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<DIV id="submenu"class="semaku">
								<nested:equal property="torimodoshi_fuka_flg" value="0">
									<nested:equal property="upd_user_id_flg" value="1">
										<input type="button" value="<%=i18n.get(GL.BTN_TORIMODOSHI)%>" onclick="doSubmit('tori')">
									</nested:equal>
								</nested:equal>
								</DIV>
							</TD>
							<TD style="border:0px;margin:0px;padding:0px;"></TD>
						</TR>
					</TABLE>
					<BR>
					<nested:equal property="karento_tab" value="2">
					<DIV id="tab">
						<%-- 査定 --%>
						<span><%=i18n.get(GL.OS6104_SATEI)%></span>
					</DIV>
					<DIV id="tab">
						<%-- 実質滞留債権判定 --%>
						<a href="#" onClick="doTairyu('1')"><%=i18n.get(GL.OS6104_ZISSHITU)%></a>
					</DIV>
					<iframe src="../common/satei_sincyoku_tab.jsp" width=100% height=450px>
					</nested:equal>
					<nested:equal property="karento_tab" value="1">
						<DIV id="tab">
							<%-- 査定 --%>
							<a href="#" onClick="doTairyu('2')"><%=i18n.get(GL.OS6104_SATEI)%></a>
						</DIV>
						<DIV id="tab">
							<%-- 実質滞留債権判定 --%>
							<span><%=i18n.get(GL.OS6104_ZISSHITU)%></a></span>
						</DIV>
						<iframe src="../common/tairyu_sincyoku_tab.jsp" width=100% height=450px>
					</nested:equal>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>