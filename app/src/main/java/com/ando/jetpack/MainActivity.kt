package com.ando.jetpack

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import com.ando.jetpack.db.DaoHelper
import com.ando.jetpack.db.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val user = User(System.currentTimeMillis(), "11", "22")
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                DaoHelper.getDao(applicationContext).userDao().add(user)
            }
            Log.w("coroutines", "插入成功!")
        }

        (Int::toFloat)(1)
        Int::toFloat.invoke(1)
        1.toFloat()
        //1.toFloat().invoke()

        GlobalScope.launch(Dispatchers.Main) {
            Log.w("coroutines", "launch ${Thread.currentThread().name}")
            val bitmap = getSuspendImage()
            Log.w("coroutines", "launch getImage Ok")
            iv1.setImageBitmap(bitmap)

            val bitmapHalf2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width / 2, bitmap.height / 2)
            iv2.setImageBitmap(bitmapHalf2)

            val bitmapHalf3 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width / 3, bitmap.height / 3)
            iv3.setImageBitmap(bitmapHalf3)
        }

    }

    private suspend fun getSuspendImage(): Bitmap = withContext(Dispatchers.IO) {
        Log.w("coroutines", "suspend ${Thread.currentThread().name}")
        OkHttpClient()
            .newCall(
                Request.Builder()
                    .url("http://oss.tuyuing.com/TUYU/trend/20190930/trend257401569854904487.jpeg")
                    .get()
                    .build()
            )
            .execute().body?.byteStream().use {
                Log.w("coroutines", "suspend getImage Ok")
                BitmapFactory.decodeStream(it)
            }
    }

//    private suspend fun getSuspendImageDelay():Bitmap= delay(500){
//        OkHttpClient()
//                .newCall(Request.Builder()
//                        .url("https://pic.qqtn.com/up/2019-9/15690311636958128.jpg")
//                        .get()
//                        .build())
//                .execute().body?.byteStream().use {
//                    BitmapFactory.decodeStream(it)
//                }
//    }


    fun b(param: Int): String {
        a { i, s ->
            true
        }
        return ""
    }

    val d = MainActivity::b
    val e = d


    fun a(func: (Int, String) -> Boolean): (String) -> Unit = fun(s: String): Unit {}

    //----------------扩展函数


    val str: String.(Int) -> Unit = String::method1
    val str2: (String, Int) -> Unit = String::method1

    val str3: String.(Int) -> Unit = ::method2
    val str4: (String, Int) -> Unit = ::method2

    fun strTest() {
        str("hello", 1)
        str.invoke("hello", 1)
        "".method1(1)
    }

    fun strTest2() {
        fun String.method1(i: Int) {
        }

        val str: String.(Int) -> Unit = String::method1
        val str2: (String, Int) -> Unit = String::method1

        "hello".method1(100)
    }


}