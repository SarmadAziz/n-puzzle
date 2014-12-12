package nl.mprog.N_puzzle6182097;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Won extends ActionBarActivity {
	
	Bitmap smalImg;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.won);
		
		Bundle extras = getIntent().getExtras();
		
		TextView youWin = (TextView) findViewById(R.id.textView1);
		youWin.setText("You won with " + extras.getInt("moves") + " moves!");
		
		ImageView img = (ImageView) findViewById(R.id.single_image);
        int resource = extras.getInt("imageToDisplay");
        
        BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 8;
		smalImg = BitmapFactory.decodeResource(this.getResources(), resource, options);
        
        img.setImageBitmap(smalImg);
	}
	
	public void backToPicList(View v){
		smalImg.recycle();
		Won.this.finish();
	}
	
}
