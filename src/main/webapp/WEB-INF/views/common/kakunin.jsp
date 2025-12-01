<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="KakuninSyokaiForm" name="KakuninSyokaiForm" type="app.common.form.KakuninSyokaiForm" />

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
<%-- コンテンツ部分 --%>
<DIV id="tabcontents">
<html:form action="/common/kakunin">

<DIV id="list">
	<DIV class="mainlist">
	
	<TABLE style="border:0px;width:100%;border-collapse: collapse;table-layout:fixed;">
			<TR style="border:0px;">
			<BR>
				<TD style="border:0px;width:44%;text-align:right;vertical-align: top;">
					<%-- 単位 --%>
					<%=i18n.get(GL.OD1102_TANI)%>&nbsp;:&nbsp;<bean:write name="KakuninSyokaiForm" property="tuuka_cd"/><BR>

				<TABLE style="margin: 4;width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;">
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							&nbsp;
						</TD>
						<TD style="width: 24%; text-align: center;">
							<%-- 【前期(半期決算前)】 --%>
							<%=i18n.get(GL.OD1102_TITLE_ZENKI)%>
						</TD>
						<TD style="width: 24%; text-align: center;">
							<%-- 【仮基準】 --%>
							<%=i18n.get(GL.OD1102_TITLE_KIJUN)%>
						</TD>
						<TD style="width: 24%; text-align: center;">
							<%-- 【今期】 --%>
							<%=i18n.get(GL.OD1102_TITLE_KONKI)%>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 年月 --%>
							<%=i18n.get(GL.OD1102_YM)%>
						</TD>
						<!-- 半期の場合 -->
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_ym" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ym" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_ym" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 信用格付 --%>
							<%=i18n.get(GL.OD1102_KTK)%>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 親会社信用格付 --%>
							<%=i18n.get(GL.OD1102_OYA_KTK)%>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_oya_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_oya_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_oya_ktk" readonly="true" styleClass="inputNoL" style="background-color : #CCECFF;text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 親会社名称 --%>
							<%=i18n.get(GL.OD1102_COMPANY_NM)%>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_company_nm" readonly="true" styleClass="inputNoL" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_company_nm" readonly="true" styleClass="inputNoL" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_company_nm" readonly="true" styleClass="inputNoL" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 親会社一体判断 --%>
							<%=i18n.get(GL.OD1102_OYA_FLG)%>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_oya_flg" readonly="true" styleClass="inputNoL" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_oya_flg" readonly="true" styleClass="inputNoL" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_oya_flg" readonly="true" styleClass="inputNoL " style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 滞留区分 --%>
							<%=i18n.get(GL.OD1102_TAIRYU_KBN)%>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_tairyu_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tairyu_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_tairyu_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 滞留区分名称 --%>
							<%=i18n.get(GL.OD1102_TAIRYU_KBN_NM)%>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 取引先区分 --%>
							<%=i18n.get(GL.OD1102_TORIHIKISAKI_KBN)%>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_torihikisaki_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_torihikisaki_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_torihikisaki_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 債権区分 --%>
							<%=i18n.get(GL.OD1102_SAIKEN_KBN)%>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_saiken_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_saiken_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: left;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_saiken_kbn" readonly="true" styleClass="inputNoL3" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
				</TABLE>
				<TABLE style="margin: 4;width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD colspan="1" rowspan="14" style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD colspan="1" rowspan="12" style="width: 3%;border-bottom-color: #666699;" class="inputRyuhoColor"><br>
						</TD>
						<TD style="width: 22%; text-align: right;" class="inputKanjoColor">
							<%-- 受取手形 --%>
							<%=i18n.get(GL.OD1102_UKETORI_TEGATA)%>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 輸出受取手形 --%>
							<%=i18n.get(GL.OD1102_YUSHUTU_UKETORI_TEGATA)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_yushutu_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_yushutu_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_yushutu_uketori_tegata" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="vertical-align: top; text-align: right;" class="inputKanjoColor">
							<%-- 売掛金 --%>
							<%=i18n.get(GL.OD1102_URIKAKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_urikake_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_urikake_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_urikake_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 取引前渡金 --%>
							<%=i18n.get(GL.OD1102_TORIHIKI_MAEWATASHIE_KIN)%><BR>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 立替金 --%>
							<%=i18n.get(GL.OD1102_TATEKAE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_tatekae_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tatekae_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_tatekae_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 未収入金 --%>
							<%=i18n.get(GL.OD1102_MISHUUNYUU_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_mishuunyuu_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_mishuunyuu_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_mishuunyuu_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 未収収益 --%>
							<%=i18n.get(GL.OD1102_MISHUU_SHUUEKI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_mishuu_shuueki" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_mishuu_shuueki" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_mishuu_shuueki" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 短期貸付金 --%>
							<%=i18n.get(GL.OD1102_TANKI_KASHITUKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_tanki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tanki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_tanki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 差入保証金 --%>
							<%=i18n.get(GL.OD1102_SASHIIRE_HOSHOU_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_sashiire_hoshou_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_sashiire_hoshou_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_sashiire_hoshou_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 仮払金 --%>
							<%=i18n.get(GL.OD1102_KARIBARAI_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_karibarai_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_karibarai_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_karibarai_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 長期貸付金 --%>
							<%=i18n.get(GL.OD1102_CHOUKI_KASHITUKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_chouki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_chouki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_chouki_kashituke_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- その他投資 --%>
							<%=i18n.get(GL.OD1102_SONOTA_TOUSHI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_sonota_toushi" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_sonota_toushi" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_sonota_toushi" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%-- 一般債権計 --%>
							<%=i18n.get(GL.OD1102_IPPAN_SAIKEN_KEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_ippan_saiken_kei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ippan_saiken_kei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_ippan_saiken_kei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%-- 通貨調整 --%>
							<%=i18n.get(GL.OD1102_TUUKA_CHOUSEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_komoku1" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_komoku1" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_komoku1" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="3" class="inputThColor">
							<%-- 債権残高合計① --%>
							<%=i18n.get(GL.OD1102_SAIKEN_ZANDAKA_GOUKEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_saiken_zandaka_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_saiken_zandaka_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_saiken_zandaka_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
				</TABLE>

				<TABLE style="margin: 4;width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 留保債務 --%>
							<%=i18n.get(GL.OD1102_RYUUHO_SAIMU)%>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ryuhosaimu" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 第三者留保債務 --%>
							<%=i18n.get(GL.OD1102_NO3_RYUUHO_SAIMU)%>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_oth_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_oth_ryuhosaimu" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_oth_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 留保債務計② --%>
							<%=i18n.get(GL.OD1102_RYUUHO_SAIMU_KEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_ryuuho_saimu_kei" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ryuuho_saimu_kei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_ryuuho_saimu_kei" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 保全③ --%>
							<%=i18n.get(GL.OD1102_HOZEN)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_hozen" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_hozen" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_hozen" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- その他回収④ --%>
							<%=i18n.get(GL.OD1102_SONOTA_KAISHUU)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_sonotakaisyu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_sonotakaisyu" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_sonotakaisyu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
				</TABLE>

				<TABLE style="margin: 4;width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="1" colspan="2" style="border-bottom:none;"  class="inputThColor" >
						<%-- 保証債務合計 --%>
						<%=i18n.get(GL.OD1102_HOSHOU_SAIMU_GOUKEI)%>
					</TD>
					<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
						<html:text name="KakuninSyokaiForm" property="zenki_hoshou_saimu_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
					</TD>
					<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
						<html:text name="KakuninSyokaiForm" property="kijun_hoshou_saimu_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
					</TD>
					<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
						<html:text name="KakuninSyokaiForm" property="konki_hoshou_saimu_goukei" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
					</TD>
				</TR>
				<TR>
					<TD style="width: 3%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
					</TD>
					<TD style="width: 25%; text-align: right; border:solid 1px #AAA;">
						<%-- 履行請求懸念⑤ --%>
						<%=i18n.get(GL.OD1102_RIKOU_SEIKYUU_KENEN)%>
					</TD>
					<TD style="width: 24%; text-align: right;;background-color : #CCECFF;" class="inputBlankColor">
						<input type="text" name="zenki_riko_kenen" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
					</TD>
					<TD style="width: 24%; text-align: right;;background-color : #CCECFF;" class="inputBlankColor">
						<html:text name="KakuninSyokaiForm" property="kijun_riko_kenen" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
					</TD>
					<TD style="width: 24%; text-align: right;;background-color : #CCECFF;" class="inputBlankColor">
						<input type="text" name="konki_riko_kenen" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
					</TD>
				</TR>
				</TABLE>
				
				<TABLE style="margin: 4;width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 28%;" class="inputThColor">
							<%-- 既引当金⑥ --%>
							<%=i18n.get(GL.OD1102_KI_HIKIATE_KIN)%>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="zenki_ki_hikiate_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_ki_hikiate_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="konki_ki_hikiate_kin" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%-- 引当対象金額<BR>[ ①-(②+③+④)+⑤-⑥ ] --%>
							<%=i18n.get(GL.OD1102_HIKIATE_TAISHOU_KINGAKU)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="zenki_hikiate_taishou_kingaku" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_hikiate_taishou_kingaku" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="konki_hikiate_taishou_kingaku" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
				</TABLE>
				<TABLE style="margin: 4;width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 追加引当金 --%>
							<%=i18n.get(GL.OD1102_TUIKA_HIKIATE_KIN)%>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tuika_hikiate" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 24%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%-- 通貨調整 --%>
							<%=i18n.get(GL.OD1102_TUUKA_CHOUSEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="ryuhosaimu3" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_komoku2" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="ryuhosaimu3" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 追加引当金(調整後) --%>
							<%=i18n.get(GL.OD1102_TUIKA_HIKIATE_KIN_CHOUSEI_USIRO)%>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="totalRyuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<html:text name="KakuninSyokaiForm" property="kijun_tuika_hikiate_usiro" readonly="true" styleClass="inputNo" style ="background-color : #CCECFF;"/>
						</TD>
						<TD style="width: 27%; text-align: right;background-color : #CCECFF;" class="inputBlankColor">
							<input type="text" name="totalRyuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo" style ="background-color : #CCECFF;">
						</TD>
					</TR>
				</TABLE>
				</TR>
				<%-- 課題No.28 引当対象外/帳簿外対応 --%>
				<%-- 追加開始 --%>
				<TABLE style="border:0px;border-collapse: collapse;float: right;" class="semaku">
					<TR style="border:0px;">
						<TD style="border:0px;" class="semaku">
								<%=i18n.get(GL.OD1102_HIKIATEKIN_SHOSAI)%>
						</TD>
						<TD style="border:0px;">
							<DIV class="ReadOnlybox" style="width:144px;">
								<bean:write name="KakuninSyokaiForm" property="hikiatekin_shosai"/>
							</DIV>
						</TD>
					</TR>
				</TABLE>
				<%-- 追加完了 --%>
				<TR>
				<TABLE style="margin: 4;width:100%;border:0px;border-collapse: collapse;table-layout:fixed;" class="semaku">
					<TR>
						<%-- 区分判定根拠<BR>(前期(半期決算前)) --%>
						<TD style="border:0px;width:20%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OZ6106_ZENKI_COMMOND)%>
						</TD>
						<TD style="border:0px;width:80%;text-align:left;vertical-align:top;">
							<html:textarea name="KakuninSyokaiForm" property="zenki_commond" styleClass="ReadOnlybox" style="height:4em;width:100%;word-wrap: break-word; display: inline;overflow:visible" readonly="true"/>
						</TD>
					</TR>
					<TR>
						<%-- 引当金算定根拠（仮基準） --%>
						<TD style="border:0px;width:20%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OZ6106_KIJUN_COMMOND)%>
						</TD>
						<TD style="border:0px;width:80%;text-align:left;vertical-align:top;">
							<html:textarea name="KakuninSyokaiForm" property="kijun_commond" styleClass="ReadOnlybox" style="height:4em;width:100%;word-wrap: break-word; display: inline; overflow:visible" readonly="true"/>
						</TD>
					</TR>
					<TR>
						<%-- 区分判定根拠（今期） --%>
						<TD style="border:0px;width:40%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OZ6106_KONKI_COMMOND)%>
						</TD>
						<TD style="border:0px;width:40%;text-align:left;vertical-align:top;">
							<DIV style="width:100%;">
								<html:textarea name="KakuninSyokaiForm" property="konki_commond" styleClass="ReadOnlybox" style="height:4em;width:100%;overflow:visible"/>
							</DIV>
						</TD>
					</TR>
				</TABLE>
			</TR>
		</TABLE>
	</DIV>
</DIV>
</html:form>
</DIV>
</BODY>
</HTML>