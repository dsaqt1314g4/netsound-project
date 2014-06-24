package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Playlist;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class MyPlaylistActivity extends Activity implements AsyncResponse {

	private User user;
	private HashMap<String, String> playlists = new HashMap<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_playlist);
		setTitle("My playlists");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		user = (User) this.getIntent().getExtras().get("user");
		loadPlaylists(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_playlist, menu);
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
	    case R.id.action_logout:
	        Utils.logout(this);
	        return true;
	    case R.id.action_publish:
	    	publish();
	        return true;
	    case android.R.id.home:
			Intent i = new Intent(getApplicationContext(), HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			i.putExtra("user", user);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void publish() {
		Intent in = new Intent(getApplicationContext(), PublishStingActivity.class);			
		in.putExtra("user", user);
		in.putExtra("whichPost", PublishStingActivity.PLAYLIST_POST);
		in.putExtra("contentMap", playlists);
		startActivity(in);
	}
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_my_playlist,
					container, false);
			return rootView;
		}
	}
	
	private void loadPlaylists(User user) {
		String urlString = MainActivity.BASE_URL +"playlist/username/"+user.getUsername();
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.PLAYLIST_OPERATION);
	}

	@Override
	public void goToHome(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToLogin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printContent(List<Object> playlist) {
		TableLayout table = (TableLayout) findViewById(R.id.playlists);
		table.removeAllViews();
			//TODO: PINTAR BÉ ELS playlists
			//mirar si m'arriba algun
			for(Object obj : playlist){
				TableRow row=new TableRow(this);
				
				String playlistId= ((Playlist) obj).getPlaylistid();
				String playlistName= ((Playlist) obj).getPlaylist_name();				 
				playlists.put(playlistId, playlistName);
				 
				 TextView playlistNameText=new TextView(this);
				 playlistNameText.setText("Playlist name: "+((Playlist) obj).getPlaylist_name()+" ");
				 
				 TextView content=new TextView(this);
				 content.setText("Description: "+((Playlist) obj).getDescription()+ System.getProperty ("line.separator"));
				 
				 TextView score=new TextView(this);
				 score.setText("Score: "+((Playlist) obj).getScore()+" ");
				 
				 TextView style=new TextView(this);
				 style.setText("Style: "+((Playlist) obj).getStyle()+ System.getProperty ("line.separator")+ System.getProperty ("line.separator"));
				 
				 row.addView(playlistNameText);
				 row.addView(content);
				 row.addView(score);
				 row.addView(style);
				 table.addView(row);
			}
		
	}

	@Override
	public void goToStings() {
		// TODO Auto-generated method stub
		
	}

}
