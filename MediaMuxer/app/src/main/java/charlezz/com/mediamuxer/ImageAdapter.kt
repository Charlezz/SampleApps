package charlezz.com.mediamuxer

import android.content.Context
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Checkable
import android.widget.FrameLayout
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import org.jetbrains.anko.imageView
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 28/11/2017.
 */
class ImageAdapter(context: Context, columnCount: Int) : BaseAdapter() {

    val TAG = ImageAdapter::class.java.simpleName

    val columnWidth: Float
    lateinit var context: Context

    var imageList: ArrayList<ImageData> = ArrayList()

    init {
        this.context = context
        columnWidth = context.resources.displayMetrics.widthPixels.toFloat() / columnCount.toFloat()
        imageList = fetchAllImages(context)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view: ImageItemView? = convertView as? ImageItemView
        if (view == null) {
            view = ImageItemView(context, columnWidth.toInt())
        }

        val data = imageList.get(position)

        Glide.with(parent?.context).load(data.contentUri).transition(DrawableTransitionOptions.withCrossFade()).into(view.iv)
        return view!!
    }

    override fun getItem(position: Int) = imageList.get(position)

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount(): Int = imageList.size

    fun fetchAllImages(context: Context): ArrayList<ImageData> {
        var imageDataList = ArrayList<ImageData>()

        var projection: Array<String> = Array(4) { position ->
            when (position) {
                0 -> MediaStore.MediaColumns.DATA
                1 -> MediaStore.MediaColumns.DISPLAY_NAME
                2 -> MediaStore.MediaColumns._ID
                3 -> MediaStore.MediaColumns.DATE_ADDED
                else -> throw IndexOutOfBoundsException("Must be within 4")
            }
        }


        var cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC"
        )

        while (cursor.moveToNext()) {
            val data = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA))
            val name = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME))
            val id = cursor.getLong(cursor.getColumnIndex(MediaStore.MediaColumns._ID))
            imageDataList.add(ImageData(id, name, data))
        }
        return imageDataList
    }

}

data class ImageData(var id: Long, var displayName: String, var contentUri: String)

class ImageItemView(context: Context, columnsWidth: Int) : FrameLayout(context), Checkable {

    private var isChecked = false
    override fun isChecked(): Boolean {
        return isChecked
    }

    override fun toggle() {
        setChecked(!isChecked)
    }

    override fun setChecked(checked: Boolean) {
        isChecked = checked
        if (isChecked) {
            iv.alpha = 0.5f
        } else {
            iv.alpha = 1.0f
        }
    }

    lateinit var iv: ImageView

    init {
        verticalLayout {
            lparams(matchParent, matchParent)
            iv = imageView {
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(matchParent, columnsWidth)
        }
    }
}