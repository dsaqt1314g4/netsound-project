package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.MediaType;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.SongResource;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.UserResource;

public class Song {
	@InjectLinks({
		@InjectLink(resource = SongResource.class, style = Style.ABSOLUTE, rel = "self", title = "Songpage", type = MediaType.NETSOUND_API_SONG, method = "getSong", bindings = @Binding(name = "songid", value = "${instance.songid}")),
		@InjectLink(resource = SongResource.class, style = Style.ABSOLUTE, rel = "stings", title = "SongStings", type = MediaType.NETSOUND_API_STING, method = "getSongStings", bindings = @Binding(name = "songid", value = "${instance.songid}")),
		@InjectLink(resource = SongResource.class, style = Style.ABSOLUTE, rel = "create-sting", title = "Create Song Sting", type = MediaType.NETSOUND_API_STING, method = "createSongSting", bindings = @Binding(name = "songid", value = "${instance.songid}")),
		@InjectLink(resource = SongResource.class, style = Style.ABSOLUTE, rel = "delete-song", title = "Delete Song", type = MediaType.NETSOUND_API_SONG, method = "deleteSong", bindings = @Binding(name = "songid", value = "${instance.songid}")),
		})
	
	private List<Link> links;
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
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getSongid() {
		return songid;
	}
	public void setSongid(String songid) {
		this.songid = songid;
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
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getSongURL() {
		return songURL;
	}
	public void setSongURL(String songURL) {
		this.songURL = songURL;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNum_votes() {
		return num_votes;
	}
	public void setNum_votes(String num_votes) {
		this.num_votes = num_votes;
	}
	
	
}
