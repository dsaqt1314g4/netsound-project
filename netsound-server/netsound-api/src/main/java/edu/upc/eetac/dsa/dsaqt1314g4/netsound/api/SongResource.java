package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.SongCollection;





@Path("/songs")
public class SongResource {
	@Context
	private Application app;
	private SecurityContext security;
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();
	
	@GET
	@Produces(MediaType.NETSOUND_API_SONG_COLLECTION)
	public SongCollection getSongs(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after,
			@QueryParam("search") String search) {
		SongCollection songs = new SongCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			boolean updateSearch = search != null;
			stmt = conn.prepareStatement(buildGetSongsQuery(updateFromLast,
					updateSearch));
			if (updateSearch){ 
				stmt.setString(1, "%" + search + "%");
			}
			else if (updateFromLast) {
				stmt.setTimestamp(1, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(1, new Timestamp(before));
				else
					stmt.setTimestamp(1, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(2, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Song song = new Song();
				song.setSongid(rs.getString("songid"));
				song.setUserid(rs.getString("userid"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setSongbin(rs.getString("songbin"));
				song.setScore(rs.getString("score"));
				songs.addSong(song);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return songs;
	}
	
	private String buildGetSongsQuery(boolean updateFromLast,
			boolean updateSearch) {
		if (updateSearch)
			return "select * from Songs where song_name like ? order by score desc";
		else if (updateFromLast)
			return "select * from Songs where last_modified > ? order by last_modified desc";
		else
			return "select * from Songs where last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}
	
	@GET
	@Path("/{songid}")
	@Produces(MediaType.NETSOUND_API_SONG)
	public Song getSong(@PathParam("songid") String songid,
			@Context Request request) {
		Song song = new Song();
		song = getSongFromDatabase(songid);
		return song;
	}

	private Song getSongFromDatabase(String songid) {
		Song song = new Song();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetSongById());
			stmt.setInt(1, Integer.valueOf(songid));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				song.setSongid(rs.getString("songid"));
				song.setUserid(rs.getString("userid"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setSongbin(rs.getString("songbin"));
				song.setScore(rs.getString("score"));
			} else {
				throw new NotFoundException("There's no Song with songid="
						+ songid);
			}

		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return song;
	}
	
	private String buildGetSongById(){
		return "select * from Songs where songid = ?";
	}
	
	
	
	//Aqui va el POST
	/*
	private UUID writeSong(InputStream file) {
		AudioInputStream inputStream = null;
		try {
			inputStream = AudioSystem.getAudioInputStream(file);

		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when reading the file.");
		}
		UUID uuid = UUID.randomUUID();
		String filename = uuid.toString() + ".mp3";
		try {
			AudioSystem.write(inputStream, "ogg", new File(app.getProperties().get("uploadFolder") + filename));
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}

		return uuid;
	}
	*/
	
	
}
