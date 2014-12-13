package nl.mprog.N_puzzle6182097;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class Settings extends ActionBarActivity{
	private SeekBar seekBarWidth;
	private SeekBar seekBarHeight;
	private TextView customWidth;
	private TextView customHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);		
		
		// initialization
		seekBarWidth  = (SeekBar)  findViewById(R.id.seekBar_width);
		seekBarHeight = (SeekBar)  findViewById(R.id.seekBar_height);
		customWidth   = (TextView) findViewById(R.id.custom_width);
		customHeight  = (TextView) findViewById(R.id.custom_height);
		
		seekBarWidth.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
			int progressWidth       = prefs.getInt("customWidth", 2);
			
			@Override
			// display the chosen custom width, when it's changed
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progressWidth = progresValue + 2;
				customWidth.setText("Custom width " + progressWidth);
			}
		
			@Override
			// remember the custom width
			public void onStopTrackingTouch(SeekBar seekBar) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("customWidth", progressWidth);
				editor.commit();
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
		});
		
		seekBarHeight.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
			int progressHeight      = prefs.getInt("customHeight", 2);
			  
			@Override
			// display the chosen custom height, when it's changed
			public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
				progressHeight = progresValue + 2;
				customHeight.setText("Custom height " + progressHeight);
			}
			
			@Override
			// remember the custom height
			public void onStopTrackingTouch(SeekBar seekBar) {
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("customHeight", progressHeight);
				editor.commit();
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});
	}
	
	public void backToMenu(View v){
		this.finish();
	}
}
