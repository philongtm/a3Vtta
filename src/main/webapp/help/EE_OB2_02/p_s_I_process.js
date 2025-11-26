function Key()
{
this.KeyDown=KeyDown;
this.cancelEvent=cancelEvent;


        function KeyDown()
        {
	        var e = null;
	        if((typeof event == 'object')&&(event != null))
		        e = event;
	        else
		        e = contentFrame.event;
	        if (e==null)
		        return false;
 var oSource = e.srcElement;
	if(oSource.nodeName.toLowerCase()=="input"&&oSource.type.toLowerCase()=="text")
	isEditAction = true;
	else
	isEditAction = false;
	        if(isEditAction)
	        {
		        switch (e.keyCode)
		        {
		        case 112://F1
		        case 114://F3
		        case 116://F5
		        case 117://F6
		        case 118://F7
		        case 119://F8
		        case 120://F9
		        case 121://F10
		        case 122://F11
		        case 123://F12
			        e.cancelBubble=true;
			        e.returnValue=false;
			        if ( (e.keyCode!=16) && (e.keyCode!=17) )
				        e.keyCode = 0;
			        break;
		        default:
			        break;
		        }
		        return;
	        }
	        var items=contentFrame.actions.items;
	        var  isCorrect=false;
	        var strKey = getKeyOnIE();
        	
	        arResult[nStepIndex][1] = strKey;
	        for(var i=contentFrame.actions.items.length-1;i>-1;i--)
	        {
		        if(items[i].type=="K")
		        {
        			
			        arResult[nStepIndex][0] = items[i].actionParam;
			        
			        if (items[i].isAutoPass == true)
			        {
				        isCorrect = true;
				        goToStep(items[i].nextUrl);
				        return;
			        }
			        else
			        {
                       var strOne=items[i].command.split(",");
				        var strTwo=strKey.split(",");
				        if(CompareString(strOne,strTwo))
				        {
					        isCorrect = true;
					        goToStep(items[i].nextUrl);
					        return;
				        }
			        }
		        }
	        }
	        if((isPractice() || isOperationExercise())&& !isCorrect && !isControlKey(strKey) && (strKey != "[ALT]"))
	        {
		        displayHint(isCorrect, "Key");
	        }
        }

   function CompareString(strOne,strTwo)
   {
       if (strOne[1].indexOf("[16]") != -1
        || strOne[1].indexOf("[17]") != -1
        || strOne[1].indexOf("[18]") != -1)
       {
           var result=false;
           var strOneTemp = strOne[0].split("]");
           var strTwoTemp = strTwo[0].split("]");
           if (strTwoTemp.length != strOneTemp.length + 1)
               return false;
           for (intTmpTwo in strTwoTemp)
           {
               result = false;
               for (intTmpOne in strOneTemp)
               {
                   if (strOneTemp[intTmpOne] == strTwoTemp[intTmpTwo])
                   {
                       result = true;
                   }
               }
               if (strOne[1] == strTwoTemp[intTmpTwo] + "]")
               {
                   result = true;
               }
               if(result==false)
                   return result;
           }
           return true;
       }
       else
       {
           if (strOne[0] != "" && strOne[0] != strTwo[0])
               return false;
           if (strTwo[1] == "" || strOne[1].indexOf(strTwo[1]) == -1)
               return false;
           return true;
       }
   }
        function getKeyOnIE()
        {
	        var strKey = "";
	        var eventObject = null;
        	
	        if((typeof event == 'object')&&(event != null))
		        eventObject = event;
	        else
		        eventObject = contentFrame.event;
        	
	        if(eventObject.ctrlKey)
		        strKey += "[17]";
	        if(eventObject.shiftKey)
		        strKey += "[16]";		
	        if(eventObject.altKey)
		        strKey += "[18]";
		    strKey += ",";
	        if((eventObject.keyCode != 0)&&((eventObject.keyCode > 18)||(eventObject.keyCode<16)))
		        strKey += "["+eventObject.keyCode+"]";
        	
	        eventObject.cancelBubble = true;
	        eventObject.returnValue = false;
	        if ( (eventObject.keyCode !=16) && (eventObject.keyCode!=17) )
		        eventObject.keyCode = 0;
        	
	        return strKey;
        }

        function isControlKey(strKey)
        {
	        return ((strKey=="[16],")||(strKey=="[17],")||(strKey=="[18],")||
		      (strKey=="[17][16],")||(strKey=="[16][18],")||(strKey=="[17][18],")||(strKey=="[17][16][18],"));
        }
        
        function cancelEvent()
        {
        	
	        var eventObject = null;
        	
	        if((typeof event == 'object')&&(event != null))
		        eventObject = event;
	        else
		        eventObject = contentFrame.event;
        		
	        eventObject.returnValue=false;
	        eventObject.cancelBubble=true;
	        if ( (eventObject.keyCode!=17) && (eventObject.keyCode!=16) )
		        eventObject.keyCode = 0;
        }







}


