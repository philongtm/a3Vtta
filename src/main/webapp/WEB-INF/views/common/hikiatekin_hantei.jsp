<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>

<bean:define id="HikiatekinHanteiSyokaiForm" name="HikiatekinHanteiSyokaiForm" type="app.common.form.HikiatekinHanteiSyokaiForm" />
<bean:define id="cmnData" name="app.SessionData" type="app.SessionData" scope="session" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />


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
	  ///////////////////////////////////////
	  //障害票：438
	  //チェックイン日：2008/5/24
	  //対応者：SJA小森
	  //概要：フレームの領域下の余白が長すぎるのは見栄えが悪い
	  ////////////////////////////////////////
		--%>
		var sHeightMin = 470;
		if(sHeight < sHeightMin){
			sHeight = sHeightMin;
		}
		<%--	
		// 2008/05/24 Komori End
		--%>

		
		iframeObj.style.height = sHeight;
		
	}
	
</script>

</HEAD>
<BODY onload="resizeParentIFrame()">

<%--コンテンツ部分--%>
<DIV id="tabcontents">

<form action="/common/hikiatekin_hantei">

<DIV id="list">
<DIV class="mainlist">

<TABLE style="border:0px;width:98%;margin: 0px; ">
	<TR style="border:0px;">
		<TD style="border:0px;width:49%;vertical-align:top;">
		<TABLE style="border:0px;width:100%;">
			<TR style="border:0px;">
			
				<TD style="border:0px;" class="left">
					<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1104_HIKIATEHANTEI)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
				</TD>
				<TD style="border:0px;" class="right">
					<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;
 					<bean:write name="HikiatekinHanteiSyokaiForm" property="tani" /> 
				</TD>
			</TR>
		</TABLE>
		
		<TABLE style="width:100%;border-collapse:collapse;">
			<TR>
				<TD rowspan="14" style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
				</TD>
				<TD rowspan="12" style="width: 7%;border-bottom-color: #666699;" class="inputRyuhoColor"><br>
				</TD>
				<TD style="width:40%;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_UKETORI_TEGATA)%>
				</TD>
				<TD style="width:40%;background-color:#CCECFF;" class="right">
						<bean:write name="HikiatekinHanteiSyokaiForm" property="uketoritegata" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_YUSHUTU_UKETORI_TEGATA)%>
				</TD>
				<TD style="width:40%;background-color:#CCECFF;"  class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="yusyutu_uketoritegata" />
				</TD>
			</TR>
			<TR>
				<TD style="vertical-align: top;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_URIKAKE_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="urikakekin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_TORIHIKI_MAEWATASHI_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="torihikimaetokin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_TATEKAE_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tatekaekin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_MISHUUNYUU_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="misyunyukin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_MISHUU_SHUUEKI)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="misyusyueki" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_TANKI_KASHITUKE_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tanki_kashitsukekin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_SASHIIRE_HOSHOU_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="sashiire_hosyokin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_KARIBARAI_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="karibaraikin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_CHOUKI_KASHITUKE_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tyoki_kashitsukekin" />
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_SONOTA_TOUSHI)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="sonota_toshi" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" class="inputRyuhoColor">
					<%=i18n.get(GL.OC1104_IPPAN_SAIKEN_KEI)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="ipan_saimukei" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" class="inputRyuhoColor">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hanyo1_title" />
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hanyo1" />
				</TD>
			</TR>
			<TR>
				<TD colspan="3" class="inputThColor">
					<%=i18n.get(GL.OC1104_SAIKEN_ZANDAKA_GOUKEI_1)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="saiken_zankei" />
				</TD>
			</TR>
		</TABLE>

		<BR>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD rowspan="2" style="width: 7%; border-bottom:none; background-color:#330088;"><br>
				</TD>
				<TD style="width: 47%;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_RYUUHO_SAIMU )%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="ryuhosaimu" />
				</TD>
			</TR>
			<TR>
				<TD style="width: 47%;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_NO3_RYUUHO_SAIMU)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="oth_ryuhosaimu" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_RYUUHO_SAIMU_KEI_2)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="ryuhosaimu_kei" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_HOZEN_3)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hozen" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_SONOTA_KAISHUU_4)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="sonotakaisyu" />
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD colspan="2" style="border-bottom:none;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_HOSHOU_SAIMU_GOUKEI)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hosyosaimu_gokei" />
				</TD>
			</TR>
			<TR>
				<TD style="width: 7%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
				</TD>
				<TD style="width: 47%;border:solid 1px #AAA;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_RIKOU_SEIKYUU_KENEN_5)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="riko_kenen" />
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_KI_HIKIATE_KIN_6)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="kibikiatekin" />
				</TD>
			</TR>
			<TR>
				<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_HIKIATE_TAISHOU_KINGAKU)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hikiate_taisyokingaku" />
				</TD>
			</TR>
			</TABLE>
		<TABLE style="width:100%;border:0px;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="width: 54%;border:0px;">
					<%=i18n.get(GL.OC1104_HIKIATE_BANGO)%>
				</TD>
				<TD style="width: 40%;border:0px;" class="right">
					&nbsp;
				</TD>
			</TR>
		</TABLE>
		<br>
		

		<% if(TorihikisakiBean.getSystem_kbn().equals(GS.GSS)) {%>
		
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tuika_hikiate" />
				</TD>
			</TR>
			</TABLE>
			<%} else { %>
			
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD rowspan="2" style="width: 7%; border-bottom:none; background-color:#330088;"><br>
				</TD>
				<TD style="width: 47%;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN )%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tuika_hikiate" />
				</TD>
			</TR>
			<TR>
				<TD style="width: 47%;background-color:#EAEAEA;" class="right">
					<%=i18n.get(GL.OC1104_TUUKA_CHOUSEI)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tuuka_tyousei" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_TUIKA_HIKIATE_KIN_CHOUSEIGO)%>
				</TD>
				<TD style="width: 40%;background-color:#CCECFF;" class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="tuika_kingaku_go" />
				</TD>
			</TR>
		</TABLE>
		<%}%>
		</TD> 
		<TD style="border:0px;width:49%;vertical-align:top;">
		<TABLE style="border:0px;width:100%;">
			<TR style="border:0px;">
				<TD style="border:0px;width:70%;" class="left">
					<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1104_NO3_RYUUHO_SAIMU)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
				</TD>
				<TD style="border:0px;width:30%;" class="right">
					<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tani" />
				</TD>
			</TR>
		</TABLE>
 
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD style="width:25%; background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_KANJO_CD)%>
				</TD>
				<TD style="width:25%; background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_TORIHIKISAKI_NM)%>
				</TD>
				<TD style="width:25%; background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_KANJOKAMOKU)%>
				</TD>
				<TD style="width:25%; background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_KINGAKU)%>
				</TD>
			</TR>
				<TR>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tori_cd_1" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tor_nm_1" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kanjo_nm_1" /><br>
					</TD>
					<TD class="right">
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kingaku_1" /><br>
					</TD>
				</TR>
				<TR>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tori_cd_2" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tor_nm_2" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kanjo_nm_2" /><br>
					</TD>
					<TD class="right">
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kingaku_2" /><br>
					</TD>
				</TR>
				<TR>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tori_cd_3" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tor_nm_3" /><br>
					</TD>
					<TD>
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kanjo_nm_3" /><br>
					</TD>
					<TD class="right">
						<bean:write name="HikiatekinHanteiSyokaiForm" property="kingaku_3" /><br>
					</TD>
				</TR>
		</TABLE>
		<BR>
				
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OC1104_HOZEN)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
				</TD>
				<TD style="border:0px;width:10%;" class="right">
					<%=i18n.get(GL.OC1104_TANI)%>&nbsp;<%=i18n.get(GL.COMMON_COLON)%>&nbsp;
						<bean:write name="HikiatekinHanteiSyokaiForm" property="tani" />
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD style="width:20%;background-color:#330088;color:#FFFFFF;"><BR>
				</TD>
				<TD style="width:20%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_HUDOUSAN_TANPO)%>
				</TD>
				<TD style="width:20%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_DOSAN_TANPO)%>
				</TD>
				<TD style="width:20%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_BOEKI_HOKEN)%>
				</TD>
				<TD style="width:20%;background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_SONOTA)%>
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_KEIYAKU_GAKU)%>
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hudosan_k" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="dosan_k" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hoken_k" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="sonota_k" /> 
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.OC1104_HYOKA_GAKU)%>
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hudosan_h" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="dosan_h" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="hoken_h" />
				</TD>
				<TD class="right">
					<bean:write name="HikiatekinHanteiSyokaiForm" property="sonota_h" />
				</TD>
			</TR>
		</TABLE>
		<BR>
		<BR>
	
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
				<TD style="border:0px;width:18%;" class="left">
				<%} else {%>
				<TD style="border:0px;width:40%;" class="left">
				<%}%>
					<%=i18n.get(GL.OC1104_TASYA_RISUKU)%> </TD>
					<TD style="border:0px;width:82%;"class="semaku">
						<input type="checkbox" name="HikiatekinHanteiSyokaiForm" property="tasya_risuku" value="1" disabled="true" />
				</TD>
			</TR>
		</TABLE>
		<BR>
		
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.OC1104_ETC_NAIYO)%>
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="width:100%;border:0px;">
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">

