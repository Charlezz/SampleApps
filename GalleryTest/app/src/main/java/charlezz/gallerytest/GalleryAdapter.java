package charlezz.gallerytest;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

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

    @Override
    public int getCount() {
        return 100;
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
        GridView.LayoutParams params = new AbsListView.LayoutParams((int) imageWidth, (int) imageWidth);
        iv.setBackgroundResource(R.color.colorAccent);
        iv.setLayoutParams(params);
        return iv;
    }
}
