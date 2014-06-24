package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;

public class CallAPI extends AsyncTask<String, String, String> {
	 
	
	public static String LOGIN_OPERATION = "0";
	public static String STINGS_OPERATION = "1";
	public static String PLAYLIST_OPERATION = "2";
	public static String SONGS_OPERATION = "3";
	public static String FOLLOWER_OPERATION = "4";
	public static String FOLLOWINGS_OPERATION = "5";
	
	public User user = null;
	public List<Object> contentList = null;
	public AsyncResponse callback=null;	
	
	public CallAPI(AsyncResponse callback) {
		super();
		this.callback = callback;
	}

	@Override
	protected String doInBackground(String... params) {

		 String urlString=params[0]; // URL to call
		 String basic=params[1]; // Basic
		 String whichOperation=params[2]; // operation
		 
		   
		 InputStream in = null;
		 JSONObject json;
		 // HTTP Get
		 try {
		  		 
			 URL myURL = new URL(urlString);
			 HttpURLConnection myURLConnection = (HttpURLConnection)myURL.openConnection();		
			 myURLConnection.setRequestProperty ("Authorization", basic);
			 myURLConnection.setRequestMethod("GET");
			 myURLConnection.setRequestProperty("Content-Language", "es-ES");
			 myURLConnection.setUseCaches(false);
			 myURLConnection.setDoInput(true);
			 in = myURLConnection.getInputStream();
			 BufferedReader rd = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));
		     String jsonText = readAll(rd);
		     json = new JSONObject(jsonText);
		     
		     if(whichOperation.equalsIgnoreCase(LOGIN_OPERATION)){
		    	 user = parseUserJson(json);
			     user.setToken(basic);
		     }
		     else if(whichOperation.equalsIgnoreCase(STINGS_OPERATION)){
		    	 contentList = parseStingJson(json);
		     }
		     else if(whichOperation.equalsIgnoreCase(PLAYLIST_OPERATION)){
		    	 contentList = parsePlayListJson(json);
		     }
		     else if(whichOperation.equalsIgnoreCase(SONGS_OPERATION)){
		    	 contentList = parseSongsJson(json);
		     }
		     else if(whichOperation.equalsIgnoreCase(FOLLOWER_OPERATION)){
		    	 contentList = parseFollowersJson(json);
		     }
		     else if(whichOperation.equalsIgnoreCase(FOLLOWINGS_OPERATION)){
		    	 contentList = parseFollowingsJson(json);
		     }
			 	  
		 } catch (Exception e ) {		  
			 System.out.println(e.getMessage());		  			 		  
		 } finally {
			 try { in.close(); } 
		     catch (Exception e) {
		    	
		     };	
		 }
		 		 			
		 return whichOperation; 
	}
	
	
	private List<Object> parseFollowersJson(JSONObject json) {
		List<Object> users = null;
		try {
			 users = new ArrayList<>();
			JSONArray array = json.getJSONArray("users");
			for(int i=0; i<array.length();i++){
				JSONObject following = array.getJSONObject(i);
				String description = (String) following.get("description");
				String name = (String) following.get("name");
				String username = (String) following.get("username");
								
				User us = new User(username, name, description, "", "", System.currentTimeMillis());
				us.setMyFollower(true);
				users.add(us);
			}
			return users;
			
		} catch (JSONException e) {
			return users;
		}	
	}

	private List<Object> parseFollowingsJson(JSONObject json) {
		// {"users":[{"date_create":0,"description":"I am batman","name":"Ferran","username":"ferran.diaz"}]}
		List<Object> users = null;
		try {
			 users = new ArrayList<>();
			JSONArray array = json.getJSONArray("users");
			for(int i=0; i<array.length();i++){
				JSONObject following = array.getJSONObject(i);
				String description = (String) following.get("description");
				String name = (String) following.get("name");
				String username = (String) following.get("username");
								
				User us = new User(username, name, description, "", "", System.currentTimeMillis());
				us.setMyFollower(false);
				users.add(us);
			}
			return users;
			
		} catch (JSONException e) {
			return users;
		}	
	}

	private List<Object> parseSongsJson(JSONObject json) {
		try {
			List<Object> songs = new ArrayList<>();
			JSONArray array = json.getJSONArray("songs");
			for(int i=0; i<array.length();i++){
				JSONObject sting = array.getJSONObject(i);
				String album = (String) sting.get("album");
				String description = (String) sting.get("description");
				String score = (String) sting.get("score");
				String songURL = (String) sting.get("songURL");
				String song_name = (String) sting.get("song_name");
				String songid = (String) sting.get("songid");
				String style = (String) sting.get("style");
				String username = (String) sting.get("username");				
				songs.add(new Song(songid, username, song_name, album, description, style, new Date().getTime(), songURL,score, "0"));
			}
			return songs;
			
		} catch (JSONException e) {
			return null;
		}	
	
	}

	private List<Object> parsePlayListJson(JSONObject json) {
		try {
			List<Object> playlist = new ArrayList<>();
			JSONArray array = json.getJSONArray("playlist");
			for(int i=0; i<array.length();i++){
				JSONObject playlistItem = array.getJSONObject(i);
				String description = (String) playlistItem.get("description");
				String playlist_name = (String) playlistItem.get("playlist_name");
				long lastModified = (int) playlistItem.get("lastModified");
				String playlistid = (String) playlistItem.get("playlistid");
				String score = (String) playlistItem.get("score");
				String style = (String) playlistItem.get("style");
				String username = (String) playlistItem.get("username");
				playlist.add(new Playlist(playlistid, username, playlist_name, description, style, lastModified, score, "0"));
			}
			return playlist;
			
		} catch (JSONException e) {
			return null;
		}	
		
		
	}

	private List<Object> parseStingJson(JSONObject json) {
		try {
			List<Object> stings = new ArrayList<>();
			JSONArray array = json.getJSONArray("stings");
			for(int i=0; i<array.length();i++){
				JSONObject sting = array.getJSONObject(i);
				String username = (String) sting.get("username");
				String content = (String) sting.get("content");
				long lastModified = (long) sting.get("lastModified");				
				stings.add(new Sting(null, content, username, lastModified));
			}
			return stings;
			
		} catch (JSONException e) {
			return null;
		}		
	}

	private User parseUserJson(JSONObject json){
		try {						
			String username = (String) json.get("username");
			String name = (String) json.get("name");
			String description = (String) json.get("description");
			//Long date_create = (Long) json.get("date_create");
			return new User(username, name, description, null, null, 0);		
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	  }

	  public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
	    InputStream is = new URL(url).openStream();
	    try {
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	      String jsonText = readAll(rd);
	      JSONObject json = new JSONObject(jsonText);
	      return json;
	    } finally {
	      is.close();
	    }
	  }
	
	
	protected void onPostExecute(String operation) {
		
		if(operation.equalsIgnoreCase(LOGIN_OPERATION)){
			if(user!=null){
				callback.goToHome(user);
			}
			else{
				callback.printError("error on login");
			}
		}
		else if(contentList!=null && !contentList.isEmpty()){
			callback.printContent(contentList);
		}
		
		
	}
} // end CallAPI 
