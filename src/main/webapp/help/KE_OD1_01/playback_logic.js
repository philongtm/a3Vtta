function displayHint(isCorrect, strActionType)
{
	if (isCorrect)
		return;

	arResult[nStepIndex][3] += 1;
	if(isPractice())
	{
		var strConfirmMsg = parent.strTeachMeMsg1;
		strConfirmMsg = strConfirmMsg.replace("[nTimes]", arResult[nStepIndex][3]);
		if(frames(0).boolPointCount == false)
		{
			    return;
		}
		else
		{
		    if ( (arResult[nStepIndex][3] >= nMaxTries) && (confirm(strConfirmMsg)) )
		    {
			    operatingMode = "lecture";
			    frameStructure = getFrameStructure(stepFramePositionInOperationLecture,false);
			    if(stepFramePositionInOperationLecture != "Right")
				    parentFrame.rows = frameStructure;
			    else
				    parentFrame.cols = frameStructure;
			    if(stepFramePositionInOperationLecture != "None")
			    {
				    top.document.getElementById("stepFrame").noResize = false;
				    stepFrame.document.location = "stepContainer.htm";
			    }
			    setVisibleHint(true);
			    contentFrame.location.reload();
			    return;
		    }
		}
	}
	if(isOperationExercise())
	{
		if((strActionType=="Mouse" ) && blnHintVisible)
			return;
	    if(frames(0).boolPointCount != null)
	     {
	    	    if(frames(0).boolPointCount == false)
	    	    {
	    	    	curStepPassed = false;
	        		blnShownHint = true;
	        		setVisibleHint(true, strActionType);
			        return;
	        	}
	      }
	    	    if (confirm(strSelfTestHelpMsg) )
	    	    {
	    	    	curStepPassed = false;
	        		blnShownHint = true;
	        		setVisibleHint(true, strActionType);
	        		parent.contentFrame.DoBlink();
	        	}
            	else
               	{
	                intStepClick++;
   	               if((intStepClick > 0) && (0 == 1))
	                {
	    	    	    curStepPassed = false;
    	            }
              }
   }
}

function setVisibleHint(isVisible, strActionType)
{
	var prop=isVisible?"visible":"hidden";

	var hints=contentFrame.divs;
	
	if(typeof hints=='undefined')	return;
	blnHintVisible=isVisible;
	for(var i=0;i<hints.length;i++)
	{
		if(typeof hints[i] == "object")
			hints[i].visibility = prop;
		else
			contentFrame.document.getElementById(hints[i]).style.visibility = prop;
	}

	if(typeof(strActionType) == "undefined")
		return;
	if(!isOperationExercise())
		return;
	if((strActionType=="Edit") && isEditAction ) // Display help msg for edit action
	{
		alert(strCommand);
		return;
	}
	if(typeof contentFrame.keyhelp=='undefined') return;
	var keyhelp=contentFrame.keyhelp;
	var strHelp="";
	var i;
	if ( (strActionType=="Key") && (keyhelp.length>0) )
	{
       strHelp = "キーを押す : \n\n\t" + keyhelp[0];
		if (keyhelp.length>1)
		{
			for(i=1;i<keyhelp.length;i++)
			{
				strHelp+="\n\t"+keyhelp[i];
			}
		}
		alert(strHelp);
	}
	return;
}

function endLesson()
{
   if (bSendToLMS)
   {
       commitData();
   }

   if( mode_config == 0 || (mode_config == 1 && !parent.isOperationExercise()))
   {
       window.close();
   }
}

function uncheckDoneAll()
{
   ResetResult();
   if(getStepFramePosition() != "None")
   {
       setUndoneAll();
       if(!isIE)
       {
           treeMenuDisplay();
       }
       else
       {
           setTimeout("treeMenuDisplay()", 10);
       }
   }
}

