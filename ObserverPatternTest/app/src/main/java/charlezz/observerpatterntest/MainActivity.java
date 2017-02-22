package charlezz.observerpatterntest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btn;

    private ArrayList<OnCustomClickListener> mOnCustomClickListener = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (OnCustomClickListener listener : mOnCustomClickListener) {
                    listener.onClick();
                }
            }
        });

        Subscriber subscriber1 = new Subscriber("subscriber1");
        Subscriber subscriber2 = new Subscriber("subscriber2");

        addCustomClickListener(subscriber1);
        addCustomClickListener(subscriber2);

    }

    public void addCustomClickListener(OnCustomClickListener listener) {
        mOnCustomClickListener.add(listener);
    }

    public void removeCustomClickListener(OnCustomClickListener listener) {
        mOnCustomClickListener.remove(listener);
    }


}
