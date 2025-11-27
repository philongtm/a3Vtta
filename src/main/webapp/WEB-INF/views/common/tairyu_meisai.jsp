<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="TairyuMeisaiForm" name="05TairyuMeisaiForm" type="app.common.form.TairyuMeisaiForm" />
<% Pager pager = TairyuMeisaiForm.getPager(); %>

<script>
	function indexCheck(index) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["id"].value = index;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "tenpu";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
	
	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;
  		  		
  		var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];
	
		<%--
		// scrollHeightの値ピッタリでは、スクロールバーが出る可能性がある為
		// サイズに余裕を持たせる。
		--%>
		sHeight += 10;
		var sHeightMin = 450;
		if(sHeight < sHeightMin){
			sHeight = sHeightMin;
		}
		
		iframeObj.style.height = sHeight;
	}
	
</script>
</HEAD>

<BODY onload="resizeParentIFrame()">
<CENTER>

<%--コンテンツ部分--%>

<DIV id="tagcontents">
<DIV id="list">
<html:form action="/common/tairyu_meisai">
<html:hidden property="id"/>
<DIV class="mainlist">		
		<TABLE style="border:0px;width:100%;">
			<TR style="border:0px;">
				<TD style="border:0px;width:38%;"class="semaku"></TD>
				
					<%-- 表示件数セレクトボックス --%>
						<TD style="border:0px;width:10%;"class="semaku right"><%=i18n.get(GL.COMMON_SHOW)%></TD>
						<TD style="border:0px;width:10%;"class="semaku"><html:select property="view" onchange="doSubmit('show')">
		    				<html:optionsCollection name="TairyuMeisaiForm" property="ar_show" value="value" label="key" /></html:select>
						</TD>
				
					<%-- 前のXX件 --%>
	 					<TD style="border:0px;width:14%;"class="semaku right">
							<logic:notEqual name="TairyuMeisaiForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="TairyuMeisaiForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="TairyuMeisaiForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
					<%-- 次のXX件 --%>
						<TD style="border:0px;width:13%;"class="right">
							<logic:notEqual name="TairyuMeisaiForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="TairyuMeisaiForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="TairyuMeisaiForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
					<%-- XX/YY件 --%>
						<TD style="border:0px;width:15%;"class="semaku right">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
					</TD>
				</TR>
		</TABLE>
		<%-- 一覧情報 --%>
 		<TABLE style="width:98%;cellSpacing:0px;cellPadding:0px;border-collapse: collapse;table-layout:fixed;border-left-color:#000000;">
			<THEAD>
				<TR>
					<%-- 組織 --%>
					<TH colspan=9 style="width:28%;text-align:center"><%=i18n.get(GL.OZ6101_SOSHIKI)%></TH>
					<%-- 科目 --%>
					<TH style="width:8%;text-align:center"><%=i18n.get(GL.OZ6101_KANJO_CD)%></TH>
					<%-- 科目名称 --%>
					<TH style="width:17%;text-align:center"><%=i18n.get(GL.OZ6101_KANJO_NM)%></TH>
					<%-- 金額計(通貨) --%>
					<TH style="width:10%;text-align:center"><%=i18n.get(GL.OZ6101_KINGAKU_TOTAL)%><bean:write name="TairyuMeisaiForm" property="tuuka_cd" /></TH>
					<%-- 収支予定日、勘定処理日、汎用１ --%>
					<TH style="width:12%;text-align:center"><%=i18n.get(GL.OZ6101_SHUSI_DT)%><BR><%=i18n.get(GL.OZ6101_KANJO_DT)%><BR><bean:write name="TairyuMeisaiForm" property="hanyo1_title" /></TH>
					<%-- 契約伝票No、インボイスNo --%>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<TH style="width:13%;text-align:center">
					<%} else {%>
						<TH style="width:11%;text-align:center">
					<%}%>
						<%=i18n.get(GL.OZ6101_KEIYAKU_DENPYO_NO)%><BR><%=i18n.get(GL.OZ6101_INVOICE_NO)%></TH>
					<%-- 添付参照 --%>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<TH rowspan=2 style="width:6%;text-align:center"class="borderRight">
					<%} else {%>
						<TH rowspan=2 style="width:10%;text-align:center"class="borderRight">
					<%}%>
						<%=i18n.get(GL.OZ6101_TENPU_SANSYO)%></TH>
				</TR>
	 			<TR>
					<%-- 滞留区分 --%>	 			
					<TH colspan=3 style="width:8%;text-align:center"><%=i18n.get(GL.OZ6101_TAIRYU_KBN)%></TH>
					<%-- 滞留/非滞留 --%>
					<TH colspan=6 style="width:15%;text-align:center"><%=i18n.get(GL.OZ6101_TAIRYU_HITAIRYU)%></TH>
					<%-- 判定事由 --%>
					<TH colspan=4 style="width:40%;text-align:center"><%=i18n.get(GL.OZ6101_HANTEI_JIYUU)%></TH>
					<TH style="width:6%;text-align:center"><bean:write name="TairyuMeisaiForm" property="komoku1" /><BR><bean:write name="TairyuMeisaiForm" property="komoku3" /></TH>
			
				</TR>
			</THEAD>
 	 		<TBODY>
				<% if(TairyuMeisaiForm.getList() != null) { %>
					<nested:iterate name="TairyuMeisaiForm" property="list" indexId="idx">
						<TR>
							<TD colspan=9 style="width:40%;"><nested:write property="soshiki" /></TD>
							<TD style="width:8%;"><nested:write property="kanjo_kamoku_cd" /></TD>
							<TD style="width:14%;"><nested:write property="kanjo_kamoku_nm" /></TD>
							<TD style="width:12%;text-align:right"><nested:write property="kingaku_kei" /></TD>
							<TD style="width:12%;text-align:center"><nested:write property="syusi_yoteibi" /><BR><nested:write property="kanjo_syoribi" /><BR><nested:write property="hanyo1" /></TD>
							<TD style="width:12%;text-align:center;"><nested:write property="keiyaku_denpyo_no" /><BR><nested:write property="invoice_no" />
							<nested:notEmpty property="bunsyo_no">
							<TD rowspan=2 style="width:20%;text-align:center;border-bottom-color:#000000;border-right-color:#000000;">
								<input 	type="button"
									value="<%=i18n.get(GL.BTN_SANSYO)%>" 
									onClick="indexCheck(<nested:write property="id"/>)">
							</TD>
							</nested:notEmpty>
							<nested:empty property="bunsyo_no">
							<TD rowspan=2 style="width:20%;text-align:center;border-bottom-color:#000000;border-right-color:#000000;">
								<input 	type="button" 
										value="<%=i18n.get(GL.BTN_SANSYO)%>" 
								disabled>
							</TD>
							</nested:empty>
						</TR>	
				 		<TR>
							<TD colspan=3 style="width:10%;text-align:center;border-bottom-color:#000000;"><nested:write property="tairyu_kbn" /></TD>
							<TD colspan=6 style="width:15%;text-align:left;border-bottom-color:#000000;"><nested:write property="tairyu_hantei" /></TD>
							<TD colspan=4 style="width:42%;border-bottom-color:#000000;"><pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><nested:write property="hantei_jiyu" /></font></pre></TD>
							<TD style="width:5%;text-align:center;border-bottom-color:#000000;"><nested:write property="komoku1" /><BR><nested:write property="komoku3" /></TD>
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