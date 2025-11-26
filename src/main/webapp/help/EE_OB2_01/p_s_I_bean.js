function RectangularCallout(id,boundary,xpoints,ypoints,body,textRect,fontName,fontSize,nAlign,nVAlign,strContent,bgcolor,fgColor,txtColor,stroke,txtZoomPercentage,nShowAfter, nAppearanceType, nEdgeWidth, nAttachedEdge,strPopImgUrl)
{
	this.id = id;
	this.boundary = boundary;	//instance of RECT
	this.xpoints = xpoints;		//instance of Array
	this.ypoints = ypoints;		//instance of Array
	this.body = body;			//instance of RECT
	this.textRect = textRect;
	this.bgcolor = bgcolor;		//in format "#rrggbb"
	this.fgColor = fgColor;
	this.txtColor = txtColor;
	this.stroke = stroke;		//thickness of the border
	this.strContent = strContent;
	this.fontName = fontName;
	this.timeToShow = nShowAfter;
	this.nAppearanceType = nAppearanceType;
	this.nEdgeWidth = nEdgeWidth;
	this.nAttachedEdge = nAttachedEdge;
	this.strPopImgUrl = strPopImgUrl;

	this.fontSize = fontSize+"px";

	if(nAlign == 1)
		this.hAlign = "center";
	else if(nAlign == 2)
		this.hAlign = "right";
	else if(nAlign == 3)
		this.hAlign = "justify;text-justify:distribute-all-lines;";
	else
		this.hAlign = "left";
		
   this.valign = nVAlign;	this.txtZoomPercentage = txtZoomPercentage;
	
	this.getType = function()
	{
		return "RactangularCallout";
	}
	this.getID = function()
	{
		return "DIVNOTE" + this.id;
	}
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.draw = draw;

	function getStyleString()
	{
		var str;

		str =  "#"+this.id+" {position:absolute; visibility:visible; left:"+this.boundary.left+"px; top:"+this.boundary.top+
				 	 "px; width:"+this.boundary.width+"px; height:"+this.boundary.height+"px; z-index:101; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";

		return str;
	}
	
	function getDrawString()
	{
		var str;
		
		str = "<div id="+this.getID()+"></div>";
		
		return str;
	}
	
	function draw()
	{
		var jsGph = new jsGraphics(this.getID(),contentFrame);
		var xtemp = boundary.left;
		var ytemp = boundary.top;
		if(xpoints != null && ypoints != null)
		{
		    if(xtemp>xpoints[0])
		    {
		        xtemp = xpoints[0];
		    }
		    if(ytemp>ypoints[0])
		    {
		        ytemp = ypoints[0];
		    }
		}
		jsGph.drawPopImage(strPopImgUrl,xtemp,ytemp);
		/* draw string*/
		jsGph.setColor(this.txtColor);
		jsGph.setFont(this.fontName,this.fontSize,Font.PLAIN);
		jsGph.drawString(this.strContent,this.textRect.left,this.textRect.top,this.textRect.width,this.textRect.height,this.hAlign,this.valign,this.txtZoomPercentage);
		jsGph.paint();
   }
}

