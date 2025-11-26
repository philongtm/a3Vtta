<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="TairyuSincyokuForm" name="05tairyuSincyokuForm" type="app.common.form.TairyuSincyokuForm" />
<script>

function sinchoku(event, id) {
	form = document.forms[0];
	form.elements["id_sosiki"].value = id;
	doSubmit(event);
}

function comment(event, id) {
	form = document.forms[0];
	form.elements["id_sinchoku"].value = id;
	doSubmit(event);
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
		<html:form action="/common/tairyu_sincyoku">
			<html:hidden property="id_sosiki"/>
			<html:hidden property="id_sinchoku"/>
			
			<DIV class="mainlist">	
				<TABLE style="width:80%;" align="left" border=0 cellSpacing=0 cellPadding=0 >
				<THEAD>
				<TR>
					<%-- 汎用１タイトル --%>
					<TH style="width:15%;" align="left"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
					<%-- 組織 --%>
					<TH style="width:60%;" align="left"><%=i18n.get(GL.OZ6110_SOSHIKI)%></TH>
					<%-- 進捗 --%>
					<TH style="width:25%;" align="left"><%=i18n.get(GL.OZ6110_SHINCHOKU)%></TH>
				</TR>
				</THEAD>
				<nested:notEmpty property="ar_sosiki">
					<nested:iterate property="ar_sosiki" indexId="idx1">
						<TR>
							<%-- 汎用１ --%>
							<TD><nested:write property="hanyou1"/>&nbsp;</TD>
							<%-- 組織 --%>
							<TD>
								<a href="#" onClick="sinchoku('sinchoku', '<nested:write property="id" />')">
									<nested:write property="soshiki"/>
								</a>&nbsp;
							</TD>
							<%-- 進捗 --%>
							<TD>
								<nested:write property="sintyoku"/>&nbsp;
							</TD>
						</TR>
					</nested:iterate>
				</nested:notEmpty>
				<TBODY>
				</TBODY>
				</TABLE>
			</DIV>
		
			<DIV class="mainlist">
				<BR><BR>
				<TABLE border=0 cellSpacing=0 cellPadding=0>
					<THEAD>
						<TR>
							<%-- フェーズ --%>
							<TH width="12%">
								<%=i18n.get(GL.OZ6110_PHASE)%>
							</TH>
							<%-- 汎用１タイトル --%>
							<TH width="6%">
								<%=SESSION_DATA_APP.getLbl_nm1()%>
							</TH>
							<%-- 組織 --%>
							<TH width="35%">
								<%=i18n.get(GL.OZ6110_SOSHIKI)%>
							</TH>
							<%-- 担当者 --%>
							<TH width="18%">
								<%=i18n.get(GL.OZ6110_TANTOU)%>
							</TH>
							<%-- 処理 --%>
							<TH width="13%">
								<%=i18n.get(GL.OZ6110_SYORI)%>
							</TH>
							<%-- 処理日時 --%>
							<TH width="16%">
								<%=i18n.get(GL.OZ6110_SYORI_NITIZI)%><br/>								
								<nested:write property="syori_dt_til"/>
							</TH>
						</TR>
					</THEAD>
					<TBODY>
						<nested:notEmpty property="ar_sinchoku">
							<nested:iterate property="ar_sinchoku" indexId="idx2">
								<TR>
									<%-- フェーズ --%>
									<TD>
										<nested:write property="phase" />&nbsp;
									</TD>
									<%-- 汎用１タイトル --%>
									<TD>
										<nested:write property="hanyou1" />&nbsp;
									</TD>
									<%-- 組織 --%>
									<TD>
										<nested:write property="soshiki" />&nbsp;
									</TD>
									<%-- 担当者 --%>
									<TD>
										<nested:write property="tantou" />&nbsp;
									</TD>
									<%-- 処理 --%>
									<TD>
										<nested:equal property="link_flg" value="true">
											<a href="#" onClick="comment('comment', '<nested:write property="id" />')">
												<nested:write property="syori" />
											</a>
										</nested:equal>
										<nested:equal property="link_flg" value="false">
											<nested:write property="syori" />
										</nested:equal>&nbsp;
									</TD>									
									<%-- 処理日時 --%>
									<TD>
										<nested:write property="syori_dt" />&nbsp;
									</TD>
								</TR>
							</nested:iterate>
						</nested:notEmpty>
					</TBODY>
				</TABLE>
			</DIV>

		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>