package com.charlezz.backendlesstest.network.call;

import com.charlezz.backendlesstest.data.User;
import com.charlezz.backendlesstest.network.NetworkManager;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Charles on 2017. 2. 6..
 */
public interface PostUser {
    @Headers({
            NetworkManager.MY_APP_ID,
            NetworkManager.SECRET_KEY_FOR_REST,
            NetworkManager.CONTENT_TYPE_JSON,
            NetworkManager.APPLICATION_TYPE_REST
    })

    @POST("Users")
    Call<User> getResult(@Body User user);
}

