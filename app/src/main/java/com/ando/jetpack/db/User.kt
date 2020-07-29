package com.ando.jetpack.db

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  14:19
 */
@Entity(tableName = "t_user")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") var uid: Long?,
    @ColumnInfo(name = "nick_name") var nickName: String?,
    @ColumnInfo(name = "first_name") var firstName: String?,
    @ColumnInfo(name = "last_name") var lastName: String?
) {
    @Ignore
    var picture: Bitmap? = null
    //or
    // constructor() : this(0, "", "")
}