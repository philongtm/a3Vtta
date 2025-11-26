<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>
<bean:define id="DownloadForm" name="04DownloadForm" type="app.system.form.DownloadForm" />
</HEAD>
<BODY>
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
	<DIV id="head">
		<IMG alt="Sojitz" src="../image/navi001.gif" width="89" height="52">
 		<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="../image/<%=i18n.get(GL.IMG_TITLE)%>.gif" height="54">
	<%-- ヘルプリンク --%>
		<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
	</DIV>
	<%--メニューリンク部分--%>
	<DIV id="menu">
		<%@ include file = "/menu.jspf" %>
	</DIV>
<%--コンテンツ部分--%>
<DIV id="contents">
	<H1 class="title01"><%=i18n.get(GL.TITLE_OS8101)%></H1>
	<DIV id="submenu">
		<%-- 戻る --%>
		<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
	</DIV>
	<DIV id="list">
		<html:form action="/system/download" >
			<DIV class="headerlist">
			<DIV class="left">
				<TABLE>
					<TR><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OS8101_SELECT_LIST)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%></TR>
					<TR class="semaku">
						<%-- 帳票種別 --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.OS8101_LIST_TYPE)%></TD>
						<TD class="semaku"><html:select property="list_type" onchange="doSubmit('list_type')">
							<html:optionsCollection name="DownloadForm" property="ar_list_type" value="value" label="key" />
							</html:select>
						</TD>
					</TR>
				</TABLE>
				<BR>
				<TABLE>
					<TR><%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OS8101_OUTPUT)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%></TR>
					<TR class="semaku">
						<%-- 査定期 --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%></TD>
						<TD class="semaku">
							<html:text property="sateiki" maxlength="6" size="10" style="<%=DownloadForm.getBgcolorSateiki()%>"/><%=i18n.get(GL.OS8101_YM_LABEL)%>
						</TD>
						<%-- 半期四半期区分 --%>
						<nested:equal property="hanki_sihanki_kbn_hyoji_flg" value="true">
							<TD class="semaku"  style="width:100px;"><html:select property="hanki_sihanki_kbn">
								<html:optionsCollection name="DownloadForm" property="ar_hanki_sihanki_kbn" value="value" label="key" />
								</html:select>
							</TD>
						</nested:equal>
						<nested:notEqual property="hanki_sihanki_kbn_hyoji_flg" value="true">
							<TD class="semaku" style="width:100px;"></TD>
						</nested:notEqual>
						<%-- 対象年月 --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.OS8101_TAISYO_YM)%></TD>
						<TD class="semaku">
							<html:text property="taisyo_ym" maxlength="6" size="10" style="<%=DownloadForm.getBgcolorTaisyo_ym()%>"/><%=i18n.get(GL.OS8101_YM_LABEL)%>
						</TD>
						<%-- 〆区分 --%>
						<nested:equal property="sime_kbn_hyoji_flg" value="true">
							<TD class="semaku" style="width:65px;" colspan="2"><html:select property="sime_kbn" style="width:75px;background-color:#FFC1E0;">
								<html:optionsCollection name="DownloadForm" property="ar_sime_kbn" value="value" label="key" />
								</html:select>
							</TD>
						</nested:equal>
						<nested:notEqual property="sime_kbn_hyoji_flg" value="true">
							<TD class="semaku" colspan="2"></TD>
						</nested:notEqual>
					</TR>
					<TR class="semaku">
						<%-- 勘定先CD --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.OS8101_KANJO_CD)%></TD>
						<TD class="semaku" colspan="4">
							<html:text property="kanjo_cd" maxlength="12"/><%=i18n.get(GL.COMMON_ZENPOUICCHI)%>
						</TD>
						<%-- DUNS_NO --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.OS8101_DUNS_NO)%></TD>
						<TD class="semaku">
							<html:text property="duns_no" maxlength="9" style="width:150px"/>
						</TD>
					</TR>
					<TR class="semaku">
						<%-- 勘定先名称 --%>
						<TD class="semaku" style="width:60px;"><%=i18n.get(GL.OS8101_KANJO_NM)%></TD>
						<TD class="semaku" colspan="5">
							<html:text property="kanjo_nm" style="width:320px" styleClass="doubleByte"/><%=i18n.get(GL.COMMON_BUBUNICCHI)%>
						</TD>
					</TR>
					<TR>
						<%-- 汎用１ --%>
						<TD class="semaku" style="width:60px;"><nested:write property="hanyou1Title"/></TD>
<%-- 課題No.195  国内帳票ダウンロード時、必須入力チェックに分類２を追加  --%>
<%-- 追加開始 --%>
<%--						<TD class="semaku" colspan="2"><html:select property="hanyo1"  style="width:70px" onchange="doSubmit('hanyo1')"> --%>
						<TD class="semaku" colspan="2"><html:select property="hanyo1"  style="<%=DownloadForm.getBgcolorHanyou1()%>" onchange="doSubmit('hanyo1')">
<%-- 追加完了 --%>
							<html:option value=""></html:option>
							<html:optionsCollection name="DownloadForm" property="ar_hanyo1" value="value" label="key" />
							</html:select>
						</TD>
						<%-- 汎用２ --%>
						<TD class="semaku" style="width:45px;"><nested:write property="hanyou2Title"/></TD>
						<TD class="semaku" colspan="2"><html:select property="hanyo2" style="width:180px;">
							<html:option value=""></html:option>
							<html:optionsCollection name="DownloadForm" property="ar_hanyo2" value="value" label="key" />
							</html:select>
						</TD>
					</TR>
				</TABLE>
				<DIV id="submenu"class="semaku">
					<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNon('download')">
				</DIV>
			</DIV>
			</DIV>
		</html:form>
	</DIV>
</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
