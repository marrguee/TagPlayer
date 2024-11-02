package com.example.tagplayer.core.domain

import com.example.tagplayer.core.MediaScannerFinishedReceiver

interface ProvideMediaReceiver {
    fun mediaReceiver() : MediaScannerFinishedReceiver
}