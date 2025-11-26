<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>

<bean:define id="HikiateSyoninSyosaiForm" name="06HikiateSyoninSyosaiForm" type="app.hikiate.form.HikiateSyoninSyosaiForm" />

</HEAD>

<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="../../../image/navi001.gif" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="../image/<%=i18n.get(GL.IMG_TITLE)%>.gif" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
	<%@ include file = "/menu.jspf" %>
	</DIV>
	<%--コンテンツ部分--%>
	<DIV id="contents">
		<logic:equal name="HikiateSyoninSyosaiForm" property="tabId" value="1">
			<%-- 引当金確認 承認一覧  --%>
			<H1 class="title01"><%=i18n.get(GL.TITLE_OD1104)%></H1>
		</logic:equal>
		<logic:equal name="HikiateSyoninSyosaiForm" property="tabId" value="2">
			<%-- 引当金検証 承認一覧  --%>
			<H1 class="title01"><%=i18n.get(GL.TITLE_HIKIATEKENSYO_SYONIN)%></H1>
		</logic:equal>
		<DIV id="submenu">
		<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<%-- 差戻 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashi')">
			<%-- 承認実行 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('zikko')">
			<%-- 添付参照 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		<%} else {%>
			<%-- 差戻 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashi')"style="WIDTH: 90px; HEIGHT: 22px">
			<%-- 承認実行 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('zikko')"style="WIDTH: 90px; HEIGHT: 22px">
			<%-- 添付参照 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpu')"style="font-size:10px;WIDTH: 90px; HEIGHT: 22px">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')"style="WIDTH: 90px; HEIGHT: 22px">
		<%}%>
		</DIV>
		<DIV id="list">
			<html:form action="/hikiate/hikiate_syonin_syosai" >
				<DIV class="headlist">
					<%-- 汎用１ --%>
					<DIV class="dottitle" style="width=5%;margin-bottom: 2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV>
					<DIV style="width=5%;margin-bottom: 2px;" class="ReadOnlybox">
						<bean:write name="HikiateSyoninSyosaiForm" property="hanyou1"/>
					</DIV>
					&nbsp;&nbsp;&nbsp;
					<%-- 組織 --%>
					<DIV class="dottitle" style="width=5%;margin-bottom: 2px;"><%=i18n.get(GL.OD1104_SOSHIKI)%></DIV>
					<DIV style="width=80%; margin-bottom: 2px;" class="ReadOnlybox">
						<bean:write name="HikiateSyoninSyosaiForm" property="soshiki_nm"/>
					</DIV>
					<br>
					<%-- 勘定先CD --%>
					<DIV class="dottitle" style="width=9%"><%=i18n.get(GL.OD1104_KANJO_CD)%></DIV>
					<DIV style="width=9%" class="ReadOnlybox">
						<bean:write name="HikiateSyoninSyosaiForm" property="kanjo_cd"/>
					</DIV>
					&nbsp;
					<%-- 勘定先名称 --%>
					<DIV class="dottitle" style="width=11%;">&nbsp;&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OD1104_KANJO_NM)%></DIV>
					<DIV style="width=67%;" class="ReadOnlybox ">
						<bean:write name="HikiateSyoninSyosaiForm" property="kanjo_nm"/>
					</DIV>
				</DIV>
				
				<DIV class="mainlist">
				<logic:equal name="HikiateSyoninSyosaiForm" property="tabId" value="1">
				<%-- 引当金確認  --%>
				<DIV id="tab"><span><%=i18n.get(GL.TITLE_OD1102)%></span></DIV>
					<%-- OZ6106_引当金確認照会タブ --%>
					<iframe src="../common/kakunin.jsp" width=100% height=700px>
					
					
					</iframe>
				</DIV>
				</logic:equal>
				<logic:equal name="HikiateSyoninSyosaiForm" property="tabId" value="2">
				<%-- 引当金検証  --%>
				<DIV id="tab"><span><%=i18n.get(GL.TITLE_HIKIATEKENSYO_TAB)%></span></DIV>
					<%-- 1.5次版機能引当金検証タブ --%>
					<iframe src="../common/kensyo.jsp" width=100% height=700px>
					
					
					</iframe>
				</DIV>
				</logic:equal>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>