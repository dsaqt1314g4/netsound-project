var API_URL = "http://147.83.7.158:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");	
var url="";
var songpgjs;
$('#playlistdropdown').click(function (e){
	e.preventDefault();
	loadPlaylistsUserDropdown(url = getCookie("myplaylists"));
	});

	
function loadSongPage(url){

	$('#songremove').hide();
	getSong(url, function(song){
		songpgjs = new Song(song);
		if(username == songpgjs.username){
			$('#songremove').show();
		}
		loadSong();
	});
}

function loadSong(){
	$('#songpgpaneldescription').text("");
	$('#songpgpaneldescription').append('<h4>Name: ' + songpgjs.song_name + '</h4>');
	$('#songpgpaneldescription').append('<h4>Album: ' + songpgjs.album + '</h4>');
	$('#songpgpaneldescription').append('<h4>User: ' + songpgjs.username + '</h4>');
	$('#songpgpaneldescription').append('<h4>Style: ' + songpgjs.style + '</h4>');
	$('#songpgpaneldescription').append('<h4>Description: ' + songpgjs.description + '</h4>');
	
}

function loadStingsSong(url){ 
	$('#songpgpanelstings').text("");
	$('#songpgpanelstings').append(' <div class ="col-md-3"></div>');
	var stings = getStings(url, function (stingCollection){
	console.log(stingCollection);
		$('<div class ="col-md-8"><ul id = "timelinesongs" class="timeline">').appendTo($("#songpgpanelstings"));
		$.each(stingCollection.stings, function(index,item){
			var sting = new Sting(item);
		//	console.log(sting.links.user.href);
			
			var link = $('<li><div class="timeline-panel"><div  class="timeline-heading"><a id="link" href ="#" ><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>');
			link.click(function(e){
				e.preventDefault();
				loadProfile(sting.getLink('user').href);
				$('#songpg').hide();
				$('#profilepg').show();
				
				return false;
			});
			$('#timelinesongs').append(link)
		});
		$('#songpgpanelstings').append('</ul></div>');
		
	}); 
}
function loadPlaylistsUserDropdown(url){
	$('#playlistaddsongpg').text("");
	var playlist =	getPlaylists(url, function(playlistCollection){
		console.log(playlistCollection);
		$.each(playlistCollection.playlist, function(index,item){
			var playlistjs = new Playlist(item);
			var link = $('<li><a id="playlist" href="#Playlist">'+ playlistjs.playlist_name +'</a></li>');
			link.click(function (e){
				e.preventDefault();
				var url = playlistjs.getLink('upload-song').href;
				var content = playlistjs.getLink('upload-song').type
				alert(url);
				createPlaylistSong(url,content, JSON.stringify(songpgjs), function(songpgjs){
					alert("Added!");
					
			  	});
			});
			$('#playlistaddsongpg').append(link);
		});
});
}

$('#postssongstingsong').click(function (e){
	e.preventDefault();
	var sting = new Object();
	sting.username = username;
	sting.content = $('#stingsongtext').val();
	console.log(songpgjs);
	var link = API_URL + "/songs/" + songpgjs.songid + "/stings";
	createSting(link, "application/vnd.netsound.api.sting+json", JSON.stringify(sting), function(sting){
		alert("Nice!");
		loadStingsSong(songpgjs.getLink('stings').href);
		$('#poststingsongmodalsong').modal('hide');
	} );
	
	});

$('#songremove').click(function (e){
	console.log(songpgjs);
	var deletesongurl = songpgjs.getLink('delete-song').href;
	deleteSong(deletesongurl, function(songpgjs){
		alert("Song Deleted!");
		$('#songpg').hide();
		$('#homepg').show();
		
	});
});

$('#songpgplay').click(function(e){
	loadPlaySong(songpgjs.getLink('self').href);
	
});