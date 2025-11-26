<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="IchiranForm" name="02IchiranForm" type="app.satei.form.IchiranForm" />
<% Pager pager = IchiranForm.getPager();%>
<script>
	function sateiMogitoriConfirm(event,id) {
		if(window.confirm('<%=i18n.get(GL.CONFIRM_TAKE)%>')){
			sateiSyosai(event,id);
		}
	}
	function sateiSyosai(event,id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		form = document.forms[0];	
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OC1101)%></H1>

			<html:form action="/satei/ichiran" >
			<html:hidden property="id" />
			<DIV id="submenu">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
				<input type="button" value="<%=i18n.get(GL.BTN_TAKE_ALL)%>" onclick="doSubmit('allMogitori')">
			</DIV>

			<DIV id="list">
				<DIV class="leftbox">
					<%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%>&nbsp;
					<html:select property="sateiki" onchange="doSubmit('sateiki')">
    				<html:optionsCollection name="IchiranForm" property="ar_sateiki" value="value" label="key" />
	  				</html:select>
				</DIV>
 
				<DIV class="rightbox">
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TABLE class="titl">
						<TR><TD class="nonBorder">&nbsp;</TD></TR>
						<TR><TD class="nonBorder"><%=i18n.get(GL.OC1101_ITIJISATEI)%></TD></TR>
						<TR><TD class="nonBorder"><%=i18n.get(GL.OC1101_ITIJISATEI_K)%></TD></TR>
						<TR><TD class="nonBorder"><%=i18n.get(GL.OC1101_NIJISATEI)%></TD></TR>
						<TR><TD class="nonBorder"><%=SESSION_DATA_APP.getLbl_nm4()%></TD></TR>
					</TABLE>
					<%} else {%>
					<TABLE class="titl">
						<TR><TD class="nonBorder">&nbsp;</TD></TR>
						<TR><TD class="nonBorder"><DIV class="top10"><%=i18n.get(GL.OC1101_ITIJISATEI)%></DIV></TD></TR>
						<TR><TD class="nonBorder"><DIV class="top1"><%=i18n.get(GL.OC1101_ITIJISATEI_K)%></DIV></TD></TR>
						<TR><TD class="nonBorder"><DIV class="top1"><%=i18n.get(GL.OC1101_NIJISATEI)%></DIV></TD></TR>
						<TR><TD class="nonBorder"><DIV class="top1"><%=SESSION_DATA_APP.getLbl_nm4()%></DIV></TD></TR>
					</TABLE>
					<%}%>
					<TABLE class="tbl">
						<TR>
							<TH><%=i18n.get(GL.COMMON_UNPROCESSED)%></TH>
							<TH><%=i18n.get(GL.COMMON_PROCESSING)%></TH>
							<TH><%=i18n.get(GL.COMMON_WATING_APPROVAL)%></TH>
							<TH><%=i18n.get(GL.COMMON_COMPLETE)%></TH>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="itijisatei_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisatei_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisatei_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisatei_kanryo" /></TD>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="itijisateikensyo_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisateikensyo_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisateikensyo_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="itijisateikensyo_kanryo" /></TD>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="nijisatei_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="nijisatei_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="nijisatei_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="nijisatei_kanryo" /></TD>
						</TR>
						<TR>
							<TD><bean:write name="IchiranForm" property="hanyou3_misyori" /></TD>
							<TD><bean:write name="IchiranForm" property="hanyou3_syorityu" /></TD>
							<TD><bean:write name="IchiranForm" property="hanyou3_syoninmati" /></TD>
							<TD><bean:write name="IchiranForm" property="hanyou3_kanryo" /></TD>
						</TR>
					</TABLE>
				</DIV>

				<DIV class="headerlist">
					<TABLE>
						<TR style="width:100%;">
							<%-- 担当 ラジオボタン --%>
							<TD style="width:2%;"><html:radio onclick="doSubmit('tanto')" property="tanto" value="1"/></TD>
							<TD style="width:10%;"><%=i18n.get(GL.COMMON_MYTASKS)%></TD>
							<TD style="width:2%;"><html:radio onclick="doSubmit('tanto')" property="tanto" value="2"/></TD>
							<TD style="border:0px;width:10%;"><%=SESSION_DATA_APP.getLbl_nm2()%></TD>
							<%-- ソート順 selectBox --%>
							<TD style="width:10%;"><%=i18n.get(GL.COMMON_SORT)%></TD>
							<TD style="width:12%;">
								<html:select property="sort_item" onchange="doSubmit('sort_item')" style="width:100">
									<html:optionsCollection name="IchiranForm" property="ar_sort_item" value="value" label="key" /></html:select>
							</TD>
							<%-- 整列方向 --%>
							<TD style="width:3%;">
								<html:select property="sort_order" onchange="doSubmit('sort_order')" style="width:70">
									<html:optionsCollection name="IchiranForm" property="ar_sort_order" value="value" label="key" /></html:select>
							</TD>
							<%-- 表示件数セレクトボックス --%>
							<TD class="right" style="width:11%;"><%=i18n.get(GL.COMMON_SHOW)%></TD>
							<TD style="width:15%;"><html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="IchiranForm" property="ar_show" value="value" label="key" /></html:select>
							</TD>
							<%-- ←前のXX件 --%>
							<TD style="width:13%;">
								<logic:notEqual name="IchiranForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="IchiranForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- 次のXX件→ --%>
							<TD style="width:14%;">
								<logic:notEqual name="IchiranForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="IchiranForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>

							<%-- XX/YY件 --%>
							<TD class="right" style="width:13%;">
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
								<TH colspan=3 style="width:11%;"><%=i18n.get(GL.OC1101_KANJO_CD)%></TH>
								<TH colspan=6 style="width:27%;"><%=i18n.get(GL.OC1101_KANJO_NM)%></TH>
								<TH colspan=5 style="width:22%;"><%=i18n.get(GL.OC1101_JIYU)%></TH>
								<TH colspan=5 style="width:20%;"><%=i18n.get(GL.OC1101_KINGAKU)%></TH>
								<TH colspan=2 style="width:10%;"><%=i18n.get(GL.OC1101_TAISYO_YM)%></TH>
								<TH colspan=1 rowspan=2 style="width:10%;text-align:center;"class="borderRight"><%=i18n.get(GL.OC1101_MOGITORI)%></TH>
							</TR>
							<TR>
								<TH colspan=2 style="width:9%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
								<TH colspan=12 style="width:51%;"><%=i18n.get(GL.OC1101_SOSHIKI)%></TH>
								<TH colspan=3 style="width:15%;"><%=i18n.get(GL.OC1101_TANTO_NM)%></TH>
								<TH colspan=4 style="width:15%;"><%=i18n.get(GL.OC1101_PROGRESS)%></TH>
							</TR>
						</THEAD>
					<TBODY>
						<% if(IchiranForm.getList() != null) { %>
							<nested:iterate name="IchiranForm" property="list" indexId="idx">
								<%String bgColor = "background-color:#FFFFFF";%>
								<nested:equal property="hanki_sihanki_kbn" value='2'>
									<%bgColor = "background-color:#CCFFFF";%>
								</nested:equal>
								<nested:equal property="differ_flg" value='true'><%bgColor = "background-color:#FFC1E0";%></nested:equal>
								<TR style='<%=bgColor%>'>
									<TD colspan=3>
											<nested:equal property="link_flg" value="true">
												<%-- IT036対応 --%>
												<nested:equal property="hoji_user_id" value="">
													<a href="#" onClick="sateiMogitoriConfirm('mogitori','<nested:write property="id"/>')" /><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:notEqual property="hoji_user_id" value="">
													<a href="#" onClick="sateiSyosai('mogitori','<nested:write property="id"/>')" /><nested:write property="kanjo_cd" /></a>
												</nested:notEqual>
												<%-- IT036ここまで --%>
											</nested:equal>
											<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" />
											</nested:equal>&nbsp;
									</TD>
									<TD colspan=6 ><nested:write property="kanjo_nm" />&nbsp;</TD>
									<TD colspan=5><nested:write property="jiyu_nm" />&nbsp;</TD>
									<TD colspan=5 class="right"><nested:write property="kingaku" />&nbsp;</TD>
									<TD colspan=2><nested:write property="taisyo_ym_hyoji" />
													<nested:equal property="hanki_sihanki_kbn" value="2"><%= GS.QUARTER_CHARCTER %></nested:equal>&nbsp;</TD>
									<TD colspan=1 rowspan=2 class="center borderBottom borderRight">
										<nested:equal property="link_flg" value="true"><nested:checkbox property="mogitori_chk" value="1" /></nested:equal>
										<nested:equal property="link_flg" value="false"><nested:checkbox property="mogitori_chk" value="1" disabled="true" /></nested:equal>
										</TD>
									</TR>	
									<TR style='<%=bgColor%>'>
										<TD colspan=2 class="borderBottom"><nested:write property="sateikaisya_cd" />&nbsp;</TD>
										<TD colspan=12 class="borderBottom "><nested:write property="soshiki" /></TD>
										<TD colspan=3 class="borderBottom "><nested:write property="tanto_nm" />&nbsp;</TD>
										<TD colspan=4 class="borderBottom"><nested:write property="sintyoku" />&nbsp;</TD>
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