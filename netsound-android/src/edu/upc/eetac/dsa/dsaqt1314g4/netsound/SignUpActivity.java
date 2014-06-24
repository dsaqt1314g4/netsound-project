package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils.Utils;

public class SignUpActivity extends Activity implements AsyncResponse{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_sign_up,
					container, false);
			return rootView;
		}
	}
	
	public void signup(View view){
		
	
		 EditText usernameText = (EditText) findViewById(R.id.username);
		 String username = usernameText.getText().toString(); 
		 
		 EditText passwordText = (EditText) findViewById(R.id.password);
		 String password = passwordText.getText().toString(); 
		 
		 EditText nameText = (EditText) findViewById(R.id.name);
		 String name = nameText.getText().toString(); 
		 
		 EditText emailText = (EditText) findViewById(R.id.email);
		 String email = emailText.getText().toString(); 
		 
		 EditText descriptionText = (EditText) findViewById(R.id.description);
		 String description = descriptionText.getText().toString(); 
		
		 String urlString = MainActivity.BASE_URL +"profile";
		 String urlParameters = "{\"name\":\""+name+"\",\"username\":\""+username+"\",\"userpass\":\""+password+"\",\"email\":\""+email+"\",\"description\":\""+description+"\"}";
		
		 new SignAPI(this).execute(urlString, urlParameters);	
	}

	@Override
	public void goToHome(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToLogin() {		
		Utils.createDialog(this, "User created", "Well done, your user has been created. You can log in", true); 
		
	}

	@Override
	public void printError(String error) {
		 Utils.createDialog(this, "Error", error, false); 
		
	}

	@Override
	public void printContent(List<Object> stingList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void goToStings() {
		// TODO Auto-generated method stub
		
	}

}
