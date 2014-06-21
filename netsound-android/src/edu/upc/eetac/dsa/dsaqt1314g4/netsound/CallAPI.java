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
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;

public class CallAPI extends AsyncTask<String, String, String> {
	 
	public static String LOGIN_OPERATION = "0";
	public static String SIGNUP_OPERATION = "1";
	public static String STINGS_OPERATION = "2";
	
	public User user = null;
	public List<Sting> stingList = null;
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
		    	 stingList = parseStingJson(json);
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
	
	
	private List<Sting> parseStingJson(JSONObject json) {
		try {
			List<Sting> stings = new ArrayList();
			JSONArray array = json.getJSONArray("stings");
			for(int i=0; i<array.length();i++){
				JSONObject sting = array.getJSONObject(i);
				String username = (String) sting.get("username");
				String content = (String) sting.get("content");
				long lastModified = (Long) sting.get("lastModified");				
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
		else if(operation.equalsIgnoreCase(STINGS_OPERATION)){
			callback.printStings(stingList);
		}
		
		
	}
} // end CallAPI 
