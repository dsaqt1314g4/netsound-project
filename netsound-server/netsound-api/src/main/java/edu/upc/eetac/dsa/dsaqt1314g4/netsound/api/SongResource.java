package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.UUID;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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

import org.glassfish.jersey.media.multipart.FormDataParam;

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
			@QueryParam("name") String name, @QueryParam("style") String style) {
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
			boolean updateName = name != null;
			boolean updateStyle = style != null;
			stmt = conn.prepareStatement(buildGetSongsQuery(updateFromLast));
			if (updateName) {
				stmt.setString(1, "%" + name + "%");
			} else {
				stmt.setString(1, "%%");
			}

			if (updateStyle) {
				stmt.setString(2, "%" + style + "%");
			} else {
				stmt.setString(2, "%%");
			}

			if (updateFromLast) {
				stmt.setTimestamp(3, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(3, new Timestamp(before));
				else
					stmt.setTimestamp(3, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(4, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Song song = new Song();
				song.setSongid(rs.getString("songid"));
				song.setUsername(rs.getString("username"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setScore(rs.getString("score"));
				song.setSongURL(app.getProperties().get("imgBaseURL") + song.getSongid());
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

	private String buildGetSongsQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select * from Songs where song_name like ? and style like ? and last_modified > ? order by last_modified desc";
		else
			return "select * from Songs where song_name like ? and style like ? and last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}

	@GET
	@Path("/username/{username}")
	@Produces(MediaType.NETSOUND_API_SONG_COLLECTION)
	public SongCollection getSongsByUsername(
			@PathParam("username") String username,
			@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
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
			stmt = conn.prepareStatement(buildGetSongsQueryByUsername(updateFromLast));
			stmt.setString(1, username);
			if (updateFromLast) {
				stmt.setTimestamp(2, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(2, new Timestamp(before));
				else
					stmt.setTimestamp(2, null);
				length = (length <= 0) ? 20 : length;
				stmt.setInt(3, length);
			}
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Song song = new Song();
				song.setSongid(rs.getString("songid"));
				song.setUsername(rs.getString("username"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setScore(rs.getString("score"));
				song.setSongURL(app.getProperties().get("imgBaseURL") + song.getSongid());
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

	private String buildGetSongsQueryByUsername(boolean updateFromLast) {
		if (updateFromLast)
			return "select * from Songs where username = ? and last_modified > ? order by last_modified desc";
		else
			return "select * from Songs where username = ? and last_modified < ifnull(?, now())  order by last_modified desc limit ?";

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
				song.setUsername(rs.getString("username"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setScore(rs.getString("score"));
				song.setSongURL(app.getProperties().get("imgBaseURL") + song.getSongid());
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

	private String buildGetSongById() {
		return "select * from Songs where songid = ?";
	}

	// @Consumes(MediaType.NETSOUND_API_SONG)
	@POST
	@Produces(MediaType.NETSOUND_API_SONG)
	public Song uploadSong(@FormDataParam("song") InputStream file) {// Song
																		// song)
																		// {
		Song song = new Song();
		song.setAlbum("LOLO");
		song.setSong_name("LOLO");
		song.setDescription("kñljkljl jkljl");
		song.setStyle("LOLO");
		UUID uuid = writeSong(file);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn
					.prepareStatement(
							"insert into Songs (songid,username, song_name, album_name, description, style, score, num_votes) value (?,?,?,?,?,?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, uuid.toString());
			stmt.setString(2, "alejandro.jimenez");// System.out.println(security.getUserPrincipal().getName());
			stmt.setString(3, song.getSong_name());
			stmt.setString(4, song.getAlbum());
			stmt.setString(5, song.getDescription());
			stmt.setString(6, song.getStyle());
			stmt.setInt(7, 0);
			stmt.setInt(8, 0);
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String songid = rs.getString(0);

				song = getSongFromDatabase(songid);
				song.setSongURL(app.getProperties().get("") + song.getSongid());
			} else {
				// Something has failed...
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

	private UUID writeSong(InputStream file) {
		UUID uuid = UUID.randomUUID();
		String filename = uuid.toString() + ".mp3";
		DataInputStream dis = new DataInputStream(file);
		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(
					app.getProperties().get("uploadFolder") + filename));

			byte[] buffer = new byte[1024];
			int readed = 0;
			while ((readed = dis.read(buffer)) != -1) {
				dos.write(buffer, 0, readed);
			}

			dis.close();
			dos.close();
		} catch (FileNotFoundException e1) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		} catch (IOException e) {
			throw new InternalServerErrorException(
					"Something has been wrong when converting the file.");
		}

		return uuid;
	}

	@PUT
	@Path("/{songid}")
	@Consumes(MediaType.NETSOUND_API_SONG)
	@Produces(MediaType.NETSOUND_API_SONG)
	public Song updateSong(@PathParam("songid") String songid, Song song,
			@FormDataParam("score") int score) {
		// validateUser(stingid);
		// validateUpdateSting(sting);
		Connection conn = null;
		int votantes;
		double res_score;
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
				song.setUsername(rs.getString("username"));
				song.setSong_name(rs.getString("song_name"));
				song.setAlbum(rs.getString("album_name"));
				song.setDescription(rs.getString("description"));
				song.setStyle(rs.getString("style"));
				song.setDate(rs.getTimestamp("last_modified").getTime());
				song.setScore(rs.getString("score"));
				song.setNum_votes(rs.getString("num_votes"));
			} else {
				throw new NotFoundException("There's no Song with songid="
						+ songid);
			}

			votantes = Integer.valueOf(song.getNum_votes()) + 1;
			res_score = (Integer.valueOf(song.getScore()
					+ Integer.valueOf(score)))
					/ votantes;
			stmt = conn.prepareStatement(buildUpdateSting());
			stmt.setDouble(1, res_score);
			stmt.setInt(2, votantes);
			stmt.setString(3, songid);
			int rows = stmt.executeUpdate();

			if (rows == 1)
				song = getSongFromDatabase(songid);
			else {
				throw new NotFoundException("There's no sting with songid="
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

	private String buildUpdateSting() {
		return "update Songs set score= ?, num_votes = ? where stingid=?";
	}
}
