
var API_URL="http://localhost:8080/netsound-api/";
var PASSWORD ="";
$("#login").click(function(e) {
	e.preventDefault();
	var USERNAME = $("#Username").val();
	PASSWORD = $("#Userpass").val();
	$.ajaxSetup({
	    headers: { 'Authorization': "Basic " +$.base64.btoa(USERNAME+':'+PASSWORD) }
	});
	Login($("#Username").val());
	console.log($("#Username").val());
	});

$("#singup").click(function(e) {
	e.preventDefault();

	window.location.replace("/signup.html");
	
	});

function Login(Username){
	var url = API_URL + 'profile/'+ Username;

	getUser(url, function(user){
		var userjs = new User(user);

document.cookie="userlink="+userjs.getLink('self').href;
document.cookie ="followingstings="+userjs.getLink('following-stings').href;
document.cookie="following="+userjs.getLink('following').href;
document.cookie="mystings="+userjs.getLink('stings').href;
//document.cookie="playlists="+userjs.getLink('playlists').href;
document.cookie="username="+userjs.username;
document.cookie="userpass="+PASSWORD;
window.location.replace("/home.html");
});
}

function getCookie(name){
    var pattern = RegExp(name + "=.[^;]*");
    matched = document.cookie.match(pattern);
    if(matched){
        var cookie = matched[0].split('=');
        return cookie[1];
    }
    return false;
}
