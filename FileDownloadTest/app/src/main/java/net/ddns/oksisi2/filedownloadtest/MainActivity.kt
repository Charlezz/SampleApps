package net.ddns.oksisi2.filedownloadtest

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.support.v4.content.PermissionChecker
import android.support.v7.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if(permissionCheck == PermissionChecker)


        Manifest.permission.WRITE_EXTERNAL_STORAGE

        var retrofit = Retrofit.Builder()
                .baseUrl("https://www.google.com.au")
                .build()

        var fileDownload = retrofit.create(IFileDownload::class.java)

        var call = fileDownload.IFileDownload("googlelogo_color_272x92dp.png")

        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
            }

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {

                var imageFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "image_download.png")

                var bos = BufferedOutputStream(FileOutputStream(imageFile))
                bos.write(response?.body()?.bytes())
                bos.flush()
                bos.close()
            }
        })


    }
}
