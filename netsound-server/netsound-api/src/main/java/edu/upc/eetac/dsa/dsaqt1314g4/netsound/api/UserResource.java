package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.StingCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.UserCollection;

@Path("/profile")
public class UserResource {

	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@Path("/{profileid}")
	@GET
	@Produces(MediaType.NETSOUND_API_USER)
	public User getUser(@PathParam("profileid") String profileid) {
		User user = new User();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetUserById());
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user.setUserid(String.valueOf(rs.getInt("userid")));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
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

		return user;
	}

	private String buildGetUserById() {

		return "select * from Users where userid = ?";
	}
	@Path("/username/{username}")
	@GET
	@Produces(MediaType.NETSOUND_API_USER)
	public User getUserbyUsername(@PathParam("username") String username) {
		User user = new User();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetUserByUsername());
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				user.setUserid(String.valueOf(rs.getInt("userid")));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
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

		return user;
	}

	private String buildGetUserByUsername() {

		return "select * from Users where username = ?";
	}

	@Path("/{profileid}/following")
	@GET
	@Produces(MediaType.NETSOUND_API_USER_COLLECTION)
	public UserCollection getFollowing(@PathParam("profileid") String profileid) {
		UserCollection following = new UserCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetFollowingById());
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUserid(String.valueOf(rs.getInt("userid")));
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
				following.addUser(user);
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

		return following;
	}

	private String buildGetFollowingById() {
		return "select u.* from Users u, follow f where f.followingid = u.userid and f.followerid = ?";
	}

	
	@Path("/{profileid}/stings")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserStings(
			@PathParam("profileid") String profileid) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetUserIdStings(), profileid);
		return stings;

	}

	private String buildGetUserIdStings() {

		return "select s.*, u.username from Stings s,  Users u where  u.userid = s.userid and s.userid = ? order by last_modified desc";
	}

	@Path("/{profileid}/following/stings")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserFollowingStings(
			@PathParam("profileid") String profileid) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetFollowingStings(),
				profileid);
		return stings;

	}

	private String buildGetFollowingStings() {

		return "select s.*, u.username from Stings s, Follow f, Users u where u.userid=f.followingid and s.userid=f.followingid and f.followerid = ? order by last_modified desc";
	}

	private StingCollection getStingsFromDatabaseByQuery(String Query,
			String profileid) {
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
			stmt = conn.prepareStatement(Query);
			stmt.setInt(1, Integer.valueOf(profileid));
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Sting sting = new Sting();
				sting.setStingid(String.valueOf(rs.getInt("stingid")));
				sting.setUserid(String.valueOf(rs.getInt("userid")));
				sting.setContent(rs.getString("content"));
				sting.setUsername(rs.getString("username"));
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
	
	@POST
	@Consumes(MediaType.NETSOUND_API_USER)
	@Produces(MediaType.NETSOUND_API_USER)
	public User createUser(User user) {
		validateUser(user);
		if(user.getUsername().equals(getUserbyUsername(user.getUsername()).getUsername())){
			throw new BadRequestException("There is already a user with this Username");
		}
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
			stmt = conn.prepareStatement(buildCreateUser(),
					Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, user.getUsername());
			stmt.setString(2, user.getUserpass());
			stmt.setString(3, user.getName());
			if (user.getDescription() == null) {
				user.setDescription("I'm always tired, because I'm a superhero at night");
			}
			stmt.setString(4, user.getDescription());
			stmt.setString(5, user.getEmail());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				String userid = String.valueOf(rs.getInt(1));
				user = getUser(userid);
				stmt2 = conn.prepareStatement(buildCreateUserRole());
				stmt2.setInt(1, Integer.valueOf(user.getUserid()));
				stmt2.executeUpdate();
			} else {

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

		return user;

	}


	private String buildCreateUserRole() {
		
		return "insert into User_roles values(?, 'registered')";
	}

	private void validateUser(User user) {
		if(user.getUsername() == null)
			throw new BadRequestException ("Username can't be null");
		if(user.getName() == null)
			throw new BadRequestException ("Name can't be null");
		if(user.getUserpass() == null)
			throw new BadRequestException ("Password can't be null");
		if(user.getEmail() == null)
			throw new BadRequestException ("Email can't be null");

	}

	private String buildCreateUser() {

		return "insert into Users (username, userpass, name, description, email) values (?, MD5(?), ?, ?, ?);";
	}

}
