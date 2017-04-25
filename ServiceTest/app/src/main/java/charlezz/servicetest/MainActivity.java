package charlezz.servicetest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    @BindViews({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    List<Button> btns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                startService(new Intent(MainActivity.this, BasicService.class));
                Log.e(TAG, "btn1");
                break;
            case R.id.btn2:
                startService(new Intent(MainActivity.this, NotStickyService.class));
                Log.e(TAG, "btn2");
                break;
            case R.id.btn3:
                Log.e(TAG, "btn3");
                break;
            case R.id.btn4:
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                while (true) {
//                                    try {
//                                        Thread.sleep(3000);
//                                        runOnUiThread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Toast.makeText(MainActivity.this, "I am still alive", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }).start();
                Log.e(TAG, "btn4");
                break;
        }
    }
}
