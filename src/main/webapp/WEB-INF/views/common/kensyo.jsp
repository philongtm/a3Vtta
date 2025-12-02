<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file="../include/jspHeader.jsp" %>
<%@ include file="../include/jspUtil.jsp" %>

<bean:define id="HikiatekinKensyoSyokaiForm" name="HikiatekinKensyoSyokaiForm" type="app.common.form.KensyoSyokaiForm" />

<%-- No339, 2008/05/16, SJA渡辺, 二段スクロール表示させないように修正 --%>
<script type='text/javascript'>

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
<BODY onload="resizeParentIFrame();">
<%--コンテンツ部分--%>
<DIV id="tagcontents">
<html:form action="/common/kensyo">
<%-- 障害No：329 --%>
<%-- チェックイン日：2008/05/15 --%>
<%-- 対応者：SJA渡辺 --%>
<%-- 修正概要：表示欄のずれ、その他体制がずれている個所を修正 --%>
<DIV id="list">
<DIV class="mainlist">
	<TABLE style="border:0px;width:100%;" class="semaku">
		<TR style="border:0px;" class="semaku">
			<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				<%=i18n.get(GL.LABEL_HYOJI_DATA)%>
			</TD>
			<TD style="text-align: left;width=10%;border:0px;" class="semaku">
				<html:select name="HikiatekinKensyoSyokaiForm" property="hyoji" onchange="doSubmit('hyoji')">
					<html:optionsCollection name="HikiatekinKensyoSyokaiForm" property="syoriKaisuList" value="value" label="key" />
				</html:select>
			</TD>
		</TR>
		<TR style="border:0px;" class="semaku">
			<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_SATEI_YM)%></DIV>
			</TD>
			<TD style="border:0px;width:14%;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;" class="semaku">
				<bean:write name="HikiatekinKensyoSyokaiForm" property="satei_ym"/></DIV>
			</TD>
			<TD style="border:0px;width:9%;text-align:left;" class="semaku">
			</TD>
			<TD style="border:0px;width:14%;text-align:left;" class="semaku">
			</TD>
			<TD style="border:0px;width:5%;text-align:left;" class="semaku">
			</TD>
			<TD style="border:0px;width:10%;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_BASE_YEAR)%></DIV>
			</TD>
			<TD style="border:0px;width:14%;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
				<bean:write name="HikiatekinKensyoSyokaiForm" property="kijun_ym"/></DIV>
			</TD>
			<TD style="border:0px;width:10%;text-align:left;" class="semaku">
			</TD>
			<TD style="border:0px;width:14%;text-align:left;" class="semaku">
			</TD>
		</TR>
		<TR style="border:0px;" class="semaku">
			<TD style="border:0px;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_CUST_CATEGORY)%></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
				<bean:write name="HikiatekinKensyoSyokaiForm" property="tori_kbn"/></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_CRED_CATEGORY)%></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
				<bean:write name="HikiatekinKensyoSyokaiForm" property="saiken_kbn" /></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku">
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_CUST_CATEGORY)%></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
				<%-- 課題No.60 引当金検証時の区分設定 --%>
				<%-- 修正開始 --%>
				<%-- <bean:write name="HikiatekinKensyoSyokaiForm" property="tori_kbn" /></DIV> --%>
				<bean:write name="HikiatekinKensyoSyokaiForm" property="final_tori_kbn" /></DIV>
				<%-- 修正完了 --%>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku">
				<DIV class="dottitle"><%=i18n.get(GL.LABEL_CRED_CATEGORY)%></DIV>
			</TD>
			<TD style="border:0px;text-align:left;" class="semaku"><DIV class="ReadOnlybox" style="width:100%;word-break:break-all;">
				<%-- 課題No.60 引当金検証時の区分設定 --%>
				<%-- 修正開始 --%>
				<%-- <bean:write name="HikiatekinKensyoSyokaiForm" property="saiken_kbn" /></DIV> --%>
				<bean:write name="HikiatekinKensyoSyokaiForm" property="final_saiken_kbn" /></DIV>
				<%-- 修正完了 --%>
			</TD>
		</TR>
	</TABLE>
	
	<TABLE style="border:0px;width:100%;border-collapse: collapse;" class="semaku">
		<TR style="border:0px;">
		<TD style="border:0px;width:44%;vertical-align:top;text-align:right;">
			<%=i18n.get(GL.LABEL_TUUKA)%>&nbsp;<%=i18n.get(GL.LABEL_COLON)%>&nbsp;<bean:write name="HikiatekinKensyoSyokaiForm" property="tuuka_cd_1" />

			<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD colspan="1" rowspan="14" style="width: 7%;background-color:#330088;border-bottom-color: #330088;"><br>
					</TD>
					<TD colspan="1" rowspan="12" style="width: 7%;background-color:#666699;border-bottom-color: #666699;"><br>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_UKETORI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="uketoritegata_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_YUSYUTSU_UKETORI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="yusyutu_uketoritegata_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="vertical-align: top; text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_UTIKAKE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="urikakekin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_TORI_TOKIN)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="torihikimaetokin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_TATEKAE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="tatekaekin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_MISYUNYU)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="misyunyukin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_MISYUSYU)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="misyusyueki_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_TANKI_KASHI)%><BR>
					</TD>
					<%-- No550, 2008/06/05, SJA渡辺, 正しい値を設定するように修正 --%>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="tanki_kashitsukekin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_SASHIIRE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="sashiire_hosyokin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_KARIBARAI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="karibaraikin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_TYOKI_KASHI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="tyoki_kashitsukekin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_SONOTA_TOSHI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="sonota_toshi_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="2" style="border:0px;background-color:#666699;color:#FFFFFF;border-bottom:solid 1px #AAA">
						<%=i18n.get(GL.LABEL_IPANSAIKEN_KEI)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="ipan_saimukei_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="2" style="border:0px;background-color:#666699;color:#FFFFFF;border-bottom:solid 1px #AAA;border-right:solid 1px #AAA;">
						<%=i18n.get(GL.LABEL_KOTEI_EIGYO)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="koteika_eigyosaiken_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="3" style="background-color:#330088;color:#FFFFFF;" border-bottom:solid 1px #AAA">
						<%=i18n.get(GL.LABEL_SAIKEN_ZAN)%><%=i18n.get(GL.LABEL_ONE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="saiken_zankei_1" />&nbsp;
					</TD>
				</TR>
			</TABLE>
			<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="2" style="width: 7%;background-color:#330088;border-bottom-color: #330088;"><br>
					</TD>
					<TD style="width: 47%; text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_RESERVATION)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="ryuhosaimu_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="width: 47%; text-align: right;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_OP_RYUHO)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="oth_ryuhosaimu_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_RYUHO_SAIMU_KEI)%><%=i18n.get(GL.LABEL_TWO)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="ryuhosaimu_kei_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_HOZEN)%><%=i18n.get(GL.LABEL_THREE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="hozen_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_SONOTA_KAISYU)%><%=i18n.get(GL.LABEL_FOUR)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="sonotakaisyu_1" />&nbsp;
					</TD>
				</TR>
			</TABLE>
			<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;border-bottom:none;">
						<%=i18n.get(GL.LABEL_HOSYO_SAIMU)%>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="hosyosaimu_gokei_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="width: 7%;" style="border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
					</TD>
					<TD style="width: 47%; text-align: right; border:solid 1px #AAA;background-color:#EAEAEA;">
						<%=i18n.get(GL.LABEL_RIKO_SEIKYU)%><%=i18n.get(GL.LABEL_FIVE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="riko_kenen_1" />&nbsp;
					</TD>
				</TR>
			</TABLE>
			<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_KIBIKIATE)%><%=i18n.get(GL.LABEL_SIX)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="kibikiatekin_1" />&nbsp;
					</TD>
				</TR>
				<TR>
					<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_HIKIATE_TAISYO)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="hikiate_taisyokingaku_1" />&nbsp;
					</TD>
				</TR>
			</TABLE>
			<TABLE style="width:100%;border-collapse: collapse;">
				<TR>
					<TD style="width: 54%;background-color:#330088;color:#FFFFFF;">
						<%=i18n.get(GL.LABEL_TUIKA_HIKIATE)%><BR>
					</TD>
					<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="tuika_hikiate_1" />&nbsp;
					</TD>
				</TR>
			</TABLE>
			<BR>
			<TABLE style="width:100%;border:0px;table-layout:fixed">
				<%=i18n.get(GL.LABEL_HIKIATE_KONKYO)%><BR>
				<TR style="border:0px;width:100%;">
					<TD style="border:0px;"><DIV class="ReadOnlybox" style="width:100%;">
				<%-- 障害表：482,486 チェックイン日：2008/5/29 対応者：SJA中島 概要：BRタグを認識するように修正 --%>
				<%-- 障害表：482,486 チェックイン日：2008/5/31 対応者：SJA中島 概要：<pre>に変更 --%>
				<%-- No486, 2008/06/03, SJA内田
						コメント欄が適切に折り返され横に伸びないように修正。
						コメント欄の最下行に不要な改行が入らないように修正
						<pre>タグ直後の改行は無視される為、予め改行をして置く(先頭行を改行した場合の対策)
				--%>
				
				<%-- No.827 2008/06/13 新実 フォントを改めて指定することで、フォントの変化を回避。 --%>
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinKensyoSyokaiForm" property="hikiate_cmt" /></font></pre>&nbsp;</DIV>
						
					</TD>
				</TR>
			</TABLE>
		</TD>
		<TD style="border:0px;width:9%;text-align:center;">
			<img src="<c:url value='/image/yajirushi.gif' />">
		</TD>
		<%-- 2008/06/12 新実 ラベルを用いてコロンを表示するように修正 --%>
		<TD style="border:0px;width:44%;vertical-align:top;text-align:right;">
			<%=i18n.get(GL.LABEL_TUUKA)%>&nbsp;<%=i18n.get(GL.LABEL_COLON)%>&nbsp;<bean:write name="HikiatekinKensyoSyokaiForm" property="tuuka_cd_2" />

			<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD colspan="1" rowspan="14" style="width: 7%;background-color:#330088;border-bottom-color: #330088;"><br>
				</TD>
				<TD colspan="1" rowspan="12" style="width: 7%;background-color:#666699;border-bottom-color: #666699;"><br>
				</TD>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_UKETORI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="uketoritegata_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_YUSYUTSU_UKETORI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="yusyutu_uketoritegata_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_UTIKAKE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="urikakekin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_TORI_TOKIN)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="torihikimaetokin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_TATEKAE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="tatekaekin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_MISYUNYU)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="misyunyukin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_MISYUSYU)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="misyusyueki_2" />&nbsp;
					</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_TANKI_KASHI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="tanki_kashitsukekin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_SASHIIRE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="sashiire_hosyokin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_KARIBARAI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="karibaraikin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_TYOKI_KASHI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="tyoki_kashitsukekin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_SONOTA_TOSHI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="sonota_toshi_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="2" style="border:0px;background-color:#666699;color:#FFFFFF;border-bottom:solid 1px #AAA">
					<%=i18n.get(GL.LABEL_IPANSAIKEN_KEI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;border-bottom:solid 1px #AAA">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="ipan_saimukei_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="2" style="border:0px;background-color:#666699;color:#FFFFFF;border-bottom:solid 1px #AAA">
					<%=i18n.get(GL.LABEL_KOTEI_EIGYO)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="koteika_eigyosaiken_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="3" style="border:0px;background-color:#330088;color:#FFFFFF;border-bottom:solid 1px #AAA;border-right:solid 1px #AAA;">
					<%=i18n.get(GL.LABEL_SAIKEN_ZAN)%><%=i18n.get(GL.LABEL_ONE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
						<bean:write name="HikiatekinKensyoSyokaiForm" property="saiken_zankei_2" />&nbsp;
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD rowspan="2" style="width: 7%;background-color:#330088;border-bottom-color: #330088;"><br>
				</TD>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_RESERVATION)%><BR>
				</TD>
			<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
				<bean:write name="HikiatekinKensyoSyokaiForm" property="ryuhosaimu_2" />&nbsp;
			</TD>
			</TR>
			<TR>
				<TD style="text-align: right;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_OP_RYUHO)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="oth_ryuhosaimu_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_RYUHO_SAIMU_KEI)%><%=i18n.get(GL.LABEL_TWO)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="ryuhosaimu_kei_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_HOZEN)%><%=i18n.get(GL.LABEL_THREE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hozen_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_SONOTA_KAISYU)%><%=i18n.get(GL.LABEL_FOUR)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="sonotakaisyu_2" />&nbsp;
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD rowspan="1" colspan="2" style="background-color:#330088;color:#FFFFFF;border-bottom:none;">
					<%=i18n.get(GL.LABEL_HOSYO_SAIMU)%><BR>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hosyosaimu_gokei_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="width: 7%;" style="border-top:none; border-right:none; background-color:#330088;color:#FFFFFF;"><br>
				</TD>
				<TD style=" text-align: right; border:solid 1px #AAA;background-color:#EAEAEA;">
					<%=i18n.get(GL.LABEL_RIKO_SEIKYU)%><%=i18n.get(GL.LABEL_FIVE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="riko_kenen_2" />&nbsp;
				</TD>
			</TR>
		</TABLE>
		<TABLE style="width:100%;border-collapse: collapse;">
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_KIBIKIATE)%><%=i18n.get(GL.LABEL_SIX)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="kibikiatekin_2" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_HIKIATE_KENSYO_ZAN)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hikiate_kojo" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_HIKIATE_HOSEI)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hikiatekin_hosei" />&nbsp;
				</TD>
			</TR>
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_HOSEIGO_HIKIATE)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hoseigo_hikiate" />&nbsp;
				</TD>
			</TR>
			<%-- 案件No.D9059 項目削除 --%>
			<%-- 
			<TR>
				<TD style="background-color:#330088;color:#FFFFFF;">
					<%=i18n.get(GL.LABEL_HOSEI_HIKIATE_KOJO)%><BR>
				</TD>
				<TD style="width: 40%; text-align: right;background-color:#CCECFF;">
					<bean:write name="HikiatekinKensyoSyokaiForm" property="hoseigo_hikiatekojo_zan" />&nbsp;
				</TD>
			</TR>
			--%>
		</TABLE>
		<BR>
		<TABLE style="width:100%;border:0px;table-layout:fixed">
			<%=i18n.get(GL.LABEL_RESERVE_K)%><%=i18n.get(GL.LABEL_HIKIATE_KONKYO)%><BR>
			<TR style="border:0px;">
				<TD style="border:0px;"><DIV class="ReadOnlybox" style="width:100%;">
					<%-- 障害表：482,486 チェックイン日：2008/5/29 対応者：SJA中島 概要：BRタグを認識するように修正 --%>
					<%-- 障害表：482,486 チェックイン日：2008/5/31 対応者：SJA中島 概要：<pre>に変更 --%>
					<%-- No486, 2008/06/03, SJA内田
						コメント欄が適切に折り返され横に伸びないように修正。
						コメント欄の最下行に不要な改行が入らないように修正
						<pre>タグ直後の改行は無視される為、予め改行をして置く(先頭行を改行した場合の対策)
				--%>
				
				<%-- No.827 2008/06/13 新実 フォントを改めて指定することで、フォントの変化を回避。 --%>
<pre style="word-wrap: break-word; display: inline;">
<font face="ＭＳ Ｐゴシック,Arial"><bean:write name="HikiatekinKensyoSyokaiForm" property="hikiatekin_kensyo_cmt" /></font></pre>&nbsp;</DIV>
					
				</TD>
			</TR>
		</TABLE>
		</TD>
	</TR>
	</TABLE>
</DIV>
</DIV>
</html:form>
</DIV>
</BODY>
</HTML>