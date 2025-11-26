 function formatSelfTestSumMsg(strMsg)
            {
	            	
	            
	            
	            var strResult = "";
				nTotalSteps = GetTakenStepCount();
				
				
	            var nIncorrectSteps = GetFalseStepCount();
	            var nCorrectSteps = nTotalSteps - nIncorrectSteps;
            if (nTotalSteps == 0)
            {
                var nPercent =0;
            }
            else
            {
                var nPercent = parseInt( (nCorrectSteps*100*100)/nTotalSteps )/100;
            }
	            
	            
	            
	            
	            var nIndex = strMsg.indexOf("%s");
            	
            	
            	
            	
	            if(nIndex == -1)
		            return strMsg;
	            var strMsg1 = strMsg.substring(0, nIndex + 2);
	            
	            
	            
	            var strExp = /%s/i;
            	
	            strResult += strMsg1.replace(strExp, nTotalSteps);
	            nIndex = strMsg.indexOf("%s", strMsg1.length);
	            
	            
	            if(nIndex == -1)
	   	return strMsg;
	   	
	            var strMsg2 = strMsg.substring(strMsg1.length, nIndex + 2);
            	
            
            	
	            strResult += strMsg2.replace(strExp, nIncorrectSteps);
            	
            
            
            	
	            var strMsg3 = strMsg.substr(strMsg1.length+strMsg2.length);
            	
	            strResult += strMsg3.replace(strExp, nPercent + "%");
	            
	            
	            	
	            if (nIncorrectSteps > 0)
	            {
strResult = strResult.replace("%MSI%", strInCorrectSteps + GetFalseSteps());
	            }
	            else
	            {
strResult = strResult.replace("%MSI%","");
	            }

	            if (bSendToLMS)
	            {
		            
		            updateSCOUser_GradeInfo(nPercent)
		            	
		            
	            }
	            return strResult;
	            
	            	
            }
            function ShowReport()
            {
	            var strReport = "";
	            nTotalSteps = GetTakenStepCount();
	            var nIncorectSteps = GetFalseStepCount();
	            var nCorrectSteps = nTotalSteps - nIncorectSteps;
            if (nTotalSteps == 0)
            {
                var nPercent =0;
            }
            else
            {
                var nPercent = parseInt( (nCorrectSteps*100*100)/nTotalSteps )/100;
            }
            	
	            var date = new Date();
if (GetFalseStepCount()>0)
		            strReport += 'Inaccurate Step List' + ": " + GetFalseSteps() + "\n";
	            strReport += 'Score' + ": " + nPercent + "\n";
	            strReport += 'END REPORT' + "\n";
            	
	            var TestInfo = new Object();
	            TestInfo.strLessonName = strLessonName;
	            TestInfo.strAuthor = strAuthor;
	            months = new Array("January", "February", "March", "Aprial", "May", "June", "July", "August", "September", "October", "November", "December");
	            days = new Array("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");
	            time = new Date();
	            hours = time.getHours();
	            minutes = time.getMinutes();
	            seconds = time.getSeconds();
	            dates = time.getDate();
	            years = time.getFullYear();
	            day = time.getDay();
	            nameofday = days[day];
	            month = time.getMonth();
	            nameofmonth = months[month];
	           if (hours < 10)
	           {
	               hours ="0" + hours;
	           }
	           if (minutes < 10)
	           {
	               minutes = "0" + minutes;
	           }
	           if (seconds < 10)
	           {
	               seconds ="0" + seconds;
	           }
               TestInfo.strDate = nameofday+", "+dates+" "+nameofmonth+" "+years+" "+hours+":"+minutes+":"+seconds;
TestInfo.TotalTime = parseInt(nTimeLessonCounter/3600) + " h : " + parseInt((nTimeLessonCounter- parseInt(nTimeLessonCounter/3600)*3600)/60) + " m : " + (nTimeLessonCounter%60) + " s.";
	            TestInfo.TotalSteps = nTotalSteps;
	            TestInfo.CorrectSteps = nCorrectSteps;
	            TestInfo.IncorrectSteps = nIncorectSteps;
	            TestInfo.Percentage = nPercent + 'Point';
	            if (nRequiredPercent>0)
		            TestInfo.RequiredPercentage = nRequiredPercent + 'Point';
	            else
		            TestInfo.RequiredPercentage = 0;
	            if (nPercent >= nRequiredPercent)
		            TestInfo.Passed = 'Acceptance'; //passed
	            else
		            TestInfo.Passed = 'Rejection'; //failed
	            TestInfo.Details = arResult;
	            TestInfo.SendReport = nSendReport;
	            var strRet="";
	            try{
	            strRet=window.showModalDialog("showreport.htm", TestInfo, "dialogHeight:450px;dialogWidth:600px;center:yes;help:no;resizable:no;status:no");
	            }
	            catch(e)
	            {}
	            return strRet;
            }

            function GetTakenStepCount()
            {
	            var	nRet = 0;
	            for (var i=0 ; i < arResult.length ; i++ )
	            {
		            if (arResult[i][3] > 0 && arResult[i][5] == true)
			            nRet++;
	            }
	            return nRet;
            }
            function GetFalseStepCount()
            {
	            var	nRet = 0;
	            for (var i=0 ; i < arResult.length ; i++ )
	            {
		            if ( (arResult[i][2] == false) && (arResult[i][3] > 0) && (arResult[i][5] == true))
			            nRet++;
	            }
	            return nRet;
            }


