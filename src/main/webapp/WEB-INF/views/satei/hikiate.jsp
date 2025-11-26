<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>
<link rel="stylesheet" href="../../../css/Satei.css" type="text/css">
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="HikiateForm" name="02HikiateForm" type="app.satei.form.HikiateForm" />

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
	<%-- 引当金検討対象と追加引当金額が異なる場合の確認処理 --%>
	function compareKingaku(event) {
		form = document.forms[0];

		if(form.elements["tuikahikiatekingaku"].value == null || form.elements["tuikahikiatekingaku"].value == ""){
			<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
				form.elements["tuikahikiatekingaku"].value = "0.00";
			</logic:notEqual>
			<logic:equal name="TorihikisakiBean" property="system_kbn" value="01">
				form.elements["tuikahikiatekingaku"].value = "0";
			</logic:equal>
		}
		if(form.elements["ippanFlg"].value == "true" || removeConma(form.elements["hikiatetaishokingaku"].value) == removeConma(form.elements["tuikahikiatekingaku"].value)) {
			<%--ボタン連打ブロック--%>
			if(blockSubmit()==false) return;

			action = form.action;
			form.target = "_top";
			form.action += "?<%=GS.EVENT%>=" + event;
			form.submit();
		} else if(removeConma(form.elements["hikiatetaishokingaku"].value) > removeConma(form.elements["tuikahikiatekingaku"].value)){
			if(window.confirm('<%=i18n.get(GL.CONFIRM_HIKIATEHUSOKU)%>')){
				<%--ボタン連打ブロック--%>
				if(blockSubmit()==false) return;

				action = form.action;
				form.target = "_top";
				form.action += "?<%=GS.EVENT%>=" + event;
				form.submit();
			}
		} else if(removeConma(form.elements["hikiatetaishokingaku"].value) < removeConma(form.elements["tuikahikiatekingaku"].value)){
			if(window.confirm('<%=i18n.get(GL.CONFIRM_HIKIATEKAJOU)%>')){
				<%--ボタン連打ブロック--%>
				if(blockSubmit()==false) return;

				action = form.action;
				form.target = "_top";
				form.action += "?<%=GS.EVENT%>=" + event;
				form.submit();
			}
		}
	}
	<%-- 保全計算 --%>
	function calcHozen() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["hyokagaku_hudousantanpo"].value));
		number2 = inputCheck(removeConma(form.elements["hyokagaku_dousantanpo"].value));
		number3 = inputCheck(removeConma(form.elements["hyokagaku_bouekihoken"].value));
		number4 = inputCheck(removeConma(form.elements["hyokagaku_sonota"].value));
		var result = number1 + number2 + number3 + number4;
		result = Math.floor((result * 100) + 0.5) / 100;
		form.elements["hozen"].value = result;
		calcHikiatetaisyo();
	}
	<%-- 第三者留保債務計算 --%>
	function calcOthRyuhosaimu() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["kingaku1"].value));
		number2 = inputCheck(removeConma(form.elements["kingaku2"].value));
		number3 = inputCheck(removeConma(form.elements["kingaku3"].value));
		var result = number1 + number2 + number3;
		result = Math.floor((result * 100) + 0.5) / 100;
		form.elements["othryuhosaimu"].value = result;
		calcTotalRyuhosaimu();
	}
	<%-- 引当金対象計算 --%>
	function calcHikiatetaisyo() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["saikenzandakagokei"].value));
		number2 = inputCheck(removeConma(form.elements["ryuhosaimukei"].value));
		number3 = inputCheck(removeConma(form.elements["hozen"].value));
		number4 = inputCheck(removeConma(form.elements["sonotakaishu"].value));
		number5 = inputCheck(removeConma(form.elements["rikoseikyukenen"].value));
		number6 = inputCheck(removeConma(form.elements["kibikiatekin"].value));
		var result = number1 - (number2 + number3 + number4) + number5 - number6;
		result = Math.floor((result * 100) + 0.5) / 100;
		form.elements["hikiatetaishokingaku"].value = result;
	}
	<%-- 追加引当金額(調整後)計算 --%>
	function calcTuikahikiatekingaku_after() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["tuikahikiatekingaku"].value));
		number2 = inputCheck(removeConma(form.elements["tukachosei"].value));
		var result = number1 + number2;
		result = Math.floor((result * 100) + 0.5) / 100;
		form.elements["tuikahikiatekingaku_after"].value = result;
	}
	<%-- 債権残高合計計算 --%>
	function calcSaikenzandakagokei() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["ippansaikenkei"].value));
		number2 = inputCheck(removeConma(form.elements["hanyo1"].value));
		var result = number1 + number2;
		result = Math.floor((result * 100) + 0.5) / 100;
		form.elements["saikenzandakagokei"].value = result;
		calcHikiatetaisyo();
	}
	<%-- 留保債務計計算 --%>
	function calcTotalRyuhosaimu() {
		form = document.forms[0];
		number1 = inputCheck(removeConma(form.elements["ryuhosaimu"].value));
		number2 = inputCheck(removeConma(form.elements["othryuhosaimu"].value));
		var result = number1 + number2;
		result = Math.floor((result * 100) + 0.5) / 100;
		var totalZandaka = inputCheck(removeConma(form.elements["saikenzandakagokei"].value));
		<%-- totalZandakaがマイナス値の場合、ryuhosaimukeiを0にする --%>
		if(result > totalZandaka) {
			if(0>totalZandaka){
				form.elements["ryuhosaimukei"].value = 0;
			}else{
				form.elements["ryuhosaimukei"].value = totalZandaka;
			}
		} else {
			form.elements["ryuhosaimukei"].value = result;
		}

		calcHikiatetaisyo();
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
	
	<%-- フォーカスの設定 --%>
	function setFocus(focus) {
		var form = document.forms[0];
		if(focus == 'null' || focus == '') {
		} else {
			form.elements[focus].focus();
		}
	}
	
	<%-- 三桁でカンマ挿入 --%>
	function insertComma(val) {
		var result = val;
		var tmp = "";
		while (result != (tmp = result.replace(/^([+-]?\d+)(\d\d\d)/,"$1,$2"))) {
			result = tmp;
		}
		return result;
	}
	
	<%-- 三桁でカンマ挿入実行 --%>
	function doInsertComma() {
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			addShosu();
		</logic:notEqual>
		form = document.forms[0];
		form.elements["hanyo1"].value = insertComma(removeConma(form.elements["hanyo1"].value));
		form.elements["ryuhosaimu"].value = insertComma(removeConma(form.elements["ryuhosaimu"].value));
		form.elements["othryuhosaimu"].value = insertComma(removeConma(form.elements["othryuhosaimu"].value));
		form.elements["hozen"].value = insertComma(removeConma(form.elements["hozen"].value));
		form.elements["sonotakaishu"].value = insertComma(removeConma(form.elements["sonotakaishu"].value));		
		form.elements["rikoseikyukenen"].value = insertComma(removeConma(form.elements["rikoseikyukenen"].value));
		form.elements["tuikahikiatekingaku"].value = insertComma(removeConma(form.elements["tuikahikiatekingaku"].value));
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tukachosei"].value = insertComma(removeConma(form.elements["tukachosei"].value));
		</logic:notEqual>
		form.elements["keiyakugaku_hudousantanpo"].value = insertComma(removeConma(form.elements["keiyakugaku_hudousantanpo"].value));
		form.elements["keiyakugaku_dousantanpo"].value = insertComma(removeConma(form.elements["keiyakugaku_dousantanpo"].value));
		form.elements["keiyakugaku_bouekihoken"].value = insertComma(removeConma(form.elements["keiyakugaku_bouekihoken"].value));
		form.elements["keiyakugaku_sonota"].value = insertComma(removeConma(form.elements["keiyakugaku_sonota"].value));
		form.elements["hyokagaku_hudousantanpo"].value = insertComma(removeConma(form.elements["hyokagaku_hudousantanpo"].value));
		form.elements["hyokagaku_dousantanpo"].value = insertComma(removeConma(form.elements["hyokagaku_dousantanpo"].value));
		form.elements["hyokagaku_bouekihoken"].value = insertComma(removeConma(form.elements["hyokagaku_bouekihoken"].value));
		form.elements["hyokagaku_sonota"].value = insertComma(removeConma(form.elements["hyokagaku_sonota"].value));

		form.elements["kingaku1"].value = insertComma(removeConma(form.elements["kingaku1"].value));
		form.elements["kingaku2"].value = insertComma(removeConma(form.elements["kingaku2"].value));
		form.elements["kingaku3"].value = insertComma(removeConma(form.elements["kingaku3"].value));
		
		form.elements["ippansaikenkei"].value = insertComma(removeConma(form.elements["ippansaikenkei"].value));
		form.elements["saikenzandakagokei"].value = insertComma(removeConma(form.elements["saikenzandakagokei"].value));
		form.elements["ryuhosaimukei"].value = insertComma(removeConma(form.elements["ryuhosaimukei"].value));
		form.elements["hikiatetaishokingaku"].value = insertComma(removeConma(form.elements["hikiatetaishokingaku"].value));
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tuikahikiatekingaku_after"].value = insertComma(removeConma(form.elements["tuikahikiatekingaku_after"].value));
		</logic:notEqual>
	}

	<%-- 三桁でカンマ挿入実行(オンロード時) --%>
	function onLoadInsertComma(){

		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			addShosu();
		</logic:notEqual>

		form = document.forms[0];
		form.elements["uketoritegata"].value = insertComma(form.elements["uketoritegata"].value);
		form.elements["yushutsuuketoritegata"].value = insertComma(form.elements["yushutsuuketoritegata"].value);
		form.elements["urikakekin"].value = insertComma(form.elements["urikakekin"].value);
		form.elements["torihikimaewatashikin"].value = insertComma(form.elements["torihikimaewatashikin"].value);
		form.elements["tatekaekin"].value = insertComma(form.elements["tatekaekin"].value);
		form.elements["mishunyukin"].value = insertComma(form.elements["mishunyukin"].value);
		form.elements["mishushueki"].value = insertComma(form.elements["mishushueki"].value);
		form.elements["tankikashitsukekin"].value = insertComma(form.elements["tankikashitsukekin"].value);
		form.elements["sashiirehosyokin"].value = insertComma(form.elements["sashiirehosyokin"].value);
		form.elements["karibaraikin"].value = insertComma(form.elements["karibaraikin"].value);
		form.elements["chokikashitsukekin"].value = insertComma(form.elements["chokikashitsukekin"].value);
		form.elements["sonotatousi"].value = insertComma(form.elements["sonotatousi"].value);
		form.elements["hoshosaimugokei"].value = insertComma(form.elements["hoshosaimugokei"].value);
		form.elements["kibikiatekin"].value = insertComma(form.elements["kibikiatekin"].value);

		form.elements["hanyo1"].value = insertComma(form.elements["hanyo1"].value);
		form.elements["ryuhosaimu"].value = insertComma(form.elements["ryuhosaimu"].value);
		form.elements["othryuhosaimu"].value = insertComma(form.elements["othryuhosaimu"].value);
		form.elements["hozen"].value = insertComma(form.elements["hozen"].value);
		form.elements["sonotakaishu"].value = insertComma(form.elements["sonotakaishu"].value);		
		form.elements["rikoseikyukenen"].value = insertComma(form.elements["rikoseikyukenen"].value);
		form.elements["tuikahikiatekingaku"].value = insertComma(form.elements["tuikahikiatekingaku"].value);
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tukachosei"].value = insertComma(form.elements["tukachosei"].value);
		</logic:notEqual>
		form.elements["keiyakugaku_hudousantanpo"].value = insertComma(form.elements["keiyakugaku_hudousantanpo"].value);
		form.elements["keiyakugaku_dousantanpo"].value = insertComma(form.elements["keiyakugaku_dousantanpo"].value);
		form.elements["keiyakugaku_bouekihoken"].value = insertComma(form.elements["keiyakugaku_bouekihoken"].value);
		form.elements["keiyakugaku_sonota"].value = insertComma(form.elements["keiyakugaku_sonota"].value);
		form.elements["hyokagaku_hudousantanpo"].value = insertComma(form.elements["hyokagaku_hudousantanpo"].value);
		form.elements["hyokagaku_dousantanpo"].value = insertComma(form.elements["hyokagaku_dousantanpo"].value);
		form.elements["hyokagaku_bouekihoken"].value = insertComma(form.elements["hyokagaku_bouekihoken"].value);
		form.elements["hyokagaku_sonota"].value = insertComma(form.elements["hyokagaku_sonota"].value);
		
		form.elements["ippansaikenkei"].value = insertComma(form.elements["ippansaikenkei"].value);
		form.elements["saikenzandakagokei"].value = insertComma(form.elements["saikenzandakagokei"].value);
		form.elements["ryuhosaimukei"].value = insertComma(form.elements["ryuhosaimukei"].value);
		form.elements["hikiatetaishokingaku"].value = insertComma(form.elements["hikiatetaishokingaku"].value);
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tuikahikiatekingaku_after"].value = insertComma(form.elements["tuikahikiatekingaku_after"].value);
		</logic:notEqual>
		form.elements["kingaku1"].value = insertComma(form.elements["kingaku1"].value);
		form.elements["kingaku2"].value = insertComma(form.elements["kingaku2"].value);
		form.elements["kingaku3"].value = insertComma(form.elements["kingaku3"].value);
	}

	<%-- 小数点以下付与 --%>
	function addShosu(){
		form = document.forms[0];
		form.elements["hanyo1"].value = setShosu(removeConma(form.elements["hanyo1"].value));
		form.elements["ryuhosaimu"].value = setShosu(removeConma(form.elements["ryuhosaimu"].value));
		form.elements["othryuhosaimu"].value = setShosu(removeConma(form.elements["othryuhosaimu"].value));
		form.elements["hozen"].value = setShosu(removeConma(form.elements["hozen"].value));
		form.elements["sonotakaishu"].value = setShosu(removeConma(form.elements["sonotakaishu"].value));		
		form.elements["rikoseikyukenen"].value = setShosu(removeConma(form.elements["rikoseikyukenen"].value));
		form.elements["tuikahikiatekingaku"].value = setShosu(removeConma(form.elements["tuikahikiatekingaku"].value));
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tukachosei"].value = setShosu(removeConma(form.elements["tukachosei"].value));
		</logic:notEqual>
		form.elements["keiyakugaku_hudousantanpo"].value = setShosu(removeConma(form.elements["keiyakugaku_hudousantanpo"].value));
		form.elements["keiyakugaku_dousantanpo"].value = setShosu(removeConma(form.elements["keiyakugaku_dousantanpo"].value));
		form.elements["keiyakugaku_bouekihoken"].value = setShosu(removeConma(form.elements["keiyakugaku_bouekihoken"].value));
		form.elements["keiyakugaku_sonota"].value = setShosu(removeConma(form.elements["keiyakugaku_sonota"].value));
		form.elements["hyokagaku_hudousantanpo"].value = setShosu(removeConma(form.elements["hyokagaku_hudousantanpo"].value));
		form.elements["hyokagaku_dousantanpo"].value = setShosu(removeConma(form.elements["hyokagaku_dousantanpo"].value));
		form.elements["hyokagaku_bouekihoken"].value = setShosu(removeConma(form.elements["hyokagaku_bouekihoken"].value));
		form.elements["hyokagaku_sonota"].value = setShosu(removeConma(form.elements["hyokagaku_sonota"].value));

		form.elements["kingaku1"].value = setShosu(removeConma(form.elements["kingaku1"].value));
		form.elements["kingaku2"].value = setShosu(removeConma(form.elements["kingaku2"].value));
		form.elements["kingaku3"].value = setShosu(removeConma(form.elements["kingaku3"].value));
		
		form.elements["ippansaikenkei"].value = setShosu(removeConma(form.elements["ippansaikenkei"].value));
		form.elements["saikenzandakagokei"].value = setShosu(removeConma(form.elements["saikenzandakagokei"].value));
		form.elements["ryuhosaimukei"].value = setShosu(removeConma(form.elements["ryuhosaimukei"].value));
		form.elements["hikiatetaishokingaku"].value = setShosu(removeConma(form.elements["hikiatetaishokingaku"].value));
		<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
			form.elements["tuikahikiatekingaku_after"].value = setShosu(removeConma(form.elements["tuikahikiatekingaku_after"].value));
		</logic:notEqual>
	}
	
	<%-- 小数点以下付与 --%>
	function setShosu(str){
		var result = str;
		if (result.match(/(^[-+]?\d+$)/)) {
		    result = result + "\.00";
		}else if(result.match(/(^[-+]?\d+\.$)/)){
		    result = result + "00";
		}else if(result.match(/(^[-+]?\d+\.\d$)/)){
		    result = result + "0";
		}
		return result;
	}
</script>
</HEAD>
<BODY onload="onLoadInsertComma();setFocus('<%= request.getAttribute(GS.FOCUS_FIELD) %>')">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">

<DIV id="head">
	<IMG alt="Sojitz" src="../../../image/navi001.gif" width="89" height="52">
	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="../image/<%=i18n.get(GL.IMG_TITLE)%>.gif" height="54">
	<%-- ヘルプリンク --%>
	<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<logic:equal name="TorihikisakiBean" property="phase" value="40">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1104A)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="50">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1104B)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="60">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1104C)%></H1>
