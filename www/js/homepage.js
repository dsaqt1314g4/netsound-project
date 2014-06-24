
var API_URL = "http://147.83.7.158:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");


var url = "";

$("#stingshomepg").click(function(e){
	e.preventDefault();
	loadStings(url = getCookie("followingstings"));
	});
$("#songshomepg").click(function(e){
	e.preventDefault();
	getFollowingSongs(url = getCookie("followingsongs"));
	});
$("#playlistshomepg").click(function(e){
	e.preventDefault();
	getFollowingPlaylists(url = getCookie("followingplaylists"));
	});


function loadStings(url){ 
	console.log(url);
	var stings = getStings(url, function (stingCollection){
	
	console.log(url);
		$('#variable').text("");

		$('#variable').append(' <div class ="col-md-6"></div>');
		$('#variable').append('<h4><strong>Stings</strong></h4>');
		$('#variable').append('<ul id = "timeline" class="timeline">');
		$.each(stingCollection.stings, function(index,item){
			var sting = new Sting(item);
		//	console.log(sting.links.user.href);
			
			var link = $('<li><div class="timeline-panel"><div  class="timeline-heading"><a id="link" href ="#" ><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>');
			link.click(function(e){
				e.preventDefault();
				loadProfile(sting.getLink('user').href);
				$('#homepg').hide();
				$('#profilepg').show();
				
				return false;
			});
			$('#timeline').append(link);
		});
		$('#variable').append('</ul></div>');
		
	});
}


function getFollowingSongs(url){

	$('#variable').text("");
	var songs = getSongs(url, function (songCollection){
			$('#variable').append('<h4><strong>Songs</strong></h4>');
			$('#variable').append('<table id = "songtable" class="table table-striped">');
			$('#songtable').append('<tbody>');
			$.each(songCollection.songs, function(index, item) {
				var song = new Song(item);
				var link = $('<tr><th><a href ="#">' + song.song_name +'</a></th>');
				link.click(function(e){
					e.preventDefault();
					console.log(song.getLink('self').href);
					loadSongPage(song.getLink('self').href);
					loadStingsSong(song.getLink('stings').href);
					$('#homepg').hide();
					$('#songpg').show();
					
					
					return false;
				});
				$('#songtable').append(link);
			});
			$('#songtable').append('</tbody>');
			$('#variable').append('</table>');
});
}

function getFollowingPlaylists(url){

	$('#variable').text("");
	var playlist = getPlaylists(url, function (playlistCollection){
			$('#variable').append('<h4><strong>Playlists</strong></h4>');
			$('#variable').append('<table id = "playlisttalbe" class="table table-striped">');
			$('#playlisttable').append('<tbody>');
			$.each(playlistCollection.playlist, function(index, item) {
				var playlistjs = new Playlist(item);
				console.log(playlistjs);
				var link = $('<tr><th><a href ="#">' + playlistjs.playlist_name +'</a></th>');
				link.click(function(e){
					e.preventDefault();
					loadPlaylistPage(playlistjs.getLink('self').href);
					$('#homepg').hide();
					$('#playlistpg').show();
					return false;
				});
				$('#playlisttalbe').append(link);
			});
			$('#playlisttable').append('</tbody>');
			$('#variable').append('</table>');
});
}
$('#poststinghome').click(function (e){
	e.preventDefault();
	var sting = new Object();
	sting.username = username;
	sting.content = $('#stinghometext').val();
	console.log(songpgjs);
	var link = getCookie("createsting");
	createSting(link, "application/vnd.netsound.api.sting+json", JSON.stringify(sting), function(sting){
		alert("Nice!");
		loadStings(url = getCookie("followingstings"));
		$('#createStingHomepg').modal('hide');
	} );
	
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

