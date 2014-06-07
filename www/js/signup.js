var API_URL = "http://localhost:8080/netsound-api";

$(document).ready(function(){
	loadRootAPI(function(rootAPI){
$("#signin").click(function(e){
	e.preventDefault();
	$("#result").text(' ');
	if($("#name").val() == "" || $("#username").val() == "" || $("#password").val() == "" || $("#email").val() == "" ){
		$('<div class="alert alert-danger"> <strong>Oh!</strong> You must complete all the fields </div>').appendTo($("#result"));
	}
	else{
	var user = new Object();
	user.name = $("#name").val();
	user.username = $("#username").val();
	user.userpass = $("#password").val();
	user.email = $("#email").val();
	user.description = $("#description").val();
	var createUserLink = rootAPI.getLink('create-user');
	console.log(createUserLink);
	createUser(createUserLink.href, createUserLink.type, JSON.stringify(user), function(user){
		$('<div class="alert alert-success"> <strong>Well done!</strong></div>').appendTo($("#result"));
		$('<button id="login" class="btn btn-lg btn-primary btn-block login-btn" type="submit">Login</button>').appendTo($("#result"));
		$("#login").click(function(e){
			window.location.replace("/login.html");
			});	
  	});
	
	}
	});
});
});



/*
function signin(Signin){
	var url = API_URL + '/profile';
	var data = JSON.stringify(Signin);


	$("#result").text(' ');

	$.ajax({
			url : url,
			type : 'POST',
			crossDomain : true,
			contentType: 'application/vnd.netsound.api.user+json',
			dataType : 'json',
			data : data,
		statusCode: {
	    		404: function() {$('<span><div class="alert alert-danger"> <strong>Oh!</strong> Page not found </div></span>').appendTo($("#result"));}
	    	}
		}).done(function(data, status, jqxhr) {
			$('<div class="alert alert-success"> <strong>Well done!</strong></div>').appendTo($("#result"));
			$('<button id="login" class="btn btn-lg btn-primary btn-block login-btn" type="submit">Login</button>').appendTo($("#result"));
			$("#login").click(function(e){
				window.location.replace("/login.html");
				});
			
	  	}).fail(function() {
			$('<div class="alert alert-danger"> <strong>Oh!</strong> You already have an account </div>').appendTo($("#result"));
		});
	}*/