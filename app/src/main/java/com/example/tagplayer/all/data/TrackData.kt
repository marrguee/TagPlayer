package com.example.tagplayer.all.data

import android.net.Uri
import androidx.room.Entity
import com.example.tagplayer.all.domain.Map
import com.example.tagplayer.all.domain.ModelMapper

data class TrackData(
    private val id: Long,
    private val title: String,
    private val duration: Long,
    private val uri: Uri
) : Map {

    override fun <T> map(modelMapper: ModelMapper<T>): T =
        modelMapper.map(id, title, duration, uri)

}