</logic:equal>
<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%>
<BR><BR><BR>
<%}%>

<html:form action="/satei/hikiate">
<html:hidden property="ippanFlg" />

<DIV id="submenu">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
	<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="compareKingaku('forward')">
	<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="compareKingaku('sendBack')">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="compareKingaku('firstPreserve')">
	<input type="button" value="<%=i18n.get(GL.BTN_TOP)%>" onclick="compareKingaku('toHead')">
	<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="compareKingaku('register')">
	<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('back')"><BR>
	<DIV align="right">
	<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')">
	<input type="button" value="<%=i18n.get(GL.BTN_CL)%>" onclick="compareKingaku('claimDetail')">
	<input type="button" value="<%=i18n.get(GL.BTN_RESERVATION)%>" onclick="compareKingaku('reserveDebt')">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPUSENTAKU)%>" onclick="compareKingaku('appendSelection')">
	<%} else {%>
	<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="compareKingaku('forward')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="compareKingaku('sendBack')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="compareKingaku('firstPreserve')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TOP)%>" onclick="compareKingaku('toHead')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="compareKingaku('register')" style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('back')" style="WIDTH: 115px; HEIGHT: 22px"><BR>
	<DIV align="right">
	<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')"  style="WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_CL)%>" onclick="compareKingaku('claimDetail')" style="font-size:10px; WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_RESERVATION)%>" onclick="compareKingaku('reserveDebt')" style="font-size:10px;WIDTH: 115px; HEIGHT: 22px">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPUSENTAKU)%>" onclick="compareKingaku('appendSelection')" style="WIDTH: 115px; HEIGHT: 22px"><BR><BR>
	<%}%>
	</DIV>
