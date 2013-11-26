package edu.mit.media.amarino.multicolorlamp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	Button color1;
	Button color2;
	Button color3;
	Button color4;
	Button speed;
	Button pattern;
	Button address;
	ImageView iv;
	String device = "00:06:66:06:9B:5C";
	int p;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final SharedPreferences prefs = getSharedPreferences("prefs", 0);
		device = prefs.getString("device", device);
		p = prefs.getInt("pattern", 0);
		iv = (ImageView) findViewById(R.id.imageView1);
		
	    color1 = (Button) findViewById(R.id.color1);
	    color1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Color1.class);
				startActivity(i);
			}
	    	
	    });
	    
	    color2 = (Button) findViewById(R.id.color2);
	    color2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Color2.class);
				startActivity(i);
			}
	    	
	    });
	    
	    color3 = (Button) findViewById(R.id.color3);
	    color3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Color3.class);
				startActivity(i);
			}
	    	
	    });
	    
	    color4 = (Button) findViewById(R.id.color4);
	    color4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Color4.class);
				startActivity(i);
			}
	    	
	    });
	    
	    speed = (Button) findViewById(R.id.speed);
	    speed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Speed.class);
				startActivity(i);		
			}
	    	
	    });
	    
	    pattern = (Button) findViewById(R.id.pattern);
	    pattern.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(MainActivity.this, Pattern.class);
				startActivity(i);		
			}
	    	
	    });
	    
	    address = (Button) findViewById(R.id.address);
	    address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

				alert.setTitle("Set BT Address");
				alert.setMessage("XX:XX:XX:XX:XX:XX (All caps)");

				// Set an EditText view to get user input 
				final EditText input = new EditText(MainActivity.this);
				alert.setView(input);

				alert.setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				device = input.getText().toString();
			    SharedPreferences.Editor e = prefs.edit();
				e.putString("device", device);
				e.commit();
				Toast.makeText(getApplicationContext(), device, Toast.LENGTH_LONG).show();
				}
				});

				alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					//cancel
				}
				});
				 alert.show();
			}
	    });
	}
}
