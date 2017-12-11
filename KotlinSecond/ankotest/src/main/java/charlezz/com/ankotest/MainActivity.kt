package charlezz.com.ankotest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.simpleName


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        foo()
    }

    fun foo() {
        val ints: IntArray = intArrayOf(0,1,2,3)
        ints.forEach {
            if (it == 0) return // nonlocal return from inside lambda directly to the caller of foo()
            Log.e(TAG, "foo" + it)
        }
        Log.e(TAG, "end of foo")
    }

    fun foo2() {
        val ints: IntArray = intArrayOf(5)

        ints.forEach lit@ {
            if (it == 0) return@lit
            Log.e(TAG, "foo2" + it)
        }

        Log.e(TAG, "end of foo2")
    }

    fun foo3() {
        val ints: IntArray = intArrayOf(5)
        ints.forEach {
            if (it == 0) return@forEach
            Log.e(TAG, "foo3" + it)
        }
        Log.e(TAG, "end of foo3")
    }

    fun foo4() {
        val ints: IntArray = intArrayOf(5)
        ints.forEach(fun(value: Int) {
            if (value == 0) return  // local return to the caller of the anonymous fun, i.e. the forEach loop
            Log.e(TAG, "foo4" + value)
        })
        Log.e(TAG, "end of foo4")
    }
    fun foo5(){
        val x =arrayOf("a","b","c")
        x.forEach {
            if(it == "b") return
            Log.e(TAG,"$it")
        }
        Log.e(TAG,"after foreach")
    }
}
