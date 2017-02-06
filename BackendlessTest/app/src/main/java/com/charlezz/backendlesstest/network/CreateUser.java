package com.charlezz.backendlesstest.network;

import com.charlezz.backendlesstest.data.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Charles on 2017. 2. 6..
 */
public interface CreateUser {
    @POST("users/new")
    Call<User> createUser(@Body User user);
}

