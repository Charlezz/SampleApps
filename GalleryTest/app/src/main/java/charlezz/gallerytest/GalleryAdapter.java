package charlezz.gallerytest;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Charles on 2017. 5. 15..
 */

public class GalleryAdapter extends BaseAdapter {
    ArrayList<String> items = new ArrayList<>();
    float imageWidth;

    public GalleryAdapter(float imageWidth) {
        this.imageWidth = imageWidth;
        Log.e(TAG, "imageWidth:" + imageWidth);
    }

    public void setitems(ArrayList<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(parent.getContext());
        iv.setImageResource(R.mipmap.ic_launcher);
        GridView.LayoutParams params = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) imageWidth);
        iv.setBackgroundResource(R.color.colorAccent);
        iv.setLayoutParams(params);

//        int targetW = iv.getWidth();
//        int targetH = iv.getHeight();
//
//        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
//        bmOptions.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(items.get(position), bmOptions);
//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;
//
////        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
//        int scaleFactor = (int) (photoW / imageWidth);
//        bmOptions.inJustDecodeBounds = false;
//        bmOptions.inSampleSize = scaleFactor;
//        bmOptions.inPurgeable = true;
//
//        Bitmap bitmap = BitmapFactory.decodeFile(items.get(position), bmOptions);
//        iv.setImageBitmap(bitmap);
//        iv.setScaleType(ImageView.ScaleType.FIT_XY);

        Glide.with(parent.getContext())
                .load(items.get(position))
                .into(iv);

        return iv;
    }
}
