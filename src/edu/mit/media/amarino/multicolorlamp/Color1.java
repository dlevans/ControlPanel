package edu.mit.media.amarino.multicolorlamp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.abraxas.amarino.Amarino;

public class Color1 extends Activity implements OnSeekBarChangeListener{
	
	private static final String TAG = "ControlPanel";
	private static String DEVICE_ADDRESS = ""; // = "00:06:66:06:9B:5C";
	
	final int DELAY = 150;
	SeekBar redSB;
	SeekBar greenSB;
	SeekBar blueSB;
	View colorIndicator;
	int red1, green1, blue1;
	long lastChange;
	String device;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorone);
        final SharedPreferences prefs = getSharedPreferences("prefs", 0);
        DEVICE_ADDRESS = prefs.getString("device", "**");
        Toast.makeText(getApplicationContext(), "Connecting to:"+DEVICE_ADDRESS, Toast.LENGTH_LONG).show();
		Amarino.connect(this, DEVICE_ADDRESS);
        
        // get references to views defined in our main.xml layout file
        redSB = (SeekBar) findViewById(R.id.SeekBarRed);
        greenSB = (SeekBar) findViewById(R.id.SeekBarGreen);
        blueSB = (SeekBar) findViewById(R.id.SeekBarBlue);
        colorIndicator = findViewById(R.id.ColorIndicator);

        // register listeners
        redSB.setOnSeekBarChangeListener(this);
        greenSB.setOnSeekBarChangeListener(this);
        blueSB.setOnSeekBarChangeListener(this);
    }
    
	@Override
	protected void onStart() {
		super.onStart();

		// load last state
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        red1 = prefs.getInt("red1", 0);
        green1 = prefs.getInt("green1", 0);
        blue1 = prefs.getInt("blue1", 0);
        // set seekbars and feedback color according to last state
        redSB.setProgress(red1);
        greenSB.setProgress(green1);
        blueSB.setProgress(blue1);
        colorIndicator.setBackgroundColor(Color.rgb(red1, green1, blue1));
        new Thread(){
        	public void run(){
        		try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {}
				Log.d(TAG, "update colors");
        		updateAllColors();
        	}
        }.start();
        
	}

	@Override
	protected void onStop() {
		super.onStop();
		// save state
		PreferenceManager.getDefaultSharedPreferences(this)
			.edit()
				.putInt("red1", red1)
				.putInt("green1", green1)
				.putInt("blue1", blue1)
			.commit();
		
		// stop Amarino's background service, we don't need it any more 
		Amarino.disconnect(this, DEVICE_ADDRESS);
	}



	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// do not send to many updates, Arduino can't handle so much
		if (System.currentTimeMillis() - lastChange > DELAY ){
			updateState(seekBar);
			lastChange = System.currentTimeMillis();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		lastChange = System.currentTimeMillis();
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		updateState(seekBar);
	}

	private void updateState(final SeekBar seekBar) {
		
		switch (seekBar.getId()){
			case R.id.SeekBarRed:
				red1 = seekBar.getProgress();
				updateRed();
				break;
			case R.id.SeekBarGreen:
				green1 = seekBar.getProgress();
				updateGreen();
				break;
			case R.id.SeekBarBlue:
				blue1 = seekBar.getProgress();
				updateBlue();
				break;
		}
		// provide user feedback
		colorIndicator.setBackgroundColor(Color.rgb(red1, green1, blue1));
	}
	
	private void updateAllColors() {
		// send state to Arduino
        updateRed();
        updateGreen();
        updateBlue();
	}
	
	private void updateRed(){
		// I have chosen random small letters for the flag 'o' for red, 'p' for green and 'q' for blue
		// you could select any small letter you want
		// however be sure to match the character you register a function for your in Arduino sketch
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'C', red1);
	}
	
	private void updateGreen(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'E', green1);
	}
	
	private void updateBlue(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'D', blue1);
	}
	
}