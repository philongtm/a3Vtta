<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>
<%@ page import="app.TorihikisakiBean" %>
<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="SincyokuForm" name="03SincyokuForm" type="app.syokai.form.SincyokuForm" />
<% Pager pager = SincyokuForm.getPager(); %>

<script language="javascript">
	function doDetail(index) {
		document.getElementById("selectIdx").value = index;
		doSubmit('detailPage');
	}
</script>

</HEAD>
<BODY>
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
		<%-- OS6103 進捗状況照会  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS6103)%></H1>
		<DIV id="submenu">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>
		<DIV id="list">
			<html:form action="/syokai/sincyoku" >
				<html:hidden property="id" styleId="selectIdx"/>
				<DIV align='left'>
					<TABLE class='none' >
						<TR>
							<%-- 対象年月 --%>
							<TD style="width:60px;border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_MONTH)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- (YYYYMM) --%>
								<html:text name="SincyokuForm" property="month" maxlength="6" size="10"/><%=i18n.get(GL.OS6103_KENSAKU_YM)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="width:70px;border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
						<TR>
							<%-- 勘定先CD --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_KANJO_CD)%></TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- (前方一致) --%>
								<html:text name="SincyokuForm" property="kanjo_cd" maxlength="12"/><%=i18n.get(GL.COMMON_ZENPOUICCHI)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<%-- DUNS No. --%>
							<TD style="width:150px;border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_DUNS_NO)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text name="SincyokuForm" property="duns_no" maxlength="9" style="width:150px"/>
							</TD>
						</TR>
						<TR>
							<%-- 勘定先名称 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_KANJO_NM)%></TD>
							<TD colspan='4' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- (部分一致) --%>
								<html:text name="SincyokuForm" property="kanjo_nm" maxlength="120" style="width:320px" styleClass="doubleByte"/><%=i18n.get(GL.COMMON_BUBUNICCHI)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<%-- 所在国 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_COUNTRY)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="country" style="width:150px">
									<html:option value=""></html:option>
									<%-- 課題No.210 --%>
									<%-- 修正開始 --%>
									<html:optionsCollection name="SincyokuForm" property="ar_country" value="key" label="value" />
									<%-- 修正完了 --%>
								</html:select>
						</TR>
						<TR>
							<%-- フェーズ --%>
							<TD style="border:0px;width:13%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_PHASE)%></TD>
							<TD colspan='3' style="border:0px;width:20%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="phase" style="width:100%">
									<html:option value=""></html:option>
									<html:optionsCollection name="SincyokuForm" property="ar_phase" value="value" label="key" />
								</html:select>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<%-- ステータス --%>
							<TD style="border:0px;width:60px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_STATUS)%></TD>
							<TD style="border:0px;width:150px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="status" style="width:100%">
									<html:option value=""></html:option>
									<html:optionsCollection name="SincyokuForm" property="ar_status" value="value" label="key" />
								</html:select>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
						<TR>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 汎用1 --%>
								<nested:write property="hanyou1Title"/>
							</TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou1" style="width:70" onchange="doSubmit('change1')">
									<html:option value=""></html:option>
									<html:optionsCollection name="SincyokuForm" property="ar_hanyou1" value="value" label="key" />
								</html:select>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 汎用2 --%>
								<nested:write property="hanyou2Title"/>
							</TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou2" style="width:150px" onchange="doSubmit('change2')">
									<html:option value=""></html:option>
									<html:optionsCollection name="SincyokuForm" property="ar_hanyou2" value="value" label="key" />
								</html:select>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 汎用3 --%>
								<nested:write property="hanyou3Title"/>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou3" style="width:150px">
									<html:option value=""></html:option>
									<html:optionsCollection name="SincyokuForm" property="ar_hanyou3" value="value" label="key" />
								</html:select>
							</TD>
						</TR>
						<TR>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 関係者 --%>
								<%=i18n.get(GL.OS6103_KENSAKU_PARTIES)%>
							</TD>
							<TD colspan='5' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- (e-mail address) --%><%-- (前方一致) --%>
								<html:text name="SincyokuForm" property="parties" style="width:320px" maxlength="100"/><%=i18n.get(GL.OS6103_KENSAKU_MAIL)%> <%=i18n.get(GL.COMMON_ZENPOUICCHI)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
						<TR>
							<%-- 処理日 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6103_KENSAKU_SYORI_DTFROM)%></TD>
							<TD colspan='5' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- (YYYYMMDD) --%>
								<html:text name="SincyokuForm" property="syori_dtFrom" maxlength="8" size="12"/><%=i18n.get(GL.OS6103_KENSAKU_YMD)%>
								<%-- ～ --%>
								<%-- 課題No148 ２バイト文字対応 --%>
								<%-- 追加開始 --%>
								&nbsp<%=i18n.get(GL.OS6103_NAMIGATA)%>&nbsp
								<%-- 追加完了 --%>
								<html:text name="SincyokuForm" property="syori_dtTo" maxlength="8" size="12"/><%=i18n.get(GL.OS6103_KENSAKU_YMD)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
					</TABLE>
					<DIV id="submenu"class="semaku">
						<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')"/>
					</DIV>
				</DIV>
				
				<DIV class="mainlist">
					<TABLE style="border:0px;width:100%;">
						<TR style="border:0px;">
							<TD style="border:0px;width:41%;"class="semaku"><BR>
							</TD>
							<%-- 表示件数 --%>
							<TD align="right" style="border:0px;width:9%;"class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR>
							</TD>
							<TD style="border:0px;width:9%;"class="semaku">
								<html:select property="view" onchange="doSubmit('show')" style="width:70">
									<html:optionsCollection name="SincyokuForm" property="ar_show" value="value" label="key" />
								</html:select>
							</TD>
							<%-- ←前のXX件 --%>
							<TD style="border:0px;width:15%;text-align:right;">
								<logic:notEqual name="SincyokuForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SincyokuForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="SincyokuForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							
							<TD style="border:0px;width:15%;text-align:right;">
								<logic:notEqual name="SincyokuForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SincyokuForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="SincyokuForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<TD style="border:0px;width:10%;text-align:right;"class="semaku">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
					</TABLE>
				
					<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color: #AAA;">
						<THEAD>
							<TR>
								<TH width="7%">
									<%-- 勘定先CD --%>
									<%=i18n.get(GL.OS6103_KENSAKU_KANJO_CD)%>
								</TH>
								<TH width="21%">
									<%-- 勘定先名称 --%>
									<%=i18n.get(GL.OS6103_KENSAKU_KANJO_NM)%>
								</TH>
								<TH width="5%">
									<%-- 汎用1 --%>
									<nested:write property="hanyou1Title"/>
								</TH>
								<TH width="32%">
									<%-- 組織 --%>
									<%=i18n.get(GL.OS6103_SOSHIKI)%>
								</TH>
								<TH width="9%">
									<%-- 対象年月 --%>
									<%=i18n.get(GL.OS6103_KENSAKU_MONTH)%>
								</TH>
								<TH width="10%">
									<%-- 担当者名 --%>
									<%=i18n.get(GL.OS6103_TANTO_NM)%>
								</TH>
								<TH width="19%">
									<%-- 進捗 --%>
									<%=i18n.get(GL.OS6103_PROGRESS)%>
								</TH>
							</TR>
						</THEAD>
						<TBODY>
						<% if(SincyokuForm.getList() != null) { %>
						<%
							//課題No.30 パフォーマンスアップ対応
							//削除開始
							//List<TorihikisakiBean> ar_meisai = new ArrayList<TorihikisakiBean>();
							//ar_meisai = SincyokuForm.getList();
							//int i = 0;
							//削除完了
							String bgColor = "background-color:#FFFFFF";
							String strQ = GS.EMPTY_CHARCTER;
						%>
							<nested:iterate name="SincyokuForm" property="list" indexId="idx">
								<nested:equal property="hanki_sihanki_kbn" value='2'>
									<%bgColor = "background-color:#CCFFFF";%>
									<%strQ = GS.QUARTER_CHARCTER;%>
								</nested:equal>
								<nested:notEqual property="hanki_sihanki_kbn" value='2'>
									<%bgColor = "background-color:#FFFFFF";%>
									<%strQ = GS.EMPTY_CHARCTER;%>
								</nested:notEqual>
								<TR style='<%=bgColor%>'>
									<TD>
										<a href="#" onClick="doDetail('<nested:write property="id" />')">
											<nested:write property="kanjo_cd" />
										</a>
									</TD>
									<TD>
										<nested:write property="kanjo_nm" />
									</TD>
									<TD><nested:write property="sateikaisya_cd" /></TD>
									<%-- 課題No.30 パフォーマンスアップ対応 --%>
									<%-- 修正開始 --%>
									<TD>
										<%-- <%=ar_meisai.get(i).getSoshiki().replaceAll("№1∇№1", "<br>")%> --%>
										<nested:write property="soshiki" />
									</TD>
									<TD>
										<%-- <%=ar_meisai.get(i).getTaisyo_ym_hyoji().replaceAll("№1∇№1", strQ + "<br>")%><%=strQ%> --%>
										<nested:write property="taisyo_ym_hyoji" /><%=strQ%>
									</TD>
									<TD>
										<%-- <%=ar_meisai.get(i).getTanto_nm().replaceAll("№1∇№1", "<br>")%> --%>
										<nested:write property="tanto_nm" />
									</TD>
									<TD>
										<%-- <%=ar_meisai.get(i).getSintyoku().replaceAll("№1∇№1", "<br>")%> --%>
										<nested:write property="sintyoku" />
									</TD>
									<%-- 修正完了 --%>
								</TR>
								<%-- i += 1; --%>
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
