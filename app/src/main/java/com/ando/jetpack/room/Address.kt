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
 * @date 2020/7/29  14:31
 */
@Entity
data class Address(
    @ColumnInfo(name = "id") @PrimaryKey val id: String?,
    @ColumnInfo(name = "street") val street: String?,
    @ColumnInfo(name = "state") val state: String?,
    @ColumnInfo(name = "city") val city: String?,
    @ColumnInfo(name = "post_code") val postCode: Int //邮政编码
)