function processMouseDown(e)
{

	if(isIE)
	{
/*
		var contentFrame=window.frames["course"];
		if(typeof contentFrame!='object')	return;
*/

		var x = (!isIE) ? e.pageX : contentFrame.event.clientX + contentFrame.document.body.scrollLeft;
   		var y = (!isIE) ? e.pageY : contentFrame.event.clientY + contentFrame.document.body.scrollTop;
		oldMouseX = x;
		oldMouseY = y;
		mouseStatus = 1;
	}else 
	{
		oldMouseX = mX;
		oldMouseY = mY;
		mouseStatus = 1 - mouseStatus;
	}
}

function processMouseUp()
{
	if(isIE){ mouseStatus = 0;}
}



function processMouseMove(e)
{

	var x = (!isIE) ? e.pageX : contentFrame.event.clientX + contentFrame.document.body.scrollLeft;
	var y = (!isIE) ? e.pageY : contentFrame.event.clientY + contentFrame.document.body.scrollTop;

	if(mouseStatus == 1)
	{
		contentFrame.skn.left = parseInt(contentFrame.skn.left) + x - oldMouseX;
		contentFrame.skn.top = parseInt(contentFrame.skn.top) + y - oldMouseY;
		sknX = parseInt(contentFrame.skn.left);
		sknY = parseInt(contentFrame.skn.top);
		oldMouseX = x;
		oldMouseY = y;
	}
	mX = x;
	mY = y;
}


