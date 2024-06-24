package com.example.tagplayer.all.domain

import android.net.Uri
import com.example.tagplayer.all.presentation.SongUi
import java.util.concurrent.TimeUnit

data class SongDomain(
    private val id: Long,
    private val title: String,
    private val duration: Long,
    private val uri: Uri
) {

    interface Mapper<T> {
        fun map(id: Long, title: String, duration: Long, uri: Uri) : T

        object ToPresentation : Mapper<SongUi> {
            override fun map(id: Long, title: String, duration: Long, uri: Uri): SongUi {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                val formattedDuration = String.format("%02d:%02d", minutes, seconds)
                return SongUi(id, title, formattedDuration)
            }
        }
    }

    fun <T> map(modelMapper: Mapper<T>): T =
        modelMapper.map(id, title, duration, uri)
}