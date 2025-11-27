<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="UserBean" name="app.SessionData" property="user_bean" type="app.UserBean" />
<bean:define id="UserMaintenanceBean" name="app.SessionData" property="user_maintenance_bean" type="app.UserMaintenanceBean" />
<bean:define id="UserTorokuForm" name="07UserTorokuForm" type="app.system.form.UserTorokuForm" />
<% 
String focus = GS.EMPTY_CHARCTER;
if (request.getAttribute(GS.FOCUS_FIELD) == null || GS.EMPTY_CHARCTER.equals(request.getAttribute(GS.FOCUS_FIELD))) {
	focus = "";
} else {
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
 %>
<% Pager pager = UserTorokuForm.getPager(); %>
<script language="JavaScript">

	<%-- 課題No.230 確認メッセージ追加 --%>
	function confirmLogoff() {
		if('<bean:write name="07UserTorokuForm" property="user_id_flg" />' == 'true'){
			if(window.confirm('<%=i18n.get(GL.CONFIRM_LOGOFF)%>')){
				doSubmit('toroku');
			}
		}else{
			doSubmit('toroku');
		}
	}

	function delBtn_click(idx,event) {
		form = document.forms[0];
		form.elements["gyoumu_huro_index"].value = idx;
<%-- 課題No.145 Submitチェック追加 --%>
		doSubmit(event);
<%--
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
--%>
	}

	function default_chick(id) {
		form = document.forms[0];
		form.elements["rdo_status"].value = "1";
		form.elements["gyoumu_itiran_id"].value = id;
	}
	
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
			form.elements[val].focus();
		}
		form = document.forms[0];
	}	

	<%-- メール配信先選択 --%>
	function showMailSentaku(){
		ans = showModalDialog("./mailsoushin_sentaku.do?event=appExecute",window,"dialogWidth:800px;dialogHeight:600px;resizable:yes;scroll:no;");
		if(ans == 1){
			doSubmit('reMeilSentaku');
		}
	}
	function delHaishinsaki(no){
		form.elements["haishinDel"].value = no;
		doSubmit('delHaishinsaki');
	}
	
	<%-- 担当組織選択 --%>
	function showTantoSentaku(){
		ans = showModalDialog("./tantososhiki_sentaku.do?event=appExecute",window,"dialogWidth:800px;dialogHeight:600px;resizable:yes;scroll:no;");
		if(ans == 1){
			doSubmit('reTantoSoshiki');
		}
	}

</script>
</HEAD>
<BODY onload="setFocus('<%=focus%>')">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>



	<DIV id="menu">	<%--メニューリンク部分--%>
		<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
		</DIV>
	</DIV>
	
