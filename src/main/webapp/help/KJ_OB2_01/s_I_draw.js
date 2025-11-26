document.writeln("<style>");
var i;
for(i=0;i<imageObjects.length;i++){	
	document.writeln (imageObjects[i].getStyleString());
}
for(i=0;i<actions.items.length;i++){
	document.writeln (actions.items[i].getStyleString());
}
for(i=0;i<notes.length;i++){
	document.writeln (notes[i].getStyleString());
}
document.writeln("</style>");

var str="";
for(i=0;i<imageObjects.length;i++)
	str+=imageObjects[i].getDrawString();
if(str != "")
	document.writeln (str);

str="";
for(i=0;i<actions.items.length;i++)
	str+=actions.items[i].getDrawString();
if(str!="")
	document.writeln(str);

str="";
for(i=0;i<notes.length;i++)
{
	
	if(notes[i].getType() == "FoldedConer")
	{

		if(parent.isOperationExercise() || parent.isPractice())

			str+=notes[i].getDrawString();
	}
	else
		str+=notes[i].getDrawString();
}
if(str!="")
	document.writeln(str);

for(i=0;i<notes.length;i++)
{
	if(notes[i].getType()=="FoldedConer")
	{

		if(parent.isOperationExercise() || parent.isPractice())

			notes[i].draw();
	}
	else
		notes[i].draw();
}

if(parent.isOperationExercise() || parent.isPractice())
{
	var divs = new Array(), j;
	for(i=0;i<actions.items.length;i++)
	{
		if(actions.items[i].isMouse() || actions.items[i].isDragDrop())
			divs=divs.concat(actions.items[i].getID());
	}
	
	for(i=0;i<notes.length;i++)
	{
		if((notes[i].getType() != "FoldedConer")&&(notes[i].getType() != "Rectangular"))
			divs=divs.concat(notes[i].getID());
	}
	parent.setVisibleHint(false);
}



function showHint(hintID)
{

	var hintCtrl = document.getElementById(hintID);

	if((typeof(hintCtrl)=='undefined')||(hintCtrl==null))
		return;
	hintCtrl.style.visibility = "visible";
}


if(parent.isAutoDemonstration())
{
	for(n=0; n<notes.length; n++)
	{
		if((notes[n].getType() != "FoldedConer"))
		{
			document.getElementById(notes[n].getID()).style.visibility = "hidden";
			setTimeout("showHint(\""+notes[n].getID()+"\")",notes[n].timeToShow);
		}
	}
}

