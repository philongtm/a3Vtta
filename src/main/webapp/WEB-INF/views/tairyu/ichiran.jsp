<META HTTP-EQUIV="content-type" CONTENT="text/html;charset=SHIFT_JIS">
<%@ page language="java" contentType="text/html; charset=Windows-31J" errorPage="/include/jspException.jsp" %>

<HTML>
<HEAD>
    <%@ include file="/include/jspHeader.jsp" %>
    <%@ include file="/include/jspUtil.jsp" %>

    <c:set var="IchiranForm" value="${sessionScope['01IchiranForm']}"/>
    <c:set var="pager" value="${IchiranForm.getPager()}"/>
</HEAD>
<BODY>
<CENTER>
    <%--ヘッダ部分--%>
    <DIV id="main">
        <DIV id="head">
            <IMG alt="Sojitz" src="<c:url value='/image/navi001.gif' />" width="89" height="52">
            <IMG alt="<%=i18n.get(GL.TITLE_SYSTEM)%>" src="<c:url value='/image/${i18n.get("img.title")}.gif' />"
                 height="54">
            <%-- ヘルプリンク --%>
            <a href="#" class="<%=helpStyle%>" onClick="doSubmitNonHelp('help_open');"><%=i18n.get(GL.LINK_HELP)%>
            </a>
        </DIV>

        <%--メニューリンク部分--%>
        <DIV id="menu">
            <%@ include file="/menu.jspf" %>
        </DIV>

        <%--コンテンツ部分--%>
        <DIV id="contents">
            <H1 class="title01"><%=i18n.get(GL.TITLE_OB1101)%>
            </H1>

            <DIV id="submenu">
                <input type="button" value="<%=i18n.get(GL.BTN_BACK)%>" onclick="doSubmit('menuLinkOS2101')">
            </DIV>

            <DIV id="list">
                <form action="<c:url value='/tairyu/ichiran.do' />">

                    <%-- 勘定先リンククリック時の引数 --%>
                    <input type="hidden" value="${IchiranForm.anken_no}"/>
                    <input type="hidden" value="${IchiranForm.id}"/>

                    <%-- 自担当分/汎用２ラジオボタン --%>
                    <DIV class="leftbox">
                        <html:radio onclick="doSubmit('tanto')" property="tanto" value="1"/>
                        <DIV class="top3"><%=i18n.get(GL.COMMON_MYTASKS)%>&nbsp;</DIV>
                        <html:radio onclick="doSubmit('tanto')" property="tanto" value="2"/>
                        <DIV class="top3"><%=SESSION_DATA_APP.getLbl_nm2()%>
                        </DIV>
                    </DIV>

                    <%-- 各進捗件数 --%>
                    <DIV class="rightbox">
                        <%if (SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
                        <TABLE class="titl">
                            <TR>
                                <TD class="nonBorder">&nbsp;</TD>
                            </TR>
                            <TR>
                                <TD class="nonBorder"><%=i18n.get(GL.OB1101_TAIRYUHANTEI)%>
                                </TD>
                            </TR>
                            <TR>
                                <TD class="nonBorder"><%=i18n.get(GL.OB1101_TAIRYUHANTEI_K)%>
                                </TD>
                            </TR>
                        </TABLE>
                        <%} else {%>
                        <TABLE class="titl">
                            <TR>
                                <TD class="nonBorder">&nbsp;</TD>
                            </TR>
                            <TR>
                                <TD class="nonBorder">
                                    <DIV class="top10"><%=i18n.get(GL.OB1101_TAIRYUHANTEI)%>
                                    </DIV>
                                </TD>
                            </TR>
                            <TR>
                                <TD class="nonBorder">
                                    <DIV class="top3"><%=i18n.get(GL.OB1101_TAIRYUHANTEI_K)%>
                                    </DIV>
                                </TD>
                            </TR>
                        </TABLE>
                        <%}%>
                        <TABLE class="tbl">
                            <TR>
                                <TH><%=i18n.get(GL.COMMON_UNPROCESSED)%>
                                </TH>
                                <TH><%=i18n.get(GL.COMMON_PROCESSING)%>
                                </TH>
                                <TH><%=i18n.get(GL.COMMON_WATING_APPROVAL)%>
                                </TH>
                                <TH><%=i18n.get(GL.COMMON_COMPLETE)%>
                                </TH>
                            </TR>
                            <TR>
                                <TD><c:out value="${IchiranForm.hantei_misyori}"/></TD>
                                <TD><c:out value="${IchiranForm.hantei_syorityu}"/></TD>
                                <TD><c:out value="${IchiranForm.hantei_syoninmati}"/></TD>
                                <TD><c:out value="${IchiranForm.hantei_kanryo}"/></TD>
                            </TR>
                            <TR>
                                <TD><c:out value="${IchiranForm.kensyo_misyori}"/></TD>
                                <TD><c:out value="${IchiranForm.kensyo_syorityu}"/></TD>
                                <TD><c:out value="${IchiranForm.kensyo_syoninmati}"/></TD>
                                <TD><c:out value="${IchiranForm.kensyo_kanryo}"/></TD>
                            </TR>
                        </TABLE>
                    </DIV>

                    <DIV class="headerlist">
                        <TABLE>
                            <TR style="width:100%;">
                                <%-- 査定期セレクトボックス --%>
                                <TD style="width:8%;"><%=i18n.get(GL.COMMON_ASSESSING_PERIOD)%>
                                </TD>
                                <TD style="width:14%;">
                                    <select name="sateiki" onchange="doSubmit('sateiki')" style="width:80">
                                        <c:forEach var="item" items="${IchiranForm.ar_sateiki}">
                                            <option value="${item.value}"
                                                    <c:if test="item.value == IchiranForm.sateiki">selected</c:if>>${item.key}</option>
                                        </c:forEach>
                                    </select>
                                </TD>

                                <%-- ソート順セレクトボックス --%>
                                <TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SORT)%>
                                    <%-- ソート項目 --%>
                                <TD style="width:13%;">
                                    <select name="sort_item" onchange="doSubmit('sort_item')" style="width:100">
                                        <c:forEach var="item" items="${IchiranForm.ar_sort_item}">
                                            <option value="${item.value}"
                                                    <c:if test="item.value == IchiranForm.sort_item">selected</c:if>>${item.key}</option>
                                        </c:forEach>
                                    </select>
                                </TD>
                                <%-- 整列方向 --%>
                                <TD style="width:3%;">
                                    <select name="sort_order" onchange="doSubmit('sort_order')" style="width:70">
                                        <c:forEach var="item" items="${IchiranForm.ar_sort_order}">
                                            <option value="${item.value}"
                                                    <c:if test="item.value == IchiranForm.sort_item">selected</c:if>>${item.key}</option>
                                        </c:forEach>
                                    </select>
                                </TD>

                                <%-- 表示件数セレクトボックス --%>
                                <TD class="right" style="width:9%;"><%=i18n.get(GL.COMMON_SHOW)%>
                                </TD>
                                <TD style="width:9%;">
                                    <select name="view" onchange="doSubmit('show')" style="width:70">
                                        <c:forEach var="item" items="${IchiranForm.ar_show}">
                                            <option value="${item.value}"
                                                    <c:if test="item.value == IchiranForm.sort_item">selected</c:if>>${item.key}</option>
                                        </c:forEach>
                                    </select>
                                </TD>

                                <%-- ←前のXX件 --%>
                                <TD style="width:13%;">
                                    <c:if test="${not empty IchiranForm.x}">
                                        <%if (SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
                                        <a href="#" onClick="doSubmit('prevX')"><c:out value="${IchiranForm.x}"/></a>
                                        <%} else {%>
                                        <a href="#" onClick="doSubmit('prevX')"><c:out value="${IchiranForm.xen}"/></a>
                                        <%}%>
                                    </c:if>
                                </TD>

                                <%-- 次のXX件→ --%>
                                <TD style="width:14%;">
                                    <c:if test="${not empty IchiranForm.y}">
                                        <%if (SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
                                        <a href="#" onClick="doSubmit('nextY')"><c:out value="${IchiranForm.y}"/></a>
                                        <%} else {%>
                                        <a href="#" onClick="doSubmit('nextY')"><c:out value="${IchiranForm.yen}"/></a>
                                        <%}%>
                                    </c:if>
                                </TD>

                                <%-- XX/YY件 --%>
                                <TD class="right" style="width:12%;">
                                    ${pager.lastIndexOfCurrentPage}${i18n.get("Common.slash")}${pager.listSize}&nbsp;${i18n.get("Common.data")}
                                </TD>
                            </TR>
                        </TABLE>
                    </DIV>

                    <%-- 一覧情報 --%>
                    <DIV class="mainlist">
                        <TABLE>
                            <THEAD>
                            <TR>
                                <TH colspan="2" width="11%"><%=i18n.get(GL.OB1101_KANJO_CD)%>
                                </TH>
                                <TH width="49%"><%=i18n.get(GL.OB1101_KANJO_NM)%>
                                </TH>
                                <TH colspan="3" width="16%"><%=i18n.get(GL.OB1101_COUNTRY)%>
                                </TH>
                                <TH colspan="3" width="15%"><%=i18n.get(GL.OB1101_KINGAKU_TOTAL)%>
                                </TH>
                                <TH width="9%" class="borderRight"><%=i18n.get(GL.OB1101_TAISYO_YM)%>
                                </TH>
                            </TR>
                            <TR>
                                <TH width="6%"><%=SESSION_DATA_APP.getLbl_nm1()%>
                                </TH>
                                <TH colspan="2" width="58%"><%=i18n.get(GL.OB1101_SOSHIKI)%>
                                </TH>
                                <TH colspan="4" width="16%"><%=i18n.get(GL.OB1101_TANTO_NM)%>
                                </TH>
                                <TH colspan="3" width="22%" class="borderRight"><%=i18n.get(GL.OB1101_PROGRESS)%>
                                </TH>
                            </TR>
                            </THEAD>
                            <TBODY>
                            <c:if test="${not empty IchiranForm.list}">
                                <c:forEach var="item" items="${IchiranForm.list}">
                                    <TR>
                                        <TD colspan="2" width="11%">
                                            <c:if test="${item.link_flg}">
                                                <a href="#" onClick="mogitoriConfirm('mogitori','<c:out
                                                        value="${item.anken_no}"/>','<c:out value="${item.id}"/>','
                                                    <c:out value="${item.tanto_nm}"/>')"><c:out
                                                        value="${item.kanjo_cd}"/></a>
                                            </c:if>
                                            <c:if test="${!item.link_flg}">
                                                <c:out value="${item.kanjo_cd}"/></c:if>
                                        </TD>
                                        <TD width="49%" class=""><c:out value="${item.kanjo_nm}"/></TD>
                                        <TD colspan="3" width="16%"><c:out value="${item.syozaikoku}"/></TD>
                                        <TD colspan="3" width="15%" class="right"><c:out value="${item.kingaku}"/>
                                        </TD>
                                        <TD width="9%" class="borderRight"><c:out value="${item.taisyo_ym_hyoji}"/>
                                        </TD>
                                    </TR>
                                    <TR>
                                        <TD width="6%" class="borderBottom"><c:out value="${item.sateikaisya_cd}"/>
                                        </TD>
                                        <TD colspan="2" width="58%" class="borderBottom "><c:out
                                                value="${item.soshiki}"/></TD>
                                        <TD colspan="4" width="16%" class="borderBottom "><c:out
                                                value="${item.tanto_nm}"/>
                                        </TD>
                                        <TD colspan="3" width="22%" class="borderBottom borderRight">
                                            <c:out value="${item.sintyoku}"/>
                                        </TD>
                                    </TR>
                                </c:forEach>
                            </c:if>
                            </TBODY>
                        </TABLE>
                    </DIV>
                </form>
            </DIV>
        </DIV>
    </DIV>
</CENTER>
</BODY>
</HTML>