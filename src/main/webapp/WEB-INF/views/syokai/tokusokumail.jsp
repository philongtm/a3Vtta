<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>
<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>

<bean:define id="TokusokumailForm" name="03TokusokumailForm" type="app.syokai.form.TokusokumailForm" />


<script language="javascript">
	// タブ操作
	function doTairyu(tab){
		document.getElementById("karento_tab").value = tab;
		doSubmit('tairyu');
	}

	// 新規担当者選択ボックス
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
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/<%=i18n.get(GL.IMG_TITLE)%>.gif' />" height="54">
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
	<%@ include file = "/menu.jspf" %>
	</DIV>

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<%-- OS6105 督促メール送信選択  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS6105)%></H1>
		<DIV id="submenu">
			<%-- 送信 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_SEND)%>" onclick="doSubmitNonTokusokuMailTiming('<%=SESSION_DATA_APP.getComLangMode()%>')">
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		</DIV>

		<DIV id="list">
			<html:form action="/syokai/tokusokumail" >
				<input type="hidden" id="send" value="<%=i18n.get(GL.CONFIRM_SEND)%>" />
				<input type="hidden" id="lastMailDate" value="<%=i18n.get(GL.CONFIRM_LASTMAILDATE)%>" />
				<input type="hidden" id="lastSaveDate" value="<%=i18n.get(GL.CONFIRM_LASTSAVEDATE)%>" />
				<input type="hidden" id="syoukakko" value="<%=i18n.get(GL.COMMON_SYOUKAKKO) %>" />
				<input type="hidden" id="syoukakkoToji" value="<%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%>" />
				<input type="hidden" id="haishinDt" value="<%= TokusokumailForm.getHaishin_Dt() %>" />
				<input type="hidden" id="hozonDt" value="<%= TokusokumailForm.getHozon_Dt() %>" />
			<nested:hidden property="karento_tab" styleId="karento_tab"/>

				<DIV align='left'>
					<%--送信部セレクトボックス--%>
					<nested:hidden property="soushinbu" styleId="soushinbu"/>
					<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
						<TR>
							<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OS6105_SOUSHINSAKI)%></DIV></TD>
							<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
								<DIV class="box" style="margin-bottom:2px;">
									<html:select property="soushinbu" onchange="doSubmit('soushinsaki')">
										<html:optionsCollection name="TokusokumailForm" property="ar_soushinbu" value="value" label="key" />
									</html:select>
								</DIV>
							</TD>
						</TR>
					</TABLE>
				</DIV>

				<DIV class="mainlist">
					<nested:equal property="karento_tab" value="2">
						<DIV id="tab">
							<%-- 査定 --%>
							<span><%=i18n.get(GL.OS6105_SATEITAB)%></span>
						</DIV>
						<DIV id="tab">
							<%-- 実質滞留債権判定 --%>
							<a href="#" onClick="doTairyu('1')"><%=i18n.get(GL.OS6105_ZISSHITUTAB)%></a>
						</DIV><br>
							<%@ include file = "./tokusoku_satei_tab.jspf" %>
					</nested:equal>
					<nested:equal property="karento_tab" value="1">
							<DIV id="tab" >
								<%-- 査定 --%>
								<a href="#" onClick="doTairyu('2')"><%=i18n.get(GL.OS6105_SATEITAB)%></a>
							</DIV>
							<DIV id="tab">
								<%-- 実質滞留債権判定 --%>
								<span><%=i18n.get(GL.OS6105_ZISSHITUTAB)%></a></span>
						</DIV><br>
							<%@ include file = "./tokusoku_tairyu_tab.jspf" %>
					</nested:equal>

					<BR><BR>
					<DIV class="headlist">
						<H1 class="title01"><%=i18n.get(GL.OS6105_SHINKITANTO)%></H1>
						<html:hidden name="TokusokumailForm" property="selectedTantoId"/>
						<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
							<TR>
								<%-- 入力／検索 --%>
								<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OS6105_NYURYOKUKENSAKU)%></DIV></TD>
								<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
									<DIV class="box" style="margin-bottom:2px;">
										<nested:text property="inTanto" style="width:60%;" onkeyup="doInputSearch(this)" />
									</DIV>
								</TD>
							</TR>
							<TR>
								<%-- 担当者選択 --%>
								<TD width="7%"><DIV class="dottitle"><%=i18n.get(GL.OS6105_TANTO_SENTAKU)%></DIV></TD>
								<%-- 担当者一覧 --%>
								<TD>
									<DIV style="float:left;margin-left:5px;width:35%;">
										<DIV class="boxtitle"><%=i18n.get(GL.OS6105_TANTO_ICHIRAN)%></DIV>
										<html:select name="TokusokumailForm" property="tanto" size="8" ondblclick="AddTanto()" style="width:100%;">
											<html:optionsCollection name="TokusokumailForm" property="ar_Tanto" value="togo_id" label="email_addr"/>
										</html:select>
										<logic:iterate id="tanto" name="TokusokumailForm" property="ar_Tanto">
											<input type="hidden" name="tantoV" value="<bean:write name='tanto' property='togo_id'/>"/>
											<input type="hidden" name="tantoL" value="<bean:write name='tanto' property='email_addr'/>"/>
										</logic:iterate>
									</DIV>
									<html:button property="addTanto" style="float:left;background:#CCCCCC;margin:20px 10px;" onclick="AddTanto()">
										&nbsp;&gt;&nbsp;
									</html:button>
									<%-- 担当者 --%>
					  				<DIV style="float:left;width:35%;">
					  					<DIV class="boxtitle"><%=i18n.get(GL.OS6105_TANTO)%></DIV>
					  					<html:text name="TokusokumailForm" readonly="true" property="txtTanto" style="width:100%;background-color: #F8F8FF;border: solid 1px #AAA;" />
			  						</DIV>
					  			</TD>
							</TR>
						</TABLE>
					</DIV>
				</DIV>
			</html:form>
		</DIV>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>