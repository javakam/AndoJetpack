package com.ando.jetpack.room

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Title: User&Book 两个实体之间建立一对一关系
 * <p>
 * Description:
 * </p>
 * @author Changbao
 * @date 2020/7/29  14:46
 */
data class UserAndBook(
    @Embedded val user: User,
    @Relation(parentColumn = "uid", entityColumn = "ownerUserId")
    val book: Book
)