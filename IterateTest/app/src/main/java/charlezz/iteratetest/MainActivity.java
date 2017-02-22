package charlezz.iteratetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static charlezz.iteratetest.R.id.test1;
import static charlezz.iteratetest.R.id.test2;
import static charlezz.iteratetest.R.id.test3;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    Unbinder unbinder;

    @BindView(R.id.count)
    EditText count;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.realtime)
    TextView realTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    long time;

    @OnClick(test1)
    public void test1() {
        result.setText("");
        time = System.currentTimeMillis();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int temp;
                int j = Integer.parseInt(count.getText().toString());
                for (int i = 0; i < j; i++) {
//                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            realTime.setText("count:" + String.valueOf(finalI));
//                        }
//                    });
                    temp = i;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("time:" + (int) (System.currentTimeMillis() - time));
                    }
                });
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @OnClick(test2)
    public void test2() {
        result.setText("");
        time = System.currentTimeMillis();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                int temp;
                for (int i = Integer.parseInt(count.getText().toString()); i > 0; i--) {
//                    final int finalI = i;
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            realTime.setText("count:" + String.valueOf(finalI));
//                        }
//                    });
                    temp = i;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        result.setText("time:" + (int) (System.currentTimeMillis() - time));
                    }
                });
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    @OnClick(test3)
    public void test3() {
        result.setText("");
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }


}
