package com.ando.jetpack

import android.app.Application
import kotlin.properties.Delegates

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/29  9:09
 */
class App : Application() {

    companion object {
        private var instance: App by Delegates.notNull<App>()
        fun getApp() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }


}