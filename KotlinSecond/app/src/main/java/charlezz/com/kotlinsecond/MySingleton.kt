package charlezz.com.kotlinsecond

import android.util.Log

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 17/10/2017.
 */


class MySingleton {

    companion object{
        val TAG = MySingleton::class.java.simpleName
        fun getInstance() = MySingleton()
    }


    init {
        Log.e(TAG, "MySingleton Default Constructor")
    }

    fun sayHello(){
        Log.e(TAG, "Hello from MySingleton")
    }



}