</DIV>
<DIV id="list">
<DIV class="mainlist">
	<TABLE style="border:0px;width:100%;">
		<TR class="OC1104BorderNone">
		<TD style="width:10%;" class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_KANJO)%></DIV><BR>
		</TD>
		<TD style="width:12%;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write  name="TorihikisakiBean" property="kanjo_cd" /></DIV><BR>
		</TD>
		<TD style="width:4%;"class="semakuBorderNone"><BR>
		</TD>
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_KANJO_NM)%></DIV><BR>
		</TD>
		<TD colspan="3" style="width:64%;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV><BR>
		</TD>
		</TR>
	</TABLE>

	<DIV class="right">
		<logic:equal name="HikiateForm" property="shoninshaListFlg" value="1">
			<%=i18n.get(GL.OC1104_SHONINSHA)%>
			<html:select name="HikiateForm" property="shoninsha" style="width:200">
				<html:optionsCollection name="HikiateForm" property="shoninList" value="key" label="value" />
  			</html:select><BR>
		</logic:equal>
	</DIV>
<%-- 第三者留保債務のIMEコントロール制御 --%>
<%
String oth_kanjo_cd = "text-align: left;ime-mode:inactive";
String oth_kanjo_nm = "text-align: left;ime-mode:active";
String oth_kanjo_kamoku = "text-align: left;ime-mode:active";
if(SESSION_DATA_APP.getComLangMode().equals("En")) {
	oth_kanjo_nm = "text-align: left;ime-mode:inactive";
	oth_kanjo_kamoku = "text-align: left;ime-mode:inactive";
}
%>
	<TABLE style="width:100%;height:750px;table-layout:fixed;"class="semakuBorderNone">
		<TR class="OC1104BorderNone">
		<TD style="width:49%;vertical-align:top;" class="semakuBorderNone">
			<TABLE style="border:0px;width:100%;border-collapse: collapse;">
				<TR class="OC1104BorderNone">
					<TD class="semakuBorderNone">
						[&nbsp;<%=i18n.get(GL.OC1104_HIKIATEHANTEI)%>&nbsp;]
					</TD>
					<TD style="text-align: right;"class="semakuBorderNone">
						<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;<bean:write name="HikiateForm" property="tani" />
					</TD>
				</TR>
			</TABLE>
			
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;"class="semaku">
    <TR class="OC1104BorderNone">
      <TD colspan="1" rowspan="14" style="width: 7%;border-bottom-color: #330088;" class="inputThColor "><br>
      </TD>
      <TD colspan="1" rowspan="12" style="width: 7%;border-bottom-color: #666699;" class="inputRyuhoColor "><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="inputKanjoColor "><%=i18n.get(GL.OC1104_UKETORI_TEGATA)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="uketoritegata" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor "><%=i18n.get(GL.OC1104_YUSHUTU_UKETORI_TEGATA)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="yushutsuuketoritegata" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="vertical-align: top; text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_URIKAKE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="urikakekin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_TORIHIKI_MAEWATASHI_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="torihikimaewatashikin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_TATEKAE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="tatekaekin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align:right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_MISHUUNYUU_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="mishunyukin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_MISHUU_SHUUEKI)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="mishushueki" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_TANKI_KASHITUKE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="tankikashitsukekin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_SASHIIRE_HOSHOU_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="sashiirehosyokin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_KARIBARAI_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="karibaraikin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_CHOUKI_KASHITUKE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="chokikashitsukekin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_SONOTA_TOUSHI)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="sonotatousi" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD rowspan="1" colspan="2" class="inputRyuhoColor"><%=i18n.get(GL.OC1104_IPPAN_SAIKEN_KEI)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
  	<html:text name="HikiateForm" property="ippansaikenkei" maxlength="15" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" onfocus="select()" onblur="calcSaikenzandakagokei();doInsertComma()" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD rowspan="1" colspan="2" class="inputRyuhoColor"><%=HikiateForm.getHanyo1title()%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getHanyo1StyleTD()%>">
      	<html:text name="HikiateForm" property="hanyo1" maxlength="15" readonly="<%=HikiateForm.isHanyo1Read()%>" tabindex="<%=HikiateForm.getHanyo1Tab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHanyo1Style()%>" onfocus="select()" onblur="calcSaikenzandakagokei();doInsertComma()" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD rowspan="1" colspan="3" class="inputThColor"><%=i18n.get(GL.OC1104_SAIKEN_ZANDAKA_GOUKEI_1)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="saikenzandakagokei" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
