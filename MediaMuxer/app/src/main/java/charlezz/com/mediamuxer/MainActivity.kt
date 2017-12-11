package charlezz.com.mediamuxer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.GridView
import com.gun0912.tedpermission.PermissionListener
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {


    val TAG = MainActivity::class.java.simpleName
    val STANDARD_CELL_WIDTH: Float = 200.0f

    lateinit var gridView: GridView
    lateinit var imageAdapter: ImageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        var view : View?= null
        view?.parent



        verticalLayout {
            gridView = gridView {
                id = R.id.grid_view
                horizontalSpacing = (1f * resources.displayMetrics.scaledDensity).toInt()
                verticalSpacing = (1f * resources.displayMetrics.scaledDensity).toInt()
                padding = (1f * resources.displayMetrics.scaledDensity).toInt()
                lparams(matchParent, matchParent)
            }
        }

        PermissionManager.getInstance().checkPermission(this, object : PermissionListener {
            override fun onPermissionGranted() {
                toast("permisson granted")
            }

            override fun onPermissionDenied(deniedPermissions: ArrayList<String>?) {
                toast("permisson denied")
            }

        })

        Log.e(TAG, "onCreate: xdpi=${resources.displayMetrics.xdpi}")
        Log.e(TAG, "onCreate: 나눔=${resources.displayMetrics.xdpi / STANDARD_CELL_WIDTH}")
        Log.e(TAG, "onCreate: 올림=${Math.ceil((resources.displayMetrics.xdpi / STANDARD_CELL_WIDTH).toDouble())}")

        var columnCount = Math.ceil((resources.displayMetrics.xdpi / STANDARD_CELL_WIDTH).toDouble()).toInt()

        imageAdapter = ImageAdapter(this, columnCount)
        gridView.numColumns = columnCount
        gridView.adapter = imageAdapter

        gridView.choiceMode = GridView.CHOICE_MODE_MULTIPLE


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add(0, 0, 0, "make")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            0 -> {
                toast("Make")
                make()
            }

            else -> Log.e(TAG, "onOptionsItemSelected: default")
        }
        return super.onOptionsItemSelected(item)
    }


    fun make() {
        var array = gridView.checkedItemPositions

        var uris = ArrayList<String>()

        for (index in 0 until array.size()) {
            val key = array.keyAt(index)
            val checked = array.get(key)
            if (checked) {
                Log.e(TAG, "make: key = ${key}")
                var data = imageAdapter.getItem(key)
                uris.add(data.contentUri)
            }
        }


    }

//    fun encode(){
//        var muxer = MediaMuxer("test.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
//
//
//        var audioFormat : MediaFormat = MediaFormat.createAudioFormat()
//        var videoFormat : MediaFormat = MediaFormat.createVideoFormat()
//        muxer.addTrack(audioFormat)
//        muxer.addTrack(videoFormat)
//
//        var inputBuffer:ByteBuffer = ByteBuffer.allocate(bufferSize)
//        var finished:Boolean = false
//
//        var bufferInfo: MediaCodec.BufferInfo = MediaCodec.BufferInfo()
//
//        muxer.start()
//
//        while(!finished){
//            finished =
//        }
//
//    }

}
