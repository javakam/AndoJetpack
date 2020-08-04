package com.ando.jetpack.room.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ando.jetpack.room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  15:13
 */
@Database(entities = [User::class, Book::class, Playlist::class, Song::class, PlaylistSongCrossRef::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    private class UserDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val userDao = database.userDao()

                    // Delete all content here.
                    userDao.deleteAll()

                    //延迟一秒
                    //delay(1000)

                    // Add sample users.
                    var user =
                        User(
                            uid = null,
                            nickName = "xiaobao"
                        )
                    userDao.add(user)
                    user = User(
                        uid = null,
                        nickName = "jingang"
                    )
                    userDao.add(user)

                }
            }
        }
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                    "CREATE TABLE t_user_new (uid INTEGER,nick_name TEXT,first_name TEXT, last_name TEXT, PRIMARY KEY(uid))"
                )

                // Copy the data
                database.execSQL(
                    "INSERT INTO t_user_new (uid,first_name,last_name) SELECT uid, first_name, last_name FROM t_user"
                )
                // Remove the old table
                database.execSQL("DROP TABLE t_user")
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE t_user_new RENAME TO t_user")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    //.addMigrations(MIGRATION_1_2)
                    .addCallback(
                        UserDatabaseCallback(
                            scope
                        )
                    )
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}