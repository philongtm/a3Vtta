<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="KakuninForm" name="06KakuninForm" type="app.hikiate.form.KakuninForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
</HEAD>

<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
 		<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>

	<%--メニューリンク部分--%>
	<DIV id="menu">
	<%@ include file = "/menu.jspf" %>
	</DIV>
	
<DIV id="contents">
	<%-- 引当金確認 --%>
	<H1 class="title01"><%=i18n.get(GL.TITLE_OD1102)%></H1>

	<html:form action="/hikiate/kakunin">

	<DIV id="submenu">
		<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
		<%-- もぎ取り解除 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('kaijyo')">
		<%-- 転送 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('tensou')">
		<%-- 一時保存 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('save')">
		<%-- 登録 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('regist')">
		<%-- メニューへ --%>
		<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('menu')"><BR>
		<DIV align="right">
		<%-- 差戻 --%>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="false">
			<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashi')">
		</logic:equal>
		<%-- ダウンロード --%>
		<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')" >
		<%-- 査定結果 --%>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="true">
			<input type="button" value="<%=i18n.get(GL.BTN_SATEI_KEKA)%>" onclick="doSubmit('syosai')">
		</logic:equal>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="false">
			<input type="button" value="<%=i18n.get(GL.BTN_SATEI_KEKA)%>" disabled='true' onclick="doSubmit('syosai')">
		</logic:equal>
		<%-- 添付選択 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPUSENTAKU)%>" onclick="doSubmit('temp')">
		<%} else {%>
		<%-- もぎ取り解除 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_CLEARUSER)%>" onclick="doSubmit('kaijyo')"style="WIDTH: 115px; HEIGHT: 22px">
		<%-- 転送 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TRANSFER)%>" onclick="doSubmit('tensou')"style="WIDTH: 115px; HEIGHT: 22px">
		<%-- 一時保存 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPORALLYSAVE)%>" onclick="doSubmit('save')"style="WIDTH: 115px; HEIGHT: 22px">
		<%-- 登録 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="doSubmit('regist')"style="WIDTH: 115px; HEIGHT: 22px">
		<%-- メニューへ --%>
		<input type="button" value="<%=i18n.get(GL.BTN_MENU)%>" onclick="backConfirm('menu')"style="WIDTH: 115px; HEIGHT: 22px"><BR>
		<DIV align="right">
		<%-- 差戻 --%>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="false">
			<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sashi')"style="WIDTH: 115px; HEIGHT: 22px">
		</logic:equal>
		<%-- ダウンロード --%>
		<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')"style="WIDTH: 115px; HEIGHT: 22px">
		<%-- 査定結果 --%>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="true">
			<input type="button" value="<%=i18n.get(GL.BTN_SATEI_KEKA)%>" onclick="doSubmit('syosai')"style="font-size:10px;WIDTH: 115px; HEIGHT: 22px">
		</logic:equal>
		<logic:equal name="KakuninForm" property="sateiKekkaBtnFlg" value="false">
			<input type="button" value="<%=i18n.get(GL.BTN_SATEI_KEKA)%>" disabled='true' onclick="doSubmit('syosai')"style="font-size:10px;WIDTH: 115px; HEIGHT: 22px">
		</logic:equal>
		<%-- 添付選択 --%>
		<input type="button" value="<%=i18n.get(GL.BTN_TEMPUSENTAKU)%>" onclick="doSubmit('temp')"style="font-size:10px;WIDTH: 115px; HEIGHT: 22px">
		<%}%>
	
		<BR><BR>
		<%-- コメント表示 --%>
		<logic:equal name="TorihikisakiBean" property="sasi_ten_flg" value="1">
			<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('comment')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
		</logic:equal>
		<logic:equal name="TorihikisakiBean" property="sasi_ten_flg" value="2">
			<DIV align="right"><a href="#" style="color:#FF0000;" onClick="doSubmit('comment')" class="linkStyle"><%=i18n.get(GL.LINK_OZ4101)%></a></DIV>
		</logic:equal>
	</DIV>
</DIV>

