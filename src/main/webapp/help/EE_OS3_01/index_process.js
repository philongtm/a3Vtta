function initializeScorm()
{
try{
	if (bSendToLMS)	loadPage();
}catch(e)
{}
}

function unloadScorm()
{
try{
	if (bSendToLMS)	unloadPage();
}catch(e)
{}
}

function openPlayback(url)
{

try{
var result;
result=checkMedia();
if(!result)
	{ 
	if(result != 2)
			return;
	
	}
	
	
	if(typeof strAutoStepWindowPos != 'undefined')
		url += "&stepFramePositionInAutoDemonstration=" + strAutoStepWindowPos;
	
	if(typeof strStandardStepWindowPos != 'undefined')
		url += "&stepFramePositionInOperationLecture=" + strStandardStepWindowPos;
		
	if(typeof stepFrameRatioInAuto != 'undefined')
		url += "&stepFrameRatioInAuto=" + stepFrameRatioInAuto;		
		
	if(typeof stepFrameRatioInLecture != 'undefined')
		url += "&stepFrameRatioInLecture=" + stepFrameRatioInLecture;		
				
	url = encodeURI(url);
	//window.open(url,null,'width='+screen.availWidth+',height='+screen.availHeight+',top=0,left=0,status=yes,scrollbars=yes,resizable=yes');

	NewWin = window.open(url,null,'status=yes,scrollbars=yes,resizable=yes');
	NewWin.moveTo(0,0);
	NewWin.resizeTo(screen.availWidth,screen.availHeight); 




}catch(e)
{}
}





