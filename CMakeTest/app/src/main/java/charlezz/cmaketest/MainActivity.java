package charlezz.cmaketest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import charlezz.nativemodule.NativeInterface;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("native-lib");
        System.loadLibrary("native-module");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

        TextView tv2 = (TextView) findViewById(R.id.sample_text2);
        tv2.setText(new NativeInterface().hiThere());
    }

    public native String stringFromJNI();
}
