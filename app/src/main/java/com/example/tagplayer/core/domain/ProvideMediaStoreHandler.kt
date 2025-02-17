package com.example.tagplayer.core.domain

import com.example.tagplayer.home.data.HandleMediaStore

interface ProvideMediaStoreHandler {
    fun mediaStoreHandler() : HandleMediaStore
}