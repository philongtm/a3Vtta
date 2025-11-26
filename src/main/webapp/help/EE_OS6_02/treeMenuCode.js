var treeMenuIndex = 0;

function TreeMenuItem(text, url, target, icon, type)
{
	this.text = text;
	this.done=false;
	if(type)
		this.type = type;	
	else
		this.type="";
	if(url)
		this.url = url;
	else
		this.url = "";
	if(this.type=="WindowCaption")
		this.url="";
	else if(this.type=="Branch")
		this.url="";
	if(target)
		this.target = target;
	else
		this.target = "";
	if(icon)
		this.icon = icon;
	else
		this.icon = "";
	this.submenu = null;
	this.parentMenu = null;
	this.index = treeMenuIndex++;
	this.makeSubmenu = TreeMenuMakeSubmenu;
}

function TreeMenuMakeSubmenu(menu)
{
	this.submenu = menu;
	this.submenu.parentItem = this;   
}

function TreeMenu()
{
	this.items = new Array();
	this.parentItem = null;
	this.addItem = treeMenuAddItem;
}

function treeMenuAddItem(item)
{
	item.parentMenu = this;
	this.items[this.items.length] = item;
}

var treeMenuDocument;		
var treeMenuWidth;		
var treeMenuExpand = null;	
var treeMenuSelected = -1;	
var treeMenuSelectedFound;	
var treeMenuScrollX;		
var treeMenuScrollY;		
var treeMenuLastItem;		
var treeMenuDepth;		
var treeMenuBars;		
var blnOpeningDoc = false;
var activeStepItem = null;
var blnFinished = false;

var strStepIconId = "stepicon";
var strDoneIconId = "doneicon";
var strMenuRowId = "menurow";
function treeMenuDisplay()
{
try{
	if(blnOpeningDoc) 
		return;
	blnOpeningDoc = true;
	blnFinished = false;
	var i, cookie;
	
	if(treeMenuExpand == null)
	{
		treeMenuExpand = new Array();
		for(i=0; i<treeMenuIndex; i++)
			treeMenuExpand[i] = 0;
	}
	if(activeStepItem == null)
		activeStepItem = seachItem(treeMenu,activePageURL);
	var item = activeStepItem;
	while(item!=null && item.parentMenu!=null)
	{
		if(treeMenuExpand[item.index]==0)
		{
			treeMenuExpand[item.index] = 1; 
		}
		item = item.parentMenu.parentItem;
	}
	treeMenuExpand[activeStepItem.index] = 1;
	if(treeMenuDays!=null)
	{
		var date = new Date ();
		date.setTime (date.getTime() + (86400 * 1000 * treeMenuDays));		
	}
	treeMenuDocument = stepFrame.document;

	treeMenuDepth = 0;
	treeMenuBars = new Array();

	treeMenuSelectedFound = false;
	treeMenuScrollX = 36;

	if(document.images)
		treeMenuDocument.open("text/html", "replace");
	else
		treeMenuDocument.open("text/html");
		
	treeMenuDocument.writeln('<html>');
	treeMenuDocument.writeln('<head>');
	treeMenuDocument.writeln('<meta http-equiv="content-type" content="text/html; charset='+ strCharset +'">');
	treeMenuDocument.writeln('<meta http-equiv="imagetoolbar" content="no">');
	treeMenuDocument.writeln('<title></title>');
	treeMenuDocument.writeln('<style type="text/css">a {text-decoration:none;}</style>');
	treeMenuDocument.writeln('<script LANGUAGE="JavaScript">');
	treeMenuDocument.writeln('<!--');
	treeMenuDocument.writeln('document.oncontextmenu=new Function("return false");');
	treeMenuDocument.writeln('//-->');
	treeMenuDocument.writeln('</script>');
	treeMenuDocument.writeln('</head>');
	treeMenuDocument.writeln('<body bgcolor="' + treeMenuBgColor + '" text="' + treeMenuFgColor + '" link="' + treeMenuFgColor + '" alink="' + treeMenuFgColor + '" vlink="' + treeMenuFgColor + '" onLoad="if(document.layers) parent.treeMenuScroll();">');
	treeMenuDocument.writeln('<table  lang = "ja"  border=0 cellpadding=0 cellspacing=0 width=' + treeMenuWidth + '>');
	treeMenuDocument.write('<tr valign=top><td>');
	treeMenuDocument.write('<img src="' + treeMenuImgDir + 'menu_root.gif" align=left border=0 vspace=0 hspace=0>');
	treeMenuDocument.write('&nbsp;' + treeMenuRoot + '&nbsp;');
	treeMenuDocument.writeln('</td><td width=50>&nbsp;' + strDoneMsg + '</td></tr>');
	treeMenuListItems(treeMenu);
	treeMenuDocument.writeln('</table>');
	treeMenuDocument.writeln('</body>');  
	treeMenuDocument.writeln('</html>');
	treeMenuDocument.close();
	blnOpeningDoc=false;
	blnFinished = true;
	if(document.all)
		treeMenuScroll();
	havetoWait = false;
}
catch (e) {};
}

