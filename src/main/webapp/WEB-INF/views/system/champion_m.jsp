<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "/include/jspHeader.jsp" %>
<%@ include file = "/include/jspUtil.jsp" %>

<bean:define id="ChampionForm" name="04ChampionForm" type="app.system.form.ChampionForm" />
<%
	Pager pager = ChampionForm.getPager();
	List list = ChampionForm.getList();
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/17
//対応者 　福士
//修正概要
//①ラジオボタン選択値を取得可能な状態に修正
//④遷移時にURLパラメーターに格納される引数をbean格納に変更
///////////////////////////////////////
	int listsize = 0;
	if(list != null) {
		listsize = list.size();
	}
%>

<script>
function championToroku() {

		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		
		var id = null;
	
		form = document.forms[0];

		for ( i = 0; i < <%=listsize%>; i++ ) {
			if(form.elements["champion" + i].length == null) {
				if ( form.elements["champion" + i].checked ) {
						if( id == null ) {
							id = i + "-0";
						} else {
							id += "/" + i + "-0";
						}
				}
			} else {
				for ( j = 0; j < form.elements["champion" + i].length; j++ ) {
					if ( form.elements["champion" + i][j].checked ) {
						if( id == null ) {
							id = i + "-" + j;
						} else {
							id += "/" + i + "-" + j;
						}
					}
				}
			}
		}

		form.elements["chk"].value = id;

		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + 'toroku';
		form.submit();
		form.action = action;
}
<%-- ここまで（障害票No359　①、④）--%>
</script>
</HEAD>
<BODY onload="">
<CENTER>
<%--ヘッダ部分--%>
<DIV id="main">
<DIV id="head">
	<IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
	<IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />" height="54">
 	<%-- ヘルプリンク --%>
	<a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%></a>
</DIV>

<%--メニュー部分--%>
<DIV id="menu">
<%@ include file = "/menu.jspf" %>
</DIV>

<%--コンテンツ部分--%>
<DIV id="contents">
<H1 class="title01"><%=i18n.get(GL.TITLE_04_03)%></H1>

<DIV id="submenu">
<% 
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/1
//対応者 　福士
//修正概要
//⑤明細が一件もない場合、登録ボタン非表示
///////////////////////////////////////
%>
	<% if(list != null) { %>
	<input type="button" value="<%=i18n.get(GL.BTN_REGISTER)%>" onclick="championToroku()">
	<%}%>
<%-- ここまで（障害票No359　⑤）--%>
	<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
</DIV>

