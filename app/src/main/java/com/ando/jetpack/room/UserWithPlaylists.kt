package com.ando.jetpack.room

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.RoomWarnings

data class UserWithPlaylists(
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded val user: User,
    @Relation(
        parentColumn = "uid",
        entityColumn = "userCreatorId"
    )
    val playlists: List<Playlist>
)