function treeMenuListItems(menu)
{

	var i;

	for(i = 0; i<menu.items.length; i++)
	{
		if(i==menu.items.length - 1)
			treeMenuLastItem = true;
		else
			treeMenuLastItem = false;
		treeMenuDisplayItem(menu.items[i]);
	}
}

function treeMenuDisplayItem(item)
{
	var bars, cmd, expanded, i, img, link, more, submenu;  

	if(treeMenuExpand[item.index] == 1)
		expanded = true;
	else
		expanded = false;

	if(item.submenu)
		submenu = true;
	else
		submenu = false;
	if(item.url != "")
		link = true;
	else
		link = false;
	cmd = "return parent.treeMenuClick(" + item.index + ", " + link + ", " + submenu + ", '" + item.url + "');";

	if((item.url!="")&&(item.url==activePageURL))
	{
		bgcolor="#FFE7BD";
		activeStepItem=item;
		treeMenuSelected = item.index;
		treeMenuSelectedFound = true;
	}
	else if(item.type=="WindowCaption") 
		bgcolor="#BBDFF9";
	else if(item.type=="Branch") 
		bgcolor="#81F9F8";
	else 
		bgcolor="#DBF7FB";


	treeMenuDocument.write('<tr valign=top  bgcolor='+bgcolor+' id=' + strMenuRowId + item.index + '><td>');


	bars = new Array();
	for(i=0; i<treeMenuDepth; i++) 
	{
		if(treeMenuBars[i]) 
		{
			treeMenuDocument.write('<img src="' + treeMenuImgDir + 'menu_bar.gif" align=left border=0 vspace=0 hspace=0>');
			bars[i] = true;
		}
		else 
		{
			treeMenuDocument.write('<img src="' + treeMenuImgDir + 'menu_pixel.gif" align=left border=0 vspace=0 hspace=0 width=18 height=18>');
			bars[i] = false;
		}
		if(item.index == treeMenuSelected)
			treeMenuScrollX += 10;
	}

	more = false;
	if(item.submenu)
	{
		for (i = 0; i < item.submenu.items.length; i++)
		{
			if(item.submenu.items[i].submenu != null)
				more = true;
		}
	}

	if(!more)
	{
		if(treeMenuLastItem) 
		{
			img = "menu_corner.gif";
			bars[bars.length] = false;
		}
		else 
		{
			img = "menu_tee.gif";
			bars[bars.length] = true;
		}
		treeMenuDocument.write('<img src="' + treeMenuImgDir + img + '" align=left border=0 vspace=0 hspace=0>');
	}  

	if(more) 
	{
		if(expanded) 
		{
			if(treeMenuLastItem) 
			{
				img = "menu_corner_minus.gif";
				bars[bars.length] = false;
			}
			else 
			{
				img = "menu_tee_minus.gif";
				bars[bars.length] = true;
			}
		}
		else
		{
			if(treeMenuLastItem)
			{
				img = "menu_corner_plus.gif";
				bars[bars.length] = false;
			}
			else
			{
				img = "menu_tee_plus.gif";
				bars[bars.length] = true;
			}
		}    
		var cmd2="return parent.treeMenuClick(" + item.index + ",false,"+ submenu + ", '" + item.url + "');";
		treeMenuDocument.write('<a href="#" onClick="' + cmd2 + ';"><img src="' + treeMenuImgDir + img + '" align=left border=0 vspace=0 hspace=0></a>');    
	}

	if(item.url != "")
		treeMenuDocument.write('<a name="A' + item.index + '"  href="' + item.url + '" target="' + item.target + '" onClick="' + cmd + '">');
	else if(item.type=="WindowCaption")
		treeMenuDocument.write('<a name = "A' + item.index + '">');
	else
		treeMenuDocument.write('<a name = "A' + item.index + '" href="#" onClick="' + cmd + '">');

	if(item.icon != "")
		img = item.icon;
	else if(item.type=="Step")
	{
		img = (item.index==treeMenuSelected)?"step_icon_active.gif":"step_icon.gif";
	}
	else if(item.type=="WindowCaption")
		img = "step_win_icon.gif";
	else if(item.type=="Branch")
		img = "branch_icon.gif";
	else
		img = (item.index==treeMenuSelected)?"step_icon_active.gif":"step_icon.gif";
	treeMenuDocument.write('<img id=' + strStepIconId + item.index + ' src="' + treeMenuImgDir + img + '" align=left border=0 vspace=0 hspace=0>');

	if(item.index==treeMenuSelected)
		treeMenuDocument.write('<font face="ＭＳ ゴシック" size=2 font-size:5pt><span class="selected">&nbsp;' + item.text + '&nbsp;</span></font>');
	else
		treeMenuDocument.write('<font face="ＭＳ ゴシック" size=2 font-size:5pt> ' + item.text + '</font>');

	var strDrawDoneIcon;	
	if(item==activeStepItem)
		strDrawDoneIcon = '<td align=right><img id=' + strDoneIconId + item.index + ' src='+ (item.done?treeMenuImgDir +'done_icon.gif>':treeMenuImgDir +'undone_icon_active.gif>') + '</td>';
	else
		strDrawDoneIcon = '<td align=right><img id=' + strDoneIconId + item.index + ' src='+ (item.done?treeMenuImgDir +'done_icon.gif>':treeMenuImgDir +'undone_icon_inactive.gif>') + '</td>';
	treeMenuDocument.write('</a>');
	treeMenuDocument.writeln('</td>');
	treeMenuDocument.writeln(strDrawDoneIcon);
	treeMenuDocument.writeln('</tr>');

	treeMenuBars = bars;

	if(item.submenu && expanded)
	{
		treeMenuDepth++;
		treeMenuListItems(item.submenu);
		treeMenuDepth--;
	}
}


