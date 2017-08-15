package charlezz.com.duplicatedfragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 05/07/2017.
 */

public class UIFragment extends Fragment {

    public static final String TAG = UIFragment.class.getSimpleName();

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ui_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.left_top)
    public void onLeftTopClick() {
        Log.e(TAG, "onLeftTopClick2");
    }

    @OnClick(R.id.left_bottom)
    public void onLeftBottomClick() {
        Log.e(TAG, "onLeftBottomClick2");
    }

    @OnClick(R.id.right_bottom)
    public void onRightBottomClick() {
        Log.e(TAG, "onRightBottomClick2");
    }

    @OnClick(R.id.right_top)
    public void onRightTopClick() {
        Log.e(TAG, "onRightTopClick2");
    }

    @OnClick(R.id.center)
    public void onCenterClick() {
        Log.e(TAG, "onCenterClick2");
    }
}
