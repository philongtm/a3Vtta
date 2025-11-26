<script language="javascript">

var errMsg;
var itemName;

var aryElements = new Object();
var numElements = 0;

<%/************************************************************
	onkeydown イベントハンドラ
************************************************************/%>
function onKeydown(no) {
	rc = true;
	e = window.event;

	<%-- ショートカットキーの無効化 --%>
	if(blockKeyDown(e)==false) {
		//alert("ng");
		return false;
	}
	
	<%-- ENTER/TAB操作 --%>
	if( (e.keyCode == 13) || (e.keyCode == 9) ) {
		if(e.shiftKey) {
			if(no <= 0) no=numElements;
			no--;
		} else {
			if((no+1) >= numElements) no=-1;
			no++;
		}
		if(aryElements[no].type!="hidden"){
			if(aryElements[no].disabled || aryElements[no].readOnly) {
				onKeydown(no);
			} else {
				aryElements[no].focus();
			}
		}
		rc = false;
	}
	e.cancelBubble = true;
	return rc;
}

<%/************************************************************
	onblur イベントハンドラ
************************************************************/%>
function onBlur(no,col1,col2) {
	e = window.event;
	if(e.srcElement.name.search(/T/) == 0) {
		val = e.srcElement.value;
		e.srcElement.value = val; <%-- セレクトが残る場合の対応 --%>
		if (isNaN(val)) {
		} else {
			if (col1 > 0) {
				e.srcElement.value = insertComma2(val,col2);
			}
		}
	}
}

<%/************************************************************
	onfocus イベントハンドラ
************************************************************/%>
function onFocus(no,sel) {
	if(sel == 1) {
		aryElements[no].select();
	}
}

<%/************************************************************
	onfocus イベントハンドラ 行の色変更対応
************************************************************/%>
function onFocusColorChange(no,sel,rowNo) {
    obj = document.all.item('header',rowNo);
    obj.style.backgroundColor='#FFCC99';
	if(sel == 1) {
		aryElements[no].select();
	}
}

<%/************************************************************
	onclick イベントハンドラ Shift + クリックを防ぐ
************************************************************/%>
document.onclick=shiftKeyCancel;
function shiftKeyCancel() {
	if (window.event.shiftKey) {
		return false;
	}
}

<%/************************************************************
	oncontextmenu イベントハンドラ 右クリックメニューを防ぐ
************************************************************/%>
<%-- 保留とするためコメントアウト 2008/06/01
document.oncontextmenu=noDspMenu;

function noDspMenu() {
	return false;
}
--%>

<%/************************************************************
	要素の制御情報の登録
	引数１：要素名
	引数２：配列型要素
			0:配列型ではない 1:配列型である
	引数３：フォーカス時の入力文字選択指定
			0:選択無し 1:選択有り
	引数４：行番号（未設定の場合は色変更を行いません）
	※注意　色を変更する行のヘッダの名称は"header"にし、且つ
	　　　　色を戻す処理はそれぞれの画面で組み込むこと！！
************************************************************/%>
function registElement( elementName, arrayFlg, fildSel, rowNo) {
	<%-- タグの存在確認 --%>
	obj = document.getElementById(elementName);
	if(obj==null){
		alert("registElement\n要素が見つかりません。\n\n" + elementName);
		return;
	}
	<%-- イベントハンドラ登録 --%>
	if(arrayFlg==0) {
		if (rowNo == undefined || rowNo == null) {
			obj.onfocus = new Function("","onFocus("+numElements+","+fildSel+");");
			obj.onkeydown = new Function("","return onKeydown("+numElements+");");
			aryElements[numElements] = obj;
			numElements++;
		} else {
			obj.onfocus = new Function("","onFocusColorChange("+numElements+","+fildSel+","+rowNo+");");
			obj.onkeydown = new Function("","return onKeydown("+numElements+");");
			aryElements[numElements] = obj;
			numElements++;
		}

	} else {
		obj = document.getElementsByName(elementName);
		for(i=0;i<obj.length;i++){
			obj2 = obj[i];
			obj2.onkeydown = new Function("","return onKeydown("+numElements+");");
			aryElements[numElements] = obj2;
			numElements++;
		}
	}
}

</script>
