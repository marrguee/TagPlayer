package com.example.tagplayer.all.data

import androidx.room.Entity

@Entity("Tracks")
class Tracks(
    private val id: Int,
    private val title: String,
    private val duration: Long,
    private val uri: String
) {

}