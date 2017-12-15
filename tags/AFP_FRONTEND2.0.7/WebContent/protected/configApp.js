var APP_VERSION = '2.0.7';
var PROTOCOL = 'http://';
var HOST = '192.168.50.13';
var PORT = '8080';
var WS_URI = '/facpro-backend/rest/';

function getAppVersion() {
	return APP_VERSION;
}

function saveAs(blob, name) {
	var url = URL.createObjectURL(blob);
	window.open(url, '_blank', '');
}

function getWSURL($wsClassPath, $wsMethodPath) {
	return PROTOCOL + HOST + ':' + PORT + WS_URI + $wsClassPath + '/'
			+ $wsMethodPath;
}

function getFormattedDate(date){
    var month = date.getMonth()+1;
    var day = date.getDate();
    var year = date.getFullYear();
       return (day<10?"0"+day:day)+"-"+(month<10?"0"+month:month)+"-"+year;
}

function getDate(input){
	return new Date(input.replace( /(\d{2})-(\d{2})-(\d{4})/, "$2/$1/$3"));
}

function getStringElement(attrElement, scope, functionDo, value) {
	var elements = [];
	var element = null;
	var elementPrevName = "";
	elements = attrElement.split(".");
	element = scope;

	for (var i = 0; i < elements.length; i++) {
		if (i == elements.length - 1) {
			elementPrevName = elements[i].toString();
		} else {
			element = element[elements[i].toString()];
		}
	}
	return functionDo(element, elementPrevName, value);
}


function getStringFormatDate(){
	return 'dd-mm-yyyy';
}
