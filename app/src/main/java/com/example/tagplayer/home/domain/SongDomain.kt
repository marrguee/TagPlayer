package com.example.tagplayer.home.domain

import com.example.tagplayer.main.presentation.SongUi
import java.util.Locale
import java.util.concurrent.TimeUnit

data class SongDomain(
    private val id: Long,
    private val thumbnail: String?,
    private val title: String,
    private val author: String?,
    private val duration: Long
) {

    interface Mapper<T> {
        suspend fun map(
            id: Long,
            thumbnail: String?,
            title: String,
            author: String?,
            duration: Long
        ): T

        class ToPresentation: Mapper<SongUi> {
            override suspend fun map(
                id: Long,
                thumbnail: String?,
                title: String,
                author: String?,
                duration: Long
            ): SongUi {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                val formattedDuration =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                return SongUi(id, thumbnail, title, author?:String(), formattedDuration)
            }
        }
    }

    suspend fun <T> map(modelMapper: Mapper<T>): T =
        modelMapper.map(id, thumbnail, title, author, duration)
}