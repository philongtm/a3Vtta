<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="SateisyosaiForm" name="03SateisyosaiForm" type="app.syokai.form.SateisyosaiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />

<script>
	function changeTab(tabIdx){
		document.getElementById("currentTab").value = tabIdx;
		doSubmit('changeTab');
	}
</script>

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

	<%--コンテンツ部分--%>
	<DIV id="contents">
		<html:form action="/syokai/sateisyosai" >
		<nested:hidden property="currentTab" styleId="currentTab"></nested:hidden>
		
		<%-- OS6102_査定内容詳細  --%>
		<H1 class="title01"><%=i18n.get(GL.TITLE_OS6102)%></H1>
		<DIV id="submenu">
		<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
			<%-- 差戻 --%>
			<nested:equal property="btn_sasimodo_flg" value="<%= GS.ON %>">
				<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sasiModosi')">
			</nested:equal>
			<%-- 添付参照 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpuSansyo')">
			<%-- ダウンロード --%>
			<nested:equal property="btn_download_flg" value="<%= GS.ON %>">
			<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')">
			</nested:equal>
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		<%} else {%>
			<%-- 差戻 --%>
			<nested:equal property="btn_sasimodo_flg" value="<%= GS.ON %>">
				<input type="button" value="<%=i18n.get(GL.BTN_SASHIMODOSHI)%>" onclick="doSubmit('sasiModosi')">
			</nested:equal>
			<%-- 添付参照 --%>
			<input type="button" value="<%=i18n.get(GL.BTN_TENPU_SANSYO)%>" onclick="doSubmit('tenpuSansyo')"style="font-size:10px;WIDTH: 90px; HEIGHT: 20px">
			<%-- ダウンロード --%>
			<nested:equal property="btn_download_flg" value="<%= GS.ON %>">
			<input type="button" value="<%=i18n.get(GL.BTN_DOWNLOAD)%>" onclick="doSubmitNonDownload('download','<%=SESSION_DATA_APP.getUser_bean().getComTyohyo_default_kbn()%>')">
			</nested:equal>
			<%-- 戻る --%>
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
		<%}%>
			<%-- コメント表示 --%>
			<nested:equal property="link_comment_flg" value="<%= GS.ON %>">
				<BR><BR>
				<DIV align="right">
					<a href="#" style="color:#FF0000;" onClick="doSubmit('comment')">
						<%=i18n.get(GL.LINK_OZ4101)%>
					</a>
				</DIV>
			</nested:equal>
		</DIV>
		<BR><BR><BR><BR><BR>
		<DIV id="list">
			<DIV id="headlist">
				<DIV class="CNDbox" style="float:left;">
					<%-- フェーズ --%>
					<%=i18n.get(GL.OS6102_PHASE)%>
					<nested:equal property="disp_phase_flg" value="<%= GS.ON %>">
						<html:select property="anken_phase" onchange="doSubmit('phase')">
							<html:optionsCollection name="SateisyosaiForm" property="ar_phase" value="value" label="key" />
						</html:select>
					</nested:equal>
					<nested:equal property="disp_phase_flg" value="<%= GS.OFF %>">
						<html:select property="anken_phase" disabled="true">
							<html:optionsCollection name="SateisyosaiForm" property="ar_phase" value="value" label="key" />
						</html:select>
					</nested:equal>
					
				</DIV>
				<BR><BR>
				<TABLE style="border:0px;width:100%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
					<TR style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
						<%-- 対象年月 --%>
						<TD style="border:0px;width:9%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="dottitle"><%=i18n.get(GL.OS6102_YM)%></DIV><BR>
						</TD>
						<TD colspan="1" style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="ReadOnlybox" style="width: 100%;" >
								<bean:write name="TorihikisakiBean" property="taisyo_ym_hyoji" />
							</DIV><BR>
						</TD>
						<%-- 汎用1 --%>
						<TD style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="dottitle"><%=SESSION_DATA_APP.getLbl_nm1()%></DIV><BR>
						</TD>
						<TD colspan="1" style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="ReadOnlybox" style="width: 100%;" >
								<bean:write name="TorihikisakiBean" property="sateikaisya_cd" />
							</DIV><BR>
						</TD>
						<%-- 組織 --%>
						<TD style="border:0px;width:5%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="dottitle"><%=i18n.get(GL.OS6102_SOSHIKI)%></DIV><BR>
						</TD>
						<TD colspan="1" style="border:0px;width:56%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="ReadOnlybox" style="width: 100%;" >
								<bean:write name="TorihikisakiBean" property="soshiki" />
							</DIV><BR>
						</TD>
					</TR>
					<TR style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
						<%-- 勘定先CD --%>
						<TD style="border:0px;width:9%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="dottitle"><%=i18n.get(GL.OS6102_KANJYOCD)%></DIV><BR>
						</TD>
						<TD colspan="1" style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="ReadOnlybox" style="width: 100%;" >
								<bean:write name="TorihikisakiBean" property="kanjo_cd" />
							</DIV><BR>
						</TD>
						<%-- 勘定先名称 --%>
						<TD style="border:0px;width:5%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="dottitle"><%=i18n.get(GL.OS6102_KANJYONAME)%></DIV><BR>
						</TD>
						<TD colspan="3" style="border:0px;width:81%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
							<DIV class="ReadOnlybox" style="width: 100%;" >
								<bean:write name="TorihikisakiBean" property="kanjo_nm" />
							</DIV><BR>
						</TD>
					</TR>
				</TABLE>
				<TABLE style="border:0px;width:100%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
					<TR style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
						<TD colspan="4" style="border:0px;width:20%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;font-weight: bold;">
							<%-- [登録担当者/登録日時(処理日時タイトル)] --%>
							<DIV class="dottitle">
								[<%=i18n.get(GL.OS6102_TOROKU_TANTO)%><bean:write name="SateisyosaiForm" property="syoribiTitle" />]
							</DIV>
						</TD>
						<TD colspan="4" style="border:0px;width:40%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;font-weight: bold;">
							<%-- [承認担当者/承認日時(処理日時タイトル)] --%>
							<DIV class="dottitle">
								[<%=i18n.get(GL.OS6102_SYONIN_TANTO)%><bean:write name="SateisyosaiForm" property="syoribiTitle" />]
							</DIV>
						</TD>
					</TR>
					<%-- 登録/承認担当者【リスト】は繰り返し --%>
					<nested:notEmpty name="SateisyosaiForm" property="ar_toroku_shounin">
						<nested:iterate name="SateisyosaiForm" property="ar_toroku_shounin" indexId="idx">
							<TR>
								<TD style="border:0px;width:2%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">&nbsp;</TD>
								<%-- フェーズ --%>
								<TD style="border:0px;width:10%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<DIV class="dottitle"><nested:write property="phase" /></DIV>
								</TD>
								<%-- 登録担当者 --%>
								<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="touroku_tanto" /></DIV>
								</TD>
								<%-- 登録日時 --%>
								<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="touroku_syori_dt" /></DIV>
								</TD>
								<TD style="border:0px;width:2%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">&nbsp;</TD>
								<%-- 承認担当者 --%>
								<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="shounin_tanto" /></DIV>
								</TD>
								<%-- 承認日時 --%>
								<TD style="border:0px;width:15%;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">
									<DIV class="ReadOnlybox" style="width:100%;" ><nested:write property="shounin_syori_dt" /></DIV>
								</TD>
								<TD style="border:0px;margin-top: 0px;margin-bottom: 0px;padding-top: 0px;padding-bottom: 0px;">&nbsp;</TD>
							</TR>
						</nested:iterate>
					</nested:notEmpty>
				</TABLE>
			</DIV>
			<%-- 一覧情報 --%>
			<DIV class="mainlist">
				<%-- 取引先概要タブ --%>
				<nested:equal property="dispTabTorihikiGaiyo" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="1">
							<span><%=i18n.get(GL.TITLE_OZ6102)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="1">
							<a href="#" onClick="changeTab('1')"><%=i18n.get(GL.TITLE_OZ6102)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 取引先・債権区分判定タブ --%>
				<nested:equal property="dispTabSaikenKubun" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="2">
							<span><%=i18n.get(GL.TITLE_OZ6103)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="2">
							<a href="#" onClick="changeTab('2')"><%=i18n.get(GL.TITLE_OZ6103)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 引当金判定タブ --%>
				<nested:equal property="dispTabHikiateHantei" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="3">
							<span><%=i18n.get(GL.TITLE_OZ6104)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="3">
							<a href="#" onClick="changeTab('3')"><%=i18n.get(GL.TITLE_OZ6104)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 債権明細タブ --%>
				<nested:equal property="dispTabSaikenMeisai" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="4">
							<span><%=i18n.get(GL.TITLE_OZ6105)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="4">
							<a href="#" onClick="changeTab('4')"><%=i18n.get(GL.TITLE_OZ6105)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 留保債務タブ --%>
				<nested:equal property="dispTabRyuhoSaimu" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="5">
							<span><%=i18n.get(GL.TITLE_OZ6107)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="5">
							<a href="#" onClick="changeTab('5')"><%=i18n.get(GL.TITLE_OZ6107)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 滞留債権明細タブ --%>
				<nested:equal property="dispTabTairyuSaiken" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="6">
							<span><%=i18n.get(GL.TITLE_OZ6101)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="6">
							<a href="#" onClick="changeTab('6')"><%=i18n.get(GL.TITLE_OZ6101)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 引当金確認タブ --%>
				<nested:equal property="dispTabHikiateKakunin" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="7">
							<span><%=i18n.get(GL.TITLE_OZ6106)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="7">
							<a href="#" onClick="changeTab('7')"><%=i18n.get(GL.TITLE_OZ6106)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<%-- 引当金検証タブ --%>
				<nested:equal property="dispTabHikiateKensyo" value="<%= GS.ON %>">
					<DIV id="tab">
						<nested:equal property="currentTab" value="8">
							<span><%=i18n.get(GL.TITLE_HIKIATEKENSYO_TAB)%></span>
						</nested:equal>
						<nested:notEqual property="currentTab" value="8">
							<a href="#" onClick="changeTab('8')"><%=i18n.get(GL.TITLE_HIKIATEKENSYO_TAB)%></a>
						</nested:notEqual>
					</DIV>
				</nested:equal>
				<nested:define id="jspPath" property="jspPath"/>
				<iframe src="<%= jspPath %>" width=100% height=700px></iframe>
			</DIV>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
