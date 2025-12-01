<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="TenpuSentakuForm" name="TenpuSentakuForm" type="app.common.form.TenpuSentakuForm" />
<% 
	ArrayList list = TenpuSentakuForm.getList();
%>

<script>
	function indexCheck(index) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["indexId"].value = index;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "download";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
	function setTenpuIndex(index) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["indexId"].value = index;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "setTenpu";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
	function setSakujoIndex(index) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		form = document.forms[0];
		form.elements["indexId"].value = index;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + "setSakujo";
		form.submit();
		form.action = action;
		resetBlockSubmit();
	}
<%-- No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御追加 --%>
	function setFocus(focus) {
		var form = document.forms[0];
		if(focus == 'null' || focus == '') {
			form.elements["fileUp"].focus();
		} else if (focus == "fileUp") {
			form.elements[focus].focus();
		} else {
			form = document.forms[1];
			form.elements[focus].focus();
		}
	}

<%-- 結合テスト障害No034対応, キーダウンキャンセル処理追加 --%>
	function keycancel() {
		event.cancelBubble = true;
		return false;
	}
</script>


</HEAD>
<%-- 結合テスト障害No034対応, 全選択を不可とする --%>
<BODY onload="setFocus('<%= request.getAttribute(GS.FOCUS_FIELD) %>')" onselectstart="return false" >
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
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
<H1 class="title01"><%=i18n.get(GL.TITLE_05_00)%></H1>

<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
</DIV>
<DIV id="list">
<%-- No459, 2008/05/30, SJA渡辺, フォーカスの設定を追加 --%>
<%-- No554, 2008/06/06, SJA遠藤, エラー時のフォーカス制御のためフォーカスの設定を削除 --%>
<html:form action="/common/tenpu_sentaku" 
			method="POST" 
			enctype="multipart/form-data">
<html:hidden property="indexId" />
	<DIV class="mainlist">

		<TABLE style="border:0px; width:100%;">
			<%-- 管理票No200807071057, 2008/07/09, SJA渡辺, 説明文を追加 --%>
			<TR style="border:0px; width:100%;">
				<TD colspan="2" style="border:0px; text-align:left;">
					<FONT COLOR="#FF0000">
						<%=i18n.get(GL.MSG_0020)%>
					</FONT>
				</TD>
			</TR>
			<TR style="border:0px; width:100%;">
				<TD colspan="2" style="border:0px; text-align:left;">
					<%=i18n.get(GL.LABEL_ATTACHMENT)%>&nbsp;<%=i18n.get(GL.LABEL_FILE_NM)%>
				</TD>
			</TR>
			<TR style="border:0px; width:100%;">
				<TD style="border:0px; width:90%;">
					<%-- No862, 2008/06/16, SJA渡辺, 言語モードでIMEコントロールを切り替えるように修正 --%>
					<%-- No862, 2008/06/16, SJA渡辺, 初期値が英数字入力モードに修正 --%>
					<%-- 結合テスト障害No034対応, テキストボックスを入力不可とする --%>
					<input type="file" name="fileUp" value="" onkeydown="return keycancel();" onbeforeeditfocus="return false;" style="padding: 2px; width:100%;background-color: #F8F8FF;" class="singleByte">
				</TD>
				<TD style="border:0px; width:10%; text-align:right;" >
					<input style="width: 50px;
								background-color:#CCCCCC" 
								type="button" 
								value=" OK " 
								onclick="doSubmit('ok')"/>
				</TD>
			</TR>
			<TR style="border:0px; width:100%;" >
				<TD style="border:0px" colspan="2" class="right">
					<input style="text-align: center;
								width: 100px;
								background-color:#CCCCCC" 
								class="center" 
								type="button" 
								value="<%=i18n.get(GL.BTN_REGISTER)%>" 
								onclick="doSubmit('touroku')"/>
				</TD>
			</TR>
		</TABLE>
	</DIV>
</html:form>
</DIV>

