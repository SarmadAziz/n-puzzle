package nl.mprog.N_puzzle6182097;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GameAdapter extends BaseAdapter {

	private Context myContext;
	
	// array variables
	private ImageView[] views;
	private Tile[] images;
	private Bitmap[] cropImg;
	
	// int variables
	private int prevImgCount;
	private int moveCount = 0;
	private int emptyTilePos;
	
	// Constructor
	public GameAdapter(Context c, Bitmap[] cropped, int[] imgPos) {
		int imgLength = cropped.length;
		myContext     = c;	
		cropImg       = cropped;
		views         = new ImageView[imgLength];
		images        = new Tile[imgLength];
		prevImgCount  = imgPos.length;
		
		for (int x = 0; x < imgLength; x++){
			views[x]  = new ImageView(myContext);
			images[x] = new Tile(cropped[x], x);
			
			if (prevImgCount == imgLength){
				images[x] = new Tile(cropped[imgPos[x]], imgPos[x]);
			}
		}
		getEmptyTile();
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
		ImageView imgView = views[position];
		if (prevImgCount == views.length){
			imgView.setImageBitmap(images[position].image);
		} else {
			imgView.setImageBitmap(images[position].image);
		}
		return imgView;
	}

	// swap 2 bitmap images with each other
	public void swap(int position){
		Tile temp = images[position];
		images[position] = images[emptyTilePos];
		images[emptyTilePos] = temp;
		emptyTilePos = position;
		notifyDataSetChanged();
	}
	
	// shuffle the game in reverse order
	public void shuffle(){
		int max = cropImg.length - 2;
		for (int x = 0; x < cropImg.length - 1; x++){
			views[x] = new ImageView(myContext);
			images[max-x] = new Tile(cropImg[x], x);
		}
		// if the tiles are uneven, swap tile 1 and 2
		if (cropImg.length % 2 == 0){
			Tile temp = images[max];
			images[max] = images[max - 1];
			images[max - 1] = temp;
		}
		notifyDataSetChanged(); 
	}
	
	// swap when image is adjacent to empty tile
	public boolean swapRule(int pos, int block){		
		if (pos - 1 != emptyTilePos && pos + 1 != emptyTilePos &&
				pos - block != emptyTilePos && pos + block != emptyTilePos){
			return false;
		}
		
		swap(pos);
		moveCount++;
		return true;
	}
	
	public int getNumOfMoves(int previousMoves){
		return moveCount + previousMoves;
	}
	
	// check if game is won
	public boolean won(){
		for (int x = 0; x < images.length; x++){
			if (images[x].tag != x){
				return false;
			}
		}
		return true;
	}

	// remember the position of the imgTags to resume future game
	public int[] rememberedImgPos(){
		int j = getCount();
		int imageAtPos[] = new int[j];
		for (int i = 0; i < j; i++) {
			imageAtPos[i] = images[i].tag;
		}
		return imageAtPos;
	}
}


