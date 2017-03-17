package com.charlezz.backendlesstest.network;

import com.charlezz.backendlesstest.data.DeleteResult;
import com.charlezz.backendlesstest.data.User;
import com.charlezz.backendlesstest.data.UserResult;
import com.charlezz.backendlesstest.network.call.DeleteUser;
import com.charlezz.backendlesstest.network.call.GetUserByEmail;
import com.charlezz.backendlesstest.network.call.GetUsers;
import com.charlezz.backendlesstest.network.call.PostUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Charles on 2017. 2. 6..
 */
public class NetworkManager {

    private static final String KEY_APPLICATION_ID = "application-id";
    private static final String KEY_SECRET_KEY = "secret-key";
    private static final String KEY_USER_TOKEN = "user-token";
    private static final String KEY_CONTENT_TYPE = "Content-Type";
    private static final String KEY_APPLICATION_TYPE = "application-type";

    private static final String APPLICATION_JSON = "application/json";

    public static final String BASE_URL = "http://api.backendless.com/v1/data/";
    public static final String MY_APP_ID = KEY_APPLICATION_ID + ": 7E81F018-2593-5454-FF0A-FFB33DEA2C00";
    public static final String SECRET_KEY_FOR_REST = KEY_SECRET_KEY + ": 3CFB754E-6735-17A2-FF6E-94F0A0B9C200";
    public static final String CONTENT_TYPE_JSON = KEY_CONTENT_TYPE + ":" + APPLICATION_JSON;
    public static final String APPLICATION_TYPE_REST = KEY_APPLICATION_TYPE + ": APPLICATION_TYPE_REST";


    public static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager ourInstance = new NetworkManager();

    public static NetworkManager getInstance() {
        return ourInstance;
    }

    private Retrofit retrofitWithGson;
    private Retrofit retrofit;

    private NetworkManager() {
        retrofitWithGson = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public void getAllUsers(Callback<UserResult> callBack) {
        GetUsers users = retrofitWithGson.create(GetUsers.class);
        Call<UserResult> result = users.getResult();
        result.enqueue(callBack);
    }

    public void getUserByEmail(String email, Callback<UserResult> callBack) {
        GetUserByEmail users = retrofitWithGson.create(GetUserByEmail.class);
        Call<UserResult> result = users.getResult("email=\'" + email + "\'");
        result.enqueue(callBack);
    }

    public void postUser(User user, Callback<User> callback) {
        PostUser postUser = retrofitWithGson.create(PostUser.class);
        Call<User> result = postUser.getResult(user);
        result.enqueue(callback);
    }

    public void deleteUser(String objId, Callback<DeleteResult> callback) {
        DeleteUser deleteUser = retrofitWithGson.create(DeleteUser.class);
        Call<DeleteResult> result = deleteUser.getResult(objId);
        result.enqueue(callback);
    }
}