function treeMenuClick(n, link, submenu, url)
{
	if(isAutoDemonstration())
		return false;

	var date, cookie;
	if(!treeMenuExpand)
		treeMenuDisplay();

	if(submenu && !link)
		treeMenuExpand[n] = 1 - treeMenuExpand[n];
	treeMenuSelected = n;

	// Return the link flag.
	if(link)
	{
		activePageURL = url;
	}
	activePageURL = url;
	setTimeout("parent.treeMenuDisplay()", 10);

	return link;
}


// 機能を拡張するため、削除しません。保留します。
function treeMenuClickRoot()
{
	treeMenuExpand = null;
	treeMenuSelected = -1;
	setTimeout("parent.treeMenuDisplay()", 10);

	return false;
}

function findAnchor(strAnchorName)
{
	var anchors = treeMenuDocument.anchors;
	for(var i = 0; i < anchors.length; i++)
	{
		if(anchors[i].name == strAnchorName)
			return anchors[i];
	}
	
	return null;
}


function treeMenuScroll()
{
	var win, height, width;
	win = stepFrame;
	if(typeof activeStepItem != 'object')
		return;
	
	var activeAnchor = findAnchor ("A" + activeStepItem.index);
	var jg_n5 = (navigator.appName + parseInt(navigator.appVersion) == 'Netscape5');
	if(document.layers)
	{
   		height = win.innerHeight;
   		width = win.innerWidth;
   		if(activeAnchor != null)
   			treeMenuScrollY = activeAnchor.y - 16; 
	}
	else if(document.all)
	{
		height = win.document.body.clientHeight;
		width = win.document.body.clientWidth;	
		if(activeAnchor != null)
			treeMenuScrollY = activeAnchor.offsetParent.offsetTop;
	}
	else if(document.images)
	{
		win.scroll(0, treeMenuScrollY);
		return;
	}
	else
		return;

	win.scrollTo(0,treeMenuScrollY);	
}

