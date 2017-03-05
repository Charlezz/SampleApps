package com.charlezz.backendlesstest.network;

import com.charlezz.backendlesstest.data.RetrieveUsersResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by Charles on 2017. 3. 5..
 */

public interface RetrieveUsers {
    @Headers({
            NetworkManager.MY_APP_ID,
            NetworkManager.REST_SECRET_KEY,
            NetworkManager.REST
    })
    @GET("Users")
    Call<RetrieveUsersResult> getResult();
}
