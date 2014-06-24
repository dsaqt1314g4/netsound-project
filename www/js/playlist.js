var API_URL = "http://147.83.7.158:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");
var url = "";
var playlistpgjs;


function loadPlaylistPage(url) {

	$('#playlistpg').hide();
	getPlaylist(url, function(playlist) {
		playlistpgjs = new Playlist(playlist);
		if(username == playlistpgjs.username){

			$('#playlistpg').show();
		}
		loadPlaylist();
		loadPlaylistSongs(playlistpgjs.getLink('songs').href);
		loadStingsPlaylist(playlistpgjs.getLink('stings').href);
	});
}
function loadPlaylist(){
	$('#playlistpgpaneldescription').text("");
	$('#playlistpgpaneldescription').append('<h4>Name: ' + playlistpgjs.playlist_name + '</h4>');
	$('#playlistpgpaneldescription').append('<h4>User: ' + playlistpgjs.username + '</h4>');
	$('#playlistpgpaneldescription').append('<h4>Style: ' + playlistpgjs.style + '</h4>');
	$('#playlistpgpaneldescription').append('<h4>Description: ' + playlistpgjs.description + '</h4>');
}

function loadStingsPlaylist(url){ 
	$('#playlistpgpanelstings').text("");
	$('#playlistpgpanelstings').append(' <div class ="col-md-3"></div>');
	var stings = getStings(url, function (stingCollection){
	console.log(stingCollection);
		$('<div class ="col-md-8"><ul id = "timelineplaylists" class="timeline">').appendTo($("#playlistpgpanelstings"));
		$.each(stingCollection.stings, function(index,item){
			var sting = new Sting(item);
			var link = $('<li><div class="timeline-panel"><div  class="timeline-heading"><a id="link" href ="#" ><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>');
			link.click(function(e){
				e.preventDefault();
				loadProfile(sting.getLink('user').href);
				$('#playlistpg').hide();
				$('#profilepg').show();
				return false;
			});
			$('#timelineplaylists').append(link)
		});
		$('#playlistpgpanelstings').append('</ul></div>');
		
	}); 
}

function loadPlaylistSongs(url){
	
	$('#songsplaylisttable').text("");
	var songs = getSongs(url, function(songCollection){
		$.each(songCollection.songs, function(index,item){
			var song = new Song(item);
			var songjsid = $('<tr><th><a id="song" href="#Song">' + song.song_name + '</a></th></tr>');
				songjsid.click(function(e){
					e.preventDefault();
					console.log(song.getLink('self').href);
					loadSongPage(song.getLink('self').href);
					loadStingsSong(song.getLink('stings').href);
					$('#playlistpg').hide();
					$('#songpg').show();
					return false;
				});
			$('#songsplaylisttable').append(songjsid);
			
		});
});
}

$('#poststingplaylist').click(function (e){
	e.preventDefault();
	var sting = new Object();
	sting.username = username;
	sting.content = $('#stingplaylisttext').val();
	var link = API_URL + "playlist/" +playlistpgjs.playlistid + "/stings";
	console.log(link);
	createSting(link, "application/vnd.netsound.api.sting+json", JSON.stringify(sting), function(sting){
		console.log(link +  " application/vnd.netsound.api.sting+json " + JSON.stringify(sting))
		alert("Nice!");
		loadStingsPlaylist(playlistpgjs.getLink('stings').href);
		$('#poststingsongmodalplaylist').modal('hide');
	} );
	
	});

$('#playlistremove').click(function (e){
	var deleteplaylisturl = playlistpgjs.getLink('delete-playlist').href;
	deletePlaylist(deleteplaylisturl, function(playlistpgjs){
		alert("Playlist Deleted!");
		$('#playlistpg').hide();
		$('#homepg').show();
		
	});
});


$('#playlistpgplay').click(function(e){
	e.preventDefault();
	playlistPlay(playlistpgjs.getLink('songs').href);
});
	
