package nl.mprog.N_puzzle6182097;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends ActionBarActivity {
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
	}
	
	public void play(View v) {
    	Intent i = new Intent(this, PicList.class);
    	startActivity(i);  	
    }
	
	public void scoreBoard(View v) {
    	Intent i = new Intent(this, ScoreBoard.class);
    	startActivity(i);  	
    }
	
	public void resume(View v) {
		Button resume = (Button) findViewById(R.id.resume);
		SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
		if (prefs.getInt("image", 4) != -1){
			Intent i = new Intent(this, Game.class);
	    	i.putExtra("imageToDisplay", prefs.getInt("image", 4)); 
			i.putExtra("width", prefs.getInt("width", 4));
			i.putExtra("height", prefs.getInt("height", 4));
	    	startActivity(i);
		} else {
			Toast.makeText(this, "No saved gameplay was found!", Toast.LENGTH_LONG).show();
			resume.setVisibility(View.GONE);
		}
    }
}

