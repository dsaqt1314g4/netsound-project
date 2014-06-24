var API_URL = "http://147.83.7.158:8080/netsound-api/";
var AUTH_URL = "http://147.83.7.158:8080/netsound-auth/AuthenticationServlet";



$("#login").click(function(e) {
	e.preventDefault();
	var USERNAME = $("#Username").val();
	var PASSWORD = $("#Userpass").val();
	document.cookie = "username=" + USERNAME;
	document.cookie = "userpass=" + PASSWORD;
	$.ajaxSetup({
		headers : {
			'Authorization' : "Basic " + $.base64.btoa(username + ':' + userpass)
		}
	});
	
	loginServlet(USERNAME, PASSWORD);

});

$("#singup").click(function(e) {
	e.preventDefault();

	window.location.replace("/signup.html");

});

function loginServlet(username, password) {
	

	$.ajax({
		url : AUTH_URL,
		type : 'POST',
		data : 'username=' + username + '&password=' + password
	}).done(function(data, status, jqxhr) {
		if (data.success){
			Login(username);
		}else{//Vuelvelo a intentar
			alert("Incorrect Username or Userpass");
			}
		
	});
}

function Login(Username){
	var url = API_URL + 'profile/'+ Username;

	getUser(url, function(user){
		var userjs = new User(user);
		console.log(userjs);
		document.cookie = "userlink=" + userjs.getLink('self').href;
		document.cookie = "followingstings="
				+ userjs.getLink('following-stings').href;
		document.cookie = "createfollower=" + userjs.getLink('create-follower').href;
		document.cookie = "type=" + userjs.getLink('create-follower').type;
		document.cookie = "following=" + userjs.getLink('following').href;	
		document.cookie = "follower=" + userjs.getLink('follower').href;
		document.cookie = "createsting=" + userjs.getLink('create-sting').href;
		document.cookie = "followingsongs=" + userjs.getLink('following-songs').href;
		document.cookie = "followingplaylists=" + userjs.getLink('following-playlists').href;
		document.cookie = "mystings=" + userjs.getLink('stings').href;
		document.cookie="myplaylists="+userjs.getLink('playlists').href;
		window.location.replace("/Homepage.html");
});
}
function getCookie(name) {
	var pattern = RegExp(name + "=.[^;]*");
	matched = document.cookie.match(pattern);
	if (matched) {
		var cookie = matched[0].split('=');
		return cookie[1];
	}
	return false;
}