function EmailReport1Admin()
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
   var strUserName = null;
   var strHeader = "";
   var date = new Date();
   var TestInfo = new Object();
   TestInfo.UILanguage = UILanguage;
   TestInfo.strLessonName = strLessonName;
   TestInfo.strAuthor = strAuthor;
               TestInfo.strDate = nameofday+", "+dates+" "+nameofmonth+" "+years+" "+hours+":"+minutes+":"+seconds;
   TestInfo.TotalTime = parseInt(nTimeLessonCounter/3600) + " h : " + parseInt(nTimeLessonCounter/60) + " m : " + (nTimeLessonCounter%60) + " s.";
   TestInfo.TotalSteps = nTotalSteps;
   TestInfo.CorrectSteps = nCorrectSteps;
   TestInfo.IncorrectSteps = nIncorectSteps;
   TestInfo.Percentage = nPercent + '点';
   TestInfo.RequiredPercentage = nRequiredPercent + '点';
   if (nPercent >= nRequiredPercent)
       TestInfo.Passed = '合格'; //passed
   else
       TestInfo.Passed = '不合格'; //failed
   TestInfo.Details = arResult;
   TestInfo.SendReport = nSendReport;
   TestInfo.strTo = strReportEmail;
   TestInfo.strSubject = strLessonName + "-" + 'レポート';

