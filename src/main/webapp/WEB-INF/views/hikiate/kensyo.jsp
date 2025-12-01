<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>
<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="HikiatekinKensyoForm" name="02HikiatekinKensyoForm" type="app.hikiate.form.KensyoForm" />

<script>
	<%-- 金額のカンマを取り除く処理 --%>
	function removeConma(value) {
		if (value == null || value.length == 0) {
			return value;
		}
		returnValue = "";
		valueArray = value.split(",");
		for(i = 0; i < valueArray.length; i++) {
			returnValue = returnValue.concat(valueArray[i]);
		}
		return returnValue;
	}
	<%-- 引当金対象計算 --%>
	function calcFinalZandaka() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["totalFinalSaikenZandaka"].value));
		number2 = inputCheck(removeConma(form.elements["totalFinalRyuhosaimu"].value));
		number3 = inputCheck(removeConma(form.elements["finalHozen"].value));
		number4 = inputCheck(removeConma(form.elements["finalKingakuSonota"].value));
		number5 = inputCheck(removeConma(form.elements["finalKingakuKenen"].value));
		number6 = inputCheck(removeConma(form.elements["finalKingaku15"].value));
		var result = number1 - (number2 + number3 + number4) + number5 - number6;
		form.elements["finalZandaka"].value = result;
		calcFinalZnadakaAfterHosei();
	}
	<%-- 留保債務計計算 --%>
	function calcTotalFinalRyuhosaimu() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["finalRyuhosaimu"].value));
		number2 = inputCheck(removeConma(form.elements["finalRyuhosaimu3"].value));
		var result = number1 + number2;
		var totalFinalSaikenZandaka = inputCheck(removeConma(form.elements["totalFinalSaikenZandaka"].value));

		<%-- 障害票No803　2008/6/14　細野　totalFinalSaikenZandakaがマイナス値の場合、totalFinalRyuhosaimuを０にする --%>
		if(result > totalFinalSaikenZandaka) {
			if(0>totalFinalSaikenZandaka){
				form.elements["totalFinalRyuhosaimu"].value = 0;
			}else{
				form.elements["totalFinalRyuhosaimu"].value = totalFinalSaikenZandaka;
			}
		} else {
			form.elements["totalFinalRyuhosaimu"].value = result;
		}
		calcFinalZandaka();
	}
	<%-- 補正後引当金額計算 --%>
	function calcFinalKingakuAfterHosei() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["finalKingaku15"].value));
		number2 = inputCheck(removeConma(form.elements["finalKingakuHosei"].value));
		var result = number1 + number2;
		form.elements["finalKingakuAfterHosei"].value = result;
		calcFinalZnadakaAfterHosei();
	}
	<%-- 補正後引当控除後残高 --%>
	function calcFinalZnadakaAfterHosei() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["finalZandaka"].value));
		number2 = inputCheck(removeConma(form.elements["finalKingakuHosei"].value));
		var result = number1 - number2;
		form.elements["finalZnadakaAfterHosei"].value = result;
	}
	<%-- 入力チェック --%>
	function inputCheck(value) {
		var result = value;
		if(isNaN(result)) {
			result = 0;
		} else {
			result = new Number(result);
		}
		return result;
	}
	
	function setFocusId(elm) {
		form = document.forms[0];
		var size = form.elements.length;
		for(i = 0;i < size;i++) {
			if (elm == form.elements[i]) {
				form.elements["focusId"].value = i;
				break;
			}
		}
	}
	<%-- No459, 2008/05/30, SJA渡辺, フォーカスの設定を追加 --%>
	function cngFocus(val) {
		if (val!=0) {
			document.forms[0].elements[val].focus();
		}
	}
	function resetFocusId() {
		document.forms[0].elements["focusId"].value = 0;
	}
	
	<%-- No647, 2008/06/06, SJA渡辺, 三桁でカンマ挿入修正 --%>
	function insertComma(val) {
		var result = val;
		var tmp = "";
		while (result != (tmp = result.replace(/^([+-]?\d+)(\d\d\d)/,"$1,$2"))) {
			result = tmp;
		}
		return result;
	}
	
	<%-- No647, 2008/06/06, SJA渡辺, 三桁でカンマ挿入実行修正 --%>
	function doInsertComma() {
		form = document.forms[0];
		form.elements["finalRyuhosaimu"].value = insertComma(removeConma(form.elements["finalRyuhosaimu"].value));
		form.elements["finalRyuhosaimu3"].value = insertComma(removeConma(form.elements["finalRyuhosaimu3"].value));
		form.elements["totalFinalRyuhosaimu"].value = insertComma(removeConma(form.elements["totalFinalRyuhosaimu"].value));
		form.elements["finalHozen"].value = insertComma(removeConma(form.elements["finalHozen"].value));
		form.elements["finalKingakuSonota"].value = insertComma(removeConma(form.elements["finalKingakuSonota"].value));				
		form.elements["finalKingakuKenen"].value = insertComma(removeConma(form.elements["finalKingakuKenen"].value));
		
		form.elements["finalZandaka"].value = insertComma(removeConma(form.elements["finalZandaka"].value));
		form.elements["finalKingakuHosei"].value = insertComma(removeConma(form.elements["finalKingakuHosei"].value));		
		form.elements["finalKingakuAfterHosei"].value = insertComma(removeConma(form.elements["finalKingakuAfterHosei"].value));
		form.elements["finalZnadakaAfterHosei"].value = insertComma(removeConma(form.elements["finalZnadakaAfterHosei"].value));
	}
	<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
	<%-- 追加開始 --%>
	function chkSaikenToriKbn() {
		form = document.forms[0];
		rtn = true;
		if(form.elements["txtKbnTorihiki"].value != form.elements["finalKbnTorihiki"].value){
			rtn = false;
		}
		if(form.elements["txtKbnSaiken"].value != form.elements["finalKbnSaiken"].value){
			rtn = false;
		}
		if(!rtn){
			alert('<%=i18n.get(GL.ERR_CHK_SAIKENTORIKBN)%>');
		}else{
			doSubmit('register');
		}
	}
	<%-- 追加完了 --%>
