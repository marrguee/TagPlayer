package com.example.tagplayer.filter_by_tags.domain

import com.example.tagplayer.filter_by_tags.presentation.FilterUi

data class TagFilterDomain(
    private val id: Long,
    private val title: String,
    private val color: String,
) {
    fun mapToUi() = FilterUi(id, title, color)
}
