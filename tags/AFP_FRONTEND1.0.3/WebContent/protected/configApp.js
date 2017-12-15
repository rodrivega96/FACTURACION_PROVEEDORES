var APP_VERSION = '1.0.3';
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
       return year+"-"+(month<10?"0"+month:month)+"-"+(day<10?"0"+day:day);
}

function getStringFormatDate(){
	return 'yyyy-mm-dd';
}