function Mouse()
{
this.MouseDown=MouseDown;
this.MouseUp=MouseUp;
this.MouseClick=MouseClick;
this.MouseDouble=MouseDouble;

            function MouseDouble(nsEvent)
            {
	            isEditAction = false;
	            if(havetoWait==true)
		            return;

	            var items=contentFrame.actions.items;
	            var i, isCorrect=false;
	            if((i=getEqualAction(items, nsEvent))!=-1)
	            {
            		
		            if(items[i].type=="M"&&items[i].actionParam.indexOf("Double")!=-1)
		            {
            		
			            if(isIE){//IE
				            goToStep(items[i].nextUrl);
			            }else{//Netscape
				            goToStep(items[i].nextUrl);
			            }
			            isCorrect=true;
		            }
	            }
            }


            function MouseClick(nsEvent)
            {
            	
	            isEditAction = false;
	            if(havetoWait==true)
		            return;
	            isMouseHolding = true;

	            var items=contentFrame.actions.items;
	            var i, isCorrect=false;
	            if((i=getEqualAction(items, nsEvent))!=-1)
	            {
            	
            	
		            if(items[i].type=="M"&&items[i].actionParam.indexOf("Click")!=-1)
		            {
            			
			            compareMouse(items[i]);
		            }
	            }
            }

        
        
        function MouseDown(nsEvent)
        {
	        document.oncontextmenu = new Function("return false");
        	
   parent.out=false;
		contentFrame.event.cancelBubble=true;
	        isEditAction = false;
	        if(havetoWait==true)
		        return;
	        isMouseHolding = true;


	        var items=contentFrame.actions.items;
	        var i, isCorrect=false;
	        if((i=getEqualAction(items, nsEvent))!=-1)
	        {
        		
		        if ((items[i].type=="M"  &&items[i].actionParam.indexOf("Down")!=-1))
		        {
        			compareMouse(items[i]);
			        
		        }
		        else if(items[i].type=="M"  &&items[i].actionParam.indexOf("Click")!=-1)
		        {
        			
			        compareMouse(items[i]);
		        }
		        
		        if(items[i].type=="B")
		        {
		                    isCorrect=true;
		                    arResult[nStepIndex][1]="button";
					        goToStep(items[i].nextUrl);
		        }
	        }
        }
        
        
        function compareMouse(items)
        {
            
            if(isIE)
			        {
				        if(compareMouseCommandIE(items.command,contentFrame))
				        {
					        isCorrect=true;
					        goToStep(items.nextUrl);
				        }
			        }
			        else
			        {
				        if(compareMouseCommandNS(items.command,nsEvent))
				        {
					        isCorrect=true;
					        goToStep(items.nextUrl);
				        }
			        }
        
        }
        
         function MouseUp(nsEvent)
        {
	        isEditAction = false;
	        if(havetoWait==true)
		        return;

	        var items=contentFrame.actions.items;
	        var i, isCorrect=false;
	        if((i=getEqualAction(items, nsEvent))!=-1)
	        {
        		
        	
		        if(items[i].type=="M"  &&items[i].actionParam.indexOf("Up")!=-1)
		        {
        			
			        compareMouse(items[i]);
		        }
	        }

	        if(bClickOnIcon==false)
	        {
		        if((isOperationExercise() || isPractice()) && isMouseHolding)
		        {
			        var actType = getMouseOnAction(items, nsEvent);
			        if(isIE && contentFrame.event.srcElement.tagName == "INPUT")
				        actType = "Edit";
			        if(actType != "DoubleMouse" && actType != "Edit")
			        {
				        displayHint(isCorrect, "Mouse");
			        }
		        }
	        }
	        else
		        bClickOnIcon = false;
        }




        function getEqualAction(actions, event)
        {
	        if(isIE)
	        {
		        for(var i=actions.length-1;i>-1;i--)
		        {
			        if((actions[i].rect)&&(!actions[i].isWait()))
			        {
				        if(actions[i].rect.contains(contentFrame.event.clientX+contentFrame.document.body.scrollLeft,
					        contentFrame.event.clientY + contentFrame.document.body.scrollTop))
					        return i;
			        }
		        }
	        }
	        else
	        {
		        for(var i=actions.length-1;i>-1;i--)
		        {
			        if(actions[i].rect)
			        {
				        if(actions[i].rect.contains(event.pageX,event.pageY))
					        return i;
			        }
		        }
	        }
	        return -1;
        }

        function compareMouseCommandIE(strCommand, win)
        {
	        var strKey = "";
	        var strTemp1 = "";
	        if (win.event.ctrlKey) strKey += "[17]";
	        if (win.event.altKey) strKey += "[18]";
	        if (win.event.shiftKey) strKey += "[16]";
        	
	        strTemp1.replace([16], "[Shift]");
	        strTemp1.replace([17], "[Control]");
	        strTemp1.replace([18], "[Alt]");

	        if (win.event.button == 5 ||win.event.button==1)
	        {
		        strKey += "[1]";
		        strTemp1 += "[left Click]";
	        }
	        else if (win.event.button == 6 ||win.event.button == 2)
	        {
		        strKey += "[2]";
		        strTemp1 += "[Right Click]";
	        }
        	
	        arResult[nStepIndex][1] = strTemp1;

	        if (strKey == strCommand) return true;
	        return false;
        }

        function compareMouseCommandNS(strCommand, event)
        {
	        var strKey = "";
	        switch(event.modifiers) {
		        case 1:	strKey += "[18]"; break;
		        case 2:	strKey += "[17]"; break;
		        case 3:	strKey += "[17][18]"; break;
		        case 4:	strKey += "[16]"; break;
		        case 5:	strKey += "[18][16]"; break;
		        case 6:	strKey += "[17][16]"; break;
		        case 7:	strKey += "[17][18][16]"; break;
	        }
	        if (event.which == 1){
		        strKey += "[1]";
	        }else {
		        strKey += "[2]";
	        }
	        if (strKey == strCommand) return true;
	        return false;
        }
    
        

        function getMouseOnAction(actions, event)
        {
	        var i = getEqualAction(actions, event);
        	
	        if(i!=-1&&actions[i].actionParam.indexOf("Double")!=-1)
		        return "DoubleMouse";
	        if(i!=-1&&actions[i].isEdit())
		        return "Edit";
        		
	        return "";
        }
    
}



