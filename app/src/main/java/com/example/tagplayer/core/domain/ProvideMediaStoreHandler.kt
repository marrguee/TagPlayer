package com.example.tagplayer.core.domain

import com.example.tagplayer.core.data.MediaStoreHandler

interface ProvideMediaStoreHandler {
    fun mediaStoreHandler() : MediaStoreHandler
}