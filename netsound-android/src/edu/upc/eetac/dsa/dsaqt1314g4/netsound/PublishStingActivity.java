package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class PublishStingActivity extends Activity implements AsyncResponse{

	public static int PLAYLIST_POST = 0;
	public static int SONG_POST = 1;
	public static int STING_POST = 2;
	
	private int whichPost;
	private User user;
	private HashMap<String, String> contentMap = new HashMap<>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    final Button btn = (Button)findViewById(R.id.btn_publish);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_sting);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		user = (User) this.getIntent().getExtras().get("user");
		whichPost = (int) this.getIntent().getExtras().get("whichPost");
		contentMap = (HashMap<String, String>) this.getIntent().getExtras().get("contentMap");
		if(whichPost!=2){
		printSelect();
		}
		btn.setOnClickListener(new OnClickListener() {	
			
			@Override
			public void onClick(View v) {
				 String urlString = "";
				 EditText stingText = (EditText) findViewById(R.id.sting);
				 String sting = stingText.getText().toString(); 
				 urlString= MainActivity.BASE_URL +"profile/stings";
				 String urlParameters = "{\"username\":\""+user.getUsername()+"\",\"content\":\""+sting+"\"}";		
				 prova(urlString,urlParameters);
			}
		});
	}
	private void prova(String urlString,String urlParameters){
		
		 new StingAPI(this).execute(urlString, urlParameters);	

	}

	private void printSelect() {
		
		Spinner spinner = (Spinner) findViewById(R.id.content_spinner);
		
		List<String> list = new ArrayList<String>();
		
		for(String content : contentMap.values()){
			list.add(content);
		}
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(dataAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.publish_sting, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_publish_sting,
					container, false);
			return rootView;
		}
	}
	
	public void publish(View view){
		
		 String urlString = "";
		 EditText stingText = (EditText) findViewById(R.id.sting);
		 String sting = stingText.getText().toString(); 
		 if(whichPost!=2){
		 Spinner spinner = (Spinner) findViewById(R.id.content_spinner);
		 String contentName = spinner.getSelectedItem().toString();
		 String id = "";
		 
		 for (Entry<String, String> content : contentMap.entrySet()) {
	            if (content.getValue().equals(contentName)) {
	            	id = content.getKey();
	            }
	     }
		 if(whichPost==SONG_POST){
			 urlString= MainActivity.BASE_URL +"songs/"+id+"/stings";
		 }
		 else
			 if(whichPost==PLAYLIST_POST){
			 urlString= MainActivity.BASE_URL +"playlist/"+id+"/stings";
			 }
		 
		 }
		 
			 if(whichPost==2){
			 urlString= MainActivity.BASE_URL +"profile/stings";

			 }
			 
		 String urlParameters = "{\"username\":\""+user.getUsername()+"\",\"content\":\""+sting+"\"}";		
		 new StingAPI(this).execute(urlString, urlParameters);	
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
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void goToStings() {
		Intent in = new Intent(getApplicationContext(), MyStingsActivity.class);			
		in.putExtra("user", user);		
		startActivity(in);
		
	}

}
