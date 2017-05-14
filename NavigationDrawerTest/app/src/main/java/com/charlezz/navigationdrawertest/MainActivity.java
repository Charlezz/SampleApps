package com.charlezz.navigationdrawertest;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                Log.e(TAG, "onDrawerSlide:" + slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                Log.e(TAG, "onDrawerOpened");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Log.e(TAG, "onDrawerClosed");
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                Log.e(TAG, "onDrawerStateChanged");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.e(TAG, "onOptionsItemSelected");
        return super.onOptionsItemSelected(item);
    }
}
