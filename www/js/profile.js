$("#navbarid").load("Bar.html");
var API_URL = "http://localhost:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");
var url = "";
$.ajaxSetup({
    headers: { 'Authorization': "Basic "+btoa(username+':'+userpass) }
});
var userjs;
$(document).ready(function(){
	getUser(url = getCookie("userprofile"), function(user){
		 userjs = new User(user);
		console.log(userjs.getLink('stings').href);
		$('#name').append(userjs.name);
		loadStings(userjs.getLink('stings').href);
});
	
	
});
$("#stings").click(function(e){
	e.preventDefault();

	loadStings(userjs.getLink('stings').href);
	});
$("#songs").click(function(e){
	e.preventDefault();

	updateSong();
	});


function loadStings(url){ 
	console.log(url);
	$('#panel').text("");
	var stings = getStings(url, function (stingCollection){
	
	console.log(stingCollection);
		$('<ul id = "timeline" class="timeline">').appendTo($("#panel"));
		$.each(stingCollection.stings, function(index,item){
			var sting = new Sting(item);
		//	console.log(sting.links.user.href);
			
			var link = $('<li><div class="timeline-panel"><div  class="timeline-heading"><a id="link" href ="#" ><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>');
			link.click(function(e){
				e.preventDefault();
				document.cookie="userprofile="+sting.getLink('user').href;
				console.log(document.cookie);
				window.location.replace("/profile.html");
				
				return false;
			});
			$('#timeline').append(link)
		});
		$('#panel').append('</ul>');
		
	});
}

function updateSong(){
	$('#panel').text("");
	if(userjs.username == username){
	$('#panel').load("modal.html");
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