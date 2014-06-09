drop database if exists netsounddb;
create database netsounddb;
 
use netsounddb;
 
create table users (
	username	varchar(20) not null primary key,
	userpass	char(32) not null,
	email		varchar(255) not null,
	name	varchar(70) not null,
	imageid varchar(36),
	description	varchar(500) not  null
);
 
create table user_roles (
	username	varchar(20) not null,
	rolename 			varchar(20) not null,
	foreign key(username) references users(username) on delete cascade,
	primary key (username, rolename)
);

create table Songs (
	songid varchar(36) not null,
	username	varchar(20) not null,
	song_name	varchar(100) not null,
	album_name varchar(100),
	description	varchar(500),
	style	varchar(50) not  null,
	last_modified	timestamp,
	score	double not null,
	num_votes int not null,
	foreign key(username) references users(username),
	primary key (username, song_name),
	index(songid)
);

create table Playlists (
	playlistid int not null auto_increment,
	username	varchar(20) not null,
	playlist_name	varchar(100) not null,
	description	varchar(500) not  null,
	style	varchar(50) not  null,
	last_modified	timestamp,
	score	double not null,
	num_votes int not null,
	foreign key(username) references users(username),
	primary key (playlistid, playlist_name),
	index(playlistid)
);

create table Stings (
	stingid int not null auto_increment primary key,
	username	varchar(20) not null,
	content	varchar(500) not null,
	foreign key(username) references users(username),
	last_modified	timestamp
);

create table Stings_Song (
	stingid int not null,
	songid varchar(36) not null,
	foreign key(stingid) references Stings(stingid),
	foreign key(songid) references Songs(songid)
);

create table Stings_Playlist (
	stingid int not null,
	playlistid int not null,
	foreign key(stingid) references Stings(stingid),
	foreign key(playlistid) references Playlists(playlistid)
);

create table Playlist_Relation(
	songid varchar(36) not null,
	playlistid int,
	last_modified	timestamp,
	foreign key(songid) references Songs(songid),
	foreign key(playlistid) references Playlists(playlistid)
);

create table Follow(
	followingname varchar(20),
	followername varchar(20),
	foreign key(followingname) references users(username),
	foreign key(followername) references users(username),
	primary key (followingname, followername)
);