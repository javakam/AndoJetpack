package com.ando.jetpack.kotlin

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.ando.jetpack.MainActivity

class P_01 {

    fun demo(): Unit {
        System.out.println("hello")
        println("hello¬")
        //
        var firstName = "a"
        val lastName = "a";
        //
        var name2: String?
        name2 = null
        //
        name2?.let { val len = name2.length }
        val len = name2?.length
        //
        var fullName = "Name is $firstName $lastName"
        //
        var text =
            """
            |第一行
            |第二行
            |第三行。。。
   |
            """.trimMargin()
        //
        var sanMu = if (2 > 1) "2>1" else "1>2"
        val msg: String? = null
        //
        val activity: MainActivity = MainActivity()
        if (activity is AppCompatActivity) {
            var car = activity as FragmentActivity
            //if activity is null
            var carNoNull = activity as? FragmentActivity
            var carNoNull1 = activity as FragmentActivity?

            //implicit 隐式转换
            var car2 = activity
            if (activity is FragmentActivity) {
                var carNull = activity
            }
        }
        //
        var score = 1
        if (score in 0..18) {
        }
        var grade = when (score) {
            0, 5 -> "Excellent"
            in 6..8 -> "Good"
            9, 10 -> "Ok"
            else -> "Fail"
        }


        // Fast :  https://blog.csdn.net/github_33304260/article/details/80343514
        // https://github.com/MindorksOpenSource/from-java-to-kotlin


    }
}