var CorrectStepCount=0;         //正解数
var IncorrectStepCount=0;       //不正解数
var Percentage = 0;             //得点
var WhetherPassed=false         // whether 合格
var Passed;                 // whether 合格
for (var i=0; i < TestInfo.Details.length; i++)
{
   if(TestInfo.Details[i][5]==true)
       {
            if(TestInfo.Details[i][2]==true)
               {
                    CorrectStepCount++;
               }
            else  
               {
                   IncorrectStepCount++;
               }
        }
}
if(CorrectStepCount!=0||IncorrectStepCount!=0)
{
   Percentage=parseInt((CorrectStepCount*100*100)/(CorrectStepCount+IncorrectStepCount))/100;
}
try
{
   if(Percentage>=parseInt(nRequiredPercent))
       {
            Passed='合格'; //passed
        }
    else  
        {
             Passed='不合格'; //failed
        }
}
catch(e)
{
    Passed='不合格'; //failed
}
 TestInfo.CorrectSteps = CorrectStepCount;
 TestInfo.IncorrectSteps = IncorrectStepCount;
 TestInfo.Percentage = Percentage + '点';
 TestInfo.Passed = Passed ;
   strReport = 'BEGIN REPORT' + "%0D%0A";
   strReport += '日付' + ": " + TestInfo.strDate + "%0D%0A";
   strReport += 'レッスン名' + ": " + strMailLessonName + "%0D%0A";
   strReport += '作者' + ": " + strMailAuthor + "%0D%0A";
   strReport += '実施時間' + ": " + TestInfo.TotalTime + "%0D%0A";
   strReport += '設問数' + ": " + parseInt(TestInfo.CorrectSteps+TestInfo.IncorrectSteps) + "%0D%0A";
   strReport += '正解数' + ": " + TestInfo.CorrectSteps + "%0D%0A";
   strReport += '不正解数' + ": " + TestInfo.IncorrectSteps + "%0D%0A";
   if (GetFalseStepCount()>0)
       strReport += '不正解のステップリスト' + ": " + GetFalseSteps() + "%0D%0A";
   strReport += '得点' + ": " + TestInfo.Percentage.replace(/%/g,"persent") + "%0D%0A";
   if (nRequiredPercent>0)
       strReport += '合格ライン' + ": " + TestInfo.RequiredPercentage + "%0D%0A";
   strReport += "%0D%0A" + '詳細' + ":%0D%0A";

   var strDetailsHTML="";
   strDetailsHTML += 'ステップID' + "%09" + '試行数' + "%09" + '判定';
   for (var i=0; i < TestInfo.Details.length; i++)
   {
       if (TestInfo.Details[i][4] != "")
           strDetailsHTML += "%0D%0A" + 
       TestInfo.Details[i][4] + "%09%09" + 
       (((TestInfo.Details[i][6]==true)||(TestInfo.Details[i][5]==false))? '-' : TestInfo.Details[i][3])  + "%09%09" + 
       ((TestInfo.Details[i][5]==true) ?(TestInfo.Details[i][2]==true?'正解':'不正解'): '-');
   }
   strReport += strDetailsHTML;
   strReport += "%0D%0A" + 'END REPORT';

   var strSignature="";
   TestInfo.strContent = strReport + strSignature;
   var strRet=window.showModalDialog("savereport.htm", TestInfo, "dialogHeight:300px;dialogWidth:400px;center:yes;help:no;resizable:no;status:no");
   if (strRet=="Continue")
   {
       strEncoding = "encoding=\"shift_jis\"";
       strHeader = "<?xml version=\"1.0\"?>\n";
       strHeader += "<!-- This file was generated automatically by DOJO-DHTML\n";
       strHeader += "DO NOT EDIT MANUALLY OR IT WILL BE CORRUPTED -->\n";
       strHeader += "<Content>\n";
       strReport = "<Report>\n"
       strReport += "<Encoding>" + strEncoding + "</Encoding>\n";
       strReport += "<Date>" + TestInfo.strDate + "</Date>\n";
       strReport += "<LessonName>" + TestInfo.strLessonName + "</LessonName>\n";
       strReport += "<LessonAuthor>" + TestInfo.strAuthor + "</LessonAuthor>\n";
       strReport += "<TotalTime>" + TestInfo.TotalTime + "</TotalTime>\n";
       strReport += "<TotalSteps>" + parseInt(TestInfo.CorrectSteps+TestInfo.IncorrectSteps) + "</TotalSteps>\n";
       strReport += "<CorrectSteps>" + TestInfo.CorrectSteps + "</CorrectSteps>\n";
       strReport += "<IncorrectStepList>" + GetFalseSteps() + "</IncorrectStepList>\n";
       strReport += "<Percentage>" + Percentage + "</Percentage>\n";
       if (nRequiredPercent>0)
           strReport += "<RequiredPercentage>" + nRequiredPercent + "</RequiredPercentage>\n";

       var strDetails = "";
       for (var i=0; i < TestInfo.Details.length; i++)
       {
           if (TestInfo.Details[i][4] != "")
               strDetails += "<Step><StepID>"+TestInfo.Details[i][4]+"</StepID>"+"<StepTries>"+  (((TestInfo.Details[i][6]==true)||(TestInfo.Details[i][5]==false))? '-' : TestInfo.Details[i][3])+"</StepTries>"+"<StepResult>"+((TestInfo.Details[i][5]==false)?'-':TestInfo.Details[i][2])+"</StepResult></Step>\n";
       }
       strReport += "<Details>" + strDetails + "</Details>\n";

       strReport += "</Report>\n";
       strSignature = "\n<Signature>" + calcMD5(strReport) + "</Signature>\n";
       var repWin = window.open("", '_blank', 'width=100,height=100,top=100,left=100,titlebar=0,toolbar=0,menubar=0,location=0,visibility=0', true);
       var repDoc = repWin.document;
       var strContent = strReport +strSignature + "</Content>\n";

       repDoc.write(strHeader+strContent);
       if ( !repDoc.execCommand("SaveAs",true, "DOJO-" + strLessonName +  ".xml") )
       {
           repWin.close();
           return;
       }
       repWin.close();
       var strMailTo = "mailto:" + strMailReportEmail + "?subject=" + strMailLessonName + "-" + 'レポート' + "&body=" + TestInfo.strContent;
       window.open(strMailTo);

   }
   return;
}
function EmailReport2Admin()
{

   if( mode_config == 0 )
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
       var strUserName = null;
       var strHeader = "";
       var date = new Date();
       var TestInfo = new Object();
       TestInfo.UILanguage = UILanguage;
       TestInfo.strLessonName = strLessonName;
       TestInfo.strAuthor = strAuthor;
               TestInfo.strDate = nameofday+", "+dates+" "+nameofmonth+" "+years+" "+hours+":"+minutes+":"+seconds;
       TestInfo.TotalTime = parseInt(nTimeLessonCounter/3600) + " h : " + parseInt(nTimeLessonCounter/60) + " m : " + (nTimeLessonCounter%60) + " s.";
       TestInfo.TotalSteps = nTotalSteps;
       TestInfo.CorrectSteps = nCorrectSteps;
       TestInfo.IncorrectSteps = nIncorectSteps;
       TestInfo.Percentage = nPercent + '点';
       TestInfo.RequiredPercentage = nRequiredPercent + '点';
       if (nPercent >= nRequiredPercent)
           TestInfo.Passed = '合格'; //passed
       else
           TestInfo.Passed = '不合格'; //failed
       TestInfo.Details = arResult;
       TestInfo.SendReport = nSendReport;
       TestInfo.strTo = strReportEmail;
       TestInfo.strSubject = strLessonName + "-" + 'レポート';

var CorrectStepCount=0;         //正解数
var IncorrectStepCount=0;       //不正解数
var Percentage = 0;             //得点
var WhetherPassed=false         // whether 合格
var Passed;                 // whether 合格
for (var i=0; i < TestInfo.Details.length; i++)
{
   if(TestInfo.Details[i][5]==true)
       {
            if(TestInfo.Details[i][2]==true)
               {
                    CorrectStepCount++;
               }
            else  
               {
                   IncorrectStepCount++;
               }
        }
}
if(CorrectStepCount!=0||IncorrectStepCount!=0)
{
   Percentage=parseInt((CorrectStepCount*100*100)/(CorrectStepCount+IncorrectStepCount))/100;
}
try
{
   if(Percentage>=parseInt(nRequiredPercent))
       {
            Passed='合格'; //passed
        }
    else  
        {
             Passed= '不合格'; //failed
        }
}
catch(e)
{
    Passed= '不合格'; //failed
}
 TestInfo.CorrectSteps = CorrectStepCount;
 TestInfo.IncorrectSteps = IncorrectStepCount;
 TestInfo.Percentage = Percentage + '点';
 TestInfo.Passed = Passed;
       strReport = 'BEGIN REPORT' + "%0D%0A";
       strReport += '日付' + ": " + TestInfo.strDate + "%0D%0A";
       strReport += 'レッスン名' + ": " + strMailLessonName + "%0D%0A";
       strReport += '作者' + ": " + strMailAuthor + "%0D%0A";
       strReport += '実施時間' + ": " + TestInfo.TotalTime + "%0D%0A";
       strReport += '設問数' + ": " + parseInt(TestInfo.CorrectSteps+TestInfo.IncorrectSteps) + "%0D%0A";
       strReport += '正解数' + ": " + TestInfo.CorrectSteps + "%0D%0A";
       strReport += '不正解数' + ": " + TestInfo.IncorrectSteps + "%0D%0A";
       if (GetFalseStepCount()>0)
           strReport += '不正解のステップリスト' + ": " + GetFalseSteps() + "%0D%0A";
       strReport += '得点' + ": " + TestInfo.Percentage.replace(/%/g,"persent") + "%0D%0A";
       if (nRequiredPercent>0)
           strReport += '合格ライン' + ": " + TestInfo.RequiredPercentage + "%0D%0A";
       strReport += "%0D%0A" + '詳細' + ":%0D%0A";

       var strDetailsHTML="";
       strDetailsHTML += 'ステップID' + "%09" + '試行数' + "%09" + '判定';
       for (var i=0; i < TestInfo.Details.length; i++)
       {
           if (TestInfo.Details[i][4] != "")
               strDetailsHTML += "%0D%0A" + 
               TestInfo.Details[i][4] + "%09%09" + 
               (((TestInfo.Details[i][6]==true)||(TestInfo.Details[i][5]==false)) ? '-' : TestInfo.Details[i][3]) + "%09%09" + 
               ((TestInfo.Details[i][5]==true) ?(TestInfo.Details[i][2]==true?'正解':'不正解'): '-');
       }
       strReport += strDetailsHTML;
       strReport += "%0D%0A" + 'END REPORT';

       var strSignature="";
       TestInfo.strContent = strReport + strSignature;
       var strMailTo = "mailto:" + strMailReportEmail + "?subject=" + strMailLessonName + "-" + 'レポート' + "&body=" + TestInfo.strContent;
       window.open(strMailTo);
   }
   else if( mode_config == 1 ) 
   {
       var langPercentMark = "点";
       var langReport = "レポート";

       if (nSendReport == 2)
           return SaveReport2XMLFile();
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
       var strUserName = null;
       var strHeader = "";
       var date = new Date();
       var TestInfo = new Object();
       TestInfo.UILanguage = UILanguage;
       TestInfo.strLessonName = strLessonName;
       TestInfo.strAuthor = strAuthor;
       TestInfo.strDate = date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
       TestInfo.TotalTime = parseInt(nTimeLessonCounter/3600) + " h : " + parseInt(nTimeLessonCounter/60) + " m : " + (nTimeLessonCounter%60) + " s.";
       TestInfo.TotalSteps = nTotalSteps;
       TestInfo.CorrectSteps = nCorrectSteps;
       TestInfo.IncorrectSteps = nIncorectSteps;
       TestInfo.Percentage = nPercent + langPercentMark;
       TestInfo.RequiredPercentage = nRequiredPercent + langPercentMark;
       if (nPercent >= nRequiredPercent)
           TestInfo.Passed = "Passed";
       else
           TestInfo.Passed = "Failed";

       TestInfo.Details = arResult;
       TestInfo.SendReport = nSendReport;
       TestInfo.strTo = strReportEmail;
       TestInfo.strSubject = strLessonName + "-" + langReport;

var CorrectStepCount=0;         //正解数
var IncorrectStepCount=0;       //不正解数
var Percentage = 0;             //得点
var WhetherPassed=false         // whether 合格
var Passed;                 // whether 合格
for (var i=0; i < TestInfo.Details.length; i++)
{
   if(TestInfo.Details[i][5]==true)
       {
            if(TestInfo.Details[i][2]==true)
               {
                    CorrectStepCount++;
               }
            else  
               {
                   IncorrectStepCount++;
               }
        }
}
if(CorrectStepCount!=0||IncorrectStepCount!=0)
{
   Percentage=parseInt((CorrectStepCount*100*100)/(CorrectStepCount+IncorrectStepCount))/100;
}
try
{
   if(Percentage>=parseInt(nRequiredPercent))
       {
            Passed='合格'; //passed
        }
    else  
        {
             Passed= '不合格'; //failed
        }
}
catch(e)
{
    Passed= '不合格'; //failed
}
 TestInfo.CorrectSteps = CorrectStepCount;
 TestInfo.IncorrectSteps = IncorrectStepCount;
 TestInfo.Percentage = Percentage + '点';
 TestInfo.Passed = Passed;
 TestInfo.TotalSteps =parseInt(CorrectStepCount+IncorrectStepCount) ;
       url=scorebook_url;
       url+="contents_code=4";                           
       url+="&lesson="+ TestInfo.strLessonName;		　　
       url+="&user=" + TestInfo.strAuthor;				
       url+="&test_date=" + TestInfo.strDate;			
       url+="&jishi_time=" + TestInfo.TotalTime;			
       url+="&mondai_su=" + TestInfo.TotalSteps;			
       url+="&seikai_su=" + TestInfo.CorrectSteps;		
       url+="&fuseikai_su=" + TestInfo.IncorrectSteps;	
       url+="&tokuten=" + nPercent;						
       url+="&judge=" + TestInfo.Passed;					

       var reportDetails="";
       for (var i=0; i < TestInfo.Details.length; i++)
       {
           if (TestInfo.Details[i][4] != "")
               reportDetails +=  
               TestInfo.Details[i][4] + "," + 
               TestInfo.Details[i][3] + "," + 
               (TestInfo.Details[i][5]==true?"1":"0") + "," + 
               (TestInfo.Details[i][2]==true?"Correct":"Not Correct")
               + ".";
       }

       url+="&reportDetails=" + reportDetails;

       window.location.href = url;

   }

   return;
}

