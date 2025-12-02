<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="CyusyutujyokenForm" name="04CyusyutujyokenForm" type="app.system.form.CyusyutujyokenHqForm" />
<% 
	List list = CyusyutujyokenForm.getKentouList();
	
	String type = CyusyutujyokenForm.getType();
	boolean readonlyFlg = false;
	if(!"1".equals(type)) {
		readonlyFlg = true;
	}
	
	boolean kentouDisabled = true;
	if("1".equals(CyusyutujyokenForm.getKentou_flg())) {
		kentouDisabled = false;
	}
	
	boolean tairyuDisabled = true;
	if("1".equals(CyusyutujyokenForm.getTairyu_flg())) {
		tairyuDisabled = false;
	}
%>
<script>
	function cyusyutujyokenLink(event, idx) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;

		form = document.forms[0];
		form.elements["selectIdx"].value = idx;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
		form.action = action;
	}
	function cyusyutujyokenSakujo(event, cd, name) {
		if (cd == null || cd == "") {
			alert('<%=i18n.get(GL.CONFIRM_0014)%>');
		} else {
			if(window.confirm(cd + ' ' + name + ' <%=i18n.get(GL.CONFIRM_0004)%>')){
				form = document.forms[0];
				action = form.action;
				form.target = "_top";
				form.action += "?<%=GS.EVENT%>=" + event;
				form.submit();
			}
		}
	}
	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;
  		  		
  		var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];
	
		<%-- scrollHeightの値ピッタリでは、スクロールバーが出る可能性がある為
		 サイズに余裕を持たせる。--%>
		sHeight += 10;
		
		<%-- デフォルト値を超える場合はリサイズ --%>
		if (700 < sHeight) {
			iframeObj.style.height = sHeight;
		}
	}
	<%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
	function doToroku(event) {
	 	document.forms[0].elements["focusEvent"].value = event;
	  document.forms[0].elements["focusId_satei"].value = event;
	  
	 	if(blockSubmit()==false) return;

	 	form = document.forms[0];
	 	action = form.action;
	 	form.target = "_top";
	 	form.action += "?event=" + event;
	 	form.submit();
	}
</script>


</HEAD>
<BODY onload="resizeParentIFrame()">

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<DIV id="list">

<form action="/system/cyusyutujyoken_hq" >

<input type="hidden" property="selectIdx" />
<input type="hidden" property="focusEvent" /><%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>
<input type="hidden" property="focusId_satei" /><%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のため処理追加 --%>

