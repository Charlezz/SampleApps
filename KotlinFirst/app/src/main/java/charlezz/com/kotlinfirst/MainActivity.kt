package charlezz.com.kotlinfirst

import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.sdk25.coroutines.onClick

class MainActivity : AppCompatActivity(), AnkoLogger {


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        val view = verticalLayout {
//            val name = editText()
//            button("Say Hello"){
//                onClick{
//                    toast("Hello ${name.text}")
//                }
//            }
//        }
//        setContentView(view)



        MyActivityUI().setContentView(this)
        Log.e(TAG, MyClass.getInstance().hello)

        bg {
            if (mainLooper == Looper.myLooper()) {
                Log.e(TAG, "MainThread")
            } else {
                Log.e(TAG, "Not MainThread")
            }
        }

        async(kotlinx.coroutines.experimental.android.UI) {
            var count =0
            for (i in 1..10) {

                bg {
                    count ++


                    Log.e(TAG, "go sleep")
                    Thread.sleep(5000)
                    Log.e(TAG, "end sleep")
                }.await()

                if (mainLooper == Looper.myLooper()) {
                    Log.e(TAG, "MainThread"+count)
                } else {
                    Log.e(TAG, "Not MainThread1"+count)
                }



            }

            Log.e(TAG, "finished")
        }


    }

    private fun someMethod() {
    }
}

class MyActivityUI : AnkoComponent<MainActivity> {

    override fun createView(ui: AnkoContext<MainActivity>) = ui.apply {
        verticalLayout {
            val name = editText()
            button("Say Hello") {
                onClick { ctx.toast("Hello, ${name.text}!") }
            }
            val tv = textView() {
                text = "hello worl2d"
                textSize = 30.0f
            }

        }
    }.view
}