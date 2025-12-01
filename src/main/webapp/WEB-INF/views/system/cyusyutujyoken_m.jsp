<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="CyusyutujyokenForm" name="10CyusyutujyokenForm" type="app.system.form.CyusyutujyokenForm" />
<% Pager pager = CyusyutujyokenForm.getPager(); %>

<script>
function jiyuLink(event, id) {
	form = document.forms[0];
	form.elements["id"].value = id;
	doSubmit(event);
}

//2022/06/10 Fix bug No.17 START
function blockEnter(e) {
	if (e.keyCode=='0xD'){
		window.event.returnValue = false;
		return false;
	}
}
//2022/06/10 Fix bug No.17 END
</script>

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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7110)%></H1>
		<html:form action="/system/cyusyutujyoken">
		<nested:hidden property="id"/>
		<%--検索用--%>
		<nested:hidden property="srhSystemKbn"/>
		<nested:hidden property="srhHanyo1"/>
		<nested:hidden property="srhHanyo2"/>
		<nested:hidden property="srhKesanKbn"/>
		<nested:hidden property="srhKijyunbi"/>
		<nested:hidden property="srhJyokenNm"/>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_SHINKI)%>" onclick="doSubmit('newRegist')" />
			&nbsp;
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')" />
		</DIV>

		<DIV id="list">
			<DIV class="mainlist">
				<DIV class="CNDbox">
				<TABLE class='none'>
				<TR style="border:0px;">
					<TD style="border:0px;width:8%;" class="semaku" >
					<%-- システム --%>
					<%=i18n.get(GL.OS7110_SYSTEM)%>&nbsp;</TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku">
						<nested:select property="systemKbn" onchange="doSubmit('systemKbn')" style="width:70px">
							<nested:optionsCollection property="ar_systemKbn" value="value" label="key" />
						</nested:select>
					</TD>
					<TD style="border:0px;width:8%;" class="semaku">
					<%-- 汎用１ --%>
					<%=SESSION_DATA_APP.getLbl_nm1()%>&nbsp;</TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku">
						<nested:select property="hanyo1" onchange="doSubmit('hanyo1')" style="width:80px">
							<html:option value=""></html:option>
							<nested:optionsCollection property="ar_hanyo1" value="value" label="key" />
						</nested:select>
					</TD>
					<TD style="border:0px;width:8%;" class="semaku">
					<%-- 汎用２ --%>
					<%=SESSION_DATA_APP.getLbl_nm6()%></TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku">
						<nested:select property="hanyo2" style="width:160px">
							<html:option value=""></html:option>
							<nested:optionsCollection property="ar_hanyo2" value="value" label="key" />
						</nested:select>
					</TD>
					<TD style="border:0px;width:25%;" class="semaku" ></TD>

				</TR>
				<TR style="border:0px;">
					<TD style="border:0px;width:12%;" class="semaku" >
					<%-- 決算期区分 --%>
					<%=i18n.get(GL.OS7110_KESSANKIKBN)%>&nbsp;</TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku">
						<nested:select property="kesanKbn" style="width:100px">
							<html:option value=""></html:option>
							<nested:optionsCollection property="ar_kesanKbn" value="value" label="key" />
						</nested:select>
					</TD>
					<TD style="border:0px;width:10%;" class="semaku">
					<%-- 基準日 --%>
					<%=i18n.get(GL.OS7110_KIJYUNBI)%>&nbsp;</TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku">
						<nested:select property="kijyunbi" style="width:100px">
							<html:option value=""></html:option>
							<nested:optionsCollection property="ar_kijyunbi" value="value" label="key" />
						</nested:select>
					</TD>
				</TR>
				<TR style="border:0px;">
					<TD style="border:0px;width:10%;" class="semaku">
					<%-- 条件名称 --%>
					<%=i18n.get(GL.OS7110_JYOKENMEISYO)%>&nbsp;</TD>
					<TD style="border:0px;width:15%;text-align:left;margin-top:2px;"class="semaku"  colspan="4">
					<nested:text property="jyokenNm" style="width:250;ime-mode: active;"onkeydown="blockEnter(event)"></nested:text>
					<%=i18n.get(GL.OS7110_BUBUNICHI)%>
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

				</DIV>
				<TABLE style="border:0px;width:100%;">
					<TR>
						<TD colspan="4" style="border:0px;" class="semaku"><br></TD>
						<%-- 表示件数 --%>
						<TD style="border:0px;width:10%;text-align:right;"class="semaku">
							<%=i18n.get(GL.COMMON_SHOW)%><BR>
						</TD>
						<TD style="border:0px;width:12%;"class="semaku">
							<html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="CyusyutujyokenForm" property="ar_show" value="value" label="key" />
							</html:select>
							<BR>
						</TD>
						<TD colspan="4" style="border:0px;" class="semaku">
						<%-- 前のXX件 --%>
						<TD style="border:0px;width:10%;">
							<logic:notEqual name="CyusyutujyokenForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="CyusyutujyokenForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="CyusyutujyokenForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- 次のXX件 --%>
						<TD style="border:0px;width:10%;">
							<logic:notEqual name="CyusyutujyokenForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="CyusyutujyokenForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="CyusyutujyokenForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- XX/YY件 --%>
						<TD style="border:0px;width:10%;text-align:right;"class="semaku">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%><BR>
						</TD>
					</TR>
				</TABLE>
				<br>
				<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
					<THEAD>
					<TR>
						<%-- システム --%>
						<TH style="width:8%;"><p class=""><%=i18n.get(GL.OS7110_SYSTEM)%></TH>
						<%-- 汎用１ --%>
						<TH style="width:16%;"><p class=""><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
						<%-- 汎用２ --%>
						<TH colspan=2 style="colspan=3""width:16%;"><p class=""><%=SESSION_DATA_APP.getLbl_nm6()%></TH>
						<%-- 決算期区分 --%>
						<TH colspan=2 style="colspan=2""width:24%;"><p class=""><%=i18n.get(GL.OS7110_KESSANKIKBN)%></TH>
						<%-- 基準日 --%>
						<TH class="borderRight" colspan=2 style="colspan=2;width:20%;"><%=i18n.get(GL.OS7110_KIJYUNBI)%></TH>
					</TR>
					<TR>
						<%-- 抽出事由 --%>
						<TH style="width:8%;"><p class="center"><%=i18n.get(GL.OS7110_CHUSYUTUJIYU)%></TH>
						<%-- 条件名称<BR>(日本語/英語) --%>
						<TH style="width:16%;"><p class=""><%=i18n.get(GL.OS7110_ICHIRANJYOKEN)%></TH>
						<%-- 格付 --%>
						<TH style="width:7%;"><p class="center"><%=i18n.get(GL.OS7110_KAKUDUKE)%></TH>
						<%-- 金額基準1 --%>
						<TH style="width:9%;"><p class=""><%=i18n.get(GL.OS7110_KINGAKUKIJUN1)%></TH>
						<%-- 金額基準2 --%>
						<TH style="width:12%;"><p class=""><%=i18n.get(GL.OS7110_KINGAKUKIJUN2)%></TH>
						<%-- 滞留期間(月) --%>
						<TH style="width:12%;"><p class="center"><%=i18n.get(GL.OS7110_TAIRYUKIKAN)%></TH>
						<%-- 実質滞留<BR>判定対象 --%>
						<TH style="width:10%;"><p class="center"><%=i18n.get(GL.OS7110_TAIRYUTAISYO)%></TH>
						<%-- 査定対象 --%>
						<TH style="width:10%;border-right-color:#000000;"><p class="center"><%=i18n.get(GL.OS7110_SATEITAISYO)%></TH>
					</TR>
					</THEAD>
					<nested:notEmpty property="list">
						<nested:iterate property="list" indexId="idx">
							<TR>
								<%-- システム --%>
								<TD style="width:8%;"><p class="">
									<nested:write property="system_kbn_nm"/>
								</TD>
								<%-- 汎用１ --%>
								<TD style="width:12%;">
									<nested:write property="bunrui1"/>
								</TD>
								<%-- 汎用２ --%>
								<TD colspan=2 style="colspan=3""width:16%;">
									<nested:write property="bunrui2"/>
								</TD>
								<%-- 決算期区分 --%>
								<TD colspan=2 style="colspan=2""width:24%;"><p class="">
									<nested:write property="kessanki_kbn_nm"/>
								</TD>
								<%-- 基準日 --%>
								<TD style="border-right-color:#000000" colspan=2 style="colspan=2""width:24%;"><p class="">
									<nested:write property="kijunbi_nm"/>
								</TD>
							</TR>
							<TR>
								<%-- 抽出事由 --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="width:8%;"class="center">
									<a href="#" onClick="jiyuLink('jiyuLink', '<nested:write property="id" />')">
										<nested:write property="tyusyutu_jiyu"/>
									</a>
								</TD>
								<%-- 条件名称<BR>(日本語/英語) --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="width:12%;font-weight:bold;">
									<nested:write property="joken_nm"/><BR><nested:write property="joken_nm_en"/>
								</TD>
								<%-- 格付 --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="colspan=3""width:16%;"><p class="center">
									<nested:write property="ktk"/>
								</TD>
								<%-- 金額基準1 --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="colspan=3""width:16%;"><p class="right">
									<nested:write property="kingaku1_kingaku"/><nested:write property="kingaku_tuuka"/>
								</TD>
								<%-- 金額基準2(滞留区分) --%>
								<TD style="colspan=3""width:16%;"><p class="right">
									&nbsp;<nested:write property="kingaku2_tairyu_nm"/>
									<nested:notEmpty property="kingaku2_tairyu_nm">
										<%=i18n.get(GL.OS7110_IJYO)%>
									</nested:notEmpty>
								</TD>
								<%-- 滞留期間(月) --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="colspan=3""width:16%;"><p class="center">
									<nested:write property="tairyu_kikan_tuki"/>
								</TD>
								<%-- 実質滞留<BR>判定対象 --%>
								<TD style="border-bottom-color:#000000" rowspan="2"style="colspan=3""width:16%;"><p class="center">
									<nested:write property="tairyu_hantei_nm"/>
								</TD>
								<%-- 査定対象 --%>
								<TD style="border-bottom-color:#000000;border-right-color:#000000;" rowspan="2"style="colspan=3""width:16%;"><p class="center">
									<nested:write property="satei_taisyo_nm"/>
								</TD>
							</TR>
							<TR>
								<%-- 金額基準2(金額) --%>
								<TD style="border-bottom-color:#000000" style="colspan=3""width:16%;"><p class="right">
									&nbsp;<nested:write property="kingaku2_kingaku"/><nested:write property="kingaku_tuuka"/>
								</TD>
							<TR>
						</nested:iterate>
					</nested:notEmpty>
				</TABLE>
		<br>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