function keyPressForEdit(sender, actionItem)
{
	isEditAction = true;

	var e = null;
	if((typeof event == 'object')&&(event != null))
		e = event;
	else
		e = contentFrame.event;
	if (e==null)
		return;
		
	if ( e.type!="keyup" )
	{
		var bCancel = false;
		switch (e.keyCode)
		{
			case 112://F1
			case 114://F3
			case 116://F5
			case 117://F6
			case 118://F7
			case 119://F8
			case 120://F9
			case 121://F10
			case 122://F11
			case 123://F12
				bCancel=true;
				break;
			default:
				bCancel=false;
		}
		if (e.type=="help")
			bCancel=true;
		if (bCancel)
		{
			e.cancelBubble=true;
			e.returnValue=false;
			if ( (e.keyCode!=16) && (e.keyCode!=17) )
				e.keyCode = 0;
			return;
		}
		return;
	}
	
	
	



    if(!(actionItem.strSwitch == "JP"&&contentFrame.event.keyCode != 13))
	    {


	    if(typeof(contentFrame) == "undefined")
		    return;
	    var items=contentFrame.actions.items;
	    var  isCorrect=false;

	    arResult[nStepIndex][1] = sender.value;



	    for(var i=contentFrame.actions.items.length-1;i>-1;i--)
	    {
		    if(items[i].type=="E")
		    {
			    arResult[nStepIndex][0] = items[i].actionParam;
    			
			    if(!isAutoDemonstration())
			    {
				    if (items[i].isAutoPass)
				    {
    					
					    isCorrect = true;
					    arResult[nStepIndex][4] = "Autopass Action";
					    goToStep(items[i].nextUrl);
					    return;
				    }
			    }
			    else
			    {
				    break;
			    }
		    }
	    }
    	


    	
	        if(!isAutoDemonstration())
	        {

        	if((actionItem.isEditOK(sender.value) == true))
		        {
        			
					        goToStep(actionItem.getNextStep(sender.value));
        				
		        }
        	
        	
        		
		          if( actionItem.isEditPartialOK(sender.value) == false )
		        {
			        var	strHelpMsg = actionItem.command;
			        re = /<actionbegin>s(_(\d)*)*\.htm<actionend>/g;
			        strHelpMsg = strHelpMsg.replace(re, "");
			        re = /<textend>,<textbegin>/g;
				        strHelpMsg = strHelpMsg.replace(re, "\nor\n\t");
				        strHelpMsg = "Correct answers when one of the following was entered.\n\t" + strHelpMsg;
			        re = /<textbegin>/gi;
			        strHelpMsg = strHelpMsg.replace(re, "");
			        re = /<textend>/gi;
			        strHelpMsg = strHelpMsg.replace(re, "");

			        strCommand = strHelpMsg;
			        blnHintVisible=false;
			        if(isOperationExercise() || isPractice())
			        {
				        displayHint(false, "Edit");
			        }
			        sender.value = actionItem.oldCmdValue;
		        }
		        
        		
        		actionItem.oldCmdValue = sender.value;
        		
            }
    		
    		
    		
    		
    		
	    }
	
	
		if(actionItem.strSwitch == "JP"&&contentFrame.event.keyCode != 13)
			{
			  actionItem.oldCmdValue = "";
		 if( actionItem.isEditPartialOK(sender.value) == true )
						 {
			  actionItem.oldCmdValue = sender.value;
						}
		}
	
	
	
	return;
}


function startAutoPlay()
{
	if(autoPlaybackStatus == 1)
	{//Pausing
		autoPlaybackStatus = 0;
		goNextStep();
	}
	else if(autoPlaybackStatus == 2)
	{//Stoping
		autoPlaybackStatus = 0;
		goFirstStep();
	}
}

function stopAutoPlay()
{
	
		autoPlaybackStatus = 2;

		contentFrame.document.images["ImgTbPause"].src = "images/pause.gif";
		contentFrame.document.images["ImgTbPlay"].src = "images/play.gif";
		contentFrame.document.images["ImgTbStop"].src = "images/stopd.gif";
		contentFrame.stopAudio();
		contentFrame.stopAutoDemonstration();
	}



	function pauseAutoPlay()
	{
	    contentFrame.gone = true;
		autoPlaybackStatus = 1;

		contentFrame.document.images["ImgTbStop"].src = "images/stop.gif";
		contentFrame.document.images["ImgTbPlay"].src = "images/play.gif";
		contentFrame.document.images["ImgTbPause"].src = "images/paused.gif";
		contentFrame.pauseAudio();
	}


function onFirstClick()
{

	bFirstClicking = true;
	contentFrame.document.images["ImgTbFirst"].src = "images/firstd.gif";
	goFirstStep();
}

function onPreviousClick()
{

	if(typeof contentFrame.strNextWaitPageUrl!='string')
		return;
	bPreviousClicking = true;
	contentFrame.document.images["ImgTbPrevious"].src = "images/previousd.gif";
	if((contentFrame.strPreUrl=="")||(contentFrame.strPreUrl==activePageURL))
		return
	goToStep(contentFrame.strPreUrl);
}

function onNextClick()
{

	bNextClicking = true;
	contentFrame.document.images["ImgTbNext"].src = "images/nextd.gif";
	goNextStep();
}


function onLastClick()
{

	if(typeof contentFrame.strNextWaitPageUrl!='string')
		return;
	bLastClicking = true;
	contentFrame.document.images["ImgTbLast"].src = "images/lastd.gif";
	goToStep(strLastPage);
}

function onMuteAudioClick()
{

	muteAudio();
}