function setDoneActiveStepItem(strNextUrl)
{
var nextItemTemp = null;
nextItemTemp = seachItem(treeMenu, strNextUrl);
if (nextItemTemp == null)
{
    alert("The specified step doesn't exist.");
    return false;
}
	if((typeof activeStepItem!='undefined')&&(activeStepItem!=null)&&(typeof stepFrame != 'undefined'))
	{
		activeStepItem.done=true;

		treeMenuDocument = stepFrame.document;
			
		treeMenuDocument.getElementById(strMenuRowId+activeStepItem.index).style.backgroundColor = "#DBF7FB";
		treeMenuDocument.getElementById(strDoneIconId+activeStepItem.index).src = treeMenuImgDir +'done_icon.gif';
		treeMenuDocument.getElementById(strStepIconId+activeStepItem.index).src = treeMenuImgDir +'step_icon.gif';
		treeMenuSelectedFound = true;
		expandActiveItem(activeStepItem, strNextUrl);
		treeMenuDocument.getElementById(strMenuRowId+activeStepItem.index).style.backgroundColor = "#FFE7BD";
		treeMenuDocument.getElementById(strStepIconId+activeStepItem.index).src = treeMenuImgDir +'step_icon_active.gif';
		treeMenuDocument.getElementById(strDoneIconId+activeStepItem.index).src = treeMenuImgDir +'undone_icon_active.gif';
		treeMenuSelected = activeStepItem.index;		
		treeMenuScroll();
	}
}

function setUndoneMenu(menu)
{
	var i;
	for(i=0; i<menu.items.length; i++)
	{
		menu.items[i].done=false;
		if(menu.items[i].submenu)
			setUndoneMenu(menu.items[i].submenu);
	}
}

function setUndoneAll()
{
	setUndoneMenu(treeMenu);
}

function expandActiveItem(item, url)
{
	if(openBranch1(item, url))
	{
		treeMenuDisplay();//Redraw Tree menu.
	}
}

// 機能を拡張するため、削除しません。保留します。
function isStepUrlInItemList(itemList, url, fromIndex)
{
	for (var i=fromIndex;i<itemList.length;i++)
	{		
		if(itemList[i].url != "")
		{
			if(itemList[i].url==url)
				return itemList[i];
		}
	}
	
	return null;
}

function seachItem(inMenu, itemUrl)
{
	var itemList = inMenu.items;
	var searchedItem = null;

	for(var i=0; i<itemList.length; i++)
	{
		if(itemList[i].url != "")
		{
			if(itemList[i].url == itemUrl)
			{
				searchedItem = itemList[i];
				break;
			}
		}
		if(itemList[i].submenu)
			searchedItem = seachItem(itemList[i].submenu,itemUrl);
		if(searchedItem)
			return searchedItem;
	}

	return searchedItem;
}

function openBranch1(item,activeUrl)
{
	var topMostMenu = item.parentMenu;
	var nextItem = null;

	while(topMostMenu.parentItem || topMostMenu.parentMenu)
	{
		if(topMostMenu.parentItem)
			topMostMenu = topMostMenu.parentItem;
		if(topMostMenu.parentMenu)
			topMostMenu = topMostMenu.parentMenu;
	}
	nextItem = seachItem(topMostMenu,activeUrl);
	if(nextItem == null)
		alert("The specified step doesn't exist.");
	if(nextItem.parentMenu == item.parentMenu)	
	{
		activeStepItem = nextItem;
		
		return false;			
	}
	activeStepItem = nextItem;
	while(nextItem!=null && nextItem.parentMenu!=null)
	{
		if(treeMenuExpand[nextItem.index]==0)
		{
			treeMenuExpand[nextItem.index] = 1; 
		}
		nextItem = nextItem.parentMenu.parentItem;
	}
	treeMenuExpand[item.index] = 1;
	treeMenuExpand[activeStepItem.index] = 1;
	
	return true;
}

