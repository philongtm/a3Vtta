<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="RyuhoSaimuSyokaiForm" name="RyuhoSaimuSyokaiForm" type="app.common.form.RyuhoSaimuSyokaiForm" />

<% Pager pager = RyuhoSaimuSyokaiForm.getPager(); %>
<script type='text/javascript'>

	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;
	  	var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];

		<%--
		// scrollHeightの値ピッタリでは、スクロールバーが出る可能性がある為
		// サイズに余裕を持たせる。
		--%>
		sHeight += 10;

		var sHeightMin = 470;
		if(sHeight < sHeightMin){
			sHeight = sHeightMin;
		}
		iframeObj.style.height = sHeight;
	}

</script>

<BODY onload="resizeParentIFrame()">

<CENTER>

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<DIV id="list">
<html:form action="/common/ryuho_saimu">
<DIV class="mainlist">		
	<TABLE style="border:0px;">
		<TR style="border:0px;">
			<%-- 債務総計 --%>
			<TD style="width=11%;border:0px;" class="left">
				<%=i18n.get(GL.OZ6107_SAIMU_KEI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
			</TD>
			<TD style="width=14%;border:0px;" class="right">
				&nbsp;<bean:write name="RyuhoSaimuSyokaiForm" property="saimu_kei" />
			</TD>
 			<%-- 表示件数セレクトボックス --%> 
			<TD rowspan="2" style="width=25%;border:0px;" class="right">
				<%=i18n.get(GL.COMMON_SHOW)%>
			</TD>
			<TD rowspan="2" style="width=10%;border:0px;" class="left"><html:select property="view" onchange="doSubmit('show')">
		    	<html:optionsCollection name="RyuhoSaimuSyokaiForm" property="ar_show" value="value" label="key" /></html:select>
			</TD>
			<%-- ←前のXX件 --%>
			<TD rowspan="2"style="width:13%;border:0px;">
				<logic:notEqual name="RyuhoSaimuSyokaiForm" property="x" value="">
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<a href="#" onClick="doSubmit('prevX')"><bean:write name="RyuhoSaimuSyokaiForm" property="x" /></a>
					<%} else {%>
						<a href="#" onClick="doSubmit('prevX')"><bean:write name="RyuhoSaimuSyokaiForm" property="xen" /></a>
					<%}%>
				</logic:notEqual>
			</TD>
			<%-- 次のXX件→ --%>
			<TD rowspan="2"style="width:14%;border:0px;">
				<logic:notEqual name="RyuhoSaimuSyokaiForm" property="y" value="">
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<a href="#" onClick="doSubmit('nextY')"><bean:write name="RyuhoSaimuSyokaiForm" property="y" /></a>
					<%} else {%>
						<a href="#" onClick="doSubmit('nextY')"><bean:write name="RyuhoSaimuSyokaiForm" property="yen" /></a>
					<%}%>
				</logic:notEqual>
			</TD>
			<%-- XX/YY件 --%>
			<TD rowspan="2"class="right" style="width:12%;border:0px;">
				<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
			</TD>
		</TR>
		<%-- 留保債務総計 --%>
		<TR style="border:0px;">
			<TD style="width=13%;border:0px;" class="left">
				<%=i18n.get(GL.OZ6107_RYUHO_SAIMU_KEI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
			</TD>
			<TD style="width=14%;border:0px;" class="right">
				&nbsp;<bean:write name="RyuhoSaimuSyokaiForm" property="ryuhosaimu_kei" />
			</TD>
		</TR>	
	</TABLE>
	<%-- 一覧情報 --%>
	<TABLE style="width:99%;table-layout:fixed;border-left-color:#000000;" border=0 cellSpacing=0 cellPadding=0>
		<THEAD>
			<TR>
				<%-- 組織 --%>
				<TH colspan="4" style="width:45%;text-align:center;">
					<%=i18n.get(GL.OZ6107_SOSHIKI)%></TH>
				<%-- 科目 --%>
				<TH colspan="2" style="width:20%;text-align:center;">
					<%=i18n.get(GL.OZ6107_KANJO_KAMOKU_CD)%></TH>
				<%-- 科目名称 --%>
				<TH colspan="1" style="width:34%;text-align:center;"class="borderRight">
					<%=i18n.get(GL.OZ6107_KANJO_KAMOKU_NM)%></TH>
			</TR>
			<TR>
				<%-- 収支予定日 --%>
				<TH colspan="1" style="width:15%;text-align:center;">
					<%=i18n.get(GL.OZ6107_SHUSI_DT)%></TH>
				<%-- 契約No --%>
				<TH  colspan="2" style="width:10%;text-align:center;">
					<%=i18n.get(GL.OZ6107_KEIYAKU_DENPYO_NO)%></TH>
				<%-- 備考 --%>
				<TH colspan="2" style="width:45%;text-align:center;">
					<%=i18n.get(GL.OZ6107_BIKO)%></TH>
				<%-- 留保債務 --%>
				<TH colspan="1" style="width:10%;text-align:center;">
					<%=i18n.get(GL.OZ6107_RYUHOSAIMU)%></TH>
				<%-- 金額計 --%>
				<TH colspan="1" style="width:19%;text-align:center;"class="borderRight">
					<%=i18n.get(GL.OZ6107_KINGAKU_TOTAL)%><bean:write name="RyuhoSaimuSyokaiForm" property="tuuka_cd" /></TH>
			</TR>
		</THEAD>
		<TBODY>
			<% if(RyuhoSaimuSyokaiForm.getList() != null) { %>
			<nested:iterate name="RyuhoSaimuSyokaiForm" property="list" indexId="idx">
			<TR>
				<TD colspan="4" width="45%" ><nested:write property="soshiki" />&nbsp;</TD>
				<TD colspan="2" width="20%"class='center' ><nested:write property="kanjo_kamoku_cd" />&nbsp;</TD>
				<TD colspan="1" width="34%" class="borderRight "><nested:write property="kanjo_kamoku_nm" />&nbsp;</TD>
			</TR>
			<TR>
				<TD colspan="1" width="15%" class="borderBottom "><nested:write property="syusi_yoteibi" />&nbsp;</TD>
				<TD colspan="2" width="10%" class="borderBottom "><nested:write property="keiyaku_denpyo_no" />&nbsp;</TD>
				<TD colspan="2" width="45%" class="borderBottom ">
					<logic:empty name="list" property="biko">
						&nbsp;
					</logic:empty><pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><bean:write name="list" property="biko" /></font></pre></TD>
					
				<TD colspan="1" width="10%" class="borderBottom center">					
					<nested:write property="ryuhosaimu" />&nbsp;</TD>
				<TD colspan="1" width="19%" class="borderBottom borderRight right">
					<nested:write property="kingaku_kei" />&nbsp;</TD>
			</TR>
			</nested:iterate>
			<% } %>
		</TBODY>
	</TABLE>
	</DIV>
</html:form>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>