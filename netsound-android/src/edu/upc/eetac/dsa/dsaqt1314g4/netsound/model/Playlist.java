package edu.upc.eetac.dsa.dsaqt1314g4.netsound.model;

public class Playlist {

	private String playlistid;
	private String username;
	private String playlist_name;
	private String description;
	private String style;
	private long lastModified;
	private String score;
	private String num_votes;
	
	
	
	public Playlist(String playlistid, String username, String playlist_name,
			String description, String style, long lastModified, String score,
			String num_votes) {
		super();
		this.playlistid = playlistid;
		this.username = username;
		this.playlist_name = playlist_name;
		this.description = description;
		this.style = style;
		this.lastModified = lastModified;
		this.score = score;
		this.num_votes = num_votes;
	}
	public String getPlaylistid() {
		return playlistid;
	}
	public void setPlaylistid(String playlistid) {
		this.playlistid = playlistid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPlaylist_name() {
		return playlist_name;
	}
	public void setPlaylist_name(String playlist_name) {
		this.playlist_name = playlist_name;
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
	public long getLastModified() {
		return lastModified;
	}
	public void setLastModified(long lastModified) {
		this.lastModified = lastModified;
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
