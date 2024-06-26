package com.example.tagplayer.all.presentation

import android.widget.TextView
import com.example.tagplayer.core.domain.Bind
import com.example.tagplayer.core.domain.CompareItem
import com.example.tagplayer.core.domain.Tap

data class SongUi(
    private val id: Long,
    private val title: String,
    private val duration: String
) : Bind, Tap, CompareItem<SongUi> {
    override fun bind(titleView: TextView, durationView: TextView) {
        titleView.text = title
        durationView.text = duration
    }

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun same(other: SongUi) =
        other.id == id && other.title == title && other.duration == duration

}