function GetFalseSteps()
{
   var	strTemp = "";
   for(var i=0; i<arResult.length; i++)
   {
       if (arResult[i][2] == false)
       {
            if ( arResult[i][3] > 0 &&arResult[i][5]==true)
           {
               if (strTemp != "") strTemp += ", ";
                   strTemp += "[" + arResult[i][4] + "]";
           }
       }
   }
   return strTemp;
}

/*
Function : updateSCOUser_GradeInfo
Purpose  :
   Update user's performance(on current sco) traking information to LMS
Input	  :
   nPercent: points that students scored over the test
*/
function updateSCOUser_GradeInfo(nPercent)
{
   try
   {
       if(getCompletedStatus() == true)
       {
           var objAPI=getAPI();
           if(typeof objAPI=='undefined')
           {
               alert("API object not found, tracking information not updated");
               return;
           }
           nCurrentObjective = GetTakenStepCount();
           var n_OldScore=doLMSGetValue("cmi.core.score.raw");
           doLMSSetValue("cmi.core.score.max", 100);
           doLMSSetValue("cmi.core.score.min", 0);
           doLMSSetValue("cmi.core.score.raw", nPercent);
       }
       return true;
   }
   catch(e)
   {
       alert("Update score error due to following reason:\r\n" + e);
   }
}
function commitData()
{
   if(getCompletedStatus() == true)
   {
       var objAPI=getAPI();
       if(typeof objAPI=='undefined')
       {
           alert("API object not found, tracking information not updated");
           return;
       }
       doLMSSetValue("cmi.core.lesson_status", "completed");
       doLMSCommit();
   }
}
function getCompletedStatus()
{
   if((intCompletionState == 0) && (isAutoDemonstration()))
   {
       return true;
   }
   if((intCompletionState == 1) && (isOperationLecture()))
   {
       return true;
   }
   if((intCompletionState == 2) && (isPractice()))
   {
       return true;
   }
   if((intCompletionState == 3) && (isOperationExercise()))
   {
       return true;
   }
   if ((intCompletionState == 4) && (isAutoDemonstration()||isOperationExercise()||isPractice() || isOperationLecture()))
   {
       return true;
   }
   return false;
}
function UpdateResults()
{
	if ( (nStepIndex<0) || (nStepIndex>=arResult.length) )
		return;

	arResult[nStepIndex][3] += 1;
	arResult[nStepIndex][5]= frames(0).boolPointCount;
if(isAutoDemonstration() == true)
{
  arResult[nStepIndex][6] = frames(0).boolEnableModeAuto; 
}
if(isOperationLecture() == true)
{
  arResult[nStepIndex][6] = frames(0).boolEnableModeLecture; 
}
if(isPractice() == true)
{
  arResult[nStepIndex][6] = frames(0).boolEnableModeTeachme; 
}
if(isOperationExercise() == true)
{
  arResult[nStepIndex][6] = frames(0).boolEnableModeExercise; 
}
	if (curStepPassed && !blnShownHint)
	{
		arResult[nStepIndex][2] = true; // passed
	}
	else
	{
		arResult[nStepIndex][2] = false; // not passed	
	}
}

