package com.charles.butterknifetest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @BindString(R.string.app_name)
    String title;

    @BindView(R.id.tv)
    TextView tv;

    @BindViews({R.id.editText0, R.id.editText1, R.id.editText2})
    List<EditText> editTexts;

    @BindView(R.id.button)
    Button button;


    boolean isClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Log.e(TAG,title);
        tv.setText(title);
    }

    @OnClick(R.id.button)
    public void test(Button btn) {
        btn.setText("Hello");
        isClicked = !isClicked;
        ButterKnife.apply(editTexts, new ButterKnife.Action<EditText>() {
            @Override
            public void apply(@NonNull EditText view, int index) {
                view.setEnabled(isClicked);
            }
        });
    }
}
