package com.example.tagplayer.all.domain

import android.net.Uri
import com.example.tagplayer.all.presentation.TrackUi
import java.util.concurrent.TimeUnit

interface ModelMapper<T> {
    fun map(id: Long, title: String, duration: Long, uri: Uri) : T

    object ToDomain : ModelMapper<TrackDomain> {
        override fun map(id: Long, title: String, duration: Long, uri: Uri) =
            TrackDomain(id, title, duration, uri)
    }

    object ToPresentation : ModelMapper<TrackUi> {
        override fun map(id: Long, title: String, duration: Long, uri: Uri): TrackUi {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            val formattedDuration = String.format("%02d:%02d", minutes, seconds)
            return TrackUi(title, formattedDuration)
        }
    }
}