</TABLE>


<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;"class="semaku">

    <TR class="OC1104BorderNone">
      <TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
      </TD>
      <TD style="width: 47%; text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_RYUUHO_SAIMU)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getRyuhosaimuStyleTD()%>">
      	<html:text name="HikiateForm" property="ryuhosaimu" maxlength="15" readonly="<%=HikiateForm.isRyuhosaimuRead()%>" tabindex="<%=HikiateForm.getRyuhosaimuTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getRyuhosaimuStyle()%>" onfocus="select()" onblur="calcTotalRyuhosaimu();doInsertComma()"/>
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
      </TD>
      <TD style="width: 47%; text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_NO3_RYUUHO_SAIMU)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getOthRyuhosaimuStyleTD()%>">
      	<html:text name="HikiateForm" property="othryuhosaimu" maxlength="15" readonly="<%=HikiateForm.isOthRyuhosaimuRead()%>" tabindex="<%=HikiateForm.getOthRyuhosaimuTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getOthRyuhosaimuStyle()%>" onfocus="select()" onblur="calcTotalRyuhosaimu();doInsertComma()"/>
      </TD>
    </TR>
    <TR>
      <TD rowspan="1" colspan="2" class="inputThColor"><%=i18n.get(GL.OC1104_RYUUHO_SAIMU_KEI_2)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="ryuhosaimukei" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD rowspan="1" colspan="2" class="inputThColor"><%=i18n.get(GL.OC1104_HOZEN_3)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getHozenStyleTD()%>">
      	<html:text name="HikiateForm" property="hozen" maxlength="15" readonly="<%=HikiateForm.isHozenRead()%>" tabindex="<%=HikiateForm.getHozenTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenStyle()%>" onfocus="select()" onblur="calcHikiatetaisyo();doInsertComma()"/>
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD rowspan="1" colspan="2" class="inputThColor"><%=i18n.get(GL.OC1104_SONOTA_KAISHUU_4)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getSonotakaishuStyleTD()%>">
      	<html:text name="HikiateForm" property="sonotakaishu" maxlength="15" readonly="<%=HikiateForm.isSonotakaishuRead()%>" tabindex="<%=HikiateForm.getSonotakaishuTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getSonotakaishuStyle()%>" onfocus="select()" onblur="calcHikiatetaisyo();doInsertComma()"/>
      </TD>
    </TR>
