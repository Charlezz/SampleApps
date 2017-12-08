package net.ddns.oksisi2.filedownloadtest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 08/12/2017.
 */


interface IFileDownload {

    @GET("/images/branding/googlelogo/1x/{fileName}")
    fun IFileDownload(@Path("fileName") fileName: String): Call<ResponseBody>
}