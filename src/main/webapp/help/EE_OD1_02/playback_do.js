try{
ResetResult();

TimerLessonID  = window.setInterval("CheckLessonTimeOut();",1000);

function CheckLessonTimeOut()
{
   if(isAutoDemonstration())
   {
       window.clearInterval(TimerLessonID);
   }
   else if(isPractice())
   {
       window.clearInterval(TimerLessonID);
   }
   else if(isOperationExercise())
   {
       if ( ( (nTimeLessonCounter/60) >= nTimeLessonExercise )  && (nTimeLessonExercise >0) )
       {
           alert(strTimerSelfTestMsg)
           window.clearInterval(TimerLessonID);
           LessonTimeOut();
       }
       else
       {
           nTimeLessonCounter++;
           
       }
   }
   else
   {
       window.clearInterval(TimerLessonID);
   }
}

function LessonTimeOut()
{
	var	strMsg;
	if ( isOperationExercise() )
	{
		if ( bSendToLMS )
		{
			strMsg = formatSelfTestSumMsg(strSCORMSelfTestMsg, nPassedStepCount, nIncorrectStepCount);
			alert(strMsg);
			endLesson();
		}
		else
		{
			strMsg = ShowReport();
			if (strMsg == "Finish")
			{
				endLesson();
				return;
			}
			else if (strMsg == "Restart")
			{
				uncheckDoneAll();
				goFirstStep();
           nTimeLessonCounter = 0;
TimerLessonID  = window.setInterval("CheckLessonTimeOut();",1000);
				return;
			}
			else if (strMsg == "SendReport")
			{
				if (nSendReport != 0)
               if (nSendReport == 2)
                       EmailReport1Admin();     //text
               if (nSendReport == 1)
                       EmailReport2Admin();     //xml
				endLesson();
				return;
			}
		}
	}
	return;
}

if(!isIE)
{
   var oldWidth=window.innerWidth;
   var oldHeight=window.innerHeight;
}

}
catch(e)
{}
