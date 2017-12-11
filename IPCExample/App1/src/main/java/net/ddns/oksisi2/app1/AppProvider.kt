package net.ddns.oksisi2.app1

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 22/11/2017.
 */


class AppProvider : ContentProvider() {
    private val TAG = AppProvider::class.java.simpleName

    lateinit var dbHelper: MyDBHelper

    val MEMO = 0
    val MEMO_ID = 1

    val PACKAGE_NAME = "net.ddns.oksisi2.app1"

    private var matcher = UriMatcher(UriMatcher.NO_MATCH)

    init {
        matcher.addURI(PACKAGE_NAME, "${MyDBHelper.TABLE_MEMO}", MEMO)
        matcher.addURI(PACKAGE_NAME, "${MyDBHelper.TABLE_MEMO}/#", MEMO_ID)
    }

    override fun onCreate(): Boolean {
        Log.e(TAG, "onCreate")
        dbHelper = MyDBHelper(context)

        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        Log.e(TAG, "query")

        return when (matcher.match(uri)) {
            MEMO -> dbHelper.writableDatabase.query(
                    MyDBHelper.TABLE_MEMO,
                    arrayOf(MyDBHelper.ID, MyDBHelper.NAME, MyDBHelper.CONTENT, MyDBHelper.TIMESTAMP),
                    null,
                    null,
                    null,
                    null,
                    "${MyDBHelper.TIMESTAMP} DESC"
            )

            MEMO_ID -> dbHelper.writableDatabase.query(
                    MyDBHelper.TABLE_MEMO,
                    arrayOf(MyDBHelper.ID, MyDBHelper.NAME, MyDBHelper.CONTENT, MyDBHelper.TIMESTAMP),
                    "${MyDBHelper.ID} = ?",
                    arrayOf(uri.lastPathSegment),
                    null,
                    null,
                    "${MyDBHelper.TIMESTAMP} DESC"
            )

            else -> {
                IllegalArgumentException("This is an Unkown URI ${uri}")
                return null
            }
        }
    }

    override fun getType(uri: Uri): String? {
        Log.e(TAG, "getType")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e(TAG, "insert")
        Log.e(TAG,"saved:${dbHelper.writableDatabase.insert(MyDBHelper.TABLE_MEMO, null, values)}")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = when (matcher.match(uri)) {
        MEMO_ID -> dbHelper.writableDatabase.delete(MyDBHelper.TABLE_MEMO, "${MyDBHelper.ID} = ?", arrayOf(uri.lastPathSegment))
        else -> {
            IllegalArgumentException("This is an Unkown URI ${uri}")
            0
        }
    }


    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        Log.e(TAG, "update")
        return when (matcher.match(uri)) {
            MEMO_ID -> dbHelper.writableDatabase.update(MyDBHelper.TABLE_MEMO, values, "${MyDBHelper.ID} = ?", arrayOf(uri.lastPathSegment))
            else -> 0
        }
    }
}

class MyDBHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    val TAG = MyDBHelper::class.java.simpleName

    companion object {
        val DB_NAME = "MyDB.db"
        val DB_VERSION = 1

        val TABLE_MEMO = "Memo"

        val ID: String = "_id"
        val NAME: String = "Name"
        val CONTENT: String = "String"
        val TIMESTAMP: String = "Timestamp"

    }

    val DATABASE_CREATE =
            "CREATE TABLE if not exists ${TABLE_MEMO} (" +
                    "${ID} integer PRIMARY KEY autoincrement," +
                    "${NAME} text," +
                    "${CONTENT} text," +
                    "${TIMESTAMP} TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")"

    override fun onCreate(db: SQLiteDatabase?) {
        Log.e(TAG,"onCreate")
        db?.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}
