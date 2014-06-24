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
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
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
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.glassfish.jersey.media.multipart.FormDataParam;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.PlaylistCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.SongCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.StingCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.User;

@Path("/songs")
public class SongResource {
	@Context
	private Application app;
	@Context
	private SecurityContext security;

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	// Get de todas las canciones con Querys
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
				song.setSongURL(app.getProperties().get("SongBaseURL")
						+ song.getSongid());
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

	// Get de todas las canciones de quien Sigo
	@Path("/following")
	@GET
	@Produces(MediaType.NETSOUND_API_SONG_COLLECTION)
	public SongCollection getSongsFollowing(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		SongCollection songs = new SongCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetSongsFollowingQuery(updateFromLast));
			stmt.setString(1, security.getUserPrincipal().getName());
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
				song.setSongURL(app.getProperties().get("SongBaseURL")
						+ song.getSongid());
				songs.addSong(song);
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

		return songs;
	}

	private String buildGetSongsFollowingQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select s.* from Songs s, Follow f where f.followername = ? and s.username = f.followingname and last_modified > ? order by last_modified desc";
		else
			return "select s.* from Songs s, Follow f where f.followername = ? and s.username = f.followingname and last_modified < ifnull(?, now())  order by last_modified desc limit ?";

	}
	
	// Get de todas las canciones de un usuario
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
			stmt = conn
					.prepareStatement(buildGetSongsQueryByUsername(updateFromLast));
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
				song.setSongURL(app.getProperties().get("SongBaseURL")
						+ song.getSongid());
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

	// Get de una cancion en concreto
	@GET
	@Path("/{songid}")
	@Produces(MediaType.NETSOUND_API_SONG)
	public Response getSong(@PathParam("songid") String songid,
			@Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();

		Song song = getSongFromDatabase(songid);

		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(song.getDate()));

		// Verify if it matched with etag available in http request
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(song).cacheControl(cc).tag(eTag);

		return rb.build();

	}

	// Funcion para devolver un cancion segun su id
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
			stmt.setString(1, songid);
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
				song.setSongURL(app.getProperties().get("SongBaseURL")
						+ song.getSongid());
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



	@POST
	@Produces(MediaType.NETSOUND_API_SONG)
	public Song uploadSong(@FormDataParam("songfile") InputStream file,
			@FormDataParam("song") String Song_name,
			@FormDataParam("album") String Album,
			@FormDataParam("description") String Description,
			@FormDataParam("style") String Style) {
		Song song = new Song();
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
			stmt = conn.prepareStatement(buildPostSong(),
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, uuid.toString());
			stmt.setString(2, security.getUserPrincipal().getName());
			stmt.setString(3, Song_name);
			stmt.setString(4, Album);
			stmt.setString(5, Description);
			stmt.setString(6, Style);
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
				conn = ds.getConnection();
			} catch (SQLException e) {
				throw new ServerErrorException("Could not connect to the database",
						Response.Status.SERVICE_UNAVAILABLE);
			}
		}

			return song;
		}

		private String buildPostSong() {
			return "insert into Songs (songid,username, song_name, album_name, description, style, score, num_votes) value (?,?,?,?,?,?,?,?)";
		}

		private UUID writeSong(InputStream file) {
			UUID uuid = UUID.randomUUID();
			String filename = uuid.toString() + ".mp3";

			DataInputStream dis = new DataInputStream(file);
			
			try {

				DataOutputStream dos = new DataOutputStream(new FileOutputStream(
						app.getProperties().get("uploadFolder") + filename));
				System.out.println(app.getProperties().get("uploadFolder") + filename);
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

			System.out.println(uuid);
			return uuid;
		}

	// Put para puntuar una cancion
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
		PreparedStatement stmt2 = null;
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
			res_score = ((Double.valueOf(song.getScore()) + Integer
					.valueOf(score))) / votantes;
			stmt2 = conn.prepareStatement(buildUpdateValoracion());
			stmt2.setDouble(1, res_score);
			stmt2.setInt(2, votantes);
			stmt2.setString(3, songid);
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
				if (stmt2 != null)
					stmt2.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return song;
	}

	private String buildUpdateValoracion() {
		return "update Songs set score= ?, num_votes = ? where songid=?";
	}

	@DELETE
	@Path("/{songid}")
	public void deleteSong(@PathParam("songid") String songid) {
		validateUserDelete(songid);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteSong();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, songid);
	
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no Song with songid="
						+ songid);
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
	}
	
	private String buildDeleteSong() {
		return "delete from Songs where songid=?";
	}
	
	private void validateUserDelete(String songid) {
		Song currentSong = getSongFromDatabase(songid);
		if (!security.getUserPrincipal().getName()
				.equals(currentSong.getUsername()))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
	
	// STINGS DE SONG

	// Get de todos los stings de una cancion
	@Path("/{songid}/stings")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getSongStings(@PathParam("songid") String songid) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseBySongid(songid);
		return stings;

	}

	private StingCollection getStingsFromDatabaseBySongid(String songid) {
		StingCollection stings = new StingCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetStingsBySongid());
			stmt.setString(1, songid);
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Sting sting = new Sting();
				sting.setUsername(rs.getString("username"));
				sting.setContent(rs.getString("content"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());
				oldestTimestamp = rs.getTimestamp("last_modified").getTime();
				sting.setLastModified(oldestTimestamp);
				if (first) {
					first = false;
					stings.setNewestTimestamp(sting.getLastModified());
				}
				stings.addSting(sting);
			}
			stings.setOldestTimestamp(oldestTimestamp);
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
		return stings;
	}

	private String buildGetStingsBySongid() {

		return "select s.* from Stings s, Stings_Song ss where s.stingid= ss.stingid and ss.songid = ?   order by last_modified desc";
	}

	// Post de un string en una cancion
	@Path("/{songid}/stings")
	@POST
	@Consumes(MediaType.NETSOUND_API_STING)
	@Produces(MediaType.NETSOUND_API_STING)
	public Sting createSongSting(@PathParam("songid") String songid, Sting sting) {
		validateSting(sting);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		PreparedStatement stmt2 = null;
		try {
			String sql = buildInsertSting();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			try{
				stmt.setString(1, security.getUserPrincipal().getName());
			}catch(NullPointerException e){
				stmt.setString(1, sting.getUsername());
			}
			
			stmt.setString(2, sting.getContent());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);

				sting = getStingFromDatabaseByStingid(Integer.toString(stingid));
			} else {
				// Something has failed...
			}
			stmt2 = conn.prepareStatement(buildInsertSongSting());
			stmt2.setInt(1, Integer.valueOf(sting.getStingid()));
			stmt2.setString(2, songid);
			stmt2.executeUpdate();
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (stmt2 != null)
					stmt2.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return sting;
	}

	private void validateSting(Sting sting) {
		if (sting.getContent() == null)
			throw new BadRequestException("Content can't be null.");
		if (sting.getContent().length() > 500)
			throw new BadRequestException(
					"Content can't be greater than 500 characters.");
	}

	private String buildInsertSting() {
		return "insert into Stings (username, content) value (?, ?)";
	}

	private String buildInsertSongSting() {
		return "insert into Stings_Song (stingid, songid) value (?, ?)";
	}

	private Sting getStingFromDatabaseByStingid(String stingid) {
		Sting sting = new Sting();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetStings());
			stmt.setInt(1, Integer.valueOf(stingid));
			ResultSet rs = stmt.executeQuery();
			long oldestTimestamp = 0;
			if (rs.next()) {
				sting.setStingid(stingid);
				sting.setUsername(rs.getString("username"));
				sting.setContent(rs.getString("content"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());
				oldestTimestamp = rs.getTimestamp("last_modified").getTime();
				sting.setLastModified(oldestTimestamp);

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
		return sting;
	}

	private String buildGetStings() {

		return "select * from Stings where stingid= ?";
	}
	
	@DELETE
	@Path("/{songid}/stings/{stingid}")
	public void deleteSting(@PathParam("songid") String songid, @PathParam("stingid") String stingid) {
		validateUserSting(stingid);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteSting();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(stingid));
	
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no sting with stingid="
						+ stingid);
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
	}
	
	private String buildDeleteSting() {
		return "delete from Stings where stingid=?";
	}
	
	private void validateUserSting(String stingid) {
		Sting currentSting = getStingFromDatabaseByStingid(stingid);
		if (!security.getUserPrincipal().getName()
				.equals(currentSting.getUsername()))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}

}
