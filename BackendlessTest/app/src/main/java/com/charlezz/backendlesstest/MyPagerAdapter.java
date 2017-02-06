package com.charlezz.backendlesstest;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.charlezz.backendlesstest.list.ListFragment;
import com.charlezz.backendlesstest.signup.SignUpFragment;

/**
 * Created by Charles on 2017. 1. 29..
 */

public class MyPagerAdapter extends FragmentStatePagerAdapter {


    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ListFragment.newInstance();
            case 1:
                return SignUpFragment.newInstance();
        }

        return ListFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "List";
            case 1:
                return "SignUp";
        }
        return "";
    }
}
