package com.charlezz.backendlesstest.network;

import com.charlezz.backendlesstest.data.UserResult;

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
            NetworkManager.REST_SECRET_KEY,
            NetworkManager.REST
    })
    @GET("Users")
    Call<UserResult> getResult(@Query("where") String email);


//    http://api.backendless.com/v1/data/Users?where=email%3D%27test%40test.com%27
    //http://api.backendless.com/v1/data/Users?where=&email=%3Demail%3Dtest@test.com}
}
