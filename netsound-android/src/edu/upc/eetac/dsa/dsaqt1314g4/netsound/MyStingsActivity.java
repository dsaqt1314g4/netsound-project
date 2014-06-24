package edu.upc.eetac.dsa.dsaqt1314g4.netsound;

import java.util.List;

import android.app.ActionBar.LayoutParams;
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

public class MyStingsActivity extends Activity implements AsyncResponse {

private User user;
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_my_stings);
setTitle("My stings");
if (savedInstanceState == null) {
getFragmentManager().beginTransaction()
.add(R.id.container, new PlaceholderFragment()).commit();
}
user = (User) this.getIntent().getExtras().get("user");
loadStings(user);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {

// Inflate the menu; this adds items to the action bar if it is present.
getMenuInflater().inflate(R.menu.my_stings, menu);
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
View rootView = inflater.inflate(R.layout.fragment_my_stings,
container, false);
return rootView;
}
}
private void loadStings(User user) {
String urlString = MainActivity.BASE_URL +"profile/"+user.getUsername()+"/stings";
new CallAPI(this).execute(urlString, user.getToken(), CallAPI.STINGS_OPERATION);

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
public void printContent(List<Object> stingList) {
// TODO Auto-generated method stub
TableLayout table = (TableLayout) findViewById(R.id.stings);
table.removeAllViews();
//TODO: PINTAR BÉ ELS STINGS
//mirar si m'arriba algun
for(Object obj : stingList){
TableRow row=new TableRow(this);
TextView username=new TextView(this);
username.setText("User: "+((Sting) obj).getUsername() +" ");
 
TextView content=new TextView(this);
content.setText("Sting: "+((Sting) obj).getContent() + System.getProperty ("line.separator") + System.getProperty ("line.separator"));
 
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
