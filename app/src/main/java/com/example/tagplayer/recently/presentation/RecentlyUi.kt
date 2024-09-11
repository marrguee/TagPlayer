package com.example.tagplayer.recently.presentation

import com.example.tagplayer.core.domain.CompareContent
import com.example.tagplayer.core.domain.HandleTap
import com.example.tagplayer.main.presentation.ItemUiListener
import com.example.tagplayer.main.presentation.generic_adapter.types.ItemUiListenerType
import com.example.tagplayer.main.presentation.MyView

interface RecentlyUi : ItemUiListener {

    data class RecentlySongUi(
        private val id: Long,
        private val title: String,
        private val duration: String
    ) : RecentlyUi, HandleTap {

        override fun type() = ItemUiListenerType.RecentlyListenerType

        override fun bind(vararg views: MyView) {
            views[0].title(title)
            views[1].title(duration)
        }

        override fun same(other: CompareContent) = other.compare(id)

        override fun compare(otherId: Long) = id == otherId

        override fun compare(otherDate: String) = false
        override fun compare(otherBoolean: Boolean): Boolean = false

        override fun tap(listener: (Long) -> Unit) {
            listener.invoke(id)
        }
    }

    data class DateUi(
        private val date: String
    ) : RecentlyUi {

        override fun bind(vararg views: MyView) {
            views[0].title(date)
        }

        override fun same(other: CompareContent) = other.compare(date)

        override fun compare(otherId: Long) = false

        override fun compare(otherDate: String) = otherDate == date
        override fun compare(otherBoolean: Boolean): Boolean = false

        override fun type(): ItemUiListenerType = ItemUiListenerType.RecentlyDateListenerType
    }
}

