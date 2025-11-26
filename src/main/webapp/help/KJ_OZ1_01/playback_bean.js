function ImageObject(x, y, w, h, id, src)
{
	this.left=x;
	this.top=y;
	this.width=w;
	this.height=h;

	this.src=src;
	this.id=id;
	this.getDrawString=getDrawString;
	this.getStyleString=getStyleString;
	this.getID = getID;
	function getID()
	{
		return "DivImg" + this.id;
	}
	function getDrawString(){
		return "<div id=\"DivImg"+this.id+"\"><IMG SRC=\""+this.src+"\" BORDER=0 width=\""+this.width+"\" height=\""+this.height+"\" onmousemove=\"if(event.button==1){window.event.returnValue = false;}\"></div>";
	}
	function getStyleString(){
		return "#DivImg"+this.id+" {position:absolute; visibility:visible; left:"+this.left+"px; top:"+this.top+
		"px; width:"+this.width+"px; height:"+this.height+"px; z-index:0; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";
	}
}

function Actions()
{

	this.items=new Array();



}
function Action (id,left,top,width,height,bDefaultAction,borderColor,nextUrl,strCommand,strActionParam,strType,strSwitch,isAutoPass)
{
	this.nextUrl=nextUrl;
	this.command=strCommand;
	this.actionParam=strActionParam;
	this.type=strType;//'Mouse', 'Edit', 'Key', 'Wait'
	this.strSwitch = strSwitch;
	
	this.getDrawString=getDrawString;
	this.getStyleString=getStyleString;
	this.isMouse=isMouse;
	this.isEdit=isEdit;
	this.isWait=isWait;
	this.isKey=isKey;
	this.isButton=isButton;
	
	this.getID=getID;
	this.isAutoPass = isAutoPass;
	this.oldCmdValue = "";
	this.isEditPartialOK = isEditPartialOK;
	this.isEditOK = isEditOK;
	this.getNextStep = getNextStep;
	this.bDefaultAction = bDefaultAction;
	
	if(this.isMouse())
		this.rect = new Rect(left,top,width,height,borderColor,id);
	
	if(this.isEdit())
	{
		var strText;
			strText = '<input type="text" style="width:100%" name="editAct' + id + '" onmousedown="isEditAction=true;" onfocus="isEditionAction=true; if(parent.isAutoDemonstration()) this.disabled = true;" onblur="isEditAtion=false;" onkeydown=" parent.keyPressForEdit(this, actions.items[' + id + ']);" onkeyup=" parent.keyPressForEdit(this, actions.items[' + id + ']);" onhelp="parent.keyPressForEdit(this, actions.items[' + id + ']);">';
		this.rect = new EditRect(left,top,width,height,borderColor,"FFFFFF",strText,id);
	}
	
	if(this.isButton())
	{
		
		this.rect = new ButtonRect(left,top,width,height,this.actionParam,id);
	}
	
	
	
	
		
	function getID()
	{
		if(this.rect)
			return this.rect.getID();
		else
			return "";
	}
	
	function getDrawString()
	{
		if((this.isMouse() || this.isEdit()||this.isButton())&&(this.rect))
			return this.rect.getDrawString();

		return "";
	}
	
	function getStyleString()
	{
		if((this.isMouse() || this.isEdit()||this.isButton())&&(this.rect))
			return this.rect.getStyleString();
		
		return "";
	}
	
	
	function isMouse()
	{
		return(this.type=="M");
	}
	function isEdit()
	{
		return(this.type=="E");
	}
	function isWait()
	{
		return(this.type=="W");
	}
	function isKey()
	{
		return(this.type=="K");
	}
	
	function isButton()
	{
		return(this.type=="B");
	}
	
	
	
	
// 機能を拡張するため、削除しません。保留します。
	function isDefaultAction()
	{
		return this.bDefaultAction;
	}
	

	function isEditPartialOK(strText)
	{
		var	strTemp, str;
		strTemp = this.command ;
		str = "<textbegin>" + strText;
		if (strTemp.indexOf(str) != -1)
			return true;
		else
			return false;
	}

	function isEditOK(strText)
	{
		var	strTemp, str;
		strTemp = this.command ;
		str = "<textbegin>" + strText + "<textend>";
		if (strTemp.indexOf(str) != -1)
			return true;
		else
			return false;
	}
	
	function getNextStep(strText)
	{
		var	strCommand, strAction, str;
		strCommand = this.command ;
		var strArray = strCommand.split("<actionend>,<textbegin>");
		str = "<textbegin>" + strText + "<textend>";
		for (var i=0; i<strArray.length; i++)
		{
			if (strArray[i].indexOf("<textbegin>") == -1)
				strArray[i] = "<textbegin>" + strArray[i];
			if (strArray[i].indexOf("<actionend>") == -1)
				strArray[i] = strArray[i] + "<actionend>";
			if (strArray[i].indexOf(str) != -1)
			{
				strAction = strArray[i];
				strAction = strAction.replace(str, "");
				re = /<actionbegin>/g;
				strAction = strAction.replace(re, "");
				re = /<actionend>/g;
				strAction = strAction.replace(re, "");
				return strAction;
			}
		}
		return "";
	}

	this.isDragDrop = function()
	{
		return false;
	}
}