<%-- No535, 2008/06/04, SJA渡辺, 読み取り専用テキストのフォーカスを外す --%>
<%
String tabIndexValue = "0";
if ( readonlyFlg==true ) {
tabIndexValue = "-1";
}
%>

	<DIV class="headlist">
		&nbsp;
		<%-- 処理タイプ選択 ラジオボタン --%>
		<input type="radio" onclick="doSubmit('type')" property="type" value="1" /><span><%=i18n.get(GL.LABEL_NEW_REGISTER)%>&nbsp;</span>
		<input type="radio" onclick="doSubmit('type')" property="type" value="2" /><span><%=i18n.get(GL.LABEL_UPDATE)%>&nbsp;</span>
		<input type="radio" onclick="doSubmit('type')" property="type" value="3" /><span><%=i18n.get(GL.LABEL_DELETE)%>&nbsp;</span>
		<BR>
		<TABLE class="none" cellSpacing=0 cellPadding=0>
		<TR>
			<TD>
				<BR>
				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
					<%-- 抽出事由 --%>
					<%-- No535, 2008/06/04, SJA渡辺, 読み取り専用テキストのフォーカスを外す --%>
					<TD width="12%" style="word-break:break-all;"><%=i18n.get(GL.LABEL_REASON_I)%></TD>
					<TD><input type="text" name="CyusyutujyokenForm" property="jiyuu_cd" maxlength="2" size="4" readonly="<%=readonlyFlg%>" tabindex="<%=tabIndexValue%>" /></TD>
					<%-- 条件名称 --%>
					<TD width="12%" style="word-break:break-all;"><%=i18n.get(GL.LABEL_JOKEN_NAME)%></TD>
					<TD><input type="text" name="CyusyutujyokenForm" property="jiyuu_nm" maxlength="60" styleClass="doubleByte"/></TD>
					<%-- 格付 selectbox --%>
					<TD width="7%" style="word-break:break-all;"><%=i18n.get(GL.LABEL_KAKUTSUKE)%></TD>
					<TD>
						<select property="kakuzuke">
				    		<optionsCollection name="CyusyutujyokenForm" property="kakutukeList" value="value" label="key" />
				  	</select>
					</TD>
				</TR>
				</TABLE>

				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
					<%-- 滞留期間 --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_TAIRYU_KIKAN)%></TD>
					<TD><input type="text" name="CyusyutujyokenForm" property="tairyu_from" maxlength="3" size="2" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_MON)%></TD>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TD>～</TD>
					<%} else {%>
					<TD>-</TD>
					<%}%>
					<TD><input type="text" name="CyusyutujyokenForm" property="tairyu_to" maxlength="3" size="2" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_MON)%></TD>
					<%-- 通貨 --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_TUUKA)%></TD>
					<TD><input type="text" name="CyusyutujyokenForm" property="tuuka_kentou" maxlength="3" size="2" /></TD>
					<%-- 金額 --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_KINGAKU)%></TD>
					<TD><input type="text" name="CyusyutujyokenForm" property="kingaku_kentou" style="text-align:right;padding-right:1px;" /></TD>
				</TR>
				</TABLE>
							
				<TABLE class="none" style="width:70%" cellSpacing=0 cellPadding=0>
				<TR>
					<%-- 検討対象 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_KENTOU_T)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="kentou_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_kentou')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="kentou_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_kentou')" />
						</logic:notEqual>
					</TD>
					<%-- 滞留判定 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_TAIRYU)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="tairyu_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_tairyu')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="tairyu_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_tairyu')" />
						</logic:notEqual>
					</TD>
					<%-- 査定対象 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_SATEI_T)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="satei_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_satei')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="satei_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_satei')" />
						</logic:notEqual>
					</TD>
				</TR>
				<TR>
					<%-- 債務超過 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_SAIMUCHOKA)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="saimutyouka_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_saimutyouka')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="saimutyouka_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_saimutyouka')"/>
						</logic:notEqual>
					</TD>
					<%-- 赤字 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_AKAJI)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="akaji_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_akaji')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="akaji_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_akaji')"/>
						</logic:notEqual>
					</TD>
					<%-- リ企指 checkbox --%>
					<TD style="word-break:break-all;" class="right">
						<%=i18n.get(GL.LABEL_SHITEI)%>
					</TD>
					<TD>
						<logic:equal name="CyusyutujyokenForm" property="riki_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_riki')" checked />
						</logic:equal>
						<logic:notEqual name="CyusyutujyokenForm" property="riki_flg" value="1">
							<input type=checkbox onclick="doSubmit('check_riki')"/>
						</logic:notEqual>
					</TD>
				</TR>
				</TABLE>
			</TD>
			<TD class="center" valign="top">
				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
					<%-- 債権フラグ設定 head --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_SAIKEN_FLG)%></TD>
					<TD style="word-break:break-all;" width="16%"><p class="center"><%=i18n.get(GL.LABEL_IPPAN)%></p></TD>
					<TD style="word-break:break-all;" width="16%"><p class="center"><%=i18n.get(GL.LABEL_KOTEI)%></p></TD>
					<TD style="word-break:break-all;" width="16%"><p class="center"><%=i18n.get(GL.LABEL_HOSYOU)%></p></TD>
					<TD style="word-break:break-all;" width="16%"><p class="center"><%=i18n.get(GL.LABEL_HIKIATE)%></p></TD>
				</TR>
				<TR>
					<%-- データ作成(債権フラグ設定) checkbox --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_DATA_S)%></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_data_flg" value="1" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_data_flg" value="2" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_data_flg" value="3" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_data_flg" value="9" /></p></TD>
				</TR>
				<TR>
					<%-- 検討対象債権フラグ設定) checkbox --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_KENTOU_T)%></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_kentou_flg" value="1" disabled="<%=kentouDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_kentou_flg" value="2" disabled="<%=kentouDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_kentou_flg" value="3" disabled="<%=kentouDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_kentou_flg" value="9" disabled="<%=kentouDisabled%>" /></p></TD>
				</TR>
				<TR>
					<%-- 滞留判定(債権フラグ設定) checkbox --%>
					<TD style="word-break:break-all;"><%=i18n.get(GL.LABEL_TAIRYU)%></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_tairyu_flg" value="1" disabled="<%=tairyuDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_tairyu_flg" value="2" disabled="<%=tairyuDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_tairyu_flg" value="3" disabled="<%=tairyuDisabled%>" /></p></TD>
					<TD><p class="center"><input type="checkbox" name="CyusyutujyokenForm" property="saiken_tairyu_flg" value="9" disabled="<%=tairyuDisabled%>" /></p></TD>
				</TR>
				</TABLE>
			</TD>
		</TR>
		</TABLE>
				<%-- 要件No.四-13 2008/10/07 水口 抽出条件追加項目 --%>
				<%-- 追加開始 --%>
				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
			   	<TD style="width:1%;"></TD>
					<TD style="word-break:break-all;width:11%;" class="left"><%=i18n.get(GL.LABEL_KAKO_KTK_FLG)%></TD>
					<TD style="word-break:break-all;width:4%;" class="left"><input type="checkbox" name="CyusyutujyokenForm" property="kakoKtkFlg" value="1" /></TD>
					<TD style="word-break:break-all;width:10%;" class="right"><%=i18n.get(GL.LABEL_KAKO_KTK)%></TD>
					<TD style="width:4%;" class="left"><input type="text" name="CyusyutujyokenForm" property="kakoKtkFrom" maxlength="1" size="1" style="text-align:right;padding-right:1px;" /></TD>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TD colspan=2 style="width:4%;" class="center">～</TD>
					<%} else {%>
					<TD colspan=2 style="width:4%;" class="center">-</TD>
					<%}%>
					<TD style="width:4%;" class="left"><input type="text" name="CyusyutujyokenForm" property="kakoKtkTo" maxlength="1" size="1" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="word-break:break-all;width:17%;" class="right"><%=i18n.get(GL.LABEL_KAKO_KTK_SANSYO)%></TD>
					<TD style="width:4%;" class="left"><input type="text" name="CyusyutujyokenForm" property="kakoKtkSansyo" maxlength="3" size="3" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="word-break:break-all;width:10%;" class="left"><%=i18n.get(GL.LABEL_KAGETUMAE)%></TD>
					<TD style="width:25%;"></TD>
				</TR>
				</TABLE>
				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
				  <TD style="width:1%;"></TD>
					<TD style="word-break:break-all;width:8%;"><%=i18n.get(GL.LABEL_GENZAI_KOTEI_SAIKENGAKU)%></TD>
					<TD style="width:10%;"><input type="text" name="CyusyutujyokenForm" property="genzaiKoteiSaikengakuKagen" maxlength="15" size="15" style="text-align:right;padding-right:1px;" /></TD>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TD style="width:2%;" class="center">～</TD>
					<%} else {%>
					<TD style="width:2%;" class="center">-</TD>
					<%}%>
					<TD style="width:10%;"><input type="text" name="CyusyutujyokenForm" property="genzaiKoteiSaikengakuJyogen" maxlength="15" size="15" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="width:1%;"></TD>
					<TD style="word-break:break-all;width:8%;"><%=i18n.get(GL.LABEL_KAKO_KOTEI_SAIKENGAKU)%></TD>
					<TD style="width:10%;"><input type="text" name="CyusyutujyokenForm" property="kakoKoteiSaikengakuKagen" maxlength="15" size="15" style="text-align:right;padding-right:1px;" /></TD>
					<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
					<TD style="width:2%;" class="center">～</TD>
					<%} else {%>
					<TD style="width:2%;" class="center">-</TD>
					<%}%>
					<TD style="width:10%;"><input type="text" name="CyusyutujyokenForm" property="kakoKoteiSaikengakuJyogen" maxlength="15" size="15" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="width:1%;"></TD>					
					<TD style="word-break:break-all;width:8%;"><%=i18n.get(GL.LABEL_KOTEI_SAIKENGAKU_SANSYO)%></TD>
					<TD style="width:4%;"><input type="text" name="CyusyutujyokenForm" property="koteiSaikengakuSansyo" maxlength="3" size="3" style="text-align:right;padding-right:1px;" /></TD>
					<TD style="word-break:break-all;width:6%;"><%=i18n.get(GL.LABEL_KAGETUMAE)%></TD>
				</TR>
				</TABLE>
				<TABLE class="none" cellSpacing=0 cellPadding=0>
				<TR>
					<TD style="width:1%;"></TD>
					<TD style="word-break:break-all;width:12%;"class="left"><%=i18n.get(GL.LABEL_FLGSAKI_FLG)%></TD>
					<TD style="width:4%;" class="left"><input type="checkbox" name="CyusyutujyokenForm" property="flgsakiFlg" value="1" /></TD>
					<TD style="width:85%;"></TD>
				</TR>
				</TABLE>
				<%-- 追加完了 --%>
		<DIV class="center">
		<TABLE class="none" style="width:95%;align:center" cellSpacing=0 cellPadding=0>
			<TR>
				<TD><p class="right">
					<% if("3".equals(type)) { %><%-- 削除ボタン --%>
						<input type="button" value="&nbsp;<%=i18n.get(GL.BTN_DELETE)%>&nbsp;" 
						onclick="cyusyutujyokenSakujo('kentou_sakujo','<bean:write name="CyusyutujyokenForm" property="jiyuu_cd" />','<bean:write name="CyusyutujyokenForm" property="jiyuu_nm" />')" style="background:#CCCCCC;">
					<% } else { %><%-- 登録ボタン --%>
						<input type="button" value="&nbsp;<%=i18n.get(GL.BTN_REGISTER)%>&nbsp;" onclick="doToroku('kentou_touroku')" style="background:#CCCCCC;"><%-- No554, 2008/06/06, SJA平林, エラー時のフォーカス制御のためdoSubmitの名称変更 --%>
					<% } %>&nbsp;
				</p></TD>
			</TR>
			<TR><TD><%=i18n.get(GL.GUIDANCE_0008)%></TD></TR>
		</TABLE>
		</DIV>
	</DIV>
	
	<DIV class="mainlist">
		<TABLE style="width:90%" cellSpacing=0 cellPadding=0>
		<THEAD>
		<TR>
			<TH width="13%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_REASON_I)%></p></TH>
			<TH width="17%" colspan="3"><p class="center"><%=i18n.get(GL.LABEL_JOKEN_NAME)%></p></TH>
			<TH width="13%"><p class="center"><%=i18n.get(GL.LABEL_KAKUTSUKE)%></p></TH>
			<TH width="8%"><p class="center"><%=i18n.get(GL.LABEL_TUUKA)%></p></TH>
			<TH width="21%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_KINGAKU)%></p></TH>
			<TH width="12%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_FROM)%></p></TH>
			<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<TH width="4%"><p class="center">～</p></TH>
			<%} else {%>
			<TH width="4%"><p class="center">-</p></TH>
			<%}%>
			<TH width="12%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_TO)%></p></TH>
		</TR>
		<TR>
			<TH width="9%"><p class="center"><%=i18n.get(GL.LABEL_KENTOU_T)%></p></TH>
			<TH width="9%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_TAIRYU)%></p></TH>
			<TH width="9%"><p class="center"><%=i18n.get(GL.LABEL_SATEI_T)%></p></TH>
			<TH width="16%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_DATA_FLG)%></p></TH>
			<TH width="16%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_KENTOU_FLG)%></p></TH>
			<TH width="16%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_TAIRYU_FLG)%></p></TH>
			<TH width="9%"><p class="center"><%=i18n.get(GL.LABEL_SAIMUCHOKA)%></p></TH>
			<TH width="8%" colspan="2"><p class="center"><%=i18n.get(GL.LABEL_AKAJI)%></p></TH>
			<TH width="8%"><p class="center"><%=i18n.get(GL.LABEL_RIKI)%></p></TH>
		</TR>
		</THEAD>
		<TBODY>
		<% if(list != null) { %>
		<logic:iterate id="meisai" collection="<%=list%>" type="java.util.HashMap" indexId="idx">
		<TR>
			<TD colspan=2>
				<p class="center">
					<a href="#" onClick="cyusyutujyokenLink('jiyuu_link','<bean:write name="idx"/>')">
						<bean:write name="meisai" property="jiyuu_cd" />
					</a>
				</p>
			</TD>
			<TD colspan=3 style="word-break:break-all;"><p class="center">&nbsp;<bean:write name="meisai" property="jiyuu_nm" /></p></TD>
			<TD>
				<p class="center">
				<logic:equal name="meisai" property="mukakuzuke_flg" value="1">
					<%=i18n.get(GL.LABEL_MUKAKUTSUKE)%>
				</logic:equal>
				<logic:notEqual name="meisai" property="mukakuzuke_flg" value="1">
					<bean:write name="meisai" property="kakuzuke" />
				</logic:notEqual>&nbsp;
				</p>
			</TD>
			<TD><bean:write name="meisai" property="tuuka" />&nbsp;</TD>
			<TD colspan="2"><p class="right">&nbsp;<bean:write name="meisai" property="kingaku" /></p></TD>
			<TD colspan="2"><bean:write name="meisai" property="tairyu_from" />&nbsp;</TD>
			<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<TD>～</TD>
			<%} else {%>
			<TD>-</TD>
			<%}%>
			<TD colspan="2"><bean:write name="meisai" property="tairyu_to" />&nbsp;</TD>
		</TR>
		<TR>
			<TD><p class="center"><input type="checkbox" name="meisai" property="kentou_flg" value="1" disabled="true" /></p></TD>
			<TD colspan="2"><p class="center"><input type="checkbox" name="meisai" property="tairyu_flg" value="1" disabled="true" /></p></TD>
			<TD><p class="center"><input type="checkbox" name="meisai" property="satei_flg" value="1" disabled="true" /></p></TD>
			<TD colspan="2"><p class="center"><bean:write name="meisai" property="saiken_data_flg" />&nbsp;</p></TD>
			<TD colspan="2"><p class="center"><bean:write name="meisai" property="saiken_kentou_flg" />&nbsp;</p></TD>
			<TD colspan="2"><p class="center"><bean:write name="meisai" property="saiken_tairyu_flg" />&nbsp;</p></TD>
			<TD><p class="center"><input type="checkbox" name="meisai" property="saimutyouka_flg" value="1" disabled="true" /></p></TD>
			<TD colspan="2"><p class="center"><input type="checkbox" name="meisai" property="akaji_flg" value="1" disabled="true" /></p></TD>
			<TD><p class="center"><input type="checkbox" name="meisai" property="riki_flg" value="1" disabled="true" /></p></TD>
		</TR>
		</logic:iterate>
		<% } %>

		</TBODY>
		</TABLE>
	
	</DIV>
</form>
</DIV>
</DIV>
</BODY>
</HTML>