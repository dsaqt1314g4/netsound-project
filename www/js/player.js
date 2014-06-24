var audio = new Audio('./song/ddddd.mp3');
var playlistplay;
var songplay;
$("#play").click(function(e) {
	e.preventDefault();
	PlaySong(songplay.songid);
});
$('#pause').click(function(e){
	e.preventDefault();
	audio.pause();
});

function PlaySong(Songid) {
	
	$('#songname').text("");
	audio.pause();
	audio = new Audio('./song/' + Songid + '.mp3');
	$('#songname').append("Now Playing: " + songplay.song_name);
	audio.play();
	
}
$('#songname').click(function(e) {
	e.preventDefault();
	loadSongPage(songplay.getLink('self').href);
	loadStingsSong(songplay.getLink('self').href + "/stings");
	$('#profilepg').hide();
	$('#homepg').hide();
	$('#playlistpg').hide();
	$('#songpg').show();
});

function loadPlaySong(url) {
	getSong(url, function(song) {
		songplay = new Song(song);
		console.log(songplay);
		PlaySong(songplay.songid);
	});

}

function playlistPlay(url) {

	$('#songname').text("");
	var i = 0;
	audio.pause();
	
	songs = getSongs(url, function(songCollection) {
		console.log(songCollection.songs);
		audio = new Audio('./song/' + songCollection.songs[i].songid + ".mp3");
		songplay = new Song(songCollection.songs[i]);
		$('#songname').append("Now Playing: " + songplay.song_name);
		audio.play();
		
		audio.addEventListener("ended", function() {
			
			$('#songname').text("");
			i = i + 1;
			if(i< songCollection.songs.length){
			songplay = new Song(songCollection.songs[i]);
			audio.src = './song/' + songplay.songid + ".mp3";
			$('#songname').append("Now Playing: " + songplay.song_name);
			console.log("songplay: " + songplay);
			audio.play();
			}
			else{
				audio.pause();
			}
			$('#next').click(function(e) {
				
				$('#songname').text("");
				i = i + 1;
				if(i< songCollection.songs.length){
				songplay = new Song(songCollection.songs[i]);
				audio.src = './song/' + songplay.songid + ".mp3";
				$('#songname').append("Now Playing: " + songplay.song_name);
				console.log("songplay: " + songplay);
				audio.play();
				}
				else{
					audio.pause();
				}
				
			});
			
			$('#previous').click(function(e) {
				$('#songname').text("");
				i = i - 1;
				if(i>= 0){
				songplay = new Song(songCollection.songs[i]);
				audio.src = './song/' + songplay.songid + ".mp3";
				$('#songname').append("Now Playing: " + songplay.song_name);
				console.log("songplay: " + songplay);
				audio.play();
				}else{
					audio.pause();
					
					}
				
			});
		});

	});
}