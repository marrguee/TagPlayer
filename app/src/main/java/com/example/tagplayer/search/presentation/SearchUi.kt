package com.example.tagplayer.search.presentation

data class SearchUi(private val searchRequest: String) {
    override fun toString() = searchRequest
}