</TABLE>

<TABLE style="width:100%;border-collapse: collapse;border-left-color: #AAA;border-left-width: 1px;">
	<TR>
		<TD rowspan="1" colspan="2" style="border-bottom:none;"class="inputThColor">
			<%=i18n.get(GL.OC1104_HOSHOU_SAIMU_GOUKEI)%>
		</TD>
		<TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
			<html:text name="HikiateForm" property="hoshosaimugokei" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
		</TD>
	</TR>
	<TR>
		<TD style="width: 7%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
		</TD>
		<TD style="width: 47%; text-align: right; border:solid 1px #AAA;"class="inputKanjoColor">
			<%=i18n.get(GL.OC1104_RIKOU_SEIKYUU_KENEN_5)%>
		</TD>
		<TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getRikoseikyukenenStyleTD()%>">
			<html:text name="HikiateForm" property="rikoseikyukenen" maxlength="15" readonly="<%=HikiateForm.isRikoseikyukenenRead()%>" tabindex="<%=HikiateForm.getRikoseikyukenenTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getRikoseikyukenenStyle()%>" onfocus="select()" onblur="calcHikiatetaisyo();doInsertComma()"/>
		</TD>
	</TR>
</TABLE>

<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;"class="semaku">
    <TR class="OC1104BorderNone">
      <TD style="width: 54%;" class="inputThColor"><%=i18n.get(GL.OC1104_KI_HIKIATE_KIN_6)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	
      	<html:text name="HikiateForm" property="kibikiatekin" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="width: 54%;" class="inputThColor"><%=i18n.get(GL.OC1104_HIKIATE_TAISHOU_KINGAKU)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	
      	<html:text name="HikiateForm" property="hikiatetaishokingaku" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO" />
      </TD>
    </TR>
