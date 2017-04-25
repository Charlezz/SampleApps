package charlezz.servicetest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Charles on 24/04/2017.
 */

public class MusicAdapter extends BaseAdapter {
    private ArrayList<MusicItem> items = new ArrayList<>();

    public void add(MusicItem item) {
        items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public MusicItem getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.music_item_view, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        }
        holder.name.setText(items.get(position).name);

        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