<DIV id="list">
<html:form action="/system/champion">
<html:hidden property="anken_no" />
<html:hidden property="id" />
<% 
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/15
//対応者 　福士
//修正概要
//①ラジオボタン選択値を取得可能な状態に修正
///////////////////////////////////////
%>
<html:hidden property="chk" />
<%-- ここまで（障害票No359　①）--%>
	<DIV class="mainlist">
		<%-- 管理票No200807071013, 2008/07/09, SJA渡辺, 線を黒に修正 --%>
		<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#000000;">
		<THEAD>
		<TR>
			<TH width="8%" rowspan=2><%=i18n.get(GL.LABEL_CUST_CD)%></TH>
			<TH width="35%"><%=i18n.get(GL.LABEL_CUST_NM)%></TH>
			<TH width="20%" colspan=2><%=i18n.get(GL.LABEL_COUNTRY)%></TH>
			<TH width="10%"><%=i18n.get(GL.LABEL_MONTH)%></TH>
			<TH width="20%"><%=i18n.get(GL.LABEL_TAD)%></TH>
			<TH class="borderRight"><%=i18n.get(GL.LABEL_CO)%></TH>
		</TR>
		<TR>
			<TH colspan=2><%=i18n.get(GL.LABEL_DEPT)%></TH>
			<TH colspan=2><%=i18n.get(GL.LABEL_OJ_TTL)%></TH>
			<TH><%=i18n.get(GL.LABEL_SAIKEN_ZANKEI)%></TH>
			<TH class="borderRight"><%=i18n.get(GL.LABEL_SELECT)%></TH>
		</TR>
		</THEAD>
		<TBODY>
		<% if(list != null) { %>
		<%
		int index = 0;
		%>
		<logic:iterate id="meisai" collection="<%=list%>" type="java.util.HashMap" indexId="idx">
			<TR>
				<TD rowspan='<bean:write name="meisai" property="count" />' style="border-bottom-color:#000000;">
					<a href="#" onClick="syosai('syosai','0',<%=idx%>)"><bean:write name="meisai" property="kanjo_cd" /></a>
				</TD>
				<TD><bean:write name="meisai" property="kanjo_nm" />&nbsp;</TD>
				<TD colspan=2><bean:write name="meisai" property="syozaikoku" />&nbsp;</TD>
				<TD class="center"><bean:write name="meisai" property="ymSlash" />&nbsp;</TD>
				<TD class="right">
					<bean:write name="meisai" property="saiken_total" />
					&nbsp;(<bean:write name="meisai" property="tuuka_nm" />)
				</TD>
				<TD style="border-right-color:#000000;"><bean:write name="meisai" property="kaisya" />&nbsp;</TD>
			</TR>
<% 
///////////////////////////////////////
//障害票No359
//チェックイン日 2008/05/15
//対応者 　福士
//修正概要
//①ラジオボタン選択値を取得可能な状態に修正
///////////////////////////////////////
%>		
			<% List bu_meisai = (List)meisai.get("bu_meisai"); %>
			<nested:iterate id="meisai_bu" collection="<%=bu_meisai%>" type="java.util.HashMap" indexId="idx_so">
			<%-- 管理票No200807071013, 2008/07/09, SJA渡辺, 線を黒に修正 --%>
			<%
			index = idx_so.intValue() + 2;
			%>
			<% if ((""+index).equals(meisai.get("count"))) { %>
				<TR>
					<TD colspan=2 style="border-bottom-color:#000000;">
						<nested:write name="meisai_bu" property="soshiki_nm" />
						&nbsp;(<nested:write name="meisai_bu" property="soshiki_cd_view" />)
					</TD>
					<TD colspan=2 class="right" style="border-bottom-color:#000000;">
						<nested:write name="meisai_bu" property="tairyu_total" />
						&nbsp;(<bean:write name="meisai" property="tuuka_nm" />)
					</TD>
					<TD class="right" style="border-bottom-color:#000000;">
						<nested:write name="meisai_bu" property="saiken_total_so" />
						&nbsp;(<bean:write name="meisai" property="tuuka_nm" />)
					</TD>
					<TD class="center" style="border-bottom-color:#000000;border-right-color:#000000;">
						<input type="radio" name="<%= "champion" + idx %>"  value="<%=idx_so%>" />
					</TD>
				</TR>
			<% } else { %>
				<TR>
					<TD colspan=2>
						<nested:write name="meisai_bu" property="soshiki_nm" />
						&nbsp;(<nested:write name="meisai_bu" property="soshiki_cd_view" />)
					</TD>
					<TD colspan=2 class="right">
						<nested:write name="meisai_bu" property="tairyu_total" />
						&nbsp;(<bean:write name="meisai" property="tuuka_nm" />)
					</TD>
					<TD class="right">
						<nested:write name="meisai_bu" property="saiken_total_so" />
						&nbsp;(<bean:write name="meisai" property="tuuka_nm" />)
					</TD>
					<TD class="center" style="border-right-color:#000000;">
						<input type="radio" name="<%= "champion" + idx %>"  value="<%=idx_so%>" />
					</TD>
				</TR>
			<% } %>
			</nested:iterate>
<%-- ここまで（障害票No359　①）--%>
		</logic:iterate>
		<% } %>
		</TBODY>	
		</TABLE>
	</DIV>
</html:form>
</DIV>
</DIV>

</DIV>
</CENTER>
</BODY>
</HTML>