function LineCallout(id,boundary,xpoints,ypoints,body,textRect,fontName,fontSize,nAlign,nVAlign,strContent,bgcolor,fgColor,txtColor,stroke,txtZoomPercentage,nShowAfter, nAppearanceType, nEdgeWidth, nAttachedEdge,strPopImgUrl)
{
	this.id = id;
	this.boundary = boundary;	//instance of RECT
	this.xpoints = xpoints;		//instance of Array
	this.ypoints = ypoints;		//instance of Array
	this.body = body;			//instance of RECT
	this.textRect = textRect;
	this.bgcolor = bgcolor;
	this.fgColor = fgColor;
	this.txtColor = txtColor;
	this.stroke = stroke;
	this.strContent = strContent;
	this.fontName = fontName;
	this.timeToShow = nShowAfter;
	this.nAppearanceType = nAppearanceType;
	this.nEdgeWidth = nEdgeWidth;
	this.nAttachedEdge = nAttachedEdge;

	this.fontSize = fontSize+"px";
	this.strPopImgUrl = strPopImgUrl;

	if(nAlign == 1)
		this.hAlign = "center";
	else if(nAlign == 2)
		this.hAlign = "right";
	else if(nAlign == 3)
		this.hAlign = "justify;text-justify:distribute-all-lines;";
	else
		this.hAlign = "left";
		
   this.valign = nVAlign;	this.txtZoomPercentage = txtZoomPercentage;
	
	this.getType = function()
	{
		return "LineCallout";
	}
	this.getID = function()
	{
		return "DIVNOTE" + this.id;
	}
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.draw = draw;
	
	function getStyleString()
	{
		var str;

		str =  "#"+this.id+" {position:absolute; visibility:visible; left:"+this.boundary.left+"px; top:"+this.boundary.top+
				 	 "px; width:"+this.boundary.width+"px; height:"+this.boundary.height+"px; z-index:101; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";

		return str;
	}
	
	function getDrawString()
	{
		var str;
		
		str = "<div id="+this.getID()+"></div>";
		
		return str;
	}
	
	function draw()
	{
		var jsGph = new jsGraphics(this.getID(),contentFrame);
		var xtemp = boundary.left;
		var ytemp = boundary.top;
		if(xpoints != null && ypoints != null)
		{
		    if(xtemp>xpoints[0])
		    {
		        xtemp = xpoints[0];
		    }
		    if(ytemp>ypoints[0])
		    {
		        ytemp = ypoints[0];
		    }
		}
		jsGph.drawPopImage(strPopImgUrl,xtemp,ytemp);
		jsGph.setColor(this.txtColor);
		jsGph.setFont(this.fontName,this.fontSize,Font.PLAIN);
		jsGph.drawString(this.strContent,this.textRect.left,this.textRect.top,this.textRect.width,this.textRect.height,this.hAlign,this.valign,this.txtZoomPercentage);
		jsGph.paint();
	}
}

function OvalCallout(id,boundary,xpoints,ypoints,body,txtRect,fontName,fontSize,nAlign,nVAlign,strContent,bgcolor,fgColor,txtColor,stroke,txtZoomPercentage,nShowAfter, nAppearanceType, nEdgeWidth, nAttachedEdge,notesPosition,strPopImgUrl)
{
	this.id = id;
	this.boundary = boundary;	//instance of RECT
	this.xpoints = xpoints;		//instance of Array
	this.ypoints = ypoints;		//instance of Array
	this.body = body;			//instance of RECT
	this.bgcolor = bgcolor;
	this.fgColor = fgColor;
	this.txtColor = txtColor;
	this.stroke = stroke;
	this.strContent = strContent;
	this.fontName = fontName;
	this.timeToShow = nShowAfter;
	this.nAppearanceType = nAppearanceType;
	this.nEdgeWidth = nEdgeWidth;
	this.strPopImgUrl = strPopImgUrl;
	this.notesPosition = notesPosition;
	this.nAttachedEdge = nAttachedEdge;
   
	this.fontSize = fontSize+"px";
	
	this.txtRect = txtRect;
	if(nAlign == 1)
		this.hAlign = "center";
	else if(nAlign == 2)
		this.hAlign = "right";
	else if(nAlign == 3)
		this.hAlign = "justify;text-justify:distribute-all-lines;";
	else
		this.hAlign = "left";
		
   this.valign = nVAlign;	this.txtZoomPercentage = txtZoomPercentage;
	
	this.getType = function()
	{
		return "OvalCallout";
	}
	this.getID = function()
	{
		return "DIVNOTE" + this.id;
	}
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.draw = draw;
	
	function getStyleString()
	{
		var str;
       
		str =  "#"+this.id+" {position:absolute; visibility:visible; left:"+this.boundary.left+"px; top:"+this.boundary.top+
				 	 "px; width:"+this.boundary.width+"px; height:"+this.boundary.height+"px; z-index:101; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";
       
		return str;
	}
	
	function getDrawString()
	{
		var str;
		
		str = '<div id='+this.getID()+'></div>';
		
		return str;
	}
	
	function draw()
	{
		var jsGph = new jsGraphics(this.getID(),contentFrame);
		var xtemp = boundary.left;
		var ytemp = boundary.top;
		if(xpoints != null && ypoints != null)
		{
		    if(xtemp>xpoints[0])
		    {
		        xtemp = xpoints[0];
		    }
		    if(ytemp>ypoints[0])
		    {
		        ytemp = ypoints[0];
		    }
		}
		jsGph.drawPopImage(strPopImgUrl,xtemp,ytemp);
		/* string*/
	    jsGph.setColor(this.txtColor);
		jsGph.setFont(this.fontName,this.fontSize,Font.PLAIN);
		jsGph.drawString(this.strContent,this.txtRect.left,this.txtRect.top,this.txtRect.width,this.txtRect.height,this.hAlign,this.valign,this.txtZoomPercentage);
		
		jsGph.paint();
	}
}

