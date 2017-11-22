package net.ddns.oksisi2.app1

import android.content.ContentProvider
import android.content.ContentValues
import android.content.ContentValues.TAG
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

    lateinit var dbHelper:MyDBHelper

    val MEMO = 0
    val MEMO_ID = 1


    private var matcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun onCreate(): Boolean {
        Log.e(TAG, "onCreate")
        dbHelper = MyDBHelper(context)
        matcher.addURI(context.packageName, MyDBHelper.TABLE, MEMO)
        matcher.addURI(context.packageName, "${MyDBHelper.TABLE}/#", MEMO_ID)

        return false
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        Log.e(TAG, "query")

        dbHelper.readableDatabase.query()y(MyDBHelper.TABLE,projection,selection, selectionArgs, )
        return null
    }

    override fun getType(uri: Uri): String? {
        Log.e(TAG, "getType")
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        Log.e(TAG, "insert")
        dbHelper.writableDatabase.insert(MyDBHelper.TABLE, null, values)
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        Log.e(TAG, "delete")
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        Log.e(TAG, "update")
        return 0
    }
}

class MyDBHelper(context:Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object {
        val DB_NAME = "forecast.db"
        val DB_VERSION = 1

        val TABLE = "Memo"

        val ID:String = "_id"
        val NAME:String = "Name"
        val CONTENT:String = "String"
        val TIMESTAMP:String = "Timestamp"

    }

    val DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE + " (" +
                    "${ID} integer PRIMARY KEY autoincrement," +
                    "${NAME} text,"+
                    "${CONTENT} text,"+
                    "${TIMESTAMP} integer" +
                    ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DATABASE_CREATE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

}
