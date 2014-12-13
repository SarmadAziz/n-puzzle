package nl.mprog.N_puzzle6182097;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PicListAdapter extends BaseAdapter {

	private int[] images;
	private Context myContext;
	private Bitmap[] cachedImg;

	public PicListAdapter(Context c) {

		myContext = c;
		Field[] list = R.drawable.class.getFields();
		int count = 0, index = 0, j = list.length;

		// get all images starting with img_
		for(int i=0; i < j; i++)
			if(list[i].getName().startsWith("img_")){
				count++;
			}

		images = new int[count];
		cachedImg = new Bitmap[count];

		try {
			for(int i = 0; i < j; i++)
				if(list[i].getName().startsWith("img_"))
					images[index++] = list[i].getInt(null);
		} catch(Exception IllegalArgumentException) {}

	}

	@Override
	// the number of items in the adapter
	public int getCount() {
		return images.length;
	}

	@Override
	// return image at specified position
	public Object getItem(int position) {
		return images[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	// create a new ImageView when requested
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView imgView;

		if(convertView == null) {
			imgView = new ImageView(myContext);
		} else {
			// recycle an old view 
			imgView = (ImageView) convertView;
		}

		if(cachedImg[position] == null) {
			// resize and store the image 
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 32;
			Bitmap resizedImg = BitmapFactory.decodeResource(myContext.getResources(), images[position], options);
			cachedImg[position] = resizedImg;
		}
		
		imgView.setImageBitmap(cachedImg[position]);
		return imgView;
	}
}

