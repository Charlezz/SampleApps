package com.charlezz.retrofit2test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ProgressBar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.progres)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.download)
    public void download() {
        new DownloadTask().execute();
    }

    class DownloadTask extends AsyncTask<Void, Integer, Void> {
        long fileSize;
        int length;
        long count = 0;
        byte[] buf = new byte[1024 * 4];
        int oldProgress = -1;

        long startTime;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file.zip");

                FileOutputStream fos = new FileOutputStream(file);

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://download.learn2crack.com/")
                        .build();

                DownloadService downloadService = retrofit.create(DownloadService.class);
                Call<ResponseBody> request = downloadService.download();
                Response<ResponseBody> response = request.execute();
                InputStream is = response.body().byteStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024 * 8);
                fileSize = response.body().contentLength();
                while ((length = bis.read(buf)) != -1) {
                    fos.write(buf, 0, length);
                    count += length;
                    int currentProgress = (int) ((count * 100) / fileSize);
                    if (oldProgress != currentProgress) {
                        publishProgress(currentProgress);
                        oldProgress = currentProgress;
                        Log.e(TAG, "progress:" + currentProgress);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "finished in :" + ((System.currentTimeMillis() - startTime) / 1000) + "sec");
        }
    }
}