</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7107)%></H1>

		<DIV id="submenu">
			<%-- 課題No.230 確認メッセージ追加対応 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="confirmLogoff()">
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		</DIV>


		<DIV id="list">
		
		<html:form action="/system/usertoroku" >

		<html:hidden property="gyoumu_huro_index"/>
		<html:hidden property="id" />
		<html:hidden property="chk_handan_flg" />
		<html:hidden property="rdo_status" />
		<html:hidden property="gyoumu_itiran_id" />
		<html:hidden property="clk_hanyou1_cd" />
		<html:hidden property="haishinHyoujiId" />
		<html:hidden property="haishinDel" />
		
			<DIV class="headerlist">

				<TABLE>
					<TR>
						
						<TD width="15%"><DIV style="width:100%;"><%=i18n.get(GL.OS7107_USER_ID)%></DIV></TD>
						<TD width="35%"><DIV class="ReadOnlybox" style="width:80%"><bean:write name="UserTorokuForm" property="user_id"/></DIV></TD>
						<TD width="17%"><DIV style="width:100%;"><%=i18n.get(GL.OS7107_USER_NM)%></DIV></TD>
						<TD width="33%"><DIV class="ReadOnlybox" style="width:100%"><bean:write name="UserTorokuForm" property="user_nm"/></DIV></TD>
					</TR>
					<TR>
						
						<TD width="15%"><DIV style="width:100%"><%=i18n.get(GL.OS7107_KAISYA_NM)%></DIV></TD>
						<TD width="35%"><DIV class="ReadOnlybox" style="width:220"><bean:write name="UserTorokuForm" property="company_nm"/></DIV></TD>
						<TD width="17%"><DIV style="width:100%"><%=i18n.get(GL.OS7107_SYOZOKUSOSHIKI_NM)%></DIV></TD>
						<TD width="33%"><DIV class="ReadOnlybox" style="width:260"><bean:write name="UserTorokuForm" property="soshiki_nm"/></DIV></TD>
					</TR>
					<TR>
						<TD ><DIV class="dottitle" style="width:100%"><%=i18n.get(GL.OS7107_MAIL_ADDR)%></DIV></TD>
						<TD colspan="2">
							<DIV class="ReadOnlybox" style="width:300"><bean:write name="UserTorokuForm" property="email_addr"/></DIV>
						</TD>
					</TR>
					
					<logic:equal name="UserBean" property="comSystemManager_flg" value="1">
						<TR>
							<TD width="15%"><%=i18n.get(GL.OS7107_ADMIN_FLG)%></TD>
							<TD width="35%">
								<DIV class="leftbox">
									<nested:checkbox property="admin_kanri_flg" value="1" onclick="doSubmit('admin_kanri')"/>
								</DIV>
							</TD>
							<TD>&nbsp;</TD>
						</TR>
					</logic:equal>
					<TR>
						<TD width="15%"><%=i18n.get(GL.OS7107_MAIL_HAISHIN)%></TD>
						<TD width="35%">
							<nested:checkbox property="mail_haisin_kbn" value="1"/>
						</TD>
						<TD>&nbsp;</TD>
						<TD>&nbsp;</TD>
					</TR>
					<TR>
						<TD width="15%"><br><%=i18n.get(GL.OS7107_PRINTOUT_DEFAULT_LANG_KBN)%></TD>
						<TD width="35%">
							<DIV class="leftbox">
								<nested:radio property="printout_default_lang_kbn" value="1"><span><DIV style="margin-top:3px;"><%=i18n.get(GL.OS7107_LANG_JP)%></DIV></span></nested:radio>
								<nested:radio property="printout_default_lang_kbn" value="2"><span><DIV style="margin-top:3px;"><%=i18n.get(GL.OS7107_LANG_EN)%></DIV></span></nested:radio>
								<nested:radio property="printout_default_lang_kbn" value="3"><span><DIV style="margin-top:3px;"><%=i18n.get(GL.OS7107_LANG_CHOICE)%></DIV></span></nested:radio>
							</DIV>
						</TD>
						<TD>&nbsp;</TD>
					</TR>			
				</TABLE>
		
				<BR/>
				
				<TABLE>
				
					<TR>
						<TD>
							<H1 class="title01"><%=i18n.get(GL.OS7107_TITLE_GYOUMU_HURO)%></H1>
						</TD>
					</TR>
					<TR>
						<TD>

							<TABLE>
								<TR>
									<TD style="width:15%"><%=i18n.get(GL.OS7107_GYOUMU_HURO)%></TD>
<%-- 課題No.113 業務フローパターン(プルダウン)文字切れ対応 --%>
<%-- 追加開始 --%>
<%--								<TD style="width:28%"><html:select property="gyoumu_huro" style="width:218">--%>
									<TD style="width:35%"><html:select property="gyoumu_huro" >
