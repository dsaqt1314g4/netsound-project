drop database if exists netsounddb;
create database netsounddb;
 
use netsounddb;
 
create table Users (
userid		int not null auto_increment primary key,
username	varchar(20) not null,
name	varchar(70) not null,
description	varchar(500) not  null
);
 
create table Songs (
songid int not null auto_increment,
userid int not null,
song_name	varchar(100) not null,
album_name varchar(100) not null,
description	varchar(500) not  null,
style	varchar(50) not  null,
last_modified	timestamp,
score	int not null,
songbin varchar(36) not null,
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
score	int not null,
foreign key(userid) references Users(userid),
primary key (playlistid, playlist_name),
index(playlistid)
);

create table Stings (
stingid int not null auto_increment primary key,
content	varchar(500) not null,
last_modified	timestamp
);

create table StingsRelation (
stingid int not null,
userid int not null,
songid int,
playlistid int
);

create table PlaylistRelation(
songid int,
playlistid int
);

create table Follow(
followingid int,
followerid int
);