function Rectangular(id,boundary,textRect,fontName,fontSize,nAlign,nVAlign,strContent,bgcolor,fgColor,txtColor,stroke,txtZoomPercentage,nShowAfter, nAppearanceType, nEdgeWidth, nAttachedEdge,strPopImgUrl)
{
	this.id = id;
	this.boundary = boundary;	//instance of RECT
	this.textRect = textRect;
	this.bgcolor = bgcolor;		//in format "#rrggbb"
	this.fgColor = fgColor;
	this.txtColor = txtColor;
	this.stroke = stroke;		//thickness of the border
	this.strContent = strContent;
	this.fontName = fontName;
	this.timeToShow = nShowAfter;
	this.nAppearanceType = nAppearanceType;
	this.nEdgeWidth = nEdgeWidth;
	this.nAttachedEdge = nAttachedEdge;
	this.fontSize = fontSize+"px";
	this.strPopImgUrl = strPopImgUrl;

	if(nAlign == 1)
		this.hAlign = "center";
	else if(nAlign == 2)
		this.hAlign = "right";
	else if(nAlign == 3)
		this.hAlign = "justify;text-justify:distribute-all-lines;";
	else
		this.hAlign = "left";
		
   this.valign = nVAlign;	this.txtZoomPercentage = txtZoomPercentage;
	
	this.getType = function()
	{
		return "Rectangular";
	}
	this.getID = function()
	{
		return "DIVNOTE" + this.id;
	}
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.draw = draw;
	
	function getStyleString()
	{
		var str;

		str =  "#"+this.id+" {position:absolute; visibility:visible; left:"+this.boundary.left+"px; top:"+this.boundary.top+
				 	 "px; width:"+this.boundary.width+"px; height:"+this.boundary.height+"px; z-index:101; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";

		return str;
	}
	
	function getDrawString()
	{
		var str;
		
		str = "<div id="+this.getID()+"></div>";
		
		return str;
	}
	
	function draw()
	{
		var jsGph = new jsGraphics(this.getID(),contentFrame);
		var xtemp = boundary.left;
		var ytemp = boundary.top;
		jsGph.drawPopImage(strPopImgUrl,xtemp,ytemp);
		jsGph.setColor(this.txtColor);
		jsGph.setFont(this.fontName,this.fontSize,Font.PLAIN);
		jsGph.drawString(this.strContent,this.textRect.left,this.textRect.top,this.textRect.width,this.textRect.height,this.hAlign,this.valign,this.txtZoomPercentage);
		jsGph.paint();
	}
}

