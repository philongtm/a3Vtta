try{
var strTimerSelfTestMsg = "You couldn't finish the lesson within the time limit.";
			var autoDemo = 0;
			var exercise = 0;
			var lecture = 1;
			var practice = 0;
			
			
			var	nTimeLessonExercise = 0; //minute(s)
			var nTimeLessonCounter  = 0;
			var TimerLessonID  = 0;
var out=true;
			var	frameStructure="";
			var	stepFramePositionInAutoDemonstration="";
			var	stepFramePositionInOperationLecture="";
			var	stepFrameRatioInAuto="";
			var	stepFrameRatioInLecture="";			
			
			var strLessonName = "Main Menu";
			var	strAuthor = " ";
			var	strLessonExplain = " ";
			var strMailLessonName = "%4d%61%69%6e%20%4d%65%6e%75";
			var	strMailAuthor = " ";
			var	strMailReportEmail = "";
			
			var strStandardStepWindowPosition="Bottom";
			var strAutoStepWindowPosition="Bottom";


var scrollX = 0;
			var scrollY = 0;
var activePageURL="";

var	havetoWait=true;
if(getStepFramePosition() == "None")
	{
		havetoWait = false;
		
	}
			
			
			var	nTotalSteps = 6;
var	strInCorrectSteps = "Step Number of Incorrect Answers :  ";
			var	UILanguage = "JP";

			var	nSendReport = 0;
			var	strReportEmail = "";
			var	nRequiredPercent = 0;


var	nStepIndex = 0;



var curStepPassed = true;
var	strStepName = "";
var	intStepClick = "";

var pageUrl = window.location;
		var firstPage = "s_0.htm";
			var strLastPage = "s_6.htm";


var	blnShownHint = false;



			var	arResult = new Array(nTotalSteps);



			var bClickOnIcon = false;
			var firstTextbox = null;
			
		

			

			var	operatingMode="";

			
			var isIE = (document.all)? true:false;
			var jg_n4 = (navigator.appName + parseInt(navigator.appVersion) == 'Netscape4' && document.layers);
			var jg_n5 = (navigator.appName + parseInt(navigator.appVersion) == 'Netscape5');
			


			var strSelfTestHelpMsg="Incorrect answer.\nDo you want to display Help?";
			var strLastStepMsg="Now the lesson is complete.\n - Click [OK] to start the lesson again.\n - Click [Cancel] to end the lesson.";
					
			var strSCORMLastStepMsg="Now the lesson is complete.";			
			
			

	var API;
			var student_name;
			var arrObjectiveStatus=[];
			var nCurrentObjective=0;


			var bSendToLMS = false;
			var strLMSStatus = "";
			var intCompletionState = 0
		
			
			
			
			
			var strCommand="";
			var isCommandHelp=false;
			var descWindow = null;	//desciption window object
			var strActiveStepUrl = "";
			var blnHintVisible = true;


			var blnAutoLectureStatusGroup = true;
			var strAutoLectureFirstGroupt;
			var nCountStepForUserGroup = 0;

			var blnAutoLectureDefaultGroup;
			var blnAutoLectureNewGroup;
			var blnAutoLectureUserGroup;
			var nAutoLectureChoiseStep;
			var nArrCheckAutoLecture = [];
			var nGetSize = 0;



			
			for ( i=0 ; i<nTotalSteps ; i++ )
			{
				arResult[i] = new Array(5);
	arResult[i][0] = ""; // Expected Action: Mouse, Key, Edit
				arResult[i][1] = ""; // User's Action: Mouse, Key, Edit
				arResult[i][2] = false;	// Passed the step or not
				arResult[i][3] = 0;	// number of tries
				arResult[i][4] = "";	// Step name
				arResult[i][5] = false;	// Step point
				arResult[i][6] = false;	// null step whether cai dian 
			}
			
		
		if(activePageURL == "")
			activePageURL = firstPage;			
			
			
		
			
operatingMode = extractOperatingMode(window.location.search);
			stepFrameRatioInAuto = extractStepFrameRatioInAuto(window.location.search);
			stepFrameRatioInLecture = extractStepFrameRatioInLecture(window.location.search);
			extractStepFramePosition(window.location.search);
			frameStructure = getFrameStructure(getStepFramePosition(),true);
	
	
			
function extractOperatingMode(urlParam)
{
	if(typeof urlParam != 'string')
		return;
	var start = urlParam.indexOf('=');
	if(start == -1)
		return "";
	var end = urlParam.indexOf('&');
	if(end == -1)
		end = urlParam.length;
	if(start+1 > end)
		return "";
	var operatingMode =  urlParam.substring(start+1,end);
	return operatingMode;
}

function extractStepFrameRatioInAuto(urlParam)
{
	if(typeof urlParam != 'string')
		return;
	var start = urlParam.indexOf("stepFrameRatioInAuto");
	if(start == -1)
		return "";
	start = urlParam.indexOf('=',start);
	if(start == -1)
		return "";
	var end = urlParam.indexOf('&',start);
	if(end == -1)
		end = urlParam.length;
	if(start+1 > end)
		return "";
	return urlParam.substring(start+1,end);
}

function extractStepFrameRatioInLecture(urlParam)
{
	if(typeof urlParam != 'string')
		return;
	var start = urlParam.indexOf("stepFrameRatioInLecture");
	if(start == -1)
		return "";
	start = urlParam.indexOf('=',start);
	if(start == -1)
		return "";
	var end = urlParam.indexOf('&',start);
	if(end == -1)
		end = urlParam.length;
	if(start+1 > end)
		return "";
	return urlParam.substring(start+1,end);
}

function extractStepFramePosition(urlParam)
{
	var paramList = urlParam.split("&");
	var i,item,start,end;
	for(i=0; i<paramList.length; i++)
	{
		item = paramList[i];
		if(item.indexOf("stepFramePositionInAutoDemonstration")!=-1)
		{
			start = item.indexOf("=");
			end = item.length;
			stepFramePositionInAutoDemonstration = item.substring(start+1,end);
		}
		else if(item.indexOf("stepFramePositionInAutoLecture")!=-1)
		{
			start = item.indexOf("=");
			end = item.length;
			stepFramePositionInAutoLecture = item.substring(start+1,end);
		}
		else if(item.indexOf("stepFramePositionInOperationLecture")!=-1)
		{
			start = item.indexOf("=");
			end = item.length;
			stepFramePositionInOperationLecture = item.substring(start+1,end);				
		}
	}
}

function getStepFramePosition()
{
	if(isAutoDemonstration())
		return stepFramePositionInAutoDemonstration;
	else if(isOperationLecture())
		return stepFramePositionInOperationLecture;
	else
		return "None";
}

function getFrameStructure(stepFramePosition,includeAttributeName)
{
	if(isPractice() || isOperationExercise())
	{
		if(includeAttributeName)
			return "rows=\"100%,*\"";
		else
			return "100%,*";
	}
	var attributeName = "";
	var attributeValue = "";
	if(stepFramePosition=='Minimal')
	{
		if(includeAttributeName)
			attributeName = "rows=";
		attributeValue = "100%,*";
	}
	else if(stepFramePosition=='Right')
	{
		if(includeAttributeName)
			attributeName = "cols=";
		if(isAutoDemonstration())
			attributeValue = stepFrameRatioInAuto;
		else if(isOperationLecture())
			attributeValue = stepFrameRatioInLecture;
	}
	else if(stepFramePosition=='Bottom')
	{
		if(includeAttributeName)
			attributeName = "rows=";
		if(isAutoDemonstration())
			attributeValue = stepFrameRatioInAuto;
		else if(isOperationLecture())
			attributeValue = stepFrameRatioInLecture;
	}
	else if(stepFramePosition=='None')
	{
		if(includeAttributeName)
			attributeName = "rows=";
		attributeValue = "100%,*";		
	}
	
	if(attributeName != "")
	{
		return attributeName + "\"" + attributeValue + "\"";
	}
	else
		return attributeValue;
}

function isAutoDemonstration()
{
		return operatingMode == "auto";
}


function isOperationLecture()
{
	return operatingMode == "lecture";
}

function isPractice()
{
	return operatingMode == "teachme";
}

function isOperationExercise()
{
	return operatingMode == "exercise";
}


if(isPractice() || isOperationExercise())
			havetoWait = false;			
	
	
var strSelfTestMsg="これでレッスンは終了です。\nトータルステップ : %s,\n-不正解数 : %s\n正解率 : %s\n%MSI%\n- OKをクリックするともう一度レッスンを行います。\n - キャンセルをクリックするとレッスンを終了します。";
			
	var	nMaxTries = 0;		//var	nMaxTries	= 3;
	var	strTeachMeMsg1 = "You made an operational error [nTimes] times in this step. Do you want to go back to the operation lecture and review the section? \n\t- Click [OK] button to go back to the operation lecture.\n\t- Click [Cancel] to continue the pretest.";	
	var strSCORMSelfTestMsg="Now the lesson is complete.\nTotal Steps : %s,\n-Number of Incorrect Answers : %s\nCorrect Answer Rate : %s\n%MSI%";
var	bLoadCompleted = false;

}
catch(e)
{}