</script>
</HEAD>
<BODY onload="cngFocus('<bean:write name="HikiatekinKensyoForm" property="focusId" />');">
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
<H1 class="title01"><%=i18n.get(GL.TITLE_02_04)%></H1>

<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%>
<BR><BR>
<%}%>

<%-- No554, 2008/06/05, SJA遠藤, エラー時のフォーカス制御を追加 --%>
<%
String focus = "";
if(request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
	focus = "indexSyonin";
}else{
	focus = (String)request.getAttribute(GS.FOCUS_FIELD);
}
%>
<html:form action="/hikiate/kensyo" focus="<%= focus %>">

<DIV id="submenu">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
	<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="resetFocusId();doSubmit('release')">
	<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="resetFocusId();doSubmit('forward')">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="resetFocusId();doSubmit('firstPreserve')">
	<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
	<%-- chkSaikenToriKbn() 追加開始 --%>
	<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="resetFocusId();chkSaikenToriKbn();">
	<%-- 追加完了 --%>
	<%-- 管理票No200807071027, 2008/07/09, SJA渡辺, ポップアップを表示するように修正 --%>
	<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="resetFocusId();backConfirm('back')"><BR>
	<DIV align="right">
	<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')">
	<input type="button" value="<%=i18n.get(GL.TITLE_00_01BOTTOM)%>" onclick="resetFocusId();doSubmit('result')">
	<input type="button" value="<%=i18n.get(GL.BTN_TMPSENTAKU)%>" onclick="resetFocusId();doSubmit('appendSelection')">
	<%} else {%>
	<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="resetFocusId();doSubmit('release')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="resetFocusId();doSubmit('forward')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="resetFocusId();doSubmit('firstPreserve')" style="WIDTH: 115px; HEIGHT: 22px">
	<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
	<%-- chkSaikenToriKbn() 追加開始 --%>
	<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="resetFocusId();chkSaikenToriKbn();" style="WIDTH: 115px; HEIGHT: 22px">
	<%-- 追加完了 --%>
	<%-- 管理票No200807071027, 2008/07/09, SJA渡辺, ポップアップを表示するように修正 --%>
	<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="resetFocusId();backConfirm('back')" style="WIDTH: 115px; HEIGHT: 22px"><BR>
	<DIV align="right">
	<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.TITLE_00_01BOTTOM)%>" onclick="resetFocusId();doSubmit('result')" style="font-size:10px;WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TMPSENTAKU)%>" onclick="resetFocusId();doSubmit('appendSelection')" style="WIDTH: 115px; HEIGHT: 22px"><BR><BR>
	<%}%>
	<BR><BR>
	<%-- 管理票No200807071054, 2008/07/09, SJA渡辺, コメント表示を赤字に修正 --%>
	<logic:equal name="HikiatekinKensyoForm" property="hidFlgSasiTen" value='1'><DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('comment')" class="linkStyle"><%=i18n.get(GL.LINK_COMMENT)%></a></DIV></logic:equal>
	<logic:equal name="HikiatekinKensyoForm" property="hidFlgSasiTen" value='2'><DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('comment')" class="linkStyle"><%=i18n.get(GL.LINK_COMMENT)%></a></DIV></logic:equal>

	</DIV>
