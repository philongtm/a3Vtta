<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%-- 再描画用 --%>
<base target="_self"/>

<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="TantoSoshikiSentakuForm" name="07TantoSoshikiSentakuForm" type="app.system.form.TantoSoshikiSentakuForm" />
<script language="javascript">

<%-- 閉じるボタンのイベント --%>
function tojiru() {
	doSubmitPop('close');
	window.close();
}

<%-- 反映ボタンのイベント --%>
function hanei(){
	<%-- チェックされた項目を記録する変数 --%>
	var str="";
	<%-- 配列を取得 --%>
	obj = document.getElementsByName("sentaku");

	<%-- for文でチェックボックスを１つずつ確認 --%>
	for( i=0; i<obj.length; i++ ){
		<%-- チェックされているか確認する --%>
		if( obj[i].checked ){
			<%--変数strが空でない時、区切りのコンマを入れる --%>
			if( str != "" ) str=str+",";
			<%-- チェックボックスのvalue値を変数strに入れる --%>
			str=str+obj[i].value;
		}
	}
	form = document.forms[0];
	form.elements["tanto_chk"].value = str;
	form.elements["hanei_flg"].value = 1;
	doSubmitPop('reflection');
}

<%-- onloadイベント（反映ボタン押下後）--%>
function openerSubmit() {
	form = document.forms[0];

	<%-- 配列を取得 --%>
	obj = document.getElementsByName("sentaku");
	var resArray = form.elements["tanto_chk"].value.split(",");

	if (form.elements["hanei_flg"].value == 1) {
		if(form.elements["errChkFlg"].value == 0){
			<%-- 戻り値をセットし、閉じる --%>
			window.returnValue = 1;
			window.close();
		} else {
			<%-- for文でチェック済みの項目に再チェックを行う --%>
			for( i=0; i<resArray.length; i++ ){
				no = resArray[i];
				obj[no].checked = true;
			}
			form.elements["errChkFlg"].value = 0;
			form.elements["hanei_flg"].value = 0;
		}
	}
}

function doSubmitPop(event) {
	<%--ボタン連打ブロック--%>
	if(blockSubmit()==false) return;
	subClose();
	form = document.forms[0];
	action = form.action;
	form.action += "?<%=GS.EVENT%>=" + event;
	form.submit();
}

</script>



