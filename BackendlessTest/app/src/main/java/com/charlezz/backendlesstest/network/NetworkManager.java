package com.charlezz.backendlesstest.network;

import com.charlezz.backendlesstest.data.RetrieveUsersResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Charles on 2017. 2. 6..
 */
public class NetworkManager {

    public static final String KEY_APPLICATION_ID = "application-id";
    public static final String KEY_SECRET_KEY = "secret-key";
    public static final String KEY_USER_TOKEN = "user-token";
    public static final String KEY_CONTENT_TYPE = "Content-Type";
    public static final String KEY_APPLICATION_TYPE = "application-type";

    public static final String BASE_URL = "http://api.backendless.com/v1/data/";
    public static final String MY_APP_ID = KEY_APPLICATION_ID + ": 7E81F018-2593-5454-FF0A-FFB33DEA2C00";
    public static final String REST_SECRET_KEY = KEY_SECRET_KEY + ": 3CFB754E-6735-17A2-FF6E-94F0A0B9C200";

    public static final String APPLICATION_JSON = "application/json";
    public static final String REST = KEY_APPLICATION_TYPE + ": REST";

    public static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private NetworkManager() {
    }


    public void getAllUsers(Callback<RetrieveUsersResult> callBack) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrieveUsers users = retrofit.create(RetrieveUsers.class);
        Call<RetrieveUsersResult> result = users.getResult();
        result.enqueue(callBack);
    }
}
