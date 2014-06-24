var API_URL = "http://147.83.7.158:8080/netsound-api/";
var username = getCookie("username");
var userpass = getCookie("userpass");	
var followid = 0; 

$('#panel2').hide();
var url = "";
var userprofilepgjs;
$("#stingstab").click(function(e) {
	e.preventDefault();
	loadStingsProfile(userprofilepgjs.getLink('stings').href);
});
$("#songstab").click(function(e) {
		e.preventDefault();
		updateSong();
		loadSongsProfile(userprofilepgjs.getLink('my-songs').href);
});

$("#playliststab").click(function(e) {
	e.preventDefault();
	createPlaylistProfile();
	loadPlaylistsProfile(userprofilepgjs.getLink('playlists').href);
});

$("#followingtab").click(function(e) {
	e.preventDefault();
	loadFollowProfile(userprofilepgjs.getLink('follower').href);
});
$("#followertab").click(function(e) {
	e.preventDefault();
	loadFollowProfile(userprofilepgjs.getLink('following').href);
});

function loadProfile(url){
	$('#songspanel').hide();
	$('#followpanel').hide();
	$('#follow').text("");
	$('#follow').append("Follow");
	$('#follow').hide();
	$('#playlistspanel').hide();
	 $('#name').text('');
	var user = getUser(url , function(user){
		 userprofilepgjs = new User(user);
		 $('#name').append(userprofilepgjs.name);
			console.log(userprofilepgjs);
			if(username != userprofilepgjs.username){
				$('#follow').show();
				var users = getUsers(getCookie("follower"), function(userCollection){
					$.each(userCollection.users, function(index,item){
						var user = new User(item);
						if(userprofilepgjs.username == user.username){
							$('#follow').text("");
							console.log("entro aqui");
							$('#follow').append("Unfollow");
						}
				});
				});
			}
			loadStingsProfile(userprofilepgjs.getLink('stings').href);
	});
	
	
}

$('#follow').click(function (e){
	if($('#follow').text() == "Follow"){
		var createfollow = getCookie("createfollower");
		var type = getCookie("type")
		createUser(createfollow, type, JSON.stringify(userprofilepgjs), function(userprofilepgjs){
			$('#follow').text("");
			$('#follow').append("Unfollow");
		});
	}else{
		var deletefollowing = getCookie("createfollower") + "/" + userprofilepgjs.username;
		deleteUser(deletefollowing, function(userprofilepgjs){

			$('#follow').text("");
			$('#follow').append("Follow");
			
		});
	}
	
	
});


function loadStingsProfile(url){ 
	$('#songspanel').hide();
	$('#followpanel').hide();
	$('#playlistspanel').hide();
	$('#stingspanel').show();
	$('#stingspanel').text("");
	console.log(url);
	$('#stingspanel').append(' <div class ="col-md-3"></div>');
	var stings = getStings(url, function (stingCollection){
	console.log(stingCollection);
		$('<div class ="col-md-8"><ul id = "timelineprofile" class="timeline">').appendTo($("#stingspanel"));
		$.each(stingCollection.stings, function(index,item){
			var sting = new Sting(item);
			var link = $('<li><div class="timeline-panel"><div  class="timeline-heading"><a id="link" href ="#" ><h4 class="timeline-title" >' + sting.username + '</h4></a></div><div class="timeline-body"><p>' + sting.content + '</p></div></div>');
			$('#timelineprofile').append(link)
		});
		$('#stingspanel').append('</ul></div>');
		
	});
}

function loadSongsProfile(url){
	$('#stingspanel').hide();
	$('#followpanel').hide();
	$('#playlistspanel').hide();
	$('#songsprofiletable').text("");
	$('#songspanel').show();
	
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
					$('#profilepg').hide();
					$('#songpg').show();
					return false;
				});
			$('#songsprofiletable').append(songjsid);
			
		});
});
}

