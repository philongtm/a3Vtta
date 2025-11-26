<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="KanjyoForm" name="08KanjyoForm" type="app.system.form.KanjyoForm" />
<% Pager pager = KanjyoForm.getPager(); %>
<% 
String focus = GS.EMPTY_CHARCTER;
if (request.getAttribute(GS.FOCUS_FIELD) == null || GS.EMPTY_CHARCTER.equals(request.getAttribute(GS.FOCUS_FIELD))) {
	focus = "";
} else {
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
 %>
<script language="JavaScript">
		
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
			form.elements[val].focus();
		}
	}	
</script>
</HEAD>
<BODY onload="setFocus('<%=focus%>')">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="../image/navi001.gif" width="89" height="52">
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

		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7108)%></H1>
	
		<DIV id="submenu">			
			<input type="button" value="<%=i18n.get(GL.OS7108_BTN_SINKI_SAKUSEI)%>" onclick="doSubmit('sinki')">
			<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>"onclick="doSubmitNon('download')">  
			<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('update')">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">

		</DIV>

		<DIV id="list">
			<html:form action="/system/kanjyo" >
			
				<DIV class="headerlist">
					<TABLE style="border=10px;width:100%;">
						<TR style="width:100%;">
						
						<%--  システムセレクトボックス --%>
						<TD style="width:14%;"><%=i18n.get(GL.OS7108_SYSTEM)%></TD>
						<TD style="width:9%;" class="left" ><html:select property="system_kbn" onchange="doSubmit('system')"  style="width:100">
								<html:optionsCollection name="KanjyoForm" property="ar_system" value="value" label="key" /></html:select>
						</TD>
						
						<%--  汎用１セレクトボックス --%>
						<TD style="width:14%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TD>
						<TD style="width:9%;" class="left" ><html:select property="hanyo1" onchange="doSubmit('hanyo1')"   style="width:100">
								<html:optionsCollection name="KanjyoForm" property="ar_hanyo1" value="value" label="key" /></html:select>
						</TD>
						<%--  汎用２セレクトボックス --%>
						<TD style="width:14%;"><%=SESSION_DATA_APP.getLbl_nm6()%></TD>
						<TD style="width:11%;text-align:left;"><html:select property="hanyo2" style="width:180">
								<html:optionsCollection name="KanjyoForm" property="ar_hanyo2" value="value" label="key" /></html:select>
						</TD>	
						<TD></TD>					
						</TR>
					</TABLE>

					<TABLE style="width:100%;">
						<TR>
							<%--  勘定科目コード --%>
							<TD style="width:13%;"><%=i18n.get(GL.OS7108_KANJO_CD)%></TD>
							<TD style="width:27%;" class="left" >
								<html:text property="kanjo_cd" style="width:100"/><%=i18n.get(GL.OS7108_FIRST_LIKE)%>
							</TD>
							<%--  勘定科目名称 --%>		
							<TD style="width:13%;">&nbsp;<%=i18n.get(GL.OS7108_KANJO_NM)%></TD>
							<TD style="width:47%;text-align:left;">
								<html:text property="kanjo_nm" style="width:240;ime-mode: active;"/><%=i18n.get(GL.OS7108_ALL_LIKE)%>
							</TD>

						</TR>
						<TR>			
							<%--  内分類コード --%>		
							<TD style="width:13%;"><%=i18n.get(GL.OS7108_KANJO_UCHI_CD)%></TD>
							<TD style="width:10%;" class="left" >
								<html:text property="kanjo_uchi_cd" style="width:100"/><%=i18n.get(GL.OS7108_FIRST_LIKE)%>
							</TD>
							<%--  債権フラグ --%>	
							<TD style="width:14%;">&nbsp;<%=i18n.get(GL.OS7108_SAIKEN_FLG)%></TD>
							<TD style="width:11%;" class="left" ><html:select property="saiken_flg" >
								<html:optionsCollection name="KanjyoForm" property="ar_saiken_flg" value="value" label="key" /></html:select>
							</TD>
							<%-- 課題No.218 ボタンのフォーマットを統一--%>
							<%-- 追加開始 --%>
						</TR>
					</TABLE>
							<DIV id="submenu"class="semaku">
								<%-- 検索ボタン --%>
								<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')" style="background:#CCCCCC;align:center">
							</DIV>
							<%-- 追加完了 --%>
					
					<TABLE style="width:100%;">
				
						<TR>
							<%--  表示件数セレクトボックス --%>
							<TD colspan="2" style="border:0px;" class="semaku"><br></TD>
							<TD style="width:10%;" class="right" class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR></TD>
							<TD style="width:10%;"class="semaku"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="KanjyoForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>
							<TD colspan="4" class="semaku"></TD>
							<TD style="width:10%;">
								<logic:notEqual name="KanjyoForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="KanjyoForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="KanjyoForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<TD style="width:10%;">
								<logic:notEqual name="KanjyoForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="KanjyoForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="KanjyoForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<TD style="width:15%;" class="right" class="semaku">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
					</TABLE>
				</DIV>
				<%-- 一覧情報 --%>
				<DIV class="mainlist">	
					<TABLE>					
						<THEAD>
							<TR>
								<%-- システム --%>
								<TH width="9%"><%=i18n.get(GL.OS7108_SYSTEM)%></TH>
								<%-- 汎用１ --%>
								<TH colspan="3" width="15%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<%-- 汎用２ --%>
								<TH colspan="3" width="15%"><%=SESSION_DATA_APP.getLbl_nm6()%></TH>
								<%-- 勘定科目 --%>
								<TH  colspan="6" width="30%"><%=i18n.get(GL.OS7108_MEISAI_KANJO_CD)%></TH>								
								<%-- 満期日優先 --%>
								<TH width="7%" rowspan="2" style="text-align:center;border-right-color:#000000;"><%=i18n.get(GL.OS7108_MEISAI_MANKIBI)%></TH>
							</TR>					
							<TR>
								<%-- 内分類 --%>
								<TH colspan="3" width="30%"><%=i18n.get(GL.OS7108_MEISAI_KANJO_UCHI_CD)%></TH>
								<%-- DR/CR区分 --%>
								<TH colspan="2" width="10%"><%=i18n.get(GL.OS7108_MEISAI_DRCR_KBN)%></TH>
								<%-- 債権フラグ --%>
								<TH colspan="4" width="20%"><%=i18n.get(GL.OS7108_SAIKEN_FLG)%></TH>
								<%-- 表示区分 --%>
								<TH colspan="4" width="18%"><%=i18n.get(GL.OS7108_MEISAI_HYOJIKUBUN)%></TH>
							</TR>	
						</THEAD>
						<TBODY>
							<% if(KanjyoForm.getList() != null) { %>
								<nested:iterate name="KanjyoForm" property="list" indexId="idx">
									<TR>
										<TD width="9%"><nested:write property="system_kbn_nm" /></TD>
										<TD colspan="3" width="15%"><nested:write property="hanyo1" /></TD>
										<TD colspan="3" width="15%"><nested:write property="hanyo2" /></TD>
										<TD colspan="6" width="30%"><nested:write property="kanjo" /></TD>
										<%-- システム区分が'01':GSSの場合、「満期日優先」が使用可能 --%>
										<logic:equal name="KanjyoForm" property="system_kbn" value="<%=GS.GSS%>">
											<TD rowspan="2" width="7%" style="text-align:center;" class="borderBottom borderRight"><nested:checkbox property="mankibi_flg" disabled="false"/></TD>
										</logic:equal>
										<%-- システム区分が'02'：MTS、'03'：FOCUSの場合、「満期日優先」が使用不能 --%>
										<logic:notEqual name="KanjyoForm" property="system_kbn" value="<%=GS.GSS%>">
											<TD rowspan="2" width="7%" style="text-align:center;" class="borderBottom borderRight"><nested:checkbox property="mankibi_flg" disabled="true" /></TD>
										</logic:notEqual>							
									</TR>
									<TR>
										<TD colspan="3" width="30%"style="border-bottom-color:#000000;"><nested:write property="kanjo_uchi" /></TD>
										<TD colspan="2" width="10%"style="border-bottom-color:#000000;">
										<%-- DR/CR区分セレクトボックス --%>
										<%-- システム区分が'01':GSSの場合、「DR/CR区分」が使用不能 --%>
										<logic:equal name="KanjyoForm" property="system_kbn" value="<%=GS.GSS%>">
											<select style="width:100%" name="list[<bean:write name='idx'/>].meisai_drcr_kbn" value="<nested:write property='meisai_drcr_kbn'/>" disabled>
										</logic:equal>
										<%-- システム区分が'02'：MTS、'03'：FOCUSの場合、「DR/CR区分」が使用可能  --%>
										<logic:notEqual name="KanjyoForm" property="system_kbn" value="<%=GS.GSS%>">
											<select style="width:100%" name="list[<bean:write name='idx'/>].meisai_drcr_kbn" value="<nested:write property='meisai_drcr_kbn'/>" >
										</logic:notEqual>		
												<logic:iterate id="drcr_kbn" name="KanjyoForm" property="ar_drcr_kbn" indexId="idx1">
													<bean:define id="drcr_KbnVal" name="drcr_kbn" property="value" type="java.lang.String" />
													<logic:equal name="list" property="meisai_drcr_kbn" value="<%=drcr_KbnVal%>">
														<option value ="<bean:write name='drcr_kbn' property='value'/>" selected="selected">
															<bean:write name="drcr_kbn" property="key"/>
														</option>
													</logic:equal>
													<logic:notEqual name="list" property="meisai_drcr_kbn" value="<%=drcr_KbnVal%>">
														<option value ="<bean:write name='drcr_kbn' property='value'/>">
															<bean:write name="drcr_kbn" property="key"/>
														</option>
													</logic:notEqual>
												</logic:iterate>
											</select>
										</TD>
										
										<%-- 債権フラグセレクトボックス --%>
										<TD colspan="4" width="20%"style="border-bottom-color:#000000;">
										<select style="width:100%" name="list[<bean:write name='idx'/>].meisai_saiken_flg" value="<nested:write property='meisai_saiken_flg'/>" >
											<logic:iterate id="saiken_flg" name="KanjyoForm" property="ar_meisai_saiken_flg" indexId="idx2">
												<bean:define id="saikenFlgVal" name="saiken_flg" property="value" type="java.lang.String" />
												<logic:equal name="list" property="meisai_saiken_flg" value="<%=saikenFlgVal%>">
													<option value ="<bean:write name='saiken_flg' property='value'/>" selected="selected">
														<bean:write name="saiken_flg" property="key"/>
													</option>
												</logic:equal>
												<logic:notEqual name="list" property="meisai_saiken_flg" value="<%=saikenFlgVal%>">
													<option value ="<bean:write name='saiken_flg' property='value'/>">
														<bean:write name="saiken_flg" property="key"/>
													</option>
												</logic:notEqual>
											</logic:iterate>
										</select>
										
										</TD>
										<%-- 表示区分セレクトボックス --%>
										<TD colspan="4" width="28%"style="border-bottom-color:#000000;">
										<select style="width:100%" name="list[<bean:write name='idx'/>].meisai_hyouji_kbn" value="<nested:write property='meisai_hyouji_kbn'/>">
											<logic:iterate id="hyoji_kbn" name="KanjyoForm" property="ar_hyoji_kbn" indexId="idx3">
												<bean:define id="hyoji_KbnVal" name="hyoji_kbn" property="value" type="java.lang.String" />
												<logic:equal name="list" property="meisai_hyouji_kbn" value="<%=hyoji_KbnVal%>">
													<option value ="<bean:write name='hyoji_kbn' property='value'/>" selected="selected">
														<bean:write name="hyoji_kbn" property="key"/>
													</option>
												</logic:equal>
												<logic:notEqual name="list" property="meisai_hyouji_kbn" value="<%=hyoji_KbnVal%>">
													<option value ="<bean:write name='hyoji_kbn' property='value'/>">
														<bean:write name="hyoji_kbn" property="key"/>
													</option>
												</logic:notEqual>
											</logic:iterate>
										</select>
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
	
</DIV>
</CENTER>
</BODY>
</HTML>