function contentFrameOnLoad(skn,doc)
{
	isEditAction = false;
	window.frames[0].focus ();
	if(isAutoDemonstration())
	{
		showToolbar(skn, doc);
		if(typeof document.VoicePlayer != "undefined")
		{
			if(soundStatus == "on")
				document.VoicePlayer.mute = false;
			else
				document.VoicePlayer.mute = true;
		}
	}
	
		bLoadCompleted = true;
	
	nStepIndex = contentFrame.nStepIndex;
	
	arResult[nStepIndex][4] = strStepName;
	
	scrollBy(scrollX, scrollY);
		setTimeout("setFocusOnEditBox()",1000);
}

function setFocusOnEditBox()
{
try
{
	for(i=0;i<contentFrame.actions.items.length;i++)
	{
		if(contentFrame.actions.items[i].isEdit())
		{
			isEditAction = true;
			if(isIE)
			{
				firstTextbox = contentFrame.document.forms[0].elements[0];
			}else
			{
				var doc = eval ("contentFrame.document."+ contentFrame.actions.items[i].getID() + ".document");
				firstTextbox = doc.forms[0].editAct1;

			}

			try {
				firstTextbox.focus();
			} catch (e) {};
			return;
		}
	}
}
catch (e) {};
}



function showToolbar(skn,doc)
			{
				if (!skn) return;
				
				var content = '<table border="0" width="74" height="20" cellspacing="0" cellpadding="0" bgcolor="#C0C0FF">\n'
								+ '<tr>\n' +
								'<td><img border="0" name="ImgTbMove" onMouseDown = "parent.processMouseDown(event)" src="images/conner_icon.gif" width="12" height="20" style="cursor:hand;"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.startAutoPlay()" src="' + (parent.autoPlaybackStatus == 0 ? 'images/playd.gif':'images/play.gif') + '" name="ImgTbPlay"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.pauseAutoPlay()" src="' + (parent.autoPlaybackStatus == 1 ? 'images/paused.gif':'images/pause.gif') + '" name="ImgTbPause"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.stopAutoPlay()" src="' + (parent.autoPlaybackStatus == 2 ?'images/stopd.gif':'images/stop.gif') + '" name="ImgTbStop"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.onFirstClick()" src="' + (parent.bFirstClicking? 'images/firstd.gif':'images/first.gif') + '" name="ImgTbFirst"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.onPreviousClick()" src="' + (parent.bPreviousClicking? 'images/previousd.gif':'images/previous.gif') + '" name="ImgTbPrevious"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.onNextClick()" src="' + (parent.bNextClicking? 'images/nextd.gif':'images/next.gif') + '" name="ImgTbNext"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.onLastClick()" src="' + (parent.bLastClicking? 'images/lastd.gif':'images/last.gif') + '" name="ImgTbLast"></td>\n'
								+ '<td><img border="0" onMouseDown="parent.onMuteAudioClick()" src="' + (parent.soundStatus=="on"? 'images/soundon.gif':'images/soundoff.gif') + '" name="ImgTbMuteAudio"></td>\n'

								+ '</tr></table>';
				skn.left = sknX;
				skn.top =  sknY;
				mouseStatus = 0;

				if (!isIE)
				{
					if (jg_n4)
					{
						skn.document.write(content);
	    				skn.document.close();
   						skn.visibility = "visible";
					}
					else if (jg_n5)
					{
    					skn.innerHTML = content;
    					skn.style.visibility = "visible";
    				}

				}else {
    				doc.all("topdeck").innerHTML = content;
    				skn.visibility = "visible";
				}

				if (bShowToolbar)
					skn.visibility = "visible";
				else
					skn.visibility = "hidden";
			}
