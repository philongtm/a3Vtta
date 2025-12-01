<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="WorkFlowTorokuForm" name="05WorkFlowTorokuForm" type="app.system.form.WorkFlowTorokuForm" />
<% Pager pager = WorkFlowTorokuForm.getPager(); %>

<script language="javascript">
	function selReadFlg(p1,p2) {



		if(document.getElementById(p1).checked == true) {
			document.getElementById(p2).disabled = false;
			// 二次査定区分セレクトボックスがの設定(チェックオンの場合)
			if(p1 == 'bl_ni_touroku' || p1 == 'bl_ni_shounin'){
					//新規の場合
				if(document.getElementById('gamenFlg').value == '1'){
					// システムセレクトボックスが「GSS」（システム区分が'01'）
					if(document.getElementById('systemkbn').value == '01'){    
						document.getElementById('ni_satei').disabled = false;
					}
					//リンクより遷移の場合
				}else if(document.getElementById('gamenFlg').value == '2'){  
					if(document.getElementById('systemkbn').value == '01'){
						document.getElementById('ni_satei').disabled = false;
					}else{
						document.getElementById('ni_satei').disabled = true;
						document.getElementById('ni_satei').value = '';
					}
				}
			}

		}else{
			document.getElementById(p2).disabled = true;
			document.getElementById(p2).value = '';
			// 二次査定区分セレクトボックスがの設定(チェックオフの場合)

			if(p1 != 'bl_ni_touroku' || p1 != 'bl_ni_shounin'){
				if(document.getElementById('bl_ni_touroku').checked == false && document.getElementById('bl_ni_shounin').checked == false ){
					document.getElementById('ni_satei').disabled = true;
					document.getElementById('ni_satei').value = '';
				}
			}
		}


	}
	
	function setChange(object) {
		if(object.value == '01' && document.getElementById('bl_ni_touroku').checked ){
			document.getElementById('ni_satei').disabled = false;
		}else{
			document.getElementById('ni_satei').disabled = true;
		}
		doSubmit('change');
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
		<%-- 業務フローパターン一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7105)%></H1>
		<DIV id="submenu">
			<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="1">
				<%-- 登録   --%>
				<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('toroku')">
			</logic:equal>
			<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="2">
				<%-- 更新   --%>
				<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('update')">
			</logic:equal>
			<%-- 削除 --%>
			<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="2">
				<input type="button" value="<%=i18n.get(GL.BTN_DELETE)%>" onclick="doSubmit('delete')">
			</logic:equal>
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		</DIV>
		
		<DIV id="list">
			<html:form action="/system/workflowtoroku" >
				<html:hidden property="gamenFlg" styleId="gamenFlg"/>
				<DIV class="headerlist">
					<table style="width:100%;">
						<tr>
							<td style="width:7%"></td>
							<%-- システム --%>
							<td style="width:24%"><%=i18n.get(GL.OS7104_SYSTEM)%></td>
							<td style="width:150">
							<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="1">
								<html:select property="systemkbn" styleId="systemkbn" style="width:150" onchange="setChange(this)">
									<html:optionsCollection name="WorkFlowTorokuForm" property="ar_systemkbn" value="value" label="key" />
								</html:select>
							</logic:equal>
							<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="2">
								<DIV class="ReadOnlybox"style="width:150;">
									<nested:hidden name="WorkFlowTorokuForm" property="systemkbn" />
									<nested:write property="systemkbn_nm" />
								</DIV>
							</logic:equal>
							</td>
						</tr>
						<tr>
							<td style="width:7%"></td>
							<%-- 汎用1 --%>
							<td style="width:24%"><%=SESSION_DATA_APP.getLbl_nm1()%></td>
							<td style="width:150">
							<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="1">
								<html:select property="hanyou1" style="width:150">
									<html:option value=""></html:option>
									<html:optionsCollection name="WorkFlowTorokuForm" property="ar_hanyou1" value="value" label="key" />
								</html:select>
							</logic:equal>
							<logic:equal name="WorkFlowTorokuForm" property="gamenFlg" value="2">
								<DIV class="ReadOnlybox"style="width:150;">
									<nested:write property="hanyou1"/>
								</DIV>
							</logic:equal>
							</td>
						</tr>
						<tr>
							<td style="width:7%"></td>
							<%-- 業務フローパターン名称(日本語) --%>
							<td style="width:24%"><%=i18n.get(GL.OS7105_NAME_JP)%></td>
							<td>
								<html:text property="workflow_nm_ja" maxlength="200" style="width:280;" styleClass="doubleByte"/>
							</td>
						</tr>
						<tr>
							<%-- 業務フローパターン名称(英語) --%>
							<td style="border:0px;width:7%"></td>
							<td style="border:0px;width:24%"><%=i18n.get(GL.OS7105_NAME_EN)%></td>
							<td style="border:0px">
								<html:text property="workflow_nm_en" maxlength="200" style="width:280;" />
							</td>
						</tr>
					</table>
				</DIV>
				<DIV class="mainlist">
					<TABLE style="border:10px;">
						<TR>
							<td style="border:0px;width:7%"></td>
							<TD style="border:0px">
								<TABLE border=0 cellSpacing=0 cellPadding=0 style="width:500px;border:1px;border-right-style:inset;border-right-color:#AAA">
									<TR>
										<%-- [実質滞留債権判定] --%>
										<TD colspan="6"style="border:0px;"class="inputThColor1">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TAIRYU_TITLE)%></TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 滞留判定 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TAIRYU_HANTEI)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px;" class="">
											&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_tairyu_touroku" styleId="bl_tairyu_touroku" onclick="selReadFlg('bl_tairyu_touroku','tairyu_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_tairyu_touroku" value="false">
												<html:select property="tairyu_touroku" styleId="tairyu_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_tairyu_touroku" value="true">
												<html:select property="tairyu_touroku" styleId="tairyu_touroku" style="width:140" >
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_tairyu_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%" class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>
											&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_tairyu_shounin" styleId="bl_tairyu_shounin" onclick="selReadFlg('bl_tairyu_shounin','tairyu_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_tairyu_shounin" value="false">
												<html:select property="tairyu_shounin" styleId="tairyu_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_tairyu_shounin" value="true">
												<html:select property="tairyu_shounin" styleId="tairyu_shounin" style="width:140" >
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_tairyu_shounin" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>	
										<%-- 滞留判定検証 --%>			
										<TD rowspan="2"style="widht:230px;padding-top: 0px;">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TAIRYU_KENSHOU)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_tairyu_kenshou_touroku" styleId="bl_tairyu_kenshou_touroku" onclick="selReadFlg('bl_tairyu_kenshou_touroku','tairyu_kenshou_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_tairyu_kenshou_touroku" value="false">
												<html:select property="tairyu_kenshou_touroku" styleId="tairyu_kenshou_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_tairyu_kenshou_touroku" value="true">
												<html:select property="tairyu_kenshou_touroku" styleId="tairyu_kenshou_touroku" style="width:140" >
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_tairyu_kenshou_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 承認 --%>
										<TD>
											&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_tairyu_kenshou_shounin" styleId="bl_tairyu_kenshou_shounin" onclick="selReadFlg('bl_tairyu_kenshou_shounin','tairyu_kenshou_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_tairyu_kenshou_shounin" value="false">
												<html:select property="tairyu_kenshou_shounin" styleId="tairyu_kenshou_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_kenshou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_tairyu_kenshou_shounin" value="true">
												<html:select property="tairyu_kenshou_shounin" styleId="tairyu_kenshou_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_tairyu_kenshou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_tairyu_kenshou_shounin" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 対象先選定 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TAISHOU_SENTEI)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_taishou_touroku" styleId="bl_taishou_touroku" onclick="selReadFlg('bl_taishou_touroku','taishou_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_taishou_touroku" value="false">
												<html:select property="taishou_touroku" styleId="taishou_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_taishou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_taishou_touroku" value="true">
												<html:select property="taishou_touroku" styleId="taishou_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_taishou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_taishou_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>
											&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_taishou_shounin" styleId="bl_taishou_shounin" onclick="selReadFlg('bl_taishou_shounin','taishou_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_taishou_shounin" value="false">
												<html:select property="taishou_shounin" styleId="taishou_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_taishou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_taishou_shounin" value="true">
												<html:select property="taishou_shounin" styleId="taishou_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_taishou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_taishou_shounin" value="false"/>
										</TD>
									</TR>	
								</TABLE>
								<%-- 滞留判定への差戻は事務局を経由 --%>
								<TABLE class="none"border=0 cellSpacing=0 cellPadding=0 style="width:500px;">
									<TR style="border:0px;">
										<TD style="border:0px;" >
											<IMG src="<c:url value='/image/yajirushi3.gif' />">
										</TD>
										<TD style="border:0px;">
											<IMG src="<c:url value='/image/yajirushi2.gif' />">
										</TD>
										<TD style="border:0px;" >
										<%=i18n.get(GL.OS7105_TAISHOU_SA)%>&nbsp;
											<html:checkbox property="bl_tairyu_sa" styleId="bl_tairyu_sa" />
											<html:hidden property="bl_tairyu_sa" value="false"/>
										</TD>
									</TR>
								</TABLE>
								
								<TABLE border=0 cellSpacing=0 cellPadding=0 style="width:500px;border:1px;border-right-style:inset;border-right-color:#AAA">
									<TR>
										<%-- [査定] --%>
										<TD colspan="8"style="border:0px;"class="inputThColor1">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SATEI_TITLE)%></TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 一次査定 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_ITI_SATEI)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_iti_touroku" styleId="bl_iti_touroku" onclick="selReadFlg('bl_iti_touroku','iti_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_iti_touroku" value="false">
												<html:select property="iti_touroku" styleId="iti_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_iti_touroku" value="true">
												<html:select property="iti_touroku" styleId="iti_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_iti_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_iti_shounin" styleId="bl_iti_shounin" onclick="selReadFlg('bl_iti_shounin','iti_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_iti_shounin" value="false">
												<html:select property="iti_shounin" styleId="iti_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_iti_shounin" value="true">
												<html:select property="iti_shounin" styleId="iti_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_iti_shounin" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 一次査定検証 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_ITI_KENSHOU)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_iti_kenshou_touroku" styleId="bl_iti_kenshou_touroku" onclick="selReadFlg('bl_iti_kenshou_touroku','iti_kenshou_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_iti_kenshou_touroku" value="false">
												<html:select property="iti_kenshou_touroku" styleId="iti_kenshou_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_iti_kenshou_touroku" value="true">
												<html:select property="iti_kenshou_touroku" styleId="iti_kenshou_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_iti_kenshou_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_iti_kenshou_shounin" styleId="bl_iti_kenshou_shounin" onclick="selReadFlg('bl_iti_kenshou_shounin','iti_kenshou_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_iti_kenshou_shounin" value="false">
												<html:select property="iti_kenshou_shounin" styleId="iti_kenshou_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_kenshou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_iti_kenshou_shounin" value="true">
												<html:select property="iti_kenshou_shounin" styleId="iti_kenshou_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_iti_kenshou_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_iti_kenshou_shounin" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 二次査定 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_NI_SATEI)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_ni_touroku" styleId="bl_ni_touroku" onclick="selReadFlg('bl_ni_touroku','ni_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_ni_touroku" value="false">
												<html:select property="ni_touroku" styleId="ni_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_ni_touroku" value="true">
												<html:select property="ni_touroku" styleId="ni_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_ni_touroku" value="false"/>
										</TD>
									</TR>
									<TR>
									<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_ni_shounin" styleId="bl_ni_shounin" onclick="selReadFlg('bl_ni_shounin','ni_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_ni_shounin" value="false">
												<html:select property="ni_shounin" styleId="ni_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_ni_shounin" value="true">
												<html:select property="ni_shounin" styleId="ni_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_ni_shounin" value="false"/>
										</TD>
									</TR>
								</TABLE>
								
								<TABLE class="none"border=0 cellSpacing=0 cellPadding=0 style="width:500px">
									<TR >
										<TD >
											<IMG src="<c:url value='/image/yajirushi3.gif' />">
										</TD>
										<TD ><IMG src="<c:url value='/image/yajirushi2.gif' />">
										</TD>
											<%-- 査定完了後の差戻を行う --%>
											<TD ><%=i18n.get(GL.OS7105_SATEI_SA)%>&nbsp;
											<html:checkbox property="bl_satei" styleId="bl_satei" />
											<html:hidden property="bl_satei" value="false"/>
											<br><br>
											<%-- 二次査定区分 --%>
											<%=i18n.get(GL.OS7105_NI_SATEI_KBN)%>
											<nested:equal property="ni_satei_flg" value="true">
												<html:select property="ni_satei" styleId="ni_satei" style="width:120">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_satei" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="ni_satei_flg" value="false">
												<html:select property="ni_satei" styleId="ni_satei" style="width:120" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_ni_satei" value="value" label="key" />
												</html:select>
											</nested:equal>

										</TD>
									</TR>
								</TABLE>
								
								<TABLE border=0 cellSpacing=0 cellPadding=0 style="width:500px;border:1px;border-right-style:inset;border-right-color:#AAA">
									<TR>
										<%-- [引当金検証/確認] --%>
										<TD colspan="8"style="border:0px;"class="inputThColor1">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_HIKIATE_TITLE)%></TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 引当金検証 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_HIKIATE_KENSHOU)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_hikiate_kenshou_touroku" styleId="bl_hikiate_kenshou_touroku" onclick="selReadFlg('bl_hikiate_kenshou_touroku','hikiate_kenshou_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_hikiate_kenshou_touroku" value="false">
												<html:select property="hikiate_kenshou_touroku" styleId="hikiate_kenshou_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_hikiate_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_hikiate_kenshou_touroku" value="true">
												<html:select property="hikiate_kenshou_touroku" styleId="hikiate_kenshou_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_hikiate_kenshou_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_hikiate_kenshou_touroku" value="false"/>
										</TD>
									
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_hikiate_kenshou_shounin" styleId="bl_hikiate_kenshou_shounin" />
											<html:hidden property="bl_hikiate_kenshou_shounin" value="false"/>
										</TD>
							
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 引当金確認 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_HIKIATE_KAKUNIN)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_hikiate_kakunin_touroku" styleId="bl_hikiate_kakunin_touroku" onclick="selReadFlg('bl_hikiate_kakunin_touroku','hikiate_kakunin_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_hikiate_kakunin_touroku" value="false">
												<html:select property="hikiate_kakunin_touroku" styleId="hikiate_kakunin_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_hikiate_kakunin_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_hikiate_kakunin_touroku" value="true">
												<html:select property="hikiate_kakunin_touroku" styleId="hikiate_kakunin_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_hikiate_kakunin_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_hikiate_kakunin_touroku" value="false"/>
										</TD>
									</TR>
				
									<TR>
									<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_hikiate_kakunin_shounin" styleId="bl_hikiate_kakunin_shounin" />
											<html:hidden property="bl_hikiate_kakunin_shounin" value="false"/>
										</TD>
									</TR>
								</TABLE>
								<br><br>
								
								<TABLE border=0 cellSpacing=0 cellPadding=0 style="width:500px;border:1px;border-right-style:inset;border-right-color:#AAA">
									<TR>
										<%-- [システム管理] --%>
										<TD colspan="8"style="border:0px;"class="inputThColor1">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SYSTEM_TITLE)%></TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- クレーム債権 --%>
										<TD rowspan="2"style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_KUREMU)%></TD>
										<%-- 登録 --%>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_TOUROKU)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_kure_touroku" styleId="bl_kure_touroku" onclick="selReadFlg('bl_kure_touroku','kure_touroku')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_kure_touroku" value="false">
												<html:select property="kure_touroku" styleId="kure_touroku" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_kure_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_kure_touroku" value="true">
												<html:select property="kure_touroku" styleId="kure_touroku" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_kure_touroku" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_kure_touroku" value="false"/>
										</TD>
									</TR>	
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1"></TD>
										<%-- 承認 --%>
										<TD>&nbsp;&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SHOUNIN)%>
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="bl_kure_shounin" styleId="bl_kure_shounin" onclick="selReadFlg('bl_kure_shounin','kure_shounin')"/>
											&nbsp;&nbsp;&nbsp;&nbsp;⇒&nbsp;&nbsp;&nbsp;&nbsp;
											<nested:equal property="bl_kure_shounin" value="false">
												<html:select property="kure_shounin" styleId="kure_shounin" style="width:140" disabled="true">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_kure_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<nested:equal property="bl_kure_shounin" value="true">
												<html:select property="kure_shounin" styleId="kure_shounin" style="width:140">
													<html:option value=""></html:option>
													<html:optionsCollection name="WorkFlowTorokuForm" property="ar_kure_shounin" value="value" label="key" />
												</html:select>
											</nested:equal>
											<html:hidden property="bl_kure_shounin" value="false"/>
										</TD>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 代行設定 --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_DAIKOU_SETTEI)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="daikou_settei" styleId="daikou_settei"/>
											<html:hidden property="daikou_settei" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 査定会社メンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_SATEI_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="settei_mentenansu" styleId="settei_mentenansu"/>
											<html:hidden property="settei_mentenansu" value="false"/>
									</TR>
					
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 業務フローパターンメンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_GYOUMU_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="gyoumu_mentenansu" styleId="gyoumu_mentenansu"/>
											<html:hidden property="gyoumu_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- ユーザマスタメンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_USER_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="user_mentenansu" styleId="user_mentenansu"/>
											<html:hidden property="user_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 勘定科目マスタメンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_KANJOU_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="kanjou_mentenansu" styleId="kanjou_mentenansu"/>
											<html:hidden property="kanjou_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 抽出条件メンテナンス(本社) --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_CHUUSHUTU_HONSHA)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="honsha_mentenansu" styleId="honsha_mentenansu"/>
											<html:hidden property="honsha_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 抽出条件メンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_CHUUSHUTU_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="jouken_mentenansu" styleId="jouken_mentenansu"/>
											<html:hidden property="jouken_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- チャンピオン部メンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">
											&nbsp;&nbsp;<%=i18n.get(GL.OS7105_CHANPION_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="chanpion_mentenansu" styleId="chanpion_mentenansu"/>
											<html:hidden property="chanpion_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- ゴルフ会員権メンテナンス --%>
										<TD style="widht:230px;padding-top: 0px;"class="">&nbsp;&nbsp;<%=i18n.get(GL.OS7105_MEMBER_MENTENANSU)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="kaiin_mentenansu" styleId="kaiin_mentenansu"/>
											<html:hidden property="kaiin_mentenansu" value="false"/>
									</TR>
									<TR>
										<TD style="border:0px;width:2%"class="inputThColor1">&nbsp;</TD>
										<%-- 連結区分マスタUPLOAD --%>
										<TD style="widht:230px;padding-top: 0px;"class="">
											&nbsp;&nbsp;<%=i18n.get(GL.OS7105_RENKETU_UPLOAD)%></TD>
										<TD colspan="4"style="width:280px"class="">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
											<html:checkbox property="renketu_upload" styleId="renketu_upload"/>
											<html:hidden property="renketu_upload" value="false"/>
										</TD>
									</TR>
								</TABLE>
								<TABLE class="none"border=0 cellSpacing=0 cellPadding=0 style="width:500px">
									<TR >
										<%-- システム管理者専用 --%>
										<TD >
											<%=i18n.get(GL.OS7105_ADMIN_ONLY)%>&nbsp;
											<html:checkbox property="system_manager" styleId="system_manager"/>
											<html:hidden property="system_manager" value="false"/>
										</TD>
									</TR>
								</TABLE>
							</TD>
						</TR>
					</TABLE>
				</DIV>
			</html:form>
		</DIV>	
	</DIV>
</DIV>
</CENTER>
</BODY>

</HEAD>
</HTML>