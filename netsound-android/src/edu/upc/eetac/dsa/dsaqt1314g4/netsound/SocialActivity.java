package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.List;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;
import android.app.Activity;
import android.app.ActionBar;
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
import android.os.Build;

public class SocialActivity extends Activity implements AsyncResponse{

	private User user;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_social);
		setTitle("Social");

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		user = (User) this.getIntent().getExtras().get("user");
		loadMyFollowings();
		loadMyFollowers();
	}

	private void loadMyFollowers() {
		String urlString = MainActivity.BASE_URL +"profile/"+user.getUsername()+"/follower";
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.FOLLOWER_OPERATION);			
	}

	private void loadMyFollowings() {
		String urlString = MainActivity.BASE_URL +"profile/"+user.getUsername()+"/following";
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.FOLLOWINGS_OPERATION);			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.social, menu);
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
	    case android.R.id.home:
			Intent i = new Intent(getApplicationContext(), HomeActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 
			i.putExtra("user", user);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
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
			View rootView = inflater.inflate(R.layout.fragment_social,
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
		User firstUser =(User) contentList.get(0); 
		TableLayout table;
		if(firstUser.isMyFollower()){
			table  = (TableLayout) findViewById(R.id.followers);
		}else{
			table  = (TableLayout) findViewById(R.id.followings);
		}
		
		table.removeAllViews();
			//TODO: PINTAR BÉ ELS STINGS
			//mirar si m'arriba algun
			for(Object obj : contentList){
				TableRow row=new TableRow(this);
				 TextView username=new TextView(this);
				 username.setText(""+((User) obj).getName() +"  ");
				 
				 TextView content=new TextView(this);
				 content.setText(""+((User) obj).getDescription());
				 
				 row.addView(username);
				 row.addView(content);
				 table.addView(row);
			}
		
	}

	@Override
	public void goToStings() {
		// TODO Auto-generated method stub
		
	}

}
