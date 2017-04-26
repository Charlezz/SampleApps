package charlezz.javacvtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.bytedeco.javacv.AndroidFrameConverter;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/test.mp4", 500, 281);

        try {
            recorder.start();
            String[] files = getAssets().list("bus2");
            for (String file : files) {
                Log.e(TAG, file);
                InputStream is = getAssets().open("bus2/" + file);
                if (is != null) {
                    Log.e(TAG, "is is null");
                }
                Bitmap bm = BitmapFactory.decodeStream(is);
                Frame frame = new AndroidFrameConverter().convert(bm);
                bm.recycle();
                is.close();
                recorder.record(frame);

            }
            recorder.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    long projectID = mAdapter.getProjectId(position);
//    ArrayList<PhotoSQL> list = SQLManager.getInstance().getPhotoList(projectID);
//
//    Bitmap bm = BitmapFactory.decodeFile(list.get(0).getPath());
//
//    int width = bm.getWidth();
//    int height = bm.getHeight();
//
//                                bm.recycle();
//    String ROOTPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
//    FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(ROOTPATH + "/Download/test.mp4", width, height);
//                                recorder.setFrameRate(3);
//                                recorder.setFormat("mp4");
//                                recorder.setAudioCodec(avcodec.AV_CODEC_ID_H264);
//                                try {
//        recorder.start();
//        for (PhotoSQL photo : list) {
//            String path = photo.getPath();
//            Log.e(TAG, "path:" + path);
//            Bitmap bmp = BitmapFactory.decodeFile(path);
//            Frame frame = new AndroidFrameConverter().convert(bmp);
//            bmp.recycle();
//            recorder.record(frame);
//        }
//        recorder.stop();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
}


