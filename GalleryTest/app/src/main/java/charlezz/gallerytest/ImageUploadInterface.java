package charlezz.gallerytest;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by Charles on 2017. 5. 21..
 */

public interface ImageUploadInterface {

    @Multipart
    @POST(NetworkManager.APPLICATION_ID + "/" + NetworkManager.REST_API_KEY + "/files/media/{filename}")
    Call<ResponseBody> upload(@Part MultipartBody.Part photo, @Path("filename") String fileName);

}
