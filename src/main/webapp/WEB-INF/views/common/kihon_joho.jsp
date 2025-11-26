<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>	
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>

<bean:define id="KihonJohoSyokaiForm" name="KihonJohoSyokaiForm" type="app.common.form.KihonJohoSyokaiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />

<script>
	
	function resizeParentIFrame() {
		var sHeight = document.body.scrollHeight;
  		  		
  	var	iframeTagList = parent.document.getElementsByTagName("iframe");
		var iframeObj = iframeTagList[0];
		<%--scrollHeightの値ピッタリでは、スクロールバーが出る可能性がある為、サイズに余裕を持たせる。--%>
		sHeight += 10;
		var sHeightMin = 470;
		if(sHeight < sHeightMin){
			sHeight = sHeightMin;
		}
		iframeObj.style.height = sHeight;
	}
	
</script>
<link rel="stylesheet" href="../css/Tairyu.css" type="text/css">

</HEAD>
<BODY id="01_02B" onload="resizeParentIFrame()">
<CENTER>

<%--コンテンツ部分--%>
<DIV id="tabcontents">
	<DIV id="list">
		<html:form action="/common/kihon_joho">
			<DIV class="mainlist">
				<BR>
				<TABLE style="width:100%;table-layout:fixed;border:0px;">
					<TR>
						<TD style="border:0px;">
							<TABLE border=0 cellSpacing=0 cellPadding=0 class="none">
								<TR>
									<%--単位--%>
									<TD class="head right">
										<p><%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;<bean:write name="KihonJohoSyokaiForm" property="tuuka_cd" /></p>
									</TD>
								</TR>
							</TABLE>
		
							<TABLE style="width:100%;border-collapse:collapse;table-layout:fixed;border-top-color:#000000;border-left-color:#000000;"class="semaku">
								<TR>
									<TD rowspan="14" style="width:7%;border-bottom-color: #330088;" class="inputThColor "><br>
									</TD>
									<TD rowspan="12" style="width:7%;border-bottom-color: #666699;" class="inputRyuhoColor "><br>
									</TD>
									<%--受取手形--%>
									<TD style="width:40%;" class="inputKanjoColor right"><%=i18n.get(GL.OC1104_UKETORI_TEGATA)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn01" /></TD>
								</TR>
								<TR>
									<%--輸出受取手形--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_YUSHUTU_UKETORI_TEGATA)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn02" /></TD>
								</TR>
								<TR>
									<%--売掛金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_URIKAKE_KIN)%></TD>
									<TD style="width: 40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn03" /></TD>
								</TR>
								<TR>
									<%--取引前渡金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_TORIHIKI_MAEWATASHI_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn04" /></TD>
								</TR>
								<TR>
									<%--立替金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_TATEKAE_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn05" /></TD>
								</TR>
								<TR>
									<%--未収入金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_MISHUUNYUU_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn06" /></TD>
								</TR>
								<TR>
									<%--未収収益--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_MISHUU_SHUUEKI)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn07" /></TD>
								</TR>
								<TR>
									<%--短期貸付金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_TANKI_KASHITUKE_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn08" /></TD>
								</TR>
								<TR>
									<%--差入保証金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_SASHIIRE_HOSHOU_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn09" /></TD>
								</TR>
								<TR>
									<%--仮払金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_KARIBARAI_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn10" /></TD>
								</TR>
								<TR>
									<%--長期貸付金--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_CHOUKI_KASHITUKE_KIN)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn11" /></TD>
								</TR>
								<TR>
									<%--その他投資--%>
									<TD class="inputKanjoColor right"><%=i18n.get(GL.OC1104_SONOTA_TOUSHI)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="kbn12" /></TD>
								</TR>
								<TR>
									<%--一般債権計--%>
									<TD colspan="2" class="inputRyuhoColor"><%=i18n.get(GL.OC1104_IPPAN_SAIKEN_KEI)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="ippan_saiken_kei" />
									</TD>
								</TR>
								<TR>
									<%--汎用１--%>
									<TD colspan="2" class="inputRyuhoColor"><bean:write name="KihonJohoSyokaiForm" property="hanyou1_lbl" /></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight"><bean:write name="KihonJohoSyokaiForm" property="hanyou1" /></TD>
								</TR>
								<TR>
									<%--債権残高合計--%>
									<TD colspan="3" class="inputThColor borderBottom"><%=i18n.get(GL.OC1104_SAIKEN_ZANDAKA_GOUKEI)%></TD>
									<TD style="width:40%;" class="inputNoColor right borderRight borderBottom"><bean:write name="KihonJohoSyokaiForm" property="saiken_kei" /></TD>
								</TR>
							</TABLE>	
						</TD>
						<TD style="vertical-align:top;border:0px;">
							<TABLE border=0 cellSpacing=0 cellPadding=0 class="none">
								<TR>
									<TD class="head right"><p>&nbsp;</p></TD>
								</TR>
							</TABLE>
							<TABLE style="width:100%;" border=0 cellSpacing=0 cellPadding=0 class="none">
								<TR>
									<%--査定期--%>
									<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
										<TD width="15%">
									<%} else {%>
										<TD width="25%">
									<%}%>
										<DIV class="dottitle"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%></DIV></TD>
									<TD><DIV class="ReadOnlybox" style="width:20%"><bean:write name="TorihikisakiBean" property="satei_ki_hyouji" /></DIV></TD>
								</TR>
								<TR>
									<%--抽出事由--%>
									<TD><DIV class="dottitle"><%=i18n.get(GL.OZ6108_REASON)%></DIV></TD>
									<TD><DIV class="ReadOnlybox" style="width:98%"><bean:write name="TorihikisakiBean" property="jiyu_nm" /></DIV></TD>
								</TR>
								<TR>
									<%--DUNS NO--%>
									<TD><DIV class="dottitle"><%=i18n.get(GL.OZ6108_DUNS_NO)%></DIV></TD>
									<TD><DIV class="ReadOnlybox" style="width:30%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="togo_tori_cd" /></DIV>
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<%--信用格付--%>
										<DIV class="dottitle"><%=i18n.get(GL.OZ6108_KTK)%></DIV>
										&nbsp;<DIV class="ReadOnlybox center" style="width:20%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="sinyoktk" /></DIV>
									</TD>
								</TR>
								<TR>
									<logic:empty name="TorihikisakiBean" property="gaibu_ktk">
										<TD><DIV class="dottitle"></DIV></TD>
										<TD><DIV class="dottitle" style="width:24%; margin-bottom:5px;"></DIV>
										&nbsp;&nbsp;
									</logic:empty>
									<logic:notEmpty name="TorihikisakiBean" property="gaibu_ktk">
										<%--格付機関--%>
										<TD><DIV class="dottitle"><bean:write name="TorihikisakiBean" property="ktk_kikan" /></DIV></TD>
										<%--外部格付--%>
										<TD><DIV class="ReadOnlybox" style="width:24%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="gaibu_ktk" /></DIV>
											&nbsp;&nbsp;
									</logic:notEmpty>
									<%--FSS--%>
										<DIV class="dottitle"><%=i18n.get(GL.OZ6108_FSS)%></DIV>
										&nbsp;<DIV class="ReadOnlybox" style="width:15%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="fss" /></DIV>
										&nbsp;&nbsp;
									<%--DUNS Rating--%>
										<DIV class="dottitle"><%=i18n.get(GL.OZ6108_DUNS_RATING)%></DIV>
										&nbsp;<DIV class="ReadOnlybox" style="width:15%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="duns_rating" /></DIV>
									</TD>
								</TR>
								<TR>
									<TD><DIV class="dottitle"><%=i18n.get(GL.OZ6108_OYA_KAISYA)%></DIV></TD>
									<%--親会社信用格付--%>
									<TD><DIV class="ReadOnlybox center" style="width:8%;margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="oya_ktk" /><BR></DIV>
									<%--親会社名称--%>
										&nbsp;<DIV class="ReadOnlybox " style="width:65%; margin-bottom:5px;"><bean:write name="KihonJohoSyokaiForm" property="oya_kaisya_nm" /></DIV>
									<%--親会社一体/独立--%>
										&nbsp;<DIV class="ReadOnlybox" style="width:24%; margin-bottom:5px;"><bean:write name="TorihikisakiBean" property="oya_ittai_dokuritu" /></DIV>	
									</TD>
								</TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD style="border:0px;" >
							<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-top-color:#000000;border-left-color:#000000;"class="semaku">
							    <TR style="border:0px;">
									<%--保証債務合計--%>
									<TD class="inputThColor borderBottom"><%=i18n.get(GL.OC1104_HOSHOU_SAIMU_GOUKEI)%><BR></TD>
							    	<TD style="width:43%;" class="inputNoColor right borderRight borderBottom">
										<bean:write name="KihonJohoSyokaiForm" property="kbn14" />
							      	</TD>
							    </TR>
							</TABLE>
						</TD>
					</TR>
					<TR>
						<TD style="border:0px;" >
							<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-top-color:#000000;border-left-color:#000000;"class="semaku">
								<TR style="border:0px;">
									<%--既引当金--%>
							    	<TD class="inputThColor borderBottom"><%=i18n.get(GL.OC1104_KI_HIKIATE_KIN)%><BR></TD>
							     	<TD style="width:43%;" class="inputNoColor right borderRight borderBottom">
										<bean:write name="KihonJohoSyokaiForm" property="kbn15" />
							      	</TD>
							    </TR>
							</TABLE>
						</TD>
					</TR>
				</TABLE>
			</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>