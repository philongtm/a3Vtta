<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>
<link rel="stylesheet" href="<c:url value='/css/Satei.css' />" type="text/css">
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="TorokuForm" name="02TorokuForm" type="app.satei.form.TorokuForm" />
<script>
	function setFocus(focus) {
		var form = document.forms[0];
		if(focus == 'null' || focus == '') {
		} else {
			form.elements[focus].focus();
		}
	}

	<%-- 小数点以下付与 --%>
	function addShosu(){
		form = document.forms[0];
		form.elements["hiritu1"].value = setShosu(form.elements["hiritu1"].value);
		form.elements["hiritu2"].value = setShosu(form.elements["hiritu2"].value);
		form.elements["hiritu3"].value = setShosu(form.elements["hiritu3"].value);
		form.elements["hiritu4"].value = setShosu(form.elements["hiritu4"].value);
		form.elements["hiritu5"].value = setShosu(form.elements["hiritu5"].value);
	}
	
	<%-- 課題No.33 保有株数カンマ編集 --%>
	<%-- 追加開始 --%>
	function doInsertComma(){
		form = document.forms[0];
		form.elements["kabusu1"].value = insertComma(removeConma(form.elements["kabusu1"].value));
		form.elements["kabusu2"].value = insertComma(removeConma(form.elements["kabusu2"].value));
		form.elements["kabusu3"].value = insertComma(removeConma(form.elements["kabusu3"].value));
		form.elements["kabusu4"].value = insertComma(removeConma(form.elements["kabusu4"].value));
		form.elements["kabusu5"].value = insertComma(removeConma(form.elements["kabusu5"].value));
	}
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
	function insertComma(val) {
		var result = val;
		var tmp = "";
		while (result != (tmp = result.replace(/^([+-]?\d+)(\d\d\d)/,"$1,$2"))) {
			result = tmp;
		}
		return result;
	}
	<%-- 追加完了 --%>
	
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
<%-- 課題No.33 保有株数カンマ編集 --%>
<%-- 修正開始 --%>
<%-- <BODY onload="addShosu();setFocus('<%= request.getAttribute(GS.FOCUS_FIELD) %>')"> --%>
<BODY onload="addShosu();doInsertComma();setFocus('<%= request.getAttribute(GS.FOCUS_FIELD) %>')">
<%-- 修正完了 --%>
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
<logic:equal name="TorihikisakiBean" property="phase" value="40">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1102A)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="50">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1102B)%></H1>
</logic:equal>
<logic:equal name="TorihikisakiBean" property="phase" value="60">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OC1102C)%></H1>
</logic:equal>

<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%>
<BR><BR><BR>
<%}%>
<html:form action="/satei/toroku">

<DIV id="submenu">
	<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('release')">
	<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('forward')">
	<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sendBack')">
	<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('firstPreserve')">
	<input type="button" value="<%=i18n.get(GL.BTN_NEXT_SCREEN)%>" onclick="doSubmit('next')">
	<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('back')">
	<BR><BR>
	<logic:equal name="TorihikisakiBean" property="sasi_ten_flg" value="1">
		<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('commentLink')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
	</logic:equal>
	<logic:equal name="TorihikisakiBean" property="sasi_ten_flg" value="2">
		<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('commentLink')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
	</logic:equal>
