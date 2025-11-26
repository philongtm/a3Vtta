var ewin = window.open("", "TRACE",
                       "width=300,height=600,scrollbars,resizable,menubar");
var dwin = ewin.document;
dwin.open("text/plain");
function TRACE(message) {
	dwin.writeln(message);
}