function goToStep(strUrl)
{
	if (bLoadCompleted==false)
		return;
	isMouseHolding = false;
	
	UpdateResults();

	if(strUrl == "") 
		return;

	curStepPassed = true;
	intStepClick = 0;
	blnShownHint = false;
	isCommandHelp=false;

	bFirstClicking = false;
	bPreviousClicking = false;
	bNextClicking = false;
	bLastClicking = false;

	if(setActiveStep(strUrl) == false)
	{
	return false;
	}
	if(isIE)
	{
		scrollX = contentFrame.document.body.scrollLeft;
		scrollY = contentFrame.document.body.scrollTop;
	}
	else
	{
		scrollX = contentFrame.pageXOffset;
		scrollY = contentFrame.pageYOffset;
	}

	bLoadCompleted = false;

	if (isIE)
	{
		contentFrame.document.location = strUrl;
	}
	else
		contentFrame.location.href = strUrl;

	window.frames[0].focus();
   if(arResult[0])
	{
	    arResult[nStepIndex][4] = contentFrame.strStepName;
	}
}

function setActiveStep(strStepUrl)
{
	activePageURL = strStepUrl;
	if(redrawStepWindow() == false)
	{
	return false;
	}
}

function redrawStepWindow()
{
	if(firstPage&&(firstPage!=activePageURL))
	{
		if(setDoneActiveStepItem(activePageURL) == false)
	{
	return false;
	}
		return;
	}

	if(!isIE)
	{
		var items = contentFrame.actions.items;
		if(isAutoDemonstration()||isAllWait(items))
		{
			treeMenuDisplay();
		}
		else
		{
			setTimeout("treeMenuDisplay()", 1);
		}
	}
	else
	{
		treeMenuDisplay();
	}
}

