package net.ddns.oksisi2.app2

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.ddns.oksisi2.app1.IRemoteService

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    var mService: IRemoteService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind_remote.setOnClickListener {
            Log.e(TAG,"bindRemote Clicked")
            var intent = Intent()
            intent.setComponent(ComponentName("net.ddns.oksisi2.app1", "net.ddns.oksisi2.app1.RemoteService"))
            bindService(intent, onServiceConnection, Context.BIND_AUTO_CREATE)
        }


        add.setOnClickListener{
            Log.e(TAG,"add Clicked")
           Log.e(TAG,"${mService?.add(1)}")
        }

        get_data.setOnClickListener{

        }
    }

    var onServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.e(TAG, "onServiceConnected")
            mService = IRemoteService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.e(TAG, "onServiceDisconnected")
        }
    }
}
