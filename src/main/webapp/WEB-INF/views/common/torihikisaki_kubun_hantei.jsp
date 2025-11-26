<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>

<bean:define id="TorihikisakiKubunHanteiSyokaiForm" name="TorihikisakiKubunHanteiSyokaiForm" type="app.common.form.TorihikisakiKubunHanteiSyokaiForm" />
<bean:define id="cmnData" name="app.SessionData" type="app.SessionData" scope="session" />
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

</HEAD>

<BODY onload="resizeParentIFrame()">
<CENTER>

<%--コンテンツ部分--%>
<DIV id="tagcontents">
<html:form action="/common/torihikisaki_kubun_hantei">

<DIV id="list">
	<DIV class="mainlist">
	 
		<TABLE style="border:0px;width:100%;border-collapse:collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_KTK)%>
				</TD>
				<TD style="border:0px;width:8%;"class="semaku"><DIV class="ReadOnlybox center" style="width:100%;">
					<bean:write name="TorihikisakiBean" property="sinyoktk"/>&nbsp;</DIV> 
				</TD>
				<TD style="border:0px;width:21%;"class="semaku right">
					<%=i18n.get(GL.OC1103_KTK)%><%=i18n.get(GL.OC1103_OYA_ITTAI_HANDAN)%>
				</TD>
				<TD style="border:0px;width:8%;"class="semaku"><DIV class="ReadOnlybox center" style="width:100%;">
					<bean:write name="TorihikisakiBean" property="oya_ktk" />&nbsp;</DIV>
				</TD> 		 
				<TD style="border:0px;"class="semaku"><DIV class="ReadOnlybox" style="width:100%;">
					<bean:write name="TorihikisakiBean" property="oya_business_nm"/>&nbsp;</DIV>
				</TD>
				<TD style="border:0px;width:12%;"class="semaku"><DIV class="ReadOnlybox center" style="width:100%;">
					<bean:write name="TorihikisakiBean" property="oya_ittai_dokuritu"/>&nbsp;</DIV>
					</DIV>
			</TR>
		</TABLE>	
		<TABLE style="border:0px;width:100%;border-collapse:collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_TAIRYU_KBN)%>
				</TD>
	 			<TD style="border:0px;width:8%;"class="semaku"><DIV class="ReadOnlybox center" style="width:100%;">
					<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="tairyu_kbn"/></DIV>
	 			</TD>
				<TD style="border:0px;width:21%;"class="semaku"><DIV class="ReadOnlybox center" style="width:100%;">
					<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="tairyu_kbn_nm" /></DIV> 
				</TD>
				<TD style="border:0px;"class="semaku left">&nbsp;</TD>
			</TR>
		</TABLE>
		<TABLE style="border:0px;width:100%;border-collapse: collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_TORIHIKISAKI_KBN)%>
				</TD>
				<TD style="border:0px;width:21%;"class="semaku"><DIV class="ReadOnlybox " style="width:100%;">
					<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="torihikisaki_kbn_nm" /></DIV> 
				</TD>
				<TD style="border:0px;"class="semaku left">&nbsp;</TD>
			</TR>
		</TABLE>
		
		<%-- 正常先・要注意先取引先概要 --%>
		<% if(TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals("1") || TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals("2") || TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals("5") || TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals(GS.EMPTY_CHARCTER)) {%>
			<TABLE style="border:0px;width:100%;border-collapse:collapse;">
				<TR style="border:0px;">
					<TD style="border:0px;width:90%;" class="semaku left">
						<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="torihikisaki_kbn_nm" />&nbsp;<%=i18n.get(GL.OC1103_GAITO_JIYU)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
			
			</TABLE>
			<TABLE style="border:0px;width:100%;">
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="seijo_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="seijo_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:10%;"class="semaku left"><%=i18n.get(GL.OC1103_SEIJOSAKI)%></TD>
					<TD style="border:0px; "class="semaku left">
						<DIV style="width:100%;border:dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_SEIJO_EXP)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="yochui_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="yochui_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:10%;"class="semaku left"><%=i18n.get(GL.OC1103_YOCHUISAKI)%></TD>
					<TD style="border:0px;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_YOCHUI_EXP)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
			</TABLE>
			
		<%-- 貸倒懸念先概要 --%>
		<%} else if(TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals("3")) {%>
		<TABLE style="border:0px;width:100%;border-collapse:collapse;">
				<TR style="border:0px;">
					<TD style="border:0px;width:10%;"class="semaku left">&nbsp;</TD>
					<TD style="border:0px;"class="semaku left"><DIV class="ReadOnlybox" style="width:100%;">
						<%=i18n.get(GL.OC1103_KASHITAORE_EXP)%>
					</TD>
				</TR>
			</TABLE>
			<TABLE style="border:0px;width:100%;border-collapse:collapse;">
				<TR style="border:0px;">
					<TD style="border:0px;width:90%;"class="semaku left">
						<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="torihikisaki_kbn_nm" />&nbsp;<%=i18n.get(GL.OC1103_GAITO_JIYU)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
			</TABLE>
			
			<TABLE style="border:0px;width:100%;">
				<TR style="border:0px;">
					
					<TD style="border:0px;text-align:left;width:5%;"class="semaku">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="tyoka_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="tyoka_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_1)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;text-align:left;width:90%;"class="semaku">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_KASHITAORE_EXP1)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="kanwa_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="kanwa_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_2)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;width:90%;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_KASHITAORE_EXP2)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="entai_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="entai_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_3)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;width:90%;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_KASHITAORE_EXP3)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
			</TABLE>
			
		<%-- 破産更生先概要 --%>
		<%} else if(TorihikisakiKubunHanteiSyokaiForm.getTorihikisaki_kbn().equals("4")) {%>
			<TABLE style="border:0px;width:100%;border-collapse:collapse;">
				<TR style="border:0px;">
					<TD style="border:0px;width:10%;"class="semaku left">&nbsp;</TD>
					<TD style="border:0px;"class="semaku left"><DIV class="ReadOnlybox" style="width:100%;">
						<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP)%>
					</TD>
				</TR>
			</TABLE>
			<TABLE style="border:0px;width:100%;border-collapse: collapse;">
				<TR style="border:0px;">
					<TD style="border:0px;width:90%;"class="semaku left">
						<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="torihikisaki_kbn_nm" />&nbsp;<%=i18n.get(GL.OC1103_GAITO_JIYU)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
			</TABLE>
			<TABLE style="border:0px;width:100%;">
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="hasanho_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="hasanho_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_1)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;width:90%;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP1)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="kaishaho_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="kaishaho_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_2)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;width:90%;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP2)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>
				<TR style="border:0px;">
					
					<TD style="border:0px;width:5%;"class="semaku left">
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="koseho_chk" value="1">
							<input style="width:100%;" type=checkbox checked disabled>
						</logic:equal>
						<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="koseho_chk" value="0">
							<input style="width:100%;" type=checkbox disabled>
						</logic:equal>
					</TD>
					<TD style="border:0px;width:3%;"class="semaku left">
						<%=i18n.get(GL.COMMON_3)%><%=i18n.get(GL.COMMON_DOT)%></TD>
					<TD style="border:0px;width:90%;"class="semaku left">
						<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
							<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP3)%>
						</DIV>
					</TD>
					<TD style="border:0px;"class="semaku left">&nbsp;</TD>
				</TR>

				<logic:equal name="TorihikisakiBean" property="system_kbn" value="01">
					<TR style="border:0px;">
						<TD style="border:0px;width:5%;"class="semaku left">
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="saiseho_chk" value="1">
								<input style="width:100%;" type=checkbox checked disabled>
							</logic:equal>
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="saiseho_chk" value="0">
								<input style="width:100%;" type=checkbox disabled>
							</logic:equal>
						</TD>
						<TD style="border:0px;width:3%;" class="semaku left">
							<%=i18n.get(GL.COMMON_4)%><%=i18n.get(GL.COMMON_DOT)%></TD>
						<TD style="border:0px;width:90%;" class="semaku left">
							<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
								<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP4)%>
							</DIV>
						</TD>
						<TD style="border:0px;"class="semaku left">&nbsp;</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;width:5%;"class="semaku left">
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="shobun_chk" value="1">
								<input style="width:100%;" type=checkbox checked disabled>
							</logic:equal>
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="shobun_chk" value="0">
								<input style="width:100%;" type=checkbox disabled>
							</logic:equal>
						</TD>
						<TD style="border:0px;width:3%;" class="semaku left">
							<%=i18n.get(GL.COMMON_5)%><%=i18n.get(GL.COMMON_DOT)%></TD>
						<TD style="border:0px;width:90%;" class="semaku left">
							<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
								<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP5)%>
							</DIV>
						</TD>
						<TD style="border:0px;"class="semaku left">&nbsp;</TD>
					</TR>
					<TR style="border:0px;">
						<TD style="border:0px;width:5%;"class="semaku left">
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="sonota_chk" value="1">
								<input style="width:100%;" type=checkbox checked disabled>
							</logic:equal>
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="sonota_chk" value="0">
								<input style="width:100%;" type=checkbox disabled>
							</logic:equal>
						</TD>
						<TD style="border:0px;width:3%;" class="semaku left">
							<%=i18n.get(GL.COMMON_6)%><%=i18n.get(GL.COMMON_DOT)%></TD>
						<TD style="border:0px;width:90%;" class="semaku left">
							<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
								<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP6)%>
							</DIV>
						</TD>
						<TD style="border:0px;"class="semaku left">&nbsp;</TD>
					</TR>
				</logic:equal>
				<logic:notEqual name="TorihikisakiBean" property="system_kbn" value="01">
					<TR style="border:0px;">
						<TD style="border:0px;width:5%;"class="semaku left">
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="sonota_chk" value="1">
								<input style="width:100%;" type=checkbox checked disabled>
							</logic:equal>
							<logic:equal name="TorihikisakiKubunHanteiSyokaiForm" property="sonota_chk" value="0">
								<input style="width:100%;" type=checkbox disabled>
							</logic:equal>
						</TD>
						<TD style="border:0px;width:3%;" class="semaku left">
							<%=i18n.get(GL.COMMON_4)%><%=i18n.get(GL.COMMON_DOT)%></TD>
						<TD style="border:0px;width:90%;" class="semaku left">
							<DIV style="width:100%;border: dotted 1px #666666;padding: 5px;">
								<%=i18n.get(GL.OC1103_HASAN_KOSEI_EXP6)%>
							</DIV>
						</TD>
						<TD style="border:0px;"class="semaku left">&nbsp;</TD>
					</TR>
				</logic:notEqual>
			</TABLE>
		<%}else{%>
		&nbsp;
		<%}%>
		
		<%-- 共通部分 --%>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_HANTEI_KONKYO)%>
				</TD>
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;"><pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><logic:empty name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_20">&nbsp;</logic:empty><bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_20" /></font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>
	 	
		<TABLE style="border:0px;width:100%;border-collapse:collapse;">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_SAIKEN_KBN)%>
				</TD>
				<TD style="border:0px;"class="semaku left"><DIV class="ReadOnlybox left" style="width:20%;">
					<bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="saiken_kbn" /></DIV>
				</TD>
				<TD style="border:0px;"class="semaku left">&nbsp;</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_HANTEI_JIYU)%>
				</TD>
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;"><pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><logic:empty name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_30">&nbsp;</logic:empty><bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_30" /></font></pre>
				</DIV>	
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<TR style="border:0px;">
				<TD style="border:0px;width:10%;"class="semaku left">
					<%=i18n.get(GL.OC1103_HASSEI_KEII)%>
				</TD>
				<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;vertical-align:top;" class="left">
				<DIV class="ReadOnlybox" style="width:100%;"><pre style="word-wrap: break-word; display: inline;"><font face="ＭＳ Ｐゴシック,Arial"><logic:empty name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_40">&nbsp;</logic:empty><bean:write name="TorihikisakiKubunHanteiSyokaiForm" property="comment_val_40" /></font></pre>
				</DIV>	
				</TD>
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