</DIV>
<DIV id="list">
<DIV class="mainlist">
	<TABLE class="OC1102contents1" >
		<TR class="OC1102BorderNone">
		<TD style="width:8%;" class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_KANJO)%></DIV>
		</TD>
		<TD style="width:15%;" class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
		</TD>
		<TD style="width:5%;" class="semakuBorderNone">
		</TD>
		<TD style="width:9%;" class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_KANJO_NM)%></DIV>
		</TD>
		<TD style="width:31%;text-align:left;" class="OC1102BorderNone semaku"><DIV class="ReadOnlybox" style="width:95%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV>
		</TD>
		<TD style="width:7%;" class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_SYOZAI_COUNTRY)%></DIV>
		</TD>
		<TD style="width:25%;" class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="syozaikoku" /></DIV>
		</TD>
		</TR>
		<TR class="OC1102BorderNone">
		<TD class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_DUNS_NO)%></DIV><BR>
		</TD>
		<TD colspan="2" style="text-align:left;" class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="TorihikisakiBean" property="togo_tori_cd" /><BR>
		</TD>
		<TD class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_SYOZAI_CHI)%></DIV><BR>
		</TD>
		<TD colspan="3" style="" class="semakuBorderNone">
			<DIV class="ReadOnlybox" style="width:100%;margin-bottom: 2px;"><bean:write name="TorihikisakiBean" property="syozaichi" /></DIV><BR>
		</TD>
		</TR>
		<TR class="OC1102BorderNone">
		<TD class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_GYOSYU)%></DIV><BR>
		</TD>
		<TD colspan="4" class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorokuForm" property="sicSm" /></DIV><BR>
		</TD>
		</TR>
		<TR class="OC1102BorderNone">
		<TD class="semakuBorderNone"><%=i18n.get(GL.OC1102_JIGYO)%><BR>
		</TD>
		<TD colspan="6" class="semakuBorderNone">
			<html:text name="TorokuForm" property="jigyoNaiyo" style="width:100%" maxlength="80" styleClass="doubleByte"/><BR>
		</TD>
		</TR>
	</TABLE>
	
