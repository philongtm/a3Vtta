	
	if (!parent.isAutoDemonstration() && !parent.isAllWait(actions.items)) 
	{
		if (document.layers) 
			document.captureEvents(Event.CLICK|Event.DBLCLICK|Event.MOUSEDOWN|Event.MOUSEUP);
		
		var mouse=new parent.Mouse();
		document.onmousedown=mouse.MouseDown;
		document.onmouseup=mouse.MouseUp;
		document.onclick=mouse.MouseClick;
		document.ondblclick=mouse.MouseDouble;
	    
	    var key=new parent.Key();
	    document.onkeydown=key.KeyDown;
		document.onkeyup=key.cancelEvent;
		document.onhelp=key.cancelEvent;
	}
	else 
	{ 
		if (document.layers) 
			document.captureEvents(Event.MOUSEMOVE);
		else 
			document.onmouseup=parent.processMouseUp;
		document.onmousemove = parent.processMouseMove;
	}
