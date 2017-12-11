package charlezz.com.kotlinsecond

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_1234_1234
    val socialSecurityNumber: Long = 999L

    val hexBytes = 0xFF_EC

    val bytes = 0b11010010

    val a: Int = 1000
    val x = (1 shl 2) and 0x000FF000
    val y = 0x000FF000


    val arr = ArrayList<String>()
    val intArr = Array(5, { i -> i })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(TAG, "Message")

        for (i in 1..9) {
            for (j in 1..9) {
                Log.e(TAG, "$i x $j = ${i * j}");
            }
        }
        Log.e(TAG, "finished");

        val myIntArray = arrayOf(1, 2, 3)
        val myIntArray2 = Array(1) { i ->
            0
        }

        val myStringArray = arrayOf("hello", "world")
        val myStringArray2 = Array(3) { i ->

            when (i) {
                0 -> "helloworld"
                1 -> "hi, world"
                else -> "last"
            }
        }

        myStringArray2.filter { text ->
            true
        }


        TestB("Name")
        TestB(1, "name")



        val floatA: Float = 0f
        val floatB: Float = 1f


        Log.e(TAG,"${(floatA..floatB)}")

        var test : Array<out Any> = Array<String>(0,{i->""})

//
//        arr.add("A")
//        arr.add("TestB")
//        arr.add("C")
//
//        arr.withIndex()
//
//        for (i: String in arr) {
//            Log.e(TAG, i)
//        }
//
//
//        for (i: Int in intArr) {
//            Log.e(TAG, "$i")
//        }
//
//
//
//        Test::TAG
//
//        val c1 = Customer("Charles", "oksisi213@gmail.com", "Maxst")
//
//
//        MySingleton.getInstance().sayHello()
//        MySingleton.getInstance().sayHello()
//        MySingleton.getInstance().sayHello()
//
//        Log.e(TAG, "x:" + x)
//        Log.e(TAG, "y:" + y)
//
//        printHello()
//
//        val a = 0
//        val b = 1
//
//        if (a > b) {
//            Log.e(TAG, "a is bigger")
//        } else if (b > a) {
//            Log.e(TAG, "b is bigger")
//        } else {
//            Log.e(TAG, "b is same as a")
//        }
//
//
//        val max = if (a > b) {
//            a
//        } else {
//            b
//        }
//
//        Log.e(TAG, "$max")
//
//        when (max) {
//            0, 1 -> {
//                Log.e(TAG, "when 0")
//            }
//            1 -> {
//                Log.e(TAG, "when 1")
//            }
//            else -> {
//                Log.e(TAG, "else")
//            }
//        }
//
//
//        Log.e(TAG, "Hello?")
//
//        hello@ for (i in 1..10) {
//
//            Log.e(TAG, "i=$i")
//            for (j in 1..100) {
//                Log.e(TAG, "j=$j")
//                if (j == 50) break@hello
//            }
//        }
//
//        val numbers = listOf(1, 2, 3)
//        val numbers2: List<Int> = numbers.filter { i ->
//            if (i > 2) {
//                true
//            }
//            false
//        }
//
//
//
//
//        for (i in numbers2) {
//            Log.e(TAG, "i={$i}")
//        }


    }

    fun sum(a: Int, b: Int): Int {
        return a + b
    }


}

data class Customer(val name: String, val email: String, val company: String)