function FoldedConer(id,bOpened,imgSrc,boundary,textRect,fontName,fontSize,nAlign,nVAlign,strContent,bgcolor,fgColor,txtColor,stroke,txtZoomPercentage,nShowAfter, nAppearanceType, nEdgeWidth, nAttachedEdge,strPopImgUrl)
{
	this.id = id;
	this.boundary = boundary;	//instance of RECT
	this.textRect = textRect;
	this.bgcolor = bgcolor;		//in format "#rrggbb"
	this.fgColor = fgColor;
	this.txtColor = txtColor;
	this.stroke = stroke;		//thickness of the border
	this.strContent = strContent;
	this.fontName = fontName;
	this.timeToShow = nShowAfter;
	this.nAppearanceType = nAppearanceType;
	this.nEdgeWidth = nEdgeWidth;
	this.nAttachedEdge = nAttachedEdge;

	this.fontSize = fontSize+"px";

	this.strPopImgUrl = strPopImgUrl;
	this.bOpened = bOpened;
	this.imgSrc = imgSrc;
	if(nAlign == 1)
		this.hAlign = "center";
	else if(nAlign == 2)
		this.hAlign = "right";
	else if(nAlign == 3)
		this.hAlign = "justify;text-justify:distribute-all-lines;";
	else
		this.hAlign = "left";
		
   this.valign = nVAlign;	this.txtZoomPercentage = txtZoomPercentage;
	
	this.getType = function()
	{
		return "FoldedConer";
	}
	this.getID = function()
	{
		return "DIVNOTE" + this.id;
	}
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.draw = draw;
	
	this.onmouseover = function()
	{
		parent.bClickOnIcon = true;
		this.bOpened = true;
		this.draw();
	}
	
	function getStyleString()
	{
		var str;

		str =  "#"+this.id+" {position:absolute; visibility:visible; left:"+this.boundary.left+"px; top:"+this.boundary.top+
				 	 "px; width:"+this.boundary.width+"px; height:"+this.boundary.height+"px; z-index:101; background-color:#FFFFFF; layer-background-color:#FFFFFF;}";

		return str;
	}
	
	function getDrawString()
	{
		var str;
		
		str = "<div id="+this.getID()+"></div>";
		
		return str;
	}
	
	function draw()
	{
		var i = 0;
		var jsGph = new jsGraphics(this.getID(),contentFrame);
		
		if(this.bOpened)
		{
		    jsGph.drawPopImage(strPopImgUrl, this.boundary.left, this.boundary.top);
		    jsGph.setColor(this.txtColor);
		    contentFrame.document.getElementById(this.getID()).innerHTML="";
			jsGph.setColor(this.txtColor);
			jsGph.setFont(this.fontName,this.fontSize,Font.PLAIN);
			jsGph.drawString(this.strContent,this.textRect.left,this.textRect.top,this.textRect.width,this.textRect.height,this.hAlign,this.valign,this.txtZoomPercentage);

			jsGph.setColor(this.fgColor);
			jsGph.setStroke(this.stroke);
			jsGph.drawRect(this.boundary.left,this.boundary.top,this.boundary.width,this.boundary.height);
		}
		else
		{
			var strEvent = ' onmouseover="notes[' + this.id + '].onmouseover()"';
			jsGph.drawImage(this.imgSrc,this.boundary.left,this.boundary.top,24,32,strEvent);
		}
		
		jsGph.paint();
	}
}

function ShadowColor(strColor,i)
{
       var nRVal = eval("0x" + strColor.substr(1,2));
		var nGVal = eval("0x" + strColor.substr(3,2));
		var nBVal = eval("0x" + strColor.substr(5,2));
		rgbMax = Math.max(Math.max(nRVal, nGVal), nBVal);
		rgbMin = Math.min(Math.min(nRVal, nGVal), nBVal);
		nLum   = (((rgbMax + rgbMin)*240 + 255)/(2*255));
		
		if(nLum > 80)
				clrOffset = 1.7*(i+1)*(i+1);
			else
				clrOffset = -1.7*(i+1)*(i+1);

			if((nRVal-clrOffset) < 0) newRVal = 0;
			else if((nRVal-clrOffset) >255) newRVal = 255;
			else newRVal = Math.round(nRVal-clrOffset);
			strNewR = newRVal.toString(16);
			if(strNewR.length < 2) strNewR = "0" + strNewR;

			if((nGVal-clrOffset) < 0) newGVal = 0;
			else if((nGVal-clrOffset) >255) newGVal = 255;
			else newGVal = Math.round(nGVal-clrOffset);
			strNewG = newGVal.toString(16);
			if(strNewG.length < 2) strNewG = "0" + strNewG;

			if((nBVal-clrOffset) < 0) newBVal = 0;
			else if((nBVal-clrOffset) >255) newBVal = 255;
			else newBVal = Math.round(nBVal-clrOffset);
			strNewB = newBVal.toString(16);
			if(strNewB.length < 2) strNewB = "0" + strNewB;

			return "#" + strNewR + strNewG + strNewB;

}

function RECT(left,top,width,height)
{
	this.left = left;
	this.top = top;
	this.width = width;
	this.height = height;
	
	this.isPtInRect = function(x,y)
	{
		return (x>=this.left&&x<=this.left+this.width&&y>=this.top&&y<=this.top+this.height);
	}	
}

