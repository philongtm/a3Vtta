<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="SateiSincyokuForm" name="05sateiSincyokuForm" type="app.common.form.SateiSincyokuForm" />
<script>
	function syosaiCom(event,id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		form = document.forms[0];	
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}

	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;
  		  		
  		var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];
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
		<html:form action="/common/Satei_sincyoku">
			<html:hidden property="id" />
			<DIV class="mainlist">
			
				<TABLE border=0 cellSpacing=0 cellPadding=0 >
					<THEAD>
						<TR>
							<%-- フェーズ --%>
							<TH width="13%">
								<%=i18n.get(GL.OZ6109_PHASE)%>
							</TH>
							<%-- 汎用１タイトル --%>
							<TH width="6%">
								<%=SESSION_DATA_APP.getLbl_nm1()%>
							</TH>
							<%-- 組織 --%>
							<TH width="35%">
								<%=i18n.get(GL.OZ6109_SOSHIKI)%>
							</TH>
							<%-- 担当者 --%>
							<TH width="17%">
								<%=i18n.get(GL.OZ6109_TANTOU)%>
							</TH>
							<%-- 処理 --%>
							<TH width="13%">
								<%=i18n.get(GL.OZ6109_SYORI)%>
							</TH>
							<%-- 処理日時 --%>
							<TH width="16%">
								<%=i18n.get(GL.OZ6109_SYORI_NITIZI)%><br/>								
								<bean:write name="SateiSincyokuForm" property="syouri_dt_t" />
							</TH>
						</TR>
					</THEAD>
					<TBODY>
						<% if(SateiSincyokuForm.getList() != null) { %>
							<nested:iterate name="SateiSincyokuForm" property="list" indexId="idx">
								<TR>
									<%-- フェーズ --%>
									<TD width="12%">
										<nested:write property="phase" />&nbsp;
									</TD>
									<%-- 汎用１ --%>
									<TD width="6%">
										<nested:write property="hanyou1" />&nbsp;
									</TD>
									<%-- 組織 --%>
									<TD width="35%">
										<nested:write property="soshiki" />&nbsp;
									</TD>
									<%-- 担当者 --%>
									<TD width="18%">
										<nested:write property="tantou" />&nbsp;
									</TD>
									<%-- 処理 --%>
									<TD width="13%">
										<nested:equal property="link_flg" value="1">
											<a href="#" onClick="syosaiCom('comment','<nested:write property="id" />')"><nested:write property="syori" /></a>
										</nested:equal>
										<nested:equal property="link_flg" value="0"><nested:write property="syori" /></nested:equal>&nbsp;</TD>									
									</TD>
									<%-- 処理日時 --%>
									<TD width="16%">
										<nested:write property="syori_dt" />&nbsp;
									</TD>
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