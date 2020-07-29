package com.ando.jetpack.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Title:
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/29  14:42
 */
@Entity
data class Book(
    @ColumnInfo(name = "id") @PrimaryKey val id: Long,
    @ColumnInfo(name = "ownerUserId") val ownerUserId: Long?
)