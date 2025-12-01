<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="SyosaiForm" name="01SyosaiForm" type="app.tairyu.form.SyosaiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<bean:define id="MeisaisyosaiBean" name="app.SessionData" property="syosai_bean" type="app.MeisaisyosaiBean" />
<script>
	function downloadOpen(event,id,fileNm){		
		form = document.forms[0];
		form.elements["id"].value = id;
		ext = new Array();
		ext = fileNm.split(".");
		extLength = ext.length;
		if(ext[extLength-1].toLowerCase() == "html" || ext[extLength-1].toLowerCase() == "htm" || ext[extLength-1].toLowerCase() == "xml"){
			form.target = "_blank";
		}else{
			form.target = "_top";
		}
		tmpAction = form.action;
		form.action += "?<%=GS.EVENT%>=" + event + "&index=" + id;
		form.submit();
		form.action = tmpAction;
	}
</script>
</HEAD>
<BODY onload="">
<CENTER>

<DIV id="main">
	<%--ヘッダ部分--%>
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
		<H1 class="title01"><%=i18n.get(GL.TITLE_OB1103)%></H1>
		<html:form action="/tairyu/syosai">
		<html:hidden property="id" />
	
		<DIV id="submenu">
			<input type="button" value="<%=i18n.get(GL.BTN_SAVE)%>" onclick="doSubmit('save')" />
			<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')" />
		</DIV>
	
		<DIV id="list">
			<DIV class="headlist">
				<TABLE class="none" style="width:100%; border:0px; cellSpacing:0px; cellPadding:0px; table-layout:fixed;">
					<TR style="border:0px;width:100%;">
						<TD style="width:18%; border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 勘定先CD --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KANJYOSAKICD)%></DIV>
						</TD>
						<TD colspan="7" style="width:87%; border:0px;margin: 0 0 0 0;padding: 0px;">
							<DIV class="ReadOnlybox" style="width:16%;"><bean:write name="TorihikisakiBean" property="kanjo_cd"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 勘定先名称 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KANJYOSAKINAME)%></DIV>
						</TD>
						<TD colspan="7" style="border:0px;margin: 0 0 0 0;padding: 0px;">
							</nowrap>
							<DIV class="ReadOnlybox" style="width:70%; word-break:break-all;"><bean:write name="TorihikisakiBean" property="kanjo_nm"/></DIV>
						</TD>
					</TR>	
					<TR style="border:0px;">	
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 信用格付 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_SHINYOKAKUDUKE)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7" class="semaku">
							<DIV class="ReadOnlybox" style="width:8%;text-align: center;"><bean:write name="TorihikisakiBean" property="sinyoktk"/></DIV>
						</TD>
						
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 枝番 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_EDABAN)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:8%;"><bean:write name="MeisaisyosaiBean" property="anken_no_eda"/>&nbsp;</DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- セル --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_CELL)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:60%;"><bean:write name="MeisaisyosaiBean" property="cell_nm"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 勘定科目 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KANJYOKAMOKU)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:60%;"><bean:write name="MeisaisyosaiBean" property="kanjo_kamoku_nm"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 収支予定日 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_SYUSIYOTEIBI)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<DIV class="ReadOnlybox" style="width:95px;"><bean:write name="MeisaisyosaiBean" property="syusi_yoteibi"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" align="right">
						<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
							<%-- 満期日 --%>
							<DIV class="dottitle;right;" style="margin-top:1px;"><%=i18n.get(GL.OB1103_MANKIBI)%></DIV>
						</logic:equal>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
						<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
							<DIV class="ReadOnlybox"  style="width:95px;"><bean:write name="MeisaisyosaiBean" property="mankibi"/></DIV>
						</logic:equal>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" align="right">
							<%-- 勘定処理日 --%>
							<DIV class="dottitle;right;" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KANJYOSHORIBI)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<DIV class="ReadOnlybox"  style="width:95px;"><bean:write name="MeisaisyosaiBean" property="kanjo_syoribi"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 契約伝票No. --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KEIYAKUDENPYONO)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="keiyaku_denpyo_no"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 金額計 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_KINGAKUKEI)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:30%; text-align: right;">
								<bean:write name="MeisaisyosaiBean" property="kingaku_kei"/>
								<bean:write name="MeisaisyosaiBean" property="tuuka_cd"/>
							</DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 滞留区分 --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_TAIRYUKUBUN)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox"  style="width:20%;"><bean:write name="MeisaisyosaiBean" property="tairyu_kbn"/></DIV>
						</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 滞留判定 --%>
							<DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OB1103_TAIRYUHANTEI)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"colspan="7">
							<DIV style="margin-bottom:2px;text-align:left;">
								<html:select name="SyosaiForm" property="syosai_bean.tairyu_hantei">
									<html:option value=""></html:option>
									<html:optionsCollection name="SyosaiForm" property="ar_tairyu_jdg" value="value" label="key" />
								</html:select>
							</DIV>
						</TD>
					</TR>
					<TR style="border:0px; width:100%;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<logic:equal name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
								<DIV class="dottitle" style="margin-top:2px;">
									<%-- 判定事由(国内) --%>
									<%=i18n.get(GL.OB1103_HANTEIJIYU_KOKUNAI)%>
								</DIV>
							</logic:equal>

							<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="<%= GS.GSS %>">
								<DIV class="dottitle" style="margin-top:2px;">
									<%-- 判定事由(海外)--%>
									<%=i18n.get(GL.OB1103_HANTEIJIYU_KAIGAI)%>
								</DIV>
							</logic:notEqual>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"colspan="7">
							<html:textarea name="SyosaiForm" property="syosai_bean.hantei_jiyu" rows="5" style="width:100%;"></html:textarea>
						</TD>
					</TR>


					<logic:notEmpty name="MeisaisyosaiBean" property="invoice_no">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- インボイスNo. --%>
							<DIV class="dottitle" style="margin-top:1px;"><%=i18n.get(GL.OB1103_INVOICENO)%></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="invoice_no"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
					<logic:notEmpty name="MeisaisyosaiBean" property="komoku1">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 項目１ --%>
							<DIV class="dottitle" style="margin-top:1px;"><bean:write name="SyosaiForm" property="komoku1"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="3">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku1"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
					<logic:notEmpty name="MeisaisyosaiBean" property="komoku2">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 項目２ --%>
							<DIV class="dottitle" style="margin-top:1px;"><bean:write name="SyosaiForm" property="komoku2"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="5">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku2"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
					<logic:notEmpty name="MeisaisyosaiBean" property="komoku3">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 項目３ --%>
							<DIV class="dottitle" style="margin-top:1px;"><bean:write name="SyosaiForm" property="komoku3"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku3"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
					<logic:notEmpty name="MeisaisyosaiBean" property="komoku4">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 項目４ --%>
							<DIV class="dottitle" style="margin-top:1px;"><bean:write name="SyosaiForm" property="komoku4"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku4"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
					<logic:notEmpty name="MeisaisyosaiBean" property="komoku5">
					<TR style="border:0px;">
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
							<%-- 項目５ --%>
							<DIV class="dottitle" style="margin-top:1px;"><bean:write name="SyosaiForm" property="komoku4"/></DIV>
						</TD>
						<TD style="border:0px;margin: 0 0 0 0;padding: 0px;" colspan="7">
							<DIV class="ReadOnlybox" style="width:20%;"><bean:write name="MeisaisyosaiBean" property="komoku5"/></DIV>
						</TD>
					</TR>
					</logic:notEmpty>
				</TABLE> 		
			</DIV>
		
			<DIV class="mainlist">
	
				<DIV class="CNDbox">
				</DIV>
					
				<DIV class="XYbox">
					<%-- 添付選択 --%>
					<input type="button" value="<%=i18n.get(GL.BTN_TEMPUSENTAKU)%>" onclick="doSubmit('add')" style="background:#CCCCCC" class="button" />
				</DIV>
				<logic:notEmpty name="SyosaiForm" property="ar_tenpu">
				<TABLE style="border-left-color: #AAA;" border=0 cellSpacing=0 cellPadding=0 class="bunsyo">
					<nested:iterate property="ar_tenpu" indexId="idx">
					<TR>
						<TD class="left">
							<bean:define id="id" name="ar_tenpu" property="id" type="java.lang.String"></bean:define>
							&nbsp;<%= Integer.parseInt(id)+1 %>.&nbsp;
							<%-- 課題No.52 添付ファイルダウンロード対応 --%>
							<%-- 修正開始 --%>
							<%-- <a href="#" onClick= "download('download', '<nested:write property="id" />')"><nested:write property="file_nm"/></a> --%>
							<a href="#" onClick= "downloadOpen('download','<nested:write property="id" />','<nested:write property="file_nm"/>')"><nested:write property="file_nm"/></a>
							<%-- 修正完了 --%>
						</TD>
						<TD class="right">
							<%-- 添付解除 --%>
							<%=i18n.get(GL.OB1103_TENPUKAIJYO)%>&nbsp;
							<bean:define id="checkedId" name="ar_tenpu" property="id" type="java.lang.String" />
							<html:multibox property="ar_kaijyo_chk" value="<%=checkedId%>"></html:multibox>
						</TD>
					</TR>
					</nested:iterate>
				</TABLE>
				</logic:notEmpty>
			</DIV>
		</DIV>
		</html:form>
	</DIV>
</DIV>
</CENTER>
<script type="text/javascript" language="JavaScript">
  <!--
  var focusControl = document.forms["01SyosaiForm"].elements["syosai_bean.tairyu_hantei"];

  if (focusControl.type != "hidden" && !focusControl.disabled) {
     focusControl.focus();
  }
  // -->
</script>
</BODY>
</HTML>
