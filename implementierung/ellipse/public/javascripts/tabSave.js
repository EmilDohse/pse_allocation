function setCookie() {
  var anzParameter = setCookie.arguments.length;
  var parameter = setCookie.arguments;
  // 1. Cookie-Name
  var name = parameter[0];
  // 2. Cookie-Wert
  var value = (anzParameter >= 2) ? parameter[1] : "";
  value = escape(value); // URL-Codierung
  // Zusammensetzen des Cookies
  var c = name + "=" + escape(value);
    c += "; expires=" + new Date($.now() + 3600000).toGMTString();
    c += "; path=/";

  // Cookie setzen
  document.cookie = c;
}

function getCookie(name) {
  var i = document.cookie.indexOf(name + "=");
  var c = "";
  if (i > -1) {
    var ende = document.cookie.indexOf("; ",
               i+name.length+1);
    if (ende == -1) {
      ende = document.cookie.length;
    }
    c = document.cookie.substring(i+name.length+1, ende);
  }
  return unescape(c);
}

function parseCollection() {
   var str = getCookie("data");
   str = unescape(str);
   var temp = new Array();
   // Daten aus dem Cookie in ein Array umwandeln
   if (str != "") {
      str = str.replace(/,/g, "\",\""); 
      str = "\"" + str + "\"" 
      eval("temp = [" + str + "]");
   }
   // assoziatives Array erstellen
   var c = new Array();
   for (var i=0; i<temp.length; i+=2) {
     c[temp[i]] = temp[i+1];
   }
   // Array zurückgeben
   return c;
}

function read(name) {
   var c = parseCollection();
   return c[name];
}

function saveCollection(c) {
   var temp = new Array();
   for (var e in c) {
      temp[temp.length]=e;
      temp[temp.length]=c[e];
   }
   setCookie("data", temp.toString());
}

function set(name, wert) {
   var c = parseCollection();
   c[name] = wert;
   saveCollection(c);
}

function loadTab(name) {
	var value = read(name);
	if(value) {
		$('#' + value).tab('show');
	} else {
		$('#' + name +  ' a:first').tab('show');
	}
}

function saveTab(name, value) {
	set(name, value);
}
