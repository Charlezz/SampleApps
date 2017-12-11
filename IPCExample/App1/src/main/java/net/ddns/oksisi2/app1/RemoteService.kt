package net.ddns.oksisi2.app1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.util.Log

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 22/11/2017.
 */

class RemoteService : Service() {
    private val TAG = RemoteService::class.java.simpleName

    private val stub = object : IRemoteService.Stub() {
        @Throws(RemoteException::class)
        override fun add(i: Int): Int {
            Log.e(TAG, "add")
            return i + 1
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return stub
    }

    override fun onCreate() {
        super.onCreate()

        Log.e(TAG, "onCreate")
    }

}
