package edu.mit.media.amarino.multicolorlamp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import at.abraxas.amarino.Amarino;

public class Pattern extends Activity{
	Button pattern0;
	Button pattern1;
	Button pattern2;
	Button pattern3;
	Button pattern4;
	Button pattern5;
	Button pattern6;
	Button pattern7;
	Button pattern8;
	private static String DEVICE_ADDRESS = ""; // = "00:06:66:06:9B:5C";
	final int DELAY = 150;
	String device;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pattern);
        final SharedPreferences prefs = getSharedPreferences("prefs", 0);
        DEVICE_ADDRESS = prefs.getString("device", "**");
        Toast.makeText(getApplicationContext(), "Connecting to:"+DEVICE_ADDRESS, Toast.LENGTH_LONG).show();
		Amarino.connect(this, DEVICE_ADDRESS);
		
		pattern0 = (Button) findViewById(R.id.pattern0);
	    pattern0.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern0();
			}
	    	
	    });
		
	    pattern1 = (Button) findViewById(R.id.pattern1);
	    pattern1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern1();
			}
	    	
	    });
	    
	    pattern2 = (Button) findViewById(R.id.pattern2);
	    pattern2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern2();
			}
	    	
	    });
	    
	    pattern3 = (Button) findViewById(R.id.pattern3);
	    pattern3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern3();
			}
	    	
	    });
	    
	    pattern4 = (Button) findViewById(R.id.pattern4);
	    pattern4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern4();
			}
	    	
	    });
	    
	    pattern5 = (Button) findViewById(R.id.pattern5);
	    pattern5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern5();
			}
	    	
	    });
	    
	    pattern6 = (Button) findViewById(R.id.pattern6);
	    pattern6.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern6();
			}
	    	
	    });
	    
	    pattern7 = (Button) findViewById(R.id.pattern7);
	    pattern7.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern7();
			}
	    	
	    });
	    
	    pattern8 = (Button) findViewById(R.id.pattern8);
	    pattern8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				updatePattern8();
			}
	    	
	    });
    }
    

	@Override
	protected void onStop() {
		super.onStop();
		// stop Amarino's background service, we don't need it any more 
		Amarino.disconnect(this, DEVICE_ADDRESS);
	}
	private void updatePattern0(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 0);
	}	
	private void updatePattern1(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 1);
	}
	private void updatePattern2(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 2);
	}
	private void updatePattern3(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 3);
	}
	private void updatePattern4(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 4);
	}
	private void updatePattern5(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 5);
	}
	private void updatePattern6(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 6);
	}
	private void updatePattern7(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 7);
	}
	private void updatePattern8(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'P', 8);
	}	
}