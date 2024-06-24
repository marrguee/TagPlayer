package com.example.tagplayer.all.presentation

import android.widget.TextView

data class SongUi(
    private val id: Long,
    private val title: String,
    private val duration: String
) : Bind, Tap {
    override fun bind(titleView: TextView, durationView: TextView) {
        titleView.text = title
        durationView.text = duration
    }

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

}