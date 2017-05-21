package charlezz.gallerytest;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * Created by Charles on 2017. 5. 21..
 */

public class NetworkManager {
    public static final String TAG = NetworkManager.class.getSimpleName();

    public static final String APPLICATION_ID = "5C71ED01-BB98-25F3-FFF4-47BA4F7FC100";
    public static final String REST_API_KEY = "B1A74747-EFBA-4437-FF31-0916E361BA00";
    private static final String BASE_URL = "https://api.backendless.com/";
    private static NetworkManager ourInstance;

    public static NetworkManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new NetworkManager();
        }
        return ourInstance;
    }

    private NetworkManager() {
    }


    public void uploadImage(final File file, final OnProgressListener onProgressListener, Callback<ResponseBody> responseBodyCallback) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).build();
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("multipart/form-data");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                long contentLength = file.length();
                int BUFFER_SIZE = 1024 * 8;//8k
                byte[] buffer = new byte[BUFFER_SIZE];
                FileInputStream fis = new FileInputStream(file);
                int byteCount;
                long uploadedCount = 0;

                while ((byteCount = fis.read(buffer)) != -1) {
                    sink.write(buffer);
                    uploadedCount += byteCount;
                    if (onProgressListener != null) {
                        onProgressListener.onProgress((int) ((uploadedCount * 100) / contentLength));
                    }
                }

                if (onProgressListener != null) {
                    onProgressListener.onFinish();
                }

            }
        });

        Call<ResponseBody> call = retrofit.create(ImageUploadInterface.class).upload(body, file.getName());
        call.enqueue(responseBodyCallback);
    }

}
