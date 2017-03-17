package com.charlezz.backendlesstest.network.call;

import com.charlezz.backendlesstest.data.UserResult;
import com.charlezz.backendlesstest.network.NetworkManager;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Charles on 2017. 3. 5..
 */

public interface GetUsers {
    @Headers({
            NetworkManager.MY_APP_ID,
            NetworkManager.SECRET_KEY_FOR_REST,
            NetworkManager.APPLICATION_TYPE_REST
    })
    @GET("Users")
    Call<UserResult> getResult();
}
