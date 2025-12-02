<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="SaikenMeisaiSyokaiForm" name="SaikenMeisaiSyokaiForm" type="app.common.form.SaikenMeisaiSyokaiForm" />
<% Pager pager = SaikenMeisaiSyokaiForm.getPager(); %>

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
<html:form action="/common/saiken_meisai">
<html:hidden property="id"/>
<DIV class="mainlist">
		<TABLE style="border:0px;width:100%;">
			<TR style="border:0px;">
			<%-- 債権残高計 --%>
				<TD style="text-align: left;width:20%;border:0px;">
					<%=i18n.get(GL.OZ6105_SAIKEN_ZAN)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
				</TD>
				<TD style="text-align: right;float:left;width:13%;border:0px;">
					<bean:write name="SaikenMeisaiSyokaiForm" property="saiken_zankei" />
				</TD>
				
			<%-- 表示件数セレクトボックス --%>
				<TD rowspan="4" style="text-align: right;width:8%;border:0px;vertical-align:bottom;">
					<%=i18n.get(GL.COMMON_SHOW)%></TD>
				</TD>
			
				<TD rowspan="4" style="text-align: left;width:10%;border:0px;vertical-align:bottom;">
					<html:select property="view" onchange="doSubmit('show')">
						<html:optionsCollection name="SaikenMeisaiSyokaiForm" property="ar_show" value="value" label="key" /></html:select>
				</TD>

			<%-- 前のXX件 --%>
				<TD rowspan="4" style="text-align:right;width:16%;border:0px;vertical-align:bottom;">
								<logic:notEqual name="SaikenMeisaiSyokaiForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SaikenMeisaiSyokaiForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SaikenMeisaiSyokaiForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
				</TD>
				
			<%-- 次のXX件 --%>
				<TD rowspan="4" style="text-align:left;width:15%;border:0px;vertical-align:bottom;">
							<logic:notEqual name="SaikenMeisaiSyokaiForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SaikenMeisaiSyokaiForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SaikenMeisaiSyokaiForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>	
							
			<%-- XX/YY件 --%>
				<TD rowspan="4" style="text-align:right;width:15%;border:0px;vertical-align:bottom;">
						<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
				</TD>
					</DIV>
				
			</TR>
			
			<%-- 保証債務合計 --%>
			<TR style="border:0px;">
				<TD style="text-align: left;width:11%;border:0px;">
					<%=i18n.get(GL.OZ6105_HOSYO_SAIMU)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
				</TD>
				<TD style="text-align: right;width:14%;border:0px;">
					<bean:write name="SaikenMeisaiSyokaiForm" property="hosyo_saimu" />
				</TD>
			</TR>
			<%-- 引当金合計 --%>
			<TR style="border:0px;">
				<TD style="text-align: left;width:11%;border:0px;">
					<%=i18n.get(GL.OZ6105_HIKIATE_TOTAL)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
				</TD>
				<TD style="text-align: right;width:14%;border:0px;">
					<bean:write name="SaikenMeisaiSyokaiForm" property="hikiatekin" />
				</TD>
			</TR>
			<%-- 滞留債権合計 --%>
			<TR style="border:0px;">
				<TD style="text-align: left;width:11%;border:0px;">
					<%=i18n.get(GL.OZ6105_CREDIT_TOTAL)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>
				</TD>
				<TD style="text-align: right;width:14%;border:0px;">
					<bean:write name="SaikenMeisaiSyokaiForm" property="tairyu_saimu" />
				</TD>
			</TR>
			</TABLE>
		<%-- 一覧情報 --%>
 		<TABLE style="width:98%;cellSpacing:0px;cellPadding:0px;border-collapse: collapse;table-layout:fixed;border-left-color:#000000;">
			<THEAD>
				<TR>
					<%-- 組織 --%>
					<TH colspan=9 style="width:28%;text-align:center"><%=i18n.get(GL.OZ6105_SOSHIKI)%></TH>
					<%-- 科目 --%>
					<TH style="width:8%;text-align:center"><%=i18n.get(GL.OZ6105_KANJO_CD)%></TH>
					<%-- 科目名称 --%>
					<TH style="width:17%;text-align:center"><%=i18n.get(GL.OZ6105_KANJO_NM)%></TH>
					<%-- 金額計(通貨) --%>
					<TH style="width:12%;text-align:center"><%=i18n.get(GL.OZ6105_KINGAKU_TOTAL)%><bean:write name="SaikenMeisaiSyokaiForm" property="tuuka_cd" /></TH>
					<%-- 収支予定日、勘定処理日、汎用１ --%>
					<TH style="width:10%;text-align:center"><%=i18n.get(GL.OZ6105_SHUSI_DT)%><BR><%=i18n.get(GL.OZ6105_KANJO_DT)%><BR><bean:write name="SaikenMeisaiSyokaiForm" property="hanyo1_title" /></TH>
					<%-- 契約伝票No、インボイスNo --%>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<TH style="width:13%;text-align:center">
					<%} else {%>
						<TH style="width:11%;text-align:center">
					<%}%>
						<%=i18n.get(GL.OZ6105_KEIYAKU_DENPYO_NO)%><BR><%=i18n.get(GL.OZ6105_INVOICE_NO)%></TH>
					<%-- 添付参照 --%>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
						<TH rowspan=2 style="width:6%;text-align:center"class="borderRight">
					<%} else {%>
						<TH rowspan=2 style="width:10%;text-align:center"class="borderRight">
					<%}%>
						<%=i18n.get(GL.OZ6105_TENPU_SANSYO)%></TH>
				</TR>
	 			<TR>
					<%-- 滞留区分 --%>	 			
					<TH colspan=3 style="width:8%;text-align:center"><%=i18n.get(GL.OZ6105_TAIRYU_KBN)%></TH>
					<%-- 滞留/非滞留 --%>
					<TH colspan=6 style="width:15%;text-align:center"><%=i18n.get(GL.OZ6105_TAIRYU_HITAIRYU)%></TH>
					<%-- 判定事由 --%>
					<TH colspan=4 style="width:40%;text-align:center"><%=i18n.get(GL.OZ6105_HANTEI_JIYUU)%></TH>
					<TH style="width:6%;text-align:center"><bean:write name="SaikenMeisaiSyokaiForm" property="komoku1" /><BR><bean:write name="SaikenMeisaiSyokaiForm" property="komoku3" /></TH>
			
				</TR>
			</THEAD>
 	 		<TBODY>
				<% if(SaikenMeisaiSyokaiForm.getList() != null) { %>
					<nested:iterate name="SaikenMeisaiSyokaiForm" property="list" indexId="idx">
						<TR>
							<TD colspan=9 style="width:40%;"><nested:write property="soshiki" /></TD>
							<TD style="width:8%;"><nested:write property="kanjo_kamoku_cd" /></TD>
							<TD style="width:14%;word-break:break-all;"><nested:write property="kanjo_kamoku_nm" /></TD>
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