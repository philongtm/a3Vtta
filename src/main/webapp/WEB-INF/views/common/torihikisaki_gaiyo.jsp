<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>
<link rel="stylesheet" href="<c:url value='/css/Satei.css' />" type="text/css">
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="TorihikisakiGaiyoSyokaiForm" name="TorihikisakiGaiyoSyokaiForm" type="app.common.form.TorihikisakiGaiyoSyokaiForm" />

<script>	
	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;

	var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];
		<%--	
		// scrollHeightの値ピッタリでは、スクロールバーが出る可能性がある為
		// サイズに余裕を持たせる。
		--%>
		sHeight += 10;
		<%--	
		//フレームの領域下の余白調整
		--%>
		var sHeightMin = 470;
		if(sHeight < sHeightMin){
			sHeight = sHeightMin;
		}
		iframeObj.style.height = sHeight;
	}
</script>

</HEAD>
<BODY onload="resizeParentIFrame()">
<CENTER>

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<html:form action="/common/torihikisaki_gaiyo">
<DIV id="list">
	<DIV class="mainlist">
	<TABLE class="OC1102contents1">
		<TR>
		<TD class="semakuBorderNone" style="width:10%;"><DIV class="dottitle"><%=i18n.get(GL.OC1102_DUNS_NO)%></DIV><BR>
		</TD>
		<TD colspan="2" class="semakuBorderNone left" style="width:30%;"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="togo_tori_cd" /><BR>
		</TD>
		<TD class="semakuBorderNone right" style="width:10%;"><DIV class="dottitle"><%=i18n.get(GL.OC1102_SYOZAI_COUNTRY)%></DIV><BR>
		</TD>
		<TD colspan="3" class="semakuBorderNone" style="width:50%;">
			<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="syozaikoku" /></DIV><BR>
		</TD>
		</TR>
		<TR>
		<TD class="semakuBorderNone" style="width:10%;"><DIV class="dottitle"><%=i18n.get(GL.OC1102_SYOZAI_CHI)%></DIV><BR>
		</TD>
		<TD colspan="6" class="semakuBorderNone" style="width:90%;">
			<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="syozaichi" /></DIV><BR>
		</TD>
		</TR>
		<TR>
		<TD class="semakuBorderNone" style="width:10%;"><DIV class="dottitle"><%=i18n.get(GL.OC1102_GYOSYU)%></DIV><BR>
		</TD>
		<TD colspan="6"class="semakuBorderNone"><DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="sic_sm_nm" /></DIV><BR>
		</TD>
		</TR>
		<TR>
		<TD class="semakuBorderNone" style="width:10%;"><%=i18n.get(GL.OC1102_JIGYO)%><BR>
		</TD>
		<TD colspan="6" class="semakuBorderNone">
			<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="jigyonaiyo" /></DIV>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE class="OC1102contents2">
		<TR>
		<TD class="semakuBorderNone" style="width:12%;vertical-align:top;"><DIV class="dottitle"><%=i18n.get(GL.OC1102_KABUNUSHI_KOUSEI)%></DIV><BR>
		</TD>
		<TD style="width:44%;vertical-align:top;" class="OC1102semaku">
			<TABLE class="OC1102BorderNone">
    			<TR class="OC1102contentsMp">
      				<TD style="width:4%" class="OC1102contentsMp"><br>
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
      				<TD style="width:50%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabunusi_nm1"/></DIV><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabusu1"/></DIV><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hiritu1"/></DIV><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp center"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_2)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabunusi_nm2"/></DIV><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabusu2"/></DIV><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hiritu2"/></DIV><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp center"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
     		 </TABLE>
		</TD>
		
		<TD style="width:44%;" class="OC1102semaku">
			<TABLE class="OC1102BorderNone">
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_3)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabunusi_nm3"/></DIV><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabusu3"/></DIV><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hiritu3"/></DIV><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp center"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
    			<TR class="OC1102contentsMp">
      				<TD style="width:4%" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_4)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabunusi_nm4"/></DIV><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabusu4"/></DIV><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hiritu4"/></DIV><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp center"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      			<TR class="OC1102contentsMp">
      				<TD style="width:4%;" class="OC1102contentsMp"><%=i18n.get(GL.COMMON_5)%><br>
      				</TD>
      				<TD style="width:50%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabunusi_nm5"/></DIV><br>
      				</TD>
      				<TD style="width:30%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="kabusu5"/></DIV><br>
      				</TD>
      				<TD style="width:12%;" class="OC1102contentsMp">
      					<DIV class="ReadOnlybox right" style="width:100%;"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hiritu5"/></DIV><br>
      				</TD>
      				<TD style="width:4%;" class="OC1102contentsMp center"><%=i18n.get(GL.COMMON_PERCENT)%><br>
      				</TD>
      			</TR>
      		</TABLE>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:100%;" class="OC1102contents2tbl2">
		<TR class="semakuBorderNone">
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_JIYU)%></DIV><BR>
		</TD>
		<TD style="width:90%;"class="semakuBorderNone left">
			<DIV class="ReadOnlybox" style="width:100%;"><bean:write name="TorihikisakiBean" property="jiyu_nm" /></DIV><BR>
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:100%;" class="OC1102contents2tbl2">
		<TR class="semakuBorderNone">
		<TD style="width:30%;"class="semakuBorderNone left">
			<DIV class="dottitle"><%=i18n.get(GL.OC1102_ZAIMU_GAIYO)%></DIV>
		</TD>
		<TD class="semakuBorderNone">
		</TD>
		</TR>
	</TABLE>
	
	<TABLE style="width:100%;" class="OC1102BorderNone">
		<TR class="OC1102BorderNone">
		<TD style="width:50%;vertical-align:top;" class="semakuBorderNone">
			<TABLE class="OC1102contents2">
        		<TR>
      			<TD style="text-align:center;width:25%;" class="OC1102BorderNone"><br></TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
      			<TD style="text-align:center;width:25%;" class="OC1102BorderNone"><nested:write property="kessan_ki" />
				<nested:notEmpty property="tantai_renketu">&nbsp;
					<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%><BR><%}%><%=i18n.get(GL.COMMON_SYOUKAKKO)%><nested:write property="tantai_renketu" /><%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%>
				</nested:notEmpty>
				</TD>
      			</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_URIAGE)%><br>
    			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="uriagedaka" /><br></TD>
				</nested:iterate>
   				 </TR>
    			<TR class="OC1102BorderNone">
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_URIAGE_TOTAL)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="uriagesorieki" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_HANBAI_KANRI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanbaihikanrihi" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_EIGYO_RIEKI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="eigyorieki" /><br></TD>
				</nested:iterate>
    			</TR>
		        <TR>
 			    <TD class="OC1102ZaimuTitle"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hanyou1" /><br>
 			    </TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou1" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hanyou2" /><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou2" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="hanyou3" /><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="hanyou3" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_TOKI_JUN_RIEKI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="tokijunrieki" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_HAITO)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="haitokin" /><br></TD>
				</nested:iterate>
    			</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_GENKA_SYOKYAKU)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="genkasyokyakuhi" /><br></TD>
				</nested:iterate>
   				</TR>
        		<TR>
      			<TD class="OC1102ZaimuTitle"><%=i18n.get(GL.OC1102_EIGYO_CF)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo"><nested:write property="eigyo_cf" /><br></TD>
				</nested:iterate>
    			</TR>
			</TABLE>
		</TD>
		<TD style="width:50%;vertical-align:top;" class="semakuBorderNone">
			<TABLE class="OC1102contents2">
   				<TR>
				<TD style="text-align:center;width:5%;" class="OC1102BorderNone"><br>
  			    </TD>
      			<TD style="text-align:center;width:20%;" class="OC1102BorderNone"><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
      			<TD style="text-align:center;width:24%;" class="OC1102BorderNone"><nested:write property="kessan_ki" />
				<nested:notEmpty property="tantai_renketu">&nbsp;
					<%if(SESSION_DATA_APP.getComLangMode().equals("En")) {%><BR><%}%><%=i18n.get(GL.COMMON_SYOUKAKKO)%><nested:write property="tantai_renketu" /><%=i18n.get(GL.COMMON_SYOUKAKKO_TOJI)%>
				</nested:notEmpty>
				</TD>
      			</nested:iterate>
    			</TR>
    			<TR>
      			<TD rowspan="2" style="width:5%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_RYUDO_SHISAN)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="ryudosisan" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_KOTEI_SHISAN)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="koteisisan" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:25%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_SHISAN_KEI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="sisangokei" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD rowspan="2" style="width:5%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_RYUDO_HUSAI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="ryudohusai" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_KOTEI_HUSAI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="koteihusai" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:25%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_HUSAI_KEI)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="husaigokei" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
     			<TD rowspan="2" style="width:5%;" class="OC1102ZaimuTitle4"><br>
      			</TD>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_SHIHONKIN)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="sihonkin" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD class="OC1102ZaimuTitle3" style="width:20%;"><%=i18n.get(GL.OC1102_NAIBU_RYUHO)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="naiburyuho" /><br></TD>
				</nested:iterate>
    			</TR>
    			<TR>
      			<TD colspan="2" style="width:25%;" class="OC1102ZaimuTitle2"><%=i18n.get(GL.OC1102_JIKO_SHIHON)%><br>
      			</TD>
				<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD class="OC1102ZaimuNaiyo2"><nested:write property="jikosihongokei" /><br></TD>
				</nested:iterate>
    			</TR>
			</TABLE>
		<TABLE style="width:100%;border:0px;border-collapse: collapse;">
		<TR style="border:0px;">
		<TD style="text-align:left;border:0px;width:25%;" class="semaku">
			<%=i18n.get(GL.OC1102_TUUKA)%><%=i18n.get(GS.KAKKO_HIDARI)%><%=i18n.get(GL.OC1102_HYOUJI_TANI)%><%=i18n.get(GS.KAKKO_MIGI)%></TD>
			<nested:iterate name="TorihikisakiGaiyoSyokaiForm" property="ar_zaimu" indexId="idx">
				<TD style="text-align:center;border:0px;width:24%;"class="semaku">
					<nested:write property="tukaCd" /><nested:write property="hyoujiTani" /></TD>
			</nested:iterate>
		</TR>
		</TABLE>
		</TD>
		</TR>
	</TABLE>

	<DIV class="left">	
	<TABLE class="OC1102BorderNone" style="width:60%;">
		<TR>
		<logic:empty name="TorihikisakiBean" property="ktk_kikan">
		<TD style="width:10%;" class="semakuBorderNone"><DIV class="dottitle"></DIV></TD>
		<TD style="width:25%;" class="semakuBorderNone left"><DIV class="dottitle" style="width:8%;"></DIV>
		</logic:empty>
		<logic:notEmpty name="TorihikisakiBean" property="ktk_kikan">
		<TD style="width:10%;" class="semakuBorderNone"><DIV class="dottitle"><bean:write name="TorihikisakiBean" property="ktk_kikan" /></DIV></TD>
		<TD style="width:25%;" class="semakuBorderNone left"><DIV class="ReadOnlybox" style="width:80%;"><bean:write name="TorihikisakiBean" property="gaibu_ktk" /></DIV>
		</logic:notEmpty>
		<TD style="width:10%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_FSS)%></DIV><BR>
		</TD>
		<TD style="width:25%;"class="semakuBorderNone left"><DIV class="ReadOnlybox" style="width:60px;"><bean:write name="TorihikisakiBean" property="fss" /></DIV><BR>
		</TD>
		<TD style="width:20%;"class="semakuBorderNone"><DIV class="dottitle"><%=i18n.get(GL.OC1102_DUNS_RATING)%></DIV><BR>
		</TD>
		<TD style="width:15%;"class="semakuBorderNone left"><DIV class="ReadOnlybox" style="width:60px;"><bean:write name="TorihikisakiBean" property="duns_rating" /></DIV><BR>
		</TD>
		<TD class="semakuBorderNone"></TD>
		</TR>
	</TABLE>
	</DIV>
	
	<TABLE class="OC1102contents1">
		<TR>
		<TD style="width:10%;"class="semakuBorderNone"><%=i18n.get(GL.OC1102_KESAN_GAIKYO)%><BR>
		</TD>
		<TD style="width:90%;"class="semakuBorderNone left">
			<DIV class="ReadOnlybox" style="width:100%; ">
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="TorihikisakiGaiyoSyokaiForm" property="comment_val" /></font></pre>&nbsp;</DIV>
		</TD>
		</TR>
	</TABLE>
</DIV>
</DIV>
</html:form>
</DIV>
</CENTER>
</BODY>
</HTML>