function Line(x, y, w, h, isLeftRight, color, order,index)
{
	this.left = x;
	this.top = y;
	this.width = w;
	this.height = h;
	this.color = color;
	this.isLeftRight = isLeftRight;
	this.order = order;
	this.getDrawString = getDrawString;
	this.getStyleString = getStyleString;
	this.getNSProp = getNSProp;
	this.getID = getID;
	
	function getID()
	{
		if(this.isLeftRight)
			return "DivLeftRightLine" + this.order;
			
		return "DivTopDownLine" + this.order;
	}

	function getDrawString()
	{
		var str;
		if(this.isLeftRight)
		{
			str = "<div id='"+this.getID()+"' style=\"width:"+this.width+"\"></div>";
		}
		else
		{
			str="<div id='"+this.getID()+"' style=\"height:"+this.height+"\"></div>";
		}
		
		return str;
	}

	function getStyleString()
	{
		var style;
		style="#"+this.getID();
		style+=" {position:absolute;left:"+this.left+";" +
						 " top:"+this.top+";width:"+this.width+"px;height:"+this.height+"px;"+
						 " layer-background-color:#"+this.color+
						 " ;background-color:#"+this.color+";font-size:1px;z-index:"+index+";}";
		return style;
	}

	function getNSProp()
	{
		var strProp;
		if(this.isLeftRight)
		{
			strProp = "document."+this.getID()+".clip.width="+this.width+";\n"
						+ " document."+this.getID()+".clip.height="+this.height+";\n";
		}
		else
		{
			strProp = "document."+this.getID()+".clip.width="+this.width+";\ndocument."+this.getID()+".clip.height="+this.height+";\n";
		}
		
		return strProp;
	}
}




function EditRect(x,y,w,h,fgColor,bgcolor,strText,id)
{
	this.left=x;
	this.top=y;
	this.width=w;
	this.height=h;
	this.fgColor=fgColor;
	this.bgcolor=bgcolor;
	this.text=strText;
	this.id=id;
	this.getDrawString=getDrawString;
	this.getStyleString=getStyleString;
	this.contains=contains;
	this.getID=getID;
	function getID(){
		return "EditRect"+this.id;
	}

	function contains(x,y)
	{
		return (x>=this.left&&x<=this.left+this.width&&y>=this.top&&y<=this.top+this.height);
	}

function getDrawString()
	{   
		var strDrawString;
		strDrawString="<div id=\""+this.getID()+"\">";
		strDrawString+="<FORM onsubmit=\"return false;\">";
		
if(fgColor=="-1")
			strDrawString+="<TABLE BORDER=\"0\" width=\""+this.width+"px\"   CELLSPACING=\"1\" CELLPADDING=\"0\" id=form1 name=form1>";
		else	
	    strDrawString+="<TABLE BORDER=\"0\" width=\""+this.width+"px\"  bgcolor=\"#"+this.fgColor+"\" CELLSPACING=\"1\" CELLPADDING=\"0\" id=form1 name=form1>";
		
		strDrawString+="<TR>";
		strDrawString+="	<TD width=\"100%\">";
		
		
		
		strDrawString+="<FONT SIZE=\"-1\" COLOR=\"#0\" style=\"margin:0 0 0 0;\">"+this.text+"</FONT>";
		
		
		
		strDrawString+="		</TD>\n</TR>\n</TABLE>";
			
		strDrawString+="	</FORM>\n</div>"+"\n";
		
		return strDrawString;
	}
	function getStyleString()
	{
		var strStyle;
		strStyle="#"+this.getID()+" {border:0;position:absolute; visibility:visible; left:"+
		this.left+"px; top:"+this.top+"px; width:"+this.width+"px; height:"+this.height+"px; z-index:"+this.id+";}";
		return strStyle;
	}
}

