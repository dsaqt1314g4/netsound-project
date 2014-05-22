package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

public class PlaylistCollection {
	
	private List<Link> links;
	private List<Playlist> playlists;
	private long newestTimestamp;
	private long oldestTimestamp;
	
	public PlaylistCollection() {
		super();
		playlists = new ArrayList<>();
	}
	
	public void addPlaylist(Playlist playlist) {
		playlists.add(playlist);
	}
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Playlist> getPlaylist() {
		return playlists;
	}
	public void setPlaylist(List<Playlist> playlist) {
		this.playlists = playlist;
	}
	public long getNewestTimestamp() {
		return newestTimestamp;
	}
	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}
	public long getOldestTimestamp() {
		return oldestTimestamp;
	}
	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}
	

}