function ResetResult()
{
	nTimeLessonCounter = 0; // reset the timer
	for ( i=0 ; i<arResult.length ; i++ )
	{
		arResult[i][0] = ""; // Expected Action: Mouse, Key, Edit
		arResult[i][1] = ""; // User's Action: Mouse, Key, Edit
		arResult[i][2] = false;	// Passed the step or not
		arResult[i][3] = 0;	// number of tries
		arResult[i][4] = "";	// Step name
		arResult[i][5] = false;	// Step point
		arResult[i][6] = false;	// null step whether cai dian
	}
}

function goFirstStep()
{
   bLoadCompleted = true;
   nStepIndex = 0;

   goToStep(firstPage);
   ResetResult();
   blnShownHint = false;

   if(isOperationExercise())
   {
       curStepPassed = true;
	    intStepClick = 0;
   }

   if(isPractice())
   {
       nErrorCount	 = 0;
   }
}

function nsRefresh()
{

   if(window.innerWidth!=oldWidth||window.innerHeight!=oldHeight)
   {
       oldWidth=window.innerWidth;
       oldHeight=window.innerHeight;
       contentFrame.document.location.reload();
   }
}

if(!isIE)
{
   window.captureEvents(Event.RESIZE);
   window.onresize = nsRefresh;
}