function Rect(x, y, w, h, fgColor,id)
{
	x=x-1;
	y=y-1;
	this.lTopDownLine=new Line(x,y,2,h,false,fgColor,"RectLeft"+id,id);
	this.rTopDownLine=new Line(x+w,y,2,h,false,fgColor,"RectRight"+id,id);
	this.tLeftRightLine=new Line(x,y,w,2,true,fgColor,"RectTop"+id,id);
	this.bLeftRightLine=new Line(x,y+h,w+2,2,true,fgColor,"RectBottom"+id,id);

	this.left=x;
	this.top=y;
	this.width=w;
	this.height=h;
	this.id=id;
	this.getDrawString=getDrawString;
	this.getStyleString=getStyleString;
	this.contains=contains;
	this.getID=getID;
	
	function getID()
	{
		var ids=new Array();
		ids[0]=this.lTopDownLine.getID();
		ids[1]=this.rTopDownLine.getID();
		ids[2]=this.tLeftRightLine.getID();
		ids[3]=this.bLeftRightLine.getID();
		return ids;
	}
	
	function contains(x,y)
	{
		return (x>=this.left&&x<=this.left+this.width&&y>=this.top&&y<=this.top+this.height);
	}
	
	function getDrawString()
	{
		var strDrawString;
		strDrawString=this.lTopDownLine.getDrawString()+"\n";
		strDrawString+=this.rTopDownLine.getDrawString()+"\n";
		strDrawString+=this.tLeftRightLine.getDrawString()+"\n";
		strDrawString+=this.bLeftRightLine.getDrawString()+"\n";
		return strDrawString;
	}
	
	function getStyleString()
	{
		var strStyle;
		strStyle=this.lTopDownLine.getStyleString(id)+"\n";
		strStyle+=this.rTopDownLine.getStyleString(id)+"\n";
		strStyle+=this.tLeftRightLine.getStyleString(id)+"\n";
		strStyle+=this.bLeftRightLine.getStyleString(id)+"\n";
		return strStyle;
	}
}



function ButtonRect(x, y, w, h, src,id)
{
	

	this.left=x;
	this.top=y;
	this.width=w;
	this.height=h;
	this.id=id;
	this.getDrawString=getDrawString;
	this.getStyleString=getStyleString;
	this.contains=contains;
	this.getID=getID;
	
	function getID()
	{
		return "ButtonRect"+this.id;
	}
	
	function contains(x,y)
	{
		return (x>=this.left&&x<=this.left+this.width&&y>=this.top&&y<=this.top+this.height);
	}
	
	function getDrawString()
	{
		var strDrawString;
		strDrawString="<div id=\""+this.getID()+"\">";
		strDrawString+="<img src='"+src+"' />";
		
		
	    strDrawString+="\n</div>";
		return strDrawString;
	}
	
	function getStyleString()
	{
		var strStyle="";
		strStyle="#"+this.getID()+" {position:absolute; visibility:visible; left:"+
		this.left+"px; top:"+this.top+"px; width:"+this.width+"px; height:"+this.height+"px; z-index:"+this.id+";}";
		return strStyle;
	}
}