function loadPlaylistsProfile(url){
	$('#songspanel').hide();
	$('#stingspanel').hide();
	$('#followpanel').hide();
	$('#playlistsprofiletable').text("");
	$('#playlistspanel').show();
console.log(url);
var playlist =	getPlaylists(url, function(playlistCollection){
		console.log(playlistCollection);
		$.each(playlistCollection.playlist, function(index,item){
			var playlistjs = new Playlist(item);
			var playlistlink = $('<tr><th><a id="playlist" href="#Playlist">'+ playlistjs.playlist_name +'</a></th></tr>');
			playlistlink.click(function (e){
				e.preventDefault();
				loadPlaylistPage(playlistjs.getLink('self').href);
				$('#profilepg').hide();
				$('#playlistpg').show();
				
			});
			$('#playlistsprofiletable').append(playlistlink);
			});
});
	
}

function loadFollowProfile(url){
	$('#songspanel').hide();
	$('#stingspanel').hide();
	$('#followprofiletable').text("");
	$('#playlistspanel').hide();
	$('#followpanel').show();
	var users = getUsers(url, function(userCollection){
		$.each(userCollection.users, function(index,item){
			var user = new User(item);
			var userlink = $('<tr><th><a id="user" href="#User">'+ user.username +'</a></th></tr>');
			userlink.click(function (e){
				e.preventDefault();
				loadProfile(user.getLink('self').href);
				
			});
			$('#followprofiletable').append(userlink);
			});
		
	});
}
function createPlaylistProfile(){
	if(username != userprofilepgjs.username){
		$('#profileplaylistmodal').hide();
		}
		else{
			$('#profileplaylistmodal').show();
		}
	
}


var style;
$("#createPlaylist").click(function(e) {
	e.preventDefault();
	var playlist = new Object();
	playlist.username = username;
	playlist.playlist_name = $('#inputPlaylist').val();
	playlist.description = $('#descriptionplaylist').val();
	playlist.style = style;
	var link = userprofilepgjs.getLink('create-playlist');
	console.log(userprofilepgjs);
	createPlaylist(link.href, link.type, JSON.stringify(playlist), function(playlist){
		alert("Playlist " + playlist.playlist_name + " created");
		$('#playlistsprofiletable').text("");
		loadPlaylistsProfile(userprofilepgjs.getLink('playlists').href);
		$('#playlistmodal').modal('hide');
			
	});
});

$('#hiphop').click(function (e){
	style = $('#hiphop').val();
});
$('#rock').click(function (e){
	style = $('#rock').val();
});
$('#blues').click(function (e){
	style = $('#blues').val();
});
$('#jazz').click(function (e){
	style = $('#jazz').val();
});
$('#pop').click(function (e){
	style = $('#pop').val();
});

function updateSong(){
	//console.log(username, userprofilepgjs.username);
	if(username != userprofilepgjs.username){
	$('#modalbutton').hide();
	}
	else{
		$('#modalbutton').show();
	}
	 
}

$('#uploadButton').click(function(e){
	$.ajaxSetup({
		headers : {
			'Authorization' : "Basic " + btoa(username + ':' + userpass)
		}
	});
	
	e.preventDefault();
	$('progress').toggle();
	var formData = new FormData($('form')[0]);
	var link = userprofilepgjs.getLink('create-song');
	url = link.href;
	var type =  false;
	
	$.ajax({
		url: url,
		type: 'POST',
		xhr: function() {  
	    	var myXhr = $.ajaxSettings.xhr();
	        if(myXhr.upload){ 
	            myXhr.upload.addEventListener('progress',progressHandlingFunction, false); // For handling the progress of the upload
	        }
	        return myXhr;
        },
		crossDomain : true,
		data: formData,
		cache: false,
		contentType: false,
        processData: false
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		lastFilename = response.filename;
		$('#uploadedImage').attr('src', response.imageURL);
		$('progress').toggle();
		$('form')[0].reset();
		alert("Uploaded!");
		$('#songsprofiletable').text("");
		loadSongsProfile(userprofilepgjs.getLink('my-songs').href);
		$('#success').modal('hide');
	})
    .fail(function (jqXHR, textStatus) {
    	alert("Something went wrong, probably because there are a song with the same name.... try again ;)");
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