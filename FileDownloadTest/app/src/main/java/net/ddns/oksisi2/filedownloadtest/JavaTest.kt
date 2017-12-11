package net.ddns.oksisi2.filedownloadtest

import retrofit2.Retrofit

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 08/12/2017.
 */

class JavaTest {

    internal fun test() {

        val retrofit = Retrofit.Builder().baseUrl("http://repo1.maven.org").build()

        val retrofitDownload = retrofit.create(IFileDownload::class.java)

        //		Call<ResponseBody> call = retrofitDownload.downloadRetrofit("retrofit-2.0.0-beta2.jar");
    }
}
