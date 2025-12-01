<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>

<bean:define id="IchiranForm" name="06IchiranForm" type="app.hikiate.form.IchiranForm" />
<% Pager pager = IchiranForm.getPager(); %>
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
	<% if(SESSION_DATA_APP.getUser_bean().getComHikiatekin_kensyo_t_flg().equals(GS.ON)){ %>
		<%-- 引当金検証 対象先一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_HIKIATEKENSYO_ICHIRAN)%></H1>
	<% }else{ %>
		<%-- 引当金確認 対象先一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OD1101)%></H1>
	<% }%>
		<DIV id="submenu">
			<%-- 一括もぎ取り --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TAKE_ALL)%>" onclick="doSubmit('ikkatuMogitori')">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
		</DIV>
		<DIV id="list">
			<form action="/hikiate/ichiran" >
				<input type="hidden" name="anken_no" value="">
				<input type="hidden" name="id" value="0">
				<input type="hidden" name="oldTanto" value="0">
				<input type="hidden" name="indexId" value="0">
				
				<DIV id="list">
					<DIV class="mainlist">
					<DIV class="CNDbox">
						<%-- 査定期 --%>
						<%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%>&nbsp;
						<select property="sateiki" onchange="doSubmit('sateiki')" style="width:80">
							<optionsCollection name="IchiranForm" property="ar_sateiki" value="value" label="key" />
						</select>
					</DIV>
			  	</DIV>
			  
				<DIV class="rightbox">
					<TABLE style="border:0px;float:left;">
						<TR><TD style="border:0px;">&nbsp;</TD></TR>
						<%-- 一次査定 --%>
						<TR><TD style="border:0px;"><%=i18n.get(GL.OD1101_SATEI1)%></TD></TR>
						<%-- 一次査定検証 --%>
						<TR><TD style="border:0px;"><%=i18n.get(GL.OD1101_ICHIJISATEIKENSHOU)%></TD></TR>
						<%-- 二次査定 --%>
						<TR><TD style="border:0px;"><%=i18n.get(GL.OD1101_SATEI2)%></TD></TR>
						<%-- 引当金確認 --%>
						<TR><TD style="border:0px;"><%=SESSION_DATA_APP.getLbl_nm4()%></TD></TR>
					</TABLE>
					
					<TABLE cellSpacing=0 cellPadding=0>
					<TR>
						<%-- 未処理 --%>
						<TH><%=i18n.get(GL.COMMON_UNPROCESSED)%></TH>
						<%-- 処理中 --%>
						<TH><%=i18n.get(GL.COMMON_PROCESSING)%></TH>
						<%-- 承認待 --%>
						<TH><%=i18n.get(GL.COMMON_WATING_APPROVAL)%></TH>
						<%-- 完了 --%>
						<TH><%=i18n.get(GL.COMMON_COMPLETE)%></TH>
					</TR>
					<TR>
						<TD><bean:write name="IchiranForm" property="ichiji_misyori" /></TD>
						<TD><bean:write name="IchiranForm" property="ichiji_syorityu" /></TD>
						<TD><bean:write name="IchiranForm" property="ichiji_syoninmati" /></TD>
						<TD><bean:write name="IchiranForm" property="ichiji_kanryo" /></TD>
					</TR>
					<TR>
						<TD><bean:write name="IchiranForm" property="kensyo_misyori" /></TD>
						<TD><bean:write name="IchiranForm" property="kensyo_syorityu" /></TD>
						<TD><bean:write name="IchiranForm" property="kensyo_syoninmati" /></TD>
						<TD><bean:write name="IchiranForm" property="kensyo_kanryo" /></TD>
					</TR>
					<TR>
						<TD><bean:write name="IchiranForm" property="niji_misyori" /></TD>
						<TD><bean:write name="IchiranForm" property="niji_syorityu" /></TD>
						<TD><bean:write name="IchiranForm" property="niji_syoninmati" /></TD>
						<TD><bean:write name="IchiranForm" property="niji_kanryo" /></TD>
					</TR>
					<TR>
						<TD><bean:write name="IchiranForm" property="kakunin_misyori" /></TD>
						<TD><bean:write name="IchiranForm" property="kakunin_syorityu" /></TD>
						<TD><bean:write name="IchiranForm" property="kakunin_syoninmati" /></TD>
						<TD><bean:write name="IchiranForm" property="kakunin_kanryo" /></TD>
					</TR>
					</TABLE>
				</DIV>
			  	
			  	<DIV class="mainlist">
					<TABLE style="border:0px;width:100%;">
						<TR style="border:0px;">
							<TD style="border:0px;width:2%;"><input type="radio" onclick="doSubmit('tanto')" property="tanto" value="1" /><BR>
							</TD>
							<%-- 自担当分 --%>
							<TD style="border:0px;width:11%;"><%=i18n.get(GL.COMMON_MYTASKS)%><BR>
							</TD>
							<TD style="border:0px;width:2%;"><input type="radio" onclick="doSubmit('tanto')" property="tanto" value="2" /><BR>
							</TD>
							<%-- 汎用2 --%>
							<TD style="border:0px;width:12%;"><%=SESSION_DATA_APP.getLbl_nm2()%><BR>
							</TD>
							<%-- ソート順セレクトボックス --%>
							<TD style="border:0px;width:10%;"><%=i18n.get(GL.COMMON_SORT)%><BR>
							</TD>	
							<%-- ソート項目 --%>
							<TD style="border:0px;width:11%;">
								<select property="sort_item" onchange="doSubmit('sort_item')" style="width:100">
									<optionsCollection name="IchiranForm" property="ar_sort_item" value="value" label="key" />
								</select>
							</TD>
							<%-- 整列方向 --%>
							<TD align="right" style="border:0px;width:2%;">
								<select property="sort_order" onchange="doSubmit('sort_order')" style="width:70">
									<optionsCollection name="IchiranForm" property="ar_sort_order" value="value" label="key" />
								</select>
							</TD>
							<%-- 表示件数セレクトボックス --%>
							<TD align="right" style="border:0px;width:11%;"><%=i18n.get(GL.COMMON_SHOW)%><BR>
							<TD style="border:0px;width:13%;">
								<select property="view" onchange="doSubmit('show')" style="width:70">
									<optionsCollection name="IchiranForm" property="ar_show" value="value" label="key" />
								</select>
							</TD>
							<%-- ←前のXX件 --%>
							<TD style="border:0px;width:14%;">
								<logic:notEqual name="IchiranForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<%-- 次のXX件→ --%>
							<TD style="border:0px;width:14%;">
								<logic:notEqual name="IchiranForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<%-- XX/YY件 --%>
							<TD style="border:0px;width:13%;text-align:right;">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
					</TABLE>

					<%-- 一覧情報 --%>					
					<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
						<THEAD>
							<TR>
								<%-- 勘定先CD --%>
								<TH colspan=3 style="width:11%;"><%=i18n.get(GL.OD1101_KANJO_CD)%></TH>
								<%-- 勘定先名称 --%>
								<TH colspan=11 style="width:47%;"><%=i18n.get(GL.OD1101_KANJO_NM)%></TH>
								<%-- 債権残計 --%>
								<TH colspan=5 style="width:20%;"><%=i18n.get(GL.OD1101_SAIKENZANKEI)%></TH>
								<%-- 基準日 --%>
								<TH colspan=2 style="width:15%;"><%=i18n.get(GL.OD1101_KIJUN_YM)%></TH>
								<%-- スペス --%>
								<TH colspan=1 rowspan=2 style="width:7%;text-align:center"class="borderRight"><%=i18n.get(GL.OD1101_MOGITORI)%></TH>
							</TR>
							<TR>
								<%-- 汎用１ --%>
								<TH colspan=2 style="width:9%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<%-- 組織 --%>
								<TH colspan=12 style="width:49%;"><%=i18n.get(GL.OD1101_SOSHIKI)%></TH>
								<%-- 担当者 --%>
								<TH colspan=3 style="width:15%;"><%=i18n.get(GL.OD1101_TANTO_NM)%></TH>
								<%-- 進捗 --%>
								<TH colspan=4 style="width:20%;"><%=i18n.get(GL.OD1101_PROGRESS)%></TH>
							</TR>
						</THEAD>
						<TBODY>
							<% if(IchiranForm.getList() != null) { %>
								<nested:iterate name="IchiranForm" property="list" indexId="idx">
									<nested:equal property="hanki_sihanki_kbn" value="2">
										<TR style='background-color:#CCFFFF'>
											<TD colspan=3 style="width:11%;">
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="mogitoriConfirm('mogitori','<nested:write property="anken_no" />','<nested:write property="id" />','<nested:write property="hoji_user_id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<TD colspan=11 style="width:30%;"><nested:write property="kanjo_nm" />&nbsp;</TD>
											<TD class="right" colspan=5 style="width:20%;"><nested:write property="saiken_kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
											<TD colspan=2 style="width:7%;"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
											<nested:equal property="link_flg" value="true">
												<TD colspan=1 rowspan=2 style="width:7%;border-bottom-color:#000000;border-right-color:#000000;text-align:center">
											</nested:equal>
											<nested:equal property="link_flg" value="false">
												<TD disabled colspan=1 rowspan=2 style="width:7%;border-bottom-color:#000000;border-right-color:#000000;text-align:center">
											</nested:equal>
												<input type="checkbox" property="selectedMountains" ><nested:write property="id" /></input type="checkbox">
											</TD>
										</TR>
										<TR style='background-color:#CCFFFF'>
											<TD class="center" colspan=2 style="width:9%;border-bottom-color:#000000;"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
											<TD colspan=12 style="width:57%;border-bottom-color:#000000;"><nested:write property="soshiki" /></TD>
											<TD colspan=3 style="width:15%;border-bottom-color:#000000;"><nested:write property="tanto_nm" />&nbsp;</TD>
											<TD colspan=4 style="width:12%;border-bottom-color:#000000;"><nested:write property="sintyoku" />&nbsp;</TD>
										</TR>
									</nested:equal>
									<nested:notEqual property="hanki_sihanki_kbn" value="2">
										<TR >
											<TD colspan=3 style="width:11%;">
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="mogitoriConfirm('mogitori','<nested:write property="anken_no" />','<nested:write property="id" />','<nested:write property="hoji_user_id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<TD colspan=11 style="width:30%;"><nested:write property="kanjo_nm" />&nbsp;</TD>
											<TD class="right" colspan=5 style="width:20%;"><nested:write property="saiken_kingaku" /><nested:write property="tuuka_cd" />&nbsp;</TD>
											<TD colspan=2 style="width:7%;"><nested:write property="taisyo_ym_hyoji" />&nbsp;</TD>
											<nested:equal property="link_flg" value="true">
												<TD colspan=1 rowspan=2 style="width:7%;border-bottom-color:#000000;border-right-color:#000000;text-align:center">
											</nested:equal>
											<nested:equal property="link_flg" value="false">
												<TD disabled colspan=1 rowspan=2 style="width:7%;border-bottom-color:#000000;border-right-color:#000000;text-align:center">
											</nested:equal>
												<input type="checkbox" property="selectedMountains" ><nested:write property="id" /></input type="checkbox">
											</TD>
										</TR>
										<TR >
											<TD colspan=2 style="width:9%;border-bottom-color:#000000;"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
											<TD colspan=12 style="width:57%;border-bottom-color:#000000;"><nested:write property="soshiki" /></TD>
											<TD colspan=3 style="width:15%;border-bottom-color:#000000;"><nested:write property="tanto_nm" />&nbsp;</TD>
											<TD colspan=4 style="width:12%;border-bottom-color:#000000;"><nested:write property="sintyoku" />&nbsp;</TD>
										</TR>
									</nested:notEqual>
								</nested:iterate>
							<% } %>
						</TBODY>
					</TABLE>
				</DIV>
			  	
			</form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>