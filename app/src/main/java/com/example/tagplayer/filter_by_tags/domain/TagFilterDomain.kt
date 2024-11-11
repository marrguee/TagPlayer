package com.example.tagplayer.filter_by_tags.domain

import com.example.tagplayer.filter_by_tags.presentation.TagFilterUi

data class TagFilterDomain(
    private val id: Long,
    private val title: String,
    private val color: String,
) {
    fun mapToUi() = TagFilterUi(id, title, color)
}
