var API_URL = "http://localhost:8080/netsound-api";


$("#signin").click(function(e){
	e.preventDefault();
	$("#result").text(' ');
	if($("#name").val() == "" || $("#username").val() == "" || $("#password").val() == "" || $("#email").val() == "" ){
		$('<div class="alert alert-danger"> <strong>Oh!</strong> You must complete all the fields </div>').appendTo($("#result"));
	}
	else{
	var Signin = new Object();
	Signin.name = $("#name").val();
	Signin.username = $("#username").val();
	Signin.userpass = $("#password").val();
	Signin.email = $("#email").val();
	Signin.description = $("#description").val();
	signin(Signin);
	}
	});




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
	}