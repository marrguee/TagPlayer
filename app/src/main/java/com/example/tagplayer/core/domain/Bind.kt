package com.example.tagplayer.core.domain

import android.widget.TextView

interface Bind {
    fun bind(titleView: TextView, durationView: TextView)
}