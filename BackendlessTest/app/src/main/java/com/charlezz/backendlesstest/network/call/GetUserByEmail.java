package com.charlezz.backendlesstest.network.call;

import com.charlezz.backendlesstest.data.UserResult;
import com.charlezz.backendlesstest.network.NetworkManager;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Charles on 2017. 3. 12..
 */

public interface GetUserByEmail {
    @Headers({
            NetworkManager.MY_APP_ID,
            NetworkManager.SECRET_KEY_FOR_REST,
            NetworkManager.APPLICATION_TYPE_REST
    })
    @GET("Users")
    Call<UserResult> getResult(@Query("where") String email);


}
