package com.ando.jetpack.db

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Title: $
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  15:13
 */
@Database(entities = arrayOf(User::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}