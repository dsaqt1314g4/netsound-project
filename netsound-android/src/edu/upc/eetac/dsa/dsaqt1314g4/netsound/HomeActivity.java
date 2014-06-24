package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

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
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class HomeActivity extends Activity implements AsyncResponse{

	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle("My home");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		TextView helloText = (TextView)findViewById(R.id.username);

		//in your OnCreate() method
		user = (User) this.getIntent().getExtras().get("user");
		helloText.setText("Hello "+user.getName());
		
		loadStings(user);		
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		
		
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
		    case R.id.action_refresh:
		    	loadStings(user);
		        return true;
		    case R.id.action_my_stings:
		    	goToMyStings(user);
		        return true;
		    case R.id.action_my_songs:
		    	goToMySongs(user);
		        return true;
		    case R.id.action_my_playlists:
		    	goToMyPlaylist(user);
		        return true;
		    case R.id.action_social:
		    	goToSocial(user);
		        return true;
//		    default:
//		        return super.onOptionsItemSelected(item);
		    }
		return super.onOptionsItemSelected(item);
	}

	private void goToSocial(User user2) {
		Intent intent = new Intent(getApplicationContext(), SocialActivity.class); 
		intent.putExtra("user", user);
		startActivity(intent);
		
	}



	private void goToMyPlaylist(User user) {
		Intent intent = new Intent(getApplicationContext(), MyPlaylistActivity.class); 
		intent.putExtra("user", user);
		  startActivity(intent);
		
	}



	private void goToMySongs(User user) {
		Intent intent = new Intent(getApplicationContext(), MySongsActivity.class); 
		intent.putExtra("user", user);
		  startActivity(intent);
		
	}



	private void loadStings(User user) {
		String urlString = MainActivity.BASE_URL +"profile/"+user.getUsername()+"/following/stings";
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.STINGS_OPERATION);
	}
	
	private void goToMyStings(User user) {
		Intent intent = new Intent(getApplicationContext(), MyStingsActivity.class); 
		intent.putExtra("user", user);
		  startActivity(intent);
		
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
			View rootView = inflater.inflate(R.layout.fragment_home, container,
					false);
			return rootView;
		}
	}

	@Override
	public void goToHome(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printError(String error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void printContent(List<Object> stingList) {
		
		TableLayout table = (TableLayout) findViewById(R.id.stings);
		table.removeAllViews();
			//TODO: PINTAR BÉ ELS STINGS
			//mirar si m'arriba algun
			for(Object obj : stingList){
				TableRow row=new TableRow(this);
				 TextView username=new TextView(this);
				 username.setText(""+((Sting) obj).getUsername());
				 
				 TextView content=new TextView(this);
				 content.setText(""+((Sting) obj).getContent());
				 
				 row.addView(username);
				 row.addView(content);
				 table.addView(row);
			}
			
//			for(int j=0; j<stingList.size();j++){
//				Sting obj = stingList.get(j);
//			}
		
	}
	
	

	@Override
	public void goToLogin() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void goToStings() {
		// TODO Auto-generated method stub
		
	}
	

}
