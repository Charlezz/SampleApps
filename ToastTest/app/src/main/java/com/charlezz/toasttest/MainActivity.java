package com.charlezz.toasttest;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT);
                ImageView iv = new ImageView(MainActivity.this);
                toast.set
                iv.setImageResource(R.drawable.circle);
                toast.setView(iv);
                toast.show();
                toast.setGravity(Gravity.CENTER, -100, 0);

            }
        });

    }

    class MyToast extends Toast {

        public MyToast(Context context) {
            super(context);
        }

        @Override
        public View getView() {
            ImageView iv = new ImageView(MainActivity.this);
            iv.setImageResource(R.mipmap.ic_launcher_round);
            return iv;
        }
    }
}
