try{
var strTimerSelfTestMsg = "あなたは制限時間内にレッスンを終了することができませんでした。";
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
			
			var strLessonName = "クレーム債権再設定登録";
			var	strAuthor = " ";
			var	strLessonExplain = " ";
			var strMailLessonName = "クレーム債権再設定登録";
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
			
			
			var	nTotalSteps = 20;
var	strInCorrectSteps = "不正解だったステップ番号 :  ";
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
			var strLastPage = "s_20.htm";


var	blnShownHint = false;



			var	arResult = new Array(nTotalSteps);



			var bClickOnIcon = false;
			var firstTextbox = null;
			
		

			

			var	operatingMode="";

			
			var isIE = (document.all)? true:false;
			var jg_n4 = (navigator.appName + parseInt(navigator.appVersion) == 'Netscape4' && document.layers);
			var jg_n5 = (navigator.appName + parseInt(navigator.appVersion) == 'Netscape5');
			


			var strSelfTestHelpMsg="不正解です. \nHelpを表示しますか?";
			var strLastStepMsg="これでレッスンは終了です。\n - OKをクリックするともう一度レッスンを行います。\n - キャンセルをクリックするとレッスンを終了します。";
					
			var strSCORMLastStepMsg="これでレッスンは終了します。";			
			
			

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
	var	strTeachMeMsg1 = "あなたはこのステップで[nTimes]回操作ミスをしました。オペレーションレクチャーに戻って再度トレーニングを行いますか？\n\t- OKボタンをクリックするとオペレーションレクチャーに戻ります。\n\t- キャンセルボタンをクリックするとプリテストを継続します。";	
	var strSCORMSelfTestMsg="これでレッスンは終了です。\nトータルステップ : %s,\n-不正解数 : %s\n正解率 : %s\n%MSI%";
var	bLoadCompleted = false;

}
catch(e)
{}
