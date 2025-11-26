function EndDialog() {
   var	strMsg;
   if ( (parent.bSendToLMS) )
   {
       strMsg = parent.strSCORMLastStepMsg;
       if (parent.isOperationExercise())
       {
           strMsg = parent.formatSelfTestSumMsg(parent.strSCORMSelfTestMsg, parent.nPassedStepCount, parent.nIncorrectStepCount);
       }
       alert(strMsg);
       parent.endLesson();
   }
   else
   {
       strMsg = parent.strLastStepMsg;
       if (parent.isOperationExercise())
       {
           strMsg = parent.ShowReport();
           if (strMsg == "Finish")
           {
               parent.endLesson();
               return;
           }
           else if (strMsg == "Restart")
           {
               parent.uncheckDoneAll();
               parent.goFirstStep();
               return;
           }
           else if (strMsg == "SendReport")
           {
               if (parent.nSendReport == 2)
                       parent.EmailReport1Admin();     //text
               if (parent.nSendReport == 1)
                       parent.EmailReport2Admin();     //xml
               parent.endLesson();
               return;
           }
       }
       else 
       { 
           if (confirm(strMsg)) 
           { 
               parent.uncheckDoneAll();
               parent.goFirstStep(); 
           } 
           else 
           { 
               parent.endLesson(); 
               return; 
           } 
       } 
   }
}
AdvanceTime = 1500;
setTimeout("EndDialog()",AdvanceTime);