<TABLE class="OC1102contents2">
	<TABLE class="OC1102contents2">
		<TR>
		<TD style="width:9%;" class="OC1102semaku"><%=i18n.get(GL.OC1102_KABUNUSHI_KOUSEI)%><br></TD>
		<TD style="width:44%;" class="OC1102semaku">
			<TABLE class="OC1102contents2tbl">
    			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><%=i18n.get(GL.OC1102_KABUNUSHI_NM)%><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp"><%=i18n.get(GL.OC1102_HOYUU_KABUSUU)%><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><%=i18n.get(GL.OC1102_HOYUU_RITU)%><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><br>
      				</TD>
      			</TR>
    			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_1)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabunusiNm1" style="width : 100%;padding-left:2px;" maxlength="60" styleClass="doubleByte"/><br>
      				</TD>
					<%-- 課題No.33 保有株数カンマ編集 --%>
					<%-- 修正開始 --%>
					<%-- <TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu1" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right"/><br> --%>
      				<TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu1" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right" onfocus="select()" onblur="doInsertComma()" /><br>
					<%-- 修正完了 --%>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="hiritu1" style="width : 100%;padding-right:2px;" maxlength="6" styleClass="right" onblur="addShosu()"/><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_2)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabunusiNm2" style="width : 100%;padding-left:2px;" maxlength="60" styleClass="doubleByte"/><br>
      				</TD>
					<%-- 課題No.33 保有株数カンマ編集 --%>
					<%-- 修正開始 --%>
					<%-- <TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu2" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right"/><br> --%>
      				<TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu2" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right" onfocus="select()" onblur="doInsertComma()" /><br>
					<%-- 修正完了 --%>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="hiritu2" style="width : 100%;padding-right:2px;" maxlength="6" styleClass="right" onblur="addShosu()"/><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
     		 </TABLE>
		</TD>
		
		<TD style="width:44%;" class="OC1102semaku">
			<TABLE class="OC1102contents2tbl">
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_3)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabunusiNm3" style="width : 100%;padding-left:2px;" maxlength="60" styleClass="doubleByte"/><br>
      				</TD>
					<%-- 課題No.33 保有株数カンマ編集 --%>
					<%-- 修正開始 --%>
					<%-- <TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu3" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right"/><br> --%>
      				<TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu3" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right" onfocus="select()" onblur="doInsertComma()" /><br>
					<%-- 修正完了 --%>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="hiritu3" style="width : 100%;padding-right:2px;"  maxlength="6" styleClass="right" onblur="addShosu()"/><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
    			<TR class="OC1102contentsMp">
      				<TD style="width:4%" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_4)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabunusiNm4" style="width : 100%;padding-left:2px;" maxlength="60" styleClass="doubleByte"/><br>
      				</TD>
					<%-- 課題No.33 保有株数カンマ編集 --%>
					<%-- 修正開始 --%>
					<%-- <TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu4" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right"/><br> --%>
      				<TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu4" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right" onfocus="select()" onblur="doInsertComma()" /><br>
					<%-- 修正完了 --%>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="hiritu4" style="width : 100%;padding-right:2px;"  maxlength="6" styleClass="right" onblur="addShosu()"/><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_5)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabunusiNm5" style="width : 100%;padding-left:2px;" maxlength="60" styleClass="doubleByte"/><br>
      				</TD>
					<%-- 課題No.33 保有株数カンマ編集 --%>
					<%-- 修正開始 --%>
					<%-- <TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu5" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right"/><br> --%>
      				<TD style="width:30%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="kabusu5" style="width : 100%;padding-right:2px;" maxlength="13" styleClass="right" onfocus="select()" onblur="doInsertComma()" /><br>
					<%-- 修正完了 --%>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp"><html:text name="TorokuForm" property="hiritu5" style="width : 100%;padding-right:2px;"  maxlength="6" styleClass="right" onblur="addShosu()"/><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      		</TABLE>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:100%;" class="OC1102contents2tbl2">
		<TR class="semakuBorderNone">
		<TD style="width:8%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_JIYU)%></DIV>
		</TD>
		<TD style="width:92%;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="jiyu_nm" /></DIV><BR>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:40%;" class="OC1102contents2tbl2">
		<TR class="semakuBorderNone">
		<TD style="width:60%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_ZAIMU_GAIYO)%></DIV>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:100%;" class="OC1102BorderNone">
		<TR class="OC1102BorderNone">
		<TD style="width:49%;vertical-align:top;" class="semakuBorderNone">
			<TABLE class="OC1102contents2">
        		<TR>
      			<TD style="text-align:center;width:25%;" class="OC1102BorderNone"><br></TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
      			<TD style="text-align:center;width:25%;" class="OC1102BorderNone"><nested:write property="kessan_ki" />
				&nbsp;<nested:notEqual property="tantai_renketu" value=""><%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%><BR><%}%><%=i18n.get(GL.COMMON_SYOUKAKKO)%><nested:write property="tantai_renketu" /><%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%></nested:notEqual>
				<br>
				</TD>
      			</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_URIAGE)%><br>
    			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="uriagedaka" /><br></TD>
				</nested:iterate>
   				 </TR>
    			<TR class="OC1102BorderNone">
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_URIAGE_TOTAL)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="uriagesorieki" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_HANBAI_KANRI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanbaihikanrihi" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_EIGYO_RIEKI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="eigyorieki" /><br></TD>
				</nested:iterate>
    			</TR>
		        <TR>
 			    <TD class="OC1102ZaimuTitle"><bean:write name="TorokuForm" property="hanyoTitle1" /><br>
 			    </TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou1" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><bean:write name="TorokuForm" property="hanyoTitle2" /><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou2" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><bean:write name="TorokuForm" property="hanyoTitle3" /><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou3" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_TOKI_JUN_RIEKI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="tokijunrieki" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_HAITO)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="haitokin" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_GENKA_SYOKYAKU)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="genkasyokyakuhi" /><br></TD>
				</nested:iterate>
   				</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_EIGYO_CF)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="eigyo_cf" /><br></TD>
				</nested:iterate>
    			</TR>
			</TABLE>
		</TD>
		<TD style="width:51%;vertical-align:top;" class="semakuBorderNone">
			<TABLE class="OC1102contents2">
   				<TR>
				<TD style="text-align:center;width:6%;" class="OC1102BorderNone"><br>
  			    </TD>
      			<TD style="text-align:center;width:25%;" class="OC1102BorderNone"><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
      			<TD style="text-align:center;width:23%;" class="OC1102BorderNone"><nested:write property="kessan_ki" />
				&nbsp;<nested:notEqual property="tantai_renketu" value=""><%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%><BR><%}%><%=i18n.get(GL.COMMON_SYOUKAKKO)%><nested:write property="tantai_renketu" /><%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%></nested:notEqual>
				<br>
				</TD>
      			</nested:iterate>
    			</TR>
    			<TR>
      			<TD rowspan="2" style="width:6%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_RYUDO_SHISAN)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="ryudosisan" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_KOTEI_SHISAN)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="koteisisan" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:31%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_SHISAN_KEI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="sisangokei" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD rowspan="2" style="width:6%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_RYUDO_HUSAI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="ryudohusai" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_KOTEI_HUSAI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="koteihusai" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:31%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_HUSAI_KEI)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="husaigokei" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
     			<TD rowspan="2" style="width:6%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_SHIHONKIN)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="sihonkin" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3"><%=i18n.get(GL.OC1102_NAIBU_RYUHO)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="naiburyuho" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:31%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_JIKO_SHIHON)%><br>
      			</TD>
				<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="jikosihongokei" /><br></TD>
				</nested:iterate>
    			</TR>
			</TABLE>

