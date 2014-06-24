package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Song;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class MySongsActivity extends Activity implements AsyncResponse{

	private User user;
	private HashMap<String, String> songs = new HashMap<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_songs);
		setTitle("My songs");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		user = (User) this.getIntent().getExtras().get("user");
		loadSongsList(user);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_songs, menu);
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
		in.putExtra("whichPost", PublishStingActivity.SONG_POST);
		in.putExtra("contentMap", songs);
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
			View rootView = inflater.inflate(R.layout.fragment_my_songs,
					container, false);
			return rootView;
		}
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
	public void printContent(List<Object> contentList) {
		TableLayout table = (TableLayout) findViewById(R.id.songs);
		table.removeAllViews();
			//TODO: PINTAR BÉ ELS songs
			//mirar si m'arriba algun
			for(final Object obj : contentList){
				TableRow row=new TableRow(this);
				
				String songId= ((Song) obj).getSongid();
				String songName= ((Song) obj).getSong_name();				 
				songs.put(songId, songName);
				
				 TextView playlistName=new TextView(this);
				 playlistName.setText(""+songName+" ");
				 
				 TextView content=new TextView(this);
				 content.setText(""+((Song) obj).getDescription()+" ");
				 
				 TextView score=new TextView(this);
				 score.setText(""+((Song) obj).getScore()+" ");
				 
				 TextView style=new TextView(this);
				 style.setText(""+((Song) obj).getStyle()+" ");
				 
				 
				 
				 Button playSong = new Button(this);
				 playSong.setBackgroundResource(R.drawable.ic_action_play);
				 playSong.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		               play( ((Song)obj).getSongURL(), v);
		             }
		         });

				 
				 row.addView(playlistName);
				 row.addView(content);
				 row.addView(score);
				 row.addView(style);
				 row.addView(playSong);
				 table.addView(row);
			}
		
	}
	
	private boolean isPLAYING = false;
	private MediaPlayer mp;
	public void play(String url, final View view) {
	
		if (!isPLAYING) {
	        isPLAYING = true;
	        mp  = new MediaPlayer();
	        try {
	            mp.setDataSource(url);
	            mp.prepare();
	            mp.start();
	            mp.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						//quan acabi la canço posem el play
						Button playSong = (Button) view;
				        playSong.setBackgroundResource(R.drawable.ic_action_play);
						
					}
				});
	            //setejar la icona pause
	            Button playSong = (Button) view;
	            playSong.setBackgroundResource(R.drawable.ic_action_stop);
	        } catch (IOException e) {
	            System.out.print("Error on play --> my songs activity " + e.getMessage());
	        }
	    } else {
	        isPLAYING = false;
	        Button playSong = (Button) view;
	        playSong.setBackgroundResource(R.drawable.ic_action_play);
	        stopPlaying();
	    }
	}
	
	private void stopPlaying() {
	    mp.release();
	    mp = null;
	}


	private void loadSongsList(User user) {
		String urlString = MainActivity.BASE_URL +"songs/username/"+user.getUsername();
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.SONGS_OPERATION);
	}

	@Override
	public void goToStings() {
		// TODO Auto-generated method stub
		
	}

}
