package charlezz.com.duplicatedfragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 05/07/2017.
 */

public class VideoFragment extends Fragment {
    private static final String SAMPLE_VIDEO_PATH = "http://ia902205.us.archive.org/31/items/Unexpect2001/Unexpect2001_512kb.mp4";

    public static final String TAG = VideoFragment.class.getSimpleName();

    Unbinder unbinder;

    @BindView(R.id.videoView)
    VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        unbinder = ButterKnife.bind(this, view);
        videoView.setVideoPath(SAMPLE_VIDEO_PATH);
        videoView.start();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.left_top)
    public void onLeftTopClick() {
        Log.e(TAG, "onLeftTopClick");
    }

    @OnClick(R.id.left_bottom)
    public void onLeftBottomClick() {
        Log.e(TAG, "onLeftBottomClick");
    }

    @OnClick(R.id.right_bottom)
    public void onRightBottomClick() {
        Log.e(TAG, "onRightBottomClick");
    }

    @OnClick(R.id.right_top)
    public void onRightTopClick() {
        Log.e(TAG, "onRightTopClick");
    }

    @OnClick(R.id.center)
    public void onCenterClick() {
        Log.e(TAG, "onCenterClick");
    }
}