function goNextStep()
{
   if(typeof contentFrame.strNextWaitPageUrl!='string') return;
   goToStep(contentFrame.strNextWaitPageUrl);
}

function isAllWait(actions)
{
	for(i=0;i<actions.length;i++)
	{
		if(!actions[i].isWait())
			return false;
	}
	
	return true;
}

function playAudio(strVoiceFileName)
{
	if(typeof contentFrame.playAudio != 'undefinded')
	{
		contentFrame.playAudio(strVoiceFileName);
	}
}

function muteAudio()
{
	if(contentFrame.document.VoicePlayer != null)
	{
		if(soundStatus == "on")
		{//Turning from on to off
			contentFrame.document.images["ImgTbMuteAudio"].src = "images/soundoff.gif";
			contentFrame.document.VoicePlayer.mute = true;
			soundStatus = "off";
		}
		else
		{//Turning from off to on
			contentFrame.document.images["ImgTbMuteAudio"].src = "images/soundon.gif";
			contentFrame.document.VoicePlayer.mute = false;
			soundStatus = "on";
		}
	}
}

function stopAudio()
{
	if(contentFrame.stopAutio != 'undefined')
		contentFrame.stopAudio();
}

function setEnableApplet(isEnable)
{
   var headerFrame=window.frames["dscrFrame"];
   if((typeof headerFrame=='undefined')
   || (typeof headerFrame.document.keyHandler=='undefined'))
   {
       return;
   }
   try
   {
       headerFrame.document.keyHandler.EnableFocus(isEnable);
   }catch (e) {};
}

// 機能を拡張するため、削除しません。保留します。
function DragDropAction(index,dragRect,dropRect,strDragImage,strDropImage,strParam,strGotoUrl,isAutoPass)
{
	this.index = index;
	this.draging = false;
	this.isAutoPass = isAutoPass;
	this.dragAction = new DragAction(index,dragRect,strDragImage,this);
	this.dropAction = new DropAction(index,dropRect,strDropImage,this,strGotoUrl);
	
	this.getID = getID;
	this.getDrawString = getDrawString;
	this.getStyleString = getStyleString;
	
	function getID()
	{
		ids = new Array();
		
		ids[0] = this.dragAction.getID();
		ids[1] = this.dropAction.getID();
		
		return ids;
	}
	
	function getDrawString()
	{
		return this.dropAction.getDrawString()+this.dragAction.getDrawString();
	}
	
	function getStyleString()
	{
		return this.dragAction.getStyleString()+this.dropAction.getStyleString();
	}
	
	this.isWait = function()
	{
		return false;
	}
	
	this.isMouse = function()
	{
		return false;
	}
	
	this.isKey = function()
	{
		return false;
	}
	
	this.isEdit = function()
	{
		return false;
	}
	
	this.isDragDrop = function()
	{
		return true;
	}
}

