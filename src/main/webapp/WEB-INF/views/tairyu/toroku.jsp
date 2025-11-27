<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<script>
	function tairyu(obj, id) {
		if(blockSubmit()==false){
			return;
		}
		<%--課題No.172--%>
		<%--追加開始--%>
		subClose();
		<%--追加完了--%>
		form = document.forms[0];
		form.elements["id"].value = id;
		var tairyu = obj.value;
		if (tairyu == "2" || tairyu == "3" || tairyu == "4" || tairyu == "5" || tairyu == "6") {
			form.elements["tairyuhantei"].value = tairyu;
			action = form.action;
			form.target = "_top";
			form.action += "?event=" + "tairyu_hantei";
			form.submit();
			form.action = action;
		}
		resetBlockSubmit();
	}
	
	function setExTairyu(obj) {
		form = document.forms[0];
		form.elements["ex_tairyuhantei"].value = obj.value;
	}
</script>

<bean:define id="TorokuForm" name="01TorokuForm" type="app.tairyu.form.TorokuForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="UserBean" name="app.SessionData" property="user_bean" type="app.UserBean" />
<% Pager pager = TorokuForm.getPager(); %>
</HEAD>
<BODY>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB1102)%></H1>
		<html:form action="/tairyu/toroku"
					method="POST" 
					enctype="multipart/form-data">
		
		<%-- 画面遷移の引数 --%>
		<html:hidden property="anken_no" />
		<html:hidden property="id" />
		<html:hidden property="tairyuhantei" />
		<html:hidden property="ex_tairyuhantei" />
		
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('kaijyo')"> <%-- もぎ取り解除 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('tensou')"> <%-- 転送 --%>
			<nested:equal property="flg_disp_sashimodoshi" value="<%= GS.ON %>">
				<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashi')"> <%-- 差戻 --%>
			</nested:equal>
			<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('save')"> <%-- 一時保存 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('regist')"> <%-- 登録 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')"> <%-- ダウンロード --%>
			<%-- 課題No.06 国内一括取込対応 --%>
			<%-- 修正開始 --%>
			<%-- <nested:equal property="flg_disp_upload_all" value="<%= GS.ON %>"> --%>
				<input type="button" value="<%=i18n.get(GL.BTN_UPLOAD_ALL)%>" onclick="doSubmitNonTorikomi()"> <%-- 一括取り込み --%>
			<%-- </nested:equal> --%>
			<%-- 修正完了 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="backConfirm('back')"> <%-- 戻る --%>
			<br>
			<div class="under">
				<nested:equal property="flg_disp_comment" value="<%= GS.ON %>">
					<a href="#" style="color:#FF0000;" onClick="doSubmit('comment')"><%=i18n.get(GL.LINK_OZ4101)%></a> <%-- コメント表示 --%>
				</nested:equal>
			</div>
		</DIV>
		<DIV id="list">			
			<DIV class="headlist">
				<%-- 汎用１ --%>
				<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV>
				<DIV class="ReadOnlybox" style="width=5%; margin-bottom:2px;">
					<bean:write name="TorihikisakiBean" property="sateikaisya_cd"/>
				</DIV>&nbsp;&nbsp;&nbsp;
				<%-- 組織 --%>
				<DIV class="dottitle" style="width=5%; margin-bottom:2px;"><%=i18n.get(GL.OB1102_SOSIKI)%></DIV>
				<DIV class="ReadOnlybox" style="width=80%; margin-bottom:2px;">
					<bean:write name="TorihikisakiBean" property="soshiki"/>
				</DIV>
				<br>
				<%-- 勘定先CD --%>
				<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1102_KANJYOSAKICD)%></DIV>
				<DIV class="ReadOnlybox" style="width=9%; margin-top:2px;">
					<bean:write name="TorihikisakiBean" property="kanjo_cd"/>
				</DIV>&nbsp;&nbsp;&nbsp;
				<%-- 勘定先名称 --%>
				<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1102_KANJYOSAKINAME)%></DIV>
				<DIV class="ReadOnlybox" style="width=68%; margin-top:2px;">
					<bean:write name="TorihikisakiBean" property="kanjo_nm"/>
				</DIV>
				<br>
				<%-- 信用格付 --%>
				<DIV class="dottitle" style="width=9%; margin-top:2px;"><%=i18n.get(GL.OB1102_SHINYOKAKUDUKE)%></DIV>
				<DIV class="ReadOnlybox" style="width=5%; margin-top:2px;text-align: center;">
					<bean:write name="TorihikisakiBean" property="sinyoktk"/>
				</DIV>
			</DIV>

			<DIV class="mainlist">
				<DIV class="XYbox">
				<table style="border:0px;width:100%;" class="semaku">
					<tr style="border:0px;width:100%;" class="semaku">
						<%-- 承認担当者セレクトボックス --%>
						<td style="border:0px; width:45%; text-align:right;" class="semaku">
							<nested:equal property="flg_disp_tanto" value="<%= GS.ON %>">
								<%=i18n.get(GL.OB1102_SHONINTANTOSHA)%>
								<html:select property="syonin_tanto" style="width:250">
									<html:option value=""></html:option>
									<html:optionsCollection name="TorokuForm" property="ar_tanto" value="key" label="value" />
								</html:select>
							</nested:equal>
						</td>
						
						<%-- 表示件数セレクトボックス --%>
						<td style="border:0px; width:19%;" class="semaku">
							<%=i18n.get(GL.COMMON_SHOW)%>
							<html:select property="view" onchange="doSubmit('show')" style="width:70">
								<html:optionsCollection name="TorokuForm" property="ar_show" value="value" label="key" />
							</html:select>
						</td>

						<%-- ←前のXX件 --%>
						<TD style="border:0px;width:13%;">
							<logic:notEqual name="TorokuForm" property="x" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="TorokuForm" property="x" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('prevX')"><bean:write name="TorokuForm" property="xen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>

						<%-- 次のXX件→ --%>
						<TD style="border:0px;width:13%;">
							<logic:notEqual name="TorokuForm" property="y" value="">
								<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="TorokuForm" property="y" /></a>
								<%} else {%>
									<a href="#" onClick="doSubmit('nextY')"><bean:write name="TorokuForm" property="yen" /></a>
								<%}%>
							</logic:notEqual>
						</TD>

						<%-- XX/YY件 --%>
						<td style="border:0px; width:10%; text-align:right;" class="semaku">
							<%=pager.getLastIndexOfCurrentPage()%><%=i18n.get(GL.COMMON_SLASH)%><%=pager.getListSize()%>&nbsp;<%=i18n.get(GL.COMMON_DATA)%>
						</td>
					</TR>
				</table>
				</DIV>
		
				<TABLE style="border-left-color: #AAA;" border=0 cellSpacing=0 cellPadding=0>
					<THEAD>
						<TR>
							<TH width="5%">&nbsp;</TH>
							<%-- 枝番 --%>
							<TH width="4%"><%=i18n.get(GL.OB1102_EDABAN)%></TH>
							<%-- セル --%>
							<TH width="20%"><%=i18n.get(GL.OB1102_CELL)%></TH>
							<%-- 勘定科目 --%>
							<TH width="18%"><%=i18n.get(GL.OB1102_KANJYOKAMOKU)%></TH>
							<%-- 収支予定日/満期日/勘定処理日 --%>
							<TH width="14%"><%=i18n.get(GL.OB1102_SYUSIYOTEIBI)%><BR/>
								<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
									<%=i18n.get(GL.OB1102_MANKIBI)%><BR/>
								</logic:equal>
								<%=i18n.get(GL.OB1102_KANJYOSHORIBI)%></TH>
							<%-- 契約伝票No./インボイスNo. --%>
							<TH width="12%"><%=i18n.get(GL.OB1102_KEIYAKUDENPYONO)%><BR/><%=i18n.get(GL.OB1102_INVOICENO)%></TH>
							<%-- 金額計(USD) --%>
							<TH width="12%"><%=i18n.get(GL.OB1102_KINGAKUKEI)%><bean:write name="TorokuForm" property="tuuka_cd"/></TH>
							<%-- 滞留判定 --%>
							<TH width="10%"><%=i18n.get(GL.OB1102_TAIRYUHANTEI)%></TH>
						</TR>
					</THEAD>
					<TBODY>
						<% if(TorokuForm.getList() != null) { %>
							<nested:iterate name="TorokuForm" property="list" indexId="idx">
								<TR>
									<%-- 詳細リンク --%>
									<TD>
										<a href="#" onClick="syosai('syosai', '<bean:write name="TorokuForm" property="anken_no" />', '<nested:write property="id" />')">
											<%=i18n.get(GL.OB1102_SHOSAI)%>
										</a>
									</TD>
									<TD class="right"><nested:write property="anken_no_eda" />&nbsp;</TD>
									<TD><nested:write property="cell_nm" />&nbsp;</TD>
									<TD><nested:write property="kanjo_kamoku_nm" />&nbsp;</TD>
									<TD>
										<nested:notEmpty property="syusi_yoteibi">
											<nested:write property="syusi_yoteibi" />&nbsp;<BR/>
										</nested:notEmpty>
										<nested:empty property="syusi_yoteibi">
											<%=i18n.get(GL.COMMON_HAIHUN)%>&nbsp;<BR/>
										</nested:empty>
										<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
											<nested:notEmpty property="mankibi">
												<nested:write property="mankibi" />&nbsp;<BR/>
											</nested:notEmpty>
											<nested:empty property="mankibi">
												<%=i18n.get(GL.COMMON_HAIHUN)%>&nbsp;<BR/>
											</nested:empty>
										</logic:equal>
										<nested:notEmpty property="kanjo_syoribi">
											<nested:write property="kanjo_syoribi" />&nbsp;
										</nested:notEmpty>
										<nested:empty property="kanjo_syoribi">
											<%=i18n.get(GL.COMMON_HAIHUN)%>&nbsp;
										</nested:empty>
									</TD>
									<TD>
										<nested:notEmpty property="keiyaku_denpyo_no">
											<nested:write property="keiyaku_denpyo_no" />&nbsp;<BR/>
										</nested:notEmpty>
										<nested:empty property="keiyaku_denpyo_no">
											<%=i18n.get(GL.COMMON_HAIHUN)%>&nbsp;<BR/>
										</nested:empty>
										<nested:notEmpty property="invoice_no">
											<nested:write property="invoice_no" />&nbsp;
										</nested:notEmpty>
										<nested:empty property="invoice_no">
											<%=i18n.get(GL.COMMON_HAIHUN)%>&nbsp;
										</nested:empty>
									</TD>
									<TD class="right"><nested:write property="kingaku_kei" />&nbsp;</TD>
									<TD>
										<%-- 滞留判定セレクトボックス --%>
										<select name="list[<bean:write name='idx'/>].tairyu_hantei" value="<nested:write property='tairyu_hantei'/>" onchange="tairyu(this, <nested:write property='id' />)" onfocus="setExTairyu(this)" >
											<option value =""></option>
											<logic:iterate id="tairyu_jdg" name="TorokuForm" property="ar_tairyu_jdg" indexId="idx2">
												<bean:define id="tairyuJdgVal" name="tairyu_jdg" property="value" type="java.lang.String" />
												<logic:equal name="list" property="tairyu_hantei" value="<%=tairyuJdgVal%>">
													<option value ="<bean:write name='tairyu_jdg' property='value'/>" selected="selected">
														<bean:write name="tairyu_jdg" property="key"/>
													</option>
												</logic:equal>
												<logic:notEqual name="list" property="tairyu_hantei" value="<%=tairyuJdgVal%>">
													<option value ="<bean:write name='tairyu_jdg' property='value'/>">
														<bean:write name="tairyu_jdg" property="key"/>
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
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
<script type="text/javascript" language="JavaScript">
  <!--
  var focusControl = document.forms["01TorokuForm"].elements["syonin_tanto"];
  if (focusControl == undefined) {
      focusControl = document.forms["01TorokuForm"].elements["view"];
  }
  if (focusControl.type != "hidden" && !focusControl.disabled) {
      focusControl.focus();
  }
  // -->
</script>
</BODY>
</HTML>