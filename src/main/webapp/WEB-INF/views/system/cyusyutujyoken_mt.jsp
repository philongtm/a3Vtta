<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="CyusyutujyokenTorokuForm" name="11CyusyutujyokenTorokuForm" type="app.system.form.CyusyutujyokenTorokuForm" />
<bean:define id="TyusyutuJokenBean" name="app.SessionData" property="joken_bean" type="app.TyusyutuJokenBean" />

</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニュー部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7111)%></H1>
		<html:form action="/system/cyusyutujyokenToroku">

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
			<DIV class="mainlist">
				<TABLE style="border: 0px;width:100%">
					<TR style="border: 0px">
						<%-- 抽出事由 --%>
						<TD style="border: 0px;width:15%;"><%=i18n.get(GL.OS7111_CHUSYUTUJIYU)%></TD>
						<TD style="border: 0px">
						<nested:equal property="seniMode" value="1">
							<nested:text property="cyusyutuJiyu" maxlength="2" style="width:100"></nested:text>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox"style="width:20%;">
								<nested:write property="cyusyutuJiyu"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR style="border: 0px">
						<%-- 条件名称(日本語) --%>
						<TD style="border: 0px;width:15%;"><%=i18n.get(GL.OS7111_JYOKENNIHONNGO)%></TD>
						<TD style="border: 0px">
							<nested:text property="jyokenNmJp" maxlength="40" style="width:300;ime-mode: active;"></nested:text>
						</TD>
					</TR>
					<TR style="border: 0px">
						<%-- 条件名称(英語) --%>
						<TD style="border: 0px;width:15%;"><%=i18n.get(GL.OS7111_JYOKENEIGO)%></TD>
						<TD style="border: 0px">
							<nested:text property="jyokenNmEn" maxlength="60" style="width:300;ime-mode: disabled;"></nested:text>
						</TD>
					</TR>
				</TABLE>
		
				<TABLE style="width:100%;border-collapse: collapse;border-left-color: #AAA;">
					<TR>
						<%-- システム --%>
						<TD colspan=2 style="width:15%;"><%=i18n.get(GL.OS7111_SYSTEM)%></TD>
						<TD>
						<nested:equal property="seniMode" value="1">
							<nested:select property="systemKbn" onchange="doSubmit('systemKbn')" style="width:70">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_systemKbn" value="value" label="key"/>
							</nested:select>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox" style="width:20%;">
								<nested:write property="systemKbn"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR>
						<%-- 汎用１ --%>
						<TD colspan=2 style="width:15%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TD>
						<TD>
						<nested:equal property="seniMode" value="1">
							<nested:select property="hanyo1" onchange="doSubmit('hanyo1')" style="width:100">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_hanyo1" value="value" label="key"/>
							</nested:select>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox" style="width:20%;">
								<nested:write property="hanyo1"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR>
						<%-- 汎用２ --%>
						<TD colspan=2 style="width:15%;"><%=SESSION_DATA_APP.getLbl_nm6()%></TD>
						<TD>
						<nested:equal property="seniMode" value="1">
							<nested:select property="hanyo2" style="width:200px">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_hanyo2" value="value" label="key"/>
							</nested:select>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox" style="width:40%;">
								<nested:write property="hanyo2"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR>
						<%-- 決算期区分 --%>
						<TD colspan=2 style="width:15%;"><%=i18n.get(GL.OS7111_KESANKIKBN)%></TD>
						<TD>
						<nested:equal property="seniMode" value="1">
							<nested:select property="kesanKbn" style="width:100px">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kesanKbn" value="value" label="key"/>
							</nested:select>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox" style="width:20%;">
								<bean:write name="TyusyutuJokenBean" property="kessanki_kbn_nm"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR>
						<%-- 基準日 --%>
						<TD colspan=2 style="width:15%;"><%=i18n.get(GL.OS7111_KIJYUNBI)%></TD>
						<TD>
						<nested:equal property="seniMode" value="1">
							<nested:select property="kijunbi" style="width:80px">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kijunbi" value="value" label="key"/>
							</nested:select>
						</nested:equal>
						<nested:equal property="seniMode" value="2">
							<DIV class="ReadOnlybox" style="width:20%;">
								<bean:write name="TyusyutuJokenBean" property="kijunbi_nm"/>
							</DIV>
						</nested:equal>
						</TD>
					</TR>
					<TR>
						<%-- [基本抽出条件] --%>
						<TD rowspan=1 colspan=3 style="border-bottom-color: #330088;" class="inputThColor"><%=i18n.get(GL.OS7111_KIHONJYOKEN)%></TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<%-- 格付 --%>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KAKUDUKE)%></TD>
						<TD>
							<nested:select property="kakuduke" style="10%">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kakuduke" value="value" label="key"/>
							</nested:select>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<%-- 金額基準1 --%>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KINGAKUKIJYUN1)%></TD>
						<TD>
							<nested:text property="kingakuJyoken1" maxlength="19" size="20" style="text-align:right;padding-right:1px;"></nested:text>
							<nested:write property="tukaCd"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<%-- 金額基準2(滞留区分) --%>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KINGAKUKIJYUN2KBN)%></TD>
						<TD>
							<nested:select property="kingakuJyoken2Kbn" style="width:150">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kingakuJyoken2Kbn" value="value" label="key"/>
							</nested:select>
							<%=i18n.get(GL.OS7111_IJYO)%>&nbsp;
							<nested:text property="kingakuJyoken2" maxlength="19" size="20" style="text-align:right;padding-right:1px;"></nested:text>
							<nested:write property="tukaCd"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<%-- 滞留期間 --%>
						<TD style="word-break:break-all;width:15%;"><%=i18n.get(GL.OS7111_TAIRYUKIKAN)%></TD>
						<TD>
							<nested:text property="tairyuKikanFrom" maxlength="3" size="2" style="text-align:right;padding-right:1px;"></nested:text>
							<%=i18n.get(GL.OS7111_KAGETU)%>&nbsp;<%=i18n.get(GL.OS7111_NAMIGATA)%>
							<nested:text property="tairyuKikanTo" maxlength="3" size="2" style="text-align:right;padding-right:1px;"></nested:text>
							<%=i18n.get(GL.OS7111_KAGETU)%></TD>
					</TR>
					<TR style="border:0px;">
						<%-- 実質滞留判定対象 --%>
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_TAIRYUTAISYO)%></TD>
						<TD class="semaku">
							<nested:checkbox property="tairyuTaisyo" value="<%= GS.ON %>"></nested:checkbox>
						</TD>
					</TR>
					<TR style="border:0px;">
						<%-- 査定対象 --%>
						<TD style="width: 2%;" class="inputThColor"></TD>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_SATEITAISYO)%></TD>
						<TD class="semaku">
							<nested:checkbox property="sateiTaisyo" value="<%= GS.ON %>"></nested:checkbox>
						</TD>
					</TR>
					<TR>
						<%-- [過去格付条件] --%>
						<TD rowspan=1 colspan=3 style="border-bottom-color: #330088;" class="inputThColor">
							<%=i18n.get(GL.OS7111_KAKOJYOKEN)%>
						</TD>
					</TR>
					<TR>
						<%-- 過去格付フラグ --%>
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KAKOFLAG)%></TD>
						<TD class="semaku">
							<nested:checkbox property="kakoKakudukeFlg" value="<%= GS.ON %>"></nested:checkbox>
						</TD>
					</TR>
					<TR>
						<%-- 過去格付 --%>
						<TD style="width: 2%;border-bottom-color: #330088;" class="inputThColor"></TD>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KAKOKAKUDUKE)%></TD>
						<TD class="semaku">
							<nested:select property="kakoKakudukeFrom" style="width:150">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kakoKakudukeFrom" value="value" label="key"/>
							</nested:select>
							&nbsp;&nbsp;<%=i18n.get(GL.OS7111_NAMIGATA)%>&nbsp;&nbsp;
							<nested:select property="kakoKakudukeTo" style="width:150">
								<html:option value=""></html:option>
								<nested:optionsCollection property="ar_kakoKakudukeTo" value="value" label="key"/>
							</nested:select>
						</TD>
					</TR>
					<TR>
						<%-- 過去格付参照時点 --%>
						<TD style="width: 2%;" class="inputThColor"></TD>
						<TD style="width:15%;"class="left"><%=i18n.get(GL.OS7111_KAKOJITEN)%></TD>
						<TD>
							<nested:text property="kakoKakudukeJiten" maxlength="3" size="3" style="text-align:right;padding-right:1px;"></nested:text>
							&nbsp;&nbsp;<%=i18n.get(GL.OS7111_KAGETUMAE)%></TD>
					</TR>
				</TABLE>
			</DIV>
		<br>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
