package nl.mprog.N_puzzle6182097;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class PicList extends ActionBarActivity implements OnItemClickListener{	
	
	GridView grid;
	MediaPlayer player;
	int width;
	int height;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_list);
		
        player = MediaPlayer.create(getApplicationContext(), R.raw.menu);
        grid = (GridView) findViewById(R.id.gridview);
        grid.setAdapter(new PicListAdapter(this));
        grid.setOnItemClickListener(this);
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    	// showDialog(position);
    	
    	int imageResource = (Integer) grid.getItemAtPosition(position);
    	//showDialog(imageResource);
    	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
        width = prefs.getInt("width", 4);
        height = prefs.getInt("height", 4);
    	Intent i = new Intent(PicList.this, Game.class);
		i.putExtra("imageToDisplay", imageResource); 
		i.putExtra("width", width);
		i.putExtra("height", height);
		startActivity(i);
    }
    
    public void onResume(){
		super.onResume();
		player.setLooping(true);
		player.start();
	}
	
	public void onPause(){
		super.onPause();
		player.pause();
	}
    
    protected Dialog onCreateDialog(int id) {
    	AlertDialog dialog = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	TextView message = new TextView(this);
    	message.setText("Difficulty");
    	message.setGravity(Gravity.CENTER);
    	
    	builder.setView(message)
			   .setPositiveButton("Hard", new DialogInterface.OnClickListener() { // Dialoginterface is een nieuw object, daarom verwijst this hier naar
				   
				   public void onClick(DialogInterface dialog, int id) {
					   Intent i = new Intent(PicList.this, Game.class);
					   int imageResource = (Integer) grid.getItemAtPosition(id);
					   i.putExtra("imageToDisplay", imageResource);
					   i.putExtra("level", 5);
					   startActivity(i);
				   }
			   })
			   .setNeutralButton("Medium", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   Intent i = new Intent(PicList.this, Game.class);
					   int imageResource = (Integer) grid.getItemAtPosition(id);
					   i.putExtra("imageToDisplay", imageResource);
					   i.putExtra("level", 4);
					   startActivity(i);
				   }
			   })
			   .setNegativeButton("Easy", new DialogInterface.OnClickListener() {
				   public void onClick(DialogInterface dialog, int id) {
					   dialog.cancel();
				   }
			   });
		dialog = builder.create();
		dialog.show();
		return dialog;
    }
}