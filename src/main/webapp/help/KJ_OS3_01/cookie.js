function saveCookie(name,value,days)
{
	if (days)
	{
		var date = new Date();
		date.setTime(date.getTime()+(days*24*60*60*1000));
		var expires = "";
		expires=""+date.toGMTString();
	}
	else
		expires = "";
	document.cookie = name+"="+value+expires+"; path=/";
}

function readCookie(name)
{
	var search;
	search = name + "="
	offset = document.cookie.indexOf(search)
	if (offset != -1)
	{
		offset += search.length ;
		end = document.cookie.indexOf(";", offset) ;
		if (end == -1)
			end = document.cookie.length;
		return unescape(document.cookie.substring(offset, end));
	}
	else
		return "";
}

function setCookie (name, value, expires)
{
	if(expires)
		document.cookie = name + "=" + escape(value) + "; expires=" + expires.toGMTString() +  "; path=/";
	else
		document.cookie = name + "=" + escape(value) + "; path=/";
}


function getCookie(name)
{
	var search;
	search = name + "="
	offset = document.cookie.indexOf(search) 
	if (offset != -1)
	{
		offset += search.length ;
		end = document.cookie.indexOf(";", offset) ;
		if(end == -1)
			end = document.cookie.length;
		return unescape(document.cookie.substring(offset, end));
	}
	else
		return "";
}


function deleteCookie(name) {

	var expdate = new Date();
	expdate.setTime(expdate.getTime() - (86400 * 1000 * 1));
	setCookie(name, "", expdate);
}
