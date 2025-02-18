package com.example.tagplayer.home.domain

interface HomeRecently<T> {
    suspend fun croppedRecently(): List<T>
}