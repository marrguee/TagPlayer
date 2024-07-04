package com.example.tagplayer.main.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandlePlayTap
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.ItemUiType
import com.example.tagplayer.main.presentation.MyView

data class SongUi(
    private val id: Long,
    private val title: String,
    private val duration: String
) : ItemUi, HandlePlayTap {

    override fun tap(listener: (Long) -> Unit) {
        listener.invoke(id)
    }

    override fun type() = ItemUiType.SongType

    override fun bind(vararg views: MyView) {
        views[0].title(title)
        views[1].title(duration)
    }

    override fun same(other: CompareContent) = other.compare(id)

    override fun compare(otherId: Long) = otherId == id
    override fun compare(otherDate: String) = false


}