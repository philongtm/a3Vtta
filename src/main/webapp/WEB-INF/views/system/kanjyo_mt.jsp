<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>

<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>
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
<bean:define id="KanjyoTorokuForm" name="09KanjyoTorokuForm" type="app.system.form.KanjyoTorokuForm" />
</HEAD>
<BODY onload="setFocus('<%=focus%>')">
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

		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7109)%></H1>
	
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('insert')">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		</DIV>

		<DIV id="list">
			<html:form action="/system/kanjyotoroku" >
			
			<br/><br/><br/><br/><br/>
				<DIV class="headerlist">	
					<TABLE>
						<%--  システムセレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_SYSTEM)%></td>
							<td><html:select property="system_kbn" onchange="doSubmit('system')"  style="width:150">
								<html:optionsCollection name="KanjyoTorokuForm" property="ar_system" value="value" label="key" /></html:select>
							</td>
						</tr>
						<%--  汎用１セレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=SESSION_DATA_APP.getLbl_nm1()%></td>
							<td>
								<html:select property="hanyo1" onchange="doSubmit('hanyo1')"   style="width:150">
									<html:optionsCollection name="KanjyoTorokuForm" property="ar_hanyo1" value="value" label="key" />
								</html:select>
							</td>
						</tr>
						<%--  汎用２セレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=SESSION_DATA_APP.getLbl_nm6()%></td>
							<td>
								<html:select property="hanyo2" style="width:280">
									<html:optionsCollection name="KanjyoTorokuForm" property="ar_hanyo2" value="value" label="key" />
								</html:select>
							</td>
						</tr>
						<%--  勘定科目コード --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_CD)%></td>
							<td>
								<html:text property="kanjo_cd" style="width:150" maxlength="10"/>
							</td>
						</tr>
						<%--  勘定科目名称 --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_NM)%></td>
							<td>
								<html:text property="kanjo_nm" style="width:280;ime-mode: active;" maxlength="60"/>
							</td>
						</tr>
						<logic:equal name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
							<%--  内分類コード --%>
							<tr>
								<td style="width:7%"></td>
								<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_UCHI_CD)%></td>
								<td>
									<html:text property="kanjo_uchi_cd" style="width:150" disabled="false" maxlength="7"/>
								</td>
							</tr>
							<%--  内分類名称 --%>
							<tr>
								<td style="width:7%"></td>
								<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_UCHI_NM)%></td>
								<td>
									<html:text property="kanjo_uchi_nm" disabled="false" style="width:280;ime-mode: active;" maxlength="50"/>
								</td>
							</tr>
						</logic:equal>
						<logic:notEqual name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
							<%--  内分類コード --%>
							<tr>
								<td style="width:7%"></td>
								<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_UCHI_CD)%></td>
								<td>
									<html:text property="kanjo_uchi_cd" style="width:150" disabled="true"/>
								</td>
							</tr>
							<%--  内分類名称 --%>
							<tr>
								<td style="width:7%"></td>
								<td style="width:20%"><%=i18n.get(GL.OS7109_KANJO_UCHI_NM)%></td>
								<td>
									<html:text property="kanjo_uchi_nm" style="width:280" disabled="true"/>
								</td>
							</tr>
						</logic:notEqual>
						
						<%--  DR/CR区分セレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_DRCR_KBN)%></td>
							<td>
								<%-- システム区分が'01':GSSの場合、「満期日優先」が使用可能 --%>
								<logic:equal name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
									<html:select property="drcr_kbn" style="width:80" disabled="true">
										<html:optionsCollection name="KanjyoTorokuForm" property="ar_drcr_kbn" value="value" label="key" />
									</html:select>
								</logic:equal>
								<%-- システム区分が'02'：MTS、'03'：FOCUSの場合、「満期日優先」が使用不能 --%>
								<logic:notEqual name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
									<html:select property="drcr_kbn" style="width:80" disabled="false">
										<html:optionsCollection name="KanjyoTorokuForm" property="ar_drcr_kbn" value="value" label="key" />
									</html:select>
								</logic:notEqual>
							</td>
						</tr>						
						<%--  債権フラグセレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_SAIKEN_FLG)%></td>
							<td>
								<html:select property="saiken_flg" style="width:280">
									<html:optionsCollection name="KanjyoTorokuForm" property="ar_saiken_flg" value="value" label="key" />
								</html:select>
							</td>
						</tr>
						<%--  表示区分セレクトボックス --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_HYOJIKUBUN)%></td>
							<td>
								<html:select property="hyoji_kbn" style="width:280">
									<html:optionsCollection name="KanjyoTorokuForm" property="ar_hyoji_kbn" value="value" label="key" />
								</html:select>
							</td>
						</tr>
						<%-- 満期日優先 --%>
						<tr>
							<td style="width:7%"></td>
							<td style="width:20%"><%=i18n.get(GL.OS7109_MANKIBI)%></td>
								<%-- システム区分が'01':GSSの場合、「満期日優先」が使用可能 --%>
								<logic:equal name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
									<TD><nested:checkbox property="mankibi_flg" disabled="false"/></TD>
								</logic:equal>
								<%-- システム区分が'02'：MTS、'03'：FOCUSの場合、「満期日優先」が使用不能 --%>
								<logic:notEqual name="KanjyoTorokuForm" property="system_kbn" value="<%=GS.GSS%>">
									<TD><nested:checkbox property="mankibi_flg" disabled="true" /></TD>
								</logic:notEqual>	
							</td>
						</tr>
					</TABLE>
				</DIV>
			</html:form>
	</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>
