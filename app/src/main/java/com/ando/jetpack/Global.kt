package com.ando.jetpack

import android.content.res.Resources
import android.util.TypedValue

//扩展函数
fun String.method1(i: Int) {}

fun method2(s:String,i: Int) {}

//扩展属性
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
