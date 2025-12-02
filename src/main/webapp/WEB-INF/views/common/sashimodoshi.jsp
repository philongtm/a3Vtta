<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="../include/jspException.jsp" %>

<HTML>
<HEAD>
<%@ include file = "../../../include/jspHeader.jsp" %>
<%@ include file = "../../../include/jspUtil.jsp" %>
<%@ page import="app.common.form.RirekiBean" %>
<%@ page import="app.common.form.RirekiListBean" %>

<bean:define id="SashimodoshiForm" name="05SashimodoshiForm" type="app.common.form.SashimodoshiForm" />
<bean:define id="TorihikisakiBean" name="app.SessionData" property="tori_bean" type="app.TorihikisakiBean" />
<% 
	String focus = "sashi_comment";
	if (("5").equals(SashimodoshiForm.getGamen_mode())) {
		if (request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
			focus = "sashiphase";
		} else {
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	} else if (("4").equals(SashimodoshiForm.getGamen_mode()) || ("6").equals(SashimodoshiForm.getGamen_mode())) {
		if (request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
			focus = "sashikbn";
		} else {
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	} else {
		if (request.getAttribute(GS.FOCUS_FIELD) == null || "".equals(request.getAttribute(GS.FOCUS_FIELD))){
		} else {
			focus = (String)request.getAttribute(GS.FOCUS_FIELD);
		}
	}	
	
	String Sateikaisya_cd = SESSION_DATA_APP.getTori_bean().getSateikaisya_cd();
%>

<script>
	function cancelEnter() { 
		if (event.keyCode == 13)
				event.returnValue = false;
	}
	function setFocus(val){
		form = document.forms[0];
		if (val!="") {
    		form.elements[val].focus();
    	}
	}
	function AddTanto() {
		form = document.forms[0];
		var n = form.elements["tanto"].selectedIndex;
		if(n > -1) {
			form.elements["txtTanto"].value = form.elements["tanto"].options[n].text;
			form.elements["selectedTantoId"].value = form.elements["tanto"].options[n].value;
		}
	}
	function doInputSearch(txtSrh) {
		var selTanto;
		var lstTantoV;
		var lstTantoL;

		selTanto = document.forms[0].elements["tanto"];
		lstTantoV = document.getElementsByName("tantoV");
		lstTantoL = document.getElementsByName("tantoL");

		selTanto.options.length = 0; 
		for(var i=0; i<lstTantoL.length; i++) {
			if (txtSrh.value == "" || lstTantoL[i].value.indexOf(txtSrh.value) == 0) {
				var newItem = new Option(lstTantoL[i].value, lstTantoV[i].value);
				selTanto.options.add(newItem);
				
			}
		}
	}
	function check(chkId) {
		form = document.forms[0];
		var chkName = chkId.name;
		var rName = chkName.substr(0, 13) + "radio_id";
		var rbtn = form.elements[rName];
		if ( !chkId.checked ){
			for(i=0;i<rbtn.length;i++){
			rbtn[i].checked = false;
			}
		}
	}
	function radioCheck(rbtn) {
		form = document.forms[0];
		var rName = rbtn.name;
		var cName = rName.substr(0, 13) + "chk_id";
		var cbox = form.elements[cName];
		if( cbox.checked ){
			return true;
		}else{
			return false;
		}
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
			<H1 class="title01"><%=i18n.get(GL.TITLE_OZ2101)%></H1>
			<DIV id="submenu">
				<input type="button" value="<%=i18n.get(GL.BTN_DO_SASHIMODOSHI)%>" onclick="doSubmit('sashimodoshi')">
				<input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('back')">
			</DIV>

			<DIV id="list">
				<form action="/common/sashimodoshi" focus="<%=focus%>">
					<input type="hidden" name="SashimodoshiForm" property="selectedTantoId"/>
					<DIV class="headlist">
						<%--勘定先ＣＤ--%>
						<DIV class="dottitle" style="width:9%"><%=i18n.get(GL.OZ2101_KANJO_CD)%></DIV>
						<DIV class="ReadOnlybox" style="width:10%"><bean:write name="TorihikisakiBean" property="kanjo_cd" /></DIV>
						&nbsp;&nbsp;&nbsp;
						<%--勘定先名称--%>
						<DIV class="dottitle" style="width:10%"><%=i18n.get(GL.OZ2101_KANJO_NM)%></DIV>
						<DIV class="ReadOnlybox" style="width:66%;word-break:break-all;"><bean:write name="TorihikisakiBean" property="kanjo_nm" /></DIV>
						<br><br>
						<%--滞留判定フェーズ差戻--%>
						<% if(SashimodoshiForm.isTairyu_sashi_flg() || SashimodoshiForm.isTo_tairyu_sashi_flg()) {%>
							<input type="radio" onclick="doSubmit('gamen_mode')" property="gamen_mode" value="1" /><span>&nbsp;<%=i18n.get(GL.OZ2101_TAIRYU_SASHI)%>&nbsp;</span>
						<%} %>
						<%--査定フェーズ差戻--%>
						<% if(SashimodoshiForm.isSatei_sashi_flg()) {%>
							<input type="radio" onclick="doSubmit('gamen_mode')" property="gamen_mode" value="2" /><span>&nbsp;<%=i18n.get(GL.OZ2101_SATEI_SASHI)%>&nbsp;</span>
						<%} %>
						<%--引当金確認フェーズ差戻--%>
						<% if(SashimodoshiForm.isHikiate_sashi_flg()) {%>
							<input type="radio" onclick="doSubmit('gamen_mode')" property="gamen_mode" value="3" /><span>&nbsp;<%=i18n.get(GL.OZ2101_HIKIATE_SASHI)%>&nbsp;</span>
						<%} %>
						<%--事務局差戻--%>
						<% if(SashimodoshiForm.isJimu_sashi_flg()) {%>
							<input type="radio" onclick="doSubmit('gamen_mode')" property="gamen_mode" value="4" /><span>&nbsp;<%=i18n.get(GL.OZ2101_JIMUKYOKU_SASHI)%>&nbsp;</span>
						<%} %>
						<%--事務局差戻(仮基準日追加)--%>
						<% if(SashimodoshiForm.isJimuSashiKariFlg()) {%>
							<input type="radio" property="gamen_mode" value="6" /><span>&nbsp;<%=i18n.get(GL.OZ2101_JIMUKYOKU_SASHI)%>&nbsp;</span>
						<%} %>
						<%--新規差戻先選択--%>
						<% if(SashimodoshiForm.isSinki_sashi_flg()) {%>
							<input type="radio" onclick="doSubmit('gamen_mode')" property="gamen_mode" value="5" /><span>&nbsp;<%=i18n.get(GL.OZ2101_SINKI_SASHI)%>&nbsp;</span>
						<%} %>
							<logic:equal name="SashimodoshiForm" property="gamen_mode" value="4">
						<br><br>
							<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
								<TR>
									<%--差戻区分セレクトボックス--%>
									<TD width="10%" style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:6px;"><%=i18n.get(GL.OZ2101_SASHI_KBN)%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
									<DIV class="box" style="margin-left:5px;margin-bottom:2px;">
									<select property="sashikbn">
									<optionsCollection name="SashimodoshiForm" property="ar_Sashikbn" value="value" label="key" />
									</select>
									</DIV></TD>
								</TR>
							</TABLE>
						</logic:equal>
							<logic:equal name="SashimodoshiForm" property="gamen_mode" value="6">
						<br><br>
							<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
								<TR>
									<%--差戻区分セレクトボックス--%>
									<TD width="10%" style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:6px;"><%=i18n.get(GL.OZ2101_SASHI_KBN)%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
									<DIV class="box" style="margin-left:5px;margin-bottom:2px;">
									<select property="sashikbn">
									<optionsCollection name="SashimodoshiForm" property="ar_Sashikbn" value="value" label="key" />
									</select>
									</DIV></TD>
								</TR>
							</TABLE>
						</logic:equal>
							<logic:equal name="SashimodoshiForm" property="gamen_mode" value="5">
						<br><br>
							<TABLE class="none" border=0 cellSpacing=0 cellPadding=0>
								<TR>
									<%--差戻フェーズセレクトボックス--%>
									<TD width="10%" style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:6px;"><%=i18n.get(GL.OZ2101_SASHI_PHASE)%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
										<DIV class="box" style="margin-left:5px;"><select property="sashiphase" onchange="doSubmit('sashi_phase')">
										<optionsCollection name="SashimodoshiForm" property="ar_Sashiphase" value="value" label="key" />
 											</select></DIV></TD>
								</TR>
								<TR>
									<%--汎用２(左)--%>
									<TD width="10%" style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:6px;"><%=SESSION_DATA_APP.getLbl_nm8()%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="ReadOnlybox" style="width:8%;margin-bottom:6px;margin-left:10px;"><bean:write name="TorihikisakiBean" property="sateikaisya_cd" /></DIV>
									<%--汎用３タイトル--%>
									<DIV class="dottitle" style="margin-bottom:5px;">&nbsp;&nbsp;&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm3()%></DIV>
									<%--汎用２(右)セレクトボックス--%>
									<DIV class="box" style="margin-bottom:2px;"><select property="hanyou2" onchange="doSubmit('hanyo2')">
									<optionsCollection name="SashimodoshiForm" property="ar_Hanyou2" value="value" label="key" />
 										</select></DIV>
									<% if(Sateikaisya_cd.equals("SJ")){%>
									<%--汎用４タイトル--%>
									<DIV class="dottitle" style="margin-bottom:5px;">&nbsp;&nbsp;&nbsp;&nbsp;<%=SESSION_DATA_APP.getLbl_nm10()%></DIV>
									<%--汎用３セレクトボックス--%>
									<DIV class="box" style="margin-bottom:2px;"><select property="hanyou3" onchange="doSubmit('hanyo3')">
									<optionsCollection name="SashimodoshiForm" property="ar_Hanyou3" value="value" label="key" />
 										</select></DIV>
 									<%}%>
 										</TD>
								</TR>
								<TR>
									<%-- 入力／検索 --%>
									<TD width="11%"><DIV class="dottitle" style="margin-top:5px;"><%=i18n.get(GL.OZ2101_NYURYOKUKENSAKU)%></DIV></TD>
									<TD style="border:0px;margin: 0 0 0 0;padding: 0px;">
										<DIV class="box" style="margin-bottom:2px;">
											<nested:text property="inTanto" style="width:60%;" onkeyup="doInputSearch(this)" />
										</DIV>
									</TD>
								</TR>
								<TR>
									<%-- 担当者選択 --%>
									<TD width="7%"><DIV class="dottitle"><%=i18n.get(GL.OZ2101_TANTO_SENTAKU)%></DIV></TD>
									<%-- 担当者一覧 --%>
									<TD>
										<DIV style="float:left;margin-left:5px;width:35%;">
											<DIV class="boxtitle"><%=i18n.get(GL.OZ2101_TANTO_ICHIRAN)%></DIV>
											<select name="SashimodoshiForm" property="tanto" size="8" ondblclick="AddTanto()" style="width:100%;">
												<optionsCollection name="SashimodoshiForm" property="ar_Tanto" value="togo_id" label="email_addr"/>
											</select>
											<logic:iterate id="tanto" name="SashimodoshiForm" property="ar_Tanto">
												<input type="hidden" name="tantoV" value="<bean:write name='tanto' property='togo_id'/>"/>
												<input type="hidden" name="tantoL" value="<bean:write name='tanto' property='email_addr'/>"/>
											</logic:iterate>
										</DIV> 
										<button type="submit" property="addTanto" style="float:left;background:#CCCCCC;margin:20px 10px;" onclick="AddTanto()">
											&nbsp;&gt;&nbsp;
										</button>
										<%-- 担当者 --%>
						  				<DIV style="float:left;width:35%;">
						  					<DIV class="boxtitle"><%=i18n.get(GL.OZ2101_TANTO)%></DIV>
						  					<input type="text" name="SashimodoshiForm" readonly="true" property="txtTanto" style="width:100%;background-color: #F8F8FF;border: solid 1px #AAA;" />
				  						</DIV>
						  			</TD>
								</TR>
							</TABLE>
						</logic:equal>
						</DIV>
						<DIV class="mainlist">
						<%--差戻コメント--%>
						<TABLE style="width:100%; border:0px;margin: 0 0 0 0;padding: 0px;table-layout:fixed;">
							<TR style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;">
								<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;"><DIV class="dottitle" style="margin-top:15px;">
									<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OZ2101_COMMENT)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%></DIV>
								</TD>
							</TR>
							<TR style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;">
								<TD style="width:100%;border:0px;margin: 0 0 0 0;padding: 0px;"><input type="text"area property="sashi_comment" style="width:100%" rows="5" /></TD>
							</TR>
						</TABLE>

						<logic:equal name="SashimodoshiForm" property="gamen_mode" value="1">
							<br>
							<%--差戻案件情報(滞留判定フェーズ内差戻)--%>
							<logic:equal name="SashimodoshiForm" property="to_tairyu_sashi_flg" value="false">
								<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#AAA">
									<TR>
										<TH width="5%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
										<TH><%=i18n.get(GL.OZ2101_SOSHIKI)%></TH>
										<TH width="25%"><%=i18n.get(GL.OZ2101_SINTYOKU)%></TH>
									</TR>
									<TR>
										<TD><bean:write name="TorihikisakiBean" property="sateikaisya_cd" />&nbsp;</TD>
										<TD><bean:write name="TorihikisakiBean" property="soshiki" />&nbsp;</TD>
										<TD><bean:write name="TorihikisakiBean" property="sintyoku" />&nbsp;</TD>
									</TR>
								</TABLE>
							</logic:equal>
								<%--差戻先一覧(査定から滞留判定フェーズへ差戻)--%>
							<logic:equal name="SashimodoshiForm" property="to_tairyu_sashi_flg" value="true">
							<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OZ2101_SASHI_ICHIRAN)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
							<TABLE border=0 cellSpacing=0 cellPadding=0>
								<THEAD>
									<TR>
										<TH width="5%" style="text-align:center;"><%=i18n.get(GL.OZ2101_SOSHIKI_SENTAKU)%></TH>
										<TH width="6%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
										<TH><%=i18n.get(GL.OZ2101_SOSHIKI)%></TH>
										<TH width="8%" style="text-align:center;"><%=i18n.get(GL.OZ2101_PHASE_SENTAKU)%></TH>
										<TH width="15%"><%=i18n.get(GL.OZ2101_PHASE)%></TH>
										<TH width="15%"><%=i18n.get(GL.OZ2101_TANTO)%></TH>
										<TH width="12%"><%=i18n.get(GL.OZ2101_SYORI)%></TH>
										<TH width="14%"><%=i18n.get(GL.OZ2101_SYORI_DATE)%><BR><bean:write name="SashimodoshiForm" property="dt_title" /></TH>
									</TR>
								</THEAD>
								<TBODY>
 									<logic:notEmpty name="SashimodoshiForm" property="ar_rireki">
										<nested:iterate id="meisai" name="SashimodoshiForm" property="ar_rireki" indexId="idx">
 											<nested:notEmpty>
 	 											<logic:iterate id="rireki" name="meisai" property="rireki_list" indexId="idx_ex">
													<% 
														String cId = String.valueOf(idx); 
														String rId = String.valueOf(idx_ex); 
														String chk = "ar_rireki[" + idx + "].chk_id"; 
														String rbtn = "ar_rireki[" + idx + "].radio_id"; 
														RirekiListBean list_bean = (RirekiListBean)SashimodoshiForm.getAr_rireki().get(idx);
														RirekiBean rireki_bean = (RirekiBean)list_bean.getRireki_list().get(idx_ex);
													%> 
													<TR>
												 	  	<logic:equal name="rireki" property="soshiki_chk" value="true">
															<TD class="<%=rireki_bean.getTd_styleBottom()%>" style="text-align:center;"><nested:checkbox property="chk_id" value="<%=cId%>" onclick="check(this);"/></TD>
														</logic:equal>
													  	<logic:equal name="rireki" property="soshiki_chk" value="false">
															<TD class="<%=rireki_bean.getTd_styleBottom()%>" style="text-align:center;"></TD>
														</logic:equal>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>"><bean:write name="rireki" property="sateikaisya_cd" />&nbsp;</TD>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>"><bean:write name="rireki" property="soshiki" />&nbsp;</TD>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>" style="text-align:center;"><nested:radio property="radio_id" value="<%=rId%>" onclick="return radioCheck(this);"/></TD>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>"><bean:write name="rireki" property="phase_nm" />&nbsp;</TD>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>"><bean:write name="rireki" property="tanto_nm" />&nbsp;</TD>
														<TD class="<%=rireki_bean.getTd_styleBottom()%>"><bean:write name="rireki" property="syori" />&nbsp;</TD>
														<TD class="<%=rireki_bean.getTd_style()%>"><bean:write name="rireki" property="syori_dt" />&nbsp;</TD>
													</TR>
							    	  			</logic:iterate>
											</nested:notEmpty>
						      			</nested:iterate>
									</logic:notEmpty>

								</TBODY>
							</TABLE>
							</logic:equal>
						</logic:equal>
							<%--差戻先一覧(査定から滞留判定フェーズへ差戻以外)--%>
						<% if((("2").equals(SashimodoshiForm.getGamen_mode()) || ("3").equals(SashimodoshiForm.getGamen_mode())) || (("1").equals(SashimodoshiForm.getGamen_mode()) && !SashimodoshiForm.isTo_tairyu_sashi_flg())) {%>
							<br>
							<%=i18n.get(GL.COMMON_KAKKO)%>&nbsp;<%=i18n.get(GL.OZ2101_SASHI_ICHIRAN)%>&nbsp;<%=i18n.get(GL.COMMON_KAKKO_TOJI)%>
							<TABLE border=0 cellSpacing=0 cellPadding=0 style="border-left-color:#AAA">
								<THEAD>
									<TR>
										<TH width="8%" style="text-align:center;"><%=i18n.get(GL.OZ2101_PHASE_SENTAKU)%></TH>
										<TH width="15%"><%=i18n.get(GL.OZ2101_PHASE)%></TH>
										<TH width="6%"><%=SESSION_DATA_APP.getLbl_nm1()%></TH>
										<TH><%=i18n.get(GL.OZ2101_SOSHIKI)%></TH>
										<TH width="15%"><%=i18n.get(GL.OZ2101_TANTO)%></TH>
										<TH width="15%"><%=i18n.get(GL.OZ2101_SYORI)%></TH>
										<TH width="14%"><%=i18n.get(GL.OZ2101_SYORI_DATE)%><BR><bean:write name="SashimodoshiForm" property="dt_title" /></TH>
									</TR>
								</THEAD>
								<TBODY>
									<logic:notEmpty name="SashimodoshiForm" property="ar_rireki">
										<nested:iterate id="meisai" name="SashimodoshiForm" property="ar_rireki" indexId="idx">
											<nested:notEmpty>
  												<logic:iterate id="rireki" name="meisai" property="rireki_list" indexId="idx_ex">
													<TR>
														<% 
															String rId = String.valueOf(idx_ex); 
														%> 
														<TD class="center"><nested:radio property="radio_id" value="<%=rId%>"/></TD>
														<TD><bean:write name="rireki" property="phase_nm" />&nbsp;</TD>
														<TD><bean:write name="rireki" property="sateikaisya_cd" />&nbsp;</TD>
														<TD><bean:write name="rireki" property="soshiki" />&nbsp;</TD>
														<TD><bean:write name="rireki" property="tanto_nm" />&nbsp;</TD>
														<TD><bean:write name="rireki" property="syori" />&nbsp;</TD>
														<TD><bean:write name="rireki" property="syori_dt" />&nbsp;</TD>
													</TR>
								      			</logic:iterate>
											</nested:notEmpty>
						      			</nested:iterate>
									</logic:notEmpty>
								</TBODY>
							</TABLE>
						<%} %>
					</DIV>
				</form>
			</DIV>
		</DIV>
	</DIV>
</CENTER>
</BODY>
</HTML>