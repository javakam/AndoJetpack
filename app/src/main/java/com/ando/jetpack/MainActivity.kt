package com.ando.jetpack

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ando.jetpack.room.User
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_USER_ADD = 1

    private lateinit var userViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                outRect.set(0, 2, 0, 2)
            }
        }, 0)

        //https://stackoverflow.com/questions/44194579/android-viewmodel-has-no-zero-argument-constructor
        userViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        //or
//        val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
//        userViewModel = ViewModelProvider(this,factory).get(MainActivityViewModel::class.java)

        userViewModel.allUsers.observe(this, Observer { users ->
            users?.let { adapter.setUsers(it) }
        })

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewUserActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_USER_ADD)
        }

        /*
        val user = User(System.currentTimeMillis(), "Changbao", "Ma")
         GlobalScope.launch(Dispatchers.Main) {
             withContext(Dispatchers.IO) {
                 AppDatabase.getDatabase(applicationContext, userViewModel.viewModelScope).userDao()
                     .add(user)
             }
             Log.w("coroutines", "插入成功!")
         }
         */

        GlobalScope.launch(Dispatchers.Main) {
            Log.w("coroutines", "launch ${Thread.currentThread().name}")
            val bitmap = getSuspendImage()
            Log.w("coroutines", "launch getImage Ok ${bitmap.width}")

        }

        //(Int::toFloat)(1)
        //Int::toFloat.invoke(1)
        //1.toFloat()

        //1.toFloat().invoke()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_USER_ADD && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(NewUserActivity.EXTRA_REPLY)?.let {
                val word = User(null, it, null,null)
                userViewModel.insert(word)
            }
        } else {
            Toast.makeText(applicationContext, "内容不能为空!", Toast.LENGTH_LONG).show()
        }
    }

    //withContext , delay
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