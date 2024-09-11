package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

data class SearchUi(private val searchRequest: String): ItemUi {

    override fun same(other: CompareContent) = other.compare(searchRequest)

    override fun toString() = searchRequest
    override fun compare(otherId: Long) = false

    override fun compare(otherDate: String) = otherDate == searchRequest
    override fun compare(otherBoolean: Boolean): Boolean = false

    override fun bind(vararg views: MyView) {

    }

    override fun type(): ItemUiType = ItemUiType.TagType
}