package net.ddns.oksisi2.app2

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import net.ddns.oksisi2.app1.IRemoteService


class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName

    var mService: IRemoteService? = null

    val TABLE_MEMO = "Memo"

    val ID: String = "_id"
    val NAME: String = "Name"
    val CONTENT: String = "String"
    val TIMESTAMP: String = "Timestamp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bind_remote.setOnClickListener {
            Log.e(TAG, "bindRemote Clicked")
            var intent = Intent()
            intent.setComponent(ComponentName("net.ddns.oksisi2.app1", "net.ddns.oksisi2.app1.RemoteService"))
            bindService(intent, onServiceConnection, Context.BIND_AUTO_CREATE)
        }


        add.setOnClickListener {
            Log.e(TAG, "add Clicked")
            Log.e(TAG, "${mService?.add(1)}")
        }

        save.setOnClickListener {
            var uri = Uri.parse("content://net.ddns.oksisi2.app1/Memo")

            var values = ContentValues()
            values.put(NAME, System.currentTimeMillis().toString())
            values.put(CONTENT, input.text.toString())
            var resultUri = contentResolver.insert(uri, values)
        }

        delete.setOnClickListener {
            var uri = Uri.parse("content://net.ddns.oksisi2.app1/Memo/${id.text.toString()}")
            var result = contentResolver.delete(uri, null, null)
        }

        update.setOnClickListener {
            var uri = Uri.parse("content://net.ddns.oksisi2.app1/Memo/${id.text.toString()}")
            var values = ContentValues()
            values.put(CONTENT, input.text.toString())
            var result = contentResolver.update(uri, values, null, null)
        }
        query.setOnClickListener {
            var uri = Uri.parse("content://net.ddns.oksisi2.app1/Memo/${id.text.toString()}")
            var cursor = contentResolver.query(uri, null, null, null, null)
            cursor.moveToFirst()

            var id = cursor.getString(cursor.getColumnIndex(ID))
            var name = cursor.getString(cursor.getColumnIndex(NAME))
            var content = cursor.getString(cursor.getColumnIndex(CONTENT))
            var timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP))

            tv.text = "${id}\n${name}\n${content}\n${timestamp}\n\n"


        }
        query_all.setOnClickListener {
            var uri = Uri.parse("content://net.ddns.oksisi2.app1/Memo/")
            var cursor = contentResolver.query(uri, null, null, null, null)

            var sb = StringBuilder()

            while(cursor.moveToNext()){
                var id = cursor.getString(cursor.getColumnIndex(ID))
                var name = cursor.getString(cursor.getColumnIndex(NAME))
                var content = cursor.getString(cursor.getColumnIndex(CONTENT))
                var timestamp = cursor.getString(cursor.getColumnIndex(TIMESTAMP))

                sb.append("${id}\n${name}\n${content}\n${timestamp}\n\n")
            }

            tv.text = sb.toString()
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
