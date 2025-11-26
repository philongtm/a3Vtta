
var hasWMP;

document.writeln('<OBJECT id="AudioPlayer" width="0" height="0" classid="CLSID:22D6F312-B0F6-11D0-94AB-0080C74C7E95" \n'
	+ 'codebase=""\n'
    + 'standby="Loading Microsoftｮ Windowsｮ Media Player components..." \n'
    + 'type="application/x-oleobject">\n'
    + '<embed type="application/x-mplayer2"  \n' 
	+ '	pluginspage = "http://www.microsoft.com/Windows/MediaPlayer/" \n' 
	+ '	name="AudioPlayer"  \n' 
	+ '	width="2"  \n' 
	+ '	height="2"  \n' 
	+ '	showstatusbar="0"  \n' 
	+ '	autostart=-1\n' 
	+ '	autosize="0"> \n' 	
	+ '</embed> \n'
    + '</OBJECT>');
  
    
if(document.all)
{
	hasWMP = (typeof document.AudioPlayer != 'undefined' 
			 && typeof document.AudioPlayer.FileName != 'undefined');
}
else if(document.layers)
{
	navigator.plugins.refresh();
	var fHasWMP52 = navigator.mimeTypes && navigator.mimeTypes["application/x-mplayer2"] && navigator.mimeTypes["application/x-mplayer2"].enabledPlugin;
	var fHasWMP64 = navigator.mimeTypes && navigator.mimeTypes["video/x-ms-wm"] && navigator.mimeTypes["video/x-ms-wm"].enabledPlugin && navigator.mimeTypes["video/x-ms-wmv"] && navigator.mimeTypes["video/x-ms-wmv"].enabledPlugin;
	hasWMP = (fHasWMP64 || fHasWMP52) && (typeof document.embeds["AudioPlayer"] != 'undefined');
}
else if(document.getElementById)
{
	navigator.plugins.refresh();
	var fHasWMP52 = navigator.mimeTypes && navigator.mimeTypes["application/x-mplayer2"] && navigator.mimeTypes["application/x-mplayer2"].enabledPlugin;
	var fHasWMP64 = navigator.mimeTypes && navigator.mimeTypes["video/x-ms-wm"] && navigator.mimeTypes["video/x-ms-wm"].enabledPlugin && navigator.mimeTypes["video/x-ms-wmv"] && navigator.mimeTypes["video/x-ms-wmv"].enabledPlugin;
	hasWMP = (fHasWMP64 || fHasWMP52) && (typeof document.embeds["AudioPlayer"] != 'undefined');
}
else
{
	hasWMP = (typeof document.AudioPlayer != 'undefined' 
			 && typeof document.AudioPlayer.FileName != 'undefined');
		
}  


var audioStatus = 0;

function checkMedia()
{
if(!hasWMP)
	{ 
		notHasAudio();
		
			return audioStatus;
	}
else
{
return true;
}
	
}


function notHasAudio()
{
	if(audioStatus == 0)
	{ //Not processed yet
		if(confirm(strAudioConfirm))
		{
			window.open("http://www.microsoft.com/Windows/MediaPlayer/", "WMPDownload");
			audioStatus = 1;
		}
		else
		{ //Cancel
			audioStatus = 2;
		}
	}
	else if(audioStatus == 1)
	{ //Downloading		
	}
}
