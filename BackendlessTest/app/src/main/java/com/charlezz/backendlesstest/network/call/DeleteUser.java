package com.charlezz.backendlesstest.network.call;

import com.charlezz.backendlesstest.data.DeleteResult;
import com.charlezz.backendlesstest.network.NetworkManager;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Charles on 3/17/17.
 */

public interface DeleteUser {
    @Headers({
            NetworkManager.MY_APP_ID,
            NetworkManager.SECRET_KEY_FOR_REST,
            NetworkManager.APPLICATION_TYPE_REST
    })
    @DELETE("Users/{obj_id}")
    Call<DeleteResult> getResult(@Path("obj_id") String objectId);
}
