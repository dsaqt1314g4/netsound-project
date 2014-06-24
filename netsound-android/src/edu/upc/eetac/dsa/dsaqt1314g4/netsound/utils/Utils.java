package edu.upc.eetac.dsa.dsaqt1314g4.netsound.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import edu.upc.eetac.dsa.dsaqt1314g4.netsound.MainActivity;

public class Utils {

	
	public static void createDialog(final Activity context, String title, String message, final boolean backOnPressed){
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
			
		// set title
		alertDialog.setTitle(title);

		// set dialog message
		alertDialog.setMessage(message);

		alertDialog.setButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						if(backOnPressed){
							context.onBackPressed();
						}
					}
				  });
					 
		// show it
		alertDialog.show();
	}
	
	public static void logout(Activity activity){
		  Intent intent = new Intent(activity.getApplicationContext(), MainActivity.class);
		  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
		  activity.startActivity(intent);
	}
}