<DIV id="list">
<html:form action="/common/tenpu_sentaku">
<html:hidden property="indexId" />

	<DIV class="mainlist">
		<TABLE border=0 cellSpacing=0 cellPadding=0 style="width:100%;">
		<THEAD>
		<TR>
			<%--課題No.09--%>
			<%--修正開始--%>
			<TH style="text-align: center;width:8%;"><%=i18n.get(GL.LABEL_ATTACHMENT)%></TH>
			<TH style="text-align: center;width:8%;"><%=i18n.get(GL.BTN_DELETE)%></TH>
			<%--<TH style="text-align: center;width:54%;"><%=i18n.get(GL.LABEL_FILE_NM)%></TH>--%>
			<%--<TH style="text-align: center;width:15%;"><%=i18n.get(GL.LABEL_PHASE)%></TH>--%>
			<%--<TH style="text-align: center;width:15%;"><%=i18n.get(GL.LABEL_OWNER)%></TH>--%>
			<TH style="text-align: center;width:53%;"><%=i18n.get(GL.LABEL_FILE_NM)%></TH>
			<TH style="text-align: center;width:8%;"><%=i18n.get(GL.LABEL_EDABAN)%></TH>
			<TH style="text-align: center;width:15%;"><%=i18n.get(GL.LABEL_PHASE)%></TH>
			<TH style="text-align: center;width:8%;"><%=i18n.get(GL.LABEL_OWNER)%></TH>
			<%--修正完了--%>
		</TR>
		</THEAD>
		<TBODY>
		<% if(list != null) { %>
		<logic:iterate id="meisai" collection="<%=list%>" type="java.util.HashMap" indexId="idx">
		<%--課題No.09--%>
		<%--修正開始--%>
		<%--<TR>--%>
		<TR style="<bean:write name="meisai" property="rowColor"/>">
			<TD <bean:write name="meisai" property="tenpuCheckDis"/> class="center">
				<logic:equal name="meisai" property="tenpuCheck" value="on">
						<input type=checkbox name="tenpu(<bean:write name="idx"/>)" property="tenpuOn" onClick="setTenpuIndex(<bean:write name="idx"/>)" checked/>
				</logic:equal>
				<logic:notEqual name="meisai" property="tenpuCheck" value="on">
						<input type=checkbox name="tenpu(<bean:write name="idx"/>)" property="tenpuOn" onClick="setTenpuIndex(<bean:write name="idx"/>)"/>
				</logic:notEqual>
			</TD>
			<TD <bean:write name="meisai" property="sakujoCheckDis"/> class="center">
				<logic:equal name="meisai" property="sakujoCheck" value="on">
						<input type=checkbox name="sakujo(<bean:write name="idx"/>)" property="sakujoOn" onClick="setSakujoIndex(<bean:write name="idx"/>)" checked/>
				</logic:equal>
				<logic:notEqual name="meisai" property="sakujoCheck" value="on">
						<input type=checkbox name="sakujo(<bean:write name="idx"/>)" property="sakujoOn" onClick="setSakujoIndex(<bean:write name="idx"/>)"/>
				</logic:notEqual>
			</TD>
			<TD class="left" style="word-break:break-all;">
				<%--<logic:equal name="meisai" property="touroku_flg" value="1">--%>
					<%-- 課題No.52 添付ファイルダウンロード対応 --%>
					<%-- 修正開始 --%>
					<%-- <a href="<%=GS.WEB_COMMON+GS.TENPU_SENTAKU+GS.ACTION%>?<%=GS.EVENT%>=download&index=<%=idx.intValue()%>"> --%>
					<a href="#" onclick="windowOpen('download','<%=idx.intValue()%>','<bean:write name="meisai" property="file_nm" />')">
					<%-- 修正完了 --%>
				<%--</logic:equal>--%>
				<bean:write name="meisai" property="file_nm" /></a>&nbsp;</TD>
			<TD class="center" style="word-break:break-all;"><bean:write name="meisai" property="anken_no_eda" />&nbsp;</TD>
		<%--修正完了--%>
			<TD class="center" style="word-break:break-all;"><bean:write name="meisai" property="phase" />&nbsp;</TD>
			<TD class="center"><bean:write name="meisai" property="syoyuu_kaisha_cd" />&nbsp;</TD>
		</TR>
		</logic:iterate>
		<% } %>
		</TABLE>
	</DIV>

</html:form>
</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>