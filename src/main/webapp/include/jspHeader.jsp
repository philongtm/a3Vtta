<%@ page pageEncoding="Windows-31J" %>

<%@ taglib uri="/tags/struts-html" prefix="html" %>
<%@ taglib uri="/tags/struts-bean" prefix="bean" %>
<%@ taglib uri="/tags/struts-logic" prefix="logic" %>
<%@ taglib uri="/tags/struts-tiles" prefix="tiles" %>
<%@ taglib uri="/tags/struts-nested" prefix="nested" %>
<%@ taglib uri="/tags/common" prefix="common" %>

<%@ page import="java.util.*" %>
<%@ page import="common.*" %>
<%@ page import="common.global.*" %>
<%@ page import="common.struts.AppLocale" %>
<%@ page import="common.util.*" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.LinkedHashMap" %>

<%-- セッションデータ定義 --%>
<bean:define id="SESSION_DATA_APP" name="app.SessionData" type="app.SessionData" scope="session" />

<%
	/* クライアント側fの「キャッシュ」を無効化 */
	Calendar objCal1=Calendar.getInstance();
	Calendar objCal2=Calendar.getInstance();
	objCal2.set(1970,0,1,0,0,0);
	response.setDateHeader("Last-Modified",objCal1.getTime().getTime());
	response.setDateHeader("Expires",objCal2.getTime().getTime());
	response.setHeader("Pragma","no-cache,must-revalidate");
	response.setHeader("Cache-Control","no-cache");

	String helpStyle;
	if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {
		helpStyle = "helpStyleJa";
	}else{
		helpStyle = "helpStyleEn";
	}
	AppLocale.setDefault(request);
	JspMessage i18n = new JspMessage(session);
%>

<TITLE><%=i18n.get(GL.TITLE_SYSTEM)%></TITLE>
<link rel="stylesheet" href="../css/CommonStyle.css" type="text/css">

<%-- No862, 2008/06/16, SJA渡辺, 言語モードによりIMEコントロールを切り替えるように修正 --%>
<%if(SESSION_DATA_APP.getComLangMode().equals("Ja")) {%>
<link rel="stylesheet" href="../css/ime_mode_ja.css" type="text/css">
<%} else {%>
<link rel="stylesheet" href="../css/ime_mode_en.css" type="text/css">
<%}%>

<%--エラーメッセージの表示--%>
<logic:present name="<%=GS.MESSAGECONTEXT%>">
<script>
	setTimeout("errMsg()",500);
	function errMsg() {
		var msg = "<%= request.getAttribute(GS.MESSAGECONTEXT) %>";
		if(msg!="") {
			var ary = msg.split("\\n");
			alert(ary.join("\n"));
		}
		<% request.removeAttribute(GS.MESSAGECONTEXT); %>
	}
</script>
</logic:present>

