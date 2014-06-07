
var API_URL="http://localhost:8080/netsound-api/";
$("#login").click(function(e) {
	e.preventDefault();

	Login($("#Username").val());
	console.log($("#Username").val());
	});

$("#singup").click(function(e) {
	e.preventDefault();

	window.location.replace("/signup.html");
	
	});

function Login(Username){
	var url = API_URL + 'profile/username/'+ Username;

		getUser(url, function(user){
			var userjs = new User(user);
				//console.log(userjs.getLink('user').href);
document.cookie ="userid="+userjs.userid;
document.cookie="userlink="+userjs.getLink('user').href;
			console.log(document.cookie);
			window.location.replace("/prueba.html");
	});
}