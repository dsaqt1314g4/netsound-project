package edu.upc.eetac.dsa.dsaqt1314g4.netsound.model;

public class Song {

	private String songid;
	private String username;
	private String song_name;
	private String album;
	private String description;
	private String style;
	private long date;
	private String songURL;
	private String score;
	private String num_votes;
	
		
	public Song(String songid, String username, String song_name, String album,
			String description, String style, long date, String songURL,
			String score, String num_votes) {
		super();
		this.songid = songid;
		this.username = username;
		this.song_name = song_name;
		this.album = album;
		this.description = description;
		this.style = style;
		this.date = date;
		this.songURL = songURL;
		this.score = score;
		this.num_votes = num_votes;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSong_name() {
		return song_name;
	}
	public void setSong_name(String song_name) {
		this.song_name = song_name;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getSongURL() {
		return songURL;
	}
	public void setSongURL(String songURL) {
		this.songURL = songURL;
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getNum_votes() {
		return num_votes;
	}
	public void setNum_votes(String num_votes) {
		this.num_votes = num_votes;
	}
	
	
}
