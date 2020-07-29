package com.ando.jetpack.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_playlist")
data class Playlist(
    @PrimaryKey val playlistId: Long,
    val userCreatorId: Long,
    val playlistName: String
)
    
