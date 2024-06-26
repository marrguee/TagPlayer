package com.example.tagplayer.history.presentation

import android.widget.TextView
import com.example.tagplayer.core.domain.CompareItem
import com.example.tagplayer.core.domain.Tap

abstract class HistoryUi : Tap, CompareItem<HistoryUi> {
    abstract fun bind(titleView: TextView, durationView: TextView? = null)
    data class Song(
        private val id: Long,
        private val title: String,
        private val duration: String
    ) : HistoryUi() {
        override fun bind(titleView: TextView, durationView: TextView?) {
            titleView.text = title
            durationView!!.text = duration
        }

        override fun tap(listener: (Long) -> Unit) {
            listener.invoke(id)
        }

        override fun same(other: HistoryUi) = try {
            val otherSong = other as Song
            id == otherSong.id && title == otherSong.title && duration == otherSong.duration
        }
        catch (e: Exception) { false }
    }

    data class Date(
        private val date: String
    ) : HistoryUi() {
        override fun bind(titleView: TextView, durationView: TextView?) {
            titleView.text = date
        }

        override fun tap(listener: (Long) -> Unit) = Unit
        override fun same(other: HistoryUi) = try {
            (other as Date).date == date
        }
        catch (e: Exception) { false }
    }
}