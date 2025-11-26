try{
if (parent.isAutoDemonstration() || parent.isAllWait(actions.items)) 
	{
		if (window.parent.isIE)
			skn = document.getElementById('topdeck').style;
		else if (window.parent.jg_n4 || window.parent.jg_n5)
			skn = document.getElementById('topdeck');
		if(parent.autoPlaybackStatus == 0) 
			iIntervalID = setInterval("waitToGoNext(strNextWaitPageUrl)",AdvanceTime);
	}
parent.contentFrameOnLoad(skn,document);


function waitToGoNext(strUrl)
{
	
	if(gone) return;
	setTimeout("window.parent.goNextStep();", 20);
	gone = true;
}
function StartBlink()
{
	for (i=0; i<actions.items.length; i++)
	{
		if ( actions.items[i].isMouse() || actions.items[i].isMouse())
		{
			for (n=0; n<4; n++)
			{
				Visible = document.getElementById(actions.items[i].getID()[n]).style.visibility;
				if ( (Visible == "") || (Visible == "hidden") )
				{
					Visible = "visible";
				}
				else
					Visible = "hidden";
				document.getElementById(actions.items[i].rect.getID()[n]).style.visibility = Visible;
			}
		}
	}
}
if ( !parent.isPractice() && !parent.isOperationExercise() )
	window.setInterval("StartBlink();", 500);
}
catch(e)
{}