</DIV>
<DIV id="list">
	<DIV class="mainlist">
	<html:hidden property="focusId" />
	<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
	<%-- 追加開始 --%>
	<html:hidden property="txtKbnTorihiki" />
	<html:hidden property="txtKbnSaiken" />
	<%-- 追加完了 --%>
		<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;width:8%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_TORIHIKISAKI)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:12%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:70px;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtCdTorihiki" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:8%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_TORIHIKISAKI_NM)%>
          </DIV>
				</TD>
				<TD colspan="5" style="border:0px;width:70%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtNameTorihiki" /><BR>
          </DIV>
				</TD>
			</TR>
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-top:2px;">
					  <%=i18n.get(GL.LABEL_OP)%>
          </DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:70px;">
	  				<bean:write name="HikiatekinKensyoForm" property="showKbnTairyu" /><BR>
		      </DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-top:2px;">				  
					  <%=i18n.get(GL.LABEL_C_RATING)%>
		      </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:40px;text-align:center;">				
				  	<bean:write name="HikiatekinKensyoForm" property="txtKtk" /><BR>
			    </DIV>
				</TD>
				<TD style="border:0px;width:6%;text-align:left;" class="semaku">
					<DIV class="dottitle" style="margin-top:2px;">
					  <%=i18n.get(GL.LABEL_OYA_KAISYA)%>
                    </DIV>
				</TD>
				<TD style="border:0px;width:6%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:40px;text-align:center;">
					<logic:notEmpty name="HikiatekinKensyoForm" property="txtNoOyaDuns">
						<bean:write name="HikiatekinKensyoForm" property="txtOyaKtk" />
					</logic:notEmpty><BR>
                  </DIV>
				</TD>
				<TD style="border:0px;width:38%;text-align:left;" class="semaku">
          <DIV class="ReadOnlybox" style="width:100%;">
				  	<bean:write name="HikiatekinKensyoForm" property="txtNameOya" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:center;" class="semaku">
					<%String flg = "";%>
					<logic:notEmpty name="HikiatekinKensyoForm" property="txtNoOyaDuns">
						<logic:equal name="HikiatekinKensyoForm" property="txtFlgIttai" value='1'><%flg = i18n.get(GL.LABEL_OYA_ITTAI);%></logic:equal>
						<logic:equal name="HikiatekinKensyoForm" property="txtFlgDokuritu" value='1'><%flg = i18n.get(GL.LABEL_OYA_DOKURITU);%></logic:equal>
					</logic:notEmpty>
					<DIV class="ReadOnlybox" style="width:100%;"><%=flg%></DIV><BR>
				</TD>
			</TR>
		</TABLE>
		<%} else {%>
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_TORIHIKISAKI)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtCdTorihiki" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_TORIHIKISAKI_NM)%>
          </DIV>
				</TD>
				<TD colspan="5" style="border:0px;width:52%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtNameTorihiki" /><BR>
          </DIV>
				</TD>
			</TR>
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="dottitle">
					  <%=i18n.get(GL.LABEL_OP)%>
          </DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;">
	  				<bean:write name="HikiatekinKensyoForm" property="showKbnTairyu" /><BR>
		      </DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				  <DIV class="dottitle">				  
					  <%=i18n.get(GL.LABEL_C_RATING)%>
		      </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:40px;text-align:center;">				
				  	<bean:write name="HikiatekinKensyoForm" property="txtKtk" /><BR>
			    </DIV>
				</TD>
				<TD style="border:0px;width:8%;text-align:left;" class="semaku">
					<DIV class="dottitle">
					  <%=i18n.get(GL.LABEL_OYA_KAISYA)%>
                    </DIV>
				</TD>
				<TD style="border:0px;width:6%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:40px;text-align:center;">
					<logic:notEmpty name="HikiatekinKensyoForm" property="txtNoOyaDuns">
						<bean:write name="HikiatekinKensyoForm" property="txtOyaKtk" />
					</logic:notEmpty><BR>
                  </DIV>
				</TD>
				<TD style="border:0px;width:37%;text-align:left;" class="semaku">
          <DIV class="ReadOnlybox" style="width:100%;">
				  	<bean:write name="HikiatekinKensyoForm" property="txtNameOya" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:center;" class="semaku">
					<%String flg = "";%>
					<logic:notEmpty name="HikiatekinKensyoForm" property="txtNoOyaDuns">
						<logic:equal name="HikiatekinKensyoForm" property="txtFlgIttai" value='1'><%flg = i18n.get(GL.LABEL_OYA_ITTAI);%></logic:equal>
						<logic:equal name="HikiatekinKensyoForm" property="txtFlgDokuritu" value='1'><%flg = i18n.get(GL.LABEL_OYA_DOKURITU);%></logic:equal>
					</logic:notEmpty>
					<DIV class="ReadOnlybox" style="width:100%;"><%=flg%></DIV><BR>
				</TD>
			</TR>
		</TABLE>
		<%}%>
		
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;">
				<TD style="border:0px;text-align:right;" class="semaku">
					<DIV style="text-align:right;">
					<%=i18n.get(GL.LABEL_A_PERSON)%>
					<html:select  property="indexSyonin" style="width:200">
						<logic:notEmpty name="HikiatekinKensyoForm" property="recoName">
							<logic:iterate id="meisai" name="HikiatekinKensyoForm" property="recoName" type="java.util.HashMap">
								<%String id = (String)meisai.get("id");%>
								<html:option value="<%=id%>">
									<bean:write name="meisai" property="USER_NM" />
								</html:option>
							</logic:iterate>
						</logic:notEmpty>
					</html:select>
					</DIV>
				</TD>
			</TR>
		</TABLE>
	
		<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_SATEI_YM)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:18%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:60px;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtYmSatei" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:8%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:13%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:5%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:10%;text-align:left;margin-bottom:5px;" class="semaku">
				  <DIV class="dottitle">
					  <%=i18n.get(GL.LABEL_BASE_YEAR)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:14%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:60px;margin-bottom:5px;">
					  <bean:write name="HikiatekinKensyoForm" property="txtFinalYmKijun" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:8%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:14%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
			</TR>
			
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;text-align:left;" class="semaku">
					<DIV class="dottitle"><%=i18n.get(GL.LABEL_CUST_CATEGORY)%></DIV>
				</TD>
				<%-- No537, 2008/06/05, SJA渡辺, 重複する、または、使用されていないプロパティ値削除による修正 --%>
				<TD style="border:0px;text-align:left;" class="semaku">
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="1" >
						<DIV class="ReadOnlybox" style="width:100px;">
						<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
						<%-- 修正開始 --%>
							<%-- <%=i18n.get(GL.LABEL_SEIJO_YOUTYUUI)%> --%>
							<%=i18n.get(GL.LABEL_SEIJO)%>
						<%-- 修正完了 --%>
						</DIV>
					</logic:equal>
					<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
					<%-- 追加開始 --%>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="2" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_YOUTYUUI)%>
						</DIV>
					</logic:equal>
					<%-- 追加完了 --%>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="3" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_KASHITAORE)%>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="4" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_HASAN_KOSEI)%>
						</DIV>
					</logic:equal>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<DIV class="dottitle"><%=i18n.get(GL.LABEL_CRED_CATEGORY)%></DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="1" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_IPAN_SAIKEN)%>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="2" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_KASHITAORE_KENEN_SAIKEN)%>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="3" >
						<DIV class="ReadOnlybox" style="width:100px;">
							<%=i18n.get(GL.LABEL_HASAN_KOSEI_SAIKEN)%>
						</DIV>
					</logic:equal>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<%=i18n.get(GL.LABEL_CUST_CATEGORY)%>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
				<%-- onchange 削除開始 --%>
					<html:select property="finalKbnTorihiki" style="width:120">
				<%-- 削除完了 --%>
						<html:optionsCollection name="HikiatekinKensyoForm" property="torihikiList" value="value" label="key" />
					</html:select>&nbsp;&nbsp;
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<%=i18n.get(GL.LABEL_CRED_CATEGORY)%>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku"><DIV style="width : 100%;">
				<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
				<%-- onchange 削除開始 --%>
				<html:select property="finalKbnSaiken" style="width:120">
				<%-- 削除完了 --%>
					<html:optionsCollection name="HikiatekinKensyoForm" property="saikenList" value="value" label="key" />
				</html:select></DIV>
				</TD>
			</TR>
		</TABLE>
		<%} else {%>
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;width:9%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%=i18n.get(GL.LABEL_SATEI_YM)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:19%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;margin-bottom:5px;word-break:break-all;">
					  <bean:write name="HikiatekinKensyoForm" property="txtYmSatei" />
				  </DIV>
				</TD>
				<TD style="border:0px;width:9%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:11%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:3%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:10%;text-align:left;margin-bottom:5px;" class="semaku">
				  <DIV class="dottitle">
					  <%=i18n.get(GL.LABEL_BASE_YEAR)%>
          </DIV>
				</TD>
				<TD style="border:0px;width:15%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:60px;margin-bottom:5px;word-break:break-all;">
					  <bean:write name="HikiatekinKensyoForm" property="txtFinalYmKijun" /><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:10%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
				<TD style="border:0px;width:14%;text-align:left;margin-bottom:5px;" class="semaku">
				</TD>
			</TR>
			
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;text-align:left;" class="semaku">
					<DIV class="dottitle"><%=i18n.get(GL.LABEL_CUST_CATEGORY)%></DIV>
				</TD>
				<%-- No537, 2008/06/05, SJA渡辺, 重複する、または、使用されていないプロパティ値削除による修正 --%>
				<TD style="border:0px;text-align:left;" class="semaku">
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="1" >
						<DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
						<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
						<%-- 修正開始 --%>
							<%-- <%=i18n.get(GL.LABEL_SEIJO_YOUTYUUI)%> --%>
							<%=i18n.get(GL.LABEL_SEIJO)%>
						<%-- 修正完了 --%>
						</DIV>
					</logic:equal>
					<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
					<%-- 追加開始 --%>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="2" >
						<DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
							<%=i18n.get(GL.LABEL_YOUTYUUI)%>
						</DIV>
					</logic:equal>
					<%-- 追加完了 --%>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="3" >
						<DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
							<%=i18n.get(GL.LABEL_KASHITAORE)%>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnTorihiki" value="4" >
						<DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
							<%=i18n.get(GL.LABEL_HASAN_KOSEI)%>
						</DIV>
					</logic:equal>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<DIV class="dottitle"><%=i18n.get(GL.LABEL_CRED_CATEGORY)%></DIV>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="1" >
						<DIV class="ReadOnlybox" style="word-break:break-all;">
							<%=i18n.get(GL.LABEL_IPAN_SAIKEN)%><BR>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="2" >
						<DIV class="ReadOnlybox" style="word-break:break-all;">
							<%=i18n.get(GL.LABEL_KASHITAORE_KENEN_SAIKEN)%>
						</DIV>
					</logic:equal>
					<logic:equal name="HikiatekinKensyoForm" property="txtKbnSaiken" value="3" >
						<DIV class="ReadOnlybox" style="word-break:break-all;">
							<%=i18n.get(GL.LABEL_HASAN_KOSEI_SAIKEN)%>
						</DIV>
					</logic:equal>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<%=i18n.get(GL.LABEL_CUST_CATEGORY)%>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
				<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
				<%-- onchange 削除開始 --%>
					<html:select property="finalKbnTorihiki" style="width:110">
				<%-- 削除完了 --%>
						<html:optionsCollection name="HikiatekinKensyoForm" property="torihikiList" value="value" label="key" />
					</html:select>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku">
					<%=i18n.get(GL.LABEL_CRED_CATEGORY)%>
				</TD>
				<TD style="border:0px;text-align:left;" class="semaku"><DIV style="width : 100%;">
				<%-- 課題No.73 取引先区分プルダウン設定値を2.0次に合わせる --%>
				<%-- onchange 削除開始 --%>
				<html:select property="finalKbnSaiken" style="width:110">
				<%-- 削除完了 --%>
					<html:optionsCollection name="HikiatekinKensyoForm" property="saikenList" value="value" label="key" />
				</html:select></DIV>
				</TD>
			</TR>
		</TABLE>
		<%}%>
		
		<TABLE style="border:0px;width:100%;height:100%;border-collapse: collapse;table-layout:fixed;">
			<TR style="border:0px;">
			<%-- 初回・中間月 Start--%>
				<TD style="border:0px;width:44%;text-align:right;vertical-align: top;">
					<%=i18n.get(GL.LABEL_TUUKA)%>&nbsp;
					<%=i18n.get(GL.LABEL_COLON)%>&nbsp;
					<bean:write name="HikiatekinKensyoForm" property="txtNameTuuka" />
					<br>

				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD colspan="1" rowspan="14" style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD colspan="1" rowspan="12" style="width: 7%;border-bottom-color: #666699;" class="inputRyuhoColor"><br>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_UKETORI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<%-- No535, 2008/05/31, SJA関塚, 読み取り専用テキストのフォーカスを外す --%>
							<html:text name="HikiatekinKensyoForm" property="kingaku01" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_YUSYUTSU_UKETORI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku02" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="vertical-align: top; text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_UTIKAKE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku03" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TORI_TOKIN)%><BR>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku04" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TATEKAE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku05" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_MISYUNYU)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku06" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_MISYUSYU)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku07" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TANKI_KASHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku08" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_SASHIIRE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku09" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_KARIBARAI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku10" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TYOKI_KASHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku11" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_SONOTA_TOSHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku12" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%=i18n.get(GL.LABEL_IPANSAIKEN_KEI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalSaikenIppan" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%=i18n.get(GL.LABEL_KOTEI_EIGYO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingaku16" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="3" class="inputThColor">
							<%=i18n.get(GL.LABEL_SAIKEN_ZAN)%><%=i18n.get(GL.LABEL_ONE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalSaikenZandaka" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
				</TABLE>

				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%=i18n.get(GL.LABEL_RESERVATION)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="ryuhosaimu" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%=i18n.get(GL.LABEL_OP_RYUHO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="ryuhosaimu3" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_RYUHO_SAIMU_KEI)%><%=i18n.get(GL.LABEL_TWO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalRyuhosaimu" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_HOZEN)%><%=i18n.get(GL.LABEL_THREE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="hozen" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_SONOTA_KAISYU)%><%=i18n.get(GL.LABEL_FOUR)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingakuSonota" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
				</TABLE>

				<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="1" colspan="2" style="border-bottom:none;"  class="inputThColor" >
						<%=i18n.get(GL.LABEL_HOSYO_SAIMU)%>
					</TD>
					<TD style="width: 40%; text-align: right;" class="inputNoColor">
						<bean:write name="HikiatekinKensyoForm" property="kingaku14" />
					</TD>
				</TR>
				<TR>
					<TD style="width: 7%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
					</TD>
					<TD style="width: 47%; text-align: right; border:solid 1px #AAA;">
						<%=i18n.get(GL.LABEL_RIKO_SEIKYU)%><%=i18n.get(GL.LABEL_FIVE)%>
					</TD>
					<TD style="width: 40%; text-align: right;"  class="inputNoColor">
						<bean:write name="HikiatekinKensyoForm" property="kingakuKenen" />
					</TD>
				</TR>
				</TABLE>
				
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_KIBIKIATE)%><%=i18n.get(GL.LABEL_SIX)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							
							<html:text name="HikiatekinKensyoForm" property="kingaku15" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_HIKIATE_TAISYO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingakuHikiateTaisyo" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
				</TABLE>
				<DIV align="left">
					<%=i18n.get(GL.LABEL_TUIKA_KEISAN)%>
				</DIV>
				<TABLE style="border:0px;width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_TUIKA_HIKIATE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="kingakuTuika" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
				</TABLE>
				<BR>
				<DIV align="left">
					<%=i18n.get(GL.LABEL_HIKIATE_KONKYO)%>
				</DIV>
				<TABLE style="width:100%;border:0px;border-collapse: collapse;table-layout:fixed;" class="semaku">
					<TR style="border:0px;">
					<%-- 障害票No383　2008/05/16　SJA渡辺　入力不可コメント欄を統一性を保つためにテキストエリアではなく、枠で囲む方法にする --%>
						<TD style="border:0px;width:100%;text-align:left;vertical-align:top;">
							<DIV class="ReadOnlybox" style="width:100%;">
							<%-- No577, 2008/06/13, 新実
						コメント欄が適切に折り返され横に伸びないように修正。
						コメント欄の最下行に不要な改行が入らないように修正
						<pre>タグ直後の改行は無視される為、予め改行をして置く(先頭行を改行した場合の対策)
				--%>
				
				<%-- No.827 2008/06/13 新実 フォントを改めて指定することで、フォントの変化を回避。 --%>
							<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinKensyoForm" property="cmtValKonkyo" /></font></pre>&nbsp;</DIV>

				</TD>
					</TR>
				</TABLE>
				</TD>
				<%-- 初回・中間月 End--%>

				<TD style="border:0px;width:10%;" class="center">
					<IMG src="<c:url value='/image/yajirushi.gif' />">
				</TD>
				
				<%-- 最終月Start --%>
				<TD style="border:0px;width:44%;text-align: right;vertical-align: top;">
					<%=i18n.get(GL.LABEL_TUUKA)%>&nbsp;
					<%=i18n.get(GL.LABEL_COLON)%>&nbsp;
					<bean:write name="HikiatekinKensyoForm" property="txtNameFinalTuuka" />
					<br>
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD colspan="1" rowspan="14" style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD colspan="1" rowspan="12" style="width: 7%;border-bottom-color: #666699;" class="inputRyuhoColor"><br>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_UKETORI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku01" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_YUSYUTSU_UKETORI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku02" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="vertical-align: top; text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_UTIKAKE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku03" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TORI_TOKIN)%><BR>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku04" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TATEKAE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku05" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_MISYUNYU)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku06" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_MISYUSYU)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku07" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TANKI_KASHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku08" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR>
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_SASHIIRE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku09" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_KARIBARAI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku10" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_TYOKI_KASHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku11" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%=i18n.get(GL.LABEL_SONOTA_TOSHI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku12" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%=i18n.get(GL.LABEL_IPANSAIKEN_KEI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalFinalSaikenIppan" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%=i18n.get(GL.LABEL_KOTEI_EIGYO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku16" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="3" class="inputThColor">
							<%=i18n.get(GL.LABEL_SAIKEN_ZAN)%><%=i18n.get(GL.LABEL_ONE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalFinalSaikenZandaka" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
				</TABLE>
				
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
				
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%=i18n.get(GL.LABEL_RESERVATION)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputOkColor">
							<%-- No588, 2008/06/05, SJA中島, 再計算アクション削除 --%>
							<%-- No647, 2008/06/06, SJA渡辺, 三桁でカンマ挿入実行修正 --%>
							<%-- No647, 2008/06/07, SJA渡辺, onblur,onkeyupを使用し三桁でカンマ挿入実行修正 --%>
							<%-- No647, 2008/06/07, SJA渡辺, onblur時に計算処理、三桁でカンマ挿入を実行、フォーカスが当たった場合値を全選択ように修正 --%>
							<html:text name="HikiatekinKensyoForm" property="finalRyuhosaimu" styleClass="inputOk" onfocus="setFocusId(this);select()" onblur="calcTotalFinalRyuhosaimu();doInsertComma()"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%=i18n.get(GL.LABEL_OP_RYUHO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputOkColor">
							<html:text name="HikiatekinKensyoForm" property="finalRyuhosaimu3" styleClass="inputOk" onfocus="setFocusId(this);select()" onblur="calcTotalFinalRyuhosaimu();doInsertComma()"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_RYUHO_SAIMU_KEI)%><%=i18n.get(GL.LABEL_TWO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="totalFinalRyuhosaimu" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_HOZEN)%><%=i18n.get(GL.LABEL_THREE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputOkColor">
							<html:text name="HikiatekinKensyoForm" property="finalHozen" styleClass="inputOk" onfocus="setFocusId(this);select()" onblur="calcFinalZandaka();doInsertComma()"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%=i18n.get(GL.LABEL_SONOTA_KAISYU)%><%=i18n.get(GL.LABEL_FOUR)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputOkColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingakuSonota" styleClass="inputOk" onfocus="setFocusId(this);select()" onblur="calcFinalZandaka();doInsertComma()"/>
						</TD>
					</TR>
				</TABLE>
				
				<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="1" colspan="2" style="border-bottom:none;"  class="inputThColor" >
						<%=i18n.get(GL.LABEL_HOSYO_SAIMU)%>
					</TD>
					<TD style="width: 40%; text-align: right;" class="inputNoColor">
						<html:text name="HikiatekinKensyoForm" property="finalKingaku14" readonly="true" styleClass="inputNo" tabindex="-1" />
					</TD>
				</TR>
				<TR>
					<TD style="width: 7%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
					</TD>
					<TD style="width: 47%; text-align: right; border:solid 1px #AAA;">
						<%=i18n.get(GL.LABEL_RIKO_SEIKYU)%><%=i18n.get(GL.LABEL_FIVE)%>
					</TD>
					<TD style="width: 40%; text-align: right;"  class="inputOkColor">
						<html:text name="HikiatekinKensyoForm" property="finalKingakuKenen" styleClass="inputOk" onfocus="setFocusId(this);select()" onblur="calcFinalZandaka();doInsertComma()"/>
					</TD>
				</TR>
				</TABLE>

				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_KIBIKIATE)%><%=i18n.get(GL.LABEL_SIX)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingaku15" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_HIKIATE_KENSYO_ZAN)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalZandaka" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_HIKIATE_HOSEI)%>
						</TD>
						<TD style="width: 40%; text-align: right;" >
							<html:text name="HikiatekinKensyoForm" property="finalKingakuHosei" styleClass="inputBlank" onfocus="setFocusId(this);select()" onblur="calcFinalKingakuAfterHosei();doInsertComma()"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_HOSEIGO_HIKIATE)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalKingakuAfterHosei" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					<%-- 案件No.D9059 項目削除 --%>
					<%-- 
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%=i18n.get(GL.LABEL_HOSEI_HIKIATE_KOJO)%>
						</TD>
						<TD style="width: 40%; text-align: right;" class="inputNoColor">
							<html:text name="HikiatekinKensyoForm" property="finalZnadakaAfterHosei" readonly="true" styleClass="inputNo" tabindex="-1" />
						</TD>
					</TR>
					--%>
				</TABLE>
				<BR>
				<DIV align="left">
					<%=i18n.get(GL.LABEL_HIKIATE_KONKYO)%>
				</DIV>
				<TABLE style="width:100%;border:0px;border-collapse: collapse;table-layout:fixed;" class="semaku">
					<TR style="border:0px;">
						<TD style="border:0px;width:100%;text-align:left;">
							<html:textarea name="HikiatekinKensyoForm" property="finalCmtValKonkyo" style="height:4em;width : 100%;" onfocus="setFocusId(this)" />
						</TD>
					</TR>
				</TABLE>
				<%-- 最終月End --%>
				</TD>
			</TR>
		</TABLE>
	</DIV>
</DIV>
</html:form>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>