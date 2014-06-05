drop database if exists netsounddb;
create database netsounddb;
 
use netsounddb;
 
create table Users ( 
	userid		int not null auto_increment primary key,
	username	varchar(20) not null,
	userpass	char(32) not null,
	email		varchar(255) not null,
	name	varchar(70) not null,
	description	varchar(500) not  null
);
 
create table User_roles (
	userid				int not null,
	rolename 			varchar(20) not null,
	foreign key(userid) references Users(userid) on delete cascade,
	primary key (userid, rolename)
);

create table Songs (
	songid varchar(36) not null,
	userid int not null,
	song_name	varchar(100) not null,
	album_name varchar(100) not null,
	description	varchar(500) not  null,
	style	varchar(50) not  null,
	last_modified	timestamp,
	score	double not null,
	num_votes int not null,
	foreign key(userid) references Users(userid),
	primary key (songid, song_name),
	index(songid)
);

create table Playlists (
	playlistid int not null auto_increment,
	userid int not null,
	playlist_name	varchar(100) not null,
	description	varchar(500) not  null,
	style	varchar(50) not  null,
	last_modified	timestamp,
	score	double not null,
	foreign key(userid) references Users(userid),
	primary key (playlistid, playlist_name),
	index(playlistid)
);

create table Stings (
stingid int not null auto_increment primary key,
userid int not null,
content	varchar(500) not null,
foreign key(userid) references Users(userid),
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
songid int,
playlistid int
);

create table Follow(
followingid int,
followerid int,
foreign key(followingid) references Users(userid),
foreign key(followerid) references Users(userid),
primary key (followingid, followerid)
);