<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>
<bean:define id="SateiForm" name="03SateiForm" type="app.syokai.form.SateiForm" />
<% Pager pager = SateiForm.getPager(); %>

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
	
		<%-- OS6101 査定内容照会 --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS6101)%></H1>
		
		<DIV id="submenu">
				<%-- 戻る --%>	
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>
		
	<DIV id="list">
		<html:form action="/syokai/satei" >
			<html:hidden property="id" styleId="selectIdx"/>
		
			<DIV class="leftbox">
				<%-- 査定検索 --%>
				<html:radio onclick="doSubmit('satei')"  property="satei_tairyu" value="2"/><span style="margin-top:3px;"><%=i18n.get(GL.OS6101_SATEI_KENSAKU)%>&nbsp;</span>
				<%-- 滞留判定検索 --%>
				<html:radio onclick="doSubmit('satei')"  property="satei_tairyu" value="1"/><span style="margin-top:3px;"><%=i18n.get(GL.OS6101_TAIRYU_KENSAKU)%>&nbsp;</span>
			</DIV>
			<BR><BR><BR>
				<DIV id="headerlist">
					<TABLE class='none'>
						<TR>
							<%-- 査定期 --%>
							<TD style="width:60px;border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_SATEIKI)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text property="sateiki" maxlength="6" size="10" /><%=i18n.get(GL.OS6101_YM_LABEL)%>
							</TD>
							<%-- 半期・四半期区分 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<%-- 査定検索時：表示 --%>
								<logic:equal name="SateiForm" property="satei_tairyu" value="2">
									<html:select property="hanki_sihanki">
										<html:option value=""></html:option>
										<html:optionsCollection name="SateiForm" property="ar_hanki_sihanki" value="value" label="key" />
									</html:select>
								</logic:equal>
							</TD>
							<%-- 対象年月 --%>
							<TD style="width:60px;border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_YM)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text property="month" maxlength="6" size="10"/><%=i18n.get(GL.OS6101_YM_LABEL)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
						<TR>
							<%-- 勘定先CD --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_KANJYOCD)%></TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text property="kanjo_cd" maxlength="12"/><%=i18n.get(GL.OS6101_PREFIX_SEARCH)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<%-- DUNS No. --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_DUNS_NO)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text property="duns_no" style="width:150" maxlength="9"/>
							</TD>
						</TR>
						<TR>
							<%-- 勘定先名称 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_KANJYONAME)%></TD>
							<TD colspan='4' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text property="kanjo_nm" style="width:320;ime-mode: active;" maxlength="120" styleClass="doubleByte"/><%=i18n.get(GL.OS6101_PARTIAL_SEARCH)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<%-- 所在国 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_COUNTRY)%></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="country" style="width:150px">
									<html:option value=""></html:option>
									<%-- 課題No.210 --%>
									<%-- 修正開始 --%>
									<html:optionsCollection name="SateiForm" property="ar_country" value="key" label="value" />
									<%-- 修正完了 --%>
								</html:select>
							</TD>
						</TR>
						<%-- 査定検索時：表示 --%>
						<logic:equal name="SateiForm" property="satei_tairyu" value="2">
							<TR>
								<%-- 抽出事由 --%>
								<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_TYUSYUTU)%></TD>
								<TD colspan='10' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<html:select property="tyusyutu">
										<html:option value=""></html:option>
										<html:optionsCollection name="SateiForm" property="ar_tyusyutu" value="value" label="key" />
									</html:select>
								</TD>
							</TR>
							<TR>
								<%-- 取引先区分 --%>
								<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_HEAD_TORI_KBN)%></TD>
								<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<html:select property="torihikisaki_kbn" style="width:180px">
										<html:option value=""></html:option>
										<html:optionsCollection name="SateiForm" property="ar_torihikisaki_kbn" value="value" label="key" />
									</html:select>
								</TD>
								<%-- 債権区分 --%>
								<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_HEAD_SAIKEN_KBN)%></TD>
								<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<html:select property="saiken_kbn" style="width:180px">
										<html:option value=""></html:option>
										<html:optionsCollection name="SateiForm" property="ar_saiken_kbn" value="value" label="key" />
									</html:select>
								</TD>
							</TR>
						</logic:equal>
						<TR>
							<%-- 汎用1 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><nested:write property="hanyou1Title"/></TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou1" style="width:70" onchange="doSubmit('change1')">
									<html:option value=""></html:option>
									<html:optionsCollection name="SateiForm" property="ar_hanyou1" value="value" label="key" />
								</html:select>
							</TD>
							<%-- 汎用2 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><nested:write property="hanyou2Title"/></TD>
							<TD colspan='2' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou2" style="width:180px" onchange="doSubmit('change2')">
									<html:option value=""></html:option>
									<html:optionsCollection name="SateiForm" property="ar_hanyou2" value="value" label="key" />
								</html:select>
							</TD>
							<%-- 汎用3 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><nested:write property="hanyou3Title"/></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:select property="hanyou3" style="width:150px">
									<html:option value=""></html:option>
									<html:optionsCollection name="SateiForm" property="ar_hanyou3" value="value" label="key" />
								</html:select>
							</TD>
						</TR>
						<TR>
							<%-- 関係者 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_KANKEISHA)%></TD>
							<TD colspan='6' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
								<html:text style="width:320px" property="parties" maxlength="100" /><%=i18n.get(GL.OS6101_EMAIL_LABEL)%>&nbsp<%=i18n.get(GL.OS6101_PREFIX_SEARCH)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
						<TR>
							<%-- 処理日 --%>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"><%=i18n.get(GL.OS6101_SYORIBI)%></TD>
							<TD colspan='5' style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<html:text property="syori_dtFrom" maxlength="8" size="12"/><%=i18n.get(GL.OS6101_YMD_LABEL)%>&nbsp<%=i18n.get(GL.OS6101_NAMIGATA)%>
							<html:text property="syori_dtTo" maxlength="8" size="12"/><%=i18n.get(GL.OS6101_YMD_LABEL)%>
							</TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
							<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;"></TD>
						</TR>
					</TABLE>
					<DIV id="submenu"class="semaku">
						<%-- 検索ボタン --%>
						<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" onclick="doSubmit('search')">
					</DIV>
			
					<TABLE style="border:0px;width:100%;">
						<TR style="border:0px;">
						<TD style="border:0px;width:12%;"class="semaku"><BR>
						</TD>
						<%-- ソート順セレクトボックス --%>
						<TD style="border:0px;width:8%;"class="semaku"><%=i18n.get(GL.COMMON_SORT)%><BR>
						</TD>
						<TD style="border:0px;width:15%;text-align:left;"class="semaku">
							<html:select property="sort_item" style="width:110">
								<html:optionsCollection name="SateiForm" property="ar_sort_item" value="value" label="key" />
							</html:select>
						</TD>
						<%-- 整列方向 --%>
						<TD align="right" style="border:0px;width:3%;">
							<html:select property="sort_order" >
								<html:optionsCollection name="SateiForm" property="ar_sort_order" value="value" label="key" />
							</html:select>
						</TD>
						<%-- 表示件数セレクトボックス --%>
						<TD style="border:0px;width:10%;text-align:right;"class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR>
						</TD>
						<TD style="border:0px;width:10%;"class="semaku">
							<html:select property="view" onchange="doSubmit('show')">
								<html:optionsCollection name="SateiForm" property="ar_show" value="value" label="key" />
							</html:select>
						</TD>
						<%-- ←前のXX件 --%>
						<TD style="border:0px;width:14%;text-align:right;">
							<logic:notEqual name="SateiForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SateiForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="SateiForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- 次のXX件→ --%>
						<TD style="border:0px;width:13%;text-align:right;">
							<logic:notEqual name="SateiForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SateiForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="SateiForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>
						<%-- XX/YY件 --%>
						<TD style="border:0px;width:15%;text-align:right;"class="semaku">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
						</TD>
						</TR>
					</TABLE>
				</DIV>
				<DIV class="mainlist">
					<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color: #AAA;">
						<THEAD>
							<TR style="height: 35px;">
								<%-- 滞留判定検索時 --%>
								<logic:equal name="SateiForm" property="satei_tairyu" value="1">
									<%-- 勘定先CD --%>
									<TH width="10%"><%=i18n.get(GL.OS6101_KANJYOCD)%></TH>
									<%-- 勘定先名称 --%>
									<TH width="18%"><%=i18n.get(GL.OS6101_KANJYONAME)%></TH>
									<%-- 対象年月 --%>
									<TH width="10%"><%=i18n.get(GL.OS6101_YM)%></TH>
									<%-- 組織 --%>
									<TH width="27%"><%=i18n.get(GL.OS6101_SOSHIKI)%></TH>
									<%-- 金額計 --%>
									<TH width="14%"><%=i18n.get(GL.OS6101_KINGAKU_TOTAL)%></TH>
									<%-- 進捗 --%>
									<TH><%=i18n.get(GL.OS6101_SHINCHOKU)%></TH>
								</logic:equal>
								<%-- 査定検索時 --%>
								<logic:equal name="SateiForm" property="satei_tairyu" value="2">							
									<%-- 勘定先CD --%>
									<TH width="7%"><%=i18n.get(GL.OS6101_KANJYOCD)%></TH>
									<%-- 勘定先名称 --%>
									<TH width="19%"><%=i18n.get(GL.OS6101_KANJYONAME)%></TH>
									<%-- 対象年月 --%>
									<TH width="9%"><%=i18n.get(GL.OS6101_YM)%></TH>
									<%-- 取引先区分 --%>
									<TH width="7%"><%=i18n.get(GL.OS6101_TORI_KBN)%></TH>
									<%-- 債権区分 --%>
									<TH width="7%"><%=i18n.get(GL.OS6101_SAIKEN_KBN)%></TH>
									<%-- 汎用2 --%>
									<TH width="6%"><nested:write property="hanyou2Title"/></TH>
									<%-- 総債権残 --%>
									<TH width="14%"><%=i18n.get(GL.OS6101_SOUSAIKENZAN)%></TH>
									<%-- 追加引当金 --%>
									<TH width="14%"><%=i18n.get(GL.OS6101_TUIKA_HIKIATEKIN)%></TH>
									<%-- 進捗 --%>
									<TH><%=i18n.get(GL.OS6101_SHINCHOKU)%></TH>
								</logic:equal>
							</TR>
						</THEAD>
						<TBODY>
							<% if(SateiForm.getList() != null) { %>
								<nested:iterate name="SateiForm" property="list" indexId="idx">
									<nested:equal property="hanki_sihanki_kbn" value="2">
										<TR style='background-color:#CCFFFF'>
											<%-- 滞留判定検索時 --%>
											<logic:equal name="SateiForm" property="satei_tairyu" value="1">								
												<%-- 勘定先CD --%>
												<TD width="10%">
													<a href="#" onClick="doDetail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</TD>
												<%-- 勘定先名称 --%>
												<TD width="18%"><nested:write property="kanjo_nm" />&nbsp;</TD>
												<%-- 対象年月 --%>
												<TD width="10%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
												<%-- 組織 --%>
												<TD width="27%"><nested:write property="soshiki" /></TD>
												<%-- 金額計 --%>
												<TD width="14%" class="right"><nested:write property="kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 進捗 --%>
												<TD><nested:write property="sintyoku" />&nbsp;</TD>
											</logic:equal>
											
											<%-- 査定検索時 --%>
											<logic:equal name="SateiForm" property="satei_tairyu" value="2">
												<%-- 勘定先CD --%>
												<TD width="10%">
													<a href="#" onClick="doDetail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</TD>
												<%-- 勘定先名称 --%>
												<TD width="19%"><nested:write property="kanjo_nm" />&nbsp;</TD>
												<%-- 対象年月 --%>
												<TD width="9%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
												<%-- 取引先区分 --%>
												<TD width="7%"><nested:write property="tori_kbn_nm" />&nbsp;</TD>
												<%-- 債権区分 --%>
												<TD width="7%"><nested:write property="sai_kbn_nm" />&nbsp;</TD>
												<%-- 汎用2 --%>
												<TD width="6%"><nested:write property="init_bunrui2" />&nbsp;</TD>
												<%-- 総債権残 --%>
												<TD width="14%" class="right"><nested:write property="kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 追加引当金 --%>
												<TD width="14%" class="right"><nested:write property="tuika_kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 進捗 --%>
												<TD><nested:write property="sintyoku" />&nbsp;</TD>
											</logic:equal>
										</TR>
									</nested:equal>
									<nested:notEqual property="hanki_sihanki_kbn" value="2">
										<TR>
											<%-- 滞留判定検索時 --%>
											<logic:equal name="SateiForm" property="satei_tairyu" value="1">								
												<%-- 勘定先CD --%>
												<TD width="10%">
													<a href="#" onClick="doDetail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</TD>
												<%-- 勘定先名称 --%>
												<TD width="18%"><nested:write property="kanjo_nm" />&nbsp;</TD>
												<%-- 対象年月 --%>
												<TD width="10%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
												<%-- 組織 --%>
												<TD width="27%"><nested:write property="soshiki" /></TD>
												<%-- 金額計 --%>
												<TD width="14%" class="right"><nested:write property="kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 進捗 --%>
												<TD><nested:write property="sintyoku" />&nbsp;</TD>
											</logic:equal>
											
											<%-- 査定検索時 --%>
											<logic:equal name="SateiForm" property="satei_tairyu" value="2">
												<%-- 勘定先CD --%>
												<TD width="10%">
													<a href="#" onClick="doDetail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</TD>
												<%-- 勘定先名称 --%>
												<TD width="19%"><nested:write property="kanjo_nm" />&nbsp;</TD>
												<%-- 対象年月 --%>
												<TD width="9%"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
												<%-- 取引先区分 --%>
												<TD width="7%"><nested:write property="tori_kbn_nm" />&nbsp;</TD>
												<%-- 債権区分 --%>
												<TD width="7%"><nested:write property="sai_kbn_nm" />&nbsp;</TD>
												<%-- 汎用2 --%>
												<TD width="6%"><nested:write property="init_bunrui2" />&nbsp;</TD>
												<%-- 総債権残 --%>
												<TD width="14%" class="right"><nested:write property="kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 追加引当金 --%>
												<TD width="14%" class="right"><nested:write property="tuika_kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
												<%-- 進捗 --%>
												<TD><nested:write property="sintyoku" />&nbsp;</TD>
											</logic:equal>
										</TR>
									</nested:notEqual>
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
