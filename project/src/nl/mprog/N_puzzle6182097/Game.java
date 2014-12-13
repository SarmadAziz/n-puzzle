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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

public class Game extends ActionBarActivity implements OnItemClickListener{
	
	// other variables
	MediaPlayer player;
	GridView grid;
	Bitmap[] bmp;
	GameAdapter adapter;
	
	// game variables
	int imageResource;
	int tilesInWidth;
	int tilesInHeight;
	int totalTiles;
	int numOfMoves;
	int previousMoves = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);

		player = MediaPlayer.create(getApplicationContext(), R.raw.game_play);
        grid   = (GridView)findViewById(R.id.grid_images);
        
        // and retrieve the imageToDisplay ID from the extras bundle
        SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
        Bundle extras           = getIntent().getExtras();
        imageResource           = extras.getInt("imageToDisplay");
        
    	int[] imgPos = new int[1];
        if (imageResource == prefs.getInt("image", -1)){
    		// Get saved information
    		tilesInWidth       = prefs.getInt("width", tilesInWidth);
    		tilesInHeight      = prefs.getInt("height", tilesInHeight);
    		totalTiles         = tilesInWidth * tilesInHeight;
    		previousMoves      = prefs.getInt("moves", 0);
    		
    		// get the position of the saved images from the string
    		String savedString = prefs.getString("string", "");
    		StringTokenizer st = new StringTokenizer(savedString, ",");
    		imgPos = new int[totalTiles];
    		for (int i = 0, j = totalTiles; i < j; i++) {
    		    imgPos[i] = Integer.parseInt(st.nextToken());
    		}
        } 
       
        tilesInWidth  = extras.getInt("width");
        tilesInHeight = extras.getInt("height");
        grid.setNumColumns(tilesInWidth);
        
		// cut image and pass it to adapter
        Bitmap[] cutImages = imagePieces();
        grid               = (GridView)findViewById(R.id.grid_images);
        adapter            = new GameAdapter(this, cutImages, imgPos);
        grid.setAdapter(adapter);
        
        countDown();
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
    	int imageAtPos[]  = adapter.rememberedImgPos(); 
  		StringBuilder str = new StringBuilder();
		for (int i = 0; i < imageAtPos.length; i++) {
		    str.append(imageAtPos[i]).append(",");
		}
		
		// save the information
		editor.putString("string", str.toString());
    	editor.putInt("width", tilesInWidth);
    	editor.putInt("height", tilesInHeight);
    	editor.putInt("image", imageResource);
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
		
		// reset the image and moves when it is won
		if (adapter.won()){
        	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
    		SharedPreferences.Editor editor = prefs.edit();
        	
    		editor.putInt("moves", 0);
        	editor.putInt("image", -1);
        	editor.commit();
		}
	}
	
	public void countDown(){
	    new CountDownTimer(3000, 1000) {
	    	TextView count = new TextView(Game.this);
	    	// grid.addView(count);
	        public void onTick(long millisUntilFinished) {
	            count.setText("" + millisUntilFinished / 1000);
	        }
	
	        public void onFinish() {
	        	//grid.removeView(count);
	        	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
	        	// shuffle game when picture/width/height is different
	        	if (imageResource     != prefs.getInt("image", -1) ||
	        		tilesInWidth  != prefs.getInt("width", 4)  ||
	        		tilesInHeight != prefs.getInt("height", 4)){
	        		adapter.shuffle();
	        	}
	        	grid.setOnItemClickListener(Game.this);
	        }
	     }.start();
	}
	
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {		
    	// swap if clicked image adjacent to empty tile
    	if (adapter.swapRule(position, tilesInWidth) == false)
    		return;

    	// check if game is won
    	if (adapter.won()){
    		Intent i = new Intent(Game.this, Won.class);
    		i.putExtra("imageToDisplay", imageResource);
    		i.putExtra("moves", adapter.getNumOfMoves(previousMoves));
    		
        	startActivity(i);
    		this.finish();
    	}
	}
	
	public Bitmap[] imagePieces(){
		// make image size smaller
        BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		Bitmap smalImg = BitmapFactory.decodeResource(this.getResources(), imageResource, options);
		Bitmap emptyTile = BitmapFactory.decodeResource(this.getResources(), R.drawable.white);
		
		// board size
		totalTiles = tilesInWidth * tilesInHeight;
		bmp= new Bitmap[totalTiles]; 
    	int count = 0;
    	
    	// Tile dimensions
    	int width=smalImg.getWidth() / tilesInWidth; 
    	int height=smalImg.getHeight() / tilesInHeight;
    	
    	// Cut the image in pieces
    	for(int y = 0; y < tilesInHeight; y++){
    	   for(int x = 0; x < tilesInWidth; x++){
    		   if (count < totalTiles - 1){
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
	
	// open same image in different difficulty
	public void intent(Intent i, int tilesInWidth, int tilesInHeight){
		i = new Intent(this, Game.class);
	    i.putExtra("imageToDisplay", imageResource);
	    i.putExtra("width", tilesInWidth);
	    i.putExtra("height", tilesInHeight);
	    startActivity(i);
		Game.this.finish(); 
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i = new Intent();
    	SharedPreferences prefs = getSharedPreferences("myName", MODE_PRIVATE);
    	
    	// choose menu item
    	if (item.getItemId() == R.id.picList){
    		Game.this.finish();
    	} else if (item.getItemId() == R.id.easy) {
		    intent(i, 3, 3);
    	} else if (item.getItemId() == R.id.medium) {
    		intent(i, 4, 4);
    	} else if (item.getItemId() == R.id.hard){
    		intent(i, 5, 5);
    	} else {
    		intent(i, prefs.getInt("customWidth", 3), prefs.getInt("customHeight", 4));
    	}
    	return true;
    }
}