</HEAD>
<BODY onload="openerSubmit();">
<CENTER>
	<%--コンテンツ部分--%>
	<DIV id="contents">
		<BR/>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS7116)%></H1>

		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_HANEI)%>" onclick="hanei()"/>
			<input type="button" value="<%=i18n.get(GL.BTN_CLOSE)%>" onclick="tojiru()"/>
		</DIV>
		<BR/>
		<html:form action="/system/tantososhiki_sentaku" >

		<html:hidden property="hanei_flg" />
		<html:hidden property="tanto_chk" />
		<html:hidden property="errChkFlg" />

		<DIV id="list">
			<DIV style="align:left;text-align:left" >
				<TABLE style="border:0px">
					<TR>
						<TD class="semaku" style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<%-- 汎用1 --%>
							<%=SESSION_DATA_APP.getLbl_nm1()%>
						</TD>
						<TD class="semaku" style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<html:select property="hanyou1" style="width:70" onchange="doSubmitPop('change1')">
								<html:option value=""></html:option>
								<html:optionsCollection name="TantoSoshikiSentakuForm" property="ar_hanyou1" value="value" label="key" />
							</html:select>
						</TD>
						<TD class="semaku" style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<%-- 汎用2 --%>
							<%=SESSION_DATA_APP.getLbl_nm3()%>
						</TD>
						<TD class="semaku" style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<html:select property="hanyou2" style="width:150px">
								<html:option value=""></html:option>
								<html:optionsCollection name="TantoSoshikiSentakuForm" property="ar_hanyou2" value="value" label="key" />
							</html:select>
						</TD>
						<TD class="semaku" style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<input type="button" value="<%=i18n.get(GL.BTN_SEARCH)%>" style="background:#CCCCCC;" onclick="doSubmitPop('search')"/>
						</TD>
					</TR>
				</TABLE>
			</DIV>
		</DIV>
		<BR/><BR/>

		<%-- 一覧情報 --%>
		<DIV class="mainlist" >
			<DIV style="width:98%;clear:left;">
				<DIV style="width:100%;align:left;text-align:left;margin: 0 30 0 0">
					<TABLE style="width:100%;border-left-color:#000000;" cellSpacing=0 cellPadding=0  >
						<THEAD>
							<TR class="semaku">
								<TH style="width:22%;" colspan="2">
									<p class="center"><%=SESSION_DATA_APP.getLbl_nm1()%></p>
								</TH>
								<TH style="width:40%;" colspan="2">
									<p class="center"><%=SESSION_DATA_APP.getLbl_nm3()%></p>
								</TH>
								<TH style="width:31%;" colspan="2">
								<% if(GS.ON.equals(TantoSoshikiSentakuForm.getHanyou3LabelFlg())) { %>
									<p class="center"><%=i18n.get(GL.OS7116_HONBU)%></p>
								<% } else { %>
									<p class="center">&nbsp;</p>
								<% } %>
								</TH>
								<TH style="width:7%;border-right-color:#000000;" rowspan="2"><p class="center"><%=i18n.get(GL.OS7116_SENTAKU)%></p></TH>
							</TR>
							<TR class="semaku">
								<TH style="width:8%;"><p><%=i18n.get(GL.OS7116_CODE)%></p></TH>
								<TH style="width:14%;"><p><%=i18n.get(GL.OS7116_NAME)%></p></TH>
								<TH style="width:10%;"><p><%=i18n.get(GL.OS7116_CODE)%></p></TH>
								<TH style="width:30%;"><p><%=i18n.get(GL.OS7116_NAME)%></p></TH>
								<% if(GS.ON.equals(TantoSoshikiSentakuForm.getHanyou3LabelFlg())) { %>
									<TH style="width:10%;"><p><%=i18n.get(GL.OS7116_CODE)%></p></TH>
									<TH style="width:21%;"><p><%=i18n.get(GL.OS7116_NAME)%></p></TH>
								<% } else { %>
									<TH style="width:10%;">&nbsp;</TH>
									<TH style="width:21%;">&nbsp;</TH>
								<% } %>
							</TR>
						</THEAD>
					</TABLE>
				</DIV>
				<DIV style="overflow-y:scroll;height: 400px;width:100%;">
					<DIV style="width:100%;align:right;margin: 0 15 0 0">
						<TABLE style="width:100%;border-left-color:#000000;" cellSpacing=0 cellPadding=0  >
							<TBODY>
								<nested:notEmpty property="ar_meisai">
								<nested:iterate property="ar_meisai" indexId="idx">
									<TR class="semaku">
										<%-- 汎用1 --%>
										<TD class="borderBottom borderLeft" style="width:8%;">
											<nested:write property="HANYOU1" />&nbsp;
										</TD>
										<%-- 汎用1名 --%>
										<TD class="borderBottom" style="width:14%;">
											<nested:write property="HANYOU1_NM" />&nbsp;
										</TD>
										<%-- 汎用2 --%>
										<TD class="borderBottom" style="width:10%;">
											<nested:write property="HANYOU2" />&nbsp;
										</TD>
										<%-- 汎用2名 --%>
										<TD class="borderBottom" style="width:30%;">
											<nested:write property="HANYOU2_NM" />&nbsp;
										</TD>
										<%-- 汎用3 --%>
										<TD class="borderBottom" style="width:10%;">
											<nested:write property="HANYOU3" />&nbsp;
										</TD>
										<%-- 汎用3名 --%>
										<TD class="borderBottom" style="width:21%;">
											<nested:write property="HANYOU3_NM" />&nbsp;
										</TD>
										<%-- 選択 --%>
										<nested:equal property="SENTAKU_FLG" value="1">
											<TD style="text-align:center;width:7%" class="borderBottom borderRight">
												<input type="checkbox" name="sentaku" value="<nested:write property="ID" />" checked="checked" />
											</TD>
										</nested:equal>
										<nested:notEqual property="SENTAKU_FLG" value="1">
											<TD style="text-align:center;width:7%" class="borderBottom borderRight">
												<input type="checkbox" name="sentaku" value="<nested:write property="ID" />" />
											</TD>
										</nested:notEqual>
									</TR>
								</nested:iterate>
								</nested:notEmpty>
							</TBODY>
						</TABLE>
					</DIV>
				</DIV>
			</DIV>
		</DIV>
		<BR/><BR/>
	</html:form>
</DIV>
</CENTER>
</BODY>
</HTML>