package com.ando.jetpack.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "t_song")
data class Song(
    @PrimaryKey val songId: Long,
    val songName: String,
    val artist: String
)