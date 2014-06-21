package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.netsound_android.R;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;

public class MainActivity extends Activity implements AsyncResponse{

	public static final String BASE_URL = "http://192.168.1.108:8000/netsound-api/";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	 public void login(View view) {	
		 
		 EditText usernameText = (EditText) findViewById(R.id.username);
		 String username = usernameText.getText().toString(); 
		 
		 EditText passwordText = (EditText) findViewById(R.id.password);
		 String password = passwordText.getText().toString(); 
		
		 String urlString = BASE_URL +"profile/"+username;
		 
		 String toEncode = username +":" +password;
		 String basic = "Basic " + Base64.encodeToString(toEncode.getBytes(),0);

		 new CallAPI(this).execute(urlString, basic, CallAPI.LOGIN_OPERATION);		 
		  
	}
	 
	 public void goToHome(User user){
		 
		Intent i = new Intent(getApplicationContext(), HomeActivity.class);
		i.putExtra("user", user);
		startActivity(i);
	 }
	 
	 @SuppressWarnings("deprecation")
	public void printError(String error){
		 AlertDialog alertDialog = new AlertDialog.Builder(this).create();
					
	 
				// set title
		 alertDialog.setTitle("Try again");
	 
				// set dialog message
		 alertDialog.setMessage("Your username or password is wrong");
		 
		 alertDialog.setButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							
							dialog.cancel();
						}
					  });
	 					 
					// show it
					alertDialog.show();
	}

	@Override
	public void printStings(List<Sting> stingList) {
		// TODO Auto-generated method stub
		
	}
	 

}
