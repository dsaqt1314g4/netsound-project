var NETSOUND_API_HOME="http://localhost:8080/netsound-api/";

function Link(rel, linkheader){
	this.rel = rel;
	this.href = decodeURIComponent(linkheader.find(rel).template().template);
	this.type = linkheader.find(rel).attr('type');
	this.title = linkheader.find(rel).attr('title');
}

function buildLinks(linkheaders){
	var links = {};
	$.each(linkheaders, function(i,linkheader){
		var linkhdr = $.linkheaders(linkheader);
		var rels = linkhdr.find().attr('rel').split(' ');
		$.each(rels, function(key,rel){
			var link = new Link(rel, linkhdr);
			links[rel] = link;
		});
	});

	return links;
}

function User(user){
	this.username = user.username;
	this.userpass = user.userpass;
	this.description = user.description;
	this.name = user.name;
	this.email = user.email;
	var instance = this;
	this.links = buildLinks(user.links);
	this.getLink = function(rel){
		return this.links[rel];
	}
	
}
function UserCollection(userCollection){
	this.users = userCollection.users;

	this.links = buildLinks(stingCollection.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}

function Sting(sting){
	this.username = sting.username;
	this.userid = sting.userid;
	this.lastModified = sting.lastModified;
	this.content = sting.content;
	this.links = buildLinks(sting.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}

function StingCollection(stingCollection){
	this.oldestTimestamp = stingCollection.oldestTimestamp;
	this.newestTimestamp = stingCollection.newestTimestamp;
	this.stings = stingCollection.stings;
//	console.log(this.stings);
	/*this.links = buildLinks(stingCollection.links);  Cuando esten los injectilnk descomentar
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}*/
}

function Song(song){
	this.songid = song.songid;
	this.userid = song.userid;
	this.song_name = song.song_name;
	this.album = song.album;
	this.description = song.description;
	this.style = song.style;
	this.date = song.date;
	this.songURL = song.songURL;
	this.score = song.score;
	/*this.links = buildLinks(sting.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}*/
}

function SongCollection(songCollection){
	this.oldestTimestamp = stingCollection.oldestTimestamp;
	this.newestTimestamp = stingCollection.newestTimestamp;
	this.songs = songCollection.songs;
//	console.log(this.stings);
	/*this.links = buildLinks(stingCollection.links);  Cuando esten los injectilnk descomentar
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}*/
}

function Playlist(playlist){
	this.playlistid = playlist.playlistid;
	this.userid = playlist.userid;
	this.playlist_name = playlist.playlist_name;
	this.description = playlist.description;
	this.style = playlist.style;
	this.date = playlist.date;
	this.playlistURL = playlist.playlistURL;
	this.score = playlist.score;
	/*this.links = buildLinks(sting.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}*/
}

function PlaylistCollection(playlistCollection){
	this.oldestTimestamp = stingCollection.oldestTimestamp;
	this.newestTimestamp = stingCollection.newestTimestamp;
	this.playlists = playlistCollection.playlists;
//	console.log(this.stings);
	/*this.links = buildLinks(stingCollection.links);  Cuando esten los injectilnk descomentar
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}*/
}


function RootAPI(rootAPI){
	this.links = buildLinks(rootAPI.links);
	var instance = this;
	this.getLink = function(rel){
		return this.links[rel];
	}
}


function loadRootAPI(success){
	$.ajax({
		url : NETSOUND_API_HOME,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var rootAPI = new RootAPI(response);
    	success(rootAPI);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
	
}

function getStings(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		console.log(response);
		var stingCollection = new StingCollection(response);
		console.log(stingCollection);
		//success(response.stings);
		success(stingCollection);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function createSting(url, type, sting, success){
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		contentType: type, 
		data: sting
	})
	.done(function (data, status, jqxhr) {
		var sting = $.parseJSON(jqxhr.responseText);
		success(sting);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}


function deleteSting(url, success){
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		//var sting = $.parseJSON(jqxhr.responseText);
		success();
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getUsers(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var userCollection = new UserCollection(response);
		
		success(userCollection);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getUser(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var user = $.parseJSON(jqxhr.responseText);
		success(user);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function createUser(url, type, user, success){
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		contentType: type, 
		data: user
	})
	.done(function (data, status, jqxhr) {
		var user = $.parseJSON(jqxhr.responseText);
		success(user);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}


function deleteUser(url, success){
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		success();
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getSongs(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var response = $.parseJSON(jqxhr.responseText);
		var songCollection = new SongCollection(response);
		
		success(songCollection);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function getSong(url, success){
	$.ajax({
		url : url,
		type : 'GET',
		crossDomain : true,
		dataType: 'json'
	})
	.done(function (data, status, jqxhr) {
		var song = $.parseJSON(jqxhr.responseText);
		success(song);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}

function createSong(url, type, song, success){
	$.ajax({
		url : url,
		type : 'POST',
		crossDomain : true,
		contentType: type, 
		data: song
	})
	.done(function (data, status, jqxhr) {
		var song = $.parseJSON(jqxhr.responseText);
		success(song);
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}


function deleteSong(url, success){
	$.ajax({
		url : url,
		type : 'DELETE',
		crossDomain : true
	})
	.done(function (data, status, jqxhr) {
		success();
	})
    .fail(function (jqXHR, textStatus) {
		console.log(textStatus);
	});
}