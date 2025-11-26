function stopAutoDemonstration()
{
	if(iIntervalID != "")
		clearInterval(iIntervalID);
	if(iTimeoutID != "")
		clearTimeout(iTimeoutID);
}
document.onmousemove=function()
			{
			   if(parent.isOperationExercise()||parent.isPractice())
			   {
			   
			    for(i=0;i<notes.length;i++)
			    {
			        if(notes[i].bOpened==true)
			        {
    			
			             if(event.clientX<notes[i].boundary.left||event.clientX>(notes[i].boundary.left+ notes[i].boundary.width)||event.clientY<notes[i].boundary.top||event.clientY>(notes[i].boundary.top+ notes[i].boundary.height))
			             {

			                notes[i].bOpened = false;
			                notes[i].bClickOnIcon=false;
			                document.getElementById(notes[i].getID()).innerHTML="";
		                    notes[i].draw();
		                    
		                    
			             }
			            
			            
			            
			            
    			
			         }
    			
    			
			    }
			    
			    
			    
			    }
			  
            }
