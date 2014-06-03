$("#navbarid").load("Homepage2.html");
var API_URL = "http://localhost:8080/netsound-api";
var userid = getCookie("userid");
$.ajax({
	url : API_URL + "/profile/" + userid + "/following/stings",
	type : 'GET',
	crossDomain : true,
	dataType : 'json', 
}).done(function(data, status, jqxhr) {
			var stings = data;
			var Object = stings.stings;
			$.each(Object, function(i, v) {
				var sting = v;
				var d = new Date(sting.lastModified * 1000);
				var day = d.getDay();
				var month = d.getMonth();
				$('<li><div class="timeline-panel"><div  class="timeline-heading"><a  href =' + API_URL + "/profile/" + sting.userid +'><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>').appendTo($('#timeline'));
				console.log(d);
				console.log(v);
			});

}).fail(function() {
	$("#tablet").text("No repositories.");
});

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