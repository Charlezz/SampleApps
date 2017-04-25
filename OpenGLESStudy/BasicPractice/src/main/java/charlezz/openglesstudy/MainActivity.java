package charlezz.openglesstudy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import charlezz.openglesstudy.views.BasicDraw;

public class MainActivity extends ListActivity {

    ArrayAdapter<String> menuAdapter;
    Class[] items = new Class[]{BasicDraw.class};

    public static final String KEY_MENU = "key_menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        menuAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        for (int i = 0; i < items.length; i++) {
            menuAdapter.add(items[i].getSimpleName());
        }

        setListAdapter(menuAdapter);
        getListView().setOnItemClickListener(itemClickListener);
    }

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Intent intent = new Intent(MainActivity.this, OpenGLActivity.class);
            intent.putExtra(KEY_MENU, items[i].getCanonicalName());
            startActivity(intent);
        }
    };


}
