package com.ando.jetpack.room

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import androidx.room.RoomWarnings

data class PlaylistWithSongs(
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playlistId",
        entityColumn = "songId",
        associateBy = Junction(PlaylistSongCrossRef::class)
    )
    val songs: List<Song>
)