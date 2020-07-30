package com.ando.jetpack.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import androidx.room.RoomWarnings

data class SongWithPlaylists(
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded val song: Song,
    @Relation(
        parentColumn = "songId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val playlists: List<Playlist>
)