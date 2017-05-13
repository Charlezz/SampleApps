package charlezz.javacvtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    int timeCount = 0;
    int count2 = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //176초

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(getAssets().open("rightnow.mp3"));
                    grabber.start();
                    int count = 0;
                    Frame grabbedFrame;
                    Log.e(TAG, "count:" + count);
                    Log.e(TAG, "grabber.getTimestamp():" + grabber.getTimestamp());
                    Log.e(TAG, "grabber.getAudioBitrate():" + grabber.getAudioBitrate());
                    Log.e(TAG, "grabber.getAudioChannels():" + grabber.getAudioChannels());
                    Log.e(TAG, "grabber.getAudioCodec():" + grabber.getAudioCodec());
                    Log.e(TAG, "grabber.getFormat():" + grabber.getFormat());
                    Log.e(TAG, "grabber.getFrameRate():" + grabber.getFrameRate());
                    Log.e(TAG, "grabber.getLengthInFrames():" + grabber.getLengthInFrames());
                    Log.e(TAG, "grabber.getLengthInTime():" + grabber.getLengthInTime());
                    Log.e(TAG, "grabber.getSampleFormat():" + grabber.getSampleFormat());
                    Log.e(TAG, "grabber.getSampleRate():" + grabber.getSampleRate());


                    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4", 500, 281, 2);
                    recorder.start();
                    recorder.setFrameRate(33333);
                    recorder.setSampleRate(grabber.getSampleRate());
                    recorder.setAudioBitrate(grabber.getAudioBitrate());
                    while ((grabbedFrame = grabber.grabSamples()) != null) {

                        InputStream is = getAssets().open("bus2/" + count + ".jpg");
                        Bitmap bm = BitmapFactory.decodeStream(is);
                        Frame frame = new AndroidFrameConverter().convert(bm);
                        bm.recycle();
                        is.close();
                        grabbedFrame.image = frame.image;
                        grabbedFrame.imageWidth = frame.imageWidth;
                        grabbedFrame.imageHeight = frame.imageHeight;
                        grabbedFrame.imageChannels = frame.imageChannels;
                        grabbedFrame.imageStride = frame.imageStride;
                        grabbedFrame.imageDepth = frame.imageDepth;
//                        grabbedFrame.timestamp = grabber.getTimestamp();
                        Log.e(TAG, "grabber.getTimestamp():" + grabber.getTimestamp());
                        recorder.setTimestamp(timeCount);
                        timeCount += 33;
                        recorder.record(grabbedFrame);
                        count++;
                        count2++;
                        if (count >= 100) {
                            count = 0;
                        }
                        if (count2 >= 200) {
                            break;
                        }
                    }
                    Log.e(TAG, "count:" + count);
                    Log.e(TAG, "grabber.getTimestamp():" + grabber.getTimestamp());
                    Log.e(TAG, "grabber.getAudioBitrate():" + grabber.getAudioBitrate());
                    Log.e(TAG, "grabber.getAudioChannels():" + grabber.getAudioChannels());
                    Log.e(TAG, "grabber.getAudioCodec():" + grabber.getAudioCodec());
                    Log.e(TAG, "grabber.getFormat():" + grabber.getFormat());
                    Log.e(TAG, "grabber.getFrameRate():" + grabber.getFrameRate());
                    Log.e(TAG, "grabber.getLengthInFrames():" + grabber.getLengthInFrames());
                    Log.e(TAG, "grabber.getLengthInTime():" + grabber.getLengthInTime());
                    Log.e(TAG, "grabber.getSampleFormat():" + grabber.getSampleFormat());
                    Log.e(TAG, "grabber.getSampleRate():" + grabber.getSampleRate());

                    recorder.stop();
                    grabber.stop();
                    grabber.release();


//            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4", 500, 281);
//            recorder.setTimestamp(1000 * 1000 * 100);
//            recorder.start();
//            for (int i = 0; i < 100; i++) {
//                Log.e(TAG, "bus2/" + i + ".jpg");
//                InputStream is = getAssets().open("bus2/" + i + ".jpg");
//                Bitmap bm = BitmapFactory.decodeStream(is);
//                Frame frame = new AndroidFrameConverter().convert(bm);
//                bm.recycle();
//                is.close();
//                recorder.record(frame);
//            }
//            recorder.stop();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                onPlay();
                Log.e(TAG, "Finished");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onPlay();
                    }
                });
            }
        });

    }

    @OnClick(R.id.play)
    public void onPlay() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4"));
        intent.setDataAndType(Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4"), "video/mp4");
        startActivity(intent);
    }
}


