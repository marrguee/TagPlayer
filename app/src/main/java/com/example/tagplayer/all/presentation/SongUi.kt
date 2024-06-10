package com.example.tagplayer.all.presentation

import android.widget.TextView

data class SongUi(
    private val title: String,
    private val duration: String
) : Bind {
    override fun bind(titleView: TextView, durationView: TextView) {
        titleView.text = title
        durationView.text = duration
    }
}