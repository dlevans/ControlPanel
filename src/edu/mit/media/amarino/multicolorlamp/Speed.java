package edu.mit.media.amarino.multicolorlamp;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import at.abraxas.amarino.Amarino;

public class Speed extends Activity implements OnSeekBarChangeListener{
	
	TextView textViewSpeed;
	private static final String TAG = "MultiColorLamp";
	private static String DEVICE_ADDRESS = ""; // = "00:06:66:06:9B:5C";
	final int DELAY = 150;
	SeekBar speedSB;
	int speed;
	long lastChange;
	String device;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speed);
        final SharedPreferences prefs = getSharedPreferences("prefs", 0);
        DEVICE_ADDRESS = prefs.getString("device", "**");
        Toast.makeText(getApplicationContext(), "Connecting to:"+DEVICE_ADDRESS, Toast.LENGTH_LONG).show();
		Amarino.connect(this, DEVICE_ADDRESS);
        // get references to views defined in our main.xml layout file
        speedSB = (SeekBar) findViewById(R.id.SeekBarspeed);
        // register listeners
        speedSB.setOnSeekBarChangeListener(this);
        textViewSpeed = (TextView) findViewById(R.id.textViewSpeed);
    }
    
	@Override
	protected void onStart() {
		super.onStart();

		// load last state
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        speed = prefs.getInt("speed", 0);
        
        // set seekbars and feedback color according to last state
        speedSB.setProgress(speed);
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
				.putInt("speed", speed)
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
			case R.id.SeekBarspeed:
				speed = seekBar.getProgress();
				updatespeed();
				break;
		}
		textViewSpeed.setText(String.valueOf(speed));
	}
	
	private void updateAllColors() {
		// send state to Arduino
        updatespeed();
	}
	
	private void updatespeed(){
		Amarino.sendDataToArduino(this, DEVICE_ADDRESS, 'S', speed);
	}
}