function DragAction(id,rect,strImagePath,notifyto)
{
	this.id = "drag_"+id;
	this.rect = rect;
	this.container = notifyto;
	this.strImagePath = strImagePath;
	
	this.getID = getID;
	this.getDrawString = getDrawString;
	this.getStyleString = getStyleString;
	this.onDragStart = onDragStart;

	function getID()
	{
		return this.id;
	}
	
	function getStyleString()
	{
		var strStyleString;
		
		strStyleString = "#"+this.getID();
		strStyleString += " {position:absolute; Z-INDEX:100; left:"+this.rect.left+";top:"+this.rect.top+";width:"+this.rect.width+";height:"+this.rect.height+"; BACKGROUND-COLOR: black}";
		
		return strStyleString;
	}

	function getDrawString()
	{
		var strDrawString;
		
		strDrawString = "<div id="+this.getID()+">";
		strDrawString += "<IMG SRC='"+this.strImagePath+"' ondragstart='actions.items["+this.container.index+"].dragAction.onDragStart()'></div>";
		
		return strDrawString;
	}

	function onDragStart()
	{
		var contentFrm = window.frames["course"];
		
		if((typeof contentFrm != 'object')||(contentFrm == null))
			return;
		contentFrm.document.getElementById(this.getID()).style.zIndex = 10;
		this.container.draging = true;
	}
}

function DropAction(id,rect,strImagePath,notifyto,strGotoUrl)
{
	this.id = "dop_"+id;
	this.rect = rect;
	this.container = notifyto;
	this.strGotoUrl = strGotoUrl;
	this.strImagePath = strImagePath;
	
	this.getID = getID;
	this.getStyleString = getStyleString;
	this.getDrawString = getDrawString;
	this.onDrop = onDrop;
	
	function getID()
	{
		return this.id;
	}
	
	function getStyleString()
	{
		var strStyleString;
		
		strStyleString = "#"+this.getID();
		strStyleString += " {position:absolute; Z-INDEX:100;left:"+this.rect.left+";top:"+this.rect.top+";width:"+this.rect.width+";height:"+this.rect.height+"; BACKGROUND-COLOR: black}";
		
		return strStyleString;	
	}

	function getDrawString()
	{
		var strDrawString;
		
		strDrawString = "<div id="+this.getID()+">";
		strDrawString += "<IMG SRC='"+this.strImagePath+"' ondrop='actions.items["+this.container.index+"].dropAction.onDrop()' ondragenter='window.event.returnValue=false' ondragover='window.event.returnValue=false'></div>";
		
		return strDrawString;
	}

	function onDrop()
	{
		if(this.container.draging)
			goToStep(this.strGotoUrl);
	}
}

// 機能を拡張するため、削除しません。保留します。
function processDrag()
{
	if((typeof contentFrame != 'object')||(contentFrame == null))
		return;
	var item = null;
	var actions = contentFrame.actions;
	
	for(var i=0; i<actions.items.length; i++)
	{
		item = actions.items[i];
		if(item.isDragDrop())
		{
			if(item.dragAction.rect.isPtInRect(contentFrame.event.clientX+contentFrame.document.body.scrollLeft,contentFrame.event.clientY + contentFrame.document.body.scrollTop))
				item.draging = true;
			contentFrame.document.getElementById(item.dragAction.getID()).style.zIndex = 10;
		}
	}
}

// 機能を拡張するため、削除しません。保留します。
function processDrop()
{
	if((typeof contentFrame != 'object')||(contentFrame == null))
		return;
	var item = null;
	var actions = contentFrame.actions;
	
	for(var i=0; i<actions.items.length; i++)
	{
		item = actions.items[i];
		if(item.isDragDrop())
		{
			if(item.draging && item.dropAction.rect.isPtInRect(contentFrame.event.clientX+contentFrame.document.body.scrollLeft,contentFrame.event.clientY + contentFrame.document.body.scrollTop))
			{
				goToStep(item.dropAction.strGotoUrl);
				return;
			}
           contentFrame.document.getElementById(item.dragAction.getID()).style.zIndex = 100;		
			item.draging = false;
		}
	}
	displayHint(false,"DragDrop");
}

function onclickhyperlink()
{
	bClickOnIcon = true;
}

// 機能を拡張するため、削除しません。保留します。
function onContextMenu()
{

	contentFrame.event.returnValue = false;

}