<script>
	var BLOCK = false;
	var LOOKUP = false;
	
	<%--課題No.172--%>
	<%--追加開始--%>
	var winTori;
	var winDl;
	<%--追加完了--%>

	<%/***********************************************************
		blockSubmit/blockIt
		ボタン、リンクの連打をブロックする。
		onclick="return blockSubmit()"
	************************************************************/%>
	function blockSubmit(){
		if(BLOCK){
			window.alert("<%=i18n.get(GL.WARNING_WAITJAVASCRIPT)%>");
			return false;
		}
		setBlockSubmit(true);
		return true;
	}
	
	function resetBlockSubmit() {
		setBlockSubmit(false);
	}

	function setBlockSubmit(bBlock) {
		if(LOOKUP) return;
		BLOCK = bBlock;
		LOOKUP = true;
		parent.setBlockSubmit(bBlock);
		LOOKUP = false;
	}

	<%/***********************************************************
		添付ファイルダウンロード
	************************************************************/%>
	<%-- 課題No.52 添付ファイルダウンロード対応 --%>
	<%-- 追加開始 --%>
	function windowOpen(event,id,fileNm){
		form = document.forms[0];
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
	<%-- 追加完了 --%>

	<%/***********************************************************
		ショートカットキーの無効化
	************************************************************/%>
	function blockKeyDown(e) {
		block = true;
	
		switch(event.keyCode){
			case 0x1b:	<%-- ESC --%>
			<%-- case 0x70: // F1 機能しない為削除 --%>
			case 0x72:	<%-- F3 --%>
			case 0x74:	<%-- F5 --%>
			case 0x7a:	<%-- F11 --%>
				break;
				
			<%-- case 0x24:// HOME 機能しない為削除--%>
			case 0x25:	<%-- ← --%>
			case 0x27:	<%-- → --%>
				if( event.altKey ){ <%-- ALT --%>
					return false;
				}
				block = false;
				break;
	
			case 0x52:	<%-- R --%>
				block = event.ctrlKey; <%-- CTL --%>
				break;
	
			case 0x08:	<%-- BS --%>
				<%-- INPUTタグ(text,password)の場合は入力可 --%>
				for (i = 0; i < document.all.tags("INPUT").length; i++) {
					if (document.all.tags("INPUT")(i).name == window.event.srcElement.name &&
						(document.all.tags("INPUT")(i).type == "text" || document.all.tags("INPUT")(i).type == "password" || document.all.tags("INPUT")(i).type == "file") &&
						 document.all.tags("INPUT")(i).readOnly == false) {
						return true;
					}
				}
				<%-- TEXTAREAタグの場合は入力可 --%>
				for (i = 0; i < document.all.tags("TEXTAREA").length; i++) {
					if (document.all.tags("TEXTAREA")(i).name == window.event.srcElement.name &&
						document.all.tags("TEXTAREA")(i).readOnly == false) {
						return true;
					}
				}
				break;
			
			default:
				block = false;				
		}
	
		if( block ) {
			window.event.keyCode = 0;
			window.event.cancelBubble = false;
			window.event.returnValue = false;
			return false;
		} else {
			return true;
		}
	}
	
	resetBlockSubmit();

	window.document.onkeydown=blockKeyDown;

	function message(msg) {
		alert(msg);
	}

	<%/***********************************************************
		子ウィンドウクローズ
	************************************************************/%>
	function subClose(){
		if(winTori != null && !winTori.closed){
			winTori.close();
		}
		if(winDl != null && !winDl.closed){
			winDl.close();
		}
	}
	
	<%/***********************************************************
		アップロードファイルの拡張子のチェック
	************************************************************/%>
	function uploadFileChk(path) {
		if(path=="") return true;
		<%-- ext = "bmp,jpeg,jpg,gif,tiff,png,dib,html,htm"; 2005/12/22 htmlを有効にする --%>
		ext = "bmp,jpeg,jpg,gif,tiff,png,dib";
		a = path.toLowerCase().split(".");
		if(ext.indexOf(a[a.length-1])>=0){
			alert("<%=i18n.get(GL.WARNING_EXTENSIONJAVASCRIPT)%>");
			return false;
		} else {
			return true;
		}
	}
	
	<%/***********************************************************
		Status画面表示
	************************************************************/%>
	function showStatusDialog() {
		var	link;
		var	option;
		form = document.forms[0];
	    link = "<%=GS.WEB_ROOT%>/common/status.do";
		option = "dialogWidth:950px;dialogHeight:450px;dialogLeft:50px;dialogTop:100px;status:no";
		var	args = new Array("subWindow");
		return showModalDialog(link, args, option);
	}
	<%/************************************************************
		onkeydown イベントハンドラ：連打ブロックあり
	************************************************************/%>
	function doSubmit(event) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		<%--課題No.172--%>
		<%--追加開始--%>
		subClose();
		<%--追加完了--%>
		form = document.forms[0];
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
	<%/************************************************************
		onkeydown イベントハンドラ：連打ブロックなし
	************************************************************/%>
	function doSubmitNon(event) {
		form = document.forms[0];
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
	<%/************************************************************
		onkeydown イベントハンドラ：連打ブロックなし(ヘルプ画面用)
	************************************************************/%>
	function doSubmitNonHelp(event) {
	    win = window.open('<%= request.getAttribute(GS.HELP_PATH) %>','HELP',
        'toolbar=yes,status=yes,menubar=yes,scrollbars=yes,resizable=yes,directories=no,location=no,left=0,top=0');
        window.opener=win;
        win.focus();
	}
	<%/************************************************************
		onkeydown イベントハンドラ：連打ブロックなし(一括取込画面用)
	************************************************************/%>
	function doSubmitNonTorikomi() {
	    winTori = window.open('../tairyu/torikomi.jsp','_TORIKOMI',
        'toolbar=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,directories=no,location=no,width=400,height=100,left=0,top=0');
        winTori.focus();
	}
	<%/************************************************************
		onkeydown イベントハンドラ：連打ブロックなし(帳票言語選択用)
	************************************************************/%>
	function doSubmitNonDownload(event,langKbn) {
		if(langKbn == "1" || langKbn == "2"){
			form = document.forms[0];
			form.action += "?<%=GS.EVENT%>=" + event + langKbn;
			form.submit();
		}else{
		    winDl = window.open('../common/download_<%=SESSION_DATA_APP.getComLangMode()%>.html','DOWNLOAD',
        	'toolbar=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,directories=no,location=no,width=350,height=200,left=0,top=0');
    	    window.opener=winDl;
	        winDl.focus();
		}
	}
	<%/************************************************************
		onkeydown イベントハンドラ（引数あり）
	************************************************************/%>
	function syosai(event,anken_no,id) {
		<%--ボタン連打ブロック--%>
		if(blockSubmit()==false) return;
		<%--課題No.172--%>
		<%--追加開始--%>
		subClose();
		<%--追加完了--%>
		form = document.forms[0];	
		form.elements["anken_no"].value = anken_no;
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
	<%/************************************************************
		onkeydown イベントハンドラ（引数あり/連打ブロックなし）
	************************************************************/%>
	function download(event,anken_no,id) {		
		form = document.forms[0];	
		form.elements["anken_no"].value = anken_no;
		form.elements["id"].value = id;
		action = form.action;
		form.target = "_top";
		form.action += "?<%=GS.EVENT%>=" + event;
		form.submit();
	}
	<%-- 管理票No200807071019, 2008/07/09, SJA渡辺, ポップアップを表示するように修正 --%>
	function mogitoriConfirm(event,anken,id,tanto) {
		var obj;
		<%-- 空白を除去 --%>
		if (tanto == null) {
			obj = null;
		} else {
			obj = tanto.split(" ").join("").split("　").join("");
		}
		
		if (obj == null || obj == "" || obj.length == 0) {
			if(window.confirm('<%=i18n.get(GL.CONFIRM_TAKE)%>')){
				syosai(event,anken,id);
			}
		} else {
			syosai(event,anken,id);
		}
	}
	<%-- 管理票No200807071030, 2008/07/09, SJA渡辺, ポップアップを表示するように修正 --%>
	function backConfirm(event) {
		form = document.forms[0];
		if(window.confirm('<%=i18n.get(GL.CONFIRM_DISAPPEARE)%>')){
			<%--ボタン連打ブロック--%>
			if(blockSubmit()==false) return;
			<%--課題No.172--%>
			<%--追加開始--%>
			subClose();
			<%--追加完了--%>
			action = form.action;
			form.target = "_top";
			form.action += "?<%=GS.EVENT%>=" + event;
			form.submit();
		}
	}

	<%/************************************************************
	onkeydown イベントハンドラ：連打ブロックなし(督促メール送信タイミング選択用)
	************************************************************/%>
	function doSubmitNonTokusokuMailTiming(langKbn) {
		winDl = window.open('../syokai/tokusoku_mail_timing_<%=SESSION_DATA_APP.getComLangMode()%>.html','TOKUSOKUMAILTIMING',
		'toolbar=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,directories=no,location=no,width=350,height=200,left=0,top=0');
	    window.opener=winDl;
        winDl.focus();
	}
</script>


<script language="vbscript">
	Option Explicit

<%/***********************************************************
	EXCELをダウンロードしてマクロを起動する
	引数１：ファイル名(拡張子なし)
************************************************************/%>
	sub doExcel(excelFile)
		On Error Resume Next
		dim objExcel
		Set objExcel = CreateObject("Excel.Application")
		objExcel.visible = false
		objExcel.DisplayAlerts  = false
		objExcel.Workbooks.Open("http://<%=request.getServerName()+request.getContextPath()+GS.EXCELDIR%>"+excelFile+".xls")
		if error() then 
			set objExcel = nothing
			exit sub
		end if
		objExcel.visible = true
		objExcel.Run("Main")
		set objExcel = nothing
	end sub

<%/***********************************************************
	エラーチェック
************************************************************/%>
	Function error()
		If Err.Number = 0 Then
			error = false
		else
			Call message(Err.Description,vbOKOnly+vbExclamation)
			error = true
		End If
	end Function
<%/***********************************************************
	アップロードファイルのチェック
	※ブラウザのセキュリティ変更が必要なため廃止
************************************************************/%>
<%--
	Function uploadChk(path)
		uploadChk = false
		On Error Resume Next
	  Dim fso, f1, f
		Set fso = CreateObject("Scripting.FileSystemObject")
		if error() then exit function
		if fso.FileExists(path) then
			set f = fso.GetFile(path)
			if f.Size > (<%=Profile.getInt(GS.PROFILE_UPLOADMAXSZ,2)%>*1024*1024) then
				Call message("<%=i18n.get(GL.W_INPUT_0009)%>")
			else
				uploadChk = true
			end if
		else
			Call message("<%=i18n.get(GL.W_INPUT_0125)%>")
		end if
	end Function
--%>
</script>

<% if( request.getAttribute(GS.MERGEDXLS) != null ){ %>
<script>
	setTimeout("doMergedExcel()",100);
	function doMergedExcel() {
		doExcel('<%=request.getAttribute(GS.MERGEDXLS)%>');
	}
</script>
<% request.removeAttribute(GS.MERGEDXLS); } %>