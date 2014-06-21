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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.netsound_android.R;

import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.Sting;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.model.User;

public class HomeActivity extends Activity implements AsyncResponse{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		TextView helloText = (TextView)findViewById(R.id.username);

		//in your OnCreate() method
		User user = (User) this.getIntent().getExtras().get("user");
		helloText.setText("Hello "+user.getName());
		
		String urlString = MainActivity.BASE_URL +"profile/"+user.getUsername()+"/following/stings";
		new CallAPI(this).execute(urlString, user.getToken(), CallAPI.STINGS_OPERATION);		
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
	public void printStings(List<Sting> stingList) {
		
		TableLayout table = (TableLayout) findViewById(R.id.stings);
	
			//TODO: PINTAR BÉ ELS STINGS
			//mirar si m'arriba algun
			for(Sting obj : stingList){
				TableRow row=new TableRow(this);
				 TextView username=new TextView(this);
				 username.setText(""+obj.getUsername());
				 
				 TextView content=new TextView(this);
				 content.setText(""+obj.getContent());
				 
				 row.addView(username);
				 row.addView(content);
				 table.addView(row);
			}
			
//			for(int j=0; j<stingList.size();j++){
//				Sting obj = stingList.get(j);
//			}
		
	}

}
