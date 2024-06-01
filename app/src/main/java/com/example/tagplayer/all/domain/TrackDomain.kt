package com.example.tagplayer.all.domain

import android.net.Uri

data class TrackDomain(
    private val id: Long,
    private val title: String,
    private val duration: Long,
    private val uri: Uri
) : Map {
    override fun <T> map(modelMapper: ModelMapper<T>): T =
        modelMapper.map(id, title, duration, uri)
}