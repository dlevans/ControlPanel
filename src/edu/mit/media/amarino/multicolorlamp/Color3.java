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

public class Color3 extends Activity implements OnSeekBarChangeListener{
	
	private static final String TAG = "MultiColorLamp";
	private static String DEVICE_ADDRESS = ""; //00:06:66:06:9B:5C";
	
	final int DELAY = 150;
	SeekBar redSB;
	SeekBar greenSB;
	SeekBar blueSB;
	View colorIndicator;
	
	int red3, green3, blue3;
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
        red3 = prefs.getInt("red3", 0);
        green3 = prefs.getInt("green3", 0);
        blue3 = prefs.getInt("blue3", 0);
        
        // set seekbars and feedback color according to last state
        redSB.setProgress(red3);
        greenSB.setProgress(green3);
        blueSB.setProgress(blue3);
        colorIndicator.setBackgroundColor(Color.rgb(red3, green3, blue3));
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
				.putInt("red3", red3)
				.putInt("green3", green3)
				.putInt("blue3", blue3)
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
				red3 = seekBar.getProgress();
				updateRed();
				break;
			case R.id.SeekBarGreen:
				green3 = seekBar.getProgress();
				updateGreen();
				break;
			case R.id.SeekBarBlue:
				blue3 = seekBar.getProgress();
				updateBlue();
				break;
		}
		// provide user feedback
		colorIndicator.setBackgroundColor(Color.rgb(red3, green3, blue3));
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
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'I', red3);
	}
	
	private void updateGreen(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'K', green3);
	}
	
	private void updateBlue(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'J', blue3);
	}
	
}