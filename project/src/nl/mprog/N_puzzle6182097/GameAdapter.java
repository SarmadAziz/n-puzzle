package nl.mprog.N_puzzle6182097;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GameAdapter extends BaseAdapter {

	private ImageView[] viewss;
	private Tile[] images;
	private Context myContext;
	int count = 0;
	public int emptyTilePos;
	public Bitmap[] cropImg;
	int[] prevImgPos;
	Tile[] prevImgPosTile;
	
	// Constructor
	public GameAdapter(Context c, Bitmap[] cropped, int[] imgPos) {
		myContext = c;	
		cropImg = cropped;
		prevImgPos = new int[imgPos.length];
		viewss = new ImageView[cropped.length];
		images = new Tile[cropped.length];
		prevImgPosTile = new Tile[imgPos.length];
		for (int x = 0; x < cropped.length; x++){
			viewss[x] = new ImageView(myContext);
			images[x] = new Tile(cropped[x], x);
			if (prevImgPos.length == cropped.length){
				prevImgPos[x] = imgPos[x];
			}
			if (prevImgPosTile.length == cropped.length){
				images[x] = new Tile(cropped[imgPos[x]], imgPos[x]);
			}
		}
		if (images != null){
			for (int i = 0; i < images.length; i++){
				Log.d("images", images[i].tag+"");
			}
		}
		
		getEmptyTile();
		Log.d("test", emptyTilePos + "");
	}

	@Override
	// the number of items in the adapter
	public int getCount() {
		return images.length;
	}
	
	@Override
	public Object getItem(int position) {
		return images[position];
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void  getEmptyTile(){
		for (int i = 0, j = getCount(); i < j; i++){
			if (images[i].tag == getCount()-1){
				emptyTilePos = i; 
				break;
			}
		}
	}
	
	// create a new ImageView when requested
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imgView = viewss[position];
		if (prevImgPosTile.length == viewss.length){
			imgView.setImageBitmap(images[position].image);
		} else {
			imgView.setImageBitmap(images[position].image);
		}
		return imgView;
	}

	public void swap(int position){
		Tile temp = images[position];
		images[position] = images[emptyTilePos];
		images[emptyTilePos] = temp;
		emptyTilePos = position;
		notifyDataSetChanged();
	}
	
	public void shuffle(){
		// emptyTilePos = cropImg.length - 1;
		int max = cropImg.length - 2;
		for (int x = 0; x < cropImg.length - 1; x++){
			viewss[x] = new ImageView(myContext);
			images[max-x] = new Tile(cropImg[x], x);
		}
		if (cropImg.length % 2 == 0){
			Tile temp = images[max];
			images[max] = images[max - 1];
			images[max - 1] = temp;
		}
		notifyDataSetChanged(); 
	}
	
	public boolean swapRule(int pos, int block){		
		// if pos not adjecent to empty tile, don't swap
		getEmptyTile();
		if (pos - 1 != emptyTilePos && pos + 1 != emptyTilePos &&
				pos - block != emptyTilePos && pos + block != emptyTilePos){
			return false;
		}
		
		swap(pos);
		count++;
		
		return true;
	}
	
	public int getNumOfMoves(int previousMoves){
		return count + previousMoves;
	}
	
	public boolean won(){
		for (int x = 0; x < images.length; x++){
			if (images[x].tag != x){
				return false;
			}
		}
		return true;
	}

	public int[] rememberedImgPos(){
		int j = getCount();
		int imageAtPos[] = new int[j];
		for (int i = 0; i < j; i++) {
			imageAtPos[i] = images[i].tag;
		}
		return imageAtPos;
	}
}


