

document.oncontextmenu=new Function("return false");




if(document.layers)
{
	var str ="";
	
	for(i=0;i<actions.items.length;i++)
	{
		if(!actions.items[i].isMouse())
			continue;
		str+=actions.items[i].rect.lTopDownLine.getNSProp();
		str+=actions.items[i].rect.rTopDownLine.getNSProp();
		str+=actions.items[i].rect.tLeftRightLine.getNSProp();
		str+=actions.items[i].rect.bLeftRightLine.getNSProp();
	}
	if(str!="") 
		eval(str);
}

if(document.layers)
{	document.DivBgLeft.clip.width = 209;
	document.DivBgLeft.clip.height = 768;
	document.DivBgTop.clip.width = 674;
	document.DivBgTop.clip.height = 30;
	document.DivBgRight.clip.width = 350;
	document.DivBgRight.clip.height = 768;
	document.DivBgBottom.clip.width = 465;
	document.DivBgBottom.clip.height = 279;
}
