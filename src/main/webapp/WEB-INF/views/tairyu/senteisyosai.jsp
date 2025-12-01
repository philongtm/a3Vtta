<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>	
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="SenteisyosaiForm" name="01SenteisyosaiForm" type="app.tairyu.form.SenteisyosaiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<% 
	String rtn_id = SESSION_DATA_APP.getReturn_gamenId();
	String focus = "";
	if (GS.OB2101.equals(rtn_id)) {
		if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
			focus = "";
		}else{
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	} else if (GS.OB2104.equals(rtn_id)) {
		if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
			focus = "";
		}else{
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	} else {
		if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
			focus = "syonin_tanto";
		}else{
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	}
%>

<script>
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
    		form.elements[val].focus();
    	}
	}
</script>	

</HEAD>
<BODY id="01_02B" onload="setFocus('<%=focus%>')">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
	 	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB2102)%></H1>

		<DIV id="submenu">
			<%--ボタン表示制御 --%>
			<%if(GS.OB2101.equals(SESSION_DATA_APP.getReturn_gamenId())) {%>
				<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')">
				<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('mogitori_kaijo')">
				<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('toroku')">
				<input type="button" value="<%=i18n.get(GL.BTN_ANNULMENT)%>" onclick="doSubmit('taisyogai')">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="backConfirm('back')">
			<%} else if(GS.OB2104.equals(SESSION_DATA_APP.getReturn_gamenId())) {%>
				<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')">
				<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('syonin')">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
			<%} else {%>
				<input type="button" value="<%=i18n.get(GL.BTN_ADD)%>" onclick="doSubmit('add')">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="backConfirm('back')">
			<%}%>
			<BR><BR>
			<%--コメント表示リンク --%>
			<logic:notEmpty name="TorihikisakiBean" property="sasi_ten_flg">
				<DIV class="right">
					<a href="#" style="color:#FF0000;" onClick="doSubmit('comment')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a>
				</DIV>
			</logic:notEmpty>
		</DIV>

		<DIV id="list">
			<html:form action="/tairyu/senteisyosai">

				<DIV class="headlist">

					<%--勘定先CD --%>
					<DIV class="dottitle" style="width=8%; margin-bottom:2px;"><%=i18n.get(GL.OB2102_KANJO_CD)%></DIV>
					<DIV class="ReadOnlybox" style="width:9%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
					&nbsp;&nbsp;&nbsp;
					<%--勘定先名称 --%>
					<DIV class="dottitle" style="width=9%; margin-bottom:2px;"><%=i18n.get(GL.OB2102_KANJO_NM)%></DIV>
					<DIV class="ReadOnlybox" style="width:69%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV><br>
					<%--汎用１ --%>
					<DIV class="dottitle" style="width=5%; margin-top:2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV>
					<DIV class="ReadOnlybox" style="width:5%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="sateikaisya_cd" /></DIV>
					&nbsp;&nbsp;&nbsp;
					<%--組織 --%>
					<DIV class="dottitle" style="width=5%; margin-top:2px;"><%=i18n.get(GL.OB2102_SOSHIKI)%></DIV>
					<DIV class="ReadOnlybox" style="width:80%; margin-top:2px;"><bean:write name="TorihikisakiBean" property="soshiki" /></DIV>
					<br>
					<%if(GS.OB2104.equals(SESSION_DATA_APP.getReturn_gamenId())) {%>
						<%--選定区分 --%>
						<DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OB2102_SENTEI_KBN)%></DIV>&nbsp;
						<DIV class="ReadOnlybox" style="width=9%;margin-top:5px;"><bean:write name="TorihikisakiBean" property="sentei_kbn" /></DIV>
					<%} else {%>
						<DIV class="right">
							<%if(GS.OB2101.equals(SESSION_DATA_APP.getReturn_gamenId())) {%>
								<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%=GS.GSS%>">
									<%--対象外区分セレクトボックス --%>
									<DIV class="dottitle" style="margin-bottom:5px;"><%=i18n.get(GL.OB2102_ANNULMENT_TYPE)%></DIV>
									<DIV class="box"><html:select property="taisyogai_kbn">
										<html:optionsCollection name="SenteisyosaiForm" property="ar_taisyogai" value="value" label="key" />
	  									</html:select></DIV>
		  							&nbsp;&nbsp;&nbsp;
		  						</logic:equal>
							<%} else {%>
								<%--抽出事由セレクトボックス --%>
								<DIV class="dottitle" style="margin-bottom:5px;"><%=i18n.get(GL.OB2102_REASON)%></DIV>
							    <DIV class="box"><html:select property="tyusyutu_jiyu" style="width:150">
									<html:optionsCollection name="SenteisyosaiForm" property="ar_tyusyutu_jiyu" value="value" label="key" />
	  								</html:select></DIV>
	  							&nbsp;&nbsp;&nbsp;
		  					<%}%>
							<%--承認担当者セレクトボックス --%>
				  			<DIV class="dottitle" style="margin-bottom:5px;"><%=i18n.get(GL.OB2102_SYONIN_TANTO)%></DIV>
							<DIV class="box"><html:select property="syonin_tanto" style="width:180">
								<html:optionsCollection name="SenteisyosaiForm" property="ar_syonin_tanto" value="key" label="value" />
  								</html:select></DIV>
  						</DIV>
			  		<%}%>
				</DIV>

				<DIV class="mainlist">
					<br>
					<%--対象外・追加コメント --%>
					<TABLE style="width:100%;border:0px;table-layout:fixed;">
						<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OB2102_COMMENT)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%><BR>
						<TR style="border:0px;">
							<TD style="border:0px;">
								<%if(GS.OB2104.equals(SESSION_DATA_APP.getReturn_gamenId())) {%>
									<DIV class="ReadOnlybox" style="width:100%;">
										<pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><bean:write name="SenteisyosaiForm" property="comment" /></font></pre>&nbsp;
									</DIV>
								<%} else {%>
									<DIV style="width:100%;"><html:textarea property="comment" rows="3" style="width : 100%;"/></DIV>
								<%}%>
							</TD>
						</TR>
					</TABLE>
					<br>
					<logic:equal name="SenteisyosaiForm" property="tabValue" value="1">
						<DIV id="tab"><span><%=i18n.get(GL.TITLE_OZ6108)%></span></DIV>
						<DIV id="tab"><a href="#" onclick="doSubmit('saiken_meisai')"><%=i18n.get(GL.TITLE_OZ6105)%></a></DIV>
						<iframe src="../common/kihon_joho.jsp" width=100% height=550px>
					</logic:equal>
					<logic:equal name="SenteisyosaiForm" property="tabValue" value="2">
						<DIV id="tab"><a href="#" onclick="doSubmit('kihon_joho')"><%=i18n.get(GL.TITLE_OZ6108)%></a></DIV>
						<DIV id="tab"><span><%=i18n.get(GL.TITLE_OZ6105)%></span></DIV>
						<iframe src="../common/saiken_meisai.jsp" width=100% height=550px>
					</logic:equal>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>