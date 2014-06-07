$("#navbarid").load("Homepage2.html");
var API_URL = "http://localhost:8080/netsound-api/";
var userid = getCookie("userid");
var title ="";

$(document).ready(function(){
		loadStings(userid);
	});

$("#stings").click(function(e){
	e.preventDefault();
	loadStings(userid);
	});
$("#songrock").click(function(e){
	e.preventDefault();
	title = "Most popular Songs";
	getMostPopularSongs();
	
	});
$("#songpop").click(function(e){
	e.preventDefault();
	title = "Most popular Songs";
	getMostPopularSongs();
	});
$("#songjazz").click(function(e){
	e.preventDefault();
	title = "Most popular Songs";
	getMostPopularSongs();
	});
$("#playlistrock").click(function(e){
	e.preventDefault();
	title = "Most popular Playlists";
	getMostPopularSongs();
	});
$("#playlistpop").click(function(e){
	e.preventDefault();
	title = "Most popular Playlists";
	getMostPopularSongs();
	});
$("#playlistjazz").click(function(e){
	e.preventDefault();
	title = "Most popular Playlists";
	getMostPopularSongs();
	});
$("#link").click(function(e){
	e.preventDefault();
	title = "Most popular Playlists";
	getMostPopularSongs();
	});


function loadStings(userid){ //cambiar userid por el link cuando se hayan hecho los injectlinks
	var url = API_URL + "profile/"+userid+"/following/stings";
	var stings = getStings(url, function (stingCollection){
	
	console.log(stingCollection);
		$('#variable').text("");
		$('#variable').append('<h4><strong>Stings</strong></h4>');
		$('#variable').append('<ul id = "timeline" class="timeline">');
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
			$('#timeline').append(link);
		});
		$('#variable').append('</ul>');
		
	});
}

/*
function getMostPopularSongs(url){
	var songs = getSongs(url, function (songCollection)){
			$('#variable').append('<h4><strong>Most Popular Songs</strong></h4>');
			$('#variable').append('<table id = "table" class="table table-striped">');
			$('#table').append('<tbody>');
			$.each(songCollection.songs, function(index, item) {
				var song = new Song(item);
				var link = $('<tr><th><a href ="#">' + song.song_name +'</a></th>');
				link.click(function(e){
					e.preventDefault();
					document.cookie="userprofile="+sting.getLink('user').href;
					console.log(document.cookie);
					
					return false;
				});
				$('#table').append(link);
			});
			$('#table').append('<tbody>');
			$('#variable').append('</table>');
});
}

function getMostPopularPlaylist(url){
	var playlist = getPlaylists(url, function (playlistCollection)){
			$('#variable').append('<h4><strong>Most Popular Playlists</strong></h4>');
			$('#variable').append('<table id = "table" class="table table-striped">');
			$('#table').append('<tbody>');
			$.each(playlistCollection.playlists, function(index, item) {
				var playlist = new Playlist(item);
				var link = $('<tr><th><a href ="#">' + playlist.playlist_name +'</a></th>');
				link.click(function(e){
					e.preventDefault();
					document.cookie="userprofile="+sting.getLink('user').href;
					console.log(document.cookie);
					
					return false;
				});
				$('#table').append(link);
			});
			$('#table').append('<tbody>');
			$('#variable').append('</table>');
});
}

*/



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

