var API_URL = "http://localhost:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");
var url = "";
$.ajaxSetup({
    headers: { 'Authorization': "Basic "+btoa(username+':'+userpass) }
});


var url = getCookie("createsong");

$('form').submit(function(e){
	e.preventDefault();
	$('progress').toggle();

	var formData = new FormData($('form')[0]);

	createSong(createSongLink.href, createSongLink.type, formData, function(formData){
		
		xhr: function() {  
	    	var myXhr = $.ajaxSettings.xhr();
	        if(myXhr.upload){ 
	            myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // For handling the progress of the upload
	        }
	        return myXhr;
        }
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		console.log(response);
		$('progress').toggle();
		$('form')[0].reset();
	})
    .fail(function (jqXHR, textStatus) {
    	alert("KO");
		console.log(textStatus);
	});
});

function progressHandlingFunction(e){
    if(e.lengthComputable){
        $('progress').attr({value:e.loaded,max:e.total});
    }
}


function delete_cookie( name ) {
	  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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