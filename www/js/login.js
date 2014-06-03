
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
	var url = API_URL + '/profile/username/'+ Username;

		$.ajax({
			url : url,
			type : 'GET',
			crossDomain : true,
			dataType : 'json',
		}).done(function(data, status, jqxhr) {

					var repo = data;
						document.cookie="userid="+repo.userid;
						window.location.replace("/prueba.html");
					

				}).fail(function() {
					});
	}