<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="sonota_naiyo" />&nbsp;</font></pre>
				</DIV>
				</TD>
			</TR>
		</TABLE>
		<BR>		
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.OC1104_ETC_KAISYU_NAIYO)%>
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="width:100%;border:0px;">
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">

<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="sonota_kaisyu_naiyo" />&nbsp;</font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>
		<BR>
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.OC1104_RIKO_NAIYOU)%>
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">
				
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="rikoseikyu_kenen_naiyo" />&nbsp;</font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>
		<BR>

		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.OC1104_HIKIATE_NAIYO)%>
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">
				
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="hikiatekin_konkyo_naiyo" />&nbsp;</font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>

		<BR>
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;" class="left">
					<%=i18n.get(GL.OC1104_KONGO_MITOSHI)%>
				</TD>
			</TR>
			
		</TABLE>
		

		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">
				
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="kaisyu_naiyo" />&nbsp;</font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>

	<TR>
		
	<TABLE style="border:0px;width:100%;table-layout:fixed;">
	<TR style="border:0px;">
		<TD style="border:0px;width:15%;"class="semaku"><DIV class="dottitle"><%=i18n.get(GL.OC1104_SIHANKI_FLG)%></DIV></TD>
		<TD style="border:0px;width:6%;"class="semaku">
			<input type="checkbox" name="HikiatekinHanteiSyokaiForm" property="sihanki_flg" value="1" disabled="true" />
			</TD>
		<TD style="border:0px;width:11%;"class="semaku"><DIV class="dottitle"><%=i18n.get(GL.OC1104_FLG_KBN)%></DIV></TD>
		<TD colspan=2 style="border:0px;width:70%;"class="semaku">
			<select property="flg_kbn" disabled="true" style="width:40%;" >
    		<optionsCollection name="HikiatekinHanteiSyokaiForm" property="ar_flg_kbn" value="key" label="value" />
			</select>
		</TD>
	
	</TR>
	 
	<TR style="border:0px;">
		<TD style="border:0px;width:15%;"class="semaku"><DIV class="dottitle"><%=i18n.get(GL.OC1104_FLG_COMMENT)%></DIV></TD>
		<TD colspan=4 style="border:0px;width:85%;"class="semaku"></TD>
	</TR>
	<TR style="border:0px;">
				<TD  colspan=5 style="width:100%;border:0px;margin: 0 0 0 0;padding: 5px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;">
				
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinHanteiSyokaiForm" property="flg_comment" />&nbsp;</font></pre>
				</DIV>	
				</TD>
	</TR>
	</TABLE>	
	</TR>

</TABLE>   
</DIV>
</DIV>
</form>
</DIV>
</BODY>
</HTML>