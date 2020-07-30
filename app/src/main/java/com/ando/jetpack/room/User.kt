package com.ando.jetpack.room

import android.graphics.Bitmap
import androidx.room.*

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/23  14:19
 */
@SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
@Entity(tableName = "t_user")
data class User(
    @ColumnInfo(name = "uid") @PrimaryKey var uid: Long?,
    @ColumnInfo(name = "nick_name") var nickName: String?,
    @ColumnInfo(name = "first_name") var firstName: String? = null,
    @ColumnInfo(name = "last_name") var lastName: String? = null,
    @Embedded var address: Address? = null
) {
    @Ignore
    var picture: Bitmap? = null
    //or
    // constructor() : this(0, "", "")
}