package nl.mprog.N_puzzle6182097;


import java.util.StringTokenizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class Game extends ActionBarActivity implements OnItemClickListener{
	
	MediaPlayer player;
	GridView grid;
	int resource;
	int widthBlocks;
	int heightBlocks;
	int numOfMoves;
	int previousMoves = 0;
	
	Bitmap[] bmp;
	GameAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		player = MediaPlayer.create(getApplicationContext(), R.raw.game_play);
        grid = (GridView)findViewById(R.id.grid_images);
        
        // and retrieve the imageToDisplay ID from the extras bundle
        SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();
        resource = extras.getInt("imageToDisplay");
        
    	int[] imgPos = new int[1];
        if (resource == prefs.getInt("image", -1)){
    		// Get saved information
    		widthBlocks          = prefs.getInt("width", widthBlocks);
    		heightBlocks         = prefs.getInt("height", heightBlocks);
    		int tiles = widthBlocks * heightBlocks;
    		previousMoves   = prefs.getInt("moves", 0);
    		String savedString = prefs.getString("string", "");
    		StringTokenizer st = new StringTokenizer(savedString, ",");
    		imgPos = new int[tiles];
    		for (int i = 0, j = tiles; i < j; i++) {
    		    imgPos[i] = Integer.parseInt(st.nextToken());
    		}
        }
        widthBlocks = extras.getInt("width");
        heightBlocks = extras.getInt("height");
        grid.setNumColumns(widthBlocks);
        
		// cut image and pass it to adapter
        Bitmap[] cutImages = imagePieces();
        grid = (GridView)findViewById(R.id.grid_images);
        adapter = new GameAdapter(this, cutImages, imgPos);
        grid.setAdapter(adapter);
        
        // countdown
        new CountDownTimer(1000, 1000) {
        	TextView count = new TextView(Game.this);
        	// grid.addView(count);
            public void onTick(long millisUntilFinished) {
                count.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
            	//grid.removeView(count);
            	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
            	if (resource != prefs.getInt("image", -1) ||
            		widthBlocks != prefs.getInt("width", 4) ||
            		heightBlocks != prefs.getInt("height", 4)){
            		adapter.shuffle();
            	}
            	grid.setOnItemClickListener(Game.this);
            }
         }.start();
   	}
	
	public void onResume(){
		super.onResume();
		player.setLooping(true);
		player.start();	
	}
	
	public void onPause(){
		super.onPause();
		player.pause();
		
    	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		
		// save the position of the images in a string
    	int imageAtPos[] = adapter.rememberedImgPos(); 
  		StringBuilder str = new StringBuilder();
		for (int i = 0; i < imageAtPos.length; i++) {
		    str.append(imageAtPos[i]).append(",");
		}
		
		// save the information
		editor.putString("string", str.toString());
    	editor.putInt("width", widthBlocks);
    	editor.putInt("height", heightBlocks);
    	editor.putInt("image", resource);
    	editor.putInt("moves", adapter.getNumOfMoves(previousMoves));
    	
    	editor.commit();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		
		// remove all bmp images
		for (int i = 0, j = bmp.length ; i < j; i++){
			bmp[i].recycle();
		}
		
		// reset the game when it is won
		if (adapter.won()){
			// saves state
        	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
    		SharedPreferences.Editor editor = prefs.edit();
    		
        	// editor.clear();
        	editor.putInt("moves", 0);
        	editor.putInt("image", -1);
        	
        	editor.commit();
		}
		
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {		
    	// swap if adjecent to empty tile
    	if (adapter.swapRule(position, widthBlocks) == false)
    		return;

    	// check if game is won
    	numOfMoves = adapter.getNumOfMoves(previousMoves);
    	if (adapter.won()){
    		Intent i = new Intent(Game.this, Won.class);
    		i.putExtra("imageToDisplay", resource);
    		i.putExtra("moves", numOfMoves);
    		
        	startActivity(i);
    		this.finish();
    	}
	}
	
	public Bitmap[] imagePieces(){
		// make image size smaller
        BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		Bitmap smalImg = BitmapFactory.decodeResource(this.getResources(), resource, options);
		Bitmap emptyTile = BitmapFactory.decodeResource(this.getResources(), R.drawable.white);
		
		// 
		int dimension = widthBlocks * heightBlocks;
		bmp= new Bitmap[dimension]; 
    	int count = 0;
    	
    	// Tile dimensions
    	int width=smalImg.getWidth() / widthBlocks; 
    	int height=smalImg.getHeight() / heightBlocks;
    	
    	// Cut the image in pieces
    	for(int y = 0; y < heightBlocks; y++){
    	   for(int x = 0; x < widthBlocks; x++){
    		   if (count < dimension - 1){
    			   bmp[count] = Bitmap.createBitmap(smalImg, x * width, y * height , width, height);   
    		   } else {
    			   bmp[count] = Bitmap.createBitmap(emptyTile, 0, 0 , width, height);
    			   emptyTile.recycle();
    		   }
    		   count++;
    	   }
    	}
    	smalImg.recycle();
    	return bmp;
    }
	
	@Override 
	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void intent(Intent i, int widthBlocks, int heightBlocks){
		i = new Intent(this, Game.class);
	    i.putExtra("imageToDisplay", resource);
	    i.putExtra("width", widthBlocks);
	    i.putExtra("height", heightBlocks);
	    startActivity(i);
		Game.this.finish(); 
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i = new Intent();
    	if (item.getItemId() == R.id.picList){
    		Game.this.finish();
    	}
    	else if (item.getItemId() == R.id.easy) {
		    intent(i, 3, 3);
    	}
    	else if (item.getItemId() == R.id.medium) {
    		intent(i, 4, 4);
    	}
    	else if (item.getItemId() == R.id.hard){
    		intent(i, 5, 5);
    	} else {
    		intent(i, 3, 4);
    	}
    	return true;
    }
}
