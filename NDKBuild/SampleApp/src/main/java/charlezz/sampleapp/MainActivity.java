package charlezz.sampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.load1, R.id.load2, R.id.load3, R.id.load4, R.id.load5, R.id.load6})
    public void OnClick(View v) {
        switch (v.getId()) {
            case R.id.load1:
                Toast.makeText(MainActivity.this, new charlezz.ndkbuildlib.NativeManager().hello(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.load2:
                Toast.makeText(MainActivity.this, new charlezz.ndkbuildlibtwo.NativeManager().hello(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.load3:
                Toast.makeText(MainActivity.this, new charlezz.cmakebuildlib.NativeManager().hello(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.load4:
                break;
            case R.id.load5:
                break;
            case R.id.load6:
                break;

        }
    }
}
