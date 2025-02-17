package com.example.tagplayer.playback.presentation

import com.example.tagplayer.R
import com.example.tagplayer.core.MyView
import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.main.presentation.ItemUi
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiType

data class TagPlaybackUi(
    private val title: String,
    private val color: String,
): ItemUi {
    override fun compare(otherId: Long): Boolean = true

    override fun same(other: CompareContent): Boolean = other.compare(title)

    override fun compare(otherDate: String): Boolean = title == otherDate

    override fun compare(otherBoolean: Boolean): Boolean = true

    override fun bind(vararg views: MyView) {
        views[0].let {
            it.title(title)
            it.color(R.drawable.background_tag_oval, color)
        }
    }

    override fun type(): ItemUiType = ItemUiType.TagPlaybackType
}