package charlezz.com.kotlinsecond

import android.util.Log

/**
 * Copyright 2017 Maxst, Inc. All Rights Reserved.
 * Created by Charles on 18/10/2017.
 */
open class TestB(age: Int, name: String) {

    val TAG = TestB::class.java.simpleName

    init {
        Log.e(TAG,"primary constructor")
    }

    constructor(name:String) : this(0,name){
        Log.e(TAG,"secondary constructor")
    }
    class TestC constructor(num: Int){

        val value:Number?=null

        fun test(){
            var mInt = 0
            var mLong = 0L

            mLong = mInt.toLong()

            val a: Int? = 1 // A boxed Int (java.lang.Integer)
            val b: Long? = 1 // implicit conversion yields a boxed Long (java.lang.Long)








        }
    }
}