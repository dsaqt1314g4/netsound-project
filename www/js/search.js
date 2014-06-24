var API_URL = "http://147.83.7.158:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");

function loadSearchPage(type, name){
	if(type == "Song"){
		searchSong(type, name);
	}else if(type == "Playlist"){
		searchPlaylist(type, name);
	}else if (type == "User"){
		searchUser(type, name);
	}
}

function searchSong(type, name){

	$('#panelsearchtitle').text("");
	$('#searchtable').text("");
	$('#panelsearchtitle').append(type);
	var url = API_URL + "songs?name=" + name;
	console.log(url);
	var songs = getSongs(url, function(songCollection){
		console.log(songCollection);
		$.each(songCollection.songs, function(index,item){
			var song = new Song(item);
			var songjsid = $('<tr><th><a id="song" href="#Song">' + song.song_name + '</a></th></tr>');
				songjsid.click(function(e){
					e.preventDefault();
					console.log(song.getLink('self').href);
					loadSongPage(song.getLink('self').href);
					loadStingsSong(song.getLink('stings').href);
					$('#searchpg').hide();
					$('#songpg').show();
					return false;
				});
			$('#searchtable').append(songjsid);
			
		});
});
}

function searchPlaylist(type, name){
	$('#panelsearchtitle').text("");
	$('#searchtable').text("");
	$('#panelsearchtitle').append(type);
	var url = API_URL + "playlist?name=" + name;
	console.log(url);
	var playlist =	getPlaylists(url, function(playlistCollection){
		console.log(playlistCollection);
		$.each(playlistCollection.playlist, function(index,item){
			var playlistjs = new Playlist(item);
			var playlistlink = $('<tr><th><a id="playlist" href="#Playlist">'+ playlistjs.playlist_name +'</a></th></tr>');
			playlistlink.click(function (e){
				e.preventDefault();
				loadPlaylistPage(playlistjs.getLink('self').href);
				$('#searchpg').hide();
				$('#playlistpg').show();
				
			});
			$('#searchtable').append(playlistlink);
			});
});
}

function searchUser(type, name){
	$('#panelsearchtitle').text("");
	$('#searchtable').text("");
	$('#panelsearchtitle').append(type);
	var url = API_URL + "profile?name=" + name;
	console.log(url);
	var users =	getUsers(url, function(userCollection){
		console.log(userCollection);
		$.each(userCollection.users, function(index,item){
			var userjs = new User(item);
			var userlink = $('<tr><th><a id="user" href="#User">'+ userjs.username +'</a></th></tr>');
			userlink.click(function (e){
				e.preventDefault();
				loadProfile(userjs.getLink('self').href);
				$('#searchpg').hide();
				$('#profilepg').show();
				
			});
			$('#searchtable').append(userlink);
			});
});
}
	