<DIV id="list">
	<DIV class="mainlist">
		<input type="hidden" name="focusId" value="0">
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;" class="semaku">
				<TD style="border:0px;width:8%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					  <%-- 取引先 --%>
					  <%=i18n.get(GL.OD1102_TORI_CD)%>
          		  </DIV>
				</TD>
				<TD style="border:0px;width:12%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:70px;margin-bottom:5px;">
					  <bean:write name="KakuninForm" property="kanjyo_cd"/><BR>
				  </DIV>
				</TD>
				<TD style="border:0px;width:8%;text-align:left;" class="semaku">
				  <DIV class="dottitle" style="margin-bottom:5px;">
					 <%-- 取引先名 --%> 
					 <%=i18n.get(GL.OD1102_TORI_NM)%>
          		  </DIV>
				</TD>
				<TD colspan="5" style="border:0px;width:70%;text-align:left;" class="semaku">
				  <DIV class="ReadOnlybox" style="width:100%;margin-bottom:5px;">
					  <bean:write name="KakuninForm" property="kanjyo_nm"/>
          		  </DIV>
				</TD>
			</TR>
		</TABLE>
		
		<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
			<TR style="border:0px;">
				<TD style="border:0px;text-align:right;" class="semaku">
					<DIV style="text-align:right;">
						<%-- 承認担当者 --%>
						<%=i18n.get(GL.OD1102_SHOUNIN_TANTOUSHA)%>
						<html:select property="shonin_tanto" style="width:200">
							<html:option value=""></html:option>
							<html:optionsCollection name="KakuninForm" property="ar_shonin_tanto" value="key" label="value" />
						</html:select>
					</DIV>
				</TD>
			</TR>
		</TABLE>

		<TABLE style="border:0px;width:100%;height:100%;border-collapse: collapse;table-layout:fixed;">
			<TR style="border:0px;">
				<TD style="border:0px;width:44%;text-align:right;vertical-align: top;">
					<%-- 単位 --%>
					<%=i18n.get(GL.OD1102_TANI)%>&nbsp;:&nbsp;<bean:write name="KakuninForm" property="tuuka_cd"/><BR>
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;border-left-color: #AAA;">
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
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_ym" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ym" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_ym" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 信用格付 --%>
							<%=i18n.get(GL.OD1102_KTK)%>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 親会社信用格付 --%>
							<%=i18n.get(GL.OD1102_OYA_KTK)%>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_oya_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_oya_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_oya_ktk" readonly="true" styleClass="inputNoL" style="text-align:center"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 親会社名称 --%>
							<%=i18n.get(GL.OD1102_COMPANY_NM)%>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_company_nm" readonly="true" styleClass="inputNoL"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_company_nm" readonly="true" styleClass="inputNoL"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_company_nm" readonly="true" styleClass="inputNoL"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 親会社一体判断 --%>
							<%=i18n.get(GL.OD1102_OYA_FLG)%>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_oya_flg" readonly="true" styleClass="inputNoL"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_oya_flg" readonly="true" styleClass="inputNoL"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_oya_flg" readonly="true" styleClass="inputNoL"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 滞留区分 --%>
							<%=i18n.get(GL.OD1102_TAIRYU_KBN)%>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_tairyu_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tairyu_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_tairyu_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 滞留区分名称 --%>
							<%=i18n.get(GL.OD1102_TAIRYU_KBN_NM)%>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_tairyu_kbn_nm" readonly="true" styleClass="inputNoL3"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 28%; text-align: left;">
							<%-- 取引先区分 --%>
							<%=i18n.get(GL.OD1102_TORIHIKISAKI_KBN)%>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_torihikisaki_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_torihikisaki_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 24%; text-align: left;" class="inputColor">
							<html:select property="konki_torihikisaki_kbn" style="width:100%" >
								<html:optionsCollection name="KakuninForm" property="ar_konki_torihikisaki_kbn" value="value" label="key" />
							</html:select>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 47%; text-align: left;">
							<%-- 債権区分 --%>
							<%=i18n.get(GL.OD1102_SAIKEN_KBN)%>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_saiken_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_saiken_kbn" readonly="true" styleClass="inputNoL3"/>
						</TD>
						<TD style="width: 27%; text-align: left;" class="inputColor">
							<html:select property="konki_saiken_kbn" style="width:100%">
								<html:optionsCollection name="KakuninForm" property="ar_konki_saiken_kbn" value="value" label="key" />
							</html:select>
						</TD>
					</TR>
				</TABLE>
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD colspan="1" rowspan="14" style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD colspan="1" rowspan="12" style="width: 3%;border-bottom-color: #666699;" class="inputRyuhoColor"><br>
						</TD>
						<TD style="width: 22%; text-align: right;" class="inputKanjoColor">
							<%-- 受取手形 --%>
							<%=i18n.get(GL.OD1102_UKETORI_TEGATA)%>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 輸出受取手形 --%>
							<%=i18n.get(GL.OD1102_YUSHUTU_UKETORI_TEGATA)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_yushutu_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_yushutu_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_yushutu_uketori_tegata" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="vertical-align: top; text-align: right;" class="inputKanjoColor">
							<%-- 売掛金 --%>
							<%=i18n.get(GL.OD1102_URIKAKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_urikake_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_urikake_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_urikake_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 取引前渡金 --%>
							<%=i18n.get(GL.OD1102_TORIHIKI_MAEWATASHIE_KIN)%><BR>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_torihiki_maewatashie_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 立替金 --%>
							<%=i18n.get(GL.OD1102_TATEKAE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_tatekae_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tatekae_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_tatekae_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 未収入金 --%>
							<%=i18n.get(GL.OD1102_MISHUUNYUU_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_mishuunyuu_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_mishuunyuu_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_mishuunyuu_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 未収収益 --%>
							<%=i18n.get(GL.OD1102_MISHUU_SHUUEKI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_mishuu_shuueki" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_mishuu_shuueki" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_mishuu_shuueki" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 短期貸付金 --%>
							<%=i18n.get(GL.OD1102_TANKI_KASHITUKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_tanki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tanki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_tanki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 差入保証金 --%>
							<%=i18n.get(GL.OD1102_SASHIIRE_HOSHOU_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_sashiire_hoshou_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_sashiire_hoshou_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_sashiire_hoshou_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 仮払金 --%>
							<%=i18n.get(GL.OD1102_KARIBARAI_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_karibarai_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_karibarai_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_karibarai_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- 長期貸付金 --%>
							<%=i18n.get(GL.OD1102_CHOUKI_KASHITUKE_KIN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_chouki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_chouki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_chouki_kashituke_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="text-align: right;" class="inputKanjoColor">
							<%-- その他投資 --%>
							<%=i18n.get(GL.OD1102_SONOTA_TOUSHI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_sonota_toushi" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_sonota_toushi" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_sonota_toushi" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%-- 一般債権計 --%>
							<%=i18n.get(GL.OD1102_IPPAN_SAIKEN_KEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_ippan_saiken_kei" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ippan_saiken_kei" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_ippan_saiken_kei" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputRyuhoColor">
							<%-- 通貨調整 --%>
							<%=i18n.get(GL.OD1102_TUUKA_CHOUSEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_komoku1" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_komoku1" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_komoku1" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="3" class="inputThColor">
							<%-- 債権残高合計① --%>
							<%=i18n.get(GL.OD1102_SAIKEN_ZANDAKA_GOUKEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_saiken_zandaka_goukei" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_saiken_zandaka_goukei" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_saiken_zandaka_goukei" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
				</TABLE>

				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 留保債務 --%>
							<%=i18n.get(GL.OD1102_RYUUHO_SAIMU)%>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ryuhosaimu" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 第三者留保債務 --%>
							<%=i18n.get(GL.OD1102_NO3_RYUUHO_SAIMU)%>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_oth_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_oth_ryuhosaimu" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_oth_ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 留保債務計② --%>
							<%=i18n.get(GL.OD1102_RYUUHO_SAIMU_KEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_ryuuho_saimu_kei" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ryuuho_saimu_kei" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_ryuuho_saimu_kei" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 保全③ --%>
							<%=i18n.get(GL.OD1102_HOZEN)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_hozen" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_hozen" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_hozen" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- その他回収④ --%>
							<%=i18n.get(GL.OD1102_SONOTA_KAISHUU)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_sonotakaisyu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_sonotakaisyu" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_sonotakaisyu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
				</TABLE>

				<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="1" colspan="2" style="border-bottom:none;"  class="inputThColor" >
						<%-- 保証債務合計 --%>
						<%=i18n.get(GL.OD1102_HOSHOU_SAIMU_GOUKEI)%>
					</TD>
					<TD style="width: 24%; text-align: right;" class="inputNoColor">
						<html:text name="KakuninForm" property="zenki_hoshou_saimu_goukei" readonly="true" styleClass="inputNo"/>
					</TD>
					<TD style="width: 24%; text-align: right;" class="inputNoColor">
						<html:text name="KakuninForm" property="kijun_hoshou_saimu_goukei" readonly="true" styleClass="inputNo"/>
					</TD>
					<TD style="width: 24%; text-align: right;" class="inputNoColor">
						<html:text name="KakuninForm" property="konki_hoshou_saimu_goukei" readonly="true" styleClass="inputNo"/>
					</TD>
				</TR>
				<TR>
					<TD style="width: 3%; border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
					</TD>
					<TD style="width: 25%; text-align: right; border:solid 1px #AAA;">
						<%-- 履行請求懸念⑤ --%>
						<%=i18n.get(GL.OD1102_RIKOU_SEIKYUU_KENEN)%>
					</TD>
					<TD style="width: 24%; text-align: right;"  class="inputNoColor">
						<input type="text" name="zenki_riko_kenen" tabindex="-1" value="-" readonly="readonly" class="inputNo">
					</TD>
					<TD style="width: 24%; text-align: right;"  class="inputNoColor">
						<html:text name="KakuninForm" property="kijun_riko_kenen" readonly="true" styleClass="inputNo"/>
					</TD>
					<TD style="width: 24%; text-align: right;"  class="inputNoColor">
						<input type="text" name="konki_riko_kenen" tabindex="-1" value="-" readonly="readonly" class="inputNo">
					</TD>
				</TR>
				</TABLE>
				
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 28%;" class="inputThColor">
							<%-- 既引当金⑥ --%>
							<%=i18n.get(GL.OD1102_KI_HIKIATE_KIN)%>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="zenki_ki_hikiate_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_ki_hikiate_kin" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="konki_ki_hikiate_kin" readonly="true" styleClass="inputNo"/>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 54%;" class="inputThColor">
							<%-- 引当対象金額<BR>[ ①-(②+③+④)+⑤-⑥ ] --%>
							<%=i18n.get(GL.OD1102_HIKIATE_TAISHOU_KINGAKU)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="zenki_hikiate_taishou_kingaku" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_hikiate_taishou_kingaku" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="konki_hikiate_taishou_kingaku" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
				</TABLE>
				<TABLE style="width:100%;border-collapse: collapse;table-layout:fixed;">
					<TR style="border:0px;">
						<TD style="width: 3%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 25%; text-align: right;">
							<%-- 追加引当金 --%>
							<%=i18n.get(GL.OD1102_TUIKA_HIKIATE_KIN)%>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tuika_hikiate" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 24%; text-align: right;" class="inputNoColor">
							<input type="text" name="ryuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="width: 7%;border-bottom-color: #330088;" class="inputThColor"><br>
						</TD>
						<TD style="width: 47%; text-align: right;">
							<%-- 通貨調整 --%>
							<%=i18n.get(GL.OD1102_TUUKA_CHOUSEI)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="ryuhosaimu3" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_komoku2" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="ryuhosaimu3" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD rowspan="1" colspan="2" class="inputThColor">
							<%-- 追加引当金(調整後) --%>
							<%=i18n.get(GL.OD1102_TUIKA_HIKIATE_KIN_CHOUSEI_USIRO)%>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="totalRyuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<html:text name="KakuninForm" property="kijun_tuika_hikiate_usiro" readonly="true" styleClass="inputNo"/>
						</TD>
						<TD style="width: 27%; text-align: right;" class="inputNoColor">
							<input type="text" name="totalRyuhosaimu" tabindex="-1" value="-" readonly="readonly" class="inputNo">
						</TD>
					</TR>
				</TABLE>
				
				</TD>
				<%-- 課題No.28 引当対象外/帳簿外対応 --%>
				<%-- 追加開始 --%>
				<TABLE style="border:0px;border-collapse: collapse;width:100%;" class="semaku">
					<TR style="border:0px;">
						<TD style="border:0px;text-align:right;" class="semaku">
							<DIV style="text-align:right;"><%=i18n.get(GL.OD1102_HIKIATEKIN_SHOSAI)%>
							<html:select property="hikiatekin_shosai"><html:optionsCollection name="KakuninForm" property="ar_hikiatekin_shosai" value="value" label="key" />
							</DIV>
							</html:select>
						</TD>
					</TR>
				</TABLE>
				<%-- 追加完了 --%>
				<TABLE style="width:100%;border:0px;border-collapse: collapse;table-layout:fixed;" class="semaku">
					<TR>
						<%-- 区分判定根拠<BR>(前期(半期決算前)) --%>
						<TD style="border:0px;width:20%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OD1102_ZENKI_COMMOND)%>
						</TD>
						<TD style="border:0px;width:80%;text-align:left;vertical-align:top;">
							<html:textarea name="KakuninForm" property="zenki_commond" styleClass="ReadOnlybox" style="height:4em;width:100%;word-wrap: break-word; display: inline;overflow:visible" readonly="true"/>
						</TD>
					</TR>
					<TR>
						<%-- 引当金算定根拠（仮基準） --%>
						<TD style="border:0px;width:20%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OD1102_KIJUN_COMMOND)%>
						</TD>
						<TD style="border:0px;width:80%;text-align:left;vertical-align:top;">
							<html:textarea name="KakuninForm" property="kijun_commond" styleClass="ReadOnlybox" style="height:4em;width:100%;word-wrap: break-word; display: inline; overflow:visible" readonly="true"/>
						</TD>
					</TR>
					<TR>
						<%-- 区分判定根拠（今期） --%>
						<TD style="border:0px;width:40%;text-align:left;vertical-align:top;">
							&nbsp;<%=i18n.get(GL.OD1102_KONKI_COMMOND)%>
						</TD>
						<TD style="border:0px;width:40%;text-align:left;vertical-align:top;">
							<DIV style="width:100%;">			
								<html:textarea name="KakuninForm" property="konki_commond" style="height:4em;width:100%;"/>
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
</DIV>
</CENTER>
</BODY>
</HTML>