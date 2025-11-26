<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="TensouForm" name="05TensouForm" type="app.common.form.TensouForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<% 
	String focus = "";
	if (request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
		focus = "hanyou1";
	} else {
		focus = (String)request.getAttribute(GS.FOCUS_FIELD);
	}

	String Sateikaisya_cd = SESSION_DATA_APP.getTori_bean().getSateikaisya_cd();
	
%>

<script>
	function cancelEnter() { 
		if (event.keyCode == 13)
				event.returnValue = false;
	}
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
    		form.elements[val].focus();
    	}
	}
	function AddTanto() {
		form = document.forms[0];
		var n = form.elements["tanto"].selectedIndex;
		if(n > -1) {
			form.elements["txtTanto"].value = form.elements["tanto"].options[n].text;
			form.elements["selectedTantoId"].value = form.elements["tanto"].options[n].value;
		}
	}
	function doInputSearch(txtSrh) {
		var selTanto;
		var lstTantoV;
		var lstTantoL;

		selTanto = document.forms[0].elements["tanto"];
		lstTantoV = document.getElementsByName("tantoV");
		lstTantoL = document.getElementsByName("tantoL");

		selTanto.options.length = 0; 
		for(var i=0; i<lstTantoL.length; i++) {
			if (txtSrh.value == "" || lstTantoL[i].value.indexOf(txtSrh.value) == 0) {
				var newItem = new Option(lstTantoL[i].value, lstTantoV[i].value);
				selTanto.options.add(newItem);
				
			}
		}
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
			<H1 class="title01"><%=i18n.get(GL.TITLE_OZ3101)%></H1>
			<DIV id="submenu">
				<input type="button" value="<%=i18n.get(GL.BTN_TRANSFERMATION)%>" onclick="doSubmit('tensou')">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
			</DIV>

			<DIV id="list">
				<html:form action="/common/tensou" focus="<%=focus%>">
					<html:hidden name="TensouForm" property="selectedTantoId"/>
					<DIV class="headlist">
						<%--勘定先ＣＤ--%>
						<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.OZ3101_KANJO_CD)%></DIV>
						<DIV class="ReadOnlybox" style="width:10%"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
						&nbsp;&nbsp;&nbsp;
						<%--勘定先名称--%>
						<DIV class="dottitle" style="width:10%"><%=i18n.get(GL.OZ3101_KANJO_NM)%></DIV>
						<DIV class="ReadOnlybox" style="width:66%;word-break:break-all;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV>
						<br><br>
						<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
							<nested:equal property="hanyouHyoujiFlg" value="true">
							<TR>
								<%--汎用２(左)--%>
								<TD width="10%" style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:6px;"><%=SESSION_DATA_APP.getLbl_nm9()%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="ReadOnlybox" style="width:8%;margin-bottom:6px;margin-left:10px;"><bean:write name="TorihikisakiBean" property="sateikaisya_cd" /></DIV>
								<%--汎用２タイトル--%>
								<DIV class="dottitle" style="margin-bottom:5px;">&nbsp;&nbsp;&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm3()%></DIV>
								<%--汎用２(右)セレクトボックス--%>
								<DIV class="box" style="margin-bottom:2px;"><html:select property="hanyou1" onchange="doSubmit('hanyo1')">
								<html:optionsCollection name="TensouForm" property="ar_Hanyou1" value="value" label="key" />
									</html:select></DIV>
								<% if(Sateikaisya_cd.equals("SJ")){%>
								<%--汎用３タイトル--%>
								<DIV class="dottitle" style="margin-bottom:5px;">&nbsp;&nbsp;&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm10()%></DIV>
								<%--汎用３セレクトボックス--%>
								<DIV class="box" style="margin-bottom:2px;"><html:select property="hanyou2" onchange="doSubmit('hanyo2')">
								<html:optionsCollection name="TensouForm" property="ar_Hanyou2" value="value" label="key" />
									</html:select></DIV>
								<%}%>
									</TD>
							</TR>
							</nested:equal>
							<TR>
								<%-- 入力／検索 --%>
								<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OZ3101_NYURYOKUKENSAKU)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
									<DIV class="box" style="margin-bottom:2px;">
										<nested:text property="inTanto" style="width:60%;" onkeyup="doInputSearch(this)" />
									</DIV>
								</TD>
							</TR>
							<TR>
								<%-- 担当者選択 --%>
								<TD width="7%"><DIV class="dottitle"><%=i18n.get(GL.OZ3101_TANTO_SENTAKU)%></DIV></TD>
								<%-- 担当者一覧 --%>
								<TD>
									<DIV style="float:left;margin-left:5px;width:35%;">
										<DIV class="boxtitle"><%=i18n.get(GL.OZ3101_TANTO_ICHIRAN)%></DIV>
										<html:select name="TensouForm" property="tanto" size="8" ondblclick="AddTanto()" style="width:100%;">
											<html:optionsCollection name="TensouForm" property="ar_Tanto" value="togo_id" label="email_addr"/>
										</html:select>
										<logic:iterate id="tanto" name="TensouForm" property="ar_Tanto">
											<input type="hidden" name="tantoV" value="<bean:write name='tanto' property='togo_id'/>"/>
											<input type="hidden" name="tantoL" value="<bean:write name='tanto' property='email_addr'/>"/>
										</logic:iterate>
									</DIV> 
									<html:button property="addTanto" style="float:left;background:#CCCCCC;margin:20px 10px;" onclick="AddTanto()">
										&nbsp;&gt;&nbsp;
									</html:button>
									<%-- 担当者 --%>
					  				<DIV style="float:left;width:35%;">
					  					<DIV class="boxtitle"><%=i18n.get(GL.OZ3101_TANTO)%></DIV>
					  					<html:text name="TensouForm" readonly="true" property="txtTanto" style="width:100%;background-color: #F8F8FF;border: solid 1px #AAA;" />
			  						</DIV>
					  			</TD>
							</TR>
						</TABLE>
					</DIV>
					<DIV class="mainlist">
						<%--転送コメント--%>
						<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OZ3101_COMMENT)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
						<DIV class="box" style="width:100%;">
							<html:textarea property="comment" rows="5" style="width:100%"/>
						</DIV>
					</DIV>
				</html:form>
			</DIV>
		</DIV>
	</DIV>
</CENTER>
</BODY>
</HTML>