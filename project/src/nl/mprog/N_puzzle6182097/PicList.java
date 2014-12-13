package nl.mprog.N_puzzle6182097;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class PicList extends ActionBarActivity implements OnItemClickListener{	
	
	GridView grid;
	MediaPlayer music;
	int width;
	int height;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_list);
		
        music = MediaPlayer.create(getApplicationContext(), R.raw.menu);
        grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(new PicListAdapter(this));
        grid.setOnItemClickListener(this);
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {    	
    	int imageResource = (Integer) grid.getItemAtPosition(position);
    	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);

    	// new intent with saved width and height
    	Intent i = new Intent(PicList.this, Game.class);
		i.putExtra("imageToDisplay", imageResource); 
		i.putExtra("width", prefs.getInt("width", 4));
		i.putExtra("height", prefs.getInt("height", 4));
		startActivity(i);
    }
    
    public void onResume(){
		super.onResume();
		music.setLooping(true);
		music.start();
	}
	
	public void onPause(){
		super.onPause();
		music.pause();
	}
}