</TABLE>
		<%=i18n.get(GL.OC1104_HIKIATE_BANGO)%>
<BR>
<logic:equal name="TorihikisakiBean" property="system_kbn" value="01">
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;"class="semaku">
    <TR class="OC1104BorderNone">
      <TD style="width: 54%;" class="inputThColor"><%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getTuikahikiatekingakuStyleTD()%>">
      	<html:text name="HikiateForm" property="tuikahikiatekingaku" maxlength="15" readonly="<%=HikiateForm.isTuikahikiatekingakuRead()%>" tabindex="<%=HikiateForm.getTuikahikiatekingakuTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getTuikahikiatekingakuStyle()%>" onfocus="select()" onblur="doInsertComma()" />
      </TD>
    </TR>
</TABLE>
</logic:equal>
<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;"class="semaku">
    <TR class="OC1104BorderNone">
      <TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
      </TD>
      <TD style="width: 47%; text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getTuikahikiatekingakuStyleTD()%>">
      	<html:text name="HikiateForm" property="tuikahikiatekingaku" maxlength="15" readonly="<%=HikiateForm.isTuikahikiatekingakuRead()%>" tabindex="<%=HikiateForm.getTuikahikiatekingakuTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getTuikahikiatekingakuStyle()%>" onfocus="select()" onblur="calcTuikahikiatekingaku_after();doInsertComma()" />
      </TD>
    </TR>
    <TR class="OC1104BorderNone">
      <TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
      </TD>
      <TD style="width: 47%; text-align: right;" class="inputKanjoColor"><%=i18n.get(GL.OC1104_TUUKA_CHOUSEI)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="<%=HikiateForm.getTukachoseiStyleTD()%>">
      	<html:text name="HikiateForm" property="tukachosei" maxlength="15" readonly="<%=HikiateForm.isTukachoseiRead()%>" tabindex="<%=HikiateForm.getTukachoseiTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getTukachoseiStyle()%>" onfocus="select()" onblur="calcTuikahikiatekingaku_after();doInsertComma()" />
      </TD>
    </TR>
    <TR>
      <TD rowspan="1" colspan="2" class="inputThColor"><%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN_CHOUSEIGO)%><br>
      </TD>
      <TD style="width: 40%; text-align: right;" class="OC1104_TD_MIZUIRO">
      	<html:text name="HikiateForm" property="tuikahikiatekingaku_after" readonly="true" tabindex="-1" style="text-align: right;" styleClass="OC1104_TXT_MIZUIRO"/>
      </TD>
    </TR>
