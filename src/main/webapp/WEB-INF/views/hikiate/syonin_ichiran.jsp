<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="HikiateSyoninForm" name="06HikiateSyoninForm" type="app.hikiate.form.HikiateSyoninForm" />
<% Pager pager = HikiateSyoninForm.getPager(); %>

<script language=javascript>
	// 一括承認チェックボックスアクション
	function selectAll(form){
		for (var i=0;i<form.elements.length;i++){
			var e = form.elements[i];
			if (e.Name != 'chkAll' && e.disabled==false) {
				e.checked = form.chkAll.checked;
			}
		}
	}
	
	// OD1104へ遷移する
	function detail(index){
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
	<% if(SESSION_DATA_APP.getUser_bean().getComHikiatekin_kensyo_s_flg().equals(GS.ON)){ %>
		<%-- 引当金検証 承認一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_HIKIATEKENSYO_SYONIN_ICHIRAN)%></H1>
	<% }else{ %>
		<%-- 引当金確認 承認一覧  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OD1103)%></H1>
	<% }%>
		<DIV id="submenu">
			<%-- 承認実行 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_APPROVE)%>" onclick="doSubmit('zikko')"/>
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')"/>
		</DIV>
		<DIV id="list">
			<html:form action="/hikiate/syonin_ichiran" >
				<html:hidden property="id" styleId="selectIdx"/>
				<DIV class="mainlist">
					<input type="hidden" name="anken_no" value="">
					<input type="hidden" name="id" value="0">
					<input type="hidden" name="oldTanto" value="0">
					<input type="hidden" name="indexId" value="0">
				
					<TABLE style="border:0px;width:100%;"  class="semaku">
						<TR style="border:0px;" class="semaku">
							<TD style="border:0px;width:40%;" class="semaku"><BR>
							</TD>
							<%-- 表示件数セレクトボックス --%>
							<TD align="right" style="border:0px;width:10%;" class="semaku"><%=i18n.get(GL.COMMON_SHOW)%><BR>
							</TD>
							<TD style="border:0px;width:9%;" class="semaku">
								<html:select property="view" onchange="doSubmit('show')" style="width:70">
									<html:optionsCollection name="HikiateSyoninForm" property="ar_show" value="value" label="key" />
								</html:select><BR>
							</TD>	
							<%-- ←前のXX件 --%>
							<TD  align="right" style="border:0px;width:13%;">
								<logic:notEqual name="HikiateSyoninForm" property="x" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="HikiateSyoninForm" property="x" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('prevX')"><bean:write name="HikiateSyoninForm" property="xen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<%-- 次のXX件→ --%>
							<TD  align="right" style="border:0px;width:15%;">
								<logic:notEqual name="HikiateSyoninForm" property="y" value="">
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="HikiateSyoninForm" property="y" /></a>
									<%} else {%>
										<a href="#" onClick="doSubmit('nextY')"><bean:write name="HikiateSyoninForm" property="yen" /></a>
									<%}%>
								</logic:notEqual>
							</TD>
							<%-- XX/YY件 --%>
							<TD style="border:0px;width:13%;text-align:right;" class="semaku">
								<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
							</TD>
						</TR>
						
						<TR>
						<TD colspan="4" style="border:0px;" class="semaku"><br>
						</TD>
						<TD colspan="2" style="border:0px;" class="semaku">
							<%-- 一括承認 --%>
							<DIV class="right"><%=i18n.get(GL.OD1103_IKKATUSYONIN)%>&nbsp;
								<input type="checkbox" name="check_all" id="chkAll" value="true" onclick="selectAll(this.form)"/>
							</DIV>
						</TD>
						</TR>
					</TABLE>
				
				<TABLE style="width:100%;" border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
					<THEAD>
					<TR style="width:100%;">
						<%-- 勘定先CD --%>
						<TH colspan=2 style="width:10%;"><%=i18n.get(GL.OD1103_KANJO_CD)%></TH>
						<%-- 勘定先名称 --%>
						<TH colspan=7 style="width:35%;"><%=i18n.get(GL.OD1103_KANJO_NM)%></TH>
						<%-- 取引先区分 --%>
						<TH colspan=5 style="width:20%;"><%=i18n.get(GL.OD1103_TORIHIKISAKI_KBN)%></TH>
						<%-- 債権区分 --%>
						<TH colspan=5 style="width:20%;"><%=i18n.get(GL.OD1103_SAIKEN_KBN)%></TH>
						<%-- 承認 --%>
						<TH colspan=1 rowspan="2" style="width:5%;text-align: center;"class="borderRight"><%=i18n.get(GL.OD1103_SYONIN)%></TH>
					</TR>
					<TR>
						<%-- 汎用１ --%>
						<TH colspan=1 style="width:5%;"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
						<%-- 組織 --%>
						<TH colspan=6 style="width:30%;"><%=i18n.get(GL.OD1103_SOSHIKI)%></TH>
						<%-- 補正後引当金額--%>
							<% if(SESSION_DATA_APP.getUser_bean().getComHikiatekin_kensyo_s_flg().equals(GS.ON)){ %>
								<%-- 国内 --%>
								<TH colspan=4 style="width:20%;"><%=i18n.get(GL.OD1103_HIKIATE_HOSEIGAKU_KOKUNAI)%></TH>
							<% }else{ %>
								<%-- 海外  --%>
								<TH colspan=4 style="width:20%;"><%=i18n.get(GL.OD1103_HIKIATE_HOSEIGAKU_KAIGAI)%></TH>
							<% }%>
						<%-- 担当者 --%>
						<TH colspan=5 style="width:25%;"><%=i18n.get(GL.OD1103_TANTO_NM)%></TH>
						<%-- 対象年月 --%>
						<TH colspan=3 style="width:15%;"><%=i18n.get(GL.OD1103_TAISYO_YM)%></TH>
					</TR>
					</THEAD>
					<TBODY>
						<% if(HikiateSyoninForm.getList() != null) { %>
							<nested:iterate name="HikiateSyoninForm" property="list" indexId="idx">
								<%-- 第1/3四半期の場合 --%>
								<nested:equal property="hanki_sihanki_kbn" value="2">
									<%-- 引当金確認登録時に取引先区分・債権区分が変更された場合は、ピンク色で表示する --%>
									<nested:equal property="differ_flg" value="false">
										<TR style="background-color:#FFC1E0">
											<TD colspan=2>
												<%-- 勘定先CD --%>
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="detail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<%-- 勘定先名称 --%>
											<TD colspan=7 >
												<nested:write property="kanjo_nm" />&nbsp;
											</TD>
											<%-- 取引先区分 --%>
											<TD colspan=5>
												<nested:write property="tori_kbn_nm" />&nbsp;
											</TD>
											<%-- 債権区分 --%>
											<TD colspan=5>
												<nested:write property="sai_kbn_nm" />&nbsp;
											</TD>
											<%-- 承認 --%>
											<TD colspan=1 rowspan="2" class="center" style="border-bottom-color:#000000;border-right-color:#000000;">
												<nested:checkbox property="syonin_chk" value="1"/>
												
											</TD>
										</TR>
										<TR style="background-color:#FFC1E0">
											<%-- 汎用１ --%>
											<TD colspan=1 style="border-bottom-color:#000000;">
												<nested:write property="sateikaisya_cd" />&nbsp;
											</TD>
											<TD colspan=6 style="border-bottom-color:#000000;">
												<nested:write property="soshiki" />&nbsp;
											</TD>
											<%-- 追加引当金額 --%>
											<TD colspan=4 style="border-bottom-color:#000000;" class="right">
												<nested:write property="kingaku" />&nbsp;<nested:write property="tuuka_cd" />
											</TD>
											<TD colspan=5 style="border-bottom-color:#000000;">
												<nested:write property="tanto_nm" />&nbsp;
											</TD>
											<TD colspan=3 style="border-bottom-color:#000000;" class="right">
												<nested:write property="taisyo_ym_hyoji" />&nbsp;
											</TD>
										</TR>
									</nested:equal>
									<nested:equal property="differ_flg" value="true">
										<TR style="background-color:#CCFFFF">
											<TD colspan=2>
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="detail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<TD colspan=7 >
												<nested:write property="kanjo_nm" />&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="tori_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="sai_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=1 rowspan="2" class="center" style="border-bottom-color:#000000;border-right-color:#000000;">
												<nested:checkbox property="syonin_chk" value="1"/>
												
											</TD>
										</TR>
										<TR style="background-color:#CCFFFF">
											<TD colspan=1 style="border-bottom-color:#000000;">
												<nested:write property="sateikaisya_cd" />&nbsp;
											</TD>
											<TD colspan=6 style="border-bottom-color:#000000;">
												<nested:write property="soshiki" />&nbsp;
											</TD>
											<TD colspan=4 style="border-bottom-color:#000000;" class="right">
												<nested:write property="kingaku" />&nbsp;<nested:write property="tuuka_cd" />
											</TD>
											<TD colspan=5 style="border-bottom-color:#000000;">
												<nested:write property="tanto_nm" />&nbsp;
											</TD>
											<TD colspan=3 style="border-bottom-color:#000000;" class="right">
												<nested:write property="taisyo_ym_hyoji" />&nbsp;
											</TD>
										</TR>
									</nested:equal>
								</nested:equal>
								<%-- 第1/3四半期の場合ではない --%>
								<nested:notEqual property="hanki_sihanki_kbn" value="2">
									<%-- 引当金確認登録時に取引先区分・債権区分が変更された場合は、ピンク色で表示する --%>
									<nested:equal property="differ_flg" value="false">
										<TR style="background-color:#FFC1E0">
											<TD colspan=2>
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="detail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<TD colspan=7 >
												<nested:write property="kanjo_nm" />&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="tori_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="sai_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=1 rowspan="2" class="center" style="border-bottom-color:#000000;border-right-color:#000000;">
												<nested:checkbox property="syonin_chk" value="1"/>
											</TD>
										</TR>
										<TR style="background-color:#FFC1E0">
											<TD colspan=1 style="border-bottom-color:#000000;">
												<nested:write property="sateikaisya_cd" />&nbsp;
											</TD>
											<TD colspan=6 style="border-bottom-color:#000000;">
												<nested:write property="soshiki" />&nbsp;
											</TD>
											<TD colspan=4 style="border-bottom-color:#000000;" class="right">
												<nested:write property="kingaku" />&nbsp;<nested:write property="tuuka_cd" />
											</TD>
											<TD colspan=5 style="border-bottom-color:#000000;">
												<nested:write property="tanto_nm" />&nbsp;
											</TD>
											<TD colspan=3 style="border-bottom-color:#000000;" class="right">
												<nested:write property="taisyo_ym_hyoji" />&nbsp;
											</TD>
										</TR>
									</nested:equal>
									<nested:equal property="differ_flg" value="true">
										<TR>
											<TD colspan=2>
												<nested:equal property="link_flg" value="true">
													<a href="#" onClick="detail('<nested:write property="id" />')"><nested:write property="kanjo_cd" /></a>
												</nested:equal>
												<nested:equal property="link_flg" value="false"><nested:write property="kanjo_cd" /></nested:equal>&nbsp;
											</TD>
											<TD colspan=7 >
												<nested:write property="kanjo_nm" />&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="tori_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=5>
												<nested:write property="sai_kbn_nm"/>&nbsp;
											</TD>
											<TD colspan=1 rowspan="2" class="center" style="border-bottom-color:#000000;border-right-color:#000000;">
												<nested:checkbox property="syonin_chk" value="1"/>
												
											</TD>
										</TR>
										<TR>
											<TD colspan=1 style="border-bottom-color:#000000;">
												<nested:write property="sateikaisya_cd" />&nbsp;
											</TD>
											<TD colspan=6 style="border-bottom-color:#000000;">
												<nested:write property="soshiki" />&nbsp;
											</TD>
											<TD colspan=4 style="border-bottom-color:#000000;" class="right">
												<nested:write property="kingaku" />&nbsp;<nested:write property="tuuka_cd" />
											</TD>
											<TD colspan=5 style="border-bottom-color:#000000;">
												<nested:write property="tanto_nm" />&nbsp;
											</TD>
											<TD colspan=3 style="border-bottom-color:#000000;" class="right">
												<nested:write property="taisyo_ym_hyoji" />&nbsp;
											</TD>
										</TR>
									</nested:equal>
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