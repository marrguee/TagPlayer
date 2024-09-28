package com.example.tagplayer.search.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.main.presentation.ItemUiListener
import com.example.tagplayer.main.presentation.MyView
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType

data class SongSearchUi(
    private val id: Long,
    private val title: String,
    private val duration: String
) : ItemUiListener, HandleTap {

    override fun same(other: CompareContent) = other.compare(title)

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun toString() = title
    override fun compare(otherId: Long) = false

    override fun compare(otherDate: String) = otherDate == title
    override fun compare(otherBoolean: Boolean): Boolean = false

    override fun bind(vararg views: MyView) {
        views[0].title(title)
        views[1].title(duration)
    }

    override fun type(): ItemUiListenerType = ItemUiListenerType.SongListenerType
}