</TABLE>
</logic:notEqual>
		</TD>
		<TD style="width:100%;vertical-align:top;"class="semakuBorderNone">
			<TABLE style="border:0px;width:100%;border-collapse: collapse;table-layout:fixed;">
				<TR class="OC1104BorderNone">
					<TD style="width:70%;"class="semakuBorderNone">
						[&nbsp;<%=i18n.get(GL.OC1104_NO3_RYUUHO_SAIMU_UTIWAKE)%>&nbsp;]
					</TD>
					<TD style="text-align: right;width:30%;"class="semakuBorderNone">
						<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;<bean:write name="HikiateForm" property="tani" />
					</TD>
				</TR>
			</TABLE>
	<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;" class="semaku">
		<TR class="OC1104BorderNone" >
			<TD class="inputThColor"><%=i18n.get(GL.OC1104_KANJO_CD)%><BR>
			</TD>
			<TD class="inputThColor" style="width:25%;"><%=i18n.get(GL.OC1104_TORIHIKISAKI_NM)%><BR>
			</TD>
			<TD class="inputThColor" style="width:25%;"><%=i18n.get(GL.OC1104_KANJOKAMOKU)%><BR>
			</TD>
			<TD class="inputThColor" style="width:25%;"><%=i18n.get(GL.OC1104_KINGAKU)%><BR>
			</TD>
		</TR>
		<TR class="OC1104BorderNone">
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_cd1" maxlength="12" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_cd%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="tori_nm1" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_nm%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_kamoku1" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_kamoku%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kingaku1" maxlength="15" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="text-align: right;" onfocus="select()" onblur="calcOthRyuhosaimu();doInsertComma()"/></TD>
		</TR>
		<TR class="OC1104BorderNone">
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_cd2" maxlength="12" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_cd%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="tori_nm2" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_nm%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_kamoku2" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_kamoku%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kingaku2" maxlength="15" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="text-align: right;" onfocus="select()" onblur="calcOthRyuhosaimu();doInsertComma()"/></TD>
		</TR>
		<TR class="OC1104BorderNone">
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_cd3" maxlength="12" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_cd%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="tori_nm3" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_nm%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kanjo_kamoku3" maxlength="80" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="<%=oth_kanjo_kamoku%>" /></TD>
			<TD class="<%=HikiateForm.getOthryuhosaimunaiyakuStyleTD()%>"><html:text name="HikiateForm" property="kingaku3" maxlength="15" readonly="<%=HikiateForm.isOthryuhosaimunaiyakuRead()%>" tabindex="<%=HikiateForm.getOthryuhosaimunaiyakuTab()%>" styleClass="<%=HikiateForm.getOthryuhosaimunaiyakuStyle()%>" style="text-align: right;" onfocus="select()" onblur="calcOthRyuhosaimu();doInsertComma()"/></TD>
		</TR>
	</TABLE>
	<BR>
			<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;"class="semakuBorderNone">
				<TR class="OC1104BorderNone">
					<TD style="width:55%;"class="semakuBorderNone">
						[&nbsp;<%=i18n.get(GL.OC1104_HOZEN)%>&nbsp;]
					</TD>
					<TD style="text-align: right;width:45%;"class="semakuBorderNone">
						<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;<bean:write name="HikiateForm" property="tani" />
					</TD>
				</TR>
			</TABLE>
			
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;border-left-width: 1px;">
	<TR class="OC1104BorderNone">
		<TD class="inputThColor" style="width:16%;"><BR>
		</TD>
		<TD class="inputThColor" style="width:21%;"><%=i18n.get(GL.OC1104_HUDOUSAN_TANPO)%><BR>
		</TD>
		<TD class="inputThColor" style="width:21%;"><%=i18n.get(GL.OC1104_DOSAN_TANPO)%><BR>
		</TD>
		<TD class="inputThColor" style="width:21%;"><%=i18n.get(GL.OC1104_BOEKI_HOKEN)%><BR>
		</TD>
		<TD class="inputThColor" style="width:21%;"><%=i18n.get(GL.OC1104_SONOTA)%><BR>
		</TD>
	</TR>
	<TR class="OC1104BorderNone">
		<TD class="inputThColor"><%=i18n.get(GL.OC1104_KEIYAKU_GAKU)%><BR>
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="keiyakugaku_hudousantanpo" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="doInsertComma()" />
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="keiyakugaku_dousantanpo" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="doInsertComma()" />
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="keiyakugaku_bouekihoken" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="doInsertComma()" />
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="keiyakugaku_sonota" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="doInsertComma()" />
		</TD>
	</TR>
	<TR style="border:0px;" class="inputThColor">
		<TD><%=i18n.get(GL.OC1104_HYOKA_GAKU)%><BR>
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="hyokagaku_hudousantanpo" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="calcHozen();doInsertComma()"/>
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="hyokagaku_dousantanpo" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="calcHozen();doInsertComma()"/>
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="hyokagaku_bouekihoken" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="calcHozen();doInsertComma()"/>
		</TD>
		<TD class="<%=HikiateForm.getHozenHyoStyleTD()%>">
			<html:text name="HikiateForm" property="hyokagaku_sonota" maxlength="15" readonly="<%=HikiateForm.isHozenHyoRead()%>" tabindex="<%=HikiateForm.getHozenHyoTab()%>" style="text-align: right;" styleClass="<%=HikiateForm.getHozenHyoStyle()%>" onfocus="select()" onblur="calcHozen();doInsertComma()"/>
		</TD>
	</TR>
</TABLE>
<BR>
	<TABLE style="border:0px;width:40%;table-layout:fixed;">
	<TR class="OC1104BorderNone">
	<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<TD style="width:5%;"class="semakuBorderNone">
	<%}else{%>
		<TD style="width:15%;"class="semakuBorderNone">
	<%}%>
			<DIV class="dottitle"><%=i18n.get(GL.OC1104_TASYA_RISUKU)%></DIV>
		</TD>
		<TD style="width:6%;"class="semakuBorderNone">
			<html:checkbox name="HikiateForm" property="othriskflg" value="1" disabled="<%=HikiateForm.isOthriskflgRead()%>" tabindex="<%=HikiateForm.getOthriskflgTab()%>"/>
		</TD>
		</TR>
	</TABLE>
<BR>
<%=i18n.get(GL.OC1104_ETC_NAIYO)%><BR>
<TABLE style="border:0px;width:100%;table-layout:fixed;">
	<TR class="OC1104BorderNone">
		<TD style="height:4em;"class="semakuBorderNone">
			<html:textarea name="HikiateForm" property="sonotanonaiyo" readonly="<%=HikiateForm.isCommentRuiRead()%>" tabindex="<%=HikiateForm.getCommentRuiTab()%>" styleClass="<%=HikiateForm.getCommentRuiStyle()%>" rows="3"/>
		</TD>
	</TR>
</TABLE>
<BR>
<%=i18n.get(GL.OC1104_ETC_KAISYU_NAIYO)%><BR>
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;"class="semakuBorderNone">
	<TR class="OC1104BorderNone">
		<TD style="height:4em;"class="semakuBorderNone">
			<html:textarea name="HikiateForm" property="sonotakaishunonaiyo" readonly="<%=HikiateForm.isCommentRuiRead()%>" tabindex="<%=HikiateForm.getCommentRuiTab()%>" styleClass="<%=HikiateForm.getCommentRuiStyle()%>" rows="3"/>
		</TD>
	</TR>
