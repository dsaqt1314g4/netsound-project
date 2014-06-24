package edu.upc.eetac.dsa.dsaqt1314g4.netsound.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.StingCollection;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.api.model.UserCollection;

@Path("/profile")
public class UserResource {
	@Context
	private SecurityContext security;
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	// Get de Users con Querys
		@GET
		@Produces(MediaType.NETSOUND_API_USER_COLLECTION)
		public UserCollection getSongs(@QueryParam("length") int length,
				@QueryParam("before") long before, @QueryParam("after") long after,
				@QueryParam("name") String name, @QueryParam("style") String style) {
			UserCollection users = new UserCollection();

			Connection conn = null;
			try {
				conn = ds.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			PreparedStatement stmt = null;
			try {
				stmt = conn.prepareStatement(buildGetUserQuery());
				stmt.setString(1, "%" + name + "%");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					User user = new User();
					user.setUsername(rs.getString("username"));
					user.setName(rs.getString("name"));
					user.setDescription(rs.getString("description"));
					users.addUser(user);
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
			System.out.println(users);
			return users;
		}

		private String buildGetUserQuery() {
				return "select * from users where username like ? ";

		}
	
	
	
	@Path("/{username}")
	@GET
	@Produces(MediaType.NETSOUND_API_USER)
	public Response getUser(@PathParam("username") String username,
			@Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();

		User user = getUserFromDatabase(username);

		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(user.getDate_create()));

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
		rb = Response.ok(user).cacheControl(cc).tag(eTag);

		return rb.build();
	}
	
	private User getUserFromDatabase(String username) {
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

		return "select * from users where username = ?";
	}

	@Path("/{username}/following")
	@GET
	@Produces(MediaType.NETSOUND_API_USER_COLLECTION)
	public UserCollection getFollowing(@PathParam("username") String username) {
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
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User();
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
		return "select u.* from users u, Follow f where f.followingname = u.username and f.followername = ?";
	}

	@Path("/{username}/follower")
	@GET
	@Produces(MediaType.NETSOUND_API_USER_COLLECTION)
	public UserCollection getFollower(@PathParam("username") String username) {
		UserCollection follower = new UserCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetFollowerById());
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				User user = new User();
				user.setUsername(rs.getString("username"));
				user.setName(rs.getString("name"));
				user.setDescription(rs.getString("description"));
				follower.addUser(user);
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

		return follower;
	}

	private String buildGetFollowerById() {
		return "select u.* from users u, Follow f where f.followername = u.username and f.followingname = ?";
	}
	
	
	
	
	@POST
	@Consumes(MediaType.NETSOUND_API_USER)
	@Produces(MediaType.NETSOUND_API_USER)
	public User createUser(User user) {
		validatePostUser(user);
		if(user.getUsername().equals(getUserFromDatabase(user.getUsername()).getUsername())){
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
				String username = rs.getString(1);
				user = getUserFromDatabase(username);
				stmt2 = conn.prepareStatement(buildCreateUserRole());
				stmt2.setString(1, user.getUsername());
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
		
		return "insert into user_roles values(?, 'registered')";
	}

	private void validatePostUser(User user) {
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

		return "insert into users (username, userpass, name, description, email) values (?, MD5(?), ?, ?, ?);";
	}
		
	@DELETE
	@Path("/{username}")
	public void deleteUser(@PathParam("username") String username) {
		validateUserDelete(username);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteUser();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
	
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no User with username="
						+ username);
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
	
	private String buildDeleteUser() {
		return "delete from users where username=?";
	}
	
	private void validateUserDelete(String username) {
		User currentUser = getUserFromDatabase(username);
		if (!security.getUserPrincipal().getName()
				.equals(currentUser.getUsername()))
			throw new ForbiddenException(
					"You are not allowed to modify this sting.");
	}
	
	@Path("/{username}/following")
	@POST
	@Consumes(MediaType.NETSOUND_API_USER)
	public User createFollower(@PathParam("username") String username, User user) {
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildCreateFollower());
			stmt.setString(1, user.getUsername());
			stmt.setString(2, username);
			stmt.executeUpdate();
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
	
	private String buildCreateFollower() {

		return "insert into Follow (followingname, followername) values (?, ?);";
	}
		
	
	@Path("/{username}/following/{following}")
	@DELETE
	public void deleteFollowing(@PathParam("username") String username, @PathParam("following") String following) {
		validateUserDelete(username);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
	
		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteFollow();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, following);
			int rows = stmt.executeUpdate();
			if (rows == 0)
				throw new NotFoundException("There's no User with username="
						+ username + " or " + following);
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
	
	private String buildDeleteFollow() {
		return "delete from Follow where followername = ? and followingname = ?";
	}
	
	//STINGS de USER
	
	@Path("/{username}/stings")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserStings(
			@PathParam("username") String username) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetUserIdStings(), username);
		return stings;

	}

	private String buildGetUserIdStings() {

		return "select s.*, u.username from Stings s,  users u where  u.username = s.username and s.username = ? order by last_modified desc";
	}

	@Path("/{username}/following/stings")
	@GET
	@Produces(MediaType.NETSOUND_API_STING_COLLECTION)
	public StingCollection getUserFollowingStings(
			@PathParam("username") String username) {
		StingCollection stings = new StingCollection();
		stings = getStingsFromDatabaseByQuery(buildGetFollowingStings(),
				username);
		return stings;

	}

	private String buildGetFollowingStings() {
		return "select s.*, u.username from Stings s, Follow f, users u where u.username=f.followingname and s.username=f.followingname and f.followername = ? order by last_modified desc";
	}

	private StingCollection getStingsFromDatabaseByQuery(String Query,
			String username) {
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
			stmt.setString(1, username);
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
	
	
	// Post de un string en una cancion
	@Path("/stings")
	@POST
	@Consumes(MediaType.NETSOUND_API_STING)
	@Produces(MediaType.NETSOUND_API_STING)
	public Sting createUserSting(Sting sting) {
		validateSting(sting);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildInsertSting();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(2, sting.getContent());
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);
				sting = getStingFromDatabaseByStingid(Integer.toString(stingid));
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
				sting.setUsername(rs.getString("username"));
				sting.setContent(rs.getString("content"));
				sting.setLastModified(rs.getTimestamp("last_modified")
						.getTime());
				oldestTimestamp = rs.getTimestamp("last_modified").getTime();
				sting.setLastModified(oldestTimestamp);
				
			}else {
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
	@Path("/stings/{stingid}")
	public void deleteSting(@PathParam("stingid") String stingid) {
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