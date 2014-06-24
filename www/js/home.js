var username = getCookie("username");
var userpass = getCookie("userpass");
$.ajaxSetup({
	headers : {
		'Authorization' : "Basic " + $.base64.btoa(username + ':' + userpass)
	}
});
$(document).ready(function() {
	$('#searchpg').hide();
	$('#songpg').hide();
	$('#profilepg').hide();
	$('#playlistpg').hide();
	$('#homepg').show();
	loadStings(url = getCookie("followingstings"));
});
$('.search-panel .dropdown-menu').find('a').click(function(e) {
	e.preventDefault();
	var param = $(this).attr("href").replace("#", "");
	var concept = $(this).text();
	$('.search-panel span#search_concept').text(concept);
	$('.input-group #search_param').val(param);
});

$("#button_search").click(function(e) {
	e.preventDefault();
	if($('#search_param').val() == "all" || $('#search').val() == ""){
		alert("You have to select a type or put something to search");
	}
	else{
		$('#songpg').hide();
		$('#profilepg').hide();
		$('#playlistpg').hide();
		$('#homepg').hide();
		$('#searchpg').show(); 
		loadSearchPage($('#search_param').val(), $('#search').val());
			$('#search_param').val("all");
		
	}
	

});

$("#home").click(function(e) {
	e.preventDefault();
	$('#searchpg').hide();
	$('#songpg').hide();
	$('#profilepg').hide();
	$('#playlistpg').hide();
	loadStings(url = getCookie("followingstings"));
	$('#homepg').show();

});

$("#profile").click(function(e) {
	e.preventDefault();
	$('#searchpg').hide();
	$('#homepg').hide();
	$('#songpg').hide();
	$('#playlistpg').hide();
	var userprofile = getCookie("userlink");
	loadProfile(userprofile);
	$('#myTab a').removeClass('selected');
	$('#profilepg').show();

});

function delete_cookie(name) {
	document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
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
