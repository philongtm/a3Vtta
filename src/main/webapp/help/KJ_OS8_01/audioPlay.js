function playAudio(url)
{
	if(document.all)
	{		
		if(typeof document.VoicePlayer == 'undefined' || typeof document.VoicePlayer.FileName == 'undefined')
			return;
		document.all.DivPlayer.style.visibility="hidden"; 
		document.VoicePlayer.Open(url);
       if (parent.soundStatus == "on") 
       { 
          document.VoicePlayer.mute = false; 
       } 
       else 
       { 
            document.VoicePlayer.mute = true; 
       } 
	}
	else if(document.VoicePlayer)
	{
		document.VoicePlayer.URL=url;
		document.VoicePlayer.controls.play();
	}
	else if(typeof document.embeds["VoicePlayer"] != 'undefined')
		document.embeds["VoicePlayer"].play(url);
	else if(typeof document.layers["DivPlayer"].document.embeds["VoicePlayer"] != 'undefined')
		document.layers["DivPlayer"].document.embeds["VoicePlayer"].Open(url);
}

function pauseAudio()
{
	if(document.all) 
	{
		if(typeof document.VoicePlayer == 'undefined' || typeof document.VoicePlayer.FileName == 'undefined')
			return;
		document.all.DivPlayer.style.visibility="hidden"; 
		document.VoicePlayer.pause();
	}
	else if(document.VoicePlayer)
		document.VoicePlayer.controls.pause();
	else if(typeof document.embeds["VoicePlayer"] != 'undefined')
		document.embeds["VoicePlayer"].play(url);
	else if(typeof document.layers["DivPlayer"].document.embeds["VoicePlayer"] != 'undefined')
		document.layers["DivPlayer"].document.embeds["VoicePlayer"].Open(url);
}


function stopAudio()
{
	if(document.all) 
	{
		if(typeof document.VoicePlayer == 'undefined' || typeof document.VoicePlayer.FileName == 'undefined')
			return;
		document.all.DivPlayer.style.visibility="hidden"; 
		document.VoicePlayer.stop();
	}
	else if(document.VoicePlayer)
		document.VoicePlayer.controls.stop();
	else if(typeof document.embeds["VoicePlayer"] != 'undefined')
		document.embeds["VoicePlayer"].stop();
	else if(typeof document.layers["DivPlayer"].document.embeds["VoicePlayer"] != 'undefined')
		document.layers["DivPlayer"].document.embeds["VoicePlayer"].stop();
}




if((typeof hasVoice!='undefined') && hasVoice)
{
	document.writeln ('<div id = "DivPlayer"><OBJECT id="VoicePlayer" width="2" height="2" \n' +
	'classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95" \n' +
	' codebase=""standby="Loading Microsoftｮ Windowsｮ Media Player components..."  \n' +
	'type="application/x-oleobject"> \n' +
	'<param name="FileName" value=""> \n' +
	'<param name="ShowStatusBar" value="False"> \n' +
	'<param name="AutoSize" value="False"> \n' +
	'<embed type="application/x-mplayer2"  \n' +
	'	pluginspage = "http://www.microsoft.com/Windows/MediaPlayer/" \n' +	
	'	name="VoicePlayer"  \n' +
	'	width="2"  \n' +
	'	height="2"  \n' +
	'	showstatusbar="0"  \n' +
	'	autostart=-1\n' +
	'	autosize="0"> \n' +	
	'</embed> \n' +
	'</OBJECT></div>');
}