<TABLE style="width:100%;border:0px;border-collapse: collapse;">
		<TR style="border:0px;">
		<TD style="text-align:left;border:0px;width:31%;" class="semaku">
			<%=i18n.get(GL.OC1102_TUUKA)%><%=i18n.get(GS.KAKKO_HIDARI)%><%=i18n.get(GL.OC1102_HYOUJI_TANI)%><%=i18n.get(GS.KAKKO_MIGI)%></TD>
			<nested:iterate name="TorokuForm" property="zaimu" indexId="idx">
				<TD style="text-align:center;border:0px;width:23%;"class="semaku">
					<nested:write property="tukaCd" /><nested:write property="hyoujiTani" /></TD>
			</nested:iterate>
		</TR>
		</TABLE>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE class="OC1102BorderNone" style="width:55%;">
		<TR>
		<logic:empty name="TorihikisakiBean" property="ktk_kikan">
		<TD style="width:15%;"class="semakuBorderNone"><BR>
		</TD>
		<TD style="width:15%;"class="semakuBorderNone"><BR>
		</TD>
		</logic:empty>
		<logic:notEmpty name="TorihikisakiBean" property="ktk_kikan">
		<TD style="width:15%;"class="semakuBorderNone"><bean:write name="TorihikisakiBean" property="ktk_kikan" /><BR>
		</TD>
		<TD style="width:15%;text-align:left;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:60px;"><bean:write name="TorihikisakiBean" property="gaibu_ktk" /><BR>
		</TD>
		</logic:notEmpty>
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_FSS)%></DIV><BR>
		</TD>
		<TD style="width:25%;text-align:left;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:60px;"><bean:write name="TorihikisakiBean" property="fss" /></DIV><BR>
		</TD>
		<TD style="width:20%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_DUNS_RATING)%></DIV><BR>
		</TD>
		<TD style="width:15%;text-align:left;"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:60px;"><bean:write name="TorihikisakiBean" property="duns_rating" /></DIV><BR>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE class="OC1102contents1">
		<TR>
		<TD style="bwidth:10%;vertical-align:top;"class="semakuBorderNone"><%=i18n.get(GL.OC1102_KESAN_GAIKYO)%><BR>
		</TD>
		<TD style="height:4em;width:90%;"class="semakuBorderNone"><html:textarea name="TorokuForm" property="kesanGaikyo" rows="3" style="width : 100%;" /><BR>
		</TD>
		</TR>
	</TABLE>
</TABLE>
</DIV>
</DIV>
</html:form>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>