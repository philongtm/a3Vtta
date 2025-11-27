<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="KureemuSyosaiForm" name="04KureemSyosai" type="app.system.form.KureemuSyosaiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="MeisaisyosaiBean" name="app.SessionData" property="syosai_bean" type="app.MeisaisyosaiBean" />
<script>
	function download(event, id) {		
		form = document.forms[0];	
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
</script>
</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニュー部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS3103)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_SAVE)%>" onclick="doSubmit('save')" />
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="backConfirm('back')" />
		</DIV>

		<DIV id="list">

			<html:form action="/kureemu/KureemuSyosai">
				<html:hidden property="id" />
	
				<DIV class="headlist">
						<TABLE class="none" style="width:100%; border:0px; cellSpacing:0px; cellPadding:0px; table-layout:fixed;">
							<%-- 勘定先CD --%>
							<TR style="border:0px;width:100%;">
								<TD style="width:13%; border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KANJYOSAKICD)%></DIV></TD>
								<TD colspan="7" style="width:87%; border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="ReadOnlybox" style="width:16%;"><bean:write name="TorihikisakiBean" property="kanjo_cd"/>&nbsp;</DIV></TD>
							</TR>
							<%-- 勘定先名称 --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KANJYOSAKINAME)%></DIV></TD>
								<TD  colspan="7" style="border:0px;margin: 0 0 0 0;padding: 0px;"></nowrap>
									<DIV class="ReadOnlybox" style="width:70%;"><bean:write name="TorihikisakiBean" property="kanjo_nm"/>&nbsp;</DIV>
								</TD>
							</TR>
							<%-- 信用格付 --%>
							<TR style="border:0px;">	
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">	
									<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_SHINYOKAKUDUKE)%></DIV>
								</TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7" class="semaku">
									<DIV class="ReadOnlybox" style="width:8%;text-align: center;"><bean:write name="TorihikisakiBean" property="sinyoktk"/>&nbsp;</DIV>
								</TD>								
							</TR>
							<%-- セル --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_CELL)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:60%;"><bean:write name="MeisaisyosaiBean" property="cell_nm"/>&nbsp;</DIV></TD>
							</TR>
							<%-- 勘定科目 --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KANJYOKAMOKU)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:60%;"><bean:write name="MeisaisyosaiBean" property="kanjo_kamoku_nm"/>&nbsp;</DIV></TD>
							</TR>
							<TR style="border:0px;">
								<%-- 収支予定日 --%>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_SYUSIYOTEIBI)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="2"><DIV class="ReadOnlybox" style="width:95px;"><bean:write name="MeisaisyosaiBean" property="syusi_yoteibi"/>&nbsp;</DIV></TD>
								<%-- 満期日 --%>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" align="right">
									<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
										<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_MANKIBI)%></DIV>&nbsp;&nbsp;&nbsp;
									</logic:equal>
								</TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="2">
									<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
										<DIV class="ReadOnlybox"  style="width:95px;"><bean:write name="MeisaisyosaiBean" property="mankibi"/></DIV>
									</logic:equal>
								</TD>
								<%-- 勘定処理日 --%>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" align="right"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KANJYOSHORIBI)%></DIV>&nbsp;&nbsp;&nbsp;</TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="2"><DIV class="ReadOnlybox"  style="width:95px;"><bean:write name="MeisaisyosaiBean" property="kanjo_syoribi"/></DIV></TD>
							</TR>
							<%-- 契約伝票No. --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KIYAKUDENPYONO)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="keiyaku_denpyo_no"/>&nbsp;</DIV></TD>
							</TR>
							<%-- 金額計 --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KINGAKUKEI)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:30%; text-align: right;">
									<bean:write name="MeisaisyosaiBean" property="kingaku_kei"/>
									<bean:write name="MeisaisyosaiBean" property="tuuka_cd"/>&nbsp;</DIV>
								</TD>
							</TR>
							<%-- 滞留区分 --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_TAIRYUKUBUN)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox"  style="width:20%;"><bean:write name="MeisaisyosaiBean" property="tairyu_kbn"/>&nbsp;</DIV></TD>
							</TR>
							<%-- 滞留判定 --%>
							<TR style="border:0px;">
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_TAIRYUHANTEI)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="3"><DIV class="ReadOnlybox"  style="width:137;"><bean:write name="MeisaisyosaiBean" property="tairyu_hantei"/>&nbsp;</DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"colspan="2">
							<%-- クレーム債権 --%>
							<html:checkbox name="KureemuSyosaiForm" property="kureemu_saiken" />
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_KUREEMU_SAIKEN)%></DIV></TD>
								
							</TR>
							<%-- 判定事由 --%>
							<TR style="border:0px; width:100%;">
							
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:2px;"><%=i18n.get(GL.OS3103_HANTEIJIYU)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"colspan="7"><html:textarea name="KureemuSyosaiForm" property="syosai_bean.hantei_jiyu" rows="5" style="width:100%;"></html:textarea></TD>
							</TR>
							<logic:notEmpty name="MeisaisyosaiBean" property="invoice_no">
								<%-- インボイスNo. --%>
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OS3103_INVOICENO)%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="invoice_no"/>&nbsp;</DIV></TD>
								</TR>
							</logic:notEmpty>
							<logic:notEmpty name="MeisaisyosaiBean" property="komoku1">
								<%-- 項目１ --%>
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><bean:write name="KureemuSyosaiForm" property="komoku1"/></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="3"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku1"/>&nbsp;</DIV></TD>
								</TR>
							</logic:notEmpty>
							<logic:notEmpty name="MeisaisyosaiBean" property="komoku2">
								<%-- 項目２ --%>
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><bean:write name="KureemuSyosaiForm" property="komoku2"/></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="5"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku2"/>&nbsp;</DIV></TD>
								</TR>
							</logic:notEmpty>
							<logic:notEmpty name="MeisaisyosaiBean" property="komoku3">
								<%-- 項目３ --%>
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><bean:write name="KureemuSyosaiForm" property="komoku3"/></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku3"/>&nbsp;</DIV></TD>
								</TR>
							</logic:notEmpty>
							<logic:notEmpty name="MeisaisyosaiBean" property="komoku4">
								<%-- 項目４ --%>							
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><bean:write name="KureemuSyosaiForm" property="komoku4"/></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku4"/></DIV></TD>
								</TR>
							</logic:notEmpty>								
							<logic:notEmpty name="MeisaisyosaiBean" property="komoku5">		
								<%-- 項目５ --%>					
								<TR style="border:0px;">
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:1px;"><bean:write name="KureemuSyosaiForm" property="komoku5"/></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7"><DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku5"/></DIV></TD>
								</TR>
							</logic:notEmpty>
						</TABLE> 		
				</DIV>
		</html:form>
	</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>