<%-- 追加完了 --%>
									<html:optionsCollection name="UserTorokuForm" property="ar_gyoumu_huro" value="value" label="key" /></html:select>
									</TD>
									<TD  style="width:5%" rowspan="2" align="center" valign="bottom">
										<input type="button" value="<%=i18n.get(GL.BTN_ADD)%>" style="background:#CCCCCC;" onclick="doSubmit('tuika')">
									</TD>
									<TD></TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>			
			</DIV>
			

			<TABLE style="width:100%;border-top-style:none" cellSpacing=0 cellPadding=0>
				<TR>
					<TH style="width:8%;text-align:center;"><%=i18n.get(GL.OS7107_DEFAULT_FLG)%></TH>
<%-- 課題No.113 業務フローパターン(プルダウン)文字切れ対応 --%>
<%-- 追加開始 --%>
<%--					<TH style="width:34%;text-align:left;"><%=i18n.get(GL.OS7107_PATTERN_NAME_JP)%></TH>--%>
					<TH style="width:45%;text-align:left;"><%=i18n.get(GL.OS7107_PATTERN_NAME_JP)%></TH>
<%-- 追加完了 --%>
				</TR>
				<nested:iterate name="UserTorokuForm" property="ar_gyoumu_huro_itiran" indexId="idx">
					<bean:define id="vIdx" name="ar_gyoumu_huro_itiran" property="id" type="java.lang.String" />
					<TR>
						<TD style="text-align:center;">
							<bean:define id="vid" name="ar_gyoumu_huro_itiran" property="id" type="java.lang.String" />
							<html:radio name="UserTorokuForm" property="default_flg_checked" value="<%= vid %>"/>
						</TD>
						<TD><nested:write property="pattern_name_jp" /></TD>
						<TD width="40px" style="border:none;text-align:center;">
							<logic:equal name="ar_gyoumu_huro_itiran" property="delete_btn_flg" value="1">
								<input type="button" value="<%=i18n.get(GL.BTN_DELETE)%>" onclick="delBtn_click('<%=vIdx%>','delete')" style="background:#CCCCCC;">								
							</logic:equal>
						</TD>
					</TR>
				</nested:iterate>
				<nested:empty name="ar_gyoumu_huro_itiran">
					<TR>
						<TD style="text-align:center;">								

						</TD>
						<TD></TD>
						<TD width="40px" style="border:none;text-align:center;">
							
						</TD>
					</TR>
				</nested:empty>
			</TABLE>
			
			<BR/><BR/>
						
			<%-- 一覧情報 --%>
			<DIV class="mainlist">	
				<DIV>
					<TABLE style="border:none;width:95%;">
							<TR style="border:none">
								<TD style="border:none"><H1 class="title01" style="border:none"><%=i18n.get(GL.OS7107_TITLE_TANTOSOSHIKI)%></H1></TD>
								<TD style="border:none;text-align:right;">
									<font color="#FF0000;"><%=i18n.get(GL.OS7107_TANTOCLICK)%></FONT>
									<input type="button" value="<%=i18n.get(GL.BTN_TANTOSOSHIKI)%>" style="background:#CCCCCC;" onclick="showTantoSentaku()"/>
								</TD>
							</TR>
							<TR>
								<TABLE style="width:95%;">
									<THEAD>
										<TR>
											<TH style="width:20%;" colspan="2"><p class="center"><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>
											<TH style="width:35%;" colspan="2"><p class="center"><%=SESSION_DATA_APP.getLbl_nm3()%></p></TH>
											<% if(GS.ON.equals(UserTorokuForm.getHanyou4LabelFlg())) { %>
												<TH style="width:40%;" colspan="2">
													<p class="center"><%=i18n.get(GL.OS7107_HONBU)%></p>
												</TH>
											<% } else { %>
												<TH style="width:40%;" colspan="2">&nbsp;</TH>
											<% } %>
											<TH style="width:5%;border-right-color:#000000;" rowspan="2"><p class="center"><%=i18n.get(GL.OS7107_SENTAKU)%></p></TH>
										</TR>
										<TR>
											<TH style="width:7%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
											<TH style="width:15%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
											<TH style="width:7%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
											<TH style="width:30%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
											<% if(GS.ON.equals(UserTorokuForm.getHanyou4LabelFlg())) { %>
												<TH style="width:7%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
												<TH style="width:25%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
											<% } else { %>
												<TH style="width:7%;">&nbsp;</TH>
												<TH style="width:25%;">&nbsp;</TH>
											<% } %>
										</TR>
									</THEAD>
									<TBODY>
											<% if(UserTorokuForm.getAr_meisai() != null) { %>
												<nested:iterate id="tantou_bean" name="UserTorokuForm" property="ar_meisai" >
													<TR class="semaku">
														<TD class="borderBottom"><nested:write property="hanyou1_cd" /></TD>
														<TD class="borderBottom"><nested:write property="hanyou1_nm" /></TD>
														<TD class="borderBottom"><nested:write property="hanyou2_cd" /></TD>
														<TD class="borderBottom"><nested:write property="hanyou2_nm" /></TD>
														<TD class="borderBottom"><nested:write property="hanyou4_cd" /></TD>
														<TD class="borderBottom"><nested:write property="hanyou4_nm" /></TD>
														<nested:equal name="tantou_bean" property="taisyogaiFlg" value="0">
															<TD style="text-align:center;" class="borderBottom borderRight">
																<input type="checkbox" name="tantou_bean" value="" disabled="disabled" checked="checked" />
																<html:hidden name="tantou_bean" property="taisyogaiFlg" value="0"/>
															</TD>
														</nested:equal>
														<nested:notEqual name="tantou_bean" property="taisyogaiFlg" value="0">
															<TD style="text-align:center;background-color:#ffc1e0;" class="borderBottom borderRight" >
																<input type="checkbox" name="tantou_bean" value="" disabled="disabled" checked="checked" />
																<html:hidden name="tantou_bean" property="taisyogaiFlg" value="1"/>
															</TD>
														</nested:notEqual>
													</TR>
												</nested:iterate>
											<% } %>
									</TBODY>
								</TABLE>
								<TABLE class="none" style="width:95%;" cellSpacing=0 cellPadding=0>
									<THEAD>
										<TR>
											<TD class="none" style="text-align:right;">
												<p class="right">
													<font color="#FF0000;"><%=i18n.get(GL.OS7107_TANTOPINK)%></FONT>
												</p>
											</TD>
										</TR>
									</THEAD>
								</TABLE>
							</TR>
					</TABLE>
				</DIV>
				<BR>
				<DIV style="clear:left;width:95%;">
					<% if(UserTorokuForm.getHaishinHyoujiFlg() == "1") { %>
					<%-- 業務フローに01がある場合のみ、配信先を表示 --%>
					<TABLE class="none" style="width:100%;" cellSpacing=0 cellPadding=0>
						<TR>
							<TD><H1 class="title01"><%=i18n.get(GL.OS7107_TITLE_HAISHINSAKI)%></H1></TD>
							<TD style="border:none;text-align:right;">
								<input type="button" value="<%=i18n.get(GL.BTN_SOUSHINSAKI)%>" style="background:#CCCCCC;" onclick="showMailSentaku()"/>
							</TD>
							<TD>&nbsp;</TD>
						</TR>
						<TR>
							<TABLE style="width:100%;border-left-color:#000000;border-top-style:none" cellSpacing=0 cellPadding=0  >
								<THEAD>
									<TR class="semaku">
										<TH style="width:22%;" colspan="3"><p class="center"><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>
										<TH style="width:31%;" colspan="3";><p class="center"><%=SESSION_DATA_APP.getLbl_nm3()%></p></TH>
										<TH style="width:47%;border-right-color:#000000;" colspan="3";><p class="center"><%=SESSION_DATA_APP.getLbl_nm10()%></p></TH>
										<TD style="width:3%;border:none;text-align:center;">&nbsp;</TD>
									</TR>
									<TR class="semaku">
										<TH style="width:3%;"><p class="center">&nbsp;</p></TH>
										<TH style="width:8%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
										<TH style="width:11%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
										<TH style="width:3%;"><p class="center">&nbsp;</p></TH>
										<TH style="width:9%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
										<TH style="width:12%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
										<TH style="width:10%;"><p><%=i18n.get(GL.OS7107_CODE)%></p></TH>
										<TH style="width:22%;"><p><%=i18n.get(GL.OS7107_NAME)%></p></TH>
										<TH style="width:3%;border-right-color:#000000;"><p class="center">&nbsp;</p></TH>
										<TD style="width:3%;border:none;text-align:center;">&nbsp;</TD>
									</TR>
								</THEAD>
								<TBODY>
									<nested:notEmpty property="ar_haishinsaki">
										<nested:iterate property="ar_haishinsaki" indexId="idx">
											<%
												String rId = String.valueOf(idx);
											%>
											<TR class="semaku">
												<%-- 選択（非活性） --%>
												<nested:equal property="HANYOU1FLG" value="1">
													<TD style="text-align:center;" class="borderBottom">
														<input type="checkbox" name="HANYOU1FLG" value="" disabled="disabled" checked="checked"/>
													</TD>
												</nested:equal>
												<nested:notEqual property="HANYOU1FLG" value="1">
													<TD style="text-align:center;" class="borderBottom">&nbsp;</TD>
												</nested:notEqual>
												<%-- 汎用1 --%>
												<TD class="borderBottom borderLeft"">
													<nested:write property="HANYOU1" />&nbsp;
												</TD>
												<%-- 汎用1名 --%>
												<TD class="borderBottom">
													<nested:write property="HANYOU1_NM" />&nbsp;
												</TD>
												<%-- 選択（非活性） --%>
												<nested:equal property="HANYOU2FLG" value="1">
													<TD style="text-align:center;" class="borderBottom">
														<input type="checkbox" name="HANYOU2FLG" value="" disabled="disabled" checked="checked" />
													</TD>
												</nested:equal>
												<nested:notEqual property="HANYOU2FLG" value="1">
													<TD style="text-align:center;" class="borderBottom">&nbsp;</TD>
												</nested:notEqual>
												<%-- 汎用2 --%>
												<TD class="borderBottom" >
													<nested:write property="HANYOU2" />&nbsp;
												</TD>
												<%-- 汎用2名 --%>
												<TD class="borderBottom">
													<nested:write property="HANYOU2_NM" />&nbsp;
												</TD>
												<%-- 汎用3 --%>
												<TD class="borderBottom">
													<nested:write property="HANYOU3" />&nbsp;
												</TD>
												<%-- 汎用3名 --%>
												<TD class="borderBottom">
													<nested:write property="HANYOU3_NM" />&nbsp;
												</TD>
												<%-- 選択（非活性） --%>
												<nested:equal property="HANYOU3FLG" value="1">
													<TD style="text-align:center;" class="borderBottom borderRight">
														<input type="checkbox" name="HANYOU3FLG" value="" disabled="disabled" checked="checked" />
													</TD>
												</nested:equal>
												<nested:notEqual property="HANYOU3FLG" value="1">
													<TD style="text-align:center;" class="borderBottom borderRight"">&nbsp;</TD>
												</nested:notEqual>
												<%-- 削除ボタン --%>
												<TD style="border:none;text-align:center;">
													<input type="button" value="<%=i18n.get(GL.BTN_DELETE)%>" onclick="delHaishinsaki(<%=rId%>)" style="background:#CCCCCC;">
												</TD>
											</TR>
										</nested:iterate>
									</nested:notEmpty>
								</TBODY>
							</TABLE>
						</TR>
					</TABLE>
					<% } %>
					
				</DIV>
			</DIV>
		<BR/><BR/>
	</html:form>
		
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>