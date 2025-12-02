<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="SateikaisyaTorokuForm" name="03SateikaisyaTorokuForm" type="app.system.form.SateikaisyaTorokuForm" />
<bean:define id="SateiKaisyaBean" name="app.SessionData" property="sateikaisya_bean" type="app.SateiKaisyaBean" />

</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニュー部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7103)%></H1>
		<html:form action="/system/sateikaisyatoroku">

		<DIV id="submenu">
			<nested:equal property="seniMode" value="1">
				<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('regist')" />
			</nested:equal>
			<nested:equal property="seniMode" value="2">
				<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('update')" />
				<input type="button" value="<%=i18n.get(GL.BTN_DELETE)%>" onclick="doSubmit('delete')" />
			</nested:equal>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')" />
		</DIV>

		<DIV id="list">
			<br><br><br><br><br>
			<DIV class="mainlist">
				<table style="border:0px;width:100%;">
					<%-- システム --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=i18n.get(GL.OS7103_SYSTEM)%></td>
						<nested:equal property="seniMode" value="1">
							<td style="border:0px">
								<nested:select property="systemKbn" onchange="doSubmit('systemKbn')" style="width:150">
									<nested:optionsCollection property="ar_systemKbn" value="value" label="key"/>
								</nested:select>
							</td>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<td style="border:0pxwidth:150">
								<DIV class="ReadOnlybox"style="width:150;">
									<bean:write name="SateiKaisyaBean" property="system_kbn_nm"/>
								</DIV>
							</td>
						</nested:equal>
					</tr>
					<%-- 汎用1 --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=SESSION_DATA_APP.getLbl_nm1()%></td>
						<nested:equal property="seniMode" value="1">
							<td style="border:0px">
								<nested:select property="hanyo1" style="width:150">
									<html:option value=""></html:option>
									<nested:optionsCollection property="ar_hanyo1" value="value" label="key"/>
								</nested:select>
							</td>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<td style="border:0pxwidth:150">
								<DIV class="ReadOnlybox"style="width:150;">
									<nested:write property="hanyo1"/>
								</DIV>
							</td>
						</nested:equal>
					</tr>
					<%-- 汎用2 --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=SESSION_DATA_APP.getLbl_nm3()%></td>
						<td style="border:0px">
						<nested:equal property="seniMode" value="1">
							<nested:text property="hanyo2" maxlength="3" size="24" style="width:150"></nested:text>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox"style="width:150;">
								<nested:write property="hanyo2"/>
							</DIV>
						</nested:equal>
						</td>
					</tr>
					<%-- 汎用2名称（日本語） --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=SESSION_DATA_APP.getLbl_nm3()%><%=i18n.get(GL.OS7103_MEISYO)%><%=i18n.get(GL.OS7103_NIHONGO)%></td>
						<td style="border:0px">
							<nested:text property="hanyo2Jp" maxlength="80" size="46" style="width:280;" styleClass="doubleByte" />
						</td>
					</tr>
					<%-- 汎用2名称（英語） --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=SESSION_DATA_APP.getLbl_nm3()%><%=i18n.get(GL.OS7103_MEISYO)%><%=i18n.get(GL.OS7103_EIGO)%></td>
						<td style="border:0px">
							<nested:text property="hanyo2En" maxlength="80" size="46" style="width:280;" />
						</td>
					</tr>
					<%-- 標準時刻 --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=i18n.get(GL.OS7103_HYOJUNJIKOKU)%></td>
						<td style="border:0px">
							<nested:select property="hyojunJikokuCd" style="width:150">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_hyojunJikoku" value="value" label="key"/>
							</nested:select>
						</td>
					</tr>
					<%-- 抽出対象 --%>
					<tr style="border:0px">
						<td style="border:0px;width:7%"></td>
						<td style="border:0px;width:20%"><%=i18n.get(GL.OS7103_CHUSYUTUTAISYO)%></td>
						<td style="border:0px">
							<nested:checkbox property="tyusyutu_taisyo_flg" value="<%= GS.ON %>"></nested:checkbox>
						</td>
					</tr>
				</table>
			</DIV>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
