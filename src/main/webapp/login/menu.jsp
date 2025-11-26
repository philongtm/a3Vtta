<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>
<HTML>
<HEAD>
<%@ include file = "../include/jspHeader.jsp" %>
<%@ include file = "../include/jspUtil.jsp" %>
<link rel="stylesheet" href="../css/Login.css" type="text/css">
<bean:define id="MenuForm" name="00MenuForm" type="app.login.form.MenuForm" />
<script>
	function setWorkFlow() {
		form = document.forms[0];
		var n = form.elements["pattern"].selectedIndex;
		var lstId = document.getElementsByName("tmp_pattern_id");
		var lstSystemKbn = document.getElementsByName("tmp_pattern_system_kbn");
		var lstSateikaisyaCd = document.getElementsByName("tmp_pattern_sateikaisya_cd");
		form.elements["pattern_id"].value = lstId[n].value;
		form.elements["pattern_system_kbn"].value = lstSystemKbn[n].value;
		form.elements["pattern_sateikaisya_cd"].value = lstSateikaisyaCd[n].value;
		doSubmit("pattern");
	}
	function setTempDaiko(obj) {
		form = document.forms[0];
		form.elements["temp_daiko"].value = obj.value;
	}
</script>
</HEAD>
<BODY id="menu">
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

		<html:form action="/login/menu" >
			<html:hidden name="MenuForm" property="pattern_id"/>
			<html:hidden name="MenuForm" property="pattern_system_kbn"/>
			<html:hidden name="MenuForm" property="pattern_sateikaisya_cd"/>
			<html:hidden name="MenuForm" property="temp_daiko"/>
			<DIV id="list">

				<DIV class="headerlist">
					<logic:equal name="MenuForm" property="pattern_flg" value="true">
						<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
							<%-- 業務フロー切替 --%>
							<TR>
								<TD style="border:0px;width:12%;float:left;"><%=i18n.get(GL.OS2101_GYOUMU_HURO)%>&nbsp;</TD>				
								<%-- 課題No.55 プルダウン幅調整 --%>
								<%-- 修正開始 --%>
								<%-- <TD class="" style="border:0px;width:25%;float:left;">
									<html:select property="pattern" onchange="setWorkFlow()" style="border:0px;width:100%;float:left;"> --%>
								<TD class="" style="border:0px;float:left;">
									<html:select property="pattern" onchange="setWorkFlow()" style="border:0px;float:left;">
								<%-- 修正完了 --%>
											<html:optionsCollection name="MenuForm" property="ar_pattern" value="id" label="pattern_name" />
									</html:select>
									<logic:iterate id="workFlow" name="MenuForm" property="ar_pattern">
										<input type="hidden" name="tmp_pattern_id" value="<bean:write name='workFlow' property='pattern_id'/>"/>
										<input type="hidden" name="tmp_pattern_system_kbn" value="<bean:write name='workFlow' property='system_kbn'/>"/>
										<input type="hidden" name="tmp_pattern_sateikaisya_cd" value="<bean:write name='workFlow' property='sateikaisya_cd'/>"/>
									</logic:iterate>
								</TD>
								<%-- 課題No.55 プルダウン幅調整 --%>
								<%-- 削除開始 --%>
								<TD style="border:0px;width:63%;text-align:right;">
									<a href="#" name="lnkJa" onClick="doSubmit('gengo_j');">Japanese</a>
									&nbsp;/&nbsp;
									<a href="#" name="lnkEn" onClick="doSubmit('gengo_e');">English</a>
								</TD>
								<%-- 削除完了 --%>
							</TR>
						</TABLE><BR>
					</logic:equal>
					<logic:equal name="MenuForm" property="pattern_flg" value="false">
						<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
							<%-- 業務フロー切替なし --%>
							<TR>
								<TD style="border:0px;width:12%;float:left;">&nbsp;</TD>
								<TD class="" style="border:0px;float:left;">&nbsp;</TD>
								<TD style="border:0px;width:63%;text-align:right;">
									<a href="#" name="lnkJa" onClick="doSubmit('gengo_j');">Japanese</a>
									&nbsp;/&nbsp;
									<a href="#" name="lnkEn" onClick="doSubmit('gengo_e');">English</a>
								</TD>
							</TR>
						</TABLE><BR>
					</logic:equal>

					<H1 class="title01"><%=i18n.get(GL.OS2101_TITLE_STATUS)%></H1>

					<TABLE border=0 cellSpacing=0 cellPadding=0 widht="100%">						
						<TR>
							<%-- 査定期 --%>
							<TD style="border:0px;width:14%;" class="none_b"><%=i18n.get(GL.OS2101_SATEI_KI)%></TD>
							<TD colspan="3" width="15%" class="none_b">
								<html:select property="satei_ki" onchange="doSubmit('satei')" style="width:80">
										<html:optionsCollection name="MenuForm" property="ar_satei_ki" value="value" label="key" />
								</html:select>
							</TD>
							<%-- 対象年月 --%>							
							<TD style="border:0px;width:10%;"><%=i18n.get(GL.OS2101_YM)%></TD>
							<TD colspan="3" width="31%" class="none_b">
								<html:select property="ym" onchange="doSubmit('taishou_ym')" style="width:80">
										<html:optionsCollection name="MenuForm" property="ar_ym" value="value" label="key" />
								</html:select>
							</TD>
							<%-- 代行画面切替 --%>	
							<TD class="right" style="border:0px;width:18%;"><%=i18n.get(GL.OS2101_DAIKO_GAMEN)%>&nbsp;</TD>
							<TD colspan="3" width="27%" class="none_b">
								<html:select property="daiko" onfocus="setTempDaiko(this)" onchange="doSubmit('daiko')" style="width:200;float:right;">
										<html:optionsCollection name="MenuForm" property="ar_daiko" value="key" label="value" />
								</html:select>
							</TD>			
						</TR>
					</TABLE>
				</DIV>
				<DIV class="mainlist">
	
					<%-- ステータス.一覧情報 --%>
					<TABLE width="100%">
						<THEAD>
						<%-- A,B,Cの説明 --%>	
							<TR>	
								<%-- 課題No.119 --%>
								<%-- 修正開始 --%>
								<%-- <TH colspan="14" class="none_b"><span class="come"><span class="come"><%=i18n.get(GS.KOMEJIRUSI)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_MI_SYORI_A)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_SYORI_TYU_B)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_SYORI_ZUMI_C)%><%=i18n.get(GS.SPACE_CHARCTER)%></b></span></TH> --%>
								<TH colspan="14" class="none_b"><span class="come"><span class="come"><%=i18n.get(GL.COMMON_KOMEJIRUSI)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_MI_SYORI_A)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_SYORI_TYU_B)%><%=i18n.get(GS.SPACE_CHARCTER)%><%=i18n.get(GL.OS2101_SYORI_ZUMI_C)%><%=i18n.get(GS.SPACE_CHARCTER)%></b></span></TH>
								<%-- 修正完了 --%>
							</TR>			
							<TR>	
								<TH colspan="4" width="50%" class="none_b"></TH>
								<%-- 実質滞留債権判定 --%>
								<TH width="15%" colspan="3" ><p class="center"><%=i18n.get(GL.OS2101_JISSHI_TAIRYU_HANTEI)%></p></TH>
								<%-- 査定 --%>
								<TH width="20%" colspan="4" ><p class="center"><%=i18n.get(GL.OS2101_SATEI)%></p></TH>
								<%-- 汎用３ --%>
								<TH width="15%" colspan="3" ><p class="center"><%=SESSION_DATA_APP.getLbl_nm4()%></p></TH>						
							</TR>
							
							<TR>
								<%-- 汎用１ --%>
								<TH width="6%"rowspan="2"><p class="center"><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>	
								<%-- 汎用２ --%>	
								<TH width="28%"rowspan="2"><p class="center"><%=SESSION_DATA_APP.getLbl_nm7()%></p></TH>
								<%-- 対象年月 --%>
								<TH width="8%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_YM)%></p></TH>
								<%-- 受信日 --%>
								<TH width="8%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_JUSHIN_BI)%></p></TH>
								<%-- 実質滞留債権判定.未処理 --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_A)%></p></TH>
								<%-- 実質滞留債権判定.処理中 --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_B)%></p></TH>
								<%-- 実質滞留債権判定.済み --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_C)%></p></TH>
								<%-- 査定.未処理 --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_A)%></p></TH>
								<%-- 査定.処理中--%>
								<TH width="10%"colspan="2"><p class="center"><%=i18n.get(GL.OS2101_B)%></p></TH>
								<%-- 査定.済み --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_C)%></p></TH>
								<%-- 汎用３.未処理 --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_A)%></p></TH>
								<%-- 汎用３.処理中 --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_B)%></p></TH>
								<%-- 汎用３.済み --%>
								<TH width="5%"rowspan="2"><p class="center"><%=i18n.get(GL.OS2101_C)%></p></TH>				
							</TR>	
							<TR>
								<%-- 査定.一次査定中 --%>
								<TH width="5%" ><p class="center"><%=i18n.get(GL.OS2101_SATEI_ICHI)%></p></TH>
								<%-- 査定.二次査定中 --%>
								<TH width="5%" ><p class="center"><%=i18n.get(GL.OS2101_SATEI_NI)%></p></TH>
							</TR>		
						</THEAD>
						<TBODY>		
							<% if(MenuForm.getAr_status() != null) { %>	
								<nested:iterate name="MenuForm" property="ar_status" indexId="idx">
									<nested:equal property="niju_jushin_flg" value="1">
										<TR style="background-color:#FFC1E0">
									</nested:equal>
									<nested:notEqual property="niju_jushin_flg" value="1">
										<TR>
									</nested:notEqual>
										<%-- 汎用１ --%>
										<TD class="tdlb"><nested:write property="sateikaisya_cd" /></TD>
										<%-- 汎用２ --%>
										<TD style="word-break: normal;"><nested:write property="soshiki" />&nbsp;</TD>
										<%-- 対象年月 --%>
										<TD><nested:write property="ym" />&nbsp;</TD>
										<%-- 受信日 --%>
										<TD><nested:write property="jushin_bi_hyoji" />&nbsp;</TD>
										<%-- 実質滞留債権判定.未処理 --%>
										<TD class="right"><nested:write property="tairyuu_mi_shori" />&nbsp;</TD>
										<%-- 実質滞留債権判定.処理中 --%>
										<TD class="right"><nested:write property="tairyuu_shori" />&nbsp;</TD>
										<%-- 実質滞留債権判定.済み --%>
										<TD class="right"><nested:write property="tairyuu_zumi" />&nbsp;</TD>
										<%-- 査定.未処理 --%>
										<TD class="right"><nested:write property="satei_mi_shori" />&nbsp;</TD>
										<%-- 査定.一次査定中 --%>
										<TD class="right"><nested:write property="satei_ichi" />&nbsp;</TD>
										<%-- 査定.二次査定中 --%>
										<TD class="right"><nested:write property="satei_ni" />&nbsp;</TD>
										<%-- 査定.済み --%>
										<TD class="right"><nested:write property="satei_zumi" />&nbsp;</TD>
										<%-- 汎用３.未処理 --%>
										<TD class="right"><nested:write property="hanyou_mi_syori" />&nbsp;</TD>
										<%-- 汎用３.処理中 --%>
										<TD class="right"><nested:write property="hanyou_syori" />&nbsp;</TD>
										<%-- 汎用３.済み --%>
										<TD class="right"><nested:write property="hanyou_zumi" />&nbsp;</TD>				
									</TR>
								</nested:iterate>
							<% } %>
						</TBODY>
					
					</TABLE>
					
					<br/><br/>
					
					<%-- 査定結果タイトル --%>
					<H1 class="title01"><%=i18n.get(GL.OS2101_TITLE_RESUALT)%></H1>
					
					<%-- 査定結果.一覧情報 --%>
					<TABLE width="100%">
						<THEAD>				
							<TR>
								<%-- ※( ) 内は滞留件数 --%>
								<TH colspan="5" class="none_b"><span class="come"><%=i18n.get(GL.OS2101_TAIRYUUKENSUU)%></span></TH>
							</TR>
							<TR>	
								<TH colspan="2" class="none_b"></TH>
								<%-- 貸倒懸念・破産更生債権判定先件数 --%>
								<TH colspan="3" ><p class="center"><%=i18n.get(GL.OS2101_KASHIDAOREKENEN)%></p></TH>
							</TR>
							<TR>
								<%-- 汎用１ --%>
								<TH class="thlb" width=11%><p class="center"><%=SESSION_DATA_APP.getLbl_nm1()%></p></TH>
								<%-- 汎用２ --%>
								<TH><p class="center"><%=SESSION_DATA_APP.getLbl_nm7()%></p></TH>
								<logic:empty name="MenuForm" property="middle_ym_list">
									<%-- 貸倒懸念・破産更生債権判定先件数（基準月（最終月））タイトル --%>
									<TH width=12%><p class="center"><bean:write name="MenuForm" property="last_ym_hy"/></p></TH>
									<bean:define id="middle_ym" name="MenuForm" property="middle_ym_list"/>
									<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（中間月））タイトル(改行し2段で表示する) --%>
									<TH width=12%><p class="center"><bean:write name="MenuForm" property="first_ym_hy"/></p></TH>
									<%-- 中間月は空欄となります --%>
									<TH width=12%><p class="center">&nbsp;</p></TH>									
								</logic:empty>
								<logic:notEmpty name="MenuForm" property="middle_ym_list">
									<%-- 貸倒懸念・破産更生債権判定先件数（基準月（最終月））タイトル --%>
									<TH width=12%><p class="center"><bean:write name="MenuForm" property="last_ym_hy"/></p></TH>
									<bean:define id="middle_ym" name="MenuForm" property="middle_ym_list"/>
									<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（中間月））タイトル(改行し2段で表示する) --%>
									<TH width=12%><p class="center"><%=middle_ym%></p></TH>
									<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（初回月））タイトル --%>
									<TH width=12%><p class="center"><bean:write name="MenuForm" property="first_ym_hy"/></p></TH>									
								</logic:notEmpty>

							</TR>
						</THEAD>
						<TBODY>
							<% if(MenuForm.getAr_satei_resualt() != null) { %>	
								<nested:iterate name="MenuForm" property="ar_satei_resualt" indexId="idx">
									<TR>
										<%-- 汎用１ --%>
										<TD class="tdlb"><nested:write property="satei_kaisha_cd" /></TD>
										<%-- 汎用２ --%>
										<TD><nested:write property="soshiki" /></TD>
										<nested:empty name="MenuForm" property="middle_ym_list">
											<%-- 貸倒懸念・破産更生債権判定先件数（基準月（最終月）） --%>
											<TD class="right">&nbsp;<nested:write property="hasan_num_last" />&nbsp;<nested:write property="hasan_num_last_kakko" /></TD>
											<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（初回月）） --%>
											<TD class="right">&nbsp;<nested:write property="hasan_num_first" />&nbsp;<nested:write property="hasan_num_first_kakko" /></TD>
											<%-- 中間月は空欄となります --%>
											<TD class="right">&nbsp;</TD>								
										</nested:empty>
										<nested:notEmpty name="MenuForm" property="middle_ym_list">
											<%-- 貸倒懸念・破産更生債権判定先件数（基準月（最終月）） --%>
											<TD class="right">&nbsp;<nested:write property="hasan_num_last" />&nbsp;<nested:write property="hasan_num_last_kakko" /></TD>
											<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（中間月） --%>
											<TD class="right">&nbsp;<nested:write property="hasan_num_middle" />&nbsp;<nested:write property="hasan_num_middle_kakko" /></TD>
											<%-- 貸倒懸念・破産更生債権判定先件数（仮基準月（初回月）） --%>
											<TD class="right">&nbsp;<nested:write property="hasan_num_first" />&nbsp;<nested:write property="hasan_num_first_kakko" /></TD>								
										</nested:notEmpty>
									</TR>
								</nested:iterate>
							<% } %>
						</TBODY>
					</TABLE>
				</DIV>
			</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
</BODY>
</HTML>
