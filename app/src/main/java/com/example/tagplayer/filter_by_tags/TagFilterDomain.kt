package com.example.tagplayer.filter_by_tags

data class TagFilterDomain(
    private val id: Long,
    private val title: String,
    private val color: String,
) {
    fun mapToUi() = TagFilterUi(id, title, color)
}