</TABLE>
<BR>
<%=i18n.get(GL.OC1104_RIKO_NAIYOU)%><BR>
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;"class="semakuBorderNone">
	<TR class="OC1104BorderNone">
		<TD style="height:4em;" class="semakuBorderNone">
			<html:textarea name="HikiateForm" property="rikoseikyukenennonaiyosetumei" readonly="<%=HikiateForm.isRikoseikyukenennonaiyosetumeiRead()%>" tabindex="<%=HikiateForm.getRikoseikyukenennonaiyosetumeiTab()%>" styleClass="<%=HikiateForm.getRikoseikyukenennonaiyosetumeiStyle()%>" rows="3"/>
		</TD>
	</TR>
</TABLE>
<BR>
<%=i18n.get(GL.OC1104_HIKIATE_NAIYO)%><BR>
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;"class="semakuBorderNone">
	<TR class="OC1104BorderNone">
		<TD style="height:5.5em;"class="semakuBorderNone">
			<html:textarea name="HikiateForm" property="hikiatekinsanteikonkyononaiyosetumei" readonly="<%=HikiateForm.isCommentRuiRead()%>" tabindex="<%=HikiateForm.getCommentRuiTab()%>" styleClass="<%=HikiateForm.getCommentRuiStyle()%>" rows="4"/>
		</TD>
	</TR>
</TABLE>
<BR>
<%=i18n.get(GL.OC1104_KONGO_MITOSHI)%><BR>
<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;"class="semakuBorderNone">
	<TR class="OC1104BorderNone">
		<TD style="height:9em;"class="semakuBorderNone">
			<%-- 課題No.90 コメント｢今後の回収見通しなど｣仕様変更 --%>
			<%-- 修正開始 --%>
			<%-- <html:textarea name="HikiateForm" property="kongonokaishumitoshi" readonly="<%=HikiateForm.isCommentRuiRead()%>" tabindex="<%=HikiateForm.getCommentRuiTab()%>" styleClass="<%=HikiateForm.getCommentRuiStyle()%>" rows="6" /> --%>
			<html:textarea name="HikiateForm" property="kongonokaishumitoshi" readonly="false" tabindex="0" styleClass="OC1104_TXTA_SIRO" rows="6" />
			<%-- 修正完了 --%>
		</TD>
	</TR>
</TABLE>
		</TD>
		</TR>
	</TABLE>
<%if("1".equals(HikiateForm.getShihankichushutsukomokuFlg())) {%>
	<TABLE style="border:0px;width:100%;table-layout:fixed;">
		<TR class="OC1104BorderNone">
		<TD style="width:15%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_SIHANKI_FLG)%></DIV></TD>
		<TD style="width:6%;"class="semakuBorderNone">
			<html:checkbox name="HikiateForm" property="shihankichushutsuflg" value="1" disabled="<%=HikiateForm.isShihankichushutsuflgRead()%>" tabindex="<%=HikiateForm.getShihankichushutsuflgTab()%>" onclick="doSubmit('shihankiHyouji')" />
		</TD>
		<TD style="width:09%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_FLG_KBN)%></DIV></TD>
		<TD colspan=2 style="width:70%;"class="semakuBorderNone">
			<html:select property="flgkbn" disabled="<%=HikiateForm.isFlgkbnRead()%>" tabindex="<%=HikiateForm.getFlgkbnTab()%>" >
    		<html:optionsCollection name="HikiateForm" property="flgKbnList" value="value" label="key" />
	  		</html:select>
		</TD>
		</TR>
		<TR class="OC1104BorderNone">
		<TD style="width:15%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_FLG_COMMENT)%></DIV></TD>
		<TD colspan=4 style="width:85%;"class="semakuBorderNone"></TD>
		</TR>
		<TR class="OC1104BorderNone">
		<TD colspan=5 style="height:6em;"class="semakuBorderNone">
			<html:textarea name="HikiateForm" property="flgcomment" readonly="<%=HikiateForm.isFlgCommentRead()%>" tabindex="<%=HikiateForm.getFlgCommentTab()%>" styleClass="<%=HikiateForm.getFlgCommentStyle()%>" rows="6" />
		</TD>
		</TR>
	</TABLE>
<%}else if(!"1".equals(HikiateForm.getShihankichushutsukomokuFlg()) && "1".equals(TorihikisakiBean.getHanki_sihanki_kbn())){%>
	<TABLE style="border:0px;width:100%;table-layout:fixed;">
		<TR class="OC1104BorderNone">
		<TD style="width:15%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1104_SIHANKI_FLG)%></DIV></TD>
		<TD style="width:6%;"class="semakuBorderNone">
			<html:checkbox name="HikiateForm" property="shihankichushutsuflg" value="1" disabled="<%=HikiateForm.isShihankichushutsuflgRead()%>" tabindex="<%=HikiateForm.getShihankichushutsuflgTab()%>" onclick="doSubmit('shihankiHyouji')" />
		</TD>
		<TD style="width:79%;"class="semakuBorderNone"></TD>
		</TR>
	</TABLE>
<%}%>
<DIV/>
<DIV/>
</DIV>
</DIV>
</html:form>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>