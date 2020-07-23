package com.ando.jetpack.db

import android.content.Context
import androidx.room.Room

/**
 * Title: $
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  15:15
 */
object DaoHelper {

    fun getDao(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "database-name").build()


}